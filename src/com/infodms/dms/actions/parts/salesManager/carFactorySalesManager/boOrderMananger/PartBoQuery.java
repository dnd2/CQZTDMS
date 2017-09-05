package com.infodms.dms.actions.parts.salesManager.carFactorySalesManager.boOrderMananger;

import org.apache.log4j.Logger;

import com.infodms.dms.actions.partsmanage.infoSearch.DealerDlrstockInfo;
import com.infodms.dms.actions.sales.planmanage.PlanUtil.BaseImport;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.constants.PTConstants;
import com.infodms.dms.dao.parts.purchaseManager.partPlanQuery.PartPlanQueryDao;
import com.infodms.dms.exception.BizException;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;

public class PartBoQuery extends BaseImport implements PTConstants  {
	public Logger logger = Logger.getLogger(DealerDlrstockInfo.class);
	/**
	 * 
	 * @Title      : PART_BO_QUERY
	 * @Description: BO单查询初始化
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-18
	 */
	public void partBoQueryInit(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			
			act.setForword(PART_BO_QUERY);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.SPECIAL_MEG,"BO单查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 
	 * @Title      : PART_BO_QUERY
	 * @Description: BO单查询
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-18
	 */
	public void queryBoOrder(){

		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			
			act.setForword(PART_BO_QUERY);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.SPECIAL_MEG,"BO单查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	
	}
}
