/**********************************************************************
* <pre>
* FILE : ClaimAuditingComponent.java
* CLASS : ClaimAuditingComponent
* AUTHOR : XZM
* FUNCTION : 索赔申请自动审核组件
*            1、加载 自动审核基本参数
*            2、加载 未审核的索赔单数据
*======================================================================
* CHANGE HISTORY LOG
*----------------------------------------------------------------------
* MOD. NO.| DATE     | NAME | REASON | CHANGE REQ.
*----------------------------------------------------------------------
*         |2010-06-05| XZM  | Created |
* DESCRIPTION:
* </pre>
***********************************************************************/
/**
 * $Id: ClaimAuditingComponent.java,v 1.1 2010/08/16 01:44:08 yuch Exp $
 */
package com.infodms.dms.actions.claim.auditing;

import java.util.Map;

import com.infodms.dms.actions.claim.auditing.rule.fixation.UntreadAuditingRule16;
import com.infoservice.mvc.component.Component;

/**
 * 索赔申请自动审核组件
 * 功能：加载 自动审核基本参数
 *       加载 未审核的索赔单数据
 * @author XZM
 */
public class ClaimAuditingComponent implements Component{

	public void destroy() {
	}

	public void init(Map<String, String> arg0) {
		ClaimAuditingProxy proxy = new ClaimAuditingProxy();
		
		Integer poolSize = null;
		Integer queueSize = null;
		Long keepAliveTime = null;
		String isOpen = null;
		
		
		if(arg0.containsKey("poolSize"))//线程池大小
			poolSize = Integer.parseInt(arg0.get("poolSize"));
		if(arg0.containsKey("queueSize"))//任务队列大小
			queueSize = Integer.parseInt(arg0.get("queueSize"));
		if(arg0.containsKey("keepAliveTime"))//任务注销等待时间
			keepAliveTime = Long.parseLong(arg0.get("keepAliveTime"));
		if(arg0.containsKey("isOpen"))//任务注销等待时间
			isOpen = arg0.get("isOpen");
		if(arg0.containsKey("limitMonths")){//初始化 索赔申请审核月数限制
			String temp = arg0.get("limitMonths");
			if(temp!=null && !"".equals(temp)){
				Integer limit = Integer.parseInt(temp);
				UntreadAuditingRule16.months = limit;
			}
		}
		
		//初始化参数，加载并审核索赔单
		if("true".equalsIgnoreCase(isOpen))
		    proxy.init(poolSize, queueSize, keepAliveTime);
	}

}
