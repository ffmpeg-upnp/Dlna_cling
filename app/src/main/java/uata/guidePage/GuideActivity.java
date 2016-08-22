package uata.guidePage;



import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.uata.adapter.ViewPagerAdapter;
import com.uata.dlna_application.R;
import com.uata.fragment.Fragment1;
import com.uata.fragment.Fragment2;
import com.uata.fragment.Fragment3;
import com.uata.fragment.Fragment4;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName:  GuideActivity
 * @Description: (引导页)
 * @author: 王少栋 
 * @date:   2015年7月29日 上午11:37:05 
 */
public class GuideActivity extends FragmentActivity {

  private ViewPager viewPage;
  private Fragment1 mfragment1;
  private Fragment2 mfragment2;
  private Fragment3 mfragment3;
  private Fragment4 mfragment4;
  private PagerAdapter madapterPg;
  private RadioGroup dotLayout;
  private List<Fragment> marrListFragment = new ArrayList<Fragment>();


  private BroadcastReceiver guideReceiver = new BroadcastReceiver() {

    @Override
    public void onReceive(Context context, Intent intent) {
      // Auto-generated method stub

      if (intent.getAction().equals("com.yutao.business.GUIDE")) {

      }
    }


  };

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_guide);

    // IntentFilter localIntent=new IntentFilter();
    // localIntent.addAction("com.yutao.business.GUIDE");
    initView();
    viewPage.setOnPageChangeListener(new MyPagerChangeListener());
    // registerReceiver(guideReceiver, localIntent);

  }

  private void initView() {
    dotLayout = (RadioGroup) findViewById(R.id.advertise_point_group);
    viewPage = (ViewPager) findViewById(R.id.viewpager);
    mfragment1 = new Fragment1();
    mfragment2 = new Fragment2();
    mfragment3 = new Fragment3();
    mfragment4 = new Fragment4();
    marrListFragment.add(mfragment1);
    marrListFragment.add(mfragment2);
    marrListFragment.add(mfragment3);
    marrListFragment.add(mfragment4);
    madapterPg = new ViewPagerAdapter(getSupportFragmentManager(), marrListFragment);
    viewPage.setAdapter(madapterPg);

  }

  public class MyPagerChangeListener implements OnPageChangeListener {

    public void onPageSelected(int position) {

    }

    public void onPageScrollStateChanged(int arg0) {

    }

    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
      ((RadioButton) dotLayout.getChildAt(position)).setChecked(true);
    }

  }
}
