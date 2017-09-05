package com.infodms.dms.actions.schedule;

import com.infodms.dms.dao.schedule.TaskDao;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.po3.core.context.DBService;
import com.infoservice.po3.core.context.POContext;
import com.infoservice.po3.core.util.ContextUtil;
import com.infoservice.schedule.po.pos.TaskInfoPO;
import com.infoservice.schedule.po.pos.TaskPlanPO;
import com.infoservice.schedule.Constant;
public class TaskAction {
	private TaskDao taskDao = TaskDao.getInstance();
	
	public static void main(String[] args) {
		ContextUtil.loadConf();
		POContext.beginTxn(DBService.getInstance().getDefTxnManager(), -1);
		TaskAction action = new TaskAction();
		action.test();
		POContext.endTxn(true);
	}
	
	public void test() {
		TaskInfoPO taskInfo = addTaskInfo();
		TaskPlanPO taskPlan = addTaskPlan(taskInfo);
		taskDao.insert(taskInfo);
		taskDao.insert(taskPlan);
	}
	
	private TaskInfoPO addTaskInfo() {
		TaskInfoPO po = new TaskInfoPO();
		po.setTaskId(taskDao.getLongPK(po));
		//设置要运行定时任务的Action的全路径
		po.setActionId("com.infoservice.dms.chana.actions.FromERPTask");
		//设置Action名字
		po.setActionName("FromERPTask");
		//Action描述
		po.setActionDesc("从ERP交换到DCS端");
		//???
		po.setTaskType("USER");
		//设置任务状态为有效
		po.setTaskStatus(Constant.STATUS_1);
		//设置任务优先级
		po.setTaskPriority(5);
		//设定任务控制方式
		po.setTaskManual("SYSTEM");
		//设置数据库超时时间
		po.setTaskDuration(180000);
		return po;
	}
	
	private TaskPlanPO addTaskPlan(TaskInfoPO taskInfo) {
		TaskPlanPO po = new TaskPlanPO();
		po.setPlanId(taskDao.getLongPK(po));
		//Task_Info ID
		po.setTaskId(taskInfo.getTaskId());
		//设置计划的优先级
		po.setPlanPriority(5);
		//设置计划开始时间
		po.setPlanStart("00:00");
		//设置计划结束时间
		po.setPlanEnd("23:59");
		po.setPlanRepeatType("HOUR");
		//TASK_IGNORE_FLAG_0: 设置任务可跳过  TASK_IGNORE_FLAG_1: 不可跳过   
		po.setPlanIgnoreFlag(Constant.TASK_IGNORE_FLAG_0);
		//设置任务启动超时时间
		po.setPlanStartTimeout(5);
		//任务跳过后执行类型为不适用
		po.setPlanRunType(-1);
		//设置时间间隔
		po.setTaskInterval(6);
		//状态为有效
		po.setPlanStatus(Constant.STATUS_1);
		return po;
	}
}
