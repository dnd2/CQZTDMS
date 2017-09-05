package com.infodms.dms.dao.usermng;

import java.sql.ResultSet;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.common.Constant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.dao.common.BaseDao;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

public class UserRegionDao extends BaseDao<PO>{
	public static Logger logger = Logger.getLogger(UserRegionDao.class);
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public PageResult<Map<String, Object>> queryUserRegionRelation(String acnt, String name, String companyId, int curPage, int pageSize){
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT ROWNUM NUM, USER_ID, ACNT, NAME, USER_STATUS\n" );
		sql.append("FROM TC_USER\n" );
		sql.append("WHERE COMPANY_ID = ").append(companyId).append("\n");
		if(Utility.testString(acnt)){
			sql.append("AND ACNT LIKE '%").append(acnt).append("%'\n");
		}
		if(Utility.testString(name)){
			sql.append("AND NAME LIKE '%").append(name).append("%'\n");
		}
		PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
		return ps;
	}
	
	public Map<String, Object> getUserMap(String userId){
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT USER_ID, ACNT, NAME\n" );
		sql.append("FROM TC_USER\n" );
		sql.append("WHERE USER_ID = ").append(userId).append("\n");
		Map<String, Object> map = pageQueryMap(sql.toString(), null, getFunName());
		return map;
	}
	
	
	public PageResult<Map<String, Object>> queryRelationByUserList(String userId, int curPage, int pageSize){
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT A.REGION_NAME, B.ID\n" );
		sql.append("FROM TM_REGION A, TC_USER_REGION_RELATION B\n" );
		sql.append("WHERE A.REGION_CODE = B.REGION_CODE\n");
		sql.append("AND B.USER_ID = ").append(userId).append("\n");
		PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
		return ps;
	}
	
	public PageResult<Map<String, Object>> queryRegionList(String userId, String regionName, int curPage, int pageSize){
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT REGION_ID, REGION_CODE, REGION_NAME\n" );
		sql.append("FROM TM_REGION\n" );
		sql.append("WHERE REGION_TYPE = ").append(Constant.REGION_TYPE_02).append("\n");
		if(Utility.testString(userId)){
			sql.append("AND REGION_CODE NOT IN (\n" );
			sql.append("   SELECT REGION_CODE FROM TC_USER_REGION_RELATION WHERE USER_ID = ").append(userId).append("\n");
			sql.append("   )\n");
		}
		if(Utility.testString(regionName)){
			sql.append("AND REGION_NAME LIKE '%").append(regionName).append("%'\n");
		}
		PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
		return ps;
	}
	
	//zhumingwei 2011-7-13
	public PageResult<Map<String, Object>> queryModelGroupList(int curPage, int pageSize){
		StringBuffer sql= new StringBuffer();
		sql.append("select * from Tt_As_Wr_Model_Group g where g.wrgroup_type=10451001\n" );
		PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
		return ps;
	}
}
