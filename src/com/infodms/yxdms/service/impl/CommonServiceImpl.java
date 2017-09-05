package com.infodms.yxdms.service.impl;

import java.util.List;
import java.util.Map;

import com.infodms.yxdms.dao.CommonDAO;
import com.infodms.yxdms.service.CommonService;

public class CommonServiceImpl extends CommonDAO implements CommonService{

	/**
	 * 1 为成功 -1 为失败
	 */
	public int del(String tableName, String idName,String id) {
		return super.del(tableName,idName,id);
	}

	public List<Map<String, Object>> findBtnQury(String mainTainSql) {
		return super.findBtnQury(mainTainSql);
	}

	public List<?> findSelectList(String type, String exist, String noExist,String sql) {
		return super.findSelectList(type,exist,noExist,sql);
	}
	
}
