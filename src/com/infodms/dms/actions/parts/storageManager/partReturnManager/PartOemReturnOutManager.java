package com.infodms.dms.actions.parts.storageManager.partReturnManager;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.bean.OrgBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.constants.PTConstants;
import com.infodms.dms.dao.parts.baseManager.partsBaseManager.PartWareHouseDao;
import com.infodms.dms.dao.parts.purchaseOrderManager.PurchaseOrderInDao;
import com.infodms.dms.dao.parts.storageManager.partReturnManager.PartOemReturnOutDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TtPartLoactionDefinePO;
import com.infodms.dms.po.TtPartOemReturnDtlPO;
import com.infodms.dms.po.TtPartOemReturnMainPO;
import com.infodms.dms.po.TtPartPoInPO;
import com.infodms.dms.po.TtPartRecordPO;
import com.infodms.dms.po.TtPartReturnRecordPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;
/**
 * 
 * @ClassName     : PartDlrReturnOutManager 
 * @Description   : 采购退货出库 
 * @author        : chenjunjiang
 * CreateDate     : 2013-4-26
 */
@SuppressWarnings("unchecked")
public class PartOemReturnOutManager implements PTConstants {

	public Logger logger = Logger.getLogger(PartOemReturnOutManager.class);
	private PartOemReturnOutDao dao = PartOemReturnOutDao.getInstance();
	private PurchaseOrderInDao orderInDao = PurchaseOrderInDao.getInstance();
    private ActionContext act = ActionContext.getContext();
    private AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
	/**
	 * 
	 * @Title      : 
	 * @Description: 采购退货出库查询初始化，转到查询页面 
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-25
	 */
	public void queryPartReturnInit(){
		
		try {
			act.setOutData("old",CommonUtils.getBefore(new Date()));
            act.setOutData("now",CommonUtils.getDate());
			act.setForword(PART_OEMRETURN_OUT_QUERY_URL);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"采购退货申请");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}

