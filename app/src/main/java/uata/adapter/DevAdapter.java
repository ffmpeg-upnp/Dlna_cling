package uata.adapter;



import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.uata.dlna_application.R;
import com.uata.dmp.DeviceItem;

import java.util.List;

/**
 * @ClassName:  DevAdapter
 * @Description: (设备的显示 adapter)
 * @author: 王少栋 
 * @date:   2015年7月29日 下午2:39:03 
 */
public class DevAdapter extends ArrayAdapter<DeviceItem> {


  private static final String TAG = "DeviceAdapter";
  private List<DeviceItem> arrDeviceItems;
  public int dmrPosition = 0;
  private LayoutInflater inflater;
  int select = 0;

  /**
   *@Title:  DevAdapter 
   *@Description:   
   *@param:  @param context
   *@param:  @param paramList
   *@param:  @param arg3
   *@throws
   */
  public DevAdapter(Context context, int paramList, List<DeviceItem> arg3) {
    super(context, paramList, arg3);
    // this.mInflater = ((LayoutInflater)paramList.getSystemService("layout_inflater"));

    this.inflater = ((LayoutInflater) context.getSystemService("layout_inflater"));
    this.arrDeviceItems = arg3;
  }

  @Override
  public int getCount() {
    // Auto-generated method stub
    return arrDeviceItems.size();
  }

  @Override
  public DeviceItem getItem(int position) {
    // Auto-generated method stub
    return (DeviceItem) arrDeviceItems.get(position);
  }

  @Override
  public long getItemId(int position) {
    // Auto-generated method stub
    return position;
  }

  /**
   *@Title: changeSelected
   *@Description: 
   *@param: @param positon  
   *@return: void
   *@throws 
   */
  public void changeSelected(int positon) { // 刷新方法
    if (positon != select) {
      select = positon;
      notifyDataSetChanged();
    }
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    DevHolder localDevHolder;
    DeviceItem localDeviceItem;
    View convert = convertView;
    if (convert == null) {
      convert = this.inflater.inflate(R.layout.dmr_item, null);
      localDevHolder = new DevHolder();
      localDevHolder.filename = ((TextView) convert.findViewById(R.id.dmr_name_tv));
      // localDevHolder.checkBox = ((CheckBox)convertView.findViewById(R.id.dmr_cb));
      convert.setTag(localDevHolder);
    } else {
      localDevHolder = (DevHolder) convert.getTag();
      localDeviceItem = this.arrDeviceItems.get(position);
      localDevHolder.filename.setText(localDeviceItem.toString());
      // if ((BaseApplication.deviceItem == null) ||
      // (!BaseApplication.deviceItem.equals(localDeviceItem))){
      //
      // localDevHolder.checkBox.setChecked(true);
      //
      // if ((BaseApplication.dmrDeviceItem != null) &&
      // (BaseApplication.dmrDeviceItem.equals(localDeviceItem)))
      // localDevHolder.checkBox.setChecked(true);
      //
      // }else{
      // localDevHolder.checkBox.setChecked(false);
      // }


      if (select == position) {
        convert.setBackgroundResource(R.color.light_orange); // 选中项背景
      } else {
        convert.setBackgroundResource(R.color.bg_white); // 其他项背景
      }
    }


    return convert;


  }

  public final class DevHolder {
    // public CheckBox checkBox;
    public TextView filename;

    public DevHolder() {}
  }

}
