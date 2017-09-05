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
import com.infodms.dms.dao.sales.storage.sendManage.DispatchOrderManageDao;
import com.infodms.dms.dao.sales.storage.sendManage.DlvPlanManageDao;
import com.infodms.dms.dao.sales.storage.sendManage.SendBoardDao;
import com.infodms.dms.dao.sales.storage.sendManage.SendBoardSeachDao;
import com.infodms.dms.dao.sales.storage.sendManage.SendDriverSeachDao;
import com.infodms.dms.dao.sales.storage.storagebase.LogisticsDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TcUserPO;
import com.infodms.dms.po.TtSalesBoardPO;
import com.infodms.dms.po.TtSalesCityDisPO;
import com.infodms.dms.po.TtSalesWayAddressLogPO;
import com.infodms.dms.po.TtSalesWayBillAddressPO;
import com.infodms.dms.po.TtSalesWayBillDtlPO;
import com.infodms.dms.po.TtSalesWaybillPO;
import com.infodms.dms.po.TtVsDispatchOrderPO;
import com.infodms.dms.po.TtVsDlvryErpPO;
import com.infodms.dms.po.TtVsDlvryPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

/**
 * 司机车辆绑定
 * @author shuyh
 * 2017/8/7
 */
public class DriverVehManage {
	public Logger logger = Logger.getLogger(DriverVehManage.class);
	private ActionContext act = ActionContext.getContext();
	RequestWrapper request = act.getRequest();
	private final SendDriverSeachDao reDao = SendDriverSeachDao.getInstance();
	private final SendBoardDao sbDao = SendBoardDao.getInstance();
	private final DispatchOrderManageDao ddDao = DispatchOrderManageDao.getInstance();
	private final String queryInitUtl = "/jsp/sales/storage/sendmanage/driverManage/driverVehBind.jsp";

	/**
	 * 查询初始化
	 */
	public void queryInit(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			//获取当前用户下的司机列表
			List<Map<String, Object>> drivers=reDao.getDriverListByPose(logonUser.getPoseId().toString());
			act.setOutData("drivers", drivers);
			act.setForword(queryInitUtl);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE,
					"发运计划发送初始化");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 列表查询
	 */
	public void DriverVehManageQuery() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			/******************************页面查询字段start**************************/
			
