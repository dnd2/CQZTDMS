package com.infodms.dms.dao.sysbusinesparams.businesparamsmanage;

import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

/**   
 * @Title  : TaskTimeSetDAO.java
 * @Package: com.infodms.dms.dao.sysbusinesparams.TaskTimeSetDAO
 * @Description: TODO(用一句话描述该文件做什么)
 * @date   : 2013-11-1
 * @version: V1.0   
 */
public class TaskTimeSetDAO extends BaseDao{
	public Logger logger = Logger.getLogger(TaskTimeSetDAO.class);
	private ActionContext act = ActionContext.getContext();
	private static final TaskTimeSetDAO dao = new TaskTimeSetDAO ();
	public static final TaskTimeSetDAO getInstance() {
		return dao;
	}
	
	/** 
	* @Title	  : getTaskQuery 
	* @Description: 定时任务列表
	* @throws 
	* @LastUpdate :2011-11-1
	*/
	public static PageResult <Map<String,Object>> getTaskQuery(Map<String,Object> map,int pageSize,int curPage){
		String taskName =CommonUtils.checkNull((String)map.get("taskName")); // 定时任务名称
		String plan_id =CommonUtils.checkNull((String)map.get("plan_id")); // 计划名称
		List<Object> params = new LinkedList<Object>();
		StringBuffer sbSql = new StringBuffer("");
		sbSql.append("SELECT A.TASK_ID,B.PLAN_ID,A.ACTION_DESC, B.TASK_INTERVAL,DECODE(B.PLAN_REPEAT_TYPE,'MINUTE','分','HOUR','小时','DAY','天') PLAN_REPEAT_TYPE_NAME,B.PLAN_REPEAT_TYPE\n");
		sbSql.append("  FROM TASK_INFO A, TASK_PLAN B\n");
		sbSql.append(" WHERE A.TASK_ID = B.TASK_ID\n");
		sbSql.append("   AND B.PLAN_STATUS = 1\n"); 
		if(!taskName.equals("")){
			sbSql.append("AND A.ACTION_DESC like ?\n");
			params.add("%"+taskName+"%");
		}
		if(!plan_id.equals("")){
			sbSql.append("AND B.PLAN_ID = ?\n");
			params.add(plan_id);
		}
		sbSql.append("   ORDER BY B.PLAN_REPEAT_TYPE DESC\n"); 
		return dao.pageQuery(sbSql.toString(), params, dao.getFunName(),pageSize ,curPage);
	}
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}
}
