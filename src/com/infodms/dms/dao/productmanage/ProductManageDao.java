/**
 * @Title: ProductManageDao.java
 * 
 * @Description:
 * 
 * @Copyright: Copyright (c) 2010
 * 
 * @Company: www.infoservice.com.cn
 * @Date: 2010-7-1
 * 
 * @author yuyong
 * @mail yuyong@infoservice.com.cn
 * @version 1.0
 * @remark
 */
package com.infodms.dms.dao.productmanage;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.common.Constant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.dao.sales.ordermanage.carSubmission.CarSubmissionDao;
import com.infodms.dms.dao.sales.ordermanage.carSubmission.CarSubmissionQueryDao;
import com.infodms.dms.po.TmVhclMaterialGroupPO;
import com.infodms.dms.po.TmVhclMaterialGroupRPO;
import com.infodms.dms.po.TmVhclMaterialPO;
import com.infodms.dms.po.TmpVsPriceDtlPO;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

/**
 * @author yuyong
 * 
 */
public class ProductManageDao extends BaseDao {
	public static Logger logger = Logger.getLogger(ProductManageDao.class);
	private static final ProductManageDao dao = new ProductManageDao();

	public static final ProductManageDao getInstance() {
		return dao;
	}

	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 物料组维护查询列表
	 * 
	 * @param map
	 * @param curPage
	 * @param pageSize
	 * @return
	 */
	public PageResult<Map<String, Object>> getMaterialGroupManageQueryList(
			Map<String, Object> map, int curPage, int pageSize) {

		String groupCode = (String) map.get("groupCode");
		String groupName = (String) map.get("groupName");
		String parentGroupCode = (String) map.get("parentGroupCode");
		String status = (String) map.get("status");
		String companyId = (String) map.get("companyId");
		String forcast_flag = (String) map.get("forcast_flag");
		String groupLevel = CommonUtils.checkNull(map.get("groupLevel"));
		
		List<Object> params = new ArrayList<Object>();
		StringBuffer sbSql = new StringBuffer();
		
		sbSql.append("SELECT TVMG1.GROUP_ID,\n");
		sbSql.append("       TVMG1.GROUP_CODE,\n");
		sbSql.append("       TVMG1.GROUP_NAME,\n");
		sbSql.append("       TVMG1.STATUS,\n");
		sbSql.append("       TVMG1.GROUP_LEVEL,\n");
		sbSql.append("       TVMG2.GROUP_NAME PAR_GROUP_NAME,\n");
		sbSql.append("       TVMG1.FORCAST_FLAG,\n");
		sbSql.append("       TU.NAME,\n");
		sbSql.append("       TVMG1.UPDATE_DATE\n");
		sbSql.append("  FROM TM_VHCL_MATERIAL_GROUP TVMG1,\n");
		sbSql.append("       TM_VHCL_MATERIAL_GROUP TVMG2,\n");
		sbSql.append("       TC_USER                TU\n");
		sbSql.append(" WHERE TVMG1.PARENT_GROUP_ID = TVMG2.GROUP_ID(+)\n");
		sbSql.append("   AND TU.USER_ID(+) = TVMG1.UPDATE_BY\n");
		
		if(!groupLevel.equals("")) {
			sbSql.append("   AND TVMG1.GROUP_LEVEL = ?\n");
			params.add(groupLevel);
		}
		
		if (!groupCode.equals(""))
		{
			sbSql.append("   AND TVMG1.GROUP_CODE LIKE ?\n");
			params.add("%" + groupCode + "%");
		}
		if (!groupName.equals(""))
		{
			sbSql.append("   AND TVMG1.GROUP_NAME LIKE ?\n");
			params.add("%" + groupName + "%");
		}
		if (!status.equals(""))
		{
			sbSql.append("   AND TVMG1.STATUS = ?\n");
			params.add(status);
		}
		// 新增过滤条件是否在产 2012-05-08 hxy
		if (!forcast_flag.equals(""))
		{
			sbSql.append("   AND TVMG1.FORCAST_FLAG = ?\n");
			params.add(forcast_flag);
		}
		if (!companyId.equals(""))
		{
			sbSql.append("   AND TVMG1.COMPANY_ID = ?\n");
			params.add(companyId);
		}
		if (!parentGroupCode.equals(""))
		{
			sbSql.append("   AND TVMG2.GROUP_CODE LIKE ?");
			params.add("%" + parentGroupCode + "%");
		}
		sbSql.append("ORDER BY TVMG1.GROUP_LEVEL");
		return pageQuery(sbSql.toString(), params, getFunName(), pageSize, curPage);
	}
	
	//zhumingwei 2011-10-20
	public PageResult<Map<String, Object>> getDealerProductPackageQuery(Map<String, Object> map, int curPage, int pageSize) {
		String packageCode = (String) map.get("packageCode");
		String packageName = (String) map.get("packageName");
		String regionId = (String) map.get("regionId");
		String status = (String) map.get("status");
		String dealerId = (String) map.get("dealerId");
		
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT a.*,r.region_name\n");
		sql.append("  FROM tt_as_wr_product_package a,tm_region r\n");
		sql.append(" WHERE a.province_id=r.region_id\n");
		if (!packageCode.equals("")) {
			sql.append("   AND a.package_code LIKE '%" + packageCode + "%'\n");
		}
		if (!packageName.equals("")) {
			sql.append("   AND a.package_name LIKE '%" + packageName + "%'\n");
		}
		if (!regionId.equals("")) {
			sql.append("   AND r.region_id  in(" + regionId + ")\n");
		}
		if (!status.equals("")) {
			sql.append("   AND a.status = '" + status + "'");
		}
		if (!CommonUtils.isNullString(dealerId)) {
			sql.append("   AND exists(select 1 from tt_product_distribution tpd where tpd.product_id=a.product_id and tpd.company_id in(").append(dealerId).append("))");
		}
		PageResult<Map<String, Object>> ps = pageQuery(sql.toString(),null,
				"com.infodms.dms.dao.productmanage.ProductManageDao.getDealerProductPackageQuery",pageSize, curPage);
		return ps;
	}
	
	//zhumingwei 2011-10-27
	public PageResult<Map<String, Object>> getDealerPackageQuery(Map<String, Object> map, int curPage, int pageSize) {
		String packageCode = (String) map.get("packageCode");
		String packageName = (String) map.get("packageName");
		String regionId = (String) map.get("regionId");
		String status = (String) map.get("status");
		String companyId = (String) map.get("companyId");
		
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT a.*,r.region_name\n");
		sql.append("  FROM tt_as_wr_product_package a,tm_region r\n");
		sql.append(" WHERE a.province_id=r.region_id\n");
		if (!packageCode.equals("")) {
			sql.append("   AND a.package_code LIKE '%" + packageCode + "%'\n");
		}
		if (!packageName.equals("")) {
			sql.append("   AND a.package_name LIKE '%" + packageName + "%'\n");
		}
		if (!regionId.equals("")) {
			sql.append("   AND r.region_id  in(" + regionId + ")\n");
		}
		if (!status.equals("")) {
			sql.append("   AND a.status = '" + status + "'");
		}
		if (!CommonUtils.isNullString(companyId)) {
			sql.append("   AND exists(select 1 from tt_product_distribution tpd where tpd.product_id=a.product_id and tpd.company_id ="+companyId+")");
		}
		PageResult<Map<String, Object>> ps = pageQuery(sql.toString(),null,
				"com.infodms.dms.dao.productmanage.ProductManageDao.getDealerProductPackageQuery",pageSize, curPage);
		return ps;
	}

