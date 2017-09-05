package com.infodms.dms.dao.sysusermng;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.TmUserInfoBean;
import com.infodms.dms.common.OrderUtil;
import com.infodms.dms.dao.sysposition.SysPositionDAO;
import com.infodms.dms.po.TcUserOnlinePO;
import com.infoservice.po3.POFactory;
import com.infoservice.po3.POFactoryBuilder;
import com.infoservice.po3.bean.PageResult;
import com.infoservice.po3.core.callback.DAOCallback;

public class SysUserInfoManagerDao {
	public static Logger logger = Logger.getLogger(SysPositionDAO.class);
	private static POFactory factory = POFactoryBuilder.getInstance();
	
	/**
	 * BY 用户ID,组织ID，公司ID 查询出指定用户的职务信息
	 * @param userId,orgId,companyId 用户ID: userId,组织ID:orgId,公司ID:companyId
	 * @return
	 */
	public static PageResult<TmUserInfoBean> findUserPositionInfo(Long userId,Long orgId,Long companyId, int curPage, int pageSize, String orderName, String da){
		
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT O.ORG_DESC,P.POSE_NAME ");
		sb.append(" FROM TR_USER_POSE U LEFT JOIN TC_POSE P ON U.POSE_ID = P.POSE_ID ");
		sb.append(" LEFT JOIN TM_ORG O ON P.ORG_ID = O.ORG_ID AND P.COMPANY_ID = O.COMPANY_ID ");
		sb.append(" WHERE 1=1 ");
		sb.append(" AND U.USER_ID = ? ");
		sb.append(" AND P.ORG_ID = ? ");
		sb.append(" AND P.COMPANY_ID = ? ");
		
		List<Object> params = new ArrayList<Object>();		
		params.add(userId);
		params.add(orgId);
		params.add(companyId);
		
		String sql = OrderUtil.addOrderBy(sb.toString(), orderName, da);
		return factory.pageQuery(sql, params, new DAOCallback<TmUserInfoBean>() {
			public TmUserInfoBean wrapper(ResultSet rs, int idx) {
				TmUserInfoBean bean = new TmUserInfoBean();
				try {
					bean.setOrgDesc(rs.getString("ORG_DESC"));
					bean.setPoseName(rs.getString("POSE_NAME"));	
				} catch (SQLException e) {
					e.printStackTrace();
				}
				return bean;
			}
		}, pageSize, curPage);		
	}
	
	
	/**
	 * BY 用户ID 查询出指定用户最后一次登陆时间
	 * @param userId 用户ID: userId
	 * @return
	 */
	public static TcUserOnlinePO getLastUserLoginDate(Long userId){
		
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT T.LOGIN_DATE FROM TC_USER_ONLINE T WHERE T.USER_ID = ? ORDER BY T.LOGIN_DATE DESC");
		
		List<Object> params = new ArrayList<Object>();		
		params.add(userId);
		
		List<TcUserOnlinePO> list = factory.select(sb.toString(), params,new DAOCallback<TcUserOnlinePO>() {
			public TcUserOnlinePO wrapper(ResultSet rs, int idx){
				TcUserOnlinePO po = new TcUserOnlinePO();
				try {
					po.setLoginDate(rs.getTimestamp("LOGIN_DATE"));
				} catch (SQLException e) {
					e.printStackTrace();
				}
				return po;
			}
		});
		
		if(list.size() > 0){
			return list.get(0);
		}else{
			return null;
		}
	}

