package com.infodms.dms.actions.sales.customerInfoManage;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.actions.claim.auditing.rule.fixation.common.GuaranteePeriod;
import com.infodms.dms.actions.common.FileUploadManager;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.common.getCompanyId.GetOemcompanyId;
import com.infodms.dms.common.relation.DealerRelation;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.dao.common.CommonDAO;
import com.infodms.dms.dao.crm.taskmanage.TaskManageDao;
import com.infodms.dms.dao.sales.customerInfoManage.SalesReportDAO;
import com.infodms.dms.dao.sales.dealer.DealerInfoDao;
import com.infodms.dms.dao.sales.salesConsultant.SalesConsultantDAO;
import com.infodms.dms.dao.sales.storageManage.CheckVehicleDAO;
import com.infodms.dms.dao.sales.storageManage.VehicleInfoDAO;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.FsFileuploadPO;
import com.infodms.dms.po.TPcCustomerPO;
import com.infodms.dms.po.TPcDelvyPO;
import com.infodms.dms.po.TPcLinkManPO;
import com.infodms.dms.po.TPcOrderDetailPO;
import com.infodms.dms.po.TPcOrderPO;
import com.infodms.dms.po.TPcVechilePO;
import com.infodms.dms.po.TcCodePO;
import com.infodms.dms.po.TmCompanyPactPO;
import com.infodms.dms.po.TmDealerPO;
import com.infodms.dms.po.TmFleetPO;
import com.infodms.dms.po.TmVehiclePO;
import com.infodms.dms.po.TmVhclMaterialGroupPO;
import com.infodms.dms.po.TtCustomerPO;
import com.infodms.dms.po.TtDealerActualSalesPO;
import com.infodms.dms.po.TtFleetContractPO;
import com.infodms.dms.po.TtLinkmanPO;
import com.infodms.dms.po.TtVsIntegrationChangePO;
import com.infodms.dms.po.TtVsPersonPO;
import com.infodms.dms.po.TtVsVhclChngPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.csv.CsvWriterUtil;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.dms.chana.actions.OSB32;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.ActionContextExtend;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;
import com.infoservice.po3.bean.DynaBean;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

@SuppressWarnings("rawtypes")
public class SalesReport extends BaseDao {
	public Logger logger = Logger.getLogger(SalesReport.class);
	private ActionContext act = ActionContext.getContext();
	private static final CheckVehicleDAO dao = new CheckVehicleDAO();

	public static final CheckVehicleDAO getInstance() {
		return dao;
	}

	public final GuaranteePeriod guaranteePeriodDAO = new GuaranteePeriod();
	ResponseWrapper response = act.getResponse();
	private final String salesReportInitUrl = "/jsp/sales/customerInfoManage/salesReportInit.jsp";
	private final String toReportURL = "/jsp/sales/customerInfoManage/toReport.jsp";
	private final String toReportURL_CVS = "/jsp/sales/customerInfoManage/toReport_CVS.jsp";
	private final String customerListURL = "/jsp/sales/customerInfoManage/customerList.jsp";
	private final String linkManListURL = "/jsp/sales/customerInfoManage/linkManLis.jsp";
	private final String toAddLinkManURL = "/jsp/sales/customerInfoManage/toAddLinkMan.jsp";
	private final String otherLinkManListURL = "/jsp/sales/customerInfoManage/otherLinkManList.jsp";
	private final String toQueryFleetListURL = "/jsp/sales/customerInfoManage/fleetList.jsp";
	private final String toSalesManListURL = "/jsp/sales/customerInfoManage/salesManList.jsp";
	private final String toActivitiesListURL = "/jsp/sales/customerInfoManage/activitiesList.jsp";
	private final String toQueryFleetContractListURL = "/jsp/sales/customerInfoManage/fleetContractList.jsp";
	private final String salesDetailURL = "/jsp/sales/customerInfoManage/salesDetail.jsp";
	private final String toChangeSalesInfoURL = "/jsp/sales/customerInfoManage/toChangeSalesInfo.jsp";
	private final String toChangeSalesInfoURL_CVS = "/jsp/sales/customerInfoManage/toChangeSalesInfo_CVS.jsp";

	// 接口请求url
	private final String toReportURLRpc = "/jsp/sales/customerInfoManage/toReportRpc.jsp";
	private final String toReportCVSURLRpc = "/jsp/sales/customerInfoManage/toReportRpc_CVS.jsp";
	private final String toReportURLRpc_error = "/jsp/sales/customerInfoManage/toReportRpc_error.jsp";

