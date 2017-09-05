package com.infodms.dms.actions.parts.partAllotManager;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.constants.PTConstants;
import com.infodms.dms.dao.parts.partAllotManager.PartTransferDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TtPartDlrOrderMainPO;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;
/**
 * 
 * @ClassName     : PartTransferChkManager 
 * @Description   : 配件调拨审核 
 * @author        : chenjunjiang
 * CreateDate     : 2013-5-12
 */
public class PartTransferChkManager implements PTConstants {

	public Logger logger = Logger.getLogger(PartTransferChkManager.class);
	private PartTransferDao dao = PartTransferDao.getInstance();
	/**
	 * 
	 * @Title      : 
	 * @Description: 配件调拨单审核查询初始化  
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-5-12
	 */
	public void queryPartTransferChkInit(){
		ActionContext act = ActionContext.getContext();
		RequestWrapper request = act.getRequest();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
		   boolean flag = true;
    	   if(logonUser.getDealerId()!=null){//如果不是车厂,就不能审核,只能查看
				flag = true;
			}
    	   act.setOutData("flag", flag);
		   act.setForword(PART_TRANSFERCHK_QUERY_URL);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"配件调拨单");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 
	 * @Title      : 
	 * @Description: 配件调拨单审核查询 
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-5-12
	 */
	public void queryPartTransferChkInfo(){
		ActionContext act = ActionContext.getContext();
		RequestWrapper request = act.getRequest();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
      try {
    	   boolean flag = true;
    	   if(logonUser.getDealerId()!=null){//如果不是车厂,就不能审核,只能查看
				flag = false;
			}
			
    	   String orderCode = CommonUtils.checkNull(request.getParamValue("ORDER_CODE"));//订单号
			String dealerName = CommonUtils.checkNull(request.getParamValue("DEALER_NAME"));//调入单位
			String sellerName = CommonUtils.checkNull(request.getParamValue("SELLER_NAME"));//调出单位
			String startDate = CommonUtils.checkNull(request.getParamValue("startDate"));//制单开始时间
			String endDate = CommonUtils.checkNull(request.getParamValue("endDate"));//制单结束时间
			String startDate1 = CommonUtils.checkNull(request.getParamValue("startDate1"));//提交开始时间
			String endDate1 = CommonUtils.checkNull(request.getParamValue("endDate1"));//提交结束时间
			
			TtPartDlrOrderMainPO po = new TtPartDlrOrderMainPO();
			po.setOrderCode(orderCode);
			po.setDealerName(dealerName);
			po.setSellerName(sellerName);
			
			//分页方法 begin
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1; // 处理当前页
			PageResult<Map<String, Object>> ps = dao.queryPartTransferChkList(po,startDate,endDate,startDate1,endDate1,logonUser,curPage,Constant.PAGE_SIZE);
			//分页方法 end
			act.setOutData("ps", ps);
			act.setOutData("flag", flag);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"配件调拨单");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 
	 * @Title      : 
	 * @Description: 调拨单审核初始化 
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-5-12
	 */
	public void partTransferChkInit(){
		ActionContext act = ActionContext.getContext();
		RequestWrapper request = act.getRequest();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			String orderId = CommonUtils.checkNull(request.getParamValue("orderId"));
			Map map = dao.getPartDlrOrderMainInfo(orderId);
			List list1 = CommonUtils.getPartUnitList(Constant.FIXCODE_TYPE_04);// 获取配件发运方式
			act.setOutData("transList",list1);
			request.setAttribute("po", map);
			act.setForword(PART_TRANSFER_CHK_URL);
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "配件调拨审核");
			logger.error(logonUser, e1);
			act.setException(e1);
		}

	}
	
	/**
	 * 
	 * @Title      : 
	 * @Description: 同意 
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-5-12
	 */
	public void agreePartTransfer(){
		ActionContext act = ActionContext.getContext();
		RequestWrapper request = act.getRequest();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			String orderId = CommonUtils.checkNull(request.getParamValue("orderId"));
			String auditOption = CommonUtils.checkNull(request.getParamValue("AUDIT_OPINION"));//审核意见
			String errors = "";//错误信息
			String success = "";//成功信息
			
			TtPartDlrOrderMainPO mainPO = new TtPartDlrOrderMainPO();
			mainPO.setOrderId(CommonUtils.parseLong(orderId));
			mainPO = (TtPartDlrOrderMainPO) dao.select(mainPO).get(0);
			
			//如果该调拨单已经被审核(通过或驳回),再次审核就要提示错误信息
			if(mainPO.getState().equals(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE_06)||mainPO.getState().equals(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE_05)){
				errors+="该调拨单已经被审核,请选择其他调拨单审核!";
			}else{
				TtPartDlrOrderMainPO spo = new TtPartDlrOrderMainPO();
				spo.setOrderId(CommonUtils.parseLong(orderId));
				TtPartDlrOrderMainPO po = new TtPartDlrOrderMainPO();
				po.setAuditDate(new Date());
				po.setAuditBy(logonUser.getUserId());
				po.setState(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE_06);
				po.setAuditOpinion(auditOption);
				dao.update(spo, po);
				success = "审核通过!";
			}
			
			act.setOutData("error", errors);
			act.setOutData("success", success);
			
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.SPECIAL_MEG, "配件调拨审核失败,请联系管理员!");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 
	 * @Title      : 
	 * @Description: 驳回
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-5-12
	 */
	public void rejectPartTransfer(){
		ActionContext act = ActionContext.getContext();
		RequestWrapper request = act.getRequest();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			String orderId = CommonUtils.checkNull(request.getParamValue("orderId"));
			String auditOption = CommonUtils.checkNull(request.getParamValue("AUDIT_OPINION"));//审核意见
			String errors = "";//错误信息
			String success = "";//成功信息
			
			TtPartDlrOrderMainPO mainPO = new TtPartDlrOrderMainPO();
			mainPO.setOrderId(CommonUtils.parseLong(orderId));
			mainPO = (TtPartDlrOrderMainPO) dao.select(mainPO).get(0);
			
			//如果该调拨单已经被审核,再次审核就要提示错误信息
			if(mainPO.getState().equals(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE_06)||mainPO.getState().equals(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE_05)){
				errors+="该调拨单已经被审核,请选择其他调拨单审核!";
			}else{
				TtPartDlrOrderMainPO spo = new TtPartDlrOrderMainPO();
				spo.setOrderId(CommonUtils.parseLong(orderId));
				TtPartDlrOrderMainPO po = new TtPartDlrOrderMainPO();
				po.setAuditDate(new Date());
				po.setAuditBy(logonUser.getUserId());
				po.setState(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE_05);
				po.setAuditOpinion(auditOption);
				dao.update(spo, po);
				
				//驳回成功之后需要释放掉先前占用的库存
				//调用库存释放逻辑
	            /*List ins = new LinkedList<Object>();
	            ins.add(0, mainPO.getOrderId());
	            ins.add(1, Constant.PART_CODE_RELATION_15);
	            ins.add(2,0);// 1:占用 0：释放占用
	            dao.callProcedure("PKG_PART.P_UPDATEPARTSTATE", ins, null);*/
				success = "驳回成功!";
			}
			
			act.setOutData("error", errors);
			act.setOutData("success", success);
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.SPECIAL_MEG, "配件调拨审核失败,请联系管理员!");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
}
