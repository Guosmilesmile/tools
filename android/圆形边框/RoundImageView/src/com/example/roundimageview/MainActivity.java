package com.example.roundimageview;

import com.makeramen.roundedimageview.RoundedImageView;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends Activity {

	private RoundedImageView roundedImageView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		roundedImageView = (RoundedImageView) findViewById(R.id.imageView1);
		
	}

}
