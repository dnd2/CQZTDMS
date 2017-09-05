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
import com.infodms.dms.dao.common.AjaxSelectDao;
import com.infodms.dms.dao.sales.storage.StorageUtil;
import com.infodms.dms.dao.sales.storage.sendManage.DlvPlanManageDao;
import com.infodms.dms.dao.sales.storage.sendManage.SendBoardDao;
import com.infodms.dms.dao.sales.storage.sendManage.SendBoardSeachDao;
import com.infodms.dms.dao.sales.storage.sendManage.SendDriverSeachDao;
import com.infodms.dms.dao.sales.storage.storagebase.LogisticsDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TtSalesBoardPO;
import com.infodms.dms.po.TtSalesCityDisPO;
import com.infodms.dms.po.TtVsDispatchOrderPO;
import com.infodms.dms.po.TtVsDlvryPO;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

/**
 * 发运计划管理
 * @author shuyh
 * 2017/8/1
 */
public class DlvPlanManage {
	public Logger logger = Logger.getLogger(DlvPlanManage.class);
	private ActionContext act = ActionContext.getContext();
	RequestWrapper request = act.getRequest();
	private final DlvPlanManageDao reDao = DlvPlanManageDao.getInstance();
	private final LogisticsDao reLDao = LogisticsDao.getInstance();
	private final SendBoardSeachDao ssDao = SendBoardSeachDao.getInstance();
	private final SendBoardDao sbDao = SendBoardDao.getInstance();
	private final String dlvPlanManageInitUtl = "/jsp/sales/storage/sendmanage/dlvPlan/dlvPlanManageList.jsp";
	private final String seachInitUtl = "/jsp/sales/storage/sendmanage/dlvPlan/dlvPlanManageDel.jsp";
	private final String dlvPlanQueryInitUtl = "/jsp/sales/storage/sendmanage/dlvPlan/dlvPlanQueryList.jsp";
	private final String dlvbillPrintUtl = "/jsp/sales/storage/sendmanage/dlvPlan/printDlvbill.jsp";//运单打印

