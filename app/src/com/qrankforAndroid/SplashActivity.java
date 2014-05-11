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
		
        // �X�v���b�V���̃��C�A�E�g��\��������B
        setContentView(R.layout.splash);
        
        // �A�N�e�B�r�e�B��]������B
        Handler hdl = new Handler();
        hdl.postDelayed(new splashHandler(), 1000);
	}
	
	class splashHandler implements Runnable {
		public void run() {
			// �X�v���b�V��������Ɏ��s����Activity���w�肷��B
			Intent intent = new Intent(getApplication(), MainActivity.class);
			startActivity(intent);
			
			// SplashActivity���I��������B
			SplashActivity.this.finish();
		}
	}
}
