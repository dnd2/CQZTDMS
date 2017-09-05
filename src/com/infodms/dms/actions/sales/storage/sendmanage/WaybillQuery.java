package com.infodms.dms.actions.sales.storage.sendmanage;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.actions.claim.basicData.ToExcel;
import com.infodms.dms.actions.sysmng.usemng.SgmDealerSysUser;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.materialManager.MaterialGroupManagerDao;
import com.infodms.dms.dao.sales.storage.sendManage.WaybillPrintDao;
import com.infodms.dms.dao.sales.storage.storagebase.LogisticsDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TtSalesCityDisPO;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

public class WaybillQuery {
	public Logger logger = Logger.getLogger(SgmDealerSysUser.class);
	private ActionContext act = ActionContext.getContext();
	RequestWrapper request = act.getRequest();
	private final WaybillPrintDao wpDao = WaybillPrintDao.getInstance();
	private final LogisticsDao reLDao = LogisticsDao.getInstance();
	private final String WaybillQuertInitUtl = "/jsp/sales/storage/sendmanage/WaybillPrint/waybillQueryInit.jsp";
	private final String WaybillQueryUtl = "/jsp/sales/storage/sendmanage/WaybillPrint/queryWaybill.jsp";//运单查看

  
	/**
	 * 
	 * @Title      : 
	 * @Description: 运单查询初始化
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-7-18
	 */
	public void waybillQueryInit(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		String areaIds = MaterialGroupManagerDao
		.getPoseIdBusinessIdStr(logonUser.getPoseId().toString());

		try {
			List<Map<String, Object>> list_yieldly=MaterialGroupManagerDao.getPoseIdBusiness(logonUser.getPoseId().toString());
			act.setOutData("list", list_yieldly);//产地LIST
			List<Map<String, Object>> list_logi=new ArrayList<Map<String, Object>>();
			if (logonUser.getPoseBusType().intValue() == Constant.POSE_BUS_TYPE_WL)
			{
				list_logi=reLDao.getLogiByPoseId(areaIds,logonUser);
			}else{
				list_logi=reLDao.getLogiByArea(areaIds);
			}
			act.setOutData("list_logi", list_logi);//物流商LIST

			act.setForword(WaybillQuertInitUtl);

		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE,
					"运单初始化");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 
	 * @Title      : 
	 * @Description: 运单查看查询
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-5-20
	 */
	public void wayBillQuery(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);


		try {
			/******************************页面查询字段start**************************/
			String dealerCode = CommonUtils.checkNull(request.getParamValue("dealerCode")); // 经销商ID
			String orderNo = CommonUtils.checkNull(request.getParamValue("ORDER_NO")); //运单号
			String sendStartDate = CommonUtils.checkNull(request.getParamValue("sendStartDate"));
			String sendEndDate = CommonUtils.checkNull(request.getParamValue("sendEndDate"));
			String address = CommonUtils.checkNull(request.getParamValue("address"));
			String countyId = CommonUtils.checkNull(request.getParamValue("COUNTY_ID")); // 区县
			String cityId = CommonUtils.checkNull(request.getParamValue("CITY_ID")); // 地市
			String provinceId = CommonUtils.checkNull(request.getParamValue("PROVINCE")); // 省份
			String areaId = CommonUtils.checkNull(request.getParamValue("YIELDLY")); // 产地
			String logiName = CommonUtils.checkNull(request.getParamValue("LOGI_NAME")); // 物流商
			String common = CommonUtils.checkNull(request.getParamValue("common")); // 类型
			String VIN = CommonUtils.checkNull(request.getParamValue("VIN")); // VIN
			String isConfirm = CommonUtils.checkNull(request.getParamValue("isConfirm"));//运单确认状态
			String confirmBeginDate = CommonUtils.checkNull(request.getParamValue("confirmBeginDate"));
			String confirmEndDate = CommonUtils.checkNull(request.getParamValue("confirmEndDate"));
			/******************************页面查询字段end***************************/
			Map<String, Object> map = new HashMap<String, Object>();

			map.put("dealerCode", dealerCode);
			map.put("orderNo", orderNo);
			map.put("poseId", logonUser.getPoseId().toString());
			map.put("sendStartDate", sendStartDate);
			map.put("sendEndDate", sendEndDate);
			map.put("address", address);
			map.put("countyId", countyId);
			map.put("CITY_ID", cityId);
			map.put("PROVINCE_ID", provinceId);
			map.put("AREA_ID", areaId);
			map.put("logiName", logiName);
			map.put("VIN", VIN);
			map.put("isConfirm", isConfirm);
			map.put("confirmBeginDate", confirmBeginDate);
			map.put("confirmEndDate", confirmEndDate);
			if("1".equals(common)){//统计 调用
				Map<String, Object> valueMap = wpDao.tgSum(map);
				act.setOutData("valueMap", valueMap);	
			}else if("2".equals(common)){//导出 调用
				List<Map<String, Object>> mapList = wpDao.getWaybillListExport(map);
				String [] head={"运单号","车系","发运经销商名称","物流公司","发运省份","发运城市","发运地区","车队","车牌","运单数量","运单时间","里程数","运费"};
				String [] cols={"BILL_NO","SER_NAME","DEALER_NAME","LOGI_NAME","SEND_CITY","SEND_CITY1","SEND_CITY2","CAR_TEAM","CAR_NO","VEH_NUM","BILL_CRT_DATE","DISTANCE","CG_AMOUNT"};
				ToExcel.toReportExcel(act.getResponse(),request, "运单详细信息.xls",head,cols,mapList);
			}else{
				Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request
						.getParamValue("curPage")) : 1;
				PageResult<Map<String, Object>> ps =wpDao.getWaybillList(map, curPage, Constant.PAGE_SIZE);
				act.setOutData("ps", ps);
			}
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "运单查看查询信息");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
		
	}
	/**
	 * 
	 * @Title      : 
	 * @Description: 运单查看
	 * @param      :       
	 * @return     :    
	 * @throws     : 
	 * LastDate    : 2013-5-30 ranjian
	 */
	public void waybillDetailQuery(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String wayBillId=CommonUtils.checkNullNum(request.getParamValue("wayBillId"));	
			String ser_id=CommonUtils.checkNullNum(request.getParamValue("ser_id"));	
			Map<String,Object> valueMap=wpDao.billPrintMsg(Long.parseLong(wayBillId),ser_id);
			if(valueMap==null){
				valueMap =new HashMap<String, Object>();
			}
			List<Map<String,Object>> valueList=wpDao.billPrintMainMsg(Long.parseLong(wayBillId),ser_id);
			int veh_num=valueList==null?0:valueList.size();//数量【特殊处理字段】
			valueMap.put("VEH_NUM_01", veh_num);
			//计算运单到达日期
			Date createDate = (Date)valueMap.get("CREATE_DATE"); //发运单创建时间
			BigDecimal areaId = (BigDecimal)valueMap.get("AREA_ID");
			BigDecimal cityId = (BigDecimal)valueMap.get("CITY_ID");
			 TtSalesCityDisPO po = new TtSalesCityDisPO();
			 po.setYieldly(areaId!=null?areaId.longValue():0L);
			 po.setCityId(cityId!=null?cityId.longValue():0L);
			 List<PO> list = wpDao.select(po);
			 if(list != null && list.size()>0){
				 //根据发运单创建时间和城市里程数中到达天数，得到该发运单预计到达时间
				 TtSalesCityDisPO po_1 = (TtSalesCityDisPO)list.get(0); //城市里程
				 Calendar c = Calendar.getInstance();
				 c.setTimeInMillis(createDate.getTime());
				 c.add(Calendar.DATE,po_1.getArriveDays());
				 Date d=  new Date(c.getTimeInMillis());
				 String expectArriveDate = sdf.format(d);
				 act.setOutData("expectArriveDate", expectArriveDate);
			 }
			 
			act.setOutData("valueMap", valueMap);
			act.setOutData("valueList", valueList);
			act.setForword(WaybillQueryUtl);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE,
					"运单查看");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

}
