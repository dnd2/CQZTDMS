/**
 * RepairOrder.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.infodms.dms.common.repairorder.webservice.RepairOrder;

import java.net.URL;

import javax.xml.rpc.Service;
import javax.xml.rpc.ServiceException;

public interface RepairOrder extends Service {

/**
 * ????????
 */
    public String getQueryAsAServiceSoapAddress();

    public QueryAsAServiceSoap getQueryAsAServiceSoap() throws ServiceException;

    public QueryAsAServiceSoap getQueryAsAServiceSoap(URL portAddress) throws ServiceException;
}
