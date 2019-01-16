package com.imotor.manual;

import android.Manifest;
import android.app.Activity;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import static com.imotor.manual.GreenComConstants.FileSavepathPics;

public class ManualActivity extends Activity implements View.OnClickListener {
    private final String TAG = "ManualActivity";
    private final String filePath = "bootlogo/config/manual/";

    public static final String FileSavepath = Environment.getExternalStorageDirectory()
            .getAbsolutePath() + "/" + "Pictures/";

    private final boolean isMemary = false;
    private List<Uri> mImageUris;
    private TextView mPage;
    private int mImagePosion;
    private ImageEntries imageEntries;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manual_main);

        if(!checkPermission(this)){
            Log.e(TAG, "onCreate: permision error" );
            finish();
        }
        setupView();
        init();
    }

    void updateImage() {
        if(imageEntries ==null){
            return ;
        }

    }


    private void setupView() {
//        mImagesLinearLayout = (LinearLayout) findViewById(R.id.ll_image_group);
//        mImageView = (ImageView) findViewById(R.id.image_surface_view);
//        mImageViewL = (ImageView) findViewById(R.id.image_surface_view_l);
//        mImageViewR = (ImageView) findViewById(R.id.image_surface_view_r);
        mPage = (TextView) findViewById(R.id.page_name);
        mViewPager = (ViewPager) findViewById(R.id.viewpager_image);
        mViewPager.setOnClickListener(this);
    }

    private void init() {
        ImageModel imageModel = new ImageModel(getApplicationContext());
//        imageEntries = imageModel.getImagePath(FileSavepath);
        //
        imageEntries = imageModel.getImagePath(FileSavepathPics);
        if (imageEntries == null) {
            Toast.makeText(getApplicationContext(), "未发现图片", Toast.LENGTH_SHORT).show();
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
        mViewPager.setOffscreenPageLimit(0);
        //switch to last posion
        if (mImageUris.size() > 0) {
            mImagePosion = readLastPosion();
//            mViewPager.setCurrentItem(mImagePosion);
//            mViewPager.setCurrentItem(mImagePosion, false);
        }


        if (mImageUris.size() > 0) {
            mImagePosion = readLastPosion();
            updateImage();
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


    /**
     * 权限检查
     *
     * @param context
     * @return
     */
    private static boolean checkPermission(Activity context) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission
                .WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED || ContextCompat
                .checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                String[] mPermissionList = new String[]{Manifest.permission
                        .WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
                ActivityCompat.requestPermissions(context, mPermissionList, 1);
                return false;
            }
        }
        return true;
    }
}
