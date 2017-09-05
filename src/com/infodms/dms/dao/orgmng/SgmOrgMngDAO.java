/**********************************************************************
 * <pre>
 * FILE : SgmOrgMngDAO.java
 * CLASS : SgmOrgMngDAO
 * 
 * AUTHOR : ChenLiang
 *
 * FUNCTION : SGM组织管理DAO.
 *
 *
 *======================================================================
 * CHANGE HISTORY LOG
 *----------------------------------------------------------------------
 * MOD. NO.| DATE     |    NAME    | REASON | CHANGE REQ.
 *----------------------------------------------------------------------
 *         |2009-08-27| ChenLiang  | Created |
 * DESCRIPTION:
 * </pre>
 ***********************************************************************/

package com.infodms.dms.dao.orgmng;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.OrgBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.OrderUtil;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.po.TmOrgPO;
import com.infoservice.po3.POFactory;
import com.infoservice.po3.POFactoryBuilder;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;
import com.infoservice.po3.core.callback.DAOCallback;

public class SgmOrgMngDAO extends BaseDao<PO>{
	public static Logger logger = Logger.getLogger(SgmOrgMngDAO.class);
	private static final SgmOrgMngDAO dao = new SgmOrgMngDAO ();
	private static POFactory factory = POFactoryBuilder.getInstance();
	
	public static PageResult<OrgBean> sgmOrgMngQuery(String orgId,Long companyId,
			int curPage, int pageSize, String orderName,String da, String deptLevel) throws Exception {
		String query = "select  td.parent_org_id,tmp.org_name parent_org_name "
				+ ",td.org_code,td.org_name,td.status,td.org_id,td.company_id "
				+ " from tm_org td,tm_org tmp where td.parent_org_id = tmp.org_id(+) and td.company_id = '"
				+ companyId + "' and td.org_type = 10191001 ";
		if (orgId != null && !orgId.equals("")) { // 查询SGM部门
			query += "  and td.parent_org_id = '" + orgId + "'";
		}
		if(deptLevel != null && !deptLevel.equals("")) {
			query += " and td.duty_type = " + deptLevel + "\n";
		}
		query = OrderUtil.addOrderBy(query, orderName, da);
		logger.debug("SQL+++++++++++++++++++++++: " + query);
		return factory.pageQuery(query, null, new DAOCallback<OrgBean>() {
			public OrgBean wrapper(ResultSet rs, int idx) {
				OrgBean bean = new OrgBean();
				try {
					bean.setParentOrgName(rs.getString("parent_org_name"));
					bean.setParentOrgId(rs.getLong("parent_org_id"));
					bean.setOrgCode(rs.getString("org_code"));
					bean.setOrgName(rs.getString("org_name"));
					bean.setStatus(rs.getInt("status"));
					bean.setOrgId(rs.getLong("org_id"));
					bean.setCompanyId(rs.getLong("company_id"));
				} catch (SQLException e) {
					e.printStackTrace();
				}
				return bean;
			}
		}, curPage, pageSize);
	}
	
