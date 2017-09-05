
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
 *         &lt;element name="strCallid" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="strDate" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
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
    "strCallid",
    "strDate"
})
@XmlRootElement(name = "Get_Rec_FTP_Info")
public class GetRecFTPInfo {

    protected String strCallid;
    protected String strDate;

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
     * Gets the value of the strDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStrDate() {
        return strDate;
    }

    /**
     * Sets the value of the strDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStrDate(String value) {
        this.strDate = value;
    }

}
