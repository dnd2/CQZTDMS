package com.infodms.dms.actions.sales.storageManage;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.base.ActionBase;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.dao.sales.storageManage.CheckVehicleDAO;
import com.infodms.dms.dao.sales.storageManage.VehicleAllocateDAO;
import com.infodms.dms.dao.sales.storageManage.VehicleLocationChangeDAO;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TmDealerPO;
import com.infodms.dms.po.TmVehiclePO;
import com.infodms.dms.po.TtVsVehicleAllocatePO;
import com.infodms.dms.po.TtVsVhclChngPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.OrderCode;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

public class VehicleAllocate extends ActionBase {
	public Logger logger = Logger.getLogger(VehicleAllocate.class);
	private ActionContext act = ActionContext.getContext();
	private static final CheckVehicleDAO dao = new CheckVehicleDAO();

	public static final CheckVehicleDAO getInstance() {
		return dao;
	}

	private final String vehicleAllocateInit = "/jsp/sales/storageManage/vehicleAllocateInit.jsp";
	private final String chooseVehicleAllocateInit = "/jsp/sales/storageManage/chooseVehicleAllocateInit.jsp";
	private final String vehicleAllocateQuery = "/jsp/sales/storageManage/vehicleAllocateQuery.jsp";
	private final String vehicleAllocateOutInit = "/jsp/sales/storageManage/vehicleAllocateOutInit.jsp";
	private final String vehicleAllocateInInit = "/jsp/sales/storageManage/vehicleAllocateInInit.jsp";

	public void getJs() {
		act.setForword(getJs(act.getRequest()));
	}

