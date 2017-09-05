package com.infodms.dms.actions.claim.application;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.common.getCompanyId.GetOemcompanyId;
import com.infodms.dms.dao.claim.application.ClaimBillCountDAO;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TtAsWrApplicationExtPO;
import com.infodms.dms.po.TtAsWrApplicationPO;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;

/**
 * 
* @ClassName: ClaimBillCount 
* @Description: TODO(索赔申请单结算) 
* @author wangchao 
* @date Jun 18, 2010 3:28:22 PM 
*
 */
public class ClaimBillCount {
	private Logger logger = Logger.getLogger(ClaimBillCount.class);
	private final ClaimBillCountDAO dao = ClaimBillCountDAO.getInstance();
	private final String CLAIM_BILL_URL = "/jsp/claim/application/claimBillCount.jsp";// 主页面（查询）
	private final String CLAIM_BILL_DETAIL_URL = "/jsp/claim/application/claimBillStatusDetail.jsp";// 明细页面
	/**
	 * 
	* @Title: claimBillCountForward 
	* @Description: TODO(索赔结算查询跳转) 
	* @param     设定文件 
	* @return void    返回类型 
	* @throws
	 */
	public void claimBillCountForward() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			act.setForword(CLAIM_BILL_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔结算");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 
	* @Title: claimBillCountQuery 
	* @Description: TODO(索赔申请单结算查询) 
	* @param     设定文件 
	* @return void    返回类型 
	* @throws
	 */
	public void claimBillCountQuery () {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		Long companyId=GetOemcompanyId.getOemCompanyId(logonUser);    
		try {
			RequestWrapper request = act.getRequest();
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1; // 处理当前页
			String dealerCode = request.getParamValue("DEALER_CODE");
			String dealerName = request.getParamValue("DEALER_NAME");
			String claimType = request.getParamValue("CLAIM_TYPE");
			String endDate = request.getParamValue("CON_LAST_DAY");
			String yieldly = request.getParamValue("YIELDLY");
			
			Map<String ,String > map = new HashMap<String ,String >();
			map.put("dealerCode", dealerCode);
			map.put("dealerName", dealerName);
			map.put("claimType", claimType);
			map.put("endDate", endDate);
			map.put("companyId", companyId.toString());
			map.put("yieldly", yieldly);
			PageResult<TtAsWrApplicationExtPO> ps = dao.query(map, Constant.PAGE_SIZE, curPage);
			//List<TtAsWrApplicationPO> ps1 = dao.queryId(map); //查询要结算的索赔单ID
			
			//act.setOutData("ids", ps1);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔结算");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 
	* @Title: claimBillCount 
	* @Description: TODO(索赔申请单结算) 
	* @param     设定文件 
	* @return void    返回类型 
	* @throws
	 */
	public void claimBillCount() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1; // 处理当前页
			String[] dealerIds = request.getParamValues("dealerId");
			String[] fees = request.getParamValues("fee");
			String[] yieldly = request.getParamValues("yieldly");
			String ids = request.getParamValue("ids");
			if (Utility.testString(ids)) {
				String[] allIds = ids.split(",");
				dao.count(allIds,dealerIds,fees,yieldly,logonUser);
			}
			
			claimBillCountForward();
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔结算");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
}
