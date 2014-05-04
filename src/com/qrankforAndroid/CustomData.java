package com.qrankforAndroid;

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
