
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
 *         &lt;element name="strACD_Group_New" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="strACD_Group_Src" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
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
    "strACDGroupNew",
    "strACDGroupSrc",
    "nEditType"
})
@XmlRootElement(name = "Edit_ACD_Group_Info")
public class EditACDGroupInfo {

    @XmlElement(name = "strACD_Group_New")
    protected String strACDGroupNew;
    @XmlElement(name = "strACD_Group_Src")
    protected String strACDGroupSrc;
    protected int nEditType;

    /**
     * Gets the value of the strACDGroupNew property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStrACDGroupNew() {
        return strACDGroupNew;
    }

    /**
     * Sets the value of the strACDGroupNew property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStrACDGroupNew(String value) {
        this.strACDGroupNew = value;
    }

    /**
     * Gets the value of the strACDGroupSrc property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStrACDGroupSrc() {
        return strACDGroupSrc;
    }

    /**
     * Sets the value of the strACDGroupSrc property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStrACDGroupSrc(String value) {
        this.strACDGroupSrc = value;
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
