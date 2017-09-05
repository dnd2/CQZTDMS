package com.infodms.dms.actions.sales.storage.sendmanage;

import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import jxl.Cell;

import org.apache.log4j.Logger;

import com.infodms.dms.actions.claim.basicData.ToExcel;
import com.infodms.dms.actions.parts.baseManager.partsBaseManager.PartBaseImport;
import com.infodms.dms.actions.sysmng.usemng.SgmDealerSysUser;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.common.materialManager.MaterialGroupManagerDao;
import com.infodms.dms.dao.sales.storage.sendManage.CommprDao;
import com.infodms.dms.dao.sales.storage.storagebase.FareSetDao;
import com.infodms.dms.dao.sales.storage.storagebase.LogisticsDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TtSalesLogiPO;
import com.infodms.dms.po.TtSalesWaybillPO;
import com.infodms.dms.po.TtVehicleAddressPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.csv.CsvWriterUtil;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;
import com.infoservice.po3.bean.PageResult;

/**
 * 
/**
 * 
 * @ClassName     : CommprManage
 * @Description   : 综合信息管理
 * @author        : ranjian
 * CreateDate     : 2013-9-11
 */
public class CommprManage extends PartBaseImport {
	public Logger logger = Logger.getLogger(SgmDealerSysUser.class);
	private ActionContext act = ActionContext.getContext();
	RequestWrapper request = act.getRequest();
	private final CommprDao cDao = CommprDao.getInstance();
	private final LogisticsDao reLDao = LogisticsDao.getInstance();
	private final FareSetDao reDao = FareSetDao.getInstance();

	private final String COMMPR_INIT_UTL = "/jsp/sales/storage/sendmanage/commpr/commprList.jsp";
	private final String locationMaintain_INIT_UTL = "/jsp/sales/storage/sendmanage/commpr/locationMaintain.jsp";
	private final String locationMaintain_UTL = "/jsp/sales/storage/sendmanage/commpr/locationMaintainCh.jsp";
	private final String locationMaintain_INIT_query_UTL = "/jsp/sales/storage/sendmanage/commpr/locationMaintainQuery.jsp";
	private final String locationMaintain_detail_address_UTL = "/jsp/sales/storage/sendmanage/commpr/locationMaintainAddress.jsp";
	private final String locationMaintain_detail_UTL = "/jsp/sales/storage/sendmanage/commpr/locationMaintainDetail.jsp";
	private final String locationMaintain_INIT_query_UTL_DRL = "/jsp/sales/storage/sendmanage/commpr/locationMaintainQueryDRL.jsp";
	private final String dealer_locationMaintain_INIT_query_UTL = "/jsp/sales/storage/sendmanage/commpr/dealerLocationMaintainQuery.jsp";
	
