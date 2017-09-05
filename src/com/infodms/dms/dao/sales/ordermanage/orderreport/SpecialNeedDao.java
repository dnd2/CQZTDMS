/**********************************************************************
* <pre>
* FILE : SpecialNeedDao.java
* CLASS : SpecialNeedDao
* AUTHOR : 
* FUNCTION : 订单提报
*======================================================================
* CHANGE HISTORY LOG
*----------------------------------------------------------------------
* MOD. NO.|   DATE   |    NAME    | REASON  |  CHANGE REQ.
*----------------------------------------------------------------------
*         |2010-06-12|            | Created |
* DESCRIPTION:
* </pre>
***********************************************************************/
package com.infodms.dms.dao.sales.ordermanage.orderreport;

import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.common.getCompanyId.GetOemcompanyId;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.dao.productmanage.ProductManageDao;
import com.infodms.dms.dao.sales.ordermanage.reqquery.ReqQueryDao;
import com.infodms.dms.po.TmBusinessParaPO;
import com.infodms.dms.po.TmDateSetPO;
import com.infodms.dms.po.TtVsOrderPO;
import com.infodms.dms.po.TtVsSpecialReqPO;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;
/**
 * @Title: 订做车需求DAO
 * @Description:InfoFrame3.0.V01
 * @Copyright: Copyright (c) 2010
 * @Company: www.infoservice.com.cn
 * @Date: 2010-6-12
 * @author 
 * @mail   	
 * @version 1.0
 * @remark 
 */
public class SpecialNeedDao extends BaseDao<PO> {
	public static Logger logger = Logger.getLogger(OrderReportDao.class);
	private static final SpecialNeedDao dao = new SpecialNeedDao();

	public static final SpecialNeedDao getInstance() {
		return dao;
	}

	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * Function         : 订做车需求提报查询
	 * @param           : 业务范围ID
	 * @param           : 经销商ID
	 * @param           : 当前页码
	 * @param           : 每页显示记录数
	 * @return          : 满足条件的信息，包含分页
	 * @throws          : Exception
	 * LastUpdate       : 2010-06-12
	 */
	public PageResult<Map<String, Object>> getSpecialNeedList(String areaId,String area,String dealerId,int curPage,int pageSize) throws Exception{
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT TMBA.AREA_NAME,TVSR.REQ_ID,NVL(TVSR.VER,0) VER,TVSR.REQ_STATUS, TVSR.REFIT_DESC\n" );
		sql.append("  FROM TT_VS_SPECIAL_REQ TVSR,TM_BUSINESS_AREA TMBA\n" );
		sql.append(" WHERE TVSR.REQ_STATUS IN ("+Constant.SPECIAL_NEED_STATUS_01+", "+Constant.SPECIAL_NEED_STATUS_05+")\n" );
		sql.append(" AND TMBA.AREA_ID = TVSR.AREA_ID");
		if(!"-1".equals(areaId)&&!"".equals(areaId)&&areaId!=null){
			sql.append(" AND TVSR.AREA_ID = "+areaId+"\n");
		}
		if("-1".equals(areaId)&&!"".equals(area)&&area!=null){
			sql.append(" AND TVSR.AREA_ID IN ("+area+")\n");
		}
		if(!"".equals(dealerId)&&dealerId!=null){
			sql.append(" AND TVSR.DEALER_ID IN("+dealerId+")\n");
		}
		sql.append(" ORDER BY TVSR.CREATE_DATE DESC");
		/*sql.append("SELECT TMBA.AREA_NAME,\n");
		sql.append("       TVSR.REQ_ID,\n");  
		sql.append("       NVL(TVSR.VER, 0) VER,\n");  
		sql.append("       TVSR.REQ_STATUS,\n");  
		sql.append("       TVSR.REFIT_DESC\n");  
		sql.append("  FROM TT_VS_SPECIAL_REQ TVSR, TM_BUSINESS_AREA TMBA\n");  
		sql.append(" WHERE TVSR.REQ_STATUS IN ("+Constant.SPECIAL_NEED_STATUS_01+", "+Constant.SPECIAL_NEED_STATUS_05+")\n" );
		sql.append(" AND TMBA.AREA_ID = TVSR.AREA_ID");
		if(!"-1".equals(areaId)&&!"".equals(areaId)&&areaId!=null){
			sql.append(" AND TVSR.AREA_ID =  "+areaId+"\n");
		}
		if("-1".equals(areaId)&&!"".equals(area)&&area!=null){
			sql.append(" AND TVSR.AREA_ID IN ("+area+")\n");
		}
		if(!"".equals(dealerId)&&dealerId!=null){
			sql.append(" AND TVSR.DEALER_ID IN("+dealerId+")\n");
		}
		sql.append("UNION\n");  
		sql.append("SELECT TMBA.AREA_NAME,\n");  
		sql.append("       TVSR.REQ_ID,\n");  
		sql.append("       NVL(TVSR.VER, 0) VER,\n");  
		sql.append("       TVSR.REQ_STATUS,\n");  
		sql.append("       TVSR.REFIT_DESC\n");  
		sql.append("  FROM TT_VS_SPECIAL_REQ TVSR, TM_BUSINESS_AREA TMBA, tm_dealer td\n");  
		sql.append(" WHERE TVSR.REQ_STATUS IN ("+Constant.SPECIAL_NEED_STATUS_10+")\n" );
		sql.append("   AND TMBA.AREA_ID = TVSR.AREA_ID\n"); 
		if(!"".equals(dealerId)&&dealerId!=null){
			sql.append("  and td.parent_dealer_d in ( ");
			sql.append(dealerId);
			sql.append(")\n");
		}
		sql.append("   AND TVSR.DEALER_ID IN (td.dealer_id)\n");*/
		PageResult<Map<String, Object>> rs = dao.pageQuery(sql.toString(), null, getFunName(),pageSize, curPage);
		return rs;
	}
	/**
	 * Function         : 订做车需求物料查询
	 * @param           : 物料CODE
	 * @return          : 物料信息
	 * @throws          : Exception
	 * LastUpdate       : 2010-06-12
	 */
	public Map<String, Object> getMaterialInfo(String materialCode) {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT TVMG3.GROUP_CODE SERIES_CODE,\n");
		sql.append("       TVMG3.GROUP_NAME SERIES_NAME,\n");
		sql.append("       TVM.MATERIAL_ID,\n");
		sql.append("       TVM.MATERIAL_CODE,\n");
		sql.append("       TVM.MATERIAL_NAME\n");
		sql.append("  FROM TM_VHCL_MATERIAL_GROUP   TVMG1,\n");
		sql.append("       TM_VHCL_MATERIAL_GROUP   TVMG2,\n");
		sql.append("       TM_VHCL_MATERIAL_GROUP   TVMG3,\n");
		sql.append("       TM_VHCL_MATERIAL         TVM,\n");
		sql.append("       TM_VHCL_MATERIAL_GROUP_R TVMGR\n");
		sql.append(" WHERE TVMG1.PARENT_GROUP_ID = TVMG2.GROUP_ID\n");
		sql.append("   AND TVMG2.PARENT_GROUP_ID = TVMG3.GROUP_ID\n");
		sql.append("   AND TVMG1.GROUP_ID = TVMGR.GROUP_ID\n");
		sql.append("   AND TVMGR.MATERIAL_ID = TVM.MATERIAL_ID\n");
		sql.append("   AND TVM.MATERIAL_CODE = '" + materialCode + "'");
		Map<String, Object> map = pageQueryMap(sql.toString(), null,getFunName());
		return map;
	}
	
	
	/**
	 * Function         : 订做车需求物料查询
	 * @param           : 物料CODE
	 * @return          : 物料信息
	 * @throws          : Exception
	 * LastUpdate       : 2010-06-12
	 */
	public List<Map<String, Object>> getMaterialGroupInfo(String materialGroupCode, String confIds) {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT TVMG3.GROUP_NAME SERIES_CODE,\n");
		sql.append("       TVMG.GROUP_ID,\n");  
		sql.append("       TVMG.GROUP_CODE,\n");  
		sql.append("       TVMG.GROUP_NAME,\n");  
		sql.append("       TVMG2.GROUP_CODE MODEL_CODE\n");  
		sql.append("  FROM TM_VHCL_MATERIAL_GROUP TVMG,\n");  
		sql.append("       TM_VHCL_MATERIAL_GROUP   TVMG2,\n");  
		sql.append("       TM_VHCL_MATERIAL_GROUP   TVMG3\n");  
		sql.append(" WHERE TVMG.GROUP_LEVEL = 4\n");  
		sql.append("       AND TVMG.PARENT_GROUP_ID = TVMG2.GROUP_ID\n");  
		sql.append("       AND TVMG2.PARENT_GROUP_ID = TVMG3.GROUP_ID\n");
		sql.append("	and tvmg.group_code in ('");
		String[] groupCodes = materialGroupCode.split(",");
		for(int i=0; i<groupCodes.length; i++) {
			sql.append(groupCodes[i]);
			if(i != groupCodes.length-1){
				sql.append("','");
			}
		}
		sql.append("')\n");
		if(confIds != null && !"".equals(confIds)){
			sql.append("AND TVMG.GROUP_ID not in (");
			sql.append(confIds);
			sql.append(")\n");
		}
		List<Map<String, Object>> list = pageQuery(sql.toString(), null,getFunName());
		return list;
	}
	
	/**
	 * Function         : 订做车需求修改查询
	 * @param           : 需求ID
	 * @return          : 满足条件的信息
	 * @throws          : Exception
	 * LastUpdate       : 2010-06-13
	 */
	public List<Map<String, Object>> getSpecialNeedDetail(String reqId) throws Exception{
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT TVMG3.GROUP_NAME SERIES_NAME,\n");
		sql.append("       TVMG.GROUP_ID,\n");  
		sql.append("       TVMG.GROUP_CODE,\n");  
		sql.append("       TVMG.GROUP_NAME,\n");  
		sql.append("       TVMG.MODEL_CODE,\n");  
		sql.append("       TVSRD.AMOUNT\n");  
		sql.append("  FROM TM_VHCL_MATERIAL_GROUP   TVMG,\n");  
		sql.append("       TM_VHCL_MATERIAL_GROUP   TVMG2,\n");  
		sql.append("       TM_VHCL_MATERIAL_GROUP   TVMG3,\n");  
		sql.append("       TT_VS_SPECIAL_REQ_DTL    TVSRD\n");  
		sql.append(" WHERE TVMG.PARENT_GROUP_ID = TVMG2.GROUP_ID\n");  
		sql.append("   AND TVMG2.PARENT_GROUP_ID = TVMG3.GROUP_ID\n");  
		sql.append("   AND TVMG.GROUP_ID = TVSRD.MATERIAL_ID\n");
		if(!"".equals(reqId)&&reqId!=null){
			sql.append(" AND TVSRD.REQ_ID = "+reqId+"\n");
		}
		sql.append("ORDER BY TVSRD.CREATE_DATE DESC\n");
		List<Map<String, Object>> list = dao.pageQuery(sql.toString(), null, getFunName());
		return list;
	}
	
	/**
	 * Function         : 订做车需求审核查询
	 * @param           : 需求ID
	 * @return          : 满足条件的信息
	 * @throws          : Exception
	 * LastUpdate       : 2010-06-17
	 */
	public List<Map<String, Object>> getSpecialNeedCheck(String reqId) throws Exception{
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT TO_CHAR(TVSRC.CHK_DATE, 'YYYY-MM-DD') CHECK_DATE,\n" );
		sql.append("       TMO.ORG_NAME,\n" );
		sql.append("       TCU.NAME,\n" );
		sql.append("       TCC.CODE_DESC CHECK_STATUS,\n" );
		sql.append("       TVSRC.CHK_DESC CHECK_DESC\n" );
		sql.append("  FROM TT_VS_SPECIAL_REQ_CHK TVSRC, TM_ORG TMO, TC_USER TCU,TC_CODE TCC\n" );
		sql.append(" WHERE TVSRC.CHK_ORG_ID = TMO.ORG_ID\n" );
		sql.append("   AND TVSRC.CHK_USER_ID = TCU.USER_ID\n" );
		sql.append("   AND TVSRC.CHK_STATUS = TCC.CODE_ID\n" );
		if(!"".equals(reqId)&&reqId!=null){
			sql.append(" AND TVSRC.REQ_ID = "+reqId+"\n");
		}
		sql.append("ORDER BY TVSRC.CHK_DATE DESC\n");
		List<Map<String, Object>> list = dao.pageQuery(sql.toString(), null, getFunName());
		return list;
	}
	
