package com.qrankforAndroid;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.Window;
import android.view.WindowManager;
import android.os.Handler;

public class SplashActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
        // スプラッシュのレイアウトを表示させる。
        setContentView(R.layout.splash);
        
        // アクティビティを転送する。
        Handler hdl = new Handler();
        hdl.postDelayed(new splashHandler(), 1000);
	}
	
	class splashHandler implements Runnable {
		public void run() {
			// スプラッシュ完了後に実行するActivityを指定する。
			Intent intent = new Intent(getApplication(), MainActivity.class);
			startActivity(intent);
			
			// SplashActivityを終了させる。
			SplashActivity.this.finish();
		}
	}
}
