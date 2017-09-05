package com.infodms.dms.dao.sales.fleetmanage.fleetinfomanage;

import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.infodms.dms.bean.FleetInfoBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.po.TmFleetPO;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.po3.bean.PageResult;


/**
 * 
 * <p>Title:FleetInfoAppDao.java</p>
 *
 * <p>Description: 集团客户信息报备持久化层</p>
 *
 * <p>Copyright: Copyright (c) 2010</p>
 *
 * <p>Company: www.infoservice.com.cn</p>
 * <p>Date:2010-6-9</p>
 *
 * @author zouchao
 * @version 1.0
 * @remark
 */
public class FleetInfoAppDao extends BaseDao<TmFleetPO>{
	
	private static final FleetInfoAppDao dao = new FleetInfoAppDao();
	
	public static final FleetInfoAppDao getInstance() {
		return dao;
	}
	protected TmFleetPO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}
    
	/**
	 * 新增大用户报备信息
	 * @param po
	 */
	public void saveFleetInfo(TmFleetPO po){
		dao.insert(po);
	}
	
	/**
	 * 更改集团客户报备信息
	 * @param po1
	 * @param po2
	 */
	public void updateFleetInfo(TmFleetPO po1,TmFleetPO po2){
		dao.update(po1, po2);
	}
	
	
	/**
	 * 集团客户信息报备查询
	 * @param bean
	 * @param pageSize
	 * @param curPage
	 * @return
	 */
	public PageResult<Map<String, Object>> queryFleetInfo(FleetInfoBean bean,int pageSize,int curPage){
		StringBuffer sql= new StringBuffer();
		sql.append(" select * from (");
		sql.append(" SELECT DISTINCT TF.FLEET_ID,\n" );
		sql.append("                TF.FLEET_CODE,\n");
		sql.append("                TF.FLEET_NAME,\n" );
		sql.append("                TF.FLEET_TYPE,\n" );
		sql.append("                TF.REGION,\n" );
		sql.append("                TF.MAIN_LINKMAN,\n" );
		sql.append("                TF.MAIN_PHONE,\n" );
		sql.append("                TF.STATUS,\n" );
		sql.append("                TF.SUBMIT_USER,\n" );
		sql.append("                TO_CHAR(TF.SUBMIT_DATE, 'YYYY-MM-DD') AS SUBMIT_DATE,\n" );
		sql.append("                TO_CHAR(TF.AUDIT_DATE, 'YYYY-MM-DD') AS AUDIT_DATE,\n" );
		sql.append("                TF.AUDIT_REMARK,\n" );
		sql.append("                TF.MAIN_BUSINESS,\n" );
		sql.append("                TF.FUND_SIZE,\n" );
		sql.append("                TF.STAFF_SIZE,\n" );
		sql.append("                TF.PURPOSE,\n" );
		sql.append("                TF.ADDRESS,\n" );
		sql.append("                TF.MAIN_JOB,\n" );
		sql.append("                TF.ZIP_CODE,\n" );
		sql.append("                TF.MAIN_EMAIL,\n" );
		sql.append("                TF.OTHER_LINKMAN,\n" );
		sql.append("                TF.OTHER_PHONE,\n" );
		sql.append("                TF.OTHER_JOB,\n" );
		sql.append("                TF.OTHER_EMAIL,\n" );
		sql.append("                NVL(D.GROUP_NAME, '全系') GROUP_NAME,\n" );
		sql.append("                TF.SERIES_COUNT,\n" );
		sql.append("                TF.REQ_REMARK,\n" );
		sql.append("                F.ORG_NAME,\n" );
		sql.append("                TU.NAME,\n" );
		sql.append("                TR.NAME AUDITNAME,\n" );
		sql.append("                TCOM.COMPANY_SHORTNAME,\n" );
		sql.append("                 C.TOTAL REQUEST_COUNT,  ");
		sql.append("                 D.FINISH_TOTAL FINISH_COUNT   ");
		sql.append("  FROM TM_FLEET TF,\n" );
		sql.append("       TC_USER TU,\n" );
		sql.append("       TC_USER TR,\n" );
		sql.append("       TM_COMPANY TCOM,\n" );
		sql.append("       TM_VHCL_MATERIAL_GROUP D,\n" );
		sql.append("       TM_DEALER E,\n" );
		sql.append("       TM_ORG F,\n" );
		sql.append("       TM_DEALER_ORG_RELATION G,vw_org_dealer VOD,\n" );
		
		
		sql.append("(SELECT FLEET_ID, TOTAL\n" );
		sql.append("           FROM (SELECT TF.FLEET_ID FLEET_ID, SUM(NVL(TFRD.AMOUNT, 0)) TOTAL\n" );
		sql.append("                   FROM TM_FLEET TF\n" );
		sql.append("                   JOIN TM_FLEET_REQUEST_DETAIL TFRD\n" );
		sql.append("                     ON TFRD.FLEET_ID = TF.FLEET_ID\n" );
		sql.append("                  GROUP BY TF.FLEET_ID)) C,\n" );
		sql.append("          (SELECT FLEET_ID,NVL(FINISH_TOTAL,0) FINISH_TOTAL  FROM( SELECT TF.FLEET_ID, COUNT(*) FINISH_TOTAL\n" );
		sql.append("   FROM TT_DEALER_ACTUAL_SALES TDAS\n" );
		sql.append("   JOIN TM_FLEET TF\n" );
		sql.append("     ON TDAS.FLEET_ID = TF.FLEET_ID\n" );
		sql.append("    AND TDAS.FLEET_ID IS NOT NULL\n" );
		sql.append("    AND TDAS.IS_RETURN = 10041002\n" );
		sql.append("    AND TDAS.IS_FLEET = 10041001\n" );
		sql.append("  GROUP BY TF.FLEET_ID))    D");

		
		
		sql.append(" WHERE TF.IS_DEL = 0\n" );
		sql.append("   AND TF.SUBMIT_USER = TU.USER_ID(+)\n" );
		sql.append("   AND TF.AUDIT_USER_ID = TR.USER_ID(+)");
		sql.append("   AND TF.SERIES_ID = D.GROUP_ID(+)\n" );
		sql.append("   AND TF.DLR_COMPANY_ID = E.COMPANY_ID\n" );
		sql.append("   AND VOD.DEALER_ID=E.DEALER_ID");
		sql.append("   AND VOD.ROOT_DEALER_ID = G.DEALER_ID\n" );
		sql.append("   AND VOD.ROOT_ORG_ID=F.ORG_ID\n");
		sql.append("   AND D.FLEET_ID(+)=TF.FLEET_ID\n");
		sql.append("  AND  C.FLEET_ID(+)=TF.FLEET_ID\n");
	//	sql.append("   AND F.ORG_ID = G.ORG_ID\n" );
	//	sql.append("   AND F.IS_COUNT = 0\n");
		sql.append("   AND TF.DLR_COMPANY_ID = TCOM.COMPANY_ID\n" );
		sql.append("   AND TCOM.COMPANY_TYPE = ").append(Constant.COMPANY_TYPE_DEALER).append("\n");
		if(null!= bean.getDlrCompanyId()&&!"".equals(bean.getDlrCompanyId())){
			sql.append("   AND TF.DLR_COMPANY_ID IN(");
			sql.append(bean.getDlrCompanyId());
			sql.append(")\n");
		}else{
			sql.append("   AND TF.STATUS <>");
			sql.append(Constant.FLEET_INFO_TYPE_01);
			sql.append("\n");
		}
		if(Utility.testString(bean.getDutyType())){
			if(bean.getDutyType().equals(String.valueOf(Constant.DUTY_TYPE_LARGEREGION))){
				sql.append("AND F.ORG_ID = ").append(bean.getOrgId());
			}
			if(bean.getDutyType().equals(String.valueOf(Constant.DUTY_TYPE_SMALLREGION))){
				sql.append(" and E.DEALER_ID IN ");
				sql.append("  (SELECT DEALER_ID FROM vw_org_dealer ");
				sql.append("WHERE PQ_ORG_ID="+bean.getOrgId()+")");
			}
		}
		if(null!=bean.getBeginTime()&&!"".equals(bean.getBeginTime())){
			sql.append("   AND TF.SUBMIT_DATE >= TO_DATE('" );
			sql.append(bean.getBeginTime());
			sql.append(" 00:00:00','YYYY-MM-DD HH24:MI:SS')\n");
		}
		
		if(null!=bean.getEndTime()&&!"".equals(bean.getEndTime())){
			sql.append("   AND TF.SUBMIT_DATE <= TO_DATE('");
			sql.append(bean.getEndTime());
			sql.append(" 23:59:59','YYYY-MM-DD HH24:MI:SS')\n");
		}
		if(null!= bean.getDlrCompanyCode()&&!"".equals(bean.getDlrCompanyCode())&&!bean.getDlrCompanyCode().equals("''")){
			sql.append("AND TF.DLR_COMPANY_ID IN\n" );
			sql.append("       (SELECT DISTINCT TD.COMPANY_ID\n" );
			sql.append("          FROM TM_DEALER TD\n" );
			//sql.append("         WHERE TD.DEALER_TYPE = ").append(Constant.DEALER_TYPE_DVS).append("\n");
			sql.append("         WHERE TD.DEALER_CODE IN(").append(bean.getDlrCompanyCode()).append(")\n");
			sql.append("       )\n");
		}
		if(!"".equals(bean.getFleetName())){
			sql.append("   AND TF.FLEET_NAME LIKE '%" );
			sql.append(bean.getFleetName());
			sql.append("%'\n");
		}
		
		if(!"".equals(bean.getFleetType())){
			sql.append("   AND TF.FLEET_TYPE =" );
			sql.append(bean.getFleetType());
			sql.append("\n");
		}
		
		if(null!= bean.getStatus()&&!"".equals(bean.getStatus())){
			sql.append("   AND TF.STATUS IN(");
			sql.append(bean.getStatus());
			sql.append(")\n");
		}
		sql.append("  )  order by submit_date desc");
		PageResult<Map<String, Object>> ps = (PageResult<Map<String, Object>>) pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
		return ps;
		
	}
	/**
	 * 集团客户信息报备查询（小区）
	 * @param bean
	 * @param pageSize
	 * @param curPage
	 * @return
	 */
	public PageResult<Map<String, Object>> querySmallFleetInfo(FleetInfoBean bean,String orgId,int pageSize,int curPage){
		StringBuffer sql= new StringBuffer();

		sql.append(" SELECT DISTINCT TF.FLEET_ID,\n" );
		sql.append("                TF.FLEET_NAME,\n" );
		sql.append("                TF.FLEET_TYPE,\n" );
		sql.append("                TF.REGION,\n" );
		sql.append("                TF.MAIN_LINKMAN,\n" );
		sql.append("                TF.MAIN_PHONE,\n" );
		sql.append("                TF.STATUS,\n" );
		sql.append("                TF.SUBMIT_USER,\n" );
		sql.append("                TO_CHAR(TF.SUBMIT_DATE, 'YYYY-MM-DD') AS SUBMIT_DATE,\n" );
		sql.append("                TO_CHAR(TF.AUDIT_DATE, 'YYYY-MM-DD') AS AUDIT_DATE,\n" );
		sql.append("                TF.AUDIT_REMARK,\n" );
		sql.append("                TF.MAIN_BUSINESS,\n" );
		sql.append("                TF.FUND_SIZE,\n" );
		sql.append("                TF.STAFF_SIZE,\n" );
		sql.append("                TF.PURPOSE,\n" );
		sql.append("                TF.ADDRESS,\n" );
		sql.append("                TF.MAIN_JOB,\n" );
		sql.append("                TF.ZIP_CODE,\n" );
		sql.append("                TF.MAIN_EMAIL,\n" );
		sql.append("                TF.OTHER_LINKMAN,\n" );
		sql.append("                TF.OTHER_PHONE,\n" );
		sql.append("                TF.OTHER_JOB,\n" );
		sql.append("                TF.OTHER_EMAIL,\n" );
		sql.append("                NVL(D.GROUP_NAME, '全系') GROUP_NAME,\n" );
		sql.append("                TF.SERIES_COUNT,\n" );
		sql.append("                TF.REQ_REMARK,\n" );
		sql.append("                F.ORG_NAME,\n" );
		sql.append("                TU.NAME,\n" );
		sql.append("                TR.NAME AUDITNAME,\n" );
		sql.append("                TCOM.COMPANY_SHORTNAME\n" );
		sql.append("  FROM TM_FLEET TF,\n" );
		sql.append("       TC_USER TU,\n" );
		sql.append("       TC_USER TR,\n" );
		sql.append("       TM_COMPANY TCOM,\n" );
		sql.append("       TM_VHCL_MATERIAL_GROUP D,\n" );
		sql.append("       TM_DEALER E,\n" );
		sql.append("       TM_ORG F,\n" );
		sql.append("       TM_DEALER_ORG_RELATION G,vw_org_dealer VOD\n" );
		sql.append(" WHERE TF.IS_DEL = 0\n" );
		sql.append("   AND TF.SUBMIT_USER = TU.USER_ID(+)\n" );
		sql.append("   AND TF.AUDIT_USER_ID = TR.USER_ID(+)");
		sql.append("   AND TF.SERIES_ID = D.GROUP_ID(+)\n" );
		sql.append("   AND TF.DLR_COMPANY_ID = E.COMPANY_ID\n" );
		sql.append("   AND VOD.DEALER_ID=E.DEALER_ID");
		sql.append("   AND VOD.ROOT_DEALER_ID = G.DEALER_ID\n" );
		sql.append("   AND VOD.ROOT_ORG_ID=F.ORG_ID");
	//	sql.append("   AND F.ORG_ID = G.ORG_ID\n" );
	//	sql.append("   AND F.IS_COUNT = 0\n");
		sql.append("   AND TF.DLR_COMPANY_ID = TCOM.COMPANY_ID\n" );
		sql.append("   AND TCOM.COMPANY_TYPE = ").append(Constant.COMPANY_TYPE_DEALER).append("\n");
		if(null!= bean.getDlrCompanyId()&&!"".equals(bean.getDlrCompanyId())){
			sql.append("   AND TF.DLR_COMPANY_ID IN(");
			sql.append(bean.getDlrCompanyId());
			sql.append(")\n");
		}else{
			sql.append("   AND TF.STATUS <>");
			sql.append(Constant.FLEET_INFO_TYPE_01);
			sql.append("\n");
		}
		if(Utility.testString(bean.getDutyType())){
			if(bean.getDutyType().equals(String.valueOf(Constant.DUTY_TYPE_LARGEREGION))){
				sql.append("AND F.ORG_ID = ").append(bean.getOrgId());
			}
		}
		if(null!=bean.getBeginTime()&&!"".equals(bean.getBeginTime())){
			sql.append("   AND TF.SUBMIT_DATE >= TO_DATE('" );
			sql.append(bean.getBeginTime());
			sql.append(" 00:00:00','YYYY-MM-DD HH24:MI:SS')\n");
		}
		
		if(null!=bean.getEndTime()&&!"".equals(bean.getEndTime())){
			sql.append("   AND TF.SUBMIT_DATE <= TO_DATE('");
			sql.append(bean.getEndTime());
			sql.append(" 23:59:59','YYYY-MM-DD HH24:MI:SS')\n");
		}
		if(null!= bean.getDlrCompanyCode()&&!"".equals(bean.getDlrCompanyCode())&&!bean.getDlrCompanyCode().equals("''")){
			sql.append("AND TF.DLR_COMPANY_ID IN\n" );
			sql.append("       (SELECT DISTINCT TD.COMPANY_ID\n" );
			sql.append("          FROM TM_DEALER TD\n" );
			//sql.append("         WHERE TD.DEALER_TYPE = ").append(Constant.DEALER_TYPE_DVS).append("\n");
			sql.append("         WHERE TD.DEALER_CODE IN(").append(bean.getDlrCompanyCode()).append(")\n");
			sql.append("       )\n");
		}
		if(!"".equals(bean.getFleetName())){
			sql.append("   AND TF.FLEET_NAME LIKE '%" );
			sql.append(bean.getFleetName());
			sql.append("%'\n");
		}
		
		if(!"".equals(bean.getFleetType())){
			sql.append("   AND TF.FLEET_TYPE =" );
			sql.append(bean.getFleetType());
			sql.append("\n");
		}
		
		if(null!= bean.getStatus()&&!"".equals(bean.getStatus())){
			sql.append("   AND TF.STATUS IN(");
			sql.append(bean.getStatus());
			sql.append(")\n");
		}
		sql.append("AND VOD.DEALER_ID IN");
		sql.append("  (SELECT DEALER_ID FROM vw_org_dealer VW ");
		sql.append("WHERE VW.PQ_ORG_ID="+orgId+")");
		PageResult<Map<String, Object>> ps = (PageResult<Map<String, Object>>) pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
		return ps;
		
	}
	/**
	 * 集团客户信息报备查询（大区）
	 * @param bean
	 * @param pageSize
	 * @param curPage
	 * @return
	 */
	public PageResult<Map<String, Object>> queryLargeFleetInfo(FleetInfoBean bean,String orgId,int pageSize,int curPage){
		StringBuffer sql= new StringBuffer();

		sql.append(" SELECT DISTINCT TF.FLEET_ID,\n" );
		sql.append("                TF.FLEET_NAME,\n" );
		sql.append("                TF.FLEET_TYPE,\n" );
		sql.append("                TF.REGION,\n" );
		sql.append("                TF.MAIN_LINKMAN,\n" );
		sql.append("                TF.MAIN_PHONE,\n" );
		sql.append("                TF.STATUS,\n" );
		sql.append("                TF.SUBMIT_USER,\n" );
		sql.append("                TO_CHAR(TF.SUBMIT_DATE, 'YYYY-MM-DD') AS SUBMIT_DATE,\n" );
		sql.append("                TO_CHAR(TF.AUDIT_DATE, 'YYYY-MM-DD') AS AUDIT_DATE,\n" );
		sql.append("                TF.AUDIT_REMARK,\n" );
		sql.append("                TF.MAIN_BUSINESS,\n" );
		sql.append("                TF.FUND_SIZE,\n" );
		sql.append("                TF.STAFF_SIZE,\n" );
		sql.append("                TF.PURPOSE,\n" );
		sql.append("                TF.ADDRESS,\n" );
		sql.append("                TF.MAIN_JOB,\n" );
		sql.append("                TF.ZIP_CODE,\n" );
		sql.append("                TF.MAIN_EMAIL,\n" );
		sql.append("                TF.OTHER_LINKMAN,\n" );
		sql.append("                TF.OTHER_PHONE,\n" );
		sql.append("                TF.OTHER_JOB,\n" );
		sql.append("                TF.OTHER_EMAIL,\n" );
		sql.append("                NVL(D.GROUP_NAME, '全系') GROUP_NAME,\n" );
		sql.append("                TF.SERIES_COUNT,\n" );
		sql.append("                TF.REQ_REMARK,\n" );
		sql.append("                F.ORG_NAME,\n" );
		sql.append("                TU.NAME,\n" );
		sql.append("                TR.NAME AUDITNAME,\n" );
		sql.append("                TCOM.COMPANY_SHORTNAME\n" );
		sql.append("  FROM TM_FLEET TF,\n" );
		sql.append("       TC_USER TU,\n" );
		sql.append("       TC_USER TR,\n" );
		sql.append("       TM_COMPANY TCOM,\n" );
		sql.append("       TM_VHCL_MATERIAL_GROUP D,\n" );
		sql.append("       TM_DEALER E,\n" );
		sql.append("       TM_ORG F,\n" );
		sql.append("       TM_DEALER_ORG_RELATION G,vw_org_dealer VOD\n" );
		sql.append(" WHERE TF.IS_DEL = 0\n" );
		sql.append("   AND TF.SUBMIT_USER = TU.USER_ID(+)\n" );
		sql.append("   AND TF.AUDIT_USER_ID = TR.USER_ID(+)");
		sql.append("   AND TF.SERIES_ID = D.GROUP_ID(+)\n" );
		sql.append("   AND TF.DLR_COMPANY_ID = E.COMPANY_ID\n" );
		sql.append("   AND VOD.DEALER_ID=E.DEALER_ID");
		sql.append("   AND VOD.ROOT_DEALER_ID = G.DEALER_ID\n" );
		sql.append("   AND VOD.ROOT_ORG_ID=F.ORG_ID");
	//	sql.append("   AND F.ORG_ID = G.ORG_ID\n" );
	//	sql.append("   AND F.IS_COUNT = 0\n");
		sql.append("   AND TF.DLR_COMPANY_ID = TCOM.COMPANY_ID\n" );
		sql.append("   AND TCOM.COMPANY_TYPE = ").append(Constant.COMPANY_TYPE_DEALER).append("\n");
		if(null!= bean.getDlrCompanyId()&&!"".equals(bean.getDlrCompanyId())){
			sql.append("   AND TF.DLR_COMPANY_ID IN(");
			sql.append(bean.getDlrCompanyId());
			sql.append(")\n");
		}else{
			sql.append("   AND TF.STATUS <>");
			sql.append(Constant.FLEET_INFO_TYPE_01);
			sql.append("\n");
		}
		if(Utility.testString(bean.getDutyType())){
			if(bean.getDutyType().equals(String.valueOf(Constant.DUTY_TYPE_LARGEREGION))){
				sql.append("AND F.ORG_ID = ").append(bean.getOrgId());
			}
		}
		if(null!=bean.getBeginTime()&&!"".equals(bean.getBeginTime())){
			sql.append("   AND TF.SUBMIT_DATE >= TO_DATE('" );
			sql.append(bean.getBeginTime());
			sql.append(" 00:00:00','YYYY-MM-DD HH24:MI:SS')\n");
		}
		
		if(null!=bean.getEndTime()&&!"".equals(bean.getEndTime())){
			sql.append("   AND TF.SUBMIT_DATE <= TO_DATE('");
			sql.append(bean.getEndTime());
			sql.append(" 23:59:59','YYYY-MM-DD HH24:MI:SS')\n");
		}
		if(null!= bean.getDlrCompanyCode()&&!"".equals(bean.getDlrCompanyCode())&&!bean.getDlrCompanyCode().equals("''")){
			sql.append("AND TF.DLR_COMPANY_ID IN\n" );
			sql.append("       (SELECT DISTINCT TD.COMPANY_ID\n" );
			sql.append("          FROM TM_DEALER TD\n" );
			//sql.append("         WHERE TD.DEALER_TYPE = ").append(Constant.DEALER_TYPE_DVS).append("\n");
			sql.append("         WHERE TD.DEALER_CODE IN(").append(bean.getDlrCompanyCode()).append(")\n");
			sql.append("       )\n");
		}
		if(!"".equals(bean.getFleetName())){
			sql.append("   AND TF.FLEET_NAME LIKE '%" );
			sql.append(bean.getFleetName());
			sql.append("%'\n");
		}
		
		if(!"".equals(bean.getFleetType())){
			sql.append("   AND TF.FLEET_TYPE =" );
			sql.append(bean.getFleetType());
			sql.append("\n");
		}
		
		if(null!= bean.getStatus()&&!"".equals(bean.getStatus())){
			sql.append("   AND TF.STATUS IN(");
			sql.append(bean.getStatus());
			sql.append(")\n");
		}
		sql.append("AND VOD.DEALER_ID IN");
		sql.append("  (SELECT DEALER_ID FROM vw_org_dealer VW ");
		sql.append("WHERE VW.ROOT_ORG_ID="+orgId+")");
		PageResult<Map<String, Object>> ps = (PageResult<Map<String, Object>>) pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
		return ps;
		
	}
	/**
	 * 根据集团客户ID查询集团客户信息详情
	 * @param fleetId
	 * @return
	 */
	public Map<String, Object> getFleetInfobyId(String fleetId){
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT TF.FLEET_ID,\n" );
		sql.append("       TF.OEM_COMPANY_ID,\n" );
		sql.append("       TF.DLR_COMPANY_ID,\n" );
		sql.append("       TF.FLEET_NAME,\n" );
		sql.append("       TF.FLEET_TYPE,\n" );
		sql.append("       TF.FLEET_CODE,\n");
		sql.append("       TF.REGION,\n" );
		sql.append("       TF.MAIN_LINKMAN,\n" );
		sql.append("       TF.MAIN_PHONE,\n" );
		sql.append("       TF.SUBMIT_USER,\n" );
		sql.append("       TF.SUBMIT_DATE,\n" );
		sql.append("       TO_CHAR(TF.SUBMIT_DATE,'YYYY-MM-DD') SUBMIT_DAY,\n" );
		sql.append("       TF.MAIN_BUSINESS,\n" );
		sql.append("       TF.FUND_SIZE,\n" );
		sql.append("       TF.STAFF_SIZE,\n" );
		sql.append("       TF.PURPOSE,\n" );
		sql.append("       TF.ADDRESS,\n" );
		sql.append("       TF.MAIN_JOB,\n" );
		sql.append("       TF.ZIP_CODE,\n" );
		sql.append("       TF.MAIN_EMAIL,\n" );
		sql.append("       TF.STATUS,\n" );
		sql.append("       TF.OTHER_LINKMAN,\n" );
		sql.append("       TF.OTHER_PHONE,\n" );
		sql.append("       TF.SERIES_ID,\n" );
		sql.append("       NVL(D.GROUP_NAME, '全系') GROUP_NAME,\n" );
		sql.append("       TF.SERIES_COUNT,\n" );
		sql.append("       TF.OTHER_JOB,\n" );
		sql.append("       TF.OTHER_EMAIL,\n" );
		sql.append("       TF.REQ_REMARK,\n" );
		sql.append("       TF.AUDIT_DATE,\n" );
		sql.append("       TF.AUDIT_USER_ID,\n" );
		sql.append("       TF.AUDIT_REMARK,\n" );
		sql.append("       TF.IS_DEL,\n" );
		sql.append("       TF.IS_PACT,\n" );
		sql.append("       TF.PACT_ID,\n" );
		sql.append("       TF.pact_manage,\n" );
		sql.append("       TF.pact_manage_phone,\n" );
		sql.append("       TF.pact_manage_EMAIL,\n" );
		sql.append("       tcp.pact_name,\n" );
		sql.append("       TO_CHAR(TF.VISIT_DATE, 'YYYY-MM-DD') VISIT_DATE,\n" );
		sql.append("       TF.PACT_MANAGE NAME,\n" );
		sql.append("       TF.PACT_MANAGE_PHONE ,\n" );
		sql.append("       TF.MARKET_INFO ,\n" );
		sql.append("       TF.CONFIG_RQUIRE ,\n" );
		sql.append("       TF.FLEETREQ_DISCOUNT ,\n" );
		sql.append("       TF.OTHERCOMP_FAVORPOL ,\n" );
		sql.append("       TCOM.COMPANY_SHORTNAME\n" );
		sql.append("  FROM TM_FLEET TF,TC_USER TU,TM_COMPANY TCOM, TM_VHCL_MATERIAL_GROUP D, Tm_Company_Pact tcp\n");
		sql.append(" WHERE TF.IS_DEL = 0\n" );
		sql.append(" AND   TF.pact_id = tcp.pact_id(+)\n" );
		sql.append(" AND   TF.SUBMIT_USER = TU.USER_ID(+)\n" );
		sql.append(" AND TF.SERIES_ID = D.GROUP_ID(+)\n");
		sql.append("   AND TF.DLR_COMPANY_ID = TCOM.COMPANY_ID\n");
		sql.append("   AND TCOM.COMPANY_TYPE = ");
		sql.append(Constant.COMPANY_TYPE_DEALER);
		sql.append("\n");
		sql.append(" AND TF.FLEET_ID =");
		sql.append(fleetId);
		
		Map<String, Object> map = pageQueryMap(sql.toString(),null,getFunName());
		return map;
	}
	/**
	 * 根据集团客户ID查询需求说明分录中的信息
	 * @param fleetId
	 * @return
	 */
	public List<Map<String, Object>> getMaterialByFleetId(String fleetId){
		StringBuilder sql= new StringBuilder();
		sql.append("SELECT TVM.MATERIAL_ID,\n" );
		sql.append("       TVM.MATERIAL_CODE,\n" );
		sql.append("       TVM.MATERIAL_NAME,\n" );
		sql.append("       TVMP3.GROUP_NAME,\n" );
		sql.append("       TFRD.FLEET_ID,\n" );
		sql.append("       TFRD.AMOUNT,\n" );
		sql.append("       TFRD.DISCRIBE,\n" );
		sql.append("       TFRD.DETAIL_ID\n" );
		sql.append("  FROM TM_VHCL_MATERIAL         TVM,\n" );
		sql.append("       TM_FLEET_REQUEST_DETAIL  TFRD,\n" );
		sql.append("       TM_VHCL_MATERIAL_GROUP   TVMP1,\n" );
		sql.append("       TM_VHCL_MATERIAL_GROUP   TVMP2,\n" );
		sql.append("       TM_VHCL_MATERIAL_GROUP   TVMP3,\n" );
		sql.append("       TM_VHCL_MATERIAL_GROUP_R TVMPR\n" );
		sql.append(" WHERE TVM.MATERIAL_ID = TFRD.MATER_ID\n" );
		sql.append("   AND TVMP1.PARENT_GROUP_ID = TVMP2.GROUP_ID\n" );
		sql.append("   AND TVMP2.PARENT_GROUP_ID = TVMP3.GROUP_ID\n" );
		sql.append("   AND TVM.MATERIAL_ID = TVMPR.MATERIAL_ID\n" );
		sql.append("   AND TVMPR.GROUP_ID = TVMP1.GROUP_ID\n" );
		sql.append("   AND TVMP1.STATUS = 10011001\n" );
		sql.append("   AND TVMP2.STATUS = 10011001\n" );
		sql.append("   AND TVMP3.STATUS = 10011001\n" );
		sql.append("   AND TVM.STATUS = 10011001\n" );
		sql.append("   AND TFRD.FLEET_ID = "+fleetId);
		
		List<Map<String, Object>> list = pageQuery(sql.toString(),null,getFunName());
		return list;
	}
	/**
	 * 根据集团客户ID查询需求说明分录中的信息
	 * @param fleetId
	 * @return
	 */
	public List<Map<String, Object>> getSupportInfoByFleetId(String fleetId){
		StringBuilder sql= new StringBuilder();
		sql.append("SELECT TFSI.SUPPORT_INFO_ID,\n" );
		sql.append("       TFSI.FLEET_ID,\n" );
		sql.append("       TFSI.INTENT_SERIES,\n" );
		sql.append("       TFSI.PRICE,\n" );
		sql.append("       TFSI.DEPOT_PRO_PRICE,\n" );
		sql.append("       TFSI.AMOUNT,\n" );
		sql.append("       TFSI.PROFIT,\n" );
		sql.append("       TFSI.GIVE_AND_ACCEPT,\n" );
		sql.append("       TFSI.MARKET_DEVELOP,\n" );
		sql.append("       TFSI.REAL_PRICE,\n" );
		sql.append("       TFSI.REAL_PROFIT,\n" );
		sql.append("       TFSI.REQUEST_SUPPORT,\n" );
		sql.append("       TFSI.AUDIT_MONEY,\n" );
		sql.append("       TVMP.GROUP_NAME\n" );
		sql.append("  FROM TT_FLEET_SUPPORT_INFO TFSI, TM_VHCL_MATERIAL_GROUP TVMP\n" );
		sql.append(" WHERE TVMP.GROUP_ID = TFSI.INTENT_SERIES");
		sql.append("      AND TFSI.FLEET_ID="+fleetId);
		List<Map<String, Object>> list = pageQuery(sql.toString(),null,getFunName());
		return list;
	}
	
	/**
	 * 集团客户报备更改申请查询
	 * @param bean
	 * @param pageSize
	 * @param curPage
	 * @return
	 */
	public PageResult<Map<String, Object>> queryFleetInfoForModify(FleetInfoBean bean,int pageSize,int 

curPage){
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT TF.FLEET_ID,\n" );
		sql.append("       TF.FLEET_NAME,\n" );
		sql.append("       TF.FLEET_TYPE,\n" );
		sql.append("       TF.REGION,\n" );
		sql.append("       TF.MAIN_LINKMAN,\n" );
		sql.append("       TF.MAIN_PHONE,\n" );
		sql.append("       TF.STATUS,\n" );
		sql.append("       TF.SUBMIT_USER,\n" );
		sql.append("       TO_CHAR(TF.SUBMIT_DATE,'YYYY-MM-DD') AS SUBMIT_DATE,\n" );
		sql.append("       TF.MAIN_BUSINESS,\n" );
		sql.append("       TF.FUND_SIZE,\n" );
		sql.append("       TF.STAFF_SIZE,\n" );
		sql.append("       TF.PURPOSE,\n" );
		sql.append("       TF.ADDRESS,\n" );
		sql.append("       TF.MAIN_JOB,\n" );
		sql.append("       TF.ZIP_CODE,\n" );
		sql.append("       TF.MAIN_EMAIL,\n" );
		sql.append("       TF.OTHER_LINKMAN,\n" );
		sql.append("       TF.OTHER_PHONE,\n" );
		sql.append("       TF.OTHER_JOB,\n" );
		sql.append("       TF.OTHER_EMAIL,\n" );
		sql.append("       TF.REQ_REMARK,\n" );
		sql.append("       TU.NAME,\n" );
		sql.append("       TCOM.COMPANY_SHORTNAME\n" );
		sql.append("  FROM TM_FLEET TF,TC_USER TU,TM_COMPANY TCOM");
		sql.append(" WHERE TF.IS_DEL = 0\n" );
		sql.append("   AND TF.SUBMIT_USER = TU.USER_ID(+)\n");
		sql.append("   AND TF.DLR_COMPANY_ID = TCOM.COMPANY_ID\n");
		sql.append("   AND TCOM.COMPANY_TYPE = ");
		sql.append(Constant.COMPANY_TYPE_DEALER);
		sql.append("\n");
		
		
		if(!"".equals(bean.getFleetName())){
			sql.append("   AND TF.FLEET_NAME LIKE '%" );
			sql.append(bean.getFleetName());
			sql.append("%'\n");
		}
		
		if(!"".equals(bean.getFleetType())){
			sql.append("   AND TF.FLEET_TYPE =" );
			sql.append(bean.getFleetType());
			sql.append("\n");
		}
		
		if(!"".equals(bean.getBeginTime())){
			sql.append("   AND TF.SUBMIT_DATE >= TO_DATE('" );
			sql.append(bean.getBeginTime());
			sql.append(" 00:00:00','YYYY-MM-DD HH24:MI:SS')\n");
		}
		
		if(!"".equals(bean.getEndTime())){
			sql.append("   AND TF.SUBMIT_DATE <= TO_DATE('");
			sql.append(bean.getEndTime());
			sql.append(" 23:59:59','YYYY-MM-DD HH24:MI:SS')\n");
		}
		
		if(null!= bean.getStatus()&&!"".equals(bean.getStatus())){
			sql.append("   AND TF.STATUS IN(");
			sql.append(bean.getStatus());
			sql.append(")\n");
		}
		if(null!= bean.getDlrCompanyId()&&!"".equals(bean.getDlrCompanyId())){
			sql.append("   AND TF.DLR_COMPANY_ID IN(");
			sql.append(bean.getDlrCompanyId());
			sql.append(")\n");
		}
		

		PageResult<Map<String, Object>> ps = (PageResult<Map<String, Object>>) pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
		return ps;
		
	}
	
	public PageResult<Map<String, Object>> oemQueryFleetInfoForModify(FleetInfoBean bean,int pageSize,int curPage){
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT TF.FLEET_ID,\n" );
		sql.append("       TF.FLEET_NAME,\n" );
		sql.append("       TF.FLEET_TYPE,\n" );
		sql.append("       TF.REGION,\n" );
		sql.append("       TF.MAIN_LINKMAN,\n" );
		sql.append("       TF.MAIN_PHONE,\n" );
		sql.append("       TF.STATUS,\n" );
		sql.append("       TF.SUBMIT_USER,\n" );
		sql.append("       TO_CHAR(TF.SUBMIT_DATE,'YYYY-MM-DD') SUBMIT_DATE,\n" );
		sql.append("       TF.MAIN_BUSINESS,\n" );
		sql.append("       TF.FUND_SIZE,\n" );
		sql.append("       TF.STAFF_SIZE,\n" );
		sql.append("       TF.PURPOSE,\n" );
		sql.append("       TF.ADDRESS,\n" );
		sql.append("       TF.MAIN_JOB,\n" );
		sql.append("       TF.ZIP_CODE,\n" );
		sql.append("       TF.MAIN_EMAIL,\n" );
		sql.append("       TF.OTHER_LINKMAN,\n" );
		sql.append("       TF.OTHER_PHONE,\n" );
		sql.append("       TF.OTHER_JOB,\n" );
		sql.append("       TF.OTHER_EMAIL,\n" );
		sql.append("       TF.REQ_REMARK,\n" );
		sql.append("       TU.NAME,\n" );
		sql.append("       TCOM.COMPANY_SHORTNAME\n" );
		sql.append("  FROM TM_FLEET TF,TC_USER TU,TM_COMPANY TCOM");
		sql.append(" WHERE TF.IS_DEL = 0\n" );
		sql.append("   AND TF.SUBMIT_USER = TU.USER_ID(+)\n");
		sql.append("   AND TF.DLR_COMPANY_ID = TCOM.COMPANY_ID\n");
		sql.append("   AND TCOM.COMPANY_TYPE = ");
		sql.append(Constant.COMPANY_TYPE_DEALER);
		sql.append("\n");
		
		
		if(!"".equals(bean.getFleetName())){
			sql.append("   AND TF.FLEET_NAME LIKE '%" );
			sql.append(bean.getFleetName());
			sql.append("%'\n");
		}
		
		if(!"".equals(bean.getFleetType())){
			sql.append("   AND TF.FLEET_TYPE =" );
			sql.append(bean.getFleetType());
			sql.append("\n");
		}
		
		if(!"".equals(bean.getBeginTime())){
			sql.append("   AND TF.SUBMIT_DATE >= TO_DATE('" );
			sql.append(bean.getBeginTime());
			sql.append(" 00:00:00','YYYY-MM-DD HH24:MI:SS')\n");
		}
		
		if(!"".equals(bean.getEndTime())){
			sql.append("   AND TF.SUBMIT_DATE <= TO_DATE('");
			sql.append(bean.getEndTime());
			sql.append(" 23:59:59','YYYY-MM-DD HH24:MI:SS')\n");
		}
		
		sql.append("   AND TF.STATUS <>").append(Constant.FLEET_INFO_TYPE_01).append("\n"); 
		
//		if(null!= bean.getDlrCompanyId()&&!"".equals(bean.getDlrCompanyId())){
//			sql.append("   AND TF.DLR_COMPANY_ID IN(");
//			sql.append(bean.getDlrCompanyId());
//			sql.append(")\n");
//		}
		
		PageResult<Map<String, Object>> ps = (PageResult<Map<String, Object>>) pageQuery

(sql.toString(), null, getFunName(), pageSize, curPage);
		return ps;
		
	}
	
	
	/*
	 * 集团客户确认
	 */
	public PageResult<Map<String, Object>> showFieetList(String id, String name,int curPage, int pageSize){
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT B.COMPANY_SHORTNAME, A.FLEET_NAME, A.MAIN_LINKMAN,\n" );
		sql.append("       A.MAIN_PHONE, NVL(C.GROUP_NAME, '全系') GROUP_NAME,\n" );
		sql.append("       A.SERIES_COUNT, TO_CHAR(A.SUBMIT_DATE, 'YYYY-MM-DD') SUBMIT_DATE,\n" );
		sql.append("       A.STATUS\n" );
		sql.append("FROM TM_FLEET A, TM_COMPANY B, TM_VHCL_MATERIAL_GROUP C\n" );
		sql.append("WHERE A.DLR_COMPANY_ID = B.COMPANY_ID\n" );
		sql.append("AND A.SERIES_ID = C.GROUP_ID(+)\n" );
		sql.append("AND A.FLEET_NAME LIKE '%").append(name).append("%'\n");
		sql.append("AND A.STATUS <> ").append(Constant.FLEET_INFO_TYPE_01).append("\n");
		if(!id.equals("")){
			sql.append("AND A.FLEET_ID <> ").append(id).append("\n");	
		}
		
		PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
		return ps;
	}
	/**
	 * 根据客户ID查询客户意向审核信息
	 * @param  :客户ID
	 * @return :集团客户意向审核信息
	 */
	public List<Map<String, Object>> getFleetAuditInfobyId(String fleetId){
		StringBuffer sql= new StringBuffer();
		sql.append("select tc.code_desc,to_char(tfa.AUDIT_DATE,'yyyy-mm-dd')AUDITDATE,tfa.audit_remark,tu.name, tmo.org_name from tt_fleet_audit tfa,TM_ORG tmo,tc_user tu,tc_code tc where tfa.org_id=tmo.org_id and tfa.audit_user_id=tu.user_id and tc.code_id=tfa.audit_result");
		
		if(!"".equals(fleetId)&&fleetId!=null){
			sql.append("   AND tfa.FLEET_ID ="+fleetId+"\n" );
		}
		sql.append(" order by tfa.AUDIT_DATE desc");
		List<Map<String, Object>> list = pageQuery(sql.toString(),null,getFunName());
		return list;
	}
	
	public List<Map<String, Object>> getPactInfo(Map<String, String> map) {
		String status = map.get("status") ;
		String isAllowApply = map.get("isAllowApply") ;
		String pactId = map.get("pactId") ;
		
		StringBuffer sql = new StringBuffer("\n") ;
		
		sql.append("select tmp.pact_id, tmp.pact_name, tmp.parent_fleet_type\n");
		sql.append("  from Tm_Company_Pact tmp\n");  
		sql.append(" where tmp.status = ").append(status).append("\n");  
		sql.append("   and tmp.is_allow_apply = ").append(isAllowApply).append("\n");
		
		if(!CommonUtils.isNullString(pactId)) {
			sql.append("   and tmp.pact_id = ").append(pactId).append("\n");
		}
		
		return super.pageQuery(sql.toString(), null, super.getFunName()) ;
	}
	
	public Map<String, Object> getFleetPactManage(String dlrCom) {
		StringBuffer sql = new StringBuffer("\n") ;
		
		sql.append("select temp.pact_manage, temp.pact_manage_phone\n");
		sql.append("  from (select tmf.fleet_name, tmf.pact_manage, tmf.pact_manage_phone\n");  
		sql.append("          from tm_fleet tmf where tmf.dlr_company_id = ").append(dlrCom).append("\n");  
		sql.append("         order by rownum desc) temp\n");  
		sql.append(" where rownum = 1\n");
		
		return super.pageQueryMap(sql.toString(), null, super.getFunName()) ;
	}
	/**
	 * 查询物料的信息
	 * @param name
	 * @param dlrCompanyId
	 * @param oemCompanyId
	 * @param pageSize
	 * @param curPage
	 * @return
	 * @throws Exception
	 */
	public static PageResult<Map<String, Object>> getMaterialList
	(String name,String code,String selectedVehile, Long dlrCompanyId, Long oemCompanyId, int pageSize, int curPage)throws Exception{
		StringBuilder sql= new StringBuilder();
		sql.append("SELECT TVM.MATERIAL_ID,\n" );
		sql.append("         TVM.MATERIAL_CODE,\n" );
		sql.append("         TVM.MATERIAL_NAME,\n" );
		sql.append("         TVMP3.GROUP_NAME\n" );
		sql.append("    FROM TM_VHCL_MATERIAL_GROUP   TVMP1,\n" );
		sql.append("         TM_VHCL_MATERIAL_GROUP   TVMP2,\n" );
		sql.append("         TM_VHCL_MATERIAL_GROUP   TVMP3,\n" );
		sql.append("         TM_VHCL_MATERIAL         TVM,\n" );
		sql.append("         TM_VHCL_MATERIAL_GROUP_R TVMPR\n" );
		sql.append("   WHERE TVMP1.PARENT_GROUP_ID = TVMP2.GROUP_ID\n" );
		sql.append("     AND TVMP2.PARENT_GROUP_ID = TVMP3.GROUP_ID\n" );
		sql.append("     AND TVM.MATERIAL_ID = TVMPR.MATERIAL_ID\n" );
		sql.append("     AND TVMPR.GROUP_ID = TVMP1.GROUP_ID\n" );
		sql.append("     AND TVMP1.STATUS = 10011001\n" );
		sql.append("     AND TVMP2.STATUS = 10011001\n" );
		sql.append("     AND TVMP3.STATUS = 10011001\n" );
		sql.append("     AND TVM.STATUS = 10011001");
		if(name!=null&&!"".equals(name)){
			sql.append("     AND TVM.Material_NAME LIKE '%"+name+"%'");
		}
		if(code!=null&&!"".equals(code)){
			sql.append("     AND TVM.Material_CODE LIKE '%"+code+"%'");
		}
		if(selectedVehile!=null&&!"".equals(selectedVehile)){
			selectedVehile="'"+selectedVehile.replaceAll(",", "','")+"'";
			sql.append(" AND TVM.MATERIAL_ID NOT IN("+selectedVehile+") ");
		}
		return  dao.pageQuery(sql.toString(), null, dao.getFunName(), pageSize,
				curPage);
	}
	/**
	 * 获取当天经销商审核的大客户数
	 * @return
	 * @throws Exception
	 */
	public int getDealerFleetCount(String dealer_id,String current_date) throws Exception{
		int i=0;
		StringBuilder sql= new StringBuilder();
		sql.append("SELECT COUNT(*) COUNTS \n" );
		sql.append("  FROM TM_FLEET TF\n" );
		sql.append(" WHERE TF.DLR_COMPANY_ID = "+dealer_id+"\n" );
		sql.append("   AND TF.STATUS=11021003\n" );
		sql.append(" AND TF.FLEET_CODE IS NOT NULL\n");
		sql.append("   AND ((TF.AUDIT_DATE < SYSDATE\n" );
		sql.append("   AND TF.AUDIT_DATE >\n" );
		sql.append("       TO_DATE('"+current_date+" 00:00:00', 'yyyy-mm-dd hh24:mi:ss'))");
		sql.append("   OR( TF.CREATE_DATE >\n" );
		sql.append("       TO_DATE('"+current_date+" 00:00:00', 'yyyy-mm-dd hh24:mi:ss')))");
		Map<String,Object> map=dao.pageQueryMap(sql.toString(), null, dao.getFunName());
		i=Integer.parseInt(map.get("COUNTS").toString());
		return i;
	}
	/**
	 * 根据集团客户ID查询需求说明分录中的信息
	 * @param fleetId
	 * @return
	 */
	public Map<String,Object>  getPrintAuditRemark(String fleetId,String dutyType){
		StringBuilder sql= new StringBuilder();
		sql.append("SELECT TFIA.AUDIT_REMARK,tcu.name USER_NAME\n" );
		sql.append("  FROM TT_FLEET_INTENT_AUDIT TFIA, TC_USER TCU, TC_CODE TCC, TM_ORG TMO\n" );
		sql.append(" WHERE TFIA.AUDIT_USER_ID = TCU.USER_ID\n" );
		sql.append("   AND TFIA.AUDIT_RESULT = TCC.CODE_ID\n" );
		sql.append("   AND TFIA.ORG_ID = TMO.ORG_ID\n" );
		sql.append("   AND TFIA.FLEET_ID = "+fleetId+"\n" );
		sql.append("   AND ROWNUM = 1\n" );
		sql.append("   AND TMO.DUTY_TYPE = "+dutyType+"\n" );
		sql.append(" ORDER BY TFIA.AUDIT_DATE DESC");

		Map<String,Object> m = pageQueryMap(sql.toString(),null,getFunName());
		
	
		return m;
	}
	
}
