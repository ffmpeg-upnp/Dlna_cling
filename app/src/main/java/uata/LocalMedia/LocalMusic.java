package uata.LocalMedia;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.util.Log;

import com.uata.MediaModel.MediaAudio;
import com.uata.dlna_application.R;

import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;


/**
 * @ClassName:  LocalMusic
 * @Description: (获取本地的音乐)
 * @author: 王少栋 
 * @date:   2015年7月29日 下午2:30:20 
 */
public class LocalMusic {

  // private static boolean isLoadLocalVideoOK;
  // private static LocalVideo mLoadLocalVideo;
  // private static Object obj = new Object();
  // private Context mContext;
  private ArrayList<MediaAudio> arrAudio = new ArrayList<MediaAudio>();
  private static final Uri ALBUMARTURI = Uri.parse("content://media/external/audio/albumart");

  // static
  // {
  // isLoadLocalVideoOK = false;
  // }

  // private LocalVideo(Context paramContext)
  // {
  // this.mContext = paramContext;
  // }

  // public LocalVideo()
  // {
  //
  // }
  
  /**
   * All rights reserved by uata.
   * @Title: getLocalAudio
   * @Description: 通过ContentResolver 来获取视频的缩略图 
   * @param paramContext
   * @return  
   * @return: ArrayList<MediaAudio>
   * @throws 
   */
  public ArrayList<MediaAudio> getLocalAudio(Context paramContext) {
    this.arrAudio.clear();

    String[] str =
        new String[] {MediaStore.Audio.Media.DATA, MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.TITLE, MediaStore.Audio.Media.ALBUM,
            MediaStore.Audio.Media.ARTIST, MediaStore.Audio.Media.ALBUM_ID};

    ContentResolver cr = paramContext.getContentResolver();
    Cursor cursor = cr.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, str, null, null, null);
    if (cursor == null || cursor.getCount() == 0) {
      Log.d("getLocalVideo", "first return");
      return null;

    }

