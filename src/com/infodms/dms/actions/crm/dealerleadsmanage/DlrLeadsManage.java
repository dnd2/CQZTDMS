package com.infodms.dms.actions.crm.dealerleadsmanage;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import jxl.Cell;
import jxl.CellType;
import jxl.DateCell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.format.UnderlineStyle;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

import org.apache.log4j.Logger;

import com.infodms.dms.actions.sales.planmanage.PlanUtil.BaseImport;
import com.infodms.dms.actions.sales.planmanage.PlanUtil.ExcelErrors;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.dao.crm.dealerleadsmanage.DlrLeadsManageDao;
import com.infodms.dms.dao.crm.oemleadsmanage.OemLeadsManageDao;
import com.infodms.dms.dao.crm.taskmanage.TaskManageDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TPcLeadsAllotPO;
import com.infodms.dms.po.TPcLeadsDealerPO;
import com.infodms.dms.po.TPcLeadsPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.csv.CsvWriterUtil;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.FileObject;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;
import com.infoservice.po3.bean.DynaBean;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;
/**
 * @author chenh
 */
@SuppressWarnings("unchecked")
public class DlrLeadsManage extends BaseImport {

	public Logger logger = Logger.getLogger(DlrLeadsManage.class);
	// DLR线索分派初始页面
	private final String dealerLeadsAllotUrl = "/jsp/crm/dealerleadsmanage/dealerleadsallotinit.jsp";
	// DLR线索分派选择顾问页面
	private final String dealerLeadsAllotSelectAdviserUrl = "/jsp/crm/dealerleadsmanage/dealerleadsallotselectadviser.jsp";
	// DLR线索重新分派错误提醒页面
	// private final String dealerLeadsReAllotDefaultUrl = "/jsp/crm/dealerleadsmanage/dealerleadsreallotdefault.jsp";
	// DLR线索查询初始页面
	private final String dlrLeadsFindUrl = "/jsp/crm/dealerleadsmanage/dlrleadsfindinit.jsp";
	// DCRC录入初始页面
	private final String dcrcEnterUrl = "/jsp/crm/dealerleadsmanage/dcrcenterinit.jsp";
	// DCRC录入新增页面
	private final String dcrcEnterInsertUrl = "/jsp/crm/dealerleadsmanage/dcrcenterinsert.jsp";
	// DCRC录入初始页面
	private final String adviserEnterUrl = "/jsp/crm/dealerleadsmanage/adviserInit.jsp";
	// DCRC录入新增页面
	private final String adviserEnterInsertUrl = "/jsp/crm/dealerleadsmanage/adviserInsert.jsp";
	
	// 线索导入初始页面
	private final String dlrLeadsManageImplortUrl = "/jsp/crm/dealerleadsmanage/dlrleadsmanageimport.jsp";
	// 线索导入成功页面
	private final String dlrLeadsManageSuccessUrl = "/jsp/crm/dealerleadsmanage/dlrleadsmanagesuccess.jsp";
	// 线索导入失败页面
	private final String dlrLeadsManageFailureUrl = "/jsp/crm/dealerleadsmanage/dlrleadsmanagefailure.jsp";
	// 线索导入完成页面
	private final String dlrLeadsManageCompleteUrl = "/jsp/crm/dealerleadsmanage/dlrleadsmanageimportcomplete.jsp";
	private final String FILEURL = "http://fdms.changansuzuki.com:8082/dms/dlrData/";//服务器URL
	//顾问导入客流界面
	private final String CUS_IMPORT_PAGE_URL = "/jsp/crm/dealerleadsmanage/adviserImport.jsp";
	//顾问导入客流处理完界面
	private final String CUS_IMPORT_SUCCESS_PAGE_URL = "/jsp/crm/dealerleadsmanage/adviserImportSuccess.jsp";
	
