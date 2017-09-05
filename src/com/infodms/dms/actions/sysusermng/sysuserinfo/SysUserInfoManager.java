package com.infodms.dms.actions.sysusermng.sysuserinfo;

import java.text.SimpleDateFormat;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.bean.TmUserInfoBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.dao.sysusermng.SysUserInfoManagerDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TcUserOnlinePO;
import com.infodms.dms.po.TcUserPO;
import com.infodms.dms.util.ActionUtil;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.POFactory;
import com.infoservice.po3.POFactoryBuilder;
import com.infoservice.po3.bean.PageResult;

public class SysUserInfoManager {

	public Logger logger = Logger.getLogger(SysUserInfoManager.class);
	private POFactory factory = POFactoryBuilder.getInstance();
	private ActionContext act = ActionContext.getContext();
	private AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
	
	private final String searchUserInfoURL = "/jsp/sysusermng/SearchUserInfo.jsp";
	private final String changeUserURL = "/jsp/sysusermng/showUser.jsp";
	
	//zhmingwei 2011-11-21
	private final String visitAdd = "/jsp/sysusermng/visitAdd.jsp";
	private final String visitAddDealer = "/jsp/sysusermng/visitAddDealer.jsp";
	
	/**
	 * Function       :  个人信息维护初始化
	 * @throws        :  Exception
	 * LastUpdate     :  2010-1-12
	 */
	public void queryUserInfoInit(){
		try {	
						
			//得到当前用户的ID;
			Long userId = logonUser.getUserId();
			
			TcUserPO tcUserPO = new TcUserPO();
			tcUserPO.setUserId(new Long(userId));
			tcUserPO = factory.select(tcUserPO).get(0);
			
			TcUserOnlinePO lastLoginDate = SysUserInfoManagerDao.getLastUserLoginDate(new Long(userId));
			
			act.setOutData("user", tcUserPO);
			act.setOutData("lastLoginDate", lastLoginDate);
			
			act.setForword(searchUserInfoURL);
		}catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"个人信息维护初始化");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * Function       :  个人信息查询
	 * @throws        :  Exception
	 * LastUpdate     :  2010-1-12
	 */
	public void queryUserInfo(){
		try {	
			RequestWrapper request = act.getRequest();
			if("1".equals(request.getParamValue("command"))){ //json请求
				act.getResponse().setContentType("application/json");
				
				//获取排序字段和排序类型
				String orderName = request.getParamValue("orderCol");
				String da = request.getParamValue("order");
					
				Integer curPage = request.getParamValue("curPage") !=null ? Integer.parseInt(
						request.getParamValue("curPage")) : 1;
						
				//得到当前用户的ID;
				Long userId = logonUser.getUserId();
				Long orgId = logonUser.getOrgId();
				Long companyId = logonUser.getCompanyId();
				
				PageResult<TmUserInfoBean> ps = SysUserInfoManagerDao.findUserPositionInfo(userId,orgId,companyId, curPage, Constant.PAGE_SIZE, orderName, da);
				act.setOutData("ps",ps);
			}
			act.setForword(searchUserInfoURL);
		}catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"个人信息查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * Function       :  个人信息维护
	 * @throws        :  Exception
	 * LastUpdate     :  2010-1-12
	 */
	public void modifyUserInfo(){
		try {	
			RequestWrapper request = act.getRequest();
			//得到页面参数
			String userId = CommonUtils.checkNull(request.getParamValue("userId"));
			String name = CommonUtils.checkNull(request.getParamValue("name"));
			String gender = CommonUtils.checkNull(request.getParamValue("gender"));
			String birthday = CommonUtils.checkNull(request.getParamValue("birthday"));
			String zipCode = CommonUtils.checkNull(request.getParamValue("zipCode"));
			String phone = CommonUtils.checkNull(request.getParamValue("phone"));
			String handPhone = CommonUtils.checkNull(request.getParamValue("handPhone"));
			String email = CommonUtils.checkNull(request.getParamValue("email"));
			String addr = CommonUtils.checkNull(request.getParamValue("addr"));
			
			TcUserPO poDB = new TcUserPO();
			poDB.setUserId(new Long(userId));
			
			TcUserPO poValue = new TcUserPO();
			poValue.setName(name);
			if(null != gender && !"".equals(gender)){
				poValue.setGender(Integer.valueOf(gender));
			}
			if(null != birthday && !"".equals(birthday)){
		        //设置日期格式	
		        SimpleDateFormat fmat = new SimpleDateFormat ("yyyy-MM-dd");
				poValue.setBirthday(fmat.parse(birthday));
			}
			if(null != zipCode && !"".equals(zipCode)){
				poValue.setZipCode(Integer.valueOf(zipCode));
			}
			poValue.setPhone(phone);
			poValue.setHandPhone(handPhone);
			poValue.setEmail(email);
			poValue.setAddr(addr);
			ActionUtil.setUpatePO(poValue, logonUser);
			factory.update(poDB,poValue);			
			
			TcUserPO tcUserPO = new TcUserPO();
			tcUserPO.setUserId(new Long(userId));
			tcUserPO = factory.select(tcUserPO).get(0);
			
			TcUserOnlinePO lastLoginDate = SysUserInfoManagerDao.getLastUserLoginDate(new Long(userId));
			
			act.setOutData("user", tcUserPO);
			act.setOutData("lastLoginDate", lastLoginDate);
			
			act.setForword(searchUserInfoURL);
		}catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"个人信息维护");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * Function       :  用户切换
	 * @throws        :  Exception
	 * LastUpdate     :  2010-1-12
	 */
	public void openUserList(){
		act.setForword(changeUserURL);
	}
	
	//zhumingwei 2011-11-21
	public void queryVisitAddCarFactoryInit(){
		try {				
			//得到当前用户的ID;
			Long userId = logonUser.getUserId();
			
			act.setOutData("userId", userId);
			act.setForword(visitAdd);
		}catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"个人信息维护初始化");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	//zhumingwei 2011-11-21
	public void queryVisitAddDealerInit(){
		try {	
						
			//得到当前用户的ID;
			Long userId = logonUser.getUserId();
			
			act.setOutData("user", userId);
			
			act.setForword(visitAddDealer);
		}catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"个人信息维护初始化");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
}
