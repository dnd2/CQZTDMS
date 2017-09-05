package com.infodms.dms.dao.sales.dealer;

import java.io.UnsupportedEncodingException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.common.materialManager.MaterialGroupManagerDao;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.po.TcCodePO;
import com.infodms.dms.po.TmDealerCreditPO;
import com.infodms.dms.po.TmOrgPO;
import com.infodms.dms.po.TmpTmDealerPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.StringUtil;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.POFactory;
import com.infoservice.po3.POFactoryBuilder;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;
import com.infoservice.po3.core.callback.DAOCallback;

import flex.messaging.util.StringUtils;

/**
 * @Title: CHANADMS
 *
 * @Description:
 *
 * @Copyright: Copyright (c) 2010
 *
 * @Company:  www.infoservice.com.cn
 * @Date: 2010-7-5
 *
 * @author zjy 
 * @mail   zhaojinyu@infoservice.com.cn
 * @version 1.0
 * @remark 
 */
public class DealerInfoDao extends BaseDao<PO> {
	public static Logger logger = Logger.getLogger(DealerInfoDao.class);
	private static POFactory factory = POFactoryBuilder.getInstance();
	private static DealerInfoDao dao = new DealerInfoDao ();
	public static final DealerInfoDao getInstance() {
		if (dao == null) {
			dao = new DealerInfoDao();
		}
		return dao;
	}
	private DealerInfoDao() {}
	
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		return null;
	}
	
	/**
	 * ranke 20150409
	 * 修改用户是否被锁定  
	 * @param userId
	 * @param isLock
	 * @param lastsigninTime
	 */
	public void editUserLock(long userId, int isLock, String lastsigninTime) {
		String sql ="update tc_user tc set tc.is_lock = "+ isLock +",tc.lastsignin_time = to_date('"+ lastsigninTime +"','yyyy-mm-dd hh24:mi:ss') where user_id =" + userId;
		this.update(sql, null);
	}
	
	/**
	 * ranke 20150525
	 * 查询经销商也售后绑定关系列表
	 * @param map
	 * @param curPage
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	public PageResult<Map<String, Object>> queryBindingRelationInfo(Map<String, Object> map,int curPage,int pageSize) throws Exception{
		String dealerId = CommonUtils.checkNull(map.get("dealerId"));
		String shDealerId = CommonUtils.checkNull(map.get("shDealerId"));
		List<Object> params = new LinkedList<Object>();
		StringBuffer sbSql = new StringBuffer();
		sbSql.append("select a.relation_id,xs_d.dealer_code xs_dealer_code,xs_d.dealer_name xs_dealer_name,"
				+ "xs_c.company_name xs_company_name,sh_d.dealer_code sh_dealer_code,sh_d.dealer_name sh_dealer_name,sh_c.company_name "
				+ "sh_company_name,a.status from TT_DEALER_RELATION a,tm_dealer xs_d,tm_dealer sh_d,tm_company xs_c,tm_company sh_c where a.xs_dealer_id = xs_d.dealer_id and a.sh_dealer_id = sh_d.dealer_id and a.xs_company_id = xs_c.company_id and a.sh_company_id = sh_c.company_id ");
		if(!dealerId.equals("")) {
			sbSql.append(" and a.xs_dealer_id = ?");
			params.add(dealerId);
		}
		if(!shDealerId.equals("")) {
			sbSql.append(" and a.sh_dealer_id= ? ");
			params.add(shDealerId);
		}
		return dao.pageQuery(sbSql.toString(), params, getFunName(),pageSize, curPage);
	}
	
	/**
	 * Function         : 经销商查询
	 * @param           : 经销商CODE
	 * @param           : 经销商名称
	 * @param           : 经销商等级
	 * @param           : 经销商状态
	 * @param           : 上级经销商代码
	 * @param           : 上级车厂组织
	 * @param           : 经销商类型
	 * @param           : 服务商类型
	 * @param           : 经销商公司ID
	 * @param           : 服务站状态
	 * @param           : 分页参数
	 * @return          : 经销商信息
	 * @throws          : Exception
	 * LastUpdate       : 2013-11-18 ranj
	 */
	public PageResult<Map<String, Object>> queryDealerInfo(Map<String, Object> map,int curPage,int pageSize) throws Exception{
		
		String dealerType = CommonUtils.checkNull(map.get("DEALER_TYPE")); 
		String dealerCode = CommonUtils.checkNull(map.get("DEALER_CODE"));
		String dealerName = CommonUtils.checkNull(map.get("DEALER_NAME"));
		String dealerLevel = CommonUtils.checkNull(map.get("DEALER_LEVEL"));
		String dealerStatus = CommonUtils.checkNull(map.get("DEALER_STATUS"));
//		String orgCode = CommonUtils.checkNull(map.get("ORGCODE"));
		String sJDealerCode = CommonUtils.checkNull(map.get("sJDealerCode"));
		String companyName = CommonUtils.checkNull(map.get("COMPANY_NAME"));
		String serviceStatus = CommonUtils.checkNull(map.get("SERVICE_STATUS"));
		
		String regionId = CommonUtils.checkNull(map.get("regionId"));
		String orgCode = CommonUtils.checkNull(map.get("orgCode"));
		String orgId = CommonUtils.checkNull(map.get("orgId"));
		String parentOrgCode = CommonUtils.checkNull(map.get("parentOrgCode"));
		String AUTHORIZATION_TYPE = CommonUtils.checkNull(map.get("AUTHORIZATION_TYPE"));
		String WORK_TYPE = CommonUtils.checkNull(map.get("WORK_TYPE"));
		String IMAGE_LEVEL = CommonUtils.checkNull(map.get("IMAGE_LEVEL"));
		String SCREATE_DATE = CommonUtils.checkNull(map.get("SCREATE_DATE"));
		String IMAGE_COMFIRM_LEVEL = CommonUtils.checkNull(map.get("IMAGE_COMFIRM_LEVEL"));
		String ECREATE_DATE = CommonUtils.checkNull(map.get("ECREATE_DATE"));
		
		String regionCode = CommonUtils.checkNull(map.get("regionCode"));
		
		String AUTHORIZATION_SCREATE_DATE = CommonUtils.checkNull(map.get("AUTHORIZATION_SCREATE_DATE"));
		String AUTHORIZATION_ECREATE_DATE = CommonUtils.checkNull(map.get("AUTHORIZATION_ECREATE_DATE"));
		
		
		List<Object> params = new LinkedList<Object>();
		StringBuffer sbSql = new StringBuffer();
		
		sbSql.append("select a.dealer_id, a.dealer_code,\n");
		sbSql.append("       a.dealer_name,\n");
		sbSql.append("       a.DEALER_SHORTNAME,\n");
		sbSql.append("       a.status,\n");
		sbSql.append("       e.COMPANY_NAME,\n");
		sbSql.append("		 c.dealer_code parent_dealer_code,\n");
		sbSql.append("		 c.dealer_name parent_dealer_name,\n"); 
		sbSql.append("       o.org_name,\n");
		sbSql.append("       a.dealer_level,\n");
		sbSql.append("       b.AUTHORIZATION_TYPE,\n");
		sbSql.append("       b.AUTHORIZATION_DATE,\n");
		sbSql.append("       b.WORK_TYPE,\n");
		sbSql.append("       DECODE(b.SHOP_TYPE,'直营','重点客户','代理','代理') SHOP_TYPE, \n");
		sbSql.append("       b.IMAGE_LEVEL,\n");
		sbSql.append("       b.IMAGE_COMFIRM_LEVEL,\n");
		sbSql.append("       a.service_status,\n");
		sbSql.append("       g.ROOT_ORG_NAME,\n");
		sbSql.append("       g.ROOT_ORG_ID,\n");
		sbSql.append("       f.REGION_NAME\n");
		
		sbSql.append("  from tm_dealer              a,\n");
		sbSql.append("       tm_dealer_detail       b,\n");
		sbSql.append("       tm_dealer              c,\n");
		sbSql.append("       tm_org                 o,\n");
		sbSql.append("       tm_company             e,\n");
		sbSql.append("       tm_region             f,\n");
		sbSql.append("       vw_org_dealer_service            g\n");
		sbSql.append(" where a.dealer_id = b.fk_dealer_id(+)\n");
		sbSql.append(" and a.dealer_id = g.dealer_id(+)\n");
		sbSql.append("   and a.parent_dealer_d = c.dealer_id(+)\n");
		sbSql.append("   and o.org_id = a.dealer_org_id\n");
		sbSql.append("   and a.company_id = e.company_id\n");
		sbSql.append("   and a.province_id = f.region_code\n");  
		
		if(!dealerType.equals("")) {
			sbSql.append("   and a.dealer_type = ?\n");
			params.add(dealerType);
		}
        if (!"".equals(AUTHORIZATION_SCREATE_DATE)) {
        	sbSql.append(" and AUTHORIZATION_DATE>= to_date('").append(AUTHORIZATION_SCREATE_DATE).append(" 00:00:00','YYYY/MM/dd HH24:mi:ss')");
        	sbSql.append(" and AUTHORIZATION_DATE<= to_date('").append(AUTHORIZATION_ECREATE_DATE).append(" 23:59:59','YYYY/MM/dd HH24:mi:ss')");
        }
        if(!dealerName.equals("")){
			sbSql.append("   and (a.dealer_name like ? or a.pinyin like ?  )\n");
			params.add("%"+dealerName+"%");
			params.add("%"+dealerName+"%");
		}
		
		if(!dealerCode.equals("")){
			sbSql.append("   and a.dealer_code like ?\n");
			params.add("%"+dealerCode+"%");
		}
		
		if(!dealerLevel.equals("")) {
			sbSql.append("	 and a.dealer_level = ?\n");
			params.add(dealerLevel);
		}
		
		if(!dealerStatus.equals("")) {
			sbSql.append(" 	 and a.status = ?\n");
			params.add(dealerStatus);
		}

		if(!parentOrgCode.equals("")) {
			sbSql.append(" 	 and o.parent_org_id in (select org_id from tm_org where org_code = ?)\n");
			params.add(parentOrgCode);
		}

		if(!sJDealerCode.equals("")) {
			sbSql.append(" 	 and a.parent_dealer_d in (select dealer_id from tm_dealer where dealer_code = ?)\n");
			params.add(sJDealerCode);
		}
		
		if(!companyName.equals("")) {
			sbSql.append(" 	 and e.company_shortname = ?\n");
			params.add(companyName);
		}
		
		if(!serviceStatus.equals("")) {
			sbSql.append("   and a.service_status = ?\n");
			params.add(serviceStatus);
		}
		
		if(!IMAGE_LEVEL.equals("")) {
			sbSql.append("   and b.IMAGE_LEVEL = ?\n");
			params.add(IMAGE_LEVEL);
		}
		if(!IMAGE_COMFIRM_LEVEL.equals("")) {
			sbSql.append("   and b.IMAGE_COMFIRM_LEVEL = ?\n");
			params.add(IMAGE_COMFIRM_LEVEL);
		}
		if(dealerType.equals("10771001")){
			if(!WORK_TYPE.equals("")) {
				sbSql.append("   and b.SHOP_TYPE = ?\n");
				params.add(WORK_TYPE);
			}
		}else{
			if(!WORK_TYPE.equals("")) {
				sbSql.append("   and b.WORK_TYPE = ?\n");
				params.add(WORK_TYPE);
			}
		}
		if(!AUTHORIZATION_TYPE.equals("")) {
			sbSql.append("   and b.AUTHORIZATION_TYPE = ?\n");
			params.add(AUTHORIZATION_TYPE);
		}
		if (!CommonUtils.isNullString(orgCode)) {
			sbSql.append("   and o.org_Code = '").append(orgCode).append("'\n") ;
		}
		if (!CommonUtils.isNullString(orgId)) {
			sbSql.append("   and g.ROOT_ORG_ID = ").append(orgId).append("\n");
		}
		if (!CommonUtils.isNullString(regionCode)) {
			sbSql.append(Utility.getConSqlByParamForEqual(regionCode, params, "G", "ORG_Code"));
		}
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		
		if(null != logonUser.getDealerId()){
			sbSql.append(" and a.dealer_id=?");
			params.add(logonUser.getDealerId());
		}else{
			if(dealerType.equals("10771001")){
				sbSql.append(MaterialGroupManagerDao.getOrgDealerLimitSql("a.DEALER_ID", logonUser));
			}else{
				sbSql.append(MaterialGroupManagerDao.getOrgDealerLimitSqlForService("a.DEALER_ID", logonUser));
			}
		}
		sbSql.append(MaterialGroupManagerDao.getPoseIdBusinessSql(Constant.areaId, logonUser.getPoseId().toString()));
		sbSql.append("   order by g.ROOT_ORG_ID,g.org_id,g.dealer_id");
		return dao.pageQuery(sbSql.toString(), params, getFunName(),pageSize, curPage);
	}
	
	
	
	/**
	 * Function         : 经销商查询
	 * @param           : 经销商CODE
	 * @param           : 经销商名称
	 * @param           : 经销商等级
	 * @param           : 经销商状态
	 * @param           : 上级经销商代码
	 * @param           : 上级车厂组织
	 * @param           : 经销商类型
	 * @param           : 经销商公司ID
	 * @param           : 分页参数
	 * @return          : 经销商信息
	 * @throws          : Exception
	 * LastUpdate       : 2017-08-14
	 */
	public PageResult<Map<String, Object>> queryDealerInfo(String areaId,String dealerClass,String dealerCode,String dealerName,String dealerLevel,String status,String sJdealerCode,String orgCode,String dealerType,String companyId,String oemCompanyId,String province,int curPage,int pageSize) throws Exception{
		StringBuffer sql= new StringBuffer();
		/*sql.append("SELECT D.DEALER_ID,\n");
		sql.append("       D.DEALER_CODE,\n");  
		sql.append("       D.DEALER_SHORTNAME,\n");  
		sql.append("       D.DEALER_TYPE,\n");  
		sql.append("       D.STATUS,\n"); 
		sql.append("       D.DEALER_LEVEL,\n"); 
		sql.append("       DECODE(TD1.DEALER_SHORTNAME, NULL, '', TD1.DEALER_SHORTNAME) AS SHANGJINAME,\n"); 
		if(String.valueOf(Constant.DEALER_LEVEL_01).equals(dealerLevel))
		{
			sql.append("      D.SHANGJIORGNAME,\n");  	
		}else
		{
			sql.append("        '' AS SHANGJIORGNAME,\n");
		}
		sql.append("       D.COMPANY_SHORTNAME\n");
		sql.append("  FROM (SELECT TD.DEALER_ID,\n");  
		sql.append("               TD.DEALER_CODE,\n");  
		sql.append("               TD.DEALER_SHORTNAME,\n");  
		sql.append("               TD.DEALER_TYPE,\n");  
		sql.append("               TD.STATUS,\n");
		sql.append("               TD.DEALER_LEVEL,\n"); 
		if(String.valueOf(Constant.DEALER_LEVEL_01).equals(dealerLevel))
		{
			sql.append("               TMO.ORG_NAME AS SHANGJIORGNAME,\n"); 
		}
		sql.append("               TD.PARENT_DEALER_D,\n");  
		sql.append("               TC.COMPANY_SHORTNAME\n");  
		if(String.valueOf(Constant.DEALER_LEVEL_01).equals(dealerLevel))
		{
			sql.append("           FROM TM_DEALER TD, TM_COMPANY TC,TM_ORG TMO,TM_DEALER_ORG_RELATION TDOR\n");
		}else
		{
			sql.append("           FROM TM_DEALER TD, TM_COMPANY TC \n");
		}
		sql.append("         WHERE TD.COMPANY_ID = TC.COMPANY_ID\n");  
		if(!"".equals(dealerCode)&&dealerCode!=null)
		{
			sql.append("           AND TD.DEALER_CODE LIKE '%"+dealerCode+"%'\n"); 
		}
		if(!"".equals(dealerName)&&dealerName!=null)
		{
			sql.append("           AND TD.DEALER_SHORTNAME LIKE '%"+dealerName+"%'\n"); 
		}
		if(!"".equals(status)&&status!=null)
		{
			sql.append("           AND TD.STATUS = "+status+"\n"); 
		}
		if(!"".equals(dealerType)&&dealerType!=null)
		{
			sql.append("           AND TD.DEALER_TYPE = "+dealerType+"\n"); 
		}
		if(String.valueOf(Constant.DEALER_LEVEL_01).equals(dealerLevel))
		{
			sql.append("           AND TD.DEALER_ID = TDOR.DEALER_ID\n"); 
			sql.append("           AND TDOR.ORG_ID = TMO.ORG_ID\n"); 
			sql.append("           AND TD.DEALER_LEVEL = "+dealerLevel+"\n"); 
			if(!"".equals(orgCode)&&orgCode!=null)
			{
				sql.append("     AND EXISTS (SELECT 1 FROM TM_DEALER_ORG_RELATION TDOR,TM_ORG TMO WHERE TDOR.ORG_ID=TMO.ORG_ID AND TMO.ORG_CODE='"+orgCode+"' AND TDOR.DEALER_ID=TD.DEALER_ID)\n"); 
			}
		}else
		{
			sql.append("           AND TD.DEALER_LEVEL = "+dealerLevel+"\n"); 
			if(!"".equals(sJdealerCode)&&sJdealerCode!=null)
			{
				sql.append("           AND TD.PARENT_DEALER_D = (SELECT DEALER_ID FROM TM_DEALER WHERE DEALER_CODE ='"+sJdealerCode+"')\n");	
			}
		} 
		if(!"".equals(companyId)&&companyId!=null)
		{
			sql.append("           AND TD.COMPANY_ID = "+companyId+"\n"); 
		}
		if(oemCompanyId!=null&&!"".equals(oemCompanyId))
		{
			sql.append("           AND TD.OEM_COMPANY_ID = "+oemCompanyId+"\n"); 
		}
		if(dealerClass!=null&&dealerClass!=""){
			sql.append("          AND TD.DEALER_CLASS='"+dealerClass+"'\n"); 
		}
		sql.append("           AND TD.COMPANY_ID = TC.COMPANY_ID)D\n");
		sql.append("  LEFT JOIN TM_DEALER TD1 ON D.PARENT_DEALER_D = TD1.DEALER_ID\n");
		sql.append("  ORDER BY D.DEALER_ID ASC\n");*/
		
		
		sql.append("SELECT DISTINCT D.DEALER_ID,\n");
		sql.append("       D.DEALER_CODE,\n");  
		sql.append("       D.DEALER_SHORTNAME,\n");  
		sql.append("       D.DEALER_TYPE,\n");  
		sql.append("       D.STATUS,\n"); 
		sql.append("       D.DEALER_LEVEL,\n"); 
		sql.append("       DECODE(TD1.DEALER_SHORTNAME, NULL, '', TD1.DEALER_SHORTNAME) AS SHANGJINAME,\n"); 
		sql.append("       D.SHANGJIORGNAME,\n");  	
		sql.append("       D.COMPANY_SHORTNAME,\n");
		sql.append("       TO_CHAR(D.CREATE_DATE,'yyyy-MM-dd') AS CREATEDATE,\n");
		sql.append("       TO_CHAR(D.UPDATE_DATE,'yyyy-MM-dd') AS UPDATEDATE,\n");
		sql.append("       DECODE(E.NAME, NULL, '', E.NAME) AS CREATEPER,\n");
		sql.append("       DECODE(F.NAME, NULL, '', F.NAME) AS UPDATEPER\n");
		sql.append("  FROM (SELECT TD.DEALER_ID,\n");  
		sql.append("               TD.DEALER_CODE,\n");  
		sql.append("               TD.DEALER_SHORTNAME,\n");  
		sql.append("               TD.DEALER_TYPE,\n");  
		sql.append("               TD.STATUS,\n");
		sql.append("               TD.DEALER_LEVEL,\n"); 
		sql.append("               TD.CREATE_BY,\n"); 
		sql.append("               TD.UPDATE_BY,\n"); 
		sql.append("               TD.CREATE_DATE,\n"); 
		sql.append("               TD.UPDATE_DATE,\n"); 
		sql.append("               TMO.ORG_NAME AS SHANGJIORGNAME,\n"); 
		sql.append("               TD.PARENT_DEALER_D,\n");  
		sql.append("               TC.COMPANY_SHORTNAME\n");  
		sql.append("           FROM TM_COMPANY TC,TM_ORG TMO,TM_DEALER_ORG_RELATION TDOR,TM_DEALER TD, TM_DEALER_BUSINESS_AREA TDBA\n");
		sql.append("         	WHERE TD.COMPANY_ID = TC.COMPANY_ID\n");  
		sql.append("         	  AND TD.DEALER_ID = TDBA.DEALER_ID(+)\n"); 
		if(dealerCode!=null&&!"".equals(dealerCode))
		{
			sql.append("           AND TD.DEALER_CODE LIKE '%"+dealerCode+"%'\n"); 
		}
		if(dealerName!=null&&!"".equals(dealerName))
		{
			sql.append("           AND TD.DEALER_SHORTNAME LIKE '%"+dealerName+"%'\n"); 
		}
		if(status!=null&&!"".equals(status))
		{
			sql.append("           AND TD.STATUS = "+status+"\n"); 
		}
		if(dealerType!=null&&!"".equals(dealerType))
		{
			sql.append("           AND TD.DEALER_TYPE = "+dealerType+"\n"); 
		}
		sql.append("           AND F_GET_PID(TD.DEALER_ID) = TDOR.DEALER_ID \n"); 
		sql.append("           AND TDOR.ORG_ID = TMO.ORG_ID\n"); 
		if(dealerLevel!=null&&!"".equals(dealerLevel))
		{
			sql.append("           AND TD.DEALER_LEVEL = "+dealerLevel+"\n"); 
		}
		if(orgCode!=null&&!"".equals(orgCode))
		{
			sql.append("     AND EXISTS (SELECT 1 FROM TM_DEALER_ORG_RELATION TDOR,TM_ORG TMO WHERE TDOR.ORG_ID=TMO.ORG_ID AND TMO.ORG_CODE='"+orgCode+"' AND (TDOR.DEALER_ID=TD.DEALER_ID OR TDOR.DEALER_ID=TD.PARENT_DEALER_D))\n"); 
		}
	
		
		if(sJdealerCode!=null&&!"".equals(sJdealerCode))
		{
			sql.append("           AND TD.PARENT_DEALER_D = (SELECT DEALER_ID FROM TM_DEALER WHERE DEALER_CODE ='"+sJdealerCode+"')\n");	
		}
		if(companyId!=null&&!"".equals(companyId))
		{
			sql.append("           AND TD.COMPANY_ID = "+companyId+"\n"); 
		}
		if(oemCompanyId!=null&&!"".equals(oemCompanyId))
		{
			sql.append("           AND TD.OEM_COMPANY_ID = "+oemCompanyId+"\n"); 
		}
		if(dealerClass!=null&&dealerClass!=""){
			sql.append("          AND TD.DEALER_CLASS='"+dealerClass+"'\n"); 
		}
		if(province!=null&&province!=""){
			sql.append("          AND TD.PROVINCE_ID='"+province+"'\n"); 
		}
		
		sql.append("           AND TD.COMPANY_ID = TC.COMPANY_ID)D\n");
		sql.append("  LEFT JOIN TM_DEALER TD1 ON D.PARENT_DEALER_D = TD1.DEALER_ID\n");
		sql.append("  LEFT JOIN TC_USER E ON D.CREATE_BY=E.USER_ID\n");
		sql.append("  LEFT JOIN TC_USER F ON D.UPDATE_BY=F.USER_ID\n");
		
		if(!CommonUtils.isNullString(areaId)) {
			sql.append("LEFT JOIN TM_DEALER_BUSINESS_AREA TDBA\n");
			sql.append("    ON D.DEALER_ID = TDBA.DEALER_ID\n");  
			sql.append("    WHERE TDBA.AREA_ID = ").append(areaId).append("\n");
		}
	    //2017-08-14 只查询销售经销商	
		//sql.append("  WHERE D.DEALER_TYPE = 10771001\n");
		sql.append("  ORDER BY D.STATUS ASC, D.DEALER_ID ASC\n");
		PageResult<Map<String, Object>> rs = dao.pageQuery(sql.toString(), null, getFunName(),pageSize, curPage);
		return rs;
	}
	
public PageResult<Map<String, Object>> query2ndDealerInfo(Map<String, Object> map,int curPage,int pageSize, AclUserBean logonUser) throws Exception{
		
		String dealerType = CommonUtils.checkNull(map.get("DEALER_TYPE")); 
		String dealerCode = CommonUtils.checkNull(map.get("DEALER_CODE"));
		String dealerName = CommonUtils.checkNull(map.get("DEALER_NAME"));
		String dealerLevel = CommonUtils.checkNull(map.get("DEALER_LEVEL"));
		String dealerStatus = CommonUtils.checkNull(map.get("DEALER_STATUS"));
//		String orgCode = CommonUtils.checkNull(map.get("ORGCODE"));
		String sJDealerCode = CommonUtils.checkNull(map.get("sJDealerCode"));
		String companyName = CommonUtils.checkNull(map.get("COMPANY_NAME"));
		String serviceStatus = CommonUtils.checkNull(map.get("SERVICE_STATUS"));
		
		String regionId = CommonUtils.checkNull(map.get("regionId"));
		String orgCode = CommonUtils.checkNull(map.get("orgCode"));
		String orgId = CommonUtils.checkNull(map.get("orgId"));
		String parentOrgCode = CommonUtils.checkNull(map.get("parentOrgCode"));
		String AUTHORIZATION_TYPE = CommonUtils.checkNull(map.get("AUTHORIZATION_TYPE"));
		String WORK_TYPE = CommonUtils.checkNull(map.get("WORK_TYPE"));
		String IMAGE_LEVEL = CommonUtils.checkNull(map.get("IMAGE_LEVEL"));
		String SCREATE_DATE = CommonUtils.checkNull(map.get("SCREATE_DATE"));
		String IMAGE_COMFIRM_LEVEL = CommonUtils.checkNull(map.get("IMAGE_COMFIRM_LEVEL"));
		String ECREATE_DATE = CommonUtils.checkNull(map.get("ECREATE_DATE"));
		
		String regionCode = CommonUtils.checkNull(map.get("regionCode"));
		String status = CommonUtils.checkNull(map.get("status"));
		
		String AUTHORIZATION_SCREATE_DATE = CommonUtils.checkNull(map.get("AUTHORIZATION_SCREATE_DATE"));
		String AUTHORIZATION_ECREATE_DATE = CommonUtils.checkNull(map.get("AUTHORIZATION_ECREATE_DATE"));
		
		
		List<Object> params = new LinkedList<Object>();
		StringBuffer sbSql = new StringBuffer();
		
		sbSql.append("select a.dealer_id, a.dealer_code,\n");
		sbSql.append("       a.dealer_name,\n");
		sbSql.append("       a.DEALER_SHORTNAME,\n");
		sbSql.append("       a.status,\n");
		sbSql.append("       e.COMPANY_NAME,\n");
		sbSql.append("		 c.dealer_code parent_dealer_code,\n");
		sbSql.append("		 c.dealer_name parent_dealer_name,\n"); 
//		sbSql.append("       o.org_name,\n");
		sbSql.append("       a.dealer_level,\n");
		sbSql.append("       b.AUTHORIZATION_TYPE,\n");
		sbSql.append("       b.AUTHORIZATION_DATE,\n");
		sbSql.append("       b.WORK_TYPE,\n");
		sbSql.append("       DECODE(b.SHOP_TYPE,'直营','重点客户','代理','代理') SHOP_TYPE, \n");
		sbSql.append("       b.IMAGE_LEVEL,\n");
		sbSql.append("       b.IMAGE_COMFIRM_LEVEL,\n");
		sbSql.append("       a.service_status,\n");
		sbSql.append("       g.ROOT_ORG_NAME,\n");
		sbSql.append("       g.ROOT_ORG_ID,\n");
		sbSql.append("       f.REGION_NAME,\n");
//		sbSql.append("       h.remark as h_remark,\n");
		sbSql.append("       decode(a.SECEND_AUTID_STATUS,'92901001','保存','92901002','大区经理审核中','92901003','销售管理中心审核中','92901004','总经理审核中','92901005','驳回','92901006','总经理审核通过','92901007','审核通过','') as H_STATE,\n");
		if(logonUser.getDealerId()==null){
			sbSql.append("       'no' as is_dealer\n");
		}else{
			sbSql.append("       'yes' as is_dealer\n");
		}
		sbSql.append("  from tm_dealer              a,\n");
		sbSql.append("       tm_dealer_detail       b,\n");
		sbSql.append("       tm_dealer              c,\n");
//		sbSql.append("       tm_org                 o,\n");
		sbSql.append("       tm_company             e,\n");
		sbSql.append("       tm_region             f,\n");
		sbSql.append("       vw_org_dealer_service            g\n");
//		sbSql.append("       tt_dealer_secend_audit            h\n");
		sbSql.append(" where a.dealer_id = b.fk_dealer_id(+)\n");
		sbSql.append(" and a.parent_dealer_d = g.dealer_id(+)\n");
//		sbSql.append(" and a.dealer_id = h.dealer_id\n");
		sbSql.append("   and a.parent_dealer_d = c.dealer_id(+)\n");
		sbSql.append("   and a.dealer_level = 10851002\n");
		sbSql.append("   and a.company_id = e.company_id\n");
		sbSql.append("   and a.province_id = f.region_code\n");  
		
		if(!dealerType.equals("")) {
			sbSql.append("   and a.dealer_type = ?\n");
			params.add(dealerType);
		}
        if (!"".equals(AUTHORIZATION_SCREATE_DATE)) {
        	sbSql.append(" and AUTHORIZATION_DATE>= to_date('").append(AUTHORIZATION_SCREATE_DATE).append(" 00:00:00','YYYY/MM/dd HH24:mi:ss')");
        	sbSql.append(" and AUTHORIZATION_DATE<= to_date('").append(AUTHORIZATION_ECREATE_DATE).append(" 23:59:59','YYYY/MM/dd HH24:mi:ss')");
        }
		if(!dealerName.equals("")){
			sbSql.append("   and a.dealer_name like ?\n");
			params.add("%"+dealerName+"%");
		}
		if(!status.equals("")){
			sbSql.append("   and a.secend_autid_status = ?\n");
			params.add(status);
		}else{
			if(logonUser.getDealerId()==null){
				sbSql.append("   and a.secend_autid_status != 92901001\n");
			}
		}
		if(!dealerCode.equals("")){
			sbSql.append("   and a.dealer_code like ?\n");
			params.add("%"+dealerCode+"%");
		}
		
		if(!dealerLevel.equals("")) {
			sbSql.append("	 and a.dealer_level = ?\n");
			params.add(dealerLevel);
		}
		
		if(!dealerStatus.equals("")) {
			sbSql.append(" 	 and a.status = ?\n");
			params.add(dealerStatus);
		}

		if(!parentOrgCode.equals("")) {
			sbSql.append(" 	 and o.parent_org_id in (select org_id from tm_org where org_code = ?)\n");
			params.add(parentOrgCode);
		}

		if(!sJDealerCode.equals("")) {
			sbSql.append(" 	 and a.parent_dealer_d in (select dealer_id from tm_dealer where dealer_code = ?)\n");
			params.add(sJDealerCode);
		}
		
		if(!companyName.equals("")) {
			sbSql.append(" 	 and e.company_shortname = ?\n");
			params.add(companyName);
		}
		
		if(!serviceStatus.equals("")) {
			sbSql.append("   and a.service_status = ?\n");
			params.add(serviceStatus);
		}
		
		if(!IMAGE_LEVEL.equals("")) {
			sbSql.append("   and b.IMAGE_LEVEL = ?\n");
			params.add(IMAGE_LEVEL);
		}
		if(!IMAGE_COMFIRM_LEVEL.equals("")) {
			sbSql.append("   and b.IMAGE_COMFIRM_LEVEL = ?\n");
			params.add(IMAGE_COMFIRM_LEVEL);
		}
		if(dealerType.equals("10771001")){
			if(!WORK_TYPE.equals("")) {
				sbSql.append("   and b.SHOP_TYPE = ?\n");
				params.add(WORK_TYPE);
			}
		}else{
			if(!WORK_TYPE.equals("")) {
				sbSql.append("   and b.WORK_TYPE = ?\n");
				params.add(WORK_TYPE);
			}
		}
		if(!AUTHORIZATION_TYPE.equals("")) {
			sbSql.append("   and b.AUTHORIZATION_TYPE = ?\n");
			params.add(AUTHORIZATION_TYPE);
		}
		if (!CommonUtils.isNullString(orgCode)) {
			sbSql.append("   and o.org_Code = '").append(orgCode).append("'\n") ;
		}
		if (!CommonUtils.isNullString(orgId)) {
			sbSql.append("   and g.ROOT_ORG_ID = ").append(orgId).append("\n");
		}
		if (!CommonUtils.isNullString(regionCode)) {
			sbSql.append(Utility.getConSqlByParamForEqual(regionCode, params, "G", "ORG_Code"));
		}
		if(logonUser.getDealerId()==null){
			if(dealerType.equals("10771001")){
				sbSql.append(MaterialGroupManagerDao.getOrgDealerLimitSql("a.DEALER_ID", logonUser));
			}else{
				sbSql.append(MaterialGroupManagerDao.getOrgDealerLimitSqlForService("a.DEALER_ID", logonUser));
			}
		}else{
			sbSql.append("and a.PARENT_DEALER_D="+logonUser.getDealerId()+"");
		}

		
		sbSql.append("   order by g.ROOT_ORG_ID,g.org_code,g.dealer_id");
		return dao.pageQuery(sbSql.toString(), params, getFunName(),pageSize, curPage);
	}
	
