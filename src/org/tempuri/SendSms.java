
package org.tempuri;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
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
 *         &lt;element name="strTel_To" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="strBody" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
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
    "strTelTo",
    "strBody",
    "strGhid"
})
@XmlRootElement(name = "Send_Sms")
public class SendSms {

    protected String strSdate;
    protected String strStime;
    protected String strUname;
    protected String strSubj;
    @XmlElement(name = "strTel_To")
    protected String strTelTo;
    protected String strBody;
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
     * Gets the value of the strTelTo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStrTelTo() {
        return strTelTo;
    }

    /**
     * Sets the value of the strTelTo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStrTelTo(String value) {
        this.strTelTo = value;
    }

    /**
     * Gets the value of the strBody property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStrBody() {
        return strBody;
    }

    /**
     * Sets the value of the strBody property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStrBody(String value) {
        this.strBody = value;
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
