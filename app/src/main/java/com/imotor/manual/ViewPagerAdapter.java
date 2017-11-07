package com.imotor.manual;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.util.LruCache;
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


    private List<String> mContainList = new ArrayList<String>();

    private LruCache<Uri, Bitmap> mBitmapLruCache;

    public ViewPagerAdapter(Context context, List<Uri> uris) {
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mContext = context;
        mImageUris = uris;
        mActivity = (ManualActivity) mContext;

        int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        Log.w(TAG, "Max memory is " + maxMemory + "KB");
        int cacheSize = maxMemory / 8;
        mBitmapLruCache= new LruCache<Uri, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(Uri key, Bitmap value) {
                Log.w(TAG,"sizeOf-Viewpage--"+String.valueOf(value.getByteCount() / 1024));
                return value.getByteCount() / 1024;
            }
        };
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


        ImageView imageView = new ImageView(mContext);
        Uri uri = mImageUris.get(position);
        imageView.setImageURI(uri);
//        Bitmap bitmap = mBitmapLruCache.get(uri);
//        if (bitmap == null) {
//            BitmapFactory.Options options = new BitmapFactory.Options();
//            options.inJustDecodeBounds = true;
//            BitmapFactory.decodeFile(uri.getPath(), options);
//            Log.d(TAG,"options.outHeight="+options.outHeight+" options.outWidth="+options.outWidth);
//            options.inJustDecodeBounds = false;
//            bitmap = BitmapFactory.decodeFile(uri.getPath(), options);
//            mBitmapLruCache.put(uri, bitmap);
//        }
//        imageView.setImageBitmap(bitmap);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActivity.changeHorizonbarVisiblity();
            }
        });

        int childcount= container.getChildCount();
        if(childcount>4){
            container.removeViewAt(4);
        }

        Log.d(TAG,"-container-childcount--before==="+childcount);
        Log.d(TAG,"-container.getChildAt(position)---"+container.getChildAt(position));
        Log.w(TAG,"-container---imageView---"+imageView);

        container.addView(imageView,0);

        return imageView;
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
//        super.setPrimaryItem(container,position,object);
        mIOnViewPagerChangedLister.onPageChangeTo(position);
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
