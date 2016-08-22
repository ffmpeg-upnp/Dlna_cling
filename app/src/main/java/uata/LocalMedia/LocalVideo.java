package uata.LocalMedia;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import android.util.Log;

import com.uata.MediaModel.MediaVideo;

import java.util.ArrayList;

/**
 * @ClassName:  LocalVideo
 * @Description: (这里用一句话描述这个类的作用)
 * @author: 王少栋 
 * @date:   2015年7月29日 下午2:30:39 
 */
public class LocalVideo {
  //
  // private static boolean isLoadLocalVideoOK;
  // private static LocalVideo mLoadLocalVideo;
  // private static Object obj = new Object();
  // private Context mContext;
  private ArrayList<MediaVideo> arrVideos = new ArrayList<MediaVideo>();
  


  // static
  // {
  // isLoadLocalVideoOK = false;
  // }

  // private LocalVideo(Context paramContext)
  // {
  // this.mContext = paramContext;
  // }

  public LocalVideo() {

  }

  // 通过ContentResolver 来获取视频的缩略图
  /**
   * All rights reserved by uata.
   * @Title: getLocalVideo
   * @Description: 
   * @param paramContext
   * @return  
   * @return: ArrayList<MediaVideo>
   * @throws 
   */
  public ArrayList<MediaVideo> getLocalVideo(Context paramContext) {
    this.arrVideos.clear();
    String[] str = {"_data", "_display_name", "duration"};
    ContentResolver cr = paramContext.getContentResolver();
    Cursor cursor = cr.query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, str, null, null, null);
    if (cursor == null || cursor.getCount() == 0) {
      Log.d("getLocalVideo", "first return");
      return null;

    }

    if (cursor.moveToFirst()) {
      int id;
      int nameId;
      int durationId;
      id = cursor.getColumnIndex("_data");
      nameId = cursor.getColumnIndex("_display_name");
      durationId = cursor.getColumnIndex("duration");

      do {
        // 把数据拿到以后存入MediaVideo的数组中
        MediaVideo localMediaVideo = new MediaVideo();
        localMediaVideo.setStrVideoPath(cursor.getString(id));
        localMediaVideo.setStrVideoName(cursor.getString(nameId));
        localMediaVideo.setIntVideoDuration(cursor.getInt(durationId));
        arrVideos.add(localMediaVideo);

        Log.i("movieID", cursor.getString(id));
        Log.i("movieName", cursor.getString(nameId));
      } while (cursor.moveToNext());
      return arrVideos;
    }
    cursor.close();
    return null;
  }

  // public getVideos()
  // {
  // return this.mVideos;
  // }
}
