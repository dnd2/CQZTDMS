package com.infodms.yxdms.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.bean.ClaimApproveAndStoredReturnInfoBean;
import com.infodms.dms.bean.TtAsWrBackListQryBean;
import com.infodms.dms.bean.TtAsWrOldPartBackListDetailBean;
import com.infodms.dms.bean.TtAsWrOldPartDetailListBean;
import com.infodms.dms.common.tag.BaseUtils;
import com.infodms.dms.po.TmOldpartTransportPO;
import com.infodms.dms.po.TrReturnLogisticsPO;
import com.infodms.dms.po.TtAsPartBorrowPO;
import com.infodms.dms.po.TtAsWrOldReturnedDetailPO;
import com.infodms.dms.po.TtAsWrOldReturnedPO;
import com.infodms.dms.po.TtAsWrReturnedOrderPO;
import com.infodms.yxdms.dao.OldReturnDAO;
import com.infodms.yxdms.service.OldReturnService;
import com.infodms.yxdms.utils.DaoFactory;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;


public class OldReturnServiceImpl extends OldReturnDAO implements OldReturnService{

	public Map<String, Object> findReturnData(RequestWrapper request) {
		String id = DaoFactory.getParam(request, "id");
		return super.findReturnData(id);
	}

	public PageResult<Map<String, Object>> returnAmountAuditListData(RequestWrapper request, Integer pageSize, Integer currPage) {
		return super.returnAmountAuditListData(request,pageSize,currPage);
	}
	

	public int returnAmountAudit(RequestWrapper request, AclUserBean loginUser) {
		String id = DaoFactory.getParam(request, "id");
		String price = DaoFactory.getParam(request, "price");
		String signRemark = DaoFactory.getParam(request, "signRemark");
		return super.returnAmountAudit(id,price,signRemark,loginUser);
	}

	public String checkBoxTheSame(RequestWrapper request) {
		return super.checkBoxTheSame(request);
	}

	public PageResult<Map<String, Object>> oldPartApplyList(RequestWrapper request, Integer pageSize, Integer currPage) {
		return super.oldPartApplyList(request,pageSize,currPage);
	}

	public int oldPartApplyAddSure(RequestWrapper request, AclUserBean loginUser) {
		return super.oldPartApplyAddSure(request,loginUser);
	}

	@SuppressWarnings("unchecked")
	public Map<String, Object> findOldPartApplyData(RequestWrapper request) {
		String id = DaoFactory.getParam(request, "id");
		StringBuffer sb= new StringBuffer();
		sb.append("select t.*,\n" );
		sb.append("       c.name as user_name,\n" );
		sb.append("       tm.dealer_code,\n" );
		sb.append("       tm.dealer_shortname\n" );
		sb.append("  from tt_AS_old_return_apply t, tc_user c, tm_dealer tm left join tt_as_wr_returned_order r on r.dealer_id=tm.dealer_id \n" );
		sb.append(" where 1 = 1\n" );
		sb.append("   and t.apply_id = c.user_id\n" );
		sb.append("   and tm.dealer_id = c.dealer_id\n");
		sb.append("   and t.status = '93451001' ");
		sb.append("    and return_type = '10731002' ");
		DaoFactory.getsql(sb, "t.id", id, 1);
		return this.pageQueryMap(sb.toString(), null, getFunName());
	}

	public int oldPartApplyAudit(RequestWrapper request) {
		return super.oldPartApplyAudit(request);
	}

	public boolean checkDateOldReturn(RequestWrapper request, AclUserBean loginUser) {
		return super.checkDateOldReturn(request, loginUser);
	}

	public PageResult<Map<String, Object>> oldPartApplyList1(
			RequestWrapper request, Integer pageSize, Integer currPage) {
		return super.oldPartApplyList1(request,pageSize,currPage);
	}

	public Long insertreturnapply(RequestWrapper request) {
		   Long pkid=	super.insertreturnapply(request);
			return pkid;
		}

	public int oldParReport(RequestWrapper request) {
		int res =-1;
	    int size = super.finddatabyreturnnoAndid(request);
		if (size<1) {
			 super.updatepart_old_returned(request);//修改主表是否延期
			 res = super.oldParReport(request);	
		}
       return res ;
	}

	public Map<String, Object> finddatabyreturnno(RequestWrapper request) {
		return super.finddatabyreturnno(request);
	}

