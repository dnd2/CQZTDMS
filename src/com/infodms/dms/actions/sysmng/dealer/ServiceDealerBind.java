package com.infodms.dms.actions.sysmng.dealer;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.dao.sales.dealer.ServiceDealerBindDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TmDealerPO;
import com.infodms.dms.po.TtServiceDealerBindPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

/**
 * 售后经销商绑定
 * 
 * @author jiangrp
 *
 */
public class ServiceDealerBind {
	private final String serviceDealerBindInit = "/jsp/systemMng/dealer/serviceDealerBindInit.jsp";
	
	private final String queryBindServiceDealerInit = "/jsp/systemMng/dealer/queryBindServiceDealerInit.jsp";

	public Logger logger = Logger.getLogger(ServiceDealerBind.class);
	ActionContext act = ActionContext.getContext();
	RequestWrapper request = act.getRequest();
	ServiceDealerBindDao dao = ServiceDealerBindDao.getInstance();
	
	/**
	 * 售后经销商信息查询页面初始化
	 */
	public void  serviceDealerBindInit() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			act.setForword(serviceDealerBindInit);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "售后经销商信息查询初始化");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 售后经销商查询
	 */
	public void queryServiceDealerInfo() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			
			map.put("DEALER_CODE", CommonUtils.checkNull(request.getParamValue("DEALER_CODE")));
			map.put("DEALER_NAME", CommonUtils.checkNull(request.getParamValue("DEALER_NAME")));
			map.put("poseId", logonUser.getPoseId());
			
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
			PageResult<Map<String, Object>> ps = dao.queryServiceDealerInfo(map, curPage, Constant.PAGE_SIZE);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "经销商查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 售后经销商绑定页面初始化
	 */
	public void queryBindServiceDealerInit() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String dealerId = CommonUtils.checkNull(request.getParamValue("DEALER_ID"));
			String successFlag = CommonUtils.checkNull(request.getParamValue("SUCCESS"));
			TmDealerPO td = new TmDealerPO();
			td.setDealerId(Long.parseLong(dealerId));
			td = (TmDealerPO) dao.select(td).get(0);
			act.setOutData("dealerName", td.getDealerName());
			act.setOutData("curDealerId", td.getDealerId());
			if ("1".equals(successFlag)) {
				act.setOutData("successMessage", "经销商绑定成功!");
			} else if ("2".equals(successFlag)) {
				act.setOutData("successMessage", "经销商解除绑定成功!");
			}
			act.setForword(queryBindServiceDealerInit);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "售后经销商绑定初始化");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 绑定经销商查询
	 */
	public void queryBindServiceDealerInfo() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("CUR_DEALER_ID", CommonUtils.checkNull(request.getParamValue("CUR_DEALER_ID")));
			
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
			PageResult<Map<String, Object>> ps = dao.queryBindServiceDealerInfo(map, curPage, Constant.PAGE_SIZE);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "绑定经销商查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 售后经销商绑定CHECK
	 */
	public void bindServiceDealerCheck() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String dealerId = CommonUtils.checkNull(request.getParamValue("DEALER_ID"));
			TtServiceDealerBindPO ttServiceDealerBindPO = new TtServiceDealerBindPO();
			ttServiceDealerBindPO.setBindDealerId(Long.parseLong(dealerId));
			ttServiceDealerBindPO.setStatus(Constant.IF_TYPE_YES);
			List<PO> ttServiceDealerBindPOLst = dao.select(ttServiceDealerBindPO);
			
			// 选中经销商已被其它经销商绑定
			if (ttServiceDealerBindPOLst != null && !ttServiceDealerBindPOLst.isEmpty()) {
				throw new BizException(act, ErrorCodeConstant.SPECIAL_MEG, "选定经销商已被其余经销商绑定，不能再进行绑定操作!");
			}
			
			// 选定经销商之前有绑定过其它经销商
			ttServiceDealerBindPO = new TtServiceDealerBindPO();
			ttServiceDealerBindPO.setBindFlag(Long.parseLong(dealerId));
			ttServiceDealerBindPO.setStatus(Constant.IF_TYPE_YES);
			ttServiceDealerBindPOLst = dao.select(ttServiceDealerBindPO);
			if (ttServiceDealerBindPOLst != null && !ttServiceDealerBindPOLst.isEmpty()) {
				act.setOutData("warningMessage", "警告：选定经销商之前有绑定过其它经销商，确定将会将其绑定的经销商一同绑定到现有经销商.");
			}
		} catch (BizException e1) {
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 售后经销商绑定
	 */
	public void bindServiceDealer() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String dealerId = CommonUtils.checkNull(request.getParamValue("DEALER_ID"));
			String curDealerId = CommonUtils.checkNull(request.getParamValue("CUR_DEALER_ID"));
			
			// 先绑定选定的经销商
			TtServiceDealerBindPO ttServiceDealerBindPO = new TtServiceDealerBindPO();
			ttServiceDealerBindPO.setBindDealerId(Long.parseLong(dealerId));
			ttServiceDealerBindPO.setBindFlag(Long.parseLong(curDealerId));
			ttServiceDealerBindPO.setBindId(new Long(SequenceManager.getSequence("")));
			ttServiceDealerBindPO.setCreateBy(logonUser.getUserId());
			ttServiceDealerBindPO.setCreateDate(new Date());
			ttServiceDealerBindPO.setStatus(Constant.IF_TYPE_YES);
			dao.insert(ttServiceDealerBindPO);
			
			// 如果选定经销商 之前有绑定过其它经销商，则一并绑定到现经销商
			ttServiceDealerBindPO = new TtServiceDealerBindPO();
			ttServiceDealerBindPO.setBindFlag(Long.parseLong(dealerId));
			ttServiceDealerBindPO.setStatus(Constant.IF_TYPE_YES);
			List<PO> ttServiceDealerBindPOLst = dao.select(ttServiceDealerBindPO);
			if (ttServiceDealerBindPOLst != null && !ttServiceDealerBindPOLst.isEmpty()) {
				for (PO po : ttServiceDealerBindPOLst) {
					TtServiceDealerBindPO po1 = (TtServiceDealerBindPO) po;
					ttServiceDealerBindPO = new TtServiceDealerBindPO();
					ttServiceDealerBindPO.setBindDealerId(po1.getBindDealerId());
					ttServiceDealerBindPO.setBindFlag(Long.parseLong(curDealerId));
					ttServiceDealerBindPO.setBindId(new Long(SequenceManager.getSequence("")));
					ttServiceDealerBindPO.setCreateBy(logonUser.getUserId());
					ttServiceDealerBindPO.setCreateDate(new Date());
					ttServiceDealerBindPO.setStatus(Constant.IF_TYPE_YES);
					dao.insert(ttServiceDealerBindPO);
					dao.delete(po1);
				}
			}
			act.setRedirect("/sysmng/dealer/ServiceDealerBind/queryBindServiceDealerInit.do?SUCCESS=1&DEALER_ID="+curDealerId);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "售后经销商绑定失败,请联系系统管理员!");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 解除绑定
	 */
	public void relieveBindServiceDealer() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {

			String dealerId = CommonUtils.checkNull(request.getParamValue("DEALER_ID"));
			String curDealerId = CommonUtils.checkNull(request.getParamValue("CUR_DEALER_ID"));
			
			TtServiceDealerBindPO ttServiceDealerBindPO = new TtServiceDealerBindPO();
			ttServiceDealerBindPO.setBindDealerId(Long.parseLong(dealerId));
			ttServiceDealerBindPO.setBindFlag(Long.parseLong(curDealerId));
			dao.delete(ttServiceDealerBindPO);
			act.setRedirect("/sysmng/dealer/ServiceDealerBind/queryBindServiceDealerInit.do?SUCCESS=2&DEALER_ID="+curDealerId);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "售后经销商解除绑定失败,请联系系统管理员!");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
}
