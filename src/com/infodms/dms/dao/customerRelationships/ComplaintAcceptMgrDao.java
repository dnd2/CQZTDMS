package com.infodms.dms.dao.customerRelationships;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.StringUtil;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

@SuppressWarnings("unchecked")
public class ComplaintAcceptMgrDao extends BaseDao{
	
	private static final ComplaintAcceptMgrDao dao = new ComplaintAcceptMgrDao();
	
	public static final ComplaintAcceptMgrDao getInstance() {
		return dao;
	}
	
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		return null;
	}
	
	public PageResult<Map<String, Object>> queryConsultInfo(Map<String, Object> map,Long userId,int pageSize,int curPage){
		
		String cpNo = CommonUtils.checkNull(map.get("cpNo")); 
		String voucherType = CommonUtils.checkNull(map.get("voucherType"));
		String voucherStatus = CommonUtils.checkNull(map.get("voucherStatus"));
		String level = CommonUtils.checkNull(map.get("level"));
		String acceptStart = CommonUtils.checkNull(map.get("acceptStart"));
		String acceptEnd = CommonUtils.checkNull(map.get("acceptEnd"));
		String dealStart = CommonUtils.checkNull(map.get("dealStart"));
		String dealEnd = CommonUtils.checkNull(map.get("dealEnd"));
		
		StringBuffer sql = new StringBuffer();

		sql.append(" SELECT COMPLAINT.CP_NO CPNO, /*单号*/\n");
		sql.append("        COMPLAINT.CP_ID CPID, /*ID*/\n");
		sql.append("        COMPLAINT.CP_LEVEL CPLEVEL, /*级别*/\n");
		sql.append("        COMPLAINT.CP_STATUS STATUS, /*状态*/\n");
		sql.append("        COMPLAINT.CP_BIZ_TYPE BIZTYPE, /*业务类型*/\n");
		sql.append("        COMPLAINT.CP_BIZ_CONTENT BIZCONT, /*需转换*/\n");
		sql.append("        TRP.REGION_NAME PRO, /*省份*/\n");
		sql.append("        TRC.REGION_NAME CITY, /*城市*/\n");
		sql.append("        COMPLAINT.CP_NAME CTMNAME, /*客户名称*/\n");
		sql.append("        COMPLAINT.CP_CUS_ID CTMID, /*客户ID*/\n");
		sql.append("        COMPLAINT.CP_PHONE PHONE, /*电话*/\n");
		sql.append("        COMPLAINT.CP_CONCAT_PERSON, /*联系人姓名*/\n");
		sql.append("        COMPLAINT.CP_CONCAT_PHONE, /*联系人电话*/\n");
		sql.append("        COMPLAINT.CP_CONTENT_TYPE_REMARK REMARK, /*备注*/\n");
		sql.append("        COMPLAINT.CP_ACC_DATE CPACCDATE, /*受理日期*/\n");
		sql.append("        ACUSER.NAME ACUSER, /*受理人*/\n");
		sql.append("        COMPLAINT.CP_VIN VIN,\n");
		sql.append("        TO_CHAR(COMPLAINT.CP_BUY_DATE, 'YYYY-MM-DD') BDATE, /*购买日期*/\n");
		sql.append("        TO_CHAR(COMPLAINT.CP_CLOSE_DATE, 'YYYY-MM-DD') CP_CLOSE_DATE, /*关闭日期*/\n");

		sql.append(" CASE WHEN COMPLAINT.CP_DEAL_ID="+userId+" THEN 0\n");
		sql.append("           ELSE 1 END ISSELF");

		sql.append("   FROM TT_CRM_COMPLAINT COMPLAINT\n");
		sql.append("   LEFT JOIN TM_REGION TRP\n");
		sql.append("     ON TRP.REGION_CODE = COMPLAINT.CP_PROVINCE_ID\n");
		sql.append("    AND TRP.REGION_TYPE = 10541002\n");
		sql.append("   LEFT JOIN TM_REGION TRC\n");
		sql.append("     ON TRC.REGION_CODE = COMPLAINT.CP_CITY_ID\n");
		sql.append("    AND TRC.REGION_TYPE = 10541003\n");
		sql.append("   LEFT JOIN TM_DEALER REOBJ\n");
		sql.append("     ON REOBJ.DEALER_ID = COMPLAINT.CP_OBJECT\n");
		sql.append("   LEFT JOIN TC_USER ACUSER\n");
		sql.append("     ON ACUSER.USER_ID = COMPLAINT.CP_ACC_USER\n");
		sql.append("  WHERE 1 = 1\n");
		//查询条件
		if(StringUtil.notNull(cpNo)){
			sql.append("    AND COMPLAINT.CP_NO LIKE '%"+cpNo+"%'\n");
		}
		if(StringUtil.notNull(voucherType)){
		sql.append("    AND COMPLAINT.CP_BIZ_TYPE = "+voucherType+"\n");
		}
		if(StringUtil.notNull(voucherStatus)){
		sql.append("    AND COMPLAINT.CP_STATUS = "+voucherStatus+"\n");
		}
		if(StringUtil.notNull(acceptStart)){
		sql.append("    AND COMPLAINT.CP_ACC_DATE >= TO_DATE('"+acceptStart+"', 'YYYY-MM-DD')\n");
		}
		if(StringUtil.notNull(acceptEnd)){
		sql.append("    AND COMPLAINT.CP_ACC_DATE <=\n");
		sql.append("        TO_DATE('"+acceptEnd+" 23:59:59', 'YYYY-MM-DD HH24:MI:SS')\n");
		}
		if(StringUtil.notNull(dealStart)){
		sql.append("    AND COMPLAINT.CP_CLOSE_DATE >= TO_DATE('"+dealStart+"', 'YYYY-MM-DD')\n");
		}
		if(StringUtil.notNull(cpNo)){
		sql.append("    AND COMPLAINT.CP_CLOSE_DATE <=\n");
		sql.append("        TO_DATE('"+dealEnd+" 23:59:59', 'YYYY-MM-DD HH24:MI:SS')\n");
		}
		if(StringUtil.notNull(level)){
		sql.append("    AND COMPLAINT.CP_LEVEL = "+level+"\n");
		}
		sql.append("  ORDER BY COMPLAINT.CP_ACC_DATE DESC");

		return dao.pageQuery(sql.toString(), null, getFunName(),pageSize, curPage);
	}
	
