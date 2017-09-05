package com.infodms.dms.service.impl;

import java.util.List;
import java.util.Map;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.dao.claim.application.ClaimBillStatusTrackDao;
import com.infodms.dms.po.FsFileuploadPO;
import com.infodms.dms.service.ClaimBillStatusTrackService;
import com.infoservice.mvc.context.RequestWrapper;

public class ClaimBillStatusTrackServiceImpl extends ClaimBillStatusTrackDao  implements ClaimBillStatusTrackService  {

	public Map<String, Object> pdiView(AclUserBean loginUser, RequestWrapper request){
		return super.pdiView(loginUser, request);
	}
	public List<Map<String, Object>> findLaboursById(RequestWrapper request){
		return super.findLaboursById( request);
	}
	public List<Map<String, Object>> findPartsById(RequestWrapper request){
		return super.findPartsById( request);
	}
	public Map<String, Object> findClaimPoById(RequestWrapper request){
		return super.findClaimPoById( request);
	}
	public Map<String, Object> findoutrepair(RequestWrapper request) {
		return super.findoutrepair( request);
	}
	public Map<String, Object> findoutrepairmoney(RequestWrapper request) {
		return super.findoutrepairmoney( request);
	}
	public Map<String, Object> findComPoById(RequestWrapper request) {
		return super.findComPoById( request);
	}
	public List<Map<String, Object>> findAccPoById(RequestWrapper request) {
		return super.findAccPoById(request);
	}
	public List<FsFileuploadPO> queryAttById(String ywzj){
		return super.queryAttById( ywzj);
		
	}
}
