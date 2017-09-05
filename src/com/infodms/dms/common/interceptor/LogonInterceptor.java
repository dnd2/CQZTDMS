package com.infodms.dms.common.interceptor;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.infodms.dms.actions.common.UserManager;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.common.login.HandleLogonInterceptor;
import com.infodms.dms.dao.common.CommonDAO;
import com.infodms.dms.dao.sales.dealer.DealerInfoDao;
import com.infodms.dms.dao.sysbusinesparams.businesparamsmanage.SysParameterManageDAO;
import com.infodms.dms.po.TcUserPO;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.ActionContextExtend;
import com.infoservice.mvc.context.SessionWrapper;
import com.infoservice.mvc.impl.ActionInvocation;
import com.infoservice.mvc.impl.Dispatcher;
import com.infoservice.mvc.interceptor.Interceptor;

/**
 * @author ZhaoLi
 * 
 */
public class LogonInterceptor implements Interceptor {

	private static Logger log = LogManager.getLogger(LogonInterceptor.class);
	private static String logonUser = null; // session放置用户信息的key
	// added by andy.ten@tom.com
	public static String sessionValidate = "1";// session是否校验
	public static String innerUrl = null; // 外网url
	public static String outUrl = null; // 内网url
	public static String logoutUrl = null; // 退出url
	public static String firstPage = "firstPage";
	public static String rejectPermission = "rejectPermission" ;//权限校验提示
	public static final String ERROR_PAGE = "error";
	public static final String RPCERROR_PAGE = "rpcError";
	public static final String REPEAT_PAGE = "repeatPage";// 重复提交提示页面
	public static String sessionInvalid = "sessionInvalid";
	public static final String LOGIN_ERROR = "loginError";// 接口登录错误页面

	public boolean accept() {

		ActionContextExtend ace = (ActionContextExtend) ActionContext
				.getContext();
		Dispatcher dispatcher = Dispatcher.getInstance();

		return dispatcher.accept(ace.getRequestURI());
	}

	public void destroy() {
	}

	public void init(Map<String, String> params) {
		logonUser = params.get("paramName");
		innerUrl = params.get("innerUrl");
		outUrl = params.get("outerUrl");
		logoutUrl = params.get("logoutUrl");
		sessionValidate = params.get("sessionValidate");
	}

