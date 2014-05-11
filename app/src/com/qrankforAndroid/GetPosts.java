package com.qrankforAndroid;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

class GetPosts extends AsyncTask<String, Integer, List>{
  
  private String json;
  private JSONObject j;
  private MainActivity mActivity;
  private ListView mPostList;
  private View view;
  
    public GetPosts(MainActivity mActivity){
        super();
        this.mActivity = mActivity;
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
      
      // Drawer Navigation �őI�����ꂽ�J�e�S���[�̃f�[�^���擾����B
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
      
      // �G���[���b�Z�[�W�̃r���[���\���ɂ���B
      TextView errorMessage = (TextView) this.mActivity.findViewById(R.id.error_message);
      errorMessage.setVisibility(View.GONE);
      
      // �ǂݍ��݃A�C�R�����\���ɂ���B
      ProgressBar progressBar = (ProgressBar) this.mActivity.findViewById(R.id.progressbar);
      progressBar.setVisibility(View.GONE);
      
      // �f�[�^�̎擾�Ɏ��s�����ꍇ�̓G���[���b�Z�[�W�̃r���[��\������B
      if(result == null){
        errorMessage.setVisibility(View.VISIBLE);
      }else{
        // �f�[�^���A�_�v�^�[�ɓn���A���X�g�r���[�ɕ\��������B
        CustomAdapter customAdapter = new CustomAdapter(this.mActivity, R.layout.fragment_list_item, result);
        mPostList = (ListView) this.mActivity.findViewById(R.id.content_frame);
        mPostList.setOnItemClickListener(new PostItemClickListener(this.mActivity));
        mPostList.setAdapter(customAdapter);
        mPostList.setVisibility(View.VISIBLE);
      }
    }

}

