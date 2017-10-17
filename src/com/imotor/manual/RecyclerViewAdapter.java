package com.imotor.manual;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>
{
    private String TAG = "RecyclerViewAdapter";
    private LayoutInflater mInflater;
    private List<Uri> mDatas;

    public RecyclerViewAdapter(Context context, List<Uri> datats)
    {
        mInflater = LayoutInflater.from(context);
        mDatas = datats;
    }

    public  class ViewHolder extends RecyclerView.ViewHolder
    {
        public ViewHolder(View arg0)
        {
            super(arg0);
            Log.d(TAG,"-ViewHolder-");
        }
        ImageView mImg;
    }

    @Override
    public int getItemCount()
    {
        Log.d(TAG,"-getItemCount-"+mDatas.size());
        return mDatas.size();
    }

    /**
     * 创建ViewHolder
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
    {
        Log.d(TAG,"-onCreateViewHolder-");
        View view = mInflater.inflate(R.layout.recycler_item,
                viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.mImg = (ImageView) view.findViewById(R.id.id_index_gallery_item_image);
        return viewHolder;
    }

    /**
     * 设置值
     */
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i)
    {
        Log.d(TAG,"-onBindViewHolder-i="+i);
        viewHolder.mImg.setImageURI(mDatas.get(i));
        if (mOnItemClickLitener != null)
        {
            viewHolder.itemView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    mOnItemClickLitener.onItemClick(viewHolder.itemView, i);
                }
            });

        }
    }


    public interface OnItemClickLitener
    {
        void onItemClick(View view, int position);
    }

    private OnItemClickLitener mOnItemClickLitener;

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener)
    {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

}
