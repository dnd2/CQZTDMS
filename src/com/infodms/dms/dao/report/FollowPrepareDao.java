package com.infodms.dms.dao.report;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.common.Utility;
import com.infodms.dms.dao.common.BaseDao;
import com.infoservice.po3.bean.PO;

public class FollowPrepareDao extends BaseDao{
	public static final Logger logger = Logger.getLogger(FollowPrepareDao.class);
	
	public static final FollowPrepareDao dao = new FollowPrepareDao();
	public static FollowPrepareDao getInstance(){
		return dao;
		
	}
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public List<Map<String, Object>> getFollowPrepareSelect(Map<String, String> map){
		String followDate1 = map.get("followDate1");
		String followDate2 = map.get("followDate2");
		String orgId = map.get("orgId");
		List<Map<String, Object>> list = null;
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT /*+ all_rows */ DISTINCT TMF.FLEET_ID,\n");
		sql.append("  B.ORG_NAME, --大区\n");
		sql.append(" TMR.REGION_NAME, --省 \n");
		sql.append("TO_CHAR(TMF.SUBMIT_DATE, 'YYYY-MM-DD') SUBMIT_DATE, --报备日期\n");
		sql.append("TMC.COMPANY_SHORTNAME COMPANY_NAME, --申请 单位\n");
		sql.append("TMF.FLEET_NAME, --客户名称\n");
		sql.append("TC.CODE_DESC FLEET_TYPE, --客户类型\n");
		sql.append(" TC1.CODE_DESC MAIN_BUSINESS, --主营业务\n");
		sql.append("TMF.ZIP_CODE, --邮编\n");
		sql.append("TMF.ADDRESS, --详细地址\n");
		sql.append("TMF.MAIN_LINKMAN, --主要联系人\n");
		sql.append("TMF.MAIN_JOB, --职务\n");
		sql.append("TMF.MAIN_PHONE, --主要联系人电话\n");
		sql.append("NVL(TVMG.GROUP_NAME, '全系') GROUP_NAME, --车系\n");
		sql.append("TMF.SERIES_COUNT, --数量\n");
		sql.append("TMF.REQ_REMARK, --备注\n");
		sql.append(" TO_CHAR(TFF.FOLLOW_DATE, 'YYYY-MM-DD') FOLLOW_DATE, --跟进日期\n");
		sql.append(" TFF.FOLLOW_REMARK --跟进内容\n");
		sql.append("FROM TM_FLEET               TMF,\n");
		sql.append("TM_COMPANY             TMC,\n");
		sql.append("TM_DEALER              A,\n");
		sql.append("TM_ORG                 B,\n");
		sql.append(" TM_DEALER_ORG_RELATION C,\n");
		sql.append("TM_REGION              TMR,\n");
		sql.append("TM_FLEET_FOLLOW        TFF,\n");
		sql.append("TM_VHCL_MATERIAL_GROUP TVMG,\n");
		sql.append("TC_CODE                TC,\n");
		sql.append(" TC_CODE                TC1 \n");
		sql.append("WHERE TMC.COMPANY_ID = TMF.DLR_COMPANY_ID\n");
		sql.append("AND TC.CODE_ID = TMF.FLEET_TYPE\n");
		sql.append(" AND TMF.FLEET_ID = TFF.FLEET_ID\n");
		sql.append("AND TC1.CODE_ID(+)=TMF.MAIN_BUSINESS\n");
		sql.append("AND TMF.DLR_COMPANY_ID = A.COMPANY_ID\n");
		sql.append("AND TVMG.GROUP_ID(+) = TMF.SERIES_ID\n");
		sql.append(" AND TMR.REGION_CODE(+) = TMF.REGION\n");
		sql.append("AND f_get_pid(A.DEALER_ID) = C.DEALER_ID\n");
		sql.append("AND B.ORG_ID = C.ORG_ID\n");
		sql.append("AND B.IS_COUNT = 0\n");
		/*sql.append("AND A.DEALER_LEVEL = 10851001\n");*/
		sql.append(" AND TMF.STATUS = 11021003\n");
		if(Utility.testString(followDate1)){
			sql.append("AND TO_DATE(TO_CHAR(TFF.FOLLOW_DATE, 'YYYY-MM-DD'),'yyyy-mm-dd') >=  TO_DATE('"+followDate1+"','yyyy-mm-dd')\n");
		}
		if(Utility.testString(followDate2)){
			sql.append("AND (TO_DATE('"+followDate2+"','yyyy-mm-dd')) >=  TO_DATE(TO_CHAR(TFF.FOLLOW_DATE, 'YYYY-MM-DD'),'yyyy-mm-dd')\n");
		}
		if(Utility.testString(orgId)){
			sql.append("AND B.ORG_ID IN ("+orgId+")\n");
		}
		sql.append("and exists\n");
		sql.append(" (select 1 from tm_fleet_follow ff where ff.fleet_id = tmf.fleet_id)\n");
		sql.append("order by ORG_NAME,REGION_NAME,COMPANY_NAME\n");
		
		list = super.pageQuery(sql.toString(),null, getFunName());
		
		
		
		
		return list;
	}

}