public PageResult<Map<String, Object>> queryViConstructInfo(Map<String, Object> map,int curPage,int pageSize) throws Exception{
		
		String dealerType = CommonUtils.checkNull(map.get("DEALER_TYPE")); 
		String dealerCode = CommonUtils.checkNull(map.get("DEALER_CODE"));
		String dealerName = CommonUtils.checkNull(map.get("DEALER_NAME"));
		String dealerLevel = CommonUtils.checkNull(map.get("DEALER_LEVEL"));
		String dealerStatus = CommonUtils.checkNull(map.get("DEALER_STATUS"));
//		String orgCode = CommonUtils.checkNull(map.get("ORGCODE"));
		String sJDealerCode = CommonUtils.checkNull(map.get("sJDealerCode"));
		String companyName = CommonUtils.checkNull(map.get("COMPANY_NAME"));
		String serviceStatus = CommonUtils.checkNull(map.get("SERVICE_STATUS"));
		
		String regionId = CommonUtils.checkNull(map.get("regionId"));
		String orgCode = CommonUtils.checkNull(map.get("orgCode"));
		String orgId = CommonUtils.checkNull(map.get("orgId"));
		String parentOrgCode = CommonUtils.checkNull(map.get("parentOrgCode"));
		String AUTHORIZATION_TYPE = CommonUtils.checkNull(map.get("AUTHORIZATION_TYPE"));
		String WORK_TYPE = CommonUtils.checkNull(map.get("WORK_TYPE"));
		String IMAGE_LEVEL = CommonUtils.checkNull(map.get("IMAGE_LEVEL"));
		String SCREATE_DATE = CommonUtils.checkNull(map.get("SCREATE_DATE"));
		String IMAGE_COMFIRM_LEVEL = CommonUtils.checkNull(map.get("IMAGE_COMFIRM_LEVEL"));
		String ECREATE_DATE = CommonUtils.checkNull(map.get("ECREATE_DATE"));
		String STATUS = CommonUtils.checkNull(map.get("STATUS"));
		
		String regionCode = CommonUtils.checkNull(map.get("regionCode"));
		
		String AUTHORIZATION_SCREATE_DATE = CommonUtils.checkNull(map.get("AUTHORIZATION_SCREATE_DATE"));
		String AUTHORIZATION_ECREATE_DATE = CommonUtils.checkNull(map.get("AUTHORIZATION_ECREATE_DATE"));
		
		
		List<Object> params = new LinkedList<Object>();
		StringBuffer sbSql = new StringBuffer();
		
		sbSql.append("select a.dealer_id, a.dealer_code,\n");
		sbSql.append("       a.dealer_name,\n");
		sbSql.append("       a.DEALER_SHORTNAME,\n");
		sbSql.append("       a.status,\n");
		sbSql.append("       e.COMPANY_NAME,\n");
		sbSql.append("		 c.dealer_code parent_dealer_code,\n");
		sbSql.append("		 c.dealer_name parent_dealer_name,\n"); 
		sbSql.append("       o.org_name,\n");
		sbSql.append("       a.dealer_level,\n");
		sbSql.append("       b.AUTHORIZATION_TYPE,\n");
		sbSql.append("       b.AUTHORIZATION_DATE,\n");
		sbSql.append("       b.WORK_TYPE,\n");
		sbSql.append("       DECODE(b.SHOP_TYPE,'直营','重点客户','代理','代理') SHOP_TYPE, \n");
		sbSql.append("       b.IMAGE_LEVEL,\n");
		sbSql.append("       b.IMAGE_COMFIRM_LEVEL,\n");
		sbSql.append("       a.service_status,\n");
		sbSql.append("       g.ROOT_ORG_NAME,\n");
		sbSql.append("       g.ROOT_ORG_ID,\n");
//		sbSql.append("       g.REGION_NAME\n");
		sbSql.append("       f.REGION_NAME, \n");
//		sbSql.append("       decode(h.status,'92861000','已保存','92861001','已上报','92861002','渠道部审核中','92861003','销售部审核中','92861004','总经理审核中','92861005','驳回','92861006','终止','92861007','总经理审核通过','') as H_STATE\n");

        sbSql.append("(SELECT AA.is_have_audit FROM (SELECT '第'|| tvsd.year_flag || '年' || tvmg.group_name || '待审核' is_have_audit,tvcm.dealer_id \n");
        sbSql.append("FROM tt_vi_construct_detail tvsd, tm_vhcl_material_group tvmg, Tt_Vi_Construct_Main tvcm \n");
        sbSql.append("WHERE tvcm.vehicle_series_id=tvmg.group_id AND tvcm.ID=tvsd.ID AND tvsd.status=92861001 \n");
        sbSql.append("ORDER BY tvsd.year_flag,tvcm.vehicle_series_id) AA WHERE ROWNUM = 1 AND AA.dealer_id = a.dealer_id) is_have_audit, \n");
        sbSql.append("(SELECT NVL(TVCM.ID, '') FROM Tt_Vi_Construct_Main TVCM WHERE TVCM.VEHICLE_SERIES_ID=2014032694231206 AND tvcm.dealer_id = a.dealer_id) main_id_suv, \n");
        sbSql.append("(SELECT NVL(TVCM.ID, '') FROM Tt_Vi_Construct_Main TVCM WHERE TVCM.dealer_id = a.dealer_id AND TVCM.VEHICLE_SERIES_ID=2015011508783002) main_id_mpv  \n");
		sbSql.append("  from tm_dealer              a,\n");
		sbSql.append("       tm_dealer_detail       b,\n");
		sbSql.append("       tm_dealer              c,\n");
		sbSql.append("       tm_org                 o,\n");
		sbSql.append("       --tm_dealer_org_relation d,\n");
		sbSql.append("       tm_company             e,\n");
		sbSql.append("       tm_region             f,\n");
		sbSql.append("       vw_org_dealer_service            g\n");
		sbSql.append(" where a.dealer_id = b.fk_dealer_id(+)\n");
		sbSql.append(" and a.dealer_id = g.dealer_id\n");
		sbSql.append("   and a.parent_dealer_d = c.dealer_id(+)\n");
		sbSql.append("   --and a.dealer_id = d.dealer_id\n");
		sbSql.append("   and o.org_id = a.dealer_org_id\n");
		sbSql.append("   and a.company_id = e.company_id\n");
		sbSql.append("   and a.province_id = f.region_code\n");  
		
		if(!dealerType.equals("")) {
			sbSql.append("   and a.dealer_type = ?\n");
			params.add(dealerType);
		}
        if (!"".equals(AUTHORIZATION_SCREATE_DATE)) {
        	sbSql.append(" and AUTHORIZATION_DATE>= to_date('").append(AUTHORIZATION_SCREATE_DATE).append(" 00:00:00','YYYY/MM/dd HH24:mi:ss')");
        	sbSql.append(" and AUTHORIZATION_DATE<= to_date('").append(AUTHORIZATION_ECREATE_DATE).append(" 23:59:59','YYYY/MM/dd HH24:mi:ss')");
        }
		if(!dealerName.equals("")){
			sbSql.append("   and a.dealer_name like ?\n");
			params.add("%"+dealerName+"%");
		}
		
		if(!dealerCode.equals("")){
			sbSql.append("   and a.dealer_code like ?\n");
			params.add("%"+dealerCode+"%");
		}
		
		if(!dealerLevel.equals("")) {
			sbSql.append("	 and a.dealer_level = ?\n");
			params.add(dealerLevel);
		}
		
		if(!dealerStatus.equals("")) {
			sbSql.append(" 	 and a.status = ?\n");
			params.add(dealerStatus);
		}

		if(!parentOrgCode.equals("")) {
			sbSql.append(" 	 and o.parent_org_id in (select org_id from tm_org where org_code = ?)\n");
			params.add(parentOrgCode);
		}

		if(!sJDealerCode.equals("")) {
			sbSql.append(" 	 and a.parent_dealer_d in (select dealer_id from tm_dealer where dealer_code = ?)\n");
			params.add(sJDealerCode);
		}
		
		if(!companyName.equals("")) {
			sbSql.append(" 	 and e.company_shortname = ?\n");
			params.add(companyName);
		}
		
		if(!serviceStatus.equals("")) {
			sbSql.append("   and a.service_status = ?\n");
			params.add(serviceStatus);
		}
		
		if(!IMAGE_LEVEL.equals("")) {
			sbSql.append("   and b.IMAGE_LEVEL = ?\n");
			params.add(IMAGE_LEVEL);
		}
		if(!IMAGE_COMFIRM_LEVEL.equals("")) {
			sbSql.append("   and b.IMAGE_COMFIRM_LEVEL = ?\n");
			params.add(IMAGE_COMFIRM_LEVEL);
		}
		if(dealerType.equals("10771001")){
			if(!WORK_TYPE.equals("")) {
				sbSql.append("   and b.SHOP_TYPE = ?\n");
				params.add(WORK_TYPE);
			}
		}else{
			if(!WORK_TYPE.equals("")) {
				sbSql.append("   and b.WORK_TYPE = ?\n");
				params.add(WORK_TYPE);
			}
		}
		if(!AUTHORIZATION_TYPE.equals("")) {
			sbSql.append("   and b.AUTHORIZATION_TYPE = ?\n");
			params.add(AUTHORIZATION_TYPE);
		}
		if(!STATUS.equals("")) {
			sbSql.append("   and h.STATUS = ?\n");
			params.add(STATUS);
		}
		if (!CommonUtils.isNullString(orgCode)) {
			sbSql.append("   and o.org_Code = '").append(orgCode).append("'\n") ;
		}
		if (!CommonUtils.isNullString(orgId)) {
			sbSql.append("   and g.ROOT_ORG_ID = ").append(orgId).append("\n");
		}
		if (!CommonUtils.isNullString(regionCode)) {
			sbSql.append(Utility.getConSqlByParamForEqual(regionCode, params, "G", "ORG_Code"));
		}
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		if(dealerType.equals("10771001")){
			sbSql.append(MaterialGroupManagerDao.getOrgDealerLimitSql("a.DEALER_ID", logonUser));
		}else{
			sbSql.append(MaterialGroupManagerDao.getOrgDealerLimitSqlForService("a.DEALER_ID", logonUser));
		}
		
		sbSql.append("   order by g.ROOT_ORG_ID,o.org_name,a.dealer_code");
		return dao.pageQuery(sbSql.toString(), params, getFunName(),pageSize, curPage);
	}