public PageResult<Map<String, Object>> queryConsultInfoZxAndKh(Map<String, Object> map,int pageSize,int curPage){
		
		String cpNo = CommonUtils.checkNull(map.get("cpNo")); 
		String voucherType = CommonUtils.checkNull(map.get("voucherType"));
		String voucherStatus = CommonUtils.checkNull(map.get("voucherStatus"));
		String level = CommonUtils.checkNull(map.get("level"));
		String acceptStart = CommonUtils.checkNull(map.get("acceptStart"));
		String acceptEnd = CommonUtils.checkNull(map.get("acceptEnd"));
		String dealStart = CommonUtils.checkNull(map.get("dealStart"));
		String dealEnd = CommonUtils.checkNull(map.get("dealEnd"));
		
		StringBuffer sql = new StringBuffer();

		sql.append(" SELECT COMPLAINT.CP_NO CPNO, /*单号*/\n");
		sql.append("        COMPLAINT.CP_ID CPID, /*ID*/\n");
		sql.append("        COMPLAINT.CP_LEVEL CPLEVEL, /*级别*/\n");
		sql.append("        COMPLAINT.CP_STATUS STATUS, /*状态*/\n");
		sql.append("        COMPLAINT.CP_BIZ_TYPE BIZTYPE, /*业务类型*/\n");
		sql.append("        COMPLAINT.CP_BIZ_CONTENT BIZCONT, /*需转换*/\n");
		sql.append("        TRP.REGION_NAME PRO, /*省份*/\n");
		sql.append("        TRC.REGION_NAME CITY, /*城市*/\n");
		sql.append("        COMPLAINT.CP_NAME CTMNAME, /*客户名称*/\n");
		sql.append("        COMPLAINT.CP_CUS_ID CTMID, /*客户ID*/\n");
		sql.append("        COMPLAINT.CP_PHONE PHONE, /*电话*/\n");
		sql.append("        COMPLAINT.CP_CONCAT_PERSON, /*联系人姓名*/\n");
		sql.append("        COMPLAINT.CP_CONCAT_PHONE, /*联系人电话*/\n");
		sql.append("        COMPLAINT.CP_CONTENT_TYPE_REMARK REMARK, /*备注*/\n");
		sql.append("        COMPLAINT.CP_ACC_DATE CPACCDATE, /*受理日期*/\n");
		sql.append("        ACUSER.NAME ACUSER, /*受理人*/\n");
		sql.append("        COMPLAINT.CP_VIN VIN,\n");
		sql.append("        TO_CHAR(COMPLAINT.CP_BUY_DATE, 'YYYY-MM-DD') BDATE, /*购买日期*/\n");
		sql.append("        TO_CHAR(COMPLAINT.CP_CLOSE_DATE, 'YYYY-MM-DD') CP_CLOSE_DATE/*关闭日期*/\n");
		sql.append("   FROM TT_CRM_COMPLAINT COMPLAINT\n");
		sql.append("   LEFT JOIN TM_REGION TRP\n");
		sql.append("     ON TRP.REGION_CODE = COMPLAINT.CP_PROVINCE_ID\n");
		sql.append("    AND TRP.REGION_TYPE = 10541002\n");
		sql.append("   LEFT JOIN TM_REGION TRC\n");
		sql.append("     ON TRC.REGION_CODE = COMPLAINT.CP_CITY_ID\n");
		sql.append("    AND TRC.REGION_TYPE = 10541003\n");
		sql.append("   LEFT JOIN TM_DEALER REOBJ\n");
		sql.append("     ON REOBJ.DEALER_ID = COMPLAINT.CP_OBJECT\n");
		sql.append("   LEFT JOIN TC_USER ACUSER\n");
		sql.append("     ON ACUSER.USER_ID = COMPLAINT.CP_ACC_USER\n");
		sql.append("  WHERE 1 = 1\n");
		sql.append("  AND COMPLAINT.CP_STATUS NOT IN ("+Constant.VOUCHER_STATUS_01+")\n");
		//查询条件
		if(StringUtil.notNull(cpNo)){
			sql.append("    AND COMPLAINT.CP_NO LIKE '%"+cpNo+"%'\n");
		}
		if(StringUtil.notNull(voucherType)){
		sql.append("    AND COMPLAINT.CP_BIZ_TYPE = "+voucherType+"\n");
		}
		if(StringUtil.notNull(voucherStatus)){
		sql.append("    AND COMPLAINT.CP_STATUS = "+voucherStatus+"\n");
		}
		if(StringUtil.notNull(acceptStart)){
		sql.append("    AND COMPLAINT.CP_ACC_DATE >= TO_DATE('"+acceptStart+"', 'YYYY-MM-DD')\n");
		}
		if(StringUtil.notNull(acceptEnd)){
		sql.append("    AND COMPLAINT.CP_ACC_DATE <=\n");
		sql.append("        TO_DATE('"+acceptEnd+" 23:59:59', 'YYYY-MM-DD HH24:MI:SS')\n");
		}
		if(StringUtil.notNull(dealStart)){
		sql.append("    AND COMPLAINT.CP_CLOSE_DATE >= TO_DATE('"+dealStart+"', 'YYYY-MM-DD')\n");
		}
		if(StringUtil.notNull(cpNo)){
		sql.append("    AND COMPLAINT.CP_CLOSE_DATE <=\n");
		sql.append("        TO_DATE('"+dealEnd+" 23:59:59', 'YYYY-MM-DD HH24:MI:SS')\n");
		}
		if(StringUtil.notNull(level)){
		sql.append("    AND COMPLAINT.CP_LEVEL = "+level+"\n");
		}
		sql.append("  ORDER BY COMPLAINT.CP_ACC_DATE DESC");

		return dao.pageQuery(sql.toString(), null, getFunName(),pageSize, curPage);
	}
	
