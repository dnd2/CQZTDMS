package com.infodms.dms.actions.partsmanage.partclaim;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.actions.partsmanage.common.PartClaimItemMemory;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.bean.PartClaimBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.constants.PTConstants;
import com.infodms.dms.dao.partinfo.PartClaimCheckDao;
import com.infodms.dms.dao.partinfo.PartClaimDao;
import com.infodms.dms.dao.partinfo.PartShippingDao;
import com.infodms.dms.dao.partinfo.PartShippingItemDao;
import com.infodms.dms.dao.partinfo.PartorderDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TtPtClaimCheckPO;
import com.infodms.dms.po.TtPtClaimPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;

public class PartClaimCheck implements PTConstants {
	public static final Logger logger = Logger.getLogger(PartClaimApply.class);
	private ActionContext act = ActionContext.getContext();
	private AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
	private RequestWrapper request = act.getRequest();
	private PartClaimDao partClaimDao = PartClaimDao.getInstance();
	private PartShippingItemDao partShippingItemDao = PartShippingItemDao.getInstance();
	private PartClaimCheckDao partClaimCheckDao = PartClaimCheckDao.getInstance();
	public void partClaimCheckInit() {
		try {
			act.setForword(PART_CLAIM_CHECK_QUERY);
		} catch(Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE,"配件基本信息初始化");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	public void partClaimCheckQuery() {
		Integer curPage = request.getParamValue("curPage") != null ?
				Integer.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页	
		PageResult<Map<String, Object>> ps = partClaimDao.partClaimCheckQuery(assembleBean(), curPage, Constant.PAGE_SIZE);
		act.setOutData("ps", ps);
	}
	
	public void partClaimDetail() {
		try {
			String claimId = request.getParamValue("claimId");
			if (!Utility.testString(claimId)) {
				throw new IllegalArgumentException("no claimId");
			}
			String claimNo = request.getParamValue("claimNo");
			PartClaimBean bean = new PartClaimBean();
			bean.setClaimId(Long.parseLong(claimId));
			Map<String, Object> ps = partClaimDao.partClaimDetail(bean);
			act.setOutData("ps", ps);
			act.setForword(PART_CLAIM_CHECK);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE,"配件基本信息初始化");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	public void partClaimQueryDetail() {
		try {
			String claimId = request.getParamValue("claimId");
			String signNo = request.getParamValue("signNo");
			if (!Utility.testString(claimId) || !Utility.testString(signNo)) {
				throw new IllegalArgumentException("no claimId or no signNo, claimId == " + claimId + ", signNo == " + signNo);
			}
			PartClaimBean bean = new PartClaimBean();
			bean.setClaimId(Long.parseLong(claimId));
			Map<String, Object> ps = partClaimDao.partClaimDetail(bean);
			act.setOutData("ps", ps);
			
			List<Map<String, Object>> lists = partClaimCheckDao.queryClaimCheckItems(Long.parseLong(claimId));
			act.setOutData("lists", lists);
			act.setOutData("signNo", signNo);
			act.setForword(PART_CLAIM_DETAIL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE,"配件基本信息初始化");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	public void partClaimItem() {
		String claimId = request.getParamValue("claimId");
		String signNo = request.getParamValue("signNo");
		//String orderId = request.getParamValue("orderId");
		if (!Utility.testString(claimId) || !Utility.testString(signNo)) {
			throw new IllegalArgumentException("no claimId or no signNo, claimId == " + claimId + ", singNo == " + signNo);
		}
		Integer curPage = request.getParamValue("curPage") != null ?
				Integer.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页	
		PageResult<Map<String, Object>> ps = partShippingItemDao.getClaimItem(signNo, claimId, curPage, Constant.PAGE_SIZE);
		act.setOutData("ps", ps);
	}
	
	public void partClaimCheck() {
		partClaimCheckDao.insert(assemblePO());
	}
	
	private TtPtClaimCheckPO assemblePO() {
		String claimId = request.getParamValue("claimId");
		String checkRemark = request.getParamValue("checkRemark");
		String checkFlag = CommonUtils.checkNull(request.getParamValue("checkFlag"));
		if (!Utility.testString(claimId)) {
			throw new IllegalArgumentException("No claimId.");
		}
		TtPtClaimCheckPO po = new TtPtClaimCheckPO();
		po.setCheckId(Long.parseLong(SequenceManager.getSequence("")));
		po.setClaimId(Long.parseLong(claimId));
		po.setOrgType(logonUser.getOrgType());
		po.setOrgId(logonUser.getOrgId());
		po.setUserId(logonUser.getUserId());
		po.setCheckDate(new Date());
		TtPtClaimPO oldPO = new TtPtClaimPO();
		oldPO.setClaimId(Long.parseLong(claimId));
		TtPtClaimPO newPO = new TtPtClaimPO();
		if ("1".equals(checkFlag)) {
			po.setCheckStatus(Constant.PART_CLAIM_STATUS_03);
			newPO.setStatus(Constant.PART_CLAIM_STATUS_03);
		} else {
			po.setCheckStatus(Constant.PART_CLAIM_STATUS_04);
			newPO.setStatus(Constant.PART_CLAIM_STATUS_04);
		}
		partClaimDao.update(oldPO, newPO);
		po.setCheckRemark(checkRemark);
		po.setCreateBy(logonUser.getUserId());
		po.setCreateDate(new Date());
		return po;
	}
	
	private PartClaimBean assembleBean() {
		PartClaimBean bean = new PartClaimBean();
		//经销商查询页面
		if (null != logonUser.getDealerId()) {
			bean.setDealerId(Long.parseLong(logonUser.getDealerId()));
		}
		//车厂查询页面
		bean.setDealerName(request.getParamValue("dealerName"));
		bean.setDealerCodes(request.getParamValue("dealerCode"));
		bean.setOrgCodes(request.getParamValue("orgCode"));
		bean.setClaimNo(request.getParamValue("claimNo"));
		bean.setDoNo(request.getParamValue("doNo"));
		bean.setBeginDate(request.getParamValue("beginDate"));
		bean.setEndDate(request.getParamValue("endDate"));
		bean.setCheckStatus(request.getParamValue("checkStatus"));
		return bean;
	}
}
