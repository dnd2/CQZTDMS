package com.infodms.dms.actions.sales.ordermanage.orderacnt;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.getCompanyId.GetOemcompanyId;
import com.infodms.dms.common.materialManager.MaterialGroupManagerDao;
import com.infodms.dms.dao.common.CommonDAO;
import com.infodms.dms.dao.sales.financemanage.AccountBalanceDetailDao;
import com.infodms.dms.dao.sales.ordermanage.audit.OrderAuditDao;
import com.infodms.dms.dao.sales.ordermanage.orderacnt.OrderAcntDao;
import com.infodms.dms.dao.sales.ordermanage.orderquery.OrderSeachDao;
import com.infodms.dms.dao.sales.ordermanage.orderreport.OrderReportDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TcUserPO;
import com.infodms.dms.po.TmBusinessParaPO;
import com.infodms.dms.po.TmDateSetPO;
import com.infodms.dms.po.TmDealerPO;
import com.infodms.dms.po.TmOrderOrgPO;
import com.infodms.dms.po.TmVehiclePO;
import com.infodms.dms.po.TtVsDlvryDtlPO;
import com.infodms.dms.po.TtVsDlvryPO;
import com.infodms.dms.po.TtVsDlvryReqPO;
import com.infodms.dms.po.TtVsOrderPO;
import com.infodms.dms.po.TtVsReqCheckPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.DateTimeUtil;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.ActionContextExtend;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;

public class OrderAcntQuery {
	private Logger logger = Logger.getLogger(OrderAcntQuery.class);
	private ActionContext act = ActionContext.getContext();
	RequestWrapper request = act.getRequest();
	private final OrderAcntDao dao = OrderAcntDao.getInstance();

	private final String ORDER_ACNT_URL = "/jsp/sales/ordermanage/orderacnt/orderAcntInit.jsp";// 账号大区维护
	private final String ORDER_ACNT_ADD_URL = "/jsp/sales/ordermanage/orderacnt/orderAcntAddInit.jsp";// 账号大区新增
	private final String ORDER_ACNT_ADD_SELECT_URL = "/jsp/sales/ordermanage/orderacnt/orderAcntAddSelectInit.jsp";// 账号大区新增
	private final String ORDER_LARGE_ADD_SELECT_URL = "/jsp/sales/ordermanage/orderacnt/orderLargeAddSelectInit.jsp";// 账号大区新增
	private final String ORDER_ACNT_MOD_URL = "/jsp/sales/ordermanage/orderacnt/orderAcntModInit.jsp";// 账号大区新增

	private final String ORDER_DETAIL_QUERY_URL = "/jsp/sales/ordermanage/orderquery/orderSeachDetail.jsp";// 批售订单明细页
	
	public void OrderAcntQueryInit() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String orderAcnt=request.getParamValue("orderAcnt"); // 账号
			String areaLarge=request.getParamValue("areaLarge"); // 大区
			String acntPose=request.getParamValue("acntPose"); // 职位
			String userId=logonUser.getUserId().toString();
			
