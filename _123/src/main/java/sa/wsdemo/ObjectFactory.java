//
// 此文件是由 JavaTM Architecture for XML Binding (JAXB) 引用实现 v2.2.7 生成的
// 请访问 <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// 在重新编译源模式时, 对此文件的所有修改都将丢失。
// 生成时间: 2019.11.03 时间 04:57:12 PM CST 
//


package sa.wsdemo;

import javax.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the sa.wsdemo package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {


    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: sa.wsdemo
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link Deciderequest }
     * 
     */
    public Deciderequest createDeciderequest() {
        return new Deciderequest();
    }

    /**
     * Create an instance of {@link Usersbatchrequest }
     * 
     */
    public Usersbatchrequest createUsersbatchrequest() {
        return new Usersbatchrequest();
    }

    /**
     * Create an instance of {@link Userresponse }
     * 
     */
    public Userresponse createUserresponse() {
        return new Userresponse();
    }

    /**
     * Create an instance of {@link Userrequest }
     * 
     */
    public Userrequest createUserrequest() {
        return new Userrequest();
    }

}
