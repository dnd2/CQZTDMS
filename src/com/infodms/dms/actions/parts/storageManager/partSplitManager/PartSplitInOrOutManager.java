package com.infodms.dms.actions.parts.storageManager.partSplitManager;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.constants.PTConstants;
import com.infodms.dms.dao.claim.laborList.LaborListSummaryReportDao;
import com.infodms.dms.dao.parts.purchaseOrderManager.PurchaseOrderInDao;
import com.infodms.dms.dao.parts.salesManager.PartSoManageDao;
import com.infodms.dms.dao.parts.storageManager.partSplitManager.PartSplitInOrOutDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TtPartSpcpMainPO;
import com.infodms.dms.po.TtPartSplitCompoundDtlPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;
/**
 * 
 * @ClassName     : PartSplitOutManager 
 * @Description   : 配件拆合件出库 
 * @author        : chenjunjiang
 * CreateDate     : 2013-5-6
 */
@SuppressWarnings("unchecked")
public class PartSplitInOrOutManager implements PTConstants {
	public Logger logger = Logger.getLogger(PartSplitInOrOutManager.class);
	private PartSplitInOrOutDao dao = PartSplitInOrOutDao.getInstance();
	private PurchaseOrderInDao inDao = PurchaseOrderInDao.getInstance();
	/**
	 * 
	 * @Title      : 
	 * @Description: 拆合申请查询初始化 
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-5-6
	 */
	public void queryPartSplitApplyInit(){
		ActionContext act = ActionContext.getContext();
		RequestWrapper request = act.getRequest();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			act.setForword(PART_SPLIT_INOROUT_URL);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"配件拆合件出库");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 
	 * @Title      : 
	 * @Description: 查询拆合申请 
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-5-6
	 */
	public void queryPartSpiltApplyInfo(){
		ActionContext act = ActionContext.getContext();
		RequestWrapper request = act.getRequest();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
      try {
			String spcpdCode = CommonUtils.checkNull(request.getParamValue("SPCPD_CODE"));//拆合单号
			String startDate = CommonUtils.checkNull(request.getParamValue("startDate"));//制单开始时间
			String endDate = CommonUtils.checkNull(request.getParamValue("endDate"));//制单结束时间
			//分页方法 begin
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1; // 处理当前页
			PageResult<Map<String, Object>> ps = dao.queryPartSpiltApplyList(spcpdCode,startDate,endDate,logonUser,curPage,Constant.PAGE_SIZE);
			//分页方法 end
			act.setOutData("ps", ps);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"配件拆合申请");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	
	}
	
