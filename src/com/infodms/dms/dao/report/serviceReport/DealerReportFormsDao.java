package com.infodms.dms.dao.report.serviceReport;


import java.sql.ResultSet;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;

import com.infodms.dms.common.Utility;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.util.StringUtil;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

@SuppressWarnings("unchecked")
public class DealerReportFormsDao extends BaseDao{
	public static final Logger logger = Logger.getLogger(DealerReportFormsDao.class);
	public static DealerReportFormsDao dao = new DealerReportFormsDao();
	public static DealerReportFormsDao getInstance(){
		return dao;
	}
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		return null;
	}
	
	public List<Map<String, Object>> getDealerReportList(String dealerCode,String dealerName,String tmorg,
			String tmOrgSmall,String claimType,String feeType,String activityType,String AStartDate,String AEndDate,
			String FStartDate,String FEndDate ){
		String sql = queryDealerReportSql(dealerCode, dealerName, tmorg, tmOrgSmall, claimType, feeType, activityType, AStartDate, AEndDate, FStartDate, FEndDate);
		return this.pageQuery(sql.toString(), null, this.getFunName());
	}
	public PageResult<Map<String,Object>> queryDealerReportForms(String dealerCode,String dealerName,String tmorg,
			String tmOrgSmall,String claimType,String feeType,String activityType,String AStartDate,String AEndDate,
			String FStartDate,String FEndDate,int pageSize,int curPage ){
		String sql = queryDealerReportSql(dealerCode, dealerName, tmorg, tmOrgSmall, claimType, feeType, activityType, AStartDate, AEndDate, FStartDate, FEndDate);
		return this.pageQuery(sql.toString(),null,this.getFunName(), pageSize, curPage); 
	}
	
	private String queryDealerReportSql(String dealerCode,String dealerName,String tmorg,
			String tmOrgSmall,String claimType,String feeType,String activityType,String AStartDate,String AEndDate,
			String FStartDate,String FEndDate){
		//FStartDate 结算上报日期开始  AStartDate 结算日期结束
		StringBuffer sql = new StringBuffer();
		if(Utility.testString(FStartDate))
		{
			sql.append("SELECT M.DEALER_CODE, /*服务站代码*/\r\n" );
			sql.append("       M.DEALER_NAME, /*服务站名称*/\r\n" );
			sql.append("       M.ROOT_DEALER_CODE, /*一级站代码*/\r\n" );
			sql.append("       M.ROOT_DEALER_NAME, /*一级站名称*/\r\n" );
			sql.append("       ROUND(SUM(M.FREE_AMOUNT) / 1.17) FREE_AMOUNT, /*强保费用*/\r\n" );
			sql.append("       ROUND(SUM(M.PART_AMOUNT) / 1.17) PART_AMOUNT, /*维修材料费*/\r\n" );
			sql.append("       SUM(M.FREE_TIMES) FREE_TIMES, /*强保台数*/\r\n" );
			sql.append("       SUM(M.CHECK_TIMES) CHECK_TIMES, /*定保次数*/\r\n" );
			sql.append("       SUM(M.BALANCE_TIMES) BALANCE_TIMES, /*结算台次*/\r\n" );
			sql.append("       SUM(M.REPAIR_TIMES) REPAIR_TIMES /*维修台次*/\r\n" );
			sql.append("  FROM (SELECT DS.DEALER_CODE, /*服务站代码*/\r\n" );
			sql.append("               DS.DEALER_NAME, /*服务站名称*/\r\n" );
			sql.append("               DS.ROOT_DEALER_CODE, /*一级站代码*/\r\n" );
			sql.append("               DS.ROOT_DEALER_NAME, /*一级站名称*/\r\n" );
			sql.append("               DECODE(A.CLAIM_TYPE, 10661002, NVL(mg.part_price, 0), 0) FREE_AMOUNT, /*强保费用*/\r\n" );
			sql.append("               NVL(A.APPLY_PART_AMOUNT, 0) + NVL(a.part_down,0) PART_AMOUNT, /*维修材料费+材料打折费用*/\r\n" );
			sql.append("               DECODE(A.CLAIM_TYPE, 10661002, 1, 0) FREE_TIMES, /*强保台数*/\r\n" );
			sql.append("               0 CHECK_TIMES, /*定保次数*/\r\n" );
			sql.append("               CASE WHEN A.STATUS IN (10791006, 10791013, 10791016) THEN 1 ELSE 0 END BALANCE_TIMES, /*结算台次*/\r\n" );
			sql.append("               CASE WHEN A.Claim_Type IN (10661001, 10661007, 10661009, 10661010) THEN 1 ELSE 0 END REPAIR_TIMES /*维修台次*/\r\n" );
			sql.append("          FROM TT_AS_WR_APPLICATION A\r\n" );
			sql.append("          JOIN VW_ORG_DEALER_SERVICE DS ON NVL(A.SECOND_DEALER_ID, A.DEALER_ID) = DS.DEALER_ID\r\n" );
			sql.append("          LEFT JOIN tm_vhcl_material_group g ON a.model_code = g.group_code AND g.group_level = 4\r\n" );
			sql.append("          LEFT JOIN tt_as_wr_model_item mi ON g.group_id = mi.model_id\r\n" );
			sql.append("          LEFT JOIN tt_as_wr_model_group mg ON mg.wrgroup_id = mi.wrgroup_id\r\n" );
//			sql.append("        /*     AND RO.FOR_BALANCE_TIME >= TO_DATE(''\\*结算上报日期开始*\\,'YYYY-MM-DD')\r\n" );
//			sql.append("             AND RO.FOR_BALANCE_TIME <= TO_DATE(''\\*结算上报日期结束*\\,'YYYY-MM-DD HH24:MI:SS')*/\r\n" );
			
			sql.append("          LEFT JOIN TT_AS_ACTIVITY AA ON A.CAMPAIGN_CODE = AA.ACTIVITY_CODE\r\n" );
			sql.append("          LEFT JOIN TT_AS_ACTIVITY_SUBJECT TS ON AA.SUBJECT_ID =\r\n" );
			sql.append("                                                 TS.SUBJECT_ID\r\n" );
			sql.append("         WHERE A.STATUS >= 10791002\r\n" );
			sql.append("           AND A.BALANCE_YIELDLY = 95411001\r\n" );
			
			if(StringUtil.notNull(FStartDate)){
				sql.append(" and A.REPORT_DATE >=to_date('"+FStartDate+"','yyyy-MM-dd') \r\n");
			}
			if(StringUtil.notNull(FEndDate)){
				sql.append(" and A.REPORT_DATE<=to_date('"+FEndDate+" 23:59:59','yyyy-MM-dd hh24:mi:ss') \r\n");
			}
			if(StringUtil.notNull(dealerCode)){
				sql.append(" and DS.DEALER_CODE LIKE '%"+dealerCode+"%' \r\n");
			}
			if(StringUtil.notNull(dealerName)){
				sql.append(" and DS.DEALER_NAME LIKE '%"+dealerName+"%' \r\n");
			}
			if(StringUtil.notNull(claimType)){
				sql.append(" and A.CLAIM_TYPE IN ('"+claimType+"') \r\n");
			}
			if(StringUtil.notNull(activityType)){
				sql.append(" and TS.ACTIVITY_TYPE IN ('"+activityType+"')  \r\n");
			}
			if(StringUtil.notNull(tmorg)){
				sql.append(" and DS.ROOT_ORG_ID ='"+tmorg+"'  \r\n");
			}
			if(StringUtil.notNull(tmOrgSmall)){
				sql.append(" and DS.ORG_ID ='"+tmOrgSmall+"'  \r\n");
			}
			sql.append("        UNION ALL\r\n" );
			sql.append("        SELECT DS.DEALER_CODE,\r\n" );
			sql.append("               DS.DEALER_NAME,\r\n" );
			sql.append("               DS.ROOT_DEALER_CODE,\r\n" );
			sql.append("               DS.ROOT_DEALER_NAME,\r\n" );
			sql.append("               0,\r\n" );
			sql.append("               DECODE(S.BALANCE_FEE_TYPE, 94141002, S.DECLARE_SUM1, 0),\r\n" );
			sql.append("               0,\r\n" );
			sql.append("               0,\r\n" );
			sql.append("               DECODE(S.STATUS, 11841010, 1, 0),\r\n" );
			sql.append("               0\r\n" );
			sql.append("          FROM TT_AS_WR_SPEFEE S\r\n" );
			sql.append("          JOIN VW_ORG_DEALER_SERVICE DS ON S.DEALER_ID = DS.DEALER_ID\r\n" );
			sql.append("          LEFT JOIN TT_AS_WR_SPEFEE_AUDITING SA ON S.ID = SA.FEE_ID AND SA.STATUS = 11841010\r\n" );
			sql.append("         WHERE S.FEE_TYPE != 11831002\r\n" );
			sql.append("           AND S.STATUS >= 11841002\r\n" );
			if(StringUtil.notNull(FStartDate)){
				sql.append(" and S.MAKE_DATE >=to_date('"+FStartDate+"','yyyy-MM-dd') \r\n");
			}
			if(StringUtil.notNull(FEndDate)){
				sql.append(" and S.MAKE_DATE<=to_date('"+FEndDate+" 23:59:59','yyyy-MM-dd hh24:mi:ss') \r\n");
			}
			if(StringUtil.notNull(AStartDate)){
				sql.append(" and SA.AUDITING_DATE >=to_date('"+AStartDate+"','yyyy-MM-dd') \r\n");
			}
			if(StringUtil.notNull(AEndDate)){
				sql.append(" and SA.AUDITING_DATE<=to_date('"+AEndDate+" 23:59:59','yyyy-MM-dd hh24:mi:ss') \r\n");
			}
			if(StringUtil.notNull(feeType)){
				sql.append(" and S.FEE_TYPE IN ('"+feeType+"')\r\n");
			}
			if(StringUtil.notNull(tmorg)){
				sql.append(" and DS.ORG_ID  = '"+tmorg+"' \r\n");
			}
			if(StringUtil.notNull(tmOrgSmall)){
				sql.append(" and DS.ROOT_ORG_ID = '"+tmOrgSmall+"' \r\n");
			}
			sql.append("        ) M\r\n" );
			sql.append(" GROUP BY M.DEALER_CODE,\r\n" );
			sql.append("          M.DEALER_NAME,\r\n" );
			sql.append("          M.ROOT_DEALER_CODE,\r\n" );
			sql.append("          M.ROOT_DEALER_NAME");
			
			
		}else
		{
			sql.append("SELECT M.DEALER_CODE, /*服务站代码*/\r\n" );
			sql.append("       M.DEALER_NAME, /*服务站名称*/\r\n" );
			sql.append("       M.ROOT_DEALER_CODE, /*一级站代码*/\r\n" );
			sql.append("       M.ROOT_DEALER_NAME, /*一级站名称*/\r\n" );
			sql.append("       ROUND(SUM(M.FREE_AMOUNT) / 1.17) FREE_AMOUNT, /*强保费用*/\r\n" );
			sql.append("       ROUND(SUM(M.PART_AMOUNT) / 1.17) PART_AMOUNT, /*维修材料费*/\r\n" );
			sql.append("       SUM(M.FREE_TIMES) FREE_TIMES, /*强保台数*/\r\n" );
			sql.append("       SUM(M.CHECK_TIMES) CHECK_TIMES, /*定保次数*/\r\n" );
			sql.append("       SUM(M.BALANCE_TIMES) BALANCE_TIMES, /*结算台次*/\r\n" );
			sql.append("       SUM(M.REPAIR_TIMES) REPAIR_TIMES /*维修台次*/\r\n" );
			sql.append("  FROM (SELECT DS.DEALER_CODE, /*服务站代码*/\r\n" );
			sql.append("               DS.DEALER_NAME, /*服务站名称*/\r\n" );
			sql.append("               DS.ROOT_DEALER_CODE, /*一级站代码*/\r\n" );
			sql.append("               DS.ROOT_DEALER_NAME, /*一级站名称*/\r\n" );
			sql.append("               0 FREE_AMOUNT, /*强保费用*/\r\n" );
			sql.append("               0 PART_AMOUNT, /*维修材料费+材料打折费用*/\r\n" );
			sql.append("               0 FREE_TIMES, /*强保台数*/\r\n" );
			sql.append("               case when  A.FREE_TIMES >=2   then 1 else 0 end  CHECK_TIMES , /*定保次数*/\r\n" );
			sql.append("               0 BALANCE_TIMES, /*结算台次*/\r\n" );
			sql.append("               0 REPAIR_TIMES /*维修台次*/\r\n" );
			sql.append("          FROM TT_AS_REPAIR_ORDER A\r\n" );
			sql.append("          JOIN VW_ORG_DEALER_SERVICE DS ON NVL(A.SECOND_DEALER_ID, A.DEALER_ID) = DS.DEALER_ID\r\n" );
			sql.append("         WHERE \r\n" );
			sql.append("           A.BALANCE_YIELDLY = 95411001   and A.FREE_TIMES >=2 \r\n" );
			
			if(StringUtil.notNull(AStartDate)){
				sql.append(" and A.FOR_BALANCE_TIME >=to_date('"+AStartDate+"','yyyy-MM-dd') \r\n");
			}
			if(StringUtil.notNull(AEndDate)){
				 sql.append(" and A.FOR_BALANCE_TIME<=to_date('"+AEndDate+" 23:59:59','yyyy-MM-dd hh24:mi:ss') \r\n");
			}
			if(StringUtil.notNull(dealerCode)){
				sql.append(" and DS.DEALER_CODE LIKE '%"+dealerCode+"%' \r\n");
			}
			if(StringUtil.notNull(tmorg)){
				sql.append(" and DS.ROOT_ORG_ID ='"+tmorg+"'  \r\n");
			}
			if(StringUtil.notNull(tmOrgSmall)){
				sql.append(" and DS.ORG_ID ='"+tmOrgSmall+"'  \r\n");
			}
			sql.append("        ) M\r\n" );
			sql.append(" GROUP BY M.DEALER_CODE,\r\n" );
			sql.append("          M.DEALER_NAME,\r\n" );
			sql.append("          M.ROOT_DEALER_CODE,\r\n" );
			sql.append("          M.ROOT_DEALER_NAME");
			
		}
		return sql.toString();
		
	}

}
