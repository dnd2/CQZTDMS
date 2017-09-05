/**********************************************************************
 * <pre>
 * FILE : SysRoleDAO.java
 * CLASS : SysRoleDAO
 * 
 * AUTHOR : ChenLiang
 *
 * FUNCTION : 系统角色DAO.
 *
 *
 *======================================================================
 * CHANGE HISTORY LOG
 *----------------------------------------------------------------------
 * MOD. NO.| DATE     |    NAME    | REASON | CHANGE REQ.
 *----------------------------------------------------------------------
 *         |2009-08-03| ChenLiang  | Created |
 * DESCRIPTION:
 * </pre>
 ***********************************************************************/

package com.infodms.dms.dao.sysrole;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.common.OrderUtil;
import com.infodms.dms.dao.usermng.UserMngDAO;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TcRolePO;
import com.infoservice.po3.POFactory;
import com.infoservice.po3.POFactoryBuilder;
import com.infoservice.po3.bean.PageResult;
import com.infoservice.po3.core.callback.DAOCallback;

public class SysRoleDAO {
	private static POFactory factory = POFactoryBuilder.getInstance();
	public static Logger logger = Logger.getLogger(SysRoleDAO.class);
	private static final  UserMngDAO daos = new UserMngDAO();
	/**
	 * 系统角色查询
	 * 
	 * @param roleDesc
	 *            角色名称
	 * @param roleName
	 *            角色代码
	 * @param roleType
	 *            角色类型
	 * @param curPage
	 *            当前页
	 * @param pageSize
	 *            每页最大记录数
	 * @param orderName
	 *            排序字段名
	 * @param da
	 *            排序类型
	 * @return
	 * @throws Exception
	 */
	public static PageResult<Map<String, Object>> sysRoleQuery(String roleDesc,
			String roleName, String roleType, Long comapnyId,int curPage, int pageSize,
			String orderName, String da,Long userId,Long poseId) throws BizException {
		StringBuffer query = new StringBuffer("select UPDATE_DATE,(SELECT NAME FROM TC_USER  WHERE USER_ID=A.UPDATE_BY) AS UPDATE_NAME,ROLE_ID,ROLE_DESC,ROLE_NAME,ROLE_TYPE,ROLE_STATUS from tc_role A where 1=1  AND OEM_COMPANY_ID="+comapnyId);
		if (roleDesc != null && !roleDesc.equals("")) { // 拼查询角色的SQL
			query.append("  and ROLE_DESC like '%" + roleDesc + "%'");
		}
		if (roleName != null && !roleName.equals("")) {
			query.append("  and upper(ROLE_NAME) like '%" + roleName.toUpperCase() + "%'");
		}
		if (roleType != null && !roleType.equals("")) {
			query.append("  and ROLE_TYPE like '%" + roleType + "%'");
		}
		
		/**********用于判断sa用户不是sa用户登录进来的不能修改    add zhumingwei 2014-2-17**************************/
		/*if(userId!=1000002433){
			query.append("and a.role_id <> 4000004040 \n");
//			query.append("       (select e.role_id\n");
//			query.append("          from tr_role_pose e\n");
//			query.append("         where e.pose_id = (select g.pose_id\n");
//			query.append("                              from tr_user_pose g\n");
//			query.append("                             where g.user_id = "+userId+"\n");
//			query.append("                               and g.pose_id = "+poseId+"))");
		}else{
			if(poseId!=4000008240L){
				// 如果当前用户是SA,但职位权限不是超级用户的职位
				query.append("and a.role_id <> 4000004040 \n");
//				query.append("and a.role_id not in\n");
//				query.append("       (select e.role_id\n");
//				query.append("          from tr_role_pose e\n");
//				query.append("         where e.pose_id = (select g.pose_id\n");
//				query.append("                              from tr_user_pose g\n");
//				query.append("                             where g.user_id = "+userId+"\n");
//				query.append("                               and g.pose_id = "+poseId+"))");
			}
		}*/
		
		query.append("  ORDER BY ROLE_STATUS,rownum ");
		
		logger.debug("SQL+++++++++++++++++++++++: " + query);
		/*return factory.pageQuery(OrderUtil.addOrderBy(query.toString(), orderName, da), null, new DAOCallback<TcRolePO>() {
			public TcRolePO wrapper(ResultSet rs, int idx) {
				TcRolePO bean = new TcRolePO();
				try {
					bean.setRoleId(rs.getLong("ROLE_ID"));
					bean.setRoleDesc(rs.getString("ROLE_DESC"));
					bean.setRoleName(rs.getString("ROLE_NAME"));
					bean.setRoleType(rs.getInt("ROLE_TYPE"));
					bean.setRoleStatus(rs.getInt("ROLE_STATUS"));
				} catch (SQLException e) {
					e.printStackTrace();
				}
				return bean;
			}
		}, curPage, pageSize);*/
		 PageResult<Map<String, Object>> ps =daos.pageQuery(query.toString(), null, daos.getFunName(), curPage, pageSize);
		 return ps;
	}

	/**
	 * 根据角色ID和角色名称查找
	 * 
	 * @param roleDesc
	 *            角色名称
	 * @param roleId
	 *            角色ID
	 * @return
	 * @throws Exception
	 */
	public static List<TcRolePO> getRoleByRoleIdOrRoleDesc(String roleDesc,
			Long roleId,Integer roleType,Long companyId) throws BizException {
		StringBuffer query = new StringBuffer(" select role_id from tc_role where role_desc = '"
				+ roleDesc + "' and role_id <> '" + roleId + "' and role_type ='" + roleType + "' and oem_company_id="+companyId);
		logger.debug("SQL+++++++++++++++++++++++: " + query);
		return factory.select(query.toString(), null, new DAOCallback<TcRolePO>() {
			public TcRolePO wrapper(ResultSet rs, int idx) {
				TcRolePO bean = new TcRolePO();
				try {
					bean.setRoleId(rs.getLong("ROLE_ID"));
				} catch (SQLException e) {
					e.printStackTrace();
				}
				return bean;
			}
		});
	}
}
