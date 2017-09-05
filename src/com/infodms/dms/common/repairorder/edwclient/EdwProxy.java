/**********************************************************************
* <pre>
* FILE :   EdwProxy.java
* CLASS :  EdwProxy
*
* AUTHOR : witti
*
* FUNCTION : 维修历史调用接口
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
* $Id: EdwProxy.java,v 1.1 2010/08/16 01:43:46 yuch Exp $
*/

package com.infodms.dms.common.repairorder.edwclient;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.infodms.dms.common.repairorder.webservice.OrderDetail.OrderDetailLocator;
import com.infodms.dms.common.repairorder.webservice.RepairOrder.Row;
import com.infodms.dms.common.repairorder.webservice.RepairOrder.RepairOrderLocator;


/**
 * Function 	: 维修历史调用接口
 * Author  		: witti
 * Create_Date  : 2009-08-21
 * Version		: 0.1
 */
public class EdwProxy {
	
	/**
	* 功能说明：查询维修历史主数据清单
	* @param： vin 
	* @return：List<RepairOrder>
	* @throws：Exception
	* 最后修改时间：2009-08-22
	*/
	public static List<RepairOrder> getRepairOrderList(String vin) throws Exception {
		if(!isConnect(RepairOrderLocator.QueryAsAServiceSoap_address)) {
			throw new Exception();
		}
		List<RepairOrder> list = new ArrayList<RepairOrder>();
		List<Row> tlist = EdwWebService.queryRepairOrderList(vin);
		for(Row row:tlist){
			RepairOrder ro = new RepairOrder();
			ro.setBalanceNo((row.getBalance_No()==null)?"":row.getBalance_No().trim());					//结算单号
			ro.setInMileage((row.getIn_Mileage()==null)?"":row.getIn_Mileage());						//进厂里程
			ro.setIsChangeMileage((row.getIs_Change_Mileage()==null)?"":row.getIs_Change_Mileage().trim());	//是否换表
			ro.setLicense((row.getLicense()==null)?"":row.getLicense().trim());						//车牌号
			ro.setRepairType((row.getRepair_Type()==null)?"":row.getRepair_Type().trim());				//维修类型
			ro.setRoNo((row.getRo_No()==null)?"":row.getRo_No().trim());							//工单号
			ro.setRoType((row.getRo_Type()==null)?"":row.getRo_Type().trim());						//工单类型
			ro.setStartTime((row.getStart_Time()==null)?"":row.getStart_Time().trim());					//开单日期
			list.add(ro);
		}
		tlist = null;
		return list;
	}
	
