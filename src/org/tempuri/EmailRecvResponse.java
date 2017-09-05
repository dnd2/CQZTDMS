
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
 *         &lt;element name="Email_RecvResult" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
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
    "emailRecvResult"
})
@XmlRootElement(name = "Email_RecvResponse")
public class EmailRecvResponse {

    @XmlElement(name = "Email_RecvResult")
    protected String emailRecvResult;

    /**
     * Gets the value of the emailRecvResult property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEmailRecvResult() {
        return emailRecvResult;
    }

    /**
     * Sets the value of the emailRecvResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEmailRecvResult(String value) {
        this.emailRecvResult = value;
    }

}
