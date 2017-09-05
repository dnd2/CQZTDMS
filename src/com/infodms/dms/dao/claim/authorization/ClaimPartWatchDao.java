/**   
* @Title: ClaimPartWatchDao.java 
* @Package com.infodms.dms.dao.claim.authorization 
* @Description: TODO(监控配件维护DAO) 
* @author Administrator   
* @date 2010-6-10 上午11:15:44 
* @version V1.0   
*/
package com.infodms.dms.dao.claim.authorization;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.common.Constant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.po.TtAsWrAuthmonitorpartPO;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

/** 
 * @ClassName: ClaimPartWatchDao 
 * @Description: TODO(监控配件维护DAO) 
 * @author Administrator 
 * @date 2010-6-10 上午11:15:44 
 *  
 */
public class ClaimPartWatchDao extends BaseDao {

	public static Logger logger = Logger.getLogger(ClaimPartWatchDao.class);
	private static final ClaimPartWatchDao dao = new ClaimPartWatchDao ();
	public static final ClaimPartWatchDao getInstance() {
		return dao;
	}
	
	/**
	 * 
	* @Title: getLevelList 
	* @Description: TODO(获取授权级别列表) 
	* @param oemCompanyId
	* @param type 授权规则类型(0:技术授权 1:结算授权)
	* @return List  
	* @throws
	 */
	@SuppressWarnings("unchecked")
	public List getLevelList(Long oemCompanyId,String type){
		StringBuffer sb = new StringBuffer();
		sb.append(" select tawa.approval_level_code,tawa.approval_level_name,tawa.approval_level_tier from tt_as_wr_authinfo tawa  ");
		sb.append(" where tawa.approval_level_code<>100 ");
		sb.append(" and tawa.type = ").append(type).append("\n");
		sb.append(" order by tawa.Approval_Level_Tier");
		List<Map<String, Object>> list = pageQuery(sb.toString(), null,getFunName());
		return list;
	}
	/**
	 * 
	* @Title: claimPartWatchQuery 
	* @Description: TODO(监控配件查询) 
	* @param @param pageSize          ：每页显示的记录数
	* @param @param curPage           ：当前页
	* @param @param whereSql          ：查询条件
	* @param @param params            ：参数集合
	* @param @return
	* @param @throws Exception   
	* @return PageResult<Map<String,Object>>  
	* @throws
	 */
	@SuppressWarnings("unchecked")
	public   PageResult<Map<String, Object>> claimPartWatchQuery(int pageSize, int curPage, String whereSql,List<Object> params,Long oemCompanyId) throws Exception {
		PageResult<Map<String, Object>> result = null;
		List list = dao.getLevelList(oemCompanyId,Constant.AUDIT_TYPE_01.toString());
		StringBuffer sb = new StringBuffer();
		sb.append(" select tawap.id,tawmg.wrgroup_code,tawap.part_code, ");
		sb.append("  tawap.part_name,  tawap.approval_level ");
		for(int i = 0 ; i < list.size(); i++){
			HashMap tcpo = (HashMap)list.get(i);
			sb.append(" ,CASE WHEN instr(tawap.approval_level,'"+tcpo.get("APPROVAL_LEVEL_CODE")+"')>0 then '"+tcpo.get("APPROVAL_LEVEL_CODE")+"' else '' end \""+tcpo.get("APPROVAL_LEVEL_CODE")+"\"");
		}		
		sb.append(" from tt_as_wr_authmonitorpart tawap, tt_as_wr_model_group tawmg ");
		sb.append(" where tawap.model_group= tawmg.wrgroup_id(+) ");
		sb.append(" and tawap.is_del = 0 ");
		if(whereSql != null && !"".equals(whereSql.trim())){
			sb.append(whereSql);
		}
		sb.append(" order by tawap.create_date desc ");
		result = (PageResult<Map<String, Object>>) pageQuery(sb.toString(), params, getFunName(), pageSize, curPage);
		return result;
	}
	
