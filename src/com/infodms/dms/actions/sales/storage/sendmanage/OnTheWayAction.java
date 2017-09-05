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

import org.apache.commons.lang.StringUtils;

import com.infodms.dms.actions.claim.basicData.ToExcel;
import com.infodms.dms.actions.parts.baseManager.partsBaseManager.PartBaseImport;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.materialManager.MaterialGroupManagerDao;
import com.infodms.dms.dao.sales.storage.storagebase.LogisticsDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TcPosePO;
import com.infodms.dms.po.TtSalesLogiPO;
import com.infodms.dms.po.TtSalesWayBillAddressPO;
import com.infodms.dms.po.TtSalesWaybillPO;
import com.infodms.dms.po.TtVehicleAddressPO;
import com.infodms.dms.service.ontheway.OnTheWayService;
import com.infodms.dms.service.ontheway.OnTheWayServiceImpl;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.csv.CsvWriterUtil;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infodms.yxdms.utils.BaseAction;
import com.infodms.yxdms.utils.DaoFactory;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;
import com.infoservice.po3.bean.PageResult;

import jxl.Cell;

public class OnTheWayAction extends BaseAction{
	//承运商
	private final String LOCATION_MAINTAIN_INIT_QUERY_UTL_LOGI = "/jsp/sales/storage/sendmanage/commpr/location_maintain_logi.jsp";
	//经销商
	private final String LOCATION_MAINTAIN_INIT_QUERY_UTL_DEALER = "/jsp/sales/storage/sendmanage/commpr/location_maintain_dealer.jsp";
	private final String LOCATION_MAINTAIN_INIT_UTL = "/jsp/sales/storage/sendmanage/commpr/locationMaintain.jsp";
	private final String LOCATION_MAINTAIN_UTL = "/jsp/sales/storage/sendmanage/commpr/locationMaintainCh.jsp";
	private final String LOCATION_MAINTAIN_DETAIL_ADDRESS_UTL = "/jsp/sales/storage/sendmanage/commpr/locationMaintainAddress.jsp";
	private final String SHOW_ONTHEWAY_ADDRESS_DTl_URL = "/jsp/sales/storage/sendmanage/commpr/show_ontheway_dtl_address.jsp";
	private final String LOCATION_MAINTAIN_INIT_QUERY_UTL = "/jsp/sales/storage/sendmanage/commpr/locationMaintainQuery.jsp";
	private final String SHOW_BIND_CAR_ADDRESS_URL = "/jsp/sales/storage/sendmanage/commpr/show_bind_car_address.jsp";
	private final LogisticsDao reLDao = LogisticsDao.getInstance();
	/**
	 *  在途位置维护(初始页)
	 */
	public void locationMaintainInit(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		String areaIds = MaterialGroupManagerDao
		.getPoseIdBusinessIdStr(logonUser.getPoseId().toString());
		try {
			String type = DaoFactory.getParam(request, "query");
			OnTheWayService  service  =  new OnTheWayServiceImpl(); 
			if("query".equals(type)){
				PageResult<Map<String, Object>> list = service.getOnTheWayList(request,loginUser,Constant.PAGE_SIZE,getCurrPage());
				act.setOutData("ps", list);
			}else {
				List<Map<String, Object>> list_logi=new ArrayList<Map<String, Object>>();
				if (logonUser.getPoseBusType().intValue() == Constant.POSE_BUS_TYPE_WL)
				{
					list_logi=reLDao.getLogiByPoseId(areaIds,logonUser);
				}else{
					list_logi=reLDao.getLogiByArea(areaIds);
				}
				act.setOutData("list_logi", list_logi);
				act.setForword(LOCATION_MAINTAIN_INIT_UTL);
			}
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE,
					"在途查询维护");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 在途维护
	 */
	public void onTheWayMaintain() {
		try {
			OnTheWayService  service  =  new OnTheWayServiceImpl(); 
			PageResult<Map<String, Object>> list = service.getTtSalesWayBillDtlpo(request,loginUser,Constant.PAGE_SIZE,getCurrPage());
			act.setOutData("ps", list);
		} catch (Exception e) {
			e.printStackTrace();
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE,
					"在途查询维护");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
		
	}
	
	/**
	 * 在途位置维护(维护页面)
	 */
	public void locationMaintainChInit(){
			try {
				String billId = DaoFactory.getParam(request, "billId");
				OnTheWayService  service  =  new OnTheWayServiceImpl(); 
				TtSalesWaybillPO poValue = service.getTtSalesWaybillPO(Long.valueOf(billId));
				TtSalesLogiPO poiValue = service.getTtSalesLogiPOByLogiId(poValue.getLogiId());
				
				act.setOutData("logiName", poiValue.getLogiName());//承运商名称
				act.setOutData("billNo", poValue.getBillNo());//发运单号
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				act.setOutData("billCrtDate", sdf.format(poValue.getBillCrtDate()));//发运时间
				act.setOutData("chepaiNo", poValue.getChepaiNo());
				act.setOutData("siji", poValue.getSiji());
				act.setOutData("tel", poValue.getTel());
				List<Map<String, Object>> list = service.getMateriaDetail(billId);
				act.setOutData("list", list);
				act.setOutData("billId", billId);
				act.setForword(LOCATION_MAINTAIN_UTL);
		}catch(Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.DETAIL_FAILURE_CODE,"在途位置维护(维护页面)");
			logger.error(loginUser,e1);
			act.setException(e1);
		}
	}