	public TmVhclMaterialGroupPO getTmVhclMaterialGroupPO(
			TmVhclMaterialGroupPO po) {
		List<PO> list = dao.select(po);
		TmVhclMaterialGroupPO result = list.size() != 0 ? (TmVhclMaterialGroupPO) list
				.get(0)
				: null;
		return result;
	}

	/**
	 * 物料维护查询列表
	 * 
	 * @param map
	 * @param curPage
	 * @param pageSize
	 * @return
	 */
	public PageResult<Map<String, Object>> getMaterialManageQueryList(
			Map<String, Object> map, int curPage, int pageSize) {

		String materialCode = (String) map.get("materialCode");
		String materialName = (String) map.get("materialName");
		String status = (String) map.get("status");
		String groupCode = (String) map.get("groupCode");
		String companyId = (String) map.get("companyId");
		String ERPName = (String) map.get("ERPName");


		String orderFlag = (String) map.get("orderFlag");
		String procuctFlag = (String) map.get("procuctFlag");
		String isExport = (String) map.get("isExport");
		String mtseach = (String) map.get("mtseach");
		StringBuffer sql = new StringBuffer("\n");

		sql.append("SELECT TVM.MATERIAL_ID,\n");
		sql.append("       TVM.MATERIAL_CODE,\n");  
		sql.append("       TVM.MATERIAL_NAME,\n");  
		sql.append("       TVM.ERP_NAME, --ERP名称\n");  
		sql.append("       TVM.COLOR_CODE,\n");  
		sql.append("       TVM.COLOR_NAME,\n");  
		sql.append("       DECODE(TVM.ORDER_FLAG, 10331001, '可提报', '不可提报') ORDER_FLAG, ---是否可提报\n");  
		sql.append("       DECODE(TVM.PROCUCT_FLAG, 96891001, '可生产', '不可生产') PROCUCT_FLAG, ---是否可提报\n");  
		sql.append("       TVM.EXPORT_SALES_FLAG,\n");  
		sql.append("       TVM.MAT_TYPE,\n"); 
		sql.append("       TVM.IS_INSALE,\n");
		sql.append("       TVM.STATUS\n");  
		sql.append("  FROM TM_VHCL_MATERIAL TVM\n");  
		sql.append(" WHERE 1 = 1\n");  
		
		if (!CommonUtils.isNullString(materialCode)) {
			sql.append("   AND TVM.MATERIAL_CODE LIKE '%").append(materialCode).append("%'\n");  
		}
		if (!CommonUtils.isNullString(materialName)) {
			sql.append("   AND TVM.MATERIAL_NAME LIKE '%").append(materialName).append("%'\n");  
		}
		if (!CommonUtils.isNullString(companyId)) {
			sql.append("   AND TVM.COMPANY_ID = ").append(companyId).append("\n");  
		}
		if (!CommonUtils.isNullString(status)) {
			sql.append("   AND TVM.STATUS = ").append(status).append("\n");  
		}
		if (!CommonUtils.isNullString(orderFlag)) {
			sql.append("   AND TVM.ORDER_FLAG = ").append(orderFlag).append("\n");  
		}
		if (!CommonUtils.isNullString(procuctFlag)) {
			sql.append("   AND TVM.PROCUCT_FLAG = ").append(procuctFlag).append("\n");  
		}
		if (!CommonUtils.isNullString(mtseach)) {
			sql.append("   AND TVM.MAT_TYPE = ").append(mtseach).append("\n");  
		}
		
		if (!CommonUtils.isNullString(groupCode)) {
			sql.append("   AND TVM.MATERIAL_ID IN\n");  
			sql.append("       (SELECT MGR.MATERIAL_ID\n");  
			sql.append("          FROM TM_VHCL_MATERIAL_GROUP_R MGR, TM_VHCL_MATERIAL_GROUP VMG\n");  
			sql.append("         WHERE MGR.GROUP_ID = VMG.GROUP_ID\n");  
			sql.append("           AND VMG.GROUP_CODE LIKE '%").append(groupCode).append("%')\n");
		}
		if (!CommonUtils.isNullString(ERPName)) {
			sql.append("   AND TVM.ERP_NAME LIKE '%").append(ERPName).append("%'\n");  
		}
		if (!CommonUtils.isNullString(isExport)) {
			sql.append("   AND TVM.EXPORT_SALES_FLAG =").append(isExport).append("\n");  
		}
		PageResult<Map<String, Object>> ps = pageQuery(
				sql.toString(),
				null,
				"com.infodms.dms.dao.productmanage.ProductManageDao.getMaterialManageQueryList",
				pageSize, curPage);
		return ps;
	}

	/**
	 * 获得整车物料po
	 * 
	 * @param po
	 * @return
	 */
	public TmVhclMaterialPO getTmVhclMaterialPO(TmVhclMaterialPO po) {
		List<PO> list = dao.select(po);
		TmVhclMaterialPO result = list.size() != 0 ? (TmVhclMaterialPO) list
				.get(0) : null;
		return result;
	}

