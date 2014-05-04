package com.qrankforAndroid;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

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