			String billNo = CommonUtils.checkNull(request.getParamValue("billNo")); //交接单号
			String orderNo = CommonUtils.checkNull(request.getParamValue("orderNo")); //订单号
			String boNo = CommonUtils.checkNull(request.getParamValue("boNo")); //组板号
			String driverName = CommonUtils.checkNull(request.getParamValue("driverName")); //驾驶员姓名
			String driverTel = CommonUtils.checkNull(request.getParamValue("driverTel")); // 驾驶员电话
			String startDate = CommonUtils.checkNull(request.getParamValue("START_DATE")); //绑定日期开始
			String endDate = CommonUtils.checkNull(request.getParamValue("END_DATE")); // 绑定日期结束
			//String common= CommonUtils.checkNull(request.getParamValue("common"));//1表示导出
			/******************************页面查询字段end***************************/
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("billNo", billNo);
			map.put("orderNo", orderNo);
			map.put("boNo", boNo);
			map.put("driverName", driverName);
			map.put("poseId", logonUser.getPoseId().toString());
			map.put("driverTel", driverTel);
			map.put("startDate", startDate);
			map.put("endDate", endDate);
			//根据职位ID获取是否属于物流商以及物流商ID
			Map<String, Object> pmap=sbDao.getPoseLogiById(logonUser.getPoseId().toString());
			map.put("posBusType", pmap.get("POSE_BUS_TYPE").toString());
			map.put("logiId", (BigDecimal)pmap.get("LOGI_ID"));
			
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request
					.getParamValue("curPage")) : 1;
			PageResult<Map<String, Object>> ps = reDao.getDriverVehSeachQuery(map, curPage,
					Constant.PAGE_SIZE);
			act.setOutData("ps", ps);
			
			
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "司机车辆绑定信息查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 车辆绑定
	 */
	public void saveDriverVehBind() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String[] groupIds=request.getParamValues("groupIds");//交接单明细ID
			String[] hiddenIds=request.getParamValues("hiddenIds");//隐藏复选框所有值
			String[] userIds=request.getParamValues("DRIVER_TEL");//驾驶员电话
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			boolean isError=false;
			String eMsg="";
			if(groupIds!=null && groupIds.length>0){
				for(int i=0;i<groupIds.length;i++){//循环选中的多选框
					int index=0;
					for(int j=0;j<hiddenIds.length;j++){//循环所有的多选框
					if(groupIds[i].equals(hiddenIds[j])){//如果相等 可以取出对应一行的值
							index=j;//获取对应行的下标
							break;
						}
					}
					String detId=groupIds[i];
					String userId=userIds[index];
					//验证司机所属承运商与车辆组板承运商是否相同
					List<Map<String, Object>> slist=reDao.getIsMatchLogi(userId, detId);
					if(slist==null||slist.size()==0){
						isError=true;
						eMsg+=i+",";
					}
					
				}
				if(isError){
					act.setOutData("returnValue", 3);
					act.setOutData("eMsg", "选中数据中的第"+eMsg+"条记录司机所对应物流商与组板承运商不相同。无法绑定");
				}else{
					for(int i=0;i<groupIds.length;i++){//循环选中的多选框
						int index=0;
						for(int j=0;j<hiddenIds.length;j++){//循环所有的多选框
						if(groupIds[i].equals(hiddenIds[j])){//如果相等 可以取出对应一行的值
								index=j;//获取对应行的下标
								break;
							}
						}
						String detId=groupIds[i];
						String userId=userIds[index];
						TcUserPO tup=new TcUserPO();
						tup.setUserId(Long.parseLong(userId));
						tup=(TcUserPO) reDao.select(tup).get(0);
						//更新交接单明细表
						TtSalesWayBillDtlPO po=new TtSalesWayBillDtlPO();
						po.setDtlId(Long.parseLong(detId));
						TtSalesWayBillDtlPO po2=new TtSalesWayBillDtlPO();
						po2.setDriverUserId(Long.parseLong(userId));
						po2.setDriverBindDate(new Date());
						po2.setUpdateBy(logonUser.getUserId());
						po2.setUpdateDate(new Date());
						po2.setDriverPhone(tup.getHandPhone());
						po2.setStatus(Constant.WAYBILL_DTL_STATUS_01);//在途
						reDao.update(po, po2);
						//向在途位置信息表新增一条位置信息
						TtSalesWayBillAddressPO tsa=new TtSalesWayBillAddressPO();
						tsa.setAddressId(Long.parseLong(SequenceManager.getSequence(null)));
						tsa.setDtlId(Long.parseLong(detId));
						List list=reDao.select(po);
						po=(TtSalesWayBillDtlPO) list.get(0);
						TtSalesWaybillPO tsw=new TtSalesWaybillPO();
						tsw.setBillId(po.getBillId());
						tsw=(TtSalesWaybillPO) reDao.select(tsw).get(0);
						tsa.setBillId(po.getBillId());
						tsa.setVehicleId(po.getVehicleId());
						
						TtVsDlvryPO tvd=new TtVsDlvryPO();
						tvd.setOrdId(po.getOrderId());
						tvd=(TtVsDlvryPO) reDao.select(tvd).get(0);
						
						List<Map<String, Object>> addrlist = ddDao.getAddrByReceiveWrList(tvd.getDlvWhId().toString());
						if(addrlist!=null&&addrlist.size()>0){
							tsa.setAddress(addrlist.get(0).get("ADDRESS").toString());
						}else{
							throw new Exception("发运仓库下未维护地址信息");
						}
						
						tsa.setCreateBy(logonUser.getUserId());
						tsa.setCreateDate(new Date());
						tsa.setAddressDate(new Date());
						tsa.setVin(po.getVin());
						
						tsa.setDriverPhone(tup.getHandPhone());
						tsa.setMatId(po.getMatId());
						tsa.setBillNo(tsw.getBillNo());
						tsa.setOrderNo(po.getOrderNo());
						reDao.insert(tsa);
						//更新明细表中的最新位置
						TtSalesWayBillDtlPO pold=new TtSalesWayBillDtlPO();
						pold.setDtlId(Long.parseLong(detId));
						TtSalesWayBillDtlPO pnew=new TtSalesWayBillDtlPO();
						pnew.setReportAddress(addrlist.get(0).get("ADDRESS").toString());
						pnew.setReportDate(new Date());
						reDao.update(pold, pnew);
						//根据交接单号更新tt_vs_dlvry_erp中的司机电话和姓名
						TtVsDlvryErpPO tvdep=new TtVsDlvryErpPO();
						tvdep.setSendcarOrderNumber(tsw.getBillNo());
						TtVsDlvryErpPO tvdep2=new TtVsDlvryErpPO();
						tvdep2.setMotorman(tup.getName());
						tvdep2.setMotormanPhone(tup.getHandPhone());
						tvdep2.setUpdateBy(logonUser.getUserId());
						tvdep2.setUpdateDate(new Date());
						reDao.update(tvdep, tvdep2);
						//向日志表写入数据
						TtSalesWayAddressLogPO gpo=new TtSalesWayAddressLogPO();
						gpo.setLogId(Long.parseLong(SequenceManager.getSequence(null)));
						gpo.setDtlId(Long.parseLong(detId));
						gpo.setVehicleId(po.getVehicleId());
						gpo.setAfterStatus(Constant.WAYBILL_DTL_STATUS_01);//在途
						gpo.setCreateBy(logonUser.getUserId());
						gpo.setCreateDate(new Date());
						gpo.setBusinessBy(logonUser.getUserId());
						reDao.insert(gpo);
					}
					act.setOutData("returnValue", 1);//成功
				}
				
			}else{
				act.setOutData("returnValue", 2);//错误提示
			}	
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "司机车辆绑定信息查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
}
