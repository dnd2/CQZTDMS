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

package com.infodms.dms.dao.usermng;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.actions.common.Demo;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.OrderUtil;
import com.infodms.dms.common.Utility;
import com.infodms.dms.common.materialManager.MaterialGroupManagerDao;
import com.infodms.dms.dao.claim.application.DealerKpDao;
import com.infodms.dms.dao.claim.auditing.BalanceAuditingDao;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.dao.orgmng.DlrInfoMngDAO;
import com.infodms.dms.dao.potentialCustomer.PotentialCustomerDao;
import com.infodms.dms.po.TcPosePO;
import com.infodms.dms.po.TcUserPO;
import com.infodms.dms.po.TmCompanyPO;
import com.infodms.dms.po.TmDealerPO;
import com.infodms.dms.po.TmOrgPO;
import com.infodms.dms.po.TmPtSupplierPO;
import com.infodms.dms.po.TmPtPartTypePO;
import com.infodms.dms.po.TtAsWrSpecialChargeAuditPO;
import com.infodms.dms.po.TtAsWrSpecialChargePO;
import com.infodms.dms.po.VwMaterialGroupPO;
import com.infodms.dms.po.VwMaterialInfoPO;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.po3.POFactory;
import com.infoservice.po3.POFactoryBuilder;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;
import com.infoservice.po3.core.callback.DAOCallback;
import com.infoservice.po3.core.context.DBService;
import com.jatools.swing.r;

@SuppressWarnings("unchecked")
public class UserMngDAO extends BaseDao{
	public static Logger logger = Logger.getLogger(UserMngDAO.class);
	private static POFactory factory = POFactoryBuilder.getInstance();
	private static final  UserMngDAO dao = new UserMngDAO();
	
	public static UserMngDAO getInstance() {
		return dao;
	}
	
	/**
	 * 根据id查询经销商信息
	 * @param dealerIds
	 * @return
	 */
	public static List<TmDealerPO> selectDealerById(String dealerIds){
		if(dealerIds == null || "".equals(dealerIds)){
			return null;
		}
		String sql = "select * from TM_DEALER where dealer_id in ("+ dealerIds +")";
		List<TmDealerPO> list = dao.select(TmDealerPO.class, sql, null);
		return list;
	}
	
	/**
	 * 查询所有有效的一级销售经销商
	 * @param dealerIds
	 * @return
	 */
	public static List<TmDealerPO> selectXsDealer(String dealerType){
		//10771001 销售
		//10771002 售后
		String sql = "select dealer_id,company_id from TM_DEALER where dealer_type = '"+dealerType+"' and status='10011001' and dealer_level = '10851001'";
		List<TmDealerPO> list = dao.select(TmDealerPO.class, sql, null);
		return list;
	}
	
