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
import java.util.List;

public class ViewPagerAdapter extends PagerAdapter {
    private static final String TAG = "ViewPagerAdapter";
    private Context mContext;
    private List<Uri> mImageUris;
    private ManualActivity mActivity;
    private LayoutInflater mInflater;
    private int mPosition = -1;
    public ViewPagerAdapter(Context context, List<Uri> uris) {
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mContext = context;
        mImageUris = uris;
        mActivity = (ManualActivity) mContext;
    }

    public class ViewHolder {
        public ImageView imageView;
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
        Log.d(TAG, "instantiateItem--" + position);
        ImageView imageView = new ImageView(mContext);
        imageView.setImageURI(mImageUris.get(position));
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActivity.changeHorizonbarVisiblity();
            }
        });
        int childcount= container.getChildCount();
        Log.d(TAG,"childcount--before==="+childcount);
        Log.d(TAG,"-container.getChildAt(position)---"+container.getChildAt(position));
        container.addView(imageView);

        Log.d(TAG,container.toString());
        return imageView;
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
//        super.setPrimaryItem(container,position,object);
        Log.d(TAG, "setPrimaryItem--" + position +"-mPosition-"+mPosition);
        if(mPosition!=position){
            Log.d(TAG,"setPrimaryItem--mPosition!=position");
            mIOnViewPagerChangedLister.onPageChangeTo(position);
            if(container.getChildAt(position)!=null){
                ImageView iv =(ImageView) container.getChildAt(position);
                iv.setImageURI(mImageUris.get(position));
                return ;
            }
        }
        mPosition =position;
    }

    @Override
    public int getItemPosition(Object object) {
        Log.d(TAG, "getItemPosition--" + super.getItemPosition(object));
        return super.getItemPosition(object);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        Log.d(TAG, "destroyItem--position==" + position+"--mp--"+mPosition);
        View v = (View) object;
        if (v == null||container.getChildAt(position)==null){
            return;
        }
        ImageView imageView = (ImageView) container.getChildAt(position);
        imageView.setImageURI(null);
        releaseImageViewResourse(imageView);
//        container.removeView(imageView);
//        container.removeViewAt(position);//result some page missing
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
