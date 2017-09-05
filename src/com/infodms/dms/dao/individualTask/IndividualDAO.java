/**********************************************************************
 * <pre>
 * FILE : IndividualDAO.java
 * CLASS : IndividualDAO
 * 
 * AUTHOR : ChenLiang
 *
 * FUNCTION : 个人资料DAO.
 *
 *
 *======================================================================
 * CHANGE HISTORY LOG
 *----------------------------------------------------------------------
 * MOD. NO.| DATE     |    NAME    | REASON | CHANGE REQ.
 *----------------------------------------------------------------------
 *         |2009-09-14| ChenLiang  | Created |
 * DESCRIPTION:
 * </pre>
 ***********************************************************************/

package com.infodms.dms.dao.individualTask;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.apache.log4j.Logger;

import com.infodms.dms.common.Constant;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TcPosePO;
import com.infoservice.po3.POFactory;
import com.infoservice.po3.POFactoryBuilder;
import com.infoservice.po3.core.callback.DAOCallback;
import com.infoservice.po3.core.exceptions.DAOException;

public class IndividualDAO {
	public static Logger logger = Logger.getLogger(IndividualDAO.class);
	private static POFactory factory = POFactoryBuilder.getInstance();

	/**
	 * 根据用户ID的到职位列表
	 * @param userId
	 * @return
	 */
	public static List<TcPosePO> getPosebyUserId(String userId) throws BizException {
		String sql = "select tp.pose_name,tp.pose_id,tp.org_id from tc_pose tp,tr_user_pose tup where tup.pose_id = tp.pose_id and tup.user_id = '"
				+ userId + "' and tp.pose_status = '" + Constant.STATUS_ENABLE + "' ";
		List<TcPosePO> lst = factory.select(sql, null,
				new DAOCallback<TcPosePO>() {
					public TcPosePO wrapper(ResultSet rs, int idx) {
						TcPosePO bean = new TcPosePO();
						try {
							bean.setPoseName(rs.getString("pose_name"));
							bean.setPoseId(rs.getLong("pose_id"));
							bean.setOrgId(rs.getLong("org_id"));
						} catch (SQLException e) {
							throw new DAOException(e);
						}
						return bean;
					}
				});
		return lst;
	}

}
