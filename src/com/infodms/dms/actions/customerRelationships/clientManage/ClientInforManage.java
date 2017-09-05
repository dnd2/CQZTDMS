package com.infodms.dms.actions.customerRelationships.clientManage;

import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jxl.Workbook;
import jxl.write.Label;

import org.apache.log4j.Logger;

import com.infodms.dms.actions.util.CommonUtilActions;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.bean.ClientSearchCondition;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.common.materialManager.MaterialGroupManagerDao;
import com.infodms.dms.dao.common.CommonUtilDao;
import com.infodms.dms.dao.customerRelationships.ClientInforManageDao;
import com.infodms.dms.dao.sales.customerInfoManage.SalesReportDAO;
import com.infodms.dms.dao.sales.salesConsultant.SalesConsultantDAO;
import com.infodms.dms.dao.sales.salesInfoManage.SalesInfoQueryDAO;
import com.infodms.dms.dao.sales.storageManage.CheckVehicleDAO;
import com.infodms.dms.dao.tccode.TcCodeDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TmBusinessAreaPO;
import com.infodms.dms.po.TmDealerPO;
import com.infodms.dms.po.TmVehiclePO;
import com.infodms.dms.po.TmVhclMaterialGroupPO;
import com.infodms.dms.po.TtCustomerPO;
import com.infodms.dms.po.TtDealerActualSalesLogPO;
import com.infodms.dms.po.TtDealerActualSalesPO;
import com.infodms.dms.po.TtFleetContractPO;
import com.infodms.dms.po.TtLinkmanPO;
import com.infodms.dms.po.TtVsVhclChngPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.XHBUtil;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;
import com.infoservice.po3.bean.PageResult;
/**
 * 
 * @ClassName     : ClientInforManage 
 * @Description   : 客户信息管理
 * @author        : wangming
 * CreateDate     : 2013-4-8
 */
public class ClientInforManage{
	private static Logger logger = Logger.getLogger(ClientInforManage.class);
	// 客户信息初始化页面
	private final String clientInforManageUrl = "/jsp/customerRelationships/clientManage/clientInforManage.jsp";
	private final String clientInforManageWatchUrl = "/jsp/customerRelationships/clientManage/clientInforManageWatch.jsp";
	private final String callCenterSalesInfoCheckInitUrl = "/jsp/customerRelationships/clientManage/callCenterSalesInfoCheckInit.jsp";
	private final String salesInfoCheckInitUrl = "/jsp/sales/salesInfoManage/salesInfoCheckInit.jsp";
	private final String salesInfoCheck = "/jsp/customerRelationships/clientManage/salesInfoCheck.jsp";
	private final String updateReportCVS = "/jsp/customerRelationships/clientManage/updateReportCVS.jsp";
	private final String salesInfoCheckLog = "/jsp/sales/customerInfoManage/salesInfoCheckLog.jsp";
	
