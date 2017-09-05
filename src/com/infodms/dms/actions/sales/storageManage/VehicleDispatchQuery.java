package com.infodms.dms.actions.sales.storageManage;

import java.io.OutputStream;
import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.materialManager.MaterialGroupManagerDao;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.dao.sales.storageManage.CheckVehicleDAO;
import com.infodms.dms.dao.sales.storageManage.VehicleDispatchQueryDAO;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.csv.CsvWriterUtil;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.ActionContextExtend;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

public class VehicleDispatchQuery extends BaseDao{
	public Logger logger = Logger.getLogger(VehicleDispatchQuery.class);
	private ActionContext act = ActionContext.getContext();
	private static final CheckVehicleDAO dao = new CheckVehicleDAO ();
	public static final CheckVehicleDAO getInstance() {
		return dao;
	}
	
	private final String  VehicleDispatchQueryDLRInitUrl = "/jsp/sales/storageManage/vehicleDispatchQueryDLRInit.jsp";
	private final String  vehicleDispatchQueryOEMInitUrl = "/jsp/sales/storageManage/vehicleDispatchQueryOEMInit.jsp";
	
	/**
	 * FUNCTION		:	调拨查询面初始化(DLR)
	 * @param 		:	
	 * @return		:
	 * @throws		:	
	 * LastUpdate	:	2010-5-27
	 */
	public void VehicleDispatchQueryDLRInit(){
		AclUserBean logonUser = null;
		try {
			ActionContextExtend atx = (ActionContextExtend) ActionContext.getContext();
			String reqURL = atx.getRequest().getContextPath();
			if("/CVS-SALES".equals(reqURL.toUpperCase())){
				act.setOutData("returnValue", 1);
			}else{
				act.setOutData("returnValue", 2);
			}
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);	
			act.setForword(VehicleDispatchQueryDLRInitUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "调拨查询面初始化(DLR)");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	} 
	
