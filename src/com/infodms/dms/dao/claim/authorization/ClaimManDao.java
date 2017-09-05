/**   
* @Title: ClaimManDao.java 
* @Package com.infodms.dms.dao.claim.authorization 
* @Description: TODO(授权人员管理DAO) 
* @author wangjinbao   
* @date 2010-6-11 上午08:46:47 
* @version V1.0   
*/
package com.infodms.dms.dao.claim.authorization;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.common.Constant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.po.TcUserPO;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

/** 
 * @ClassName: ClaimManDao 
 * @Description: TODO(授权人员管理DAO) 
 * @author wangjinbao 
 * @date 2010-6-11 上午08:46:47 
 *  
 */
public class ClaimManDao extends BaseDao {
	public static Logger logger = Logger.getLogger(ClaimManDao.class);
	private static final ClaimManDao dao = new ClaimManDao ();
	public static final ClaimManDao getInstance() {
		return dao;
	}
	/**
	 * 
	* @Title: claimManQuery 
	* @Description: TODO(授权人员管理查询) 
	* @param @param pageSize
	* @param @param curPage
	* @param @param map                 ：对应的查询条件map
	* @param @return
	* @param @throws Exception   
	* @return PageResult<Map<String,Object>>  
	* @throws
	 */
	@SuppressWarnings("unchecked")
	public PageResult<Map<String, Object>> claimManQuery(int pageSize, int curPage,HashMap map) throws Exception {
		PageResult<Map<String, Object>> result = null;
		List<Object> params = new LinkedList<Object>();
		String auditFlag = String.valueOf(map.get("aduitFlag"));
		StringBuffer sql= new StringBuffer();
		sql.append(" select distinct tu.user_id,tu.acnt,tu.emp_num,tu.name,tu.user_status,\n " );
		if(auditFlag.equals(String.valueOf(Constant.AUDIT_TYPE_01))){
			sql.append(" tu.company_id,tu.phone,tu.email,decode(tu.approval_level_code,'0','',tu.approval_level_code) approval_level_code,tawa.approval_level_name,tu.person_code,\n " );
		}else{
			sql.append(" tu.company_id,tu.phone,tu.email,tu.BALANCE_LEVEL_CODE approval_level_code,tawa.approval_level_name,'' person_code,\n " );
		}
		sql.append(" ");
		sql.append(" tu.update_date,tu.create_date\n");
		sql.append(" from tc_user tu,tc_pose tp,tr_user_pose tup, \n " );
		
		//----2013.05.20 艾春修改授权人员管理功能：过滤原结算授权级别-----------------------
        // sql.append(" (select * from tt_as_wr_authinfo tt where 1=1 ");
		sql.append(" (select * from tt_as_wr_authinfo tt where 1=1 and tt.type = 0 ");
		//----2013.05.20 艾春修改授权人员管理功能：过滤原结算授权级别-----------------------
		
		sql.append(" ) tawa \n");
		sql.append(" where tu.user_id = tup.user_id\n " );
		sql.append(" and tp.pose_id = tup.pose_id\n ");
		if(auditFlag.equals(String.valueOf(Constant.AUDIT_TYPE_01))){
			sql.append(" and tu.approval_level_code = tawa.approval_level_code(+)\n ");
		}else{
			sql.append(" and tu.BALANCE_LEVEL_CODE = tawa.approval_level_code(+)\n ");
		}	
		sql.append(" and tu.user_status = ? ");//有用的状态
		params.add(Constant.STATUS_ENABLE);
		if(Utility.testString(map.get("COMPANYID").toString())){
			sql.append(" and tu.company_id = ? ");
			params.add(map.get("COMPANYID"));
		}
		if(Utility.testString(map.get("NAME").toString())){
			sql.append(" and upper(tu.name) like ? ");
			params.add("%"+map.get("NAME").toString().toUpperCase()+"%");
		}
		if(auditFlag.equals(String.valueOf(Constant.AUDIT_TYPE_01))){
			if(Utility.testString(map.get("LEVEL").toString())){
				sql.append(" and tu.approval_level_code = ? ");
				params.add(map.get("LEVEL"));
			}
		}else{
			if(Utility.testString(map.get("LEVEL").toString())){
				sql.append(" and tu.BALANCE_LEVEL_CODE = ? ");
				params.add(map.get("LEVEL"));
			}
		}
		result = getPoseByUserId((PageResult<Map<String, Object>>) pageQuery(sql.toString(), params, getFunName(), pageSize, curPage));
		return result;
	}
	/**
	 * 
	* @Title: getPoseByUserId 
	* @Description: TODO(授权人员管理根据id查找职位) 
	* @param @param rs
	* @param @return   
	* @return PageResult<Map<String,Object>>  
	* @throws
	 */
	@SuppressWarnings("unchecked")
	private  PageResult<Map<String, Object>> getPoseByUserId(PageResult<Map<String, Object>> rs) {
		if (rs.getRecords() != null) {
			List<Map<String, Object>> list = rs.getRecords(); // 取出用户
			String poseName = null;
			String sql = " select tp.pose_name from tc_pose tp,tr_user_pose tup where tup.pose_id = tp.pose_id ";
			for (int i = 0; i < list.size() && list != null; i++) {
				poseName = "";
				HashMap user = (HashMap)list.get(i);
				String sql1 = sql + " and tup.user_id ='" + user.get("USER_ID") + "'";
				List<Map<String, Object>> postList = dao.pageQuery(sql1, null,getFunName());
				for (int j = 0; j < postList.size(); j++) {
					poseName += postList.get(j).get("POSE_NAME") + ",";
				}
				if (!"".equals(poseName) && poseName.length() > 0) {
					poseName = poseName.substring(0, poseName.length() - 1);
				}
				user.put("POSENAME", poseName);//职位列
				list.set(i, user);
			}
			rs.setRecords(list);
		}
		return rs;
	}
	/**
	 * 
	* @Title: getUserById 
	* @Description: TODO(授权人员管理：根据id查询授权人员列表) 
	* @param @param id
	* @param @return   
	* @return Map<String,Object>  
	* @throws
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> getUserById(String id){
		List<Object> params = new LinkedList<Object>();
		StringBuffer sql= new StringBuffer();
		sql.append("select * from tc_user tu where tu.user_id = ? \n" );
		sql.append("and tu.user_status = ? ");
		params.add(id);
		params.add(Constant.STATUS_ENABLE);
		return dao.pageQueryMap(sql.toString(), params, getFunName());
	}
	/**
	 * 
	* @Title: updateUser 
	* @Description: TODO(授权人员管理修改) 
	* @param @param selpo
	* @param @param updatepo   
	* @return void  
	* @throws
	 */
	@SuppressWarnings("unchecked")
	public void updateUser(TcUserPO selpo,TcUserPO updatepo){
		dao.update(selpo, updatepo);
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
