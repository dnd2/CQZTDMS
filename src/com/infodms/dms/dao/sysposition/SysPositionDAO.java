/**********************************************************************
* <pre>
* FILE : SysPositionDAO.java
* CLASS : SysPositionDAO
* 
* AUTHOR : ChenLiang
*
* FUNCTION : 系统职位DAO.
*
*
*======================================================================
* CHANGE HISTORY LOG
*----------------------------------------------------------------------
* MOD. NO.| DATE     |    NAME    | REASON | CHANGE REQ.
*----------------------------------------------------------------------
*         |2009-08-23| ChenLiang  | Created |
* DESCRIPTION:
* </pre>
***********************************************************************/
package com.infodms.dms.dao.sysposition;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.log4j.Logger;



import com.infodms.dms.common.Constant;
import com.infodms.dms.common.OrderUtil;
import com.infodms.dms.dao.claim.basicData.ClaimBasicParamsDao;
import com.infodms.dms.dao.common.BaseDao;


import com.infodms.dms.dao.usermng.UserMngDAO;
import com.infodms.dms.po.TcPosePO;
import com.infodms.dms.po.TcPoseUpdateName;
import com.infodms.dms.po.TcRolePO;
import com.infodms.dms.po.TmBrandPO;
import com.infodms.dms.po.TmBusinessAreaPO;
import com.infodms.dms.po.TmPoseBusinessAreaPO;
import com.infodms.dms.po.TrPoseBinsPO;
import com.infodms.dms.po.TrRoleBinsPO;
import com.infodms.dms.po.TrRoleFuncPO;
import com.infoservice.po3.POFactory;
import com.infoservice.po3.POFactoryBuilder;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;
import com.infoservice.po3.callbackimpl.POCallBack;
import com.infoservice.po3.core.callback.DAOCallback;
import com.sun.org.apache.bcel.internal.generic.NEW;

public class SysPositionDAO extends  BaseDao<PO>{
	public static Logger logger = Logger.getLogger(SysPositionDAO.class);
	private static POFactory factory = POFactoryBuilder.getInstance();
	private static final  UserMngDAO daos = new UserMngDAO();
	private static final SysPositionDAO dao = new SysPositionDAO ();
	public static final SysPositionDAO getInstance() {
		return dao;
	}
	
