package com.infodms.dms.actions.partsmanage.sales;

import org.apache.log4j.Logger;

import com.infodms.dms.actions.partsmanage.purchase.PurchaseOrderSearch;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.constants.PTConstants;
import com.infodms.dms.dao.partinfo.PartInfoItemDao;
import com.infodms.dms.dao.partinfo.PartorderDao;
import com.infodms.dms.exception.BizException;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;

public class PurchaseOrderStatusSearch implements PTConstants {
	public Logger logger = Logger.getLogger(PurchaseOrderStatusSearch.class);
	private ActionContext act = ActionContext.getContext();
	private AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
	private RequestWrapper request = act.getRequest();
	public PartInfoItemDao partInfoItemDao= PartInfoItemDao.getInstance();
	private final PartorderDao dao = (PartorderDao) PartorderDao.getInstance();
	
	public void purchaseOrderStatusSearchInit() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			act.setForword(PART_ORDER_QUERY);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"配件采购订单初始化");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}

}
