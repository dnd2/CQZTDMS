package com.infodms.dms.actions.sales.storage.sendmanage;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import com.infodms.dms.dao.common.AjaxSelectDao;
import com.infodms.dms.dao.sales.storage.sendManage.SendAssignmentChangeDao;
import com.infodms.dms.dao.sales.storage.storagebase.LogisticsDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TtSalesAssignPO;
import com.infodms.dms.po.TtVsDispatchOrderDtlPO;
import com.infodms.dms.po.TtVsDispatchOrderPO;
import com.infodms.dms.po.TtVsDlvryDtlPO;
import com.infodms.dms.po.TtVsDlvryPO;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;

/**
 * 
/**
 * 
 * @ClassName     : SendAssignmentChange
 * @Description   : 发送分派更改管理
 * @author        : ranjian
 * CreateDate     : 2013-4-18
 */
public class SendAssignmentChange {
	public Logger logger = Logger.getLogger(SgmDealerSysUser.class);
	private ActionContext act = ActionContext.getContext();
	RequestWrapper request = act.getRequest();
	private final SendAssignmentChangeDao reDao = SendAssignmentChangeDao.getInstance();
	private final LogisticsDao reLDao = LogisticsDao.getInstance();
	private final String sendAssignmentChangeInitUtl = "/jsp/sales/storage/sendmanage/sendAssignmentChange/sendAssignmentChangeList.jsp";
	private final String cancelSendQueryUrl = "/jsp/sales/storage/sendmanage/sendAssignmentChange/sendCancelQuery.jsp";
	/**
	 * 
	 * @Title      : 
	 * @Description: 发送分派更改初始化
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-18
	 */
	public void sendAssignmentChangeInit(){
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
			act.setForword(sendAssignmentChangeInitUtl);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE,
					"发送分派更改初始化");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 
	 * @Title      : 
	 * @Description: 发送分派更改查询信息
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-18
	 */
	public void sendAssignmentChangeQuery() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			/******************************页面查询字段start**************************/
			
			String common = CommonUtils.checkNull(request.getParamValue("common")); // 类型
			String yieldly = CommonUtils.checkNull(request.getParamValue("YIELDLY")); // 产地
			String dealerCode = CommonUtils.checkNull(request.getParamValue("dealerCode")); // 经销商ID
			String dlvOrdType = CommonUtils.checkNull(request.getParamValue("dlvOrdType")); //单据来源
			String reqNO = CommonUtils.checkNull(request.getParamValue("reqNO")); //申请单号
			String dlvStartDate = CommonUtils.checkNull(request.getParamValue("DLV_START_DATE")); //最晚发运开始日期
			String dlvEndDate = CommonUtils.checkNull(request.getParamValue("DLV_END_DATE")); //最晚发运结束日期
			String arrStartDate = CommonUtils.checkNull(request.getParamValue("ARR_START_DATE")); //最晚到货开始日期
			String arrEndDate = CommonUtils.checkNull(request.getParamValue("ARR_END_DATE")); //最晚到货结束日期
			String assStartdate = CommonUtils.checkNull(request.getParamValue("ASS_STARTDATE")); // 分派日期开始
			String assEnddate = CommonUtils.checkNull(request.getParamValue("ASS_ENDDATE")); //分派日期结束
			String logiName = CommonUtils.checkNull(request.getParamValue("LOGI_NAME_SEACH")); //物流商
			//String isMiddleTurn = CommonUtils.checkNull(request.getParamValue("isMiddleTurn")); //是否中转
			String isSdan = CommonUtils.checkNull(request.getParamValue("isSdan")); //是否散单
			
