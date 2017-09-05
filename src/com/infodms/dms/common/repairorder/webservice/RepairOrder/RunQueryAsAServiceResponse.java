/**
 * RunQueryAsAServiceResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.infodms.dms.common.repairorder.webservice.RepairOrder;

public class RunQueryAsAServiceResponse  implements java.io.Serializable {
    private Row[] table;

    private java.lang.String message;

    private java.lang.String creatorname;

    private java.util.Calendar creationdate;

    private java.lang.String creationdateformated;

    private java.lang.String description;

    private java.lang.String universe;

    private int queryruntime;

    private int fetchedrows;

    public RunQueryAsAServiceResponse() {
    }

    public RunQueryAsAServiceResponse(
           Row[] table,
           java.lang.String message,
           java.lang.String creatorname,
           java.util.Calendar creationdate,
           java.lang.String creationdateformated,
           java.lang.String description,
           java.lang.String universe,
           int queryruntime,
           int fetchedrows) {
           this.table = table;
           this.message = message;
           this.creatorname = creatorname;
           this.creationdate = creationdate;
           this.creationdateformated = creationdateformated;
           this.description = description;
           this.universe = universe;
           this.queryruntime = queryruntime;
           this.fetchedrows = fetchedrows;
    }


    /**
     * Gets the table value for this RunQueryAsAServiceResponse.
     * 
     * @return table
     */
    public Row[] getTable() {
        return table;
    }


    /**
     * Sets the table value for this RunQueryAsAServiceResponse.
     * 
     * @param table
     */
    public void setTable(Row[] table) {
        this.table = table;
    }


    /**
     * Gets the message value for this RunQueryAsAServiceResponse.
     * 
     * @return message
     */
    public java.lang.String getMessage() {
        return message;
    }


    /**
     * Sets the message value for this RunQueryAsAServiceResponse.
     * 
     * @param message
     */
    public void setMessage(java.lang.String message) {
        this.message = message;
    }


    /**
     * Gets the creatorname value for this RunQueryAsAServiceResponse.
     * 
     * @return creatorname
     */
    public java.lang.String getCreatorname() {
        return creatorname;
    }


    /**
     * Sets the creatorname value for this RunQueryAsAServiceResponse.
     * 
     * @param creatorname
     */
    public void setCreatorname(java.lang.String creatorname) {
        this.creatorname = creatorname;
    }


    /**
     * Gets the creationdate value for this RunQueryAsAServiceResponse.
     * 
     * @return creationdate
     */
    public java.util.Calendar getCreationdate() {
        return creationdate;
    }


    /**
     * Sets the creationdate value for this RunQueryAsAServiceResponse.
     * 
     * @param creationdate
     */
    public void setCreationdate(java.util.Calendar creationdate) {
        this.creationdate = creationdate;
    }


    /**
     * Gets the creationdateformated value for this RunQueryAsAServiceResponse.
     * 
     * @return creationdateformated
     */
    public java.lang.String getCreationdateformated() {
        return creationdateformated;
    }


    /**
     * Sets the creationdateformated value for this RunQueryAsAServiceResponse.
     * 
     * @param creationdateformated
     */
    public void setCreationdateformated(java.lang.String creationdateformated) {
        this.creationdateformated = creationdateformated;
    }


    /**
     * Gets the description value for this RunQueryAsAServiceResponse.
     * 
     * @return description
     */
    public java.lang.String getDescription() {
        return description;
    }


    /**
     * Sets the description value for this RunQueryAsAServiceResponse.
     * 
     * @param description
     */
    public void setDescription(java.lang.String description) {
        this.description = description;
    }


    /**
     * Gets the universe value for this RunQueryAsAServiceResponse.
     * 
     * @return universe
     */
    public java.lang.String getUniverse() {
        return universe;
    }


    /**
     * Sets the universe value for this RunQueryAsAServiceResponse.
     * 
     * @param universe
     */
    public void setUniverse(java.lang.String universe) {
        this.universe = universe;
    }


    /**
     * Gets the queryruntime value for this RunQueryAsAServiceResponse.
     * 
     * @return queryruntime
     */
    public int getQueryruntime() {
        return queryruntime;
    }


    /**
     * Sets the queryruntime value for this RunQueryAsAServiceResponse.
     * 
     * @param queryruntime
     */
    public void setQueryruntime(int queryruntime) {
        this.queryruntime = queryruntime;
    }


    /**
     * Gets the fetchedrows value for this RunQueryAsAServiceResponse.
     * 
     * @return fetchedrows
     */
    public int getFetchedrows() {
        return fetchedrows;
    }


    /**
     * Sets the fetchedrows value for this RunQueryAsAServiceResponse.
     * 
     * @param fetchedrows
     */
    public void setFetchedrows(int fetchedrows) {
        this.fetchedrows = fetchedrows;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof RunQueryAsAServiceResponse)) return false;
        RunQueryAsAServiceResponse other = (RunQueryAsAServiceResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.table==null && other.getTable()==null) || 
             (this.table!=null &&
              java.util.Arrays.equals(this.table, other.getTable()))) &&
            ((this.message==null && other.getMessage()==null) || 
             (this.message!=null &&
              this.message.equals(other.getMessage()))) &&
            ((this.creatorname==null && other.getCreatorname()==null) || 
             (this.creatorname!=null &&
              this.creatorname.equals(other.getCreatorname()))) &&
            ((this.creationdate==null && other.getCreationdate()==null) || 
             (this.creationdate!=null &&
              this.creationdate.equals(other.getCreationdate()))) &&
            ((this.creationdateformated==null && other.getCreationdateformated()==null) || 
             (this.creationdateformated!=null &&
              this.creationdateformated.equals(other.getCreationdateformated()))) &&
            ((this.description==null && other.getDescription()==null) || 
             (this.description!=null &&
              this.description.equals(other.getDescription()))) &&
            ((this.universe==null && other.getUniverse()==null) || 
             (this.universe!=null &&
              this.universe.equals(other.getUniverse()))) &&
            this.queryruntime == other.getQueryruntime() &&
            this.fetchedrows == other.getFetchedrows();
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = 1;
        if (getTable() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getTable());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getTable(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getMessage() != null) {
            _hashCode += getMessage().hashCode();
        }
        if (getCreatorname() != null) {
            _hashCode += getCreatorname().hashCode();
        }
        if (getCreationdate() != null) {
            _hashCode += getCreationdate().hashCode();
        }
        if (getCreationdateformated() != null) {
            _hashCode += getCreationdateformated().hashCode();
        }
        if (getDescription() != null) {
            _hashCode += getDescription().hashCode();
        }
        if (getUniverse() != null) {
            _hashCode += getUniverse().hashCode();
        }
        _hashCode += getQueryruntime();
        _hashCode += getFetchedrows();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(RunQueryAsAServiceResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("RepairOrder", ">runQueryAsAServiceResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("table");
        elemField.setXmlName(new javax.xml.namespace.QName("RepairOrder", "table"));
        elemField.setXmlType(new javax.xml.namespace.QName("RepairOrder", "Row"));
        elemField.setNillable(false);
        elemField.setItemQName(new javax.xml.namespace.QName("RepairOrder", "row"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("message");
        elemField.setXmlName(new javax.xml.namespace.QName("RepairOrder", "message"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("creatorname");
        elemField.setXmlName(new javax.xml.namespace.QName("RepairOrder", "creatorname"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("creationdate");
        elemField.setXmlName(new javax.xml.namespace.QName("RepairOrder", "creationdate"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("creationdateformated");
        elemField.setXmlName(new javax.xml.namespace.QName("RepairOrder", "creationdateformated"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("description");
        elemField.setXmlName(new javax.xml.namespace.QName("RepairOrder", "description"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("universe");
        elemField.setXmlName(new javax.xml.namespace.QName("RepairOrder", "universe"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("queryruntime");
        elemField.setXmlName(new javax.xml.namespace.QName("RepairOrder", "queryruntime"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("fetchedrows");
        elemField.setXmlName(new javax.xml.namespace.QName("RepairOrder", "fetchedrows"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
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
    public static org.apache.axis.encoding.Serializer getSerializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanSerializer(
            _javaType, _xmlType, typeDesc);
    }

    /**
     * Get Custom Deserializer
     */
    public static org.apache.axis.encoding.Deserializer getDeserializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanDeserializer(
            _javaType, _xmlType, typeDesc);
    }

}
