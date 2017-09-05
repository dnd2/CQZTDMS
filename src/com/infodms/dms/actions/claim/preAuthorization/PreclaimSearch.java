/**********************************************************************
* <pre>
* FILE : PreclaimSearch.java
* CLASS : PreclaimSearch
*
* AUTHOR : PGM
*
* FUNCTION :索赔预授权---索赔预授权审核作业.
*
*
*======================================================================
* CHANGE HISTORY LOG
*----------------------------------------------------------------------
* MOD. NO.| DATE     | NAME | REASON | CHANGE REQ.
*----------------------------------------------------------------------
*         |2010-06-22| PGM  | Created |
* DESCRIPTION:
* </pre>
***********************************************************************/
/**
 * $Id: PreclaimSearch.java,v 1.1 2010/08/16 01:43:57 yuch Exp $
 */
package com.infodms.dms.actions.claim.preAuthorization;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.actions.common.ClaimCommonAction;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.bean.PreclaimAuditBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.getCompanyId.GetOemcompanyId;
import com.infodms.dms.dao.claim.preAuthorization.PreclaimSearchDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.FsFileuploadPO;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;

/**
 * Function       :  索赔预授权---索赔预授权审核作业
 * @author        :  PGM
 * CreateDate     :  2010-06-22
 * @version       :  0.1
 */
public class PreclaimSearch {
	private Logger logger = Logger.getLogger(PreclaimSearch.class);
	private PreclaimSearchDao dao = PreclaimSearchDao.getInstance();
	private ActionContext act = ActionContext.getContext();//获取ActionContext
	private AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
	private final ClaimCommonAction claimCommon = ClaimCommonAction.getInstance();
	private final String preclaimSearchInitUrl = "/jsp/claim/preAuthorization/preclaimSearch.jsp";//查询页面
	private final String preclaimAuditDetialQueryInitUrl="/jsp/claim/preAuthorization/preclaimAuditDetial.jsp";//审核初始化页面
	private final String preclaimAuditCheckUrl="/jsp/claim/preAuthorization/serviceActivityPreclaimSuccess.jsp";//审核初始化页面
	
	/**
	 * Function       :  索赔预授权---索赔预授权审核作业页面初始化
	 * @param         :  
	 * @return        :  serviceActivityManageInit
	 * @throws        :  Exception
	 * LastUpdate     :  2010-06-22
	 */
	public void preclaimAuditInit(){
		try {
			act.setForword(preclaimSearchInitUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"索赔预授权---索赔预授权审核作业");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * Function       :  根据条件查询索赔预授权中符合条件的信息
	 * @param         :  request-经销商代码、经销商名称、申请开始日期、申请结束日期、预授权单号、维修工单号
	 * @return        :  索赔预授权
	 * @throws        :  Exception
	 * LastUpdate     :  2010-06-22
	 */
	public void preclaimAuditQuery(){
		try {
			RequestWrapper request = act.getRequest();
			String dealerCodes = request.getParamValue("dealerCode");        //经销商代码
			String dealerName = request.getParamValue("dealerName");        //经销商名称
			String startdate = request.getParamValue("startDate");          //申请开始日期
			String enddate = request.getParamValue("endDate");              //申请结束日期
			String foNo = request.getParamValue("foNo");              		//预授权单号
			String roNo = request.getParamValue("roNo");              		//维修工单号
			//modify at 2010-07-19 start
			Long companyId=GetOemcompanyId.getOemCompanyId(logonUser);     //公司ID
			//modify end
			Integer curPage = request.getParamValue("curPage") !=null ? Integer.parseInt(request.getParamValue("curPage")) : 1;
			PageResult<Map<String, Object>> ps = dao.getAllPreclaimAuditQuery(companyId,dealerCodes,dealerName,startdate,enddate,foNo,roNo,curPage,Constant.PAGE_SIZE );
			act.setOutData("ps", ps);
			act.setForword(preclaimSearchInitUrl);
		}catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"索赔预授权---索赔预授权审核作业查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * Function       :  索赔预授权---索赔预授权审核作业[审核]页面初始化
	 * @param         :  
	 * @return        :  serviceActivityManageInit
	 * @throws        :  Exception
	 * LastUpdate     :  2010-06-22
	 */
	public void preclaimAuditDetialQueryInit(){
		try {
			RequestWrapper request = act.getRequest();
			String id = request.getParamValue("id");        //预申请ID
			Map<String,Object> map = claimCommon.getUserMap(logonUser.getUserId());
			//modify at 2010-07-19 start
			Long companyId=GetOemcompanyId.getOemCompanyId(logonUser);     //公司ID
			//modify end
			PreclaimAuditBean auditBean =dao.preclaimAuditInfo(companyId,id);//[审核]基本信息查询
			List<PreclaimAuditBean> list=dao.preclaimAuditInfoList(id); //[审核]查询
			//取附件信息：
			List<FsFileuploadPO> attachLs = dao.queryAttById(id);// 取得附件
			request.setAttribute("userMap", map);
			request.setAttribute("auditBean", auditBean);
			request.setAttribute("list", list);
			request.setAttribute("id", id);
			act.setOutData("attachLs", attachLs);//附件列表
			act.setForword(preclaimAuditDetialQueryInitUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"索赔预授权---索赔预授权审核作业");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * Function       :  索赔预授权---索赔预授权审核作业[审核]--维护授权代码、备注
	 * @param         :  
	 * @return        :  serviceActivityManageInit
	 * @throws        :  Exception
	 * LastUpdate     :  2010-06-22
	 */
	@SuppressWarnings("static-access")
	public void preclaimAuditDetialAdd(){
		try {
			RequestWrapper request = act.getRequest();
			String id=request.getParamValue("id"); 
			String [] itemId=request.getParamValues("itemId");                  //申请项目ID
			String [] authCode = request.getParamValues("authCode");        //授权代码
			String [] status = request.getParamValues("status");            //审批意见
			dao.preclaimAuditDetialAdd(id,itemId, authCode, status);           //功能:调用修改方法;描述：修改授权代码、审核意见
			act.setOutData("success", "true");
			//act.setForword(preclaimSearchInitUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"索赔预授权---索赔预授权审核作业");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
  }
	/**
	 * Function       :  索赔预授权---审核成功页面
	 * @param         :  
	 * @return        :  serviceActivityManageInit
	 * @throws        :  Exception
	 * LastUpdate     :  2010-06-22
	 */
	public void preclaimAuditCheck(){
		try {
			act.setForword(preclaimAuditCheckUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"索赔预授权---索赔预授权审核作业");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
}