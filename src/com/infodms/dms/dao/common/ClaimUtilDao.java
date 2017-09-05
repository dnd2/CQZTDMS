/**   
* @Title: ClaimUtilDao.java 
* @Package com.infodms.dms.dao.common 
* @Description: TODO(售后索赔公用DAO) 
* @author wangjinbao   
* @date 2010-6-4 上午10:17:37 
* @version V1.0   
*/
package com.infodms.dms.dao.common;

import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.common.Constant;
import com.infoservice.po3.bean.PO;

/** 
 * @ClassName: ClaimUtilDao 
 * @Description: TODO(售后索赔公用DAO) 
 * @author wangjinbao 
 * @date 2010-6-4 上午10:17:37 
 *  
 */
public class ClaimUtilDao extends BaseDao {
	public static Logger logger = Logger.getLogger(ClaimUtilDao.class);
	private static final ClaimUtilDao dao = new ClaimUtilDao ();
	public static final ClaimUtilDao getInstance() {
		return dao;
	}
	/**
	 * 
	* @Title: getClaimWrModelGroup 
	* @Description: TODO(获得索赔工时组的列表) 
	* @param @param type
	* @param @return   
	* @return List  
	* @throws
	 */
	@SuppressWarnings("unchecked")
	public List getClaimWrModelGroup(Integer type,Long oemCompanyId){
		List<Object> params = new LinkedList<Object>();
		StringBuilder sb = new StringBuilder();
		sb.append(" select tawmg.wrgroup_id,tawmg.wrgroup_name,tawmg.wrgroup_type,tawmg.wrgroup_code  ");
		sb.append(" from TT_AS_WR_MODEL_GROUP tawmg ");
		sb.append("	where tawmg.wrgroup_type = ?  ");
		params.add(type);
		sb.append("");
		List<Map<String, Object>> list = pageQuery(sb.toString(), params,getFunName());
		return list;
	}
	/**
	 * 
	* @Title: getClaimWrLevel 
	* @Description: TODO(获得售后授权级别列表) 
	* @param @return   
	* @return List  
	* @throws
	 */
	@SuppressWarnings("unchecked")
	public List getClaimWrLevel(Long oemCompanyId, String flag){
		StringBuffer sql= new StringBuffer();
		sql.append(" select tawa.approval_level_code,tawa.approval_level_name,tawa.approval_level_tier\n " );
		sql.append(" from tt_as_wr_authinfo tawa\n " );
		sql.append(" where tawa.approval_level_code<>100\n " );
		sql.append(" and tawa.type = ").append(flag).append("\n");
		sql.append(" order by tawa.Approval_Level_Tier ");
		List<Map<String, Object>> list = pageQuery(sql.toString(), null,getFunName());
		return list;
	}
	/**
	 * 
	* @Title: getDealerMap 
	* @Description: TODO(根据经销商id获得经销商信息) 
	* @param @param dealerId     ：经销商ID
	* @param @return   
	* @return Map  
	* @throws
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> getDealerMap(String dealerId){
		List<Object> params = new LinkedList<Object>();
		StringBuffer sql= new StringBuffer();
		sql.append(" select t.dealer_id,t.company_id,t.dealer_type,\n" );
		sql.append(" t.dealer_code,t.dealer_name,t.dealer_level,\n" );
		sql.append(" t.dealer_class,t.dealer_org_id,t.dealer_shortname,\n" );
		sql.append(" t.oem_company_id,t.link_man,t.address,t.parent_dealer_d\n" );
		sql.append(" from tm_dealer t\n" );
		sql.append(" where t.dealer_id = ? \n" );
		sql.append(" and t.status = ? ");
		params.add(dealerId);
		params.add(Constant.STATUS_ENABLE);
		return pageQueryMap(sql.toString(), params,getFunName());
	}
	/**
	 * 
	* @Title: getUserInfo 
	* @Description: TODO(根据userid查询用户信息) 
	* @param @param userId
	* @param @return   
	* @return Map<String,Object>  
	* @throws
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> getUserInfo(Long userId){
		StringBuffer sql= new StringBuffer();
		sql.append(" select * from tc_user t\n" );
		sql.append(" where t.user_status = "+Constant.STATUS_ENABLE+"\n" );
		sql.append(" and t.user_id =" +userId+" ");
		return pageQueryMap(sql.toString(), null,getFunName());
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
