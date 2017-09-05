package com.infodms.dms.actions.crmphone.common;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.actions.sales.planmanage.PlanUtil.BaseImport;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.dao.crmphone.CrmPhoneDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TPcSuggestPO;
import com.infodms.dms.po.TcUserPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.po3.bean.DynaBean;

public class Common extends BaseImport {
	public Logger logger = Logger.getLogger(Common.class);
	
	/**
	 * 日程管理初始页面
	 */
	public void login() {
		ActionContext act = ActionContext.getContext();
		act.getResponse().addHeader("Access-Control-Allow-Origin", "*");
		//response.setHeader("Access-Control-Allow-Origin", "*");
		CrmPhoneDao dao = new CrmPhoneDao();
		String userId = act.getRequest().getParamValue("userId");
		String password = act.getRequest().getParamValue("password");
		String adviserFlag = "10011002";
		Map strResult = new HashMap();
		Map result = new HashMap();
		try {//获取userId
			TcUserPO userPo = new TcUserPO();
			userPo.setAcnt(userId);
			userPo=(TcUserPO) dao.select(userPo).get(0);
			//判断是否顾问登陆
			if(CommonUtils.judgeAdviserLogin(userPo.getUserId().toString())) {
				adviserFlag = "10011001";
			} else if(CommonUtils.judgeDirectorLogin(userPo.getUserId().toString())){//判断是否主管登陆
				adviserFlag = "10011003";
			} else if(CommonUtils.judgeDcrcLogin(userPo.getUserId().toString())){//判断是否DCRC登陆
				adviserFlag = "10011004";
			}
			//验证用户名密码是否正确
			List<Map<String, Object>> getHasList = dao.doLogin(userId, password);
			
			if("10011004".equals(adviserFlag))
			{
				strResult.put("\"success\"", "false");
				strResult.put("\"msg\"", "DCRC用户没有权限登录!");
				act.setOutData("\"strResult\"", strResult);
			}
			//登陆成功
			else if(getHasList.size() > 0) {
				//获取用户基础信息
				List<DynaBean> userList = dao.getUserInfo(userId);
				DynaBean db = userList.get(0);
				result.put("\"userId\"", db.get("USER_ID"));
				result.put("\"userName\"", db.get("NAME"));
				result.put("\"userType\"", db.get("USER_TYPE"));
				result.put("\"poseName\"", db.get("POSE_NAME"));
				result.put("\"orgId\"", db.get("ORG_ID"));
				result.put("\"dealerId\"", db.get("DEALER_ID"));
				result.put("\"adviserFlag\"", adviserFlag);
				
				strResult.put("\"success\"", "true");
				strResult.put("\"msg\"", "操作成功");
				strResult.put("\"result\"", result);
				
				act.setOutData("\"strResult\"", strResult);
			} else {//登陆失败
				strResult.put("\"success\"", "false");
				strResult.put("\"msg\"", "用户名或密码错误!");
				act.setOutData("\"strResult\"", strResult);
			}
		} catch (Exception e) {
			strResult.put("\"success\"", "false");
			strResult.put("\"msg\"", "网络出错,请重新执行!");
			act.setOutData("\"strResult\"", strResult);
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"未知错误!");
			act.setException(e1);
		}
	}
	
	/**
	 * 修改密码
	 */
	public void modifyPassWord() {
		ActionContext act = ActionContext.getContext();
		act.getResponse().addHeader("Access-Control-Allow-Origin", "*");
		CrmPhoneDao dao = new CrmPhoneDao();
		String userId = act.getRequest().getParamValue("userId");
		String oldPassword = act.getRequest().getParamValue("oldPassword");
		String newPassword = act.getRequest().getParamValue("newPassword");
		Map strResult = new HashMap();
		Map result = new HashMap();
		try {//获取acnt
			TcUserPO userPo = new TcUserPO();
			userPo.setUserId(Long.parseLong(userId));
			userPo=(TcUserPO) dao.select(userPo).get(0);
			//验证用户名密码是否正确
			List<Map<String, Object>> getHasList = dao.doLogin(userPo.getAcnt(), oldPassword);
			
			//原密码正确
			if(getHasList.size() > 0) {
				//修改密码
				dao.modifyPassword(userId,newPassword);
				
				strResult.put("\"success\"", "true");
				strResult.put("\"msg\"", "操作成功");
				strResult.put("\"result\"", result);
				
				act.setOutData("\"strResult\"", strResult);
			} else {//原密码错误
				strResult.put("\"success\"", "false");
				strResult.put("\"msg\"", "原密码输入不正确!");
				act.setOutData("\"strResult\"", strResult);
			}
		} catch (Exception e) {
			strResult.put("\"success\"", "false");
			strResult.put("\"msg\"", "网络出错,请重新执行!");
			act.setOutData("\"strResult\"", strResult);
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"未知错误!");
			act.setException(e1);
		}
	}
	
	/**
	 * 提交合理化建议
	 * @throws UnsupportedEncodingException 
	 */
	public void doSuggest() throws UnsupportedEncodingException {
		ActionContext act = ActionContext.getContext();
		CrmPhoneDao dao = new CrmPhoneDao();
		String userId = act.getRequest().getParamValue("userId");
		String offerName = act.getRequest().getParamValue("offerName");
//		if(offerName!=null&&!"".equals(offerName)){
//			offerName = new String(offerName.getBytes("iso-8859-1"),"gbk");
//		}
		String offerPhone = act.getRequest().getParamValue("offerPhone");
//		if(offerPhone!=null&&!"".equals(offerPhone)){
//			offerPhone = new String(offerPhone.getBytes("iso-8859-1"),"gbk");
//		}
		String suggestInfo = act.getRequest().getParamValue("suggestInfo");
//		if(suggestInfo!=null&&!"".equals(suggestInfo)){
//			suggestInfo = new String(suggestInfo.getBytes("iso-8859-1"),"gbk");
//		}
		Map strResult = new HashMap();
		Map result = new HashMap();
		try {
			//保存合理化建议
			String suggestId = SequenceManager.getSequence("");
			TPcSuggestPO suggestPo = new TPcSuggestPO();
			suggestPo.setSuggestId(Long.parseLong(suggestId));
			suggestPo.setOfferName(offerName);
			suggestPo.setOfferPhone(offerPhone);
			suggestPo.setSuggestInfo(suggestInfo);
			suggestPo.setCreateBy(userId);
			suggestPo.setCreateDate(new Date());
			dao.insert(suggestPo);
			
			strResult.put("\"success\"", "true");
			strResult.put("\"msg\"", "操作成功");
			strResult.put("\"result\"", result);
			
			act.setOutData("\"strResult\"", strResult);
		} catch (Exception e) {
			strResult.put("\"success\"", "false");
			strResult.put("\"msg\"", "网络出错,请重新执行!");
			act.setOutData("\"strResult\"", strResult);
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"未知错误!");
			act.setException(e1);
		}
	}
}