	/**
	 * 
	 * @Title      : 
	 * @Description: 总成件出库或入库,同时需要把其关联的分总成件入库 或出库
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-5-6
	 */
	public void outOrInPartMain(){
		ActionContext act = ActionContext.getContext();
		RequestWrapper request = act.getRequest();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			String spcpdId = CommonUtils.checkNull(request.getParamValue("spcpdId"));
			String flagStr = CommonUtils.checkNull(request.getParamValue("flag"));//出入库标志
			int flag = 0;
			if(!"".equals(flagStr)){
				flag = CommonUtils.parseInteger(flagStr);
			}
			
			TtPartSpcpMainPO mainPO = new TtPartSpcpMainPO();
			mainPO.setSpcpdId(CommonUtils.parseLong(spcpdId));
			mainPO = (TtPartSpcpMainPO) dao.select(mainPO).get(0);
			
			boolean isLocked = inDao.isLocked(mainPO.getPartId(), mainPO.getLocId().toString(), mainPO.getWhId().toString(), logonUser.getDealerId() == null ? logonUser.getOrgId() : Long.valueOf(logonUser.getDealerId()));//当前仓库中的配件是否被锁定
			String error = "";
			
            if (isLocked) {//如果该配件在库存中已经被锁定
            	error = "拆合单【" + mainPO.getSpcpdCode() + "】中的配件【" + mainPO.getPartName() + "】已经被锁定,不能出入库!";
            	act.setOutData("error", error);
            	return;
            }
			
            TtPartSplitCompoundDtlPO dtlPO = new TtPartSplitCompoundDtlPO();
            dtlPO.setSpcpdId(CommonUtils.parseLong(spcpdId));
            List<TtPartSplitCompoundDtlPO> dtlList = dao.select(dtlPO);
            for(TtPartSplitCompoundDtlPO sdtDtlPO:dtlList){
            	isLocked = inDao.isLocked(dtlPO.getPartId(), dtlPO.getLocId().toString(), mainPO.getWhId().toString(), logonUser.getDealerId() == null ? logonUser.getOrgId() : Long.valueOf(logonUser.getDealerId()));//当前仓库中的配件是否被锁定
            	if (isLocked) {//如果该配件在库存中已经被锁定
                	error = "拆合单【" + mainPO.getSpcpdCode() + "】中的配件【" + mainPO.getPartName() + "】已经被锁定,不能出入库!";
                	act.setOutData("error", error);
                	return;
                }
            }
            
			TtPartSpcpMainPO spo = new TtPartSpcpMainPO();
			TtPartSpcpMainPO po = new TtPartSpcpMainPO();
			spo.setSpcpdId(CommonUtils.parseLong(spcpdId));
			po.setState(Constant.PART_SPCPD_STATUS_04);//更新状态为已完成
			
			long iostock_id = CommonUtils.parseLong(SequenceManager.getSequence(""));//出入库id
			//向配件拆合出入库表和出入库明细表中插入数据,同时向出入库记录表中插入数据
			dao.insertPartInfo(spcpdId,logonUser,iostock_id,flag);
			dao.update(spo, po);
			
			if("1".equals(flagStr)){//如果是总成件入库,那么分总成件就是出库
				//总成件调用入库逻辑
				List ins1 = new LinkedList<Object>();
				ins1.add(0,CommonUtils.parseLong(spcpdId));
				ins1.add(1,Constant.PART_CODE_RELATION_04);
				dao.callProcedure("PKG_PART.P_UPDATEPARTSTOCK",ins1,null);
				
				//分总成件调用出库逻辑
				List ins = new LinkedList<Object>();
				ins.add(0,CommonUtils.parseLong(spcpdId));
				ins.add(1,Constant.PART_CODE_RELATION_09);
				ins.add(2,1);//0表示先前未占用(默认),1表示先前已占用
				dao.callProcedure("PKG_PART.P_UPDATEPARTSTOCK",ins,null);
			}
			
			if("2".equals(flagStr)){//如果是总成件出库,那么分总成件就是入库
				//总成件调用出库逻辑
	            List ins = new LinkedList<Object>();
	            ins.add(0,CommonUtils.parseLong(spcpdId));
	            ins.add(1,Constant.PART_CODE_RELATION_09);
	            ins.add(2,1);//0表示先前未占用(默认),1表示先前已占用
	            dao.callProcedure("PKG_PART.P_UPDATEPARTSTOCK",ins,null);
	            
	            //分总成件调用入库逻辑
	            List ins1 = new LinkedList<Object>();
                ins1.add(0,CommonUtils.parseLong(spcpdId));
                ins1.add(1,Constant.PART_CODE_RELATION_04);
                dao.callProcedure("PKG_PART.P_UPDATEPARTSTOCK",ins1,null);
			}
			
			act.setOutData("success", "1".equals(flagStr)?"入库成功!":"出库成功!");
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,
					ErrorCodeConstant.SPECIAL_MEG, "配件拆合件出入库失败,请联系管理员!");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	
	}
	
	/**
	 * 
	 * @Title      : 
	 * @Description: 配件拆分出入库明细查询初始化
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-5-6
	 */
	public void querySpiltInOrOutDetailInit(){
		ActionContext act = ActionContext.getContext();
		RequestWrapper request = act.getRequest();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			String spcpdId = CommonUtils.checkNull(request.getParamValue("spcpdId"));
			Map map = dao.getPartSpcpIoStockMainInfo(spcpdId);
			request.setAttribute("po", map);
			act.setForword(PART_SPLIT_INOROUT_DETAIL_URL);
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
	 * @Description: 查询配件拆合出入库明细 
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-5-7
	 */
	public void querySpiltInOrOutDetail(){
		ActionContext act = ActionContext.getContext();
		RequestWrapper request = act.getRequest();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
      try {
			String ioStockId = CommonUtils.checkNull(request.getParamValue("ioStockId"));//拆合出入库id
		    
			//分页方法 begin
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1; // 处理当前页
			PageResult<Map<String, Object>> ps = dao.querySpiltInOrOutDetailList(ioStockId,curPage,Constant.PAGE_SIZE);
			//分页方法 end
			act.setOutData("ps", ps);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"分总成件信息");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	
	}
	
	/**
	 * 
	 * @Title      : 
	 * @Description: 打印 
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-7-1
	 */
	public void opPrintHtml(){
		ActionContext act = ActionContext.getContext();
		AclUserBean loginUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		LaborListSummaryReportDao laborListSummaryReportDao = LaborListSummaryReportDao.getInstance();
		Map<String,Object> dataMap = new HashMap();
	try{
		
		String spcpdId = CommonUtils.checkNull(request.getParamValue("spcpdId"));
		Map map = dao.getPartSpcpIoStockMainInfo(spcpdId);
		request.setAttribute("po", map);
		
		String ioStockId = ((BigDecimal) map.get("IOSTOCK_ID")).toString();//拆合出入库id
	    
		List<Map<String,Object>> detailList = dao.querySpiltInOrOutDetail(ioStockId);
		
		dataMap.put("mainMap", map);
		dataMap.put("detailList", detailList);
		act.setOutData("dataMap", dataMap);
		act.setForword(PART_SPLIT_INOROUT_DETAILPRINT_URL);
	}catch(Exception e) {//异常方法
		BizException e1 = new BizException(act,e,ErrorCodeConstant.SPECIAL_MEG,"配件拆合件出入库清单打印错误,请联系管理员!");
		logger.error(loginUser,e1);
		act.setException(e1);
	}
}
}
