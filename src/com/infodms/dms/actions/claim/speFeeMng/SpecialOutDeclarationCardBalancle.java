/**********************************************************************
* <pre>
* FILE : SpecialOutDeclarationCardBalancle.java
* CLASS : SpecialOutDeclarationCardBalancle
*
* AUTHOR : PGM
*
* FUNCTION :特殊外出费用结算单.
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
 * $Id: SpecialOutDeclarationCardBalancle.java,v 1.1 2010/08/16 01:44:20 yuch Exp $
 */
package com.infodms.dms.actions.claim.speFeeMng;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import com.infodms.dms.actions.claim.serviceActivity.BaseImport;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.bean.TtAsWrSpeoutfeeBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.getCompanyId.GetOemcompanyId;
import com.infodms.dms.dao.claim.speFeeMng.OutFeeApplyManagerDao;
import com.infodms.dms.dao.claim.speFeeMng.SpecialOutDeclarationCardBalancleDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.FsFileuploadPO;
import com.infodms.dms.po.TtAsWrSpeoutfeeAuditingPO;
import com.infodms.dms.po.TtAsWrSpeoutfeePO;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;
/**
 * Function       :  特殊外出费用结算单
 * @author        :  PGM
 * CreateDate     :  2010-07-15
 * @version       :  0.1
 */
