package com.imotor.manual;

import android.app.Activity;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ManualActivity extends Activity {
    private final String TAG = "ManualActivity";

    private final String filePath = "bootlogo/config/manual";
    private ImageView mImageView;
    private List<Uri> mImageUris;
    private TextView mPage;
    private int mImagePosion;
    private float downX;
    private ImageEntries imageEntries;
    private List<View> mImageViews=new ArrayList<>();
    private ViewPager mViewPager ;
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
        mViewPager = (ViewPager) findViewById(R.id.viewpager_image);
    }

    private void init() {
        ImageModel imageModel = new ImageModel(getApplicationContext());
        imageEntries = imageModel.getImagePath(filePath);
        if (imageEntries == null) {
            Toast.makeText(getApplicationContext(), getString(R.string.file_no_found), Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        mImageUris = imageEntries.mUriList;
        Log.d(TAG, "mImageUris---" + mImageUris);
        for (Uri uri : mImageUris) {
            ImageView imageView = new ImageView(this);
            imageView.setImageURI(uri);
            mImageViews.add(imageView);
            Log.d(TAG, "uri==" + uri);
        }

        if (mImageUris.size() > 0) {
            mImagePosion = readLastPosion();
            updateImage();
        }

        mViewPager.setAdapter(new ViewPageAdapter());
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
        if (imageEntries == null) {
            Toast.makeText(getApplicationContext(), getString(R.string.file_no_found), Toast.LENGTH_SHORT).show();
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
                            Animation animation_r = AnimationUtils.loadAnimation(this, R.anim.push_left_in);
                            mImageView.startAnimation(animation_r);
                            mImagePosion--;
                            break;
                        case 'l':
                            if (mImagePosion == mImageUris.size() - 1) {
                                Toast.makeText(this, getString(R.string.the_last_page), Toast.LENGTH_SHORT).show();
                                break;
                            }
                            Animation animation_l = AnimationUtils.loadAnimation(this, R.anim.push_right_in);
                            mImageView.startAnimation(animation_l);
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
        if (imageEntries == null) {
            return;
        }
        mImageView.setImageURI(mImageUris.get(mImagePosion));
        mPage.setText(mImagePosion + 1 + "/" + mImageUris.size());
    }

    private int getMoveOrientation(float dx) {
        return dx > 0 ? 'r' : 'l';
    }


    class ViewPageAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return mImageViews.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        //getView
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View v = mImageViews.get(position);
            container.addView(v);
            return v;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            //滑动时移除
            View v = mImageViews.get(position);
            container.removeView(v);
        }
    }
}