	public static PageResult<Map<String, Object>> sysPoseQuery(String companyId,String poseCode,String poseName,String poseType,String dealerId, String deptId , String roleName, int curPage, int pageSize, String orderName,String da,Long oemCompanyId,String poseStatus,Long userId,Long poseId) throws Exception {
        String  query = "select A.UPDATE_DATE,(SELECT NAME FROM TC_USER WHERE USER_ID=A.UPDATE_BY) AS UPDATE_NAME,A.POSE_ID,A.POSE_CODE,A.POSE_NAME,A.POSE_TYPE,A.POSE_STATUS from tc_pose A where 1=1 ";
        if(poseCode != null && !poseCode.equals("")) { // 拼查询职位的SQL
        	query += "  and upper(A.POSE_CODE) like '%" + poseCode.toUpperCase() +"%'";
        }
        if(poseName != null && !poseName.equals("")) {
        	query += "  and upper(A.POSE_NAME) like '%" + poseName.toUpperCase() +"%'";
        }
        if(poseType != null && !poseType.equals("")) {
        	query += "  and A.POSE_TYPE = '" + poseType +"'";
        }
        if(poseStatus != null && !poseStatus.equals("")) {
        	query += "  and A.POSE_STATUS = '" + poseStatus +"'";
        }
        if(roleName != null && !roleName.equals("")) {
        	query += "  and EXISTS (SELECT B.ROLE_POSE_ID FROM TR_ROLE_POSE B, TC_ROLE C WHERE A.POSE_ID = B.POSE_ID AND B.ROLE_ID = C.ROLE_ID AND C.ROLE_NAME LIKE '%"+roleName+"%')";
        }
        if(poseType != null && poseType.equals(String.valueOf(Constant.SYS_USER_DEALER))) 
        {
        	//modified by andy.ten@tom.com
        	if(!"".equals(dealerId))
        	{
        		query += "  and A.ORG_ID = (SELECT DEALER_ORG_ID FROM TM_DEALER WHERE DEALER_ID = "+dealerId+")";
            	
        	}
        	//end
        	 if(companyId != null && !"".equals(companyId)) 
        	 {
             	query += "  and A.COMPANY_ID IN (SELECT DEALER_ID FROM TM_DEALER WHERE COMPANY_ID = "+companyId+")";
             }
        }else
        {
        	query += "  and A.COMPANY_ID = '" + oemCompanyId +"'";	
        }
        if(deptId != null && !"".equals(deptId)) {
        	query += "  and A.ORG_ID = '" + deptId +"'";
        }
        
        if(userId!=1000002433){
	        query +="and A.POSE_ID not in(4000008240)\n";
        }else{
        	if(poseId!=4000008240L){
        		query +="and A.POSE_ID not in(4000008240)\n";
        	}
        }
        
        query += "\n order by a.pose_code desc";
     query = OrderUtil.addOrderBy(query, orderName, da);
		logger.debug("SQL+++++++++++++++++++++++: "+query);
		  /* 	return factory.pageQuery(query, null,new DAOCallback<TcPoseUpdateName>() {
			public TcPoseUpdateName wrapper(ResultSet rs, int idx){
				TcPoseUpdateName bean = new TcPoseUpdateName();
				try {
					bean.setPoseId(rs.getLong("POSE_ID"));
					bean.setPoseCode(rs.getString("POSE_CODE"));
					bean.setPoseName(rs.getString("POSE_NAME"));
					bean.setPoseType(rs.getInt("POSE_TYPE"));
					bean.setPoseStatus(rs.getInt("POSE_STATUS"));
					
					
					bean.setUpdateDate(rs.getDate("UPDATE_DATE"));
					bean.setUpdateDates(rs.getString("UPDATE_DATE"));
					bean.setUpdateBy(rs.getLong("UPDATE_BY"));
					bean.setUpdateName(rs.getString("UPDATE_NAME"));
					
				} catch (SQLException e) {
					e.printStackTrace();
				}
				return bean;
			}
		}, curPage, pageSize);*/
	
		
		 PageResult<Map<String, Object>> ps =daos.pageQuery(query.toString(), null, daos.getFunName(), curPage, pageSize);
		 return ps;
	}
	
	public  boolean jude_del(String DEPT_ID,String[] DEALER_ID)
	{
		String spend = "";
		for(int i = 0 ;i<DEALER_ID.length;i++)
		{
			if(i == DEALER_ID.length-1)
			{
				spend = spend + DEALER_ID[i];
			}else
			{
				spend = spend + DEALER_ID[i] + ",";
			}
		}
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT A.*\n" );
		sql.append("  from TC_POSE A, TR_POSE_DEALER B\n" );
		sql.append(" where A.POSE_ID = B.POSE_ID\n" );
		sql.append("   AND A.POSE_BUS_TYPE = "+Constant.POSE_BUS_TYPE_WR+"\n" );
		sql.append("   AND B.DEALER_ID in (" +spend+")");
		sql.append("   AND A.ORG_ID ="+ DEPT_ID);
		System.out.println(sql);
	    List<TcPosePO> list = super.factory.select(sql.toString(), null, new POCallBack<TcPosePO>(factory, TcPosePO.class));
	    if(list != null && list.size()>0)
	    {
	    	return false;
	    }else
	    {
	    	return true;
	    }

	}
	
