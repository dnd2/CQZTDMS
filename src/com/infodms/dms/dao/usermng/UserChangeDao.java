package com.infodms.dms.dao.usermng;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import org.apache.log4j.Logger;

import com.infodms.dms.common.Constant;
import com.infodms.dms.common.OrderUtil;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.po.TcPosePO;
import com.infodms.dms.po.TcUserPO;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.po3.POFactory;
import com.infoservice.po3.POFactoryBuilder;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;
import com.infoservice.po3.core.callback.DAOCallback;

@SuppressWarnings("unchecked")
public class UserChangeDao extends BaseDao {
	public static final Logger logger = Logger.getLogger(UserChangeDao.class);
	
	private static POFactory factory = POFactoryBuilder.getInstance();
	
	
	/**
	 * SGM系统用户查询
	 * 
	 * @param deptId
	 * @param empNum
	 * @param name
	 * @param curPage
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	public static PageResult<TcUserPO> sgmSysUserQuery(String orgId,Long companyId,String poseName,
			String acnt,String name, int curPage, int pageSize,
			String orderName, String da) throws Exception {
		String query = " SELECT DISTINCT TU.USER_ID,TU.ACNT,TU.PASSWORD,TU.NAME,TU.USER_STATUS FROM TC_USER TU,TC_POSE TP,"
				+ "TR_USER_POSE TUP WHERE TU.USER_ID = TUP.USER_ID AND TP.POSE_ID = TUP.POSE_ID AND TP.POSE_STATUS = " + Constant.STATUS_ENABLE + " AND "
				+ "TU.COMPANY_ID ='" + companyId + "' ";
		if (orgId != null && !orgId.equals("")) {
			query += "  AND TP.ORG_ID = '" + orgId + "'";
		}
		if (acnt != null && !acnt.equals("")) {
			query += "  AND upper(TU.ACNT) like '%" + acnt.toUpperCase() + "%'";
		}
		if (name != null && !name.equals("")) {
			query += "  AND upper(TU.NAME) like '%" + name.toUpperCase() + "%'";
		}
		
		if(!CommonUtils.isNullString(poseName)) {
			query += "AND TP.POSE_NAME LIKE '%" + poseName + "%'\n" ;
		}

		query = OrderUtil.addOrderBy(query, orderName, da);
		logger.debug("SQL: " + query);
		return getPoseByUserId(factory.pageQuery(query, null,
				new DAOCallback<TcUserPO>() {
					public TcUserPO wrapper(ResultSet rs, int idx) {
						TcUserPO bean = new TcUserPO();
						try {
							bean.setUserId(rs.getLong("USER_ID"));
							bean.setAcnt(rs.getString("ACNT"));
							bean.setName(rs.getString("NAME"));
							bean.setUserStatus(rs.getInt("USER_STATUS"));
							bean.setPassword(rs.getString("PASSWORD"));
						} catch (SQLException e) {
							e.printStackTrace();
						}
						return bean;
					}
				}, curPage, pageSize));
	}

	private static PageResult<TcUserPO> getPoseByUserId(PageResult<TcUserPO> rs) {
		if (rs.getRecords() != null) {
			List<TcUserPO> list = rs.getRecords(); // 取出用户
			String poseName = null;
			String sql = " SELECT TP.POSE_NAME FROM TC_POSE TP,TR_USER_POSE TUP WHERE TUP.POSE_ID = TP.POSE_ID AND TP.POSE_STATUS = " + Constant.STATUS_ENABLE + "";
			for (int i = 0; i < list.size() && list != null; i++) {
				poseName = "";
				TcUserPO user = list.get(i);
				List<TcPosePO> postList = factory.select(sql
						+ " AND TUP.USER_ID ='" + user.getUserId() + "'", null,
						new DAOCallback<TcPosePO>() {
							public TcPosePO wrapper(ResultSet rs, int idx) {
								TcPosePO bean = new TcPosePO();
								try {
									bean.setPoseName(rs.getString("POSE_NAME"));
								} catch (SQLException e) {
									e.printStackTrace();
								}
								return bean;
							}
						});
				for (int j = 0; j < postList.size(); j++) {
					poseName += postList.get(j).getPoseName() + ",";
				}
				if (!"".equals(poseName) && poseName.length() > 0) {
					poseName = poseName.substring(0, poseName.length() - 1);
				}
				user.setAddr(poseName);
				list.set(i, user);
			}
			rs.setRecords(list);
		}
		return rs;
	}

	
	/**
	 * SGM维护经销商用户查询
	 * 
	 * @param dealerId
	 * @param deptId
	 * @param acnt
	 * @param name
	 * @param curPage
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	public static PageResult<TcUserPO> sgmDrlSysUserQuery(String companyId,Long oemCompanyId,
			String deptId,String companyName, String acnt, String name, int curPage,
			int pageSize, String orderName, String da) throws Exception {
		String query = " SELECT DISTINCT TU.USER_ID,TU.PASSWORD,COM.COMPANY_SHORTNAME,TU.ACNT,TU.NAME,TU.USER_STATUS,TP.POSE_BUS_TYPE FROM TC_USER TU,TR_USER_POSE UP,TC_POSE TP, "
				+ " TM_COMPANY COM WHERE COM.COMPANY_ID = TU.COMPANY_ID AND  TU.USER_ID = UP.USER_ID(+) "
				+ " AND UP.POSE_ID = TP.POSE_ID(+) "
				+ " and COM.STATUS(+) = " + Constant.STATUS_ENABLE + " AND COM.COMPANY_TYPE <> " + Constant.COMPANY_TYPE_SGM;
		if (companyId != null && !"".equals(companyId)) {
			query += "  AND COM.COMPANY_ID ='" + companyId + "'";
		}
		if (oemCompanyId != null && !"".equals(oemCompanyId)) {
			query += "  AND COM.OEM_COMPANY_ID ='" + oemCompanyId + "'";
		}
		if (companyName != null && !"".equals(companyName)) {
			query += "  AND COM.COMPANY_NAME LIKE '%" + companyName + "%'";
		}
		if (deptId != null && !deptId.equals("")) {
			query += "  AND TP.ORG_ID = '" + deptId + "'";
		}
		if (acnt != null && !acnt.equals("")) {
			query += "  AND upper(TU.ACNT) like '%" + acnt.toUpperCase() + "%'";
		}
		if (name != null && !name.equals("")) {
			query += "  AND upper(TU.NAME) like '%" + name.toUpperCase() + "%'";
		}
		query += "ORDER BY TU.USER_STATUS, TP.POSE_BUS_TYPE, TU.ACNT DESC\n" ;


		query = OrderUtil.addOrderBy(query, orderName, da);
		logger.debug("SQL------------: " + query);
		return getPoseByUserId(factory.pageQuery(query, null,
				new DAOCallback<TcUserPO>() {
					public TcUserPO wrapper(ResultSet rs, int idx) {
						TcUserPO bean = new TcUserPO();
						try {
							bean.setUserId(rs.getLong("USER_ID"));
							bean.setAcnt(rs.getString("ACNT"));
							bean.setName(rs.getString("NAME"));
							bean.setUserStatus(rs.getInt("USER_STATUS"));
							bean.setPhone(rs.getString("COMPANY_SHORTNAME"));
							bean.setPassword(rs.getString("PASSWORD"));
						} catch (SQLException e) {
							e.printStackTrace();
						}
						return bean;
					}
				}, curPage, pageSize));
	}
	
	
	public static List<TcPosePO> getDRLPoseByCompanyId(Long companyId,
			String poseCode, String poseName, String poseIds)
			throws Exception {
		String query = " SELECT POSE_ID,POSE_CODE,POSE_NAME FROM TC_POSE WHERE POSE_TYPE = '"
				+ Constant.SYS_USER_DEALER
				+ "' AND POSE_STATUS = '"
				+ Constant.STATUS_ENABLE + "' AND COMPANY_ID = " + companyId ;
		if (poseCode != null && !poseCode.equals("")) {
			query += "  AND upper(POSE_CODE) LIKE '%" + poseCode.toUpperCase() + "%'";
		}
		if (poseName != null && !poseName.equals("")) {
			query += "  AND upper(POSE_NAME) like '%" + poseName.toUpperCase() + "%'";
		}
		if (poseIds != null && !poseIds.equals("")) {
			query += "  AND POSE_ID NOT IN ( " + poseIds + " ) ";
		}

		logger.debug("SQL+++++++++++++++++++++++: " + query);
		return factory.select(query, null, new DAOCallback<TcPosePO>() {
			public TcPosePO wrapper(ResultSet rs, int idx) {
				TcPosePO bean = new TcPosePO();
				try {
					bean.setPoseId(rs.getLong("POSE_ID"));
					bean.setPoseCode(rs.getString("POSE_CODE"));
					bean.setPoseName(rs.getString("POSE_NAME"));
				} catch (SQLException e) {
					e.printStackTrace();
				}
				return bean;
			}
		});
	}

	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		
		return null;
	}
	
	public String getFunName() {
		StackTraceElement stack[] = new Throwable().getStackTrace(); 
		StackTraceElement ste = stack[1];   
		//System.out.println(ste.getClassName() + "." + ste.getMethodName() + ste.getLineNumber()); 
		StringBuilder strBuilder = new StringBuilder();
		return strBuilder.append(ste.getClassName()).
			append(".").append(ste.getMethodName()).
			append(ste.getLineNumber()).
			toString();  
	}

}
