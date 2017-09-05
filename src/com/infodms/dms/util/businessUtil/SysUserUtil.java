/**********************************************************************
* <pre>
* FILE : SysUserUtil.java
* CLASS : SysUserUtil
*
* AUTHOR : xianchao zhang
*
* FUNCTION : TODO
*
*
*======================================================================
* CHANGE HISTORY LOG
*----------------------------------------------------------------------
* MOD. NO.| DATE | NAME | REASON | CHANGE REQ.
*----------------------------------------------------------------------
* 		  |Sep 18, 2009| xianchao zhang| Created |
* DESCRIPTION:
* </pre>
***********************************************************************/
/**
* $Id: SysUserUtil.java,v 1.1 2010/08/16 01:44:40 yuch Exp $
*/
package com.infodms.dms.util.businessUtil;

import java.sql.ResultSet;
import java.util.List;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.bean.TcUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.DataAclUtil;
import com.infoservice.po3.POFactory;
import com.infoservice.po3.POFactoryBuilder;
import com.infoservice.po3.core.callback.DAOCallback;
import com.infoservice.po3.core.exceptions.DAOException;

/**
 * 功能说明：系统用户的相关公用类
 * 典型用法：
 * 示例程序如下：
 * 特殊用法：
 * 创建者：xianchao zhang
 * 创建时间：Sep 18, 2009
 * 修改人：
 * 修改时间：
 * 修改原因：
 * 修改内容：
 * 版本：0.1
 */
public class SysUserUtil {
	private static Logger logger = Logger.getLogger(SysUserUtil.class);
	private static POFactory factory = POFactoryBuilder.getInstance();
	/**
	 * 查询某个职位下的用户集合
	 * @author Andy
	 * @throws Exception 
	 * @decription
	 * 用户查询结果查询结果
	 * 
	 * 参数：
	 * 经销商
	 * 意向
	 * 部门
	 */
	public static List<TcUserBean> queryUsersByPosition(String actionName,AclUserBean logonUser,String dealerId,String positionType,String departmentId) throws Exception{
		try {
			StringBuffer sql = new StringBuffer();
			String actionSql = "";
			sql.append("WITH position AS(\n");
			sql.append("select tp.pose_id,\n");
			sql.append("       tp.pose_name,\n");
			sql.append("       tp.pose_code,\n");
			sql.append("       tp.dlr_id,\n");
			sql.append("       tp.dept_id,\n");
			sql.append("       td.dept_name,\n");
			sql.append("       tdlr.company_name\n");
			sql.append("  from tc_pose tp, tm_dept td, tm_company tdlr\n");
			sql.append(" where exists (select 1\n");
			sql.append("          from tr_pose_bins tpb\n");
			sql.append("         where tp.pose_id = tpb.pose_id\n");
			//传入职位类型
			if(!"".equalsIgnoreCase(positionType)){
				sql.append("           and tpb.bins_code_id = '"+positionType+"'\n");
			}
			sql.append("         )\n");
			//传入查询部门
//			if(!"".equalsIgnoreCase(departmentId)){
//				sql.append("           and tp.dept_id = '"+departmentId+"'\n");
//			}
			sql.append("   and tp.dept_id = td.dept_id\n");
			sql.append("   and tp.dlr_id = tdlr.company_id\n");
			//传入查询经销商
			if(!"".equalsIgnoreCase(dealerId)){
				sql.append("   and tp.dlr_id = '"+dealerId+"'\n");
			}
			sql.append("   )\n");
			sql.append("   select tu.*,\n");
			sql.append("          p.dlr_id,\n");//数据校验字段
			sql.append("          p.dept_id,\n");//数据校验字段
			sql.append("          (tu.name||'('||p.dept_name||')') conname,\n");
			sql.append("          (tu.user_id||'&'||p.dept_id) conid\n");
			sql.append("     from tc_user tu, tr_user_pose tup, position p\n");
			sql.append("   where tup.pose_id = p.pose_id\n");
			sql.append("   and tu.user_id = tup.user_id\n");
			sql.append("   and tu.user_status = '"+Constant.STATUS_ENABLE+"'\n");
			if(!"".equalsIgnoreCase(dealerId)){
				sql.append("   and tu.company_id = '"+dealerId+"'\n");
			}
			
			actionSql = DataAclUtil.getAclSql(sql.toString(), "user_id", "dept_id", "dlr_id", actionName, logonUser, "conname", " asc");
			logger.debug(sql.toString());
			List<TcUserBean> userList = factory.select(actionSql,null,new DAOCallback<TcUserBean>() {
				public TcUserBean wrapper(ResultSet rs, int idx){
					TcUserBean bean = new TcUserBean();
					try {
						bean.setName(rs.getString("name"));
						bean.setUserId(rs.getString("user_id"));
						bean.setDeptId(rs.getString("dept_id"));
						bean.setNameNameAndDept(rs.getString("conname"));
						bean.setIdNameAndDept(rs.getString("conid"));
					} catch (Exception e) {
						throw new DAOException(e);
					}
					return bean;
				}
			});
			return userList;
		}catch (Exception e) {
			throw e;
		}
	}
}