public PageResult<Map<String, Object>> queryConsultInfoOrgAndDr(Map<String, Object> map,Long userId,int pageSize,int curPage){
	
	String cpNo = CommonUtils.checkNull(map.get("cpNo")); 
	String voucherType = CommonUtils.checkNull(map.get("voucherType"));
	String voucherStatus = CommonUtils.checkNull(map.get("voucherStatus"));
	String level = CommonUtils.checkNull(map.get("level"));
	String acceptStart = CommonUtils.checkNull(map.get("acceptStart"));
	String acceptEnd = CommonUtils.checkNull(map.get("acceptEnd"));
	String dealStart = CommonUtils.checkNull(map.get("dealStart"));
	String dealEnd = CommonUtils.checkNull(map.get("dealEnd"));
	
	StringBuffer sql = new StringBuffer();

	sql.append(" SELECT COMPLAINT.CP_NO CPNO, /*单号*/\n");
	sql.append("        COMPLAINT.CP_ID CPID, /*ID*/\n");
	sql.append("        COMPLAINT.CP_LEVEL CPLEVEL, /*级别*/\n");
	sql.append("        COMPLAINT.CP_STATUS STATUS, /*状态*/\n");
	sql.append("        COMPLAINT.CP_BIZ_TYPE BIZTYPE, /*业务类型*/\n");
	sql.append("        COMPLAINT.CP_BIZ_CONTENT BIZCONT, /*需转换*/\n");
	sql.append("        TRP.REGION_NAME PRO, /*省份*/\n");
	sql.append("        TRC.REGION_NAME CITY, /*城市*/\n");
	sql.append("        COMPLAINT.CP_NAME CTMNAME, /*客户名称*/\n");
	sql.append("        COMPLAINT.CP_CUS_ID CTMID, /*客户ID*/\n");
	sql.append("        COMPLAINT.CP_PHONE PHONE, /*电话*/\n");
	sql.append("        COMPLAINT.CP_CONCAT_PERSON, /*联系人姓名*/\n");
	sql.append("        COMPLAINT.CP_CONCAT_PHONE, /*联系人电话*/\n");
	sql.append("        COMPLAINT.CP_CONTENT_TYPE_REMARK REMARK, /*备注*/\n");
	sql.append("        COMPLAINT.CP_ACC_DATE CPACCDATE, /*受理日期*/\n");
	sql.append("        ACUSER.NAME ACUSER, /*受理人*/\n");
	sql.append("        COMPLAINT.CP_VIN VIN,\n");
	sql.append("        TO_CHAR(COMPLAINT.CP_BUY_DATE, 'YYYY-MM-DD') BDATE, /*购买日期*/\n");
	sql.append("        TO_CHAR(COMPLAINT.CP_CLOSE_DATE, 'YYYY-MM-DD') CP_CLOSE_DATE/*关闭日期*/\n");
	sql.append("   FROM TT_CRM_COMPLAINT COMPLAINT\n");
	sql.append("   LEFT JOIN TM_REGION TRP\n");
	sql.append("     ON TRP.REGION_CODE = COMPLAINT.CP_PROVINCE_ID\n");
	sql.append("    AND TRP.REGION_TYPE = 10541002\n");
	sql.append("   LEFT JOIN TM_REGION TRC\n");
	sql.append("     ON TRC.REGION_CODE = COMPLAINT.CP_CITY_ID\n");
	sql.append("    AND TRC.REGION_TYPE = 10541003\n");
	sql.append("   LEFT JOIN TM_DEALER REOBJ\n");
	sql.append("     ON REOBJ.DEALER_ID = COMPLAINT.CP_OBJECT\n");
	sql.append("   LEFT JOIN TC_USER ACUSER\n");
	sql.append("     ON ACUSER.USER_ID = COMPLAINT.CP_ACC_USER\n");
	sql.append("   FULL JOIN （\n");
	sql.append(" SELECT DISTINCT T.CP_ID, T.CD_USER_ID\n");
	sql.append("   FROM TT_CRM_COMPLAINT_DEAL_RECORD T）A\n");
	sql.append("     ON COMPLAINT.CP_ID = A.CP_ID\n");
	sql.append("  WHERE 1 = 1\n");

	sql.append("  AND COMPLAINT.CP_STATUS NOT IN ("+Constant.VOUCHER_STATUS_01+")\n");
	//查询条件
	if(StringUtil.notNull(cpNo)){
		sql.append("    AND COMPLAINT.CP_NO LIKE '%"+cpNo+"%'\n");
	}
	if(StringUtil.notNull(voucherType)){
	sql.append("    AND COMPLAINT.CP_BIZ_TYPE = "+voucherType+"\n");
	}
	if(StringUtil.notNull(voucherStatus)){
	sql.append("    AND COMPLAINT.CP_STATUS = "+voucherStatus+"\n");
	}
	if(StringUtil.notNull(acceptStart)){
	sql.append("    AND COMPLAINT.CP_ACC_DATE >= TO_DATE('"+acceptStart+"', 'YYYY-MM-DD')\n");
	}
	if(StringUtil.notNull(acceptEnd)){
	sql.append("    AND COMPLAINT.CP_ACC_DATE <=\n");
	sql.append("        TO_DATE('"+acceptEnd+" 23:59:59', 'YYYY-MM-DD HH24:MI:SS')\n");
	}
	if(StringUtil.notNull(dealStart)){
	sql.append("    AND COMPLAINT.CP_CLOSE_DATE >= TO_DATE('"+dealStart+"', 'YYYY-MM-DD')\n");
	}
	if(StringUtil.notNull(cpNo)){
	sql.append("    AND COMPLAINT.CP_CLOSE_DATE <=\n");
	sql.append("        TO_DATE('"+dealEnd+" 23:59:59', 'YYYY-MM-DD HH24:MI:SS')\n");
	}
	if(StringUtil.notNull(level)){
	sql.append("    AND COMPLAINT.CP_LEVEL = "+level+"\n");
	}
	sql.append("    AND A.CD_USER_ID = "+userId+"\n");
	sql.append("  ORDER BY COMPLAINT.CP_ACC_DATE DESC");

	return dao.pageQuery(sql.toString(), null, getFunName(),pageSize, curPage);
}
	
