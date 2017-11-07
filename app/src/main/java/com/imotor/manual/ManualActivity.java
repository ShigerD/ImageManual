package com.imotor.manual;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.util.LruCache;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Gallery;
import android.widget.Toast;
import java.util.List;

public class ManualActivity extends Activity {
    private final String TAG = "ManualActivity";
    private final String filePath = "bootlogo/config/manual/";
    private final boolean isMemary = false;
    private List<Uri> mImageUris;
    private int mImagePosion;
    private ImageEntries imageEntries;
    private ViewPager mViewPager;
    private Gallery mGallery;
    private LruCache<Uri, Bitmap> mMemoryCacheGallery;
    private LruCache<Uri, Bitmap> mMemoryCacheViewpage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manual_main);



        setupView();
        init();
    }

    void updateImage() {
        if(imageEntries ==null){
            return ;
        }

        mGallery.setSelection(mImagePosion);
    }


    private void setupView() {

        mViewPager = (ViewPager) findViewById(R.id.viewpager_image);
        mGallery = (Gallery) findViewById(R.id.gallery);
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

        final ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(this,mImageUris);
        viewPagerAdapter.setIOnViewPagerChangedLister(new ViewPagerAdapter.IOnViewPagerChangedLister() {
            public void onPageChangeTo(int position) {
                Log.d(TAG,"onPageChangeTo-posion"+position);
                if(position>mImageUris.size()){
                    position=0;
                }
                mGallery.setSelection(position);
            }
        });

        mViewPager.setAdapter(viewPagerAdapter);
        mViewPager.setOffscreenPageLimit(0);

        //switch to last posion
        if (mImageUris.size() > 0) {
            mImagePosion = readLastPosion();
//            mViewPager.setCurrentItem(mImagePosion);
//            mViewPager.setCurrentItem(mImagePosion, false);
        }


        GalleryAdapter galleryAdapter =new GalleryAdapter(this,mImageUris);
        mGallery.setAdapter(galleryAdapter);
        mGallery.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                mViewPager.setCurrentItem(position,false);
//                mImagePosion=position;
//                mImageView.setImageURI(mImageUris.get(position));
            }
        });

        if (mImageUris.size() > 0) {
            mImagePosion = readLastPosion();
            updateImage();
        }
    }

    void  changeHorizonbarVisiblity(){
        if(mGallery.getVisibility()==View.VISIBLE){
            mGallery.setVisibility(View.INVISIBLE);
        }else {
            mGallery.setVisibility(View.VISIBLE);
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
        if(isMemary){
            SharedPreferences.Editor editor = getSharedPreferences("lock", MODE_WORLD_WRITEABLE).edit();
            editor.putInt("posion", mImagePosion);
            editor.apply();
        }

    }

    int readLastPosion() {
        if(isMemary) {
            SharedPreferences read = getSharedPreferences("lock", MODE_WORLD_READABLE);
            return read.getInt("posion", 1);
        }else {
            return 0;
        }
    }


}
