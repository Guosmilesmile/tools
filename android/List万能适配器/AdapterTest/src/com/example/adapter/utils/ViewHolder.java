package com.example.adapter.utils;

import android.R.integer;
import android.R.string;
import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class ViewHolder {
	private SparseArray<View> mView;
	private int mPosttion;
	private View mConvertView;

	public ViewHolder(Context context, ViewGroup parent, int layoutId,
			int position) {
		this.mPosttion = position;
		this.mView = new SparseArray<View>();
		mConvertView = LayoutInflater.from(context).inflate(layoutId, parent,
				false);
		mConvertView.setTag(this);
	}

	public static ViewHolder get(Context context, View convertView,//入口函数
			ViewGroup parent, int layoutId, int position) {
		if (convertView == null) {
			return new ViewHolder(context, parent, layoutId, position);
		} else {
			ViewHolder holder = (ViewHolder) convertView.getTag();
			holder.mPosttion = position;//convertView是复用的，可是positon是变化的。
			return holder;
		}
	}

	public ViewHolder setText(int viewId,String text){
		TextView textView = getView(viewId);
		textView.setText(text);
		return this;
	}
	
	public ViewHolder setImage(int viewId,int resid){
		ImageView imageView = getView(viewId);
		imageView.setImageResource(resid);
		return this;
	}
	/**
	 * 通过ViewId获取控件
	 * **/
	public <T extends View> T getView(int viewId){
		View view = mView.get(viewId);//先获取容器中的view判断是否存储过，如果存储过就跳过
		if(view==null){
			view =  mConvertView.findViewById(viewId);
			mView.put(viewId, view);
		}
		return (T)view;
	}
	
	public View getConvertView() {
		return mConvertView;
	}
	
	
	
}
