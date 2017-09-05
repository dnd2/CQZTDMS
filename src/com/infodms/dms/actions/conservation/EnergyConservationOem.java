package com.infodms.dms.actions.conservation;

import java.io.IOException;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import jxl.Workbook;
import jxl.write.DateTime;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

import org.apache.log4j.Logger;

import com.infodms.dms.actions.repairOrder.RoMaintainMain;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.common.materialManager.MaterialGroupManagerDao;
import com.infodms.dms.dao.conservation.EnergyConservationOemDao;
import com.infodms.dms.dao.sales.ordermanage.delivery.OrderDeliveryDao;
import com.infodms.dms.dao.sales.ordermanage.orderreport.OrderReportDao;
import com.infodms.dms.dao.sales.planmanage.YearPlanDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TtVsEnergyConservationChgPO;
import com.infodms.dms.po.TtVsEnergyConservationPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.csv.CsvWriterUtil;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;
import com.infoservice.po3.bean.PageResult;

public class EnergyConservationOem {
	
	private static final EnergyConservationOemDao dao = new EnergyConservationOemDao();
	private final OrderReportDao reportDao = OrderReportDao.getInstance();
	public static final EnergyConservationOemDao getInstance() {
		return dao;
	}
	private Logger logger = Logger.getLogger(RoMaintainMain.class);
	private final String ENERGY_CON_DETAIL_QUERY = "/jsp/conservation/conOem/benefitPeople_qry.jsp";// 申请查询页面
	private final String ENERGY_CON_CHECK_QUERY = "/jsp/conservation/conOem/benefitPeople_chk_Init.jsp";
	private final String ENERGY_CON_DETAIL_CHECK = "/jsp/conservation/conOem/benefitPeople_chk_Dtl.jsp";
	private final String ENERGY_CON_EXPORT_QUERY = "/jsp/conservation/conOem/benefitPeople_export.jsp";
	private final String ENERGY_CON_OUTCHECK_QUERY = "/jsp/conservation/conOem/benefitPeople_outcheck.jsp";
	private final String ENERGY_CON_VIEW_DETAIL = "/jsp/conservation/conDlr/benefitPeople_Dlr_Dtl.jsp";
	
