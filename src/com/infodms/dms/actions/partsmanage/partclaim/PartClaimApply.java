package com.infodms.dms.actions.partsmanage.partclaim;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.infodms.dms.actions.partsmanage.common.PartClaimItemMemory;
import com.infodms.dms.actions.partsmanage.common.PartClaimItemSet;
import com.infodms.dms.actions.partsmanage.infoSearch.PartInfoSearch;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.bean.PartClaimBean;
import com.infodms.dms.bean.PartClaimItemBean;
import com.infodms.dms.bean.PartinfoBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.constants.PTConstants;
import com.infodms.dms.dao.partinfo.PartClaimDao;
import com.infodms.dms.dao.partinfo.PartClaimTypeDao;
import com.infodms.dms.dao.partinfo.PartShippingDao;
import com.infodms.dms.dao.partinfo.PartShippingItemDao;
import com.infodms.dms.dao.partinfo.PartinfoDao;
import com.infodms.dms.dao.partinfo.PartorderDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TtPtClaimItemPO;
import com.infodms.dms.po.TtPtClaimPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.StringUtil;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;

/**
 * 
* @ClassName: PartClaimApply 
* @Description: TODO(配件索赔申请) 
* @author liuqiang 
* @date Jun 13, 2010 10:34:15 AM 
*
 */
public class PartClaimApply implements PTConstants {
	
	public static final Logger logger = Logger.getLogger(PartClaimApply.class);
	private ActionContext act = ActionContext.getContext();
	private AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
	private RequestWrapper request = act.getRequest();
	private PartClaimDao partClaimDao = PartClaimDao.getInstance();
	private PartorderDao partOrderDao = new PartorderDao();
	private PartShippingDao partShippingDao = PartShippingDao.getInstance();
	private PartShippingItemDao partShippingItemDao = PartShippingItemDao.getInstance();
	private PartClaimItemMemory partClaimItemMemory = PartClaimItemMemory.getInstance();
	