			/******************************页面查询字段end***************************/
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("yieldly", yieldly);
			map.put("dealerCode", dealerCode);
			map.put("dlvOrdType", dlvOrdType);
			map.put("reqNO", reqNO);
			map.put("dlvStartDate", dlvStartDate);
			map.put("dlvEndDate", dlvEndDate);
			map.put("arrStartDate", arrStartDate);
			map.put("arrEndDate", arrEndDate);
			map.put("assStartDate", assStartdate);
			map.put("assEndDate", assEnddate);
			map.put("logiName", logiName);
			//map.put("isMiddleTurn", isMiddleTurn);
			map.put("isSdan", isSdan);
			map.put("poseId", logonUser.getPoseId().toString());
			if("1".equals(common)){//统计 调用
				Map<String, Object> valueMap = reDao.tgSum(map);
				act.setOutData("valueMap", valueMap);	
			}else if("2".equals(common)){//导出 调用
				List<Map<String, Object>> mapList = reDao.getSendAssignmentChangeExport(map);
				String [] head={};
				String [] cols={};
				ToExcel.toReportExcel(act.getResponse(),request, "发运分派更改信息.xls",head,cols,mapList);
			}else{
				Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request
						.getParamValue("curPage")) : 1;
				PageResult<Map<String, Object>> ps = reDao.getSendAssignmentChangeQuery(map, curPage,
						Constant.PAGE_SIZE);
				act.setOutData("ps", ps);
			}
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "发运分派更改信息查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 
	 * @Title      : 
	 * @Description: 确定发运更改
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-18
	 */
	public void  sendAssignmentChangeMain(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String[] groupIds=request.getParamValues("groupIds");//获取分派ID
			String[] logiNames=request.getParamValues("LOGI_NAME");//获取物流商IDS
			String[] planDate = request.getParamValues("PLAN_DATE");//计划装车时间
			String[] asRemark = request.getParamValues("AS_REMARK");//说明
			String[] hiddenIds=request.getParamValues("hiddenIds");//隐藏复选框所有值
			String remark=request.getParamValue("REMARK");//备注
			
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//格式化时间
			
			if(groupIds!=null && groupIds.length>0){
				for(int i=0;i<groupIds.length;i++){//循环选中的多选框
					int index=0;
					for(int j=0;j<hiddenIds.length;j++){//循环所有的多选框
					if(groupIds[i].equals(hiddenIds[j])){//如果相等 可以取出对应一行的值
							index=j;//获取对应行的下标
						}
					}
					//修改分派表
					TtSalesAssignPO tspo=new TtSalesAssignPO();
					tspo.setLogiId(Long.parseLong(logiNames[index]));//选中的物流商ID
					if(planDate[index] != null && !planDate[index].equals(""))
					{
						Date date = df.parse(planDate[index]);
						tspo.setPlanDate(date);//计划装车时间
					}
					if(asRemark[index] != null && !asRemark[index].equals(""))
					{
						tspo.setAsRemark(asRemark[index]);//说明
					}
					tspo.setUpdateBy(logonUser.getUserId());
					tspo.setUpdateDate(CommonUtils.parseDateTime(AjaxSelectDao.getInstance().getCurrentServerTime()));	
					tspo.setAssRemark(remark);//备注
					TtSalesAssignPO seachPO=new TtSalesAssignPO();
					seachPO.setAssId(Long.parseLong(groupIds[i]));//修改分派表数据
					reDao.updateAssignment(seachPO,tspo);
				}
				act.setOutData("returnValue", 1);//成功
			}else{
				act.setOutData("returnValue", 2);//错误提示
			}	
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "发运分派更改信息确认");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 取消分派
	 */
	public void  cancelAssignmentDo(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String[] groupIds=request.getParamValues("groupIds");//获取订单号
			String remark=request.getParamValue("REMARK");//备注
			
			if(groupIds!=null && groupIds.length>0){
				for(int i=0;i<groupIds.length;i++){//循环选中的多选框
					
					String reqId=groupIds[i];
					TtVsDlvryPO tvp=new TtVsDlvryPO();
					tvp.setReqId(Long.parseLong(reqId));
					TtVsDlvryPO tvp2=new TtVsDlvryPO();
					tvp2.setDlvFpTotal(0);//分派数量清零
					tvp2.setDlvStatus(Constant.ORDER_STATUS_14);//分派驳回
					tvp2.setUpdateBy(logonUser.getUserId());
					tvp2.setUpdateDate(new Date());
					//审核日期
					tvp2.setAuditDate(new Date());
					//审核人
					tvp2.setAuditBy(logonUser.getUserId());
					//审核备注
					tvp2.setAuditRemark(remark);
					reDao.update(tvp, tvp2);

				}
				act.setOutData("returnValue", 1);//成功
			}else{
				act.setOutData("returnValue", 2);//错误提示
			}	
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "发运分派信息确认");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 发运分派取消初始化
	 */
	public void cancelAssignmentInit(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		String areaIds = MaterialGroupManagerDao
		.getPoseIdBusinessIdStr(logonUser.getPoseId().toString());
		try {
			String poseId=logonUser.getPoseId().toString();
			String poseBusType=logonUser.getPoseBusType().toString();
			List<Map<String, Object>> list_yieldly=MaterialGroupManagerDao.getWarehouseList(poseId,poseBusType);
			act.setOutData("list", list_yieldly);//仓库LIST
			List<Map<String, Object>> list_logi=new ArrayList<Map<String, Object>>();
			if (logonUser.getPoseBusType().intValue() == Constant.POSE_BUS_TYPE_WL)
			{
				list_logi=reLDao.getLogiByPoseId(areaIds,logonUser);
			}else{
				list_logi=reLDao.getLogiByArea(areaIds);
			}
			act.setOutData("logi", list_logi);//物流商LIST
			if(list_logi != null && list_logi.size() > 0)
			{
				Map<String, Object> obj = list_logi.get(0);
				act.setOutData("LOGI_NAME", obj.get("LOGI_NAME"));
			}
			act.setForword(cancelSendQueryUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "发送分派取消初始化");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 发运分派取消查询
	 */
	public void sendCancelQueryList()
	{
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			/******************************页面查询字段start**************************/
			String whId = CommonUtils.checkNull(request.getParamValue("whId")); // 发运仓库
			String logiId = CommonUtils.checkNull(request.getParamValue("logiId")); // 承运商
			String dealerCode = CommonUtils.checkNull(request.getParamValue("dealerCode")); // 经销商ID
			String reqNo = CommonUtils.checkNull(request.getParamValue("reqNo")); // 申请单号
			String transType = CommonUtils.checkNull(request.getParamValue("transType")); // 申请发运方式
			String dlvIsSd = CommonUtils.checkNull(request.getParamValue("dlvIsSd")); // 是否散单
			String startDate = CommonUtils.checkNull(request.getParamValue("START_DATE")); // 申请日期开始
			String endDate = CommonUtils.checkNull(request.getParamValue("END_DATE")); // 申请日期结束
			String fpStartDate = CommonUtils.checkNull(request.getParamValue("FP_START_DATE")); // 分派日期开始
			String fpEndDate = CommonUtils.checkNull(request.getParamValue("FP_END_DATE")); // 分派日期结束
			String dlvType = CommonUtils.checkNull(request.getParamValue("DLV_TYPE")); // 分派日期结束
			String common = CommonUtils.checkNull(request.getParamValue("common")); // 查询类型
			/******************************页面查询字段end***************************/
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("whId", whId);
			map.put("dealerCode", dealerCode);
			map.put("reqNo", reqNo);
			map.put("startDate", startDate);
			map.put("endDate", endDate);
			map.put("logiId", logiId);
			map.put("transType", transType);
			map.put("fpStartDate", fpStartDate);
			map.put("fpEndDate", fpEndDate);
			map.put("dlvType", dlvType);
			map.put("dlvIsSd", dlvIsSd);
			
			map.put("poseId", logonUser.getPoseId().toString());
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request
					.getParamValue("curPage")) : 1;
			PageResult<Map<String, Object>> ps = reDao.getSendCancelQuery(map, curPage,
					Constant.PAGE_SIZE);
			act.setOutData("ps", ps);
			
			
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "发运分派取消信息查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 分派取消操作
	 */
	public void  cancelAssignmentConfirm(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String[] groupIds=request.getParamValues("groupIds");//获取订单号
			String[] hiddenIds=request.getParamValues("hiddenIds");//隐藏复选框所有值
			String[] dlvTypes=request.getParamValues("dlvType");//发运类型（订单或调拨）
			String[] ordIds=request.getParamValues("ordId");//订单ID
			if(groupIds!=null && groupIds.length>0){
				for(int i=0;i<groupIds.length;i++){//循环选中的多选框
					int index=0;
					for(int j=0;j<hiddenIds.length;j++){//循环所有的多选框
					if(groupIds[i].equals(hiddenIds[j])){//如果相等 可以取出对应一行的值
							index=j;//获取对应行的下标
							break;
						}
					}
					String reqId=groupIds[i];
					String dlvType=dlvTypes[index];
					String ordId=ordIds[index];
					/**
					 * 1、向分派取消日志主表和明细表插入数据
					 * 2、判断发运类型是否是调拨单，若是，更新调拨单主表的分派数量为0(调拨明细表暂无字段)
					 * 3、更新发运主表的状态为未分派、分派数量和明细表的分派数量
					 */
					reDao.insert(insertTsiAssignMainPo(reqId,logonUser.getUserId()));
					reDao.insert(insertTsiAssignDtlPo(reqId,logonUser.getUserId()));
					//判断发运类型是否是调拨单，若是，更新调拨单主表的分派数量为0
					if(dlvType.equals(Constant.DELIVERY_ORD_TYPE_ALLOCAT.toString())){
						//更新调拨单主表
						TtVsDispatchOrderPO tvp=new TtVsDispatchOrderPO();
						tvp.setDispId(Long.parseLong(ordId));
						TtVsDispatchOrderPO tvp2=new TtVsDispatchOrderPO();
						tvp2.setFpNum(0);
						tvp2.setUpdateBy(logonUser.getUserId());
						tvp2.setUpdateDate(new Date());
						reDao.update(tvp, tvp2);
					}
					//更新发运分派明细表数据
					TtVsDlvryDtlPO tdpo=new TtVsDlvryDtlPO();
					tdpo.setReqId(Long.parseLong(reqId));
					TtVsDlvryDtlPO tdpo2=new TtVsDlvryDtlPO();
					tdpo2.setFpTotal(0);
					tdpo2.setUpdateBy(logonUser.getUserId());
					tdpo2.setUpdateDate(new Date());
					reDao.update(tdpo, tdpo2);
					//更新发运分派主表数据,还原分派时修改信息（发运仓库、收货仓库、结算省市县、是否中转、中转省市县、发运方式、是否散单、承运商）
					
					TtVsDlvryPO tvo=new TtVsDlvryPO();
					tvo.setReqId(Long.parseLong(reqId));
					tvo=(TtVsDlvryPO) reDao.select(tvo).get(0);
					
					TtVsDlvryPO tpo=new TtVsDlvryPO();
					tpo.setReqId(Long.parseLong(reqId));
					TtVsDlvryPO tpo2=new TtVsDlvryPO();
					tpo2.setDlvFpTotal(0);
					tpo2.setDlvStatus(Constant.ORDER_STATUS_01);//未分派
					tpo2.setUpdateBy(logonUser.getUserId());
					tpo2.setUpdateDate(new Date());
					tpo2.setDlvWhId(tvo.getReqWhId());//发运仓库
					tpo2.setDlvRecWhId(tvo.getReqRecWhId());//收货仓库
					tpo2.setDlvBalProvId(tvo.getReqRecProvId());//结算省份
					tpo2.setDlvBalCityId(tvo.getReqRecCityId());//结算城市
					tpo2.setDlvBalCountyId(tvo.getReqRecCountyId());//结算区县
					//tpo2.setDlvIsZz(Constant.IF_TYPE_NO);//是否中转为否
					//中转省市县置空
//					tpo2.setDlvZzProvId(Long.valueOf(0));
//					tpo2.setDlvZzCityId(Long.valueOf(0));
//					tpo2.setDlvZzCountyId(Long.valueOf(0));
					tpo2.setDlvShipType(tvo.getReqShipType());//发运方式
					tpo2.setDlvIsSd(Constant.IF_TYPE_NO);//是否散单为否
					tpo2.setDlvLogiId(Long.valueOf(0));//承运商置空
					reDao.update(tpo, tpo2);
				}
				act.setOutData("returnValue", 1);//成功
			}else{
				act.setOutData("returnValue", 2);//错误提示
			}	
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "发运分派取消");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 向分派取消日志主表写入数据
	 * @param boId
	 * @param userId
	 * @return
	 */
	public String insertTsiAssignMainPo(String reqId,long userId){
		StringBuffer sql= new StringBuffer();
		sql.append("insert into TSI_TT_VS_DLVRY\n" );
		sql.append("  (REQ_ID,\n" );
		sql.append("   REQ_TOTAL,\n" );
		sql.append("   ORD_ID,\n" );
		sql.append("   ORD_TOTAL,\n" );
		sql.append("   CREATE_BY,\n" );
		sql.append("   CREATE_DATE,\n" );
		sql.append("   VER,\n" );
		sql.append("   REQ_REC_COUNTY_ID,\n" );
		sql.append("   REQ_REC_ADDR,\n" );
		sql.append("   REQ_LINK_MAN,\n" );
		sql.append("   REQ_TEL,\n" );
		sql.append("   REQ_WH_ID,\n" );
		sql.append("   REQ_REMARK,\n" );
		sql.append("   ORD_NO,\n" );
		sql.append("   REQ_NO,\n" );
		sql.append("   REQ_SHIP_TYPE,\n" );
		sql.append("   REQ_DATE,\n" );
		sql.append("   REQ_REC_DEALER_ID,\n" );
		sql.append("   REQ_REC_ADDR_ID,\n" );
		sql.append("   REQ_REC_PROV_ID,\n" );
		sql.append("   REQ_REC_CITY_ID,\n" );
		sql.append("   ORD_PUR_DEALER_ID,\n" );
		sql.append("   DLV_TYPE,\n" );
		sql.append("   DLV_SHIP_TYPE,\n" );
		sql.append("   DLV_STATUS,\n" );
		sql.append("   DLV_FP_TOTAL,\n" );
		sql.append("   DLV_BD_TOTAL,\n" );
		sql.append("   DLV_FY_TOTAL,\n" );
		sql.append("   DLV_JJ_TOTAL,\n" );
		sql.append("   DLV_YS_TOTAL,\n" );
		sql.append("   DLV_FY_DATE,\n" );
		sql.append("   DLV_JJ_DATE,\n" );
		sql.append("   DLV_WH_ID,\n" );
		sql.append("   DLV_LOGI_ID,\n" );
		sql.append("   DLV_BAL_PROV_ID,\n" );
		sql.append("   DLV_BAL_CITY_ID,\n" );
		sql.append("   DLV_BAL_COUNTY_ID,\n" );
//		sql.append("   DLV_IS_ZZ,\n" );
//		sql.append("   DLV_ZZ_PROV_ID,\n" );
//		sql.append("   DLV_ZZ_CITY_ID,\n" );
//		sql.append("   DLV_ZZ_COUNTY_ID,\n" );
		sql.append("   REQ_REC_WH_ID,\n" );
		sql.append("   DLV_DATE,\n" );
		sql.append("   DLV_IS_SD,\n" );
		sql.append("   DLV_BY,\n" );
		sql.append("   DLV_REMARK,\n" );
		sql.append("   DLV_REC_WH_ID,\n" );
		sql.append("   AUDIT_BY,\n" );
		sql.append("   AUDIT_REMARK,\n" );
		sql.append("   AUDIT_DATE)\n" );
		sql.append("  SELECT REQ_ID,\n" );
		sql.append("         REQ_TOTAL,\n" );
		sql.append("         ORD_ID,\n" );
		sql.append("         ORD_TOTAL,\n" );
		sql.append("         '"+userId+"',\n" );
		sql.append("         SYSDATE,\n" );
		sql.append("         VER,\n" );
		sql.append("         REQ_REC_COUNTY_ID,\n" );
		sql.append("         REQ_REC_ADDR,\n" );
		sql.append("         REQ_LINK_MAN,\n" );
		sql.append("         REQ_TEL,\n" );
		sql.append("         REQ_WH_ID,\n" );
		sql.append("         REQ_REMARK,\n" );
		sql.append("         ORD_NO,\n" );
		sql.append("         REQ_NO,\n" );
		sql.append("         REQ_SHIP_TYPE,\n" );
		sql.append("         REQ_DATE,\n" );
		sql.append("         REQ_REC_DEALER_ID,\n" );
		sql.append("         REQ_REC_ADDR_ID,\n" );
		sql.append("         REQ_REC_PROV_ID,\n" );
		sql.append("         REQ_REC_CITY_ID,\n" );
		sql.append("         ORD_PUR_DEALER_ID,\n" );
		sql.append("         DLV_TYPE,\n" );
		sql.append("         DLV_SHIP_TYPE,\n" );
		sql.append("         DLV_STATUS,\n" );
		sql.append("         DLV_FP_TOTAL,\n" );
		sql.append("         DLV_BD_TOTAL,\n" );
		sql.append("         DLV_FY_TOTAL,\n" );
		sql.append("         DLV_JJ_TOTAL,\n" );
		sql.append("         DLV_YS_TOTAL,\n" );
		sql.append("         DLV_FY_DATE,\n" );
		sql.append("         DLV_JJ_DATE,\n" );
		sql.append("         DLV_WH_ID,\n" );
		sql.append("         DLV_LOGI_ID,\n" );
		sql.append("         DLV_BAL_PROV_ID,\n" );
		sql.append("         DLV_BAL_CITY_ID,\n" );
		sql.append("         DLV_BAL_COUNTY_ID,\n" );
//		sql.append("         DLV_IS_ZZ,\n" );
//		sql.append("         DLV_ZZ_PROV_ID,\n" );
//		sql.append("         DLV_ZZ_CITY_ID,\n" );
//		sql.append("         DLV_ZZ_COUNTY_ID,\n" );
		sql.append("         REQ_REC_WH_ID,\n" );
		sql.append("         DLV_DATE,\n" );
		sql.append("         DLV_IS_SD,\n" );
		sql.append("         DLV_BY,\n" );
		sql.append("         DLV_REMARK,\n" );
		sql.append("         DLV_REC_WH_ID,\n" );
		sql.append("         AUDIT_BY,\n" );
		sql.append("         AUDIT_REMARK,\n" );
		sql.append("         AUDIT_DATE\n" );
		sql.append("    FROM TT_VS_DLVRY TVD\n" );
		sql.append("   WHERE TVD.REQ_ID = '"+reqId+"'");
		return sql.toString();
	}
	/**
	 * 向分派取消日志明细表写入数据
	 * @param boId
	 * @param userId
	 * @return
	 */
	public String insertTsiAssignDtlPo(String reqId,long userId){
		StringBuffer sql= new StringBuffer();
		sql.append("insert into TSI_TT_VS_DLVRY_DTL\n" );
		sql.append("  (REQ_DETAIL_ID,\n" );
		sql.append("   REQ_ID,\n" );
		sql.append("   ORD_ID,\n" );
		sql.append("   ORD_DETAIL_ID,\n" );
		sql.append("   MATERIAL_ID,\n" );
		sql.append("   ORD_TOTAL,\n" );
		sql.append("   REQ_TOTAL,\n" );
		sql.append("   FP_TOTAL,\n" );
		sql.append("   BD_TOTAL,\n" );
		sql.append("   FY_TOTAL,\n" );
		sql.append("   JJ_TOTAL,\n" );
		sql.append("   YS_TOTAL,\n" );
		sql.append("   CREATE_BY,\n" );
		sql.append("   CREATE_DATE,\n" );
		sql.append("   VER)\n" );
		sql.append("  select REQ_DETAIL_ID,\n" );
		sql.append("         REQ_ID,\n" );
		sql.append("         ORD_ID,\n" );
		sql.append("         ORD_DETAIL_ID,\n" );
		sql.append("         MATERIAL_ID,\n" );
		sql.append("         ORD_TOTAL,\n" );
		sql.append("         REQ_TOTAL,\n" );
		sql.append("         FP_TOTAL,\n" );
		sql.append("         BD_TOTAL,\n" );
		sql.append("         FY_TOTAL,\n" );
		sql.append("         JJ_TOTAL,\n" );
		sql.append("         YS_TOTAL,\n" );
		sql.append("         '"+userId+"',\n" );
		sql.append("         sysdate,\n" );
		sql.append("         VER\n" );
		sql.append("    from tt_vs_dlvry_dtl t\n" );
		sql.append("   where t.req_id = '"+reqId+"'");
		return sql.toString();
	}
}
