package com.infodms.dms.actions.sales.customerInfoManage;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.dao.sales.customerInfoManage.SalesDailyReportDAO;
import com.infodms.dms.dao.sales.dealer.DealerInfoDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TtDailyReportPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.ResponseWrapper;
import com.infoservice.po3.bean.PageResult;

/**
 * 2013-02-04
 * 
 * @author zhulei
 * 
 */

public class SalesDailyReport {
	public Logger logger = Logger.getLogger(SalesDailyReport.class);
	private ActionContext act = ActionContext.getContext();
	private static final SalesDailyReportDAO dao = SalesDailyReportDAO
			.getInstance();
	ResponseWrapper response = act.getResponse();
	private final String salesDailyReportInitUrl = "/jsp/sales/customerInfoManage/dailySearch.jsp";
	private final String addDailyReportInitUrl = "/jsp/sales/customerInfoManage/dayReportAdd.jsp";
	private final String dailyQuery = "/jsp/sales/customerInfoManage/dailyReportQuery.jsp";
	private final String dailyUpdate = "/jsp/sales/customerInfoManage/dailyReportUpdate.jsp";
	AclUserBean logonUser = (AclUserBean) act.getSession().get(
			Constant.LOGON_USER);

	/**
	 * 日报表初始化
	 */
	public void DailyReportInit() {
		AclUserBean logonUser = null;
		try {
				logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
				logonUser.getDutyType();
			 	Calendar calendar = Calendar.getInstance(); //昨天的日期
			   // calendar.add(Calendar.DATE,-1);
	 			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
				String sd  = dateFormat.format(calendar.getTime());
				act.setOutData("dutyType", logonUser.getDutyType());
				act.setOutData("orgId", logonUser.getOrgId());
				act.setOutData("logonUser", logonUser);
	  			act.setOutData("date", sd);
	  			String dealerIds=logonUser.getDealerId();
				List<Map<String, Object>> dealerList = DealerInfoDao.getInstance().getDealerInfo(dealerIds) ;
				act.setOutData("dealerList", dealerList);
				act.setForword(salesDailyReportInitUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "日报表初始化");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	
	/**
	 * 显示添加界面
	 * 
	 */
	public void addDailyReportInit() {
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			String dealerIds=logonUser.getDealerId();
			List<Map<String, Object>> dealerList = DealerInfoDao.getInstance().getDealerInfo(dealerIds) ;
			act.setOutData("dealerList", dealerList);
			act.setOutData("carType", Constant.carType);
			act.setOutData("seriesCount", Constant.carType.length);
			act.setOutData("cols", Constant.carType.length+2-5);
			act.setForword(addDailyReportInitUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.DISABLED_FAILURE_CODE,"新增日报表初始化");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 
	 * 插入
	 */
	public void DailyReportInsert() {
		try {
			TtDailyReportPO tdrp1 = new TtDailyReportPO();
			TtDailyReportPO tdrpTotal=new TtDailyReportPO();
			String dlrId = logonUser.getDealerId();
			String contentId=SequenceManager.getSequence("");
			for (int i=0;i<Constant.carType.length;i++){
					String carType = act.getRequest().getParamValue("carType"+i);
					String firstPassenger1 =CommonUtils.checkNull(act.getRequest()
								.getParamValue("FirstPassenger"+(i)));
					String invitePassenger1 = CommonUtils.checkNull(act.getRequest()
							.getParamValue("InvitePassenger"+(i)));
					String callPassenger1 = CommonUtils.checkNull(act.getRequest()
							.getParamValue("CallPassenger"+(i)));
					String testDrive1 = CommonUtils.checkNull(act.getRequest()
							.getParamValue("TestDrive"+(i)));
					String delivery1 = CommonUtils.checkNull(act.getRequest().getParamValue(
							"Delivery"+(i)));
					String largerDelivery1 = CommonUtils.checkNull(act.getRequest()
							.getParamValue("LargerDelivery"+(i)));
					String secondDelivery1 = CommonUtils.checkNull(act.getRequest()
							.getParamValue("SecondDelivery"+(i)));
					String o_LevelOrder1 = CommonUtils.checkNull(act.getRequest()
							.getParamValue("O_levelOrder"+(i)));
					String h_LevelOrder1 = CommonUtils.checkNull(act.getRequest()
							.getParamValue("H_levelOrder"+(i)));
					String a_LevelOrder1 = CommonUtils.checkNull(act.getRequest()
							.getParamValue("A_levelOrder"+(i)));
					String b_LevelOrder1 = CommonUtils.checkNull(act.getRequest()
							.getParamValue("B_levelOrder"+(i)));
					String c_LevelOrder1 = CommonUtils.checkNull(act.getRequest()
							.getParamValue("C_levelOrder"+(i)));
					String createCard1 = CommonUtils.checkNull(act.getRequest()
							.getParamValue("CreateCard"+(i)));
					String lost1 = CommonUtils.checkNull(act.getRequest().getParamValue(
							"Lost"+(i)));
					String realStock1 = CommonUtils.checkNull(act.getRequest()
							.getParamValue("RealStock"+(i)));
					String regularRecommend1 = CommonUtils.checkNull(act.getRequest()
							.getParamValue("RegularRecommend"+(i)));
					String oldCarReplace1 = CommonUtils.checkNull(act.getRequest()
							.getParamValue("OldCarReplace"+(i)));
					String h_Retain1 = CommonUtils.checkNull(act.getRequest().getParamValue(
							"H_Retain"+(i)));
					String a_Retain1 = CommonUtils.checkNull(act.getRequest().getParamValue(
							"A_Retain"+(i)));
					String b_Retain1 = CommonUtils.checkNull(act.getRequest().getParamValue(
							"B_Retain"+(i)));
					String c_Retain1 = CommonUtils.checkNull(act.getRequest().getParamValue(
							"C_Retain"+(i)));
					String unCommitOrder1 = CommonUtils.checkNull(act.getRequest()
							.getParamValue("UnCommitOrder"+(i)));
					tdrp1.setDailyReportId(Long.parseLong(SequenceManager.getSequence("")));
					tdrp1.setCarType(carType);
					tdrp1.setDlrId(Long.parseLong(dlrId));
					if(firstPassenger1==null||firstPassenger1.equals("")){
						tdrp1.setFirstPassenger(BigDecimal.valueOf(Long.parseLong("0")));
					}else{
						tdrp1.setFirstPassenger(BigDecimal.valueOf(Long.parseLong(firstPassenger1)));
					}
					if(invitePassenger1==null||invitePassenger1.equals("")){
					tdrp1.setInvitePassenger(BigDecimal.valueOf(Long.parseLong("0")));
					}else{
						tdrp1.setInvitePassenger(BigDecimal.valueOf(Long.parseLong(invitePassenger1)));
					}
					if(callPassenger1==null||callPassenger1.equals("")){
					tdrp1.setCallPassenger(BigDecimal.valueOf(Long.parseLong("0")));
					}else{
						tdrp1.setCallPassenger(BigDecimal.valueOf(Long.parseLong(callPassenger1)));
					}
					if(testDrive1==null||testDrive1.equals("")){
					tdrp1.setTestDriver(BigDecimal.valueOf(Long.parseLong("0")));
					}else{
						tdrp1.setTestDriver(BigDecimal.valueOf(Long.parseLong(testDrive1)));
					}
					if(delivery1==null||delivery1.equals("")){
					tdrp1.setDelivery(BigDecimal.valueOf(Long.parseLong("0")));
					}else{
						tdrp1.setDelivery(BigDecimal.valueOf(Long.parseLong(delivery1)));
					}
					if(largerDelivery1==null||largerDelivery1.equals("")){
					tdrp1.setLargerDelivery(BigDecimal.valueOf(Long.parseLong("0")));
					}else{
						tdrp1.setLargerDelivery(BigDecimal.valueOf(Long.parseLong(largerDelivery1)));
					}
					if(secondDelivery1==null||secondDelivery1.equals("")){
					tdrp1.setSecondDelivery(BigDecimal.valueOf(Long.parseLong("0")));
					}else{
						tdrp1.setSecondDelivery(BigDecimal.valueOf(Long.parseLong(secondDelivery1)));
					}
					if(o_LevelOrder1==null||o_LevelOrder1.equals("")){
					tdrp1.setOLevelOrder(BigDecimal.valueOf(Long.parseLong("0")));
					}else{
						tdrp1.setOLevelOrder(BigDecimal.valueOf(Long.parseLong(o_LevelOrder1)));
					}
					if(h_LevelOrder1==null||h_LevelOrder1.equals("")){
					tdrp1.setHLevelOrder(BigDecimal.valueOf(Long.parseLong("0")));
					}else{
						tdrp1.setHLevelOrder(BigDecimal.valueOf(Long.parseLong(h_LevelOrder1)));
					}
					if(a_LevelOrder1==null||a_LevelOrder1.equals("")){
					tdrp1.setALevelOrder(BigDecimal.valueOf(Long.parseLong("0")));
					}else{
						tdrp1.setALevelOrder(BigDecimal.valueOf(Long.parseLong(a_LevelOrder1)));
					}
					if(b_LevelOrder1==null||b_LevelOrder1.equals("")){
					tdrp1.setBLevelOrder(BigDecimal.valueOf(Long.parseLong("0")));
					}else{
						tdrp1.setBLevelOrder(BigDecimal.valueOf(Long.parseLong(b_LevelOrder1)));
					}
					if(c_LevelOrder1==null||c_LevelOrder1.equals("")){
					tdrp1.setCLevelOrder(BigDecimal.valueOf(Long.parseLong("0")));
					}else{
						tdrp1.setCLevelOrder(BigDecimal.valueOf(Long.parseLong(c_LevelOrder1)));
					}
					if(createCard1==null||createCard1.equals("")){
					tdrp1.setCreateCard(BigDecimal.valueOf(Long.parseLong("0")));
					}else{
						tdrp1.setCreateCard(BigDecimal.valueOf(Long.parseLong(createCard1)));
					}
					if(lost1==null||lost1.equals("")){
					tdrp1.setLost(BigDecimal.valueOf(Long.parseLong("0")));
					}else{
						tdrp1.setLost(BigDecimal.valueOf(Long.parseLong(lost1)));
					}
					if(realStock1==null||realStock1.equals("")){
					tdrp1.setRealStock(BigDecimal.valueOf(Long.parseLong("0")));
					}else{
						tdrp1.setRealStock(BigDecimal.valueOf(Long.parseLong(realStock1)));
					}
					if(regularRecommend1==null||regularRecommend1.equals("")){
					tdrp1.setRegularRecommend(BigDecimal.valueOf(Long.parseLong("0")));
					}else{
						tdrp1.setRegularRecommend(BigDecimal.valueOf(Long.parseLong(regularRecommend1)));
					}
					if(oldCarReplace1==null||oldCarReplace1.equals("")){
					tdrp1.setOldCarReplace(BigDecimal.valueOf(Long.parseLong("0")));
					}else{
						tdrp1.setOldCarReplace(BigDecimal.valueOf(Long.parseLong(oldCarReplace1)));
					}
					if(h_Retain1==null||h_Retain1.equals("")){
					tdrp1.setHRetain(BigDecimal.valueOf(Long.parseLong("0")));
					}else{
						tdrp1.setHRetain(BigDecimal.valueOf(Long.parseLong(h_Retain1)));
					}
					if(a_Retain1==null||a_Retain1.equals("")){
					tdrp1.setARetain(BigDecimal.valueOf(Long.parseLong("0")));
					}else{
						tdrp1.setARetain(BigDecimal.valueOf(Long.parseLong(a_Retain1)));
					}
					if(b_Retain1==null||b_Retain1.equals("")){
					tdrp1.setBRetain(BigDecimal.valueOf(Long.parseLong("0")));
					}else{
						tdrp1.setBRetain(BigDecimal.valueOf(Long.parseLong(b_Retain1)));
					}
					if(c_Retain1==null||c_Retain1.equals("")){
					tdrp1.setCRetain(BigDecimal.valueOf(Long.parseLong("0")));
					}else{
						tdrp1.setCRetain(BigDecimal.valueOf(Long.parseLong(c_Retain1)));
					}
					if(unCommitOrder1==null||unCommitOrder1.equals("")){
					tdrp1.setUncommitOrder(BigDecimal.valueOf(Long.parseLong("0")));
					}else{
						tdrp1.setUncommitOrder(BigDecimal.valueOf(Long.parseLong(unCommitOrder1)));
					}
						Date currentTime = new Date();
						SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
						String S_createDate = formatter.format(currentTime);
						Date createDate=formatter.parse(S_createDate);
					tdrp1.setCreateDate(createDate);
					tdrp1.setContentId(Long.parseLong(contentId));
					tdrp1.setStatus(Constant.DAILY_STATUS_UNCONFIRM);
					tdrp1.setCreateBy(logonUser.getUserId());
					dao.insert(tdrp1);
			
			}
			String total = act.getRequest().getParamValue("total");
			if (total.equals("小计")) {
				String firstPassengerTotal =CommonUtils.checkNull(act.getRequest()
							.getParamValue("FirstPassengerTotal"));
				String invitePassengerTotal = CommonUtils.checkNull(act.getRequest()
						.getParamValue("InvitePassengerTotal"));
				String callPassengerTotal = CommonUtils.checkNull(act.getRequest()
						.getParamValue("CallPassengerTotal"));
				String testDriveTotal = CommonUtils.checkNull(act.getRequest()
						.getParamValue("TestDriveTotal"));
				String deliveryTotal = CommonUtils.checkNull(act.getRequest().getParamValue(
						"DeliveryTotal"));
				String largerDeliveryTotal = CommonUtils.checkNull(act.getRequest()
						.getParamValue("LargerDeliveryTotal"));
				String secondDeliveryTotal = CommonUtils.checkNull(act.getRequest()
						.getParamValue("SecondDeliveryTotal"));
				String o_LevelOrderTotal = CommonUtils.checkNull(act.getRequest()
						.getParamValue("O_levelOrderTotal"));
				String h_LevelOrderTotal = CommonUtils.checkNull(act.getRequest()
						.getParamValue("H_levelOrderTotal"));
				String a_LevelOrderTotal = CommonUtils.checkNull(act.getRequest()
						.getParamValue("A_levelOrderTotal"));
				String b_LevelOrderTotal = CommonUtils.checkNull(act.getRequest()
						.getParamValue("B_levelOrderTotal"));
				String c_LevelOrderTotal = CommonUtils.checkNull(act.getRequest()
						.getParamValue("C_levelOrderTotal"));
				String createCardTotal = CommonUtils.checkNull(act.getRequest()
						.getParamValue("CreateCardTotal"));
				String lostTotal = CommonUtils.checkNull(act.getRequest().getParamValue(
						"LostTotal"));
				String realStockTotal = CommonUtils.checkNull(act.getRequest()
						.getParamValue("RealStockTotal"));
				String regularRecommendTotal = CommonUtils.checkNull(act.getRequest()
						.getParamValue("RegularRecommendTotal"));
				String oldCarReplaceTotal = CommonUtils.checkNull(act.getRequest()
						.getParamValue("OldCarReplaceTotal"));
				String h_RetainTotal = CommonUtils.checkNull(act.getRequest().getParamValue(
						"H_RetainTotal"));
				String a_RetainTotal = CommonUtils.checkNull(act.getRequest().getParamValue(
						"A_RetainTotal"));
				String b_RetainTotal = CommonUtils.checkNull(act.getRequest().getParamValue(
						"B_RetainTotal"));
				String c_RetainTotal = CommonUtils.checkNull(act.getRequest().getParamValue(
						"C_RetainTotal"));
				String unCommitOrderTotal = CommonUtils.checkNull(act.getRequest()
						.getParamValue("UnCommitOrderTotal"));
				tdrpTotal.setDailyReportId(Long.parseLong(SequenceManager.getSequence("")));
				tdrpTotal.setCarType(total);
				tdrpTotal.setDlrId(Long.parseLong(dlrId));
				if(firstPassengerTotal==null||firstPassengerTotal.equals("")){
					tdrpTotal.setFirstPassenger(BigDecimal.valueOf(Long.parseLong("0")));
				}else{
					tdrpTotal.setFirstPassenger(BigDecimal.valueOf(Long.parseLong(firstPassengerTotal)));
				}
				if(invitePassengerTotal==null||invitePassengerTotal.equals("")){
				tdrpTotal.setInvitePassenger(BigDecimal.valueOf(Long.parseLong("0")));
				}else{
					tdrpTotal.setInvitePassenger(BigDecimal.valueOf(Long.parseLong(invitePassengerTotal)));
				}
				if(callPassengerTotal==null||callPassengerTotal.equals("")){
				tdrpTotal.setCallPassenger(BigDecimal.valueOf(Long.parseLong("0")));
				}else{
					tdrpTotal.setCallPassenger(BigDecimal.valueOf(Long.parseLong(callPassengerTotal)));
				}
				if(testDriveTotal==null||testDriveTotal.equals("")){
				tdrpTotal.setTestDriver(BigDecimal.valueOf(Long.parseLong("0")));
				}else{
					tdrpTotal.setTestDriver(BigDecimal.valueOf(Long.parseLong(testDriveTotal)));
				}
				if(deliveryTotal==null||deliveryTotal.equals("")){
				tdrpTotal.setDelivery(BigDecimal.valueOf(Long.parseLong("0")));
				}else{
					tdrpTotal.setDelivery(BigDecimal.valueOf(Long.parseLong(deliveryTotal)));
				}
				if(largerDeliveryTotal==null||largerDeliveryTotal.equals("")){
				tdrpTotal.setLargerDelivery(BigDecimal.valueOf(Long.parseLong("0")));
				}else{
					tdrpTotal.setLargerDelivery(BigDecimal.valueOf(Long.parseLong(largerDeliveryTotal)));
				}
				if(secondDeliveryTotal==null||secondDeliveryTotal.equals("")){
				tdrpTotal.setSecondDelivery(BigDecimal.valueOf(Long.parseLong("0")));
				}else{
					tdrpTotal.setSecondDelivery(BigDecimal.valueOf(Long.parseLong(secondDeliveryTotal)));
				}
				if(o_LevelOrderTotal==null||o_LevelOrderTotal.equals("")){
				tdrpTotal.setOLevelOrder(BigDecimal.valueOf(Long.parseLong("0")));
				}else{
					tdrpTotal.setOLevelOrder(BigDecimal.valueOf(Long.parseLong(o_LevelOrderTotal)));
				}
				if(h_LevelOrderTotal==null||h_LevelOrderTotal.equals("")){
				tdrpTotal.setHLevelOrder(BigDecimal.valueOf(Long.parseLong("0")));
				}else{
					tdrpTotal.setHLevelOrder(BigDecimal.valueOf(Long.parseLong(h_LevelOrderTotal)));
				}
				if(a_LevelOrderTotal==null||a_LevelOrderTotal.equals("")){
				tdrpTotal.setALevelOrder(BigDecimal.valueOf(Long.parseLong("0")));
				}else{
					tdrpTotal.setALevelOrder(BigDecimal.valueOf(Long.parseLong(a_LevelOrderTotal)));
				}
				if(b_LevelOrderTotal==null||b_LevelOrderTotal.equals("")){
				tdrpTotal.setBLevelOrder(BigDecimal.valueOf(Long.parseLong("0")));
				}else{
					tdrpTotal.setBLevelOrder(BigDecimal.valueOf(Long.parseLong(b_LevelOrderTotal)));
				}
				if(c_LevelOrderTotal==null||c_LevelOrderTotal.equals("")){
				tdrpTotal.setCLevelOrder(BigDecimal.valueOf(Long.parseLong("0")));
				}else{
					tdrpTotal.setCLevelOrder(BigDecimal.valueOf(Long.parseLong(c_LevelOrderTotal)));
				}
				if(createCardTotal==null||createCardTotal.equals("")){
				tdrpTotal.setCreateCard(BigDecimal.valueOf(Long.parseLong("0")));
				}else{
					tdrpTotal.setCreateCard(BigDecimal.valueOf(Long.parseLong(createCardTotal)));
				}
				if(lostTotal==null||lostTotal.equals("")){
				tdrpTotal.setLost(BigDecimal.valueOf(Long.parseLong("0")));
				}else{
					tdrpTotal.setLost(BigDecimal.valueOf(Long.parseLong(lostTotal)));
				}
				if(realStockTotal==null||realStockTotal.equals("")){
				tdrpTotal.setRealStock(BigDecimal.valueOf(Long.parseLong("0")));
				}else{
					tdrpTotal.setRealStock(BigDecimal.valueOf(Long.parseLong(realStockTotal)));
				}
				if(regularRecommendTotal==null||regularRecommendTotal.equals("")){
				tdrpTotal.setRegularRecommend(BigDecimal.valueOf(Long.parseLong("0")));
				}else{
					tdrpTotal.setRegularRecommend(BigDecimal.valueOf(Long.parseLong(regularRecommendTotal)));
				}
				if(oldCarReplaceTotal==null||oldCarReplaceTotal.equals("")){
				tdrpTotal.setOldCarReplace(BigDecimal.valueOf(Long.parseLong("0")));
				}else{
					tdrpTotal.setOldCarReplace(BigDecimal.valueOf(Long.parseLong(oldCarReplaceTotal)));
				}
				if(h_RetainTotal==null||h_RetainTotal.equals("")){
				tdrpTotal.setHRetain(BigDecimal.valueOf(Long.parseLong("0")));
				}else{
					tdrpTotal.setHRetain(BigDecimal.valueOf(Long.parseLong(h_RetainTotal)));
				}
				if(a_RetainTotal==null||a_RetainTotal.equals("")){
				tdrpTotal.setARetain(BigDecimal.valueOf(Long.parseLong("0")));
				}else{
					tdrpTotal.setARetain(BigDecimal.valueOf(Long.parseLong(a_RetainTotal)));
				}
				if(b_RetainTotal==null||b_RetainTotal.equals("")){
				tdrpTotal.setBRetain(BigDecimal.valueOf(Long.parseLong("0")));
				}else{
					tdrpTotal.setBRetain(BigDecimal.valueOf(Long.parseLong(b_RetainTotal)));
				}
				if(c_RetainTotal==null||c_RetainTotal.equals("")){
				tdrpTotal.setCRetain(BigDecimal.valueOf(Long.parseLong("0")));
				}else{
					tdrpTotal.setCRetain(BigDecimal.valueOf(Long.parseLong(c_RetainTotal)));
				}
				if(unCommitOrderTotal==null||unCommitOrderTotal.equals("")){
				tdrpTotal.setUncommitOrder(BigDecimal.valueOf(Long.parseLong("0")));
				}else{
					tdrpTotal.setUncommitOrder(BigDecimal.valueOf(Long.parseLong(unCommitOrderTotal)));
				}
				
				Date currentTime = new Date();
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
				String S_createDate = formatter.format(currentTime);
				Date createDate=formatter.parse(S_createDate);
				tdrpTotal.setCreateDate(createDate);
				tdrpTotal.setContentId(Long.parseLong(contentId));
				tdrpTotal.setStatus(Constant.DAILY_STATUS_UNCONFIRM);
				tdrpTotal.setCreateBy(logonUser.getUserId());
				dao.insert(tdrpTotal);
			}
			act.setOutData("returnValue", 1);
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "页面物料组树查询结果");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**queryDaily
	 * 
	 * 查询具体报表
	 */
	public void dailyReportQuery(){
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			String dealerIds=logonUser.getDealerId();
			String[] carType0=Constant.carType;
			String[] carType=new String[carType0.length+1];
			for(int i=0;i<carType.length;i++){
				if(i==carType0.length){
					carType[i]="小计";
				}else{
					carType[i]=carType0[i];
				}
			}
			String contentId=act.getRequest().getParamValue("contentId");
			act.setOutData("dilyDate", contentId);
			//查询时使用--start
			List<Map<String,Object>> totalList=new ArrayList<Map<String,Object>>();
			totalList=dao.queryDaily(null,contentId,null);
			act.setOutData("TotalList", totalList);
			//end
			
			for(int i=0;i<carType.length;i++){
				List<Map<String,Object>> dailyList1=dao.queryDaily(carType[i],contentId,null);
				act.setOutData("dailyList"+(i+1), dailyList1);
			}
			List<Map<String, Object>> dealerList=dao.queryDealerName(contentId);
			act.setOutData("carType", Constant.carType);
			act.setOutData("dealerList", dealerList);
			act.setOutData("cols", Constant.carType.length+2-5);
			act.setForword(dailyQuery);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.DISABLED_FAILURE_CODE,"新增日报表初始化");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 
	 * 显示查询列表
	 */
	public void SeachDailyInit(){
			AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try{
			String startDate=act.getRequest().getParamValue("CON_APPLY_DATE_START");
			String endDate=act.getRequest().getParamValue("CON_APPLY_DATE_END");
			Long orgId=logonUser.getOrgId();
			String dealerCodes="";
			String dealerCode="";
			String dailyAudit=act.getRequest().getParamValue("dailyAudit");
			Long dailyAudits=null;
			if(dailyAudit.equals("0")){
				dailyAudits=Long.parseLong("0");
			}else{
				dailyAudits=Long.parseLong(dailyAudit);
			}
			if(logonUser.getDutyType().equals(Constant.DUTY_TYPE_DEALER.toString())){
				dealerCodes=logonUser.getDealerCode();
			}else{
				dealerCode=act.getRequest().getParamValue("dealerCodes");
				if("".equals(dealerCode)||dealerCode==null){
					dealerCodes=dealerCode;
				}else{
					dealerCodes=dealerCode.replace(",", "','");
				}
			}
			
			if(startDate==null||startDate.equals("")){
//				Date beginDate=new Date(1970-01-01);
//				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
//				startDate=formatter.format(beginDate);
			}
			if(endDate==null||endDate.equals("")){
//				Date currentTime = new Date();
//				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
//				endDate = formatter.format(currentTime);
			}
			Integer curPage = act.getRequest().getParamValue("curPage") != null ? Integer.parseInt(act.getRequest().getParamValue("curPage")) : 1;
			PageResult<Map<String,Object>> ps=dao.seachDaily(dealerCodes,startDate,endDate, Constant.PAGE_SIZE, curPage,logonUser.getDutyType(),orgId,dailyAudits);
			act.setOutData("ps", ps);
		}catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.DISABLED_FAILURE_CODE,"新增日报表初始化");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
		
	}
	
	/**
	 * 进入修改界面的方法
	 * 
	 */
	public void dailyReportShowUpdate(){
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			String dealerIds=logonUser.getDealerId();
			List<Map<String, Object>> dealerList = DealerInfoDao.getInstance().getDealerInfo(dealerIds) ;
			act.setOutData("dealerList", dealerList);
			String[] carType0=Constant.carType;
			String[] carType=new String[carType0.length+1];
			for(int i=0;i<carType.length;i++){
				if(i==carType0.length){
					carType[i]="小计";
				}else{
					carType[i]=carType0[i];
				}
			}
			String contentId=act.getRequest().getParamValue("contentId");
			act.setOutData("contentId", contentId);
			List<Map<String,String>> dailyListIds=new ArrayList<Map<String,String>>();
			for(int i=0;i<carType.length;i++){
				List<Map<String,Object>> dailyList1=dao.queryDaily(carType[i],contentId,null);
				act.setOutData("dailyList"+(i+1), dailyList1);
				Map<String,String> mapIds=new HashMap<String, String>();
				mapIds.put("DAILY_REPORT_ID", dailyList1.get(0).get("DAILY_REPORT_ID").toString());
				dailyListIds.add(mapIds);
				
			}
			//不包含小计的数据
			List<Map<String,Object>> totalList=new ArrayList<Map<String,Object>>();
			totalList=dao.queryDaily(null,contentId,"小计");
			act.setOutData("TotalList", totalList);
			//小计的数据
			List<Map<String,Object>> smallList=new ArrayList<Map<String,Object>>();
			smallList=dao.queryDaily("小计",contentId,"");
			act.setOutData("smallList", smallList);
			act.setOutData("seriesCount", Constant.carType.length);
			act.setOutData("dailyReportIds", dailyListIds);
			act.setOutData("carType", Constant.carType);
			act.setOutData("cols", Constant.carType.length+2-5);
			act.setForword(dailyUpdate);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.DISABLED_FAILURE_CODE,"新增日报表初始化");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 
	 * 修改保存的方法
	 */
	public void updateDaily(){

		try {
//			String alto = act.getRequest().getParamValue("carType");
			String total = act.getRequest().getParamValue("total");
			TtDailyReportPO oldTdrpTotal=new TtDailyReportPO();
			TtDailyReportPO tdrpTotal=new TtDailyReportPO();
			for(int i=0;i<Constant.carType.length;i++){
				String carType=act.getRequest().getParamValue("carType"+(i+1));
				TtDailyReportPO tdrp1 = new TtDailyReportPO();
				TtDailyReportPO oldTdrp1 = new TtDailyReportPO();
				
				String dailyReportId1=act.getRequest().getParamValue("dailyReportId"+i);
				String firstPassenger1 =CommonUtils.checkNull(act.getRequest()
							.getParamValue("FirstPassenger"+i));
				String invitePassenger1 = CommonUtils.checkNull(act.getRequest()
						.getParamValue("InvitePassenger"+i));
				String callPassenger1 = CommonUtils.checkNull(act.getRequest()
						.getParamValue("CallPassenger"+i));
				String testDrive1 = CommonUtils.checkNull(act.getRequest()
						.getParamValue("TestDrive"+i));
				String delivery1 = CommonUtils.checkNull(act.getRequest().getParamValue(
						"Delivery"+i));
				String largerDelivery1 = CommonUtils.checkNull(act.getRequest()
						.getParamValue("LargerDelivery"+i));
				String secondDelivery1 = CommonUtils.checkNull(act.getRequest()
						.getParamValue("SecondDelivery"+i));
				String o_LevelOrder1 = CommonUtils.checkNull(act.getRequest()
						.getParamValue("O_levelOrder"+i));
				String h_LevelOrder1 = CommonUtils.checkNull(act.getRequest()
						.getParamValue("H_levelOrder"+i));
				String a_LevelOrder1 = CommonUtils.checkNull(act.getRequest()
						.getParamValue("A_levelOrder"+i));
				String b_LevelOrder1 = CommonUtils.checkNull(act.getRequest()
						.getParamValue("B_levelOrder"+i));
				String c_LevelOrder1 = CommonUtils.checkNull(act.getRequest()
						.getParamValue("C_levelOrder"+i));
				String createCard1 = CommonUtils.checkNull(act.getRequest()
						.getParamValue("CreateCard"+(i)));
				String lost1 = CommonUtils.checkNull(act.getRequest().getParamValue(
						"Lost"+(i)));
				String realStock1 = CommonUtils.checkNull(act.getRequest()
						.getParamValue("RealStock"+(i)));
				String regularRecommend1 = CommonUtils.checkNull(act.getRequest()
						.getParamValue("RegularRecommend"+(i)));
				String oldCarReplace1 = CommonUtils.checkNull(act.getRequest()
						.getParamValue("OldCarReplace"+(i)));
				String h_Retain1 = CommonUtils.checkNull(act.getRequest().getParamValue(
						"H_Retain"+(i)));
				String a_Retain1 = CommonUtils.checkNull(act.getRequest().getParamValue(
						"A_Retain"+(i)));
				String b_Retain1 = CommonUtils.checkNull(act.getRequest().getParamValue(
						"B_Retain"+(i)));
				String c_Retain1 = CommonUtils.checkNull(act.getRequest().getParamValue(
						"C_Retain"+(i)));
				String unCommitOrder1 = CommonUtils.checkNull(act.getRequest()
						.getParamValue("UnCommitOrder"+(i)));
				oldTdrp1.setDailyReportId(Long.parseLong(dailyReportId1));
				if(firstPassenger1==null||firstPassenger1.equals("")){
					tdrp1.setFirstPassenger(BigDecimal.valueOf(Long.parseLong("0")));
				}else{
					tdrp1.setFirstPassenger(BigDecimal.valueOf(Long.parseLong(firstPassenger1)));
				}
				if(invitePassenger1==null||invitePassenger1.equals("")){
				tdrp1.setInvitePassenger(BigDecimal.valueOf(Long.parseLong("0")));
				}else{
					tdrp1.setInvitePassenger(BigDecimal.valueOf(Long.parseLong(invitePassenger1)));
				}
				if(callPassenger1==null||callPassenger1.equals("")){
				tdrp1.setCallPassenger(BigDecimal.valueOf(Long.parseLong("0")));
				}else{
					tdrp1.setCallPassenger(BigDecimal.valueOf(Long.parseLong(callPassenger1)));
				}
				if(testDrive1==null||testDrive1.equals("")){
				tdrp1.setTestDriver(BigDecimal.valueOf(Long.parseLong("0")));
				}else{
					tdrp1.setTestDriver(BigDecimal.valueOf(Long.parseLong(testDrive1)));
				}
				if(delivery1==null||delivery1.equals("")){
				tdrp1.setDelivery(BigDecimal.valueOf(Long.parseLong("0")));
				}else{
					tdrp1.setDelivery(BigDecimal.valueOf(Long.parseLong(delivery1)));
				}
				if(largerDelivery1==null||largerDelivery1.equals("")){
				tdrp1.setLargerDelivery(BigDecimal.valueOf(Long.parseLong("0")));
				}else{
					tdrp1.setLargerDelivery(BigDecimal.valueOf(Long.parseLong(largerDelivery1)));
				}
				if(secondDelivery1==null||secondDelivery1.equals("")){
				tdrp1.setSecondDelivery(BigDecimal.valueOf(Long.parseLong("0")));
				}else{
					tdrp1.setSecondDelivery(BigDecimal.valueOf(Long.parseLong(secondDelivery1)));
				}
				if(o_LevelOrder1==null||o_LevelOrder1.equals("")){
				tdrp1.setOLevelOrder(BigDecimal.valueOf(Long.parseLong("0")));
				}else{
					tdrp1.setOLevelOrder(BigDecimal.valueOf(Long.parseLong(o_LevelOrder1)));
				}
				if(h_LevelOrder1==null||h_LevelOrder1.equals("")){
				tdrp1.setHLevelOrder(BigDecimal.valueOf(Long.parseLong("0")));
				}else{
					tdrp1.setHLevelOrder(BigDecimal.valueOf(Long.parseLong(h_LevelOrder1)));
				}
				if(a_LevelOrder1==null||a_LevelOrder1.equals("")){
				tdrp1.setALevelOrder(BigDecimal.valueOf(Long.parseLong("0")));
				}else{
					tdrp1.setALevelOrder(BigDecimal.valueOf(Long.parseLong(a_LevelOrder1)));
				}
				if(b_LevelOrder1==null||b_LevelOrder1.equals("")){
				tdrp1.setBLevelOrder(BigDecimal.valueOf(Long.parseLong("0")));
				}else{
					tdrp1.setBLevelOrder(BigDecimal.valueOf(Long.parseLong(b_LevelOrder1)));
				}
				if(c_LevelOrder1==null||c_LevelOrder1.equals("")){
				tdrp1.setCLevelOrder(BigDecimal.valueOf(Long.parseLong("0")));
				}else{
					tdrp1.setCLevelOrder(BigDecimal.valueOf(Long.parseLong(c_LevelOrder1)));
				}
				if(createCard1==null||createCard1.equals("")){
				tdrp1.setCreateCard(BigDecimal.valueOf(Long.parseLong("0")));
				}else{
					tdrp1.setCreateCard(BigDecimal.valueOf(Long.parseLong(createCard1)));
				}
				if(lost1==null||lost1.equals("")){
				tdrp1.setLost(BigDecimal.valueOf(Long.parseLong("0")));
				}else{
					tdrp1.setLost(BigDecimal.valueOf(Long.parseLong(lost1)));
				}
				if(realStock1==null||realStock1.equals("")){
				tdrp1.setRealStock(BigDecimal.valueOf(Long.parseLong("0")));
				}else{
					tdrp1.setRealStock(BigDecimal.valueOf(Long.parseLong(realStock1)));
				}
				if(regularRecommend1==null||regularRecommend1.equals("")){
				tdrp1.setRegularRecommend(BigDecimal.valueOf(Long.parseLong("0")));
				}else{
					tdrp1.setRegularRecommend(BigDecimal.valueOf(Long.parseLong(regularRecommend1)));
				}
				if(oldCarReplace1==null||oldCarReplace1.equals("")){
				tdrp1.setOldCarReplace(BigDecimal.valueOf(Long.parseLong("0")));
				}else{
					tdrp1.setOldCarReplace(BigDecimal.valueOf(Long.parseLong(oldCarReplace1)));
				}
				if(h_Retain1==null||h_Retain1.equals("")){
				tdrp1.setHRetain(BigDecimal.valueOf(Long.parseLong("0")));
				}else{
					tdrp1.setHRetain(BigDecimal.valueOf(Long.parseLong(h_Retain1)));
				}
				if(a_Retain1==null||a_Retain1.equals("")){
				tdrp1.setARetain(BigDecimal.valueOf(Long.parseLong("0")));
				}else{
					tdrp1.setARetain(BigDecimal.valueOf(Long.parseLong(a_Retain1)));
				}
				if(b_Retain1==null||b_Retain1.equals("")){
				tdrp1.setBRetain(BigDecimal.valueOf(Long.parseLong("0")));
				}else{
					tdrp1.setBRetain(BigDecimal.valueOf(Long.parseLong(b_Retain1)));
				}
				if(c_Retain1==null||c_Retain1.equals("")){
				tdrp1.setCRetain(BigDecimal.valueOf(Long.parseLong("0")));
				}else{
					tdrp1.setCRetain(BigDecimal.valueOf(Long.parseLong(c_Retain1)));
				}
				if(unCommitOrder1==null||unCommitOrder1.equals("")){
				tdrp1.setUncommitOrder(BigDecimal.valueOf(Long.parseLong("0")));
				}else{
					tdrp1.setUncommitOrder(BigDecimal.valueOf(Long.parseLong(unCommitOrder1)));
				}
				Date currentTime = new Date();
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
				String S_updateDate = formatter.format(currentTime);
				Date updateDate=formatter.parse(S_updateDate);
				tdrp1.setUpdateDate(updateDate);
				tdrp1.setUpdateBy(logonUser.getUserId());
				dao.update(oldTdrp1,tdrp1);
			}
			String dailyReportId6=act.getRequest().getParamValue("dailyReportId"+Constant.carType.length);
			String firstPassengerTotal =CommonUtils.checkNull(act.getRequest()
						.getParamValue("FirstPassengerTotal"));
			String invitePassengerTotal = CommonUtils.checkNull(act.getRequest()
					.getParamValue("InvitePassengerTotal"));
			String callPassengerTotal = CommonUtils.checkNull(act.getRequest()
					.getParamValue("CallPassengerTotal"));
			String testDriveTotal = CommonUtils.checkNull(act.getRequest()
					.getParamValue("TestDriveTotal"));
			String deliveryTotal = CommonUtils.checkNull(act.getRequest().getParamValue(
					"DeliveryTotal"));
			String largerDeliveryTotal = CommonUtils.checkNull(act.getRequest()
					.getParamValue("LargerDeliveryTotal"));
			String secondDeliveryTotal = CommonUtils.checkNull(act.getRequest()
					.getParamValue("SecondDeliveryTotal"));
			String o_LevelOrderTotal = CommonUtils.checkNull(act.getRequest()
					.getParamValue("O_levelOrderTotal"));
			String h_LevelOrderTotal = CommonUtils.checkNull(act.getRequest()
					.getParamValue("H_levelOrderTotal"));
			String a_LevelOrderTotal = CommonUtils.checkNull(act.getRequest()
					.getParamValue("A_levelOrderTotal"));
			String b_LevelOrderTotal = CommonUtils.checkNull(act.getRequest()
					.getParamValue("B_levelOrderTotal"));
			String c_LevelOrderTotal = CommonUtils.checkNull(act.getRequest()
					.getParamValue("C_levelOrderTotal"));
			String createCardTotal = CommonUtils.checkNull(act.getRequest()
					.getParamValue("CreateCardTotal"));
			String lostTotal = CommonUtils.checkNull(act.getRequest().getParamValue(
					"LostTotal"));
			String realStockTotal = CommonUtils.checkNull(act.getRequest()
					.getParamValue("RealStockTotal"));
			String regularRecommendTotal = CommonUtils.checkNull(act.getRequest()
					.getParamValue("RegularRecommendTotal"));
			String oldCarReplaceTotal = CommonUtils.checkNull(act.getRequest()
					.getParamValue("OldCarReplaceTotal"));
			String h_RetainTotal = CommonUtils.checkNull(act.getRequest().getParamValue(
					"H_RetainTotal"));
			String a_RetainTotal = CommonUtils.checkNull(act.getRequest().getParamValue(
					"A_RetainTotal"));
			String b_RetainTotal = CommonUtils.checkNull(act.getRequest().getParamValue(
					"B_RetainTotal"));
			String c_RetainTotal = CommonUtils.checkNull(act.getRequest().getParamValue(
					"C_RetainTotal"));
			String unCommitOrderTotal = CommonUtils.checkNull(act.getRequest()
					.getParamValue("UnCommitOrderTotal"));
			oldTdrpTotal.setDailyReportId(Long.parseLong(dailyReportId6));
			if(firstPassengerTotal==null||firstPassengerTotal.equals("")){
				tdrpTotal.setFirstPassenger(BigDecimal.valueOf(Long.parseLong("0")));
			}else{
				tdrpTotal.setFirstPassenger(BigDecimal.valueOf(Long.parseLong(firstPassengerTotal)));
			}
			if(invitePassengerTotal==null||invitePassengerTotal.equals("")){
			tdrpTotal.setInvitePassenger(BigDecimal.valueOf(Long.parseLong("0")));
			}else{
				tdrpTotal.setInvitePassenger(BigDecimal.valueOf(Long.parseLong(invitePassengerTotal)));
			}
			if(callPassengerTotal==null||callPassengerTotal.equals("")){
			tdrpTotal.setCallPassenger(BigDecimal.valueOf(Long.parseLong("0")));
			}else{
				tdrpTotal.setCallPassenger(BigDecimal.valueOf(Long.parseLong(callPassengerTotal)));
			}
			if(testDriveTotal==null||testDriveTotal.equals("")){
			tdrpTotal.setTestDriver(BigDecimal.valueOf(Long.parseLong("0")));
			}else{
				tdrpTotal.setTestDriver(BigDecimal.valueOf(Long.parseLong(testDriveTotal)));
			}
			if(deliveryTotal==null||deliveryTotal.equals("")){
			tdrpTotal.setDelivery(BigDecimal.valueOf(Long.parseLong("0")));
			}else{
				tdrpTotal.setDelivery(BigDecimal.valueOf(Long.parseLong(deliveryTotal)));
			}
			if(largerDeliveryTotal==null||largerDeliveryTotal.equals("")){
			tdrpTotal.setLargerDelivery(BigDecimal.valueOf(Long.parseLong("0")));
			}else{
				tdrpTotal.setLargerDelivery(BigDecimal.valueOf(Long.parseLong(largerDeliveryTotal)));
			}
			if(secondDeliveryTotal==null||secondDeliveryTotal.equals("")){
			tdrpTotal.setSecondDelivery(BigDecimal.valueOf(Long.parseLong("0")));
			}else{
				tdrpTotal.setSecondDelivery(BigDecimal.valueOf(Long.parseLong(secondDeliveryTotal)));
			}
			if(o_LevelOrderTotal==null||o_LevelOrderTotal.equals("")){
			tdrpTotal.setOLevelOrder(BigDecimal.valueOf(Long.parseLong("0")));
			}else{
				tdrpTotal.setOLevelOrder(BigDecimal.valueOf(Long.parseLong(o_LevelOrderTotal)));
			}
			if(h_LevelOrderTotal==null||h_LevelOrderTotal.equals("")){
			tdrpTotal.setHLevelOrder(BigDecimal.valueOf(Long.parseLong("0")));
			}else{
				tdrpTotal.setHLevelOrder(BigDecimal.valueOf(Long.parseLong(h_LevelOrderTotal)));
			}
			if(a_LevelOrderTotal==null||a_LevelOrderTotal.equals("")){
			tdrpTotal.setALevelOrder(BigDecimal.valueOf(Long.parseLong("0")));
			}else{
				tdrpTotal.setALevelOrder(BigDecimal.valueOf(Long.parseLong(a_LevelOrderTotal)));
			}
			if(b_LevelOrderTotal==null||b_LevelOrderTotal.equals("")){
			tdrpTotal.setBLevelOrder(BigDecimal.valueOf(Long.parseLong("0")));
			}else{
				tdrpTotal.setBLevelOrder(BigDecimal.valueOf(Long.parseLong(b_LevelOrderTotal)));
			}
			if(c_LevelOrderTotal==null||c_LevelOrderTotal.equals("")){
			tdrpTotal.setCLevelOrder(BigDecimal.valueOf(Long.parseLong("0")));
			}else{
				tdrpTotal.setCLevelOrder(BigDecimal.valueOf(Long.parseLong(c_LevelOrderTotal)));
			}
			if(createCardTotal==null||createCardTotal.equals("")){
			tdrpTotal.setCreateCard(BigDecimal.valueOf(Long.parseLong("0")));
			}else{
				tdrpTotal.setCreateCard(BigDecimal.valueOf(Long.parseLong(createCardTotal)));
			}
			if(lostTotal==null||lostTotal.equals("")){
			tdrpTotal.setLost(BigDecimal.valueOf(Long.parseLong("0")));
			}else{
				tdrpTotal.setLost(BigDecimal.valueOf(Long.parseLong(lostTotal)));
			}
			if(realStockTotal==null||realStockTotal.equals("")){
			tdrpTotal.setRealStock(BigDecimal.valueOf(Long.parseLong("0")));
			}else{
				tdrpTotal.setRealStock(BigDecimal.valueOf(Long.parseLong(realStockTotal)));
			}
			if(regularRecommendTotal==null||regularRecommendTotal.equals("")){
			tdrpTotal.setRegularRecommend(BigDecimal.valueOf(Long.parseLong("0")));
			}else{
				tdrpTotal.setRegularRecommend(BigDecimal.valueOf(Long.parseLong(regularRecommendTotal)));
			}
			if(oldCarReplaceTotal==null||oldCarReplaceTotal.equals("")){
			tdrpTotal.setOldCarReplace(BigDecimal.valueOf(Long.parseLong("0")));
			}else{
				tdrpTotal.setOldCarReplace(BigDecimal.valueOf(Long.parseLong(oldCarReplaceTotal)));
			}
			if(h_RetainTotal==null||h_RetainTotal.equals("")){
			tdrpTotal.setHRetain(BigDecimal.valueOf(Long.parseLong("0")));
			}else{
				tdrpTotal.setHRetain(BigDecimal.valueOf(Long.parseLong(h_RetainTotal)));
			}
			if(a_RetainTotal==null||a_RetainTotal.equals("")){
			tdrpTotal.setARetain(BigDecimal.valueOf(Long.parseLong("0")));
			}else{
				tdrpTotal.setARetain(BigDecimal.valueOf(Long.parseLong(a_RetainTotal)));
			}
			if(b_RetainTotal==null||b_RetainTotal.equals("")){
			tdrpTotal.setBRetain(BigDecimal.valueOf(Long.parseLong("0")));
			}else{
				tdrpTotal.setBRetain(BigDecimal.valueOf(Long.parseLong(b_RetainTotal)));
			}
			if(c_RetainTotal==null||c_RetainTotal.equals("")){
			tdrpTotal.setCRetain(BigDecimal.valueOf(Long.parseLong("0")));
			}else{
				tdrpTotal.setCRetain(BigDecimal.valueOf(Long.parseLong(c_RetainTotal)));
			}
			if(unCommitOrderTotal==null||unCommitOrderTotal.equals("")){
			tdrpTotal.setUncommitOrder(BigDecimal.valueOf(Long.parseLong("0")));
			}else{
				tdrpTotal.setUncommitOrder(BigDecimal.valueOf(Long.parseLong(unCommitOrderTotal)));
			}
			Date currentTimes = new Date();
			SimpleDateFormat formatters = new SimpleDateFormat("yyyy-MM-dd");
			String S_updateDates = formatters.format(currentTimes);
			Date updateDates=formatters.parse(S_updateDates);
			tdrpTotal.setUpdateDate(updateDates);
			tdrpTotal.setUpdateBy(logonUser.getUserId());
			dao.update(oldTdrpTotal,tdrpTotal);
			act.setOutData("returnValue", 1);
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "页面物料组树查询结果");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 
	 * 提交
	 */
	public void dailyCommit(){
		try{
			TtDailyReportPO oldTdrp=new TtDailyReportPO();
			TtDailyReportPO tdrp=new TtDailyReportPO();
			String contentId=act.getRequest().getParamValue("contentId");
			oldTdrp.setContentId(Long.parseLong(contentId));
			Date currentTime = new Date();
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			String S_updateDate = formatter.format(currentTime);
			Date updateDate=formatter.parse(S_updateDate);
			tdrp.setAuditDate(updateDate);
			tdrp.setAuditBy(logonUser.getUserId());
			tdrp.setStatus(Constant.DAILY_STATUS_CONFIRM);
			dao.update(oldTdrp, tdrp);
		act.setOutData("returnValue", 1);
		} catch (Exception e) {// 异常方法
		BizException e1 = new BizException(act, e,
				ErrorCodeConstant.QUERY_FAILURE_CODE, "提交失败");
		logger.error(logonUser, e1);
		act.setException(e1);
		}
	}
	
	/**
	 * 
	 * 删除
	 */
	public void dailyDelete(){
		try{
			TtDailyReportPO oldTdrp=new TtDailyReportPO();
			TtDailyReportPO tdrp=new TtDailyReportPO();
			String contentId=act.getRequest().getParamValue("contentId");
			oldTdrp.setContentId(Long.parseLong(contentId));
			Date currentTime = new Date();
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			String S_updateDate = formatter.format(currentTime);
			Date updateDate=formatter.parse(S_updateDate);
			tdrp.setAuditDate(updateDate);
			tdrp.setAuditBy(logonUser.getUserId());
			tdrp.setStatus(Constant.DAILY_STATUS_DELETE);
			dao.update(oldTdrp, tdrp);
		act.setOutData("returnValue", 1);
		} catch (Exception e) {// 异常方法
		BizException e1 = new BizException(act, e,
				ErrorCodeConstant.QUERY_FAILURE_CODE, "删除失败");
		logger.error(logonUser, e1);
		act.setException(e1);
		}
	}
}
