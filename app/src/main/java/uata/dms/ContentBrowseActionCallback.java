/*
 * Copyright (C) 2010 fourthline GmbH, Switzerland
 * 
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU Lesser General Public License as published by the Free Software Foundation, either version 3
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License along with this program.
 * If not, see <http://www.gnu.org/licenses/>.
 */

package uata.dms;

import android.app.Activity;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.widget.ArrayAdapter;

import com.uata.application.ConfigData;
import com.uata.dmp.ContentItem;

import org.fourthline.cling.model.action.ActionInvocation;
import org.fourthline.cling.model.message.UpnpResponse;
import org.fourthline.cling.model.meta.Service;
import org.fourthline.cling.support.contentdirectory.callback.Browse;
import org.fourthline.cling.support.model.BrowseFlag;
import org.fourthline.cling.support.model.DIDLContent;
import org.fourthline.cling.support.model.SortCriterion;
import org.fourthline.cling.support.model.container.Container;
import org.fourthline.cling.support.model.item.Item;

import java.util.ArrayList;
import java.util.logging.Logger;

/**
 * Updates a tree model after querying a backend <em>ContentDirectory</em> service.
 * 
 * @author Christian Bauer
 */

public class ContentBrowseActionCallback extends Browse {



  private static  final Logger LOG = Logger.getLogger(ContentBrowseActionCallback.class.getName());

  private Service service;
  private Container container;
  private ArrayAdapter<ContentItem> listAdapter;
  private Activity activity;
  private FragmentActivity frag;
  private Fragment frag1;

  private ArrayList<ContentItem> listcontent = new ArrayList();
  private ArrayList<ContentItem> listmusic = new ArrayList();
  private ArrayList<ContentItem> listphoto = new ArrayList();
  private ArrayList<ContentItem> listvideo = new ArrayList();
  private Handler mhandler;
  private int num;



  // public ContentBrowseActionCallback(Activity activity, Service service,
  // Container container, ArrayAdapter<ContentItem> listadapter, ArrayList<ContentItem>
  // listcontent,Handler mHandler) {
  // super(service, container.getId(), BrowseFlag.DIRECT_CHILDREN, "*", 0,
  // null, new SortCriterion(true, "dc:title"));
  // this.activity = activity;
  // this.service = service;
  // this.container = container;
  // this.listAdapter = listadapter;
  // this.mHandler=mHandler;
  // }
  /**
   *@Title:  ContentBrowseActionCallback 
   *@Description:   
   *@param:  @param activity
   *@param:  @param service
   *@param:  @param container
   *@param:  @param listcontent
   *@param:  @param mHandler
   *@throws
   */
  public ContentBrowseActionCallback(Activity activity, Service service, Container container,
      ArrayList<ContentItem> listcontent, Handler mHandler) {
    super(service, container.getId(), BrowseFlag.DIRECT_CHILDREN, "*", 0, null, new SortCriterion(
        true, "dc:title"));
    this.activity = activity;
    this.service = service;
    this.container = container;
    this.listcontent = listcontent;
    this.mhandler = mHandler;
  }

  // public ContentBrowseActionCallback(FragmentActivity fragActivity, Service service,
  // Container container, ArrayList<ContentItem> listcontent) {
  // super(service, container.getId(), BrowseFlag.DIRECT_CHILDREN, "*", 0,
  // null, new SortCriterion(true, "dc:title"));
  // this.frag = fragActivity;
  // this.service = service;
  // this.container = container;
  //
  // this.listcontent=listcontent;
  //
  //
  // }
  /**
   *@Title:  ContentBrowseActionCallback 
   *@Description:    
   *@param:  @param fragM
   *@param:  @param service
   *@param:  @param container
   *@param:  @param listcontent
   *@param:  @param mHandler
   *@throws
   */
  public ContentBrowseActionCallback(Fragment fragM, Service service, Container container,
                                     ArrayList<ContentItem> listcontent, Handler mHandler) {
    super(service, container.getId(), BrowseFlag.DIRECT_CHILDREN, "*", 0, null, new SortCriterion(
        true, "dc:title"));
    this.frag1 = fragM;
    this.service = service;
    this.container = container;

    this.listcontent = listcontent;
    this.mhandler = mHandler;

  }

