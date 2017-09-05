package com.infodms.yxdms.service;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.bean.ClaimApproveAndStoredReturnInfoBean;
import com.infodms.dms.bean.TtAsWrBackListQryBean;
import com.infodms.dms.bean.TtAsWrOldPartBackListDetailBean;
import com.infodms.dms.bean.TtAsWrOldPartDetailListBean;
import com.infodms.dms.po.TmOldpartTransportPO;
import com.infodms.dms.po.TrReturnLogisticsPO;
import com.infodms.dms.po.TtAsPartBorrowPO;
import com.infodms.dms.po.TtAsWrOldReturnedDetailPO;
import com.infodms.dms.po.TtAsWrOldReturnedPO;
import com.infodms.dms.po.TtAsWrReturnedOrderPO;
import com.infodms.yxdms.entity.ysq.TtAsComRecordPO;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;



public interface OldReturnService {

	Map<String, Object> findReturnData(RequestWrapper request);

	PageResult<Map<String, Object>> returnAmountAuditListData(RequestWrapper request, Integer pageSize, Integer currPage);

	int returnAmountAudit(RequestWrapper request, AclUserBean loginUser);

	String checkBoxTheSame(RequestWrapper request);

	PageResult<Map<String, Object>> oldPartApplyList(RequestWrapper request,Integer pageSize, Integer currPage);
	
	PageResult<Map<String, Object>> oldPartApplyList1(RequestWrapper request,Integer pageSize, Integer currPage);

	int oldPartApplyAddSure(RequestWrapper request, AclUserBean loginUser);

	Map<String, Object> findOldPartApplyData(RequestWrapper request);

	int oldPartApplyAudit(RequestWrapper request);

	boolean checkDateOldReturn(RequestWrapper request, AclUserBean loginUser);

	Long insertreturnapply(RequestWrapper request);

	int oldParReport(RequestWrapper request);

	Map<String, Object> finddatabyreturnno(RequestWrapper request);

	PageResult<Map<String, Object>> oldPartApplyAudit(RequestWrapper request,
			Integer pageSize, Integer currPage);

	int updateOldPartApplybyID(RequestWrapper request);

	int updateOldPartApplybyID1(RequestWrapper request);

	int updateOldreturnbyID(RequestWrapper request);

	int updateOldreturnapplybyID(RequestWrapper request);

	int expotDataoldpartapply(PageResult<Map<String, Object>> list);

	int updateOldPartApplyresonbyID(RequestWrapper request);

	PageResult<Map<String, Object>> oldPartApplyAudit1(RequestWrapper request,
			Integer pageSize, Integer currPage);

	int oldPartcheck(RequestWrapper request);

	void updatereturnedstatus(RequestWrapper request);

	int updateOldreturnstatus(RequestWrapper request);

	int updatereturnedstatusbyreturnno(Long pkid);

	PageResult<Map<String, Object>> QueryEmergencyTracking(RequestWrapper request,AclUserBean loginUser, Integer pageSize, Integer currPage);

	int addsure(RequestWrapper request, AclUserBean loginUser);

	void EmergencyTrackExport(RequestWrapper request, AclUserBean loginUser,ActionContext act, Integer pageSizeMax, Integer currPage);

	List<TtAsPartBorrowPO> queryEmergencyremarkByid(RequestWrapper request,AclUserBean loginUser);

	ClaimApproveAndStoredReturnInfoBean getApproveAndStoredReturnInfo11(Map<String, String> params);

	List<Map<String, Object>> queryClaimBackDetailList2(Map<String, String> params);

	List<Map<String, Object>> queryClaimBackDetailList3(String claimId);

	List<Map<String, Object>> getDeductList();

	int oldPartSignAuditIn(RequestWrapper request, AclUserBean loginUser);

	List historyoldreturnquery(RequestWrapper request,AclUserBean loginUser);

	PageResult<Map<String, Object>> QualityAuditlistQuery(RequestWrapper request, AclUserBean loginUser, Integer pageSize,Integer currPage);

	PageResult<Map<String, Object>> returnAmountAuditListNew(RequestWrapper request, AclUserBean loginUser, Integer pageSize,Integer currPage);

	int returnAmountAuditNew(RequestWrapper request, AclUserBean loginUser);

	PageResult<TtAsWrBackListQryBean> queryClaimBackList(RequestWrapper request,ActionContext act, AclUserBean loginUser, Integer currPage, Integer pageSize);

	TtAsWrOldPartBackListDetailBean getClaimBackInfo(RequestWrapper request,AclUserBean loginUser);

	List<TtAsWrOldPartDetailListBean> queryClaimBackDetailList(RequestWrapper request, AclUserBean loginUser,TtAsWrOldPartBackListDetailBean detailInfoBean);

	List<TtAsPartBorrowPO> queryTtAsPartBorrow(TtAsPartBorrowPO borrow);

	List<TtAsWrOldReturnedDetailPO> getOldReturnedDetailPOByReturnId(RequestWrapper request, int i, int j);
	List<TtAsWrOldReturnedDetailPO> getOldReturnedDetailPOByReturnId1(RequestWrapper request, int i, int j);
	int updateClaimBackOrdMainInfo(RequestWrapper request, AclUserBean loginUser);

	TtAsWrOldPartBackListDetailBean getClaimBackInfo(RequestWrapper request,int i, int j);

	List<TtAsWrOldPartDetailListBean> queryClaimBackDetailList(RequestWrapper request);

	void insertTtAsComRecord(RequestWrapper request, AclUserBean loginUser);

	TtAsWrOldReturnedPO queryTtAsWrOldReturnedByid(RequestWrapper request);

	TrReturnLogisticsPO queryTrReturnLogistics(RequestWrapper request);

	TtAsWrReturnedOrderPO queryTtAsWrReturnedOrder(RequestWrapper request,TrReturnLogisticsPO sp);

	List<TmOldpartTransportPO> queryGetTransPList(Long currDealerId);

	List<Map<String, Object>> getStr(Long l);

	List<Map<String, Object>> getStr(List<TmOldpartTransportPO> sList,String returnType);

	int updateTtAsWrOldReturned(RequestWrapper request,AclUserBean loginUser,List<TtAsWrOldReturnedDetailPO> returnDetaiList);

	void insertTtAsComRecord1(RequestWrapper request, AclUserBean loginUser);

	TtAsWrOldPartBackListDetailBean getClaimBackInfo1(RequestWrapper request,int i, int j);

	List<TtAsWrOldPartDetailListBean> queryClaimBackDetailList1(RequestWrapper request, AclUserBean loginUser,TtAsWrOldPartBackListDetailBean detailInfoBean);


}
