
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
 *         &lt;element name="Fax_RecvResult" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
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
    "faxRecvResult"
})
@XmlRootElement(name = "Fax_RecvResponse")
public class FaxRecvResponse {

    @XmlElement(name = "Fax_RecvResult")
    protected String faxRecvResult;

    /**
     * Gets the value of the faxRecvResult property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFaxRecvResult() {
        return faxRecvResult;
    }

    /**
     * Sets the value of the faxRecvResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFaxRecvResult(String value) {
        this.faxRecvResult = value;
    }

}
