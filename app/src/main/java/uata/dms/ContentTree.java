package uata.dms;

import org.fourthline.cling.support.model.WriteStatus;
import org.fourthline.cling.support.model.container.Container;

import java.util.HashMap;


/**
 * @ClassName:  ContentTree
 * @Description: (创建输节点 )
 * @author: 王少栋 
 * @date:   2015年7月29日 下午1:46:46 
 */
public class ContentTree {

  public static final  String ROOT_ID = "0";
  public static final  String VIDEO_ID = "1";
  public static final  String AUDIO_ID = "2";
  public static final  String IMAGE_ID = "3";
  public static final String VIDEO_PREFIX = "video-item-";
  public static final  String AUDIO_PREFIX = "audio-item-";
  public static final  String IMAGE_PREFIX = "image-item-";

  private static HashMap<String, ContentNode> contentMap = new HashMap<String, ContentNode>();

  private static ContentNode rootNode = createRootNode();

  public ContentTree(){
   
  }

  protected static ContentNode createRootNode() {
    // create root container
    // 创建一个根 来创建节点
    Container root = new Container();
    root.setId(ROOT_ID);
    root.setParentID("-1");
    root.setTitle("GNaP MediaServer root directory");
    root.setCreator("GNaP Media Server");
    root.setRestricted(true);// 受限制 为true
    root.setSearchable(true);// 可被搜索 为true
    root.setWriteStatus(WriteStatus.NOT_WRITABLE);
    root.setChildCount(0);
    ContentNode rootNode = new ContentNode(ROOT_ID, root);// 权限：是否可写 为不可写
    contentMap.put(ROOT_ID, rootNode);
    return rootNode;// 返回创建完成的节点
  }

  public static ContentNode getRootNode() {
    return rootNode;
  }

  /**
   *@Title: getNode
   *@Description: getid 
   *@param: @param id
   *@param: @return  
   *@return: ContentNode
   *@throws 
   */
  public static ContentNode getNode(String id) {
    if (contentMap.containsKey(id)) {
      return contentMap.get(id);
    }
    return null;
  }

  public static boolean hasNode(String id) {
    return contentMap.containsKey(id);
  }

  /**
   *@Title: addNode
   *@Description: addNode 
   *@param: @param ID
   *@param: @param Node  
   *@return: void
   *@throws 
   */
  public static void addNode(String astrId, ContentNode aconNode) {
    contentMap.put(astrId, aconNode);
  }
}