	public String intercept(ActionInvocation invocation) {
		ActionContextExtend atx = (ActionContextExtend) ActionContext
				.getContext();
		if(atx.getRequestURI().indexOf("crmphone")>=0) {
//			atx.setForword(firstPage);
			invocation.invoke();
			return "OK";
		}
		log.info(atx.getRequestURI());
		String userChange = atx.getRequest().getParamValue("userchange");// 用户切换功能判断标
		if ("1".equals(userChange)) {
			atx.getSession().remove(Constant.LOGON_USER);
		}
		try {
			SessionWrapper session = atx.getSession();
			if(session.get("loginMap")==null){
				session.set("loginMap", new HashMap());
			}
			String anct = atx.getRequest().getParamValue("userName");

			if("".equals(anct)) {
				atx.setOutData("ERROR_MESSAGE", "账号必须输入！");
				atx.setForword(firstPage);
				return "OK";
			}
			
			AclUserBean usr = (AclUserBean) session.get(logonUser);
			String rFlag = atx.getRequest().getParamValue("rFlag");
			if(anct!=null&&atx.getRequestURI().endsWith("common/UserManager/login.do")){
				
				TcUserPO tup = UserManager.getUser(anct);
				if(tup!=null){
					if(tup.getIsLock()==1){
						atx.setOutData("ERROR_MESSAGE", "账号"+anct+"已被锁定，请联系系统管理员");
						atx.setForword(firstPage);
						return "OK";
					}
					//判断该用户是否创建好后，3天未登陆 ranke 20150409
					if(tup.getIsFirst() == 1 && tup.getLastsigninTime() == null && (tup.getUserType().toString()).equals(Constant.SYS_USER_DEALER.toString())){//表示首次登陆
						Date createDate = tup.getCreateDate();
						Calendar c = Calendar.getInstance();  
				        c.setTime(createDate);   //设置当前日期  
				        c.add(Calendar.DATE, 3); //日期加3天  
				        createDate = c.getTime();  
				        if(new Date().after(createDate)){//当前时间是否在创建时间之后
				        	//修改数据库，锁定该用户
				        	DealerInfoDao dao = DealerInfoDao.getInstance();
				        	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				        	String lastsigninTime = format.format(new Date());
				        	dao.editUserLock(tup.getUserId(), 1, lastsigninTime);
				        	
				        	atx.setOutData("ERROR_MESSAGE", "账号"+anct+"由于开户3天未登陆已被锁定，请联系系统管理员");
							atx.setForword(firstPage);
							return "OK";
				        }
					}
					//end
					if(tup.getIsDcs()==1&&!"1".equals(rFlag)){
						atx.setOutData("ERROR_MESSAGE", "账号"+anct+"属于下端店面系统用户，无法直接登陆DMS系统");
						atx.setForword(firstPage);
						return "OK";
					}
				}
			}
			
			String rpcFlag = atx.getRequest().getParamValue("rpcFlag");
			String uuFlag = atx.getRequest().getParamValue("uuFlag");// 新增flag

			if (uuFlag != null) {// 新增
				if (uuFlag.equals("1")) {
					String GuId = UUID.randomUUID().toString();
					atx.getSession().set("GuId", GuId);
				} else if (uuFlag.equals("2")) {
					String reqGuId = atx.getRequest().getParamValue("reqGuId");// 提交flag
					String GuId = String.valueOf(atx.getSession().get("GuId"));
					if (reqGuId != null && GuId != null) {
						if (reqGuId.equals(GuId)) {
							atx.getSession().remove("GuId");
						} else {// session中和request中的GuId不相等
							atx.setForword(REPEAT_PAGE);
						}
					} else {
						atx.setForword(REPEAT_PAGE);
					}
				}
			}

			if (Utility.testString(rpcFlag)) {
				try {
					if (rpcFlag.equals("2")) {
						// 接口匿名登录
						invocation.invoke();
						return "OK";
					}
					// 接口请求
					if (null != usr) {
						if (!CommonDAO.authCheck(usr, atx.getRequestURI()
								.substring(atx.getRequestURI().indexOf("/", 2),
										atx.getRequestURI().indexOf(".")))) {
							if (null != usr.getRpcFlag()) {
								// add by liuqiang
								// 接口用户登录,该用户职位没有权限
								atx.setForword(ERROR_PAGE);
								return "OK";
							}
						}
						// 接口用户已经登录过系统
						invocation.invoke();
					} else {
						String rpcName = atx.getRequest().getParamValue(
								"rpcName");
						String rpcDealerCode = atx.getRequest().getParamValue(
								"rpcDealerCode");
						String rpcUserId = atx.getRequest().getParamValue(
								"rpcUserId");
						String rpcPoseId  = atx.getRequest().getParamValue(
						"roleId");
						HandleLogonInterceptor handle = new HandleLogonInterceptor();
						// 取出登录用户的所有信息放到session中
						AclUserBean logonUser = handle
								.getAclUserBeanByUserName(rpcName,
										rpcDealerCode, rpcFlag, rpcUserId,rpcPoseId);
						atx.getSession().set(Constant.LOGON_USER, logonUser);
						// 调用实际请求
						invocation.invoke();
					}
					return "OK";
				} catch (Throwable e) {
					// 捕获所有的接口异常 跳转到接口错误页面
					e.printStackTrace();
					atx.setForword(LOGIN_ERROR);
					return "OK";
				}
			}

			// if session is null:用户登录
			if (usr == null
					|| atx.getRequestURI().endsWith(
							"common/UserManager/login.do")) {
				String[] acnt_ = atx.getRequest().getParamValues("userName");
				String acnt = null;
				if(acnt_ != null && acnt_.length != 0) {
					acnt = acnt_[0] ;
				}

				/*String acnt = atx.getRequest().getParamValue("userName");*/
				if (acnt != null && !"".equals(acnt)&&!"1".equals(rFlag)) // added by
														// andy.ten@tom.com
														// 用户名转为大写
					acnt = acnt.toUpperCase();
				String pwd = atx.getRequest().getParamValue("password");

				if (null == pwd || "null".equals(pwd)) { // added by
															// andy.ten@tom.com
					pwd = "";
				} // end
				
				//TODO 添加验证码校验 2012-07-09 韩晓宇
				String validateCode = null;
				String sessionValidateCode = null;
				if(usr == null && atx.getRequestURI().endsWith("common/UserManager/login.do")) {
					validateCode = atx.getRequest().getParamValue("validateCode");
					sessionValidateCode = (String) atx.getSession().get("validateCode");
				} else if(atx.getRequestURI().endsWith("common/UserManager/validateLogin.json")){
					validateCode = null;
					sessionValidateCode = null;
					invocation.invoke();
					return "OK";
				}
				//非切换用户登陆
				if(atx.getSession() == null) {
					atx.setOutData("SESSION_INVALID", "true");
					atx.setForword(sessionInvalid);
					log.error("当前登录session已失效，请重新登录！");
					return "OK";
				}
				//String userName = atx.getRequest().getParamValue("userName");
				log.info(".............................."+validateCode+"............."+atx.getRequest().getParamValue("userName")+".................");

				//压力测试 用于验证码0000开关
				//if(!"1".equals(userChange) && sessionValidateCode != null && !"1".equals(rFlag) && !"0000".equals(validateCode)) {
				if(!"1".equals(userChange) && sessionValidateCode != null && !"1".equals(rFlag)) {
					if(validateCode != null && !"".equals(validateCode)) {
						if(!validateCode.equals(sessionValidateCode)) {
							atx.setOutData("ERROR_MESSAGE", "验证码错误!");
							atx.setForword("firstPage");
							log.error("帐号" + acnt + "验证码输入错误!");
							return "OK";
						} else {
							atx.getSession().remove("validateCode");
						}
					} else {
						atx.setOutData("ERROR_MESSAGE", "验证码不能为空!");
						atx.setForword("firstPage");
						log.error("帐号" + acnt + "验证码未输入!");
						return "OK";
					}
				}
				//TODO END
				
				if (acnt != null && (!"".equals(pwd) || "1".equals(userChange))) {
					if("1".equals(userChange)){
						usr = CommonDAO.getUserChangeLogon(acnt, pwd); //如果是切换用户就改用这种方法登陆 否则用普通登陆
					}else{
					   usr = CommonDAO.getLogon(acnt,pwd);
					}
				}
				
				if("1".equals(rFlag)){
					String id = atx.getRequest().getParamValue("id");
					String remoteIp = atx.getRequest().getRemoteAddr();
					log.info("===========客户端IP为："+remoteIp);
					String clientIp = atx.getRequest().getParamValue("clientIp");
					usr = CommonDAO.getRpcLogon(acnt, id, clientIp);
				}
				
				
				if (usr != null) { 
					if (this.URLContrl(atx, usr)) { // 判断是否启用URL控制
													// 经销商和售后不能访问相互系统 车厂用户除外
						atx.setOutData("ERROR_MESSAGE", "当前登录系统与用户职位不匹配！");
						atx.setForword(firstPage);
						log.error("当前登录系统与用户职位不匹配，请重新登录！");
						return "OK";
					}
					CommonDAO.disableUserOnlineInfo(usr.getUserId());
//					Object logUserFlag=session.get("logUserFlagSession");
//					if(logUserFlag!=null){
//						if(!acnt.equals(logUserFlag.toString())){
//							atx.setOutData("ERROR_MESSAGE", "一台计算机只能同时一个帐号登录,您如果确定要登录请重启浏览器!");
//							//atx.setOutData("ERROR_MESSAGE", "一台计算机只能同时一个帐号登录!");
//							atx.setForword(firstPage);
//							log.info("请不要重复登录!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
//							return "OK";
//						}
//					}
					session.set("logUserFlagSession",acnt);
					session.set(logonUser, usr);
					log.info("帐号" + acnt + "登录成功");

					if (atx.getRequest().getParamValue("backUser") != null) {
						session.set("loginType", "dealer");
						session.set("backUser", atx.getRequest().getParamValue(
								"backUser"));
						session.set("backUrl", atx.getRequest().getParamValue(
								"backUrl"));
					}
				} else { // added by andy.ten@tom.com
					if (atx.getRequestURI().endsWith(
							"common/UserManager/login.do")) {
						if("1".equals(rFlag)){
							atx.setForword(RPCERROR_PAGE);
							return "OK";
						}
						anct = atx.getRequest().getParamValue("userName");
						TcUserPO tup = UserManager.getUser(anct);
						if(tup!=null){
							Map loginMap = (Map)session.get("loginMap");
							Integer count = 1;
							boolean isAdmin = UserManager.isAdmin(tup.getUserId());
							if(!loginMap.containsKey(tup.getAcnt())){
								loginMap.put(tup.getAcnt(), count);
								if(isAdmin){
									atx.setOutData("ERROR_MESSAGE", "超级用户"+tup.getAcnt()+"密码输入错误,请确认后重新登陆！");
									atx.setForword(firstPage);
								}else{
									atx.setOutData("ERROR_MESSAGE", "普通用户"+tup.getAcnt()+"密码输入错误,请确认后重新登陆！");
									atx.setForword(firstPage);
								}
							}else{
								count = (Integer)loginMap.get(tup.getAcnt())+1;
								loginMap.put(tup.getAcnt(), count);
								if(isAdmin){
									if(count>3){
										UserManager.lockUser(tup.getUserId());
									}
									atx.setOutData("ERROR_MESSAGE", "超级用户"+tup.getAcnt()+"密码输入错误,请确认后重新登陆！");
									atx.setForword(firstPage);
								}else{
									if(count>10){
										UserManager.lockUser(tup.getUserId());
									}
									atx.setOutData("ERROR_MESSAGE", "普通用户"+tup.getAcnt()+"密码输入错误,请确认后重新登陆！");
									atx.setForword(firstPage);
								}
							}
						}else{
							atx.setOutData("ERROR_MESSAGE", "账号输入错误，请重新输入！");
							atx.setForword(firstPage);
							log.error("帐号" + acnt + "在系统中没有维护，请联系系统管理员");
						}
					} else if (atx.getRequestURI().endsWith(
							"common/UserManager/logout.do")) {
						atx.setForword(firstPage);
						log.error("点击退出！");
					}else {
						atx.setOutData("SESSION_INVALID", "true");
						atx.setForword(sessionInvalid);
						log.error("当前登录session已失效，请重新登录！");
					}
					return "OK";
				}

			} else {
				// 判断用户的功能权限
				if (atx.getRequestURI().endsWith(".do")) {
					if (!CommonDAO.authCheck(usr, atx.getRequestURI()
							.substring(atx.getRequestURI().indexOf("/", 2),
									atx.getRequestURI().indexOf(".")))) {
						if (null != usr.getRpcFlag()) {
							// add by liuqiang
							// 接口用户登录,该用户职位没有权限
							atx.setForword(ERROR_PAGE);
							return "OK";
						}
						atx.setForword(rejectPermission);
						return "OK";
					}

				}
			}

			if (atx.getRequestURI().endsWith(".js.gz")
					&& isGZipEncoding((HttpServletRequest) atx.getRequest())) {
				((HttpServletResponse) atx.getResponse()).setHeader(
						"Cache-Control", "public, max-age=2592000");
				((HttpServletResponse) atx.getResponse()).setHeader(
						"Content-Encoding", "gzip");
			}
			invocation.invoke();
			return "OK";
		} catch (Exception e) {
			atx.setException(e);
			log.error("用户登录出现错误：" + e.getMessage());
			e.printStackTrace();
		} finally {
			log.debug("Exit Interceptor: " + this.getClass().getSimpleName());
		}
		return "OK";
	}

