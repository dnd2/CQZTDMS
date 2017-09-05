/**********************************************************************
 * <pre>
 * FILE : DealerDeptMngDAO.java
 * CLASS : DealerDeptMngDAO
 * 
 * AUTHOR : ChenLiang
 *
 * FUNCTION : 经销商部门管理DAO.
 *
 *
 *======================================================================
 * CHANGE HISTORY LOG
 *----------------------------------------------------------------------
 * MOD. NO.| DATE     |    NAME    | REASON | CHANGE REQ.
 *----------------------------------------------------------------------
 *         |2009-08-31| ChenLiang  | Created |
 * DESCRIPTION:
 * </pre>
 ***********************************************************************/

package com.infodms.dms.dao.orgmng;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;

import com.infodms.dms.common.Constant;
import com.infodms.dms.common.OrderUtil;
import com.infodms.dms.po.TmOrgPO;
import com.infoservice.po3.POFactory;
import com.infoservice.po3.POFactoryBuilder;
import com.infoservice.po3.bean.PageResult;
import com.infoservice.po3.core.callback.DAOCallback;

public class DealerDeptMngDAO {
	public static Logger logger = Logger.getLogger(DealerDeptMngDAO.class);
	private static POFactory factory = POFactoryBuilder.getInstance();
	/**
	 * 经销商部门查询
	 * 
	 * @param dealerId
	 * @param orgId
	 * @param curPage
	 * @param pageSize
	 * @param factory
	 * @return
	 * @throws Exception
	 */
	public static PageResult<TmOrgPO> dealerDeptQuery(Long dealerId,
			Long orgId, int curPage, int pageSize,String orderName,String da)
			throws Exception {
		String query = "select "
				+ "( select org_name from tm_org where org_id = td.parent_org_id ) PNAME"
				+ ",td.org_code,td.org_name,td.status,td.org_id "
				+ " from tm_org td where td.company_id = '"
				+ dealerId + "' and td.org_type = '" + Constant.SYS_USER_DEALER + "' ";
		if (orgId != null && !orgId.equals("") && orgId != 0) { // 查询经销商部门
			query += "  and td.parent_org_id = '" + orgId + "'";
		}
		query = OrderUtil.addOrderBy(query, orderName, da);
		logger.debug("SQL+++++++++++++++++++++++: " + query);
		return factory.pageQuery(query, null, new DAOCallback<TmOrgPO>() {
			public TmOrgPO wrapper(ResultSet rs, int idx) {
				TmOrgPO bean = new TmOrgPO();
				try {
					bean.setParentOrgId(new Long(rs.getString("PNAME")));
					bean.setOrgCode(rs.getString("org_code"));
					bean.setOrgName(rs.getString("org_name"));
					bean.setStatus(Integer.valueOf(rs.getString("status")));
					bean.setOrgId(new Long(rs.getString("org_id")));
				} catch (SQLException e) {
					e.printStackTrace();
				}
				return bean;
			}
		}, curPage, pageSize);
	}

	/**
	 * 判断同一经销商下部门代码是否存在
	 * 
	 * @param dealerID
	 *            经销商ID
	 * @param deptCode
	 *            部门代码
	 * @param factory
	 * @return
	 * @throws Exception
	 */
	public static List<TmOrgPO> isDeptCode(Long companyId, String orgCode,
			POFactory factory) throws Exception {
		List<TmOrgPO> list = null;
		String sql = "select td.org_id,td.parent_org_id,td.org_desc,td.org_name,td.org_code"
				+ " from tm_org td where td.company_id = '"
				+ companyId + "' " + "and td.org_code='" + orgCode + "'";

		list = factory.select(sql.toString(), null,
				new DAOCallback<TmOrgPO>() {
					public TmOrgPO wrapper(ResultSet rs, int idx) {
						TmOrgPO bean = new TmOrgPO();
						try {
							bean.setOrgId(rs.getLong("org_id"));
							bean.setParentOrgId(rs.getLong("parent_org_id"));
							bean.setOrgDesc(rs.getString("org_desc"));
							bean.setOrgCode(rs.getString("org_code"));
							bean.setOrgName(rs.getString("org_name"));
						} catch (SQLException e) {
							e.printStackTrace();
						}
						return bean;
					}
				});

		return list;
	}

