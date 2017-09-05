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
import com.infodms.dms.dao.parts.storageManager.partReturnManager.PartDlrReturnChkrDao;
import com.infodms.dms.dao.parts.storageManager.partReturnManager.PartDlrReturnOutDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TtPartDlrReturnDtlPO;
import com.infodms.dms.po.TtPartDlrReturnMainPO;
import com.infodms.dms.po.TtPartLoactionDefinePO;
import com.infodms.dms.po.TtPartRecordPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;
/**
 * 
 * @ClassName     : PartDlrReturnOutManager 
 * @Description   : 销售退货出库 
 * @author        : chenjunjiang
 * CreateDate     : 2013-4-26
 */
public class PartDlrReturnOutManager implements PTConstants {

	public Logger logger = Logger.getLogger(PartDlrReturnOutManager.class);
	private PartDlrReturnOutDao dao = PartDlrReturnOutDao.getInstance();
	/**
	 * 
	 * @Title      : 
	 * @Description: 销售退货出库查询初始化，转到查询页面 
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-25
	 */
	public void queryPartReturnApplyInit(){
		
		ActionContext act = ActionContext.getContext();
		RequestWrapper request = act.getRequest();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			act.setForword(PART_RETURN_OUT_QUERY_URL);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"销售退货申请");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}

	/**
	 * 
	 * @Title      : 
	 * @Description: 查询销售退货信息
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-25
	 */
	public void queryPartDlrReturnApplyInfo(){
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
			PageResult<Map<String, Object>> ps = dao.queryPartDlrReturnApplyList(returnCode,startDate,endDate,logonUser,curPage,Constant.PAGE_SIZE);
			//分页方法 end
			act.setOutData("ps", ps);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"销售退货信息");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 
	 * @Title      : 
	 * @Description: 销售退货审核信息查询初始化 
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-26
	 */
	public void queryPartDlrReturnChkInit(){
		ActionContext act = ActionContext.getContext();
		RequestWrapper request = act.getRequest();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			String returnId = CommonUtils.checkNull(request.getParamValue("returnId"));
			Map map = dao.getPartDlrReturnMainInfo(returnId);
			List list = dao.getPartWareHouseList(logonUser);//获取当前机构的库房信息
			act.setOutData("wareHouses", list);
			request.setAttribute("po", map);
			act.setForword(PART_RETURN_OUT_URL);
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "配件销售退货主信息");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	
	}
	
	/**
	 * 
	 * @Title      : 
	 * @Description: 查询销售退货审核信息 
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-26
	 */
	public void queryPartDlrReturnChkInfo(){

		ActionContext act = ActionContext.getContext();
		RequestWrapper request = act.getRequest();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
        try {
			
			String returnId = CommonUtils.checkNull(request.getParamValue("returnId"));
			String soCode = CommonUtils.checkNull(request.getParamValue("soCode"));
			
			//分页方法 begin
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1; // 处理当前页
			PageResult<Map<String, Object>> ps = dao.queryPartDlrReturnChkList(returnId,soCode,curPage,Constant.PAGE_SIZE);
			//分页方法 end
			act.setOutData("ps", ps);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"销售退货审核");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	
	}
	
	/**
	 * 
	 * @Title      : 
	 * @Description: 销售退货出库 
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-26
	 */
	public void outPartDlrReturn(){
		ActionContext act = ActionContext.getContext();
		RequestWrapper request = act.getRequest();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
        try {
			String returnId = CommonUtils.checkNull(request.getParamValue("returnId"));
			
			TtPartDlrReturnMainPO mainPO = new TtPartDlrReturnMainPO();
			mainPO.setReturnId(CommonUtils.parseLong(returnId));
			mainPO = (TtPartDlrReturnMainPO) dao.select(mainPO).get(0);
			
			if(mainPO.getState().intValue()==Constant.PART_DLR_RETURN_STATUS_04.intValue()){//只有当退货单状态还是出库中的时候才允许出库
				    TtPartDlrReturnMainPO sPartDlrReturnMainPO = new TtPartDlrReturnMainPO();
				    TtPartDlrReturnMainPO partDlrReturnMainPO = new TtPartDlrReturnMainPO();
				    
				    sPartDlrReturnMainPO.setReturnId(CommonUtils.parseLong(returnId));
				    partDlrReturnMainPO.setReturnDate(new Date());//退货时间
				    partDlrReturnMainPO.setState(Constant.PART_DLR_RETURN_STATUS_05);//更新状态为入库中
				    dao.update(sPartDlrReturnMainPO, partDlrReturnMainPO);
					
				    TtPartDlrReturnDtlPO dtlPO = new TtPartDlrReturnDtlPO();
				    dtlPO.setReturnId(mainPO.getReturnId());
				    List<TtPartDlrReturnDtlPO> list = dao.select(dtlPO);
				    
				    for(TtPartDlrReturnDtlPO po:list){
				    	
				    	TtPartDlrReturnDtlPO sdtDtlPO = new TtPartDlrReturnDtlPO();
				    	sdtDtlPO.setDtlId(po.getDtlId());
				    	TtPartDlrReturnDtlPO dtlPO2 = new TtPartDlrReturnDtlPO();
				    	dtlPO2.setOutQty(po.getCheckQty());//设置出库数量
				    	dtlPO2.setReturnQty(po.getCheckQty());//设置以退货数量
				    	
				    	//插入信息到出入库记录表 
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
		                ttPartRecordPO.setPartBatch("1306");//////////////////配件批次
		                ttPartRecordPO.setVenderId(21799l);///////////////////配件供应商
		                ttPartRecordPO.setConfigId(Long.valueOf(Constant.PART_CODE_RELATION_29));//出库单
		                ttPartRecordPO.setOrderId(CommonUtils.parseLong(returnId));//出库单ID
		                ttPartRecordPO.setOrderCode(mainPO.getReturnCode());//出库单编码
		                //ttPartRecordPO.setLineId();
		                List<OrgBean> orgBeanList = PartWareHouseDao.getInstance().getOrgInfo(logonUser);
		                ttPartRecordPO.setOrgId(orgBeanList.get(0).getOrgId());
		                ttPartRecordPO.setOrgCode(orgBeanList.get(0).getOrgCode());
		                ttPartRecordPO.setOrgName(orgBeanList.get(0).getOrgName());

		                if(mainPO.getStockOut()!=null&&!mainPO.getStockOut().equals(0l)){//如果在退货的时候选择了仓库才设置货位信息
		                	ttPartRecordPO.setWhId(mainPO.getStockOut());
		                	
		                	TtPartLoactionDefinePO loactionDefinePO = new TtPartLoactionDefinePO();
			                loactionDefinePO.setWhId(mainPO.getStockOut());
			                loactionDefinePO.setPartId(po.getPartId());
			                loactionDefinePO.setOrgId(orgBeanList.get(0).getOrgId());
			                loactionDefinePO.setState(Constant.STATUS_ENABLE);
			                loactionDefinePO.setStatus(1);
			                loactionDefinePO = (TtPartLoactionDefinePO)dao.select(loactionDefinePO).get(0);

			                ttPartRecordPO.setLocId(loactionDefinePO.getLocId());
			                ttPartRecordPO.setLocCode(loactionDefinePO.getLocCode());
		                }
		                ttPartRecordPO.setOptDate(new Date());
		                ttPartRecordPO.setCreateDate(new Date());
		                ttPartRecordPO.setPersonId(logonUser.getUserId());
		                ttPartRecordPO.setPersonName(logonUser.getName());
		                ttPartRecordPO.setPartState(1);

		                dao.update(sdtDtlPO, dtlPO2);
		                dao.insert(ttPartRecordPO);
				    	
				    }
				    
				    if(mainPO.getStockOut()!=null&&!mainPO.getStockOut().equals(0l)){//有出库库房的时候才调用出库逻辑
				    	//调用出库逻辑
			            List ins = new LinkedList<Object>();
			            ins.add(0,CommonUtils.parseLong(returnId));
			            ins.add(1,Constant.PART_CODE_RELATION_29);
			            ins.add(2,1);//0表示先前未占用(默认),1表示先前已占用
			            dao.callProcedure("PKG_PART.P_UPDATEPARTSTOCK",ins,null);
				    }
				    
				    act.setOutData("success", "出库成功!");
				    
			}else{
				act.setOutData("error", "该退货单已经出库,请选择其他退货单出库!");
				return;
			}
			
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ADD_FAILURE_CODE,"销售退货出库");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
}
