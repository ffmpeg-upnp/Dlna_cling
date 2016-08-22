package uata.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.uata.dlna_application.R;

public class Fragment3 extends Fragment {

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
    super.onCreateView(inflater, container, savedInstanceState);
    return inflater.inflate(R.layout.fragment_3, container, false);
  }

}