public class SpecialOutDeclarationCardBalancle extends BaseImport{
	private Logger logger = Logger.getLogger(SpecialOutDeclarationCardBalancle.class);
	private SpecialOutDeclarationCardBalancleDao dao = SpecialOutDeclarationCardBalancleDao.getInstance();
	private OutFeeApplyManagerDao ManagerDao = OutFeeApplyManagerDao.getInstance();
	private ActionContext act = ActionContext.getContext();//获取ActionContext
	private AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
	private final String SpecialBalancleInitUrl = "/jsp/claim/speFeeMng/specialOutDeclarationCardBalancle.jsp";//查询页面
	private final String SpecialBalancleUpdateQueryInitUrl= "/jsp/claim/speFeeMng/specialOutDeclarationCardDo.jsp";//结算页面
	
	
	/**
	 * Function       :  特殊外出费用结算单页面初始化
	 * @param         :  
	 * @return        :  claimRuleInit
	 * @throws        :  Exception
	 * LastUpdate     :  2010-07-15
	 */
	public void SpecialBalancleInit(){
		try {
			act.setForword(SpecialBalancleInitUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"特殊外出费用结算单");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * Function       :  根据条件查询特殊外出费用结算单中符合条件的信息，其中包括：
	 * @param         :  request-费用单据编码、巡航单据编码、经销商代码、经销商名称、费用单据上报时间
	 * @return        :  特殊外出费用结算单
	 * @throws        :  Exception
	 * LastUpdate     :  2010-07-15
	 */
	public void SpecialBalancleQuery(){
		try {
			RequestWrapper request = act.getRequest();
			String feeNo = request.getParamValue("feeNo");              //费用单据编码
			String crNo = request.getParamValue("crNo");                //巡航单据编码
			String dealerCode = request.getParamValue("dealerCode");    //经销商代码
			String dealerName = request.getParamValue("dealerName");    //经销商名称
			String startDate = request.getParamValue("startDate");      //费用单据上报开始时间
			String endDate = request.getParamValue("endDate");          //费用单据上报结束时间
			Long companyId=GetOemcompanyId.getOemCompanyId(logonUser);  //公司ID
			TtAsWrSpeoutfeeBean feeBean =new TtAsWrSpeoutfeeBean();
			feeBean.setFeeNo(feeNo);
			feeBean.setCrNo(crNo);
			feeBean.setDealerCode(dealerCode);
			feeBean.setDealerName(dealerName);
		//	SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd");
			if(!"".equals(startDate)&&null!=startDate){
				feeBean.setStartDate(startDate);
			}
			if(!"".equals(endDate)&&null!=endDate){
				feeBean.setEndDate(endDate);
			}
			feeBean.setCompanyId(companyId);
			Integer curPage = request.getParamValue("curPage") !=null ? Integer.parseInt(request.getParamValue("curPage")) : 1;
			PageResult<Map<String, Object>> ps = dao.getSpecialBalancleQuery(feeBean,curPage,Constant.PAGE_SIZE );
			act.setOutData("ps", ps);
			act.setForword(SpecialBalancleInitUrl);
		}catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"特殊外出费用结算单查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * Function       :  根据特殊外出费用ID条件,查询特殊外出费用结算单中符合条件的信息，其中包括：
	 * @param         :  request-ID
	 * @return        :  特殊外出费用结算单
	 * @throws        :  Exception
	 * LastUpdate     :  2010-07-15
	 */
	public void SpecialBalancleUpdateQueryInit(){
		try {
			RequestWrapper request = act.getRequest();
			String id = request.getParamValue("id");              //ID
			TtAsWrSpeoutfeeBean feeBean =dao.SpecialBalancleUpdateQueryInit(id);
			List<TtAsWrSpeoutfeeBean> list=dao.SpecialBalancleUpdateQueryList(id);
			List<TtAsWrSpeoutfeeBean> listBean=dao.SpecialBalancleQueryList(id);
			//获得附件列表
			List<FsFileuploadPO> fileList=dao.queryAttachFileInfo(id);
			String productName=ManagerDao.getProducerName("CHANA");//调用方法获得，生产厂家
			String code_name=ManagerDao.getCodeName(Constant.SPE_OUTFEE_CHANNEL_01);
			request.setAttribute("productName", productName);
			request.setAttribute("feeBean", feeBean);
			request.setAttribute("list", list);
			request.setAttribute("listBean", listBean);
			act.setOutData("fileList", fileList);
			act.setOutData("code_name",code_name);
			act.setForword(SpecialBalancleUpdateQueryInitUrl);
	  }catch (Exception e) {
		BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"特殊外出费用结算单查询");
		logger.error(logonUser,e1);
		act.setException(e1);
	 }
   }
	/**
	 * Function       :  根据特殊外出费用ID条件,特殊外出费用结算单【审核】
	 * @param         :  request-ID
	 * @return        :  特殊外出费用结算单
	 * @throws        :  Exception
	 * LastUpdate     :  2010-07-15
	 */
	@SuppressWarnings("static-access")
	public void SpecialBalancle(){
		try {
			RequestWrapper request = act.getRequest();
			String flag = request.getParamValue("flag");//结算、驳回
			String id = request.getParamValue("id");              //ID
			String auditingOpinion = request.getParamValue("auditingOpinion");//审核意见
			//新增审核意见到TT_AS_WR_SPEOUTFEE_AUDITING表 开始
			TtAsWrSpeoutfeeAuditingPO   AuditingPO  =new TtAsWrSpeoutfeeAuditingPO();
			AuditingPO.setId(Long.parseLong(SequenceManager.getSequence("")));//ID
			AuditingPO.setFeeId(Long.parseLong(id));//特殊费用ID
			AuditingPO.setAuditingDate(new Date());//审批日期
			AuditingPO.setAuditingPerson(logonUser.getUserId());//审核人员
			AuditingPO.setPresonDept(logonUser.getOrgId());//人员部门
			AuditingPO.setAuditingOpinion(auditingOpinion);//审核意见
			AuditingPO.setCreateBy(logonUser.getUserId());
			AuditingPO.setCreateDate(new Date());
			AuditingPO.setUpdateBy(logonUser.getUserId());
			AuditingPO.setUpdateDate(new Date());
			
			//新增审核意见到TT_AS_WR_SPEOUTFEE_AUDITING表 新增
			
			//修改状态TT_AS_WR_SPEOUTFEE表 开始
			TtAsWrSpeoutfeePO feePO =new TtAsWrSpeoutfeePO();     //条件
			feePO.setId(Long.parseLong(id));
			TtAsWrSpeoutfeePO feePOContent =new TtAsWrSpeoutfeePO();
			feePOContent.setAuditingOpinion(auditingOpinion);
			if("05".equals(flag)){
				AuditingPO.setStatus(Constant.SPE_OUTFEE_STATUS_05);//已结算
				feePOContent.setStatus(Constant.SPE_OUTFEE_STATUS_05);//已结算
			}else{
				AuditingPO.setStatus(Constant.SPE_OUTFEE_STATUS_06);//已结算
				feePOContent.setStatus(Constant.SPE_OUTFEE_STATUS_06);//结算驳回
			}
		
			feePOContent.setUpdateBy(logonUser.getUserId());
			feePOContent.setUpdateDate(new Date());
			dao.SpecialBalancleInsert(AuditingPO);//调用新增方法，新增状态
		    dao.SpecialBalancle(feePO, feePOContent);//调用修改方法，修改状态
		    //修改状态TT_AS_WR_SPEOUTFEE表 结束
			act.setForword(SpecialBalancleInitUrl);
			act.setOutData("retCode","success");
			act.setOutData("flag",flag);
	  }catch (Exception e) {
		BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"特殊外出费用结算单查询");
		logger.error(logonUser,e1);
		act.setException(e1);
	 }
   }
	
}