package com.infodms.dms.actions.partsmanage.partclaim;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.constants.PTConstants;
import com.infodms.dms.dao.partinfo.PartClaimCheckDao;
import com.infodms.dms.dao.partinfo.PartClaimDao;
import com.infodms.dms.dao.partinfo.PartShippingItemDao;
import com.infodms.dms.exception.BizException;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;

public class PartClaimQuery implements PTConstants {
	public static final Logger logger = Logger.getLogger(PartClaimQuery.class);
	private ActionContext act = ActionContext.getContext();
	private AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
	private RequestWrapper request = act.getRequest();
	private PartClaimDao partClaimDao = PartClaimDao.getInstance();
	private PartShippingItemDao partShippingItemDao = PartShippingItemDao.getInstance();
	private PartClaimCheckDao partClaimCheckDao = PartClaimCheckDao.getInstance();
	
	public void partClaimQueryInit() {
		try {
			act.setForword(PART_CLAIM_QUERY);
		} catch(Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE,"配件基本信息初始化");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	public void partClaimQueryDealerInit() {
		try {
			act.setForword(PART_CLAIM_DEALER_QUERY);
		} catch(Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE,"配件基本信息初始化");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
}
