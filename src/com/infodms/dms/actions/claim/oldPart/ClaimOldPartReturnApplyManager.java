package com.infodms.dms.actions.claim.oldPart;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.common.getCompanyId.GetOemcompanyId;
import com.infodms.dms.dao.claim.oldPart.ClaimoldPartReturnApplyDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TmDealerPO;
import com.infodms.dms.po.TmOldpartReturnApplyDetailPO;
import com.infodms.dms.po.TmOldpartReturnApplyPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;
/**
 * 类说明：索赔旧件管理--回运清单维护
 * 作者：  赵伦达
 */
@SuppressWarnings("unchecked")
public class ClaimOldPartReturnApplyManager{
	public Logger logger = Logger.getLogger(ClaimBackPieceBackListOrdManager.class);
	String returnApplyIndexUrl = "/jsp/claim/oldPart/oldPartReturnApplyIndex.jsp";
	String returnApplyAddUrl = "/jsp/claim/oldPart/oldPartReturnApplyAdd.jsp";
	String returnOldpartListUrl = "/jsp/claim/oldPart/showOldpartReturnList.jsp";
	String returnApplyDetailUrl = "/jsp/claim/oldPart/oldPartReturnApplyDetail.jsp";
	String returnApplyModifyUrl = "/jsp/claim/oldPart/oldPartReturnApplyModify.jsp";
	String returnApplyTranUrl = "/jsp/claim/oldPart/oldpartReturnApplyTran.jsp";
	private ActionContext act = ActionContext.getContext();
	private AclUserBean logonUserBean = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
	private RequestWrapper request=act.getRequest();
	private ClaimoldPartReturnApplyDao dao=ClaimoldPartReturnApplyDao.getInstance();
	public AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
	public Long companyId=GetOemcompanyId.getOemCompanyId(logonUserBean);
	
	
	public void queryOldpartReturnApply(){
		try {
			act=ActionContext.getContext();
			request = act.getRequest();
			logonUserBean=(AclUserBean) act.getSession().get(Constant.LOGON_USER);
			companyId=GetOemcompanyId.getOemCompanyId(logonUserBean);
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			String COMMAND = request.getParamValue("COMMAND");
			if(COMMAND!=null){
				
				String dealerCode = logonUser.getDealerCode();
				
				//查询条件
			    String transportNo = request.getParamValue("RETURN_APPLY_NO");//运输公司名称
			    String transportStatus = request.getParamValue("STATUS");//运输公司状态
			    
			    PageResult<Map<String, Object>> ps = null;
				Integer curPage = getCurrPage();


				ps = dao.queryOldPartReturnApply(request,dealerCode,transportNo,transportStatus, curPage, 15);
				request.setAttribute("DEALER_CODE", dealerCode);
				act.setOutData("ps", ps);
				act.setForword(returnApplyIndexUrl);
	
			}else{
				request.setAttribute("DEALER_CODE", logonUser.getDealerCode());
				act.setForword(returnApplyIndexUrl);
			}
			
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, "旧件运输单查询");
			logger.error(logonUserBean, e1);
			act.setException(e1);
		}
	}
	public void oldparReturnApplyAdd() {
		act = ActionContext.getContext();
		String dealerId = logonUser.getDealerId();
		RequestWrapper request = act.getRequest();
		Long userId = logonUser.getUserId();
		try {
			
			String COMMAND = request.getParamValue("COMMAND");
			if(COMMAND==null){//新增
				
				String flag = request.getParamValue("flag");
				String[] return_no = request.getParamValues("RETURN_NO");
				String[] part_code = request.getParamValues("PART_CODE");
				String[] part_name = request.getParamValues("PART_NAME");
				String[] claim_no = request.getParamValues("CLAIM_NO");
				String[] barcode_no = request.getParamValues("BARCODE_NO");
				String[] deductRemark = request.getParamValues("DEDUCT_REMARK_CODE");
				String[] returnDetailId = request.getParamValues("RETURN_DETAIL_ID");
				
				TmOldpartReturnApplyPO ret = new TmOldpartReturnApplyPO();
				Long returnApplyId = Utility.getLong(SequenceManager.getSequence(""));
				ret.setCreateBy(userId);
				ret.setCreateDate(new Date());
				ret.setDealerId(Long.valueOf(dealerId));
				if("1".equals(flag)){//保存
					
					ret.setStatus(Constant.SP_JJ_RETURN_APPLY_STATUS_01);
				}else{//提交
					
					ret.setStatus(Constant.SP_JJ_RETURN_APPLY_STATUS_02);
					ret.setReportDate(new Date());
					ret.setReportUser(userId);
				}
				
				ret.setReturnApplyId(returnApplyId);
				ret.setReturnApplyNo(SequenceManager.getSequence("AO"));
				
				dao.insert(ret);
				
			
				for(int i =0;i<return_no.length;i++){
					
					TmOldpartReturnApplyDetailPO tor = new TmOldpartReturnApplyDetailPO();
					
					tor.setBarcodeNo(barcode_no[i]);
					tor.setClaimNo(claim_no[i]);
					tor.setDeductRemark(Integer.valueOf(deductRemark[i]));
					tor.setDetailId(Utility.getLong(SequenceManager.getSequence("")));
					//tor.setIsAgree(Constant.SP_JJ_RETURN_APPLY_ISAGREE_02);//默认为待审
					tor.setPartCode(part_code[i]);
					tor.setPartName(part_name[i]);
					tor.setReturnApplyId(returnApplyId);
					tor.setReturnNo(return_no[i]);
					tor.setCreateBy(userId);
					tor.setCreateDate(new Date());
					tor.setReturnDetailId(Long.valueOf(returnDetailId[i]));
					
					dao.insert(tor);
				}
				act.setOutData("returnValue", 1);
			}else{//跳转
				String dealrId = logonUser.getDealerId();
				TmDealerPO td = new TmDealerPO();
				td.setDealerId(Long.valueOf(dealerId));
				td = (TmDealerPO) dao.select(td).get(0);
				String dealrName = td.getDealerShortname();
				String dealerCode = td.getDealerCode();
				request.setAttribute("DEALER_NAME", dealrName);
				request.setAttribute("DEALER_CODE", dealerCode);
				
				act.setForword(returnApplyAddUrl);
			}
			
			
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, "旧件运输单添加");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	public void selectOldPartReturnListForward() {
		ActionContext act = ActionContext.getContext();
		try {
			String COMMAND = request.getParamValue("COMMAND");
			if(COMMAND!=null){
			
				String returnNo = request.getParamValue("RETURN_NO");
				String partCode = request.getParamValue("PART_CODE");
				String dealerCode = logonUser.getDealerCode();
				
				String returnDetailId = request.getParamValue("RETURN_DETAIL_ID");
				String[] rdi = returnDetailId==null?null:returnDetailId.split(",");
				PageResult<Map<String, Object>> ps = null;
				Integer curPage = getCurrPage();
				// 查询语句


				ps = dao.queryOldPartReturnList(request, dealerCode, returnNo, partCode,rdi, curPage, 15);
				act.setOutData("ps", ps);
				act.setForword(returnOldpartListUrl);
			}else{
				
				act.setForword(returnOldpartListUrl);
			}
			
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔单维护");
			logger.error(logonUser, e1);
			act.setException(e1);
		}

	}
	/**
	 * 获取当前页
	 * @return
	 */
	private Integer getCurrPage() {
		// 处理当前页
		Integer curPage = request.getParamValue("curPage") != null ? Integer
				.parseInt(request.getParamValue("curPage")) : 1;
		return curPage;
	}
	/**
	 * 明细页面+审核页面
	 */
	public void oemReturnApplyCheck(){
		try {
			
			String COMMAND = request.getParamValue("COMMAND");
			String returnApplyId = request.getParamValue("RETURN_APPLY_ID");
			
			if(COMMAND!=null){
				String[] isAgree = request.getParamValues("IS_AGREE1");
				String[] remark = request.getParamValues("REMARK");
				String[] returnNos = request.getParamValues("returnNo");
				String[] bools = request.getParamValues("bool");
				
				TmOldpartReturnApplyPO tora = new TmOldpartReturnApplyPO();
				tora.setReturnApplyId(Long.valueOf(returnApplyId));
				
				TmOldpartReturnApplyPO toraVal = new TmOldpartReturnApplyPO();
				toraVal.setCheckDate(new Date());
				toraVal.setCheckUser(logonUser.getUserId());
				toraVal.setStatus(Constant.SP_JJ_RETURN_APPLY_STATUS_03);//已审核
				toraVal.setUpdateBy(logonUser.getUserId());
				toraVal.setUpdateDate(new Date());
				dao.update(tora, toraVal);
				/**
				 * 审批
				 */
				for (int i=0;i<returnNos.length;i++) {
					String remarkTemp=CommonUtils.checkNull(remark[i].equals("无")?"":remark[i]);
					String isAgreeTemp=CommonUtils.checkNull(isAgree[i]);
					String bool=CommonUtils.checkNull(bools[i]);
					
					TmOldpartReturnApplyDetailPO torad1 = new TmOldpartReturnApplyDetailPO();
					torad1.setReturnApplyId(Long.valueOf(CommonUtils.checkNull(returnApplyId)));
					torad1.setDetailId(Long.valueOf(isAgreeTemp));
					
					TmOldpartReturnApplyDetailPO toradVal1 = new TmOldpartReturnApplyDetailPO();
					if("1".equals(bool)){
						toradVal1.setIsAgree(Constant.SP_JJ_RETURN_APPLY_ISAGREE_01);
					}else{
						toradVal1.setIsAgree(Constant.SP_JJ_RETURN_APPLY_ISAGREE_02);
					}
					toradVal1.setRemark(remarkTemp);
					toradVal1.setUpdateBy(logonUser.getUserId());
					toradVal1.setUpdateDate(new Date());
					dao.update(torad1, toradVal1);
				}
				act.setOutData("returnValue", 1);
				act.setForword(returnApplyIndexUrl);
			}else{
				String viewOrCheck = request.getParamValue("VIEW_OR_CHECK");
				
				TmOldpartReturnApplyDetailPO torad = new TmOldpartReturnApplyDetailPO();
				torad.setReturnApplyId(Long.valueOf(returnApplyId));
				List<TmOldpartReturnApplyDetailPO> listBean = dao.select(torad);
				
				act.setOutData("listBean", listBean);
				
				Map<String,Object> hashMap = dao.queryOldPartReturnApplyById(request, returnApplyId);
				
				request.setAttribute("DEALER_NAME", hashMap.get("DEALER_SHORTNAME"));
				request.setAttribute("DEALER_CODE", hashMap.get("DEALER_CODE"));
				request.setAttribute("VIEW_OR_CHECK", viewOrCheck);
				request.setAttribute("IS_OEM", logonUser.getDealerCode());
				act.setForword(returnApplyDetailUrl);
			}
			
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, "旧件回运申请查询审核失败");
			logger.error(logonUserBean, e1);
			act.setException(e1);
		}
	}
	public void modifyReturnApplyDetail(){
		try {
			
			String COMMAND = request.getParamValue("COMMAND");
			String dealerCode = logonUser.getDealerCode();
			
			if(COMMAND!=null){
				
				Long userId = logonUser.getUserId();
				
				String returnApplyId = request.getParamValue("RETURN_APPLY_ID");
				String flag = request.getParamValue("flag");
				String[] return_no = request.getParamValues("RETURN_NO");
				String[] part_code = request.getParamValues("PART_CODE");
				String[] part_name = request.getParamValues("PART_NAME");
				String[] claim_no = request.getParamValues("CLAIM_NO");
				String[] barcode_no = request.getParamValues("BARCODE_NO");
				String[] deduct_remark_code = request.getParamValues("DEDUCT_REMARK_CODE");
				String[] return_detail_id = request.getParamValues("RETURN_DETAIL_ID");
				if(return_no!=null&&return_no.length>0){
					
					TmOldpartReturnApplyPO tor = new TmOldpartReturnApplyPO();
					tor.setReturnApplyId(Long.valueOf(returnApplyId));
					TmOldpartReturnApplyPO torVal = new TmOldpartReturnApplyPO();
					if("1".equals(flag)){//保存
						
						torVal.setStatus(Constant.SP_JJ_RETURN_APPLY_STATUS_01);
						torVal.setUpdateBy(userId);
						torVal.setUpdateDate(new Date());
						
					}else{//提交
						for(int j =0;j<return_no.length;j++){
							
							int num = dao.queryOldPartReturnApplyDetailByDetailId(request, return_detail_id[j], logonUser.getDealerId());
							if(num>0){
								act.setOutData("returnValue", "配件 "+part_code[j]+" 已申请！");
								act.setForword(returnApplyModifyUrl);
								return;
							}
						}
						torVal.setStatus(Constant.SP_JJ_RETURN_APPLY_STATUS_02);
						torVal.setReportDate(new Date());
						torVal.setReportUser(userId);
						torVal.setUpdateBy(userId);
						torVal.setUpdateDate(new Date());
					}
					
					dao.update(tor, torVal);
				
				
					//删除以前的明细
					dao.delOldPartReturnApplysport(request, dealerCode, returnApplyId);
					for(int i =0;i<return_no.length;i++){
						
						TmOldpartReturnApplyDetailPO tordVal = new TmOldpartReturnApplyDetailPO();
						tordVal.setBarcodeNo(barcode_no[i]);
						tordVal.setClaimNo(claim_no[i]);
						tordVal.setDeductRemark(Integer.valueOf(deduct_remark_code[i]));
						tordVal.setPartCode(part_code[i]);
						tordVal.setPartName(part_name[i]);
						tordVal.setReturnNo(return_no[i]);
						tordVal.setUpdateBy(userId);
						tordVal.setUpdateDate(new Date());
						tordVal.setReturnDetailId(Long.valueOf(return_detail_id[i]));
						Long detailId = Utility.getLong(SequenceManager.getSequence(""));
						
						tordVal.setReturnApplyId(Long.valueOf(returnApplyId));
						tordVal.setDetailId(detailId);
						tordVal.setCreateBy(userId);
						tordVal.setCreateDate(new Date());
						
						dao.insert(tordVal);
						
						/*if(detail_id[i]!=null){//存在更新
							TmOldpartReturnApplyDetailPO todr = new TmOldpartReturnApplyDetailPO();
							todr.setDetailId(Long.valueOf(detail_id[i]));
							
							dao.update(todr, tordVal);
						}else{//不存在新增
							Long detailId = Utility.getLong(SequenceManager.getSequence(""));
							newDetailList.add(detailId);
							
							tordVal.setReturnApplyId(Long.valueOf(returnApplyId));
							tordVal.setDetailId(detailId);
							tordVal.setCreateBy(userId);
							tordVal.setCreateDate(new Date());
							
							dao.insert(tordVal);
						}*/
						
					}
				}
				
			    act.setOutData("returnValue", 1);
			    request.setAttribute("DEALER_CODE", dealerCode);
			    act.setForword(returnApplyIndexUrl);
			}else{
				String returnApplyId = request.getParamValue("RETURN_APPLY_ID");
				/*TmOldpartReturnApplyDetailPO tor = new TmOldpartReturnApplyDetailPO();
				tor.setReturnApplyId(Long.valueOf(returnApplyId));
				List<TmOldpartReturnApplyDetailPO> listBean = dao.select(tor);*/
				
				List<Map<String, Object>> listBean = dao.queryOldPartReturnApplyDetailById(request, returnApplyId);
				
				act.setOutData("listBean", listBean);
				request.setAttribute("DEALER_CODE", dealerCode);
				request.setAttribute("RETURN_APPLY_ID", returnApplyId);
				act.setForword(returnApplyModifyUrl);
			}
			
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, "旧件运输单查询");
			logger.error(logonUserBean, e1);
			act.setException(e1);
		}
	}
	public void oldpartReturnApplyTran(){
		try {
			
			String COMMAND = request.getParamValue("COMMAND");
			String returnApplyId = request.getParamValue("RETURN_APPLY_ID");
			
			if(COMMAND!=null){
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				
				String sendNo = request.getParamValue("SEND_NO");
				String sendDate = request.getParamValue("SEND_DATE");
				String sendLinkUser = request.getParamValue("SEND_LINK_USER");
				String sendLinkPhone = request.getParamValue("SEND_LINK_PHONE");
				
				
				TmOldpartReturnApplyPO tora = new TmOldpartReturnApplyPO();
				tora.setReturnApplyId(Long.valueOf(returnApplyId));
				
				TmOldpartReturnApplyPO toraVal = new TmOldpartReturnApplyPO();
				toraVal.setSendUser(logonUser.getUserId());
				if(sendDate!=null){
					toraVal.setSendDate(sdf.parse(sendDate));
				}
				toraVal.setSendNo(sendNo);
				toraVal.setSendLinkPhone(sendLinkPhone);
				toraVal.setSendLinkUser(sendLinkUser);
				toraVal.setUpdateBy(logonUser.getUserId());
				toraVal.setUpdateDate(new Date());
				toraVal.setStatus(Constant.SP_JJ_RETURN_APPLY_STATUS_04);//已补录
				
				dao.update(tora, toraVal);
				
				act.setForword(returnApplyIndexUrl);
				
			}else{
				request.setAttribute("RETURN_APPLY_ID", returnApplyId);
				act.setForword(returnApplyTranUrl);
			}
			
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, "旧件回运申请查询审核失败");
			logger.error(logonUserBean, e1);
			act.setException(e1);
		}
	}
	public void changeReturnApplyStatus(){
		try {
			
			String returnApplyId = request.getParamValue("RETURN_APPLY_ID");
			String status = request.getParamValue("STATUS");
			
			TmOldpartReturnApplyPO tor = new TmOldpartReturnApplyPO();
			tor.setReturnApplyId(Long.valueOf(returnApplyId));
			//验证是否有提报过的配件
			TmOldpartReturnApplyDetailPO orad = new TmOldpartReturnApplyDetailPO();
			orad.setReturnApplyId(Long.valueOf(returnApplyId));
			List<TmOldpartReturnApplyDetailPO> listBean = dao.select(orad);
			for(int j =0;j<listBean.size();j++){
				
				int num = dao.queryOldPartReturnApplyDetailByDetailId(request, listBean.get(j).getReturnDetailId().toString(), logonUser.getDealerId());
				if(num>0){
					request.setAttribute("returnValue", "配件 "+listBean.get(j).getPartCode()+" 已申请！");
					act.setForword(returnApplyIndexUrl);
					request.setAttribute("DEALER_CODE", logonUser.getDealerCode());
					return;
				}
			}
			TmOldpartReturnApplyPO torVal = new TmOldpartReturnApplyPO();
			torVal.setUpdateBy(logonUser.getUserId());
			torVal.setUpdateDate(new Date());
			torVal.setReportDate(new Date());
			torVal.setReportUser(logonUser.getUserId());
			torVal.setStatus(Constant.SP_JJ_RETURN_APPLY_STATUS_02);
			
			dao.update(tor, torVal);
			
		    request.setAttribute("DEALER_CODE", logonUser.getDealerCode());
		    act.setForword(returnApplyIndexUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, "旧件运输单查询");
			logger.error(logonUserBean, e1);
			act.setException(e1);
		}
	}
}
	

 	