	/**
	 * 
	 * @Title: roForward
	 * @Description: TODO(车辆节能惠民信息审核查询)
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	public void vehicleEnergyConQuery() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			String COMMAND = CommonUtils.checkNull(request.getParamValue("COMMAND"));
			if(!"".equals(COMMAND)){
				String saleStartDate = CommonUtils.checkNull(request.getParamValue("saleStartDate"));
				String saleEndDate = CommonUtils.checkNull(request.getParamValue("saleEndDate"));
				String reportStartDate = CommonUtils.checkNull(request.getParamValue("reportStartDate"));
				String reportEndDate = CommonUtils.checkNull(request.getParamValue("reportEndDate"));
				String dealerCode = CommonUtils.checkNull(request.getParamValue("dealerCode"));
				String model = CommonUtils.checkNull(request.getParamValue("model"));
				String orgId = CommonUtils.checkNull(request.getParamValue("orgId"));
				String areaId = CommonUtils.checkNull(request.getParamValue("areaId"));
				String energyNo = CommonUtils.checkNull(request.getParamValue("energyNo"));
				String vin = CommonUtils.checkNull(request.getParamValue("vin"));
				if (null != vin && !"".equals(vin)) {
					vin = vin.trim();
				}
				String dPro = CommonUtils.checkNull(request.getParamValue("dPro"));
				String dCity = CommonUtils.checkNull(request.getParamValue("dCity"));
				
				Integer curPage = request.getParamValue("curPage") != null ? Integer
						.parseInt(request.getParamValue("curPage"))
						: 1; // 处理当前页
				PageResult<Map<String, Object>> ps = dao.energyConApplyQuery(saleStartDate,
						saleEndDate,reportStartDate,reportEndDate,dealerCode,model,orgId,areaId,energyNo,vin,dPro,dCity,
						curPage, Constant.PAGE_SIZE);
				act.setOutData("ps", ps);
			}else{
				List<Map<String, Object>> areaBusList=MaterialGroupManagerDao.getPoseIdBusiness(logonUser.getPoseId().toString());
//				List<Map<String, Object>> orgIdList = reportDao.getTMOREG();
				List<Map<String, Object>> orgIdList = OrderDeliveryDao.getInstance().getOemOrg(logonUser.getDutyType(), logonUser.getOrgId()) ;
				act.setOutData("orgIdList", orgIdList);
				act.setOutData("areaBusList", areaBusList);
				act.setForword(ENERGY_CON_CHECK_QUERY);
			}
			
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "车辆节能惠民信息审核查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 
	 * @Title: roForward
	 * @Description: TODO(车辆节能惠民审核)
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	public void vehicleEnergyConCheck() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			String COMMAND = CommonUtils.checkNull(request.getParamValue("COMMAND"));
			if(!"".equals(COMMAND)){
				String conservationId = CommonUtils.checkNull(request.getParamValue("conservationId"));
				String arg = CommonUtils.checkNull(request.getParamValue("arg"));
				String desc = CommonUtils.checkNull(request.getParamValue("remark"));
				TtVsEnergyConservationPO ec = new TtVsEnergyConservationPO();
				ec.setConservationId(Long.valueOf(conservationId));
				TtVsEnergyConservationPO ecValue = new TtVsEnergyConservationPO();
				if("1".equals(arg)){
					ecValue.setConservationStatus(Constant.VEHICLE_ENERGY_CON_SALEPASS);
				}else{
					ecValue.setConservationStatus(Constant.VEHICLE_ENERGY_CON_CANCLE);
				}
				ecValue.setUpdateBy(logonUser.getUserId());
				ecValue.setUpdateDate(new Date());
				dao.update(ec, ecValue);
				TtVsEnergyConservationChgPO ecc = new TtVsEnergyConservationChgPO();
				ecc.setChangeId(Utility.getLong(SequenceManager.getSequence("")));
				ecc.setConservationId(Long.valueOf(conservationId));
				ecc.setCreateBy(logonUser.getUserId());
				ecc.setCreateDate(new Date());
				if("1".equals(arg)){
					ecc.setStatus(Constant.VEHICLE_ENERGY_CON_SALEPASS);
				}else{
					ecc.setStatus(Constant.VEHICLE_ENERGY_CON_CANCLE);
				}
				
				ecc.setDescription(desc);
				dao.insert(ecc);
				act.setForword(ENERGY_CON_CHECK_QUERY);
			}else{
				String conservationId = CommonUtils.checkNull(request.getParamValue("conservationId"));
				
				Map<String, Object> map = dao.energyConDetailCheckA(conservationId);
				List<Map<String, Object>> listEnergyConStatus = dao.energyConStatusInfo(conservationId);
				act.setOutData("map", map);
				act.setOutData("listEnergyConStatus", listEnergyConStatus);
				act.setOutData("conservationId",conservationId );
				act.setForword(ENERGY_CON_DETAIL_CHECK);
			}
			
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "车辆节能惠民审核");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 
	 * @Title: roForward
	 * @Description: TODO(车辆节能惠民批量审核)
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	public void vehicleEnergyConBatchCheck() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			String COMMAND = CommonUtils.checkNull(request.getParamValue("COMMAND"));
			if(!"".equals(COMMAND)){
				
			}else{
				String[] conservationIds = request.getParamValues("conservationId");
				for(int i = 0; i < conservationIds.length; i++){
					TtVsEnergyConservationPO ec = new TtVsEnergyConservationPO();
					ec.setConservationId(Long.valueOf(conservationIds[i]));
					TtVsEnergyConservationPO ecValue = new TtVsEnergyConservationPO();
					ecValue.setConservationStatus(Constant.VEHICLE_ENERGY_CON_CANCLE);
					ecValue.setUpdateBy(logonUser.getUserId());
					ecValue.setUpdateDate(new Date());
					dao.update(ec, ecValue);
					TtVsEnergyConservationChgPO ecc = new TtVsEnergyConservationChgPO();
					ecc.setChangeId(Utility.getLong(SequenceManager.getSequence("")));
					ecc.setConservationId(Long.valueOf(conservationIds[i]));
					ecc.setCreateBy(logonUser.getUserId());
					ecc.setCreateDate(new Date());
					ecc.setStatus(Constant.VEHICLE_ENERGY_CON_CANCLE);
					ecc.setDescription("批量审核驳回！");
					dao.insert(ecc);
				}
				act.setOutData("msg", "01");
			}
			
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "车辆节能惠民批量审核");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	public void vehicleEnergyConAllCheck() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			String COMMAND = CommonUtils.checkNull(request.getParamValue("COMMAND"));
			if(!"".equals(COMMAND)){
				
			} else {
				String saleStartDate = CommonUtils.checkNull(request.getParamValue("saleStartDate"));
				String saleEndDate = CommonUtils.checkNull(request.getParamValue("saleEndDate"));
				String reportStartDate = CommonUtils.checkNull(request.getParamValue("reportStartDate"));
				String reportEndDate = CommonUtils.checkNull(request.getParamValue("reportEndDate"));
				String dealerCode = CommonUtils.checkNull(request.getParamValue("dealerCode"));
				String model = CommonUtils.checkNull(request.getParamValue("model"));
				String orgId = CommonUtils.checkNull(request.getParamValue("orgId"));
				String areaId = CommonUtils.checkNull(request.getParamValue("areaId"));
				String energyNo = CommonUtils.checkNull(request.getParamValue("energyNo"));
				String vin = CommonUtils.checkNull(request.getParamValue("vin"));
				if (null != vin && !"".equals(vin)) {
					vin = vin.trim();
				}
				String dPro = CommonUtils.checkNull(request.getParamValue("dPro"));
				String dCity = CommonUtils.checkNull(request.getParamValue("dCity"));
				List<Map<String, Object>> conList = dao.energyConApplyQuery(saleStartDate, saleEndDate,reportStartDate,reportEndDate,dealerCode,model,orgId,areaId,energyNo,vin,dPro,dCity, 1, 99999).getRecords();
				int chkSize = dao.energyConApplyUpadate(saleStartDate,saleEndDate,reportStartDate,reportEndDate,dealerCode,model,orgId,areaId,energyNo,vin,dPro,dCity);
				
				if(chkSize > 0) {
					int length = conList.size() ;
					for(int i=0; i<length; i++) {
						TtVsEnergyConservationChgPO ecc = new TtVsEnergyConservationChgPO();
						ecc.setChangeId(Utility.getLong(SequenceManager.getSequence("")));
						ecc.setConservationId(Long.parseLong(conList.get(i).get("CONSERVATION_ID").toString()));
						ecc.setCreateBy(logonUser.getUserId());
						ecc.setCreateDate(new Date());
						ecc.setStatus(Constant.VEHICLE_ENERGY_CON_SALEPASS);
						ecc.setDescription("批量审核通过！");
						dao.insert(ecc);
					}
				}
			}
			act.setOutData("msg", "01");
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "车辆节能惠民批量审核");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 
	 * @Title: roForward
	 * @Description: TODO(车辆节能惠民审核--全部通过)
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	public void vehicleEnergyAllCheck() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			String COMMAND = CommonUtils.checkNull(request.getParamValue("COMMAND"));
			if(!"".equals(COMMAND)){
				
			} else {
				String saleStartDate = CommonUtils.checkNull(request.getParamValue("saleStartDate"));
				String saleEndDate = CommonUtils.checkNull(request.getParamValue("saleEndDate"));
				String reportStartDate = CommonUtils.checkNull(request.getParamValue("reportStartDate"));
				String reportEndDate = CommonUtils.checkNull(request.getParamValue("reportEndDate"));
				String dealerCode = CommonUtils.checkNull(request.getParamValue("dealerCode"));
				String model = CommonUtils.checkNull(request.getParamValue("model"));
				String orgId = CommonUtils.checkNull(request.getParamValue("orgId"));
				String areaId = CommonUtils.checkNull(request.getParamValue("areaId"));
				String energyNo = CommonUtils.checkNull(request.getParamValue("energyNo"));
				String vin = CommonUtils.checkNull(request.getParamValue("vin"));
				String remark = CommonUtils.checkNull(request.getParamValue("remark"));
				if (null != vin && !"".equals(vin)) {
					vin = vin.trim();
				}
				String expVer = CommonUtils.checkNull(request.getParamValue("expVer"));
				String dPro = CommonUtils.checkNull(request.getParamValue("dPro"));
				String dCity = CommonUtils.checkNull(request.getParamValue("dCity"));
				
				List<Map<String, Object>> conList = dao.energyConOutCheckQuery(expVer,saleStartDate,saleEndDate,reportStartDate,reportEndDate,dealerCode,model,orgId,areaId,energyNo,vin,dPro,dCity, 1, 99999).getRecords();
				int chkSize = dao.energyConOutCheckUpdate(expVer, saleStartDate, saleEndDate, reportStartDate, reportEndDate, dealerCode, model, orgId, areaId, energyNo, vin, dPro, dCity) ;
				
				if(chkSize > 0) {
					int length = conList.size() ;
					for(int i=0; i<length; i++) {
						TtVsEnergyConservationChgPO ecc = new TtVsEnergyConservationChgPO();
						ecc.setChangeId(Utility.getLong(SequenceManager.getSequence("")));
						ecc.setConservationId(Long.parseLong(conList.get(i).get("CONSERVATION_ID").toString()));
						ecc.setCreateBy(logonUser.getUserId());
						ecc.setCreateDate(new Date());
						ecc.setStatus(Constant.VEHICLE_ENERGY_CON_PASS);
						ecc.setDescription(remark);
						dao.insert(ecc);
					}
				}
			}
			act.setOutData("msg", "01");
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "车辆节能惠民批量审核");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 
	 * @Title: roForward
	 * @Description: TODO(车辆节能惠民信息导出查询)
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	public void vehicleEnergyConExportQuery() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			String COMMAND = CommonUtils.checkNull(request.getParamValue("COMMAND"));
			if(!"".equals(COMMAND)){
				
				String reportStartDate = CommonUtils.checkNull(request.getParamValue("reportStartDate"));
				String reportEndDate = CommonUtils.checkNull(request.getParamValue("reportEndDate"));
				String dealerCode = CommonUtils.checkNull(request.getParamValue("dealerCode"));
				String model = CommonUtils.checkNull(request.getParamValue("model"));
				String orgId = CommonUtils.checkNull(request.getParamValue("orgId"));
				String areaId = CommonUtils.checkNull(request.getParamValue("areaId"));
				String energyNo = CommonUtils.checkNull(request.getParamValue("energyNo"));
				String vin = CommonUtils.checkNull(request.getParamValue("vin"));
				String isEx = CommonUtils.checkNull(request.getParamValue("isEx")) ;
				if (null != vin && !"".equals(vin)) {
					vin = vin.trim();
				}
				String dPro = CommonUtils.checkNull(request.getParamValue("dPro"));
				String dCity = CommonUtils.checkNull(request.getParamValue("dCity"));
				
				Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
				PageResult<Map<String, Object>> ps = dao.energyConExportQuery(isEx, reportStartDate,reportEndDate,dealerCode,model,orgId,areaId,energyNo,vin,dPro,dCity, curPage, Constant.PAGE_SIZE);
				act.setOutData("ps", ps);
			}else{
				List<Map<String, Object>> areaBusList=MaterialGroupManagerDao.getPoseIdBusiness(logonUser.getPoseId().toString());
				List<Map<String, Object>> orgIdList = OrderDeliveryDao.getInstance().getOemOrg(logonUser.getDutyType(), logonUser.getOrgId()) ;
				act.setOutData("orgIdList", orgIdList);
				act.setOutData("areaBusList", areaBusList);
				act.setForword(ENERGY_CON_EXPORT_QUERY);
			}
			
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "车辆节能惠民信息导出查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 车辆节能惠民信息下载
	 */
	public void vehicleEnergyConExportDo(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = null;
		RequestWrapper request = act.getRequest();
		ResponseWrapper response = act.getResponse();
		logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		act.getSession().get(Constant.LOGON_USER);

		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");
		Calendar   calendar = Calendar.getInstance();
		Long timeStr = Long.parseLong(sdf.format(calendar.getTime()).toString());
		
		// 导出的文件名
		String fileName = "节能惠民申请_"+timeStr+".csv";
		// 导出的文字编码
		OutputStream os = null;
		try {
			
			
			fileName = new String(fileName.getBytes("GB2312"), "ISO8859-1");
			response.setContentType("Application/text/csv");
			response.addHeader("Content-Disposition", "attachment;filename="+ fileName);

			List<List<Object>> list = new LinkedList<List<Object>>();
			List<Object> listTemp = new LinkedList<Object>();
			listTemp.add("节能惠民单号");
			listTemp.add("上报日期");
			listTemp.add("经销商代码");
			listTemp.add("经销商名称");
			listTemp.add("车型代码");
			listTemp.add("车型");
			listTemp.add("消费者名称");
			listTemp.add("联系人");
			listTemp.add("联系电话");
			listTemp.add("VIN");
			listTemp.add("销售日期");
			list.add(listTemp);
			
			String reportStartDate = CommonUtils.checkNull(request.getParamValue("reportStartDate"));
			String reportEndDate = CommonUtils.checkNull(request.getParamValue("reportEndDate"));
			String dealerCode = CommonUtils.checkNull(request.getParamValue("dealerCode"));
			String model = CommonUtils.checkNull(request.getParamValue("model"));
			String orgId = CommonUtils.checkNull(request.getParamValue("orgId"));
			String areaId = CommonUtils.checkNull(request.getParamValue("areaId"));
			String energyNo = CommonUtils.checkNull(request.getParamValue("energyNo"));
			String vin = CommonUtils.checkNull(request.getParamValue("vin"));
			if (null != vin && !"".equals(vin)) {
				vin = vin.trim();
			}
			String dPro = CommonUtils.checkNull(request.getParamValue("dPro"));
			String dCity = CommonUtils.checkNull(request.getParamValue("dCity"));
			
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1; // 处理当前页
			PageResult<Map<String, Object>> ps = dao.energyConExportQuery("-1",
					reportStartDate,reportEndDate,dealerCode,model,orgId,areaId,energyNo,vin,dPro,dCity,
					curPage, 100000);
			List<Map<String, Object>> results  = ps.getRecords();
			
			if(results!=null && !results.equals("")){

				for (int i = 0; i < results.size(); i++) {
					Map<String, Object> record = results.get(i);
					listTemp = new LinkedList<Object>();
					listTemp.add(CommonUtils.checkNull(record.get("CONSERVATION_NO")));
					listTemp.add(CommonUtils.checkNull(record.get("CREATE_DATE")));
					listTemp.add(CommonUtils.checkNull(record.get("DEALERCODE")));
					listTemp.add(CommonUtils.checkNull(record.get("DEALERNAME")));
					listTemp.add(CommonUtils.checkNull(record.get("MODELCODE")));
					listTemp.add(CommonUtils.checkNull(record.get("MODELNAME")));
					listTemp.add(CommonUtils.checkNull(record.get("CTM_NAME")));
					listTemp.add(CommonUtils.checkNull(record.get("CTM_LINKMAN")));
					listTemp.add(CommonUtils.checkNull(record.get("CTM_LINKTEL")));
					listTemp.add(CommonUtils.checkNull(record.get("VIN")));
					listTemp.add(CommonUtils.checkNull(record.get("PURCHASED_DATE")));
					list.add(listTemp);
					//设置导出标识
					TtVsEnergyConservationPO ec = new TtVsEnergyConservationPO();
					ec.setConservationId(Long.valueOf(record.get("CONSERVATION_ID").toString()));
					TtVsEnergyConservationPO ecValue = new TtVsEnergyConservationPO();
					ecValue.setIsExport(1);
	
					ecValue.setExportFlag(timeStr);
					ecValue.setExportPose(logonUser.getPoseId());
					ecValue.setExportUser(logonUser.getUserId());
					ecValue.setUpdateBy(logonUser.getUserId());
					ecValue.setUpdateDate(new Date());
					dao.update(ec, ecValue);
				}
			}
			os = response.getOutputStream();
			CsvWriterUtil.writeCsv(list, os);		
			os.flush();
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"车辆节能惠民信息下载");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 车辆节能惠民信息下载XLS格式
	 */
	public void vehicleEnergyConExportDoXLS(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		OutputStream os = null;
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");
		Calendar   calendar = Calendar.getInstance();
		Long timeStr = Long.parseLong(sdf.format(calendar.getTime()).toString());
		
		try{
			ResponseWrapper response = act.getResponse();
			
			// 用于下载传参的集合
			List<List<Object>> list = new LinkedList<List<Object>>();
			
			//标题
			List<Object> listHead = new LinkedList<Object>();
			listHead.add("消费者名称");
			listHead.add("联系人");
			listHead.add("联系电话");
			listHead.add("详细地址");
			listHead.add("消费者实际所在地行政区划代码");
			listHead.add("消费者实际所在地行政区划代码对应名称");
			listHead.add("销售时间");
			listHead.add("销售车辆通用名称");
			listHead.add("销售车辆型号");
			listHead.add("车辆识别代号");
			listHead.add("车辆牌照号码");
			listHead.add("厂商指导价（元）");
			listHead.add("销售价格（元）");
			listHead.add("发票号");
			listHead.add("终端经销商名称");
			listHead.add("联系人");
			listHead.add("联系电话");
			listHead.add("详细地址");
			listHead.add("经销商实际所在地行政区划代码");
			listHead.add("经销商实际所在地行政区划代码对应名称");
			listHead.add("备注");
			
			list.add(listHead);
			
			String reportStartDate = CommonUtils.checkNull(request.getParamValue("reportStartDate"));
			String reportEndDate = CommonUtils.checkNull(request.getParamValue("reportEndDate"));
			String dealerCode = CommonUtils.checkNull(request.getParamValue("dealerCode"));
			String model = CommonUtils.checkNull(request.getParamValue("model"));
			String orgId = CommonUtils.checkNull(request.getParamValue("orgId"));
			String areaId = CommonUtils.checkNull(request.getParamValue("areaId"));
			String energyNo = CommonUtils.checkNull(request.getParamValue("energyNo"));
			String vin = CommonUtils.checkNull(request.getParamValue("vin"));
			String isEx = CommonUtils.checkNull(request.getParamValue("isEx"));
			if (null != vin && !"".equals(vin)) {
				vin = vin.trim();
			}
			String dPro = CommonUtils.checkNull(request.getParamValue("dPro"));
			String dCity = CommonUtils.checkNull(request.getParamValue("dCity"));
			
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1; // 处理当前页
			PageResult<Map<String, Object>> ps = dao.energyConExportQuery(isEx, reportStartDate,reportEndDate,dealerCode,model,orgId,areaId,energyNo,vin,dPro,dCity,
					curPage, 100000);
			
			List<Map<String, Object>> orgSeriesList = ps.getRecords();
			
			if(!CommonUtils.isNullList(orgSeriesList)) {
				for(int i=0; i<orgSeriesList.size();i++){
					List<Object> listValue = new LinkedList<Object>();
					listValue.add(orgSeriesList.get(i).get("CTM_NAME") != null ? orgSeriesList.get(i).get("CTM_NAME") : "");
					listValue.add(orgSeriesList.get(i).get("CTM_LINKMAN") != null ? orgSeriesList.get(i).get("CTM_LINKMAN") : "");
					listValue.add(orgSeriesList.get(i).get("CTM_LINKTEL") != null ? orgSeriesList.get(i).get("CTM_LINKTEL") : "");
					listValue.add(orgSeriesList.get(i).get("CTM_ADDRESS") != null ? orgSeriesList.get(i).get("CTM_ADDRESS") : "");
					listValue.add(orgSeriesList.get(i).get("CTM_TOWN_ID") != null ? orgSeriesList.get(i).get("CTM_ZIP_NAME") : "");
					listValue.add(orgSeriesList.get(i).get("CTM_TOWN_NAME") != null ? orgSeriesList.get(i).get("CTM_ZIP_NAME") : "");
					listValue.add(orgSeriesList.get(i).get("SALES_DATE") != null ? orgSeriesList.get(i).get("SALES_DATE") : "");
					listValue.add(orgSeriesList.get(i).get("SERIESNAME") != null ? orgSeriesList.get(i).get("SERIESNAME") : "");
					listValue.add(orgSeriesList.get(i).get("MODELCODE") != null ? orgSeriesList.get(i).get("MODELCODE") : "");
					listValue.add(orgSeriesList.get(i).get("VIN") != null ? orgSeriesList.get(i).get("VIN") : "");
					listValue.add(orgSeriesList.get(i).get("VEHICLE_NO") != null ? orgSeriesList.get(i).get("VEHICLE_NO") : "");
					listValue.add(orgSeriesList.get(i).get("FACTORY_PRICE") != null ? orgSeriesList.get(i).get("FACTORY_PRICE") : "");
					listValue.add(orgSeriesList.get(i).get("SALES_PRICE") != null ? orgSeriesList.get(i).get("SALES_PRICE") : "");
					listValue.add(orgSeriesList.get(i).get("INVOCE_NO") != null ? orgSeriesList.get(i).get("INVOCE_NO") : "");
					listValue.add(orgSeriesList.get(i).get("DEALERNAME") != null ? orgSeriesList.get(i).get("DEALERNAME") : "");
					listValue.add(orgSeriesList.get(i).get("DLR_LINKMAN") != null ? orgSeriesList.get(i).get("DLR_LINKMAN") : "");
					listValue.add(orgSeriesList.get(i).get("DLR_LINKTEL") != null ? orgSeriesList.get(i).get("DLR_LINKTEL") : "");
					listValue.add(orgSeriesList.get(i).get("DLR_ADDRESS") != null ? orgSeriesList.get(i).get("DLR_ADDRESS") : "");
					listValue.add(orgSeriesList.get(i).get("DLR_ZIP_CODE") != null ? orgSeriesList.get(i).get("DLR_ZIP_CODE") : "");
					listValue.add(orgSeriesList.get(i).get("DLR_TOWN_NAME") != null ? orgSeriesList.get(i).get("DLR_ZIP_NAME") : "");
					listValue.add(orgSeriesList.get(i).get("REMARK") != null ? orgSeriesList.get(i).get("REMARK") : "");
					list.add(listValue);
					
					if(!"1".equals(isEx)) {
					//设置导出标识
						TtVsEnergyConservationPO ec = new TtVsEnergyConservationPO();
						ec.setConservationId(Long.valueOf(orgSeriesList.get(i).get("CONSERVATION_ID").toString()));
						TtVsEnergyConservationPO ecValue = new TtVsEnergyConservationPO();
						ecValue.setIsExport(1);
		
						ecValue.setExportFlag(timeStr);
						ecValue.setExportPose(logonUser.getPoseId());
						ecValue.setExportUser(logonUser.getUserId());
						ecValue.setUpdateBy(logonUser.getUserId());
						ecValue.setUpdateDate(new Date());
						dao.update(ec, ecValue);
					}
				}
			}
			
			// 导出的文件名
			String fileName = "节能惠民.xls";
			// 导出的文字编码
			fileName = new String(fileName.getBytes("GB2312"), "ISO8859-1");
			response.setContentType("Application/text/xls");
			response.addHeader("Content-Disposition", "attachment;filename=" + fileName);
			
			os = response.getOutputStream();
//			CsvWriterUtil.writeCsv(list, os);
			this.createXlsFile(list, os);
			
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
	 * 
	 * @Title: roForward
	 * @Description: TODO(车辆节能惠民信息外部审核查询)
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	public void vehicleEnergyConOutCheckQuery() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			String COMMAND = CommonUtils.checkNull(request.getParamValue("COMMAND"));
			if(!"".equals(COMMAND)){
				
				String saleStartDate = CommonUtils.checkNull(request.getParamValue("saleStartDate"));
				String saleEndDate = CommonUtils.checkNull(request.getParamValue("saleEndDate"));
				String reportStartDate = CommonUtils.checkNull(request.getParamValue("reportStartDate"));
				String reportEndDate = CommonUtils.checkNull(request.getParamValue("reportEndDate"));
				String dealerCode = CommonUtils.checkNull(request.getParamValue("dealerCode"));
				String model = CommonUtils.checkNull(request.getParamValue("model"));
				String orgId = CommonUtils.checkNull(request.getParamValue("orgId"));
				String areaId = CommonUtils.checkNull(request.getParamValue("areaId"));
				String energyNo = CommonUtils.checkNull(request.getParamValue("energyNo"));
				String vin = CommonUtils.checkNull(request.getParamValue("vin"));
				if (null != vin && !"".equals(vin)) {
					vin = vin.trim();
				}
				String expVer = CommonUtils.checkNull(request.getParamValue("expVer"));
				String dPro = CommonUtils.checkNull(request.getParamValue("dPro"));
				String dCity = CommonUtils.checkNull(request.getParamValue("dCity"));
				
				Integer curPage = request.getParamValue("curPage") != null ? Integer
						.parseInt(request.getParamValue("curPage"))
						: 1; // 处理当前页
				PageResult<Map<String, Object>> ps = dao.energyConOutCheckQuery(
						expVer,saleStartDate,
						saleEndDate,reportStartDate,reportEndDate,dealerCode,model,orgId,areaId,energyNo,vin,dPro,dCity,
						curPage, 400);
				act.setOutData("ps", ps);
			}else{
				List<Map<String,Object>> listExpVer = dao.energyConExportVerNoQuery(logonUser.getUserId().toString());
				List<Map<String, Object>> areaBusList=MaterialGroupManagerDao.getPoseIdBusiness(logonUser.getPoseId().toString());
				List<Map<String, Object>> orgIdList = OrderDeliveryDao.getInstance().getOemOrg(logonUser.getDutyType(), logonUser.getOrgId()) ;
				act.setOutData("orgIdList", orgIdList);
				act.setOutData("areaBusList", areaBusList);
				act.setOutData("listExpVer", listExpVer);
				act.setForword(ENERGY_CON_OUTCHECK_QUERY);
			}
			
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "车辆节能惠民信息外部审核查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 
	 * @Title: roForward
	 * @Description: TODO(车辆节能惠民审批-审批结果反馈)
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	public void vehicleEnergyConBatchFinalCheck() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			String COMMAND = CommonUtils.checkNull(request.getParamValue("COMMAND"));
			if(!"".equals(COMMAND)){
				
			}else{
				String[] conservationIds = request.getParamValues("conservationId");
				String[] allConservationIds = request.getParamValues("allConservationId");
				String flag = CommonUtils.checkNull(request.getParamValue("flag"));
				String remark = CommonUtils.checkNull(request.getParamValue("remark"));
				Integer statusTemp = Constant.VEHICLE_ENERGY_CON_CANCLE;
				String desc = "结果反馈驳回";
				for(int i = 0; i < allConservationIds.length; i++){
					
					for(int j = 0; j < conservationIds.length; j++){
//						System.out.println("1>"+allConservationIds[i]+" <2>"+conservationIds[j]);
						if(allConservationIds[i].equals(conservationIds[j])){
							
							if("1".equals(flag)){   //通过
								statusTemp = Constant.VEHICLE_ENERGY_CON_PASS;
								desc = "结果反馈通过";
								
								TtVsEnergyConservationPO ec = new TtVsEnergyConservationPO();
								ec.setConservationId(Long.valueOf(allConservationIds[i]));
								TtVsEnergyConservationPO ecValue = new TtVsEnergyConservationPO();
								ecValue.setConservationStatus(statusTemp);
								/*ecValue.setRemark(remark);*/
//								ecValue.setStatus(Constant.STATUS_DISABLE);
								ecValue.setUpdateBy(logonUser.getUserId());
								ecValue.setUpdateDate(new Date());
								dao.update(ec, ecValue);
								TtVsEnergyConservationChgPO ecc = new TtVsEnergyConservationChgPO();
								ecc.setChangeId(Utility.getLong(SequenceManager.getSequence("")));
								ecc.setConservationId(Long.valueOf(allConservationIds[i]));
								ecc.setCreateBy(logonUser.getUserId());
								ecc.setCreateDate(new Date());
								ecc.setStatus(statusTemp);
								ecc.setDescription(remark);
								dao.insert(ecc);
								
							} 
							if("2".equals(flag)) {                //驳回
								statusTemp = Constant.VEHICLE_ENERGY_CON_CANCLE;
								desc = "结果反馈驳回";
								
								TtVsEnergyConservationPO ec = new TtVsEnergyConservationPO();
								ec.setConservationId(Long.valueOf(allConservationIds[i]));
								TtVsEnergyConservationPO ecValue = new TtVsEnergyConservationPO();
								ecValue.setConservationStatus(statusTemp);
								ecValue.setRemark(remark);
//								ecValue.setStatus(Constant.STATUS_DISABLE);
								ecValue.setUpdateBy(logonUser.getUserId());
								ecValue.setUpdateDate(new Date());
								dao.update(ec, ecValue);
								TtVsEnergyConservationChgPO ecc = new TtVsEnergyConservationChgPO();
								ecc.setChangeId(Utility.getLong(SequenceManager.getSequence("")));
								ecc.setConservationId(Long.valueOf(allConservationIds[i]));
								ecc.setCreateBy(logonUser.getUserId());
								ecc.setCreateDate(new Date());
								ecc.setStatus(statusTemp);
								ecc.setDescription(remark);
								dao.insert(ecc);
								
								
							}
							break;
						}
					}
					
				}
				act.setOutData("msg", "01");
			}
			
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "车辆节能惠民信息外部审核");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 节能惠民信息明细查看（OEM）
	 */
	public void vehicleEnergyConOemQuery() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			String COMMAND = CommonUtils.checkNull(request.getParamValue("COMMAND"));
			if(!"".equals(COMMAND)){
				String postId = logonUser.getPoseId().toString();
				String saleStartDate = CommonUtils.checkNull(request.getParamValue("saleStartDate"));
				String saleEndDate = CommonUtils.checkNull(request.getParamValue("saleEndDate"));
				String reportStartDate = CommonUtils.checkNull(request.getParamValue("reportStartDate"));
				String reportEndDate = CommonUtils.checkNull(request.getParamValue("reportEndDate"));
				String dealerCode = CommonUtils.checkNull(request.getParamValue("dealerCode"));
				String model = CommonUtils.checkNull(request.getParamValue("model"));
				String orgId = CommonUtils.checkNull(request.getParamValue("orgId"));
				String areaId = CommonUtils.checkNull(request.getParamValue("areaId"));
				String energyNo = CommonUtils.checkNull(request.getParamValue("energyNo"));
				String vin = CommonUtils.checkNull(request.getParamValue("vin"));
				if (null != vin && !"".equals(vin)) {
					vin = vin.trim();
				}
				String dPro = CommonUtils.checkNull(request.getParamValue("dPro"));
				String dCity = CommonUtils.checkNull(request.getParamValue("dCity"));
				String status = CommonUtils.checkNull(request.getParamValue("status"));
				
				Integer curPage = request.getParamValue("curPage") != null ? Integer
						.parseInt(request.getParamValue("curPage"))
						: 1; // 处理当前页
				
				PageResult<Map<String,Object>> ps = dao.energyConViewQuery(
						saleStartDate,saleEndDate,orgId,
						dPro,dCity,dealerCode,
						reportStartDate, reportEndDate, model, 
						 areaId, status, energyNo, vin, postId,
						curPage, Constant.PAGE_SIZE);
				act.setOutData("ps", ps);
			}else{
				List<Map<String, Object>> areaBusList=MaterialGroupManagerDao.getPoseIdBusiness(logonUser.getPoseId().toString());
				List<Map<String, Object>> orgIdList = OrderDeliveryDao.getInstance().getOemOrg(logonUser.getDutyType(), logonUser.getOrgId()) ;
				act.setOutData("orgIdList", orgIdList);
				act.setOutData("areaBusList", areaBusList);
				act.setForword(ENERGY_CON_DETAIL_QUERY);
			}
			
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "节能惠民信息明细查看");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 节能惠民信息汇总查询（OEM）
	 */
	public void vehicleEnergyConOemTotalQuery() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			String postId = logonUser.getPoseId().toString();
			String saleStartDate = CommonUtils.checkNull(request.getParamValue("saleStartDate"));
			String saleEndDate = CommonUtils.checkNull(request.getParamValue("saleEndDate"));
			String reportStartDate = CommonUtils.checkNull(request.getParamValue("reportStartDate"));
			String reportEndDate = CommonUtils.checkNull(request.getParamValue("reportEndDate"));
			String dealerCode = CommonUtils.checkNull(request.getParamValue("dealerCode"));
			String model = CommonUtils.checkNull(request.getParamValue("model"));
			String orgId = CommonUtils.checkNull(request.getParamValue("orgId"));
			String areaId = CommonUtils.checkNull(request.getParamValue("areaId"));
			String energyNo = CommonUtils.checkNull(request.getParamValue("energyNo"));
			String vin = CommonUtils.checkNull(request.getParamValue("vin"));
			if (null != vin && !"".equals(vin)) {
				vin = vin.trim();
			}
			String dPro = CommonUtils.checkNull(request.getParamValue("dPro"));
			String dCity = CommonUtils.checkNull(request.getParamValue("dCity"));
			String status = CommonUtils.checkNull(request.getParamValue("status"));
			
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1; // 处理当前页
			
			PageResult<Map<String,Object>> ps = dao.energyConViewTotalQuery(
					saleStartDate,saleEndDate,orgId,
					dPro,dCity,dealerCode,
					reportStartDate, reportEndDate, model, 
					 areaId, status, energyNo, vin, postId,
					curPage, Constant.PAGE_SIZE);
			act.setOutData("ps", ps);
			
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "节能惠民信息明细查看");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	public void getTotal() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			String postId = logonUser.getPoseId().toString();
			String saleStartDate = CommonUtils.checkNull(request.getParamValue("saleStartDate"));
			String saleEndDate = CommonUtils.checkNull(request.getParamValue("saleEndDate"));
			String reportStartDate = CommonUtils.checkNull(request.getParamValue("reportStartDate"));
			String reportEndDate = CommonUtils.checkNull(request.getParamValue("reportEndDate"));
			String dealerCode = CommonUtils.checkNull(request.getParamValue("dealerCode"));
			String model = CommonUtils.checkNull(request.getParamValue("model"));
			String orgId = CommonUtils.checkNull(request.getParamValue("orgId"));
			String areaId = CommonUtils.checkNull(request.getParamValue("areaId"));
			String energyNo = CommonUtils.checkNull(request.getParamValue("energyNo"));
			String vin = CommonUtils.checkNull(request.getParamValue("vin"));
			if (null != vin && !"".equals(vin)) {
				vin = vin.trim();
			}
			String dPro = CommonUtils.checkNull(request.getParamValue("dPro"));
			String status = CommonUtils.checkNull(request.getParamValue("status"));
			
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
			
