/**   
* @Title: PreclaimPreDao.java 
* @Package com.infodms.dms.dao.claim.preAuthorization 
* @Description: TODO(索赔预授权工单申请DAO) 
* @author wangjinbao   
* @date 2010-6-21 下午02:59:54 
* @version V1.0   
*/
package com.infodms.dms.dao.claim.preAuthorization;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.ConditionBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.po.FsFileuploadPO;
import com.infodms.dms.po.TmVehicleExtPO;
import com.infodms.dms.util.businessUtil.GetVinUtil;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

/** 
 * @ClassName: PreclaimPreDao 
 * @Description: TODO(索赔预授权工单申请DAO) 
 * @author wangjinbao 
 * @date 2010-6-21 下午02:59:54 
 *  
 */
public class PreclaimPreDao extends BaseDao {
	public static Logger logger = Logger.getLogger(PreclaimPreDao.class);
	private static final PreclaimPreDao dao = new PreclaimPreDao ();
	public static final PreclaimPreDao getInstance() {
		return dao;
	}
	
	@SuppressWarnings("unchecked")
	public   PageResult<Map<String, Object>> preclaimPreQuery(int pageSize, int curPage, String whereSql,List<Object> params) throws Exception {
		PageResult<Map<String, Object>> result = null;
		StringBuffer sb = new StringBuffer();
		sb.append(" select taww.id,taww.wrgroup_id,taww.labour_code,");
		sb.append(" taww.cn_des,trim(to_char(taww.labour_quotiety,'999999.00')) as labour_quotiety,");
		sb.append(" trim(to_char(taww.labour_hour,'999999.00')) as labour_hour ");
		sb.append(" from TT_AS_WR_WRLABINFO taww where taww.is_del = 0 ");
		sb.append(" and taww.tree_code = '3' ");
		if(whereSql != null && !"".equals(whereSql.trim())){
			sb.append(whereSql);
		}
		sb.append(" order by taww.id desc ");
		result = (PageResult<Map<String, Object>>) pageQuery(sb.toString(), params, getFunName(), pageSize, curPage);
		return result;
	}
	/**
	 * 
	* @Title: getWrgroupIdByModelId 
	* @Description: TODO(根据车型id取对应的车型组id) 
	* @param @param modelid
	* @param @return   
	* @return HashMap  
	* @throws
	 */
	@SuppressWarnings("unchecked")
	public HashMap getWrgroupIdByModelId(String modelid){
		HashMap map = new HashMap();
//		StringBuffer sql= new StringBuffer();
//		sql.append("select * from TT_AS_WR_MODEL_ITEM tawmi\n" );
//		sql.append("where tawmi.model_id ="+modelid+" ");
		
		StringBuffer sql= new StringBuffer();
		sql.append(" select tawmg.wrgroup_id,\n" );
		sql.append("      tawmg.wrgroup_type,\n" );
		sql.append("      tawmg.wrgroup_code,\n" );
		sql.append("      tawmi.model_id\n" );
		sql.append(" from TT_AS_WR_MODEL_GROUP tawmg,TT_AS_WR_MODEL_ITEM tawmi\n" );
		sql.append(" where tawmg.wrgroup_type = "+Constant.WR_MODEL_GROUP_TYPE_01+"\n" );
		sql.append(" and tawmg.wrgroup_id = tawmi.wrgroup_id");
		sql.append(" and tawmi.model_id ="+modelid+" ");
		
		List<Map<String, Object>> relist = pageQuery(sql.toString(), null,getFunName());
		if(relist != null && relist.size() > 0){
			map = (HashMap)relist.get(0);
		}
		return map;
	}
	/**
	 * 
	* @Title: getItems 
	* @Description: TODO(获取项目列表) 
	* @param @param bean
	* @param @param wrgroupid
	* @param @return   
	* @return List<Map<String,Object>>  
	* @throws
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getItemsOld(ConditionBean bean,String wrgroupid){
		StringBuffer sql= new StringBuffer();
		//预授权监控工时:TT_AS_WR_FOREAPPROVALLAB
		StringBuffer sqlf= new StringBuffer();
		sqlf.append(" select tf.id,tc.code_id,tc.code_desc,\n" );
		sqlf.append(" tf.operation_code as code,tf.operation_desc as name,tf.oem_company_id\n" );
		sqlf.append(" from TT_AS_WR_FOREAPPROVALLAB tf,tc_code tc\n" );
		sqlf.append(" where tf.is_send ="+Constant.DOWNLOAD_CODE_STATUS_03+" \n" );
		if(Utility.testString(wrgroupid)){
			sqlf.append(" and tf.wrgroup_id ="+wrgroupid+" \n" );
		}else{
			sqlf.append(" and tf.wrgroup_id is null  \n" );
		}
		sqlf.append(" and tc.code_id = "+Constant.PRE_AUTH_ITEM_01+"\n" );
		//预授权监控配件:TT_AS_WR_FOREAPPROVALPT
		StringBuffer sqlp= new StringBuffer();
		sqlp.append(" select tfp.id,tc.code_id,tc.code_desc,\n" );
		sqlp.append(" tfp.part_code as code,tfp.part_name as name\n" );
		sqlp.append(" from TT_AS_WR_FOREAPPROVALPT tfp,tc_code tc\n" );
		sqlp.append(" where tfp.is_send = "+Constant.DOWNLOAD_CODE_STATUS_03+"\n" );
		sqlp.append(" and tc.code_id = "+Constant.PRE_AUTH_ITEM_02+"\n" );
		//预授权监控其他项目:TT_AS_WR_FOREAPPROVALOTHERITEM
		StringBuffer sqlo= new StringBuffer();
		sqlo.append(" select tfo.id,tc.code_id,tc.code_desc,\n" );
		sqlo.append(" tfo.item_code as code,tfo.item_desc as name\n" );
		sqlo.append(" from TT_AS_WR_FOREAPPROVALOTHERITEM tfo,tc_code tc\n" );
		sqlo.append(" where tc.code_id = "+Constant.PRE_AUTH_ITEM_03+"\n" );
		//开始拼sql：
		sql.append(" select * from (\n");
		if((Constant.PRE_AUTH_ITEM_01.toString()).equals(bean.getConOne())){
			sql.append(sqlf.toString());			
		}else if((Constant.PRE_AUTH_ITEM_02.toString()).equals(bean.getConOne())){
			sql.append(sqlp.toString());
		}else if((Constant.PRE_AUTH_ITEM_03.toString()).equals(bean.getConOne())){
			sql.append(sqlo.toString());
		}else{
			sql.append(sqlf.toString());
			sql.append(" union all \n");
			sql.append(sqlp.toString());
			sql.append(" union all \n");
			sql.append(sqlo.toString());
		}
		sql.append(" ) c\n");
		sql.append(" where (1=1) \n");
		if(Utility.testString(bean.getConTwo())){//code不为空
			sql.append(" and c.code like '%"+bean.getConTwo()+"%'\n");
		}
		if(Utility.testString(bean.getConThree())){//name不为空
			sql.append(" and c.name like '%"+bean.getConThree()+"%'\n");
		}
		if(Utility.testString(bean.getConFour())){//要删选的id不为空
			sql.append(" and c.id not in  ("+bean.getConFour()+")\n");
		}		
		sql.append(" order by c.id desc ");

		List<Map<String, Object>> relist = pageQuery(sql.toString(), null,getFunName());
		return relist;
	}
	/**
	 * 
	* @Title: getItems 
	* @Description: TODO(获取项目列表) :旧业务选择项目方法
	* @param @param bean
	* @param @param wrgroupid
	* @param @param modelid
	* @param @return   
	* @return List<Map<String,Object>>  
	* @throws
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getItems(Long companyId,ConditionBean bean,String wrgroupid,String modelid){
		StringBuffer sql= new StringBuffer();
		//维修项目（索赔工时）:TT_AS_WR_WRLABINFO
		StringBuffer sqlf= new StringBuffer();
		sqlf.append(" select taww.id,\n" );
//		sqlf.append("       taww.wrgroup_id,\n" );
		sqlf.append("       tc.code_id,\n" );
		sqlf.append("       tc.code_desc,\n" );
		sqlf.append("       taww.labour_code as code,\n" );
		sqlf.append("       taww.cn_des as name\n" );
		sqlf.append("  from TT_AS_WR_WRLABINFO taww,tc_code tc\n" );
		sqlf.append(" where taww.is_del = 0\n" );
		sqlf.append("   and taww.tree_code = '3'\n" );
		//modify at 2010-07-19 start 
		//modify end		
		if(Utility.testString(wrgroupid)){
			sqlf.append(" and taww.wrgroup_id ="+wrgroupid+" \n" );
		}else{
			sqlf.append(" and taww.wrgroup_id is null  \n" );
		}
		sqlf.append(" and tc.code_id = "+Constant.PRE_AUTH_ITEM_01+"\n" );
		//维修材料（配件基础信息）:TM_PT_PART_BASE
		StringBuffer sqlp= new StringBuffer();
		sqlp.append("select tppb.part_id as id,\n" );
		sqlp.append("       tc.code_id,\n" );
		sqlp.append("       tc.code_desc,\n" );
		sqlp.append("       tppb.part_code as code,\n" );
		sqlp.append("       tppb.part_name as name\n" );
//		sqlp.append("       tppb.group_id\n" );
		sqlp.append("  from TM_PT_PART_BASE tppb, tc_code tc\n" );
		sqlp.append(" where tppb.is_del = 0\n" );
		sqlp.append(" 	and tc.code_id = "+Constant.PRE_AUTH_ITEM_02+"\n" );
//		sqlp.append("   and tppb.group_id ="+modelid+"\n"); //长安项目配件和车型不关联

		
		//其它费用:TT_AS_WR_OTHERFEE
		StringBuffer sqlo= new StringBuffer();
		sqlo.append("select tawo.fee_id as id,\n" );
		sqlo.append("       tc.code_id,\n" );
		sqlo.append("       tc.code_desc,\n" );
		sqlo.append("       tawo.fee_code as code,\n" );
		sqlo.append("       tawo.fee_name as name\n" );
		sqlo.append("  from TT_AS_WR_OTHERFEE tawo, tc_code tc\n" );
		sqlo.append(" where tawo.is_del = 0\n" );
		sqlo.append(" and tc.code_id = "+Constant.PRE_AUTH_ITEM_03+"\n" );
		//modify at 2010-07-19 start 
		//modify end		
		//开始拼sql：
		sql.append(" select * from (\n");
		if((Constant.PRE_AUTH_ITEM_01.toString()).equals(bean.getConOne())){
			sql.append(sqlf.toString());			
		}else if((Constant.PRE_AUTH_ITEM_02.toString()).equals(bean.getConOne())){
			sql.append(sqlp.toString());
		}else if((Constant.PRE_AUTH_ITEM_03.toString()).equals(bean.getConOne())){
			sql.append(sqlo.toString());
		}else{
			sql.append(sqlf.toString());
			sql.append(" union all \n");
			sql.append(sqlp.toString());
			sql.append(" union all \n");
			sql.append(sqlo.toString());
		}
		sql.append(" ) c\n");
		sql.append(" where (1=1) \n");
		if(Utility.testString(bean.getConTwo())){//code不为空
			sql.append(" and upper(c.code) like '%"+bean.getConTwo().toUpperCase()+"%'\n");
		}
		if(Utility.testString(bean.getConThree())){//name不为空
			sql.append(" and c.name like '%"+bean.getConThree()+"%'\n");
		}
		if(Utility.testString(bean.getConFour())){//要删选的id不为空
			sql.append(" and c.id not in  ("+bean.getConFour()+")\n");
		}		
		sql.append(" order by c.code_id ,c.id desc ");

		List<Map<String, Object>> relist = pageQuery(sql.toString(), null,getFunName());
		return relist;
	}
	/**
	 * modify at 2010-07-19
	* @Title: getItems   
	* @Description: TODO(获取预授权要选择的项目列表) 
	* @param @param companyId
	* @param @param wrgroupid
	* @param @param pageSize
	* @param @param curPage
	* @param @param bean
	* @param @return
	* @param @throws Exception   
	* @return PageResult<Map<String,Object>>  
	* @throws
	 */
	@SuppressWarnings("unchecked")
	public   PageResult<Map<String, Object>> getItems(Long companyId,String wrgroupid,int pageSize, int curPage, ConditionBean bean) throws Exception {
		PageResult<Map<String, Object>> result = null;
		StringBuffer sql= new StringBuffer();
		//维修项目（索赔工时）:TT_AS_WR_WRLABINFO
		StringBuffer sqlf= new StringBuffer();
		sqlf.append(" select taww.id,\n" );
//		sqlf.append("       taww.wrgroup_id,\n" );
		sqlf.append("       tc.code_id,\n" );
		sqlf.append("       tc.code_desc,\n" );
		sqlf.append("       taww.labour_code as code,\n" );
		sqlf.append("       taww.cn_des as name\n" );
		sqlf.append("  from TT_AS_WR_WRLABINFO taww,tc_code tc\n" );
		sqlf.append(" where taww.is_del = 0\n" );
		sqlf.append("   and taww.tree_code = '3'\n" );
		//modify at 2010-07-19 start 
		//modify end		
		if(Utility.testString(wrgroupid)){
			sqlf.append(" and taww.wrgroup_id ="+wrgroupid+" \n" );
		}else{
			sqlf.append(" and taww.wrgroup_id is null  \n" );
		}
		sqlf.append(" and tc.code_id = "+Constant.PRE_AUTH_ITEM_01+"\n" );
		//维修材料（配件基础信息）:TM_PT_PART_BASE
		StringBuffer sqlp= new StringBuffer();
		sqlp.append("select tppb.part_id as id,\n" );
		sqlp.append("       tc.code_id,\n" );
		sqlp.append("       tc.code_desc,\n" );
		sqlp.append("       tppb.part_code as code,\n" );
		sqlp.append("       tppb.part_name as name\n" );
//		sqlp.append("       tppb.group_id\n" );
		sqlp.append("  from TM_PT_PART_BASE tppb, tc_code tc\n" );
		sqlp.append(" where tppb.is_del = 0\n" );
		sqlp.append(" 	and tc.code_id = "+Constant.PRE_AUTH_ITEM_02+"\n" );
//		sqlp.append("   and tppb.group_id ="+modelid+"\n");

		
		//其它费用:TT_AS_WR_OTHERFEE
		StringBuffer sqlo= new StringBuffer();
		sqlo.append("select tawo.fee_id as id,\n" );
		sqlo.append("       tc.code_id,\n" );
		sqlo.append("       tc.code_desc,\n" );
		sqlo.append("       tawo.fee_code as code,\n" );
		sqlo.append("       tawo.fee_name as name\n" );
		sqlo.append("  from TT_AS_WR_OTHERFEE tawo, tc_code tc\n" );
		sqlo.append(" where tawo.is_del = 0\n" );
		sqlo.append(" and tc.code_id = "+Constant.PRE_AUTH_ITEM_03+"\n" );
		//modify at 2010-07-19 start 
		//modify end		
		//开始拼sql：
		sql.append(" select * from (\n");
		if((Constant.PRE_AUTH_ITEM_01.toString()).equals(bean.getConOne())){
			sql.append(sqlf.toString());			
		}else if((Constant.PRE_AUTH_ITEM_02.toString()).equals(bean.getConOne())){
			sql.append(sqlp.toString());
		}else if((Constant.PRE_AUTH_ITEM_03.toString()).equals(bean.getConOne())){
			sql.append(sqlo.toString());
		}else{
			sql.append(sqlf.toString());
			sql.append(" union all \n");
			sql.append(sqlp.toString());
			sql.append(" union all \n");
			sql.append(sqlo.toString());
		}
		sql.append(" ) c\n");
		sql.append(" where (1=1) \n");
		if(Utility.testString(bean.getConTwo())){//code不为空
			sql.append(" and upper(c.code) like '%"+bean.getConTwo().toUpperCase()+"%'\n");
		}
		if(Utility.testString(bean.getConThree())){//name不为空
			sql.append(" and c.name like '%"+bean.getConThree()+"%'\n");
		}
		if(Utility.testString(bean.getConFour())){//要删选的id不为空
			sql.append(" and c.id not in  ("+bean.getConFour()+")\n");
		}		
		sql.append(" order by c.code_id ,c.id desc ");
		result = (PageResult<Map<String, Object>>) pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
		return result;
	}
	
