package com.infodms.yxdms.service.impl;

import java.util.List;
import java.util.Map;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.yxdms.dao.YsqDAO;
import com.infodms.yxdms.entity.maintain.TtAsYsqPartPO;
import com.infodms.yxdms.service.YsqService;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;

public class YsqServiceImpl extends YsqDAO implements YsqService{

	public PageResult<Map<String, Object>> ysqEngineList(RequestWrapper request, Integer pageSize, Integer currPage) {
		return super.ysqEngineList(request,pageSize,currPage);
	}

	public PageResult<Map<String, Object>> ysqDealerList(RequestWrapper request, AclUserBean loginUser, Integer pageSize,Integer currPage) {
		return super.ysqDealerList(request,loginUser,pageSize,currPage);
	}

	public PageResult<Map<String, Object>> ysqTechList(RequestWrapper request,AclUserBean loginUser, Integer pageSize, Integer currPage) {
		return super.ysqTechList(request,loginUser,pageSize,currPage);
	}

	public Map<String, Object> getPartInfo(String mainPartId) {
		return super.getPartInfo(mainPartId);
	}

	public int ysqAddSure(RequestWrapper request, AclUserBean loginUser) {
		return super.ysqAddSure(request,loginUser);
	}

	public List<Map<String, Object>> findYsqData(RequestWrapper request) {
		return super.findYsqData(request);
	}

	public PageResult<Map<String, Object>> ysqTechDirectorList(RequestWrapper request, AclUserBean loginUser, Integer pageSize,Integer currPage) {
		return super.ysqTechDirectorList(request,loginUser,pageSize,currPage);
	}

	public String jugeYsq(RequestWrapper request) {
		return super.jugeYsq(request);
	}

	public int auditYsq(RequestWrapper request,AclUserBean loginUser) {
		return super.auditYsq(request,loginUser);
	}

	public List<Map<String, Object>> findYsqRecords(RequestWrapper request) {
		return super.findYsqRecords(request);
	}

	public int showWorkFlow(RequestWrapper request) {
		return super.showWorkFlow(request);
	}

	public int auditRebut(RequestWrapper request, AclUserBean loginUser) {
		return super.auditRebut(request,loginUser);
	}

	public int ysqReportSubmit(String id,RequestWrapper request,AclUserBean loginUser) {
		return super.ysqReportSubmit(id, request, loginUser);
	}

	public List<Map<String, Object>> findYsqRecords(String id) {
		return super.findYsqRecords(id);
	}

	public String changeByYsqRo(String ysqNo) {
		return super.changeByYsqRo(ysqNo);
	}

	public Map<String,Object> judgePartUseType(RequestWrapper request) {
		return super.judgePartUseType(request);
	}

	public int auditEidt(RequestWrapper request, AclUserBean loginUser) {
		return super.auditEidt(request,loginUser);
	}

	public List<TtAsYsqPartPO> checkpartCodeToysq(AclUserBean loginUser,RequestWrapper request) {
		return super.checkpartCodeToysq(loginUser,request);
	}
	
}
