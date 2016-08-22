package uata.dmc;

import android.util.Log;

import com.uata.dmp.ContentItem;

import org.fourthline.cling.model.XMLUtil;
import org.fourthline.cling.support.model.DIDLObject;
import org.fourthline.cling.support.model.DescMeta;
import org.fourthline.cling.support.model.Res;
import org.fourthline.cling.support.model.container.Container;
import org.fourthline.cling.support.model.item.Item;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.StringWriter;
import java.util.Iterator;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

/**
 * @ClassName:  GenerateXml
 * @Description: (解析)
 * @author: 王少栋 
 * @date:   2015年7月29日 上午10:50:12 
 */
public class GenerateXml {
  public static final String UNKNOWN_TITLE = "Unknown Title";
  private static final Logger LOG = Logger.getLogger(GenerateXml.class.getName());

  // 添加DIDLObject.Class 类
  protected void appendClass(Document document, Element element,
      DIDLObject.Class objParam, String strParam, boolean blnparam) {
    Element localElement =
        XMLUtil.appendNewElementIfNotNull(document,element, strParam,
          objParam.getValue(), "urn:schemas-upnp-org:metadata-1-0/upnp/");
    if ((objParam.getFriendlyName() != null) && (objParam.getFriendlyName().length() > 0)){
      
      localElement.setAttribute("name", objParam.getFriendlyName());
    }
    if ( blnparam){
      
      localElement.setAttribute("includeDerived", Boolean.toString(objParam.isIncludeDerived()));
    }
  }

  // 添加DIDLObject的内容
  protected void appendProperties(Document document, Element element,
      DIDLObject paramDidlObject, String strParam,
      Class<? extends DIDLObject.Property.NAMESPACE> paramClass, String str) {
    Log.v("appendProperties", "appendProperties is call ");
    DIDLObject.Property[] arrProperty = paramDidlObject.getPropertiesByNamespace(paramClass);
    int len = arrProperty.length;
    for (int j = 0;; j++) {
      if (j >= len) {
        return;
      }
      DIDLObject.Property localProperty = arrProperty[j];
      Element localElement =
          document.createElementNS(str,
            strParam + ":" + localProperty.getDescriptorName());
     element.appendChild(localElement);
      localProperty.setOnElement(localElement);
    }
  }

  // item 调用的两个方法 然后进入dom的解析的模式
  public String generate(ContentItem contentItem) throws Exception {
    Log.e("generate", "" + contentItem);
    return generate(contentItem, false);
  }

  public String generate(ContentItem contentItem, boolean bln) throws Exception {
    Log.e("generate", "" + contentItem + "" + bln);
    return documentToString(buildDom(contentItem, bln), true);
  }


  protected String booleanToInt(boolean paramBoolean) {
    if (paramBoolean) {
      return "1";
    }
    return "0";
  }

  // 用DocumentBuilderFactory对资源进行解析
  protected Document buildDom(ContentItem contentItem, boolean bln) throws Exception {

    // ArrayList<Document> doc=new ArrayList<Document>();
   
    DocumentBuilderFactory localDocumentBuilderFactory = DocumentBuilderFactory.newInstance();
    localDocumentBuilderFactory.setNamespaceAware(true);
    Document localDocument = localDocumentBuilderFactory.newDocumentBuilder().newDocument();
    generateRoot(contentItem, localDocument, bln);
    // doc.add(localDocument);

    Log.v("1111111111111", "结束解析--" + localDocument);
    
    return localDocument;

  }

  /*
   * 
   * setAttributeNS() 方法创建或改变具有命名空间的属性。
   * 
   * xmlDoc=loadXMLDoc("books_ns.xml");
   * 
   * x=xmlDoc.getElementsByTagName("book")[0]; ns="http://www.w3school.com.cn/edition/";
   * 
   * x.setAttributeNS(ns,"edition","first");
   * 
   * document.write(x.getAttributeNS(ns,"edition"));
   * 
   * 输出： first
   */