	@SuppressWarnings("unchecked")
	public   PageResult<Map<String, Object>> preclaimQuery(Long companyId,String dealerId,int pageSize, int curPage, ConditionBean bean) throws Exception {
		PageResult<Map<String, Object>> result = null;
		StringBuffer sql= new StringBuffer();
		sql.append(" select tawf.id,tawf.report_status,tawf.vin,tawf.fo_no,tawf.ro_no,\n" );
		sql.append(" tawf.approval_date,tawf.deliverer,tawf.approval_person,tawf.approval_phone,ti.cnum\n" );
		sql.append(" from TT_AS_WR_FOREAPPROVAL tawf,\n" );
		sql.append(" (select titem.fid,count(titem.id) as cnum\n" );
		sql.append(" from TT_AS_WR_FOREAPPROVALITEM titem\n" );
		sql.append(" group by titem.fid) ti\n" );
		sql.append(" where tawf.id = ti.fid\n" );
		//modify at 2010-07-19 start 
		sql.append(" and tawf.dealer_id = "+dealerId+"\n" );
		//modify end		
		sql.append(" and tawf.report_status = "+Constant.PRE_AUTH_STATUS_01+"\n");
		if(Utility.testString(bean.getConOne())){//开始时间不为空
			sql.append(" and tawf.approval_date >= to_date('"+bean.getConOne()+"', 'yyyy-MM-dd')\n");
		}		
		if(Utility.testString(bean.getConTwo())){//结束时间不为空
			sql.append(" and tawf.approval_date <= to_date('"+bean.getConTwo()+"', 'yyyy-MM-dd')\n");
		}
		if(Utility.testString(bean.getConThree())){//预授权单号不为空
			sql.append(" and tawf.fo_no like '%"+bean.getConThree()+"%'\n");
		}
		if(Utility.testString(bean.getConSix())){//工单号不为空
			sql.append(" and tawf.ro_no like '%"+bean.getConSix()+"%'\n");
		}		
		if(Utility.testString(bean.getConFour())){//VIN不为空
//			sql.append(" and tawf.vin like '%"+bean.getConFour()+"%'\n");
			sql.append(GetVinUtil.getVins(bean.getConFour(),"tawf"));
		}
		if(Utility.testString(bean.getConFive())){//提报状态不为空
			sql.append(" and tawf.report_status = '"+bean.getConFive()+"'\n");
		}		
		sql.append(" order by tawf.id desc ");
		result = (PageResult<Map<String, Object>>) pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
		return result;
	}
	
