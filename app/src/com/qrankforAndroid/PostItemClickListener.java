package com.qrankforAndroid;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class PostItemClickListener implements ListView.OnItemClickListener {
  private MainActivity mActivity;
  
  public PostItemClickListener(MainActivity mActivity){
    this.mActivity = mActivity;
  }
  
  @Override
  public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    ListView listView = (ListView)parent;
    final CustomData item = (CustomData)listView.getAdapter().getItem(position);
    Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("http://qiita.com/items/" + item.getQiitaPostId()));
    this.mActivity.startActivity(i);
  }
}
