package com.infoservice.dms.chana.dao;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("rawtypes")
public class SalseBoardDao extends BaseDao {
	public static Logger logger = Logger.getLogger(SalseBoardDao.class);
	private static final SalseBoardDao dao = new SalseBoardDao ();
	public static final SalseBoardDao getInstance() {
		return dao;
	}
	
	/**
	 * 方法描述 ： 获取服务器时间<br/>
	 * 
	 * @return
	 * @throws Exception
	 * @author hezongkun
	 */
	@SuppressWarnings("unchecked")
	public String getDbDate() throws Exception
	{
		return dao.pageQueryMap("SELECT TO_CHAR(SYSDATE,'yyyy-mm-dd hh24:mi:ss') DB_DATE FROM DUAL", null, getFunName()).get("DB_DATE").toString();
	}
	
	/**
	 * 根据物料ID获取车系ID
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String,Object>> getSeriesIdBymaterialId(){
		StringBuffer sql = new StringBuffer("") ;
		sql.append("SELECT  T3.GROUP_ID SERIES_ID,T3.GROUP_NAME SERIES_NAME,TVM.MATERIAL_ID \n");
		sql.append("FROM  TM_VHCL_MATERIAL_GROUP T,    \n");
		sql.append("      TM_VHCL_MATERIAL_GROUP T2,   \n");
		sql.append("      TM_VHCL_MATERIAL_GROUP T3,   \n");
		sql.append("      TM_VHCL_MATERIAL_GROUP_R R,  \n");
		sql.append("      TM_VHCL_MATERIAL TVM--物料表    \n");
		sql.append("WHERE                              \n");
		sql.append("       T.PARENT_GROUP_ID = T2.GROUP_ID      \n");
		sql.append("       AND T2.PARENT_GROUP_ID = T3.GROUP_ID \n");
		sql.append("       AND T.GROUP_ID = R.GROUP_ID          \n");
		sql.append("       AND R.MATERIAL_ID = TVM.MATERIAL_ID  \n");
		List<Map<String,Object>> list = dao.pageQuery(sql.toString(), null,getFunName());
		return list;
	}
	
	/**
	 * 获取当天的提车情况
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String,Object>> getOrderDetail() throws Exception{
		StringBuffer sbSql = new StringBuffer("");
//		sbSql.append("SELECT DISTINCT TVO.DEALER_ID,                   \n");
//		sbSql.append("                TD.DEALER_NAME,                  \n");
//		sbSql.append("                TD.DEALER_CODE,                  \n");
//		sbSql.append("                TSO.ORG_ID,                      \n");
//		sbSql.append("                TSO.ORG_NAME REGION_NAME,        \n");
//		sbSql.append("                T3.GROUP_ID       SERIES_ID,     \n");
//		sbSql.append("                T3.GROUP_NAME     SERIES_NAME,   \n");
//		sbSql.append("                NVL(TVOD.ORDER_AMOUNT,0) TODAY_BILL_AMOUNT, \n");
//		sbSql.append("       to_char(sysdate-4,'yyyy') NYEAR,          \n");
//		sbSql.append("       to_char(sysdate-4,'MM') NMONTH,           \n");
//		sbSql.append("       to_char(sysdate-4,'DD') NDAY              \n");
//		sbSql.append("  FROM TM_VHCL_MATERIAL_GROUP   T,               \n");
//		sbSql.append("       TM_VHCL_MATERIAL_GROUP   T2,              \n");
//		sbSql.append("       TM_VHCL_MATERIAL_GROUP   T3,              \n");
//		sbSql.append("       TM_VHCL_MATERIAL_GROUP_R R,               \n");
//		sbSql.append("       TM_VHCL_MATERIAL         TVM,             \n");
//		sbSql.append("       TT_VS_ORDER              TVO,             \n");
//		sbSql.append("       TT_VS_ORDER_DETAIL       TVOD,            \n");
//		sbSql.append("       TT_VS_ORDER_CHECK        TVOC,            \n");
//		sbSql.append("       TM_DEALER                TD,              \n");
//		sbSql.append("       TM_ORG                   TSO,             \n");
//		sbSql.append("       TM_DEALER_ORG_RELATION   TDO              \n");
//		sbSql.append(" WHERE T.PARENT_GROUP_ID = T2.GROUP_ID           \n");
//		sbSql.append("   AND T2.PARENT_GROUP_ID = T3.GROUP_ID          \n");
//		sbSql.append("   AND T.GROUP_ID = R.GROUP_ID                   \n");
//		sbSql.append("   AND R.MATERIAL_ID = TVM.MATERIAL_ID           \n");
//		sbSql.append("   AND TD.DEALER_ID = TVO.DEALER_ID              \n");
//		sbSql.append("   AND TVOD.MATERIAL_ID = TVM.MATERIAL_ID        \n");
//		sbSql.append("   AND TVO.ORDER_ID = TVOD.ORDER_ID              \n");
//		sbSql.append("   AND TVO.ORDER_ID = TVOC.ORDER_ID              \n");
//		sbSql.append("   AND TSO.ORG_ID = TDO.ORG_ID                   \n");
//		sbSql.append("   AND TDO.DEALER_ID = TVO.DEALER_ID             \n");
//		sbSql.append("   AND TVO.ORDER_STATUS IN                       \n");
//		sbSql.append("       ("+Constant.ORDER_STATUS_07+", "+Constant.ORDER_STATUS_08+", "+Constant.ORDER_STATUS_09+", "+Constant.ORDER_STATUS_11+")\n");
//		sbSql.append("   AND TVOC.CHECK_STATUS IN ("+Constant.FLEET_SUPPORT_CHECK_STATUS_01+", "+Constant.FLEET_SUPPORT_CHECK_STATUS_03+") \n");
//		sbSql.append("   AND TO_CHAR(TVOC.UPDATE_DATE, 'YYYY-MM-DD') = TO_CHAR(SYSDATE - 2, 'YYYY-MM-DD')                                  \n");
		
		/** --------  wangsw修改  -------- */
		sbSql.append("SELECT TVO.DEALER_ID,\n");
		sbSql.append("       TD.DEALER_NAME,\n");
		sbSql.append("       TD.DEALER_CODE,\n");
		sbSql.append("       TORG.ORG_NAME REGION_NAME,\n");
		sbSql.append("       VMG.SERIES_ID,\n");
		sbSql.append("       VMG.SERIES_NAME,\n");
		sbSql.append("       SUM(NVL(TVOD.ORDER_AMOUNT, 0)) TODAY_BILL_AMOUNT,\n");
		sbSql.append("       TO_CHAR(SYSDATE, 'YYYY') NYEAR,\n");
		sbSql.append("       TO_CHAR(SYSDATE, 'MM') NMONTH,\n");
		sbSql.append("       TO_CHAR(SYSDATE, 'DD') NDAY\n");
		sbSql.append("  FROM TT_VS_ORDER            TVO,\n");
		sbSql.append("       TT_VS_ORDER_DETAIL     TVOD,\n");
		sbSql.append("       TM_DEALER              TD,\n");
		sbSql.append("       TM_ORG                 TORG,\n");
		sbSql.append("       TM_DEALER_ORG_RELATION TDOR,\n");
		sbSql.append("       VW_MATERIAL_GROUP_MAT  VMG\n");
		sbSql.append(" WHERE TVO.ORDER_ID = TVOD.ORDER_ID\n");
		sbSql.append("   AND TVOD.MATERIAL_ID = VMG.MATERIAL_ID\n");
		sbSql.append("   AND TVO.DEALER_ID = TD.DEALER_ID\n");
		sbSql.append("   AND TD.DEALER_ID = TDOR.DEALER_ID\n");
		sbSql.append("   AND TDOR.ORG_ID = TORG.ORG_ID\n");
		sbSql.append("   AND TVO.ORDER_STATUS IN ("+Constant.ORDER_STATUS_07+", "+Constant.ORDER_STATUS_08+", "+Constant.ORDER_STATUS_09+", "+Constant.ORDER_STATUS_11+")\r\n");
		sbSql.append("   AND TO_CHAR(TVO.INVO_DATE, 'YYYY-MM-DD') = TO_CHAR(SYSDATE, 'YYYY-MM-DD')\n");
		sbSql.append(" GROUP BY TVO.DEALER_ID,\n");
		sbSql.append("          TD.DEALER_NAME,\n");
		sbSql.append("          TD.DEALER_CODE,\n");
		sbSql.append("          TORG.ORG_NAME,\n");
		sbSql.append("          VMG.SERIES_ID,\n");
		sbSql.append("          VMG.SERIES_NAME"); 
		
