package com.imotor.manual;

import android.app.Activity;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
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
    private CustomListViewAdapter customListViewAdapter;
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
        mHorizontalListView = (HorizontalListView) findViewById(R.id.horizontalListView);
        mViewPager.setOnClickListener(this);

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
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(this,mImageUris);
        viewPagerAdapter.setIOnViewPagerChangedLister(new ViewPagerAdapter.IOnViewPagerChangedLister() {
            public void onPageChangeTo(int position) {
                mHorizontalListView.scrollTo(position);
            }
        });
        mViewPager.setAdapter(viewPagerAdapter);
        mViewPager.setOffscreenPageLimit(1);
        //switch to last posion
        if (mImageUris.size() > 0) {
            mImagePosion = readLastPosion();
            mViewPager.setCurrentItem(mImagePosion);
        }

        customListViewAdapter = new CustomListViewAdapter(this,mImageUris);

        mHorizontalListView.setAdapter(customListViewAdapter);
        mHorizontalListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mViewPager.setCurrentItem(position);
//                customListViewAdapter.setSelectPosition(position);
            }
        });

    }

    void  changeHorizonbarVisiblity(){
        if(mHorizontalListView.getVisibility()==View.VISIBLE){
            mHorizontalListView.setVisibility(View.INVISIBLE);
        }else {
            mHorizontalListView.setVisibility(View.VISIBLE);
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

}
