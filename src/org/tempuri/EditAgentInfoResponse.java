
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
 *         &lt;element name="Edit_Agent_InfoResult" type="{http://www.w3.org/2001/XMLSchema}int"/>
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
    "editAgentInfoResult"
})
@XmlRootElement(name = "Edit_Agent_InfoResponse")
public class EditAgentInfoResponse {

    @XmlElement(name = "Edit_Agent_InfoResult")
    protected int editAgentInfoResult;

    /**
     * Gets the value of the editAgentInfoResult property.
     * 
     */
    public int getEditAgentInfoResult() {
        return editAgentInfoResult;
    }

    /**
     * Sets the value of the editAgentInfoResult property.
     * 
     */
    public void setEditAgentInfoResult(int value) {
        this.editAgentInfoResult = value;
    }

}
