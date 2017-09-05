package com.infodms.dms.dao.sales.dealer;

import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.common.Constant;
import com.infodms.dms.common.materialManager.MaterialGroupManagerDao;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

/**
 * 售后经销商绑定
 * 
 * @author jiangrp
 *
 */
public class ServiceDealerBindDao extends BaseDao<PO> {
	public static Logger logger = Logger.getLogger(ServiceDealerBindDao.class);
	private static ServiceDealerBindDao dao = new ServiceDealerBindDao ();
	public static final ServiceDealerBindDao getInstance() {
		if (dao == null) {
			dao = new ServiceDealerBindDao();
		}
		return dao;
	}
	private ServiceDealerBindDao() {}
	
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		return null;
	}
	
	/**
	 * 售后经销商查询
	 */
	public PageResult<Map<String, Object>> queryServiceDealerInfo(Map<String, Object> map,int curPage,int pageSize) throws Exception{
		
		String dealerCode = CommonUtils.checkNull(map.get("DEALER_CODE"));
		String dealerName = CommonUtils.checkNull(map.get("DEALER_NAME"));
		String poseId = CommonUtils.checkNull(map.get("poseId"));
		
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
		sbSql.append("       (SELECT td.dealer_name FROM tm_dealer td WHERE td.dealer_id=tsdb.bind_flag) BINDED_DEALER_NAME \n");
		sbSql.append("  from tm_dealer              a,\n");
		sbSql.append("       tm_dealer_detail       b,\n");
		sbSql.append("       tm_dealer              c,\n");
		sbSql.append("       tm_org                 o,\n");
		sbSql.append("       tm_company             e,\n");
		sbSql.append("       tm_region             f,\n");
		sbSql.append("       vw_org_dealer_service            g,\n");
		sbSql.append("       TT_SERVICE_DEALER_BIND TSDB \n");
		sbSql.append(" where a.dealer_id = b.fk_dealer_id(+)\n");
		sbSql.append(" and a.dealer_id = g.dealer_id(+)\n");
		sbSql.append("   and a.parent_dealer_d = c.dealer_id(+)\n");
		sbSql.append("   and o.org_id = a.dealer_org_id\n");
		sbSql.append("   and a.company_id = e.company_id\n");
		sbSql.append("   and a.province_id = f.region_code\n");  
		sbSql.append("   and a.dealer_id=tsdb.bind_dealer_id(+) \n");
		sbSql.append("   and a.dealer_type=10771002 \n");
        if(!dealerName.equals("")){
			sbSql.append("   and (a.dealer_name like ? or a.pinyin like ?  )\n");
			params.add("%"+dealerName+"%");
			params.add("%"+dealerName+"%");
		}
		
		if(!dealerCode.equals("")){
			sbSql.append("   and a.dealer_code like ?\n");
			params.add("%"+dealerCode+"%");
		}
		sbSql.append(MaterialGroupManagerDao.getPoseIdBusinessSql(Constant.areaId, poseId));
		sbSql.append("   order by g.ROOT_ORG_ID,g.org_id,g.dealer_id");
		return dao.pageQuery(sbSql.toString(), params, getFunName(),pageSize, curPage);
	}
	
	/**
	 * 绑定的经销商查询
	 */
	public PageResult<Map<String, Object>> queryBindServiceDealerInfo(Map<String, Object> map,int curPage,int pageSize) throws Exception {
		String CUR_DEALER_ID = CommonUtils.checkNull(map.get("CUR_DEALER_ID"));
		List<Object> params = new LinkedList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT VMDS.Root_Org_Name, \n");
		sql.append("       TR.REGION_NAME, \n");
		sql.append("       TD.DEALER_ID, \n");
		sql.append("       TD.DEALER_CODE, \n");
		sql.append("       TD.DEALER_NAME,  \n");
		sql.append("       TD.DEALER_LEVEL, \n");
		sql.append("       TOR.ORG_NAME,    \n");
		sql.append("       TC.COMPANY_NAME,  \n");
		sql.append("       TD.SERVICE_STATUS, \n");
		sql.append("       TDD.IMAGE_COMFIRM_LEVEL \n");
		sql.append("FROM Tt_Service_Dealer_Bind TSDB  \n");
		sql.append("INNER JOIN TM_DEALER TD ON TSDB.BIND_DEALER_ID=TD.DEALER_ID \n");
		sql.append("INNER JOIN TM_DEALER_DETAIL TDD ON TD.DEALER_ID=TDD.FK_DEALER_ID \n");
		sql.append("INNER JOIN vw_org_dealer_service VMDS ON TD.Dealer_Id=VMDS.DEALER_ID \n");
		sql.append("INNER JOIN tm_region TR ON TD.PROVINCE_ID=TR.REGION_CODE \n");
		sql.append("INNER JOIN TM_ORG TOR ON TOR.ORG_ID=TD.DEALER_ORG_ID \n");
		sql.append("INNER JOIN TM_COMPANY TC ON TC.COMPANY_ID=TD.COMPANY_ID \n");
		sql.append("WHERE TSDB.STATUS=10041001 AND TSDB.BIND_FLAG=").append(CUR_DEALER_ID).append(" \n");
		sql.append("ORDER BY TSDB.BIND_ID");
		return dao.pageQuery(sql.toString(), params, getFunName(),pageSize, curPage);
	}
}
