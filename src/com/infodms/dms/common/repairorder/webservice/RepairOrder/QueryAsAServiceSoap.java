/**
 * QueryAsAServiceSoap.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.infodms.dms.common.repairorder.webservice.RepairOrder;

import java.rmi.RemoteException;

public interface QueryAsAServiceSoap extends java.rmi.Remote {

    /**
     * Get Web Service Provider server info
     */
    public RunQueryAsAServiceResponse runQueryAsAService(RunQueryAsAService parameters, String request_header) throws RemoteException;
}
