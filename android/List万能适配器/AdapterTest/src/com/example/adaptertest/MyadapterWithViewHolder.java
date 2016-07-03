package com.example.adaptertest;

import java.util.List;

import com.example.adapter.utils.CommonAdapter;
import com.example.adapter.utils.ViewHolder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MyadapterWithViewHolder extends CommonAdapter<ItemBean> {

	public MyadapterWithViewHolder(Context context, List<ItemBean> list,int layoutId) {
		super(context,list,layoutId);
	}

	@Override
	public void convert(ViewHolder viewHolder, ItemBean Itembean) {
		/*ImageView imageView = viewHolder.getView(R.id.iv_image);
		imageView.setImageResource(Itembean.ItemImageResid);// 赋值
*/		//TextView title = viewHolder.getView(R.id.tv_title);
		//title.setText(Itembean.Itemtitle);
		viewHolder.setImage(R.id.iv_image,Itembean.ItemImageResid);
		viewHolder.setText(R.id.tv_title,Itembean.Itemtitle);
		viewHolder.setText(R.id.tv_content,Itembean.itemContent);
		/*TextView content = viewHolder.getView(R.id.tv_content);
		content.setText(Itembean.itemContent);*/
	}

	
	

}