	/**
	 * 查询出当前用户的快捷功能列表
	 * @param userId
	 * @param poseId
	 * @return
	 */
	public static List<TmUserInfoBean> findSystemFunctionNames(Long userId,Long poseId){
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT F.FUNC_ID,F.FUNC_NAME,Q.FUNC_ORDER ");
		sb.append(" FROM TM_QUICK_FUNC Q LEFT JOIN TC_FUNC F ON Q.FUNC_ID = F.FUNC_ID ");
		sb.append(" WHERE Q.USER_ID = ? ");
		sb.append(" AND Q.POSE_ID = ? ");
		sb.append(" ORDER BY Q.FUNC_ORDER ");
		
		List<Object> params = new ArrayList<Object>();
		params.add(userId);
		params.add(poseId);
		
		return factory.select(sb.toString(), params,new DAOCallback<TmUserInfoBean>() {
			public TmUserInfoBean wrapper(ResultSet rs, int idx){
				TmUserInfoBean bean = new TmUserInfoBean();
				try {
					bean.setFuncId(rs.getLong("FUNC_ID"));
					bean.setFuncName(rs.getString("FUNC_NAME"));
					bean.setFuncOrder(rs.getInt("FUNC_ORDER"));
				} catch (SQLException e) {
					e.printStackTrace();
				}
				return bean;
			}
		});
	}
	
	/**
	 * 根据功能ID查询出所有的功能菜单列表
	 * @param funcIds
	 * @return
	 */
	public static List<TmUserInfoBean> findSystemFunctionNamesByFuncIds(String funcIds){
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT T.FUNC_ID,T.FUNC_NAME FROM TC_FUNC T WHERE T.FUNC_ID IN (" + funcIds + ") ORDER BY T.FUNC_NAME");
		
		return factory.select(sb.toString(), null,new DAOCallback<TmUserInfoBean>() {
			public TmUserInfoBean wrapper(ResultSet rs, int idx){
				TmUserInfoBean bean = new TmUserInfoBean();
				try {
					bean.setFuncId(rs.getLong("FUNC_ID"));
					bean.setFuncName(rs.getString("FUNC_NAME"));
				} catch (SQLException e) {
					e.printStackTrace();
				}
				return bean;
			}
		});
	}
	
	/**
	 * 查询出当前登录用户所对应职务下的所有的系统功能菜单(子窗体 ：不包括已经设定为快捷菜单的功能)
	 * @param userId,poseId 用户ID: userId,职务ID : poseId
	 * @return
	 */
	public static PageResult<TmUserInfoBean> findShortcutKeyHistoryInfo(Long poseId, String funcName,String funcIds, int curPage, int pageSize, String orderName, String da){
		
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT P.FUNC_ID,F.FUNC_NAME FROM TR_POSE_FUNC P LEFT JOIN TC_FUNC F ON P.FUNC_ID = F.FUNC_ID ");
		sb.append(" WHERE 1=1 ");
		sb.append(" AND P.POSE_ID = ? ");
		if(null != funcIds && !"".equals(funcIds)){
			sb.append(" AND P.FUNC_ID <> ALL(" + funcIds + ") ");
		}	
		if(null != funcName && !"".equals(funcName)){
			sb.append(" AND F.FUNC_NAME LIKE '%" + funcName + "%'");
		}
		sb.append(" ORDER BY F.FUNC_NAME ");
		
		List<Object> params = new ArrayList<Object>();		
		params.add(poseId);
		
		String sql = OrderUtil.addOrderBy(sb.toString(), orderName, da);
		return factory.pageQuery(sql, params, new DAOCallback<TmUserInfoBean>() {
			public TmUserInfoBean wrapper(ResultSet rs, int idx) {
				TmUserInfoBean bean = new TmUserInfoBean();
				try {
					bean.setFuncId(rs.getLong("FUNC_ID"));
					bean.setFuncName(rs.getString("FUNC_NAME"));
				} catch (SQLException e) {
					e.printStackTrace();
				}
				return bean;
			}
		}, pageSize, curPage);		
	}
	
	/**
	 * BY 用户ID,职务ID 删除当前的快捷功能列表
	 * @param userId,poseId 用户ID: userId,职务ID : poseId
	 * @return
	 */
	public static void deleteShortcutKeyHistoryInfoByUserIdByPoseId(Long userId,Long poseId){
		String sql = "DELETE TM_QUICK_FUNC T WHERE T.USER_ID = ? AND T.POSE_ID = ?";
		
		List<Object> params = new ArrayList<Object>();		
		params.add(userId);
		params.add(poseId);
		
		factory.delete(sql, params);
	}
}
