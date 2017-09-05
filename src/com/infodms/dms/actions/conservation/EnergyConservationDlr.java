package com.infodms.dms.actions.conservation;

import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.actions.repairOrder.RoMaintainMain;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.common.materialManager.MaterialGroupManagerDao;
import com.infodms.dms.dao.common.CommonDAO;
import com.infodms.dms.dao.conservation.EnergyConservationDlrDao;
import com.infodms.dms.dao.conservation.EnergyConservationOemDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TtVsEnergyConservationChgPO;
import com.infodms.dms.po.TtVsEnergyConservationPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.StringUtil;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;
import com.infoservice.po3.bean.PageResult;

public class EnergyConservationDlr {
	
	private static final EnergyConservationOemDao daos = new EnergyConservationOemDao();
	private EnergyConservationDlrDao dao = EnergyConservationDlrDao.getInstance();
	private Logger logger = Logger.getLogger(RoMaintainMain.class);
	private final String ENERGY_CON_APPLY_QUERY = "/jsp/conservation/conDlr/benefitPeople_Init.jsp";// 申请查询页面
	private final String ENERGY_CON_DETAIL_QUERY = "/jsp/conservation/conDlr/benefitPeople_qry.jsp";// 申请查询页面
	private final String ENERGY_CON_DETAIL = "/jsp/conservation/conDlr/benefitPeople_Dlr_Dtl.jsp";// 申请查询页面
	private final String ENERGY_CON_REPORT = "/jsp/conservation/conDlr/benefitPeople_report.jsp";// 申请页面
	private final String dtl_URL = "/jsp/conservation/conDlr/jnhm_qry_dtl.jsp";// 申请查询明细页面
	private final String ENERGY_CON_VIEW_DETAIL = "/jsp/conservation/conDlr/benefitPeople_Dlr_Dtl.jsp";// 申请页面
	/**
	 * 
	 * @Title: roForward
	 * @Description: TODO(车辆节能惠民信息申请)
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	public void vehicleEnergyConApply() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			String COMMAND = CommonUtils.checkNull(request.getParamValue("COMMAND"));
			if(!"".equals(COMMAND)){
				String para = CommonDAO.getPara(Constant.ENERGY_APPLY_END_DATE.toString()) ;
				int endDate = Integer.parseInt(para) ;
				
				if(endDate < Calendar.getInstance().get(Calendar.DAY_OF_MONTH)) {
					throw new RuntimeException("已超过当月节能惠民申请日期!(节能惠民申请截止日期为每月" + endDate + "号)") ;
				}
				
				String dealerId = logonUser.getDealerId();
				Long poseId = logonUser.getPoseId();
				String saleStartDate = CommonUtils.checkNull(request.getParamValue("saleStartDate"));
				String saleEndDate = CommonUtils.checkNull(request.getParamValue("saleEndDate"));
				String model = CommonUtils.checkNull(request.getParamValue("model"));
				String areaId = CommonUtils.checkNull(request.getParamValue("areaId"));
				String vin = CommonUtils.checkNull(request.getParamValue("vin"));
				//Modify by WHX,2012.10.17
				//================================================Start
				String saleDate = null ;//CommonDAO.getPara(Constant.ENERGY_VHL_SALES_DATE.toString()) ;
				//================================================End
				if (null != vin && !"".equals(vin)) {
					vin = vin.trim();
				}
				Integer curPage = request.getParamValue("curPage") != null ? Integer
						.parseInt(request.getParamValue("curPage"))
						: 1; // 处理当前页
				PageResult<Map<String,Object>> ps = dao.saleVehicleQuery(saleDate,
						saleStartDate,saleEndDate,model,areaId,vin,
						dealerId, curPage, Constant.PAGE_SIZE);
				act.setOutData("ps", ps);
			}else{
				List<Map<String, Object>> areaBusList=MaterialGroupManagerDao.getPoseIdBusiness(logonUser.getPoseId().toString());
				act.setOutData("areaBusList", areaBusList);
				act.setForword(ENERGY_CON_APPLY_QUERY);
			}
			
		} catch(RuntimeException e) {
			logger.error(e) ;
			act.setException(e) ;
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "车辆节能惠民信息申请");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 节能惠民提报（DLR）
	 */
	public void vehicleEnergyConReport() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			String COMMAND = CommonUtils.checkNull(request.getParamValue("COMMAND"));
			if(!"".equals(COMMAND)){
				
				//转换日期
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				
				String invoceNo = CommonUtils.checkNull(request.getParamValue("invoceNo"));
				String modelId = CommonUtils.checkNull(request.getParamValue("modelId"));
				String orgId = CommonUtils.checkNull(request.getParamValue("orgId"));
				
				
				String vin = CommonUtils.checkNull(request.getParamValue("vin"));
				String purChasedDate = CommonUtils.checkNull(request.getParamValue("purChasedDate"));
				String modelCode = CommonUtils.checkNull(request.getParamValue("modelName"));
				String modelName = CommonUtils.checkNull(request.getParamValue("modelCode"));
				String vichId = CommonUtils.checkNull(request.getParamValue("vichId"));
				String vichNo = CommonUtils.checkNull(request.getParamValue("vichNo"));
				
				String factoryPrice = CommonUtils.checkNull(request.getParamValue("factoryPrice"));
				String salePrice = CommonUtils.checkNull(request.getParamValue("salePrice"));
				String payDate = CommonUtils.checkNull(request.getParamValue("payDate"));
				String payMoney = CommonUtils.checkNull(request.getParamValue("payMoney"));
				String dealerName = CommonUtils.checkNull(request.getParamValue("dealerName"));
				String dealerCode = logonUser.getDealerCode();
				String dealerId = CommonUtils.checkNull(request.getParamValue("dealerId"));
				String dPro = CommonUtils.checkNull(request.getParamValue("dPro"));
				String dCity = CommonUtils.checkNull(request.getParamValue("dCity"));
				String dTown = CommonUtils.checkNull(request.getParamValue("dTown"));
				String saleZipCode1 = CommonUtils.checkNull(request.getParamValue("saleZipCode1"));
				String linkPerson1 = CommonUtils.checkNull(request.getParamValue("linkPerson1"));
				String linkTel1 = CommonUtils.checkNull(request.getParamValue("linkTel1"));
				
				String fixatePhoneHead = CommonUtils.checkNull(request.getParamValue("fixatePhoneHead")) ;
				String fixatePhoneBody = CommonUtils.checkNull(request.getParamValue("fixatePhoneBody")) ;
				String dlrTell = CommonUtils.checkNull(request.getParamValue("dlrTell")) ;
				if(dlrTell.equals("fix")) {
					linkTel1 = fixatePhoneHead + "-" + fixatePhoneBody ;
				}
				
				String saleZipCode2 = CommonUtils.checkNull(request.getParamValue("saleZipCode2"));
				String salesId = CommonUtils.checkNull(request.getParamValue("salesId"));
				String address1 = CommonUtils.checkNull(request.getParamValue("address1"));
				String address2 = CommonUtils.checkNull(request.getParamValue("address2"));
				String ctmId = CommonUtils.checkNull(request.getParamValue("ctmId"));
				String cPro = CommonUtils.checkNull(request.getParamValue("cPro"));
				String cCity = CommonUtils.checkNull(request.getParamValue("cCity"));
				String cTown = CommonUtils.checkNull(request.getParamValue("cTown"));
				String ctmName = CommonUtils.checkNull(request.getParamValue("ctmName"));
				String linkPerson2 = CommonUtils.checkNull(request.getParamValue("linkPerson2"));
				String linkTel2 = CommonUtils.checkNull(request.getParamValue("linkTel2"));
				String remark = CommonUtils.checkNull(request.getParamValue("remark"));
				
				boolean flag = chkVhlNo(vichNo) ;
				
				if(flag) {
					TtVsEnergyConservationPO energy = new TtVsEnergyConservationPO();
					energy.setConservationId(Utility.getLong(SequenceManager.getSequence("")));
					//生成申请编号
	//				SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMdd");
	//				Calendar   calendar = Calendar.getInstance();
	//				String timeStr = sdf2.format(calendar.getTime()).toString();
					String jNo = Utility.GetBillNo("",dealerCode,"JNO");
	//				String jNo = Utility.getString(SequenceManager.getSequence("JNO"));
					jNo = jNo.substring(jNo.length()-11);
					jNo = "JN"+dealerCode+jNo;
					
					
					
					energy.setConservationNo(jNo);
					energy.setConservationStatus(Constant.VEHICLE_ENERGY_CON_APPLY);
					energy.setCtmAddress(address2);
					energy.setCtmId(Long.valueOf(ctmId));
					energy.setCtmLinkman(linkPerson2);
					energy.setCtmLinktel(linkTel2);
					energy.setCtmProviceId(Long.valueOf(cPro));
					energy.setCtmCityId(Long.valueOf(cCity));
					energy.setCtmTownId(Long.valueOf(cTown));
					energy.setCtmZipCode(Integer.parseInt(saleZipCode2));
					energy.setDealerId(Long.valueOf(dealerId));
					energy.setDlrAddress(address1);
					energy.setDlrLinkman(linkPerson1);
					energy.setDlrProviceId(Long.valueOf(dPro));
					energy.setDlrCityId(Long.valueOf(dCity));
					energy.setDlrLinktel(linkTel1);
					energy.setDlrZipCode(Integer.valueOf(saleZipCode1));
					energy.setExportFlag(0l);
					energy.setExportPose(0l);
					energy.setExportUser(0l);
					energy.setFactoryPrice(Double.valueOf(factoryPrice));
					energy.setInvoceNo(invoceNo);
					energy.setIsExport(0);
					energy.setModelId(Long.valueOf(modelId));
					energy.setOrgId(Long.valueOf(orgId));
					energy.setRemark(remark);
					energy.setSalesDate(sdf.parse(purChasedDate));
					energy.setSalesId(Long.valueOf(salesId));
					energy.setSalesPrice(Double.parseDouble(salePrice));
					energy.setStatus(Constant.STATUS_ENABLE);
					energy.setUpdateBy(logonUser.getUserId());
					energy.setUpdateDate(new Date());
					energy.setCreateBy(logonUser.getUserId());
					energy.setCreateDate(new Date());
					energy.setVehicleId(Long.valueOf(vichId));
					energy.setVehicleNo(vichNo);
					// energy.setPayDate(sdf.parse(payDate));
					energy.setPayMoney(Double.valueOf(payMoney));
					energy.setDlrTownId(Long.valueOf(dTown));
					dao.insert(energy);
					
					//记录申请审核状态
					TtVsEnergyConservationChgPO chg = new TtVsEnergyConservationChgPO();
					chg.setChangeId(Utility.getLong(SequenceManager.getSequence("")));
					chg.setConservationId(energy.getConservationId());
					chg.setCreateBy(logonUser.getUserId());
					chg.setCreateDate(new Date());
					chg.setDescription("");
					chg.setStatus(Constant.VEHICLE_ENERGY_CON_APPLY);
					dao.insert(chg);
					act.setOutData("jNo", jNo);
				} else {
					act.setOutData("returnStr", "error") ;
				}
			}else{
				String vehicleId = CommonUtils.checkNull(request.getParamValue("vehicleId"));
				Map<String, Object> map = dao.saleVehicleDetailQuery(vehicleId);
				
				String dealerId = map.get("DEALER_ID").toString() ;
				String isLast = Constant.STATUS_ENABLE.toString() ;
				
				Map<String, String> theMap = new HashMap<String, String>() ;
				theMap.put("dealerId", dealerId) ;
				theMap.put("isLast", isLast) ;
				
				List<Map<String, Object>> theList = dao.getEnergyList(theMap) ;
				
				if(!CommonUtils.isNullList(theList)) {
					Map<String, Object> EneryMap = theList.get(0) ;
					map.put("PHONE", EneryMap.get("DLR_LINKTEL") == null ? "" : EneryMap.get("DLR_LINKTEL").toString()) ;
					map.put("LINK_MAN", EneryMap.get("DLR_LINKMAN") == null ? "" : EneryMap.get("DLR_LINKMAN").toString()) ;
					map.put("PROVINCE_ID", EneryMap.get("DLR_PROVICE_ID") == null ? "" : EneryMap.get("DLR_PROVICE_ID").toString()) ;
					map.put("CITY_ID", EneryMap.get("DLR_CITY_ID") == null ? "" : EneryMap.get("DLR_CITY_ID").toString()) ;
					map.put("TOWN_ID", EneryMap.get("DLR_TOWN_ID") == null ? "" : EneryMap.get("DLR_TOWN_ID").toString()) ;
					map.put("DEALER_ADDRESS", EneryMap.get("DLR_ADDRESS") == null ? "" : EneryMap.get("DLR_ADDRESS").toString()) ;
					
					if(EneryMap.get("DLR_LINKTEL") != null) {
						if(EneryMap.get("DLR_LINKTEL").toString().indexOf("-") != -1) {
							act.setOutData("phoneType", "fix") ;
						}
					}
				}
				
				act.setOutData("map", map);
				act.setForword(ENERGY_CON_REPORT);
			}
			
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "节能惠民提报");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 节能惠民信息查询（DLR）
	 */
	public void vehicleEnergyConQuery() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			String COMMAND = CommonUtils.checkNull(request.getParamValue("COMMAND"));
			if(!"".equals(COMMAND)){
				String dealerId = logonUser.getDealerId();
//				String saleStartDate = CommonUtils.checkNull(request.getParamValue("saleStartDate"));
//				String saleEndDate = CommonUtils.checkNull(request.getParamValue("saleEndDate"));
				String reportStartDate = CommonUtils.checkNull(request.getParamValue("reportStartDate"));
				String reportEndDate = CommonUtils.checkNull(request.getParamValue("reportEndDate"));
//				String dealerCode = CommonUtils.checkNull(request.getParamValue("dealerCode"));
				String model = CommonUtils.checkNull(request.getParamValue("model"));
//				String orgId = CommonUtils.checkNull(request.getParamValue("orgId"));
				String areaId = CommonUtils.checkNull(request.getParamValue("areaId"));
				String energyNo = CommonUtils.checkNull(request.getParamValue("energyNo"));
				String vin = CommonUtils.checkNull(request.getParamValue("vin"));
				if (null != vin && !"".equals(vin)) {
					vin = vin.trim();
				}
//				String dPro = CommonUtils.checkNull(request.getParamValue("dPro"));
//				String dCity = CommonUtils.checkNull(request.getParamValue("dCity"));
				String status = CommonUtils.checkNull(request.getParamValue("status"));
				
				Integer curPage = request.getParamValue("curPage") != null ? Integer
						.parseInt(request.getParamValue("curPage"))
						: 1; // 处理当前页
				
				PageResult<Map<String,Object>> ps = dao.energyConViewQuery(
						reportStartDate, reportEndDate, model, 
						 areaId, status, energyNo, vin, dealerId,
						curPage, Constant.PAGE_SIZE);
				act.setOutData("ps", ps);
			}else{
				List<Map<String, Object>> areaBusList=MaterialGroupManagerDao.getPoseIdBusiness(logonUser.getPoseId().toString());
				act.setOutData("areaBusList", areaBusList);
				act.setForword(ENERGY_CON_DETAIL_QUERY);
			}
			
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "节能惠民信息查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 节能惠民信息查看（DLR）
	 */
	public void vehicleEnergyConView() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			String COMMAND = CommonUtils.checkNull(request.getParamValue("COMMAND"));
			if(!"".equals(COMMAND)){
				String dealerId = logonUser.getDealerId();
				String reportStartDate = CommonUtils.checkNull(request.getParamValue("reportStartDate"));
				String reportEndDate = CommonUtils.checkNull(request.getParamValue("reportEndDate"));
				String model = CommonUtils.checkNull(request.getParamValue("model"));
				String areaId = CommonUtils.checkNull(request.getParamValue("areaId"));
				String energyNo = CommonUtils.checkNull(request.getParamValue("energyNo"));
				String vin = CommonUtils.checkNull(request.getParamValue("vin"));
				String status = CommonUtils.checkNull(request.getParamValue("status"));
				
				Integer curPage = request.getParamValue("curPage") != null ? Integer
						.parseInt(request.getParamValue("curPage"))
						: 1; // 处理当前页
				
				PageResult<Map<String,Object>> ps = dao.energyConViewQuery(
						reportStartDate, reportEndDate, model, 
						 areaId, status, energyNo, vin, dealerId,
						curPage, Constant.PAGE_SIZE);
				act.setOutData("ps", ps);
			}else{
				String conservationId = CommonUtils.checkNull(request.getParamValue("conservationId"));
				
				Map<String, Object> map = dao.energyConDetailCheckA(conservationId);
				List<Map<String, Object>> listEnergyConStatus = dao.energyConStatusInfo(conservationId);
				act.setOutData("map", map);
				act.setOutData("listEnergyConStatus", listEnergyConStatus);
				act.setOutData("conservationId",conservationId );
				act.setForword(ENERGY_CON_VIEW_DETAIL);
			}
			
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "节能惠民信息查看");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/*
	 * 车辆节能惠民主页面主查询
	 */
	public void mainQuery(){
		 ActionContext act = ActionContext.getContext();
		 RequestWrapper req = act.getRequest() ;
		 AclUserBean user = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try{
			
			String beginDate = req.getParamValue("beginDate");//上报开始时间
			String endDate = req.getParamValue("endDate");//上报结束时间			
			String areaId = req.getParamValue("areaId");//业务范围
			String materialCode = req.getParamValue("materialCode");//车型
			String status = req.getParamValue("status");//状态
			String No = req.getParamValue("No");//节能惠民编号
			String vin = req.getParamValue("vin");//vin	
			
			
			StringBuffer con = new StringBuffer();
			if(StringUtil.notNull(No))
				con.append("  and s.CONSERVATION_NO like '%").append(No).append("%'\n");
			if(StringUtil.notNull(vin))
				con.append("  and TV.VIN = ").append(vin).append("\n");
			
			if(StringUtil.notNull(areaId))
				
				con.append(" AND EXISTS (SELECT H.AREA_ID FROM TM_DEALER_BUSINESS_AREA H WHERE H.DEALER_ID=S.DEALER_ID AND H.AREA_ID LIKE '%"+areaId+"%') \n");
			
			// 选择物料组
			if (null != materialCode && !"".equals(materialCode)) {
				String[] materialCodes = materialCode.split(",");
				if (null != materialCodes && materialCodes.length > 0) {
					StringBuffer buffer = new StringBuffer();
					for (int i = 0; i < materialCodes.length; i++) {
						buffer.append("'").append(materialCodes[i]).append("'")
								.append(",");
					}
					buffer = buffer.deleteCharAt(buffer.length() - 1);
				    con.append("       and  (S.MODEL_ID IN (SELECT G.GROUP_ID \n");
					con.append("                             FROM TM_VHCL_MATERIAL_GROUP G \n");
					con.append("                            WHERE G.GROUP_CODE IN (" + buffer.toString() + "))) \n");
				}
			}
			
			
			if(null != status && !"".equals(status)
					&& !"-1".equals(status))
				con.append("  and s.STATUS = ").append(status).append("\n");

			if(StringUtil.notNull(beginDate))
				con.append("  and s.CREATE_DATE>=to_date('").append(beginDate).append(" 00:00:00','YYYY-MM-DD hh24:mi:ss')\n");
			if(StringUtil.notNull(endDate))
				con.append("  and s.CREATE_DATE<=to_date('").append(endDate).append(" 23:59:59','YYYY-MM-DD hh24:mi:ss')\n");
			
			con.append("  and s.DEALER_ID in (").append(user.getDealerId()).append(")\n");
			
			int pageSize = 10;
			Integer curPage = req.getParamValue("curPage") !=null ? Integer.parseInt(req.getParamValue("curPage")) : 1;
			PageResult<Map<String,Object>> ps = dao.mainQuery(con.toString(), pageSize, curPage);
			act.setOutData("ps",ps);
		} catch(Exception e){
			BizException be = new BizException(act,e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "车辆节能惠民");
			logger.error(user, be);
			act.setException(be);
		}
	}
	
	/*public void vehicledtl() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			String COMMAND = CommonUtils.checkNull(request.getParamValue("COMMAND"));
				String conservationId = CommonUtils.checkNull(request.getParamValue("conservationId"));
				
				Map<String, Object> map = dao.energyConDetailCheck(conservationId);
				List<Map<String, Object>> listEnergyConStatus = dao.energyConStatusInfo(conservationId);
				act.setOutData("map", map);
				act.setOutData("listEnergyConStatus", listEnergyConStatus);
				act.setOutData("conservationId",conservationId );
				act.setForword(dtl_URL);
		//	}
			
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "车辆节能惠民查询信息");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}*/
	
	// 2011/11/21
	public void vehicleMuchReport() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			String flag = "success" ;
			String[] vehicleIds = request.getParamValues("vehicleIds");
			int length = vehicleIds.length ;
			
			if(length > 0) {
				for(int i=0; i<length; i++) {
					Map<String, Object> map = dao.saleVehicleDetailQuery(vehicleIds[i]);
	
					String dealerId = map.get("DEALER_ID").toString();
					String isLast = Constant.STATUS_ENABLE.toString();
	
					Map<String, String> theMap = new HashMap<String, String>();
					theMap.put("dealerId", dealerId);
					theMap.put("isLast", isLast);
	
					List<Map<String, Object>> theList = dao.getEnergyList(theMap);
	
					if (!CommonUtils.isNullList(theList)) {
						Map<String, Object> EneryMap = theList.get(0);
						map.put("PHONE", EneryMap.get("DLR_LINKTEL") == null ? "" : EneryMap.get("DLR_LINKTEL").toString());
						map.put("LINK_MAN", EneryMap.get("DLR_LINKMAN") == null ? "" : EneryMap.get("DLR_LINKMAN").toString());
						map.put("PROVINCE_ID", EneryMap.get("DLR_PROVICE_ID") == null ? "" : EneryMap.get("DLR_PROVICE_ID").toString());
						map.put("CITY_ID", EneryMap.get("DLR_CITY_ID") == null ? "" : EneryMap.get("DLR_CITY_ID").toString());
						map.put("TOWN_ID", EneryMap.get("DLR_TOWN_ID") == null ? "" : EneryMap.get("DLR_TOWN_ID").toString());
						map.put("DEALER_ADDRESS", EneryMap.get("DLR_ADDRESS") == null ? "" : EneryMap.get("DLR_ADDRESS").toString());
						
						String salesId = map.get("SALES_ID") == null ? "" : map.get("SALES_ID").toString() ;
						String invNo = map.get("INVOICE_NO") == null ? "" : map.get("INVOICE_NO").toString() ;
						String vehNo = map.get("VEHICLE_NO") == null ? "" : map.get("VEHICLE_NO").toString() ;
						String salesPrice = map.get("PRICE") == null ? "" : map.get("PRICE").toString() ;
						String vehId = map.get("VEHICLE_ID") == null ? "" : map.get("VEHICLE_ID").toString() ;
						String modelId = map.get("MODEL_ID") == null ? "" : map.get("MODEL_ID").toString() ;
						String salesDate = map.get("SALES_DATE") == null ? "" : map.get("SALES_DATE").toString() ;
						
						String dlrId = map.get("DEALER_ID") == null ? "" : map.get("DEALER_ID").toString() ;
						String dlrCode = map.get("DEALER_CODE") == null ? "" : map.get("DEALER_CODE").toString() ;
						String dlrTel = map.get("PHONE") == null ? "" : map.get("PHONE").toString() ;
						String dlrLinkMan = map.get("LINK_MAN") == null ? "" : map.get("LINK_MAN").toString() ;
						String dlrPro = map.get("PROVINCE_ID") == null ? "" : map.get("PROVINCE_ID").toString() ;
						String dlrCit = map.get("CITY_ID") == null ? "" : map.get("CITY_ID").toString() ;
						String dlrTow = map.get("TOWN_ID") == null ? "" : map.get("TOWN_ID").toString() ;
						String dlrAdd = map.get("DEALER_ADDRESS") == null ? "" : map.get("DEALER_ADDRESS").toString() ;
						String orgId = map.get("ORG_ID") == null ? "" : map.get("ORG_ID").toString() ;
						
						String ctmId = map.get("CTM_ID") == null ? "" : map.get("CTM_ID").toString() ;
						String ctmTel = map.get("MAIN_PHONE") == null ? "" : map.get("MAIN_PHONE").toString() ;
						String ctmLinkMan = map.get("CTM_NAME") == null ? "" : map.get("CTM_NAME").toString() ;
						String ctmPro = map.get("PROVINCE") == null ? "" : map.get("PROVINCE").toString() ;
						String ctmCit = map.get("CITY") == null ? "" : map.get("CITY").toString() ;
						String ctmTow = map.get("TOWN") == null ? "" : map.get("TOWN").toString() ;
						String ctmAdd = map.get("CTM_ADDRESS") == null ? "" : map.get("CTM_ADDRESS").toString() ;
						
						if(invNo == null || "".equals(invNo) || invNo.length() < 6) {
							throw new RuntimeException("发票号错误！") ;
						}
						
						if(vehNo == null || "".equals(vehNo)) {
							throw new RuntimeException("车牌号错误！") ;
						} else {
							if(!this.chkVhlNo(vehNo)) {
								throw new RuntimeException("车牌号已存在提报记录！") ;
							}
						}
						
						if(salesPrice == null || "".equals(salesPrice) || Long.parseLong(salesPrice) <= 0) {
							throw new RuntimeException("销售价格错误！") ;
						}
						
						if(vehId == null || "".equals(vehId)) {
							throw new RuntimeException("车辆数据错误！") ;
						}
						
						if(salesDate == null || "".equals(salesDate)) {
							throw new RuntimeException("销售时间错误！") ;
						}
						
						if(dlrTel == null || "".equals(dlrTel)) {
							throw new RuntimeException("经销商联系电话错误！") ;
						}
						
						if(dlrLinkMan == null || "".equals(dlrLinkMan)) {
							throw new RuntimeException("经销商联系人错误！") ;
						}
						
						if(dlrPro == null || "".equals(dlrPro)) {
							throw new RuntimeException("经销商省份错误！") ;
						}
						
						if(dlrCit == null || "".equals(dlrCit)) {
							throw new RuntimeException("经销商城市错误！") ;
						}
						
						if(dlrTow == null || "".equals(dlrTow)) {
							throw new RuntimeException("经销商县区错误！") ;
						}
						
						if(dlrAdd == null || "".equals(dlrAdd)) {
							throw new RuntimeException("经销商地址错误！") ;
						}
						
						if(ctmTel == null || "".equals(ctmTel)) {
							throw new RuntimeException("客户联系电话错误！") ;
						}
						
						if(ctmLinkMan == null || "".equals(ctmLinkMan)) {
							throw new RuntimeException("客户联系人错误！") ;
						}
						
						if(ctmPro == null || "".equals(ctmPro)) {
							throw new RuntimeException("客户省份错误！") ;
						}
						
						if(ctmCit == null || "".equals(ctmCit)) {
							throw new RuntimeException("客户城市错误！") ;
						}
						
						if(ctmTow == null || "".equals(ctmTow)) {
							throw new RuntimeException("客户区县错误！") ;
						}
						
						if(ctmAdd == null || "".equals(ctmAdd)) {
							throw new RuntimeException("客户地址错误！") ;
						}
						
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
						TtVsEnergyConservationPO energy = new TtVsEnergyConservationPO();
						energy.setConservationId(Utility.getLong(SequenceManager.getSequence("")));
						//生成申请编号
	//					SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMdd");
	//					Calendar   calendar = Calendar.getInstance();
	//					String timeStr = sdf2.format(calendar.getTime()).toString();
						String jNo = Utility.GetBillNo("",dlrCode,"JNO");
	//					String jNo = Utility.getString(SequenceManager.getSequence("JNO"));
						jNo = jNo.substring(jNo.length()-11);
						jNo = "JN"+dlrCode+jNo;
						
						
						
						energy.setConservationNo(jNo);
						energy.setConservationStatus(Constant.VEHICLE_ENERGY_CON_APPLY);
						energy.setCtmAddress(ctmAdd);
						energy.setCtmId(Long.valueOf(ctmId));
						energy.setCtmLinkman(ctmLinkMan);
						energy.setCtmLinktel(ctmTel);
						energy.setCtmProviceId(Long.valueOf(ctmPro));
						energy.setCtmCityId(Long.valueOf(ctmCit));
						energy.setCtmTownId(Long.valueOf(ctmTow));
						energy.setCtmZipCode(Integer.parseInt(ctmTow));
						energy.setDealerId(Long.valueOf(dlrId));
						energy.setDlrAddress(dlrAdd);
						energy.setDlrLinkman(dlrLinkMan);
						energy.setDlrProviceId(Long.valueOf(dlrPro));
						energy.setDlrCityId(Long.valueOf(dlrCit));
						energy.setDlrLinktel(dlrTel);
						energy.setDlrZipCode(Integer.valueOf(salesDate));
						energy.setExportFlag(0l);
						energy.setExportPose(0l);
						energy.setExportUser(0l);
						energy.setFactoryPrice(Double.valueOf(salesPrice));
						energy.setInvoceNo("NO" + invNo.substring(0, 6));
						energy.setIsExport(0);
						energy.setModelId(Long.valueOf(modelId));
						energy.setOrgId(Long.valueOf(orgId));
						energy.setRemark("");
						energy.setSalesDate(sdf.parse(salesDate));
						energy.setSalesId(Long.valueOf(salesId));
						energy.setSalesPrice(Double.parseDouble(salesPrice));
						energy.setStatus(Constant.STATUS_ENABLE);
						energy.setUpdateBy(logonUser.getUserId());
						energy.setUpdateDate(new Date());
						energy.setCreateBy(logonUser.getUserId());
						energy.setCreateDate(new Date());
						energy.setVehicleId(Long.valueOf(vehId));
						energy.setVehicleNo(vehNo);
						// energy.setPayDate(sdf.parse(payDate));
						energy.setPayMoney(Double.valueOf(salesPrice));
						energy.setDlrTownId(Long.valueOf(dlrTow));
						dao.insert(energy);
						
						//记录申请审核状态
						TtVsEnergyConservationChgPO chg = new TtVsEnergyConservationChgPO();
						chg.setChangeId(Utility.getLong(SequenceManager.getSequence("")));
						chg.setConservationId(energy.getConservationId());
						chg.setCreateBy(logonUser.getUserId());
						chg.setCreateDate(new Date());
						chg.setDescription("");
						chg.setStatus(Constant.VEHICLE_ENERGY_CON_APPLY);
						dao.insert(chg);
					}
				}
			}
			
			if(length > 0) {
				
			} else {
				flag = "noData" ;
			}
			
			act.setOutData("flag", flag) ;
		} catch(RuntimeException e) {
			logger.error(e) ;
			act.setException(e) ;
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "节能惠民提报");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**-------------------------------------------------------------------------------------------------**/
	public boolean chkVhlNo(String vNo) {
		List<Map<String, Object>> flagList = dao.chkVhlNo(vNo) ;
		
		if(!CommonUtils.isNullList(flagList)) {
			return false ;
		} else {
			return true ;
		}
	}
	
	//节能惠民信息经销商信息查询
	public void dealerVehicleEnergyConDownload(){
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
			map.put("dealerId", logonUser.getDealerId()) ;
			
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
			PageResult<Map<String, Object>> ps = daos.energyConExport(map, curPage, 100000);
			
			List<Map<String, Object>> orgSeriesList = ps.getRecords();
			
			if(!CommonUtils.isNullList(orgSeriesList)) {
				for(int i=0; i<orgSeriesList.size();i++){
					List<Object> listValue = new LinkedList<Object>();
					listValue.add(orgSeriesList.get(i).get("CTM_NAME") != null ? orgSeriesList.get(i).get("CTM_NAME") : "");
					listValue.add(orgSeriesList.get(i).get("CTM_LINKMAN") != null ? orgSeriesList.get(i).get("CTM_LINKMAN") : "");
					listValue.add(orgSeriesList.get(i).get("CTM_LINKTEL") != null ? orgSeriesList.get(i).get("CTM_LINKTEL") : "");
					listValue.add(orgSeriesList.get(i).get("CTM_ADDRESS") != null ? orgSeriesList.get(i).get("CTM_ADDRESS") : "");
					listValue.add(orgSeriesList.get(i).get("CTM_TOWN_ID") != null ? orgSeriesList.get(i).get("CTM_TOWN_ID") : "");
					listValue.add(orgSeriesList.get(i).get("CTM_TOWN_NAME") != null ? orgSeriesList.get(i).get("CTM_TOWN_NAME") : "");
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
					listValue.add(orgSeriesList.get(i).get("DLR_TOWN_NAME") != null ? orgSeriesList.get(i).get("DLR_TOWN_NAME") : "");
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
			EnergyConservationOem eco=new EnergyConservationOem();
			
			eco.createXlsFile(list, os);
			
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
