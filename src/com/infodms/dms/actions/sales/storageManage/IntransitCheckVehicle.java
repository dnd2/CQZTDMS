package com.infodms.dms.actions.sales.storageManage;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
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
import com.infodms.dms.dao.sales.storageManage.IntransitCheckVehicleDAO;
import com.infodms.dms.dao.sales.storageManage.VehicleLocationChangeDAO;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TmVehiclePO;
import com.infodms.dms.po.TtVsDlvryErpDtlPO;
import com.infodms.dms.po.TtVsDlvryErpPO;
import com.infodms.dms.po.TtVsInspectionDetailPO;
import com.infodms.dms.po.TtVsInspectionPO;
import com.infodms.dms.po.TtVsVehicleTransferPO;
import com.infodms.dms.po.TtVsVhclChngPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.dms.chana.actions.OSB21;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

public class IntransitCheckVehicle extends BaseDao {
	public Logger logger = Logger.getLogger(IntransitCheckVehicle.class);
	private ActionContext act = ActionContext.getContext();
	private static final CheckVehicleDAO dao = new CheckVehicleDAO();

	public static final CheckVehicleDAO getInstance() {
		return dao;
	}

	private final String CheckVehicleInitUrl = "/jsp/sales/storageManage/IntransitcheckVehicleInit.jsp";
	private final String VehicleDetailUrl = "/jsp/sales/storageManage/vehicleDetail.jsp";
	private final String DlvryERPURL = "/jsp/sales/storageManage/dlvryERP.jsp";
	private final String tolookLogisticsURL = "/jsp/sales/storageManage/IntransitlogisticsInfo.jsp";
	/**
	 * FUNCTION : 车厂在途查询页面初始化
	 * 
	 * @param :
	 * @return :
	 * @throws :
	 *             LastUpdate : 2010-5-20
	 */
	public void IntransitCheckVehicleInit() {
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			act.setOutData("dutyType", logonUser.getDutyType());
            act.setOutData("orgId", logonUser.getOrgId());
            System.out.println("dsfdsfdsf:"+logonUser.getDutyType());
			//warehouseList();
			//Integer oemFlag = CommonUtils.getNowSys(Long.parseLong(logonUser.getOemCompanyId()));
			Integer oemFlag=1;
			act.setOutData("oemFlag", oemFlag);
			String poseId = logonUser.getPoseId().toString();
			String dealerIds = logonUser.getDealerId();
	
			List<Map<String,Object>> lists=IntransitCheckVehicleDAO.getCheckLists(poseId, dealerIds, null, null,null, null,null,null, null,null, Constant.PAGE_SIZE, 1);;
			act.setOutData("lists",lists);
			act.setForword(CheckVehicleInitUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "车厂在途查询");
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
	 *             LastUpdate : 2010-5-20
	 */
	public void warehouseList() {
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			String dealerIds = logonUser.getDealerId();

			List<Map<String, Object>> list = VehicleLocationChangeDAO.warehouseQuery(dealerIds, Constant.DEALER_WAREHOUSE_TYPE_01.toString()); // 初始化页面默认为自有库

			String wareType = "";
			if (!CommonUtils.isNullList(list)) {
				wareType = list.get(0).get("WAREHOUSE_TYPE").toString();
			}

			act.setOutData("wareType", wareType);
			act.setOutData("list", list);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "车厂在途查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * FUNCTION : 获取采购方代销库下拉列表
	 * 
	 * @param :
	 * @return :
	 * @throws :
	 *             LastUpdate : 2010-9-22
	 */
	public void orderWareList() {
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			String warehouseType = act.getRequest().getParamValue("warehouseType");
			if (warehouseType == null || warehouseType.equals("")) {
				warehouseType = Constant.DEALER_WAREHOUSE_TYPE_02.toString();
			}
			String orderOrgId = act.getRequest().getParamValue("orderOrgId");
			String receiver = act.getRequest().getParamValue("receiver");
			List<Map<String, Object>> orderList = VehicleLocationChangeDAO.warehouseQuery(orderOrgId, warehouseType);
			act.setOutData("receiver", receiver);
			act.setOutData("orderList", orderList);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "车厂在途查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * FUNCTION : 查询可车辆验收的车辆列表
	 * 
	 * @param :
	 * @return :
	 * @throws :
	 *             LastUpdate : 2010-5-20
	 */
	public void checkList() {
		AclUserBean logonUser = null;
		try {
			RequestWrapper request = act.getRequest();
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			String dealerIds = logonUser.getDealerId();
			String dutyType=logonUser.getDutyType();
            String orgId= logonUser.getOrgId().toString();

			// 得到VIN
			String vin = CommonUtils.checkNull(request.getParamValue("vin"));
			String dlvNo = CommonUtils.checkNull(request.getParamValue("dlvNo")); // 获取发运申请单号
			String dlvryNo = CommonUtils.checkNull(request.getParamValue("dlvryNo")); // 获取发运单号
			String dealerCode = CommonUtils.checkNull(request.getParamValue("dealerCode"));//获取经销商代码
			//发车日期
			String startDate =  CommonUtils.checkNull(request.getParamValue("deliverystartDate"));
			String endDate =  CommonUtils.checkNull(request.getParamValue("deliveryendDate"));
		
			
			if (null != vin && !"".equals(vin)) {
				vin = vin.trim();
			}
			String poseId = logonUser.getPoseId().toString();
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
			PageResult<Map<String, Object>> ps = IntransitCheckVehicleDAO.getCheckList(poseId, dealerIds,dutyType,orgId,dealerCode,startDate,endDate, vin, dlvNo,dlvryNo, Constant.PAGE_SIZE, curPage);
			List<Map<String, Object>> lists= IntransitCheckVehicleDAO.getCheckLists(poseId, dealerIds,dutyType,orgId,dealerCode,startDate,endDate, vin, dlvNo,dlvryNo, Constant.PAGE_SIZE, curPage);
			act.setOutData("lists",lists);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "查询可验收车辆");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * FUNCTION : 查看入库车辆信息
	 * 
	 * @param :
	 * @return :
	 * @throws :
	 *             LastUpdate : 2010-5-21
	 */
	public void toCheck() {
		AclUserBean logonUser = null;
		try {
			RequestWrapper request = act.getRequest();
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);

			String vehicle_id = CommonUtils.checkNull(request.getParamValue("vehlId"));

			Map<String, Object> vehicleInfo = CheckVehicleDAO.getVehicleInfo(vehicle_id); // 根据vehicle_id查询车辆详细信息

			getWareNew_SUZUKI();

			act.setOutData("vehicleInfo", vehicleInfo);
			act.setOutData("vehicle_id", vehicle_id);
			act.setForword(VehicleDetailUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "查看入库车辆信息");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * FUNCTION : 验收入库提交
	 * 
	 * @param :
	 * @return :
	 * @throws :
	 *             LastUpdate : 2010-5-21
	 */
	public void checkSubmit() {
		AclUserBean logonUser = null;
		try {
			RequestWrapper request = act.getRequest();
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			act.getSession().get(Constant.LOGON_USER);
			String dealerId = logonUser.getDealerId();
			// 得到用户填写的车辆验收信息
			String vehicleId = CommonUtils.checkNull(request.getParamValue("vehicleId")); // 车辆vehicleId
			String arrive_date = CommonUtils.checkNull(request.getParamValue("arrive_date")); // 实际到车日期
			String arrive_time = CommonUtils.checkNull(request.getParamValue("arrive_time")); // 实际到车时间
			String transport_person = CommonUtils.checkNull(request.getParamValue("transport_person")); // 送车人员
			String inspection_no = ""; // 验收单号
			String inspection_person = CommonUtils.checkNull(request.getParamValue("inspection_person")); // 验收人员
			String vehicle_area = CommonUtils.checkNull(request.getParamValue("warehouse__")); // 车辆所在位置
			String remark = CommonUtils.checkNull(request.getParamValue("remark")); // 备注

			String IS_DAMAGE = CommonUtils.checkNull(request.getParamValue("IS_DAMAGE")); // 备注
			String[] describes = (String[]) request.getParamValues("describe"); // 损坏描述
			String[] part = (String[]) request.getParamValues("part"); // 损坏部位
			String dlvryDtlId = CommonUtils.checkNull(request.getParamValue("dlvryDtlId"));// TT_VS_DLVRY_ERP_DTL.PK发运单明细表主键

			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

			// Map<String, Object> map =
			// CheckVehicleDAO.getChkVehlInfo(vehicleId);
			// String dealer_id = String.valueOf(map.get("DEALER_ID")); //
			// 车辆在途时的所有者
			// String order_org_id = String.valueOf(map.get("ORDER_ORG_ID")); //
			// 订货方
			// String billing_org_id =
			// String.valueOf(map.get("BILLING_ORG_ID")); // 付款方
			// String receiver = String.valueOf(map.get("RECEIVER")); // 收货方

			// 1、修改节点状态：经销商在库
			TmVehiclePO tempVehiclePO = new TmVehiclePO();
			tempVehiclePO.setVehicleId(Long.parseLong(vehicleId));

			TmVehiclePO valueVehiclePO = new TmVehiclePO();
			valueVehiclePO.setLifeCycle(Constant.VEHICLE_LIFE_03);// 修改车辆生命周期：经销商在库
			valueVehiclePO.setStorageDate(format.parse(arrive_date)); // 车辆验收日期
			valueVehiclePO.setDealerId(new Long(logonUser.getDealerId()));
			valueVehiclePO.setVehicleArea(vehicle_area.trim()); // 车辆所在位置，存放经销商仓库ID
			valueVehiclePO.setIsDamage(new Integer(IS_DAMAGE));// 质损状态
			dao.update(tempVehiclePO, valueVehiclePO); // 完成车辆节点更新

			// 从车辆表中查询dealerID
			// TmVehiclePO tmVehiclePO = new TmVehiclePO();
			// tmVehiclePO.setVehicleId(Long.parseLong(vehicleId));
			List<TmVehiclePO> vehicleList = dao.select(tempVehiclePO);
			// Long dealerId = ((TmVehiclePO) vehicleList.get(0)).getDealerId();

			// Long matchId = Long.parseLong(map.get("MATCH_ID").toString());
			// Long matchId = Long.parseLong(map.get("MATCH_ID").toString());

			// 2、向“经销商验收(TT_INSPECTION)”写入验收基本信息
			TtVsInspectionPO inspectionPO = new TtVsInspectionPO();
			String oemComId = logonUser.getOemCompanyId();
			inspectionPO.setCompanyId(Long.parseLong(oemComId));
			Long inspectionId = Long.parseLong(SequenceManager.getSequence(""));
			inspectionPO.setInspectionId(inspectionId); // 验收ID
			inspectionPO.setVehicleId(Long.parseLong(vehicleId)); // 车辆ID
			inspectionPO.setArriveDate(format.parse(arrive_date)); // 到达日期
			inspectionPO.setArriveTime(arrive_time); // 到达时间
			inspectionPO.setTransportPerson(transport_person.trim()); // 送车人
			inspection_no = "YS" + inspectionId.toString(); // 验收单号：YS+验收ID
			inspectionPO.setInspectionNo(inspection_no.trim()); // 验收单号
			inspectionPO.setInspectionPerson(inspection_person.trim()); // 验收人
			inspectionPO.setVehicleArea(vehicle_area.trim()); // 车辆位置
			inspectionPO.setOperateDealer(Long.parseLong(dealerId)); // 收货方
			if (null != remark && !"".equals(remark)) {
				inspectionPO.setRemark(remark.trim()); // 备注
			}
			inspectionPO.setCreateBy(logonUser.getUserId());
			inspectionPO.setCreateDate(new Date());
			inspectionPO.setDlvryDtlId(new Long(dlvryDtlId)); // 发运明细id

			dao.insert(inspectionPO);

			// 验收成功,则修改发车ERP表中的验收信息

			TtVsDlvryErpDtlPO tvdedA = new TtVsDlvryErpDtlPO();
			TtVsDlvryErpDtlPO tvdedB = new TtVsDlvryErpDtlPO();
			tvdedA.setDetailId(new Long(dlvryDtlId));
			tvdedB.setDetailId(new Long(dlvryDtlId));
			tvdedB.setIsReceive(new Long(Constant.IS_RECEIVE_1));// 车辆是否验收,修改为已验收
			tvdedB.setUpdateDate(new Date());// 修改时间
			dao.update(tvdedA, tvdedB); // 修改配车验收状态

			// 验收后,修改对应发运明细表中的验收数量
			// List<TtVsDlvryMchPO> tvdmList = dao.select(tvdedA);

			/*
			 * 获取发运明细表中对应的发运id，并获取该发运表中汇总的发运数量（DELIVERY_AMOUNT）和验收数量（INSPECTION_AMOUNT）
			 * 若汇总后的DELIVERY_AMOUNT==INSPECTION_AMOUNT，则将发运表中的DELIVER_STATUS改为完全验收状态
			 * 若汇总后的DELIVERY_AMOUNT!=INSPECTION_AMOUNT，则将发运表中的DELIVER_STATUS改为部分验收状态
			 */

			// 3、如果有“止损描述”，向“经销商验收明细(TT_INSPECTION_DETAIL)”写入止损信息
			if (null != IS_DAMAGE && IS_DAMAGE.equals(Constant.IS_DAMAGE_1 + "")) {

				tempVehiclePO = new TmVehiclePO();
				tempVehiclePO.setVehicleId(Long.parseLong(vehicleId));

				valueVehiclePO = new TmVehiclePO();
				valueVehiclePO.setLockStatus(Constant.LOCK_STATUS_04);

				dao.update(tempVehiclePO, valueVehiclePO); // 完成车辆节点更新

				for (int i = 0; describes != null && i < describes.length; i++) {
					if (null != describes[i] && !"".equals(describes[i])) {
						TtVsInspectionDetailPO inspectionDetailPO = new TtVsInspectionDetailPO();
						Long detailId = Long.parseLong(SequenceManager.getSequence(""));
						inspectionDetailPO.setDetailId(detailId); // 明细ID
						inspectionDetailPO.setInspectionId(inspectionId); // 验收ID
						inspectionDetailPO.setDamagePart(part[i].trim()); // 损坏部位
						inspectionDetailPO.setDamageDesc(describes[i].trim()); // 损坏描述
						inspectionDetailPO.setCreateBy(logonUser.getUserId());
						inspectionDetailPO.setCreateDate(new Date());
						dao.insert(inspectionDetailPO);
					}
				}
			}

			/*
			 * 根据vehicleId查询该车辆的dealerId，并关联到订单表，查询订单的“定货方id”、“付款方id” 如果
			 * 车辆表的dealerId==定货方id && 车辆表的dealerId != 付款方id，说明此车为二级经销商订购
			 * 则系统自动生成“车辆调拨记录”： 调出方：付款方id 调入方：定货方id by Davey
			 */

			// 4、向TT_VS_VHCL_CHNG写入变更日志
			TtVsVhclChngPO chngPO = new TtVsVhclChngPO();
			Long vhclChangeId = Long.parseLong(SequenceManager.getSequence(""));
			chngPO.setVhclChangeId(vhclChangeId); // 改变序号
			chngPO.setVehicleId(Long.parseLong(vehicleId)); // 车辆ID
			chngPO.setOrgType(logonUser.getOrgType()); // 组织类型
			chngPO.setOrgId(logonUser.getOrgId()); // 组织ID
			chngPO.setDealerId(Long.parseLong(dealerId)); // 经销商ID
			chngPO.setChangeCode(Constant.STORAGE_CHANGE_TYPE); // 改变类型:库存状态更改
			chngPO.setChangeName(Constant.STORAGE_CHANGE_TYPE_01.toString()); // 改变名称:验收入库
			chngPO.setChangeDate(new Date()); // 改变时间
			if (null != remark && !"".equals(remark)) {
				chngPO.setChangeDesc(remark.trim()); // 改变描述
			}
			chngPO.setDocNo(inspection_no.trim()); // 相关单据号
			chngPO.setCreateDate(new Date()); // 记录创建日期
			chngPO.setCreateBy(logonUser.getUserId()); // 记录创建者
			dao.insert(chngPO);

			warehouseList();
			Long poseId = logonUser.getPoseId();
			Long comId = logonUser.getCompanyId();
			List<Map<String, Object>> areaList = MaterialGroupManagerDao.getDealerId(comId.toString(), poseId.toString());
			String dealerIds__ = "";
			String dealer__ = "";
			for (int i = 0; i < areaList.size(); i++) {
				dealer__ += areaList.get(i).get("DEALER_ID").toString() + ",";
				dealerIds__ += areaList.get(i).get("DEALER_ID").toString() + "," + areaList.get(i).get("AREA_ID").toString() + ",";
			}
			dealerIds__ = dealerIds__.substring(0, (dealerIds__.length() - 1));
			dealer__ = dealer__.substring(0, (dealer__.length() - 1));
			List<Map<String, Object>> list = CheckVehicleDAO.getSpecialDlr(dealer__);
			int flag = 0;
			if (null != list && list.size() > 0) { // 特殊经销商判断
				flag = 1;
			}

			String[] vId = { vehicleId };
			this.callOther(vId, remark, "13071002", dealerId, null);

			act.setOutData("flag", flag);
			act.setOutData("dealerIds__", dealerIds__);
			act.setForword(CheckVehicleInitUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "验收入库提交");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	public void checkAllSubmit() {
		AclUserBean logonUser = null;
		try {

			RequestWrapper request = act.getRequest();
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			String arrive_date = CommonUtils.checkNull(request.getParamValue("arrive_date")); // 实际到车日期
			String arrive_time = CommonUtils.checkNull(request.getParamValue("arrive_time")); // 实际到车时间
			String inspection_person = CommonUtils.checkNull(request.getParamValue("inspection_person")); // 验收人员
			String warehouseId = CommonUtils.checkNull(request.getParamValue("warehouse__")); // 车辆所在位置
			String remark = CommonUtils.checkNull(request.getParamValue("remark")); // 备注
			String[] dlvryDtlIds = request.getParamValues("dlvryDtlIds");
			String dealerId = logonUser.getDealerId();

			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

			if (null != dlvryDtlIds) {
				int len = dlvryDtlIds.length;

				for (int i = 0; i < len; i++) {
					System.out.println(dlvryDtlIds[i]);

					TtVsDlvryErpDtlPO ttVsDlvryErpDtlPO = new TtVsDlvryErpDtlPO();
					ttVsDlvryErpDtlPO.setDetailId(new Long(dlvryDtlIds[i]));
					ttVsDlvryErpDtlPO = (TtVsDlvryErpDtlPO) dao.select(ttVsDlvryErpDtlPO).get(0);

					// String vehicleId =vehicleIds[i];
					// 1、修改节点状态：经销商在库
					TmVehiclePO tempVehiclePO = new TmVehiclePO();
					tempVehiclePO.setVehicleId(ttVsDlvryErpDtlPO.getVehicleId());
					List vList = dao.select(tempVehiclePO);

					TmVehiclePO valueVehiclePO = new TmVehiclePO();
					valueVehiclePO.setLifeCycle(Constant.VEHICLE_LIFE_03);// 修改车辆生命周期：经销商在库
					valueVehiclePO.setDealerId(new Long(logonUser.getDealerId()));
					valueVehiclePO.setStorageDate(format.parse(arrive_date)); // 车辆验收日期
					valueVehiclePO.setVehicleArea(warehouseId.toString()); // 车辆所在位置，存放经销商仓库ID
					dao.update(tempVehiclePO, valueVehiclePO); // 完成车辆节点更新

					// 从车辆表中查询dealerID
					// TmVehiclePO tmVehiclePO = new TmVehiclePO();
					// tmVehiclePO.setVehicleId(Long.parseLong(vehicleId));

					// 2、向“经销商验收(TT_INSPECTION)”写入验收基本信息
					TtVsInspectionPO inspectionPO = new TtVsInspectionPO();
					Long inspectionId = Long.parseLong(SequenceManager.getSequence(""));
					String oemComId = logonUser.getOemCompanyId();
					inspectionPO.setCompanyId(Long.parseLong(oemComId));
					inspectionPO.setInspectionId(inspectionId); // 验收ID
					inspectionPO.setVehicleId(ttVsDlvryErpDtlPO.getVehicleId()); // 车辆ID
					inspectionPO.setArriveDate(format.parse(arrive_date)); // 到达日期
					inspectionPO.setArriveTime(arrive_time); // 到达时间
					String inspection_no = "YS" + inspectionId.toString(); // 验收单号：YS+验收ID
					inspectionPO.setInspectionNo(inspection_no.trim()); // 验收单号
					inspectionPO.setInspectionPerson(inspection_person.trim()); // 验收人
					inspectionPO.setVehicleArea(warehouseId.toString()); // 车辆位置，存放经销商仓库ID
					inspectionPO.setOperateDealer(Long.parseLong(dealerId)); // 收货方

					if (null != remark && !"".equals(remark)) {
						inspectionPO.setRemark(remark.trim()); // 备注
					}
					inspectionPO.setCreateBy(logonUser.getUserId());
					inspectionPO.setCreateDate(new Date());
					// inspectionPO.setMatchId(matchId); // 配车表id
					inspectionPO.setDlvryDtlId(ttVsDlvryErpDtlPO.getDetailId()); // 发运明细id

					dao.insert(inspectionPO);

					TtVsDlvryErpDtlPO tvdedA = new TtVsDlvryErpDtlPO();
					TtVsDlvryErpDtlPO tvdedB = new TtVsDlvryErpDtlPO();
					tvdedA.setDetailId(ttVsDlvryErpDtlPO.getDetailId());
					tvdedB.setDetailId(ttVsDlvryErpDtlPO.getDetailId());
					tvdedB.setIsReceive(new Long(Constant.IS_RECEIVE_1));// 车辆是否验收,修改为已验收
					tvdedB.setUpdateDate(new Date());// 修改时间
					dao.update(tvdedA, tvdedB); // 修改配车验收状态

					/*
					 * 根据vehicleId查询该车辆的dealerId，并关联到订单表，查询订单的“定货方id”、“付款方id” 如果
					 * 车辆表的dealerId==定货方id && 车辆表的dealerId != 付款方id，说明此车为二级经销商订购
					 * 则系统自动生成“车辆调拨记录”： 调出方：付款方id 调入方：定货方id by Davey
					 */

					// 3、向TT_VS_VHCL_CHNG写入变更日志
					TtVsVhclChngPO chngPO = new TtVsVhclChngPO();
					Long vhclChangeId = Long.parseLong(SequenceManager.getSequence(""));
					chngPO.setVhclChangeId(vhclChangeId); // 改变序号
					chngPO.setVehicleId(ttVsDlvryErpDtlPO.getVehicleId()); // 车辆ID
					chngPO.setOrgType(logonUser.getOrgType()); // 组织类型
					chngPO.setOrgId(logonUser.getOrgId()); // 组织ID
					chngPO.setDealerId(Long.parseLong(dealerId)); // 经销商ID
					chngPO.setChangeCode(Constant.STORAGE_CHANGE_TYPE); // 改变类型:库存状态更改
					chngPO.setChangeName(Constant.STORAGE_CHANGE_TYPE_01.toString()); // 改变名称:验收入库
					chngPO.setChangeDate(new Date()); // 改变时间
					if (null != remark && !"".equals(remark)) {
						chngPO.setChangeDesc(remark.trim()); // 改变描述
					}
					chngPO.setCreateDate(new Date()); // 记录创建日期
					chngPO.setCreateBy(logonUser.getUserId()); // 记录创建者
					dao.insert(chngPO);

					String[] vId = { ttVsDlvryErpDtlPO.getVehicleId() + "" };
					this.callOther(vId, remark, "13071002", dealerId, null);

				}

			}

			// act.setForword(CheckVehicleInitUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "验收车辆错误");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * @param inspectionPO
	 */
	private void callInspectionInterface2ERP(TtVsInspectionPO inspectionPO) {
		List<Object> ins = new LinkedList<Object>();
		ins.add(inspectionPO.getInspectionId().toString());

		List<Integer> outs = new LinkedList<Integer>();

		dao.callProcedure(" P_INSPECTIONDATA_TO_ERP", ins, outs);
	}

	/**
	 * @param logonUser
	 * @param remark
	 * @param ids__
	 * @param i
	 * @param dealerId
	 * @param inspection_no
	 * @param dealer_id
	 * @param order_org_id
	 * @param billing_org_id
	 * @param dealerList_J
	 * @param orderList_T
	 */
	private void autoTransfer(AclUserBean logonUser, String remark, String vehicleId, Long dealerId, String inspection_no, String dealer_id, String order_org_id, String billing_org_id, List<Map<String, Object>> dealerList_J, List<Map<String, Object>> orderList_T) {
		if (!billing_org_id.equals(order_org_id) /* && orderList_T.size()<1 */) { // 开票方不等于订货方
			TtVsVehicleTransferPO transferPO = new TtVsVehicleTransferPO();
			String transferId = SequenceManager.getSequence("");
			String transferNo = "PF" + transferId;
			transferPO.setTransferId(Long.parseLong(transferId)); // 调拨ID
			transferPO.setVehicleId(Long.parseLong(vehicleId)); // 车辆ID
			transferPO.setOutDealerId(Long.parseLong(billing_org_id)); // 调出经销商ID
			transferPO.setTransferDate(new Date()); // 调拨日期
			transferPO.setTransferReason("系统自动调拨"); // 调拨原因
			transferPO.setCheckStatus(Constant.DISPATCH_STATUS_03); // 审核状态
			transferPO.setInDealerId(Long.parseLong(order_org_id)); // 调入经销商ID
			transferPO.setTransferNo(transferNo);
			dao.insert(transferPO);

			/*
			 * if (dealerList_J.size()>0 && orderList_T.size()<1){//开票方是结算中心,并且订货方不是特殊经销商
			 * List<Long> ins = new LinkedList<Long>();
			 * ins.add(Long.parseLong(transferId)); dao.callProcedure("
			 * p_vehicle_transferdata_to_erp", ins, null);调用结算中心批发接口 }
			 */

			TtVsVhclChngPO chngPO_sys = new TtVsVhclChngPO();
			Long vhclChangeId_sys = Long.parseLong(SequenceManager.getSequence(""));
			chngPO_sys.setVhclChangeId(vhclChangeId_sys); // 改变序号
			chngPO_sys.setVehicleId(Long.parseLong(vehicleId)); // 车辆ID
			chngPO_sys.setOrgType(logonUser.getOrgType()); // 组织类型
			chngPO_sys.setOrgId(logonUser.getOrgId()); // 组织ID
			chngPO_sys.setDealerId(dealerId); // 经销商ID
			chngPO_sys.setChangeCode(Constant.STORAGE_CHANGE_TYPE); // 改变类型:库存状态更改
			chngPO_sys.setChangeName(Constant.STORAGE_CHANGE_TYPE_03.toString()); // 改变名称:验收入库
			chngPO_sys.setChangeDate(new Date()); // 改变时间
			if (null != remark && !"".equals(remark)) {
				chngPO_sys.setChangeDesc(remark.trim()); // 改变描述
			}
			chngPO_sys.setDocNo(inspection_no.trim()); // 相关单据号
			chngPO_sys.setCreateDate(new Date()); // 记录创建日期
			chngPO_sys.setCreateBy(logonUser.getUserId()); // 记录创建者
			dao.insert(chngPO_sys);

			TmVehiclePO tv = new TmVehiclePO();
			TmVehiclePO tv_ = new TmVehiclePO();
			tv_.setVehicleId(Long.parseLong(vehicleId));
			tv.setVehicleId(Long.parseLong(vehicleId)); // 车辆ID
			if (dealerList_J.size() > 0 && orderList_T.size() > 0) { // 开票方是结算中心,并且订货方是特殊经销商
				tv.setDealerId(Long.parseLong(billing_org_id)); // 将车的dealerId设置成开票方
			} else {
				tv.setDealerId(Long.parseLong(order_org_id)); // 将车的dealerId从开票方设置成采购方
			}

			dao.update(tv_, tv);
		}
	}

	public void openDlvryERP() {
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			RequestWrapper request = act.getRequest();
			String sendcarId = CommonUtils.checkNull(request.getParamValue("sendcarId"));

			TtVsDlvryErpPO dlvryErp = new TtVsDlvryErpPO();
			dlvryErp.setSendcarHeaderId(new BigDecimal(sendcarId));
			List<TtVsDlvryErpPO> dlvryErpList = dao.select(dlvryErp);

			if (null != dlvryErpList && dlvryErpList.size() > 0) {
				dlvryErp = dlvryErpList.get(0);
			}

			List<Map<String, Object>> dlvryErpDltList = CheckVehicleDAO.getInstance().getDlvryEROInfo(sendcarId);

			act.setOutData("dlvryErp", dlvryErp);
			act.setOutData("dlvryErpDltList", dlvryErpDltList);

			act.setForword(DlvryERPURL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "交接单信息查询错误");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	// -------------------------------------------------------- 本类通用方法
	// --------------------------------------------------//

	/**
	 * 根据车辆id获取对应经销商渠道的仓库
	 */
	public void getWareNew() {
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			RequestWrapper request = act.getRequest();

			String vehlId = CommonUtils.checkNull(request.getParamValue("vehlId"));
			// String wareType =
			// CommonUtils.checkNull(request.getParamValue("wareType_Z")) ;

			Map<String, Object> chkInfoMap = dao.getChkVehlInfo(vehlId);

			String orderId = chkInfoMap.get("ORDER_ORG_ID").toString();
			String billingId = chkInfoMap.get("BILLING_ORG_ID").toString();
			String receiverId = chkInfoMap.get("RECEIVER").toString();

			Integer specialFlag = chkSpecialDlr(receiverId);

			List<Map<String, Object>> wareList__A = null;

			String wareType = "0"; // 1表示入代销库，0表示入自有库
			// 如果收货方是特殊经销商，则需要将车放入结算中心代销库，代管经销商为收货方
			if (specialFlag == 1) {
				wareList__A = VehicleLocationChangeDAO.getDLRWare(billingId, receiverId);

				wareType = "1";
			} else { // 收货方不是特殊经销商
				wareList__A = VehicleLocationChangeDAO.getDLRWare(orderId, receiverId);

				if (!orderId.equals(receiverId)) {
					wareType = "1";
				}
			}

			act.setOutData("wareType", wareType);
			act.setOutData("wareList__A", wareList__A);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "获取仓库");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	public void getWareNew_SUZUKI() {
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			RequestWrapper request = act.getRequest();
			String dealerId = logonUser.getDealerId();

			String vehlId = CommonUtils.checkNull(request.getParamValue("vehlId"));

			// Map<String, Object> chkInfoMap =
			// dao.getChkVehlInfo_SUZUKI(vehlId);
			// String receiverId = chkInfoMap.get("RECEIVER").toString();

			List<Map<String, Object>> wareList__A = null;

			String wareType = "0"; // 1表示入代销库，0表示入自有库
			wareList__A = VehicleLocationChangeDAO.getDLRWare(dealerId);

			act.setOutData("wareList__A", wareList__A);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "获取仓库");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * 判断经销商是否特殊经销商，返回0表示不是特殊经销商，返回1表示是特殊经销商
	 * 
	 * @param dealerId
	 *            经销商id
	 * @return Integer
	 */
	public Integer chkSpecialDlr(String dealerId) {
		List<Map<String, Object>> speDrlList = dao.getSpecialDlr(dealerId);

		if (CommonUtils.isNullList(speDrlList)) {
			return 0;
		}

		return 1;
	}

	public void callOther(String[] vehicleId, String remark, String vType, String dealerId, String outDlrId) {
		StringBuffer vId = new StringBuffer("");

		for (int i = 0; i < vehicleId.length; i++) {
			if (vId.length() == 0) {
				vId.append(vehicleId[i]);
			} else {
				vId.append(",").append(vehicleId[i]);
			}
		}

		List<Map<String, Object>> vinList = dao.getVin(vId.toString());
		List<String> list = new ArrayList<String>();

		if (!CommonUtils.isNullList(vinList)) {
			int len = vinList.size();

			for (int i = 0; i < len; i++) {
				list.add(vinList.get(i).get("VIN").toString());
			}
		}

		OSB21 osb = new OSB21();

		if (CommonUtils.isNullString(outDlrId)) {
			outDlrId = "-1";
		}
		osb.sendData(list, remark, vType, Long.parseLong(dealerId), Long.parseLong(outDlrId));
	}
	/**
	 * FUNCTION : 实销信息上报:查询销售顾问列表
	 * 
	 * @param :
	 * @return :
	 * @throws :
	 *             LastUpdate : 2010-6-18
	 */
	public void tolookLogistics() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			String vin_id=act.getRequest().getParamValue("vin_id");
			//根据vin_id获取vin
			TmVehiclePO tv =new TmVehiclePO();
			tv.setVehicleId(new Long(vin_id));
			tv=(TmVehiclePO) dao.select(tv).get(0);
			String vin=tv.getVin();
			act.getSession().get(Constant.LOGON_USER);
			act.setOutData("vin", vin);
			act.setForword(tolookLogisticsURL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "实销信息上报:查询销售顾问列表");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * FUNCTION : 获取物流信息
	 * 
	 * @param :
	 * @return :
	 * @throws :
	 *             LastUpdate : 2010-6-18
	 */
	public void getLogisticsList() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = null;
		try {
			RequestWrapper request = act.getRequest();
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			act.getSession().get(Constant.LOGON_USER);
			String vin = CommonUtils.checkNull(request.getParamValue("vin"));
			Map<String,String> map=new HashMap<String,String>();
			map.put("vin", vin);
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
			PageResult<Map<String, Object>> ps = dao.getLogisticsList(map,Constant.PAGE_SIZE, curPage);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "实销信息上报:查询销售顾问列表结果展示");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}
}
