package uata.adapter;





import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.uata.dlna_application.R;
import com.uata.dmp.DeviceItem;

import java.util.ArrayList;

public class GroupAdapter extends BaseAdapter {

  private Context context;
  private ArrayList<DeviceItem> dmrItem;
  private LayoutInflater inflater;

  /**
   *@Title:  GroupAdapter 
   *@Description:   构造参数
   *@param:  @param context
   *@param:  @param dmrItem
   *@throws
   */
  public GroupAdapter(Context context, ArrayList<DeviceItem> dmrItem) {
    this.context = context;
    this.dmrItem = dmrItem;
    this.inflater = ((LayoutInflater) context.getSystemService("layout_inflater"));
  }

  @Override
  public int getCount() {
    // Auto-generated method stub
    return dmrItem.size();
  }

  @Override
  public Object getItem(int position) {
    // Auto-generated method stub
    return dmrItem.get(position);
  }

  @Override
  public long getItemId(int position) {
    // Auto-generated method stub
    return position;
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    DevHolder viewHolder;
    View convert = convertView;
    if (convert == null) {

      convert = this.inflater.inflate(R.layout.text_item, null);
      viewHolder = new DevHolder();
      viewHolder.filename = (TextView) convert.findViewById(R.id.text_item);
      convert.setTag(viewHolder);
      viewHolder.filename.setText(dmrItem.get(position).toString());
    } else {
      convert.getTag();

    }
    return convert;
  }

  public final class DevHolder {
    // public CheckBox checkBox;
    public TextView filename;


  }

}
