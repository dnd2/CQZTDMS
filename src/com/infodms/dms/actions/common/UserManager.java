/**********************************************************************
* <pre>
* FILE : UserManager.java
* CLASS : UserManager
*
* AUTHOR : LAX
*
* FUNCTION : 经销商用户查询.
*
*
*======================================================================
* CHANGE HISTORY LOG
*----------------------------------------------------------------------
* MOD. NO.| DATE     | NAME | REASON | CHANGE REQ.
*----------------------------------------------------------------------
*         |2009-09-09| LAX  | Created |
* DESCRIPTION:
* </pre>
***********************************************************************/
/**
 * $UserManager,v0.1 2009/09/09  lax  经销商用户查询$
 */

package com.infodms.dms.actions.common;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.infodms.dms.actions.customerRelationships.classMonitorManage.ClassMonitorManage;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.MD5Util;
import com.infodms.dms.common.interceptor.LogonInterceptor;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.dao.common.CommonDAO;
import com.infodms.dms.dao.sales.dealer.DealerInfoDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TcPosePO;
import com.infodms.dms.po.TcUserPO;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.dms.chana.dao.DeCommonDao;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PO;

import edu.emory.mathcs.backport.java.util.LinkedList;

/**
 * Function       :  查询经销商用户
 * @author        :  LAX
 * CreateDate     :  2009-09-09
 * @version       :  0.1
 */
public class UserManager extends BaseDao{
	private static Logger logger = Logger.getLogger(UserManager.class);
	private static final UserManager dao = new  UserManager();
	private DeCommonDao deCommonDao = DeCommonDao.getInstance();
	
	/**
	 * Function       :  用户登录职位选择 
	 * @author        :  witti
	 * CreateDate     :  2009-09-16
	 * @version       :  0.1
	 */
	public void login() throws Exception {
		ActionContext atx = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)atx.getSession().get(Constant.LOGON_USER);
		//登陆时增加一个标记  ranke 20150408
		//atx.getSession().set("loginTab", 1);
		//end
		TcUserPO tup = new TcUserPO();
		tup.setUserId(logonUser.getUserId());
		List<PO> ls = dao.select(tup);
		tup = (TcUserPO)ls.get(0);
		int isFirst = tup.getIsFirst();
		boolean isAdmin = this.isAdmin(logonUser.getUserId());
		int isDcs = tup.getIsDcs();
		atx.setOutData("isAdmin",isAdmin);
		StringBuffer checkMessage = new StringBuffer();
		String checkFirstLogin = CommonDAO.getPara(Constant.CHECK_FIRST_LOGIN.toString()) ;
		String checkPasswordDate = CommonDAO.getPara(Constant.CHECK_PASSWORD_DATE.toString()) ;
		String userChange = atx.getRequest().getParamValue("userchange");// 用户切换功能判断标
		String seatChange = atx.getRequest().getParamValue("seatchange");// 坐席切换功能判断标
		if(isDcs!=1){
			if("1".equals(checkFirstLogin) && !"1".equals(userChange)){
				if(isFirst==1){
					checkMessage.append("首次登陆，请修改密码");
					atx.setOutData("checkMessage", checkMessage);
					atx.setOutData("user", tup);
					atx.setForword("/firstLogin.jsp");
					return;
				}
			}
			if("1".equals(checkPasswordDate) && !"1".equals(userChange)){
				if(new Date().after(tup.getOverDate())){
					checkMessage.append("密码过期，请修改密码");
					atx.setOutData("checkMessage", checkMessage);
					atx.setOutData("user", tup);
					atx.setForword("/firstLogin.jsp");
					return;
				}
			}
		}
		ClassMonitorManage classMonitorManage = new ClassMonitorManage();
		//2013.07.07 添加用户登入日志
		int state = classMonitorManage.seatLogin();
		if(state == 0)
		{
			 state = classMonitorManage.isSeatManeger();
		}
		// 艾春 13.11.22 添加坐席日志控制，如果坐席是登录不是用户切换，则记录登录日志
		if(null == seatChange){
			loginRcord(logonUser.getUserId(), 1, atx.getRequest());
		}
//		
//		
		//更改坐席空闲状态
		
		
		