	public static List<TmDealerPO> selectAllDealer(){
		String sql = "select dealer_id,dealer_name from TM_DEALER where dealer_name is not null";
		List<TmDealerPO> list = dao.select(TmDealerPO.class, sql, null);
		for(TmDealerPO po : list){
			Long dealerId = po.getDealerId();
			String dealerName = po.getDealerName();
			try{
				dealerName = Utility.hanZiToPinyin(dealerName);
				if(dealerName == null || "".equals(dealerName)){
					logger.info("========经销商名称为空，经销商id："+dealerId+"===================");
					continue;
				}
				String sql2 = "update TM_DEALER set pinyin = '"+dealerName+"' where DEALER_ID = " + dealerId;
				dao.update(sql2, null);
			}catch(Exception e){
				System.out.println("========================"+dealerId);
			}
		}
		return list;
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
	public static PageResult<TcUserPO> sgmSysUserQuery(String orgId,Long companyId,
			String acnt,String name, int curPage, int pageSize,
			String orderName, String da) throws Exception {
		String query = " SELECT DISTINCT TU.USER_ID,TU.ACNT,TU.NAME,TU.USER_STATUS FROM TC_USER TU,TC_POSE TP,"
				+ "TR_USER_POSE TUP WHERE TU.USER_ID = TUP.USER_ID AND TP.POSE_ID = TUP.POSE_ID AND "
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
						} catch (SQLException e) {
							e.printStackTrace();
						}
						return bean;
					}
				}, curPage, pageSize));
	}
	
	public static PageResult<Map<String,Object>> sgmSysUserQueryA(String orgId,Long companyId, String acnt,String name, int curPage, int pageSize, String orderName, String da,Long userId,Long poseId) {
		StringBuffer sql = new StringBuffer("\n") ;
		
		sql.append(" SELECT DISTINCT TU.USER_ID,TU.ACNT,TU.NAME,TU.USER_STATUS, TCU.NAME UPDATE_NAME, TO_CHAR(TU.UPDATE_DATE, 'YYYY-MM-DD') UPDATE_DATE FROM TC_USER TU,TC_POSE TP,"
			+ "TR_USER_POSE TUP, TC_USER TCU WHERE TU.UPDATE_BY = TCU.USER_ID(+) AND TU.USER_ID = TUP.USER_ID AND TP.POSE_ID = TUP.POSE_ID AND "
			+ "TU.COMPANY_ID ='" + companyId + "' ");
		
		if (userId!=1000002433) {
			sql.append("  and tu.user_id <> 1000002433");
		}else{
			if(poseId!=4000008240L){
				sql.append("  and tu.user_id <> 1000002433");
			}
		}
		
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
		
		return getPoseByUserIdA(dao.pageQuery(mySql, null, dao.getFunName(), curPage, pageSize)) ;
	}

	
	public static List<TtAsWrSpecialChargePO> getPosition(
			String id) throws Exception {
		StringBuffer sql= new StringBuffer();
		sql.append("\n" );
		sql.append("SELECT A.SPE_ID,A.SPE_LEVEL,A.MIN_AMOUNT,A.MAX_AMOUNT,\n" );
		sql.append("       (SELECT COUNT(*)\n" );
		sql.append("          from TT_AS_WR_SPECIAL_CHARGE_AUDIT B\n" );
		sql.append("         where A.SPE_ID = B.SPE_ID\n" );
		sql.append("           AND B.USER_ID = "+id+") AS VAR\n" );
		sql.append("  from TT_AS_WR_SPECIAL_CHARGE A");
		return dao.select(TtAsWrSpecialChargePO.class, sql.toString(), null);
	}
	
	public static PageResult<Map<String,Object>> specialQuery( String acnt,String name, int curPage, int pageSize) {
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT A.USER_ID,A.ACNT,A.NAME\n" );
		sql.append("  from TC_USER A, TC_POSE B, TR_USER_POSE C\n" );
		sql.append(" where A.USER_ID = C.USER_ID\n" );
		sql.append("   AND B.POSE_ID(+) = C.POSE_ID\n" );
		sql.append("   AND B.POSE_TYPE = "+Constant.SYS_USER_SGM);
		
		if (acnt != null && !acnt.equals("")) {
			sql.append("  AND upper(A.ACNT) like '%" + acnt.toUpperCase() + "%'");
		}
		if (name != null && !name.equals("")) {
			sql.append("  AND upper(A.NAME) like '%" + name.toUpperCase() + "%'");
	}
		sql.append("   GROUP by A.USER_ID,a.ACNT,a.NAME");
		return dao.pageQuery(sql.toString(), null, dao.getFunName(), curPage, pageSize) ;
	}
	
	private static PageResult<TcUserPO> getPoseByUserId(PageResult<TcUserPO> rs) {
		if (rs.getRecords() != null) {
			List<TcUserPO> list = rs.getRecords(); // 取出用户
			String poseName = null;
			String sql = " SELECT TP.POSE_NAME FROM TC_POSE TP,TR_USER_POSE TUP WHERE TUP.POSE_ID = TP.POSE_ID ";
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
	
	private static PageResult<Map<String,Object>> getPoseByUserIdA(PageResult<Map<String,Object>> rs) {
		if (rs.getRecords() != null) {
			List<Map<String,Object>> list = rs.getRecords(); // 取出用户
			String poseName = null;
			String sql = " SELECT TP.POSE_NAME FROM TC_POSE TP,TR_USER_POSE TUP WHERE TUP.POSE_ID = TP.POSE_ID ";
			for (int i = 0; i < list.size() && list != null; i++) {
				poseName = "";
				Map<String,Object> user = list.get(i);
				List<TcPosePO> postList = factory.select(sql
						+ " AND TUP.USER_ID ='" + user.get("USER_ID") + "'", null,
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
	public static List<TcPosePO> getSGMPoseByCompanyId(String poseCode,Long companyId,
			String poseName,String poseIds) throws Exception {
		String query = " SELECT A.POSE_ID,A.POSE_CODE,A.POSE_NAME FROM TC_POSE A WHERE A.POSE_BUS_TYPE != "+Constant.POSE_BUS_TYPE_WR+" AND  A.POSE_TYPE = '"
				+ Constant.SYS_USER_SGM
				+ "' AND A.POSE_STATUS = '"
				+ Constant.STATUS_ENABLE
				+ "' AND A.COMPANY_ID = '"
				+ companyId + "' ";
		
		if (poseCode != null && !poseCode.equals("")) {
			query += "  AND A.POSE_CODE LIKE '%" + poseCode + "%'";
		}
		if (poseName != null && !poseName.equals("")) {
			query += "  AND A.POSE_NAME like '%" + poseName + "%'";
		}
		if (poseIds != null && !poseIds.equals("")) {
			query += "  AND A.POSE_ID NOT IN ( " + poseIds + " ) ";
		}
		query += " UNION SELECT A.POSE_ID,A.POSE_CODE,A.POSE_NAME FROM TC_POSE A,TR_USER_POSE B WHERE   A.POSE_BUS_TYPE = "+Constant.POSE_BUS_TYPE_WR+" AND  A.POSE_TYPE = '"
		+ Constant.SYS_USER_SGM
		+ "' AND A.POSE_STATUS = '"
		+ Constant.STATUS_ENABLE
		+ "' AND A.COMPANY_ID = '"
		+ companyId + "' ";
		if (poseCode != null && !poseCode.equals("")) {
			query += "  AND A.POSE_CODE LIKE '%" + poseCode + "%'";
		}
		if (poseName != null && !poseName.equals("")) {
			query += "  AND A.POSE_NAME like '%" + poseName + "%'";
		}
		if (poseIds != null && !poseIds.equals("")) {
			query += "  AND A.POSE_ID NOT IN ( " + poseIds + " ) ";
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
	public static 	List<TcPosePO> getSGMPoseByCompanyId2(String poseCode,Long companyId, String poseName,String poseIds, String poseBusType) throws Exception {
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT a.pose_id, a.pose_code, a.pose_name\n" );
		sql.append("  FROM tc_pose a, tt_sales_logi tl\n" );
		sql.append(" WHERE a.logi_id = tl.logi_id\n");
		sql.append("   AND a.pose_bus_type = "+Constant.POSE_BUS_TYPE_WL+"\n" );
		sql.append("   AND a.pose_type = "+Constant.SYS_USER_SGM+"\n" );
		sql.append("   AND a.pose_status = "+Constant.STATUS_ENABLE+"\n" );
		sql.append("   AND a.company_id = "+companyId+"\n");
		if (poseCode != null && !poseCode.equals("")) {
			sql.append("  AND A.POSE_CODE LIKE '%" + poseCode + "%'\n");
		}
		if (poseName != null && !poseName.equals("")) {
			sql.append("  AND A.POSE_NAME like '%" + poseName + "%'\n");
		}
		if (poseIds != null && !poseIds.equals("")) {
			sql.append("  AND A.POSE_ID IN ( " + poseIds + " )\n");
		}
		sql.append("union\n" );
		sql.append("SELECT a.pose_id, a.pose_code, a.pose_name\n" );
		sql.append("  FROM tc_pose a, tt_sales_logi tl\n" );
		sql.append(" WHERE a.logi_id = tl.logi_id\n");
		sql.append("   AND a.pose_bus_type = "+Constant.POSE_BUS_TYPE_WL+"\n" );
		sql.append("   AND a.pose_type = "+Constant.SYS_USER_SGM+"\n" );
		sql.append("   AND a.pose_status = "+Constant.STATUS_ENABLE+"\n" );
		sql.append("   AND a.company_id = "+companyId+"\n");
		sql.append("   AND "+Constant.POSE_BUS_TYPE_VS+" = "+poseBusType+"\n");
		if (poseCode != null && !poseCode.equals("")) {
			sql.append("  AND A.POSE_CODE LIKE '%" + poseCode + "%'\n");
		}
		if (poseName != null && !poseName.equals("")) {
			sql.append("  AND A.POSE_NAME like '%" + poseName + "%'\n");
		}
		
		
//		logger.debug("SQL+++++++++++++++++++++++: " + query);
		return factory.select(sql.toString(), null, new DAOCallback<TcPosePO>() {
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
	//return 	dao.pageQuery(sql.toString(), null, dao.getFunName(), pageSize, curPage);
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
	public static List<TcPosePO> getSGMPoseByPoseIds(String poseIds,Long companyId) throws Exception {
		String query = " SELECT POSE_ID,POSE_CODE,POSE_NAME FROM TC_POSE WHERE POSE_TYPE = '"
				+ Constant.SYS_USER_SGM
				+ "' AND POSE_STATUS = '"
				+ Constant.STATUS_ENABLE
				+ "' and POSE_ID IN ("
				+ poseIds
				+ ") AND COMPANY_ID = '" + companyId + "' ";

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
	public static PageResult<TcUserPO> drlSysUserQuery(Long dealerId,
			String deptId, String empNum, String name, int curPage,
			int pageSize, String orderName, String da) throws Exception {
		String query = " SELECT DISTINCT TU.USER_ID,TU.EMP_NUM,TU.NAME,TU.USER_STATUS FROM TC_USER TU,TM_COMPANY TPC "
				+ " WHERE TPC.COMPANY_ID = TU.COMPANY_ID AND TPC.company_stat = '" + Constant.STATUS_ENABLE + "' AND "
				+ "TU.COMPANY_ID ='" + dealerId + "' ";
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
		return getPoseByUserId(factory.pageQuery(query, null,
				new DAOCallback<TcUserPO>() {
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
	public static PageResult<TcUserPO> sgmDrlSysUserQuery(String companyId,Long oemCompanyId,
			String deptId, String acnt, String name, String poseCode, int curPage,
			int pageSize, String orderName, String da) throws Exception {
		String query = " SELECT DISTINCT TU.USER_ID,COM.COMPANY_SHORTNAME,TU.ACNT,TU.NAME,TU.USER_STATUS,TP.POSE_BUS_TYPE FROM TC_USER TU,TR_USER_POSE UP,TC_POSE TP, "
				+ " TM_COMPANY COM WHERE COM.COMPANY_ID = TU.COMPANY_ID AND  TU.USER_ID = UP.USER_ID(+) "
				+ " AND UP.POSE_ID = TP.POSE_ID(+) "
				+ " and COM.STATUS(+) = " + Constant.STATUS_ENABLE + " AND COM.COMPANY_TYPE <> " + Constant.COMPANY_TYPE_SGM;
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
		query += "ORDER BY TU.USER_STATUS, TP.POSE_BUS_TYPE, TU.ACNT DESC\n" ;


		query = OrderUtil.addOrderBy(query, orderName, da);
		logger.debug("SQL+++++++++++++++++++++++: " + query);
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
						} catch (SQLException e) {
							e.printStackTrace();
						}
						return bean;
					}
				}, curPage, pageSize));
	}
	
	public static PageResult<Map<String,Object>> sgmDrlSysUserQueryA(String dealerId,Long oemCompanyId,
			String deptId, String acnt, String name, String poseCode, int curPage,
			int pageSize, String orderName, String da) throws Exception {
		String query = " SELECT DISTINCT TU.USER_ID,COM.COMPANY_SHORTNAME,TU.ACNT,TU.NAME,TU.USER_STATUS,TP.POSE_BUS_TYPE, TCUS.NAME UPDATE_NAME, TO_CHAR(TU.UPDATE_DATE, 'YYYY-MM-DD') UPDATE_DATE FROM TC_USER TU,TR_USER_POSE UP,TC_POSE TP, TM_DEALER TD,\n"
				+ " TM_COMPANY COM, TC_USER TCUS WHERE TU.DEALER_ID = TD.DEALER_ID AND TU.UPDATE_BY = TCUS.USER_ID(+) AND COM.COMPANY_ID = TD.COMPANY_ID AND  TU.USER_ID = UP.USER_ID(+) "
				+ " AND UP.POSE_ID = TP.POSE_ID(+) \n"
				+ " and COM.STATUS(+) = " + Constant.STATUS_ENABLE + " AND COM.COMPANY_TYPE <> " + Constant.COMPANY_TYPE_SGM;
		if (dealerId != null && !"".equals(dealerId)) {
			query += "  AND td.dealer_ID ='" + dealerId + "'";
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
		query += "ORDER BY TU.USER_STATUS, TP.POSE_BUS_TYPE, TU.ACNT DESC\n" ;


		query = OrderUtil.addOrderBy(query, orderName, da);
		logger.debug("SQL+++++++++++++++++++++++: " + query);
		return getPoseByUserIdA(dao.pageQuery(query, null, dao.getFunName(), curPage, pageSize)) ;
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

	public static PageResult<TmDealerPO> getDRLByDeptId(String orgId,String dcode,String dsname,Long companyId,int curPage,
			int pageSize, String orderName, String da) throws Exception {
		List<TmOrgPO> list = new ArrayList<TmOrgPO>();
		List<TmOrgPO> zDept = getXjbm(orgId,companyId, list);
		String indept = "";
		if(zDept != null && zDept.size()>0) {
			for (int i = 0; i < zDept.size(); i++) {
				indept += zDept.get(i).getOrgId()+",";
			}
			indept = indept.substring(0, indept.length()-1)+","+orgId;
		}
		
		if("".equals(indept)) {
			indept = String.valueOf(orgId);
		}

		logger.error("indept==================:  " + indept);
		StringBuffer query = new StringBuffer();
		//modify by xiayanpeng begin 经销商表修改DEALER_NAME 
		query.append(" SELECT A.DEALER_CODE, A.DEALER_ID, A.DEALER_NAME DEALER_NAME FROM TM_DEALER a,TM_DEALER_ORG_RELATION REL ");
		//modify by xiayanpeng end 
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
					//modify by xiayanpeng begin 经销商表修改DEALER_NAME 
					bean.setDealerName(rs.getString("DEALER_NAME"));
					//modify by xiayanpeng end 
				} catch (SQLException e) {
					e.printStackTrace();
				}
				return bean;
			}
		}, curPage, pageSize);
	}
	
	
	
	/*public static PageResult<TmDealerPO> getAllDRLByDeptId(String orgId,String dcode,String dsname,Long companyId,String provinceId,String dealerClass,int curPage,
			int pageSize, String orderName, String da,String inputOrgId,Integer poseBusType,Long poseId, String isAllLevel) throws Exception {
		List<TmOrgPO> list = new ArrayList<TmOrgPO>();
		//modified by andy.ten@tom.com begin
		//List<TmOrgPO> zDept = getXjbm(orgId,companyId, list);
//		if(zDept != null && zDept.size()>0) {
//			for (int i = 0; i < zDept.size(); i++) {
//				s_orgId += zDept.get(i).getOrgId()+",";
//			}
//			s_orgId = s_orgId.substring(0, s_orgId.length()-1)+","+orgId;
//		}
//		
//		if("".equals(s_orgId)) {
//			s_orgId = String.valueOf(orgId);
//		}

		StringBuffer query = new StringBuffer();
		query.append(" SELECT A.DEALER_CODE, A.DEALER_ID, A.DEALER_NAME,A.DEALER_SHORTNAME FROM TM_DEALER a,TM_DEALER_ORG_RELATION REL ");
		query.append(" where   A.STATUS = ");
		query.append(Constant.STATUS_ENABLE);
		query.append(" AND REL.DEALER_ID =\n");
		query.append("    (SELECT TD.DEALER_ID\n");  
		query.append("       FROM TM_DEALER TD\n");  
		query.append("      WHERE TD.DEALER_LEVEL = "+Constant.DEALER_LEVEL_01+"\n");  
		query.append("      START WITH TD.DEALER_ID = A.DEALER_ID\n");  
		query.append("     CONNECT BY PRIOR TD.PARENT_DEALER_D = TD.DEALER_ID)");

		query.append(" AND (REL.DEALER_ID = A.DEALER_ID OR REL.DEALER_ID = A.PARENT_DEALER_D)");
		query.append(" AND A.OEM_COMPANY_ID = "+companyId+"\n");
		if (orgId != null && !orgId.equals("")) {
			query.append(" AND REL.ORG_ID IN (");
			query.append(" SELECT ORG_ID FROM TM_ORG ORG WHERE ORG.STATUS = " + Constant.STATUS_ENABLE);
			query.append(" START WITH ORG.ORG_ID = " + orgId);
			query.append(" CONNECT BY PRIOR ORG.ORG_ID = ORG.PARENT_ORG_ID) ");
		}else //modify by zjy 接收页面操作人自己输入的组织ID
		{
			if(inputOrgId != null && !"".equals(inputOrgId)&&!"null".equals(inputOrgId)){
			query.append(" AND REL.ORG_ID IN (");
			query.append(" SELECT ORG_ID FROM TM_ORG ORG WHERE ORG.STATUS = " + Constant.STATUS_ENABLE);
			query.append(" START WITH ORG.ORG_ID = " + inputOrgId);
			query.append(" CONNECT BY PRIOR ORG.ORG_ID = ORG.PARENT_ORG_ID) ");
			}
		}
		if (dcode != null && !dcode.equals("")) {
			query.append(" AND A.DEALER_CODE LIKE '%");
			query.append(dcode);
			query.append("%' ");
		}
		if (dsname != null && !dsname.equals("")) {
			query.append(" and A.DEALER_NAME like '%");
			query.append(dsname);
			query.append("%' ");
		}
		if (provinceId != null && !"".equals(provinceId)) {
			query.append(" AND A.PROVINCE_ID = '"+ provinceId +"' \n");
		}
		if (dealerClass != null && !"".equals(dealerClass)) {
			query.append(" AND A.DEALER_CLASS = '"+ dealerClass +"' \n");
		}
		if (isAllLevel.equals("false")) {
			query.append(" AND A.DEALER_LEVEL = "+ Constant.DEALER_LEVEL_01 +" \n");
		}
		//modify by zhaojinyu 2010-06-12 经销商树中售后只查询售后的经销商，销售只查询销售的经销商。
		if(Constant.POSE_BUS_TYPE_WR==poseBusType||Constant.POSE_BUS_TYPE_DWR==poseBusType){
			query.append(" and A.DEALER_TYPE ="+Constant.DEALER_TYPE_DWR+" \n");
		}else if(Constant.POSE_BUS_TYPE_VS==poseBusType||Constant.POSE_BUS_TYPE_DVS==poseBusType)
		{
			query.append(" and A.DEALER_TYPE IN ("+Constant.DEALER_TYPE_DVS+", "+Constant.DEALER_TYPE_QYZDL+", "+Constant.DEALER_TYPE_JSZX+") \n");
		}
		//end
		// ADD BY YUYONG 2010.8.30 增加业务范围过滤
		String areaIds = MaterialGroupManagerDao.getAreaIdsByPoseId(poseId);

		query.append("  AND EXISTS\n");
		query.append("(SELECT TDBA.DEALER_ID\n");  
		query.append("         FROM TM_DEALER_BUSINESS_AREA TDBA\n");  
		query.append("        WHERE A.DEALER_ID = TDBA.DEALER_ID\n");  
		query.append("          AND TDBA.AREA_ID IN\n");  
		query.append("              ("+areaIds+"))");

		//query.append(" AND A.DEALER_ID IN (SELECT TDBA.DEALER_ID FROM TM_DEALER_BUSINESS_AREA TDBA WHERE TDBA.AREA_ID IN ("+areaIds+")) \n");
		query.append(" ORDER BY A.DEALER_CODE,A.DEALER_NAME \n") ;
		logger.info("SQL: " + query);
		return factory.pageQuery(query.toString(), null, new DAOCallback<TmDealerPO>() {
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
	}*/
	public static PageResult<TmDealerPO> getNowDownDRLByDeptId(String dealerIds,String orgId,String dcode,String dsname,Long companyId,String provinceId,String dealerClass,int curPage,
			int pageSize, String orderName, String da,String inputOrgId,Integer poseBusType,Long poseId, String isAllLevel, String isAllArea) throws Exception {

		StringBuffer sql = new StringBuffer();
		sql.append("SELECT A.DEALER_CODE, A.DEALER_ID, A.DEALER_NAME, A.DEALER_SHORTNAME\n");
		sql.append("  FROM TM_DEALER a\n");  
		sql.append(" where A.STATUS = "+Constant.STATUS_ENABLE+"\n");  
		sql.append("   AND A.OEM_COMPANY_ID = "+companyId+"\n");  
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
			sql.append("   AND A.PROVINCE_ID = '"+ provinceId +"' \n");
		}
		if (dealerClass != null && !"".equals(dealerClass)) {
			sql.append("   AND A.DEALER_CLASS = '"+ dealerClass +"' \n");
		}
//		if (isAllLevel.toLowerCase().equals("false")) {
//			sql.append("   AND A.DEALER_LEVEL = "+ Constant.DEALER_LEVEL_01 +" \n");
//		}
		sql.append("AND A.PARENT_DEALER_D IN("+dealerIds+")");
		//modify by zhaojinyu 2010-06-12 经销商树中售后只查询售后的经销商，销售只查询销售的经销商。
		if(Constant.POSE_BUS_TYPE_WR==poseBusType||Constant.POSE_BUS_TYPE_DWR==poseBusType){
			sql.append("   and A.DEALER_TYPE ="+Constant.DEALER_TYPE_DWR+" \n");
		}
		else if(Constant.POSE_BUS_TYPE_VS==poseBusType||Constant.POSE_BUS_TYPE_DVS==poseBusType||Constant.POSE_BUS_TYPE_WL==poseBusType){
			sql.append("   and A.DEALER_TYPE IN ("+Constant.DEALER_TYPE_DVS+", "+Constant.DEALER_TYPE_QYZDL+", "+Constant.DEALER_TYPE_JSZX+") \n");
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
			sql.append("                              WHERE ORG.STATUS = "+Constant.STATUS_ENABLE+"\n");  
			sql.append("                              START WITH ORG.ORG_ID = "+orgId+"\n");  
			sql.append("                             CONNECT BY PRIOR ORG.ORG_ID = ORG.PARENT_ORG_ID))\n");  
			sql.append("        CONNECT BY PRIOR TD.DEALER_ID = td.parent_dealer_d)\n");  
		}else{
			if(inputOrgId != null && !"".equals(inputOrgId)&&!"null".equals(inputOrgId)){
				sql.append("   and a.dealer_id in\n");  
				sql.append("       (select td.dealer_id\n");  
				sql.append("          from tm_dealer td\n");  
				sql.append("         START WITH TD.DEALER_ID in\n");  
				sql.append("                    (select tdor.dealer_id\n");  
				sql.append("                       from tm_dealer_org_relation tdor\n");  
				sql.append("                      where tdor.org_id in\n");  
				sql.append("                            (SELECT ORG_ID\n");  
				sql.append("                               FROM TM_ORG ORG\n");  
				sql.append("                              WHERE ORG.STATUS = "+Constant.STATUS_ENABLE+"\n");  
				sql.append("                              START WITH ORG.ORG_ID = "+inputOrgId+"\n");  
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
			sql.append("               ("+areaIds+"))\n");  
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
	
	public static PageResult<TmDealerPO> getAllDRLByDeptIdForZotye(String areaId,String isDealerType,String orgId,String dcode,String dsname,Long companyId,String provinceId,String dealerClass,String dealerType,int curPage,
			int pageSize, String orderName, String da,String inputOrgId,Integer poseBusType,Long poseId, String isAllLevel, String isAllArea, String isLogoutDealerClass, AclUserBean logonUser) throws Exception {
		List par=new ArrayList<Object>();
		StringBuffer sbSql=new StringBuffer();
		sbSql.append("  SELECT distinct  A.DEALER_CODE,\n");
		sbSql.append("           A.DEALER_ID,\n");
		sbSql.append("           A.DEALER_TYPE,\n");
		sbSql.append("           A.DEALER_NAME,\n");
		sbSql.append("           A.DEALER_SHORTNAME\n");
		sbSql.append("    FROM   TM_DEALER a, VW_ORG_DEALER_SERVICE d\n");
		sbSql.append("   WHERE   a.dealer_id = d.dealer_id and A.STATUS = "+Constant.STATUS_ENABLE+"\n");
		sbSql.append("           AND A.OEM_COMPANY_ID = ?\n");
		par.add(new Long(companyId));
		
		// 根据用户职位类型和权限限定用户查询数据范围
//		if(logonUser.getDealerId() != null) {
//			sbSql.append(" and a.dealer_id = " + logonUser.getDealerId());
//		} else if(logonUser.getDutyType().equals(Constant.DUTY_TYPE_LARGEREGION)) {
//			MaterialGroupManagerDao.getOrgDealerLimitSql("a.dealer_id", logonUser);
//		}
		
		if(logonUser.getDutyType().equals(Constant.DUTY_TYPE_LARGEREGION.toString())) {
			sbSql.append(MaterialGroupManagerDao.getOrgDealerLimitSql("a.dealer_id", logonUser));
		}
		
		if(areaId != null && !areaId.equals("") && !areaId.equals("null")){
			sbSql.append(Utility.getConSqlByParamForEqual(areaId,par,"TDBA","AREA_ID"));
		}
		
		if (dcode != null && !dcode.equals("")) {
			sbSql.append("   AND A.DEALER_CODE LIKE '%");
			sbSql.append(dcode);
			sbSql.append("%' ");
		}
		if (dsname != null && !dsname.equals("")) {
			sbSql.append("   and A.DEALER_NAME like '%");
			sbSql.append(dsname);
			sbSql.append("%' ");
		}
		if (provinceId != null && !"".equals(provinceId)) {
			sbSql.append("   AND A.PROVINCE_ID = ?\n");
			par.add(provinceId);
		}
		if (dealerClass != null && !"".equals(dealerClass)) {
			sbSql.append("   AND (A.DEALER_CLASS IS NOT NULL AND A.DEALER_CLASS = ?) \n");
			par.add(dealerClass);
		}
		if (dealerType != null && !"".equals(dealerType)) {
			sbSql.append("   AND A.DEALER_type = ? \n");
			par.add(dealerType);
		}
		
		if (isAllLevel != null && !"".equals(isAllLevel)) {
			sbSql.append("   AND A.DEALER_LEVEL = ? \n");
			par.add(isAllLevel);
		}
		
		//add by hxy 2012-04-23 新增是否为退网类经销商评级
		if(isLogoutDealerClass != null && !"".equals(isLogoutDealerClass)) {
			sbSql.append("	AND A.DEALER_CLASS <> 11451013\n");
		}
		
		if(!isDealerType.equals("null") && isDealerType!=""){
			sbSql.append("and A.DEALER_TYPE IN ("+isDealerType+")\n");
		}
		if (inputOrgId != null && !inputOrgId.equals("")&& !inputOrgId.equals("null")) {
			sbSql.append("and  d.root_org_id = ?\n");
			par.add(Long.valueOf(inputOrgId));
		}
		if (orgId != null && !orgId.equals("")) {
			sbSql.append("and (d.org_id = ? or d.root_org_id = ? or\n") ;
			sbSql.append("       1 = (select 1\n") ;
			sbSql.append("           from tm_org trg\n") ;
			sbSql.append("          where trg.org_id = ?\n") ;
			sbSql.append("            and trg.parent_org_id = -1))\n") ;

			par.add(Long.valueOf(orgId));
			par.add(Long.valueOf(orgId));
			par.add(Long.valueOf(orgId));
		}
		
		sbSql.append("ORDER BY   A.DEALER_CODE, A.DEALER_NAME\n");
		
		return factory.pageQuery(sbSql.toString(), par, new DAOCallback<TmDealerPO>() {
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
	
	public static PageResult<TmDealerPO> getAllDRLByDeptId(String areaId,String isDealerType,String orgId,String dcode,String dsname,Long companyId,String provinceId,String dealerClass,String dealerType,int curPage,
			int pageSize, String orderName, String da,String inputOrgId,Integer poseBusType,Long poseId, String isAllLevel, String isAllArea, String isLogoutDealerClass) throws Exception {

//		StringBuffer sql = new StringBuffer();
//		sql.append("SELECT DISTINCT A.DEALER_CODE, A.DEALER_ID,A.DEALER_TYPE, A.DEALER_NAME, A.DEALER_SHORTNAME\n");
//		sql.append("  FROM TM_DEALER a,TM_DEALER_BUSINESS_AREA TDBA\n");  
//		sql.append(" where A.STATUS = "+Constant.STATUS_ENABLE+"\n");  
//		sql.append("   AND A.OEM_COMPANY_ID = "+companyId+"\n");  
//		sql.append(" AND TDBA.DEALER_ID=A.DEALER_ID\n");
//		if(areaId != null && !areaId.equals("") && !areaId.equals("null")){
//			sql.append("AND TDBA.AREA_ID IN ("+areaId+")\n");
//		}
//		if (dcode != null && !dcode.equals("")) {
//			sql.append("   AND A.DEALER_CODE LIKE '%");
//			sql.append(dcode);
//			sql.append("%' ");
//		}
//		if (dsname != null && !dsname.equals("")) {
//			sql.append("   and A.DEALER_NAME like '%");
//			sql.append(dsname);
//			sql.append("%' ");
//		}
//		if (provinceId != null && !"".equals(provinceId)) {
//			sql.append("   AND A.PROVINCE_ID = '"+ provinceId +"' \n");
//		}
//		if (dealerClass != null && !"".equals(dealerClass)) {
//			sql.append("   AND (A.DEALER_CLASS IS NOT NULL OR A.DEALER_CLASS = '"+ dealerClass +"') \n");
//		}
//		if (dealerType != null && !"".equals(dealerType)) {
//			sql.append("   AND A.DEALER_type = '"+ dealerType +"' \n");
//		}
//		if (isAllLevel.toLowerCase().equals("false")) {
//			sql.append("   AND A.DEALER_LEVEL = "+ Constant.DEALER_LEVEL_01 +" \n");
//		}
//		
//		//add by hxy 2012-04-23 新增是否为退网类经销商评级
//		if(isLogoutDealerClass != null && !"".equals(isLogoutDealerClass)) {
//			sql.append("	AND A.DEALER_CLASS <> 11451013\n");
//		}
//		
//		//modify by zhaojinyu 2010-06-12 经销商树中售后只查询售后的经销商，销售只查询销售的经销商。
//		if(isDealerType.equals("null")){
//			isDealerType="";
//		}
//		if(isDealerType.equals("")){
//		if(Constant.POSE_BUS_TYPE_WR==poseBusType||Constant.POSE_BUS_TYPE_DWR==poseBusType){
//			sql.append("   and A.DEALER_TYPE ="+Constant.DEALER_TYPE_DWR+" \n");
//		}
//		else if( Constant.POSE_BUS_TYPE_VS==poseBusType||Constant.POSE_BUS_TYPE_DVS==poseBusType){
//			sql.append("   and A.DEALER_TYPE IN ("+Constant.DEALER_TYPE_DVS+", "+Constant.DEALER_TYPE_QYZDL+", "+Constant.DEALER_TYPE_JSZX+") \n");
//		}
//		}
//		if(!isDealerType.equals("null") && isDealerType!=""){
//			sql.append("and A.DEALER_TYPE IN ("+isDealerType+")");
//		}
//		if (orgId != null && !orgId.equals("")) {
//			sql.append("   and a.dealer_id in\n");  
//			sql.append("       (select td.dealer_id\n");  
//			sql.append("          from tm_dealer td\n");  
//			sql.append("         START WITH TD.DEALER_ID in\n");  
//			sql.append("                    (select tdor.dealer_id\n");  
//			sql.append("                       from tm_dealer_org_relation tdor\n");  
//			sql.append("                      where tdor.org_id in\n");  
//			sql.append("                            (SELECT ORG_ID\n");  
//			sql.append("                               FROM TM_ORG ORG\n");  
//			sql.append("                              WHERE ORG.STATUS = "+Constant.STATUS_ENABLE+"\n");  
//			sql.append("                              START WITH ORG.ORG_ID = "+orgId+"\n");  
//			sql.append("                             CONNECT BY PRIOR ORG.ORG_ID = ORG.PARENT_ORG_ID))\n");  
//			sql.append("        CONNECT BY PRIOR TD.DEALER_ID = td.parent_dealer_d)\n");  
//		}else{
//			if(inputOrgId != null && !"".equals(inputOrgId)&&!"null".equals(inputOrgId)){
//				sql.append("   and a.dealer_id in\n");  
//				sql.append("       (select td.dealer_id\n");  
//				sql.append("          from tm_dealer td\n");  
//				sql.append("         START WITH TD.DEALER_ID in\n");  
//				sql.append("                    (select tdor.dealer_id\n");  
//				sql.append("                       from tm_dealer_org_relation tdor\n");  
//				sql.append("                      where tdor.org_id in\n");  
//				sql.append("                            (SELECT ORG_ID\n");  
//				sql.append("                               FROM TM_ORG ORG\n");  
//				sql.append("                              WHERE ORG.STATUS = "+Constant.STATUS_ENABLE+"\n");  
//				sql.append("                              START WITH ORG.ORG_ID = "+inputOrgId+"\n");  
//				sql.append("                             CONNECT BY PRIOR ORG.ORG_ID = ORG.PARENT_ORG_ID))\n");  
//				sql.append("        CONNECT BY PRIOR TD.DEALER_ID = td.parent_dealer_d)\n"); 
//			}
//		}
//		
//		if (isAllArea.toLowerCase().equals("false")) {
//			// ADD BY YUYONG 2010.8.30 增加业务范围过滤
//			String areaIds = MaterialGroupManagerDao.getAreaIdsByPoseId(poseId);
//			sql.append("   AND EXISTS\n");  
//			sql.append(" (SELECT TDBA.DEALER_ID\n");  
//			sql.append("          FROM TM_DEALER_BUSINESS_AREA TDBA\n");  
//			sql.append("         WHERE A.DEALER_ID = TDBA.DEALER_ID\n");  
//			sql.append("           AND TDBA.AREA_ID IN\n");  
//			sql.append("               ("+areaIds+"))\n");  
//			sql.append(" ORDER BY A.DEALER_CODE, A.DEALER_NAME");
//		}
//
//		logger.info("SQL: " + sql);
		List par=new ArrayList<Object>();
		StringBuffer sbSql=new StringBuffer();
		sbSql.append("  SELECT distinct  A.DEALER_CODE,\n");
		sbSql.append("           A.DEALER_ID,\n");
		sbSql.append("           A.DEALER_TYPE,\n");
		sbSql.append("           A.DEALER_NAME,\n");
		sbSql.append("           A.DEALER_SHORTNAME\n");
		sbSql.append("    FROM   TM_DEALER a, VW_ORG_DEALER_SERVICE d\n");
		sbSql.append("   WHERE   a.dealer_id = d.dealer_id and A.STATUS = "+Constant.STATUS_ENABLE+"\n");
		sbSql.append("           AND A.OEM_COMPANY_ID = ?\n");
		par.add(new Long(companyId));
		
		if(areaId != null && !areaId.equals("") && !areaId.equals("null")){
			sbSql.append(Utility.getConSqlByParamForEqual(areaId,par,"TDBA","AREA_ID"));
		}
		DlrInfoMngDAO dlrDAO = new DlrInfoMngDAO();
		if (dcode != null && !dcode.equals("")) {
			sbSql.append("   AND (A.DEALER_CODE LIKE ?");
			par.add("%"+dcode+"%");
			String hisSql = dlrDAO.queryHisDealerIdsByCodeSql(dcode);
			if(hisSql!=null){
				sbSql.append(" or a.DEALER_ID IN (");
				sbSql.append(hisSql);
				sbSql.append(" ) ");
			}
			sbSql.append(" ) ");
		}
		if (dsname != null && !dsname.equals("")) {
			sbSql.append("   and (A.DEALER_NAME like ? or A.pinyin like ?");
			par.add("%"+dsname+"%");
			par.add("%"+dsname+"%");
			String hisSql = dlrDAO.queryHisDealerIdsByNameSql(dsname);
			if(hisSql!=null){
				sbSql.append(" or a.DEALER_ID IN (");
				sbSql.append(hisSql);
				sbSql.append(" ) ");
			}
			sbSql.append(" ) ");
		}
		if (provinceId != null && !"".equals(provinceId)) {
			sbSql.append("   AND A.PROVINCE_ID = ?\n");
			par.add(provinceId);
		}
		if (dealerClass != null && !"".equals(dealerClass)) {
			sbSql.append("   AND (A.DEALER_CLASS IS NOT NULL AND A.DEALER_CLASS = ?) \n");
			par.add(dealerClass);
		}
		if (dealerType != null && !"".equals(dealerType)) {
			sbSql.append("   AND A.DEALER_type = ? \n");
			par.add(dealerType);
		}
		//if (isAllLevel.toLowerCase().equals("false")) {
			sbSql.append("   AND A.DEALER_LEVEL = ? \n");
			par.add(Constant.DEALER_LEVEL_01);
		//}
		sbSql.append("   AND A.DEALER_LEVEL = 10851001 \n");
		//add by hxy 2012-04-23 新增是否为退网类经销商评级
		if(isLogoutDealerClass != null && !"".equals(isLogoutDealerClass)) {
			sbSql.append("	AND A.DEALER_CLASS <> 11451013\n");
		}
		
		//modify by zhaojinyu 2010-06-12 经销商树中售后只查询售后的经销商，销售只查询销售的经销商。
		if(isDealerType.equals("null")){
			isDealerType="";
		}
		if(isDealerType.equals("")){
			if(Constant.POSE_BUS_TYPE_WR==poseBusType||Constant.POSE_BUS_TYPE_DWR==poseBusType){
				sbSql.append("   and A.DEALER_TYPE ="+Constant.DEALER_TYPE_DWR+" \n");
			}
			else if( Constant.POSE_BUS_TYPE_VS==poseBusType||Constant.POSE_BUS_TYPE_DVS==poseBusType||Constant.POSE_BUS_TYPE_WL==poseBusType){
				sbSql.append("   and A.DEALER_TYPE IN ("+Constant.DEALER_TYPE_DVS+", "+Constant.DEALER_TYPE_QYZDL+", "+Constant.DEALER_TYPE_JSZX+") \n");
			}
		}
		if(!isDealerType.equals("null") && isDealerType!=""){
			sbSql.append("and A.DEALER_TYPE IN ("+isDealerType+")\n");
		}
		if (inputOrgId != null && !inputOrgId.equals("")&& !inputOrgId.equals("null")) {
			sbSql.append("and  d.root_org_id = ?\n");
			par.add(Long.valueOf(inputOrgId));
		}
		if (orgId != null && !orgId.equals("")) {
			sbSql.append("and (d.org_id = ? or d.root_org_id = ?)\n");
			par.add(Long.valueOf(orgId));
			par.add(Long.valueOf(orgId));
		}
		
			sbSql.append("ORDER BY   A.DEALER_CODE, A.DEALER_NAME\n");
		//}

		
		
		
		return factory.pageQuery(sbSql.toString(), par, new DAOCallback<TmDealerPO>() {
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
	
	public static PageResult<TmDealerPO> getAllDRLByDeptIdForSCSJ(String areaId,String isDealerType,String orgId,String dcode,String dsname,Long companyId,String provinceId,String dealerClass,String dealerType,int curPage,
			int pageSize, String orderName, String da,String inputOrgId,Integer poseBusType,Long poseId, String isAllLevel, String isAllArea, String isLogoutDealerClass,String orgCode) throws Exception {

		List par=new ArrayList<Object>();
		StringBuffer sbSql=new StringBuffer();
		sbSql.append("  SELECT   A.DEALER_CODE,\n");
		sbSql.append("           A.DEALER_ID,\n");
		sbSql.append("           A.DEALER_TYPE,\n");
		sbSql.append("           A.DEALER_NAME,\n");
		sbSql.append("           A.DEALER_SHORTNAME\n");
		sbSql.append("    FROM   TM_DEALER a, VW_ORG_DEALER_SERVICE d\n");
		sbSql.append("   WHERE   a.dealer_id = d.dealer_id and A.STATUS = "+Constant.STATUS_ENABLE+"\n");
		sbSql.append("           AND A.OEM_COMPANY_ID = ?\n");
		par.add(new Long(companyId));
		
		if(areaId != null && !areaId.equals("") && !areaId.equals("null")){
			sbSql.append(Utility.getConSqlByParamForEqual(areaId,par,"TDBA","AREA_ID"));
		}
		if (dcode != null && !dcode.equals("")) {
			sbSql.append("   AND A.DEALER_CODE LIKE ?");
			par.add("%"+dcode+"%");
		}
		if (dsname != null && !dsname.equals("")) {
			sbSql.append("   and A.DEALER_NAME like ?");
			par.add("%"+dsname+"%");
		}
		if (provinceId != null && !"".equals(provinceId)) {
			sbSql.append("   AND A.PROVINCE_ID = ?\n");
			par.add(provinceId);
		}
		if (dealerClass != null && !"".equals(dealerClass)) {
			sbSql.append("   AND (A.DEALER_CLASS IS NOT NULL AND A.DEALER_CLASS = ?) \n");
			par.add(dealerClass);
		}
		if (dealerType != null && !"".equals(dealerType)) {
			sbSql.append("   AND A.DEALER_type = ? \n");
			par.add(dealerType);
		}
		if (isAllLevel.toLowerCase().equals("false")) {
			sbSql.append("   AND A.DEALER_LEVEL = ? \n");
			par.add(Constant.DEALER_LEVEL_01);
		}
		
		//add by hxy 2012-04-23 新增是否为退网类经销商评级
		if(isLogoutDealerClass != null && !"".equals(isLogoutDealerClass)) {
			sbSql.append("	AND A.DEALER_CLASS <> 11451013\n");
		}
		
		//modify by zhaojinyu 2010-06-12 经销商树中售后只查询售后的经销商，销售只查询销售的经销商。
		if(isDealerType.equals("null")){
			isDealerType="";
		}
		if(isDealerType.equals("")){
			if(Constant.POSE_BUS_TYPE_WR==poseBusType||Constant.POSE_BUS_TYPE_DWR==poseBusType){
				sbSql.append("   and A.DEALER_TYPE ="+Constant.DEALER_TYPE_DWR+" \n");
			}
			else if( Constant.POSE_BUS_TYPE_VS==poseBusType||Constant.POSE_BUS_TYPE_DVS==poseBusType||Constant.POSE_BUS_TYPE_WL==poseBusType){
				sbSql.append("   and A.DEALER_TYPE IN ("+Constant.DEALER_TYPE_DVS+", "+Constant.DEALER_TYPE_QYZDL+", "+Constant.DEALER_TYPE_JSZX+") \n");
			}
		}
		if(!isDealerType.equals("null") && isDealerType!=""){
			sbSql.append("and A.DEALER_TYPE IN ("+isDealerType+")\n");
		}
		
		if (orgId != null && !orgId.equals("")) {
			sbSql.append("and (d.org_id = ? or d.root_org_id = ?)\n");
			par.add(Long.valueOf(orgId));
			par.add(Long.valueOf(orgId));
		}
		
		if (orgCode != null && !orgCode.equals("")) {
			sbSql.append("and d.org_code  like '%").append(orgCode).append("%'");
		}
			sbSql.append("AND A.SERVICE_STATUS = 13691002\n");
			sbSql.append("ORDER BY   A.DEALER_CODE, A.DEALER_NAME\n");
		//}

		
		
		
		return factory.pageQuery(sbSql.toString(), par, new DAOCallback<TmDealerPO>() {
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
	 * 新闻发布用
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
	 * @param dealerLevel
	 * @return
	 * @throws Exception
	 */
	public static PageResult<TmDealerPO> getAllDRLByDeptIdByNews(String areaId,String isDealerType,String orgId,String dcode,String dsname,Long companyId,String provinceId,String dealerClass,String dealerType,int curPage,
			int pageSize, String orderName, String da,String inputOrgId,Integer poseBusType,Long poseId, String isAllLevel, String isAllArea, String isLogoutDealerClass,String dealerLevel) throws Exception {

//		logger.info("SQL: " + sql);
		List par=new ArrayList<Object>();
		StringBuffer sbSql=new StringBuffer();
		sbSql.append("  SELECT   A.DEALER_CODE,\n");
		sbSql.append("           A.DEALER_ID,\n");
		sbSql.append("           A.DEALER_TYPE,\n");
		sbSql.append("           A.DEALER_NAME,\n");
		sbSql.append("           A.DEALER_SHORTNAME\n");
		sbSql.append("    FROM   TM_DEALER a, VW_ORG_DEALER_SERVICE d\n");
		sbSql.append("   WHERE   a.dealer_id = d.dealer_id and A.STATUS = "+Constant.STATUS_ENABLE+"\n");
		sbSql.append("           AND A.OEM_COMPANY_ID = ?\n");
		par.add(new Long(companyId));
		
		if(areaId != null && !areaId.equals("") && !areaId.equals("null")){
			sbSql.append(Utility.getConSqlByParamForEqual(areaId,par,"TDBA","AREA_ID"));
		}
		if (dcode != null && !dcode.equals("")) {
			sbSql.append("   AND A.DEALER_CODE LIKE ?");
			par.add("%"+dcode+"%");
		}
		if (dsname != null && !dsname.equals("")) {
			sbSql.append("   and A.DEALER_NAME like ?");
			par.add("%"+dsname+"%");
		}
		if (provinceId != null && !"".equals(provinceId)) {
			sbSql.append("   AND A.PROVINCE_ID = ?\n");
			par.add(provinceId);
		}
		if (dealerClass != null && !"".equals(dealerClass)) {
			sbSql.append("   AND (A.DEALER_CLASS IS NOT NULL AND A.DEALER_CLASS = ?) \n");
			par.add(dealerClass);
		}
		if (dealerType != null && !"".equals(dealerType)) {
			sbSql.append("   AND A.DEALER_type = ? \n");
			par.add(dealerType);
		}
		if (isAllLevel.toLowerCase().equals("false")) {
			sbSql.append("   AND A.DEALER_LEVEL = ? \n");
			par.add(Constant.DEALER_LEVEL_01);
		}
		if(dealerLevel!=null&&!"".equals(dealerLevel)){
			sbSql.append("   AND A.DEALER_LEVEL = ? \n");
			par.add(dealerLevel);
		}
		
		//add by hxy 2012-04-23 新增是否为退网类经销商评级
		if(isLogoutDealerClass != null && !"".equals(isLogoutDealerClass)) {
			sbSql.append("	AND A.DEALER_CLASS <> 11451013\n");
		}
		
		//modify by zhaojinyu 2010-06-12 经销商树中售后只查询售后的经销商，销售只查询销售的经销商。
		if(isDealerType.equals("null")){
			isDealerType="";
		}
		if(isDealerType.equals("")){
			if(Constant.POSE_BUS_TYPE_WR==poseBusType||Constant.POSE_BUS_TYPE_DWR==poseBusType){
				sbSql.append("   and A.DEALER_TYPE ="+Constant.DEALER_TYPE_DWR+" \n");
			}
			else if( Constant.POSE_BUS_TYPE_VS==poseBusType||Constant.POSE_BUS_TYPE_DVS==poseBusType||Constant.POSE_BUS_TYPE_WL==poseBusType){
				sbSql.append("   and A.DEALER_TYPE IN ("+Constant.DEALER_TYPE_DVS+", "+Constant.DEALER_TYPE_QYZDL+", "+Constant.DEALER_TYPE_JSZX+") \n");
			}
		}
		if(!isDealerType.equals("null") && isDealerType!=""){
			sbSql.append("and A.DEALER_TYPE IN ("+isDealerType+")\n");
		}
		
		if (orgId != null && !orgId.equals("")) {
			sbSql.append("and (d.org_id = ? or d.root_org_id = ?)\n");
			par.add(Long.valueOf(orgId));
			par.add(Long.valueOf(orgId));
		}
		
			sbSql.append("ORDER BY   A.DEALER_CODE, A.DEALER_NAME\n");
		//}

		
		
		
		return factory.pageQuery(sbSql.toString(), par, new DAOCallback<TmDealerPO>() {
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
	
	//zhumingwei 2011-11-22
	public static PageResult<TmDealerPO> getAllDRLByDeptIdCon(String userId,String areaId,String isDealerType,String orgId,String dcode,String dsname,Long companyId,String provinceId,String dealerClass,int curPage,
			int pageSize, String orderName, String da,String inputOrgId,Integer poseBusType,Long poseId, String isAllLevel, String isAllArea) throws Exception {
		
		StringBuffer sql = new StringBuffer();
		
		TcPosePO pPo = new TcPosePO();
		pPo.setPoseId(poseId);
		TcPosePO pPpValue = (TcPosePO)factory.select(pPo).get(0);
		TmOrgPO po = new TmOrgPO();
		po.setOrgId(pPpValue.getOrgId());
		TmOrgPO poValue = (TmOrgPO)factory.select(po).get(0);
		if(poValue.getDutyType()==10431001){
			sql.append("select d.DEALER_CODE,d.dealer_id,d.dealer_type,d.dealer_name,t.DEALER_SHORTNAME from vw_org_dealer d,tm_dealer t where t.dealer_id=d.dealer_id\n");
		}else{
			sql.append("select d.DEALER_CODE,d.dealer_id,d.dealer_type,d.dealer_name,t.DEALER_SHORTNAME from vw_org_dealer d,tm_dealer t where t.dealer_id=d.dealer_id and d.root_org_id=(select p.org_id from tc_pose p where p.pose_id="+poseId+")\n");
		}
//		System.out.println(sql.toString());
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
	
	//zhumingwei 2011-11-22
	public static PageResult<TmDealerPO> getAllDRLByDeptIdZWCon(String userId,String areaId,String isDealerType,String orgId,String dcode,String dsname,Long companyId,String provinceId,String dealerClass,int curPage,
			int pageSize, String orderName, String da,String inputOrgId,Integer poseBusType,String dealerId, String isAllLevel, String isAllArea) throws Exception {
		DealerKpDao dao  = new DealerKpDao();
		StringBuffer sql = new StringBuffer();
		
		StringBuffer sql1 = new StringBuffer();
		sql1.append("select d.dealer_level from vw_org_dealer d where d.dealer_id in("+dealerId+")");
		List<Map<String, Object>> list = dao.pageQuery(sql1.toString(), null, dao.getFunName());
		
		Long dealerLevel = ((BigDecimal)list.get(0).get("DEALER_LEVEL")).longValue();
		
		if(dealerLevel==10851001){
			System.out.println("aaaaaaaaaaaa");
			sql.append("select d.DEALER_CODE,d.dealer_id,d.dealer_type,d.dealer_name,t.DEALER_SHORTNAME from vw_org_dealer d,tm_dealer t where t.dealer_id=d.dealer_id and d.root_dealer_id in("+dealerId+")\n");
		}else{
			sql.append("select d.DEALER_CODE,d.dealer_id,d.dealer_type,d.dealer_name,t.DEALER_SHORTNAME from vw_org_dealer d,tm_dealer t where t.dealer_id=d.dealer_id and d.dealer_id in ("+dealerId+")\n");
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

	public PageResult<TmDealerPO> getAllDRLByDeptIdReg(String regCode,String areaId,String isDealerType,String orgId,String dcode,String dsname,Long companyId,String provinceId,String dealerClass,int curPage,
			int pageSize, String orderName, String da,String inputOrgId,Integer poseBusType,Long poseId, String isAllLevel, String isAllArea) throws Exception {

		StringBuffer sql = new StringBuffer();
		sql.append("SELECT DISTINCT A.DEALER_CODE, A.DEALER_ID,A.DEALER_TYPE, A.DEALER_NAME, A.DEALER_SHORTNAME\n");
		sql.append("  FROM TM_DEALER a,TM_DEALER_BUSINESS_AREA TDBA\n");  
		sql.append(" where A.STATUS = "+Constant.STATUS_ENABLE+"\n");  
		sql.append("   AND A.OEM_COMPANY_ID = "+companyId+"\n");  
		sql.append(" AND TDBA.DEALER_ID=A.DEALER_ID\n");

		if(regCode != null && !regCode.equals("") && !regCode.equals("null")){
			sql.append("AND A.province_id IN ("+regCode+")\n");
		}
		if(areaId != null && !areaId.equals("") && !areaId.equals("null")){
			sql.append("AND TDBA.AREA_ID IN ("+areaId+")\n");
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
			sql.append("   AND A.PROVINCE_ID = '"+ provinceId +"' \n");
		}
		if (dealerClass != null && !"".equals(dealerClass)) {
			sql.append("   AND A.DEALER_CLASS = '"+ dealerClass +"' \n");
		}
		//modify by zhaojinyu 2010-06-12 经销商树中售后只查询售后的经销商，销售只查询销售的经销商。
		if(isDealerType.equals("null")){
			isDealerType="";
		}
		if(isDealerType.equals("")){
		if(Constant.POSE_BUS_TYPE_WR==poseBusType||Constant.POSE_BUS_TYPE_DWR==poseBusType){
			sql.append("   and A.DEALER_TYPE ="+Constant.DEALER_TYPE_DWR+" \n");
		}
		else if( Constant.POSE_BUS_TYPE_VS==poseBusType||Constant.POSE_BUS_TYPE_DVS==poseBusType||Constant.POSE_BUS_TYPE_WL==poseBusType){
			sql.append("   and A.DEALER_TYPE IN ("+Constant.DEALER_TYPE_DVS+", "+Constant.DEALER_TYPE_QYZDL+", "+Constant.DEALER_TYPE_JSZX+") \n");
		}
		}
		if(!isDealerType.equals("null") && isDealerType!=""){
			sql.append("and A.DEALER_TYPE IN ("+isDealerType+")");
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
			sql.append("                              WHERE ORG.STATUS = "+Constant.STATUS_ENABLE+"\n");  
			sql.append("                              START WITH ORG.ORG_ID = "+orgId+"\n");  
			sql.append("                             CONNECT BY PRIOR ORG.ORG_ID = ORG.PARENT_ORG_ID))\n");  
			sql.append("        CONNECT BY PRIOR TD.DEALER_ID = td.parent_dealer_d)\n");  
		}else{
			if(inputOrgId != null && !"".equals(inputOrgId)&&!"null".equals(inputOrgId)){
				sql.append("   and a.dealer_id in\n");  
				sql.append("       (select td.dealer_id\n");  
				sql.append("          from tm_dealer td\n");  
				sql.append("         START WITH TD.DEALER_ID in\n");  
				sql.append("                    (select tdor.dealer_id\n");  
				sql.append("                       from tm_dealer_org_relation tdor\n");  
				sql.append("                      where tdor.org_id in\n");  
				sql.append("                            (SELECT ORG_ID\n");  
				sql.append("                               FROM TM_ORG ORG\n");  
				sql.append("                              WHERE ORG.STATUS = "+Constant.STATUS_ENABLE+"\n");  
				sql.append("                              START WITH ORG.ORG_ID = "+inputOrgId+"\n");  
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
			sql.append("               ("+areaIds+"))\n");  
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
	public static PageResult<TmOrgPO> getAllOrg(String areaId,String orgId,String orgCode,String orgName,Long companyId,int curPage,
			int pageSize,String inputOrgId,boolean area_flag) throws Exception {
		List<TmOrgPO> list = new ArrayList<TmOrgPO>();
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT TMO.ORG_ID,\n");
		sql.append("       TMO.ORG_CODE,\n");  
		sql.append("       TMO.ORG_NAME,\n");   
		sql.append("       TMO.ORG_DESC,\n");  
		sql.append("       TMO.TREE_CODE,\n");  
		sql.append("       TMO.ORG_LEVEL\n");  
		sql.append("  FROM TM_ORG TMO\n");  
		sql.append(" WHERE TMO.STATUS = "+Constant.STATUS_ENABLE+"\n");
		sql.append(" AND TMO.ORG_TYPE = " + Constant.ORG_TYPE_OEM);
		//sql.append(" AND TOBA.ORG_ID=TMO.ORG_ID");
		if(area_flag){
			sql.append(" AND tmo.org_level=2");  //zxf   选大区级别
		}
		else{
			sql.append(" AND tmo.org_level=3");  //zxf   选大区级别
		}
		sql.append(" AND TMO.COMPANY_ID = ?\n");
		List par=new ArrayList();
		par.add(companyId);
		if(!"".equals(orgCode)&&orgCode!=null)
		{
			sql.append("   AND TMO.ORG_CODE LIKE ?\n");  	
			par.add("%"+orgCode+"%");
		}
//		if(!"".equals(areaId)&&areaId!=null)
//		{
//			sql.append(" AND TOBA.AREA_ID='"+areaId+"'\n");  	
//		}
		
		if(!"".equals(orgName)&&orgName!=null)
		{
			sql.append("   AND TMO.ORG_NAME LIKE ?\n"); 
			par.add("%"+orgName+"%");
		}
		if (orgId != null && !orgId.equals("")) 
	   {
//			sql.append("       AND EXISTS (    SELECT   1\n");
//			sql.append("                       FROM   TM_ORG TTO\n");
//			sql.append("                      WHERE   TTO.STATUS = "
//					+ Constant.STATUS_ENABLE + "\n");
//			sql.append("                              AND TTO.ORG_ID=TMO.ORG_ID\n");
//			sql.append("                 START WITH   TTO.ORG_ID="+orgId);
//
//			sql.append("                 CONNECT BY   PRIOR TTO.ORG_ID = TTO.PARENT_ORG_ID)\n");
			sql.append(" AND EXISTS(");
			sql.append("SELECT   1 \n"); 
			sql.append("  FROM   TM_ORG ORG1, TM_ORG ORG2\n"); 
			sql.append(" WHERE   ORG1.ORG_ID = ORG2.PARENT_ORG_ID AND ORG2.ORG_TYPE = 10191001 AND ORG1.ORG_TYPE=10191001\n");
			sql.append(" AND (ORG1.PARENT_ORG_ID=? OR ORG2.PARENT_ORG_ID=? OR ORG2.ORG_ID=?) AND ORG2.ORG_ID=TMO.ORG_ID");
			sql.append(")\n");
			par.add(orgId);
			par.add(orgId);
			par.add(orgId);
		}else
		{ 
			if(inputOrgId != null && !"".equals(inputOrgId)&&!"null".equals(inputOrgId))
			{
				sql.append("       AND EXISTS (    SELECT   1\n");
				sql.append("                       FROM   TM_ORG TTO\n");
				sql.append("                      WHERE   TTO.STATUS = "
						+ Constant.STATUS_ENABLE + "\n");
				sql.append("                              AND TTO.ORG_ID=TMO.ORG_ID\n");
				sql.append("                 START WITH   TTO.ORG_ID="+inputOrgId);

				sql.append("                 CONNECT BY   PRIOR TTO.ORG_ID = TTO.PARENT_ORG_ID)\n");
		    }
			
		}
		sql.append(" ORDER  BY  TMO.ORG_ID \n");
		//logger.info("SQL: " + sql);
		return factory.pageQuery(sql.toString(), par, new DAOCallback<TmOrgPO>() {
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
	
	//zhumingwei 2011-02-25
	public static PageResult<TmOrgPO> getAllOrg111(String areaId,String orgId,String orgCode,String orgName,Long companyId,int curPage,
			int pageSize,String inputOrgId) throws Exception {
		List<TmOrgPO> list = new ArrayList<TmOrgPO>();
		StringBuffer sql = new StringBuffer();
		sql.append("select * from tm_org o where o.org_type='"+Constant.ORG_TYPE_DEALER+"' and o.duty_type="+Constant.DUTY_TYPE_DEALER+"\n");
		if(!"".equals(orgCode)&&orgCode!=null){
			sql.append("   AND o.ORG_CODE LIKE '%"+orgCode+"%'\n");  	
		}
		if(!"".equals(orgName)&&orgName!=null){
		sql.append("   AND o.ORG_NAME LIKE '%"+orgName+"%'\n"); 
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
	public static PageResult<TmDealerPO> getAllAreaDealer(String areaId,String orgId,int curPage,int pageSize) throws Exception {
		List<TmOrgPO> list = new ArrayList<TmOrgPO>();
		StringBuffer sql = new StringBuffer();
		sql.append("select * from tm_dealer td where td.dealer_id in (select tdor.dealer_id from tm_dealer_org_relation tdor where 1=1");
		if(!"".equals(orgId)&&orgId!=null)
		{
		sql.append("AND TDOR.ORG_ID='"+orgId+"'");	
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
	private static List<TmOrgPO> getXjbm(String orgId,Long companyId, List<TmOrgPO> retLst) {
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
		List<TmOrgPO> lst = factory.select(sql.toString(), null,
				new DAOCallback<TmOrgPO>() {
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
	public static List<TcPosePO> getDRLPoseByPoseIds(Long companyId,
			String poseIds) throws Exception {
		String query = " SELECT POSE_ID,POSE_CODE,POSE_NAME FROM TC_POSE WHERE POSE_TYPE = '"
			+ Constant.SYS_USER_DEALER
			+ "' AND POSE_STATUS = '"
			+ Constant.STATUS_ENABLE
			+ "' and POSE_ID IN ("
			+ poseIds
			+ ") AND COMPANY_ID = " + companyId ;

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
	 * @author xwj
	 * @return List<TmCompanyPO>
	 * @throws Exception
	 */
	public static List<TmCompanyPO> getDownUserCompany() throws Exception {
		String query = " SELECT DISTINCT A.COMPANY_CODE,A.COMPANY_ID"
				+ " FROM TM_COMPANY A,TC_USER B WHERE A.COMPANY_ID = B.COMPANY_ID"
				+ " AND B.IS_DOWN = 0";
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
	
	public static PageResult<TmPtSupplierPO> allSuppQuery(String suppCode,String suppName,int curPage, int pageSize){
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT A.SUPPLIER_ID, A.SUPPLIER_CODE, A.SUPPLIER_NAME, A.SHORT_NAME, A.LINK_MAN, A.PHONE_NUMBER ");
		sql.append("FROM TM_PT_SUPPLIER A ");
		sql.append("WHERE A.IS_DEL = 0 ");
		if(!suppCode.equals("")){
			sql.append("AND A.SUPPLIER_CODE LIKE '%").append(suppCode).append("%' ");
		}
		if(!suppName.equals("")){
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
	
	public static PageResult<TmPtPartTypePO> allPartTypeQuery(String suppCode,String suppName,int curPage, int pageSize){
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT A.ID, A.PARTTYPE_CODE, A.PARTTYPE_NAME, A.PART_NUM, A.IS_MAX, A.IS_RETURN ");
		sql.append("FROM TM_PT_PART_TYPE A ");
		sql.append("WHERE 1 = 1 ");
		if(!suppCode.equals("")){
			sql.append("AND A.PARTTYPE_CODE LIKE '%").append(suppCode).append("%' ");
		}
		if(!suppName.equals("")){
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

	public  PageResult<Map<String, Object>> getAllRegion(Map<String, String> map,int curpage,int pagesize){
		String regionCode = map.get("regionCode") ;
		String regionName = map.get("regionName") ;
		String org_id = map.get("org_id") ;
		String dealer_type = map.get("dealer_type") ;
		StringBuffer sql= new StringBuffer("\n");
		
		sql.append("select distinct r.org_id, r.org_code, r.org_name,r.root_org_id\n");
		sql.append("  from vw_org_dealer_service r\n");  
		sql.append(" where 1 = 1\n");  
		if (dealer_type != null && !"".equals(dealer_type) && !"null".equals(dealer_type)) {
			sql.append("   and r.dealer_type =").append(dealer_type).append("\n"); 
		}
		if (org_id != null && !"".equals(org_id) && !"null".equals(org_id)) {
			sql.append("   and (r.org_id like '%").append(org_id).append("%' or r.root_org_id = "+ org_id +")\n"); 
		}
		
		if(!CommonUtils.isNullString(regionCode) && !"null".equals(regionCode)) {
			sql.append("   and r.org_code like '%").append(regionCode).append("%'\n");  
		}
		
		if(!CommonUtils.isNullString(regionName) && !"null".equals(regionName)) {
			sql.append("   and r.org_name like '%").append(regionName).append("%'\n");
		}

		sql.append("   order by  r.root_org_id,r.org_id ");
		PageResult<Map<String, Object>> ps = this.pageQuery(sql.toString(), null, getFunName(),pagesize, curpage);
		return ps;
	}	
	
	public  PageResult<Map<String, Object>> getAllCity(Map<String, String> map,int curpage,int pagesize){
		String regionCode = map.get("regionCode") ;
		String regionName = map.get("regionName") ;
		String CityName = map.get("CityName") ;
		String CityCode = map.get("CityCode") ;
		String org_id = map.get("org_id") ;
		String dealer_type = map.get("dealer_type") ;
		StringBuffer sql= new StringBuffer("\n");
		
		sql.append("select distinct r.city_name, r.city_ID,r.org_id, r.org_code, r.org_name,r.root_org_id\n");
		sql.append("  from vw_org_dealer_service r\n");  
		sql.append(" where 1 = 1\n");  
		if (dealer_type != null && !"".equals(dealer_type) && !"null".equals(dealer_type)) {
			sql.append("   and r.dealer_type =").append(dealer_type).append("\n"); 
		}
		if (org_id != null && !"".equals(org_id) && !"null".equals(org_id)) {
			sql.append("   and (r.org_id like '%").append(org_id).append("%' or r.root_org_id = "+ org_id +")\n"); 
		}
		
		if(!CommonUtils.isNullString(CityName) && !"null".equals(CityName)) {
			sql.append("   and r.City_Name like '%").append(CityName).append("%'\n");  
		}
		if(!CommonUtils.isNullString(CityCode) && !"null".equals(CityCode)) {
			sql.append("   and r.City_ID =").append(CityCode).append("\n");  
		}
		if(!CommonUtils.isNullString(regionCode) && !"null".equals(regionCode)) {
			sql.append("   and r.org_code like '%").append(regionCode).append("%'\n");  
		}
		if(!CommonUtils.isNullString(regionName) && !"null".equals(regionName)) {
			sql.append("   and r.org_name like '%").append(regionName).append("%'\n");
		}

		sql.append("   order by  r.root_org_id,r.org_id ");
		PageResult<Map<String, Object>> ps = this.pageQuery(sql.toString(), null, getFunName(),pagesize, curpage);
		return ps;
	}	
	
	
	
	
	/************
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
	public static PageResult<TmDealerPO> getAllDRLByDeptIdNew(String areaId,String isDealerType,String orgId,String dcode,String dsname,Long companyId,String provinceId,String dealerClass,int curPage,
			int pageSize, String orderName, String da,String inputOrgId,Integer poseBusType,Long poseId, String isAllLevel, String isAllArea) throws Exception {

		StringBuffer sql = new StringBuffer();
		sql.append("SELECT DISTINCT A.DEALER_CODE, A.DEALER_ID,A.DEALER_TYPE, A.DEALER_NAME, A.DEALER_NAME DEALER_SHORTNAME\n");
		sql.append("  FROM TM_DEALER a,TM_DEALER_BUSINESS_AREA TDBA\n");  
		sql.append(" where A.STATUS = "+Constant.STATUS_ENABLE+"\n");  
		sql.append("   AND A.OEM_COMPANY_ID = "+companyId+"\n");  
		sql.append(" AND TDBA.DEALER_ID=A.DEALER_ID and a.invoice_level=13611001 \n");
		if(areaId != null && !areaId.equals("") && !areaId.equals("null")){
			sql.append("AND TDBA.AREA_ID IN ("+areaId+")\n");
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
			sql.append("   AND A.PROVINCE_ID = '"+ provinceId +"' \n");
		}
		if (dealerClass != null && !"".equals(dealerClass)) {
			sql.append("   AND A.DEALER_CLASS = '"+ dealerClass +"' \n");
		}
		if (isAllLevel.toLowerCase().equals("false")) {
			sql.append("   AND A.DEALER_LEVEL = "+ Constant.DEALER_LEVEL_01 +" \n");
		}
		//modify by zhaojinyu 2010-06-12 经销商树中售后只查询售后的经销商，销售只查询销售的经销商。
		if(isDealerType.equals("null")){
			isDealerType="";
		}
		if(isDealerType.equals("")){
		if(Constant.POSE_BUS_TYPE_WR==poseBusType||Constant.POSE_BUS_TYPE_DWR==poseBusType){
			sql.append("   and A.DEALER_TYPE ="+Constant.DEALER_TYPE_DWR+" \n");
		}
		else if( Constant.POSE_BUS_TYPE_VS==poseBusType||Constant.POSE_BUS_TYPE_DVS==poseBusType||Constant.POSE_BUS_TYPE_WL==poseBusType){
			sql.append("   and A.DEALER_TYPE IN ("+Constant.DEALER_TYPE_DVS+", "+Constant.DEALER_TYPE_QYZDL+", "+Constant.DEALER_TYPE_JSZX+") \n");
		}
		}
		if(!isDealerType.equals("null") && isDealerType!=""){
			sql.append("and A.DEALER_TYPE IN ("+isDealerType+")");
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
			sql.append("                              WHERE ORG.STATUS = "+Constant.STATUS_ENABLE+"\n");  
			sql.append("                              START WITH ORG.ORG_ID = "+orgId+"\n");  
			sql.append("                             CONNECT BY PRIOR ORG.ORG_ID = ORG.PARENT_ORG_ID))\n");  
			sql.append("        CONNECT BY PRIOR TD.DEALER_ID = td.parent_dealer_d)\n");  
		}else{
			if(inputOrgId != null && !"".equals(inputOrgId)&&!"null".equals(inputOrgId)){
				sql.append("   and a.dealer_id in\n");  
				sql.append("       (select td.dealer_id\n");  
				sql.append("          from tm_dealer td\n");  
				sql.append("         START WITH TD.DEALER_ID in\n");  
				sql.append("                    (select tdor.dealer_id\n");  
				sql.append("                       from tm_dealer_org_relation tdor\n");  
				sql.append("                      where tdor.org_id in\n");  
				sql.append("                            (SELECT ORG_ID\n");  
				sql.append("                               FROM TM_ORG ORG\n");  
				sql.append("                              WHERE ORG.STATUS = "+Constant.STATUS_ENABLE+"\n");  
				sql.append("                              START WITH ORG.ORG_ID = "+inputOrgId+"\n");  
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
			sql.append("               ("+areaIds+"))\n");  
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
	
	public static void batchAddUserByDealerCode(String dealerCode) throws SQLException{
		String code = null;
		String _S = "-S";
		String _F = "-F";
		String procedureName = null;
		if(dealerCode.endsWith(_S)){
			code = dealerCode.replace(_S, "");
			procedureName = "pkg_autouser.createUser_S";
		} else if(dealerCode.endsWith(_F)){
			code = dealerCode.replace(_F, "");
			procedureName = "pkg_autouser.createUser_F";
		}
		if(null!=procedureName){
			Connection conn = DBService.getInstance().getConnection();
			Utility.setDealerType(code,procedureName, conn);
		}
	}


/**
 * 
* @Title: queryPoseBusTypeByUserid 
* @Description: TODO(根据userid查询pose_bus_type信息) 
* @param @param uid
* @return Map<String,Object>返回类型 
* @throws
 */
@SuppressWarnings("unchecked")
public  Map<String, Object> queryPoseBusTypeByUserid(long uid) {
	String sql= " select u.user_id,u.name,p.pose_bus_type from tc_user u, tc_pose p, TR_USER_POSE up\n" +
		        " where u.user_id = up.user_id and p.pose_id = up.pose_id and u.user_status ='10011001' and u.user_id = '"+uid+"'";
	Map<String, Object> ps = pageQueryMap(sql.toString(), null, getFunName());
	return ps;
}

/**
 * 
* @Title: queryPoseTypeByUserid 
* @Description: TODO(根据userid查询pose_bus_type信息) 
* @param @param uid
* @return Map<String,Object>返回类型 
* @throws
 */
@SuppressWarnings("unchecked")
public  Map<String, Object> queryPoseTypeByUserid(long uid) {
	String sql = "select c.company_type,u.user_id from tm_company c,tc_user u\n" +
		" where c.company_id = u.company_id\n" + 
		" and c.company_type <>"+Constant.COMPANY_TYPE_SGM+" \n" + 
		" and u.user_id = '"+uid+"'"; 
	Map<String, Object> ps = pageQueryMap(sql.toString(), null, getFunName());
	return ps;
}

/**
 * 根据经销商ID获取用户信息
 * @param dealerId
 * @return
 */
public List<Map<String, Object>> queryUserByDealerId(String dealerId){
	String sql = "select * from tc_user where user_status="+Constant.STATUS_ENABLE+" and dealer_id = ?";
	List<Object> params = new ArrayList<Object>();
	params.add(CommonUtils.checkNull(dealerId));
	return pageQuery(sql, params, this.getFunName());
}

@Override
protected PO wrapperPO(ResultSet rs, int idx) {
	// TODO Auto-generated method stub
	return null;
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
	sql.append(" order by dealer_code ");

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
	   
	sql.append(" select DISTINCT DEALER_CODE,DEALER_ID,DEALER_TYPE, DEALER_NAME, DEALER_SHORTNAME from (  \n");
	  
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
		
	}
	
	 
	sql.append("   union all \n");
	sql.append("  select b.dealer_codes DEALER_CODE, vw.DEALER_ID DEALER_ID,vw.DEALER_TYPE DEALER_TYPE,vw.DEALER_NAME,vw.DEALER_SHORTNAME \n");
	sql.append("   from t_pc_company_group b \n");
	sql.append("   LEFT JOIN VW_ORG_DEALER_ALL VW on b.dealer_codes = vw.DEALER_CODE \n");
	sql.append("  where 1 = 1 and b.par_dealer_id = '2012112619156961'  ) \n");
									
	sql.append(" ORDER BY DEALER_CODE, DEALER_NAME");

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
public static List<TcPosePO> getDealerPoseByCompanyId(Long companyId, String poseCode, String poseName, String poseIds) throws Exception {
String query="";
	query+="SELECT tp.POSE_ID, tp.POSE_CODE, tp.POSE_NAME ";
	  query+=" FROM TC_POSE tp,tr_role_pose trp,tc_role tr ";
	 query+="  WHERE POSE_TYPE = '"+Constant.SYS_USER_DEALER+"'";
	 query+="  and tp.pose_id=trp.pose_id ";
	query+= "  and trp.role_id=tr.role_id ";
	  query+= "  AND POSE_STATUS = '10011001' ";
	   query+="  AND COMPANY_ID = "+companyId;
		 query+="  and tr.role_id not in(4000004121,4000004540,4000004541,4000004580,4000004660)";
	  
//	String query = " SELECT POSE_ID,POSE_CODE,POSE_NAME FROM TC_POSE WHERE POSE_TYPE = '" + Constant.SYS_USER_DEALER + "' AND POSE_STATUS = '" + Constant.STATUS_ENABLE + "' AND COMPANY_ID = " + companyId;
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

/**
 * yinshunhui
 * @param companyId
 * @param oemCompanyId
 * @param deptId
 * @param acnt
 * @param name
 * @param poseCode
 * @param curPage
 * @param pageSize
 * @param orderName
 * @param da
 * @return
 * @throws Exception
 */
public static PageResult<Map<String, Object>> getDlrSysUserQueryA(String companyId, Long oemCompanyId, String deptId, String acnt, String name, String poseCode, int curPage, int pageSize, String orderName, String da,String poseRank,String groupId,String userStatus,String isLock) throws Exception {
	String query = " SELECT DISTINCT TU.USER_ID,TG.GROUP_NAME,TU.IS_lOCK,COM.COMPANY_SHORTNAME,TU.ACNT,TU.NAME,TU.USER_STATUS,TP.POSE_BUS_TYPE,TCD.CODE_DESC POSE_RANK, TCUS.NAME UPDATE_NAME, TO_CHAR(TU.UPDATE_DATE, 'YYYY-MM-DD') UPDATE_DATE" +
			" FROM TC_USER TU,TR_USER_POSE UP,TC_POSE TP, " + " TM_COMPANY COM, TC_USER TCUS,T_PC_GROUP TG,TC_CODE TCD" +
			" WHERE TU.UPDATE_BY = TCUS.USER_ID(+) AND TG.GROUP_ID(+)=TU.GROUP_ID AND COM.COMPANY_ID = TU.COMPANY_ID " +
			"AND  TU.USER_ID = UP.USER_ID(+) AND TCD.CODE_ID(+)=TU.POSE_RANK "
			+ " AND UP.POSE_ID = TP.POSE_ID(+) " + "  AND COM.COMPANY_TYPE <> " + Constant.COMPANY_TYPE_SGM;
	if (companyId != null && !"".equals(companyId)) {
		query += "  AND COM.COMPANY_ID ='" + companyId + "'";
	}
	query+=" and Tu.pose_rank not in(60281001,60281002) ";
	//yin 注释的
//	if (oemCompanyId != null && !"".equals(oemCompanyId)) {
//		query += "  AND COM.OEM_COMPANY_ID ='" + oemCompanyId + "'";
//	}
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
	if (poseRank != null && !poseRank.equals("")) {
		query += "  AND TCD.CODE_DESC like '%" + poseRank + "%'";
	}
	if(groupId!=null&&!"".equals(groupId)){
		query +=" AND TG.GROUP_ID="+groupId;
	}
	if(userStatus!=null&&!"".equals(userStatus)){
		query +=" AND Tu.user_status="+userStatus;
	}
	if(isLock!=null&&!"".equals(isLock)){
		query +=" AND Tu.is_lock="+isLock;
	}
	
	query += "    ORDER BY TU.USER_STATUS, TP.POSE_BUS_TYPE, TU.ACNT DESC\n";

//	query = OrderUtil.addOrderBy(query, orderName, da);
	logger.debug("SQL+++++++++++++++++++++++: " + query);
	return getPoseByUserIdA(dao.pageQuery(query, null, dao.getFunName(), curPage, pageSize));
}



/**
 * 物流人员查询
 * @param poseName
 * @param orgId
 * @param companyId
 * @param acnt
 * @param name
 * @param curPage
 * @param pageSize
 * @param orderName
 * @param da
 * @param userId
 * @param poseId
 * @return
 */
public static PageResult<Map<String,Object>> sgmSysUserQueryA2(String logiName, Integer poseBusType ,Long companyId, String acnt,String name,int curPage, int pageSize, String orderName, String da,Long userId,Long poseId) {
	StringBuffer sql= new StringBuffer();
	sql.append("SELECT tu.user_id,\n" );
	sql.append("       tu.acnt,\n" );
	sql.append("       tu.NAME,\n" );
	sql.append("       tu.user_status,\n" );
	sql.append("       tp.pose_id,\n" );
	sql.append("       tp.pose_name,\n" );
	sql.append("       tl.logi_code,\n" );
	sql.append("       tl.logi_name,\n" );
	sql.append("       tcu.NAME update_name,\n" );
	sql.append("       to_char(tu.update_date, 'YYYY-MM-DD') update_date\n" );
	sql.append("  FROM tc_user       tu,\n" );
	sql.append("       tc_pose       tp,\n" );
	sql.append("       tr_user_pose  tup,\n" );
	sql.append("       tc_user       tcu,\n" );
	sql.append("       tt_sales_logi tl\n" );
	sql.append(" WHERE tu.update_by = tcu.user_id(+)\n" );
	sql.append("   AND tu.user_id = tup.user_id\n" );
	sql.append("   AND tp.pose_id = tup.pose_id\n" );
	sql.append("   AND tp.logi_id = tl.logi_id\n");
	sql.append("   AND tu.company_id = "+companyId+"\n" );
	sql.append("   AND tp.pose_bus_type = "+Constant.POSE_BUS_TYPE_WL+"\n");
	if (Constant.POSE_BUS_TYPE_VS!=poseBusType) {
		sql.append("  and tp.pose_id ="+poseId+"\n");
	}
	if (acnt != null && !acnt.equals("")) {
		sql.append("  AND upper(TU.ACNT) like '%" + acnt.toUpperCase() + "%'");
	}
	if (name != null && !name.equals("")) {
		sql.append("  AND upper(TU.NAME) like '%" + name.toUpperCase() + "%'");
	}
	if (logiName != null && !logiName.equals("")) {
		sql.append("  AND tl.logi_name like '%" + logiName + "%'");
	}

	String mySql = OrderUtil.addOrderBy(sql.toString(), orderName, da);
	
	return dao.pageQuery(mySql, null, dao.getFunName(), curPage, pageSize) ;
}


public static PageResult<VwMaterialInfoPO> getMaterialInfo_dlvry(String invoice_dlvry_id,String materialCode,String materialName,int curPage, int pageSize) throws Exception {

	StringBuffer sbSql=new StringBuffer();
	sbSql.append(
			"select * from  vw_material_info info\n" +
					"where info.material_id  in(\n" + 
					"select dtl.material_id  from  tt_vs_dlvry_req_dtl dtl where dtl.req_id in(\n" + 
					"select req.req_id from tt_vs_dlvry_req  req where req.dlvry_req_no = "+invoice_dlvry_id+"\n" + 
					")\n" + 
					")"
		);
	if(materialCode!=null){
		sbSql.append(" and  info.material_code  like \'%"+materialCode+"%\'");
	}
	
	if(materialName!=null){
		sbSql.append(" and  info.MATERIAL_NAME  like \'%"+materialName+"%\'");
	}
	return factory.pageQuery(sbSql.toString(), null, new DAOCallback<VwMaterialInfoPO>() {
		public VwMaterialInfoPO wrapper(ResultSet rs, int idx) {
			VwMaterialInfoPO bean = new VwMaterialInfoPO();
			try {
				//bean.setSeriesCode(rs.getString("SERIES_ID"));
				bean.setMaterialId(rs.getLong("MATERIAL_ID"));
				bean.setMaterialCode(rs.getString("MATERIAL_CODE"));
				bean.setMaterialName(rs.getString("MATERIAL_NAME"));
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return bean;
		}
	}, pageSize,curPage);
}


}
