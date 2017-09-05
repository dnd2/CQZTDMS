/**   
* @Title: ClaimLaborWatchDao.java 
* @Package com.infodms.dms.dao.claim.authorization 
* @Description: TODO(监控工时维护DAO) 
* @author Administrator   
* @date 2010-6-8 下午02:58:42 
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
import com.infodms.dms.po.TtAsWrAuthmonitorlabPO;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

/** 
 * @ClassName: ClaimLaborWatchDao 
 * @Description: TODO(监控工时维护DAO) 
 * @author Administrator 
 * @date 2010-6-8 下午02:58:42 
 *  
 */
public class ClaimLaborWatchDao extends BaseDao {
	public static Logger logger = Logger.getLogger(ClaimLaborWatchDao.class);
	private static final ClaimLaborWatchDao dao = new ClaimLaborWatchDao ();
	public static final ClaimLaborWatchDao getInstance() {
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
		sb.append(" order by tawa.Approval_Level_Tier ");
		List<Map<String, Object>> list = pageQuery(sb.toString(), null,getFunName());
		return list;
	}
	/**
	 * 
	* @Title: claimLaborWatchQuery 
	* @Description: TODO(监控工时查询) 
	* @param @param pageSize
	* @param @param curPage
	* @param @param whereSql
	* @param @param params
	* @param @return
	* @param @throws Exception   
	* @return PageResult<Map<String,Object>>  
	* @throws
	 */
	@SuppressWarnings("unchecked")
	public   PageResult<Map<String, Object>> claimLaborWatchQuery(int pageSize, int curPage, String whereSql,List<Object> params,Long oemCompanyId) throws Exception {
		PageResult<Map<String, Object>> result = null;
		List list = dao.getLevelList(oemCompanyId,Constant.AUDIT_TYPE_01.toString());
		StringBuffer sb = new StringBuffer();
		sb.append(" select tawal.id,tawmg.wrgroup_code,tawal.labour_operation_no, tawal.is_del,");
		sb.append(" tawal.labour_operation_name, ");
		sb.append(" tawal.approval_level ");
		for(int i = 0 ; i < list.size(); i++){
			HashMap tcpo = (HashMap)list.get(i);
			sb.append(" ,CASE WHEN instr(tawal.approval_level,'"+tcpo.get("APPROVAL_LEVEL_CODE")+"')>0 then '"+tcpo.get("APPROVAL_LEVEL_CODE")+"' else '' end \""+tcpo.get("APPROVAL_LEVEL_CODE")+"\"");
		}		
		sb.append(" from tt_as_wr_authmonitorlab tawal, tt_as_wr_model_group tawmg ");
		sb.append(" where tawal.model_group= tawmg.wrgroup_id(+) ");
		/*sb.append(" and tawal.is_del = 0 ");*/
		if(whereSql != null && !"".equals(whereSql.trim())){
			sb.append(whereSql);
		}
		sb.append(" order by tawal.id desc ");
		result = (PageResult<Map<String, Object>>) pageQuery(sb.toString(), params, getFunName(), pageSize, curPage);
		return result;
	}
	/**
	 * 
	* @Title: getModelGroupById 
	* @Description: TODO(根据车型组id查询索赔工时列表，用于弹出页) 
	* @param @param whereSql
	* @param @param params
	* @param @return   
	* @return List  
	* @throws
	 */
	@SuppressWarnings("unchecked")
	public List getModelGroupById(Long oemCompanyId , String whereSql,List<Object> params){
		StringBuffer sql= new StringBuffer();
		sql.append(" select taww.id,taww.wrgroup_id,tawmg.wrgroup_code,taww.labour_code,taww.cn_des,\n" );
		sql.append(" taww.labour_quotiety,taww.labour_hour\n" );
		sql.append(" from tt_as_wr_wrlabinfo taww\n" );
		sql.append(" join tt_as_wr_model_group tawmg on taww.wrgroup_id = tawmg.wrgroup_id\n" );
		sql.append(" where taww.is_del = 0\n" );
		sql.append(" and taww.tree_code =3\n" );
		sql.append("and taww.labour_code not in (select labour_operation_no from tt_as_wr_authmonitorlab where model_group=?)"); 

		if(Utility.testString(whereSql)){
			sql.append(whereSql);
		}
		List<Map<String, Object>> list = pageQuery(sql.toString(), params,getFunName());
		return list;
	}
	public PageResult<Map<String, Object>> getModelGroupById2(Long oemCompanyId , String whereSql,List<Object> params,int pageSize, int curPage){
		StringBuffer sql= new StringBuffer();
		sql.append(" select taww.id,taww.wrgroup_id,tawmg.wrgroup_code,taww.labour_code,taww.cn_des,\n" );
		sql.append(" taww.labour_quotiety,taww.labour_hour\n" );
		sql.append(" from tt_as_wr_wrlabinfo taww\n" );
		sql.append(" join tt_as_wr_model_group tawmg on taww.wrgroup_id = tawmg.wrgroup_id\n" );
		sql.append(" where taww.is_del = 0\n" );
		sql.append(" and taww.tree_code =3\n" );
		sql.append("and taww.labour_code not in (select labour_operation_no from tt_as_wr_authmonitorlab where model_group=?)"); 

		if(Utility.testString(whereSql)){
			sql.append(whereSql);
		}
		PageResult<Map<String, Object>> list =(PageResult<Map<String, Object>>) pageQuery(sql.toString(), params,getFunName(), pageSize, curPage);
		return list;
	}
	/**
	 * 
	* @Title: getExistPO 
	* @Description: TODO(根据车型组id和工时代码查询监控工时记录) 
	* @param @param id
	* @param @param code
	* @param @return   
	* @return List  
	* @throws
	 */
	@SuppressWarnings("unchecked")
	public List getExistPO(String id,String code){
		StringBuffer sb1 = new StringBuffer();
		sb1.append(" SELECT * FROM Tt_As_Wr_Authmonitorlab WHERE 1=1 ");
		sb1.append(" AND model_group='"+id+"' ");
		sb1.append(" and Labour_Operation_No='"+code+"' ");
		sb1.append(" and is_del = 0 ");
		return dao.select(TtAsWrAuthmonitorlabPO.class, sb1.toString(), null);
	}
	/**
	 * 
	* @Title: claimLaborWatchQueryById 
	* @Description: TODO(根据id查询监控工时的记录，用于修改前的查询) 
	* @param @param id             
	* @param @return
	* @param @throws Exception   
	* @return HashMap  
	* @throws
	 */
	@SuppressWarnings("unchecked")
	public   HashMap claimLaborWatchQueryById(String id,Long oemCompanyId) throws Exception {
		List list = dao.getLevelList(oemCompanyId,Constant.AUDIT_TYPE_01.toString());
		HashMap map = new HashMap();
		StringBuffer sb = new StringBuffer();
		sb.append(" select tawal.id,tawmg.wrgroup_code,tawal.labour_operation_no, ");
		sb.append(" tawal.labour_operation_name, tawmg.wrgroup_id, ");
		sb.append(" tawal.approval_level ");
		for(int i = 0 ; i < list.size(); i++){
			HashMap tcpo = (HashMap)list.get(i);
			sb.append(" ,CASE WHEN instr(tawal.approval_level,'"+tcpo.get("APPROVAL_LEVEL_CODE")+"')>0 then '"+tcpo.get("APPROVAL_LEVEL_CODE")+"' else '' end \""+tcpo.get("APPROVAL_LEVEL_CODE")+"\"");
		}		
		sb.append(" from tt_as_wr_authmonitorlab tawal, tt_as_wr_model_group tawmg ");
		sb.append(" where tawal.model_group= tawmg.wrgroup_id(+) ");
		sb.append(" and tawal.id ="+id+" ");
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

}
