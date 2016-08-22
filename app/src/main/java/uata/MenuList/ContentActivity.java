package uata.MenuList;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.uata.dlna_application.R;
import com.uata.dlna_application.R.id;
import com.uata.dlna_application.R.layout;
import com.uata.dlna_application.R.menu;

/**
 * @ClassName:  ContentActivity
 * @Description: (客服类)
 * @author: 王少栋 
 * @date:   2015年7月29日 下午1:57:13 
 */
public class ContentActivity extends Activity {

  private TextView content;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.control_activity);
    initView();

  }

  private void initView() {
    content = (TextView) findViewById(R.id.tv_content);
    content.setText("客服qq：×××××××××");
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.content, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();
    if (id == R.id.action_settings) {
      return true;
    }
    return super.onOptionsItemSelected(item);
  }
}
