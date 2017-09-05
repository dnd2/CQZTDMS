/**********************************************************************
 * <pre>
 * FILE : DealerPositionDAO.java
 * CLASS : DealerPositionDAO
 * 
 * AUTHOR : ChenLiang
 *
 * FUNCTION : 经销商职位DAO.
 *
 *
 *======================================================================
 * CHANGE HISTORY LOG
 *----------------------------------------------------------------------
 * MOD. NO.| DATE     |    NAME    | REASON | CHANGE REQ.
 *----------------------------------------------------------------------
 *         |2009-08-26| ChenLiang  | Created |
 * DESCRIPTION:
 * </pre>
 ***********************************************************************/

package com.infodms.dms.dao.dealerposition;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.apache.log4j.Logger;

import com.infodms.dms.common.Constant;
import com.infodms.dms.common.OrderUtil;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TcPosePO;
import com.infodms.dms.po.TcRolePO;
import com.infodms.dms.po.TrRoleFuncPO;
import com.infoservice.po3.POFactory;
import com.infoservice.po3.POFactoryBuilder;
import com.infoservice.po3.bean.PageResult;
import com.infoservice.po3.core.callback.DAOCallback;

public class DealerPositionDAO {
	public static Logger logger = Logger.getLogger(DealerPositionDAO.class);
	private static POFactory factory = POFactoryBuilder.getInstance();

	public static PageResult<TcPosePO> sysPoseQuery(String dealerPoseCode,
			String dealerPoseName, Long orgId, int curPage, int pageSize,
			String orderName, String da) throws Exception {
		String query = "select POSE_ID,POSE_CODE,POSE_NAME,POSE_TYPE,POSE_STATUS from tc_pose where 1=1 ";
		if (dealerPoseCode != null && !dealerPoseCode.equals("")) {
			query += "  and upper(POSE_CODE) like '%" + dealerPoseCode.toUpperCase() + "%'";
		}
		if (dealerPoseName != null && !dealerPoseName.equals("")) {
			query += "  and upper(POSE_NAME) like '%" + dealerPoseName.toUpperCase() + "%'";
		}
		if (orgId != null && !orgId.equals("") && orgId != 0) {
			query += "  and ORG_ID = '" + orgId + "'";
		}
		query += " and pose_type = '" + Constant.SYS_USER_DEALER + "' ";
		query = OrderUtil.addOrderBy(query, orderName, da);
		logger.debug("SQL+++++++++++++++++++++++: " + query);
		return factory.pageQuery(query, null, new DAOCallback<TcPosePO>() {
			public TcPosePO wrapper(ResultSet rs, int idx) {
				TcPosePO bean = new TcPosePO();
				try {
					bean.setPoseId(rs.getLong("POSE_ID"));
					bean.setPoseCode(rs.getString("POSE_CODE"));
					bean.setPoseName(rs.getString("POSE_NAME"));
					bean.setPoseType(rs.getInt("POSE_TYPE"));
					bean.setPoseStatus(rs.getInt("POSE_STATUS"));
				} catch (SQLException e) {
					e.printStackTrace();
				}
				return bean;
			}
		}, curPage, pageSize);
	}

	public static PageResult<TcRolePO> sysRoleQuery(String roleType,
			String roleName, int curPage, int pageSize) throws Exception {
		String query = "select ROLE_ID,ROLE_DESC,ROLE_NAME,ROLE_TYPE,ROLE_STATUS from tc_role where 1=1 ";
		if (roleName != null && !roleName.equals("")) {
			query += "  and ROLE_DESC like '%" + roleName + "%'";
		}
		if (roleType != null && !roleType.equals("")) {
			query += "  and ROLE_TYPE = '" + roleType + "'";
		}

		logger.debug("SQL+++++++++++++++++++++++: " + query);
		return factory.pageQuery(query, null, new DAOCallback<TcRolePO>() {
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
		}, curPage, pageSize);
	}

	/**
	 * 修改职位时职位名称是否重复
	 * 
	 * @param DealerId
	 *            公司ID
	 * @param poseCode
	 *            职位代码
	 * @param poseName
	 *            职位名称
	 * @return
	 * @throws Exception
	 */
	public static List<TcPosePO> getDealerPoseByDealerIdOrPoseCode(
			Long CompanyId, String poseCode, String poseName)
			throws BizException {
		StringBuffer query = new StringBuffer(
				"select t.pose_id from tc_pose t where t.company_id = '" + CompanyId
						+ "' and t.pose_code <> '" + poseCode
						+ "' and t.pose_name = '" + poseName + "'");
		logger.debug("SQL+++++++++++++++++++++++: " + query);
		return factory.select(query.toString(), null,
				new DAOCallback<TcPosePO>() {
					public TcPosePO wrapper(ResultSet rs, int idx) {
						TcPosePO bean = new TcPosePO();
						try {
							bean.setPoseId(rs.getLong("pose_id"));
						} catch (SQLException e) {
							e.printStackTrace();
						}
						return bean;
					}
				});
	}

	public static List<TrRoleFuncPO> sysFunsByRoleIdsQuery(String[] roleids)
			throws Exception {
		String query = "select DISTINCT func_id from tr_role_func where 1=1 ";

		for (int i = 0; i < roleids.length; i++) {
			if (i == 0) {
				query += " and role_id ='" + roleids[i] + "' ";
			} else {
				query += " or role_id ='" + roleids[i] + "' ";
			}
		}

		logger.debug("SQL+++++++++++++++++++++++: " + query);
		return factory.select(query, null, new DAOCallback<TrRoleFuncPO>() {
			public TrRoleFuncPO wrapper(ResultSet rs, int idx) {
				TrRoleFuncPO bean = new TrRoleFuncPO();
				try {
					bean.setFuncId(rs.getLong("func_id"));
				} catch (SQLException e) {
					e.printStackTrace();
				}
				return bean;
			}
		});
	}
}
