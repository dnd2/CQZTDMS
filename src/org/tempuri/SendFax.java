
package org.tempuri;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="strSdate" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="strStime" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="strUname" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="strSubj" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="strFax" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="strFile1" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="strFile2" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="strFile3" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="strGhid" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "strSdate",
    "strStime",
    "strUname",
    "strSubj",
    "strFax",
    "strFile1",
    "strFile2",
    "strFile3",
    "strGhid"
})
@XmlRootElement(name = "Send_Fax")
public class SendFax {

    protected String strSdate;
    protected String strStime;
    protected String strUname;
    protected String strSubj;
    protected String strFax;
    protected String strFile1;
    protected String strFile2;
    protected String strFile3;
    protected String strGhid;

    /**
     * Gets the value of the strSdate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStrSdate() {
        return strSdate;
    }

    /**
     * Sets the value of the strSdate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStrSdate(String value) {
        this.strSdate = value;
    }

    /**
     * Gets the value of the strStime property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStrStime() {
        return strStime;
    }

    /**
     * Sets the value of the strStime property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStrStime(String value) {
        this.strStime = value;
    }

    /**
     * Gets the value of the strUname property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStrUname() {
        return strUname;
    }

    /**
     * Sets the value of the strUname property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStrUname(String value) {
        this.strUname = value;
    }

    /**
     * Gets the value of the strSubj property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStrSubj() {
        return strSubj;
    }

    /**
     * Sets the value of the strSubj property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStrSubj(String value) {
        this.strSubj = value;
    }

    /**
     * Gets the value of the strFax property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStrFax() {
        return strFax;
    }

    /**
     * Sets the value of the strFax property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStrFax(String value) {
        this.strFax = value;
    }

    /**
     * Gets the value of the strFile1 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStrFile1() {
        return strFile1;
    }

    /**
     * Sets the value of the strFile1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStrFile1(String value) {
        this.strFile1 = value;
    }

    /**
     * Gets the value of the strFile2 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStrFile2() {
        return strFile2;
    }

    /**
     * Sets the value of the strFile2 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStrFile2(String value) {
        this.strFile2 = value;
    }

    /**
     * Gets the value of the strFile3 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStrFile3() {
        return strFile3;
    }

    /**
     * Sets the value of the strFile3 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStrFile3(String value) {
        this.strFile3 = value;
    }

    /**
     * Gets the value of the strGhid property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStrGhid() {
        return strGhid;
    }

    /**
     * Sets the value of the strGhid property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStrGhid(String value) {
        this.strGhid = value;
    }

}
