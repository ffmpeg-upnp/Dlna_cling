package com.wsd.sun.myapplication.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.wsd.sun.myapplication.R;


/**
 * @author zenghui.he
 * @email hezh@dxyer.com
 * @date 2014-2-8
 */
public class BaseActivity extends AppCompatActivity {

    protected Context mContext;
   // public CompositeSubscription mSubscription;
    private static final String TAG = "BaseActivity";
   // private LinkedME linkedME;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
//        if (mSubscription == null) {
//            mSubscription = new CompositeSubscription();
//        }
    }

//    protected void initActionBar(String title) {
//        View view = findViewById(R.id.actionbar);
//        ImageView backView = (ImageView) view.findViewById(R.id.actionbar_back);
//        TextView titleView = (TextView) view.findViewById(R.id.actionbar_title);
//        if (CheckUtil.isNotEmpty(title)) {
//            titleView.setText(title);
//        }
//        backView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onBackPressed();
//            }
//        });
//    }

    public void onResume() {
        super.onResume();
       // MobclickAgent.onResume(this);
    }

    public void onPause() {
        super.onPause();
//        MobclickAgent.onPause(this);
//        Glide.get(getApplicationContext()).clearMemory();
//        if (mSubscription != null) {
//            mSubscription.clear();
//        }
    }

    @Override
    public void onBackPressed() {
        finish();
       // overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
    }

    public void initToolbar(int titleRes) {
        String title = getString(titleRes);
        initToolbar(title);
    }

    public void initToolbar(int titleRes, int backIconRes) {
        String title = getString(titleRes);
        initToolbar(title, backIconRes);
    }

    public void initToolbar(String title) {
        initToolbar(title, 0);
    }

    public void initToolbar(String title, int backIconRes) {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView titleText = (TextView) toolbar.findViewById(R.id.title);
        titleText.setText(title);
        setSupportActionBar(toolbar);
        setActionBar(toolbar, backIconRes);
    }

    public void initToolbar(String title, String rightMenu, final OnToolbarMenuClickListener listener) {
        initToolbar(title, 0, rightMenu, listener);
    }

//    public void initToolbarWithRightMenuBg(String title, @DrawableRes int menuBg, final OnToolbarMenuClickListener
//            listener) {
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        TextView titleText = (TextView) toolbar.findViewById(R.id.title);
//        titleText.setText(title);
//        TextView menuText = (TextView) toolbar.findViewById(R.id.toolbar_right_text_menu);
//        menuText.setVisibility(View.VISIBLE);
//        menuText.setBackgroundResource(menuBg);
//        menuText.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                listener.onItemClick();
//            }
//        });
//        setSupportActionBar(toolbar);
//        setActionBar(toolbar, 0);
//    }

    public void initToolbar(String title, int backIconRes, String rightMenu, final OnToolbarMenuClickListener
            listener) {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView titleText = (TextView) toolbar.findViewById(R.id.title);
        titleText.setText(title);
        TextView menuText = (TextView) toolbar.findViewById(R.id.toolbar_right_text_menu);
        menuText.setVisibility(View.VISIBLE);
        menuText.setText(rightMenu);
        menuText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick();
            }
        });
        setSupportActionBar(toolbar);
        setActionBar(toolbar, backIconRes);
    }

    public void initToolbar(int titleRes, int rightMenuRes, final OnToolbarMenuClickListener listener) {
        initToolbar(getString(titleRes), getString(rightMenuRes), listener);
    }

    public void initToolbar(String title, int rightMenuRes, final OnToolbarMenuClickListener listener) {
        initToolbar(title, getString(rightMenuRes), listener);
    }

    private void setActionBar(Toolbar toolbar, int backIconRes) {
        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(false);
            ab.setDisplayShowTitleEnabled(false);
            ImageView back = (ImageView) toolbar.findViewById(R.id.toolbar_back);
            if (backIconRes != 0) {
                back.setImageResource(backIconRes);
            } else {
                back.setImageResource(R.mipmap.ic_toolbar_back);
            }
            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    onBackPressed();
                }
            });
        }
    }

    public void setTitle(String title) {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView titleText = (TextView) toolbar.findViewById(R.id.title);
        titleText.setText(title);
    }

    public interface OnToolbarMenuClickListener {
        void onItemClick();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onToolbarBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void onToolbarBackPressed() {
        onBackPressed();
    }


    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG, "onStart: " + this.getClass().getSimpleName());
