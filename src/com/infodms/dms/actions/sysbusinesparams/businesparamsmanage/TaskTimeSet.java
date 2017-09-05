package com.infodms.dms.actions.sysbusinesparams.businesparamsmanage;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.dao.sysbusinesparams.businesparamsmanage.TaskTimeSetDAO;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.dms.chana.po.TaskPlanPO;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

/**   
 * @Title  : TaskTimeSet.java
 * @Package: com.infodms.dms.actions.sysbusinesparams.businesparamsmanage
 * @Description: 定时任务时间设置
 * @date   : 2010-7-5 
 * @version: V1.0   
 */
public class TaskTimeSet extends BaseDao<PO>{
	public Logger logger = Logger.getLogger(TaskTimeSet.class);
	private ActionContext act = ActionContext.getContext();
	RequestWrapper request = act.getRequest();
	private final String  TASK_TIME_INIT = "/jsp/sysbusinesparams/businesparamsmanage/taskTimeInit.jsp";
	private final String  TASK_TIME_SET = "/jsp/sysbusinesparams/businesparamsmanage/taskTimeSet.jsp";
	
	/** 
	* @Title	  : taskTimeInit 
	* @Description: 定时任务时间设置初始化
	* @throws 
	* @create :2013-11-1
	*/
	public void taskTimeInit(){
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);	
			act.setForword(TASK_TIME_INIT);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "定时任务时间设置初始化");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	} 
	
	/** 
	* @Title	  : taskTimeQuery 
	* @Description: 定时任务时间设置列表
	* @throws 
	* @create :2013-11-1
	*/
	public void taskTimeQuery(){
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);	
			String taskName = CommonUtils.checkNull(request.getParamValue("taskName")); // 定时任务名称
			/******************************页面查询字段end***************************/
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("taskName", taskName);
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
			PageResult<Map<String, Object>> ps = TaskTimeSetDAO.getInstance().getTaskQuery(map, Constant.PAGE_SIZE, curPage);
			act.setOutData("ps", ps);
		}catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "定时任务时间设置列表");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/** 
	* @Title	  : taskTimeSetInit 
	* @Description:定时任务时间设置修改 初始化
	* @throws 
	* @create :2013-11-1
	*/
	public void taskTimeSetInit(){
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);	
			String plan_id = CommonUtils.checkNull(request.getParamValue("plan_id"));
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("plan_id", plan_id);
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
			PageResult<Map<String, Object>> ps = TaskTimeSetDAO.getInstance().getTaskQuery(map, Constant.PAGE_SIZE, curPage);
			Map<String, Object> valueMap=ps.getRecords().get(0);
			act.setOutData("valueMap", valueMap);
			act.setForword(TASK_TIME_SET);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "定时任务时间设置修改 初始化 ");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/** 
	* @Title	  : saveTaskTime 
	* @Description:定时任务时间设置修改 
	* @throws 
	* @create :2013-11-1
	*/
	public void saveTaskTime(){
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);	
			String plan_id = CommonUtils.checkNull(request.getParamValue("PLAN_ID"));
			String task_interval = CommonUtils.checkNull(request.getParamValue("TASK_INTERVAL"));
			String plan_repeat_type = CommonUtils.checkNull(request.getParamValue("PLAN_REPEAT_TYPE"));
			TaskPlanPO se= new TaskPlanPO();
			se.setPlanId(Long.parseLong(plan_id));
			TaskPlanPO po= new TaskPlanPO();
			po.setTaskInterval(Integer.parseInt(task_interval));
			po.setPlanRepeatType(plan_repeat_type);
			TaskTimeSetDAO.getInstance().update(se, po);
			act.setOutData("returnValue", 1);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "定时任务时间设置修改 ");
			logger.error(logonUser,e1);
			act.setOutData("returnValue", 2);
			act.setException(e1);
		}
	}
	
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}
}
