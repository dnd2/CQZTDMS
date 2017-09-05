package com.infodms.dms.actions.parts.storageManager.partSplitManager;

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
import com.infodms.dms.dao.parts.storageManager.partSplitManager.PartSpiltApplyDao;
import com.infodms.dms.dao.parts.storageManager.partSplitManager.PartSpiltChkDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TmOrgPO;
import com.infodms.dms.po.TtPartOemReturnDtlPO;
import com.infodms.dms.po.TtPartOemReturnMainPO;
import com.infodms.dms.po.TtPartSpcpMainPO;
import com.infodms.dms.po.TtPartSplitCompoundDtlPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.OrderCodeManager;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;
/**
 * 
 * @ClassName     : PartSpiltChkManager 
 * @Description   : 配件拆合件审核
 * @author        : chenjunjiang
 * CreateDate     : 2013-5-3
 */
public class PartSpiltChkManager implements PTConstants {
	public Logger logger = Logger.getLogger(PartSpiltChkManager.class);
	private PartSpiltChkDao dao = PartSpiltChkDao.getInstance();
	private PartSpiltApplyDao applyDao = PartSpiltApplyDao.getInstance();
	/**
	 * 
	 * @Title      : 
	 * @Description: 配件拆合件申请查询初始化 
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-5-3
	 */
	public void queryPartSplitApplyInit(){
		ActionContext act = ActionContext.getContext();
		RequestWrapper request = act.getRequest();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			act.setForword(PART_SPLIT_CHK_QUERY_URL);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"配件拆合件审核");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 
	 * @Title      : 
	 * @Description: 查询配件拆合申请 
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-5-3
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
	 * @Description: 拆合申请明细查询初始化  
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-5-5
	 */
	public void querySpiltApplyDetailInit(){
		ActionContext act = ActionContext.getContext();
		RequestWrapper request = act.getRequest();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			String spcpdId = CommonUtils.checkNull(request.getParamValue("spcpdId"));
			Map map = applyDao.getPartSpcpMainInfo(spcpdId);
			request.setAttribute("po", map);
			act.setForword(PART_SPLIT_CHK_URL);
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
	 * @Description: 通过拆合单id获取分总成件信息 
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-5-5
	 */
	public void querySubPartInfoBySpcpdId(){
		ActionContext act = ActionContext.getContext();
		RequestWrapper request = act.getRequest();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
      try {
			String spcpdId = CommonUtils.checkNull(request.getParamValue("spcpdId"));//拆合单id
		    
			//分页方法 begin
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1; // 处理当前页
			PageResult<Map<String, Object>> ps = applyDao.querySubpartBySpcpdIdList(spcpdId,curPage,Constant.PAGE_SIZE);
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
	 * @Description: 同意拆合申请 
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-5-5
	 */
	public void agreePartSpiltApply(){
		ActionContext act = ActionContext.getContext();
		RequestWrapper request = act.getRequest();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			String spcpdId = CommonUtils.checkNull(request.getParamValue("spcpdId"));
			String spcpdType = CommonUtils.checkNull(request.getParamValue("SPCPD_TYPE"));//拆合类型
			
			TtPartSpcpMainPO mainPO = new TtPartSpcpMainPO();
			mainPO.setSpcpdId(CommonUtils.parseLong(spcpdId));
			mainPO = (TtPartSpcpMainPO) dao.select(mainPO).get(0);
			
			if(mainPO.getState().intValue()==Constant.PART_SPCPD_STATUS_01){//拆合单状态为审核中的才可以审核
				TtPartSpcpMainPO spo = new TtPartSpcpMainPO();
				spo.setSpcpdId(CommonUtils.parseLong(spcpdId));
				TtPartSpcpMainPO po = new TtPartSpcpMainPO();
				po.setState(Constant.PART_SPCPD_STATUS_03);
				dao.update(spo, po);
				act.setOutData("success", "审核通过!");
			}else{
				act.setOutData("error", "该拆合单已经审核,请选择其他审核单审核!");
				return;
			}
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.UPDATE_FAILURE_CODE, "配件拆合申请审核");
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
	 * LastDate    : 2013-5-5
	 */
	public void rejectPartSpiltApply(){
		ActionContext act = ActionContext.getContext();
		RequestWrapper request = act.getRequest();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			String spcpdId = CommonUtils.checkNull(request.getParamValue("spcpdId"));
			String rejectReason = CommonUtils.checkNull(request.getParamValue("rejectReason"));
			
			TtPartSpcpMainPO mainPO = new TtPartSpcpMainPO();
			mainPO.setSpcpdId(CommonUtils.parseLong(spcpdId));
			mainPO = (TtPartSpcpMainPO) dao.select(mainPO).get(0);
			
			if(mainPO.getState().intValue()==Constant.PART_SPCPD_STATUS_01){//拆合单状态为审核中的才可以审核
				TtPartSpcpMainPO spo = new TtPartSpcpMainPO();
				spo.setSpcpdId(CommonUtils.parseLong(spcpdId));
				TtPartSpcpMainPO po = new TtPartSpcpMainPO();
				po.setState(Constant.PART_SPCPD_STATUS_02);
				po.setRemark1(rejectReason);
				dao.update(spo, po);
				//如果是拆件,就要释放总成件占用的库存
				if(mainPO.getSpcpdType().equals(Constant.PART_SPCPD_TYPE_01)){
					//调用库存释放逻辑
					List ins = new LinkedList<Object>();
		            ins.add(0, mainPO.getSpcpdId());
		            ins.add(1, Constant.PART_CODE_RELATION_23);
		            ins.add(2,0);// 1:占用 0：释放占用
		            dao.callProcedure("PKG_PART.P_UPDATEPARTSTATE", ins, null);
				}
				//如果是合件,就要释放分总成件占用的库存
				if(mainPO.getSpcpdType().equals(Constant.PART_SPCPD_TYPE_02)){
					List ins = new LinkedList<Object>();
		            ins.add(0, mainPO.getSpcpdId());
		            ins.add(1, Constant.PART_CODE_RELATION_27);
		            ins.add(2,0);// 1:占用 0：释放占用
		            dao.callProcedure("PKG_PART.P_UPDATEPARTSTATE", ins, null);
				}
				act.setOutData("success", "驳回成功!");
			}else{
				act.setOutData("error", "该拆合单已经驳回,请选择其他审核单审核!");
				return;
			}
			
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.UPDATE_FAILURE_CODE, "配件拆合申请审核");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	
	}
}
