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
import com.infodms.dms.dao.common.BaseDao;
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
public class ProductManageNewDao extends BaseDao<PO> {
	public static Logger logger = Logger.getLogger(ProductManageNewDao.class);
	private static final ProductManageNewDao dao = new ProductManageNewDao();

	public static final ProductManageNewDao getInstance() {
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

		StringBuffer sql = new StringBuffer();

		sql
				.append("SELECT TVMG1.GROUP_ID, TVMG1.GROUP_CODE, TVMG1.GROUP_NAME, TVMG1.STATUS, TVMG1.GROUP_LEVEL\n");
		sql
				.append("  FROM TM_VHCL_MATERIAL_GROUP TVMG1, TM_VHCL_MATERIAL_GROUP TVMG2\n");
		sql.append(" WHERE TVMG1.PARENT_GROUP_ID = TVMG2.GROUP_ID(+)\n");
		if (!groupCode.equals("")) {
			sql.append("   AND TVMG1.GROUP_CODE LIKE '%" + groupCode + "%'\n");
		}
		if (!groupName.equals("")) {
			sql.append("   AND TVMG1.GROUP_NAME LIKE '%" + groupName + "%'\n");
		}
		if (!status.equals("")) {
			sql.append("   AND TVMG1.STATUS =" + status + "\n");
		}
		if (!companyId.equals("")) {
			sql.append("   AND TVMG1.COMPANY_ID =" + companyId + "\n");
		}
		if (!parentGroupCode.equals("")) {
			sql.append("   AND TVMG2.GROUP_CODE LIKE '%" + parentGroupCode
					+ "%'");
		}

		PageResult<Map<String, Object>> ps = pageQuery(
				sql.toString(),
				null,
				"com.infodms.dms.dao.productmanage.ProductManageNewDao.getMaterialGroupManageQueryList",
				pageSize, curPage);
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
		//String groupCode = (String) map.get("groupCode");
		String companyId = (String) map.get("companyId");

		StringBuffer sql = new StringBuffer();
		sql.append("select * from TM_VHCL_MATERIAL TVM where TVM.MATERIAL_ID NOT IN(SELECT TVMGR.MATERIAL_ID FROM TM_VHCL_MATERIAL_GROUP_R TVMGR)");
		if (!materialCode.equals("")) {
			sql.append("   AND TVM.MATERIAL_CODE LIKE '%" + materialCode
					+ "%'\n");
		}
		if (!materialName.equals("")) {
			sql.append("   AND TVM.MATERIAL_NAME LIKE '%" + materialName
					+ "%'\n");
		}
		if (!status.equals("")) {
			sql.append("   AND TVM.STATUS = " + status + "\n");
		}
		if (!companyId.equals("")) {
			sql.append("   AND TVM.COMPANY_ID =" + companyId + "\n");
		}

		PageResult<Map<String, Object>> ps = pageQuery(
				sql.toString(),
				null,
				"com.infodms.dms.dao.productmanage.ProductManageNewDao.getMaterialManageQueryList",
				pageSize, curPage);
		return ps;
	}
	
