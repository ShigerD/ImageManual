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
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(imageEntries ==null){
            Toast.makeText(getApplicationContext(), getString(R.string.file_no_found), Toast.LENGTH_SHORT).show();
            return true;
        }
        float x = event.getX();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                updateImage();
                mLastDownX = x;
                mMImageViewLastLeft = mImageView.getLeft();
                mRImageViewLastLeft = mImageViewR.getLeft();
                mLImageViewLastLeft = mImageViewL.getLeft();

                mImagesLinearLayoutLastLeft = mImagesLinearLayout.getLeft();
                break;
            case MotionEvent.ACTION_UP:
                Log.d(TAG, "=======upX-----" + x);
                Log.d(TAG, "=======mLastDownX---" + x);
                float dx = x - mLastDownX;
                if (Math.abs(dx) > 8) {
                    int orientation = dx > 0 ? 'r' : 'l';
                    switch (orientation) {
                        case 'r':
                            if (mImagePosion == 0) {
                                Toast.makeText(this, getString(R.string.the_first_page), Toast.LENGTH_SHORT).show();
                                break;
                            }
                            Animation animation_r = AnimationUtils.loadAnimation(this,R.anim.push_left_in);
//                            mImageView.startAnimation(animation_r);
                            mImagePosion--;

                            break;
                        case 'l':
                            if (mImagePosion == mImageUris.size() - 1) {
                                Toast.makeText(this, getString(R.string.the_last_page), Toast.LENGTH_SHORT).show();
                                break;
                            }
                            Animation animation_l= AnimationUtils.loadAnimation(this,R.anim.push_right_in);
//                            mImageView.startAnimation(animation_l);
                            mImagePosion++;
//                            mImageView.layout(0,0,(int)dx,0);
                            break;
                    }
                }else {
                    changeHorizonbarVisiblity();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (mImagePosion == mImageUris.size() - 1) {
                    break;
                }
                Log.d(TAG, "=======move-----" + x);
                int dxOffset = (int)(x - mLastDownX);
                Log.d(TAG,"mLastDownX="+mLastDownX+" x="+x);
                Log.d(TAG,"dxOffset="+dxOffset);
                Log.w(TAG,"l="+mImageView.getLeft()+" r="+mImageView.getRight());
        /*        if(mImageView.getRight()>2048){
                    break;
                }*/
//                mImageView.offsetLeftAndRight((dxOffset));
//                mImageViewR.offsetLeftAndRight(dxOffset);
                if(dxOffset<0){
                    mImageView.layout(
                            mMImageViewLastLeft +(int)dxOffset,
                            mImageView.getTop(),
                            mMImageViewLastLeft +1024+(int)dxOffset,
                            mImageView.getBottom());

                    mImageViewR.layout(
                            mRImageViewLastLeft +(int)dxOffset,
                            mImageViewR.getTop(),
                            mRImageViewLastLeft +1024+(int)dxOffset,
                            mImageViewR.getBottom());
                    mImageViewL.layout(
                            mLImageViewLastLeft+dxOffset,
                            mImageViewL.getTop(),
                            mLImageViewLastLeft+1024+dxOffset,
                            mImageViewL.getBottom()
                    );
                }else {
                   /* mImageView.layout(
                            mMImageViewLastLeft +(int)dxOffset,
                            mImageView.getTop(),
                            mMImageViewLastLeft +1024+(int)dxOffset,
                            mImageView.getBottom());

                    mImageViewR.layout(
                            mRImageViewLastLeft +(int)dxOffset,
                            mImageViewR.getTop(),
                            mRImageViewLastLeft +1024+(int)dxOffset,
                            mImageViewR.getBottom());*/

               /*     mImageViewL.layout(
                            mLImageViewLastLeft+dxOffset,
                            mImageViewL.getTop(),
                            mLImageViewLastLeft+1024+dxOffset,
                            mImageViewL.getBottom()
                            );*/
                }

                Log.w(TAG,"end----------l="+mImageView.getLeft()+" r="+mImageView.getRight());

//                mImagesLinearLayout.offsetLeftAndRight((int)dxOffset);

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
   /*     if(mImagePosion-1>=1){
            mImageViewL.setVisibility(View.VISIBLE);
            mImageViewL.setImageURI(mImageUris.get(mImagePosion-1));
        }else {
            mImageViewL.setVisibility(View.GONE);
        }*/
        if(mImagePosion+1<mImageUris.size()){
            mImageViewR.setImageURI(mImageUris.get(mImagePosion+1));
        }
        int intL = -1024;
    /*    mImageView.layout(
                0,
                mImageView.getTop(),
                1024,
                mImageView.getBottom());

        mImageViewR.layout(
                2048,
                mImageViewR.getTop(),
                3096,
                mImageViewR.getBottom());

        mImageViewL.layout(
                intL,
                mImageViewL.getTop(),
                0,
                mImageViewL.getBottom()
        );*/

        mGallery.setSelection(mImagePosion);
    }

    void updateImageRevace() {
        if(imageEntries ==null){
            return ;
        }
        mImageViewR.setImageURI(mImageUris.get(mImagePosion));
   /*     if(mImagePosion-1>=1){
            mImageViewL.setVisibility(View.VISIBLE);
            mImageViewL.setImageURI(mImageUris.get(mImagePosion-1));
        }else {
            mImageViewL.setVisibility(View.GONE);
        }*/
        if(mImagePosion+1<mImageUris.size()){
            mImageViewL.setImageURI(mImageUris.get(mImagePosion+1));
        }

    }

    private void setupView() {
        mImagesLinearLayout = (LinearLayout) findViewById(R.id.ll_image_group);
        mImageView = (ImageView) findViewById(R.id.image_surface_view);
        mImageViewL = (ImageView) findViewById(R.id.image_surface_view_l);
        mImageViewR = (ImageView) findViewById(R.id.image_surface_view_r);
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
 /*       ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(this,mImageUris);
        viewPagerAdapter.setIOnViewPagerChangedLister(new ViewPagerAdapter.IOnViewPagerChangedLister() {
            public void onPageChangeTo(int position) {
//                mHorizontalListView.scrollTo(position);
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
            mViewPager.setCurrentItem(mImagePosion);
        }*/

        GalleryAdapter galleryAdapter =new GalleryAdapter(this,mImageUris);
        mGallery.setAdapter(galleryAdapter);
  /*      mGallery.setOnItemSelectedListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mViewPager.setCurrentItem(position);
            }
        });*/

        mGallery.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
//                mViewPager.setCurrentItem(position);
                mImagePosion=position;
                mImageView.setImageURI(mImageUris.get(position));
            }
        });

    /*    mCustomListViewAdapter = new CustomListViewAdapter(this,mImageUris);
        mHorizontalListView.setAdapter(mCustomListViewAdapter);
        mHorizontalListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mViewPager.setCurrentItem(position);
//                mCustomListViewAdapter.setSelectPosition(position);
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
