package uata.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.uata.dmp.DeviceItem;

import java.util.ArrayList;

public class DevicesAdapter extends BaseAdapter {
  private ArrayList<DeviceItem> deviceItems;
  public int dmrPosition = 0;
  private LayoutInflater layoutInflater;
  /**
   *@Title:  DevicesAdapter 
   *@Description:    
   *@param:  @param paramContext
   *@param:  @param paramArrayList
   *@param:  @param paramListView
   *@throws
   */
  public DevicesAdapter(Context paramContext, ArrayList<DeviceItem> paramArrayList,
      ListView paramListView) {
    this.deviceItems = paramArrayList;
    this.layoutInflater = ((LayoutInflater) paramContext.getSystemService("layout_inflater"));
  }

  public int getCount() {
    return this.deviceItems.size();
  }

  public Object getItem(int paramInt) {
    return this.deviceItems.get(paramInt);
  }

  public long getItemId(int paramInt) {
    return paramInt;
  }
  @Override
  public View getView(int paramInt, View convertView, ViewGroup paramViewGroup) {
    Holder localHolder;
    View convert = convertView;
    if (convert == null) {
      convert = this.layoutInflater.inflate(2130903046, null);
      localHolder = new Holder();
      localHolder.filename = ((TextView) convert.findViewById(2131361820));
      convert.setTag(localHolder);
    } else {
      localHolder = (Holder) convert.getTag();
      DeviceItem localDeviceItem = (DeviceItem) this.deviceItems.get(paramInt);
      localHolder.filename.setTextColor(-1);
      localHolder.filename.setText(localDeviceItem.toString());
    }
    return convert;
  }

  public void setSelectDevices() {
    this.dmrPosition = 0;
  }

  class Holder {
    TextView filename;
    ImageView imageView;

    Holder() {}
  }
}