	/**
	 * 添加位置信息初始化页面
	 */
	public void addAddressInit() {
		try {
			String dtl_ids = DaoFactory.getParam(request, "dtl_ids");
			act.setOutData("dtl_ids", dtl_ids);
			act.setForword(LOCATION_MAINTAIN_DETAIL_ADDRESS_UTL);
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "供应商基本信息");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	
	     /**
	      * 保存位置信息
	      */
		public void saveAddAddress() {
			try {
				String billId = request.getParamValue("billId");
				OnTheWayService  service  =  new OnTheWayServiceImpl();
				String res = service.saveOntheWayAddress(request,loginUser);
			
				act.setOutData("billId", billId);
				act.setOutData("OK", 1);
			} catch (Exception e) {// 异常方法
				BizException e1 = new BizException(act, e, ErrorCodeConstant.UPDATE_FAILURE_CODE, "位置上报");
				logger.error(loginUser, e1);
//				act.setException(e1);
				act.setOutData("errinfo", e.getMessage());
			}
		}
		/**
		 * 查看在途位置信息
		 */
		public void showOntheWayAddress() {
			try {
				if(StringUtils.isEmpty(DaoFactory.getParam(request, "dtl_id"))){
					throw new Exception("没有获取到该条记录");
				}
				OnTheWayService  service  =  new OnTheWayServiceImpl();
				List<Map<String,Object>> list = service.showOntheWayAddress(request,loginUser);
				act.setOutData("list", list);
			    act.setForword(SHOW_ONTHEWAY_ADDRESS_DTl_URL);
			} catch (Exception e) {
				e.printStackTrace();
				BizException e1 = new BizException(act, e, ErrorCodeConstant.UPDATE_FAILURE_CODE, e.getMessage());
				logger.error(loginUser, e1);
				act.setException(e1);
			}
		}
		/**
		 * 模板导出
		 */
		public void exportVenderTemplate() {
			OutputStream os = null;
			try {
				ResponseWrapper response = act.getResponse();

				// 用于下载传参的集合
				List<List<Object>> list = new LinkedList<List<Object>>();

				// 标题
				List<Object> listHead = new LinkedList<Object>();// 导出模板第一行
				listHead.add("是否交车(是/否)*");
				listHead.add("司机手机号");
				listHead.add("VIN*");
				listHead.add("位置*");

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
				logger.error(loginUser, e1);
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
		 * 导入
		 */
		@SuppressWarnings("rawtypes")
		public void uploadVenderExcel() {
			String billId = CommonUtils.checkNull(request.getParamValue("billId"));
			StringBuffer errorInfo = new StringBuffer("");
			RequestWrapper request = act.getRequest();
			try {
				long maxSize = 1024 * 1024 * 5;
				PartBaseImport partBaseImport = new PartBaseImport();
				int errNum = partBaseImport.insertIntoTmp(request, "uploadFile", 4, 3, maxSize);

				String err = "";

				if (errNum != 0) {
					switch (errNum) {
					case 1:
						err += "文件列数过多,请修改后再上传!";
						break;
					case 2:
						err += "空行不能大于四行,请修改后再上传!";
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
					List<Map> list = partBaseImport.getMapList();
					List<TtSalesWayBillAddressPO> voList = new ArrayList<TtSalesWayBillAddressPO>();
					loadVoList(voList, list, errorInfo);
					if (errorInfo.length() > 0) {
						BizException e1 = new BizException(act,ErrorCodeConstant.SPECIAL_MEG, errorInfo);
						throw e1;
					}
					
					OnTheWayService  service  =  new OnTheWayServiceImpl();
					if (errorInfo.length() > 0) {
						BizException e1 = new BizException(act,ErrorCodeConstant.SPECIAL_MEG, errorInfo);
						throw e1;
					}
					/**
					 * 导入
					 */
					String res = service.importExcelOnTheWay(voList,request,loginUser);
					if(!"SUCCESS".equals(res)){
//						throw new Exception(res);
						BizException e1 = new BizException(act,ErrorCodeConstant.SPECIAL_MEG, res);
						throw e1;
					}
				}
				this.locationMaintainChInit111(billId);
			} catch (Exception e) {
				BizException e1 = null;
				if (e instanceof BizException) {
					e1 = (BizException) e;
				} else {
					e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG,e.getMessage());
				}
				logger.error(loginUser, e1);
				act.setException(e);
				try {
					if(ErrorCodeConstant.SPECIAL_MEG.equals(e1.getErrCode())){
						act.setOutData("errcode", e.getMessage());
					}
//					String billId = DaoFactory.getParam(request, "billId");
					OnTheWayService  service  =  new OnTheWayServiceImpl(); 
					TtSalesWaybillPO poValue = service.getTtSalesWaybillPO(Long.valueOf(billId));
					TtSalesLogiPO poiValue = service.getTtSalesLogiPOByLogiId(poValue.getLogiId());
					
					act.setOutData("logiName", poiValue.getLogiName());//承运商名称
					act.setOutData("billNo", poValue.getBillNo());//发运单号
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					act.setOutData("billCrtDate", sdf.format(poValue.getBillCrtDate()));//发运时间
					act.setOutData("chepaiNo", poValue.getChepaiNo());
					act.setOutData("siji", poValue.getSiji());
					act.setOutData("tel", poValue.getTel());
					List<Map<String, Object>> list = service.getMateriaDetail(billId);
					act.setOutData("list", list);
					act.setOutData("billId", billId);
					act.setForword(LOCATION_MAINTAIN_UTL);
			}catch(Exception e2) {
				BizException e3 = new BizException(act,e,ErrorCodeConstant.DETAIL_FAILURE_CODE,"在途位置维护(维护页面)");
				logger.error(loginUser,e3);
				act.setException(e3);
			}
//				this.locationMaintainChInit111(billId);//跳转
			}
		}
		
		//专门用于批量导入了跳转到原始页面(带参数的)
		public void locationMaintainChInit111(String billId){
				try {
//					String billId = DaoFactory.getParam(request, "billId");
					OnTheWayService  service  =  new OnTheWayServiceImpl(); 
					TtSalesWaybillPO poValue = service.getTtSalesWaybillPO(Long.valueOf(billId));
					TtSalesLogiPO poiValue = service.getTtSalesLogiPOByLogiId(poValue.getLogiId());
					
					act.setOutData("logiName", poiValue.getLogiName());//承运商名称
					act.setOutData("billNo", poValue.getBillNo());//发运单号
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					act.setOutData("billCrtDate", sdf.format(poValue.getBillCrtDate()));//发运时间
					act.setOutData("chepaiNo", poValue.getChepaiNo());
					act.setOutData("siji", poValue.getSiji());
					act.setOutData("tel", poValue.getTel());
					List<Map<String, Object>> list = service.getMateriaDetail(billId);
					act.setOutData("list", list);
					act.setOutData("billId", billId);
					act.setForword(LOCATION_MAINTAIN_UTL);
			}catch(Exception e) {
				BizException e1 = new BizException(act,e,ErrorCodeConstant.DETAIL_FAILURE_CODE,"在途位置维护(维护页面)");
				logger.error(loginUser,e1);
				act.setException(e1);
			}
		}

		@SuppressWarnings({ "unchecked", "rawtypes" })
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
		
		private void parseCells(List<TtSalesWayBillAddressPO> list, String rowNum, Cell[] cells,StringBuffer errorInfo) throws Exception {
			TtSalesWayBillAddressPO addressPO = new TtSalesWayBillAddressPO();
			if ("" == subCell(cells[0].getContents().trim())) {
				errorInfo.append("第" + rowNum + "行的是否交车不能为空,请修改后再上传!");
				return;
			}else{
				String str = cells[0].getContents().trim();
				if(!"是".equals(str)&&!"否".equals(str)){
					errorInfo.append("第" + rowNum + "行的是否交车只能选择是否,请修改后再上传!");
					return;
				}else{
					if("是".equals(str)){
						addressPO.setIsSub(Constant.WAYBILL_DTL_STATUS_02);
					}else{
						addressPO.setIsSub(Constant.WAYBILL_DTL_STATUS_01);
					}
				}
			}
			
			//if(this.existVenderCode(subCell(cells[0].getContents().trim()))){
				//errorInfo.append("第" + rowNum + "行的供应商编码已经存在,请修改后再上传!");
				//return;
			//}
//			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//			String date = sdf.format(new Date());
//			addressPO.setAddressDate(sdf.parse(date.substring(0, 2) + subCell(cells[0].getContents().trim())));
//			if ("" == subCell(cells[1].getContents().trim())) {
//				errorInfo.append("第" + rowNum + "行的司机手机号不能为空,请修改后再上传!");
//				return;
//			}
			if ("" == subCell(cells[2].getContents().trim())) {
				errorInfo.append("第" + rowNum + "行的VIN不能为空,请修改后再上传!");
				return;
			}
			if ("" == subCell(cells[3].getContents().trim())) {
				errorInfo.append("第" + rowNum + "行的位置名称不能为空,请修改后再上传!");
				return;
			}

			//if(reDao.existVenderName(subCell(cells[1].getContents().trim()))){
				//errorInfo.append("第" + rowNum + "行的供应商名称已经存在,请修改后再上传!");
				//return;
			//}
			addressPO.setDriverPhone(subCell(cells[1].getContents().trim()));
			addressPO.setVin(subCell(cells[2].getContents().trim()));
			addressPO.setAddress(subCell(cells[3].getContents().trim()));
			list.add(addressPO);
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
		
		
		/**
		 * 查询在途信息
		 */
		public void locationMaintainQueryInit(){
			AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			String areaIds = MaterialGroupManagerDao
			.getPoseIdBusinessIdStr(logonUser.getPoseId().toString());
			Integer poseType = loginUser.getPoseType();
			try {
				String type = DaoFactory.getParam(request, "query");
				OnTheWayService  service  =  new OnTheWayServiceImpl(); 
				TcPosePO posePO = service.getTcPostByPostId(loginUser);
				if("query".equals(type)){//查询
					PageResult<Map<String, Object>> list=new PageResult<Map<String,Object>>();
					if(Constant.SYS_USER_SGM.equals(poseType)&&0==posePO.getLogiId()){
						list= service.getCarFactoryOnTheWayListSGM(request,loginUser,Constant.PAGE_SIZE,getCurrPage());
					}else{
						//承运商
						if(StringUtils.isEmpty(loginUser.getDealerId())){
							list= service.getCarFactoryOnTheWayList(request,loginUser,Constant.PAGE_SIZE,getCurrPage());
						}else{//经销商
							list= service.getCarFactoryOnTheWayListDealer(request,loginUser,Constant.PAGE_SIZE,getCurrPage());
						}
					}
					act.setOutData("ps", list);
				}else if ("excel".equals(type)) {
					PageResult<Map<String, Object>> list=new PageResult<Map<String,Object>>();
					
					if(Constant.SYS_USER_SGM.equals(poseType)&&0==posePO.getLogiId()){
						list= service.getCarFactoryOnTheWayListSGM(request,loginUser,Constant.PAGE_SIZE_MAX,getCurrPage());
						String [] head ={"VIN","经销商","组板号","交接单号","批售单号","调拨单号","车型",
								"配置","颜色","车架号","是否交车","最晚发运日期","位置上报时间","最新上报位置地址","详细地址"};
						String [] cols={"VIN","DEALER_NAME","BO_NO","BILL_NO","ORDER_NO","ORDER_NO","MODEL_NAME","PACKAGE_NAME",
								"COLOR_NAME","VIN","STATUS_NAME","DLV_FY_DATE","REPORT_DATE","REPORT_ADDRESS","ADDRESS"};
						ToExcel.toReportExcel(act.getResponse(),request, "在途位置明细.xls",head,cols,list.getRecords());
					}else{
						//承运商
						if(StringUtils.isEmpty(loginUser.getDealerId())){
							list= service.getCarFactoryOnTheWayList(request,loginUser,Constant.PAGE_SIZE_MAX,getCurrPage());
							String [] head ={"VIN","组板号","交接单号","批售单号","调拨单号","车型","配置","颜色","车架号","是否交车","绑定时间","位置上报时间","最新上报位置地址","详细地址"};
							String [] cols={"VIN","BO_NO","BILL_NO","ORDER_NO","ORDER_NO","MODEL_NAME","PACKAGE_NAME",
									"COLOR_NAME","VIN","STATUS_NAME","DRIVER_BIND_DATE","REPORT_DATE","REPORT_ADDRESS","ADDRESS"};
							ToExcel.toReportExcel(act.getResponse(),request, "在途位置明细.xls",head,cols,list.getRecords());
						}else{//经销商
							list= service.getCarFactoryOnTheWayListDealer(request,loginUser,Constant.PAGE_SIZE_MAX,getCurrPage());
							String [] head ={"VIN","交接单号","批售单号","调拨单号","车型","配置","颜色","车架号","是否交车","最晚发运日期","位置上报时间","最新上报位置地址","详细地址"};
							String [] cols={"VIN","BILL_NO","ORDER_NO","ORDER_NO","MODEL_NAME","PACKAGE_NAME",
									"COLOR_NAME","VIN","STATUS_NAME","DLV_FY_DATE","REPORT_DATE","REPORT_ADDRESS","ADDRESS"};
							ToExcel.toReportExcel(act.getResponse(),request, "在途位置明细.xls",head,cols,list.getRecords());
						}
					}
				} else {
					//车厂
					if(Constant.SYS_USER_SGM.equals(poseType)&&0==posePO.getLogiId()){//
						List<Map<String, Object>> list_logi=new ArrayList<Map<String, Object>>();
						if (logonUser.getPoseBusType().intValue() == Constant.POSE_BUS_TYPE_WL)
						{
							list_logi=reLDao.getLogiByPoseId(areaIds,logonUser);
						}else{
							list_logi=reLDao.getLogiByArea(areaIds);
						}
						act.setOutData("list_logi", list_logi);
						act.setForword(LOCATION_MAINTAIN_INIT_QUERY_UTL);
					}else{
						//承运商
						if(StringUtils.isEmpty(loginUser.getDealerId())){
							act.setForword(LOCATION_MAINTAIN_INIT_QUERY_UTL_LOGI);
						}else{//经销商
							act.setForword(LOCATION_MAINTAIN_INIT_QUERY_UTL_DEALER);
						}
						
					}
				}
			} catch (Exception e) {
				BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE,"在途位置查询(初始页)");
				logger.error(loginUser, e1);
				act.setException(e1);
			}
			
			
			
		}
	/**
	 * 查询绑定，解绑日志
	 */
   public void showbindCarlog() {
	   OnTheWayService  service  =  new OnTheWayServiceImpl(); 
	   List<Map<String, Object>> list = service.getbindCarlog(request,loginUser);
       act.setOutData("list", list);
       act.setForword(SHOW_BIND_CAR_ADDRESS_URL);
   }
}