	/**
	 * URL判断浏览器是否支持GZIP
	 * 
	 * @param request
	 * @return
	 */
	private static boolean isGZipEncoding(HttpServletRequest request) {
		boolean flag = false;
		String encoding = request.getHeader("Accept-Encoding");
		if (encoding.indexOf("gzip") != -1) {
			flag = true;
		}
		return flag;
	}

	/**
	 * 判断是否启用URL控制
	 * 
	 * @return
	 */
	@SuppressWarnings("unused")
	private boolean isUseURLlimit() {
		boolean flag = false;
		Map<String, Object> urlmap = SysParameterManageDAO
				.getSysParameterInfo("20241002");
		if ("1".equals(urlmap.get("PARA_VALUE"))) {
			flag = true;
		}
		return flag;

	}

	/**
	 * URL控制
	 * 
	 * @return
	 */
	@SuppressWarnings("unused")
	private boolean URLContrl(ActionContextExtend atx, AclUserBean user) {

		boolean isforward = false;

		String reqURL = atx.getRequest().getContextPath();

		int user_pose_bus_type = 0;

		int user_posetype = 0;

		if (null != user.getPoseBusType()) {
			user_pose_bus_type = user.getPoseBusType();
		}

		String JC = "/JC";

		String JCX = "/JCX";

		String CVS_SERVICE = "/CVS-SERVICE";

		String CVS_SALES = "/CVS-SALES";

		if (null != user.getPoseType()
				&& Integer.parseInt(Constant.COMPANY_TYPE_SGM) != user
						.getPoseType()) {

			if (this.isUseURLlimit()) {

				if (JC.equals(reqURL.toUpperCase())) {

					if (Constant.POSE_BUS_TYPE_DWR != user_pose_bus_type) {

						isforward = true;
					}

				} else if (JCX.equals(reqURL.toUpperCase())) {

					if (Constant.POSE_BUS_TYPE_DVS != user_pose_bus_type) {

						isforward = true;
					}

				} else if (CVS_SERVICE.equals(reqURL.toUpperCase())) {

					if (Constant.POSE_BUS_TYPE_DWR != user_pose_bus_type) {

						isforward = true;
					}

				} else if (CVS_SALES.equals(reqURL.toUpperCase())) {

					if (Constant.POSE_BUS_TYPE_DVS != user_pose_bus_type) {

						isforward = true;
					}

				}
			}
		}
		return isforward;
	}

}
