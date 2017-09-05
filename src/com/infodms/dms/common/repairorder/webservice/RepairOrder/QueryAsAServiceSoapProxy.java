package com.infodms.dms.common.repairorder.webservice.RepairOrder;

import java.rmi.RemoteException;

import javax.xml.rpc.Stub;

public class QueryAsAServiceSoapProxy implements QueryAsAServiceSoap {
	private String _endpoint = null;
	private QueryAsAServiceSoap queryAsAServiceSoap = null;

	public QueryAsAServiceSoapProxy() {
		_initQueryAsAServiceSoapProxy();
	}

	public QueryAsAServiceSoapProxy(String endpoint) {
		_endpoint = endpoint;
		_initQueryAsAServiceSoapProxy();
	}

	private void _initQueryAsAServiceSoapProxy() {
		try {
			queryAsAServiceSoap = (new RepairOrderLocator()).getQueryAsAServiceSoap();
			if (queryAsAServiceSoap != null) {
				if (_endpoint != null)
					((javax.xml.rpc.Stub) queryAsAServiceSoap)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
				else
					_endpoint = (String) ((javax.xml.rpc.Stub) queryAsAServiceSoap)._getProperty("javax.xml.rpc.service.endpoint.address");
			}

		} catch (javax.xml.rpc.ServiceException serviceException) {
		}
	}

	public String getEndpoint() {
		return _endpoint;
	}

	public void setEndpoint(String endpoint) {
		_endpoint = endpoint;
		if (queryAsAServiceSoap != null)
			((Stub) queryAsAServiceSoap)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);

	}

	public QueryAsAServiceSoap getQueryAsAServiceSoap() {
		if (queryAsAServiceSoap == null)
			_initQueryAsAServiceSoapProxy();
		return queryAsAServiceSoap;
	}

	public RunQueryAsAServiceResponse runQueryAsAService(RunQueryAsAService parameters, String request_header) throws RemoteException {
		if (queryAsAServiceSoap == null)
			_initQueryAsAServiceSoapProxy();
		return queryAsAServiceSoap.runQueryAsAService(parameters, request_header);
	}

}