package com.infodms.dms.actions.feedbackmng.exam;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.actions.common.FileUploadManager;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.dao.feedbackMng.ExamDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.FsFileuploadPO;
import com.infodms.dms.po.TmDealerPO;
import com.infodms.dms.po.TtExamDetailPO;
import com.infodms.dms.po.TtExamDlrPO;
import com.infodms.dms.po.TtExamPO;
import com.infodms.dms.util.StringUtil;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;

/**
 * 考试信息管理(OEM)
 * @author Administrator
 *
 */
public class ExamOemAction {
	private ActionContext act = ActionContext.getContext();
	private RequestWrapper req = act.getRequest();
	private Logger logger = Logger.getLogger(ExamOemAction.class);
	private ExamDao dao = ExamDao.getInstance();
	private AclUserBean logonUser = (AclUserBean) act.getSession().get(
			Constant.LOGON_USER);
	
	private final String INIT_URL = "/jsp/feedbackMng/exam/examInfo.jsp" ;
	private final String ADD_URL = "/jsp/feedbackMng/exam/examInfoAdd.jsp" ;
	private final String DETAIL_URL = "/jsp/feedbackMng/exam/examInfoDetail.jsp" ;
	private final String UPDATE_URL = "/jsp/feedbackMng/exam/examInfoModify.jsp" ;
	private final String INIT_MNG_URL = "/jsp/feedbackMng/exam/examInfoMng.jsp" ;
	private final String ANSWER_URL = "/jsp/feedbackMng/exam/examAnswerMng.jsp" ;
	
