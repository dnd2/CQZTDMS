package com.infodms.dms.dao.customerRelationships;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

import com.infodms.dms.common.Constant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.util.StringUtil;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;



@SuppressWarnings("unchecked")
public class SeatWorksReportformsDao extends BaseDao{

	private static final SeatWorksReportformsDao dao = new SeatWorksReportformsDao();
	
	public static final SeatWorksReportformsDao getInstance() {
		return dao;
	}
	
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		return null;
	}
	
	
	
	public PageResult<Map<String,Object>> querySeatWorksReportforms(String dealName,String dateStart,String dateEnd,String paiStart,String paiEnd ,int pageSize,int curPage){
		String sql  = returnSQL(dealName, dateStart, dateEnd,paiStart,paiEnd);
		return (PageResult<Map<String, Object>>)this.pageQuery(sql.toString(),
				null,
				this.getFunName(),
				pageSize,
				curPage);
	}
	
	
	public List<Map<String,Object>> querySeatWorksReportformsList(String dealName,String dateStart,String dateEnd,String paiStart,String paiEnd){
		String sql  = returnSQL(dealName, dateStart, dateEnd,paiStart,paiEnd);
		return (List<Map<String, Object>>)this.pageQuery(sql,
				null,
				this.getFunName());
	}	
	
	private String returnSQL(String dealName,String dateStart,String dateEnd,String paiStart,String paiEnd){
		StringBuffer sbSql = new StringBuffer();
		// 艾春 9.13 添加工作量统计SQL修改
		if((!Utility.testString(dateStart) && !Utility.testString(paiStart)) || (Utility.testString(dateStart)))
		{
			StringBuffer sql= new StringBuffer();
			sql.append("SELECT  NVL(A.SE_ACCOUNT, '合计') ACC,\n" );
			sql.append("        decode(a.se_account,null,null,MAX(A.SE_NAME)) NAME,\n" );
			sql.append("        SUM(CC.COMCM) COMCM,\n" );
			sql.append("        SUM(CC.COMCN) COMCN,\n" );
			sql.append("        SUM(CC.COMCB) COMCB,\n" );
			sql.append("        SUM(CC.COMCC) COMCC,\n" );
			sql.append("        SUM(CC.COUCM) COUCM,\n" );
			sql.append("        SUM(CC.COUCN) COUCN,\n" );
			sql.append("        SUM(CC.COUCB) COUCB,\n" );
			sql.append("        SUM(CC.COUCC) COUCC,\n" );
			sql.append("        SUM(V.RVDS) RVDS,\n" );
			sql.append("        SUM(V.RVEF) RVEF,\n" );
			sql.append("        SUM(V.RVFS) RVFS,\n" );
			sql.append("        SUM(V.RVGF) RVGF,\n" );
			sql.append("        SUM(V.RVHS) RVHS,\n" );
			sql.append("        SUM(V.RVIF) RVIF,\n" );
			sql.append("        SUM(V.RVJS) RVJS,\n" );
			sql.append("        SUM(V.RVKF) RVKF\n" );
			sql.append("  FROM TT_CRM_SEATS A\n" );
			sql.append("  LEFT JOIN (SELECT C.CP_ACC_USER, SUM(CASE WHEN C.CP_BIZ_TYPE = "+Constant.TYPE_COMPLAIN+" AND DECODE(S.WT_TYPE, "+Constant.SHIFT_TIMES_NIGHT_DAYS+", 0, "+Constant.SHIFT_TIMES_LONG_DAYS+", 1, "+Constant.SHIFT_TIMES_A_DAYS+", 1, "+Constant.SHIFT_TIMES_NORMAL_DAYS+", 2, NULL) = 1 THEN  1 END) COMCM,\n" );
			sql.append("                                   SUM(CASE WHEN C.CP_BIZ_TYPE = "+Constant.TYPE_COMPLAIN+" AND DECODE(S.WT_TYPE, "+Constant.SHIFT_TIMES_NIGHT_DAYS+", 0, "+Constant.SHIFT_TIMES_LONG_DAYS+", 1, "+Constant.SHIFT_TIMES_A_DAYS+", 1, "+Constant.SHIFT_TIMES_NORMAL_DAYS+", 2, NULL) = 0 THEN  1 END) COMCN,\n" );
			sql.append("                                   SUM(CASE WHEN C.CP_BIZ_TYPE = "+Constant.TYPE_COMPLAIN+" AND DECODE(S.WT_TYPE, "+Constant.SHIFT_TIMES_NIGHT_DAYS+", 0, "+Constant.SHIFT_TIMES_LONG_DAYS+", 1, "+Constant.SHIFT_TIMES_A_DAYS+", 1, "+Constant.SHIFT_TIMES_NORMAL_DAYS+", 2, NULL) IS NULL THEN  1 END) COMCB,\n" );
			sql.append("                                   SUM(CASE WHEN C.CP_BIZ_TYPE = "+Constant.TYPE_COMPLAIN+" AND DECODE(S.WT_TYPE, "+Constant.SHIFT_TIMES_NIGHT_DAYS+", 0, "+Constant.SHIFT_TIMES_LONG_DAYS+", 1, "+Constant.SHIFT_TIMES_A_DAYS+", 1, "+Constant.SHIFT_TIMES_NORMAL_DAYS+", 2, NULL) = 2 THEN  1 END) COMCC,\n" );
			sql.append("                                   SUM(CASE WHEN C.CP_BIZ_TYPE = "+Constant.TYPE_CONSULT+" AND DECODE(S.WT_TYPE, "+Constant.SHIFT_TIMES_NIGHT_DAYS+", 0, "+Constant.SHIFT_TIMES_LONG_DAYS+", 1, "+Constant.SHIFT_TIMES_A_DAYS+", 1, "+Constant.SHIFT_TIMES_NORMAL_DAYS+", 2, NULL) = 1 THEN  1 END) COUCM,\n" );
			sql.append("                                   SUM(CASE WHEN C.CP_BIZ_TYPE = "+Constant.TYPE_CONSULT+" AND DECODE(S.WT_TYPE, "+Constant.SHIFT_TIMES_NIGHT_DAYS+", 0, "+Constant.SHIFT_TIMES_LONG_DAYS+", 1, "+Constant.SHIFT_TIMES_A_DAYS+", 1, "+Constant.SHIFT_TIMES_NORMAL_DAYS+", 2, NULL) = 0 THEN  1 END) COUCN,\n" );
			sql.append("                                   SUM(CASE WHEN C.CP_BIZ_TYPE = "+Constant.TYPE_CONSULT+" AND DECODE(S.WT_TYPE, "+Constant.SHIFT_TIMES_NIGHT_DAYS+", 0, "+Constant.SHIFT_TIMES_LONG_DAYS+", 1, "+Constant.SHIFT_TIMES_A_DAYS+", 1, "+Constant.SHIFT_TIMES_NORMAL_DAYS+", 2, NULL) IS NULL THEN  1 END) COUCB,\n" );
			sql.append("                                   SUM(CASE WHEN C.CP_BIZ_TYPE = "+Constant.TYPE_CONSULT+" AND DECODE(S.WT_TYPE, "+Constant.SHIFT_TIMES_NIGHT_DAYS+", 0, "+Constant.SHIFT_TIMES_LONG_DAYS+", 1, "+Constant.SHIFT_TIMES_A_DAYS+", 1, "+Constant.SHIFT_TIMES_NORMAL_DAYS+", 2, NULL) = 2 THEN  1 END) COUCC\n" );
			sql.append("               FROM TT_CRM_COMPLAINT C LEFT JOIN TT_CRM_SORT_SHIFT S ON C.CP_ACC_USER = S.USER_ID AND C.CP_ACC_DATE BETWEEN S.STA_DATE AND S.END_DATE + 1 / 48\n" );
			sql.append("             WHERE 1 = 1\n");
			if(Utility.testString(dateStart))
			{
				sql.append(" and C.cp_acc_date >= to_date('"+dateStart+"','yyyy-MM-dd') ");
			}
			if(Utility.testString(dateEnd))
			{
				sql.append(" and C.cp_acc_date <= to_date('"+dateEnd+" 23:59:59','yyyy-MM-dd hh24:mi:ss')\n" );
			}

			sql.append(" group by c.CP_ACC_USER) CC ON CC.cp_acc_user = a.se_user_id\n" );
			sql.append("\n" );
			sql.append(" LEFT JOIN (SELECT R.RD_USER_ID,\n" );
			sql.append("                   SUM(CASE WHEN M.RV_TYPE = "+Constant.TYPE_RETURN_VISIT1+" AND M.RV_STATUS = "+Constant.RV_STATUS_4+" THEN 1 END) RVDS,\n" );
			sql.append("                   SUM(CASE WHEN M.RV_TYPE = "+Constant.TYPE_RETURN_VISIT1+" AND M.RV_STATUS NOT IN ("+Constant.RV_STATUS_4+", "+Constant.RV_STATUS_5+", "+Constant.RV_STATUS_6+") THEN 1 END) RVEF,\n" );
			sql.append("                   SUM(CASE WHEN M.RV_TYPE = "+Constant.TYPE_RETURN_VISIT2+" AND M.RV_STATUS = "+Constant.RV_STATUS_4+" THEN 1 ELSE NULL END) RVFS,\n" );
			sql.append("                   SUM(CASE WHEN M.RV_TYPE = "+Constant.TYPE_RETURN_VISIT2+" AND M.RV_STATUS NOT IN ("+Constant.RV_STATUS_4+", "+Constant.RV_STATUS_5+", "+Constant.RV_STATUS_6+") THEN 1 END) RVGF,\n" );
			sql.append("                   SUM(CASE WHEN M.RV_TYPE = "+Constant.TYPE_RETURN_VISIT3+" AND M.RV_STATUS = "+Constant.RV_STATUS_4+" THEN 1 ELSE NULL END) RVHS,\n" );
			sql.append("                   SUM(CASE WHEN M.RV_TYPE = "+Constant.TYPE_RETURN_VISIT3+" AND M.RV_STATUS NOT IN ("+Constant.RV_STATUS_4+", "+Constant.RV_STATUS_5+", "+Constant.RV_STATUS_6+") THEN 1 END) RVIF,\n" );
			sql.append("                   SUM(CASE WHEN M.RV_TYPE = "+Constant.TYPE_RETURN_VISIT4+" AND M.RV_STATUS = "+Constant.RV_STATUS_4+" THEN 1 ELSE NULL END) RVJS,\n" );
			sql.append("                   SUM(CASE WHEN M.RV_TYPE = "+Constant.TYPE_RETURN_VISIT4+" AND M.RV_STATUS NOT IN ("+Constant.RV_STATUS_4+", "+Constant.RV_STATUS_5+", "+Constant.RV_STATUS_6+") THEN 1 END) RVKF\n" );
			sql.append("               FROM TT_CRM_RETURN_VISIT M JOIN TT_CRM_RETURN_VISIT_RECORD R ON M.RV_ID = R.RV_ID\n" );
			sql.append("               WHERE 1 = 1\n" );
			if(Utility.testString(dateStart))
			{

			   sql.append("               and m.RV_DATE >= to_date('"+dateStart+"','yyyy-MM-dd')\n" );
			}
			if(Utility.testString(dateEnd))
			{
				sql.append("               and m.RV_DATE <= to_date('"+dateEnd+" 23:59:59','yyyy-MM-dd hh24:mi:ss')\n" );
			}

			sql.append("             GROUP BY R.RD_USER_ID) V\n" );
			sql.append("             ON V.RD_USER_ID = a.se_user_id\n" );
			sql.append(" where a.se_status = "+Constant.STATUS_ENABLE+" and a.se_is_seats = "+Constant.IF_TYPE_YES+"\n");
			if(Utility.testString(dealName))
			{
				sql.append(" and a.se_name like '%"+dealName+"%'  ");
			}
			sql.append("group by rollup(A.se_account)");
			sbSql = sql;
		}else if(Utility.testString(paiStart))
		{
			StringBuffer sql= new StringBuffer();
			sql.append("SELECT  NVL(A.SE_ACCOUNT, '合计') ACC,\n" );
			sql.append("        decode(a.se_account,null,null,MAX(A.SE_NAME)) NAME,\n" );
			sql.append("        SUM(CC.COMCM) COMCM,\n" );
			sql.append("        SUM(CC.COMCN) COMCN,\n" );
			sql.append("        SUM(CC.COMCB) COMCB,\n" );
			sql.append("        SUM(CC.COMCC) COMCC,\n" );
			sql.append("        SUM(CC.COUCM) COUCM,\n" );
			sql.append("        SUM(CC.COUCN) COUCN,\n" );
			sql.append("        SUM(CC.COUCB) COUCB,\n" );
			sql.append("        SUM(CC.COUCC) COUCC,\n" );
			sql.append("        SUM(V.RVDS) RVDS,\n" );
			sql.append("        SUM(V.RVEF) RVEF,\n" );
			sql.append("        SUM(V.RVFS) RVFS,\n" );
			sql.append("        SUM(V.RVGF) RVGF,\n" );
			sql.append("        SUM(V.RVHS) RVHS,\n" );
			sql.append("        SUM(V.RVIF) RVIF,\n" );
			sql.append("        SUM(V.RVJS) RVJS,\n" );
			sql.append("        SUM(V.RVKF) RVKF\n" );
			sql.append("  FROM TT_CRM_SEATS A\n" );
			sql.append("  LEFT JOIN (SELECT C.CP_ACC_USER, SUM(CASE WHEN C.CP_BIZ_TYPE = 9505 AND DECODE(S.WT_TYPE, "+Constant.SHIFT_TIMES_NIGHT_DAYS+", 0, "+Constant.SHIFT_TIMES_LONG_DAYS+", 1, "+Constant.SHIFT_TIMES_A_DAYS+", 1, "+Constant.SHIFT_TIMES_NORMAL_DAYS+", 2, NULL) = 1 THEN  1 END) COMCM,\n" );
			sql.append("                                   SUM(CASE WHEN C.CP_BIZ_TYPE = 9505 AND DECODE(S.WT_TYPE, "+Constant.SHIFT_TIMES_NIGHT_DAYS+", 0, "+Constant.SHIFT_TIMES_LONG_DAYS+", 1, "+Constant.SHIFT_TIMES_A_DAYS+", 1, "+Constant.SHIFT_TIMES_NORMAL_DAYS+", 2, NULL) = 0 THEN  1 END) COMCN,\n" );
			sql.append("                                   SUM(CASE WHEN C.CP_BIZ_TYPE = 9505 AND DECODE(S.WT_TYPE, "+Constant.SHIFT_TIMES_NIGHT_DAYS+", 0, "+Constant.SHIFT_TIMES_LONG_DAYS+", 1, "+Constant.SHIFT_TIMES_A_DAYS+", 1, "+Constant.SHIFT_TIMES_NORMAL_DAYS+", 2, NULL) IS NULL THEN  1 END) COMCB,\n" );
			sql.append("                                   SUM(CASE WHEN C.CP_BIZ_TYPE = 9505 AND DECODE(S.WT_TYPE, "+Constant.SHIFT_TIMES_NIGHT_DAYS+", 0, "+Constant.SHIFT_TIMES_LONG_DAYS+", 1, "+Constant.SHIFT_TIMES_A_DAYS+", 1, "+Constant.SHIFT_TIMES_NORMAL_DAYS+", 2, NULL) = 2 THEN  1 END) COMCC,\n" );
			sql.append("                                   SUM(CASE WHEN C.CP_BIZ_TYPE = 9506 AND DECODE(S.WT_TYPE, "+Constant.SHIFT_TIMES_NIGHT_DAYS+", 0, "+Constant.SHIFT_TIMES_LONG_DAYS+", 1, "+Constant.SHIFT_TIMES_A_DAYS+", 1, "+Constant.SHIFT_TIMES_NORMAL_DAYS+", 2, NULL) = 1 THEN  1 END) COUCM,\n" );
			sql.append("                                   SUM(CASE WHEN C.CP_BIZ_TYPE = 9506 AND DECODE(S.WT_TYPE, "+Constant.SHIFT_TIMES_NIGHT_DAYS+", 0, "+Constant.SHIFT_TIMES_LONG_DAYS+", 1, "+Constant.SHIFT_TIMES_A_DAYS+", 1, "+Constant.SHIFT_TIMES_NORMAL_DAYS+", 2, NULL) = 0 THEN  1 END) COUCN,\n" );
			sql.append("                                   SUM(CASE WHEN C.CP_BIZ_TYPE = 9506 AND DECODE(S.WT_TYPE, "+Constant.SHIFT_TIMES_NIGHT_DAYS+", 0, "+Constant.SHIFT_TIMES_LONG_DAYS+", 1, "+Constant.SHIFT_TIMES_A_DAYS+", 1, "+Constant.SHIFT_TIMES_NORMAL_DAYS+", 2, NULL) IS NULL THEN  1 END) COUCB,\n" );
			sql.append("                                   SUM(CASE WHEN C.CP_BIZ_TYPE = 9506 AND DECODE(S.WT_TYPE, "+Constant.SHIFT_TIMES_NIGHT_DAYS+", 0, "+Constant.SHIFT_TIMES_LONG_DAYS+", 1, "+Constant.SHIFT_TIMES_A_DAYS+", 1, "+Constant.SHIFT_TIMES_NORMAL_DAYS+", 2, NULL) = 2 THEN  1 END) COUCC\n" );
			sql.append("               FROM TT_CRM_COMPLAINT C LEFT JOIN TT_CRM_SORT_SHIFT S ON C.CP_ACC_USER = S.USER_ID AND C.CP_ACC_DATE BETWEEN S.STA_DATE AND S.END_DATE + 1 / 48\n" );
			sql.append("             WHERE 1 = 1\n");
			sql.append("               and C.cp_acc_date >= (SELECT MIN(ts.sta_date) FROM tt_crm_sort_shift ts WHERE trunc(ts.duty_date) = to_date('"+paiStart+"','yyyy-MM-dd'))\n" );
			sql.append("               and C.cp_acc_date <= (SELECT MAX(ts.end_date) + 1/48 FROM tt_crm_sort_shift ts WHERE trunc(ts.duty_date) = to_date('"+paiEnd+"','yyyy-MM-dd'))\n" );
			sql.append("               AND CASE WHEN s.wt_type != "+Constant.SHIFT_TIMES_NIGHT_DAYS+" AND trunc(s.end_date) = to_date('"+paiEnd+"','yyyy-MM-dd')+1 \n" );
			sql.append("                             AND c.cp_acc_date BETWEEN (SELECT MIN(ts.sta_date) FROM tt_crm_sort_shift ts WHERE ts.wt_type != "+Constant.SHIFT_TIMES_NIGHT_DAYS+" AND trunc(ts.duty_date) = to_date('"+paiEnd+"','yyyy-MM-dd') + 1)\n" );
			sql.append("                             AND (SELECT MAX(ts.end_date) + 1/48 FROM tt_crm_sort_shift ts WHERE ts.wt_type = "+Constant.SHIFT_TIMES_NIGHT_DAYS+" AND trunc(ts.duty_date) = to_date('"+paiEnd+"','yyyy-MM-dd')) THEN 0\n" );
			sql.append("                        WHEN s.wt_type = "+Constant.SHIFT_TIMES_NIGHT_DAYS+" AND trunc(s.end_date) = to_date('"+paiStart+"','yyyy-MM-dd')-1\n" );
			sql.append("                             AND c.cp_acc_date BETWEEN (SELECT MIN(ts.sta_date) FROM tt_crm_sort_shift ts WHERE ts.wt_type != "+Constant.SHIFT_TIMES_NIGHT_DAYS+" AND trunc(ts.duty_date) = to_date('"+paiStart+"','yyyy-MM-dd'))\n" );
			sql.append("                             AND (SELECT MAX(ts.end_date) + 1/48 FROM tt_crm_sort_shift ts WHERE ts.wt_type = "+Constant.SHIFT_TIMES_NIGHT_DAYS+" AND trunc(ts.duty_date) = to_date('"+paiStart+"','yyyy-MM-dd')-1) THEN 0 ELSE 1 END = 1\n" );
			sql.append("             group by c.CP_ACC_USER ) CC ON CC.cp_acc_user = a.se_user_id\n" );
			sql.append(" LEFT JOIN (SELECT R.RD_USER_ID,\n" );
			sql.append("                   SUM(CASE WHEN M.RV_TYPE = 95071001 AND M.RV_STATUS = "+Constant.RV_STATUS_4+" THEN 1 END) RVDS,\n" );
			sql.append("                   SUM(CASE WHEN M.RV_TYPE = 95071001 AND M.RV_STATUS NOT IN ("+Constant.RV_STATUS_4+", "+Constant.RV_STATUS_5+", "+Constant.RV_STATUS_6+") THEN 1 END) RVEF,\n" );
			sql.append("                   SUM(CASE WHEN M.RV_TYPE = 95071002 AND M.RV_STATUS = "+Constant.RV_STATUS_4+" THEN 1 ELSE NULL END) RVFS,\n" );
			sql.append("                   SUM(CASE WHEN M.RV_TYPE = 95071002 AND M.RV_STATUS NOT IN ("+Constant.RV_STATUS_4+", "+Constant.RV_STATUS_5+", "+Constant.RV_STATUS_6+") THEN 1 END) RVGF,\n" );
			sql.append("                   SUM(CASE WHEN M.RV_TYPE = 95071003 AND M.RV_STATUS = "+Constant.RV_STATUS_4+" THEN 1 ELSE NULL END) RVHS,\n" );
			sql.append("                   SUM(CASE WHEN M.RV_TYPE = 95071003 AND M.RV_STATUS NOT IN ("+Constant.RV_STATUS_4+", "+Constant.RV_STATUS_5+", "+Constant.RV_STATUS_6+") THEN 1 END) RVIF,\n" );
			sql.append("                   SUM(CASE WHEN M.RV_TYPE = 95071004 AND M.RV_STATUS = "+Constant.RV_STATUS_4+" THEN 1 ELSE NULL END) RVJS,\n" );
			sql.append("                   SUM(CASE WHEN M.RV_TYPE = 95071004 AND M.RV_STATUS NOT IN ("+Constant.RV_STATUS_4+", "+Constant.RV_STATUS_5+", "+Constant.RV_STATUS_6+") THEN 1 END) RVKF\n" );
			sql.append("               FROM TT_CRM_RETURN_VISIT M JOIN TT_CRM_RETURN_VISIT_RECORD R ON M.RV_ID = R.RV_ID\n" );
			sql.append("               WHERE 1 = 1\n" );
			sql.append("               and r.rd_date >= to_date('"+paiStart+"','yyyy-MM-dd')\n" );
			sql.append("               and r.rd_date <= to_date('"+paiEnd+" 23:59:59','yyyy-MM-dd hh24:mi:ss')\n" );
			sql.append("             GROUP BY R.RD_USER_ID) V\n" );
			sql.append("             ON v.RD_USER_ID = a.se_user_id\n" );
			sql.append(" where a.se_status = "+Constant.STATUS_ENABLE+" and a.se_is_seats = "+Constant.IF_TYPE_YES+"\n");
			if(Utility.testString(dealName))
			{
				sql.append(" and a.se_name like '%"+dealName+"%'  ");
			}
			sql.append("group by rollup(A.se_account)");
			sbSql = sql;
		}
		
		
		return sbSql.toString();
	}
	

}
