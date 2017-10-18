package com.imotor.manual;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class ManualActivity extends Activity implements View.OnClickListener {
    private final String TAG = "ManualActivity";

    private final String filePath = "bootlogo/config/imgreader";
    private List<Uri> mImageUris;
    private TextView mPage;
    private int mImagePosion;
    private ImageEntries imageEntries;
    private ViewPager mViewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manual_main);
        setupView();
        init();
    }

    private void setupView() {
        mPage = (TextView) findViewById(R.id.page_name);
        mViewPager = (ViewPager) findViewById(R.id.viewpager_image);
        mViewPager.setOnClickListener(this);
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
        mViewPager.setAdapter(new ViewPageAdapter(this));

        //switch to last posion
        if (mImageUris.size() > 0) {
            mImagePosion = readLastPosion();
            mViewPager.setCurrentItem(mImagePosion);
        }
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "-onDestroy-");
        savePosion();
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        Log.d(TAG, "-onStop-");
        savePosion();
        super.onStop();
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

    void updatePage(int posion) {
        if (imageEntries == null) {
            return;
        }
        mPage.setText(posion + 1 + "/" + mImageUris.size());
        mImagePosion = posion;// save posion
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        Log.w(TAG,"ontouch--"+action);
        return super.onTouchEvent(event);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        Log.w(TAG, "--click---" + id);
        switch (id) {
            case R.id.viewpager_image:
                break;
        }
    }



    private class ViewPageAdapter extends PagerAdapter {

        private Context mContext;

        public ViewPageAdapter(Context context) {
            mContext = context;
        }


        @Override
        public int getCount() {
            return mImageUris.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        //getView
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
//            Log.d(TAG, "instantiateItem--" + position);
            ImageView imageView = new ImageView(mContext);
            imageView.setImageURI(mImageUris.get(position));
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });
            container.addView(imageView);
            return imageView;
        }

        @Override
        public void setPrimaryItem(ViewGroup container, int position, Object object) {
//            Log.d(TAG, "setPrimaryItem--" + position);
            updatePage(position);
            super.setPrimaryItem(container, position, object);
        }

        @Override
        public int getItemPosition(Object object) {
//            Log.d(TAG, "getItemPosition--" + super.getItemPosition(object));
            return super.getItemPosition(object);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            //remove
            container.removeView(container.getChildAt(position));
        }
    }


}