public PageResult<Map<String, Object>> queryViConstructAudit(Map<String, Object> map,int curPage,int pageSize) throws Exception{
	
	String dealerType = CommonUtils.checkNull(map.get("DEALER_TYPE")); 
	String dealerCode = CommonUtils.checkNull(map.get("DEALER_CODE"));
	String dealerName = CommonUtils.checkNull(map.get("DEALER_NAME"));
	String dealerLevel = CommonUtils.checkNull(map.get("DEALER_LEVEL"));
	String dealerStatus = CommonUtils.checkNull(map.get("DEALER_STATUS"));
//	String orgCode = CommonUtils.checkNull(map.get("ORGCODE"));
	String sJDealerCode = CommonUtils.checkNull(map.get("sJDealerCode"));
	String companyName = CommonUtils.checkNull(map.get("COMPANY_NAME"));
	String serviceStatus = CommonUtils.checkNull(map.get("SERVICE_STATUS"));
	
	String regionId = CommonUtils.checkNull(map.get("regionId"));
	String orgCode = CommonUtils.checkNull(map.get("orgCode"));
	String orgId = CommonUtils.checkNull(map.get("orgId"));
	String parentOrgCode = CommonUtils.checkNull(map.get("parentOrgCode"));
	String AUTHORIZATION_TYPE = CommonUtils.checkNull(map.get("AUTHORIZATION_TYPE"));
	String WORK_TYPE = CommonUtils.checkNull(map.get("WORK_TYPE"));
	String IMAGE_LEVEL = CommonUtils.checkNull(map.get("IMAGE_LEVEL"));
	String SCREATE_DATE = CommonUtils.checkNull(map.get("SCREATE_DATE"));
	String IMAGE_COMFIRM_LEVEL = CommonUtils.checkNull(map.get("IMAGE_COMFIRM_LEVEL"));
	String ECREATE_DATE = CommonUtils.checkNull(map.get("ECREATE_DATE"));
	String STATUS = CommonUtils.checkNull(map.get("STATUS"));
	String user_id = CommonUtils.checkNull(map.get("user_id"));
	String auditYear = CommonUtils.checkNull(map.get("auditYear"));
	
	String regionCode = CommonUtils.checkNull(map.get("regionCode"));
	
	String AUTHORIZATION_SCREATE_DATE = CommonUtils.checkNull(map.get("AUTHORIZATION_SCREATE_DATE"));
	String AUTHORIZATION_ECREATE_DATE = CommonUtils.checkNull(map.get("AUTHORIZATION_ECREATE_DATE"));
	
	
	List<Object> params = new LinkedList<Object>();
	StringBuffer sbSql = new StringBuffer();
	
	sbSql.append("select a.dealer_id, a.dealer_code,\n");
	sbSql.append("       a.dealer_name,\n");
	sbSql.append("       a.DEALER_SHORTNAME,\n");
	sbSql.append("       a.status,\n");
	sbSql.append("       e.COMPANY_NAME,\n");
	sbSql.append("		 c.dealer_code parent_dealer_code,\n");
	sbSql.append("		 c.dealer_name parent_dealer_name,\n"); 
	sbSql.append("       o.org_name,\n");
	sbSql.append("       a.dealer_level,\n");
	sbSql.append("       b.AUTHORIZATION_TYPE,\n");
	sbSql.append("       b.AUTHORIZATION_DATE,\n");
	sbSql.append("       b.WORK_TYPE,\n");
	sbSql.append("       DECODE(b.SHOP_TYPE,'直营','重点客户','代理','代理') SHOP_TYPE, \n");
	sbSql.append("       b.IMAGE_LEVEL,\n");
	sbSql.append("       b.IMAGE_COMFIRM_LEVEL,\n");
	sbSql.append("       a.service_status,\n");
	sbSql.append("       g.ROOT_ORG_NAME,\n");
	sbSql.append("       g.ROOT_ORG_ID,\n");
	sbSql.append("       f.REGION_NAME,\n");
	sbSql.append("       (SELECT '第'|| AA.YEAR_FLAG || '年SUV待审核' is_have_audit \n");
	sbSql.append("       FROM (SELECT TVCD.YEAR_FLAG,TVCD.DEALER_ID FROM tt_vi_construct_detail TVCD,tt_vi_construct_main TVCM  \n");
	sbSql.append("       WHERE TVCD.ID=TVCM.ID AND TVCD.status=92861001 AND TVCM.VEHICLE_SERIES_ID=2014032694231206 \n");
	if (!StringUtils.isEmpty(auditYear)) {
		sbSql.append("         AND TVCD.YEAR_FLAG = '").append(auditYear).append("'\n");
	}
	sbSql.append("       ORDER BY TVCD.YEAR_FLAG) AA WHERE AA.DEALER_ID=A.DEALER_ID AND ROWNUM=1) is_have_audit_suv,\n");
	sbSql.append("       (SELECT '第'|| AA.YEAR_FLAG || '年MPV待审核' is_have_audit \n");
	sbSql.append("       FROM (SELECT TVCD.YEAR_FLAG,TVCD.DEALER_ID FROM tt_vi_construct_detail TVCD,tt_vi_construct_main TVCM  \n");
	sbSql.append("       WHERE TVCD.ID=TVCM.ID AND TVCD.status=92861001 AND TVCM.VEHICLE_SERIES_ID=2015011508783002 \n");
	if (!StringUtils.isEmpty(auditYear)) {
		sbSql.append("         AND TVCD.YEAR_FLAG = '").append(auditYear).append("'\n");
	}
	sbSql.append("       ORDER BY TVCD.YEAR_FLAG) AA WHERE AA.DEALER_ID=A.DEALER_ID AND ROWNUM=1) is_have_audit_mpv \n");
	sbSql.append("  from tm_dealer              a,\n");
	sbSql.append("       tm_dealer_detail       b,\n");
	sbSql.append("       tm_dealer              c,\n");
	sbSql.append("       tm_org                 o,\n");
	sbSql.append("       --tm_dealer_org_relation d,\n");
	sbSql.append("       tm_company             e,\n");
	sbSql.append("       tm_region             f,\n");
	sbSql.append("       vw_org_dealer_service            g\n");
	sbSql.append(" where a.dealer_id = b.fk_dealer_id(+)\n");
	sbSql.append(" and a.dealer_id = g.dealer_id\n");
	sbSql.append("   and a.parent_dealer_d = c.dealer_id(+)\n");
	sbSql.append("   and o.org_id = a.dealer_org_id\n");
	sbSql.append("   and a.company_id = e.company_id\n");
	sbSql.append("   and a.province_id = f.region_code\n");  
	if(!dealerType.equals("")) {
		sbSql.append("   and a.dealer_type = ?\n");
		params.add(dealerType);
	}
    if (!"".equals(AUTHORIZATION_SCREATE_DATE)) {
    	sbSql.append(" and AUTHORIZATION_DATE>= to_date('").append(AUTHORIZATION_SCREATE_DATE).append(" 00:00:00','YYYY/MM/dd HH24:mi:ss')");
    	sbSql.append(" and AUTHORIZATION_DATE<= to_date('").append(AUTHORIZATION_ECREATE_DATE).append(" 23:59:59','YYYY/MM/dd HH24:mi:ss')");
    }
	if(!dealerName.equals("")){
		sbSql.append("   and a.dealer_name like ?\n");
		params.add("%"+dealerName+"%");
	}
	
	if(!dealerCode.equals("")){
		sbSql.append("   and a.dealer_code like ?\n");
		params.add("%"+dealerCode+"%");
	}
	
	if(!dealerLevel.equals("")) {
		sbSql.append("	 and a.dealer_level = ?\n");
		params.add(dealerLevel);
	}
	
	if(!dealerStatus.equals("")) {
		sbSql.append(" 	 and a.status = ?\n");
		params.add(dealerStatus);
	}

	if(!parentOrgCode.equals("")) {
		sbSql.append(" 	 and o.parent_org_id in (select org_id from tm_org where org_code = ?)\n");
		params.add(parentOrgCode);
	}

	if(!sJDealerCode.equals("")) {
		sbSql.append(" 	 and a.parent_dealer_d in (select dealer_id from tm_dealer where dealer_code = ?)\n");
		params.add(sJDealerCode);
	}
	
	if(!companyName.equals("")) {
		sbSql.append(" 	 and e.company_shortname = ?\n");
		params.add(companyName);
	}
	
	if(!serviceStatus.equals("")) {
		sbSql.append("   and a.service_status = ?\n");
		params.add(serviceStatus);
	}
	
	if(!IMAGE_LEVEL.equals("")) {
		sbSql.append("   and b.IMAGE_LEVEL = ?\n");
		params.add(IMAGE_LEVEL);
	}
	if(!IMAGE_COMFIRM_LEVEL.equals("")) {
		sbSql.append("   and b.IMAGE_COMFIRM_LEVEL = ?\n");
		params.add(IMAGE_COMFIRM_LEVEL);
	}
	if(dealerType.equals("10771001")){
		if(!WORK_TYPE.equals("")) {
			sbSql.append("   and b.SHOP_TYPE = ?\n");
			params.add(WORK_TYPE);
		}
	}else{
		if(!WORK_TYPE.equals("")) {
			sbSql.append("   and b.WORK_TYPE = ?\n");
			params.add(WORK_TYPE);
		}
	}
	if(!AUTHORIZATION_TYPE.equals("")) {
		sbSql.append("   and b.AUTHORIZATION_TYPE = ?\n");
		params.add(AUTHORIZATION_TYPE);
	}
	if(!STATUS.equals("")) {
		sbSql.append("   and h.STATUS = ?\n");
		params.add(STATUS);
	}
	if (!CommonUtils.isNullString(orgCode)) {
		sbSql.append("   and o.org_Code = '").append(orgCode).append("'\n") ;
	}
	if (!CommonUtils.isNullString(orgId)) {
		sbSql.append("   and g.ROOT_ORG_ID = ").append(orgId).append("\n");
	}
	if (!CommonUtils.isNullString(regionCode)) {
		sbSql.append(Utility.getConSqlByParamForEqual(regionCode, params, "G", "ORG_Code"));
	}
	ActionContext act = ActionContext.getContext();
	AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
	if(dealerType.equals("10771001")){
		sbSql.append(MaterialGroupManagerDao.getOrgDealerLimitSql("a.DEALER_ID", logonUser));
	}else{
		sbSql.append(MaterialGroupManagerDao.getOrgDealerLimitSqlForService("a.DEALER_ID", logonUser));
	}
	if (!StringUtils.isEmpty(auditYear)) {
		sbSql.append(" and a.DEALER_ID IN (SELECT TVCD.DEALER_ID FROM tt_vi_construct_detail TVCD \n");
		sbSql.append(" WHERE TVCD.status=92861001 AND TVCD.YEAR_FLAG = '").append(auditYear).append("') \n");
	}
	sbSql.append("   order by g.ROOT_ORG_ID,o.org_name,a.dealer_code");
	return dao.pageQuery(sbSql.toString(), params, getFunName(),pageSize, curPage);
}
	
	public PageResult<Map<String, Object>> queryServiceDealerInfo(Map<String, Object> map,int curPage,int pageSize) throws Exception{
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		String dealerType = CommonUtils.checkNull(map.get("DEALER_TYPE")); 
		String dealerCode = CommonUtils.checkNull(map.get("DEALER_CODE"));
		String dealerName = CommonUtils.checkNull(map.get("DEALER_NAME"));
		String dealerLevel = CommonUtils.checkNull(map.get("DEALER_LEVEL"));
		String dealerStatus = CommonUtils.checkNull(map.get("DEALER_STATUS"));
		String parentOrgCode = CommonUtils.checkNull(map.get("parentOrgCode"));
		String sJDealerCode = CommonUtils.checkNull(map.get("sJDealerCode"));
		String companyName = CommonUtils.checkNull(map.get("COMPANY_NAME"));
		String serviceStatus = CommonUtils.checkNull(map.get("SERVICE_STATUS"));
		String DEALER_ID = CommonUtils.checkNull(map.get("DEALER_ID"));
		
		String regionId = CommonUtils.checkNull(map.get("regionId"));
		String AUTHORIZATION_TYPE = CommonUtils.checkNull(map.get("AUTHORIZATION_TYPE"));
		String orgCode = CommonUtils.checkNull(map.get("orgCode"));
		String orgId = CommonUtils.checkNull(map.get("orgId"));
		String SCREATE_DATE = CommonUtils.checkNull(map.get("SCREATE_DATE"));
		String ECREATE_DATE = CommonUtils.checkNull(map.get("ECREATE_DATE"));
		
		List<Object> params = new LinkedList<Object>();
		StringBuffer sbSql = new StringBuffer();
		
		sbSql.append("select a.dealer_id, a.dealer_code,\n");
		sbSql.append("       a.dealer_name,\n");
		sbSql.append("       a.status,\n");
		sbSql.append("		 c.dealer_code parent_dealer_code,\n");
		sbSql.append("		 c.dealer_name parent_dealer_name,\n"); 
		sbSql.append("       o.org_name,\n");
		sbSql.append("       a.dealer_level,\n");
		sbSql.append("       a.DEALER_SHORTNAME,\n");
		sbSql.append("       e.company_name,\n");
		sbSql.append("       a.service_status,\n");
		sbSql.append("       g.ROOT_ORG_ID,\n");
		sbSql.append("       g.ROOT_ORG_NAME,\n");
		sbSql.append("       f.REGION_NAME,\n");
		sbSql.append("       b.AUTHORIZATION_TYPE\n");
		sbSql.append("  from tm_dealer              a,\n");
		sbSql.append("       tm_dealer_detail       b,\n");
		sbSql.append("       tm_dealer              c,\n");
		sbSql.append("       tm_org                 o,\n");
		sbSql.append("       --tm_dealer_org_relation d,\n");
		sbSql.append("       tm_company             e,\n");
		sbSql.append("       tm_region             f,\n");
		sbSql.append("       vw_org_dealer_service            g\n");
		sbSql.append(" where a.dealer_id = b.fk_dealer_id(+)\n");
		sbSql.append("   and a.dealer_id = g.dealer_id(+)\n");
		sbSql.append("   and a.parent_dealer_d = c.dealer_id(+)\n");
		sbSql.append("   --and a.dealer_id = d.dealer_id\n");
		sbSql.append("   and o.org_id = a.dealer_org_id\n");
		sbSql.append("   and a.company_id = e.company_id\n");
		sbSql.append("   and a.province_id = f.region_code\n");
		if(!dealerType.equals("")) {
			sbSql.append("   and a.dealer_type = ?\n");
			params.add(dealerType);
		}
		
		if(!dealerName.equals("")){
			sbSql.append("   and a.dealer_name like ?\n");
			params.add("%"+dealerName+"%");
		}
		
		if(!dealerCode.equals("")){
			sbSql.append("   and a.dealer_code like ?\n");
			params.add("%"+dealerCode+"%");
		}
		
		if(!dealerLevel.equals("")) {
			sbSql.append("	 and a.dealer_level = ?\n");
			params.add(dealerLevel);
		}
		
		if(!dealerStatus.equals("")) {
			sbSql.append(" 	 and a.status = ?\n");
			params.add(dealerStatus);
		}

		if(!parentOrgCode.equals("")) {
			sbSql.append(" 	 and o.org_id in (select org_id from tm_org where org_code = ?)\n");
			params.add(parentOrgCode);
		}

		if(!sJDealerCode.equals("")) {
			sbSql.append(" 	 and a.parent_dealer_d in (select dealer_id from tm_dealer where dealer_code = ?)\n");
			params.add(sJDealerCode);
		}
		
		if(!companyName.equals("")) {
			sbSql.append(" 	 and e.company_shortname = ?\n");
			params.add(companyName);
		}
		
		if(!serviceStatus.equals("")) {
			sbSql.append("   and a.service_status = ?\n");
			params.add(serviceStatus);
		}
		if(!StringUtil.isNull(logonUser.getDealerId())) {
			sbSql.append("   and a.DEALER_ID = ?\n");
			params.add(logonUser.getDealerId());
		}else{
			sbSql.append(MaterialGroupManagerDao.getOrgDealerLimitSqlForService("a.DEALER_ID", logonUser));
		}
		if(!AUTHORIZATION_TYPE.equals("")) {
			sbSql.append("   and b.AUTHORIZATION_TYPE = ?\n");
			params.add(AUTHORIZATION_TYPE);
		}
		if (!CommonUtils.isNullString(regionId)) {
			sbSql.append("   and g.org_id in (").append(regionId).append(")\n") ;
		}
		if (!CommonUtils.isNullString(orgCode)) {
			sbSql.append("   and o.org_Code = '").append(orgCode).append("'\n") ;
		}
		if(!orgId.equals("")) {
			sbSql.append("   AND g.ROOT_org_id=").append(orgId);
		}
		sbSql.append(MaterialGroupManagerDao.getPoseIdBusinessSql(Constant.areaId, logonUser.getPoseId().toString()));
		
		sbSql.append("  order by g.ROOT_ORG_ID");
		return dao.pageQuery(sbSql.toString(), params, getFunName(),pageSize, curPage);
	}
	
	
	public PageResult<Map<String, Object>> queryServiceDealerInfo2nd(Map<String, Object> map,int curPage,int pageSize) throws Exception{
		AclUserBean logonUser = (AclUserBean) map.get("logonUser");
		String dealerCode = CommonUtils.checkNull(map.get("DEALER_CODE"));
		String dealerName = CommonUtils.checkNull(map.get("DEALER_NAME"));
		
		String FIRST_DEALER_CODE = CommonUtils.checkNull(map.get("FIRST_DEALER_CODE"));
		String FIRST_DEALER_NAME = CommonUtils.checkNull(map.get("FIRST_DEALER_NAME"));
		
		String dealerLevel = CommonUtils.checkNull(map.get("DEALER_LEVEL"));
		String dealerStatus = CommonUtils.checkNull(map.get("DEALER_STATUS"));
		String parentOrgCode = CommonUtils.checkNull(map.get("parentOrgCode"));
		String sJDealerCode = CommonUtils.checkNull(map.get("sJDealerCode"));
		String companyName = CommonUtils.checkNull(map.get("COMPANY_NAME"));
		String serviceStatus = CommonUtils.checkNull(map.get("SERVICE_STATUS"));
		String DEALER_ID = logonUser.getDealerId();
		String regionCode = CommonUtils.checkNull(map.get("regionCode"));
		String AUTHORIZATION_TYPE = CommonUtils.checkNull(map.get("AUTHORIZATION_TYPE"));
		String orgCode = CommonUtils.checkNull(map.get("orgCode"));
		String orgId = CommonUtils.checkNull(map.get("orgId"));
		
		String user_id = CommonUtils.checkNull(map.get("user_id"));
		String status = CommonUtils.checkNull(map.get("status"));
		
		String CendDate = CommonUtils.checkNull(map.get("CendDate"));
		String CstartDate = CommonUtils.checkNull(map.get("CstartDate"));
		String UstartDate = CommonUtils.checkNull(map.get("UstartDate"));
		String UendDate = CommonUtils.checkNull(map.get("UendDate"));
		
		String IS_QUALIFIED_SERVICE = CommonUtils.checkNull(map.get("IS_QUALIFIED_SERVICE"));
		String IS_QUALIFIED_SALES = CommonUtils.checkNull(map.get("IS_QUALIFIED_SALES"));
		
		List<Object> params = new LinkedList<Object>();
		StringBuffer sbSql = new StringBuffer();
		
		sbSql.append("select * from (select e.company_Name as ecompany_Name,a.status,a.PARENT_DEALER_D,a.secend_autid_status,g.org_code,a.dealer_id, a.dealer_code,\n");
		sbSql.append("       a.dealer_name,\n");
		sbSql.append("       decode(a.SECEND_AUTID_STATUS,'92901001','保存','92901002','大区经理审核中','92901003','销售管理中心审核中','92901004','总经理审核中','92901005','驳回','92901006','总经理审核通过','92901007','审核通过','') as H_STATE,\n");
		sbSql.append("		 c.dealer_code parent_dealer_code,\n");
		sbSql.append("		 c.dealer_name parent_dealer_name,\n"); 
		sbSql.append("       a.dealer_level,\n");
		sbSql.append("       a.DEALER_SHORTNAME,\n");
		sbSql.append("       e.company_name,\n");
		sbSql.append("       a.service_status,\n");
		sbSql.append("       g.ROOT_ORG_ID,\n");
		sbSql.append("       g.ROOT_ORG_NAME,\n");
		sbSql.append("       f.REGION_NAME,\n");
		sbSql.append("       b.AUTHORIZATION_TYPE,\n");
		sbSql.append("       b.is_qualified_sales,\n");
		sbSql.append("       b.is_qualified_service,\n");
		sbSql.append("       (select max(create_date) from tt_dealer_secend_audit where dealer_id = a.dealer_id  and status in(92901002)) create_date,\n");
		sbSql.append("       (select max(create_date) from tt_dealer_secend_audit where dealer_id = a.dealer_id  and status in(92901003,92901004,92901006)) audit_date\n");
		sbSql.append("  from tm_dealer              a,\n");
		sbSql.append("       tm_dealer_detail       b,\n");
		sbSql.append("       tm_dealer              c,\n");
		sbSql.append("       tm_company             e,\n");
		sbSql.append("       tm_region              f,\n");
		sbSql.append("       vw_org_dealer_service             g\n");
		sbSql.append(" where a.dealer_id = b.fk_dealer_id(+)\n");
		sbSql.append("   and a.dealer_id = g.dealer_id(+)\n");
		sbSql.append("   and a.parent_dealer_d = c.dealer_id(+)\n");
		sbSql.append("   and a.dealer_level = 10851002\n");
		sbSql.append("   and a.company_id = e.company_id(+)\n");
		sbSql.append("   and a.province_id = f.region_code(+)\n");
		sbSql.append("   and a.dealer_type = 10771001) aa where 1=1\n");
		
		if(!CstartDate.equals("")){
			sbSql.append("   and trunc(create_date)>=trunc(to_date('"+CstartDate+"','yyyy-mm-dd'))\n");
		}
		if(!CendDate.equals("")){
			sbSql.append("   and trunc(create_date)<=trunc(to_date('"+CendDate+"','yyyy-mm-dd'))\n");
		}
		if(!UstartDate.equals("")){
			sbSql.append("   and trunc(audit_date)>=trunc(to_date('"+UstartDate+"','yyyy-mm-dd'))\n");
		}
		if(!UendDate.equals("")){
			sbSql.append("   and trunc(audit_date)<=trunc(to_date('"+UendDate+"','yyyy-mm-dd'))\n");
		}
		
		if(!user_id.equals("")) {
			if(user_id.equals(Constant.DEALER_SECEND_AUDIT_02.toString())){
				sbSql.append("   and SECEND_AUTID_STATUS = 92901002\n");
				sbSql.append(MaterialGroupManagerDao.getOrgDealerLimitSql("DEALER_ID", logonUser));
			}else if(user_id.equals(Constant.DEALER_SECEND_AUDIT_03.toString())){
				sbSql.append(MaterialGroupManagerDao.getOrgDealerLimitSql("DEALER_ID", logonUser));
				sbSql.append("   and SECEND_AUTID_STATUS = 92901003\n");
			}else if(user_id.equals(Constant.DEALER_SECEND_AUDIT_04.toString())){
				sbSql.append(MaterialGroupManagerDao.getOrgDealerLimitSql("DEALER_ID", logonUser));
				sbSql.append("   and SECEND_AUTID_STATUS = 92901004\n");
				if(!StringUtil.isNull(IS_QUALIFIED_SALES)){
					sbSql.append("   and IS_QUALIFIED_SALES =?\n");
					params.add(IS_QUALIFIED_SALES);
				}
				if(!StringUtil.isNull(IS_QUALIFIED_SERVICE)){
					sbSql.append("   and IS_QUALIFIED_SERVICE =?\n");
					params.add(IS_QUALIFIED_SERVICE);
				}
			}
		}
		if(!StringUtil.isNull(dealerStatus)){
			sbSql.append("   and status =?\n");
			params.add(dealerStatus);
		}
		
		if(!StringUtil.isNull(companyName)){
			sbSql.append("   and ecompany_Name like ?\n");
			params.add("%"+companyName+"%");
		}
		
		if(!status.equals("")) {
			sbSql.append("   and secend_autid_status = ?\n");
			params.add(status);
		}else{
		}
		if(!dealerName.equals("")){
			sbSql.append("   and dealer_name like ?\n");
			params.add("%"+dealerName+"%");
		}
		if(!FIRST_DEALER_CODE.equals("")){
			sbSql.append("   and parent_dealer_code like ?\n");
			params.add("%"+FIRST_DEALER_CODE+"%");
		}
		if(!StringUtil.isNull(DEALER_ID)){
			sbSql.append("   and PARENT_DEALER_D =?\n");
			params.add(DEALER_ID);
		}
		if(!dealerCode.equals("")){
			sbSql.append("   and dealer_code like ?\n");
			params.add("%"+dealerCode+"%");
		}
		if(!FIRST_DEALER_NAME.equals("")){
			sbSql.append("   and parent_dealer_name like ?\n");
			params.add("%"+FIRST_DEALER_NAME+"%");
		}
		
		if(!parentOrgCode.equals("")) {
			sbSql.append(" 	 and parent_org_id in (select org_id from tm_org where org_code = ?)\n");
			params.add(parentOrgCode);
		}
		
		if(!serviceStatus.equals("")) {
			sbSql.append("   and service_status = ?\n");
			params.add(serviceStatus);
		}
		if(!AUTHORIZATION_TYPE.equals("")) {
			sbSql.append("   and AUTHORIZATION_TYPE = ?\n");
			params.add(AUTHORIZATION_TYPE);
		}
		if (!CommonUtils.isNullString(regionCode)) {
			sbSql.append(Utility.getConSqlByParamForEqual(regionCode, params, "aa", "ORG_Code"));
		}
		if (!CommonUtils.isNullString(orgCode)) {
			sbSql.append("   and org_Code = '").append(orgCode).append("'\n") ;
		}
		if(!orgId.equals("")) {
			sbSql.append("   AND ROOT_org_id=").append(orgId);
		}
		
		sbSql.append("  order by ROOT_ORG_ID,org_code,dealer_id,create_date desc");
		return dao.pageQuery(sbSql.toString(), params, getFunName(),pageSize, curPage);
	}
	
	public List<Map<String, Object>> dealerInfoDownload(Map<String, String> map) {
		String dealerClass = map.get("dealerClass") ;
		String dealerCode = map.get("dealerCode") ;
		String dealerName = map.get("dealerName") ;
		String dealerLevel = map.get("dealerLevel") ;
		String status = map.get("status") ;
		String sJdealerCode = map.get("sJdealerCode") ;
		String orgCode = map.get("orgCode") ;
		String dealerType = map.get("dealerType") ;
		String companyId = map.get("companyId") ;
		String oemCompanyId = map.get("oemCompanyId") ;
		String province = map.get("province") ;
		String pdealerType = map.get("pdealerType");
		String serviceStatus = map.get("serviceStatus");
		String areaId = map.get("areaId");
		
		StringBuffer sbSql = new StringBuffer() ;
		
		sbSql.append("WITH A_TAB AS\n");
		sbSql.append(" (SELECT TD.DEALER_ID,\n");
		sbSql.append("         TD.DEALER_CODE,\n");
		sbSql.append("         TD.DEALER_SHORTNAME,\n");
		sbSql.append("         TD.DEALER_NAME,\n");
		sbSql.append("         TD1.DEALER_NAME CDEALER_NAME,\n");
		sbSql.append("         TD.DEALER_TYPE,\n");
		sbSql.append("         TD.STATUS,\n");
		sbSql.append("         TD.DEALER_LEVEL,\n");
		sbSql.append("         TD.CREATE_BY,\n");
		sbSql.append("         TD.UPDATE_BY,\n");
		sbSql.append("         TD.CREATE_DATE,\n");
		sbSql.append("         TD.UPDATE_DATE,\n");
		sbSql.append("         TMO.ORG_NAME AS SHANGJIORGNAME,\n");
		sbSql.append("         TD.PDEALER_TYPE,\n");
		sbSql.append("         TD.PARENT_DEALER_D,\n");
		sbSql.append("         TC.COMPANY_SHORTNAME,\n");
		sbSql.append("         TD.PROVINCE_ID,\n");
		sbSql.append("         TD.CITY_ID,\n");
		sbSql.append("         TD.COUNTIES,\n");
		sbSql.append("         TD.ZIP_CODE,\n");
		sbSql.append("         TD.ADDRESS,\n");
		sbSql.append("         TD.PHONE,\n");
		sbSql.append("         TD.FAX_NO,\n");
		sbSql.append("         TD.LINK_MAN,\n");
		sbSql.append("         TD.EMAIL,\n");
		sbSql.append("         TD.LEGAL,\n");
		sbSql.append("         TD.DUTY_PHONE,\n");
		sbSql.append("         TD.ZZADDRESS,\n");
		sbSql.append("         TD.CH_ADDRESS,\n");
		sbSql.append("         TD.CH_ADDRESS2,\n");
		sbSql.append("         TD.WEBMASTER_NAME,\n");
		sbSql.append("         TD.DEALER_LABOUR_TYPE\n");
		sbSql.append("    FROM TM_COMPANY              TC,\n");
		sbSql.append("         TM_ORG                  TMO,\n");
		sbSql.append("         TM_DEALER_ORG_RELATION  TDOR,\n");
		sbSql.append("         TM_DEALER               TD,\n");
		sbSql.append("         TM_DEALER               TD1,\n");
		sbSql.append("         TM_DEALER_BUSINESS_AREA TDBA\n");
		sbSql.append("   WHERE TD.COMPANY_ID = TC.COMPANY_ID\n");
		sbSql.append("     AND TD.DEALER_ID = TDBA.DEALER_ID(+)\n"); 
		sbSql.append("     AND TD.PARENT_DEALER_D=TD1.DEALER_ID(+)\n"); 

		if(dealerCode!=null&&!"".equals(dealerCode))
		{
			sbSql.append("           AND TD.DEALER_CODE LIKE '%"+dealerCode+"%'\n"); 
		}
		if(dealerName!=null&&!"".equals(dealerName))
		{
			sbSql.append("           AND TD.DEALER_SHORTNAME LIKE '%"+dealerName+"%'\n"); 
		}
		if(status!=null&&!"".equals(status))
		{
			sbSql.append("           AND TD.STATUS = "+status+"\n"); 
		}
		if(dealerType!=null&&!"".equals(dealerType))
		{
			sbSql.append("           AND TD.DEALER_TYPE = "+dealerType+"\n"); 
		}
		sbSql.append("           AND F_GET_PID(TD.DEALER_ID) = TDOR.DEALER_ID \n"); 
		sbSql.append("           AND TDOR.ORG_ID = TMO.ORG_ID\n"); 
		if(dealerLevel!=null&&!"".equals(dealerLevel))
		{
			sbSql.append("           AND TD.DEALER_LEVEL = "+dealerLevel+"\n"); 
		}
		if(orgCode!=null&&!"".equals(orgCode))
		{
			sbSql.append("     AND EXISTS (SELECT 1 FROM TM_DEALER_ORG_RELATION TDOR,TM_ORG TMO WHERE TDOR.ORG_ID=TMO.ORG_ID AND TMO.ORG_CODE='"+orgCode+"' AND (TDOR.DEALER_ID=TD.DEALER_ID OR TDOR.DEALER_ID=TD.PARENT_DEALER_D))\n"); 
		}
		if(sJdealerCode!=null&&!"".equals(sJdealerCode))
		{
			sbSql.append("           AND TD.PARENT_DEALER_D = (SELECT DEALER_ID FROM TM_DEALER WHERE DEALER_CODE ='"+sJdealerCode+"')\n");	
		}
		if(companyId!=null&&!"".equals(companyId))
		{
			sbSql.append("           AND TD.COMPANY_ID = "+companyId+"\n"); 
		}
		if(oemCompanyId!=null&&!"".equals(oemCompanyId))
		{
			sbSql.append("           AND TD.OEM_COMPANY_ID = "+oemCompanyId+"\n"); 
		}
		if(dealerClass!=null&&dealerClass!=""){
			sbSql.append("          AND TD.DEALER_CLASS='"+dealerClass+"'\n"); 
		}
		if(province!=null&&province!=""){
			sbSql.append("          AND TD.PROVINCE_ID='"+province+"'\n"); 
		}
		if(pdealerType!=null&&!"".equals(pdealerType))
		{
			sbSql.append("           AND TD.PDEALER_TYPE = "+pdealerType+"\n"); 
		}
		if(serviceStatus!=null&&!"".equals(serviceStatus))
		{
			sbSql.append("           AND TD.SERVICE_STATUS = "+serviceStatus+"\n"); 
		}
		sbSql.append("     AND TD.COMPANY_ID = TC.COMPANY_ID)\n");
		sbSql.append("SELECT   --序号\n");
		sbSql.append("       DISTINCT D.DEALER_CODE, --经销商CODE\n");
		sbSql.append("       D.CDEALER_NAME, --上级经销商全称\n");
		sbSql.append("       D.DEALER_SHORTNAME, --经销商简称\n");
		sbSql.append("       D.DEALER_NAME, --经销商全称\n");
		sbSql.append("       G.REGION_NAME P_NAME, --省份\n");
		sbSql.append("       H.REGION_NAME C_NAME, --城市\n");
		sbSql.append("       I.REGION_NAME, --区县\n");
		sbSql.append("       D.ZIP_CODE, --邮编\n");
		sbSql.append("       D.ADDRESS, --地址\n");
		sbSql.append("       D.PHONE, --联系电话\n");
		sbSql.append("       D.FAX_NO, --传真\n");
		sbSql.append("       D.LINK_MAN, --联系人\n");
		sbSql.append("       D.EMAIL, --电子邮件\n");
		sbSql.append("       D.LEGAL, --法人\n");
		sbSql.append("       D.DUTY_PHONE, --值班电话\n");
		sbSql.append("       D.ZZADDRESS, --注册地址\n");
		sbSql.append("       D.CH_ADDRESS, --昌铃形象地址\n");
		sbSql.append("       D.CH_ADDRESS2, --昌汽形象地址\n");
		sbSql.append("       D.WEBMASTER_NAME, --站长姓名\n");
		sbSql.append("       D.DEALER_LABOUR_TYPE, --是否连锁店\n");
		sbSql.append("       D.DEALER_LEVEL\n");
		sbSql.append("  FROM A_TAB D\n");
		sbSql.append("  LEFT JOIN TM_REGION G\n");
		sbSql.append("    ON D.PROVINCE_ID = G.REGION_CODE\n");
		sbSql.append("  LEFT JOIN TM_REGION H\n");
		sbSql.append("    ON D.CITY_ID = H.REGION_CODE\n");
		sbSql.append("  LEFT JOIN TM_REGION I\n");
		sbSql.append("    ON D.COUNTIES = I.REGION_CODE\n");
		if(!CommonUtils.isNullString(areaId)) {
			sbSql.append("LEFT JOIN TM_DEALER_BUSINESS_AREA TDBA\n");
			sbSql.append("    ON D.DEALER_ID = TDBA.DEALER_ID\n");  
			sbSql.append("    WHERE TDBA.AREA_ID = ").append(areaId).append("\n");
		}
		sbSql.append(" ORDER BY D.DEALER_CODE DESC"); 
		return super.pageQuery(sbSql.toString(), null, super.getFunName()) ;
	}
	public List<Map<String, Object>> dealerInfoDownloadNew(Map<String, String> map) {
		String areaId = (String) map.get("areaId"); 
		String dealerClass = (String) map.get("dealerClass"); 
		String dealerCode = (String) map.get("dealerCode"); 
		String dealerName = (String) map.get("dealerName"); 
		String dealerLevel = (String) map.get("dealerLevel"); 
		String status = (String) map.get("status"); 
		String sJdealerCode = (String) map.get("sJdealerCode"); 
		String orgCode = (String) map.get("orgCode");
		String dealerType = (String) map.get("dealerType"); 
		String companyId = (String) map.get("companyId"); 
		String oemCompanyId = (String) map.get("oemCompanyId"); 
		String province = (String) map.get("province");
		String city = (String) map.get("city"); 
		String pdealerType = (String) map.get("pdealerType"); 
		String serviceStatus = (String) map.get("serviceStatus"); 
		String brand = (String) map.get("brand");
		String imageLevel = (String) map.get("imageLevel"); 
		String imageLevel2 = (String) map.get("imageLevel2"); 
		String isSpecial = (String) map.get("isSpecial");
		
//		String WORK_TYPE = (String) map.get("WORK_TYPE"); 
//		String AUTHORIZATION_TYPE = (String) map.get("AUTHORIZATION_TYPE"); 
//		String regionId = (String) map.get("regionId"); 
		
		StringBuffer sbSql = new StringBuffer() ;
		List<Object> param=new LinkedList<Object> ();
		sbSql.append("WITH UT_TAB AS\n");
		sbSql.append(" (\n");
		sbSql.append("  --销售/售后\n");
		sbSql.append("  SELECT A.COMPANY_ID,\n");
		sbSql.append("          A.DEALER_ID,\n");
		sbSql.append("          A.DEALER_TYPE,\n");
		sbSql.append("          F_GET_TMREGION_NAME(A.PROVINCE_ID) PROVINCE_NAME, --省份\n");
		sbSql.append("          F_GET_TMREGION_NAME(A.CITY_ID) CITY_NAME, --城市\n");
		sbSql.append("          F_GET_TMREGION_NAME(A.COUNTIES) COUNTIES_NAME, --区县\n");
		sbSql.append("          A.ADMIN_LEVEL, --行政级别\n");
		sbSql.append("          D.ROOT_ORG_NAME DQ_ORG_NAME, --大区\n");
		sbSql.append("          A.DEALER_CODE, --服务商代码\n");
		sbSql.append("          A.DEALER_SHORTNAME, --服务商简称\n");
		sbSql.append("          A.DEALER_NAME, --服务商名称\n");
		sbSql.append("          A.ADDRESS, --服务商地址\n");
		sbSql.append("          A.BRAND BRAND_CODE, --品牌CODE，后面用到\n");
		sbSql.append("          A.DEALER_LEVEL, --售后\n");
		sbSql.append("          DECODE(F_GET_STR_COUNT(A.BRAND, 'CL'),\n");
		sbSql.append("                 1,\n");
		sbSql.append("                 F_GET_TCCODE_DESC(A.DEALER_LEVEL),\n");
		sbSql.append("                 '') DEALER_LEVEL_CL, --昌铃\n");
		sbSql.append("          DECODE(F_GET_STR_COUNT(A.BRAND, 'CH'),\n");
		sbSql.append("                 1,\n");
		sbSql.append("                 F_GET_TCCODE_DESC(A.DEALER_LEVEL),\n");
		sbSql.append("                 '') DEALER_LEVEL_GF, --股份\n");
		sbSql.append("          REPLACE(REPLACE(A.BRAND, 'CH', '昌河汽车'), 'CL', '昌河铃木') BRAND, --品牌\n");
		sbSql.append("          B.COMPANY_NAME, --经销商公司\n");
		sbSql.append("          F_GET_TCCODE_DESC(A.IMAGE_LEVEL) IMAGE_LEVEL, --昌铃服务形象等级\n");
		sbSql.append("          F_GET_TCCODE_DESC(A.IMAGE_LEVEL2) IMAGE_LEVEL2, --股份服务形象等级\n");
		sbSql.append("          F_GET_TCCODE_DESC(A.BALANCE_LEVEL) BALANCE_LEVEL, --结算等级\n");
		sbSql.append("          F_GET_TCCODE_DESC(A.INVOICE_LEVEL) INVOICE_LEVEL, --开票等级\n");
		sbSql.append("          F_GET_TCCODE_DESC(A.IS_DQV) IS_DQV, --是否通过DQV\n");
		sbSql.append("          D.ORG_CODE, --上级组织代码\n");
		sbSql.append("          D.ORG_NAME, --上级组织名称\n");
		sbSql.append("          DECODE(F_GET_STR_COUNT(A.BRAND, 'CL'), 1, F.DEALER_NAME, '') P_DEALER_NAME_CL, --昌铃\n");
		sbSql.append("          DECODE(F_GET_STR_COUNT(A.BRAND, 'CH'), 1, F.DEALER_NAME, '') P_DEALER_NAME_GF, --股份\n");
		sbSql.append("          F.DEALER_CODE P_DEALER_CODE, --上级服务商代码\n");
		sbSql.append("          F.DEALER_NAME P_DEALER_NAME, --上级服务商名称\n");
		sbSql.append("          F_GET_TCCODE_DESC(A.SERVICE_STATUS) STATUS, --(信息状态)服务商状态\n");
		sbSql.append("          TO_CHAR(A.SITEDATE, 'YYYY-MM-DD') SITEDATE, --建站日期（销售准入时间）\n");
		sbSql.append("          TO_CHAR(A.DESTROYDATE, 'YYYY-MM-DD') DESTROYDATE, --撤站日期（销售退出时间）\n");
		sbSql.append("          TO_CHAR(A.UPDATE_DATE, 'YYYY-MM-DD') UPDATE_DATE, --信息变更日期\n");
		sbSql.append("          A.DUTY_PHONE, --24小时值班电话\n");
		sbSql.append("          A.FAX_NO, --传真\n");
		sbSql.append("          A.EMAIL, --E-MAIL\n");
		sbSql.append("          A.ZIP_CODE, --邮编\n");
		sbSql.append("          A.ZZADDRESS, --注册地址（营业执照）\n");
		sbSql.append("          A.LEGAL, --法人\n");
		sbSql.append("          A.LEGAL_TEL, --法人电话\n");
		sbSql.append("          A.WEBMASTER_NAME, --站长(销售为总经理名称)\n");
		sbSql.append("          A.WEBMASTER_PHONE, --站长电话(销售为总经理电话)\n");
		sbSql.append("          A.MARKET_NAME, --销售专用，销售经理名称\n");
		sbSql.append("          A.MARKET_TEL, --销售专用，销售经理电话\n");
		sbSql.append("          A.SPY_MAN, --索赔员姓名\n");
		sbSql.append("          A.SPY_PHONE, --索赔员电话\n");
		sbSql.append("          F_GET_TCCODE_DESC(A.MAIN_RESOURCES) MAIN_RESOURCES, --维修资质\n");
		sbSql.append("          A.CH_ADDRESS, --昌铃\n");
		sbSql.append("          A.CH_ADDRESS2, --股份\n");
		sbSql.append("          A.ERP_CODE, --开票名称\n");
		sbSql.append("          A.TAXES_NO, --税号\n");
		sbSql.append("          A.BANK, --开户行\n");
		sbSql.append("          A.INVOICE_ACCOUNT, --开户账号\n");
		sbSql.append("          A.INVOICE_ADD, --开户地址\n");
		sbSql.append("          A.INVOICE_PHONE, --开户电话\n");
		sbSql.append("          A.INVOICE_POST_ADD --发票邮寄地址\n");
		sbSql.append("    FROM TM_DEALER     A, --经销商表\n");
		sbSql.append("          TM_COMPANY    B, --经销商公司表\n");
		sbSql.append("          TM_COMPANY    C, --上级经销商公司表\n");
		sbSql.append("          TM_DEALER     F, --上级经销商表\n");
		sbSql.append("          VW_ORG_DEALER D\n");
		sbSql.append("   WHERE A.COMPANY_ID = B.COMPANY_ID\n");
		sbSql.append("     AND A.OEM_COMPANY_ID = C.COMPANY_ID(+)\n");
		sbSql.append("     AND A.DEALER_ID = D.DEALER_ID(+)\n");
		sbSql.append("     AND A.PARENT_DEALER_D = F.DEALER_ID(+)\n");
		/* add by wangsw 是否特殊客商查询条件*/
		if(isSpecial != null && !"".equals(isSpecial)) {
			sbSql.append("           AND A.IS_SPECIAL = ?\n"); 
			param.add(isSpecial);
		}
		if(dealerCode!=null&&!"".equals(dealerCode))
		{
			sbSql.append("           AND A.DEALER_CODE LIKE ?\n"); 
			param.add("%"+dealerCode+"%");
		}
		if(dealerName!=null&&!"".equals(dealerName))
		{
			sbSql.append("           AND A.DEALER_SHORTNAME LIKE ?\n"); 
			param.add("%"+dealerName+"%");
		}
		if(status!=null&&!"".equals(status))
		{
			sbSql.append("           AND A.STATUS = ?\n"); 
			param.add(status);
		}
		if(dealerLevel!=null&&!"".equals(dealerLevel))
		{
			sbSql.append("           AND A.DEALER_LEVEL = ?\n"); 
			param.add(dealerLevel);
		}
		if(orgCode!=null&&!"".equals(orgCode))
		{
			sbSql.append("     AND EXISTS (SELECT 1 FROM TM_DEALER_ORG_RELATION TDOR,TM_ORG TMO WHERE TDOR.ORG_ID=TMO.ORG_ID AND TMO.ORG_CODE=? AND (TDOR.DEALER_ID=TD.DEALER_ID OR TDOR.DEALER_ID=A.PARENT_DEALER_D))\n"); 
			param.add(orgCode);
		}
		if(sJdealerCode!=null&&!"".equals(sJdealerCode))
		{
			sbSql.append("           AND A.PARENT_DEALER_D = (SELECT DEALER_ID FROM TM_DEALER WHERE DEALER_CODE =?)\n");	
			param.add(sJdealerCode);
		}
		if(companyId!=null&&!"".equals(companyId))
		{
			sbSql.append("           AND A.COMPANY_ID = ?\n"); 
			param.add(companyId);
		}
		if(oemCompanyId!=null&&!"".equals(oemCompanyId))
		{
			sbSql.append("           AND A.OEM_COMPANY_ID = ?\n"); 
			param.add(oemCompanyId);
		}
		if(dealerClass!=null&&dealerClass!=""){
			sbSql.append("          AND TD.DEALER_CLASS=?\n"); 
			param.add(dealerClass);
		}
		if(province!=null&&province!=""){
			sbSql.append("          AND TD.PROVINCE_ID=?\n"); 
			param.add(province);
		}
		if(pdealerType!=null&&!"".equals(pdealerType))
		{
			sbSql.append("           AND A.PDEALER_TYPE = ?\n"); 
			param.add(pdealerType);
		}
		if(serviceStatus!=null&&!"".equals(serviceStatus))
		{
			sbSql.append("           AND A.SERVICE_STATUS = ?\n"); 
			param.add(serviceStatus);
		}
		if(brand!=null&&!"".equals(brand))
		{
			sbSql.append("           AND A.BRAND LIKE ?\n"); 
			param.add("%"+brand+"%");
		}
		if(imageLevel!=null&&!"".equals(imageLevel))
		{
			sbSql.append("           AND A.IMAGE_LEVEL = ?\n"); 
			param.add(imageLevel);
		}
		if(imageLevel2!=null&&!"".equals(imageLevel2))
		{
			sbSql.append("           AND A.IMAGE_LEVEL2 = ?\n"); 
			param.add(imageLevel2);
		}
		
//		if(WORK_TYPE!=null&&!"".equals(WORK_TYPE))
//		{
//			sbSql.append("           AND b.WORK_TYPE = ?\n"); 
//			param.add(WORK_TYPE);
//		}
//		if(imageLevel2!=null&&!"".equals(imageLevel2))
//		{
//			sbSql.append("           AND A.IMAGE_LEVEL2 = ?\n"); 
//			param.add(imageLevel2);
//		}
//		if(imageLevel2!=null&&!"".equals(imageLevel2))
//		{
//			sbSql.append("           AND A.IMAGE_LEVEL2 = ?\n"); 
//			param.add(imageLevel2);
//		}
		sbSql.append("     ),\n");
		sbSql.append("HT_TAB AS\n");
		sbSql.append(" ( --取得合同表的号\n");
		sbSql.append("  SELECT S.DEALER_ID D_ID, MAX(S.CONTRACT_NO) HT_NO\n");
		sbSql.append("    FROM TT_SALES_CONTRACT S\n");
		sbSql.append("   WHERE S.STATUS = 10011001\n");
		sbSql.append("     AND S.CONTRACT_YEAR = TO_CHAR(SYSDATE, 'YYYY')\n");
		sbSql.append("   GROUP BY S.DEALER_ID, S.CONTRACT_NO),\n");
		sbSql.append("HT_HB_TAB AS\n");
		sbSql.append(" ( --合同合并\n");
		sbSql.append("  SELECT A.COMPANY_ID,\n");
		sbSql.append("          A.DEALER_ID,\n");
		sbSql.append("          A.DEALER_TYPE,\n");
		sbSql.append("          A.PROVINCE_NAME, --省份\n");
		sbSql.append("          A.CITY_NAME, --城市\n");
		sbSql.append("          A.COUNTIES_NAME, --区县\n");
		sbSql.append("          A.DEALER_CODE, --经销商代码\n");
		sbSql.append("          A.BRAND_CODE, --品牌CODE，后面用到\n");
		sbSql.append("          B.HT_NO, --合同号\n");
		sbSql.append("          --A.DEALER_SHORTNAME, --经销商商简称\n");
		sbSql.append("          A.DEALER_NAME, --经销商名称\n");
		sbSql.append("          A.IMAGE_LEVEL, --昌铃等级\n");
		sbSql.append("          A.IMAGE_LEVEL2, --股份等级\n");
		sbSql.append("          A.STATUS, --(信息状态)服务商状态\n");
		sbSql.append("          A.DEALER_LEVEL_CL, --经销商等级(昌铃)\n");
		sbSql.append("          A.DEALER_LEVEL_GF, --经销商等级(股份)\n");
		sbSql.append("          A.P_DEALER_NAME_CL, --上级经销商名称(昌铃)\n");
		sbSql.append("          A.P_DEALER_NAME_GF, --上级经销商名称(股份)\n");
		sbSql.append("          A.SITEDATE, --准入日期\n");
		sbSql.append("          A.DESTROYDATE, --退出日期\n");
		sbSql.append("          --首次验收时间\n");
		sbSql.append("          A.LEGAL, --法人\n");
		sbSql.append("          A.LEGAL_TEL, --法人电话\n");
		sbSql.append("          A.WEBMASTER_NAME, --总经理名称\n");
		sbSql.append("          A.WEBMASTER_PHONE, --总经理电话\n");
		sbSql.append("          A.MARKET_NAME, --销售经理名称\n");
		sbSql.append("          A.MARKET_TEL, --销售经理电话\n");
		sbSql.append("          A.FAX_NO, --传真\n");
		sbSql.append("          A.EMAIL, --E-MAIL\n");
		sbSql.append("          A.CH_ADDRESS, --昌铃\n");
		sbSql.append("          A.CH_ADDRESS2, --股份\n");
		sbSql.append("          A.ZIP_CODE, --邮编\n");
		sbSql.append("          A.DUTY_PHONE, --24小时值班电话\n");
		sbSql.append("          A.ERP_CODE, --开票名称\n");
		sbSql.append("          A.TAXES_NO, --税号\n");
		sbSql.append("          A.BANK, --开户行\n");
		sbSql.append("          A.INVOICE_ACCOUNT, --开户账号\n");
		sbSql.append("          A.INVOICE_ADD, --开户地址\n");
		sbSql.append("          A.INVOICE_PHONE, --开户电话\n");
		sbSql.append("          A.INVOICE_POST_ADD --发票邮寄地址\n");
		sbSql.append("  --A.ADDRESS, --经销商地址\n");
		sbSql.append("  --A.BRAND, --品牌\n");
		sbSql.append("  --A.COMPANY_NAME, --经销商公司\n");
		sbSql.append("  --A.UPDATE_DATE, --信息变更日期\n");
		sbSql.append("  -- A.ZZADDRESS, --注册地址（营业执照）\n");
		sbSql.append("    FROM UT_TAB A, HT_TAB B\n");
		sbSql.append("   WHERE A.DEALER_ID = B.D_ID(+)),\n");
		sbSql.append("XS_TABA AS\n");
		sbSql.append(" ( --销售A\n");
		sbSql.append("  SELECT A.COMPANY_ID,\n");
		sbSql.append("          A.DEALER_ID,\n");
		sbSql.append("          A.DEALER_TYPE,\n");
		sbSql.append("          A.PROVINCE_NAME, --省份\n");
		sbSql.append("          A.CITY_NAME, --城市\n");
		sbSql.append("          A.COUNTIES_NAME, --区县\n");
		sbSql.append("          A.DEALER_CODE, --经销商代码\n");
		sbSql.append("          A.BRAND_CODE, --品牌CODE，后面用到\n");
		sbSql.append("          A.HT_NO, --合同号\n");
		sbSql.append("          --A.DEALER_SHORTNAME, --经销商商简称\n");
		sbSql.append("          A.DEALER_NAME, --经销商名称\n");
		sbSql.append("          A.IMAGE_LEVEL, --昌铃等级\n");
		sbSql.append("          A.IMAGE_LEVEL2, --股份等级\n");
		sbSql.append("          A.STATUS, --(信息状态)服务商状态\n");
		sbSql.append("          A.DEALER_LEVEL_CL, --经销商等级(昌铃)\n");
		sbSql.append("          A.DEALER_LEVEL_GF, --经销商等级(股份)\n");
		sbSql.append("          A.P_DEALER_NAME_CL, --上级经销商名称(昌铃)\n");
		sbSql.append("          A.P_DEALER_NAME_GF, --上级经销商名称(股份)\n");
		sbSql.append("          A.SITEDATE, --准入日期\n");
		sbSql.append("          A.DESTROYDATE, --退出日期\n");
		sbSql.append("          --首次验收时间\n");
		sbSql.append("          A.LEGAL, --法人\n");
		sbSql.append("          A.LEGAL_TEL, --法人电话\n");
		sbSql.append("          A.WEBMASTER_NAME, --总经理名称\n");
		sbSql.append("          A.WEBMASTER_PHONE, --总经理电话\n");
		sbSql.append("          A.MARKET_NAME, --销售经理名称\n");
		sbSql.append("          A.MARKET_TEL, --销售经理电话\n");
		sbSql.append("          A.FAX_NO, --传真\n");
		sbSql.append("          A.EMAIL, --E-MAIL\n");
		sbSql.append("          A.CH_ADDRESS, --昌铃\n");
		sbSql.append("          A.CH_ADDRESS2, --股份\n");
		sbSql.append("          A.ZIP_CODE, --邮编\n");
		sbSql.append("          A.DUTY_PHONE, --24小时值班电话\n");
		sbSql.append("          A.ERP_CODE, --开票名称\n");
		sbSql.append("          A.TAXES_NO, --税号\n");
		sbSql.append("          A.BANK, --开户行\n");
		sbSql.append("          A.INVOICE_ACCOUNT, --开户账号\n");
		sbSql.append("          A.INVOICE_ADD, --开户地址\n");
		sbSql.append("          A.INVOICE_PHONE, --开户电话\n");
		sbSql.append("          A.INVOICE_POST_ADD --发票邮寄地址\n");
		sbSql.append("    FROM HT_HB_TAB A\n");
		sbSql.append("   WHERE A.DEALER_TYPE = 10771001\n");
		sbSql.append("     AND A.DEALER_CODE LIKE 'A%'),\n");
		sbSql.append("XS_TABB AS\n");
		sbSql.append(" ( --销售B\n");
		sbSql.append("  SELECT A.COMPANY_ID       COMPANY_ID1,\n");
		sbSql.append("          A.COUNTIES_NAME    COUNTIES_NAME1,\n");
		sbSql.append("          '/'||A.DEALER_CODE    DEALER_CODE1,\n");
		sbSql.append("          A.DEALER_LEVEL_CL  DEALER_LEVEL_CL1, --经销商等级(昌铃)\n");
		sbSql.append("          A.DEALER_LEVEL_GF  DEALER_LEVEL_GF1, --经销商等级(股份)\n");
		sbSql.append("          A.P_DEALER_NAME_CL P_DEALER_NAME_CL1, --上级经销商名称(昌铃)\n");
		sbSql.append("          A.P_DEALER_NAME_GF P_DEALER_NAME_GF1, --上级经销商名称(股份)\n");
		sbSql.append("          A.BRAND_CODE       BRAND_CODE1 --品牌CODE，后面用到\n");
		sbSql.append("    FROM HT_HB_TAB A\n");
		sbSql.append("   WHERE A.DEALER_TYPE = 10771001\n");
		sbSql.append("     AND A.DEALER_CODE LIKE 'B%'),\n");
		sbSql.append("SH_TAB AS\n");
		sbSql.append(" ( --售后服务\n");
		sbSql.append("  SELECT A.COMPANY_ID       COMPANY_IDS,\n");
		sbSql.append("          A.DEALER_ID        DEALER_IDS,\n");
		sbSql.append("          A.DEALER_TYPE      DEALER_TYPES,\n");
		sbSql.append("          A.PROVINCE_NAME    PROVINCE_NAMES, --省份\n");
		sbSql.append("          A.CITY_NAME        CITY_NAMES, --城市\n");
		sbSql.append("          A.COUNTIES_NAME    COUNTIES_NAMES, --区县\n");
		sbSql.append("          A.ADMIN_LEVEL      ADMIN_LEVELS, --行政级别\n");
		sbSql.append("          A.DQ_ORG_NAME      DQ_ORG_NAMES, --大区\n");
		sbSql.append("          A.DEALER_CODE      DEALER_CODES, --服务商代码\n");
		sbSql.append("          A.DEALER_SHORTNAME DEALER_SHORTNAMES, --服务商简称\n");
		sbSql.append("          A.DEALER_NAME      DEALER_NAMES, --服务商名称\n");
		sbSql.append("          A.ADDRESS          ADDRESSS, --服务商地址\n");
		sbSql.append("          A.DEALER_LEVEL     DEALER_LEVELS, --服务商等级\n");
		sbSql.append("          A.BRAND            BRANDS, --品牌\n");
		sbSql.append("          A.COMPANY_NAME     COMPANY_NAMES, --经销商公司\n");
		sbSql.append("          A.IMAGE_LEVEL      IMAGE_LEVELS, --昌铃服务形象等级\n");
		sbSql.append("          A.IMAGE_LEVEL2     IMAGE_LEVELS2, --股份服务形象等级\n");
		sbSql.append("          A.BALANCE_LEVEL    BALANCE_LEVELS, --结算等级\n");
		sbSql.append("          A.INVOICE_LEVEL    INVOICE_LEVELS, --开票等级\n");
		sbSql.append("          A.IS_DQV           IS_DQVS, --是否通过DQV\n");
		sbSql.append("          A.ORG_CODE         ORG_CODES, --上级组织代码\n");
		sbSql.append("          A.ORG_NAME         ORG_NAMES, --上级组织名称\n");
		sbSql.append("          A.P_DEALER_CODE    P_DEALER_CODES, --上级服务商代码\n");
		sbSql.append("          A.P_DEALER_NAME    P_DEALER_NAMES, --上级服务商名称\n");
		sbSql.append("          A.STATUS           STATUSS, --(信息状态)服务商状态\n");
		sbSql.append("          A.SITEDATE         SITEDATES, --建站日期\n");
		sbSql.append("          A.DESTROYDATE      DESTROYDATES, --撤站日期\n");
		sbSql.append("          A.UPDATE_DATE      UPDATE_DATES, --信息变更日期\n");
		sbSql.append("          A.DUTY_PHONE       DUTY_PHONES, --24小时值班电话\n");
		sbSql.append("          A.FAX_NO           FAX_NOS, --传真\n");
		sbSql.append("          A.EMAIL            EMAILS, --E-MAIL\n");
		sbSql.append("          A.ZIP_CODE         ZIP_CODES, --邮编\n");
		sbSql.append("          A.ZZADDRESS        ZZADDRESSS, --注册地址（营业执照）\n");
		sbSql.append("          A.LEGAL            LEGALS, --法人\n");
		sbSql.append("          A.LEGAL_TEL        LEGAL_TELS, --法人电话\n");
		sbSql.append("          A.WEBMASTER_NAME   WEBMASTER_NAMES, --站长\n");
		sbSql.append("          A.WEBMASTER_PHONE  WEBMASTER_PHONES, --站长电话\n");
		sbSql.append("          A.SPY_MAN          SPY_MANS, --索赔员姓名\n");
		sbSql.append("          A.SPY_PHONE        SPY_PHONES, --索赔员电话\n");
		sbSql.append("          A.MAIN_RESOURCES   MAIN_RESOURCESS, --维修资质\n");
		sbSql.append("          A.ERP_CODE         ERP_CODES, --开票名称\n");
		sbSql.append("          A.TAXES_NO         TAXES_NOS, --税号\n");
		sbSql.append("          A.BANK             BANKS, --开户行\n");
		sbSql.append("          A.INVOICE_ACCOUNT  INVOICE_ACCOUNTS, --开户账号\n");
		sbSql.append("          A.INVOICE_ADD      INVOICE_ADDS, --开户地址\n");
		sbSql.append("          A.INVOICE_PHONE    INVOICE_PHONES, --开户电话\n");
		sbSql.append("          A.INVOICE_POST_ADD INVOICE_POST_ADDS --发票邮寄地址\n");
		sbSql.append("    FROM UT_TAB A\n");
		sbSql.append("   WHERE A.DEALER_TYPE = 10771002),\n");
		sbSql.append("\n");
		sbSql.append("XS_HB_TAB AS\n");
		sbSql.append(" ( --销售合并[关于 数据库销售A,B 账号问题，2种情况，昌铃/股份经销商等级等级不一样数据分为2条数据存储，第二种是上级经销商不一样也是2条]\n");
		sbSql.append("  SELECT\n");
		sbSql.append("\n");
		sbSql.append("   A.COMPANY_ID,\n");
		sbSql.append("    A.DEALER_ID,\n");
		sbSql.append("    A.DEALER_TYPE,\n");
		sbSql.append("    A.PROVINCE_NAME, --省份\n");
		sbSql.append("    A.CITY_NAME, --城市\n");
		sbSql.append("    A.COUNTIES_NAME, --区县\n");
		sbSql.append("    A.DEALER_CODE, --A类经销商代码\n");
		sbSql.append("    B.DEALER_CODE1, --b类经销商代码\n");
		sbSql.append("    A.BRAND_CODE, --品牌CODE，后面用到\n");
		sbSql.append("    B.BRAND_CODE1, --品牌CODE，后面用到\n");
		sbSql.append("    A.HT_NO, --合同号\n");
		sbSql.append("    --A.DEALER_SHORTNAME, --经销商商简称\n");
		sbSql.append("    A.DEALER_NAME, --经销商名称\n");
		sbSql.append("    A.IMAGE_LEVEL, --昌铃等级\n");
		sbSql.append("    A.IMAGE_LEVEL2, --股份等级\n");
		sbSql.append("    A.STATUS, --(信息状态)服务商状态\n");
		sbSql.append("    DECODE(F_GET_STR_COUNT(A.BRAND_CODE, 'CL'),\n");
		sbSql.append("           1,\n");
		sbSql.append("           A.DEALER_LEVEL_CL,\n");
		sbSql.append("           B.DEALER_LEVEL_CL1) DEALER_LEVEL_CL, --经销商等级(昌铃)\n");
		sbSql.append("    DECODE(F_GET_STR_COUNT(A.BRAND_CODE, 'CH'),\n");
		sbSql.append("           1,\n");
		sbSql.append("           A.DEALER_LEVEL_GF,\n");
		sbSql.append("           B.DEALER_LEVEL_GF1) DEALER_LEVEL_GF, --经销商等级(股份)\n");
		sbSql.append("    DECODE(F_GET_STR_COUNT(A.BRAND_CODE, 'CL'),\n");
		sbSql.append("           1,\n");
		sbSql.append("           A.P_DEALER_NAME_CL,\n");
		sbSql.append("           B.P_DEALER_NAME_CL1) P_DEALER_NAME_CL, --上级经销商名称(昌铃)\n");
		sbSql.append("    DECODE(F_GET_STR_COUNT(A.BRAND_CODE, 'CH'),\n");
		sbSql.append("           1,\n");
		sbSql.append("           A.P_DEALER_NAME_GF,\n");
		sbSql.append("           B.P_DEALER_NAME_GF1) P_DEALER_NAME_GF, --上级经销商名称(股份)\n");
		sbSql.append("    A.SITEDATE, --准入日期\n");
		sbSql.append("    A.DESTROYDATE, --退出日期\n");
		sbSql.append("    --首次验收时间\n");
		sbSql.append("    A.LEGAL, --法人\n");
		sbSql.append("    A.LEGAL_TEL, --法人电话\n");
		sbSql.append("    A.WEBMASTER_NAME, --总经理名称\n");
		sbSql.append("    A.WEBMASTER_PHONE, --总经理电话\n");
		sbSql.append("    A.MARKET_NAME, --销售经理名称\n");
		sbSql.append("    A.MARKET_TEL, --销售经理电话\n");
		sbSql.append("    A.FAX_NO, --传真\n");
		sbSql.append("    A.EMAIL, --E-MAIL\n");
		sbSql.append("    A.CH_ADDRESS, --昌铃\n");
		sbSql.append("    A.CH_ADDRESS2, --股份\n");
		sbSql.append("    A.ZIP_CODE, --邮编\n");
		sbSql.append("    A.DUTY_PHONE, --24小时值班电话\n");
		sbSql.append("    A.ERP_CODE, --开票名称\n");
		sbSql.append("    A.TAXES_NO, --税号\n");
		sbSql.append("    A.BANK, --开户行\n");
		sbSql.append("    A.INVOICE_ACCOUNT, --开户账号\n");
		sbSql.append("    A.INVOICE_ADD, --开户地址\n");
		sbSql.append("    A.INVOICE_PHONE, --开户电话\n");
		sbSql.append("    A.INVOICE_POST_ADD --发票邮寄地址\n");
		sbSql.append("\n");
		sbSql.append("    FROM XS_TABA A, XS_TABB B\n");
		sbSql.append("   WHERE A.COMPANY_ID = B.COMPANY_ID1(+))\n");
		sbSql.append("\n");
		if(dealerType!=null&&!"".equals(dealerType))
		{
			if(dealerType.equals(String.valueOf(Constant.DEALER_TYPE_DVS))){//销售
				sbSql.append(xsSQL());
			}else if(dealerType.equals(String.valueOf(Constant.DEALER_TYPE_DWR))){//售后
				sbSql.append(shSQL());
			}else{
				sbSql.append(xsSQL());
				sbSql.append(" union\n");
				sbSql.append("\n");
				sbSql.append(shSQL());
			}
			 
		}else{
			sbSql.append(xsSQL());
			sbSql.append(" union\n");
			sbSql.append("\n");
			sbSql.append(shSQL());	
		}
		
		 

		return super.pageQuery(sbSql.toString(), param, super.getFunName()) ;
	}
	/***
	 * 销售SQL
	 * @return
	 */
	public StringBuffer xsSQL(){
		StringBuffer sbSql = new StringBuffer() ;
		sbSql.append("SELECT A.PROVINCE_NAME,\n");
		sbSql.append("       A.CITY_NAME,\n");
		sbSql.append("       A.COUNTIES_NAME,\n");
		sbSql.append("       A.COUNTIES_NAME COUNTIES_NAME1,\n");
		sbSql.append("       A.DEALER_CODE || A.DEALER_CODE1 DEALER_CODE, --经销商代码\n");
		sbSql.append("       A.HT_NO, --合同号\n");
		sbSql.append("       A.DEALER_NAME, --经销商名称\n");
		sbSql.append("       A.IMAGE_LEVEL, --昌铃等级\n");
		sbSql.append("       A.IMAGE_LEVEL2, --股份等级\n");
		sbSql.append("       A.STATUS, --(信息状态)服务商状态\n");
		sbSql.append("       A.BRAND_CODE,\n");
		sbSql.append("       A.BRAND_CODE1,\n");
		sbSql.append("       A.DEALER_LEVEL_CL, --经销商等级\n");
		sbSql.append("       A.DEALER_LEVEL_GF, --经销商等级\n");
		sbSql.append("       A.P_DEALER_NAME_CL, --上级经销商名称\n");
		sbSql.append("       A.P_DEALER_NAME_GF, --上级经销商名称\n");
		sbSql.append("       A.SITEDATE, --准入日期\n");
		sbSql.append("       A.DESTROYDATE, --退出日期\n");
		sbSql.append("       --首次验收时间\n");
		sbSql.append("       A.LEGAL, --法人\n");
		sbSql.append("       A.LEGAL_TEL, --法人电话\n");
		sbSql.append("       A.WEBMASTER_NAME, --总经理名称\n");
		sbSql.append("       A.WEBMASTER_PHONE, --总经理电话\n");
		sbSql.append("       A.MARKET_NAME, --销售经理名称\n");
		sbSql.append("       A.MARKET_TEL, --销售经理电话\n");
		sbSql.append("       A.FAX_NO, --传真\n");
		sbSql.append("       A.EMAIL, --E-MAIL\n");
		sbSql.append("       A.CH_ADDRESS, --昌铃\n");
		sbSql.append("       A.CH_ADDRESS2, --股份\n");
		sbSql.append("       A.ZIP_CODE, --邮编\n");
		sbSql.append("       A.DUTY_PHONE, --24小时值班电话\n");
		sbSql.append("       A.ERP_CODE, --开票名称\n");
		sbSql.append("       A.TAXES_NO, --税号\n");
		sbSql.append("       A.BANK, --开户行\n");
		sbSql.append("       A.INVOICE_ACCOUNT, --开户账号\n");
		sbSql.append("       A.INVOICE_ADD, --开户地址\n");
		sbSql.append("       A.INVOICE_PHONE, --开户电话\n");
		sbSql.append("       A.INVOICE_POST_ADD, --发票邮寄地址\n");
		sbSql.append("       --售后\n");
		sbSql.append("       B.PROVINCE_NAMES, --省份\n");
		sbSql.append("       B.CITY_NAMES, --城市\n");
		sbSql.append("       B.COUNTIES_NAMES, --区县\n");
		sbSql.append("       B.ADMIN_LEVELS, --行政级别\n");
		sbSql.append("       B.DQ_ORG_NAMES, --大区\n");
		sbSql.append("       B.DEALER_CODES, --服务商代码\n");
		sbSql.append("       B.DEALER_SHORTNAMES, --服务商简称\n");
		sbSql.append("       B.DEALER_NAMES, --服务商名称\n");
		sbSql.append("       B.ADDRESSS, --服务商地址\n");
		sbSql.append("       B.DEALER_LEVELS, --服务商等级\n");
		sbSql.append("       B.BRANDS, --品牌\n");
		sbSql.append("       B.COMPANY_NAMES, --经销商公司\n");
		sbSql.append("       B.IMAGE_LEVELS, --昌铃服务形象等级\n");
		sbSql.append("       B.IMAGE_LEVELS2, --股份服务形象等级\n");
		sbSql.append("       B.BALANCE_LEVELS, --结算等级\n");
		sbSql.append("       B.INVOICE_LEVELS, --开票等级\n");
		sbSql.append("       B.IS_DQVS, --是否通过DQV\n");
		sbSql.append("       B.ORG_CODES, --上级组织代码\n");
		sbSql.append("       B.ORG_NAMES, --上级组织名称\n");
		sbSql.append("       B.P_DEALER_CODES, --上级服务商代码\n");
		sbSql.append("       B.P_DEALER_NAMES, --上级服务商名称\n");
		sbSql.append("       B.STATUSS, --(信息状态)服务商状态\n");
		sbSql.append("       B.SITEDATES, --建站日期\n");
		sbSql.append("       B.DESTROYDATES, --撤站日期\n");
		sbSql.append("       B.UPDATE_DATES, --信息变更日期\n");
		sbSql.append("       B.DUTY_PHONES, --24小时值班电话\n");
		sbSql.append("       B.FAX_NOS, --传真\n");
		sbSql.append("       B.EMAILS, --E-MAIL\n");
		sbSql.append("       B.ZIP_CODES, --邮编\n");
		sbSql.append("       B.ZZADDRESSS, --注册地址（营业执照）\n");
		sbSql.append("       B.LEGALS, --法人\n");
		sbSql.append("       B.LEGAL_TELS, --法人电话\n");
		sbSql.append("       B.WEBMASTER_NAMES, --站长\n");
		sbSql.append("       B.WEBMASTER_PHONES, --站长电话\n");
		sbSql.append("       B.SPY_MANS, --索赔员姓名\n");
		sbSql.append("       B.SPY_PHONES, --索赔员电话\n");
		sbSql.append("       B.MAIN_RESOURCESS, --维修资质\n");
		sbSql.append("       B.ERP_CODES, --开票名称\n");
		sbSql.append("       B.TAXES_NOS, --税号\n");
		sbSql.append("       B.BANKS, --开户行\n");
		sbSql.append("       B.INVOICE_ACCOUNTS, --开户账号\n");
		sbSql.append("       B.INVOICE_ADDS, --开户地址\n");
		sbSql.append("       B.INVOICE_PHONES, --开户电话\n");
		sbSql.append("       B.INVOICE_POST_ADDS --发票邮寄地址\n");
		sbSql.append("  FROM XS_HB_TAB A, SH_TAB B\n");
		sbSql.append(" WHERE A.COMPANY_ID = B.COMPANY_IDS(+)\n");
		return sbSql;
	}
	/**
	 * 售后SQL
	 * @return
	 */
	public StringBuffer shSQL(){
		StringBuffer sbSql = new StringBuffer() ;
		sbSql.append(" SELECT A.PROVINCE_NAME,\n");
		sbSql.append("       A.CITY_NAME,\n");
		sbSql.append("       A.COUNTIES_NAME,\n");
		sbSql.append("       A.COUNTIES_NAME COUNTIES_NAME1,\n");
		sbSql.append("       A.DEALER_CODE || A.DEALER_CODE1 DEALER_CODE, --经销商代码\n");
		sbSql.append("       A.HT_NO, --合同号\n");
		sbSql.append("       A.DEALER_NAME, --经销商名称\n");
		sbSql.append("       A.IMAGE_LEVEL, --昌铃等级\n");
		sbSql.append("       A.IMAGE_LEVEL2, --股份等级\n");
		sbSql.append("       A.STATUS, --(信息状态)服务商状态\n");
		sbSql.append("       A.BRAND_CODE,\n");
		sbSql.append("       A.BRAND_CODE1,\n");
		sbSql.append("       A.DEALER_LEVEL_CL, --经销商等级\n");
		sbSql.append("       A.DEALER_LEVEL_GF, --经销商等级\n");
		sbSql.append("       A.P_DEALER_NAME_CL, --上级经销商名称\n");
		sbSql.append("       A.P_DEALER_NAME_GF, --上级经销商名称\n");
		sbSql.append("       A.SITEDATE, --准入日期\n");
		sbSql.append("       A.DESTROYDATE, --退出日期\n");
		sbSql.append("       --首次验收时间\n");
		sbSql.append("       A.LEGAL, --法人\n");
		sbSql.append("       A.LEGAL_TEL, --法人电话\n");
		sbSql.append("       A.WEBMASTER_NAME, --总经理名称\n");
		sbSql.append("       A.WEBMASTER_PHONE, --总经理电话\n");
		sbSql.append("       A.MARKET_NAME, --销售经理名称\n");
		sbSql.append("       A.MARKET_TEL, --销售经理电话\n");
		sbSql.append("       A.FAX_NO, --传真\n");
		sbSql.append("       A.EMAIL, --E-MAIL\n");
		sbSql.append("       A.CH_ADDRESS, --昌铃\n");
		sbSql.append("       A.CH_ADDRESS2, --股份\n");
		sbSql.append("       A.ZIP_CODE, --邮编\n");
		sbSql.append("       A.DUTY_PHONE, --24小时值班电话\n");
		sbSql.append("       A.ERP_CODE, --开票名称\n");
		sbSql.append("       A.TAXES_NO, --税号\n");
		sbSql.append("       A.BANK, --开户行\n");
		sbSql.append("       A.INVOICE_ACCOUNT, --开户账号\n");
		sbSql.append("       A.INVOICE_ADD, --开户地址\n");
		sbSql.append("       A.INVOICE_PHONE, --开户电话\n");
		sbSql.append("       A.INVOICE_POST_ADD, --发票邮寄地址\n");
		sbSql.append("       --售后\n");
		sbSql.append("       B.PROVINCE_NAMES, --省份\n");
		sbSql.append("       B.CITY_NAMES, --城市\n");
		sbSql.append("       B.COUNTIES_NAMES, --区县\n");
		sbSql.append("       B.ADMIN_LEVELS, --行政级别\n");
		sbSql.append("       B.DQ_ORG_NAMES, --大区\n");
		sbSql.append("       B.DEALER_CODES, --服务商代码\n");
		sbSql.append("       B.DEALER_SHORTNAMES, --服务商简称\n");
		sbSql.append("       B.DEALER_NAMES, --服务商名称\n");
		sbSql.append("       B.ADDRESSS, --服务商地址\n");
		sbSql.append("       B.DEALER_LEVELS, --服务商等级\n");
		sbSql.append("       B.BRANDS, --品牌\n");
		sbSql.append("       B.COMPANY_NAMES, --经销商公司\n");
		sbSql.append("       B.IMAGE_LEVELS, --昌铃服务形象等级\n");
		sbSql.append("       B.IMAGE_LEVELS2, --股份服务形象等级\n");
		sbSql.append("       B.BALANCE_LEVELS, --结算等级\n");
		sbSql.append("       B.INVOICE_LEVELS, --开票等级\n");
		sbSql.append("       B.IS_DQVS, --是否通过DQV\n");
		sbSql.append("       B.ORG_CODES, --上级组织代码\n");
		sbSql.append("       B.ORG_NAMES, --上级组织名称\n");
		sbSql.append("       B.P_DEALER_CODES, --上级服务商代码\n");
		sbSql.append("       B.P_DEALER_NAMES, --上级服务商名称\n");
		sbSql.append("       B.STATUSS, --(信息状态)服务商状态\n");
		sbSql.append("       B.SITEDATES, --建站日期\n");
		sbSql.append("       B.DESTROYDATES, --撤站日期\n");
		sbSql.append("       B.UPDATE_DATES, --信息变更日期\n");
		sbSql.append("       B.DUTY_PHONES, --24小时值班电话\n");
		sbSql.append("       B.FAX_NOS, --传真\n");
		sbSql.append("       B.EMAILS, --E-MAIL\n");
		sbSql.append("       B.ZIP_CODES, --邮编\n");
		sbSql.append("       B.ZZADDRESSS, --注册地址（营业执照）\n");
		sbSql.append("       B.LEGALS, --法人\n");
		sbSql.append("       B.LEGAL_TELS, --法人电话\n");
		sbSql.append("       B.WEBMASTER_NAMES, --站长\n");
		sbSql.append("       B.WEBMASTER_PHONES, --站长电话\n");
		sbSql.append("       B.SPY_MANS, --索赔员姓名\n");
		sbSql.append("       B.SPY_PHONES, --索赔员电话\n");
		sbSql.append("       B.MAIN_RESOURCESS, --维修资质\n");
		sbSql.append("       B.ERP_CODES, --开票名称\n");
		sbSql.append("       B.TAXES_NOS, --税号\n");
		sbSql.append("       B.BANKS, --开户行\n");
		sbSql.append("       B.INVOICE_ACCOUNTS, --开户账号\n");
		sbSql.append("       B.INVOICE_ADDS, --开户地址\n");
		sbSql.append("       B.INVOICE_PHONES, --开户电话\n");
		sbSql.append("       B.INVOICE_POST_ADDS --发票邮寄地址\n");
		sbSql.append("  FROM XS_HB_TAB A, SH_TAB B\n");
		sbSql.append(" WHERE A.COMPANY_ID(+) = B.COMPANY_IDS");
		return sbSql;
	}
	/**
	 * 新增经销商时查询经销商的公司的组织Id
	 * 
	 * @param companyId 公司Id
	 * @return
	 * @throws Exception
	 */
	public  List<TmOrgPO> getOrgInfo(Long companyId)throws Exception {

		String query = " SELECT TMO.ORG_ID FROM TM_ORG TMO WHERE TMO.COMPANY_ID = "+companyId;

		logger.debug("SQL+++++++++++++++++++++++: " + query);
		return factory.select(query, null, new DAOCallback<TmOrgPO>() {
			public TmOrgPO wrapper(ResultSet rs, int idx) {
				TmOrgPO bean = new TmOrgPO();
				try {
					bean.setOrgId(rs.getLong("ORG_ID"));
				} catch (SQLException e) {
					e.printStackTrace();
				}
				return bean;
			}
		});
	}
	/**
	 * 根据经销商Id查询经销商详情
	 * @param dealerId
	 * @return
	 */
	public Map<String, Object> getDealerInfobyId(String dealerId){
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT D.*,\n");
		sql.append("       TD.DEALER_ID AS SJDEALERID,\n");  
		sql.append("       TD.DEALER_CODE AS SJDEALERCODE,\n");  
		sql.append("       DECODE(D.DEALER_LEVEL,\n");  
		sql.append("              10851001,\n");  
		sql.append("              (SELECT TMO.ORG_ID\n");  
		sql.append("                 FROM TM_ORG TMO, TM_DEALER_ORG_RELATION TDOR\n");  
		sql.append("                WHERE TMO.ORG_ID = TDOR.ORG_ID\n");  
		sql.append("                  AND TDOR.DEALER_ID = D.DEALER_ID),\n");  
		sql.append("              '') AS ORG_ID,\n");  
		sql.append("       DECODE(D.DEALER_LEVEL,\n");  
		sql.append("              10851001,\n");  
		sql.append("              (SELECT TMO.ORG_CODE\n");  
		sql.append("                 FROM TM_ORG TMO, TM_DEALER_ORG_RELATION TDOR\n");  
		sql.append("                WHERE TMO.ORG_ID = TDOR.ORG_ID\n");  
		sql.append("                  AND TDOR.DEALER_ID = D.DEALER_ID),\n");  
		sql.append("              '') AS ORG_CODE\n");  
		sql.append("  FROM (SELECT TD.DEALER_ID,\n");  
		sql.append("               TD.COMPANY_ID,\n");  
		sql.append("               TD.DEALER_TYPE,\n");  
		sql.append("               TD.DEALER_CODE,\n");  
		sql.append("               TD.DEALER_NAME,\n");  
		sql.append("               TD.DEALER_CLASS,\n"); 
		sql.append("               TD.STATUS,\n");  
		sql.append("               TD.DEALER_LEVEL,\n");  
		sql.append("               TD.PARENT_DEALER_D,\n");  
		sql.append("               TD.DEALER_ORG_ID,\n");  
		sql.append("               TD.PROVINCE_ID,\n");  
		sql.append("               TD.CITY_ID,\n");  
		sql.append("               TD.ZIP_CODE,\n");  
		sql.append("               TD.ADDRESS,\n");  
		sql.append("               TD.PHONE,\n");  
		sql.append("               TD.FAX_NO,\n");  
		sql.append("               TD.LINK_MAN,\n");  
		sql.append("               TD.EMAIL,\n");  
		sql.append("    		   TD.SITEDATE,\n" );
		sql.append("      		   TD.DESTROYDATE,\n" );
		sql.append("               TD.SPY_MAN,\n" );
		sql.append("               TD.SPY_PHONE,\n" );
		sql.append("               TD.WEBMASTER_NAME,\n" );
		sql.append("               TD.BRAND,\n" );
		sql.append("               TD.ZZADDRESS,");
		sql.append("               TD.REMARK,\n");  
		sql.append("               TD.DEALER_SHORTNAME,\n");  
		sql.append("               TD.OEM_COMPANY_ID,\n");  
		sql.append("               TD.PRICE_ID,\n");   
		sql.append("               TC.COMPANY_SHORTNAME,\n");  
		sql.append("               TD.ERP_CODE,\n");  
		sql.append("               TD.SERVICE_STATUS,\n");  
		sql.append("               TD.TAXES_NO,td.dealer_labour_type,\n");  
		sql.append("               TD.IS_DQV,\n"); 
		sql.append("               TD.BALANCE_LEVEL,\n"); 
		sql.append("               TD.IS_SPECIAL,\n"); 
		sql.append("               TD.LEGAL_TEL,\n"); 
		sql.append("               TD.BILL_ADDRESS,\n"); //2017-08-16新增 开票地址
		sql.append("               TD.MARKET_NAME,\n"); 
		sql.append("               TD.MARKET_TEL,\n"); 
		sql.append("               TO_CHAR(TD.CREATE_DATE,'YYYYMM') CREATE_DATE,\n"); 
		sql.append("               TD.INVOICE_LEVEL,TD.COUNTIES,TD.TOWNSHIP,TD.LEGAL,TD.WEBMASTER_PHONE,TD.DUTY_PHONE,TD.BANK,TD.MAIN_RESOURCES,TD.ADMIN_LEVEL,TD.IMAGE_LEVEL,TD.TAX_LEVEL,TD.INVOICE_ACCOUNT,TD.INVOICE_ADD,TD.INVOICE_PHONE,TD.INVOICE_POST_ADD,TD.IMAGE_LEVEL2,TD.CH_ADDRESS,TD.CH_ADDRESS2\n"); 
		sql.append("          FROM TM_DEALER TD, TM_COMPANY TC\n");  
		sql.append("         WHERE TD.COMPANY_ID = TC.COMPANY_ID\n");  
		sql.append("           AND TD.DEALER_ID = "+dealerId+") D\n");  
		sql.append("  LEFT JOIN TM_DEALER TD ON D.PARENT_DEALER_D = TD.DEALER_ID\n");
		logger.debug("SQL+++++++++++++++++++++++: " + sql.toString());
		Map<String, Object> map = pageQueryMap(sql.toString(),null,getFunName());
		return map;
	}
	
	/**
	 * 根据经销商Id查询经销商详情
	 * @param dealerId
	 * @return
	 */
	public Map<String, Object> getDealerInfobyId1(String dealerId){
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT D.*,\n");
		sql.append("       TD.DEALER_ID AS SJDEALERID,\n");  
		sql.append("       TD.DEALER_NAME AS SJDEALERNAME,\n");  
		sql.append("       DECODE(D.DEALER_LEVEL,\n");  
		sql.append("              10851001,\n");  
		sql.append("              (SELECT TMO.ORG_ID\n");  
		sql.append("                 FROM TM_ORG TMO, TM_DEALER_ORG_RELATION TDOR\n");  
		sql.append("                WHERE TMO.ORG_ID = TDOR.ORG_ID\n");  
		sql.append("                  AND TDOR.DEALER_ID = D.DEALER_ID),\n");  
		sql.append("              '') AS ORG_ID,\n");  
		sql.append("       DECODE(D.DEALER_LEVEL,\n");  
		sql.append("              10851001,\n");  
		sql.append("              (SELECT TMO.ORG_CODE\n");  
		sql.append("                 FROM TM_ORG TMO, TM_DEALER_ORG_RELATION TDOR\n");  
		sql.append("                WHERE TMO.ORG_ID = TDOR.ORG_ID\n");  
		sql.append("                  AND TDOR.DEALER_ID = D.DEALER_ID),\n");  
		sql.append("              '') AS ORG_CODE,\n");  
		sql.append("       DECODE(D.DEALER_LEVEL,\n");  
		sql.append("              10851001,\n");  
		sql.append("              (SELECT TMO.ORG_NAME\n");  
		sql.append("                 FROM TM_ORG TMO, TM_DEALER_ORG_RELATION TDOR\n");  
		sql.append("                WHERE TMO.ORG_ID = TDOR.ORG_ID\n");  
		sql.append("                  AND TDOR.DEALER_ID = D.DEALER_ID),\n");  
		sql.append("              '') AS ORG_NAME\n");  
		sql.append("  FROM (SELECT TD.DEALER_ID,\n");  
		sql.append("               TD.COMPANY_ID,\n");  
		sql.append("               TD.DEALER_TYPE,TC2.CODE_DESC DEALER_TYPE_N ,\n");  
		sql.append("               TD.DEALER_CODE,\n");  
		sql.append("               TD.DEALER_NAME,\n");  
		sql.append("               TD.DEALER_CLASS, TC4.CODE_DESC DEALER_CLASS_N,\n"); 
		sql.append("               TD.STATUS,TC3.CODE_DESC STATUS_N,\n");  
		sql.append("               TD.DEALER_LEVEL,TC1.CODE_DESC DEALER_LEVEL_N,\n");  
		sql.append("               TD.PARENT_DEALER_D,\n");  
		sql.append("               TD.DEALER_ORG_ID,\n");  
		sql.append("               (SELECT REGION_NAME FROM TM_REGION WHERE REGION_CODE = TD.PROVINCE_ID) PROVINCE_NAME,\n");  
		sql.append("               (SELECT REGION_NAME FROM TM_REGION WHERE REGION_CODE = TD.CITY_ID ) CITY_NAME,\n");  
		sql.append("               TD.ZIP_CODE,\n");  
		sql.append("               TD.ADDRESS,\n");  
		sql.append("               TD.PHONE,\n");  
		sql.append("               TD.FAX_NO,\n");  
		sql.append("               TD.LINK_MAN,\n");  
		sql.append("               TD.EMAIL,\n");  
		sql.append("    		   TD.SITEDATE,\n" );
		sql.append("      		   TD.DESTROYDATE,\n" );
		sql.append("               TD.SPY_MAN,\n" );
		sql.append("               TD.SPY_PHONE,\n" );
		sql.append("               TD.WEBMASTER_NAME,\n" );
		sql.append("               TD.BRAND,\n" );
		sql.append("               TD.ZZADDRESS,");
		sql.append("               TD.REMARK,\n");  
		sql.append("               TD.DEALER_SHORTNAME,\n");  
		sql.append("               TD.OEM_COMPANY_ID,\n");  
		sql.append("               TD.PRICE_ID,\n");   
		sql.append("               TC.COMPANY_SHORTNAME,\n");  
		sql.append("               TD.IS_SPECIAL,\n"); 
		sql.append("               TD.MARKET_NAME,\n");  
		sql.append("               TD.MARKET_TEL,\n"); 
		sql.append("               TD.ERP_CODE,\n");  
		sql.append("               (SELECT CODE_DESC FROM TC_CODE WHERE CODE_ID = TD.SERVICE_STATUS) SERVICE_STATUS,\n");  
		sql.append("               TD.TAXES_NO,TC6.CODE_DESC DEALER_LABOUR_TYPE,\n");  
		sql.append("               (SELECT CODE_DESC FROM TC_CODE WHERE CODE_ID = TD.IS_DQV) IS_DQV,\n"); 
		sql.append("               (SELECT CODE_DESC FROM TC_CODE WHERE CODE_ID = TD.BALANCE_LEVEL) BALANCE_LEVEL,\n"); 
		sql.append("               TO_CHAR(TD.CREATE_DATE,'YYYYMM') CREATE_DATE,\n");
		sql.append("               (SELECT CODE_DESC FROM TC_CODE WHERE CODE_ID = TD.ADMIN_LEVEL) ADMIN_LEVEL,\n");
		sql.append("               (SELECT CODE_DESC FROM TC_CODE WHERE CODE_ID = TD.IMAGE_LEVEL) IMAGE_LEVEL,\n");
		sql.append("               (SELECT CODE_DESC FROM TC_CODE WHERE CODE_ID = TD.IMAGE_LEVEL2) IMAGE_LEVEL2,\n");
		sql.append("               (SELECT CODE_DESC FROM TC_CODE WHERE CODE_ID = TD.INVOICE_LEVEL) INVOICE_LEVEL,(SELECT REGION_NAME FROM TM_REGION WHERE REGION_CODE = TD.COUNTIES) COUNTIES,TD.TOWNSHIP,TD.LEGAL,TD.WEBMASTER_PHONE,TD.DUTY_PHONE,TD.BANK,TC5.CODE_DESC MAIN_RESOURCES,TD.TAX_LEVEL,TD.INVOICE_ACCOUNT,TD.INVOICE_ADD,TD.INVOICE_PHONE,TD.INVOICE_POST_ADD,TD.CH_ADDRESS,TD.CH_ADDRESS2\n"); 
		sql.append("          FROM TM_DEALER TD, TM_COMPANY TC , TC_CODE TC1 , TC_CODE TC2 ,TC_CODE TC3, \n");
		sql.append("          	   TC_CODE TC4, TC_CODE TC5 , TC_CODE TC6 \n"); 
		sql.append("         WHERE TD.COMPANY_ID = TC.COMPANY_ID\n");  
		sql.append("           AND TD.DEALER_LEVEL = TC1.CODE_ID(+) AND TD.DEALER_TYPE = TC2.CODE_ID(+) AND TD.STATUS = TC3.CODE_ID(+)\n");
		sql.append("           AND TD.DEALER_CLASS = TC4.CODE_ID(+) AND TD.MAIN_RESOURCES = TC5.CODE_ID(+) AND TD.DEALER_LABOUR_TYPE = TC6.CODE_ID(+) \n");
		sql.append("           AND TD.DEALER_ID = "+dealerId+") D,TM_DEALER TD WHERE 1 = 1\n");  
		sql.append("  AND D.PARENT_DEALER_D = TD.DEALER_ID(+)\n");
		logger.debug("SQL+++++++++++++++++++++++: " + sql.toString());
		Map<String, Object> map = pageQueryMap(sql.toString(),null,getFunName());
		return map;
	}
	
	/**
	 * 根据地址Id查询经销商地址详情
	 * @param id
	 * @return
	 */
	public Map<String, Object> getAddressfobyId(String id){
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT TVA.Receive_Org,TVA.Province_Id,TVA.City_Id,TVA.Area_Id,TVA.ID,TVA.ADD_CODE,TVA.ADDRESS,TVA.LINK_MAN,TVA.TEL,TVA.REMARK,TVA.STATUS,TVA.DEALER_ID, TD.address as myaddress, tva.b_area_id,TVA.MOBILE_PHONE  FROM TM_VS_ADDRESS TVA,TM_DEALER TD WHERE TVA.Dealer_Id=TD.DEALER_ID  and TVA.ID ='"+id+"'");
		logger.debug("SQL+++++++++++++++++++++++: " + sql.toString());
		Map<String, Object> map = pageQueryMap(sql.toString(),null,getFunName());
		return map;
	}
	public  int setDownDealerPrice(String erpCode)throws Exception {
		StringBuffer sql= new StringBuffer();
		sql.append("UPDATE t_dealer_account_details_tmp TDADT SET TDADT.REMARK='',TDADT.EXPORT_STATUS=0\n");
		sql.append("WHERE TDADT.REMARK LIKE '%XXX%' AND TDADT.EXPORT_STATUS=3  AND TDADT.DEALER_ERP_ORG_CODE='"+erpCode+"'");
		return factory.update(sql.toString(), null);
		
	}
	/**
	 * 查询经销商业务范围
	 * 
	 * @param dealerId 经销商Id
	 * @return
	 * @throws Exception
	 */
	public  List<Map<String, Object>> getDealerBusinessArea(String dealerId)throws Exception {
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT TDBA.RELATION_ID,\n");
		sql.append("       TDBA.DEALER_ID,\n");  
		sql.append("       TDBA.AREA_ID,\n");  
		sql.append("       TBA.AREA_CODE,\n");  
		sql.append("       TBA.AREA_NAME\n");  
		sql.append("  FROM TM_DEALER_BUSINESS_AREA TDBA, TM_BUSINESS_AREA TBA\n");  
		sql.append(" WHERE TDBA.AREA_ID = TBA.AREA_ID\n");  
		sql.append("   AND TDBA.DEALER_ID = "+dealerId+"\n");
		logger.debug("SQL+++++++++++++++++++++++: " + sql.toString());
		return pageQuery(sql.toString(),null,getFunName());
	}
	/**
	 * 查询经销商地址
	 * 
	 * @param dealerId 经销商Id
	 * @return
	 * @throws Exception
	 */
	public  List<Map<String, Object>> getAddress(String dealerId)throws Exception {

		StringBuffer sql= new StringBuffer();
		sql.append("SELECT TVA.ID,\n");  
		sql.append("       TVA.ADD_CODE,\n");  
		sql.append("       TVA.ADDRESS,\n");  
		sql.append("       TVA.LINK_MAN,\n");  
		sql.append("       TVA.TEL,TVA.MOBILE_PHONE,\n");  
		sql.append("       TVA.REMARK,\n");  
		sql.append("       TVA.STATUS,\n");  
		sql.append("       TBA.AREA_NAME,\n"); 
		sql.append("       TVA.DEALER_ID\n");  
		sql.append("  FROM TM_VS_ADDRESS TVA, TM_BUSINESS_AREA TBA\n");  
		sql.append(" WHERE TVA.B_AREA_ID = TBA.AREA_ID(+) AND TVA.DEALER_ID = "+dealerId+" ORDER BY TVA.Status,TVA.ADDRESS \n");		
		logger.debug("SQL+++++++++++++++++++++++: " + sql.toString());
		return pageQuery(sql.toString(),null,getFunName());
	}
	/**
	 * 查询没维护给经销商业务范围
	 * 
	 * @param dealerId 经销商Id
	 * @param companyId 公司Id
	 * @return
	 * @throws Exception
	 */
	public  List<Map<String, Object>> getBusinessNoInDealer(String dealerId,Long companyId)throws Exception {

		StringBuffer sql= new StringBuffer();
		sql.append("SELECT TBA.AREA_ID, TBA.AREA_CODE, TBA.AREA_NAME\n");
		sql.append("  FROM TM_BUSINESS_AREA TBA\n");  
		sql.append(" WHERE TBA.COMPANY_ID = "+companyId+"\n");  
		sql.append("   AND NOT EXISTS (SELECT 1\n");  
		sql.append("          FROM TM_DEALER_BUSINESS_AREA TDBA\n");  
		sql.append("         WHERE TDBA.AREA_ID = TBA.AREA_ID\n");  
		sql.append("           AND TDBA.DEALER_ID = "+dealerId+")\n");

		//增加 隐藏同一公司下已经被其他渠道对应的业务范围 的逻辑 by WangLiang @100727
		//也就是说不能一个业务范围不能被同一个公司内的经销商选择多次
		sql.append("   AND NOT EXISTS (SELECT 1\n");
		sql.append("       FROM TM_DEALER D, TM_DEALER_BUSINESS_AREA A\n");  
		sql.append("      WHERE D.COMPANY_ID IN\n");  
		sql.append("            (SELECT COMPANY_ID\n");  
		sql.append("               FROM TM_DEALER\n");  
		sql.append("              WHERE DEALER_ID = "+dealerId+")\n");  
		sql.append("        AND (D.DEALER_TYPE = "+Constant.DEALER_TYPE_DVS+" OR D.DEALER_TYPE = "+Constant.DEALER_TYPE_JSZX+")\n");  
		sql.append("        AND D.STATUS = "+Constant.STATUS_ENABLE+"\n");  
		sql.append("        AND A.DEALER_ID = D.DEALER_ID\n");  
		sql.append("        AND A.AREA_ID = TBA.AREA_ID)");
		//END
		
		logger.debug("SQL+++++++++++++++++++++++: " + sql.toString());
		return pageQuery(sql.toString(),null,getFunName());
	}
	
	public  List<Map<String, Object>> querySameBusiness(String dealerId,Long companyId)throws Exception {

		StringBuffer sql= new StringBuffer();
	
				sql.append("select temp.produce_base,       count(1) as count  from (select distinct a.dealer_id, d.produce_base                       From tm_dealer               a,                            Tm_Dealer_Business_Area c,                           TM_BUSINESS_AREA        d                      where a.dealer_level = "+Constant.DEALER_LEVEL_01+"                        and a.status = "+Constant.STATUS_ENABLE+"                        and a.dealer_type != "+Constant.MSG_TYPE_2+"                        and c.dealer_id = a.dealer_id                        and d.area_id = c.area_id                       and a.company_id =                           (select b.company_id                              from tm_dealer b                             where b.dealer_id = "+dealerId+"  and a.dealer_id !="+dealerId+")        ) temp group by temp.produce_base");
		logger.debug("SQL+++++++++++++++++++++++: " + sql.toString());
		return pageQuery(sql.toString(),null,getFunName());
	}
	
	/*
	 * 通过经销商ID查询经销商名称
	 * @dealerIds 经销商ID
	 */
	public List<Map<String,Object>> getDealerInfo(String dealerIds) {
		StringBuffer sql = new StringBuffer("") ;
		
		sql.append("select td.dealer_name, td.dealer_id,td.dealer_code\n");
		sql.append("  from tm_dealer td\n");  
		sql.append(" where td.dealer_id in ("+dealerIds+")\n");  
		sql.append("and td.status = "+Constant.STATUS_ENABLE+"\n");
		sql.append(" order by td.dealer_name,td.dealer_code\n");

		return dao.pageQuery(sql.toString(), null, getFunName()) ;
	}
	/*
	 * 通过经销商ID查询其下级经销商
	 * @dealerIds 经销商ID
	 */
	public List<Map<String,Object>> getLowDelaerInfo(String dealerIds) {
		StringBuffer sql = new StringBuffer("") ;
		
		sql.append("select td2.dealer_name, td2.dealer_id,td2.parent_dealer_d\n");
		sql.append("  from tm_dealer td1, tm_dealer td2\n");  
		sql.append(" where td2.parent_dealer_d = td1.dealer_id\n");  
		sql.append(" and td1.dealer_id in ("+dealerIds+")\n");  
		sql.append(" and td1.status = "+Constant.STATUS_ENABLE+"\n");  
		sql.append(" and td2.status = "+Constant.STATUS_ENABLE+"\n");  
		sql.append(" order by td2.dealer_name\n");

		return dao.pageQuery(sql.toString(), null, getFunName()) ;
	}
	/*
	 * 经销商查询
	 * @dealerId 本级经销商Id
	 * @dealerIds 上级经销商Id
	 * @dealerLevel 需查询经销商级别
	 * @dealerType 经销商类型
	 */
	public List<Map<String, Object>> getDel(String dealerId,String dealerIds, String dealerLevel, String dealerType) {
		StringBuffer sql = new StringBuffer("") ;
		
		sql.append("SELECT  TD.DEALER_NAME, \n") ;
		sql.append("		TD.DEALER_CODE, \n") ;
		sql.append("		TD.DEALER_ID \n") ;
		sql.append("FROM	TM_DEALER TD \n") ;
		sql.append("WHERE	TD.STATUS = "+ Constant.STATUS_ENABLE +" \n") ;
		
		if (!"".equals(dealerId)) {
			sql.append("	AND	TD.DEALER_ID IN ("+ dealerId +") \n") ;
		}
		if (!"".equals(dealerLevel)) {
			sql.append("	AND	TD.DEALER_LEVEL = "+ dealerLevel.trim() +" \n") ;
		}
		if (!"".equals(dealerIds)) {
			sql.append("	AND	TD.PARENT_DEALER_D IN ("+ dealerIds +") \n") ;
		}
		if (!"".equals(dealerType)) {
			sql.append("	AND	TD.DEALER_TYPE = "+ dealerType.trim() +" \n") ;
		}
		
		return dao.pageQuery(sql.toString(), null, getFunName()) ;
	}
	/*
	 * 查询经销商所属区域
	 * @dealerId 经销商Id
	 */
	public List<Map<String, Object>> getOrg(String dealerId) {
		StringBuffer sql = new StringBuffer("") ;
		
		sql.append("SELECT TMDOR.ORG_ID\n");
		sql.append("FROM TM_DEALER TMD, TM_DEALER_ORG_RELATION TMDOR\n");  
		sql.append("WHERE TMD.DEALER_ID = TMDOR.DEALER_ID\n");  
		sql.append("AND TMD.DEALER_ID = " + dealerId + "\n");

		return dao.pageQuery(sql.toString(), null, getFunName()) ;
	}
	
	public List<Map<String, Object>> getDel(String dealerId,String poseId) {
		StringBuffer sql = new StringBuffer("") ;
		
		sql.append("SELECT TMD.DEALER_NAME\n");
		sql.append("  FROM TM_DEALER               TMD,\n");  
		sql.append("       TM_DEALER_BUSINESS_AREA TMDBA,\n");  
		sql.append("       TC_POSE                 TCP,\n");  
		sql.append("       TM_POSE_BUSINESS_AREA   TMPBA\n");  
		sql.append(" WHERE 1 = 1\n");  
		sql.append("   AND TMD.DEALER_ID = TMDBA.DEALER_ID\n");  
		sql.append("   AND TMDBA.AREA_ID = TMPBA.AREA_ID\n");  
		sql.append("   AND TMPBA.POSE_ID = TCP.POSE_ID\n");  
		sql.append("   AND TMD.STATUS = " + Constant.STATUS_ENABLE + " \n");  
		sql.append("   AND TMD.DEALER_ID in (" + dealerId + ") \n");  
		sql.append("   AND TCP.POSE_ID = " + poseId + " \n");
		
		return dao.pageQuery(sql.toString(), null, getFunName()) ;
	}
	
	/**
	 * 获取当前经销商的下级经销商
	 * @param dealerId
	 * @return
	 */
	public List<Map<String, Object>> getDel(String dealerId) {
		StringBuffer sql = new StringBuffer("\n") ;
		
		sql.append("SELECT TMD2.DEALER_ID, TMD2.DEALER_NAME\n");
		sql.append("  FROM TM_DEALER TMD1, TM_DEALER TMD2\n");  
		sql.append(" WHERE TMD1.DEALER_ID = TMD2.PARENT_DEALER_D\n");  
		sql.append("   AND TMD1.STATUS = " + Constant.STATUS_ENABLE + "\n");  
		sql.append("   AND TMD2.STATUS = " + Constant.STATUS_ENABLE + "\n");  
		sql.append("   AND TMD1.DEALER_ID IN (" + dealerId + ")\n");
		sql.append("   AND TMD2.SECEND_AUTID_STATUS ="+Constant.DEALER_SECEND_STATUS_06+"\n");
		return dao.pageQuery(sql.toString(), null, getFunName()) ;
	}
	
	/**
	 * 获取当前经销商的下级经销商
	 * @param dealerId
	 * @return
	 */
	public List<Map<String, Object>> getDelByCode(String dealer_code) {
		StringBuffer sql = new StringBuffer("\n") ;
		
		sql.append("SELECT TMD2.DEALER_ID, TMD2.DEALER_NAME\n");
		sql.append("  FROM TM_DEALER TMD1, TM_DEALER TMD2\n");  
		sql.append(" WHERE TMD1.DEALER_ID = TMD2.PARENT_DEALER_D\n");  
		sql.append("   AND TMD1.STATUS = " + Constant.STATUS_ENABLE + "\n");  
		sql.append("   AND TMD2.STATUS = " + Constant.STATUS_ENABLE + "\n");  
		if(null != dealer_code && !"".equals(dealer_code))
			sql.append("   AND TMD1.DEALER_CODE='"+dealer_code+"' \n");
		sql.append("   AND TMD2.SECEND_AUTID_STATUS ="+Constant.DEALER_SECEND_STATUS_06+"\n");
		return dao.pageQuery(sql.toString(), null, getFunName()) ;
	}
	
	/**
	 * 获取当前经销商的下级经销商
	 * @param dealerId
	 * @return
	 */
	public String getDel__(String dealerId) {
		StringBuffer sql = new StringBuffer("\n") ;
		
		sql.append("SELECT TMD2.DEALER_ID, TMD2.DEALER_NAME\n");
		sql.append("  FROM TM_DEALER TMD1, TM_DEALER TMD2\n");  
		sql.append(" WHERE TMD1.DEALER_ID = TMD2.PARENT_DEALER_D\n");  
		sql.append("   AND TMD1.STATUS = " + Constant.STATUS_ENABLE + "\n");  
		sql.append("   AND TMD2.STATUS = " + Constant.STATUS_ENABLE + "\n");  
		sql.append("   AND TMD1.DEALER_ID IN (" + dealerId + ")\n");
		sql.append("   AND TMD2.SECEND_AUTID_STATUS ="+Constant.DEALER_SECEND_STATUS_06+"\n");
		
		List<Map<String, Object>> dealerList = dao.pageQuery(sql.toString(), null, getFunName()) ;
		
		StringBuffer dealer = new StringBuffer("") ;
		if (null != dealerList && dealerList.size() > 0) {
			int len = dealerList.size() ;
			
			for (int i=0; i<len; i++) {
				if ("".equals(dealer.toString())) {
					dealer.append(dealerList.get(i).get("DEALER_ID")) ;
				} else {
					dealer.append(",").append(dealerList.get(i).get("DEALER_ID")) ;
				}
			}
		}
		
		return dealer.toString() ;
	}
	
	/**
	 * 通过公司id获取该公司下的所有经销商
	 * @param comId
	 * @return
	 */
	public List<Map<String, Object>> getDelList(String comId) {
		StringBuffer sql = new StringBuffer("") ;
		
		sql.append("SELECT TMD.DEALER_ID, TMD.DEALER_CODE, TMD.DEALER_NAME\n");
		sql.append("FROM TM_DEALER TMD\n");  
		sql.append("WHERE TMD.COMPANY_ID = " + comId + "\n");
		
		return dao.pageQuery(sql.toString(), null, getFunName()) ;
	}
	
	/**
	 * 通过经销商id获取经销商所在的业务范围
	 * @param comId
	 * @return
	 */
	public List<Map<String, Object>> getAreaList(String delId) {
		StringBuffer sql = new StringBuffer("") ;
		
		sql.append("SELECT TMBA.AREA_ID, TMBA.AREA_CODE, TMBA.AREA_NAME\n");
		sql.append("  FROM TM_DEALER_BUSINESS_AREA TMDBA, TM_DEALER TMD, TM_BUSINESS_AREA TMBA\n");  
		sql.append(" WHERE 1 = 1\n");  
		sql.append("   AND TMD.DEALER_ID = TMDBA.DEALER_ID\n");  
		sql.append("   AND TMBA.AREA_ID = TMDBA.AREA_ID\n");  
		sql.append("   AND TMDBA.DEALER_ID = " + delId + "\n");
		
		return dao.pageQuery(sql.toString(), null, getFunName()) ;
	}
	
	/**
	 * 获得当前经销商所属区域
	 * @param delId
	 * @return
	 */
	public Long getOrgId(String delId) {
		Long orgId = new Long("1") ;
		StringBuffer sql = new StringBuffer("") ;
		
		sql.append("SELECT TMDOR.root_org_id org_id, TMDOR.root_org_code org_code, TMDOR.root_org_name org_name, TMDOR.DEALER_ID\n");
		sql.append("  FROM vw_org_dealer TMDOR\n");  
		sql.append(" WHERE 1 = 1\n");  
		sql.append("   AND TMDOR.DEALER_ID IN (" + delId + ")\n");

		List<Map<String, Object>> listOrg = dao.pageQuery(sql.toString(), null, getFunName()) ;
		
		if(null != listOrg && listOrg.size() > 0) {
			orgId = Long.parseLong(listOrg.get(0).get("ORG_ID").toString()) ;
		}
		
		return orgId ;
	}
	/**
	 * 根据登录用户的POSE_ID判断是否有"当日销售看板"功能，如果有，返回1，否则返回0
	 * */
	public String isDayReport(Long poseId, String funcName){
		String isReport = "0";
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT TF.FUNC_NAME\n");
		sql.append("  FROM TC_POSE      TP,\n");  
		sql.append("       TR_ROLE_POSE RP,\n");  
		sql.append("       TC_ROLE      TR,\n");  
		sql.append("       TR_ROLE_FUNC TRF,\n");  
		sql.append("       TC_FUNC      TF\n");  
		sql.append(" WHERE TP.POSE_ID = RP.POSE_ID\n");  
		sql.append("   AND RP.ROLE_ID = TR.ROLE_ID\n");  
		sql.append("   AND TR.ROLE_ID = TRF.ROLE_ID\n");  
		sql.append("   AND TRF.FUNC_ID = TF.FUNC_ID\n");  
		sql.append("   AND TP.POSE_ID = "+poseId+"\n");  
		sql.append("   AND TF.FUNC_NAME = '");
		sql.append(funcName);
		sql.append("'\n");
		List<Map<String, Object>> list = dao.pageQuery(sql.toString(), null, getFunName()) ;
		if (null != list && list.size()>0) {
			isReport = "1";
		}
		return isReport;
	}
	
	
	public PageResult<Map<String,Object>> getDealerBS(String con,int pageSize,int curPage){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		Long poseId = logonUser.getPoseId();//职位ID
		//查询职位中业务范围
		String areaIds = MaterialGroupManagerDao.getAreaIdsByPoseId(poseId);
		StringBuffer sql = new StringBuffer("\n");
		sql.append("select t.id,d.dealer_id,d.dealer_code, d.dealer_name,t.update_date, t.status,t.yieldly\n");
		sql.append("  from tm_dealer d, tt_as_dealer_type t,vw_org_dealer_service ds \n");  
		sql.append(" where d.dealer_id = t.dealer_id\n");
		if(StringUtil.notNull(con))
		sql.append(con);
		sql.append("  AND EXISTS (SELECT 1\n" );
		sql.append("       FROM tm_business_area TDBA\n" );
		sql.append("      WHERE TDBA.produce_base = t.yieldly\n" );
		sql.append("        AND TDBA.AREA_ID IN ("+areaIds+")\n)");
		sql.append("and d.dealer_id=ds.dealer_id\n" );
		sql.append(" order by d.dealer_code asc\n");
		return this.pageQuery(sql.toString(),null,this.getFunName(), pageSize, curPage);
	}
	//zhumingwei add by 2011-02-24
	public PageResult<Map<String, Object>> queryDealerInfoChange(String dealerId,String code,String status,String startDate,String endDate,int curPage,int pageSize) throws Exception{
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT * from tm_dealer_change a where 1=1\n");
		if (Utility.testString(dealerId)) {
			sql.append("and a.dealer_id = '"+dealerId+"' \n");
		}
		if (Utility.testString(code)) {
			sql.append("and a.dealer_num like '"+code+"' \n");
		}
		if (Utility.testString(status)) {
			sql.append("and a.status = "+status+" \n");
		}
		if(Utility.testString(startDate)){
			sql.append("AND A.dealer_time >= TO_DATE('"+startDate+"','YYYY-MM-DD HH24:MI:SS')\n" );
		}
		if(Utility.testString(endDate)){
			sql.append("AND A.dealer_time <= TO_DATE('"+endDate+"','YYYY-MM-DD HH24:MI:SS')\n" );
		}
		sql.append("order by a.dealer_time desc\n" );
		PageResult<Map<String, Object>> rs = dao.pageQuery(sql.toString(), null, getFunName(),pageSize, curPage);
		return rs;
	}
	
	public PageResult<Map<String, Object>> queryAuthDealerChange(String code,String status,String startDate,String endDate,int curPage,int pageSize) throws Exception{
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT * from tm_dealer_change a where 1=1\n");
		if (Utility.testString(code)) {
			sql.append("and a.dealer_num like '"+code+"' \n");
		}
		if (Utility.testString(status)) {
			sql.append("and a.status = "+status+" \n");
		}
		if(Utility.testString(startDate)){
			sql.append("AND A.dealer_time >= TO_DATE('"+startDate+"','YYYY-MM-DD HH24:MI:SS')\n" );
		}
		if(Utility.testString(endDate)){
			sql.append("AND A.dealer_time <= TO_DATE('"+endDate+"','YYYY-MM-DD HH24:MI:SS')\n" );
		}
		PageResult<Map<String, Object>> rs = dao.pageQuery(sql.toString(), null, getFunName(),pageSize, curPage);
		return rs;
	}
	
	public List<Map<String, Object>> selectDealerInfo(long dealerId) {
		StringBuffer sql = new StringBuffer("") ;
		sql.append("select b.root_org_name,b.root_org_code,c.company_shortname,a.*,(select ee.dealer_name from  tm_dealer ee where ee.dealer_id=a.parent_dealer_d) as parent_name from tm_dealer a,vw_org_dealer_service b,TM_COMPANY c where a.dealer_id=b.dealer_id and a.company_id=c.company_id and a.dealer_id="+dealerId+"\n");
		return dao.pageQuery(sql.toString(), null, getFunName()) ;
	}
	
	public List<Map<String, Object>> selectDealerUpdateAuthing(String dealerId,String yilyle) {
		String sql = "select a.*,u.namE from TT_AS_DEALER_TYPE_AUTHITEM a,tc_user u   where a.dealer_id="+dealerId+" and a.yilie="+yilyle+" and a.create_by=u.user_id order by a.create_date  ";
		
		return dao.pageQuery(sql.toString(), null, getFunName()) ;
	}
	
	public Map<String, Object> getDlrClass(String dealerId) {
		StringBuffer sql = new StringBuffer("\n") ;
		
		sql.append("select tmd.dealer_class from tm_dealer tmd where tmd.dealer_id = ").append(dealerId).append(" ;") ;
		
		return super.pageQueryMap(sql.toString(), null, super.getFunName()) ;
	}
	
	public PageResult<Map<String, Object>> DealerCreditInfo(String dealerCode,String dealerName,String mortgageType,String startDate,String endDate,String orgCode,String province,int curPage,int pageSize) throws Exception{
		StringBuffer sql= new StringBuffer();
		
		
		sql.append("SELECT distinct TD.DEALER_ID,TD.DEALER_SHORTNAME,TD.DEALER_CODE,TDC.CREDIT_DATE,TC.CODE_DESC AS TYPE from TM_DEALER_CREDIT TDC,TM_DEALER TD,TC_CODE TC WHERE TDC.DEALER_ID=TD.DEALER_ID  AND TC.CODE_ID=TDC.TYPE\n");
		
		if(dealerCode!=null&&!"".equals(dealerCode))
		{
			sql.append("           AND TD.DEALER_CODE LIKE '%"+dealerCode+"%'\n"); 
		}
		if(dealerName!=null&&!"".equals(dealerName))
		{
			sql.append("           AND TD.DEALER_SHORTNAME LIKE '%"+dealerName+"%'\n"); 
		}
		if(mortgageType!=null&&!"".equals(mortgageType))
		{
			sql.append("           AND TDC.TYPE = "+mortgageType+"\n"); 
		}
		
		if (null != startDate && !"".equals(startDate)) {
			sql.append("   AND TDC.CREDIT_DATE >= TO_DATE('"+ startDate+" 00:00:00','yyyy-MM-dd HH24:MI:SS')\n");
		}
		if (null != endDate && !"".equals(endDate)) {
			sql.append("   AND TDC.CREDIT_DATE  <= TO_DATE('"+endDate+" 23:59:59','yyyy-MM-dd HH24:MI:SS')\n");
		}
		
		if(orgCode!=null&&!"".equals(orgCode))
		{
			sql.append("     AND EXISTS (SELECT 1 FROM TM_DEALER_ORG_RELATION TDOR,TM_ORG TMO WHERE TDOR.ORG_ID=TMO.ORG_ID AND TMO.ORG_CODE='"+orgCode+"' AND (TDOR.DEALER_ID=TD.DEALER_ID OR TDOR.DEALER_ID=TD.PARENT_DEALER_D))\n"); 
		}
	

	if(province!=null&&province!=""){
			sql.append("          AND TD.PROVINCE_ID='"+province+"'\n"); 
		}


		
		return dao.pageQuery(sql.toString(), null, dao.getFunName(),pageSize , curPage);
		
	}
	
	//查询信贷类型
	public List<Map<String,Object>> queryMortgageType(){
		StringBuffer sql = new StringBuffer();
		List<Object> params = new LinkedList<Object>();
		sql.append("select tc.code_id,tc.code_desc From tc_code tc where type="+Constant.MORTGAGE_TYPE);
	
		
		return dao.pageQuery(sql.toString(), params, getFunName());
	}
	
	public static PageResult<Map<String,Object>> addNewDealerCreditInfo(String orgId, String dealerCode,String dealerName,int curPage,int pageSize){
StringBuffer sql = new StringBuffer("\n") ;
		
		sql.append(" select distinct  tm.dealer_id,tm.dealer_code,tm.dealer_shortname,to_char(TDC.CREDIT_DATE, 'yyyy-mm-dd') CREDIT_DATE From tm_dealer tm,tm_dealer_credit TDC, vw_org_dealer vod  WHERE 1=1 AND tm.dealer_id=vod.dealer_id and tm.DEALER_ID=TDC.DEALER_ID(+) ");
			
		if(dealerCode!=null&&!"".equals(dealerCode))
		{
			sql.append("           AND tm.DEALER_CODE LIKE '%"+dealerCode+"%'\n"); 
		}
		if(dealerName!=null&&!"".equals(dealerName))
		{
			sql.append("           AND tm.DEALER_SHORTNAME LIKE '%"+dealerName+"%'\n"); 
		}
		
		if(!CommonUtils.isNullString(orgId)) {
			sql.append("           AND vod.root_org_id in ("+orgId+")\n"); 
		}

		
		
		return getPoseByUserIdA(dao.pageQuery(sql.toString(), null, dao.getFunName(),pageSize , curPage)) ;
	}
	
	private static PageResult<Map<String,Object>> getPoseByUserIdA(PageResult<Map<String,Object>> rs) {
		if (rs.getRecords() != null) {
			List<Map<String,Object>> list = rs.getRecords(); // 取出用户
			String TYPE = null;
			String sql = " SELECT TC.CODE_DESC FROM tm_dealer_credit TDC,TM_DEALER TD,TC_CODE TC WHERE TDC.DEALER_ID = TD.DEALER_ID AND  TC.CODE_ID=TDC.TYPE";
			for (int i = 0; i < list.size() && list != null; i++) {
				TYPE = "";
				Map<String,Object> user = list.get(i);
				List<TcCodePO> postList = factory.select(sql
						+ " AND TDC.DEALER_id ='" + user.get("DEALER_ID") + "'", null,
						new DAOCallback<TcCodePO>() {
							public TcCodePO wrapper(ResultSet rs, int idx) {
								TcCodePO bean = new TcCodePO();
								try {
									bean.setCodeDesc(rs.getString("CODE_DESC"));
								} catch (SQLException e) {
									e.printStackTrace();
								}
								return bean;
							}
						});
				for (int j = 0; j < postList.size(); j++) {
					TYPE += postList.get(j).getCodeDesc() + ",";
				}
				if (!"".equals(TYPE) && TYPE.length() > 0) {
					TYPE = TYPE.substring(0, TYPE.length() - 1);
				}
				user.put("TYPE", TYPE);
				list.set(i, user);
			}
			rs.setRecords(list);
		}
		return rs;
	}
	
	//删除个人信贷
	public  void deleteDealerCredit(String dealerId){
		
		List listPar=new ArrayList();
		String sql="delete from tm_dealer_credit where DEALER_ID=?";
		//System.out.println("dddd:"+acFileId);
		listPar.add(dealerId);	
		//System.out.println(sql);
		factory.delete(sql,listPar);
	}
	
	//新增个人信贷
	public  Long addDealerCredit(TmDealerCreditPO tt){
		//得到一个自动增长的主键
		 String pkId=SequenceManager.getSequence("");  
		Long id=Long.valueOf(pkId);
		//把自动增长放入存放数据的类中
		tt.setCreditId(id);
		//插入方法
		factory.insert(tt);	
		return id;
	}
	
	public PageResult<Map<String,Object>> getIndividualTrust(Map<String, String> map,int pageSize, int curPage) {
		String orgId = map.get("orgId") ;
		String regionId = map.get("regionId") ;
		String dlrCode = map.get("dlrCode") ;
		String dlrName = map.get("dlrName") ;
		String iType = map.get("iType") ;
		String startDate = map.get("startDate") ;
		String endDate = map.get("endDate") ;
		
		StringBuffer sql = new StringBuffer("\n") ;
		
		sql.append("select tmd.dealer_code,\n");
		sql.append("       tmd.dealer_shortname,\n");  
		sql.append("       vod.root_org_name,\n");  
		sql.append("       tmr.region_name,\n");  
		sql.append("       tcc.code_desc,\n");  
		sql.append("       to_char(tdc.credit_date, 'yyyy-mm-dd') CREDIT_DATE\n");  
		sql.append("  from TM_DEALER_CREDIT tdc,\n");  
		sql.append("       vw_org_dealer    vod,\n");  
		sql.append("       tm_dealer        tmd,\n");  
		sql.append("       tm_region        tmr,\n");  
		sql.append("       tc_code          tcc\n");  
		sql.append(" where tdc.dealer_id = vod.dealer_id\n");  
		sql.append("   and vod.dealer_id = tmd.dealer_id\n");  
		sql.append("   and tmd.province_id = tmr.region_code\n");  
		sql.append("   and tdc.type = tcc.code_id\n");
		
		if(!CommonUtils.isNullString(orgId)) {
			sql.append("   and vod.root_org_id in (").append(orgId).append(")\n") ;
		}
		if (!CommonUtils.isNullString(regionId)) {
			sql.append("   and tmr.region_id in (").append(regionId).append(")\n") ;
		}
		if (!CommonUtils.isNullString(dlrCode)) {
			sql.append("   and tmd.dealer_code like '%").append(dlrCode).append("%'\n") ;
		}
		if (!CommonUtils.isNullString(dlrName)) {
			sql.append("   and tmd.dealer_shortname like '%").append(dlrName).append("%'\n") ;
		}
		if (!CommonUtils.isNullString(iType)) {
			sql.append("   and tdc.type = ").append(iType).append("\n") ;
		}
		if (!CommonUtils.isNullString(startDate)) {
			sql.append("   and trunc(credit_date) >= to_date('").append(startDate).append("','yyyy-mm-dd')\n") ;
		}
		if (!CommonUtils.isNullString(endDate)) {
			sql.append("   and trunc(credit_date) <= to_date('").append(endDate).append("','yyyy-mm-dd')\n") ;
		}
		
		sql.append("  order by vod.root_org_name, tmr.region_name, tmd.dealer_shortname\n");

		return super.pageQuery(sql.toString(), null, super.getFunName(), pageSize, curPage) ;
	}
	
	//根据经销商查询个人信贷信息
	public List<Map<String,Object>> queryMortgageForDealer(String dealerId){
		StringBuffer sql = new StringBuffer();
		List<Object> params = new LinkedList<Object>();
		sql.append("select tc.code_desc,tdc.credit_id,to_char(tdc.credit_date,'yyyy-mm-hh') as credit_date from tm_dealer_credit tdc,tc_code tc where tc.code_id=tdc.type and dealer_id="+dealerId);
	
		
		return dao.pageQuery(sql.toString(), params, getFunName());
	}
	
	/**
	 * 根据经销商编号获取经销商ID
	 * @param dealerCode
	 * @return
	 */
	public static String getDealerIdByCode(String dealerCode) {
		StringBuffer sql = new StringBuffer();
		List<Object> params = new LinkedList<Object>(); params.add(dealerCode);
		sql.append("SELECT * FROM TM_DEALER WHERE DEALER_CODE = ?");
		return dao.pageQueryMap(sql.toString(), params, dao.getFunName()).get("DEALER_CODE").toString();
	}
	
	/**
	 * 经销商详细信息下载
	 * @param dealer_type
	 * @param map 
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	public List<Map<String, Object>> dealerInfoDetailDownload(String dealer_type) {
		List<Object> params = new LinkedList<Object>();
		ActionContext act = ActionContext.getContext();
		RequestWrapper request = act.getRequest();
		String ECREATE_DATE = CommonUtils.checkNull(request.getParamValue("ECREATE_DATE"));
		String SCREATE_DATE = CommonUtils.checkNull(request.getParamValue("SCREATE_DATE"));
		String regionId = CommonUtils.checkNull(request.getParamValue("regionId"));
		String AUTHORIZATION_TYPE = CommonUtils.checkNull(request.getParamValue("AUTHORIZATION_TYPE"));
		String dealerLevel = CommonUtils.checkNull(request.getParamValue("DEALERLEVEL"));
		String sJDealerCode = CommonUtils.checkNull(request.getParamValue("sJDealerCode"));
		String dealerType = CommonUtils.checkNull(request.getParamValue("DEALER_TYPE"));
		String orgCode = CommonUtils.checkNull(request.getParamValue("orgCode"));
		String dealerName = CommonUtils.checkNull(request.getParamValue("DEALER_NAME"));
		String orgId = CommonUtils.checkNull(request.getParamValue("orgId"));
		String serviceStatus = CommonUtils.checkNull(request.getParamValue("SERVICE_STATUS"));
		String parentOrgCode = CommonUtils.checkNull(request.getParamValue("parentOrgCode"));
		String dealerCode = CommonUtils.checkNull(request.getParamValue("DEALER_CODE"));
		String companyName = CommonUtils.checkNull(request.getParamValue("COMPANY_NAME"));
		String dealerStatus = CommonUtils.checkNull(request.getParamValue("DEALERSTATUS"));
		String regionCode = CommonUtils.checkNull(request.getParamValue("regionCode"));
		StringBuffer sbSql = new StringBuffer();
		sbSql.append("select (select region_name from tm_region where region_code = a.province_id) province_name,"
					+"(select region_name from tm_region where region_code = a.city_id) city_name,"
					+"(select region_name from tm_region where region_code = a.counties) counties_name,a.*, b.* from "
					+ "tm_dealer a, "
					+ "tm_dealer_detail b "
					+ "where a.dealer_id = b.fk_dealer_id(+) "
					+ "and a.dealer_type = " + dealer_type);
		
		if(!dealerType.equals("")) {
			sbSql.append("   and a.dealer_type = ?\n");
			params.add(dealerType);
		}
		
		if(!dealerName.equals("")){
			sbSql.append("   and a.dealer_name like ?\n");
			params.add("%"+dealerName+"%");
		}
		
		if(!dealerCode.equals("")){
			sbSql.append("   and a.dealer_code like ?\n");
			params.add("%"+dealerCode+"%");
		}
		
		if(!dealerLevel.equals("")) {
			sbSql.append("	 and a.dealer_level = ?\n");
			params.add(dealerLevel);
		}
		
		if(!dealerStatus.equals("")) {
			sbSql.append(" 	 and a.status = ?\n");
			params.add(dealerStatus);
		}

		if(!parentOrgCode.equals("")) {
			sbSql.append(" 	 and o.parent_org_id in (select org_id from tm_org where org_code = ?)\n");
			params.add(parentOrgCode);
		}

		if(!sJDealerCode.equals("")) {
			sbSql.append(" 	 and a.parent_dealer_d in (select dealer_id from tm_dealer where dealer_code = ?)\n");
			params.add(sJDealerCode);
		}
		
		if(!companyName.equals("")) {
			sbSql.append(" 	 and e.company_shortname = ?\n");
			params.add(companyName);
		}
		
		if(!serviceStatus.equals("")) {
			sbSql.append("   and a.service_status = ?\n");
			params.add(serviceStatus);
		}
//		if(!WORK_TYPE.equals("")) {
//			sbSql.append("   and b.WORK_TYPE = ?\n");
//			params.add(WORK_TYPE);
//		}
		if(!AUTHORIZATION_TYPE.equals("")) {
			sbSql.append("   and b.AUTHORIZATION_TYPE = ?\n");
			params.add(AUTHORIZATION_TYPE);
		}
		if (!CommonUtils.isNullString(regionId)) {
			sbSql.append("   and f.region_id in (").append(regionId).append(")\n") ;
		}
		if (!CommonUtils.isNullString(orgCode)) {
			sbSql.append("   and o.org_Code = '").append(orgCode).append("'\n") ;
		}
		return dao.pageQuery(sbSql.toString(), params, getFunName());
	}
	
	/**
	 * 经销商地址打印查询
	 * @param map
	 * @param pageSize
	 * @param curPage
	 * @return
	 */
	public PageResult<Map<String, Object>> queryDealerAddressInit(
			Map<String, Object> map, Integer pageSize, Integer curPage) {
		String dealerType = CommonUtils.checkNull(map.get("DEALER_TYPE")); 
		String dealerCode = CommonUtils.checkNull(map.get("DEALER_CODE"));
		String dealerName = CommonUtils.checkNull(map.get("DEALER_NAME"));
		String addressType = CommonUtils.checkNull(map.get("ADDRESS_TYPE"));
		String addressType_sh = CommonUtils.checkNull(map.get("ADDRESS_TYPE_SH"));
		
		List<Object> params = new LinkedList<Object>();
		StringBuffer sbSql = new StringBuffer();
		
		sbSql.append("with addr as(SELECT *\n");
		sbSql.append("  FROM (SELECT dealer_id,\n");
		sbSql.append("               address,\n");
		sbSql.append("               sex,\n");
		sbSql.append("               link_man,\n");
		sbSql.append("               tel,\n");
		sbSql.append("               mobile_phone,\n");
		sbSql.append("               address_type,\n");
		sbSql.append("               status,\n");
		sbSql.append("               id\n");
		sbSql.append("          FROM tm_vs_address_more\n");
		sbSql.append("        UNION ALL\n");
		sbSql.append("        SELECT dealer_id,\n");
		sbSql.append("               (SELECT region_name\n");
		sbSql.append("                  FROM tm_region\n");
		sbSql.append("                 WHERE region_code = province_id) ||\n");
		sbSql.append("               (SELECT region_name FROM tm_region WHERE region_code = city_id) ||\n");
		sbSql.append("               (SELECT region_name FROM tm_region WHERE region_code = area_id) ||\n");
		sbSql.append("               address as address,\n");
		sbSql.append("               sex,\n");
		sbSql.append("               link_man,\n");
		sbSql.append("               tel,\n");
		sbSql.append("               mobile_phone,\n");
		sbSql.append("               address_type,\n");
		sbSql.append("               status,\n");
		sbSql.append("               id\n");
		sbSql.append("          FROM tm_vs_address\n");
		sbSql.append("        UNION ALL\n");
		sbSql.append("        SELECT dealer_id,\n");
		sbSql.append("               addr as address,\n");
		sbSql.append("               gender as sex,\n");
		sbSql.append("               linkman,\n");
		sbSql.append("               tel,\n");
		sbSql.append("               mobile_phone,\n");
		sbSql.append("               address_type,\n");
		sbSql.append("               state as status,\n");
		sbSql.append("               addr_id as id\n");
		sbSql.append("          FROM TT_PART_ADDRESS_MORE\n");
		sbSql.append("        UNION ALL\n");
		sbSql.append("        SELECT dealer_id,\n");
		sbSql.append("               addr as address,\n");
		sbSql.append("               gender as sex,\n");
		sbSql.append("               linkman,\n");
		sbSql.append("               tel,\n");
		sbSql.append("               mobile_phone,\n");
		sbSql.append("               20491001 as address_type,\n");
		sbSql.append("               state as status,\n");
		sbSql.append("               addr_id as id\n");
		sbSql.append("          FROM tt_part_addr_define)\n");
		sbSql.append(")\n");
		sbSql.append(" select a.dealer_id, a.dealer_code,\n");
		sbSql.append("       a.dealer_name,\n");
		sbSql.append("     c.dealer_code parent_dealer_code,\n");
		sbSql.append("     c.dealer_name parent_dealer_name,\n");
		sbSql.append("       o.org_name,\n");
		sbSql.append("       a.dealer_level,\n");
		sbSql.append("       a.dealer_type,\n");
		sbSql.append("       e.company_name,\n");
		sbSql.append("       a.service_status,\n");
		sbSql.append("       f.address,\n");
		sbSql.append("       f.sex,\n");
		sbSql.append("       f.link_man,\n");
		sbSql.append("       f.tel,\n");
		sbSql.append("       f.mobile_phone,\n");
		sbSql.append("       f.address_type,\n");
		sbSql.append("       f.status,\n");
		sbSql.append("       f.id\n");
		sbSql.append("  from tm_dealer              a,\n");
		sbSql.append("       tm_dealer_detail       b,\n");
		sbSql.append("       tm_dealer              c,\n");
		sbSql.append("       tm_org                 o,\n");
		sbSql.append("       tm_company             e,\n");
		sbSql.append("       addr                   f\n");
		sbSql.append(" where a.dealer_id = b.fk_dealer_id(+)\n");
		sbSql.append("   and a.parent_dealer_d = c.dealer_id(+)\n");
		sbSql.append("   and o.org_id = a.dealer_org_id\n");
		sbSql.append("   and a.company_id = e.company_id\n");
		sbSql.append("   and a.dealer_id = f.dealer_id(+)"); 
		
		if(!dealerType.equals("")) {
			sbSql.append("   and a.dealer_type = ?\n");
			params.add(dealerType);
		}
		
		if(!dealerName.equals("")){
			sbSql.append("   and a.dealer_name like ?\n");
			params.add("%"+dealerName+"%");
		}
		
		if(!dealerCode.equals("")){
			sbSql.append("   and a.dealer_code like ?\n");
			params.add("%"+dealerCode+"%");
		}
		
		if(dealerType.equals("10771001")){
			if(!addressType.equals("")){
				sbSql.append("   and f.address_type = ?\n");
				params.add(addressType);
			}else{
				sbSql.append("   and f.address_type in (20481001,20481002,20481003,20481004)\n");
			}
		}else{
			if(!addressType_sh.equals("")){
				sbSql.append("   and f.address_type = ?\n");
				params.add(addressType_sh);
			}else{
				sbSql.append("   and f.address_type in (20491001,20491002,20491003)\n");
			}
		}
		sbSql.append(" order by a.dealer_code");
		
		return dao.pageQuery(sbSql.toString(), params, getFunName(),pageSize, curPage);
	}
	
	/**
	 * 经销商地址导出查询
	 * @param map
	 * @param pageSize
	 * @param curPage
	 * @return
	 */
	public List<Map<String, Object>> queryDealerAddressInit(Map<String, Object> map) {
		String dealerType = CommonUtils.checkNull(map.get("DEALER_TYPE")); 
		String dealerCode = CommonUtils.checkNull(map.get("DEALER_CODE"));
		String dealerName = CommonUtils.checkNull(map.get("DEALER_NAME"));
		String addressType = CommonUtils.checkNull(map.get("ADDRESS_TYPE"));
		String addressType_sh = CommonUtils.checkNull(map.get("ADDRESS_TYPE_SH"));
		List<Object> params = new LinkedList<Object>();
		StringBuffer sbSql = new StringBuffer();
		
		sbSql.append("with addr as(SELECT *\n");
		sbSql.append("  FROM (SELECT dealer_id,\n");
		sbSql.append("               address,\n");
		sbSql.append("               sex,\n");
		sbSql.append("               link_man,\n");
		sbSql.append("               tel,\n");
		sbSql.append("               mobile_phone,\n");
		sbSql.append("               address_type,\n");
		sbSql.append("               status,\n");
		sbSql.append("               id\n");
		sbSql.append("          FROM tm_vs_address_more\n");
		sbSql.append("        UNION ALL\n");
		sbSql.append("        SELECT dealer_id,\n");
		sbSql.append("               (SELECT region_name\n");
		sbSql.append("                  FROM tm_region\n");
		sbSql.append("                 WHERE region_code = province_id) ||\n");
		sbSql.append("               (SELECT region_name FROM tm_region WHERE region_code = city_id) ||\n");
		sbSql.append("               (SELECT region_name FROM tm_region WHERE region_code = area_id) ||\n");
		sbSql.append("               address as address,\n");
		sbSql.append("               sex,\n");
		sbSql.append("               link_man,\n");
		sbSql.append("               tel,\n");
		sbSql.append("               mobile_phone,\n");
		sbSql.append("               address_type,\n");
		sbSql.append("               status,\n");
		sbSql.append("               id\n");
		sbSql.append("          FROM tm_vs_address\n");
		sbSql.append("        UNION ALL\n");
		sbSql.append("        SELECT dealer_id,\n");
		sbSql.append("               addr as address,\n");
		sbSql.append("               gender as sex,\n");
		sbSql.append("               linkman,\n");
		sbSql.append("               tel,\n");
		sbSql.append("               mobile_phone,\n");
		sbSql.append("               address_type,\n");
		sbSql.append("               state as status,\n");
		sbSql.append("               addr_id as id\n");
		sbSql.append("          FROM TT_PART_ADDRESS_MORE\n");
		sbSql.append("        UNION ALL\n");
		sbSql.append("        SELECT dealer_id,\n");
		sbSql.append("               addr as address,\n");
		sbSql.append("               gender as sex,\n");
		sbSql.append("               linkman,\n");
		sbSql.append("               tel,\n");
		sbSql.append("               mobile_phone,\n");
		sbSql.append("               20491001 as address_type,\n");
		sbSql.append("               state as status,\n");
		sbSql.append("               addr_id as id\n");
		sbSql.append("          FROM tt_part_addr_define)\n");
		sbSql.append(")\n");
		sbSql.append(" select a.dealer_id, a.dealer_code,\n");
		sbSql.append("       a.dealer_name,\n");
		sbSql.append("     c.dealer_code parent_dealer_code,\n");
		sbSql.append("     c.dealer_name parent_dealer_name,\n");
		sbSql.append("       o.org_name,\n");
		sbSql.append("       a.dealer_level,\n");
		sbSql.append("       (select code_desc from tc_code where code_id = a.dealer_type) as dealer_type,\n");
		sbSql.append("       e.company_name,\n");
		sbSql.append("       a.service_status,\n");
		sbSql.append("       f.address,\n");
		sbSql.append("       (select code_desc from tc_code where code_id = f.sex) as sex,\n");
		sbSql.append("       f.link_man,\n");
		sbSql.append("       f.tel,\n");
		sbSql.append("       f.mobile_phone,\n");
		sbSql.append("       (select code_desc from tc_code where code_id = f.address_type) as address_type,\n");
		sbSql.append("       (select code_desc from tc_code where code_id = f.status) as status,\n");
		sbSql.append("       f.id\n");
		sbSql.append("  from tm_dealer              a,\n");
		sbSql.append("       tm_dealer_detail       b,\n");
		sbSql.append("       tm_dealer              c,\n");
		sbSql.append("       tm_org                 o,\n");
		sbSql.append("       tm_company             e,\n");
		sbSql.append("       addr                   f\n");
		sbSql.append(" where a.dealer_id = b.fk_dealer_id(+)\n");
		sbSql.append("   and a.parent_dealer_d = c.dealer_id(+)\n");
		sbSql.append("   and o.org_id = a.dealer_org_id\n");
		sbSql.append("   and a.company_id = e.company_id\n");
		sbSql.append("   and a.dealer_id = f.dealer_id(+)"); 
		
		if(!dealerType.equals("")) {
			sbSql.append("   and a.dealer_type = ?\n");
			params.add(dealerType);
		}
		
		if(!dealerName.equals("")){
			sbSql.append("   and a.dealer_name like ?\n");
			params.add("%"+dealerName+"%");
		}
		
		if(!dealerCode.equals("")){
			sbSql.append("   and a.dealer_code like ?\n");
			params.add("%"+dealerCode+"%");
		}
		
		if(dealerType.equals("10771001")){
			if(!addressType.equals("")){
				sbSql.append("   and f.address_type = ?\n");
				params.add(addressType);
			}else{
				sbSql.append("   and f.address_type in (20481001,20481002,20481003,20481004)\n");
			}
		}else{
			if(!addressType_sh.equals("")){
				sbSql.append("   and f.address_type = ?\n");
				params.add(addressType_sh);
			}else{
				sbSql.append("   and f.address_type  in (20491001,20491002,20491003)\n");
			}
		}
		return dao.pageQuery(sbSql.toString(), params, getFunName());
	}
	/**
	 * 经销商地址信息查询
	 * @param map
	 * @param pageSize
	 * @param curPage
	 * @return
	 */
	public PageResult<Map<String, Object>> queryDealerAddressQuery(
			Map<String, Object> map, Integer pageSize, Integer curPage) {
		
		StringBuilder sbSql = new StringBuilder(100);
		sbSql.append("SELECT *\n");
		sbSql.append("  FROM (SELECT dealer_id,\n");
		sbSql.append("               address,\n");
		sbSql.append("               sex,\n");
		sbSql.append("               link_man,\n");
		sbSql.append("               tel,\n");
		sbSql.append("               mobile_phone,\n");
		sbSql.append("               address_type,\n");
		sbSql.append("               status,\n");
		sbSql.append("               id\n");
		sbSql.append("          FROM tm_vs_address_more\n");
		sbSql.append("        UNION ALL\n");
		sbSql.append("        SELECT dealer_id,\n");
		sbSql.append("               (SELECT region_name\n");
		sbSql.append("                  FROM tm_region\n");
		sbSql.append("                 WHERE region_code = province_id) ||\n");
		sbSql.append("               (SELECT region_name FROM tm_region WHERE region_code = city_id) ||\n");
		sbSql.append("               (SELECT region_name FROM tm_region WHERE region_code = area_id) ||\n");
		sbSql.append("               address as address,\n");
		sbSql.append("               sex,\n");
		sbSql.append("               link_man,\n");
		sbSql.append("               tel,\n");
		sbSql.append("               mobile_phone,\n");
		sbSql.append("               address_type,\n");
		sbSql.append("               status,\n");
		sbSql.append("               id\n");
		sbSql.append("          FROM tm_vs_address\n");
		sbSql.append("        SELECT dealer_id,\n");
		sbSql.append("               addr as address,\n");
		sbSql.append("               gender as sex,\n");
		sbSql.append("               linkman,\n");
		sbSql.append("               tel,\n");
		sbSql.append("               mobile_phone,\n");
		sbSql.append("               address_type,\n");
		sbSql.append("               state as status,\n");
		sbSql.append("               addr_id as id\n");
		sbSql.append("          FROM TT_PART_ADDRESS_MORE\n");
		sbSql.append("        UNION ALL\n");
		sbSql.append("        SELECT dealer_id,\n");
		sbSql.append("               addr as address,\n");
		sbSql.append("               gender as sex,\n");
		sbSql.append("               linkman,\n");
		sbSql.append("               tel,\n");
		sbSql.append("               mobile_phone,\n");
		sbSql.append("               20491001 as address_type,\n");
		sbSql.append("               state as status,\n");
		sbSql.append("               addr_id as id\n");
		sbSql.append("          FROM tt_part_addr_define) a\n");
		sbSql.append(" where 1 = 1\n"); 

		
		List<Object> params = new ArrayList<Object>();
		String link_man = map.get("LINK_MAN").toString();
		String tel = map.get("TEL").toString();
		String mobile_phone = map.get("MOBILE_PHONE").toString();
		String address_type = map.get("ADDRESS_TYPE").toString();
		String sex = map.get("SEX").toString();
		String dealer_id = map.get("DEALER_ID").toString();
		
		if(!"".equals(link_man)) {
			sbSql.append(" and a.link_man like '%?%'\n");
			params.add(link_man);
		}

		if(!"".equals(tel)) {
			sbSql.append(" and a.tel = ?\n");
			params.add(tel);
		}
		
		if(!"".equals(mobile_phone)) {
			sbSql.append(" and a.mobile_phone = ?\n");
			params.add(mobile_phone);
		}
		
		if(!"".equals(address_type)) {
			sbSql.append(" and a.address_type = ?\n");
			params.add(address_type);
		}
		
		if(!"".equals(sex)) {
			sbSql.append(" and a.sex = ?\n");
			params.add(sex);
		}
		
		if(!"".equals(dealer_id)) {
			sbSql.append(" and a.dealer_id = ?\n");
			params.add(dealer_id);
		}
		
		return dao.pageQuery(sbSql.toString(), params, getFunName(),pageSize, curPage);
	}
	
	/**
	 * 经销商地址信息查询byid
	 * @param id
	 */
	public List<Map<String, String>> queryDealerAddressQueryById(String id) {
		StringBuilder sbSql = new StringBuilder(100);
		sbSql.append("SELECT a.*, c.company_name\n");
		sbSql.append("  FROM (SELECT dealer_id,\n");
		sbSql.append("               address,\n");
		sbSql.append("               sex,\n");
		sbSql.append("               link_man,\n");
		sbSql.append("               tel,\n");
		sbSql.append("               mobile_phone,\n");
		sbSql.append("               address_type,\n");
		sbSql.append("               status,\n");
		sbSql.append("               id\n");
		sbSql.append("          FROM tm_vs_address_more\n");
		sbSql.append("        UNION ALL\n");
		sbSql.append("        SELECT dealer_id,\n");
		sbSql.append("               (SELECT region_name\n");
		sbSql.append("                  FROM tm_region\n");
		sbSql.append("                 WHERE region_code = province_id) ||\n");
		sbSql.append("               (SELECT region_name FROM tm_region WHERE region_code = city_id) ||\n");
		sbSql.append("               (SELECT region_name FROM tm_region WHERE region_code = area_id) ||\n");
		sbSql.append("               address as address,\n");
		sbSql.append("               sex,\n");
		sbSql.append("               link_man,\n");
		sbSql.append("               tel,\n");
		sbSql.append("               mobile_phone,\n");
		sbSql.append("               address_type,\n");
		sbSql.append("               status,\n");
		sbSql.append("               id\n");
		sbSql.append("          FROM tm_vs_address\n");
		sbSql.append("        UNION ALL\n");
		sbSql.append("        SELECT dealer_id,\n");
		sbSql.append("               addr as address,\n");
		sbSql.append("               gender as sex,\n");
		sbSql.append("               linkman,\n");
		sbSql.append("               tel,\n");
		sbSql.append("               mobile_phone,\n");
		sbSql.append("               address_type,\n");
		sbSql.append("               state as status,\n");
		sbSql.append("               addr_id as id\n");
		sbSql.append("          FROM TT_PART_ADDRESS_MORE\n");
		sbSql.append("        UNION ALL\n");
		sbSql.append("        SELECT dealer_id,\n");
		sbSql.append("               addr as address,\n");
		sbSql.append("               gender as sex,\n");
		sbSql.append("               linkman,\n");
		sbSql.append("               tel,\n");
		sbSql.append("               mobile_phone,\n");
		sbSql.append("               20491001 as address_type,\n");
		sbSql.append("               state as status,\n");
		sbSql.append("               addr_id as id\n");
		sbSql.append("          FROM tt_part_addr_define) a,\n");
		sbSql.append("          tm_dealer              b,\n");
		sbSql.append("          tm_company             c\n");
		sbSql.append(" where\n");
		sbSql.append(" a.dealer_id = b.dealer_id\n");
		sbSql.append(" and b.company_id = c.company_id\n");
		sbSql.append(" and a.id = "+ id +"\n"); 

		return dao.pageQuery01(sbSql.toString(), null, dao.getFunName());
	}
	
	public List<Map<String, Object>> queryServiceDealerInfoForXLS(Map<String, Object> map) throws Exception{
		
		String dealerType = CommonUtils.checkNull(map.get("dealerType")); 
		String dealerCode = CommonUtils.checkNull(map.get("dealerCode"));
		String dealerName = CommonUtils.checkNull(map.get("dealerName"));
		String dealerLevel = CommonUtils.checkNull(map.get("dealerLevel"));
		String dealerStatus = CommonUtils.checkNull(map.get("dealerStatus"));
		String parentOrgCode = CommonUtils.checkNull(map.get("parentOrgCode"));
		String sJDealerCode = CommonUtils.checkNull(map.get("sJDealerCode"));
		String companyName = CommonUtils.checkNull(map.get("companyName"));
		String serviceStatus = CommonUtils.checkNull(map.get("serviceStatus"));
		String workType = CommonUtils.checkNull(map.get("workType"));
		
		String regionId = CommonUtils.checkNull(map.get("regionId"));
		String AUTHORIZATION_TYPE = CommonUtils.checkNull(map.get("AUTHORIZATION_TYPE"));
		String orgCode = CommonUtils.checkNull(map.get("orgCode"));
		String SCREATE_DATE = CommonUtils.checkNull(map.get("SCREATE_DATE"));
		String ECREATE_DATE = CommonUtils.checkNull(map.get("ECREATE_DATE"));
		String orgId = CommonUtils.checkNull(map.get("orgId"));
		
		String IMAGE_COMFIRM_LEVEL = CommonUtils.checkNull(map.get("IMAGE_COMFIRM_LEVEL"));
		
		String AUTHORIZATION_SCREATE_DATE = CommonUtils.checkNull(map.get("AUTHORIZATION_SCREATE_DATE"));
		String AUTHORIZATION_ECREATE_DATE = CommonUtils.checkNull(map.get("AUTHORIZATION_ECREATE_DATE"));
		
		List<Object> params = new LinkedList<Object>();
		StringBuffer sbSql = new StringBuffer();
		
		sbSql.append("select a.*,a.LEGAL_EMAIL as LEGAL_EMAIL_A,(SELECT CODE_DESC FROM TC_CODE WHERE CODE_ID = a.SERVICE_STATUS) AS SERVICE_STATUS_NAME, \n");
		sbSql.append("		(SELECT REGION_NAME FROM tm_region WHERE REGION_CODE = a.COUNTIES) AS COUNTIES_NAME,\n");
		sbSql.append("		(SELECT CODE_DESC FROM TC_CODE WHERE CODE_ID = a.DEALER_LEVEL) AS DEALER_LEVEL_NAME,\n");
		sbSql.append("		(SELECT CODE_DESC FROM TC_CODE WHERE CODE_ID = b.IS_ACTING_BRAND) AS IS_ACTING_BRAND_NAME,\n");
		sbSql.append("		(SELECT CODE_DESC FROM TC_CODE WHERE CODE_ID = b.IS_AUTHORIZE_CITY) AS IS_AUTHORIZE_CITY_NAME,\n");
		sbSql.append("		(SELECT CODE_DESC FROM TC_CODE WHERE CODE_ID = b.IS_AUTHORIZE_COUNTY) AS IS_AUTHORIZE_COUNTY_NAME,\n");
		sbSql.append("		(SELECT CODE_DESC FROM TC_CODE WHERE CODE_ID = A.MAIN_RESOURCES) AS MAIN_RESOURCES_NAME,\n"); 
		sbSql.append("		(SELECT CODE_DESC FROM TC_CODE WHERE CODE_ID = A.BALANCE_LEVEL) AS BALANCE_LEVEL_NAME,\n");
		sbSql.append("		(SELECT CODE_DESC FROM TC_CODE WHERE CODE_ID = A.INVOICE_LEVEL) AS INVOICE_LEVEL_NAME,\n");
		sbSql.append("		(SELECT CODE_DESC FROM TC_CODE WHERE CODE_ID = b.IMAGE_LEVEL) AS IMAGE_LEVEL_NAME,\n");
		sbSql.append("		(SELECT CODE_DESC FROM TC_CODE WHERE CODE_ID = a.TAX_INVOICE) AS TAX_INVOICE_NAME,\n");
		sbSql.append("		(SELECT CODE_DESC FROM TC_CODE WHERE CODE_ID = b.IMAGE_COMFIRM_LEVEL) AS IMAGE_COMFIRM_LEVEL_name,\n");
		sbSql.append("		(select dealer_name from tm_dealer where dealer_id = a.parent_dealer_d) as PARENT_DEALER_NAME,\n");
		
		sbSql.append("		 b.*,b.FAX_NO AS FAX_NO_B,b.EMAIL AS EMAIL_B,b.WORK_TYPE AS WORK_TYPE_B,b.MARKET_NAME AS MARKET_NAME_B,"
				+ "b.WEBMASTER_NAME AS WEBMASTER_NAME_B,b.WEBMASTER_PHONE AS WEBMASTER_PHONE_B,b.WEBMASTER_TELPHONE AS WEBMASTER_TELPHONE_B,"
				+ "b.WEBMASTER_EMAIL AS WEBMASTER_EMAIL_B,\n");
		sbSql.append("		 c.*,\n");
		sbSql.append("		 f.*,\n");
		sbSql.append("       o.*,\n");
		sbSql.append("       e.*,\n");
		sbSql.append("       g.*\n");
		sbSql.append("  from tm_dealer              a,\n");
		sbSql.append("       tm_dealer_detail       b,\n");
		sbSql.append("       tm_dealer              c,\n");
		sbSql.append("       tm_org                 o,\n");
		sbSql.append("       tm_company             e,\n");
		sbSql.append("       tm_region              f,\n");
		sbSql.append("       vw_org_dealer_service   g\n");
		sbSql.append(" where a.dealer_id = b.fk_dealer_id(+)\n");
		sbSql.append("   and a.dealer_id = g.dealer_id(+)\n");
		sbSql.append("   and a.parent_dealer_d = c.dealer_id(+)\n");
		sbSql.append("   and o.org_id = a.dealer_org_id\n");
		sbSql.append("   and a.company_id = e.company_id\n");
		sbSql.append("   and a.province_id = f.region_code\n");  
		
		if(!dealerType.equals("")) {
			sbSql.append("   and a.dealer_type = ?\n");
			params.add(dealerType);
		}
		
		if(!dealerName.equals("")){
			sbSql.append("   and a.dealer_name like ?\n");
			params.add("%"+dealerName+"%");
		}
		
		if(!dealerCode.equals("")){
			sbSql.append("   and a.dealer_code like ?\n");
			params.add("%"+dealerCode+"%");
		}
        if (!"".equals(AUTHORIZATION_SCREATE_DATE)) {
        	sbSql.append(" and AUTHORIZATION_DATE>= to_date('").append(AUTHORIZATION_SCREATE_DATE).append(" 00:00:00','YYYY/MM/dd HH24:mi:ss')");
        	sbSql.append(" and AUTHORIZATION_DATE<= to_date('").append(AUTHORIZATION_ECREATE_DATE).append(" 23:59:59','YYYY/MM/dd HH24:mi:ss')");
        }
		if(!dealerLevel.equals("")) {
			sbSql.append("	 and a.dealer_level = ?\n");
			params.add(dealerLevel);
		}
		
		if(!dealerStatus.equals("")) {
			sbSql.append(" 	 and a.status = ?\n");
			params.add(dealerStatus);
		}
		
		if(!IMAGE_COMFIRM_LEVEL.equals("")) {
			sbSql.append(" 	 and b.IMAGE_COMFIRM_LEVEL = ?\n");
			params.add(IMAGE_COMFIRM_LEVEL);
		}
		if(!parentOrgCode.equals("")) {
			sbSql.append(" 	 and o.parent_org_id in (select org_id from tm_org where org_code = ?)\n");
			params.add(parentOrgCode);
		}

		if(!sJDealerCode.equals("")) {
			sbSql.append(" 	 and a.parent_dealer_d in (select dealer_id from tm_dealer where dealer_code = ?)\n");
			params.add(sJDealerCode);
		}
		
		if(!companyName.equals("")) {
			sbSql.append(" 	 and e.company_shortname = ?\n");
			params.add(companyName);
		}
		
		if(!serviceStatus.equals("")) {
			sbSql.append("   and a.service_status = ?\n");
			params.add(serviceStatus);
		}
		
		if(dealerType.equals("10771001")){
			if(!workType.equals("")) {
				sbSql.append("   and b.SHOP_TYPE = ?\n");
				params.add(workType);
			}
		}else{
			if(!workType.equals("")) {
				sbSql.append("   and b.WORK_TYPE = ?\n");
				params.add(workType);
			}
		}
		
		if(!AUTHORIZATION_TYPE.equals("")) {
			sbSql.append("   and b.AUTHORIZATION_TYPE = ?\n");
			params.add(AUTHORIZATION_TYPE);
		}
		if (!CommonUtils.isNullString(regionId)) {
			sbSql.append("   and g.org_id in (").append(regionId).append(")\n") ;
		}
		if (!CommonUtils.isNullString(orgCode)) {
			sbSql.append("   and o.org_Code = '").append(orgCode).append("'\n") ;
		}
		if (!CommonUtils.isNullString(orgId)) {
			sbSql.append("   and g.ROOT_ORG_ID = '").append(orgId).append("'\n") ;
		}
		sbSql.append("   order by g.ROOT_ORG_ID");
		return dao.pageQuery(sbSql.toString(), params, getFunName());
	}
	