public PageResult<Map<String, Object>> queryConsultInfoKh(Map<String, Object> map,Long userId,int pageSize,int curPage){
		
		String cpNo = CommonUtils.checkNull(map.get("cpNo")); 
		String voucherType = CommonUtils.checkNull(map.get("voucherType"));
		String voucherStatus = CommonUtils.checkNull(map.get("voucherStatus"));
		String level = CommonUtils.checkNull(map.get("level"));
		String acceptStart = CommonUtils.checkNull(map.get("acceptStart"));
		String acceptEnd = CommonUtils.checkNull(map.get("acceptEnd"));
		String dealStart = CommonUtils.checkNull(map.get("dealStart"));
		String dealEnd = CommonUtils.checkNull(map.get("dealEnd"));
		
		StringBuffer sql = new StringBuffer();

		sql.append(" SELECT COMPLAINT.CP_NO CPNO, /*单号*/\n");
		sql.append("        COMPLAINT.CP_ID CPID, /*ID*/\n");
		sql.append("        COMPLAINT.CP_LEVEL CPLEVEL, /*级别*/\n");
		sql.append("        COMPLAINT.CP_STATUS STATUS, /*状态*/\n");
		sql.append("        COMPLAINT.CP_BIZ_TYPE BIZTYPE, /*业务类型*/\n");
		sql.append("        COMPLAINT.CP_BIZ_CONTENT BIZCONT, /*需转换*/\n");
		sql.append("        TRP.REGION_NAME PRO, /*省份*/\n");
		sql.append("        TRC.REGION_NAME CITY, /*城市*/\n");
		sql.append("        COMPLAINT.CP_NAME CTMNAME, /*客户名称*/\n");
		sql.append("        COMPLAINT.CP_CUS_ID CTMID, /*客户ID*/\n");
		sql.append("        COMPLAINT.CP_PHONE PHONE, /*电话*/\n");
		sql.append("        COMPLAINT.CP_CONCAT_PERSON, /*联系人姓名*/\n");
		sql.append("        COMPLAINT.CP_CONCAT_PHONE, /*联系人电话*/\n");
		sql.append("        COMPLAINT.CP_CONTENT_TYPE_REMARK REMARK, /*备注*/\n");
		sql.append("        COMPLAINT.CP_ACC_DATE CPACCDATE, /*受理日期*/\n");
		sql.append("        ACUSER.NAME ACUSER, /*受理人*/\n");
		sql.append("        COMPLAINT.CP_VIN VIN,\n");
		sql.append("        TO_CHAR(COMPLAINT.CP_BUY_DATE, 'YYYY-MM-DD') BDATE, /*购买日期*/\n");
		sql.append("        TO_CHAR(COMPLAINT.CP_CLOSE_DATE, 'YYYY-MM-DD') CP_CLOSE_DATE, /*关闭日期*/\n");
		sql.append(" CASE WHEN COMPLAINT.CP_DEAL_ID="+userId+" THEN 0\n");
		sql.append("           ELSE 1 END ISSELF");
		sql.append("   FROM TT_CRM_COMPLAINT COMPLAINT\n");
		sql.append("   LEFT JOIN TM_REGION TRP\n");
		sql.append("     ON TRP.REGION_CODE = COMPLAINT.CP_PROVINCE_ID\n");
		sql.append("    AND TRP.REGION_TYPE = 10541002\n");
		sql.append("   LEFT JOIN TM_REGION TRC\n");
		sql.append("     ON TRC.REGION_CODE = COMPLAINT.CP_CITY_ID\n");
		sql.append("    AND TRC.REGION_TYPE = 10541003\n");
		sql.append("   LEFT JOIN TM_DEALER REOBJ\n");
		sql.append("     ON REOBJ.DEALER_ID = COMPLAINT.CP_OBJECT\n");
		sql.append("   LEFT JOIN TC_USER ACUSER\n");
		sql.append("     ON ACUSER.USER_ID = COMPLAINT.CP_ACC_USER\n");

		sql.append(" FULL JOIN （\r\n");
		sql.append("  SELECT DISTINCT T.CP_ID, T.CD_USER_ID\r\n");
		sql.append("    FROM TT_CRM_COMPLAINT_DEAL_RECORD T）A\r\n");
		sql.append("      ON COMPLAINT.CP_ID = A.CP_ID\r\n");
		sql.append("   WHERE 1 = 1\r\n");
		sql.append("   AND COMPLAINT.CP_STATUS NOT IN ("+Constant.VOUCHER_STATUS_01+")\r\n");
		sql.append("   and COMPLAINT.CP_STATUS ="+Constant.VOUCHER_STATUS_03+" or A.CD_USER_ID = "+userId+"\n");

		//查询条件
		if(StringUtil.notNull(cpNo)){
			sql.append("    AND COMPLAINT.CP_NO LIKE '%"+cpNo+"%'\n");
		}
		if(StringUtil.notNull(voucherType)){
		sql.append("    AND COMPLAINT.CP_BIZ_TYPE = "+voucherType+"\n");
		}
		if(StringUtil.notNull(voucherStatus)){
		sql.append("    AND COMPLAINT.CP_STATUS = "+voucherStatus+"\n");
		}
		if(StringUtil.notNull(acceptStart)){
		sql.append("    AND COMPLAINT.CP_ACC_DATE >= TO_DATE('"+acceptStart+"', 'YYYY-MM-DD')\n");
		}
		if(StringUtil.notNull(acceptEnd)){
		sql.append("    AND COMPLAINT.CP_ACC_DATE <=\n");
		sql.append("        TO_DATE('"+acceptEnd+" 23:59:59', 'YYYY-MM-DD HH24:MI:SS')\n");
		}
		if(StringUtil.notNull(dealStart)){
		sql.append("    AND COMPLAINT.CP_CLOSE_DATE >= TO_DATE('"+dealStart+"', 'YYYY-MM-DD')\n");
		}
		if(StringUtil.notNull(cpNo)){
		sql.append("    AND COMPLAINT.CP_CLOSE_DATE <=\n");
		sql.append("        TO_DATE('"+dealEnd+" 23:59:59', 'YYYY-MM-DD HH24:MI:SS')\n");
		}
		if(StringUtil.notNull(level)){
		sql.append("    AND COMPLAINT.CP_LEVEL = "+level+"\n");
		}
		sql.append("  ORDER BY COMPLAINT.CP_ACC_DATE DESC");

		return dao.pageQuery(sql.toString(), null, getFunName(),pageSize, curPage);
	}
	
