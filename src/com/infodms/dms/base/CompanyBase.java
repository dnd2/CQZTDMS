package com.infodms.dms.base;

import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.dao.relation.CompanyRelationDAO;
import com.infoservice.po3.bean.PageResult;

public class CompanyBase {
	public Logger logger = Logger.getLogger(CompanyBase.class) ;
	
	CompanyRelationDAO dao = CompanyRelationDAO.getInstance() ;
	
	public PageResult<Map<String, Object>> getCompanyByOtherQuery(Map<String, String> map,int pageSize,int curPage) {
		return dao.getCompanyByOtherQuery(map, pageSize, curPage) ;
	}
}
