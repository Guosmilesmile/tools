package com.example.adaptertest;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class Myadapter extends BaseAdapter{
	private  LayoutInflater inflater;
	private List<ItemBean> list;
	public Myadapter(Context context,List<ItemBean> list){
		this.list = list;
		inflater = LayoutInflater.from(context);
	}
	
	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int arg0) {	
		return list.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
	/*	//逗比式的写法，忽略的listview本身的缓冲机制   每次都创建一个新的view
		View view  = inflater.inflate(R.layout.item, null);//想item.xml转换成对应的view
		ImageView imageView = (ImageView)view.findViewById(R.id.iv_image);//获取xml转换成view中的对应的image，之后进行赋值
		TextView title = (TextView) view.findViewById(R.id.tv_title);//理由同上
		TextView cntent = (TextView) view.findViewById(R.id.tv_content);//理由同上
		ItemBean itemBean = list.get(position);//获取相应位置的iteambean
		imageView.setImageResource(itemBean.ItemImageResid);//赋值
		title.setText(itemBean.Itemtitle);
		cntent.setText(itemBean.itemContent);
		return view;*/
		
		//普通式  避免创建创建太多的view
	/*	if(convertView==null){
			convertView = inflater.inflate(R.layout.item, null);
		}
		ImageView imageView = (ImageView)convertView.findViewById(R.id.iv_image);//获取xml转换成view中的对应的image，之后进行赋值
		TextView title = (TextView) convertView.findViewById(R.id.tv_title);//理由同上
		TextView cntent = (TextView) convertView.findViewById(R.id.tv_content);//理由同上
		ItemBean itemBean = list.get(position);//获取相应位置的iteambean
		imageView.setImageResource(itemBean.ItemImageResid);//赋值
		title.setText(itemBean.Itemtitle);
		cntent.setText(itemBean.itemContent);
		return convertView;*/
		
		//文艺式  避免两个耗时操作，convertView  和 findViewById
		ViewHolder viewHolder;
		if(convertView==null){
			viewHolder = new ViewHolder();
			convertView = inflater.inflate(R.layout.item, null);
			viewHolder.imageView = (ImageView)convertView.findViewById(R.id.iv_image);
			viewHolder.title=(TextView) convertView.findViewById(R.id.tv_title);
			viewHolder.content = (TextView) convertView.findViewById(R.id.tv_content);
			convertView.setTag(viewHolder);//将这个类和convertView进行关联，使得可以通过get获取。不必每次都进行findviewbyid
		}else{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		ItemBean itemBean = list.get(position);//获取相应位置的iteambean
		viewHolder.imageView.setImageResource(itemBean.ItemImageResid);//赋值
		viewHolder.title.setText(itemBean.Itemtitle);
		viewHolder.content.setText(itemBean.itemContent);
		return convertView;
	}
	/**
	 * 通过创建viewholder来暂存通过findviewbyid获取的各个组件，然后通过convertview的settag来进行关联。
	 * **/
	class ViewHolder{
		public ImageView imageView;
		public TextView title;
		public TextView content;
	}

}