	public PageResult<Map<String, Object>> oldPartApplyAudit(
			RequestWrapper request, Integer pageSize, Integer currPage) {
		return super.oldPartApplyAuditList(request, pageSize,  currPage);
	}

	public int updateOldPartApplybyID(RequestWrapper request) {
         return  super.updateOldPartApplybyID(request);		
	}

	public int updateOldPartApplybyID1(RequestWrapper request) {
		String type = DaoFactory.getParam(request, "type");
		int res = 0;
		if ("Refuse".equals(type)) {
		   res =	super.updatepart_old_returned1(request);
		            super.updateOldPartApplybyID1(request);//退回
		}else {
		   res = super.updateOldPartApplybyID1(request);
		}
		return  res;
	}

	public int updateOldreturnbyID(RequestWrapper request) {
		return super.updateOldreturnbyID(request);
	}

	public int updateOldreturnapplybyID(RequestWrapper request) {
		return super.updateOldreturnapplybyID(request);
	}

	@SuppressWarnings("unchecked")
	public int expotDataoldpartapply(PageResult<Map<String, Object>> list) {
		try {
			String[] head = new String[] {"ID" ,"经销商代码","经销商简称", "经销商ID","清单号", "开始时间", "结束时间",
					"建单时间","申请时间","审核时间", "申请人","审核人", "状态" };
			List<Map<String, Object>> records = list.getRecords();
			List params = new ArrayList();
			if (records != null && records.size() > 0) {
				for (Map<String, Object> map : records) {
					String[] detail = new String[13];
					detail[0] = BaseUtils.checkNull(map.get("ID"));
					detail[1] = BaseUtils.checkNull(map.get("DEALER_CODE"));
					detail[2] = BaseUtils.checkNull(map.get("DEALER_SHORTNAME"));
					detail[3] = BaseUtils.checkNull(map.get("DEALER_ID"));
					detail[4] = BaseUtils.checkNull(map.get("RETURN_NO"));
					detail[5] = BaseUtils.checkNull(map.get("START_DATE"));
					detail[6] = BaseUtils.checkNull(map.get("END_DATE"));
					detail[7] = BaseUtils.checkNull(map.get("CREATE_DATE"));
					detail[8] = BaseUtils.checkNull(map.get("APPLY_DATE"));
					detail[9] = BaseUtils.checkNull(map.get("AUDIT_DATE"));
					detail[10] = BaseUtils.checkNull(map.get("USER_NAME"));
					detail[11] = BaseUtils.checkNull(map.get("AUDIT_MAN1"));
					detail[12] = BaseUtils.checkNull(map.get("CODE_DESC"));
					params.add(detail);
				}
			}
			String systemDateStr = BaseUtils.getSystemDateStr();
			BaseUtils.toExcel(ActionContext.getContext().getResponse(), ActionContext.getContext().getRequest(), head, params, "延期申请审核"+systemDateStr+".xls", "导出数据", null);
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		
		
		return 0;
	}

	public int updateOldPartApply_reson_byID(RequestWrapper request) {
		return super.updateOldPartApplyresonbyID(request);
	}

	public PageResult<Map<String, Object>> oldPartApplyAudit1(
			RequestWrapper request, Integer pageSize, Integer currPage) {
		return super.oldPartApplyAuditList1(request,pageSize,currPage);
	}

	public int oldPartcheck(RequestWrapper request) {
		return super.oldPartcheck(request);
	}

	public void updatereturnedstatus(RequestWrapper request) {
		super.updatepart_old_returned(request);
	}

	public int updateOldreturnstatus(RequestWrapper request) {
		return super.updateOldreturnstatus(request);
	}

	public int updatereturnedstatusbyreturnno(Long pkid) {
		return super.updatereturnedstatusbyreturnno(pkid);
	}

	public PageResult<Map<String, Object>> QueryEmergencyTracking(RequestWrapper request, AclUserBean loginUser, Integer pageSize,Integer currPage) {
		return super.QueryEmergencyTracking(request,loginUser,pageSize,currPage);
	}

	public int addsure(RequestWrapper request, AclUserBean loginUser) {
		return super.addsure(request,loginUser);
	}

	@SuppressWarnings("unchecked")
	public void EmergencyTrackExport(RequestWrapper request,AclUserBean loginUser, ActionContext act, Integer pageSizeMax,Integer currPage) {
		PageResult<Map<String, Object>> list = super.QueryEmergencyTracking(request,loginUser,pageSizeMax,currPage);
		 try {
			    String[] head={"大区","经销商代码","经销商简称","借件人","索赔单号","配件代码","配件名称","下发时间","延期天数","备注"};
				List<Map<String, Object>> records = list.getRecords();
				List params=new ArrayList();
				String date2;
				Long date=new Date().getTime();
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
				if(records!=null &&records.size()>0){
					for (Map<String, Object> map : records) {
						Integer t=0;
						String[] detail=new String[10];
						detail[0]=BaseUtils.checkNull(map.get("ROOT_ORG_NAME"));
						detail[1]=BaseUtils.checkNull(map.get("DEALER_CODE"));
						detail[2]=BaseUtils.checkNull(map.get("DEALER_SHORTNAME"));
						detail[3]=BaseUtils.checkNull(map.get("BORROW_PERSON"));
						detail[4]=BaseUtils.checkNull(map.get("CLAIM_NO"));
						detail[5]=BaseUtils.checkNull(map.get("PART_CODE"));
						detail[6]=BaseUtils.checkNull(map.get("PART_NAME"));
						if (map.get("NEXT_TIME")!=null && ""!=map.get("NEXT_TIME")) {
							date2 =	dateFormat.format(map.get("NEXT_TIME"));
							Long time1 = dateFormat.parse(date2).getTime();
							Long time =  (date -time1)/(24*60*60*1000);
							 t = Integer.parseInt(time.toString());
						}else {
							date2="无";
						}
						detail[7]=BaseUtils.checkNull(date2);
					    if (t==0) {
					    	detail[8]=BaseUtils.checkNull("--");
						}else {
							detail[8]=BaseUtils.checkNull(t);
						}
						detail[9]=BaseUtils.checkNull(map.get("REMARK"));
						params.add(detail);
					}
				}
				    String systemDateStr = BaseUtils.getSystemDateStr();
					BaseUtils.toExcel(act.getResponse(), act.getRequest(), head, params, "紧急调件导出"+systemDateStr+".xls", "导出数据",null);
				} catch (Exception e) {
					e.printStackTrace();
				}
	
	}

	public List<TtAsPartBorrowPO> queryEmergencyremarkByid(RequestWrapper request, AclUserBean loginUser) {
		return super.queryEmergencyremarkByid( request,  loginUser);
	}

	public ClaimApproveAndStoredReturnInfoBean getApproveAndStoredReturnInfo11(Map<String, String> params) {
		return super.getApproveAndStoredReturnInfo11(params);
	}

	public List<Map<String, Object>> getDeductList() {
		return super.getDeductList();
	}

	public List<Map<String, Object>> queryClaimBackDetailList2(Map<String, String> params) {
		return super.queryClaimBackDetailList2( params);
	}

	public List<Map<String, Object>> queryClaimBackDetailList3(String claimId) {
		return super.queryClaimBackDetailList3( claimId);
	}

	public int oldPartSignAuditIn(RequestWrapper request,AclUserBean loginUser) {
		return super.oldPartSignAuditIn( request, loginUser);
	}

	public List historyoldreturnquery(RequestWrapper request, AclUserBean loginUser) {
		return super.historyoldreturnquery( request, loginUser);
	}

	public PageResult<Map<String, Object>> QualityAuditlistQuery(RequestWrapper request, AclUserBean loginUser, Integer pageSize,Integer currPage) {
		return super.QualityAuditlistQuery( request,  loginUser,  pageSize, currPage);
	}

	public PageResult<Map<String, Object>> returnAmountAuditListNew(RequestWrapper request, AclUserBean loginUser, Integer pageSize,Integer currPage) {
		return super.returnAmountAuditListNew( request,  loginUser,  pageSize, currPage);
	}

	public int returnAmountAuditNew(RequestWrapper request,AclUserBean loginUser) {
		return super.returnAmountAuditNew( request, loginUser);
	}

	public PageResult<TtAsWrBackListQryBean> queryClaimBackList(RequestWrapper request, ActionContext act,AclUserBean loginUser, Integer currPage, Integer pageSize) {
		return super.queryClaimBackList( request,act,loginUser,  currPage,  pageSize);
	}

	public TtAsWrOldPartBackListDetailBean getClaimBackInfo(RequestWrapper request, AclUserBean loginUser) {
		return super.getClaimBackInfo( request,loginUser);
	}

	public List<TtAsWrOldPartDetailListBean> queryClaimBackDetailList(RequestWrapper request, AclUserBean loginUser,TtAsWrOldPartBackListDetailBean detailInfoBean) {
		return super.queryClaimBackDetailList( request,loginUser, detailInfoBean);
	}

	@SuppressWarnings("unchecked")
	public List<TtAsPartBorrowPO> queryTtAsPartBorrow(TtAsPartBorrowPO borrow) {
		return super.select(borrow);
	}

	public List<TtAsWrOldReturnedDetailPO> getOldReturnedDetailPOByReturnId(RequestWrapper request, int i, int j) {
		return super.getOldReturnedDetailPOByReturnId( request,  i,  j);
	}
	public List<TtAsWrOldReturnedDetailPO> getOldReturnedDetailPOByReturnId1(RequestWrapper request, int i, int j) {
		return super.getOldReturnedDetailPOByReturnId1( request,  i,  j);
	}

	public int updateClaimBackOrdMainInfo(RequestWrapper request,AclUserBean loginUser) {
		
		return super.updateClaimBackOrdMainInfo( request, loginUser);
	}

	public TtAsWrOldPartBackListDetailBean getClaimBackInfo(RequestWrapper request, int i, int j) {
		return super.getClaimBackInfo( request,  i,  j);
	}

	public List<TtAsWrOldPartDetailListBean> queryClaimBackDetailList(RequestWrapper request) {
		return super.queryClaimBackDetailList( request);
	}

	public void insertTtAsComRecord(RequestWrapper request,AclUserBean loginUser) {
		super.insertTtAsComRecord( request,loginUser);
	}

	@SuppressWarnings("unchecked")
	public TtAsWrOldReturnedPO queryTtAsWrOldReturnedByid(RequestWrapper request) {
		String claimid = DaoFactory.getParam(request,"ORDER_ID");
		TtAsWrOldReturnedPO po = new TtAsWrOldReturnedPO();
		po.setId(Long.parseLong(claimid));
		return (TtAsWrOldReturnedPO) super.select(po).get(0);
	}

	@SuppressWarnings("unchecked")
	public TrReturnLogisticsPO queryTrReturnLogistics(RequestWrapper request) {
		String claimid = DaoFactory.getParam(request,"ORDER_ID");
		TrReturnLogisticsPO sp = new TrReturnLogisticsPO();
		sp.setLogictisId(Long.parseLong(claimid));
		return (TrReturnLogisticsPO) super.select(sp).get(0);
	}

	@SuppressWarnings("unchecked")
	public TtAsWrReturnedOrderPO queryTtAsWrReturnedOrder(RequestWrapper request, TrReturnLogisticsPO sp) {
		TtAsWrReturnedOrderPO op=new TtAsWrReturnedOrderPO();
		op.setId(sp.getReturnId());
		return (TtAsWrReturnedOrderPO) super.select(op).get(0);
	}

	public List<TmOldpartTransportPO> queryGetTransPList(Long currDealerId) {
		return super.queryGetTransPList( currDealerId);
	}

	public  List<Map<String, Object>> getStr(Long id ){
		return super.getStr( id );
	}

	public List<Map<String, Object>> getStr(List<TmOldpartTransportPO> sList,String returnType) {
		return super.getStr(sList, returnType);
	}

	public int updateTtAsWrOldReturned(RequestWrapper request,AclUserBean loginUser,List<TtAsWrOldReturnedDetailPO> returnDetaiList) {
		return super.updateTtAsWrOldReturned( request, loginUser, returnDetaiList);
	}

	public void insertTtAsComRecord1(RequestWrapper request,AclUserBean loginUser) {
		super.insertTtAsComRecord1( request, loginUser);
	}

	public TtAsWrOldPartBackListDetailBean getClaimBackInfo1(RequestWrapper request, int i, int j) {
		return super.getClaimBackInfo1( request, i,j);
	}

	public List<TtAsWrOldPartDetailListBean> queryClaimBackDetailList1(RequestWrapper request, AclUserBean loginUser,TtAsWrOldPartBackListDetailBean detailInfoBean) {
		return super.queryClaimBackDetailList1( request,  loginUser, detailInfoBean);
	}

	
	

	}
