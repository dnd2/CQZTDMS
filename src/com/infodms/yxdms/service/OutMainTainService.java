package com.infodms.yxdms.service;

import java.util.List;
import java.util.Map;

import com.infoservice.po3.bean.PageResult;

public interface OutMainTainService {
	public PageResult<Map<String,Object>> Maintainselect(Map<String,Object> paraMap,int curPage,int pageSize);//打开外出维修申请查询页面
	
	/**
	 * 保存数据
	 */
	//public void saveMainTain(TtAsEgressPO po,TtAsCustomerServicePO po1,String newCusFlag) throws Exception;//新增申请
	
	
	/**
	 * 审核
	 * 
	 */
//	public void Auditing(TtAsWrAuditingPO po,String status) throws Exception;//新增申请
	public List<Map<String, Object>> getListUserName(String s) throws Exception;
	public List<Map<String, Object>> getDealerInfos(String s) throws Exception;
	List<Map<String, Object>> specialRecord(String id)throws Exception;
}