	/**
	 * Function         : 订做车需求大客户审核查询
	 * @param           : 业务范围ID
	 * @param           : 经销商CODE
	 * @param           : 需求提报日期
	 * @param           : 当前页码
	 * @param           : 每页显示记录数
	 * @return          : 满足条件的信息，包含分页
	 * @throws          : Exception
	 * LastUpdate       : 2010-11-16
	 */
	public PageResult<Map<String, Object>> getSpecialNeedFleetCheckList(String areaId,String area,String dealerCode,String startDate,String endDate,int curPage,int pageSize) throws Exception{
		List<Object> params = new LinkedList<Object>();
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT TVSR.REQ_ID,\n" );
		sql.append("       TMBA.AREA_NAME,\n" );
		sql.append("       TMD.DEALER_CODE,\n" );
		sql.append("       TMD.DEALER_SHORTNAME DEALER_NAME,\n" );
		sql.append("       TO_CHAR(TVSR.REQ_DATE, 'YYYY-MM-DD') REQ_DATE,\n" );
		sql.append("       NVL(TVSR.VER,0) VER,\n" );
		sql.append("       TVSR.REFIT_DESC,\n" );
		sql.append("       SUM(TVSRD.AMOUNT) AMOUNT\n" );
		sql.append("  FROM TT_VS_SPECIAL_REQ TVSR, TT_VS_SPECIAL_REQ_DTL TVSRD, TM_DEALER TMD,TM_BUSINESS_AREA TMBA\n" );
		sql.append(" WHERE TVSR.REQ_ID = TVSRD.REQ_ID\n" );
		sql.append("   AND TVSR.AREA_ID = TMBA.AREA_ID\n" );
		sql.append("   AND TVSR.DEALER_ID = TMD.DEALER_ID\n" );
		sql.append("   AND TVSR.REQ_STATUS = "+Constant.SPECIAL_NEED_STATUS_09+"\n" );
		if (!"".equals(dealerCode)&&null!=dealerCode){
			sql.append(Utility.getConSqlByParamForEqual(dealerCode, params, "TMD", "DEALER_CODE"));
		}
		if(!"-1".equals(areaId)&&!"".equals(areaId)&&areaId!=null){
			sql.append("           AND TVSR.AREA_ID = "+areaId+"\n" );
		}
		if("-1".equals(areaId)&&!"".equals(area)&&area!=null){
			sql.append("           AND TVSR.AREA_ID IN( "+area+")\n" );
		}
		if(!"".equals(startDate)&&startDate!=null){
			sql.append("   AND TVSR.REQ_DATE >= TO_DATE('"+startDate+" 00:00:00','YYYY-MM-DD HH24:MI:SS')\n");
		}
		if(!"".equals(endDate)&&endDate!=null){
			sql.append("   AND TVSR.REQ_DATE <= TO_DATE('"+endDate+" 23:59:59','YYYY-MM-DD HH24:MI:SS')\n");
		}
		sql.append(" GROUP BY TVSR.REQ_ID,\n" );
		sql.append("          TMBA.AREA_NAME,\n" );
		sql.append("          TMD.DEALER_CODE,TVSR.VER,\n" );
		sql.append("          TMD.DEALER_SHORTNAME,\n" );
		sql.append("          TVSR.REQ_DATE,\n" );
		sql.append("          TVSR.REFIT_DESC\n" );
		sql.append(" ORDER BY TVSR.REQ_DATE DESC\n");
		PageResult<Map<String, Object>> rs = dao.pageQuery(sql.toString(), params, getFunName(),pageSize, curPage);
		return rs;
	}
	
	/**
	 * Function         : 订做车需求产品审核查询
	 * @param           : 业务范围ID
	 * @param           : 经销商CODE
	 * @param           : 需求提报日期
	 * @param           : 当前页码
	 * @param           : 每页显示记录数
	 * @return          : 满足条件的信息，包含分页
	 * @throws          : Exception
	 * LastUpdate       : 2010-06-17
	 */
	public PageResult<Map<String, Object>> getSpecialNeedCheckList(String orgId, String areaId,String area,String dealerCode,String startDate,String endDate,int curPage,int pageSize) throws Exception{
		List<Object> params = new LinkedList<Object>();
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT TVSR.REQ_ID,\n" );
		sql.append("       TMBA.AREA_NAME,\n" );
		sql.append("       TMD.DEALER_CODE,\n" );
		sql.append("       TMD.DEALER_SHORTNAME DEALER_NAME,\n" );
		sql.append("       TO_CHAR(TVSR.REQ_DATE, 'YYYY-MM-DD') REQ_DATE,\n" );
		sql.append("       NVL(TVSR.VER,0) VER,\n" );
		sql.append("       TVSR.REFIT_DESC,\n" );
		sql.append("       SUM(TVSRD.AMOUNT) AMOUNT\n" );
		sql.append("  FROM TT_VS_SPECIAL_REQ TVSR, TT_VS_SPECIAL_REQ_DTL TVSRD, TM_DEALER TMD,TM_BUSINESS_AREA TMBA\n" );
		sql.append(" WHERE TVSR.REQ_ID = TVSRD.REQ_ID\n" );
		sql.append("   AND TVSR.AREA_ID = TMBA.AREA_ID\n" );
		sql.append("   AND TVSR.DEALER_ID = TMD.DEALER_ID\n" );
		sql.append("   AND TVSR.REQ_STATUS = "+Constant.SPECIAL_NEED_STATUS_02+"\n" );
		if (!"".equals(dealerCode)&&null!=dealerCode){
			sql.append(Utility.getConSqlByParamForEqual(dealerCode, params, "TMD", "DEALER_CODE"));
		}
		if(!"-1".equals(areaId)&&!"".equals(areaId)&&areaId!=null){
			sql.append("           AND TVSR.AREA_ID = "+areaId+"\n" );
		}
		if("-1".equals(areaId)&&!"".equals(area)&&area!=null){
			sql.append("           AND TVSR.AREA_ID IN( "+area+")\n" );
		}
		if(!"".equals(startDate)&&startDate!=null){
			sql.append("   AND TVSR.REQ_DATE >= TO_DATE('"+startDate+" 00:00:00','YYYY-MM-DD HH24:MI:SS')\n");
		}
		if(!"".equals(endDate)&&endDate!=null){
			sql.append("   AND TVSR.REQ_DATE <= TO_DATE('"+endDate+" 23:59:59','YYYY-MM-DD HH24:MI:SS')\n");
		}
		
		if(!CommonUtils.isNullString(orgId)) {
			sql.append(ReqQueryDao.limitOrg("TMD", "dealer_id", orgId)) ;
		}
		
		sql.append(" GROUP BY TVSR.REQ_ID,\n" );
		sql.append("          TMBA.AREA_NAME,\n" );
		sql.append("          TMD.DEALER_CODE,TVSR.VER,\n" );
		sql.append("          TMD.DEALER_SHORTNAME,\n" );
		sql.append("          TVSR.REQ_DATE,\n" );
		sql.append("          TVSR.REFIT_DESC\n" );
		sql.append(" ORDER BY TVSR.REQ_DATE DESC\n");
		PageResult<Map<String, Object>> rs = dao.pageQuery(sql.toString(), params, getFunName(),pageSize, curPage);
		return rs;
	}
	
	
	
	public PageResult<Map<String, Object>> getSpecialNeedCheckListLever(String dealerIds, String areaId,String area, String startDate,String endDate,int curPage,int pageSize) throws Exception{
		List<Object> params = new LinkedList<Object>();
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT TVSR.REQ_ID,\n" );
		sql.append("       TMBA.AREA_NAME,\n" );
		sql.append("       TMD.DEALER_CODE,\n" );
		sql.append("       TMD.DEALER_SHORTNAME DEALER_NAME,\n" );
		sql.append("       TO_CHAR(TVSR.REQ_DATE, 'YYYY-MM-DD') REQ_DATE,\n" );
		sql.append("       NVL(TVSR.VER,0) VER,\n" );
		sql.append("       TVSR.REFIT_DESC,\n" );
		sql.append("       SUM(TVSRD.AMOUNT) AMOUNT\n" );
		sql.append("  FROM TT_VS_SPECIAL_REQ TVSR, TT_VS_SPECIAL_REQ_DTL TVSRD, TM_DEALER TMD,TM_BUSINESS_AREA TMBA\n" );
		sql.append(" WHERE TVSR.REQ_ID = TVSRD.REQ_ID\n" );
		sql.append("   AND TVSR.AREA_ID = TMBA.AREA_ID\n" );
		sql.append("   AND TVSR.DEALER_ID = TMD.DEALER_ID\n" );
		sql.append("   AND TVSR.REQ_STATUS = "+Constant.SPECIAL_NEED_STATUS_10+"\n" );
		
		if(!"-1".equals(areaId)&&!"".equals(areaId)&&areaId!=null){
			sql.append("           AND TVSR.AREA_ID = "+areaId+"\n" );
		}
		if("-1".equals(areaId)&&!"".equals(area)&&area!=null){
			sql.append("           AND TVSR.AREA_ID IN( "+area+")\n" );
		}
		if(!"".equals(startDate)&&startDate!=null){
			sql.append("   AND TVSR.REQ_DATE >= TO_DATE('"+startDate+" 00:00:00','YYYY-MM-DD HH24:MI:SS')\n");
		}
		if(!"".equals(endDate)&&endDate!=null){
			sql.append("   AND TVSR.REQ_DATE <= TO_DATE('"+endDate+" 23:59:59','YYYY-MM-DD HH24:MI:SS')\n");
		}
		
		if(!CommonUtils.isNullString(dealerIds)) {
			sql.append(" and exists (select 1 from vw_org_dealer vod where vod.root_dealer_id in (").append(dealerIds).append(") and vod.dealer_id = tvsr.dealer_id)") ;
		}
		
		sql.append(" GROUP BY TVSR.REQ_ID,\n" );
		sql.append("          TMBA.AREA_NAME,\n" );
		sql.append("          TMD.DEALER_CODE,TVSR.VER,\n" );
		sql.append("          TMD.DEALER_SHORTNAME,\n" );
		sql.append("          TVSR.REQ_DATE,\n" );
		sql.append("          TVSR.REFIT_DESC\n" );
		sql.append(" ORDER BY TVSR.REQ_DATE DESC\n");
		PageResult<Map<String, Object>> rs = dao.pageQuery(sql.toString(), params, getFunName(),pageSize, curPage);
		return rs;
	}
	/**
	 * Function         : 订做车需求产品审核查询
	 * @param           : 需求ID
	 * @return          : 满足条件的信息
	 * @throws          : Exception
	 * LastUpdate       : 2010-06-17
	 */
	public List<Map<String, Object>> getSpecialNeedDetailCheckList(String reqId) throws Exception{
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT TVMG3.GROUP_NAME SERIES_NAME,\n");
		sql.append("       TVSRD.DTL_ID,\n");  
		sql.append("       DECODE(TVSRD.SPECIAL_BATCH_NO,NULL,'',TVSRD.SPECIAL_BATCH_NO) BATCH_NO,\n");  
		sql.append("       TVMG.GROUP_ID,\n");  
		sql.append("       TVMG.GROUP_CODE,\n");  
		sql.append("       TVMG.GROUP_NAME,\n");  
		sql.append("       TVMG.MODEL_CODE,\n");  
		sql.append("       TO_CHAR(TVSRD.EXPECTED_DATE,'YYYY-MM-DD') EXPECTED_DATE,\n");  
		sql.append("       TVSRD.AMOUNT\n");  
		sql.append("  FROM TM_VHCL_MATERIAL_GROUP   TVMG,\n");  
		sql.append("       TM_VHCL_MATERIAL_GROUP   TVMG2,\n");  
		sql.append("       TM_VHCL_MATERIAL_GROUP   TVMG3,\n");  
		sql.append("       TT_VS_SPECIAL_REQ_DTL    TVSRD\n");  
		sql.append(" WHERE TVMG.PARENT_GROUP_ID = TVMG2.GROUP_ID\n");  
		sql.append("   AND TVMG2.PARENT_GROUP_ID = TVMG3.GROUP_ID\n");  
		sql.append("   AND TVSRD.MATERIAL_ID = TVMG.GROUP_ID\n");
		if(!"".equals(reqId)&&reqId!=null){
			sql.append(" AND TVSRD.REQ_ID = "+reqId+"\n");
		}
		sql.append("ORDER BY TVSRD.CREATE_DATE DESC\n");
		List<Map<String, Object>> list = dao.pageQuery(sql.toString(), null, getFunName());
		return list;
	}
	
	/**
	 * Function         : 订做车需求价格核定查询
	 * @param           : 业务范围ID
	 * @param           : 经销商CODE
	 * @param           : 需求提报日期
	 * @param           : 当前页码
	 * @param           : 每页显示记录数
	 * @return          : 满足条件的信息，包含分页
	 * @throws          : Exception
	 * LastUpdate       : 2010-06-17
	 */
	public PageResult<Map<String, Object>> getSpecialNeedPriceCheckList(String orgId, String areaId,String area,String dealerCode,String startDate,String endDate,int curPage,int pageSize) throws Exception{
		List<Object> params = new LinkedList<Object>();
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT TVSR.REQ_ID,\n" );
		sql.append("       TMBA.AREA_NAME,\n" );
		sql.append("       TVSR.DEALER_ID,\n" );
		sql.append("       TVSR.REQ_STATUS,\n" );
		sql.append("       TMD.DEALER_CODE,\n" );
		sql.append("       TMD.DEALER_SHORTNAME DEALER_NAME,\n" );
		sql.append("       TO_CHAR(TVSR.REQ_DATE, 'YYYY-MM-DD') REQ_DATE,\n" );
		sql.append("       TVSR.REFIT_DESC,\n" );
		sql.append("       SUM(TVSRD.AMOUNT) AMOUNT\n" );
		sql.append("  FROM TT_VS_SPECIAL_REQ TVSR, TT_VS_SPECIAL_REQ_DTL TVSRD, TM_DEALER TMD,TM_BUSINESS_AREA TMBA\n" );
		sql.append(" WHERE TVSR.REQ_ID = TVSRD.REQ_ID\n" );
		sql.append("   AND TVSR.DEALER_ID = TMD.DEALER_ID\n" );
		sql.append("   AND TVSR.AREA_ID = TMBA.AREA_ID\n" );
		sql.append("   AND TVSR.REQ_STATUS = "+Constant.SPECIAL_NEED_STATUS_03+"\n" );
		if (!"".equals(dealerCode)&&null!=dealerCode){
			sql.append(Utility.getConSqlByParamForEqual(dealerCode, params, "TMD", "DEALER_CODE"));
		}
		if(!"-1".equals(areaId)&&!"".equals(areaId)&&areaId!=null){
			sql.append("           AND TVSR.AREA_ID = "+areaId+"\n" );
		}
		if("-1".equals(areaId)&&!"".equals(area)&&area!=null){
			sql.append("           AND TVSR.AREA_ID IN( "+area+")\n" );
		}
		if(!"".equals(startDate)&&startDate!=null){
			sql.append("   AND TVSR.REQ_DATE >= TO_DATE('"+startDate+" 00:00:00','YYYY-MM-DD HH24:MI:SS')\n");
		}
		if(!"".equals(endDate)&&endDate!=null){
			sql.append("   AND TVSR.REQ_DATE <= TO_DATE('"+endDate+" 23:59:59','YYYY-MM-DD HH24:MI:SS')\n");
		}
		
		if(!CommonUtils.isNullString(orgId)) {
			sql.append(ReqQueryDao.limitOrg("TMD", "dealer_id", orgId)) ;
		}
		
		sql.append(" GROUP BY TVSR.REQ_ID,\n" );
		sql.append("          TMBA.AREA_NAME,\n" );
		sql.append("          TVSR.DEALER_ID,\n" );
		sql.append("          TVSR.REQ_STATUS,\n" );
		sql.append("          TMD.DEALER_CODE,\n" );
		sql.append("          TMD.DEALER_SHORTNAME,\n" );
		sql.append("          TVSR.REQ_DATE,\n" );
		sql.append("          TVSR.REFIT_DESC\n" );
		sql.append(" ORDER BY TVSR.REQ_DATE DESC\n");
		PageResult<Map<String, Object>> rs = dao.pageQuery(sql.toString(), params, getFunName(),pageSize, curPage);
		return rs;
	}
	