  /**
   *@see org.fourthline.cling.support.contentdirectory.callback.Browse#received(org.fourthline.cling.model.action.ActionInvocation, org.fourthline.cling.support.model.DIDLContent)
   *@Title: received
   *@Description: 
   *@param: @param actionInvocation
   *@param: @param didl  
   *@throws 
   */
  public void received(final ActionInvocation actionInvocation, final DIDLContent didl) {
    LOG.fine("Received browse action DIDL descriptor, creating tree nodes");
    // Log.v("into", "into container--"+i);

    frag1.getActivity().runOnUiThread(new Runnable() {


      public void run() {


        // Iterator localIterator3 = null ;
        // Iterator localIterator2=null;
        try {
          // listAdapter.clear();
          // ContentBrowseActionCallback.this.listcontent.clear();
//          Log.v("11111", "111111");
//          Log.v("11111", didl.getContainers() + "");
          // Containers first
          for (Container childContainer : didl.getContainers()) {
//           LOG.fine("add child container " + childContainer.getTitle());
            // listAdapter.add(new ContentItem(childContainer, service));
            ContentBrowseActionCallback.this.listcontent.add(new ContentItem(childContainer,
                ContentBrowseActionCallback.this.service));
            ConfigData.arrlistDirctory = ContentBrowseActionCallback.this.listcontent;


            // Log.d(" listDirctory","listSize---"+ ConfigData.listDirctory);

            // ContentBrowseActionCallback.this.listcontent.add(new ContentItem(childContainer
            // ,ContentBrowseActionCallback.this.service));
          }

          // Now items
          for (Item childItem : didl.getItems()) {
            LOG.fine("add child item" + childItem.getTitle());
            // listAdapter.add(new ContentItem(childItem, service));
            ContentBrowseActionCallback.this.listvideo.add(new ContentItem(childItem,
                ContentBrowseActionCallback.this.service));


            // Log.v("listvideo","listSize----"+ ConfigData.listcontent );
            ConfigData.arrlistcontent = ContentBrowseActionCallback.this.listvideo;
          }


        } catch (Exception ex) {

          // while(true){
          // log.fine("Creating DIDL tree nodes failed: " + ex);
          // actionInvocation.setFailure(new ActionException(
          // ErrorCode.ACTION_FAILED,
          // "Can't create list childs: " + ex, ex));
          // failure(actionInvocation, null);
          //
          // // ContentBrowseActionCallback.this.mHandler.sendEmptyMessage(0);
          //
          //
          // Log.v("error", ContentBrowseActionCallback.this.container+"");
          // Log.v("error", ContentBrowseActionCallback.this.container.getItems()+"");
          // Log.v("error", localIterator3+"");
          // for (Item childItem : didl.getItems()) {
          // log.fine("add child item" + childItem.getTitle());
          // listAdapter.add(new ContentItem(childItem, service));
//          Log.v("11111", "111111");
//          System.out.println("遍历发生问题");
        }

        // Item localItem = (Item) localIterator3.next();
        // ContentItem localContentItem = new ContentItem(localItem,
        // ContentBrowseActionCallback.this.service);

        // if ((localContentItem.getItem().getTitle().toString() == null)
        // || (localContentItem.getItem().getResources() == null))
        // {
        //
        // List localList = localContentItem.getItem().getResources();
        // if ((localList.size() == 0)
        // || (((Res) localList.get(0)).getProtocolInfo() == null)
        // || (((Res) localList.get(0)).getProtocolInfo()
        // .getContentFormat() == null))
        //
        // if (((Res) localList.get(0))
        // .getProtocolInfo()
        // .getContentFormat()
        // .substring(0,
        // ((Res) localList.get(0)).getProtocolInfo()
        // .getContentFormat().indexOf("/"))
        // .equals("image")) {
        //
        // ContentBrowseActionCallback.this.listphoto
        // .add(new ContentItem(
        // localItem,
        // ContentBrowseActionCallback.this.service));
        //
        // ContentBrowseActionCallback.this.listcontent
        // .add(new ContentItem(
        // localItem,
        // ContentBrowseActionCallback.this.service));
        //
        // }
        // if (((Res) localList.get(0))
        // .getProtocolInfo()
        // .getContentFormat()
        // .substring(
        // 0,
        // ((Res) localList.get(0)).getProtocolInfo()
        // .getContentFormat().indexOf("/"))
        // .equals("audio")) {
        //
        // ContentBrowseActionCallback.this.listmusic
        // .add(new ContentItem(
        // localItem,
        // ContentBrowseActionCallback.this.service));
        //
        // ContentBrowseActionCallback.this.listcontent
        // .add(new ContentItem(
        // localItem,
        // ContentBrowseActionCallback.this.service));
        //
        // }
        //
        // ContentBrowseActionCallback.this.listvideo
        // .add(new ContentItem(localItem,
        // ContentBrowseActionCallback.this.service));
        //
        // ContentBrowseActionCallback.this.listcontent
        // .add(new ContentItem(localItem,
        // ContentBrowseActionCallback.this.service));
        // }
        // }
        // }

      }
    });
    num = num + 1;
    mhandler.sendEmptyMessageDelayed(0, 200);

  }

  public void updateStatus(final Status status) {}

  /**
   *@see org.fourthline.cling.controlpoint.ActionCallback#failure(org.fourthline.cling.model.action.ActionInvocation, org.fourthline.cling.model.message.UpnpResponse, String)
   *@Title: failure
   *@Description: 
   *@param: @param invocation
   *@param: @param operation
   *@param: @param defaultMsg  
   *@throws 
   */
  @Override
  public void failure(ActionInvocation invocation, UpnpResponse operation, final String defaultMsg) {
    
  }
}
