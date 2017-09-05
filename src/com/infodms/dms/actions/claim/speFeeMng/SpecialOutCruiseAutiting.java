/**********************************************************************
* <pre>
* FILE : SpecialOutCruiseAutiting.java
* CLASS : SpecialOutCruiseAutiting
*
* AUTHOR : PGM
*
* FUNCTION :巡航服务线路审核单.
*
*
*======================================================================
* CHANGE HISTORY LOG
*----------------------------------------------------------------------
* MOD. NO.| DATE     | NAME | REASON | CHANGE REQ.
*----------------------------------------------------------------------
*         |2010-07-15| PGM  | Created |
* DESCRIPTION:
* </pre>
***********************************************************************/
/**
 * $Id: SpecialOutCruiseAutiting.java,v 1.1 2010/08/16 01:44:20 yuch Exp $
 */
package com.infodms.dms.actions.claim.speFeeMng;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.bean.TtAsWrSpeoutfeeBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.common.getCompanyId.GetOemcompanyId;
import com.infodms.dms.dao.claim.speFeeMng.SpecialOutCruiseAutitingDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TtAsWrCruiseAuditingPO;
import com.infodms.dms.po.TtAsWrCruisePO;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;
/**
 * Function       :  巡航服务线路审核单
 * @author        :  PGM
 * CreateDate     :  2010-07-15
 * @version       :  0.1
 */
