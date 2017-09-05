/**********************************************************************
 * <pre>
 * FILE : UserMngDAO.java
 * CLASS : UserMngDAO
 * 
 * AUTHOR : ChenLiang
 *
 * FUNCTION : 系统用户维护(SGM)DAO.
 *
 *
 *======================================================================
 * CHANGE HISTORY LOG
 *----------------------------------------------------------------------
 * MOD. NO.| DATE     |    NAME    | REASON | CHANGE REQ.
 *----------------------------------------------------------------------
 *         |2009-09-03| ChenLiang  | Created |
 * DESCRIPTION:
 * </pre>
 ***********************************************************************/

package com.infodms.dms.dao.crm.sysUser;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.common.Constant;
import com.infodms.dms.common.OrderUtil;
import com.infodms.dms.common.materialManager.MaterialGroupManagerDao;
import com.infodms.dms.dao.claim.application.DealerKpDao;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.po.TcPosePO;
import com.infodms.dms.po.TcUserPO;
import com.infodms.dms.po.TmCompanyPO;
import com.infodms.dms.po.TmDealerPO;
import com.infodms.dms.po.TmOrgPO;
import com.infodms.dms.po.TmPtPartTypePO;
import com.infodms.dms.po.TmPtSupplierPO;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.po3.POFactory;
import com.infoservice.po3.POFactoryBuilder;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;
import com.infoservice.po3.core.callback.DAOCallback;

@SuppressWarnings("unchecked")
public class UserDAO extends BaseDao {
	public static Logger logger = Logger.getLogger(UserDAO.class);
	private static POFactory factory = POFactoryBuilder.getInstance();
	private static final UserDAO dao = new UserDAO();

	public static UserDAO getInstance() {
		return dao;
	}

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
	public static PageResult<TcUserPO> sgmSysUserQuery(String orgId, Long companyId, String acnt, String name, int curPage, int pageSize, String orderName, String da) throws Exception {
		String query = " SELECT DISTINCT TU.USER_ID,TU.ACNT,TU.NAME,TU.USER_STATUS FROM TC_USER TU,TC_POSE TP," + "TR_USER_POSE TUP WHERE TU.USER_ID = TUP.USER_ID AND TP.POSE_ID = TUP.POSE_ID AND " + "TU.COMPANY_ID ='" + companyId + "' ";
		if (orgId != null && !orgId.equals("")) {
			query += "  AND TP.ORG_ID = '" + orgId + "'";
		}
		if (acnt != null && !acnt.equals("")) {
			query += "  AND upper(TU.ACNT) like '%" + acnt.toUpperCase() + "%'";
		}
		if (name != null && !name.equals("")) {
			query += "  AND upper(TU.NAME) like '%" + name.toUpperCase() + "%'";
		}

		query = OrderUtil.addOrderBy(query, orderName, da);
		logger.debug("SQL: " + query);
		return getPoseByUserId(factory.pageQuery(query, null, new DAOCallback<TcUserPO>() {
			public TcUserPO wrapper(ResultSet rs, int idx) {
				TcUserPO bean = new TcUserPO();
				try {
					bean.setUserId(rs.getLong("USER_ID"));
					bean.setAcnt(rs.getString("ACNT"));
					bean.setName(rs.getString("NAME"));
					bean.setUserStatus(rs.getInt("USER_STATUS"));
				} catch (SQLException e) {
					e.printStackTrace();
				}
				return bean;
			}
		}, curPage, pageSize));
	}

	public static PageResult<Map<String, Object>> sgmSysUserQueryA(String orgId, Long companyId, String acnt, String name, int curPage, int pageSize, String orderName, String da) {
		StringBuffer sql = new StringBuffer("\n");

		sql.append(" SELECT DISTINCT TU.USER_ID,TU.ACNT,TU.NAME,TU.USER_STATUS, TCU.NAME UPDATE_NAME, TO_CHAR(TU.UPDATE_DATE, 'YYYY-MM-DD') UPDATE_DATE FROM TC_USER TU,TC_POSE TP," + "TR_USER_POSE TUP, TC_USER TCU WHERE TU.UPDATE_BY(+) = TCU.USER_ID AND TU.USER_ID = TUP.USER_ID(+)  and tu.user_status=10011001 "
		// AND TP.POSE_ID = TUP.POSE_ID AND
				+ " and TU.COMPANY_ID ='" + companyId + "' ");
		if (orgId != null && !orgId.equals("")) {
			sql.append("  AND TP.ORG_ID = '" + orgId + "'");
		}
		if (acnt != null && !acnt.equals("")) {
			sql.append("  AND upper(TU.ACNT) like '%" + acnt.toUpperCase() + "%'");
		}
		if (name != null && !name.equals("")) {
			sql.append("  AND upper(TU.NAME) like '%" + name.toUpperCase() + "%'");
		}

		String mySql = OrderUtil.addOrderBy(sql.toString(), orderName, da);

		return getPoseByUserIdA(dao.pageQuery(mySql, null, dao.getFunName(), curPage, pageSize));
	}