//        try {
//            //如果消息未处理则会初始化initSession，因此不会每次都去处理数据，不会影响应用原有性能问题
//            if (!LinkedME.getInstance().isHandleStatus()) {
//                Log.i(TAG, "LinkedME +++++++ initSession... " + this.getClass().getSimpleName());
//                //初始化LinkedME实例
//                linkedME = LinkedME.getInstance();
//                //初始化Session，获取Intent内容及跳转参数
//                linkedME.initSession(simpleInitListener, this.getIntent().getData(), this);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }
    @Override
    public void onStop() {
        super.onStop();
        Log.i(TAG, "onStop: " + this.getClass().getSimpleName());
//        if (linkedME != null) {
//            linkedME.closeSession(new LMReferralCloseListener() {
//                @Override
//                public void onCloseFinish() {
//                }
//            });
//        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        Log.i(TAG, "onNewIntent: " + this.getClass().getSimpleName());
     //   simpleInitListener.reset();
        setIntent(intent);
    }


    /**
     * <p>解析深度链获取跳转参数，开发者自己实现参数相对应的页面内容</p>
     * <p>通过LinkProperties对象调用getControlParams方法获取自定义参数的HashMap对象,
     * 通过创建的自定义key获取相应的值,用于数据处理。</p>
     */
    //。
//    LMSimpleInitListener simpleInitListener = new LMSimpleInitListener() {
//        @Override
//        public void onSimpleInitFinished(LMUniversalObject lmUniversalObject, LinkProperties linkProperties, LMError error) {
//            try {
//                Log.i(TAG, "开始处理deep linking数据... " + this.getClass().getSimpleName());
//                if (error != null) {
//                    Log.i("LinkedME-Demo", "LinkedME初始化失败. " + error.getMessage());
//                } else {
//
//                    //LinkedME SDK初始化成功，获取跳转参数，具体跳转参数在LinkProperties中，和创建深度链接时设置的参数相同；
//                    Log.i("LinkedME-Demo", "LinkedME初始化完成");
//
//                    if (linkProperties != null) {
//                        Log.e("LinkedME-Demo", "Channel " + linkProperties.getChannel());
//                        Log.e("LinkedME-Demo", "control params " + linkProperties.getControlParams());
//
//                        //获取自定义参数封装成的hashmap对象
//                        HashMap<String, String> hashMap = linkProperties.getControlParams();
//
//                        //获取传入的参数
//                        String view = hashMap.get("msg");
//                        String bmsg = hashMap.get("bmsg");
//                        String title = "";
//                        String shareContent = "";
//                        String url_path = "";
//                        //http://apptest.superwan.cn/shop/1005
//                        Log.e("LinkedME-Demo","text:"+view);
//                        if("true".equals(bmsg)){ //为true 则跳转服务中心
//                            startActivity(ServiceCenterActivity.intent(mContext));
//                        }else{
//                            startActivity(InfoActivity.intent(mContext,view));
//
//                        }
//                        finish();
//                      //  view="http://192.168.16.58/appsite/community/list_article_detail.php?comm_id=1200";
////                        title = "linkedPage";
////                        shareContent = "LinkedME产品已经被众多移动应用垂青, 比如Uber、滴滴、36Kr、小饭桌、每天、道口贷等等, 更多应用正在集成中...;";
////                        url_path ="partner";
//                      //  view="http://192.168.16.64/appsite/community/list_article.php?area_id=330100";
//
//
//                    }
//
//                    if (lmUniversalObject != null) {
//                        Log.i("LinkedME-Demo", "title " + lmUniversalObject.getTitle());
//                        Log.i("LinkedME-Demo", "control " + linkProperties.getControlParams());
//                        Log.i("ContentMetaData", "metadata " + lmUniversalObject.getMetadata());
//                    }
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//    };


}