    if (cursor.moveToFirst()) {
      int id;
      int uri;
      int title;
      int album;
      int artist;
      int albumId;
      id = cursor.getColumnIndex(MediaStore.Audio.Media._ID);
      uri = cursor.getColumnIndex(MediaStore.Audio.Media.DATA);
      title = cursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
      album = cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM);
      artist = cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
      albumId = cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID);

      do {
        // 把数据拿到以后存入MediaAudio的数组中



        MediaAudio localMediaAudio = new MediaAudio();
        localMediaAudio.setId(cursor.getInt(id));
        localMediaAudio.setStruri(cursor.getString(uri));
        localMediaAudio.setStrName(cursor.getString(title));
        localMediaAudio.setStralbum(cursor.getString(album));
        localMediaAudio.setStrartist(cursor.getString(artist));
        localMediaAudio.setLongalbumid(cursor.getLong(albumId));
        arrAudio.add(localMediaAudio);



        Log.i("AudionID", cursor.getString(title));
        Log.i("AudioName", cursor.getString(artist));


      } while (cursor.moveToNext());
      return arrAudio;
    }
    cursor.close();
    return null;
  }

  // public getVideos()
  // {
  // return this.mVideos;
  // }

  // private String getAlbumArt(int album_id,Context paramContext) {
  // String mUriAlbums = "content://media/external/audio/albums";
  // String[] projection = new String[] { "album_art" };
  // Cursor cur = paramContext.getContentResolver().query(
  // Uri.parse(mUriAlbums + "/" + Integer.toString(album_id)),
  // projection, null, null, null);
  // String album_art = null;
  // if (cur.getCount() > 0 && cur.getColumnCount() > 0) {
  // cur.moveToNext();
  // album_art = cur.getString(0);
  // }
  // cur.close();
  // cur = null;
  // return album_art;
  // }

  /**
   * 获取默认专辑图片
   * 
   * @param context
   * @return
   */
  public static Bitmap getDefaultArtwork(Context context, boolean small) {
    Options opts = new Options();
    opts.inPreferredConfig = Bitmap.Config.RGB_565;
    if (small) { // 返回小图片
      return BitmapFactory.decodeStream(context.getResources().openRawResource(R.drawable.music),
          null, opts);
    }
    return BitmapFactory.decodeStream(context.getResources().openRawResource(R.drawable.music),
        null, opts);
  }


  /**
   * 从文件当中获取专辑封面位图
   *
   * @param context
   * @param songid
   * @param albumid
   * @return
   */
  private static Bitmap getArtworkFromFile(Context context, long songid, long albumid) {
    Bitmap bm = null;
    if (albumid < 0 && songid < 0) {
      throw new IllegalArgumentException("Must specify an album or a song id");
    }
    try {
      Options options = new Options();
      FileDescriptor fd = null;
      if (albumid < 0) {
        Uri uri = Uri.parse("content://media/external/audio/media/" + songid + "/albumart");
        ParcelFileDescriptor pfd = context.getContentResolver().openFileDescriptor(uri, "r");
        if (pfd != null) {
          fd = pfd.getFileDescriptor();
        }
      } else {
        Uri uri = ContentUris.withAppendedId(ALBUMARTURI, albumid);
        ParcelFileDescriptor pfd = context.getContentResolver().openFileDescriptor(uri, "r");
        if (pfd != null) {
          fd = pfd.getFileDescriptor();
        }
      }
      options.inSampleSize = 1;
      // 只进行大小判断
      options.inJustDecodeBounds = true;
      // 调用此方法得到options得到图片大小
      BitmapFactory.decodeFileDescriptor(fd, null, options);
      // 我们的目标是在800pixel的画面上显示
      // 所以需要调用computeSampleSize得到图片缩放的比例
      options.inSampleSize = 100;
      // 我们得到了缩放的比例，现在开始正式读入Bitmap数据
      options.inJustDecodeBounds = false;
      options.inDither = false;
      options.inPreferredConfig = Bitmap.Config.ARGB_8888;

      // 根据options参数，减少所需要的内存
      bm = BitmapFactory.decodeFileDescriptor(fd, null, options);
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
    return bm;
  }

  /**
   * 获取专辑封面位图对象
   *
   * @param context
   * @param song_id
   * @param album_id
   * @param allowdefalut
   * @return
   */
  public static Bitmap getArtwork(Context context, long song_id, long album_id,
      boolean allowdefalut, boolean small) {
    if (album_id < 0) {
      if (song_id < 0) {
        Bitmap bm = getArtworkFromFile(context, song_id, -1);
        if (bm != null) {
          return bm;
        }
      }
      if (allowdefalut) {
        return getDefaultArtwork(context, small);
      }
      return null;
    }

    ContentResolver res = context.getContentResolver();
    Uri uri = ContentUris.withAppendedId(ALBUMARTURI, album_id);
    if (uri != null) {
      InputStream in = null;
      try {
        in = res.openInputStream(uri);
        Options options = new Options();
        // 先制定原始大小
        options.inSampleSize = 1;
        // 只进行大小判断
        options.inJustDecodeBounds = true;
        // 调用此方法得到options得到图片的大小
        BitmapFactory.decodeStream(in, null, options);
        /** 我们的目标是在你N pixel的画面上显示。 所以需要调用computeSampleSize得到图片缩放的比例 **/
        /** 这里的target为800是根据默认专辑图片大小决定的，800只是测试数字但是试验后发现完美的结合 **/
        if (small) {
          options.inSampleSize = computeSampleSize(options, 300);
        } else {
          options.inSampleSize = computeSampleSize(options, 600);
        }
        // 我们得到了缩放比例，现在开始正式读入Bitmap数据
        options.inJustDecodeBounds = false;
        options.inDither = false;
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        in = res.openInputStream(uri);
        return BitmapFactory.decodeStream(in, null, options);
      } catch (FileNotFoundException e) {
        Bitmap bm = getArtworkFromFile(context, song_id, album_id);
        if (bm != null) {
          if (bm.getConfig() == null) {
            bm = bm.copy(Bitmap.Config.RGB_565, false);
            if (bm == null && allowdefalut) {
              return getDefaultArtwork(context, small);
            }
          }
        } else if (allowdefalut) {
          bm = getDefaultArtwork(context, small);
        }
        return bm;
      } finally {
        try {
          if (in != null) {
            in.close();
          }
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
    return null;
  }

  /**
   * 对图片进行合适的缩放
   * 
   * @param options
   * @param target
   * @return
   */
  public static int computeSampleSize(Options options, int target) {
    int width = options.outWidth;
    int high = options.outHeight;
    int candidateW = width / target;
    int candidateH = high / target;
    int candidate = Math.max(candidateW, candidateH);
    if (candidate == 0) {
      return 1;
    }
    if (candidate > 1) {
      if ((width > target) && (width / candidate) < target) {
        candidate -= 1;
      }
    }
    if (candidate > 1) {
      if ((high > target) && (high / candidate) < target) {
        candidate -= 1;
      }
    }
    return candidate;
  }


}
