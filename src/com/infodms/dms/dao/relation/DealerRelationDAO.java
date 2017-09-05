package com.infodms.dms.dao.relation;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.common.BaseDao;
import com.infoservice.po3.bean.PO;

/**
 * 
 * @author fengalon
 *
 */
public class DealerRelationDAO extends BaseDao<PO>{
	public static Logger logger = Logger.getLogger(DealerRelationDAO.class);
	private DealerRelationDAO() {
	}
	
	/**
	 * @author fengalon
	 *
	 */
	private static class DealerRelationDaoSingleton {
		private static DealerRelationDAO dao = new DealerRelationDAO() ;
	}
	
	public static DealerRelationDAO getInstance() {
		return DealerRelationDaoSingleton.dao ;
	}
	
	/**
	 * 经销商与职位之间的关系查询
	 * @param companyId 对应经销商公司id
	 * @param poseId 对应经销商职位id
	 * @return List
	 */
	public List<Map<String, Object>> getDealerPoseRelation(Long companyId,Long poseId) {
		StringBuffer sql = new StringBuffer("\n") ;
		
		sql.append("SELECT distinct TMD.DEALER_ID, TMD.DEALER_NAME, TMD.DEALER_CODE\n");
		sql.append("  FROM TM_DEALER               TMD,\n");  
		sql.append("       TM_DEALER_BUSINESS_AREA TMDBA,\n");  
		sql.append("       TC_POSE                 TCP,\n");  
		sql.append("       TM_POSE_BUSINESS_AREA   TMPBA\n");  
		sql.append(" WHERE 1 = 1\n");  
		sql.append("   AND TMD.DEALER_ID = TMDBA.DEALER_ID\n");  
		sql.append("   AND TMDBA.AREA_ID = TMPBA.AREA_ID\n");  
		sql.append("   AND TMPBA.POSE_ID = TCP.POSE_ID\n");  
		sql.append("   AND TMD.STATUS = " + Constant.STATUS_ENABLE + " \n");  
		sql.append("   AND TMD.COMPANY_ID = ").append(companyId).append(" \n");  
		sql.append("   AND TCP.POSE_ID = ").append(poseId).append(" \n");
		
		return super.pageQuery(sql.toString(), null, getFunName()) ;
	}
	
	public List<Map<String, Object>> getDlrByCompany(String companyId) {
		StringBuffer sql = new StringBuffer("\n") ;
		
		sql.append("select tmd.dealer_id, tmd.dealer_name, tmd.dealer_code\n");
		sql.append("  from tm_dealer tmd\n");  
		sql.append(" where tmd.dealer_type <> ").append(Constant.MSG_TYPE_2).append("\n");  
		sql.append("   and tmd.status = ").append(Constant.STATUS_ENABLE).append("\n");  
		sql.append("   and tmd.company_id in (").append(companyId).append(")\n");

		return super.pageQuery(sql.toString(), null, super.getFunName()) ;
	}
	
	public List<Map<String, Object>> getAreaByDlr(String dealerId) {
		StringBuffer sql = new StringBuffer("\n") ;
		
		sql.append("select tdba.dealer_id, tdba.area_id, tba.area_name\n");
		sql.append("  from tm_dealer_business_area tdba, tm_business_area tba\n");  
		sql.append(" where tdba.area_id = tba.area_id\n");  
		sql.append("   and tdba.dealer_id in (").append(dealerId).append(")\n");
		
		return super.pageQuery(sql.toString(), null, super.getFunName()) ;
	}
	
	public List<Map<String, Object>> getAreaByDlrAndPose(String dealerId, String poseId) {
		StringBuffer sql = new StringBuffer("\n") ;
				
		sql.append("select tdba.dealer_id, tdba.area_id, tba.area_name\n");
		sql.append("  from tm_dealer_business_area tdba,\n");  
		sql.append("       tm_business_area        tba,\n");  
		sql.append("       tm_pose_business_area   tpba\n");  
		sql.append(" where tdba.area_id = tba.area_id\n");  
		sql.append("   and tdba.area_id = tpba.area_id\n");  
		sql.append("   and tpba.pose_id = ").append(poseId).append("\n");  
		sql.append("   and tdba.dealer_id in (").append(dealerId).append(")\n");
		
		return super.pageQuery(sql.toString(), null, super.getFunName()) ;
	}
	
	public Map<String, Object> getFirstDlr(String secDlrId) {
		StringBuffer sql = new StringBuffer("\n") ;
		
		sql.append("select tmd.dealer_id, tmd.dealer_type\n");
		sql.append("  from tm_dealer tmd\n");  
		sql.append(" where tmd.dealer_level = ").append(Constant.DEALER_LEVEL_01).append("\n");  
		sql.append(" start with tmd.dealer_id = ").append(secDlrId).append("\n");  
		sql.append("connect by prior tmd.parent_dealer_d = tmd.dealer_id\n");

		return super.pageQueryMap(sql.toString(), null, super.getFunName()) ;
	}
	
	public Map<String, Object> getDlrInfo(String dlrId) {
		StringBuffer sql = new StringBuffer("\n") ;
		
		sql.append("select tmd.dealer_id, tmd.dealer_type\n");
		sql.append("  from tm_dealer tmd\n");  
		sql.append(" where tmd.dealer_id = ").append(dlrId).append("\n");  

		return super.pageQueryMap(sql.toString(), null, super.getFunName()) ;
	}
	
	public Map<String, Object> getLevelDlr(String secDlrId, String level) {
		String levelStr = null ;
		
		StringBuffer sql = new StringBuffer("\n") ;
		
		sql.append("select max(level) maxLevel\n");
		sql.append("  from tm_dealer tmd\n");  
		sql.append(" start with tmd.dealer_id = ").append(secDlrId).append("\n"); 
		sql.append("connect by prior tmd.parent_dealer_d = tmd.dealer_id\n");
		
		Map<String, Object> map = super.pageQueryMap(sql.toString(), null, super.getFunName()) ;
		
		levelStr = map.get("MAXLEVEL").toString() ;
		
		if(levelStr.equals("2")) {
			level = "1" ;
		}
		
		sql = new StringBuffer("\n") ;

		sql.append("select distinct tmd.dealer_id, tmd.dealer_type\n");
		sql.append("  from tm_dealer tmd\n");  
		sql.append(" where level = ").append(level).append("\n");  
		sql.append(" start with tmd.dealer_id = ").append(secDlrId).append("\n");  
		sql.append("connect by prior tmd.parent_dealer_d = tmd.dealer_id\n");

		return super.pageQueryMap(sql.toString(), null, super.getFunName()) ;
	}
	
	/**
	 * 通过一级经销商id，查询出该经销商树下的所有有效经销商，包括一级
	 * @param firstDlr
	 * @return
	 */
	public List<Map<String, Object>> getAllDlrByFrtDlr(String firstDlr) {

		StringBuffer sql = new StringBuffer("\n") ;

		sql.append("select tmd.dealer_id\n") ;
		sql.append("  from tm_dealer tmd\n") ;
		sql.append(" where tmd.status = ").append(Constant.STATUS_ENABLE).append("\n") ;
		sql.append(" start with tmd.dealer_id in (").append(firstDlr).append(")\n") ;
		sql.append("CONNECT BY PRIOR tmd.dealer_id = tmd.parent_dealer_d") ;

		return super.pageQuery(sql.toString(), null, super.getFunName()) ;
	}
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}

}