	/**
	* 功能说明：查询维修历史明细数据清单
	* @param： vin
	* @param： balance_no 结算单号
	* @throws: Exception 
	* @return：List<RepairItem>[]
	* 最后修改时间：2009-08-22
	*/
	public static List<RepairItem>[] getRepairOrderList(String vin, String balance_no) throws Exception {
		
		List<RepairItem>[] list = null;
		
		ArrayList<RepairItem> wx = new ArrayList<RepairItem>();		//维修项目
		ArrayList<RepairItem> wc = new ArrayList<RepairItem>();		//维修材料
		ArrayList<RepairItem> xc = new ArrayList<RepairItem>();		//销售材料
		ArrayList<RepairItem> fx = new ArrayList<RepairItem>();		//附加项目
		ArrayList<RepairItem> fglx = new ArrayList<RepairItem>();	//辅料管理项目
		
		List<com.infodms.dms.common.repairorder.webservice.OrderDetail.Row> tlist = EdwWebService.queryOrderDetailList(vin,balance_no);
		
		for(com.infodms.dms.common.repairorder.webservice.OrderDetail.Row row:tlist){
			RepairItem ro = null;
			if(row.getType().equals("1")) {
				ro = new RepairItem();
				ro.setTroubleDesc((row.getTrouble_Desc()==null)?"":row.getTrouble_Desc().trim());		//故障描述
				ro.setTroubleCause((row.getTrouble_Cause()==null)?"":row.getTrouble_Cause().trim());	//故障原因
				ro.setCode((row.getCode()==null)?"":row.getCode().trim());								//项目代码
				ro.setName((row.getName()==null)?"":row.getName().trim());								//项目名称
				ro.setStdLabourHour(row.getStd_Labour_Hour());											//标准工时
				ro.setAddLabourHour(row.getAdd_Labour_Hour());											//附加工时
				ro.setChargeMode((row.getCharge_Mode()==null)?"":row.getCharge_Mode().trim());			//收费区分
				wx.add(ro);
			}else if(row.getType().equals("2")) {
				ro = new RepairItem();
				ro.setCode((row.getCode()==null)?"":row.getCode().trim());								//配件代码
				ro.setName((row.getName()==null)?"":row.getName().trim());								//配件名称
				ro.setPartQuantity(row.getPart_Quantity());												//数量
				ro.setChargeMode((row.getCharge_Mode()==null)?"":row.getCharge_Mode().trim());			//收费区分
				wc.add(ro);
			}else if(row.getType().equals("3")) {
				ro = new RepairItem();
				ro.setCode((row.getCode()==null)?"":row.getCode().trim());								//配件代码
				ro.setName((row.getName()==null)?"":row.getName().trim());								//配件名称
				ro.setPartQuantity(row.getPart_Quantity());												//数量
				fx.add(ro);
			}else if(row.getType().equals("4")) {
				ro = new RepairItem();
				ro.setCode((row.getCode()==null)?"":row.getCode().trim());								//代码
				ro.setRemark((row.getRemark()==null)?"":row.getRemark().trim());						//说明
				xc.add(ro);
			}else if(row.getType().equals("5")) {
				ro = new RepairItem();
				ro.setCode((row.getCode()==null)?"":row.getCode().trim());								//代码
				ro.setName((row.getName()==null)?"":row.getName().trim());								//名称
				fglx.add(ro);
			}
		}
		if(wx!=null||wc!=null||xc!=null||fx!=null||fglx!=null) {
			list = new ArrayList[]{wx,wc,xc,fx,fglx};
		}
		tlist = null;
		wx = null;
		wc = null;
		xc = null;
		fx = null;
		fglx = null;
		return list;
	}
	
	public static void main(String[] args) throws Exception {
		EdwWebService.uName = "wsuser";
		EdwWebService.uPwd = "wsuser";
		OrderDetailLocator.QueryAsAServiceSoap_address = "http://10.203.16.13:8080/dswsbobje/qaawsservices/queryasaservice";
		RepairOrderLocator.QueryAsAServiceSoap_address = "http://10.203.16.13:8080/dswsbobje/qaawsservices/queryasaservice";
		List<RepairOrder> t1 = getRepairOrderList("LSGVU52Z57Y046068");
		List<RepairItem>[] t2 = getRepairOrderList("LSGVU52Z57Y046068","ABO07060174");
		for(RepairOrder r:t1) {
			System.err.println("BalanceNo:   "+r.getBalanceNo());
			System.err.println("InMileage:   "+r.getInMileage());
			System.err.println("IsChangeMileage:   "+r.getIsChangeMileage());
			System.err.println("License:   "+r.getLicense());
			System.err.println("RepairType:   "+r.getRepairType());
			System.err.println("RoNo:   "+r.getRoNo());
			System.err.println("RoType:   "+r.getRoType());
			System.err.println("StartTime:   "+r.getStartTime());
		}
		for(List<RepairItem> tt:t2) {
			for(RepairItem r:tt) {
				System.err.println("AddLabourHour:   "+r.getAddLabourHour());
				System.err.println("BalanceNo:   "+r.getBalanceNo());
				System.err.println("ChargeMode:   "+r.getChargeMode());
				System.err.println("Code:   "+r.getCode());
				System.err.println("Name:   "+r.getName());
				System.err.println("PartQuantity:   "+r.getPartQuantity());
				System.err.println("Remark:   "+r.getRemark());
				System.err.println("StdLabourHour:   "+r.getStdLabourHour());
				System.err.println("TroubleCause:   "+r.getTroubleCause());
				System.err.println("TroubleDesc:   "+r.getTroubleDesc());
			}
		}
	}
	
	// 检查WEBSERVICE服务器是否能响应
	private synchronized static boolean isConnect(String url) throws Exception {
		int counts = 0;               
		if (url==null||url.length()<= 0) { 
			return false;                  
		}
		int state = -1;
		while (counts < 5) {
		  	counts++;
			URL urlStr = new URL(url);
			HttpURLConnection conn = (HttpURLConnection) urlStr.openConnection();
			state = conn.getResponseCode();
			if (state != 200) {
				return false;
			}
		}
		return true;
	}
	
}