	/**Iverson add with 2010-11-09
	 * @Title: claimPartWatchBigQuery 
	 * @Description: TODO(监控配件大类查询) 
	 * @param @param pageSize          ：每页显示的记录数
	 * @param @param curPage           ：当前页
	 * @param @param whereSql          ：查询条件
	 * @param @param params            ：参数集合
	 * @param @return
	 * @param @throws Exception   
	 * @return PageResult<Map<String,Object>>  
	 * @throws
	 */
	@SuppressWarnings("unchecked")
	public   PageResult<Map<String, Object>> claimPartWatchBigQuery(int pageSize, int curPage, String whereSql,List<Object> params,Long oemCompanyId) throws Exception {
		PageResult<Map<String, Object>> result = null;
		List list = dao.getLevelList(oemCompanyId,Constant.AUDIT_TYPE_01.toString());
		StringBuffer sb = new StringBuffer();
		sb.append(" select tawat.ID,tawat.PART_BIGTYPE_CODE, ");
		sb.append("  tawat.PART_BIGTYPE_NAME,tawat.approval_level ");
		for(int i = 0 ; i < list.size(); i++){
			HashMap tcpo = (HashMap)list.get(i);
			sb.append(" ,CASE WHEN instr(tawat.approval_level,'"+tcpo.get("APPROVAL_LEVEL_CODE")+"')>0 then '"+tcpo.get("APPROVAL_LEVEL_CODE")+"' else '' end \""+tcpo.get("APPROVAL_LEVEL_CODE")+"\"");
		}
		sb.append(" from TT_AS_WR_AUTHMONITORTYPE tawat ");
		sb.append(" where tawat.is_del=0 ");
		if(whereSql != null && !"".equals(whereSql.trim())){
			sb.append(whereSql);
		}
		sb.append(" order by tawat.create_date desc ");
		System.out.println("sql=="+sb);
		result = (PageResult<Map<String, Object>>) pageQuery(sb.toString(), params, getFunName(), pageSize, curPage);
		return result;
	}
	/**
	* modify : 2010-07-23 XZM 删除对应 oemCompanyId 关联条件
	* @Title: getPartById 
	* @Description: TODO(查询车型组关联车型所对应的配件列表) 
	* @param @param whereSql
	* @param @param params
	* @param @return   
	* @return List  
	* @throws
	* 
	 */
	@SuppressWarnings("unchecked")
	public List getPartByIdOLD(Long oemCompanyId ,String whereSql,List<Object> params){
		StringBuffer sql= new StringBuffer();
		sql.append("select tppb.part_code,tppb.part_name,tppb.stock_price from TT_AS_WR_MODEL_ITEM tawmi\n" );
		sql.append("join TM_PT_PART_BASE tppb\n" );
		sql.append("on tawmi.model_id = tppb.group_id\n" );
		sql.append("where 1=1 \n");
		if(Utility.testString(whereSql)){
			sql.append(whereSql);
		}
		List<Map<String, Object>> list = pageQuery(sql.toString(), params,getFunName());
		return list;
	}
	/**
	* modify at 2010-07-23   Iverson modify 2010-11-09
	* @Title: getPartById 
	* @Description: TODO(查询车型组关联车型所对应的配件列表) 
	* @param @param whereSql
	* @param @param params
	* @param @return   
	* @return List  
	* @throws
	 */
	@SuppressWarnings("unchecked")
	public PageResult<Map<String, Object>> getPartById1(int pageSize, int curPage,Long oemCompanyId ,String whereSql){
		PageResult<Map<String, Object>> result = null;
		StringBuffer sql= new StringBuffer();
		sql.append("select tppb.part_code,tppb.part_name, tppb.part_id, tppb.stock_price\n" );
		sql.append("  from TM_PT_PART_BASE tppb\n" );
		sql.append(" where tppb.is_del = 0\n");
		sql.append("and  tppb.part_code not in (select part_code from tt_as_wr_authmonitorpart)\n"); 
		if(Utility.testString(whereSql)){
			sql.append(whereSql);
		}
		System.out.println(sql.toString());
		result=(PageResult<Map<String, Object>>) pageQuery(sql.toString(), null,getFunName(), pageSize, curPage);
		return result;
	}	
	