		if(state == 1){
			classMonitorManage.changeWorkStatus(logonUser.getUserId(), Constant.SEAT_FREE_TYPE);
		}else if(state == 2){
			classMonitorManage.changeWorkStatus(logonUser.getUserId(), Constant.SEAT_FREE_TYPE);
			/*atx.setOutData("ERROR_MESSAGE", "坐席"+logonUser.getName()+"此时你未被排班,不能登陆!");
			atx.setForword("firstPage");
			return ;*/
		}
		
		try {
			List<TcPosePO> poseList = CommonDAO.queryUserPosition(logonUser.getUserId());
			atx.setOutData("poseList", poseList);
 			if(poseList!=null&&poseList.size()==1) {
 				if(state == 1)
 				{
 					atx.setRedirect("/common/MenuShow/menuDisplay.do?deptId="+poseList.get(0).getOrgId()+"&poseId="+poseList.get(0).getPoseId()+"&poseType="+poseList.get(0).getPoseType()+"&poseBusType="+poseList.get(0).getPoseBusType()+"&state="+state);
 				}else
 				{
 					atx.setRedirect("/common/MenuShow/menuDisplay.do?deptId="+poseList.get(0).getOrgId()+"&poseId="+poseList.get(0).getPoseId()+"&poseType="+poseList.get(0).getPoseType()+"&poseBusType="+poseList.get(0).getPoseBusType());
 				}
				
			}else {
				atx.setForword("positon");
			}
		}catch (Exception e) {
			BizException e1 = new BizException(atx,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"用户职位信息");
			logger.error(logonUser,e1);
			atx.setException(e1);
		}
	}
	
	/**
	 * Function       :  用户登录显示职位或经销商信息
	 * CreateDate     :  2010-09-28
	 * @version       :  0.1
	 */
	public void getUserInfo () {
		ActionContext atx = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)atx.getSession().get(Constant.LOGON_USER) ;
		try {
			RequestWrapper request = atx.getRequest() ;
			String flag = CommonUtils.checkNull(request.getParamValue("flag")) ;
			String info = "" ;
			String infoA = "" ;
			String isReport = "0";
			String funcName = null;
			if ("0".equals(flag)) {
				info = CommonUtils.checkNull(request.getParamValue("poseId")) ;
				//如果isReport==“1”，说明有“当日销售看板”
				funcName = "当日销售看板";
				isReport = DealerInfoDao.getInstance().isDayReport(Long.parseLong(info), funcName);
				TcPosePO tcp = new TcPosePO() ;
				tcp.setPoseId(Long.parseLong(info)) ;
				List<TcPosePO> infos = dao.select(tcp);
				if (null != infos && infos.size() > 0) {
					infoA = infos.get(0).getPoseName() ;
					
					if(Constant.DUTY_TYPE_COMPANY.toString().equals(logonUser.getDutyType())) {
						this.getBoard(Constant.SYS_USER_SGM.toString(), Constant.DUTY_TYPE_COMPANY.toString()) ;
					} else if(Constant.DUTY_TYPE_LARGEREGION.toString().equals(logonUser.getDutyType())) {
						this.getBoard(Constant.SYS_USER_SGM.toString(), Constant.DUTY_TYPE_LARGEREGION.toString()) ;
					}
				}
			} else {
				/*info = CommonUtils.checkNull(request.getParamValue("dealerId")) ;*/
				info = CommonUtils.checkNull(request.getParamValue("poseId")) ;
				funcName = "经销商当日销售看板";
				isReport = DealerInfoDao.getInstance().isDayReport(Long.parseLong(info), funcName);
				TcPosePO tcp = new TcPosePO() ;
				tcp.setPoseId(Long.parseLong(info)) ;
				List<TcPosePO> infos = dao.select(tcp);
				if (null != infos && infos.size() > 0) {
					infoA = infos.get(0).getPoseName() ;
					
					if(Constant.DUTY_TYPE_DEALER.toString().equals(logonUser.getDutyType())) {
						this.getBoard(Constant.SYS_USER_DEALER.toString(), Constant.DUTY_TYPE_DEALER.toString()) ;
					} 
				}
				/*List<Map<String, Object>> infos = DealerInfoDao.getInstance().getDel(info, poseInfo) ;
				if (null != infos && infos.size() > 0) {
					infoA = infos.get(0).get("DEALER_NAME").toString() ;
				}*/
			}
			
			atx.setOutData("info", infoA) ;
			atx.setOutData("isReport", isReport) ;
		} catch(Exception e) {
			BizException e1 = new BizException(atx,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"用户信息");
			logger.error(logonUser,e1);
			atx.setException(e1);
		}
	}
	/**
	 * Function       :  用户退出系统 
	 * @author        :  witti
	 * CreateDate     :  2009-09-18
	 * @version       :  0.1
	 */
	public void logout() throws Exception {
		ActionContext atx = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)atx.getSession().get(Constant.LOGON_USER);
		try {
			CommonDAO.disableUserOnlineInfo(logonUser.getUserId());
			CommonDAO.logUserOnlineTime(logonUser.getUserId());
			logger.info("用户："+logonUser.getName()+"正常退出");
			
			// 2013.07.07 艾春 添加记录用户登出日志
			loginRcord(logonUser.getUserId(), 2, atx.getRequest());
			
			//更改坐席未登录状态
			ClassMonitorManage classMonitorManage = new ClassMonitorManage();
			classMonitorManage.changeWorkStatus(logonUser.getUserId(), Constant.SEAT_UNLOGIN_TYPE);
			String loginType = (String)atx.getSession().get("loginType");
			if(loginType!=null && loginType.equals("dealer")){
				   String userName = (String)atx.getSession().get("backUser");
				   String backUrl = (String)atx.getSession().get("backUrl");
				   atx.setOutData("backUser", userName);
				   atx.setOutData("backUrl", backUrl);
					atx.getSession().remove(Constant.LOGON_USER);
					atx.getSession().invalidate();
					atx.setForword("/trans.jsp");
					return;
			}
			
			
	//		atx.getSession().remove(Constant.LOGON_USER);
			atx.getSession().invalidate();
			
			logonUser = null;
			// 区分内外网进行Redirect
//			if(CommonUtils.isInnerNetwork(atx.getRequest().getRemoteAddr())) {
//				atx.setRedirect(LogonInterceptor.logoutInnerUrl);
//			}else {
			//	atx.setRedirect(LogonInterceptor.logoutUrl);
			atx.setForword(LogonInterceptor.firstPage);
//			}
		}catch (Exception e) {
			BizException e1 = new BizException(atx,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"用户职位信息");
			logger.error(logonUser,e1);
			atx.setException(e1);
		}
	}
	/**
	 * 
	* @Title: getInterUser 
	* @Description: TODO(下端调用上端的方法，用于获取所有接口用户) 
	* @param     设定文件 
	* @return void    返回类型 
	* @throws
	 */
	public void getInterUser() {
		OutputStreamWriter ow = null;
		ActionContext atx = ActionContext.getContext();
		String rpcDealerCode = atx.getRequest().getParamValue("rpcDealerCode");
		logger.info("获取接口用户 dmsCode === " + rpcDealerCode);
		AclUserBean logonUser = (AclUserBean)atx.getSession().get(Constant.LOGON_USER);
		try{
			Map<String, Object> map = deCommonDao.getDcsCompanyCode(rpcDealerCode);
			List<TcUserPO> list = CommonDAO.getInterUser(map.get("DCS_CODE").toString());
			StringBuffer json = new StringBuffer("");
			if(list!=null && list.size()>0){
				json.append("{T_SYS_USER_ROLE:[");
				for(int i=0;i<list.size();i++){
					TcUserPO po = list.get(i);
					List<Map<String,Object>> listRoles = getRoles(po);
					if(listRoles!=null&&listRoles.size()>0){
						for(int j=0;j<listRoles.size();j++){
							json.append("{");
							json.append("USER_ID:'");
							json.append(po.getAcnt());
							json.append("',");
							json.append("LOGIN_NAME:'");
							json.append(po.getAcnt());
							json.append("',");
							json.append("ROLE_ID:'");
							json.append(listRoles.get(j).get("POSE_ID"));
							json.append("',");
							json.append("ROLE_NAME:'");
							json.append(listRoles.get(j).get("POSE_NAME"));
							json.append("'}");
							if((i+1)!=list.size()||(j+1)!=listRoles.size()){
								json.append(",");
							}
						}
					}
				}
				json.append("]}");
			}else{
				logger.error("找不到接口用户");
				json.append("{T_SYS_USER_ROLE:[]}");
			}
			
			OutputStream out = atx.getResponse().getOutputStream();
			ow = new OutputStreamWriter(out,"GB2312");  
			ow.write(json.toString()); 
			ow.flush();  
			//atx.setForword("/test.jsp");
		}catch (Exception e) {
			BizException e1 = new BizException(atx,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"用户信息");
			logger.error(logonUser,e1);
			atx.setException(e1);
		} finally {
			if (null != ow) {
				try {
					ow.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * 
	* @Title: getInterUser 
	* @Description: TODO(根据接口用户取对应的职位) 
	* @param @param dcsCode 上端经销商公司code
	* @return List<TcUserPO>    返回类型 
	* @throws
	 */
	public static List<Map<String,Object>> getRoles(TcUserPO po){
		StringBuffer sql= new StringBuffer();
		sql.append(" select tup.POSE_ID, tcp.POSE_NAME\n" );
		sql.append("  from tc_user tcu, tr_user_pose tup, tc_pose tcp\n" );
		sql.append(" where tcu.USER_ID = " ).append(po.getUserId()).append("\n");
		sql.append("   and tcu.USER_ID = tup.USER_ID and tup.POSE_ID = tcp.POSE_ID");
		List<Map<String,Object>> listRoles = dao.pageQuery(sql.toString(), null, dao.getFunName());
		return listRoles;
	}
	
	public static boolean isAdmin(Long userId){
		StringBuffer sql= new StringBuffer();
		sql.append(" select * from tc_user tu,tr_user_pose tup where tu.user_id=tup.user_id\n" );
		sql.append(" and tup.pose_id=2010082600119486 and tu.user_id="+ userId);
		Map<String,Object> map = dao.pageQueryMap(sql.toString(), null, dao.getFunName());
		if(map!=null){
			return true;
		}
		return false;
	}
	
	public static TcUserPO getUser(String anct){
		if(anct==null) {
			return null ;
		}
		
		anct = anct.toUpperCase();
		TcUserPO tup = new TcUserPO();
		tup.setAcnt(anct);
		List<PO> ls = dao.select(tup);
		if(ls.size()>0&&ls!=null){
			return (TcUserPO)ls.get(0);
		}
		return null;
	}
	
	
	public void getBoard(String funcType, String funcLevel) {
		ActionContext atx = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)atx.getSession().get(Constant.LOGON_USER);
		StringBuffer funcName = new StringBuffer("") ;
		StringBuffer funcCode = new StringBuffer("") ;
		
		StringBuffer sql = new StringBuffer("\n") ;
		
		String flag = CommonUtils.checkNull(atx.getRequest().getParamValue("flag")) ;
		Long dealerId = logonUser.getCompanyId();
		Calendar calendar = Calendar.getInstance();
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		int startDay = calendar.getActualMinimum(Calendar.DAY_OF_MONTH);
		int endDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
		calendar.set(Calendar.DAY_OF_MONTH, startDay);
		String startDate = dateFormat.format(calendar.getTime());
		calendar.set(Calendar.DAY_OF_MONTH, endDay);
		String endDate = dateFormat.format(calendar.getTime());
		
		sql.append("select tbf.func_code, tbf.func_name\n");
		sql.append("  from TC_BILLBOARD_FUNCTION tbf\n");  
		sql.append(" where 1 = 1\n");  
		sql.append("   and tbf.func_type = ").append(funcType).append("\n");  
		sql.append("   and tbf.func_level = ").append(funcLevel).append("\n");  
		sql.append(" order by tbf.sort_order\n");

		List<Map<String, Object>> funcList = super.pageQuery(sql.toString(), null, super.getFunName()) ;
		
		int len = 0 ;
		
		if(funcList !=null) {
			len = funcList.size() ;
		}
		
		for(int i=0; i<len; i++) {
			if(funcName.length() == 0) {
				funcName.append(funcList.get(i).get("FUNC_NAME")) ; 
				if("0".equals(flag) && "集客量汇总查询".equals(funcList.get(i).get("FUNC_NAME"))) {
					funcCode.append(funcList.get(i).get("FUNC_CODE")) ; 
					funcCode.append("?startDate=");
					funcCode.append(startDate);
					funcCode.append("&endDate=");
					funcCode.append(endDate);
					if(Constant.DUTY_TYPE_LARGEREGION.toString().equals(logonUser.getDutyType())) {
						funcCode.append("&orgId=");
						funcCode.append(logonUser.getOrgId());
					}
				} else if("1".equals(flag) && "集客量汇总查询".equals(funcList.get(i).get("FUNC_NAME"))){
					funcCode.append(funcList.get(i).get("FUNC_CODE")) ; 
					funcCode.append("?dealerId=");
					funcCode.append(dealerId);
					funcCode.append("&startDate=");
					funcCode.append(startDate);
					funcCode.append("&endDate=");
					funcCode.append(endDate);
				} else {
					funcCode.append(funcList.get(i).get("FUNC_CODE")) ; 
				}
			} else {
				funcName.append(",").append(funcList.get(i).get("FUNC_NAME")) ; 
				if("0".equals(flag) && "集客量汇总查询".equals(funcList.get(i).get("FUNC_NAME"))) {
					funcCode.append(",").append(funcList.get(i).get("FUNC_CODE")) ; 
					funcCode.append("?startDate=");
					funcCode.append(startDate);
					funcCode.append("&endDate=");
					funcCode.append(endDate);
					if(Constant.DUTY_TYPE_LARGEREGION.toString().equals(logonUser.getDutyType())) {
						funcCode.append("&orgId=");
						funcCode.append(logonUser.getOrgId());
					}
				} else if("1".equals(flag) && "集客量汇总查询".equals(funcList.get(i).get("FUNC_NAME"))){
					funcCode.append(",").append(funcList.get(i).get("FUNC_CODE")) ; 
					funcCode.append("?dealerId=");
					funcCode.append(dealerId);
					funcCode.append("&startDate=");
					funcCode.append(startDate);
					funcCode.append("&endDate=");
					funcCode.append(endDate);
				} else {
					funcCode.append(",").append(funcList.get(i).get("FUNC_CODE")) ; 
				}
			}
		}
		
		atx.setOutData("funcCode", funcCode.toString()) ;
		atx.setOutData("funcName", funcName.toString()) ;
	}
	/**
	 * 
	* @Title: rpcRequest 
	* @Description: TODO(处理所有接口请求) 
	 */
	public void rpcRequest() {
		//空处理,用于下端做公共的登录请求
	}
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public static void lockUser(Long userId){
		/*POContext.beginTxn(DBService.getInstance().getDefTxnManager(), -1);
		TcUserPO poDB = new TcUserPO();
		poDB.setUserId(userId);
		TcUserPO poValue = new TcUserPO();
		poValue.setIsLock(1);
		dao.update(poDB, poValue);
		POContext.endTxn(true);*/
		
		List<Object> ins = new LinkedList();
		ins.add(userId);
		dao.callProcedure("p_update_user", ins, null);
	}
	
	public void modefyPassword(){
		ActionContext atx = ActionContext.getContext();
		RequestWrapper request = atx.getRequest();
		
		//得到页面参数
		String userId = CommonUtils.checkNull(request.getParamValue("userId"));
		String password = CommonUtils.checkNull(request.getParamValue("passId"));
		//现在用的密码
		String nowPassword = CommonUtils.checkNull(request.getParamValue("nowPassword"));
		//新密码
		String newPassword = CommonUtils.checkNull(request.getParamValue("newPassword"));
		
		String userType = CommonUtils.checkNull(request.getParamValue("userType"));
		
		boolean isAdmin = Boolean.valueOf(CommonUtils.checkNull(request.getParamValue("isAdmin")));
		
		//2013.07.07 添加用户登入日志
		loginRcord(Long.parseLong(userId), 1, request);
		
		Boolean checkStatus = false;
		//checkStatus = MD5Util.checkPassword(nowPassword, new Long(userId));
		checkStatus = (MD5Util.MD5Encryption(nowPassword)).equals(password);
		if(checkStatus){
			
			TcUserPO poDB = new TcUserPO();
			poDB.setUserId(new Long(userId));
			TcUserPO poValue = new TcUserPO();
			poValue.setPassword(MD5Util.MD5Encryption(newPassword));
			poValue.setUpdateBy(new Long(userId));
			poValue.setUpdateDate(new Date(System.currentTimeMillis()));
			poValue.setIsFirst(0);
			Calendar cal = Calendar.getInstance();
			if(isAdmin){
				cal.add(Calendar.MONTH, 3);
			}else{
				cal.add(Calendar.MONTH, 6);
			}
			poValue.setOverDate(cal.getTime());
			factory.update(poDB, poValue);	
			atx.setOutData("returnValue",1);
		}else{
			atx.setOutData("returnValue",0);
		}
	}
	
	/**
	 * 用于记录坐席登入登出ID
	 * @param userId 登录用户ID
	 */
	public void loginRcord(Long userId, Integer log_type, RequestWrapper request) {
//		List<Object> ins = new java.util.LinkedList<Object>();
//		ins.add(userId);
//		ins.add(log_type);
//		ins.add(request.getRemoteAddr());
//		ins.add(request.getRemoteHost());
		// 调用存储过程太慢，修改为直接SQL插入
//		dao.callProcedure("PKG_SALE_SERVICE.proc_loginoutRcord", ins, null);
		// 直接通过用户ID查询是否是坐席,如果是,写入登入登出日志
		String sql ="SELECT count(1) flag \n" +
					"  FROM TC_USER T, TT_CRM_SEATS S\n" + 
					" WHERE T.USER_ID = S.SE_USER_ID\n" + 
		// 艾春 2013.12.3 修改添加记录潘墨林登入登出日志
					"   AND ((S.SE_IS_SEATS = 10041001 \n" + 
					"   AND T.USER_ID =" + userId+") OR (2013082804093800 = "+userId+"))\n";
		
		try {
			List<Map<String, Object>> funcList = super.pageQuery(sql.toString(), null, super.getFunName());
			Map<String, Object> mflag = funcList.get(0);
			String flag = mflag.get("FLAG").toString();
			// 如果是坐席
			if(!"0".equals(flag)){
				String inserSQL = "";
				String updateSQL = "";
				if(1 == log_type){
					inserSQL = "INSERT INTO TT_CRM_SEATS_LOG(LOG_ID, USER_ID, LOGIN_DATE, LOG_DAYS, IP, HOSTNAME)\n" +
						       "SELECT F_GETID(), "+userId+", SYSDATE, TRUNC(SYSDATE), '"+request.getRemoteAddr()+"', '"+
						       request.getRemoteHost()+"' FROM DUAL";
				}else{
					inserSQL = "INSERT INTO TT_CRM_SEATS_LOG(LOG_ID, USER_ID, LOGOUT_DATE, LOG_DAYS, IP, HOSTNAME)\n" +
				               "SELECT F_GETID(), "+userId+", SYSDATE, TRUNC(SYSDATE), '"+request.getRemoteAddr()+"', '"+
				               request.getRemoteHost()+"' FROM DUAL";
					// 艾春9.25添加 坐席登出修改坐席状态
					updateSQL = "UPDATE tt_crm_seats t SET t.se_work_status = "+Constant.SEAT_UNLOGIN_TYPE+" WHERE t.se_user_id = "+userId;
					dao.update(updateSQL, null);
				}
				dao.insert(inserSQL);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
     * @Description:验证用户是否已有登陆信息
     * 2013-05-15 HXY
     */
     public void validateLogin() {
            ActionContext atx = ActionContext.getContext();
            AclUserBean logonUser = (AclUserBean)atx.getSession().get(Constant.LOGON_USER);
            try{
                   if(logonUser != null && logonUser.getUserId() != null) {
                          atx.setOutData("logined", true);
                   }
            }catch (Exception e) {
                   BizException e1 = new BizException(atx,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"验证用户是否已有登陆信息");
                   logger.error(logonUser,e1);
                   atx.setException(e1);
            } 
     }

}
