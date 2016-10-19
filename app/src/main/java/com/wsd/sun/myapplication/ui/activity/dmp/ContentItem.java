package com.wsd.sun.myapplication.ui.activity.dmp;


import org.fourthline.cling.model.meta.Device;
import org.fourthline.cling.model.meta.Service;
import org.fourthline.cling.support.model.DIDLObject;
import org.fourthline.cling.support.model.container.Container;
import org.fourthline.cling.support.model.item.Item;


public class ContentItem {
  private Device device;
  private Service service;
  private DIDLObject content;
  private String id;
  private Boolean isContainer;

  /**
   *@Title:  ContentItem 
   *@Description:    
   *@param:  @param container
   *@param:  @param service
   *@throws
   */
  public ContentItem(Container container, Service service) {
    // Auto-generated constructor stub
    this.service = service;
    this.content = container;
    this.id = container.getId();
    this.isContainer = true;
  }

  /**
   *@Title:  ContentItem 
   *@Description:    
   *@param:  @param container
   *@throws
   */
  public ContentItem(Container container) {
    // Auto-generated constructor stub

    this.content = container;
    this.id = container.getId();
    this.isContainer = true;
  }

  /**
   *@Title:  ContentItem 
   *@Description:   
   *@param:  @param item
   *@param:  @param service
   *@throws
   */
  public ContentItem(Item item, Service service) {
    // Auto-generated constructor stub
    this.service = service;
    this.content = item;
    this.id = item.getId();
    this.isContainer = false;
  }

  /**
   * All rights reserved by uata.
   * @Title: getContainer
   * @Description: 
   * @return  
   * @return: Container
   * @throws 
   */
  public Container getContainer() {
    if (isContainer)
    {
      return (Container) content;
    }else{
      return null;
    }
  }

  public Item getItem() {
    if (isContainer){
      
      return null;
    }else{
      return (Item) content;
    }
  }

  public Service getService() {
    return service;
  }

  public Boolean isContainer() {
    return isContainer;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    
    if (o == null || getClass() != o.getClass()){
      return false;
    }

    ContentItem that = (ContentItem) o;

    if (!id.equals(that.id)) {
      return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    return content.hashCode();
  }

  @Override
  public String toString() {
    return content.getTitle();
  }
}