		return dao.pageQuery(sbSql.toString(), null,getFunName());
	}
	
	/**
	 * 获取前日的车辆销售情况
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String,Object>> getCarSalesDetail(){
		StringBuffer sbSql = new StringBuffer("");
		sbSql.append("SELECT TDAS.DEALER_ID,                  \n");
		sbSql.append("       TD.DEALER_NAME,                  \n");
		sbSql.append("       TD.DEALER_CODE,                  \n");
		sbSql.append("       TSO.ORG_ID,                      \n");
		sbSql.append("       TSO.ORG_NAME REGION_NAME,        \n");
		sbSql.append("       TV.MATERIAL_ID,                  \n");
		sbSql.append("       TV.SERIES_ID,                    \n");
		sbSql.append("       T.GROUP_NAME SERIES_NAME,        \n");
		sbSql.append("       NVL(COUNT(*),0)  TODAY_SALES_AMOUNT,  \n");
		sbSql.append("       to_char(sysdate-4,'yyyy') NYEAR, \n");
		sbSql.append("       to_char(sysdate-4,'MM') NMONTH,  \n");
		sbSql.append("       to_char(sysdate-4,'DD') NDAY     \n");
		sbSql.append("FROM                                    \n");
		sbSql.append("       TT_DEALER_ACTUAL_SALES TDAS,     \n");
		sbSql.append("       TM_VEHICLE   TV,                 \n");
		sbSql.append("       TM_DEALER    TD,                 \n");
		sbSql.append("       TM_VHCL_MATERIAL_GROUP T,        \n");
		sbSql.append("       TM_ORG       TSO,                \n");
		sbSql.append("       TM_DEALER_ORG_RELATION TDO       \n");
		sbSql.append("WHERE                                   \n");
		sbSql.append("          TDAS.VEHICLE_ID=TV.VEHICLE_ID \n");
		sbSql.append("      AND TD.DEALER_ID = TDAS.DEALER_ID \n");
		sbSql.append("      AND TSO.ORG_ID = TDO.ORG_ID       \n");
		sbSql.append("      AND T.GROUP_ID = TV.SERIES_ID     \n");
		sbSql.append("      AND TDO.DEALER_ID = TDAS.DEALER_ID \n");
		sbSql.append("      AND TDAS.IS_DEL=0                 \n");
		sbSql.append("      AND TO_CHAR(TDAS.SALES_DATE,'YYYY-MM-DD')=TO_CHAR(SYSDATE-2,'YYYY-MM-DD') \n");
		sbSql.append("GROUP  BY TDAS.DEALER_ID,               \n");
		sbSql.append("       TD.DEALER_NAME,                  \n");
		sbSql.append("       TD.DEALER_CODE,                  \n");
		sbSql.append("       TSO.ORG_ID,                      \n");
		sbSql.append("       TSO.ORG_NAME,                    \n");
		sbSql.append("       TV.MATERIAL_ID,                  \n");
		sbSql.append("       TV.SERIES_ID,                    \n");
		sbSql.append("       T.GROUP_NAME                     \n");

		List<Map<String,Object>> list = dao.pageQuery(sbSql.toString(), null,getFunName());
		return list;
	}
	
	/**
	 * 获取销售计划
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String,Object>> getSalesPlanDetail(){
        StringBuffer sbSql = new StringBuffer("");
        sbSql.append("SELECT                                  \n");
        sbSql.append("       A.DEALER_ID,                     \n");
        sbSql.append("       TDV.DEALER_NAME,                 \n");
        sbSql.append("       TDV.DEALER_CODE,                 \n");
        sbSql.append("       TSO.ORG_ID,                      \n");
        sbSql.append("       TSO.ORG_NAME REGION_NAME,        \n");
        sbSql.append("       B.MATERIAL_GROUPID SERIES_ID,    \n");
        sbSql.append("       C.GROUP_NAME SERIES_NAME,        \n");
        sbSql.append("       B.SALE_AMOUNT MONTH_BILL_AMOUNT1,\n");
        sbSql.append("       to_char(sysdate-1,'yyyy') NYEAR, \n");
		sbSql.append("       to_char(sysdate-1,'MM') NMONTH,  \n");
		sbSql.append("       to_char(sysdate-1,'DD') NDAY     \n");
        sbSql.append("FROM   TT_VS_MONTHLY_PLAN  A,           \n");
        sbSql.append("       TT_VS_MONTHLY_PLAN_DETAIL B,     \n");
        sbSql.append("       TM_VHCL_MATERIAL_GROUP C,        \n");
        sbSql.append("       TM_ORG TSO,                      \n");
        sbSql.append("       TM_DEALER_ORG_RELATION TDO,      \n");
        sbSql.append("       TM_DEALER TDV                    \n");
        sbSql.append("WHERE  A.PLAN_ID=B.PLAN_ID              \n");
        sbSql.append("      AND B.MATERIAL_GROUPID=C.GROUP_ID \n");
        sbSql.append("      AND TSO.ORG_ID = TDO.ORG_ID       \n");
        sbSql.append("      AND TDO.DEALER_ID = TDV.DEALER_ID \n");
        sbSql.append("      AND TDV.DEALER_ID = A.DEALER_ID   \n");
        sbSql.append("      AND A.STATUS=" +Constant.PLAN_MANAGE_CONFIRM +"  \n");
        sbSql.append("      AND A.PLAN_YEAR=to_char(sysdate-1,'yyyy')   \n");
        sbSql.append("      AND A.PLAN_MONTH=to_char(sysdate-1,'MM')    \n");
		List<Map<String,Object>> list = dao.pageQuery(sbSql.toString(), null,getFunName());
		return list;
	}
	
	/**
	 * 根据订单情况向销售看板表插入数据
	 * @param list
	 */
	public void insertIntoBaseBoardByOrder(List<Map<String,Object>> list){
		for (int i = 0; i < list.size(); i++) {
			StringBuffer sbSql = new StringBuffer("");
			sbSql.append("INSERT INTO TT_VS_DASHBOARD(         \n");
			sbSql.append("    SERIES_ID,                       \n");
			sbSql.append("    SERIES_NAME,                     \n");
			sbSql.append("    ORG_ID,                          \n");
			sbSql.append("    REGION_NAME,                     \n");
			sbSql.append("    DEALER_CODE,                     \n");
			sbSql.append("    DEALER_NAME,                     \n");
			sbSql.append("    TODAY_BILL_AMOUNT,               \n");
			sbSql.append("    TODAY_SALES_AMOUNT,              \n");
			sbSql.append("    MONTH_SALES_AMOUNT,              \n");
			sbSql.append("    YEAR_SALES_AMOUNT,               \n");
			sbSql.append("    NYEAR,                           \n");
			sbSql.append("    NMONTH,                          \n");
			sbSql.append("    NDAY                             \n");
			sbSql.append("    )                                \n");
			sbSql.append("    VALUES(                          \n");
			sbSql.append("    "+list.get(i).get("SERIES_ID").toString()+",      \n");
			sbSql.append("    '"+list.get(i).get("SERIES_NAME").toString()+"',  \n");
			sbSql.append("    "+list.get(i).get("ORG_ID").toString()+",         \n");
			sbSql.append("    '"+list.get(i).get("REGION_NAME").toString()+"',  \n");
			sbSql.append("    '"+list.get(i).get("DEALER_CODE").toString()+"',  \n");
			sbSql.append("    '"+list.get(i).get("DEALER_NAME").toString()+"',  \n");
			sbSql.append("    "+list.get(i).get("TODAY_BILL_AMOUNT").toString()+",  \n");
			sbSql.append("    0,                                                 \n");
			sbSql.append("    0,                                                 \n");
			sbSql.append("    0,                                                 \n");
			sbSql.append("    to_char(sysdate-4,'yyyy'),                         \n");
			sbSql.append("    to_char(sysdate-4,'MM'),                           \n");
			sbSql.append("    to_char(sysdate-4,'DD')                            \n");
			sbSql.append("    )                                                  \n");
			dao.insert(sbSql.toString());
		}
	} 
	
	/**
	 * 根据车辆销售情况向销售看板表插入数据
	 * @param list
	 */
	public void insertIntoBaseBoardByCarSales(Map<String,Object> map){
			StringBuffer sbSql = new StringBuffer("");
			sbSql.append("INSERT INTO TT_VS_DASHBOARD(         \n");
			sbSql.append("    SERIES_ID,                       \n");
			sbSql.append("    SERIES_NAME,                     \n");
			sbSql.append("    ORG_ID,                          \n");
			sbSql.append("    REGION_NAME,                     \n");
			sbSql.append("    DEALER_CODE,                     \n");
			sbSql.append("    DEALER_NAME,                     \n");
			sbSql.append("    TODAY_SALES_AMOUNT,              \n");
			sbSql.append("    MONTH_SALES_AMOUNT,              \n");
			sbSql.append("    YEAR_SALES_AMOUNT,               \n");
			sbSql.append("    TODAY_BILL_AMOUNT,               \n");
			sbSql.append("    MONTH_BILL_AMOUNT,               \n");
			sbSql.append("    YEAR_BILL_AMOUNT,                \n");
			sbSql.append("    NYEAR,                           \n");
			sbSql.append("    NMONTH,                          \n");
			sbSql.append("    NDAY                             \n");
			sbSql.append("    )                                \n");
			sbSql.append("    VALUES(                          \n");
			sbSql.append("    "+map.get("SERIES_ID").toString()+",      \n");
			sbSql.append("    '"+map.get("SERIES_NAME").toString()+"',  \n");
			sbSql.append("    "+map.get("ORG_ID").toString()+",         \n");
			sbSql.append("    '"+map.get("REGION_NAME").toString()+"',  \n");
			sbSql.append("    '"+map.get("DEALER_CODE").toString()+"',  \n");
			sbSql.append("    '"+map.get("DEALER_NAME").toString()+"',  \n");
			sbSql.append("    "+map.get("TODAY_SALES_AMOUNT").toString()+",    \n");
			sbSql.append("    "+map.get("TODAY_SALES_AMOUNT").toString()+",    \n");
			sbSql.append("    "+map.get("TODAY_SALES_AMOUNT").toString()+",    \n");
			sbSql.append("    0,                                        \n");
			sbSql.append("    0,                                        \n");
			sbSql.append("    0,                                        \n");
			sbSql.append("    to_char(sysdate-4,'yyyy'),                \n");
			sbSql.append("    to_char(sysdate-4,'MM'),                  \n");
			sbSql.append("    to_char(sysdate-4,'DD')                   \n");
			sbSql.append("    )                                         \n");
			dao.insert(sbSql.toString());
	} 
	/**
	 * 根据销售计划向销售看板表插入数据
	 * @param list
	 */
	public void insertIntoBaseBoardByPlan(Map<String,Object> map){
			StringBuffer sbSql = new StringBuffer("");
			sbSql.append("INSERT INTO TT_VS_DASHBOARD(         \n");
			sbSql.append("    SERIES_ID,                       \n");
			sbSql.append("    SERIES_NAME,                     \n");
			sbSql.append("    ORG_ID,                          \n");
			sbSql.append("    REGION_NAME,                     \n");
			sbSql.append("    DEALER_CODE,                     \n");
			sbSql.append("    DEALER_NAME,                     \n");
			sbSql.append("    MONTH_BILL_AMOUNT1,              \n");
			sbSql.append("    MONTH_SALES_AMOUNT1,             \n");
			sbSql.append("    TODAY_BILL_AMOUNT,               \n");
			sbSql.append("    MONTH_BILL_AMOUNT,               \n");
			sbSql.append("    YEAR_BILL_AMOUNT,                \n");
			sbSql.append("    TODAY_SALES_AMOUNT,              \n");
			sbSql.append("    MONTH_SALES_AMOUNT,              \n");
			sbSql.append("    YEAR_SALES_AMOUNT,               \n");
			sbSql.append("    NYEAR,                           \n");
			sbSql.append("    NMONTH,                          \n");
			sbSql.append("    NDAY                             \n");
			sbSql.append("    )                                \n");
			sbSql.append("    VALUES(                          \n");
			sbSql.append("    "+map.get("SERIES_ID").toString()+",      \n");
			sbSql.append("    '"+map.get("SERIES_NAME").toString()+"',  \n");
			sbSql.append("    "+map.get("ORG_ID").toString()+",         \n");
			sbSql.append("    '"+map.get("REGION_NAME").toString()+"',  \n");
			sbSql.append("    '"+map.get("DEALER_CODE").toString()+"',  \n");
			sbSql.append("    '"+map.get("DEALER_NAME").toString()+"',  \n");
			sbSql.append("    "+map.get("MONTH_BILL_AMOUNT1").toString()+",    \n");
			sbSql.append("    "+map.get("MONTH_BILL_AMOUNT1").toString()+",    \n");
			sbSql.append("    0,                                        \n");
			sbSql.append("    0,                                        \n");
			sbSql.append("    0,                                        \n");
			sbSql.append("    0,                                        \n");
			sbSql.append("    0,                                        \n");
			sbSql.append("    0,                                        \n");
			sbSql.append("    to_char(sysdate-1,'yyyy'),                \n");
			sbSql.append("    to_char(sysdate-1,'MM'),                  \n");
			sbSql.append("    to_char(sysdate-1,'DD')                   \n");
			sbSql.append("    )                                         \n");
			dao.insert(sbSql.toString());
	} 
	
	/**
	 * 根据条件查询销售看板表
	 * @param dealerCode
	 * @param seriesid
	 * @param year
	 * @param month
	 * @param day
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String,Object>> selectBaseBoard(String dealerCode,String seriesid,String year,String month,String day){
		StringBuffer sbSql = new StringBuffer("");
		sbSql.append("SELECT A.SERIES_ID,A.DEALER_CODE             \n");
		sbSql.append("FROM    TT_VS_DASHBOARD A                    \n");
		sbSql.append("WHERE   A.DEALER_CODE='"+dealerCode+"'       \n");
		sbSql.append("    AND A.SERIES_ID="+seriesid+"             \n");
		sbSql.append("    AND A.NYEAR="+year+"                     \n");
		sbSql.append("    AND A.NMONTH="+month+"                   \n");
		sbSql.append("    AND A.NDAY="+day+"                       \n");
		List<Map<String,Object>> list = dao.pageQuery(sbSql.toString(), null, getFunName());
		return list;
	}
	
	/**
	 * 更新月份提车、销售情况
	 */
	@SuppressWarnings("unchecked")
	public void updateMonthData(){
		StringBuffer sbSql = new StringBuffer("");
		sbSql.append("UPDATE TT_VS_DASHBOARD B                                                            \n");
		sbSql.append("SET B.MONTH_BILL_AMOUNT= (SELECT  SUM(A.TODAY_BILL_AMOUNT) MONTH_BILL_AMOUNT        \n");
		sbSql.append("                          FROM   TT_VS_DASHBOARD A                                  \n");
		sbSql.append("                          WHERE  A.NMONTH=to_char(sysdate-1,'MM')                   \n");
		sbSql.append("                          AND A.NYEAR=to_char(sysdate-1,'yyyy')                     \n");
		sbSql.append("                          AND A.DEALER_CODE=B.DEALER_CODE                           \n");
		sbSql.append("                          AND A.SERIES_ID=B.SERIES_ID                               \n");
		sbSql.append("                          AND A.NYEAR=B.NYEAR                                       \n");
		sbSql.append("                          AND A.NMONTH=B.NMONTH                                     \n");
		sbSql.append("                          GROUP BY A.DEALER_CODE,A.SERIES_ID,A.NYEAR,A.NMONTH       \n");
		sbSql.append("                          ) ,                                                       \n");
		sbSql.append("   B.MONTH_SALES_AMOUNT= (SELECT  SUM(A.TODAY_SALES_AMOUNT) MONTH_SALES_AMOUNT      \n");
		sbSql.append("                          FROM   TT_VS_DASHBOARD A                                  \n");
		sbSql.append("                          WHERE  A.NMONTH=to_char(sysdate-1,'MM')                   \n");
		sbSql.append("                          AND A.NYEAR=to_char(sysdate-1,'yyyy')                     \n");
		sbSql.append("                          AND A.DEALER_CODE=B.DEALER_CODE                           \n");
		sbSql.append("                          AND A.SERIES_ID=B.SERIES_ID                               \n");
		sbSql.append("                          AND A.NYEAR=B.NYEAR                                       \n");
		sbSql.append("                          AND A.NMONTH=B.NMONTH                                     \n");
		sbSql.append("                          GROUP BY A.DEALER_CODE,A.SERIES_ID,A.NYEAR,A.NMONTH       \n");
		sbSql.append("                          )                                                         \n"); 
		sbSql.append("WHERE B.NYEAR = to_char(sysdate-1,'yyyy')                                           \n"); 
		sbSql.append("      AND B.NMONTH = to_char(sysdate-1,'MM')                                        \n"); 
		dao.update(sbSql.toString(), null);

	}
	
	/**
	 * 更新年提车、售车数据
	 */
	@SuppressWarnings("unchecked")
	public void updateYearData(){
		StringBuffer sbSql = new StringBuffer("");
		sbSql.append("UPDATE TT_VS_DASHBOARD B                                                    \n");
		sbSql.append("SET B.YEAR_BILL_AMOUNT= (SELECT  SUM(A.TODAY_BILL_AMOUNT) YEAR_BILL_AMOUNT  \n");
		sbSql.append("                          FROM   TT_VS_DASHBOARD A                          \n");
		sbSql.append("                          WHERE                                             \n");
		sbSql.append("                              A.NYEAR=to_char(sysdate-1,'yyyy')             \n");
		sbSql.append("                          AND A.DEALER_CODE=B.DEALER_CODE                   \n");
		sbSql.append("                          AND A.SERIES_ID=B.SERIES_ID                       \n");
		sbSql.append("                          AND A.NYEAR=B.NYEAR                               \n");
		sbSql.append("                          GROUP BY A.DEALER_CODE,A.SERIES_ID,A.NYEAR        \n");
		sbSql.append("                          ) ,                                               \n");
		sbSql.append("   B.YEAR_SALES_AMOUNT= (SELECT  SUM(A.TODAY_SALES_AMOUNT) YEAR_SALES_AMOUNT\n");
		sbSql.append("                          FROM   TT_VS_DASHBOARD A                          \n");
		sbSql.append("                          WHERE                                             \n");
		sbSql.append("                              A.NYEAR=to_char(sysdate-1,'yyyy')             \n");
		sbSql.append("                          AND A.DEALER_CODE=B.DEALER_CODE                   \n");
		sbSql.append("                          AND A.SERIES_ID=B.SERIES_ID                       \n");
		sbSql.append("                          AND A.NYEAR=B.NYEAR                               \n");
		sbSql.append("                          GROUP BY A.DEALER_CODE,A.SERIES_ID,A.NYEAR        \n");
		sbSql.append("                          )                                                 \n"); 
		sbSql.append("WHERE B.NYEAR = to_char(sysdate-1,'yyyy')                                   \n");
		dao.update(sbSql.toString(), null);
	}
	
	/**
	 * 更新销售看板表车辆销售数据
	 * @param list
	 */
	@SuppressWarnings("unchecked")
	public void updateBaseBoardBySales(String dealerCode,String seriesId,String year,String month,String day,String todaySalesAmount){
			StringBuffer sbSql = new StringBuffer("");
			sbSql.append("UPDATE  TT_VS_DASHBOARD A SET                \n");
			sbSql.append("    A.TODAY_SALES_AMOUNT="+todaySalesAmount+",\n");
			sbSql.append("    A.MONTH_SALES_AMOUNT="+todaySalesAmount+",\n");
			sbSql.append("    A.YEAR_SALES_AMOUNT="+todaySalesAmount+"  \n");
			sbSql.append("WHERE                                        \n");
			sbSql.append("        A.DEALER_CODE='"+dealerCode+"'       \n");
			sbSql.append("    AND A.SERIES_ID="+seriesId+"             \n");
			sbSql.append("    AND A.NYEAR="+year+"                     \n");
			sbSql.append("    AND A.NMONTH="+month+"                   \n");
			sbSql.append("    AND A.NDAY="+day+"                       \n");
			dao.update(sbSql.toString(), null);
	}
	
	/**
	 * 更新销售看板表销售计划数据
	 * @param list
	 */
	@SuppressWarnings("unchecked")
	public void updateBaseBoardByPlan(String dealerCode,String seriesId,String year,String month,String day,String planAmount){
			StringBuffer sbSql = new StringBuffer("");
			sbSql.append("UPDATE  TT_VS_DASHBOARD A SET                \n");
			sbSql.append("    A.MONTH_BILL_AMOUNT1="+planAmount+",     \n");
			sbSql.append("    A.MONTH_SALES_AMOUNT1="+planAmount+"     \n");
			sbSql.append("WHERE                                        \n");
			sbSql.append("        A.DEALER_CODE='"+dealerCode+"'       \n");
			sbSql.append("    AND A.SERIES_ID="+seriesId+"             \n");
			sbSql.append("    AND A.NYEAR="+year+"                     \n");
			sbSql.append("    AND A.NMONTH="+month+"                   \n");
			sbSql.append("    AND A.NDAY="+day+"                       \n");
			dao.update(sbSql.toString(), null);
	}
	
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 获取当天的启票数量和实销数量
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> getTodayInvoAndSales() throws Exception {
		
		StringBuffer sbSql = new StringBuffer();
		
		sbSql.append("WITH INVO_DATA AS (\n");
		sbSql.append("-- 当日启票数量\n");
		sbSql.append("SELECT TVO.DEALER_ID,\n");
		sbSql.append("       TD.DEALER_NAME,\n");
		sbSql.append("       TD.DEALER_CODE,\n");
		sbSql.append("       TORG.ORG_NAME REGION_NAME,\n");
		sbSql.append("       VMG.SERIES_ID,\n");
		sbSql.append("       VMG.SERIES_NAME,\n");
		sbSql.append("       SUM(NVL(TVOD.ORDER_AMOUNT, 0)) TODAY_BILL_AMOUNT,\n");
		sbSql.append("       TO_CHAR(SYSDATE, 'YYYY') NYEAR,\n");
		sbSql.append("       TO_CHAR(SYSDATE, 'MM') NMONTH,\n");
		sbSql.append("       TO_CHAR(SYSDATE, 'DD') NDAY\n");
		sbSql.append("  FROM TT_VS_ORDER            TVO,\n");
		sbSql.append("       TT_VS_ORDER_DETAIL     TVOD,\n");
		sbSql.append("       TM_DEALER              TD,\n");
		sbSql.append("       TM_ORG                 TORG,\n");
		sbSql.append("       TM_DEALER_ORG_RELATION TDOR,\n");
		sbSql.append("       VW_MATERIAL_GROUP_MAT  VMG\n");
		sbSql.append(" WHERE TVO.ORDER_ID = TVOD.ORDER_ID\n");
		sbSql.append("   AND TVOD.MATERIAL_ID = VMG.MATERIAL_ID\n");
		sbSql.append("   AND TVO.DEALER_ID = TD.DEALER_ID\n");
		sbSql.append("   AND TD.DEALER_ID = TDOR.DEALER_ID\n");
		sbSql.append("   AND TDOR.ORG_ID = TORG.ORG_ID\n");
		sbSql.append("   AND TVO.ORDER_STATUS IN ("+Constant.ORDER_STATUS_07+", "+Constant.ORDER_STATUS_08+", "+Constant.ORDER_STATUS_09+", "+Constant.ORDER_STATUS_11+")\n");
		sbSql.append("   AND TO_CHAR(TVO.INVO_DATE, 'YYYY-MM-DD') = TO_CHAR(SYSDATE, 'YYYY-MM-DD')\n");
		sbSql.append(" GROUP BY TVO.DEALER_ID,\n");
		sbSql.append("          TD.DEALER_NAME,\n");
		sbSql.append("          TD.DEALER_CODE,\n");
		sbSql.append("          TORG.ORG_NAME,\n");
		sbSql.append("          VMG.SERIES_ID,\n");
		sbSql.append("          VMG.SERIES_NAME\n");
		sbSql.append("),\n");
		sbSql.append("SALES_DATA AS (\n");
		sbSql.append("-- 当日实销数量\n");
		sbSql.append("SELECT TDAS.DEALER_ID,\n");
		sbSql.append("       TD.DEALER_NAME,\n");
		sbSql.append("       TD.DEALER_CODE,\n");
		sbSql.append("       TORG.ORG_NAME REGION_NAME,\n");
		sbSql.append("       VMG.SERIES_ID,\n");
		sbSql.append("       VMG.SERIES_NAME,\n");
		sbSql.append("       COUNT(*) TODAY_SALES_AMOUNT,\n");
		sbSql.append("       TO_CHAR(sysdate, 'yyyy') NYEAR,\n");
		sbSql.append("       TO_CHAR(sysdate, 'MM') NMONTH,\n");
		sbSql.append("       TO_CHAR(sysdate, 'DD') NDAY\n");
		sbSql.append("  FROM TT_DEALER_ACTUAL_SALES TDAS,\n");
		sbSql.append("       TM_VEHICLE             TV,\n");
		sbSql.append("       TM_DEALER              TD,\n");
		sbSql.append("       TM_ORG                 TORG,\n");
		sbSql.append("       TM_DEALER_ORG_RELATION TDOR,\n");
		sbSql.append("       VW_MATERIAL_GROUP_MAT VMG\n");
		sbSql.append(" WHERE TDAS.VEHICLE_ID = TV.VEHICLE_ID\n");
		sbSql.append("   AND TD.DEALER_ID = TDAS.DEALER_ID\n");
		sbSql.append("   AND TDAS.DEALER_ID = TDOR.DEALER_ID\n");
		sbSql.append("   AND TDOR.ORG_ID = TORG.ORG_ID\n");
		sbSql.append("   AND TV.SERIES_ID = VMG.SERIES_ID\n");
		sbSql.append("   AND TO_CHAR(TDAS.INVOICE_DATE,'YYYY-MM-DD') = TO_CHAR(SYSDATE,'YYYY-MM-DD')\n");
		sbSql.append("   AND TDAS.IS_DEL = 0\n");
		sbSql.append(" GROUP BY TDAS.DEALER_ID,\n");
		sbSql.append("       TD.DEALER_NAME,\n");
		sbSql.append("       TD.DEALER_CODE,\n");
		sbSql.append("       TORG.ORG_NAME,\n");
		sbSql.append("       VMG.SERIES_ID,\n");
		sbSql.append("       VMG.SERIES_NAME\n");
		sbSql.append(")\n");
		sbSql.append("SELECT A.DEALER_ID,\n");
		sbSql.append("       A.DEALER_NAME,\n");
		sbSql.append("       A.DEALER_CODE,\n");
		sbSql.append("       A.REGION_NAME,\n");
		sbSql.append("       A.SERIES_ID,\n");
		sbSql.append("       A.SERIES_NAME,\n");
		sbSql.append("       A.TODAY_BILL_AMOUNT,\n");
		sbSql.append("       B.TODAY_SALES_AMOUNT,\n");
		sbSql.append("       A.NYEAR,\n");
		sbSql.append("       A.NMONTH,\n");
		sbSql.append("       A.NDAY\n");
		sbSql.append("  FROM INVO_DATA A\n");
		sbSql.append("  FULL JOIN SALES_DATA B\n");
		sbSql.append("    ON A.DEALER_ID = B.DEALER_ID\n");
		sbSql.append("   AND A.SERIES_ID = B.SERIES_ID"); 
		
		return dao.pageQuery(sbSql.toString(), null, getFunName());
	}

	/**
	 * 将当日的启票数据和实销数据插入到销售看板数据表
	 * @param list
	 * @throws Exception
	 */
	public void insertIntoBaseBoard(List<Map<String, Object>> list) throws Exception{
		
		for (int i = 0; i < list.size(); i++) 
		{
			StringBuffer sbSql = new StringBuffer();
			String dealerCode = CommonUtils.checkNull(list.get(i).get("DEALER_CODE"));
			String seriesId = CommonUtils.checkNull(list.get(i).get("SERIES_ID"));
			String year = CommonUtils.checkNull(list.get(i).get("NYEAR"));
			String month = CommonUtils.checkNull(list.get(i).get("NMONTH"));
			String day = CommonUtils.checkNull(list.get(i).get("NDAY"));
			sbSql.append("SELECT * FROM TT_VS_DASHBOARD WHERE SERIES_ID = " + seriesId + " AND DEALER_CODE = '"+dealerCode+"' AND NYEAR = " + year + " AND NMONTH = " + month +" AND NDAY = " + day);
			
			List sl = dao.pageQuery(sbSql.toString(), null, getFunName());
			
			if(sl.size() == 0) {
				sbSql.append("INSERT INTO TT_VS_DASHBOARD(         \n");
				sbSql.append("    SERIES_ID,                       \n");
				sbSql.append("    SERIES_NAME,                     \n");
				sbSql.append("    ORG_ID,                          \n");
				sbSql.append("    REGION_NAME,                     \n");
				sbSql.append("    DEALER_CODE,                     \n");
				sbSql.append("    DEALER_NAME,                     \n");
				sbSql.append("    TODAY_BILL_AMOUNT,               \n");
				sbSql.append("    TODAY_SALES_AMOUNT,              \n");
				sbSql.append("    NYEAR,                           \n");
				sbSql.append("    NMONTH,                          \n");
				sbSql.append("    NDAY                             \n");
				sbSql.append("    )                                \n");
				sbSql.append("    VALUES						   \n");
				sbSql.append("	  (								   \n");
				sbSql.append(CommonUtils.checkNull(list.get(i).get("SERIES_ID")) + ",");
				sbSql.append(CommonUtils.checkNull(list.get(i).get("SERIES_NAME")) + ",");
				sbSql.append(CommonUtils.checkNull(list.get(i).get("ORG_ID")) + ",");
				sbSql.append(CommonUtils.checkNull(list.get(i).get("REGION_NAME")) + ",");
				sbSql.append(CommonUtils.checkNull(list.get(i).get("DEALER_CODE")) + ",");
				sbSql.append(CommonUtils.checkNull(list.get(i).get("DEALER_NAME")) + ",");
				sbSql.append(CommonUtils.checkNull(list.get(i).get("TODAY_BILL_AMOUNT")) + ",");
				sbSql.append(CommonUtils.checkNull(list.get(i).get("TODAY_SALES_AMOUNT")));
				sbSql.append(CommonUtils.checkNull(list.get(i).get("NYEAR")) + ",");
				sbSql.append(CommonUtils.checkNull(list.get(i).get("NMONTH")) + ",");
				sbSql.append(CommonUtils.checkNull(list.get(i).get("NDAY")));
				sbSql.append("	  )");
				
				dao.insert(sbSql.toString());
			}
		}
		
	}

}
