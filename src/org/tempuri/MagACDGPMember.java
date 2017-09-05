
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
 *         &lt;element name="strACDGPMem_New" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="strACDGPMem_Src" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="nMagType" type="{http://www.w3.org/2001/XMLSchema}int"/>
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
    "strACDGPMemNew",
    "strACDGPMemSrc",
    "nMagType"
})
@XmlRootElement(name = "Mag_ACDGP_Member")
public class MagACDGPMember {

    @XmlElement(name = "strACDGPMem_New")
    protected String strACDGPMemNew;
    @XmlElement(name = "strACDGPMem_Src")
    protected String strACDGPMemSrc;
    protected int nMagType;

    /**
     * Gets the value of the strACDGPMemNew property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStrACDGPMemNew() {
        return strACDGPMemNew;
    }

    /**
     * Sets the value of the strACDGPMemNew property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStrACDGPMemNew(String value) {
        this.strACDGPMemNew = value;
    }

    /**
     * Gets the value of the strACDGPMemSrc property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStrACDGPMemSrc() {
        return strACDGPMemSrc;
    }

    /**
     * Sets the value of the strACDGPMemSrc property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStrACDGPMemSrc(String value) {
        this.strACDGPMemSrc = value;
    }

    /**
     * Gets the value of the nMagType property.
     * 
     */
    public int getNMagType() {
        return nMagType;
    }

    /**
     * Sets the value of the nMagType property.
     * 
     */
    public void setNMagType(int value) {
        this.nMagType = value;
    }

}