public PageResult<Map<String, Object>> queryConsultInfoOrg(Map<String, Object> map,Long userId,int pageSize,int curPage){
	
	String cpNo = CommonUtils.checkNull(map.get("cpNo")); 
	String voucherType = CommonUtils.checkNull(map.get("voucherType"));
	String voucherStatus = CommonUtils.checkNull(map.get("voucherStatus"));
	String level = CommonUtils.checkNull(map.get("level"));
	String acceptStart = CommonUtils.checkNull(map.get("acceptStart"));
	String acceptEnd = CommonUtils.checkNull(map.get("acceptEnd"));
	String dealStart = CommonUtils.checkNull(map.get("dealStart"));
	String dealEnd = CommonUtils.checkNull(map.get("dealEnd"));
	
	StringBuffer sql = new StringBuffer();

	sql.append(" SELECT COMPLAINT.CP_NO CPNO, /*单号*/\n");
	sql.append("        COMPLAINT.CP_ID CPID, /*ID*/\n");
	sql.append("        COMPLAINT.CP_LEVEL CPLEVEL, /*级别*/\n");
	sql.append("        COMPLAINT.CP_STATUS STATUS, /*状态*/\n");
	sql.append("        COMPLAINT.CP_BIZ_TYPE BIZTYPE, /*业务类型*/\n");
	sql.append("        COMPLAINT.CP_BIZ_CONTENT BIZCONT, /*需转换*/\n");
	sql.append("        TRP.REGION_NAME PRO, /*省份*/\n");
	sql.append("        TRC.REGION_NAME CITY, /*城市*/\n");
	sql.append("        COMPLAINT.CP_NAME CTMNAME, /*客户名称*/\n");
	sql.append("        COMPLAINT.CP_CUS_ID CTMID, /*客户ID*/\n");
	sql.append("        COMPLAINT.CP_PHONE PHONE, /*电话*/\n");
	sql.append("        COMPLAINT.CP_CONCAT_PERSON, /*联系人姓名*/\n");
	sql.append("        COMPLAINT.CP_CONCAT_PHONE, /*联系人电话*/\n");
	sql.append("        COMPLAINT.CP_CONTENT_TYPE_REMARK REMARK, /*备注*/\n");
	sql.append("        COMPLAINT.CP_ACC_DATE CPACCDATE, /*受理日期*/\n");
	sql.append("        ACUSER.NAME ACUSER, /*受理人*/\n");
	sql.append("        COMPLAINT.CP_VIN VIN,\n");
	sql.append("        TO_CHAR(COMPLAINT.CP_BUY_DATE, 'YYYY-MM-DD') BDATE, /*购买日期*/\n");
	sql.append("        TO_CHAR(COMPLAINT.CP_CLOSE_DATE, 'YYYY-MM-DD') CP_CLOSE_DATE ,/*关闭日期*/\n");
	
	sql.append(" CASE WHEN COMPLAINT.CP_DEAL_ID="+userId+" THEN 0\n");
	sql.append("           ELSE 1 END ISSELF");

	sql.append("   FROM TT_CRM_COMPLAINT COMPLAINT\n");
	sql.append("   LEFT JOIN TM_REGION TRP\n");
	sql.append("     ON TRP.REGION_CODE = COMPLAINT.CP_PROVINCE_ID\n");
	sql.append("    AND TRP.REGION_TYPE = 10541002\n");
	sql.append("   LEFT JOIN TM_REGION TRC\n");
	sql.append("     ON TRC.REGION_CODE = COMPLAINT.CP_CITY_ID\n");
	sql.append("    AND TRC.REGION_TYPE = 10541003\n");
	sql.append("   LEFT JOIN TM_DEALER REOBJ\n");
	sql.append("     ON REOBJ.DEALER_ID = COMPLAINT.CP_OBJECT\n");
	sql.append("   LEFT JOIN TC_USER ACUSER\n");
	sql.append("     ON ACUSER.USER_ID = COMPLAINT.CP_ACC_USER\n");
	sql.append(" FULL JOIN （\r\n");
	sql.append("  SELECT DISTINCT T.CP_ID, T.CD_USER_ID\r\n");
	sql.append("    FROM TT_CRM_COMPLAINT_DEAL_RECORD T）A\r\n");
	sql.append("      ON COMPLAINT.CP_ID = A.CP_ID\r\n");
	sql.append("   WHERE 1 = 1\r\n");
	sql.append("   AND COMPLAINT.CP_STATUS NOT IN ("+Constant.VOUCHER_STATUS_01+")\r\n");
	sql.append("   and COMPLAINT.CP_STATUS ="+Constant.VOUCHER_STATUS_04+" or A.CD_USER_ID = "+userId+"\n");
	//查询条件
	if(StringUtil.notNull(cpNo)){
		sql.append("    AND COMPLAINT.CP_NO LIKE '%"+cpNo+"%'\n");
	}
	if(StringUtil.notNull(voucherType)){
	sql.append("    AND COMPLAINT.CP_BIZ_TYPE = "+voucherType+"\n");
	}
	if(StringUtil.notNull(voucherStatus)){
	sql.append("    AND COMPLAINT.CP_STATUS = "+voucherStatus+"\n");
	}
	if(StringUtil.notNull(acceptStart)){
	sql.append("    AND COMPLAINT.CP_ACC_DATE >= TO_DATE('"+acceptStart+"', 'YYYY-MM-DD')\n");
	}
	if(StringUtil.notNull(acceptEnd)){
	sql.append("    AND COMPLAINT.CP_ACC_DATE <=\n");
	sql.append("        TO_DATE('"+acceptEnd+" 23:59:59', 'YYYY-MM-DD HH24:MI:SS')\n");
	}
	if(StringUtil.notNull(dealStart)){
	sql.append("    AND COMPLAINT.CP_CLOSE_DATE >= TO_DATE('"+dealStart+"', 'YYYY-MM-DD')\n");
	}
	if(StringUtil.notNull(cpNo)){
	sql.append("    AND COMPLAINT.CP_CLOSE_DATE <=\n");
	sql.append("        TO_DATE('"+dealEnd+" 23:59:59', 'YYYY-MM-DD HH24:MI:SS')\n");
	}
	if(StringUtil.notNull(level)){
	sql.append("    AND COMPLAINT.CP_LEVEL = "+level+"\n");
	}
	sql.append("  ORDER BY COMPLAINT.CP_ACC_DATE DESC");

	return dao.pageQuery(sql.toString(), null, getFunName(),pageSize, curPage);
}

