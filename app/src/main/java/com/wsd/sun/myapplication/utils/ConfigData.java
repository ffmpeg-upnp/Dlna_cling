package com.wsd.sun.myapplication.utils;

import android.graphics.Bitmap;
import android.widget.ArrayAdapter;

import com.wsd.sun.myapplication.model.MediaAudio;
import com.wsd.sun.myapplication.model.MediaImage;
import com.wsd.sun.myapplication.model.MediaVideo;
import com.wsd.sun.myapplication.ui.activity.dmp.ContentItem;
import com.wsd.sun.myapplication.ui.activity.dmp.DeviceItem;

import java.util.ArrayList;
import java.util.List;


/**
 * @ClassName:  ConfigData
 * @Description: 用于传递一些值
 * @author: 王少栋 
 * @date:   2015年7月29日 上午10:21:57 
 */
public class ConfigData {
  public static int audioPosition;
  public static ArrayList<ContentItem> arrlistcontent;
  public static ArrayList<ContentItem> arrlistDirctory;
  public static ArrayList<ContentItem> arrlistAudios;
  public static ArrayList<ContentItem> arrlistPhotos;
  public static ArrayList<ContentItem> arrlistVideos;
  public static ArrayList<DeviceItem> arrDmrList;
  public static ArrayList<MediaVideo> arrMediaVideo;
  public static ArrayList<MediaImage> arrMeidaImage;
  public static ArrayList<MediaAudio> arrMediaAudio;
  public static List<Bitmap> bitMap;

  public static ArrayAdapter<DeviceItem> dmrAdapter;

  public static int photoPosition = 0;
  public static int videoPosition = 0;
  public static int itemPosition;


}
