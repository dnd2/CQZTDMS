package com.infodms.yxdms.service;

import java.util.List;
import java.util.Map;

import com.infodms.dms.bean.AclUserBean;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;
import com.infoservice.po3.bean.PageResult;

public interface MainTainService {

	PageResult<Map<String, Object>> keepFitTemplateData(RequestWrapper request, Integer pageSize, Integer currPage);

	PageResult<Map<String, Object>> addLabour(RequestWrapper request,Integer pageSize, Integer currPage);

	PageResult<Map<String, Object>> addPart(RequestWrapper request,Integer pageSize, Integer currPage);

	int addMainTainCommit(RequestWrapper request, AclUserBean loginUser);

	List<Map<String, Object>> findLaboursById(RequestWrapper request);

	List<Map<String, Object>> findPartsById(RequestWrapper request);

	Map<String, Object> findKeepFit(RequestWrapper request);

	int publish(RequestWrapper request);

	int deleteMainTain(RequestWrapper request);

	Map<String, Object> checkIsFirst(RequestWrapper request);

	PageResult<Map<String, Object>> findLabPartRelation(RequestWrapper request, Integer pageSize, Integer currPage);

	PageResult<Map<String, Object>> findaddLabour(RequestWrapper request,Integer pageSize, Integer currPage);

	int insertRalation(RequestWrapper request, AclUserBean loginUser);

	int statusDel(RequestWrapper request);

	PageResult<Map<String, Object>> ysqPartData(RequestWrapper request,Integer pageSize, Integer currPage);

	PageResult<Map<String, Object>> addYsqPartData(RequestWrapper request,Integer pageSize, Integer currPage);

	int insertYsqPart(RequestWrapper request, AclUserBean loginUser);

	PageResult<Map<String, Object>> emergencyMainTain(RequestWrapper request,Integer pageSize, Integer currPage);

	int addEmergency(RequestWrapper request, AclUserBean loginUser);

	Map<String, Object> viewEmergency(RequestWrapper request, String param);

	PageResult<Map<String, Object>> mailList(RequestWrapper request,AclUserBean loginUser, Integer pageSize, Integer currPage);

	int addMailListSure(RequestWrapper request, AclUserBean loginUser);

	List findTtasrolabour(String trim);

	List findTtaswrwrlabinfo(String trim);

	void insertlabpartRelation(RequestWrapper request, AclUserBean loginUser);

	void downExecl(ResponseWrapper response, RequestWrapper request,AclUserBean loginUser);

	PageResult<Map<String, Object>> queryPartCode(RequestWrapper request,AclUserBean loginUser, Integer pageSize, Integer currPage);

	int delYsqPart(String param);

	int delYsqPart1(RequestWrapper request, AclUserBean loginUser);

	int insertoldoutpart(RequestWrapper request, AclUserBean loginUser);

	PageResult<Map<String, Object>> queryWinterMaintenancelist(RequestWrapper request, AclUserBean loginUser, Integer pageSize,Integer currPage);

	PageResult<Map<String, Object>> queryDealer(RequestWrapper request,AclUserBean loginUser, Integer pageSize, Integer currPage);

	String insertWinterMaintenDealer(RequestWrapper request,AclUserBean loginUser);

	PageResult<Map<String, Object>> queryWinterDetail(RequestWrapper request,AclUserBean loginUser);

	String UpdateWinterMainten(RequestWrapper request, AclUserBean loginUser);

	List<Map<String, Object>> winterMaintenView(RequestWrapper request, AclUserBean loginUser);

	PageResult<Map<String, Object>> querychoosepageCode(RequestWrapper request,AclUserBean loginUser, Integer pageSize, Integer currPage);

	PageResult<Map<String, Object>> queryConfiguration(RequestWrapper request,AclUserBean loginUser, Integer pageSize, Integer currPage);

	PageResult<Map<String, Object>> queryConfigurationBycode(RequestWrapper request, AclUserBean loginUser, Integer pageSize,Integer currPage);

	PageResult<Map<String, Object>> queryConfigurationById(RequestWrapper request, AclUserBean loginUser, Integer pageSize,Integer currPage);

	List<Map<String, Object>> queryWinterById(String str,AclUserBean loginUser);

	List<Map<String, Object>> queryWinterById(RequestWrapper request,AclUserBean loginUser);

}
