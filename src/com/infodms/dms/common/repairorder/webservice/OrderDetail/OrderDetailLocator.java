/**
 * OrderDetailLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.infodms.dms.common.repairorder.webservice.OrderDetail;

import javax.xml.namespace.QName;

public class OrderDetailLocator extends org.apache.axis.client.Service implements OrderDetail {

	/**
	 * ??????????
	 */

	public OrderDetailLocator() {
	}

	public OrderDetailLocator(org.apache.axis.EngineConfiguration config) {
		super(config);
	}

	public OrderDetailLocator(String wsdlLoc, QName sName) throws javax.xml.rpc.ServiceException {
		super(wsdlLoc, sName);
	}

	// Use to get a proxy class for QueryAsAServiceSoap
	public static String QueryAsAServiceSoap_address = "";

	public String getQueryAsAServiceSoapAddress() {
		return QueryAsAServiceSoap_address;
	}

	// The WSDD service name defaults to the port name.
	private String QueryAsAServiceSoapWSDDServiceName = "QueryAsAServiceSoap";

	public String getQueryAsAServiceSoapWSDDServiceName() {
		return QueryAsAServiceSoapWSDDServiceName;
	}

	public void setQueryAsAServiceSoapWSDDServiceName(String name) {
		QueryAsAServiceSoapWSDDServiceName = name;
	}

	public QueryAsAServiceSoap getQueryAsAServiceSoap() throws javax.xml.rpc.ServiceException {
		java.net.URL endpoint;
		try {
			endpoint = new java.net.URL(QueryAsAServiceSoap_address);
		} catch (java.net.MalformedURLException e) {
			throw new javax.xml.rpc.ServiceException(e);
		}
		return getQueryAsAServiceSoap(endpoint);
	}

	public QueryAsAServiceSoap getQueryAsAServiceSoap(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
		try {
			QueryAsAServiceSoapStub _stub = new QueryAsAServiceSoapStub(portAddress, this);
			_stub.setPortName(getQueryAsAServiceSoapWSDDServiceName());
			return _stub;
		} catch (org.apache.axis.AxisFault e) {
			return null;
		}
	}

	public void setQueryAsAServiceSoapEndpointAddress(String address) {
		QueryAsAServiceSoap_address = address;
	}

	/**
	 * For the given interface, get the stub implementation. If this service has
	 * no port for the given interface, then ServiceException is thrown.
	 */
	public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
		try {
			if (QueryAsAServiceSoap.class.isAssignableFrom(serviceEndpointInterface)) {
				QueryAsAServiceSoapStub _stub = new QueryAsAServiceSoapStub(new java.net.URL(QueryAsAServiceSoap_address), this);
				_stub.setPortName(getQueryAsAServiceSoapWSDDServiceName());
				return _stub;
			}
		} catch (java.lang.Throwable t) {
			throw new javax.xml.rpc.ServiceException(t);
		}
		throw new javax.xml.rpc.ServiceException("There is no stub implementation for the interface:  "
				+ (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
	}

	/**
	 * For the given interface, get the stub implementation. If this service has
	 * no port for the given interface, then ServiceException is thrown.
	 */
	public java.rmi.Remote getPort(QName portName, Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
		if (portName == null) {
			return getPort(serviceEndpointInterface);
		}
		String inputPortName = portName.getLocalPart();
		if ("QueryAsAServiceSoap".equals(inputPortName)) {
			return getQueryAsAServiceSoap();
		} else {
			java.rmi.Remote _stub = getPort(serviceEndpointInterface);
			((org.apache.axis.client.Stub) _stub).setPortName(portName);
			return _stub;
		}
	}

	public QName getServiceName() {
		return new QName("OrderDetail", "OrderDetail");
	}

	private java.util.HashSet ports = null;

	public java.util.Iterator getPorts() {
		if (ports == null) {
			ports = new java.util.HashSet();
			ports.add(new QName("OrderDetail", "QueryAsAServiceSoap"));
		}
		return ports.iterator();
	}

	/**
	 * Set the endpoint address for the specified port name.
	 */
	public void setEndpointAddress(String portName, String address) throws javax.xml.rpc.ServiceException {

		if ("QueryAsAServiceSoap".equals(portName)) {
			setQueryAsAServiceSoapEndpointAddress(address);
		} else { // Unknown Port Name
			throw new javax.xml.rpc.ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
		}
	}

	/**
	 * Set the endpoint address for the specified port name.
	 */
	public void setEndpointAddress(QName portName, String address) throws javax.xml.rpc.ServiceException {
		setEndpointAddress(portName.getLocalPart(), address);
	}

}
