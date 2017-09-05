package com.infodms.yxdms.service;

import java.util.List;
import java.util.Map;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.yxdms.entity.maintain.TtAsYsqPartPO;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;

public interface YsqService {

	PageResult<Map<String, Object>> ysqEngineList(RequestWrapper request,Integer pageSize, Integer currPage);

	PageResult<Map<String, Object>> ysqDealerList(RequestWrapper request,AclUserBean loginUser, Integer pageSize, Integer currPage);

	PageResult<Map<String, Object>> ysqTechList(RequestWrapper request,AclUserBean loginUser, Integer pageSize, Integer currPage);

	Map<String, Object> getPartInfo(String mainPartId);

	int ysqAddSure(RequestWrapper request, AclUserBean loginUser);

	List<Map<String, Object>> findYsqData(RequestWrapper request);

	PageResult<Map<String, Object>> ysqTechDirectorList(RequestWrapper request,AclUserBean loginUser, Integer pageSize, Integer currPage);

	String jugeYsq(RequestWrapper request);

	int auditYsq(RequestWrapper request, AclUserBean loginUser);

	List<Map<String, Object>> findYsqRecords(RequestWrapper request);

	int showWorkFlow(RequestWrapper request);

	int auditRebut(RequestWrapper request, AclUserBean loginUser);

	int ysqReportSubmit(String param,RequestWrapper request,AclUserBean loginUser);

	List<Map<String, Object>> findYsqRecords(String param);

	String changeByYsqRo(String ysqNo);

	Map<String,Object> judgePartUseType(RequestWrapper request);

	int auditEidt(RequestWrapper request, AclUserBean loginUser);

	List<TtAsYsqPartPO> checkpartCodeToysq(AclUserBean loginUser,RequestWrapper request);

	
	
}
