package com.infodms.dms.dao.common;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.infodms.dms.dao.sales.ordermanage.carSubmission.CarSubmissionQueryDao;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

public class OrgShowDao extends BaseDao {

	@Override
	protected PO wrapperPO(ResultSet rs, int idx)
	{
		return null;
	}
	
	private static final OrgShowDao dao = new OrgShowDao();
	
	public static final OrgShowDao getInstance() {
		return dao;
	}

	/**
	 * 查询组织数据
	 * 
	 * @param hsMap
	 * @param command 
	 * @param pageSize 
	 * @param curPage 
	 * @return
	 */
	public PageResult<Map<String, Object>> selectOrgArea(HashMap<String, Object> hsMap, int curPage, int pageSize, String command)
	{
		String orgCode = hsMap.get("orgCode").toString();
		String orgName = hsMap.get("orgName").toString();
		String orgLevel = hsMap.get("orgLevel").toString();
		String dutyType = hsMap.get("dutyType").toString();
		String ids[] = (String[])hsMap.get("ids");
		
		List<Object> params = new ArrayList<Object>();
		StringBuffer sbSql = new StringBuffer();
		
		sbSql.append("SELECT T.ORG_ID, T.ORG_CODE, T.ORG_NAME\n");
		sbSql.append("  FROM TM_ORG T\n");
		sbSql.append(" WHERE T.STATUS = 10011001\n");
		if(!"".equals(dutyType)) {
			sbSql.append("   AND T.DUTY_TYPE = ?\n");
			params.add(dutyType);
		}
		if(!"".equals(orgCode)) {
			sbSql.append("   AND t.ORG_CODE LIKE ?\n");
			params.add("%"+orgCode+"%");
		}
		if(!"".equals(orgLevel)) {
			sbSql.append("   AND T.ORG_LEVEL = ?\n"); 
			params.add(orgLevel);
		}
		if(!"".equals(orgName)) {
			sbSql.append("   AND T.ORG_NAME LIKE ?\n"); 
			params.add("%"+orgName+"%");
		}
		if(!"".equals(ids) && ids != null) {
			CarSubmissionQueryDao queryDao = CarSubmissionQueryDao.getInstance();
			sbSql.append("	AND T.ORG_ID NOT IN("+queryDao.getSqlBuffer(ids, params)+")");
		}
		sbSql.append(" ORDER BY T.NAME_SORT");
		
		if("".equals(command)) {
			return dao.pageQuery(sbSql.toString(), params, getFunName(), pageSize, curPage);
		} else {
			PageResult<Map<String, Object>> ps = new PageResult<Map<String,Object>>();
			List<Map<String, Object>> list = dao.pageQuery(sbSql.toString(), params, getFunName());
			
			int page = list.size() > 0 ? 1 : 0;
			ps.setCurPage(page);
			ps.setPageSize(page);
			ps.setRecords(list);
			ps.setTotalPages(page);
			ps.setTotalRecords(list.size());
			
			return ps;
		}
	}
	
}
