package com.infodms.dms.actions.partsmanage.partclaim;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.infodms.dms.actions.partsmanage.common.PartClaimItemMemory;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.bean.PartClaimItemBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.constants.PTConstants;
import com.infodms.dms.dao.partinfo.PartClaimTypeDao;
import com.infodms.dms.dao.partinfo.PartShippingDao;
import com.infodms.dms.dao.partinfo.PartShippingItemDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;

public class PartShippingAction implements PTConstants {
	public static final Logger logger = Logger.getLogger(PartShippingAction.class);
	private ActionContext act = ActionContext.getContext();
	private AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
	private RequestWrapper request = act.getRequest();
	private PartShippingDao partShippingDao = PartShippingDao.getInstance();
	private PartShippingItemDao partShippingItemDao = PartShippingItemDao.getInstance();
	private PartClaimTypeDao partClaimTypeDao = PartClaimTypeDao.getInstance();
	private PartClaimItemMemory partClaimItemMemory = PartClaimItemMemory.getInstance();
	
	public void partShippingItems() {
		try {
			String signNo = request.getParamValue("signNo");
			if (!Utility.testString(signNo)) {
				throw new Exception("No signNo === " + signNo);
			}
			String claimId = CommonUtils.checkNull(request.getParamValue("claimId"));
			List<Map<String, Object>> shippingItems = partShippingItemDao.getSignDetail(signNo, claimId);
			Set<PartClaimItemBean> partClaimItems = partClaimItemMemory.setPartClaimItem(shippingItems);
			act.setOutData("ps", partClaimItems);
			
			List<Map<String, Object>> partClaimType = partClaimTypeDao.getClaimType();
			act.setOutData("pcts", partClaimType);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"索赔单明细");
			logger.error(logonUser,e1);
			act.setException(e1);
		}

	}
	
	public void addPartShippingItems() {
		//Set<PartClaimItemBean> partClaimItems = partClaimItemMemory.addPartClaimItem();
	}
}
