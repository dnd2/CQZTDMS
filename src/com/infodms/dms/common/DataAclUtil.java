/**********************************************************************
 * <pre>
 * FILE : DataAclUtil.java
 * CLASS : DataAclUtil
 * 
 * AUTHOR : ChenLiang
 *
 * FUNCTION : 数据权限.
 *
 *
 *======================================================================
 * CHANGE HISTORY LOG
 *----------------------------------------------------------------------
 * MOD. NO.| DATE     |    NAME    | REASON | CHANGE REQ.
 *----------------------------------------------------------------------
 *         |2009-09-09| ChenLiang  | Created |
 * DESCRIPTION:
 * </pre>
 ***********************************************************************/

package com.infodms.dms.common;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TcDataAuthPO;
import com.infoservice.po3.POFactory;
import com.infoservice.po3.POFactoryBuilder;
import com.infoservice.po3.core.callback.DAOCallback;
import com.infoservice.po3.core.exceptions.DAOException;

public class DataAclUtil {

	public static Logger logger = Logger.getLogger(DataAclUtil.class);
	private static POFactory factory = POFactoryBuilder.getInstance();

	/**
	 * 用户数据权限控制公共方法(dealPerId，deptId和dlrId字段在原始SQL中必须暴露在最外层)
	 * 
	 * @param sql
	 *            原始SQL
	 * @param dealPerId
	 *            在原始SQL中业务所有人字段名，可以为空
	 * @param deptId
	 *            在原始SQL中业务所有人所在部门字段名
	 * @param dlrId
	 *            在原始SQL中公司ID字段名
	 * @param actionName
	 *            用户当前调用的action名字（类名）
	 * @param user
	 *            登录用户对象
	 * @param orderName
	 *            排序的字段名
	 * @param da
	 *            升序ASC,降序DESC
	 * @return String 添加了数据权限控制条件和排序条件的SQL
	 * @throws BizException
	 */
	public static String getAclSql(String sql, String dealPerId, String deptId,
			String dlrId, String actionName, AclUserBean user,
			String orderName, String da) throws BizException {
		StringBuffer bf = new StringBuffer();
		
		List<TcDataAuthPO> dataAuthIds = getDataAuthByPoseIdOrActionName(user.getPoseId(),
				actionName); // 得到用户在该功能的数据权限ID
		
		if (dataAuthIds == null || dataAuthIds.size() < 1) { // 未找到该功能的数据权限
			throw new BizException(ErrorCodeConstant.DATA_ACL_NOT_FOUND_CODE);
		}

		if(user.getUserType().equals(Constant.SYS_USER_DEALER)) {
			bf.append(" SELECT * FROM ( " + sql + " ) WHERE ");
		} else {
			bf.append(" SELECT * FROM ( " + sql + " ) # WHERE ");
		}

		for (int i = 0; i < dataAuthIds.size(); i++) { // 拼限制权限的SQL
			bf.append(" ( " + getAuthSqlByAuthId(dataAuthIds.get(i).getDataAuthId(),dlrId,dealPerId,user,deptId) + " ) OR ");
		}
		
		if(user.getUserType().equals(Constant.SYS_USER_SGM)) {
			if(bf.indexOf("TTCC") != -1) { // SGM用户 不含大卖场
				bf = bf.replace(bf.indexOf("#"), bf.indexOf("#")+1, ",TM_COMPANY TTCC");
			} else {
				bf = bf.replace(bf.indexOf("#"), bf.indexOf("#")+1, "");
			}
		}
		
		bf = bf.delete(bf.length()-3, bf.length());
		
		if(orderName != null && !"".equals(orderName)) {
			/**
			 * add by zhangxianchao 
			 * 新增按拼音排序的方式
			 */
			String[] arr = orderName.split("-");
			String orderType = null;
			if(arr!=null && arr.length>=2){
				orderName = arr[0];
				orderType = arr[1];
			}else{
				orderName = arr[0];
			}
			if(orderType!=null&&orderType.equals("pingyin")){
				bf.append(" order by NLSSORT("+orderName+",'NLS_SORT = SCHINESE_PINYIN_M')"
						+ ("1".equals(da) ? " asc" : " desc"));
			}else{
				bf.append(" order by " + orderName
						+ ("1".equals(da) ? " asc" : " desc"));
			}
			
		}
		logger.debug("getAclSql ++++++++++++++++++++:  " + bf.toString());
		return bf.toString();
	}
	
