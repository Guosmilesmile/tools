package com.jay.example.viewpagerdemo4;

import java.util.ArrayList;
import java.util.List;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.app.Activity;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;


public class MainActivity extends FragmentActivity {

	private ViewPager mPager;
	private ArrayList<Fragment> fragments;
	private FragmentManager fragmentManager;
	private Fragment1 fragment1;
	private Fragment2 fragment2;
	private Fragment3 fragment3;
	
	
	private TextView t1, t2, t3;//三个导航栏
	
	private ImageView cursor;
	private int offset = 0;//移动条图片的偏移量
	private int currIndex = 0;//当前页面的编号
	private int bmpWidth;// 移动条图片的长度
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initCursorPos();
		initView();		
	}
	
	public void initView()
	{
		fragmentManager = getSupportFragmentManager();
		fragment1 = new Fragment1();
		fragment2 = new Fragment2();
		fragment3 = new Fragment3();
		
		mPager = (ViewPager) findViewById(R.id.vPager);
		t1 = (TextView) findViewById(R.id.text1);
		t2 = (TextView) findViewById(R.id.text2);
		t3 = (TextView) findViewById(R.id.text3);	
		
		fragments = new ArrayList<Fragment>();
		fragments.add(fragment1);
		fragments.add(fragment2);
		fragments.add(fragment3);
		
		mPager.setAdapter(new MyFragmentAdapter(fragmentManager,fragments));
		mPager.setCurrentItem(0);
		
		
		t1.setOnClickListener(new MyClickListener(0));
		t2.setOnClickListener(new MyClickListener(1));
		t3.setOnClickListener(new MyClickListener(2));
		mPager.setOnPageChangeListener(new MyOnPageChangeListener());
	}
	
	
	//初始化指示器的位置,就是下面那个移动条一开始放的地方
    public void initCursorPos() { 
		cursor = (ImageView) findViewById(R.id.cursor);
		bmpWidth = BitmapFactory.decodeResource(getResources(), R.drawable.a)
				.getWidth();// 获取图片宽度
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		int screenW = dm.widthPixels;// 获取分辨率宽度
		offset = (screenW / 3 - bmpWidth) / 2;// 计算偏移量
		Matrix matrix = new Matrix();
		matrix.postTranslate(offset, 0);
		cursor.setImageMatrix(matrix);// 设置动画初始位置
    } 
    
    //设置点击事件,点击上面文字切换页面的
  	public class MyClickListener implements OnClickListener
  	{
  		private int index = 0;
  		public MyClickListener(int i)
  		{
  			index = i;
  		}
  		
  		@Override
  		public void onClick(View arg0) {
  			mPager.setCurrentItem(index);			
  		}
  		
  	}
  	
  //监听页面切换时间,主要做的是动画处理,就是移动条的移动
  	public class MyOnPageChangeListener implements OnPageChangeListener {
  		int one = offset * 2 + bmpWidth;// 移动一页的偏移量,比如1->2,或者2->3
  		int two = one * 2;// 移动两页的偏移量,比如1直接跳3

  		@Override
  		public void onPageSelected(int index) {
  			Animation animation = null;
  			switch (index) {
  			case 0:
  				if (currIndex == 1) {
  					animation = new TranslateAnimation(one, 0, 0, 0);
  				} else if (currIndex == 2) {
  					animation = new TranslateAnimation(two, 0, 0, 0);
  				}
  				break;
  			case 1:
  				if (currIndex == 0) {
  					animation = new TranslateAnimation(offset, one, 0, 0);
  				} else if (currIndex == 2) {
  					animation = new TranslateAnimation(two, one, 0, 0);
  				}
  				break;
  			case 2:
  				if (currIndex == 0) {
  					animation = new TranslateAnimation(offset, two, 0, 0);
  				} else if (currIndex == 1) {
  					animation = new TranslateAnimation(one, two, 0, 0);
  				}
  				break;
  			}
  			currIndex = index;
  			animation.setFillAfter(true);// true表示图片停在动画结束位置
  			animation.setDuration(300); //设置动画时间为300毫秒
  			cursor.startAnimation(animation);//开始动画
  		}

  		@Override
  		public void onPageScrollStateChanged(int arg0) {}

  		@Override
  		public void onPageScrolled(int arg0, float arg1, int arg2) {}
  	
  	}
	
}
