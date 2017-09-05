package com.infodms.yxdms.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.tag.BaseUtils;
import com.infodms.yxdms.dao.GoodClaimDAO;
import com.infodms.yxdms.dao.SpecialDAO;
import com.infodms.yxdms.entity.special.TtAsSpecialAmountRangePO;
import com.infodms.yxdms.service.GoodClaimService;
import com.infodms.yxdms.service.SpecialService;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;

public class GoodClaimServiceImpl extends GoodClaimDAO implements GoodClaimService{

	public PageResult<Map<String, Object>> specialDealerList(RequestWrapper request, AclUserBean loginUser, Integer pageSize,Integer currPage) {
		return super.specialDealerList(request,loginUser,pageSize,currPage);
	}

	public String getSpecialNo() {
		return super.getSpecialNo();
	}

	public PageResult<Map<String, Object>> qureyAllClaim(RequestWrapper request, AclUserBean loginUser, Integer pageSize,Integer currPage) {
		return super.qureyAllClaim(request,loginUser,pageSize,currPage);
	}

	public Map<String, Object> findDataByVin(String vin) {
		return super.findDataByVin(vin);
	}

	public PageResult<Map<String, Object>> queryPartCode(RequestWrapper request, Integer pageSize, Integer currPage) {
		return super.queryPartCode(request,pageSize,currPage);
	}

	public PageResult<Map<String, Object>> querySupplierCode(RequestWrapper request, Integer pageSize, Integer currPage) {
		return super.querySupplierCode(request,pageSize,currPage);
	}

	public int saveOrUpdate(RequestWrapper request, AclUserBean loginUser) {
		return super.saveOrUpdate(request,loginUser);
	}

	public int delSpe(String special_type, String id) {
		return super.delSpe(special_type,id);
	}

	public Map<String, Object> findSpeData(String id) {
		return super.findSpeData(id);
	}
	
	public List<Map<String, Object>> findPartSupply(String id) {
		return super.findPartSupply(id);
	}

	public PageResult<Map<String, Object>> specialServiceManagerList(RequestWrapper request, AclUserBean loginUser, Integer pageSize,Integer currPage) {
		return super.specialServiceManagerList(request,loginUser,pageSize,currPage);
	}

	public PageResult<Map<String, Object>> specialClaimSettlementList(TtAsSpecialAmountRangePO po,RequestWrapper request, AclUserBean loginUser, Integer pageSize,Integer currPage) {
		return super.specialClaimSettlementList(po,request,loginUser,pageSize,currPage);
	}

	public PageResult<Map<String, Object>> specialTecSupportList(TtAsSpecialAmountRangePO po,RequestWrapper request, AclUserBean loginUser, Integer pageSize,Integer currPage) {
		return super.specialTecSupportList(po,request,loginUser,pageSize,currPage);
	}
	
	public PageResult<Map<String, Object>> specialTecSeSupportList(RequestWrapper request, AclUserBean loginUser, Integer pageSize,Integer currPage) {
		return super.specialTecSeSupportList(request,loginUser,pageSize,currPage);
	}

	public PageResult<Map<String, Object>> specialRegionalDirectorList(TtAsSpecialAmountRangePO po,RequestWrapper request, AclUserBean loginUser, Integer pageSize,Integer currPage) {
		return super.specialRegionalDirectorList(po,request,loginUser,pageSize,currPage);
	}

	public PageResult<Map<String, Object>> specialRegionalManagerList(TtAsSpecialAmountRangePO po,RequestWrapper request, AclUserBean loginUser, Integer pageSize,Integer currPage) {
		return super.specialRegionalManagerList(po,request,loginUser,pageSize,currPage);
	}

	public int audit(RequestWrapper request, AclUserBean loginUser) {
		return super.audit(request,loginUser);
	}

	public int sureInsert(RequestWrapper request,String view,AclUserBean loguser) {
		return super.sureInsert(request,view,loguser);
	}

	public PageResult<Map<String, Object>> findreturncarAndclaim(RequestWrapper request, Integer pageSize, Integer currPage) {
		return super.findreturncarAndclaim(request,pageSize,currPage);
	}

	public Map<String, Object> findreturncarByid(RequestWrapper request) {
		return super.findreturncarByid(request);
	}

	public int updateReturncar(RequestWrapper request,AclUserBean loginUser, String view) {
		return super.updateReturncar(request,loginUser,view);
	}

	public Map<String, Object> findDateapplyByUserid(Long userId) {
		return super.findDateapplyByUserid(userId);
	}

	public PageResult<Map<String, Object>> findspecialappno(RequestWrapper request, AclUserBean loguser, Integer pageSize, Integer currPage) {
		return super.findspecialappno(request, loguser,pageSize,currPage);
	}

