package com.wsd.sun.myapplication.ui.activity.dmp;


import android.graphics.drawable.Drawable;

import org.fourthline.cling.model.meta.Device;
import org.fourthline.cling.model.types.UDN;


public class DeviceItem {

  private UDN udn;
  private Device device;

  private String[] label;
  private Drawable icon;

  public DeviceItem(Device device) {
    this.udn = device.getIdentity().getUdn();
    this.device = device;
  }

  /**
   *@Title:  DeviceItem 
   *@Description:    
   *@param:  @param device
   *@param:  @param label
   *@throws
   */
  public DeviceItem(Device device, String... label) {
    this.udn = device.getIdentity().getUdn();
    this.device = device;
    this.label = label;
  }

  public UDN getUdn() {
    return udn;
  }

  public Device getDevice() {
    return device;
  }

  public String[] getLabel() {
    return label;
  }

  public void setLabel(String[] label) {
    this.label = label;
  }

  public Drawable getIcon() {
    return icon;
  }

  public void setIcon(Drawable icon) {
    this.icon = icon;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o){ 
      return true;
    }
    if (o == null || getClass() != o.getClass()){
      return false;
    }

    DeviceItem that = (DeviceItem) o;

    if (!udn.equals(that.udn)) {
      return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    return udn.hashCode();
  }

  @Override
  public String toString() {
    String display;

    if (device.getDetails().getFriendlyName() != null){
      display = device.getDetails().getFriendlyName();
    }else{
      display = device.getDisplayString();
    }
    // Display a little star while the device is being loaded (see
    // performance optimization earlier)
    return device.isFullyHydrated() ? display : display + " *";
  }


  // 通过实现Parcelable接口序列化对象的步骤：
  //
  // 1、实现Parcelable接口。
  // 2、并且实现Parcelable接口的public
  // void writeToParcel(Parcel dest, int flags)方法 。
  // 3、自定义类型中必须含有一个名称为CREATOR的静态成员，该成员对象要求实现Parcelable.Creator接口及其方法。
  //
  //
  // 简而言之：通过writeToParcel将你的对象映射成Parcel对象，再通过createFromParcel将Parcel对象映射成你的对象。
  // 也可以将Parcel看成是一个流，通过writeToParcel把对象写到流里面，在通过createFromParcel从流里读取对象，
  // 只不过这个过程需要你来实现，因此写的顺序和读的顺序必须一致。
  // @Override
  // public int describeContents() {
  // // Auto-generated method stub
  // return 0;
  // }
  //
  // @Override
  // public void writeToParcel(Parcel dest, int flags) {
  // dest.write;
  // dest.writeValue(this.device);
  // dest.writeValue(this.icon);
  // dest.writeValue(this.label);
  //
  // }
  //
  // public static final Parcelable.Creator<DeviceItem> CREATOR=new Parcelable.Creator<DeviceItem>()
  // {
  //
  // @Override
  // public DeviceItem createFromParcel(Parcel source) {
  // // Auto-generated method stub
  // return new DeviceItem(source.read);
  // }
  //
  // @Override
  // public DeviceItem[] newArray(int size) {
  // // Auto-generated method stub
  // return new DeviceItem[size];
  // }
  // };
}
