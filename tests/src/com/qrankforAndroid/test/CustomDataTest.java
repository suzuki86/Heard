package com.qrankforAndroid.test;

import junit.framework.TestCase;
import com.qrankforAndroid.CustomData;

public class CustomDataTest extends TestCase {
  public void testConstructor(){
    CustomData cd = new CustomData("title", "111", "222", "333");
    assertEquals("title", cd.getTitle());
    assertEquals("111", cd.getQiitaPostId());
    assertEquals("222", cd.getStockCount());
    assertEquals("333", cd.getHatenaBookmarkCount());
  }
}
