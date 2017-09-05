package com.infodms.dms.actions.parts.storageManager.partReturnManager;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Arith;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.constants.PTConstants;
import com.infodms.dms.dao.parts.storageManager.partReturnManager.PartOemReturnChkDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TtPartOemReturnDtlPO;
import com.infodms.dms.po.TtPartOemReturnMainPO;
import com.infodms.dms.po.TtPartPoInPO;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;
/**
 *
 * @ClassName     : PartOemReturnApplyManager
 * @Description   : 采购退货申请审核
 * @author        : chenjunjiang
 * CreateDate     : 2013-4-27
 */
@SuppressWarnings("unchecked")
public class PartOemReturnChkManager implements PTConstants {

	public Logger logger = Logger.getLogger(PartOemReturnChkManager.class);
	private PartOemReturnChkDao dao = PartOemReturnChkDao.getInstance();
    ActionContext act = ActionContext.getContext();
    RequestWrapper request = act.getRequest();
    AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);

	/**
	 *
	 * @Title      :
	 * @Description: 采购退货审核初始化,转向申请查询页面
	 * @param      :
	 * @return     :
	 * @throws     :
	 * LastDate    : 2013-4-27
	 */
	public void queryPartOemReturnApplyInit(){
		try {
			act.setOutData("old",CommonUtils.getBefore(new Date()));
            act.setOutData("now",CommonUtils.getDate());
			act.setForword(PART_OEMRETURN_CHECK_QUERY_URL);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"采购退货申请");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}

	/**
	 *
	 * @Title      :
	 * @Description: 查询申请
	 * @param      :
	 * @return     :
	 * @throws     :
	 * LastDate    : 2013-4-28
	 */
	public void queryPartOemReturnApplyInfo(){
		ActionContext act = ActionContext.getContext();
		RequestWrapper request = act.getRequest();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
      try {
			String returnCode = CommonUtils.checkNull(request.getParamValue("RETURN_CODE"));//退货单号
			String startDate = CommonUtils.checkNull(request.getParamValue("startDate"));//制单开始时间
			String endDate = CommonUtils.checkNull(request.getParamValue("endDate"));//制单结束时间

			//分页方法 begin
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1; // 处理当前页
			PageResult<Map<String, Object>> ps = dao.queryPartOemReturnApplyList(returnCode,startDate,endDate,logonUser,curPage,Constant.PAGE_SIZE);
			//分页方法 end
			act.setOutData("ps", ps);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"销售退货申请");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}

	/**
	 *
	 * @Title      :
	 * @Description: 进入审核页面
	 * @param      :
	 * @return     :
	 * @throws     :
	 * LastDate    : 2013-4-29
	 */
	public void queryApplyDetailInit(){
		ActionContext act = ActionContext.getContext();
		RequestWrapper request = act.getRequest();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			String returnId = CommonUtils.checkNull(request.getParamValue("returnId"));

			int flag = 0;
			Map<String, Object> map = dao.getPartOemReturnMainInfo(returnId);//查询采购退货申请主表信息
			//在审核之前判断是否是无入库单退货
			TtPartOemReturnDtlPO dtlPO = new TtPartOemReturnDtlPO();
			dtlPO.setReturnId(CommonUtils.parseLong(returnId));
			List<TtPartOemReturnDtlPO> list = dao.select(dtlPO);
			for(TtPartOemReturnDtlPO paDtlPO:list){
				if(paDtlPO.getInId()==null||paDtlPO.getInId().longValue()==0){//如果没有入库单id,就是无入库单退货
					flag = 1;
					break;
				}
			}
			request.setAttribute("po", map);
			request.setAttribute("flag", flag);
			act.setForword(PART_OEMRETURN_CHECK_URL);
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "配件销售退货申请明细");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 *
	 * @Title      :
	 * @Description: 查询申请明细
	 * @param      :
	 * @return     :
	 * @throws     :
	 * LastDate    : 2013-4-30
	 */
	public void queryPartOemReturnApplyDetail(){
		ActionContext act = ActionContext.getContext();
		RequestWrapper request = act.getRequest();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
        try {
			String returnId = CommonUtils.checkNull(request.getParamValue("returnId"));
//			String soCode = CommonUtils.checkNull(request.getParamValue("soCode"));

			//分页方法 begin
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1; // 处理当前页
			PageResult<Map<String, Object>> ps = dao.queryPartOemReturnApplyDetailList(returnId,curPage,Constant.PAGE_SIZE_MAX);
			//分页方法 end
			act.setOutData("ps", ps);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"采购退货申请明细");
			logger.error(logonUser,e1);
			act.setException(e1);
		}

	}

	/**
	 *
	 * @Title      :
	 * @Description: 审核通过
	 * @param      :
	 * @return     :
	 * @throws     :
	 * LastDate    : 2013-4-30
	 */
	public void agreePartOemReturnApply(){

		ActionContext act = ActionContext.getContext();
		RequestWrapper request = act.getRequest();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			String returnId = CommonUtils.checkNull(request.getParamValue("returnId"));//退货单id

			String[] inIds = request.getParamValues("inId");
//			String[] partIds = request.getParamValues("partId");
			TtPartOemReturnMainPO ttPartOemReturnMainPO = new TtPartOemReturnMainPO();
			ttPartOemReturnMainPO.setReturnId(CommonUtils.parseLong(returnId));
			ttPartOemReturnMainPO = (TtPartOemReturnMainPO) dao.select(ttPartOemReturnMainPO).get(0);
			
			if(ttPartOemReturnMainPO.getState().intValue()==Constant.PART_OEM_RETURN_STATUS_01.intValue()){//只有状态为审核中的退货单才能审核
				TtPartOemReturnDtlPO dtlPO = new TtPartOemReturnDtlPO();
				dtlPO.setReturnId(CommonUtils.parseLong(returnId));
				List<TtPartOemReturnDtlPO> list = dao.select(dtlPO);

				double amount = 0;//退货金额
//				if(inIds!=null){
					
					for(int i=0;i<inIds.length;i++){
						TtPartOemReturnDtlPO po = list.get(i);

		                TtPartOemReturnDtlPO po1 = new TtPartOemReturnDtlPO();
		                po1.setDtlId(po.getDtlId());

		                TtPartOemReturnDtlPO po2 =  new TtPartOemReturnDtlPO();
						long checkQty = CommonUtils.parseLong(request.getParamValue("CHECK_QTY"+inIds[i]));
		                //po2.setApplyQty(checkQty);//审核退货数量
						po2.setCheckQty(checkQty);//审核退货数量
						//po2.setReturnQty(checkQty);//已退货数量
						//po2.setOutQty(checkQty);//退货出库数量
						po2.setBuyAmount(Arith.round(Arith.mul(checkQty, po.getBuyPrice()),2));//退货行金额
						amount+=po2.getBuyAmount();

		                //更新入库单上面的已申请退货数量
//		                TtPartPoInPO inPO = new TtPartPoInPO();
//		                inPO.setInId(po.getInId());
//		                inPO = (TtPartPoInPO)dao.select(inPO).get(0);
//
//		                TtPartPoInPO inPO1 = new TtPartPoInPO();
//		                TtPartPoInPO inPO2 = new TtPartPoInPO();
//		                inPO1.setInId(po.getInId());
//		                inPO2.setApplyQty(inPO.getReturnQty()+checkQty);

		                dao.update(po1, po2);
//		                dao.update(inPO1,inPO2);

						}
					
//				}
				
				//如果是无入库单退货
//				if(partIds!=null){
//					
//					for(int i=0;i<partIds.length;i++){
//						TtPartOemReturnDtlPO po = list.get(i);
//
//		                TtPartOemReturnDtlPO po1 = new TtPartOemReturnDtlPO();
//		                po1.setDtlId(Long.valueOf(partIds[i]));
//
//		                TtPartOemReturnDtlPO po2 =  new TtPartOemReturnDtlPO();
//						long checkQty = CommonUtils.parseLong(request.getParamValue("CHECK_QTY"+partIds[i]));
//						po2.setCheckQty(checkQty);//审核退货数量
//						po2.setBuyAmount(Arith.round(Arith.mul(checkQty, po.getBuyPrice()),2));//退货行金额
//						amount+=po2.getBuyAmount();
//						
//		                dao.update(po1, po2);
//					}
//					
//				}
				
				TtPartOemReturnMainPO mainPO = new TtPartOemReturnMainPO();

				TtPartOemReturnMainPO spo = new TtPartOemReturnMainPO();
				spo.setReturnId(CommonUtils.parseLong(returnId));
				mainPO.setVerifyDate(new Date());//审核日期
				mainPO.setVerifyBy(logonUser.getUserId());//审核人
				mainPO.setAmount(amount);//退货金额
				mainPO.setState(Constant.PART_OEM_RETURN_STATUS_03);//状态更新为出库中
	            //库存释放和占用逻辑，此审核页面审核数量可以修改，需要把先前已经占用的资源释放掉，然后根据审核通过的结果再重新占用资源
	            //调用库存释放逻辑
	            List<Object> ins = new LinkedList<Object>();
	            ins.add(0, CommonUtils.parseLong(returnId));
	            ins.add(1, Constant.PART_CODE_RELATION_18);
	            ins.add(2,0);// 1:占用 0：释放占用
	            dao.callProcedure("PKG_PART.P_UPDATEPARTSTATE", ins, null);

	            dao.update(spo, mainPO);

	            //调用库存占用逻辑
	            List<Object> ins2 = new LinkedList<Object>();
	            ins2.add(0, CommonUtils.parseLong(returnId));
	            ins2.add(1, Constant.PART_CODE_RELATION_25);
	            ins2.add(2,1);// 1:占用 0：释放占用
	            dao.callProcedure("PKG_PART.P_UPDATEPARTSTATE", ins2, null);

				act.setOutData("success", "审核通过!");
			}else{
				act.setOutData("error", "该退货单已经审核,请选择其他退货单审核!");
			}
			
			
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.UPDATE_FAILURE_CODE, "采购退货申请审核");
			logger.error(logonUser, e1);
			act.setException(e1);
		}

	}

	/**
	 *
	 * @Title      :
	 * @Description: 驳回申请
	 * @param      :
	 * @return     :
	 * @throws     :
	 * LastDate    : 2013-4-30
	 */
	public void rejectPartOemReturnApply(){
		ActionContext act = ActionContext.getContext();
		RequestWrapper request = act.getRequest();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
        try {

			String returnId = CommonUtils.checkNull(request.getParamValue("returnId"));
			String rejectReason = CommonUtils.checkNull(request.getParamValue("rejectReason"));

			TtPartOemReturnMainPO ttPartOemReturnMainPO = new TtPartOemReturnMainPO();
			ttPartOemReturnMainPO.setReturnId(CommonUtils.parseLong(returnId));
			ttPartOemReturnMainPO = (TtPartOemReturnMainPO) dao.select(ttPartOemReturnMainPO).get(0);
			if(ttPartOemReturnMainPO.getState().intValue()==Constant.PART_OEM_RETURN_STATUS_01.intValue()){//只有状态为审核中的退货单才能驳回
				TtPartOemReturnMainPO spo = new TtPartOemReturnMainPO();
				TtPartOemReturnMainPO po = new 	TtPartOemReturnMainPO();
				spo.setReturnId(CommonUtils.parseLong(returnId));
				po.setState(Constant.PART_OEM_RETURN_STATUS_02);//更新状态为驳回
				po.setRemark1(rejectReason);

	            //更新入库单上的申请退货数量
	            TtPartOemReturnDtlPO rPO = new TtPartOemReturnDtlPO();
	            rPO.setReturnId(CommonUtils.parseLong(returnId));
	             List<TtPartOemReturnDtlPO> lrPO = dao.select(rPO);
	            for(int i =0 ;i < lrPO.size() ;i++){
	                rPO = lrPO.get(i);
	                if(rPO.getInId()==null||rPO.getInId().longValue()==0){
	                	break;
	                }else{
	                	TtPartPoInPO inPO = new TtPartPoInPO();
		                inPO.setInId(rPO.getInId());
		                inPO = (TtPartPoInPO)dao.select(inPO).get(0);

		                TtPartPoInPO inPO1 = new TtPartPoInPO();
		                inPO1.setInId(rPO.getInId());
		                TtPartPoInPO inPO2 = new TtPartPoInPO();
		                inPO2.setApplyQty(inPO.getApplyQty()- rPO.getApplyQty());

		                dao.update(inPO1,inPO2);
	                }
	            }

				dao.update(spo, po);
	            //调用库存释放逻辑
	            List<Object> ins = new LinkedList<Object>();
	            ins.add(0, CommonUtils.parseLong(returnId));
	            ins.add(1, Constant.PART_CODE_RELATION_18);
	            ins.add(2,0);// 1:占用 0：释放占用
	            dao.callProcedure("PKG_PART.P_UPDATEPARTSTATE", ins, null);
				act.setOutData("success", "驳回成功!");
			}else{
				act.setOutData("error", "该退货单已经驳回,请选择其他退货单审核!");
			}
			
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.UPDATE_FAILURE_CODE,"采购退货申请");
			logger.error(logonUser,e1);
			act.setException(e1);
		}

	}

}
