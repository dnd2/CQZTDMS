package com.infodms.yxdms.service;

import java.util.List;
import java.util.Map;

public interface CommonService {
	
	/**
	 * 提供tablleName和idNam字段 id值 主键 来物理删除
	 */
	public int del(String tableName, String idName,String id);
	
	/**
	 * 根据数据库维护的按钮来查询数据
	 * @param mainTainSql
	 * @return
	 */
	public List<Map<String, Object>> findBtnQury(String mainTainSql);
	
	public List<?> findSelectList(String type,String exist,String noExist,String sql);
}