	/***************************************************************************
	 * 通知下达界面
	 **************************************************************************/
	public void vehicleAllocateInit() {
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			act.setForword(vehicleAllocateInit);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "调拨申请页面初始化");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/***************************************************************************
	 * 通知下达界面选择车辆的弹出框
	 **************************************************************************/
	public void chooseVehicleAllocateInit() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			act.setForword(chooseVehicleAllocateInit);
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "批发车辆选择初始化");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/***************************************************************************
	 * 通知下达界面选择车辆的弹出框查询
	 **************************************************************************/
	public void vehicleAllocateList() {
		AclUserBean logonUser = null;
		try {
			RequestWrapper request = act.getRequest();
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			String exVeh = CommonUtils.checkNull(request.getParamValue("exVeh"));
			String materialCode= CommonUtils.checkNull(request.getParamValue("material_code"));

			String dealerId = CommonUtils.checkNull(request.getParamValue("dealerId"));
			String vin = CommonUtils.checkNull(request.getParamValue("vin"));

			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页

			PageResult<Map<String, Object>> ps = VehicleAllocateDAO.getCanAllocateVehicleList(dealerId, vin, exVeh,materialCode, Constant.PAGE_SIZE, curPage);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "调拨申请：查询展示");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/***************************************************************************
	 * 提交，通知下达。
	 **************************************************************************/
	public void vehicleAllocateSubmit() {
		AclUserBean logonUser = null;
		try {
			RequestWrapper request = act.getRequest();
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			String vehicleIds = request.getParamValue("vehicleIds");
			String[] vehicleId = vehicleIds.split(","); // 要调拨的车辆id
			String dealerIdOut = CommonUtils.checkNull(request.getParamValue("dealerIdOut")); // 调出车辆的dealerId;
			String dealerIdIn = CommonUtils.checkNull(request.getParamValue("dealerIdIn")); // 调入车辆的dealerId

			String fundTypeOut = CommonUtils.checkNull(request.getParamValue("fundTypeOut")); // 调出车辆的dealerId;
			String fundTypeIn = CommonUtils.checkNull(request.getParamValue("fundTypeIn")); // 调入车辆的dealerId

			TmDealerPO tdp = new TmDealerPO();

			tdp.setDealerId(Long.parseLong(dealerIdIn));
			tdp = (TmDealerPO) dao.select(tdp).get(0);
			Long oemComId = new Long(0);
			oemComId = tdp.getOemCompanyId();

			String allocate_reason = CommonUtils.checkNull(request.getParamValue("allocate_reason")); // 调拨原因

			if (null != vehicleIds && vehicleId.length > 0) {
				String allocateNo = null;

				allocateNo = OrderCode.get_DB(new Long(fundTypeIn));

				for (int i = 0; i < vehicleId.length; i++) {
					TtVsVehicleAllocatePO allocatePO = new TtVsVehicleAllocatePO();
					Long allocateId = Long.parseLong(SequenceManager.getSequence(""));

					allocatePO.setAllocateId(allocateId); // 调拨ID
					allocatePO.setAllocateNo(allocateNo);
					allocatePO.setVehicleId(Long.parseLong(vehicleId[i])); // 车辆ID
					allocatePO.setFundtypeOutId(new Long(fundTypeOut));
					allocatePO.setOutDealerId(new Long(dealerIdOut)); // 调出经销商ID
					allocatePO.setFundtypeInId(new Long(fundTypeIn));
					allocatePO.setInDealerId(new Long(dealerIdIn)); // 调入经销商ID
					allocatePO.setAllocateDate(new Date()); // 调拨日期
					allocatePO.setCompanyId(oemComId);
					allocatePO.setAllocateReason(allocate_reason); // 调拨原因
					allocatePO.setCheckStatus(Constant.ALLOCATE_STATUS_01);
					allocatePO.setCreateDate(new Date());
					allocatePO.setCreateBy(logonUser.getUserId());
					allocatePO.setInterfaceFlag(new Long("10011001"));
					dao.insert(allocatePO);

					TmVehiclePO tmVehiclePOwhere = new TmVehiclePO();
					tmVehiclePOwhere.setVehicleId(Long.parseLong(vehicleId[i]));
					tmVehiclePOwhere.setDealerId(new Long(dealerIdOut));
					tmVehiclePOwhere.setLockStatus(Constant.LOCK_STATUS_01);
					tmVehiclePOwhere.setLifeCycle(Constant.VEHICLE_LIFE_03);

					Iterator k = dao.select(tmVehiclePOwhere).iterator();
					if (k.hasNext()) {
						TmVehiclePO tmVehiclePO = (TmVehiclePO) k.next();
						tmVehiclePO.setLockStatus(Constant.LOCK_STATUS_08);
						dao.update(tmVehiclePOwhere, tmVehiclePO);
						TtVsVhclChngPO chngPO = new TtVsVhclChngPO();
						Long vhclChangeId = Long.parseLong(SequenceManager.getSequence(""));
						chngPO.setVhclChangeId(vhclChangeId); // 改变序号
						chngPO.setVehicleId(tmVehiclePO.getVehicleId()); // 车辆ID
						chngPO.setOrgType(logonUser.getOrgType()); // 组织类型
						chngPO.setOrgId(logonUser.getOrgId()); // 组织ID
						chngPO.setDealerId(new Long(dealerIdOut)); // 经销商ID
						chngPO.setChangeCode(Constant.STORAGE_CHANGE_TYPE); // 改变类型
						chngPO.setChangeName(Constant.STORAGE_CHANGE_TYPE_12 + "");// 改变名称
						chngPO.setChangeDate(new Date()); // 改变时间
						chngPO.setChangeDesc(allocate_reason);
						chngPO.setCreateDate(new Date());
						chngPO.setCreateBy(logonUser.getUserId());
						dao.insert(chngPO);
					} else {
						throw new RuntimeException("该车不符合调拨状态！");
					}
					//修改为查看时调用
//					List in = new ArrayList();
//					in.add(allocateId);
//					dao.callProcedure("  PRO_ERP_ALLOCATE", in, null);
				}
			}
			act.setOutData("returnValue", 1);
		} catch (RuntimeException e) {
			logger.error(e);
			act.setException(e);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "调拨申请：提交");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/***************************************************************************
	 * 调拨查看
	 **************************************************************************/
	public void vehicleAllocateQuery() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			act.setOutData("dutyType", logonUser.getDutyType());
			act.setForword(vehicleAllocateQuery);
			
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "批发车辆选择初始化");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/***************************************************************************
	 * 调拨查看，json
	 **************************************************************************/
	public void vehicleAllocateQueryList() {
		AclUserBean logonUser = null;
		try {
			RequestWrapper request = act.getRequest();
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);

			String dutyType = logonUser.getDutyType();
			String vin = CommonUtils.checkNull(request.getParamValue("vin"));

			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
			
			PageResult<Map<String, Object>> ps = null;
			if (dutyType.equals(Constant.DUTY_TYPE_DEALER.toString())) {
				ps = VehicleAllocateDAO.getCanAllocateVehicleQuery(logonUser.getDealerId(), dutyType, vin, Constant.PAGE_SIZE, curPage);
			} else {
				ps = VehicleAllocateDAO.getCanAllocateVehicleQuery(logonUser.getOrgId() + "", dutyType, vin, Constant.PAGE_SIZE, curPage);
			}
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "调拨申请：查询展示");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/***************************************************************************
	 * 调拨出库查看，界面
	 **************************************************************************/
	public void vehicleAllocateOutInit() {
		AclUserBean logonUser = null;
		try {

			act.setForword(vehicleAllocateOutInit);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "调拨申请页面初始化");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	public void vehicleAllocateOutList() {
		AclUserBean logonUser = null;
		try {
			RequestWrapper request = act.getRequest();
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			String vin = CommonUtils.checkNull(request.getParamValue("vin"));
			
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
			PageResult<Map<String, Object>> ps = VehicleAllocateDAO.getCanAllocateVehicleOutList(logonUser.getDealerId(), vin, Constant.PAGE_SIZE, curPage);

			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "调拨申请：查询展示");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	public void vehicleAllocateOutSubmit() {
		AclUserBean logonUser = null;
		try {
			RequestWrapper request = act.getRequest();
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			String[] allocateIds = request.getParamValue("allocateIds").split(","); // 要调拨的车辆id
			Long userId = logonUser.getUserId();
			String dealerId = logonUser.getDealerId();
			Date date = new Date();
			for (int i = 0; i < allocateIds.length; i++) {
				TtVsVehicleAllocatePO ttVsVehicleAllocatePOwhere = new TtVsVehicleAllocatePO();
				ttVsVehicleAllocatePOwhere.setAllocateId(new Long(allocateIds[i]));
				ttVsVehicleAllocatePOwhere.setOutDealerId(new Long(dealerId));
				ttVsVehicleAllocatePOwhere.setCheckStatus(Constant.ALLOCATE_STATUS_01);
				Iterator j = dao.select(ttVsVehicleAllocatePOwhere).iterator();
				if (j.hasNext()) {
					TtVsVehicleAllocatePO ttVsVehicleAllocatePO = (TtVsVehicleAllocatePO) j.next();
					ttVsVehicleAllocatePO.setCheckStatus(Constant.ALLOCATE_STATUS_02);
					ttVsVehicleAllocatePO.setAllocateDateOut(date);
					ttVsVehicleAllocatePO.setUpdateBy(userId);
					ttVsVehicleAllocatePO.setUpdateDate(date);
					TmVehiclePO tmVehiclePOwhere = new TmVehiclePO();
					tmVehiclePOwhere.setVehicleId(ttVsVehicleAllocatePO.getVehicleId());
					Iterator k = dao.select(tmVehiclePOwhere).iterator();
					if (k.hasNext()) {
						TmVehiclePO tmVehiclePO = (TmVehiclePO) k.next();
						tmVehiclePO.setLifeCycle(Constant.VEHICLE_LIFE_05);
						tmVehiclePO.setVehicleArea("");
						dao.update(tmVehiclePOwhere, tmVehiclePO);
						TtVsVhclChngPO chngPO = new TtVsVhclChngPO();
						Long vhclChangeId = Long.parseLong(SequenceManager.getSequence(""));
						chngPO.setVhclChangeId(vhclChangeId); // 改变序号
						chngPO.setVehicleId(tmVehiclePO.getVehicleId()); // 车辆ID
						chngPO.setOrgType(logonUser.getOrgType()); // 组织类型
						chngPO.setOrgId(logonUser.getOrgId()); // 组织ID
						chngPO.setDealerId(new Long(dealerId)); // 经销商ID
						chngPO.setChangeCode(Constant.STORAGE_CHANGE_TYPE); // 改变类型
						chngPO.setChangeName(Constant.STORAGE_CHANGE_TYPE_10 + "");// 改变名称
						chngPO.setChangeDate(new Date()); // 改变时间
						chngPO.setChangeDesc(ttVsVehicleAllocatePO.getAllocateReason());
						chngPO.setCreateDate(new Date());
						chngPO.setCreateBy(logonUser.getUserId());
						dao.insert(chngPO);
					}

					dao.update(ttVsVehicleAllocatePOwhere, ttVsVehicleAllocatePO);
				}
			}
			act.setOutData("returnValue", 1);
		} catch (RuntimeException e) {
			logger.error(e);
			act.setException(e);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "调拨申请：提交");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/***************************************************************************
	 * 调拨入库查看，界面
	 **************************************************************************/
	public void vehicleAllocateInInit() {
		AclUserBean logonUser = null;
		try {
			warehouseList();
			act.setForword(vehicleAllocateInInit);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "调拨申请页面初始化");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	public void warehouseList() {
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			String dealerId = logonUser.getDealerId();
			List<Map<String, Object>> list = VehicleLocationChangeDAO.warehouseQuery(dealerId, Constant.DEALER_WAREHOUSE_TYPE_01.toString()); // 初始化页面默认为自有库
			act.setOutData("warehouses", list);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "车辆验收");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	public void vehicleAllocateInList() {
		AclUserBean logonUser = null;
		try {
			RequestWrapper request = act.getRequest();
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			String vin = CommonUtils.checkNull(request.getParamValue("vin"));
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
			PageResult<Map<String, Object>> ps = VehicleAllocateDAO.getCanAllocateVehicleInList(logonUser.getDealerId(), vin, Constant.PAGE_SIZE, curPage);

			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "调拨申请：查询展示");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	public void vehicleAllocateInSubmit() {
		AclUserBean logonUser = null;
		try {
			RequestWrapper request = act.getRequest();
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			String dealerId = logonUser.getDealerId();
			String vehicleArea = CommonUtils.checkNull(request.getParamValue("warehouseId")); // 调入车辆的dealerId
			String[] allocateIds = request.getParamValue("allocateIds").split(","); // 要调拨的车辆id
			Long userId = logonUser.getUserId();
			Date date = new Date();
			for (int i = 0; i < allocateIds.length; i++) {
				TtVsVehicleAllocatePO ttVsVehicleAllocatePOwhere = new TtVsVehicleAllocatePO();
				ttVsVehicleAllocatePOwhere.setAllocateId(new Long(allocateIds[i]));
				ttVsVehicleAllocatePOwhere.setCheckStatus(Constant.ALLOCATE_STATUS_02);
				Iterator j = dao.select(ttVsVehicleAllocatePOwhere).iterator();
				if (j.hasNext()) {
					TtVsVehicleAllocatePO ttVsVehicleAllocatePO = (TtVsVehicleAllocatePO) j.next();
					ttVsVehicleAllocatePO.setCheckStatus(Constant.ALLOCATE_STATUS_03);
					ttVsVehicleAllocatePO.setAllocateDateIn(date);
					ttVsVehicleAllocatePO.setUpdateBy(userId);
					ttVsVehicleAllocatePO.setUpdateDate(date);
					TmVehiclePO tmVehiclePOwhere = new TmVehiclePO();
					tmVehiclePOwhere.setVehicleId(ttVsVehicleAllocatePO.getVehicleId());
					Iterator k = dao.select(tmVehiclePOwhere).iterator();
					if (k.hasNext()) {
						TmVehiclePO tmVehiclePO = (TmVehiclePO) k.next();
						tmVehiclePO.setLockStatus(Constant.LOCK_STATUS_01);
						tmVehiclePO.setLifeCycle(Constant.VEHICLE_LIFE_03);
						tmVehiclePO.setDealerId(new Long(dealerId));
						tmVehiclePO.setVehicleArea(vehicleArea);
						dao.update(tmVehiclePOwhere, tmVehiclePO);
						TtVsVhclChngPO chngPO = new TtVsVhclChngPO();
						Long vhclChangeId = Long.parseLong(SequenceManager.getSequence(""));
						chngPO.setVhclChangeId(vhclChangeId); // 改变序号
						chngPO.setVehicleId(tmVehiclePO.getVehicleId()); // 车辆ID
						chngPO.setOrgType(logonUser.getOrgType()); // 组织类型
						chngPO.setOrgId(logonUser.getOrgId()); // 组织ID
						chngPO.setDealerId(new Long(dealerId)); // 经销商ID
						chngPO.setChangeCode(Constant.STORAGE_CHANGE_TYPE); // 改变类型
						chngPO.setChangeName(Constant.STORAGE_CHANGE_TYPE_11 + "");// 改变名称
						chngPO.setChangeDate(new Date()); // 改变时间
						chngPO.setChangeDesc(ttVsVehicleAllocatePO.getAllocateReason());
						chngPO.setCreateDate(new Date());
						chngPO.setCreateBy(logonUser.getUserId());
						dao.insert(chngPO);
					}
					dao.update(ttVsVehicleAllocatePOwhere, ttVsVehicleAllocatePO);
				}
			}
			act.setOutData("returnValue", 1);
		} catch (RuntimeException e) {
			logger.error(e);
			act.setException(e);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "调拨申请：提交");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 调拨查看界面
	 * 导入ERP的方法
	 */
	public void updateIntefaceFlag() {
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			int count=0;
			String allocateNo=CommonUtils.checkNull(act.getRequest().getParamValue("allocateNo"));
			if(allocateNo!=null&&!"".equals(allocateNo)){
				//通过调拨单号查询调拨id号
				TtVsVehicleAllocatePO tvp =new TtVsVehicleAllocatePO();
				tvp.setAllocateNo(allocateNo);
				List<PO>list=dao.select(tvp);
				count=list.size();
				
				//受影响的行数
				int rowCount=0;
				for(int i=0;i<count;i++){
					TtVsVehicleAllocatePO tvaap=new TtVsVehicleAllocatePO();
					TtVsVehicleAllocatePO tvaap0=new TtVsVehicleAllocatePO();
					tvaap0=(TtVsVehicleAllocatePO) list.get(i);
					Long allocateId=tvaap0.getAllocateId();
					
					if(allocateId!=null){
						List in = new ArrayList();
						in.add(allocateId);
						dao.callProcedure("  PRO_ERP_ALLOCATE", in, null);
						tvaap.setAllocateId(tvaap0.getAllocateId());
						TtVsVehicleAllocatePO tvaap1=new TtVsVehicleAllocatePO();
						tvaap1.setInterfaceFlag(new Long(10011002));
						rowCount+=dao.update(tvaap, tvaap1);
					}else{
						break;
					}
				}
				
				//修改的记录数和查询出来的相等说明修改成功
				if(rowCount==count&&count!=0){
					count=1;
				}else{
					count=0;
				}
			}
			
			
			act.setOutData("flag", count);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "车辆验收");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
}
