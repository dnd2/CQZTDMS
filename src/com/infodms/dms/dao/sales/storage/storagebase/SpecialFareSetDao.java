package com.infodms.dms.dao.sales.storage.storagebase;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.infodms.dms.common.Constant;
import com.infodms.dms.common.materialManager.MaterialGroupManagerDao;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.po.TmSpecialCityFarePO;
import com.infodms.dms.po.TtSalesFarePO;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

/**
 * 
 * @ClassName     : FareSetDao 
 * @Description   : 运费设定DAO 
 * @author        : ranjian
 * CreateDate     : 2013-4-1
 */
public class SpecialFareSetDao extends BaseDao<PO>{
	private static final SpecialFareSetDao dao = new SpecialFareSetDao ();
	public static final SpecialFareSetDao getInstance() {
		return dao;
	}
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}
	/**
	 * 运费设定信息查询
	 * @param map
	 * @param curPage
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	public PageResult<Map<String, Object>> specialFareSetQuery(Map<String, Object> map, int curPage, int pageSize)throws Exception{
		String yieldly = (String)map.get("YIELDLY");
		String groupId = (String)map.get("GROUP_ID");
		String poseId = (String)map.get("poseId");
		String province = (String)map.get("PROVINCE");
		StringBuffer sbSql = new StringBuffer();
		
		sbSql.append("SELECT TBA.AREA_ID,\n");
		sbSql.append("       TBA.AREA_NAME,\n");
		sbSql.append("       TVMG.GROUP_ID,\n");
		sbSql.append("       TVMG.GROUP_CODE,\n");
		sbSql.append("       TVMG.GROUP_NAME,\n");
		sbSql.append("       TR1.REGION_CODE PROVINCE_CODE,\n");
		sbSql.append("       TR1.REGION_NAME PROVINCE_NAME,\n");
		//sbSql.append("       TR2.REGION_ID   CITY_ID,\n");
		//sbSql.append("       TR2.REGION_NAME CITY_NAME,\n");
		sbSql.append("       A.AMOUNT\n");
		sbSql.append("  FROM TM_SPECIAL_CITY_FARE   A,\n");
		sbSql.append("       TM_BUSINESS_AREA       TBA,\n");
		sbSql.append("       TM_VHCL_MATERIAL_GROUP TVMG,\n");
		sbSql.append("       TM_REGION              TR1,\n");
		sbSql.append("       TM_REGION              TR2\n");
		sbSql.append(" WHERE A.YIELDLY = TBA.AREA_ID\n");
		sbSql.append("   AND A.SERIES_ID = TVMG.GROUP_ID\n");
		sbSql.append("   AND A.PROVINCE_ID = TR1.REGION_CODE\n");
		sbSql.append("   AND A.CITY_IDD = TR2.REGION_ID\n");

		if(yieldly != null && !"".equals(yieldly)){
			sbSql.append(" AND A.YIELDLY = "+yieldly+"\n");
		}
		if(groupId != null && !"".equals(groupId)){
			sbSql.append(" AND A.SERIES_ID = "+groupId+"\n");
		}
		if(province != null && !"".equals(province)){
			sbSql.append(" AND TR1.REGION_CODE = "+province+"\n");
		}
		sbSql.append(MaterialGroupManagerDao.getPoseIdBusinessSql("A.YIELDLY", poseId));
		sbSql.append(" group by TBA.AREA_ID,\n");
		sbSql.append("          TBA.AREA_NAME,\n");
		sbSql.append("          TVMG.GROUP_ID,\n");
		sbSql.append("          TVMG.GROUP_CODE,\n");
		sbSql.append("          TVMG.GROUP_NAME,\n");
		sbSql.append("          TR1.REGION_CODE,\n");
		sbSql.append("          TR1.REGION_NAME,\n");
		//sbSql.append("          TR2.REGION_ID,\n");
		//sbSql.append("          TR2.REGION_NAME,\n");
		sbSql.append("          A.AMOUNT"); 
		PageResult<Map<String, Object>> ps= dao.pageQuery(sbSql.toString(), null, getFunName(),pageSize,curPage);
		return  ps;
	}
	/**
	 * 
	 * @Title      : 
	 * @Description: 运费设定信息添加 
	 * @param      : @param ttSalesAreaPO      
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-5
	 */
	public static void fareSetAdd(TtSalesFarePO ttSalesFarePO) {
		dao.insert(ttSalesFarePO);
    }
	/**
	 * 
	 * @Title      : 
	 * @Description: 运费设定信息修改 
	 * @param      : @param seachSalesAreaPO
	 * @param      : @param ttSalesAreaPO      
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-5
	 */
	public static void fareSetUpdate(TtSalesFarePO seachPO,TtSalesFarePO ttSalesFarePO) {
		dao.update(seachPO, ttSalesFarePO);
    }
	/**
	 * 
	 * @Title      : 
	 * @Description: 根据车系ID得到车系信息
	 * @param      : @return     车系信息
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-5
	 */
	public List<Map<String, Object>> getVhclMsg (String yieldly)throws Exception{
		StringBuffer sql= new StringBuffer();
		sql.append("select group_id,group_code,group_name from tm_vhcl_material_group A where group_level=2 and status="+Constant.STATUS_ENABLE+"\n");
		if(!"".equals(yieldly)){//查询产地下的车系
			sql.append("AND EXISTS(SELECT 1 FROM TM_AREA_GROUP B WHERE B.MATERIAL_GROUP_ID=A.GROUP_ID AND B.AREA_ID="+yieldly+")");
		}
		List<Map<String, Object>> list= dao.pageQuery(sql.toString(), null, getFunName());
		return list;
	}
	/**
	 * 
	 * @Title      : 
	 * @Description: 根据车系ID,产地，省份，城市得到特殊运费信息组
	 * @param      : @return     特殊运费信息组
	 * @return     :    
	 * @throws     :yieldly,groupId,provinceCode,cityCode
	 * LastDate    : 2013-4-5
	 */
	public List<Map<String, Object>> getUpdateMsg(String yieldly,String groupId,String provinceCode)throws Exception{
		StringBuffer sbSql= new StringBuffer();
		sbSql.append("select A.SERIES_ID,\n");
		sbSql.append("       E.GROUP_NAME,\n");
		sbSql.append("       A.PROVINCE_ID,\n");
		sbSql.append("       B.REGION_NAME PROV,\n");
		sbSql.append("       A.YIELDLY,\n");
		sbSql.append("       D.AREA_NAME,\n");
	  //sbSql.append("       A.CITY_IDD,\n");
	//	sbSql.append("       C.REGION_NAME CC,\n");
		sbSql.append("       A.AMOUNT\n");
		sbSql.append("  from TM_SPECIAL_CITY_FARE   A,\n");
		sbSql.append("       tm_region              B,\n");
		sbSql.append("       tm_region              C,\n");
		sbSql.append("       tm_business_area       D,\n");
		sbSql.append("       TM_VHCL_MATERIAL_GROUP E\n");
		sbSql.append(" where A.series_id = E.GROUP_ID\n");
		sbSql.append("   AND A.PROVINCE_ID = B.REGION_CODE\n");
		sbSql.append("   AND A.CITY_IDD = C.REGION_ID\n");
		sbSql.append("   AND A.YIELDLY = D.AREA_ID\n");
		sbSql.append("   AND E.GROUP_LEVEL = 2"); 
		sbSql.append("   AND A.YIELDLY=?"); 
		sbSql.append("   AND A.SERIES_ID=?"); 
		sbSql.append("   AND A.PROVINCE_ID=?"); 
		//sbSql.append("   AND A.CITY_IDD=?"); 
		List<Object> param=new ArrayList<Object>();
		param.add(yieldly);
		param.add(groupId);
		param.add(provinceCode);
		//param.add(cityCode);
		List<Map<String, Object>> list= dao.pageQuery(sbSql.toString(), param, getFunName());
		return list;
	}
	

	/**
	 * 根据省份CODE得到城市List
	 * @param provinceCode
	 * @return
	 */
	public List<Map<String, Object>> getCitys(String provinceCode){
		StringBuffer sbSql = new StringBuffer();
		sbSql.append("SELECT T1.REGION_CODE,\n");
		sbSql.append("       T1.REGION_NAME\n");
		sbSql.append(" FROM TM_REGION T1,\n");
		sbSql.append("      TM_REGION T2\n");
		sbSql.append(" WHERE T1.PARENT_ID = T2.REGION_ID\n");
		sbSql.append(" AND   T2.REGION_CODE = "+provinceCode+"\n");
		sbSql.append(" AND   T1.REGION_TYPE = "+Constant.REGION_TYPE_03+"\n"); 
		List<Map<String, Object>> list= dao.pageQuery(sbSql.toString(), null, getFunName());
		return list;
	}
	
	public List<Map<String, Object>> getProvinceList(){
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT REGION_ID,REGION_NAME,REGION_CODE FROM TM_REGION WHERE REGION_TYPE = "+Constant.REGION_TYPE_02+"\n");
		return dao.pageQuery(sql.toString(), null, getFunName());
	}
	
	public List<Map<String, Object>> getCityList(String provinceCode){
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT REGION_ID,REGION_NAME,REGION_CODE FROM TM_REGION WHERE REGION_TYPE = "+Constant.REGION_TYPE_03+" AND PARENT_ID IN \n");
		sql.append("(SELECT REGION_ID FROM TM_REGION WHERE REGION_CODE = "+provinceCode+") \n");
		return dao.pageQuery(sql.toString(), null, getFunName());
	}
	/**
	 * 获取城市
	 * @param map
	 * @param curPage
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	public PageResult<Map<String, Object>> getCitys(Map<String, Object> map, int curPage, int pageSize)throws Exception{
		String province = (String)map.get("PROVINCE");
		StringBuffer sbSql = new StringBuffer();
		sbSql.append("SELECT T1.REGION_CODE,T1.REGION_ID,\n");
		sbSql.append("       T1.REGION_NAME,\n");
		sbSql.append("       T2.REGION_NAME PROVICE_NAME\n");
		sbSql.append(" FROM TM_REGION T1,\n");
		sbSql.append("      TM_REGION T2\n");
		sbSql.append(" WHERE T1.PARENT_ID = T2.REGION_ID\n");
		sbSql.append(" AND   T2.REGION_CODE = ?\n");
		sbSql.append(" AND   T1.REGION_TYPE = ?\n"); 
		List<Object> param=new ArrayList<Object>();
		param.add(province);
		param.add(Constant.REGION_TYPE_03);
		PageResult<Map<String, Object>> ps= dao.pageQuery(sbSql.toString(), param, getFunName(),pageSize,curPage);
		return  ps;
	}
	/**
	 * 根据删除特殊里程
	 * @param param 
	 */
	public void delFare(TmSpecialCityFarePO tpo){
		dao.delete(tpo);
	}
	/**
	 * 根据删除特殊里程
	 * @param param 
	 */
	public void insertFare(List<Object> params){
		StringBuffer sbSql = new StringBuffer();
		sbSql.append("insert into TM_SPECIAL_CITY_FARE\n");
		sbSql.append("  (FARE_ID,\n");
		sbSql.append("   SERIES_ID,\n");
		sbSql.append("   YIELDLY,\n");
		sbSql.append("   PROVINCE_ID,\n");
		sbSql.append("   CITY_ID,\n");
		sbSql.append("   AMOUNT,\n");
		sbSql.append("   GREATE_BY,\n");
		sbSql.append("   CREATE_DATE,\n");
		sbSql.append("   CITY_IDD)\n");
		sbSql.append("  SELECT f_getid,\n");
		sbSql.append("         ?,\n");
		sbSql.append("         ?,\n");
		sbSql.append("         A.REGION_CODE,\n");
		sbSql.append("         C.REGION_CODE,\n");
		sbSql.append("         ?,\n");
		sbSql.append("         ?,\n");
		sbSql.append("         sysdate,\n");
		sbSql.append("         B.REGION_ID\n");
		sbSql.append("    FROM TM_REGION A, TM_REGION B, TM_REGION C\n");
		sbSql.append("   WHERE A.REGION_ID = B.PARENT_ID\n");
		sbSql.append("     AND B.REGION_ID = C.PARENT_ID\n");
		sbSql.append("     AND A.REGION_CODE = ?"); 
		dao.update(sbSql.toString(), params);
	}
}