	public List<Map<String, Object>> getErpMaterial(String modelCode) {
		StringBuffer sql = new StringBuffer("\n");
		
		sql.append("select * from TM_VHCL_MATERIAL TVM where TVM.MATERIAL_ID NOT IN(SELECT TVMGR.MATERIAL_ID FROM TM_VHCL_MATERIAL_GROUP_R TVMGR)");
		
		if (!CommonUtils.isNullString(modelCode)) {
			sql.append("   AND TVM.MATERIAL_CODE LIKE '" + modelCode
					+ ".%'\n");
		}
		
		sql.append("and tvm.status = ").append(Constant.STATUS_ENABLE).append("\n");
		
		List<Map<String, Object>> materialList = dao.pageQuery(sql.toString(), null, dao.getFunName()) ;
		
		return materialList ;
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
		sql.append("SELECT distinct TVP.PRICE_ID,TVP.PRICE_CODE, TVP.PRICE_DESC,tc.code_desc,TDPR.Relation_Id\n");
		sql.append("  FROM vw_TT_VS_PRICE TVP,vw_tm_dealer_price_relation TDPR,tc_code tc,tm_dealer td\n");
		sql.append(" WHERE TVP.START_DATE < SYSDATE\n");
		sql.append("   AND TVP.END_DATE > SYSDATE");
		sql.append("   AND TVP.COMPANY_ID = " + companyId + "");
		sql.append("   AND  TDPR.Price_Id=TVP.PRICE_ID ");
		sql.append("   AND TDPR.DEALER_ID=td.dealer_id ");
		sql.append("  AND TDPR.IS_DEFAULT=tc.code_id");
		sql.append("  AND TDPR.Dealer_Id='"+dealerId+"'");

		List<Map<String, Object>> list = pageQuery(sql.toString(), null,
				"com.infodms.dms.dao.productmanage.ProductManageNewDao.getmyPriceTypeList");
		return list;
	}
	/*
	 * 获得特殊价格排除列表
	 */
	public List<Map<String, Object>> getmyOtherPriceTypeList(Long companyId,String dealerId) {

		StringBuffer sql = new StringBuffer();
		sql.append("select a.PRICE_ID, a.PRICE_DESC from vw_TT_VS_PRICE a where a.PRICE_ID not in((SELECT distinct TVP.PRICE_ID FROM vw_TT_VS_PRICE TVP,vw_tm_dealer_price_relation TDPR,tc_code tc,tm_dealer td WHERE TVP.START_DATE < SYSDATE AND TVP.END_DATE > SYSDATE   AND TVP.COMPANY_ID = " + companyId + "   AND  TDPR.Price_Id=TVP.PRICE_ID   AND TDPR.IS_DEFAULT=tc.code_id  AND TDPR.Dealer_Id='"+dealerId+"'))  and a.START_DATE < SYSDATE AND a.END_DATE > SYSDATE AND a.COMPANY_ID =" + companyId);
//		sql.append("SELECT distinct TVP.PRICE_ID,TVP.PRICE_CODE, TVP.PRICE_DESC,tc.code_desc,TDPR.Relation_Id\n");
//		sql.append("  FROM TT_VS_PRICE TVP,tm_dealer_price_relation TDPR,tc_code tc,tm_dealer td\n");
//		sql.append(" WHERE TVP.START_DATE < SYSDATE\n");
//		sql.append("   AND TVP.END_DATE > SYSDATE");
//		sql.append("   AND TVP.COMPANY_ID = " + companyId + "");
//		sql.append("   AND  TDPR.Price_Id!=TVP.PRICE_ID ");
//		sql.append("  AND TDPR.IS_DEFAULT=tc.code_id");
//		sql.append("  AND TDPR.Dealer_Id='"+dealerId+"'");

		List<Map<String, Object>> list = pageQuery(sql.toString(), null,
				"com.infodms.dms.dao.productmanage.ProductManageNewDao.getmyOtherPriceTypeList");
		return list;
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
				"com.infodms.dms.dao.productmanage.ProductManageNewDao.getPriceTypeList");
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
			Map<String, Object> map, int curPage, int pageSize) {

		String groupCode = (String) map.get("groupCode");
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
		sql.append("   AND TVP.START_DATE < SYSDATE\n");
		sql.append("   AND TVP.END_DATE > SYSDATE\n");
		if (!groupCode.equals("")) {
			sql.append("   AND TVM.GROUP_CODE LIKE '%" + groupCode + "%'\n");
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
				"com.infodms.dms.dao.productmanage.ProductManageNewDao.getMaterialPriceManageTypeQueryList",
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
				"com.infodms.dms.dao.productmanage.ProductManageNewDao.getModelGroupList");
		return list;
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
				"com.infodms.dms.dao.productmanage.ProductManageNewDao.getModelGroup");
		return list.size() != 0 ? (Map<String, Object>) list.get(0) : null;
	}

	public String getMaterialGroupTreeCode(String parentId) {

		StringBuffer sql = new StringBuffer();

		sql.append("SELECT F_GET_MATERIALGROUPCODE('" + parentId
				+ "') CODE FROM DUAL\n");

		List<Map<String, Object>> list = pageQuery(sql.toString(), null,
				"com.infodms.dms.dao.productmanage.ProductManageNewDao.getMaterialGroupTreeCode");

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
	
	public static boolean groupIsExists(String groupCode, String level) {
		List<Object> params = new ArrayList<Object>() ;
		
		StringBuffer sql = new StringBuffer("\n") ;
		
		sql.append("select * from tm_vhcl_material_group tvmg where tvmg.group_code = ? and tvmg.group_level = ?\n");
		params.add(groupCode) ;
		params.add(level) ;
		
		Map<String, Object> groupMap = dao.pageQueryMap(sql.toString(), params, dao.getFunName()) ;
		
		if(!CommonUtils.isNullMap(groupMap)) {
			return true ;
		} else {
			return false ;
		}
	}
	
	public static String getMaterialId(String materialCode) {
		String materialId = "" ;
		
		List<Object> params = new ArrayList<Object>() ;
		
		StringBuffer sql = new StringBuffer("\n") ;
		
		sql.append("select tvm.material_id from tm_vhcl_material tvm where tvm.material_code = ? and tvm.status = ").append(Constant.STATUS_ENABLE).append("\n");
		params.add(materialCode) ;
		
		Map<String, Object> materialMap = dao.pageQueryMap(sql.toString(), params, dao.getFunName()) ;
		
		if(!CommonUtils.isNullMap(materialMap)) {
			materialId = materialMap.get("MATERIAL_ID").toString() ;
		} 
		
		return materialId ;
	}
	
	public static boolean materialIsExistsR(String materialId) {
		List<Object> params = new ArrayList<Object>() ;
		
		StringBuffer sql = new StringBuffer("\n") ;
		
		sql.append("select * from tm_vhcl_material_group_r tvmgr where tvmgr.material_id = ?\n");
		params.add(materialId) ;
		
		Map<String, Object> materialRMap = dao.pageQueryMap(sql.toString(), params, dao.getFunName()) ;
		
		if(!CommonUtils.isNullMap(materialRMap)) {
			return true ;
		} else {
			return false ;
		}
	}
	
	private void insCode(String code, String modelCode, String parentCode, String level, Long userId, Long oemCompanyId) {
		StringBuffer sql = new StringBuffer("\n") ;
		
		sql.append("insert into tm_vhcl_material_group\n");
		sql.append("  (group_id,\n");  
		sql.append("   group_code,\n");  
		sql.append("   group_name,\n");  
		sql.append("   model_code,\n");  
		sql.append("   group_level,\n");  
		sql.append("   tree_code,\n");  
		sql.append("   parent_group_id,\n");  
		sql.append("   status,\n");  
		sql.append("   forcast_flag,\n");  
		sql.append("   create_date,\n");  
		sql.append("   create_by,\n");  
		sql.append("   company_id,\n");  
		sql.append("   if_status)\n");  
		sql.append("values\n");  
		sql.append("  (f_getid(),\n");  
		sql.append("   '").append(code).append("',\n");  
		sql.append("   '").append(code).append("',\n");
		sql.append("   '").append(modelCode).append("',\n");  
		sql.append("   ").append(level).append(",\n");  
		
		if("4".equals(level)) {
			sql.append("   (select substr(max(tvmg.tree_code), 1, 11) ||\n");  
			sql.append("           (to_number(substr(max(tvmg.tree_code), 12, 1)) + 1)\n");  
			sql.append("      from tm_vhcl_material_group tvmg\n");  
			sql.append("     where tvmg.group_level = 4), -- treeCode\n");  
		} else if("3".equals(level)) {
			sql.append("(select substr(max(tvmg.tree_code), 1, 8) ||\n");
			sql.append("             (to_number(substr(max(tvmg.tree_code), 9, 1)) + 1)\n");  
			sql.append("        from tm_vhcl_material_group tvmg\n");  
			sql.append("       where tvmg.group_level = 3), -- treeCode\n");
		}
		
		sql.append("   (select tvmg1.group_id\n");  
		sql.append("      from tm_vhcl_material_group tvmg1\n");  
		sql.append("     where tvmg1.group_code = '").append(parentCode).append("'),\n");  
		sql.append("   ").append(Constant.STATUS_ENABLE).append(",\n");  
		sql.append("   0,\n");  
		sql.append("   sysdate,\n");  
		sql.append("   ").append(userId).append(",\n");  
		sql.append("   ").append(oemCompanyId).append(",\n");  
		sql.append("   0)\n");

		dao.insert(sql.toString()) ;
	}
	
	public static void insLink(String materialId, String packageCode, Long userId) {
		StringBuffer sql = new StringBuffer("\n") ;
		
		sql.append("insert into tm_vhcl_material_group_r\n");
		sql.append("  (id,\n");  
		sql.append("   material_id,\n");  
		sql.append("   group_id,\n");  
		sql.append("   create_date,\n");  
		sql.append("   create_by)\n");  
		sql.append("values\n");  
		sql.append("  (f_getid(),\n");  
		sql.append("   ").append(materialId).append(",\n");  
		sql.append("   (select tvmg.group_id\n");  
		sql.append("      from tm_vhcl_material_group tvmg\n");  
		sql.append("     where tvmg.group_code = '").append(packageCode).append("'),\n");  
		sql.append("   sysdate,\n");  
		sql.append("   ").append(userId).append(")\n");

		dao.insert(sql.toString()) ;
	}
	
	public static void updateVPacakge(String materialId) {
		StringBuffer sql = new StringBuffer("\n") ;
		
		sql.append("UPDATE TM_VEHICLE V SET V.PACKAGE_ID = (SELECT GR.GROUP_ID FROM TM_VHCL_MATERIAL_GROUP_R GR WHERE GR.MATERIAL_ID = V.MATERIAL_ID) WHERE V.PACKAGE_ID IS NULL\n");
		
		if(!CommonUtils.isNullString(materialId)) {
			sql.append("   and v.MATERIAL_ID = ").append(materialId).append("\n") ;
		}

		dao.update(sql.toString(), null) ;
	}
	
	public static void updateVModel(String materialId) {
		StringBuffer sql = new StringBuffer("\n") ;
		
		sql.append("UPDATE TM_VEHICLE V SET V.MODEL_ID = (SELECT G.parent_group_id FROM TM_VHCL_MATERIAL_GROUP G WHERE G.Group_Id = V.package_id) WHERE V.model_id IS NULL\n");
		
		if(!CommonUtils.isNullString(materialId)) {
			sql.append("   and v.MATERIAL_ID = ").append(materialId).append("\n") ;
		}
		
		dao.update(sql.toString(), null) ;
	}
	
	public static void updateVSeries(String materialId) {
		StringBuffer sql = new StringBuffer("\n") ;
		
		sql.append("UPDATE TM_VEHICLE V SET V.series_id = (SELECT G.parent_group_id FROM TM_VHCL_MATERIAL_GROUP G WHERE G.Group_Id = V.model_id) WHERE V.series_id IS NULL\n");
		
		if(!CommonUtils.isNullString(materialId)) {
			sql.append("   and v.MATERIAL_ID = ").append(materialId).append("\n") ;
		}
		
		dao.update(sql.toString(), null) ;
	}
	
	public static void insPackage(String code, String modelCode, Long userId, Long oemCompanyId) {
		ProductManageNewDao pmnd = new ProductManageNewDao() ;
		
		pmnd.insCode(code, modelCode, modelCode, "4", userId, oemCompanyId) ;
	}
	
	public static void insModel(String code, String seriesCode, Long userId, Long oemCompanyId) {
		ProductManageNewDao pmnd = new ProductManageNewDao() ;
		
		pmnd.insCode(code, "", seriesCode, "3", userId, oemCompanyId) ;
	}
}
