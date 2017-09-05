package com.infodms.dms.dao.report.serviceReport;

import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.common.Constant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.dao.common.BaseDao;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

public class DealerFinFDetailDao extends BaseDao
{
	public static final Logger logger = Logger.getLogger(DealerFinFDetailDao.class);
	public static final DealerFinFDetailDao dao = new DealerFinFDetailDao();
	public static final DealerFinFDetailDao getInstance(){
		return dao;
	}
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}
	public PageResult<Map<String, Object>> exportqueryOemDealer(Map<String,String> map,int pageSize,int curPage) {
		StringBuffer sqlStr=new StringBuffer();
		List params = new LinkedList();
		sqlStr.append("select\n" );
		sqlStr.append("       t.name,\n" );
		sqlStr.append("       tba.area_name,\n" );
		sqlStr.append("       a.DEALER_CODE,\n" );
		sqlStr.append("       A.DEALER_NAME,\n" );
		sqlStr.append("       to_char(b.fine_date, 'yyyy-MM-dd') fine_date,\n" );
		sqlStr.append("       b.FINE_TYPE,\n" );
		sqlStr.append("       b.datum_sum,\n" );
		sqlStr.append("       b.labour_sum,\n" );
		sqlStr.append("       b.REMARK,\n" );
		sqlStr.append("       b.PAY_STATUS,\n" );
		sqlStr.append("       b.labour_bh,\n" );
		sqlStr.append("       b.balance_oder,\n" );
		sqlStr.append("       b.FINE_REASON\n" );
		sqlStr.append("  from tm_dealer a, TT_AS_WR_FINE b,tc_user t,tm_business_area tba\n" );
		sqlStr.append(" where a.DEALER_ID = b.DEALER_ID\n" );
		sqlStr.append(" and t.user_id=b.create_by\n" );
		sqlStr.append(" and tba.area_id=b.yieldly");
		sqlStr.append(" AND a.STATUS ="+Constant.STATUS_ENABLE+"\n");
		sqlStr.append(" AND a.DEALER_TYPE =");
		sqlStr.append(Constant.DEALER_TYPE_DWR);
		sqlStr.append("\n");

		if (null != map.get("dealerCode") && !"".equals(map.get("dealerCode"))) {
			String[] array = map.get("dealerCode").split(",");
			sqlStr.append("   AND a.DEALER_CODE IN ( \n");
			if(array.length>0){
				for (int i = 0; i < array.length; i++) {
					sqlStr.append("'"+array[i]+"'");
						if (i != array.length - 1) {
							sqlStr.append(",");
						}	
				}
			}else{
				sqlStr.append("''");//放空置，防止in里面报错
			}
			sqlStr.append(")\n");
		}	
		
		if (Utility.testString(map.get("deductStartDate"))) {//提报开始日期
			sqlStr.append("   AND b.fine_date >= TO_DATE('").append(map.get("deductStartDate").trim().concat(" 00:00:00")).append("','YYYY-MM-DD HH24:MI:SS') \n");
		}
		if(Utility.testString(map.get("fine_type")))
		{
			sqlStr.append(" and  b.FINE_TYPE = "+map.get("fine_type"));
		}
		if (Utility.testString(map.get("deductEndDate"))) {//提报结束日期
			sqlStr.append("   AND b.fine_date <= TO_DATE('").append(map.get("deductEndDate").trim().concat(" 23:59:59")).append("','YYYY-MM-DD HH24:MI:SS') \n");
		}
		if (Utility.testString(map.get("dealerName"))){
			sqlStr.append(" and  a.dealer_name like '%"+map.get("dealerName")+"%' ");
		}
		if (Utility.testString(map.get("ButieBh"))){
			sqlStr.append(" and  b.LABOUR_BH like '%"+map.get("ButieBh")+"%' ");
		}
		if (Utility.testString(map.get("SubsidiesType"))){
			sqlStr.append(" and  b.REMARK like '%"+map.get("SubsidiesType")+"%' "); //补贴类型
		}
		sqlStr.append(" ORDER BY a.DEALER_ID DESC ");
		PageResult<Map<String, Object>> ps= pageQuery(sqlStr.toString(), null, getFunName(),pageSize, curPage);
		return ps;
	}
	
	public PageResult<Map<String,Object>> queryDealerReportForms(String dealerCode,String dealerName,String tmorg,
			String tmOrgSmall,String claimType,String feeType,String activityType,String AStartDate,String AEndDate,
			String FStartDate,String FEndDate,int pageSize,int curPage ){
		StringBuffer sql= new StringBuffer();
		if(Utility.testString(claimType))
		{
		sql.append("SELECT DS.ROOT_DEALER_CODE DEALER_CODE, /*服务站代码*/\n" );
		sql.append("       DS.ROOT_DEALER_NAME DEALER_NAME, /*服务站名称*/\n" );
		sql.append("       SUM(BALANCE_AMOUNT) BALANCE_AMOUNT, /*结算总金额*/\n" );
		sql.append("       COUNT(DISTINCT VIN) REPAIR_TOTAL /*维修台数*/\n" );
		sql.append(" FROM (SELECT NVL(A.SECOND_DEALER_ID, A.DEALER_ID) DEALER_ID, A.VIN,  /*结算单*/\n" );
		sql.append("             CASE WHEN a.status IN ("+Constant.CLAIM_APPLY_ORD_TYPE_13+","+Constant.CLAIM_APPLY_ORD_TYPE_16+")\n" );
		if(Utility.testString(FStartDate))
		{
			sql.append("                  THEN CASE WHEN A.DI_DATE BETWEEN TO_DATE('"+FStartDate+"' /*结算上报日期开始*/, 'YYYY-MM-DD')\n" );
		}else
		{
			sql.append("                  THEN CASE WHEN A.DI_DATE BETWEEN TO_DATE('' /*结算上报日期开始*/, 'YYYY-MM-DD')\n" );
		}
		if(Utility.testString(FEndDate))
		{
			sql.append("                             AND TO_DATE('"+FEndDate+" 23:59:59' /*结算上报日期结束*/, 'YYYY-MM-DD HH24:MI:SS')\n" );
		}else
		{
			sql.append("                             AND TO_DATE('' /*结算上报日期结束*/, 'YYYY-MM-DD HH24:MI:SS')\n" );
		}
		
		sql.append("                            THEN NVL(A.BALANCE_AMOUNT,0) - NVL(A.DISCOUNT,0)\n" );
		sql.append("                            ELSE NVL(A.BALANCE_AMOUNT,0) END\n" );
		sql.append("                  ELSE 0 END BALANCE_AMOUNT,\n" );
		sql.append("              A.REPORT_DATE, A.FI_DATE\n" );
		sql.append("        FROM TT_AS_WR_APPLICATION A\n" );
		sql.append("        LEFT JOIN TT_AS_ACTIVITY TA ON A.CAMPAIGN_CODE = TA.ACTIVITY_CODE\n" );
		sql.append("        LEFT JOIN TT_AS_ACTIVITY_SUBJECT TS ON TA.SUBJECT_ID = TS.SUBJECT_ID\n" );
		sql.append("       WHERE A.STATUS >= "+Constant.CLAIM_APPLY_ORD_TYPE_02+"  /*结算上报后的数据*/\n" );
		sql.append("   AND (a.claim_type != 10661006 OR (a.claim_type = 10661006 AND ts.activity_type = 10561001)) ");
		sql.append("         AND A.BALANCE_YIELDLY = "+Constant.PART_IS_CHANGHE_01+" /*仅限昌河*/\n" );
		if(Utility.testString(claimType))
		{
			sql.append("         AND A.CLAIM_TYPE IN ('"+claimType+"')\n" );
		}
		if(Utility.testString(activityType))
		{
			sql.append("         AND TS.ACTIVITY_TYPE IN ('"+activityType+"') \n" );
		}
		if(Utility.testString(feeType)){
					
					//选择了特殊类型过后才会查询特殊单
					
						sql.append("      UNION ALL /*特殊单*/\n" );
						sql.append("      SELECT WS.DEALER_ID, WS.VIN, decode(ws.status,"+Constant.SPEFEE_STATUS_10+",NVL(WS.OFFICE_DECLARE_SUM,0),0) DECLARE_SUM, WS.MAKE_DATE, SA.AUDITING_DATE\n" );
						sql.append("       FROM TT_AS_WR_SPEFEE WS\n" );
						sql.append("        LEFT JOIN   ( SELECT tm.FEE_ID,max(tm.STATUS) STATUS, max(tm.AUDITING_DATE) AUDITING_DATE from   TT_AS_WR_SPEFEE_AUDITING  tm group by tm.FEE_ID   )    SA ON WS.ID = SA.FEE_ID AND SA.STATUS = "+Constant.SPEFEE_STATUS_10+"\n" );
						sql.append("       WHERE WS.FEE_TYPE != "+Constant.SPEFEE_STATUS_02+"\n" );
						sql.append("        AND WS.STATUS >= "+Constant.SPEFEE_STATUS_02+"\n" );
						sql.append("         AND WS.FEE_TYPE IN ('"+feeType+"')\n" );
					
					
				}
		
		sql.append("      UNION ALL /*条码补办*/\n" );
		sql.append("      SELECT BA.APPLY_BY, BA.VIN, decode(ba.apply_status, "+Constant.BARCODE_APPLY_STATUS_04+", DECODE(NVL(BA.AUDIT_ACOUNT, 0),0,0,-1*NVL(BA.AUDIT_ACOUNT, 0)), 0) AUDIT_ACOUNT,\n" );
		sql.append("             BA.REPORT_DATE, BA.AUDIT_DATE\n" );
		sql.append("       FROM TT_AS_BARCODE_APPLY BA\n" );
		sql.append("       WHERE BA.APPLY_STATUS >= "+Constant.BARCODE_APPLY_STATUS_02+"\n" );
		sql.append("      UNION ALL /*三包凭证*/\n" );
		sql.append("      SELECT PA.APPLY_BY, PA.VIN, decode(pa.apply_status, "+Constant.PACKGE_CHANGE_STATUS_06+", DECODE(NVL(PA.AUDIT_ACOUNT,0),0,0,-1*NVL(PA.AUDIT_ACOUNT,0)), 0) AUDIT_ACOUNT,\n" );
		sql.append("             PA.REPORT_DATE, PD.AUDIT_DATE\n" );
		sql.append("       FROM TT_AS_PACKGE_CHANGE_APPLY PA\n" );
		sql.append("        LEFT JOIN TT_AS_PACKGE_CHANGE_DETAIL PD ON PA.ID = PD.APPLY_ID AND PD.AUDIT_STATUS = "+Constant.PACKGE_CHANGE_STATUS_06+"\n" );
		sql.append("       WHERE PA.APPLY_STATUS >= "+Constant.PACKGE_CHANGE_STATUS_02+") Y, VW_ORG_DEALER_SERVICE DS\n" );
		sql.append(" WHERE Y.DEALER_ID = DS.DEALER_ID\n" );
		
		if(Utility.testString(FStartDate))
		{
			sql.append("   AND Y.REPORT_DATE >= TO_DATE('"+FStartDate+"','YYYY-MM-DD')\n" );
		}
		if(Utility.testString(FEndDate))
		{
			sql.append("     AND Y.REPORT_DATE <= TO_DATE('"+FEndDate+" 23:59:59','YYYY-MM-DD HH24:MI:SS')\n" );
		}
		
		if(Utility.testString(AStartDate))
		{
			sql.append("     AND Y.FI_DATE >= TO_DATE('"+AStartDate+"','YYYY-MM-DD')\n" );
		}
		if(Utility.testString(AEndDate))
		{
			sql.append("     AND Y.FI_DATE <= TO_DATE('"+AEndDate+" 23:59:59','YYYY-MM-DD HH24:MI:SS')\n" );
		}
		
		if(Utility.testString(dealerCode))
		{
			sql.append("     AND DS.DEALER_CODE LIKE '%"+dealerCode+"%' \n" );
		}
		if(Utility.testString(dealerName))
		{
			sql.append("     AND DS.DEALER_NAME LIKE '%"+dealerName+"%' \n" );
		}
		
		if(Utility.testString(tmOrgSmall))
		{
			sql.append("     AND DS.ORG_ID = "+tmOrgSmall+" \n" );
		}
		
		if(Utility.testString(tmorg))
		{
			sql.append("     AND DS.ROOT_ORG_ID ="+tmorg+"  \n" );
		}
		
		
		sql.append("  GROUP BY DS.ROOT_DEALER_CODE, DS.ROOT_DEALER_NAME");
		}else if(Utility.testString(feeType)){
			
			//选择了特殊类型过后才会查询特殊单
				sql.append("SELECT DS.ROOT_DEALER_CODE DEALER_CODE, /*服务站代码*/\n" );
				sql.append("       DS.ROOT_DEALER_NAME DEALER_NAME, /*服务站名称*/\n" );
				sql.append("       SUM(DECLARE_SUM) BALANCE_AMOUNT, /*结算总金额*/\n" );
				sql.append("       COUNT(DISTINCT VIN) REPAIR_TOTAL /*维修台数*/\n" );
					sql.append(" FROM ( ");
				sql.append("     /*特殊单*/\n" );
				sql.append("      SELECT WS.DEALER_ID, WS.VIN, decode(ws.status,"+Constant.SPEFEE_STATUS_10+",NVL(WS.OFFICE_DECLARE_SUM,0),0) DECLARE_SUM, WS.MAKE_DATE, SA.AUDITING_DATE\n" );
				sql.append("       FROM TT_AS_WR_SPEFEE WS\n" );
				sql.append("        LEFT JOIN   ( SELECT tm.FEE_ID,max(tm.STATUS) STATUS, max(tm.AUDITING_DATE) AUDITING_DATE from   TT_AS_WR_SPEFEE_AUDITING  tm group by tm.FEE_ID   )    SA ON WS.ID = SA.FEE_ID AND SA.STATUS = "+Constant.SPEFEE_STATUS_10+"\n" );
				sql.append("       WHERE WS.FEE_TYPE != "+Constant.SPEFEE_STATUS_02+"\n" );
				sql.append("        AND WS.STATUS >= "+Constant.SPEFEE_STATUS_02+"\n" );
				sql.append("         AND WS.FEE_TYPE IN ('"+feeType+"')\n" );
				sql.append("   )Y,VW_ORG_DEALER_SERVICE DS\n" );
				sql.append("WHERE Y.DEALER_ID = DS.DEALER_ID\n" );
				if(Utility.testString(dealerCode))
				{
					sql.append("     AND DS.DEALER_CODE LIKE '%"+dealerCode+"%' \n" );
				}
				if(Utility.testString(dealerName))
				{
					sql.append("     AND DS.DEALER_NAME LIKE '%"+dealerName+"%' \n" );
				}
				
				if(Utility.testString(tmOrgSmall))
				{
					sql.append("     AND DS.ORG_ID = "+tmOrgSmall+" \n" );
				}
				
				if(Utility.testString(tmorg))
				{
					sql.append("     AND DS.ROOT_ORG_ID ="+tmorg+"  \n" );
				}
				if(Utility.testString(FStartDate))
				{
					sql.append("   AND  y.MAKE_DATE >= TO_DATE('"+FStartDate+"','YYYY-MM-DD')\n" );
				}
				if(Utility.testString(FEndDate))
				{
					sql.append("     AND y.MAKE_DATE <= TO_DATE('"+FEndDate+" 23:59:59','YYYY-MM-DD HH24:MI:SS')\n" );
				}
				
				if(Utility.testString(AStartDate))
				{
					sql.append("     AND  y.AUDITING_DATE >= TO_DATE('"+AStartDate+"','YYYY-MM-DD')\n" );
				}
				if(Utility.testString(AEndDate))
				{
					sql.append("     AND  y.AUDITING_DATE <= TO_DATE('"+AEndDate+" 23:59:59','YYYY-MM-DD HH24:MI:SS')\n" );
				}
				sql.append(" GROUP BY DS.ROOT_DEALER_CODE, DS.ROOT_DEALER_NAME");
				
				

			
			
		}
		return this.pageQuery(sql.toString(),null,this.getFunName(), pageSize, curPage); 
	}
	
	public List<Map<String, Object>> exportqueryOemDealer(Map<String,String> map) {
		StringBuffer sqlStr=new StringBuffer();
		List params = new LinkedList();
		sqlStr.append("select\n" );
		sqlStr.append("       t.name,\n" );
		sqlStr.append("       tba.area_name,\n" );
		sqlStr.append("       a.DEALER_CODE,\n" );
		sqlStr.append("       A.DEALER_NAME,\n" );
		sqlStr.append("       to_char(b.fine_date, 'yyyy-MM-dd') fine_date,\n" );
		sqlStr.append("       b.FINE_TYPE,\n" );
		sqlStr.append("       b.datum_sum,\n" );
		sqlStr.append("       b.labour_sum,\n" );
		sqlStr.append("       b.REMARK,\n" );
		sqlStr.append("       b.PAY_STATUS,\n" );
		sqlStr.append("       b.labour_bh,\n" );
		sqlStr.append("       b.balance_oder,\n" );
		sqlStr.append("       b.FINE_REASON\n" );
		sqlStr.append("  from tm_dealer a, TT_AS_WR_FINE b,tc_user t,tm_business_area tba\n" );
		sqlStr.append(" where a.DEALER_ID = b.DEALER_ID\n" );
		sqlStr.append(" and t.user_id=b.create_by\n" );
		sqlStr.append(" and tba.area_id=b.yieldly");
		sqlStr.append(" AND a.STATUS ="+Constant.STATUS_ENABLE+"\n");
		sqlStr.append(" AND a.DEALER_TYPE =");
		sqlStr.append(Constant.DEALER_TYPE_DWR);
		sqlStr.append("\n");

		if (null != map.get("dealerCode") && !"".equals(map.get("dealerCode"))) {
			String[] array = map.get("dealerCode").split(",");
			sqlStr.append("   AND a.DEALER_CODE IN ( \n");
			if(array.length>0){
				for (int i = 0; i < array.length; i++) {
					sqlStr.append("'"+array[i]+"'");
						if (i != array.length - 1) {
							sqlStr.append(",");
						}	
				}
			}else{
				sqlStr.append("''");//放空置，防止in里面报错
			}
			sqlStr.append(")\n");
		}	
		
		if (Utility.testString(map.get("deductStartDate"))) {//提报开始日期
			sqlStr.append("   AND b.fine_date >= TO_DATE('").append(map.get("deductStartDate").trim().concat(" 00:00:00")).append("','YYYY-MM-DD HH24:MI:SS') \n");
		}
		if(Utility.testString(map.get("fine_type")))
		{
			sqlStr.append(" and  b.FINE_TYPE = "+map.get("fine_type"));
		}
		if (Utility.testString(map.get("deductEndDate"))) {//提报结束日期
			sqlStr.append("   AND b.fine_date <= TO_DATE('").append(map.get("deductEndDate").trim().concat(" 23:59:59")).append("','YYYY-MM-DD HH24:MI:SS') \n");
		}
		if (Utility.testString(map.get("dealerName"))){
			sqlStr.append(" and  a.dealer_name like '%"+map.get("dealerName")+"%' ");
		}
		if (Utility.testString(map.get("ButieBh"))){
			sqlStr.append(" and  b.LABOUR_BH like '%"+map.get("ButieBh")+"%' ");
		}
		if (Utility.testString(map.get("SubsidiesType"))){
			sqlStr.append(" and  b.REMARK like '%"+map.get("SubsidiesType")+"%' "); //补贴类型
		}
		sqlStr.append(" ORDER BY a.DEALER_ID DESC ");
		List<Map<String, Object>> ps= pageQuery(sqlStr.toString(), null, getFunName());
		return ps;
	}


}
