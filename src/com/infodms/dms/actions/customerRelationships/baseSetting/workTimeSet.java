package com.infodms.dms.actions.customerRelationships.baseSetting;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.actions.util.CommonUtilActions;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.dao.customerRelationships.WorkTimeSetDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TtCrmWorktimePO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.DateTimeUtil;
import com.infodms.dms.util.StringUtil;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;
/**
 * 
 * @ClassName     : workTimeSet 
 * @Description   : 基础设定下的班次类型设置
 * @author        : wangming
 * CreateDate     : 2013-4-1
 */
public class workTimeSet {
	private static Logger logger = Logger.getLogger(workTimeSet.class);
	// 班次类型设定初始化页面
	private final String workTimeSetUrl = "/jsp/customerRelationships/baseSetting/workTimeSet.jsp";
	//班次类型设定新增页面
	private final String workTimeSetAddUrl = "/jsp/customerRelationships/baseSetting/workTimeSetAdd.jsp";

	ActionContext act = ActionContext.getContext();
	AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
	RequestWrapper request = act.getRequest();
	
	
	/**
	 * 班次类型设定初始化
	 */
	public void workTimeSetInit(){		
		try{
			CommonUtilActions commonUtilActions = new CommonUtilActions();
			// 初始化 班次类型到页面
			act.setOutData("sts", commonUtilActions.getTcCode(Constant.SHIFT_TIMES.toString()));
			act.setForword(workTimeSetUrl);
		}catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ACTION_NAME_ERROR_CODE,"班次类型设定");
			logger.error(logger,e1);
			act.setException(e1);
		}
	}
	
	public void queryWorkTimeSet(){
		act.getResponse().setContentType("application/json");
		try{
			
			WorkTimeSetDao dao = WorkTimeSetDao.getInstance();
			
			CommonUtilActions commonUtilActions = new CommonUtilActions();
			// 初始化 班次类型到页面
			act.setOutData("sts", commonUtilActions.getTcCode(Constant.SHIFT_TIMES.toString()));
			
			//分页方法 begin
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage"))	: 1; // 处理当前页	
				
			PageResult<Map<String,Object>> workTimeSetData = dao.queryWorkTimeSet(Constant.PAGE_SIZE,curPage);
			
			act.setOutData("ps", workTimeSetData);
		} catch(Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"班次类型查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 
	 * @Title      : 班次类型设定增加
	 * @Description: 班次类型设定增加 
	 * LastDate    : 2013-4-1
	 */
	public void addWorkTimeSet(){
		
		CommonUtilActions commonUtilActions = new CommonUtilActions();
		// 初始化 班次类型到页面
		act.setOutData("sts", commonUtilActions.getTcCode(Constant.SHIFT_TIMES.toString()));
		
		ActionContext act = ActionContext.getContext();
		String [] hourStr = {"1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19"};
		String [] minStr = {"00","05","55"};
		act.setOutData("hourStr", hourStr);
		act.setOutData("minStr", minStr);
		
		String id = request.getParamValue("id");
		if(id!=null&&!"".equals(id)){
			WorkTimeSetDao dao = WorkTimeSetDao.getInstance();
			TtCrmWorktimePO ttCrmWorktimePO = new TtCrmWorktimePO();
			ttCrmWorktimePO.setWtId(Long.valueOf(id));
			TtCrmWorktimePO ttCrmWorktimePO2 = dao.queryTtCrmWorktimePOById(ttCrmWorktimePO);
			act.setOutData("ttCrmWorktimePO", ttCrmWorktimePO2);
		}
		
		act.setForword(workTimeSetAddUrl);
	}
	/**
	 * 
	 * @Title      : 新增班次类型提交
	 * @Description: TODO 新增班次类型提交
	 * LastDate    : 2013-4-1
	 */
	public void addWorkTimeSetSubmit(){
		// 班次类型信息
		String wtType = CommonUtils.checkNull(request.getParamValue("wtType"));  				//设置班次类型
		String workAmStartHourTime = CommonUtils.checkNull(request.getParamValue("workAmStartHourTime"));   //上班时钟
		String workAmStartMinTime = CommonUtils.checkNull(request.getParamValue("workAmStartMinTime"));    //上班分钟
		String workPmEndHourTime = CommonUtils.checkNull(request.getParamValue("workPmEndHourTime"));     //下班时钟
		String workPmEndMinTime = CommonUtils.checkNull(request.getParamValue("workPmEndMinTime"));      //下班分钟
		
		WorkTimeSetDao dao = WorkTimeSetDao.getInstance();
		
		TtCrmWorktimePO ttCrmWorktimePO = new TtCrmWorktimePO();
		TtCrmWorktimePO ttCrmWorktimePO2 = new TtCrmWorktimePO();
		
		String id = request.getParamValue("id");
		if(id!=null&&!"".equals(id)){
			ttCrmWorktimePO2.setWtId(Long.valueOf(id));
			ttCrmWorktimePO.setUpdateBy(logonUser.getUserId());
			ttCrmWorktimePO.setUpdateDate(new Date());
		}else{		
			ttCrmWorktimePO.setWtId(new Long(SequenceManager.getSequence("")));
			ttCrmWorktimePO.setCreateBy(logonUser.getUserId());
			ttCrmWorktimePO.setCreateDate(new Date());
		}
		ttCrmWorktimePO.setWtType(Integer.parseInt(wtType));// 班次类型
		ttCrmWorktimePO.setWtEndOffMinute(Integer.parseInt(workPmEndMinTime));//结束下班时钟
		ttCrmWorktimePO.setWtEndOnMinute(Integer.parseInt(workPmEndHourTime));//结束上班时钟
		ttCrmWorktimePO.setWtStaOffMinute2(Integer.parseInt(workAmStartMinTime));//开始下班时钟
		ttCrmWorktimePO.setWtStaOnMinute(Integer.parseInt(workAmStartHourTime));//开始上班时钟
		
		
		if(id!=null&&!"".equals(id)){
			
		StringBuffer sql= new StringBuffer();
		sql.append(" UPDATE TT_CRM_SORT_SHIFT A set  A.END_DATE =  to_date(to_char(A.END_DATE,'yyyy-mm-dd')|| ' ' || '"+workPmEndHourTime+":"+workPmEndMinTime+"', 'yyyy-mm-dd hh24:mi' )\n" );
		sql.append(" where to_char(A.DUTY_DATE,'yyyy-mm-dd') = to_char(SYSDATE,'yyyy-mm-dd')    and A.WT_TYPE ="+wtType+" \n" );
		sql.append(" and  A.END_DATE < to_date(to_char(A.END_DATE,'yyyy-mm-dd')|| ' ' || '"+workPmEndHourTime+":"+workPmEndMinTime+"', 'yyyy-mm-dd hh24:mi' )");
		dao.update(sql.toString(), null);
		
		sql= new StringBuffer();
		sql.append(" UPDATE TT_CRM_SORT_SHIFT A  set  A.END_DATE =  to_date(to_char(A.END_DATE,'yyyy-mm-dd')|| ' ' || '"+workPmEndHourTime+":"+workPmEndMinTime+"', 'yyyy-mm-dd hh24:mi' ),\n" );
		sql.append(" A.STA_DATE = to_date(to_char(A.STA_DATE,'yyyy-mm-dd')|| ' ' || '"+workAmStartHourTime+":"+workAmStartMinTime+"', 'yyyy-mm-dd hh24:mi' )\n" );
		sql.append(" where A.DUTY_DATE >  SYSDATE  and A.WT_TYPE ="+wtType+" ");
		dao.update(sql.toString(), null);

			
			
			dao.update(ttCrmWorktimePO2, ttCrmWorktimePO);
		}else{
			dao.insert(ttCrmWorktimePO);
		}
		act.setOutData("success", "true");
	}

}