/**
 * Row.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.infodms.dms.common.repairorder.webservice.OrderDetail;

import javax.xml.namespace.QName;

import org.apache.axis.description.ElementDesc;
import org.apache.axis.description.TypeDesc;
import org.apache.axis.encoding.Deserializer;
import org.apache.axis.encoding.Serializer;
import org.apache.axis.encoding.ser.BeanDeserializer;
import org.apache.axis.encoding.ser.BeanSerializer;

public class Row implements java.io.Serializable {

	private String type;

	private String balance_No;

	private String trouble_Desc;

	private String trouble_Cause;

	private String code;

	private String name;

	private double std_Labour_Hour;

	private double add_Labour_Hour;

	private String charge_Mode;

	private double part_Quantity;

	private String unit;

	private String remark;

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
	 * Gets the trouble_Desc value for this Row.
	 * 
	 * @return trouble_Desc
	 */
	public String getTrouble_Desc() {
		return trouble_Desc;
	}

	/**
	 * Sets the trouble_Desc value for this Row.
	 * 
	 * @param trouble_Desc
	 */
	public void setTrouble_Desc(String trouble_Desc) {
		this.trouble_Desc = trouble_Desc;
	}

	/**
	 * Gets the trouble_Cause value for this Row.
	 * 
	 * @return trouble_Cause
	 */
	public String getTrouble_Cause() {
		return trouble_Cause;
	}

	/**
	 * Sets the trouble_Cause value for this Row.
	 * 
	 * @param trouble_Cause
	 */
	public void setTrouble_Cause(String trouble_Cause) {
		this.trouble_Cause = trouble_Cause;
	}

	/**
	 * Gets the code value for this Row.
	 * 
	 * @return code
	 */
	public String getCode() {
		return code;
	}

	/**
	 * Sets the code value for this Row.
	 * 
	 * @param code
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * Gets the name value for this Row.
	 * 
	 * @return name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name value for this Row.
	 * 
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Gets the std_Labour_Hour value for this Row.
	 * 
	 * @return std_Labour_Hour
	 */
	public double getStd_Labour_Hour() {
		return std_Labour_Hour;
	}

	/**
	 * Sets the std_Labour_Hour value for this Row.
	 * 
	 * @param std_Labour_Hour
	 */
	public void setStd_Labour_Hour(double std_Labour_Hour) {
		this.std_Labour_Hour = std_Labour_Hour;
	}

	/**
	 * Gets the add_Labour_Hour value for this Row.
	 * 
	 * @return add_Labour_Hour
	 */
	public double getAdd_Labour_Hour() {
		return add_Labour_Hour;
	}

	/**
	 * Sets the add_Labour_Hour value for this Row.
	 * 
	 * @param add_Labour_Hour
	 */
	public void setAdd_Labour_Hour(double add_Labour_Hour) {
		this.add_Labour_Hour = add_Labour_Hour;
	}

	/**
	 * Gets the charge_Mode value for this Row.
	 * 
	 * @return charge_Mode
	 */
	public String getCharge_Mode() {
		return charge_Mode;
	}

	/**
	 * Sets the charge_Mode value for this Row.
	 * 
	 * @param charge_Mode
	 */
	public void setCharge_Mode(String charge_Mode) {
		this.charge_Mode = charge_Mode;
	}

	/**
	 * Gets the part_Quantity value for this Row.
	 * 
	 * @return part_Quantity
	 */
	public double getPart_Quantity() {
		return part_Quantity;
	}

	/**
	 * Sets the part_Quantity value for this Row.
	 * 
	 * @param part_Quantity
	 */
	public void setPart_Quantity(double part_Quantity) {
		this.part_Quantity = part_Quantity;
	}

	/**
	 * Gets the unit value for this Row.
	 * 
	 * @return unit
	 */
	public String getUnit() {
		return unit;
	}

	/**
	 * Sets the unit value for this Row.
	 * 
	 * @param unit
	 */
	public void setUnit(String unit) {
		this.unit = unit;
	}

	/**
	 * Gets the remark value for this Row.
	 * 
	 * @return remark
	 */
	public String getRemark() {
		return remark;
	}

	/**
	 * Sets the remark value for this Row.
	 * 
	 * @param remark
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}

	// Type metadata
	private static TypeDesc typeDesc = new TypeDesc(Row.class, true);

	static {
		typeDesc.setXmlType(new QName("OrderDetail", "Row"));
		ElementDesc elemField = new ElementDesc();
		elemField.setFieldName("type");
		elemField.setXmlName(new QName("OrderDetail", "Type"));
		elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("balance_No");
		elemField.setXmlName(new QName("OrderDetail", "Balance_No"));
		elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("trouble_Desc");
		elemField.setXmlName(new QName("OrderDetail", "Trouble_Desc"));
		elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("trouble_Cause");
		elemField.setXmlName(new QName("OrderDetail", "Trouble_Cause"));
		elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("code");
		elemField.setXmlName(new QName("OrderDetail", "Code"));
		elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("name");
		elemField.setXmlName(new QName("OrderDetail", "Name"));
		elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("std_Labour_Hour");
		elemField.setXmlName(new QName("OrderDetail", "Std_Labour_Hour"));
		elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "double"));
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("add_Labour_Hour");
		elemField.setXmlName(new QName("OrderDetail", "Add_Labour_Hour"));
		elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "double"));
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("charge_Mode");
		elemField.setXmlName(new QName("OrderDetail", "Charge_Mode"));
		elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("part_Quantity");
		elemField.setXmlName(new QName("OrderDetail", "Part_Quantity"));
		elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "double"));
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("unit");
		elemField.setXmlName(new QName("OrderDetail", "Unit"));
		elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new ElementDesc();
		elemField.setFieldName("remark");
		elemField.setXmlName(new QName("OrderDetail", "Remark"));
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
