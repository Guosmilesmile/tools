package com.example.adapter.utils;
import java.util.List;

import com.example.adaptertest.ItemBean;
import com.example.adaptertest.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public abstract class CommonAdapter<T> extends BaseAdapter{

	protected Context mContext;
	protected List<T>	mDatas;
	protected LayoutInflater mInflater;
	private int layoutId;
	public CommonAdapter(Context context,List<T> datas,int layoutId){
		this.mContext = context;
		mInflater = LayoutInflater.from(context);
		this.mDatas = datas;
		this.layoutId = layoutId;
	}
	@Override
	public int getCount() {
		return mDatas.size();
	}
	@Override
	public T getItem(int position) {
		return mDatas.get(position);
	}
	@Override
	public long getItemId(int position) {
		return position;
	}
	@Override
	public  View getView(int position, View convertView, ViewGroup parent){
		ViewHolder viewHolder = ViewHolder.get(mContext, convertView, parent,
				layoutId, position);
		convert(viewHolder, getItem(position));
		return viewHolder.getConvertView();//返回的是绑定的converview，不是参数convertView
	}
	
	
	/**
	 * 实现对控件的赋值。
	 * 先将第二个参数的类型修改成bean类型
	 * example：
	 * TextView title = holder.getView(R.id.tv_title);
	 * title.setText(itemBean.Itemtitle);
	 * 
	 * **/
	public abstract void convert(ViewHolder viewholder , T Itembean);

}
