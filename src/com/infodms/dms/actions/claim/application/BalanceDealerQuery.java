package com.infodms.dms.actions.claim.application;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.getCompanyId.GetOemcompanyId;
import com.infodms.dms.dao.claim.application.BalanceQueryDAO;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TtAsWrBalanceExtPO;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;

/**
 * 
* @ClassName: BalanceDealerQuery 
* @Description: TODO(结算清单查询经销商端) 
* @author wangchao 
* @date Jun 23, 2010 3:09:49 PM 
*
 */
public class BalanceDealerQuery {
	
	private Logger logger = Logger.getLogger(BalanceDealerQuery.class);
	private final BalanceQueryDAO dao = BalanceQueryDAO.getInstance();
	private final String BALANCE_QUERY = "/jsp/claim/application/balanceDealerQuery.jsp";// 主页面（查询）
	private final String CLAIM_BILL_DETAIL_URL = "/jsp/claim/application/claimBillStatusDetail.jsp";// 明细页面
	/**
	 * 
	* @Title: claimBillForward 
	* @Description: TODO(索赔结算查询跳转) 
	* @param     设定文件 
	* @return void    返回类型 
	* @throws
	 */
	public void balanceQueryForward() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			act.setForword(BALANCE_QUERY);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "结算清单查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 
	* @Title: balanceQuery 
	* @Description: TODO(结算清单查询) 
	* @param     设定文件 
	* @return void    返回类型 
	* @throws
	 */
	public void balanceQuery () {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		String dealerId = logonUser.getDealerId();
		Long companyId=GetOemcompanyId.getOemCompanyId(logonUser);    
		try {
			RequestWrapper request = act.getRequest();
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1; // 处理当前页
			String balanceNo = request.getParamValue("BALANCE_NO");
			String balanceDateStr = request.getParamValue("CON_LAST_DAY");
			String balanceDateEnd = request.getParamValue("CON_LAST_DAY");
			String yieldly = request.getParamValue("YIELDLY");
			String reduction = request.getParamValue("REDUCTION_FLAG");
			
			Map<String ,String > map = new HashMap<String ,String >();
			map.put("dealerId", dealerId);
			map.put("balanceNo", balanceNo);
			map.put("balanceDateStr", balanceDateStr);
			map.put("balanceDateEnd", balanceDateEnd);
			map.put("companyId", companyId.toString());
			map.put("yieldly", yieldly);
			map.put("reduction", reduction);
			
			PageResult<TtAsWrBalanceExtPO> ps = dao.balanceQuery(map, Constant.PAGE_SIZE, curPage);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "结算清单查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
}
