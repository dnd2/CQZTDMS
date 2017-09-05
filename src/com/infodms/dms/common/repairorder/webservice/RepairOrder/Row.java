/**
 * Row.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.infodms.dms.common.repairorder.webservice.RepairOrder;

import java.io.Serializable;

import javax.xml.namespace.QName;

import org.apache.axis.description.TypeDesc;
import org.apache.axis.encoding.Deserializer;
import org.apache.axis.encoding.ser.BeanDeserializer;

public class Row implements Serializable {
	private String type;

	private String balance_No;

	private String ro_No;

	private String start_Time;

	private String license;

	private String ro_Type;

	private String repair_Type;

	private String in_Mileage;

	private String is_Change_Mileage;
	
	private String warranty_begin_date;

	/**
	 * Gets the type value for this Row.
	 * 
	 * @return type
	 */
	public String getType() {
		return type;
	}

	/**
	 * Sets the type value for this Row.
	 * 
	 * @param type
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * Gets the balance_No value for this Row.
	 * 
	 * @return balance_No
	 */
	public String getBalance_No() {
		return balance_No;
	}

	/**
	 * Sets the balance_No value for this Row.
	 * 
	 * @param balance_No
	 */
	public void setBalance_No(String balance_No) {
		this.balance_No = balance_No;
	}

	/**
	 * Gets the ro_No value for this Row.
	 * 
	 * @return ro_No
	 */
	public String getRo_No() {
		return ro_No;
	}

	/**
	 * Sets the ro_No value for this Row.
	 * 
	 * @param ro_No
	 */
	public void setRo_No(String ro_No) {
		this.ro_No = ro_No;
	}

	/**
	 * Gets the start_Time value for this Row.
	 * 
	 * @return start_Time
	 */
	public String getStart_Time() {
		return start_Time;
	}

	/**
	 * Sets the start_Time value for this Row.
	 * 
	 * @param start_Time
	 */
	public void setStart_Time(String start_Time) {
		this.start_Time = start_Time;
	}

	/**
	 * Gets the license value for this Row.
	 * 
	 * @return license
	 */
	public String getLicense() {
		return license;
	}

	/**
	 * Sets the license value for this Row.
	 * 
	 * @param license
	 */
	public void setLicense(String license) {
		this.license = license;
	}

	/**
	 * Gets the ro_Type value for this Row.
	 * 
	 * @return ro_Type
	 */
	public String getRo_Type() {
		return ro_Type;
	}

	/**
	 * Sets the ro_Type value for this Row.
	 * 
	 * @param ro_Type
	 */
	public void setRo_Type(String ro_Type) {
		this.ro_Type = ro_Type;
	}

	/**
	 * Gets the repair_Type value for this Row.
	 * 
	 * @return repair_Type
	 */
	public String getRepair_Type() {
		return repair_Type;
	}

	/**
	 * Sets the repair_Type value for this Row.
	 * 
	 * @param repair_Type
	 */
	public void setRepair_Type(String repair_Type) {
		this.repair_Type = repair_Type;
	}

	/**
	 * Gets the in_Mileage value for this Row.
	 * 
	 * @return in_Mileage
	 */
	public String getIn_Mileage() {
		return in_Mileage;
	}

	/**
	 * Sets the in_Mileage value for this Row.
	 * 
	 * @param in_Mileage
	 */
	public void setIn_Mileage(String in_Mileage) {
		this.in_Mileage = in_Mileage;
	}

	/**
	 * Gets the is_Change_Mileage value for this Row.
	 * 
	 * @return is_Change_Mileage
	 */
	public String getIs_Change_Mileage() {
		return is_Change_Mileage;
	}

	/**
	 * Sets the is_Change_Mileage value for this Row.
	 * 
	 * @param is_Change_Mileage
	 */
	public void setIs_Change_Mileage(String is_Change_Mileage) {
		this.is_Change_Mileage = is_Change_Mileage;
	}

	public String getWarranty_begin_date() {
		return warranty_begin_date;
	}

	public void setWarranty_begin_date(String warranty_begin_date) {
		this.warranty_begin_date = warranty_begin_date;
	}
	
	// Type metadata
	private static TypeDesc typeDesc = new TypeDesc(Row.class, true);

	static {
		typeDesc.setXmlType(new QName("RepairOrder", "Row"));
		org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("type");
		elemField.setXmlName(new QName("RepairOrder", "Type"));
		elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("balance_No");
		elemField.setXmlName(new QName("RepairOrder", "Balance_No"));
		elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("ro_No");
		elemField.setXmlName(new QName("RepairOrder", "Ro_No"));
		elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("start_Time");
		elemField.setXmlName(new QName("RepairOrder", "Start_Time"));
		elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("license");
		elemField.setXmlName(new QName("RepairOrder", "License"));
		elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("ro_Type");
		elemField.setXmlName(new QName("RepairOrder", "Ro_Type"));
		elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("repair_Type");
		elemField.setXmlName(new QName("RepairOrder", "Repair_Type"));
		elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("in_Mileage");
		elemField.setXmlName(new QName("RepairOrder", "In_Mileage"));
		elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("is_Change_Mileage");
		elemField.setXmlName(new QName("RepairOrder", "Is_Change_Mileage"));
		elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("warranty_begin_date");
		elemField.setXmlName(new QName("RepairOrder", "Warranty_begin_date"));
		elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
	}

	/**
	 * Return type metadata object
	 */
	public static org.apache.axis.description.TypeDesc getTypeDesc() {
		return typeDesc;
	}

	/**
	 * Get Custom Serializer
	 */
	public static org.apache.axis.encoding.Serializer getSerializer(String mechType, Class _javaType, QName _xmlType) {
		return new org.apache.axis.encoding.ser.BeanSerializer(_javaType, _xmlType, typeDesc);
	}

	/**
	 * Get Custom Deserializer
	 */
	public static Deserializer getDeserializer(String mechType, Class _javaType, QName _xmlType) {
		return new BeanDeserializer(_javaType, _xmlType, typeDesc);
	}

}