	/**
	 * Function         : 订做车需求核价明细查询
	 * @param           : 需求ID
	 * @return          : 满足条件的信息
	 * @throws          : Exception
	 * LastUpdate       : 2010-06-17
	 */
	public List<Map<String, Object>> getSpecialNeedDetailPriceCheckList(String reqId,String dealerId) throws Exception{
		ActionContext act = ActionContext.getContext() ;
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		Map<String, Object> map = new HashMap<String, Object>();
		String companyId = GetOemcompanyId.getOemCompanyId(logonUser).toString();

		map.put("dealerId", dealerId);
		map.put("companyId", companyId);

		
		ProductManageDao productDao = new ProductManageDao();
		List<Map<String, Object>> types = productDao.getmyPriceTypeList(logonUser.getCompanyId(),dealerId);
		Map mymap=productDao.getMyMap(logonUser.getCompanyId(), dealerId);
		types.add(mymap);
		List<Map<String, Object>> priceList = types;
		String priceId = "" ;
		if (priceList != null && priceList.size() > 0) { 
			priceId = priceList.get(0).get("PRICE_ID").toString();
		}

	
		/*TmDealerPriceRelationPO relation = new TmDealerPriceRelationPO();
		relation.setDealerId(new Long(dealerId));
		relation.setIsDefault(Constant.IF_TYPE_YES);
		List<PO> rlist = select(relation);
		Long priceId = rlist.size() == 0 ? null : ((TmDealerPriceRelationPO)rlist.get(0)).getPriceId();*/
		
		StringBuffer sql= new StringBuffer();
		
		/*sql.append("SELECT TVMG3.GROUP_NAME SERIES_NAME,\n");
		sql.append("       TVSRD.DTL_ID,\n");  
		sql.append("       DECODE(TVSRD.SPECIAL_BATCH_NO,NULL,'',TVSRD.SPECIAL_BATCH_NO) BATCH_NO,\n");  
		sql.append("       TVMG.MODEL_CODE,\n");  
		sql.append("       TVMG.GROUP_ID,\n");  
		sql.append("       TVMG.GROUP_NAME,\n");  
		sql.append("       TVMG.GROUP_CODE,\n");
		sql.append("       F_GET_PRICE("+priceId+", TVSRD.MATERIAL_ID) AS PRICE,\n" );
		sql.append("TO_CHAR(TVSRD.EXPECTED_DATE,'YYYY-MM-DD') EXPECTED_DATE,\n");
		sql.append("      TVSRD.AMOUNT,\n");  
		sql.append("      NVL(TVSRD.EXPECTED_PERIOD, 0) EXPECTED_PERIOD\n");  
		sql.append(" FROM TM_VHCL_MATERIAL_GROUP   TVMG,\n");  
		sql.append("      TM_VHCL_MATERIAL_GROUP   TVMG2,\n");  
		sql.append("      TM_VHCL_MATERIAL_GROUP   TVMG3,\n");  
		sql.append("      TT_VS_SPECIAL_REQ_DTL    TVSRD\n");  
		sql.append("WHERE TVMG.PARENT_GROUP_ID = TVMG2.GROUP_ID\n");  
		sql.append("  AND TVMG2.PARENT_GROUP_ID = TVMG3.GROUP_ID\n");  
		sql.append("  AND TVMG.GROUP_ID = TVSRD.MATERIAL_ID\n");*/
		
		sql.append("SELECT TVMG3.GROUP_NAME SERIES_NAME,\n");
		sql.append("       TVSRD.DTL_ID,\n");  
		sql.append("       DECODE(TVSRD.SPECIAL_BATCH_NO, NULL, '', TVSRD.SPECIAL_BATCH_NO) BATCH_NO,\n");  
		sql.append("       TVMG.MODEL_CODE,\n");  
		sql.append("       TVMG.GROUP_ID,\n");  
		sql.append("       TVMG.GROUP_NAME,\n");  
		sql.append("       TVMG.GROUP_CODE,\n");  
		sql.append("       TVSRD.MATERIAL_ID,\n");  
		sql.append("       decode(tvpd.SALES_PRICE, null, 9000000, tvpd.SALES_PRICE) AS PRICE,\n");  
		sql.append("       tvpd.PRICE_ID,\n");  
		sql.append("       TO_CHAR(TVSRD.EXPECTED_DATE, 'YYYY-MM-DD') EXPECTED_DATE,\n");  
		sql.append("       TVSRD.AMOUNT,\n");  
		sql.append("       NVL(TVSRD.EXPECTED_PERIOD, 0) EXPECTED_PERIOD\n");  
		sql.append("  FROM TM_VHCL_MATERIAL_GROUP      TVMG,\n");  
		sql.append("       TM_VHCL_MATERIAL_GROUP      TVMG2,\n");  
		sql.append("       TM_VHCL_MATERIAL_GROUP      TVMG3,\n");  
		sql.append("       TT_VS_SPECIAL_REQ_DTL       TVSRD,\n");
		sql.append("	(\n");
		sql.append("SELECT VTVPD.PRICE_ID, VTVPD.SALES_PRICE, VTVPD.GROUP_ID\n");
		sql.append("  FROM TT_VS_SPECIAL_REQ_DTL TVSRD, VW_TT_VS_PRICE_DTL VTVPD\n");  
		sql.append(" WHERE TVSRD.MATERIAL_ID = VTVPD.GROUP_ID\n");  
		if(priceId != null && !"".equals(priceId)) {
			sql.append("   AND VTVPD.PRICE_ID = ");
			sql.append(priceId);
			sql.append("\n");  
		}
		if(reqId != null && !"".equals(reqId)) {
			sql.append("   AND TVSRD.REQ_ID = ");
			sql.append(reqId);
			sql.append("\n");
		} 
		sql.append("      ) TVPD\n");
		sql.append("WHERE TVMG.PARENT_GROUP_ID = TVMG2.GROUP_ID\n");
		sql.append("   AND TVMG2.PARENT_GROUP_ID = TVMG3.GROUP_ID\n");  
		sql.append("   AND TVMG.GROUP_ID = TVSRD.MATERIAL_ID\n");
		sql.append("and tvpd.group_id(+) = tvsrd.material_id\n");
		if(!"".equals(reqId)&&reqId!=null){
			sql.append(" AND TVSRD.REQ_ID = "+reqId+"\n");
		}
		sql.append("ORDER BY TVSRD.CREATE_DATE DESC\n");
		List<Map<String, Object>> list = dao.pageQuery(sql.toString(), null, getFunName());
		return list;
	}
	
	/**
	 * Function         : 订做车需求查询
	 * @param           : 业务范围ID
	 * @param           : 需求状态
	 * @param           : 需求提报日期
	 * @param           : 当前页码
	 * @param           : 每页显示记录数
	 * @return          : 满足条件的信息，包含分页
	 * @throws          : Exception
	 * LastUpdate       : 2010-06-17
	 */
	public PageResult<Map<String, Object>> getSpecialNeedList(Map<String, Object> map, String orgId, String areaId,String area,String needStatus,String startDate,String endDate,int curPage,int pageSize) throws Exception{
		String dealerId = map.get("dealerId").toString() ;
		String groupId = map.get("groupId").toString() ;
		
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT TVSR.REQ_ID,\n" );
		sql.append("       TMBA.AREA_NAME,\n" );
		sql.append("       TMD.DEALER_CODE,\n" );
		sql.append("       TMD.DEALER_SHORTNAME DEALER_NAME,\n" );
		sql.append("       TO_CHAR(TVSR.REQ_DATE, 'YYYY-MM-DD') REQ_DATE,\n" );
		sql.append("       TVSR.REFIT_DESC,\n" );
		sql.append("       TVSR.REQ_STATUS,\n" );
		sql.append("       SUM(TVSRD.AMOUNT) AMOUNT,\n" );
		sql.append("       TTO.ORDER_ID,\n");
		sql.append("       TTO.ORDER_NO,\n");
		sql.append("       TAT.TYPE_NAME\n");

		sql.append("FROM TT_VS_SPECIAL_REQ     TVSR,\n");
		sql.append("     TT_VS_SPECIAL_REQ_DTL TVSRD,\n");  
		sql.append("     TM_DEALER             TMD,\n");  
		sql.append("     TM_BUSINESS_AREA      TMBA,\n");  
		sql.append("     TT_VS_ORDER           TTO,\n");  
		sql.append("     TT_VS_ACCOUNT_TYPE    TAT\n");

		sql.append(" WHERE TVSR.REQ_ID = TVSRD.REQ_ID\n" );
		sql.append("   AND TVSR.DEALER_ID = TMD.DEALER_ID\n" );
		sql.append("   AND TVSR.AREA_ID = TMBA.AREA_ID\n" );
		sql.append("   AND TVSR.REQ_ID = TTO.SPECIAL_REQ_ID(+)\n");
		sql.append("   AND TTO.FUND_TYPE_ID = TAT.TYPE_ID(+)\n");

		sql.append("   AND TVSR.REQ_STATUS != "+Constant.SPECIAL_NEED_STATUS_01+"\n" );
		if(!"-1".equals(needStatus)&&needStatus!=null){
			sql.append("           AND TVSR.REQ_STATUS = "+needStatus+"\n" );
		}
		if(!"-1".equals(areaId)&&!"".equals(areaId)&&areaId!=null){
			sql.append("           AND TVSR.AREA_ID = "+areaId+"\n" );
		}
		if("-1".equals(areaId)&&!"".equals(area)&&area!=null){
			sql.append("           AND TVSR.AREA_ID IN( "+area+")\n" );
		}
		if(!"".equals(startDate)&&startDate!=null){
			sql.append("   AND TVSR.REQ_DATE >= TO_DATE('"+startDate+" 00:00:00','YYYY-MM-DD HH24:MI:SS')\n");
		}
		if(!"".equals(endDate)&&endDate!=null){
			sql.append("   AND TVSR.REQ_DATE <= TO_DATE('"+endDate+" 23:59:59','YYYY-MM-DD HH24:MI:SS')\n");
		}
		
		if(!CommonUtils.isNullString(dealerId)) {
			sql.append("   AND TVSR.DEALER_ID in (").append(dealerId).append(")\n");
		}
		
		if(!CommonUtils.isNullString(groupId)) {
			sql.append("   AND TVSRD.material_id in (").append(groupId).append(")\n");
		}
		
		if(!CommonUtils.isNullString(orgId)) {
			sql.append(ReqQueryDao.limitOrg("TMD", "dealer_id", orgId)) ;
		}
		
		sql.append(" GROUP BY TVSR.REQ_ID,\n" );
		sql.append("          TMBA.AREA_NAME,\n" );
		sql.append("          TMD.DEALER_CODE,\n" );
		sql.append("          TMD.DEALER_SHORTNAME,\n" );
		sql.append("          TVSR.REQ_DATE,\n" );
		sql.append("          TVSR.REFIT_DESC,\n" );
		sql.append("          TVSR.REQ_STATUS,\n" );
		sql.append("          TTO.ORDER_ID,\n");
		sql.append("          TTO.ORDER_NO,\n");
		sql.append("          TAT.TYPE_NAME\n");

		sql.append(" ORDER BY TVSR.REQ_DATE DESC\n");
		PageResult<Map<String, Object>> rs = dao.pageQuery(sql.toString(), null, getFunName(),pageSize, curPage);
		return rs;
	}
	
