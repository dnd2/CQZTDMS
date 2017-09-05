package com.infodms.dms.dao.report.serviceReport;

import java.sql.ResultSet;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.common.Constant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

public class TechnologyUpgradeDao extends BaseDao {
	public static final Logger logger = Logger.getLogger(TechnologyUpgradeDao.class);
	public static final TechnologyUpgradeDao dao = new TechnologyUpgradeDao();
	public static final TechnologyUpgradeDao getInstance(){
		return dao;
	}

	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO 自动生成的方法存根
		return null;
	}
	
	public  PageResult<Map<String, Object>> QueryTechnologyUpgrade(Map<String,String> map,int pageSize,int curPage) {
		
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT A.BALANCE_YIELDLY, /*结算基地*/\n" );
		sql.append("       A.RO_NO, /*工单号*/\n" );
		sql.append("       A.CLAIM_NO, /*结算单号*/\n" );
		sql.append("       NVL(A.SECOND_DEALER_CODE, D.DEALER_CODE) DEALER_CODE, /*服务站代码*/\n" );
		sql.append("       NVL(A.SECOND_DEALER_NAME, D.DEALER_NAME) DEALER_NAME, /*服务站全称*/\n" );
		sql.append("       D.DEALER_CODE PDEALER_CODE, /*一级站代码*/\n" );
		sql.append("       D.DEALER_NAME PDEALER_NAME, /*一级站全称*/\n" );
		sql.append("       D.ROOT_ORG_NAME, /*大区*/\n" );
		sql.append("       D.ORG_NAME, /*小区*/\n" );
		sql.append("       A.SERIES_NAME, /*车系*/\n" );
		sql.append("       A.MODEL_CODE, /*车型*/\n" );
		sql.append("       A.LICENSE_NO, /*车牌号*/\n" );
		sql.append("       A.ENGINE_NO, /*发动机号*/\n" );
		sql.append("       A.VIN, /*VIN*/\n" );
		sql.append("       TO_CHAR(V.PRODUCT_DATE, 'YYYY-MM-DD') PRODUCT_DATE, /*生产日期*/\n" );
		sql.append("       TO_CHAR(V.PURCHASED_DATE, 'YYYY-MM-DD') PURCHASED_DATE, /*购车日期*/\n" );
		sql.append("       C.CTM_NAME, /*车主*/\n" );
		sql.append("       C.MAIN_PHONE, /*车主电话*/\n" );
		sql.append("       C.ADDRESS, /*车主地址*/\n" );
		sql.append("       TO_CHAR(A.RO_STARTDATE, 'YYYY-MM-DD HH24:MI') RO_CREATE_DATE, /*工单维修时间*/\n" );
		sql.append("       TO_CHAR(A.RO_ENDDATE, 'YYYY-MM-DD HH24:MI') FOR_BALANCE_TIME, /*工单结算时间*/\n" );
		sql.append("       A.IN_MILEAGE, /*行驶里程*/\n" );
		sql.append("       '更换' REPAIR_TYPE, /*配件维修类型*/\n" );
		sql.append("       M.MAL_NAME, /*故障代码(取名称)*/\n" );
		sql.append("       P.REMARK, /*故障描述*/\n" );
		sql.append("       P.DOWN_PART_CODE, /*故障件代码*/\n" );
		sql.append("       PD.PART_CODE DOWN_PART_NO, /*故障件件号*/\n" );
		sql.append("       P.DOWN_PART_NAME DOWN_PART_NAME, /*故障件名称*/\n" );
		sql.append("       RT.CODE_DESC RESPONSIBILITY_TYPE, /*是否主因件*/\n" );
		sql.append("       P.PART_CODE, /*更换件代码*/\n" );
		sql.append("       P.PART_NAME, /*更换件名称*/\n" );
		sql.append("       P.APPLY_PRICE, /*配件单价*/\n" );
		sql.append("       P.QUANTITY, /*配件数量*/\n" );
		sql.append("       P.APPLY_AMOUNT, /*材料费*/\n" );
		sql.append("       DECODE(P.RESPONSIBILITY_TYPE, "+Constant.RESPONS_NATURE_STATUS_01+", L.APPLY_PRICE, NULL) APPLY_PRICE1, /*主因件工时单价*/\n" );
		sql.append("       DECODE(P.RESPONSIBILITY_TYPE, "+Constant.RESPONS_NATURE_STATUS_01+", L.APPLY_AMOUNT, NULL) APPLY_AMOUNT1, /*主因件工时费*/\n" );
		sql.append("       P.DOWN_PRODUCT_CODE, /*换下件制造商代码*/\n" );
		sql.append("       P.DOWN_PRODUCT_NAME, /*换下件制造商名称*/\n" );
		sql.append("       AA.ACTIVITY_CODE, /*活动编号*/\n" );
		sql.append("       AA.ACTIVITY_NAME, /*活动名称*/\n" );
		sql.append("       1 AS FREE_PART, /*优惠材料系数*/\n" );
		sql.append("       1 AS FREE_LABOUR, /*优惠工时系数*/\n" );
		sql.append("       N.APPLY_AMOUNT APPLY_AMOUNT2, /*赠送费用*/\n" );
		sql.append("       TO_CHAR(AA.FACTSTARTDATE, 'YYYY-MM-DD') FACTSTARTDATE, /*活动开始日期*/\n" );
		sql.append("       TO_CHAR(AA.FACTENDDATE, 'YYYY-MM-DD') FACTENDDATE/*活动结束日期*/\n" );
		sql.append("  FROM TT_AS_WR_APPLICATION A\n" );
		sql.append("  LEFT JOIN TT_AS_WR_LABOURITEM L ON A.ID = L.ID\n" );
		sql.append("  LEFT JOIN TT_AS_WR_PARTSITEM P ON P.ID = A.ID AND P.WR_LABOURCODE = L.WR_LABOURCODE\n" );
		sql.append("  LEFT JOIN VW_ORG_DEALER_SERVICE D ON A.DEALER_ID = D.DEALER_ID AND D.DEALER_TYPE = 10771002\n" );
		sql.append("  LEFT JOIN TM_VEHICLE V ON A.VIN = V.VIN\n" );
		sql.append("  LEFT JOIN TT_DEALER_ACTUAL_SALES DAS ON V.VEHICLE_ID = das.VEHICLE_ID\n" );
		sql.append("  LEFT JOIN TT_CUSTOMER C ON DAS.CTM_ID = C.CTM_ID\n" );
		sql.append("  LEFT JOIN TT_AS_WR_MALFUNCTION M ON M.MAL_ID = L.TROUBLE_TYPE\n" );
		sql.append("  LEFT JOIN TT_PART_DEFINE PD ON P.DOWN_PART_CODE = PD.PART_OLDCODE\n" );
		sql.append("  LEFT JOIN TC_CODE RT ON RT.TYPE = "+Constant.RESPONS_NATURE_STATUS+" AND RT.CODE_ID = P.RESPONSIBILITY_TYPE\n" );
		sql.append("  JOIN TT_AS_ACTIVITY AA ON A.CAMPAIGN_CODE = AA.ACTIVITY_CODE\n" );
		sql.append("  JOIN TT_AS_ACTIVITY_SUBJECT TS ON AA.SUBJECT_ID = TS.SUBJECT_ID\n" );
		sql.append("  LEFT JOIN (SELECT T.ID, SUM(NVL(T.APPLY_AMOUNT,0)) APPLY_AMOUNT FROM TT_AS_WR_NETITEM T GROUP BY T.ID) N ON A.ID = N.ID\n" );
		sql.append(" WHERE A.CREATE_DATE >= TO_DATE('2013-08-26', 'YYYY-MM-DD')\n" );
		sql.append("   AND A.CLAIM_TYPE = "+Constant.CLA_TYPE_06+"\n" );
		sql.append("   AND TS.ACTIVITY_TYPE = "+Constant.SERVICEACTIVITY_TYPE_01+"\n");
		

		if(Utility.testString(map.get("yieldlyType"))){
			sql.append(" and A.BALANCE_YIELDLY ='"+map.get("yieldlyType").toUpperCase()+"'\n"); 
		}
		if(Utility.testString(map.get("vin"))){
			sql.append(" AND A.VIN like '%"+map.get("vin").toUpperCase()+"%'\n"); 
		}
		if(Utility.testString(map.get("serisid"))){
			sql.append(" AND A.SERIES_CODE ='"+map.get("serisid").toUpperCase()+"'\n"); 
		}
		if (null != map.get("groupCode") && !"".equals(map.get("groupCode"))) {
			String[] array = map.get("groupCode").toString().split(",");
			sql.append("   AND A.MODEL_CODE IN ( \n");
			if(array.length>0){
				for (int i = 0; i < array.length; i++) {
					sql.append("'"+array[i]+"'");
						if (i != array.length - 1) {
							sql.append(",");
						}	
				}
			}else{
				sql.append("''");//放空置，防止in里面报错
			}
			sql.append(")\n");
		}
		if(Utility.testString(map.get("bigorgId"))){
			sql.append(" and D.ROOT_ORG_ID ='"+map.get("bigorgId").toUpperCase()+"'\n"); 
		}
		if(Utility.testString(map.get("smallorg"))){
			sql.append(" and D.ORG_ID ='"+map.get("smallorg").toUpperCase()+"'\n"); 
		}
		if (null != map.get("dealerCode") && !"".equals(map.get("dealerCode"))) {
			String[] array = map.get("dealerCode").toString().split(",");
			sql.append("   AND D.DEALER_CODE IN ( \n");
			if(array.length>0){
				for (int i = 0; i < array.length; i++) {
					sql.append("'"+array[i]+"'");
						if (i != array.length - 1) {
							sql.append(",");
						}	
				}
			}else{
				sql.append("''");//放空置，防止in里面报错
			}
			sql.append(")\n");
		}
		if(Utility.testString(map.get("dealerName"))){
			sql.append(" and D.DEALER_NAME like '%"+map.get("dealerName")+"%'\n"); 
		}
		if(Utility.testString(map.get("bDate"))){
			sql.append("and A.RO_STARTDATE>=to_date('"+map.get("bDate")+" 00:00:00','yyyy-mm-dd hh24:mi:ss')\n"); 
		}
		if(Utility.testString(map.get("eDate"))){
			sql.append("and A.RO_STARTDATE<=to_date('"+map.get("eDate")+" 23:59:59','yyyy-mm-dd hh24:mi:ss')\n"); 
		}
		if(Utility.testString(map.get("bgDate"))){
			sql.append("and A.RO_ENDDATE>=to_date('"+map.get("bgDate")+" 00:00:00','yyyy-mm-dd hh24:mi:ss')\n"); 
		}
		if(Utility.testString(map.get("egDate"))){
			sql.append("and A.RO_ENDDATE<=to_date('"+map.get("egDate")+" 23:59:59','yyyy-mm-dd hh24:mi:ss')\n"); 
		}
		if(Utility.testString(map.get("crbDate"))){
			sql.append("and V.PRODUCT_DATE>=to_date('"+map.get("crbDate")+" 00:00:00','yyyy-mm-dd hh24:mi:ss')\n"); 
		}
		if(Utility.testString(map.get("creDate"))){
			sql.append("and V.PRODUCT_DATE<=to_date('"+map.get("creDate")+" 23:59:59','yyyy-mm-dd hh24:mi:ss')\n"); 
		}
		if(Utility.testString(map.get("engineNo"))){
			sql.append(" and  A.ENGINE_NO like '%"+map.get("engineNo")+"%'\n"); 
		}
		if (null != map.get("activityId") && !"".equals(map.get("activityId"))) {
			String[] array = map.get("activityId").toString().split(",");
			sql.append("   AND AA.ACTIVITY_ID IN ( \n");
			if(array.length>0){
				for (int i = 0; i < array.length; i++) {
					sql.append(""+array[i]+"");
						if (i != array.length - 1) {
							sql.append(",");
						}	
				}
			}else{
				sql.append("''");//放空置，防止in里面报错
			}
			sql.append(")\n");
		}
		
		if(Utility.testString(map.get("activityName"))){
			sql.append(" and AA.ACTIVITY_NAME like '%"+map.get("activityName")+"%'\n"); 
		}
		
		PageResult<Map<String, Object>> ps= pageQuery(sql.toString(), null, getFunName(),pageSize, curPage);
		return ps;


}

	/**  
	* QueryActivity(查询活动编号)  
	* @param name  
	* @param @return 设定文件  
	* @return String DOM对象  
	* @Exception 异常对象  
	* @since  CodingExample　Ver(编码范例查看) 1.1  
	*/  
	public PageResult<Map<String, Object>> QueryActivity(
			Map<String, String> map, Integer pageSize, Integer curPage) {
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT A.ACTIVITY_ID,a.activity_code,a.activity_name,a.activity_type  FROM TT_AS_ACTIVITY A\n" );
		sql.append("WHERE 1=1\n" );
		sql.append(" and a.is_del=0 and a.status="+Constant.SERVICEACTIVITY_STATUS_02+" AND A.activity_type="+Constant.SERVICEACTIVITY_TYPE_01+"");
		if(Utility.testString(map.get("activityCode"))){
			sql.append(" and A.activity_code like '%"+map.get("activityCode")+"%'\n"); 
		}
		if(Utility.testString(map.get("activityName"))){
			sql.append(" and A.activity_name like '%"+map.get("activityName")+"%'\n"); 
		}
		
		
		
		PageResult<Map<String, Object>> ps= pageQuery(sql.toString(), null, getFunName(),pageSize, curPage);
		return ps;

	}
	



}
