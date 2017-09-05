
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
 *         &lt;element name="strEdate" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="strCaller" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="strExt" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="strCallid" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="strGhid" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="strDirection" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
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
    "strEdate",
    "strCaller",
    "strExt",
    "strCallid",
    "strGhid",
    "strDirection"
})
@XmlRootElement(name = "Get_Talentel_log_Info")
public class GetTalentelLogInfo {

    protected String strSdate;
    protected String strEdate;
    protected String strCaller;
    protected String strExt;
    protected String strCallid;
    protected String strGhid;
    protected String strDirection;

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
     * Gets the value of the strEdate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStrEdate() {
        return strEdate;
    }

    /**
     * Sets the value of the strEdate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStrEdate(String value) {
        this.strEdate = value;
    }

    /**
     * Gets the value of the strCaller property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStrCaller() {
        return strCaller;
    }

    /**
     * Sets the value of the strCaller property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStrCaller(String value) {
        this.strCaller = value;
    }

    /**
     * Gets the value of the strExt property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStrExt() {
        return strExt;
    }

    /**
     * Sets the value of the strExt property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStrExt(String value) {
        this.strExt = value;
    }

    /**
     * Gets the value of the strCallid property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStrCallid() {
        return strCallid;
    }

    /**
     * Sets the value of the strCallid property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStrCallid(String value) {
        this.strCallid = value;
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

    /**
     * Gets the value of the strDirection property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStrDirection() {
        return strDirection;
    }

    /**
     * Sets the value of the strDirection property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStrDirection(String value) {
        this.strDirection = value;
    }

}
