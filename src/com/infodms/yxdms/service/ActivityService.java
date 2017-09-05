package com.infodms.yxdms.service;

import java.util.List;
import java.util.Map;

import com.infodms.dms.bean.AclUserBean;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;


public interface ActivityService {

	PageResult<Map<String, Object>> ActivityVinManageData(RequestWrapper request, Integer pageSize, Integer currPage);

	PageResult<Map<String, Object>> ActivityVinData(RequestWrapper request,Integer pageSize, Integer currPage);

	int bntDelAll(RequestWrapper request);

	void expotActivityVinData(ActionContext act,PageResult<Map<String, Object>> list);

	PageResult<Map<String, Object>> openSubject(RequestWrapper request,Integer pageSize, Integer currPage);

	PageResult<Map<String, Object>> vinByDetail(Long dealerId,RequestWrapper request, Integer pageSize, Integer currPage);

	PageResult<Map<String, Object>> vinDetailByCount(Long dealerId,RequestWrapper request, Integer pageSize, Integer currPage);

	PageResult<Map<String, Object>> vinDetailByautomobile(Long currDealerId,RequestWrapper request, Integer pageSize, Integer currPage);

	PageResult<Map<String, Object>> vinDetailByCountFactory(String dealer_code,RequestWrapper request, Integer pageSize, Integer currPage);

	PageResult<Map<String, Object>> openActivity(RequestWrapper request,Integer pageSize, Integer currPage);

	PageResult<Map<String, Object>> activityTemplet(RequestWrapper request,Integer pageSize, Integer currPage);

	int templetAddSure(RequestWrapper request, AclUserBean loginUser);

	int deleteTemplet(RequestWrapper request);

	int templetPublish(RequestWrapper request);

	List<Map<String, Object>> findTempletMain(RequestWrapper request);

	List<Map<String, Object>> findTempletLab(RequestWrapper request);

	List<Map<String, Object>> findTempletPart(RequestWrapper request);

	List<Map<String, Object>> findTempletAcc(RequestWrapper request);

	List<Map<String, Object>> findTempletCom(RequestWrapper request);

	PageResult<Map<String, Object>> showSubject(RequestWrapper request,Integer pageSize, Integer currPage);

	int activityAddSure(RequestWrapper request, AclUserBean loginUser);

	PageResult<Map<String, Object>> activityList(RequestWrapper request,Integer pageSize, Integer currPage);

	Map<String, Object> findActivity(RequestWrapper request);

	PageResult<Map<String, Object>> relationShow(RequestWrapper request,Integer pageSize, Integer currPage);

	int cancelAcSure(RequestWrapper request);

	String findTempletByCamCode(RequestWrapper request);

	List<Map<String, Object>> findTempletMain(String id);

	List<Map<String, Object>> findTempletLab(String id);

	List<Map<String, Object>> findTempletPart(String id);

	List<Map<String, Object>> findTempletAcc(String id);

	List<Map<String, Object>> findTempletCom(String id);
    /**
     * 服务活动VIN明细展示 导出
     * @param currDealerId
     * @param request
     * @param pageSizeMax
     * @param currPage
     */
	void exportToexcel(ActionContext act,Long currDealerId, RequestWrapper request,Integer pageSizeMax, Integer currPage);


	PageResult<Map<String, Object>> QueryactivityByvin(RequestWrapper request,AclUserBean loginUser, Integer pageSize, Integer currPage);
}
