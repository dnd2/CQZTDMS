/**
 * FileName: MaterialPriceDao.java
 * Author: yudanwen
 * Date: 2013-4-7 下午12:06:30
 * Email: guanyunhong@oraro.net
 * 
 * Copyright ORARO Corporation 2013
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
import com.infodms.dms.po.TmpVsPriceDtlOemPO;
import com.infodms.dms.po.TmpVsPriceDtlOemPO;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

/**
 * @Description
 * @author  ranj
 * @date 2013-12-12 下午12:06:30
 * @version 2.0
 */
public class MaterialPriceDao  extends BaseDao{
	public static Logger logger = Logger.getLogger(MaterialPriceDao.class);
	private static final MaterialPriceDao dao = new MaterialPriceDao();

	public static final MaterialPriceDao getInstance() {
		return dao;
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
		String areaId=(String)map.get("areaId");
		String poseId=(String)map.get("poseId");
		List par=new ArrayList();
		StringBuffer sql = new StringBuffer("\n");

		sql.append("SELECT TVM.MATERIAL_ID,\n");
		sql.append("       TVM.MATERIAL_CODE,\n");  
		sql.append("       TVM.MATERIAL_NAME,\n");  
		sql.append("       TVM.ERP_NAME, --ERP名称\n");  
		sql.append("       TVM.COLOR_CODE,\n");  
		sql.append("       TVM.COLOR_NAME,\n");  
		sql.append("       TVM.COM_VHCL_PRICE VHCL_PRICE,\n");  
		sql.append("       DECODE(TVM.ORDER_FLAG, "+Constant.NASTY_ORDER_REPORT_TYPE_01+", '可提报', '不可提报') ORDER_FLAG, ---是否可提报\n");  
		sql.append("       TVM.STATUS\n");  
		sql.append("  FROM TM_VHCL_MATERIAL TVM,VW_MATERIAL_GROUP_MAT B,TM_AREA_GROUP TAG\n"); 
		sql.append(" WHERE 1 = 1\n");  
		sql.append(" AND TVM.MATERIAL_ID=B.MATERIAL_ID AND B.SERIES_ID=TAG.MATERIAL_GROUP_ID \n");
		
//		if("".equals(areaId)){
//			sql.append("     AND EXISTS(SELECT  TVMG.GROUP_ID \n");
//			sql.append("      FROM   TM_VHCL_MATERIAL_GROUP TVMG\n");
//			sql.append("     WHERE   TVMG.GROUP_LEVEL = 4 AND TVMG.STATUS = "+Constant.STATUS_ENABLE+" AND RR.GROUP_ID=TVMG.GROUP_ID\n");
//			sql.append("START WITH   TVMG.GROUP_ID IN\n");
//			sql.append("                   (SELECT   AA.MATERIAL_GROUP_ID\n");
//			sql.append("                      FROM   TM_AREA_GROUP AA, TM_POSE_BUSINESS_AREA BB\n");
//			sql.append("                     WHERE   AA.AREA_ID = BB.AREA_ID AND BB.POSE_ID = "+poseId+")\n");
//			sql.append("CONNECT BY   PRIOR TVMG.GROUP_ID = TVMG.PARENT_GROUP_ID)\n");
//		}
		if(!"".equals(areaId)){			
//			sql.append("     AND EXISTS(SELECT  TVMG.GROUP_ID \n");
//			sql.append("      FROM   TM_VHCL_MATERIAL_GROUP TVMG\n");
//			sql.append("     WHERE   TVMG.GROUP_LEVEL = 4 AND TVMG.STATUS = "+Constant.STATUS_ENABLE+" AND RR.GROUP_ID=TVMG.GROUP_ID\n");
//			sql.append("START WITH   TVMG.GROUP_ID IN\n");
//			sql.append("                   (SELECT   AA.MATERIAL_GROUP_ID\n");
//			sql.append("                      FROM   TM_AREA_GROUP AA, TM_POSE_BUSINESS_AREA BB\n");
//			sql.append("                     WHERE   AA.AREA_ID = BB.AREA_ID AND AA.AREA_ID in("+areaId+"))\n");
//			sql.append("CONNECT BY   PRIOR TVMG.GROUP_ID = TVMG.PARENT_GROUP_ID)\n");
			sql.append("AND TAG.AREA_ID=?\n");
			par.add(new Long(areaId));
		}	
		
		if (!CommonUtils.isNullString(materialCode)) {
			sql.append("   AND TVM.MATERIAL_CODE LIKE ?\n");  
			par.add("%"+materialCode+"%");
		}
		if (!CommonUtils.isNullString(materialName)) {
			sql.append("   AND TVM.MATERIAL_NAME LIKE ?\n");  
			par.add("%"+materialName+"%");
		}
		if (!CommonUtils.isNullString(status)) {
			sql.append("   AND TVM.STATUS = ?\n");  
			par.add(status);
		}
		if (!CommonUtils.isNullString(groupCode)) {
			sql.append("   AND TVM.MATERIAL_ID IN\n");  
			sql.append("       (SELECT MGR.MATERIAL_ID\n");  
			sql.append("          FROM TM_VHCL_MATERIAL_GROUP_R MGR, TM_VHCL_MATERIAL_GROUP VMG\n");  
			sql.append("         WHERE MGR.GROUP_ID = VMG.GROUP_ID\n");  
			sql.append("           AND VMG.GROUP_CODE LIKE ?)\n");
			par.add("%"+groupCode+"%");
		}


		PageResult<Map<String, Object>> ps = dao.pageQuery(
				sql.toString(),par,"com.infodms.dms.dao.sales.planmanage.getMaterialManageQueryList",pageSize, curPage);
		return ps;
	}