	/**
	* modify at 2010-07-23
	* @Title: getPartById 
	* @Description: TODO(查询车型组关联车型所对应的配件列表) ：长安项目监控配件和车型没有关联
	* @param @param whereSql
	* @param @param params
	* @param @return   
	* @return List  
	* @throws
	 */
	@SuppressWarnings("unchecked")
	public List getPartById(Long oemCompanyId ,String whereSql,List<Object> params){
		StringBuffer sql= new StringBuffer();
		sql.append("select tppb.part_code,tppb.part_name, tppb.part_id, tppb.stock_price\n" );
		sql.append("  from TM_PT_PART_BASE tppb\n" );
		sql.append(" where tppb.is_del = 0\n");
		if(Utility.testString(whereSql)){
			sql.append(whereSql);
		}
		List<Map<String, Object>> list = pageQuery(sql.toString(), params,getFunName());
		return list;
	}
	
	/**
	* Iverson add with 2010-11-09
	* @Title: getPartBigById 
	* @Description: TODO(查询车型组关联车型所对应的配件大类列表) ：长安项目监控配件和车型没有关联
	* @param @param whereSql
	* @param @param params
	* @param @return   
	* @return List  
	* @throws
	 */
	@SuppressWarnings("unchecked")
	public PageResult<Map<String, Object>> getPartBigById(int pageSize, int curPage,Long oemCompanyId ,String whereSql,List<Object> params){
		PageResult<Map<String, Object>> result = null;
		StringBuffer sql= new StringBuffer();
		sql.append("select tppt.PARTTYPE_CODE,tppt.PARTTYPE_NAME,tppt.ID\n" );
		sql.append(" from Tm_Pt_Part_Type tppt\n" );
		System.out.println("sql==="+sql);
		if(Utility.testString(whereSql)){
			sql.append(whereSql);
		}
		result = (PageResult<Map<String, Object>>) pageQuery(sql.toString(), params,getFunName(), pageSize, curPage);
		return result;
	}	
	/**
	 * 
	* @Title: getRelatingForPart 
	* @Description: TODO(通过车型组id和配件代码关联查询) 
	* @param @param id             ：车型组id
	* @param @param code           ：配件代码
	* @param @return   
	* @return List  
	* @throws
	* 长安DMS不需要此方法 : modify at 2010-07-23
	 */
	@SuppressWarnings("unchecked")
	public List getRelatingForPart(String id,String code){
		StringBuffer sql= new StringBuffer();
		sql.append("select tppb.part_code,tppb.part_name,tppb.stock_price from TT_AS_WR_MODEL_ITEM tawmi\n" );
		sql.append("join TM_PT_PART_BASE tppb\n" );
		sql.append("on tawmi.model_id = tppb.group_id\n" );
		sql.append("where tawmi.wrgroup_id = "+id+"\n" );
		sql.append("and tppb.part_code = '"+code+"'");
		return dao.pageQuery(sql.toString(), null,getFunName());
	}
	/**
	 * 
	* @Title: getExistPO 
	* @Description: TODO(通过车型组id和配件代码取监控配件的信息) 
	* @param @param id              ：车型组id
	* @param @param code            ：配件代码
	* @param @return   
	* @return List  
	* @throws
	 */
	
	@SuppressWarnings("unchecked")
	public List getExistPO(String id,String code){
		StringBuffer sql= new StringBuffer();
		sql.append(" select * from tt_as_wr_authmonitorpart\n" );
		sql.append(" where is_del = 0\n" );
	//	sql.append(" and model_group = "+id+"\n" );
		sql.append(" and part_code = '"+code+"'");
		return dao.select(TtAsWrAuthmonitorpartPO.class, sql.toString(), null);
	}
	