	ActionContext act = ActionContext.getContext();
	AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
	RequestWrapper request = act.getRequest();
	
	
	/**
	 * 客户信息初始化
	 */
	public void clientInforManageInit(){		
		try{
			CommonUtilActions commonUtilActions = new CommonUtilActions();
			act.setOutData("seriesList", getAllSeries());
			act.setOutData("yieldlyList", getYieldly());
			
			//省份
			act.setOutData("proviceList", commonUtilActions.getProvice());
			
			act.setOutData("guestStarsList",commonUtilActions.getTcCode(Constant.GUEST_STARS.toString()));
			act.setOutData("useList",commonUtilActions.getTcCode(Constant.SALES_ADDRESS.toString()));
			act.setForword(clientInforManageUrl);
		}catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ACTION_NAME_ERROR_CODE,"客户信息初始化");
			logger.error(logger,e1);
			act.setException(e1);
		}
	}
	
	public void queryClientInforManage(){
		act.getResponse().setContentType("application/json");
		try{
			ClientSearchCondition clientSearchCondition = setClientSearchCondition();

			ClientInforManageDao dao = ClientInforManageDao.getInstance();
			//分页方法 begin
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage"))	: 1; // 处理当前页	
				
			PageResult<Map<String,Object>> queryClientInforManageData = dao.queryClientInforManage(clientSearchCondition,Constant.PAGE_SIZE,curPage);
			
			act.setOutData("ps", queryClientInforManageData);
		} catch(Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"客户信息查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	public void watchClientInforManage(){
		try{
			long orderId = Long.parseLong((String)request.getParamValue("id"));
			ClientInforManageDao dao = ClientInforManageDao.getInstance();
			act.setOutData("clientInforManageMap", dao.getClientInforByOrderId(orderId));
			act.setForword(clientInforManageWatchUrl);
		}catch(Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"客户信息查看");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	public void watchClientInfor(){
		try{
			
			long ctmid = Long.parseLong(((String)request.getParamValue("ctmid")).equals("null") ? "-1":(String)request.getParamValue("ctmid"));
			long cpid = Long.parseLong((String)request.getParamValue("cpid"));
			String vin = (String)request.getParamValue("vin");
			ClientInforManageDao dao = ClientInforManageDao.getInstance();
			act.setOutData("clientInforManageMap", dao.getClientInforByCtmidAndVinAndCpidYx(ctmid,vin,cpid));
			act.setForword(clientInforManageWatchUrl);
		}catch(Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"客户信息查看");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 
	 * @Title      : setClientSearchCondition()
	 * @Description: 封装客户信息查询条件
	 * @param      : @return  ClientSearchCondition    
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-9
	 */
	private ClientSearchCondition setClientSearchCondition(){
		String purchasedDateStart = CommonUtils.checkNull(request.getParamValue("purchasedDateStart"));  				
		String purchasedDateEnd = CommonUtils.checkNull(request.getParamValue("purchasedDateEnd"));  				
		String ctmName = CommonUtils.checkNull(request.getParamValue("ctmName"));  				
		String tel = CommonUtils.checkNull(request.getParamValue("tel"));  				
		String yieldly = CommonUtils.checkNull(request.getParamValue("yieldly"));  				
		String series = CommonUtils.checkNull(request.getParamValue("series"));  				
		String model = CommonUtils.checkNull(request.getParamValue("model"));  				
		String engineNo = CommonUtils.checkNull(request.getParamValue("engineNo"));  				
		String vin = CommonUtils.checkNull(request.getParamValue("vin"));  				
		String guestStars = CommonUtils.checkNull(request.getParamValue("guestStars"));  				
		String province = CommonUtils.checkNull(request.getParamValue("province"));  				
		String city = CommonUtils.checkNull(request.getParamValue("city"));
		String use = CommonUtils.checkNull(request.getParamValue("use"));
		
		ClientSearchCondition clientSearchCondition = new ClientSearchCondition();
		clientSearchCondition.setCity(city);
		clientSearchCondition.setCtmName(ctmName);
		clientSearchCondition.setEngineNo(engineNo);
		clientSearchCondition.setGuestStars(guestStars);
		clientSearchCondition.setModel(model);
		clientSearchCondition.setProvince(province);
		clientSearchCondition.setPurchasedDateEnd(purchasedDateEnd);
		clientSearchCondition.setPurchasedDateStart(purchasedDateStart);
		clientSearchCondition.setSeries(series);
		clientSearchCondition.setTel(tel);
		clientSearchCondition.setVin(vin);
		clientSearchCondition.setYieldly(yieldly);
		clientSearchCondition.setUse(use);
		return clientSearchCondition;
	}
	/**
	 * 
	 * @Title      : getAllSeries
	 * @Description: 查询所有车系
	 * @param      : @return  List<TmVhclMaterialGroupPO>    
	 * @return     :  List<TmVhclMaterialGroupPO>
	 * @throws     :
	 * LastDate    : 2013-4-9
	 * 
	 */
	private List<TmVhclMaterialGroupPO> getAllSeries(){
		CommonUtilDao dao = CommonUtilDao.getInstance();
		return dao.getAllSeries();
	}
	/**
	 * 
	 * @Title      : getYieldly
	 * @Description: 查询所有产地 
	 * @param      : @return List<TmBusinessAreaPO>     
	 * @return     : List<TmBusinessAreaPO>   
	 * LastDate    : 2013-4-16
	 * @author wangming
	 */
	private List<TmBusinessAreaPO> getYieldly(){
		CommonUtilDao dao = CommonUtilDao.getInstance();
		return dao.getYieldly();
	}
	
	
	public void clientInforManageExcel(){
		try{
			ClientSearchCondition clientSearchCondition = setClientSearchCondition();
			ClientInforManageDao dao = ClientInforManageDao.getInstance();
			int count = dao.countClientInforManage(clientSearchCondition);
			
			if(count>10000) {
				act.setOutData("DataLonger", "数据超过10000条,最高导出10000条数据");
			}else{
				List<Map<String, Object>> clientInforManageData = dao.queryClientInforManage(clientSearchCondition);
				if(clientInforManageData!=null && clientInforManageData.size()>0){
					callClientInforManageDataToExcel(clientInforManageData);
				}
			}
			
			act.setForword(clientInforManageUrl);
		}catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.SPECIAL_MEG,"转Excel失败!");
			logger.error(logonUser,e1);
			act.setException(e1);
		}

	}
	
	private void callClientInforManageDataToExcel(List<Map<String, Object>> list) throws Exception{
		TcCodeDao tcCodeDao = TcCodeDao.getInstance();
		String[] head=new String[9];
		head[0]="客户名称";
		head[1]="手机号";
		head[2]="VIN号";
		head[3]="车型";
		head[4]="省份";
		head[5]="县市";
		head[6]="客户级别";
		head[7]="地区";
		head[8]="经销商名称";
		
	    List list1=new ArrayList();
	    if(list!=null&&list.size()!=0){
			for(int i=0;i<list.size();i++){
		    	Map map =(Map)list.get(i);
		    	if(map!=null&&map.size()!=0){
					String[]detail=new String[9];
					detail[0] = CommonUtils.checkNull(map.get("CTMNAME"));
					detail[1] = CommonUtils.checkNull(map.get("PHONE"));
					detail[2] = CommonUtils.checkNull(map.get("VIN"));
					detail[3] = CommonUtils.checkNull(map.get("MODELNAME"));
					detail[4] = CommonUtils.checkNull(map.get("PROVINCE"));
					detail[5] = CommonUtils.checkNull(map.get("CITY"));
					detail[6] = CommonUtils.checkNull(map.get("GUESTSTARS"));
					detail[7] = CommonUtils.checkNull(map.get("TOWN"));
					detail[8] = CommonUtils.checkNull(map.get("DEALERNAME"));
					list1.add(detail);
		    	}
		    }
			this.exportEx(ActionContext.getContext().getResponse(), request, head, list1);
	    }
	}	

	public static Object exportEx(ResponseWrapper response,
			RequestWrapper request, String[] head, List<String[]> list)
			throws Exception {

		String name = "客户信息查询清单.xls";
		jxl.write.WritableWorkbook wwb = null;
		OutputStream out = null;
		try {
			response.setContentType("application/octet-stream");
		    response.addHeader("Content-disposition", "attachment;filename="+URLEncoder.encode(name, "utf-8"));
			out = response.getOutputStream();
			wwb = Workbook.createWorkbook(out);
			jxl.write.WritableSheet ws = wwb.createSheet("sheettest", 0);

			if (head != null && head.length > 0) {
				for (int i = 0; i < head.length; i++) {
					ws.addCell(new Label(i, 0, head[i]));
				}
			}
			int pageSize=list.size()/30000;
			for (int z = 1; z < list.size() + 1; z++) {
				String[] str = list.get(z - 1);
				for (int i = 0; i < str.length; i++) {
					ws.addCell(new Label(i, z, str[i]));
				}
			}
			wwb.write();
			out.flush();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			if (null != wwb) {
				wwb.close();
			}
			if (null != out) {
				out.close();
			}
		}
		return null;
	}
	
	/**
	 * 客户实销信息审核初始化页面
	 */
	public void callCenterSalesInfoCheckInit(){
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);	
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			Calendar c = Calendar.getInstance();
			Date d1 = c.getTime();
		    String endDate = dateFormat.format(d1);
		    c.add(Calendar.MONTH, -1);
		    Date d2 = c.getTime();
		    String startDate = dateFormat.format(d2);
		    act.setOutData("startDate", startDate);
		    act.setOutData("endDate", endDate);
			List<Map<String, Object>> areaList = MaterialGroupManagerDao.getPoseIdBusiness(logonUser.getPoseId().toString());
			act.setOutData("areaList", areaList);
			act.setForword(callCenterSalesInfoCheckInitUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "呼叫中心实销信息审核初始化");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 销售部实销信息审核初始化页面
	 */
	public void salesInfoCheckInit(){
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);	
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			Calendar c = Calendar.getInstance();
			Date d1 = c.getTime();
		    String endDate = dateFormat.format(d1);
		    c.add(Calendar.MONTH, -1);
		    Date d2 = c.getTime();
		    String startDate = dateFormat.format(d2);
		    act.setOutData("startDate", startDate);
		    act.setOutData("endDate", endDate);
			List<Map<String, Object>> areaList = MaterialGroupManagerDao.getPoseIdBusiness(logonUser.getPoseId().toString());
			act.setOutData("areaList", areaList);
			act.setForword(salesInfoCheckInitUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "销售部实销信息审核初始化");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/** 
	* @Title	  : salesInfoQuery 
	* @Description: 实销信息查询结果展示
	* @throws 
	* @LastUpdate :2010-6-30
	*/
	public void salesInfoCheckQuery(){
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			act.getSession().get(Constant.LOGON_USER);
			String orgCode=CommonUtils.checkNull(request.getParamValue("orgCode"));					//选择区域
			String customer_type = CommonUtils.checkNull(request.getParamValue("customer_type"));	//客户类型
			String customer_name = CommonUtils.checkNull(request.getParamValue("customer_name"));	//客户名称
			String vin = CommonUtils.checkNull(request.getParamValue("vin"));						//VIN
			String customer_phone = CommonUtils.checkNull(request.getParamValue("customer_phone"));	//客户电话
			String fleet_name = CommonUtils.checkNull(request.getParamValue("fleet_name"));			//集团客户名称
			String materialCode = CommonUtils.checkNull(request.getParamValue("materialCode"));		//选择物料组
			String is_fleet = CommonUtils.checkNull(request.getParamValue("is_fleet"));				//是否集团客户
			String contract_no = CommonUtils.checkNull(request.getParamValue("contract_no"));		//集团客户合同
			String startDate = CommonUtils.checkNull(request.getParamValue("startDate"));			//上报日期:开始
			String endDate = CommonUtils.checkNull(request.getParamValue("endDate"));				//上报日期 ：截止
			String dealerCode = CommonUtils.checkNull(request.getParamValue("dealerCode"));			//经销商代码
			String areaId = CommonUtils.checkNull(request.getParamValue("areaId"));
			String callcenterCheckStatus = CommonUtils.checkNull(request.getParamValue("callcenterCheckStatus"));//呼叫中心审核状态
			String salesCheckStatus = CommonUtils.checkNull(request.getParamValue("salesCheckStatus"));//销售中心审核状态
			String	carCharctor = CommonUtils.checkNull(request.getParamValue("SERVICEACTIVITY_CHARACTOR"));
			String  comm = CommonUtils.checkNull(request.getParamValue("comm"));
			String	onlySndDealer = CommonUtils.checkNull(request.getParamValue("onlySndDealer"));
			String orgId = "" ;
			String dutyType = logonUser.getDutyType() ;
			
			if(Constant.DUTY_TYPE_LARGEREGION.toString().equals(dutyType)) {
				orgId = logonUser.getOrgId().toString() ;
			}
			Map<String,String> map = new HashMap<String, String>();
			map.put("orgId", orgId);
			map.put("orgCode", orgCode);
			map.put("customer_type", customer_type);
			map.put("customer_name", customer_name);
			map.put("vin", vin);
			map.put("customer_phone", customer_phone);
			map.put("fleet_name", fleet_name);
			map.put("materialCode", materialCode);
			map.put("is_fleet", is_fleet);
			map.put("contract_no", contract_no);
			map.put("startDate", startDate);
			map.put("endDate", endDate);
			map.put("dealerCode", dealerCode);
			map.put("areaId", areaId);
			map.put("poseId", logonUser.getPoseId().toString());
			map.put("callcenterCheckStatus", callcenterCheckStatus);
			map.put("salesCheckStatus", salesCheckStatus);
			map.put("SERVICEACTIVITY_CHARACTOR", carCharctor);
			map.put("onlySndDealer", onlySndDealer);
			map.put("comm", comm);
			
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")): 1; // 处理当前页
			PageResult<Map<String, Object>> ps= SalesInfoQueryDAO.queryReportInfoList_CVS(map, Constant.PAGE_SIZE, curPage,logonUser);
			act.setOutData("ps", ps);
			
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "实销信息查询结果展示");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 实销信息审核
	 */
	public void salesInfoCheck(){
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);	
			String ids = request.getParamValue("ids");
			String comm = request.getParamValue("comm");
			act.setOutData("comm", comm);
		    act.setOutData("ids", ids);
			act.setForword(salesInfoCheck);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "实销信息审核初始化");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	public void salesInfoCheckLog(){
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);	
			String order_id = request.getParamValue("order_id");
			String comm = request.getParamValue("comm");
			act.setOutData("comm", comm);
			act.setOutData("order_id", order_id);
			act.setForword(salesInfoCheckLog);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "实销信息日志查询初始化");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	public void salesInfoCheckLogQuery(){
		act.getResponse().setContentType("application/json");
		try{
			String order_id = request.getParamValue("order_id");
			String comm = request.getParamValue("comm");

			ClientInforManageDao dao = ClientInforManageDao.getInstance();
			//分页方法 begin
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage"))	: 1; // 处理当前页	
				
			PageResult<Map<String,Object>> queryLog = dao.queryClientCheckInfoLog(order_id,comm,Constant.PAGE_SIZE,curPage);
			
			act.setOutData("ps", queryLog);
		} catch(Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"实销上报日志查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 实销审核保存
	 */
	public void save(){
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);	
			String comm = request.getParamValue("comm");
			String ids = request.getParamValue("ids");
			String checkRemark = request.getParamValue("checkRamark");
			String status = request.getParamValue("status");
			
			//TODO 需要先更新实销信息的数据状态
			
			List<Object> params = new ArrayList<Object>();
			StringBuffer sbSql = new StringBuffer();
			sbSql.append("UPDATE TM_VEHICLE\n");
			sbSql.append("   SET LIFE_CYCLE = 10321004\n");
			sbSql.append(" WHERE VEHICLE_ID IN (SELECT T.VEHICLE_ID\n");
			sbSql.append("                        FROM TT_DEALER_ACTUAL_SALES T\n");
			sbSql.append("                       WHERE T.ORDER_ID = ?\n");
			sbSql.append("                         AND T.IS_RETURN = 10041002\n");
			sbSql.append("                         AND T.CALLCENTER_CHECK_STATUS = 18011002\n");
			sbSql.append("                         AND T.SALES_CHECK_STATUS = 19011002)"); 
			
			if(ids != null){
				String [] orderIds = ids.split(",");
				if(orderIds != null && orderIds.length > 0){
					SalesInfoQueryDAO dao = SalesInfoQueryDAO.getInstance();
					for (String orderId : orderIds) {
						//修改实销信息
						TtDealerActualSalesPO tsOld = new TtDealerActualSalesPO();
						tsOld.setOrderId(Long.parseLong(orderId));
						
						if("1".equals(comm)){
							//呼叫中心审核
							TtDealerActualSalesPO tsNew = new TtDealerActualSalesPO();
							tsNew.setUpdateBy(logonUser.getUserId());
							tsNew.setUpdateDate(new Date());
							tsNew.setCallcenterCheckStatus(Integer.parseInt(status));
							dao.update(tsOld, tsNew);
//							if (Integer.parseInt(status) == Constant.CALLCENTER_CHECK_STATUS_03) {
//								//如果驳回则修改车辆信息的车辆生命周期为经销商库存
//								List<Map<String, Object>> maps = dao.queryByOrderId(orderId);
//								if (maps != null && maps.size() > 0) {
//									TmVehiclePO tvOld = new TmVehiclePO();
//									tvOld.setVehicleId(Long.parseLong(maps.get(0).get("VEHICLE_ID")+""));
//									
//									TmVehiclePO tvNew = new TmVehiclePO();
//									tvNew.setLifeCycle(Constant.VEHICLE_LIFE_03);
//									tvNew.setUpdateBy(logonUser.getUserId());
//									tvNew.setUpdateDate(new Date());
//									dao.update(tvOld, tvNew);
//								}
//							}
						}else {
							//销售部审核
							TtDealerActualSalesPO tsNew = new TtDealerActualSalesPO();
							tsNew.setUpdateBy(logonUser.getUserId());
							tsNew.setUpdateDate(new Date());
							tsNew.setSalesCheckStatus(Integer.parseInt(status));
							tsNew.setIsStatus(1);
							dao.update(tsOld, tsNew);
//							if (Integer.parseInt(status) == Constant.SALESX_CHECK_STATUS_03) {
//								//如果驳回则修改车辆信息的车辆生命周期为经销商库存
//								List<Map<String, Object>> maps = dao.queryByOrderId(orderId);
//								if (maps != null && maps.size() > 0) {
//									TmVehiclePO tvOld = new TmVehiclePO();
//									tvOld.setVehicleId(Long.parseLong(maps.get(0).get("VEHICLE_ID")+""));
//									
//									TmVehiclePO tvNew = new TmVehiclePO();
//									tvNew.setLifeCycle(Constant.VEHICLE_LIFE_03);
//									tvNew.setUpdateBy(logonUser.getUserId());
//									tvNew.setUpdateDate(new Date());
//									dao.update(tvOld, tvNew);
//								}
//							}
						}
						
						// 更新车辆表状态
						params.clear();
						params.add(orderId);
						dao.update(sbSql.toString(), params);
						
						//插入审核日志
						TtDealerActualSalesLogPO tsLog = new TtDealerActualSalesLogPO();
						tsLog.setLogId(Long.parseLong(SequenceManager.getSequence("")));
						tsLog.setOrderId(Long.parseLong(orderId));
						tsLog.setUserId(logonUser.getUserId());
						tsLog.setOrgId(logonUser.getOrgId());
						SimpleDateFormat  formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
						Date dDate = formatter.parse(formatter.format(new Date()));
						tsLog.setLogDate(dDate);
						tsLog.setLogStatus(Integer.parseInt(status));
						tsLog.setRemark(checkRemark);
						tsLog.setCreateBy(logonUser.getUserId());
						tsLog.setCreateDate(dDate);
						tsLog.setUserName(logonUser.getName());
						tsLog.setLogType((long)Constant.ACTUAL_DEALER_SUBMIT_LOG_01);//设置日志类型
						dao.insert(tsLog);
					}
				}
				
			}
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "实销信息审核");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 实销信息修改
	 */
	public void updateSalesInfo() {
		AclUserBean logonUser = null;
		try {
			CheckVehicleDAO dao = CheckVehicleDAO.getInstance();
			
			RequestWrapper request = act.getRequest();
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			act.getSession().get(Constant.LOGON_USER);
			Date d = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			String date = sdf.format(d);
			act.setOutData("d", date);
			String vehicleId = CommonUtils.checkNull(request
					.getParamValue("vehicle_id"));

			// 1.查询“车辆资料”
			Map<String, Object> vehicleInfo = SalesReportDAO
					.getVehicleInfo(vehicleId);

			TtDealerActualSalesPO actualSalesPO = new TtDealerActualSalesPO();
			actualSalesPO.setVehicleId(Long.parseLong(vehicleId));
			actualSalesPO.setIsDel(0);
			List actualSalesPOList = dao.select(actualSalesPO);
			if (null != actualSalesPOList && actualSalesPOList.size() > 0) {
				// 2.查询车辆实销信息(TT_DEALER_ACTUAL_SALES)
				TtDealerActualSalesPO salesInfo = (TtDealerActualSalesPO) actualSalesPOList
						.get(0);
				SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
				if(!XHBUtil.IsNull(salesInfo.getInvoiceDate()))act.setOutData("invoiceDate", sdfDate.format(salesInfo.getInvoiceDate()));//开票日期
				if(!XHBUtil.IsNull(salesInfo.getInsuranceDate()))act.setOutData("insuranceDate", sdfDate.format(salesInfo.getInsuranceDate()));//保险日期
				if(!XHBUtil.IsNull(salesInfo.getConsignationDate()))act.setOutData("consignationDate", sdfDate.format(salesInfo.getConsignationDate()));//车辆交付日期
				// 查询大客户信息
				String isFleet = salesInfo.getIsFleet().toString();
				if(null != isFleet && isFleet.equals(Constant.IF_TYPE_YES + "")){
					Long fleetId = salesInfo.getFleetId(); 						// 集团客户id
					SalesReportDAO sDao = SalesReportDAO.getInstance();
					Map<String, Object> srMap = sDao.queryFleetInfo(fleetId);
					if (srMap != null) {
						act.setOutData("fleetId", srMap.get("FLEET_ID"));
						act.setOutData("fleetName", srMap.get("FLEET_NAME"));
						act.setOutData("type", srMap.get("TYPE"));
					}
					
					TtFleetContractPO contractPO = new TtFleetContractPO();
					Long contractId = salesInfo.getContractId(); 					// 集团客户合同id
					if(contractId != null){
						contractPO.setContractId(contractId);
						List conList = dao.select(contractPO);
						if (null != conList && conList.size() > 0) {
							String contractNo = ((TtFleetContractPO) conList.get(0)).getContractNo(); // 集团客户合同
							act.setOutData("contractNo", contractNo==null?"":contractNo);
						}
					}			
				}

				// 3.查询客户信息(TT_CUSTOMER)
				Long ctmId = salesInfo.getCtmId();
				TtCustomerPO customerPO = new TtCustomerPO();
				customerPO.setCtmId(ctmId);
				List customerPOList = dao.select(customerPO);
				if (null != customerPOList && customerPOList.size() > 0) {
					TtCustomerPO customerInfo = (TtCustomerPO) customerPOList
							.get(0);
					act.setOutData("customerInfo", customerInfo);
				}
				act.setOutData("salesInfo", salesInfo);
				
				//查询审核日志
				List<Map<String, Object>> salesLogList = dao.querySalesLogList(salesInfo.getOrderId()+"");
				if(salesLogList != null && salesLogList.size() > 0){
					act.setOutData("salesLogList", salesLogList);
				}
				SalesConsultantDAO scDao = SalesConsultantDAO.getInstance();
				//销售顾问
				act.setOutData("scList", scDao.getSalesConsultantById(salesInfo.getSalesConId()));
			}
			act.setOutData("vehicleInfo", vehicleInfo);
			act.setForword(updateReportCVS);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "实销信息上报:填写上报信息");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 保存实销修改的信息
	 */
	public void saveUpdateSalesInfo(){
		AclUserBean logonUser = null;
		try {
			CheckVehicleDAO dao = CheckVehicleDAO.getInstance();
			
			RequestWrapper request = act.getRequest();
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			act.getSession().get(Constant.LOGON_USER);
			
			String oldOrderId = request.getParamValue("orderId");
			if (XHBUtil.IsNull(oldOrderId)) {
				act.setOutData("message", "实销信息为空!");
				return;
			}
			
			String vehicle_id = request.getParamValue("vehicle_id");
			String isUnCust = CommonUtils.checkNull(request.getParamValue("isUnCust")); //是否异地客户
			String isForBusi = CommonUtils.checkNull(request.getParamValue("isForBusi")); //是否营运车辆
			Map<String, Object> vehicleInfo = dao.getDealerId(vehicle_id);
			if (vehicle_id == null || "".equals(vehicle_id)) {
				String vin_rpc = request.getParamValue("vin");
				vehicleInfo = dao.getDealerIdRpc(vehicle_id, vin_rpc);
				vehicle_id = vehicleInfo.get("VEHICLE_ID").toString();
			}
			
			Map<String, String> infoMap = saveReportInfo_getJSPInfo(dao);
			if (0 == infoMap.size()) {
				act.setOutData("message", "未找到车辆信息!");
				return;
			}

			String mortgageType = CommonUtils.checkNull(request
					.getParamValue("mortgageType"));// 按揭类型

			String FirstPrice = CommonUtils.checkNull(request
					.getParamValue("FirstPrice"));// 首付比例

			String LoansType = CommonUtils.checkNull(request
					.getParamValue("LoansType"));// 贷款方式

			String LoansYear = CommonUtils.checkNull(request
					.getParamValue("LoansYear"));// 贷款年限
			/*
			 * 查询： 1.车厂公司ID： OEM_COMPANY_ID 2.经销商公司ID： DLR_COMPANY_ID
			 * 3.经销商渠道： DEALER_ID
			 */
			String s_dealer_id = ""; // 经销商渠道
			/**
			 * 增加经销商名称和简称冗余显示<经销商更名>
			 * wizard_lee
			 * 2014-04-29
			 */
			String s_dealer_name="";
			String s_dealer_Shortname="";
			String s_oem_company_id = ""; // 车厂公司ID
			String s_dlr_company_id = ""; // 经销商公司ID
			TmVehiclePO tmVehiclePO = new TmVehiclePO();
			tmVehiclePO.setVehicleId(Long.parseLong(infoMap
					.get("vehicle_id")));
			List vehicleList = dao.select(tmVehiclePO);
			if (null != vehicleList && vehicleList.size() > 0) {
				// 取经销商ID
				s_dealer_id = ((TmVehiclePO) vehicleList.get(0))
						.getDealerId() + "";
				s_dealer_name = ((TmVehiclePO) vehicleList.get(0))
				.getDealerName();
				s_dealer_Shortname = ((TmVehiclePO) vehicleList.get(0))
				.getDealerShortname();
				TmDealerPO dealerPO = new TmDealerPO();
				dealerPO.setDealerId(((TmVehiclePO) vehicleList.get(0))
						.getDealerId());
				List dealerList = dao.select(dealerPO);
				if (null != dealerList && dealerList.size() > 0) {
					s_oem_company_id = ((TmDealerPO) dealerList.get(0))
							.getOemCompanyId() + "";
					s_dlr_company_id = ((TmDealerPO) dealerList.get(0))
							.getCompanyId() + "";
				}
			}

			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

			TtCustomerPO customerPO = new TtCustomerPO();
			Long ctmId = Long.parseLong(SequenceManager.getSequence(""));
			customerPO.setCtmId(ctmId);
			customerPO.setDlrCompanyId(Long.parseLong(s_dlr_company_id));
			customerPO.setOemCompanyId(Long.parseLong(s_oem_company_id));

			String customer_type = infoMap.get("customer_type"); // 客户类型
			String sel_cus_type = infoMap.get("sel_cus_type"); // 新客户/老客户

			if (null != customer_type && !"".equals(customer_type)) {
				/*
				 * 一、如果“客户类型”是“公司客户” 1.向（TT_CUSTOMER实销客户表）写入“公司客户信息”
				 * 2.如果“是否大客户
				 * ”为“是”，向（TT_DEALER_ACTUAL_SALES车辆实销表）写入“大客户名称”和“大客户合同”
				 */
				if (Constant.CUSTOMER_TYPE_02.toString().equals(
						customer_type)) {
					customerPO.setCtmType(Constant.CUSTOMER_TYPE_02);
					customerPO.setCtmName(infoMap.get("company_name")
							.trim()); // 向客户名称字段中插入公司名称
					customerPO.setCompanyName(infoMap.get("company_name")
							.trim()); // 公司名称
					if (null != infoMap.get("company_s_name")
							&& !"".equals(infoMap.get("company_s_name"))) {
						customerPO.setCompanySName(infoMap.get(
								"company_s_name").trim()); // 公司简称
					}
					customerPO.setCtmForm(Integer.parseInt(infoMap
							.get("myctm_form")));// 客户来源
					customerPO.setCompanyPhone(infoMap.get("company_phone")
							.trim()); // 公司电话
					customerPO.setLevelId(Integer.parseInt(infoMap
							.get("level_id"))); // 公司规模
					customerPO
							.setKind(Integer.parseInt(infoMap.get("kind"))); // 公司性质
					if (null != infoMap.get("vehicle_num")
							&& !"".equals(infoMap.get("vehicle_num"))) {
						customerPO.setVehicleNum(Integer.parseInt(infoMap
								.get("vehicle_num").trim())); // 目前车辆数
					}

					/*
					 * 二、如果“客户类型”是“个人客户”
					 * 1.如果实销客户为“新客户”，向（TT_CUSTOMER实销客户表）新增客户信息，否则只写入“老客户id”
					 * 2.
					 * 如果“是否大客户”为“是”，向（TT_DEALER_ACTUAL_SALES车辆实销表）写入“大客户名称
					 * ”和“大客户合同”
					 */
				} else if (Constant.CUSTOMER_TYPE_01.toString().equals(
						customer_type)) {
					//if (Constant.IS_OLD_CTM_01.toString().equals(
							//sel_cus_type)) {
						customerPO.setCtmType(Constant.CUSTOMER_TYPE_01); // 客户类型
						customerPO.setCtmName(infoMap.get("ctm_name")
								.trim()); // 客户名称
						if (infoMap.get("sex") != null) {
							customerPO.setSex(Integer.parseInt(infoMap
									.get("sex"))); // 性别
						}
						customerPO.setCardType(Integer.parseInt(infoMap
								.get("card_type"))); // 证件类型
						customerPO.setCardNum(infoMap.get("card_num")
								.trim()); // 证件号码
						customerPO.setMainPhone(infoMap.get("main_phone")
								.trim()); // 主要联系电话
						if (null != infoMap.get("other_phone")
								&& !"".equals(infoMap.get("other_phone"))) {
							customerPO.setOtherPhone(infoMap.get(
									"other_phone").trim()); // 其他联系电话
						}
						if (null != infoMap.get("email")
								&& !"".equals(infoMap.get("email"))) {
							customerPO
									.setEmail(infoMap.get("email").trim()); // 电子邮件
						}
						if (null != infoMap.get("post_code")
								&& !"".equals(infoMap.get("post_code"))) {
							customerPO.setPostCode(infoMap.get("post_code")
									.trim()); // 邮政编码
						}
						if (Utility.testString(infoMap.get("birthday"))) {
							customerPO.setBirthday(dateFormat.parse(infoMap
									.get("birthday"))); // 生日
						}
						if (Utility.testString(infoMap.get("ctm_form"))) {
							customerPO.setCtmForm(Integer.parseInt(infoMap
									.get("ctm_form"))); // 客户来源
						}
						if (Utility.testString(infoMap.get("income"))) {
							customerPO.setIncome(Integer.parseInt(infoMap
									.get("income"))); // 家庭月收入
						}
						if (Utility.testString(infoMap.get("education"))) {
							customerPO.setEducation(Integer
									.parseInt(infoMap.get("education"))); // 教育程度
						}
						if (Utility.testString(infoMap.get("industry"))) {
							customerPO.setIndustry(Integer.parseInt(infoMap
									.get("industry"))); // 所在行业
						}
						if (Utility.testString(infoMap.get("is_married"))) {
							customerPO.setIsMarried(Integer
									.parseInt(infoMap.get("is_married"))); // 婚姻状况
						}
						if (Utility.testString(infoMap.get("profession"))) {
							customerPO.setProfession(Integer
									.parseInt(infoMap.get("profession"))); // 职业
						}
						if (null != infoMap.get("job")
								&& !"".equals(infoMap.get("job"))) {
							customerPO.setJob(infoMap.get("job").trim()); // 职务
						}
						if (null != infoMap.get("custStars")
								&& !"".equals(infoMap.get("custStars"))) {
							customerPO.setGuestStars((infoMap.get("custStars").trim())); // 星级
						}
					//}
					
				}

//					if (Constant.CUSTOMER_TYPE_02.toString().equals(
//							customer_type)
//							|| (Constant.CUSTOMER_TYPE_01.toString().equals(
//									customer_type) && Constant.IS_OLD_CTM_01
//									.toString().equals(sel_cus_type))) {
				if (Constant.CUSTOMER_TYPE_02.toString().equals(
				customer_type)
				|| (Constant.CUSTOMER_TYPE_01.toString().equals(
						customer_type))) {
					String isSecond = CommonUtils.checkNull(request
							.getParamValue("secondeVeh"));

					customerPO.setIsSecond(new Long(isSecond));

					if (null != infoMap.get("province")
							&& !"".equals(infoMap.get("province"))) {
						customerPO.setProvince(Long.parseLong(infoMap
								.get("province"))); // 省份
					}
					if (null != infoMap.get("city")
							&& !"".equals(infoMap.get("city"))) {
						customerPO.setCity(Long.parseLong(infoMap
								.get("city"))); // 地级市
					}
					if (null != infoMap.get("district")
							&& !"".equals(infoMap.get("district"))) {
						customerPO.setTown(Long.parseLong(infoMap
								.get("district"))); // 区、县
					}
					if (null != infoMap.get("custStars")
							&& !"".equals(infoMap.get("custStars"))) {
						customerPO.setGuestStars((infoMap.get("custStars").trim())); // 星级
					}
					customerPO.setAddress(infoMap.get("address").trim());
					customerPO.setCreateBy(logonUser.getUserId());
					customerPO.setCreateDate(new Date());
					//jony rj 2013-7-16 update
					//老客户（2013-7-16 RJ insert）
					if(Constant.CUSTOMER_TYPE_01.toString().equals(customer_type) && Constant.IS_OLD_CTM_02.toString().equals(sel_cus_type)){//老客户 -个人客户
						customerPO.setCtmId(null);//防止修改
						TtCustomerPO customerPO1 = new TtCustomerPO();
						customerPO1.setCtmId(Long.parseLong(infoMap
								.get("oldCustomerId")));
						dao.update(customerPO1, customerPO);
					}else{
						String oldCtmId = request.getParamValue("ctmId");
						if (!XHBUtil.IsNull(oldCtmId)) {
							//修改
							TtCustomerPO customerPO1 = new TtCustomerPO();
							customerPO1.setCtmId(Long.parseLong(oldCtmId));
							
							customerPO.setCtmId(null);//防止修改
							dao.update(customerPO1, customerPO);
						}else {
							dao.insert(customerPO);
						}
					}
				}
			}

			// 其他联系人

			Map<String, String[]> linkManInfo = getLinkManInfo();
			if (null != sel_cus_type
					&& sel_cus_type.equals(Constant.IS_OLD_CTM_01 + "")) {
				String oldCtmId = request.getParamValue("ctmId");
				if (!XHBUtil.IsNull(oldCtmId)) {
					saveReportInfo_AddorUpdateLinkMan(Long.parseLong(oldCtmId) + "", linkManInfo,
							s_dlr_company_id, s_oem_company_id,dao);						
				} else {
					saveReportInfo_AddorUpdateLinkMan(ctmId + "", linkManInfo,
							s_dlr_company_id, s_oem_company_id,dao);	
				}
			}
			if (null != sel_cus_type
					&& sel_cus_type.equals(Constant.IS_OLD_CTM_02 + "")) {
				saveReportInfo_AddorUpdateLinkMan(
						infoMap.get("oldCustomerId"), linkManInfo,
						s_dlr_company_id, s_oem_company_id,dao);
			}

			// 实销信息
			TtDealerActualSalesPO actualSalesPO = new TtDealerActualSalesPO();
			if (null != mortgageType && !"".equals(mortgageType)) {
				actualSalesPO.setMortgageType(Long.valueOf(mortgageType));// 按揭类型
			}
			if (null != FirstPrice && !"".equals(FirstPrice)) {

				actualSalesPO
						.setShoufuRatio((Double.valueOf(FirstPrice) / 100));// 首付比例
			}

			if (null != LoansType && !"".equals(LoansType)) {
				actualSalesPO.setLoansType(Long.valueOf(LoansType));// 贷款方式
			}

			if (null != LoansYear && !"".equals(LoansYear)) {
				actualSalesPO.setLoansYear(Long.valueOf(LoansYear));// 贷款年限
			}

			actualSalesPO
					.setKnowAddress(infoMap.get("know_Address").trim());
			actualSalesPO.setSalesAddress(infoMap.get("sales_Address")
					.trim());
			actualSalesPO.setSalesReson(infoMap.get("sales_Reson").trim());
			actualSalesPO.setOemCompanyId(Long.parseLong(s_oem_company_id)); // 车厂公司ID
			actualSalesPO.setDlrCompanyId(Long.parseLong(s_dlr_company_id)); // 经销商公司ID
			actualSalesPO.setDealerId(Long.parseLong(s_dealer_id)); // 经销商渠道
			actualSalesPO.setDealerName(s_dealer_name);			//经销商更名名称字段冗余
			actualSalesPO.setDealerShortname(s_dealer_Shortname);	//经销商更名简称字段冗余
			actualSalesPO.setVehicleId(Long.parseLong(infoMap
					.get("vehicle_id")));// 车辆ID
			if(!CommonUtils.checkNull(infoMap.get("salesCon")).equals("")){
				actualSalesPO.setSalesConId(Long.parseLong(infoMap.get("salesCon")));
			} else {
				throw new IllegalArgumentException("销售顾问不能为空!");
			}
			
			if(!CommonUtils.checkNull(infoMap.get("saleType")).equals("")) {
				actualSalesPO.setSaleType(Integer.parseInt(infoMap.get("saleType")));
			}
			
			if (null != infoMap.get("vehicle_no")
					&& !"".equals(infoMap.get("vehicle_no"))) {
				actualSalesPO
						.setVehicleNo(infoMap.get("vehicle_no").trim()); // 车牌号
			}
			if (null != infoMap.get("contractNo")
					&& !"".equals(infoMap.get("contractNo"))) {
				actualSalesPO.setContractNo(infoMap.get("contractNo")
						.trim()); // 合同编号
			}
			actualSalesPO.setInvoiceDate(dateFormat.parse(infoMap
					.get("invoice_date"))); // 开票日期
			actualSalesPO.setInvoiceNo(infoMap.get("invoice_no")); // 发票编号
			if (null != infoMap.get("insurance_company")
					&& !"".equals(infoMap.get("insurance_company"))) {
				actualSalesPO.setInsuranceCompany(infoMap.get(
						"insurance_company").trim()); // 保险公司
			}
			if (null != infoMap.get("insurance_date")
					&& !"".equals(infoMap.get("insurance_date"))) {
				actualSalesPO.setInsuranceDate(dateFormat.parse(infoMap
						.get("insurance_date"))); // 保险日期
			}
			actualSalesPO.setConsignationDate(dateFormat.parse(infoMap
					.get("consignation_date"))); // 车辆交付日期
			if (null != infoMap.get("miles")
					&& !"".equals(infoMap.get("miles"))) {
				actualSalesPO.setMiles(Float.parseFloat(infoMap
						.get("miles").trim())); // 交付时公里数
			}
			actualSalesPO.setPayment(Integer.parseInt(infoMap
					.get("payment"))); // 付款方式
			actualSalesPO.setPrice(Double.parseDouble(infoMap.get("price")
					.trim())); // 价格
			if (null != infoMap.get("memo")
					&& !"".equals(infoMap.get("memo"))) {
				actualSalesPO.setMemo(infoMap.get("memo").trim()); // 备注
			}
			actualSalesPO.setIsReturn(Constant.IF_TYPE_NO); // 是否退车
			// 如果是大客户
			String is_fleet = infoMap.get("is_fleet");
			if (null != is_fleet
					&& is_fleet.equals(Constant.IF_TYPE_YES + "")) {
				String fleet_id = infoMap.get("fleet_id"); // 大客户id
				String fleet_contract_no_id = infoMap
						.get("fleet_contract_no_id"); // 大客户合同id
				String fleet_contract_no = infoMap.get("fleet_contract_no");
				actualSalesPO.setIsFleet(Constant.IF_TYPE_YES); // 是否是大客户
				actualSalesPO.setFleetId(Long.parseLong(fleet_id)); // 大客户ID
				if (fleet_contract_no_id != null
						&& !fleet_contract_no_id.equals("")) {
					actualSalesPO.setContractId(Long
							.parseLong(fleet_contract_no_id)); // 大客户合同ID
				}
				if (fleet_contract_no != null
						&& !fleet_contract_no.equals("")) {
					actualSalesPO.setContractNo(fleet_contract_no); // 大客户合同号
				}
				actualSalesPO
						.setFleetStatus(Constant.Fleet_SALES_CHECK_STATUS_01); // 未审核
			} else {
				actualSalesPO.setIsFleet(Constant.IF_TYPE_NO);
			}
			// 如果是新客户
			if (null != sel_cus_type
					&& sel_cus_type.equals(Constant.IS_OLD_CTM_01 + "")) {
				String oldCtmId = request.getParamValue("ctmId");
				if (!XHBUtil.IsNull(oldCtmId)) {
					actualSalesPO.setCtmId(Long.parseLong(oldCtmId));
				}else {
					actualSalesPO.setCtmId(ctmId);
				}
			}
			// 如果是老客户
			if (null != sel_cus_type
					&& sel_cus_type.equals(Constant.IS_OLD_CTM_02 + "")) {
				actualSalesPO.setCtmId(Long.parseLong(infoMap
						.get("oldCustomerId")));
			}
				
			actualSalesPO.setSalesDate(new Date());
			actualSalesPO.setCreateDate(new Date());
			actualSalesPO.setCreateBy(logonUser.getUserId());
			if (Utility.testString(infoMap.get("carCharactor"))) {
				actualSalesPO.setCarCharactor(Integer.parseInt(infoMap
						.get("carCharactor")));// 车辆性质
			}
			if(!"".equals(isUnCust)){
				actualSalesPO.setIsUnCust(Integer.valueOf(isUnCust));
			}else{
				actualSalesPO.setIsUnCust(Integer.valueOf(Constant.IF_TYPE_NO));
			}
			if(!"".equals(isForBusi)){
				actualSalesPO.setIsForBusi(Integer.valueOf(isForBusi));
			}
			/*
			 * 修改实销信息
			 */
			actualSalesPO.setSoNo(CommonUtils.checkNull(request
					.getParamValue("soNo")));// 下端通过URL请求
//			actualSalesPO.setSalesDate(new Date());
			
			TtDealerActualSalesPO oldTs = new TtDealerActualSalesPO();
			oldTs.setOrderId(Long.parseLong(oldOrderId));
			dao.update(oldTs, actualSalesPO);
			
			// 向TT_VS_VHCL_CHNG写入变更日志
			TtVsVhclChngPO chngPO = new TtVsVhclChngPO();
			Long vhclChangeId = Long.parseLong(SequenceManager
					.getSequence(""));
			chngPO.setVhclChangeId(vhclChangeId); // 改变序号
			chngPO.setVehicleId(Long.parseLong(infoMap.get("vehicle_id"))); // 车辆ID
			chngPO.setOrgType(logonUser.getOrgType()); // 组织类型
			chngPO.setOrgId(logonUser.getOrgId()); // 组织ID
			chngPO.setDealerId(Long.parseLong(s_dealer_id)); // 经销商ID
			chngPO.setChangeCode(Constant.SALES_STATUS_CHANGE_TYPE); // 改变类型:销售状态更改
			chngPO.setChangeName(Constant.SALES_STATUS_CHANGE_TYPE_04
					.toString()); // 改变名称:实效上报
			chngPO.setChangeDate(new Date()); // 改变时间
			if (null != infoMap.get("memo")
					&& !"".equals(infoMap.get("memo"))) {
				chngPO.setChangeDesc(infoMap.get("memo").trim()); // 改变描述
			}
			chngPO.setCreateDate(new Date()); // 记录创建日期
			chngPO.setCreateBy(logonUser.getUserId()); // 记录创建者
			dao.insert(chngPO);
			act.setOutData("message", "操作成功");
		} catch (RuntimeException e) {
			logger.error(logonUser, e);
			act.setException(e);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "实销信息修改:修改");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * FUNCTION : 实销信息修改:得到页面信息
	 * 
	 * @param :
	 * @return :
	 * @throws : LastUpdate : 2010-6-7
	 */
	public Map<String, String> saveReportInfo_getJSPInfo(CheckVehicleDAO dao) {
		AclUserBean logonUser = null;
		Map<String, String> infoMap = new HashMap<String, String>();
		try {
			RequestWrapper request = act.getRequest();
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			act.getSession().get(Constant.LOGON_USER);

			/*
			 * 1.销售信息
			 */
			String vehicle_no = CommonUtils.checkNull(request
					.getParamValue("vehicle_no")); // 车牌号
			String contractNo = CommonUtils.checkNull(request
					.getParamValue("contract_no")); // 合同编号
			String invoice_date = CommonUtils.checkNull(request
					.getParamValue("invoice_date")); // 开票日期
			String invoice_no = CommonUtils.checkNull(request
					.getParamValue("invoice_no")); // 发票编号
			String insurance_company = CommonUtils.checkNull(request
					.getParamValue("insurance_company")); // 保险公司
			String insurance_date = CommonUtils.checkNull(request
					.getParamValue("insurance_date")); // 保险日期
			String consignation_date = CommonUtils.checkNull(request
					.getParamValue("consignation_date")); // 车辆交付日期
			String miles = CommonUtils
					.checkNull(request.getParamValue("miles")); // 交付时公里数
			String payment = CommonUtils.checkNull(request
					.getParamValue("payment")); // 付款方式
			String price = CommonUtils
					.checkNull(request.getParamValue("price")); // 价格
			String memo = CommonUtils.checkNull(request.getParamValue("memo")); // 备注
			String carCharactor = CommonUtils.checkNull(request
					.getParamValue("carCharactor")); // 车辆性质
			String salesCon = CommonUtils.checkNull(request
					.getParamValue("salesCon")); // 销售顾问
			String saleType = CommonUtils.checkNull(request.getParamValue("sale_type"));

			/*
			 * 2.客户类型
			 */
			String customer_type = CommonUtils.checkNull(request
					.getParamValue("customer_type")); // 客户类型
			String is_fleet = CommonUtils.checkNull(request
					.getParamValue("is_fleet")); // 是否大客户
			String fleet_id = CommonUtils.checkNull(request
					.getParamValue("fleet_id")); // 大客户id
			String fleet_contract_no_id = CommonUtils.checkNull(request
					.getParamValue("fleet_contract_no_id"));// 大客户合同id
			String fleet_contract_no = CommonUtils.checkNull(request
					.getParamValue("fleet_contract_no")); // 大客户合同
			/*
			 * 3.公司客户信息
			 */
			String company_name = CommonUtils.checkNull(request
					.getParamValue("company_name")); // 公司名称
			String company_s_name = CommonUtils.checkNull(request
					.getParamValue("company_s_name")); // 公司简称
			String company_phone = CommonUtils.checkNull(request
					.getParamValue("company_phone")); // 公司电话
			String level_id = CommonUtils.checkNull(request
					.getParamValue("level_id")); // 公司规模
			String kind = CommonUtils.checkNull(request.getParamValue("kind")); // 公司性质
			String vehicle_num = CommonUtils.checkNull(request
					.getParamValue("vehicle_num")); // 目前车辆数
			String myctm_form = CommonUtils.checkNull(request
					.getParamValue("myctm_form"));// 客户来源
			/*
			 * 4.个人客户信息
			 */
			String ctm_name = CommonUtils.checkNull(request
					.getParamValue("ctm_name")); // 客户姓名
			String sex = CommonUtils.checkNull(request.getParamValue("sex")); // 性别
			String card_type = CommonUtils.checkNull(request
					.getParamValue("card_type")); // 证件类别
			String card_num = CommonUtils.checkNull(request
					.getParamValue("card_num")); // 证件号码
			String main_phone = CommonUtils.checkNull(request
					.getParamValue("main_phone")); // 主要联系电话
			String other_phone = CommonUtils.checkNull(request
					.getParamValue("other_phone")); // 其他联系电话
			String email = CommonUtils
					.checkNull(request.getParamValue("email")); // 电子邮件
			String post_code = CommonUtils.checkNull(request
					.getParamValue("post_code")); // 邮编
			String birthday = CommonUtils.checkNull(request
					.getParamValue("birthday")); // 出生年月
			String ctm_form = CommonUtils.checkNull(request
					.getParamValue("ctm_form")); // 客户来源
			String income = CommonUtils.checkNull(request
					.getParamValue("income")); // 家庭月收入
			String education = CommonUtils.checkNull(request
					.getParamValue("education")); // 教育程度
			String industry = CommonUtils.checkNull(request
					.getParamValue("industry")); // 所在行业
			String is_married = CommonUtils.checkNull(request
					.getParamValue("is_married")); // 婚姻状况
			String profession = CommonUtils.checkNull(request
					.getParamValue("profession")); // 职业
			String job = CommonUtils.checkNull(request.getParamValue("job")); // 职务
			String province = CommonUtils.checkNull(request
					.getParamValue("province")); // 省份
			String city = CommonUtils.checkNull(request.getParamValue("city")); // 地级市
			String district = CommonUtils.checkNull(request
					.getParamValue("district")); // 区、县
			String address = CommonUtils.checkNull(request
					.getParamValue("address")); // 详细地址
			String knowAddress = CommonUtils.checkNull(request
					.getParamValue("knowaddress")); // 了解途径
			String salesReson = CommonUtils.checkNull(request
					.getParamValue("salesreson")); // 购买原因
			String salesAddress = CommonUtils.checkNull(request
					.getParamValue("salesaddress")); // 购买用途
			String custStars = CommonUtils.checkNull(request
					.getParamValue("custStars"));
			/*
			 * 5.sel_cus_type：新客户/老客户
			 */
			String sel_cus_type = CommonUtils.checkNull(request
					.getParamValue("sel_cus_type")); // 用户选择的类型:新、老客户
			// String initialIsOldCtm =
			// CommonUtils.checkNull(request.getParamValue("initialIsOldCtm"));
			// //保存后的类型
			String oldCustomerId = CommonUtils.checkNull(request
					.getParamValue("oldCustomerId")); // 用户选择的老客户id
			/*
			 * 6.实销id
			 */
			// String order_OldId =
			// CommonUtils.checkNull(request.getParamValue("ORDER_ID"));

			// 上报车辆id
			String vehicle_id = CommonUtils.checkNull(request
					.getParamValue("vehicle_id"));

			/**
			 * add by liuqiang vin从下端接口传入,后面的代码用到vehicle_id,这里用vin查出vehicle_id
			 */
			String vin = CommonUtils.checkNull(request.getParamValue("vin"));
			if (!"".equals(vin)) {
				TmVehiclePO tmVehiclePO = new TmVehiclePO();
				tmVehiclePO.setVin(vin);
				
				/*****add by liuxh 20131108判断车架号不能为空*****/
				CommonUtils.jugeVinNull(vin);
				/*****add by liuxh 20131108判断车架号不能为空*****/
				
				List<TmVehiclePO> vehicleList = dao.select(tmVehiclePO);
				if (null != vehicleList && vehicleList.size() > 0) {
					vehicle_id = String.valueOf(vehicleList.get(0)
							.getVehicleId());
				} else {
					throw new IllegalArgumentException("没有找到VIN:" + vin);
				}
			}

			// Josn信息
			String isJson = CommonUtils.checkNull(request
					.getParamValue("isJson"));

			infoMap.put("vehicle_no", vehicle_no);
			infoMap.put("contractNo", contractNo);
			infoMap.put("invoice_date", invoice_date);
			infoMap.put("invoice_no", invoice_no);
			infoMap.put("insurance_company", insurance_company);
			infoMap.put("insurance_date", insurance_date);
			infoMap.put("consignation_date", consignation_date);
			infoMap.put("miles", miles);
			infoMap.put("payment", payment);
			infoMap.put("price", price);
			infoMap.put("memo", memo);
			infoMap.put("customer_type", customer_type);
			infoMap.put("is_fleet", is_fleet);
			infoMap.put("fleet_id", fleet_id);
			infoMap.put("fleet_contract_no_id", fleet_contract_no_id);
			infoMap.put("company_name", company_name);
			infoMap.put("company_s_name", company_s_name);
			infoMap.put("company_phone", company_phone);
			infoMap.put("level_id", level_id);
			infoMap.put("kind", kind);
			infoMap.put("vehicle_num", vehicle_num);
			infoMap.put("ctm_name", ctm_name);
			infoMap.put("sex", sex);
			infoMap.put("card_type", card_type);
			infoMap.put("card_num", card_num);
			infoMap.put("main_phone", main_phone);
			infoMap.put("other_phone", other_phone);
			infoMap.put("email", email);
			infoMap.put("post_code", post_code);
			infoMap.put("birthday", birthday);
			infoMap.put("ctm_form", ctm_form);
			infoMap.put("income", income);
			infoMap.put("education", education);
			infoMap.put("industry", industry);
			infoMap.put("is_married", is_married);
			infoMap.put("profession", profession);
			infoMap.put("job", job);
			infoMap.put("province", province);
			infoMap.put("city", city);
			infoMap.put("district", district);
			infoMap.put("address", address);
			infoMap.put("sel_cus_type", sel_cus_type);
			// infoMap.put("initialIsOldCtm", initialIsOldCtm);
			infoMap.put("oldCustomerId", oldCustomerId);
			// infoMap.put("order_OldId", order_OldId);
			// infoMap.put("ctmId_Old", ctmId_Old);
			infoMap.put("vehicle_id", vehicle_id);
			infoMap.put("fleet_contract_no", fleet_contract_no);
			infoMap.put("carCharactor", carCharactor);
			infoMap.put("myctm_form", myctm_form);
			infoMap.put("isJson", isJson);
			infoMap.put("sales_Reson", salesReson);
			infoMap.put("know_Address", knowAddress);
			infoMap.put("sales_Address", salesAddress);
			infoMap.put("salesCon", salesCon);
			infoMap.put("saleType", saleType);
			infoMap.put("custStars", custStars);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "实销信息上报:得到页面信息");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
		return infoMap;
	}
	
	/**
	 * 获取其他联系人
	 * @return
	 */
	public Map<String, String[]> getLinkManInfo() {
		AclUserBean logonUser = null;
		Map<String, String[]> linkManMap = new HashMap<String, String[]>();
		try {
			RequestWrapper request = act.getRequest();
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			act.getSession().get(Constant.LOGON_USER);

			String[] linkMan_name = request.getParamValues("linkMan_name");
			String[] linkMan_main_phone = request
					.getParamValues("linkMan_main_phone");
			String[] linkMan_other_phone = request
					.getParamValues("linkMan_other_phone");
			String[] linkMan_contract_reason = request
					.getParamValues("linkMan_contract_reason");
			linkManMap.put("linkMan_name", linkMan_name);
			linkManMap.put("linkMan_main_phone", linkMan_main_phone);
			linkManMap.put("linkMan_other_phone", linkMan_other_phone);
			linkManMap.put("linkMan_contract_reason", linkMan_contract_reason);

		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "实销信息修改:得到其他联系人信息");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
		return linkManMap;
	}
	
	/**
	 * FUNCTION : 实销信息修改:新增/修改其他联系人信息
	 * 
	 * @param : String ctm_id:新增时的客户id String ctm_id_new:修改后的客户id Map linkMap
	 *        :其他联系人信息 LastUpdate : 2010-6-17
	 */
	public void saveReportInfo_AddorUpdateLinkMan(String ctm_id, Map linkMap,
			String s_dlr_company_id, String s_oem_company_id,CheckVehicleDAO dao) {
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			act.getSession().get(Constant.LOGON_USER);
			String[] linkMan_name = (String[]) linkMap.get("linkMan_name");
			String[] linkMan_main_phone = (String[]) linkMap
					.get("linkMan_main_phone");
			String[] linkMan_other_phone = (String[]) linkMap
					.get("linkMan_other_phone");
			String[] linkMan_contract_reason = (String[]) linkMap
					.get("linkMan_contract_reason");
			TtLinkmanPO linkPO = new TtLinkmanPO();
			linkPO.setCtmId(Long.parseLong(ctm_id));
			dao.delete(linkPO);
			if (null != linkMan_name && linkMan_name.length > 0) {
				for (int i = 0; i < linkMan_name.length; i++) {
					TtLinkmanPO linkmanPO = new TtLinkmanPO();
					linkmanPO.setLmId(Long.parseLong(SequenceManager
							.getSequence(""))); // 联系人ID
					linkmanPO.setCtmId(Long.parseLong(ctm_id)); // 客户序列号

					if (null != s_dlr_company_id
							&& !"".equals(s_dlr_company_id)) {
						linkmanPO.setDlrCompanyId(Long
								.parseLong(s_dlr_company_id)); // 经销商公司
					}
					if (null != s_oem_company_id
							&& !"".equals(s_oem_company_id)) {
						linkmanPO.setOemCompanyId(Long
								.parseLong(s_oem_company_id)); // 车厂公司ID
					}
					linkmanPO.setName(linkMan_name[i].trim()); // 姓名
					linkmanPO.setMainPhone(linkMan_main_phone[i].trim()); // 主要联系电话
					if (null != linkMan_other_phone[i]
							&& !"".equals(linkMan_other_phone[i])) {
						linkmanPO.setOtherPhone(linkMan_other_phone[i].trim()); // 其他联系电话
					}
					if (null != linkMan_contract_reason[i]
							&& !"".equals(linkMan_contract_reason[i])) {
						linkmanPO.setContractReason(linkMan_contract_reason[i]
								.trim()); // 联系目的
					}
					linkmanPO.setCreateBy(logonUser.getUserId());
					linkmanPO.setCreateDate(new Date());
					dao.insert(linkmanPO);
				}
			}
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "实销信息上报:新增/修改其他联系人信息");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
}