package com.infodms.dms.service;

import java.util.List;
import java.util.Map;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.po.FsFileuploadPO;
import com.infoservice.mvc.context.RequestWrapper;

public interface ClaimBillStatusTrackService {

	Map<String, Object> pdiView(AclUserBean loginUser, RequestWrapper request);

	List<Map<String, Object>> findLaboursById(RequestWrapper request);

	List<Map<String, Object>> findPartsById(RequestWrapper request);

	Map<String, Object> findClaimPoById(RequestWrapper request);

	Map<String, Object> findoutrepair(RequestWrapper request);

	Map<String, Object> findoutrepairmoney(RequestWrapper request);

	Map<String, Object> findComPoById(RequestWrapper request);

	List<Map<String, Object>> findAccPoById(RequestWrapper request);

	List<FsFileuploadPO> queryAttById(String ywzj);

}
