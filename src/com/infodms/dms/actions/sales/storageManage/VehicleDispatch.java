package com.infodms.dms.actions.sales.storageManage;

import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.base.ActionBase;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.materialManager.MaterialGroupManagerDao;
import com.infodms.dms.dao.common.CommonDAO;
import com.infodms.dms.dao.sales.dealer.DealerInfoDao;
import com.infodms.dms.dao.sales.storageManage.CheckVehicleDAO;
import com.infodms.dms.dao.sales.storageManage.VehicleDispatchDAO;
import com.infodms.dms.dao.sales.storageManage.VehicleLocationChangeDAO;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TmDealerPO;
import com.infodms.dms.po.TmDealerWarehousePO;
import com.infodms.dms.po.TmVehiclePO;
import com.infodms.dms.po.TtVsVehicleTransferChkPO;
import com.infodms.dms.po.TtVsVehicleTransferPO;
import com.infodms.dms.po.TtVsVhclChngPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.ActionContextExtend;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;

public class VehicleDispatch extends ActionBase {
	public Logger logger = Logger.getLogger(VehicleDispatch.class);
	private ActionContext act = ActionContext.getContext();
	private static final CheckVehicleDAO dao = new CheckVehicleDAO();

	public static final CheckVehicleDAO getInstance() {
		return dao;
	}
	
	private final String vehicleShiftLibraryInitUrl = "/jsp/sales/storageManage/vehicleShiftLibraryInit.jsp";

	private final String vehicleDispatchInitUrl = "/jsp/sales/storageManage/vehicleDispatchInit.jsp";
	private final String vehicleDispatchCheckOEMInit = "/jsp/sales/storageManage/vehicleDispatchCheckOEMInit.jsp";
	private final String chooseVehicleDispatchInit = "/jsp/sales/storageManage/chooseVehicleDispatchInit.jsp";
	private final String vehicleDispatchInInit = "/jsp/sales/storageManage/vehicleDispatchInInit.jsp";