public List<Map<String, Object>> queryServiceDealerInfoForXLS2nd(Map<String, Object> map) throws Exception{
		
		String dealerType = CommonUtils.checkNull(map.get("dealerType")); 
		String dealerCode = CommonUtils.checkNull(map.get("dealerCode"));
		String dealerName = CommonUtils.checkNull(map.get("dealerName"));
		String dealerLevel = CommonUtils.checkNull(map.get("dealerLevel"));
		String dealerStatus = CommonUtils.checkNull(map.get("dealerStatus"));
		String parentOrgCode = CommonUtils.checkNull(map.get("parentOrgCode"));
		String sJDealerCode = CommonUtils.checkNull(map.get("sJDealerCode"));
		String companyName = CommonUtils.checkNull(map.get("companyName"));
		String serviceStatus = CommonUtils.checkNull(map.get("serviceStatus"));
		String workType = CommonUtils.checkNull(map.get("workType"));
		
		String regionId = CommonUtils.checkNull(map.get("regionId"));
		String AUTHORIZATION_TYPE = CommonUtils.checkNull(map.get("AUTHORIZATION_TYPE"));
		String orgCode = CommonUtils.checkNull(map.get("regionCode"));
		String dealer_id = CommonUtils.checkNull(map.get("DEALER_ID"));
		String status = CommonUtils.checkNull(map.get("status"));
		String orgId = CommonUtils.checkNull(map.get("orgId"));
		
		List<Object> params = new LinkedList<Object>();
		StringBuffer sbSql = new StringBuffer();
		
		sbSql.append("select (select code_desc from tc_code where code_id = a.secend_autid_status) as secend_autid_status_name, a.*,a.LEGAL_EMAIL as LEGAL_EMAIL_A,(SELECT CODE_DESC FROM TC_CODE WHERE CODE_ID = a.SERVICE_STATUS) AS SERVICE_STATUS_NAME, \n");
		sbSql.append("		(SELECT REGION_NAME FROM tm_region WHERE REGION_CODE = a.COUNTIES) AS COUNTIES_NAME,\n");
		sbSql.append("		(SELECT CODE_DESC FROM TC_CODE WHERE CODE_ID = a.DEALER_LEVEL) AS DEALER_LEVEL_NAME,\n");
		sbSql.append("		(SELECT CODE_DESC FROM TC_CODE WHERE CODE_ID = b.IS_ACTING_BRAND) AS IS_ACTING_BRAND_NAME,\n");
		sbSql.append("		(SELECT CODE_DESC FROM TC_CODE WHERE CODE_ID = b.IS_AUTHORIZE_CITY) AS IS_AUTHORIZE_CITY_NAME,\n");
		sbSql.append("		(SELECT CODE_DESC FROM TC_CODE WHERE CODE_ID = b.IS_AUTHORIZE_COUNTY) AS IS_AUTHORIZE_COUNTY_NAME,\n");
		sbSql.append("		(SELECT CODE_DESC FROM TC_CODE WHERE CODE_ID = A.MAIN_RESOURCES) AS MAIN_RESOURCES_NAME,\n"); 
		sbSql.append("		(SELECT CODE_DESC FROM TC_CODE WHERE CODE_ID = A.BALANCE_LEVEL) AS BALANCE_LEVEL_NAME,\n");
		sbSql.append("		(SELECT CODE_DESC FROM TC_CODE WHERE CODE_ID = A.INVOICE_LEVEL) AS INVOICE_LEVEL_NAME,\n");
		sbSql.append("		(SELECT CODE_DESC FROM TC_CODE WHERE CODE_ID = b.IMAGE_LEVEL) AS IMAGE_LEVEL_NAME,\n");
		sbSql.append("		(SELECT CODE_DESC FROM TC_CODE WHERE CODE_ID = a.TAX_INVOICE) AS TAX_INVOICE_NAME,\n");
		sbSql.append("		(select dealer_name from tm_dealer where dealer_id = a.parent_dealer_d) as PARENT_DEALER_NAME,\n");
		
		sbSql.append("		 b.*,b.FAX_NO AS FAX_NO_B,b.EMAIL AS EMAIL_B,b.WORK_TYPE AS WORK_TYPE_B,b.MARKET_NAME AS MARKET_NAME_B,"
				+ "b.WEBMASTER_NAME AS WEBMASTER_NAME_B,b.WEBMASTER_PHONE AS WEBMASTER_PHONE_B,b.WEBMASTER_TELPHONE AS WEBMASTER_TELPHONE_B,"
				+ "b.WEBMASTER_EMAIL AS WEBMASTER_EMAIL_B,\n");
		sbSql.append("		 c.*,\n");
		sbSql.append("		 c.dealer_name as up_dealer_Name,\n");
		sbSql.append("		 c.dealer_code as up_dealer_code,\n");
		
		sbSql.append("       (SELECT CODE_DESC FROM TC_CODE WHERE CODE_ID=tdsl.SECOND_LEVEL_NETWORK_NATURE) SECOND_LEVEL_NETWORK_NATURE,\n");
		sbSql.append("       tdsl.COMPETING_BRAND,\n");
		sbSql.append("       tdsl.AND_COMPETING_RUN_DISTANCE,\n");
		sbSql.append("       tdsl.MONTH_AVERAGE_SALES,\n");
		sbSql.append("       tdsl.MARKET_OCCUPANCY,\n");
		sbSql.append("       tdsl.DOORHEAD_LENGTH,\n");
		sbSql.append("       (SELECT CODE_DESC FROM TC_CODE WHERE CODE_ID=tdsl.IS_HAVE_SALES_DOORHEAD) IS_HAVE_SALES_DOORHEAD,\n");
		sbSql.append("       (SELECT CODE_DESC FROM TC_CODE WHERE CODE_ID=tdsl.IS_HAVE_SALES_IMAGE_WALL) IS_HAVE_SALES_IMAGE_WALL,\n");
		sbSql.append("       tdsl.AGENT_ZONE_POPULATION_COUNT,\n");
		sbSql.append("       tdsl.SALES_CONSULTANT_COUNT,\n");
		sbSql.append("       (SELECT CODE_DESC FROM TC_CODE WHERE CODE_ID=tdsl.SERVICE_NETWORK_NATURE) SERVICE_NETWORK_NATURE,\n");
		sbSql.append("       (SELECT CODE_DESC FROM TC_CODE WHERE CODE_ID=tdsl.REPAIR_APTITUDE) REPAIR_APTITUDE,\n");
		sbSql.append("       tdsl.SERVICE_WORKSHOP_AREA,\n");
		sbSql.append("       (SELECT CODE_DESC FROM TC_CODE WHERE CODE_ID=tdsl.IS_HAVE_SERVICE_DOORHEAD) IS_HAVE_SERVICE_DOORHEAD,\n");
		sbSql.append("       (SELECT CODE_DESC FROM TC_CODE WHERE CODE_ID=tdsl.IS_HAVE_SERVICE_IMAGE_WALL) IS_HAVE_SERVICE_IMAGE_WALL,\n");
		sbSql.append("       tdsl.SERVICE_SALES_NETWORK_DISTANCE,\n");
		sbSql.append("       tdsl.REPAIR_ENGINEER_LOWEST_DEPLOY,\n");
		sbSql.append("       (SELECT CODE_DESC FROM TC_CODE WHERE CODE_ID=tdsl.LEVEL_REPORT) LEVEL_REPORT,\n");
		
		sbSql.append("       (select dealer_name from tm_dealer where dealer_id = a.parent_dealer_d) as PARENT_DEALER_NAME, \n");
//		sbSql.append("		 f.*,\n");
//		sbSql.append("       o.*,\n");
		sbSql.append("       e.*,\n");
		sbSql.append("       g.*\n");
		sbSql.append("  from tm_dealer              a,\n");
		sbSql.append("       tm_dealer_detail       b,\n");
		sbSql.append("       tm_dealer              c,\n");
//		sbSql.append("       tm_org                 o,\n");
		sbSql.append("       tm_company             e,\n");
//		sbSql.append("       tt_dealer_secend_audit              f,\n");
		sbSql.append("       vw_org_dealer_service   g, \n");
		sbSql.append("       TM_DEALER_SECOND_LEVEL tdsl \n");
		sbSql.append(" where a.dealer_id = b.fk_dealer_id(+)\n");
		sbSql.append("   and a.dealer_id = g.dealer_id(+)\n");
		sbSql.append("   and a.parent_dealer_d = c.dealer_id(+)\n");
//		sbSql.append("   and a.dealer_org_id = g.org_id(+)\n");
		sbSql.append("   and a.company_id = e.company_id(+)\n");
		sbSql.append("   and a.dealer_id = tdsl.fk_dealer_id(+)\n");
//		sbSql.append("   and a.dealer_id = f.dealer_id(+)\n");  
		sbSql.append("   and a.dealer_level = 10851002\n");  
		
		if(!dealerType.equals("")) {
			sbSql.append("   and a.dealer_type = ?\n");
			params.add(dealerType);
		}
		if(!orgId.equals("")) {
			sbSql.append("   and g.root_org_id = ?\n");
			params.add(orgId);
		}
		if(!status.equals("")) {
			sbSql.append("   and a.SECEND_AUTID_STATUS = ?\n");
			params.add(status);
		}
		if(!dealerName.equals("")){
			sbSql.append("   and a.dealer_name like ?\n");
			params.add("%"+dealerName+"%");
		}
		
		if(!dealerCode.equals("")){
			sbSql.append("   and a.dealer_code like ?\n");
			params.add("%"+dealerCode+"%");
		}
		
		if(!dealer_id.equals("")) {
			sbSql.append(" and a.PARENT_DEALER_D =?\n");
			params.add(dealer_id);
		}
		if(!dealerLevel.equals("")) {
			sbSql.append("	 and a.dealer_level = ?\n");
			params.add(dealerLevel);
		}
		
		if(!dealerStatus.equals("")) {
			sbSql.append(" 	 and a.status = ?\n");
			params.add(dealerStatus);
		}

		if(!parentOrgCode.equals("")) {
			sbSql.append(" 	 and o.parent_org_id in (select org_id from tm_org where org_code = ?)\n");
			params.add(parentOrgCode);
		}

		if(!sJDealerCode.equals("")) {
			sbSql.append(" 	 and a.parent_dealer_d in (select dealer_id from tm_dealer where dealer_code = ?)\n");
			params.add(sJDealerCode);
		}
		
		if(!companyName.equals("")) {
			sbSql.append(" 	 and e.company_shortname = ?\n");
			params.add(companyName);
		}
		
		if(!serviceStatus.equals("")) {
			sbSql.append("   and a.service_status = ?\n");
			params.add(serviceStatus);
		}
		
		if(!workType.equals("")) {
			sbSql.append("   and b.work_Type = ?\n");
			params.add(workType);
		}
		
		if(!AUTHORIZATION_TYPE.equals("")) {
			sbSql.append("   and b.AUTHORIZATION_TYPE = ?\n");
			params.add(AUTHORIZATION_TYPE);
		}
//		if (!CommonUtils.isNullString(regionId)) {
//			sbSql.append("   and f.region_id in (").append(regionId).append(")\n") ;
//		}
		if (!CommonUtils.isNullString(orgCode)) {
			sbSql.append("   and g.org_Code = '").append(orgCode).append("'\n") ;
		}
		return dao.pageQuery(sbSql.toString(), params, getFunName());
	}
	
	public  List<Map<String, Object>> getConstructInfo(String dealerId, String suvOrMpv)throws Exception {
		List<Object> params = new LinkedList<Object>();
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT t1.dealer_id,\n"); 
		sql.append("	   t1.construct_sdate,\n");
		sql.append("	   t1.accept_date,\n");
		sql.append("	   t1.construct_edate,\n");
		sql.append("	   t1.image_level,\n");
		sql.append("	   t1.id,\n");
		sql.append("	   t1.checked_image_level,\n");
		sql.append("	   t1.checked_date,\n");
		sql.append("	   t1.CHECKED_TYPE,\n");
		sql.append("	   t1.OFFLINE_REBATE,\n");
		sql.append("	   t1.VEHICLE_SERIES_ID,\n");
		sql.append("	   CASE WHEN T4.DEPLOY_ID IS NULL THEN '' \n");
		sql.append("	   ELSE T4.DEPLOY_ID||'_'||t4.amount \n");
		sql.append("	   END  AS DEPLOY_ID, \n");
		sql.append("       t4.amount,\n");
		sql.append("       t4.support_sdate,\n"); 
		sql.append("       t4.support_edate,\n"); 
		sql.append("       t4.year_rent,\n"); 
		sql.append("       t4.scale,\n"); 
		sql.append("       t4.create_by,\n"); 
		sql.append("       t4.remark,\n"); 
		sql.append("       t4.create_date,\n"); 
		sql.append("       t4.status,\n"); 
		sql.append("       decode(t4.status,'92861000','已保存','92861001','销售部审核中','92861002','审核通过','92861003','驳回','92861004','终止','') as H_STATE,\n");
		sql.append("       t4.year_flag,\n"); 
		sql.append("       t4.detail_id,\n"); 
		sql.append("       t4.support_number,\n"); 
		sql.append("       trunc(nvl(t4.year_rent*(100-t4.scale)/100/t4.amount,0)) maxAmount, \n");
		sql.append("       t3.group_name\n"); 
		sql.append("  FROM tt_vi_construct_main   t1,\n"); 
		sql.append("       tt_vi_construct_detail T4, \n");
		sql.append("       tm_vhcl_material_group T3 \n");
		sql.append(" WHERE t1.ID = t4.ID(+)\n"); 
		sql.append("   AND t1.VEHICLE_SERIES_ID = t3.GROUP_ID(+)\n"); 
		sql.append("   and t1.DEALER_ID = ?\n"); 
		params.add(dealerId);
		if (!StringUtils.isEmpty(suvOrMpv)) {
			sql.append("   AND T3.GROUP_ID=? \n");
			params.add(suvOrMpv);
		}
		sql.append(" order by t1.DEALER_ID, t4.year_flag, t1.VEHICLE_SERIES_ID\n"); 

		return  pageQuery(sql.toString(),params,getFunName());
	}
	
