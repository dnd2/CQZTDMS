
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
    "strEdate",
    "strGhid"
})
@XmlRootElement(name = "VMS_Recv")
public class VMSRecv {

    protected String strSdate;
    protected String strEdate;
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
