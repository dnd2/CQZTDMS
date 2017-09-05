package com.infodms.dms.actions.sales.usermng;

import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import jxl.Workbook;
import jxl.format.Alignment;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import org.apache.log4j.Logger;

//import com.infodms.dms.actions.sales.marketmanage.planmanage.ActivitiesCamCost;
import com.infodms.dms.actions.sales.storageManage.AddressAddApply;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.materialManager.MaterialGroupManagerDao;
import com.infodms.dms.common.relation.DealerRelation;
import com.infodms.dms.dao.sales.usermng.UserManageDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TPcGroupPO;
import com.infodms.dms.po.TmDealerPO;
import com.infodms.dms.po.TtVsDemioGetintegPO;
import com.infodms.dms.po.TtVsIntegrationChangePO;
import com.infodms.dms.po.TtVsPersonChangePO;
import com.infodms.dms.po.TtVsPersonPO;
import com.infodms.dms.po.TtVsPersonRegistPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

public class UserManage {
	public Logger logger = Logger.getLogger(AddressAddApply.class);
	private ActionContext act = ActionContext.getContext();
	RequestWrapper request = act.getRequest();
	private ResponseWrapper response = act.getResponse();
	private UserManageDao dao = UserManageDao.getInstance();
	private final String USER_REGISTER_INIT = "/jsp/sales/userMng/user_register_init.jsp";
	private final String USER_REGISTER = "/jsp/sales/userMng/user_register.jsp";
	private final String USER_SELECT_INIT = "/jsp/sales/userMng/user_select.jsp";
	private final String USER_AUDIT_INIT = "/jsp/sales/userMng/user_audit_init.jsp";
	private final String USER_AUDIT = "/jsp/sales/userMng/user_audit.jsp";
	private final String USER_UPDATE_INIT = "/jsp/sales/userMng/user_update.jsp";
	private final String USER_AUTHEN_INIT = "/jsp/sales/userMng/user_authentication_init.jsp";
	private final String USER_AUTHEN = "/jsp/sales/userMng/user_authentication.jsp";
	private final String PERSON_SELECT_INIT = "/jsp/sales/userMng/person_leave.jsp";
	private final String PERSON_UPDATE_INIT = "/jsp/sales/userMng/person_update_init.jsp";
	private final String PERSON_UPDATE_LOAD = "/jsp/sales/userMng/person_update.jsp";
	private final String PERSON_CHANGE_INIT = "/jsp/sales/userMng/person_change_init.jsp";
	private final String PERSON_SELECT_PRE = "/jsp/sales/userMng/person_select_init.jsp";
	private final String PERSON_SELECT_DETAIL = "/jsp/sales/userMng/person_select.jsp";
	private final String PERSON_SWITCH_PRE = "/jsp/sales/userMng/person_switch_init.jsp";
	private final String PERSON_SWITCH_DETAIL = "/jsp/sales/userMng/person_switch.jsp";
	private final String PERSON_COUNT_INIT = "/jsp/sales/userMng/person_count_init.jsp";
	private final String PERSON_DETAIL_INIT = "/jsp/sales/userMng/person_detail_init.jsp";

	/**
	 * 人员注册初始化
	 */
	public void userRegisterInit() {
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			List<Map<String, Object>> areaList = MaterialGroupManagerDao.getDealerBusinessAll(logonUser.getPoseId().toString());
//
//			ActivitiesCamCost acc = new ActivitiesCamCost();
//			String areaIds = acc.getAreaStr(areaList);
//
//			act.setOutData("areaIds", areaIds);
			act.setOutData("areaList", areaList);

			act.setForword(USER_REGISTER_INIT);
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "人员注册初始化");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * 人员注册页面跳转
	 */
	public void userRegister() {
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);

			DealerRelation dr = new DealerRelation();
			List<Map<String, Object>> dlrList = dr.getDealerPoseRelation(logonUser.getCompanyId(), logonUser.getPoseId());
			//获取经销商所有的组 start  2014-10-30
			TPcGroupPO tgp=new TPcGroupPO();
			tgp.setDealerId(new Long(logonUser.getDealerId()));
			List<PO> tgpList=dao.select(tgp);
			act.setOutData("tgpList", tgpList);
			//end
			act.setOutData("dlrList", dlrList);
			act.setOutData("dealerId", logonUser.getDealerId());
			