	public void getJs() {
		act.setForword(getJs(act.getRequest()));
	}

	
	
	
	public void VehicleShiftLibraryInit() {
		AclUserBean logonUser = null;
		try {
			ActionContextExtend atx = (ActionContextExtend) ActionContext.getContext();
			String reqURL = atx.getRequest().getContextPath();
			if ("/CVS-SALES".equals(reqURL.toUpperCase())) {
				act.setOutData("returnValue", 1);
			} else {
				act.setOutData("returnValue", 2);
			}
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			getSecodeDel_D();
			String dlrId = logonUser.getDealerId();

			Integer oemFlag = CommonUtils.getNowSys(Long.parseLong(logonUser.getOemCompanyId()));
			TmDealerPO td =new TmDealerPO();
			td.setDealerId(new Long(logonUser.getDealerId()));
			td=(TmDealerPO) dao.select(td).get(0);
			if(td.getDealerLevel().intValue()==10851002){
				List<Map<String,Object>>listInfo=CommonUtils.seachDealerInfo(new Long(dlrId));
				act.setOutData("listInfo", listInfo);
			}
			act.setOutData("dealerLevel", td.getDealerLevel());
			act.setOutData("oemFlag", oemFlag);
			act.setOutData("dlrId", dlrId);
			act.setForword(vehicleShiftLibraryInitUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "调拨申请页面初始化");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	
	public void vehicleShiftLibrarySubmit() {
		AclUserBean logonUser = null;
		try {
			RequestWrapper request = act.getRequest();
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			String vehicleIds = request.getParamValue("vehicleIds");
			String warehouseId = request.getParamValue("warehouseId");
			dao.vehicleShiftLibrary(vehicleIds,warehouseId);
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
	 * FUNCTION : 调拨申请页面初始化(DLR)
	 * 
	 * @param :
	 * @return :
	 * @throws :
	 *             LastUpdate : 2010-5-27
	 */
	public void VehicleDispatchInit() {
		AclUserBean logonUser = null;
		try {
			ActionContextExtend atx = (ActionContextExtend) ActionContext.getContext();
			String reqURL = atx.getRequest().getContextPath();
			if ("/CVS-SALES".equals(reqURL.toUpperCase())) {
				act.setOutData("returnValue", 1);
			} else {
				act.setOutData("returnValue", 2);
			}
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			getSecodeDel_D();
			String dlrId = logonUser.getDealerId();

			Integer oemFlag = CommonUtils.getNowSys(Long.parseLong(logonUser.getOemCompanyId()));
			TmDealerPO td =new TmDealerPO();
			td.setDealerId(new Long(logonUser.getDealerId()));
			td=(TmDealerPO) dao.select(td).get(0);
			if(td.getDealerLevel().intValue()==10851002){
				List<Map<String,Object>>listInfo=CommonUtils.seachDealerInfo(new Long(dlrId));
				act.setOutData("listInfo", listInfo);
			}
			act.setOutData("dealerLevel", td.getDealerLevel());
			act.setOutData("oemFlag", oemFlag);
			act.setOutData("dlrId", dlrId);
			act.setForword(vehicleDispatchInitUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "调拨申请页面初始化");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	public void chooseVehicleDispatchInit() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			warehouseList();
			act.setForword(chooseVehicleDispatchInit);
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "批发车辆选择初始化");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * FUNCTION : 经销商仓库位置下拉列表
	 * 
	 * @param :
	 * @return :
	 * @throws :
	 *             LastUpdate : 2010-7-20
	 */
	public void warehouseList() {
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			String warehouseType = act.getRequest().getParamValue("warehouseType");
			if (warehouseType == null || warehouseType.equals("")) {
				warehouseType = "";
			}
			Long poseId = logonUser.getPoseId();
			Long comId = logonUser.getCompanyId();
			List<Map<String, Object>> areaList = MaterialGroupManagerDao.getDealerId(comId.toString(), poseId.toString());
			String dealerIds__ = "";
			for (int i = 0; i < areaList.size(); i++) {
				dealerIds__ += areaList.get(i).get("DEALER_ID").toString() + ",";
			}
			if (!"".equals(dealerIds__)) {
				dealerIds__ = dealerIds__.substring(0, (dealerIds__.length() - 1));
			} else {
				dealerIds__ = "";
			}
			List<Map<String, Object>> list = VehicleLocationChangeDAO.warehouseQuery(dealerIds__, "");
			act.setOutData("list", list);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "当前经销商用户仓库查询失败");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/*
	 * FUNCTION : 获取当前用户下的所有二级经销商：下拉列表使用
	 */
	public void getSecodeDel_D() {
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			Long poseId = logonUser.getPoseId();
			Long comId = logonUser.getCompanyId();
			List<Map<String, Object>> areaList = MaterialGroupManagerDao.getDealerId(comId.toString(), poseId.toString());
			String dealerIds__ = "";
			for (int i = 0; i < areaList.size(); i++) {
				dealerIds__ += areaList.get(i).get("DEALER_ID").toString() + ",";
			}
			if (!"".equals(dealerIds__)) {
				dealerIds__ = dealerIds__.substring(0, (dealerIds__.length() - 1));
			} else {
				dealerIds__ = "";
			}

			List<Map<String, Object>> secondeDels = DealerInfoDao.getInstance().getDel("", dealerIds__, Constant.DEALER_LEVEL_02.toString(), Constant.DEALER_TYPE_DVS + "");
			act.setOutData("secondeDels", secondeDels);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "当前经销商用户下级经销商查询失败");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * FUNCTION : 调拨申请：查询展示(DLR)
	 * 
	 * @param :
	 * @return :
	 * @throws :
	 *             LastUpdate : 2010-5-27
	 */
	public void VehicleDispatchList() {
		AclUserBean logonUser = null;
		try {
			RequestWrapper request = act.getRequest();
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			String exVeh = CommonUtils.checkNull(request.getParamValue("exVeh"));

			String materialCode = CommonUtils.checkNull(request.getParamValue("materialCode")); // 物料组
			String materialCode__ = CommonUtils.checkNull(request.getParamValue("materialCode__")); // 物料
			String vin = CommonUtils.checkNull(request.getParamValue("vin"));
			String warehouseId = CommonUtils.checkNull(request.getParamValue("warehouse__"));
			Long poseId = logonUser.getPoseId();
			Long comId = logonUser.getCompanyId();
			List<Map<String, Object>> areaList = MaterialGroupManagerDao.getDealerId(comId.toString(), poseId.toString());
			String dealerIds__ = "";
			for (int i = 0; i < areaList.size(); i++) {
				dealerIds__ += areaList.get(i).get("DEALER_ID").toString() + ",";
			}
			dealerIds__ = dealerIds__.substring(0, (dealerIds__.length() - 1)); // 当前用户职位对应的经销商ID

			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页

			PageResult<Map<String, Object>> ps = VehicleDispatchDAO.getCanDispatchVehicleList(warehouseId, dealerIds__, materialCode, materialCode__, vin, exVeh, Constant.PAGE_SIZE, curPage);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "调拨申请：查询展示");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * FUNCTION : 调拨申请：提交(DLR)
	 * 
	 * @param :
	 * @return :
	 * @throws :
	 *             LastUpdate : 2010-5-31
	 */
	public void vehicleDispatchSubmit() {
		AclUserBean logonUser = null;
		try {
			RequestWrapper request = act.getRequest();
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			String vehicleIds = request.getParamValue("vehicleIds");
			String[] vehicleId = vehicleIds.split(","); // 要调拨的车辆id
			String dealerId__A = CommonUtils.checkNull(request.getParamValue("dealerId")); // 接收车辆的dealerId
			String dealerId__B = null;
			TmDealerPO tdp = new TmDealerPO();
			tdp.setDealerId(Long.parseLong(dealerId__A));
			List<TmDealerPO> listC = dao.select(tdp);
			Long oemComId = new Long(0);

			String para = CommonDAO.getPara(Constant.CHANA_SYS.toString());

			if (listC != null && listC.size() > 0) {
				oemComId = listC.get(0).getOemCompanyId();
			}
			List<Map<String, Object>> codeList = DealerInfoDao.getInstance().getDel(dealerId__A, "", "", "");
			String dealerCode = "";
			if (codeList != null && codeList.size() > 0) {
				dealerCode = codeList.get(0).get("DEALER_CODE").toString(); // 接收车辆的dealerCode
			}
			// 得到经销商级别
			List<Map<String, Object>> arealist = VehicleDispatchDAO.selectAreaId(dealerCode);
			String str = "";
			String myVinStr = "";// 返回检索数据验证字符串
			for (int i = 0; i < vehicleId.length; i++) {
				myVinStr = VehicleDispatchDAO.getVinRightCar(vehicleId[i]);
				if (myVinStr != "") {
					myVinStr = "VIN码为" + myVinStr + "的车辆当前位置不存在，请在车辆位置变更中为该车设置当前位置";
					break;
				}

				boolean areaFlag = false;
				// List<Map<String, Object>> vhcllist =
				// VehicleDispatchDAO.selectVehicleAreaId(vehicleId[i]);

				List<Map<String, Object>> vhcllist = VehicleDispatchDAO.selectVehicleAreaId(Long.parseLong(vehicleId[i]));
				String vin = "";
				for (int n = 0; n < vhcllist.size(); n++) {
					String vareaId = vhcllist.get(n).get("AREA_ID")==null?"":vhcllist.get(n).get("AREA_ID").toString();
					vin = vhcllist.get(n).get("VIN").toString();
					for (int m = 0; m < arealist.size() && areaFlag == false; m++) {
						String areaId = arealist.get(m).get("AREA_ID").toString();
						if (vareaId.equals(areaId)) {
							areaFlag = true;
							break;
						} else {
							areaFlag = false;

							if (Constant.COMPANY_CODE_JC.equals(para.toUpperCase())) {
								areaFlag = false;
							} else if (Constant.COMPANY_CODE_CVS.equals(para.toUpperCase())) {
								areaFlag = this.chkProductBase(vareaId, areaId);

								if (areaFlag) {
									break;
								}
							} else {
								throw new RuntimeException("判断当前系统的系统参数错误！");
							}
						}
					}
					if (areaFlag) {
						break;
					}
				}
				if (areaFlag == false) {
					if ("".equals(str)) {
						str = vin;
					} else {
						str = vin + "," + str;
					}
				}
			}
			if (myVinStr != "") {
				act.setOutData("returnValue", 3);
				act.setOutData("myVinStr", myVinStr);
			} else if (str.length() > 0) {
				if (Constant.COMPANY_CODE_JC.equals(para.toUpperCase())) {
					act.setOutData("returnValue", 2);
					act.setOutData("str", str);
				} else if (Constant.COMPANY_CODE_CVS.equals(para.toUpperCase())) {
					act.setOutData("returnValue", 4);
					act.setOutData("str", str);
				} else {
					throw new RuntimeException("判断当前系统的系统参数错误！");
				}

				/*
				 * act.setOutData("returnValue", 2); act.setOutData("str", str);
				 */
			} else {
				boolean isRela = false;
				String transfer_reason = CommonUtils.checkNull(request.getParamValue("transfer_reason")); // 调拨原因
				/*
				 * if (null != transfer_reason && !"".equals(transfer_reason)) {
				 * transfer_reason = new
				 * String(transfer_reason.getBytes("ISO-8859-1"),"GB2312"); }
				 */
				if (null != vehicleIds && vehicleId.length > 0) {
					String transferNo = null;
					for (int i = 0; i < vehicleId.length; i++) {
						TtVsVehicleTransferPO transferPO = new TtVsVehicleTransferPO();
						Long transferId = Long.parseLong(SequenceManager.getSequence(""));
						if (transferNo == null) {
							transferNo = "PF" + transferId; // 批发号
						}
						transferPO.setTransferId(transferId); // 调拨ID
						transferPO.setTransferNo(transferNo);
						transferPO.setVehicleId(Long.parseLong(vehicleId[i])); // 车辆ID
						// 通过TM_VEHICLE得到dealerID
						TmVehiclePO vehiclePO = new TmVehiclePO();
						vehiclePO.setVehicleId(Long.parseLong(vehicleId[i]));
						List vehicelList = dao.select(vehiclePO);
						// TODO 新增判断是不该车状态为10241006,已经批发，正在审核中 2012-06-08 韩晓宇

						TmVehiclePO condition = vehicelList != null ? (TmVehiclePO) vehicelList.get(0) : null;
						if (condition != null && condition.getLockStatus() != null) {
							String lock_status = condition.getLockStatus().toString();
							if (lock_status != null && Constant.LOCK_STATUS_PFSD.toString().equals(lock_status)) {
								throw new RuntimeException("该车已批发，等待审核！");
							}
						}

						String warehouseId = ((TmVehiclePO) vehicelList.get(0)).getVehicleArea();
						TmDealerWarehousePO tmDealerWarehousePO = new TmDealerWarehousePO();
						tmDealerWarehousePO.setWarehouseId(new Long(warehouseId));
						vehicelList = dao.select(tmDealerWarehousePO);

						Long dealerId = ((TmDealerWarehousePO) vehicelList.get(0)).getDealerId();
						transferPO.setOutDealerId(dealerId); // 调出经销商ID
						dealerId__B = dealerId.toString();
						transferPO.setTransferDate(new Date()); // 调拨日期
						transferPO.setCompanyId(oemComId);
						if (null != transfer_reason && !"".equals(transfer_reason)) {
							transferPO.setTransferReason(transfer_reason.trim()); // 调拨原因
						} else {
							transferPO.setTransferReason(" ");
						}
						transferPO.setCheckStatus(Constant.DISPATCH_STATUS_01); // 审核状态
						TmDealerPO dealerPO = new TmDealerPO();
						dealerPO.setDealerCode(dealerCode);
						List list = dao.select(dealerPO);
						if (null != list && list.size() > 0) {
							Long dealerID = ((TmDealerPO) list.get(0)).getDealerId();
							transferPO.setInDealerId(dealerID); // 调入经销商ID

							transferPO.setCreateDate(new Date());
							transferPO.setCreateBy(logonUser.getUserId());

							boolean isRelation = false;
							/*
							 * String logonDealerId = logonUser.getDealerId();
							 * 
							 * //得到调入经销商的parentDealerId Map<String, Object>
							 * D_ParentDealerId__ =
							 * VehicleDispatchDAO.getParentDealerId(dealerID+"");
							 * String D_ParentDealerId =
							 * D_ParentDealerId__.get("PARENT_DEALER_D").toString();
							 * 
							 * String[] logonIds = logonDealerId.split(","); for
							 * (int j = 0; j < logonIds.length; j++) { if
							 * (logonIds[j].equals(D_ParentDealerId)) {
							 * isRelation = true; break; } }
							 */
							// 验证两个经销商dealerId是否属于同一经销商级别树
							if (VehicleDispatchDAO.chkDlr(dealerID.toString(), dealerId.toString()) || VehicleDispatchDAO.chkDlr(dealerId.toString(), dealerID.toString())) {
								isRelation = true;
								isRela = true;
							}
							//判断两个经销商是不是同一家一级店下面的二级店
							//调入经销商
							TmDealerPO td1=new TmDealerPO();
							td1.setDealerId(dealerId);
							td1=(TmDealerPO) dao.select(td1).get(0);
							//调出经销商
							TmDealerPO td2=new TmDealerPO();
							td2.setDealerId(dealerID);
							td2=(TmDealerPO) dao.select(td2).get(0);
							if(Constant.DEALER_LEVEL_02.toString().equals(td1.getDealerLevel().toString())&&Constant.DEALER_LEVEL_02.toString().equals(td2.getDealerLevel().toString())){
								if(td1.getParentDealerD().toString().equals(td2.getParentDealerD().toString())){
									isRelation = true;
								}
							}
							
							if (isRelation) {
								// 直接修改dealerId
								TmVehiclePO tmVehiclePO__ = new TmVehiclePO();
								tmVehiclePO__.setVehicleId(Long.parseLong(vehicleId[i]));
								List<TmVehiclePO> listB = dao.select(tmVehiclePO__);
								Long dealerIdA = listB.get(0).getDealerId(); // 调出方
								transferPO.setCheckStatus(Constant.DISPATCH_STATUS_03);
								dao.insert(transferPO);
								// 判断是否是结算中心

								TmDealerPO tmDealerPO = new TmDealerPO();
								tmDealerPO.setDealerId(dealerIdA);
								List dealerList = dao.select(tmDealerPO);
								Integer dealerType = ((TmDealerPO) dealerList.get(0)).getDealerType();
								if (dealerType == Constant.DEALER_TYPE_JSZX) {
									List<Object> ins = new LinkedList<Object>();
									ins.add(transferId);
									List<Integer> outs = new LinkedList<Integer>();
									dao.callProcedure("P_VEHICLE_TRANSFERDATA_TO_ERP", ins, outs);
								}
								TmVehiclePO tmVehiclePO__value = new TmVehiclePO();
								tmVehiclePO__value.setVehicleId(Long.parseLong(vehicleId[i]));
								tmVehiclePO__value.setDealerId(dealerID);
								tmVehiclePO__value.setStorageDate(new Date());
								tmVehiclePO__value.setVehicleArea("");
								tmVehiclePO__value.setLockStatus(Constant.LOCK_STATUS_01);
								tmVehiclePO__value.setLifeCycle(Constant.VEHICLE_LIFE_05);
								dao.update(tmVehiclePO__, tmVehiclePO__value);

							} else {
								TmVehiclePO tmVehiclePO__ = new TmVehiclePO();
								tmVehiclePO__.setVehicleId(Long.parseLong(vehicleId[i]));
								TmVehiclePO tmVehiclePO__value = new TmVehiclePO();
								tmVehiclePO__value.setLockStatus(Constant.LOCK_STATUS_PFSD);
								dao.update(tmVehiclePO__, tmVehiclePO__value);

								dao.insert(transferPO);
							}
						}

						// 向日志表写入操作记录
						TtVsVhclChngPO chngPO = new TtVsVhclChngPO();
						Long vhclChangeId = Long.parseLong(SequenceManager.getSequence(""));
						chngPO.setVhclChangeId(vhclChangeId); // 改变序号
						chngPO.setVehicleId(Long.parseLong(vehicleId[i])); // 车辆ID
						chngPO.setOrgType(logonUser.getOrgType()); // 组织类型
						chngPO.setOrgId(logonUser.getOrgId()); // 组织ID
						chngPO.setDealerId(dealerId); // 经销商ID
						chngPO.setChangeCode(Constant.STORAGE_CHANGE_TYPE); // 改变类型
						chngPO.setChangeName(Constant.STORAGE_CHANGE_TYPE_08 + "");// 改变名称
						chngPO.setChangeDate(new Date()); // 改变时间
						if (null != transfer_reason && !"".equals(transfer_reason)) {
							chngPO.setChangeDesc(transfer_reason); // 改变描述
						}

						chngPO.setCreateDate(new Date());
						chngPO.setCreateBy(logonUser.getUserId());
						dao.insert(chngPO);
					}
				}

				if (isRela) {
					CheckVehicle cv = new CheckVehicle();
					cv.callOther(vehicleId, transfer_reason.trim(), "13071003", dealerId__A, dealerId__B);
				}

				act.setOutData("returnValue", 1);
				act.setForword(vehicleDispatchInitUrl);
			}
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "调拨申请：提交");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * FUNCTION : 调拨审核页面初始化(OEM)
	 * 
	 * @param :
	 * @return :
	 * @throws :
	 *             LastUpdate : 2010-5-31
	 */
	public void vehicleDispatchCheckOEMInit() {
		AclUserBean logonUser = null;
		try {
			ActionContextExtend atx = (ActionContextExtend) ActionContext.getContext();
			String reqURL = atx.getRequest().getContextPath();
			if ("/CVS-SALES".equals(reqURL.toUpperCase())) {
				act.setOutData("returnValue", 1);
			} else {
				act.setOutData("returnValue", 2);
			}
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			act.setForword(vehicleDispatchCheckOEMInit);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "调拨申请页面初始化");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * FUNCTION : 调拨审核页面:查询可审批的调拨列表
	 * 
	 * @param :
	 * @return :
	 * @throws :
	 *             LastUpdate : 2010-6-1
	 */
	public void vehicleDispatchCheckList() {
		AclUserBean logonUser = null;
		try {
			RequestWrapper request = act.getRequest();
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			Long orgId = logonUser.getOrgId();
			String orgType = logonUser.getDutyType();
			String outDealerCode = CommonUtils.checkNull(request.getParamValue("outDealerCode")); // 调出经销商
			String inDealerCode = CommonUtils.checkNull(request.getParamValue("inDealerCode")); // 调入经销商
			String materialCode = CommonUtils.checkNull(request.getParamValue("materialCode")); // 物料组
			String vin = CommonUtils.checkNull(request.getParamValue("vin")); // VIN

			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
			PageResult<Map<String, Object>> ps = null;
			System.out.println(orgType);
			if (orgType.equals(Constant.DUTY_TYPE_COMPANY.toString())) {
				ps = VehicleDispatchDAO.getVehicleDispatchCheckListA(outDealerCode, inDealerCode, materialCode, vin, Constant.PAGE_SIZE, curPage);
			}
			if (orgType.equals(Constant.DUTY_TYPE_LARGEREGION.toString())) {
				Long poseId = logonUser.getPoseId();
				ps = VehicleDispatchDAO.getVehicleDispatchCheckListB(poseId, orgId.toString(), outDealerCode, inDealerCode, materialCode, vin, Constant.PAGE_SIZE, curPage);
			}
			if (orgType.equals(Constant.DUTY_TYPE_SMALLREGION.toString())) {
				Long poseId = logonUser.getPoseId();
				ps = VehicleDispatchDAO.getVehicleDispatchCheckListC(poseId, orgId.toString(), outDealerCode, inDealerCode, materialCode, vin, Constant.PAGE_SIZE, curPage);
			}
			// PageResult<Map<String, Object>> ps =
			// VehicleDispatchDAO.getVehicleDispatchCheckList(outDealerCode,
			// inDealerCode, materialCode, vin, Constant.PAGE_SIZE, curPage);
			act.setOutData("ps", ps);

		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "调拨审核页面:查询可审批的调拨列表");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * FUNCTION : 调拨审核页面:提交审核结果
	 * 
	 * @param :
	 * @return :
	 * @throws :
	 *             LastUpdate : 2010-6-1
	 */
	public void vehicleDispatchCheckSubmit() {
		AclUserBean logonUser = null;
		try {
			RequestWrapper request = act.getRequest();
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			String[] transferIds = request.getParamValue("transferIdsA").split(",");
			String checkStatus = CommonUtils.checkNull(request.getParamValue("checkStatus"));// 审核结果：1:同意；0：驳回
			String checkDesc = CommonUtils.checkNull(request.getParamValue("checkDesc")); // 审批意见
			String orgId=logonUser.getOrgId().toString();
			if (null != transferIds && transferIds.length > 0) {
				for (int j = 0; j < transferIds.length; j++) {
				
					// 1.更新“调拨申请表(TT_VS_VEHICLE_TRANSFER)”审核状态
					TtVsVehicleTransferPO ttVsVehicleTransferPOwhere = new TtVsVehicleTransferPO();
					ttVsVehicleTransferPOwhere.setTransferId(Long.parseLong(transferIds[j]));
					
					Iterator iterator = dao.select(ttVsVehicleTransferPOwhere).iterator();
					if (iterator.hasNext()) {
						TtVsVehicleTransferPO ttVsVehicleTransferPO = (TtVsVehicleTransferPO) iterator.next();
						String outDealerId=ttVsVehicleTransferPO.getOutDealerId().toString();
						String outDealerOrgId=CommonUtils.getDealerOrgId(outDealerId);
						String inDealerId=ttVsVehicleTransferPO.getInDealerId().toString();
						String inDealerOrgId=CommonUtils.getDealerOrgId(inDealerId);
						//调出属于当前组织，调入不属于当前组织
//						if(orgId.equals(outDealerOrgId)&&!orgId.equals(inDealerOrgId)){
//							checkStatus=Constant.DISPATCH_STATUS_06.toString();
//						}
						ttVsVehicleTransferPO.setCheckStatus(new Integer(checkStatus));
						dao.update(ttVsVehicleTransferPOwhere, ttVsVehicleTransferPO);
						// 2.向TT_VS_VEHICLE_TRANSFER_CHK写入审核信息
						TtVsVehicleTransferChkPO chkPO = new TtVsVehicleTransferChkPO();
						Long checkId = Long.parseLong(SequenceManager.getSequence(""));
						chkPO.setCheckId(checkId); // 审核ID
						chkPO.setTransferId(Long.parseLong(transferIds[j])); // 调拨ID
						chkPO.setCheckOrgId(logonUser.getOrgId()); // 审核部门
						chkPO.setCheckPositionId(logonUser.getPoseId()); // 审核职位
						chkPO.setCheckUserId(logonUser.getUserId()); // 审核人
						chkPO.setCheckDesc(checkDesc);
						chkPO.setCheckStatus(new Integer(checkStatus));
						chkPO.setCreateBy(logonUser.getUserId());
						chkPO.setCreateDate(new Date());
							dao.insert(chkPO);
						

						// 4.向TT_VS_VHCL_CHNG写入变更日志

						TtVsVhclChngPO chngPO = new TtVsVhclChngPO();
						Long vhclChangeId = Long.parseLong(SequenceManager.getSequence(""));
						chngPO.setVhclChangeId(vhclChangeId); // 改变序号
						chngPO.setVehicleId(ttVsVehicleTransferPO.getVehicleId());// 车辆ID
						chngPO.setOrgType(logonUser.getOrgType()); // 组织类型
						chngPO.setOrgId(logonUser.getOrgId()); // 组织ID
						chngPO.setDealerId(ttVsVehicleTransferPO.getOutDealerId());
						chngPO.setChangeCode(Constant.STORAGE_CHANGE_TYPE); // 改变类型:库存状态更改
						if (Constant.DISPATCH_STATUS_03.toString().equals(checkStatus)) {
							chngPO.setChangeName(Constant.STORAGE_CHANGE_TYPE_06 + "");// 改变名称:批发审批通过
						}
						if (Constant.DISPATCH_STATUS_06.toString().equals(checkStatus)) {
							chngPO.setChangeName(Constant.STORAGE_CHANGE_TYPE_13 + "");// 改变名称:批发初审通过
						}
						if (Constant.DISPATCH_STATUS_04.toString().equals(checkStatus)) {
							chngPO.setChangeName(Constant.STORAGE_CHANGE_TYPE_07 + "");// 改变名称:批发审批驳回
						}
						chngPO.setChangeDate(new Date()); // 改变时间
						chngPO.setChangeDesc(checkDesc);
						chngPO.setCreateDate(new Date());
						chngPO.setCreateBy(logonUser.getUserId());
						dao.insert(chngPO);

						// 3.根据“调拨申请表TT_VS_VEHICLE_TRANSFER”更新TM_VEHICLE的dealerId

						if (Constant.DISPATCH_STATUS_03.toString().equals(checkStatus)) {
							TmVehiclePO tmVehiclePOwhere = new TmVehiclePO();
							tmVehiclePOwhere.setVehicleId(ttVsVehicleTransferPO.getVehicleId());
							TmVehiclePO tmVehiclePO = new TmVehiclePO();
							tmVehiclePO.setLifeCycle(Constant.VEHICLE_LIFE_05);
							tmVehiclePO.setLockStatus(Constant.LOCK_STATUS_01);
							tmVehiclePO.setVehicleArea("");
							dao.update(tmVehiclePOwhere, tmVehiclePO);
						}
						if (Constant.DISPATCH_STATUS_04.toString().equals(checkStatus)) {
							TmVehiclePO tmVehiclePOwhere = new TmVehiclePO();
							tmVehiclePOwhere.setVehicleId(ttVsVehicleTransferPO.getVehicleId());
							TmVehiclePO tmVehiclePO = new TmVehiclePO();
							tmVehiclePO.setLockStatus(Constant.LOCK_STATUS_01);
							dao.update(tmVehiclePOwhere, tmVehiclePO);
						}
					}
				}
			}
			act.setOutData("returnValue", 1);
			act.setForword(vehicleDispatchCheckOEMInit);
		} catch (RuntimeException e) {
			logger.error(e);
			act.setException(e);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "调拨审核页面:提交审核结果");
			logger.error(logonUser, e1);
			act.setException(e1);
		}

	}

	public boolean chkMaterialArea(String groupIds, String areaIds) {
		List<Map<String, Object>> chkList = dao.chkMaterial(groupIds, areaIds);

		if (CommonUtils.isNullList(chkList)) {
			return false;
		} else {
			return true;
		}
	}

	public boolean chkProductBase(String dlrArea, String vehArea) {
		Long dlrBase = dao.chkProductBase(dlrArea).getProduceBase();
		Long vehBase = dao.chkProductBase(vehArea).getProduceBase();

		if (dlrBase.intValue() == vehBase.intValue()) {
			return true;
		} else {
			return false;
		}
	}

	public void vehicleDispatchInInit() {
		AclUserBean logonUser = null;
		try {
			warehouseList();
			act.setForword(vehicleDispatchInInit);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "调拨申请页面初始化");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	public void vehicleDispatchInList() {
		AclUserBean logonUser = null;
		try {
			RequestWrapper request = act.getRequest();
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			String vin = CommonUtils.checkNull(request.getParamValue("vin"));
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
			PageResult<Map<String, Object>> ps = VehicleDispatchDAO.getCanDispatchVehicleInList(logonUser.getDealerId(), vin, Constant.PAGE_SIZE, curPage);

			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "调拨申请：查询展示");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	public void vehicleDispatchInSubmit() {
		AclUserBean logonUser = null;
		try {
			RequestWrapper request = act.getRequest();
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			String dealerId = logonUser.getDealerId();
			String vehicleArea = CommonUtils.checkNull(request.getParamValue("warehouseId")); // 调入车辆的dealerId
			String[] transferIds = request.getParamValue("transferIds").split(","); // 要调拨的车辆id
			Long userId = logonUser.getUserId();
			Date date = new Date();
			for (int i = 0; i < transferIds.length; i++) {
				TtVsVehicleTransferPO ttVsVehicleTransferPOwhere = new TtVsVehicleTransferPO();
				ttVsVehicleTransferPOwhere.setTransferId(new Long(transferIds[i]));
				ttVsVehicleTransferPOwhere.setCheckStatus(Constant.DISPATCH_STATUS_03);
				Iterator j = dao.select(ttVsVehicleTransferPOwhere).iterator();
				if (j.hasNext()) {
					TtVsVehicleTransferPO ttVsVehicleTransferPO = (TtVsVehicleTransferPO) j.next();
					ttVsVehicleTransferPO.setCheckStatus(Constant.DISPATCH_STATUS_05);
					ttVsVehicleTransferPO.setUpdateBy(userId);
					ttVsVehicleTransferPO.setUpdateDate(date);
					TmVehiclePO tmVehiclePOwhere = new TmVehiclePO();
					tmVehiclePOwhere.setVehicleId(ttVsVehicleTransferPO.getVehicleId());
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
						chngPO.setChangeName(Constant.STORAGE_CHANGE_TYPE_03 + "");// 改变名称
						chngPO.setChangeDate(new Date()); // 改变时间
						chngPO.setChangeDesc(ttVsVehicleTransferPO.getTransferReason());
						chngPO.setCreateDate(new Date());
						chngPO.setCreateBy(logonUser.getUserId());
						dao.insert(chngPO);
					}
					dao.update(ttVsVehicleTransferPOwhere, ttVsVehicleTransferPO);
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
}
