package com.infodms.dms.actions.customerRelationships.reviewmanage;

import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import org.apache.log4j.Logger;

import com.infodms.dms.actions.util.CommonUtilActions;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.dao.customerRelationships.ReviewManageDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TtCrmReturnVisitPO;
import com.infodms.dms.po.TtCrmReturnVisitRecordPO;
import com.infodms.dms.po.TtCrmSeatsPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.XHBUtil;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;
import com.infoservice.po3.bean.PageResult;

public class ReviewManage {

	
	private static Logger logger = Logger.getLogger(ReviewManage.class);
	private static final String myClientReviewInit="/jsp/customerRelationships/reviewmanage/myClientReviewInit.jsp";
	private static final String pendingReviewInit="/jsp/customerRelationships/reviewmanage/pendingReviewInit.jsp";
	private static final String reviewSearchInit="/jsp/customerRelationships/reviewmanage/reviewSearchInit.jsp";
	private static final String patchSetQuestionnaireInit="/jsp/customerRelationships/reviewmanage/patchSetQuestionnaireInit.jsp";
	private static final String allocateReviewJobInit="/jsp/customerRelationships/reviewmanage/allocateReviewJobInit.jsp";
	private static final String querySet="/jsp/customerRelationships/reviewmanage/querySet.jsp";
	private static final String chioseSet="/jsp/customerRelationships/reviewmanage/choiseSet.jsp";
	private static final String examManageInit="/jsp/customerRelationships/reviewmanage/examManageInit.jsp";
	private static final String questionManageInit="/jsp/customerRelationships/reviewmanage/questionManageInit.jsp";
	private static final String showQuestion="/jsp/customerRelationships/reviewmanage/showQuestion.jsp";
	private static final String createReviewInit="/jsp/customerRelationships/reviewmanage/createReviewInit.jsp";
	private static final String addReviewInit="/jsp/customerRelationships/reviewmanage/addReviewInit.jsp";
	private static final String satisfactionReviewInit="/jsp/customerRelationships/reviewmanage/satisfactionReviewInit.jsp";
	private static final String reviewQuestionnairExportInit="/jsp/customerRelationships/reviewmanage/reviewQuestionnairExportInit.jsp";
	private static final String reviewScheduleSearchInit="/jsp/customerRelationships/reviewmanage/reviewScheduleSearchInit.jsp";
	private static final String seeReview="/jsp/customerRelationships/reviewmanage/seeReview.jsp";
	private static final String review="/jsp/customerRelationships/reviewmanage/review.jsp";
	private static final String pendreview="/jsp/customerRelationships/reviewmanage/pendreview.jsp";
	private static final String reviewSearchreview="/jsp/customerRelationships/reviewmanage/reviewSearchreview.jsp";
	private static final String updateReview="/jsp/customerRelationships/reviewmanage/updateReview.jsp";
	
	private static final String seeAllocateReview="/jsp/customerRelationships/reviewmanage/seeAllocateReview.jsp";
	private static final String seeSetQuestionairReview="/jsp/customerRelationships/reviewmanage/seeSetQuestionairReview.jsp";
	private static final String examAddInit="/jsp/customerRelationships/reviewmanage/examAddInit.jsp";
	private static final String editExamInit="/jsp/customerRelationships/reviewmanage/editExamInit.jsp";
	private static final String seeExam="/jsp/customerRelationships/reviewmanage/seeExam.jsp";
	private static final String addQuestionInit="/jsp/customerRelationships/reviewmanage/addQuestionInit.jsp";
	private static final String editQuestionInit="/jsp/customerRelationships/reviewmanage/editQuestionInit.jsp";
	private static final String editQuestionInit2="/jsp/customerRelationships/reviewmanage/editQuestionInit2.jsp";
	private static final String seeQuestionair="/jsp/customerRelationships/reviewmanage/seeQuestionair.jsp";
	private static final String addQuestionairInit="/jsp/customerRelationships/reviewmanage/addQuestionairInit.jsp";
	private static final String editQuestionairInit="/jsp/customerRelationships/reviewmanage/editQuestionairInit.jsp";
	private static final String addQuestionInit2="/jsp/customerRelationships/reviewmanage/addQuestionInit2.jsp";
	private static final String modifyQuestExport="/jsp/customerRelationships/reviewmanage/modifyQuestExport.jsp";
	
	private static final String hotSpotManage="/jsp/customerRelationships/reviewmanage/hotSpotManage.jsp";
	
