package com.infodms.dms.actions.claim.preAuthorization;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.common.getCompanyId.GetOemcompanyId;
import com.infodms.dms.dao.claim.dealerClaimMng.ClaimBillMaintainDAO;
import com.infodms.dms.dao.claim.preAuthorization.OtherMaintainDAO;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TtAsWrForeapprovalotheritemExtPO;
import com.infodms.dms.po.TtAsWrForeapprovalotheritemPO;
import com.infodms.dms.po.TtAsWrOtherfeePO;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;

/**
 * 
* @ClassName: OtherMaintain 
* @Description: TODO(其他项目) 
* @author wangchao 
* @date Jun 21, 2010 3:00:58 PM 
*
 */
public class OtherMaintain {
	
	private Logger logger = Logger.getLogger(OtherMaintain.class);
	private final OtherMaintainDAO dao = OtherMaintainDAO.getInstance();
	private final ClaimBillMaintainDAO dao2 = ClaimBillMaintainDAO.getInstance(); 
	private final String OTHER_URL = "/jsp/claim/preAuthorization/otherMaintain.jsp";//主页面（查询）
	private final String OTHER_ADD_URL = "/jsp/claim/preAuthorization/otherMaintainAdd.jsp";//新增页面
	private final String CLAIM_LABOR_WATCH_ADD_URL = "/jsp/claim/preAuthorization/labourMaintainAdd.jsp";//新增页面
	private final String CLAIM_LABOR_MODEL_QUERY_URL ="/jsp/claim/authorization/claimLaborModelQuery.jsp";//工时选择页面
	private final String CLAIM_PART_WATCH_UPDATE_URL = "/jsp/claim/authorization/claimPartWatchModify.jsp";//修改页面
	/**
	 * 
	* @Title: partMaintainForward 
	* @Description: TODO(其他项目页面跳转) 
	* @param     设定文件 
	* @return void    返回类型 
	* @throws
	 */
	public void otherMaintainForward() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			
			act.setOutData("OTHERFEE",getOtherfeeStr());
			act.setForword(OTHER_URL);
			
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"监控配件维护");
			logger.error(logonUser,e1);
			act.setException(e1);
		}		
	}
	/**
	 * 
	* @Title: otherMaintainAddForward 
	* @Description: TODO(其他项目增加页面跳转) 
	* @param     设定文件 
	* @return void    返回类型 
	* @throws
	 */
	public void otherMaintainAddForward() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			
			act.setOutData("OTHERFEE",getOtherfeeStr1());
			act.setForword(OTHER_ADD_URL);
			
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"监控配件维护");
			logger.error(logonUser,e1);
			act.setException(e1);
		}		
	}
	/**
	 * 
	 * @Title: getOtherfeeStr
	 * @Description: TODO(取得其他费用下拉框)
	 * @param
	 * @param type
	 * @param
	 * @return 设定文件
	 * @return String 返回类型
	 * @throws
	 */
	public String getOtherfeeStr() {
		ActionContext ctx = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) ctx.getSession().get(
				Constant.LOGON_USER);
		Long companyId=GetOemcompanyId.getOemCompanyId(logonUser);     //公司ID
		List<TtAsWrOtherfeePO> seriesList = dao2.queryOtherFee(companyId);
		String retStr = "";
		retStr += "<option value=\'\'>-请选择-</option>";
		for (int i = 0; i < seriesList.size(); i++) {
			TtAsWrOtherfeePO bean = new TtAsWrOtherfeePO();
			bean = (TtAsWrOtherfeePO) seriesList.get(i);
			retStr += "<option value=\'" + bean.getFeeCode() + "\'" + "title=\'"
					+ bean.getFeeName() + "\'>" + bean.getFeeName()
					+ "</option>";
		}
		return retStr;
	}
	/**
	 * 
	* @Title: getOtherfeeStr1 
	* @Description: TODO(取得其他费用不可为空) 
	* @param @return    设定文件 
	* @return String    返回类型 
	* @throws
	 */
	public String getOtherfeeStr1() {
		ActionContext ctx = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) ctx.getSession().get(
				Constant.LOGON_USER);
		Long companyId=GetOemcompanyId.getOemCompanyId(logonUser);     //公司ID
		List<TtAsWrOtherfeePO> seriesList = dao2.queryOtherFee(companyId);
		String retStr = "";
		//retStr += "<option value=\'\'>-请选择-</option>";
		for (int i = 0; i < seriesList.size(); i++) {
			TtAsWrOtherfeePO bean = new TtAsWrOtherfeePO();
			bean = (TtAsWrOtherfeePO) seriesList.get(i);
			retStr += "<option value=\'" + bean.getFeeCode() + "\'" + "title=\'"
					+ bean.getFeeName() + "\'>" + bean.getFeeName()
					+ "</option>";
		}
		return retStr;
	}
	/**
	 * 
	* @Title: otherQuery 
	* @Description: TODO(查询其他项目) 
	* @param     设定文件 
	* @return void    返回类型 
	* @throws
	 */
	public void otherQuery() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		Long oemCompanyId = GetOemcompanyId.getOemCompanyId(logonUser);
		Map<String,String> map = new HashMap<String,String>();
		try {
			RequestWrapper request = act.getRequest();
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1; // 处理当前页
			String itemCode = request.getParamValue("ITEM_CODE");
			String itemDesc = request.getParamValue("ITEM_DESC");
			if(Utility.testString(itemCode)) {
				map.put("ITEM_CODE", itemCode);
			}
			if(Utility.testString(itemDesc)) {
				map.put("ITEM_DESC", itemDesc);
			}
			PageResult<TtAsWrForeapprovalotheritemExtPO> ps = dao.otherQuery(oemCompanyId,map, Constant.PAGE_SIZE, curPage);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "预授权其他项目维护");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 
	* @Title: otherMaintainAdd 
	* @Description: TODO(其他项目增加) 
	* @param     设定文件 
	* @return void    返回类型 
	* @throws
	 */
	@SuppressWarnings("unchecked")
	public void otherMaintainAdd() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		Long oemCompanyId = GetOemcompanyId.getOemCompanyId(logonUser);
		Date date = new Date();
		try {
			RequestWrapper request = act.getRequest();
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1; // 处理当前页
			String itemCode = request.getParamValue("ITEM_CODE");
			String itemDesc = request.getParamValue("ITEM_DESC");
			//modify by wjb at 2010-08-02 start 
			//判断是否存在以维护的其他项目
			TtAsWrForeapprovalotheritemPO selpo = new TtAsWrForeapprovalotheritemPO();
			selpo.setItemCode(itemCode);
			selpo.setOemCompanyId(oemCompanyId);
			List list = dao.select(selpo);
			if(list !=null && list.size() > 0){
				TtAsWrOtherfeePO po = new TtAsWrOtherfeePO();
				po.setOemCompanyId(oemCompanyId);
				po.setIsDel(0);
				po.setFeeCode(itemCode);
				List li = dao.select(po);
				po = (TtAsWrOtherfeePO)li.get(0);
				act.setOutData("existcode", po.getFeeName());
			}
			//modify by wjb at 2010-08-02 end
			else{
				TtAsWrForeapprovalotheritemPO t = new TtAsWrForeapprovalotheritemPO();
				t.setId(Utility.getLong(SequenceManager.getSequence("")));
				t.setItemCode(itemCode);
				t.setItemDesc(itemDesc);
				t.setCreateBy(logonUser.getUserId());
				t.setCreateDate(date);
				t.setOemCompanyId(oemCompanyId);
				dao.insert(t);
			}
			act.setOutData("success", true);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "预授权其他项目维护");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 
	* @Title: otherMaintainDel 
	* @Description: TODO(其他项目删除) 
	* @param     设定文件 
	* @return void    返回类型 
	* @throws
	 */
	public void otherMaintainDel() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1; // 处理当前页
			String id = request.getParamValue("ID");
			TtAsWrForeapprovalotheritemPO t = new TtAsWrForeapprovalotheritemPO();
			t.setId(Utility.getLong(id));
			dao.delete(t);
			act.setOutData("success", true);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "预授权其他项目维护");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 
	* @Title: otherMaintainCount 
	* @Description: TODO(其他项目结算) 
	* @param     设定文件 
	* @return void    返回类型 
	* @throws
	 */
	public void otherMaintainCount() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1; // 处理当前页
			String[] ids = request.getParamValues("ids");
			//操作
			for (int i =0 ;i<ids.length;i++) {
				System.out.println(ids[i]);
			}
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "预授权其他项目维护");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
}