	/**
	 * 根据权限ID得到SQL
	 * @param aid 权限ID
	 * @param dlrId
	 * @param dealPerId
	 * @param user
	 * @param deptId
	 * @return
	 */
	private static String getAuthSqlByAuthId(Long aid,String dlrId,String dealPerId,AclUserBean user,String deptId) {
		if (Constant.DLR_BR.equals(aid)) { // 经销商-本人
			if(dealPerId == null || "".equals(dealPerId)) {
				return dlrId + " = '" + user.getCompanyId()+"' ";
			} else {
				return dlrId + " = '" + user.getCompanyId()	+ "' AND " + dealPerId + " = '" + user.getUserId() + "' ";
			}
		} else if (Constant.DRL_BZZJYX.equals(aid)) { // 经销商-本组织及以下
			if(deptId == null || "".equals(deptId)) {
				return dlrId + " = '" + user.getCompanyId()+"' ";
			} else {
				return dlrId + " = '" + user.getCompanyId() + "' AND " + deptId + " in (" + user.getBxjDept() + ") ";
			}
		} else if (Constant.DRL_ZGJXS.equals(aid)) { // 经销商-整个经销商
			return dlrId + " = '" + user.getCompanyId() + "' ";
		} else if (Constant.SGM_BZZJYX.equals(aid)) { // 车厂-本组织及以下
			return " " + dlrId + " = " + user.getCompanyId() + " AND " + deptId + " IN (" + user.getBxjDept() + ") ";
		} else if (Constant.SGM_ZGJXS.equals(aid)) { // 车厂-整个SGM经销商
			return " 1=1 ";
		} 
		return "";
	}

	/**
	 * 根据职位ID和功能名得到用户当前的数据权限
	 * 
	 * @param poseId
	 *            用户当前的职位ID
	 * @param actionName
	 *            用户当前功能ID
	 * @return String 功能权限代码
	 * @throws BizException
	 */
	private static List<TcDataAuthPO> getDataAuthByPoseIdOrActionName(Long poseId,
			String actionName) throws BizException {
		if(actionName!=null&&actionName.indexOf("/") > -1 && actionName.indexOf(".") > -1) {
			actionName = actionName.substring(1, actionName.length());
			actionName = actionName.substring(actionName.indexOf("/"), actionName.indexOf("."));
		} else {
			throw new BizException(ErrorCodeConstant.ACTION_NAME_ERROR_CODE);
		}
		String query = "SELECT TPFDA.DATA_AUTH_ID FROM TC_FUNC TF,TR_POSE_FUNC_DATA_AUTH TPFDA "
				+ "WHERE TF.FUNC_ID = TPFDA.FUNC_ID "
				+ "AND TF.FUNC_CODE = '"
				+ actionName + "' AND TPFDA.POSE_ID = '" + poseId + "'";

		logger
				.debug("getDataAuthByPoseIdOrActionName SQL+++++++++++++++++++++++: "
						+ query);
		List<TcDataAuthPO> list = factory.select(query, null,
				new DAOCallback<TcDataAuthPO>() {
					public TcDataAuthPO wrapper(ResultSet rs, int idx) {
						TcDataAuthPO bean = new TcDataAuthPO();
						try {
							bean.setDataAuthId(rs.getLong("DATA_AUTH_ID"));
						} catch (SQLException e) {
							e.printStackTrace();
						}
						return bean;
					}
				});
		return list;
	}
	
	/**
	 * 根据职位ID和功能名得到用户当前的数据权限
	 * 
	 * @param poseId
	 *            用户当前的职位ID
	 * @param actionName
	 *            用户当前功能ID
	 * @return String 功能权限代码
	 * @throws BizException
	 */
	public static List<String> getDataAuthIdByPoseIdOrActionName(Long poseId,
			String actionName) throws BizException {
		if(actionName!=null&&actionName.indexOf("/") > -1 && actionName.indexOf(".") > -1) {
			actionName = actionName.substring(1, actionName.length());
			actionName = actionName.substring(actionName.indexOf("/"), actionName.indexOf("."));
		} else {
			throw new BizException(ErrorCodeConstant.ACTION_NAME_ERROR_CODE);
		}
		String query = "SELECT TPFDA.DATA_AUTH_ID FROM TC_FUNC TF,TR_POSE_FUNC_DATA_AUTH TPFDA "
				+ "WHERE TF.FUNC_ID = TPFDA.FUNC_ID "
				+ "AND TF.FUNC_CODE = '"
				+ actionName + "' AND TPFDA.POSE_ID = '" + poseId + "'";

		logger.debug("查询职位数据权限 SQL+++++++++++++++++++++++: "+ query);
		List<String> list = factory.select(query, null,
				new DAOCallback<String>() {
					public String wrapper(ResultSet rs, int idx) {
						String dataAuthId = null;
						try {
							dataAuthId = rs.getString("DATA_AUTH_ID");
							return dataAuthId;
						} catch (SQLException e) {
							throw new DAOException(e);
						}
					}
				});
		return list;
	}

}
