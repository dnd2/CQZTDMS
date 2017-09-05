package com.infodms.dms.util.businessUtil;

import java.sql.ResultSet;
import java.util.Calendar;
import java.util.Map;

import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.common.BaseDao;
import com.infoservice.po3.bean.PO;

public class GetOrderNOUtil extends BaseDao {
	
	private static final GetOrderNOUtil dao = new GetOrderNOUtil();

	public static final GetOrderNOUtil getInstance() {
		return dao;
	}

	/**
	 * 生成订单号
	 * 订单号规则：业务范围+订单类型+订单使用范围+经销商代码+年(后两位)+月+日+序列号(从001开始递增)+发运指令号码(从"-01"开始递增)
	 * 注：序列号的递增以经销商为标准，如 经销商A提一张订单,订单号为 DJJXSA1001001,当经销商B提订单时,订单号为DJJXSB1001001
	 *     序列号都为001
	 * 1.String  orderType：订单类型 
	 *   (1)常规订单:订单号以C开始
	 *   (2)空：订单号码从“使用范围”开始
	 * 2.String  orderRange：订单使用范围
	 *   (1)轿车：DJ
	 *   (2)微车：待定
	 * 3.String  dealerCode：经销商代码(订单开票方)
	 * 4.String  isDelivery: 是否是发运指令订单号
	 * 例：String orderNO1 = getOrderNO("A",Constant.ORDER_TYPE_01+"" , "DJ" , "经销商代码A");
	 * 	   result=ACDJ经销商代码A10091901
	 *     String orderNO2 = getOrderNO("B","", "DW" , "经销商代码B");
	 *     result=BDW经销商代码A10091901
	 * */
	public static String getOrderNO(String busType ,String orderType,String orderRange,String dealerCode){
		
		StringBuffer orderNO = new StringBuffer();
		
		Calendar calendar = Calendar.getInstance();
		int year_ = calendar.get(calendar.YEAR);
		String year = year_+"";
		year = year.substring(2, 4);
		int month_ = calendar.get(calendar.MONTH)+1;
		String month = month_+"";
		if (month.length()<2) {
			month = month.format("0"+month, month);
		}
		int day_ = calendar.get(calendar.DAY_OF_MONTH);
		String day = day_+"";
		if (day.length()<2) {
			day = day.format("0"+day, day);
		}
		/*
		 * 查询数据库中是否已有相关最大订单号，如果有，新订单号=原订单号+1
		 * 否则新生成订单号
		 * */
		Map<String,String> oldOrderNOMap = getMaxOrderSeq(busType, orderType, orderRange, dealerCode, year, month ,day);
		String oldOrderNO = oldOrderNOMap.get("ORDER_NO");
		
		if (null != oldOrderNO && !"".equals(oldOrderNO)) {
			String commonNO = oldOrderNO.substring(0, oldOrderNO.length()-3);					//1得到订单序列号前的号码
			String indexNO = oldOrderNO.substring(oldOrderNO.length()-3, oldOrderNO.length());	//2.得到订单序列号
			int index_res = getIndex(busType, orderRange, dealerCode, year,  month, day, Integer.parseInt(indexNO)+1) ;										//3.将订单序列号+1
			String index_res_ = index_res+"";													//4.如果订单序列号不足三位，用0进行填补
			StringBuffer noBuffer = new StringBuffer();
			int index_res_length = 3-index_res_.length();
			for (int i = 0; i < index_res_length; i++) {
				noBuffer.append("0");
			}
			noBuffer.append(index_res_);
			orderNO.append(commonNO).append(noBuffer);											//5.将从数据中查询到的订单号+1后，赋给orderNO
			return orderNO.toString();
		}else{
			orderNO.append(busType);
			//如果是常规订单
			if (null != orderType && (Constant.ORDER_TYPE_01+"").equals(orderType)) {
				orderNO.append("C");
			}
			if (null != orderRange && !"".equals(orderRange)) {
				orderNO.append(orderRange).append(dealerCode);
			}else{
				orderNO.append("D").append(dealerCode);
			}
			
			int index_res = getIndex(busType, orderRange, dealerCode, year,  month, day, 1) ;										//3.将订单序列号+1
			String index_res_ = index_res+"";													//4.如果订单序列号不足三位，用0进行填补
			StringBuffer noBuffer = new StringBuffer();
			int index_res_length = 3-index_res_.length();
			for (int i = 0; i < index_res_length; i++) {
				noBuffer.append("0");
			}
			noBuffer.append(index_res_);
			return orderNO.append(year).append(month).append(day).append(noBuffer.substring(1)).toString();
		}
	}
	