	private ActionContext act = ActionContext.getContext();
	RequestWrapper request = act.getRequest();
	
	
	/**
	 * DLR初始线索分派页面
	 */
	public void dlrLeadsAllot() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		String managerLogon = "yes";
		try {
			//判断是否顾问登陆
			if(CommonUtils.judgeAdviserLogin(logonUser.getUserId().toString())) {
				managerLogon = "no";
			} else if(CommonUtils.judgeDirectorLogin(logonUser.getUserId().toString())){//判断是否主管登陆
				managerLogon = "no";
			} else if(CommonUtils.judgeDcrcLogin(logonUser.getUserId().toString())){//判断是否DCRC登陆
				managerLogon = "no";
			} 
			act.setOutData("managerLogon", managerLogon);
			act.setOutData("dealerId", logonUser.getDealerId());
			act.setForword(dealerLeadsAllotUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"未知错误!");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * DLR初始线索分派查询
	 */
	public void dlrAllotQuery() {
		DlrLeadsManageDao dao = new DlrLeadsManageDao();
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		String managerLogon = "yes";
		try {
			RequestWrapper request = act.getRequest();
			//获取页面参数
			String customerName = CommonUtils.checkNull(request.getParamValue("customer_name"));
			String telephone = CommonUtils.checkNull(request.getParamValue("telephone"));
			String startDate = CommonUtils.checkNull(request.getParamValue("startDate"));
			String endDate = CommonUtils.checkNull(request.getParamValue("endDate"));
			String leadsOrigin = CommonUtils.checkNull(request.getParamValue("leads_origin"));
			String allotStatus = CommonUtils.checkNull(request.getParamValue("allot_status"));
			String userDealerId = logonUser.getDealerId();
			
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1; // 处理当前页
			PageResult<Map<String, Object>> ps = dao.dlrLeadsAllotQuery(customerName,
					telephone,startDate,endDate,leadsOrigin,allotStatus,userDealerId,
					curPage, Constant.PAGE_SIZE);
			
			//判断是否顾问登陆
			if(CommonUtils.judgeAdviserLogin(logonUser.getUserId().toString())) {
				managerLogon = "no";
			} else if(CommonUtils.judgeDirectorLogin(logonUser.getUserId().toString())){//判断是否主管登陆
				managerLogon = "no";
			} else if(CommonUtils.judgeDcrcLogin(logonUser.getUserId().toString())){//判断是否DCRC登陆
				managerLogon = "no";
			} 
			act.setOutData("managerLogon", managerLogon);
			act.setOutData("ps", ps);
			act.setOutData("dealerId", logonUser.getDealerId());
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "销售线索分派查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * DLR初始线索分派顾问选择页面
	 */
	public void dlrLeadsAllotByDlrInit() {
		ActionContext act = ActionContext.getContext();
		RequestWrapper request = act.getRequest();
		DlrLeadsManageDao dao = new DlrLeadsManageDao();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			String leadsAllotId = CommonUtils.checkNull(request.getParamValue("leadsCode"));
			String leadsGroup = CommonUtils.checkNull(request.getParamValue("leadsGroup"));
			List<DynaBean> adviserList = dao.getAdviserBydealer3(logonUser.getDealerId());
			act.setOutData("adviserList", adviserList);
			act.setOutData("leadsAllotId", leadsAllotId);
			act.setOutData("leadsGroup", leadsGroup);
			act.setForword(dealerLeadsAllotSelectAdviserUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"未知错误!");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * DLR初始线索分派
	 */
	public void dlrLeadsAllotByDlr() {
		DlrLeadsManageDao dao = new DlrLeadsManageDao();
		ActionContext act = ActionContext.getContext();
		RequestWrapper request = act.getRequest();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			String leadsCode = CommonUtils.checkNull(request.getParamValue("leadsCode"));
			String leadsAllotId = CommonUtils.checkNull(request.getParamValue("leadsAllotId"));
			String leadsGroup = CommonUtils.checkNull(request.getParamValue("leadsGroup"));//为leadsAllotId的多条信息
			String leadsCodeGroup=CommonUtils.checkNull(request.getParamValue("leadsCodeGroup"));//为多个leadscode
			String adviserId = CommonUtils.checkNull(request.getParamValue("adviserId"));
			String comeFlag = CommonUtils.checkNull(request.getParamValue("comeFlag"));
			String managerLogon = "yes";
			
			//9：00-17:00之间分配给经销商的公司类信息，主管当日19:00前完成分配，顾问必须在当日完成处理
			//17:00-次日9:00分配给经销商的公司类信息，主管次日11:00前完成分配，顾问必须在次日12:00前完成处理。
			Date d1 = new Date(); //当前时间
			String d2 = "090000";
			String d3 = "170000";
			String remindDate = null;
			SimpleDateFormat f = new SimpleDateFormat("hhmmss"); //格式化为 hhmmss
			int d1Number = Integer.parseInt(f.format(d1).toString()); //将第一个时间格式化后转为int
			int d2Number = Integer.parseInt(d2); //将第二个时间格式化后转为int
			int d3Number = Integer.parseInt(d3); //将第三个时间格式化后转为int
			
			SimpleDateFormat f1 = new SimpleDateFormat("yyyy-MM-dd");
			Calendar c = Calendar.getInstance();
			c.add(Calendar.DAY_OF_MONTH, 1);
								
			if(d1Number>=d2Number && d1Number<=d3Number){
				remindDate = f1.format(d1);
			} else {
				remindDate = f1.format(c.getTime());
			}
			//获取销售线索主表信息
			TPcLeadsPO leadsPo = new TPcLeadsPO();
			leadsPo.setLeadsCode(Long.parseLong(leadsCode));
			TPcLeadsPO leadsPo2 = (TPcLeadsPO)dao.select(leadsPo).get(0);
			
			String remark="经理分派线索方法ByDlr";
			TPcLeadsPO leadsOldPoByDcrc = new TPcLeadsPO();
			leadsOldPoByDcrc.setLeadsCode(Long.parseLong(leadsCode));
			TPcLeadsPO leadsNewPoByDcrc = new TPcLeadsPO();
			leadsNewPoByDcrc.setFailureRemark(remark);
			dao.update(leadsOldPoByDcrc, leadsNewPoByDcrc);
			
			//获取线索来源(60151001:车厂下发；60151002：DCRC录入)
			//String leadsOrigin = leadsPo2.getLeadsOrigin();
			 //leadsOrigin = leadsOrigin.substring(0, 8);
			//获取线索类别(60141001:车厂下发；60141002：DCRC录入)
			 String leadsType = leadsPo2.getLeadsType();
			 leadsType = leadsType.substring(0, 8);
			//leadsGroup为空说明页面为单选线索进行分派
			if(leadsGroup==null || "".equals(leadsGroup)) {
				remark=remark+"-单选线索";
				leadsNewPoByDcrc.setFailureRemark(remark);
				dao.update(leadsOldPoByDcrc, leadsNewPoByDcrc);
				// 开始执行分派到选择的顾问
				// 获取销售线索信息
				TPcLeadsAllotPO po = new TPcLeadsAllotPO();
				po.setLeadsAllotId(Long.parseLong(leadsAllotId));
				TPcLeadsAllotPO po2 = (TPcLeadsAllotPO)dao.select(po).get(0);
					//判断是否已建过档案（电话、姓名、经销商ID）
					List<Map<String, Object>> getHasList = null;
					getHasList=dao.getHasCustomer(po2.getTelephone(),po2.getCustomerName(),logonUser.getDealerId());
					//已存在档案，不能进行分派顾问
					if(getHasList.size()>0) {
						remark=remark+"-已建档";
						leadsNewPoByDcrc.setFailureRemark(remark);
						dao.update(leadsOldPoByDcrc, leadsNewPoByDcrc);
						//获取已建档的分派顾问
						String adviser = getHasList.get(0).get("ADVISER").toString();
						String dealerId = getHasList.get(0).get("DEALER_ID").toString();
						String customerId = getHasList.get(0).get("CUSTOMER_ID").toString();
						String ctmType = getHasList.get(0).get("CTM_TYPE").toString();
						//如果已建档的分派顾问等于重新分派的顾问，并且如果客户类型不属于战败、失效、保有则设为无效并增加接触点信息，不等则更新线索中的顾问
						if(adviser.equals(adviserId)) {
							remark=remark+"-重复分派同一顾问";
							leadsNewPoByDcrc.setFailureRemark(remark);
							dao.update(leadsOldPoByDcrc, leadsNewPoByDcrc);
							//修改销售线索主表状态为重复
							TPcLeadsPO oldLeadsPo = new TPcLeadsPO();
							oldLeadsPo.setLeadsCode(Long.parseLong(leadsCode));
							TPcLeadsPO newLeadsPo = new TPcLeadsPO();
							//newLeadsPo.setLeadsCode(Long.parseLong(leadsCode));
							if(!"60341001".equals(ctmType)&&!"60341004".equals(ctmType)&&!"60341005".equals(ctmType)) {
								newLeadsPo.setIfHandle(10041001);
								newLeadsPo.setLeadsStatus(Constant.LEADS_STATUS_05.longValue());
							} else {
								newLeadsPo.setLeadsStatus(Constant.LEADS_STATUS_01.longValue());
							}
							dao.update(oldLeadsPo, newLeadsPo);
							//修改线索分派表状态为无效且已确认
							TPcLeadsAllotPO oldPo = new TPcLeadsAllotPO();
							oldPo.setLeadsAllotId(Long.parseLong(leadsAllotId));
							TPcLeadsAllotPO newPo = new TPcLeadsAllotPO();
							//newPo.setLeadsAllotId(Long.parseLong(leadsAllotId));
							if(!"60341001".equals(ctmType)&&!"60341004".equals(ctmType)&&!"60341005".equals(ctmType)) {
								newPo.setStatus(Constant.STATUS_DISABLE);//设置为无效
								newPo.setIfConfirm(Constant.ADVISER_CONFIRM_02);
								newPo.setConfirmDate(new Date());
								newPo.setCustomerId(Long.parseLong(customerId));
							} else {
								newPo.setStatus(Constant.STATUS_ENABLE);//设置为有效
								newPo.setIfConfirm(Constant.ADVISER_CONFIRM_01);
								newPo.setAdviser(adviserId);
								
							}
							newPo.setAllotAdviserDate(new Date());
							newPo.setUpdateDate(new Date());
							newPo.setUpdateBy(logonUser.getUserId());
							dao.update(oldPo, newPo);
							
							//标记提醒信息为已完成
							CommonUtils.setRemindDone(leadsAllotId,Constant.REMIND_TYPE_01.toString());
							CommonUtils.setRemindDone(leadsAllotId,Constant.REMIND_TYPE_02.toString());
							CommonUtils.setRemindDone(leadsAllotId,Constant.REMIND_TYPE_03.toString());
							CommonUtils.setRemindDone(leadsAllotId,Constant.REMIND_TYPE_04.toString());
							if(!"60341001".equals(ctmType)&&!"60341004".equals(ctmType)&&!"60341005".equals(ctmType)) {
								//增加接触点信息
								CommonUtils.addContackPoint(Constant.POINT_WAY_01, "重复线索", customerId, adviser, dealerId);
								CommonUtils.updateLeadStatus(leadsCode);
								String repeatLeads = SequenceManager.getSequence("");
								//新增提醒信息
								CommonUtils.addRemindInfo(Constant.REMIND_TYPE_20.toString(), repeatLeads, customerId, dealerId, adviser, f1.format(new Date()),"");
								//标记线索为重复线索
								CommonUtils.updateIfRepeat(leadsAllotId);
							} else {
								if("60141001".equals(leadsType)) {//车厂线索
									//新增提醒信息
									CommonUtils.addRemindInfo(Constant.REMIND_TYPE_03.toString(), leadsAllotId, "", logonUser.getDealerId(), adviserId, remindDate,"");
								}else{//DCRC线索
									//新增提醒信息
									CommonUtils.addRemindInfo(Constant.REMIND_TYPE_04.toString(), leadsAllotId, "", logonUser.getDealerId(), adviserId, remindDate,"");
								}
							}
						} else {
							remark=remark+"-重复分派不同顾问";
							leadsNewPoByDcrc.setFailureRemark(remark);
							dao.update(leadsOldPoByDcrc, leadsNewPoByDcrc);
							TPcLeadsAllotPO oldPo = new TPcLeadsAllotPO();
							TPcLeadsAllotPO newPo = new TPcLeadsAllotPO();
							oldPo.setLeadsAllotId(Long.parseLong(leadsAllotId));
							//newPo.setLeadsAllotId(Long.parseLong(leadsAllotId));
							newPo.setAdviser(adviserId);
							newPo.setAllotAdviserDate(new Date());
							newPo.setUpdateBy(logonUser.getUserId());
							newPo.setUpdateDate(new Date());
							dao.update(oldPo, newPo);
							//修改销售线索主表状态为有效
							TPcLeadsPO oldLeadsPo = new TPcLeadsPO();
							oldLeadsPo.setLeadsCode(Long.parseLong(leadsCode));
							TPcLeadsPO newLeadsPo = new TPcLeadsPO();
							newLeadsPo.setLeadsCode(Long.parseLong(leadsCode));
							newLeadsPo.setLeadsStatus(Constant.LEADS_STATUS_01.longValue());
							newLeadsPo.setUpdateBy(logonUser.getUserId());
							newLeadsPo.setUpdateDate(new Date());
							dao.update(oldLeadsPo, newLeadsPo);
							
							//标记提醒信息为已完成
							CommonUtils.setRemindDone(leadsAllotId,Constant.REMIND_TYPE_01.toString());
							CommonUtils.setRemindDone(leadsAllotId,Constant.REMIND_TYPE_02.toString());
							CommonUtils.setRemindDone(leadsAllotId,Constant.REMIND_TYPE_03.toString());
							CommonUtils.setRemindDone(leadsAllotId,Constant.REMIND_TYPE_04.toString());
							
							if("60141001".equals(leadsType)) {//车厂线索
								//新增提醒信息
								CommonUtils.addRemindInfo(Constant.REMIND_TYPE_03.toString(), leadsAllotId, "", logonUser.getDealerId(), adviserId, remindDate,"");
							} else{//DCRC线索
								//新增提醒信息
								CommonUtils.addRemindInfo(Constant.REMIND_TYPE_04.toString(), leadsAllotId, "", logonUser.getDealerId(), adviserId, remindDate,"");
							}
						}
						
					} else {//未存在则新到增分派顾问（做修改，增加顾问字段信息）
						remark=remark+"-未建档";
						leadsNewPoByDcrc.setFailureRemark(remark);
						dao.update(leadsOldPoByDcrc, leadsNewPoByDcrc);
						
						TPcLeadsAllotPO oldPo = new TPcLeadsAllotPO();
						TPcLeadsAllotPO newPo = new TPcLeadsAllotPO();
						oldPo.setLeadsAllotId(Long.parseLong(leadsAllotId));
						//newPo.setLeadsAllotId(Long.parseLong(leadsAllotId));
						newPo.setAdviser(adviserId);
						newPo.setAllotAdviserDate(new Date());
						dao.update(oldPo, newPo);
						
						//修改销售线索主表状态为有效
						TPcLeadsPO oldLeadsPo = new TPcLeadsPO();
						oldLeadsPo.setLeadsCode(Long.parseLong(leadsCode));
						TPcLeadsPO newLeadsPo = new TPcLeadsPO();
						newLeadsPo.setLeadsCode(Long.parseLong(leadsCode));
						newLeadsPo.setLeadsStatus(Constant.LEADS_STATUS_01.longValue());
						newLeadsPo.setUpdateBy(logonUser.getUserId());
						newLeadsPo.setUpdateDate(new Date());
						dao.update(oldLeadsPo, newLeadsPo);
						
						//标记提醒信息为已完成
						CommonUtils.setRemindDone(leadsAllotId,Constant.REMIND_TYPE_01.toString());
						CommonUtils.setRemindDone(leadsAllotId,Constant.REMIND_TYPE_02.toString());
						CommonUtils.setRemindDone(leadsAllotId,Constant.REMIND_TYPE_03.toString());
						CommonUtils.setRemindDone(leadsAllotId,Constant.REMIND_TYPE_04.toString());
						
						if("60141001".equals(leadsType)) {//车厂线索
							//新增提醒信息
							CommonUtils.addRemindInfo(Constant.REMIND_TYPE_03.toString(), leadsAllotId, "", logonUser.getDealerId(), adviserId, remindDate,"");
						}else{
							//新增提醒信息
							CommonUtils.addRemindInfo(Constant.REMIND_TYPE_04.toString(), leadsAllotId, "", logonUser.getDealerId(), adviserId, remindDate,"");
						}
					}
				// 页面多选线索进行分派
			} else {
				remark=remark+"-多选线索";
				leadsNewPoByDcrc.setFailureRemark(remark);
				dao.update(leadsOldPoByDcrc, leadsNewPoByDcrc);
				// 拆分销售线索
				String[] leadsGroup2 = leadsGroup.split(",");

				String[] leadsCodeGroup2 = leadsCodeGroup.split(",");
				
				for (int i = 0; i < leadsGroup2.length; i++) {
					
					String lgCode = leadsGroup2[i];//线索分派ID
					
					String leadsCode2=leadsCodeGroup2[i];//线索ID
					
					//获取销售线索主表信息
					TPcLeadsPO leadsPo3 = new TPcLeadsPO();
					leadsPo3.setLeadsCode(Long.parseLong(leadsCode2));
					TPcLeadsPO leadsPo4 = (TPcLeadsPO)dao.select(leadsPo3).get(0);
				
					//获取线索类别(60141001:车厂下发；60141002：DCRC录入)
					 String leadsType2 = leadsPo4.getLeadsType();
					 leadsType2 = leadsType2.substring(0, 8);
					// 获取销售线索信息
					TPcLeadsAllotPO po = new TPcLeadsAllotPO();
					po.setLeadsAllotId(Long.parseLong(lgCode));
					TPcLeadsAllotPO po2 = (TPcLeadsAllotPO)dao.select(po).get(0);
					//判断是否已建过档案（电话、姓名、经销商ID）
					List<Map<String, Object>> getHasList = null;
					getHasList=dao.getHasCustomer(po2.getTelephone(),po2.getCustomerName(),logonUser.getDealerId());
					//已存在档案，不能进行分派顾问
					if(getHasList.size()>0) {
						remark=remark+"-已建档";
						leadsNewPoByDcrc.setFailureRemark(remark);
						dao.update(leadsOldPoByDcrc, leadsNewPoByDcrc);
						//获取已建档的分派顾问
						String adviser = getHasList.get(0).get("ADVISER").toString();
						String dealerId = getHasList.get(0).get("DEALER_ID").toString();
						String customerId = getHasList.get(0).get("CUSTOMER_ID").toString();
						String ctmType = getHasList.get(0).get("CTM_TYPE").toString();
						//如果已建档的分派顾问等于重新分派的顾问，并且如果客户类型不属于战败、失效、保有则设为无效并增加接触点信息，不等则更新线索中的顾问
						if(adviser.equals(adviserId)) {
							remark=remark+"-重复分派同一顾问";
							leadsNewPoByDcrc.setFailureRemark(remark);
							dao.update(leadsOldPoByDcrc, leadsNewPoByDcrc);
							//修改销售线索主表状态为无效
							TPcLeadsPO oldLeadsPo = new TPcLeadsPO();
							oldLeadsPo.setLeadsCode(Long.parseLong(leadsCode2));
							TPcLeadsPO newLeadsPo = new TPcLeadsPO();
							//newLeadsPo.setLeadsCode(Long.parseLong(leadsCode));
							if(!"60341001".equals(ctmType)&&!"60341004".equals(ctmType)&&!"60341005".equals(ctmType)) {
								newLeadsPo.setIfHandle(10041001);
								newLeadsPo.setLeadsStatus(Constant.LEADS_STATUS_05.longValue());
							} else {
								newLeadsPo.setLeadsStatus(Constant.LEADS_STATUS_01.longValue());
							}
							dao.update(oldLeadsPo, newLeadsPo);
							//修改线索分派表状态为无效
							TPcLeadsAllotPO oldPo = new TPcLeadsAllotPO();
							oldPo.setLeadsAllotId(Long.parseLong(lgCode));
							TPcLeadsAllotPO newPo = new TPcLeadsAllotPO();
							//newPo.setLeadsAllotId(Long.parseLong(lgCode));
							if(!"60341001".equals(ctmType)&&!"60341004".equals(ctmType)&&!"60341005".equals(ctmType)) {
								newPo.setStatus(Constant.STATUS_DISABLE);//设置为无效
								newPo.setIfConfirm(Constant.ADVISER_CONFIRM_02);
								newPo.setConfirmDate(new Date());
								newPo.setCustomerId(Long.parseLong(customerId));
							} else {
								newPo.setStatus(Constant.STATUS_ENABLE);//设置为有效
								newPo.setIfConfirm(Constant.ADVISER_CONFIRM_01);
								newPo.setAdviser(adviserId);
								newPo.setAllotAdviserDate(new Date());
							}
							newPo.setUpdateBy(logonUser.getUserId());
							newPo.setUpdateDate(new Date());
							dao.update(oldPo, newPo);
							
							if(!"60341001".equals(ctmType)&&!"60341004".equals(ctmType)&&!"60341005".equals(ctmType)) {
								//增加接触点信息
								CommonUtils.addContackPoint(Constant.POINT_WAY_01, "重复线索", customerId, adviser, dealerId);
								//修改状态
								CommonUtils.updateLeadStatus(leadsCode2);
								String repeatLeads = SequenceManager.getSequence("");
								//新增提醒信息
								CommonUtils.addRemindInfo(Constant.REMIND_TYPE_20.toString(), repeatLeads, customerId, dealerId, adviser, f1.format(new Date()),"");
								//标记线索为重复线索
								CommonUtils.updateIfRepeat(lgCode);
							} else {
								if("60141001".equals(leadsType2)) {//车厂线索
									//新增提醒信息
									CommonUtils.addRemindInfo(Constant.REMIND_TYPE_03.toString(), lgCode, "", logonUser.getDealerId(), adviserId, remindDate,"");
								} else{//DCRC线索
									//新增提醒信息
									CommonUtils.addRemindInfo(Constant.REMIND_TYPE_04.toString(), lgCode, "", logonUser.getDealerId(), adviserId, remindDate,"");
								}
							}
							//标记提醒信息为已完成
							CommonUtils.setRemindDone(lgCode,Constant.REMIND_TYPE_01.toString());
						} else {
							remark=remark+"-重复分派不同顾问";
							leadsNewPoByDcrc.setFailureRemark(remark);
							dao.update(leadsOldPoByDcrc, leadsNewPoByDcrc);
							
							TPcLeadsAllotPO oldPo = new TPcLeadsAllotPO();
							TPcLeadsAllotPO newPo = new TPcLeadsAllotPO();
							oldPo.setLeadsAllotId(Long.parseLong(lgCode));
							//newPo.setLeadsAllotId(Long.parseLong(leadsAllotId));
							newPo.setAdviser(adviserId);
							newPo.setAllotAdviserDate(new Date());
							newPo.setUpdateBy(logonUser.getUserId());
							newPo.setUpdateDate(new Date());
							dao.update(oldPo, newPo);
							//修改销售线索主表状态为有效
							TPcLeadsPO oldLeadsPo = new TPcLeadsPO();
							oldLeadsPo.setLeadsCode(Long.parseLong(leadsCode2));
							TPcLeadsPO newLeadsPo = new TPcLeadsPO();
							newLeadsPo.setLeadsCode(Long.parseLong(leadsCode2));
							newLeadsPo.setLeadsStatus(Constant.LEADS_STATUS_01.longValue());
							newLeadsPo.setUpdateBy(logonUser.getUserId());
							newLeadsPo.setUpdateDate(new Date());
							dao.update(oldLeadsPo, newLeadsPo);
							
							if("60141001".equals(leadsType2)) {//车厂线索
								//新增提醒信息
								CommonUtils.addRemindInfo(Constant.REMIND_TYPE_03.toString(), lgCode, "", logonUser.getDealerId(), adviserId, remindDate,"");
							} else{//DCRC线索
								//新增提醒信息
								CommonUtils.addRemindInfo(Constant.REMIND_TYPE_04.toString(), lgCode, "", logonUser.getDealerId(), adviserId, remindDate,"");
							}
							//标记提醒信息为已完成
							CommonUtils.setRemindDone(lgCode,Constant.REMIND_TYPE_01.toString());
						}
					} else {//未存在则新增分派到顾问（做修改，增加顾问字段信息）
						remark=remark+"-未建档";
						leadsNewPoByDcrc.setFailureRemark(remark);
						dao.update(leadsOldPoByDcrc, leadsNewPoByDcrc);
						
						TPcLeadsAllotPO oldPo = new TPcLeadsAllotPO();
						TPcLeadsAllotPO newPo = new TPcLeadsAllotPO();
						oldPo.setLeadsAllotId(Long.parseLong(lgCode));
						newPo.setLeadsAllotId(Long.parseLong(lgCode));
						newPo.setAdviser(adviserId);
						newPo.setAllotAdviserDate(new Date());
						newPo.setUpdateBy(logonUser.getUserId());
						newPo.setUpdateDate(new Date());
						dao.update(oldPo, newPo);
						
						//修改销售线索主表状态为有效
						TPcLeadsPO oldLeadsPo = new TPcLeadsPO();
						oldLeadsPo.setLeadsCode(Long.parseLong(leadsCode2));
						TPcLeadsPO newLeadsPo = new TPcLeadsPO();
						newLeadsPo.setLeadsCode(Long.parseLong(leadsCode2));
						newLeadsPo.setLeadsStatus(Constant.LEADS_STATUS_01.longValue());
						newLeadsPo.setUpdateBy(logonUser.getUserId());
						newLeadsPo.setUpdateDate(new Date());
						dao.update(oldLeadsPo, newLeadsPo);
						
						//标记提醒信息为已完成
						CommonUtils.setRemindDone(lgCode,Constant.REMIND_TYPE_01.toString());
						CommonUtils.setRemindDone(lgCode,Constant.REMIND_TYPE_02.toString());
						CommonUtils.setRemindDone(lgCode,Constant.REMIND_TYPE_03.toString());
						CommonUtils.setRemindDone(lgCode,Constant.REMIND_TYPE_04.toString());
						
						if("60141001".equals(leadsType2)) {//车厂线索
							//新增提醒信息
							CommonUtils.addRemindInfo(Constant.REMIND_TYPE_03.toString(), lgCode, "", logonUser.getDealerId(), adviserId, remindDate,"");
						}else{
							//新增提醒信息
							CommonUtils.addRemindInfo(Constant.REMIND_TYPE_04.toString(), lgCode, "", logonUser.getDealerId(), adviserId, remindDate,"");
						}
					}
				}
			}
			//判断是否顾问登陆
			if(CommonUtils.judgeAdviserLogin(logonUser.getUserId().toString())) {
				managerLogon = "no";
			} else if(CommonUtils.judgeDirectorLogin(logonUser.getUserId().toString())){//判断是否主管登陆
				managerLogon = "no";
			} else if(CommonUtils.judgeDcrcLogin(logonUser.getUserId().toString())){//判断是否DCRC登陆
				managerLogon = "no";
			} 
			act.setOutData("managerLogon", managerLogon);
			act.setOutData("leadsAllotId",leadsAllotId);
			act.setOutData("leadsCode",leadsCode);
			act.setOutData("dealerId",logonUser.getDealerId());
			if(comeFlag=="xsfpcx"||"xsfpcx".equals(comeFlag)) {
				act.setForword(dlrLeadsFindUrl);
			} else {
				act.setForword(dealerLeadsAllotUrl);
			}
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"未知错误!");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * DLR初始使线索查询页面
	 */
	public void dlrLeadsFind() {
		ActionContext act = ActionContext.getContext();
		DlrLeadsManageDao dao = new DlrLeadsManageDao();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		String managerLogon = "yes";
		String adviserLogon = "no";
		String userId = null;
		try {
			//判断是否顾问登陆
			if(CommonUtils.judgeAdviserLogin(logonUser.getUserId().toString())) {
				managerLogon = "no";
				adviserLogon = "yes";
			} else if(CommonUtils.judgeDirectorLogin(logonUser.getUserId().toString())){//判断是否主管登陆
				managerLogon = "no";
				adviserLogon = "no";
				//获取主管下属分组的所有顾问
				userId = CommonUtils.getAdviserByDirector(logonUser.getUserId().toString());
			} else if(CommonUtils.judgeDcrcLogin(logonUser.getUserId().toString())){//判断是否DCRC登陆
				managerLogon = "no";
				adviserLogon = "no";
			} 
			//获取顾问列表
			List<DynaBean> adviserList = dao.getAdviserBydealer2(logonUser.getDealerId(),userId);
			//获取分组列表
			List<DynaBean> groupList = dao.getGroupBydealer(logonUser.getDealerId());
			act.setOutData("adviserList", adviserList);
			act.setOutData("groupList", groupList);
			act.setOutData("managerLogon", managerLogon);
			act.setOutData("adviserLogon", adviserLogon);
			act.setForword(dlrLeadsFindUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"未知错误!");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * DLR初始线索查询
	 */
	public void dlrLeadsFindQuery() {
		DlrLeadsManageDao dao = new DlrLeadsManageDao();
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			//获取页面参数
			String customerName = CommonUtils.checkNull(request.getParamValue("customer_name"));
			String telephone = CommonUtils.checkNull(request.getParamValue("telephone"));
			String startDate = CommonUtils.checkNull(request.getParamValue("startDate"));
			String endDate = CommonUtils.checkNull(request.getParamValue("endDate"));
			String allotStartDate = CommonUtils.checkNull(request.getParamValue("allotStartDate"));
			String allotEndDate = CommonUtils.checkNull(request.getParamValue("allotEndDate"));
			String leadsOrigin = CommonUtils.checkNull(request.getParamValue("leads_origin"));
			String allotStatus = CommonUtils.checkNull(request.getParamValue("allot_status"));
			String timeOut = CommonUtils.checkNull(request.getParamValue("time_out"));
			String userDealerId = CommonUtils.checkNull(logonUser.getDealerId());
			String leadsStatus = CommonUtils.checkNull(request.getParamValue("leads_status"));
			String adviser = CommonUtils.checkNull(request.getParamValue("adviser"));
			String groupId = CommonUtils.checkNull(request.getParamValue("groupId"));
			String jcway = CommonUtils.checkNull(request.getParamValue("collect_fashion"));
			String seriesId = CommonUtils.checkNull(request.getParamValue("seriesId")) ;
			String adviserId = null;
			String managerLogon = "yes";
			
			//判断是否顾问登陆
			if(CommonUtils.judgeAdviserLogin(logonUser.getUserId().toString())) {
				adviser = null;
				adviserId = logonUser.getUserId().toString();
				managerLogon = "no";
			} else if(CommonUtils.judgeDirectorLogin(logonUser.getUserId().toString())){//判断是否主管登陆
				//获取主管下属分组的所有顾问
				adviserId = CommonUtils.getAdviserByDirector(logonUser.getUserId().toString());
				managerLogon = "no";
			} else if(CommonUtils.judgeDcrcLogin(logonUser.getUserId().toString())){//判断是否DCRC登陆
				adviserId = null;
				managerLogon = "no";
			} 
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1; // 处理当前页
			PageResult<Map<String, Object>> ps = dao.dlrLeadsFindQuery(customerName,
					telephone,startDate,endDate,allotStartDate,allotEndDate,leadsOrigin,allotStatus,timeOut,userDealerId,leadsStatus,adviser,adviserId,groupId,jcway,
					seriesId,curPage, Constant.PAGE_SIZE);
			act.setOutData("ps", ps);
			act.setOutData("managerLogon", managerLogon);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "销售线索查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * DCRC录入初始查询页面
	 */
	public void dcrcEnter() {
		ActionContext act = ActionContext.getContext();
		DlrLeadsManageDao dao = new DlrLeadsManageDao();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			//获取顾问列表
			List<DynaBean> adviserList = dao.getAdviserBydealer(logonUser.getDealerId());
			//获取分组列表
			List<DynaBean> groupList = dao.getGroupBydealer(logonUser.getDealerId());
			act.setOutData("adviserList", adviserList);
			act.setOutData("groupList", groupList);
			act.setForword(dcrcEnterUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"未知错误!");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 顾问录入初始查询页面
	 */
	public void adviserEnter() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			act.setForword(adviserEnterUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"未知错误!");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * DCRC录入查询
	 */
	public void dcrcEnterFindQuery() {
		DlrLeadsManageDao dao = new DlrLeadsManageDao();
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			//获取页面参数
			String customerName = CommonUtils.checkNull(request.getParamValue("customer_name"));
			String telephone = CommonUtils.checkNull(request.getParamValue("telephone"));
			String startDate = CommonUtils.checkNull(request.getParamValue("startDate"));
			String endDate = CommonUtils.checkNull(request.getParamValue("endDate"));
			String customerStatus = CommonUtils.checkNull(request.getParamValue("customer_status"));
			String allotStatus = CommonUtils.checkNull(request.getParamValue("allot_status"));
			String adviser = CommonUtils.checkNull(request.getParamValue("adviser"));
			String groupId = CommonUtils.checkNull(request.getParamValue("groupId"));
			String jcway = CommonUtils.checkNull(request.getParamValue("collect_fashion"));
			String userDealerId = CommonUtils.checkNull(logonUser.getDealerId());
			
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1; // 处理当前页
			PageResult<Map<String, Object>> ps = dao.dcrcEnterFindQuery(customerName,
					telephone,startDate,endDate,customerStatus,allotStatus,userDealerId,adviser,groupId,jcway,
					curPage, Constant.PAGE_SIZE);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "销售线索查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 顾问录入查询
	 */
	public void adviserEnterFindQuery() {
		DlrLeadsManageDao dao = new DlrLeadsManageDao();
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			//获取页面参数
			String customerName = CommonUtils.checkNull(request.getParamValue("customer_name"));
			String telephone = CommonUtils.checkNull(request.getParamValue("telephone"));
			String startDate = CommonUtils.checkNull(request.getParamValue("startDate"));
			String endDate = CommonUtils.checkNull(request.getParamValue("endDate"));
			String customerStatus = CommonUtils.checkNull(request.getParamValue("customer_status"));
			String allotStatus = CommonUtils.checkNull(request.getParamValue("allot_status"));
			String userDealerId = CommonUtils.checkNull(logonUser.getDealerId());
			String adviser = logonUser.getUserId().toString();
			
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1; // 处理当前页
			PageResult<Map<String, Object>> ps = dao.adviserEnterFindQuery(customerName,
					telephone,startDate,endDate,customerStatus,allotStatus,userDealerId,adviser,
					curPage, Constant.PAGE_SIZE);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "销售线索查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * DCRC录入新增页面
	 */
	public void dcrcEnterInsert() {
		ActionContext act = ActionContext.getContext();
		DlrLeadsManageDao dao = new DlrLeadsManageDao();
		TaskManageDao dao2 = new TaskManageDao();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			String leadsCode = request.getParamValue("leadsCode");
			String leadsAllotId = request.getParamValue("leadsAllotId");
			String updateFlag = request.getParamValue("updateFlag");
			String leadsOrigin = null;
			String telephone = null;
			String intentVehicle = null;
			String adviser = null;
			String adviser2 = null;
			String customerDescribe = null;
			String comeMeet = null;
			String jcWay = null;
			String jcWay2 = null;
			String buyBudget=null;
			String buyBudget2=null;
			String testDriving=null;
			String testDriving2=null;
			String buyType=null;
			String buyType2=null;
			String comeDate=null;
			String customerType=null;
			String customerType2=null;
			//String intentType=null;
			// String intentType2=null;
			String customerName=null;
			
			String oldCustomerName = null;
			String oldTelephone = null;
			String oldVehicleId = null;
			                      
			
			//进入修改页面
			if("yes".equals(updateFlag)) {
				//根据leadsCode获取线索信息
				List<DynaBean> leadsList = dao.getInfoByleadsCode(leadsCode,leadsAllotId);
				Iterator it = (Iterator) leadsList.iterator();
				while(it.hasNext()) {
					DynaBean db = (DynaBean)it.next();
					if(db.get("LEADS_ORIGIN")!=null&&!"".equals(db.get("LEADS_ORIGIN"))) {
						leadsOrigin = db.get("LEADS_ORIGIN").toString();
					} else {
						leadsOrigin = "";
					}
					if(db.get("TELEPHONE")!=null&&!"".equals(db.get("TELEPHONE"))) {
						telephone = db.get("TELEPHONE").toString();
					} else {
						telephone = "";
					}
					if(db.get("INTENT_VEHICLE")!=null&&!"".equals(db.get("INTENT_VEHICLE"))) {
						intentVehicle = db.get("INTENT_VEHICLE").toString();
					} else {
						intentVehicle = null;
					}
					if(db.get("ADVISER")!=null&&!"".equals(db.get("ADVISER"))) {
						adviser = db.get("ADVISER").toString();
					} else {
						adviser = "";
					}
					if(db.get("ADVISER2")!=null&&!"".equals(db.get("ADVISER2"))) {
						adviser2 = db.get("ADVISER2").toString();
					} else {
						adviser2 = "";
					}
					if(db.get("CUSTOMER_DESCRIBE")!=null&&!"".equals(db.get("CUSTOMER_DESCRIBE"))) {
						customerDescribe = db.get("CUSTOMER_DESCRIBE").toString();
					} else {
						customerDescribe = "";
					}
					if(db.get("COME_MEET")!=null&&!"".equals(db.get("COME_MEET"))) {
						comeMeet = db.get("COME_MEET").toString();
					} else {
						comeMeet = "";
					}
					if(db.get("JC_WAY")!=null&&!"".equals(db.get("JC_WAY"))) {
						jcWay = db.get("JC_WAY").toString();
					} else {
						jcWay = "";
					}
					if(db.get("JC_WAY2")!=null&&!"".equals(db.get("JC_WAY2"))) {
						jcWay2 = db.get("JC_WAY2").toString();
					} else {
						jcWay2 = "";
					}
					
					if(db.get("CUSTOMER_NAME")!=null&&!"".equals(db.get("CUSTOMER_NAME"))) {
						customerName = db.get("CUSTOMER_NAME").toString();
					} else {
						customerName = "";
					}
					if(db.get("BUY_BUDGET")!=null&&!"".equals(db.get("BUY_BUDGET"))) {
						buyBudget = db.get("BUY_BUDGET").toString();
					} else {
						buyBudget = "";
					}
					if(db.get("BUY_BUDGET2")!=null&&!"".equals(db.get("BUY_BUDGET2"))) {
						buyBudget2 = db.get("BUY_BUDGET2").toString();
					} else {
						buyBudget2 = "";
					}
					if(db.get("TEST_DRIVING")!=null&&!"".equals(db.get("TEST_DRIVING"))) {
						testDriving = db.get("TEST_DRIVING").toString();
					} else {
						testDriving = "";
					}
					if(db.get("TEST_DRIVING2")!=null&&!"".equals(db.get("TEST_DRIVING2"))) {
						testDriving2 = db.get("TEST_DRIVING2").toString();
					} else {
						testDriving2= "";
					}
					
					if(db.get("BUY_TYPE")!=null&&!"".equals(db.get("BUY_TYPE"))) {
						buyType = db.get("BUY_TYPE").toString();
					} else {
						buyType = "";
					}
					if(db.get("BUY_TYPE2")!=null&&!"".equals(db.get("BUY_TYPE2"))) {
						buyType2 = db.get("BUY_TYPE2").toString();
					} else {
						buyType2 = "";
					}
					if(db.get("CUSTOMER_TYPE")!=null&&!"".equals(db.get("CUSTOMER_TYPE"))) {
						customerType = db.get("CUSTOMER_TYPE").toString();
					} else {
						customerType = "";
					}
					if(db.get("CUSTOMER_TYPE2")!=null&&!"".equals(db.get("CUSTOMER_TYPE2"))) {
						customerType2 = db.get("CUSTOMER_TYPE2").toString();
					} else {
						customerType2 = "";
					}
					/*
					if(db.get("INTENT_TYPE")!=null&&!"".equals(db.get("INTENT_TYPE"))) {
						intentType = db.get("INTENT_TYPE").toString();
					} else {
						intentType = "";
					}
					if(db.get("INTENT_TYPE2")!=null&&!"".equals(db.get("INTENT_TYPE2"))) {
						intentType2 = db.get("INTENT_TYPE2").toString();
					} else {
						intentType2 = "";
					}
					*/
					if(db.get("COME_DATE")!=null&&!"".equals(db.get("COME_DATE"))) {
						comeDate = db.get("COME_DATE").toString();
					} else {
						comeDate = "";
					}
					
					if(db.get("OLD_CUSTOMER_NAME")!=null&&!"".equals(db.get("OLD_CUSTOMER_NAME"))) {
						oldCustomerName = db.get("OLD_CUSTOMER_NAME").toString();
						
					} else {
						oldCustomerName = "";
						
					}
					if(db.get("OLD_TELEPHONE")!=null&&!"".equals(db.get("OLD_TELEPHONE"))) {
						oldTelephone = db.get("OLD_TELEPHONE").toString();
					} else {
						oldTelephone = "";
					}
					if(db.get("OLD_VEHICLE_ID")!=null&&!"".equals(db.get("OLD_VEHICLE_ID"))) {
						oldVehicleId = db.get("OLD_VEHICLE_ID").toString();
					} else {
						oldVehicleId = "";
					}
					
					 
					
				}
				
			}
			//获取意向车型一级列表
			List<DynaBean> menusAList = dao2.getIntentVehicleA();
			//获取意向车型二级列表
			List<DynaBean> menusBList = dao2.getIntentVehicleB();
			//获取一级意向车型对应二级列表
			List<DynaBean> menusABList = null;
			String upSeriesCode = null;
			List<DynaBean> menusABList2 = null;
			if(intentVehicle!=null&&!"".equals(intentVehicle)) {
				menusABList = dao2.getIntentVehicleAB(intentVehicle);
				DynaBean db2 = menusABList.get(0);
				upSeriesCode = db2.get("PARENTID").toString();
				menusABList2 = dao2.getIntentVehicleAB2(upSeriesCode);
			}
			
			//获取顾问列表
			List<DynaBean> adviserList = dao.getAdviserBydealer(logonUser.getDealerId());
			//取当前时间到页面
			Date date = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			SimpleDateFormat sdf2 = new SimpleDateFormat("HH");
			SimpleDateFormat sdf3 = new SimpleDateFormat("mm");
			String nowDate = sdf.format(date);
			String nowHour = sdf2.format(date);
			String nowMinute = sdf3.format(date);
			SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			
			if("yes".equals(updateFlag)) {
				Date d=sdf1.parse(comeDate);
				act.setOutData("nowDate", sdf.format(d));
				act.setOutData("nowHour", sdf2.format(d));
				act.setOutData("nowMinute", sdf3.format(d));
			}else{
				act.setOutData("nowDate", nowDate);
				act.setOutData("nowHour", nowHour);
				act.setOutData("nowMinute", nowMinute);
			}
			act.setOutData("adviserList", adviserList);
			act.setOutData("menusAList", menusAList);
			act.setOutData("menusBList", menusBList);
			act.setOutData("menusABList2", menusABList2);
			act.setOutData("upSeriesCode", upSeriesCode);
			act.setOutData("leadsCode", leadsCode);
			act.setOutData("leadsAllotId", leadsAllotId);
			act.setOutData("leadsOrigin", leadsOrigin);
			act.setOutData("telephone", telephone);
			act.setOutData("intentVehicle", intentVehicle);
			act.setOutData("adviser", adviser);
			act.setOutData("adviser2", adviser2);
			act.setOutData("customerDescribe", customerDescribe);
			act.setOutData("comeMeet", comeMeet);
			act.setOutData("jcWay", jcWay);
			act.setOutData("jcWay2", jcWay2);
			act.setOutData("customerName", customerName);
			act.setOutData("buyType", buyType);
			act.setOutData("buyType2", buyType2);
			act.setOutData("buyBudget", buyBudget);
			act.setOutData("buyBudget2", buyBudget2);
			act.setOutData("testDriving", testDriving);
			act.setOutData("testDriving2", testDriving2);
			act.setOutData("customerType", customerType);
			act.setOutData("customerType2", customerType2);
			act.setOutData("oldCustomerName", oldCustomerName);
			act.setOutData("oldTelephone", oldTelephone);
			act.setOutData("oldVehicleId", oldVehicleId);
			/*
			act.setOutData("intentType", intentType);
			act.setOutData("intentType2", intentType2);
			*/
			act.setOutData("updateFlag", updateFlag);
			act.setForword(dcrcEnterInsertUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"未知错误!");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 顾问录入新增页面
	 */
	public void adviserEnterInsert() {
		ActionContext act = ActionContext.getContext();
		DlrLeadsManageDao dao = new DlrLeadsManageDao();
		TaskManageDao dao2 = new TaskManageDao();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			//获取意向车型一级列表
			List<DynaBean> menusAList = dao2.getIntentVehicleA();
			//获取意向车型二级列表
			List<DynaBean> menusBList = dao2.getIntentVehicleB();
			//获取顾问列表
			List<DynaBean> adviserList = dao.getAdviserBydealer(logonUser.getDealerId());
			//取当前时间到页面
			Date date = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			SimpleDateFormat sdf2 = new SimpleDateFormat("HH");
			SimpleDateFormat sdf3 = new SimpleDateFormat("mm");
			String nowDate = sdf.format(date);
			String nowHour = sdf2.format(date);
			String nowMinute = sdf3.format(date);
			act.setOutData("nowDate", nowDate);
			act.setOutData("nowHour", nowHour);
			act.setOutData("nowMinute", nowMinute);
			act.setOutData("adviserList", adviserList);
			act.setOutData("menusAList", menusAList);
			act.setOutData("menusBList", menusBList);
			act.setForword(adviserEnterInsertUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"未知错误!");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * DCRC录入新增保存
	 */
	public void dcrcEnterInsertSave() {
		DlrLeadsManageDao dao = new DlrLeadsManageDao();
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			//获取页面参数
			String leadsOrigin = CommonUtils.checkNull(request.getParamValue("leads_origin"));
			String startDate = CommonUtils.checkNull(request.getParamValue("startDate"));
			String startHHDate = CommonUtils.checkNull(request.getParamValue("startHHDate"));
			String startMMDate = CommonUtils.checkNull(request.getParamValue("startMMDate"));
			String startDate2 = CommonUtils.checkNull(request.getParamValue("startDate2"));
			String startHHDate2 = CommonUtils.checkNull(request.getParamValue("startHHDate2"));
			String startMMDate2 = CommonUtils.checkNull(request.getParamValue("startMMDate2"));
			String adviser = CommonUtils.checkNull(request.getParamValue("adviserId"));
			String describe = CommonUtils.checkNull(request.getParamValue("describe"));
			String telephone = CommonUtils.checkNull(request.getParamValue("telephone"));
			String userDealerId = CommonUtils.checkNull(logonUser.getDealerId());
			String comeDate = startDate + " "+startHHDate+":"+startMMDate+":00";
			String comeDate2 = startDate2 + " "+startHHDate2+":"+startMMDate2+":00";
			String intentVehicle = CommonUtils.checkNull(request.getParamValue("intentVehicleB"));
			String comeMeet = CommonUtils.checkNull(request.getParamValue("come_meet"));
			String jcway = CommonUtils.checkNull(request.getParamValue("collect_fashion"));
			String customerName = CommonUtils.checkNull(request.getParamValue("customer_name"));//客户名称
			String buyBudget = CommonUtils.checkNull(request.getParamValue("buy_budget"));//购车预算
			String customerType = CommonUtils.checkNull(request.getParamValue("customer_type"));//客户类型
			String testDriving = CommonUtils.checkNull(request.getParamValue("test_driving"));//试乘试驾
			String buyType = CommonUtils.checkNull(request.getParamValue("buy_type"));//购买类型
			//String intentType = CommonUtils.checkNull(request.getParamValue("intent_type"));//意向等级
			String oldCustomerName = CommonUtils.checkNull(request.getParamValue("old_customer_name")); //老客户姓名	
			String oldTelephone = CommonUtils.checkNull(request.getParamValue("old_telephone"));		//老客户电话
			String oldVehicleId = CommonUtils.checkNull(request.getParamValue("old_vehicle_id"));		//老客户车架号
			
			
			//9：00-17:00之间分配给经销商的公司类信息，主管当日19:00前完成分配，顾问必须在当日完成处理
			//17:00-次日9:00分配给经销商的公司类信息，主管次日11:00前完成分配，顾问必须在次日12:00前完成处理。
			Date d1 = new Date(); //当前时间
			String d2 = "090000";
			String d3 = "170000";
			String remindDate = null;
			SimpleDateFormat f = new SimpleDateFormat("hhmmss"); //格式化为 hhmmss
			int d1Number = Integer.parseInt(f.format(d1).toString()); //将第一个时间格式化后转为int
			int d2Number = Integer.parseInt(d2); //将第二个时间格式化后转为int
			int d3Number = Integer.parseInt(d3); //将第三个时间格式化后转为int
			
			SimpleDateFormat f1 = new SimpleDateFormat("yyyy-MM-dd");
			Calendar c = Calendar.getInstance();
			c.add(Calendar.DAY_OF_MONTH, 1);
								
			if(d1Number>=d2Number && d1Number<=d3Number){
				remindDate = f1.format(d1);
			} else {
				remindDate = f1.format(c.getTime());
			}
			
			//保存新增的DCRC录入
			//新增到销售线索表
			TPcLeadsPO tlkeyPo = new TPcLeadsPO();
			//获取销售线索主键
			Long LeadsPK = dao.getLongPK(tlkeyPo);
			TPcLeadsPO tlPo = new TPcLeadsPO();
			tlPo.setLeadsCode(LeadsPK);
			tlPo.setLeadsType(Constant.LEADS_TYPE_02.toString());
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			//线索来源判断
			if("60151011".equals(leadsOrigin)) {
				tlPo.setLeadsOrigin("60151011");
				tlPo.setComeDate(sdf.parse(comeDate));
				tlPo.setLeadsStatus(Constant.LEADS_STATUS_04.longValue());
				tlPo.setComeMeet(Long.parseLong(comeMeet));
				tlPo.setJcWay(jcway);
				tlPo.setTelephone(telephone);
			} else {
				tlPo.setLeadsOrigin("60151012");
				tlPo.setComeDate(sdf.parse(comeDate2));
				tlPo.setLeadsStatus(Constant.LEADS_STATUS_04.longValue());
				tlPo.setTelephone(telephone);
			}
			tlPo.setCollectDate(new Date());
			tlPo.setCreateDate(new Date());
			tlPo.setCreateBy(logonUser.getUserId().toString());
			tlPo.setCustomerDescribe(describe);
			tlPo.setIntentVehicle(intentVehicle);
			
			tlPo.setCustomerName(customerName);
			tlPo.setBuyBudget(buyBudget);
			tlPo.setCustomerType(customerType);
			tlPo.setTestDriving(testDriving);
			//tlPo.setIntentType(intentType);
			tlPo.setBuyType(buyType);
			
			tlPo.setOldCustomerName(oldCustomerName);
			tlPo.setOldTelephone(oldTelephone);
			tlPo.setOldVehicleId(oldVehicleId);
			
			dao.insert(tlPo);
			
			//新增到销售线索分派表
			TPcLeadsAllotPO tplkeyPo = new TPcLeadsAllotPO();
			//获取销售线索分派主键
			Long LeadsAllotPK = dao.getLongPK(tplkeyPo);
			TPcLeadsAllotPO tplPo = new TPcLeadsAllotPO();
			tplPo.setLeadsAllotId(LeadsAllotPK);
			tplPo.setLeadsCode(LeadsPK);
			tplPo.setDealerId(userDealerId);
			tplPo.setAllotDealerDate(new Date());
			tplPo.setAdviser(adviser);
			tplPo.setTelephone(telephone);
			tplPo.setAllotAdviserDate(new Date());
			tplPo.setAllotAgain(Constant.IF_TYPE_NO);
			tplPo.setStatus(Constant.STATUS_ENABLE);
			tplPo.setUpdateDate(new Date());
			tplPo.setUpdateBy(logonUser.getUserId());
			dao.insert(tplPo);
			
			//新增提醒信息
//			CommonUtils.addRemindInfo(Constant.REMIND_TYPE_02.toString(), LeadsAllotPK.toString(), "", userDealerId, adviser, remindDate,"");
			
			act.setForword(dcrcEnterUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "销售线索查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * DCRC录入新增修改
	 */
	public void dcrcEnterInsertUpdate() {
		DlrLeadsManageDao dao = new DlrLeadsManageDao();
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			//获取页面参数
			String leadsCode = CommonUtils.checkNull(request.getParamValue("leadsCode"));
			String leadsAllotId = CommonUtils.checkNull(request.getParamValue("leadsAllotId"));
			String leadsOrigin = CommonUtils.checkNull(request.getParamValue("leads_origin"));
			String startDate = CommonUtils.checkNull(request.getParamValue("startDate"));
			String startHHDate = CommonUtils.checkNull(request.getParamValue("startHHDate"));
			String startMMDate = CommonUtils.checkNull(request.getParamValue("startMMDate"));
			String startDate2 = CommonUtils.checkNull(request.getParamValue("startDate2"));
			String startHHDate2 = CommonUtils.checkNull(request.getParamValue("startHHDate2"));
			String startMMDate2 = CommonUtils.checkNull(request.getParamValue("startMMDate2"));
			
			String endDate = CommonUtils.checkNull(request.getParamValue("endDate"));
			String endHHDate = CommonUtils.checkNull(request.getParamValue("endHHDate"));
			String endMMDate = CommonUtils.checkNull(request.getParamValue("endMMDate"));
			
			String adviser = CommonUtils.checkNull(request.getParamValue("adviserId"));
			String describe = CommonUtils.checkNull(request.getParamValue("describe"));
			String telephone = CommonUtils.checkNull(request.getParamValue("telephone"));
			String userDealerId = CommonUtils.checkNull(logonUser.getDealerId());
			String comeDate = startDate + " "+startHHDate+":"+startMMDate+":00";
			String comeDate2 = startDate2 + " "+startHHDate2+":"+startMMDate2+":00";
			String comeDate3 = endDate + " "+endHHDate+":"+endMMDate+":00";
			String intentVehicle = CommonUtils.checkNull(request.getParamValue("intentVehicleB"));
			String comeMeet = CommonUtils.checkNull(request.getParamValue("come_meet"));
			String jcway = CommonUtils.checkNull(request.getParamValue("collect_fashion"));
			String customerName = CommonUtils.checkNull(request.getParamValue("customer_name"));//客户名称
			String buyBudget = CommonUtils.checkNull(request.getParamValue("buy_budget"));//购车预算
			String customerType = CommonUtils.checkNull(request.getParamValue("customer_type"));//客户类型
			String testDriving = CommonUtils.checkNull(request.getParamValue("test_driving"));//试乘试驾
			String buyType = CommonUtils.checkNull(request.getParamValue("buy_type"));//购买类型
			// old_customer_name  old_telephone old_vehicle_id String intentType = CommonUtils.checkNull(request.getParamValue("intent_type"));//意向等级
			
			String oldCustomerName = CommonUtils.checkNull(request.getParamValue("old_customer_name")); //老客户姓名	
			String oldTelephone = CommonUtils.checkNull(request.getParamValue("old_telephone"));		//老客户电话
			String oldVehicleId = CommonUtils.checkNull(request.getParamValue("old_vehicle_id"));		//老客户车架号
			
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			//修改销售线索主表
			TPcLeadsPO oldLeadsPo = new TPcLeadsPO();
			oldLeadsPo.setLeadsCode(Long.parseLong(leadsCode));
			TPcLeadsPO newLeadsPo = new TPcLeadsPO();
			newLeadsPo.setLeadsCode(Long.parseLong(leadsCode));
			//线索来源判断
			if("60151011".equals(leadsOrigin)) {
				newLeadsPo.setLeadsOrigin("60151011");
				newLeadsPo.setComeDate(sdf.parse(comeDate));
				newLeadsPo.setLeaveDate(sdf.parse(comeDate3));
				newLeadsPo.setComeMeet(Long.parseLong(comeMeet));
				newLeadsPo.setJcWay(jcway);
				newLeadsPo.setTelephone(telephone);
			} else {
				newLeadsPo.setLeadsOrigin("60151012");
				newLeadsPo.setComeDate(sdf.parse(comeDate2));
				newLeadsPo.setTelephone(telephone);
			}
			newLeadsPo.setCustomerDescribe(describe);
			newLeadsPo.setIntentVehicle(intentVehicle);
			newLeadsPo.setUpdateDate(new Date());
			newLeadsPo.setUpdateBy(logonUser.getUserId());
			newLeadsPo.setCustomerName(customerName);
			newLeadsPo.setBuyBudget(buyBudget);
			newLeadsPo.setBuyType(buyType);
			newLeadsPo.setCustomerType(customerType);
			newLeadsPo.setOldCustomerName(oldCustomerName);
			newLeadsPo.setOldTelephone(oldTelephone);
			newLeadsPo.setOldVehicleId(oldVehicleId);
			//newLeadsPo.setIntentType(intentType);
			newLeadsPo.setTestDriving(testDriving);
			dao.update(oldLeadsPo, newLeadsPo);
			
			//修改销售线索分派表
			TPcLeadsAllotPO oldAllotPo = new TPcLeadsAllotPO();
			oldAllotPo.setLeadsAllotId(Long.parseLong(leadsAllotId));
			TPcLeadsAllotPO newAllotPo = new TPcLeadsAllotPO();
			newAllotPo.setLeadsAllotId(Long.parseLong(leadsAllotId));
			newAllotPo.setAdviser(adviser);
			newAllotPo.setTelephone(telephone);
			newAllotPo.setUpdateDate(new Date());
			newAllotPo.setUpdateBy(logonUser.getUserId());
			dao.update(oldAllotPo,newAllotPo);
			
			act.setForword(dcrcEnterUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "销售线索查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 顾问录入新增保存
	 */
	public void adviserEnterInsertSave() {
		DlrLeadsManageDao dao = new DlrLeadsManageDao();
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			//获取页面参数
			String leadsOrigin = CommonUtils.checkNull(request.getParamValue("leads_origin"));
//			String startDate = CommonUtils.checkNull(request.getParamValue("startDate"));
//			String startHHDate = CommonUtils.checkNull(request.getParamValue("startHHDate"));
//			String startMMDate = CommonUtils.checkNull(request.getParamValue("startMMDate"));
//			String startDate2 = CommonUtils.checkNull(request.getParamValue("startDate2"));
//			String startHHDate2 = CommonUtils.checkNull(request.getParamValue("startHHDate2"));
//			String startMMDate2 = CommonUtils.checkNull(request.getParamValue("startMMDate2"));
			String adviser = CommonUtils.checkNull(request.getParamValue("adviserId"));
			String describe = CommonUtils.checkNull(request.getParamValue("describe"));
			String userDealerId = CommonUtils.checkNull(logonUser.getDealerId());
			String customerName = CommonUtils.checkNull(request.getParamValue("customer_name"));
			String telephone = CommonUtils.checkNull(request.getParamValue("telephone"));
			String sex = CommonUtils.checkNull(request.getParamValue("sex"));
//			String comeDate = startDate + " "+startHHDate+":"+startMMDate+":00";
//			String comeDate2 = startDate2 + " "+startHHDate2+":"+startMMDate2+":00";
			String intentVehicle = CommonUtils.checkNull(request.getParamValue("intentVehicleB"));
			String comeMeet = CommonUtils.checkNull(request.getParamValue("come_meet"));
			String adviserId = logonUser.getUserId().toString();
			
			//9：00-17:00之间分配给经销商的公司类信息，主管当日19:00前完成分配，顾问必须在当日完成处理
			//17:00-次日9:00分配给经销商的公司类信息，主管次日11:00前完成分配，顾问必须在次日12:00前完成处理。
			Date d1 = new Date(); //当前时间
			String d2 = "090000";
			String d3 = "170000";
			String remindDate = null;
			SimpleDateFormat f = new SimpleDateFormat("hhmmss"); //格式化为 hhmmss
			int d1Number = Integer.parseInt(f.format(d1).toString()); //将第一个时间格式化后转为int
			int d2Number = Integer.parseInt(d2); //将第二个时间格式化后转为int
			int d3Number = Integer.parseInt(d3); //将第三个时间格式化后转为int
			
			SimpleDateFormat f1 = new SimpleDateFormat("yyyy-MM-dd");
			Calendar c = Calendar.getInstance();
			c.add(Calendar.DAY_OF_MONTH, 1);
								
			if(d1Number>=d2Number && d1Number<=d3Number){
				remindDate = f1.format(d1);
			} else {
				remindDate = f1.format(c.getTime());
			}
			
			//保存新增的DCRC录入
			//新增到销售线索表
			TPcLeadsPO tlkeyPo = new TPcLeadsPO();
			//获取销售线索主键
			Long LeadsPK = dao.getLongPK(tlkeyPo);
			TPcLeadsPO tlPo = new TPcLeadsPO();
			tlPo.setLeadsCode(LeadsPK);
			tlPo.setCustomerName(customerName);
			tlPo.setTelephone(telephone);
			tlPo.setLeadsType(Constant.LEADS_TYPE_03.toString());
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			tlPo.setLeadsOrigin(leadsOrigin);
			tlPo.setCollectDate(new Date());
			tlPo.setCreateDate(new Date());
			tlPo.setCreateBy(logonUser.getUserId().toString());
			tlPo.setSex(sex);
			tlPo.setLeadsStatus(new Long(Constant.LEADS_STATUS_01));
			tlPo.setCreateBy(logonUser.getUserId().toString());
			tlPo.setCustomerDescribe(describe);
			tlPo.setIntentVehicle(intentVehicle);
//			tlPo.setComeMeet(Long.parseLong(comeMeet));
			dao.insert(tlPo);
			
			//新增到销售线索分派表
			TPcLeadsAllotPO tplkeyPo = new TPcLeadsAllotPO();
			//获取销售线索分派主键
			Long LeadsAllotPK = dao.getLongPK(tplkeyPo);
			TPcLeadsAllotPO tplPo = new TPcLeadsAllotPO();
			tplPo.setLeadsAllotId(LeadsAllotPK);
			tplPo.setLeadsCode(LeadsPK);
			tplPo.setDealerId(userDealerId);
			tplPo.setAllotDealerDate(new Date());
			tplPo.setAdviser(adviserId);
			tplPo.setSex(sex);
			tplPo.setAllotAdviserDate(new Date());
			tplPo.setAllotAgain(Constant.IF_TYPE_NO);
			tplPo.setStatus(Constant.STATUS_ENABLE);
			dao.insert(tplPo);
			
			//新增提醒信息
			CommonUtils.addRemindInfo(Constant.REMIND_TYPE_04.toString(), LeadsAllotPK.toString(), "", userDealerId, adviserId, remindDate,"");
			
			act.setForword(adviserEnterUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "销售线索查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * DCRC离店确认
	 */
	public void dcrcLeaveDate() {
		DlrLeadsManageDao dao = new DlrLeadsManageDao();
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			//获取页面参数
			String leadsCode = CommonUtils.checkNull(request.getParamValue("leadsCode"));
			String leadsAllotId = CommonUtils.checkNull(request.getParamValue("leadsAllotId"));
			String adviser = CommonUtils.checkNull(request.getParamValue("adviserId"));
		
			String userDealerId = CommonUtils.checkNull(logonUser.getDealerId());
			
			//判断线索状态（如若已提交离店则抛出异常）
			TPcLeadsPO oPo = new TPcLeadsPO();
			oPo.setLeadsCode(Long.parseLong(leadsCode));
			List<PO> poxx = dao.select(oPo);
			oPo = (TPcLeadsPO)poxx.get(0);
			/*
			if(oPo.getLeaveDate() != null) {
				throw new Exception("该条数据已被处理过！");
			}*/
			
			//9：00-17:00之间分配给经销商的公司类信息，主管当日19:00前完成分配，顾问必须在当日完成处理
			//17:00-次日9:00分配给经销商的公司类信息，主管次日11:00前完成分配，顾问必须在次日12:00前完成处理。
			Date d1 = new Date(); //当前时间
			String d2 = "090000";
			String d3 = "170000";
			String remindDate = null;
			SimpleDateFormat f = new SimpleDateFormat("hhmmss"); //格式化为 hhmmss
			int d1Number = Integer.parseInt(f.format(d1).toString()); //将第一个时间格式化后转为int
			int d2Number = Integer.parseInt(d2); //将第二个时间格式化后转为int
			int d3Number = Integer.parseInt(d3); //将第三个时间格式化后转为int
			
			SimpleDateFormat f1 = new SimpleDateFormat("yyyy-MM-dd");
			Calendar c = Calendar.getInstance();
			c.add(Calendar.DAY_OF_MONTH, 1);
								
			if(d1Number>=d2Number && d1Number<=d3Number){
				remindDate = f1.format(d1);
			} else {
				remindDate = f1.format(c.getTime());
			}
			//修改销售线索主表信息（标记为有效,增加离店时间）
			TPcLeadsPO oldLeadsPo = new TPcLeadsPO();
			oldLeadsPo.setLeadsCode(Long.parseLong(leadsCode));
			TPcLeadsPO newLeadsPo = new TPcLeadsPO();
			//newLeadsPo.setLeadsCode(Long.parseLong(leadsCode));
			newLeadsPo.setLeadsStatus(Constant.LEADS_STATUS_01.longValue());
			if(oPo.getLeaveDate() == null) {
				newLeadsPo.setLeaveDate(new Date());
			}			
			dao.update(oldLeadsPo, newLeadsPo);
			//修改销售线索分派表（标记为有效）
			TPcLeadsAllotPO oldAllotPo = new TPcLeadsAllotPO();
			oldAllotPo.setLeadsAllotId(Long.parseLong(leadsAllotId));
			TPcLeadsAllotPO newAllotPo = new TPcLeadsAllotPO();
			//newAllotPo.setLeadsAllotId(Long.parseLong(leadsAllotId));
			newAllotPo.setStatus(Constant.STATUS_ENABLE);
			dao.update(oldAllotPo,newAllotPo);
			
			//新增提醒信息 有离店时间的数据不增加提醒信息
//			if(!ifLeaveTime(leadsAllotId)){
				CommonUtils.addRemindInfo(Constant.REMIND_TYPE_04.toString(), leadsAllotId, "", userDealerId, adviser, remindDate,"");
//			}
			act.setForword(dcrcEnterUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "销售线索查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	
	
	/**
	 * 初始使化导入页面
	 */
	public void leadImportInit() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			act.setForword(dlrLeadsManageImplortUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"未知错误!");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 销售线索导入模板下载
	 */
	public void olddownloadTemple(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		OutputStream os = null;
		try{
			ResponseWrapper response = act.getResponse();
			
			// 用于下载传参的集合
			List<List<Object>> list = new LinkedList<List<Object>>();
			
			//标题
			List<Object> listHead = new LinkedList<Object>();//导出模板第一列
			listHead.add("客户姓名");
			listHead.add("联系电话");
			listHead.add("客户所在省");
			listHead.add("客户所在市");
			listHead.add("客户所在区");
			listHead.add("线索来源");
			listHead.add("收集时间");
			listHead.add("经销商代码");
			
			list.add(listHead);
			
			// 导出的文件名
			String fileName = "销售线索导入模板.xls";
			// 导出的文字编码
			fileName = new String(fileName.getBytes("GB2312"), "ISO8859-1");
			response.setContentType("Application/text/xls");
			response.addHeader("Content-Disposition", "attachment;filename=" + fileName);
			
			os = response.getOutputStream();
//			CsvWriterUtil.writeCsv(list, os);
			CsvWriterUtil.createXlsFile(list, os);
			os.flush();			
		}catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.BATCH_IMPORT_FAILURE_CODE,"文件读取错误");
			logger.error(logonUser,e1);
			act.setException(e1);
		} finally {
			if (null != os) {
				try {
					os.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * 销售线索导入临时表
	 */
	public void leadsManagePlanExcelOperate(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		DlrLeadsManageDao dao=DlrLeadsManageDao.getInstance();
		
        try {
			RequestWrapper request = act.getRequest();
//			String year=CommonUtils.checkNull(request.getParamValue("year"));
//			String month=CommonUtils.checkNull(request.getParamValue("month"));
//			String areaId=CommonUtils.checkNull(request.getParamValue("buss_area"));
			TPcLeadsDealerPO po=new TPcLeadsDealerPO();
			po.setDealerId(logonUser.getDealerId());
			//清空临时表中的数据
			dao.delete(po);
			long maxSize=1024*1024*5;
			insertIntoTmp(request, "uploadFile",6,3,maxSize);
			List<ExcelErrors> el=getErrList();
			if(null!=el&&el.size()>0){
				act.setOutData("errorList", el);
				act.setForword(dlrLeadsManageFailureUrl);
			}else{
				List<Map> list=getMapList();
				//删除临时表中数据
				
				//将数据插入临时表
				insertTmpLeadsManage(list, logonUser.getUserId());
				//校验临时表数据
				List<ExcelErrors> errorList=checkData();
//				List errorList = null;
				if(null!=errorList){
					act.setOutData("errorList", errorList);
					act.setForword(dlrLeadsManageFailureUrl);
				}else{
					//获取临时表数据，并显示到界面
					List<Map<String, Object>> tmpList=dao.dlrSelectTmpLeadsManage(logonUser.getUserId());
					act.setOutData("leadsList", tmpList);
					act.setForword(dlrLeadsManageSuccessUrl);
				}
			}
			
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.BATCH_IMPORT_FAILURE_CODE,"文件读取错误");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}	
	
	/*
	 * 把所有导入记录插入临时表
	 */
	private void insertTmpLeadsManage(List<Map> list,Long userId) throws Exception{
		if(null==list){
			list=new ArrayList();
		}
		for(int i=0;i<list.size();i++){
			Map map=list.get(i);
			if(null==map){
				map=new HashMap<String, Cell[]>();
			}
			Set<String> keys=map.keySet();
			Iterator it=keys.iterator();
			String key="";
			
			while(it.hasNext()){
				key=(String)it.next();
				Cell[] cells=(Cell[])map.get(key);
				parseCells(key, cells, userId);
			}
		}
		
	}
	/*
	 * 每一行插入TPcLeadsTempPO（长安汽车）
	 */
	private void parseCells(String rowNum,Cell[] cells,Long userId) throws Exception{
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
			OemLeadsManageDao dao=new OemLeadsManageDao();
		    TPcLeadsDealerPO po=new TPcLeadsDealerPO();
			po.setRowNumber(rowNum.trim());
//			po.setOrgCode(subCell(cells[0].getContents().trim()));
			if(subCell(cells[0].getContents().trim())==null || "".equals(subCell(cells[0].getContents().trim()))) {
				po.setCustomerName(null);
			} else {
				po.setCustomerName(subCell(cells[0].getContents().trim()));
			}
			if(subCell(cells[1].getContents().trim())==null || "".equals(subCell(cells[1].getContents().trim()))) {
				po.setTelephone(null);
			} else {
				po.setTelephone(subCell(cells[1].getContents().trim()));
			}
//			po.setProvince(subCell(cells[2].getContents().trim()));
//			po.setCity(subCell(cells[3].getContents().trim()));
//			po.setArea(subCell(cells[4].getContents().trim()));
//			// 线索来源 
			int origin = 60151003;
			if(subCell(cells[2].getContents().trim())=="客户中心" || "客户中心".equals(subCell(cells[2].getContents().trim()))) {
				origin = 60151003;
			}else if(subCell(cells[2].getContents().trim())=="官网" || "官网".equals(subCell(cells[2].getContents().trim()))) {
				origin = 60151004;
			} else if(subCell(cells[2].getContents().trim())=="易车网" || "易车网".equals(subCell(cells[2].getContents().trim()))) {
				origin = 60151018;
			} else if(subCell(cells[2].getContents().trim())=="汽车之家" || "汽车之家".equals(subCell(cells[2].getContents().trim()))) {
				origin = 60151017;
			} else if(subCell(cells[2].getContents().trim())=="车展/巡展/路演" || "车展/巡展/路演".equals(subCell(cells[2].getContents().trim()))) {
				origin = 60151007;
			}else if(subCell(cells[2].getContents().trim())=="网络媒体" || "网络媒体".equals(subCell(cells[2].getContents().trim()))) {
				origin = 60151006;
			}else if(subCell(cells[2].getContents().trim())=="品牌体验活动（上市活动、试乘试驾、大篷车等）" || "品牌体验活动（上市活动、试乘试驾、大篷车等）".equals(subCell(cells[2].getContents().trim()))) {
				origin = 60151008;
			}else if(subCell(cells[2].getContents().trim())=="亲朋/老客户介绍及其他" || "亲朋/老客户介绍及其他".equals(subCell(cells[2].getContents().trim()))) {
				origin = 60151016;
			}else if(subCell(cells[2].getContents().trim())=="商圈定展" || "商圈定展".equals(subCell(cells[2].getContents().trim()))) {
				origin = 60151019;
			}else if(subCell(cells[2].getContents().trim())=="新媒体（移动端APP、移动网站等）" || "新媒体（移动端APP、移动网站等）".equals(subCell(cells[2].getContents().trim()))) {
				origin = 60151020;
			}else if(subCell(cells[2].getContents().trim())=="社会化媒体（微博、微信、论坛等）" || "社会化媒体（微博、微信、论坛等）".equals(subCell(cells[2].getContents().trim()))) {
				origin = 60151021;
			}else {
				origin = 60151003;
			}
			if(subCell(cells[2].getContents().trim())==null || "".equals(subCell(cells[2].getContents().trim()))) {
				po.setLeadsOrigin(null);
			} else {
				po.setLeadsOrigin(subCell(cells[2].getContents().trim()));
			}
//			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//			Date date = sdf.parse(subCell(cells[6].getContents().trim()));
			if (cells[3].getType() == CellType.DATE) {
				//DateCell dc = (DateCell) cells[6];
                Date date = ((DateCell) cells[3]).getDate();
                //SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String sDate = new SimpleDateFormat("yyyy-MM-dd").format(date);
                po.setCollectDate(subCell(sDate));
			} else {
				po.setCollectDate(subCell(cells[3].getContents().trim()));
			}
			if(subCell(cells[5].getContents().trim())==null || "".equals(subCell(cells[5].getContents().trim()))) {
				po.setCustomerDescribe(null);
			} else {
				po.setCustomerDescribe(subCell(cells[5].getContents().trim()));
			}
			po.setDealerId(logonUser.getDealerId());
			// 线索类别为经销商导入
			po.setLeadsType(Constant.LEADS_TYPE_04);
			po.setCreateDate(new Date());
			po.setCreateBy(userId.toString());
			if(subCell(cells[4].getContents().trim())==null || "".equals(subCell(cells[4].getContents().trim()))) {
				po.setAdviser(null);
			} else {
				po.setAdviser(subCell(cells[4].getContents().trim()));
			}
			//po.setAdviser(adviser)
			dao.insert(po);
	}	
	/*
	 * 将输入字符截取最多30位
	 */
	private String subCell(String orgAmt){
		String newAmt="";
		if(null==orgAmt||"".equals(orgAmt)){
			return newAmt;
		}
		if(orgAmt.length()>30){
			newAmt=orgAmt.substring(0,30);
		}else{
			newAmt=orgAmt;
		}
		return newAmt;
	}
	/*
	 * 校验临时表中数据是否符合导入标准
	 */
	@SuppressWarnings("rawtypes")
	private List<ExcelErrors> checkData(){
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		OemLeadsManageDao dao=new OemLeadsManageDao();
		TPcLeadsDealerPO tempPo=new TPcLeadsDealerPO();
		tempPo.setDealerId(logonUser.getDealerId());
		List<TPcLeadsDealerPO> list=dao.select(tempPo);
		if(null==list){
			list=new ArrayList();
		}
		
		ExcelErrors errors=null;
		TPcLeadsDealerPO po=null;
		StringBuffer errorInfo=new StringBuffer("");
		boolean isError=false;
		
		List<ExcelErrors> errorList = new ArrayList<ExcelErrors>();
		for(int i=0;i<list.size();i++){
			errors=new ExcelErrors();
			//取得TPcLeadsTempPO
			po=list.get(i);
			//取得行号
			String rowNum=po.getRowNumber();
			
			//数据校验条件的MAP
			
			Map<String, Object> conMap=new HashMap<String, Object>();
//			conMap.put("province", po.getProvince());
//			conMap.put("city", po.getCity());
//			conMap.put("area", po.getArea());
			conMap.put("dealerId", po.getDealerId());
			
			// 客户所在省
//			List<Map<String, Object>> provinceDlrList = null;
//			if(!"".equals(po.getProvince())&&po.getProvince()!=null) {
//				provinceDlrList=dao.oemLeadsCheckProvince(conMap);
//			}
			// 客户所在市
//			List<Map<String, Object>> cityDlrList = null;
//			if(!"".equals(po.getCity())&&po.getCity()!=null) {
//				cityDlrList=dao.oemLeadsCheckCity(conMap);
//			}
			// 客户所在区
//			List<Map<String, Object>> areaDlrList = null;
//			if(!"".equals(po.getArea())&&po.getArea()!=null) {
//				areaDlrList=dao.oemLeadsCheckArea(conMap);
//			}
			// 经销商编码
//			List<Map<String, Object>> dealerList = null;
//			if(!"".equals(po.getDealerId())&&po.getDealerId()!=null) {
//				dealerList=dao.oemLeadsCheckDealer(conMap);
//			}
			String collectDate = null;
			if(po.getCollectDate()==null) {
				collectDate = "2014-01-01";
			} else {
				collectDate = po.getCollectDate().toString();
			}
			//校验合计
			try {
				if(po.getAdviser()!=null&&!"".equals(po.getAdviser().toString())){
					boolean judgeFlag=judgeAcnt(po.getAdviser());
					if(!judgeFlag){
						isError=true;
						errorInfo.append("账号录入有误！！");
					}
				}
				if(!isValidDate(collectDate)){
					isError=true;
					errorInfo.append("收集时间格式不正确,");
				} else if (po.getCustomerName()==null|| "".equals(po.getCustomerName())){
					isError=true;
					errorInfo.append("客户姓名不能为空,");
				} else if (po.getTelephone().trim()=="null" || "null".equals(po.getTelephone().trim())) {
					isError=true;
					errorInfo.append("联系电话不能为空,");
				}
				//else if (!"客户中心".equals(po.getLeadsOrigin().trim())&&!"官网".equals(po.getLeadsOrigin().trim())&&!"网络媒体".equals(po.getLeadsOrigin().trim())&&!"车展".equals(po.getLeadsOrigin().trim())&&!"巡展".equals(po.getLeadsOrigin().trim())&&!"试乘试驾".equals(po.getLeadsOrigin().trim())&&!"上市活动".equals(po.getLeadsOrigin().trim())&&!"媒体".equals(po.getLeadsOrigin().trim())&&!"汽车之家".equals(po.getLeadsOrigin().trim())&&!"易车网".equals(po.getLeadsOrigin().trim())) {
				//	isError=true;
				//	errorInfo.append("线索来源格式不正确,");
				//}
//				if((po.getProvince()!=null&&!"".equals(po.getProvince()))&&provinceDlrList.size()==0){
//						isError=true;
//						errorInfo.append("所在省代码不正确");
//				}
//				if((po.getCity()!=null&&!"".equals(po.getCity()))&&cityDlrList.size()==0){
//						isError=true;
//						errorInfo.append("所在市代码不正确");
//				}
//				if((po.getArea()!=null&&!"".equals(po.getArea()))&&areaDlrList.size()==0){
//					isError=true;
//					errorInfo.append("所在区代码错误");
//				}
//				if((po.getDealerId()!=null&&!"".equals(po.getDealerId()))&&dealerList.size()==0){
//					isError=true;
//					errorInfo.append("经销商代码错误");
//				}
			} catch (Exception e) {
				e.printStackTrace();
				isError=true;
			}
			if(errorInfo.length()>0){
				String info=errorInfo.substring(0,errorInfo.length()-1);
				errors.setRowNum(new Integer(rowNum));
				errors.setErrorDesc(info);
				errorList.add(errors);
				errorInfo.delete(0, errorInfo.length());
			}
		}
		
		if(isError){
			return errorList;
		}else{
			return null;
		}
	}
	
	/**
	 * 导入业务表
	 */
	public void importExcel(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			DlrLeadsManageDao dao=DlrLeadsManageDao.getInstance();
			//导入数据到销售线索表和线索分派表
			dao.insertLeads(logonUser.getUserId());
			
			act.setForword(dlrLeadsManageCompleteUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/** 
	 * @param date yyyy-MM-dd HH:mm:ss 
	 * @return 
	 */  
	public static boolean isValidDate(String date) {  
	    try {
	    	//date 有可能是 yyyy/mm/dd 的格式，先把这种格式转换为 yyyy-mm-dd 的格式
	    	String newdate = date;
	    	if (newdate.indexOf("/") != -1) {
	    		newdate = new SimpleDateFormat("yyyy-MM-dd").format(new SimpleDateFormat("yyyy/MM/dd").parse(date));
	    	}
	    	
	        int year = Integer.parseInt(newdate.substring(0, 4));  
	        if (year <= 0)  
	            return false;  
	        int month = Integer.parseInt(newdate.substring(5, 7));  
	        if (month <= 0 || month > 12)  
	            return false;  
	        int day = Integer.parseInt(newdate.substring(8, 10));  
	        if (day <= 0 || day > DAYS[month])  
	            return false;  
	        if (month == 2 && day == 29 && !isGregorianLeapYear(year)) {  
	            return false;  
	        }  
//	        int hour = Integer.parseInt(date.substring(11, 13));  
//	        if (hour < 0 || hour > 23)  
//	            return false;  
//	        int minute = Integer.parseInt(date.substring(14, 16));  
//	        if (minute < 0 || minute > 59)  
//	            return false;  
//	        int second = Integer.parseInt(date.substring(17, 19));  
//	        if (second < 0 || second > 59)  
//	            return false;  
	  
	    } catch (Exception e) {  
	        e.printStackTrace();  
	        return false;  
	    }  
	    return true;  
	}  
	static int[] DAYS = { 0, 31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };  
	public static final boolean isGregorianLeapYear(int year) {  
	    return year % 4 == 0 && (year % 100 != 0 || year % 400 == 0);  
	}  
	

	
	/**
	 * 经销商模板导出
	 */
	public void downloadTemple(){
		ActionContext act = ActionContext.getContext();
		
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		
		try {
			ResponseWrapper response=act.getResponse();
			
			//Properties props = new Properties();
			
			//props.load(ServiceActivityVinImport.class.getClassLoader().getResourceAsStream("FileStore.properties"));
    		
			
			
			String dlrLeadsData = "dlrImportData";//KPI及基础数据定义
			
		
			 
			//String fileTitle = CommonUtils.checkNull(request.getParamValue("fileTitle"));
			
			String old="";
			old = dlrLeadsData+".xls";
			String filePath= FILEURL+old;
 
			
			String fileName="";
			
			openExls(response, fileName, filePath, old);
			
		} catch (Exception e) 
		{
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"");
			
			logger.error(logonUser,e1);
			
			act.setException(e1);
		}
	}
	
	
	public static boolean openExls(ResponseWrapper response,String fileName,String filePath,String old) throws Exception {
		
		boolean flag = false;
		
		old=new String(old.getBytes(),"UTF-8");
		
		String pathStr="";
		
		if("".equals(fileName)&&filePath.indexOf(".")!=-1)
		{
			pathStr=filePath;
		}else
		{
			pathStr=filePath+"\\"+fileName;
		}
		
		BufferedInputStream bis = null;
		
		OutputStream os = null;
		
		try {
			
			URL url = new URL(pathStr);
			
			InputStream ism=url.openStream();
			
			bis = new BufferedInputStream(ism);
			
			byte[] buffer = new byte[2048];
			
			response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
			
			response.addHeader("Content-disposition", "attachment;filename="+ URLEncoder.encode(old, "UTF-8"));
			
			os = response.getOutputStream();
			
			Thread.sleep(500);
			
			int len = 0;
			
			while((len = bis.read(buffer))!=-1 )
			{
				os.write(buffer,0,len);
				
				os.flush();
			}
			 
			bis.close();
			
			os.close();
			
			flag = true;
		} catch (Exception e) 
		{
			e.printStackTrace();
			throw e;
		} finally 
		{
			if (null != bis) 
			{
				bis.close();
			} 
			if (null != os) 
			{
				os.close();
			}
		}
		return flag;
	}

	/**
	 * 校验账号
	 */
	public boolean judgeAcnt(String acnt){
		ActionContext act = ActionContext.getContext();
		boolean flag=false;
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		String companyId=logonUser.getCompanyId().toString();
		try {
			DlrLeadsManageDao dao = new DlrLeadsManageDao();
			StringBuilder sql= new StringBuilder();
			sql.append("select count(1) counts\n" );
			sql.append("  from tc_user tu\n" );
			sql.append(" where tu.acnt = '"+acnt+"'\n" );
			sql.append("   and tu.company_id = '"+companyId+"'\n" );
			sql.append("   and tu.pose_rank = '60281004'");
			List<Map<String,Object>> countList=dao.pageQuery(sql.toString(), null, dao.getFunName());
			int count=Integer.parseInt(countList.get(0).get("COUNTS").toString());
			if(count>0){
				flag=true;
			}
			
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"");
			logger.error(logonUser,e1);
			act.setException(e1);
			return flag;
		}
		return flag;
	}
	

	//判断当前的数据离店时间是否有值
	public boolean ifLeaveTime(String allot_id){
		boolean flag=false;
		StringBuilder sql= new StringBuilder();
		sql.append("select count(1) counts\n" );
		sql.append("  from t_pc_leads_allot tpla, t_pc_leads tpl\n" );
		sql.append(" where tpla.leads_allot_id = '"+allot_id+"'\n" );
		sql.append("   and tpl.leads_code = tpla.leads_code\n" );
		sql.append("   and tpl.leave_date is not null");
		DlrLeadsManageDao dao = new DlrLeadsManageDao();
		List<Map<String,Object>> countList=dao.pageQuery(sql.toString(), null, dao.getFunName());
		int count=Integer.parseInt(countList.get(0).get("COUNTS").toString());
		if(count>0){
			flag=true;
		}
		return flag;
	}
	
	/**
	 * 顾问导入客流模板下载
	 * */
	public void downloadCusTemple(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		OutputStream os = null;
		try{
			ResponseWrapper response = act.getResponse();
			List<List<Object>> list = new LinkedList<List<Object>>();
			//标题
			List<Object> listHead = new LinkedList<Object>();//导出模板第一列
			listHead.add("客户姓名");
			listHead.add("联系电话");
			listHead.add("客户性别");
			listHead.add("意向车系");
			listHead.add("意向车型");
			listHead.add("线索来源");
			listHead.add("客户描述");
			list.add(listHead);
			// 导出的文件名
			String fileName = "顾问导入客流模板.xls";
			// 导出的文字编码
			fileName = new String(fileName.getBytes("GB2312"), "ISO8859-1");
			response.setContentType("Application/text/xls");
			response.addHeader("Content-Disposition", "attachment;filename=" + fileName);
			os = response.getOutputStream();
			createCusXlsFile(list, os);
			os.flush();			
		}catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.BATCH_IMPORT_FAILURE_CODE,"文件读取错误");
			logger.error(logonUser,e1);
			act.setException(e1);
		} finally {
			if (null != os) {
				try {
					os.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * 导出模板公共方法
	 * */
	public static void createCusXlsFile(List<List<Object>> content,OutputStream os){
		DlrLeadsManageDao dao=DlrLeadsManageDao.getInstance();
		TaskManageDao dao2 = new TaskManageDao();
		try {
			WritableWorkbook workbook = Workbook.createWorkbook(os);
			WritableSheet sheet = workbook.createSheet("下载模板", 0);
			WritableFont wf_title = new WritableFont(WritableFont.ARIAL, 11,  WritableFont.NO_BOLD, false, UnderlineStyle.NO_UNDERLINE, jxl.format.Colour.RED);
			WritableCellFormat wcf_title = new WritableCellFormat(wf_title); // 单元格定义  
            wcf_title.setBackground(jxl.format.Colour.WHITE); // 设置单元格的背景颜色  
            wcf_title.setAlignment(jxl.format.Alignment.LEFT); // 设置对齐方式  
            wcf_title.setVerticalAlignment(jxl.format.VerticalAlignment.TOP);
            wcf_title.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.THIN,jxl.format.Colour.GRAY_25); 
            wcf_title.setWrap(true);
            //查询线索来源
            List<Map<String,Object>> sourceList = dao.getSourceList();
            String sc = "";
            for (int i=0;i<sourceList.size();i++) {
            	// sc = sc + sourceList.get(i).get("CODE_ID").toString()+"("+sourceList.get(i).get("CODE_DESC").toString()+")"+",";
            	sc = sc + sourceList.get(i).get("CODE_DESC").toString()+",";
            }
           //获取意向车型一级列表
			List<DynaBean> menusAList = dao2.getIntentVehicleA();
			String sr="";
			for (int j=0;j<menusAList.size();j++) {
				//sr = sr+menusAList.get(j).get("SERIES_CODE")+"("+menusAList.get(j).get("NAME")+")"+",";
				sr = sr+menusAList.get(j).get("NAME")+",";
			}
			//获取意向车型二级列表
			List<DynaBean> menusBList = dao2.getIntentVehicleB();
			String sm="";
			for (int k=0;k<menusBList.size();k++) {
				//sm = sm+menusBList.get(k).get("SERIES_CODE")+"("+menusBList.get(k).get("NAME")+")"+",";
				sm = sm+menusBList.get(k).get("NAME")+",";
			}
            StringBuilder titleName = new StringBuilder();
            titleName.append("请填写下列字段范围内的数据： \n");
            titleName.append("   1、性别：男,女 \n");
            titleName.append("   2、线索来源："+sc.substring(0, sc.length()-1)+" \n");
            titleName.append("   3、意向车系："+sr.substring(0, sr.length()-1)+" \n");
            titleName.append("   4、意向车型："+sm.substring(0, sm.length()-1)+" \n");
			Label title=new Label(0,0,titleName.toString(),wcf_title);  
			sheet.mergeCells(0, 0, 50, 0);   
			sheet.setRowView(0, 1600, false); 
			sheet.addCell(title);   
			for(int i=0;i<content.size();i++){
				for(int j = 0;j<content.get(i).size();j++){
					// 添加单元格
					sheet.addCell(new Label(j,i+1,(content.get(i).get(j)).toString()));
				}
			}
			workbook.write();
			workbook.close();
		} catch (RowsExceededException e) {
			e.printStackTrace();
		}catch (WriteException e) {
			e.printStackTrace();
		}catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 顾问导入界面
	 */
	public void adviserEnterImportPage() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			act.setForword(CUS_IMPORT_PAGE_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"未知错误!");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 导入客流数据
	 */
	public void adviserCusImportFlow(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		DlrLeadsManageDao dao=DlrLeadsManageDao.getInstance();
		List<Map<String,Object>> errorList = new ArrayList<Map<String,Object>>();
		List<TPcLeadsPO> tpList = new ArrayList<TPcLeadsPO>();
		List<TPcLeadsAllotPO> tapList = new ArrayList<TPcLeadsAllotPO>();
		try {
			FileObject uploadFile = request.getParamObject("uploadFile");//获取导入文件
			String fileName = uploadFile.getFileName();//获取文件名
			fileName = fileName.substring(fileName.lastIndexOf("\\") + 1, fileName.length());//截取文件名
			ByteArrayInputStream is = new ByteArrayInputStream(uploadFile.getContent());//获取文件数据
			Workbook wb = Workbook.getWorkbook(is);
			Sheet sheet = wb.getSheet(0);
			int rowLength = sheet.getRows();
			//存放错误信息
			Map<String,Object> errorMap = null;
			for (int i=2;i<rowLength;i++) {
				TPcLeadsPO tp = new TPcLeadsPO();//线索表
				Long tpId = dao.getLongPK(tp);
				tp.setLeadsCode(tpId);
				tp.setLeadsType(Constant.LEADS_TYPE_03+"");
				tp.setCollectDate(new Date());
				tp.setCreateBy(logonUser.getUserId()+"");
				tp.setCreateDate(new Date());
				tp.setLeadsStatus(Constant.LEADS_STATUS_01.longValue());
				tp.setCustomerType(Constant.CTM_PROP_01+"");
				TPcLeadsAllotPO tap = new TPcLeadsAllotPO(); // 线索分派表
				tap.setLeadsAllotId(dao.getLongPK(tap));
				tap.setLeadsCode(tpId);
				tap.setStatus(Constant.STATUS_ENABLE);
				tap.setDealerId(logonUser.getDealerId());
				tap.setCreateBy(logonUser.getUserId()+"");
				tap.setCreateDate(new Date());
				tap.setAdviser(logonUser.getUserId()+"");
				tap.setAllotDealerDate(new Date());
				tap.setAllotAdviserDate(new Date());
				tap.setAllotAgain(Constant.PART_BASE_FLAG_NO);
				tap.setIfConfirm(Constant.ADVISER_CONFIRM_01);
				tap.setIsRepeat(Constant.IF_TYPE_NO.longValue());
				//读取单元格的值
				Cell[] cells = sheet.getRow(i);
				String cusName = cells[0].getContents().trim();// 客户姓名
				if (CommonUtils.isEmpty(cusName)) {
					errorMap = new HashMap<String,Object>();
					errorMap.put("rowNum",i+1);
					errorMap.put("errorMsg", "客户姓名不能为空");
					errorList.add(errorMap);
				}
				tp.setCustomerName(cusName);
				tap.setCustomerName(cusName);
				String telpone = cells[1].getContents().trim();// 客户电话
				tp.setTelephone(telpone);
				tap.setTelephone(telpone);
				String gendar = cells[2].getContents().trim();// 客户性别
				if (CommonUtils.isEmpty(gendar)) {
					tp.setSex(Constant.MAN+"");
					tap.setSex(Constant.MAN+"");
				} else {
					//根据性别去查询编码
					tp.setSex(dao.getSetMsg(gendar));
					tap.setSex(dao.getSetMsg(gendar));
				}
				String seriesName = cells[3].getContents().trim();// 意向车系
				if (CommonUtils.isEmpty(seriesName)) {
					errorMap = new HashMap<String,Object>();
					errorMap.put("rowNum",i+1);
					errorMap.put("errorMsg", "意向车系不能为空");
					errorList.add(errorMap);
				}
				//查询车系是否存在
				String ishasSeries = dao.getSeriesIshas(seriesName);
				if (CommonUtils.isEmpty(ishasSeries)) {
					errorMap = new HashMap<String,Object>();
					errorMap.put("rowNum",i+1);
					errorMap.put("errorMsg", "意向车系在库中不存在");
					errorList.add(errorMap);
				} else {
					tp.setIntentCar(ishasSeries);
				}
				String modelName = cells[4].getContents().trim();// 意向车型
				if (CommonUtils.isEmpty(modelName)) {
					errorMap = new HashMap<String,Object>();
					errorMap.put("rowNum",i+1);
					errorMap.put("errorMsg", "意向车型不能为空");
					errorList.add(errorMap);
				}
				String isHasModel = dao.getModelIshas(modelName);
				if (CommonUtils.isEmpty(isHasModel)) {
					errorMap = new HashMap<String,Object>();
					errorMap.put("rowNum",i+1);
					errorMap.put("errorMsg", "意向车型在库中不存在");
					errorList.add(errorMap);
				} else {
					tp.setIntentVehicle(isHasModel);
				}
				String sourceClue = cells[5].getContents().trim();// 线索来源
				if (CommonUtils.isEmpty(sourceClue)) {
					errorMap = new HashMap<String,Object>();
					errorMap.put("rowNum",i+1);
					errorMap.put("errorMsg", "线索来源不能为空");
					errorList.add(errorMap);
				}
				//查询线索在库中是否存在
				String isHasSourceClue = dao.getSourceIshas(sourceClue);
				if (CommonUtils.isEmpty(isHasSourceClue)) {
					errorMap = new HashMap<String,Object>();
					errorMap.put("rowNum",i+1);
					errorMap.put("errorMsg", "线索来源在库中不存在");
					errorList.add(errorMap);
				} else {
					tp.setLeadsOrigin(isHasSourceClue);
				}
				String cusRemark = cells[6].getContents().trim();// 客户描述
				tp.setCustomerDescribe(cusRemark);
				//添加到类里面去
				tpList.add(tp);
				tapList.add(tap);
			//循环结束
			}
			//如果没有错误，就直接插入到数据库
			if (errorList.size()==0 && tpList.size()>0) {
				dao.insert(tpList);
				dao.insert(tapList);
			}
			act.setOutData("errorList", errorList);
			act.setForword(CUS_IMPORT_SUCCESS_PAGE_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}

}