public PageResult<Map<String, Object>> viConstructInfoQuery1(Map<String, Object> map,int curPage,int pageSize) throws Exception{
		
		StringBuffer sbSql = new StringBuffer();
		String user_id = CommonUtils.checkNull(map.get("user_id"));
//		Long par_user_id = new Long(user_id)-1;
		String dealer_id = CommonUtils.checkNull(map.get("dealer_id"));
		sbSql.append("SELECT t1.CONSTRUCT_SDATE,\n");
		sbSql.append("       t1.CONSTRUCT_EDATE,\n");
		sbSql.append("       t1.checked_type,\n");
		sbSql.append("       t1.ACCEPT_DATE,\n");
		sbSql.append("       t2.YEAR_RENT,\n");
		sbSql.append("       t2.SCALE,\n");
		sbSql.append("       t2.DETAIL_ID,\n");
		sbSql.append("       t2.SUPPORT_SDATE,\n");
		sbSql.append("       t2.SUPPORT_EDATE,\n");
		sbSql.append("       t2.AMOUNT,\n");
//		sbSql.append("       t3.STATUS,\n");
		sbSql.append("       decode(t2.status,'92861000','已保存','92861001','销售部审核中','92861002','审核通过','92861003','驳回','92861004','终止','') as H_STATE,\n");
		sbSql.append("		 t2.status audit_status, \n");
		sbSql.append("       t2.USER_ID,\n");
		sbSql.append("       (SELECT AA.remark FROM (select t.remark,t.detail_id from tt_vi_construct_Audit t where 1=1 ORDER BY t.id DESC ) AA WHERE AA.detail_id = t2.detail_id AND ROWNUM=1) remark,\n");
		sbSql.append("       t2.VIOVER_DATE,\n");
		sbSql.append("       t4.DEALER_ID,\n");
		sbSql.append("       t4.DEALER_CODE,\n");
		sbSql.append("       t4.DEALER_NAME,\n");
		sbSql.append("       '第'||t2.YEAR_FLAG||'年' as YEAR_FLAG,\n");
		sbSql.append("       t2.YEAR_FLAG as YEAR_FLAG_num,\n");
		sbSql.append("       t4.DEALER_SHORTNAME,\n");
		sbSql.append("       t5.GROUP_ID,\n");
		sbSql.append("       t5.GROUP_NAME\n");
		sbSql.append("  FROM tt_vi_construct_main   t1,\n");
		sbSql.append("      tt_vi_construct_detail t2,\n");
//		sbSql.append("      tt_vi_construct_Audit  t3,\n");
		sbSql.append("       tm_dealer  t4,\n");
		sbSql.append("       TM_VHCL_MATERIAL_GROUP  t5\n");
		sbSql.append(" WHERE t1.ID = t2.ID\n");
		sbSql.append("   and t1.DEALER_ID = t4.DEALER_ID\n");
		sbSql.append("   and t1.VEHICLE_SERIES_ID = t5.GROUP_ID \n");
		sbSql.append("   and t1.dealer_id = "+dealer_id+"\n");
		sbSql.append(" order by t1.DEALER_ID, t2.year_flag\n");

		return dao.pageQuery(sbSql.toString(), null, getFunName(),pageSize, curPage);
	}