	/**
	 * 生成发运单号
	 * @param busType
	 * @param orderRange
	 * @param dealerCode
	 * @return
	 */
    public static String getDlvryReqNO(String busType ,String orderRange,String dealerCode){
		
		StringBuffer orderNO = new StringBuffer();
		
		Calendar calendar = Calendar.getInstance();
		int year_ = calendar.get(calendar.YEAR);
		String year = year_+"";
		year = year.substring(2, 4);
		int month_ = calendar.get(calendar.MONTH)+1;
		String month = month_+"";
		if (month.length()<2) {
			month = month.format("0"+month, month);
		}
		int day_ = calendar.get(calendar.DAY_OF_MONTH);
		String day = day_+"";
		if (day.length()<2) {
			day = day.format("0"+day, day);
		}
		/*
		 * 查询数据库中是否已有相关最大订单号，如果有，新订单号=原订单号+1
		 * 否则新生成订单号
		 * */
		Map<String,String> oldOrderNOMap = getMaxDeliveryReqSeq(busType, orderRange, dealerCode, year, month, day);
		String oldOrderNO = oldOrderNOMap.get("ORDER_NO");
		if (null != oldOrderNO && !"".equals(oldOrderNO)) {
			String commonNO = oldOrderNO.substring(0, oldOrderNO.length()-3);					//1得到订单序列号前的号码
			String indexNO = oldOrderNO.substring(oldOrderNO.length()-3, oldOrderNO.length());	//2.得到订单序列号
			int index_res = getOrderIndex(busType, orderRange, dealerCode, year,  month, day, Integer.parseInt(indexNO)+1);										//3.将订单序列号+1
			String index_res_ = index_res+"";													//4.如果订单序列号不足三位，用0进行填补
			StringBuffer noBuffer = new StringBuffer();
			int index_res_length = 3-index_res_.length();
			for (int i = 0; i < index_res_length; i++) {
				noBuffer.append("0");
			}
			noBuffer.append(index_res_);
			orderNO.append(commonNO).append(noBuffer);											//5.将从数据中查询到的订单号+1后，赋给orderNO
			return orderNO.toString();
		}else{
			orderNO.append(busType);
			if (null != orderRange && !"".equals(orderRange)) {
				orderNO.append(orderRange).append(dealerCode);
			}else{
				orderNO.append("D").append(dealerCode);
			}
			int index_res = getOrderIndex(busType, orderRange, dealerCode, year,  month, day, 1) ;										//3.将订单序列号+1
			String index_res_ = index_res+"";													//4.如果订单序列号不足三位，用0进行填补
			StringBuffer noBuffer = new StringBuffer();
			int index_res_length = 3-index_res_.length();
			for (int i = 0; i < index_res_length; i++) {
				noBuffer.append("0");
			}
			noBuffer.append(index_res_);
			return orderNO.append(year).append(month).append(day).append(noBuffer.substring(1)).toString();
		}
	}
	
	/**
	 * 生成发运申请单号
	 * String orderNO :订单号
	 * */
	public static String getDlvryReqNO(String orderNO){
		
		StringBuffer dlvryReqNO = new StringBuffer();
		Map<String,String> oldDlvryReqNOMap = getMaxDlvryReqNO(orderNO);
		String oldDlvryReqNO = oldDlvryReqNOMap.get("DLVRY_REQ_NO");
		if (null != oldDlvryReqNO && !"".equals(oldDlvryReqNO)) {
			String indexNO = oldDlvryReqNO.substring(oldDlvryReqNO.length()-2, oldDlvryReqNO.length());	//1.得到发运申请序列号
			int index_res = Integer.parseInt(indexNO)+1;												//2.将发运申请序列号+1
			String index_res_ = index_res+"";												//3.如果发运申请序列号号不足两位，用0进行填补
			if (index_res_.length()<2) {
				index_res_ = String.format("0"+index_res_, index_res_);
			}
			dlvryReqNO.append(orderNO).append("-").append(index_res_);
			return dlvryReqNO.toString();
		}else{
			return dlvryReqNO.append(orderNO).append("-").append("01").toString();
		}
		
	}
	
