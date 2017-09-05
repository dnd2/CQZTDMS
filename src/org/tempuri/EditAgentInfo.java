
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
 *         &lt;element name="strAgentInfo_New" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="strAgentInfo_Src" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="nEditType" type="{http://www.w3.org/2001/XMLSchema}int"/>
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
    "strAgentInfoNew",
    "strAgentInfoSrc",
    "nEditType"
})
@XmlRootElement(name = "Edit_Agent_Info")
public class EditAgentInfo {

    @XmlElement(name = "strAgentInfo_New")
    protected String strAgentInfoNew;
    @XmlElement(name = "strAgentInfo_Src")
    protected String strAgentInfoSrc;
    protected int nEditType;

    /**
     * Gets the value of the strAgentInfoNew property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStrAgentInfoNew() {
        return strAgentInfoNew;
    }

    /**
     * Sets the value of the strAgentInfoNew property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStrAgentInfoNew(String value) {
        this.strAgentInfoNew = value;
    }

    /**
     * Gets the value of the strAgentInfoSrc property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStrAgentInfoSrc() {
        return strAgentInfoSrc;
    }

    /**
     * Sets the value of the strAgentInfoSrc property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStrAgentInfoSrc(String value) {
        this.strAgentInfoSrc = value;
    }

    /**
     * Gets the value of the nEditType property.
     * 
     */
    public int getNEditType() {
        return nEditType;
    }

    /**
     * Sets the value of the nEditType property.
     * 
     */
    public void setNEditType(int value) {
        this.nEditType = value;
    }

}