			act.setForword(ORDER_ACNT_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "订单资源审核查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * 订单资源审核查询
	 */
	public void orderAcntQuery() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String orderAcnt = request.getParamValue("orderAcnt"); // 账号
			String largeArea = request.getParamValue("largeArea"); // 大区
			String orderPose = request.getParamValue("orderPose"); // 职位
			String companyId=String.valueOf(logonUser.getCompanyId());
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1;
			PageResult<Map<String, Object>> ps = dao.getOrderAcntQuery(orderAcnt,largeArea,orderPose,companyId,curPage, Constant.PAGE_SIZE);
			act.setOutData("ps", ps);
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "订单资源审核查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	//账号新增初始化
	public void orderAcntAddInit(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String orderAcnt=request.getParamValue("orderAcnt"); // 账号
			String areaLarge=request.getParamValue("areaLarge"); // 大区
			String acntPose=request.getParamValue("acntPose"); // 职位
			String userId=logonUser.getUserId().toString();
			act.setForword(ORDER_ACNT_ADD_URL);
		}catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "订单账号新增");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	public void showOrderAcnt(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			act.setForword(ORDER_ACNT_ADD_SELECT_URL);
		}catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "订单账号新增");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	public void showOrderLarge(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String largeIds=request.getParamValue("largeIdss");
			act.setOutData("largeIds", largeIds);
			act.setForword(ORDER_LARGE_ADD_SELECT_URL);
		}catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "订单账号新增");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	
	/**
	 * 
	 */
	public void orderAcntAddQuery() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String orderAcnt = request.getParamValue("orderAcnt"); // 账号
			String acntName = request.getParamValue("acntName"); // 姓名
			String companyId=String.valueOf(logonUser.getCompanyId());
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1;
			PageResult<Map<String, Object>> ps = dao.getOrderAcntAddQuery(orderAcnt,acntName,companyId,curPage, Constant.PAGE_SIZE);
			Map<String,Object> map=ps.getRecords().get(0);
			act.setOutData("ps", ps);
			act.setOutData("info",map);
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "订单账号新增");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	public void orderLargeAddQuery() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String largeIds = request.getParamValue("largeIds"); // 
			String largeName = request.getParamValue("largeName"); // 
			String largeCode = request.getParamValue("largeCode"); // 
			String companyId=String.valueOf(logonUser.getCompanyId());
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1;
			PageResult<Map<String, Object>> ps = dao.getOrderLargeAddQuery(largeIds,largeName,largeCode,companyId,curPage, Constant.PAGE_SIZE);
			act.setOutData("ps", ps);
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "订单账号新增");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	public void orderLargeAddMap() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String largeId = request.getParamValue("largeId"); // 
			String companyId=String.valueOf(logonUser.getCompanyId());
			Map<String, Object> info = dao.getOrderLargeAddMap(largeId,companyId);
			act.setOutData("info", info);
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "订单账号新增");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	public void orderAcntLargeAdd(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String orderAcntId = request.getParamValue("orderAcntId"); // 
			String largeIds = request.getParamValue("largeIds"); // 
			//String[] largeId=largeIds.split(",");
			/*for(int i=0;i<largeId.length;i++){
				TmOrderOrgPO toop=new TmOrderOrgPO();
				toop.setOrderOrgId(Long.valueOf(SequenceManager.getSequence("")));
				toop.setUserId(Long.valueOf(orderAcntId));
				toop.setOrgId(Long.valueOf(largeId[i]));
				toop.setStatus(Constant.STATUS_ENABLE);
				dao.insert(toop);
			}*/
			Enumeration<String> enumeration = request.getParamNames();
			while (enumeration.hasMoreElements()) {
				String temp = enumeration.nextElement();
				if (temp.length() > 5
						&& temp.substring(0, 5).equals("orgId")) {
					String subStr = temp.substring(5, temp.length());
					TmOrderOrgPO toop=new TmOrderOrgPO();
					toop.setOrderOrgId(Long.valueOf(SequenceManager.getSequence("")));
					toop.setUserId(Long.valueOf(orderAcntId));
					String orgId=request.getParamValue("orgId"+subStr);
					toop.setOrgId(Long.valueOf(orgId));
					toop.setStatus(Constant.STATUS_ENABLE);
					dao.insert(toop);
				}
			}
			act.setOutData("returnValue", 1);
		}catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "订单账号新增");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	public void orderAcntModInit(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			//String orderOrgId=request.getParamValue("orderOrgId");
			String orderOrgId=CommonUtils.checkNull(request.getParamValue("orderOrgId"));
			TmOrderOrgPO toop=new TmOrderOrgPO();
			toop.setOrderOrgId(Long.valueOf(orderOrgId));
			List toopList=dao.select(toop);
			toop=(TmOrderOrgPO) toopList.get(0);
			
			String userId=toop.getUserId().toString(); // 
			String companyId=String.valueOf(logonUser.getCompanyId());
			TcUserPO tup=new TcUserPO();
			tup.setUserId(Long.valueOf(userId));
			List ttlist=dao.select(tup);
			tup=(TcUserPO) ttlist.get(0);
			act.setOutData("acnt", tup.getAcnt());
			act.setOutData("name", tup.getName());
			act.setOutData("userId", tup.getUserId());
			act.setOutData("orderOrgId", orderOrgId);
			List<Map<String,Object>> list=dao.getOrderLargeByQuery(userId,companyId);
			StringBuffer largeIds = new StringBuffer();
			for(int i=0;i<list.size();i++){
				if(i<list.size()-1){
					largeIds=largeIds.append(String.valueOf(list.get(i).get("ORG_ID"))).append(",");
				}else{
					largeIds=largeIds.append(String.valueOf(list.get(i).get("ORG_ID")));
				}
			}
			act.setOutData("largeIds", String.valueOf(largeIds));
			act.setOutData("list", list);
			act.setForword(ORDER_ACNT_MOD_URL);
		}catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "订单账号新增");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	public void orderAcntLargeMod(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			//String orderOrgId=CommonUtils.checkNull(request.getParamValue("orderOrgId"));
			String orderAcntId = request.getParamValue("orderAcntId"); // 
			//String orderOrgId=request.getParamValue("orderOrgId");
			//String largeIds = request.getParamValue("largeIds"); // 
			//String[] largeId=largeIds.split(",");
			TmOrderOrgPO deltoop=new TmOrderOrgPO();
			deltoop.setUserId(Long.parseLong(orderAcntId));
			dao.delete(deltoop);
			Enumeration<String> enumeration = request.getParamNames();
			while (enumeration.hasMoreElements()) {
				String temp = enumeration.nextElement();
				if (temp.length() > 5
						&& temp.substring(0, 5).equals("orgId")) {
					String subStr = temp.substring(5, temp.length());
					TmOrderOrgPO toop=new TmOrderOrgPO();
					toop.setOrderOrgId(Long.valueOf(SequenceManager.getSequence("")));
					toop.setUserId(Long.valueOf(orderAcntId));
					String orgId=request.getParamValue("orgId"+subStr);
					toop.setOrgId(Long.valueOf(orgId));
					toop.setStatus(Constant.STATUS_ENABLE);
					dao.insert(toop);
				}
			}
			act.setOutData("returnValue", 1);
		}catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "订单账号新增");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	public void orderAcntDelInit(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
		String orderOrgId=request.getParamValue("orderOrgId");
		TmOrderOrgPO deltoop=new TmOrderOrgPO();
		deltoop.setOrderOrgId(Long.parseLong(orderOrgId));
		dao.delete(deltoop);
		act.setOutData("returnValue",1);
		}catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "订单账号删除");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
}