	/**
	 * 获得整车物料与物料组关系po
	 * 
	 * @param po
	 * @return
	 */
	public TmVhclMaterialGroupRPO getTmVhclMaterialGroupRPO(
			TmVhclMaterialGroupRPO po) {
		List<PO> list = dao.select(po);
		TmVhclMaterialGroupRPO result = list.size() != 0 ? (TmVhclMaterialGroupRPO) list
				.get(0)
				: null;
		return result;
	}
	/*
	 * 获得特殊价格列表
	 */
	public List<Map<String, Object>> getmyPriceTypeList(Long companyId,String dealerId) {

		StringBuffer sql = new StringBuffer();

//		sql.append("SELECT *\n");
//		sql.append("FROM (\n");  
//		sql.append("SELECT TVP.PRICE_ID,\n");  
//		sql.append("       TVP.PRICE_CODE,\n");  
//		sql.append("       TVP.PRICE_DESC,\n");  
//		sql.append("       TC.CODE_DESC,\n");  
//		sql.append("       TDPR.RELATION_ID\n");  
//		sql.append("  FROM TT_VS_PRICE              TVP,\n");  
//		sql.append("       TM_DEALER_PRICE_RELATION TDPR,\n");  
//		sql.append("       TC_CODE                  TC,\n");  
//		sql.append("       TM_DEALER                TD\n");  
//		sql.append(" WHERE TRUNC(TVP.START_DATE) <= TRUNC(SYSDATE)\n");  
//		sql.append("   AND TRUNC(TVP.END_DATE) >= TRUNC(SYSDATE)\n");  
//		sql.append("   AND TDPR.PRICE_ID = TVP.PRICE_ID\n");  
//		sql.append("   AND TDPR.DEALER_ID = TD.DEALER_ID\n");  
//		sql.append("   AND TDPR.IS_DEFAULT = TC.CODE_ID\n");  
//		sql.append("   AND TVP.COMPANY_ID = " + companyId + "\n");  
//		sql.append("   AND TDPR.DEALER_ID = '" + dealerId + "'\n");  
//		sql.append("   UNION\n");  
//		sql.append("SELECT TVPA.PRICE_ID,\n");  
//		sql.append("       TVPA.PRICE_CODE,\n");  
//		sql.append("       TVPA.PRICE_DESC,\n");  
//		sql.append("       TCA.CODE_DESC,\n");  
//		sql.append("       TDPRA.RELATION_ID\n");  
//		sql.append("  FROM TT_VS_PRICE              TVPA,\n");  
//		sql.append("       TM_DEALER_PRICE_RELATION TDPRA,\n");  
//		sql.append("       TC_CODE TCA,\n");  
//		sql.append("       TM_DEALER                TDA\n");  
//		sql.append(" WHERE TDPRA.PRICE_ID = TVPA.PRICE_ID\n");  
//		sql.append("   AND TDPRA.DEALER_ID = TDA.DEALER_ID\n");  
//		sql.append("   AND TDPRA.IS_DEFAULT = TCA.CODE_ID\n");  
//		sql.append("   AND TVPA.CREATE_BY = '-100'\n");  
//		sql.append("   AND TVPA.COMPANY_ID = " + companyId + "\n");  
//		sql.append("   AND TDPRA.DEALER_ID = '" + dealerId + "') B\n");  
//		sql.append(" ORDER BY B.CODE_DESC DESC, B.PRICE_DESC\n");
//		sql.append("SELECT *\n");
//	    sql.append("FROM (\n");  
//	    sql.append("SELECT rownum,TVP.PRICE_ID,\n");  
//	    sql.append("       TVP.PRICE_CODE,\n");  
//	    sql.append("       TVP.PRICE_DESC,\n");  
//	    sql.append("       TC.CODE_DESC,\n");  
//	    sql.append("       TDPR.RELATION_ID,TVP.CREATE_DATE\n");  
//	    sql.append("  FROM TT_VS_PRICE              TVP,\n");  
//	    sql.append("       TM_DEALER_PRICE_RELATION TDPR,\n");  
//	    sql.append("       TC_CODE                  TC,\n");  
//	    sql.append("       TM_DEALER                TD\n");  
//	    sql.append(" WHERE TRUNC(TVP.START_DATE) <= TRUNC(SYSDATE)\n");  
//	    sql.append("   AND TRUNC(TVP.END_DATE) >= TRUNC(SYSDATE)\n");  
//	    sql.append("   AND TDPR.PRICE_ID = TVP.PRICE_ID\n");  
//	    sql.append("   AND TDPR.DEALER_ID = TD.DEALER_ID\n");  
//	    sql.append("   AND TDPR.IS_DEFAULT = TC.CODE_ID\n");  
//	    sql.append("	AND TDPR.CREATE_BY = '-1'\n");
//	    sql.append("   AND TVP.COMPANY_ID = " + companyId + "\n");  
//	    sql.append("   AND TDPR.DEALER_ID = '" + dealerId + "'\n");  
//	    sql.append("   AND ROWNUM=1  order by TVP.CREATE_DATE DESC  ");
//	    sql.append("   UNION\n");  
	    sql.append("SELECT TVPA.PRICE_ID,\n");  
	    sql.append("       TVPA.PRICE_CODE,\n");  
	    sql.append("       TVPA.PRICE_DESC,\n");  
	    sql.append("       TCA.CODE_DESC,\n");
	    sql.append("       TVPA.START_DATE,\n"); 
	    sql.append("       TVPA.END_DATE,\n"); 
	    sql.append("       TVPA.CREATE_DATE,\n"); 
	    sql.append("       TDPRA.RELATION_ID\n");  
	    sql.append("  FROM vw_TT_VS_PRICE              TVPA,\n");  
	    sql.append("       vw_TM_DEALER_PRICE_RELATION TDPRA,\n");  
	    sql.append("       TC_CODE TCA,\n");  
	    sql.append("       TM_DEALER                TDA\n");  
	    sql.append(" WHERE TDPRA.PRICE_ID = TVPA.PRICE_ID\n");  
	    sql.append("   AND TDPRA.DEALER_ID = TDA.DEALER_ID\n");  
	    sql.append("   AND TDPRA.IS_DEFAULT = TCA.CODE_ID\n");  
	    sql.append("	 AND TDPRA.CREATE_BY!=-1");
	    sql.append("	 AND TDPRA.CREATE_BY!=-100");
	    //sql.append("   AND TDPRA.IS_DEFAULT="+Constant.IF_TYPE_YES+"\n");
	    //sql.append("   AND TVPA.CREATE_BY = '-100'\n");  
	    sql.append("   AND TVPA.COMPANY_ID = " + companyId + "\n");  
	    sql.append("   AND TDPRA.DEALER_ID = '" + dealerId + "'\n");  
	    sql.append(" ORDER BY TCA.CODE_DESC DESC, TVPA.PRICE_DESC\n");

		List<Map<String, Object>> list = pageQuery(sql.toString(), null,
				"com.infodms.dms.dao.productmanage.ProductManageDao.getmyPriceTypeList");
		return list;
	}
	/*
	 * 获得特殊价格排除列表
	 */
	public PageResult<Map<String, Object>> getmyOtherPriceTypeList(String priceDesc,Long companyId,String dealerId,int curPage, int pageSize) {

		StringBuffer sql = new StringBuffer();
		sql.append("select p.price_id,p.price_code,p.price_desc,to_char(p.start_date,'yyyy-mm-dd') as start_date,to_char(p.end_date,'yyyy-mm-dd') as end_date,to_char(p.create_date,'yyyy-mm-dd') as create_date from tt_vs_price p where p.start_date<= sysdate and p.end_date>=sysdate ");
		if (null != priceDesc && !"".equals(priceDesc)) {
			sql.append("and p.PRICE_DESC LIKE '%"+priceDesc.trim()+"%'\n");
		}
		
		/*sql.append("SELECT A.PRICE_ID, A.PRICE_DESC, A.CREATE_DATE\n");
		sql.append("  FROM vw_TT_VS_PRICE A\n");  
		//sql.append(" WHERE TRUNC(A.START_DATE) <= TRUNC(SYSDATE)\n");  
		//sql.append("   AND TRUNC(NVL(A.END_DATE, SYSDATE + 1)) >= TRUNC(SYSDATE)\n");  
		sql.append("   WHERE A.COMPANY_ID = " + companyId + "\n");  
		if (null != priceDesc && !"".equals(priceDesc)) {
			sql.append("AND A.PRICE_DESC LIKE '%"+priceDesc.trim()+"%'\n");
		}
		// 新添加个列表，该列表中可能无开始与结束时间，由B.CREATE_BY=-100标示
		sql.append("UNION\n");
		sql.append("SELECT B.PRICE_ID, B.PRICE_DESC, B.CREATE_DATE\n");  
		sql.append("  FROM vw_TT_VS_PRICE B\n");  
		sql.append(" WHERE B.COMPANY_ID = " + companyId + "\n");  
		sql.append("   AND B.CREATE_BY = '-100'\n");
		if (null != priceDesc && !"".equals(priceDesc)) {
			sql.append("AND B.PRICE_DESC LIKE '%"+priceDesc.trim()+"%'\n");
		}
//		sql.append("MINUS\n");  
//		sql.append("SELECT TVP.PRICE_ID, TVP.PRICE_DESC, TVP.CREATE_DATE\n");  
//		sql.append("  FROM TT_VS_PRICE              TVP,\n");  
//		sql.append("       TM_DEALER_PRICE_RELATION TDPR,\n");  
//		sql.append("       TM_DEALER                TD\n");  
//		sql.append("   WHERE TVP.COMPANY_ID = " + companyId + "\n");  
//		sql.append("   AND TDPR.PRICE_ID = TVP.PRICE_ID\n");  
//		sql.append("   AND TDPR.DEALER_ID = '" + dealerId + "'\n");
*/
		PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null,
				"com.infodms.dms.dao.productmanage.ProductManageDao.getmyOtherPriceTypeList", pageSize, curPage);
		return ps;
	}
	/**
	 * 获得价格类型列表
	 * 
	 * @return
	 */
	public List<Map<String, Object>> getPriceTypeList(Long companyId) {

		StringBuffer sql = new StringBuffer();

		sql.append("SELECT TVP.PRICE_ID, TVP.PRICE_DESC\n");
		sql.append("  FROM vw_TT_VS_PRICE TVP\n");
		sql.append(" WHERE TVP.START_DATE < SYSDATE\n");
		sql.append("   AND TVP.END_DATE > SYSDATE");
		sql.append("   AND TVP.COMPANY_ID = " + companyId + "");

		List<Map<String, Object>> list = pageQuery(sql.toString(), null,
				"com.infodms.dms.dao.productmanage.ProductManageDao.getPriceTypeList");
		return list;
	}
	/**
	 * 获得价格类型列表
	 * 
	 * @return
	 */
	public List<Map<String, Object>> getbrandList() {
		
		StringBuffer sql= new StringBuffer();
		sql.append("select t.group_id, t.group_code, t.group_name\n" );
		sql.append("  from tm_vhcl_material_group t\n" );
		sql.append(" where t.group_level = 1");

		
		List<Map<String, Object>> list = pageQuery(sql.toString(), null,
				"com.infodms.dms.dao.productmanage.ProductManageDao.getbrandList");
		return list;
	}

	/**
	 * 物料维护查询列表
	 * 
	 * @param map
	 * @param curPage
	 * @param pageSize
	 * @return
	 */
	public PageResult<Map<String, Object>> getMaterialPriceManageQueryList(
			Map<String, Object> map, int curPage, int pageSize,String priceDesc) {

		String groupId = (String) map.get("groupId");
		String priceId = (String) map.get("priceId");
		String companyId = (String) map.get("companyId");

		StringBuffer sql = new StringBuffer();

		sql.append("SELECT TVM.GROUP_CODE,\n");
		sql.append("       TVM.GROUP_NAME,\n");
		sql.append("       TVP.PRICE_CODE,\n");
		sql.append("       TVP.PRICE_DESC,\n");
		sql.append("       TVPD.SALES_PRICE,\n");
		sql.append("       TVPD.NO_TAX_PRICE\n");
		sql.append("  FROM vw_TT_VS_PRICE_DTL TVPD, vw_TT_VS_PRICE TVP, TM_VHCL_MATERIAL_GROUP TVM\n");
		sql.append(" WHERE TVPD.PRICE_ID = TVP.PRICE_ID\n");
		sql.append("   AND TVPD.GROUP_ID = TVM.GROUP_ID\n");
		//sql.append("   AND TVP.START_DATE < SYSDATE\n");
		//sql.append("   AND TVP.END_DATE > SYSDATE\n");
		if(!priceDesc.equals("")){
			sql.append("AND TVP.price_desc like '%"+priceDesc+"%'\n");
		}
		if (!groupId.equals("")) {
			sql.append("   AND TVM.GROUP_Id = '" + groupId + "'\n");
		}
		if (!priceId.equals("")) {
			sql.append("   AND TVP.PRICE_ID = " + priceId + "");
		}
		if (!companyId.equals("")) {
			sql.append("   AND TVP.COMPANY_ID = " + companyId + "");
		}

		PageResult<Map<String, Object>> ps = pageQuery(sql.toString(),null, getFunName(), pageSize, curPage);
		return ps;
	}

	public PageResult<Map<String, Object>> getMaterialPriceManageTypeQueryList(
			Map<String, Object> map, int curPage, int pageSize) {

		String companyId = (String) map.get("companyId");

		StringBuffer sql = new StringBuffer();

		sql.append("SELECT TVP.PRICE_ID,\n");
		sql.append("       TVP.PRICE_CODE,\n");
		sql.append("       TVP.PRICE_DESC,\n");
		sql
				.append("       TO_CHAR(TVP.START_DATE, 'YYYY-MM-DD') START_DATE,\n");
		sql.append("       TO_CHAR(TVP.END_DATE, 'YYYY-MM-DD') END_DATE\n");
		sql.append("  FROM vw_TT_VS_PRICE TVP");
		sql.append(" WHERE 1=1\n");
		if (!companyId.equals("")) {
			sql.append("   AND TVP.COMPANY_ID = " + companyId + "");
		}

		PageResult<Map<String, Object>> ps = pageQuery(
				sql.toString(),
				null,
				"com.infodms.dms.dao.productmanage.ProductManageDao.getMaterialPriceManageTypeQueryList",
				pageSize, curPage);
		return ps;
	}

	/**
	 * 查询每条数据在临时表是否重复 return: boolean true不重复，false重复
	 * 
	 * @param paraPo
	 * @return
	 */
	public boolean isDateDump(TmpVsPriceDtlPO paraPo) {
		List<Object> params = new LinkedList<Object>();
		StringBuffer sql = new StringBuffer("");
		boolean bl = true;

		sql.append("SELECT COUNT(*) AMT \n");
		sql.append("  from TMP_VS_PRICE_DTL P\n");
		sql.append(" WHERE P.PRICE_CODE = ?\n");
		params.add(paraPo.getPriceCode());
		sql.append("   AND P.GROUP_CODE = ?\n");
		params.add(paraPo.getGroupCode());
		sql.append("   AND P.USER_ID = ?\n");
		params.add(paraPo.getUserId());

		List<Map<String, Object>> list = dao.pageQuery(sql.toString(), params,
				getFunName());
		if (null != list && list.size() > 0) {
			Map<String, Object> map = list.get(0);
			Integer amt = new Integer(map.get("AMT").toString());
			if (amt.intValue() > 1) {
				bl = false;
			}
		}
		return bl;
	}

	public PageResult<Map<String, Object>> getMaterialPriceManageImportTempList(
			Map<String, Object> map, int curPage, int pageSize) {

		String userId = (String) map.get("userId");

		StringBuffer sql = new StringBuffer();

		sql.append("SELECT TVPD.PRICE_CODE,\n");
		sql.append("       TVPD.GROUP_CODE,\n");
		sql.append("       TVPD.SALES_PRICE,\n");
		sql.append("       TVPD.NO_TAX_PRICE\n");
		sql.append("  FROM TMP_VS_PRICE_DTL TVPD\n");
		sql.append(" WHERE TVPD.USER_ID = " + userId + "");

		PageResult<Map<String, Object>> ps = pageQuery(sql.toString(),null,getFunName(),pageSize, curPage);
		return ps;
	}

	public void insertTtVsPriceDtl(Map<String, Object> map) {

		String userId = (String) map.get("userId");

		StringBuffer sql = new StringBuffer();

		sql.append("MERGE INTO TT_VS_PRICE_DTL TVPD\n");
		sql.append("USING (SELECT TVP.PRICE_ID,\n");
		sql.append("              TVM.GROUP_ID,\n");
		sql.append("              TVPD.SALES_PRICE,\n");
		sql.append("              TVPD.NO_TAX_PRICE\n");
		sql.append("         FROM TMP_VS_PRICE_DTL TVPD, TT_VS_PRICE TVP, TM_VHCL_MATERIAL_GROUP TVM\n");
		sql.append("        WHERE TVPD.PRICE_CODE = TVP.PRICE_CODE\n");
		sql.append("          AND TVPD.GROUP_CODE = TVM.GROUP_CODE\n");
		sql.append("          AND TVPD.USER_ID = " + userId + ") TEMP\n");
		sql.append("ON (TVPD.PRICE_ID = TEMP.PRICE_ID AND TVPD.GROUP_ID = TEMP.GROUP_ID)\n");
		sql.append("WHEN MATCHED THEN\n");
		sql.append("  UPDATE\n");
		sql.append("     SET TVPD.SALES_PRICE  = TEMP.SALES_PRICE,\n");
		sql.append("         TVPD.NO_TAX_PRICE = TEMP.NO_TAX_PRICE\n");
		sql.append("WHEN NOT MATCHED THEN\n");
		sql.append("  INSERT\n");
		sql.append("    (DETAIL_ID,\n");
		sql.append("     PRICE_ID,\n");
		sql.append("     GROUP_ID,\n");
		sql.append("     SALES_PRICE,\n");
		sql.append("     NO_TAX_PRICE,\n");
		sql.append("     CREATE_BY,\n");
		sql.append("     CREATE_DATE)\n");
		sql.append("  VALUES\n");
		sql.append("    (F_GETID,\n");
		sql.append("     TEMP.PRICE_ID,\n");
		sql.append("     TEMP.GROUP_ID,\n");
		sql.append("     TEMP.SALES_PRICE,\n");
		sql.append("     TEMP.NO_TAX_PRICE,\n");
		sql.append("     " + userId + ",\n");
		sql.append("     SYSDATE)");

		dao.update(sql.toString(), null);
	}

	/**
	 * 获得索赔车型组列表
	 * 
	 * @param wrgroupType
	 * @return
	 */
	public List<Map<String, Object>> getModelGroupList(Integer wrgroupType,
			Long companyId) {

		StringBuffer sql = new StringBuffer();

		sql.append("SELECT TAWMG.WRGROUP_ID, TAWMG.WRGROUP_NAME\n");
		sql.append("  FROM TT_AS_WR_MODEL_GROUP TAWMG\n");
		sql.append(" WHERE TAWMG.WRGROUP_TYPE = " + wrgroupType + "\n");
		sql.append(" AND TAWMG.OEM_COMPANY_ID = " + companyId + "");

		List<Map<String, Object>> list = pageQuery(sql.toString(), null,
				"com.infodms.dms.dao.productmanage.ProductManageDao.getModelGroupList");
		return list;
	}
	
	//zhumingwei 2011-10-21
	public List<Map<String, Object>> getProdyctRegion(String id) {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT a.*,b.region_name,b.region_id, b.region_code\n");
		sql.append("  FROM tt_as_wr_product_package a,Tm_Region b\n");
		sql.append(" WHERE a.province_id=b.region_id and a.product_id = " + id + "\n");

		List<Map<String, Object>> list = pageQuery(sql.toString(), null,"com.infodms.dms.dao.productmanage.ProductManageDao.getProdyctRegion");
		return list;
	}
	
	//zhumingwei 2011-10-24
	public static PageResult <Map<String,Object>> getProdyctMaterialInfoList(String product_id,int pageSize,int curPage){
		StringBuffer sql = new StringBuffer("");
		sql.append("SELECT AG.id ,TG.GROUP_CODE, TG.GROUP_NAME\n");
		sql.append("  FROM tt_product_Material AG, TM_VHCL_MATERIAL_GROUP TG\n");  
		sql.append(" WHERE AG.material_id = TG.GROUP_ID\n");  
		sql.append("   AND AG.product_id ="+product_id+"\n");  
		sql.append(" ORDER BY AG.CREATE_DATE DESC, TG.CREATE_DATE DESC\n");
		return dao.pageQuery(sql.toString(), null, dao.getFunName(),pageSize ,curPage);
	}
	
	//zhumingwei 2011-10-24
	public List<Map<String, Object>> getProductDistribution(String id) {
		StringBuffer sql = new StringBuffer("\n") ;
		
		sql.append("select tpd.product_distribution_id, tpd.company_id, tpd.company_code, tpd.company_name\n");
		sql.append("  from tt_product_distribution tpd\n");  
		sql.append(" where tpd.product_id ='"+id+"'");
		
		return super.pageQuery(sql.toString(), null, super.getFunName()) ;
	}
	
	//zhumingwei 2011-10-21
	public List<Map<String, Object>> getProdyctMaterial(String id) {
		StringBuffer sql = new StringBuffer();
		sql.append("select b.group_code,b.group_name from  tt_product_Material a, tm_vhcl_material_group b  where a.material_id=b.group_id and a.product_id="+id+"\n");

		List<Map<String, Object>> list = pageQuery(sql.toString(), null,"com.infodms.dms.dao.productmanage.ProductManageDao.getProdyctMaterial");
		return list;
	}
	
	//zhumingwei 2011-10-25
	public List<Map<String, Object>> dealerPackageReportExcel(String packageCode,String packageName,String regionId,String status){
		StringBuilder sql= new StringBuilder();
		sql.append("SELECT a.*,r.region_name,tc.code_desc\n");
		sql.append("  FROM tt_as_wr_product_package a,tm_region r,TC_CODE TC\n");
		sql.append(" WHERE a.province_id=r.region_id AND a.status=tc.code_id\n");
		
		if (Utility.testString(packageCode)) {
			sql.append("   AND a.package_code LIKE '%" + packageCode + "%'\n");
		}
		if (Utility.testString(packageName)) {
			sql.append("   AND a.package_name LIKE '%" + packageName + "%'\n");
		}
		if (Utility.testString(regionId)) {
			sql.append("   AND r.region_id  in(" + regionId + ")\n");
		}
		if (Utility.testString(status)) {
			sql.append("   AND a.status = '" + status + "'");
		}
		System.out.println("sqlsql=="+sql.toString());
		List<Map<String, Object>> list = this.pageQuery(sql.toString(), null, this.getFunName());
		return list;
	}
	
	public List<Map<String, Object>> getProductComboDtl(Map<String, String> map) {
		String packageCode = map.get("packageCode") ;
		String packageName = map.get("packageName") ;
		String regionId = map.get("regionId") ;
		String status = map.get("status") ;
		
		StringBuilder sql= new StringBuilder("\n");
		
		sql.append("SELECT TAWPP.PRODUCT_ID,\n");
		sql.append("       TAWPP.PACKAGE_CODE,\n");  
		sql.append("       TAWPP.PACKAGE_NAME,\n");  
		sql.append("       TMR.REGION_NAME,\n");  
		sql.append("       DECODE(TAWPP.STATUS, 10011001, '有效', '无效') STATUS,\n");  
		sql.append("       TVMG.GROUP_CODE,\n");  
		sql.append("       TVMG.GROUP_NAME\n");  
		sql.append("  FROM TT_AS_WR_PRODUCT_PACKAGE TAWPP,\n");  
		sql.append("       TM_REGION                TMR,\n");  
		sql.append("       TT_PRODUCT_MATERIAL      TPM,\n");  
		sql.append("       TM_VHCL_MATERIAL_GROUP   TVMG\n");  
		sql.append(" WHERE TAWPP.PROVINCE_ID = TMR.REGION_ID\n");  
		sql.append("   AND TAWPP.PRODUCT_ID = TPM.PRODUCT_ID\n");  
		sql.append("   AND TPM.MATERIAL_ID = TVMG.GROUP_ID\n");  

		if (Utility.testString(packageCode)) {
			sql.append("   AND TAWPP.package_code LIKE '%" + packageCode + "%'\n");
		}
		if (Utility.testString(packageName)) {
			sql.append("   AND TAWPP.package_name LIKE '%" + packageName + "%'\n");
		}
		if (Utility.testString(regionId)) {
			sql.append("   AND tmr.region_id  in(" + regionId + ")\n");
		}
		if (Utility.testString(status)) {
			sql.append("   AND TAWPP.status = '" + status + "'");
		}
		
		sql.append(" ORDER BY TAWPP.PRODUCT_ID\n");
		
		return super.pageQuery(sql.toString(), null, super.getFunName()) ;
	}
	
	public List<Map<String, Object>> getProductComboDlr(Map<String, String> map) {
		String packageCode = map.get("packageCode") ;
		String packageName = map.get("packageName") ;
		String regionId = map.get("regionId") ;
		String status = map.get("status") ;
		String companyId = map.get("companyId") ;
		
		StringBuilder sql= new StringBuilder("\n");
		
		sql.append("SELECT TAWPP.PRODUCT_ID,\n");
		sql.append("       TAWPP.PACKAGE_CODE,\n");  
		sql.append("       TAWPP.PACKAGE_NAME,\n");  
		sql.append("       TMR.REGION_NAME,\n");  
		sql.append("       DECODE(TAWPP.STATUS, 10011001, '有效', '无效') STATUS,\n");  
		sql.append("       TPD.COMPANY_CODE,\n");  
		sql.append("       TPD.COMPANY_NAME\n");  
		sql.append("  FROM TT_AS_WR_PRODUCT_PACKAGE TAWPP,\n");  
		sql.append("       TM_REGION                TMR,\n");  
		sql.append("       TT_PRODUCT_DISTRIBUTION  TPD\n");  
		sql.append(" WHERE TAWPP.PROVINCE_ID = TMR.REGION_ID\n");  
		sql.append("   AND TAWPP.PRODUCT_ID = TPD.PRODUCT_ID\n");  
		

		if (Utility.testString(packageCode)) {
			sql.append("   AND TAWPP.package_code LIKE '%" + packageCode + "%'\n");
		}
		if (Utility.testString(packageName)) {
			sql.append("   AND TAWPP.package_name LIKE '%" + packageName + "%'\n");
		}
		if (Utility.testString(regionId)) {
			sql.append("   AND tmr.region_id  in(" + regionId + ")\n");
		}
		if (Utility.testString(status)) {
			sql.append("   AND TAWPP.status = '" + status + "'");
		}
		
		if (Utility.testString(companyId)) {
			sql.append("   AND TPD.company_id in (" + companyId + ")");
		}
		
		sql.append(" ORDER BY TAWPP.PRODUCT_ID\n");
		
		return super.pageQuery(sql.toString(), null, super.getFunName()) ;
	}
	
	/**
	 * 经销商价格列表
	 * 
	 * @param dealerId 经销商Id
	 * @return
	 * @throws Exception
	 */
	public  List<Map<String, Object>> getDealerPrice(Long companyId,String dealerId){

		StringBuffer query = new StringBuffer();  
		query.append("select pr.relation_id,p.price_id, \n");  
		query.append("       p.price_code, \n");  
		query.append("       p.price_desc, \n");  
		query.append("       to_char(p.start_date,'yyyy-mm-dd') as start_date, \n");  
		query.append("       to_char(p.end_date,'yyyy-mm-dd') as end_date, \n");  
		query.append("       pr.is_default \n");  
		query.append("  from tt_vs_price p, tm_dealer_price_relation pr \n");  
		query.append(" where p.price_id = pr.price_id \n");  
		query.append("   and pr.dealer_id = "+dealerId+" \n");  
		query.append("   order by p.create_date desc \n");
	
		return pageQuery(query.toString(),null,getFunName());
	}
	
	public Map<String, Object> getMyMap(Long companyId,String dealerId) {

		StringBuffer sql = new StringBuffer();
	    sql.append("SELECT TVP.PRICE_ID,\n");  
	    sql.append("       TVP.PRICE_CODE,\n");  
	    sql.append("       TVP.PRICE_DESC,\n"); 
	    sql.append("       TVP.START_DATE,\n"); 
	    sql.append("       TVP.END_DATE,\n"); 
	    sql.append("       TVP.CREATE_DATE,\n");
	    sql.append("       TC.CODE_DESC,\n");  
	    sql.append("       TDPR.RELATION_ID,TVP.CREATE_DATE\n");  
	    sql.append("  FROM vw_TT_VS_PRICE              TVP,\n");  
	    sql.append("       vw_TM_DEALER_PRICE_RELATION TDPR,\n");  
	    sql.append("       TC_CODE                  TC,\n");  
	    sql.append("       TM_DEALER                TD\n");  
	    sql.append(" WHERE TDPR.PRICE_ID = TVP.PRICE_ID\n");  
	    sql.append("   AND TDPR.DEALER_ID = TD.DEALER_ID\n");  
	    sql.append("   AND TDPR.IS_DEFAULT = TC.CODE_ID\n");  
//	    sql.append("	AND TDPR.CREATE_BY = '-1'\n");
	    sql.append("   AND TVP.COMPANY_ID = " + companyId + "\n");  
	    sql.append("   AND TDPR.DEALER_ID = '" + dealerId + "'\n");  
	    sql.append("   order by TVP.CREATE_DATE DESC  ");
	    //sql.append(" ORDER BY TC.CODE_DESC DESC, TVP.PRICE_DESC\n");
	    
		List<Map<String, Object>> list = pageQuery(sql.toString(), null,"com.infodms.dms.dao.productmanage.ProductManageDao.getMyMap");
		return list.size() != 0 ? (Map<String, Object>) list.get(0) : null;
	}

	/**
	 * 获得索赔车型组
	 * 
	 * @param modelId
	 * @param wrgroupType
	 * @return
	 */
	public Map<String, Object> getModelGroup(Long modelId, Integer wrgroupType) {

		StringBuffer sql = new StringBuffer();

		sql
				.append("SELECT TAWMI.MODEL_ID, TAWMI.WRGROUP_ID, TAWMG.WRGROUP_TYPE\n");
		sql
				.append("  FROM TT_AS_WR_MODEL_ITEM TAWMI, TT_AS_WR_MODEL_GROUP TAWMG\n");
		sql.append(" WHERE TAWMI.WRGROUP_ID = TAWMG.WRGROUP_ID\n");
		sql.append("   AND TAWMI.MODEL_ID = " + modelId + "\n");
		sql.append("   AND TAWMG.WRGROUP_TYPE = " + wrgroupType);

		List<Map<String, Object>> list = pageQuery(sql.toString(), null,
				"com.infodms.dms.dao.productmanage.ProductManageDao.getModelGroup");
		return list.size() != 0 ? (Map<String, Object>) list.get(0) : null;
	}

	public String getMaterialGroupTreeCode(String parentId) {

		StringBuffer sql = new StringBuffer();

		sql.append("SELECT F_GET_MATERIALGROUPCODE('" + parentId
				+ "') CODE FROM DUAL\n");

		List<Map<String, Object>> list = pageQuery(sql.toString(), null,
				"com.infodms.dms.dao.productmanage.ProductManageDao.getMaterialGroupTreeCode");

		Map<String, Object> map = list.size() != 0 ? (Map<String, Object>) list
				.get(0) : null;
		String code = (map == null ? "" : (String) map.get("CODE"));
		return code;
	}

	/**
	 * 配额导入校验物料是否存在
	 * 
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> quotaImportCheckMaterial(Map<String, Object> map) {
		String userId = (String) map.get("userId");
		String companyId = (String) map.get("companyId");
		List<Object> params = new LinkedList<Object>();
		StringBuffer sql = new StringBuffer("");

		sql.append("select p.NUMBER_NO ROW_NUMBER\n");
		sql.append("  from TMP_VS_PRICE_DTL p\n");
		sql.append(" where 1 = 1\n");
		sql.append("   and p.USER_ID =" + userId + "\n");
		sql.append("   and not exists (select 1\n");
		sql.append("       from TM_VHCL_MATERIAL_GROUP g\n");
		sql.append("      where g.GROUP_CODE = p.GROUP_CODE\n");
		sql.append("        and g.STATUS = " + Constant.STATUS_ENABLE + "\n");// 有效
		sql.append("        and g.COMPANY_ID = " + companyId + ")\n");
		sql.append("   order by TO_NUMBER(p.NUMBER_NO) asc");

		return dao.pageQuery(sql.toString(), params, getFunName());
	}

	/**
	 * 查询临时表中是否有相同数据
	 * 
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> talbeCheckDump(Map<String, Object> map) {
		String userId = (String) map.get("userId");
		List<Object> params = new LinkedList<Object>();
		StringBuffer sql = new StringBuffer("");

		sql
				.append("select p1.NUMBER_NO ROW_NUMBER1, p2.NUMBER_NO ROW_NUMBER2\n");
		sql.append("  from TMP_VS_PRICE_DTL p1, TMP_VS_PRICE_DTL p2\n");
		sql.append(" where \n");
		sql.append("   p1.PRICE_CODE = p2.PRICE_CODE\n");
		sql.append("   and p1.GROUP_CODE = p2.GROUP_CODE\n");
		sql.append("   and p1.NUMBER_NO <> p2.NUMBER_NO\n");
		sql.append("   and p1.USER_ID = p2.USER_ID\n");
		sql.append("   and p1.USER_ID= ? \n");
		params.add(userId);
		sql.append("   order by TO_NUMBER(p1.NUMBER_NO)\n");

		List<Map<String, Object>> list = dao.pageQuery(sql.toString(), params,
				getFunName());

		return list;
	}
	
	/**
	 * 根据物料组id进行递归查询
	 * 
	 * @param map
	 * @return
	 */
	public List<Map<String,Object>> getAllMaterialGroup(String groupId){
		StringBuffer sql = new StringBuffer();
		List<Object> params = new LinkedList<Object>();
		sql.append("SELECT G.GROUP_ID\n");
		sql.append("  FROM TM_VHCL_MATERIAL_GROUP G\n");  
		sql.append(" START WITH G.GROUP_ID = "+groupId+"\n");  
		sql.append("CONNECT BY PRIOR G.GROUP_ID = G.PARENT_GROUP_ID\n");
		
		return dao.pageQuery(sql.toString(), params, getFunName());
	}
	
	/**
	 * 根据物料组ID获取该物料组的所有颜色
	 */
	public List<Map<String,Object>> getMaterialGroupColor(String groupId){
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT TGC.COLOR_ID,TGC.GROUP_ID,             \n");
		sql.append("       TGC.COLOR_CODE,TGC.COLOR_NAME,         \n");
		sql.append("       TGC.CREATE_DATE,(SELECT TU.NAME FROM TC_USER TU WHERE TU.USER_ID=TGC.CREATE_BY) CREATE_BY,      \n");
		sql.append("       TGC.UPDATE_DATE,(SELECT TU.NAME FROM TC_USER TU WHERE TU.USER_ID=TGC.UPDATE_BY) UPDATE_BY       \n");
		sql.append("FROM TM_GROUP_COLOR TGC                       \n");
		sql.append("WHERE TGC.GROUP_ID="+groupId+"                \n");
		return dao.pageQuery(sql.toString(), null, getFunName());
	}
	
	/**
	 * 根据物料组id删除颜色
	 * @param groupId
	 */
	public void deleteColorByGroupId(String groupId){
		StringBuffer sql = new StringBuffer();
		sql.append("DELETE TM_GROUP_COLOR TGC WHERE  TGC.GROUP_ID="+groupId+"  \n");
		dao.delete(sql.toString(), null);
	}
	
	/**
	 * 根据物料组编码获取颜色
	 * @param groupCode
	 */
	public List<Map<String,Object>> getColorByGroupCode(String groupCode){
		StringBuffer sbSql = new StringBuffer();
		sbSql.append("SELECT TGC.COLOR_ID,TGC.GROUP_ID,TGC.COLOR_CODE,TGC.COLOR_NAME \n");
		sbSql.append("FROM TM_GROUP_COLOR TGC                                        \n");
		sbSql.append("WHERE TGC.GROUP_ID=(select A.GROUP_ID                          \n");
		sbSql.append("                    from TM_VHCL_MATERIAL_GROUP A,             \n");
		sbSql.append("                         TM_VHCL_MATERIAL_GROUP B,             \n");
		sbSql.append("                         TM_VHCL_MATERIAL_GROUP C              \n");
		sbSql.append("                    where A.GROUP_ID=B.Parent_Group_Id         \n");
		sbSql.append("                          AND B.GROUP_ID=C.PARENT_GROUP_ID     \n");
		sbSql.append("                          AND C.GROUP_CODE='"+groupCode+"'     \n"); 
		sbSql.append("                    )                                          \n"); 
		List<Map<String,Object>> colorList = dao.pageQuery(sbSql.toString(), null, getFunName()); 
		return colorList;
	}
	
	//得到车系列表
	public List<Map<String, Object>> getSeriesList(){
		StringBuffer sbSql = new StringBuffer();
		sbSql.append("SELECT GROUP_ID,GROUP_NAME\n");
		sbSql.append("  FROM TM_VHCL_MATERIAL_GROUP VMG\n");
		sbSql.append(" WHERE VMG.GROUP_LEVEL = 2\n");
		sbSql.append("   AND STATUS = 10011001\n");
		sbSql.append(" ORDER BY PARENT_GROUP_ID,GROUP_ID"); 
		return pageQuery(sbSql.toString(), null, getFunName());
	}
	//得到车系列表
	public List<Map<String, Object>> getSeriesList1(Long id){
		StringBuffer sbSql = new StringBuffer();
		sbSql.append("SELECT GROUP_ID,GROUP_NAME\n");
		sbSql.append("  FROM TM_VHCL_MATERIAL_GROUP VMG\n");
		sbSql.append(" WHERE VMG.GROUP_LEVEL = 2\n");
		sbSql.append("   AND STATUS = 10011001\n");
		if(id != null){
			sbSql.append(" AND VMG.GROUP_ID = "+id+"\n");
		}
		sbSql.append(" ORDER BY PARENT_GROUP_ID,GROUP_ID"); 
		
		return pageQuery(sbSql.toString(), null, getFunName());
	}

	public void setMaterialStatus(Map<String, Object> dataMap) throws Exception
	{
		String[] materialIds = (String[])dataMap.get("mid");
		String orderSet = CommonUtils.checkNull(dataMap.get("orderSet"));
		String procuctSet = CommonUtils.checkNull(dataMap.get("procuctSet"));
		String statusUp = CommonUtils.checkNull(dataMap.get("statusUp"));
		String matType = CommonUtils.checkNull(dataMap.get("matType"));
		
		//String materialId = "";
		
//		for(int i=0;i<materialIds.length;i++) {
//			materialId += materialIds[i] + ",";
//		}
		
	//	materialId = materialId.substring(0, materialId.length() - 1);
		
		StringBuffer updateSql = new StringBuffer(); 
		List<Object> params = new ArrayList<Object>();
		updateSql.append("UPDATE TM_VHCL_MATERIAL T SET\n");
		if(!orderSet.equals("")) {
			params.add(orderSet);
			updateSql.append("T.ORDER_FLAG = ?\n");
		}
		if(!procuctSet.equals("")) {
			params.add(procuctSet);
			if(!orderSet.equals("")){
				updateSql.append(",");
			}
			updateSql.append("T.PROCUCT_FLAG = ?\n");
		}
		if(!statusUp.equals("")) {
			params.add(statusUp);
			if(!procuctSet.equals("") || !orderSet.equals("")){
				updateSql.append(",");
			}
			updateSql.append("T.STATUS = ?\n");
		}
		if(!matType.equals("")) {
			params.add(matType);
			if(!statusUp.equals("") || !procuctSet.equals("") || !orderSet.equals("")){
				updateSql.append(",");
			}
			updateSql.append("T.MAT_TYPE = ?\n");
		}
		updateSql.append("WHERE 1=1\n");
		updateSql.append(Utility.getConSqlByParamForEqual(materialIds, params, "T", "MATERIAL_ID"));
		/*if(!orderSet.equals("") && !procuctSet.equals("")) {
			params.add(orderSet);
			params.add(procuctSet);
			updateSql = "UPDATE TM_VHCL_MATERIAL SET ORDER_FLAG = ?, PROCUCT_FLAG = ? WHERE MATERIAL_ID IN("+CarSubmissionQueryDao.getInstance().getSqlBuffer(materialIds, params)+")";
		} else if(!orderSet.equals("")) {
			params.add(orderSet);
			updateSql = "UPDATE TM_VHCL_MATERIAL SET ORDER_FLAG = ? WHERE MATERIAL_ID IN("+CarSubmissionQueryDao.getInstance().getSqlBuffer(materialIds, params)+")";
		} else {
			params.add(procuctSet);
			updateSql = "UPDATE TM_VHCL_MATERIAL SET PROCUCT_FLAG = ? WHERE MATERIAL_ID IN("+CarSubmissionQueryDao.getInstance().getSqlBuffer(materialIds, params)+")";
		}*/
		
		dao.update(updateSql.toString(), params);
	}

	public String getMaterialNameByCode(String groupCode) throws Exception
	{
		StringBuffer sbSql = new StringBuffer();
		
		sbSql.append("SELECT A.GROUP_NAME FROM TM_VHCL_MATERIAL_GROUP A WHERE A.GROUP_CODE = ?\n");
		
		List<Object> params = new ArrayList<Object>();
		params.add(groupCode);
		
		return dao.pageQueryMap(sbSql.toString(), params, getFunName()).get("GROUP_NAME").toString();
		
	}
	
	public boolean isExistBydealerCode(String dealerCode) throws Exception {
		StringBuffer sbSql = new StringBuffer();

		sbSql.append("SELECT A.dealer_code FROM tm_dealer A WHERE A.dealer_code = ?\n");

		List<Object> params = new ArrayList<Object>();
		params.add(dealerCode);
		List<Map<String, Object>> pageQuery = dao.pageQuery(sbSql.toString(), params, getFunName());
		if (pageQuery != null) {
			if (pageQuery.size() > 0) {
				return true;
			} else {
				return false;
			}
		}else{
			return false;
		}
	}
}