	/**
	 * Function         : 订做车需求查询
	 * @param           : 业务范围ID
	 * @param           : 需求状态
	 * @param           : 需求提报日期
	 * @param           : 当前页码
	 * @param           : 每页显示记录数
	 * @return          : 满足条件的信息，包含分页
	 * @throws          : Exception
	 * LastUpdate       : 2010-06-17
	 */
	public PageResult<Map<String, Object>> getDealerSpecialNeedList(String areaId,String dealerId,String area,String needStatus,String startDate,String endDate,int curPage,int pageSize) throws Exception{
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT TVSR.REQ_ID,\n" );
		sql.append("       TMBA.AREA_NAME,\n" );
		sql.append("       TMD.DEALER_CODE,\n" );
		sql.append("       TMD.DEALER_SHORTNAME DEALER_NAME,\n" );
		sql.append("       TO_CHAR(TVSR.REQ_DATE, 'YYYY-MM-DD') REQ_DATE,\n" );
		sql.append("       TVSR.REFIT_DESC,\n" );
		sql.append("       TVSR.REQ_STATUS,\n" );
		sql.append("       SUM(TVSRD.AMOUNT) AMOUNT,\n" );
		sql.append("       TTO.ORDER_ID,\n");
		sql.append("       TTO.ORDER_NO,\n");
		sql.append("       TAT.TYPE_NAME\n");

		sql.append("FROM TT_VS_SPECIAL_REQ     TVSR,\n");
		sql.append("     TT_VS_SPECIAL_REQ_DTL TVSRD,\n");  
		sql.append("     TM_DEALER             TMD,\n");  
		sql.append("     TM_BUSINESS_AREA      TMBA,\n");  
		sql.append("     TT_VS_ORDER           TTO,\n");  
		sql.append("     TT_VS_ACCOUNT_TYPE    TAT\n");

		sql.append(" WHERE TVSR.REQ_ID = TVSRD.REQ_ID\n" );
		sql.append("   AND TVSR.DEALER_ID = TMD.DEALER_ID\n" );
		sql.append("   AND TVSR.AREA_ID = TMBA.AREA_ID\n" );
		sql.append("   AND TVSR.REQ_ID = TTO.SPECIAL_REQ_ID(+)\n");
		sql.append("   AND TTO.FUND_TYPE_ID = TAT.TYPE_ID(+)\n");

		sql.append("   AND TVSR.REQ_STATUS != "+Constant.SPECIAL_NEED_STATUS_01+"\n" );
		if(!"-1".equals(needStatus)&&needStatus!=null){
			sql.append("           AND TVSR.REQ_STATUS = "+needStatus+"\n" );
		}
		if(!"-1".equals(areaId)&&!"".equals(areaId)&&areaId!=null){
			sql.append("           AND TVSR.AREA_ID = "+areaId+"\n" );
		}
		if(!"-1".equals(area)&&!"".equals(area)&&area!=null){
			sql.append("           AND TVSR.AREA_ID IN( "+area+")\n" );
		}
		if(!"-1".equals(dealerId) && !"".equals(dealerId) && dealerId != null){
			sql.append("           AND TVSR.DEALER_ID IN( "+dealerId+")\n" );
		}
		if(!"".equals(startDate)&&startDate!=null){
			sql.append("   AND TVSR.REQ_DATE >= TO_DATE('"+startDate+" 00:00:00','YYYY-MM-DD HH24:MI:SS')\n");
		}
		if(!"".equals(endDate)&&endDate!=null){
			sql.append("   AND TVSR.REQ_DATE <= TO_DATE('"+endDate+" 23:59:59','YYYY-MM-DD HH24:MI:SS')\n");
		}
		sql.append(" GROUP BY TVSR.REQ_ID,\n" );
		sql.append("          TMBA.AREA_NAME,\n" );
		sql.append("          TMD.DEALER_CODE,\n" );
		sql.append("          TMD.DEALER_SHORTNAME,\n" );
		sql.append("          TVSR.REQ_DATE,\n" );
		sql.append("          TVSR.REFIT_DESC,\n" );
		sql.append("          TVSR.REQ_STATUS,\n" );
		sql.append("          TTO.ORDER_ID,\n");
		sql.append("          TTO.ORDER_NO,\n");
		sql.append("          TAT.TYPE_NAME\n");

		sql.append(" ORDER BY TVSR.REQ_DATE DESC\n");
		PageResult<Map<String, Object>> rs = dao.pageQuery(sql.toString(), null, getFunName(),pageSize, curPage);
		return rs;
	}
	
	/**
	 * Function         : 订做车需求明细查询
	 * @param           : 需求ID
	 * @return          : 满足条件的信息
	 * @throws          : Exception
	 * LastUpdate       : 2010-06-17
	 */
	public List<Map<String, Object>> getSpecialNeedDetailList(String reqId) throws Exception{
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT TVMG3.GROUP_NAME SERIES_NAME,\n");
		sql.append("       TVSRD.DTL_ID,\n");  
		sql.append("       DECODE(NVL(TVSRD.SALES_PRICE,0)-NVL(TVSRD.CHANGE_PRICE,0),0,'',NVL(TVSRD.SALES_PRICE,0)-NVL(TVSRD.CHANGE_PRICE,0)) SALES_PRICE,\n");  
		sql.append("       DECODE(NVL(TVSRD.CHANGE_PRICE,0),0,'',NVL(TVSRD.CHANGE_PRICE,0)) CHANGE_PRICE,\n");  
		sql.append("       DECODE(TVSRD.SPECIAL_BATCH_NO,NULL,'',TVSRD.SPECIAL_BATCH_NO) BATCH_NO,\n");  
		sql.append("	   TVMG.MODEL_CODE,\n");
		sql.append("       TVMG.GROUP_ID,\n");  
		sql.append("       TVMG.GROUP_CODE,\n");  
		sql.append("       TVMG.GROUP_NAME,\n");  
		sql.append("     NVL(TVSRD.SALES_PRICE, 0) SP_PRICE,\n");  
		sql.append("       TO_CHAR(TVSRD.EXPECTED_DATE,'YYYY-MM-DD') EXPECTED_DATE,\n");  
		sql.append("       TVSRD.AMOUNT,\n");  
		sql.append("       NVL(TVSRD.EXPECTED_PERIOD, 0) EXPECTED_PERIOD\n");  
		sql.append("  FROM TM_VHCL_MATERIAL_GROUP   TVMG,\n");  
		sql.append("       TM_VHCL_MATERIAL_GROUP   TVMG2,\n");  
		sql.append("       TM_VHCL_MATERIAL_GROUP   TVMG3,\n");  
		sql.append("       TT_VS_SPECIAL_REQ_DTL    TVSRD\n");  
		sql.append(" WHERE TVMG.PARENT_GROUP_ID = TVMG2.GROUP_ID\n");  
		sql.append("   AND TVMG2.PARENT_GROUP_ID = TVMG3.GROUP_ID\n");  
		sql.append("   AND TVMG.GROUP_ID = TVSRD.MATERIAL_ID\n");
		if(!"".equals(reqId)&&reqId!=null){
			sql.append(" AND TVSRD.REQ_ID = "+reqId+"\n");
		}
		sql.append("ORDER BY TVSRD.CREATE_DATE DESC\n");
		List<Map<String, Object>> list = dao.pageQuery(sql.toString(), null, getFunName());
		return list;
	}
	
	public List<Map<String, Object>> getSpecialNeedDetailListNew(String reqId, String orderId) throws Exception{
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT TVMG3.GROUP_NAME SERIES_NAME,\n");
		sql.append("       TVSRD.DTL_ID,\n");  
		sql.append("       DECODE(NVL(TVSRD.SALES_PRICE,0)-NVL(TVSRD.CHANGE_PRICE,0),0,'',NVL(TVSRD.SALES_PRICE,0)-NVL(TVSRD.CHANGE_PRICE,0)) SALES_PRICE,\n");  
		sql.append("       DECODE(NVL(TVSRD.CHANGE_PRICE,0),0,'',NVL(TVSRD.CHANGE_PRICE,0)) CHANGE_PRICE,\n");  
		sql.append("       DECODE(TVSRD.SPECIAL_BATCH_NO,NULL,'',TVSRD.SPECIAL_BATCH_NO) BATCH_NO,\n");  
		sql.append("	   TVMG.MODEL_CODE,\n");
		sql.append("       TVMG.GROUP_ID,\n");  
		sql.append("       TVMG.GROUP_CODE,\n");  
		sql.append("       TVMG.GROUP_NAME,\n");  
		sql.append("     NVL(TVSRD.SALES_PRICE, 0) SP_PRICE,\n");  
		sql.append("       TO_CHAR(TVSRD.EXPECTED_DATE,'YYYY-MM-DD') EXPECTED_DATE,\n");  
		sql.append("       TVSRD.AMOUNT - nvl(tvo.order_amount, 0) AMOUNT,\n");  
		sql.append("       NVL(TVSRD.EXPECTED_PERIOD, 0) EXPECTED_PERIOD\n");  
		sql.append("  FROM TM_VHCL_MATERIAL_GROUP   TVMG,\n");  
		sql.append("       TM_VHCL_MATERIAL_GROUP   TVMG2,\n");  
		sql.append("       TM_VHCL_MATERIAL_GROUP   TVMG3,\n");  

		sql.append("(select tvo.special_req_id,\n") ;
		sql.append("               tvmgr.group_id,\n") ;
		sql.append("               sum(tvod.order_amount) order_amount\n") ;
		sql.append("          from tt_vs_order              tvo,\n") ;
		sql.append("               tt_vs_order_detail       tvod,\n") ;
		sql.append("               tm_vhcl_material_group_r tvmgr\n") ;
		sql.append("         where tvo.order_id = tvod.order_id\n") ;
		sql.append("           and tvod.material_id = tvmgr.material_id\n") ;
		sql.append("           and tvo.special_req_id = ").append(reqId).append("\n") ;
		sql.append("           and tvo.order_status <> ").append(Constant.ORDER_STATUS_06).append("\n") ;
		
		if(!CommonUtils.isNullString(orderId)) {
			sql.append("           and tvo.order_id <> ").append(orderId).append("\n") ;
		}
		
		sql.append("         group by tvo.special_req_id, tvmgr.group_id) tvo,") ;

		sql.append("       TT_VS_SPECIAL_REQ_DTL    TVSRD\n");  
		sql.append(" WHERE TVMG.PARENT_GROUP_ID = TVMG2.GROUP_ID\n");  
		sql.append("   AND TVMG2.PARENT_GROUP_ID = TVMG3.GROUP_ID\n");  
		sql.append("   AND TVMG.GROUP_ID = TVSRD.MATERIAL_ID\n");

		sql.append("   and tvsrd.req_id = tvo.special_req_id(+)\n") ;
		sql.append("   and tvsrd.material_id = tvo.group_id(+)") ;
		
		if(!"".equals(reqId)&&reqId!=null){
			sql.append(" AND TVSRD.REQ_ID = "+reqId+"\n");
		}
		sql.append("ORDER BY TVSRD.CREATE_DATE DESC\n");
		List<Map<String, Object>> list = dao.pageQuery(sql.toString(), null, getFunName());
		return list;
	}
	
	/**
	 * Function         : 订做车需求明细查询
	 * @param           : 需求ID
	 * @return          : 满足条件的信息
	 * @throws          : Exception
	 * LastUpdate       : 2010-06-17
	 */
	public List<Map<String, Object>> getSpecialNeedDetailListByQuery(String reqId) throws Exception{

		StringBuffer sql = new StringBuffer("\n") ;

		sql.append("select TVMG2.GROUP_NAME SERIES_NAME,\n") ;
		sql.append("       DECODE(NVL(TVSRD.CHANGE_PRICE, 0), 0, '', NVL(TVSRD.CHANGE_PRICE, 0)) CHANGE_PRICE,\n") ;
		sql.append("       DECODE(TVSRD.SPECIAL_BATCH_NO, NULL, '', TVSRD.SPECIAL_BATCH_NO) BATCH_NO,\n") ;
		sql.append("       TVMG3.GROUP_CODE MODEL_CODE,\n") ;
		sql.append("       TVMG4.Group_Code,\n") ;
		sql.append("       TVMG4.GROUP_NAME,\n") ;
		sql.append("       TVSRD.AMOUNT,\n") ;
		sql.append("       TO_CHAR(TVSRD.EXPECTED_DATE, 'YYYY-MM-DD') EXPECTED_DATE,\n") ;
		sql.append("       NVL(TVSRD.EXPECTED_PERIOD, 0) EXPECTED_PERIOD\n") ;
		sql.append("  from tt_vs_special_req_dtl  tvsrd,\n") ;
		sql.append("       tm_vhcl_material_group tvmg4,\n") ;
		sql.append("       tm_vhcl_material_group tvmg3,\n") ;
		sql.append("       tm_vhcl_material_group tvmg2\n") ;
		sql.append(" where tvsrd.material_id = tvmg4.group_id\n") ;
		sql.append("   and tvmg4.parent_group_id = tvmg3.group_id\n") ;
		sql.append("   and tvmg3.parent_group_id = tvmg2.group_id\n") ;
		
		if(!CommonUtils.isNullString(reqId)) {
			sql.append("   and tvsrd.req_id = ").append(reqId).append("") ;
		}

		List<Map<String, Object>> list = dao.pageQuery(sql.toString(), null, getFunName());
		return list;
	}
	
