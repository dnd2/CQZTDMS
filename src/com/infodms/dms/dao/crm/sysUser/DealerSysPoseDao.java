package com.infodms.dms.dao.crm.sysUser;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.common.Constant;
import com.infodms.dms.common.OrderUtil;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.dao.sysposition.SysPositionDAO;
import com.infodms.dms.po.TtCrComplaintsAuditPO;
import com.infodms.dms.po.TtFleetEditLogPO;
import com.infoservice.dms.chana.common.DEConstant;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;


/**
 * 
 * <p>ComplaintAuditDao.java</p>
 *
 * <p>Description: 客户投诉处理明细持久化层</p>
 *
 * <p>Copyright: Copyright (c) 2010</p>
 *
 * <p>Company: www.infoservice.com.cn</p>
 * <p>Date:2010-6-2</p>
 *
 * @author zouchao
 * @version 1.0
 * @remark
 */
public class DealerSysPoseDao extends BaseDao<PO>{
	public static Logger logger = Logger.getLogger(DealerSysPoseDao.class);
	
	private static final DealerSysPoseDao dao = new DealerSysPoseDao();
	
	public static final DealerSysPoseDao  getInstance() {
		return dao;
	}
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		return null;
	}
	
	/**
	 * FUNCTION		:	查询经销商所有的组
	 * @param 		:	
	 * @return		:
	 * @throws		:	
	 * LastUpdate	:	2010-8-30
	 */
	public PageResult<Map<String, Object>> getGroupQueryList(
			Map<String, String> map, int pageSize, int curPage) {
		String groupName=map.get("groupName");
		String status=map.get("status");
		StringBuilder sql= new StringBuilder();
		sql.append("SELECT TPG.group_id,\n" );
		sql.append("       TPG.group_name,\n" );
		sql.append("       TPG.status,\n" );
		sql.append("       TPG.create_date,\n" );
		sql.append("       TPG.create_by,\n" );
		sql.append("       TPG.dealer_id,\n" );
		sql.append("       TD.dealer_code,\n" );
		sql.append("       TD.dealer_name\n" );
		sql.append("  FROM t_pc_group TPG, tm_dealer TD\n" );
		sql.append(" WHERE TPG.dealer_id = TD.dealer_id \n");
		if(null!=groupName&&!"".equals(groupName)){
			sql.append(" and tpg.group_name like '%"+groupName+"%'\n");
		}
		if(null!=status&&!"".equals(status)){
			sql.append(" and tpg.status ="+status);
		}
		sql.append("   order by tpg.create_date desc");
		
		
		PageResult<Map<String, Object>> ps =dao.pageQuery(sql.toString(), null, dao.getFunName(), pageSize, curPage);
		return ps;
	}
	public PageResult<Map<String, Object>> getPoseList(Map<String, String> map,
			Integer pageSize, Integer curPage) {
		String poseCode=map.get("poseCode");
		String poseName=map.get("poseName");
		String companyId=map.get("companyId");
		StringBuilder sql= new StringBuilder();
		sql.append("select tp.pose_id,\n" );
		sql.append("       tp.pose_code,\n" );
		sql.append("       tp.pose_name,\n" );
		sql.append("       tp.create_date,\n" );
		sql.append("       tp.create_by,\n" );
		sql.append("       tp.pose_status,\n" );
		sql.append("       tp.pose_rank\n" );
		sql.append("  from tc_pose tp,tm_company tc\n" );
		sql.append(" where 1 = 1\n" );
		sql.append("   and tp.company_id=tc.company_id \n");
		sql.append(" and tc.company_id="+companyId);
		if(null!=poseCode&&!"".equals(poseCode)){
			sql.append("  and tp.pose_code like '%"+poseCode+"%'\n" );
		}
		if(null!=poseName&&!"".equals(poseName)){
			sql.append("   and tp.pose_name like '%"+poseName+"%'\n");
		}
		sql.append("  order by tp.create_date desc ");
		
		
		PageResult<Map<String, Object>> ps =dao.pageQuery(sql.toString(), null, dao.getFunName(), pageSize, curPage);
		return ps;
	}

	public static PageResult<Map<String, Object>> sysPoseQuery(String par_pose_id,String poseRank,String companyId,String poseCode,String poseName,String poseType,String dealerId, String deptId , String roleName, int curPage, int pageSize, String orderName,String da,Long oemCompanyId,String poseStatus) throws Exception {
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
        
        
        if(poseRank != null && !poseRank.equals("")) {
        	query += "  and A.POSE_RANK = " + poseRank +"  ";
        }
        if(par_pose_id != null && !par_pose_id.equals("")) {
        	query += "  and A.PAR_POSE_ID= " + par_pose_id +"  ";
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
             	query += "  and A.COMPANY_ID = '" + companyId +"'";
             }
        }else
        {
        	query += "  and A.COMPANY_ID = '" + oemCompanyId +"'";	
        }
        if(deptId != null && !"".equals(deptId)) {
        	query += "  and A.ORG_ID = '" + deptId +"'";
        }
       
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
	
		
		 PageResult<Map<String, Object>> ps =dao.pageQuery(query.toString(), null, dao.getFunName(), curPage, pageSize);
		 return ps;
	}
	
	
	
	
}
