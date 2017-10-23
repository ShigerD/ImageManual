package com.imotor.manual;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerAdapter extends PagerAdapter {
    private static final String TAG = "ViewPagerAdapter";
    private Context mContext;
    private List<Uri> mImageUris;
    private ManualActivity mActivity;
    private LayoutInflater mInflater;
    public int mPosition = 0;
    private Uri[] mUriArry = new Uri[4];

    private List<String> mContainList = new ArrayList<String>();

    public ViewPagerAdapter(Context context, List<Uri> uris) {
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mContext = context;
        mImageUris = uris;
        mActivity = (ManualActivity) mContext;
//        mUriArry[0] = mImageUris.get(0);
//        for (int i = 0; i < mUriArry.length; i++) {
//            mUriArry[i] = mImageUris.get(i+3);
//        }
        setUriArry();
    }



    public class ViewHolder {
        public ImageView imageView;
    }

    public void setUriArry(){
        for (int i = 0; i < mUriArry.length; i++) {
            mUriArry[i] = mImageUris.get(i+mPosition);
        }
    }

    public interface IOnViewPagerChangedLister {
        void  onPageChangeTo(int position);
    }

    private IOnViewPagerChangedLister mIOnViewPagerChangedLister;

    public void setIOnViewPagerChangedLister(IOnViewPagerChangedLister iOnViewPagerChangedLister){
        mIOnViewPagerChangedLister = iOnViewPagerChangedLister;
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
    public Object instantiateItem(ViewGroup container, final int position) {

//        ImageView imageView = new ImageView(mContext);

        ImageView imageView = new ImageView(mContext);
        imageView.setImageURI(mImageUris.get(position));
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                mActivity.changeHorizonbarVisiblity();
            }
        });

        int childcount= container.getChildCount();
        if(childcount>8){
            container.removeViewAt(8);
        }
        Log.d(TAG,"-container-childcount--before==="+childcount);
        Log.d(TAG,"-container.getChildAt(position)---"+container.getChildAt(position));
        Log.w(TAG,"-container---imageView---"+imageView);

     /*
        if(!mContainList.contains(imageView.toString())){
            container.addView(imageView);
            mContainList.add(imageView.toString());
            Log.w(TAG,"-!mContainList.contains--mContainList="+mContainList.size());

        }
        */
//        Log.d(TAG,container.toString());
        container.addView(imageView,0);

        return imageView;
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
//        super.setPrimaryItem(container,position,object);

//        Log.w(TAG,"-mUriArry-"+mUriArry[0].toString());
    }

    @Override
    public int getItemPosition(Object object) {
        Log.d(TAG, "getItemPosition--" + super.getItemPosition(object));
        return super.getItemPosition(object);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        Log.d(TAG, "destroyItem--position==" + position);
/*        View v = (View) object;
        if (v == null||container.getChildAt(position)==null){
            return;
        }
        ImageView imageView = (ImageView) container.getChildAt(position);
        imageView.setImageURI(null);
        releaseImageViewResourse(imageView);
//        container.removeView(imageView);
//        container.removeViewAt(position);//result some page missing*/

    }


    private void releaseImageViewResourse(ImageView iv) {
        if (iv == null)
            return;
        Drawable drawable = iv.getDrawable();
        if (drawable != null && drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            Bitmap bitmap = bitmapDrawable.getBitmap();
            if (bitmap != null && !bitmap.isRecycled()) {
                bitmap.recycle();
                bitmap = null;
            }
        }
        System.gc();
        iv =null;
    }
}