			act.setForword(USER_REGISTER);
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "人员注册页面跳转");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * 人员的注册（添加到数据库）
	 */
	public void userAdd() {
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			String dealerId = logonUser.getDealerId().toString();
			String userId = logonUser.getUserId().toString();
			// String createDate=request.getParamValue("createDate");
			String idNO = request.getParamValue("idNO");
			String gender = request.getParamValue("gender");
			String degree=request.getParamValue("degree");
			String name = request.getParamValue("name");
			String email = request.getParamValue("email");
			String mobile = request.getParamValue("mobile");
			String position = request.getParamValue("position");
			String isInvestor = request.getParamValue("isInvestor");
			String entryDate0 = request.getParamValue("entryDate");
			// 日期转换
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			Date entryDate = df.parse(entryDate0);
			String bank = request.getParamValue("bank");
			String bankCardNO = request.getParamValue("bankCardNO");
			String remark = request.getParamValue("remark");
			String status = request.getParamValue("status");

			TtVsPersonRegistPO tvprp = new TtVsPersonRegistPO();
			tvprp.setDealerId(Long.parseLong(dealerId));
			tvprp.setCreateUserId(Long.parseLong(userId));
			tvprp.setCreateDate(new Date());
			tvprp.setIdNo(idNO);
			tvprp.setName(name);
			tvprp.setGender(Long.parseLong(gender));
			tvprp.setEmail(email==null?"":email);
			tvprp.setPosition(Long.parseLong(position));
			tvprp.setStatus(status == null ? null : Long.parseLong(status));
			tvprp.setIsInvestor(isInvestor == null ? null : Long.parseLong(isInvestor));
			// 入职日期
			tvprp.setEntryDate(entryDate);
			tvprp.setMobile(mobile);
			tvprp.setBank(bank == null ? null : Long.parseLong(bank));
			tvprp.setBankcardNo(bankCardNO);
			tvprp.setRemark(remark==null?"":remark);
			tvprp.setDegree(new Long(degree));
			dao.userRegist(tvprp);
			act.setOutData("subFlag", "success");
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "人员注册异常");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * 人员注册表的查询
	 * 
	 */
	public void userSelect() {
		AclUserBean logonUser = null;
		try {

			TtVsPersonRegistPO tvprp = new TtVsPersonRegistPO();
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			// 新增页面只能查询未提报和驳回的
			String flagType = request.getParamValue("flagType");
			String dealerId = logonUser.getDealerId();
			String idNO = request.getParamValue("idNO");
			String gender = request.getParamValue("gender");
			String mobile = request.getParamValue("mobile");
			String position = request.getParamValue("position");
			String name = CommonUtils.checkNull(request.getParamValue("name"));
			String bank = request.getParamValue("bank");
			String isInvestor = request.getParamValue("isInvestor");
			String email = request.getParamValue("email");
			String status = request.getParamValue("status");
			String dealerCodes=request.getParamValue("dealerCode");
			tvprp.setName(name);
			tvprp.setIdNo(idNO);
			tvprp.setGender(gender == null ? null : new Long(gender));
			tvprp.setMobile(mobile);
			tvprp.setPosition(position == null ? null : new Long(position));
			tvprp.setBank(bank == null ? null : new Long(bank));
			tvprp.setIsInvestor(isInvestor == null ? null : new Long(isInvestor));
			tvprp.setEmail(email);
			tvprp.setStatus(status == null ? null : Long.parseLong(status));
			String pageSize0 = CommonUtils.checkNull(request.getParamValue("pagesizes"));
			String curPage0 = CommonUtils.checkNull(request.getParamValue("curPage"));
			tvprp.setDealerId(dealerId == null ? null : Long.parseLong(dealerId));
			int curPage = 1;
			int pageSize = 10;
			if (curPage0 != null && !"".equals(curPage0)) {
				curPage = Integer.parseInt(curPage0);
			}
			if (pageSize0 != null && !"".equals(pageSize0)) {
				pageSize = Integer.parseInt(pageSize0);
			}
			PageResult<Map<String, Object>> ps = null;
			if ("10431003".equals(logonUser.getDutyType())) {
				ps = dao.userLargeSelect(dealerCodes,tvprp, logonUser.getOrgId(), flagType, pageSize, curPage);

			} else if ("10431004".equals(logonUser.getDutyType())) {
				ps = dao.userSmallSelect(dealerCodes,tvprp, logonUser.getOrgId(), flagType, pageSize, curPage);
			} else {
				ps = dao.userSelect(dealerCodes,tvprp, flagType, pageSize, curPage);
			}
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "人员查询异常");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * 人员注册表的查询功能初始化
	 */
	public void userSelectInit() {
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			act.setOutData("dutyType", logonUser.getDutyType());
			act.setForword(USER_SELECT_INIT);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "人员查询初始化异常");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * 人员注册表审核初始化
	 */
	public void userAuditInit() {
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			act.setForword(USER_AUDIT_INIT);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "人员查询初始化异常");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * 删除人员
	 */
	public void submitUser(){
			AclUserBean logonUser = null ;
			try {
				logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
				String registId=request.getParamValue("registId");
				TtVsPersonRegistPO tvprp=new TtVsPersonRegistPO();
				tvprp.setRegistId(registId==null?null:Long.parseLong(registId));
				TtVsPersonRegistPO tvprp1=(TtVsPersonRegistPO) dao.select(tvprp).get(0);
				tvprp1.setStatus(Constant.USER_STATUS_SUBMIT);
				int count=dao.update(tvprp,tvprp1);
				act.setOutData("count", count);
			} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"人员信息修改异常");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 加载人员注册表审核数据
	 */
	public void userAuditLoad() {
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			String registId = request.getParamValue("registId");
			TtVsPersonRegistPO tvprp = new TtVsPersonRegistPO();
			tvprp.setRegistId(Long.parseLong(registId));
			tvprp = (TtVsPersonRegistPO) dao.select(tvprp).get(0);
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			String entryDate = df.format(tvprp.getEntryDate());
			act.setOutData("tvprp", tvprp);
			act.setOutData("entryDate", entryDate);
			//获取经销商的代码和名称
			TmDealerPO dealer=new TmDealerPO();
			dealer.setDealerId(tvprp.getDealerId());
			dealer=(TmDealerPO) dao.select(dealer).get(0);
			act.setOutData("dealer",dealer);
			act.setForword(USER_AUDIT);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "人员审核初始化异常");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * 人员注册表的人员审核
	 */
	public void userAudit() {
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			TtVsPersonRegistPO tvprp = new TtVsPersonRegistPO();
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			String registId = request.getParamValue("registId");

			String status = request.getParamValue("status");

			String remark = CommonUtils.checkNull(request.getParamValue("remark"));
			tvprp.setAuditUserId(logonUser.getUserId());
			tvprp.setAuditTime(new Date());

			tvprp.setStatus(status==null?null:Long.parseLong(status));
			TtVsPersonRegistPO tvprp1=new TtVsPersonRegistPO();
			tvprp1.setRegistId(registId==null?null:Long.parseLong(registId));
			int a=dao.update(tvprp1,tvprp);
			tvprp1=(TtVsPersonRegistPO) dao.select(tvprp1).get(0);
			//审核通过的把数据加入到机构人员表中
			if(a>0&&tvprp.getStatus()==99991003){
				//原来的人员信息
				TtVsPersonPO tvppOld=new TtVsPersonPO();
				
				//根据id_no查询机构人员信息中是否存在记录
				TtVsPersonPO tvpps=new TtVsPersonPO();
				tvpps.setIdNo(tvprp1.getIdNo());
				List<PO> list=dao.select(tvpps);
				int count=list.size();
				if(count>0){
					tvppOld=(TtVsPersonPO) dao.select(tvpps).get(0);
				}
				// 向人员表里面插入数据
				TtVsPersonPO tvpp = new TtVsPersonPO();
				tvpp.setIdNo(tvprp1.getIdNo().trim());
				tvpp.setGender(tvprp1.getGender());
				tvpp.setName(tvprp1.getName());
				tvpp.setEmail(tvprp1.getEmail());
				tvpp.setMobile(tvprp1.getMobile());
				tvpp.setPosition(tvprp1.getPosition());
				tvpp.setDegree(tvprp1.getDegree());
				// 设置人员在职
				tvpp.setPositionStatus(Constant.POSITION_STATUS_ON);
				tvpp.setIsInvestor(tvprp1.getIsInvestor());

				// 设置认证等级
				tvpp.setAuthenticationLevel(null);

				tvpp.setEntryDate(tvprp1.getEntryDate());
				tvpp.setBank(tvprp1.getBank());
				tvpp.setBankCardno(tvprp1.getBankcardNo());
				tvpp.setRemark(tvprp1.getRemark());
				// 连续三个月业绩为0默认设置为否
				tvpp.setThreeMonthZero(Constant.THREE_MONTH_ZERO_NO);

				if (tvprp1.getDealerId() != null) {
					tvpp.setDealerId(tvprp1.getDealerId());
				}
				tvpp.setCreateDate(new Date());
				tvpp.setLastAuditTime(new Date());
				//如果存在记录就修改
				if(count>0){
					dao.update(tvpps, tvpp);
					//获取原来的业绩积分
					 Long performanceInteg=tvppOld.getPerformanceInteg();
					 if(performanceInteg==null){
						 performanceInteg=new Long(0);
					 }
					addIntegChange(performanceInteg,performanceInteg,tvpp,Constant.INTEG_CHANGE_INIT.toString());
				}else{
					//连续三个月业绩为0默认设置为否
					tvpp.setThreeMonthZero(Constant.THREE_MONTH_ZERO_NO);
					//设置认证等级
					tvpp.setAuthenticationLevel(null);
					dao.personInsert(tvpp);
					addIntegChange(new Long(0),new Long(0),tvpp,Constant.INTEG_CHANGE_INIT.toString());
				}
				// 人员变动表里写入记录
				TtVsPersonRegistPO tvprp01=new TtVsPersonRegistPO();
				tvprp01.setRegistId(registId==null?null:Long.parseLong(registId));
				TtVsPersonRegistPO tvprp05=new TtVsPersonRegistPO();
				tvprp05=(TtVsPersonRegistPO) dao.select(tvprp01).get(0);
				TtVsPersonPO tvpp0 = new TtVsPersonPO();
				tvpp0.setIdNo(tvprp05.getIdNo());
				tvpp0 = (TtVsPersonPO) dao.select(tvpp0).get(0);
				inertPersonChange(tvpp0, Constant.PERSON_CHANGE_TYPE_AUDIT);
				act.setOutData("subFlag", "success");
			}else if(a>0&&Constant.USER_STATUS_REJECT.toString().equals(tvprp.getStatus().toString())){
					act.setOutData("subFlag", "success") ;
			}else {
				act.setOutData("subFlag", "failure");

			}

		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "人员审核异常");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * 人员信息修改加载
	 */
	public void userUpdateLoad() {
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			String registId = request.getParamValue("registId");
			TtVsPersonRegistPO tvprp = new TtVsPersonRegistPO();
			tvprp.setRegistId(Long.parseLong(registId));
			tvprp = (TtVsPersonRegistPO) dao.select(tvprp).get(0);
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			String entryDate = df.format(tvprp.getEntryDate());
			act.setOutData("tvprp", tvprp);
			act.setOutData("entryDate", entryDate);
			act.setOutData("dealerId", logonUser.getDealerId());
			act.setForword(USER_UPDATE_INIT);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "人员修改初始化异常");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * 修改人员信息
	 */
	public void userUpdate() {
		AclUserBean logonUser = null;
		try {

			logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
			TtVsPersonRegistPO tvprp=new TtVsPersonRegistPO();
			logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
			String registId=request.getParamValue("registId");
			String idNO=request.getParamValue("idNO");
			String gender=request.getParamValue("gender");
			String mobile=request.getParamValue("mobile");
			String position=request.getParamValue("position");
			String name=CommonUtils.checkNull(request.getParamValue("name"));
			String bank=request.getParamValue("bank");
			String isInvestor=request.getParamValue("isInvestor");
			String email=request.getParamValue("email");
			String remark=request.getParamValue("remark");
			String status=request.getParamValue("status");
			String bankCardNo=request.getParamValue("bankCardNO");
			String degree=request.getParamValue("degree");
			String flag=request.getParamValue("flag");
			String entryDate=request.getParamValue("entryDate");
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			Date entryDates = df.parse(entryDate);
			tvprp.setName(name);
			tvprp.setIdNo(idNO);
			tvprp.setGender(gender == null ? null : new Long(gender));
			tvprp.setAuditUserId(logonUser.getUserId());
			tvprp.setAuditTime(new Date());
			tvprp.setMobile(mobile==null?"":mobile);
			tvprp.setPosition(position == null ? null : new Long(position));
			tvprp.setBank(bank == null ? null : new Long(bank));
			tvprp.setIsInvestor(isInvestor == null ? null : new Long(isInvestor));
			tvprp.setEmail(email==null?"":email);
			tvprp.setBankcardNo(bankCardNo==null?"":bankCardNo);
			tvprp.setEntryDate(entryDates);
			//保存
			if(!"1".equals(flag)){
				tvprp.setStatus(status==null?null:Long.parseLong(status));
			}
			tvprp.setDegree(degree==null?null:new Long(degree));
			tvprp.setRemark(remark==null?"":remark);
			TtVsPersonRegistPO tvprp1 = new TtVsPersonRegistPO();
			tvprp1.setRegistId(registId == null ? null : Long.parseLong(registId));
			int a = dao.update(tvprp1, tvprp);
			if (a > 0) {
				act.setOutData("subFlag", "success");
			} else {
				act.setOutData("subFlag", "failure");
			}

		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "人员信息修改异常");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * 删除人员
	 */
	public void deleteUser() {
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			String registId = request.getParamValue("registId");
			TtVsPersonRegistPO tvprp = new TtVsPersonRegistPO();
			tvprp.setRegistId(registId == null ? null : Long.parseLong(registId));
			TtVsPersonRegistPO tvprp1 = (TtVsPersonRegistPO) dao.select(tvprp).get(0);
			tvprp1.setStatus(Constant.USER_STATUS_CANCEL);
			dao.update(tvprp, tvprp1);
			act.setForword(USER_REGISTER_INIT);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "人员信息修改异常");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * 验证身份证是否重复
	 */
	public void userIdNoCheck() {
		AclUserBean logonUser = null;
		try {
			String idNo = request.getParamValue("idNO");
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			String flag=request.getParamValue("flag");
			String personId=request.getParamValue("personId");
			int count = dao.userIdNoCheck(idNo,flag,personId);
			act.setOutData("count", count);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "人员身份证验证异常");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 验证银行卡号是否重复
	 */
	public void userBankCardCheck() {
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			String bankCardNO = request.getParamValue("bankCardNO");
			String flag=request.getParamValue("flag");
			String personId=request.getParamValue("personId");
			int count = dao.userBankCardCheck(bankCardNO,flag,personId);
			act.setOutData("count", count);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "人员银行卡号验证异常");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 验证注册的人人员中经理级别的不能超过1个
	 */
	public void userPisitionCountCheck() {
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			String position=request.getParamValue("position");
			String dealerId=request.getParamValue("dealerId");
			String id_no=request.getParamValue("idNO");
			Long dealerIds=new Long(dealerId);
			Long positions=new Long(position);
			int count = dao.userPositionCount(dealerIds,positions,id_no);
			act.setOutData("count", count);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "人员数量验证异常");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 顾问认证初始化
	 */
	public void userAuthenInit() {
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			act.setForword(USER_AUTHEN_INIT);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "顾问认证初始化异常");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * 顾问认证初加载数据
	 */
	public void userAuthenLoad() {
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			String personId = request.getParamValue("personId");
			TtVsPersonPO tvpp = new TtVsPersonPO();
			tvpp.setPersonId(Long.parseLong(personId));
			tvpp = (TtVsPersonPO) dao.select(tvpp).get(0);
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			String entryDate = df.format(tvpp.getEntryDate());
			act.setOutData("tvpp", tvpp);
			act.setOutData("entryDate", entryDate);
			TmDealerPO dealer=new TmDealerPO();
			dealer.setDealerId(tvpp.getDealerId());
			dealer=(TmDealerPO) dao.select(dealer).get(0);
			act.setOutData("dealer", dealer);
			act.setForword(USER_AUTHEN);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "顾问认证数据加载异常");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * 顾问认证
	 */
	public void userAuthen() {
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			String personId = request.getParamValue("personId");
			String auth_remark=CommonUtils.checkNull(request.getParamValue("auth_remark"));
			String authenDate = CommonUtils.checkNull(request.getParamValue("authenDate"));
			
			TtVsPersonPO tvpp1 = new TtVsPersonPO();
			tvpp1.setPersonId(personId == null ? null : Long.parseLong(personId));
			TtVsPersonPO tvpp2 = (TtVsPersonPO) dao.select(tvpp1).get(0);
			// 修改后的数据
			TtVsPersonPO tvpp = new TtVsPersonPO();
			tvpp.setAuthRemark(auth_remark==null?"":auth_remark);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			tvpp.setLastAuthenticationTime(sdf.parse(authenDate));
			Long auth_level = tvpp2.getAuthenticationLevel();
			auth_level = auth_level == null || auth_level == 0 ? Long.parseLong("29911001") : auth_level.longValue() + 1;

			// 首次认证获取认证时间
			if (Constant.AUTHENTICATION_LEVEL_THREE.toString().equals(auth_level.toString())) {
				tvpp.setFirstAuthTime(new Date());
			}
			// 设置最大的值
			Long auth_level_max = Constant.AUTHENTICATION_LEVEL_FIVE;
			// 如果该数字比最大值还大就设置为最大值
			if (auth_level > auth_level_max) {
				auth_level = auth_level_max;
			}
			tvpp.setAuthenticationLevel(auth_level);
			int a = dao.update(tvpp1, tvpp);
			if (a > 0) {
				// start yinsh
				// 给机构人员信息表中写入记录
				TtVsPersonPO tvpp0 = new TtVsPersonPO();
				tvpp0 = (TtVsPersonPO) dao.select(tvpp1).get(0);
				// 得到原来的认证积分
				Long authenInteg = tvpp0.getAuthenticationInteg() == null ? Constant.AUTHENTICATION_LEVEL_THREE : tvpp0.getAuthenticationInteg();
				// 获取应该添加的积分
				TtVsDemioGetintegPO tvdg = new TtVsDemioGetintegPO();
				tvdg.setAuthenticationLevel(auth_level);
				tvdg = (TtVsDemioGetintegPO) dao.select(tvdg).get(0);
				Long addIteg = tvdg.getGetInteg();

				// 得到销售顾问最终积分
				Long finalInteg = authenInteg + addIteg;
				TtVsPersonPO tvpp3 = new TtVsPersonPO();
				TtVsPersonPO tvpp4 = new TtVsPersonPO();
				tvpp3.setPersonId(personId == null ? null : Long.parseLong(personId));
				tvpp4.setAuthenticationInteg(finalInteg);
				dao.update(tvpp3, tvpp4);
				// 给积分变动历史表中写入记录
				TtVsIntegrationChangePO tvic = new TtVsIntegrationChangePO();
				Long integ_change_id = Long.parseLong(SequenceManager.getSequence(""));
				tvic.setIntegChangeId(integ_change_id);
				tvic.setDealerId(tvpp0.getDealerId() == null || "".equals(tvpp0.getDealerId()) ? null : tvpp0.getDealerId());
				tvic.setCreateDate(new Date());
				tvic.setIdNo(tvpp0.getIdNo());
				tvic.setName(tvpp0.getName());
				tvic.setIntegBefore(authenInteg);
				tvic.setIntegAfter(finalInteg);
//				tvic.setIntegType(Constant.AUTHENTICATION_INTEG);
				tvic.setChangeType(Constant.INTEG_CHANGE_AUTHEN);
				tvic.setThisChangeInteg(addIteg);
				tvic.setAuthenticationInteg(addIteg);
				tvic.setRelationId(integ_change_id);
				dao.insert(tvic);
				// 人员变动表里写入记录
				inertPersonChange(tvpp0, Constant.PERSON_CHANGE_TYPE_AHTHEN);
				// end
				act.setOutData("subFlag", "success");
			} else {
				act.setOutData("subFlag", "failure");
			}
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "顾问认证异常");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * 向机构人员变动记录表中写记录
	 * 
	 * @param tvpp0
	 * @param changeType
	 */
	public void inertPersonChange(TtVsPersonPO tvpp0, Long changeType) {
		TtVsPersonChangePO tvcp = new TtVsPersonChangePO();
		Long person_change_id = Long.parseLong(SequenceManager.getSequence(""));
		tvcp.setPersonChangeId(person_change_id);
		tvcp.setDealerId(tvpp0.getDealerId() == null || "".equals(tvpp0.getDealerId()) ? null : tvpp0.getDealerId());
		tvcp.setName(tvpp0.getName());
		tvcp.setCreateDate(new Date());
		tvcp.setChangeTime(new Date());
		tvcp.setIdNo(tvpp0.getIdNo());
		tvcp.setEmail(tvpp0.getEmail());
		tvcp.setMobile(tvpp0.getMobile());
		tvcp.setPosition(tvpp0.getPosition());
		tvcp.setIsInvestor(tvpp0.getIsInvestor());
		tvcp.setPositionStatus(tvpp0.getPositionStatus());
		tvcp.setBank(tvpp0.getBank());
		tvcp.setEntryDate(tvpp0.getEntryDate());
		tvcp.setBankCardno(tvpp0.getBankCardno());
		tvcp.setGender(tvpp0.getGender());
		// 剩余积分ToDO
		tvcp.setChangeType(changeType);
		dao.insert(tvcp);
	}

	/**
	 * 人员正式表的查询
	 * 
	 */
	public void personSelectInitQuery() {
		AclUserBean logonUser = null;
		try {

			TtVsPersonPO tvprp = new TtVsPersonPO();
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);

			String dealerId = logonUser.getDealerId();
			String idNO = request.getParamValue("idNO");
			String gender = request.getParamValue("gender");
			String mobile = request.getParamValue("mobile");
			String position = request.getParamValue("position");
			String name = CommonUtils.checkNull(request.getParamValue("name"));
			String bank = request.getParamValue("bank");
			String isInvestor = request.getParamValue("isInvestor");
			String email = request.getParamValue("email");
			String dealerCode=request.getParamValue("dealerCode");
			tvprp.setName(name);
			tvprp.setIdNo(idNO);
			tvprp.setGender(gender == null ? null : new Long(gender));
			tvprp.setMobile(mobile);
			tvprp.setPosition(position == null ? null : new Long(position));
			tvprp.setBank(bank == null ? null : new Long(bank));
			tvprp.setIsInvestor(isInvestor == null ? null : new Long(isInvestor));
			tvprp.setEmail(email);
			String pageSize0 = CommonUtils.checkNull(request.getParamValue("pagesizes"));
			String curPage0 = CommonUtils.checkNull(request.getParamValue("curPage"));
			tvprp.setDealerId(dealerId == null ? null : Long.parseLong(dealerId));
			int curPage = 1;
			int pageSize = 10;
			if (curPage0 != null && !"".equals(curPage0)) {
				curPage = Integer.parseInt(curPage0);
			}
			if (pageSize0 != null && !"".equals(pageSize0)) {
				pageSize = Integer.parseInt(pageSize0);
			}
			PageResult<Map<String, Object>> ps = null;
			
			ps = dao.personSelectInitQuery(dealerCode,tvprp, pageSize, curPage);
			
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "人员查询异常");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 人员正式表的查询
	 * 
	 */
	public void personSelect() {
		AclUserBean logonUser = null;
		try {

			TtVsPersonPO tvprp = new TtVsPersonPO();
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			String dealerCodes=request.getParamValue("dealerCode");
			String dealerId = logonUser.getDealerId();
			String idNO = request.getParamValue("idNO");
			String gender = request.getParamValue("gender");
			String mobile = request.getParamValue("mobile");
			String position = request.getParamValue("position");
			String name = CommonUtils.checkNull(request.getParamValue("name"));
			String bank = request.getParamValue("bank");
			String isInvestor = request.getParamValue("isInvestor");
			String email = request.getParamValue("email");
			String dealerCode=request.getParamValue("dealerCode");
			String position_status = request.getParamValue("position_status");
			tvprp.setName(name);
			tvprp.setIdNo(idNO);
			tvprp.setGender(gender == null ? null : new Long(gender));
			tvprp.setMobile(mobile);
			tvprp.setPosition(position == null ? null : new Long(position));
			tvprp.setBank(bank == null ? null : new Long(bank));
			tvprp.setIsInvestor(isInvestor == null ? null : new Long(isInvestor));
			tvprp.setEmail(email);
			tvprp.setPositionStatus(position_status==null?null:new Long(position_status));
			String pageSize0 = CommonUtils.checkNull(request.getParamValue("pagesizes"));
			String curPage0 = CommonUtils.checkNull(request.getParamValue("curPage"));
			tvprp.setDealerId(dealerId == null ? null : Long.parseLong(dealerId));
			int curPage = 1;
			int pageSize = 10;
			if (curPage0 != null && !"".equals(curPage0)) {
				curPage = Integer.parseInt(curPage0);
			}
			if (pageSize0 != null && !"".equals(pageSize0)) {
				pageSize = Integer.parseInt(pageSize0);
			}
			PageResult<Map<String, Object>> ps = null;
			if ("10431003".equals(logonUser.getDutyType())) {
				ps = dao.personLargeSelect(dealerCode,tvprp, logonUser.getOrgId(), pageSize, curPage);

			} else if ("10431004".equals(logonUser.getDutyType())) {
				ps = dao.personSmallSelect(dealerCode,tvprp, logonUser.getOrgId(), pageSize, curPage);
			} else {
				ps = dao.personSelect(dealerCode,tvprp,dealerCodes, pageSize, curPage);
			}
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "人员查询异常");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * 机构人员离职初始化
	 */
	public void personSelectInit() {
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			act.setOutData("dutyType", logonUser.getDutyType());
			act.setForword(PERSON_SELECT_INIT);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "机构人员离职初始化异常");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * 机构人员离职
	 */
	public void personLeaveJob() {
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			String personId = CommonUtils.checkNull(request.getParamValue("personId"));
			TtVsPersonPO tvpp1 = new TtVsPersonPO();
			tvpp1.setPersonId(personId == null ? null : Long.parseLong(personId));
			TtVsPersonPO tvpp = new TtVsPersonPO();
			// 设置状态为离职
			tvpp.setPositionStatus(Constant.POSITION_STATUS_LEAVE);
			// 设置离职的时间
			tvpp.setLeaveDate(new Date());
			int a = dao.update(tvpp1, tvpp);
			if (a > 0) {
				// 人员变动表里写入记录
				TtVsPersonPO tvpp0 = new TtVsPersonPO();
				tvpp0 = (TtVsPersonPO) dao.select(tvpp1).get(0);
				inertPersonChange(tvpp0, Constant.PERSON_CHANGE_TYPE_LEAVE);
				act.setOutData("subFlag", "success");
			} else {
				act.setOutData("subFlag", "failure");
			}
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "机构人员离职异常");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * 机构人员转换机构
	 */
	public void personSwitchJob() {
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			String personId = CommonUtils.checkNull(request.getParamValue("personId"));
			TtVsPersonPO tvpp1 = new TtVsPersonPO();
			tvpp1.setPersonId(personId == null ? null : Long.parseLong(personId));
			TtVsPersonPO tvpp = new TtVsPersonPO();
			tvpp.setPositionStatus(Constant.POSITION_STATUS_SWITCH);
			int a = dao.update(tvpp1, tvpp);
			if (a > 0) {
				// 人员变动表里写入记录
				TtVsPersonPO tvpp0 = new TtVsPersonPO();
				tvpp0 = (TtVsPersonPO) dao.select(tvpp1).get(0);
				inertPersonChange(tvpp0, Constant.PERSON_CHANGE_TYPE_SWITCH);
				act.setOutData("subFlag", "success");
			} else {
				act.setOutData("subFlag", "failure");
			}
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "机构人员转换机构异常");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * 机构人员修改初始化
	 */
	public void personUpdateInit() {
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			act.setOutData("dutyType", logonUser.getDutyType());
			act.setForword(PERSON_UPDATE_INIT);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "机构人员离职初始化异常");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * 加载机构人员修改数据
	 */
	public void personUpdateLoad() {
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			String personId = request.getParamValue("personId");
			TtVsPersonPO tvprp = new TtVsPersonPO();
			tvprp.setPersonId(Long.parseLong(personId));
			tvprp = (TtVsPersonPO) dao.select(tvprp).get(0);
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			String entryDate = null;

			if(tvprp.getEntryDate()!= null) {
				 entryDate = df.format(tvprp.getEntryDate());
			}
			if(tvprp.getRemark()==null){
				tvprp.setRemark("");
			}
			
			act.setOutData("tvprp", tvprp);
			act.setOutData("entryDate", entryDate);
			act.setForword(PERSON_UPDATE_LOAD);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "人员审核初始化异常");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * 修改正式表机构人员信息
	 */
	public void personUpdate() {
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			TtVsPersonPO tvprp = new TtVsPersonPO();
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			String personId = request.getParamValue("personId");
			String idNO = request.getParamValue("idNO");
			String gender = request.getParamValue("gender");
			String mobile = request.getParamValue("mobile");
			String position = request.getParamValue("position");
			String name = CommonUtils.checkNull(request.getParamValue("name"));
			String bank = request.getParamValue("bank");
			String bankCardNo = CommonUtils.checkNull(request.getParamValue("bankCardNO"));
			String isInvestor = request.getParamValue("isInvestor");
			String email = request.getParamValue("email");
			String remark = CommonUtils.checkNull(request.getParamValue("remark"));
			String degree= CommonUtils.checkNull(request.getParamValue("degree"));
			tvprp.setName(name);
			tvprp.setIdNo(idNO);
			tvprp.setGender(gender == null ? null : new Long(gender));
			tvprp.setMobile(mobile);
			tvprp.setPosition(position == null ? null : new Long(position));
			tvprp.setBank(bank == null ? null : new Long(bank));
			tvprp.setIsInvestor(isInvestor == null ? null : new Long(isInvestor));
			tvprp.setEmail(email);
			tvprp.setBankCardno(bankCardNo);
			tvprp.setRemark(remark);
			tvprp.setDegree(new Long(degree));
			TtVsPersonPO tvprp1 = new TtVsPersonPO();
			tvprp1.setPersonId(personId == null ? null : Long.parseLong(personId));
			int a = dao.update(tvprp1, tvprp);
			if (a > 0) {
				// 人员变动表里写入记录
				TtVsPersonPO tvpp0 = new TtVsPersonPO();
				tvpp0 = (TtVsPersonPO) dao.select(tvprp1).get(0);
				inertPersonChange(tvpp0, Constant.PERSON_CHANGE_TYPE_ALTER);
				act.setOutData("subFlag", "success");
			} else {
				act.setOutData("subFlag", "failure");
			}

		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "人员信息修改异常");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * 机构人员变动管理初始化
	 */
	public void personChangeInit() {
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			act.setOutData("dutyType", logonUser.getDutyType());
			act.setForword(PERSON_CHANGE_INIT);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "机构人员变动初始化异常");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * 机构人员变动表的查询
	 * 
	 */
	public void personChangeSelect() {
		AclUserBean logonUser = null;
		try {

			TtVsPersonChangePO tvprp = new TtVsPersonChangePO();
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);

			String dealerId = logonUser.getDealerId();
			String idNO = request.getParamValue("idNO");
			String gender = request.getParamValue("gender");
			String mobile = request.getParamValue("mobile");
			String position = request.getParamValue("position");
			String name = CommonUtils.checkNull(request.getParamValue("name"));
			String bank = request.getParamValue("bank");
			String isInvestor = request.getParamValue("isInvestor");
			String email = request.getParamValue("email");
			String dealerCode=request.getParamValue("dealerCode");
			tvprp.setName(name);
			tvprp.setIdNo(idNO);
			tvprp.setGender(gender == null ? null : new Long(gender));
			tvprp.setMobile(mobile);
			tvprp.setPosition(position == null ? null : new Long(position));
			tvprp.setBank(bank == null ? null : new Long(bank));
			tvprp.setIsInvestor(isInvestor == null ? null : new Long(isInvestor));
			tvprp.setEmail(email);
			String pageSize0 = CommonUtils.checkNull(request.getParamValue("pagesizes"));
			String curPage0 = CommonUtils.checkNull(request.getParamValue("curPage"));
			tvprp.setDealerId(dealerId == null ? null : Long.parseLong(dealerId));
			int curPage = 1;
			int pageSize = 10;
			if (curPage0 != null && !"".equals(curPage0)) {
				curPage = Integer.parseInt(curPage0);
			}
			if (pageSize0 != null && !"".equals(pageSize0)) {
				pageSize = Integer.parseInt(pageSize0);
			}
			PageResult<Map<String, Object>> ps = null;
			// 大区机构人员变动查询
			if ("10431003".equals(logonUser.getDutyType())) {
				ps = dao.personChangeLargeSelect( dealerCode,tvprp, logonUser.getOrgId(), pageSize, curPage);
				// 小区机构人员变动查询
			} else if ("10431004".equals(logonUser.getDutyType())) {
				ps = dao.personChangeSmallSelect(dealerCode,tvprp, logonUser.getOrgId(), pageSize, curPage);
			} else {
				ps = dao.personChangeSelect(dealerCode,tvprp, pageSize, curPage);
			}
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "人员变动查询异常");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * 机构人员查询初始化
	 */
	public void personSelectPre() {
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			act.setOutData("dutyType", logonUser.getDutyType());
			act.setForword(PERSON_SELECT_PRE);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "机构人员变动初始化异常");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * 加载机构人员修改数据
	 */
	public void personSelectLoad() {
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			String personId = request.getParamValue("personId");
			TtVsPersonPO tvprp = new TtVsPersonPO();
			tvprp.setPersonId(Long.parseLong(personId));
			tvprp = (TtVsPersonPO) dao.select(tvprp).get(0);
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			// 入职时间
			String entryDate = "";
			if (tvprp.getEntryDate() != null) {
				entryDate = df.format(tvprp.getEntryDate());
			}
			// 创建时间
			String createDate = "";
			if (tvprp.getCreateDate() != null) {
				createDate = df.format(tvprp.getCreateDate());
			}
			// 离职时间
			String leaveDate = "";
			if (tvprp.getLeaveDate() != null) {
				leaveDate = df.format(tvprp.getLeaveDate());
			}
			// 最后认证时间
			String authenDate = "";
			if (tvprp.getLastAuthenticationTime() != null) {
				authenDate = df.format(tvprp.getLastAuthenticationTime());
			}
			// 最后审核时间
			String auditDate = "";
			if (tvprp.getLastAuditTime() != null) {
				auditDate = df.format(tvprp.getLastAuditTime());
			}
			TmDealerPO dealer = new TmDealerPO();
			dealer.setDealerId(tvprp.getDealerId());
			dealer = (TmDealerPO) dao.select(dealer).get(0);
			act.setOutData("dealer", dealer);
			act.setOutData("tvprp", tvprp);
			act.setOutData("entryDate", entryDate);
			act.setOutData("createDate", createDate);
			act.setOutData("leaveDate", leaveDate);
			act.setOutData("authenDate", authenDate);
			act.setOutData("auditDate", auditDate);
			act.setForword(PERSON_SELECT_DETAIL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "人员审核初始化异常");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 添加积分变动记录的公用方法
	 * @param beforeInteg
	 * @param after_integ
	 * @param tvp
	 * @param changeType
	 */
	public void addIntegChange(Long beforeInteg,Long after_integ,TtVsPersonPO tvp,String changeType) {
		AclUserBean logonUser = null;
		try {
		logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		//给积分变动历史表中写入记录
		TtVsIntegrationChangePO tvic=new TtVsIntegrationChangePO();
		Long integ_change_id = Long.parseLong(SequenceManager.getSequence(""));
		tvic.setIntegChangeId(integ_change_id);
		tvic.setDealerId(tvp.getDealerId());
		tvic.setCreateDate(new Date());
		tvic.setIdNo(tvp.getIdNo());
		tvic.setName(tvp.getName());
		tvic.setIntegBefore(beforeInteg);
		tvic.setIntegAfter(after_integ);
//		tvic.setIntegType(Constant.PERFORMANCE_INTEG);
		tvic.setChangeType(new Long(changeType));
		tvic.setThisChangeInteg(after_integ-beforeInteg);
		tvic.setPerformanceInteg(after_integ-beforeInteg);
		dao.insert(tvic);
		
		}catch(Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "人员积分变动添加");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 机构人员积分清零
	 */
	public void personIntegUpdate() {
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);

			try {
				String personId=request.getParamValue("personId");
				//写入积分变动历史表
				TtVsPersonPO tvp=new TtVsPersonPO();
				tvp.setPersonId(new Long(personId));
				TtVsIntegrationChangePO tvic=new TtVsIntegrationChangePO();
				Long integChangeId=Long.parseLong(SequenceManager.getSequence(""));
				tvic.setIntegChangeId(integChangeId);
				tvic.setCreateDate(new Date());
				tvic.setDealerId(tvp.getDealerId());
				tvic.setIdNo(tvp.getIdNo());
				tvic.setIntegAfter(Long.parseLong("0"));
				tvic.setChangeType(Constant.INTEG_CHANGE_CLEAR);
				tvic.setName(tvp.getName());
				tvic.setRelationId(integChangeId);
				//积分变动表里写入清零记录
				dao.insert(tvic);
				//积分清零后调用的方法
				List in = new ArrayList();
				in.add(new Date());
				in.add(tvp.getIdNo());
				dao.callProcedure("PERSON_INTEG_ZERO", in, null);
			} catch (Exception e) {
				act.setOutData("subFlag", "fail");
			}
			
			act.setOutData("subFlag", "success");
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "人员注册页面跳转");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 机构人员转换查询初始化
	 */
	public void personSwitchPre() {
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			act.setOutData("dutyType", logonUser.getDutyType());
			act.setForword(PERSON_SWITCH_PRE);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "机构人员转换初始化异常");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 加载机构人员转换修改的数据
	 */
	public void personSwitchLoad() {
		AclUserBean logonUser = null;
		try {
			String personId=request.getParamValue("personId");
			TtVsPersonPO tvp =new TtVsPersonPO();
			tvp.setPersonId(new Long(personId));
			tvp=(TtVsPersonPO) dao.select(tvp).get(0);
			TmDealerPO td=new TmDealerPO();
			td.setDealerId(tvp.getDealerId());
			td=(TmDealerPO) dao.select(td).get(0);
			act.setOutData("oldCode", td.getDealerCode());
			act.setOutData("oldName", td.getDealerName());
			act.setOutData("personId", personId);
			act.setForword(PERSON_SWITCH_DETAIL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "人员转换初始化异常");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	
	/**
	 * 机构人员转换获取dealerName
	 */
	public void userDealerName() {
		AclUserBean logonUser = null;
		try {
			String dealerName="";
			String dealerCode=request.getParamValue("dealerCode");
			TmDealerPO td=new TmDealerPO();
			td.setDealerCode(dealerCode);
			td=(TmDealerPO) dao.select(td).get(0);
			dealerName=td.getDealerName();
			act.setOutData("dealerName", dealerName);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "机构人员转换机构异常");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 机构人员转换获取dealerName
	 */
	public void userSwitchDealer() {
		AclUserBean logonUser = null;
		try {
			String dealerCode=request.getParamValue("dealerCode");
			TmDealerPO td=new TmDealerPO();
			td.setDealerCode(dealerCode);
			td=(TmDealerPO) dao.select(td).get(0);
			TtVsPersonPO tvp1=new TtVsPersonPO();
			tvp1.setDealerId(td.getDealerId());
			String personId=request.getParamValue("personId");
			TtVsPersonPO tvp =new TtVsPersonPO();
			tvp.setPersonId(new Long(personId));
			int count=0;
			count=dao.update(tvp, tvp1);
     		act.setOutData("count", count);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "机构人员转换机构异常");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 机构人员转换获取dealerName
	 */
	public void personCountPre() {
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			act.setOutData("dutyType", logonUser.getDutyType());
			act.setForword(PERSON_COUNT_INIT);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "机构人员转换机构异常");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 人员正式表的查询
	 * 
	 */
	public void personCountSelect() {
		AclUserBean logonUser = null;
		try {

			TtVsPersonPO tvprp = new TtVsPersonPO();
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			String dealerCodes=request.getParamValue("dealerCode");
			String dealerId = logonUser.getDealerId();
			String gender = request.getParamValue("gender");
			String name = CommonUtils.checkNull(request.getParamValue("name"));
			String degree = request.getParamValue("degree");
			String email = request.getParamValue("email");
			String dealerCode=request.getParamValue("dealerCode");
			String position_status = request.getParamValue("position_status");
			tvprp.setName(name);
			tvprp.setGender(gender == null ? null : new Long(gender));
			tvprp.setDegree(degree == null ? null : new Long(degree));
			tvprp.setEmail(email);
			tvprp.setPositionStatus(position_status==null?null:new Long(position_status));
			String pageSize0 = CommonUtils.checkNull(request.getParamValue("pagesizes"));
			String curPage0 = CommonUtils.checkNull(request.getParamValue("curPage"));
			tvprp.setDealerId(dealerId == null ? null : Long.parseLong(dealerId));
			int curPage = 1;
			int pageSize = 10;
			if (curPage0 != null && !"".equals(curPage0)) {
				curPage = Integer.parseInt(curPage0);
			}
			if (pageSize0 != null && !"".equals(pageSize0)) {
				pageSize = Integer.parseInt(pageSize0);
			}
			PageResult<Map<String, Object>> ps = null;
			ps = dao.personCountSelect(logonUser,dealerCode,tvprp,dealerCodes, pageSize, curPage);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "人员查询异常");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 人员关键岗位明细查询
	 */
	public void personDetailPre() {
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			act.setOutData("dutyType", logonUser.getDutyType());
			act.setForword(PERSON_DETAIL_INIT);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "机构人员转换机构异常");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 人员关键岗位明细查询
	 * 
	 */
	public void personDetailSelect() {
		AclUserBean logonUser = null;
		try {

			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			String dealerCodes=request.getParamValue("dealerCode");
			String leaveStartDate=request.getParamValue("leaveStartDate");
			String leaveEndDate=request.getParamValue("leaveEndDate");
			String auditStartDate=request.getParamValue("auditStartDate");
			String auditEndDate=request.getParamValue("auditEndDate");
			String degree = request.getParamValue("degree");
			String position = request.getParamValue("position");
			String position_status = request.getParamValue("position_status");
			String pageSize0 = CommonUtils.checkNull(request.getParamValue("pagesizes"));
			String curPage0 = CommonUtils.checkNull(request.getParamValue("curPage"));
			int curPage = 1;
			int pageSize = 10;
			if (curPage0 != null && !"".equals(curPage0)) {
				curPage = Integer.parseInt(curPage0);
			}
			if (pageSize0 != null && !"".equals(pageSize0)) {
				pageSize = Integer.parseInt(pageSize0);
			}
			Map<String,String> conditionMap=new HashMap<String, String>();
			conditionMap.put("leaveStartDate", leaveStartDate);
			conditionMap.put("leaveEndDate", leaveEndDate);
			conditionMap.put("auditStartDate", auditStartDate);
			conditionMap.put("auditEndDate", auditEndDate);
			
			conditionMap.put("degree", degree);
			conditionMap.put("position", position);
			conditionMap.put("position_status", position_status);
			
			PageResult<Map<String, Object>> ps = null;
			ps = dao.personDetailSelect(logonUser,conditionMap,dealerCodes, pageSize, curPage);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "人员查询异常");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/*
	 * 渠道人员数量汇总表
	 */
	public void personCountDownLoad(){
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			TtVsPersonPO tvprp = new TtVsPersonPO();
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			String dealerCodes=request.getParamValue("dealerCode");
			String dealerId = logonUser.getDealerId();
			String gender = request.getParamValue("gender");
			String name = CommonUtils.checkNull(request.getParamValue("name"));
			String degree = request.getParamValue("degree");
			String email = request.getParamValue("email");
			String dealerCode=request.getParamValue("dealerCode");
			String position_status = request.getParamValue("position_status");
			tvprp.setName(name);
			tvprp.setGender(gender == null ? null : new Long(gender));
			tvprp.setDegree(degree == null ? null : new Long(degree));
			tvprp.setEmail(email);
			tvprp.setPositionStatus(position_status==null?null:new Long(position_status));
			tvprp.setDealerId(dealerId == null ? null : Long.parseLong(dealerId));
			PageResult<Map<String, Object>> ps = null;
			List<Map<String,Object>> balanceList=dao.personCountDownLoad(logonUser,dealerCode,tvprp,dealerCodes);
			// 导出的文件名
			String fileName = "渠道人员汇总报表.xls";
			// 导出的文字编码
				fileName = new String(fileName.getBytes("GB2312"), "ISO8859-1");
			
			response.setContentType("Application/text/csv");
			response.addHeader("Content-Disposition", "attachment;filename="
					+ fileName);
			OutputStream os = response.getOutputStream();
			WritableWorkbook workbook = Workbook.createWorkbook(os);
			WritableSheet sheet = workbook.createSheet("渠道人员汇总查询", 0);
			WritableCellFormat wcf = new WritableCellFormat();
			wcf.setAlignment(Alignment.CENTRE);
			List<List<Object>> lists = new LinkedList<List<Object>>();
			int y=0;
			sheet.mergeCells(0, y, 8, y);
			sheet.addCell(new jxl.write.Label(0, y, "渠道人员汇总",wcf));
			++y;
			
			sheet.addCell(new Label(0, y, "片区代码"));
			sheet.addCell(new Label(1, y, "片区名称"));
			sheet.addCell(new Label(2, y, "经销商代码"));
			sheet.addCell(new Label(3, y, "经销商名称"));
			sheet.addCell(new Label(4, y, "总经理"));
			sheet.addCell(new Label(5, y, "销售经理"));
			sheet.addCell(new Label(6, y, "市场经理"));
			sheet.addCell(new Label(7, y, "销售顾问"));
			sheet.addCell(new Label(8, y, "总人数"));
			int length=balanceList.size();
			for(int i=0;i<length;i++){
				++y;
				Map<String,Object> maps=balanceList.get(i);
				sheet.addCell(new Label(0, y, maps.get("PQ_ORG_CODE")==null?"":maps.get("PQ_ORG_CODE").toString()));
				sheet.addCell(new Label(1, y, maps.get("PQ_ORG_NAME")==null?"":maps.get("PQ_ORG_NAME").toString()));
				sheet.addCell(new Label(2, y, maps.get("DEALER_CODE")==null?"":maps.get("DEALER_CODE").toString()));
				
				sheet.addCell(new Label(3, y, maps.get("DEALER_NAME")==null?"":maps.get("DEALER_NAME").toString()));
				sheet.addCell(new Label(4, y, maps.get("MGR")==null?"":maps.get("MGR").toString()));
				sheet.addCell(new Label(5, y, maps.get("SALE_MGR")==null?"":maps.get("SALE_MGR").toString()));
				sheet.addCell(new Label(6, y, maps.get("MARK_MGR")==null?"":maps.get("MARK_MGR").toString()));
				sheet.addCell(new Label(7, y, maps.get("SAL_MAN")==null?"":maps.get("SAL_MAN").toString()));
				sheet.addCell(new Label(8, y, maps.get("TOTAL")==null?"":maps.get("TOTAL").toString()));
			}
			workbook.write();
			workbook.close();
		}catch (Exception e) {
			BizException e1 = new BizException(act, e,
			ErrorCodeConstant.QUERY_FAILURE_CODE, "报表有误！");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/*
	 * 根据机构查询余额
	 */
	public void personDetailDownLoad(){
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			String orgId=logonUser.getOrgId().toString();
			String dealerCodes=request.getParamValue("dealerCode");
			String leaveStartDate=request.getParamValue("leaveStartDate");
			String leaveEndDate=request.getParamValue("leaveEndDate");
			String auditStartDate=request.getParamValue("auditStartDate");
			String auditEndDate=request.getParamValue("auditEndDate");
			String degree = request.getParamValue("degree");
			String position = request.getParamValue("position");
			String position_status = request.getParamValue("position_status");
			Map<String,String> conditionMap=new HashMap<String, String>();
			conditionMap.put("leaveStartDate", leaveStartDate);
			conditionMap.put("leaveEndDate", leaveEndDate);
			conditionMap.put("auditStartDate", auditStartDate);
			conditionMap.put("auditEndDate", auditEndDate);
			
			conditionMap.put("degree", degree);
			conditionMap.put("position", position);
			conditionMap.put("position_status", position_status);
			List<Map<String,Object>> balanceList=dao.personDetailDownLoad(logonUser,conditionMap,dealerCodes);
			// 导出的文件名
			String fileName = "关键岗位明细报表.xls";
			// 导出的文字编码
				fileName = new String(fileName.getBytes("GB2312"), "ISO8859-1");
			
			response.setContentType("Application/text/csv");
			response.addHeader("Content-Disposition", "attachment;filename="
					+ fileName);
			OutputStream os = response.getOutputStream();
			WritableWorkbook workbook = Workbook.createWorkbook(os);
			WritableSheet sheet = workbook.createSheet("关键岗位明细查询", 0);
			WritableCellFormat wcf = new WritableCellFormat();
			wcf.setAlignment(Alignment.CENTRE);
			List<List<Object>> lists = new LinkedList<List<Object>>();
			int y=0;
			sheet.mergeCells(0, y, 23, y);
			sheet.addCell(new jxl.write.Label(0, y, "关键岗位明细",wcf));
			++y;
			
			sheet.addCell(new Label(0, y, "片区名称"));
			sheet.addCell(new Label(1, y, "经销商代码"));
			sheet.addCell(new Label(2, y, "经销商名称"));
			sheet.addCell(new Label(3, y, "职位"));
			sheet.addCell(new Label(4, y, "姓名"));
			sheet.addCell(new Label(5, y, "性别"));
			sheet.addCell(new Label(6, y, "入职日期"));
			sheet.addCell(new Label(7, y, "离职日期"));
			sheet.addCell(new Label(8, y, "最后审核时间"));
			sheet.addCell(new Label(9, y, "职位状态"));
			sheet.addCell(new Label(10, y, "电话"));
			
			sheet.addCell(new Label(11, y, "邮箱"));
			sheet.addCell(new Label(12, y, "银行"));
			sheet.addCell(new Label(13, y, "银行卡号"));
			sheet.addCell(new Label(14, y, "身份证"));
			sheet.addCell(new Label(15, y, "生日"));
			sheet.addCell(new Label(16, y, "学历"));
			sheet.addCell(new Label(17, y, "业绩积分"));
			sheet.addCell(new Label(18, y, "认证积分"));
			sheet.addCell(new Label(19, y, "年限积分"));
			sheet.addCell(new Label(20, y, "总积分"));
			sheet.addCell(new Label(21, y, "已兑现积分"));
			sheet.addCell(new Label(22, y, "未兑现积分"));
			sheet.addCell(new Label(23, y, "合计"));
			int length=balanceList.size();
			for(int i=0;i<length;i++){
				++y;
				Map<String,Object> maps=balanceList.get(i);
				sheet.addCell(new Label(0, y, maps.get("PQ_ORG_NAME")==null?"":maps.get("PQ_ORG_NAME").toString()));
				sheet.addCell(new Label(1, y, maps.get("DEALER_CODE")==null?"":maps.get("DEALER_CODE").toString()));
				sheet.addCell(new Label(2, y, maps.get("DEALER_NAME")==null?"":maps.get("DEALER_NAME").toString()));
				
				sheet.addCell(new Label(3, y, maps.get("POSITION")==null?"":maps.get("POSITION").toString()));
				sheet.addCell(new Label(4, y, maps.get("NAME")==null?"":maps.get("NAME").toString()));
				sheet.addCell(new Label(5, y, maps.get("GENDER")==null?"":maps.get("GENDER").toString()));
				sheet.addCell(new Label(6, y, maps.get("ENTRY_DATE")==null?"":maps.get("ENTRY_DATE").toString()));
				sheet.addCell(new Label(7, y, maps.get("LEAVE_DATE")==null?"":maps.get("LEAVE_DATE").toString()));
				sheet.addCell(new Label(8, y, maps.get("LAST_AUDIT_TIME")==null?"":maps.get("LAST_AUDIT_TIME").toString()));
				
				sheet.addCell(new Label(9, y, maps.get("POSITION_STATUS")==null?"":maps.get("POSITION_STATUS").toString()));
				sheet.addCell(new Label(10, y, maps.get("MOBILE")==null?"":maps.get("MOBILE").toString()));
				sheet.addCell(new Label(11, y, maps.get("EMAIL")==null?"":maps.get("EMAIL").toString()));
				
				sheet.addCell(new Label(12, y, maps.get("BANK")==null?"":maps.get("BANK").toString()));
				sheet.addCell(new Label(13, y, maps.get("BANK_CARDNO")==null?"":maps.get("BANK_CARDNO").toString()));
				sheet.addCell(new Label(14, y, maps.get("ID_NO")==null?"":maps.get("ID_NO").toString()));
				
				sheet.addCell(new Label(15, y, maps.get("BIRTHDAY")==null?"":maps.get("BIRTHDAY").toString()));
				sheet.addCell(new Label(16, y, maps.get("DEGREE")==null?"":maps.get("DEGREE").toString()));
				sheet.addCell(new Label(17, y, maps.get("PERFORMANCE_INTEG")==null?"":maps.get("PERFORMANCE_INTEG").toString()));
				
				
				sheet.addCell(new Label(18, y, maps.get("AUTHENTICATION_INTEG")==null?"":maps.get("AUTHENTICATION_INTEG").toString()));
				sheet.addCell(new Label(19, y, maps.get("YEAR_INTEG")==null?"":maps.get("YEAR_INTEG").toString()));
				
				sheet.addCell(new Label(20, y, maps.get("ALLINTEG")==null?"":maps.get("ALLINTEG").toString()));
				sheet.addCell(new Label(21, y, maps.get("READY")==null?"":maps.get("READY").toString()));
				
				sheet.addCell(new Label(22, y, maps.get("UNREADY")==null?"":maps.get("UNREADY").toString()));
				sheet.addCell(new Label(23, y, maps.get("ALLTOTAL")==null?"":maps.get("ALLTOTAL").toString()));
				
			}
			workbook.write();
			workbook.close();
		}catch (Exception e) {
			BizException e1 = new BizException(act, e,
			ErrorCodeConstant.QUERY_FAILURE_CODE, "报表有误！");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

}