	/**
	 * 发运计划发送初始化
	 */
	public void planManageInit(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		String areaIds = MaterialGroupManagerDao
		.getPoseIdBusinessIdStr(logonUser.getPoseId().toString());
		try {
			String poseId=logonUser.getPoseId().toString();
			String poseBusType=logonUser.getPoseBusType().toString();
			List<Map<String, Object>> list_yieldly=reLDao.getLogiByWarehouse(poseId,poseBusType);
			act.setOutData("list", list_yieldly);//产地LIST
			
			List<Map<String, Object>> list_logi=new ArrayList<Map<String, Object>>();
			if (logonUser.getPoseBusType().intValue() == Constant.POSE_BUS_TYPE_WL)
			{
				list_logi=reLDao.getLogiByPoseId(areaIds,logonUser);
			}else{
				list_logi=reLDao.getLogiByArea(areaIds);
			}
			act.setOutData("list_logi", list_logi);//物流商LIST
			act.setForword(dlvPlanManageInitUtl);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE,
					"发运计划发送初始化");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 发运计划发送查询
	 */
	public void DlvPlanManageQuery() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			/******************************页面查询字段start**************************/
			String raiseStartDate = CommonUtils.checkNull(request.getParamValue("START_DATE")); // 组板日期开始
			String raiseEndDate = CommonUtils.checkNull(request.getParamValue("END_DATE")); // 组板日期结束
			String boNo = CommonUtils.checkNull(request.getParamValue("BO_NO")); // 组板编号
			String logiName = CommonUtils.checkNull(request.getParamValue("LOGI_NAME")); //物流商
			String transportType = CommonUtils.checkNull(request.getParamValue("TRANSPORT_TYPE")); // 发运方式
			String provinceId = CommonUtils.checkNull(request.getParamValue("jsProvince")); //结算省份
			String cityId = CommonUtils.checkNull(request.getParamValue("jsCity")); // 结算城市
			String countyId = CommonUtils.checkNull(request.getParamValue("jsCounty")); // 结算区县
			String isBill = CommonUtils.checkNull(request.getParamValue("isBill")); //是否已生成交接单
			
			String pFlag = CommonUtils.checkNull(request.getParamValue("pFlag")); // 不为空表示发运计划查询
			String common= CommonUtils.checkNull(request.getParamValue("common"));//1表示导出
			/******************************页面查询字段end***************************/
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("raiseStartDate", raiseStartDate);
			map.put("raiseEndDate", raiseEndDate);
			map.put("boNo", boNo);
			map.put("logiName", logiName);
			map.put("poseId", logonUser.getPoseId().toString());
			map.put("transportType", transportType);
			map.put("provinceId", provinceId);
			map.put("cityId", cityId);
			map.put("countyId", countyId);
			map.put("isBill", isBill);
			map.put("pFlag", pFlag);
			//根据职位ID获取是否属于物流商以及物流商ID
			Map<String, Object> pmap=sbDao.getPoseLogiById(logonUser.getPoseId().toString());
			map.put("posBusType", pmap.get("POSE_BUS_TYPE").toString());
			map.put("logiId", (BigDecimal)pmap.get("LOGI_ID"));
			if("1".equals(common)){//导出 调用
				List<Map<String, Object>> list = reDao.getDlvPlanQueryExport(map);
				String [] head={"计划装车日期","最晚发运日期","最晚到货日期","组板号","发运方式","承运商","发运结算地","组板申请日期","组板量"};
				String [] cols={"PLAN_LOAD_DATE","DLV_FY_DATE","DLV_JJ_DATE","BO_NO","SHIP_NAME","LOGI_NAME","BAL_ADDR","BO_DATE","BO_NUM"};//导出的字段名称
				ToExcel.toReportExcel(act.getResponse(),request, "发运计划查询列表.xls",head,cols,list);
			}else if("2".equals(common)){//明细导出
				List<Map<String, Object>> list = reDao.getDlvPlanQueryExpDel(map);
				String [] head={"组板号","订单号","车型","配置","颜色","物料代码","本次组板量"};
				String [] cols={"BO_NO","ORD_NO","MODEL_NAME","PACKAGE_NAME","COLOR_NAME","MATERIAL_CODE","BD_TOTAL"};//导出的字段名称
				ToExcel.toReportExcel(act.getResponse(),request, "发运计划明细列表.xls",head,cols,list);
			}else{
				Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request
						.getParamValue("curPage")) : 1;
				PageResult<Map<String, Object>> ps = reDao.getDlvPlanManaQuery(map, curPage,
						Constant.PAGE_SIZE);
				act.setOutData("ps", ps);
			}
			
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "发运组板信息查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 发运计划发送确认
	 */
	public void  sendDlvPlan(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String[] groupIds=request.getParamValues("groupIds");//组板ID
			String[] hiddenIds=request.getParamValues("hiddenIds");//隐藏复选框所有值
			String[] loadDates=request.getParamValues("LOAD_DATES");//计划装车日期
			String[] fyDates=request.getParamValues("FY_DATES");//计划发运日期
			String[] jhDates=request.getParamValues("JH_DATES");//计划到货日期
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			if(groupIds!=null && groupIds.length>0){
				for(int i=0;i<groupIds.length;i++){//循环选中的多选框
					int index=0;
					for(int j=0;j<hiddenIds.length;j++){//循环所有的多选框
					if(groupIds[i].equals(hiddenIds[j])){//如果相等 可以取出对应一行的值
							index=j;//获取对应行的下标
							break;
						}
					}
					String boId=groupIds[i];
					String loadDate=loadDates[index];
					String fyDate=fyDates[index];
					String jhDate=jhDates[index];
					Date planLoadDate=sdf.parse(loadDate);
					Date dlvFyDate=sdf.parse(fyDate);
					Date dlvJjDate=sdf.parse(jhDate);
					//更新发运组板表的相关时间
					TtSalesBoardPO tsb=new TtSalesBoardPO();//组板表实体
					tsb.setBoId(Long.parseLong(boId));
					TtSalesBoardPO tsbb=new TtSalesBoardPO();//组板表实体
					tsbb.setBoId(Long.parseLong(boId));
					tsbb=(TtSalesBoardPO) reDao.select(tsbb).get(0);
					TtSalesBoardPO tsbp=new TtSalesBoardPO();//组板表实体
					tsbp.setPlanLoadDate(planLoadDate);
					tsbp.setDlvFyDate(dlvFyDate);
					tsbp.setDlvJjDate(dlvJjDate);
					tsbp.setUpdateBy(logonUser.getUserId());
					tsbp.setUpdateDate(new Date());
					tsbp.setHandleStatus(Constant.HANDLE_STATUS11);//计划已发送
					tsbp.setSendNum(tsbb.getBoNum());//发运数量
					reDao.update(tsb, tsbp);
					//更新组板明细表的发运数量
					reDao.updateBoDetailFy(logonUser.getUserId(), boId);
					//根据组板ID更新发运明细的发运数量
					reDao.updateDlvDetailFy(logonUser.getUserId(), boId);
					//根据组板ID获取发运表数据，并更新发运数量和发运状态为已发运
					List<Map<String, Object>> dlist=reDao.getDlvIdByBoId(boId);
					if(dlist!=null&&dlist.size()>0){
						for(int j=0;j<dlist.size();j++){
							Map<String, Object> map=dlist.get(j);
							String reqId=map.get("REQ_ID").toString();
							TtVsDlvryPO tvd=new TtVsDlvryPO();
							tvd.setReqId(Long.parseLong(reqId));
							TtVsDlvryPO tvd2=new TtVsDlvryPO();
							//tvd2.setDlvStatus(Constant.ORDER_STATUS_07);//已发运
							tvd2.setDlvFyDate(dlvFyDate);
							tvd2.setDlvJjDate(dlvJjDate);
							tvd2.setUpdateBy(logonUser.getUserId());
							tvd2.setUpdateDate(new Date());
							tvd2.setDlvFyTotal(Integer.parseInt(map.get("DLV_BD_TOTAL").toString()));
							reDao.update(tvd, tvd2);
							//判断发运类型是否为调拨单，若是，更新调拨单主表和明细表的发运数量
							if(map.get("DLV_TYPE").toString()==String.valueOf(Constant.DELIVERY_ORD_TYPE_ALLOCAT)){
								//更新调拨单明细表的发运数量
								reDao.updateDispDetFy(logonUser.getUserId(), boId);
								//更新调拨单主表的发运数量
								reDao.updateDispMainFy(logonUser.getUserId(), boId);
							}
						}
					}else{
						act.setOutData("returnValue", 2);//错误提示
						throw new Exception("获取发运数据异常");
					}
				}
				act.setOutData("returnValue", 1);//成功
			}else{
				act.setOutData("returnValue", 2);//错误提示
			}	
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "发运司机调整");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 发运计划发送查看详情初始化
	 */
	public void seachInit(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String boId = CommonUtils.checkNull(request.getParamValue("Id"));//组板ID
			List<Object> params=new ArrayList<Object>();
			params.add(boId);
			Map<String,Object> sendBoardMap=ssDao.getBoardByBoId(params);
			List<Map<String, Object>> list = ssDao.getSendBoardMatListQuery(params);
			act.setOutData("sendBoardMap", sendBoardMap);
			act.setOutData("list", list);
			act.setForword(seachInitUtl);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE,
					"发运计划发送查看详情初始化");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 发运计划查询初始化
	 */
	public void planQueryInit(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		String areaIds = MaterialGroupManagerDao
		.getPoseIdBusinessIdStr(logonUser.getPoseId().toString());
		try {
			String poseId=logonUser.getPoseId().toString();
			String poseBusType=logonUser.getPoseBusType().toString();
			List<Map<String, Object>> list_yieldly=reLDao.getLogiByWarehouse(poseId,poseBusType);
			act.setOutData("list", list_yieldly);//产地LIST
			
			List<Map<String, Object>> list_logi=new ArrayList<Map<String, Object>>();
			if (logonUser.getPoseBusType().intValue() == Constant.POSE_BUS_TYPE_WL)
			{
				list_logi=reLDao.getLogiByPoseId(areaIds,logonUser);
			}else{
				list_logi=reLDao.getLogiByArea(areaIds);
			}
			act.setOutData("list_logi", list_logi);//物流商LIST
			act.setForword(dlvPlanQueryInitUtl);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE,
					"发运计划查询初始化");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 发运单打印
	 */
	public void dlvOrderPrint(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String boId=CommonUtils.checkNullNum(request.getParamValue("boId"));//组板ID	
			TtSalesBoardPO tt=new TtSalesBoardPO();
			tt.setBoId(Long.parseLong(boId));
			List list=reDao.select(tt);
			act.setOutData("boNo", ((TtSalesBoardPO)list.get(0)).getBoNo());
			//根据组板ID获取组板信息
			List<Map<String, Object>> mlist=reDao.getDlvPlanPrintMain(boId);
			//遍历列表获取物料明细数据
			if(mlist!=null&&mlist.size()>0){
				for(int i=0;i<mlist.size();i++){
					Map<String, Object> map=mlist.get(i);
					List<Map<String, Object>> dlist=reDao.getDlvPlanPrintDetail(boId, map.get("DEALER_NAME").toString(), map.get("TRANS_NAME").toString(), map.get("BAL_ADDR").toString());
					map.put("dlist", dlist);//添加明细列表
				}
			}
			act.setOutData("mlist", mlist);
			act.setForword(dlvbillPrintUtl);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE,
					"运单打印");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
}
