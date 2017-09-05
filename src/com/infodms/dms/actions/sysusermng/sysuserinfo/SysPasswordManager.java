package com.infodms.dms.actions.sysusermng.sysuserinfo;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.actions.common.UserManager;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.MD5Util;
import com.infodms.dms.dao.claim.dealerClaimMng.ClaimBillMaintainDAO;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TcUserPO;
import com.infodms.dms.po.TmClaimContactPhonePO;
import com.infodms.dms.po.TmDealerPO;
import com.infodms.dms.po.TmDealerUserInfoRecordPO;
import com.infodms.dms.po.TtAsWrOldReturnedPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.POFactory;
import com.infoservice.po3.POFactoryBuilder;
import com.infoservice.po3.bean.PageResult;

public class SysPasswordManager {
	public Logger logger = Logger.getLogger(SysPasswordManager.class);
	private POFactory factory = POFactoryBuilder.getInstance();
	private ActionContext act = ActionContext.getContext();
	private AclUserBean logonUser = (AclUserBean) act.getSession().get(
			Constant.LOGON_USER);

	private final String searchUserInfoURL = "/jsp/sysusermng/SearchUserPasswordModefyInfo.jsp";

	private final String CLAIM_PHONE_URL = "/jsp/sysusermng/claimContactPhone.jsp"; // 服务站站长及三包员联系电话
	private final String CLAIM_PHONE_OEM_URL = "/jsp/sysusermng/claimContactPhoneOem.jsp"; // 车厂站长及三包员联系电话
	private final String SHOW_CONTACT_PHONE = "/jsp/sysusermng/showContactPhone.jsp"; // 索赔单展示站长及三包员联系电话
	private final String CLAIM_HOT_LINE_PHONE_MODIFY = "/jsp/sysusermng/hotLinePhoneModify.jsp"; // 热线修改页面
	private final String CLAIM_PHONE_SHOW_CHANGE_URL = "/jsp/sysusermng/showContactChangeList.jsp";

