package com.wsd.sun.myapplication.ui.activity.dms;


import android.util.Log;

import com.wsd.sun.myapplication.ui.activity.dmp.ContentItem;

import org.fourthline.cling.support.contentdirectory.AbstractContentDirectoryService;
import org.fourthline.cling.support.contentdirectory.ContentDirectoryErrorCode;
import org.fourthline.cling.support.contentdirectory.ContentDirectoryException;
import org.fourthline.cling.support.contentdirectory.DIDLParser;
import org.fourthline.cling.support.model.BrowseFlag;
import org.fourthline.cling.support.model.BrowseResult;
import org.fourthline.cling.support.model.DIDLContent;
import org.fourthline.cling.support.model.SortCriterion;
import org.fourthline.cling.support.model.container.Container;
import org.fourthline.cling.support.model.item.Item;

import java.util.ArrayList;


/**
 * @ClassName:  ContentDirectoryService
 * @Description: (遍历服务器下 各节点的内容)
 */
public class ContentDirectoryService extends AbstractContentDirectoryService {
  // ContentDirectoryService:内容目录服务
  private static final  String LOGTAG = "MediaServer-CDS";
  private ArrayList<ContentItem> listDirtory;

  @Override
  public BrowseResult search(String containerId, String searchCriteria, String filter,
      long firstResult, long maxResults, SortCriterion[] orderBy) throws ContentDirectoryException {
    // You can override this method to implement searching!
    return super.search(containerId, searchCriteria, filter, firstResult, maxResults, orderBy);
  }

  @Override
  public BrowseResult browse(String objectID, BrowseFlag browseFlag, String filter,
      long firstResult, long maxResults, SortCriterion[] orderby) throws ContentDirectoryException {
    try {

      DIDLContent didl = new DIDLContent();

      ContentNode contentNode = ContentTree.getNode(objectID);

      Log.v(LOGTAG, "someone's browsing id: " + objectID);

      if (contentNode == null) {
        return new BrowseResult("", 0, 0);
      }

      if (contentNode.isItem()) {
        didl.addItem(contentNode.getItem());

        Log.v(LOGTAG, "returing item: " + contentNode.getItem().getTitle());

        return new BrowseResult(new DIDLParser().generate(didl), 1, 1);
      } else {
        if (browseFlag == BrowseFlag.METADATA) {
          didl.addContainer(contentNode.getContainer());

//          Log.v(LOGTAG, "returning metadata of container: " + contentNode.getContainer().getTitle());

          return new BrowseResult(new DIDLParser().generate(didl), 1, 1);
        } else {
          // 对容器的device 和远端的device进行遍历
          for (Container container : contentNode.getContainer().getContainers()) {
            didl.addContainer(container);

            Log.v(LOGTAG, "getting child container: " + container.getTitle());
            // this.listDirtory.add(new ContentItem(container));
            // ConfigData.listDirctory=this.listDirtory;
            // Log.v("listDirtory", ConfigData.listDirctory+"");


          }
          for (Item item : contentNode.getContainer().getItems()) {
            didl.addItem(item);

            Log.v(LOGTAG, "getting child item: " + item.getTitle());
          }
          return new BrowseResult(new DIDLParser().generate(didl), contentNode.getContainer()
              .getChildCount(), contentNode.getContainer().getChildCount());
        }

      }

    } catch (Exception ex) {
      throw new ContentDirectoryException(ContentDirectoryErrorCode.CANNOT_PROCESS, ex.toString());
    }

  }
}
