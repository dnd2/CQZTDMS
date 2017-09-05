package com.infodms.yxdms.service;

import java.util.List;
import java.util.Map;

import com.infodms.dms.bean.AclUserBean;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;


public interface SpecialService {

	PageResult<Map<String, Object>> specialDealerList(RequestWrapper request,AclUserBean loginUser, Integer pageSize, Integer currPage);

	String getSpecialNo();

	PageResult<Map<String, Object>> qureyAllClaim(RequestWrapper request,AclUserBean loginUser, Integer pageSize,Integer currPage);

	Map<String, Object> findDataByVin(String vin);

	PageResult<Map<String, Object>> queryPartCode(RequestWrapper request,Integer pageSize, Integer currPage);

	PageResult<Map<String, Object>> querySupplierCode(RequestWrapper request,Integer pageSize, Integer currPage);

	int saveOrUpdate(RequestWrapper request, AclUserBean loginUser);

	int delSpe(String special_type, String id);

	Map<String, Object> findSpeData(String special_type, String id);

	PageResult<Map<String, Object>> specialServiceManagerList(RequestWrapper request, AclUserBean loginUser, Integer pageSize,Integer currPage);

	PageResult<Map<String, Object>> specialClaimSettlementList(RequestWrapper request, AclUserBean loginUser, Integer pageSize,Integer currPage);

	PageResult<Map<String, Object>> specialTecSupportList(RequestWrapper request, AclUserBean loginUser, Integer pageSize,Integer currPage);

	PageResult<Map<String, Object>> specialRegionalDirectorList(RequestWrapper request, AclUserBean loginUser, Integer pageSize,Integer currPage);

	PageResult<Map<String, Object>> specialRegionalManagerList(RequestWrapper request, AclUserBean loginUser, Integer pageSize,Integer currPage);

	int audit(RequestWrapper request, AclUserBean loginUser);

	int sureInsert(RequestWrapper request,String view,AclUserBean loguser);


	PageResult<Map<String, Object>> findreturncarAndclaim(RequestWrapper request, Integer pageSize, Integer currPage);

	Map<String, Object> findreturncarByid(RequestWrapper request);

	int updateReturncar(RequestWrapper request,AclUserBean loginUser, String view);

	Map<String, Object> findDateapplyByUserid(Long userId);

	PageResult<Map<String, Object>> findspecialappno(RequestWrapper request,AclUserBean loguser,Integer pageSize, Integer currPage);

	Map<String, Object> findDateByappno(RequestWrapper request);

	PageResult<Map<String, Object>> findDatespecialapply(RequestWrapper request, AclUserBean loginUser, Integer pageSize,Integer currPage);

	Map<String, Object> viewSpecialApplyDetailed(RequestWrapper request);


	List<Map<String,Object>> checkapplyno(RequestWrapper request,AclUserBean loginUser, Integer pageSize, Integer currPage);

	void Toexcelspecialapply(PageResult<Map<String, Object>> list);

	int updatespecialapply(RequestWrapper request, AclUserBean loginUser,String type);

	int delSpecialapply(String id);

	int updatespecialapplyreport(RequestWrapper request,AclUserBean loginUser, String id);


	Map<String, Object> queryByapplyno(AclUserBean loginUser,RequestWrapper request);

	Map<String, Object> queryByCLAIMNO(AclUserBean loginUser,RequestWrapper request);

	Map<String, Object> findtotleamount(RequestWrapper request, AclUserBean loginUser);

	Map<String, Object> findreturncarByvin(RequestWrapper request);

	Double Querycountspecil(AclUserBean loginUser, RequestWrapper request);

	PageResult<Map<String, Object>> specialstatusTrackQuery(RequestWrapper request, AclUserBean loginUser, Integer pageSize,Integer currPage);

	int CancelAudit(RequestWrapper request, AclUserBean loginUser);
}
