package com.infodms.dms.dao.common;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.infodms.dms.dao.sales.ordermanage.carSubmission.CarSubmissionQueryDao;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

public class DealerShowDao extends BaseDao {

	@Override
	protected PO wrapperPO(ResultSet rs, int idx)
	{
		return null;
	}
	
	private static final DealerShowDao dao = new DealerShowDao();
	
	public static final DealerShowDao getInstance() {
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
	public PageResult<Map<String, Object>> selectDealer(HashMap<String, Object> hsMap, int curPage, int pageSize, String command)
	{
		String dealerCode = hsMap.get("dealerCode").toString();
		String dealerName = hsMap.get("dealerName").toString();
		String dealerType = hsMap.get("dealerType").toString();
		String dealerLevel = hsMap.get("dealerLevel").toString();
		String ids[] = (String[])hsMap.get("ids");
		
		List<Object> params = new ArrayList<Object>();
		StringBuffer sbSql = new StringBuffer();
		
		sbSql.append("SELECT A.DEALER_NAME, A.DEALER_CODE, A.DEALER_ID\n");
		sbSql.append("  FROM TM_DEALER A, VW_ORG_DEALER B\n");
		sbSql.append(" WHERE A.DEALER_ID = B.DEALER_ID\n");
		
		if(!"".equals(dealerType)) {
			sbSql.append("   AND A.DEALER_TYPE = ?\n");
			params.add(dealerType);
		}
		if(!"".equals(dealerLevel)) {
			sbSql.append("   AND A.DEALER_LEVEL = ?");
			params.add(dealerLevel);
		}
		if(!"".equals(dealerCode)) {
			sbSql.append("   AND A.DEALER_CODE LIKE ?");
			params.add("%"+dealerCode+"%");
		}
		if(!"".equals(dealerName)) {
			sbSql.append("   AND A.DEALER_NAME LIKE ?");
			params.add("%"+dealerName+"%");
		}

		if(!"".equals(ids) && ids != null) {
			CarSubmissionQueryDao queryDao = CarSubmissionQueryDao.getInstance();
			sbSql.append("	AND A.DEALER_ID NOT IN("+queryDao.getSqlBuffer(ids, params)+")");
		}
		sbSql.append(" ORDER BY A.DEALER_NAME");
		
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
