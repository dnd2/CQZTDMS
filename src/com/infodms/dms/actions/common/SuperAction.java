package com.infodms.dms.actions.common;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;

/**
 * 所有ACTION可以调用的父类<br/>
 * <p>主要目的：</p>
 * <p>1、简化页面对ActionContext，RequestWrapper，ResponseWrapper以及AclUserBean的重复初始化代码</p>
 * <p>
 * 	  2、提供页面正在加载的提示框，使用方式需要<br/>
 * 		 act.setOutData("processDesc", "正在加载预测数据,请等待...");<br/>
 * 		 act.setOutData("forwordPath","");<br/>
 * 	  3、提供请求常规异常提示界面的转发<br/>
 * 		 act.setOutData("messageDesc", "错误信息");<br/>
 * 		 act.setOutData("forwordPath","");<br/>
 * </p>
 * 
 * @author wangsw
 */
public abstract class SuperAction {
	public Logger logger = Logger.getLogger(SuperAction.class);
	public ActionContext act = ActionContext.getContext();
	public RequestWrapper request = act.getRequest();
	public ResponseWrapper response = act.getResponse();
	public AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
	
	public static final String LOADING_URL = "/process.jsp";
	public static final String ERROR_URL = "/custom_error.jsp";
	
}