	@SuppressWarnings("unchecked")
	public HashMap getPreclaimById(Long companyId,String id){
		HashMap map = new HashMap();
//		StringBuffer sql = new StringBuffer("");
//		sql.append(" SELECT t.*,V.*,G1.GROUP_NAME AS series_name,G2.GROUP_NAME AS model_name FROM TT_AS_WR_FOREAPPROVAL t \n");
//		sql.append(" ,TM_VEHICLE V \n");
//		sql.append(" LEFT OUTER JOIN TM_VHCL_MATERIAL_GROUP G1 on G1.GROUP_ID=V.SERIES_ID \n");
//		sql.append(" LEFT OUTER JOIN TM_VHCL_MATERIAL_GROUP G2 on G2.GROUP_ID=V.MODEL_ID \n");
//		sql.append(" LEFT OUTER JOIN TT_ACTUAL_SALES A on A.VEHILCE_ID=V.VEHICLE_ID \n");
//		sql.append(" where t.vin = v.vin \n");
//		sql.append(" and t.id = "+id+" \n");
		
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT t.*,V.*,t.license_no as license,t.yieldly as t_yieldly,\n" );
		sql.append("       a.CUSTOMER_NAME AS customer_name,\n" );
		sql.append("       a.CERT_NO AS cert_no,\n" );
		sql.append("       a.MOBILE AS mobile,\n" );
		sql.append("       a.ADDRESS_DESC AS address_desc,\n" );
		sql.append("       vm.BRAND_NAME as brand_name,\n" );
		sql.append("       vm.BRAND_CODE as brand_code,\n" );
		sql.append("       vm.SERIES_NAME AS series_name,\n" );
		sql.append("       vm.SERIES_CODE as series_code,\n" );
		sql.append("       vm.MODEL_NAME AS model_name,\n" );
		sql.append("       vm.MODEL_CODE as model_code\n" );
		sql.append("  FROM TT_AS_WR_FOREAPPROVAL t,VW_MATERIAL_GROUP vm, TM_VEHICLE V\n" );
		sql.append("  LEFT OUTER JOIN TT_ACTUAL_SALES A on A.VEHILCE_ID = V.VEHICLE_ID\n" );
		sql.append(" where v.series_id = vm.SERIES_ID\n" );
		sql.append("   and v.model_id = vm.MODEL_ID\n" );
		sql.append("   and v.package_id = vm.PACKAGE_ID\n" );
		sql.append("   and t.vin = v.vin\n" );
		//modify at 2010-07-19 start 
//		sql.append(" 	and tawf.dealer_id = "+dealerId+"\n" );
		//modify end		
		sql.append("   and t.id = "+id+" \n");

		
//		StringBuffer sql= new StringBuffer();
//		sql.append("SELECT c.WRGROUP_ID,\n" );
//		sql.append("       t.*,\n" );
//		sql.append("       V.*,\n" );
//		sql.append("       G1.GROUP_NAME AS series_name,\n" );
//		sql.append("       G2.GROUP_NAME AS model_name\n" );
//		sql.append("  FROM TT_AS_WR_FOREAPPROVAL t, TM_VEHICLE V\n" );
//		sql.append("  LEFT OUTER JOIN TM_VHCL_MATERIAL_GROUP G1 on G1.GROUP_ID = V.SERIES_ID\n" );
//		sql.append("  LEFT OUTER JOIN TM_VHCL_MATERIAL_GROUP G2 on G2.GROUP_ID = V.MODEL_ID\n" );
//		sql.append("  LEFT OUTER JOIN TT_ACTUAL_SALES A on A.VEHILCE_ID = V.VEHICLE_ID\n" );
//		sql.append("  LEFT OUTER JOIN (select tawmg.wrgroup_id,\n" );
//		sql.append("                          tawmg.wrgroup_type,\n" );
//		sql.append("                          tawmg.wrgroup_code,\n" );
//		sql.append("                          tawmi.model_id\n" );
//		sql.append("                     from TT_AS_WR_MODEL_GROUP tawmg\n" );
//		sql.append("                     left outer join TT_AS_WR_MODEL_ITEM tawmi on tawmg.wrgroup_id =\n" );
//		sql.append("                                                                  tawmi.wrgroup_id\n" );
//		sql.append("                    where tawmg.wrgroup_type = "+Constant.WR_MODEL_GROUP_TYPE_01+") c on c.model_id =\n" );
//		sql.append("                                                              v.model_id\n" );
//		sql.append(" where t.vin = v.vin\n" );
//		sql.append(" and t.id = "+id+" \n");

		
		List<Map<String, Object>> relist = pageQuery(sql.toString(), null,getFunName());
		if(relist != null && relist.size() > 0){
			map = (HashMap)relist.get(0);
		}
		return map;
	}
	
