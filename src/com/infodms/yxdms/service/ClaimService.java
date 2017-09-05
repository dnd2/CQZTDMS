package com.infodms.yxdms.service;

import java.util.List;
import java.util.Map;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.po.FsFileuploadPO;
import com.infodms.dms.po.TtAsActivityPO;
import com.infodms.dms.po.TtAsActivityProjectPO;
import com.infodms.dms.po.TtAsWrApplicationExtPO;
import com.infodms.dms.po.TtAsWrApplicationPO;
import com.infodms.dms.po.TtAsWrNetitemPO;
import com.infodms.dms.po.TtAsWrOutrepairPO;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;

public interface ClaimService {

	PageResult<Map<String, Object>> pdiManageList(AclUserBean loginUser,RequestWrapper request, Integer pageSize, Integer currPage);

	int pdiAddSure(AclUserBean loginUser, RequestWrapper request);

	Map<String, Object> pdiView(AclUserBean loginUser, RequestWrapper request);

	int pdiDelete(RequestWrapper request);

	String checkPdiByVin(RequestWrapper request);

	PageResult<Map<String, Object>> keepFitManageList(AclUserBean loginUser,RequestWrapper request, Integer pageSize, Integer currPage);

	int keepFitDelete(RequestWrapper request);

	int keepFitCancel(RequestWrapper request);

	int keepFitAddSure(AclUserBean loginUser, RequestWrapper request);

	PageResult<Map<String, Object>> findRoKeepFit(AclUserBean loginUser,RequestWrapper request, Integer pageSize, Integer currPage);

	List<Map<String, Object>> findLaboursById(RequestWrapper request);

	List<Map<String, Object>> findPartsById(RequestWrapper request);

	Map<String, Object> findClaimPoById(RequestWrapper request);

	PageResult<Map<String, Object>> normalManageList(AclUserBean loginUser,RequestWrapper request, Integer pageSize, Integer currPage);

	List<FsFileuploadPO> queryAttById(String ywzj);

	PageResult<Map<String, Object>> findRoBaseInfo(AclUserBean loginUser,RequestWrapper request, Integer pageSize, Integer currPage);

	PageResult<Map<String, Object>> addPartNormal(AclUserBean loginUser,RequestWrapper request, Integer pageSize, Integer currPage);

	int normalAddSure(AclUserBean loginUser, RequestWrapper request);

	PageResult<Map<String, Object>> supplierCodeByPartCode(AclUserBean loginUser, RequestWrapper request, Integer pageSize, Integer currPage);

	int normalDelete(RequestWrapper request);

	Map<String, Object> findoutrepairmoney(RequestWrapper request);

	Map<String, Object> findoutrepair(RequestWrapper request);

	Map<String, Object> findComPoById(RequestWrapper request);

	List<Map<String, Object>> findAccPoById(RequestWrapper request);

	int normalAudit(AclUserBean loginUser, RequestWrapper request);

	PageResult<Map<String, Object>> findYsqBaseInfo(AclUserBean loginUser,RequestWrapper request, Integer pageSize, Integer currPage);

	Map<String, Object> queryYsqDataById(AclUserBean loginUser,RequestWrapper request);

	Map<String, Object> ysqPartdataById(AclUserBean loginUser,RequestWrapper request);

	String checkClaim(RequestWrapper request, AclUserBean loginUser);

	String pdiAddCheck(String param, RequestWrapper request,AclUserBean loginUser);

	int normalReportSubmit(String id);

	PageResult<Map<String, Object>> ClaimAbandonedQuery(AclUserBean loginUser,RequestWrapper request, Integer pageSize, Integer currPage);

	List<TtAsWrOutrepairPO> getOutDetail(String id);

	List<TtAsWrNetitemPO> getOther(String id);

	List<TtAsActivityPO> getADetail(String id);

	List<TtAsWrApplicationPO> selectttAsWrApplication(TtAsWrApplicationPO tawaPo);

	List<Map<String, Object>> getclaimAccessoryDtl(String claimNo);

	List<TtAsWrApplicationExtPO> getDetail(String id);

	List<TtAsWrApplicationExtPO> getBaseBean(String id);

	String getTimes(String vin);

	List<TtAsWrApplicationExtPO> getbaoy(String vin);

	List<Map<String, Object>> barcodePrintDoGet(RequestWrapper request);

	void updatePrint(String dtlIds);

	List<TtAsActivityProjectPO> selectTtAsActivityProject(TtAsActivityProjectPO jp);

	int updateprodutercode(RequestWrapper request);

	List<Map<String, Object>> showAllRecords(String id);
	Map<String, Object> findamount(AclUserBean loginUser, RequestWrapper request);

	void exportkeepFitManageList(PageResult<Map<String, Object>> list,ActionContext act, AclUserBean loginUser);

	void exportnormalManageList(PageResult<Map<String, Object>> list,ActionContext act, AclUserBean loginUser);

	void exportpdiManageList(PageResult<Map<String, Object>> list,ActionContext act, AclUserBean loginUser);

	int checkpartCode(AclUserBean loginUser, RequestWrapper request);

	int updatenewpartcode(AclUserBean loginUser, RequestWrapper request);


	PageResult<Map<String, Object>> viewbackaduitList(AclUserBean loginUser,RequestWrapper request, Integer pageSize, Integer currPage);

	List<Map<String, Object>> findCarsystemById(RequestWrapper request);

	int updateIsReturnById(AclUserBean loginUser, RequestWrapper request);

	List<Map<String, Object>> findCarsystemByvin(AclUserBean loginUser,RequestWrapper request);

	Map<String, Object> findYsqPoById(RequestWrapper request);

	





}
