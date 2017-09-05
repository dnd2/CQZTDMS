
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
 *         &lt;element name="Send_FaxResult" type="{http://www.w3.org/2001/XMLSchema}int"/>
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
    "sendFaxResult"
})
@XmlRootElement(name = "Send_FaxResponse")
public class SendFaxResponse {

    @XmlElement(name = "Send_FaxResult")
    protected int sendFaxResult;

    /**
     * Gets the value of the sendFaxResult property.
     * 
     */
    public int getSendFaxResult() {
        return sendFaxResult;
    }

    /**
     * Sets the value of the sendFaxResult property.
     * 
     */
    public void setSendFaxResult(int value) {
        this.sendFaxResult = value;
    }

}