	@SuppressWarnings("unchecked")
	public List getPreclaimItemById(String id ){
		StringBuffer sql= new StringBuffer();
		sql.append(" select * from TT_AS_WR_FOREAPPROVALITEM titem\n" );
		sql.append(" where titem.fid = "+id+"");
		List<Map<String, Object>> list = pageQuery(sql.toString(), null,getFunName());
		return list;
	}
	
	
	/**
	 * 
	* @Title: getVin 
	* @Description: TODO(取得VIN码) 
	* @param     设定文件 
	* @return void    返回类型 
	* @throws
	 */
	@SuppressWarnings("unchecked")
	public PageResult<TmVehicleExtPO> getVin(Long companyId,String dealerId, Map<String,String> map,int pageSize,int curPage) {
		
//		StringBuffer sql = new StringBuffer("");
//		sql.append(" SELECT V.*,(select brand_name from VW_MATERIAL_GROUP vw where vw.package_id=v.package_id) as brand_name,a.CUSTOMER_NAME AS customer_name,a.CERT_NO AS cert_no,a.MOBILE AS mobile,a.ADDRESS_DESC AS address_desc,G1.GROUP_NAME AS series_name,G2.GROUP_NAME AS model_name FROM TM_VEHICLE V \n");
//		//sql.append(" LEFT OUTER JOIN TM_VHCL_MATERIAL_GROUP G0 on V.");
//		sql.append(" LEFT OUTER JOIN TM_VHCL_MATERIAL_GROUP G1 on G1.GROUP_ID=V.SERIES_ID \n");
//		sql.append(" LEFT OUTER JOIN TM_VHCL_MATERIAL_GROUP G2 on G2.GROUP_ID=V.MODEL_ID \n");
//		sql.append(" LEFT OUTER JOIN TT_ACTUAL_SALES A on A.VEHILCE_ID=V.VEHICLE_ID \n");
//		sql.append(" WHERE 1=1 ");
		StringBuffer sql= new StringBuffer();
		sql.append(" SELECT V.*,\n" );
		sql.append("       a.CUSTOMER_NAME AS customer_name,\n" );
		sql.append("       a.CERT_NO AS cert_no,\n" );
		sql.append("       a.MOBILE AS mobile,\n" );
		sql.append("       a.ADDRESS_DESC AS address_desc,\n" );
		sql.append("       vm.BRAND_NAME as brand_name,\n" );
		sql.append("       vm.BRAND_CODE as brand_code,\n" );
		sql.append("       vm.SERIES_NAME AS series_name,\n" );
		sql.append("       vm.SERIES_CODE as series_code,\n" );
		sql.append("       vm.MODEL_NAME AS model_name,\n" );
		sql.append("       vm.MODEL_CODE as model_code\n" );
		sql.append("  FROM VW_MATERIAL_GROUP vm, TM_VEHICLE V\n" );
		sql.append("  LEFT OUTER JOIN TT_ACTUAL_SALES A on A.VEHILCE_ID = V.VEHICLE_ID\n" );
		sql.append(" where v.series_id = vm.SERIES_ID\n" );
		sql.append("   and v.model_id = vm.MODEL_ID\n" );
		sql.append("   and v.package_id = vm.PACKAGE_ID\n" );
		//modify at 2010-07-19 start 
//		sql.append(" 	and v.dealer_id = "+dealerId+"\n" );
		//modify end		
		if (Utility.testString(map.get("vinParent"))) {
			sql.append(" and upper(v.VIN) = '" + map.get("vinParent") + "' ");
		}
		if (Utility.testString(map.get("vin"))) {
			sql.append(" and upper(v.VIN) LIKE '%" + map.get("vin").toUpperCase() + "%' ");
		}
		if (Utility.testString(map.get("customer"))) {
			sql.append(" and a.CUSTOMER_NAME LIKE '%" + map.get("customer") + "%' ");
		}
		sql.append(" order by v.vehicle_id ");
		PageResult<TmVehicleExtPO> ps = pageQuery(TmVehicleExtPO.class,sql.toString(),null,pageSize,curPage);
		return ps;
		
	}
	