	/**
	 * FUNCTION		:	调拨查询:查询展示(DLR)
	 * @param 		:	
	 * @return		:
	 * @throws		:	
	 * LastUpdate	:	2010-5-27
	 */
	public void VehicleDispatchQueryDLR(){
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);	
			RequestWrapper request = act.getRequest();
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);	
			String startDate = CommonUtils.checkNull(request.getParamValue("startDate"));	//调拨申请日期
			String endDate = CommonUtils.checkNull(request.getParamValue("endDate"));    	//调拨申请日期
			String materialCode = CommonUtils.checkNull(request.getParamValue("materialCode"));	//物料组
			String inDealerCode = CommonUtils.checkNull(request.getParamValue("inDealerCode"));	//调入经销商
			String outDealerCode = CommonUtils.checkNull(request.getParamValue("outDealerCode"));	//调出经销商
			String vin =  CommonUtils.checkNull(request.getParamValue("vin"));				//VIN
			String TRANSFER_NO   =  CommonUtils.checkNull(request.getParamValue("TRANSFER_NO"));//批发号
			Long poseId = logonUser.getPoseId();
			Long comId = logonUser.getCompanyId() ;
			List<Map<String, Object>> areaList = MaterialGroupManagerDao
					.getDealerId(comId.toString(),poseId.toString());
			String dealerIds__ = "";
			for(int i=0; i<areaList.size();i++) {
				dealerIds__ += areaList.get(i).get("DEALER_ID").toString()+"," ;
			}
			dealerIds__ = dealerIds__.substring(0,(dealerIds__.length()-1)) ;				//当前用户职位对应的经销商ID
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1; // 处理当前页
					//第一个参数dealerIds__ 已经修改为logonUser.getDealerId()
			PageResult<Map<String, Object>> ps = VehicleDispatchQueryDAO.getDispatchHistory_DLR(logonUser.getDealerId(),startDate,endDate,materialCode,inDealerCode,outDealerCode,vin,TRANSFER_NO,Constant.PAGE_SIZE, curPage);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "调拨查询面初始化(DLR)");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	} 
	
	/**
	 * FUNCTION		:	调拨查询面初始化(OEM)
	 * @param 		:	
	 * @return		:
	 * @throws		:	
	 * LastUpdate	:	2010-5-27
	 */
	public void vehicleDispatchQueryOEMInit(){
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
            String dutyType=logonUser.getDutyType();
            act.setOutData("dutyType", dutyType);
            act.setOutData("orgId", logonUser.getOrgId());
			act.setForword(vehicleDispatchQueryOEMInitUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "调拨查询面初始化(DLR)");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	} 
	/**
	 * FUNCTION		:	调拨查询:结果展示 (OEM)
	 * @param 		:	
	 * @return		:
	 * @throws		:	
	 * LastUpdate	:	2010-6-2
	 */
	public void vehicleDispatchQueryOEM(){
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);	
			RequestWrapper request = act.getRequest();
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);	
			String startDate = CommonUtils.checkNull(request.getParamValue("startDate"));	//调拨申请日期
			String endDate = CommonUtils.checkNull(request.getParamValue("endDate"));    	//调拨申请日期
			String materialCode = CommonUtils.checkNull(request.getParamValue("materialCode"));	//物料组
			String outDealerCode = CommonUtils.checkNull(request.getParamValue("outDealerCode"));	//调入经销商
			String inDealerCode = CommonUtils.checkNull(request.getParamValue("inDealerCode"));	//调入经销商
			String TRANSFER_NO = CommonUtils.checkNull(request.getParamValue("TRANSFER_NO")); //批发号
			String areaIds = MaterialGroupManagerDao.getAreaIdsByPoseId(logonUser.getPoseId());//业务范围
			String vin = CommonUtils.checkNull(request.getParamValue("vin"));//vin
			
			String orgId = "" ;
			String dutyType = logonUser.getDutyType() ;

			orgId = logonUser.getOrgId().toString() ;

			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
		
			PageResult<Map<String, Object>> ps = VehicleDispatchQueryDAO.getDispatchHistory_OEM(dutyType,orgId, startDate, endDate, materialCode, outDealerCode, inDealerCode, areaIds,TRANSFER_NO, vin, Constant.PAGE_SIZE, curPage);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "调拨查询:结果展示 (OEM)");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	} 
	
	/**
	 * 车辆批次下明细
	 *  add by yx 20110228
	 */
	public void vhclBatchesDownLoad(){
		AclUserBean logonUser = null;
		RequestWrapper request = act.getRequest();
		ResponseWrapper response = act.getResponse();
		logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
//		act.getSession().get(Constant.LOGON_USER);

		// 导出的文件名
		String fileName = "车辆批次下载.csv";
		// 导出的文字编码
		OutputStream os = null;
		try {
			String startDate = CommonUtils.checkNull(request.getParamValue("startDate"));	//调拨申请日期
			String endDate = CommonUtils.checkNull(request.getParamValue("endDate"));    	//调拨申请日期
			String materialCode = CommonUtils.checkNull(request.getParamValue("materialCode"));	//物料组
			String inDealerCode = CommonUtils.checkNull(request.getParamValue("inDealerCode"));	//调入经销商
			String outDealerCode = CommonUtils.checkNull(request.getParamValue("outDealerCode"));	//调出经销商
			String vin =  CommonUtils.checkNull(request.getParamValue("vin"));				//VIN
			String TRANSFER_NO = CommonUtils.checkNull(request.getParamValue("TRANSFER_NO")); //批发号

			Long poseId = logonUser.getPoseId();
			Long comId = logonUser.getCompanyId() ;
			List<Map<String, Object>> areaList = MaterialGroupManagerDao
					.getDealerId(comId.toString(),poseId.toString());
			String dealerIds__ = "";
			for(int i=0; i<areaList.size();i++) {
				dealerIds__ += areaList.get(i).get("DEALER_ID").toString()+"," ;
			}
			dealerIds__ = dealerIds__.substring(0,(dealerIds__.length()-1)) ;				//当前用户职位对应的经销商ID
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1; // 处理当前页

			fileName = new String(fileName.getBytes("GB2312"), "ISO8859-1");
			response.setContentType("Application/text/csv");
			response.addHeader("Content-Disposition", "attachment;filename="+ fileName);

			List<List<Object>> list = new LinkedList<List<Object>>();
			List<Object> listTemp = new LinkedList<Object>();
			listTemp.add("批发号");
			listTemp.add("调入经销商");
			listTemp.add("调出经销商");
			listTemp.add("VIN");
			listTemp.add("物料名称");
			listTemp.add("申请日期");
			listTemp.add("批发原因");
			listTemp.add("审批状态");
			listTemp.add("审批意见");
			list.add(listTemp);
			//List<Map<String, Object>> results  = dao.getLoadDealerVhclDetail(areaIds.toString(),dealerIds, vin, companyId, 99999999, Constant.PAGE_SIZE);
			PageResult<Map<String, Object>> ps = VehicleDispatchQueryDAO.getDispatchHistory_DLR(dealerIds__,startDate,endDate,materialCode,inDealerCode,outDealerCode,vin, TRANSFER_NO,100000, curPage);
			List<Map<String, Object>> results = ps.getRecords();
			if(results!=null && !results.equals("")){
			for (int i = 0; i < results.size(); i++) {
				Map<String, Object> record = results.get(i);
				listTemp = new LinkedList<Object>();
				listTemp.add(CommonUtils.checkNull(record.get("TRANSFER_NO")));
				listTemp.add(CommonUtils.checkNull(record.get("DEALER_SHORTNAME")));
				listTemp.add(CommonUtils.checkNull(record.get("OUT_DEALER_NAME")));
				listTemp.add(CommonUtils.checkNull(record.get("VIN")));
				listTemp.add(CommonUtils.checkNull(record.get("MATERIAL_NAME")));
				listTemp.add(CommonUtils.checkNull(record.get("APP_DATE")));
				listTemp.add(CommonUtils.checkNull(record.get("TRANSFER_REASON")));
				listTemp.add(CommonUtils.checkNull(record.get("CHECK_STATUS_DESC")));
				listTemp.add(CommonUtils.checkNull(record.get("CHECK_DESC")));
				list.add(listTemp);
			}
			}
			os = response.getOutputStream();
			CsvWriterUtil.writeCsv(list, os);		
			os.flush();
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"车辆批次下载");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	
	
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}

}