	/*
	 * 单表查询TMP_YEARLY_PLAN
	 */
	public List<TmpVsPriceDtlOemPO> selectTmpVsPriceDtl(TmpVsPriceDtlOemPO po){
		List<Object> params = new LinkedList<Object>();
		StringBuffer sql=new StringBuffer("");

		sql.append("SELECT *\n");
		sql.append("  FROM TMP_VS_PRICE_DTL_OEM\n");  
		sql.append(" WHERE 1 = 1\n");  
		sql.append("   AND User_Id = ?\n");  
		params.add(po.getUserId());
		sql.append(" order by NUMBER_NO asc");
		return dao.select(TmpVsPriceDtlOemPO.class, sql.toString(), params);
	}

	 /*
	  * 验证物料编码是否重复
	  */
	 public  List<Map<String, Object>> MaterialManagegroupcode(String salesPrice){
		 StringBuffer sql=new StringBuffer("");
		 
		 sql.append("SELECT T1.NUMBER_NO NUMBER_NO1,T2.NUMBER_NO NUMBER_NO2\n");
		 sql.append("  FROM TMP_VS_PRICE_DTL_OEM T1, TMP_VS_PRICE_DTL_OEM T2 \n");  
		 sql.append(" WHERE T1. GROUP_CODE= T2.GROUP_CODE 1\n");  
		 sql.append(" and T1.NUMBER_NO <> T2.NUMBER_NO\n");  
		 sql.append(" and T1.SALES_PRICE  not null\n"); 
		 sql.append(" and T1.SALES_PRICE = "+salesPrice+"");  
		 return dao.pageQuery(sql.toString(), null, getFunName());
		 
	 }
	 /*
	  * 验证物料编码是否重复
	  */

	 
	 