	public  boolean jude_del_no(String DEPT_ID)
	{
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT A.*\n" );
		sql.append("  from TC_POSE A\n" );
		sql.append(" WHERE   A.POSE_BUS_TYPE = "+Constant.POSE_BUS_TYPE_WR+"\n" );
		sql.append("   AND A.ORG_ID ="+ DEPT_ID);
		String  sql2= "SELECT C.* from TC_POSE C,tr_pose_dealer D where C.POSE_ID = D.POSE_ID AND C.POSE_BUS_TYPE = "+Constant.POSE_BUS_TYPE_WR+" AND C.ORG_ID ="+DEPT_ID;
		List<TcPosePO> list2 = super.factory.select(sql2, null, new POCallBack<TcPosePO>(factory, TcPosePO.class));
	    List<TcPosePO> list = super.factory.select(sql.toString(), null, new POCallBack<TcPosePO>(factory, TcPosePO.class));
	    if( list.size()>0 &&  list2.size() == 0)
	    {
	    	return false;
	    }else if( list.size()>0  && list2.size() != list.size())
	    {
	    	return false;
	    } else 
	    {
	    	return true;
	    }
	    

	}
	
	public static List<TcRolePO> sysRoleQuery(String roleType,String roleName,Long companyId) throws Exception {
        String  query = "select ROLE_ID,ROLE_DESC,ROLE_NAME,ROLE_TYPE,ROLE_STATUS from tc_role where 1=1 and oem_company_Id= "+companyId+" and ROLE_STATUS='"+Constant.STATUS_ENABLE+"'";
        if(roleName != null && !roleName.equals("")) {
        	query += "  and ROLE_DESC like '%" + roleName +"%'";
        }
        if(roleType != null && !roleType.equals("")) {
        	query += "  and ROLE_TYPE = '" + roleType +"'";
        }
    	
		logger.debug("SQL+++++++++++++++++++++++: "+query);
		
		
		return factory.select(query, null,new DAOCallback<TcRolePO>() {
			public TcRolePO wrapper(ResultSet rs, int idx){
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
		});
	}
	
	public static List<TrRoleFuncPO> sysFunsByRoleIdsQuery(String[] roleids) throws Exception {
		String  query = "select DISTINCT func_id from tr_role_func where 1=1 ";
		
		for(int i=0; i<roleids.length; i++) {
			if(i==0) {
				query += " and role_id ='"+roleids[i]+"' ";
			} else {
				query += " or role_id ='"+roleids[i]+"' ";
			}
		}
		
		logger.debug("SQL+++++++++++++++++++++++: "+query);
		return factory.select(query, null,new DAOCallback<TrRoleFuncPO>() {
			public TrRoleFuncPO wrapper(ResultSet rs, int idx){
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
	
	public static List<TrRoleBinsPO> gjPoseByRoleIds(String roleids) throws Exception {
		String  query = "select DISTINCT bins_code_id from tr_role_bins where role_id in ("
				+ roleids + ")";
		
		logger.debug("SQL+++++++++++++++++++++++: "+query);
		return factory.select(query, null,new DAOCallback<TrRoleBinsPO>() {
			public TrRoleBinsPO wrapper(ResultSet rs, int idx){
				TrRoleBinsPO bean = new TrRoleBinsPO();
				try {
					bean.setBinsCodeId(rs.getLong("bins_code_id"));
				} catch (SQLException e) {
					e.printStackTrace();
				}
				return bean;
			}
		});
	}
	
	public static List<TrPoseBinsPO> gjPoseByPoseIds(String poseIds) throws Exception {
		String  query = "select DISTINCT bins_code_id from tr_pose_bins where pose_id in ("
			+ poseIds + ")";
		
		logger.debug("SQL+++++++++++++++++++++++: "+query);
		return factory.select(query, null,new DAOCallback<TrPoseBinsPO>() {
			public TrPoseBinsPO wrapper(ResultSet rs, int idx){
				TrPoseBinsPO bean = new TrPoseBinsPO();
				try {
					bean.setBinsCodeId(rs.getString("bins_code_id"));
				} catch (SQLException e) {
					e.printStackTrace();
				}
				return bean;
			}
		});
	}
	/**
	 * 查询需要下发用户的单点登陆职位
	 * @author xwj
	 * @param companyId
	 * @return List<TcPose>
	 * @throws Exception
	 */
	public static List<TcPosePO> getDownUserPose(Long companyId,Long userId) throws Exception {
		String query = " SELECT TP.POSE_CODE"
				+ " FROM TC_POSE TP,TR_USER_POSE TR"
				+ " WHERE TP.POSE_ID = TR.POSE_ID"
				+ " AND TP.COMPANY_ID = "+companyId
				+ " AND TR.USER_ID = "+userId
				+ " AND TP.POSE_TYPE = "+10021002;
		logger.debug("the getDownUserPose SQL===== " + query);
		return factory.select(query, null, new DAOCallback<TcPosePO>() {
			public TcPosePO wrapper(ResultSet rs, int idx) {
				TcPosePO bean = new TcPosePO();
				try {
					bean.setPoseCode(rs.getString("POSE_CODE"));
				} catch (SQLException e) {
					e.printStackTrace();
				}
				return bean;
			}
		});
	}
	/**
	 * 根据职位ID查询该职位的 业务范围
	 * @param pose
	 * @return
	 */
	public static List<TmBusinessAreaPO> findPoseArea(String poseId){
		
		StringBuffer sql = new StringBuffer();

		sql.append("select ba.area_id, ba.area_name\n");
		sql.append("  from tm_pose_business_area pa, tm_business_area ba\n");  
		sql.append(" where pa.area_id = ba.area_id\n");  
		sql.append("   and pa.pose_id = ?\n");  
		sql.append("   and ba.status = ?");

		
		List<Object> params = new ArrayList<Object>();
		params.add(poseId);
		params.add(Constant.STATUS_ENABLE);
		
		
		return factory.select(sql.toString(), params,new DAOCallback<TmBusinessAreaPO>() {
			public TmBusinessAreaPO wrapper(ResultSet rs, int idx){
				TmBusinessAreaPO po = new TmBusinessAreaPO();
				try {
					po.setAreaId(rs.getLong("AREA_ID"));
					po.setAreaName(rs.getString("AREA_NAME"));
				} catch (SQLException e) {
					e.printStackTrace();
				}
				return po;
			}
		});
	}
	
	/**
	 * added by andy.ten@tom.com
	 * @param roleids
	 * @return
	 * @throws Exception
	 * List<TcRolePO>
	 * 2010-5-31
	 */
	public static List<TcRolePO> sysRolesByRoleIdsQuery(String[] roleids)throws Exception {
		String query = "select DISTINCT role_id,role_name,role_desc from tc_role where 1=1 ";
		
		for (int i = 0; i < roleids.length; i++) {
			if (i == 0) {
				query += " and role_id ='" + roleids[i] + "' ";
			} else {
				query += " or role_id ='" + roleids[i] + "' ";
			}
		}
		
		logger.debug("SQL: " + query);
		return factory.select(query, null, new DAOCallback<TcRolePO>() {
			public TcRolePO wrapper(ResultSet rs, int idx) {
				TcRolePO bean = new TcRolePO();
				try {
					bean.setRoleId(rs.getLong("role_id"));
					bean.setRoleName(rs.getString("role_name"));
					bean.setRoleDesc(rs.getString("role_desc"));
				} catch (SQLException e) {
					e.printStackTrace();
				}
				return bean;
			}
		});
		}
	public static List<TcRolePO> sysRolesByRoleIdsQueryByPoseId(String poseId)throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT TR.ROLE_ID, TR.ROLE_NAME, TR.ROLE_DESC \n");
		sql.append("  FROM TC_POSE TP, TR_ROLE_POSE R, TC_ROLE TR \n");
		sql.append(" WHERE     TP.POSE_ID = "+poseId+" \n");
		sql.append("       AND TP.POSE_ID = R.POSE_ID \n");
		sql.append("       AND TR.ROLE_ID = R.ROLE_ID \n");
		logger.debug("SQL: " + sql);
		return factory.select(sql.toString(), null, new DAOCallback<TcRolePO>() {
			public TcRolePO wrapper(ResultSet rs, int idx) {
				TcRolePO bean = new TcRolePO();
				try {
					bean.setRoleId(rs.getLong("role_id"));
					bean.setRoleName(rs.getString("role_name"));
					bean.setRoleDesc(rs.getString("role_desc"));
				} catch (SQLException e) {
					e.printStackTrace();
				}
				return bean;
			}
		});
		}
	
	/**
	 * added by andy.ten@tom.com
	 * @param poseId
	 * @return
	 * @throws Exception
	 * List<TcRolePO>
	 * 2010-5-31
	 */
	public static List<TcRolePO> sysRolesByPoseIdQuery(String poseId)throws Exception {
		String query = "select DISTINCT tr.role_id,tr.role_name,tr.role_desc from tc_role tr,tr_role_pose pr where pr.role_id = tr.role_id  ";
		
		query += " and pr.pose_id ='" + poseId + "' ";
		
		logger.debug("SQL: " + query);
		return factory.select(query, null, new DAOCallback<TcRolePO>() {
			public TcRolePO wrapper(ResultSet rs, int idx) {
				TcRolePO bean = new TcRolePO();
				try {
					bean.setRoleId(rs.getLong("role_id"));
					bean.setRoleName(rs.getString("role_name"));
					bean.setRoleDesc(rs.getString("role_desc"));
				} catch (SQLException e) {
					e.printStackTrace();
				}
				return bean;
			}
		});
		}
	
	/**
	 * added by andy.ten@tom.com
	 * @param poseId
	 * @return
	 * @throws Exception
	 * List<TrRoleFuncPO>
	 * 2010-5-31
	 */
	public static List<TrRoleFuncPO> sysFuncByPoseIdQuery(String poseId)throws Exception {
		String query = "select DISTINCT rf.role_id,rf.func_id from tr_role_func rf,tr_role_pose pr,tc_role tr where rf.role_id = pr.role_id and rf.role_id = tr.role_id";
		query += " and pr.pose_id ='" + poseId + "' and tr.role_status = 10011001 ";  //YH 2011.6.3 如果角色无效 功能列表不显示
		
		logger.debug("SQL: " + query);
		return factory.select(query, null, new DAOCallback<TrRoleFuncPO>() {
			public TrRoleFuncPO wrapper(ResultSet rs, int idx) {
				TrRoleFuncPO bean = new TrRoleFuncPO();
				try {
					bean.setRoleId(rs.getLong("role_id"));
					bean.setFuncId(rs.getLong("func_id"));	
				} catch (SQLException e) {
					e.printStackTrace();
				}
				return bean;
			}
		});
		}
	
	/**
	 * 根据职位ID查询该职位的 业务范围 
	 * @param pose  YH 2011.5.12
	 * @return
	 */
	public List<Map<String, Object>> findPoseArea2(String comId){
		
		StringBuffer sql= new StringBuffer();
		sql.append("select distinct pb.area_id \n" );
		sql.append("  from tm_pose_business_area pb,tm_business_area ba\n" );
		sql.append(" where pb.pose_id in\n" );
		sql.append("       (select A.POSE_ID\n" );
		sql.append("          from tc_pose A\n" );
		sql.append("         where 1 = 1\n" );
		sql.append("           and A.POSE_TYPE = '10021002'\n" );
		sql.append("           and A.POSE_STATUS = '10011001'\n" );
		sql.append("           and A.COMPANY_ID = '"+comId+"')\n" );
		sql.append("         and pb.area_id = ba.area_id");
		
		List<Map<String, Object>> list = pageQuery(sql.toString(), null, getFunName());
		return list;
	}
	
	/**
	 * 取物流商
	 * @return
	 */
	public List<Map<String, Object>> getLogiList(){
		StringBuffer sbSql=new StringBuffer();
		sbSql.append("SELECT   A.LOGI_ID, B.AREA_NAME || '-' || A.LOGI_NAME AS LOGI_NAME\n");
		sbSql.append("  FROM   TT_SALES_LOGI A, TM_BUSINESS_AREA B\n");
		sbSql.append(" WHERE   A.YIELDLY = B.AREA_ID AND A.STATUS = 10011001\n");
		List<Map<String, Object>> list = pageQuery(sbSql.toString(), null, getFunName());
		return list;
	}
	
	
	
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}
}
