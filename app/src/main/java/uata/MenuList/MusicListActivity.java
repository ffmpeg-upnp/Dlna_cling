package uata.MenuList;


import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.uata.MediaModel.MediaAudio;
import com.uata.application.ConfigData;
import com.uata.dlna_application.R;
import com.uata.dmc.DMCControl;
import com.uata.dmc.DMCControlMessage;
import com.uata.dmp.ContentItem;
import com.uata.dmr.MusicControl;

import org.fourthline.cling.android.AndroidUpnpService;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName:  MusicListActivity
 * @Description: (本地音乐的列表)
 * @author: 王少栋 
 * @date:   2015年7月29日 下午2:41:12 
 */
public class MusicListActivity extends Activity {

  private ArrayList<ContentItem> arrMusicList;
  private ArrayList<MediaAudio> audioList;
  public static final  String TAG = "MusicListActivity";
  private ListView mlviList;
  private ContentAdapter madapterContent;
  private Context mcontext;
  private AndroidUpnpService upnpService;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.music);
    initData();
    getActionBar().setDisplayHomeAsUpEnabled(true);
    ActionBar bar = getActionBar();
    bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#D1F5EB")));
  }

  private Handler handler = new Handler() {

    @Override
    public void handleMessage(Message msg) {
      switch (msg.what) {
        case 0:
         
          break;
        case 1:
          // Log.v("bitmap", bitmap+"");
          // // Log.v("mList", mList+"");
          // initView();
          break;
        default:
          break;
      }
    }
  };

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.music_list, menu);
    return true;
  }


  /**
   * @Title: initData
   * @Description:  (初始化数值)   *@param:     *@return: void
   * @throws 
   */

  private void initData() {
    DMCControl dmcControl = null;
    arrMusicList = ConfigData.arrlistcontent;
    audioList = ConfigData.arrMediaAudio;
    Log.v(TAG, this.arrMusicList + "");
    mlviList = (ListView) findViewById(R.id.music_List);

    if (audioList == null || arrMusicList == null) {
      Toast.makeText(MusicListActivity.this, "还没有音乐", 0).show();
    } else {

      this.madapterContent = new ContentAdapter(MusicListActivity.this, arrMusicList, audioList);
      mlviList.setAdapter(madapterContent);
      DMCControlMessage.runing = false;
      if (dmcControl != null) {
        dmcControl.stop(Boolean.FALSE);
      }
    }
    mlviList.setOnItemClickListener(contentItemClickListener);
  }


  OnItemClickListener contentItemClickListener = new OnItemClickListener() {

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
      // Auto-generated method stub
      String uri = MusicListActivity.this.audioList.get(arg2).getStruri();
      Long albumId = MusicListActivity.this.audioList.get(arg2).getLongalbumid();
      String name1 = MusicListActivity.this.audioList.get(arg2).getStrName();
      String artist = MusicListActivity.this.audioList.get(arg2).getStrartist();
      Log.e(TAG, "name11111---" + name1);
      // DMCControl dmcControl = null;
      // ContentItem localItem =MusicListActivity.this.MusicList.get(arg2);
      //
      // if(localItem.isContainer())
      // {
      // MusicListActivity.this.upnpService.getControlPoint().execute(
      // new ContentBrowseActionCallback( MusicListActivity.this,
      // localItem.getService(), localItem
      // .getContainer(),
      // MusicListActivity.this.MusicList,
      // MusicListActivity.this.handler));
      //
      //
      //
      // }else{
      // Log.v("localContentItem",localItem.getItem()+"" );
      // List<Res> localList =localItem.getItem().getResources();
      // Res type = localList.get(0);
      // MimeType localMimeType = type.getProtocolInfo().getContentFormatMimeType();
      // String str=localMimeType.getType();

      Intent intent = new Intent();
      intent.setClass(MusicListActivity.this, MusicControl.class);
      // intent.putExtra("path", localItem.getItem().getFirstResource().getValue());
      intent.putExtra("path", uri);
      intent.putExtra("ID", String.valueOf(arg2));
      intent.putExtra("album_id", String.valueOf(albumId));
      intent.putExtra("name", name1);
      intent.putExtra("artist", artist);
      // intent.putExtra("name", localItem.toString());
      //
      // intent.putExtra("currentContentFormatMimeType", localMimeType.toString());
      // try {
      // intent.putExtra("metaData",new GenerateXml().generate(localItem).toString());
      // } catch (Exception e) {
      // // Auto-generated catch block
      // e.printStackTrace();
      // }

      startActivity(intent);
    }
    // }
  };

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();
    if (id == R.id.action_settings) {
      return true;
    }
    if (id == android.R.id.home) {
      finish();
      return true;
    }

    return super.onOptionsItemSelected(item);
  }



  class ContentAdapter extends BaseAdapter {

    private static final String TAG = "ContentAdapter";
    private Bitmap audioIcon;
    private Context context;
    public int dmrPosition = 0;
//    private Bitmap folderIcon;
//    private Bitmap imageIcon;
//    private ArrayList<ContentItem> mDeviceItems;
//    private LayoutInflater mInflater;
//    private Bitmap videoIcon;
//    private String str1;
//    private ImageLoader mImageLoader;
//    private ImageLoadingListener animateFirstListener;
//    private DisplayImageOptions options;
//    private List<Bitmap> mBitmaps;
    private ArrayList<MediaAudio> mAudio;
    private List<ContentItem> listMusicItem;


    String path;

    public ContentAdapter(Context context, List<ContentItem> arg3, ArrayList<MediaAudio> mAudio) {
      this.context = context;
      // this.mDeviceItems=arg2;
      // this.videos=mVideos;
//      this.imageIcon = BitmapFactory.decodeResource(getResources(), R.drawable.icon_image);
//      this.videoIcon = BitmapFactory.decodeResource(getResources(), R.drawable.icon_video);
      this.audioIcon = BitmapFactory.decodeResource(getResources(), R.drawable.music);
//      this.folderIcon = BitmapFactory.decodeResource(getResources(), R.drawable.icon_folder);

      this.listMusicItem = arg3;
      this.mAudio = mAudio;

    }

    @Override
    public int getCount() {
      // Auto-generated method stub
      // return mDeviceItems.size();
      return mAudio.size();
    }

    @Override
    public Object getItem(int position) {
      // Auto-generated method stub
      // return mDeviceItems.get(position);
      return mAudio.get(position);
    }

    @Override
    public long getItemId(int position) {
      // Auto-generated method stub
      return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
      // Auto-generated method stub
      ViewHolder viewHolder;
      View viewTmp = convertView;
      if (viewTmp == null) {
        
        viewTmp = LayoutInflater.from(MusicListActivity.this).inflate(R.layout.audios, null);
        viewHolder = new ViewHolder();
        // viewHolder.tuImg=(ImageView) convertView.findViewById(R.id.img_tu);
        viewHolder.tuText = (TextView) viewTmp.findViewById(R.id.tv_tu);
        viewHolder.artist = (TextView) viewTmp.findViewById(R.id.tv_artist);
        viewHolder.album = (TextView) viewTmp.findViewById(R.id.tv_album);

        // System.out.println(this.mAudio.get(position).getMusicName());
        viewHolder.tuText.setText(this.mAudio.get(position).getStrName());
        viewHolder.artist.setText(this.mAudio.get(position).getStrartist());
        viewHolder.album.setText(this.mAudio.get(position).getStralbum());
        // viewHolder.tuText.setText(videos.get(position).getVideoName());
        // viewHolder.tuImg.setImageBitmap(audioIcon);
        viewTmp.setTag(viewHolder);
        // Log.v("bitmap", mBitmaps+"");
        // viewHolder.tuImg.setImageBitmap(mBitmaps.get(position));
        // str1 = videos.get(position).getVideoPath();

        // if(localItem.isContainer())
        // {
        // MovieActivity.this.upnpService.getControlPoint().execute(
        // new ContentBrowseActionCallback(MovieActivity.this,
        // localItem.getService(), localItem
        // .getContainer(),
        // MovieActivity.this.MovieList,
        // MovieActivity.this.handler));
        //
        //
        //
        // }else{
        // Log.v("localContentItem",localItem.getItem()+"" );
        // List<Res> localList =localItem.getItem().getResources();
        // Res type = localList.get(0);
        // MimeType localMimeType = type.getProtocolInfo().getContentFormatMimeType();
        // String str=localMimeType.getType();
        // // int i = localItem.getItem().getProperties().size();
        // // System.out.println(i);
        // // Property app = localItem.getItem().getProperties().get(i);
        // // String ic_image = app.getValue().toString();
        // // Log.v(ic_image, ic_image);
        // // Log.v(ic_image, ic_image);
        // // Log.v(ic_image, ic_image);
        // Log.v("Itemposition", "position=="+position);
        //
        // // ID=String.valueOf(position);
        // // name=localItem.toString();
        // // playURL=localItem.getItem().getFirstResource().getValue();
        // // currentContentFormatMimeType=localMimeType.toString();
        // // try {
        // // metaData=new GenerateXml().generate(localItem).toString();
        // // } catch (Exception e) {
        // // // Auto-generated catch block
        // // e.printStackTrace();
        // // }
        //
        //
        //
        //
        //
        // if(str.equals("video")){
        // str1=localItem.getItem().getFirstResource().getValue();
        // // viewHolder.tuImg.setImageBitmap(videoIcon);
        // // for (int i = 0; i < mDeviceItems.size(); i++) {
        // // Bitmap bitmap= ThumbnailUtils.createVideoThumbnail(str1, 30);
        // // mBitmaps.add(bitmap);
        // // System.out.println(mBitmaps);
        // // }
        // // Bitmap bit = MovieActivity.this.getVideoThumbnail(str1, 100, 80, 40);
        // // System.out.println(bit);
        // //viewHolder.tuImg.setImageBitmap();
        // }
        // // }else
        // // {
        // // str1=localItem.getItem().getFirstResource().getValue();
        // // viewHolder.tuImg.setImageBitmap(audioIcon);
        // // }
        // // System.out.println(str1);
        // }
        // mImageLoader.displayImage(str1,
        // viewHolder.tuImg, options,
        // this.animateFirstListener);
      } else {
        // localMediaVideo = (MediaVideo)this.videos.get(position);
        // path = localMediaVideo.getVideoPath();
        viewHolder = (ViewHolder) viewTmp.getTag();

      }
      // Log.d("listview", "str1-----"+str1);

      // for(int i=0;i<this.videos.size();i++)
      // {
      //
      // mImageLoader.displayImage(str1,
      // viewHolder.tuImg, options,
      // this.animateFirstListener);
      // }
      // viewHolder.tuText.setText(mList.get(position).getDisplayName());
      //
      // // viewHolder.tuImg.setImageBitmap(videoIcon);
      // Log.v("bitmap", mList.get(position).getDisplayName()+"");
      // viewHolder.tuImg.setImageBitmap(mBitmaps.get(position));

      return viewTmp;
    }

  }


  private static class ViewHolder {
    private TextView artist;
    private TextView album;
    private TextView tuText;
  }
}
