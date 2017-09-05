package com.infodms.dms.actions.customerRelationships.complaintConsult;


import java.util.Date;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import com.infodms.dms.actions.customerRelationships.baseSetting.seatsSet;
import com.infodms.dms.actions.util.CommonUtilActions;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.dao.common.CommonUtilDao;
import com.infodms.dms.dao.customerRelationships.ComplaintAcceptDao;
import com.infodms.dms.dao.customerRelationships.ComplaintConsultDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TmOrgPO;
import com.infodms.dms.po.TtCrmComplaintDealRecordPO;
import com.infodms.dms.po.TtCrmComplaintPO;
import com.infodms.dms.po.TtCrmComplaintReturnRecordPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;
/**
 * 坐席回访投诉查询ACTIONS
 * @ClassName     : SeatsReturnComplaint 
 * @Description   : 坐席回访投诉查询ACTIONS
 * @author        : wangming
 * CreateDate     : 2013-7-07
 */
public class SeatsReturnComplaint {
	private static Logger logger = Logger.getLogger(SeatsReturnComplaint.class);
	//坐席回访投诉查询页面
	private final String SeatsReturnComplaint = "/jsp/customerRelationships/complaintConsult/seatsReturnComplaint.jsp";
	//待反馈状态页面
	private final String ComplaintWaitFeedBackUpdate = "/jsp/customerRelationships/complaintConsult/complaintWaitFeedBackUpdate.jsp";


	
	ActionContext act = ActionContext.getContext();
	AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
	RequestWrapper request = act.getRequest();
	//投诉受理初始化
	public void seatsReturnComplaintInit(){
		try{
			CommonUtilActions commonUtilActions = new CommonUtilActions();
			seatsSet seat = new seatsSet();
			//省份
			act.setOutData("proviceList", commonUtilActions.getProvice());
			//大区
			act.setOutData("tmOrgList", commonUtilActions.getTmOrgPO());
			//是否是管理员
			act.setOutData("isAdmin", seat.isAdmin(logonUser.getUserId()));
			
			act.setForword(SeatsReturnComplaint);
		}catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ACTION_NAME_ERROR_CODE,"坐席回访投诉查询");
			logger.error(logger,e1);
			act.setException(e1);
		}
	}
	
	public void querySeatsReturnComplaint(){
		act.getResponse().setContentType("application/json");
		try{
			
			String vin = CommonUtils.checkNull(request.getParamValue("vin"));  				
			String name = CommonUtils.checkNull(request.getParamValue("name"));  				
			String tele = CommonUtils.checkNull(request.getParamValue("tele"));  				
			String accuser = CommonUtils.checkNull(request.getParamValue("accuser"));  				
			String dateStart = CommonUtils.checkNull(request.getParamValue("dateStart"));  				
			String dateEnd = CommonUtils.checkNull(request.getParamValue("dateEnd"));  	
			String dealStart = CommonUtils.checkNull(request.getParamValue("dealStart"));  				
			String dealEnd = CommonUtils.checkNull(request.getParamValue("dealEnd"));  	
			String status = CommonUtils.checkNull(request.getParamValue("status"));  	
			
			String advancedSearch = CommonUtils.checkNull(request.getParamValue("advancedSearch"));  	
			String region = CommonUtils.checkNull(request.getParamValue("region"));  				
			String pro = CommonUtils.checkNull(request.getParamValue("pro"));  	
			
			
			ComplaintAcceptDao dao = ComplaintAcceptDao.getInstance();
			//分页方法 begin
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage"))	: 1; // 处理当前页	
				
			CommonUtilDao commonUtilDao = new CommonUtilDao();
			
/*			List<Map<String, Object>> orgDepart = commonUtilDao.getUserOrgForAdminDepart(logonUser.getUserId());	
			List<Map<String, Object>> org = commonUtilDao.getUserOrg(logonUser.getUserId());
			
			String orgid = "";
			for(Map map : orgDepart){
				if(orgid == ""){
					orgid = map.get("ORGID").toString();
				}else{
					orgid = orgid+"','"+map.get("ORGID").toString();
				}
			}
			for(Map map : org){
				if(orgid == ""){
					orgid = map.get("ORGID").toString();
				}else{
					orgid = orgid+"','"+map.get("ORGID").toString();
				}
			}*/
			//wizard_lee 针对待回访投诉查询，如果抱怨处理人是部门，TT_CRM_COMPLAINT中保存的CP_DEAL_USER为部门id，因此此处传入条件需要调整为部门id
			// select * from TM_ORG where org_id='2010010100070674'
			//问题：通过logonUser获取到的部门id是oem部门id，而客户关系的crm部门出现了分支，所属表和po均不同，建议将部门写死2013070519381899?
			//Long orgId = logonUser.getOrgId();
			String orgId = "2013070519381899";
			//PageResult<Map<String,Object>> complaintAcceptData = dao.queryComplaintInfo(vin,name,tele,accuser,dateStart,dateEnd,dealStart,dealEnd,logonUser.getUserId().toString(),advancedSearch,region,pro,status,Constant.PAGE_SIZE,curPage);
			PageResult<Map<String,Object>> complaintAcceptData = dao.queryComplaintInfo(vin,name,tele,accuser,dateStart,dateEnd,dealStart,dealEnd,orgId,advancedSearch,region,pro,status,Constant.PAGE_SIZE,curPage);
			
			act.setOutData("ps", complaintAcceptData);
		} catch(Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"待处理投诉查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	
	public void seatsReturnComplaintUpdate(){
		try{			
			String cpid = CommonUtils.checkNull(request.getParamValue("cpid"));  	
			String ctmid = CommonUtils.checkNull(request.getParamValue("ctmid"));  	
			String stauts = CommonUtils.checkNull(request.getParamValue("stauts"));  	
			
			ComplaintAcceptDao complaintAcceptDao = ComplaintAcceptDao.getInstance();
			ComplaintConsultDao dao = ComplaintConsultDao.getInstance();
			Map<String,Object> complaintConsult = dao.queryComplaintConsult(cpid,ctmid);
			act.setOutData("complaintConsult", complaintConsult);
			act.setOutData("dealRecordList", complaintAcceptDao.queryDealRecord(Long.parseLong(cpid)));
			act.setOutData("returnRecordList", complaintAcceptDao.queryReturnRecord(Long.parseLong(cpid)));
			act.setOutData("verifyRecordList", complaintAcceptDao.queryVerifyRecord(Long.parseLong(cpid)));
			CommonUtilActions commonUtilActions = new CommonUtilActions();
			List<TmOrgPO> tmOrgList = commonUtilActions.getTmOrgPO();
			//大区
			act.setOutData("tmOrgList", tmOrgList);
			List<TmOrgPO> smallTmOrgList = commonUtilActions.getSmallTmOrgPO();
			//小区
			act.setOutData("tmOrgSmallList", smallTmOrgList);
			//省份
			act.setOutData("proviceList", commonUtilActions.getProvice());
			//管理员所在部门
			act.setOutData("seatAdminDepartList", commonUtilActions.getSeatAdminDepartAc());
			//坐席
			//SeatsSetDao seatsSetDao = SeatsSetDao.getInstance();
			//act.setOutData("ttCrmSeatsList", seatsSetDao.queryTtCrmSeatsPOAll());
			//List<Map<String, Object>> confireList = commonUtilActions.getTcCode(Constant.CONFIRE_TYPE);
			//确认意见数据
			//List<Map<String, Object>> tempConfireList = new ArrayList<Map<String,Object>>();
			
			
			
			act.setForword(ComplaintWaitFeedBackUpdate);
			
			
		} catch(Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"待处理投诉查询处理");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	public void seatsReturnComplaintUpdateSubmit(){
		act.getResponse().setContentType("application/json");
		try{	
			//区别哪个页面操作
			String id = CommonUtils.checkNull(request.getParamValue("id"));  	
			//投诉咨询ID
			String cpid = CommonUtils.checkNull(request.getParamValue("cpid"));  	
			//确认意见
			//String confirmView = CommonUtils.checkNull(request.getParamValue("confirmView"));  	
			//处理反馈内容
			String cdContent = CommonUtils.checkNull(request.getParamValue("cdContent"));  	
			//客户反馈内容	
			String crContent = CommonUtils.checkNull(request.getParamValue("crContent"));  
			//回访结果
			String crResult = CommonUtils.checkNull(request.getParamValue("crResult"));  
			//完结理由
			String ccont = CommonUtils.checkNull(request.getParamValue("ccont"));  
			//处理人
			String dealuser = CommonUtils.checkNull(request.getParamValue("dealuser"));  
			//经销商人员
			String dealeruser = CommonUtils.checkNull(request.getParamValue("dealeruser"));  
			//坐席人员
			String ttCrmSeatsId = CommonUtils.checkNull(request.getParamValue("ttCrmSeatsId"));  
			//处理人所属大区
			String dealorg = CommonUtils.checkNull(request.getParamValue("orgObj"));
			
			ComplaintAcceptDao complaintAcceptDao = ComplaintAcceptDao.getInstance();
			CommonUtilActions commonUtilActions = new CommonUtilActions();
			
			//投诉咨询
			TtCrmComplaintPO ttCrmComplaintPO1 = new TtCrmComplaintPO();
			TtCrmComplaintPO ttCrmComplaintPO2 = new TtCrmComplaintPO();
			ttCrmComplaintPO1.setCpId(Long.parseLong(cpid));
			
			TtCrmComplaintPO ttCrmComplaintPO3 = (TtCrmComplaintPO)complaintAcceptDao.select(ttCrmComplaintPO1).get(0);
			
			ttCrmComplaintPO2.setUpdateBy(logonUser.getUserId());
			ttCrmComplaintPO2.setUpdateDate(new Date());
			
			//投诉咨询处理记录
			TtCrmComplaintDealRecordPO ttCrmComplaintDealRecordPO = new TtCrmComplaintDealRecordPO();
			ttCrmComplaintDealRecordPO.setCdDate(new Date());
			ttCrmComplaintDealRecordPO.setCdId(new Long(SequenceManager.getSequence("")));
			ttCrmComplaintDealRecordPO.setCdUser(logonUser.getName());
			ttCrmComplaintDealRecordPO.setCdUserId(logonUser.getUserId());
			ttCrmComplaintDealRecordPO.setCpId(Long.parseLong(cpid));
			ttCrmComplaintDealRecordPO.setCreateBy(logonUser.getUserId());
			ttCrmComplaintDealRecordPO.setCreateDate(new Date());
			//ttCrmComplaintDealRecordPO.setCdUserProcess(Integer.parseInt(confirmView));
			ttCrmComplaintDealRecordPO.setCpDealOrg(ttCrmComplaintPO3.getCpDealOrg());
			ttCrmComplaintDealRecordPO.setCpDealDealer(ttCrmComplaintPO3.getCpDealOrg());
			ttCrmComplaintDealRecordPO.setCpNextDealId(ttCrmComplaintPO3.getCpDealOrg());
			
			TtCrmComplaintReturnRecordPO ttCrmComplaintReturnRecordPO = new TtCrmComplaintReturnRecordPO();
			ttCrmComplaintReturnRecordPO.setCpId(Long.parseLong(cpid));
			//ttCrmComplaintReturnRecordPO.setCrConfirmOpinion(Integer.parseInt(confirmView));
			ttCrmComplaintReturnRecordPO.setCrDate(new Date());
			ttCrmComplaintReturnRecordPO.setCreateBy(logonUser.getUserId());
			ttCrmComplaintReturnRecordPO.setCreateDate(new Date());
			ttCrmComplaintReturnRecordPO.setCrId(new Long(SequenceManager.getSequence("")));;
			ttCrmComplaintReturnRecordPO.setCrUser(logonUser.getName());
			ttCrmComplaintReturnRecordPO.setCrUserId(logonUser.getUserId());
			ttCrmComplaintReturnRecordPO.setCpDealOrg(ttCrmComplaintPO3.getCpDealOrg());
			ttCrmComplaintReturnRecordPO.setCpDealDealer(ttCrmComplaintPO3.getCpDealOrg());
			
			//设置投诉状态为坐席回访已处理
			//如果该坐席需要转管理员处理
			if(!dealorg.equals(String.valueOf(Constant.CONFIRE_WAIT_TYPE))){
				ttCrmComplaintPO2.setCpStatus(Integer.parseInt(Constant.COMPLAINT_STATUS_WAIT_CLOSE));
				ttCrmComplaintPO2.setCpDealUser(Long.parseLong(dealorg));
				ttCrmComplaintPO2.setCpDealUserName(commonUtilActions.getDealUserName(Long.parseLong(dealorg)));
				ttCrmComplaintDealRecordPO.setCpNextDealId(Long.parseLong(dealorg));
				ttCrmComplaintDealRecordPO.setCpStatus(Integer.parseInt(Constant.COMPLAINT_STATUS_WAIT_CLOSE));
				ttCrmComplaintReturnRecordPO.setCpStatus(Integer.parseInt(Constant.COMPLAINT_STATUS_WAIT_CLOSE));
			}else{
				ttCrmComplaintDealRecordPO.setCpNextDealId(logonUser.getUserId());
				ttCrmComplaintDealRecordPO.setCpStatus(Integer.parseInt(Constant.COMPLAINT_STATUS_WAIT_FEEDBACK));
				ttCrmComplaintReturnRecordPO.setCpStatus(Integer.parseInt(Constant.COMPLAINT_STATUS_WAIT_FEEDBACK));
				ttCrmComplaintPO2.setCpDealUser(logonUser.getUserId());
				ttCrmComplaintPO2.setCpDealUserName(logonUser.getName());
			}
		
			ttCrmComplaintReturnRecordPO.setCrContent(crContent);
			complaintAcceptDao.update(ttCrmComplaintPO1, ttCrmComplaintPO2);
			complaintAcceptDao.insert(ttCrmComplaintDealRecordPO);
			complaintAcceptDao.insert(ttCrmComplaintReturnRecordPO);
			

			act.setOutData("success", "true");
		} catch(Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"待处理投诉查询处理");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	
}
