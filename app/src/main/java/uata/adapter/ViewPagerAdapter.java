package uata.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;


/**
 * @ClassName:  ViewPagerAdapter
 * @Description:(引导页的滑动)
 * @author: 王少栋 
 * @date:   2015年7月29日 下午2:54:08 
 */
public class ViewPagerAdapter extends FragmentPagerAdapter {

  private List<Fragment> fragmentList = new ArrayList<Fragment>();

  public ViewPagerAdapter(FragmentManager fm) {
    super(fm);
  }

  public ViewPagerAdapter(FragmentManager fragmentManager, List<Fragment> arrayList) {
    super(fragmentManager);
    this.fragmentList = arrayList;
  }

  @Override
  public Fragment getItem(int arg0) {
    return fragmentList.get(arg0);
  }

  @Override
  public int getCount() {
    return fragmentList.size();
  }

}