	/**
	 * Function : 密码维护初始化
	 * 
	 * @throws : Exception update : 修改密码的读写方式 LastUpdate : 2010-5-28
	 */
	public void queryUserHistoryPasswordInfoInit() {
		try {
			// 得到当前登陆用户名
			Long userId = logonUser.getUserId();
			// 得到当前登陆用户的基本信息
			TcUserPO user = new TcUserPO();
			user.setUserId(userId);
			user = factory.select(user).get(0);
			act.setOutData("user", user);
			act.setOutData("isAdmin", UserManager.isAdmin(userId));

			/*
			 * TcUserPasswordPO password = new TcUserPasswordPO();
			 * password.setUserId(userId); List<TcUserPasswordPO> passList =
			 * factory.select(password); if(passList.size() > 0){ password =
			 * passList.get(0); } act.setOutData("password", password);
			 */

			act.setForword(searchUserInfoURL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "密码维护初始化");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * Function : 密码修改
	 * 
	 * @throws : Exception LastUpdate : 2010-1-12
	 */
	public void modefyPasswordInfo() {
		try {
			RequestWrapper request = act.getRequest();

			// 得到页面参数
			String userId = CommonUtils.checkNull(request
					.getParamValue("userId"));
			String password = CommonUtils.checkNull(request
					.getParamValue("passId"));
			// 现在用的密码
			String nowPassword = CommonUtils.checkNull(request
					.getParamValue("nowPassword"));
			// 新密码
			String newPassword = CommonUtils.checkNull(request
					.getParamValue("newPassword"));

			boolean isAdmin = Boolean.valueOf(CommonUtils.checkNull(request
					.getParamValue("isAdmin")));

			if (null != password && !"".equals(password)) {
				// 验证密码是否相同
				Boolean checkStatus = false;
				// checkStatus = MD5Util.checkPassword(nowPassword, new
				// Long(userId));
				checkStatus = (MD5Util.MD5Encryption(nowPassword))
						.equals(password);
				if (checkStatus) {

					TcUserPO poDB = new TcUserPO();
					poDB.setUserId(new Long(userId));
					TcUserPO poValue = new TcUserPO();
					poValue.setPassword(MD5Util.MD5Encryption(newPassword));
					poValue.setUpdateBy(new Long(userId));
					poValue.setUpdateDate(new Date(System.currentTimeMillis()));
					Calendar cal = Calendar.getInstance();
					if (isAdmin) {
						cal.add(Calendar.MONTH, 3);
					} else {
						cal.add(Calendar.MONTH, 6);
					}
					poValue.setOverDate(cal.getTime());
					factory.update(poDB, poValue);

					StringBuffer checkMessage = new StringBuffer();
					checkMessage.append("密码修改成功");
					act.setOutData("checkMessage", checkMessage);
				} else {
					StringBuffer checkMessage = new StringBuffer();
					checkMessage.append("密码错误，请重新输入");
					act.setOutData("checkMessage", checkMessage);
				}
			} else {
				// 如果此用户没有创建过密码，则在此新建

				TcUserPO tp = new TcUserPO();
				tp.setUserId(new Long(userId));
				TcUserPO po = new TcUserPO();
				po.setPassword(MD5Util.MD5Encryption(newPassword));
				po.setUpdateBy(new Long(userId));
				po.setUpdateDate(new Date(System.currentTimeMillis()));
				Calendar cal = Calendar.getInstance();
				if (isAdmin) {
					cal.add(Calendar.MONTH, 3);
				} else {
					cal.add(Calendar.MONTH, 6);
				}
				po.setOverDate(cal.getTime());
				factory.update(tp, po);

				StringBuffer checkMessage = new StringBuffer();
				checkMessage.append("密码创建成功");
				act.setOutData("checkMessage", checkMessage);
			}

			// 得到当前登陆用户的基本信息
			TcUserPO user = new TcUserPO();
			user.setUserId(new Long(userId));
			user = factory.select(user).get(0);
			act.setOutData("user", user);

			act.setForword(searchUserInfoURL);

		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "个人信息查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * 
	 * 2011-05-07 站长及三包员联系电话维护 add by kevinyin
	 */
	public void maintainClaimPhone() {
		try {

			RequestWrapper request = act.getRequest();

			String dealerId = logonUser.getDealerId();
			String COMMAND = request.getParamValue("COMMAND");
			String perPose = request.getParamValue("CON_PER_POSE");
			String perName = request.getParamValue("CON_PER_NAME");
			String perPhone = request.getParamValue("CON_PER_PHONE");
			String status = request.getParamValue("CON_STATUS");
			String condealerId = request.getParamValue("con_dealer_id");
			
			ClaimBillMaintainDAO dao = new ClaimBillMaintainDAO();
			
			if(dealerId==null){
				PageResult<Map<String, Object>> ps = null;
				// 处理当前页
				Integer curPage = request.getParamValue("curPage") != null ? Integer
						.parseInt(request.getParamValue("curPage")) : 1;
				ps = dao.queryContactChangeHistoryPg(request, dealerId, perPose, perPhone, perName, condealerId, status, curPage, Constant.PAGE_SIZE);
				act.setOutData("ps", ps);
				act.setForword(CLAIM_PHONE_OEM_URL);
			}else{
				if(COMMAND!=null){
					// 查询该经销商所有站长三包员信息
					List<Map<String, Object>> listPo = null;
					
					dao = new ClaimBillMaintainDAO();
					listPo = dao.queryDealerContactList(request, dealerId, perPose, perPhone, perName,condealerId, status);
					
					act.setOutData("listPo", listPo);
				}
				request.setAttribute("DEALER_ID", dealerId);
				act.setForword(CLAIM_PHONE_URL);
			}
			request.setAttribute("perPose", perPose);
			request.setAttribute("perPhone", perPhone);
			request.setAttribute("perName", perName);
			request.setAttribute("status", status);
			request.setAttribute("dealerId", dealerId);
			request.setAttribute("condealerId", condealerId);
			
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "查询添加站长及三包员信息失败");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 
	 * 2011-05-07 保存热线 add by kevinyin
	 */
	public void saveDealerHotLinePhone() {
		try {

			RequestWrapper request = act.getRequest();
			ClaimBillMaintainDAO dao = new ClaimBillMaintainDAO();
			String dealerId = request.getParamValue("dealer_id");
			String isoem = logonUser.getDealerId();
			String COMMAND = request.getParamValue("COMMAND");
			
				if(COMMAND!=null){
					TmDealerPO dealer = new TmDealerPO();
					dealer.setDealerId(Long.valueOf(dealerId));
					
					dealer = (TmDealerPO) dao.select(dealer).get(0);
					String hotLinePhone = request.getParamValue("HOT_LINE_PHONE")==null?"0":request.getParamValue("HOT_LINE_PHONE");
					String oldHotPhone = "";
					if(dealer.getHotLinePhone()==null){
						oldHotPhone = "0";
					}else{
						oldHotPhone = dealer.getHotLinePhone();
					}
					if(!oldHotPhone.trim().equals(hotLinePhone)){
						TmDealerPO d = new TmDealerPO();
						d.setDealerId(Long.valueOf(dealerId));
						TmDealerPO dealerHot = new TmDealerPO();
						dealerHot.setHotLinePhone(hotLinePhone);
						
						dao.update(d, dealerHot);
						
						//更改修改历史表
						TmDealerUserInfoRecordPO duir = new TmDealerUserInfoRecordPO();
						duir.setChangeDate(new Date());
						duir.setChangeUser(logonUser.getUserId());
						duir.setHistoryId(Long.valueOf(SequenceManager.getSequence("")));
						duir.setId(null);//没有对应ID
						duir.setDealerId(Long.valueOf(dealerId));
						duir.setNewHotLine(hotLinePhone);
						duir.setOldHotLine(oldHotPhone);
						dao.insert(duir);
					}
					if(isoem!=null){
						request.setAttribute("DEALER_ID", dealerId);
						act.setForword(CLAIM_PHONE_URL);
					}else{
						act.setForword(CLAIM_PHONE_OEM_URL);
					}
				}else{
					List<Map<String, Object>> listBean = dao.queryDealerContactList(request, dealerId, null, null, null, null, null);
					
					act.setOutData("listBean", listBean);
					act.setForword(CLAIM_HOT_LINE_PHONE_MODIFY);
				}
				
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "查询添加站长及三包员信息失败");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 保存站长及三包员联系电话 add by kevinyin 2011-05-18
	 */
	public void saveClaimPhone() {
		try {

			RequestWrapper request = act.getRequest();
			String isoem = logonUser.getDealerId();
			String[] oemdealerId = null;
			String dealerId = null;
			if(isoem==null){
				oemdealerId = request.getParamValues("dealer_id");
			}else{
				dealerId = request.getParamValue("dealer_id");
			}
			String id = request.getParamValue("contactId");

			String[] perName = request.getParamValues("PER_NAME");
			String[] perPhone = request.getParamValues("PER_PHONE");
			String[] perPose = request.getParamValues("PER_POSE");
			String[] perRemark = request.getParamValues("PER_REMARK");
			String[] connectId = request.getParamValues("CONNECT_ID");
			String[] status = request.getParamValues("STATUS");
			
			
			Long userId = logonUser.getUserId();

			if (connectId != null && connectId.length > 0) {
				for (int i = 0; i < connectId.length; i++) {

					// if("".equals(id)){

					if (connectId[i] != null) { // 存在作更新

						TmClaimContactPhonePO tccp = new TmClaimContactPhonePO();
						tccp.setId(Long.valueOf(connectId[i]));
						tccp = factory.select(tccp).get(0);
						
						//比较哪些值作过更改
						
						String newPerName = perName[i]==null?"":perName[i].trim();
						String newPerPhone = perPhone[i]==null?"":perPhone[i].trim();
						String newPerPose = perPose[i]==null?"":perPose[i].trim();
						String newPerRemark = perRemark[i]==null?"":perRemark[i].trim();
						String newStatus = status[i]==null?"":status[i].trim();
						
						String oldPerName = tccp.getPerName()==null?"":tccp.getPerName().trim();
						String oldPerPhone = tccp.getPerPhone()==null?"":tccp.getPerPhone().trim();
						String oldPerPose = tccp.getPerPose()==null?"":tccp.getPerPose().toString().trim();
						String oldPerRemark = tccp.getPerRemark()==null?"":tccp.getPerRemark().trim();
						String oldStatus = tccp.getStatus()==null?"":tccp.getStatus().toString().trim();
						
						//记录修改历史
						
						boolean changeFlag = false;
						TmDealerUserInfoRecordPO duir = new TmDealerUserInfoRecordPO();
						duir.setChangeDate(new Date());
						duir.setChangeUser(userId);
						duir.setHistoryId(Long.valueOf(SequenceManager.getSequence("")));
						duir.setId(Long.valueOf(connectId[i]));
						
						if(!oldPerName.equals(newPerName)){
							
							changeFlag = true;
						}
						if(!oldPerPhone.equals(newPerPhone)){
							
							changeFlag = true;
						}
						if(!oldPerPose.equals(newPerPose)){
							
							changeFlag = true;
						}
						/*if(!oldPerRemark.equals(newPerRemark)){
							duir.setOldName(oldPerName);
							duir.setNewName(newPerName);
						}*/
						if(!oldStatus.equals(newStatus)){
							
							changeFlag = true;
						}
							
						if(changeFlag){	
							duir.setOldName(oldPerName);
							duir.setNewName(newPerName);
							duir.setOldPhone(oldPerPhone);
							duir.setNewPhone(newPerPhone);
							duir.setOldPose(oldPerPose);
							duir.setNewPose(newPerPose);
							duir.setOldStatus(Integer.valueOf(oldStatus));
							duir.setNewStatus(Integer.valueOf(newStatus));
							if(isoem==null){
								duir.setDealerId(Long.valueOf(oemdealerId[i]));
							}else{
								duir.setDealerId(Long.valueOf(dealerId));
							}
							
							factory.insert(duir);
						}
						
						TmClaimContactPhonePO tccpSet = new TmClaimContactPhonePO();
						tccpSet.setId(Long.valueOf(connectId[i]));
						TmClaimContactPhonePO ccp1 = new TmClaimContactPhonePO();

						ccp1.setPerName(newPerName);
						ccp1.setPerPhone(newPerPhone);
						ccp1.setPerPose(Integer.valueOf(newPerPose));
						ccp1.setPerRemark(newPerRemark);
						ccp1.setUserId(userId);
						if(isoem==null){
							ccp1.setDealerId(Long.valueOf(oemdealerId[i]));
						}else{
							ccp1.setDealerId(Long.valueOf(dealerId));
						}
						ccp1.setUpdateBy(userId);
						ccp1.setUpdateDate(new Date());
						ccp1.setStatus(Integer.valueOf(newStatus));

						factory.update(tccpSet, ccp1);

					} else { // 不存在作新增操作

						TmClaimContactPhonePO ccp1 = new TmClaimContactPhonePO();

						ccp1.setId(Long.parseLong(SequenceManager
								.getSequence("")));
						ccp1.setPerName(perName[i]);
						ccp1.setPerPhone(perPhone[i]);
						ccp1.setPerPose(Integer.valueOf(perPose[i]));
						ccp1.setPerRemark(perRemark[i]);
						ccp1.setUserId(userId);
						if(isoem==null){
							ccp1.setDealerId(Long.valueOf(oemdealerId[i]));
						}else{
							ccp1.setDealerId(Long.valueOf(dealerId));
						}
						ccp1.setCreateDate(new Date());
						ccp1.setCreateBy(userId);
						ccp1.setStatus(Constant.STATUS_ENABLE);

						factory.insert(ccp1);
					}

				}
			}
			maintainClaimPhone();
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "删除站长及三包员信息失败");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * 删除站长及三包员联系电话 add by kevinyin 2011-05-18
	 */
	public void delClaimPhone() {
		try {

			RequestWrapper request = act.getRequest();

			String id = request.getParamValue("contactId");
			String dealerId = request.getParamValue("dealer_id");

			TmClaimContactPhonePO ccp = new TmClaimContactPhonePO();
			ccp.setId(Long.valueOf(id));
			
			TmClaimContactPhonePO ccpVal = new TmClaimContactPhonePO();
			ccpVal.setStatus(Constant.STATUS_DISABLE);
			ccpVal.setUpdateBy(logonUser.getUserId());
			ccpVal.setUpdateDate(new Date());
			factory.update(ccp, ccpVal);

			TmDealerUserInfoRecordPO duir = new TmDealerUserInfoRecordPO();
			duir.setId(Long.valueOf(id));
			duir.setChangeDate(new Date());
			duir.setChangeUser(logonUser.getUserId());
			duir.setHistoryId(Long.valueOf(SequenceManager.getSequence("")));
			duir.setOldStatus(Constant.STATUS_ENABLE);
			duir.setNewStatus(Constant.STATUS_DISABLE);
			duir.setDealerId(Long.valueOf(dealerId));
			
			factory.insert(duir);
			
			maintainClaimPhone();

		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "删除站长及三包员信息失败");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * 查询站长及三包员联系电话 add by kevinyin 2011-05-18
	 */
	public void showContactPhone() {
		try {

			RequestWrapper request = act.getRequest();

			String dealerId = request.getParamValue("DEALER_ID");

			// //查询该经销商所有站长三包员信息
			// TmClaimContactPhonePO ccp2 = new TmClaimContactPhonePO();
			// ccp2.setDealerId(Long.valueOf(dealerId));
			// List<TmClaimContactPhonePO> listPo = factory.select(ccp2);
			//
			// if(listPo!=null&&listPo.size()>0){
			//
			// // TcUserPO user = new TcUserPO();
			//
			// // user.setUserId(listPo.get(listPo.size()-1).getUserId());
			// // List<TcUserPO> listUser = factory.select(user);
			//
			// // act.setOutData("acnt", listUser.get(0).getAcnt());
			//
			// act.setOutData("listPo", listPo);
			//
			// }else
			// {
			//
			// }
			StringBuffer sql = new StringBuffer();
			sql.append("select max(t.TEL) Tel from tt_as_wr_old_returned t where t.DEALER_ID = "
					+ dealerId);
			ClaimBillMaintainDAO dao = ClaimBillMaintainDAO.getInstance();
			List<TtAsWrOldReturnedPO> list = dao.select(
					TtAsWrOldReturnedPO.class, sql.toString(), null);
			if (list.size() > 0) {
				act.setOutData("listPo", list.get(0).getTel());
			}

			act.setForword(SHOW_CONTACT_PHONE);

		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "删除站长及三包员信息失败");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 查询修改记录 add by kevinyin 2011-05-18
	 */
	public void queryContactChangeHistory() {
		try {

			RequestWrapper request = act.getRequest();

			String dealerId = request.getParamValue("dealer_id");
			String contactId = request.getParamValue("CONTACT_ID");
			String status = request.getParamValue("CON_STATUS");
			String change_date_start = request.getParamValue("change_date_start");
			String change_date_end = request.getParamValue("change_date_end");
			String COMMAND = request.getParamValue("COMMAND");
			
			if(COMMAND!=null){

				PageResult<Map<String, Object>> ps = null;
				// 处理当前页
				Integer curPage = request.getParamValue("curPage") != null ? Integer
						.parseInt(request.getParamValue("curPage")) : 1;
				// 查询语句
	
				ClaimBillMaintainDAO dao = new ClaimBillMaintainDAO();
				ps = dao.queryContactChangeHistoryList(request, dealerId,status, contactId,change_date_start,change_date_end, curPage, Constant.PAGE_SIZE);
				
				act.setOutData("ps", ps);
			}else{
				request.setAttribute("CONTACT_ID", contactId);
			}

			act.setForword(CLAIM_PHONE_SHOW_CHANGE_URL);

		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "删除站长及三包员信息失败");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
}