public PageResult<Map<String, Object>> queryConsultInfoDr(Map<String, Object> map,Long userId,int pageSize,int curPage){
	
	String cpNo = CommonUtils.checkNull(map.get("cpNo")); 
	String voucherType = CommonUtils.checkNull(map.get("voucherType"));
	String voucherStatus = CommonUtils.checkNull(map.get("voucherStatus"));
	String level = CommonUtils.checkNull(map.get("level"));
	String acceptStart = CommonUtils.checkNull(map.get("acceptStart"));
	String acceptEnd = CommonUtils.checkNull(map.get("acceptEnd"));
	String dealStart = CommonUtils.checkNull(map.get("dealStart"));
	String dealEnd = CommonUtils.checkNull(map.get("dealEnd"));
	
	StringBuffer sql = new StringBuffer();

	sql.append(" SELECT COMPLAINT.CP_NO CPNO, /*单号*/\n");
	sql.append("        COMPLAINT.CP_ID CPID, /*ID*/\n");
	sql.append("        COMPLAINT.CP_LEVEL CPLEVEL, /*级别*/\n");
	sql.append("        COMPLAINT.CP_STATUS STATUS, /*状态*/\n");
	sql.append("        COMPLAINT.CP_BIZ_TYPE BIZTYPE, /*业务类型*/\n");
	sql.append("        COMPLAINT.CP_BIZ_CONTENT BIZCONT, /*需转换*/\n");
	sql.append("        TRP.REGION_NAME PRO, /*省份*/\n");
	sql.append("        TRC.REGION_NAME CITY, /*城市*/\n");
	sql.append("        COMPLAINT.CP_NAME CTMNAME, /*客户名称*/\n");
	sql.append("        COMPLAINT.CP_CUS_ID CTMID, /*客户ID*/\n");
	sql.append("        COMPLAINT.CP_PHONE PHONE, /*电话*/\n");
	sql.append("        COMPLAINT.CP_CONCAT_PERSON, /*联系人姓名*/\n");
	sql.append("        COMPLAINT.CP_CONCAT_PHONE, /*联系人电话*/\n");
	sql.append("        COMPLAINT.CP_CONTENT_TYPE_REMARK REMARK, /*备注*/\n");
	sql.append("        COMPLAINT.CP_ACC_DATE CPACCDATE, /*受理日期*/\n");
	sql.append("        ACUSER.NAME ACUSER, /*受理人*/\n");
	sql.append("        COMPLAINT.CP_VIN VIN,\n");
	sql.append("        TO_CHAR(COMPLAINT.CP_BUY_DATE, 'YYYY-MM-DD') BDATE, /*购买日期*/\n");
	sql.append("        TO_CHAR(COMPLAINT.CP_CLOSE_DATE, 'YYYY-MM-DD') CP_CLOSE_DATE, /*关闭日期*/\n");
	sql.append(" CASE WHEN COMPLAINT.CP_DEAL_ID="+userId+" THEN 0\n");
	sql.append("           ELSE 1 END ISSELF");

	sql.append("   FROM TT_CRM_COMPLAINT COMPLAINT\n");
	sql.append("   LEFT JOIN TM_REGION TRP\n");
	sql.append("     ON TRP.REGION_CODE = COMPLAINT.CP_PROVINCE_ID\n");
	sql.append("    AND TRP.REGION_TYPE = 10541002\n");
	sql.append("   LEFT JOIN TM_REGION TRC\n");
	sql.append("     ON TRC.REGION_CODE = COMPLAINT.CP_CITY_ID\n");
	sql.append("    AND TRC.REGION_TYPE = 10541003\n");
	sql.append("   LEFT JOIN TM_DEALER REOBJ\n");
	sql.append("     ON REOBJ.DEALER_ID = COMPLAINT.CP_OBJECT\n");
	sql.append("   LEFT JOIN TC_USER ACUSER\n");
	sql.append("     ON ACUSER.USER_ID = COMPLAINT.CP_ACC_USER\n");
	sql.append(" FULL JOIN （\r\n");
	sql.append("  SELECT DISTINCT T.CP_ID, T.CD_USER_ID\r\n");
	sql.append("    FROM TT_CRM_COMPLAINT_DEAL_RECORD T）A\r\n");
	sql.append("      ON COMPLAINT.CP_ID = A.CP_ID\r\n");
	sql.append("   WHERE 1 = 1\r\n");
	sql.append("   AND COMPLAINT.CP_STATUS NOT IN ("+Constant.VOUCHER_STATUS_01+")\r\n");
	sql.append("   and COMPLAINT.CP_STATUS ="+Constant.VOUCHER_STATUS_05+" or A.CD_USER_ID = "+userId+"\n");
	//查询条件
	if(StringUtil.notNull(cpNo)){
		sql.append("    AND COMPLAINT.CP_NO LIKE '%"+cpNo+"%'\n");
	}
	if(StringUtil.notNull(voucherType)){
	sql.append("    AND COMPLAINT.CP_BIZ_TYPE = "+voucherType+"\n");
	}
	if(StringUtil.notNull(voucherStatus)){
	sql.append("    AND COMPLAINT.CP_STATUS = "+voucherStatus+"\n");
	}
	if(StringUtil.notNull(acceptStart)){
	sql.append("    AND COMPLAINT.CP_ACC_DATE >= TO_DATE('"+acceptStart+"', 'YYYY-MM-DD')\n");
	}
	if(StringUtil.notNull(acceptEnd)){
	sql.append("    AND COMPLAINT.CP_ACC_DATE <=\n");
	sql.append("        TO_DATE('"+acceptEnd+" 23:59:59', 'YYYY-MM-DD HH24:MI:SS')\n");
	}
	if(StringUtil.notNull(dealStart)){
	sql.append("    AND COMPLAINT.CP_CLOSE_DATE >= TO_DATE('"+dealStart+"', 'YYYY-MM-DD')\n");
	}
	if(StringUtil.notNull(cpNo)){
	sql.append("    AND COMPLAINT.CP_CLOSE_DATE <=\n");
	sql.append("        TO_DATE('"+dealEnd+" 23:59:59', 'YYYY-MM-DD HH24:MI:SS')\n");
	}
	if(StringUtil.notNull(level)){
	sql.append("    AND COMPLAINT.CP_LEVEL = "+level+"\n");
	}
	sql.append("  ORDER BY COMPLAINT.CP_ACC_DATE DESC");

	return dao.pageQuery(sql.toString(), null, getFunName(),pageSize, curPage);
}
	public Map<String, Object> queryComplaintInformation(long cpid) {
		StringBuffer sql = new StringBuffer();
		
		sql.append(" SELECT COMPLAINT.CP_NO CPNO, /*单号*/\n");
		sql.append("        COMPLAINT.CP_ID CPID, /*ID*/\n");
		sql.append("        COMPLAINT.CP_LEVEL CPLEVEL, /*级别*/\n");
		sql.append("        COMPLAINT.CP_STATUS STATUS, /*状态*/\n");
		sql.append("        COMPLAINT.CP_BIZ_TYPE BIZTYPE, /*业务类型*/\n");
		sql.append("        COMPLAINT.CP_BIZ_CONTENT BIZCONT, /*需转换*/\n");
		sql.append("        COMPLAINT.CP_MILEAGE MILEAGE,\n");
		sql.append("        COMPLAINT.CP_PROVINCE_ID PRO, /*省份*/\n");
		sql.append("        COMPLAINT.CP_CITY_ID CITY, /*城市*/\n");
		sql.append("        COMPLAINT.CP_NAME CTMNAME, /*客户名称*/\n");
		sql.append("        COMPLAINT.CP_CUS_ID CTMID, /*客户ID*/\n");
		sql.append("        COMPLAINT.CP_PHONE PHONE, /*电话*/\n");
		sql.append("        COMPLAINT.CP_CONCAT_PERSON PERSON, /*联系人姓名*/\n");
		sql.append("        COMPLAINT.CP_CONCAT_PHONE CPPHONE, /*联系人电话*/\n");
		sql.append("        COMPLAINT.CP_CONTENT_TYPE_REMARK REMARK, /*备注*/\n");
		sql.append("        COMPLAINT.CP_VIN VIN,\n");
		sql.append("        COMPLAINT.CP_VIN_USE CPUSER,\n");
		sql.append("        TO_CHAR(COMPLAINT.CP_PRODUCT_DATE, 'YYYY-MM-DD') SDATE,\n");
		sql.append("        COMPLAINT.CP_CONTENT CPCONT,\n");
		sql.append("        COMPLAINT.CP_OBJECT OBJECT,\n");
		sql.append("        COMPLAINT.CP_SERIES_ID SERIESID,\n");
		sql.append("        COMPLAINT.CP_MODEL_ID MODELID,\n");
		sql.append("        COMPLAINT.CP_VIN_NATURE NATURE,\n");
		sql.append("        COMPLAINT.CP_OBJECT_SMALL_ORG SMALLORG,\n");
		sql.append("        COMPLAINT.CP_HAS_CAR_CUSTOMER CUSTOMER,\n");
		sql.append("        COMPLAINT.CP_OBJECT_ORG OBJECTORG,\n");
		sql.append("        COMPLAINT.CP_OBJECT_SMALL_ORG OBJECTSMALLORG,\n");
		sql.append("        TO_CHAR(COMPLAINT.CP_BUY_DATE, 'YYYY-MM-DD') BDATE /*购买日期*/\n");
		sql.append("   FROM TT_CRM_COMPLAINT COMPLAINT\n");
		sql.append("  WHERE 1 = 1\n");
		sql.append("    AND COMPLAINT.CP_ID = "+cpid+"");


		return this.pageQueryMap(sql.toString(), null, null);
	}
	
	
	public Map<String, Object> queryComplaintInfor(long cpid) {
		StringBuffer sql = new StringBuffer();


		sql.append(" SELECT COMPLAINT.CP_NO CPNO, /*单号*/\n");
		sql.append("         COMPLAINT.CP_ID CPID, /*ID*/\n");
		sql.append("         F_GET_TCCODE_DESC(COMPLAINT.CP_LEVEL) CPLEVEL, /*级别*/\n");
		sql.append("         COMPLAINT.CP_STATUS STATUS, /*状态*/\n");
		sql.append("         F_GET_TCCODE_DESC(COMPLAINT.CP_BIZ_TYPE) BIZTYPE,\n");
		sql.append("         COMPLAINT.CP_BIZ_TYPE CPBIZTYPE, /*业务类型*/\n");
		sql.append("         F_GET_TCCODE_DESC(COMPLAINT.CP_BIZ_CONTENT) BIZCONT,\n");
		sql.append("         COMPLAINT.CP_BIZ_CONTENT CPBIZCONTENT, /*需转换*/\n");
		sql.append("         COMPLAINT.CP_MILEAGE MILEAGE,\n");
		sql.append("         TRP.REGION_NAME PRO, /*省份*/\n");
		sql.append("         TRC.REGION_NAME CITY, /*城市*/\n");
		sql.append("         COMPLAINT.CP_NAME CTMNAME, /*客户名称*/\n");
		sql.append("         COMPLAINT.CP_CUS_ID CTMID, /*客户ID*/\n");
		sql.append("         COMPLAINT.CP_PHONE PHONE, /*电话*/\n");
		sql.append("         COMPLAINT.CP_CONCAT_PERSON PERSON, /*联系人姓名*/\n");
		sql.append("         COMPLAINT.CP_CONCAT_PHONE CPPHONE, /*联系人电话*/\n");
		sql.append("         COMPLAINT.CP_CONTENT_TYPE_REMARK REMARK, /*备注*/\n");
		sql.append("         COMPLAINT.CP_VIN VIN,\n");
		sql.append("         TCUSER.NAME ACCUSER,\n");
		sql.append("         TO_CHAR(COMPLAINT.CP_ACC_DATE, 'yyyy-MM-dd hh24:mi:ss') ACCDATE,\n");
		sql.append("          F_GET_TCCODE_DESC(COMPLAINT.CP_VIN_USE) CPUSER,\n");
		sql.append("         TO_CHAR(COMPLAINT.CP_PRODUCT_DATE, 'YYYY-MM-DD') SDATE,\n");
		sql.append("         COMPLAINT.CP_CONTENT CPCONT,\n");
		sql.append("         CPMODEL1.GROUP_NAME SERIESID,\n");
		sql.append("         CPMODEL.GROUP_NAME MODELID,\n");
		sql.append("         F_GET_TCCODE_DESC(COMPLAINT.CP_VIN_NATURE) NATURE,\n");
		sql.append("         COMPLAINT.CP_HAS_CAR_CUSTOMER CUSTOMER,\n");
		sql.append("         COMPLAINT.CP_OBJECT_ORG OBJECTORG,\n");
		sql.append("         COMPLAINT.CP_OBJECT_SMALL_ORG OBJECTSMALLORG,\n");
		sql.append("         TO_CHAR(COMPLAINT.CP_BUY_DATE, 'YYYY-MM-DD') BDATE, /*购买日期*/\n");
		sql.append("         decode(t.dealer_name,'',(select t.org_name from tm_org_custom t where t.org_id=COMPLAINT.Cp_Object),t.dealer_name ) OBJECT\n");
		sql.append("    FROM TT_CRM_COMPLAINT COMPLAINT\n");
		sql.append("    LEFT JOIN TM_REGION TRP\n");
		sql.append("      ON TRP.REGION_CODE = COMPLAINT.CP_PROVINCE_ID\n");
		sql.append("     AND TRP.REGION_TYPE = 10541002\n");
		sql.append("    LEFT JOIN TM_REGION TRC\n");
		sql.append("      ON TRC.REGION_CODE = COMPLAINT.CP_CITY_ID\n");
		sql.append("     AND TRC.REGION_TYPE = 10541003\n");
		sql.append("    LEFT JOIN TM_VHCL_MATERIAL_GROUP CPMODEL\n");
		sql.append("      ON CPMODEL.GROUP_ID = COMPLAINT.CP_MODEL_ID\n");
		sql.append("    LEFT JOIN TM_VHCL_MATERIAL_GROUP CPMODEL1\n");
		sql.append("      ON CPMODEL1.GROUP_ID = COMPLAINT.CP_SERIES_ID\n");
		sql.append("    LEFT JOIN TC_USER TCUSER\n");
		sql.append("      ON TCUSER.USER_ID = COMPLAINT.CP_ACC_USER\n");
		sql.append("    left join tm_dealer t\n");
		sql.append("    on t.dealer_id= COMPLAINT.Cp_Object\n");
		sql.append("   WHERE 1 = 1");

		sql.append("    AND COMPLAINT.CP_ID = "+cpid+"");

		return this.pageQueryMap(sql.toString(), null, null);
	}

	
	public Map<String, Object> queryComplaintInfor2(long cpid) {
		StringBuffer sql = new StringBuffer();
		
		sql.append(" SELECT COMPLAINT.CP_NO CPNO, /*单号*/\n");
		sql.append("         COMPLAINT.CP_ID CPID, /*ID*/\n");
		sql.append("         F_GET_TCCODE_DESC(COMPLAINT.CP_LEVEL) CPLEVEL, /*级别*/\n");
		sql.append("         COMPLAINT.CP_STATUS STATUS, /*状态*/\n");
		sql.append("         F_GET_TCCODE_DESC(COMPLAINT.CP_BIZ_TYPE) BIZTYPE,\n");
		sql.append("         COMPLAINT.CP_BIZ_TYPE CPBIZTYPE, /*业务类型*/\n");
		sql.append("         F_GET_TCCODE_DESC(COMPLAINT.CP_BIZ_CONTENT) BIZCONT,\n");
		sql.append("         COMPLAINT.CP_BIZ_CONTENT CPBIZCONTENT, /*需转换*/\n");
		sql.append("         COMPLAINT.CP_MILEAGE MILEAGE,\n");
		sql.append("         TRP.REGION_NAME PRO, /*省份*/\n");
		sql.append("         TRC.REGION_NAME CITY, /*城市*/\n");
		sql.append("         COMPLAINT.CP_NAME CTMNAME, /*客户名称*/\n");
		sql.append("         COMPLAINT.CP_CUS_ID CTMID, /*客户ID*/\n");
		sql.append("         COMPLAINT.CP_PHONE PHONE, /*电话*/\n");
		sql.append("         COMPLAINT.CP_CONCAT_PERSON PERSON, /*联系人姓名*/\n");
		sql.append("         COMPLAINT.CP_CONCAT_PHONE CPPHONE, /*联系人电话*/\n");
		sql.append("         COMPLAINT.CP_CONTENT_TYPE_REMARK REMARK, /*备注*/\n");
		sql.append("         COMPLAINT.CP_VIN VIN,\n");
		sql.append("         TCUSER.NAME ACCUSER,\n");
		sql.append("         COMPLAINT.CP_ACC_DATE ACCDATE,\n");
		sql.append("         COMPLAINT.CP_VIN_USE CPUSER,\n");
		sql.append("         TO_CHAR(COMPLAINT.CP_PRODUCT_DATE, 'YYYY-MM-DD') SDATE,\n");
		sql.append("         COMPLAINT.CP_CONTENT CPCONT,\n");
		sql.append("         CPMODEL1.GROUP_NAME SERIESID,\n");
		sql.append("         CPMODEL.GROUP_NAME MODELID,\n");
		sql.append("         F_GET_TCCODE_DESC(COMPLAINT.CP_VIN_NATURE) NATURE,\n");
		sql.append("         COMPLAINT.CP_HAS_CAR_CUSTOMER CUSTOMER,\n");
		sql.append("         COMPLAINT.CP_OBJECT_ORG OBJECTORG,\n");
		sql.append("         COMPLAINT.CP_OBJECT_SMALL_ORG OBJECTSMALLORG,\n");
		sql.append("         TO_CHAR(COMPLAINT.CP_BUY_DATE, 'YYYY-MM-DD') BDATE, /*购买日期*/\n");
		sql.append("         decode(t.dealer_name,'',(select t.org_name from tm_org_custom t where t.org_id=COMPLAINT.Cp_Object),t.dealer_name ) OBJECT\n");
		sql.append("    FROM TT_CRM_COMPLAINT COMPLAINT\n");
		sql.append("    LEFT JOIN TM_REGION TRP\n");
		sql.append("      ON TRP.REGION_CODE = COMPLAINT.CP_PROVINCE_ID\n");
		sql.append("     AND TRP.REGION_TYPE = 10541002\n");
		sql.append("    LEFT JOIN TM_REGION TRC\n");
		sql.append("      ON TRC.REGION_CODE = COMPLAINT.CP_CITY_ID\n");
		sql.append("     AND TRC.REGION_TYPE = 10541003\n");
		sql.append("    LEFT JOIN TM_VHCL_MATERIAL_GROUP CPMODEL\n");
		sql.append("      ON CPMODEL.GROUP_ID = COMPLAINT.CP_MODEL_ID\n");
		sql.append("    LEFT JOIN TM_VHCL_MATERIAL_GROUP CPMODEL1\n");
		sql.append("      ON CPMODEL1.GROUP_ID = COMPLAINT.CP_SERIES_ID\n");
		sql.append("    LEFT JOIN TC_USER TCUSER\n");
		sql.append("      ON TCUSER.USER_ID = COMPLAINT.CP_ACC_USER\n");
		sql.append("    left join tm_dealer t\n");
		sql.append("    on t.dealer_id= COMPLAINT.Cp_Object\n");
		sql.append("   WHERE 1 = 1");

		sql.append("    AND COMPLAINT.CP_ID = "+cpid+"");

		return this.pageQueryMap(sql.toString(), null, null);
	}

	public Map<String, Object> queryComplaintRecord(long cpid,String status) {
		StringBuffer sql = new StringBuffer();

		sql.append(" SELECT T.CP_STATUS, T.CP_CONTENT, MAX(T.Cd_Id)\n");
		sql.append("   FROM TT_CRM_COMPLAINT_DEAL_RECORD T\n");
		sql.append("  WHERE T.CP_ID = "+cpid+"\n");
		sql.append("    AND T.CP_STATUS in( "+status+","+Constant.VOUCHER_STATUS_06+")\n");
		sql.append("  GROUP BY T.CP_STATUS, T.CP_CONTENT,T.Cd_Id");
		return this.pageQueryMap(sql.toString(), null, null);
	}
	
	/**
	 * 方法描述 ： 查询区域组织信息<br/>
	 * 
	 * @param level
	 *            2 大区， 3 小区
	 * @param type
	 *            10191001 主机厂、10191002 经销商
	 * @return
	 * @author wangsongwei
	 */
	public List<Map<String, Object>> getOrgList(int level, Integer type) throws Exception
	{
		StringBuffer sbSql = new StringBuffer();
		
		sbSql.append("SELECT T.ORG_ID, T.ORG_CODE,T.ORG_NAME FROM TM_ORG T WHERE T.ORG_TYPE = " + type + " AND T.ORG_LEVEL = " + level + " AND T.STATUS = " + Constant.STATUS_ENABLE
						+ " ORDER BY T.name_sort");
		
		return dao.pageQuery(sbSql.toString(), null, getFunName());
	}

	
	/**
	 * 查询处理结果数据
	 * @param cpid
	 * @return
	 */
	public List<Map<String, Object>> queryDealRecord(long cpid) {
		StringBuffer sql = new StringBuffer();

		sql.append(" SELECT TO_CHAR(T.CD_DATE, 'yyyy-MM-dd hh24:mi:ss') CDDATE,\n");
		sql.append("        T.CP_CONTENT CDCONT,\n");
		sql.append("        T.CD_USER USERNAME,\n");
		sql.append("        F_GET_TCCODE_DESC(T.CP_STATUS) STATUS\n");
		sql.append("   FROM TT_CRM_COMPLAINT_DEAL_RECORD T\n");
		sql.append("   LEFT JOIN TM_ORG A\n");
		sql.append("     ON A.ORG_ID = T.CP_NEXT_DEAL_ID\n");
		sql.append("   LEFT JOIN TM_DEALER B\n");
		sql.append("     ON B.DEALER_ID = T.CP_NEXT_DEAL_ID\n");
		sql.append("   LEFT JOIN TM_ORG_CUSTOM C\n");
		sql.append("     ON C.ORG_ID = T.CP_NEXT_DEAL_ID\n");
		sql.append("   LEFT JOIN TC_USER D\n");
		sql.append("     ON D.USER_ID = T.CP_NEXT_DEAL_ID\n");
		sql.append("  WHERE T.CP_ID = "+cpid+"\n");
		sql.append("  ORDER BY T.CD_DATE");

		return this.pageQuery(sql.toString(), null, null);
	}
}
