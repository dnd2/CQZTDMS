package com.infodms.dms.actions.partsmanage.purchase;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.infodms.dms.actions.partsmanage.common.DonoInfoSet;
import com.infodms.dms.actions.partsmanage.common.PartInfoSet;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.bean.PartinfoBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.constants.PTConstants;
import com.infodms.dms.dao.partinfo.PartInfoItemDao;
import com.infodms.dms.dao.partinfo.PartinfoDao;
import com.infodms.dms.dao.partinfo.PartorderDao;
import com.infodms.dms.dao.partinfo.SignInfoDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TtPtDlrSignDetailPO;
import com.infodms.dms.po.TtPtDlrSignPO;
import com.infodms.dms.po.TtPtOrderLogPO;
import com.infodms.dms.po.TtPtOrderPO;
import com.infodms.dms.po.TtPtOrditemPO;
import com.infodms.dms.po.TtPtShippingsheetPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.DateTimeUtil;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.dms.chana.actions.OSC45;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;
import com.infodms.dms.util.CycleToTimeUtil;
import java.util.Collections;
/**
 * @Title: PurchaseOrderSearch.java
 *
 * @Description:CHANADMS
 *
 * @Copyright: Copyright (c) 2010
 *
 * @Company: www.infoservice.com.cn
 * @Date: 2010-6-10
 *
 * @author lishuai 
 * @mail   lishuai103@yahoo.cn	
 * @version 1.0
 * @remark 
 */
public class PurchaseOrderSearch implements PTConstants {
	public Logger logger = Logger.getLogger(PurchaseOrderSearch.class);
	private ActionContext act = ActionContext.getContext();
	private AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
	private RequestWrapper request = act.getRequest();
	public PartInfoItemDao partInfoItemDao= PartInfoItemDao.getInstance();
	private final PartorderDao dao = (PartorderDao) PartorderDao.getInstance();
	private final SignInfoDao sao = (SignInfoDao) SignInfoDao.getInstance();

	/**
	 * 配件采购订单查询初始化
	 */
	public void purchaseOrderSearchInfo(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			act.setForword(poinitUrl);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"配件采购订单初始化");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 配件采购订单查询
	 */
	public void purchaseOrderSearch(){
		ActionContext act = ActionContext.getContext();
		RequestWrapper request = act.getRequest();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			String orderNo = CommonUtils.checkNull(request.getParamValue("orderNo"));//采购订单单号
			String beginTime = CommonUtils.checkNull(request.getParamValue("beginTime"));//开始时间
			String endTime = CommonUtils.checkNull(request.getParamValue("endTime"));//结束时间
			String orderStatus = CommonUtils.checkNull(request.getParamValue("orderStatus"));//采购订单状态
			String dealerCode = CommonUtils.checkNull(request.getParamValue("dealerCode"));//经销商代码
			String dealerName = CommonUtils.checkNull(request.getParamValue("dealerName"));//经销商名称
			PartinfoBean bean = new PartinfoBean();
			bean.setOrderNo(orderNo);
			bean.setBeginTime(beginTime);
			bean.setEndTime(endTime);
			bean.setOrderStatus(orderStatus);
			bean.setDealerId(logonUser.getDealerId());
			bean.setDealerCode(dealerCode);
			bean.setDealerName(dealerName);
			PartorderDao dao = new PartorderDao();
			//分页方法 begin
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1; // 处理当前页	
			PageResult<Map<String, Object>> ps = dao.queryPartOrderList(bean,curPage,Constant.PAGE_SIZE);
			//分页方法 end
			act.setOutData("ps", ps);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"配件采购订单");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 配件采购订单详细查询
	 */
	public void partOrderInfo(){
		ActionContext act = ActionContext.getContext();
		RequestWrapper request = act.getRequest();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			String orderId = CommonUtils.checkNull(request.getParamValue("orderId"));//采购订单编号
			PartorderDao dao = new PartorderDao();
			Map<String, Object> orderInfo = dao.getOrderInfo(orderId);//订单明细
			List<Map<String, Object>> partInfo = dao.getPartInfo(orderId);//订单中配件明细
			List<Map<String, Object>> orderLog = dao.getOrderLog(orderId);//订单审核流程
			act.setOutData("orderInfo", orderInfo);
			act.setOutData("partInfo", partInfo);
			act.setOutData("orderLog", orderLog);
			act.setForword(orderInfoUrl);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"配件采购订单详细");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 配件采购订单编辑
	 */
	public void purchaseOrderCompile(){
		//清空缓存中的配件项
		PartInfoSet.removePartInfoSet(String.valueOf(logonUser.getUserId()));
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			String dealerId = logonUser.getDealerId();  //SESSION中去经销商ID
			PartorderDao dao = new PartorderDao();
			List<Map<String, Object>> dcList = dao.getDClist(dealerId); //经销商相关联的供货方
			act.setOutData("dcList", dcList);
			act.setForword(compileUrl);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"件采购订单编辑");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 动态生成经销商资金
	 */
	public void getAmount(){
		ActionContext act = ActionContext.getContext();
		RequestWrapper request = act.getRequest();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			String dealerId = logonUser.getDealerId();  //经销商ID
			String dcId = CommonUtils.checkNull(request.getParamValue("dc"));//供货方ID
			if(dcId.equals("")){
				act.setOutData("amount", null);
			}else{
				PartorderDao dao = new PartorderDao();
			    Map<String, Object> map = dao.getAmount(dealerId, dcId);//获取资金
			    String amount = null;
			    if(map != null && map.size()>0){
			    	amount = String.valueOf(map.get("AMOUNT"));
			    }
				act.setOutData("amount", amount);
			}
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"生成经销商资金");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/*
	 * 查询供货备件信息初始化
	 */
	