	/**
	 * 
	 * @Title      : 
	 * @Description:  我的客户回访 初始页面
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-7
	 */
	public void myClientReviewInit()
	{
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		ReviewManageDao dao = ReviewManageDao.getInstance();
		try {
			List<Map<String, Object>> questionairList = dao.getQuestionairList() ;
			List<Map<String, Object>> statusList = dao.getStatusList() ;
			act.setOutData("questionairList", questionairList);
			act.setOutData("statusList", statusList);
			act.setForword(myClientReviewInit);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"我的客户回访");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 
	 * @Title      : 
	 * @Description: 我的客户回访查询 
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-7
	 */
	public void ReviewManageQuery()
	{
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		ReviewManageDao dao = ReviewManageDao.getInstance();
		try {
			String RV_CUS_NAME=CommonUtils.checkNull(request.getParamValue("RV_CUS_NAME"));
			String RV_TYPE=CommonUtils.checkNull(request.getParamValue("RV_TYPE"));
			String RV_STATUS=CommonUtils.checkNull(request.getParamValue("RV_STATUS"));
			String QR_ID=CommonUtils.checkNull(request.getParamValue("QR_ID"));
			
			int pageSize = 10 ;
			int curPage = request.getParamValue("curPage") != null ? 
					Integer.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
			PageResult<Map<String,Object>> ps = dao.reviewManageQuery( logonUser, pageSize, curPage,RV_CUS_NAME,RV_TYPE,RV_STATUS,QR_ID) ;
			act.setOutData("ps",ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"我的客户回访");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 
	 * @Title      : 
	 * @Description: 查看回访
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-7
	 */
	public void seeReview()
	{
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		ReviewManageDao dao = ReviewManageDao.getInstance();
		try {
			//获得回访ID
			String RV_ID=CommonUtils.checkNull(request.getParamValue("RV_ID"));
			String pagetype=CommonUtils.checkNull(request.getParamValue("pagetype"));
			Map<String,LinkedList<Map<String,Object>>> result = new HashMap();
			//根据ID查询基本信息
			LinkedList<Map<String,Object>> basicInof =dao.basicInofQuery(RV_ID, logonUser.getUserId());
			result.put("basicInof", basicInof);
			//根据ID查询问题信息及答案
			LinkedList<Map<String,Object>> questionInof =dao.questionInofQuery(RV_ID);
			String questionhtml=generateQuestion(questionInof);
			//根据ID查询回访历史
			LinkedList<Map<String,Object>> reviewInof =dao.reviewInofQuery(RV_ID);

			result.put("questionInof", questionInof);
			result.put("reviewInof", reviewInof);
			act.setOutData("result",result);
			act.setOutData("questionhtml", questionhtml);
			if(!("".equals(pagetype))&&(pagetype.equals("allocate"))){
				act.setForword(seeAllocateReview);
			}else if(!("".equals(pagetype))&&(pagetype.equals("questionair")))
			{
				act.setForword(seeSetQuestionairReview);
			}else 
			{
				act.setForword(seeReview);
			}
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"我的客户回访");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 
	 * @Title      : 
	 * @Description: 回访
	 * @param      : @return      
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-7
	 */
	public void Review()
	{
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		ReviewManageDao dao = ReviewManageDao.getInstance();
		try {
			//获得回访ID
			String RV_ID=CommonUtils.checkNull(request.getParamValue("RV_ID"));
			String page=CommonUtils.checkNull(request.getParamValue("page"));
			Map<String,LinkedList<Map<String,Object>>> result = new HashMap();
			//根据ID查询信息
			LinkedList<Map<String,Object>> basicInof =dao.basicInofQuery(RV_ID, logonUser.getUserId());
			result.put("basicInof", basicInof);
			//根据ID查询 问题及答案
			LinkedList<Map<String,Object>> questionInof =dao.questionInofQuery(RV_ID);
			String questionhtml=generateQuestion2(questionInof);
			//根据ID查询回访历史
			LinkedList<Map<String,Object>> reviewInof =dao.reviewInofQuery(RV_ID);
			result.put("reviewInof", reviewInof);
			
			//是否满意
			CommonUtilActions commonUtilActions = new CommonUtilActions();
			List<Map<String,Object>> isSatisList = commonUtilActions.getTcCode(Constant.GRADE_TYPE);
			List<Map<String,Object>> issatislist = new ArrayList<Map<String,Object>>();
			for(Map<String,Object> map : isSatisList){
				if(Constant.PLEASED.equals(map.get("CODE_ID").toString()) || Constant.YAWP.equals(map.get("CODE_ID").toString())){
					issatislist.add(map);
				}
			}
			act.setOutData("issatislist", issatislist);
			
			act.setOutData("questionhtml", questionhtml);
			act.setOutData("result",result);
			act.setOutData("RV_ID", RV_ID);
			act.setOutData("txtUid", logonUser.getActn());
			TtCrmSeatsPO seatsPO = new TtCrmSeatsPO();
			seatsPO.setSeSeatsNo(logonUser.getActn());
			List<TtCrmSeatsPO> seatList= dao.select(seatsPO);
			if(seatList.size() > 0)
			{
				act.setOutData("txtExt",seatList.get(0).getSeExt() );
			}
			act.setOutData("addr", logonUser.getAddr());
			if(!"".equals(page)&&page.equals("pend"))
			{
				act.setForword(pendreview);
			}else if(!"".equals(page)&&page.equals("search"))
			{
				act.setForword(reviewSearchreview);
			}else{
				act.setForword(review);
			}
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"我的客户回访");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 修改
	 */
	public void updateReview()
	{
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		ReviewManageDao dao = ReviewManageDao.getInstance();
		try {
			//获得回访ID
			String RV_ID=CommonUtils.checkNull(request.getParamValue("RV_ID"));
			Map<String,LinkedList<Map<String,Object>>> result = new HashMap();
			//根据ID查询信息
			LinkedList<Map<String,Object>> basicInof =dao.basicInofQuery(RV_ID, logonUser.getUserId());
			result.put("basicInof", basicInof);
			//根据ID查询 问题及答案
			LinkedList<Map<String,Object>> questionInof =dao.questionInofQuery(RV_ID);
			String questionhtml=generateQuestion2(questionInof);
			//根据ID查询回访历史
			LinkedList<Map<String,Object>> reviewInof =dao.reviewInofQuery(RV_ID);
			result.put("reviewInof", reviewInof);
			
			//是否满意
			CommonUtilActions commonUtilActions = new CommonUtilActions();
			List<Map<String,Object>> isSatisList = commonUtilActions.getTcCode(Constant.GRADE_TYPE);
			List<Map<String,Object>> issatislist = new ArrayList<Map<String,Object>>();
			for(Map<String,Object> map : isSatisList){
				if(Constant.PLEASED.equals(map.get("CODE_ID").toString()) || Constant.YAWP.equals(map.get("CODE_ID").toString())){
					issatislist.add(map);
				}
			}
			act.setOutData("issatislist", issatislist);
			
			act.setOutData("questionhtml", questionhtml);
			act.setOutData("result",result);
			act.setOutData("RV_ID", RV_ID);
			act.setOutData("txtUid", logonUser.getActn());
			TtCrmSeatsPO seatsPO = new TtCrmSeatsPO();
			seatsPO.setSeSeatsNo(logonUser.getActn());
			List<TtCrmSeatsPO> seatList= dao.select(seatsPO);
			if(seatList.size() > 0)
			{
				act.setOutData("txtExt",seatList.get(0).getSeExt() );
			}
			act.setOutData("addr", logonUser.getAddr());
			act.setForword(updateReview);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"我的客户回访");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 
	 * @Title      : 
	 * @Description: TODO  保存回访记录
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-10
	 */
	public void saveReview()
	{
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		ReviewManageDao dao = ReviewManageDao.getInstance();
		try{
			String rvId=CommonUtils.checkNull(request.getParamValue("RV_ID"));
			String qrId=CommonUtils.checkNull(request.getParamValue("QR_ID"));
			String rdIsAccept=CommonUtils.checkNull(request.getParamValue("RD_IS_ACCEPT"));
			String rdMode=CommonUtils.checkNull(request.getParamValue("RD_MODE"));
			String rdContent=CommonUtils.checkNull(request.getParamValue("RD_CONTENT"));
			String answers=CommonUtils.checkNull(request.getParamValue("list"));
			String isSatisfaction=CommonUtils.checkNull(request.getParamValue("isSatisfaction"));
			//如果是继续回访，则设置状态会继续回访;如果是其他状态，则状态设置为已回访 wizard_lee 2014.04.14
			int rvStatus=(rdMode.equals(Constant.RD_MODE_1.toString()))?Constant.RV_STATUS_2:Constant.RV_STATUS_4;
			//Map<String,Object> num = dao.isExist(rvId);
			//如果是成功回访才保存问卷。
			//if(null!=rdIsAccept&&(rdIsAccept.equals(Constant.RD_IS_ACCEPT_1))){
			if(!"".equals(rdIsAccept)&& rdIsAccept.equals(Constant.RD_IS_ACCEPT_1.toString()))
			{
				logger.info("----saveAnswer start");
				dao.saveAnswer(answers,rvId,logonUser,qrId);//保存问题
				logger.info("----saveAnswer end");
			}
			Long rdId=Long.parseLong(SequenceManager.getSequence(""));
			TtCrmReturnVisitRecordPO po= new TtCrmReturnVisitRecordPO();
			po.setRdId(rdId);
			po.setRvId(Long.parseLong(rvId));
			po.setRdIsAccept(Integer.parseInt(rdIsAccept));
			po.setRdMode(Integer.parseInt(rdMode));
			po.setRdUserId(logonUser.getUserId());
			po.setRdUser(logonUser.getName());
			po.setRdDate(new Date());
			po.setCreateBy(logonUser.getUserId());
			po.setRdMode(Integer.parseInt(rdMode));
			po.setRdContent(rdContent);
			dao.insert(po);
			
			TtCrmReturnVisitPO trv1 = new TtCrmReturnVisitPO();
			TtCrmReturnVisitPO trv2 = new TtCrmReturnVisitPO();
			trv1.setRvId(Long.parseLong(rvId));
			trv2.setRvSatisfaction(Integer.parseInt(isSatisfaction));
			trv2.setRvStatus(rvStatus);
			dao.update(trv1, trv2);
			
			String nextRvId=dao.getNextReviewId(logonUser.getUserId());
			act.setOutData("nextRvId", nextRvId);			
			act.setOutData("msg", "01");
			
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ADD_FAILURE_CODE,"客户回访");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 
	 * @Title      : 
	 * @Description: TODO  保存修改的内容
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-10
	 */
	public void saveUpdateReview()
	{
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		ReviewManageDao dao = ReviewManageDao.getInstance();
		try{
			String rvId=CommonUtils.checkNull(request.getParamValue("RV_ID"));
			String qrId=CommonUtils.checkNull(request.getParamValue("QR_ID"));
			String answers=CommonUtils.checkNull(request.getParamValue("list"));
			
			dao.saveAnswer(answers,rvId,logonUser,qrId);//保存问题
			
			TtCrmReturnVisitPO trv1 = new TtCrmReturnVisitPO();
			TtCrmReturnVisitPO trv2 = new TtCrmReturnVisitPO();
			trv1.setRvId(Long.parseLong(rvId));
			trv2.setUpdateBy(logonUser.getUserId());
			trv2.setUpdateDate(new Date());
			dao.update(trv1, trv2);
			
			act.setOutData("msg", "01");
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ADD_FAILURE_CODE,"客户回访");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}

	public void saveReviewContent(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		ReviewManageDao dao = ReviewManageDao.getInstance();
		try{
			String rdId=CommonUtils.checkNull(request.getParamValue("Rd_ID"));
			String documentName = CommonUtils.checkNull(request.getParamValue("content_docName"));
			String Rd_CONTENT=CommonUtils.checkNull(request.getParamValue(documentName));
			TtCrmReturnVisitRecordPO po1= new TtCrmReturnVisitRecordPO();
			TtCrmReturnVisitRecordPO po2= new TtCrmReturnVisitRecordPO();
			po1.setRdId(Long.parseLong(rdId));
			po2.setRdContent(Rd_CONTENT);
			po2.setRdDate(new Date());
			dao.update(po1, po2);
			act.setOutData("rd_id", rdId);
			act.setOutData("rd_content", Rd_CONTENT);
		}catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ADD_FAILURE_CODE,"客户回访更新失败");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
		
	}
	/**
	 * 
	 * @Title      : 
	 * @Description: TODO 待处理回访初始页面
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-11
	 */
	public void pendingReviewInit()
	{
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			act.setForword(pendingReviewInit);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"待处理回访");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 
	 * @Title      : 
	 * @Description: TODO  待处理回访查询 
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-11
	 */
	
	public void pendingReviewQuery()
	{
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		ReviewManageDao dao = ReviewManageDao.getInstance();
		try {
			String RV_CUS_NAME=CommonUtils.checkNull(request.getParamValue("RV_CUS_NAME"));
			String RV_TYPE=CommonUtils.checkNull(request.getParamValue("RV_TYPE"));			
			int pageSize = 10 ;
			int curPage = request.getParamValue("curPage") != null ? 
					Integer.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
			PageResult<Map<String,Object>> ps = dao.pedingReviewQuery( logonUser ,pageSize, curPage,RV_CUS_NAME,RV_TYPE) ;
			act.setOutData("ps",ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"我的客户回访");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
		
	}
	/**
	 * 
	 * @Title      : 
	 * @Description: 回访查询初始页面 
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-11
	 */
	public void reviewSearchInit()
	{
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			List<Map<String,Object>> typeNameList = ReviewManageDao.getAllTypeList();
			act.setOutData("typeNameList", typeNameList);
			act.setForword(reviewSearchInit);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"客户回访查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 
	 * @Title      : 
	 * @Description: TODO 客户回访查询
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-11
	 */
	public void reviewSearchQuery()
	{
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		ReviewManageDao dao = ReviewManageDao.getInstance();
		try {
			String RV_CUS_NAME=CommonUtils.checkNull(request.getParamValue("RV_CUS_NAME"));
			String RV_TYPE=CommonUtils.checkNull(request.getParamValue("RV_TYPE"));
			String RV_DATES=CommonUtils.checkNull(request.getParamValue("RV_DATES"));
			String RV_DATEE=CommonUtils.checkNull(request.getParamValue("RV_DATEE"));
			String vehicleType=CommonUtils.checkNull(request.getParamValue("vehicleType"));
			String RD_IS_ACCEPT=CommonUtils.checkNull(request.getParamValue("RD_IS_ACCEPT"));
			String TELEPHONE=CommonUtils.checkNull(request.getParamValue("TELEPHONE"));
			String RV_STATUS=CommonUtils.checkNull(request.getParamValue("RV_STATUS"));
			String RV_ASS_USER=CommonUtils.checkNull(request.getParamValue("RV_ASS_USER"));
			int pageSize = 10 ;
			int curPage = request.getParamValue("curPage") != null ? 
					Integer.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
			PageResult<Map<String,Object>> ps = dao.reviewSearchQuery( logonUser, pageSize, curPage,RV_CUS_NAME,RV_TYPE,RV_DATES,RV_DATEE,vehicleType,RD_IS_ACCEPT,TELEPHONE,RV_STATUS,RV_ASS_USER) ;
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"客户回访查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 
	 * @Title      : 
	 * @Description: TODO  批量设置问卷初始页面
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-15
	 */
	public void patchSetQuestionnaireInit()
	{
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		ReviewManageDao dao = ReviewManageDao.getInstance();
		try {
			//生下问卷模版下拉菜单
			List<Map<String, Object>> questionairList = dao.getQuestionairList() ;
			act.setOutData("questionairList", questionairList);
			act.setForword(patchSetQuestionnaireInit);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"批量设置问卷");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 
	 * @Title      : 
	 * @Description: TODO  设置问卷查询
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-15
	 */
	public void setQuestionairQuery()
	{
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		ReviewManageDao dao = ReviewManageDao.getInstance();
		try {

			String RV_TYPE=CommonUtils.checkNull(request.getParamValue("RV_TYPE"));
			String checkSDate=CommonUtils.checkNull(request.getParamValue("checkSDate"));
			String checkEDate=CommonUtils.checkNull(request.getParamValue("checkEDate"));
			String RV_ASS_USER=CommonUtils.checkNull(request.getParamValue("RV_ASS_USER"));
			String QR_ID=CommonUtils.checkNull(request.getParamValue("QR_ID"));
			int pageSize = 10 ;
			int curPage = request.getParamValue("curPage") != null ? 
					Integer.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
			PageResult<Map<String,Object>> ps = dao.setQuestionairQuery( pageSize, curPage,RV_TYPE,checkSDate,checkEDate,RV_ASS_USER,QR_ID) ;
			act.setOutData("ps", ps);
			act.setOutData("tot", ps.getTotalRecords());
			
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"客户回访查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 
	 * @Title      : 
	 * @Description: TODO 分配回访任务初始化
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-11
	 */
	public void allocateReviewJobInit()
	{
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		ReviewManageDao dao = ReviewManageDao.getInstance();
		try {
			List<Map<String, Object>> reviewerList = dao.getReviewerList() ;
			act.setOutData("reviewerList", reviewerList);
			act.setForword(allocateReviewJobInit);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"分配回访任务");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	public void querySet()
	{
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			act.setForword(querySet);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"分配回访任务");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 选择回访人
	 */
	public void choiseSet()
	{
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			act.setForword(chioseSet);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"指定回访人");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	public void querySetQuery()
	{
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		ReviewManageDao dao = ReviewManageDao.getInstance();
		try {
			RequestWrapper request = act.getRequest();
			int pageSize = 15 ;
			int curPage = request.getParamValue("curPage") != null ? 
					Integer.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
				PageResult<Map<String, Object>> ps = dao.getReviewerList( pageSize,  curPage);
				act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"分配回访任务");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 
	 * @Title      : 
	 * @Description: TODO 分配回访任务 查询
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-11
	 */
	public void allocateReviewQuery()
	{
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		ReviewManageDao dao = ReviewManageDao.getInstance();
		try {

			String RV_TYPE=CommonUtils.checkNull(request.getParamValue("RV_TYPE"));
			String checkSDate=CommonUtils.checkNull(request.getParamValue("checkSDate"));
			String checkEDate=CommonUtils.checkNull(request.getParamValue("checkEDate"));
			String RV_ASS_USER=CommonUtils.checkNull(request.getParamValue("RV_ASS_USER"));
			String questionair=CommonUtils.checkNull(request.getParamValue("Questionair"));
			int pageSize = 10 ;
			int curPage = request.getParamValue("curPage") != null ? 
					Integer.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
			PageResult<Map<String,Object>> ps = dao.allocateReviewQuery( pageSize, curPage,RV_TYPE,checkSDate,checkEDate,RV_ASS_USER,questionair) ;
			act.setOutData("tot", ps.getTotalRecords());
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"客户回访查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 
	 * @Title      : 
	 * @Description: TODO  设置问卷
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-15
	 */
	public void setQuestionair()
	{
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		ReviewManageDao dao = ReviewManageDao.getInstance();
		try {
			String RV_TYPE=CommonUtils.checkNull(request.getParamValue("RV_TYPE"));
			String checkSDate=CommonUtils.checkNull(request.getParamValue("checkSDate"));
			String checkEDate=CommonUtils.checkNull(request.getParamValue("checkEDate"));
			String RV_ASS_USER=CommonUtils.checkNull(request.getParamValue("RV_ASS_USER"));			
			String QR_ID=CommonUtils.checkNull(request.getParamValue("QR_ID"));
			String SELECT_RV_TYPE=CommonUtils.checkNull(request.getParamValue("SELECT_RV_TYPE"));
			String NumberFrom=CommonUtils.checkNull(request.getParamValue("NumFrom"));
			String NumberTo=CommonUtils.checkNull(request.getParamValue("NumTo"));
			String code [] = request.getParamValues("code");//获取待设置问卷的客户回访ID
			dao.setQuestionair(logonUser,code,QR_ID,SELECT_RV_TYPE,NumberFrom,NumberTo,RV_TYPE,checkSDate,checkEDate,RV_ASS_USER);
			act.setOutData("msg", "01");
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"客户回访查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 
	 * @Title      : 
	 * @Description: TODO  指定回访人
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-16
	 */
	public void setReviewPerson()
	{

		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		ReviewManageDao dao = ReviewManageDao.getInstance();
		try {
			String RV_TYPE=CommonUtils.checkNull(request.getParamValue("RV_TYPE"));
			String checkSDate=CommonUtils.checkNull(request.getParamValue("checkSDate"));
			String checkEDate=CommonUtils.checkNull(request.getParamValue("checkEDate"));
			String resvalue=CommonUtils.checkNull(request.getParamValue("resvalue"));
			String NumberFrom=CommonUtils.checkNull(request.getParamValue("NumberFrom"));
			String NumberTo=CommonUtils.checkNull(request.getParamValue("NumberTo"));
			String code [] = request.getParamValues("code");//获取待指定回访人的客户回访ID
			dao.setReviewPerson(logonUser,code,resvalue,NumberFrom,NumberTo,RV_TYPE,checkSDate,checkEDate);
			act.setOutData("msg", "01");
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"设置回访人");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 
	 * @Title      : 
	 * @Description: 题库管理
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-15
	 */
	public void examManageInit()
	{
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		ReviewManageDao dao = ReviewManageDao.getInstance();
		try {
			act.setForword(examManageInit);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"题库管理");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 
	 * @Title      : 
	 * @Description: 题 库查询
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-17
	 */
	public void examManageQuery()
	{
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		ReviewManageDao dao = ReviewManageDao.getInstance();
		try {
			String EX_NAME=CommonUtils.checkNull(request.getParamValue("EX_NAME"));
			
			int pageSize = 10 ;
			int curPage = request.getParamValue("curPage") != null ? 
					Integer.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
			PageResult<Map<String,Object>> ps = dao.examManageQuery( pageSize, curPage,EX_NAME) ;
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"题库查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 
	 * @Title      : 
	 * @Description: 题库新增 
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-17
	 */
	public void examAddInit()
	{
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		ReviewManageDao dao = ReviewManageDao.getInstance();
		try {
			Long EX_ID=Long.parseLong(SequenceManager.getSequence(""));
			act.setOutData("EX_ID", EX_ID);
			act.setForword(examAddInit);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ADD_FAILURE_CODE,"题库新增 ");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 
	 * @Title      : 
	 * @Description: 题库删除
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-17
	 */
	public void examDelete()
	{
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		ReviewManageDao dao = ReviewManageDao.getInstance();
		try {
			String code [] = request.getParamValues("code");//获取一个集合要删除的ID
			dao.examDelete(code);
			act.setOutData("msg", "01");
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.DELETE_FAILURE_CODE,"题库删除 ");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 
	 * @Title      : 
	 * @Description: 编辑题库页面
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-17
	 */
	public void editExamInit()
	{
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		ReviewManageDao dao = ReviewManageDao.getInstance();
		try {
			String exId=CommonUtils.checkNull(request.getParamValue("EX_ID"));
			List<Map<String,Object>> examinInfo= dao.getExamInfo(exId);
			act.setOutData("EX_ID", exId);
			act.setOutData("examinInfo", examinInfo);
			act.setForword(editExamInit);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"编辑题库");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 
	 * @Title      : 
	 * @Description: 浏览题库
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-17
	 */
	public void seeExam()
	{
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		ReviewManageDao dao = ReviewManageDao.getInstance();
		RequestWrapper request = act.getRequest();
		try {
			String exId=CommonUtils.checkNull(request.getParamValue("EX_ID"));
			List<Map<String,Object>> questionlist= dao.getQuestionList(exId) ;
			act.setOutData("questionlist", questionlist);
			act.setForword(seeExam);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"浏览题库");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 
	 * @Title      : 
	 * @Description: 编辑题库页面查询
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-18
	 */
	public void questionEditQuary()
	{
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		ReviewManageDao dao = ReviewManageDao.getInstance();
		RequestWrapper request = act.getRequest();
		try {
			String exId=CommonUtils.checkNull(request.getParamValue("EX_ID"));
			List<Map<String,Object>> isHave= dao.getExamInfo(exId);
			if(isHave.size()>0)
			{
				Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")): 1; // 处理当前页
				PageResult<Map<String, Object>> ps = dao.questionEditQuary(Constant.PAGE_SIZE, curPage, exId);
				act.setOutData("EX_ID", exId);
				act.setOutData("ps", ps);
				act.setOutData("question", ps.getRecords());
			}else
			{
				
			}
			
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"编辑题库");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 
	 * @Title      : 
	 * @Description: 题库修改后保存
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-18
	 */
	public void editSaveFact()
	{
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		ReviewManageDao dao = ReviewManageDao.getInstance();
		RequestWrapper request = act.getRequest();
		try {
			String exId=CommonUtils.checkNull(request.getParamValue("EX_ID"));
			String exName=CommonUtils.checkNull(request.getParamValue("EX_NAME"));
			String exDes=CommonUtils.checkNull(request.getParamValue("EX_DESCRIPTION"));
			List<Map<String,Object>> list = dao.editSaveFactCheck(exName,exId);
			if(list.get(0).get("NUM").toString().equals("0"))
			{
				dao.editSaveFact( logonUser,exId,exName,exDes);
				act.setOutData("msg","01");
			}else{
				act.setOutData("msg","02");
			}
			
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.UPDATE_FAILURE_CODE,"编辑题库");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 
	 * @Title      : 
	 * @Description: 新增题库保存
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-23
	 */
	public void addSaveFact()
	{
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		ReviewManageDao dao = ReviewManageDao.getInstance();
		RequestWrapper request = act.getRequest();
		try {
			String exId=CommonUtils.checkNull(request.getParamValue("EX_ID"));
			String exName=CommonUtils.checkNull(request.getParamValue("EX_NAME"));
			String exDes=CommonUtils.checkNull(request.getParamValue("EX_DESCRIPTION"));
			List<Map<String,Object>> list = dao.addSaveFactCheck(exName);
			if(list.get(0).get("NUM").toString().equals("0"))
			{
				dao.addSaveFact( logonUser,exId,exName,exDes);
				act.setOutData("msg","01");
			}else{
				act.setOutData("msg","02");
			}
			
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"保存题库");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 
	 * @Title      : 
	 * @Description: 题 库编辑页面删除问题 
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-18
	 */
	public void questionDelete()
	{
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		ReviewManageDao dao = ReviewManageDao.getInstance();
		try {
			String code [] = request.getParamValues("code");//获取一个集合要删除的ID
			dao.questionDelete(code);
			act.setOutData("msg", "01");
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.DELETE_FAILURE_CODE,"题库删除 ");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 
	 * @Title      : 问卷编辑页面删除问题
	 * @Description: TODO 
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-24
	 */
	public void questionDelete2()
	{
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		ReviewManageDao dao = ReviewManageDao.getInstance();
		try {
			String code [] = request.getParamValues("code");//获取一个集合要删除的ID
			dao.questionDelete2(code);
			act.setOutData("msg", "01");
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.DELETE_FAILURE_CODE,"题库删除 ");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 
	 * @Title      : 
	 * @Description: 新增问题初始页面
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-19
	 */
	public void addQuestionInit()
	{
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		ReviewManageDao dao = ReviewManageDao.getInstance();
		try {
			String exId=CommonUtils.checkNull(request.getParamValue("EX_ID"));
			List<Map<String, Object>> choiceList = dao.getChoiceList() ;
			act.setOutData("EX_ID", exId);
			act.setOutData("choiceList", choiceList);
			act.setForword(addQuestionInit);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.DELETE_FAILURE_CODE,"题库删除 ");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 
	 * @Title      : 
	 * @Description:  保存新增的问题。
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-19
	 */
	public void questionAddFact()
	{
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		ReviewManageDao dao = ReviewManageDao.getInstance();
		try {
			String exId=CommonUtils.checkNull(request.getParamValue("EX_ID"));
			String questuionType = request.getParamValue("ddlQuestionType"); 
			
			String rblTextMode=CommonUtils.checkNull(request.getParamValue("rblTextMode"));
			String txtWidth=CommonUtils.checkNull(request.getParamValue("txtWidth"));
			String txtHeight=CommonUtils.checkNull(request.getParamValue("txtHeight"));
			String txtQuestion=CommonUtils.checkNull(request.getParamValue("txtQuestion"));
			String txtSelection=CommonUtils.checkNull(request.getParamValue("txtSelection"));
			List<Map<String,Object>> list = dao.questionAddFactCheck(exId,txtQuestion);
			if(list.get(0).get("NUM").toString().equals("0"))
			{
				dao.questionAddFact(logonUser,exId,questuionType,rblTextMode,txtWidth,txtHeight,txtQuestion,txtSelection) ;
				act.setOutData("msg", "01");
			}else
			{
				act.setOutData("msg", "02");
			}
			
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ADD_FAILURE_CODE,"保存问题 ");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 
	 * @Title      : 
	 * @Description: TODO  修改题库中的问题
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-19
	 */
	public void editQuestion()
	{
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		ReviewManageDao dao = ReviewManageDao.getInstance();
		try {
			String exId=CommonUtils.checkNull(request.getParamValue("EX_ID"));
			String edId=CommonUtils.checkNull(request.getParamValue("ED_ID"));
			List<Map<String, Object>> question = dao.getQuestion(exId,edId) ;
			List<Map<String, Object>> choiceList = dao.getChoiceList() ;
			act.setOutData("question", question);
			act.setOutData("choiceList", choiceList);
			act.setForword(editQuestionInit);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.DELETE_FAILURE_CODE,"题库删除 ");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 
	 * @Title      : 修改问卷中的问题
	 * @Description: TODO 
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-23
	 */
	public void editQuestion2()
	{
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		ReviewManageDao dao = ReviewManageDao.getInstance();
		try {
			String qrId=CommonUtils.checkNull(request.getParamValue("QR_ID"));
			String qdId=CommonUtils.checkNull(request.getParamValue("QD_ID"));
			String qdNo=CommonUtils.checkNull(request.getParamValue("QD_NO"));
			List<Map<String, Object>> question = dao.getQuestion2(qrId,qdId) ;
			List<Map<String, Object>> choiceList = dao.getChoiceList() ;
			act.setOutData("question", question);
			act.setOutData("choiceList", choiceList);
			act.setForword(editQuestionInit2);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.DELETE_FAILURE_CODE,"题库删除 ");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 
	 * @Title      : 
	 * @Description: 保存题库中修改的问题 
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-21
	 */
	public void questionEditFact()
	{
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		ReviewManageDao dao = ReviewManageDao.getInstance();
		try {
			String exId=CommonUtils.checkNull(request.getParamValue("EX_ID"));
			String edId=CommonUtils.checkNull(request.getParamValue("ED_ID"));
			String questuionType = request.getParamValue("ddlQuestionType"); 
			String rblTextMode=CommonUtils.checkNull(request.getParamValue("rblTextModes"));
			String txtWidth=CommonUtils.checkNull(request.getParamValue("txtWidth"));
			String txtHeight=CommonUtils.checkNull(request.getParamValue("txtHeight"));
			String txtQuestion=CommonUtils.checkNull(request.getParamValue("txtQuestion"));
			String txtSelection=CommonUtils.checkNull(request.getParamValue("txtSelection"));
			List<Map<String,Object>> list = dao.questionEditFactCheck(exId, txtQuestion, edId);
			
			if(list.get(0).get("NUM").toString().equals("0"))
			{
				dao.questionEditFact(logonUser,exId,edId,questuionType,rblTextMode,txtWidth,txtHeight,txtQuestion,txtSelection) ;
				act.setOutData("msg", "01");
			}else
			{
				act.setOutData("msg", "02");
			}
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.UPDATE_FAILURE_CODE,"修改问题 ");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 
	 * @Title      : 
	 * @Description: 编辑问卷--编辑问题--保存问卷中被编辑的问题
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-24
	 */
	public void questionEditFact2()
	{
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		ReviewManageDao dao = ReviewManageDao.getInstance();
		try {
			String qrId=CommonUtils.checkNull(request.getParamValue("QR_ID"));
			String qdId=CommonUtils.checkNull(request.getParamValue("QD_ID"));
			String qdNo=CommonUtils.checkNull(request.getParamValue("QD_NO"));
			String questuionType = request.getParamValue("ddlQuestionType"); 
			String rblTextMode=CommonUtils.checkNull(request.getParamValue("rblTextModes"));
			String txtWidth=CommonUtils.checkNull(request.getParamValue("txtWidth"));
			String txtHeight=CommonUtils.checkNull(request.getParamValue("txtHeight"));
			String txtQuestion=CommonUtils.checkNull(request.getParamValue("txtQuestion"));
			String txtSelection=CommonUtils.checkNull(request.getParamValue("txtSelection"));
			List<Map<String,Object>> list = dao.questionEditFactCheck2(qrId, txtQuestion, qdId);
			List<Map<String,Object>> list1 = dao.questionNoEditFactCheck2(qrId, qdNo, qdId);
			//boolean falg=list.get(0).get("NUM").toString().equals("0");
			
			if(list.get(0).get("NUM").toString().equals("0") && list1.get(0).get("NUM").toString().equals("0") )
			{
				dao.questionEditFact2(logonUser,qrId,qdId,qdNo,questuionType,rblTextMode,txtWidth,txtHeight,txtQuestion,txtSelection) ;
				act.setOutData("msg", "01");
			}else if(!list1.get(0).get("NUM").toString().equals("0")){
				
				act.setOutData("msg", "02");
				
			}else{
				
				act.setOutData("msg", "03");
			}
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.UPDATE_FAILURE_CODE,"修改问题 ");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 
	 * @Title      : 
	 * @Description: 问卷管理 初始页面
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-21
	 */
	public void questionManageInit()
	{
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		ReviewManageDao dao = ReviewManageDao.getInstance();
		try {
			act.setForword(questionManageInit);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"问卷管理");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 显示问卷
	 */
	public void showQuestion() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			act.setForword(showQuestion);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"问卷查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 
	 * @Title      : 
	 * @Description: 问卷管理 页面查询
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-21
	 */
	public void questionairManageQuery()
	{
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		ReviewManageDao dao = ReviewManageDao.getInstance();
		RequestWrapper request = act.getRequest();
		try {
			String QR_TYPE=CommonUtils.checkNull(request.getParamValue("QR_TYPE"));
			String checkSDate=CommonUtils.checkNull(request.getParamValue("checkSDate"));
			String checkEDate=CommonUtils.checkNull(request.getParamValue("checkEDate"));
			String QR_STATUS=CommonUtils.checkNull(request.getParamValue("QR_STATUS"));
			String QR_NAME=CommonUtils.checkNull(request.getParamValue("QR_NAME"));
			
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")): 1; // 处理当前页
			PageResult<Map<String, Object>> ps = null;
			ps=dao.questionairManageQuery(Constant.PAGE_SIZE, curPage, QR_TYPE,checkSDate,checkEDate,QR_STATUS,QR_NAME);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"问卷管理");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 
	 * @Title      : 
	 * @Description: 设置问卷生效
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-22
	 */
	public void setStatus()
	{
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		ReviewManageDao dao = ReviewManageDao.getInstance();
		RequestWrapper request = act.getRequest();
		try {
			String code [] = request.getParamValues("code");//获取一个集合要删除的ID
			dao.setStatus(logonUser,code);
			act.setOutData("msg", "01");
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.UPDATE_FAILURE_CODE,"设置生效");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 
	 * @Title      : 
	 * @Description: 删除问卷
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-22
	 */
	public void deleteQuestionair()
	{
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		ReviewManageDao dao = ReviewManageDao.getInstance();
		RequestWrapper request = act.getRequest();
		try {
			String code [] = request.getParamValues("code");//获取一个集合要删除的ID
			dao.deleteQuestionair(logonUser,code);
			act.setOutData("msg", "01");
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.DELETE_FAILURE_CODE,"删除问卷");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 
	 * @Title      : 浏览问卷
	 * @Description: TODO 
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-22
	 */
	public void seeQuestionair(){
		
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		ReviewManageDao dao = ReviewManageDao.getInstance();
		RequestWrapper request = act.getRequest();
		try {
			//获得问卷ID
			String QR_ID=CommonUtils.checkNull(request.getParamValue("QR_ID"));
			Map<String,LinkedList<Map<String,Object>>> result = new HashMap();
			//根据ID查询 问题及答案
			result=dao.questionairQuery(QR_ID);
			String questionhtml=generateQuestion3(result.get("questions"));
			act.setOutData("result", result);
			act.setOutData("questionhtml", questionhtml);
			act.setForword(seeQuestionair);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE," 浏览问卷");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
		
		
	}
	/**
	 * 
	 * @Title      : 
	 * @Description: 新增问卷页面
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-22
	 */
	public void addQuestionairInit()
	{
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		ReviewManageDao dao = ReviewManageDao.getInstance();
		RequestWrapper request = act.getRequest();
		try {
			Long QR_ID=Long.parseLong(SequenceManager.getSequence(""));
			act.setOutData("QR_ID", QR_ID);
			act.setForword(addQuestionairInit);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.DELETE_FAILURE_CODE,"删除问卷");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
		
	}
	/**
	 * 
	 * @Title      : 
	 * @Description: TODO  编辑问卷中查询该问卷包含的问题
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-22
	 */
	public void questionairAddQuery()
	{
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		ReviewManageDao dao = ReviewManageDao.getInstance();
		RequestWrapper request = act.getRequest();
		try {
			String qrId=CommonUtils.checkNull(request.getParamValue("QR_ID"));
			List<Map<String,Object>> isHave= dao.getQuestionairInfo(qrId);
			if(isHave.get(0).get("NUM").toString().equals("0")==false)
			{
				Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")): 1; // 处理当前页
				PageResult<Map<String, Object>> ps = dao.questionAddQuery(Constant.PAGE_SIZE, curPage, qrId);
				act.setOutData("QR_ID", qrId);
				act.setOutData("ps", ps);
				act.setOutData("question", ps.getRecords());
			}else{	}
			
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"编辑问卷");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 
	 * @Title      : 
	 * @Description: 问卷新增
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-22
	 */
	public void questionairAddFact()
	{
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		ReviewManageDao dao = ReviewManageDao.getInstance();
		try {
			String qrId=CommonUtils.checkNull(request.getParamValue("QR_ID"));
			String QR_NAME=CommonUtils.checkNull(request.getParamValue("QR_NAME"));
			String QR_TYPE=CommonUtils.checkNull(request.getParamValue("QR_TYPE"));
			String QR_DESCRIPTION=CommonUtils.checkNull(request.getParamValue("QR_DESCRIPTION"));
			String QR_GUIDE=CommonUtils.checkNull(request.getParamValue("QR_GUIDE"));
			String QR_THANKS=CommonUtils.checkNull(request.getParamValue("QR_THANKS"));
			List<Map<String,Object>> list =dao.questionairAddFact(QR_NAME);
			if(list.get(0).get("NUM").toString().equals("0"))
			{
				dao.questionairAddFactf(logonUser,qrId,QR_NAME,QR_TYPE,QR_DESCRIPTION,QR_GUIDE,QR_THANKS) ;
				act.setOutData("msg", "01");
			}else{
				act.setOutData("msg", "02");
			}
			
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ADD_FAILURE_CODE,"保存问题 ");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 
	 * @Title      : 
	 * @Description: 问卷中新增问题
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-22
	 */
	public void addQuestionInit2()
	{
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		ReviewManageDao dao = ReviewManageDao.getInstance();
		try {
			String qrId=CommonUtils.checkNull(request.getParamValue("QR_ID"));
			List<Map<String, Object>> choiceList = dao.getChoiceList() ;
			List<Map<String,Object>> qdNolist= dao.getNextQD_NO(qrId);
			Integer qdNo= Integer.parseInt(qdNolist.get(0).get("NEXTNO").toString());
			act.setOutData("QR_ID", qrId);
			act.setOutData("choiceList", choiceList);
			act.setOutData("QD_NO", qdNo);
			act.setForword(addQuestionInit2);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.DELETE_FAILURE_CODE,"题库删除 ");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	public void questionAddFact2()
	{
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		ReviewManageDao dao = ReviewManageDao.getInstance();
		try {
			String qrId=CommonUtils.checkNull(request.getParamValue("QR_ID"));
			String qdNo=CommonUtils.checkNull(request.getParamValue("QD_NO"));
			String questuionType = request.getParamValue("ddlQuestionType"); 
			String rblTextMode=CommonUtils.checkNull(request.getParamValue("rblTextModes"));
			String txtWidth=CommonUtils.checkNull(request.getParamValue("txtWidth"));
			String txtHeight=CommonUtils.checkNull(request.getParamValue("txtHeight"));
			String txtQuestion=CommonUtils.checkNull(request.getParamValue("txtQuestion"));
			String txtSelection=CommonUtils.checkNull(request.getParamValue("txtSelection"));
			//问题重复验证
			List<Map<String,Object>> list = dao.questionAddFactCheck2(qrId,txtQuestion);
			//问题题号重复验证
			List<Map<String,Object>> list1 = dao.questionNoAddFactCheck2(qrId, qdNo);
			
			if(list.get(0).get("NUM").toString().equals("0") && list1.get(0).get("NUM").toString().equals("0") )
			{
				dao.questionAddFact2(logonUser,qrId,qdNo,questuionType,rblTextMode,txtWidth,txtHeight,txtQuestion,txtSelection) ;
				act.setOutData("msg", "01");
			}else if(!list1.get(0).get("NUM").toString().equals("0")){
				
				act.setOutData("msg", "02");
				
			}else{
				
				act.setOutData("msg", "03");
			}
						
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ADD_FAILURE_CODE,"保存问题 ");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 
	 * @Title      : 
	 * @Description: 问卷复制
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-22
	 */
	public void myCopy()
	{
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		ReviewManageDao dao = ReviewManageDao.getInstance();
		try {
			String qrId=CommonUtils.checkNull(request.getParamValue("QR_ID"));
			dao.copyQuestionair(logonUser,qrId) ;
			act.setOutData("msg", "01");
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ADD_FAILURE_CODE,"复制问卷 ");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 
	 * @Title      : 编辑问卷
	 * @Description: TODO 
	 * @param      :      
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-23
	 */
	public void editQuestionairInit()
	{
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		ReviewManageDao dao = ReviewManageDao.getInstance();
		try {
			String qrId=CommonUtils.checkNull(request.getParamValue("QR_ID"));
			List<Map<String,Object>> questionair = dao.getQuestionairDetail(qrId);
			act.setOutData("questionair", questionair);
			act.setForword(editQuestionairInit);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ADD_FAILURE_CODE,"编辑问卷 ");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 
	 * @Title      : 
	 * @Description: 问卷编辑保存 
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-23
	 */
	public void questionairEditFact()
	{
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		ReviewManageDao dao = ReviewManageDao.getInstance();
		try {
			String qrId=CommonUtils.checkNull(request.getParamValue("QR_ID"));
			String QR_NAME=CommonUtils.checkNull(request.getParamValue("QR_NAME"));
			String QR_TYPE=CommonUtils.checkNull(request.getParamValue("QR_TYPE"));
			String QR_DESCRIPTION=CommonUtils.checkNull(request.getParamValue("QR_DESCRIPTION"));
			String QR_GUIDE=CommonUtils.checkNull(request.getParamValue("QR_GUIDE"));
			String QR_THANKS=CommonUtils.checkNull(request.getParamValue("QR_THANKS"));
			List<Map<String,Object>> list =dao.questionairEditFact(QR_NAME,qrId);
			if(list.get(0).get("NUM").toString().equals("0"))
			{
				dao.questionairEditFact(logonUser,qrId,QR_NAME,QR_TYPE,QR_DESCRIPTION,QR_GUIDE,QR_THANKS) ;
				act.setOutData("msg", "01");
			}else{
				act.setOutData("msg", "02");
			}
			
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.UPDATE_FAILURE_CODE,"编辑问卷 ");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 
	 * @Title      : 
	 * @Description: TODO 查询题库中的问题
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-24
	 */
	public void allDrlQuery() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		ReviewManageDao dao = ReviewManageDao.getInstance();
		try {
			List<Map<String,Object>> examList =dao.getExams();
			act.getResponse().setContentType("application/json");
			if("1".equals(request.getParamValue("COMMAND"))) {
				String dcode = CommonUtils.checkNull(request.getParamValue("DRLCODE"));
				String isMulti = CommonUtils.checkNull(request.getParamValue("isMulti"));
				String qrId = CommonUtils.checkNull(request.getParamValue("qrId"));
				int page_size = Constant.PAGE_SIZE;
				if(isMulti != null && !"".equals(isMulti))
				{
					page_size = 20;
				}
				String provinceId = CommonUtils.checkNull(request.getParamValue("downtown") );
				if (provinceId == null) {
					provinceId = "" ;
				}
				String dealerClass = CommonUtils.checkNull(request.getParamValue("dealerClass")) ;
				if (dealerClass == null) {
					dealerClass = "" ;
				}
				Integer curPage = request.getParamValue("curPage2") != null ? Integer
						.parseInt(request.getParamValue("curPage2")): 1; // 处理当前页

						PageResult<Map<String, Object>> ps = dao.getAllDRLByDeptId(qrId,dcode, provinceId,dealerClass,curPage,page_size);
						act.setOutData("ps", ps);
						act.setOutData("qrId", qrId);
			}
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"题库查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 
	 * @Title      : 
	 * @Description: 从题 库中选择问题进行保存
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-26
	 */
	public void setQuestions()
	{
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		ReviewManageDao dao = ReviewManageDao.getInstance();
		try {
				String ids=CommonUtils.checkNull(request.getParamValue("ids"));
				String qrId=CommonUtils.checkNull(request.getParamValue("qrId"));
				
				dao.setQuestions(logonUser,qrId,ids) ;
				act.setOutData("msg", "01");
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.UPDATE_FAILURE_CODE,"编辑问卷 ");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
		
	}
	/**
	 * 
	 * @Title      : 
	 * @Description: 生成客户回访页面
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-20
	 */
	public void createReviewInit()
	{
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		ReviewManageDao dao = ReviewManageDao.getInstance();
		try {
			List<Map<String, Object>> seriesList = dao.getSeriesList();//车系
			//List<Map<String, Object>> vechileTypeList = dao.getVehicleTypeList();//车型
			CommonUtilActions commonUtilActions = new CommonUtilActions();
			act.setOutData("proviceList", commonUtilActions.getProvice());//地区
			act.setOutData("seriesList", seriesList);
			//act.setOutData("vechileTypeList", vechileTypeList);
			act.setForword(createReviewInit);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"生成客户回访");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 新增客户回访
	 */
	public void addReviewInit(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			
			act.setForword(addReviewInit);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"生成客户回访");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 客户回访登记保存
	 */
	public void saveCrmReturn(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		try {
			ReviewManageDao dao = ReviewManageDao.getInstance();
			//TcCodeDao tcCodeDao = TcCodeDao.getInstance();
			String rvCusIdx = request.getParamValue("ctmId");
			String rvCusNamex = request.getParamValue("ctmname");
			String rvPhonex = request.getParamValue("tele");
			String vinx = request.getParamValue("vinStr");
			
			String rvType = request.getParamValue("rvType");
			String rvDate = request.getParamValue("rvDate");
			String rvTimes = XHBUtil.IsNull(request.getParamValue("rvTimes")) ? "0" : request.getParamValue("rvTimes");
			String rvAssUserId = request.getParamValue("seId");
			String rvAssUser = request.getParamValue("seName");
			String rvResult = request.getParamValue("rvResult");
			String qrId = request.getParamValue("qrId");
			String rvStatus = request.getParamValue("rvStatus");
			String rvSatisfAction = request.getParamValue("rvSatisfAction");
			String remark = request.getParamValue("remark");
			
			String message = "";
			int count = 0;
			if(!XHBUtil.IsNull(rvCusIdx)) {
				String [] rvCusIds = rvCusIdx.split(",");
				String [] rvCusNames = rvCusNamex.split(",");
				String [] rvPhones = rvPhonex.split(",");
				String [] vins = vinx.split(",");
				if(!XHBUtil.IsNull(rvCusIds) && rvCusIds.length > 0){
					for (int i = 0; i < rvCusIds.length; i++) {
						String rvCusId = rvCusIds[i];
						String rvCusName = rvCusNames[i];
						String rvPhone = rvPhones[i];
						String vin = vins[i];
						
						//验证同一问卷同一客户是否已经有过回访
						List<Map<String, Object>> maps = dao.queryByCusIdAndQrId(rvCusId, qrId);
						if (maps != null && maps.size() > 0) {
							message += "客户:"+rvCusName+",该问卷已经有回访记录,系统自动跳过<br/>";
							continue;
						}else{//2014-07-23 判断已经回访过的，跳出循环
						
							TtCrmReturnVisitPO po = new TtCrmReturnVisitPO();
							po.setRvId(Long.parseLong(SequenceManager.getSequence("")));
							po.setRvCusId(Long.parseLong(rvCusId));
							po.setRvCusName(rvCusName);
							po.setRvPhone(rvPhone);
							po.setRvType(Integer.parseInt(rvType));
							SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
							po.setRvDate(sdf.parse(rvDate));
							po.setRvTimes(Integer.parseInt(rvTimes));
							po.setRvAssUserId(Long.parseLong(rvAssUserId));
							po.setRvAssUser(rvAssUser);
							po.setQrId(Long.parseLong(qrId));
							po.setRvStatus(Integer.parseInt(rvStatus));
							if (Constant.RV_STATUS_1 != po.getRvStatus()) {
								if (!XHBUtil.IsNull(rvResult)) {
									po.setRvResult(Integer.parseInt(rvResult));
								}
								if (!XHBUtil.IsNull(rvSatisfAction)) {
									po.setRvSatisfaction(Integer.parseInt(rvSatisfAction));
								}
							}
							po.setCreateBy(logonUser.getUserId());
							po.setCreateDate(new Date());
							po.setUpdateBy(logonUser.getUserId());
							po.setUpdateDate(new Date());
							po.setVin(vin);
							po.setRemark(remark);
							dao.insert(po);
						}
						count++;
					}
				}
			}
			
			/*//获取问卷ID
			String qrId = "";
			String codeDesc = tcCodeDao.getCodeDescByCodeId(Constant.QUESTION_QUESTIONNAIRE+"");
			List<Map<String, Object>> questions =  dao.queryQuestionInfo(codeDesc);
			if (questions != null && questions.size() > 0) {
				qrId = questions.get(0).get("QR_ID").toString();
			}else {
				//问卷
				TtCrmQuestionnairePO tcPo = new TtCrmQuestionnairePO();
				tcPo.setQrId(Long.parseLong(SequenceManager.getSequence("")));
				tcPo.setQrName(codeDesc);
				tcPo.setQrType(Constant.QR_TYPE_1);
				tcPo.setQrStatus(Constant.QR_STATUS_1);
				tcPo.setQrStatusDel(Constant.STATUS_ENABLE);
				tcPo.setCreateBy(logonUser.getUserId());
				tcPo.setCreateDate(new Date());
				dao.insert(tcPo);
				qrId = tcPo.getQrId()+"";
				
				//问卷明细
				String question = tcCodeDao.getCodeDescByCodeId(Constant.QUESTION_DETAIL+"");
				String answer = tcCodeDao.getCodeDescByCodeId(Constant.QUESTION_ANSWER+"");
				TtCrmQueDetailPO tcdPo = new TtCrmQueDetailPO();
				tcdPo.setQdId(Long.parseLong(SequenceManager.getSequence("")));
				tcdPo.setQrId(tcPo.getQrId());
				tcdPo.setQdNo(1);
				tcdPo.setQdQueType(Constant.QD_QUE_TYPE_1);
				tcdPo.setQdQuestion(question);
				tcdPo.setQdChoice(answer);
				tcdPo.setEdTxtWidth(600);
				tcdPo.setEdTxtHight(110);
				tcdPo.setCreateBy(logonUser.getUserId());
				tcdPo.setCreateDate(new Date());
				tcdPo.setQdStatus(Constant.STATUS_ENABLE);
				dao.insert(tcdPo);
			}*/
			message += "成功添加回访个数:["+count+"]个";
			act.setForword(addReviewInit);
			act.setOutData("message", message);
			act.setOutData("isSuccess", "0");
		} catch (Exception e) {
			act.setOutData("isSuccess", "1");
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"生成客户回访");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 
	 * @Title      : 
	 * @Description: TODO 生成客户回访查询
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-26
	 */
	public void createReviewInitQuery()
	{
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		ReviewManageDao dao = ReviewManageDao.getInstance();
		RequestWrapper request = act.getRequest();
		try {
			String checkSDate=CommonUtils.checkNull(request.getParamValue("checkSDate"));//购买日期起
			String checkEDate=CommonUtils.checkNull(request.getParamValue("checkEDate"));
//			String checkSDate=CommonUtils.checkNull(request.getParamValue("checkSDate"));//添加日期起
//			String checkEDate=CommonUtils.checkNull(request.getParamValue("checkEDate"));
			String ddlAutoTypeCode=CommonUtils.checkNull(request.getParamValue("ddlAutoTypeCode"));//汽车型号
			String ddlClasses=CommonUtils.checkNull(request.getParamValue("ddlClasses"));//汽车种类
			String txtUserLevel=CommonUtils.checkNull(request.getParamValue("txtUserLevel"));//用户级别
			String downtown=CommonUtils.checkNull(request.getParamValue("downtown"));//地区
			String txtPurpose=CommonUtils.checkNull(request.getParamValue("txtPurpose"));//用途
			String customer_type=CommonUtils.checkNull(request.getParamValue("customer_type"));//客户类型
			String txtBatholithNum=CommonUtils.checkNull(request.getParamValue("txtBatholithNum"));//vin
			String dealerName=CommonUtils.checkNull(request.getParamValue("dealerName"));//艾春2013.7.16添加 经销商名称
			
			String roSDate=CommonUtils.checkNull(request.getParamValue("roSDate"));//维修日期开始
			String roEDate=CommonUtils.checkNull(request.getParamValue("roEDate"));//维修日期结束
			
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")): 1; // 处理当前页
//			PageResult<Map<String, Object>> ps = dao.customerQuery(checkSDate2,checkEDate2,checkSDate,checkEDate,ddlAutoTypeCode,ddlClasses,txtUserLevel,downtown,txtPurpose,customer_type,txtBatholithNum, dealerName,Constant.PAGE_SIZE, curPage);
			PageResult<Map<String, Object>> ps = dao.customerQueryNew(checkSDate,checkEDate,ddlAutoTypeCode,ddlClasses,txtUserLevel,downtown,txtPurpose,customer_type,txtBatholithNum, dealerName, roSDate, roEDate, Constant.PAGE_SIZE, curPage);
			act.setOutData("tot", ps.getTotalRecords());
			act.setOutData("ps", ps);
			act.setOutData("msg", "01");
			
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"随机选择");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 
	 * @Title      : 
	 * @Description: 随机选择
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-27
	 */
	public void radomSel()
	{
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		ReviewManageDao dao = ReviewManageDao.getInstance();
		RequestWrapper request = act.getRequest();
		try {
			String txtRand=CommonUtils.checkNull(request.getParamValue("txtRand"));//随机数
			String checkSDate2=CommonUtils.checkNull(request.getParamValue("checkSDate2"));//购买日期起
			String checkEDate2=CommonUtils.checkNull(request.getParamValue("checkEDate2"));
			String checkSDate=CommonUtils.checkNull(request.getParamValue("checkSDate"));//添加日期起
			String checkEDate=CommonUtils.checkNull(request.getParamValue("checkEDate"));
			String ddlAutoTypeCode=CommonUtils.checkNull(request.getParamValue("ddlAutoTypeCode"));//汽车型号
			String ddlClasses=CommonUtils.checkNull(request.getParamValue("ddlClasses"));//汽车种类
			String txtUserLevel=CommonUtils.checkNull(request.getParamValue("txtUserLevel"));//用户级别
			String downtown=CommonUtils.checkNull(request.getParamValue("downtown"));//地区
			String txtPurpose=CommonUtils.checkNull(request.getParamValue("txtPurpose"));//用途
			String customer_type=CommonUtils.checkNull(request.getParamValue("customer_type"));//客户类型
			String txtBatholithNum=CommonUtils.checkNull(request.getParamValue("txtBatholithNum"));//vin
		
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")): 1; // 处理当前页
			List<Map<String, Object>> ps = dao.radomSel(txtRand,checkSDate2,checkEDate2,checkSDate,checkEDate,ddlAutoTypeCode,ddlClasses,txtUserLevel,downtown,txtPurpose,customer_type,txtBatholithNum,Constant.PAGE_SIZE, curPage);
			//act.setOutData("tot", ps.getTotalRecords());
			act.setOutData("ps", ps);
			StringBuilder sb= new StringBuilder();
			StringBuilder vin = new StringBuilder();
			if(ps.size()>0)
			{
				for(Map<String,Object> m:ps)
				{
					sb.append(m.get("CTM_ID")+",");
					vin.append(m.get("VIN")+",");
				}
				sb.setCharAt(sb.lastIndexOf(","), ' ');
				vin.setCharAt(vin.lastIndexOf(","), ' ');
			}
			act.setOutData("vins", vin.toString());
			act.setOutData("ids", sb.toString());
			act.setOutData("msg", "01");
			
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"生成客户回访");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
		
	}
	
	public void generateReview()
	{
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		ReviewManageDao dao = ReviewManageDao.getInstance();
		RequestWrapper request = act.getRequest();
		try {
			String RV_TYPE=CommonUtils.checkNull(request.getParamValue("RV_TYPE"));//回访类型
			String Results=CommonUtils.checkNull(request.getParamValue("Results"));//客户IDS
			
			 //dao.generateReview( logonUser,RV_TYPE,Results);
			String vin = CommonUtils.checkNull(request.getParamValue("VINS"));
			List<Map<String, Object>> ctmlist = dao.getCtmsByVin(Results,vin);
			dao.generateSatisReview(logonUser,ctmlist,RV_TYPE);
			if(ctmlist!=null){
				act.setOutData("count", ctmlist.size());
			}
			
			act.setOutData("msg", "01");
			
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ADD_FAILURE_CODE,"生成客户回访");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 检验同一个客户在同一天是否生成了同一类型的回访
	 */
	public void checkReview(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		ReviewManageDao dao = ReviewManageDao.getInstance();
		RequestWrapper request = act.getRequest();
		try {
			String result = request.getParamValue("Results");
			String rvType = request.getParamValue("RV_TYPE");
			String vin = request.getParamValue("VINS");
			//List<TtCrmReturnVisitPO>list = dao.checkReview(result,rvType,vin);
			List<TtCrmReturnVisitPO>list = dao.checkSatisReview(result,rvType,vin);
			if(list==null || list.size()==0){
				act.setOutData("msg", "yes");
			}else{
				act.setOutData("msg", "no");
			}
			
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ADD_FAILURE_CODE,"生成客户回访");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	
	
	
	
	
	/**
	 * 
	 * @Title      : 
	 * @Description:满意度回访初始页面 
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-20
	 */
	public void satisfactionReviewInit()
	{
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		ReviewManageDao dao = ReviewManageDao.getInstance();
		try {
			List<Map<String, Object>> seriesList = dao.getSeriesList();//车系
			//List<Map<String, Object>> vechileTypeList = dao.getVehicleTypeList();//车系
			List<Map<String,Object>> stations = dao.getDealerClass();
			act.setOutData("stations", stations);
			act.setOutData("seriesList", seriesList);
			//act.setOutData("vechileTypeList", vechileTypeList);
			act.setForword(satisfactionReviewInit);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"满意度回访");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 
	 * @Title      : 
	 * @Description: 满意度回访-查询
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-21
	 */
	public void reviewSatisfactionQuery()
	{
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		ReviewManageDao dao = ReviewManageDao.getInstance();
		RequestWrapper request = act.getRequest();
		try {
			String checkSDate=CommonUtils.checkNull(request.getParamValue("checkSDate"));//维修开始日期
			String checkEDate=CommonUtils.checkNull(request.getParamValue("checkEDate"));
			String ddlClasses=CommonUtils.checkNull(request.getParamValue("ddlClasses"));//汽车种类
			String ddlAutoTypeCode=CommonUtils.checkNull(request.getParamValue("ddlAutoTypeCode"));//车型
			String []chkServiceStationType=request.getParamValues("chkServiceStationType");//服务站
			String VIN=CommonUtils.checkNull(request.getParamValue("VIN"));//vin
			StringBuffer stations = new StringBuffer();
			if(chkServiceStationType!=null){
				for(int i=0;i<chkServiceStationType.length;i++){
					stations.append(chkServiceStationType[i]+",");
				}
				stations.setCharAt(stations.lastIndexOf(","), ' ');
			}
			String station = stations.toString();
			
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")): 1; // 处理当前页
			PageResult<Map<String, Object>> ps =null;
			ps= dao.reviewSatisfactionQuery(Constant.PAGE_SIZE,curPage,checkSDate,checkEDate,ddlClasses,ddlAutoTypeCode,VIN,station );
			act.setOutData("tot", ps.getTotalRecords());
			act.setOutData("ps", ps);
			act.setOutData("msg", "01");
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"满意度回访");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 随机选择 满意度回访
	 */
	public void satisRandSel(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		ReviewManageDao dao = ReviewManageDao.getInstance();
		RequestWrapper request = act.getRequest();
		try {
			String txtRand=CommonUtils.checkNull(request.getParamValue("txtRand"));//随机数
			String checkSDate=CommonUtils.checkNull(request.getParamValue("checkSDate"));//维修开始日期
			String checkEDate=CommonUtils.checkNull(request.getParamValue("checkEDate"));
			String ddlClasses=CommonUtils.checkNull(request.getParamValue("ddlClasses"));//汽车种类
			String ddlAutoTypeCode=CommonUtils.checkNull(request.getParamValue("ddlAutoTypeCode"));//车型
			String[] chkServiceStationType=request.getParamValues("chkServiceStationType");//服务站类型
			String VIN=CommonUtils.checkNull(request.getParamValue("VIN"));//vin
			StringBuffer stations = new StringBuffer();
			if(chkServiceStationType!=null){
				for(int i=0;i<chkServiceStationType.length;i++){
					stations.append(chkServiceStationType[i]+",");
				}
				stations.setCharAt(stations.lastIndexOf(","), ' ');
			}
			String station = stations.toString();
			
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")): 1; // 处理当前页
			List<Map<String, Object>> ps =null;
			ps= dao.satisRandSel(Constant.PAGE_SIZE,curPage,checkSDate,checkEDate,ddlClasses,ddlAutoTypeCode,VIN,station,txtRand );
			act.setOutData("ps", ps);
			StringBuilder sb= new StringBuilder();
			StringBuilder vin = new StringBuilder();
			if(ps.size()>0)
			{
				for(Map<String,Object> m:ps)
				{
					sb.append(m.get("CTM_ID")+",");
					vin.append(m.get("VIN")+",");
				}
				sb.setCharAt(sb.lastIndexOf(","), ' ');
				vin.setCharAt(vin.lastIndexOf(","), ' ');
			}
			act.setOutData("vins", vin.toString());
			act.setOutData("ids", sb.toString());
			act.setOutData("msg", "01");
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"满意度回访");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 检验满意度回访：当天是否生成同一客户的满意度回访
	 */
	public void checkSatisReview(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		ReviewManageDao dao = ReviewManageDao.getInstance();
		RequestWrapper request = act.getRequest();
		try {
			String result = request.getParamValue("Results");
			String vin = request.getParamValue("VINS");
			String rvType = Constant.TYPE_RETURN_VISIT4;
			List<TtCrmReturnVisitPO>list = dao.checkSatisReview(result,rvType,vin);
			if(list==null || list.size()==0){
				act.setOutData("msg", "yes");
			}else{
				act.setOutData("msg", "no");
			}
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"满意度回访");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 设置满意度回访
	 */
	public void generateSatisReview(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		ReviewManageDao dao = ReviewManageDao.getInstance();
		RequestWrapper request = act.getRequest();
		try {
			String result = CommonUtils.checkNull(request.getParamValue("Results"));
			String vin = CommonUtils.checkNull(request.getParamValue("VINS"));
			List<Map<String, Object>> ctmlist = dao.getCtmsByVin(result,vin);
			dao.generateSatisReview(logonUser,ctmlist,Constant.TYPE_RETURN_VISIT4);
			if(ctmlist!=null){
				act.setOutData("count", ctmlist.size());
			}
			act.setOutData("msg", "01");
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"满意度回访");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	
	
	
	
	/**
	 * 回访问卷导出初始化
	 */
	public void reviewQuestionnairExportInit()
	{
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		ReviewManageDao dao = ReviewManageDao.getInstance();
		try {
			List<Map<String, Object>> seriesList = dao.getSeriesList();//车系
			//List<Map<String, Object>> vechileTypeList = dao.getVehicleTypeList();//车型
			List<Map<String, Object>> questionairList = dao.getQuestionairList() ;//生下问卷模版下拉菜单
			act.setOutData("questionairList", questionairList);
			act.setOutData("seriesList", seriesList);
			//act.setOutData("vechileTypeList", vechileTypeList);
			act.setForword(reviewQuestionnairExportInit);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"回访问卷导出");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 回访问卷导出--查询
	 */
	public void reviewExportSearch(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		ReviewManageDao dao = ReviewManageDao.getInstance();
		try {
			String RV_CUS_NAME=CommonUtils.checkNull(request.getParamValue("cusName"));//客户名称
			String RV_TYPE=CommonUtils.checkNull(request.getParamValue("RV_TYPE"));//回访类型
			String checkSDate=CommonUtils.checkNull(request.getParamValue("checkSDate"));//生成日期
			String checkEDate=CommonUtils.checkNull(request.getParamValue("checkEDate"));
			String checkSDate2=CommonUtils.checkNull(request.getParamValue("checkSDate2"));//回访日期
			String checkEDate2=CommonUtils.checkNull(request.getParamValue("checkEDate2"));
			String ddlClasses = CommonUtils.checkNull(request.getParamValue("ddlClasses"));//汽车种类
			String ddlAutoTypeCode=CommonUtils.checkNull(request.getParamValue("ddlAutoTypeCode"));//汽车型号
			String RD_IS_ACCEPT=CommonUtils.checkNull(request.getParamValue("RD_IS_ACCEPT"));//回访结果
			String chkIsEmptyQuest = CommonUtils.checkNull(request.getParamValue("showQuest"));//只显示空问卷
			String TELEPHONE=CommonUtils.checkNull(request.getParamValue("TELEPHONE"));//联系电话
			String QR_ID=CommonUtils.checkNull(request.getParamValue("QR_ID"));//问卷Id
			String RV_ASS_USER=CommonUtils.checkNull(request.getParamValue("RV_ASS_USER"));//回访人
			int pageSize = 10 ;
			int curPage = request.getParamValue("curPage") != null ? 
					Integer.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
			List<Map<String,Object>> questions=null;
			if(!"".equals(QR_ID)){
				questions = dao.getQuestionById(QR_ID);//获取问题名称
			}
			PageResult<Map<String,Object>> ps = dao.reviewExportSearch( logonUser, pageSize, curPage,RV_CUS_NAME,RV_TYPE,checkSDate,checkEDate,checkSDate2,checkEDate2,RD_IS_ACCEPT,TELEPHONE,QR_ID,RV_ASS_USER,chkIsEmptyQuest,ddlAutoTypeCode,ddlClasses,questions) ;
			act.setOutData("ps", ps);
			//act.setOutData("questions", questions);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"回访问卷导出");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	public void queryQuestions(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		ReviewManageDao dao = ReviewManageDao.getInstance();
		try {
			String QR_ID=CommonUtils.checkNull(request.getParamValue("QR_ID"));//问卷Id
			String chkIsEmptyQuest = CommonUtils.checkNull(request.getParamValue("showQuest"));//只显示空问卷
			List<Map<String,Object>> questions = null;
			if(!"".equals(QR_ID)){
				questions = dao.getQuestionById(QR_ID);//获取问题名称
			}
			act.setOutData("questions", questions);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"回访问卷导出");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 回访问卷导出--导出
	 */
	public void reviewQuestExport(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		ReviewManageDao dao = ReviewManageDao.getInstance();
		try {
			String RV_CUS_NAME=CommonUtils.checkNull(request.getParamValue("cusName"));//客户名称
			String RV_TYPE=CommonUtils.checkNull(request.getParamValue("RV_TYPE"));//回访类型
			String checkSDate=CommonUtils.checkNull(request.getParamValue("checkSDate"));//生成日期
			String checkEDate=CommonUtils.checkNull(request.getParamValue("checkEDate"));
			String checkSDate2=CommonUtils.checkNull(request.getParamValue("checkSDate2"));//回访日期
			String checkEDate2=CommonUtils.checkNull(request.getParamValue("checkEDate2"));
			String ddlClasses = CommonUtils.checkNull(request.getParamValue("ddlClasses"));//汽车种类
			String ddlAutoTypeCode=CommonUtils.checkNull(request.getParamValue("ddlAutoTypeCode"));//汽车型号
			String RD_IS_ACCEPT=CommonUtils.checkNull(request.getParamValue("RD_IS_ACCEPT"));//回访结果
			String chkIsEmptyQuest = CommonUtils.checkNull(request.getParamValue("showQuest"));//只显示空问卷
			String TELEPHONE=CommonUtils.checkNull(request.getParamValue("TELEPHONE"));//联系电话
			String QR_ID=CommonUtils.checkNull(request.getParamValue("QR_ID"));//问卷Id
			String RV_ASS_USER=CommonUtils.checkNull(request.getParamValue("RV_ASS_USER"));//回访人
			//查询数据,基本信息，问卷题目，问卷答案
			List<Map<String,Object>> questions=null;
			if(!"".equals(QR_ID)){
				questions = dao.getQuestionById(QR_ID);//获取问题名称
			}
			List<Map<String,Object>> reviewInfo = dao.queryReviewInfo(RV_CUS_NAME,RV_TYPE,checkSDate,checkEDate,checkSDate2,checkEDate2,RD_IS_ACCEPT,TELEPHONE,QR_ID,RV_ASS_USER,chkIsEmptyQuest,ddlAutoTypeCode,ddlClasses,questions);
			callReviewQuestionaireDataToExecle(questions,reviewInfo);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.SPECIAL_MEG,"转Excel失败!");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/*将回访问卷内容写入execel*/
	public void callReviewQuestionaireDataToExecle(List<Map<String,Object>> questions,List<Map<String,Object>> list) throws Exception{
		String[] head = {"客户姓名","联系电话","汽车种类","汽车型号","购买日期","省份","VIN号","回访结果","回访人","备注"};
		String[] head1 = null;
		if(questions!=null && questions.size()>0){
			head1 = new String[questions.size()];
			for(int i=0;i<questions.size();i++){
				head1[i] = CommonUtils.checkNull(questions.get(i).get("QD_QUESTION"));
			}
		}
		List list1 = new ArrayList();
		if(list!=null && list.size()>0){
			for(int i=0;i<list.size();i++){
				Map map = (Map)list.get(i);
				if(map!=null && map.size()>0){
					String [] detail = null;
					if(questions!=null && questions.size()>0){
						detail = new String[10+questions.size()];
					}else{
						detail = new String[10];
					}
					detail[0] = CommonUtils.checkNull(map.get("RV_CUS_NAME"));
					detail[1] = CommonUtils.checkNull(map.get("PHONE"));
					detail[2] = CommonUtils.checkNull(map.get("SERIES_NAME"));
					detail[3] = CommonUtils.checkNull(map.get("MODEL_CODE"));
					detail[4] = CommonUtils.checkNull(map.get("PURCHASED_DATE"));
					detail[5] = CommonUtils.checkNull(map.get("PROVINCE"));
					detail[6] = CommonUtils.checkNull(map.get("VIN"));
					detail[7] = CommonUtils.checkNull(map.get("RV_RESULT"));
					detail[8] = CommonUtils.checkNull(map.get("RV_ASS_USER"));
					detail[9] = CommonUtils.checkNull(map.get("RD_CONTENT"));
					if(questions!=null&&questions.size()>0){
						for(int j=0;j<questions.size();j++){
							detail[10+j] = CommonUtils.checkNull(map.get("Q"+j));
						}
					}
					list1.add(detail);
				}
			}
		}
		this.exportInfo(ActionContext.getContext().getResponse(), ActionContext.getContext().getRequest(), head, head1, list1);
	}
	public static Object exportInfo(ResponseWrapper response,RequestWrapper request, String[] head,String[]head1, List<String[]> list) throws Exception{
		String name = "回访问卷信息.xls";
		WritableWorkbook wwb = null;
		OutputStream out = null;
		try {
			response.setContentType("application/octet-stream");
		    response.addHeader("Content-disposition", "attachment;filename="+URLEncoder.encode(name, "utf-8"));
			out = response.getOutputStream();
			wwb = Workbook.createWorkbook(out);
			WritableSheet ws = wwb.createSheet("sheettest", 0);

			if (head != null && head.length > 0) {
				for (int i = 0; i < head.length; i++) {
					ws.addCell(new Label(i, 0, head[i]));
				}
			}
			if(head1 != null && head1.length>0){
				for(int i=0;i<head1.length;i++){
					ws.addCell(new Label(10+i,0,head1[i]));
				}
			}
			if(list!=null && list.size()>0){
				for (int z = 1; z < list.size() + 1; z++) {
					String[] str = list.get(z - 1);
					for (int i = 0; i < str.length; i++) {
						ws.addCell(new Label(i, z, str[i]));
					}
				}
			}
			wwb.write();
			out.flush();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			if (null != wwb) {
				wwb.close();
			}
			if (null != out) {
				out.close();
			}
		}
		return null;
	}

	/**
	 * 回访问卷导出--修改
	 */
	public void modifyQuestExport()
	{
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		ReviewManageDao dao = ReviewManageDao.getInstance();
		try {
			//获得回访ID
			String RV_ID=CommonUtils.checkNull(request.getParamValue("RV_ID"));
			Map<String,LinkedList<Map<String,Object>>> result = new HashMap();
			//根据ID查询信息
			LinkedList<Map<String,Object>> basicInof =dao.basicInofQuery(RV_ID, logonUser.getUserId());
			result.put("basicInof", basicInof);
			//根据ID查询 问题及答案
			LinkedList<Map<String,Object>> questionInof =dao.questionInofQuery(RV_ID);
			String questionhtml=generateQuestion2(questionInof);
			//根据ID查询工单相关信息
//			LinkedList<Map<String,Object>> roInfo =dao.getRoInfo(RV_ID);
//			result.put("roInfo", roInfo);
			act.setOutData("questionhtml", questionhtml);
			act.setOutData("result",result);
			act.setOutData("RV_ID", RV_ID);
			act.setForword(modifyQuestExport);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"我的客户回访");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 回访问卷导出-修改-保存
	 */
	public void saveAnswer(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		ReviewManageDao dao = ReviewManageDao.getInstance();
		try {
			String rvId=CommonUtils.checkNull(request.getParamValue("RV_ID"));
			String qrId=CommonUtils.checkNull(request.getParamValue("QR_ID"));
			String answers=CommonUtils.checkNull(request.getParamValue("list"));
			dao.saveAnswer(answers,rvId,logonUser,qrId);//保存问题
			act.setOutData("msg", "01");
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.SAVE_FAILURE_CODE,"回访问卷导出");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 
	 * @Title      : 
	 * @Description: 回访进度查询
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-21
	 */
	public void reviewScheduleSearchInit()
	{
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			act.setForword(reviewScheduleSearchInit);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"回访进度查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 
	 * @Title      : 
	 * @Description: 进度查询页面查询 
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-21
	 */
	public void reviewScheduleQuery()
	{
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		ReviewManageDao dao = ReviewManageDao.getInstance();
		try {
				String checkSDate=CommonUtils.checkNull(request.getParamValue("checkSDate"));
				String checkEDate=CommonUtils.checkNull(request.getParamValue("checkEDate"));
				String RV_ASS_USER=CommonUtils.checkNull(request.getParamValue("RV_ASS_USER"));
				Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage")): 1; // 处理当前页
				PageResult<Map<String, Object>> ps = dao.reviewScheduleQuery(Constant.PAGE_SIZE, curPage, RV_ASS_USER,checkSDate,checkEDate);
				act.setOutData("ps", ps);
			
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"回访进度查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 
	 * @Title      : 
	 * @Description: 拨号 
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-9
	 */
	public void dialAction()
	{
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			act.setForword(reviewScheduleSearchInit);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"回访进度查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 
	 * @Title      : 
	 * @Description: 生成查看回访页面问卷 
	 * @param      : @param questionIinfo
	 * @param      : @return      
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-9
	 */
	public String generateQuestion(LinkedList<Map<String,Object>> questionIinfo)
	{
		StringBuilder qh = new StringBuilder();
		qh.append("");
		int i=1;
		for(Map<String, Object> q:questionIinfo)
		{
			String answer=q.get("QD_QUE_ANSWER").toString();
			String disable=answer.equals("null")?"":"disabled";
			String readonly=answer.equals("null")?"":"readonly=\"readonly\"";
			qh.append(" <tr >");
			qh.append("<td width=\"15%\"  align=\"center\"><span >问题 :"+q.get("QD_NO")+"</span></td>");
			qh.append(" <td width=\"85%\"  align=\"left\"><span style=\"DISPLAY:inline-block; \">"+q.get("QD_QUESTION")+"</span><br />");
			qh.append(" <table  border=\"0\"> <tbody> <tr>");
			if(Constant.QD_QUE_TYPE_1==Integer.parseInt(q.get("QD_QUE_TYPE").toString())){
				String[]radiocheck=q.get("QD_CHOICE").toString().split("\\|");
				for(int k=0;k<radiocheck.length;k++)
				{
					qh.append("<td><input id=\"q1"+q.get("QD_NO")+"\" type=\"radio\" "+(answer.equals(radiocheck[k])?"checked":" ")+" value=\""+radiocheck[k]+"\" name=\"ql"+q.get("QD_NO")+"\"/><label>"+radiocheck[k]+"</label></td>");
				}
				String rs = "";
				if(!XHBUtil.IsNull(q.get("QD_QUE_REASON"))) {
					rs = q.get("QD_QUE_REASON").toString();
					qh.append("<td>备注:<input type=\"text\" value=\""+rs+"\" style='width:450px;' name=\"qdQueReason\"/></td>");
				}
			}else if(Constant.QD_QUE_TYPE_2==Integer.parseInt(q.get("QD_QUE_TYPE").toString())){
				String[]radiocheck=q.get("QD_CHOICE").toString().split("\\|");
				for(int k=0;k<radiocheck.length;k++)
				{
					qh.append("<td><input id=\"q1"+q.get("QD_NO")+"\" type=\"checkbox\" "+(answer.indexOf(radiocheck[k])>-1?"checked ":" ")+disable+" value=\""+radiocheck[k]+"\" name=\"ql"+q.get("QD_NO")+"\"/><label>"+radiocheck[k]+"</label></td>");
				}
			}else if(Constant.QD_QUE_TYPE_3==Integer.parseInt(q.get("QD_QUE_TYPE").toString())){
			
				int width=0;
				int hight=20;
				if(Constant.qd_txt_type_1==Integer.parseInt(q.get("QD_TXT_TYPE").toString()))
				{
					width=Integer.parseInt(q.get("ED_TXT_WIDTH").toString());
				}else if(Constant.qd_txt_type_2==Integer.parseInt(q.get("QD_TXT_TYPE").toString())){
					width=Integer.parseInt(q.get("ED_TXT_WIDTH").toString());
					hight=Integer.parseInt(q.get("ED_TXT_HIGHT").toString());
				}
				qh.append("<td><input id=\"q1"+q.get("QD_NO")+"\" type=\"text\" "+readonly+"\"  value=\""+(answer.equals("null")?"":answer)+"\" name=\"ql"+q.get("QD_NO")+"\" style='WIDTH: "+width+"px'/></td>");
				
			}else if(Constant.QD_QUE_TYPE_4==Integer.parseInt(q.get("QD_QUE_TYPE").toString())){
				int width=600;
				int hight=20;
				if(Constant.qd_txt_type_1==Integer.parseInt(q.get("QD_TXT_TYPE").toString()))
				{
					width=Integer.parseInt(q.get("ED_TXT_WIDTH").toString());
				}else if(Constant.qd_txt_type_2==Integer.parseInt(q.get("QD_TXT_TYPE").toString())){
					width=Integer.parseInt(q.get("ED_TXT_WIDTH").toString());
					hight=Integer.parseInt(q.get("ED_TXT_HIGHT").toString());
				}
				qh.append("<td><textarea id=\"q1"+q.get("QD_NO")+"\" type=\"text\" "+readonly+" name=\"ql"+q.get("QD_NO")+"\" style='width: "+width+"px; hight:"+hight+"px;' rows=\"5\"/>"+(answer.equals("null")?"":answer)+"</textarea></td>");
			}else{
				qh.append("");
			}
			qh.append("</tr></tbody> </table></td>");
			qh.append("</tr>");
			i++;
		}
		return qh.toString();
	}
	/**
	 * 
	 * @Title      : 
	 * @Description: 生成回访页面问卷 
	 * @param      : @param questionIinfo
	 * @param      : @return      
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-9
	 */
	public String generateQuestion2(LinkedList<Map<String,Object>> questionIinfo)
	{
		StringBuilder qh = new StringBuilder();
		qh.append("");
		int i=1;
		for(Map<String, Object> q:questionIinfo)
		{
			String answer=q.get("QD_QUE_ANSWER").toString();
			qh.append(" <tr >");
			qh.append("<td width=\"15%\" align=\"center\"><span >问题 :"+q.get("QD_NO")+"</span></td>");
			qh.append(" <td width=\"85%\" align=\"left\"><span style=\"DISPLAY:inline-block;\">"+q.get("QD_QUESTION")+"</span><br />");
			qh.append(" <table  border=\"0\"> <tbody> <tr>");
			if(Constant.QD_QUE_TYPE_1==Integer.parseInt(q.get("QD_QUE_TYPE").toString())){
				String[]radiocheck=q.get("QD_CHOICE").toString().split("\\|");
				if (XHBUtil.IsNull(answer) && radiocheck != null && radiocheck.length > 0) {
					answer = radiocheck[0];
				}
				for(int k=0;k<radiocheck.length;k++)
				{
					qh.append("<td><input id=\"q1"+q.get("QD_NO")+"\" type=\"radio\" "+(answer.equals(radiocheck[k])?"checked":" ")+" value=\""+radiocheck[k]+"\" name=\"ql"+q.get("QD_NO")+"\"/><label>"+radiocheck[k]+"</label></td>");
					//onclick='isShow(\"qt"+q.get("QD_NO")+"\",\""+radiocheck[k]+"\")'
					/*if("不满意".equals(radiocheck[k])){
						String rs = "";
						if(!XHBUtil.IsNull(q.get("QD_QUE_REASON"))) rs = q.get("QD_QUE_REASON").toString();
						if("不满意".equals(answer)){
							qh.append("<td id=\"qt"+q.get("QD_NO")+"\">备注:<input type=\"text\" value=\""+rs+"\" id=\"qr"+q.get("QD_NO")+"\" name=\"qdQueReason\"/></td>");
						}else {
							qh.append("<td style='display:none;' id=\"qt"+q.get("QD_NO")+"\">备注:<input type=\"text\" value=\""+rs+"\" id=\"qr"+q.get("QD_NO")+"\" name=\"qdQueReason\"/></td>");
						}
					}*/
					if(k == radiocheck.length - 1){
						String rs = "";
						if(!XHBUtil.IsNull(q.get("QD_QUE_REASON"))) rs = q.get("QD_QUE_REASON").toString();
						qh.append("<td id=\"qt"+q.get("QD_NO")+"\">备注:<input type=\"text\" value=\""+rs+"\" id=\"qr"+q.get("QD_NO")+"\" style='width:450px;' name=\"qdQueReason\"/></td>");
					}
				}
			}else if(Constant.QD_QUE_TYPE_2==Integer.parseInt(q.get("QD_QUE_TYPE").toString())){
				String[]radiocheck=q.get("QD_CHOICE").toString().split("\\|");
				for(int k=0;k<radiocheck.length;k++)
				{
					qh.append("<td><input id=\"q1"+q.get("QD_NO")+"\" type=\"checkbox\" "+(answer.indexOf(radiocheck[k])>-1?"checked ":" ")+" value=\""+radiocheck[k]+"\" name=\"ql"+q.get("QD_NO")+"\"/><label>"+radiocheck[k]+"</label></td>");
				}
			}else if(Constant.QD_QUE_TYPE_3==Integer.parseInt(q.get("QD_QUE_TYPE").toString())){
			
				int width=0;
				int hight=20;
				if(Constant.qd_txt_type_1==Integer.parseInt(q.get("QD_TXT_TYPE").toString()))
				{
					width=Integer.parseInt(q.get("ED_TXT_WIDTH").toString());
				}else if(Constant.qd_txt_type_2==Integer.parseInt(q.get("QD_TXT_TYPE").toString())){
					width=Integer.parseInt(q.get("ED_TXT_WIDTH").toString());
					hight=Integer.parseInt(q.get("ED_TXT_HIGHT").toString());
				}
				qh.append("<td><input id=\"q1"+q.get("QD_NO")+"\" type=\"text\"   value=\""+(answer.equals("null")?"":answer)+"\" name=\"ql"+q.get("QD_NO")+"\" style='WIDTH: "+width+"px'/></td>");
				
			}else if(Constant.QD_QUE_TYPE_4==Integer.parseInt(q.get("QD_QUE_TYPE").toString())){
				int width=600;
				int hight=20;
				if(Constant.qd_txt_type_1==Integer.parseInt(q.get("QD_TXT_TYPE").toString()))
				{
					width=Integer.parseInt(q.get("ED_TXT_WIDTH").toString());
				}else if(Constant.qd_txt_type_2==Integer.parseInt(q.get("QD_TXT_TYPE").toString())){
					width=Integer.parseInt(q.get("ED_TXT_WIDTH").toString());
					hight=Integer.parseInt(q.get("ED_TXT_HIGHT").toString());
				}
				qh.append("<td><textarea id=\"q1"+q.get("QD_NO")+"\" type=\"text\"  name=\"ql"+q.get("QD_NO")+"\" style='width: "+width+"px; hight:"+hight+"px;' rows=\"5\"/>"+(answer.equals("null")?"":answer)+"</textarea></td>");
			}else{
				qh.append("");
			}
			qh.append("</tr></tbody> </table></td>");
			qh.append("</tr>");
			i++;
		}
		return qh.toString();
	}
	
	public String generateQuestion3(LinkedList<Map<String,Object>> questionIinfo)
	{
		StringBuilder qh = new StringBuilder();
		qh.append("");
		int i=1;
		for(Map<String, Object> q:questionIinfo)
		{
			
			qh.append(" <tr >");
			qh.append("<td  align=\"center\"><span >问题 :"+q.get("QD_NO")+"</span></td>");
			qh.append(" <td  align=\"left\"><span style=\"DISPLAY:inline-block; WIDTH: 90%\">"+q.get("QD_QUESTION")+"</span><br />");
			qh.append(" <table  border=\"0\"> <tbody> <tr>");
			if(Constant.QD_QUE_TYPE_1==Integer.parseInt(q.get("QD_QUE_TYPE").toString())){
				String[]radiocheck=q.get("QD_CHOICE").toString().split("\\|");
				for(int k=0;k<radiocheck.length;k++)
				{
					qh.append("<td><input id=\"q1"+q.get("QD_NO")+"\" type=\"radio\" value=\""+radiocheck[k]+"\" name=\"ql"+q.get("QD_NO")+"\"/><label>"+radiocheck[k]+"</label></td>");
				}
			}else if(Constant.QD_QUE_TYPE_2==Integer.parseInt(q.get("QD_QUE_TYPE").toString())){
				String[]radiocheck=q.get("QD_CHOICE").toString().split("\\|");
				for(int k=0;k<radiocheck.length;k++)
				{
					qh.append("<td><input id=\"q1"+q.get("QD_NO")+"\" type=\"checkbox\"  value=\""+radiocheck[k]+"\" name=\"ql"+q.get("QD_NO")+"\"/><label>"+radiocheck[k]+"</label></td>");
				}
			}else if(Constant.QD_QUE_TYPE_3==Integer.parseInt(q.get("QD_QUE_TYPE").toString())){
			
				int width=0;
				int hight=20;
				if(Constant.qd_txt_type_1==Integer.parseInt(q.get("QD_TXT_TYPE").toString()))
				{
					width=Integer.parseInt(q.get("ED_TXT_WIDTH").toString());
				}else if(Constant.qd_txt_type_2==Integer.parseInt(q.get("QD_TXT_TYPE").toString())){
					width=Integer.parseInt(q.get("ED_TXT_WIDTH").toString());
					hight=Integer.parseInt(q.get("ED_TXT_HIGHT").toString());
				}
				qh.append("<td><input id=\"q1"+q.get("QD_NO")+"\" type=\"text\"   value=\"\" name=\"ql"+q.get("QD_NO")+"\" style='WIDTH: "+width+"px; height:"+hight+"px;'/></td>");
			}else if(Constant.QD_QUE_TYPE_4==Integer.parseInt(q.get("QD_QUE_TYPE").toString())){
				int width=600;
				int hight=20;
				if(Constant.qd_txt_type_1==Integer.parseInt(q.get("QD_TXT_TYPE").toString()))
				{
					width=Integer.parseInt(q.get("ED_TXT_WIDTH").toString());
				}else if(Constant.qd_txt_type_2==Integer.parseInt(q.get("QD_TXT_TYPE").toString())){
					width=Integer.parseInt(q.get("ED_TXT_WIDTH").toString());
					hight=Integer.parseInt(q.get("ED_TXT_HIGHT").toString());
				}
				qh.append("<td><textarea id=\"q1"+q.get("QD_NO")+"\" type=\"text\"  name=\"ql"+q.get("QD_NO")+"\" style='width: "+width+"px; height:"+hight+"px;' /></textarea></td>");
			}else{
				qh.append("");
			}
			qh.append("</tr></tbody> </table></td>");
			qh.append("</tr>");
			i++;
		}
		return qh.toString();
	}
	
	/**
	 * 热线抽查初始化
	 */
	public void hotSpotManageInit(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			act.setForword(hotSpotManage);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"热线抽查");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 查询热线抽查
	 */
	public void hotSpotQuery(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		ReviewManageDao rmDao = ReviewManageDao.getInstance();
		RequestWrapper request = act.getRequest();
		try {
			String dealerCode = request.getParamValue("dealerCode");
			String dealerName = request.getParamValue("dealerName");
			String phone = request.getParamValue("phone");
			String result = request.getParamValue("result");
			String satisfAction = request.getParamValue("satisfAction");
			String beginDate = request.getParamValue("beginDate");
			String endDate = request.getParamValue("endDate");
			Integer pageSize = 10;
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
			PageResult ps = rmDao.queryPageHotSpot(dealerCode, dealerName, phone, result, satisfAction, beginDate, endDate, pageSize, curPage);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"热线抽查");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	public void updateAss(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		ReviewManageDao rmDao = ReviewManageDao.getInstance();
		RequestWrapper request = act.getRequest();
		try {
			String ids = request.getParamValue("ids");
			String assId = request.getParamValue("assId");
			String assName = request.getParamValue("assName");
			if (!XHBUtil.IsNull(ids)) {
				String [] rvIds = ids.split(",");
				if(rvIds != null && rvIds.length > 0){
					for (int i = 0; i < rvIds.length; i++) {
						TtCrmReturnVisitPO oldTp = new TtCrmReturnVisitPO();
						oldTp.setRvId(Long.parseLong(rvIds[i]));
						TtCrmReturnVisitPO newTp = new TtCrmReturnVisitPO();
						newTp.setRvAssUserId(Long.parseLong(assId));
						newTp.setRvAssUser(assName);
						newTp.setUpdateBy(logonUser.getUserId());
						newTp.setUpdateDate(new Date());
						rmDao.update(oldTp, newTp);
					}
				}
			}
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"修改回访人");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
}