	/**
	 * Iverson add with 2010-11-09
	 * @Title: getExistBigPO 
	 * @Description: TODO(通过配件大类代码取监控配件的信息) 
	 * @param @param code            ：配件大类代码
	 * @param @return   
	 * @return List  
	 * @throws
	 */
	@SuppressWarnings("unchecked")
	public List getExistBigPO(String code){
		StringBuffer sql= new StringBuffer();
		sql.append("select * from TT_AS_WR_AUTHMONITORTYPE\n" );
		sql.append(" where PART_BIGTYPE_CODE = '"+code+"'");
		return dao.select(TtAsWrAuthmonitorpartPO.class, sql.toString(), null);
	}
	/**
	 * 
	* @Title: claimPartWatchQueryById 
	* @Description: TODO(通过id查询监控配件表的信息) 
	* @param @param id              ：监控配件表的id
	* @param @return
	* @param @throws Exception   
	* @return HashMap  
	* @throws
	 */
	@SuppressWarnings("unchecked")
	public   HashMap claimPartWatchQueryById(String id,Long oemCompanyId) throws Exception {
		List list = dao.getLevelList(oemCompanyId,Constant.AUDIT_TYPE_01.toString());
		HashMap map = new HashMap();
		StringBuffer sb = new StringBuffer();
		sb.append(" select tawap.id,tawmg.wrgroup_code,tawap.part_code, ");
		sb.append("  tawap.part_name,  tawap.approval_level ");
		for(int i = 0 ; i < list.size(); i++){
			HashMap tcpo = (HashMap)list.get(i);
			sb.append(" ,CASE WHEN instr(tawap.approval_level,'"+tcpo.get("APPROVAL_LEVEL_CODE")+"')>0 then '"+tcpo.get("APPROVAL_LEVEL_CODE")+"' else '' end \""+tcpo.get("APPROVAL_LEVEL_CODE")+"\"");
		}		
		sb.append(" from tt_as_wr_authmonitorpart tawap, tt_as_wr_model_group tawmg ");
		sb.append(" where tawap.model_group= tawmg.wrgroup_id(+) ");
		sb.append(" and tawap.is_del = 0 ");
		sb.append(" and tawap.id ="+id+" ");
		sb.append(" order by tawap.create_date desc ");		
		List<Map<String, Object>> relist = pageQuery(sb.toString(), null,getFunName());
		if(relist != null && relist.size() > 0){
			map = (HashMap)relist.get(0);
		}
		return map;
	}
	
