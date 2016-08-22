package uata.dms;



import org.fourthline.cling.support.model.container.Container;
import org.fourthline.cling.support.model.item.Item;


/**
 * @ClassName:  ContentNode
 * @Description: (dms的资源的属性)
 * @author: 王少栋 
 * @date:   2015年7月29日 下午1:46:05 
 */
public class ContentNode {
  private Container container;
  private Item item;
  private String id;
  private String fullPath;
  private boolean isItem;

  /**
   *@Title:  ContentNode 
   *@Description:  ContentNode
   *@param:  @param id
   *@param:  @param container
   *@throws
   */
  public ContentNode(String id, Container container) {
    this.id = id;
    this.container = container;
    this.fullPath = null;
    this.isItem = false;
  }

  /**
   *@Title:  ContentNode 
   *@Description:    ContentNode
   *@param:  @param id
   *@param:  @param item
   *@param:  @param fullPath
   *@throws
   */
  public ContentNode(String id, Item item, String fullPath) {
    this.id = id;
    this.item = item;
    this.fullPath = fullPath;
    this.isItem = true;
  }

  public String getId() {
    return id;
  }

  public Container getContainer() {
    return container;
  }

  public Item getItem() {
    return item;
  }

  /**
   *@Title: getFullPath
   *@Description: getFullPath 
   *@param: @return  
   *@return: String
   *@throws 
   */
  public String getFullPath() {
    if (isItem && fullPath != null) {
      return fullPath;
    }
    return null;
  }

  public boolean isItem() {
    return isItem;
  }
}