	/**
	 * 
	 * @Title      : 
	 * @Description: 查询采购退货信息
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-25
	 */
	public void queryPartOemReturnInfo(){
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
			PageResult<Map<String, Object>> ps = dao.queryPartOemReturnList(returnCode,startDate,endDate,logonUser,curPage,Constant.PAGE_SIZE);
			//分页方法 end
			act.setOutData("ps", ps);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"采购退货信息");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 
	 * @Title      : 
	 * @Description: 采购退货审核信息查询初始化 
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-26
	 */
    public void queryPartOemReturnChkInit(){
		ActionContext act = ActionContext.getContext();
		RequestWrapper request = act.getRequest();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			String returnId = CommonUtils.checkNull(request.getParamValue("returnId"));
			Map<String, Object> map = dao.getPartOemReturnMainInfo(returnId);
			int flag = 0;
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
			act.setForword(PART_OEMRETURN_OUT_URL);
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "配件采购退货主信息");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	
	}
	
	/**
	 * 
	 * @Title      : 
	 * @Description: 查询采购退货审核信息 
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-26
	 */
	public void queryPartOemReturnChkInfo(){

		ActionContext act = ActionContext.getContext();
		RequestWrapper request = act.getRequest();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
        try {
			
			String returnId = CommonUtils.checkNull(request.getParamValue("returnId"));
			//分页方法 begin
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1; // 处理当前页
			PageResult<Map<String, Object>> ps = dao.queryPartOemReturnChkList(returnId,curPage,Constant.PAGE_SIZE);
			//分页方法 end
			act.setOutData("ps", ps);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"采购退货审核");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	
	}
	
	/**
	 * 
	 * @Title      : 
	 * @Description: 查询采购退货审核信息 (无入库单)
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-26
	 */
	public void queryPartOemReturnChkInfo1(){
		
		ActionContext act = ActionContext.getContext();
		RequestWrapper request = act.getRequest();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			
			String returnId = CommonUtils.checkNull(request.getParamValue("returnId"));
			//分页方法 begin
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1; // 处理当前页
					PageResult<Map<String, Object>> ps = dao.queryPartOemReturnChkList1(returnId,curPage,Constant.PAGE_SIZE);
					//分页方法 end
					act.setOutData("ps", ps);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"采购退货审核");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
		
	}
	
	/**
	 * 
	 * @Title      : 
	 * @Description: 采购退货出库 
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-26
	 */
	public void outPartOemReturn(){
		ActionContext act = ActionContext.getContext();
		RequestWrapper request = act.getRequest();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		
        try {
			String returnId = CommonUtils.checkNull(request.getParamValue("returnId"));
			String[] dtlIds = request.getParamValues("ck");
			String errors = "";
			String dtlError = "";
			boolean flag = false;
			
			TtPartOemReturnMainPO ttPartOemReturnMainPO = new TtPartOemReturnMainPO();
			ttPartOemReturnMainPO.setReturnId(CommonUtils.parseLong(returnId));
			ttPartOemReturnMainPO = (TtPartOemReturnMainPO) dao.select(ttPartOemReturnMainPO).get(0);
			
			if(ttPartOemReturnMainPO.getState().intValue()==Constant.PART_OEM_RETURN_STATUS_03.intValue()){//只有状态为出库中才可以出库
				
			    //出库之后需要更新入库单中的退货数量并向退货记录表中插入退货信息,还要向出入库记录表中插入数据
	            for(int i=0;i<dtlIds.length;i++){
	            	
	            	TtPartOemReturnDtlPO po = new TtPartOemReturnDtlPO();
	            	po.setDtlId(CommonUtils.parseLong(dtlIds[i]));
	            	po = (TtPartOemReturnDtlPO) dao.select(po).get(0);
	            	
	            	// 验证配件是否已结算
	            	TtPartPoInPO poInPO = new TtPartPoInPO();
	            	if(po.getInId()!=null&&po.getInId().intValue()!=0){
		            	poInPO.setInId(po.getInId());
		            	poInPO.setState(Constant.PART_PURCHASE_ORDERBALANCE_STATUS_01);
		            	List<TtPartPoInPO> poInPOList = dao.select(poInPO);
		            	if(poInPOList.size()==0){
		            		errors="该退货单中的配件已经结算,不能出库!";
		            		break;
		            	}else{
		            	    poInPO = poInPOList.get(0);
		            	}
	            	}
	            	
	            	//如果已经出库完成就不能再次出库
	            	if(po.getStatus().intValue()==2){
	            		if(po.getInCode()!=null&&!"".equals(po.getInCode())){
	            			dtlError+="入库单【"+po.getInCode()+"】中的配件【"+po.getPartCname()+"】已经出库,不能再次出库!\n";
	            		}else{
	            			dtlError+="配件【"+po.getPartCname()+"】已经出库,不能再次出库!\n";
	            		}
	            	}else{
	            		
	            		boolean isLocked = orderInDao.isLocked(po.getPartId(),po.getStockOut().toString(), po.getLocId().toString(),
	            		        logonUser.getDealerId()==null?logonUser.getOrgId():Long.valueOf(logonUser.getDealerId()));//当前仓库中的配件是否被锁定
		                
	            		//如果该配件在库存中已经被锁定
		                if(isLocked){
		                	if(po.getInCode()!=null&&!"".equals(po.getInCode())){
		                		dtlError+="入库单【"+po.getInCode()+"】中的配件【"+po.getPartCname()+"】已经被锁定,不能出库!\n";
		            		}else{
		            			dtlError+="配件【"+po.getPartCname()+"】已经被锁定,不能出库!\n";
		            		}
		                }else{
		                	flag = true;
		                	//更新采购退货单退货数量
			                TtPartOemReturnDtlPO returnDtlPO = new TtPartOemReturnDtlPO();
			                returnDtlPO.setDtlId(po.getDtlId());

			                TtPartOemReturnDtlPO returnDtlPO1 = new TtPartOemReturnDtlPO();;
			                returnDtlPO1.setReturnQty(po.getCheckQty());
			                returnDtlPO1.setStatus(2);//更新状态为出库完成

			                if(po.getInId()!=null&&po.getInId().longValue()!=0){
			                	 //更新入库单上已退货数量
				                TtPartPoInPO inPO = new TtPartPoInPO();
				                inPO.setInId(po.getInId());
				                inPO = (TtPartPoInPO) dao.select(inPO).get(0);

				                TtPartPoInPO sInPO = new TtPartPoInPO();
								sInPO.setInId(po.getInId());

				                TtPartPoInPO inPO1 = new TtPartPoInPO();
				                inPO1.setReturnQty(inPO.getReturnQty() + po.getCheckQty());
				                dao.update(sInPO, inPO1);
			                }

			                dao.update(returnDtlPO,returnDtlPO1);

			                //插入退货记录表
			                Long outId = po.getReturnId();//退货单id
			                TtPartReturnRecordPO ttPartReturnRecordPO = new TtPartReturnRecordPO();
							ttPartReturnRecordPO.setDtlId(CommonUtils.parseLong(SequenceManager.getSequence("")));
							ttPartReturnRecordPO.setReturnId(outId);
							ttPartReturnRecordPO.setReturnCode(ttPartOemReturnMainPO.getReturnCode());
							ttPartReturnRecordPO.setReturnType(Constant.PART_RETURN_TYPE_02);//退货类型
							ttPartReturnRecordPO.setSourceId(po.getDtlId());
							ttPartReturnRecordPO.setSourceCaode(ttPartOemReturnMainPO.getReturnCode());
							ttPartReturnRecordPO.setPartId(po.getPartId());
							ttPartReturnRecordPO.setPartCode(po.getPartCode());
							ttPartReturnRecordPO.setPartOldcode(po.getPartOldcode());
							ttPartReturnRecordPO.setPartCname(po.getPartCname());
							ttPartReturnRecordPO.setUnit(po.getUnit());
							ttPartReturnRecordPO.setReturnQty(po.getCheckQty());
							ttPartReturnRecordPO.setRemark(po.getRemark());
							ttPartReturnRecordPO.setCreateDate(new Date());
							ttPartReturnRecordPO.setCreateBy(po.getCreateBy());
							ttPartReturnRecordPO.setStockOut(po.getStockOut());
							ttPartReturnRecordPO.setStockIn(po.getStockIn());
							ttPartReturnRecordPO.setLocId(po.getLocId());
							ttPartReturnRecordPO.setItemQty(po.getItemQty());

			                //插入出入库记录表 add by yuan 20130513
			                TtPartRecordPO ttPartRecordPO  = new TtPartRecordPO();
			                ttPartRecordPO.setRecordId(CommonUtils.parseLong(SequenceManager.getSequence("")));
			                ttPartRecordPO.setAddFlag(2);//出库标记
			                ttPartRecordPO.setState(1);//正常出库
			                ttPartRecordPO.setPartNum(po.getCheckQty());//出库数量
			                ttPartRecordPO.setTranstypeId(0l);//默认0
			                ttPartRecordPO.setPartId(po.getPartId());//配件ID
			                ttPartRecordPO.setPartCode(po.getPartCode());//配件件号
			                ttPartRecordPO.setPartOldcode(po.getPartOldcode());//配件编码
			                ttPartRecordPO.setPartName(po.getPartCname());//配件名称
			                ttPartRecordPO.setPartBatch(poInPO.getBatchNo());//////////////////配件批次
			                ttPartRecordPO.setVenderId(po.getVenderId());///////////////////配件供应商
			                ttPartRecordPO.setConfigId(Long.valueOf(Constant.PART_CODE_RELATION_09));//出库单
			                ttPartRecordPO.setOrderId(CommonUtils.parseLong(returnId));//出库单ID
			                ttPartRecordPO.setOrderCode(ttPartOemReturnMainPO.getReturnCode());//出库单编码
			                //ttPartRecordPO.setLineId();
			                List<OrgBean> orgBeanList = PartWareHouseDao.getInstance().getOrgInfo(logonUser);
			                ttPartRecordPO.setOrgId(orgBeanList.get(0).getOrgId());
			                ttPartRecordPO.setOrgCode(orgBeanList.get(0).getOrgCode());
			                ttPartRecordPO.setOrgName(orgBeanList.get(0).getOrgName());
			                ttPartRecordPO.setWhId(po.getStockOut());
			                ttPartRecordPO.setWhName(poInPO.getWhName());

//			                TtPartLoactionDefinePO loactionDefinePO = new TtPartLoactionDefinePO();
//			                loactionDefinePO.setWhId(po.getStockOut());
//			                loactionDefinePO.setPartId(po.getPartId());
//			                loactionDefinePO.setOrgId(orgBeanList.get(0).getOrgId());
//			                loactionDefinePO.setState(Constant.STATUS_ENABLE);
//			                loactionDefinePO.setStatus(1);
//                          loactionDefinePO.setLocId(po.getLocId());
//			                loactionDefinePO = (TtPartLoactionDefinePO)dao.select(loactionDefinePO).get(0);
//			                ttPartRecordPO.setLocId(loactionDefinePO.getLocId());
//			                ttPartRecordPO.setLocCode(loactionDefinePO.getLocCode());
			                
			                ttPartRecordPO.setLocId(po.getLocId());
			                ttPartRecordPO.setLocCode(po.getLocCode());
			                ttPartRecordPO.setOptDate(new Date());
			                ttPartRecordPO.setCreateDate(new Date());
			                ttPartRecordPO.setPersonId(logonUser.getUserId());
			                ttPartRecordPO.setPersonName(logonUser.getName());
			                ttPartRecordPO.setPartState(1);

			                dao.insert(ttPartReturnRecordPO);
			                dao.insert(ttPartRecordPO);
		                	
		                }
	            		
	            	}

				}
	            
	            if(flag){
	            	//调用出库逻辑
		            List<Object> ins = new LinkedList<Object>();
		            ins.add(0,CommonUtils.parseLong(returnId));
		            ins.add(1,Constant.PART_CODE_RELATION_09);
		            ins.add(2,1);//0表示先前未占用(默认),1表示先前已占用
		            dao.callProcedure("PKG_PART.P_UPDATEPARTSTOCK",ins,null);

		            boolean fStatus = dao.isAllOut(returnId);
		            
		            if(fStatus){//如果已经全部出库
		            	TtPartOemReturnMainPO sPartOemReturnMainPO = new TtPartOemReturnMainPO();
						TtPartOemReturnMainPO partOemReturnMainPO = new TtPartOemReturnMainPO();
					    sPartOemReturnMainPO.setReturnId(CommonUtils.parseLong(returnId));
					    
					    partOemReturnMainPO.setReturnDate(new Date());//退货时间
					    partOemReturnMainPO.setState(Constant.PART_OEM_RETURN_STATUS_04);//更新状态为退货完成
					    dao.update(sPartOemReturnMainPO, partOemReturnMainPO);
		            }
	            }
	            
			}else{
				errors = "该退货单中的配件已经全部出库,请选择其他退货单出库!";
			}
			
			if("".equals(dtlError)&&"".equals(errors)){
				act.setOutData("success", "出库成功!");
			}
			act.setOutData("dtlError", dtlError);
			act.setOutData("errors", errors);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.SPECIAL_MEG,"采购退货出库失败,请联系管理员!");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
}
