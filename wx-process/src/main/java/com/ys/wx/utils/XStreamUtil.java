package com.ys.wx.utils;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class XStreamUtil {

//    public static void toEntity(String inputXML ,Class[] iclass){  
//        XStream xs = new XStream();  
////        这句和@XStreamAlias("person")等效  
////        xs.alias("person",Person.class);  
////        xs.alias("address",Address.class);  
//        xs.setMode(XStream.NO_REFERENCES);  
////      这句和@XStreamImplicit()等效  
////        xs.addImplicitCollection(Person.class,"addresses");  
////        这句和@XStreamAsAttribute()  
////        xs.useAttributeFor(Person.class, "name");  
//        //注册使用了注解的VO  
//        xs.processAnnotations(iclass);  
//        xs.
//        Person person = (Person)xs.fromXML(inputXML);  
//        System.out.println(person.getAddresses().get(0).getHouseNo()+person.getName());  
//    } 
    
    public static <T> T toBean(String xmlStr, Class<T> cls) {
        XStream xstream = new XStream(new DomDriver());
        xstream.processAnnotations(cls);
        //xstream.alias("body", cls);
        @SuppressWarnings("unchecked")
        T t = (T) xstream.fromXML(xmlStr);
        return t;
    }
}
