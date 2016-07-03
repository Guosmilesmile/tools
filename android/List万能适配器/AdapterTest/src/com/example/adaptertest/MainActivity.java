package com.example.adaptertest;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.os.Build;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		List<ItemBean> itemBeanList = new ArrayList<ItemBean>();
		for (int i = 0; i < 20; i++) {
			itemBeanList.add(new ItemBean(R.drawable.ic_launcher, "我是标题" + i,
					"我是内容" + i));
		}
		ListView listView = (ListView) findViewById(R.id.lv_main);
		listView.setAdapter(new MyadapterWithViewHolder(this, itemBeanList,R.layout.item));
	}
}
