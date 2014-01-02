package com.viral.messimania;

import android.app.Activity;
import android.app.WallpaperManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;

import com.searchboxsdk.android.StartAppSearch;
import com.startapp.android.publish.StartAppAd;
import com.startapp.android.publish.StartAppAd.AdMode;
import com.viral.messimania.service.MyService;

public class MainActivity extends Activity {
	
	private static final String TAG = "MainActivity";
	private Button setWallpaperButton;
	
	private static final int FIVE_SEC = 5000;
	private static final int TEN_SEC = 10000;
	private static final int FIFTEEN_SEC = 15000;
	
	private CheckBox isTouchEnabledCheckBox;
	
	private Spinner refreshRateSpinner;
	
	private static final String TOUCH_ENABLED = "TOUCH_ENABLED";
	private static final String REFERSH_RATE = "REFERSH_RATE";
	
	private int refreshRate = FIVE_SEC;
	
	private static final String DEVELOPER_ID = "106886572";
	private static final String APP_ID = "201543441";
	
	private StartAppAd startAppAd;
	private boolean isTouchEnabled;
	private int refreshRateValue;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		StartAppAd.init(this, DEVELOPER_ID, APP_ID);
		StartAppSearch.init(this, DEVELOPER_ID, APP_ID);
		
		StartAppSearch.showSearchBox(this);
		
		startAppAd = new StartAppAd(this);
		
		startAppAd.showAd();
		startAppAd.loadAd(AdMode.AUTOMATIC);
		
		isTouchEnabledCheckBox = (CheckBox) findViewById(R.id.touch_checkbox);
		setWallpaperButton = (Button) findViewById(R.id.set_wallpaper_button);
		refreshRateSpinner = (Spinner) findViewById(R.id.refresh_rate_spinner);
		
		try {
			refreshRateValue = Integer.parseInt(Utils.getPreference(
					getApplicationContext(), REFERSH_RATE));
			isTouchEnabled = Boolean.parseBoolean(Utils.getPreference(
					getApplicationContext(), TOUCH_ENABLED));
		}
		catch (NumberFormatException e) {
			Log.e("format", "NumberFormatException", e);
		}
		
		switch (refreshRateValue) {
			case FIVE_SEC:
				refreshRateSpinner.setSelection(0);
				break;
			case TEN_SEC:
				refreshRateSpinner.setSelection(1);
				break;
			case FIFTEEN_SEC:
				refreshRateSpinner.setSelection(2);
				break;
			
			default:
				refreshRateSpinner.setSelection(0);
				break;
		}
		
		isTouchEnabledCheckBox.setChecked(isTouchEnabled);
		
		refreshRateSpinner
				.setOnItemSelectedListener(new OnItemSelectedListener() {
					
					@Override
					public void onItemSelected(AdapterView<?> arg0, View arg1,
							int pos, long arg3) {
						switch (pos) {
							case 0:
								refreshRate = FIVE_SEC;
								break;
							case 1:
								refreshRate = TEN_SEC;
								break;
							case 2:
								refreshRate = FIFTEEN_SEC;
								break;
							
							default:
								refreshRate = FIVE_SEC;
								break;
						}
						
					}
					
					@Override
					public void onNothingSelected(AdapterView<?> arg0) {
						// TODO Auto-generated method stub
						
					}
				});
		
		setWallpaperButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				Utils.setPreference(getApplicationContext(), REFERSH_RATE,
						String.valueOf(refreshRate));
				Utils.setPreference(getApplicationContext(), TOUCH_ENABLED,
						String.valueOf(isTouchEnabledCheckBox.isChecked()));
				
				Intent intent = new Intent(
						WallpaperManager.ACTION_CHANGE_LIVE_WALLPAPER);
				intent.putExtra(
						WallpaperManager.EXTRA_LIVE_WALLPAPER_COMPONENT,
						new ComponentName(getApplicationContext(),
								MyService.class));
				startActivity(intent);
				finish();
			}
		});
		
	}
	
	@Override
	public void onResume() {
		super.onResume();
		startAppAd.onResume();
	}
	
	@Override
	public void onPause() {
		super.onPause();
		startAppAd.onPause();
		
	}
	
}