	/**
	 * FUNCTION : 实销信息上报面初始化
	 * 
	 * @param :
	 * @return :
	 * @throws :
	 *             LastUpdate : 2010-6-7
	 */
	public void salesReportInit() {
		AclUserBean logonUser = null;
		try {
			ActionContextExtend atx = (ActionContextExtend) ActionContext.getContext();
			String reqURL = atx.getRequest().getContextPath();
			if ("/CVS-SALES".equals(reqURL.toUpperCase())) {
				act.setOutData("returnValue1", 1);
			} else {
				act.setOutData("returnValue1", 2);
			}
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			/*
			 * Long poseId = logonUser.getPoseId(); Long comId =
			 * logonUser.getCompanyId() ; List<Map<String, Object>> areaList =
			 * MaterialGroupManagerDao.getDealerId(comId.toString(),poseId.toString());
			 * String dealerIds__ = ""; for(int i=0; i<areaList.size();i++) {
			 * dealerIds__ += areaList.get(i).get("DEALER_ID").toString()+"," ; }
			 * dealerIds__ = dealerIds__.substring(0,(dealerIds__.length()-1)) ;
			 */// 当前用户职位对应的经销商ID
			// List<Map<String, Object>> areaList =
			// MaterialGroupManagerDao.getPoseIdBusiness(logonUser.getPoseId().toString());
			/*
			 * List<Map<String, Object>>
			 * areaList1=GetCommonArea.getMyCarArea(dealerIds__);
			 * act.setOutData("areaList", areaList1);
			 */
			Integer oemFlag = CommonUtils.getNowSys(Long.parseLong(logonUser.getOemCompanyId()));

			act.setOutData("oemFlag", oemFlag);

			act.setForword(salesReportInitUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "实销信息上报面初始化");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * FUNCTION : 实销信息上报:查询可上报车辆
	 * 
	 * @param :
	 * @return :
	 * @throws :
	 *             LastUpdate : 2010-6-7
	 */
	/*
	 * public void reportVehicleQuery(){ AclUserBean logonUser = null; try {
	 * 
	 * RequestWrapper request = act.getRequest(); logonUser = (AclUserBean)
	 * act.getSession().get(Constant.LOGON_USER);
	 * act.getSession().get(Constant.LOGON_USER); //得到物料组 String materialCode =
	 * CommonUtils.checkNull(request.getParamValue("materialCode")); //得到VIN
	 * String vin = CommonUtils.checkNull(request.getParamValue("vin")); if
	 * (null != vin && !"".equals(vin)) { vin = vin.trim(); } Long poseId =
	 * logonUser.getPoseId(); Long comId = logonUser.getCompanyId() ; List<Map<String,
	 * Object>> areaList =
	 * MaterialGroupManagerDao.getDealerId(comId.toString(),poseId.toString());
	 * String dealerIds__ = ""; for(int i=0; i<areaList.size();i++) {
	 * dealerIds__ += areaList.get(i).get("DEALER_ID").toString()+"," ; }
	 * dealerIds__ = dealerIds__.substring(0,(dealerIds__.length()-1)) ;
	 * //当前用户职位对应的经销商ID Integer curPage = request.getParamValue("curPage") !=
	 * null ? Integer .parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
	 * String areaId="";
	 * if(request.getParamValue("areaId")!=null&&request.getParamValue("areaId")!=""){
	 * areaId=request.getParamValue("areaId"); }else{
	 * areaId=GetCommonArea.getMyArea(); } PageResult<Map<String, Object>> ps =
	 * SalesReportDAO.getCanReportVehiclet(areaId,dealerIds__, materialCode,
	 * vin, Constant.PAGE_SIZE, curPage); act.setOutData("ps", ps); } catch
	 * (Exception e) { BizException e1 = new BizException(act, e,
	 * ErrorCodeConstant.QUERY_FAILURE_CODE, "实销信息上报:查询可上报车辆");
	 * logger.error(logonUser,e1); act.setException(e1); } }
	 */

	public void reportVehicleQuery() {
		AclUserBean logonUser = null;
		try {

			RequestWrapper request = act.getRequest();
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			act.getSession().get(Constant.LOGON_USER);
			// 得到物料组
			String materialCode = CommonUtils.checkNull(request.getParamValue("materialCode"));
			// 得到VIN
			String vin = CommonUtils.checkNull(request.getParamValue("vin"));
			if (null != vin && !"".equals(vin)) {
				vin = vin.trim();
			}

			String areaId = "";
			DealerRelation dr = new DealerRelation();
			//String dealerIds__ = dr.getDealerIdByPose(logonUser.getCompanyId(), logonUser.getPoseId()); // 当前用户职位对应的经销商ID  2017-8-7注释
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页

			PageResult<Map<String, Object>> ps = SalesReportDAO.getCanReportVehiclet(areaId, logonUser.getDealerId(), materialCode, vin, Constant.PAGE_SIZE, curPage);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "实销信息上报:查询可上报车辆");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * 
	 * @Title: toReportRpc
	 * @Description: TODO(下端url请求页面,实销信息上报)
	 * @throws
	 */
	public void toReportRpc() {
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			act.getSession().get(Constant.LOGON_USER);
			String para = CommonDAO.getPara(Constant.CHANA_SYS.toString());

			if (Constant.COMPANY_CODE_JC.equals(para.toUpperCase())) {
				act.setForword(toReportURLRpc);
			} else if (Constant.COMPANY_CODE_CVS.equals(para.toUpperCase())) {
				SalesConsultantDAO scDao = new SalesConsultantDAO();
				DealerRelation dr = new DealerRelation();

				Map<String, String> map = new HashMap<String, String>();
				map.put("dealerId", dr.getDealerIdsByCompany(logonUser.getCompanyId().toString()));
				map.put("status", Constant.SALES_CONSULTANT_STATUS_PASS.toString());

				act.setOutData("scList", scDao.salesConsultantListQuery(map));
				act.setForword(toReportCVSURLRpc);
			} else {
				throw new RuntimeException("判断当前系统的系统参数错误！");
			}
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "实销信息上报面初始化");
			act.setException(e1);
		}
	}

	/**
	 * FUNCTION : 实销信息上报:填写上报信息
	 * 
	 * @param :
	 * @return :
	 * @throws :
	 *             LastUpdate : 2010-6-7
	 */
	public void toReport() {
		AclUserBean logonUser = null;
		try {

			RequestWrapper request = act.getRequest();
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			act.getSession().get(Constant.LOGON_USER);

			String vehicleId = CommonUtils.checkNull(request.getParamValue("vehicle_id"));
			//add by yinshunhui 2014-11-26 start 
			String qkId=CommonUtils.checkNull(request.getParamValue("qkId"));//潜客id
			String qkOrderDetailId=CommonUtils.checkNull(request.getParamValue("qkOrderDetailId"));//订单字表id
			String delvDetailId=CommonUtils.checkNull(request.getParamValue("delvDetailId"));//交车明细ID
			//end
			

			// 1.查询“车辆资料”
			Map<String, Object> vehicleInfo = SalesReportDAO.getVehicleInfo(vehicleId);

			TtDealerActualSalesPO actualSalesPO = new TtDealerActualSalesPO();
			actualSalesPO.setVehicleId(Long.parseLong(vehicleId));
			List actualSalesPOList = dao.select(actualSalesPO);

			if (null != actualSalesPOList && actualSalesPOList.size() > 0) {
				// 2.查询车辆实销信息(TT_DEALER_ACTUAL_SALES)
				TtDealerActualSalesPO salesInfo = (TtDealerActualSalesPO) actualSalesPOList.get(0);

				// 查询大客户信息
				Long fleetId = salesInfo.getFleetId();
				if (null != fleetId && !"".equals(fleetId)) {
					TmFleetPO fleetPO = new TmFleetPO();
					fleetPO.setFleetId(fleetId);
					List fleetList = dao.select(fleetPO);
					if (null != fleetList && fleetList.size() > 0) {
						String fleet_name = ((TmFleetPO) fleetList.get(0)).getFleetName();
						act.setOutData("fleet_name", fleet_name);
					}
				}
				// 查询大客户合同信息
				Long contractId = salesInfo.getContractId();

				if (null != contractId && !"".equals(contractId)) {
					TtFleetContractPO fleetContractPO = new TtFleetContractPO();
					fleetContractPO.setContractId(contractId);
					List contractList = dao.select(fleetContractPO);
					if (null != contractList && contractList.size() > 0) {
						String contract_no = ((TtFleetContractPO) contractList.get(0)).getContractNo();
						act.setOutData("contract_no", contract_no);
					}
				}

				// 3.查询客户信息(TT_CUSTOMER)
				Long ctmId = salesInfo.getCtmId();
				TtCustomerPO customerPO = new TtCustomerPO();
				customerPO.setCtmId(ctmId);
				List customerPOList = dao.select(customerPO);
				if (null != customerPOList && customerPOList.size() > 0) {
					TtCustomerPO customerInfo = (TtCustomerPO) customerPOList.get(0);
					act.setOutData("customerInfo", customerInfo);
				}
				act.setOutData("salesInfo", salesInfo);
			}
			// 4.查询其他联系人信息
			act.setOutData("vehicleInfo", vehicleInfo);

			// 5.根据当前系统参数页面跳转判断
			String para = CommonDAO.getPara(Constant.CHANA_SYS.toString());

			if (Constant.COMPANY_CODE_JC.equals(para.toUpperCase())) {
				Date d=new Date ();
				SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd");
				String billDate=sf.format(d);
				act.setOutData("billDate", billDate);
				TPcCustomerPO tpc=new TPcCustomerPO();
				TPcOrderDetailPO tpod=new TPcOrderDetailPO();
				TPcOrderPO tpo=new TPcOrderPO();
				//add by yinshunhui 2014-11-26 start
				//判断数据是否来源于潜客部分
				if(qkId!=null&&!"".equals(qkId)){
					tpc.setCustomerId(new Long(qkId) );
					tpc=(TPcCustomerPO) dao.select(tpc).get(0);
				}
				if(qkOrderDetailId!=null&&!"".equals(qkOrderDetailId)){
					tpod.setOrderDetailId(new Long(qkOrderDetailId));
					tpod=(TPcOrderDetailPO) dao.select(tpod).get(0);
					tpo.setOrderId(tpod.getOrderId());
					tpo=(TPcOrderPO) dao.select(tpo).get(0);
				}
				act.setOutData("tpc", tpc);
				act.setOutData("tpod", tpod);
				act.setOutData("tpo", tpo);
				act.setOutData("delvDetailId", delvDetailId);
				//end
				act.setForword(toReportURL);
			} else if (Constant.COMPANY_CODE_CVS.equals(para.toUpperCase())) {
				SalesConsultantDAO scDao = new SalesConsultantDAO();
				DealerRelation dr = new DealerRelation();

				Map<String, String> map = new HashMap<String, String>();
				map.put("dealerId", dr.getDealerIdsByCompany(logonUser.getCompanyId().toString()));
				map.put("status", Constant.SALES_CONSULTANT_STATUS_PASS.toString());

				act.setOutData("scList", scDao.salesConsultantListQuery(map));
				act.setForword(toReportURL_CVS);
			} else {
				throw new RuntimeException("判断当前系统的系统参数错误！");
			}
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "实销信息上报:填写上报信息");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * FUNCTION : 实销信息上报:得到其他联系人信息
	 * 
	 * @param :
	 * @return :
	 * @throws :
	 *             LastUpdate : 2010-6-18
	 */
	public void queryOtherLinkMan() {
		AclUserBean logonUser = null;
		try {
			RequestWrapper request = act.getRequest();
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			act.getSession().get(Constant.LOGON_USER);

			String oldCustomerId = CommonUtils.checkNull(request.getParamValue("oldCustomerId"));
			if (null != oldCustomerId && !"".equals(oldCustomerId) && !"0".equals(oldCustomerId)) {
				// 查询其他联系人信息
				List linkmanList = SalesReportDAO.getLink_List(Long.parseLong(oldCustomerId));
				act.setOutData("linkmanList", linkmanList);
			} else {
				act.setOutData("linkmanList", "");
			}
			act.setOutData("returnValue", 1);
			// act.setForword(otherLinkManListURL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "实销信息上报:得到其他联系人信息");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * FUNCTION : 实销信息上报:得到页面信息
	 * 
	 * @param :
	 * @return :
	 * @throws :
	 *             LastUpdate : 2010-6-7
	 */
	public Map<String, String> saveReportInfo_getJSPInfo() {
		AclUserBean logonUser = null;
		Map<String, String> infoMap = new HashMap<String, String>();
		try {
			RequestWrapper request = act.getRequest();
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			act.getSession().get(Constant.LOGON_USER);

			/*
			 * 客户id:如果客户id不为0，后台进行修改操作，否则新增
			 */
			// String ctmId_Old =
			// CommonUtils.checkNull(request.getParamValue("ctmId_Old"));
			// System.out.println(ctmId_Old);
			/*
			 * 1.销售信息
			 */
			String vehicle_no_fir = CommonUtils.checkNull(request.getParamValue("vehicle_no_fir")); // 车牌号首字
			if (!"".equals(vehicle_no_fir)) {
				TcCodePO code = new TcCodePO();
				code.setCodeId(vehicle_no_fir);
				code = (TcCodePO) dao.select(code).get(0);
				vehicle_no_fir = code.getCodeDesc();
			}
			String vehicle_no = CommonUtils.checkNull(request.getParamValue("vehicle_no")); // 车牌号
			String contractNo = CommonUtils.checkNull(request.getParamValue("contract_no")); // 合同编号
			String invoice_date = CommonUtils.checkNull(request.getParamValue("invoice_date")); // 开票日期
			String invoice_no = CommonUtils.checkNull(request.getParamValue("invoice_no")); // 发票编号
			String insurance_company = CommonUtils.checkNull(request.getParamValue("insurance_company")); // 保险公司
			String insurance_date = CommonUtils.checkNull(request.getParamValue("insurance_date")); // 保险日期
			String consignation_date = CommonUtils.checkNull(request.getParamValue("consignation_date")); // 车辆交付日期
			String miles = CommonUtils.checkNull(request.getParamValue("miles")); // 交付时公里数
			String payment = CommonUtils.checkNull(request.getParamValue("payment")); // 付款方式
			String price = CommonUtils.checkNull(request.getParamValue("price")); // 价格
			String memo = CommonUtils.checkNull(request.getParamValue("memo")); // 备注
			String carCharactor = CommonUtils.checkNull(request.getParamValue("carCharactor")); // 车辆性质
			String salesCon = CommonUtils.checkNull(request.getParamValue("salesCon")); // 车辆性质

			/*
			 * 2.客户类型
			 */
			String customer_type = CommonUtils.checkNull(request.getParamValue("customer_type")); // 客户类型
			String is_fleet = CommonUtils.checkNull(request.getParamValue("is_fleet")); // 是否大客户
			String fleet_id = CommonUtils.checkNull(request.getParamValue("fleet_id")); // 大客户id
			String fleet_contract_no_id = CommonUtils.checkNull(request.getParamValue("fleet_contract_no_id"));// 大客户合同id
			String fleet_contract_no = CommonUtils.checkNull(request.getParamValue("fleet_contract_no")); // 大客户合同
			/*
			 * 3.公司客户信息
			 */
			String company_name = CommonUtils.checkNull(request.getParamValue("company_name")); // 公司名称
			String company_s_name = CommonUtils.checkNull(request.getParamValue("company_s_name")); // 公司简称
			String company_phone = CommonUtils.checkNull(request.getParamValue("company_phone")); // 公司电话
			String level_id = CommonUtils.checkNull(request.getParamValue("level_id")); // 公司规模
			String kind = CommonUtils.checkNull(request.getParamValue("kind")); // 公司性质
			String vehicle_num = CommonUtils.checkNull(request.getParamValue("vehicle_num")); // 目前车辆数
			String myctm_form = CommonUtils.checkNull(request.getParamValue("myctm_form"));// 客户来源
			/*
			 * 4.个人客户信息
			 */
			String ctm_name = CommonUtils.checkNull(request.getParamValue("ctm_name")); // 客户姓名
			String sex = CommonUtils.checkNull(request.getParamValue("sex")); // 性别
			String card_type = CommonUtils.checkNull(request.getParamValue("card_type")); // 证件类别
			String card_num = CommonUtils.checkNull(request.getParamValue("card_num")); // 证件号码
			String main_phone = CommonUtils.checkNull(request.getParamValue("main_phone")); // 主要联系电话
			String other_phone = CommonUtils.checkNull(request.getParamValue("other_phone")); // 其他联系电话
			String email = CommonUtils.checkNull(request.getParamValue("email")); // 电子邮件
			String post_code = CommonUtils.checkNull(request.getParamValue("post_code")); // 邮编
			String birthday = CommonUtils.checkNull(request.getParamValue("birthday")); // 出生年月
			String ctm_form = CommonUtils.checkNull(request.getParamValue("ctm_form")); // 客户来源
			String income = CommonUtils.checkNull(request.getParamValue("income")); // 家庭月收入
			String education = CommonUtils.checkNull(request.getParamValue("education")); // 教育程度
			String industry = CommonUtils.checkNull(request.getParamValue("industry")); // 所在行业
			String is_married = CommonUtils.checkNull(request.getParamValue("is_married")); // 婚姻状况
			String profession = CommonUtils.checkNull(request.getParamValue("profession")); // 职业
			String job = CommonUtils.checkNull(request.getParamValue("job")); // 职务
			String province = CommonUtils.checkNull(request.getParamValue("province")); // 省份
			String city = CommonUtils.checkNull(request.getParamValue("city")); // 地级市
			String district = CommonUtils.checkNull(request.getParamValue("district")); // 区、县
			String address = CommonUtils.checkNull(request.getParamValue("address")); // 详细地址
			String knowAddress = CommonUtils.checkNull(request.getParamValue("knowaddress")); // 了解途径
			String salesReson = CommonUtils.checkNull(request.getParamValue("salesreson")); // 购买原因
			String salesAddress=null; 
			if (null != customer_type && !"".equals(customer_type) && customer_type.equals(Constant.CUSTOMER_TYPE_02 + "")) {
				salesAddress = CommonUtils.checkNull(request.getParamValue("salesaddress")); // 公司购买用途
			}else{
				salesAddress = CommonUtils.checkNull(request.getParamValue("salesaddress1")); // 个人购买用途
			}
			/*
			 * 5.sel_cus_type：新客户/老客户
			 */
			String sel_cus_type = CommonUtils.checkNull(request.getParamValue("sel_cus_type")); // 用户选择的类型:新、老客户
			// String initialIsOldCtm =
			// CommonUtils.checkNull(request.getParamValue("initialIsOldCtm"));
			// //保存后的类型
			String oldCustomerId = CommonUtils.checkNull(request.getParamValue("oldCustomerId")); // 用户选择的老客户id
			/*
			 * 6.实销id
			 */
			// String order_OldId =
			// CommonUtils.checkNull(request.getParamValue("ORDER_ID"));
			// 上报车辆id
			String vehicle_id = CommonUtils.checkNull(request.getParamValue("vehicle_id"));

			/**
			 * add by liuqiang vin从下端接口传入,后面的代码用到vehicle_id,这里用vin查出vehicle_id
			 */
			String vin = CommonUtils.checkNull(request.getParamValue("vin"));
			if (!"".equals(vin)) {
				TmVehiclePO tmVehiclePO = new TmVehiclePO();
				tmVehiclePO.setVin(vin);
				List<TmVehiclePO> vehicleList = dao.select(tmVehiclePO);
				if (null != vehicleList && vehicleList.size() > 0) {
					vehicle_id = String.valueOf(vehicleList.get(0).getVehicleId());
				} else {
					throw new IllegalArgumentException("没有找到VIN:" + vin);
				}
			}

			// Josn信息
			String isJson = CommonUtils.checkNull(request.getParamValue("isJson"));

			infoMap.put("vehicle_no_fir", vehicle_no_fir);
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
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "实销信息上报:得到页面信息");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
		return infoMap;
	}

	/**
	 * FUNCTION : 实销信息上报:得到其他联系人信息
	 * 
	 * @param :
	 *            LastUpdate : 2010-6-17
	 */
	public Map<String, String[]> getLinkManInfo() {
		AclUserBean logonUser = null;
		Map<String, String[]> linkManMap = new HashMap<String, String[]>();
		try {
			RequestWrapper request = act.getRequest();
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			act.getSession().get(Constant.LOGON_USER);

			String[] linkMan_name = request.getParamValues("linkMan_name");
			String[] linkMan_main_phone = request.getParamValues("linkMan_main_phone");
			String[] linkMan_other_phone = request.getParamValues("linkMan_other_phone");
			String[] linkMan_contract_reason = request.getParamValues("linkMan_contract_reason");
			linkManMap.put("linkMan_name", linkMan_name);
			linkManMap.put("linkMan_main_phone", linkMan_main_phone);
			linkManMap.put("linkMan_other_phone", linkMan_other_phone);
			linkManMap.put("linkMan_contract_reason", linkMan_contract_reason);

		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "实销信息上报:得到其他联系人信息");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
		return linkManMap;
	}

	/**
	 * FUNCTION : 实销信息上报:执行上报
	 * 
	 * @param :
	 * @return :
	 * @throws :
	 *             LastUpdate : 2010-6-21
	 */
	public void doReportAction() {
		AclUserBean logonUser = null;
		try {
			RequestWrapper request = act.getRequest();
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			act.getSession().get(Constant.LOGON_USER);
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");// 设置日期格式
			String invoice_date = request.getParamValue("invoice_date");// 获取日期
			String main_phone=act.getRequest().getParamValue("main_phone");// 获取客户手机号码
			String fjId = CommonUtils.checkNull(request.getParamValue("fjid"));
			Date myNewDate2 = df.parse(invoice_date);
			String myNewDate = df.format(new Date());
			Date myNewDate1 = df.parse(myNewDate);
			Long saleId = null;
			// TODO 新增判断车辆是否属于该经销商 2012-09-10 韩晓宇
			String vehicle_id = request.getParamValue("vehicle_id");
			Map<String, Object> vehicleInfo = dao.getDealerId(vehicle_id);
			BigDecimal dealerIdByVehicle = (BigDecimal) vehicleInfo.get("DEALER_ID"); // 车辆信息表中经销商ID
			String currentDealerId = logonUser.getDealerId(); // 当前登陆的经销商ID
			String model_name =null;
			String mortgageType =null;
			String FirstPrice = null;// 首付比例

			String LoansType = null;// 贷款方式

			String LoansYear =null;// 贷款年限

			String bank = null;// 贷款银行

			String money =null;// 贷款金额

			String lv =null;// 贷款利率

			String thischange = null;// 本品置换

			String loanschange = null;// 其他品牌置换

			String sales_man_id = null;// 销售人员
			String sales_man = null;// 销售人员名称

			String activity_id = null;// 市场活动ID
			String companyAddr = null;// 市场活动ID
			String qkOrderDetailId=null;// 获取潜客订单明细id
			String qkId=null;
			String delvDetailId=null;//获取交车明细ID
			Map<String, String> infoMap = saveReportInfo_getJSPInfo();
			// 判断当前登陆经销商与车辆信息表中经销商是不一致
			if (dealerIdByVehicle != null && !currentDealerId.contains(dealerIdByVehicle.toString())) {
				throw new RuntimeException("车辆不属于当前经销商!");
			}
			// TODO END

			if (myNewDate1.getTime() < myNewDate2.getTime()) {
				act.setOutData("returnValue", 5);
			} else {

				
				if (0 == infoMap.size()) { // YH 2011.10.15
					act.setOutData("error01", "没有找到相关车辆！请检查是否属于长安铃木汽车！");
					act.setForword(toReportURLRpc_error);
				}
				model_name = CommonUtils.checkNull(request.getParamValue("model_name"));// 车型名称

				mortgageType  = CommonUtils.checkNull(request.getParamValue("mortgageType"));// 按揭类型

				 FirstPrice = CommonUtils.checkNull(request.getParamValue("FirstPrice"));// 首付比例

				 LoansType = CommonUtils.checkNull(request.getParamValue("LoansType"));// 贷款方式

				 LoansYear = CommonUtils.checkNull(request.getParamValue("LoansYear"));// 贷款年限

				 bank = CommonUtils.checkNull(request.getParamValue("bank"));// 贷款银行

				 money = CommonUtils.checkNull(request.getParamValue("money"));// 贷款金额

				 lv = CommonUtils.checkNull(request.getParamValue("lv"));// 贷款利率

				 thischange = CommonUtils.checkNull(request.getParamValue("thischange"));// 本品置换

				 loanschange = CommonUtils.checkNull(request.getParamValue("loanschange"));// 其他品牌置换

				 sales_man_id = CommonUtils.checkNull(request.getParamValue("sales_man_id"));// 销售人员
				 sales_man = CommonUtils.checkNull(request.getParamValue("sales_man"));// 销售人员

				 activity_id = CommonUtils.checkNull(request.getParamValue("activity_id"));// 市场活动ID
				 companyAddr = CommonUtils.checkNull(request.getParamValue("companyAddr"));// 市场活动ID
				
				//add by yinshunhui 2014-11-26
				 qkOrderDetailId=CommonUtils.checkNull(request.getParamValue("qkOrderDetailId"));// 获取潜客订单明细id
				 qkId=CommonUtils.checkNull(request.getParamValue("qkId"));
				 delvDetailId=CommonUtils.checkNull(request.getParamValue("delvDetailId"));//获取交车明细ID
//				if(qkId!=null&&!"".equals(qkId)){
//					TPcOrderDetailPO tpod=new TPcOrderDetailPO();
//					tpod.setOrderDetailId(new Long(qkOrderDetailId));
//					TPcOrderDetailPO tpod2=new TPcOrderDetailPO();
//					tpod2.setOrderDetailId(new Long(qkOrderDetailId));
//					tpod2=(TPcOrderDetailPO) dao.select(tpod2).get(0);
//					TPcOrderDetailPO tpod1=new TPcOrderDetailPO();
//					tpod1.setActDelvDate(new Date());//设置实际交车时间
//					int delvNum=tpod2.getDeliveryNumber()==null?1:tpod2.getDeliveryNumber()+1;
//					tpod1.setDeliveryNumber(delvNum);//设置已交车数量ber
//					dao.update(tpod, tpod1);
//					boolean delvFlag=CommonUtils.checkIsComplete(qkId);
//					if(delvFlag){
//						//如果所有的订单都完成了那么他得状态就修改为保有客户
//						TPcCustomerPO tpc=new TPcCustomerPO();
//						tpc.setCustomerId(new Long(qkId));
//						TPcCustomerPO tpc1=new TPcCustomerPO();
//						tpc1.setCtmType(Constant.CTM_TYPE_01.toString());
//						dao.update(tpc,tpc1);
//						//结束当前的交车任务 增加一个回访任务
//					}
//				}
				
				//end

				/*
				 * 查询： 1.车厂公司ID： OEM_COMPANY_ID 2.经销商公司ID： DLR_COMPANY_ID
				 * 3.经销商渠道： DEALER_ID
				 */
				String s_dealer_id = ""; // 经销商渠道
				String s_oem_company_id = ""; // 车厂公司ID
				String s_dlr_company_id = ""; // 经销商公司ID
				String returnValue = "1";
				String yieldly = "-1";
				TmVehiclePO tmVehiclePO = new TmVehiclePO();
				tmVehiclePO.setVehicleId(Long.parseLong(infoMap.get("vehicle_id")));
				List vehicleList = dao.select(tmVehiclePO);
				if (null != vehicleList && vehicleList.size() > 0) {
					s_dealer_id = ((TmVehiclePO) vehicleList.get(0)).getDealerId() + "";
					int lifeCycle = ((TmVehiclePO) vehicleList.get(0)).getLifeCycle();
					if (((TmVehiclePO) vehicleList.get(0)).getYieldly() != null) {
						yieldly = ((TmVehiclePO) vehicleList.get(0)).getYieldly().toString();
					}
					if (lifeCycle == Constant.VEHICLE_LIFE_04) {
						returnValue = "2";
						act.setOutData("returnValue", returnValue);
						return;
					}
					TmDealerPO dealerPO = new TmDealerPO();
					dealerPO.setDealerId(((TmVehiclePO) vehicleList.get(0)).getDealerId());
					List dealerList = dao.select(dealerPO);
					if (null != dealerList && dealerList.size() > 0) {
						s_oem_company_id = ((TmDealerPO) dealerList.get(0)).getOemCompanyId() + "";
						s_dlr_company_id = ((TmDealerPO) dealerList.get(0)).getCompanyId() + "";
					}
				}

				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

				TtCustomerPO customerPO = new TtCustomerPO();
				Long ctmId = Long.parseLong(SequenceManager.getSequence(""));
				customerPO.setCtmId(ctmId);
				customerPO.setDlrCompanyId(s_dlr_company_id != null && !"".equals(s_dlr_company_id) ? Long.parseLong(s_dlr_company_id) : null);
				customerPO.setOemCompanyId(s_oem_company_id != null && !"".equals(s_oem_company_id) ? Long.parseLong(s_oem_company_id) : null);

				String customer_type = infoMap.get("customer_type"); // 客户类型
				String sel_cus_type = "11101001";//infoMap.get("sel_cus_type"); // 新客户/老客户
				/*
				 * 一、如果“客户类型”是“公司客户” 1.向（TT_CUSTOMER实销客户表）写入“公司客户信息”
				 * 2.如果“是否大客户”为“是”，向（TT_DEALER_ACTUAL_SALES车辆实销表）写入“大客户名称”和“大客户合同”
				 */
				if (null != customer_type && !"".equals(customer_type) && customer_type.equals(Constant.CUSTOMER_TYPE_02 + "")) {
					String company_code = CommonUtils.checkNull(request.getParamValue("company_code"));// 公司组织代码
					String company_man = CommonUtils.checkNull(request.getParamValue("company_man"));// 公司联系人
					String company_tel = CommonUtils.checkNull(request.getParamValue("company_tel"));// 公司联系电话
					customerPO.setCtmType(Constant.CUSTOMER_TYPE_02);
					//customerPO.setCtmName(infoMap.get("company_name").trim()); // 向客户名称字段中插入公司名称
					customerPO.setCompanyName(infoMap.get("company_name").trim()); // 公司名称
					if (null != infoMap.get("company_s_name") && !"".equals(infoMap.get("company_s_name"))) {
						customerPO.setCompanySName(infoMap.get("company_s_name").trim()); // 公司简称
					}
					customerPO.setCtmForm(Integer.parseInt(infoMap.get("myctm_form")));// 客户来源
					customerPO.setCompanyPhone(infoMap.get("company_phone").trim()); // 公司电话
					customerPO.setLevelId(Integer.parseInt(infoMap.get("level_id"))); // 公司规模
					customerPO.setKind(Integer.parseInt(infoMap.get("kind"))); // 公司性质
					customerPO.setCompanyCode(company_code);
					customerPO.setCtmName(company_man);
					customerPO.setMainPhone(company_tel);
					if (null != infoMap.get("vehicle_num") && !"".equals(infoMap.get("vehicle_num"))) {
						customerPO.setVehicleNum(Integer.parseInt(infoMap.get("vehicle_num").trim())); // 目前车辆数
					}
					if (null != infoMap.get("province") && !"".equals(infoMap.get("province"))) {
						customerPO.setProvince(Long.parseLong(infoMap.get("province"))); // 省份
					}
					if (null != infoMap.get("city") && !"".equals(infoMap.get("city"))) {
						customerPO.setCity(Long.parseLong(infoMap.get("city"))); // 地级市
					}
					if (null != infoMap.get("district") && !"".equals(infoMap.get("district"))) {
						customerPO.setTown(Long.parseLong(infoMap.get("district"))); // 区、县
					}
					customerPO.setAddress(companyAddr);
					dao.insert(customerPO);
				}

				/*
				 * 二、如果“客户类型”是“个人客户”
				 * 1.如果实销客户为“新客户”，向（TT_CUSTOMER实销客户表）新增客户信息，否则只写入“老客户id”
				 * 2.如果“是否大客户”为“是”，向（TT_DEALER_ACTUAL_SALES车辆实销表）写入“大客户名称”和“大客户合同”
				 */
				if (null != customer_type && !"".equals(customer_type) && customer_type.equals(Constant.CUSTOMER_TYPE_01 + "")) {
					if (null != sel_cus_type && sel_cus_type.equals(Constant.IS_OLD_CTM_01 + "")) {
						customerPO.setCtmType(Constant.CUSTOMER_TYPE_01); // 客户类型
						customerPO.setCtmName(infoMap.get("ctm_name").trim()); // 客户名称
						if (infoMap.get("sex") != null) {
							customerPO.setSex(Integer.parseInt(infoMap.get("sex"))); // 性别
						}
						customerPO.setCardType(Integer.parseInt(infoMap.get("card_type"))); // 证件类型
						customerPO.setCardNum(infoMap.get("card_num").trim()); // 证件号码
						customerPO.setMainPhone(infoMap.get("main_phone").trim()); // 主要联系电话
						if (null != infoMap.get("other_phone") && !"".equals(infoMap.get("other_phone"))) {
							customerPO.setOtherPhone(infoMap.get("other_phone").trim()); // 其他联系电话
						}
						if (null != infoMap.get("email") && !"".equals(infoMap.get("email"))) {
							customerPO.setEmail(infoMap.get("email").trim()); // 电子邮件
						}
						if (null != infoMap.get("post_code") && !"".equals(infoMap.get("post_code"))) {
							customerPO.setPostCode(infoMap.get("post_code").trim()); // 邮政编码
						}
						if (Utility.testString(infoMap.get("birthday"))) {
							customerPO.setBirthday(dateFormat.parse(infoMap.get("birthday"))); // 生日
						}
						if (Utility.testString(infoMap.get("ctm_form"))) {
							customerPO.setCtmForm(Integer.parseInt(infoMap.get("ctm_form"))); // 客户来源
						}
						if (Utility.testString(infoMap.get("income"))) {
							customerPO.setIncome(Integer.parseInt(infoMap.get("income"))); // 家庭月收入
						}
						if (Utility.testString(infoMap.get("education"))) {
							customerPO.setEducation(Integer.parseInt(infoMap.get("education"))); // 教育程度
						}
						if (Utility.testString(infoMap.get("industry"))) {
							customerPO.setIndustry(Integer.parseInt(infoMap.get("industry"))); // 所在行业
						}
						if (Utility.testString(infoMap.get("is_married"))) {
							customerPO.setIsMarried(Integer.parseInt(infoMap.get("is_married"))); // 婚姻状况
						}
						if (Utility.testString(infoMap.get("profession"))) {
							customerPO.setProfession(Integer.parseInt(infoMap.get("profession"))); // 职业
						}
						if (null != infoMap.get("job") && !"".equals(infoMap.get("job"))) {
							customerPO.setJob(infoMap.get("job").trim()); // 职务
						}
						if (null != request.getParamValue("province1") && !"".equals(request.getParamValue("province1"))) {
							customerPO.setProvince(Long.parseLong(request.getParamValue("province1"))); // 省份
						}
						if (null != request.getParamValue("city1") && !"".equals(request.getParamValue("city1"))) {
							customerPO.setCity(Long.parseLong(request.getParamValue("city1"))); // 地级市
						}
						if (null != request.getParamValue("district1") && !"".equals(request.getParamValue("district1"))) {
							customerPO.setTown(Long.parseLong(request.getParamValue("district1"))); // 区、县
						}
						customerPO.setAddress(infoMap.get("address").trim()); // 详细地址

						customerPO.setCreateBy(logonUser.getUserId());
						customerPO.setCreateDate(new Date());
						dao.insert(customerPO);
					}
				}

				// 其他联系人

				Map<String, String[]> linkManInfo = getLinkManInfo();
				if (null != sel_cus_type && sel_cus_type.equals(Constant.IS_OLD_CTM_01 + "")) {
					saveReportInfo_AddorUpdateLinkMan(ctmId + "", linkManInfo, s_dlr_company_id, s_oem_company_id);
				}
				if (null != sel_cus_type && sel_cus_type.equals(Constant.IS_OLD_CTM_02 + "")) {
					saveReportInfo_AddorUpdateLinkMan(infoMap.get("oldCustomerId"), linkManInfo, s_dlr_company_id, s_oem_company_id);
				}

				// 实销信息
				TtDealerActualSalesPO actualSalesPO = new TtDealerActualSalesPO();
				if(sales_man_id!=null && !"".equals(sales_man_id)){
					actualSalesPO.setSalesConId(new Long(sales_man_id));
				}
				actualSalesPO.setSalesConName(sales_man);
				saleId = Long.parseLong(SequenceManager.getSequence(""));
				String ywzj=saleId.toString();
				//建立上报车辆和附件之间的关系
				//FsFileuploadPO po = new FsFileuploadPO();
				//po.setFjid(Long.parseLong(fjId));
				//FsFileuploadPO po2 = new FsFileuploadPO();
				//po2.setYwzj(saleId);
				//dao.update(po, po2);
				String[] fjids = request.getParamValues("fjid");//获取文件ID
				FileUploadManager.fileUploadByBusiness(ywzj, fjids, logonUser);	
				//设置潜客id add by yinshunhui 2014-11-27 start
				if(qkOrderDetailId!=null&&!"".equals(qkOrderDetailId)){
					actualSalesPO.setQkOrderDetailId(qkOrderDetailId);
				}
				//设置交车明细ID
				if(delvDetailId!=null&&!"".equals(delvDetailId)){
					actualSalesPO.setDelvDetailId(delvDetailId);
				}
				//end 

				if (null != mortgageType && !"".equals(mortgageType)) {
					actualSalesPO.setMortgageType(Long.valueOf(mortgageType));// 按揭类型
				}
				if (null != FirstPrice && !"".equals(FirstPrice)) {

					actualSalesPO.setShoufuRatio((Double.valueOf(FirstPrice)));// 首付比例
				}

				if (null != LoansType && !"".equals(LoansType)) {
					actualSalesPO.setLoansType(Long.valueOf(LoansType));// 贷款方式
				}

				if (null != LoansYear && !"".equals(LoansYear)) {
					actualSalesPO.setLoansYear(Long.valueOf(LoansYear));// 贷款年限
				}
				if (null != bank && !"".equals(bank)) {
					actualSalesPO.setBank(Long.valueOf(bank));// 贷款银行
				}
				if (null != money && !"".equals(money)) {
					actualSalesPO.setMoney(Double.valueOf(money));// 贷款金额
				}
				if (null != lv && !"".equals(lv)) {
					actualSalesPO.setLv(Double.valueOf(lv));// 贷款利率
				}
				if (null != thischange && !"".equals(thischange)) {
					actualSalesPO.setThischange(Long.valueOf(thischange));// 本品置换
				}
				if (null != loanschange && !"".equals(loanschange)) {
					actualSalesPO.setLoanschange(loanschange);// 其他品牌置换
				}
				actualSalesPO.setOrderId(saleId); // 实销ID
				actualSalesPO.setKnowAddress(infoMap.get("know_Address").trim());
				actualSalesPO.setSalesAddress(infoMap.get("sales_Address").trim());
				actualSalesPO.setSalesReson(infoMap.get("sales_Reson").trim());
				actualSalesPO.setOemCompanyId(s_oem_company_id != null && !"".equals(s_oem_company_id) ? Long.parseLong(s_oem_company_id) : null); // 车厂公司ID
				actualSalesPO.setDlrCompanyId(s_dlr_company_id != null && !"".equals(s_dlr_company_id) ? Long.parseLong(s_dlr_company_id) : null); // 经销商公司ID
				actualSalesPO.setDealerId(Long.parseLong(s_dealer_id)); // 经销商渠道
				actualSalesPO.setVehicleId(Long.parseLong(infoMap.get("vehicle_id")));// 车辆ID
				if (null != infoMap.get("vehicle_no") && !"".equals(infoMap.get("vehicle_no"))) {
					actualSalesPO.setVehicleNo(infoMap.get("vehicle_no_fir").trim() + infoMap.get("vehicle_no").trim()); // 车牌号
				}
				if (null != infoMap.get("contractNo") && !"".equals(infoMap.get("contractNo"))) {
					actualSalesPO.setContractNo(infoMap.get("contractNo").trim()); // 合同编号
				}
				actualSalesPO.setInvoiceDate(dateFormat.parse(myNewDate)); // 开票日期
				actualSalesPO.setInvoiceNo(infoMap.get("invoice_no")); // 发票编号
				if (null != infoMap.get("insurance_company") && !"".equals(infoMap.get("insurance_company"))) {
					actualSalesPO.setInsuranceCompany(infoMap.get("insurance_company").trim()); // 保险公司
				}
				if (null != infoMap.get("insurance_date") && !"".equals(infoMap.get("insurance_date"))) {
					actualSalesPO.setInsuranceDate(dateFormat.parse(infoMap.get("insurance_date"))); // 保险日期
				}
				/*actualSalesPO.setConsignationDate(dateFormat.parse(infoMap.get("consignation_date"))); // 车辆交付日期
				if (null != infoMap.get("miles") && !"".equals(infoMap.get("miles"))) {
					actualSalesPO.setMiles(Float.parseFloat(infoMap.get("miles").trim())); // 交付时公里数
				}*/
				
				actualSalesPO.setPayment(Integer.parseInt(infoMap.get("payment"))); // 付款方式
				actualSalesPO.setPrice(Double.parseDouble(infoMap.get("price").trim())); // 价格
				if (null != infoMap.get("memo") && !"".equals(infoMap.get("memo"))) {
					actualSalesPO.setMemo(infoMap.get("memo").trim()); // 备注
				}
				actualSalesPO.setIsReturn(Constant.IF_TYPE_NO); // 是否退车
				// 如果是大客户
				String is_fleet = infoMap.get("is_fleet");
				if (null != is_fleet && is_fleet.equals(Constant.IF_TYPE_YES + "")) {
					String fleet_id = infoMap.get("fleet_id"); // 大客户id
					String fleet_contract_no_id = infoMap.get("fleet_contract_no_id"); // 大客户合同id
					String fleet_contract_no = infoMap.get("fleet_contract_no");
					actualSalesPO.setIsFleet(Constant.IF_TYPE_YES); // 是否是大客户
					actualSalesPO.setFleetId(Long.parseLong(fleet_id)); // 大客户ID
					if (fleet_contract_no_id != null && !fleet_contract_no_id.equals("")) {
						actualSalesPO.setContractId(Long.parseLong(fleet_contract_no_id)); // 大客户合同ID
					}
					if (fleet_contract_no != null && !fleet_contract_no.equals("")) {
						actualSalesPO.setContractNo(fleet_contract_no); // 大客户合同号
					}
					actualSalesPO.setFleetStatus(Constant.Fleet_SALES_CHECK_STATUS_01); // 未审核
				} else {
					actualSalesPO.setIsFleet(Constant.IF_TYPE_NO);
				}
				// 如果是新客户
				if (null != sel_cus_type && sel_cus_type.equals(Constant.IS_OLD_CTM_01 + "")) {
					actualSalesPO.setCtmId(ctmId);
				}
				// 如果是老客户
				if (null != sel_cus_type && sel_cus_type.equals(Constant.IS_OLD_CTM_02 + "")) {
					actualSalesPO.setCtmId(Long.parseLong(infoMap.get("oldCustomerId")));
				}

				actualSalesPO.setSalesDate(new Date());
				actualSalesPO.setCreateDate(new Date());
				actualSalesPO.setCreateBy(logonUser.getUserId());
				if (Utility.testString(infoMap.get("carCharactor"))) {
					actualSalesPO.setCarCharactor(Integer.parseInt(infoMap.get("carCharactor")));// 车辆性质
				}
				/*
				 * 写入实销信息
				 */
				actualSalesPO.setSoNo(CommonUtils.checkNull(request.getParamValue("soNo")));// 下端通过URL请求

                actualSalesPO.setActivityId(activity_id==null||"".equals(activity_id)?null:new Long(activity_id));
            	/*
				 * 添加老客户信息写入实销信息中
				 */
                if(main_phone !=null && !"".equals(main_phone)){
	            	TaskManageDao dao = new TaskManageDao();
					//获取车主与老客户的关联信息
					List<DynaBean> linkMainList =dao.getLinkMan(main_phone);
					String linkMan="";
					String linkPhone="";
					String oldVin="";
					String reLationCode="";
					if(linkMainList.size()>0){
						DynaBean dblink = linkMainList.get(0);
						if(dblink.get("LINK_MAN")!=null) {
							linkMan = dblink.get("LINK_MAN").toString();
						}
						if(dblink.get("LINK_PHONE")!=null) {
							linkPhone = dblink.get("LINK_PHONE").toString();
						}
						if(dblink.get("OLD_VEHICLE_ID")!=null) {
							oldVin = dblink.get("OLD_VEHICLE_ID").toString();
						}
						if(dblink.get("RELATION_CODE")!=null) {
							reLationCode = dblink.get("RELATION_CODE").toString();
						}
					actualSalesPO.setOldCustomerName(linkMan.trim());
					actualSalesPO.setOldTelephone(linkPhone.trim());
					actualSalesPO.setOldVehicleId(oldVin.trim());
					actualSalesPO.setOldRelationCode(reLationCode.trim());
					}
                }

				dao.insert(actualSalesPO);
				
				//更新交车表状态为已上报
				if(delvDetailId!=null && !"".equals(delvDetailId)){
					TPcDelvyPO tpdp=new TPcDelvyPO();
					tpdp.setDelvDetailId(Long.parseLong(delvDetailId));
					TPcDelvyPO tpdpnew=new TPcDelvyPO();
					tpdpnew.setDeliveryStatus(Constant.delivery_status_02);
				    dao.update(tpdp, tpdpnew);
				}

				/*
				 * List<Object> ins = new LinkedList<Object>();
				 * ins.add(saleId); List<Integer> outs = new LinkedList<Integer>();
				 * dao.callProcedure("p_xxcrm_ic_customer_dms",ins,outs) ;
				 */

				// ----------------------------------------------------------------------------
				TmVehiclePO tempPO = new TmVehiclePO();
				tempPO.setVehicleId(Long.parseLong(infoMap.get("vehicle_id")));
				TmVehiclePO valuePO = new TmVehiclePO();
				valuePO.setLifeCycle(Constant.VEHICLE_LIFE_04);
				valuePO.setPurchasedDate(new Date());

				// 新增三包策略字段
				/**
				TmDealerPO tmDealerPO = new TmDealerPO();
				tmDealerPO.setDealerId(Long.parseLong(s_dealer_id));
				List dealerList = dao.select(tmDealerPO);
				if (null != dealerList && dealerList.size() > 0) {
					TmVhclMaterialGroupPO groupPO = new TmVhclMaterialGroupPO();
					groupPO.setGroupName(model_name);
					List gList = dao.select(groupPO);
					if (null != gList && gList.size() > 0) {
						TmVhclMaterialGroupPO materialGroupPO = (TmVhclMaterialGroupPO) gList.get(0);
						Long model_id = materialGroupPO.getGroupId();
						TmDealerPO v_dealer = (TmDealerPO) dealerList.get(0);
						String  provinceId = v_dealer.getProvinceId();
						Long claimTacticsId = guaranteePeriodDAO.findThreeGuaranteesStrategy(new Date(), new Long(provinceId), model_id, GetOemcompanyId.getOemCompanyId(logonUser), new Integer(yieldly));
						valuePO.setClaimTacticsId(claimTacticsId);
					}
				}
			*/	

				// 修改车辆生命周期：实销
				dao.update(tempPO, valuePO);
				// 向TT_VS_VHCL_CHNG写入变更日志
				TtVsVhclChngPO chngPO = new TtVsVhclChngPO();
				Long vhclChangeId = Long.parseLong(SequenceManager.getSequence(""));
				chngPO.setVhclChangeId(vhclChangeId); // 改变序号
				chngPO.setVehicleId(Long.parseLong(infoMap.get("vehicle_id"))); // 车辆ID
				chngPO.setOrgType(logonUser.getOrgType()); // 组织类型
				chngPO.setOrgId(logonUser.getOrgId()); // 组织ID
				chngPO.setDealerId(Long.parseLong(s_dealer_id)); // 经销商ID
				chngPO.setChangeCode(Constant.SALES_STATUS_CHANGE_TYPE); // 改变类型:销售状态更改
				chngPO.setChangeName(Constant.SALES_STATUS_CHANGE_TYPE_04.toString()); // 改变名称:实效上报
				chngPO.setChangeDate(new Date()); // 改变时间
				if (null != infoMap.get("memo") && !"".equals(infoMap.get("memo"))) {
					chngPO.setChangeDesc(infoMap.get("memo").trim()); // 改变描述
				}
				chngPO.setCreateDate(new Date()); // 记录创建日期
				chngPO.setCreateBy(logonUser.getUserId()); // 记录创建者
				dao.insert(chngPO);

				String isJson = infoMap.get("isJson");

				/** 只有接口用户登录* */
				if (Utility.testString(logonUser.getRpcFlag())) {
					/** 下发开始* */
					OSB32 o = new OSB32();
					o.execute(actualSalesPO);
					// act.setForword(toReportURLRpc);
					returnValue = "6";
					act.setOutData("returnValue", returnValue);
					/** 下发结束* */
				} else {
					salesReportInit();
				}
				
			}

			// start yinsh
			// 给机构人员信息表中写入记录
//			String sales_man = CommonUtils.checkNull(sales_man_id);// 销售人员
//			TtVsPersonPO tvpp = new TtVsPersonPO();
//			tvpp.setPersonId(Long.parseLong(sales_man));
//			TtVsPersonPO tvpp0 = new TtVsPersonPO();
//			tvpp0 = (TtVsPersonPO) dao.select(tvpp).get(0);
//			// 得到原来的业绩积分
//			Long performanceInteg = tvpp0.getPerformanceInteg() == null ? Long.parseLong("0") : tvpp0.getPerformanceInteg();

//			// 根据vehicle_id 获取车系code
//			Map<String, String> m = dao.getGroupByVechileId(vehicle_id);
//			String groupCode = m.get("GROUP_CODE");
//
//			TmVhclMaterialGroupPO tvmg = new TmVhclMaterialGroupPO();
//			tvmg.setGroupLevel(2);
//			tvmg.setGroupCode(groupCode);
//			tvmg = (TmVhclMaterialGroupPO) dao.select(tvmg).get(0);
//			Long addIteg = tvmg.getMotivateInteg();
//			if (addIteg == null) {
//				return;
//			}
//
//			StringBuffer sql = new StringBuffer();
//			sql.append("SELECT COUNT(1) AS COU\n");
//			sql.append("  FROM TT_DEALER_ACTUAL_SALES T\n");
//			sql.append(" WHERE TRUNC(T.CREATE_DATE, 'MM') = TRUNC(SYSDATE, 'MM')\n");
//			sql.append("   AND T.IS_RETURN = 10041002\n");
//			sql.append("   AND T.INTEG IS NULL\n");
//			sql.append("   AND T.DEALER_ID = " + tvpp0.getDealerId() + "\n");
//			sql.append("   AND T.SALES_CON_ID = " + tvpp0.getPersonId());
//			int cou = Integer.parseInt(dao.pageQueryMap(sql.toString(), null, null).get("COU").toString());
//			
//
//			if (10 < cou && cou <= 15) {
//				addIteg = addIteg / 2;
//			} else if(cou > 15){
//				addIteg = 0L;
//			}
//			
//			TtDealerActualSalesPO actualSalesPO = new TtDealerActualSalesPO();
//			TtDealerActualSalesPO actualSalesPO_U = new TtDealerActualSalesPO();
//
//			actualSalesPO.setSalesConId(saleId);
//			actualSalesPO_U.setCouno(cou);
//			actualSalesPO_U.setInteg(addIteg);
//			dao.update(actualSalesPO, actualSalesPO_U);

//			// 得到销售顾问最终积分
//			Long finalInteg = performanceInteg + addIteg;
//			TtVsPersonPO tvpp1 = new TtVsPersonPO();
//			tvpp1.setPerformanceInteg(finalInteg);
//			dao.update(tvpp, tvpp1);
//			// 给积分变动历史表中写入记录
//			TtVsIntegrationChangePO tvic = new TtVsIntegrationChangePO();
//			Long integ_change_id = Long.parseLong(SequenceManager.getSequence(""));
//			tvic.setIntegChangeId(integ_change_id);
//			tvic.setDealerId(logonUser.getDealerId() == null || "".equals(logonUser.getDealerId()) ? null : Long.parseLong(logonUser.getDealerId()));
//			tvic.setCreateDate(new Date());
//			tvic.setIdNo(tvpp0.getIdNo());
//			tvic.setName(tvpp0.getName());
//			tvic.setIntegBefore(performanceInteg);
//			tvic.setIntegAfter(finalInteg);
//			tvic.setIntegType(Constant.PERFORMANCE_INTEG);
//			tvic.setChangeType(Constant.INTEG_CHANGE_SALE);
//			tvic.setThisChangeInteg(addIteg);
//			tvic.setPerformanceInteg(addIteg);
//			tvic.setRelationId(saleId);
//			dao.insert(tvic);
			// 
			/*
			TPcOrderDetailPO tpod=new TPcOrderDetailPO();
 			tpod.setOrderDetailId(new Long(qkOrderDetailId));
 			tpod=(TPcOrderDetailPO) dao.select(tpod).get(0);//通过详细订单好获取用户信息
 			
			String vehicleId=SequenceManager.getSequence("");
			String vin = CommonUtils.checkNull(request.getParamValue("vin"));// vin
			String modelCode=CommonUtils.checkNull(request.getParamValue("modelCode"));//车型代码
			String color=tpod.getIntentColor();//颜色编码
			String purDate=infoMap.get("invoice_date");//开票日期
			String enVin = infoMap.get("price").trim();//价格
			String vehicleNo = infoMap.get("vehicle_no");//车牌号
			String consignationDate = infoMap.get("consignation_date");//车辆交付日期 
			String pin ="";//音响编码
			
			TPcVechilePO tip=new TPcVechilePO();
			tip.setCtmId(new Long(qkId));
			tip.setVechileId(new Long(vehicleId));
			
			TmVehiclePO tvp=new TmVehiclePO();
			tvp.setVin(vin);
			tvp=(TmVehiclePO) dao.select(tvp).get(0);//通过Vin号获取汽车生产日期
			
		
			  
			if(vin!=null&&!"".equals(vin)){
				tip.setVin(vin);
			}
			
			if(modelCode!=null&&!"".equals(modelCode)){
				tip.setModelCode(modelCode);
			}
			if(model_name!=null&&!"".equals(model_name)){
				tip.setModelName(model_name);
			}
						
			if(purDate!=null&&!"".equals(purDate)){
				SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
				tip.setBuyDate(sdf.parse(purDate));
			}
			if(enVin!=null&&!"".equals(enVin)){ //价格
				tip.setLowVin(enVin);
			}
			if(color!=null&&!"".equals(color)){
				tip.setVechileColor(new Long(color));
			}
			if(enVin!=null&&!"".equals(enVin)){
				tip.setPrice(new Double(enVin));
			}
			if(vehicleNo!=null&&!"".equals(vehicleNo)){
				tip.setCarNumber(vehicleNo);
			}
			if(consignationDate !=null&&!"".equals(consignationDate)){
				SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
				tip.setBoardDate(sdf.parse(consignationDate));
			}
			if(pin!=null&&!"".equals(pin)){
				tip.setPin(pin);
			}
		     tip.setProductDate(tvp.getProductDate());
			 tip.setStatus(new Long(Constant.STATUS_ENABLE));
			 dao.insert(tip);
			//插入联系人数据
			String  linkman = infoMap.get("ctm_name");//客户名称
			String  linktel = infoMap.get("main_phone");//联系电话
			String  cardType = infoMap.get("card_type");//证件类别
			String  cardCode = infoMap.get("card_num");//证件号码
			String ctmID = CommonUtils.checkNull(request.getParamValue("ctmID"));// 车主id
			
			
			String linkId=SequenceManager.getSequence("");
			TPcLinkManPO tlm=new TPcLinkManPO();
			tlm.setLinkId(new Long(linkId));
			tlm.setLinkMan(linkman);
			tlm.setLinkPhone(linktel);
			tlm.setCtmId(new Long(ctmID));
			tlm.setCardType(new Long(cardType));
			tlm.setCardCode(cardCode);
			tlm.setRelationship("空后期改动");
			tlm.setStatus(new Long(Constant.STATUS_ENABLE));
			dao.insert(tlm);
			*/
			// end
			
			

		} catch (RuntimeException e) {
			logger.error(logonUser, e);
			act.setException(e);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "实销信息上报:执行上报");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * FUNCTION : 实销信息上报:新增/修改其他联系人信息
	 * 
	 * @param :
	 *            String ctm_id:新增时的客户id String ctm_id_new:修改后的客户id Map linkMap
	 *            :其他联系人信息 LastUpdate : 2010-6-17
	 */
	public void saveReportInfo_AddorUpdateLinkMan(String ctm_id, Map linkMap, String s_dlr_company_id, String s_oem_company_id) {
		AclUserBean logonUser = null;
		try {
			RequestWrapper request = act.getRequest();
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			act.getSession().get(Constant.LOGON_USER);
			String[] linkMan_name = (String[]) linkMap.get("linkMan_name");
			String[] linkMan_main_phone = (String[]) linkMap.get("linkMan_main_phone");
			String[] linkMan_other_phone = (String[]) linkMap.get("linkMan_other_phone");
			String[] linkMan_contract_reason = (String[]) linkMap.get("linkMan_contract_reason");
			TtLinkmanPO linkPO = new TtLinkmanPO();
			linkPO.setCtmId(Long.parseLong(ctm_id));
			dao.delete(linkPO);
			if (null != linkMan_name && linkMan_name.length > 0) {
				for (int i = 0; i < linkMan_name.length; i++) {
					TtLinkmanPO linkmanPO = new TtLinkmanPO();
					linkmanPO.setLmId(Long.parseLong(SequenceManager.getSequence(""))); // 联系人ID
					linkmanPO.setCtmId(Long.parseLong(ctm_id)); // 客户序列号

					if (null != s_dlr_company_id && !"".equals(s_dlr_company_id)) {
						linkmanPO.setDlrCompanyId(Long.parseLong(s_dlr_company_id)); // 经销商公司
					}
					if (null != s_oem_company_id && !"".equals(s_oem_company_id)) {
						linkmanPO.setOemCompanyId(Long.parseLong(s_oem_company_id)); // 车厂公司ID
					}
					linkmanPO.setName(linkMan_name[i].trim()); // 姓名
					linkmanPO.setMainPhone(linkMan_main_phone[i].trim()); // 主要联系电话
					if (null != linkMan_other_phone[i] && !"".equals(linkMan_other_phone[i])) {
						linkmanPO.setOtherPhone(linkMan_other_phone[i].trim()); // 其他联系电话
					}
					if (null != linkMan_contract_reason[i] && !"".equals(linkMan_contract_reason[i])) {
						linkmanPO.setContractReason(linkMan_contract_reason[i].trim()); // 联系目的
					}
					linkmanPO.setCreateBy(logonUser.getUserId());
					linkmanPO.setCreateDate(new Date());
					dao.insert(linkmanPO);
				}
			}
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "实销信息上报:新增/修改其他联系人信息");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * FUNCTION : 实销信息上报:新增/修改客户信息
	 * 
	 * @param :
	 *            String vehicleId:上报车辆id String
	 *            ctmId_Old:保存后的客户id,如果ctmId_Old有值，则修改客户信息，否则新增 String
	 *            oldCustomerId:用户选择的老客户id String initialIsOldCtm:修改前"新/老客户类型"
	 *            String isOldCtm ：修改后用户选择的"新/老客户类型" String orderId:实销id String
	 *            is_fleet:是否是大客户 String initialIsFleet:修改前是否是大客户 LastUpdate :
	 *            2010-6-17
	 */
	public void saveReportInfo_AddorUpdateCtm(String vehicleId, String ctmId_Old, String oldCustomerId, String initialIsOldCtm, String isOldCtm, String orderId, String is_fleet, String s_dealer_id, String s_oem_company_id, String s_dlr_company_id, String initialIsFleet) {
		AclUserBean logonUser = null;
		try {
			RequestWrapper request = act.getRequest();
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			act.getSession().get(Constant.LOGON_USER);

			Map<String, String> infoMap = saveReportInfo_getJSPInfo();

			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

			TtCustomerPO customerPO = new TtCustomerPO();
			Long ctmId_Add = new Long(0);
			if (null == ctmId_Old || "".equals(ctmId_Old) || "0".equals(ctmId_Old)) {
				ctmId_Add = Long.parseLong(SequenceManager.getSequence("")); // 新增客户id
				customerPO.setCtmId(ctmId_Add);
			}
			if (null != s_dlr_company_id && !"".equals(s_dlr_company_id)) {
				customerPO.setDlrCompanyId(Long.parseLong(s_dlr_company_id)); // 经销商公司
			}
			if (null != s_oem_company_id && !"".equals(s_oem_company_id)) {
				customerPO.setOemCompanyId(Long.parseLong(s_oem_company_id)); // 车厂公司ID
			}
			if (null != isOldCtm && isOldCtm.equals(Constant.IS_OLD_CTM_01 + "")) {
				customerPO.setCtmType(Integer.parseInt(infoMap.get("customer_type"))); // 客户类型
				customerPO.setCtmName(infoMap.get("ctm_name").trim()); // 客户名称
				customerPO.setCardType(Integer.parseInt(infoMap.get("card_type"))); // 证件类型
				customerPO.setCardNum(infoMap.get("card_num").trim()); // 证件号码
				customerPO.setSex(Integer.parseInt(infoMap.get("sex"))); // 性别
				customerPO.setBirthday(dateFormat.parse(infoMap.get("birthday"))); // 生日
				if (null != infoMap.get("industry") && !"".equals(infoMap.get("industry"))) {
					customerPO.setIndustry(Integer.parseInt(infoMap.get("industry"))); // 所在行业
				}
				if (null != infoMap.get("profession") && !"".equals(infoMap.get("profession"))) {
					customerPO.setProfession(Integer.parseInt(infoMap.get("profession"))); // 职业
				}
				if (null != infoMap.get("job") && !"".equals(infoMap.get("job"))) {
					customerPO.setJob(infoMap.get("job").trim()); // 职务
				}
				customerPO.setCtmForm(Integer.parseInt(infoMap.get("ctm_form"))); // 客户来源
				customerPO.setMainPhone(infoMap.get("main_phone").trim()); // 主要联系电话
				if (null != infoMap.get("other_phone") && !"".equals(infoMap.get("other_phone"))) {
					customerPO.setOtherPhone(infoMap.get("other_phone").trim()); // 其他联系电话
				}
				if (null != infoMap.get("income") && !"".equals(infoMap.get("income"))) {
					customerPO.setIncome(Integer.parseInt(infoMap.get("income"))); // 家庭月收入
				}
				if (null != infoMap.get("education") && !"".equals(infoMap.get("education"))) {
					customerPO.setEducation(Integer.parseInt(infoMap.get("education"))); // 教育程度
				}
				if (null != infoMap.get("is_married") && !"".equals(infoMap.get("is_married"))) {
					customerPO.setIsMarried(Integer.parseInt(infoMap.get("is_married"))); // 婚姻状况
				}
				if (null != infoMap.get("email") && !"".equals(infoMap.get("email"))) {
					customerPO.setEmail(infoMap.get("email").trim()); // 电子邮件
				}
				if (null != infoMap.get("province") && !"".equals(infoMap.get("province"))) {
					customerPO.setProvince(Long.parseLong(infoMap.get("province"))); // 省份
				}
				if (null != infoMap.get("city") && !"".equals(infoMap.get("city"))) {
					customerPO.setCity(Long.parseLong(infoMap.get("city"))); // 地级市
				}
				if (null != infoMap.get("district") && !"".equals(infoMap.get("district"))) {
					customerPO.setTown(Long.parseLong(infoMap.get("district"))); // 区、县
				}
				customerPO.setAddress(infoMap.get("address").trim()); // 详细地址
				if (null != infoMap.get("post_code") && !"".equals(infoMap.get("post_code"))) {
					customerPO.setPostCode(infoMap.get("post_code").trim()); // 邮政编码
				}
				customerPO.setStatus(Constant.STATUS_ENABLE); // 客户状态
				if (infoMap.get("customer_type").equals(Constant.CUSTOMER_TYPE_02 + "")) {
					customerPO.setCompanyName(infoMap.get("company_name").trim()); // 公司名称
					if (null != infoMap.get("company_s_name") && !"".equals(infoMap.get("company_s_name"))) {
						customerPO.setCompanySName(infoMap.get("company_s_name").trim()); // 公司简称
					}
					if (null != infoMap.get("kind") && !"".equals(infoMap.get("kind"))) {
						customerPO.setKind(Integer.parseInt(infoMap.get("kind"))); // 公司性质
					}
					if (null != infoMap.get("level_id") && !"".equals(infoMap.get("level_id"))) {
						customerPO.setLevelId(Integer.parseInt(infoMap.get("level_id"))); // 公司规模
					}
					customerPO.setCompanyPhone(infoMap.get("company_phone").trim()); // 公司电话
					if (null != infoMap.get("vehicle_num") && !"".equals(infoMap.get("vehicle_num"))) {
						customerPO.setVehicleNum(Integer.parseInt(infoMap.get("vehicle_num").trim())); // 目前车辆数
					}
				}

			}
			// 其他联系人信息
			Map<String, String[]> linkManMap = getLinkManInfo();
			// 新增客户信息
			if ((null == ctmId_Old || "".equals(ctmId_Old) || "0".equals(ctmId_Old)) && initialIsFleet.equals(Constant.IF_TYPE_NO + "")) {
				customerPO.setCreateDate(new Date());
				customerPO.setCreateBy(logonUser.getUserId());
				// 如果是新客户，新增客户信息
				dao.insert(customerPO);
				// 新增实销信息
				saveReportInfo_AddorUpdateSales(vehicleId, ctmId_Old, oldCustomerId, initialIsOldCtm, isOldCtm, orderId, is_fleet, ctmId_Add + "", "", s_dealer_id, s_oem_company_id, s_dlr_company_id);
				// 新增其他联系人
				saveReportInfo_AddorUpdateLinkMan(ctmId_Add + "", linkManMap, s_dlr_company_id, s_oem_company_id);
			} else {
				// 修改客户信息：修改前为“新客户”，修改后为“新客户”
				if (initialIsOldCtm.equals(Constant.IS_OLD_CTM_01 + "") && isOldCtm.equals(Constant.IS_OLD_CTM_01 + "")) {
					TtCustomerPO tempPO = new TtCustomerPO();
					tempPO.setCtmId(Long.parseLong(ctmId_Old));
					dao.update(tempPO, customerPO);
					System.out.println(infoMap.get("ctmId_Old"));
					// 只修改销售信息，不修改客户id
					saveReportInfo_AddorUpdateSales(vehicleId, ctmId_Old, oldCustomerId, initialIsOldCtm, isOldCtm, orderId, is_fleet, "", "", s_dealer_id, s_oem_company_id, s_dlr_company_id);
					// 修改其他联系人信息
					saveReportInfo_AddorUpdateLinkMan(ctmId_Old, linkManMap, s_dlr_company_id, s_oem_company_id);
				}
				// 修改客户信息：修改前为“新客户”，修改后为“老客户”，删除原客户信息，修改TT_DEALER_ACTUAL_SALES的ctmid
				if (initialIsOldCtm.equals(Constant.IS_OLD_CTM_01 + "") && isOldCtm.equals(Constant.IS_OLD_CTM_02 + "")) {
					TtCustomerPO tempPO = new TtCustomerPO();
					tempPO.setCtmId(Long.parseLong(ctmId_Old));
					dao.delete(tempPO);
					// 修改销售信息同时，修改客户id为用户选择的老客户id
					saveReportInfo_AddorUpdateSales(vehicleId, ctmId_Old, oldCustomerId, initialIsOldCtm, isOldCtm, orderId, is_fleet, "", "update", s_dealer_id, s_oem_company_id, s_dlr_company_id);
					// 修改其他联系人信息
					saveReportInfo_AddorUpdateLinkMan(oldCustomerId, linkManMap, s_dlr_company_id, s_oem_company_id);
				}
				// 修改客户信息：修改前为“新客户”，修改后为“大客户”，删除原客户信息，修改TT_DEALER_ACTUAL_SALES的ctmid
				if (initialIsOldCtm.equals(Constant.IS_OLD_CTM_01 + "") && is_fleet.equals(Constant.IF_TYPE_YES + "")) {
					TtCustomerPO tempPO = new TtCustomerPO();
					tempPO.setCtmId(Long.parseLong(ctmId_Old));
					dao.delete(tempPO);

					saveReportInfo_Fleet(vehicleId, s_oem_company_id, s_dlr_company_id, s_dealer_id);
				}

				// 修改客户信息：修改前为“老客户”，修改后为“新客户”，新增客户信息，修改TT_DEALER_ACTUAL_SALES的ctmid
				if (initialIsOldCtm.equals(Constant.IS_OLD_CTM_02 + "") && isOldCtm.equals(Constant.IS_OLD_CTM_01 + "")) {
					Long ctmId = Long.parseLong(SequenceManager.getSequence(""));
					customerPO.setCtmId(ctmId);
					dao.insert(customerPO);
					TtDealerActualSalesPO tempsalesPO = new TtDealerActualSalesPO();
					tempsalesPO.setOrderId(Long.parseLong(orderId));
					TtDealerActualSalesPO valuesalesPO = new TtDealerActualSalesPO();
					valuesalesPO.setCtmId(ctmId);
					valuesalesPO.setIsOldCtm(Constant.IS_OLD_CTM_01);
					dao.update(tempsalesPO, valuesalesPO);
					// 修改其他联系人信息
					saveReportInfo_AddorUpdateLinkMan(ctmId + "", linkManMap, s_dlr_company_id, s_oem_company_id);
				}
				// 修改客户信息：修改前为“老客户”，修改后为“老客户”，修改销售信息，修改老客户id
				if (initialIsOldCtm.equals(Constant.IS_OLD_CTM_02 + "") && isOldCtm.equals(Constant.IS_OLD_CTM_02 + "")) {
					// 修改客户id为用户选择的老客户id
					saveReportInfo_AddorUpdateSales(vehicleId, ctmId_Old, oldCustomerId, initialIsOldCtm, isOldCtm, orderId, is_fleet, "", "update", s_dealer_id, s_oem_company_id, s_dlr_company_id);
					// 修改其他联系人信息
					saveReportInfo_AddorUpdateLinkMan(ctmId_Old, linkManMap, s_dlr_company_id, s_oem_company_id);
				}
				// 修改客户信息：修改前为“老客户”，修改后为“大客户”
				if (initialIsOldCtm.equals(Constant.IS_OLD_CTM_02 + "") && is_fleet.equals(Constant.IF_TYPE_YES + "")) {
					saveReportInfo_Fleet(vehicleId, s_oem_company_id, s_dlr_company_id, s_dealer_id);
				}
				// 修改客户信息：修改前为“大客户”，修改后为“大客户”
				if (initialIsFleet.equals(Constant.IF_TYPE_YES + "") && is_fleet.equals(Constant.IF_TYPE_YES + "")) {
					saveReportInfo_Fleet(vehicleId, s_oem_company_id, s_dlr_company_id, s_dealer_id);
				}
				// 修改客户信息：修改前为“大客户”，修改后为“新客户”,新增客户信息，修改“实销”
				if (initialIsFleet.equals(Constant.IF_TYPE_YES + "") && is_fleet.equals(Constant.IF_TYPE_YES + "")) {
					TtDealerActualSalesPO salesPO = new TtDealerActualSalesPO();
					salesPO.setVehicleId(Long.parseLong(vehicleId));
					dao.delete(salesPO);
					customerPO.setCreateDate(new Date());
					customerPO.setCreateBy(logonUser.getUserId());
					// 如果是新客户，新增客户信息
					dao.insert(customerPO);
					// 新增实销信息
					saveReportInfo_AddorUpdateSales(vehicleId, ctmId_Old, oldCustomerId, initialIsOldCtm, isOldCtm, orderId, is_fleet, ctmId_Add + "", "", s_dealer_id, s_oem_company_id, s_dlr_company_id);
					// 新增其他联系人
					saveReportInfo_AddorUpdateLinkMan(ctmId_Add + "", linkManMap, s_dlr_company_id, s_oem_company_id);
				}
				// 修改客户信息：修改前为“大客户”，修改后为“老客户”
				if (initialIsFleet.equals(Constant.IF_TYPE_YES + "") && isOldCtm.equals(Constant.IS_OLD_CTM_02 + "")) {
					// TtDealerActualSalesPO salesPO = new
					// TtDealerActualSalesPO();
					// salesPO.setVehicleId(Long.parseLong(vehicleId));
					// dao.delete(salesPO);

					saveReportInfo_AddorUpdateSales(vehicleId, ctmId_Old, oldCustomerId, initialIsOldCtm, isOldCtm, orderId, is_fleet, ctmId_Add + "", "update", s_dealer_id, s_oem_company_id, s_dlr_company_id);
					// 新增其他联系人
					saveReportInfo_AddorUpdateLinkMan(ctmId_Add + "", linkManMap, s_dlr_company_id, s_oem_company_id);
				}
			}
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "实销信息上报:新增客户信息");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * FUNCTION : 实销信息上报:新增/修改 实销信息
	 * 
	 * @param :
	 *            String vehicleId:上报车辆id String
	 *            ctmId_Old:保存后的客户id,如果ctmId_Old有值，则修改客户信息，否则新增 String
	 *            oldCustomerId:用户选择的老客户id String initialIsOldCtm:修改前"新/老客户类型"
	 *            String isOldCtm ：修改后用户选择的"新/老客户类型" String orderId:实销id String
	 *            is_fleet:是否是大客户 String ctmId_Add:新增客户id String
	 *            isUpdateCtmId:是否修改实销表的客户id LastUpdate : 2010-6-17
	 */
	public void saveReportInfo_AddorUpdateSales(String vehicleId, String ctmId_Old, String oldCustomerId, String initialIsOldCtm, String isOldCtm, String orderId, String is_fleet, String ctmId_Add, String isUpdateCtmId, String s_dealer_id, String s_oem_company_id, String s_dlr_company_id) {
		AclUserBean logonUser = null;
		try {
			RequestWrapper request = act.getRequest();
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			act.getSession().get(Constant.LOGON_USER);

			Map<String, String> infoMap = saveReportInfo_getJSPInfo();
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

			TtDealerActualSalesPO salesPO = new TtDealerActualSalesPO();
			Long oid = Long.parseLong(SequenceManager.getSequence(""));
			if (null == orderId || "".equals(orderId) || "null".equals(orderId)) {
				salesPO.setOrderId(oid); // 实销ID
			}
			if (null != s_dealer_id && !"".equals(s_dealer_id)) {
				salesPO.setDealerId(Long.parseLong(s_dealer_id)); // 经销商渠道
			}
			if (null != s_oem_company_id && !"".equals(s_oem_company_id)) {
				salesPO.setOemCompanyId(Long.parseLong(s_oem_company_id)); // 车厂公司ID
			}
			if (null != s_dlr_company_id && !"".equals(s_dlr_company_id)) {
				salesPO.setDlrCompanyId(Long.parseLong(s_dlr_company_id)); // 经销商公司ID
			}
			salesPO.setVehicleId(Long.parseLong(vehicleId)); // 车辆ID
			salesPO.setInvoiceDate(dateFormat.parse(infoMap.get("invoice_date"))); // 开票日期
			salesPO.setInvoiceNo(infoMap.get("invoice_no")); // 发票编号
			if (null != infoMap.get("contractNo") && !"".equals(infoMap.get("contractNo"))) {
				salesPO.setContractNo(infoMap.get("contractNo").trim()); // 合同编号
			}
			salesPO.setPrice(Double.parseDouble(infoMap.get("price"))); // 价格
			salesPO.setPayment(Integer.parseInt(infoMap.get("payment"))); // 付款方式
			if (null != infoMap.get("miles") && !"".equals(infoMap.get("miles"))) {
				salesPO.setMiles(Float.parseFloat(infoMap.get("miles"))); // 交付时公里数
			}
			salesPO.setConsignationDate(dateFormat.parse(infoMap.get("consignation_date"))); // 车辆交付日期
			if (null != infoMap.get("vehicle_no") && !"".equals(infoMap.get("vehicle_no"))) {
				salesPO.setVehicleNo(infoMap.get("vehicle_no").trim()); // 车牌号
			}
			if (null != infoMap.get("insurance_company") && !"".equals(infoMap.get("insurance_company"))) {
				salesPO.setInsuranceCompany(infoMap.get("insurance_company").trim()); // 保险公司
			}
			if (null != infoMap.get("insurance_date") && !"".equals(infoMap.get("insurance_date"))) {
				salesPO.setInsuranceDate(dateFormat.parse(infoMap.get("insurance_date"))); // 保险日期
			}
			if (null != infoMap.get("memo") && !"".equals(infoMap.get("memo"))) {
				salesPO.setMemo(infoMap.get("memo").trim()); // 备注
			}

			salesPO.setIsFleet(Integer.parseInt(infoMap.get("is_fleet"))); // 是否大客户
			if (infoMap.get("is_fleet").equals(Constant.IF_TYPE_YES + "")) {
				salesPO.setFleetId(Long.parseLong(infoMap.get("fleet_id"))); // 大客户ID
				salesPO.setContractId(Long.parseLong(infoMap.get("fleet_contract_no_id"))); // 大客户合同ID
				salesPO.setFleetStatus(Integer.parseInt(infoMap.get("fleet_check_status"))); // 大客户审核状态
			}
			salesPO.setStatus(Constant.STATUS_ENABLE); // 状态
			salesPO.setSalesDate(new Date()); // 实销上报日期
			salesPO.setIsOldCtm(Integer.parseInt(infoMap.get("sel_cus_type"))); // 新客户
			salesPO.setCreateDate(new Date());
			salesPO.setCreateBy(logonUser.getUserId());
			// 如果是新增实销信息
			if (null == orderId || "".equals(orderId) || "null".equals(orderId)) {
				salesPO.setCtmId(Long.parseLong(ctmId_Add));
				// 如果用户选择“新客户”，客户id为新增客户
				if (infoMap.get("sel_cus_type").equals(Constant.IS_OLD_CTM_01 + "")) {
					salesPO.setCtmId(Long.parseLong(ctmId_Add));
				} else {
					// 如果用户选择“老客户”，客户id为用户选择的老客户id
					salesPO.setCtmId(Long.parseLong(oldCustomerId));
				}
				dao.insert(salesPO);
			} else {
				// 如果修改实销的客户id
				if (null != isUpdateCtmId && "update".equals(isUpdateCtmId) && null != oldCustomerId && !"".equals(oldCustomerId)) {
					salesPO.setCtmId(Long.parseLong(oldCustomerId));
				}

				TtDealerActualSalesPO tempPO = new TtDealerActualSalesPO();
				tempPO.setOrderId(Long.parseLong(orderId));
				dao.update(tempPO, salesPO);
			}
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "实销信息上报:新增/修改 实销信息");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * FUNCTION : 实销信息上报:保存上报信息:大客户
	 * 
	 * @param :
	 * @return :
	 * @throws :
	 *             LastUpdate : 2010-6-9
	 */
	public void saveReportInfo_Fleet(String vehicleId, String s_oem_company_id, String s_dlr_company_id, String s_dealer_id) {
		AclUserBean logonUser = null;
		try {
			RequestWrapper request = act.getRequest();
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			act.getSession().get(Constant.LOGON_USER);

			TtDealerActualSalesPO salesPO = new TtDealerActualSalesPO();
			salesPO.setVehicleId(Long.parseLong(vehicleId));
			dao.delete(salesPO);

			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

			Map<String, String> infoMap = saveReportInfo_getJSPInfo();
			TtDealerActualSalesPO actualSalesPO = new TtDealerActualSalesPO();
			actualSalesPO.setOrderId(Long.parseLong(SequenceManager.getSequence(""))); // 实销ID
			actualSalesPO.setOemCompanyId(Long.parseLong(s_oem_company_id)); // 车厂公司ID
			actualSalesPO.setDlrCompanyId(Long.parseLong(s_dlr_company_id)); // 经销商公司ID
			actualSalesPO.setDealerId(Long.parseLong(s_dealer_id)); // 经销商渠道
			if (null != infoMap.get("vehicle_no") && !"".equals(infoMap.get("vehicle_no"))) {
				actualSalesPO.setVehicleNo(infoMap.get("vehicle_no").trim()); // 车牌号
			}
			if (null != infoMap.get("contractNo") && !"".equals(infoMap.get("contractNo"))) {
				actualSalesPO.setContractNo(infoMap.get("contractNo").trim()); // 合同编号
			}
			actualSalesPO.setInvoiceDate(dateFormat.parse(infoMap.get("invoice_date"))); // 开票日期
			actualSalesPO.setInvoiceNo(infoMap.get("invoice_no").trim()); // 发票编号
			if (null != infoMap.get("insurance_company") && !"".equals(infoMap.get("insurance_company"))) {
				actualSalesPO.setInsuranceCompany(infoMap.get("insurance_company").trim()); // 保险公司
			}
			if (null != infoMap.get("insurance_date") && !"".equals(infoMap.get("insurance_date"))) {
				actualSalesPO.setInsuranceDate(dateFormat.parse(infoMap.get("insurance_date"))); // 保险日期
			}
			actualSalesPO.setConsignationDate(dateFormat.parse(infoMap.get("consignation_date"))); // 车辆交付日期
			if (null != infoMap.get("miles") && !"".equals(infoMap.get("miles"))) {
				actualSalesPO.setMiles(Float.parseFloat(infoMap.get("miles"))); // 交付时公里数
			}
			actualSalesPO.setPayment(Integer.parseInt(infoMap.get("payment"))); // 付款方式
			actualSalesPO.setPrice(Double.parseDouble(infoMap.get("price"))); // 价格
			if (null != infoMap.get("memo") && !"".equals(infoMap.get("memo"))) {
				actualSalesPO.setMemo(infoMap.get("memo").trim()); // 备注
			}
			actualSalesPO.setIsFleet(Constant.IF_TYPE_YES); // 是否大客户
			actualSalesPO.setFleetId(Long.parseLong(infoMap.get("fleet_id"))); // 大客户ID
			actualSalesPO.setContractId(Long.parseLong(infoMap.get("fleet_contract_no_id"))); // 大客户合同ID
			actualSalesPO.setVehicleId(Long.parseLong(vehicleId));
			dao.insert(actualSalesPO);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "实销信息上报:保存上报信息:大客户");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * FUNCTION : 实销信息上报:保存上报信息
	 * 
	 * @param :
	 * @return :
	 * @throws :
	 *             LastUpdate : 2010-6-9
	 */
	public void saveReportInfo() {
		AclUserBean logonUser = null;
		try {
			RequestWrapper request = act.getRequest();
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			act.getSession().get(Constant.LOGON_USER);

			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			// 1.上报车辆id
			String vehicleId = CommonUtils.checkNull(request.getParamValue("vehicle_id"));
			// 2.保存后的客户id:如果ctmId_Old有值，则修改客户信息，否则新增
			String ctmId_Old = CommonUtils.checkNull(request.getParamValue("ctmId_Old"));
			// 3.用户选择的老客户id
			String oldCustomerId = CommonUtils.checkNull(request.getParamValue("oldCustomerId"));
			// 4.修改前"新/老客户类型"
			String initialIsOldCtm = CommonUtils.checkNull(request.getParamValue("initialIsOldCtm"));
			// 5.修改后用户选择的"新/老客户类型"
			String isOldCtm = CommonUtils.checkNull(request.getParamValue("sel_cus_type"));
			// 6.实销id
			String orderId = CommonUtils.checkNull(request.getParamValue("orderId"));
			// 7.是否是大客户
			String is_fleet = CommonUtils.checkNull(request.getParamValue("is_fleet"));
			// 8.修改前是否是大客户
			String initialIsFleet = CommonUtils.checkNull(request.getParamValue("initialIsFleet"));
			/*
			 * 查询： 1.车厂公司ID： OEM_COMPANY_ID 2.经销商公司ID： DLR_COMPANY_ID 3.经销商渠道：
			 * DEALER_ID
			 */
			String s_dealer_id = ""; // 经销商渠道
			String s_oem_company_id = ""; // 车厂公司ID
			String s_dlr_company_id = ""; // 经销商公司ID

			TmVehiclePO tmVehiclePO = new TmVehiclePO();
			tmVehiclePO.setVehicleId(Long.parseLong(vehicleId));
			List vehicleList = dao.select(tmVehiclePO);
			if (null != vehicleList && vehicleList.size() > 0) {
				s_dealer_id = ((TmVehiclePO) vehicleList.get(0)).getDealerId() + "";
				TmDealerPO dealerPO = new TmDealerPO();
				dealerPO.setDealerId(((TmVehiclePO) vehicleList.get(0)).getDealerId());
				List dealerList = dao.select(dealerPO);
				if (null != dealerList && dealerList.size() > 0) {
					s_oem_company_id = ((TmDealerPO) dealerList.get(0)).getOemCompanyId() + "";
					s_dlr_company_id = ((TmDealerPO) dealerList.get(0)).getCompanyId() + "";
				}
			}

			// 如果是大客户
			if (null != is_fleet && is_fleet.equals(Constant.IF_TYPE_YES + "")) {
				saveReportInfo_Fleet(vehicleId, s_oem_company_id, s_dlr_company_id, s_dealer_id);
			} else {
				// 修改客户信息
				if (null != ctmId_Old && !"".equals(ctmId_Old) && !"0".equals(ctmId_Old)) {
					TtDealerActualSalesPO salesPO = new TtDealerActualSalesPO();
					salesPO.setVehicleId(Long.parseLong(vehicleId));
					List list = dao.select(salesPO);
					Long order_id = ((TtDealerActualSalesPO) list.get(0)).getOrderId();
					saveReportInfo_AddorUpdateCtm(vehicleId, ctmId_Old, oldCustomerId, initialIsOldCtm, isOldCtm, orderId, is_fleet, s_dealer_id, s_oem_company_id, s_dlr_company_id, initialIsFleet);
				} else {
					// 新增客户信息
					saveReportInfo_AddorUpdateCtm(vehicleId, ctmId_Old, oldCustomerId, initialIsOldCtm, isOldCtm, orderId, is_fleet, s_dealer_id, s_oem_company_id, s_dlr_company_id, initialIsFleet);
				}

			}

			salesReportInit();
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "实销信息上报:保存上报信息");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * FUNCTION : 实销信息上报:删除其他联系人
	 * 
	 * @param :
	 * @return :
	 * @throws :
	 *             LastUpdate : 2010-6-17
	 */
	public void delOtherLinkMan() {
		AclUserBean logonUser = null;
		try {
			RequestWrapper request = act.getRequest();
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			act.getSession().get(Constant.LOGON_USER);
			String lm_id = request.getParamValue("lm_id");
			TtLinkmanPO linkmanPO = new TtLinkmanPO();
			linkmanPO.setLmId(Long.parseLong(lm_id));
			dao.delete(linkmanPO);
			act.setOutData("returnValue", 1);
			act.setForword(otherLinkManListURL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "实销信息上报:删除其他联系人");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * FUNCTION : 实销信息上报:查询大客户列表
	 * 
	 * @param :
	 * @return :
	 * @throws :
	 *             LastUpdate : 2010-6-18
	 */
	public void toQueryFleetList() {
		AclUserBean logonUser = null;
		try {
			RequestWrapper request = act.getRequest();
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			act.getSession().get(Constant.LOGON_USER);
			act.setForword(toQueryFleetListURL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "实销信息上报:查询大客户列表");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * FUNCTION : 实销信息上报:查询大客户列表结果展示
	 * 
	 * @param :
	 * @return :
	 * @throws :
	 *             LastUpdate : 2010-6-18
	 */
	public void getFleetList() {
		AclUserBean logonUser = null;
		try {
			RequestWrapper request = act.getRequest();
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			act.getSession().get(Constant.LOGON_USER);
			String dealerIds = logonUser.getDealerId();
			Long dlrCompanyId = logonUser.getCompanyId();
			Long oemCompanyId = new Long(logonUser.getOemCompanyId());
			String fleet_name = CommonUtils.checkNull(request.getParamValue("fleet_name"));
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
			PageResult<Map<String, Object>> ps = SalesReportDAO.getFleetList(fleet_name, dlrCompanyId, oemCompanyId, Constant.PAGE_SIZE, curPage);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "实销信息上报:查询大客户列表结果展示");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * FUNCTION : 实销信息上报:查询销售顾问列表
	 * 
	 * @param :
	 * @return :
	 * @throws :
	 *             LastUpdate : 2010-6-18
	 */
	public void toSalesManList() {
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			act.getSession().get(Constant.LOGON_USER);
			act.setForword(toSalesManListURL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "实销信息上报:查询销售顾问列表");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
    /**
	 * FUNCTION : 实销信息上报:查询市场活动列表
	 *
	 * @param :
	 * @return :
	 * @throws :
	 *             LastUpdate : 2010-6-18
	 */
	public void toActivitiesList() {
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			act.getSession().get(Constant.LOGON_USER);
			act.setForword(toActivitiesListURL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "实销信息上报:查询市场活动列表");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * FUNCTION : 实销信息上报:查询大客户列表结果展示
	 * 
	 * @param :
	 * @return :
	 * @throws :
	 *             LastUpdate : 2010-6-18
	 */
	public void getSalesManList() {
		AclUserBean logonUser = null;
		try {
			RequestWrapper request = act.getRequest();
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			act.getSession().get(Constant.LOGON_USER);
			Long dealerId = logonUser.getDealerId() == null ? null : new Long(logonUser.getDealerId());
			Long oemCompanyId = new Long(logonUser.getOemCompanyId());
			String name = CommonUtils.checkNull(request.getParamValue("name"));
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
			PageResult<Map<String, Object>> ps = SalesReportDAO.getSalesManList(name, dealerId, oemCompanyId, Constant.PAGE_SIZE, curPage);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "实销信息上报:查询销售顾问列表结果展示");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * FUNCTION : 实销信息上报:查询市场活动结果展示
	 *
	 * @param :
	 * @return :
	 * @throws :
	 *             LastUpdate : 2010-6-18
	 */
	public void getActivitiesList() {
		AclUserBean logonUser = null;
		try {
			RequestWrapper request = act.getRequest();
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			act.getSession().get(Constant.LOGON_USER);
			Long dealerId = logonUser.getDealerId() == null ? null : new Long(logonUser.getDealerId());
			Long oemCompanyId = new Long(logonUser.getOemCompanyId());
			String campaignName = CommonUtils.checkNull(request.getParamValue("campaignName"));
            Map<String,String> map=new HashMap<String, String>();
            map.put("dealerId",dealerId.toString());
            map.put("campaignName",campaignName);
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
			PageResult<Map<String, Object>> ps = SalesReportDAO.getActivitiesList(map, Constant.PAGE_SIZE, curPage);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "实销信息上报:查询市场活动列表结果展示");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * FUNCTION : 实销信息上报:查询大客户合同列表
	 * 
	 * @param :
	 * @return :
	 * @throws :
	 *             LastUpdate : 2010-6-18
	 */
	public void toQueryFleetContractList() {
		AclUserBean logonUser = null;
		try {
			RequestWrapper request = act.getRequest();
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			act.getSession().get(Constant.LOGON_USER);
			act.setForword(toQueryFleetContractListURL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "实销信息上报:查询大客户合同列表");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * FUNCTION : 实销信息上报:查询大客户合同列表结果展示
	 * 
	 * @param :
	 * @return :
	 * @throws :
	 *             LastUpdate : 2010-6-18
	 */
	public void getFleetContractList() {
		AclUserBean logonUser = null;
		try {
			RequestWrapper request = act.getRequest();
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			act.getSession().get(Constant.LOGON_USER);
			String contract_no = CommonUtils.checkNull(request.getParamValue("contract_no"));
			String contract_amount = CommonUtils.checkNull(request.getParamValue("contract_amount"));
			String startDate = CommonUtils.checkNull(request.getParamValue("startDate"));
			String endDate = CommonUtils.checkNull(request.getParamValue("endDate"));
			String fleet_id = CommonUtils.checkNull(request.getParamValue("fleet_id"));

			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
			PageResult<Map<String, Object>> ps = SalesReportDAO.getFleetContractList(contract_no, contract_amount, startDate, endDate, fleet_id, Constant.PAGE_SIZE, curPage);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "实销信息上报:查询大客户合同列表结果展示");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * FUNCTION : 实销信息上报:查询客户列表
	 * 
	 * @param :
	 * @return :
	 * @throws :
	 *             LastUpdate : 2010-6-10
	 */
	public void toQueryCtmList() {
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			act.getSession().get(Constant.LOGON_USER);
			act.setForword(customerListURL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "实销信息上报:查询客户列表");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * FUNCTION : 实销信息上报:查询联系人列表
	 * 
	 * @param :
	 * @return :
	 * @throws :
	 *             LastUpdate : 2010-6-11
	 */
	public void toQueryLinkManList() {
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			act.getSession().get(Constant.LOGON_USER);
			act.setForword(linkManListURL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "实销信息上报:查询客户列表");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * FUNCTION : 实销信息上报:查询联系人列表结果展示
	 * 
	 * @param :
	 * @return :
	 * @throws :
	 *             LastUpdate : 2010-6-11
	 */
	public void queryLinkManList() {
		AclUserBean logonUser = null;
		try {
			RequestWrapper request = act.getRequest();
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			act.getSession().get(Constant.LOGON_USER);

			String linkManName = CommonUtils.checkNull(request.getParamValue("linkManName"));
			String ctm_id = CommonUtils.checkNull(request.getParamValue("ctmId_Old"));
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
			PageResult<Map<String, Object>> ps = SalesReportDAO.getLinkManList(linkManName, ctm_id, Constant.PAGE_SIZE, curPage);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "实销信息上报:查询客户列表");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * FUNCTION : 实销信息上报:新增其他联系人
	 * 
	 * @param :
	 * @return :
	 * @throws :
	 *             LastUpdate : 2010-6-11
	 */
	public void toAddLinkMan() {
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			act.getSession().get(Constant.LOGON_USER);
			act.setForword(toAddLinkManURL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "实销信息上报:新增其他联系人");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * FUNCTION : 实销信息上报:查询客户列表展示
	 * 
	 * @param :
	 * @return :
	 * @throws :
	 *             LastUpdate : 2010-6-10
	 */
	public void queryCtmList() {
		AclUserBean logonUser = null;
		try {
			RequestWrapper request = act.getRequest();
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			act.getSession().get(Constant.LOGON_USER);
			String dealerId = logonUser.getDealerId();
			String customerName = CommonUtils.checkNull(request.getParamValue("customerName"));
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
			PageResult<Map<String, Object>> ps = SalesReportDAO.getCustomerList(logonUser.getDealerId(), customerName, Constant.PAGE_SIZE, curPage);
			act.setOutData("ps", ps);
			act.setForword(customerListURL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "实销信息上报:查询客户列表");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * FUNCTION : 实销信息上报:查询老客户详细信息
	 * 
	 * @param :
	 * @return :
	 * @throws :
	 *             LastUpdate : 2010-6-10
	 */
	public void getOldCustomerInfo() {
		AclUserBean logonUser = null;
		try {
			RequestWrapper request = act.getRequest();
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			act.getSession().get(Constant.LOGON_USER);

			String ctmId_Old = CommonUtils.checkNull(request.getParamValue("ctmId_Old"));
			TtCustomerPO customerPO = new TtCustomerPO();
			customerPO.setCtmId(Long.parseLong(ctmId_Old));
			List cusList = dao.select(customerPO);
			if (null != cusList && cusList.size() > 0) {
				TtCustomerPO oldCustomerInfo = (TtCustomerPO) cusList.get(0);
				act.setOutData("oldCustomerInfo", oldCustomerInfo);
			}

		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "实销信息上报:查询老客户详细信息");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * FUNCTION : 下级经销商实销信息查询:查询历史上报信息
	 * 
	 * @param :
	 * @return :
	 * @throws :
	 *             LastUpdate : 2010-6-7
	 */
	public void queryDealerReportInfo() {
		AclUserBean logonUser = null;
		try {
			RequestWrapper request = act.getRequest();
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			act.getSession().get(Constant.LOGON_USER);

			String customer_type = CommonUtils.checkNull(request.getParamValue("customer_type")); // 客户类型
			String customer_name = CommonUtils.checkNull(request.getParamValue("customer_name")); // 客户名称
			String vin = CommonUtils.checkNull(request.getParamValue("vin")); // VIN
			String customer_phone = CommonUtils.checkNull(request.getParamValue("customer_phone")); // 客户电话
			String fleet_name = CommonUtils.checkNull(request.getParamValue("fleet_name")); // 大客户名称
			String materialCode = CommonUtils.checkNull(request.getParamValue("materialCode")); // 选择物料组
			String is_fleet = CommonUtils.checkNull(request.getParamValue("is_fleet")); // 是否大客户
			String contract_no = CommonUtils.checkNull(request.getParamValue("contract_no")); // 大客户合同
			String startDate = CommonUtils.checkNull(request.getParamValue("startDate")); // 上报日期:开始
			String endDate = CommonUtils.checkNull(request.getParamValue("endDate")); // 上报日期
																						// ：截止
			String dealerId = CommonUtils.checkNull(request.getParamValue("dealerId")); // 下级经销商实销信息查询
			Map<String, String> map = new HashMap<String, String>();
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
			map.put("poseId", logonUser.getPoseId().toString());
			// List<Map<String, Object>> listA =
			// DealerInfoDao.getInstance().getDel(logonUser.getDealerId()) ;
			// //获得二级经销商
			String dealerIdA = DealerInfoDao.getInstance().getDel__(logonUser.getDealerId());
			/*
			 * for(int i=0; i<listA.size();i++) { dealerIdA += "," +
			 * listA.get(i).get("DEALER_ID").toString() ; }
			 */
			if (dealerId != null && !dealerId.equals("")) {
				dealerIdA = dealerId;
			}
			if ("".equals(dealerIdA)) {
				dealerIdA = "''";
			}
			String areaId = request.getParamValue("areaId");

			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
			PageResult<Map<String, Object>> ps = SalesReportDAO.queryReportInfoList(areaId, dealerIdA, map, Constant.PAGE_SIZE, curPage);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "实销信息查询:查询历史上报信息");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * FUNCTION : 实销信息查询:查询历史上报信息
	 * 
	 * @param :
	 * @return :
	 * @throws :
	 *             LastUpdate : 2010-6-7
	 */
	public void queryReportInfo() {
		AclUserBean logonUser = null;
		try {
			RequestWrapper request = act.getRequest();
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			act.getSession().get(Constant.LOGON_USER);

			String customer_type = CommonUtils.checkNull(request.getParamValue("customer_type")); // 客户类型
			String customer_name = CommonUtils.checkNull(request.getParamValue("customer_name")); // 客户名称
			String vin = CommonUtils.checkNull(request.getParamValue("vin")); // VIN
//			vin = setStr(vin, "L"); // 由于当前（2011/3/29）所有的vin均存在“L”，故以此作为查询条件加快查询速度
			String customer_phone = CommonUtils.checkNull(request.getParamValue("customer_phone")); // 客户电话
			String fleet_name = CommonUtils.checkNull(request.getParamValue("fleet_name")); // 大客户名称
			String materialCode = CommonUtils.checkNull(request.getParamValue("materialCode")); // 选择物料组
			String is_fleet = CommonUtils.checkNull(request.getParamValue("is_fleet")); // 是否大客户
			String contract_no = CommonUtils.checkNull(request.getParamValue("contract_no")); // 大客户合同
			String startDate = CommonUtils.checkNull(request.getParamValue("startDate")); // 上报日期:开始
			String endDate = CommonUtils.checkNull(request.getParamValue("endDate")); // 上报日期
																						// ：截止
			String linkAddr=CommonUtils.checkNull(request.getParamValue("linkAddr"));//联系地址
			String payment=CommonUtils.checkNull(request.getParamValue("payment"));//购置方式
			String bank=CommonUtils.checkNull(request.getParamValue("bank"));//按揭银行
			String loansYear=CommonUtils.checkNull(request.getParamValue("loansYear"));//贷款年限
			String beStatus=CommonUtils.checkNull(request.getParamValue("be_status"));//转介绍类型
			Map<String, String> map = new HashMap<String, String>();
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
			map.put("poseId", logonUser.getPoseId().toString());
			map.put("linkAddr", linkAddr);
			map.put("payment", payment);
			map.put("bank",bank);
			map.put("loansYear", loansYear);
			map.put("beStatus", beStatus);

			// List<Map<String, Object>> listA =
			// DealerInfoDao.getInstance().getDel(logonUser.getDealerId()) ;
			// //获得二级经销商
			DealerRelation dr = new DealerRelation();

		//	String dealerIdA = dr.getDealerIdByPose(logonUser.getCompanyId(), logonUser.getPoseId());
			/*
			 * for(int i=0; i<listA.size();i++) { dealerIdA += "," +
			 * listA.get(i).get("DEALER_ID").toString() ; }
			 */
			String dealerIdA=logonUser.getDealerId();
			String areaId = request.getParamValue("areaId");
			Long poseId = logonUser.getPoseId();
			
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
			PageResult<Map<String, Object>> ps = null;

			String para = CommonDAO.getPara(Constant.CHANA_SYS.toString());

			if (Constant.COMPANY_CODE_JC.equals(para.toUpperCase())) {
				ps = SalesReportDAO.queryReportInfoList(areaId, dealerIdA, map, Constant.PAGE_SIZE, curPage);
			} else if (Constant.COMPANY_CODE_CVS.equals(para.toUpperCase())) {
				ps = SalesReportDAO.queryReportInfoList_CVS(areaId, dealerIdA, map, Constant.PAGE_SIZE, curPage);
			} else {
				throw new RuntimeException("判断当前系统的系统参数错误！");
			}

			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "实销信息查询:查询历史上报信息");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	public void cusDealerDownLoad() {
		AclUserBean logonUser = null;
		RequestWrapper request = act.getRequest();
		logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		act.getSession().get(Constant.LOGON_USER);

		String customer_type = CommonUtils.checkNull(request.getParamValue("customer_type")); // 客户类型
		String customer_name = CommonUtils.checkNull(request.getParamValue("customer_name")); // 客户名称
		String vin = CommonUtils.checkNull(request.getParamValue("vin")); // VIN
		String customer_phone = CommonUtils.checkNull(request.getParamValue("customer_phone")); // 客户电话
		String fleet_name = CommonUtils.checkNull(request.getParamValue("fleet_name")); // 大客户名称
		String materialCode = CommonUtils.checkNull(request.getParamValue("materialCode")); // 选择物料组
		String is_fleet = CommonUtils.checkNull(request.getParamValue("is_fleet")); // 是否大客户
		String contract_no = CommonUtils.checkNull(request.getParamValue("contract_no")); // 大客户合同
		String startDate = CommonUtils.checkNull(request.getParamValue("startDate")); // 上报日期:开始
		String endDate = CommonUtils.checkNull(request.getParamValue("endDate")); // 上报日期
																					// ：截止
		String beStatus=CommonUtils.checkNull(request.getParamValue("be_status"));//转介绍类型
		Map<String, String> map = new HashMap<String, String>();
		String areaId = CommonUtils.checkNull(request.getParamValue("areaId"));
		map.put("areaId", areaId);
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
		map.put("beStatus", beStatus);

		// List<Map<String, Object>> listA =
		// DealerInfoDao.getInstance().getDel(logonUser.getDealerId()) ;
		// //获得二级经销商
		DealerRelation dr = new DealerRelation();
		String dealerIdA = dr.getDealerIdByPose(logonUser.getCompanyId(), logonUser.getPoseId()); // 当前用户职位对应的经销商ID

		// 导出的文件名
		String fileName = "实销信息明细.csv";
		// 导出的文字编码
		OutputStream os = null;
		try {
			fileName = new String(fileName.getBytes("GB2312"), "ISO8859-1");

			response.setContentType("Application/text/csv");
			response.addHeader("Content-Disposition", "attachment;filename=" + fileName);

			List<List<Object>> list = new LinkedList<List<Object>>();

			List<Object> listTemp = new LinkedList<Object>();
			listTemp.add("经销商代码");
			listTemp.add("经销商名称");
			listTemp.add("销售顾问名称");
			listTemp.add("客户名称");
			listTemp.add("客户类型");
			listTemp.add("客户地址");
			listTemp.add("是否集团客户");
			//listTemp.add("集团客户名称");
			listTemp.add("主要联系电话");
			//listTemp.add("联系人地址");
			listTemp.add("车系名称");
			listTemp.add("车型名称");
			listTemp.add("状态名称");
			listTemp.add("颜色");
			listTemp.add("物料代码");
			/* listTemp.add("物料名称"); */
			listTemp.add("VIN");
			listTemp.add("发动机号");
			listTemp.add("生产日期");
			listTemp.add("实销时间");

			// zhumingwei 2011-09-13 begin
			//listTemp.add("颜色");
			//listTemp.add("车辆交付日期");
			//listTemp.add("家庭月收入");
			//listTemp.add("职业");
			//listTemp.add("购车用途");
			//listTemp.add("性别");
			//listTemp.add("了解途径");
			//listTemp.add("教育程度");
			//listTemp.add("购买原因");
			//listTemp.add("客户类型");
			listTemp.add("购置方式");
			listTemp.add("按揭类型");
			listTemp.add("销售价格");
			listTemp.add("首付比例");
			// zhumingwei 2011-09-13 end
			listTemp.add("贷款方式");
			listTemp.add("贷款年限");
			/*listTemp.add("情报/老客户姓名");
			listTemp.add("情报/老客户电话");
			listTemp.add("老客户车架号");*/
			list.add(listTemp);
			List<Map<String, Object>> results = SalesReportDAO.getDealerCalDetailExportList(map, dealerIdA);
			for (int i = 0; i < results.size(); i++) {
				Map<String, Object> record = results.get(i);
				listTemp = new LinkedList<Object>();
				listTemp.add(CommonUtils.checkNull(record.get("DEALER_CODE")));
				listTemp.add(CommonUtils.checkNull(record.get("DEALER_SHORTNAME")));
				listTemp.add(CommonUtils.checkNull(record.get("NAME")));
				listTemp.add(CommonUtils.checkNull(record.get("C_NAME")));
				listTemp.add(CommonUtils.checkNull(record.get("CTM_TYPE")));
				listTemp.add(CommonUtils.checkNull(record.get("ADDRESS")));
				listTemp.add(CommonUtils.checkNull(record.get("IS_FLEET")));
				//listTemp.add(CommonUtils.checkNull(record.get("FLEET_NAME")));
				listTemp.add(CommonUtils.checkNull(record.get("MAIN_PHONE")));
				//listTemp.add(CommonUtils.checkNull(record.get("ADDRESS")));
				listTemp.add(CommonUtils.checkNull(record.get("VS")));
				listTemp.add(CommonUtils.checkNull(record.get("VT")));
				listTemp.add(CommonUtils.checkNull(record.get("SN")));
				listTemp.add(CommonUtils.checkNull(record.get("COLOR")));
				listTemp.add(CommonUtils.checkNull(record.get("MATERIAL_CODE")));
				/* listTemp.add(CommonUtils.checkNull(record.get("MATERIAL_NAME"))); */
				listTemp.add(CommonUtils.checkNull(record.get("VIN")));
				listTemp.add(CommonUtils.checkNull(record.get("ENGINE_NO")));
				listTemp.add(CommonUtils.checkNull(record.get("PRODUCT_DATE")));
				listTemp.add(CommonUtils.checkNull(record.get("SALES_DATE")));

				// zhumingwei 2011-09-13 begin
				//listTemp.add(CommonUtils.checkNull(record.get("COLOR")));
				//listTemp.add(CommonUtils.checkNull(record.get("CONSIGNATION_DATE")));
				//listTemp.add(CommonUtils.checkNull(record.get("INCOME")));
				//listTemp.add(CommonUtils.checkNull(record.get("PROFESSION")));
				//listTemp.add(CommonUtils.checkNull(record.get("SALES_ADDRESS")));
				//listTemp.add(CommonUtils.checkNull(record.get("SEX")));
				//listTemp.add(CommonUtils.checkNull(record.get("KNOW_ADDRESS")));
				//listTemp.add(CommonUtils.checkNull(record.get("EDUCATION")));
				//listTemp.add(CommonUtils.checkNull(record.get("SALES_RESON")));
				//listTemp.add(CommonUtils.checkNull(record.get("CTM_TYPE")));
				listTemp.add(CommonUtils.checkNull(record.get("PAYMENT")));
				listTemp.add(CommonUtils.checkNull(record.get("MORTGAGE_TYPE")));
				listTemp.add(CommonUtils.checkNull(record.get("SALES_PRICE")));
				if (record.get("SHOUFU_RATIO") == null ||  (record.get("SHOUFU_RATIO")!=null&&"0".equals(record.get("SHOUFU_RATIO").toString()))) {
					listTemp.add(CommonUtils.checkNull(""));
				} else {
					listTemp.add(CommonUtils.checkNull(record.get("SHOUFU_RATIO") + "%"));
				}

				listTemp.add(CommonUtils.checkNull(record.get("LOANS_TYPE")));
				listTemp.add(CommonUtils.checkNull(record.get("LOANS_YEAR")));
				/*listTemp.add(CommonUtils.checkNull(record.get("OLD_CUSTOMER_NAME")));
				listTemp.add(CommonUtils.checkNull(record.get("OLD_TELEPHONE")));
				listTemp.add(CommonUtils.checkNull(record.get("OLD_VEHICLE_ID")));*/
				// zhumingwei 2011-09-13 end

				list.add(listTemp);
			}
			os = response.getOutputStream();
			CsvWriterUtil.writeCsv(list, os);
			os.flush();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BizException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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

	public void cusDealerDownLoad_CVS() {
		AclUserBean logonUser = null;
		RequestWrapper request = act.getRequest();
		logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		act.getSession().get(Constant.LOGON_USER);

		String customer_type = CommonUtils.checkNull(request.getParamValue("customer_type")); // 客户类型
		String customer_name = CommonUtils.checkNull(request.getParamValue("customer_name")); // 客户名称
		String vin = CommonUtils.checkNull(request.getParamValue("vin")); // VIN
		String customer_phone = CommonUtils.checkNull(request.getParamValue("customer_phone")); // 客户电话
		String fleet_name = CommonUtils.checkNull(request.getParamValue("fleet_name")); // 大客户名称
		String materialCode = CommonUtils.checkNull(request.getParamValue("materialCode")); // 选择物料组
		String is_fleet = CommonUtils.checkNull(request.getParamValue("is_fleet")); // 是否大客户
		String contract_no = CommonUtils.checkNull(request.getParamValue("contract_no")); // 大客户合同
		String startDate = CommonUtils.checkNull(request.getParamValue("startDate")); // 上报日期:开始
		String endDate = CommonUtils.checkNull(request.getParamValue("endDate")); // 上报日期
																					// ：截止
		Map<String, String> map = new HashMap<String, String>();
		String areaId = CommonUtils.checkNull(request.getParamValue("areaId"));
		map.put("areaId", areaId);
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

		// List<Map<String, Object>> listA =
		// DealerInfoDao.getInstance().getDel(logonUser.getDealerId()) ;
		// //获得二级经销商
		DealerRelation dr = new DealerRelation();
		String dealerIdA = dr.getDealerIdByPose(logonUser.getCompanyId(), logonUser.getPoseId()); // 当前用户职位对应的经销商ID

		// 导出的文件名
		String fileName = "实销信息明细.csv";
		// 导出的文字编码
		OutputStream os = null;
		try {
			fileName = new String(fileName.getBytes("GB2312"), "ISO8859-1");

			response.setContentType("Application/text/csv");
			response.addHeader("Content-Disposition", "attachment;filename=" + fileName);

			List<List<Object>> list = new LinkedList<List<Object>>();

			List<Object> listTemp = new LinkedList<Object>();
			listTemp.add("客户名称");
			listTemp.add("客户类型");
			listTemp.add("是否大客户");
			listTemp.add("主要联系电话");
			listTemp.add("联系人地址");
			listTemp.add("销售顾问");
			listTemp.add("物料代码");
			listTemp.add("物料名称");
			listTemp.add("VIN");
			listTemp.add("发动机号");
			listTemp.add("生产日期");
			listTemp.add("实销时间");
			list.add(listTemp);
			List<Map<String, Object>> results = SalesReportDAO.getDealerCalDetailExportList_CVS(map, dealerIdA);
			for (int i = 0; i < results.size(); i++) {
				Map<String, Object> record = results.get(i);
				listTemp = new LinkedList<Object>();
				listTemp.add(CommonUtils.checkNull(record.get("C_NAME")));
				listTemp.add(CommonUtils.checkNull(record.get("CTM_TYPE")));
				listTemp.add(CommonUtils.checkNull(record.get("IS_FLEET")));
				listTemp.add(CommonUtils.checkNull(record.get("MAIN_PHONE")));
				listTemp.add(CommonUtils.checkNull(record.get("ADDRESS")));
				listTemp.add(CommonUtils.checkNull(record.get("SALES_CON_NAME")));
				listTemp.add(CommonUtils.checkNull(record.get("MATERIAL_CODE")));
				listTemp.add(CommonUtils.checkNull(record.get("MATERIAL_NAME")));
				listTemp.add(CommonUtils.checkNull(record.get("VIN")));
				listTemp.add(CommonUtils.checkNull(record.get("ENGINE_NO")));
				listTemp.add(CommonUtils.checkNull(record.get("PRODUCT_DATE")));
				listTemp.add(CommonUtils.checkNull(record.get("SALES_DATE")));
				list.add(listTemp);
			}
			os = response.getOutputStream();
			CsvWriterUtil.writeCsv(list, os);
			os.flush();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BizException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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

	/*
	 * 下级经销商实销信息模板下载
	 */
	public void cusMyDealerDownLoad() {
		AclUserBean logonUser = null;
		RequestWrapper request = act.getRequest();
		logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		act.getSession().get(Constant.LOGON_USER);

		String customer_type = CommonUtils.checkNull(request.getParamValue("customer_type")); // 客户类型
		String customer_name = CommonUtils.checkNull(request.getParamValue("customer_name")); // 客户名称
		String vin = CommonUtils.checkNull(request.getParamValue("vin")); // VIN
		String customer_phone = CommonUtils.checkNull(request.getParamValue("customer_phone")); // 客户电话
		String fleet_name = CommonUtils.checkNull(request.getParamValue("fleet_name")); // 大客户名称
		String materialCode = CommonUtils.checkNull(request.getParamValue("materialCode")); // 选择物料组
		String is_fleet = CommonUtils.checkNull(request.getParamValue("is_fleet")); // 是否大客户
		String contract_no = CommonUtils.checkNull(request.getParamValue("contract_no")); // 大客户合同
		String startDate = CommonUtils.checkNull(request.getParamValue("startDate")); // 上报日期:开始
		String endDate = CommonUtils.checkNull(request.getParamValue("endDate")); // 上报日期
																					// ：截止
		Map<String, String> map = new HashMap<String, String>();
		String areaId = CommonUtils.checkNull(request.getParamValue("areaId"));
		String dealerId = CommonUtils.checkNull(request.getParamValue("dealerId")); // 下级经销商ID
		map.put("areaId", areaId);
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

		// List<Map<String, Object>> listA =
		// DealerInfoDao.getInstance().getDel(logonUser.getDealerId()) ;
		// //获得二级经销商
		// String dealerIdA = logonUser.getDealerId() ;
		String dealerIdA = DealerInfoDao.getInstance().getDel__(logonUser.getDealerId());

		if (dealerId != null && !dealerId.equals("")) {
			dealerIdA = dealerId;
		}
		if ("".equals(dealerIdA)) {
			dealerIdA = "''";
		}
		// 导出的文件名
		String fileName = "实销信息明细.csv";
		// 导出的文字编码
		OutputStream os = null;
		try {
			fileName = new String(fileName.getBytes("GB2312"), "ISO8859-1");

			response.setContentType("Application/text/csv");
			response.addHeader("Content-Disposition", "attachment;filename=" + fileName);

			List<List<Object>> list = new LinkedList<List<Object>>();

			List<Object> listTemp = new LinkedList<Object>();
			listTemp.add("经销商简称");
			listTemp.add("客户名称");
			listTemp.add("客户类型");
			listTemp.add("是否大客户");
			listTemp.add("物料代码");
			listTemp.add("物料名称");
			listTemp.add("VIN");
			listTemp.add("发动机号");
			listTemp.add("生产日期");
			listTemp.add("实销时间");
			list.add(listTemp);
			List<Map<String, Object>> results = SalesReportDAO.getDealerCalDetailExportList(map, dealerIdA);
			for (int i = 0; i < results.size(); i++) {
				Map<String, Object> record = results.get(i);
				listTemp = new LinkedList<Object>();
				listTemp.add(CommonUtils.checkNull(record.get("DEALER_SHORTNAME")));
				listTemp.add(CommonUtils.checkNull(record.get("C_NAME")));
				listTemp.add(CommonUtils.checkNull(record.get("CTM_TYPE")));
				listTemp.add(CommonUtils.checkNull(record.get("IS_FLEET")));
				listTemp.add(CommonUtils.checkNull(record.get("MATERIAL_CODE")));
				listTemp.add(CommonUtils.checkNull(record.get("MATERIAL_NAME")));
				listTemp.add(CommonUtils.checkNull(record.get("VIN")));
				listTemp.add(CommonUtils.checkNull(record.get("ENGINE_NO")));
				listTemp.add(CommonUtils.checkNull(record.get("PRODUCT_DATE")));
				listTemp.add(CommonUtils.checkNull(record.get("SALES_DATE")));
				list.add(listTemp);
			}
			os = response.getOutputStream();
			CsvWriterUtil.writeCsv(list, os);
			os.flush();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BizException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
	 * FUNCTION : 实销信息更改申请:查询可申请更改的信息
	 * 
	 * @param :
	 * @return :
	 * @throws :
	 *             LastUpdate : 2010-6-7
	 */
	public void queryReportInfo_Change() {
		AclUserBean logonUser = null;
		try {
			RequestWrapper request = act.getRequest();
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			act.getSession().get(Constant.LOGON_USER);

			String customer_type = CommonUtils.checkNull(request.getParamValue("customer_type")); // 客户类型
			String customer_name = CommonUtils.checkNull(request.getParamValue("customer_name")); // 客户名称
			String vin = CommonUtils.checkNull(request.getParamValue("vin")); // VIN
			vin = setStr(vin, "L"); // 由于当前（2011/3/29）所有的vin均存在“L”，故以此作为查询条件加快查询速度
			String customer_phone = CommonUtils.checkNull(request.getParamValue("customer_phone")); // 客户电话
			String fleet_name = CommonUtils.checkNull(request.getParamValue("fleet_name")); // 大客户名称
			String materialCode = CommonUtils.checkNull(request.getParamValue("materialCode")); // 选择物料组
			String is_fleet = CommonUtils.checkNull(request.getParamValue("is_fleet")); // 是否大客户
			String contract_no = CommonUtils.checkNull(request.getParamValue("contract_no")); // 大客户合同
			String startDate = CommonUtils.checkNull(request.getParamValue("startDate")); // 上报日期:开始
			String endDate = CommonUtils.checkNull(request.getParamValue("endDate")); // 上报日期
																						// ：截止
			Map<String, String> map = new HashMap<String, String>();
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
			map.put("poseId", logonUser.getPoseId().toString());
			DealerRelation dr = new DealerRelation();
			String dealerIds = dr.getDealerIdByPose(logonUser.getCompanyId(), logonUser.getPoseId()); // 当前用户职位对应的经销商ID

			String areaId = request.getParamValue("areaId");
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
			PageResult<Map<String, Object>> ps = SalesReportDAO.queryReportInfoList_Change(areaId, dealerIds, map, Constant.PAGE_SIZE, curPage);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "实销信息更改申请:查询可申请更改的信息");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * @Title : customerInfoQuery
	 * @Description: 查询客户信息
	 * @throws
	 * @LastUpdate :2010-6-28
	 */
	public void customerInfoQuery() {
		AclUserBean logonUser = null;
		try {
			RequestWrapper request = act.getRequest();
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			act.getSession().get(Constant.LOGON_USER);

			// String ctm_id =
			// CommonUtils.checkNull(request.getParamValue("ctm_id")); //客户id
			// String vehicle_id =
			// CommonUtils.checkNull(request.getParamValue("vehicle_id"));
			// //车辆id
			String order_id = CommonUtils.checkNull(request.getParamValue("order_id")); // 实效id
			// if(order_id==null||"".equals(order_id)){
			// String log_id
			// =CommonUtils.checkNull(request.getParamValue("log_id")); //实效id
			// TtDealerActualSalesAuditPO tdasapo=new
			// TtDealerActualSalesAuditPO();
			// tdasapo
			// }
			// 1.查询“车辆资料”
			// Map<String,Object> vehicleInfo =
			// SalesReportDAO.getVehicleInfo(vehicle_id);
			Map<String, Object> vehicleInfo = SalesReportDAO.getVehicleInfoS(order_id);
			//通过实效id获取下面上传附件的文件地址
			//Map<String, Object> fileAddress = SalesReportDAO.getFileAddress(order_id);
			FsFileuploadPO detail = new FsFileuploadPO();
			detail.setYwzj(Long.valueOf(order_id));
			List<FsFileuploadPO> lists = dao.select(detail);
			// 2.销售信息
			TtDealerActualSalesPO actualSalesPO = new TtDealerActualSalesPO();
			// actualSalesPO.setVehicleId(Long.parseLong(vehicle_id));
			actualSalesPO.setOrderId(Long.parseLong(order_id));
			//actualSalesPO.setIsReturn(Constant.IF_TYPE_NO);
			List actualSalesPOList = dao.select(actualSalesPO);
			if (null != actualSalesPOList && actualSalesPOList.size() > 0) {
				// 2.查询车辆实销信息(TT_DEALER_ACTUAL_SALES)
				TtDealerActualSalesPO salesInfo = (TtDealerActualSalesPO) actualSalesPOList.get(0);

				// 查询大客户信息
				Long fleetId = salesInfo.getFleetId();
				if (null != fleetId && !"".equals(fleetId + "")) {
					TmFleetPO fleetPO = new TmFleetPO();
					fleetPO.setFleetId(fleetId);
					List fleetList = dao.select(fleetPO);
					TmCompanyPactPO companyPo = new TmCompanyPactPO();
					companyPo.setPactId(fleetId);
					if (null != fleetList && fleetList.size() > 0) {
						String fleet_code = ((TmFleetPO) fleetList.get(0)).getFleetCode();
						act.setOutData("fleet_code", fleet_code);
					} else if (dao.select(companyPo).size() > 0) {
						String fleet_name = ((TmCompanyPactPO) dao.select(companyPo).get(0)).getPactName();
						act.setOutData("fleet_name", fleet_name);
						act.setOutData("contract_no", fleet_name);
					}
				}
				// 查询大客户合同信息
				Long contractId = salesInfo.getContractId();

				if (null != contractId && !"".equals(contractId + "")) {
					TtFleetContractPO fleetContractPO = new TtFleetContractPO();
					fleetContractPO.setContractId(contractId);
					List contractList = dao.select(fleetContractPO);
					if (null != contractList && contractList.size() > 0) {
						String contract_no = ((TtFleetContractPO) contractList.get(0)).getContractNo();
						act.setOutData("contract_no", contract_no);
					}
				}

				// 3.查询客户信息(TT_CUSTOMER)
				Long ctmId = salesInfo.getCtmId();
				TtCustomerPO customerPO = new TtCustomerPO();
				customerPO.setCtmId(ctmId);
				List customerPOList = dao.select(customerPO);
				if (null != customerPOList && customerPOList.size() > 0) {
					TtCustomerPO customerInfo = (TtCustomerPO) customerPOList.get(0);
					act.setOutData("customerInfo", customerInfo);
				}
				act.setOutData("salesInfo", salesInfo);
				// 4.其他联系人信息

				List linkManList = SalesReportDAO.getLink_List(ctmId);
				act.setOutData("linkManList", linkManList);
			}
			act.setOutData("vehicleInfo", vehicleInfo);
			act.setOutData("lists",lists);
			act.setForword(salesDetailURL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "实销信息查询:查询历史上报信息");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * @Title : toChangeSalesInfo
	 * @Description: 实销信息更改
	 * @throws
	 * @LastUpdate :2010-6-29
	 */
	public void toChangeSalesInfo() {
		AclUserBean logonUser = null;
		try {
			RequestWrapper request = act.getRequest();
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			act.getSession().get(Constant.LOGON_USER);

			// String vehicle_id =
			// CommonUtils.checkNull(request.getParamValue("vehicle_id"));
			String order_id = CommonUtils.checkNull(request.getParamValue("order_id"));

			// 根据order获取vhicle_id
			TtDealerActualSalesPO taspo = new TtDealerActualSalesPO();
			taspo.setOrderId(new Long(order_id));
			taspo = (TtDealerActualSalesPO) dao.select(taspo).get(0);
			String vehicle_id = taspo.getOrderId().toString();
			
			//通过实效id获取下面上传附件的文件地址
			FsFileuploadPO detail = new FsFileuploadPO();
			detail.setYwzj(Long.valueOf(order_id));
			List<FsFileuploadPO> lists = dao.select(detail);
			act.setOutData("lists",lists);
			/*
			 * 查询： 1.车厂公司ID： OEM_COMPANY_ID 2.经销商公司ID： DLR_COMPANY_ID 3.经销商渠道：
			 * DEALER_ID
			 */
			String s_dealer_id = ""; // 经销商渠道
			String s_oem_company_id = ""; // 车厂公司ID
			String s_dlr_company_id = ""; // 经销商公司ID

			TmVehiclePO tmVehiclePO = new TmVehiclePO();
			tmVehiclePO.setVehicleId(Long.parseLong(vehicle_id));

			List vehicleList = dao.select(tmVehiclePO);
			if (null != vehicleList && vehicleList.size() > 0) {
				s_dealer_id = ((TmVehiclePO) vehicleList.get(0)).getDealerId() + "";
				TmDealerPO dealerPO = new TmDealerPO();
				dealerPO.setDealerId(((TmVehiclePO) vehicleList.get(0)).getDealerId());
				List dealerList = dao.select(dealerPO);
				if (null != dealerList && dealerList.size() > 0) {
					s_oem_company_id = ((TmDealerPO) dealerList.get(0)).getOemCompanyId() + "";
					s_dlr_company_id = ((TmDealerPO) dealerList.get(0)).getCompanyId() + "";
				}
			}

			Map<String, Object> vehicleInfo = SalesReportDAO.getVehicleInfoS(order_id);
			// Map<String,Object> vehicleInfo =
			// SalesReportDAO.getVehicleInfo(vehicle_id);
			act.setOutData("vehicleInfo", vehicleInfo);
			TtDealerActualSalesPO actualSalesPO = new TtDealerActualSalesPO();
			// actualSalesPO.setVehicleId(Long.parseLong(vehicle_id));
			actualSalesPO.setOrderId(new Long(order_id));
			actualSalesPO.setIsReturn(Constant.IF_TYPE_NO);
			List vList = dao.select(actualSalesPO);

			if (null != vList && vList.size() > 0) {
				TtDealerActualSalesPO salesInfo = (TtDealerActualSalesPO) vList.get(0);
				// 如果是大客户
				Long fleetId = salesInfo.getFleetId();// 大客户id
				act.setOutData("fleetId", fleetId.toString());
				Long fleetContractId = salesInfo.getContractId();// 大客户合同id
				if (0 != fleetId) {
					TmCompanyPactPO campanyPo = new TmCompanyPactPO();
					campanyPo.setPactId(fleetId);
					List pList = dao.select(campanyPo);
//					if (null != pList && pList.size() > 0) {
//						String pName = ((TmCompanyPactPO) pList.get(0)).getPactName();// 大客户名称
//						act.setOutData("pName", pName);
//					}
					TmFleetPO fleetPO = new TmFleetPO();
					fleetPO.setFleetId(fleetId);
					List fList = dao.select(fleetPO);
					if (null != fList && fList.size() > 0) {
						String fleetName = ((TmFleetPO) fList.get(0)).getFleetName();// 大客户名称
						act.setOutData("fleetName", fleetName);
					}
					if (null != fList && fList.size() > 0) {
						String fleetCode = ((TmFleetPO) fList.get(0)).getFleetCode();// 大客户代码
						act.setOutData("fleetCode", fleetCode);
					}
//					TtFleetContractPO contractPO = new TtFleetContractPO();
//					contractPO.setContractId(fleetContractId);
//					List cList = dao.select(contractPO);
//					if (null != cList && cList.size() > 0) {
//						String contractNo = ((TtFleetContractPO) cList.get(0)).getContractNo();// 大客户合同号
//						act.setOutData("contractNo", contractNo);
//					}
				}
				// if (0 == fleetId) {
				Long ctmId = salesInfo.getCtmId();// 客户id
				TtCustomerPO customerPO = new TtCustomerPO();
				customerPO.setCtmId(ctmId);
				List cusList = dao.select(customerPO);
				if (null != cusList && cusList.size() > 0) {
					TtCustomerPO customerInfo = (TtCustomerPO) cusList.get(0);
					act.setOutData("customerInfo", customerInfo); // 客户信息
					List linkManList = SalesReportDAO.getLink_List(ctmId);
					act.setOutData("linkManList", linkManList); // 其他联系人信息
				}
				// }
				act.setOutData("salesInfo", salesInfo);
			}

			// 新增判断是轿车还是微车
			String para = CommonDAO.getPara(Constant.CHANA_SYS.toString());
			if (Constant.COMPANY_CODE_JC.equals(para.toUpperCase())) {
				act.setForword(toChangeSalesInfoURL);
			} else if (Constant.COMPANY_CODE_CVS.equals(para.toUpperCase())) {
				act.setForword(toChangeSalesInfoURL_CVS);
			} else {
				throw new RuntimeException("判断当前系统的系统参数错误！");
			}
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "实销信息更改");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * 获得大客户合同列表
	 */
	public void getContractList() {
		AclUserBean logonUser = null;
		try {
			RequestWrapper request = act.getRequest();
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			act.getSession().get(Constant.LOGON_USER);

			String fleet_id = CommonUtils.checkNull(request.getParamValue("fleet_id"));
			List<Map<String, Object>> list = SalesReportDAO.getFleetContractList(fleet_id);
			act.setOutData("contractList", list);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "实销信息上报:获得合同列表");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	public void chkArea() {
		AclUserBean logonUser = null;
		try {
			RequestWrapper request = act.getRequest();
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);

			String vId = CommonUtils.checkNull(request.getParamValue("vehicleId"));

			Integer vFlag = VehicleInfoDAO.chkArea(vId);

			act.setOutData("vFlag", vFlag);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "车辆业务范围校验");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	public static String setStr(String para, String str) {
		if (CommonUtils.isNullString(para)) {
			return str;
		} else {
			return para;
		}
	}

	/**
	 * FUNCTION : 实销信息上报:执行上报
	 * 
	 * @param :
	 * @return :
	 * @throws :
	 *             LastUpdate : 2010-6-21
	 */
	public void doReportAction_CVS() {
		AclUserBean logonUser = null;
		try {

			RequestWrapper request = act.getRequest();
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			act.getSession().get(Constant.LOGON_USER);
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");// 设置日期格式
			String invoice_date = request.getParamValue("invoice_date");// 获取日期
			Date myNewDate2 = df.parse(invoice_date);
			String myNewDate = df.format(new Date());
			Date myNewDate1 = df.parse(myNewDate);

			// TODO 新增判断车辆是否属于该经销商 2012-09-10 韩晓宇
			String vehicle_id = request.getParamValue("vehicle_id");
			Map<String, Object> vehicleInfo = dao.getDealerId(vehicle_id);
			BigDecimal dealerIdByVehicle = (BigDecimal) vehicleInfo.get("DEALER_ID"); // 车辆信息表中经销商ID
			String currentDealerId = logonUser.getDealerId(); // 当前登陆的经销商ID
			// 判断当前登陆经销商与车辆信息表中经销商是不一致
			if (dealerIdByVehicle != null && !currentDealerId.contains(dealerIdByVehicle.toString())) {
				throw new RuntimeException("车辆不属于当前经销商!");
			}
			// TODO END
			if (myNewDate1.getTime() < myNewDate2.getTime()) {
				act.setOutData("returnValue", 5);
			} else {

				Map<String, String> infoMap = saveReportInfo_getJSPInfo();
				if (0 == infoMap.size()) { // YH 2011.10.15
					act.setOutData("error01", "没有找到相关车辆！请检查是否属于长安汽车！");
					act.setForword(toReportURLRpc_error);
				}
				String model_name = CommonUtils.checkNull(request.getParamValue("model_name"));// 车型名称

				String mortgageType = CommonUtils.checkNull(request.getParamValue("mortgageType"));// 按揭类型

				String FirstPrice = CommonUtils.checkNull(request.getParamValue("FirstPrice"));// 首付比例

				String LoansType = CommonUtils.checkNull(request.getParamValue("LoansType"));// 贷款方式

				String LoansYear = CommonUtils.checkNull(request.getParamValue("LoansYear"));// 贷款年限
				/*
				 * 查询： 1.车厂公司ID： OEM_COMPANY_ID 2.经销商公司ID： DLR_COMPANY_ID
				 * 3.经销商渠道： DEALER_ID
				 */
				String s_dealer_id = ""; // 经销商渠道
				String s_oem_company_id = ""; // 车厂公司ID
				String s_dlr_company_id = ""; // 经销商公司ID
				String returnValue = "1";
				String yieldly = "-1";
				TmVehiclePO tmVehiclePO = new TmVehiclePO();
				tmVehiclePO.setVehicleId(Long.parseLong(infoMap.get("vehicle_id")));
				List vehicleList = dao.select(tmVehiclePO);
				if (null != vehicleList && vehicleList.size() > 0) {
					s_dealer_id = ((TmVehiclePO) vehicleList.get(0)).getDealerId() + "";
					int lifeCycle = ((TmVehiclePO) vehicleList.get(0)).getLifeCycle();
					if (((TmVehiclePO) vehicleList.get(0)).getYieldly() != null) {
						yieldly = ((TmVehiclePO) vehicleList.get(0)).getYieldly().toString();
					}
					if (lifeCycle == Constant.VEHICLE_LIFE_04) {
						returnValue = "2";
						act.setOutData("returnValue", returnValue);
						return;
					}
					TmDealerPO dealerPO = new TmDealerPO();
					dealerPO.setDealerId(((TmVehiclePO) vehicleList.get(0)).getDealerId());
					List dealerList = dao.select(dealerPO);
					if (null != dealerList && dealerList.size() > 0) {
						s_oem_company_id = ((TmDealerPO) dealerList.get(0)).getOemCompanyId() + "";
						s_dlr_company_id = ((TmDealerPO) dealerList.get(0)).getCompanyId() + "";
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
					 * 2.如果“是否大客户”为“是”，向（TT_DEALER_ACTUAL_SALES车辆实销表）写入“大客户名称”和“大客户合同”
					 */
					if (Constant.CUSTOMER_TYPE_02.toString().equals(customer_type)) {
						customerPO.setCtmType(Constant.CUSTOMER_TYPE_02);
						customerPO.setCtmName(infoMap.get("company_name").trim()); // 向客户名称字段中插入公司名称
						customerPO.setCompanyName(infoMap.get("company_name").trim()); // 公司名称
						if (null != infoMap.get("company_s_name") && !"".equals(infoMap.get("company_s_name"))) {
							customerPO.setCompanySName(infoMap.get("company_s_name").trim()); // 公司简称
						}
						customerPO.setCtmForm(Integer.parseInt(infoMap.get("myctm_form")));// 客户来源
						customerPO.setCompanyPhone(infoMap.get("company_phone").trim()); // 公司电话
						customerPO.setLevelId(Integer.parseInt(infoMap.get("level_id"))); // 公司规模
						customerPO.setKind(Integer.parseInt(infoMap.get("kind"))); // 公司性质
						if (null != infoMap.get("vehicle_num") && !"".equals(infoMap.get("vehicle_num"))) {
							customerPO.setVehicleNum(Integer.parseInt(infoMap.get("vehicle_num").trim())); // 目前车辆数
						}

						/*
						 * 二、如果“客户类型”是“个人客户”
						 * 1.如果实销客户为“新客户”，向（TT_CUSTOMER实销客户表）新增客户信息，否则只写入“老客户id”
						 * 2.如果“是否大客户”为“是”，向（TT_DEALER_ACTUAL_SALES车辆实销表）写入“大客户名称”和“大客户合同”
						 */
					} else if (Constant.CUSTOMER_TYPE_01.toString().equals(customer_type)) {
						if (Constant.IS_OLD_CTM_01.toString().equals(sel_cus_type)) {
							customerPO.setCtmType(Constant.CUSTOMER_TYPE_01); // 客户类型
							customerPO.setCtmName(infoMap.get("ctm_name").trim()); // 客户名称
							if (infoMap.get("sex") != null) {
								customerPO.setSex(Integer.parseInt(infoMap.get("sex"))); // 性别
							}
							customerPO.setCardType(Integer.parseInt(infoMap.get("card_type"))); // 证件类型
							customerPO.setCardNum(infoMap.get("card_num").trim()); // 证件号码
							customerPO.setMainPhone(infoMap.get("main_phone").trim()); // 主要联系电话
							if (null != infoMap.get("other_phone") && !"".equals(infoMap.get("other_phone"))) {
								customerPO.setOtherPhone(infoMap.get("other_phone").trim()); // 其他联系电话
							}
							if (null != infoMap.get("email") && !"".equals(infoMap.get("email"))) {
								customerPO.setEmail(infoMap.get("email").trim()); // 电子邮件
							}
							if (null != infoMap.get("post_code") && !"".equals(infoMap.get("post_code"))) {
								customerPO.setPostCode(infoMap.get("post_code").trim()); // 邮政编码
							}
							if (Utility.testString(infoMap.get("birthday"))) {
								customerPO.setBirthday(dateFormat.parse(infoMap.get("birthday"))); // 生日
							}
							if (Utility.testString(infoMap.get("ctm_form"))) {
								customerPO.setCtmForm(Integer.parseInt(infoMap.get("ctm_form"))); // 客户来源
							}
							if (Utility.testString(infoMap.get("income"))) {
								customerPO.setIncome(Integer.parseInt(infoMap.get("income"))); // 家庭月收入
							}
							if (Utility.testString(infoMap.get("education"))) {
								customerPO.setEducation(Integer.parseInt(infoMap.get("education"))); // 教育程度
							}
							if (Utility.testString(infoMap.get("industry"))) {
								customerPO.setIndustry(Integer.parseInt(infoMap.get("industry"))); // 所在行业
							}
							if (Utility.testString(infoMap.get("is_married"))) {
								customerPO.setIsMarried(Integer.parseInt(infoMap.get("is_married"))); // 婚姻状况
							}
							if (Utility.testString(infoMap.get("profession"))) {
								customerPO.setProfession(Integer.parseInt(infoMap.get("profession"))); // 职业
							}
							if (null != infoMap.get("job") && !"".equals(infoMap.get("job"))) {
								customerPO.setJob(infoMap.get("job").trim()); // 职务
							}
						}
					}

					if (Constant.CUSTOMER_TYPE_02.toString().equals(customer_type) || (Constant.CUSTOMER_TYPE_01.toString().equals(customer_type) && Constant.IS_OLD_CTM_01.toString().equals(sel_cus_type))) {
						String isSecond = CommonUtils.checkNull(request.getParamValue("secondeVeh"));

						customerPO.setIsSecond(new Long(isSecond));

						if (null != infoMap.get("province") && !"".equals(infoMap.get("province"))) {
							customerPO.setProvince(Long.parseLong(infoMap.get("province"))); // 省份
						}
						if (null != infoMap.get("city") && !"".equals(infoMap.get("city"))) {
							customerPO.setCity(Long.parseLong(infoMap.get("city"))); // 地级市
						}
						if (null != infoMap.get("district") && !"".equals(infoMap.get("district"))) {
							customerPO.setTown(Long.parseLong(infoMap.get("district"))); // 区、县
						}

						customerPO.setAddress(infoMap.get("address").trim());
						customerPO.setCreateBy(logonUser.getUserId());
						customerPO.setCreateDate(new Date());

						dao.insert(customerPO);
					}
				}

				// 其他联系人

				Map<String, String[]> linkManInfo = getLinkManInfo();
				if (null != sel_cus_type && sel_cus_type.equals(Constant.IS_OLD_CTM_01 + "")) {
					saveReportInfo_AddorUpdateLinkMan(ctmId + "", linkManInfo, s_dlr_company_id, s_oem_company_id);
				}
				if (null != sel_cus_type && sel_cus_type.equals(Constant.IS_OLD_CTM_02 + "")) {
					saveReportInfo_AddorUpdateLinkMan(infoMap.get("oldCustomerId"), linkManInfo, s_dlr_company_id, s_oem_company_id);
				}

				// 实销信息
				TtDealerActualSalesPO actualSalesPO = new TtDealerActualSalesPO();
				Long saleId = Long.parseLong(SequenceManager.getSequence(""));
				actualSalesPO.setOrderId(saleId); // 实销ID

				if (null != mortgageType && !"".equals(mortgageType)) {
					actualSalesPO.setMortgageType(Long.valueOf(mortgageType));// 按揭类型
				}
				if (null != FirstPrice && !"".equals(FirstPrice)) {

					actualSalesPO.setShoufuRatio((Double.valueOf(FirstPrice) / 100));// 首付比例
				}

				if (null != LoansType && !"".equals(LoansType)) {
					actualSalesPO.setLoansType(Long.valueOf(LoansType));// 贷款方式
				}

				if (null != LoansYear && !"".equals(LoansYear)) {
					actualSalesPO.setLoansYear(Long.valueOf(LoansYear));// 贷款年限
				}

				actualSalesPO.setKnowAddress(infoMap.get("know_Address").trim());
				actualSalesPO.setSalesAddress(infoMap.get("sales_Address").trim());
				actualSalesPO.setSalesReson(infoMap.get("sales_Reson").trim());
				actualSalesPO.setOemCompanyId(Long.parseLong(s_oem_company_id)); // 车厂公司ID
				actualSalesPO.setDlrCompanyId(Long.parseLong(s_dlr_company_id)); // 经销商公司ID
				actualSalesPO.setDealerId(Long.parseLong(s_dealer_id)); // 经销商渠道
				actualSalesPO.setVehicleId(Long.parseLong(infoMap.get("vehicle_id")));// 车辆ID
				actualSalesPO.setSalesConId(Long.parseLong(infoMap.get("salesCon")));
				if (null != infoMap.get("vehicle_no") && !"".equals(infoMap.get("vehicle_no"))) {
					actualSalesPO.setVehicleNo(infoMap.get("vehicle_no").trim()); // 车牌号
				}
				if (null != infoMap.get("contractNo") && !"".equals(infoMap.get("contractNo"))) {
					actualSalesPO.setContractNo(infoMap.get("contractNo").trim()); // 合同编号
				}
				actualSalesPO.setInvoiceDate(dateFormat.parse(infoMap.get("invoice_date"))); // 开票日期
				actualSalesPO.setInvoiceNo(infoMap.get("invoice_no")); // 发票编号
				if (null != infoMap.get("insurance_company") && !"".equals(infoMap.get("insurance_company"))) {
					actualSalesPO.setInsuranceCompany(infoMap.get("insurance_company").trim()); // 保险公司
				}
				if (null != infoMap.get("insurance_date") && !"".equals(infoMap.get("insurance_date"))) {
					actualSalesPO.setInsuranceDate(dateFormat.parse(infoMap.get("insurance_date"))); // 保险日期
				}
				actualSalesPO.setConsignationDate(dateFormat.parse(infoMap.get("consignation_date"))); // 车辆交付日期
				if (null != infoMap.get("miles") && !"".equals(infoMap.get("miles"))) {
					actualSalesPO.setMiles(Float.parseFloat(infoMap.get("miles").trim())); // 交付时公里数
				}
				actualSalesPO.setPayment(Integer.parseInt(infoMap.get("payment"))); // 付款方式
				actualSalesPO.setPrice(Double.parseDouble(infoMap.get("price").trim())); // 价格
				if (null != infoMap.get("memo") && !"".equals(infoMap.get("memo"))) {
					actualSalesPO.setMemo(infoMap.get("memo").trim()); // 备注
				}
				actualSalesPO.setIsReturn(Constant.IF_TYPE_NO); // 是否退车
				// 如果是大客户
				String is_fleet = infoMap.get("is_fleet");
				if (null != is_fleet && is_fleet.equals(Constant.IF_TYPE_YES + "")) {
					String fleet_id = infoMap.get("fleet_id"); // 大客户id
					String fleet_contract_no_id = infoMap.get("fleet_contract_no_id"); // 大客户合同id
					String fleet_contract_no = infoMap.get("fleet_contract_no");
					actualSalesPO.setIsFleet(Constant.IF_TYPE_YES); // 是否是大客户
					actualSalesPO.setFleetId(Long.parseLong(fleet_id)); // 大客户ID
					if (fleet_contract_no_id != null && !fleet_contract_no_id.equals("")) {
						actualSalesPO.setContractId(Long.parseLong(fleet_contract_no_id)); // 大客户合同ID
					}
					if (fleet_contract_no != null && !fleet_contract_no.equals("")) {
						actualSalesPO.setContractNo(fleet_contract_no); // 大客户合同号
					}
					actualSalesPO.setFleetStatus(Constant.Fleet_SALES_CHECK_STATUS_01); // 未审核
				} else {
					actualSalesPO.setIsFleet(Constant.IF_TYPE_NO);
				}
				// 如果是新客户
				if (null != sel_cus_type && sel_cus_type.equals(Constant.IS_OLD_CTM_01 + "")) {
					actualSalesPO.setCtmId(ctmId);
				}
				// 如果是老客户
				if (null != sel_cus_type && sel_cus_type.equals(Constant.IS_OLD_CTM_02 + "")) {
					actualSalesPO.setCtmId(Long.parseLong(infoMap.get("oldCustomerId")));
				}

				actualSalesPO.setSalesDate(new Date());
				actualSalesPO.setCreateDate(new Date());
				actualSalesPO.setCreateBy(logonUser.getUserId());
				if (Utility.testString(infoMap.get("carCharactor"))) {
					actualSalesPO.setCarCharactor(Integer.parseInt(infoMap.get("carCharactor")));// 车辆性质
				}
				/*
				 * 写入实销信息
				 */
				actualSalesPO.setSoNo(CommonUtils.checkNull(request.getParamValue("soNo")));// 下端通过URL请求
				dao.insert(actualSalesPO);

				/*
				 * List<Object> ins = new LinkedList<Object>();
				 * ins.add(saleId); List<Integer> outs = new LinkedList<Integer>();
				 * dao.callProcedure("p_xxcrm_ic_customer_dms",ins,outs) ;
				 */

				// ----------------------------------------------------------------------------
				TmVehiclePO tempPO = new TmVehiclePO();
				tempPO.setVehicleId(Long.parseLong(infoMap.get("vehicle_id")));
				TmVehiclePO valuePO = new TmVehiclePO();
				valuePO.setLifeCycle(Constant.VEHICLE_LIFE_04);
				valuePO.setPurchasedDate(new Date());

				// 新增三包策略字段
				TmDealerPO tmDealerPO = new TmDealerPO();
				tmDealerPO.setDealerId(Long.parseLong(s_dealer_id));
				List dealerList = dao.select(tmDealerPO);
				if (null != dealerList && dealerList.size() > 0) {
					TmVhclMaterialGroupPO groupPO = new TmVhclMaterialGroupPO();
					groupPO.setGroupName(model_name);
					List gList = dao.select(groupPO);
					if (null != gList && gList.size() > 0) {
						TmVhclMaterialGroupPO materialGroupPO = (TmVhclMaterialGroupPO) gList.get(0);
						Long model_id = materialGroupPO.getGroupId();
						TmDealerPO v_dealer = (TmDealerPO) dealerList.get(0);
						String  provinceId = v_dealer.getProvinceId().toString();
						Long claimTacticsId = guaranteePeriodDAO.findThreeGuaranteesStrategy(new Date(), new Long(provinceId), model_id, GetOemcompanyId.getOemCompanyId(logonUser), new Integer(yieldly));
						valuePO.setClaimTacticsId(claimTacticsId);
					}
				}

				// 修改车辆生命周期：实销
				dao.update(tempPO, valuePO);
				// 向TT_VS_VHCL_CHNG写入变更日志
				TtVsVhclChngPO chngPO = new TtVsVhclChngPO();
				Long vhclChangeId = Long.parseLong(SequenceManager.getSequence(""));
				chngPO.setVhclChangeId(vhclChangeId); // 改变序号
				chngPO.setVehicleId(Long.parseLong(infoMap.get("vehicle_id"))); // 车辆ID
				chngPO.setOrgType(logonUser.getOrgType()); // 组织类型
				chngPO.setOrgId(logonUser.getOrgId()); // 组织ID
				chngPO.setDealerId(Long.parseLong(s_dealer_id)); // 经销商ID
				chngPO.setChangeCode(Constant.SALES_STATUS_CHANGE_TYPE); // 改变类型:销售状态更改
				chngPO.setChangeName(Constant.SALES_STATUS_CHANGE_TYPE_04.toString()); // 改变名称:实效上报
				chngPO.setChangeDate(new Date()); // 改变时间
				if (null != infoMap.get("memo") && !"".equals(infoMap.get("memo"))) {
					chngPO.setChangeDesc(infoMap.get("memo").trim()); // 改变描述
				}
				chngPO.setCreateDate(new Date()); // 记录创建日期
				chngPO.setCreateBy(logonUser.getUserId()); // 记录创建者
				dao.insert(chngPO);

				String isJson = infoMap.get("isJson");

				/** 只有接口用户登录* */
				if (Utility.testString(logonUser.getRpcFlag())) {
					/** 下发开始* */
					OSB32 o = new OSB32();
					o.execute(actualSalesPO);
					// act.setForword(toReportCVSURLRpc);
					returnValue = "6";
					act.setOutData("returnValue", returnValue);
					/** 下发结束* */
				} else {
					salesReportInit();
				}
			}

			act.setOutData("flag", "success");
		} catch (RuntimeException e) {
			logger.error(logonUser, e);
			act.setException(e);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "实销信息上报:执行上报");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * 验证大客户车系
	 */
	public void checkSeriesGroup() {
		AclUserBean logonUser = null;
		try {
			RequestWrapper request = act.getRequest();
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			act.getSession().get(Constant.LOGON_USER);

			String fleet_id = CommonUtils.checkNull(request.getParamValue("fleetId"));
			String group_id = CommonUtils.checkNull(request.getParamValue("groupId"));
			int count = 0;
			count = SalesReportDAO.getCheckSeriesGroup(fleet_id, group_id);
			act.setOutData("count", count);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "实销信息上报:获得合同列表");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}

}