public class SpecialOutCruiseAutiting{
	private Logger logger = Logger.getLogger(SpecialOutCruiseAutiting.class);
	private SpecialOutCruiseAutitingDao dao = SpecialOutCruiseAutitingDao.getInstance();
	private ActionContext act = ActionContext.getContext();//获取ActionContext
	private AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
	private final String SpecialAutitingInitUrl = "/jsp/claim/speFeeMng/specialOutCruiseAutiting.jsp";//查询页面
	private final String SpecialAutitingUpdateQueryInitUrl= "/jsp/claim/speFeeMng/specialOutCruiseBusinessDo.jsp";//结算页面
	
	
	/**
	 * Function       :  巡航服务线路审核单页面初始化
	 * @param         :  
	 * @return        :  SpecialAutitingInit
	 * @throws        :  Exception
	 * LastUpdate     :  2010-07-15
	 */
	public void SpecialAutitingInit(){
		try {
			act.setForword(SpecialAutitingInitUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"巡航服务线路审核单");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * Function       :  根据条件查询巡航服务线路审核单中符合条件的信息，其中包括：
	 * @param         :  request-费用单据编码、巡航单据编码、经销商代码、经销商名称、费用单据上报时间
	 * @return        :  巡航服务线路审核单
	 * @throws        :  Exception
	 * LastUpdate     :  2010-07-15
	 */
	public void SpecialAutitingQuery(){
		try {
			RequestWrapper request = act.getRequest();
			StringBuffer sb = new StringBuffer();
			List<Object> params = new LinkedList<Object>();
			String crNo = request.getParamValue("crNo");                //巡航单据编码
			String crWhither = request.getParamValue("crWhither");      //巡航目的地
			String dealerCode = request.getParamValue("dealerCode");    //经销商代码
			String dealerName = request.getParamValue("dealerName");    //经销商名称
			String startDate = request.getParamValue("startDate");      //费用单据上报开始时间
			String endDate = request.getParamValue("endDate");          //费用单据上报结束时间
			Long companyId=GetOemcompanyId.getOemCompanyId(logonUser);  //公司ID
			TtAsWrSpeoutfeeBean feeBean =new TtAsWrSpeoutfeeBean();
			feeBean.setCrNo(crNo);
			feeBean.setCrWhither(crWhither);
			//feeBean.setDealerCode(dealerCode);
			feeBean.setDealerName(dealerName);
			//SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd");
			if(!"".equals(startDate)&&null!=startDate){
				feeBean.setStartDate(startDate);
			}
			if(!"".equals(endDate)&&null!=endDate){
				feeBean.setEndDate(endDate);
			}
			feeBean.setCompanyId(companyId);

			//拼sql的查询条件
			if (Utility.testString(dealerCode)) {
				sb.append(Utility.getConSqlByParamForEqual(dealerCode, params, "d", "DEALER_CODE"));
			}
			Integer curPage = request.getParamValue("curPage") !=null ? Integer.parseInt(request.getParamValue("curPage")) : 1;
			PageResult<Map<String, Object>> ps = dao.getSpecialBalancleQuery(feeBean,sb.toString(),params,curPage,Constant.PAGE_SIZE );
			act.setOutData("ps", ps);
			act.setForword(SpecialAutitingInitUrl);
		}catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"巡航服务线路审核单查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * Function       :  根据特殊外出费用ID条件,查询巡航服务线路审核单中符合条件的信息，其中包括：
	 * @param         :  request-ID
	 * @return        :  特殊外出费用结算单
	 * @throws        :  Exception
	 * LastUpdate     :  2010-07-15
	 */
	public void SpecialAutitingQueryInit(){
		try {
			RequestWrapper request = act.getRequest();
			String id = request.getParamValue("id");              //ID
			TtAsWrSpeoutfeeBean feeBean =dao.SpecialAutitingQueryInit(id);
			List<TtAsWrSpeoutfeeBean> list=dao.SpecialAutitingQueryList(id);
			request.setAttribute("feeBean", feeBean);
			request.setAttribute("list", list);
			act.setForword(SpecialAutitingUpdateQueryInitUrl);
	  }catch (Exception e) {
		BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"巡航服务线路审核单查询");
		logger.error(logonUser,e1);
		act.setException(e1);
	 }
   }
	/**
	 * Function       :  根据特殊外出费用ID条件,巡航服务线路审核单【审核】
	 * @param         :  request-ID
	 * @return        :  巡航服务线路审核单
	 * @throws        :  Exception
	 * LastUpdate     :  2010-07-15
	 */
	@SuppressWarnings("static-access")
	public void SpecialAutiting(){
		try {
			RequestWrapper request = act.getRequest();
			String flag = request.getParamValue("flag");//已审核通过、批复退回、中止
			String crId = request.getParamValue("id");    //巡航ID
			String auditingOpinion = request.getParamValue("auditingOpinion");//审核意见
			//新增审核意见【TT_AS_WR_CRUISE_AUDITING】 开始
			TtAsWrCruiseAuditingPO AuditingPOContent =new TtAsWrCruiseAuditingPO();
			String id=SequenceManager.getSequence("");
			AuditingPOContent.setId(Long.parseLong(id));//序列ID
			AuditingPOContent.setCrId(Long.parseLong(crId));//巡航ID
			AuditingPOContent.setAuditingOpinion(auditingOpinion);
			AuditingPOContent.setAuditingDate(new Date());//审批日期
			AuditingPOContent.setAuditingPerson(logonUser.getUserId());//审核人员
			AuditingPOContent.setPresonDept(logonUser.getOrgId());//人员部门
			AuditingPOContent.setUpdateBy(logonUser.getUserId());
			AuditingPOContent.setUpdateDate(new Date());
			//新增审核意见【TT_AS_WR_CRUISE_AUDITING】 结束
			//修改主表【TT_AS_WR_CRUISE】中审核状态 开始
			 TtAsWrCruisePO CruisePO =new TtAsWrCruisePO();
			 CruisePO.setId(Long.parseLong(crId));//巡航ID
			 TtAsWrCruisePO CruisePOContent =new TtAsWrCruisePO();
			//修改主表【TT_AS_WR_CRUISE】中审核状态 结束
			if("04".equals(flag)){
				AuditingPOContent.setStatus(Constant.CURI_SERVICE_STATUS_04);//已审核通过
				CruisePOContent.setStatus(Constant.CURI_SERVICE_STATUS_04);//已审核通过
			}else if("08".equals(flag)){
				AuditingPOContent.setStatus(Constant.CURI_SERVICE_STATUS_03);//批复退回
				CruisePOContent.setStatus(Constant.CURI_SERVICE_STATUS_03);//批复退回
			}else{
				AuditingPOContent.setStatus(Constant.CURI_SERVICE_STATUS_05);//中止
				CruisePOContent.setStatus(Constant.CURI_SERVICE_STATUS_05);//中止
			}
			CruisePOContent.setUpdateBy(logonUser.getUserId());
			CruisePOContent.setUpdateDate(new Date());
		    dao.SpecialAutiting(AuditingPOContent);//操作巡航审核明细表
		    dao.SpecialAutitingUpdate(CruisePO,CruisePOContent);//操作巡航服务线路表
		    act.setOutData("retCode","success");
		    act.setOutData("flag",flag);
			act.setForword(SpecialAutitingInitUrl);
	  }catch (Exception e) {
		BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"巡航服务线路审核单查询");
		logger.error(logonUser,e1);
		act.setException(e1);
	 }
   }
	
}