	public void getDCPart(){
		ActionContext act = ActionContext.getContext();
		RequestWrapper request = act.getRequest();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			String dealerId = logonUser.getDealerId();
			String dcId = CommonUtils.checkNull(request.getParamValue("dcId"));//供货方ID
			PartorderDao dao = new PartorderDao();
			List<Map<String, Object>> dcList = dao.getDClist(dealerId);//供货方列表
			act.setOutData("dcList", dcList);
			act.setOutData("dcId", dcId);
			act.setForword(dcPartUrl);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"供货备件信息");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/*
	 * 查询供货方备件信息
	 */
	
	public void queryDCPartStock(){
		ActionContext act = ActionContext.getContext();
		RequestWrapper request = act.getRequest();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			String partCode = CommonUtils.checkNull(request.getParamValue("partCode"));//配件代码
			String partName = CommonUtils.checkNull(request.getParamValue("partName"));//配件名称
			String dcId = CommonUtils.checkNull(request.getParamValue("dcId"));//供货方ID
			PartinfoBean bean = new PartinfoBean();
			bean.setPartCode(partCode);
			bean.setPartName(partName);
			bean.setDcId(dcId);
			PartorderDao dao = new PartorderDao();
			//分页方法 begin
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1; // 处理当前页	
			PageResult<Map<String, Object>> ps = dao.queryDCPartStock(bean,curPage,Constant.PAGE_SIZE);
			//分页方法 end
			act.setOutData("ps", ps);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"供货备件信息");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	public void savePtOrder() {
		String dealerId = logonUser.getDealerId();
		PartorderDao dao = new PartorderDao();
		try {
			TtPtOrderPO po = assembleTtPtOrderPO(request, dealerId);
			dao.insert(po);
			//从缓存取出子表数据
			saveOrderItem(po);
		} catch (ParseException e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.ADD_FAILURE_CODE,"配件采购订单");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	//保存订单配件详细
	public void saveOrderItem(TtPtOrderPO po){
		TtPtOrditemPO tp = new TtPtOrditemPO();
		tp.setOrderId(po.getOrderId());
		partInfoItemDao.delete(tp);
		Set<PartinfoBean> partInfoSet = PartInfoSet.getPartInfoSet(String.valueOf(logonUser.getUserId()));
		String[] counts = request.getParamValues("pcount");
		if(counts != null && counts.length>0){
			String[] remarks = request.getParamValues("premark");
			Iterator<PartinfoBean> it = partInfoSet.iterator();
			List<String> partIds = new ArrayList<String>();
			while (it.hasNext()) {
				partIds.add(it.next().getPartId());
			}
			for (int i = 0; i < counts.length; i++) {
				TtPtOrditemPO tt = new TtPtOrditemPO();
				tt.setOrderId(po.getOrderId());
				tt.setDetailId(Long.parseLong(SequenceManager.getSequence("")));
				tt.setPartId(Long.parseLong(partIds.get(i)));
				tt.setRemark(remarks[i]);
				tt.setOrderCount(Integer.parseInt(counts[i]));
				tt.setCreateBy(po.getCreateBy());
				tt.setCreateDate(po.getCreateDate());
				partInfoItemDao.insert(tt);
			}
		}
		PartInfoSet.removePartInfoSet(String.valueOf(logonUser.getUserId()));//清空缓存
		act.setForword(purchaseUrl);
	}
	
	//保存配件订单主表信息
	private TtPtOrderPO assembleTtPtOrderPO(RequestWrapper request, String dealerId) throws ParseException {
		
		String requireDate = CommonUtils.checkNull(request.getParamValue("requireDate"));//要求到货时间
		String freightType = CommonUtils.checkNull(request.getParamValue("freightType"));//运输类型
		String dcId = CommonUtils.checkNull(request.getParamValue("sel_dcId"));//供货方
		String remark = CommonUtils.checkNull(request.getParamValue("remark"));//备注
		String cot = CommonUtils.checkNull(request.getParamValue("cot"));//价格
		String cou = CommonUtils.checkNull(request.getParamValue("cou"));//行数
		String dis = CommonUtils.checkNull(request.getParamValue("dis"));//折扣
		String ordStatus = CommonUtils.checkNull(request.getParamValue("ordStatus"));//订单状态
		if(dis.equals("")){
			dis = "0";
		}
		TtPtOrderPO po = new TtPtOrderPO();
		po.setOrderId(Long.parseLong(SequenceManager.getSequence("")));//ID
		po.setOrderNo(SequenceManager.getSequence(PT_NO));//编号
		po.setOrderType(new Integer(dao.getDealerLevel(dealerId)));//订单类型
		po.setDealerId(Long.parseLong(dealerId));//经销商ID
		po.setHighDealerId(new Long(dao.getHighDealerId(dealerId)));//上级经销商ID
		po.setDcId(Long.parseLong(dcId));//供货方ID
		if (null != requireDate && !"".equals(requireDate)) {
			po.setRequireDate(DateTimeUtil.stringToDate(requireDate));//到货日期
		}
		
		po.setTransType(Integer.parseInt(freightType));//供货方式
		po.setOrderStatus(new Integer(ordStatus));//订单状态
		po.setOrderPrice(new Float(cot));//订单总价格
		po.setDiscountRate(new Float(dis));//折扣
		po.setItemCount(new Integer(cou));//行数
		po.setRaiseTimes(new Integer(0));//上报次数
		if (null != remark && !"".equals(remark)) {
			po.setRemark(remark.trim());//备注
		}
		
		po.setHighOperationType(Constant.ORG_TYPE_DEALER);//上级部门类型
		po.setOperationId(new Long(logonUser.getDealerId()));
		po.setCreateBy(logonUser.getUserId());//创建人
		po.setCreateDate(new Date(System.currentTimeMillis()));//创建时间	
		po.setUpdateBy(po.getCreateBy());//修改人
		po.setUpdateDate(po.getCreateDate());//修改时间
		if(ordStatus.equals(Constant.PART_ORDER_STATUS_02)){//新增log表
			TtPtOrderLogPO tt = new TtPtOrderLogPO();
			tt.setLogId(Long.parseLong(SequenceManager.getSequence("")));
			tt.setOrderId(po.getOrderId());
			tt.setOgrId(po.getOperationId());
			tt.setOrgType(po.getHighOperationType());
			tt.setUserId(po.getUpdateBy());
			tt.setNodeStatus(po.getOrderStatus());
			tt.setOpertateDate(new Date());
			tt.setIsInterface(Integer.parseInt("0"));
			tt.setCreateBy(po.getCreateBy());
			tt.setCreateDate(new Date());
			dao.insert(tt);
		}
		return po;
	}
	
	/*
	 * 添加要采购的配件
	 */
	public void setPartInfo(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		try {
			String flag = request.getParamValue("flag");
			String dcId = CommonUtils.checkNull(request.getParamValue("dcId"));
			String orderId = request.getParamValue("orderId");
			String signNo = CommonUtils.checkNull(request.getParamValue("signNo"));
			String claimId = CommonUtils.checkNull(request.getParamValue("claimId"));
			if(!dcId.equals("")){
				PartorderDao dao = new PartorderDao();
				Map<String, Object> dc = dao.getDCName(dcId);//获取供货方名称
				act.setOutData("dc", dc);
			}
			act.setOutData("flag", flag);
			act.setOutData("orderId", orderId);
			act.setOutData("signNo", signNo);
			act.setOutData("claimId", claimId);
			act.setForword(addPartUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE,"供货备件信息");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/*
	 * 分页查询供货方配件明细
	 */
	
	public void queryDCPart(){
		ActionContext act = ActionContext.getContext();
		RequestWrapper request = act.getRequest();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			String partCode = CommonUtils.checkNull(request.getParamValue("partCode"));//配件代码
			String partName = CommonUtils.checkNull(request.getParamValue("partName"));//配件名称
			String dcId = CommonUtils.checkNull(request.getParamValue("dcId"));//供货方ID
			String orderId = CommonUtils.checkNull(request.getParamValue("orderId"));//订单ID
			String signNo = CommonUtils.checkNull(request.getParamValue("signNo"));//签收单编号
			PartinfoBean bean = new PartinfoBean();
			bean.setPartCode(partCode);
			bean.setPartName(partName);
			bean.setDcId(dcId);
			bean.setOrderId(orderId);
			bean.setSignNo(signNo);
			PartorderDao dao = new PartorderDao();
			//分页方法 begin
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1; // 处理当前页	
			PageResult<Map<String, Object>> ps = null;
			if ("".equals(signNo)) {
				ps = dao.queryDCPartStock(bean,curPage,Constant.PAGE_SIZE);
			} else {
				ps = dao.queryDCPartStockBySignNo(bean,curPage,Constant.PAGE_SIZE);
			}
			//分页方法 end
			act.setOutData("ps", ps);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"供货备件信息");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/*
	 * 配件采购订单管理
	 */
	public void purchaseOrderManage(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			String dealerId = logonUser.getDealerId();  //SESSION中取经销商ID
			List<Map<String, Object>> dcList = dao.getDClist(dealerId); //经销商相关联的供货方
			act.setOutData("dcList", dcList);
			act.setForword(purchaseUrl);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"配件采购订单");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/*
	 * 分页管理配件订单
	 */
	public void queryOrderList(){
		ActionContext act = ActionContext.getContext();
		RequestWrapper request = act.getRequest();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			String orderNo = CommonUtils.checkNull(request.getParamValue("orderNo"));//配件编号
			String beginTime = CommonUtils.checkNull(request.getParamValue("beginTime"));//开始时间
			String endTime = CommonUtils.checkNull(request.getParamValue("endTime"));//结束时间
			String dcId = CommonUtils.checkNull(request.getParamValue("dcId"));//供货方ID
			String orderStatus = CommonUtils.checkNull(request.getParamValue("orderStatus"));//配件状态
			PartinfoBean bean = new PartinfoBean();
			bean.setOrderNo(orderNo);
			bean.setBeginTime(beginTime);
			bean.setEndTime(endTime);
			bean.setDcId(dcId);
			bean.setOrderStatus(orderStatus);
			bean.setDealerId(logonUser.getDealerId());//获取当前经销商ID
			
			PartorderDao dao = new PartorderDao();
			//分页方法 begin
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1; // 处理当前页	
			PageResult<Map<String, Object>> ps = dao.queryOrderList(bean,curPage,Constant.PAGE_SIZE);
			//分页方法 end
			act.setOutData("ps", ps);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"供货备件信息");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/*
	 * 删除配件订单
	 */
	public void delOrderPart(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			String orderId = request.getParamValue("orderId");//取配件订单ID
			TtPtOrderPO po = new TtPtOrderPO();
			po.setOrderId(new Long(orderId));
			dao.delete(po);//删除操作
			act.setOutData("returnValue", 1);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.DELETE_FAILURE_CODE,"配件订单");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/*
	 * 修改配件订单
	 */
	public void updateOrderPartInit(){
		//清空缓存中的配件项
		PartInfoSet.removePartInfoSet(String.valueOf(logonUser.getUserId()));
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			String orderId = request.getParamValue("orderId");//配件订单ID
			String dealerId = logonUser.getDealerId();//经销商ID
			List<Map<String, Object>> dcList = dao.getDClist(dealerId); //经销商相关联的供货方
			Map<String, Object> orderMap = dao.getOrderMap(orderId);//订单主表信息
			act.setOutData("orderMap", orderMap);
			act.setOutData("dcList", dcList);
			act.setForword(updateOrderUrl);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.DELETE_FAILURE_CODE,"配件订单");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	//json方式查询配件订单子表
	public void selOrderPartInfo(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			Set<PartinfoBean> partInfo = PartInfoSet.getPartInfoSet(String.valueOf(logonUser.getUserId()));
			String orderId = request.getParamValue("orderId");//配件订单ID
			String dealerId = logonUser.getDealerId();//经销商ID
			PartinfoDao partinfoDao = new PartinfoDao();
			List<Map<String, Object>> partLists = dao.getpartList(orderId);//配件ID列表
			if(partLists.size()>0){
				for(Map<String, Object> partList : partLists){
					Map<String, Object> part = partinfoDao.getPartDetailInfo(String.valueOf(partList.get("PART_ID")),orderId, dealerId);
					if (null != part) {
						PartinfoBean bean = new PartinfoBean();
						bean.setPartId(CommonUtils.checkNull(part.get("PART_ID")));//配件ID
						bean.setPartCode(CommonUtils.checkNull(part.get("PART_CODE")));//配件代码
						bean.setPartName(CommonUtils.checkNull(part.get("PART_NAME")));//配件名称
						bean.setUnit(CommonUtils.checkNull(part.get("UNIT")));//单位
						bean.setMiniPack(CommonUtils.checkNull(part.get("MINI_PACK")));//最小包装数
						bean.setPaperQuantity(CommonUtils.checkNull(part.get("PAPER_QUANTITY")).equals("")?"0":CommonUtils.checkNull(part.get("PAPER_QUANTITY")));//账面库存
						bean.setSafeQuantity(CommonUtils.checkNull(part.get("SAFE_QUANTITY")).equals("")?"0":CommonUtils.checkNull(part.get("SAFE_QUANTITY")));//安全库存
						bean.setSalePrice(CommonUtils.checkNull(part.get("SALE_PRICE")).equals("")?"0.00":CommonUtils.checkNull(part.get("SALE_PRICE")));//单价
						bean.setDiscountRate(CommonUtils.checkNull(part.get("DISCOUNT_RATE")).equals("")?"1.00":CommonUtils.checkNull(part.get("DISCOUNT_RATE")));//折扣
						NumberFormat nbf = NumberFormat.getInstance(); 
						nbf.setMinimumFractionDigits(2);//设置小数点后2位 
						double d = Double.parseDouble(bean.getSalePrice()) * Double.parseDouble(bean.getDiscountRate());
						String dd = nbf.format(d);
						bean.setDisPrice(dd);//折扣后价格
						bean.setCount(CommonUtils.checkNull(part.get("ORDER_COUNT")).equals("")?"0":CommonUtils.checkNull(part.get("ORDER_COUNT")));//订购数量
						bean.setRemark(CommonUtils.checkNull(part.get("REMARK")));//备注
						double p = Double.parseDouble(bean.getDisPrice()) * Double.parseDouble(bean.getCount());
						String pp = nbf.format(p);
						bean.setOrderPrice(pp);//总价格
						partInfo.add(bean);
					}
				}
			}
			PartInfoSet.setPartInfoSet(String.valueOf(logonUser.getUserId()), partInfo);
			act.setOutData("partInfoSet", partInfo);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.UPDATE_FAILURE_CODE,"配件订单");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	//修改配件订单
	public void updatePtOrder(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			TtPtOrderPO po = new TtPtOrderPO();
			TtPtOrderPO tp = new TtPtOrderPO();
			String orderId = request.getParamValue("orderId");
			tp.setOrderId(new Long(orderId));
			String requireDate = CommonUtils.checkNull(request.getParamValue("requireDate"));//要求到货时间
			String freightType = CommonUtils.checkNull(request.getParamValue("freightType"));//运输类型
			String dcId = CommonUtils.checkNull(request.getParamValue("sel_dcId"));//供货方
			String remark = CommonUtils.checkNull(request.getParamValue("remark"));//备注
			String cot = CommonUtils.checkNull(request.getParamValue("cot"));//价格
			String cou = CommonUtils.checkNull(request.getParamValue("cou"));//行数
			String dis = CommonUtils.checkNull(request.getParamValue("dis"));//折扣
			String ordStatus = CommonUtils.checkNull(request.getParamValue("ordStatus"));//订单状态
			if(dis.equals("")){
				dis = "0";
			}
			po.setDcId(Long.parseLong(dcId));//供货方ID
			po.setRequireDate(DateTimeUtil.stringToDate(requireDate));//到货日期
			po.setTransType(Integer.parseInt(freightType));//供货方式
			po.setOrderStatus(new Integer(ordStatus));//订单状态
			po.setOrderPrice(new Float(cot));//订单总价格
			po.setDiscountRate(new Float(dis));//折扣
			po.setItemCount(new Integer(cou));//行数
			po.setRemark(remark);//备注
			po.setUpdateBy(logonUser.getUserId());
			po.setUpdateDate(new Date(System.currentTimeMillis()));//创建时间	
			po.setOrderId(new Long(orderId));
			dao.update(tp, po);//修改配件订单主表
			if(ordStatus.equals(Constant.PART_ORDER_STATUS_02)){//新增log表
				TtPtOrderLogPO tt = new TtPtOrderLogPO();
				tt.setLogId(Long.parseLong(SequenceManager.getSequence("")));
				tt.setOrderId(po.getOrderId());
				//tt.setOgrId(po.getOperationId());
				tt.setOgrId(Long.parseLong(logonUser.getDealerId()));
				tt.setOrgType(po.getHighOperationType());
				tt.setUserId(po.getUpdateBy());
				tt.setNodeStatus(po.getOrderStatus());
				tt.setOpertateDate(new Date());
				tt.setOrgType(Constant.ORG_TYPE_DEALER);
				tt.setIsInterface(Integer.parseInt("0"));
				tt.setCreateBy(po.getCreateBy());
				tt.setCreateDate(new Date());
				dao.insert(tt);
			}
			//从缓存取出子表数据
			saveOrderItem(po);
		} catch (ParseException e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.ADD_FAILURE_CODE,"配件采购订单");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/*
	 * 配件采购订单上报
	 */
	public void purchaseOrderForword(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		String dealerId = logonUser.getDealerId();
		try {
			List<Map<String, Object>> dcList = dao.getDClist(dealerId); //经销商相关联的供货方
			act.setOutData("dcList", dcList);
			act.setForword(orderForwordUrl);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"配件采购订单上报");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/*
	 * 分页查询待上报的配件订单信息
	 */
	public void queryPartOrderForword(){
		ActionContext act = ActionContext.getContext();
		RequestWrapper request = act.getRequest();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			String orderNo = CommonUtils.checkNull(request.getParamValue("orderNo"));//配件编号
			String beginTime = CommonUtils.checkNull(request.getParamValue("beginTime"));//开始时间
			String endTime = CommonUtils.checkNull(request.getParamValue("endTime"));//结束时间
			String dcId = CommonUtils.checkNull(request.getParamValue("dcId"));//供货方ID
			PartinfoBean bean = new PartinfoBean();
			bean.setOrderNo(orderNo);
			bean.setBeginTime(beginTime);
			bean.setEndTime(endTime);
			bean.setDcId(dcId);
			bean.setDealerId(logonUser.getDealerId());//获取当前经销商ID
			
			//分页方法 begin
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1; // 处理当前页	
			PageResult<Map<String, Object>> ps = dao.queryPartOrderForword(bean,curPage,Constant.PAGE_SIZE);
			//分页方法 end
			act.setOutData("ps", ps);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"配件采购订单上报");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/*
	 * 配件订单提报
	 * 返回0可以提报
	 * 返回1前台没有接到ID不可以提报
	 * 返回2订单最大行数超过规定不可以提报
	 * 返回3周期内上报次数过多不可以提报
	 * 返回4上报时间不在规定范围内不可以提报
	 */
	public void orderPartCheck(){
		ActionContext act = ActionContext.getContext();
		RequestWrapper request = act.getRequest();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			String orderId = CommonUtils.checkNull(request.getParamValue("orderId"));//配件编号
			String itemCount = CommonUtils.checkNull(request.getParamValue("itemCount"));//项数
			if(orderId.equals("")||itemCount.equals("")){
				act.setOutData("returnValue", 1);//没有得到ID和项数，错误信息，不可以上报
			}else{
				String dealerId = logonUser.getDealerId();//经销商ID
				Map<String, Object> map = dao.getOrderParam(dealerId);//经销商提报的规则
				long ct = System.currentTimeMillis();
				if(map != null && map.size() > 0){//有规则的时候
					if(Integer.parseInt(itemCount) > Integer.parseInt(String.valueOf(map.get("ORDER_MAX_LINES")))){
						act.setOutData("returnValue", 2);//订单最大行数超过规则最大行数，不可以上报
					}else{
						List<Map<String, Object>> dateList = dao.getPartDateList(String.valueOf(map.get("PARAM_ID"))); //规则内的上报时间
						int cycleType = Integer.parseInt(String.valueOf(map.get("CYCLE_TYPE")));//周期类型
						List<Date> sts = new ArrayList<Date>();
						List<Date> ets = new ArrayList<Date>();
						boolean flag = false;
						if(dateList.size()>0){
							for (Map<String, Object> datel : dateList) {
								Date startDate = CycleToTimeUtil.getTime(cycleType, Integer.parseInt(String.valueOf(datel.get("START_DATE"))));
								Date endDate = CycleToTimeUtil.getTime(cycleType, Integer.parseInt(String.valueOf(datel.get("END_DATE"))));
								sts.add(startDate);//获取时间
								ets.add(endDate);//获取时间
								if (ct >= CycleToTimeUtil.getStartTime(startDate.getTime()) && ct <= CycleToTimeUtil.getEndTime(endDate.getTime())){
									flag = true;
								}
							}
							
							Collections.sort(sts);//排序
							Collections.sort(ets);//排序
							Date min = Collections.min(sts);//取最小值组里面的最小值
							Date max = Collections.max(ets);//取最大值组里面的最大值
							Integer count = dao.getDealerPartCount(dealerId,DateTimeUtil.parseDateToDate(min),DateTimeUtil.parseDateToDate(max));//查询经销商已提报的次数
							if(count >= Integer.parseInt(String.valueOf(map.get("ALLOW_SUBMIT_TIMES")))){
								act.setOutData("returnValue", 3);//周期内上报次数过多，不可以上报
							}else{//
								if(flag){
									editPartDate(orderId, ct, dealerId);//更改配件订单状态及更新提报时间
									act.setOutData("returnValue", 0);//可以提报
								}else{
									act.setOutData("returnValue", 4);//上报时间不在规则范围内， 不允许提报
								}
							}
						}else{//没有时间上的限制 可以提报
							editPartDate(orderId, ct, dealerId);//更改配件订单状态及更新提报时间
							act.setOutData("returnValue", 0);//可以提报
						}
					}
				}else{//没有规则 可以提报
					editPartDate(orderId, ct, dealerId);//更改配件订单状态及更新提报时间
					act.setOutData("returnValue", 0);//可以提报
				}
			}
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"配件采购订单上报");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/*
	 * 更改配件订单状态及更新提报时间
	 */
	public void editPartDate(String orderId, Long ct, String dealerId){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			TtPtOrderPO tp = new TtPtOrderPO();
			TtPtOrderPO po = new TtPtOrderPO();
			tp.setOrderId(new Long(orderId));
			po.setRaiseDate(new Date(ct));//更新提报时间
			po.setOrderStatus(Integer.parseInt(Constant.PART_ORDER_STATUS_03));//状态更改为已上报
			po.setRaiseTimes(dao.getRaiseTimes(orderId));//配件订单提报次数+1
			dao.update(tp, po);
			TtPtOrderLogPO to = new TtPtOrderLogPO();
			to.setLogId(Long.parseLong(SequenceManager.getSequence("")));
			to.setOrderId(Long.parseLong(orderId));
			to.setOrgType(Constant.ORG_TYPE_DEALER);
			to.setOgrId(Long.parseLong(dealerId));
			to.setUserId(logonUser.getUserId());
			to.setNodeStatus(Integer.parseInt(Constant.PART_ORDER_STATUS_03));
			to.setOpertateDate(new Date(ct));
			to.setIsInterface(Integer.parseInt("0"));
			to.setCreateBy(logonUser.getUserId());
			to.setCreateDate(new Date(ct));
			dao.insert(to);//新增审核流程
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.FAILURE_CODE,"配件订单提报");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/*
	 * 配件采购订单预审核
	 */
	public void purchaseOrderCarefully(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			String dealerId = logonUser.getDealerId();
			List<Map<String, Object>> dcList = dao.getDClist(dealerId); //经销商相关联的供货方
			act.setOutData("dcList", dcList);
			act.setForword(carefullyUrl);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.FAILURE_CODE,"配件订单提报");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/*
	 * 分页显示二级经销商提报的配件采购订单
	 */
	public void queryPartCarefully(){
		ActionContext act = ActionContext.getContext();
		RequestWrapper request = act.getRequest();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			String orderNo = CommonUtils.checkNull(request.getParamValue("orderNo"));//配件编号
			String beginTime = CommonUtils.checkNull(request.getParamValue("beginTime"));//开始时间
			String endTime = CommonUtils.checkNull(request.getParamValue("endTime"));//结束时间
			String dcId = CommonUtils.checkNull(request.getParamValue("dcId"));//供货方ID
			String dealerCode = CommonUtils.checkNull(request.getParamValue("dealerCode"));//二级经销商代码
			PartinfoBean bean = new PartinfoBean();//dao层的传参
			bean.setOrderNo(orderNo);
			bean.setBeginTime(beginTime);
			bean.setEndTime(endTime);
			bean.setDcId(dcId);
			bean.setDealerCode(dealerCode);
			bean.setDealerId(logonUser.getDealerId());//获取当前经销商ID
			
			//分页方法 begin
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1; // 处理当前页	
			PageResult<Map<String, Object>> ps = dao.queryPartCarefully(bean,curPage,Constant.PAGE_SIZE);
			//分页方法 end
			act.setOutData("ps", ps);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"配件采购订单上报");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/*
	 * 显示待审核的二级经销商提报的配件采购订单详细
	 */
	public void forwordOrderPart(){
		ActionContext act = ActionContext.getContext();
		RequestWrapper request = act.getRequest();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			String orderId = CommonUtils.checkNull(request.getParamValue("orderId"));//配件编号
			Map<String, Object> orderInfo = dao.getOrderPartInfo(orderId);//订单明细
			act.setOutData("orderInfo", orderInfo);
			act.setOutData("orderId", orderId);
			act.setForword(forwordOderPartUrl);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"配件采购订单上报");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	//分页显示待上报的订单中的配件信息
	public void forwordOrderList(){
		ActionContext act = ActionContext.getContext();
		RequestWrapper request = act.getRequest();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			String orderId = CommonUtils.checkNull(request.getParamValue("orderId"));//配件ID
			//分页方法 begin
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1; // 处理当前页	
			PageResult<Object> ps = dao.getOrderPartList(orderId,curPage,Constant.PAGE_SIZE);
			//分页方法 end
			act.setOutData("ps", ps);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"配件采购订单上报");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	//配件信息通过或驳回操作
	public void forwordPartStatus(){
		ActionContext act = ActionContext.getContext();
		RequestWrapper request = act.getRequest();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			String orderId = CommonUtils.checkNull(request.getParamValue("orderId"));//配件ID
			String status = CommonUtils.checkNull(request.getParamValue("status"));//通过或驳回的状态
			String remark = CommonUtils.checkNull(request.getParamValue("remark"));//操作意见 
			String partStatus = Constant.PART_ORDER_STATUS_04;
			if(status.equals("1")){
				partStatus = Constant.PART_ORDER_STATUS_05;
			}
			TtPtOrderPO tp = new TtPtOrderPO();
			TtPtOrderPO po = new TtPtOrderPO();
			Date addDate = new Date(System.currentTimeMillis());
			tp.setOrderId(Long.parseLong(orderId));//条件 ID
			po.setOrderStatus(Integer.parseInt(partStatus));//要更改的状态
			po.setUpdateBy(logonUser.getUserId());//更改人
			po.setUpdateDate(addDate);//更改时间
			dao.update(tp, po);
			
			TtPtOrderLogPO tl = new TtPtOrderLogPO();
			tl.setLogId(Long.parseLong(SequenceManager.getSequence("")));
			tl.setOrderId(Long.parseLong(orderId));
			tl.setOrgType(Constant.ORG_TYPE_DEALER);
			tl.setOgrId(Long.parseLong(logonUser.getDealerId()));
			tl.setCreateBy(logonUser.getUserId());
			tl.setCreateDate(addDate);
			tl.setIsInterface(Integer.parseInt("0"));
			tl.setOpertateDate(addDate);
			tl.setNodeStatus(Integer.parseInt(partStatus));
			tl.setOpertateRemark(remark);
			dao.insert(tl);
			purchaseOrderCarefully();
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"配件采购订单上报");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/*
	 * 采购订单签收
	 */
	public void purchaseOrderSign(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			act.setForword(orderSheetUrl);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"配件采购订单上报");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/*
	 * 采购订单签收列表
	 */
	public void queryDirsignList(){
		ActionContext act = ActionContext.getContext();
		RequestWrapper request = act.getRequest();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			String dealerId = logonUser.getDealerId();//经销商ID
			String beginTime = CommonUtils.checkNull(request.getParamValue("beginTime"));//采购开始日期
			String endTime = CommonUtils.checkNull(request.getParamValue("endTime"));//采购结束日期
			String orderNo = CommonUtils.checkNull(request.getParamValue("orderNo"));//采购单号
			String doNo = CommonUtils.checkNull(request.getParamValue("doNo"));//货运单号
			PartinfoBean bean = new PartinfoBean();//dao层的传参
			bean.setDealerId(dealerId);
			bean.setBeginTime(beginTime);
			bean.setEndTime(endTime);
			bean.setOrderNo(orderNo);
			bean.setDoNo(doNo);
			//分页方法 begin
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1; // 处理当前页	
			PageResult<Map<String, Object>> ps = dao.queryDirsignList(bean,curPage,Constant.PAGE_SIZE);
			//分页方法 end
			act.setOutData("ps", ps);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"配件采购订单签收");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/*
	 * 采购订单签收操作
	 */
	public void orderPartSign() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			//取选中的配件Id
			String orderNo = request.getParamValue("oNo");
			String doNo = request.getParamValue("dNo");
			String oid = request.getParamValue("oid");
			act.setOutData("orderNo", orderNo);
			act.setOutData("doNo", doNo);
			act.setOutData("orderId", oid);
			act.setForword(orderSignUrl);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"配件采购点单签收");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	//签收列表
	public void doNoOrdreList(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			DonoInfoSet.removePartInfoSet(logonUser.getUserId());//清空内存
			Set<PartinfoBean> partInfo = DonoInfoSet.getPartInfoSet(logonUser.getUserId());
			String doNo = request.getParamValue("doNo");
			List<Map<String, Object>> list = dao.getDoOrderList(doNo);
			if (null != list) {
				for(Map<String, Object> li:list){
					PartinfoBean bean = new PartinfoBean();
					bean.setPartId(String.valueOf(li.get("PART_ID")));
					bean.setPartCode(String.valueOf(li.get("PART_CODE")));
					bean.setPartName(String.valueOf(li.get("PART_NAME")));
					bean.setUnit(String.valueOf(li.get("UNIT")));
					bean.setOrderCount(String.valueOf(li.get("ORDER_COUNT")));
					bean.setDoCount(String.valueOf(li.get("DO_COUNT")));
					bean.setSignCount(String.valueOf(li.get("DO_COUNT")));
					partInfo.add(bean);
				}
			}
			DonoInfoSet.setPartInfoSet(logonUser.getUserId(), partInfo);
			act.setOutData("partInfoSet", partInfo);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"配件采购点单签收");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	//删除签收信息 暂时不用
	public void delPartInfoSet() {
		String partId = request.getParamValue("partId");
		Set<PartinfoBean> partInfo = DonoInfoSet.getPartInfoSet(logonUser.getUserId());
		Iterator<PartinfoBean> it = partInfo.iterator();
		while (it.hasNext()) {
			PartinfoBean bean = it.next();
			if (partId.equals(bean.getPartId())) {
				it.remove();
				break;
			}
		}
		DonoInfoSet.setPartInfoSet(logonUser.getUserId(), partInfo);
		act.setOutData("partInfoSet", partInfo);
		act.setForword(orderSignUrl);
	}
	
	//配件全表查询
	public void selPartAllList(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			act.setForword(partUtl);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"供货备件信息");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/*
	 * 分页查询配件明细
	 */
	
	public void queryPartAllList(){
		ActionContext act = ActionContext.getContext();
		RequestWrapper request = act.getRequest();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			String partCode = CommonUtils.checkNull(request.getParamValue("partCode"));//配件代码
			String partName = CommonUtils.checkNull(request.getParamValue("partName"));//配件名称
			PartinfoBean bean = new PartinfoBean();
			bean.setPartCode(partCode);
			bean.setPartName(partName);
			PartorderDao dao = new PartorderDao();
			//分页方法 begin
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1; // 处理当前页	
			PageResult<Map<String, Object>> ps = dao.queryPartAllList(bean,curPage,Constant.PAGE_SIZE);
			//分页方法 end
			act.setOutData("ps", ps);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"供货备件信息");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	//新增货运单里没有的配件
	public void addDoNoNullPart(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			Set<PartinfoBean> partInfo = DonoInfoSet.getPartInfoSet(logonUser.getUserId());
			String[] partIds = request.getParamValues("partIds");
			String orderId = request.getParamValue("orderId");
			PartinfoDao partinfoDao = new PartinfoDao();
			for (String partId : partIds) {
				Map<String, Object> part = partinfoDao.queryPartDetailList(partId, orderId);//获取配件内容
				if (null != part) {
					PartinfoBean bean = new PartinfoBean();
					bean.setPartId(CommonUtils.checkNull(part.get("PART_ID")));//配件ID
					bean.setPartCode(CommonUtils.checkNull(part.get("PART_CODE")));//配件代码
					bean.setPartName(CommonUtils.checkNull(part.get("PART_NAME")));//配件名称
					bean.setUnit(CommonUtils.checkNull(part.get("UNIT")));//配件单位
					bean.setOrderCount(CommonUtils.checkNull(part.get("ORDER_COUNT")).equals("")?"0":CommonUtils.checkNull(part.get("ORDER_COUNT")));//订货数量
					bean.setDoCount("0");//货运数量
					bean.setSignCount("");//签收数量
					partInfo.add(bean);
				}
			}
			DonoInfoSet.setPartInfoSet(logonUser.getUserId(), partInfo);
			act.setOutData("partInfoSet", partInfo);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"配件采购点单签收");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	//在内存中存新增签收个数
	public void modifySignCount(){
		String partId = request.getParamValue("partId");
		String signCount = request.getParamValue("signCount");
		Set<PartinfoBean> partInfo = DonoInfoSet.getPartInfoSet(logonUser.getUserId());
		Iterator<PartinfoBean> it = partInfo.iterator();
		while (it.hasNext()) {
			PartinfoBean bean = it.next();
			if (partId.equals(bean.getPartId())) {
				bean.setSignCount(signCount);
				break;
			}
		}
		DonoInfoSet.setPartInfoSet(logonUser.getUserId(), partInfo);
		act.setOutData("partInfoSet", partInfo);
	}
	
	//签收货运单
	public void saveSign(){
		String dealerId = logonUser.getDealerId();
		String doNo = request.getParamValue("doNo");//货运单号(数据库中)
		String orderNo = request.getParamValue("orderNo");//订单号
		String truedoNo = request.getParamValue("truedoNo");//真实货运单号
//		String signDate = request.getParamValue("signDate");//签收时间
//		String signMan = request.getParamValue("signMan");//签收人
		String orderId = request.getParamValue("orderId");//订单ID
		try {
			TtPtDlrSignPO po = new TtPtDlrSignPO();
			po.setSignId(Long.parseLong(SequenceManager.getSequence("")));
			po.setDoNo(doNo);
			po.setOrderId(Long.parseLong(orderId));
			po.setSignNo(SequenceManager.getSequence(PTConstants.SIGN_NO));
			po.setDealerId(Long.parseLong(dealerId));
			po.setTransNo(truedoNo);
			Date signDate = new Date();
			po.setSignDate(signDate);
			po.setSignUserId(logonUser.getName());
			po.setStatus(Integer.parseInt(Constant.IS_SIGNED_01));
			po.setCreateBy(logonUser.getUserId());
			po.setCreateDate(new Date(System.currentTimeMillis()));
			dao.insert(po);//插入签收表
			//从缓存取出子表数据
			Set<PartinfoBean> partInfoSet = DonoInfoSet.getPartInfoSet(logonUser.getUserId());
			String[] signCount = request.getParamValues("signCount");
			Iterator<PartinfoBean> it = partInfoSet.iterator();
			List<String> partIds = new ArrayList<String>();
			while (it.hasNext()) {
				partIds.add(it.next().getPartId());
			}
			for (int i = 0; i < signCount.length; i++) {
				TtPtDlrSignDetailPO tt = new TtPtDlrSignDetailPO();
				tt.setDetailId(Long.parseLong(SequenceManager.getSequence("")));
				tt.setSignId(po.getSignId());
				tt.setPartId(Long.parseLong(partIds.get(i)));
				tt.setSignQuantity(Integer.parseInt(signCount[i]));
				tt.setCreateBy(po.getCreateBy());
				tt.setCreateDate(po.getCreateDate());
				dao.insert(tt);//插入签收子表
				TtPtOrditemPO ot = new TtPtOrditemPO();
				TtPtOrditemPO oo = new TtPtOrditemPO();
				ot.setOrderId(Long.parseLong(orderId));
				ot.setPartId(Long.parseLong(partIds.get(i)));
				oo.setReceivedCount(Integer.parseInt(signCount[i]));
			}
			
			TtPtShippingsheetPO ts = new TtPtShippingsheetPO();
			TtPtShippingsheetPO sp = new TtPtShippingsheetPO();
			ts.setDoNo(doNo);
			sp.setIsSigned(Integer.parseInt(Constant.IS_SIGNED_01));
			sp.setSignPerson(logonUser.getName());
			sp.setSignCount(partInfoSet.size());
			sp.setSignDate(signDate);
			sp.setUpdateBy(po.getCreateBy());
			sp.setUpdateDate(po.getUpdateDate());
			dao.update(ts, sp);//修改货运单表签收状态
			
			TtPtOrderPO to = new TtPtOrderPO();
			TtPtOrderPO or = new TtPtOrderPO();
			to.setOrderNo(orderNo);
			or.setOrderStatus(Integer.parseInt(Constant.PART_ORDER_STATUS_06));
			or.setUpdateBy(po.getUpdateBy());
			or.setUpdateDate(po.getUpdateDate());
			dao.update(to, or);//修改配件订单状态
			
			TtPtOrderLogPO tl = new TtPtOrderLogPO();
			tl.setLogId(Long.parseLong(SequenceManager.getSequence("")));
			tl.setOrderId(dao.getOrderId(orderNo));
			tl.setOrgType(Constant.ORG_TYPE_DEALER);
			tl.setOgrId(Long.parseLong(dealerId));
			tl.setUserId(po.getCreateBy());
			tl.setNodeStatus(Integer.parseInt(Constant.PART_ORDER_STATUS_06));
			tl.setOpertateDate(po.getCreateDate());
			tl.setCreateBy(po.getCreateBy());
			tl.setCreateDate(po.getCreateDate());
			dao.insert(tl);//插入订单审批流程表
			
			DonoInfoSet.removePartInfoSet(logonUser.getUserId());//清空缓存
			/**下发签收单**/
			OSC45 o = new OSC45();
			o.execute(po.getSignNo());
			/**下发结束**/
			act.setForword(orderSheetUrl);
			//调接口
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.ADD_FAILURE_CODE,"配件采购订单");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/*
	 * 签收明细
	 */
	public void doNoSignInfo(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			//取选中的配件Id
			String orderNo = request.getParamValue("oNo");
			String doNo = request.getParamValue("dNo");
			String orderId = request.getParamValue("oid");
			Map<String, Object> sign = dao.getDonoSignInfo(doNo);//货运单
			act.setOutData("orderNo", orderNo);
			act.setOutData("orderId", orderId);
			act.setOutData("sign", sign);
			act.setForword(signInfoUrl);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"配件采购点单签收");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/*
	 * 分页查询签收明细
	 */
	public void queryDonoSignInfo(){
		ActionContext act = ActionContext.getContext();
		RequestWrapper request = act.getRequest();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			String orderId = request.getParamValue("orderId");
			String orderNo = request.getParamValue("orderNo");
			String doNo = request.getParamValue("doNo");
			PartinfoBean bean = new PartinfoBean();
			bean.setOrderId(orderId);
			bean.setOrderNo(orderNo);
			bean.setDoNo(doNo);
			//分页方法 begin
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1; // 处理当前页	
			PageResult<Map<String, Object>> ps = sao.querySignInfoList(bean,curPage,Constant.PAGE_SIZE);
			//分页方法 end
			act.setOutData("ps", ps);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"供货备件信息");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
}
