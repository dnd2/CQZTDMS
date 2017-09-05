/**********************************************************************
* <pre>
* FILE :   EdwWebService.java
* CLASS :  EdwWebService
*
* AUTHOR : witti
*
* FUNCTION : 发送数据请求并接收数据反馈
*
*
*======================================================================
* CHANGE HISTORY LOG
*----------------------------------------------------------------------
* MOD. NO.|    DATE    |   NAME    |    REASON   | CHANGE REQ.
9
*----------------------------------------------------------------------
*         | 2009-08-21 |  witti    |    Created  |
* DESCRIPTION:
* </pre>
***********************************************************************/
/**
* $Id: EdwWebService.java,v 1.1 2010/08/16 01:43:46 yuch Exp $
*/

package com.infodms.dms.common.repairorder.edwclient;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.infodms.dms.common.repairorder.webservice.OrderDetail.OrderDetailLocator;
import com.infodms.dms.common.repairorder.webservice.RepairOrder.QueryAsAServiceSoap;
import com.infodms.dms.common.repairorder.webservice.RepairOrder.RepairOrderLocator;
import com.infodms.dms.common.repairorder.webservice.RepairOrder.Row;
import com.infodms.dms.common.repairorder.webservice.RepairOrder.RunQueryAsAService;
import com.infodms.dms.common.repairorder.webservice.RepairOrder.RunQueryAsAServiceResponse;
import com.infodms.dms.exception.BizException;

/**
 * Function 	: 封装Webservice调用及数据接收
 * Author  		: witti
 * Create_Date  : 2009-08-21
 * Version		: 0.1
 */
public class EdwWebService {
	//需要传输的用户名
	public static String uName = "";
	//需要传输的密码
	public static String uPwd = "";
	
	private static Logger logger = LogManager.getLogger(EdwWebService.class);
	
	//维修明细Service代理
	private static OrderDetailLocator odLoc= new OrderDetailLocator();
	//维修历史清单Service代理
	private static RepairOrderLocator roLoc = new RepairOrderLocator();
	static{
		odLoc.setMaintainSession(true);
		roLoc.setMaintainSession(true);
	}
	
	/**
	* 功能说明：请求维修历史主数据清单
	* @param： vin
	 * @throws BizException 
	* @return：List<Row>
	* @throws：Exception
	* 最后修改时间：2009-08-21
	*/
	public static List<Row> queryRepairOrderList(String vin) throws Exception {
		
		List<Row> list = new ArrayList<Row>(); 
		try{
			QueryAsAServiceSoap ws = roLoc.getQueryAsAServiceSoap();
			RunQueryAsAService param = new RunQueryAsAService();
			param.setLogin(uName);
			param.setPassword(uPwd);
			param.setVin(vin);
			RunQueryAsAServiceResponse ret = ws.runQueryAsAService(param, null);	
			
			for( Row row : ret.getTable() ){
				if(row!=null&&row.getBalance_No()!=null&&!row.getBalance_No().trim().equals("")) {
					list.add(row);
				}
			}			
		}catch(Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage(),e);
			throw new Exception("通过WebService获取SGM车辆维修历史信息时出现错误");
		}
		
		return list;
	}
	
	/**
	* 功能说明：请求维修历史明细数据清单
	* @param： vin
	* @param： oid ：balance_no
	 * @throws BizException 
	* @return：List<Row>
	* @throws：Exception
	* 最后修改时间：2009-08-21
	*/
	public static List<com.infodms.dms.common.repairorder.webservice.OrderDetail.Row> queryOrderDetailList(String vin, String oid) throws Exception{

		List<com.infodms.dms.common.repairorder.webservice.OrderDetail.Row> list = 
			new ArrayList<com.infodms.dms.common.repairorder.webservice.OrderDetail.Row>(); 
		try{
			com.infodms.dms.common.repairorder.webservice.OrderDetail.QueryAsAServiceSoap ws =odLoc.getQueryAsAServiceSoap();			
			com.infodms.dms.common.repairorder.webservice.OrderDetail.RunQueryAsAService param = 
				new com.infodms.dms.common.repairorder.webservice.OrderDetail.RunQueryAsAService();
			param.setLogin(uName);
			param.setPassword(uPwd);
			param.setVin(vin);
			param.setBalance_no(oid);
			com.infodms.dms.common.repairorder.webservice.OrderDetail.RunQueryAsAServiceResponse ret = ws.runQueryAsAService(param, null);
			
			for( com.infodms.dms.common.repairorder.webservice.OrderDetail.Row row : ret.getTable() ){
				if( row!=null&&row.getBalance_No() !=null&& row.getBalance_No().trim().length()>0 ){
					list.add(row);
				}
			}			
		}catch(Exception e){
			e.printStackTrace();
			logger.error(e.getMessage(),e);
			throw new Exception("通过WebService获取SGM车辆维修历史明细信息时出现错误");
		}
		
		return list;
	}

	public static void setUrl(String url) {
		OrderDetailLocator.QueryAsAServiceSoap_address = url;
		RepairOrderLocator.QueryAsAServiceSoap_address = url;
	}
	
}