	/**
	 * 判断同一经销商下部门名称是否存在
	 * 
	 * @param dealerID
	 *            经销商ID
	 * @param deptName
	 *            部门名称
	 * @param factory
	 * @return
	 * @throws Exception
	 */
	public static List<TmOrgPO> isDeptName(Long companyId, String orgName,
			POFactory factory) throws Exception {
		List<TmOrgPO> list = null;
		String sql = "select td.org_id,td.parent_org_id,td.org_desc,td.org_name,td.org_code"
				+ " from tm_org td where td.company_id = '"
				+ companyId + "' " + "and td.org_name='" + orgName + "'";

		list = factory.select(sql.toString(), null,
				new DAOCallback<TmOrgPO>() {
					public TmOrgPO wrapper(ResultSet rs, int idx) {
						TmOrgPO bean = new TmOrgPO();
						try {
							bean.setOrgId(rs.getLong("org_id"));
							bean.setParentOrgId(rs.getLong("parent_org_id"));
							bean.setOrgDesc(rs.getString("org_desc"));
							bean.setOrgCode(rs.getString("org_code"));
							bean.setOrgName(rs.getString("org_name"));
						} catch (SQLException e) {
							e.printStackTrace();
						}
						return bean;
					}
				});
		return list;
	}
	/**
	 * 查询组织等级
	 * 
	 * @param parentOrgId
	 *            组织ID
	 * @param factory
	 * @return
	 * @throws Exception
	 */
	public static List<TmOrgPO> getOrgLevel(String parentOrgId,
			POFactory factory) throws Exception {
		List<TmOrgPO> list = null;
		String sql = "select td.org_level,td.org_id,td.tree_code"
				+ " from tm_org td where  td.org_id=" + parentOrgId + "";

		list = factory.select(sql.toString(), null,
				new DAOCallback<TmOrgPO>() {
					public TmOrgPO wrapper(ResultSet rs, int idx) {
						TmOrgPO bean = new TmOrgPO();
						try {
							bean.setOrgId(rs.getLong("org_id"));
							bean.setOrgLevel(rs.getInt("org_level"));
							bean.setTreeCode(rs.getString("tree_code"));
						} catch (SQLException e) {
							e.printStackTrace();
						}
						return bean;
					}
				});
		return list;
	}
	/**
	 * 查询组织tree_code方法
	 * 
	 * @param parentOrgId
	 *            上级组织ID
	 * @throws Exception
	 */
	public static String getOrgTreeCode(String parentOrgId) throws Exception {
		Object ret;
		List shortName=new ArrayList();
		if(parentOrgId==null)
	    return "";
		else 
		shortName.add(parentOrgId);
		ret = factory.callFunction("F_GET_ORGTCODE", java.sql.Types.VARCHAR, shortName);
		if(ret==null) return "";
		if(ret instanceof String){
			return String.valueOf(ret);
		}else{
			return "";
		}
	}

	/**
	 * 修改时判断同一经销商下部门名称是否重复
	 * 
	 * @param dealerID
	 *            经销商ID
	 * @param deptName
	 *            部门名称
	 * @param deptCode
	 *            部门代码
	 * @param factory
	 * @return
	 * @throws Exception
	 */
	public static List<TmOrgPO> getOrgIdByOrgName(Long companyId,
			String deptName, String deptCode, POFactory factory)
			throws Exception {
		String query = " select td.org_id from tm_org td where td.company_id = '"
				+ companyId
				+ "' "
				+ "and td.org_name = '"
				+ deptName
				+ "' and td.org_code <> '" + deptCode + "' ";
		logger.debug("SQL+++++++++++++++++++++++: " + query);
		return factory.select(query, null, new DAOCallback<TmOrgPO>() {
			public TmOrgPO wrapper(ResultSet rs, int idx) {
				TmOrgPO bean = new TmOrgPO();
				try {
					bean.setOrgId(rs.getLong("org_id"));
				} catch (SQLException e) {
					e.printStackTrace();
				}
				return bean;
			}
		});
	}
}
