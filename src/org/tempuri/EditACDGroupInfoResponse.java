
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
 *         &lt;element name="Edit_ACD_Group_InfoResult" type="{http://www.w3.org/2001/XMLSchema}int"/>
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
    "editACDGroupInfoResult"
})
@XmlRootElement(name = "Edit_ACD_Group_InfoResponse")
public class EditACDGroupInfoResponse {

    @XmlElement(name = "Edit_ACD_Group_InfoResult")
    protected int editACDGroupInfoResult;

    /**
     * Gets the value of the editACDGroupInfoResult property.
     * 
     */
    public int getEditACDGroupInfoResult() {
        return editACDGroupInfoResult;
    }

    /**
     * Sets the value of the editACDGroupInfoResult property.
     * 
     */
    public void setEditACDGroupInfoResult(int value) {
        this.editACDGroupInfoResult = value;
    }

}
