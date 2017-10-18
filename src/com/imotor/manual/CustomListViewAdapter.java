package com.imotor.manual;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;

/** An array adapter that knows how to render views when given CustomData classes */
public class CustomListViewAdapter extends BaseAdapter {

    private String TAG = "CustomListViewAdapter";

    private LayoutInflater mInflater;
    private List<Uri> mUris;
    private Context mContext;
    private OnItemClickLitener mOnItemClickLitener;

    public CustomListViewAdapter(Context context) {
        mInflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);//LayoutInflater.from(mContext);
    }

    public CustomListViewAdapter(Context context , List<Uri> uris) {
        Log.d(TAG,"CustomListViewAdapter--");
        mInflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);//LayoutInflater.from(mContext);
        mUris = uris;
        mContext = context;
    }

    public interface OnItemClickLitener
    {
        void onItemClick(int position);
    }

    public int getCount() {
        Log.d(TAG,"getCount--"+mUris.size());
        return mUris.size();
    }

    public Object getItem(int position) {
        Log.d(TAG,"getItem--"+position);
        return mUris.get(position);
    }

    public long getItemId(int position) {
        Log.d(TAG,"getItemId--"+position);
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder = new Holder();
        Log.d(TAG,"getView--"+position);

        Bitmap bitmap = miniImageUri(mUris.get(position));

        if (convertView == null|| convertView.getTag() == null) {
            // Inflate the view since it does not exist
            convertView = mInflater.inflate(R.layout.custom_data_view, parent, false);
            // Create and save off the holder in the tag so we get quick access to inner fields
            // This must be done for performance reasons
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        holder.imageView = (ImageView) convertView.findViewById(R.id.image);


        holder.imageView.setImageBitmap(bitmap);

        if (mOnItemClickLitener != null)
        {
            final int mposition=position;
//            holder.imageView.setTag(mposition);
     /*       holder.imageView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
//                    mOnItemClickLitener.onItemClick( mposition);
                    Log.d(TAG , "click");
                }
            });*/
        }
        return convertView;
    }

    /** View holder for the views we need access to */
    public class Holder {
        public ImageView imageView;
    }

    private Bitmap miniImageUri(Uri uri){

        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(mContext.getContentResolver(), uri);
            if(bitmap==null||bitmap.getWidth()<=0||bitmap.getHeight()<=0){
                return null;
            }
            bitmap = miniSizeImageView(60, bitmap);
//            viewHolder.folderImg.setImageBitmap(bitmap);
            Log.w(TAG, "M宽度为" + bitmap.getWidth() + "高度为" + bitmap.getHeight());
            return bitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }

    private Bitmap miniSizeImageView(int w, Bitmap bitmap) {

        Matrix matrix = new Matrix();
        Log.w(TAG, "input" + bitmap.getWidth() + "高度为" + bitmap.getHeight());
        float rateY = (float) w / bitmap.getHeight();
        float scale = rateY*1.5f;
        Log.d(TAG, "scale==" + scale );
        matrix.setScale(scale, scale);
        bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                bitmap.getHeight(), matrix, true);
        Log.w(TAG, "output宽度为" + bitmap.getWidth() + "高度为" + bitmap.getHeight());
        return bitmap;
    }

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener)
    {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

}
