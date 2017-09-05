/**
 * RunQueryAsAService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.infodms.dms.common.repairorder.webservice.RepairOrder;

import java.io.Serializable;

import javax.xml.namespace.QName;

import org.apache.axis.description.ElementDesc;
import org.apache.axis.description.TypeDesc;
import org.apache.axis.encoding.Deserializer;
import org.apache.axis.encoding.Serializer;
import org.apache.axis.encoding.ser.BeanDeserializer;
import org.apache.axis.encoding.ser.BeanSerializer;

public class RunQueryAsAService implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private String login;

	private String password;

	private String vin;

	/**
	 * Gets the login value for this RunQueryAsAService.
	 * 
	 * @return login
	 */
	public String getLogin() {
		return login;
	}

	/**
	 * Sets the login value for this RunQueryAsAService.
	 * 
	 * @param login
	 */
	public void setLogin(String login) {
		this.login = login;
	}

	/**
	 * Gets the password value for this RunQueryAsAService.
	 * 
	 * @return password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * Sets the password value for this RunQueryAsAService.
	 * 
	 * @param password
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * Gets the vin value for this RunQueryAsAService.
	 * 
	 * @return vin
	 */
	public String getVin() {
		return vin;
	}

	/**
	 * Sets the vin value for this RunQueryAsAService.
	 * 
	 * @param vin
	 */
	public void setVin(String vin) {
		this.vin = vin;
	}

	// Type metadata
	private static TypeDesc typeDesc = new TypeDesc(RunQueryAsAService.class, true);

	static {
		typeDesc.setXmlType(new QName("RepairOrder", ">runQueryAsAService"));
		ElementDesc elemField = new ElementDesc();
		elemField.setFieldName("login");
		elemField.setXmlName(new QName("RepairOrder", "login"));
		elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("password");
		elemField.setXmlName(new QName("RepairOrder", "password"));
		elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("vin");
		elemField.setXmlName(new QName("RepairOrder", "vin"));
		elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
	}

	/**
	 * Return type metadata object
	 */
	public static TypeDesc getTypeDesc() {
		return typeDesc;
	}

	/**
	 * Get Custom Serializer
	 */
	public static Serializer getSerializer(String mechType, Class _javaType, QName _xmlType) {
		return new BeanSerializer(_javaType, _xmlType, typeDesc);
	}

	/**
	 * Get Custom Deserializer
	 */
	public static Deserializer getDeserializer(String mechType, Class _javaType, QName _xmlType) {
		return new BeanDeserializer(_javaType, _xmlType, typeDesc);
	}

}