public String getSecendDealerNum(String DealerCode) throws Exception{
	
		StringBuffer sbSql = new StringBuffer();
		
		sbSql.append("select * from tm_dealer where dealer_Code like '%"+DealerCode+"%' and dealer_level = 10851002 AND DEALER_TYPE = 10771001\n");
		List<Map<String, Object>> list = new LinkedList<Map<String,Object>>();
	    list = dao.pageQuery(sbSql.toString(), null, getFunName());
	    if(list.size()>8){
	    	return String.valueOf(list.size()+1);
	    }else{
	    	return "0"+String.valueOf(list.size()+1);
	    }
	}

	public List<Map<String, Object>> getAttachInfo(String ywzj) throws Exception {

		String sql = "SELECT F.FJID,F.FILEURL,F.FILENAME FROM FS_FILEUPLOAD F WHERE F.YWZJ =" + ywzj + "ORDER BY F.FJID";

		List<Map<String, Object>> list = pageQuery(sql, null, getFunName());
		return list;
	}

	public List<Map<String, Object>> getSecendDealerNum1(String mainId) {
		if (mainId == null || mainId.equals("")) {
			return null;
		}
		String sql = "SELECT F.FJID,F.FILEURL,F.FILENAME FROM FS_FILEUPLOAD F WHERE F.YWZJ =" + mainId + "ORDER BY F.FJID";

		List<Map<String, Object>> list = pageQuery(sql, null, getFunName());
		return list;

	}
	
	public PageResult<Map<String, Object>> queryServiceDealerChangeInfo(Map<String, Object> map,int curPage,int pageSize) throws Exception{
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		String dealerType = CommonUtils.checkNull(map.get("DEALER_TYPE")); 
		String dealerCode = CommonUtils.checkNull(map.get("DEALER_CODE"));
		String dealerName = CommonUtils.checkNull(map.get("DEALER_NAME"));
		String dealerLevel = CommonUtils.checkNull(map.get("DEALER_LEVEL"));
		String dealerStatus = CommonUtils.checkNull(map.get("DEALER_STATUS"));
		String parentOrgCode = CommonUtils.checkNull(map.get("parentOrgCode"));
		String sJDealerCode = CommonUtils.checkNull(map.get("sJDealerCode"));
		String companyName = CommonUtils.checkNull(map.get("COMPANY_NAME"));
		String serviceStatus = CommonUtils.checkNull(map.get("SERVICE_STATUS"));
		
		String regionId = CommonUtils.checkNull(map.get("regionId"));
		String AUTHORIZATION_TYPE = CommonUtils.checkNull(map.get("AUTHORIZATION_TYPE"));
		String orgCode = CommonUtils.checkNull(map.get("orgCode"));
		String orgId = CommonUtils.checkNull(map.get("orgId"));
		
		String isFac = CommonUtils.checkNull(map.get("isFac"));
		String isQuery = CommonUtils.checkNull(map.get("isQuery"));
		List<Object> params = new LinkedList<Object>();
		StringBuffer sbSql = new StringBuffer();
		
		sbSql.append("select distinct a.dealer_id,a.id, a.dealer_code,\n");
		sbSql.append("       a.dealer_name,\n");
//		sbSql.append("       a.CREATE_DATE,\n");
		sbSql.append("       a.CHANGE_AUTID_STATUS,\n");
		sbSql.append("       a.status,\n");
		sbSql.append("		 c.dealer_code parent_dealer_code,\n");
		sbSql.append("		 c.dealer_name parent_dealer_name,\n"); 
		sbSql.append("       o.org_name,\n");
		sbSql.append("       a.dealer_level,\n");
		sbSql.append("       a.DEALER_SHORTNAME,\n");
		sbSql.append("       e.company_name,\n");
		sbSql.append("       a.service_status,\n");
		sbSql.append("       g.ROOT_ORG_ID,\n");
		sbSql.append("       g.ROOT_ORG_NAME,\n");
		sbSql.append("       f.REGION_NAME,\n");
		sbSql.append("       b.AUTHORIZATION_TYPE\n");
		sbSql.append("  from tmp_tm_dealer              a,\n");
		sbSql.append("       tmp_tm_dealer_detail       b,\n");
		sbSql.append("       tm_dealer              c,\n");
		sbSql.append("       tm_org                 o,\n");
		sbSql.append("       tm_company             e,\n");
		sbSql.append("       tm_region             f,\n");
		sbSql.append("       vw_org_dealer_service            g\n");
		sbSql.append(" where a.dealer_id = b.fk_dealer_id(+)\n");
		sbSql.append("   and a.dealer_id = g.dealer_id\n");
		sbSql.append("   and a.parent_dealer_d = c.dealer_id(+)\n");
		sbSql.append("   and a.dealer_org_id = o.org_id(+)\n");
		sbSql.append("   and a.company_id = e.company_id\n");
		sbSql.append("   and a.province_id = f.region_code\n");
		
		if(!isQuery.equals("yes")) {
			sbSql.append("   and a.delete_flag !=1\n");
		}
		
		if(!isFac.equals("")) {
			if(isFac.equals("yes")){
				sbSql.append("   and a.CHANGE_AUTID_STATUS = 20051002\n");
			}else{
				sbSql.append("   and a.CHANGE_AUTID_STATUS = 20051001\n");
			}
		}
		if(!dealerType.equals("")) {
			sbSql.append("   and a.dealer_type = 10771002\n");
		}
		
		if(!dealerName.equals("")){
			sbSql.append("   and a.dealer_name like ?\n");
			params.add("%"+dealerName+"%");
		}
		
		if(!dealerCode.equals("")){
			sbSql.append("   and a.dealer_code like ?\n");
			params.add("%"+dealerCode+"%");
		}
		
		if(!dealerLevel.equals("")) {
			sbSql.append("	 and a.dealer_level = ?\n");
			params.add(dealerLevel);
		}
		
		if(!dealerStatus.equals("")) {
			sbSql.append(" 	 and a.status = ?\n");
			params.add(dealerStatus);
		}

		if(!parentOrgCode.equals("")) {
			sbSql.append(" 	 and o.org_id in (select org_id from tm_org where org_code = ?)\n");
			params.add(parentOrgCode);
		}

		if(!sJDealerCode.equals("")) {
			sbSql.append(" 	 and a.parent_dealer_d in (select dealer_id from tm_dealer where dealer_code = ?)\n");
			params.add(sJDealerCode);
		}
		
		if(!companyName.equals("")) {
			sbSql.append(" 	 and e.company_shortname = ?\n");
			params.add(companyName);
		}
		
		if(!serviceStatus.equals("")) {
			sbSql.append("   and a.service_status = ?\n");
			params.add(serviceStatus);
		}
		if(!StringUtil.isNull(logonUser.getDealerId())) {
			sbSql.append("   and a.DEALER_ID = ?\n");
			params.add(logonUser.getDealerId());
		}else{
			sbSql.append(MaterialGroupManagerDao.getOrgDealerLimitSqlForService("a.DEALER_ID", logonUser));
		}
		if(!AUTHORIZATION_TYPE.equals("")) {
			sbSql.append("   and b.AUTHORIZATION_TYPE = ?\n");
			params.add(AUTHORIZATION_TYPE);
		}
		if (!CommonUtils.isNullString(regionId)) {
			sbSql.append("   and g.org_id in (").append(regionId).append(")\n") ;
		}
		if (!CommonUtils.isNullString(orgCode)) {
			sbSql.append("   and o.org_Code = '").append(orgCode).append("'\n") ;
		}
		if(!orgId.equals("")) {
			sbSql.append("   AND g.ROOT_org_id=").append(orgId);
		}
//		sbSql.append("   AND a.delete_flag!=1");
		sbSql.append("  order by g.ROOT_ORG_ID");
		return dao.pageQuery(sbSql.toString(), params, getFunName(),pageSize, curPage);
	}
	
