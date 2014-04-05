/*
 * Copyright 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.qrankforAndroid;

import java.util.Locale;
import java.net.URL;
import java.net.MalformedURLException;
import java.io.*;
import java.util.*;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.SearchManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.Context;
import android.os.Bundle;
import android.os.AsyncTask;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.TextView;
import android.widget.ProgressBar;
import android.util.Log;
import android.net.Uri;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.HttpStatus;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;

import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;


public class MainActivity extends Activity {
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ListView mPostList;
    private ActionBarDrawerToggle mDrawerToggle;

    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private String[] mPlanetTitles;
    private TextView textView;
    
    private getPosts task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        mTitle = mDrawerTitle = getTitle();
        
        // リソースから文字列を取得している
        mPlanetTitles = getResources().getStringArray(R.array.planets_array);
        
        // activity_main.xml にあるレイアウトを呼び出す
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        
        // activity_main.xml にあるリストビューを呼び出す
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        
        // アダプターのセット
        mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                R.layout.drawer_list_item, mPlanetTitles));
        
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.drawable.ic_drawer,  /* nav drawer image to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description for accessibility */
                R.string.drawer_close  /* "close drawer" description for accessibility */
                ) {
            public void onDrawerClosed(View view) {
                getActionBar().setTitle(mTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            public void onDrawerOpened(View drawerView) {
                getActionBar().setTitle(mDrawerTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        
        // 最初に position が 0 の内容を表示する。
        if (savedInstanceState == null) {
        	// API経由でデータを取得する。
            task = new getPosts();
            task.execute("0");
        }
    }
    
    public class CustomData {
        private String title;
        private String qiita_post_id;
        private String stockCount;
        private String hatenaBookmarkCount;
        
        public CustomData(String title, String qiita_post_id, String stockCount, String hatenaBookmarkCount){
        	this.title = title;
        	this.qiita_post_id = qiita_post_id;
        	this.stockCount = stockCount;
        	this.hatenaBookmarkCount = hatenaBookmarkCount;
        }
        
        public String getTitle(){
            return this.title;
        }
     
        public String getQiitaPostId(){
            return this.qiita_post_id;
        }
        
        public String getStockCount(){
            return this.stockCount;
        }
        
        public String getHatenaBookmarkCount(){
        	return this.hatenaBookmarkCount;
        }
        
    }
    
    public class CustomAdapter extends ArrayAdapter<CustomData>{
    	 private LayoutInflater layoutInflater_;
    	 
    	 public CustomAdapter(Context context, int textViewResourceId, List<CustomData> objects) {
    		 super(context, textViewResourceId, objects);
    		 layoutInflater_ = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    	 }
    	 
    	 @Override
    	 public View getView(int position, View convertView, ViewGroup parent) {
	    	 CustomData item = (CustomData)getItem(position);
	    	 
	    	 if (null == convertView) {
	    		 convertView = layoutInflater_.inflate(R.layout.fragment_list_item, null);
	    	 }
	    	 
	    	 ((TextView)convertView.findViewById(R.id.post_title)).setText(item.getTitle());
	    	 ((TextView)convertView.findViewById(R.id.counts)).setText(item.getStockCount() + " ストック, " + item.getHatenaBookmarkCount() + " はてブ");
	    	 
	    	 return convertView;
    	 }
    }
    
    // オプションメニューを作っているところ。
    // XMLファイルから定義を呼び出している。
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // MenuInflater inflater = getMenuInflater();
        // inflater.inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        // menu.findItem(R.id.action_websearch).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle action buttons
        switch(item.getItemId()) {
        default:
            return super.onOptionsItemSelected(item);
        }
    }

    
    private class PostItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        	ListView listView = (ListView)parent;
        	final CustomData item = (CustomData)listView.getAdapter().getItem(position);
       		Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("http://qiita.com/items/" + item.getQiitaPostId()));
       		startActivity(i);
        }
    }
    
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            
        	// ListView を非表示にする。
        	mPostList = (ListView) findViewById(R.id.content_frame);
        	mPostList.setVisibility(View.GONE);
        	
        	// ProgressBar を表示する。
        	ProgressBar progressBar = (ProgressBar)findViewById(R.id.progressbar);
            progressBar.setVisibility(View.VISIBLE);
        	
        	// API経由でデータを取得する。
            task = new getPosts();
            task.execute(Integer.toString(position));
        	
        	mDrawerLayout.closeDrawer(mDrawerList);
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    class getPosts extends AsyncTask<String, Integer, List>{
        
    	private String json;
    	private JSONObject j;
    	
        public getPosts(){
            super();
        }
        
        @Override
        protected List doInBackground(String... param){
        	Bitmap bitmap;
        	String days = "0";
        	
        	if(param[0].equals("1")){
        		days = "7";
        	}else if(param[0].equals("2")){
        		days = "30";
        	}else{
        		days = "0";
        	}
        	
        	// Drawer Navigation で選択されたカテゴリーのデータを取得する。
        	String uri = "http://qrank.wbsrv.net/api/?days=" + days;
        	DefaultHttpClient client = new DefaultHttpClient();
        	HttpGet get = new HttpGet(uri);
        	HttpResponse response = null;
        	
        	try {
        	    response = client.execute(get);
        	    if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
        	        json = EntityUtils.toString(response.getEntity());
        	        try{
        	        	JSONArray rootObject = new JSONArray(json);
        	        	List<CustomData> objects = new ArrayList<CustomData>();
        	        	
        	        	for(int i = 0; i < rootObject.length(); i++){
        	        		JSONObject j = rootObject.getJSONObject(i);
    	        			
        	                CustomData listElement = new CustomData(
        	                		j.getString("title"),
        	                		j.getString("qiita_post_id"),
        	                		j.getString("stockCount"),
        	                		j.getString("hatenaBookmarkCount")
        	                );
        	                objects.add(listElement);
        	        	}
        	        	
    	            	return objects;
    	        		
        	        }catch(JSONException e){
        	        	Log.d("custom", e.getMessage());
        	        }
        	    } else {
        	        Log.d("custom", "Request failed.");
        	    }
        	}
        	catch (ClientProtocolException e) {
        	    e.printStackTrace();
        	}
        	catch (IOException e) {
        	    e.printStackTrace();
        	}
            
            return null;
        }
        
        @Override
        protected void onPostExecute(List result){
        	
        	// エラーメッセージのビューを非表示にする。
    		TextView errorMessage = (TextView) findViewById(R.id.error_message);
    		errorMessage.setVisibility(View.GONE);
        	
    		// 読み込みアイコンを非表示にする。
        	ProgressBar progressBar = (ProgressBar)findViewById(R.id.progressbar);
            progressBar.setVisibility(View.GONE);
        	
            // データの取得に失敗した場合はエラーメッセージのビューを表示する。
            if(result == null){
        		errorMessage.setVisibility(View.VISIBLE);
        	}else{
        		
        		// データをアダプターに渡し、リストビューに表示させる。
	            CustomAdapter customAdapter = new CustomAdapter(MainActivity.this, R.layout.fragment_list_item, result);
	        	mPostList = (ListView) findViewById(R.id.content_frame);
	        	mPostList.setOnItemClickListener(new PostItemClickListener());
	        	mPostList.setAdapter(customAdapter);
	        	mPostList.setVisibility(View.VISIBLE);
        	}
        }

    }
    
}