	/**
	 *                                     
	* @Title: queryAttById 
	* @Description: TODO(通过ID查询附件) 
	* @param @param id
	* @param @return    设定文件 
	* @return List<FsFileuploadPO>    返回类型 
	* @throws
	 */
	@SuppressWarnings("unchecked")
	public List<FsFileuploadPO> queryAttById(String id) {
		StringBuffer sql = new StringBuffer();
		List<FsFileuploadPO> ls = new ArrayList<FsFileuploadPO>();
		sql.append(" SELECT A.* FROM FS_FILEUPLOAD A ");
		//sql.append(" LEFT OUTER JOIN FS_FILEUPLOAD B ON B.YWZJ = A.ID ");
		sql.append(" WHERE 1=1 " );
		sql.append(" AND A.YWZJ='"+id+"'");
		ls = select (FsFileuploadPO.class,sql.toString(),null);
		return ls;
	}
	
	/**
	 * 
	* @Title: getPreItems 
	* @Description: TODO(查询预授权所需的项目信息：接口用) 
	* @param @param code
	* @param @param type
	* @param @return   
	* @return Map<String,Object>  
	* @throws
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> getPreItems(String code ,String type){
		StringBuffer sql= new StringBuffer();
		sql.append("select * from (\n");
		if(type.equals(Constant.PRE_AUTH_ITEM_01.toString())){//维修项目
			//维修项目（索赔工时）:TT_AS_WR_WRLABINFO
			sql.append(" select taww.id as id,\n" );
			sql.append("       tc.code_id,\n" );
			sql.append("       tc.code_desc,\n" );
			sql.append("       taww.labour_code as code,\n" );
			sql.append("       taww.cn_des as name\n" );
			sql.append("  from TT_AS_WR_WRLABINFO taww,tc_code tc\n" );
			sql.append(" where taww.is_del = 0\n" );
			sql.append("   and taww.tree_code = '3'\n" );
			//modify at 2010-07-19 start 
			//modify end		
			sql.append(" and tc.code_id = "+Constant.PRE_AUTH_ITEM_01+"\n" );			
		}else if(type.equals(Constant.PRE_AUTH_ITEM_02.toString())){//维修材料
			//维修材料（配件基础信息）:TM_PT_PART_BASE
			sql.append("select tppb.part_id as id,\n" );
			sql.append("       tc.code_id,\n" );
			sql.append("       tc.code_desc,\n" );
			sql.append("       tppb.part_code as code,\n" );
			sql.append("       tppb.part_name as name\n" );
			sql.append("  from TM_PT_PART_BASE tppb, tc_code tc\n" );
			sql.append(" where tppb.is_del = 0\n" );
			sql.append(" 	and tc.code_id = "+Constant.PRE_AUTH_ITEM_02+"\n" );			
		}else
		{
			//其它费用:TT_AS_WR_OTHERFEE
			sql.append("select tawo.fee_id as id,\n" );
			sql.append("       tc.code_id,\n" );
			sql.append("       tc.code_desc,\n" );
			sql.append("       tawo.fee_code as code,\n" );
			sql.append("       tawo.fee_name as name\n" );
			sql.append("  from TT_AS_WR_OTHERFEE tawo, tc_code tc\n" );
			sql.append(" where tawo.is_del = 0\n" );
			sql.append(" and tc.code_id = "+Constant.PRE_AUTH_ITEM_03+"\n" );			
		}
		sql.append(" )\n");
		sql.append(" where code = '"+code+"' \n");
		return pageQueryMap(sql.toString(), null, getFunName());	
	}
	
	

	/* (非 Javadoc) 
	 * <p>Title: wrapperPO</p> 
	 * <p>Description: </p> 
	 * @param rs
	 * @param idx
	 * @return 
	 * @see com.infodms.dms.dao.common.BaseDao#wrapperPO(java.sql.ResultSet, int) 
	 */
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO 自动生成方法存根
		return null;
	}

}
