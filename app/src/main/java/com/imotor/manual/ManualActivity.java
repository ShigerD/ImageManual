package com.imotor.manual;

import android.app.Activity;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import java.util.List;

public class ManualActivity extends Activity implements View.OnClickListener {
    private final String TAG = "ManualActivity";
    private final String filePath = "bootlogo/config/manual/";
    private final boolean isMemary = false;
    private List<Uri> mImageUris;
    private TextView mPage;
    private int mImagePosion;
    private ImageEntries imageEntries;
    private ViewPager mViewPager;
    private HorizontalListView mHorizontalListView;
    private CustomListViewAdapter mCustomListViewAdapter;
    private Gallery mGallery;
    private ImageView mImageView ,mImageViewR ,mImageViewL;
    private float mLastDownX;
    private LinearLayout mImagesLinearLayout;

    private int mMImageViewLastLeft =0;
    private int mRImageViewLastLeft =0;
    private int mLImageViewLastLeft =0;

    private int mImagesLinearLayoutLastLeft=0;
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

    /*    mImageView.setImageURI(mImageUris.get(mImagePosion));
        if(mImagePosion+1<mImageUris.size()){
            mImageViewR.setImageURI(mImageUris.get(mImagePosion+1));
        }*/

        mGallery.setSelection(mImagePosion);
    }


    private void setupView() {
//        mImagesLinearLayout = (LinearLayout) findViewById(R.id.ll_image_group);
//        mImageView = (ImageView) findViewById(R.id.image_surface_view);
//        mImageViewL = (ImageView) findViewById(R.id.image_surface_view_l);
//        mImageViewR = (ImageView) findViewById(R.id.image_surface_view_r);
        mPage = (TextView) findViewById(R.id.page_name);
        mViewPager = (ViewPager) findViewById(R.id.viewpager_image);
        mHorizontalListView = (HorizontalListView) findViewById(R.id.horizontalListView);
        mViewPager.setOnClickListener(this);
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
        for (Uri uri : mImageUris) {
            Log.d(TAG, "uri==" + uri);
        }
        final ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(this,mImageUris);
        viewPagerAdapter.setIOnViewPagerChangedLister(new ViewPagerAdapter.IOnViewPagerChangedLister() {
            public void onPageChangeTo(int position) {
                Log.d(TAG,"onPageChangeTo-posion"+position);
                if(position>mImageUris.size()){
                    position=0;
                }
//                mGallery.setSelection(position);
            }
        });

        mViewPager.setAdapter(viewPagerAdapter);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                Log.d(TAG,"onPageScrolled="+position+ " -positionOffset="+positionOffset);
                if (position == 4&&positionOffset>0.99) {
                    //在position4左滑且左滑positionOffset百分比接近1时，偷偷替换为position1（原本会滑到position5）
//                    viewPagerAdapter.mPosition++;
//                    viewPagerAdapter.setUriArry();
                    mViewPager.setCurrentItem(1, false);
                } else if (position == 0 && positionOffset <0.01) {
                    //在position1右滑且右滑百分比接近0时，偷偷替换为position4（原本会滑到position0）
                    if(viewPagerAdapter.mPosition>0){
//                        viewPagerAdapter.mPosition--;
//                        viewPagerAdapter.setUriArry();
                    }

                    mViewPager.setCurrentItem(4, false);
                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mViewPager.setOffscreenPageLimit(0);
        //switch to last posion
        if (mImageUris.size() > 0) {
            mImagePosion = readLastPosion();
//            mViewPager.setCurrentItem(mImagePosion);
//            mViewPager.setCurrentItem(mImagePosion, false);
        }


/*        GalleryAdapter galleryAdapter =new GalleryAdapter(this,mImageUris);
        mGallery.setAdapter(galleryAdapter);
/

        mGallery.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
//                mViewPager.setCurrentItem(position);
                mImagePosion=position;
                mImageView.setImageURI(mImageUris.get(position));
            }
        });*/

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


    @Override
    public void onClick(View v) {
        int id = v.getId();
        Log.w(TAG, "--click---" + id);
        switch (id) {
            case R.id.viewpager_image:
                changeHorizonbarVisiblity();
                break;
        }
    }

}