  protected void generateRoot(ContentItem contentItem, Document paramDocument,
      boolean paramBoolean) {
    Log.d("generateRoot", "ContentItem--" + contentItem + "--Document--" + paramDocument
        + "--boolean--" + paramBoolean);
    Element localElement =
        paramDocument.createElementNS("urn:schemas-upnp-org:metadata-1-0/DIDL-Lite/", "DIDL-Lite");
    paramDocument.appendChild(localElement);
    localElement.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:upnp",
        "urn:schemas-upnp-org:metadata-1-0/upnp/");
    localElement.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:dc",
        "http://purl.org/dc/elements/1.1/");
    localElement.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:dlna",
        "urn:schemas-dlna-org:metadata-1-0/");
    localElement.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:sec",
        "http://www.sec.co.kr/");
    Log.e("paramContentItem.getContainer()--", "" + contentItem.getContainer());
    // if (paramContentItem.getContainer() != null){
    // generateContainer(paramContentItem.getContainer(), paramDocument,
    // localElement, paramBoolean);
    // }
    Log.e("paramContentItem.getItem()--", "" + contentItem.getItem());
    if (contentItem.getItem() != null) {
      generateItem(contentItem.getItem(), paramDocument, localElement);
    }
  }

  protected String documentToString(Document document, boolean blnparam) throws Exception {
    Transformer localTransformer = TransformerFactory.newInstance().newTransformer();
    if (blnparam){
      localTransformer.setOutputProperty("omit-xml-declaration", "yes");
    }
    StringWriter localStringWriter = new StringWriter();
    localTransformer.transform(new DOMSource(document), new StreamResult(localStringWriter));
    return localStringWriter.toString();
  }

  // 资源的的容器
  protected void generateContainer(Container paramContainer, Document paramDocument,
      Element paramElement, boolean paramBoolean) {
    Log.v("generateContainer", "generateContainer is call");
    if (paramContainer.getClazz() == null){
//      throw new RuntimeException("Missing 'upnp:class' element for container: "
//          + paramContainer.getId());
    }
    Element localElement = XMLUtil.appendNewElement(paramDocument, paramElement, "container");
    
    if (paramContainer.getId() == null){

    localElement.setAttribute("id", paramContainer.getId());
    }
    if (paramContainer.getParentID() == null){

    localElement.setAttribute("parentID", paramContainer.getParentID());
    }
    Log.v("generateContainer", "" + paramContainer.getId());
    Log.v("generateContainer", "" + paramContainer.getParentID());
    if (paramContainer.getChildCount() != null){
      localElement.setAttribute("childCount",
          Integer.toString(paramContainer.getChildCount().intValue()));
    localElement.setAttribute("restricted", booleanToInt(paramContainer.isRestricted()));
    localElement.setAttribute("searchable", booleanToInt(paramContainer.isSearchable()));
    String str = paramContainer.getTitle();
    
    if (str == null) {
      str = "Unknown Title";
    }
    XMLUtil.appendNewElementIfNotNull(paramDocument, localElement, "dc:title", str,
        "http://purl.org/dc/elements/1.1/");
    XMLUtil.appendNewElementIfNotNull(paramDocument, localElement, "dc:creator",
        paramContainer.getCreator(), "http://purl.org/dc/elements/1.1/");
    XMLUtil.appendNewElementIfNotNull(paramDocument, localElement, "upnp:writeStatus",
        paramContainer.getWriteStatus(), "urn:schemas-upnp-org:metadata-1-0/upnp/");
    Log.v("contain ", "contain to appendclass");
    appendClass(paramDocument, localElement, paramContainer.getClazz(), "upnp:class", false);
    }
    Iterator localIterator1 = paramContainer.getSearchClasses().iterator();
    Iterator localIterator2;
    Iterator localIterator3;
    Iterator localIterator4;
    Iterator localIterator5;

    /**
     * 
     * error
     * */
    while (!localIterator1.hasNext()) {
      localIterator2 = paramContainer.getCreateClasses().iterator();
      if (localIterator2.hasNext()) {
        appendProperties(paramDocument, localElement, paramContainer, "upnp",
            DIDLObject.Property.UPNP.NAMESPACE.class, "urn:schemas-upnp-org:metadata-1-0/upnp/");
        appendProperties(paramDocument, localElement, paramContainer, "dc",
            DIDLObject.Property.DC.NAMESPACE.class, "http://purl.org/dc/elements/1.1/");
      }
      if (paramBoolean) {

        localIterator5 = paramContainer.getItems().iterator();
        if (localIterator5.hasNext()) {

          localIterator3 = paramContainer.getResources().iterator();

          if (localIterator3.hasNext()) {

            localIterator4 = paramContainer.getDescMetadata().iterator();

            while (!localIterator4.hasNext()) {

              appendClass(paramDocument, localElement, (DIDLObject.Class) localIterator1.next(),
                  "upnp:searchClass", true);

              appendClass(paramDocument, localElement, (DIDLObject.Class) localIterator2.next(),
                  "upnp:createClass", true);

              Item localItem = (Item) localIterator5.next();
              if (localItem == null){

              }
              generateItem(localItem, paramDocument, localElement);
              Res localRes = (Res) localIterator3.next();
              if (localRes != null) {
                generateResource(localRes, paramDocument, localElement);
              }

            }
            DescMeta localDescMeta = (DescMeta) localIterator4.next();
            if (localDescMeta != null){
              
              generateDescMetadata(localDescMeta, paramDocument, localElement);
            }


          }

        }
      }
    }
    }
  

  protected void generateDescMetadata(DescMeta paramDescMeta, Document paramDocument,
      Element paramElement) {
    Log.e("generateItem to generateDescMetadata", "" + paramDescMeta + "" + paramDocument + ""
        + "paramElement");
    if (paramDescMeta.getId() == null)
    {
      
    }
    if (paramDescMeta.getNameSpace() == null){
      
    }
     
    Element localElement = XMLUtil.appendNewElement(paramDocument, paramElement, "desc");
    localElement.setAttribute("id", paramDescMeta.getId());
    localElement.setAttribute("nameSpace", paramDescMeta.getNameSpace().toString());
    if (paramDescMeta.getType() != null)
      localElement.setAttribute("type", paramDescMeta.getType());


    populateDescMetadata(localElement, paramDescMeta);
  }

  protected void generateItem(Item paramItem, Document paramDocument, Element paramElement) {
    Log.v("generateItem--", "generateItem is call");
    if (paramItem.getClazz() == null){
      
    }
//      throw new RuntimeException("Missing 'upnp:class' element for item: " + paramItem.getId());
    Element localElement = XMLUtil.appendNewElement(paramDocument, paramElement, "item");
    if (paramItem.getId() == null){
//      throw new NullPointerException("Missing id on item: " + paramItem);
    }
    localElement.setAttribute("id", paramItem.getId());
    if (paramItem.getParentID() == null){
//      throw new NullPointerException("Missing parent id on item: " + paramItem);
    }
    localElement.setAttribute("parentID", paramItem.getParentID());
    if (paramItem.getRefID() != null) {
      localElement.setAttribute("refID", paramItem.getRefID());
    }
    localElement.setAttribute("restricted", booleanToInt(paramItem.isRestricted()));
    String str = paramItem.getTitle();
    if (str == null) {
      LOG.warning("Missing 'dc:title' element for item: " + paramItem.getId());
      str = "Unknown Title";
    }
    XMLUtil.appendNewElementIfNotNull(paramDocument, localElement, "dc:title", str,
        "http://purl.org/dc/elements/1.1/");
    XMLUtil.appendNewElementIfNotNull(paramDocument, localElement, "dc:creator",
        paramItem.getCreator(), "http://purl.org/dc/elements/1.1/");
    XMLUtil.appendNewElementIfNotNull(paramDocument, localElement, "upnp:writeStatus",
        paramItem.getWriteStatus(), "urn:schemas-upnp-org:metadata-1-0/upnp/");
    Log.v("getitem", "getitem to appendClass");
    appendClass(paramDocument, localElement, paramItem.getClazz(), "upnp:class", false);
    appendProperties(paramDocument, localElement, paramItem, "upnp",
        DIDLObject.Property.UPNP.NAMESPACE.class, "urn:schemas-upnp-org:metadata-1-0/upnp/");
    appendProperties(paramDocument, localElement, paramItem, "dc",
        DIDLObject.Property.DC.NAMESPACE.class, "http://purl.org/dc/elements/1.1/");
    // appendProperties(paramDocument, localElement, paramItem, "sec",
    // DIDLObject.Property.SEC.NAMESPACE.class,
    // "http://www.sec.co.kr/");
    Log.v("迭代遍历", "迭代遍历");
    Iterator localIterator1 = paramItem.getResources().iterator();
    Iterator localIterator2 = paramItem.getDescMetadata().iterator();
    /**
     * 这里通过迭代器的遍历 来判断在adapter下的item 第一次迭代是在video下item 第二次迭代是在item
     * **/
    Log.v("迭代遍历", "!localIterator1.hasNext()" + localIterator1.hasNext());
    Log.v("迭代遍历", "!localIterator2---" + localIterator2.hasNext());
    // 具体含义： next(), 是返回当前元素， 并指向下一个元素。
    // hasNext()， 则是判断当前元素是否存在，并指向下一个元素（即所谓的索引）
    while (localIterator1.hasNext()) {// error
     

      if (!localIterator2.hasNext()) {
       
        Res localRes = (Res) localIterator1.next();
        Log.v("迭代遍历", "localRes--" + localRes);
        if (localRes != null){
          
          generateResource(localRes, paramDocument, localElement);
        }

        return;

      }
      Log.v("迭代遍历", "没有进入迭代");

      DescMeta localDescMeta = (DescMeta) localIterator2.next();
      Log.v("迭代遍历", "localDescMeta--" + localDescMeta);
      if (localDescMeta != null) {
        generateDescMetadata(localDescMeta, paramDocument, localElement);
      }
    }

    // if (localIterator1.hasNext()){
    // localIterator2 = paramItem.getDescMetadata().iterator();
    // while (true)
    // {
    // if (!localIterator2.hasNext())
    // {
    // Log.v("迭代遍历", "!localIterator2---"+localIterator2.hasNext());
    // Res localRes = (Res)localIterator1.next();
    // Log.v("迭代遍历", "1111---"+localRes);
    // if (localRes == null)
    //
    // generateResource(localRes, paramDocument, localElement);
    //
    // }
    // Log.v("迭代遍历", "没有进入迭代");
    // DescMeta localDescMeta = (DescMeta)localIterator2.next();
    // if (localDescMeta == null)
    // Log.v("迭代遍历", "localDescMeta--"+localDescMeta);
    // generateDescMetadata(localDescMeta, paramDocument, localElement);
    // }
    // }
  }

  // 资源节点信息
  protected void generateResource(Res paramRes, Document paramDocument, Element paramElement) {

    Log.v("generateItem TO generateResource", "generateResource IS CALL");
    if (paramRes.getValue() == null){
      
    }
//      throw new RuntimeException("Missing resource URI value" + paramRes);
    if (paramRes.getProtocolInfo() == null){
      
    }
//      throw new RuntimeException("Missing resource protocol info: " + paramRes);
    Element localElement =
        XMLUtil.appendNewElement(paramDocument, paramElement, "res", paramRes.getValue());
    localElement.setAttribute("protocolInfo", paramRes.getProtocolInfo().toString());
    if (paramRes.getImportUri() != null){
      localElement.setAttribute("importUri", paramRes.getImportUri().toString());
    }
    if (paramRes.getSize() != null){
      localElement.setAttribute("size", paramRes.getSize().toString());
    }
    if (paramRes.getDuration() != null){
      localElement.setAttribute("duration", paramRes.getDuration());
    }
    if (paramRes.getBitrate() != null){
      localElement.setAttribute("bitrate", paramRes.getBitrate().toString());
    }
    if (paramRes.getSampleFrequency() != null){
      localElement.setAttribute("sampleFrequency", paramRes.getSampleFrequency().toString());
    }
    if (paramRes.getBitsPerSample() != null){
      localElement.setAttribute("bitsPerSample", paramRes.getBitsPerSample().toString());
    }
    if (paramRes.getNrAudioChannels() != null){
      localElement.setAttribute("nrAudioChannels", paramRes.getNrAudioChannels().toString());
    }
    if (paramRes.getColorDepth() != null){
      localElement.setAttribute("colorDepth", paramRes.getColorDepth().toString());
    }
    if (paramRes.getProtection() != null){
      localElement.setAttribute("protection", paramRes.getProtection());
    }
    if (paramRes.getResolution() != null){
      localElement.setAttribute("resolution", paramRes.getResolution());
      
    }

    Log.v("generateResource", "" + paramRes.getValue());
    Log.v("generateResource", "" + paramRes.getProtocolInfo());
    Log.v("generateResource", "" + paramRes.getImportUri());
    Log.v("generateResource", "" + paramRes.getSize().toString());
    Log.v("generateResource", "" + paramRes.getDuration());
    Log.v("generateResource", "" + paramRes.getBitrate());

  }



  // 判断资源是否已在节点中
  protected void populateDescMetadata(Element element, DescMeta descMeta) {
    Log.v(" generateDescMetadata to populate", " populateDescMetadata is call");
    if ((descMeta.getMetadata() instanceof Document)) {
      NodeList localNodeList =
          ((Document) descMeta.getMetadata()).getDocumentElement().getChildNodes();
      int len = 0;
      if (len >= localNodeList.getLength()) {

        return;
      }
      Node localNode = localNodeList.item(len);

      if (localNode.getNodeType() != 1) {

        len++;

        element.appendChild(element.getOwnerDocument().importNode(localNode, true));
      }
    }
    LOG.warning("Unknown desc metadata content, please override populateDescMetadata(): "
        + descMeta.getMetadata());
  }
}