	/**
	 * 查询最大订单号
	 * */
	public static Map<String,String> getMaxOrderSeq(String busType,String orderType,String orderRange,String dealerCode,String year, String month,String day){
		
		StringBuffer orderNOBuffer = new StringBuffer();
		orderNOBuffer.append(busType);
		if (null != orderType && (Constant.ORDER_TYPE_01+"").equals(orderType)) {
			orderNOBuffer.append("C");
		}
		orderNOBuffer.append(orderRange).append(dealerCode).append(year).append(month).append(day);
		
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT MAX(TTO.ORDER_NO) ORDER_NO FROM TT_VS_ORDER TTO WHERE TTO.ORDER_NO LIKE '"+orderNOBuffer+"%'\n");
		return dao.pageQueryMap(sql.toString(), null, dao.getFunName());	
	}
	
	public static Map<String,String> getMaxDeliveryReqSeq(String busType,String orderRange,String dealerCode,String year, String month,String day){
		
		StringBuffer orderNOBuffer = new StringBuffer();
		orderNOBuffer.append(busType);
		orderNOBuffer.append(orderRange).append(dealerCode).append(year).append(month).append(day);
		
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT MAX(TVDR.DLVRY_REQ_NO) ORDER_NO FROM TT_VS_DLVRY_REQ TVDR WHERE TVDR.DLVRY_REQ_NO LIKE '"+orderNOBuffer+"%'\n");
		return dao.pageQueryMap(sql.toString(), null, dao.getFunName());	
	}
	/**
	 * 查询最大发运申请号
	 * */
	public static Map<String,String> getMaxDlvryReqNO(String orderNO){
		
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT MAX(REQ.DLVRY_REQ_NO) DLVRY_REQ_NO\n");
		sql.append("  FROM TT_VS_DLVRY_REQ REQ\n");  
		sql.append(" WHERE REQ.DLVRY_REQ_NO LIKE '"+orderNO+"%'\n");

		return dao.pageQueryMap(sql.toString(), null, dao.getFunName());	
	}
	
	public static Map<String,String> getAreaShortcode(String areaId){
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT B.AREA_SHORTCODE\n");
		sql.append("  FROM TM_BUSINESS_AREA B\n");  
		sql.append(" WHERE B.AREA_ID = "+areaId+"\n");
		return dao.pageQueryMap(sql.toString(), null, dao.getFunName());	
	}
	
	public static int getIndex(String busType,String orderRange,String dealerCode,String year, String month,String day, int index) {
		int maxIndex = index ;
		Map<String,String> oldReqNOMap = getMaxDeliveryReqSeq(busType, orderRange, dealerCode, year, month, day) ;
		
		String oldReqNO = oldReqNOMap.get("ORDER_NO");
		
		if (null != oldReqNO && !"".equals(oldReqNO)) {
			String indexNO = oldReqNO.substring(oldReqNO.length()-3, oldReqNO.length());	//得到订单序列号
			int index_res = Integer.parseInt(indexNO)+1;									
			
			if(index_res >= index) {
				maxIndex = index_res ;
			} else {
				maxIndex = index ;
			}
		} 
		
		return maxIndex ;
	}
	
	public static int getOrderIndex(String busType,String orderRange,String dealerCode,String year, String month,String day, int index) {
		int maxIndex = index ;
		Map<String,String> oldReqNOMap = getMaxOrderSeq(busType, Constant.ORDER_TYPE_02.toString(), orderRange, dealerCode, year, month, day) ;
		
		String oldReqNO = oldReqNOMap.get("ORDER_NO");
		
		if (null != oldReqNO && !"".equals(oldReqNO)) {
			String indexNO = oldReqNO.substring(oldReqNO.length()-3, oldReqNO.length());	//得到订单序列号
			int index_res = Integer.parseInt(indexNO)+1;									
			
			if(index_res >= index) {
				maxIndex = index_res ;
			} else {
				maxIndex = index ;
			}
		} 
		
		return maxIndex ;
	}

	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}
}