	private static Integer num = 0001 ;
	/*
	 * 初始化页面跳转
	 */
	public void firstUrlInit(){
		try {
			act.setForword(INIT_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "考试信息维护");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/*
	 * 考试信息管理主页面跳转
	 */
	public void examMngUrlInit(){
		try {
			act.setForword(INIT_MNG_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "考试信息维护");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/*
	 * 新增页面跳转
	 */
	public void addUrlInit(){
		try {
			act.setForword(ADD_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "考试信息新增");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/*
	 * 考试信息维护主页面主查询
	 */
	public void mainQuery(){
		try {
			act.getResponse().setContentType("application/json");
			String examCode = req.getParamValue("examCode");
			String examName = req.getParamValue("examName");
			String beginDate = req.getParamValue("beginDate");
			String endDate = req.getParamValue("endDate");
			String remark = req.getParamValue("examRemark");
			
			StringBuffer sql = new StringBuffer("\n");
			sql.append("select E.EXAM_ID,E.EXAM_CODE,E.EXAM_NAME,E.EXAM_REMARK,E.EXAM_STATUS, \n");
			sql.append("to_char(E.EXAM_START_TIME,'yyyy-MM-dd hh24:mi') as EXAM_START_TIME, \n");
			sql.append("to_char(E.EXAM_END_TIME,'yyyy-MM-dd hh24:mi') as EXAM_END_TIME \n");
			sql.append("from TT_EXAM E \n");
			sql.append("where 1=1 \n");
			//sql.append(" ,FS_FILEUPLOAD F where E.EXAM_ID=F.YWZJ(+) \n");
			if(StringUtil.notNull(examCode))
				sql.append("and E.EXAM_CODE LIKE '%"+examCode+"%' \n");
			if(StringUtil.notNull(examName))
				sql.append("and E.EXAM_NAME LIKE '%"+examName+"%' \n");
			if(StringUtil.notNull(beginDate))
				sql.append("and E.EXAM_START_TIME>=to_date('"+beginDate+" 00:00','yyyy-MM-dd hh24:mi') \n");
			if(StringUtil.notNull(endDate))
				sql.append("and E.EXAM_END_TIME<=to_date('"+endDate+" 23:59','yyyy-MM-dd hh24:mi') \n");
			if(StringUtil.notNull(remark))
				sql.append("and E.EXAM_REMARK LIKE '%"+remark+"%' \n");
			sql.append("order by E.EXAM_CODE DESC \n");
			int pageSize = 10 ;
			int curPage = req.getParamValue("curPage") != null ? Integer
					.parseInt(req.getParamValue("curPage")) : 1; // 处理当前页
			PageResult<Map<String,Object>> ps = dao.queryMainExam(sql.toString(), pageSize, curPage);
			act.setOutData("ps",ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "考试信息维护");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	
	/*
	 * 考试信息管理主页面主查询
	 */
	public void examMngQuery(){
		try{
			act.getResponse().setContentType("application/json");
			String examCode = req.getParamValue("examCode");
			String examName = req.getParamValue("examName");
			String beginDate = req.getParamValue("beginDate");
			String endDate = req.getParamValue("endDate");
			String remark = req.getParamValue("examRemark");
			String[] dealerIds = req.getParamValues("dealerIds");
			
			StringBuffer sql = new StringBuffer("\n");
			sql.append("select d.EXAM_RESULT,d.STATUS,td.DEALER_CODE,td.DEALER_NAME, \n");
			sql.append("td.DEALER_ID,d.EXAM_DLR_ID,E.EXAM_CODE,E.EXAM_NAME, \n");
			sql.append("to_char(e.exam_start_time,'yyyy-MM-dd hh24:mi') as BEGIN_TIME, \n");
			sql.append("to_char(e.exam_end_time,'yyyy-MM-dd hh24:mi') as END_TIME, \n");
			sql.append("to_char(d.submit_time,'yyyy-MM-dd hh24:mi') as SUBMIT_TIME \n");
			sql.append("from tt_exam_dlr d,tm_dealer td,tt_exam E \n");
			sql.append("where d.dealer_id=td.dealer_id \n");
			sql.append("and E.exam_id=d.exam_id \n");
			if(StringUtil.notNull(dealerIds[0])){
				StringBuffer sb = new StringBuffer();
				for(int i = 0 ;i<dealerIds.length;i++){
					sb.append(dealerIds[i]);
					sb.append(",");
				}
				sql.append("and td.dealer_id in ("+sb.substring(0,sb.length()-1).toString()+") \n");
			}
			if(StringUtil.notNull(examCode))
				sql.append("and E.exam_code like '%"+examCode+"%' \n");
			if(StringUtil.notNull(examName))
				sql.append("and E.EXAM_NAME LIKE '%"+examName+"%' \n");
			if(StringUtil.notNull(beginDate))
				sql.append("and E.EXAM_START_TIME>=to_date('"+beginDate+" 00:00','yyyy-MM-dd hh24:mi') \n");
			if(StringUtil.notNull(endDate))
				sql.append("and E.EXAM_END_TIME<=to_date('"+endDate+" 23:59','yyyy-MM-dd hh24:mi') \n");
			if(StringUtil.notNull(remark))
				sql.append("and E.EXAM_REMARK LIKE '%"+remark+"%' \n");
			sql.append("order by E.EXAM_CODE DESC \n");
			int pageSize = 10 ;
			int curPage = req.getParamValue("curPage") != null ? Integer
					.parseInt(req.getParamValue("curPage")) : 1; // 处理当前页
			PageResult<Map<String,Object>> ps = dao.queryMainAnswer(sql.toString(), pageSize, curPage);
			act.setOutData("ps",ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "考试信息管理");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/*
	 * 考试代码生成规则
	 * ks+yyyymmdd+0000
	 */
	public synchronized String codeGen(){
		SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd");
		String dStr = fmt.format(new Date());
		StringBuffer sb = new StringBuffer("ks");
		sb.append(dStr);
		String last ;
		String sql = "select * from tt_exam where exam_code like 'KS"+dStr+"%' order by exam_code desc" ;
		List<TtExamPO> list = dao.select(TtExamPO.class,sql,null);
		if(list.size()>0){
			last = String.valueOf((Integer.parseInt(list.get(0).getExamCode().substring(10,14))+1));
			if(last.length()==1)
				last = "000"+last;
			if(last.length()==2)
				last = "00"+last;
			if(last.length()==3)
				last = "0"+last;
		}
		else last = "0001" ;
		sb.append(last);
		return sb.toString() ;
	}
	
	/*
	 * 考试信息新增
	 */
	public void addExamInfo(){
		try{
			String examName = req.getParamValue("examName");
			String beginDate = req.getParamValue("beginDate");
			String endDate = req.getParamValue("endDate");
			String examRemark = req.getParamValue("examRemark");
			
			SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			Date bd = fmt.parse(beginDate);
			Date ed = fmt.parse(endDate);
			
			// 新增考试主表信息
			TtExamPO po = new TtExamPO();
			po.setExamId(Utility.getLong(SequenceManager.getSequence("")));
			po.setExamName(examName);
			po.setExamRemark(examRemark);
			po.setExamStartTime(bd);
			po.setExamEndTime(ed);
			po.setExamCode(this.codeGen());
			po.setExamStatus(Constant.STATUS_DISABLE);
			po.setCreateBy(logonUser.getUserId());
			po.setCreateDate(new Date());
			dao.addExam(po);
			
			// 附件管理功能
			String ywzj = po.getExamId().toString();
			String[] fjids = req.getParamValues("fjid");
			FileUploadManager.fileUploadByBusiness(ywzj, fjids, logonUser);
			
			act.setRedirect(INIT_URL);
		} catch(Exception e){
			BizException e1 = new BizException(act,e,
					ErrorCodeConstant.ADD_FAILURE_CODE,"考试信息添加");
			logger.error(logonUser,e1);
			act.setException(e1);			
		}
	}
	
	
	/*
	 * 根据EXAM_ID查询考试信息明细.以供修改/显示
	 */
	public void queryExamInfo(){
		try{
			Long examId = Long.parseLong(req.getParamValue("id"));
			String type = req.getParamValue("type");
			
			TtExamPO exam = new TtExamPO();
			exam.setExamId(examId);
			exam = dao.queryExamById(exam) ;
			
			FsFileuploadPO detail = new FsFileuploadPO();
			detail.setYwzj(examId);
			List<FsFileuploadPO> lists = dao.select(detail);
			
			act.setOutData("exam",exam);
			act.setOutData("lists", lists);
			
			if("3".equals(type))
				act.setOutData("dlr", true);
			if("1".equals(type))
				act.setForword(DETAIL_URL);
			else if("2".equals(type))
				act.setForword(UPDATE_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "考试信息明细查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/*
	 * 根据EXAM_DLR_ID查询答案明细,以供显示/收卷
	 */
	public void queryAnswerInfo(){
		try{
			Long dlrId = Long.parseLong(req.getParamValue("dlrId"));
			
			TtExamDlrPO dpo = new TtExamDlrPO();
			dpo.setExamDlrId(dlrId);
			dpo = (TtExamDlrPO)dao.select(dpo).get(0);
			
			TtExamPO epo = new TtExamPO();
			epo.setExamId(dpo.getExamId());
			epo = (TtExamPO)dao.select(epo).get(0);
			
			TmDealerPO dlr = new TmDealerPO();
			dlr.setDealerId(dpo.getDealerId());
			dlr = (TmDealerPO)dao.select(dlr).get(0);
			
			FsFileuploadPO fpo = new FsFileuploadPO();
			fpo.setYwzj(dpo.getExamDlrId());
			List<FsFileuploadPO> lists = dao.select(fpo);
			
			act.setOutData("examDlr", dpo);
			act.setOutData("exam", epo);
			act.setOutData("dealer", dlr);
			act.setOutData("lists", lists);
			
			act.setForword(ANSWER_URL);
		} catch(Exception e){
			BizException be = new BizException(act,e,
					ErrorCodeConstant.QUERY_FAILURE_CODE,"答案信息查询");
			logger.error(logonUser,be);
			act.setException(be);
		}
	}
	
	/*
	 * 删除考试附件表信息
	 */
	public void deleteDetail(){
		try{
			act.getResponse().setContentType("application/json");
			Long detailId = Long.parseLong(req.getParamValue("id"));
			String idx = req.getParamValue("idx");
			
			TtExamDetailPO po = new TtExamDetailPO();
			po.setDetailId(detailId);
			dao.deleteDetailById(po);
			
			act.setOutData("idx", idx);
			act.setOutData("flag", true);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,
					ErrorCodeConstant.DELETE_FAILURE_CODE, "考试信息明细删除");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/*
	 * 对考试信息所做的修改进行保存
	 */
	public void updateExamInfo(){
		try{
			Long examId = Long.parseLong(req.getParamValue("examId"));
			String beginDate = req.getParamValue("beginDate");
			String endDate = req.getParamValue("endDate");
			String examName = req.getParamValue("examName");
			String examRemark = req.getParamValue("examRemark");
			Integer examStatus = Integer.parseInt(req.getParamValue("status"));
			
			SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			Date bd = fmt.parse(beginDate);
			Date ed = fmt.parse(endDate);
			
			// 考试主表信息修改
			TtExamPO e1 = new TtExamPO();
			e1.setExamId(examId);
			TtExamPO e2 = dao.queryExamById(e1);
			e2.setExamName(examName);
			e2.setExamRemark(examRemark);
			e2.setExamStatus(examStatus);
			e2.setExamStartTime(bd);
			e2.setExamEndTime(ed);
			dao.update(e1, e2);
			
			// 附件管理功能
			String ywzj = e2.getExamId().toString();
			String[] fjids = req.getParamValues("fjid");
			FileUploadManager.delAllFilesUploadByBusiness(ywzj, fjids);
			FileUploadManager.fileUploadByBusiness(ywzj, fjids, logonUser);
			
			act.setForword(INIT_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,
					ErrorCodeConstant.DELETE_FAILURE_CODE, "考试信息修改");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
}