	/**
	 * 修改时判断SGM下部门名称是否重复
	 * 
	 * @param dealerID
	 *            公司ID
	 * @param deptName
	 *            部门名称
	 * @param deptCode
	 *            部门代码
	 * @param factory
	 * @return
	 * @throws Exception
	 */
	public static List<TmOrgPO> getOrgIdByOrgName(Long companyId,
			String orgName, String orgCode, POFactory factory)
			throws Exception {
		String query = " select td.org_id from tm_org td where td.company_id = '"
				+ companyId
				+ "' "
				+ "and td.org_name = '"
				+ orgName
				+ "' and td.org_code <> '" + orgCode + "' ";
		logger.debug("SQL+++++++++++++++++++++++: " + query);
		return factory.select(query, null, new DAOCallback<TmOrgPO>() {
			public TmOrgPO wrapper(ResultSet rs, int idx) {
				TmOrgPO bean = new TmOrgPO();
				try {
					bean.setOrgId(rs.getLong("org_id"));
				} catch (SQLException e) {
					e.printStackTrace();
				}
				return bean;
			}
		});
	}
	public static List<Map<String, Object>> getOrgBusiness(String orgId,Long companyId
			) throws Exception {
	    StringBuffer sql=new StringBuffer();
		sql.append("SELECT C.AREA_ID, C.AREA_NAME, C.AREA_CODE, TOBA.RELATION_ID\n");
		sql.append("  FROM (SELECT TMA.AREA_ID, TMA.AREA_NAME, TMA.AREA_CODE\n");  
		sql.append("          FROM TM_BUSINESS_AREA TMA\n");  
		//sql.append("         WHERE TMA.COMPANY_ID = "+companyId+") C\n"); 
		sql.append("         WHERE 1=1) C\n");
		sql.append("  LEFT JOIN TM_ORG_BUSINESS_AREA TOBA ON C.AREA_ID = TOBA.AREA_ID\n");  
		sql.append("                                     AND TOBA.ORG_ID = "+orgId+"\n");
		sql.append("ORDER BY C.AREA_ID\n");
		logger.debug("SQL+++++++++++++++++++++++: " + sql.toString());
		//PageResult<Map<String, Object>> po=dao.pageQuery(sql.toString(),null, dao.getFunName(), curPage, pageSize);
		List<Map<String, Object>> po=dao.pageQuery(sql.toString(), null, dao.getFunName());
		return po;
		//pageQuery(sql.toString(), null,null,curPage, pageSize) 
	
	}
	//查询车厂组织
	public static PageResult<Map<String, Object>> getOrgCompany(String companyCode,String companyName,Long companyId,
			int curPage, int pageSize) throws Exception {
	    StringBuffer sql=new StringBuffer();
	    sql.append("SELECT TC.COMPANY_ID,\n");
	    sql.append("       TC.OEM_COMPANY_ID,\n");  
	    sql.append("       TC.COMPANY_TYPE,\n");  
	    sql.append("       TC.COMPANY_CODE,\n");  
	    sql.append("       TC.COMPANY_SHORTNAME,\n");  
	    sql.append("       TC.COMPANY_NAME,\n");  
	    sql.append("       TC.STATUS\n");  
	    sql.append("  FROM TM_COMPANY TC\n");  
	    sql.append(" WHERE TC.COMPANY_TYPE = "+Constant.COMPANY_TYPE_SGM+"\n"); 
	    sql.append("    AND TC.OEM_COMPANY_ID = "+companyId+"\n"); 
	    if(!"".equals(companyCode)&&companyCode!=null)
	    {
	    	sql.append("   AND TC.COMPANY_CODE LIKE '%"+companyCode+"%'\n");  
	    }
	    if(!"".equals(companyName)&&companyName!=null)
	    {
	    sql.append("   AND TC.COMPANY_NAME LIKE '%"+companyName+"%'\n");
	    }
		logger.debug("SQL+++++++++++++++++++++++: " + sql.toString());
		PageResult<Map<String, Object>> po=dao.pageQuery(sql.toString(),null, dao.getFunName(), curPage, pageSize);
		return po;
		//pageQuery(sql.toString(), null,null,curPage, pageSize) 
	
	}
	//查询treeCODE
	public  Map<String, Object> getOrgTreeCode(){
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT G.*,\n");
		sql.append("       DECODE(LENGTH(G.NUM), 3, CHR(ASCII(G.CHARS) + 1)||'01', G.CHARS||G.NUM) AS NEW_TREECODE\n");  
		sql.append("  FROM (SELECT DECODE(LENGTH(SUBSTR(TREE_CODE, LENGTH(TREE_CODE) - 1, 2) + 1),\n");  
		sql.append("                      1,\n");  
		sql.append("                      '0' ||\n");  
		sql.append("                      (SUBSTR(TREE_CODE, LENGTH(TREE_CODE) - 1, 2) + 1),\n");  
		sql.append("                      (SUBSTR(TREE_CODE, LENGTH(TREE_CODE) - 1, 2) + 1)) AS NUM,\n");  
		sql.append("               SUBSTR(TREE_CODE, LENGTH(TREE_CODE) - 2, 1) AS CHARS,\n");  
		sql.append("               MAX(TREE_CODE)\n");  
		sql.append("          FROM TM_ORG T\n");  
		sql.append("         WHERE T.ORG_LEVEL = 1\n");  
		sql.append("         GROUP BY SUBSTR(TREE_CODE, LENGTH(TREE_CODE) - 1, 2),\n");  
		sql.append("                  SUBSTR(TREE_CODE, LENGTH(TREE_CODE) - 2, 1)) G\n");
		Map<String, Object> map = pageQueryMap(sql.toString(),null,dao.getFunName());
		return map;
	}
	//查询roleList
	public  List<Map<String, Object>> getroleList(String companyId){
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT DISTINCT TR.ROLE_ID\n");  
		sql.append("  FROM TC_ROLE TR, TR_ROLE_FUNC TRF\n");  
		sql.append(" WHERE TR.ROLE_NAME LIKE '%超级用户%'\n");  
		sql.append("   AND TR.ROLE_ID = TRF.ROLE_ID\n");
		sql.append("   AND TR.OEM_COMPANY_ID = "+companyId+"\n");  
		sql.append("   AND TRF.FUNC_ID IN (SELECT C.FUNC_ID\n");  
		sql.append("                         FROM TC_FUNC C\n");  
		sql.append("                        WHERE C.FUNC_NAME LIKE '%用户维护%'\n");  
		sql.append("                           OR C.FUNC_NAME LIKE '%角色维护%'\n");  
		sql.append("                           OR C.FUNC_NAME LIKE '%用户维护%'\n");  
		sql.append("                           OR C.FUNC_NAME LIKE '%车厂公司维护%'\n");
		sql.append("                           OR C.FUNC_NAME LIKE '%职位维护%')\n");
		List<Map<String, Object>> list = pageQuery(sql.toString(),null,dao.getFunName());
		return list;
	}
	//查询poseList
	public  List<Map<String, Object>> getposeList(String roleId){
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT T.POSE_ID\n");
		sql.append("  FROM TC_POSE T, TR_ROLE_POSE TR\n");  
		sql.append(" WHERE T.POSE_NAME LIKE '%超级用户%'\n");  
		sql.append("   AND TR.POSE_ID = T.POSE_ID\n");  
		sql.append("   AND tr.role_id="+roleId+"\n");
		List<Map<String, Object>> list = pageQuery(sql.toString(),null,dao.getFunName());
		return list;
	}
	//查询poseList
	public  List<Map<String, Object>> getfuncList(){
		StringBuffer sql= new StringBuffer();
		sql.append(" SELECT C.FUNC_ID\n");  
		sql.append("  FROM TC_FUNC C\n");  
		sql.append("  WHERE C.FUNC_NAME LIKE '%用户维护%'\n");  
		sql.append("  OR C.FUNC_NAME LIKE '%角色维护%'\n");  
		sql.append("  OR C.FUNC_NAME LIKE '%用户维护%'\n");  
		sql.append("  OR C.FUNC_NAME LIKE '%车厂公司维护%'\n");
		sql.append("  OR C.FUNC_NAME LIKE '%职位维护%'\n");
		List<Map<String, Object>> list = pageQuery(sql.toString(),null,dao.getFunName());
		return list;
	}
	