	/**
	 * Function         : 订做车需求确认查询
	 * @param           : 业务范围ID
	 * @param           : 需求提报日期
	 * @param           : 当前页码
	 * @param           : 每页显示记录数
	 * @return          : 满足条件的信息，包含分页
	 * @throws          : Exception
	 * LastUpdate       : 2010-06-17
	 */
	public PageResult<Map<String, Object>> getSpecialNeedConfirmQuery(String dealerId, String areaId,String area,String startDate,String endDate,int curPage,int pageSize) throws Exception{
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT TVSR.REQ_ID,\n" );
		sql.append("       TMBA.AREA_NAME,\n" );
		sql.append("       TMD.DEALER_CODE,\n" );
		sql.append("       TMD.DEALER_ID,\n");
		sql.append("       TMD.DEALER_SHORTNAME DEALER_NAME,\n" );
		sql.append("       TO_CHAR(TVSR.REQ_DATE, 'YYYY-MM-DD') REQ_DATE,\n" );
		sql.append("       TVSR.REFIT_DESC,\n" );
		sql.append("       TVSR.REQ_STATUS,\n" );
		sql.append("       SUM(TVSRD.AMOUNT) AMOUNT\n" );
		sql.append("  FROM TT_VS_SPECIAL_REQ TVSR, TT_VS_SPECIAL_REQ_DTL TVSRD, TM_DEALER TMD,TM_BUSINESS_AREA TMBA\n" );
		sql.append(" WHERE TVSR.REQ_ID = TVSRD.REQ_ID\n" );
		sql.append("   AND TVSR.DEALER_ID = TMD.DEALER_ID\n" );
		sql.append("   AND TVSR.AREA_ID = TMBA.AREA_ID\n" );
		sql.append("   AND TVSR.REQ_STATUS = "+Constant.SPECIAL_NEED_STATUS_04+"\n" );
		
		if(!CommonUtils.isNullString(dealerId)) {
			sql.append("   AND TVSR.DEALER_ID in ("+dealerId+")\n" );
		}
		
		if(!"-1".equals(areaId)&&!"".equals(areaId)&&areaId!=null){
			sql.append("           AND TVSR.AREA_ID = "+areaId+"\n" );
		}
		if(!"-1".equals(area)&&!"".equals(area)&&area!=null){
			sql.append("           AND TVSR.AREA_ID IN( "+area+")\n" );
		}
		if(!"".equals(startDate)&&startDate!=null){
			sql.append("   AND TVSR.REQ_DATE >= TO_DATE('"+startDate+" 00:00:00','YYYY-MM-DD HH24:MI:SS')\n");
		}
		if(!"".equals(endDate)&&endDate!=null){
			sql.append("   AND TVSR.REQ_DATE <= TO_DATE('"+endDate+" 23:59:59','YYYY-MM-DD HH24:MI:SS')\n");
		}
		sql.append(" GROUP BY TVSR.REQ_ID,\n" );
		sql.append("          TMBA.AREA_NAME,\n" );
		sql.append("          TMD.DEALER_CODE,\n" );
		sql.append("          TMD.DEALER_SHORTNAME,\n" );
		sql.append("          TVSR.REQ_DATE,\n" );
		sql.append("          TVSR.REFIT_DESC,\n" );
		sql.append("          TMD.DEALER_ID,\n");
		sql.append("          TVSR.REQ_STATUS\n" );
		sql.append(" ORDER BY TVSR.REQ_DATE DESC\n");
		PageResult<Map<String, Object>> rs = dao.pageQuery(sql.toString(), null, getFunName(),pageSize, curPage);
		return rs;
	}
	/**
	 * Function         : 订做车需求生成订单查询
	 * @param           : 需求ID
	 * @return          : 满足条件的信息
	 * @throws          : Exception
	 * LastUpdate       : 2010-06-18
	 */
	public List<Map<String, Object>> getSpecialNeedtoReportDetail(String reqId, String companyId) throws Exception{
		StringBuffer sql= new StringBuffer();
		
		sql.append("SELECT A.SERIES_NAME,\n");
		sql.append("       A.GROUP_ID,\n");  
		sql.append("       A.GROUP_CODE,\n");  
		sql.append("       A.GROUP_NAME,\n"); 
		sql.append("	   A.MODEL_CODE,\n");
		sql.append("       A.SPECIAL_BATCH_NO,\n");  
		sql.append("       A.APPLY_AMOUNT,\n");  
		sql.append("       A.AMOUNT - nvl(c.ORDER_AMOUNT, 0) AMOUNT,\n");  
		sql.append("       A.DTL_ID,\n");  
		sql.append("       nvl(c.ORDER_AMOUNT, 0) ORDER_AMOUNT,\n");  
		sql.append("       A.SALES_PRICE,\n");  
		sql.append("	   A.CHANGE_PRICE\n");
		/*sql.append("       CASE\n");  
		sql.append("         WHEN NVL(B.AVA_STOCK, 0) - NVL(C.ORDER_AMOUNT, 0) <= 0 THEN\n");  
		sql.append("          '无'\n");  
		sql.append("         ELSE\n");  
		sql.append("          '有'\n");  
		sql.append("       END RESOURCE_AMOUNT\n"); 
		*/ 
		sql.append("  FROM (SELECT TVMG3.GROUP_NAME SERIES_NAME,\n");  
		sql.append("               TVMG.GROUP_ID,\n");  
		sql.append("               TVMG.GROUP_CODE,\n");  
		sql.append("               TVMG.GROUP_NAME,\n");  
		sql.append("			   TVMG2.GROUP_CODE MODEL_CODE,\n");
		sql.append("			   TVDRD.CHANGE_PRICE,\n");
		sql.append("               NVL(TVDRD.SPECIAL_BATCH_NO, '') SPECIAL_BATCH_NO,\n");  
		sql.append("               TVDRD.AMOUNT APPLY_AMOUNT,\n");  
		sql.append("               TVDRD.AMOUNT,\n");  
		sql.append("               TVDRD.DTL_ID,\n");  
		sql.append("               NVL(TVDRD.ORDER_AMOUNT, 0) ORDER_AMOUNT,\n");  
		sql.append("               TVDRD.SALES_PRICE\n");  
		sql.append("          FROM TT_VS_SPECIAL_REQ_DTL    TVDRD,\n");  
		sql.append("               TM_VHCL_MATERIAL_GROUP   TVMG,\n");  
		sql.append("               TM_VHCL_MATERIAL_GROUP   TVMG2,\n");  
		sql.append("               TM_VHCL_MATERIAL_GROUP   TVMG3\n");  
		sql.append("         WHERE TVDRD.MATERIAL_ID = TVMG.GROUP_ID\n");  
		sql.append("           AND TVMG.PARENT_GROUP_ID = TVMG2.GROUP_ID\n");  
		sql.append("           AND TVMG2.PARENT_GROUP_ID = TVMG3.GROUP_ID\n");  
		sql.append("           AND TVDRD.REQ_ID = " + reqId + ") A,\n");  
		/*sql.append("       (SELECT TVMGR.GROUP_ID,\n");  
		sql.append("               NVL(VVR.SPECIAL_BATCH_NO, '') SPECIAL_BATCH_NO,\n");  
		sql.append("               SUM(VVR.AVA_STOCK) AVA_STOCK\n");  
		sql.append("          FROM VW_VS_RESOURCE VVR,\n");  
		sql.append("               TM_VHCL_MATERIAL_GROUP_R TVMGR\n");  
		sql.append("         WHERE VVR.COMPANY_ID = " + companyId +	"\n");  
		sql.append("         AND VVR.MATERIAL_ID = TVMGR.MATERIAL_ID\n");  
		sql.append("         GROUP BY TVMGR.GROUP_ID, VVR.SPECIAL_BATCH_NO) B,\n");  */
		sql.append("       ( SELECT TVMGR.GROUP_ID,\n");  
		sql.append("               NVL(TVDRD.PATCH_NO, '') PATCH_NO,\n");  
		sql.append("               SUM(TVDRD.REQ_AMOUNT) ORDER_AMOUNT\n");  
		sql.append("          FROM TT_VS_ORDER         TVO,\n");  
		sql.append("               TT_VS_DLVRY_REQ     TVDR,\n");  
		sql.append("               TT_VS_DLVRY_REQ_DTL TVDRD,\n");  
		sql.append("               TM_VHCL_MATERIAL_GROUP_R TVMGR\n");  
		sql.append("         WHERE TVO.ORDER_ID = TVDR.ORDER_ID\n");  
		sql.append("           AND TVDR.REQ_ID = TVDRD.REQ_ID\n");  
		sql.append("           AND TVDR.REQ_STATUS <> 11571007\n");  
		sql.append("	AND TVO.SPECIAL_REQ_ID = "+reqId+"\n");
		sql.append("          AND TVO.SPECIAL_REQ_ID = TVDRD.REQ_ID\n");
		sql.append("           AND TVO.COMPANY_ID = " + companyId + "\n");  
		sql.append("           AND TVDRD.MATERIAL_ID = TVMGR.MATERIAL_ID\n");  
		sql.append("         GROUP BY TVMGR.GROUP_ID, TVDRD.PATCH_NO) C\n");  
		sql.append(" WHERE A.GROUP_ID = C.GROUP_ID(+)\n");  
		sql.append("   AND A.SPECIAL_BATCH_NO = C.PATCH_NO(+)\n");


		/*sql.append("SELECT A.SERIES_NAME,\n");
		sql.append("       A.MATERIAL_ID,\n");  
		sql.append("       A.MATERIAL_CODE,\n");  
		sql.append("       A.MATERIAL_NAME,\n");  
		sql.append("       A.SPECIAL_BATCH_NO,\n");  
		sql.append("       A.APPLY_AMOUNT,\n");  
		sql.append("       A.AMOUNT,\n");  
		sql.append("       A.DTL_ID,\n");  
		sql.append("       A.ORDER_AMOUNT,\n");  
		sql.append("       A.SALES_PRICE,\n");  
		sql.append("       CASE\n");  
		sql.append("         WHEN NVL(B.AVA_STOCK, 0) - NVL(C.ORDER_AMOUNT, 0) <= 0 THEN\n");  
		sql.append("          '无'\n");  
		sql.append("         ELSE\n");  
		sql.append("          '有'\n");  
		sql.append("       end RESOURCE_AMOUNT\n");  
		sql.append("  FROM (SELECT TVMG3.GROUP_NAME SERIES_NAME,\n");  
		sql.append("               TVM.MATERIAL_ID,\n");  
		sql.append("               TVM.MATERIAL_CODE,\n");  
		sql.append("               TVM.MATERIAL_NAME,\n");  
		sql.append("               NVL(TVDRD.SPECIAL_BATCH_NO, '') SPECIAL_BATCH_NO,\n");  
		sql.append("               TVDRD.AMOUNT APPLY_AMOUNT,\n"); 
		sql.append("               TVDRD.AMOUNT - NVL(TVDRD.ORDER_AMOUNT, 0) AMOUNT,\n"); 
		sql.append("               TVDRD.DTL_ID,\n");   
		sql.append("               NVL(TVDRD.ORDER_AMOUNT, 0) ORDER_AMOUNT,\n");   
		sql.append("               TVDRD.SALES_PRICE\n");  
		sql.append("          FROM TT_VS_SPECIAL_REQ_DTL    TVDRD,\n");  
		sql.append("               TM_VHCL_MATERIAL         TVM,\n");  
		sql.append("               TM_VHCL_MATERIAL_GROUP_R TVMGR,\n");  
		sql.append("               TM_VHCL_MATERIAL_GROUP   TVMG1,\n");  
		sql.append("               TM_VHCL_MATERIAL_GROUP   TVMG2,\n");  
		sql.append("               TM_VHCL_MATERIAL_GROUP   TVMG3\n");  
		sql.append("         WHERE TVDRD.MATERIAL_ID = TVM.MATERIAL_ID\n");  
		sql.append("           AND TVDRD.MATERIAL_ID = TVMGR.MATERIAL_ID\n");  
		sql.append("           AND TVMGR.GROUP_ID = TVMG1.GROUP_ID\n");  
		sql.append("           AND TVMG1.PARENT_GROUP_ID = TVMG2.GROUP_ID\n");  
		sql.append("           AND TVMG2.PARENT_GROUP_ID = TVMG3.GROUP_ID\n");  
		sql.append("           AND TVDRD.REQ_ID = "+reqId+") A,\n");  
		sql.append("       (SELECT VVR.MATERIAL_ID,\n");  
		sql.append("               NVL(VVR.SPECIAL_BATCH_NO, '') SPECIAL_BATCH_NO,\n");  
		sql.append("               SUM(VVR.AVA_STOCK) AVA_STOCK\n");  
		sql.append("          FROM VW_VS_RESOURCE VVR\n");  
		sql.append("         WHERE VVR.COMPANY_ID = "+companyId+"\n");  
		sql.append("         GROUP BY VVR.MATERIAL_ID, VVR.SPECIAL_BATCH_NO) B,\n");  
		sql.append("       (SELECT TVDRD.MATERIAL_ID,\n");  
		sql.append("               NVL(TVDRD.PATCH_NO, '') PATCH_NO,\n");  
		sql.append("               SUM(TVDRD.REQ_AMOUNT) ORDER_AMOUNT\n");  
		sql.append("          FROM TT_VS_ORDER         TVO,\n");  
		sql.append("               TT_VS_DLVRY_REQ     TVDR,\n");  
		sql.append("               TT_VS_DLVRY_REQ_DTL TVDRD\n");  
		sql.append("         WHERE TVO.ORDER_ID = TVDR.ORDER_ID\n");  
		sql.append("           AND TVDR.REQ_ID = TVDRD.REQ_ID\n");  
		sql.append("           AND TVDR.REQ_STATUS = "+Constant.ORDER_REQ_STATUS_01+"\n");  
		sql.append("           AND TVO.COMPANY_ID = "+companyId+"\n");  
		sql.append("         GROUP BY TVDRD.MATERIAL_ID, TVDRD.PATCH_NO) C\n");  
		sql.append(" WHERE A.MATERIAL_ID = B.MATERIAL_ID(+)\n");  
		sql.append("   AND A.SPECIAL_BATCH_NO = B.SPECIAL_BATCH_NO(+)\n");  
		sql.append("   AND A.MATERIAL_ID = C.MATERIAL_ID(+)\n");  
		sql.append("   AND A.SPECIAL_BATCH_NO = C.PATCH_NO(+)");*/

		
		/*sql.append("SELECT T.MATERIAL_ID,\n" );
		sql.append("       T.SERIES_NAME,\n" );
		sql.append("       T.MATERIAL_CODE,\n" );
		sql.append("       T.MATERIAL_NAME,\n" );
		sql.append("       T.BATCH_NO,\n" );
		sql.append("       T.AMOUNT,\n" );
		sql.append("       T.SALES_PRICE,\n" );
		sql.append("       T.TOTAIL_PRICE,\n" );
		sql.append("       DECODE(NVL(TVR.RESOURCE_AMOUNT, 0), 0, '无', '有') RESOURCE_AMOUNT\n" );
		sql.append("  FROM (SELECT TVMG3.GROUP_NAME SERIES_NAME,\n" );
		sql.append("               TVSRD.DTL_ID,\n" );
		sql.append("               DECODE(TVSRD.SALES_PRICE, NULL, '', TVSRD.SALES_PRICE) SALES_PRICE,\n" );
		sql.append("               DECODE(TVSRD.SPECIAL_BATCH_NO,\n" );
		sql.append("                      NULL,\n" );
		sql.append("                      '',\n" );
		sql.append("                      TVSRD.SPECIAL_BATCH_NO) BATCH_NO,\n" );
		sql.append("               TVM.MATERIAL_ID,\n" );
		sql.append("               TVM.MATERIAL_CODE,\n" );
		sql.append("               TVM.MATERIAL_NAME,\n" );
		sql.append("               TVSRD.AMOUNT,\n" );
		sql.append("               (TVSRD.AMOUNT * TVSRD.SALES_PRICE) TOTAIL_PRICE\n" );
		sql.append("          FROM TM_VHCL_MATERIAL_GROUP   TVMG1,\n" );
		sql.append("               TM_VHCL_MATERIAL_GROUP   TVMG2,\n" );
		sql.append("               TM_VHCL_MATERIAL_GROUP   TVMG3,\n" );
		sql.append("               TM_VHCL_MATERIAL         TVM,\n" );
		sql.append("               TM_VHCL_MATERIAL_GROUP_R TVMGR,\n" );
		sql.append("               TT_VS_SPECIAL_REQ_DTL    TVSRD,\n" );
		sql.append("               TT_VS_SPECIAL_REQ        TVSR\n" );
		sql.append("         WHERE TVMG1.PARENT_GROUP_ID = TVMG2.GROUP_ID\n" );
		sql.append("           AND TVMG2.PARENT_GROUP_ID = TVMG3.GROUP_ID\n" );
		sql.append("           AND TVMG1.GROUP_ID = TVMGR.GROUP_ID\n" );
		sql.append("           AND TVMGR.MATERIAL_ID = TVM.MATERIAL_ID\n" );
		sql.append("           AND TVSRD.MATERIAL_ID = TVM.MATERIAL_ID\n" );
		if(!"".equals(reqId)&&reqId!=null){
			sql.append("       AND TVSR.REQ_ID = "+reqId+"\n");
		}
		sql.append("           AND TVSR.REQ_ID = TVSRD.REQ_ID) T\n" );
		sql.append("  LEFT JOIN TT_VS_RESOURCE TVR ON T.MATERIAL_ID = TVR.MATERIAL_ID\n" );
		sql.append("                              AND T.BATCH_NO = TVR.SPECIAL_BATCH_NO");*/
		List<Map<String, Object>> list = dao.pageQuery(sql.toString(), null, getFunName());
		return list;
	}
	/**
	 * Function         : 经销商账户可用余额查询
	 * @param           : 经销商ID
	 * @return          : 经销商账户信息
	 * @throws          : Exception
	 * LastUpdate       : 2010-06-18
	 */
	public List<Map<String, Object>> getDealerAccount(String dealerId)throws Exception{
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT TSA.ACCOUNT_ID,TSA.AVAILABLE_AMOUNT, TSA.FREEZE_AMOUNT, TSAT.TYPE_ID,TSAT.TYPE_NAME\n" );
		sql.append("  FROM TT_VS_ACCOUNT TSA, TT_VS_ACCOUNT_TYPE TSAT\n" );
		sql.append(" WHERE TSA.ACCOUNT_TYPE_ID = TSAT.TYPE_ID\n" );
		sql.append("   AND TSA.DEALER_ID IN("+dealerId+")\n");
		sql.append("   AND TSAT.STATUS ="+Constant.STATUS_ENABLE+"\n" );
		List<Map<String, Object>> list= dao.pageQuery(sql.toString(), null, getFunName());
		return list;
	}
	