	public void partClaimApplyInit() {
		try {
			act.setForword(INIT_URL);
		} catch(Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE,"配件基本信息初始化");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 
	* @Title: partClaimApplyQuery 
	* @Description: 根据条件查询货运单
	 */
	public void partClaimApplyQuery() {
		Integer curPage = request.getParamValue("curPage") != null ?
				Integer.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页	
		PageResult<Map<String, Object>> ps = partClaimDao.partClaimApplyQuery(assembleBean(), curPage, Constant.PAGE_SIZE);
		act.setOutData("ps", ps);
		act.setForword(INIT_URL);
	}
	
	/**
	 * 配件采购订单主表查询
	 */
//	public void partOrderDetail() {
//		try {
//			//采购订单编号
//			String orderId = CommonUtils.checkNull(request.getParamValue("signNo"));
//			//订单信息
//			Map<String, Object> orderInfo = partOrderDao.getOrderInfo(orderId);
//			act.setOutData("orderInfo", orderInfo);
//			//货运单编号
//			String doNo = CommonUtils.checkNull(request.getParamValue("doNo"));
//			//获取货运单信息
//			Map<String, Object> shippingInfo = partShippingDao.getShippingInfo(doNo);
//			act.setOutData("shippingInfo", shippingInfo);
//			//获取货运单明细
////			List<Map<String, Object>> shippingItems = partShippingItemDao.getShippingItem(doNo);
////			act.setOutData("ps", shippingItems);
//			
//			act.setForword(PART_CLAIM);
//		}catch(Exception e) {//异常方法
//			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"配件采购订单详细");
//			logger.error(logonUser,e1);
//			act.setException(e1);
//		}
//	}
	
	/**
	 * 签收明细查询
	 */
	public void partOrderDetail() {
		try {
			//采购订单编号
			String orderNo = request.getParamValue("orderNo");
			String doNo = request.getParamValue("doNo");
			String claimId = CommonUtils.checkNull(request.getParamValue("claimId"));
			if (!Utility.testString(orderNo) || !Utility.testString(doNo)) {
				throw new Exception("no orderNo == " + orderNo + " or doNo == " + doNo);
			}
			//获取货运单信息
			Map<String, Object> shippingInfo = partShippingDao.getShippingInfo(doNo, orderNo);
			act.setOutData("shippingInfo", shippingInfo);
			act.setOutData("orderNo", orderNo);
			act.setOutData("claimId", claimId);
			act.setForword(PART_CLAIM);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"配件采购订单详细");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	private PartClaimBean assembleBean() {
		PartClaimBean bean = new PartClaimBean();
		bean.setDealerId(Long.parseLong(logonUser.getDealerId()));
		bean.setOrderNo(request.getParamValue("orderNo"));
		bean.setDoNo(request.getParamValue("doNo"));
		bean.setSighNo(request.getParamValue("signNo"));
		bean.setBeginDate(request.getParamValue("beginDate"));
		bean.setEndDate(request.getParamValue("endDate"));
		return bean;
	}
	/*
	public void saveClaimOrder() {
		try {
			String claimId = CommonUtils.checkNull(request.getParamValue("claimId"));
			String orderId = CommonUtils.checkNull(request.getParamValue("orderId"));
			String doNo = CommonUtils.checkNull(request.getParamValue("doNo"));
			TtPtClaimPO po = assembleClaimPO();
			partClaimDao.insert(po);
			saveClaimItems("".equals(claimId) ? po.getClaimId() : Long.parseLong(claimId));
			List<String> list = new ArrayList<String>();
			list.add(orderId);
			list.add(doNo);
			act.setOutData("param", list);
			act.setForword(PART_CLAIM);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"配件采购订单详细");
			logger.error(logonUser,e1);
			act.setException(e1);
		}		
	}
	*/
	public void saveOrUpdateClaimOrder() {
		List<String> list = new ArrayList<String>();
		try {
			String claimId = CommonUtils.checkNull(request.getParamValue("claimId"));
			String orderId = CommonUtils.checkNull(request.getParamValue("orderId"));
			String doNo = CommonUtils.checkNull(request.getParamValue("doNo"));
			String flag = CommonUtils.checkNull(request.getParamValue("flag"));
			TtPtClaimPO oldPo = new TtPtClaimPO();
			TtPtClaimPO rpo = null;
			Map<String, Object> map = partOrderDao.getOrderInfo(orderId);
			if (!"".equals(claimId)) {
				oldPo.setClaimId(Long.parseLong(claimId));
				rpo = partClaimDao.queryClaimPO(oldPo);
			}
			if (null != rpo) {
				//更新操作
				if ("0".equals(flag)) {
					//保存操作
					TtPtClaimPO upo = new TtPtClaimPO();
					upo.setUpdateBy(logonUser.getUserId());
					upo.setUpdateDate(new Date());
					partClaimDao.update(rpo, upo);
					list.add(String.valueOf(rpo.getClaimId()));
				} else {
					//完成操作
					TtPtClaimPO upo = new TtPtClaimPO();
					upo.setStatus(Constant.PART_CLAIM_STATUS_02);
					upo.setApplyDate(new Date());
					upo.setApplyUserId(logonUser.getUserId());
					upo.setUpdateBy(logonUser.getUserId());
					upo.setUpdateDate(new Date());
					partClaimDao.update(rpo, upo);
					list.add(claimId);
					saveClaimItems("".equals(claimId) ? rpo.getClaimId() : Long.parseLong(claimId));
					return;
				}
			} else {
				rpo = assembleClaimPO();
				partClaimDao.insert(rpo);
				list.add(String.valueOf(rpo.getClaimId()));
			}
			saveClaimItems("".equals(claimId) ? rpo.getClaimId() : Long.parseLong(claimId));
			if (null == map || null == map.get("ORDER_NO")) {
				throw new Exception("No orderNo by orderId == " + orderId);
			}
			list.add(map.get("ORDER_NO").toString());
			list.add(doNo);
			act.setOutData("param", list);
			act.setForword(PART_CLAIM);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"配件采购订单详细");
			logger.error(logonUser,e1);
			act.setException(e1);
		}		
	}
	
	private void saveClaimItems(Long claimId) {
		Set<PartClaimItemBean> partClaimItemBeans = PartClaimItemSet.get(logonUser.getUserId());
		Iterator<PartClaimItemBean> it = partClaimItemBeans.iterator();
		TtPtClaimItemPO dpo = new TtPtClaimItemPO();
		dpo.setClaimId(claimId);
		partShippingItemDao.delete(dpo);
		while (it.hasNext()) {
			PartClaimItemBean bean = it.next();
			TtPtClaimItemPO po = assembleClaimItemPO(bean, claimId);
			partShippingItemDao.insert(po);
		}
	}
	
	private TtPtClaimPO assembleClaimPO() {
		TtPtClaimPO po = new TtPtClaimPO();
		po.setClaimId(Long.parseLong(SequenceManager.getSequence("")));
		po.setClaimNo(SequenceManager.getSequence(CLAIM_NO));
		po.setOrderId(Long.parseLong(request.getParamValue("orderId")));
		po.setDealerId(Long.parseLong(logonUser.getDealerId()));
		po.setDoNo(CommonUtils.checkNull(request.getParamValue("doNo")));
		po.setCreateBy(logonUser.getUserId());
		po.setCreateDate(new Date());
		//po.setIsDel(Integer.parseInt(Constant.IS_DEL_00));
		return po;
	}
	
	private TtPtClaimItemPO assembleClaimItemPO(PartClaimItemBean bean, Long claimId) {
		TtPtClaimItemPO po = new TtPtClaimItemPO();
		po.setItemId(Long.parseLong(SequenceManager.getSequence("")));
		po.setClaimId(claimId);
		po.setClaimCount(Integer.parseInt(bean.getClaimCount()));
		po.setPartId(Long.parseLong(bean.getPartId()));
		po.setClaimTypeId(Long.parseLong(bean.getClaimType()));
		po.setRemark(bean.getRemark());
		po.setCreateBy(logonUser.getUserId());
		po.setCreateDate(new Date());
		return po;
	}
	
}