	public void insertBusinessPara(Long userId,Long yuanCompanyId,String newCompanyId){
		StringBuffer sql= new StringBuffer();
		sql.append("INSERT INTO TM_BUSINESS_PARA\n");
		sql.append("  (PARA_ID,\n");  
		sql.append("   TYPE_CODE,\n");  
		sql.append("   TYPE_NAME,\n");  
		sql.append("   PARA_NAME,\n");  
		sql.append("   PARA_VALUE,\n");  
		sql.append("   REMARK,\n");  
		sql.append("   CREATE_DATE,\n");  
		sql.append("   CREATE_BY,\n");  
		sql.append("   OEM_COMPANY_ID)\n");  
		sql.append("  SELECT PARA_ID,\n");  
		sql.append("         TYPE_CODE,\n");  
		sql.append("         TYPE_NAME,\n");  
		sql.append("         PARA_NAME,\n");  
		sql.append("         PARA_VALUE,\n");  
		sql.append("         REMARK,\n");  
		sql.append("         SYSDATE,\n");  
		sql.append("         "+userId+",\n");  
		sql.append("         "+newCompanyId+"\n");  
		sql.append("    FROM TM_BUSINESS_PARA\n");  
		sql.append("   WHERE OEM_COMPANY_ID = "+yuanCompanyId+"\n");
		update(sql.toString(),null);

	}
public void insertVariablePara(Long userId,Long yuanCompanyId,String newCompanyId){
    StringBuffer sql= new StringBuffer();
	sql.append("INSERT INTO TM_VARIABLE_PARA\n");
	sql.append("  (PARA_ID,\n");  
	sql.append("   PARA_TYPE,\n");  
	sql.append("   PARA_CODE,\n");  
	sql.append("   PARA_NAME,\n");  
	sql.append("   STATUS,\n");  
	sql.append("   ISSUE,\n");  
	sql.append("   REMARK,\n");  
	sql.append("   CREATE_DATE,\n");  
	sql.append("   CREATE_BY,\n");  
	sql.append("   OEM_COMPANY_ID)\n");  
	sql.append("  SELECT F_GETID(),\n");  
	sql.append("         PARA_TYPE,\n");  
	sql.append("         PARA_CODE,\n");  
	sql.append("         PARA_NAME,\n");  
	sql.append("         STATUS,\n");  
	sql.append("         ISSUE,\n");  
	sql.append("         REMARK,\n");  
	sql.append("         SYSDATE,\n");  
	sql.append("         "+userId+",\n");  
	sql.append("         "+newCompanyId+"\n");  
	sql.append("    FROM TM_VARIABLE_PARA\n");  
	sql.append("   WHERE OEM_COMPANY_ID = "+yuanCompanyId+"\n");
	sql.append("    AND  PARA_TYPE  LIKE '"+Constant.PARA_TYPE+"%'\n");
	update(sql.toString(),null);
}

	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}
}
