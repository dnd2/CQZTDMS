package com.infodms.dms.common.repairorder.webservice.OrderDetail;

public class QueryAsAServiceSoapProxy implements com.infodms.dms.common.repairorder.webservice.OrderDetail.QueryAsAServiceSoap {
  private String _endpoint = null;
  private com.infodms.dms.common.repairorder.webservice.OrderDetail.QueryAsAServiceSoap queryAsAServiceSoap = null;
  
  public QueryAsAServiceSoapProxy() {
    _initQueryAsAServiceSoapProxy();
  }
  
  public QueryAsAServiceSoapProxy(String endpoint) {
    _endpoint = endpoint;
    _initQueryAsAServiceSoapProxy();
  }
  
  private void _initQueryAsAServiceSoapProxy() {
    try {
      queryAsAServiceSoap = (new com.infodms.dms.common.repairorder.webservice.OrderDetail.OrderDetailLocator()).getQueryAsAServiceSoap();
      if (queryAsAServiceSoap != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)queryAsAServiceSoap)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)queryAsAServiceSoap)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (queryAsAServiceSoap != null)
      ((javax.xml.rpc.Stub)queryAsAServiceSoap)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public com.infodms.dms.common.repairorder.webservice.OrderDetail.QueryAsAServiceSoap getQueryAsAServiceSoap() {
    if (queryAsAServiceSoap == null)
      _initQueryAsAServiceSoapProxy();
    return queryAsAServiceSoap;
  }
  
  public com.infodms.dms.common.repairorder.webservice.OrderDetail.RunQueryAsAServiceResponse runQueryAsAService(com.infodms.dms.common.repairorder.webservice.OrderDetail.RunQueryAsAService parameters, java.lang.String request_header) throws java.rmi.RemoteException{
    if (queryAsAServiceSoap == null)
      _initQueryAsAServiceSoapProxy();
    return queryAsAServiceSoap.runQueryAsAService(parameters, request_header);
  }
  
  
}