	/**
	 * Function         : 经销商地址查询
	 * @param           : 经销商ID
	 * @return          : 经销商地址信息
	 * @throws          : Exception
	 * LastUpdate       : 2010-06-18
	 */
	public List<Map<String, Object>> getDealerAddress(String dealerId)throws Exception{
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT TMA.ID, TMA.ADDRESS,TMA.LINK_MAN,TMA.TEL\n" );
		sql.append("  FROM TM_VS_ADDRESS TMA\n" );
		sql.append("   WHERE TMA.DEALER_ID IN("+dealerId+")\n");
		sql.append("   AND TMA.STATUS ="+Constant.STATUS_ENABLE+"\n" );
		List<Map<String, Object>> list= dao.pageQuery(sql.toString(), null, getFunName());
		return list;
	}
	/**
	 * Function         : 业务配置参数PO
	 * @param 			: 参数Id
	 * @return			：获得参数PO
	 * @throws          : Exception
	 * LastUpdate       : 2010-06-18
	 */
	public TmBusinessParaPO getTmBusinessParaPO(Integer paraId) {
		TmBusinessParaPO po = new TmBusinessParaPO();
		po.setParaId(paraId);
		List<PO> list = select(po);
		return list.size() != 0 ? (TmBusinessParaPO) list.get(0) : null;
	}
	/**
	 * Function         : 业务配置参数PO
	 * @param 			: 业务配置参数PO
	 * @return			：获得参数PO
	 * @throws          : Exception
	 * LastUpdate       : 2010-06-18
	 */
	public TmDateSetPO geTmDateSetPO(TmDateSetPO po) {
		List<PO> list = select(po);
		return list.size() != 0 ? (TmDateSetPO) list.get(0) : null;
	}
	/**
	 * 获得业务配置参数PO
	 * 
	 * @param paraId
	 * @return
	 */
	public TmBusinessParaPO getTmBusinessParaPO(Integer paraId, Long companyId) {
		TmBusinessParaPO po = new TmBusinessParaPO();
		po.setParaId(paraId);
		List<PO> list = select(po);
		return list.size() != 0 ? (TmBusinessParaPO) list.get(0) : null;
	}
	/**
	 * 获得TmDateSetPO
	 * 
	 * @param po
	 * @return
	 */
	public TmDateSetPO getTmDateSetPO(TmDateSetPO po) {
		List<PO> list = select(po);
		return list.size() != 0 ? (TmDateSetPO) list.get(0) : null;
	}