	/**
	 * 
	 * @Title      : 
	 * @Description: 综合信息查询初始化
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-9-11
	 */
	public void commprInit(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		String areaIds = MaterialGroupManagerDao
		.getPoseIdBusinessIdStr(logonUser.getPoseId().toString());
		try {
			List<Map<String, Object>> list_yieldly=MaterialGroupManagerDao.getPoseIdBusiness(logonUser.getPoseId().toString());
			act.setOutData("list", list_yieldly);//产地LIST
			//获取车系信息（查询页面车系下拉框用）
			List<Map<String, Object>> list_vchl =reDao.getVhclMsg("");
			act.setOutData("list_vchl", list_vchl);	
			List<Map<String, Object>> list_logi=new ArrayList<Map<String, Object>>();
			if (logonUser.getPoseBusType().intValue() == Constant.POSE_BUS_TYPE_WL)
			{
				list_logi=reLDao.getLogiByPoseId(areaIds,logonUser);
			}else{
				list_logi=reLDao.getLogiByArea(areaIds);
			}
			act.setOutData("list_logi", list_logi);//物流商LIST
			act.setForword(COMMPR_INIT_UTL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE,
					"综合信息查询初始化");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 
	 * @Title      : 
	 * @Description:  综合信息查询
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-9-11
	 */
	public void CommprQuery() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String vin = CommonUtils.checkNull(request.getParamValue("vin")); // VIN
			String common = CommonUtils.checkNull(request.getParamValue("common")); // 处理方式
			String addType = CommonUtils.checkNull(request.getParamValue("addType")); // 查询方式
			String billStartdate = CommonUtils.checkNull(request.getParamValue("BILL_STARTDATE")); // 发运时间开始
			String billEnddate = CommonUtils.checkNull(request.getParamValue("BILL_ENDDATE")); // 发运时间结束
			String groupId = CommonUtils.checkNull(request.getParamValue("GROUP_ID")); // 车系
			String yieldly = CommonUtils.checkNull(request.getParamValue("YIELDLY")); // 产地
			String logiName = CommonUtils.checkNull(request.getParamValue("LOGI_NAME_SEACH")); // 物流商
			String sdNumber = CommonUtils.checkNull(request.getParamValue("SD_NUMBER")); //流水号
			String orderType = CommonUtils.checkNull(request.getParamValue("ORDER_TYPE")); //订单类型
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("vin", vin);
			map.put("billStartdate", billStartdate);
			map.put("billEnddate", billEnddate);
			map.put("groupId", groupId);
			map.put("yieldly", yieldly);
			map.put("logiName", logiName);
			map.put("sdNumber", sdNumber);
			map.put("orderType", orderType);
			map.put("poseId", logonUser.getPoseId().toString());
			if(addType.equals("more")){
				if(common.equals("2")){
					List<Map<String, Object>>  valueMapList = cDao.getCommprQuery(map);
					String[] headExport={"序号","经销商名称","省份","城市","地区","VIN","车系","物料","入库时间","出库时间","物流公司","承运车队","流水号","是否中转"};
					String[] columns={"ROWNUM","DEALER_NAME","REGION_NAME","REGION_NAME1","REGION_NAME2","VIN","SERIES_NAME","MATERIAL_CODE","ORG_STORAGE_DATE","OUT_DATE","LOGI_NAME","CAR_TEAM","SD_NUMBER","ISZZ"};
					ToExcel.toReportExcel(act.getResponse(), request,"商品车出库导出.xls", headExport,columns,valueMapList);
				}else{
					Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request
							.getParamValue("curPage")) : 1;
					PageResult<Map<String, Object>> ps = cDao.getCommprQueryFY(map, curPage,
							Constant.PAGE_SIZE);
					act.setOutData("ps", ps);
				}
			}else{
				List<Map<String, Object>>  valueMapList = cDao.getCommprQuery(map);
				if(valueMapList!=null && valueMapList.size()>0){
					Map<String, Object> vmap=valueMapList.get(0);
					act.setOutData("valueMap", vmap);	
					act.setOutData("returnValue", 1);	
				}
				else{
					act.setOutData("returnValue", 2);	
				}
			}
			
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, " 综合信息查询");
			logger.error(logonUser, e1);
			act.setOutData("returnValue", 3);
			act.setException(e1);
		}
	}
	
	/**
	 *  在途位置维护(初始页)
	 */
	public void locationMaintainInit(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		String areaIds = MaterialGroupManagerDao
		.getPoseIdBusinessIdStr(logonUser.getPoseId().toString());
		try {
			/*List<Map<String, Object>> list_yieldly=MaterialGroupManagerDao.getPoseIdBusiness(logonUser.getPoseId().toString());
			act.setOutData("list", list_yieldly);//产地LIST
			//获取车系信息（查询页面车系下拉框用）
			List<Map<String, Object>> list_vchl =reDao.getVhclMsg("");
			act.setOutData("list_vchl", list_vchl);	*/
			List<Map<String, Object>> list_logi=reLDao.getLogiName();
			act.setOutData("list_logi", list_logi);
			
			act.setForword(locationMaintain_INIT_UTL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE,
					"综合信息查询初始化");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 *  在途位置查询
	 */
	public void locationMaintainQuery(){
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			String shipNo = request.getParamValue("shipNo");		    //发运单号
			String shipStatus = request.getParamValue("shipStatus");	//状态
			String startDate = request.getParamValue("startDate");		//起始时间
			String endDate = request.getParamValue("endDate");			//结束时间
			String dealerCode = request.getParamValue("dealerCode"); // 经销商ID
			String logi_name_seach = request.getParamValue("LOGI_NAME_SEACH");	//物流公司
			String province_id = request.getParamValue("");	//省
			String city_id = request.getParamValue("");	//市
			String county_id = request.getParamValue("");	//区县
			
			if(Utility.testString(startDate)){
				startDate = startDate + " 00:00:00";
			}
			if(Utility.testString(endDate)){
				endDate = endDate + " 23:59:59";
			}
			if(null != logonUser.getDealerCode()) {
				dealerCode = logonUser.getDealerCode();
			}
			
			Integer curPage = request.getParamValue("curPage") !=null ? Integer.parseInt(request.getParamValue("curPage")) : 1;
			PageResult<Map<String, Object>> ps = reDao.getLocationMaintainList(shipNo,shipStatus,dealerCode, logi_name_seach,
					startDate, endDate, province_id, city_id, county_id, curPage, Constant.PAGE_SIZE);
			act.setOutData("ps", ps);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"在途位置维护");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}

	/**
	 * 导出
	 * @author liufazhong
	 */
	public void locationMaintainQueryExport(){
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			String shipNo = request.getParamValue("shipNo");		    //发运单号
			String shipStatus = request.getParamValue("shipStatus");	//状态
			String startDate = request.getParamValue("startDate");		//起始时间
			String endDate = request.getParamValue("endDate");			//结束时间
			String dealerCode = request.getParamValue("dealerCode"); // 经销商ID
			String logi_name_seach = request.getParamValue("LOGI_NAME_SEACH");	//物流公司
			String province_id = request.getParamValue("");	//省
			String city_id = request.getParamValue("");	//市
			String county_id = request.getParamValue("");	//区县
			
			if(Utility.testString(startDate)){
				startDate = startDate + " 00:00:00";
			}
			if(Utility.testString(endDate)){
				endDate = endDate + " 23:59:59";
			}
			if(null != logonUser.getDealerCode()) {
				dealerCode = logonUser.getDealerCode();
			}
			
			PageResult<Map<String, Object>> ps = reDao.getLocationMaintainList(shipNo,shipStatus,dealerCode, logi_name_seach,
					startDate, endDate, province_id, city_id, county_id, 1, Constant.PAGE_SIZE_MAX);
			String [] head={"承运商公司","发运单单号","发运销售商","发运销售商电话","发运销售商联系人","发运地址","运单生成日期"};
			String [] cols={"LOGI_NAME","BILL_NO","DEALER_NAME","PHONE","LINK_MAN","ADDRESS","BILL_CRT_DATE"};
			ToExcel.toReportExcel(act.getResponse(),request, "在途位置明细.xls",head,cols,ps.getRecords());
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"在途位置维护");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	//add by yup 保存相关信息 2014--2-8
	public void saveInfo(){
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			String billId = request.getParamValue("billId");
			
			String cardNo = request.getParamValue("cardNo");
			String Driver = request.getParamValue("Driver");
			String Contact = request.getParamValue("Contact");
			
			//修改主表状态
			TtSalesWaybillPO po = new TtSalesWaybillPO();
			po.setBillId(Long.parseLong(billId));
			TtSalesWaybillPO poValue = new TtSalesWaybillPO();
			poValue.setChepaiNo(cardNo);
			poValue.setSiji(Driver);
			poValue.setTel(Contact);
			poValue.setUpdateBy(logonUser.getUserId());
			poValue.setUpdateDate(new Date());
			reDao.update(po, poValue);
			
			act.setOutData("returnValue", "1");
			
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.SAVE_FAILURE_CODE,"保存相关信息");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	//add by yup 添加位置 2014-2-8 保存
	public void saveAddAddress() {
		ActionContext act = ActionContext.getContext();
		RequestWrapper request = act.getRequest();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String billId = request.getParamValue("billId");
			String time = CommonUtils.checkNull(request.getParamValue("time"));
			String address = CommonUtils.checkNull(request.getParamValue("address"));

			TtVehicleAddressPO po = new TtVehicleAddressPO();
			po.setDetailId(Long.parseLong(SequenceManager.getSequence("")));
			po.setBillId(Long.parseLong(billId));
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			po.setZtDate(sdf.parse(time));
			po.setZtAddress(address);
			po.setCreateBy(logonUser.getUserId());
			po.setCreateDate(new Date());
			reDao.insert(po);
			act.setOutData("billId", billId);
			act.setOutData("OK", 1);
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.UPDATE_FAILURE_CODE, "供应商信息");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
		
	//add by yup 2014-1-23 在途位置查询(初始页)
	public void locationMaintainQueryInit(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		String areaIds = MaterialGroupManagerDao
		.getPoseIdBusinessIdStr(logonUser.getPoseId().toString());
		try {
			/*List<Map<String, Object>> list_yieldly=MaterialGroupManagerDao.getPoseIdBusiness(logonUser.getPoseId().toString());
			act.setOutData("list", list_yieldly);//产地LIST
			//获取车系信息（查询页面车系下拉框用）
			List<Map<String, Object>> list_vchl =reDao.getVhclMsg("");
			act.setOutData("list_vchl", list_vchl);	*/
			List<Map<String, Object>> list_logi=reLDao.getLogiName();
			act.setOutData("list_logi", list_logi);
			
			act.setForword(locationMaintain_INIT_query_UTL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE,"在途位置查询(初始页)");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	//经销商端在途位置查询
	public void dLocationMaintainQueryInit(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			List<Map<String, Object>> list_logi=reLDao.getLogiName();
			act.setOutData("list_logi", list_logi);
			
			act.setForword(dealer_locationMaintain_INIT_query_UTL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE,"经销商端在途位置查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	//add by yup 2014-1-23 在途位置查询(初始页经销商端)
	public void locationMaintainQueryInitDRL(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		String areaIds = MaterialGroupManagerDao
		.getPoseIdBusinessIdStr(logonUser.getPoseId().toString());
		try {
			/*List<Map<String, Object>> list_yieldly=MaterialGroupManagerDao.getPoseIdBusiness(logonUser.getPoseId().toString());
			act.setOutData("list", list_yieldly);//产地LIST
			//获取车系信息（查询页面车系下拉框用）
			List<Map<String, Object>> list_vchl =reDao.getVhclMsg("");
			act.setOutData("list_vchl", list_vchl);	*/
			String dealerCode=logonUser.getDealerCode();
			List<Map<String, Object>> list_logi=reLDao.getLogiName();
			act.setOutData("list_logi", list_logi);
			act.setOutData("dealerCode", dealerCode);
			act.setForword(locationMaintain_INIT_query_UTL_DRL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE,"在途位置查询(初始页)");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	//add by yup 2014-2-8 删除位置
	public void delInfo(){
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			String detailId = request.getParamValue("detailId");
			String billId = request.getParamValue("billId");
			
			TtVehicleAddressPO po = new TtVehicleAddressPO();
			po.setDetailId(Long.parseLong(detailId));
			reDao.delete(po);
			
			act.setOutData("returnValue", "1");
			act.setOutData("billId", billId);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.SAVE_FAILURE_CODE,"删除位置信息");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	//add by yup 2014-1-23 在途位置查询(查看页面)
	public void locationMaintainDetailInit(){
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
			try {
				String billId = CommonUtils.checkNull(request.getParamValue("billId"));
				String COMMO = CommonUtils.checkNull(request.getParamValue("COMMO"));
				TtSalesWaybillPO po = new TtSalesWaybillPO();
				po.setBillId(Long.parseLong(billId));
				TtSalesWaybillPO poValue = (TtSalesWaybillPO)reDao.select(po).get(0);
				TtSalesLogiPO poi = new TtSalesLogiPO();
				poi.setLogiId(poValue.getLogiId());
				TtSalesLogiPO poiValue = (TtSalesLogiPO)reDao.select(poi).get(0);
				act.setOutData("logiName", poiValue.getLogiName());                           //承运商名称
				act.setOutData("billNo", poValue.getBillNo());                                //发运单号
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				act.setOutData("billCrtDate", sdf.format(poValue.getBillCrtDate()));          //发运时间
				
				act.setOutData("chepaiNo", poValue.getChepaiNo());
				act.setOutData("siji", poValue.getSiji());
				act.setOutData("tel", poValue.getTel());
				
				List<Map<String, Object>> list = reDao.getMateriaDetail(billId);
				act.setOutData("list", list);
				
				List<Map<String, Object>> attachList = reDao.getAddressShDetail(billId);
				act.setOutData("attachList", attachList);
				act.setOutData("COMMO", COMMO);
				act.setForword(locationMaintain_detail_UTL);
		}catch(Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.DETAIL_FAILURE_CODE,"在途位置查询(查看页面)");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 在途位置维护(维护页面)
	 */
	public void locationMaintainChInit(){
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
			try {
				String billId = CommonUtils.checkNull(request.getParamValue("billId"));
				
				TtSalesWaybillPO po = new TtSalesWaybillPO();
				po.setBillId(Long.parseLong(billId));
				TtSalesWaybillPO poValue = (TtSalesWaybillPO)reDao.select(po).get(0);
				TtSalesLogiPO poi = new TtSalesLogiPO();
				poi.setLogiId(poValue.getLogiId());
				TtSalesLogiPO poiValue = (TtSalesLogiPO)reDao.select(poi).get(0);
				act.setOutData("logiName", poiValue.getLogiName());//承运商名称
				act.setOutData("billNo", poValue.getBillNo());//发运单号
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				act.setOutData("billCrtDate", sdf.format(poValue.getBillCrtDate()));//发运时间
				act.setOutData("chepaiNo", poValue.getChepaiNo());
				act.setOutData("siji", poValue.getSiji());
				act.setOutData("tel", poValue.getTel());
				
				List<Map<String, Object>> list = reDao.getMateriaDetail(billId);
				act.setOutData("list", list);
				
				List<Map<String, Object>> attachList = reDao.getAddressDetail(billId);
				act.setOutData("attachList", attachList);
				
				act.setOutData("billId", billId);
				
				act.setForword(locationMaintain_UTL);
		}catch(Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.DETAIL_FAILURE_CODE,"在途位置维护(维护页面)");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	//add by yup 添加位置 2014-2-8
	public void addAddressInit() {
		ActionContext act = ActionContext.getContext();
		RequestWrapper request = act.getRequest();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String billId = CommonUtils.checkNull(request.getParamValue("billId"));
			act.setOutData("billId", billId);
			act.setForword(locationMaintain_detail_address_UTL);
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "供应商基本信息");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	//add by 2014-2-14 下载模板
	public void exportVenderTemplate() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		OutputStream os = null;
		try {
			ResponseWrapper response = act.getResponse();

			// 用于下载传参的集合
			List<List<Object>> list = new LinkedList<List<Object>>();

			// 标题
			List<Object> listHead = new LinkedList<Object>();// 导出模板第一行
			listHead.add("日期");
			listHead.add("位置");

			list.add(listHead);
			// 导出的文件名
			String fileName = "位置维护模板.xls";
			// 导出的文字编码
			fileName = new String(fileName.getBytes("GB2312"), "ISO8859-1");
			response.setContentType("Application/text/xls");
			response.addHeader("Content-Disposition", "attachment;filename="+fileName);

			os = response.getOutputStream();
			CsvWriterUtil.createXlsFile(list, os);
			os.flush();
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,ErrorCodeConstant.SPECIAL_MEG, "文件读取错误");
			logger.error(logonUser, e1);
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
	
	//add by yup 2014-2-14 导入excle 位置
	public void uploadVenderExcel() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		String billId = CommonUtils.checkNull(request.getParamValue("billId"));
		StringBuffer errorInfo = new StringBuffer("");
		RequestWrapper request = act.getRequest();
		try {
			long maxSize = 1024 * 1024 * 5;
			int errNum = insertIntoTmp(request, "uploadFile", 2, 3, maxSize);

			String err = "";

			if (errNum != 0) {
				switch (errNum) {
				case 1:
					err += "文件列数过多,请修改后再上传!";
					break;
				case 2:
					err += "空行不能大于三行,请修改后再上传!";
					break;
				case 3:
					err += "文件内容不能为空,请修改后再上传!";
					break;
				case 4:
					err += "文件类型错误,请重新上传!";
					break;
				case 5:
					err += "文件不能大于" + maxSize + ",请修改后再上传";
					break;
				default:
					break;
				}
			}

			if (!"".equals(err)) {
				BizException e1 = new BizException(act,ErrorCodeConstant.SPECIAL_MEG, err);
				throw e1;
			} else {
				List<Map> list = getMapList();
				List voList = new ArrayList();
				loadVoList(voList, list, errorInfo);
				if (errorInfo.length() > 0) {
					BizException e1 = new BizException(act,ErrorCodeConstant.SPECIAL_MEG, errorInfo);
					throw e1;
				}
				for (int i = 0; i < voList.size(); i++) {
					TtVehicleAddressPO venderDefinePO = (TtVehicleAddressPO) voList.get(i);
					//boolean flag = dao.existVenderCode(venderDefinePO.getVenderCode());
					//boolean flag1 = dao.existVenderName(venderDefinePO.getVenderName());
					//if(flag){//如果供应商编码已经存在
						//errorInfo.append("第"+(i+2)+"行的供应商编码重复,请修改后再上传!<br>");
					//}
					//if(flag1){//如果供应商名称已经存在
						//errorInfo.append("第"+(i+2)+"行的供应商名称重复,请修改后再上传!<br>");
					//}
					
					if (errorInfo.length() > 0) {
						BizException e1 = new BizException(act,ErrorCodeConstant.SPECIAL_MEG, errorInfo);
						throw e1;
					}
					
					venderDefinePO.setDetailId(Long.parseLong(SequenceManager.getSequence("")));
					venderDefinePO.setBillId(Long.parseLong(billId));
					venderDefinePO.setCreateDate(new Date());
					venderDefinePO.setCreateBy(logonUser.getUserId());
					
					reDao.insert(venderDefinePO);
					}

				}
			
			//act.setOutData("returnValue", 1);//这2行本来是用于异步调用，但是异步好像不得行，先留着
			//act.setOutData("billId", billId);
			this.locationMaintainChInit111(billId);
		} catch (Exception e) {
			BizException e1 = null;
			if (e instanceof BizException) {
				e1 = (BizException) e;
			} else {
				e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG,"文件读取错误");
			}
			logger.error(logonUser, e1);
			act.setException(e1);
			
			this.locationMaintainChInit111(billId);//跳转
		}
	}
	
	//专门用于批量导入了跳转到原始页面(带参数的)
	public void locationMaintainChInit111(String billId){
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
			try {
				TtSalesWaybillPO po = new TtSalesWaybillPO();
				po.setBillId(Long.parseLong(billId));
				TtSalesWaybillPO poValue = (TtSalesWaybillPO)reDao.select(po).get(0);
				TtSalesLogiPO poi = new TtSalesLogiPO();
				poi.setLogiId(poValue.getLogiId());
				TtSalesLogiPO poiValue = (TtSalesLogiPO)reDao.select(poi).get(0);
				act.setOutData("logiName", poiValue.getLogiName());//承运商名称
				act.setOutData("billNo", poValue.getBillNo());//发运单号
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				act.setOutData("billCrtDate", sdf.format(poValue.getBillCrtDate()));//发运时间
				
				List<Map<String, Object>> list = reDao.getMateriaDetail(billId);
				act.setOutData("list", list);
				
				List<Map<String, Object>> attachList = reDao.getAddressDetail(billId);
				act.setOutData("attachList", attachList);
				
				act.setOutData("billId", billId);
				
				act.setForword(locationMaintain_UTL);
		}catch(Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.DETAIL_FAILURE_CODE,"在途位置维护(维护页面)");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	private void loadVoList(List voList, List<Map> list, StringBuffer errorInfo) throws Exception {
		if (null == list) {
			list = new ArrayList();
		}
		for (int i = 0; i < list.size(); i++) {
			Map map = list.get(i);
			if (null == map) {
				map = new HashMap<String, Cell[]>();
			}
			Set<String> keys = map.keySet();
			Iterator it = keys.iterator();
			String key = "";
			while (it.hasNext()) {
				key = (String) it.next();
				Cell[] cells = (Cell[]) map.get(key);
				parseCells(voList, key, cells, errorInfo);
				if (errorInfo.length() > 0) {
					break;
				}
			}
		}
	}
	
	private void parseCells(List list, String rowNum, Cell[] cells,StringBuffer errorInfo) throws Exception {
		TtVehicleAddressPO venderDefinePO = new TtVehicleAddressPO();
		if ("" == subCell(cells[0].getContents().trim())) {
			errorInfo.append("第" + rowNum + "行的日期不能为空,请修改后再上传!");
			return;
		}
		
		//if(this.existVenderCode(subCell(cells[0].getContents().trim()))){
			//errorInfo.append("第" + rowNum + "行的供应商编码已经存在,请修改后再上传!");
			//return;
		//}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String date = sdf.format(new Date());
		venderDefinePO.setZtDate(sdf.parse(date.substring(0, 2) + subCell(cells[0].getContents().trim())));
		
		if ("" == subCell(cells[1].getContents().trim())) {
			errorInfo.append("第" + rowNum + "行的位置名称不能为空,请修改后再上传!");
			return;
		}

		//if(reDao.existVenderName(subCell(cells[1].getContents().trim()))){
			//errorInfo.append("第" + rowNum + "行的供应商名称已经存在,请修改后再上传!");
			//return;
		//}
		venderDefinePO.setZtAddress(subCell(cells[1].getContents().trim()));
		
		list.add(venderDefinePO);
	}
	
	public boolean existVenderCode(String venderCode) throws Exception {
		try {
			TtVehicleAddressPO po = new TtVehicleAddressPO();
			po.setZtAddress(venderCode);
			List list = reDao.select(po);
			if (list.size() > 0) {
				return true;
			}
			return false;
		} catch (Exception e) {
			throw e;
		}
	}
	
	private String subCell(String orgAmt) {
		String newAmt = "";
		if (null == orgAmt || "".equals(orgAmt)) {
			return newAmt;
		}
		if (orgAmt.length() > 30) {
			newAmt = orgAmt.substring(0, 30);
		} else {
			newAmt = orgAmt;
		}
		return newAmt;
	}
}