	/**
	 * Iverson add with 2010-11-09
	 * @Title: claimPartBigWatchQueryById 
	 * @Description: TODO(通过id查询监控配件大类表的信息) 
	 * @param @param id              ：监控配件大类表的id
	 * @param @return
	 * @param @throws Exception   
	 * @return HashMap  
	 * @throws
	 */
	@SuppressWarnings("unchecked")
	public   HashMap claimPartBigWatchQueryById(String id,Long oemCompanyId) throws Exception {
		List list = dao.getLevelList(oemCompanyId,Constant.AUDIT_TYPE_01.toString());
		HashMap map = new HashMap();
		StringBuffer sb = new StringBuffer();
		sb.append(" select tawat.ID,tawat.PART_BIGTYPE_CODE, ");
		sb.append("  tawat.PART_BIGTYPE_NAME,  tawat.approval_level ");
		for(int i = 0 ; i < list.size(); i++){
			HashMap tcpo = (HashMap)list.get(i);
			sb.append(" ,CASE WHEN instr(tawat.approval_level,'"+tcpo.get("APPROVAL_LEVEL_CODE")+"')>0 then '"+tcpo.get("APPROVAL_LEVEL_CODE")+"' else '' end \""+tcpo.get("APPROVAL_LEVEL_CODE")+"\"");
		}		
		sb.append(" from TT_AS_WR_AUTHMONITORTYPE tawat ");
		sb.append(" where  ");
		sb.append("  tawat.is_del = 0 ");
		sb.append(" and tawat.id ="+id+" ");
		sb.append(" order by tawat.create_date desc ");		
		List<Map<String, Object>> relist = pageQuery(sb.toString(), null,getFunName());
		if(relist != null && relist.size() > 0){
			map = (HashMap)relist.get(0);
		}
		return map;
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
/**
 * 工单授权查询
 * @param pageSize
 * @param curPage
 * @param whereSql
 * @param params
 * @param oemCompanyId
 * @return
 * @throws Exception
 */
	@SuppressWarnings("unchecked")
	public   PageResult<Map<String, Object>> workOrderCanQuery(int pageSize, int curPage, String whereSql,List<Object> params) throws Exception {
		PageResult<Map<String, Object>> result = null;
		List list = dao.getOrderLevelList();
		StringBuffer sb = new StringBuffer();
		sb.append(" select tawl.id,tawl.num, ");
		sb.append(" tawl.code_desc, tawl.woor_level ");
		for(int i = 0 ; i < list.size(); i++){
			HashMap tcpo = (HashMap)list.get(i);
			sb.append(" ,CASE WHEN instr(tawl.woor_level,'"+tcpo.get("APPROVAL_LEVEL_CODE")+"')>0 then '"+tcpo.get("APPROVAL_LEVEL_CODE")+"' else '' end S"+tcpo.get("APPROVAL_LEVEL_CODE")+"\n");
		}		
		sb.append(" from tt_as_wr_woor_level tawl");
		sb.append(" where 1=1 ");
		if(whereSql != null && !"".equals(whereSql.trim())){
			sb.append(whereSql);
		}
		sb.append(" order by tawl.create_date desc ");
		result = super.pageQuery(sb.toString(), params, getFunName(), pageSize, curPage);
		return result;
	}
	/**
	 * 
	* @Title: getOrderLevelList 
	* @Description: TODO(获取授权级别列表) 
	* @param oemCompanyId
	* @param type 授权规则类型(0:技术授权 1:结算授权)
	* @return List  
	* @throws
	* select tawa.approval_level_code,tawa.approval_level_name,tawa.approval_level_tier from tt_as_wr_authinfo tawa   where tawa.approval_level_code<>100  and tawa.type = 0
 order by tawa.approval_level_code ; 
	 */
	@SuppressWarnings("unchecked")
	public List getOrderLevelList(){
		StringBuffer sb = new StringBuffer();
		sb.append("select tawa.approval_level_code,tawa.approval_level_name,tawa.approval_level_tier from tt_as_wr_authinfo tawa  ");
		sb.append("where tawa.approval_level_code<>100  ");
		sb.append(" and tawa.type = 0");
		sb.append(" order by tawa.Approval_Level_Tier ");
		List<Map<String, Object>> list = pageQuery(sb.toString(), null,getFunName());
		return list;
	}
	/**
	 * 
	* @Title: workOrderCanByNum 
	* @Description: TODO(通过num查询工单权限的信息) 
	* @param @param num (维修类型代码)              
	* @param @return
	* @param @throws Exception   
	* @return HashMap  
	* @throws
	 */
	@SuppressWarnings("unchecked")
	public   HashMap workOrderCanByNum(String num) throws Exception {
		List list = dao.getOrderLevelList();
		HashMap map = new HashMap();
		StringBuffer sb = new StringBuffer();
		sb.append(" select tawl.id,tawl.num, ");
		sb.append(" tawl.code_desc, tawl.woor_level ");
		for(int i = 0 ; i < list.size(); i++){
			HashMap tcpo = (HashMap)list.get(i);
			sb.append(" ,CASE WHEN instr(tawl.woor_level,'"+tcpo.get("APPROVAL_LEVEL_CODE")+"')>0 then '"+tcpo.get("APPROVAL_LEVEL_CODE")+"' else '' end S"+tcpo.get("APPROVAL_LEVEL_CODE")+"\n");
		}		
		sb.append(" from tt_as_wr_woor_level tawl");
		sb.append(" where tawl.num ="+num+" ");
		sb.append(" order by tawl.create_date desc ");		
		List<Map<String, Object>> relist = pageQuery(sb.toString(), null,getFunName());
		if(relist != null && relist.size() > 0){
			map = (HashMap)relist.get(0);
		}
		return map;
	}
}