public PageResult<Map<String, Object>> queryDealerInfoForDealer(Map<String, Object> map,int curPage,int pageSize) throws Exception{
		
		String dealerType = CommonUtils.checkNull(map.get("DEALER_TYPE")); 
		String dealerCode = CommonUtils.checkNull(map.get("DEALER_CODE"));
		String dealerName = CommonUtils.checkNull(map.get("DEALER_NAME"));
		String dealerLevel = CommonUtils.checkNull(map.get("DEALER_LEVEL"));
		String dealerStatus = CommonUtils.checkNull(map.get("DEALER_STATUS"));
		String sJDealerCode = CommonUtils.checkNull(map.get("sJDealerCode"));
		String companyName = CommonUtils.checkNull(map.get("COMPANY_NAME"));
		String serviceStatus = CommonUtils.checkNull(map.get("SERVICE_STATUS"));
		String orgCode = CommonUtils.checkNull(map.get("orgCode"));
		String orgId = CommonUtils.checkNull(map.get("orgId"));
		String parentOrgCode = CommonUtils.checkNull(map.get("parentOrgCode"));
		String AUTHORIZATION_TYPE = CommonUtils.checkNull(map.get("AUTHORIZATION_TYPE"));
		String WORK_TYPE = CommonUtils.checkNull(map.get("WORK_TYPE"));
		String IMAGE_LEVEL = CommonUtils.checkNull(map.get("IMAGE_LEVEL"));
		String IMAGE_COMFIRM_LEVEL = CommonUtils.checkNull(map.get("IMAGE_COMFIRM_LEVEL"));
		String regionCode = CommonUtils.checkNull(map.get("regionCode"));
		String AUTHORIZATION_SCREATE_DATE = CommonUtils.checkNull(map.get("AUTHORIZATION_SCREATE_DATE"));
		String AUTHORIZATION_ECREATE_DATE = CommonUtils.checkNull(map.get("AUTHORIZATION_ECREATE_DATE"));
		
		List<Object> params = new LinkedList<Object>();
		StringBuffer sbSql = new StringBuffer();
//		sbSql.append("       select * from tmp_tm_dealer a where a.delete_flag = 0\n");
//		sbSql.append("   and a.dealer_id = ").append(logonUser.getDealerId()).append("\n");
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		TmpTmDealerPO t = new TmpTmDealerPO();
		t.setDealerId(new Long(logonUser.getDealerId()));
		int size = dao.select(t).size();
		sbSql.append("select a.dealer_id,a.id, a.dealer_code,\n");
		if(size>1){
			sbSql.append("       2 as size_,\n");
		}else{
			sbSql.append("       1 as size_,\n");
		}
		sbSql.append("       a.dealer_name,\n");
		sbSql.append("       a.CHANGE_AUTID_STATUS,\n");
		sbSql.append("       a.DEALER_SHORTNAME,\n");
		sbSql.append("       a.status,\n");
		sbSql.append("       e.COMPANY_NAME,\n");
//		sbSql.append("		 c.dealer_code parent_dealer_code,\n");
//		sbSql.append("		 c.dealer_name parent_dealer_name,\n"); 
		sbSql.append("       o.org_name,\n");
		sbSql.append("       a.dealer_level,\n");
		sbSql.append("       b.AUTHORIZATION_TYPE,\n");
		sbSql.append("       b.AUTHORIZATION_DATE,\n");
		sbSql.append("       b.WORK_TYPE,\n");
		sbSql.append("       DECODE(b.SHOP_TYPE,'直营','重点客户','代理','代理') SHOP_TYPE, \n");
		sbSql.append("       b.IMAGE_LEVEL,\n");
		sbSql.append("       b.IMAGE_COMFIRM_LEVEL,\n");
		sbSql.append("       a.service_status,\n");
		sbSql.append("       g.ROOT_ORG_NAME,\n");
		sbSql.append("       g.ROOT_ORG_ID,\n");
		sbSql.append("       f.REGION_NAME,\n");
		sbSql.append("       a.delete_flag\n");
		
		sbSql.append("  from tmp_tm_dealer              a,\n");
//		sbSql.append("       tmp_tm_dealer      h,\n");
		sbSql.append("       tm_dealer_detail       b,\n");
//		sbSql.append("       tm_dealer              c,\n");
		sbSql.append("       tm_org                 o,\n");
		sbSql.append("       tm_company             e,\n");
		sbSql.append("       tm_region             f,\n");
		sbSql.append("       vw_org_dealer_service            g\n");
		sbSql.append(" where a.dealer_id = b.fk_dealer_id(+)\n");
		sbSql.append(" and a.dealer_id = g.dealer_id(+)\n");
//		sbSql.append(" and a.dealer_id = h.dealer_id(+)\n");
//		sbSql.append("   and a.parent_dealer_d = c.dealer_id(+)\n");
		sbSql.append("   and a.dealer_org_id = o.org_id (+)\n");
		sbSql.append("   and a.company_id = e.company_id\n");
		sbSql.append("   and a.province_id = f.region_code\n");  
		if(size>1){
//			sbSql.append("    and   h.CHANGE_AUTID_STATUS !=20051003 \n");
		}else{
//			sbSql.append("       1 as size_,\n");
		}
//		sbSql.append("   and not exists (select * from tmp_tm_dealer tt where tt.dealer_id = a.dealer_id and tt.delete_flag != 1)\n");  
		if(!dealerType.equals("")) {
			sbSql.append("   and a.dealer_type = ?\n");
			params.add(dealerType);
		}
        if (!"".equals(AUTHORIZATION_SCREATE_DATE)) {
        	sbSql.append(" and AUTHORIZATION_DATE>= to_date('").append(AUTHORIZATION_SCREATE_DATE).append(" 00:00:00','YYYY/MM/dd HH24:mi:ss')");
        	sbSql.append(" and AUTHORIZATION_DATE<= to_date('").append(AUTHORIZATION_ECREATE_DATE).append(" 23:59:59','YYYY/MM/dd HH24:mi:ss')");
        }
		if(!dealerName.equals("")){
			sbSql.append("   and a.dealer_name like ?\n");
			params.add("%"+dealerName+"%");
		}
		
		if(!dealerCode.equals("")){
			sbSql.append("   and a.dealer_code like ?\n");
			params.add("%"+dealerCode+"%");
		}
		
		if(!dealerLevel.equals("")) {
			sbSql.append("	 and a.dealer_level = ?\n");
			params.add(dealerLevel);
		}
		
		if(!dealerStatus.equals("")) {
			sbSql.append(" 	 and a.status = ?\n");
			params.add(dealerStatus);
		}

		if(!parentOrgCode.equals("")) {
			sbSql.append(" 	 and o.parent_org_id in (select org_id from tm_org where org_code = ?)\n");
			params.add(parentOrgCode);
		}

		if(!sJDealerCode.equals("")) {
			sbSql.append(" 	 and a.parent_dealer_d in (select dealer_id from tm_dealer where dealer_code = ?)\n");
			params.add(sJDealerCode);
		}
		
		if(!companyName.equals("")) {
			sbSql.append(" 	 and e.company_shortname = ?\n");
			params.add(companyName);
		}
		
		if(!serviceStatus.equals("")) {
			sbSql.append("   and a.service_status = ?\n");
			params.add(serviceStatus);
		}
		
		if(!IMAGE_LEVEL.equals("")) {
			sbSql.append("   and b.IMAGE_LEVEL = ?\n");
			params.add(IMAGE_LEVEL);
		}
		if(!IMAGE_COMFIRM_LEVEL.equals("")) {
			sbSql.append("   and b.IMAGE_COMFIRM_LEVEL = ?\n");
			params.add(IMAGE_COMFIRM_LEVEL);
		}
		if(dealerType.equals("10771001")){
			if(!WORK_TYPE.equals("")) {
				sbSql.append("   and b.SHOP_TYPE = ?\n");
				params.add(WORK_TYPE);
			}
		}else{
			if(!WORK_TYPE.equals("")) {
				sbSql.append("   and b.WORK_TYPE = ?\n");
				params.add(WORK_TYPE);
			}
		}
		if(!AUTHORIZATION_TYPE.equals("")) {
			sbSql.append("   and b.AUTHORIZATION_TYPE = ?\n");
			params.add(AUTHORIZATION_TYPE);
		}
		if (!CommonUtils.isNullString(orgCode)) {
			sbSql.append("   and o.org_Code = '").append(orgCode).append("'\n") ;
		}
		if (!CommonUtils.isNullString(orgId)) {
			sbSql.append("   and g.ROOT_ORG_ID = ").append(orgId).append("\n");
		}
		if (!CommonUtils.isNullString(regionCode)) {
			sbSql.append(Utility.getConSqlByParamForEqual(regionCode, params, "G", "ORG_Code"));
		}
		
		sbSql.append("   and a.dealer_id = ").append(logonUser.getDealerId()).append("\n");
		sbSql.append("   order by g.ROOT_ORG_ID,g.org_id,g.dealer_id");
		return dao.pageQuery(sbSql.toString(), params, getFunName(),pageSize, curPage);
	}

	public boolean checkEx(String dealerId) throws Exception {

		List<Object> params = new LinkedList<Object>();
		StringBuffer sbSql = new StringBuffer();
		sbSql.append("select * from tmp_tm_dealer a where a.change_autid_status != 20051003 and a.dealer_id = " + dealerId + "\n");
		List<Map<String, Object>> list = dao.pageQuery(sbSql.toString(), params, getFunName());
		if (list != null && list.size() > 0) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * 取得经销商信息
	 * 
	 * @param map 参数
	 * @param curPage 当前页码
	 * @param pageSize 页显示记录数
	 * @return 经销商信息
	 * @throws Exception
	 */
	public PageResult<Map<String, Object>> getDealerInfo(Map<String, Object> map,int curPage,int pageSize) throws Exception {
		StringBuffer sbSql = new StringBuffer();
		String dealerName = (String) map.get("DEALER_NAME");
		String dealerCode = (String) map.get("DEALER_CODE");
		List<Object> params = new LinkedList<Object>();
		sbSql.append("SELECT td.dealer_code, \n");
		sbSql.append("       td.dealer_name, \n");
		sbSql.append("       td.status, \n");
		sbSql.append("       td.dealer_id \n");
		sbSql.append("FROM tm_dealer td \n");
		sbSql.append("WHERE   td.dealer_level = 10851001 \n");
		sbSql.append("	  AND td.dealer_type = 10771001 \n");
		if(!dealerName.equals("")){
			sbSql.append("   and td.dealer_name like ?\n");
			params.add("%"+dealerName+"%");
		}
		
		if(!dealerCode.equals("")){
			sbSql.append("   and td.dealer_code like ?\n");
			params.add("%"+dealerCode+"%");
		}
		sbSql.append("   order by td.dealer_id");
		return dao.pageQuery(sbSql.toString(), params, getFunName(), pageSize, curPage);
	}
	
	/**
	 * 取得待审核的最大年份
	 */
	public List<Map<String,Object>> getMaxAuditYear() {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT MAX(tvcd.year_flag)  max_audit_year \n");
		sql.append("FROM tt_vi_construct_detail tvcd \n");
		sql.append("WHERE tvcd.status = 92861001 \n");
		sql.append("	AND tvcd.state = 10011011 \n");
		List<Map<String,Object>> result = dao.pageQuery(sql.toString(), null, getFunName());
		return result;
	}
	
	/**
	 * 取得形象店明细数据ID
	 */
	public String getDetailIdByVehicleSeriesId(String vehicleSeriesId, String dealerId, String yearFlag) {
		StringBuffer sql = new StringBuffer();
		List<Object> params = new LinkedList<Object>();
		sql.append("SELECT TVCD.DETAIL_ID \n");
		sql.append("FROM tt_vi_construct_main TVCM,\n");
		sql.append("     tt_vi_construct_detail TVCD \n");
		sql.append("WHERE TVCM.ID = TVCD.ID \n");
		sql.append("  AND TVCM.VEHICLE_SERIES_ID = ?\n");
		params.add(vehicleSeriesId);
		sql.append("  AND TVCM.DEALER_ID = ?\n");
		params.add(dealerId);
		sql.append("  AND TVCD.YEAR_FLAG= ?\n");
		params.add(yearFlag);
		List<Map<String,Object>> result = dao.pageQuery(sql.toString(), params, getFunName());
		return result.get(0).get("DETAIL_ID").toString();
	}
	public List<Map<String,Object>> queryCityByProvince(String orgId){
		String sql = "";
		if(!"".equals(orgId)){
			sql	= "SELECT REGION_ID,REGION_NAME,ORG_ID FROM TM_REGION TR1 WHERE ORG_ID = "+orgId+" AND REGION_TYPE = "+Constant.REGION_TYPE_03;
		}else{
			sql	= "SELECT REGION_ID,REGION_NAME,ORG_ID FROM TM_REGION TR1 WHERE  REGION_TYPE = "+Constant.REGION_TYPE_03 +" AND ORG_ID IS NOT NULL";
		}
		 
		return dao.pageQuery(sql, null, getFunName());
	}
	
	/**
	 * 查询开票信息
	 * 
	 * @param dealerId 经销商Id
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> getBillingInfo(String dealerId) throws Exception{
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT TBI.BILLING_INFO_ID,\n");
		sql.append("       TBI.DEALER_ID,\n");  
		sql.append("       TBI.DEALER_TYPE,\n");  
		sql.append("       TBI.BILLING_TYPE,\n");  
		sql.append("       TBI.BILLING_UNIT,\n");  
		sql.append("       TBI.TAX_NO,\n");  
		sql.append("       TBI.BANK,\n");  
		sql.append("       TBI.ACCOUNT,\n");  
		sql.append("       TBI.BILLING_ADDRESS,\n");  
		sql.append("       TBI.STATUS \n");  
		sql.append("  FROM TM_BILLING_INFO TBI WHERE TBI.DEALER_ID = " + dealerId + "\n");  
		logger.debug("SQL+++++++++++++++++++++++: " + sql.toString());
		return pageQuery(sql.toString(),null,getFunName());
	}
}