	public Map<String, Object> findDateByappno(RequestWrapper request) {
		return super.findDateByappno(request);
	}

	public PageResult<Map<String, Object>> findDatespecialapply(RequestWrapper request, AclUserBean loginUser, Integer pageSize,Integer currPage) {
		return super.findDatespecialapply(request,loginUser,pageSize,currPage);
	}

	public Map<String, Object> viewSpecialApplyDetailed(RequestWrapper request) {
		return super.viewSpecialApplyDetailed(request);
	}

	public List<Map<String,Object>> checkapplyno(RequestWrapper request,AclUserBean loginUser, Integer pageSize, Integer currPage) {
		return super.checkapplyno(request,loginUser,pageSize,currPage);
	}

	@SuppressWarnings("unchecked")
	public void Toexcelspecialapply(PageResult<Map<String, Object>> list) {
		try {
			String[] head = new String[] {"大区" ,"服务商代码","服务商简称", "申请单号","VIN", "申请日期", "审核日期",
					"供应商代码","状态","申请类型", "申请金额","审批金额","车型"};
			List<Map<String, Object>> records = list.getRecords();
			List params = new ArrayList();
			if (records != null && records.size() > 0) {
				for (Map<String, Object> map : records) {
					String[] detail = new String[13];
					String SPECIAL_TYPE="";
					if("1".equals(BaseUtils.checkNull(map.get("SPECIAL_TYPE")))){
				            SPECIAL_TYPE = "善意索赔";
				        }
				        if("0".equals(BaseUtils.checkNull(map.get("SPECIAL_TYPE")))){
				            SPECIAL_TYPE = "退换车";
				        }
					detail[0] = BaseUtils.checkNull(map.get("ROOT_ORG_NAME"));
					detail[1] = BaseUtils.checkNull(map.get("DEALER_CODE"));
					detail[2] = BaseUtils.checkNull(map.get("DEALER_SHORTNAME"));
					detail[3] = BaseUtils.checkNull(map.get("APPLY_NO"));
					detail[4] = BaseUtils.checkNull(map.get("VIN"));
					detail[5] = BaseUtils.checkNull(map.get("APPLY_DATE"));
					detail[6] = BaseUtils.checkNull(map.get("AUDIT_DATE"));
					detail[7] = BaseUtils.checkNull(map.get("SUPPLY_CODE_DEALER"));
					detail[8] = BaseUtils.checkNull(map.get("CODE_DESC"));
					detail[9] = SPECIAL_TYPE;
					detail[10] = BaseUtils.checkNull(map.get("APPLY_AMOUNT"));
					detail[11] = BaseUtils.checkNull(map.get("APPROVAL_AMOUNT"));
					detail[12] = BaseUtils.checkNull(map.get("MODEL_NAME"));
					params.add(detail);
				}
			}
			String systemDateStr = BaseUtils.getSystemDateStr();
			BaseUtils.toExcel(ActionContext.getContext().getResponse(), ActionContext.getContext().getRequest(), head, params, "退换车及善意索赔"+systemDateStr+".xls", "导出数据", null);
			} catch (Exception e) {
				e.printStackTrace();
			}
	}

	public int updatespecialapply(RequestWrapper request,AclUserBean loginUser, String type) {
		return super.specialapply(request,loginUser,type);
	}

	public int delSpecialapply(String id) {
		return super.delSpecialapply(id);
	}

	public int updatespecialapplyreport(RequestWrapper request,AclUserBean loginUser, String id) {
		return super.updatespecialapplyreport(request,loginUser,id);
	}

	public Map<String, Object> queryByapplyno(
			AclUserBean loginUser, RequestWrapper request) {
		return super.queryByapplyno(loginUser,request);
	}

	public Map<String, Object> queryByCLAIMNO(AclUserBean loginUser,
			RequestWrapper request) {
		return super.queryByCLAIMNO(loginUser,request);
	}

	public Map<String, Object> findtotleamount(RequestWrapper request, AclUserBean loginUser) {
		return super.findtotleamount(request,loginUser);
	}

	public Map<String, Object> findreturncarByvin(RequestWrapper request) {
		return super.findreturncarByvin(request);
	}

	public Double Querycountspecil(AclUserBean loginUser, RequestWrapper request) {
		return super.Querycountspecil(loginUser,request);
	}

	public PageResult<Map<String, Object>> specialstatusTrackQuery(RequestWrapper request, AclUserBean loginUser, Integer pageSize,Integer currPage) {
		return super.specialstatusTrackQuery( request,  loginUser,  pageSize, currPage);
	}

	public int CancelAudit(RequestWrapper request, AclUserBean loginUser) {
		return super.CancelAudit( request,  loginUser);
	}
	public List<Map<String, Object>> specialRecord(String id) {
	// TODO Auto-generated method stub
		return super.specialRecord(id);
	}	
}