	public TmDateSetPO getTmDateSetPO(Date date, Long companyId) {
		SimpleDateFormat formate = new SimpleDateFormat("yyyyMMdd");
		String dayStr = formate.format(date);
		// 查看日期配置表中当天的记录
		TmDateSetPO dateSet = new TmDateSetPO();
		dateSet.setSetDate(dayStr);
		dateSet.setCompanyId(companyId);
		List<PO> list = select(dateSet);
		return list.size() != 0 ? (TmDateSetPO) list.get(0) : null;
	}
	/**
	 * 补充订单周度列表
	 * 
	 * @param companyId
	 * @return
	 */
	public List<Map<String, Object>> getNastyDateList(Long companyId) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		// 查看日期配置表中当天的记录
		TmDateSetPO dateSet = getTmDateSetPO(new Date(), companyId);
		if (dateSet != null) {
			TmBusinessParaPO para1 = getTmBusinessParaPO(
					Constant.URGENT_ORDER_WEEK_BEFORE_PARA, new Long(companyId));// 补充订单提报提前周度参数
			TmBusinessParaPO para2 = getTmBusinessParaPO(
					Constant.URGENT_ORDER_WEEK_PARA, new Long(companyId));// 补充订单提报周度参数

			// 获得提报起始周
			Calendar c = Calendar.getInstance();
			c.add(Calendar.DATE, 7 * Integer.parseInt(para1.getParaValue()));
			dateSet = getTmDateSetPO(c.getTime(), companyId);
			if (dateSet != null) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("code", dateSet.getSetYear() + "-"
						+ dateSet.getSetWeek());
				map.put("name", dateSet.getSetYear() + "年"
						+ dateSet.getSetWeek() + "周");
				list.add(map);

				for (int i = 1; i < Integer.parseInt(para2.getParaValue()); i++) {
					c.add(Calendar.DATE, 7);
					dateSet = getTmDateSetPO(c.getTime(), companyId);
					if (dateSet != null) {
						map = new HashMap<String, Object>();
						map.put("code", dateSet.getSetYear() + "-"
								+ dateSet.getSetWeek());
						map.put("name", dateSet.getSetYear() + "年"
								+ dateSet.getSetWeek() + "周");
						list.add(map);
					}
				}
			}
		}

		return list;
	}
	
	public PageResult<Map<String, Object>> selectFleet(Long companyId,String fleetName,int curPage,int pageSize) throws Exception{
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT TMF.FLEET_ID, TMF.FLEET_NAME\n" );
		sql.append("  FROM TM_FLEET TMF\n" );
		sql.append(" WHERE TMF.DLR_COMPANY_ID = "+companyId+"\n" );
		if(!"".equals(fleetName)&&fleetName!=null){
			sql.append("   AND TMF.FLEET_NAME LIKE '%"+fleetName+"%'\n" );
		}
		sql.append("   AND TMF.STATUS = "+Constant.FLEET_INFO_TYPE_03+"\n");
		PageResult<Map<String, Object>> rs = dao.pageQuery(sql.toString(), null, getFunName(),pageSize, curPage);
		return rs;
	}
	
	/**
	 * Function         : 订做车需求核价明细查询,当改变价格类型时，调用此方法
	 * @param           : 需求ID
	 * @return          : 满足条件的信息
	 * @throws          : Exception
	 * LastUpdate       : 2010-06-17
	 */
	public List<Map<String, Object>> getSpecialNeedDetailPriceCheckListA(String reqId,String priceId,String dtlId) throws Exception{
		StringBuffer sql= new StringBuffer();
		/*sql.append("SELECT TVMG3.GROUP_NAME SERIES_NAME,\n" );
		sql.append("       TVSRD.DTL_ID,\n" );
		sql.append("       DECODE(TVSRD.SPECIAL_BATCH_NO,NULL,'',TVSRD.SPECIAL_BATCH_NO) BATCH_NO,\n" );
		sql.append("	  TVMG.MODEL_CODE,\n");
		sql.append("      TVMG.GROUP_ID,\n");  
		sql.append("      TVMG.GROUP_NAME,\n");  
		sql.append("      TVMG.GROUP_CODE,\n");
		sql.append("       F_GET_PRICE("+priceId+", TVSRD.MATERIAL_ID) AS PRICE,\n" );
		sql.append("       TO_CHAR(TVSRD.EXPECTED_DATE,'YYYY-MM-DD') EXPECTED_DATE,\n" );
		sql.append("       TVSRD.AMOUNT,\n" );
		sql.append("       NVL(TVSRD.EXPECTED_PERIOD, 0) EXPECTED_PERIOD\n");
		sql.append("  FROM TM_VHCL_MATERIAL_GROUP   TVMG,\n" );
		sql.append("       TM_VHCL_MATERIAL_GROUP   TVMG2,\n" );
		sql.append("       TM_VHCL_MATERIAL_GROUP   TVMG3,\n" );
		sql.append("       TT_VS_SPECIAL_REQ_DTL    TVSRD\n" );
		sql.append(" WHERE TVMG.PARENT_GROUP_ID = TVMG2.GROUP_ID\n" );
		sql.append("   AND TVMG2.PARENT_GROUP_ID = TVMG3.GROUP_ID\n" );
		sql.append("AND TVMG.GROUP_ID = TVSRD.MATERIAL_ID\n");
		if(!"".equals(reqId)&&reqId!=null){
			sql.append(" AND TVSRD.REQ_ID = "+reqId+"\n");
		}
		if(null != dtlId && !"".equals(dtlId)) {
			sql.append("   AND TVSRD.DTL_ID = " + dtlId + "\n" );
		}
		sql.append("ORDER BY TVSRD.CREATE_DATE DESC\n");*/
		sql.append("SELECT TVMG3.GROUP_NAME SERIES_NAME,\n");
		sql.append("       TVSRD.DTL_ID,\n");  
		sql.append("       DECODE(TVSRD.SPECIAL_BATCH_NO, NULL, '', TVSRD.SPECIAL_BATCH_NO) BATCH_NO,\n");  
		sql.append("       TVMG.MODEL_CODE,\n");  
		sql.append("       TVMG.GROUP_ID,\n");  
		sql.append("       TVMG.GROUP_NAME,\n");  
		sql.append("       TVMG.GROUP_CODE,\n");  
		sql.append("       TVSRD.MATERIAL_ID,\n");  
		sql.append("       DECODE(TVPD.SALES_PRICE, NULL, 9000000, TVPD.SALES_PRICE) AS PRICE,\n");  
		sql.append("       TO_CHAR(TVSRD.EXPECTED_DATE, 'YYYY-MM-DD') EXPECTED_DATE,\n");  
		sql.append("       TVSRD.AMOUNT,\n");  
		sql.append("       NVL(TVSRD.EXPECTED_PERIOD, 0) EXPECTED_PERIOD\n");  
		sql.append("  FROM TM_VHCL_MATERIAL_GROUP      TVMG,\n");  
		sql.append("       TM_VHCL_MATERIAL_GROUP      TVMG2,\n");  
		sql.append("       TM_VHCL_MATERIAL_GROUP      TVMG3,\n");  
		sql.append("       TT_VS_SPECIAL_REQ_DTL       TVSRD,\n");  
		sql.append("       (\n");  
		sql.append("SELECT VTVPD.PRICE_ID, VTVPD.SALES_PRICE, VTVPD.GROUP_ID\n");
		sql.append("  FROM TT_VS_SPECIAL_REQ_DTL TVSRD, VW_TT_VS_PRICE_DTL VTVPD\n");  
		sql.append(" WHERE TVSRD.MATERIAL_ID = VTVPD.GROUP_ID\n");  
		if(priceId != null && !"".equals(priceId)) {
			sql.append("   AND VTVPD.PRICE_ID = ");
			sql.append(priceId);
			sql.append("\n");  
		}
		if(reqId != null && !"".equals(reqId)) {
			sql.append("   AND TVSRD.REQ_ID = ");
			sql.append(reqId);
			sql.append("\n");
		}  
		sql.append("     ) TVPD\n");

		sql.append("WHERE TVMG.PARENT_GROUP_ID = TVMG2.GROUP_ID\n");
		sql.append("   AND TVMG2.PARENT_GROUP_ID = TVMG3.GROUP_ID\n");  
		sql.append("   AND TVMG.GROUP_ID = TVSRD.MATERIAL_ID\n");  
		if(!"".equals(reqId)&&reqId!=null){
			sql.append(" AND TVSRD.REQ_ID = "+reqId+"\n");
		}
		if(null != dtlId && !"".equals(dtlId)) {
			sql.append("   AND TVSRD.DTL_ID = " + dtlId + "\n" );
		} 
		sql.append("   AND TVPD.GROUP_ID(+) = TVSRD.MATERIAL_ID\n");  
		sql.append(" ORDER BY TVSRD.CREATE_DATE DESC\n");

		List<Map<String, Object>> list = dao.pageQuery(sql.toString(), null, getFunName());
		return list;
	}
	//价格列表名称
	public String getPriceName(Long priceId){
		String priceName = "";
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT PRICE_DESC FROM vw_TT_VS_PRICE WHERE PRICE_ID = ").append(priceId);
		Map<String, Object> map = pageQueryMap(sql.toString(), null, getFunName());
		if(map!=null&&map.size()>0){
			priceName = String.valueOf(map.get("PRICE_DESC"));
		}
		return priceName;
	}
	
	/**
	 * 获得TtVsSpecialReqPO
	 * @param reqId
	 * @return
	 */
	public TtVsSpecialReqPO getTtVsSpecialReqPO(Long reqId) {
		TtVsSpecialReqPO po = new TtVsSpecialReqPO();
		po.setReqId(reqId);
		List<PO> list = select(po);
		return list.size() != 0 ? (TtVsSpecialReqPO) list.get(0) : null;
	}
	
	public List<Map<String,Object>> getPriceId(String reqId) {
		StringBuffer sql = new StringBuffer() ;
		
		sql.append("select tvo.price_id\n");
		sql.append("          from tt_vs_order tvo\n");  
		sql.append("         where tvo.special_req_id = " + reqId + "\n");
		
		return dao.pageQuery(sql.toString(), null, getFunName());
	}
	
	/**
	 * 
	 */
	public Double getPreAmountP() {
		Double preAmountP = new Double(1) ;
		StringBuffer sql = new StringBuffer() ;
		
		sql.append("select tmbp.para_value\n");
		sql.append("	from tm_business_para tmbp\n");  
		sql.append("	where tmbp.para_id = " + Constant.ADVANCE_ID + "\n");
		
		List<Map<String, Object>> list = dao.pageQuery(sql.toString(), null, getFunName()) ;
		if(null != list && list.size() > 0) {
			preAmountP = Double.parseDouble(list.get(0).get("PARA_VALUE").toString().split("%")[0] ) / 100 ;
		}
		
		return preAmountP ;
	}
	
	public List<Map<String, Object>>  getAttachInfos(String ywzj){
		
		String sql = "SELECT F.FJID,F.FILEURL,F.FILENAME FROM FS_FILEUPLOAD F WHERE F.YWZJ ="+ywzj+"ORDER BY F.FJID";

		List<Map<String, Object>> list = pageQuery(sql,null,getFunName());
		return list;
	}
	
	public List<Map<String, Object>> getSalesOrderPreCheckDetailList(String orderId, String companyId, String dealerId) {

		StringBuffer sql = new StringBuffer();

		sql.append("SELECT TVMG3.GROUP_CODE SERIES_CODE,\n");
		sql.append("       TVMG3.GROUP_NAME SERIES_NAME,\n");
		sql.append("       TVM.MATERIAL_ID,\n");
		sql.append("       TVM.MATERIAL_CODE,\n");
		sql.append("       TVM.MATERIAL_NAME,\n");
		sql.append("       NVL(TVR.RESOURCE_AMOUNT, 0) RESOURCE_AMOUNT,\n");
		sql.append("       TVM.MATERIAL_ID,\n");
		sql.append("       TVOD.ORDER_AMOUNT,\n");
		sql.append("       TVSRD.SALES_PRICE SINGLE_PRICE,\n");
		sql.append("       TVOD.TOTAL_PRICE,\n");
		sql.append("       TVOD.DETAIL_ID\n");
		sql.append("  FROM TM_VHCL_MATERIAL_GROUP TVMG1,\n");
		sql.append("       TM_VHCL_MATERIAL_GROUP TVMG2,\n");
		sql.append("       TM_VHCL_MATERIAL_GROUP TVMG3,\n");
		sql.append("       TM_VHCL_MATERIAL TVM,\n");
		sql.append("       TM_VHCL_MATERIAL_GROUP_R TVMGR,\n");
		sql.append("       TT_VS_SPECIAL_REQ_DTL tvsrd,\n");
		sql.append("       TT_VS_ORDER TVO,\n");
		sql.append("       TT_VS_ORDER_DETAIL TVOD,\n");
		sql.append("       (SELECT MATERIAL_ID, COUNT(1) AS RESOURCE_AMOUNT\n");
		sql.append("          FROM TM_VEHICLE\n");
		sql.append("         WHERE OEM_COMPANY_ID = " + companyId + "\n");
		sql.append("           AND DEALER_ID IN (" + dealerId + ")\n");
		sql.append("           AND LOCK_STATUS = "+Constant.LOCK_STATUS_01+"\n");
		sql.append("           AND LIFE_CYCLE IN (" + Constant.VEHICLE_LIFE_03 + ", " + Constant.VEHICLE_LIFE_05 + ")\n");
		sql.append("           AND SPECIAL_BATCH_NO IS NULL\n");
		sql.append("         GROUP BY MATERIAL_ID) TVR\n");
		sql.append(" WHERE TVMG1.PARENT_GROUP_ID = TVMG2.GROUP_ID\n");
		sql.append("   AND TVMG2.PARENT_GROUP_ID = TVMG3.GROUP_ID\n");
		sql.append("   AND TVMG1.GROUP_ID = TVMGR.GROUP_ID\n");
		sql.append("   AND TVMGR.MATERIAL_ID = TVM.MATERIAL_ID\n");
		sql.append("   AND TVM.MATERIAL_ID = TVR.MATERIAL_ID(+)\n");
		sql.append("   AND TVM.MATERIAL_ID = TVOD.MATERIAL_ID\n");
		sql.append("   AND TVO.SPECIAL_REQ_ID = TVSRD.REQ_ID\n");
		sql.append("   AND TVO.ORDER_ID = TVOD.ORDER_ID\n");
		sql.append("AND TVMG1.GROUP_ID = TVSRD.MATERIAL_ID\n");
		sql.append("   AND TVOD.ORDER_ID = " + orderId + "");

		List<Map<String, Object>> list = pageQuery(sql.toString(), null,
				"com.infodms.dms.dao.sales.OrderReportDao.getSalesOrderPreCheckDetailList");
		return list;
	}
	
	/***********
	 * addUser xiongchuan 
	 * addDate 2011-10-21
	 * 查询整车销售经销商是否二级
	 */
	public Boolean  viewDealerLever(String dealerId){
		String  sql = "select * from tm_dealer d where d.dealer_id="+dealerId+"";
		List<Map<String, Object>> list = pageQuery(sql,null,getFunName());
		Boolean flag=false;
		if(list.size()>0){
			if(Integer.valueOf(list.get(0).get("DEALER_LEVEL").toString()).equals(Constant.DEALER_LEVEL_02)){
				flag= true;
			}
		} 
		return flag;
	}
	
	/**
	 * 查询需求ID对应的经销商是否是二级经销商
	 * @param reqId 需求ID
	 * @return boolean 
	 * */
	public Boolean  viewDealerReq(String reqId){
		String  sql = "select d.* from Tt_Vs_Special_Req r ,tm_dealer d where r.dealer_id=d.dealer_id and r.req_id="+reqId+"";
		List<Map<String, Object>> list = pageQuery(sql,null,getFunName());
		Boolean flag=false;
		if(list.size()>0){
			if(Integer.valueOf(list.get(0).get("DEALER_LEVEL").toString()).equals(Constant.DEALER_LEVEL_02)){
				flag= true;
			}
		} 
		return flag;
	}
	
	public List<Map<String, Object>> getDtl(String reqId) {
		StringBuffer sql = new StringBuffer("\n") ;

		sql.append("with ord as\n") ;
		sql.append(" (select tvmgr.group_id, sum(tvod.order_amount) order_amount\n") ;
		sql.append("    from tt_vs_order              tvo,\n") ;
		sql.append("         tt_vs_order_detail       tvod,\n") ;
		sql.append("         tm_vhcl_material_group_r tvmgr\n") ;
		sql.append("   where tvo.order_id = tvod.order_id\n") ;
		sql.append("     and tvod.material_id = tvmgr.material_id\n") ;
		sql.append("     and tvo.order_status <> ").append(Constant.ORDER_STATUS_06).append("\n") ;
		sql.append("     and tvo.special_req_id = ").append(reqId).append("\n") ;
		sql.append("   group by tvmgr.group_id),\n") ;
		sql.append("req as\n") ;
		sql.append(" (select tvsrd.material_id, sum(tvsrd.amount) amount\n") ;
		sql.append("    from tt_vs_special_req tvsr, tt_vs_special_req_dtl tvsrd\n") ;
		sql.append("   where tvsr.req_id = tvsrd.req_id\n") ;
		sql.append("     and tvsr.req_id = ").append(reqId).append("\n") ;
		sql.append("   group by tvsrd.material_id)\n") ;
		sql.append("select req.material_id, nvl(ord.order_amount, 0) order_amount, req.amount\n") ;
		sql.append("  from ord, req\n") ;
		sql.append(" where ord.group_id(+) = req.material_id") ;

		return super.pageQuery(sql.toString(), null, super.getFunName()) ;
	}
	
	public List<Map<String, Object>> getSpecialOrder(String reqId) {
		StringBuffer sql = new StringBuffer("\n") ;
		
		sql.append("select tvo.order_id from tt_vs_order tvo where tvo.special_req_id = ").append(reqId).append(" and tvo.order_status = ").append(Constant.ORDER_STATUS_02).append(" \n");

		return super.pageQuery(sql.toString(), null, super.getFunName()) ;
	}

	public Map<String, Object> getSalesPrice(String detailId, String dealerId, String priceId) {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT VTVPD.PRICE_ID,VTVPD.SALES_PRICE\n");
		sql.append("  FROM VW_TM_DEALER_PRICE_RELATION VTDPR,\n");  
		sql.append("       VW_TT_VS_PRICE_DTL          VTVPD,\n");  
		sql.append("       TT_VS_SPECIAL_REQ_DTL       TVSRD,\n");  
		sql.append("       TT_VS_SPECIAL_REQ           TVSR\n");  
		sql.append(" WHERE VTDPR.IS_DEFAULT = 10041001\n");  
		sql.append("   AND VTDPR.PRICE_ID = VTVPD.PRICE_ID\n");  
		sql.append("   AND TVSRD.MATERIAL_ID = VTVPD.GROUP_ID(+)\n");  
		sql.append("   AND VTDPR.DEALER_ID in (");
		sql.append("SELECT TMD.DEALER_ID\n");
		sql.append("  FROM TM_DEALER TMD\n");  
		sql.append(" WHERE TMD.DEALER_LEVEL = 10851001\n");  
		sql.append(" START WITH TMD.DEALER_ID = TVSR.DEALER_ID\n");  
		sql.append("CONNECT BY PRIOR TMD.PARENT_DEALER_D = TMD.DEALER_ID\n");
		sql.append(" )\n");  
		sql.append("   AND TVSRD.REQ_ID = TVSR.REQ_ID\n");  
		sql.append("   AND TVSRD.DTL_ID = ");
		sql.append(detailId);
		sql.append("\n");

		sql.append("and vtdpr.price_id = ").append(priceId).append("\n") ;

		Map<String, Object> result = dao.pageQueryMap(sql.toString(), null, getFunName());
		return result;
	}

	/**
	 * 查询配置ID和已提报订单数量
	 * @author 韩晓宇
	 * 2012-05-18
	 * @param materialId 物料ID
	 * @param reqId 需求ID
	 * */
	public Map<String, Object> getOrderAmountAndGroupId(String materialId, String reqId) {
		StringBuffer sql = new StringBuffer();
		/*sql.append("SELECT TVMG.GROUP_ID, NVL(TVSRD.ORDER_AMOUNT, 0) ORDER_AMOUNT\n");
		sql.append("FROM TM_VHCL_MATERIAL TVM,\n");  
		sql.append("     TM_VHCL_MATERIAL_GROUP_R TVMGR,\n");  
		sql.append("     TM_VHCL_MATERIAL_GROUP TVMG,\n");  
		sql.append("     TT_VS_SPECIAL_REQ_DTL TVSRD\n");  
		if(materialId != null && !"".equals(materialId)) {
			sql.append("WHERE TVM.MATERIAL_ID = ");
			sql.append(materialId);
			sql.append("\n");
		}
		sql.append("      AND TVM.MATERIAL_ID = TVMGR.MATERIAL_ID\n");  
		sql.append("      AND TVMGR.GROUP_ID = TVMG.GROUP_ID\n");  
		sql.append("      AND TVMG.GROUP_ID = TVSRD.MATERIAL_ID\n");  
		if(dtlId != null && !"".equals(dtlId)) {
			sql.append("      AND TVSRD.DTL_ID = ");
			sql.append(dtlId);
			sql.append("\n");
		}*/
		
		
		sql.append("SELECT TVSRD.MATERIAL_ID ,SUM(TVOD.ORDER_AMOUNT) ORDER_AMOUNT\n");
		sql.append("  FROM TT_VS_ORDER           TVO,\n");  
		sql.append("       TT_VS_ORDER_DETAIL    TVOD,\n");  
		sql.append("       TT_VS_SPECIAL_REQ_DTL TVSRD,\n");  
		sql.append("       TM_VHCL_MATERIAL_GROUP_R TVMGR,\n");  
		sql.append("       (SELECT TVMGR2.GROUP_ID\n");  
		sql.append("       FROM TM_VHCL_MATERIAL_GROUP_R TVMGR2\n");  
		if(materialId != null && !"".equals(materialId)) {
			sql.append("       WHERE TVMGR2.MATERIAL_ID = ");
			sql.append(materialId);
			sql.append(") T\n");  
		}
		sql.append(" WHERE 1 = 1\n");  
		sql.append("   AND TVMGR.GROUP_ID = T.GROUP_ID\n");  
		if(reqId != null && !"".equals(reqId)) {
			sql.append("   AND TVSRD.REQ_ID = ");
			sql.append(reqId);
			sql.append("\n");  
		}
		sql.append("   AND TVSRD.REQ_ID = TVO.SPECIAL_REQ_ID\n");  
		sql.append("   AND TVO.ORDER_ID = TVOD.ORDER_ID\n");  
		sql.append("   AND TVOD.MATERIAL_ID IN TVMGR.MATERIAL_ID\n");  
		sql.append("   AND TVSRD.MATERIAL_ID = TVMGR.GROUP_ID\n");  
		sql.append(" GROUP BY TVSRD.MATERIAL_ID\n");
		Map<String, Object> result = dao.pageQueryMap(sql.toString(), null, getFunName());
		return result;
		
	}

	/**
	 * 查询经销商ID对应的经销商等级是否一致
	 * @param dealerId 经销商ID
	 * @param dealerLevel 经销商等级
	 * @return boolean 
	 * */
	public Boolean  getDealerLevel(String dealerId, String dealerLevel){
		String  sql = "select d.dealer_id, d.dealer_level from tm_dealer d where d.dealer_id="+dealerId;
		List<Map<String, Object>> list = pageQuery(sql,null,getFunName());
		Boolean flag=false;
		if(list.size()>0){
			if(list.get(0).get("DEALER_LEVEL").toString().equals(dealerLevel)){
				flag= true;
			}
		} 
		return flag;
	}

	public Map<String, Object> getSalePriceId(String reqId, String dealerId) {
		StringBuffer sql = new StringBuffer("\n") ;

		sql.append("select F_GET_PID(tvsr.dealer_id) dealer_id\n") ;
		sql.append("  from TT_VS_SPECIAL_REQ tvsr\n") ;
		sql.append(" where tvsr.req_id = ").append(reqId).append("") ;

		Map<String, Object> map = super.pageQueryMap(sql.toString(), null, super.getFunName()) ;
		
		sql = new StringBuffer("\n") ;
		
		sql.append("SELECT VTDPR.PRICE_ID\n");
		sql.append("  FROM VW_TM_DEALER_PRICE_RELATION VTDPR\n");  
		sql.append(" WHERE VTDPR.IS_DEFAULT = 10041001\n");  
		sql.append("   AND VTDPR.DEALER_ID = ").append(map.get("DEALER_ID").toString()).append("\n");  
		
		Map<String, Object> result = dao.pageQueryMap(sql.toString(), null, getFunName());
		return result;
	}

	/**
	 * Function         : 获取VW_TM_DEALER_PRICE_RELATION表的价格ID
	 * @param           : firstLevelDealerId 一级经销商ID
	 * @return          : 满足条件的信息
	 * @throws          : Exception
	 * LastUpdate       : 2012-06-05
	 */
	public Map<String, Object> getSpecialNeedDetailPriceList(
			String firstLevelDealerId) {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT VTDPR.PRICE_ID\n");
		sql.append("  FROM VW_TM_DEALER_PRICE_RELATION VTDPR\n");  
		if(firstLevelDealerId != null && !"".equals(firstLevelDealerId)) {
			sql.append(" WHERE VTDPR.DEALER_ID = ");
			sql.append(firstLevelDealerId);
			sql.append("\n");  
		}
		sql.append("   AND VTDPR.IS_DEFAULT = 10041001\n");
		Map<String, Object> result = dao.pageQueryMap(sql.toString(), null, getFunName());
		return result;
	}

	/**
	 * Function         : 获取VW_TT_VS_PRICE_DTL表的价格明细
	 * @param           : price_id 价格Id
	 * @param			: reqId		需求Id
	 * @return          : 满足条件的信息
	 * @throws          : Exception
	 * LastUpdate       : 2012-06-05
	 */
	public Map<String, Object> getSpecialNeedDetailPriceDtlList(String price_id, String reqId) {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT VTVPD.PRICE_ID, VTVPD.SALES_PRICE, VTVPD.GROUP_ID\n");
		sql.append("  FROM TT_VS_SPECIAL_REQ_DTL TVSRD, VW_TT_VS_PRICE_DTL VTVPD\n");  
		sql.append(" WHERE TVSRD.MATERIAL_ID = VTVPD.GROUP_ID\n");  
		if(price_id != null && !"".equals(price_id)) {
			sql.append("   AND VTVPD.PRICE_ID = ");
			sql.append(price_id);
			sql.append("\n");  
		}
		if(reqId != null && !"".equals(reqId)) {
			sql.append("   AND TVSRD.REQ_ID = ");
			sql.append(reqId);
			sql.append("\n");
		}
		Map<String, Object> result = dao.pageQueryMap(sql.toString(), null, getFunName());
		return result;
	}

	public Map<String, Object> getGroupIdByMaterialId(String materialId) {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT TVMGR.GROUP_ID FROM\n");
		sql.append(" TM_VHCL_MATERIAL_GROUP_R TVMGR\n");  
		sql.append(" WHERE 1=1\n");  
		

		if(materialId != null && !"".equals(materialId)) {
			sql.append(" AND TVMGR.MATERIAL_ID = ");
			sql.append(materialId);
			sql.append("\n");
		}
		Map<String, Object> result = dao.pageQueryMap(sql.toString(), null, getFunName());
		return result;
	}

	public PageResult<Map<String, Object>> orderQuery(Map<String, String> map,int curPage,int pageSize) {
		String orderType = map.get("orderType") ;
		String orderStatus = map.get("orderStatus") ;
		String dealerId = map.get("dealerId") ;
		String orderNo = map.get("orderNo") ;
		String areaId = map.get("areaId") ;
		
		StringBuffer sql = new StringBuffer("\n") ;

		sql.append("select tvo.order_no,\n") ;
		sql.append("       tvo.order_id,\n") ;
		sql.append("       tvo.order_org_id dealer_id,\n") ;
		sql.append("       to_char(tvo.raise_date, 'yyyy-mm-dd') raise_date,\n") ;
		sql.append("       tvo.order_status,\n") ;
		sql.append("       sum(tvod.order_amount) sum_amount\n") ;
		sql.append("  from tt_vs_order tvo, tt_vs_order_detail tvod\n") ;
		sql.append(" where tvo.order_id = tvod.order_id\n") ;
		sql.append("   and tvo.order_type = ").append(orderType).append("\n") ;
		sql.append("   and tvo.order_status = ").append(orderStatus).append("\n") ;
		sql.append("   and tvo.order_org_id in (").append(dealerId).append(")\n") ;
		
		if(!CommonUtils.isNullString(areaId)) {
			sql.append("   and tvo.area_id in (").append(areaId).append(")\n") ;
		}
		
		if(!CommonUtils.isNullString(orderNo)) {
			sql.append("   and tvo.order_no like '%").append(orderNo).append("%'\n") ;
		}
		
		sql.append(" group by tvo.order_no, tvo.order_id, tvo.order_org_id, tvo.raise_date, tvo.order_status") ;

		return super.pageQuery(sql.toString(), null, super.getFunName(),pageSize, curPage) ;
	}
	
	public List<Map<String, Object>> getCustomizedOrderMate(String orderId) {
		StringBuffer sql = new StringBuffer("\n") ;

		sql.append("select ser.group_name SERIES_CODE,\n") ;
		sql.append("       pac.group_id,\n") ;
		sql.append("       tvm.material_id,\n") ;
		sql.append("       tvm.material_code,\n") ;
		sql.append("       tvm.material_name,\n") ;
		sql.append("       modl.group_code,\n") ;
		sql.append("       tvsrd.sales_price,\n") ;
		sql.append("       tvsrd.change_price,\n") ;
		sql.append("       tvsrd.special_batch_no,\n") ;
		sql.append("       tvsrd.amount apply_amount,\n") ;
		sql.append("       tvod.order_amount\n") ;
		sql.append("  from tt_vs_order              tvo,\n") ;
		sql.append("       tt_vs_order_detail       tvod,\n") ;
		sql.append("       tt_vs_special_req        tvsr,\n") ;
		sql.append("       tt_vs_special_req_dtl    tvsrd,\n") ;
		sql.append("       tm_vhcl_material         tvm,\n") ;
		sql.append("       tm_vhcl_material_group_r tvmgr,\n") ;
		sql.append("       tm_vhcl_material_group   pac,\n") ;
		sql.append("       tm_vhcl_material_group   modl,\n") ;
		sql.append("       tm_vhcl_material_group   ser\n") ;
		sql.append(" where tvo.order_id = tvod.order_id\n") ;
		sql.append("   and tvo.special_req_id = tvsr.req_id\n") ;
		sql.append("   and tvsr.req_id = tvsrd.req_id\n") ;
		sql.append("   and pac.group_id = tvsrd.material_id\n") ;
		sql.append("   and tvm.material_id = tvod.material_id\n") ;
		sql.append("   and tvm.material_id = tvmgr.material_id\n") ;
		sql.append("   and tvmgr.group_id = pac.group_id\n") ;
		sql.append("   and pac.parent_group_id = modl.group_id\n") ;
		sql.append("   and modl.parent_group_id = ser.group_id\n") ;
		sql.append("   and tvo.order_id = ").append(orderId).append("") ;

		return super.pageQuery(sql.toString(), null, super.getFunName()) ;
	}
	
	public List<Map<String, Object>> chkOrderAmount(Map<String, String> map) {
		String reqId = map.get("reqId") ;
		String orderId = map.get("orderId") ;
		String isStatus = map.get("isStatus") ;
		
		StringBuffer sql = new StringBuffer("\n") ;

		sql.append("with ord as\n") ;
		sql.append(" (select tvmgr.group_id, sum(tvod.order_amount) order_amount\n") ;
		sql.append("    from tt_vs_order              tvo,\n") ;
		sql.append("         tt_vs_order_detail       tvod,\n") ;
		sql.append("         tm_vhcl_material_group_r tvmgr\n") ;
		sql.append("   where tvo.order_id = tvod.order_id\n") ;
		sql.append("     and tvod.material_id = tvmgr.material_id\n") ;
		
		if(!CommonUtils.isNullString(isStatus)) {
			sql.append("     and tvo.order_status <> ").append(Constant.ORDER_STATUS_06).append("\n") ;
		}
		
		if(!CommonUtils.isNullString(orderId)) {
			sql.append("     and tvo.order_id <> ").append(orderId).append("\n") ;
		}
		
		sql.append("     and tvo.special_req_id = ").append(reqId).append("\n") ;
		sql.append("   group by tvmgr.group_id),\n") ;
		sql.append("req as\n") ;
		sql.append(" (select tvsrd.material_id, sum(tvsrd.amount) amount\n") ;
		sql.append("    from tt_vs_special_req tvsr, tt_vs_special_req_dtl tvsrd\n") ;
		sql.append("   where tvsr.req_id = tvsrd.req_id\n") ;
		sql.append("     and tvsr.req_id = ").append(reqId).append("\n") ;
		sql.append("   group by tvsrd.material_id)\n") ;
		sql.append("select req.material_id, nvl(ord.order_amount, 0) order_amount, req.amount\n") ;
		sql.append("  from ord, req\n") ;
		sql.append(" where ord.group_id(+) = req.material_id") ;

		return super.pageQuery(sql.toString(), null, super.getFunName()) ;
	}
	
	public List<Map<String, Object>> getOrderInfo(Map<String, String> map) {
		String orderId = map.get("orderId") ;

		StringBuffer sql = new StringBuffer("\n") ;

		sql.append("select tvo.order_no,\n") ;
		sql.append("       tvo.order_status,\n") ;
		sql.append("       tvmg2.group_name  SERIES_NAME,\n") ;
		sql.append("       tvmg3.group_name  model_name,\n") ;
		sql.append("       tvmg4.group_code  package_code,\n") ;
		sql.append("       tvm.material_code,\n") ;
		sql.append("       tvm.material_name,\n") ;
		sql.append("       tvod.order_amount\n") ;
		sql.append("  from tt_vs_order              tvo,\n") ;
		sql.append("       tt_vs_order_detail       tvod,\n") ;
		sql.append("       tm_vhcl_material         tvm,\n") ;
		sql.append("       tm_vhcl_material_group_r tvmgr,\n") ;
		sql.append("       tm_vhcl_material_group   tvmg4,\n") ;
		sql.append("       tm_vhcl_material_group   tvmg3,\n") ;
		sql.append("       tm_vhcl_material_group   tvmg2\n") ;
		sql.append(" where tvo.order_id = tvod.order_id\n") ;
		sql.append("   and tvod.material_id = tvmgr.material_id\n") ;
		sql.append("   and tvod.material_id = tvm.material_id\n") ;
		sql.append("   and tvmgr.group_id = tvmg4.group_id\n") ;
		sql.append("   and tvmg4.parent_group_id = tvmg3.group_id\n") ;
		sql.append("   and tvmg3.parent_group_id = tvmg2.group_id\n") ;
		sql.append("   and tvo.order_id = ").append(orderId).append("") ;

		return super.pageQuery(sql.toString(), null, super.getFunName()) ;
	}
	
	//add by WHX,2012.09.12
	//======================================================START
	public TtVsOrderPO orderQuery(Long orderId) {
		TtVsOrderPO tvo = new TtVsOrderPO();
		tvo.setOrderId(orderId) ;
		
		return (TtVsOrderPO)super.select(tvo).get(0) ;
	}
	
	public TtVsSpecialReqPO specialReqQuery(Long reqId) {
		TtVsSpecialReqPO tvsr = new TtVsSpecialReqPO() ;
		tvsr.setReqId(reqId) ;
		
		return (TtVsSpecialReqPO)super.select(tvsr).get(0) ;
	}
	
	public void specialReqUpdate(Map<String, String> map) {
		Long reqId = new Long(map.get("reqId")) ;
		String status = map.get("status") ;
		String userId = map.get("userId") ;
		
		TtVsSpecialReqPO tvsr = new TtVsSpecialReqPO() ;
		tvsr.setReqId(reqId) ;
		
		TtVsSpecialReqPO newTvsr = new TtVsSpecialReqPO() ;
		
		if(!CommonUtils.isNullString(status)) {
			newTvsr.setReqStatus(Integer.parseInt(status)) ;
		}
		
		newTvsr.setUpdateDate(new Date(System.currentTimeMillis())) ;
		newTvsr.setUpdateBy(Long.parseLong(userId)) ;
		
		super.update(tvsr, newTvsr) ;
	}
	//======================================================END
}