	private static PageResult<TcUserPO> getPoseByUserId(PageResult<TcUserPO> rs) {
		if (rs.getRecords() != null) {
			List<TcUserPO> list = rs.getRecords(); // 取出用户
			String poseName = null;
			String sql = " SELECT TP.POSE_NAME FROM TC_POSE TP,TR_USER_POSE TUP WHERE TUP.POSE_ID = TP.POSE_ID ";
			for (int i = 0; i < list.size() && list != null; i++) {
				poseName = "";
				TcUserPO user = list.get(i);
				List<TcPosePO> postList = factory.select(sql + " AND TUP.USER_ID ='" + user.getUserId() + "'", null, new DAOCallback<TcPosePO>() {
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

	private static PageResult<Map<String, Object>> getPoseByUserIdA(PageResult<Map<String, Object>> rs) {
		if (rs.getRecords() != null) {
			List<Map<String, Object>> list = rs.getRecords(); // 取出用户
			String poseName = null;
			String sql = " SELECT TP.POSE_NAME FROM TC_POSE TP,TR_USER_POSE TUP WHERE TUP.POSE_ID = TP.POSE_ID ";
			for (int i = 0; i < list.size() && list != null; i++) {
				poseName = "";
				Map<String, Object> user = list.get(i);
				List<TcPosePO> postList = factory.select(sql + " AND TUP.USER_ID ='" + user.get("USER_ID") + "'", null, new DAOCallback<TcPosePO>() {
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
				user.put("POSE_NAME", poseName);
				list.set(i, user);
			}
			rs.setRecords(list);
		}
		return rs;
	}

	/**
	 * SGM添加用户时职位查询
	 * 
	 * @param poseCode
	 * @param poseName
	 * @param curPage
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	public static List<TcPosePO> getSGMPoseByCompanyId(String poseCode, Long companyId, String poseName, String poseIds) throws Exception {
		String query = " SELECT POSE_ID,POSE_CODE,POSE_NAME FROM TC_POSE WHERE POSE_TYPE = '" + Constant.SYS_USER_SGM + "' AND POSE_STATUS = '" + Constant.STATUS_ENABLE + "' AND COMPANY_ID = '" + companyId + "' ";
		if (poseCode != null && !poseCode.equals("")) {
			query += "  AND POSE_CODE LIKE '%" + poseCode + "%'";
		}
		if (poseName != null && !poseName.equals("")) {
			query += "  AND POSE_NAME like '%" + poseName + "%'";
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

	/**
	 * SGM根据多个职位ID得到职位信息
	 * 
	 * @param poseIds
	 * @param curPage
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	public static List<TcPosePO> getSGMPoseByPoseIds(String poseIds, Long companyId) throws Exception {
		String query = " SELECT POSE_ID,POSE_CODE,POSE_NAME FROM TC_POSE WHERE POSE_TYPE = '" + Constant.SYS_USER_SGM + "' AND POSE_STATUS = '" + Constant.STATUS_ENABLE + "' and POSE_ID IN (" + poseIds + ") AND COMPANY_ID = '" + companyId + "' ";

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

	/**
	 * 经销商用户查询
	 * 
	 * @param dealerId
	 * @param deptId
	 * @param empNum
	 * @param name
	 * @param curPage
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	public static PageResult<TcUserPO> drlSysUserQuery(Long dealerId, String deptId, String empNum, String name, int curPage, int pageSize, String orderName, String da) throws Exception {
		String query = " SELECT DISTINCT TU.USER_ID,TU.EMP_NUM,TU.NAME,TU.USER_STATUS FROM TC_USER TU,TM_COMPANY TPC " + " WHERE TPC.COMPANY_ID = TU.COMPANY_ID AND TPC.company_stat = '" + Constant.STATUS_ENABLE + "' AND " + "TU.COMPANY_ID ='" + dealerId + "' ";
		if (deptId != null && !deptId.equals("")) {
			query += "  AND TPC.ORG_ID = '" + deptId + "'";
		}
		if (empNum != null && !empNum.equals("")) {
			query += "  AND upper(TU.EMP_NUM) like '%" + empNum.toUpperCase() + "%'";
		}
		if (name != null && !name.equals("")) {
			query += "  AND upper(TU.NAME) like '%" + name.toUpperCase() + "%'";
		}

		query = OrderUtil.addOrderBy(query, orderName, da);
		logger.debug("SQL+++++++++++++++++++++++: " + query);
		return getPoseByUserId(factory.pageQuery(query, null, new DAOCallback<TcUserPO>() {
			public TcUserPO wrapper(ResultSet rs, int idx) {
				TcUserPO bean = new TcUserPO();
				try {
					bean.setUserId(rs.getLong("USER_ID"));
					bean.setEmpNum(rs.getString("EMP_NUM"));
					bean.setName(rs.getString("NAME"));
					bean.setUserStatus(rs.getInt("USER_STATUS"));
				} catch (SQLException e) {
					e.printStackTrace();
				}
				return bean;
			}
		}, curPage, pageSize));
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
	public static PageResult<TcUserPO> sgmDrlSysUserQuery(String companyId, Long oemCompanyId, String deptId, String acnt, String name, String poseCode, int curPage, int pageSize, String orderName, String da) throws Exception {
		String query = " SELECT DISTINCT TU.USER_ID,COM.COMPANY_SHORTNAME,TU.ACNT,TU.NAME,TU.USER_STATUS,TP.POSE_BUS_TYPE FROM TC_USER TU,TR_USER_POSE UP,TC_POSE TP, " + " TM_COMPANY COM WHERE COM.COMPANY_ID = TU.COMPANY_ID AND  TU.USER_ID = UP.USER_ID(+) " + " AND UP.POSE_ID = TP.POSE_ID(+) " + " and COM.STATUS(+) = " + Constant.STATUS_ENABLE + " AND COM.COMPANY_TYPE <> "
				+ Constant.COMPANY_TYPE_SGM;
		if (companyId != null && !"".equals(companyId)) {
			query += "  AND COM.COMPANY_ID ='" + companyId + "'";
		}
		if (oemCompanyId != null && !"".equals(oemCompanyId)) {
			query += "  AND COM.OEM_COMPANY_ID ='" + oemCompanyId + "'";
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
		if (poseCode != null && !poseCode.equals("")) {
			query += "  AND upper(TP.POSE_CODE) like '%" + poseCode.toUpperCase() + "%'";
		}
		query += "ORDER BY TU.USER_STATUS, TP.POSE_BUS_TYPE, TU.ACNT DESC\n";

		query = OrderUtil.addOrderBy(query, orderName, da);
		logger.debug("SQL+++++++++++++++++++++++: " + query);
		return getPoseByUserId(factory.pageQuery(query, null, new DAOCallback<TcUserPO>() {
			public TcUserPO wrapper(ResultSet rs, int idx) {
				TcUserPO bean = new TcUserPO();
				try {
					bean.setUserId(rs.getLong("USER_ID"));
					bean.setAcnt(rs.getString("ACNT"));
					bean.setName(rs.getString("NAME"));
					bean.setUserStatus(rs.getInt("USER_STATUS"));
					bean.setPhone(rs.getString("COMPANY_SHORTNAME"));
				} catch (SQLException e) {
					e.printStackTrace();
				}
				return bean;
			}
		}, curPage, pageSize));
	}

	public static PageResult<Map<String, Object>> sgmDrlSysUserQueryA(String companyId, Long oemCompanyId, String deptId, String acnt, String name, String poseCode, int curPage, int pageSize, String orderName, String da) throws Exception {
		String query = " SELECT DISTINCT TU.USER_ID,COM.COMPANY_SHORTNAME,TU.ACNT,TU.NAME,TU.USER_STATUS,TP.POSE_BUS_TYPE, TCUS.NAME UPDATE_NAME, TO_CHAR(TU.UPDATE_DATE, 'YYYY-MM-DD') UPDATE_DATE FROM TC_USER TU,TR_USER_POSE UP,TC_POSE TP, " + " TM_COMPANY COM, TC_USER TCUS WHERE TU.UPDATE_BY = TCUS.USER_ID(+) AND COM.COMPANY_ID = TU.COMPANY_ID AND  TU.USER_ID = UP.USER_ID(+) "
				+ " AND UP.POSE_ID = TP.POSE_ID(+) " + " and COM.STATUS(+) = " + Constant.STATUS_ENABLE + " AND COM.COMPANY_TYPE <> " + Constant.COMPANY_TYPE_SGM;
		if (companyId != null && !"".equals(companyId)) {
			query += "  AND COM.COMPANY_ID ='" + companyId + "'";
		}
		//yin 注释的
//		if (oemCompanyId != null && !"".equals(oemCompanyId)) {
//			query += "  AND COM.OEM_COMPANY_ID ='" + oemCompanyId + "'";
//		}
		//end
		if (deptId != null && !deptId.equals("")) {
			query += "  AND TP.ORG_ID = '" + deptId + "'";
		}
		if (acnt != null && !acnt.equals("")) {
			query += "  AND upper(TU.ACNT) like '%" + acnt.toUpperCase() + "%'";
		}
		if (name != null && !name.equals("")) {
			query += "  AND upper(TU.NAME) like '%" + name.toUpperCase() + "%'";
		}
		if (poseCode != null && !poseCode.equals("")) {
			query += "  AND upper(TP.POSE_CODE) like '%" + poseCode.toUpperCase() + "%'";
		}
		query += "ORDER BY TU.USER_STATUS, TP.POSE_BUS_TYPE, TU.ACNT DESC\n";

		query = OrderUtil.addOrderBy(query, orderName, da);
		logger.debug("SQL+++++++++++++++++++++++: " + query);
		return getPoseByUserIdA(dao.pageQuery(query, null, dao.getFunName(), curPage, pageSize));
	}

	/**
	 * 经销商添加用户时职位查询
	 * 
	 * @param dealerId
	 * @param poseCode
	 * @param poseName
	 * @param curPage
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	public static List<TcPosePO> getDRLPoseByCompanyId(Long companyId, String poseCode, String poseName, String poseIds) throws Exception {
		String query = " SELECT POSE_ID,POSE_CODE,POSE_NAME FROM TC_POSE WHERE POSE_TYPE = '" + Constant.SYS_USER_DEALER + "' AND POSE_STATUS = '" + Constant.STATUS_ENABLE + "' AND COMPANY_ID = " + companyId;
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

	public static PageResult<TmDealerPO> getDRLByDeptId(String orgId, String dcode, String dsname, Long companyId, int curPage, int pageSize, String orderName, String da) throws Exception {
		List<TmOrgPO> list = new ArrayList<TmOrgPO>();
		List<TmOrgPO> zDept = getXjbm(orgId, companyId, list);
		String indept = "";
		if (zDept != null && zDept.size() > 0) {
			for (int i = 0; i < zDept.size(); i++) {
				indept += zDept.get(i).getOrgId() + ",";
			}
			indept = indept.substring(0, indept.length() - 1) + "," + orgId;
		}

		if ("".equals(indept)) {
			indept = String.valueOf(orgId);
		}

		logger.error("indept==================:  " + indept);
		StringBuffer query = new StringBuffer();
		// modify by xiayanpeng begin 经销商表修改DEALER_NAME
		query.append(" SELECT A.DEALER_CODE, A.DEALER_ID, A.DEALER_NAME DEALER_NAME FROM TM_DEALER a,TM_DEALER_ORG_RELATION REL ");
		// modify by xiayanpeng end
		query.append(" ,TM_ORG ORG where  A.DEALER_TYPE = ");
		query.append(Constant.ORG_TYPE_DEALER);
		query.append(" and A.STATUS = ");
		query.append(Constant.STATUS_ENABLE);
		query.append(" AND A.DEALER_ID = REL.DEALER_ID AND ORG.ORG_ID = REL.ORG_ID ");
		if (zDept != null && !zDept.equals("")) {
			query.append(" AND ORG.PARENT_ORG_ID IN (");
			query.append(indept);
			query.append(" ) ");
		}
		if (dcode != null && !dcode.equals("")) {
			query.append(" AND DEALER_CODE LIKE '%");
			query.append(dcode);
			query.append("%' ");
		}
		if (dsname != null && !dsname.equals("")) {
			query.append(" and DEALER_NAME like '%");
			query.append(dsname);
			query.append("%' ");
		}
		logger.debug("SQL+++++++++++++++++++++++: " + query);
		return factory.pageQuery(query.toString(), null, new DAOCallback<TmDealerPO>() {
			public TmDealerPO wrapper(ResultSet rs, int idx) {
				TmDealerPO bean = new TmDealerPO();
				try {
					bean.setDealerCode(rs.getString("DEALER_CODE"));
					bean.setDealerId(rs.getLong("DEALER_ID"));
					// modify by xiayanpeng begin 经销商表修改DEALER_NAME
					bean.setDealerName(rs.getString("DEALER_NAME"));
					// modify by xiayanpeng end
				} catch (SQLException e) {
					e.printStackTrace();
				}
				return bean;
			}
		}, curPage, pageSize);
	}

	/*
	 * public static PageResult<TmDealerPO> getAllDRLByDeptId(String
	 * orgId,String dcode,String dsname,Long companyId,String provinceId,String
	 * dealerClass,int curPage, int pageSize, String orderName, String da,String
	 * inputOrgId,Integer poseBusType,Long poseId, String isAllLevel) throws
	 * Exception { List<TmOrgPO> list = new ArrayList<TmOrgPO>(); //modified
	 * by andy.ten@tom.com begin //List<TmOrgPO> zDept =
	 * getXjbm(orgId,companyId, list); // if(zDept != null && zDept.size()>0) { //
	 * for (int i = 0; i < zDept.size(); i++) { // s_orgId +=
	 * zDept.get(i).getOrgId()+","; // } // s_orgId = s_orgId.substring(0,
	 * s_orgId.length()-1)+","+orgId; // } // // if("".equals(s_orgId)) { //
	 * s_orgId = String.valueOf(orgId); // }
	 * 
	 * StringBuffer query = new StringBuffer(); query.append(" SELECT
	 * A.DEALER_CODE, A.DEALER_ID, A.DEALER_NAME,A.DEALER_SHORTNAME FROM
	 * TM_DEALER a,TM_DEALER_ORG_RELATION REL "); query.append(" where A.STATUS =
	 * "); query.append(Constant.STATUS_ENABLE); query.append(" AND
	 * REL.DEALER_ID =\n"); query.append(" (SELECT TD.DEALER_ID\n");
	 * query.append(" FROM TM_DEALER TD\n"); query.append(" WHERE
	 * TD.DEALER_LEVEL = "+Constant.DEALER_LEVEL_01+"\n"); query.append(" START
	 * WITH TD.DEALER_ID = A.DEALER_ID\n"); query.append(" CONNECT BY PRIOR
	 * TD.PARENT_DEALER_D = TD.DEALER_ID)");
	 * 
	 * query.append(" AND (REL.DEALER_ID = A.DEALER_ID OR REL.DEALER_ID =
	 * A.PARENT_DEALER_D)"); query.append(" AND A.OEM_COMPANY_ID =
	 * "+companyId+"\n"); if (orgId != null && !orgId.equals("")) {
	 * query.append(" AND REL.ORG_ID IN ("); query.append(" SELECT ORG_ID FROM
	 * TM_ORG ORG WHERE ORG.STATUS = " + Constant.STATUS_ENABLE); query.append("
	 * START WITH ORG.ORG_ID = " + orgId); query.append(" CONNECT BY PRIOR
	 * ORG.ORG_ID = ORG.PARENT_ORG_ID) "); }else //modify by zjy
	 * 接收页面操作人自己输入的组织ID { if(inputOrgId != null &&
	 * !"".equals(inputOrgId)&&!"null".equals(inputOrgId)){ query.append(" AND
	 * REL.ORG_ID IN ("); query.append(" SELECT ORG_ID FROM TM_ORG ORG WHERE
	 * ORG.STATUS = " + Constant.STATUS_ENABLE); query.append(" START WITH
	 * ORG.ORG_ID = " + inputOrgId); query.append(" CONNECT BY PRIOR ORG.ORG_ID =
	 * ORG.PARENT_ORG_ID) "); } } if (dcode != null && !dcode.equals("")) {
	 * query.append(" AND A.DEALER_CODE LIKE '%"); query.append(dcode);
	 * query.append("%' "); } if (dsname != null && !dsname.equals("")) {
	 * query.append(" and A.DEALER_NAME like '%"); query.append(dsname);
	 * query.append("%' "); } if (provinceId != null && !"".equals(provinceId)) {
	 * query.append(" AND A.PROVINCE_ID = '"+ provinceId +"' \n"); } if
	 * (dealerClass != null && !"".equals(dealerClass)) { query.append(" AND
	 * A.DEALER_CLASS = '"+ dealerClass +"' \n"); } if
	 * (isAllLevel.equals("false")) { query.append(" AND A.DEALER_LEVEL = "+
	 * Constant.DEALER_LEVEL_01 +" \n"); } //modify by zhaojinyu 2010-06-12
	 * 经销商树中售后只查询售后的经销商，销售只查询销售的经销商。
	 * if(Constant.POSE_BUS_TYPE_WR==poseBusType||Constant.POSE_BUS_TYPE_DWR==poseBusType){
	 * query.append(" and A.DEALER_TYPE ="+Constant.DEALER_TYPE_DWR+" \n");
	 * }else
	 * if(Constant.POSE_BUS_TYPE_VS==poseBusType||Constant.POSE_BUS_TYPE_DVS==poseBusType) {
	 * query.append(" and A.DEALER_TYPE IN ("+Constant.DEALER_TYPE_DVS+",
	 * "+Constant.DEALER_TYPE_QYZDL+", "+Constant.DEALER_TYPE_JSZX+") \n"); }
	 * //end // ADD BY YUYONG 2010.8.30 增加业务范围过滤 String areaIds =
	 * MaterialGroupManagerDao.getAreaIdsByPoseId(poseId);
	 * 
	 * query.append(" AND EXISTS\n"); query.append("(SELECT TDBA.DEALER_ID\n");
	 * query.append(" FROM TM_DEALER_BUSINESS_AREA TDBA\n"); query.append("
	 * WHERE A.DEALER_ID = TDBA.DEALER_ID\n"); query.append(" AND TDBA.AREA_ID
	 * IN\n"); query.append(" ("+areaIds+"))");
	 * 
	 * //query.append(" AND A.DEALER_ID IN (SELECT TDBA.DEALER_ID FROM
	 * TM_DEALER_BUSINESS_AREA TDBA WHERE TDBA.AREA_ID IN ("+areaIds+")) \n");
	 * query.append(" ORDER BY A.DEALER_CODE,A.DEALER_NAME \n") ;
	 * logger.info("SQL: " + query); return factory.pageQuery(query.toString(),
	 * null, new DAOCallback<TmDealerPO>() { public TmDealerPO
	 * wrapper(ResultSet rs, int idx) { TmDealerPO bean = new TmDealerPO(); try {
	 * bean.setDealerCode(rs.getString("DEALER_CODE"));
	 * bean.setDealerId(rs.getLong("DEALER_ID"));
	 * bean.setDealerName(rs.getString("DEALER_NAME"));
	 * bean.setDealerShortname(rs.getString("DEALER_SHORTNAME")); } catch
	 * (SQLException e) { e.printStackTrace(); } return bean; } }, curPage,
	 * pageSize); }
	 */
	public static PageResult<TmDealerPO> getNowDownDRLByDeptId(String dealerIds, String orgId, String dcode, String dsname, Long companyId, String provinceId, String dealerClass, int curPage, int pageSize, String orderName, String da, String inputOrgId, Integer poseBusType, Long poseId, String isAllLevel, String isAllArea) throws Exception {

		StringBuffer sql = new StringBuffer();
		sql.append("SELECT A.DEALER_CODE, A.DEALER_ID, A.DEALER_NAME, A.DEALER_SHORTNAME\n");
		sql.append("  FROM TM_DEALER a\n");
		sql.append(" where A.STATUS = " + Constant.STATUS_ENABLE + "\n");
		sql.append("   AND A.OEM_COMPANY_ID = " + companyId + "\n");
		if (dcode != null && !dcode.equals("")) {
			sql.append("   AND A.DEALER_CODE LIKE '%");
			sql.append(dcode);
			sql.append("%' ");
		}
		if (dsname != null && !dsname.equals("")) {
			sql.append("   and A.DEALER_NAME like '%");
			sql.append(dsname);
			sql.append("%' ");
		}
		if (provinceId != null && !"".equals(provinceId)) {
			sql.append("   AND A.PROVINCE_ID = '" + provinceId + "' \n");
		}
		if (dealerClass != null && !"".equals(dealerClass)) {
			sql.append("   AND A.DEALER_CLASS = '" + dealerClass + "' \n");
		}
		// if (isAllLevel.toLowerCase().equals("false")) {
		// sql.append(" AND A.DEALER_LEVEL = "+ Constant.DEALER_LEVEL_01 +"
		// \n");
		// }
		sql.append("AND A.PARENT_DEALER_D IN(" + dealerIds + ")");
		// modify by zhaojinyu 2010-06-12 经销商树中售后只查询售后的经销商，销售只查询销售的经销商。
		if (Constant.POSE_BUS_TYPE_WR == poseBusType || Constant.POSE_BUS_TYPE_DWR == poseBusType) {
			sql.append("   and A.DEALER_TYPE =" + Constant.DEALER_TYPE_DWR + " \n");
		} else if (Constant.POSE_BUS_TYPE_VS == poseBusType || Constant.POSE_BUS_TYPE_DVS == poseBusType) {
			sql.append("   and A.DEALER_TYPE IN (" + Constant.DEALER_TYPE_DVS + ", " + Constant.DEALER_TYPE_QYZDL + ", " + Constant.DEALER_TYPE_JSZX + ") \n");
		}
		if (orgId != null && !orgId.equals("")) {
			sql.append("   and a.dealer_id in\n");
			sql.append("       (select td.dealer_id\n");
			sql.append("          from tm_dealer td\n");
			sql.append("         START WITH TD.DEALER_ID in\n");
			sql.append("                    (select tdor.dealer_id\n");
			sql.append("                       from tm_dealer_org_relation tdor\n");
			sql.append("                      where tdor.org_id in\n");
			sql.append("                            (SELECT ORG_ID\n");
			sql.append("                               FROM TM_ORG ORG\n");
			sql.append("                              WHERE ORG.STATUS = " + Constant.STATUS_ENABLE + "\n");
			sql.append("                              START WITH ORG.ORG_ID = " + orgId + "\n");
			sql.append("                             CONNECT BY PRIOR ORG.ORG_ID = ORG.PARENT_ORG_ID))\n");
			sql.append("        CONNECT BY PRIOR TD.DEALER_ID = td.parent_dealer_d)\n");
		} else {
			if (inputOrgId != null && !"".equals(inputOrgId) && !"null".equals(inputOrgId)) {
				sql.append("   and a.dealer_id in\n");
				sql.append("       (select td.dealer_id\n");
				sql.append("          from tm_dealer td\n");
				sql.append("         START WITH TD.DEALER_ID in\n");
				sql.append("                    (select tdor.dealer_id\n");
				sql.append("                       from tm_dealer_org_relation tdor\n");
				sql.append("                      where tdor.org_id in\n");
				sql.append("                            (SELECT ORG_ID\n");
				sql.append("                               FROM TM_ORG ORG\n");
				sql.append("                              WHERE ORG.STATUS = " + Constant.STATUS_ENABLE + "\n");
				sql.append("                              START WITH ORG.ORG_ID = " + inputOrgId + "\n");
				sql.append("                             CONNECT BY PRIOR ORG.ORG_ID = ORG.PARENT_ORG_ID))\n");
				sql.append("        CONNECT BY PRIOR TD.DEALER_ID = td.parent_dealer_d)\n");
			}
		}

		if (isAllArea.toLowerCase().equals("false")) {
			// ADD BY YUYONG 2010.8.30 增加业务范围过滤
			String areaIds = MaterialGroupManagerDao.getAreaIdsByPoseId(poseId);
			sql.append("   AND EXISTS\n");
			sql.append(" (SELECT TDBA.DEALER_ID\n");
			sql.append("          FROM TM_DEALER_BUSINESS_AREA TDBA\n");
			sql.append("         WHERE A.DEALER_ID = TDBA.DEALER_ID\n");
			sql.append("           AND TDBA.AREA_ID IN\n");
			sql.append("               (" + areaIds + "))\n");
			sql.append(" ORDER BY A.DEALER_CODE, A.DEALER_NAME");
		}

		logger.info("SQL: " + sql);
		return factory.pageQuery(sql.toString(), null, new DAOCallback<TmDealerPO>() {
			public TmDealerPO wrapper(ResultSet rs, int idx) {
				TmDealerPO bean = new TmDealerPO();
				try {
					bean.setDealerCode(rs.getString("DEALER_CODE"));
					bean.setDealerId(rs.getLong("DEALER_ID"));
					bean.setDealerName(rs.getString("DEALER_NAME"));
					bean.setDealerShortname(rs.getString("DEALER_SHORTNAME"));
				} catch (SQLException e) {
					e.printStackTrace();
				}
				return bean;
			}
		}, curPage, pageSize);
	}

	public static PageResult<TmDealerPO> getAllDRLByDeptId(String areaId, String isDealerType, String orgId, String dcode, String dsname, Long companyId, String provinceId, String dealerClass, String dealerType, int curPage, int pageSize, String orderName, String da, String inputOrgId, Integer poseBusType, Long poseId, String isAllLevel, String isAllArea, String isLogoutDealerClass)
			throws Exception {

		StringBuffer sql = new StringBuffer();
		sql.append("SELECT DISTINCT A.DEALER_CODE, A.DEALER_ID,A.DEALER_TYPE, A.DEALER_NAME, A.DEALER_SHORTNAME\n");
		sql.append("  FROM TM_DEALER a,TM_DEALER_BUSINESS_AREA TDBA\n");
		sql.append(" where A.STATUS = " + Constant.STATUS_ENABLE + "\n");
		sql.append("   AND A.OEM_COMPANY_ID = " + companyId + "\n");
		sql.append(" AND TDBA.DEALER_ID=A.DEALER_ID\n");
		if (areaId != null && !areaId.equals("") && !areaId.equals("null")) {
			sql.append("AND TDBA.AREA_ID IN (" + areaId + ")\n");
		}
		if (dcode != null && !dcode.equals("")) {
			sql.append("   AND A.DEALER_CODE LIKE '%");
			sql.append(dcode);
			sql.append("%' ");
		}
		if (dsname != null && !dsname.equals("")) {
			sql.append("   and A.DEALER_NAME like '%");
			sql.append(dsname);
			sql.append("%' ");
		}
		if (provinceId != null && !"".equals(provinceId)) {
			sql.append("   AND A.PROVINCE_ID = '" + provinceId + "' \n");
		}
		if (dealerClass != null && !"".equals(dealerClass)) {
			sql.append("   AND A.DEALER_CLASS = '" + dealerClass + "' \n");
		}
		if (dealerType != null && !"".equals(dealerType)) {
			sql.append("   AND A.DEALER_type = '" + dealerType + "' \n");
		}
		if (isAllLevel.toLowerCase().equals("false")) {
			sql.append("   AND A.DEALER_LEVEL = " + Constant.DEALER_LEVEL_01 + " \n");
		}

		// add by hxy 2012-04-23 新增是否为退网类经销商评级
		if (isLogoutDealerClass != null && !"".equals(isLogoutDealerClass)) {
			sql.append("	AND A.DEALER_CLASS <> 11451013\n");
		}

		// modify by zhaojinyu 2010-06-12 经销商树中售后只查询售后的经销商，销售只查询销售的经销商。
		if (isDealerType.equals("null")) {
			isDealerType = "";
		}
		if (isDealerType.equals("")) {
			if (Constant.POSE_BUS_TYPE_WR == poseBusType || Constant.POSE_BUS_TYPE_DWR == poseBusType) {
				sql.append("   and A.DEALER_TYPE =" + Constant.DEALER_TYPE_DWR + " \n");
			} else if (Constant.POSE_BUS_TYPE_VS == poseBusType || Constant.POSE_BUS_TYPE_DVS == poseBusType) {
				sql.append("   and A.DEALER_TYPE IN (" + Constant.DEALER_TYPE_DVS + ", " + Constant.DEALER_TYPE_QYZDL + ", " + Constant.DEALER_TYPE_JSZX + ") \n");
			}
		}
		if (!isDealerType.equals("null") && isDealerType != "") {
			sql.append("and A.DEALER_TYPE IN (" + isDealerType + ")");
		}
		if (orgId != null && !orgId.equals("")) {
			sql.append("   and a.dealer_id in\n");
			sql.append("       (select td.dealer_id\n");
			sql.append("          from tm_dealer td\n");
			sql.append("         START WITH TD.DEALER_ID in\n");
			sql.append("                    (select tdor.dealer_id\n");
			sql.append("                       from tm_dealer_org_relation tdor\n");
			sql.append("                      where tdor.org_id in\n");
			sql.append("                            (SELECT ORG_ID\n");
			sql.append("                               FROM TM_ORG ORG\n");
			sql.append("                              WHERE ORG.STATUS = " + Constant.STATUS_ENABLE + "\n");
			sql.append("                              START WITH ORG.ORG_ID = " + orgId + "\n");
			sql.append("                             CONNECT BY PRIOR ORG.ORG_ID = ORG.PARENT_ORG_ID))\n");
			sql.append("        CONNECT BY PRIOR TD.DEALER_ID = td.parent_dealer_d)\n");
		} else {
			if (inputOrgId != null && !"".equals(inputOrgId) && !"null".equals(inputOrgId)) {
				sql.append("   and a.dealer_id in\n");
				sql.append("       (select td.dealer_id\n");
				sql.append("          from tm_dealer td\n");
				sql.append("         START WITH TD.DEALER_ID in\n");
				sql.append("                    (select tdor.dealer_id\n");
				sql.append("                       from tm_dealer_org_relation tdor\n");
				sql.append("                      where tdor.org_id in\n");
				sql.append("                            (SELECT ORG_ID\n");
				sql.append("                               FROM TM_ORG ORG\n");
				sql.append("                              WHERE ORG.STATUS = " + Constant.STATUS_ENABLE + "\n");
				sql.append("                              START WITH ORG.ORG_ID = " + inputOrgId + "\n");
				sql.append("                             CONNECT BY PRIOR ORG.ORG_ID = ORG.PARENT_ORG_ID))\n");
				sql.append("        CONNECT BY PRIOR TD.DEALER_ID = td.parent_dealer_d)\n");
			}
		}

		if (isAllArea.toLowerCase().equals("false")) {
			// ADD BY YUYONG 2010.8.30 增加业务范围过滤
			String areaIds = MaterialGroupManagerDao.getAreaIdsByPoseId(poseId);
			sql.append("   AND EXISTS\n");
			sql.append(" (SELECT TDBA.DEALER_ID\n");
			sql.append("          FROM TM_DEALER_BUSINESS_AREA TDBA\n");
			sql.append("         WHERE A.DEALER_ID = TDBA.DEALER_ID\n");
			sql.append("           AND TDBA.AREA_ID IN\n");
			sql.append("               (" + areaIds + "))\n");
			sql.append(" ORDER BY A.DEALER_CODE, A.DEALER_NAME");
		}

		logger.info("SQL: " + sql);
		return factory.pageQuery(sql.toString(), null, new DAOCallback<TmDealerPO>() {
			public TmDealerPO wrapper(ResultSet rs, int idx) {
				TmDealerPO bean = new TmDealerPO();
				try {
					bean.setDealerCode(rs.getString("DEALER_CODE"));
					bean.setDealerId(rs.getLong("DEALER_ID"));
					bean.setDealerName(rs.getString("DEALER_NAME"));
					bean.setDealerShortname(rs.getString("DEALER_SHORTNAME"));
					bean.setDealerType(rs.getInt("DEALER_TYPE"));
				} catch (SQLException e) {
					e.printStackTrace();
				}
				return bean;
			}
		}, curPage, pageSize);
	}

	public static PageResult<TmDealerPO> getAllDRLByDeptId2(String orgId, String dcode, String dsname, int curPage, int pageSize, String isAllLevel) throws Exception {

		StringBuffer sql = new StringBuffer();

		sql.append("SELECT A.DEALER_CODE,\n");
		sql.append("       A.DEALER_ID,\n");
		sql.append("       A.DEALER_TYPE,\n");
		sql.append("       A.DEALER_NAME,\n");
		sql.append("       A.DEALER_SHORTNAME\n");
		sql.append("  FROM VW_ORG_DEALER A\n");
		sql.append(" WHERE 1 = 1\n");
		sql.append("   AND (A.ROOT_DEALER_ID = " + orgId + " OR A.PQ_ORG_ID = " + orgId + " OR A.ROOT_ORG_ID = " + orgId + " OR A.COMPANY_ID = " + orgId + ")");

		if (dcode != null && !dcode.equals("")) {
			sql.append("   AND A.DEALER_CODE LIKE '%");
			sql.append(dcode);
			sql.append("%' ");
		}
		if (dsname != null && !dsname.equals("")) {
			sql.append("   and A.DEALER_NAME like '%");
			sql.append(dsname);
			sql.append("%' ");
		}

		if (isAllLevel != null && isAllLevel.equals(Constant.DEALER_LEVEL_01.toString())) {
			sql.append("   AND A.DEALER_LEVEL = " + Constant.DEALER_LEVEL_01 + " \n");
		}
		if (isAllLevel != null && isAllLevel.equals(Constant.DEALER_LEVEL_02.toString())) {
			sql.append("   AND A.DEALER_LEVEL = " + Constant.DEALER_LEVEL_02 + " \n");
		}

		logger.info("SQL: " + sql);
		return factory.pageQuery(sql.toString(), null, new DAOCallback<TmDealerPO>() {
			public TmDealerPO wrapper(ResultSet rs, int idx) {
				TmDealerPO bean = new TmDealerPO();
				try {
					bean.setDealerCode(rs.getString("DEALER_CODE"));
					bean.setDealerId(rs.getLong("DEALER_ID"));
					bean.setDealerName(rs.getString("DEALER_NAME"));
					bean.setDealerShortname(rs.getString("DEALER_SHORTNAME"));
					bean.setDealerType(rs.getInt("DEALER_TYPE"));
				} catch (SQLException e) {
					e.printStackTrace();
				}
				return bean;
			}
		}, curPage, pageSize);
	}

	public static PageResult<TmDealerPO> getAllDRLByDeptId3(String areaId, String isDealerType, String orgId, String dcode, String dsname, Long companyId, String provinceId, String dealerClass, String dealerType, int curPage, int pageSize, String orderName, String da, String inputOrgId, Integer poseBusType, Long poseId, String isAllLevel, String isAllArea, String isLogoutDealerClass)
			throws Exception {

		StringBuffer sql = new StringBuffer();
		sql.append("SELECT DISTINCT A.DEALER_CODE, A.DEALER_ID,A.DEALER_TYPE, A.DEALER_NAME, A.DEALER_SHORTNAME\n");
		sql.append("  FROM TM_DEALER a,TM_DEALER_BUSINESS_AREA TDBA\n");
		sql.append(" where A.STATUS = " + Constant.STATUS_ENABLE + "\n");
		sql.append("   AND A.OEM_COMPANY_ID = " + companyId + "\n");
		sql.append(" AND TDBA.DEALER_ID=A.DEALER_ID\n");
		if (areaId != null && !areaId.equals("") && !areaId.equals("null")) {
			sql.append("AND TDBA.AREA_ID IN (" + areaId + ")\n");
		}
		if (dcode != null && !dcode.equals("")) {
			sql.append("   AND A.DEALER_CODE LIKE '%");
			sql.append(dcode);
			sql.append("%' ");
		}
		if (dsname != null && !dsname.equals("")) {
			sql.append("   and A.DEALER_NAME like '%");
			sql.append(dsname);
			sql.append("%' ");
		}
		if (provinceId != null && !"".equals(provinceId)) {
			sql.append("   AND A.PROVINCE_ID = '" + provinceId + "' \n");
		}
		if (dealerClass != null && !"".equals(dealerClass)) {
			sql.append("   AND A.DEALER_CLASS = '" + dealerClass + "' \n");
		}
		if (dealerType != null && !"".equals(dealerType)) {
			sql.append("   AND A.DEALER_type = '" + dealerType + "' \n");
		}
		if (isAllLevel.toLowerCase().equals("false")) {
			sql.append("   AND A.DEALER_LEVEL = " + Constant.DEALER_LEVEL_01 + " \n");
		}

		// add by hxy 2012-04-23 新增是否为退网类经销商评级
		if (isLogoutDealerClass != null && !"".equals(isLogoutDealerClass)) {
			sql.append("	AND A.DEALER_CLASS <> 11451013\n");
		}

		// modify by zhaojinyu 2010-06-12 经销商树中售后只查询售后的经销商，销售只查询销售的经销商。
		if (isDealerType.equals("null")) {
			isDealerType = "";
		}
		if (isDealerType.equals("")) {
			if (Constant.POSE_BUS_TYPE_WR == poseBusType || Constant.POSE_BUS_TYPE_DWR == poseBusType) {
				sql.append("   and A.DEALER_TYPE =" + Constant.DEALER_TYPE_DWR + " \n");
			} else if (Constant.POSE_BUS_TYPE_VS == poseBusType || Constant.POSE_BUS_TYPE_DVS == poseBusType) {
				sql.append("   and A.DEALER_TYPE IN (" + Constant.DEALER_TYPE_DVS + ", " + Constant.DEALER_TYPE_QYZDL + ", " + Constant.DEALER_TYPE_JSZX + ") \n");
			}
		}
		if (!isDealerType.equals("null") && isDealerType != "") {
			sql.append("and A.DEALER_TYPE IN (" + isDealerType + ")");
		}
		if (orgId != null && !orgId.equals("")) {
			sql.append("   and a.dealer_id in\n");
			sql.append("       (select td.dealer_id\n");
			sql.append("          from tm_dealer td\n");
			sql.append("         START WITH TD.DEALER_ID in\n");
			sql.append("                    (select tdor.dealer_id\n");
			sql.append("                       from tm_dealer_org_relation tdor\n");
			sql.append("                      where tdor.org_id in\n");
			sql.append("                            (SELECT ORG_ID\n");
			sql.append("                               FROM TM_ORG ORG\n");
			sql.append("                              WHERE ORG.STATUS = " + Constant.STATUS_ENABLE + "\n");
			sql.append("                              START WITH ORG.ORG_ID = " + orgId + "\n");
			sql.append("                             CONNECT BY PRIOR ORG.ORG_ID = ORG.PARENT_ORG_ID))\n");
			sql.append("        CONNECT BY PRIOR TD.DEALER_ID = td.parent_dealer_d)\n");
		} else {
			if (inputOrgId != null && !"".equals(inputOrgId) && !"null".equals(inputOrgId)) {
				sql.append("   and a.dealer_id in\n");
				sql.append("       (select td.dealer_id\n");
				sql.append("          from tm_dealer td\n");
				sql.append("         START WITH TD.DEALER_ID in\n");
				sql.append("                    (select tdor.dealer_id\n");
				sql.append("                       from tm_dealer_org_relation tdor\n");
				sql.append("                      where tdor.org_id in\n");
				sql.append("                            (SELECT ORG_ID\n");
				sql.append("                               FROM TM_ORG ORG\n");
				sql.append("                              WHERE ORG.STATUS = " + Constant.STATUS_ENABLE + "\n");
				sql.append("                              START WITH ORG.ORG_ID = " + inputOrgId + "\n");
				sql.append("                             CONNECT BY PRIOR ORG.ORG_ID = ORG.PARENT_ORG_ID))\n");
				sql.append("        CONNECT BY PRIOR TD.DEALER_ID = td.parent_dealer_d)\n");
			}
		}
		sql.append("and a.dealer_level=" + Constant.DEALER_LEVEL_01 + "\n");
		if (isAllArea.toLowerCase().equals("false")) {
			// ADD BY YUYONG 2010.8.30 增加业务范围过滤
			String areaIds = MaterialGroupManagerDao.getAreaIdsByPoseId(poseId);
			sql.append("   AND EXISTS\n");
			sql.append(" (SELECT TDBA.DEALER_ID\n");
			sql.append("          FROM TM_DEALER_BUSINESS_AREA TDBA\n");
			sql.append("         WHERE A.DEALER_ID = TDBA.DEALER_ID\n");
			sql.append("           AND TDBA.AREA_ID IN\n");
			sql.append("               (" + areaIds + "))\n");
			sql.append(" ORDER BY A.DEALER_CODE, A.DEALER_NAME");
		}

		logger.info("SQL: " + sql);
		return factory.pageQuery(sql.toString(), null, new DAOCallback<TmDealerPO>() {
			public TmDealerPO wrapper(ResultSet rs, int idx) {
				TmDealerPO bean = new TmDealerPO();
				try {
					bean.setDealerCode(rs.getString("DEALER_CODE"));
					bean.setDealerId(rs.getLong("DEALER_ID"));
					bean.setDealerName(rs.getString("DEALER_NAME"));
					bean.setDealerShortname(rs.getString("DEALER_SHORTNAME"));
					bean.setDealerType(rs.getInt("DEALER_TYPE"));
				} catch (SQLException e) {
					e.printStackTrace();
				}
				return bean;
			}
		}, curPage, pageSize);
	}

	/**
	 * 一级经销商二级经销商
	 * 
	 * @param areaId
	 * @param isDealerType
	 * @param orgId
	 * @param dcode
	 * @param dsname
	 * @param companyId
	 * @param provinceId
	 * @param dealerClass
	 * @param dealerType
	 * @param curPage
	 * @param pageSize
	 * @param orderName
	 * @param da
	 * @param inputOrgId
	 * @param poseBusType
	 * @param poseId
	 * @param isAllLevel
	 * @param isAllArea
	 * @param isLogoutDealerClass
	 * @return
	 * @throws Exception
	 */
	public static PageResult<TmDealerPO> getAllDRLByDeptId4(String areaId, String isDealerType, String orgId, String dcode, String dsname, Long companyId, String provinceId, String dealerClass, String dealerType, int curPage, int pageSize, String orderName, String da, String inputOrgId, Integer poseBusType, Long poseId, String isAllLevel, String isAllArea, String isLogoutDealerClass)
			throws Exception {

		StringBuffer sql = new StringBuffer();
		sql.append("SELECT DISTINCT A.DEALER_CODE, A.DEALER_ID,A.DEALER_TYPE, A.DEALER_NAME, A.DEALER_SHORTNAME\n");
		sql.append("  FROM TM_DEALER a,TM_DEALER_BUSINESS_AREA TDBA\n");
		sql.append(" where A.STATUS = " + Constant.STATUS_ENABLE + "\n");
		sql.append("   AND A.OEM_COMPANY_ID = " + companyId + "\n");
		sql.append(" AND TDBA.DEALER_ID=A.DEALER_ID\n");
		if (areaId != null && !areaId.equals("") && !areaId.equals("null")) {
			sql.append("AND TDBA.AREA_ID IN (" + areaId + ")\n");
		}
		if (dcode != null && !dcode.equals("")) {
			sql.append("   AND A.DEALER_CODE LIKE '%");
			sql.append(dcode);
			sql.append("%' ");
		}
		if (dsname != null && !dsname.equals("")) {
			sql.append("   and A.DEALER_NAME like '%");
			sql.append(dsname);
			sql.append("%' ");
		}
		if (provinceId != null && !"".equals(provinceId)) {
			sql.append("   AND A.PROVINCE_ID = '" + provinceId + "' \n");
		}
		if (dealerClass != null && !"".equals(dealerClass)) {
			sql.append("   AND A.DEALER_CLASS = '" + dealerClass + "' \n");
		}
		if (dealerType != null && !"".equals(dealerType)) {
			sql.append("   AND A.DEALER_type = '" + dealerType + "' \n");
		}
		// if (isAllLevel.toLowerCase().equals("false")) {
		// sql.append(" AND A.DEALER_LEVEL = "+ Constant.DEALER_LEVEL_01 +"
		// \n");
		// }

		// add by hxy 2012-04-23 新增是否为退网类经销商评级
		if (isLogoutDealerClass != null && !"".equals(isLogoutDealerClass)) {
			sql.append("	AND A.DEALER_CLASS <> 11451013\n");
		}

		// modify by zhaojinyu 2010-06-12 经销商树中售后只查询售后的经销商，销售只查询销售的经销商。
		if (isDealerType.equals("null")) {
			isDealerType = "";
		}
		if (isDealerType.equals("")) {
			if (Constant.POSE_BUS_TYPE_WR == poseBusType || Constant.POSE_BUS_TYPE_DWR == poseBusType) {
				sql.append("   and A.DEALER_TYPE =" + Constant.DEALER_TYPE_DWR + " \n");
			} else if (Constant.POSE_BUS_TYPE_VS == poseBusType || Constant.POSE_BUS_TYPE_DVS == poseBusType) {
				sql.append("   and A.DEALER_TYPE IN (" + Constant.DEALER_TYPE_DVS + ", " + Constant.DEALER_TYPE_QYZDL + ", " + Constant.DEALER_TYPE_JSZX + ") \n");
			}
		}
		if (!isDealerType.equals("null") && isDealerType != "") {
			sql.append("and A.DEALER_TYPE IN (" + isDealerType + ")");
		}
		if (orgId != null && !orgId.equals("")) {
			sql.append("   and a.dealer_id in\n");
			sql.append("       (select td.dealer_id\n");
			sql.append("          from tm_dealer td\n");
			sql.append("         START WITH TD.DEALER_ID in\n");
			sql.append("                    (select tdor.dealer_id\n");
			sql.append("                       from tm_dealer_org_relation tdor\n");
			sql.append("                      where tdor.org_id in\n");
			sql.append("                            (SELECT ORG_ID\n");
			sql.append("                               FROM TM_ORG ORG\n");
			sql.append("                              WHERE ORG.STATUS = " + Constant.STATUS_ENABLE + "\n");
			sql.append("                              START WITH ORG.ORG_ID = " + orgId + "\n");
			sql.append("                             CONNECT BY PRIOR ORG.ORG_ID = ORG.PARENT_ORG_ID))\n");
			sql.append("        CONNECT BY PRIOR TD.DEALER_ID = td.parent_dealer_d)\n");
		} else {
			if (inputOrgId != null && !"".equals(inputOrgId) && !"null".equals(inputOrgId)) {
				sql.append("   and a.dealer_id in\n");
				sql.append("(SELECT d.DEALER_ID FROM TM_DEALER d WHERE d.STATUS = 10011001 START WITH d.DEALER_ORG_ID =  " + inputOrgId + " CONNECT BY PRIOR d.DEALER_ID = d.PARENT_DEALER_D)");
			}
		}
		// sql.append("and a.dealer_level="+Constant.DEALER_LEVEL_01+"\n");
		if (isAllArea.toLowerCase().equals("false")) {
			// ADD BY YUYONG 2010.8.30 增加业务范围过滤
			String areaIds = MaterialGroupManagerDao.getAreaIdsByPoseId(poseId);
			sql.append("   AND EXISTS\n");
			sql.append(" (SELECT TDBA.DEALER_ID\n");
			sql.append("          FROM TM_DEALER_BUSINESS_AREA TDBA\n");
			sql.append("         WHERE A.DEALER_ID = TDBA.DEALER_ID\n");
			sql.append("           AND TDBA.AREA_ID IN\n");
			sql.append("               (" + areaIds + "))\n");
			sql.append(" ORDER BY A.DEALER_CODE, A.DEALER_NAME");
		}

		logger.info("SQL: " + sql);
		return factory.pageQuery(sql.toString(), null, new DAOCallback<TmDealerPO>() {
			public TmDealerPO wrapper(ResultSet rs, int idx) {
				TmDealerPO bean = new TmDealerPO();
				try {
					bean.setDealerCode(rs.getString("DEALER_CODE"));
					bean.setDealerId(rs.getLong("DEALER_ID"));
					bean.setDealerName(rs.getString("DEALER_NAME"));
					bean.setDealerShortname(rs.getString("DEALER_SHORTNAME"));
					bean.setDealerType(rs.getInt("DEALER_TYPE"));
				} catch (SQLException e) {
					e.printStackTrace();
				}
				return bean;
			}
		}, curPage, pageSize);
	}

	// zhumingwei 2011-11-22
	public static PageResult<TmDealerPO> getAllDRLByDeptIdCon(String userId, String areaId, String isDealerType, String orgId, String dcode, String dsname, Long companyId, String provinceId, String dealerClass, int curPage, int pageSize, String orderName, String da, String inputOrgId, Integer poseBusType, Long poseId, String isAllLevel, String isAllArea) throws Exception {

		StringBuffer sql = new StringBuffer();

		TcPosePO pPo = new TcPosePO();
		pPo.setPoseId(poseId);
		TcPosePO pPpValue = (TcPosePO) factory.select(pPo).get(0);
		TmOrgPO po = new TmOrgPO();
		po.setOrgId(pPpValue.getOrgId());
		TmOrgPO poValue = (TmOrgPO) factory.select(po).get(0);
		if (poValue.getDutyType() == 10431001) {
			sql.append("select d.DEALER_CODE,d.dealer_id,d.dealer_type,d.dealer_name,t.DEALER_SHORTNAME from vw_org_dealer d,tm_dealer t where t.dealer_id=d.dealer_id\n");
		} else {
			sql.append("select d.DEALER_CODE,d.dealer_id,d.dealer_type,d.dealer_name,t.DEALER_SHORTNAME from vw_org_dealer d,tm_dealer t where t.dealer_id=d.dealer_id and d.root_org_id=(select p.org_id from tc_pose p where p.pose_id=" + poseId + ")\n");
		}
		System.out.println(sql.toString());
		return factory.pageQuery(sql.toString(), null, new DAOCallback<TmDealerPO>() {
			public TmDealerPO wrapper(ResultSet rs, int idx) {
				TmDealerPO bean = new TmDealerPO();
				try {
					bean.setDealerCode(rs.getString("DEALER_CODE"));
					bean.setDealerId(rs.getLong("DEALER_ID"));
					bean.setDealerName(rs.getString("DEALER_NAME"));
					bean.setDealerShortname(rs.getString("DEALER_SHORTNAME"));
					bean.setDealerType(rs.getInt("DEALER_TYPE"));
				} catch (SQLException e) {
					e.printStackTrace();
				}
				return bean;
			}
		}, curPage, pageSize);
	}

	// zhumingwei 2011-11-22
	public static PageResult<TmDealerPO> getAllDRLByDeptIdZWCon(String userId, String areaId, String isDealerType, String orgId, String dcode, String dsname, Long companyId, String provinceId, String dealerClass, int curPage, int pageSize, String orderName, String da, String inputOrgId, Integer poseBusType, String dealerId, String isAllLevel, String isAllArea) throws Exception {
		DealerKpDao dao = new DealerKpDao();
		StringBuffer sql = new StringBuffer();

		StringBuffer sql1 = new StringBuffer();
		sql1.append("select d.dealer_level from vw_org_dealer d where d.dealer_id in(" + dealerId + ")");
		List<Map<String, Object>> list = dao.pageQuery(sql1.toString(), null, dao.getFunName());

		Long dealerLevel = ((BigDecimal) list.get(0).get("DEALER_LEVEL")).longValue();

		if (dealerLevel == 10851001) {
			System.out.println("aaaaaaaaaaaa");
			sql.append("select d.DEALER_CODE,d.dealer_id,d.dealer_type,d.dealer_name,t.DEALER_SHORTNAME from vw_org_dealer d,tm_dealer t where t.dealer_id=d.dealer_id and d.root_dealer_id in(" + dealerId + ")\n");
		} else {
			sql.append("select d.DEALER_CODE,d.dealer_id,d.dealer_type,d.dealer_name,t.DEALER_SHORTNAME from vw_org_dealer d,tm_dealer t where t.dealer_id=d.dealer_id and d.dealer_id in (" + dealerId + ")\n");
		}
		System.out.println(sql.toString());
		return factory.pageQuery(sql.toString(), null, new DAOCallback<TmDealerPO>() {
			public TmDealerPO wrapper(ResultSet rs, int idx) {
				TmDealerPO bean = new TmDealerPO();
				try {
					bean.setDealerCode(rs.getString("DEALER_CODE"));
					bean.setDealerId(rs.getLong("DEALER_ID"));
					bean.setDealerName(rs.getString("DEALER_NAME"));
					bean.setDealerShortname(rs.getString("DEALER_SHORTNAME"));
					bean.setDealerType(rs.getInt("DEALER_TYPE"));
				} catch (SQLException e) {
					e.printStackTrace();
				}
				return bean;
			}
		}, curPage, pageSize);
	}

	public PageResult<TmDealerPO> getAllDRLByDeptIdReg(String regCode, String areaId, String isDealerType, String orgId, String dcode, String dsname, Long companyId, String provinceId, String dealerClass, int curPage, int pageSize, String orderName, String da, String inputOrgId, Integer poseBusType, Long poseId, String isAllLevel, String isAllArea) throws Exception {

		StringBuffer sql = new StringBuffer();
		sql.append("SELECT DISTINCT A.DEALER_CODE, A.DEALER_ID,A.DEALER_TYPE, A.DEALER_NAME, A.DEALER_SHORTNAME\n");
		sql.append("  FROM TM_DEALER a,TM_DEALER_BUSINESS_AREA TDBA\n");
		sql.append(" where A.STATUS = " + Constant.STATUS_ENABLE + "\n");
		sql.append("   AND A.OEM_COMPANY_ID = " + companyId + "\n");
		sql.append(" AND TDBA.DEALER_ID=A.DEALER_ID\n");

		if (regCode != null && !regCode.equals("") && !regCode.equals("null")) {
			sql.append("AND A.province_id IN (" + regCode + ")\n");
		}
		if (areaId != null && !areaId.equals("") && !areaId.equals("null")) {
			sql.append("AND TDBA.AREA_ID IN (" + areaId + ")\n");
		}
		if (dcode != null && !dcode.equals("")) {
			sql.append("   AND A.DEALER_CODE LIKE '%");
			sql.append(dcode);
			sql.append("%' ");
		}
		if (dsname != null && !dsname.equals("")) {
			sql.append("   and A.DEALER_NAME like '%");
			sql.append(dsname);
			sql.append("%' ");
		}
		if (provinceId != null && !"".equals(provinceId)) {
			sql.append("   AND A.PROVINCE_ID = '" + provinceId + "' \n");
		}
		if (dealerClass != null && !"".equals(dealerClass)) {
			sql.append("   AND A.DEALER_CLASS = '" + dealerClass + "' \n");
		}
		// modify by zhaojinyu 2010-06-12 经销商树中售后只查询售后的经销商，销售只查询销售的经销商。
		if (isDealerType.equals("null")) {
			isDealerType = "";
		}
		if (isDealerType.equals("")) {
			if (Constant.POSE_BUS_TYPE_WR == poseBusType || Constant.POSE_BUS_TYPE_DWR == poseBusType) {
				sql.append("   and A.DEALER_TYPE =" + Constant.DEALER_TYPE_DWR + " \n");
			} else if (Constant.POSE_BUS_TYPE_VS == poseBusType || Constant.POSE_BUS_TYPE_DVS == poseBusType) {
				sql.append("   and A.DEALER_TYPE IN (" + Constant.DEALER_TYPE_DVS + ", " + Constant.DEALER_TYPE_QYZDL + ", " + Constant.DEALER_TYPE_JSZX + ") \n");
			}
		}
		if (!isDealerType.equals("null") && isDealerType != "") {
			sql.append("and A.DEALER_TYPE IN (" + isDealerType + ")");
		}
		if (orgId != null && !orgId.equals("")) {
			sql.append("   and a.dealer_id in\n");
			sql.append("       (select td.dealer_id\n");
			sql.append("          from tm_dealer td\n");
			sql.append("         START WITH TD.DEALER_ID in\n");
			sql.append("                    (select tdor.dealer_id\n");
			sql.append("                       from tm_dealer_org_relation tdor\n");
			sql.append("                      where tdor.org_id in\n");
			sql.append("                            (SELECT ORG_ID\n");
			sql.append("                               FROM TM_ORG ORG\n");
			sql.append("                              WHERE ORG.STATUS = " + Constant.STATUS_ENABLE + "\n");
			sql.append("                              START WITH ORG.ORG_ID = " + orgId + "\n");
			sql.append("                             CONNECT BY PRIOR ORG.ORG_ID = ORG.PARENT_ORG_ID))\n");
			sql.append("        CONNECT BY PRIOR TD.DEALER_ID = td.parent_dealer_d)\n");
		} else {
			if (inputOrgId != null && !"".equals(inputOrgId) && !"null".equals(inputOrgId)) {
				sql.append("   and a.dealer_id in\n");
				sql.append("       (select td.dealer_id\n");
				sql.append("          from tm_dealer td\n");
				sql.append("         START WITH TD.DEALER_ID in\n");
				sql.append("                    (select tdor.dealer_id\n");
				sql.append("                       from tm_dealer_org_relation tdor\n");
				sql.append("                      where tdor.org_id in\n");
				sql.append("                            (SELECT ORG_ID\n");
				sql.append("                               FROM TM_ORG ORG\n");
				sql.append("                              WHERE ORG.STATUS = " + Constant.STATUS_ENABLE + "\n");
				sql.append("                              START WITH ORG.ORG_ID = " + inputOrgId + "\n");
				sql.append("                             CONNECT BY PRIOR ORG.ORG_ID = ORG.PARENT_ORG_ID))\n");
				sql.append("        CONNECT BY PRIOR TD.DEALER_ID = td.parent_dealer_d)\n");
			}
		}

		if (isAllArea.toLowerCase().equals("false")) {
			// ADD BY YUYONG 2010.8.30 增加业务范围过滤
			String areaIds = MaterialGroupManagerDao.getAreaIdsByPoseId(poseId);
			sql.append("   AND EXISTS\n");
			sql.append(" (SELECT TDBA.DEALER_ID\n");
			sql.append("          FROM TM_DEALER_BUSINESS_AREA TDBA\n");
			sql.append("         WHERE A.DEALER_ID = TDBA.DEALER_ID\n");
			sql.append("           AND TDBA.AREA_ID IN\n");
			sql.append("               (" + areaIds + "))\n");
			sql.append(" ORDER BY A.DEALER_CODE, A.DEALER_NAME");
		}

		logger.info("SQL: " + sql);
		return factory.pageQuery(sql.toString(), null, new DAOCallback<TmDealerPO>() {
			public TmDealerPO wrapper(ResultSet rs, int idx) {
				TmDealerPO bean = new TmDealerPO();
				try {
					bean.setDealerCode(rs.getString("DEALER_CODE"));
					bean.setDealerId(rs.getLong("DEALER_ID"));
					bean.setDealerName(rs.getString("DEALER_NAME"));
					bean.setDealerShortname(rs.getString("DEALER_SHORTNAME"));
					bean.setDealerType(rs.getInt("DEALER_TYPE"));
				} catch (SQLException e) {
					e.printStackTrace();
				}
				return bean;
			}
		}, curPage, pageSize);
	}

	public static PageResult<TmOrgPO> getAllOrg(String areaId, String orgId, String orgCode, String orgName, Long companyId, int curPage, int pageSize, String inputOrgId) throws Exception {
		List<TmOrgPO> list = new ArrayList<TmOrgPO>();
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT DISTINCT TMO.ORG_ID,\n");
		sql.append("       TMO.ORG_CODE,\n");
		sql.append("       TMO.ORG_NAME,\n");
		sql.append("       TMO.ORG_DESC,\n");
		sql.append("       TMO.TREE_CODE,\n");
		sql.append("       TMO.ORG_LEVEL\n");
		sql.append("  FROM TM_ORG TMO,TM_ORG_BUSINESS_AREA TOBA\n");
		sql.append(" WHERE TMO.STATUS = " + Constant.STATUS_ENABLE + "\n");
		sql.append(" AND TMO.ORG_TYPE = " + Constant.ORG_TYPE_OEM);
		sql.append(" AND TOBA.ORG_ID=TMO.ORG_ID");
		if (orgId == null) {
			sql.append(" AND tmo.org_level=2");
		} else {
			sql.append(" AND tmo.org_level in (2,3)");
		}
		sql.append(" AND TMO.COMPANY_ID = " + companyId);
		if (!"".equals(orgCode) && orgCode != null) {
			sql.append("   AND TMO.ORG_CODE LIKE '%" + orgCode + "%'\n");
		}
		if (!"".equals(areaId) && areaId != null) {
			sql.append(" AND TOBA.AREA_ID='" + areaId + "'\n");
		}

		if (!"".equals(orgName) && orgName != null) {
			sql.append("   AND TMO.ORG_NAME LIKE '%" + orgName + "%'\n");
		}
		if (orgId != null && !orgId.equals("")) {
			sql.append("   AND (TMO.PARENT_ORG_ID = " + orgId + " OR TMO.ORG_ID=" + orgId + ")\n");
		} else {
			if (inputOrgId != null && !"".equals(inputOrgId) && !"null".equals(inputOrgId)) {
				sql.append("   AND TMO.PARENT_ORG_ID = " + inputOrgId + "\n");
			}

		}
		sql.append(" ORDER  BY  TMO.ORG_ID \n");
		logger.info("SQL: " + sql);
		return factory.pageQuery(sql.toString(), null, new DAOCallback<TmOrgPO>() {
			public TmOrgPO wrapper(ResultSet rs, int idx) {
				TmOrgPO bean = new TmOrgPO();
				try {
					bean.setOrgId(rs.getLong("ORG_ID"));
					bean.setOrgCode(rs.getString("ORG_CODE"));
					bean.setOrgName(rs.getString("ORG_NAME"));
					bean.setOrgDesc(rs.getString("ORG_DESC"));
					bean.setTreeCode(rs.getString("TREE_CODE"));
					bean.setOrgLevel(rs.getInt("ORG_LEVEL"));
				} catch (SQLException e) {
					e.printStackTrace();
				}
				return bean;
			}
		}, curPage, pageSize);
	}

	public static PageResult<TmOrgPO> getAllOrg3(String orgId, String orgCode, String orgName, Long companyId, int curPage, int pageSize, String inputOrgId) throws Exception {
		List<TmOrgPO> list = new ArrayList<TmOrgPO>();
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT DISTINCT TMO.ORG_ID,\n");
		sql.append("       TMO.ORG_CODE,\n");
		sql.append("       TMO.ORG_NAME,\n");
		sql.append("       TMO.ORG_DESC,\n");
		sql.append("       TMO.TREE_CODE,\n");
		sql.append("       TMO.ORG_LEVEL\n");
		sql.append("  FROM TM_ORG TMO\n");
		sql.append(" WHERE TMO.STATUS = " + Constant.STATUS_ENABLE + "\n");
		sql.append(" AND TMO.ORG_TYPE = " + Constant.ORG_TYPE_OEM);
		if (orgId == null) {
			sql.append(" AND tmo.org_level=3");
		} else {
			sql.append(" AND tmo.org_level in (3)");
		}
		sql.append(" AND TMO.COMPANY_ID = " + companyId);
		if (!"".equals(orgCode) && orgCode != null) {
			sql.append("   AND TMO.ORG_CODE LIKE '%" + orgCode + "%'\n");
		}

		if (!"".equals(orgName) && orgName != null) {
			sql.append("   AND TMO.ORG_NAME LIKE '%" + orgName + "%'\n");
		}
		if (orgId != null && !orgId.equals("")) {
			sql.append("   AND (TMO.PARENT_ORG_ID = " + orgId + " OR TMO.ORG_ID=" + orgId + ")\n");
		} else {
			if (inputOrgId != null && !"".equals(inputOrgId) && !"null".equals(inputOrgId)) {
				sql.append("   AND TMO.PARENT_ORG_ID = " + inputOrgId + "\n");
			}

		}
		sql.append(" ORDER  BY  TMO.ORG_ID \n");
		logger.info("SQL: " + sql);
		return factory.pageQuery(sql.toString(), null, new DAOCallback<TmOrgPO>() {
			public TmOrgPO wrapper(ResultSet rs, int idx) {
				TmOrgPO bean = new TmOrgPO();
				try {
					bean.setOrgId(rs.getLong("ORG_ID"));
					bean.setOrgCode(rs.getString("ORG_CODE"));
					bean.setOrgName(rs.getString("ORG_NAME"));
					bean.setOrgDesc(rs.getString("ORG_DESC"));
					bean.setTreeCode(rs.getString("TREE_CODE"));
					bean.setOrgLevel(rs.getInt("ORG_LEVEL"));
				} catch (SQLException e) {
					e.printStackTrace();
				}
				return bean;
			}
		}, curPage, pageSize);
	}

	// zhumingwei 2011-02-25
	public static PageResult<TmOrgPO> getAllOrg111(String areaId, String orgId, String orgCode, String orgName, Long companyId, int curPage, int pageSize, String inputOrgId) throws Exception {
		List<TmOrgPO> list = new ArrayList<TmOrgPO>();
		StringBuffer sql = new StringBuffer();
		sql.append("select * from tm_org o where o.org_type='" + Constant.ORG_TYPE_DEALER + "' and o.duty_type=" + Constant.DUTY_TYPE_DEALER + "\n");
		if (!"".equals(orgCode) && orgCode != null) {
			sql.append("   AND o.ORG_CODE LIKE '%" + orgCode + "%'\n");
		}
		if (!"".equals(orgName) && orgName != null) {
			sql.append("   AND o.ORG_NAME LIKE '%" + orgName + "%'\n");
		}
		sql.append(" ORDER  BY  o.ORG_ID \n");
		logger.info("SQL: " + sql);
		return factory.pageQuery(sql.toString(), null, new DAOCallback<TmOrgPO>() {
			public TmOrgPO wrapper(ResultSet rs, int idx) {
				TmOrgPO bean = new TmOrgPO();
				try {
					bean.setOrgId(rs.getLong("ORG_ID"));
					bean.setOrgCode(rs.getString("ORG_CODE"));
					bean.setOrgName(rs.getString("ORG_NAME"));
					bean.setOrgDesc(rs.getString("ORG_DESC"));
					bean.setTreeCode(rs.getString("TREE_CODE"));
					bean.setOrgLevel(rs.getInt("ORG_LEVEL"));
				} catch (SQLException e) {
					e.printStackTrace();
				}
				return bean;
			}
		}, curPage, pageSize);
	}

	public static PageResult<TmDealerPO> getAllAreaDealer(String areaId, String orgId, int curPage, int pageSize) throws Exception {
		List<TmOrgPO> list = new ArrayList<TmOrgPO>();
		StringBuffer sql = new StringBuffer();
		sql.append("select * from tm_dealer td where td.dealer_id in (select tdor.dealer_id from tm_dealer_org_relation tdor where 1=1");
		if (!"".equals(orgId) && orgId != null) {
			sql.append("AND TDOR.ORG_ID='" + orgId + "'");
		}
		sql.append(" ORDER  BY  TD.DEALER_ID \n");
		logger.info("SQL: " + sql);
		return factory.pageQuery(sql.toString(), null, new DAOCallback<TmDealerPO>() {
			public TmDealerPO wrapper(ResultSet rs, int idx) {
				TmDealerPO bean = new TmDealerPO();
				try {
					bean.setDealerId(rs.getLong("DEALER_ID"));
					bean.setDealerCode(rs.getString("DEALER_CODE"));
					bean.setDealerName(rs.getString("DEALER_NAME"));
					bean.setDealerShortname(rs.getString("DEALER_SHORTNAME"));
					bean.setTreeCode(rs.getString("TREE_CODE"));
					bean.setDealerLevel(rs.getInt("DEALER_LEVEL"));
				} catch (SQLException e) {
					e.printStackTrace();
				}
				return bean;
			}
		}, curPage, pageSize);
	}

	private static List<TmOrgPO> getXjbm(String orgId, Long companyId, List<TmOrgPO> retLst) {
		if (orgId == null || "".equals(orgId)) {
			return null;
		}
		StringBuffer sql = new StringBuffer();
		sql.append(" select ORG_ID,PARENT_ORG_ID  from (SELECT ORG_ID, PARENT_ORG_ID ");
		sql.append(" FROM TM_ORG  WHERE ORG_TYPE <> ");
		sql.append(Constant.ORG_TYPE_OEM);
		sql.append(" AND COMPANY_ID = ");
		sql.append(companyId);
		sql.append(" UNION  SELECT ORG_ID, 1000000000 PARENT_ORG_ID   FROM TM_ORG ");
		sql.append(" WHERE ORG_TYPE = ");
		sql.append(Constant.ORG_TYPE_OEM);
		sql.append(" AND COMPANY_ID = ");
		sql.append(companyId);
		sql.append(" )  START WITH ORG_ID = ");
		sql.append(orgId);
		sql.append(" connect by prior ORG_ID = PARENT_ORG_ID ");
		List<TmOrgPO> lst = factory.select(sql.toString(), null, new DAOCallback<TmOrgPO>() {
			public TmOrgPO wrapper(ResultSet rs, int idx) {
				TmOrgPO bean = new TmOrgPO();
				try {
					bean.setOrgId(rs.getLong("ORG_ID"));
					bean.setParentOrgId(rs.getLong("PARENT_ORG_ID"));
				} catch (SQLException e) {
					e.printStackTrace();
				}
				return bean;
			}
		});

		Iterator<TmOrgPO> it = lst.iterator();
		while (it.hasNext()) {
			TmOrgPO dept = (TmOrgPO) it.next();
			retLst.add(dept);
		}
		return retLst;
	}

	/**
	 * 经销商根据多个职位ID得到职位信息
	 * 
	 * @param dealerId
	 * @param poseIds
	 * @param curPage
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	public static List<TcPosePO> getDRLPoseByPoseIds(Long companyId, String poseIds) throws Exception {
		String query = " SELECT POSE_ID,POSE_CODE,POSE_NAME FROM TC_POSE WHERE POSE_TYPE = '" + Constant.SYS_USER_DEALER + "' AND POSE_STATUS = '" + Constant.STATUS_ENABLE + "' and POSE_ID IN (" + poseIds + ") AND COMPANY_ID = " + companyId;

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

	/**
	 * 查询需要下发用户的代理商公司
	 * 
	 * @author xwj
	 * @return List<TmCompanyPO>
	 * @throws Exception
	 */
	public static List<TmCompanyPO> getDownUserCompany() throws Exception {
		String query = " SELECT DISTINCT A.COMPANY_CODE,A.COMPANY_ID" + " FROM TM_COMPANY A,TC_USER B WHERE A.COMPANY_ID = B.COMPANY_ID" + " AND B.IS_DOWN = 0";
		logger.debug("the getDownUserCompany SQL===== " + query);
		return factory.select(query, null, new DAOCallback<TmCompanyPO>() {
			public TmCompanyPO wrapper(ResultSet rs, int idx) {
				TmCompanyPO bean = new TmCompanyPO();
				try {
					bean.setCompanyCode(rs.getString("COMPANY_CODE"));
					bean.setCompanyId(rs.getLong("COMPANY_ID"));
				} catch (SQLException e) {
					e.printStackTrace();
				}
				return bean;
			}
		});
	}

	public static PageResult<TmPtSupplierPO> allSuppQuery(String suppCode, String suppName, int curPage, int pageSize) {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT A.SUPPLIER_ID, A.SUPPLIER_CODE, A.SUPPLIER_NAME, A.SHORT_NAME, A.LINK_MAN, A.PHONE_NUMBER ");
		sql.append("FROM TM_PT_SUPPLIER A ");
		sql.append("WHERE A.IS_DEL = 0 ");
		if (!suppCode.equals("")) {
			sql.append("AND A.SUPPLIER_CODE LIKE '%").append(suppCode).append("%' ");
		}
		if (!suppName.equals("")) {
			sql.append("AND A.SUPPLIER_NAME LIKE '%").append(suppName).append("%' ");
		}

		System.out.println(sql);
		return factory.pageQuery(sql.toString(), null, new DAOCallback<TmPtSupplierPO>() {
			public TmPtSupplierPO wrapper(ResultSet rs, int idx) {
				TmPtSupplierPO po = new TmPtSupplierPO();
				try {
					po.setSupplierId(rs.getLong("SUPPLIER_ID"));
					po.setSupplierCode(rs.getString("SUPPLIER_CODE"));
					po.setSupplierName(rs.getString("SUPPLIER_NAME"));
					po.setShortName(rs.getString("SHORT_NAME"));
					po.setLinkMan(rs.getString("LINK_MAN"));
					po.setPhoneNumber(rs.getString("PHONE_NUMBER"));
				} catch (SQLException e) {
					e.printStackTrace();
				}
				return po;
			}
		}, pageSize, curPage);
	}

	public static PageResult<TmPtPartTypePO> allPartTypeQuery(String suppCode, String suppName, int curPage, int pageSize) {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT A.ID, A.PARTTYPE_CODE, A.PARTTYPE_NAME, A.PART_NUM, A.IS_MAX, A.IS_RETURN ");
		sql.append("FROM TM_PT_PART_TYPE A ");
		sql.append("WHERE 1 = 1 ");
		if (!suppCode.equals("")) {
			sql.append("AND A.PARTTYPE_CODE LIKE '%").append(suppCode).append("%' ");
		}
		if (!suppName.equals("")) {
			sql.append("AND A.PARTTYPE_NAME LIKE '%").append(suppName).append("%' ");
		}

		System.out.println(sql);
		return factory.pageQuery(sql.toString(), null, new DAOCallback<TmPtPartTypePO>() {
			public TmPtPartTypePO wrapper(ResultSet rs, int idx) {
				TmPtPartTypePO po = new TmPtPartTypePO();
				try {
					po.setId(rs.getLong("ID"));
					po.setParttypeCode(rs.getString("PARTTYPE_CODE"));
					po.setParttypeName(rs.getString("PARTTYPE_NAME"));
					po.setPartNum(rs.getLong("PART_NUM"));
					po.setIsMax(rs.getInt("IS_MAX"));
					po.setIsReturn(rs.getInt("IS_RETURN"));
				} catch (SQLException e) {
					e.printStackTrace();
				}
				return po;
			}
		}, pageSize, curPage);
	}

	public PageResult<Map<String, Object>> getAllRegion(Map<String, String> map, int curpage, int pagesize) {
		String regionCode = map.get("regionCode");
		String regionName = map.get("regionName");
		String org_id = map.get("org_id");

		StringBuffer sql = new StringBuffer("\n");

		sql.append("select r.region_id, r.region_code, r.region_name\n");
		sql.append("  from tm_region r\n");
		sql.append(" where 1 = 1\n");
		sql.append("   and r.region_type = ").append(Constant.REGION_TYPE_02).append("\n");

		if (org_id != null && !"".equals(org_id) && !"null".equals(org_id)) {
			sql.append("   and exists (select 1\n");
			sql.append("          from tt_vs_org_region_r voda\n");
			sql.append("         where voda.Region_Code = r.region_code\n");
			sql.append("           and voda.ORG_ID = ").append(org_id).append(")\n");
		}

		if (!CommonUtils.isNullString(regionCode) && !"null".equals(regionCode)) {
			sql.append("   and r.region_code like '%").append(regionCode).append("%'\n");
		}

		if (!CommonUtils.isNullString(regionName) && !"null".equals(regionName)) {
			sql.append("   and r.region_name like '%").append(regionName).append("%'\n");
		}

		PageResult<Map<String, Object>> ps = this.pageQuery(sql.toString(), null, getFunName(), pagesize, curpage);
		return ps;
	}

	/***************************************************************************
	 * @addUser xoingchuan 2011-10-13
	 * @param areaId
	 * @param isDealerType
	 * @param orgId
	 * @param dcode
	 * @param dsname
	 * @param companyId
	 * @param provinceId
	 * @param dealerClass
	 * @param curPage
	 * @param pageSize
	 * @param orderName
	 * @param da
	 * @param inputOrgId
	 * @param poseBusType
	 * @param poseId
	 * @param isAllLevel
	 * @param isAllArea
	 * @return
	 * @throws Exception
	 */
	public static PageResult<TmDealerPO> getAllDRLByDeptIdNew(String areaId, String isDealerType, String orgId, String dcode, String dsname, Long companyId, String provinceId, String dealerClass, int curPage, int pageSize, String orderName, String da, String inputOrgId, Integer poseBusType, Long poseId, String isAllLevel, String isAllArea) throws Exception {

		StringBuffer sql = new StringBuffer();
		sql.append("SELECT DISTINCT A.DEALER_CODE, A.DEALER_ID,A.DEALER_TYPE, A.DEALER_NAME, A.DEALER_NAME DEALER_SHORTNAME\n");
		sql.append("  FROM TM_DEALER a,TM_DEALER_BUSINESS_AREA TDBA\n");
		sql.append(" where A.STATUS = " + Constant.STATUS_ENABLE + "\n");
		sql.append("   AND A.OEM_COMPANY_ID = " + companyId + "\n");
		sql.append(" AND TDBA.DEALER_ID=A.DEALER_ID and a.invoice_level=13611001 \n");
		if (areaId != null && !areaId.equals("") && !areaId.equals("null")) {
			sql.append("AND TDBA.AREA_ID IN (" + areaId + ")\n");
		}
		if (dcode != null && !dcode.equals("")) {
			sql.append("   AND A.DEALER_CODE LIKE '%");
			sql.append(dcode);
			sql.append("%' ");
		}
		if (dsname != null && !dsname.equals("")) {
			sql.append("   and A.DEALER_NAME like '%");
			sql.append(dsname);
			sql.append("%' ");
		}
		if (provinceId != null && !"".equals(provinceId)) {
			sql.append("   AND A.PROVINCE_ID = '" + provinceId + "' \n");
		}
		if (dealerClass != null && !"".equals(dealerClass)) {
			sql.append("   AND A.DEALER_CLASS = '" + dealerClass + "' \n");
		}
		if (isAllLevel.toLowerCase().equals("false")) {
			sql.append("   AND A.DEALER_LEVEL = " + Constant.DEALER_LEVEL_01 + " \n");
		}
		// modify by zhaojinyu 2010-06-12 经销商树中售后只查询售后的经销商，销售只查询销售的经销商。
		if (isDealerType.equals("null")) {
			isDealerType = "";
		}
		if (isDealerType.equals("")) {
			if (Constant.POSE_BUS_TYPE_WR == poseBusType || Constant.POSE_BUS_TYPE_DWR == poseBusType) {
				sql.append("   and A.DEALER_TYPE =" + Constant.DEALER_TYPE_DWR + " \n");
			} else if (Constant.POSE_BUS_TYPE_VS == poseBusType || Constant.POSE_BUS_TYPE_DVS == poseBusType) {
				sql.append("   and A.DEALER_TYPE IN (" + Constant.DEALER_TYPE_DVS + ", " + Constant.DEALER_TYPE_QYZDL + ", " + Constant.DEALER_TYPE_JSZX + ") \n");
			}
		}
		if (!isDealerType.equals("null") && isDealerType != "") {
			sql.append("and A.DEALER_TYPE IN (" + isDealerType + ")");
		}
		if (orgId != null && !orgId.equals("")) {
			sql.append("   and a.dealer_id in\n");
			sql.append("       (select td.dealer_id\n");
			sql.append("          from tm_dealer td\n");
			sql.append("         START WITH TD.DEALER_ID in\n");
			sql.append("                    (select tdor.dealer_id\n");
			sql.append("                       from tm_dealer_org_relation tdor\n");
			sql.append("                      where tdor.org_id in\n");
			sql.append("                            (SELECT ORG_ID\n");
			sql.append("                               FROM TM_ORG ORG\n");
			sql.append("                              WHERE ORG.STATUS = " + Constant.STATUS_ENABLE + "\n");
			sql.append("                              START WITH ORG.ORG_ID = " + orgId + "\n");
			sql.append("                             CONNECT BY PRIOR ORG.ORG_ID = ORG.PARENT_ORG_ID))\n");
			sql.append("        CONNECT BY PRIOR TD.DEALER_ID = td.parent_dealer_d)\n");
		} else {
			if (inputOrgId != null && !"".equals(inputOrgId) && !"null".equals(inputOrgId)) {
				sql.append("   and a.dealer_id in\n");
				sql.append("       (select td.dealer_id\n");
				sql.append("          from tm_dealer td\n");
				sql.append("         START WITH TD.DEALER_ID in\n");
				sql.append("                    (select tdor.dealer_id\n");
				sql.append("                       from tm_dealer_org_relation tdor\n");
				sql.append("                      where tdor.org_id in\n");
				sql.append("                            (SELECT ORG_ID\n");
				sql.append("                               FROM TM_ORG ORG\n");
				sql.append("                              WHERE ORG.STATUS = " + Constant.STATUS_ENABLE + "\n");
				sql.append("                              START WITH ORG.ORG_ID = " + inputOrgId + "\n");
				sql.append("                             CONNECT BY PRIOR ORG.ORG_ID = ORG.PARENT_ORG_ID))\n");
				sql.append("        CONNECT BY PRIOR TD.DEALER_ID = td.parent_dealer_d)\n");
			}
		}

		if (isAllArea.toLowerCase().equals("false")) {
			// ADD BY YUYONG 2010.8.30 增加业务范围过滤
			String areaIds = MaterialGroupManagerDao.getAreaIdsByPoseId(poseId);
			sql.append("   AND EXISTS\n");
			sql.append(" (SELECT TDBA.DEALER_ID\n");
			sql.append("          FROM TM_DEALER_BUSINESS_AREA TDBA\n");
			sql.append("         WHERE A.DEALER_ID = TDBA.DEALER_ID\n");
			sql.append("           AND TDBA.AREA_ID IN\n");
			sql.append("               (" + areaIds + "))\n");
			sql.append(" ORDER BY A.DEALER_CODE, A.DEALER_NAME");
		}

		logger.info("SQL: " + sql);
		return factory.pageQuery(sql.toString(), null, new DAOCallback<TmDealerPO>() {
			public TmDealerPO wrapper(ResultSet rs, int idx) {
				TmDealerPO bean = new TmDealerPO();
				try {
					bean.setDealerCode(rs.getString("DEALER_CODE"));
					bean.setDealerId(rs.getLong("DEALER_ID"));
					bean.setDealerName(rs.getString("DEALER_NAME"));
					bean.setDealerShortname(rs.getString("DEALER_SHORTNAME"));
					bean.setDealerType(rs.getInt("DEALER_TYPE"));
				} catch (SQLException e) {
					e.printStackTrace();
				}
				return bean;
			}
		}, curPage, pageSize);
	}

	/**
	 * 
	 * @Title: queryPoseBusTypeByUserid
	 * @Description: TODO(根据userid查询pose_bus_type信息)
	 * @param
	 * @param uid
	 * @return Map<String,Object>返回类型
	 * @throws
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> queryPoseBusTypeByUserid(long uid) {
		String sql = " select u.user_id,u.name,p.pose_bus_type from tc_user u, tc_pose p, TR_USER_POSE up\n" + " where u.user_id = up.user_id and p.pose_id = up.pose_id and u.user_status ='10011001' and u.user_id = '" + uid + "'";
		Map<String, Object> ps = pageQueryMap(sql.toString(), null, getFunName());
		return ps;
	}

	/**
	 * 
	 * @Title: queryPoseTypeByUserid
	 * @Description: TODO(根据userid查询pose_bus_type信息)
	 * @param
	 * @param uid
	 * @return Map<String,Object>返回类型
	 * @throws
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> queryPoseTypeByUserid(long uid) {
		String sql = "select c.company_type,u.user_id from tm_company c,tc_user u\n" + " where c.company_id = u.company_id\n" + " and c.company_type <>" + Constant.COMPANY_TYPE_SGM + " \n" + " and u.user_id = '" + uid + "'";
		Map<String, Object> ps = pageQueryMap(sql.toString(), null, getFunName());
		return ps;
	}
	public PageResult<Map<String, Object>> getUserList(Map<String, String> map,
			Integer pageSize, Integer curPage) {
		String userCode=map.get("userCode");
		String userName=map.get("userName");
		String userId=map.get("userId");
		String companyId=map.get("companyId");
		String poseRank=map.get("poseRank");
		StringBuilder sql= new StringBuilder();
		sql.append("select tp.user_id,\n" );
		sql.append("       tp.acnt,\n" );
		sql.append("       tp.name,\n" );
		sql.append("       tp.create_date,\n" );
		sql.append("       tp.create_by,\n" );
		sql.append("       tp.user_status,\n" );
		sql.append("       tp.pose_rank\n" );
		sql.append("  from tc_user tp,tm_company tc\n" );
		sql.append(" where 1 = 1\n" );
		sql.append("   and tp.company_id=tc.company_id");
		sql.append("   and tp.pose_rank="+poseRank);
		if(userId!=null&&!"".equals(userId)){
			sql.append("   and tp.user_id!="+userId);
		}
		sql.append("   and tc.company_id="+companyId);
		if(null!=userCode&&!"".equals(userCode)){
			sql.append("  and tp.acnt like '%"+userCode+"%'\n" );
		}
		if(null!=userName&&!"".equals(userName)){
			sql.append("   and tp.name like '%"+userName+"%'\n");
		}
		sql.append("  order by tp.create_date desc ");
		
		
		PageResult<Map<String, Object>> ps =dao.pageQuery(sql.toString(), null, dao.getFunName(), pageSize, curPage);
		return ps;
	}


	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}

}