		public  List<Map<String, Object>> MaterialManagegroupcode2(Map<String, Object> map){

			String userId=map.get("userId").toString();
			List<Object> params = new LinkedList<Object>();
			StringBuffer sql=new StringBuffer("");
			sql.append("select p.NUMBER_NO ROW_NUMBER \n");
			sql.append("  from TMP_VS_PRICE_DTL_OEM p\n");  
			sql.append(" where 1 = 1\n");  
			sql.append("   and p.USER_ID ="+userId+"\n");  
			sql.append("   and p.GROUP_CODE is not null\n");
			sql.append("   and not exists (select 1\n");  
			sql.append("   from TM_VHCL_MATERIAL td\n");
			sql.append("   where td.MATERIAL_CODE = p.GROUP_CODE)\n");  
			sql.append("   order by TO_NUMBER(p.NUMBER_NO) asc");

			return dao.pageQuery(sql.toString(), params, getFunName());
		}
		
		
		/**
		 * 查询临时表中是否有相同数据
		 * 
		 * @param map
		 * @return
		 */
	//  TMP_VS_PRICE_DTL_OEM,TM_VHCL_MATERIAL
		public List<Map<String, Object>> talbeCheckDump(Map<String, Object> map) {
			String userId = (String) map.get("userId");
			List<Object> params = new LinkedList<Object>();
			StringBuffer sql = new StringBuffer("");

			sql
					.append("select p1.NUMBER_NO ROW_NUMBER1, p2.NUMBER_NO ROW_NUMBER2\n");
			sql.append("  from TMP_VS_PRICE_DTL_OEM p1, TMP_VS_PRICE_DTL_OEM p2\n");
			sql.append(" where \n");
			sql.append("   p1.GROUP_CODE = p2.GROUP_CODE\n");
			sql.append("   and p1.NUMBER_NO <> p2.NUMBER_NO\n");
			sql.append("   and p1.USER_ID = p2.USER_ID\n");
			sql.append("   and p1.USER_ID= ? \n");
			params.add(userId);
			sql.append("   order by TO_NUMBER(p1.NUMBER_NO)\n");

			List<Map<String, Object>> list = dao.pageQuery(sql.toString(), params,
					getFunName());

			return list;
		}
			//  TMP_VS_PRICE_DTL_OEM,TM_VHCL_MATERIAL
		/**
		 * 查询临时表的数据 与正式表 状态为有效且ID相同
		 * 
		 * @param map
		 * @return
		 */
		public List<Map<String, Object>> quotaImportCheckMaterial(String GROUP_CODE) {
			List<Object> params = new LinkedList<Object>();
			//StringBuffer sql = new StringBuffer("");

			StringBuffer sbSql=new StringBuffer();
			sbSql.append("SELECT MATERIAL_CODE FROM TM_VHCL_MATERIAL A WHERE A.MATERIAL_CODE='"+GROUP_CODE+"'");

			return dao.pageQuery(sbSql.toString(), params, getFunName());
		}
		
		/**
		 * 查询临时表的数据 
		 * 
		 * @param map
		 * @return
		 */
		
		public PageResult<Map<String, Object>> getMaterialPriceManageImportTempList(
				Map<String, Object> map, int curPage, int pageSize) {

			String userId = (String) map.get("userId");

			StringBuffer sql = new StringBuffer();
			sql.append("SELECT TVPD.PRICE_CODE,\n");
			sql.append("       TVPD.NUMBER_NO,\n");
			sql.append("       TVPD.GROUP_CODE,\n");
			sql.append("       T.NAME,\n");
			sql.append("       TVPD.SALES_PRICE,\n");
			sql.append("       TVPD.NO_TAX_PRICE\n");
			sql.append("  FROM TMP_VS_PRICE_DTL_OEM TVPD,\n");
			sql.append("  TC_USER T where t.USER_ID =TVPD.USER_ID\n");
			sql.append(" AND TVPD.USER_ID = " + userId + " order by TO_NUMBER(TVPD.NUMBER_NO)  ");

			PageResult<Map<String, Object>> ps = pageQuery(sql.toString(),null,getFunName(),pageSize, curPage);
			return ps;
		}
		
		/**
		 * 临时表的数据插入正式表
		 * 
		 * @param map
		 * @return
		 */
		
		public void insertTtVsPriceDtl(Map<String, Object> map) {

			String userId = (String) map.get("userId");
			StringBuffer sql = new StringBuffer();
			sql.append(" merge into tm_vhcl_material h\n");
			sql.append(" using (SELECT TVPD.USER_ID, TVPD.GROUP_CODE, TVPD.SALES_PRICE as SALES_PRICE\n");
			sql.append(" FROM TMP_VS_PRICE_DTL_OEM TVPD\n");
			sql.append(" where TVPD.USER_ID = '"+userId+"') TVPD\n");
			sql.append(" ON (TVPD.group_code = h.material_code)\n");
			sql.append(" when matched then\n");
			sql.append(" update set  h.COM_VHCL_PRICE=TVPD.SALES_PRICE\n");
			

			dao.update(sql.toString(), null);
		}
	
	@Override
	
		protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}
	/*
	 * 获取单条物料
	 */
	public Map<String,Object> getMat(String matId){
		List<Object> params = new LinkedList<Object>();
		params.add(matId);
		StringBuffer sql=new StringBuffer("");
		sql.append("SELECT A.MATERIAL_ID, A.MATERIAL_CODE, A.MATERIAL_NAME, A.COM_VHCL_PRICE VHCL_PRICE\n");
		sql.append("  FROM TM_VHCL_MATERIAL A WHERE A.MATERIAL_ID = ?"); 
		return dao.pageQueryMap(sql.toString(), params, getFunName());
	}
	
	
	
}
