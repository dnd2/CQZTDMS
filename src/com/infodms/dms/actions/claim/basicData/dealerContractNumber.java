/**********************************************************************
* <pre>
* FILE : ClaimRule.java
* CLASS : ClaimRule
*
* AUTHOR : PGM
*
* FUNCTION :三包规则维护.
*
*
*======================================================================
* CHANGE HISTORY LOG
*----------------------------------------------------------------------
* MOD. NO.| DATE     | NAME | REASON | CHANGE REQ.
*----------------------------------------------------------------------
*         |2010-07-14| PGM  | Created |
* DESCRIPTION:
* </pre>
***********************************************************************/
/**
 * $Id: dealerContractNumber.java,v 1.1 2012/08/15 07:26:11 xiongc Exp $
 */
package com.infodms.dms.actions.claim.basicData;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import jxl.Cell;

import org.apache.log4j.Logger;

import com.infodms.dms.actions.claim.serviceActivity.BaseImport;
import com.infodms.dms.actions.sales.planmanage.PlanUtil.ExcelErrors;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.getCompanyId.GetOemcompanyId;
import com.infodms.dms.dao.claim.basicData.ClaimRuleDao;
import com.infodms.dms.dao.claim.basicData.dealerContractNumberDao;
import com.infodms.dms.dao.claim.oldPart.ClaimBackListDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TtAsWrRuleListPO;
import com.infodms.dms.po.TtAsWrRuleListTmpPO;
import com.infodms.dms.po.TtAsWrRulePO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;
import flex.messaging.io.ArrayList;

/**
 * Function       :  三包规则维护
 * @author        :  PGM
 * CreateDate     :  2010-07-14
 * @version       :  0.1
 */
public class dealerContractNumber extends BaseImport{
	private Logger logger = Logger.getLogger(ClaimRule.class);
	private dealerContractNumberDao dao = dealerContractNumberDao.getInstance();
	private ActionContext act = null;
	private AclUserBean logonUserBean = null;
	private RequestWrapper request=null;
	private final String queryContractUrl = "/jsp/claim/basicData/dealerContractIndex.jsp";
		public void  dealerContractNumberInit(){
			try {
				act=ActionContext.getContext();
				logonUserBean=(AclUserBean) act.getSession().get(Constant.LOGON_USER);
				act.setForword(queryContractUrl);
			} catch (Exception e) {
				BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, "合同号维护--初始化");
				logger.error(logonUserBean, e1);
				act.setException(e1);
			}
		}
		public void dealerContractNumberQuery(){
			
			try {
				RequestWrapper request = act.getRequest();
				String dealerId= request.getParamValue("dealer_id");
				String dealerName = request.getParamValue("dealer_name");
				String yieldly = request.getParamValue("YIELDLY");
				
			}catch (Exception e) {
				BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"三包规则维");
				logger.error(logger,e1);
				act.setException(e1);
			}
		}

	}