			Map<String, String> map = new HashMap<String, String>() ;
			map.put("saleStartDate", saleStartDate) ;
			map.put("saleEndDate", saleEndDate) ;
			map.put("reportStartDate", reportStartDate) ;
			map.put("reportEndDate", reportEndDate) ;
			map.put("dealerCode", dealerCode) ;
			map.put("model", model) ;
			map.put("orgId", orgId) ;
			map.put("areaId", areaId) ;
			map.put("energyNo", energyNo) ;
			map.put("vin", vin) ;
			map.put("dPro", dPro) ;
			map.put("status", status) ;
			PageResult<Map<String,Object>> ps = dao.getTotal(map, curPage, Constant.PAGE_SIZE) ;
			act.setOutData("ps", ps);
		} catch(Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "节能惠民信息明细查看");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	public void getTotalDownload() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		OutputStream os = null;
		try {
			ResponseWrapper response = act.getResponse();
			
			// 用于下载传参的集合
			List<List<Object>> list = new LinkedList<List<Object>>();
			
			//标题
			List<Object> listHead = new LinkedList<Object>();
			listHead.add("大区");
			listHead.add("省份");
			listHead.add("经销商简称");
			listHead.add("车系代码");
			listHead.add("车系名称");
			listHead.add("数量");
			listHead.add("兑现金额");
			
			list.add(listHead);
			
			RequestWrapper request = act.getRequest();
			String postId = logonUser.getPoseId().toString();
			String saleStartDate = CommonUtils.checkNull(request.getParamValue("saleStartDate"));
			String saleEndDate = CommonUtils.checkNull(request.getParamValue("saleEndDate"));
			String reportStartDate = CommonUtils.checkNull(request.getParamValue("reportStartDate"));
			String reportEndDate = CommonUtils.checkNull(request.getParamValue("reportEndDate"));
			String dealerCode = CommonUtils.checkNull(request.getParamValue("dealerCode"));
			String model = CommonUtils.checkNull(request.getParamValue("model"));
			String orgId = CommonUtils.checkNull(request.getParamValue("orgId"));
			String areaId = CommonUtils.checkNull(request.getParamValue("areaId"));
			String energyNo = CommonUtils.checkNull(request.getParamValue("energyNo"));
			String vin = CommonUtils.checkNull(request.getParamValue("vin"));
			if (null != vin && !"".equals(vin)) {
				vin = vin.trim();
			}
			String dPro = CommonUtils.checkNull(request.getParamValue("dPro"));
			String status = CommonUtils.checkNull(request.getParamValue("status"));
			
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
			
			Map<String, String> map = new HashMap<String, String>() ;
			map.put("saleStartDate", saleStartDate) ;
			map.put("saleEndDate", saleEndDate) ;
			map.put("reportStartDate", reportStartDate) ;
			map.put("reportEndDate", reportEndDate) ;
			map.put("dealerCode", dealerCode) ;
			map.put("model", model) ;
			map.put("orgId", orgId) ;
			map.put("areaId", areaId) ;
			map.put("energyNo", energyNo) ;
			map.put("vin", vin) ;
			map.put("dPro", dPro) ;
			map.put("status", status) ;
			PageResult<Map<String,Object>> ps = dao.getTotal(map, curPage, 99999) ;
			List<Map<String, Object>> ReList = ps.getRecords() ;


			if(!CommonUtils.isNullList(ReList)) {
				for(int i=0; i<ReList.size();i++){
					List<Object> listValue = new LinkedList<Object>();
					listValue.add(ReList.get(i).get("ORG_NAME") != null ? ReList.get(i).get("ORG_NAME") : "");
					listValue.add(ReList.get(i).get("DLR_PROVICE_NAME") != null ? ReList.get(i).get("DLR_PROVICE_NAME") : "");
					listValue.add(ReList.get(i).get("DEALER_SHORTNAME") != null ? ReList.get(i).get("DEALER_SHORTNAME") : "");
					listValue.add(ReList.get(i).get("GROUP_CODE") != null ? ReList.get(i).get("GROUP_CODE") : "");
					listValue.add(ReList.get(i).get("GROUP_NAME") != null ? ReList.get(i).get("GROUP_NAME") : "");
					listValue.add(ReList.get(i).get("AMOUNT") != null ? ReList.get(i).get("AMOUNT") : "");
					listValue.add(ReList.get(i).get("TOTOLPRICE") != null ? ReList.get(i).get("TOTOLPRICE") : "");
					list.add(listValue);
				}
			}
			
			// 导出的文件名
			String fileName = "节能惠民汇总.xls";
			// 导出的文字编码
			fileName = new String(fileName.getBytes("GB2312"), "ISO8859-1");
			response.setContentType("Application/text/xls");
			response.addHeader("Content-Disposition", "attachment;filename=" + fileName);
			
			os = response.getOutputStream();
//			CsvWriterUtil.writeCsv(list, os);
			CsvWriterUtil.createXlsFile(list, os);
			
			os.flush();		
		} catch(Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "节能惠民信息明细查看");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	public void createXlsFile(List<List<Object>> content,OutputStream os) throws ParseException{
		try {
			WritableWorkbook workbook = Workbook.createWorkbook(os);
			WritableSheet sheet = workbook.createSheet("下载模板", 0);
			
			jxl.write.DateFormat df = new jxl.write.DateFormat("yyyy-MM-dd"); 
		    jxl.write.WritableCellFormat wcfDF = new jxl.write.WritableCellFormat(df); 
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd") ;
			
			for(int i=0;i<content.size();i++){
				for(int j = 0;j<content.get(i).size();j++){
					if(j != 6 || i == 0) {
						// 添加单元格
						sheet.addCell(new Label(j,i,(content.get(i).get(j) != null ? content.get(i).get(j).toString() : "")));
					} else {
						sheet.addCell(new DateTime(j,i,sdf.parse((content.get(i).get(j)).toString()),wcfDF)) ;
					}
				}
				
			}
			workbook.write();
			workbook.close();
		} catch (RowsExceededException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (WriteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 车辆节能惠民信息下载XLS格式
	 */
	public void vehicleEnergyConDownload(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		OutputStream os = null;
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");
		Calendar   calendar = Calendar.getInstance();
		Long timeStr = Long.parseLong(sdf.format(calendar.getTime()).toString());
		
		try{
			ResponseWrapper response = act.getResponse();
			
			// 用于下载传参的集合
			List<List<Object>> list = new LinkedList<List<Object>>();
			
			//标题
			List<Object> listHead = new LinkedList<Object>();
			listHead.add("消费者名称");
			listHead.add("联系人");
			listHead.add("联系电话");
			listHead.add("详细地址");
			listHead.add("消费者实际所在地行政区划代码");
			listHead.add("消费者实际所在地行政区划代码对应名称");
			listHead.add("销售时间");
			listHead.add("销售车辆通用名称");
			listHead.add("销售车辆型号");
			listHead.add("车辆识别代号");
			listHead.add("车辆牌照号码");
			listHead.add("厂商指导价（元）");
			listHead.add("销售价格（元）");
			listHead.add("发票号");
			listHead.add("终端经销商名称");
			listHead.add("联系人");
			listHead.add("联系电话");
			listHead.add("详细地址");
			listHead.add("经销商实际所在地行政区划代码");
			listHead.add("经销商实际所在地行政区划代码对应名称");
			listHead.add("备注");
			
			list.add(listHead);
			
			String saleStartDate = CommonUtils.checkNull(request.getParamValue("saleStartDate"));
			String saleEndDate = CommonUtils.checkNull(request.getParamValue("saleEndDate"));
			String reportStartDate = CommonUtils.checkNull(request.getParamValue("reportStartDate"));
			String reportEndDate = CommonUtils.checkNull(request.getParamValue("reportEndDate"));
			String dealerCode = CommonUtils.checkNull(request.getParamValue("dealerCode"));
			String model = CommonUtils.checkNull(request.getParamValue("model"));
			String orgId = CommonUtils.checkNull(request.getParamValue("orgId"));
			String areaId = CommonUtils.checkNull(request.getParamValue("areaId"));
			String energyNo = CommonUtils.checkNull(request.getParamValue("energyNo"));
			String vin = CommonUtils.checkNull(request.getParamValue("vin"));
			String status = CommonUtils.checkNull(request.getParamValue("status"));
			if (null != vin && !"".equals(vin)) {
				vin = vin.trim();
			}
			String dPro = CommonUtils.checkNull(request.getParamValue("dPro"));
			String dCity = CommonUtils.checkNull(request.getParamValue("dCity"));
			
			Map<String, String> map = new HashMap<String, String>() ;
			map.put("saleStartDate", saleStartDate) ;
			map.put("saleEndDate", saleEndDate) ;
			map.put("reportStartDate", reportStartDate) ;
			map.put("reportEndDate", reportEndDate) ;
			map.put("dealerCode", dealerCode) ;
			map.put("model", model) ;
			map.put("orgId", orgId) ;
			map.put("areaId", areaId) ;
			map.put("energyNo", energyNo) ;
			map.put("vin", vin) ;
			map.put("status", status) ;
			map.put("dPro", dPro) ;
			map.put("dCity", dCity) ;
			
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
			PageResult<Map<String, Object>> ps = dao.energyConExport(map, curPage, 100000);
			
			List<Map<String, Object>> orgSeriesList = ps.getRecords();
			
			if(!CommonUtils.isNullList(orgSeriesList)) {
				for(int i=0; i<orgSeriesList.size();i++){
					List<Object> listValue = new LinkedList<Object>();
					listValue.add(orgSeriesList.get(i).get("CTM_NAME") != null ? orgSeriesList.get(i).get("CTM_NAME") : "");
					listValue.add(orgSeriesList.get(i).get("CTM_LINKMAN") != null ? orgSeriesList.get(i).get("CTM_LINKMAN") : "");
					listValue.add(orgSeriesList.get(i).get("CTM_LINKTEL") != null ? orgSeriesList.get(i).get("CTM_LINKTEL") : "");
					listValue.add(orgSeriesList.get(i).get("CTM_ADDRESS") != null ? orgSeriesList.get(i).get("CTM_ADDRESS") : "");
					listValue.add(orgSeriesList.get(i).get("CTM_TOWN_ID") != null ? orgSeriesList.get(i).get("CTM_ZIP_CODE") : "");
					listValue.add(orgSeriesList.get(i).get("CTM_TOWN_NAME") != null ? orgSeriesList.get(i).get("CTM_ZIP_NAME") : "");
					listValue.add(orgSeriesList.get(i).get("SALES_DATE") != null ? orgSeriesList.get(i).get("SALES_DATE") : "");
					listValue.add(orgSeriesList.get(i).get("SERIESNAME") != null ? orgSeriesList.get(i).get("SERIESNAME") : "");
					listValue.add(orgSeriesList.get(i).get("MODELCODE") != null ? orgSeriesList.get(i).get("MODELCODE") : "");
					listValue.add(orgSeriesList.get(i).get("VIN") != null ? orgSeriesList.get(i).get("VIN") : "");
					listValue.add(orgSeriesList.get(i).get("VEHICLE_NO") != null ? orgSeriesList.get(i).get("VEHICLE_NO") : "");
					listValue.add(orgSeriesList.get(i).get("FACTORY_PRICE") != null ? orgSeriesList.get(i).get("FACTORY_PRICE") : "");
					listValue.add(orgSeriesList.get(i).get("SALES_PRICE") != null ? orgSeriesList.get(i).get("SALES_PRICE") : "");
					listValue.add(orgSeriesList.get(i).get("INVOCE_NO") != null ? orgSeriesList.get(i).get("INVOCE_NO") : "");
					listValue.add(orgSeriesList.get(i).get("DEALERNAME") != null ? orgSeriesList.get(i).get("DEALERNAME") : "");
					listValue.add(orgSeriesList.get(i).get("DLR_LINKMAN") != null ? orgSeriesList.get(i).get("DLR_LINKMAN") : "");
					listValue.add(orgSeriesList.get(i).get("DLR_LINKTEL") != null ? orgSeriesList.get(i).get("DLR_LINKTEL") : "");
					listValue.add(orgSeriesList.get(i).get("DLR_ADDRESS") != null ? orgSeriesList.get(i).get("DLR_ADDRESS") : "");
					listValue.add(orgSeriesList.get(i).get("DLR_ZIP_CODE") != null ? orgSeriesList.get(i).get("DLR_ZIP_CODE") : "");
					listValue.add(orgSeriesList.get(i).get("DLR_TOWN_NAME") != null ? orgSeriesList.get(i).get("DLR_ZIP_NAME") : "");
					listValue.add(orgSeriesList.get(i).get("REMARK") != null ? orgSeriesList.get(i).get("REMARK") : "");
					list.add(listValue);
				}
			}
			
			// 导出的文件名
			String fileName = "节能惠民.xls";
			// 导出的文字编码
			fileName = new String(fileName.getBytes("GB2312"), "ISO8859-1");
			response.setContentType("Application/text/xls");
			response.addHeader("Content-Disposition", "attachment;filename=" + fileName);
			
			os = response.getOutputStream();
//			CsvWriterUtil.writeCsv(list, os);
			this.createXlsFile(list, os);
			
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
}
