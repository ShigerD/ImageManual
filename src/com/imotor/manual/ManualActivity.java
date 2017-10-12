package com.imotor.manual;

import android.app.Activity;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class ManualActivity extends Activity {
    private final String TAG = "ManualActivity";

    private final String filePath = "bootlogo/config/imgreader";
    private ImageView mImageView;
    private List<Uri> mImageUris;
    private TextView mPage;
    private int mImagePosion;
    private float downX;
    private ImageEntries imageEntries;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manual_main);
        setupView();
        init();
    }

    private void setupView() {
        mImageView = (ImageView) findViewById(R.id.image_surface_view);
        mPage = (TextView) findViewById(R.id.page_name);
    }

    private void init() {
        ImageModel imageModel = new ImageModel(getApplicationContext());
        imageEntries = imageModel.getImagePath(filePath);
        if (imageEntries == null) {
            Toast.makeText(getApplicationContext(), getString(R.string.file_no_found), Toast.LENGTH_SHORT).show();
            return;
        }
        mImageUris = imageEntries.mUriList;
        Log.d(TAG, "mImageUris---" + mImageUris);
        for (Uri uri : mImageUris) {
            Log.d(TAG, "uri==" + uri);
        }

        if (mImageUris.size() > 0) {
            mImagePosion = readLastPosion();
            updateImage();
        }
    }

    @Override
    protected void onDestroy() {
        savePosion();
        super.onDestroy();
    }

    void savePosion() {
        SharedPreferences.Editor editor = getSharedPreferences("lock", MODE_WORLD_WRITEABLE).edit();
        editor.putInt("posion", mImagePosion);
        editor.apply();
    }

    int readLastPosion() {
        SharedPreferences read = getSharedPreferences("lock", MODE_WORLD_READABLE);
        return read.getInt("posion", 1);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(imageEntries ==null){
            return true;
        }
        float x = event.getX();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = x;
                Log.d(TAG, "=======downX---" + x);
                break;
            case MotionEvent.ACTION_UP:
                Log.d(TAG, "=======upX-----" + x);
                float dx = x - downX;
                if (Math.abs(dx) > 8) {
                    int orientation = getMoveOrientation(dx);
                    switch (orientation) {
                        case 'r':
                            if (mImagePosion == 0) {
                                Toast.makeText(this, getString(R.string.the_first_page), Toast.LENGTH_SHORT).show();
                                break;
                            }
                            mImagePosion--;
                            break;
                        case 'l':
                            if (mImagePosion == mImageUris.size() - 1) {
                                Toast.makeText(this, getString(R.string.the_last_page), Toast.LENGTH_SHORT).show();
                                break;
                            }
                            mImagePosion++;
                            break;
                    }
                }
                break;
        }
        updateImage();
        return super.onTouchEvent(event);
    }

    void updateImage() {
        if(imageEntries ==null){
            return ;
        }
        mImageView.setImageURI(mImageUris.get(mImagePosion));
        mPage.setText(mImagePosion + 1 + "/" + mImageUris.size());
    }

    private int getMoveOrientation(float dx) {
        return dx > 0 ? 'r' : 'l';
    }

}
