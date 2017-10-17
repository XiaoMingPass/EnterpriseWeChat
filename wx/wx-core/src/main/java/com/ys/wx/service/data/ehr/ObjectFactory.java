
package com.ys.wx.service.data.ehr;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each
 * Java content interface and Java element interface
 * generated in the com.demo.client package.
 * <p>An ObjectFactory allows you to programatically
 * construct new instances of the Java representation
 * for XML content. The Java representation of XML
 * content can consist of schema derived interfaces
 * and classes representing the binding of schema
 * type definitions, element declarations and model
 * groups.  Factory methods for each of these are
 * provided in this class.
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _GetPsndocsByTimeAndStatusWXResponseReturn_QNAME = new QName("", "return");
    private final static QName _GetPsndocsByTimeAndStatusWXStartTime_QNAME = new QName("", "startTime");
    private final static QName _GetPsndocsByTimeAndStatusWXIndex_QNAME = new QName("", "index");
    private final static QName _GetPsndocsByTimeAndStatusWXOperate_QNAME = new QName("", "operate");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.demo.client
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link GetPsndocsByTimeAndStatusWXResponse }
     */
    public GetPsndocsByTimeAndStatusWXResponse createGetPsndocsByTimeAndStatusWXResponse() {
        return new GetPsndocsByTimeAndStatusWXResponse();
    }

    /**
     * Create an instance of {@link GetPsndocsByTimeAndStatusWX }
     */
    public GetPsndocsByTimeAndStatusWX createGetPsndocsByTimeAndStatusWX() {
        return new GetPsndocsByTimeAndStatusWX();
    }

    /**
     * Create an instance of {@link GetDeptWXAllResponse }
     */
    public GetDeptWXAllResponse createGetDeptWXAllResponse() {
        return new GetDeptWXAllResponse();
    }

    /**
     * Create an instance of {@link GetDeptWXAll }
     */
    public GetDeptWXAll createGetDeptWXAll() {
        return new GetDeptWXAll();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     */
    @XmlElementDecl(namespace = "", name = "return", scope = GetPsndocsByTimeAndStatusWXResponse.class)
    public JAXBElement<String> createGetPsndocsByTimeAndStatusWXResponseReturn(String value) {
        return new JAXBElement<String>(_GetPsndocsByTimeAndStatusWXResponseReturn_QNAME, String.class, GetPsndocsByTimeAndStatusWXResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     */
    @XmlElementDecl(namespace = "", name = "startTime", scope = GetPsndocsByTimeAndStatusWX.class)
    public JAXBElement<String> createGetPsndocsByTimeAndStatusWXStartTime(String value) {
        return new JAXBElement<String>(_GetPsndocsByTimeAndStatusWXStartTime_QNAME, String.class, GetPsndocsByTimeAndStatusWX.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     */
    @XmlElementDecl(namespace = "", name = "index", scope = GetPsndocsByTimeAndStatusWX.class)
    public JAXBElement<Integer> createGetPsndocsByTimeAndStatusWXIndex(Integer value) {
        return new JAXBElement<Integer>(_GetPsndocsByTimeAndStatusWXIndex_QNAME, Integer.class, GetPsndocsByTimeAndStatusWX.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     */
    @XmlElementDecl(namespace = "", name = "operate", scope = GetPsndocsByTimeAndStatusWX.class)
    public JAXBElement<Integer> createGetPsndocsByTimeAndStatusWXOperate(Integer value) {
        return new JAXBElement<Integer>(_GetPsndocsByTimeAndStatusWXOperate_QNAME, Integer.class, GetPsndocsByTimeAndStatusWX.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     */
    @XmlElementDecl(namespace = "", name = "return", scope = GetDeptWXAllResponse.class)
    public JAXBElement<String> createGetDeptWXAllResponseReturn(String value) {
        return new JAXBElement<String>(_GetPsndocsByTimeAndStatusWXResponseReturn_QNAME, String.class, GetDeptWXAllResponse.class, value);
    }

}
