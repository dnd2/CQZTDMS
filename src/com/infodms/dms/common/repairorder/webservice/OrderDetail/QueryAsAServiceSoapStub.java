/**
 * QueryAsAServiceSoapStub.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.infodms.dms.common.repairorder.webservice.OrderDetail;

import java.net.URL;
import java.rmi.RemoteException;
import java.util.Enumeration;
import java.util.Vector;

import javax.xml.namespace.QName;

import org.apache.axis.AxisFault;
import org.apache.axis.NoEndPointException;
import org.apache.axis.client.Call;
import org.apache.axis.client.Stub;
import org.apache.axis.constants.Style;
import org.apache.axis.constants.Use;
import org.apache.axis.description.OperationDesc;
import org.apache.axis.description.ParameterDesc;
import org.apache.axis.encoding.DeserializerFactory;
import org.apache.axis.encoding.SerializerFactory;
import org.apache.axis.encoding.ser.ArrayDeserializerFactory;
import org.apache.axis.encoding.ser.ArraySerializerFactory;
import org.apache.axis.encoding.ser.BeanDeserializerFactory;
import org.apache.axis.encoding.ser.BeanSerializerFactory;
import org.apache.axis.encoding.ser.EnumDeserializerFactory;
import org.apache.axis.encoding.ser.EnumSerializerFactory;
import org.apache.axis.encoding.ser.SimpleDeserializerFactory;
import org.apache.axis.encoding.ser.SimpleListDeserializerFactory;
import org.apache.axis.encoding.ser.SimpleListSerializerFactory;
import org.apache.axis.encoding.ser.SimpleSerializerFactory;
import org.apache.axis.utils.JavaUtils;

public class QueryAsAServiceSoapStub extends Stub implements QueryAsAServiceSoap {
	private Vector cachedSerClasses = new Vector();
	private Vector cachedSerQNames = new Vector();
	private Vector cachedSerFactories = new Vector();
	private Vector cachedDeserFactories = new Vector();

	static OperationDesc[] _operations;

	static {
		_operations = new OperationDesc[1];
		_initOperationDesc1();
	}

	private static void _initOperationDesc1() {
		OperationDesc oper;
		ParameterDesc param;
		oper = new OperationDesc();
		oper.setName("runQueryAsAService");
		param = new ParameterDesc(new QName("OrderDetail", "runQueryAsAService"), ParameterDesc.IN,
				new QName("OrderDetail", ">runQueryAsAService"), RunQueryAsAService.class, false, false);
		oper.addParameter(param);
		param = new ParameterDesc(new QName("OrderDetail", "sessionID"), ParameterDesc.IN, new QName(
				"http://www.w3.org/2001/XMLSchema", "string"), String.class, true, false);
		oper.addParameter(param);
		oper.setReturnType(new QName("OrderDetail", ">runQueryAsAServiceResponse"));
		oper.setReturnClass(RunQueryAsAServiceResponse.class);
		oper.setReturnQName(new QName("OrderDetail", "runQueryAsAServiceResponse"));
		oper.setStyle(Style.DOCUMENT);
		oper.setUse(Use.LITERAL);
		_operations[0] = oper;

	}

	public QueryAsAServiceSoapStub() throws AxisFault {
		this(null);
	}

	public QueryAsAServiceSoapStub(URL endpointURL, javax.xml.rpc.Service service) throws AxisFault {
		this(service);
		super.cachedEndpoint = endpointURL;
	}

	public QueryAsAServiceSoapStub(javax.xml.rpc.Service service) throws AxisFault {
		if (service == null) {
			super.service = new org.apache.axis.client.Service();
		} else {
			super.service = service;
		}
		((org.apache.axis.client.Service) super.service).setTypeMappingVersion("1.2");
		java.lang.Class cls;
		QName qName;
		QName qName2;
		java.lang.Class beansf = BeanSerializerFactory.class;
		java.lang.Class beandf = BeanDeserializerFactory.class;
		java.lang.Class enumsf = EnumSerializerFactory.class;
		java.lang.Class enumdf = EnumDeserializerFactory.class;
		java.lang.Class arraysf = ArraySerializerFactory.class;
		java.lang.Class arraydf = ArrayDeserializerFactory.class;
		java.lang.Class simplesf = SimpleSerializerFactory.class;
		java.lang.Class simpledf = SimpleDeserializerFactory.class;
		java.lang.Class simplelistsf = SimpleListSerializerFactory.class;
		java.lang.Class simplelistdf = SimpleListDeserializerFactory.class;
		qName = new QName("OrderDetail", ">runQueryAsAService");
		cachedSerQNames.add(qName);
		cls = RunQueryAsAService.class;
		cachedSerClasses.add(cls);
		cachedSerFactories.add(beansf);
		cachedDeserFactories.add(beandf);

		qName = new QName("OrderDetail", ">runQueryAsAServiceResponse");
		cachedSerQNames.add(qName);
		cls = RunQueryAsAServiceResponse.class;
		cachedSerClasses.add(cls);
		cachedSerFactories.add(beansf);
		cachedDeserFactories.add(beandf);

		qName = new QName("OrderDetail", "Row");
		cachedSerQNames.add(qName);
		cls = Row.class;
		cachedSerClasses.add(cls);
		cachedSerFactories.add(beansf);
		cachedDeserFactories.add(beandf);

		qName = new QName("OrderDetail", "Table");
		cachedSerQNames.add(qName);
		cls = Row[].class;
		cachedSerClasses.add(cls);
		qName = new QName("OrderDetail", "Row");
		qName2 = new QName("OrderDetail", "row");
		cachedSerFactories.add(new ArraySerializerFactory(qName, qName2));
		cachedDeserFactories.add(new ArrayDeserializerFactory());

	}

	protected Call createCall() throws RemoteException {
		try {
			Call _call = super._createCall();
			if (super.maintainSessionSet) {
				_call.setMaintainSession(super.maintainSession);
			}
			if (super.cachedUsername != null) {
				_call.setUsername(super.cachedUsername);
			}
			if (super.cachedPassword != null) {
				_call.setPassword(super.cachedPassword);
			}
			if (super.cachedEndpoint != null) {
				_call.setTargetEndpointAddress(super.cachedEndpoint);
			}
			if (super.cachedTimeout != null) {
				_call.setTimeout(super.cachedTimeout);
			}
			if (super.cachedPortName != null) {
				_call.setPortName(super.cachedPortName);
			}
			Enumeration keys = super.cachedProperties.keys();
			while (keys.hasMoreElements()) {
				String key = (String) keys.nextElement();
				_call.setProperty(key, super.cachedProperties.get(key));
			}
			// All the type mapping information is registered
			// when the first call is made.
			// The type mapping information is actually registered in
			// the TypeMappingRegistry of the service, which
			// is the reason why registration is only needed for the first call.
			synchronized (this) {
				if (firstCall()) {
					// must set encoding style before registering serializers
					_call.setEncodingStyle(null);
					for (int i = 0; i < cachedSerFactories.size(); ++i) {
						Class cls = (Class) cachedSerClasses.get(i);
						QName qName = (QName) cachedSerQNames.get(i);
						java.lang.Object x = cachedSerFactories.get(i);
						if (x instanceof Class) {
							Class sf = (Class) cachedSerFactories.get(i);
							Class df = (Class) cachedDeserFactories.get(i);
							_call.registerTypeMapping(cls, qName, sf, df, false);
						} else if (x instanceof javax.xml.rpc.encoding.SerializerFactory) {
							SerializerFactory sf = (SerializerFactory) cachedSerFactories.get(i);
							DeserializerFactory df = (DeserializerFactory) cachedDeserFactories.get(i);
							_call.registerTypeMapping(cls, qName, sf, df, false);
						}
					}
				}
			}
			return _call;
		} catch (java.lang.Throwable _t) {
			throw new AxisFault("Failure trying to get the Call object", _t);
		}
	}

	public RunQueryAsAServiceResponse runQueryAsAService(RunQueryAsAService parameters, String request_header) throws RemoteException {
		if (super.cachedEndpoint == null) {
			throw new NoEndPointException();
		}
		org.apache.axis.client.Call _call = createCall();
		_call.setOperation(_operations[0]);
		_call.setUseSOAPAction(true);
		_call.setSOAPActionURI("OrderDetail/runQueryAsAService");
		_call.setEncodingStyle(null);
		_call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
		_call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
		_call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
		_call.setOperationName(new QName("", "runQueryAsAService"));

		setRequestHeaders(_call);
		setAttachments(_call);
		try {
			Object _resp = _call.invoke(new Object[] { parameters, request_header });

			if (_resp instanceof RemoteException) {
				throw (RemoteException) _resp;
			} else {
				extractAttachments(_call);
				try {
					return (RunQueryAsAServiceResponse) _resp;
				} catch (Exception _exception) {
					return (RunQueryAsAServiceResponse) JavaUtils.convert(_resp, RunQueryAsAServiceResponse.class);
				}
			}
		} catch (AxisFault axisFaultException) {
			throw axisFaultException;
		}
	}

	}
