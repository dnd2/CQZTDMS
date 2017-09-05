package com.infodms.dms.actions.feedbackmng.exam;

import java.util.ArrayList;
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
import com.infodms.dms.po.TtExamDlrDetailPO;
import com.infodms.dms.po.TtExamDlrPO;
import com.infodms.dms.po.TtExamPO;
import com.infodms.dms.util.StringUtil;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;

/**
 * 考试信息管理(DLR)
 * @author Administrator
 *
 */
public class ExamDlrAction {
	private ActionContext act = ActionContext.getContext();
	private Logger logger = Logger.getLogger(ExamDlrAction.class);
	private RequestWrapper req = act.getRequest() ;
	private AclUserBean logonUser = (AclUserBean) act.getSession().get(
			Constant.LOGON_USER);
	
	private final String INIT_URL = "/jsp/feedbackMng/exam/examInfoDlr.jsp" ;
	private final String DETAIL_URL = "/jsp/feedbackMng/exam/examInfoDetailDlr.jsp" ;
	
	private ExamDao dao = ExamDao.getInstance();
	
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
			sql.append("to_char(E.EXAM_END_TIME,'yyyy-MM-dd hh24:mi') as EXAM_END_TIME, \n");
			sql.append("(case when (select count(*) from tt_exam_dlr d\n");
			sql.append("where d.dealer_id = ").append(logonUser.getDealerId()).append("\n");
			sql.append("and d.exam_id = e.exam_id) > 0 then 1 else 0 end) status\n");
			sql.append("from TT_EXAM E \n");
			sql.append("where 1=1 \n");
			sql.append("and e.exam_status = ").append(Constant.STATUS_ENABLE).append("\n");
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
			sql.append("and e.exam_end_time>sysdate\n");
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
	 * 根据EXAM_ID查询明细.以供修改/显示
	 */
	public void queryExamInfo(){
		try{
			Long examId = Long.parseLong(req.getParamValue("id"));
			
			TtExamPO exam = new TtExamPO();
			exam.setExamId(examId);
			exam = dao.queryExamById(exam) ;
			
			FsFileuploadPO detail = new FsFileuploadPO();
			detail.setYwzj(examId);
			List<FsFileuploadPO> lists = dao.select(detail);
			
			TtExamDlrPO po = new TtExamDlrPO();
			po.setDealerId(Long.parseLong(logonUser.getDealerId()));
			po.setExamId(examId);
			if(dao.select(po).size()>0)
				act.setOutData("flag",1);
			
			act.setOutData("exam",exam);
			act.setOutData("lists", lists);
			
			act.setForword(DETAIL_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "考试信息明细查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/*
	 * 答案保存
	 */
	public void saveAnswer(){
		try{
			Long examId = Long.parseLong(req.getParamValue("examId"));
			TtExamDlrPO answer = new TtExamDlrPO();
			answer.setCreateBy(logonUser.getUserId());
			answer.setCreateDate(new Date());
			answer.setExamDlrId(Utility.getLong(SequenceManager.getSequence("")));
			answer.setExamId(examId);
			answer.setSubmitTime(new Date());
			answer.setStatus(Constant.STATUS_ENABLE);
			answer.setDealerId(Long.parseLong(logonUser.getDealerId()));
			dao.insert(answer);
			
			// 答案附件管理
			String ywzj = answer.getExamDlrId().toString();
			String[] fjids = req.getParamValues("fjid");
			FsFileuploadPO f = new FsFileuploadPO();
			f.setYwzj(examId);
			
			List<String> ids = new ArrayList<String>();
			List lists = dao.select(f);
			
			// 答案ID
			TtExamDlrDetailPO dpo = new TtExamDlrDetailPO();
			for(int i=lists.size();i<fjids.length;i++){
				ids.add(fjids[i]);
				
				// 同时添加TT_EXAM_DLR_DETAIL表信息
				dpo.setExamDlrDetailId(Utility.getLong(SequenceManager.getSequence("")));
				dpo.setExamDlrId(answer.getExamDlrId());
				dpo.setDetailId(Long.parseLong(fjids[i]));
				dpo.setCreateBy(logonUser.getUserId());
				dpo.setCreateDate(new Date());
				dao.insert(dpo);
			}
			if(ids.size()>0)
				FileUploadManager.fileUploadByBusiness2(ywzj, ids, logonUser); 
			
			act.setForword(INIT_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,
					ErrorCodeConstant.ADD_FAILURE_CODE, "考试答案提交");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
}
