package com.infodms.dms.actions.repairOrder;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

import org.apache.log4j.Logger;

import com.infodms.dms.actions.claim.application.ClaimManualAuditing;
import com.infodms.dms.actions.common.FileUploadManager;
import com.infodms.dms.actions.util.CommonUtilActions;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.bean.TtAsActivityProjectBean;
import com.infodms.dms.bean.TtAsRepairOrderExtBean;
import com.infodms.dms.bean.TtAsWrForeapprovalBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.DateUtil;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.common.getCompanyId.GetOemcompanyId;
import com.infodms.dms.common.tag.BaseAction;
import com.infodms.dms.common.tag.BaseUtils;
import com.infodms.dms.common.tag.DaoFactory;
import com.infodms.dms.dao.claim.auditing.ClaimAuditingDao;
import com.infodms.dms.dao.claim.dealerClaimMng.ClaimBillMaintainDAO;
import com.infodms.dms.dao.report.serviceReport.ClaimReportDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.exception.UserException;
import com.infodms.dms.po.FsFileuploadPO;
import com.infodms.dms.po.TcCodePO;
import com.infodms.dms.po.TcPosePO;
import com.infodms.dms.po.TmBusinessAreaPO;
import com.infodms.dms.po.TmDealerInvoiceIntercalatePO;
import com.infodms.dms.po.TmDealerPO;
import com.infodms.dms.po.TmPtPartBasePO;
import com.infodms.dms.po.TmVehiclePO;
import com.infodms.dms.po.TmVehiclePinRequestPO;
import com.infodms.dms.po.TrRepairMergePO;
import com.infodms.dms.po.TtAccessoryDtlPO;
import com.infodms.dms.po.TtAsActivityPO;
import com.infodms.dms.po.TtAsRepairOrderBackupPO;
import com.infodms.dms.po.TtAsRepairOrderExtPO;
import com.infodms.dms.po.TtAsRepairOrderPO;
import com.infodms.dms.po.TtAsRepairOrderProblemPO;
import com.infodms.dms.po.TtAsRoAddItemPO;
import com.infodms.dms.po.TtAsRoLabourBean;
import com.infodms.dms.po.TtAsRoLabourPO;
import com.infodms.dms.po.TtAsRoOldRepairPartPO;
import com.infodms.dms.po.TtAsRoRepairPartBean;
import com.infodms.dms.po.TtAsRoRepairPartPO;
import com.infodms.dms.po.TtAsWrApplicationExtPO;
import com.infodms.dms.po.TtAsWrApplicationPO;
import com.infodms.dms.po.TtAsWrAuthinfoPO;
import com.infodms.dms.po.TtAsWrBackupListPO;
import com.infodms.dms.po.TtAsWrCompensationAppPO;
import com.infodms.dms.po.TtAsWrCompensationMoneyPO;
import com.infodms.dms.po.TtAsWrForeapprovalPO;
import com.infodms.dms.po.TtAsWrForeapprovalitemPO;
import com.infodms.dms.po.TtAsWrForeauthdetailPO;
import com.infodms.dms.po.TtAsWrGamePO;
import com.infodms.dms.po.TtAsWrKeyDesignPO;
import com.infodms.dms.po.TtAsWrMalfunctionBean;
import com.infodms.dms.po.TtAsWrMalfunctionPO;
import com.infodms.dms.po.TtAsWrMalfunctionPositionPO;
import com.infodms.dms.po.TtAsWrOtherfeePO;
import com.infodms.dms.po.TtAsWrOutrepairPO;
import com.infodms.dms.po.TtAsWrQamaintainPO;
import com.infodms.dms.po.TtAsWrQualityDamagePO;
import com.infodms.dms.po.TtAsWrRuleListPO;
import com.infodms.dms.po.TtAsWrVinPartRepairTimesvalPO;
import com.infodms.dms.po.TtAsWrVinRepairDaysPO;
import com.infodms.dms.po.TtAsWrVinRulePO;
import com.infodms.dms.po.TtAsWrWoorLevelPO;
import com.infodms.dms.po.TtCustomerPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.DateTimeUtil;
import com.infodms.dms.util.XHBUtil;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.dms.chana.vo.WarrantyPartVO;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.po3.POFactory;
import com.infoservice.po3.POFactoryBuilder;
import com.infoservice.po3.bean.PageResult;

public class RoMaintainMain extends BaseAction {
	private Logger logger = Logger.getLogger(RoMaintainMain.class);
	private final ClaimBillMaintainDAO dao = ClaimBillMaintainDAO.getInstance();
	private final ClaimAuditingDao daom = ClaimAuditingDao.getInstance();
	private final String RO_URL = "/jsp/repairorder/roMaintain.jsp";// 主页面（查询）
	private final String RO_URL_SPECAIL = "/jsp/repairorder/roBalanceMainspeical.jsp";// 特殊工单主页面（查询）
	private final String RO_DEL_URL = "/jsp/repairorder/roDeleteMaintain.jsp";// 维修工单删除主页面(查询)Iverson
	private final String RO_SHOP_URL = "/jsp/repairorder/roShopMaintain.jsp";// 主页面（查询）
	private final String RO_SHOP_DETAIL = "/jsp/repairorder/roShopDetail.jsp";// 经销商端主页面（查询）
	private final String RO_SHOP_DEALER = "/jsp/repairorder/roShopMainDealer.jsp";// 主页面（查询）
	private final String RO_SHOP_OEM = "/jsp/repairorder/roShopMainOEM.jsp";// 主页面（查询）
	private final String RO_ADD_URL = "/jsp/repairorder/roMaintainAdd.jsp";// 增加页面
	private final String RO_MODIFY_URL = "/jsp/repairorder/roMaintainModify.jsp";// 修改页面
	private final String RO_FORL_URL = "/jsp/repairorder/roMaintainForl.jsp";// 修改页面
	private final String RO_MODIFY_URL_ERROR = "/jsp/repairorder/roMaintainModify_error.jsp";// 问题工单上报修改
	private final String RO_BALANCE_URL = "/jsp/repairorder/roBalanceMaintain.jsp"; // 工单结算（查询）
	private final String RO_APPLY_URL = "/jsp/repairorder/roApplyMaintain.jsp"; // 工单结算（查询）
	private final String RO_BALANCE_DETAIL = "/jsp/repairorder/roBalanceDetail.jsp"; // 工单结算详细
	private final String RO_BALANCE_PRINT = "/jsp/repairorder/roBalancePrint.jsp"; // 工单打印
	private final String RO_BALANCE_DETAIL2 = "/jsp/repairorder/roBalanceDetail2.jsp"; // 工单结算详细
	private final String RO_DELETE_DETAIL = "/jsp/repairorder/roDeleteDetail.jsp"; // 工单结算详细
	private final String RO_BALANCE_FOREAPPROVAL = "/jsp/repairorder/roMainforeapproval.jsp"; // 预授权详细
	private final String CLAIM_BILL_MAINTAIN_DETAIL = "/jsp/claim/dealerClaimMng/3claimBillMaintainModify.jsp";// 预授权申请2
	private final String RO_DELETE_DETAIL0 = "/jsp/repairorder/roDeleteDetail2.jsp"; // 工单结算详细
	private final String VEHICLE_PIN_REPLY_QUERY = "/jsp/repairorder/vehiclePinReplyQuery.jsp"; // 车辆PIN回复查询
	private final String VEHICLE_PIN_REPLY_DETAIL = "/jsp/repairorder/vehiclePinReplyDetail.jsp"; // 车辆PIN明细查询
	private final String REPAIR_PART_DETAIL = "/jsp/repairorder/showRepairPartDetail.jsp"; // 工单结算详细(配件)
	private final String AUTHDETAIL_URL = "/jsp/repairorder/authDetail.jsp";// 审核记录明细
	private final String MAIN_PARTCODE_URL = "/jsp/repairorder/foucePartPer.jsp";
	private final String ADD_POSITION = "/jsp/repairorder/addPosition.jsp";
	private final String MAIN_POSITION_URL = "/jsp/repairorder/foucePosition.jsp";
	private final String MAIN_KD_URL = "/jsp/repairorder/fouceKd.jsp";
	private final String MAIN_POSITION_KD = "/jsp/repairorder/PositiontoKd.jsp";
	private final String MAIN_Base_POSITION = "/jsp/repairorder/BaseToPosition.jsp";
	private final String Base_IS_NEW = "/jsp/repairorder/BaseIsNew.jsp";
	private final String PART_WAR_TYEP_MODIFY = "/jsp/repairorder/foucePartModify.jsp";// 配件三包类型修改页面
	private final String FORE_APPLY_URL = "/jsp/repairorder/foreApplyMain.jsp";
	private final String MAL_MAIN_URL = "/jsp/repairorder/malFunctionMain.jsp";// 故障代码主页面
	private final String MAL_ADD_URL = "/jsp/repairorder/malFunctionAdd.jsp";// 故障代码维护新增/修改页面
	private final String QUD_MAIN_URL = "/jsp/repairorder/qudAreaMain.jsp";// 质损区域主页面
	private final String QUD_ADD_URL = "/jsp/repairorder/qudAreaAdd.jsp";// 质损区域修改新增页面
	private final String UPDATE_POSITION = "/jsp/repairorder/updatePosition.jsp";
	private final String PRINT = "/jsp/repairorder/roShopmainOEMPrint.jsp";
	private final String DEALER_INVOICE_INTERCALATE = "/jsp/repairorder/intercalate.jsp";
	private final String MAIN_URL_TO = "/jsp/repairorder/roMaintainQuery.jsp";
	private final String showGroupUrl = "/jsp/repairorder/showGroupUrl.jsp";// 物料组选择

	/**
	 * 
	 * @Title: roForward
	 * @Description: TODO(工单主页跳转)
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	public void roForward() {
		try {
			act.setForword(RO_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "工单维护");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}

	public void roBalanceDetailinfor() {
		Long provinceId = null;
		String phone = "";
		try {
			TtAsActivityPO activity = new TtAsActivityPO();
			String id = request.getParamValue("ID");
			String roNo = request.getParamValue("roNo");
			String type = request.getParamValue("type"); // 1,4为明细，2为结算，3为取消结算
			TtAsRepairOrderPO rp = new TtAsRepairOrderPO();
			rp.setRoNo(roNo);
			rp = (TtAsRepairOrderPO) dao.select(rp).get(0);
			id = rp.getId().toString();
			boolean flag = false;
			TtAsRepairOrderExtPO tawep = dao.queryRoById(id);

			TtAsWrApplicationPO tarop = new TtAsWrApplicationPO();
			tarop.setRoNo(roNo);
			List resList = daom.queryDealerById(Long.valueOf(tawep
					.getDealerId()));
			if (resList != null && resList.size() > 0) {
				TmDealerPO dealerPO = (TmDealerPO) resList.get(0);
				if (dealerPO.getPhone() != null) {
					phone = dealerPO.getPhone();
				}
				provinceId = dealerPO.getProvinceId();
			}
			List<TtAsRoRepairPartBean> partls = dao.queryRepairPart2(roNo, id); // 取配件信息
			// 取工时
			List<TtAsRoAddItemPO> otherls = dao.queryAddItem(roNo, id);// 取其他项目
			List<Map<String, Object>> detail = dao.authDetailVin(roNo);
			act.setOutData("detail", detail);

			act.setOutData("application", tawep);
			act.setOutData("partLs", partls);
			act.setOutData("otherLs", otherls);
			act.setForword(PRINT);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "工单维护");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}

	public void roForwardSpecial() {
		try {
			String id = request.getParamValue("ID");
			act.setOutData("id", id);
			act.setForword(RO_URL_SPECAIL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "工单维护");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * Iverson add with 2010-11-12
	 * 
	 * @Title: roDeleteForward
	 * @Description: TODO(工单删除主页面跳转)
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	public void roDeleteForward() {
		try {
			act.setForword(RO_DEL_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "工单删除");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}

	public void judePosition() {
		try {
			String POS_CODE = request.getParamValue("POS_CODE");
			String POS_NAME = request.getParamValue("POS_NAME");
			TtAsWrMalfunctionPositionPO positionPO = new TtAsWrMalfunctionPositionPO();
			positionPO.setPosCode(POS_CODE.toUpperCase());
			TtAsWrMalfunctionPositionPO positionPO2 = new TtAsWrMalfunctionPositionPO();
			positionPO2.setPosName(POS_NAME);
			if (dao.select(positionPO).size() == 0
					&& dao.select(positionPO2).size() == 0) {
				act.setOutData("success", "yes");
			}
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "判断部位是否存在失败");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}

	public void judeupdatePosition() {
		try {
			String POS_CODE = request.getParamValue("POS_CODE");
			String POS_NAME = request.getParamValue("POS_NAME");
			String POS_CODE1 = request.getParamValue("POS_CODE1");
			String POS_NAME1 = request.getParamValue("POS_NAME1");
			StringBuffer sql = new StringBuffer();
			sql.append(" select * from  ");
			sql.append(" (select t.pos_code as pos_code,t.pos_name as pos_name from tt_as_wr_malfunction_position t where (t.pos_code !='"
					+ POS_CODE1 + "' and t.pos_name !='" + POS_NAME1 + "')) M");
			sql.append(" where  M.pos_code='" + POS_CODE + "' or M.pos_name= '"
					+ POS_NAME + "'");
			List<TtAsWrMalfunctionPositionPO> list = dao.select(sql.toString(),
					null, TtAsWrMalfunctionPositionPO.class);
			if (list.size() == 0) {
				act.setOutData("success", "yes");
			}
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "判断部位是否存在失败");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}

	public void addposition() {
		try {
			String POS_CODE = request.getParamValue("POS_CODE");
			String POS_NAME = request.getParamValue("POS_NAME");
			String WR_MONTHS = request.getParamValue("WR_MONTHS");
			String WR_MILEAGE = request.getParamValue("WR_MILEAGE");
			String ATT_DAYS = request.getParamValue("ATT_DAYS");
			String ATT_MILEAGE = request.getParamValue("ATT_MILEAGE");
			long POS_ID = Utility.getLong(SequenceManager.getSequence(""));
			TtAsWrMalfunctionPositionPO positionPO = new TtAsWrMalfunctionPositionPO();
			positionPO.setPosId(POS_ID);
			positionPO.setPosCode(POS_CODE.toUpperCase());
			positionPO.setPosName(POS_NAME);
			positionPO.setWrMonths(Integer.parseInt(WR_MONTHS));
			positionPO.setWrMileage(Double.parseDouble(WR_MILEAGE));
			positionPO.setAttDays(Integer.parseInt(ATT_DAYS));
			positionPO.setAttMileage(Double.parseDouble(ATT_MILEAGE));
			positionPO.setVer(0);
			positionPO.setIsDel(0);
			positionPO.setCreateBy(getCurrDealerId());
			positionPO.setCreateDate(new Date());
			dao.insert(positionPO);
			act.setForword(MAIN_POSITION_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "部位新增失败");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}

	public void judeverDupRo() {
		try {
			String vin = request.getParamValue("vin");
			act.setOutData("namejdue", dao.frLevel(vin, "111111"));
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "判定三包出错");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * 
	 * @Title: roShopForward
	 * @Description: TODO(车厂端工单维护跳转)
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	@SuppressWarnings("unchecked")
	public void roShopForward() {
		String dutyType = loginUser.getDutyType();
		@SuppressWarnings("unused")
		String orgId = "";
		String flag = "1";
		try {
			if (dutyType.equals(Constant.DUTY_TYPE_LARGEREGION + "")) {
				flag = "2"; // 如果是大区用户 YH.2010.12.15
			}
			List provinces = this.getProvinceByUserId(getCurrDealerId()); // YH
			act.setOutData("flag", flag);
			act.setOutData("provinces", provinces);

			act.setForword(RO_SHOP_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "工单维护");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}

	public void updatePositioninit() {
		try {
			String id = request.getParamValue("id");
			TtAsWrMalfunctionPositionPO positionPO = new TtAsWrMalfunctionPositionPO();
			positionPO.setPosId(Long.parseLong(id));
			act.setOutData("positionPO", dao.select(positionPO).get(0));
			act.setForword(UPDATE_POSITION);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "修改部位初始化失败");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}

	public void updateposition() {
		try {
			String id = request.getParamValue("posId");
			String POS_CODE = request.getParamValue("POS_CODE");
			String POS_NAME = request.getParamValue("POS_NAME1");
			String WR_MONTHS = request.getParamValue("WR_MONTHS");
			String WR_MILEAGE = request.getParamValue("WR_MILEAGE");
			String ATT_DAYS = request.getParamValue("ATT_DAYS");
			String ATT_MILEAGE = request.getParamValue("ATT_MILEAGE");
			TtAsWrMalfunctionPositionPO positionPO = new TtAsWrMalfunctionPositionPO();
			positionPO.setPosId(Long.parseLong(id));

			TtAsWrMalfunctionPositionPO positionPO1 = new TtAsWrMalfunctionPositionPO();
			positionPO1.setWrMonths(Integer.parseInt(WR_MONTHS));
			positionPO1.setPosCode(POS_CODE);
			positionPO1.setPosName(POS_NAME);
			positionPO1.setWrMileage(Double.parseDouble(WR_MILEAGE));
			positionPO1.setAttMileage(Double.parseDouble(ATT_MILEAGE));
			positionPO1.setAttDays(Integer.parseInt(ATT_DAYS));
			positionPO1.setUpdateBy(getCurrDealerId());
			positionPO1.setUpdateDate(new Date());
			dao.update(positionPO, positionPO1);
			act.setForword(MAIN_POSITION_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "修改部位失败");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * 
	 * @Title: getProvinceByUserId
	 * @Description: TODO(用户可查询的省市)
	 * @param 用户ID
	 * @return List 返回类型
	 * @throws
	 */
	@SuppressWarnings({ "unchecked", "unchecked" })
	// YH 2010.12.16
	public List getProvinceByUserId(Long uid) {
		StringBuffer sql = new StringBuffer();
		sql.append("select ur.user_id,ur.region_code,r.region_name\n");
		sql.append(" from tc_user_region_relation ur ,tm_region r\n");
		sql.append(" where ur.region_code = r.region_code and ur.user_id = '"
				+ uid + "' \n");
		List list = dao.pageQuery(sql.toString(), null, dao.getFunName());
		return list;
	}

	public void postionDel() {
		try {
			String id = request.getParamValue("ID");
			TtAsWrMalfunctionPositionPO positionPO = new TtAsWrMalfunctionPositionPO();
			positionPO.setPosId(Long.parseLong(id));
			TtAsWrMalfunctionPositionPO positionPO1 = new TtAsWrMalfunctionPositionPO();
			positionPO1.setIsDel(1);
			dao.update(positionPO, positionPO1);
			TmPtPartBasePO basePO = new TmPtPartBasePO();
			basePO.setPosId(Long.parseLong(id));
			List<TmPtPartBasePO> list = dao.select(basePO);
			if (list != null && list.size() > 0) {
				for (TmPtPartBasePO base : list) {
					StringBuffer sb = new StringBuffer();
					sb.append("update Tm_Pt_Part_Base t set t.pos_id = null where t.part_id ="
							+ base.getPartId());
					dao.update(sb.toString(), null);
				}
			}
			act.setOutData("success", "yes");

		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "配件删除失败");
			act.setOutData("success", false);
			logger.error(loginUser, e1);
			act.setException(e1);
		}

	}

	/**
	 * 
	 * @Title: roShopForward
	 * @Description: TODO(经销商端工单维护跳转)
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	public void roViewForDealer() {

		try {
			act.setForword(RO_SHOP_DEALER);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "工单维护");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * 
	 * @Title: roShopForward
	 * @Description: TODO(经销商端工单维护跳转)
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	public void roViewForOem() {

		try {
			act.setForword(RO_SHOP_OEM);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "工单维护");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * 
	 * @Title: roShopDetail
	 * @Description: TODO(车厂端工单详细页面)
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	public void roShopDetail() {

		Long provinceId = null;
		String phone = "";
		try {
			TtAsActivityPO activity = new TtAsActivityPO();
			String id = request.getParamValue("ID");
			String roNo = request.getParamValue("roNo");
			String type = request.getParamValue("TYPE"); // 1为明细，2为结算，3为取消结算

			List resList = daom.queryDealerById(Utility.getLong(loginUser
					.getDealerId()));
			if (resList != null && resList.size() > 0) {
				TmDealerPO dealerPO = (TmDealerPO) resList.get(0);
				phone = dealerPO.getPhone();
				provinceId = dealerPO.getProvinceId();
			}
			TtAsRepairOrderExtPO tawep = dao.queryRoById(id);
			List<TtAsRoRepairPartBean> partls = dao.queryRepairPart2(null, id); // 取配件信息
			List<TtAsRoLabourBean> itemls = dao.queryRepairitem2(null, id); // 取工时
			List<TtAsRoAddItemPO> otherls = dao.queryAddItem(null, id);// 取其他项目
			act.setOutData("application", tawep);

			act.setOutData("OTHERFEE", getOtherfeeStr());
			act.setOutData("ACTIVITY", activity);
			act.setOutData("ID", id);
			act.setOutData("itemLs", itemls);
			act.setOutData("partLs", partls);
			act.setOutData("otherLs", otherls);
			act.setOutData("phone", phone);
			act.setOutData("type", type);
			act.setForword(RO_SHOP_DETAIL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "工单维护");
			logger.error(loginUser, e1);
			act.setException(e1);
		}

	}

	/**
	 * 
	 * @Title: shopApprove
	 * @Description: TODO(车厂端进行工单的授权审核通过和驳回)
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	public void shopApprove() {
		try {
			String id = request.getParamValue("ID");
			String type = request.getParamValue("TYPE"); // 1为通过，2为拒绝
			String auditContent = request.getParamValue("AUDIT_CONTENT");
			TtAsRepairOrderPO tarop = new TtAsRepairOrderPO();
			tarop.setId(Utility.getLong(id));
			List ls = dao.select(tarop);
			if (ls != null) {
				if (ls.size() > 0) {
					TtAsRepairOrderPO tarop0 = new TtAsRepairOrderPO();
					tarop0.setId(Utility.getLong(id));
					if ("1".equals(type)) {
						tarop0.setForlStatus(Constant.RO_FORE_02); // 工单预授权通过
					} else if ("2".equals(type)) {
						tarop0.setForlStatus(Constant.RO_FORE_03); // 工单预授权拒绝
					}
					tarop0.setAuditContent(auditContent); // 审核意见
					dao.update(tarop, tarop0);
					act.setOutData("success", true);
				} else {
					act.setOutData("success", false);
					act.setOutData("reason", true); // 工单没有生成，不能授权

				}
			} else {
				act.setOutData("success", false);
				act.setOutData("reason", true); // 工单没有生成，不能授权
			}
			act.setForword(RO_SHOP_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "质量信息");
			act.setOutData("success", false);
			act.setOutData("reason", false);
			logger.error(loginUser, e1);
			act.setException(e1);
		}

	}

	/**
	 * 
	 * @Title: roBalanceForward
	 * @Description: TODO(工单结算查询页面)
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	public void roBalanceForward() {
		try {
			String type = request.getParamValue("type");
			if (type != null && Integer.valueOf(type) == 4) {
				act.setForword(RO_URL);
			} else {
				act.setForword(RO_BALANCE_URL);
			}
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "工单维护");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * @Title: roBalanceForward
	 * @Description: TODO(工单结算查询打印页面)
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	public void roBalancePrint() {
		Long provinceId = null;
		String phone = "";
		try {
			TtAsActivityPO activity = new TtAsActivityPO();
			String id = request.getParamValue("ID");
			String roNo = request.getParamValue("roNo");
			String type = request.getParamValue("type"); // 1,4为明细，2为结算，3为取消结算

			boolean flag = false;
			TtAsRepairOrderExtPO tawep = dao.queryRoById(id);

			TtAsWrApplicationPO tarop = new TtAsWrApplicationPO();
			tarop.setRoNo(roNo);
			List resList = daom.queryDealerById(Long.valueOf(tawep
					.getDealerId()));
			TmDealerPO dealerPO = null;
			if (resList != null && resList.size() > 0) {
				dealerPO = (TmDealerPO) resList.get(0);
				if (dealerPO.getPhone() != null) {
					phone = dealerPO.getPhone();
				}
				provinceId = dealerPO.getProvinceId();
			}
			List<TtAsRoRepairPartBean> partls = dao.queryRepairPart2(null, id); // 取配件信息
			List<TtAsRoAddItemPO> otherls = dao.queryAddItem(null, id);// 取其他项目

			TtAsWrOutrepairPO outrepairPO = new TtAsWrOutrepairPO();
			outrepairPO.setRoNo(tawep.getRoNo());
			List<TtAsWrOutrepairPO> list = dao.select(outrepairPO);
			if (list.size() > 0) {
				outrepairPO = list.get(0);
			}
			StringBuffer sql = new StringBuffer();
			sql.append("SELECT c.CTM_NAME,c.MAIN_PHONE,c.ADDRESS from TT_DEALER_ACTUAL_SALES a ,TM_VEHICLE b ,TT_CUSTOMER c  where\n");
			sql.append("b.VIN = '"
					+ tawep.getVin()
					+ "' and a.VEHICLE_ID = b.VEHICLE_ID and a.CTM_ID = c.CTM_ID");
			List<TtCustomerPO> customerlist = dao.select(TtCustomerPO.class,
					sql.toString(), null);
			TtCustomerPO customerPO = null;
			if (customerlist.size() > 0) {
				customerPO = customerlist.get(0);
			}

			String length = "";
			List<Map<String, Object>> accessoryDtlList = dao
					.getAccessoryDtl(roNo);
			act.setOutData("accessoryDtlList", accessoryDtlList);
			if (accessoryDtlList.size() == 0) {
				length = "0";
				act.setOutData("length", length);
			}
			//====================zyw 2014-8-4 补偿费查询明细
			List<Map<String, Object>> compensationMoneyList = dao.getCompensationMoney(roNo);
			if (compensationMoneyList != null && compensationMoneyList.size() >= 0) {
				act.setOutData("compensationMoneyList", compensationMoneyList);
			} 
			//==============================
			act.setOutData("customerPO", customerPO);
			act.setOutData("application", tawep);
			act.setOutData("outrepairPO", outrepairPO);
			act.setOutData("partLs", partls);
			act.setOutData("dealerPO", dealerPO);
			act.setOutData("otherLs", otherls);
			act.setForword(RO_BALANCE_PRINT);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "工单维护");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * 
	 * @Title: roBalanceForward
	 * @Description: TODO(工单结算查询页面)
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	public void roViewForApply() {

		try {

			String type = request.getParamValue("type");
			if (type != null && Integer.valueOf(type) == 4) {
				act.setForword(RO_URL);
			} else {
				act.setForword(RO_APPLY_URL);
			}
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "工单维护");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * 
	 * @Title: roBalanceDetail
	 * @Description: TODO(工单结算详细)
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	public void roBalanceDetail() {

		Long provinceId = null;
		String phone = "";
		try {

			TtAsActivityPO activity = new TtAsActivityPO();
			String gdId = request.getParamValue("gdid");
			String id = request.getParamValue("ID");
			String flags = request.getParamValue("flags");
			String roNo = request.getParamValue("roNo");
			String claimNo = request.getParamValue("CLAIM_NO");
			String type = request.getParamValue("type"); // 1,4为明细，2为结算，3为取消结算
			SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			// 艾春 9.15 添加时区控制
			sf.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
			Date date = new Date();
			String now = sf.format(date);
			boolean flag = false;
			TtAsRepairOrderExtBean tawep = dao.queryRoById(id);
			if ("1".equals(type) || "4".equalsIgnoreCase(type)) {
				act.setOutData("showBalButton", false);
				act.setOutData("showCanButton", false);
				act.setOutData("flag", true);
				if (Utility.handleDate1(tawep.getForBalanceTime()) != null) {
					act.setOutData("now",
							Utility.handleDate1(tawep.getForBalanceTime()));
				} else {
					act.setOutData("now", "未结算");
				}
			} else if ("2".equals(type)) {
				act.setOutData("showBalButton", true);
				act.setOutData("showCanButton", false);
				act.setOutData("now", now);
			} else if ("3".equals(type)) {
				System.out.println(Utility.handleDate1(tawep
						.getForBalanceTime()));
				act.setOutData("now",
						Utility.handleDate1(tawep.getForBalanceTime()));
				TtAsWrApplicationPO tarop = new TtAsWrApplicationPO();
				tarop.setRoNo(roNo);
				act.setOutData("showBalButton", false);
				List<TtAsWrApplicationPO> ls = dao.select(tarop);
				if (ls != null) {
					if (ls.size() > 0) {
						flag = true;
						act.setOutData("showCanButton", false);
					} else {
						act.setOutData("showCanButton", true);
					}
				} else {
					act.setOutData("showCanButton", true);
				}
			}
			List resList = daom.queryDealerById(Long.valueOf(tawep
					.getDealerId()));
			if (resList != null && resList.size() > 0) {
				TmDealerPO dealerPO = (TmDealerPO) resList.get(0);
				if (dealerPO.getPhone() != null) {
					phone = dealerPO.getPhone();
				}
				provinceId = dealerPO.getProvinceId();
			}
			TtAsWrApplicationExtPO tawep2 = new TtAsWrApplicationExtPO();
			if (gdId != null && !("null").equals(gdId)) {
				tawep2 = dao.queryApplicationById(gdId);
			}
			List<TtAsRoRepairPartBean> partls = dao.queryRepairPart2(null, id); // 取配件信息
			List<TtAsRoLabourBean> itemls = dao.queryRepairitem2(null, id); // 取工时
			List<TtAsRoAddItemPO> otherls = dao.queryAddItem(null, id);// 取其他项目
			List<FsFileuploadPO> attachLs = dao.queryAttById(id);// 取得附件
			act.setOutData("application", tawep);

			int freeTimes = 0; // 结算页面的一个标识 0:允许 1：不允许 -->针对保养次数<=当前系统保养次数的情况
			// 如果是保养类型，则执行此次判断
			if (Constant.REPAIR_TYPE_04.equals(tawep.getRepairTypeCode())) {
				act.setOutData("flag_if", true);
				String vin = tawep.getVin();
				List<TmVehiclePO> car = dao.getCar(vin);
				if (car != null) {
					if (car.size() > 0) {
						try {
							freeTimes = car.get(0).getFreeTimes();
						} catch (Exception e) {
							logger.info("此车的保养次数字段未初始化，默认为0");
						}
					}
				}
			}
			String length = "";
			List<Map<String, Object>> accessoryDtlList = dao
					.getAccessoryDtl(tawep.getRoNo());
			if (accessoryDtlList.size() == 0) {
				length = "0";
				act.setOutData("length", length);
			}
			List<Map<String, Object>> workHourCodeMap = dao.getWorkhoursCode();
			act.setOutData("workHourCodeMap", workHourCodeMap);
			act.setOutData("accessoryDtlList", accessoryDtlList);
			//====================zyw 2014-8-4 补偿费查询明细
			List<Map<String, Object>> compensationMoneyList = dao.getCompensationMoney(tawep.getRoNo());
			if (compensationMoneyList != null && compensationMoneyList.size() >= 0) {
				act.setOutData("compensationMoneyList", compensationMoneyList);
			} 
			//==============================
			// 查询tc_code 8008 区分轿微车
			TcCodePO code = new TcCodePO();
			code.setType("8008");
			List list = dao.select(code);
			if (list.size() > 0)
				code = (TcCodePO) list.get(0);
			act.setOutData("code", code);
			act.setOutData("freeTimes", freeTimes);
			act.setOutData("OTHERFEE", getOtherfeeStr());
			// act.setOutData("ACTIVITYCOMBO", getActivityStr());
			act.setOutData("ACTIVITY", activity);
			act.setOutData("ID", id);
			act.setOutData("itemLs", itemls);
			act.setOutData("partLs", partls);
			act.setOutData("otherLs", otherls);
			act.setOutData("phone", phone);
			act.setOutData("setConsel", flag);
			act.setOutData("attachLs", attachLs);
			act.setOutData("type", type);
			act.setOutData("flags", flags);
			act.setOutData("tawep2", tawep2);
			String action = CommonUtils.checkNull(request
					.getParamValue("action"));
			if ("000".equals(type)) {
				act.setForword(RO_DELETE_DETAIL0);
			} else if (action.equals("detail")) {
				act.setForword(RO_BALANCE_DETAIL2);
			} else {
				act.setForword(RO_BALANCE_DETAIL);
			}

		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "工单维护");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}

	public void roBalanceDetail2() {

		Long provinceId = null;
		String phone = "";
		try {

			TtAsActivityPO activity = new TtAsActivityPO();
			String id = request.getParamValue("ID");
			String roNo = request.getParamValue("roNo");
			String type = request.getParamValue("type"); // 1为明细，2为结算，3为取消结算
			SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			// 艾春 9.15 添加时区控制
			sf.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
			Date date = new Date();
			String now = sf.format(date);
			if ("1".equals(type)) {
				act.setOutData("showBalButton", false);
				act.setOutData("showCanButton", false);
			} else if ("2".equals(type)) {
				// 判断登陆系统
				List<Map<String, Object>> ListCode = dao.queryTcCode();
				Integer code = Integer.valueOf(ListCode.get(0).get("CODE_ID")
						.toString());
				if (code == Constant.chana_wc) {

					// 检查工单配件是不是配件大类
					List<Map<String, String>> partCode = dao
							.getRepairOrderPartCheck(id);
					String checkFlag = CommonUtils.checkNull(request
							.getParamValue("checkFlag"));
					if (!"".equals(checkFlag)) {
						boolean flag = false;
						String flagStr = "0";
						for (int j = 0; j < partCode.size(); j++) {
							if (partCode.get(j).get("PART_TYPE_ID") == null) {
								flag = true;
								flagStr = "1";
								break;
							}
						}
						if (flag) {
							act.setOutData("flag", flag);
							act.setOutData("flagStr", flagStr);
							act.setOutData("partCode", partCode);
							act.setOutData("id", id);
							act.setOutData("roNo", roNo);
							act.setOutData("type", type);
							act.setForword(REPAIR_PART_DETAIL);
							return;
						} else {
						}
					}

				}
				act.setOutData("showBalButton", true);
				act.setOutData("showCanButton", false);
			} else if ("3".equals(type)) {
				TtAsWrApplicationPO tarop = new TtAsWrApplicationPO();
				tarop.setRoNo(roNo);
				act.setOutData("showBalButton", false);
				List<TtAsWrApplicationPO> ls = dao.select(tarop);
				if (ls != null) {
					if (ls.size() > 0) {
						act.setOutData("showCanButton", false);
					} else {
						act.setOutData("showCanButton", true);
					}
				} else {
					act.setOutData("showCanButton", true);
				}
			}
			List resList = daom.queryDealerById(Utility.getLong(loginUser
					.getDealerId()));
			if (resList != null && resList.size() > 0) {
				TmDealerPO dealerPO = (TmDealerPO) resList.get(0);
				if (dealerPO.getPhone() != null) {
					phone = dealerPO.getPhone();
				}
				provinceId = dealerPO.getProvinceId();
			}
			TtAsRepairOrderExtPO tawep = dao.queryRoById(id);
			List<TtAsRoRepairPartBean> partls = dao.queryRepairPart2(null, id); // 取配件信息
			List<TtAsRoLabourBean> itemls = dao.queryRepairitem2(null, id); // 取工时
			List<TtAsRoAddItemPO> otherls = dao.queryAddItem(null, id);// 取其他项目
			List<FsFileuploadPO> attachLs = dao.queryAttById(id);// 取得附件
			act.setOutData("application", tawep);

			int freeTimes = 0; // 结算页面的一个标识 0:允许 1：不允许 -->针对保养次数<=当前系统保养次数的情况
			// 如果是保养类型，则执行此次判断
			if (Constant.REPAIR_TYPE_04.equals(tawep.getRepairTypeCode())) {
				act.setOutData("flag_if", true);
				String vin = tawep.getVin();
				List<TmVehiclePO> car = dao.getCar(vin);
				if (car != null) {
					if (car.size() > 0) {
						try {
							freeTimes = car.get(0).getFreeTimes();
						} catch (Exception e) {
							logger.info("此车的保养次数字段未初始化，默认为0");
						}
					}
				}
			}
			String length = "";
			List<Map<String, Object>> accessoryDtlList = dao
					.getAccessoryDtl(roNo);

			if (accessoryDtlList.size() == 0) {
				length = "0";
				act.setOutData("length", length);
			}

			List<Map<String, Object>> workHourCodeMap = dao.getWorkhoursCode();
			act.setOutData("workHourCodeMap", workHourCodeMap);
			act.setOutData("accessoryDtlList", accessoryDtlList);
			//====================zyw 2014-8-4 补偿费查询明细
			List<Map<String, Object>> compensationMoneyList = dao.getCompensationMoney(roNo);
			if (compensationMoneyList != null && compensationMoneyList.size() >= 0) {
				act.setOutData("compensationMoneyList", compensationMoneyList);
			} 
			//==============================

			act.setOutData("freeTimes", freeTimes);
			act.setOutData("OTHERFEE", getOtherfeeStr());
			// act.setOutData("ACTIVITYCOMBO", getActivityStr());
			act.setOutData("ACTIVITY", activity);
			act.setOutData("ID", id);
			act.setOutData("itemLs", itemls);
			act.setOutData("partLs", partls);
			act.setOutData("otherLs", otherls);
			act.setOutData("attachLs", attachLs);
			act.setOutData("phone", phone);
			act.setOutData("type", type);
			act.setOutData("now", now);
			String action = CommonUtils.checkNull(request
					.getParamValue("action"));
			// 查询tc_code 8008 区分轿微车
			TcCodePO code = new TcCodePO();
			code.setType("8008");
			List list = dao.select(code);
			if (list.size() > 0)
				code = (TcCodePO) list.get(0);
			act.setOutData("code", code);
			if ("000".equals(type)) {
				act.setForword(RO_DELETE_DETAIL0);
			} else if (action.equals("detail"))
				act.setForword(RO_BALANCE_DETAIL2);
			else
				act.setForword(RO_BALANCE_DETAIL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "工单维护");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * Iverson add with 2010-11-12
	 * 
	 * @Title: roDeleteDetail
	 * @Description: TODO(工单删除详细页面)
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	public void roDeleteDetail() {

		Long provinceId = null;
		String phone = "";
		try {

			TtAsActivityPO activity = new TtAsActivityPO();
			String id = request.getParamValue("ID");
			String roNo = request.getParamValue("roNo");
			String type = request.getParamValue("type"); // 1为明细，2为结算，3为取消结算
			if ("1".equals(type)) {
				act.setOutData("showBalButton", false);
				act.setOutData("showCanButton", false);
			} else if ("2".equals(type)) {
				act.setOutData("showBalButton", true);
				act.setOutData("showCanButton", false);
			} else if ("3".equals(type)) {
				TtAsWrApplicationPO tarop = new TtAsWrApplicationPO();
				tarop.setRoNo(roNo);
				act.setOutData("showBalButton", false);
				List<TtAsWrApplicationPO> ls = dao.select(tarop);
				if (ls != null) {
					if (ls.size() > 0) {
						act.setOutData("showCanButton", false);
					} else {
						act.setOutData("showCanButton", true);
					}
				} else {
					act.setOutData("showCanButton", true);
				}
			}
			List resList = daom.queryDealerById(Utility.getLong(loginUser
					.getDealerId()));
			if (resList != null && resList.size() > 0) {
				TmDealerPO dealerPO = (TmDealerPO) resList.get(0);
				if (dealerPO.getPhone() != null) {
					phone = dealerPO.getPhone();
				}

				provinceId = dealerPO.getProvinceId();
			}
			TtAsRepairOrderExtPO tawep = dao.queryRoById(id);
			List<TtAsRoRepairPartBean> partls = dao.queryRepairPart2(null, id); // 取配件信息
			List<TtAsRoLabourBean> itemls = dao.queryRepairitem2(null, id); // 取工时
			List<TtAsRoAddItemPO> otherls = dao.queryAddItem(null, id);// 取其他项目
			List<FsFileuploadPO> attachLs = dao.queryAttById(id);// 取得附件
			/************** Iverson add 2010-11-14 此功能判断如果工单生成了索赔单就必须先删除索赔单再删除工单 *******************/
			TtAsRepairOrderPO po = new TtAsRepairOrderPO();
			po.setId(Long.parseLong(id));
			List rono = dao.getRoNoById(po);
			TtAsRepairOrderPO roNoValue = (TtAsRepairOrderPO) rono.get(0);
			long listRono = dao.queryTtAsWrApplication(roNoValue.getRoNo());
			/************** Iverson add 2010-11-14 此功能判断如果工单生成了索赔单就必须先删除索赔单再删除工单 end ***************/
			act.setOutData("application", tawep);
			int freeTimes = 0; // 结算页面的一个标识 0:允许 1：不允许 -->针对保养次数<=当前系统保养次数的情况
			// 如果是保养类型，则执行此次判断
			if (Constant.REPAIR_TYPE_04.equals(tawep.getRepairTypeCode())) {
				act.setOutData("flag_if", true);
				String vin = tawep.getVin();
				List<TmVehiclePO> car = dao.getCar(vin);
				if (car != null) {
					if (car.size() > 0) {
						try {
							freeTimes = car.get(0).getFreeTimes();
						} catch (Exception e) {
							logger.info("此车的保养次数字段未初始化，默认为0");
						}
					}
				}
			}
			// 查询tc_code 8008 区分轿微车
			TcCodePO code = new TcCodePO();
			code.setType("8008");
			List list = dao.select(code);
			if (list.size() > 0)
				code = (TcCodePO) list.get(0);
			act.setOutData("code", code);

			String length = "";
			List<Map<String, Object>> accessoryDtlList = dao
					.getAccessoryDtl(roNo);
			if (accessoryDtlList.size() == 0) {
				length = "0";
				act.setOutData("length", length);
			}
			List<Map<String, Object>> workHourCodeMap = dao.getWorkhoursCode();
			act.setOutData("workHourCodeMap", workHourCodeMap);
			act.setOutData("accessoryDtlList", accessoryDtlList);
			//====================zyw 2014-8-4 补偿费查询明细
			List<Map<String, Object>> compensationMoneyList = dao.getCompensationMoney(roNo);
			if (compensationMoneyList != null && compensationMoneyList.size() >= 0) {
				act.setOutData("compensationMoneyList", compensationMoneyList);
			} 
			//==============================

			act.setOutData("freeTimes", freeTimes);
			act.setOutData("OTHERFEE", getOtherfeeStr());
			act.setOutData("ACTIVITY", activity);
			act.setOutData("ID", id);
			act.setOutData("itemLs", itemls);
			act.setOutData("partLs", partls);
			act.setOutData("otherLs", otherls);
			act.setOutData("attachLs", attachLs);
			act.setOutData("phone", phone);
			act.setOutData("type", type);
			/****************** Iverson add 2010-11-14 此功能判断如果工单生成了索赔单就必须先删除索赔单再删除工单 *****************************/
			act.setOutData("listRono", listRono);
			/****************** Iverson add 2010-11-14 此功能判断如果工单生成了索赔单就必须先删除索赔单再删除工单 end ****************************/
			act.setForword(RO_DELETE_DETAIL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "工单维护");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * Iverson add with 2010-12-07
	 * 
	 * @Title: roDeleteDetail
	 * @Description: TODO(查询根据工单ID看看这个工单是否有特殊费用)
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	public void roDeleteDetail1() {

		try {

			String id = request.getParamValue("id");
			String roNo = request.getParamValue("roNo");
			int count = daom.queryDealerById1(id);
			if (count > 0) {
				act.setOutData("ok", "ok");
			} else {
				act.setOutData("id", id);
				act.setOutData("roNo", roNo);
			}
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "工单维护");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * 
	 * @Title: roBalanceDetail
	 * @Description: TODO(预授权申请单明细与结算明细一样)
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	public void roBalanceDetail1() {

		Long provinceId = null;
		String phone = "";
		try {

			TtAsActivityPO activity = new TtAsActivityPO();
			String name = request.getParamValue("createName");
			String createName="";
			if(Utility.testString(name)){
				createName= new String(name.getBytes("ISO-8859-1"), "UTF-8");  
			}
			 
			String roNo = request.getParamValue("roNo");
			String fId = request.getParamValue("FID");// 获取预授权ID
			String type = request.getParamValue("type"); // 1为明细，2为审核
			if ("1".equals(type)) {
				act.setOutData("showBalButton", false);
				act.setOutData("showCanButton", false);
				act.setOutData("flag", true);
			} else if ("2".equals(type)) {
				act.setOutData("showBalButton", true);
				act.setOutData("showCanButton", true);
			}
			List<Map<String, Object>> list = dao.getID(roNo);
			String id = list.get(0).get("ID").toString();
			TtAsRepairOrderExtPO tawep = dao.queryRoById(id);
			
//			TtAsWrApplicationExtPO tawep2 = new TtAsWrApplicationExtPO();
//			if (gdId != null && !("null").equals(gdId)) {
//				tawep2 = dao.queryApplicationById(gdId);
//			}
			List resList = daom.queryDealerById(Long.valueOf(tawep
					.getDealerId()));
			if (resList != null && resList.size() > 0) {
				TmDealerPO dealerPO = (TmDealerPO) resList.get(0);
				phone = dealerPO.getPhone();
				provinceId = dealerPO.getProvinceId();
			}
			List<TtAsRoRepairPartBean> partls = dao.queryRepairPart2(null, id); // 取配件信息
			List<TtAsRoLabourBean> itemls = dao.queryRepairitem2(null, id); // 取工时
			List<TtAsRoAddItemPO> otherls = dao.queryAddItem(null, id);// 取其他项目
			List<FsFileuploadPO> attachLs = dao.queryAttById(fId);// 取得附件
			List<Map<String, Object>> listOpinion = dao.getOpinion(fId);
			String dealerId = loginUser.getDealerId();
			if (dealerId != null) {
				act.setOutData("flag", 1);
			} else {
				act.setOutData("flag", 0);
			}
			String length = "";
			List<Map<String, Object>> accessoryDtlList = dao
					.getAccessoryDtl(roNo);
			if (accessoryDtlList.size() == 0) {
				length = "0";
				act.setOutData("length", length);
			}
			List<Map<String, Object>> workHourCodeMap = dao.getWorkhoursCode();
			act.setOutData("workHourCodeMap", workHourCodeMap);
			
			//====================zyw 2014-8-4 补偿费查询明细
			List<Map<String, Object>> compensationMoneyList = dao.getCompensationMoney(roNo);
			if (compensationMoneyList != null && compensationMoneyList.size() >= 0) {
				act.setOutData("compensationMoneyList", compensationMoneyList);
			} 
			//==============================
			act.setOutData("accessoryDtlList", accessoryDtlList);
			act.setOutData("list3", listOpinion.get(0));
			act.setOutData("application", tawep);
			act.setOutData("OTHERFEE", getOtherfeeStr());
			// act.setOutData("ACTIVITYCOMBO", getActivityStr());
			act.setOutData("ACTIVITY", activity);
			act.setOutData("ID", id);
			act.setOutData("itemLs", itemls);
			act.setOutData("partLs", partls);
			act.setOutData("otherLs", otherls);
			act.setOutData("phone", phone);
			act.setOutData("attachLs", attachLs);
			act.setOutData("type", type);
			act.setOutData("fId", fId);
			act.setOutData("createName", createName);
			act.setOutData("isWarning", tawep.getIsWarning());
			act.setForword(RO_BALANCE_FOREAPPROVAL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "工单维护");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * 
	 * @Title: roBalance
	 * @Description: TODO(工单结算)
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	public void roBalance() {
		try {
			String[] PayType = request.getParamValues("PayType");
			String id = DaoFactory.getParam(request, "ID");
			String freeTimes = DaoFactory.getParam(request, "freeTimes");
			TtAsRepairOrderPO tarop = new TtAsRepairOrderPO();
			tarop.setId(Utility.getLong(id));
			
			List<TtAsRepairOrderPO> listData = dao.select(tarop);//查询查出来的工单数据
			String RepairTypeCode = listData.get(0).getRepairTypeCode();
			TtAsRepairOrderPO taropUp = new TtAsRepairOrderPO();
			String date = DaoFactory.getParam(request, "BALANCE_DATE");//工单结算时间 第一次结算时设置为当前时间 （taropUp）
			Date forBalanceTime = listData.get(0).getForBalanceTime() ;
			if (forBalanceTime== null || "".equalsIgnoreCase(forBalanceTime.toString())) {
				taropUp.setForBalanceTime(Utility.parseString2DateTime(date, "yyyy-MM-dd HH:mm"));// 结算时间 第一次
			}else{
				taropUp.setForBalanceTime(forBalanceTime); //设置 为 第一次结算的时间
			}
			taropUp.setId(Utility.getLong(id));
			taropUp.setRoStatus(Constant.RO_STATUS_02); // 以结算
			//taropUp1.get(0).setForBalanceTime(Utility.parseString2DateTime(date, "yyyy-MM-dd HH:mm"));
			listData.get(0).setRoStatus(Constant.RO_STATUS_02);
			taropUp.setVer(listData.get(0).getVer() + 1);// 将版本号加一
			taropUp.setPrintRoTime(listData.get(0).getCreateDate());

			// 如果是保养，则将保养次数更新到vehicle表
			if (Constant.REPAIR_TYPE_04.equalsIgnoreCase(listData.get(0)
					.getRepairTypeCode())) {
				TmVehiclePO pv = new TmVehiclePO();
				TmVehiclePO v = new TmVehiclePO();
				TmVehiclePO pv2 = new TmVehiclePO();
				pv.setVin(listData.get(0).getVin());
				v.setVin(listData.get(0).getVin());

				/***** add by liuxh 20131108判断车架号不能为空 *****/
				CommonUtils.jugeVinNull(listData.get(0).getVin());
				/***** add by liuxh 20131108判断车架号不能为空 *****/

				v = (TmVehiclePO) dao.select(v).get(0);
				if (Integer.parseInt(freeTimes) >= v.getFreeTimes()) {// 如果当前的次数
					pv2.setFreeTimes(Integer.parseInt(freeTimes));
					dao.update(pv, pv2);
				}

			}
			List<Map<String, String>> partCode = dao
					.getRepairOrderPartCheck(id);
			boolean flag = false;
			for (int j = 0; j < partCode.size(); j++) {
				if (partCode.get(j).get("PART_TYPE_ID") == null) {
					flag = true;
					break;
				}
			}
			if (!flag) {
				taropUp.setPartFlag(1); // 属于配件大类
			} else {
				taropUp.setPartFlag(0); // 不属于配件大类
			}
			dao.update(tarop, taropUp);
			if (PayType != null && PayType.length > 0) {
				for (String PayType1 : PayType) {
					if (PayType1.equals("" + (int) Constant.PAY_TYPE_02)) {
						dao.vehicleJude(listData.get(0).getVin(), ""
								+ listData.get(0).getInMileage(), Utility
								.handleDate(listData.get(0).getRoCreateDate()),
								listData.get(0), loginUser);
						break;
					}
				}
			}
			boolean fage = true;
			String[] PayTypePart = request.getParamValues("PayTypePart");
			if (PayTypePart != null && PayTypePart.length > 0) {
				for (String PayTypePart1 : PayTypePart) {
					if (PayTypePart1.split("-")[0].equals(""
							+ (int) Constant.PAY_TYPE_02)) {
						dao.baseJude(PayTypePart, listData.get(0), loginUser);
						fage = false;
						break;
					}
				}
			}

			String[] BourPayType = request.getParamValues("BourPayType");
			if (fage) {
				if (BourPayType != null && BourPayType.length > 0) {
					for (String BourPayType1 : BourPayType) {
						if (BourPayType1.split("-")[0].equals(""
								+ (int) Constant.PAY_TYPE_02)) {
							dao.bourjude(BourPayType, listData.get(0),
									loginUser);
							break;
						}
					}
				}

			}
			act.setOutData("BALANCE_SUCCESS", "工单结算成功! 如需索赔,请及时编辑索赔单!");
			act.setForword(RO_BALANCE_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "工单维护");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * 
	 * @Title: roBalanceCancle
	 * @Description: TODO(取消结算)
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	@SuppressWarnings("unchecked")
	public void roBalanceCancle() {
		try {
			String id = request.getParamValue("ID");
			String freeTimes = request.getParamValue("freeTimes");
			TtAsRepairOrderPO tarop = new TtAsRepairOrderPO();
			tarop.setId(Utility.getLong(id));
			TtAsRepairOrderPO taropUp = new TtAsRepairOrderPO();
			taropUp.setId(Utility.getLong(id));
			taropUp.setRoStatus(Constant.RO_STATUS_01); // 未结算
			dao.update(tarop, taropUp);
			List<TtAsRepairOrderPO> list = dao.select(tarop);
			if (list != null && list.size() > 0) {
				tarop = list.get(0);
			}
			//dao.updateBalanceDate(id);

			// 如果是保养，则将保养次数更新到vehicle表, 取消结算时，将车辆表的保养次数更新为改工单的前一次次数
			if (Constant.REPAIR_TYPE_04.equalsIgnoreCase(list.get(0)
					.getRepairTypeCode())) {
				TmVehiclePO pv = new TmVehiclePO();
				TmVehiclePO pv3 = new TmVehiclePO();
				TmVehiclePO pv2 = new TmVehiclePO();
				pv.setVin(list.get(0).getVin());
				pv3.setVin(list.get(0).getVin());

				/***** add by liuxh 20131108判断车架号不能为空 *****/
				CommonUtils.jugeVinNull(list.get(0).getVin());
				/***** add by liuxh 20131108判断车架号不能为空 *****/

				pv2.setFreeTimes(Integer.parseInt(freeTimes) - 1);
				pv3 = (TmVehiclePO) dao.select(pv3).get(0);
				if (Integer.parseInt(freeTimes) >= pv3.getFreeTimes()) {// 如果系统中的保养次数大于此次保养次数
																		// 则不更新
					dao.update(pv, pv2);
				}
			}

			tarop.setRoStatus(Constant.RO_STATUS_01);

			String[] PayType = request.getParamValues("PayType");
			if (PayType != null) {
				for (String PayType1 : PayType) {
					if (PayType1.equals("" + (int) Constant.PAY_TYPE_02)) {
						dao.vehicleJude(tarop.getVin(),
								"" + tarop.getInMileage(),
								Utility.handleDate(tarop.getRoCreateDate()),
								tarop, loginUser);
						break;
					}
				}
			}
			boolean fage = true;
			String[] PayTypePart = request.getParamValues("PayTypePart");
			if (PayTypePart != null && PayTypePart.length > 0) {
				for (String PayTypePart1 : PayTypePart) {
					if (PayTypePart1.split("-")[0].equals(""
							+ (int) Constant.PAY_TYPE_02)) {
						dao.baseJude(PayTypePart, tarop, loginUser);
						fage = false;
						break;
					}
				}
			}

			String[] BourPayType = request.getParamValues("BourPayType");
			if (fage) {
				if (BourPayType != null && BourPayType.length > 0) {
					for (String BourPayType1 : BourPayType) {
						if (BourPayType1.split("-")[0].equals(""
								+ (int) Constant.PAY_TYPE_02)) {
							dao.bourjude(BourPayType, tarop, loginUser);
							break;
						}
					}
				}
			}
			act.setOutData("BALANCE_SUCCESS", "工单取消结算成功!");
			act.setForword(RO_BALANCE_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "工单维护");
			logger.error(loginUser, e1);
			act.setException(e1);
		}

	}

	/**
	 * 
	 * @Title: roAddForward
	 * @Description: TODO(工单新增跳转)
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	public void roAddForward() {

		String dealerId = loginUser.getDealerId();
		String phone;
		try {
			TmDealerPO tdp = new TmDealerPO();
			tdp.setDealerId(Utility.getLong(dealerId));
			List<TmDealerPO> lsd = dao.select(tdp);
			if (lsd != null) {
				if (lsd.size() > 0) {
					tdp = lsd.get(0);
				}
			}
			if (tdp.getPhone() == null || tdp.getPhone().equals("")) {
				phone = "";

			} else {
				phone = tdp.getPhone();
			}
			SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			Date date = new Date();
			String now = sf.format(date);

			// 查询tc_code 8008 区分轿微车
			TcCodePO code = new TcCodePO();
			code.setType("8008");
			List list = dao.select(code);
			if (list.size() > 0)
				code = (TcCodePO) list.get(0);
			act.setOutData("code", code);
			act.setOutData("roNo", "");
			act.setOutData("OTHERFEE", getOtherfeeStr());
			// act.setOutData("ACTIVITYCOMBO", getActivityStr());
			act.setOutData("dealerName", tdp.getDealerName());
			act.setOutData("dealerCode", tdp.getDealerCode());
			act.setOutData("phone", phone);
			act.setOutData("now", now);
			// act.setOutData("isScan", tdp.getIsScan());
			act.setOutData("isScan", 1);
			List<Map<String, Object>> workHoursCodelist = this.dao
					.getWorkhoursCode();
			act.setOutData("workHourCodeMap", workHoursCodelist);
			act.setForword(RO_ADD_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔单维护");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}

	public void roModifyForward1() {

		Long companyId = GetOemcompanyId.getOemCompanyId(loginUser); // 公司ID
		Long provinceId = null;
		String phone = "";

		try {

			TtAsActivityPO activity = new TtAsActivityPO();
			String id = request.getParamValue("ID");
			List resList = daom.queryDealerById(Utility.getLong(loginUser
					.getDealerId()));
			if (resList != null && resList.size() > 0) {
				TmDealerPO dealerPO = (TmDealerPO) resList.get(0);
				if (dealerPO.getPhone() != null) {
					phone = dealerPO.getPhone();
				}
				provinceId = dealerPO.getProvinceId();
			}
			TmDealerPO d = new TmDealerPO();
			d.setDealerId(Long.valueOf(loginUser.getDealerId()));
			d = (TmDealerPO) dao.select(d).get(0);
			TtAsRepairOrderExtPO tawep = dao.queryRoById(id);
			String roNo = tawep.getRoNo();
			List<TtAsRoRepairPartBean> partls = dao.queryRepairPart2(null, id); // 取配件信息
			List<TtAsRoLabourBean> itemls = dao.queryRepairitem2(null, id); // 取工时
			List<TtAsRoAddItemPO> otherls = dao.queryAddItem(null, id);// 取其他项目
			List<TtAsWrMalfunctionPO> malList = dao.getAllMalFunction();
			List<TtAsWrQualityDamagePO> quaList = dao.getAllQuality();
			List<FsFileuploadPO> attachLs = dao.queryAttById(id);// 取得附件
			request.setAttribute("MALFUNCTION", malList);
			request.setAttribute("QUALITY", quaList);
			request.setAttribute("dealerLevel", d.getDealerLevel());
			String remark2 = "";
			if (tawep.getRemark2() != null) {
				remark2 = tawep.getRemark2();
			}
			// 查询tc_code 8008 区分轿微车
			TcCodePO code = new TcCodePO();
			code.setType("8008");
			List list = dao.select(code);
			if (list.size() > 0)
				code = (TcCodePO) list.get(0);
			act.setOutData("code", code);
			act.setOutData("application", tawep);
			act.setOutData("remark2", remark2);
			if (tawep.getRepairTypeCode().equals(Constant.REPAIR_TYPE_02)
					|| tawep.getRepairTypeCode()
							.equals(Constant.REPAIR_TYPE_04)) {
				act.setOutData("flag", true);
			} else {
				act.setOutData("flag", false);
			}
			act.setOutData("OTHERFEE", getOtherfeeStr());

			String length = "";
			List<Map<String, Object>> accessoryDtlList = dao
					.getAccessoryDtl(roNo);
			if (accessoryDtlList != null && accessoryDtlList.size() > 0) {
				act.setOutData("accSize", accessoryDtlList.size());
			} else {
				length = "0";
				act.setOutData("length", length);
				act.setOutData("accSize", 0);
			}
			//====================zyw 2014-8-4 补偿费查询明细
			List<Map<String, Object>> compensationMoneyList = dao.getCompensationMoney(roNo);
			if (compensationMoneyList != null && compensationMoneyList.size() >= 0) {
				act.setOutData("compensationMoneyList", compensationMoneyList);
			} 
			//==============================
			List<Map<String, Object>> workHourCodeMap = dao.getWorkhoursCode();

			act.setOutData("workHourCodeMap", workHourCodeMap);
			act.setOutData("accessoryDtlList", accessoryDtlList);

			// act.setOutData("ACTIVITYCOMBO", getActivityStr());
			act.setOutData("ACTIVITY", activity);
			act.setOutData("ID", id);
			act.setOutData("itemLs", itemls);
			act.setOutData("partLs", partls);
			act.setOutData("otherLs", otherls);
			act.setOutData("attachLs", attachLs);
			act.setOutData("phone", phone);
			TtAsWrApplicationPO ta1 = new TtAsWrApplicationPO();
			ta1.setRoNo(roNo);
			List lsapp = dao.select(ta1);
			if (lsapp != null) {
				// 工单在索赔单中存在不可修改，跳转到明细页面
				if (lsapp.size() > 0) {
					act.setOutData("exist", true);
					act.setForword(RO_BALANCE_DETAIL);
				} else {
					act.setOutData("exist", false);
					act.setForword(RO_MODIFY_URL);
				}
			} else {
				act.setOutData("exist", false);
				act.setForword(RO_MODIFY_URL);
			}
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔单维护");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * 
	 * @Title: roModifyForward
	 * @Description: TODO(工单修改跳转)
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	@SuppressWarnings("unchecked")
	public void roModifyForward() {

		Long companyId = GetOemcompanyId.getOemCompanyId(loginUser); // 公司ID
		Long provinceId = null;
		String phone = "";
		String remark2 = "";
		Integer dealerLevel = 0;
		try {

			TtAsActivityPO activity = new TtAsActivityPO();
			String commit = request.getParamValue("commit"); // 判断是不是问题工单的修改
			String id = request.getParamValue("ID");
			String type = request.getParamValue("type");// 等于4的话表示工单已结算返回时返回
														// 到工单首页
			List resList = daom.queryDealerById(Utility.getLong(loginUser
					.getDealerId()));
			// String phone="";
			if (resList != null && resList.size() > 0) {
				TmDealerPO dealerPO = (TmDealerPO) resList.get(0);
				if (dealerPO.getPhone() != null) {
					phone = dealerPO.getPhone();
				}
				dealerLevel = dealerPO.getDealerLevel();
				provinceId = dealerPO.getProvinceId();
			}
			TtAsRepairOrderExtBean tawep = dao.queryRoById(id);
			String vin = tawep.getVin();
			String codes = tawep.getCamCode();
			String roNo = tawep.getRoNo();
			String frLevel = dao.frLevel(vin, roNo);
			act.setOutData("frLevel", frLevel);
			String gs = "";
			String cl = "";
			List<TtAsActivityProjectBean> acList = ClaimBillMaintainDAO
					.getInstance().queryActivityOtherNew(codes,
							loginUser.getDealerId());
			if (acList != null && acList.size() > 0) {
				for (int i = 0; i < acList.size(); i++) {
					if (acList.get(i).getProCode() == 3537006) {
						gs = String.valueOf(acList.get(i).getAmount());
					} else if (acList.get(i).getProCode() == 3537007) {
						cl = String.valueOf(acList.get(i).getAmount());
					}
				}
				request.setAttribute("GS_DOWN", gs);
				request.setAttribute("CL_DOWN", cl);
			}
			List<TtAsRoRepairPartBean> partls = dao.queryRepairPart2(null, id); // 取配件信息
			List<TtAsRoLabourBean> itemls = dao.queryRepairitem2(null, id); // 取工时
			List<TtAsRoAddItemPO> otherls = dao.queryAddItem(null, id);// 取其他项目
			List<TtAsWrMalfunctionPO> malList = dao.getAllMalFunction();
			List<TtAsWrQualityDamagePO> quaList = dao.getAllQuality();

			List<FsFileuploadPO> attachLs = dao.queryAttById(id);// 取得附件
			request.setAttribute("MALFUNCTION", malList);
			request.setAttribute("QUALITY", quaList);
			act.setOutData("application", tawep);
			if (tawep.getRepairTypeCode().equals(Constant.REPAIR_TYPE_02)
					|| tawep.getRepairTypeCode()
							.equals(Constant.REPAIR_TYPE_04)) {
				act.setOutData("flag", true);
			} else {
				act.setOutData("flag", false);
			}
			if (tawep.getRemark2() != null) {
				remark2 = tawep.getRemark2();
			}
			TmVehiclePO tvp = new TmVehiclePO();
			tvp.setVin(tawep.getVin());

			/***** add by liuxh 20131108判断车架号不能为空 *****/
			CommonUtils.jugeVinNull(tawep.getVin());
			/***** add by liuxh 20131108判断车架号不能为空 *****/

			List<TmVehiclePO> tvps = dao.select(tvp);
			if (tvps.size() > 0) {
				tvp = tvps.get(0);
			}
			String length = "";
			List<Map<String, Object>> accessoryDtlList = dao.getAccessoryDtl(roNo);
			if (accessoryDtlList != null && accessoryDtlList.size() >= 0) {
				act.setOutData("accSize", accessoryDtlList.size());
			} else {
				length = "0";
				act.setOutData("length", length);
				act.setOutData("accSize", 0);
			}
			List<Map<String, Object>> workHourCodeMap = dao.getWorkhoursCode();
			act.setOutData("tvp", tvp); // 根据VIN带出车辆信息表中的车 YH
			act.setOutData("type", type);
			act.setOutData("OTHERFEE", getOtherfeeStr());
			// act.setOutData("ACTIVITYCOMBO", getActivityStr());
			act.setOutData("ACTIVITY", activity);
			act.setOutData("ID", id);
			act.setOutData("itemLs", itemls);
			act.setOutData("partLs", partls);
			act.setOutData("otherLs", otherls);
			act.setOutData("attachLs", attachLs);
			act.setOutData("phone", phone);
			act.setOutData("remark2", remark2);
			act.setOutData("dealerLevel", dealerLevel);
			act.setOutData("workHourCodeMap", workHourCodeMap);
			//====================zyw 2014-8-4 补偿费查询明细
			List<Map<String, Object>> compensationMoneyList = dao.getCompensationMoney(roNo);
			if (compensationMoneyList != null && compensationMoneyList.size() >= 0) {
				act.setOutData("compensationMoneyList", compensationMoneyList);
			} 
			//==============================
			act.setOutData("accessoryDtlList", accessoryDtlList);
			TtAsWrApplicationPO ta1 = new TtAsWrApplicationPO();
			ta1.setRoNo(roNo);
			List lsapp = dao.select(ta1);
			// 查询tc_code 8008 区分轿微车
			TcCodePO code = new TcCodePO();
			code.setType("8008");
			List list = dao.select(code);
			if (list.size() > 0)
				code = (TcCodePO) list.get(0);
			act.setOutData("code", code);
			if (null != commit && "1".equals(commit)) { // 如果是问题工单提交的修改 YH
														// 2011.7.4
				act.setOutData("exist", false);
				act.setForword(RO_MODIFY_URL_ERROR);
			} else {
				if (lsapp != null) {
					// 工单在索赔单中存在不可修改，跳转到明细页面
					if (lsapp.size() > 0) {
						act.setOutData("exist", true);
						act.setForword(RO_BALANCE_DETAIL);
					} else {
						act.setOutData("exist", false);
						act.setForword(RO_MODIFY_URL);
					}
				} else {
					act.setOutData("exist", false);
					act.setForword(RO_MODIFY_URL);
				}
			}
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔单维护");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}

	public void roModifyForlForward() {

		Long companyId = GetOemcompanyId.getOemCompanyId(loginUser); // 公司ID
		Long provinceId = null;
		String phone = "";
		String remark2 = "";
		try {

			TtAsActivityPO activity = new TtAsActivityPO();
			String commit = request.getParamValue("commit"); // 判断是不是问题工单的修改
			String id = request.getParamValue("ID");
			String type = request.getParamValue("type");// 等于4的话表示工单已结算返回时返回
														// 到工单首页
			List resList = daom.queryDealerById(Utility.getLong(loginUser
					.getDealerId()));
			// String phone="";
			if (resList != null && resList.size() > 0) {
				TmDealerPO dealerPO = (TmDealerPO) resList.get(0);
				if (dealerPO.getPhone() != null) {
					phone = dealerPO.getPhone();
				}
				provinceId = dealerPO.getProvinceId();
			}
			TtAsRepairOrderExtBean tawep = dao.queryRoById(id);
			String roNo = tawep.getRoNo();
			List<TtAsRoRepairPartBean> partls = dao.queryRepairPart2(null, id); // 取配件信息
			List<TtAsRoLabourBean> itemls = dao.queryRepairitem2(null, id); // 取工时
			List<TtAsRoAddItemPO> otherls = dao.queryAddItem(null, id);// 取其他项目
			act.setOutData("application", tawep);
			if (tawep.getRepairTypeCode().equals(Constant.REPAIR_TYPE_02)
					|| tawep.getRepairTypeCode()
							.equals(Constant.REPAIR_TYPE_04)) {
				act.setOutData("flag", true);
			} else {
				act.setOutData("flag", false);
			}
			if (tawep.getRemark2() != null) {
				remark2 = tawep.getRemark2();
			}
			TmVehiclePO tvp = new TmVehiclePO();
			tvp.setVin(tawep.getVin());

			/***** add by liuxh 20131108判断车架号不能为空 *****/
			CommonUtils.jugeVinNull(tawep.getVin());
			/***** add by liuxh 20131108判断车架号不能为空 *****/

			List<TmVehiclePO> tvps = dao.select(tvp);
			if (tvps.size() > 0) {
				tvp = tvps.get(0);
			}
			act.setOutData("tvp", tvp); // 根据VIN带出车辆信息表中的车 YH
			act.setOutData("type", type);
			act.setOutData("OTHERFEE", getOtherfeeStr());
			// act.setOutData("ACTIVITYCOMBO", getActivityStr());
			act.setOutData("ACTIVITY", activity);
			act.setOutData("ID", id);
			act.setOutData("itemLs", itemls);
			act.setOutData("partLs", partls);
			act.setOutData("otherLs", otherls);
			act.setOutData("phone", phone);
			act.setOutData("remark2", remark2);
			TtAsWrApplicationPO ta1 = new TtAsWrApplicationPO();
			ta1.setRoNo(roNo);
			List lsapp = dao.select(ta1);
			// 查询tc_code 8008 区分轿微车
			TcCodePO code = new TcCodePO();
			code.setType("8008");
			List list = dao.select(code);
			if (list.size() > 0)
				code = (TcCodePO) list.get(0);
			act.setOutData("code", code);
			if (null != commit && "1".equals(commit)) { // 如果是问题工单提交的修改 YH
														// 2011.7.4
				act.setOutData("exist", false);
				act.setForword(RO_MODIFY_URL_ERROR);
			} else {
				if (lsapp != null) {
					// 工单在索赔单中存在不可修改，跳转到明细页面
					if (lsapp.size() > 0) {
						act.setOutData("exist", true);
						act.setForword(RO_BALANCE_DETAIL);
					} else {
						act.setOutData("exist", false);
						act.setForword(RO_FORL_URL);
					}
				} else {
					act.setOutData("exist", false);
					act.setForword(RO_FORL_URL);
				}
			}
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔单维护");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * 
	 * @Title: roModifyForward
	 * @Description: TODO(工单修改跳转)
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	public void roModifyForward3() {

		Long companyId = GetOemcompanyId.getOemCompanyId(loginUser); // 公司ID
		Long provinceId = null;
		String phone = "";
		String remark2 = "";
		try {

			TtAsActivityPO activity = new TtAsActivityPO();
			String id = request.getParamValue("ID");
			String type = request.getParamValue("type");// 等于4的话表示工单已结算返回时返回
														// 到工单首页
			List resList = daom.queryDealerById(Utility.getLong(loginUser
					.getDealerId()));
			// String phone="";
			if (resList != null && resList.size() > 0) {
				TmDealerPO dealerPO = (TmDealerPO) resList.get(0);
				if (dealerPO.getPhone() != null) {
					phone = dealerPO.getPhone();
				}
				provinceId = dealerPO.getProvinceId();
			}
			TtAsRepairOrderExtBean tawep = dao.queryRoById(id);
			String roNo = tawep.getRoNo();
			List<TtAsRoRepairPartBean> partls = dao.queryRepairPart2(null, id); // 取配件信息
			List<TtAsRoLabourBean> itemls = dao.queryRepairitem2(null, id); // 取工时
			List<TtAsRoAddItemPO> otherls = dao.queryAddItem(null, id);// 取其他项目
			act.setOutData("application", tawep);
			if (tawep.getRepairTypeCode().equals(Constant.REPAIR_TYPE_02)
					|| tawep.getRepairTypeCode()
							.equals(Constant.REPAIR_TYPE_04)) {
				act.setOutData("flag", true);
			} else {
				act.setOutData("flag", false);
			}
			if (tawep.getRemark2() != null) {
				remark2 = tawep.getRemark2();
			}
			act.setOutData("type", type);
			act.setOutData("OTHERFEE", getOtherfeeStr());
			// act.setOutData("ACTIVITYCOMBO", getActivityStr());
			act.setOutData("ACTIVITY", activity);
			act.setOutData("ID", id);
			act.setOutData("itemLs", itemls);
			act.setOutData("partLs", partls);
			act.setOutData("otherLs", otherls);
			act.setOutData("phone", phone);
			act.setOutData("remark2", remark2);
			TtAsWrApplicationPO ta1 = new TtAsWrApplicationPO();
			ta1.setRoNo(roNo);
			List lsapp = dao.select(ta1);
			act.setForword(CLAIM_BILL_MAINTAIN_DETAIL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔单维护");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * 
	 * @Title: getOtherfeeStr
	 * @Description: TODO(取得其他费用下拉框)
	 * @param
	 * @param type
	 * @param
	 * @return 设定文件
	 * @return String 返回类型
	 * @throws
	 */
	public String getOtherfeeStr() {
		ActionContext ctx = ActionContext.getContext();

		Long companyId = GetOemcompanyId.getOemCompanyId(loginUser); // 公司ID
		List<TtAsWrOtherfeePO> seriesList = dao.queryOtherFee(companyId);
		String retStr = "";
		retStr += "<option value=\'\'>-请选择-</option>";
		for (int i = 0; i < seriesList.size(); i++) {
			TtAsWrOtherfeePO bean = new TtAsWrOtherfeePO();
			bean = (TtAsWrOtherfeePO) seriesList.get(i);
			retStr += "<option value=\'" + bean.getFeeCode() + "\'"
					+ "title=\'" + bean.getFeeName() + "\'>"
					+ bean.getFeeCode() + "-" + bean.getFeeName() + "</option>";
		}
		return retStr;
	}

	@SuppressWarnings("unchecked")
	public void accredit() throws Exception {

		try {
			float labourHoursMain = 0f; // 基本工时数
			float reinForceHoursMain = 0f; // 附加工时数
			double stdHourAmount = 0; // 基本工时金额
			double reinHourAmount = 0; // 附加工时金额
			int partsCount = 0; // 总配件数
			double labourAmountMain = 0; // 总工时金额
			double partsAmountMain = 0; // 配件金额

			String remark = request.getParamValue("remark_ysq");// 预授权申请内容
			String yieldly = request.getParamValue("YIELDLY"); // 产地
			String brandCode = request.getParamValue("BRAND_CODE0"); // 品牌
			String modelCode = request.getParamValue("MODEL_CODE0"); // 车型
			String seriesCode = request.getParamValue("SERIES_CODE0");// 车系
			String engineNo = request.getParamValue("ENGINE_NO");
			String gearboxNo = request.getParamValue("GEARBOX_NO0"); // 变速箱
			String rearaxleNo = request.getParamValue("REARAXLE_NO0"); // 后桥
			String transferNo = request.getParamValue("TRANSFER_NO0"); // 分动器
			String dealerCode = request.getParamValue("DEALER_CODE");
			String roNo = request.getParamValue("RO_NO"); // 工单号
			String roId = request.getParamValue("ID");// 得到页面上的工单ID
			String roStartdate = request.getParamValue("RO_STARTDATE");// 工单开始时间
			String serveAdvisor = request.getParamValue("SERVE_ADVISOR");// 接待员
			String vin = request.getParamValue("VIN_FOR");// VIN
			String freeTimes = request.getParamValue("freeTimes");// 获取保养次数
			String claimType = request.getParamValue("repareType"); // 索赔类型

			String inMileage = request.getParamValue("IN_MILEAGE");

			String delivererPhone = request.getParamValue("DELIVERER_PHONE");
			String dealerId = request.getParamValue("dealerId");
			String delivererAdress = request.getParamValue("DELIVERER_ADRESS");
			String outLicenseno = request.getParamValue("OUT_LICENSENO");
			TtAsWrForeapprovalPO po = new TtAsWrForeapprovalPO();// 预授权申请单主表
			Long id = Utility.getLong(SequenceManager.getSequence(""));
			String code = SequenceManager.getSequence2("YSQ");

			String[] level = request.getParamValues("LEVELS");// 取得工时需要预授权的层级
			String[] partLevel = request.getParamValues("PARTLEVEL");// 获取配件预授权层级
			String[] partFore = request.getParamValues("PARTFORE");// 获取配件是否预授权
			String[] fore = request.getParamValues("FORE");// 获取工时是否需要预授权
			String[] itemPayType = request.getParamValues("PAY_TYPE_ITEM");// 付费方式
			String[] partPayType = request.getParamValues("PAY_TYPE_PART"); // 配件付费方式

			String[] hasPart = request.getParamValues("HAS_PART");// 配件是否有库存
			boolean flags = true;
			if (hasPart != null) {
				for (int o = 0; o < hasPart.length; o++) {
					if (Constant.IF_TYPE_NO.toString().equalsIgnoreCase(
							hasPart[o])) {
						flags = false;
						act.setOutData("success", "noPart");
						break;
					}
				}
			}

			/*TtAsWrForeapprovalPO f = new TtAsWrForeapprovalPO();
			if (roNo.equals("") || roNo == null) {
				throw new Exception("不能为空");
			}
			f.setRoNo(roNo);
			List<TtAsWrForeapprovalPO> list = dao.select(f);
			if (list != null && list.size() > 0) {
				flags = false;
				act.setOutData("success", "cover");
			}*/
			// boolean flag = false;
			// TtAsRoRepairPartPO p = new TtAsRoRepairPartPO();
			// TtAsRoLabourPO bp = new TtAsRoLabourPO();
			// p.setRoId(Long.valueOf(roId));
			// bp.setRoId(Long.valueOf(roId));
			// List<TtAsRoRepairPartPO> pList = dao.select(p);
			// List<TtAsRoLabourPO> lList = dao.select(bp);
			// if(pList.size()>0 && lList.size()>0){
			// for(int i=0;i<lList.size();i++){
			// flag = false;
			// for(int j=0;j<pList.size();j++){
			// System.out.println(lList.get(i).getWrLabourcode().equalsIgnoreCase(pList.get(j).getLabour()));
			// System.out.println(Constant.RESPONS_NATURE_STATUS_01.toString().equalsIgnoreCase(pList.get(j).getResponsNature().toString()));
			// if(lList.get(i).getWrLabourcode().equalsIgnoreCase(pList.get(j).getLabour())&&Constant.RESPONS_NATURE_STATUS_01.toString().equalsIgnoreCase(pList.get(j).getResponsNature().toString())){
			// flag =true ;
			// break;
			// }
			// }
			// if(!flag){
			// flags =false ;
			// act.setOutData("success", "noPoint");
			// break;
			// }
			// }
			// }

			if (flags) {

				po.setId(id);// 主键ID
				po.setRemark(remark);// 预授权信息
				po.setFoNo(code);
				po.setRoNo(roNo);
				po.setStartTime(Utility.getDate(roStartdate, 1));
				po.setVin(vin);
				po.setDealerId(Long.valueOf(dealerId));
				po.setLicenseNo(outLicenseno);
				po.setEngineNo(engineNo);
				po.setBrandCode(brandCode);
				po.setSeriesCode(seriesCode);
				po.setModelCode(modelCode);
				po.setGearboxNo(gearboxNo);
				po.setRearaxleNo(rearaxleNo);
				po.setTransferNo(transferNo);
				po.setYieldly(yieldly);
				po.setInMileage(Double.valueOf(inMileage));
				po.setDeliverer(delivererAdress);// 送修人
				po.setDelivererPhone(delivererPhone);// 送修电话
				po.setApprovalType(Integer.valueOf(claimType));// 申请类型
				po.setApprovalDate(new Date());// 申请时间
				po.setDestClerk(serveAdvisor);// 接待员
				po.setReportStatus(Constant.RO_FORE_01);// 状态
				po.setCreateDate(new Date());
				po.setCreateBy(getCurrDealerId());
				if (!freeTimes.equals("") || freeTimes != null) {
					po.setFreeTime(Integer.valueOf(freeTimes));
				}
				dao.insert(po);
				// 一定要将更新工单状态放在规则验证前
				dao.backOrderRono(roNo);

				RoDealerDqvCheck check = new RoDealerDqvCheck(dealerId, id,
						Long.valueOf(roId));
				check.execute();

				TtAsRepairOrderPO op = new TtAsRepairOrderPO();
				op.setId(Long.valueOf(roId));
				op = (TtAsRepairOrderPO) dao.select(op).get(0);// 规则执行完后，取得工单主表中的临时字段(需审核的层级)
				String levelCode = op.getCustomerDesc();
				TtAsWrForeapprovalPO fp = new TtAsWrForeapprovalPO();
				TtAsWrForeapprovalPO fp2 = new TtAsWrForeapprovalPO();
				fp2.setId(id);
				String foreLevel = "";
				if (fore != null) {
					for (int i = 0; i < fore.length; i++) {
						if ("10041001".equalsIgnoreCase(fore[i])
								&& "11801002".equalsIgnoreCase(itemPayType[i])) {
							foreLevel = foreLevel + "," + level[i];
						}
					}
				}
				if (partFore != null) {
					for (int i = 0; i < partFore.length; i++) {
						if ("10041001".equalsIgnoreCase(partFore[i])
								&& "11801002".equalsIgnoreCase(partPayType[i])) {
							foreLevel = foreLevel + "," + partLevel[i];
						}
					}
				}
				if (levelCode != null && !"".equalsIgnoreCase(levelCode)
						&& !" ".equalsIgnoreCase(levelCode)) {
					foreLevel = foreLevel + "," + levelCode;
				}// 将 规则验证得到的层级码,追加到所有需要审核的层级码后面

				String[] levelStr = foreLevel.split(",");
				List<String> dist = new ArrayList();
				List<Integer> dist2 = new ArrayList();
				for (int i = 0; i < levelStr.length; i++) {// 循环去重
					if (levelStr[i].length() > 0 && !dist.contains(levelStr[i])) {
						dist.add(levelStr[i]);
					}
				}
				int temp = 0;
				if (dist != null && dist.size() > 0) {// 将得到的层级码按由小到大排序
					temp = Integer.parseInt(dist.get(0));
					for (int i = 1; i < dist.size(); i++) {
						if (temp > Integer.parseInt(dist.get(i))) {
							dist2.add(Integer.parseInt(dist.get(i)));
						} else {
							dist2.add(temp);
							temp = Integer.parseInt(dist.get(i));
						}
					}
					dist2.add(temp);
				}
				String str = "";
				if (dist2 != null && dist2.size() > 0) {// 将排序后的集合拼接为字符串
					for (int i = 0; i < dist2.size(); i++) {
						str += dist2.get(i) + ",";
					}
				}
				if (str != "") {
					str = str.substring(0, str.length() - 1);
				}
				fp.setNeedAuditor(str);
				fp.setBalanceYieldly(op.getBalanceYieldly());
				if (dist2 != null && dist2.size() > 0) {
					fp.setCurrentAuditor(dist2.get(0).toString());
				} else {// 如果自动审核规则,以及监控工时，监控配件判断后的预授权层级码还是为空,但是需要预授权.则直接设置大区审核。如：保养，服务活动，外出维修
					// if(Constant.REPAIR_TYPE_04.equalsIgnoreCase(claimType)){
					// fp.setCurrentAuditor( "300");
					// fp.setNeedAuditor("300");
					// }else{
					// fp.setCurrentAuditor( "200");
					// fp.setNeedAuditor("200");
					// }
					fp.setCurrentAuditor("800");
					fp.setNeedAuditor("800");

				}
				dao.update(fp2, fp);// 最后更新预授权申请主表里面的当前审核人以及需要审核的层级码

				/*****
				 * 插入明细
				 */
				// 主工时

				String[] wrLabourCodes = request
						.getParamValues("WR_LABOURCODE"); // 获取工时代码
				String[] labourHours = request.getParamValues("LABOUR_HOURS"); // 获取工时数
				String[] labourAmounts = request
						.getParamValues("LABOUR_AMOUNT"); // 获取工时金额
				String[] partCodes = request.getParamValues("PART_CODE"); // 上件代码
				String[] prices = request.getParamValues("PRICE"); // 单价
				String[] amounts = request.getParamValues("QUANTITY"); // 数量
				String[] itemCodes = request.getParamValues("ITEM_CODE"); // 其他项目代码
				String[] itemAmounts = request.getParamValues("ITEM_AMOUNT");// 其他项目金额

				// 主工时插入
				if (wrLabourCodes != null) {
					for (int i = 0; i < wrLabourCodes.length; i++) {
						/* if (i == 0) { */
						// 主工时数量和金额
						labourHoursMain = Utility.getFloat(labourHours[i]);
						stdHourAmount = Utility.getDouble(labourAmounts[i]);
						// 累加总工时金额
						TtAsWrForeapprovalitemPO itemPo = new TtAsWrForeapprovalitemPO();
						itemPo.setFid(id);// 预授权主键ID
						itemPo.setId(Utility.getLong(SequenceManager
								.getSequence("")));
						itemPo.setRoNo(roNo);
						itemPo.setItemType(Constant.PRE_AUTH_ITEM_01);
						itemPo.setItemQuantity(Double.valueOf(labourHours[i]));
						itemPo.setItemAmount(stdHourAmount);
						itemPo.setDealerCode(dealerCode);
						itemPo.setCreateDate(new Date());
						itemPo.setCreateBy(getCurrDealerId());
						dao.insertTtAsWrForeapprovalitemPO(itemPo);
					}
				}
				if (partCodes != null) {
					for (int i = 0; i < partCodes.length; i++) {
						TtAsWrForeapprovalitemPO itemPo = new TtAsWrForeapprovalitemPO();
						itemPo.setFid(id);// 预授权主键ID
						itemPo.setId(Utility.getLong(SequenceManager
								.getSequence("")));
						itemPo.setRoNo(roNo);
						itemPo.setItemType(Constant.PRE_AUTH_ITEM_02);
						itemPo.setItemQuantity(Double.valueOf(amounts[i]));
						itemPo.setItemAmount(Double.valueOf(amounts[i])
								* Double.valueOf(prices[i]));
						itemPo.setDealerCode(dealerCode);
						itemPo.setCreateDate(new Date());
						itemPo.setCreateBy(getCurrDealerId());
						dao.insertTtAsWrForeapprovalitemPO(itemPo);
					}
				}
				// 其他项目插入
				if (itemCodes != null) {
					for (int i = 0; i < itemCodes.length; i++) {
						TtAsWrForeapprovalitemPO itemPo = new TtAsWrForeapprovalitemPO();
						itemPo.setFid(id);// 预授权主键ID
						itemPo.setId(Utility.getLong(SequenceManager
								.getSequence("")));
						itemPo.setRoNo(roNo);
						itemPo.setItemType(Constant.PRE_AUTH_ITEM_03);
						itemPo.setItemAmount(Double.valueOf(itemAmounts[i]));
						itemPo.setDealerCode(dealerCode);
						itemPo.setCreateDate(new Date());
						itemPo.setCreateBy(getCurrDealerId());
						dao.insertTtAsWrForeapprovalitemPO(itemPo);
					}
				}
				// 将工单上传的附件 复制到预授权下面
				dao.insetFile(getCurrDealerId(), po.getId(), Long.valueOf(roId));

				// 将工单的状态更新
				// roUpdate();
				act.setOutData("success", "true");
			}
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "预授权申请失败");
			act.setException(e1);
		}
	}

	/**
	 * 
	 * @Title: roInsert
	 * @Description: TODO(新增工单)
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	@SuppressWarnings("unchecked")
	public void roInsert() throws BizException {
		Long companyId = GetOemcompanyId.getOemCompanyId(super.loginUser);
		Date date = new Date();
		try {
			float labourHoursMain = 0f; // 基本工时数
			double stdHourAmount = 0; // 基本工时金额
			int partsCount = 0; // 总配件数
			double labourAmountMain = 0; // 总工时金额
			double partsAmountMain = 0; // 配件金额
			double netItemAmount = 0; // 其他项目金额
			double repairTotal = 0; // 索赔申请金额
			String TROUBLE_DESC = request.getParamValue("TROUBLE_DESC");// 故障描述
			String TROUBLE_REASON = request.getParamValue("TROUBLE_REASON");// 故障原因
			String REPAIR_METHOD = request.getParamValue("REPAIR_METHOD");// 故障措施
			String APP_REMARK = request.getParamValue("APP_REMARK");// 申请备注
			String ownerName = request.getParamValue("CTM_NAME_1");// 车主姓名
			String licenseNo = request.getParamValue("LICENSE_NO"); // 车牌号
			String brandCode = request.getParamValue("BRAND_CODE0"); // 品牌
			String modelCode = request.getParamValue("MODEL_CODE0"); // 车型
			String seriesCode = request.getParamValue("SERIES_CODE0");// 车系
			String quelityGrate = request.getParamValue("QUELITY_GRATE");// z质量等级
			String engineNo = request.getParamValue("ENGINE_NO");
			String dealerCode = request.getParamValue("DEALER_CODE");
			String roNo = CommonUtils.checkNull(request.getParamValue("RO_NO")); // 工单号
			String roEnddate = request.getParamValue("RO_ENDDATE");// 工单结束时间
			String guaranteeDate = request.getParamValue("GUARANTEE_DATE");// 购车日期
			String serveAdvisor = request.getParamValue("SERVE_ADVISOR");// 接待员
			String vin = request.getParamValue("VIN");// VIN
			String remark = request.getParamValue("APP_REMARK");// 申请备注
			String inMileage = request.getParamValue("IN_MILEAGE");
			String deliverer = request.getParamValue("DELIVERER");
			String delivererPhone = request.getParamValue("DELIVERER_PHONE");
			String delivererMobile = request.getParamValue("DELIVERER_MOBILE");
			String delivererAdress = request.getParamValue("DELIVERER_ADRESSS");
			String repairType = request.getParamValue("REPAIR_TYPE");
			String startTime = request.getParamValue("START_DATE");
			String endTime = request.getParamValue("END_DATE");
			String carUseType = request.getParamValue("car_use_type");
			// zhumingwei 2011-03-09
			String hh = request.getParamValue("hh");
			String mm = request.getParamValue("mm");
			String hhmm = startTime + " " + hh + ":" + mm;

			String hh1 = request.getParamValue("hh1");
			String mm1 = request.getParamValue("mm1");
			String hhmm1 = endTime + " " + hh1 + ":" + mm1;
			// zhumingwei 2011-03-09

			String outPerson = request.getParamValue("OUT_PERSON");
			String outSite = request.getParamValue("OUT_SITE");
			String outLicenseno = request.getParamValue("OUT_LICENSENO");
			String fromAdress = request.getParamValue("FROM_ADRESS");
			String endAdress = request.getParamValue("END_ADRESS");
			String outMileage = request.getParamValue("OUT_MILEAGE");
			String activityCode = request.getParamValue("ACTIVITYCOMBO");
			String campaignCode = request.getParamValue("CAMPAIGN_CODE"); // 服务活动代码
			String activityType = request.getParamValue("Activity_Type");
			String remark2 = request.getParamValue("remark_ysq");// 获取预授权申请内容

			String freeTimes = request.getParamValue("freeTimes"); // 免费保养次数
			String yiedlyType = request.getParamValue("YIELDLY_TYPE"); // 经销商选择的结算基地
			// 主工时
			String[] wrLabourCodes = request.getParamValues("WR_LABOURCODE"); // 获取工时代码
			String[] wrLabournames = request.getParamValues("WR_LABOURNAME"); // 获取工时名称
			String[] labourHours = request.getParamValues("LABOUR_HOURS"); // 获取工时数
			String[] labourPrices = request.getParamValues("LABOUR_PRICE"); // 获取工时单价
			String[] labourAmounts = request.getParamValues("LABOUR_AMOUNT"); // 获取工时金额
			String[] fore = request.getParamValues("FORE");// 获取工时是否需要预授权
			String[] level = request.getParamValues("LEVELS");// 取得需要预授权的层级
			String[] malFunction = request.getParamValues("MALFUNCTION");// 故障代码
			String[] itemPayType = request.getParamValues("PAY_TYPE_ITEM");// 付费方式
			// 附加工时
			String[] partFore = request.getParamValues("PARTFORE");// 获取配件是否预授权、
			String[] partLevel = request.getParamValues("PARTLEVEL");// 获取配件预授权层级
			String[] partCodes = request.getParamValues("PART_CODE"); // 上件代码
			String[] partNames = request.getParamValues("PART_NAME"); // 上件名称
			String[] quantitys = request.getParamValues("QUANTITY"); // 上件数量
			String[] prices = request.getParamValues("PRICE"); // 单价
			String[] amounts = request.getParamValues("AMOUNT"); // 数量
			String[] labour = request.getParamValues("Labour0");// 所属维修项目
			String[] nature = request.getParamValues("RESPONS_NATURE");// 质损性质
			String[] mainPartCode = request.getParamValues("mainPartCode");// 次因件关联主因件
			String[] partPayType = request.getParamValues("PAY_TYPE_PART"); // 配件付费方式
			String[] partUseType = request.getParamValues("PART_USE_TYPE");// 获取配件的使用类型
			String[] guaNotice = request.getParamValues("IS_GUA");
			/**
			 * 将故障描述，原因等转移到每个主因件上
			 */
			String[] troubleDesc = request.getParamValues("TROUBLE_DESCRIBE");//配件故障描述
			String[] troubleReason = request.getParamValues("TROUBLE_REASON");//配件故障原因
			String[] dealMethod = request.getParamValues("DEAL_METHOD");//配件维修措施
			
			
			String[] realPartId = request.getParamValues("REAL_PART_ID");// 获取选择的配件ID
			String[] itemCodes = request.getParamValues("ITEM_CODE"); // 其他项目代码
			String[] itemNames = request.getParamValues("ITEM_NAME"); // 其他项目名称
			String[] itemAmounts = request.getParamValues("ITEM_AMOUNT");// 其他项目金额
			String[] itemRemarks = request.getParamValues("ITEM_REMARK");// 备注
			String[] otherPayType = request.getParamValues("PAY_TYPE_OTHER"); // 其他付费方式
			String[] hasPart = request.getParamValues("HAS_PART");// 得到配件是否有库存
			String outMainPart = request.getParamValue("OUT_MAIN_PART");// 外派关联主因件
			String hasWr = request.getParamValue("hasWr");// 如果是第一次保养,是否在整车三包内
			// dealerCode = "S"+dealerCode.substring(1, dealerCode.length());
			String byAmount = request.getParamValue("byAmount");
			
			String[] zfRoNo = request.getParamValues("zfRoNo");//配件索赔的自费工单号
			
			if ("".equalsIgnoreCase(activityType) || activityType == null) {
				activityType = "0";
			}
			String str = "";
			String baoyang = "";
			int gree = 0;
			boolean WrFlag = true;
			String outNotice = "";
			if (Constant.REPAIR_TYPE_05.equalsIgnoreCase(repairType)
					&& itemCodes == null
					&& !(Constant.SERVICEACTIVITY_TYPE_05.toString())
							.equals(activityType)) {
				WrFlag = false;
				outNotice += "该活动没有任何项目,请修改!\n";
			}
			if (Constant.REPAIR_TYPE_02.equalsIgnoreCase(repairType)
					&& (outMainPart == null || "".equalsIgnoreCase(outMainPart) || "-1"
							.equalsIgnoreCase(outMainPart))) {
				WrFlag = false;
				outNotice += "外派救援请选择外派费用关联的主因件!\n";
			}
			if (!Utility.testString(yiedlyType)) {
				WrFlag = false;
				outNotice += "结算基地不能为空,请修改!\n";
			}
			// 判断该车是否已经做过PDI检测
			if (Constant.REPAIR_TYPE_08.equalsIgnoreCase(repairType)) {
				TtAsRepairOrderPO opp2 = new TtAsRepairOrderPO();
				opp2.setVin(vin);
				opp2.setRepairTypeCode(Constant.REPAIR_TYPE_08);
				List<TtAsRepairOrderPO> opList2 = dao.select(opp2);
				if (opList2 != null && opList2.size() > 0) {
					outNotice += "该车已经做过PDI检测,不能重复做!\n";
					WrFlag = false;
				}

				if (itemRemarks == null) {
					outNotice += "PDI必须填写项目备注!\n";
					WrFlag = false;
				}
			}
			// 判断是否还有工单未结算。
			TtAsRepairOrderPO opp = new TtAsRepairOrderPO();
			opp.setVin(vin);
			opp.setRoStatus(Constant.RO_STATUS_01);
			opp.setBalanceYieldly(Integer.parseInt(yiedlyType));
			List<TtAsRepairOrderPO> opList = dao.select(opp);
			if (opList != null && opList.size() > 0) {
				WrFlag = false;
				if (opList.get(0).getSecondDealerCode() == null
						|| opList.get(0).getSecondDealerCode() == "") {
					outNotice = outNotice + "该车有未结算的单据(服务站:"
							+ opList.get(0).getDealerCode() + " ,单号："
							+ opList.get(0).getRoNo() + "),不能开单!\n";

				} else {
					outNotice = outNotice + "该车有未结算的单据(服务站:"
							+ opList.get(0).getSecondDealerCode() + " ,单号："
							+ opList.get(0).getRoNo() + "),不能开单!\n";
				}
			}
			if (wrLabourCodes != null && partCodes == null) {
				outNotice += "有工时代码时,必须选择配件!\n";
				WrFlag = false;
			}
			if (wrLabourCodes == null && partCodes != null) {
				outNotice += "有配件时,必须选择作业代码!\n";
				WrFlag = false;
			}
			boolean flag = false;
			if (wrLabourCodes != null && partCodes != null) {
				for (int i = 0; i < wrLabourCodes.length; i++) {
					int temp = 0;
					flag = false;
					for (int j = 0; j < partCodes.length; j++) {
						if (wrLabourCodes[i].equalsIgnoreCase(labour[j])
								&& Constant.RESPONS_NATURE_STATUS_01.toString()
										.equalsIgnoreCase(nature[j])) {
							temp++;
						}
						if (wrLabourCodes[i].equalsIgnoreCase(labour[j])) {
							flag = true;
						}
					}
					if (temp > 1) {
						WrFlag = false;
						outNotice += "每一个工时最多对应一个主因件!\n";
						break;
					}
					if (!flag) {
						WrFlag = false;
						outNotice += "每个工时都必须对应一个配件!\n";
						break;
					}
				}
			}
			if (wrLabourCodes != null) {
				for (int i = 0; i < wrLabourCodes.length; i++) {
					for (int j = i + 1; j < wrLabourCodes.length; j++) {
						if (wrLabourCodes[i].equalsIgnoreCase(wrLabourCodes[j])) {
							WrFlag = false;
							outNotice += "一张工单不能添加相同的工时!\n";
							break;
						}
					}
				}
			}
			boolean resb = false;
			String partStr = "'";
			if (partCodes != null) {
				for (int i = 0; i < partCodes.length; i++) {
					for (int j = i + 1; j < partCodes.length; j++) {
						if (partCodes[i].equalsIgnoreCase(partCodes[j])) {
							outNotice += "一张工单不能添加相同的配件!\n";
							WrFlag = false;
							break;
						}
					}
					if (Constant.RESPONS_NATURE_STATUS_01.toString()
							.equalsIgnoreCase(nature[i])) {//如果是主因件，判断故障描述，故障原因，维修措施是否填写
						if("".equalsIgnoreCase(troubleDesc[i])){
							outNotice += "主因件必须填写故障描述!\n";
							WrFlag = false;
							break;
						}
						if("".equalsIgnoreCase(troubleReason[i])){
							outNotice += "主因件必须填写故障原因!\n";
							WrFlag = false;
							break;
						}
						if("".equalsIgnoreCase(dealMethod[i])){
							outNotice += "主因件必须填写维修措施!\n";
							WrFlag = false;
							break;
						}
						resb = true;
					}
					if (Constant.RESPONS_NATURE_STATUS_02.toString()
							.equalsIgnoreCase(nature[i])
							&& "".equalsIgnoreCase(mainPartCode[i])) {
						outNotice += "每一个次因件必须选择对应的主因件!\n";
						WrFlag = false;
						break;
					}
					partStr = partStr+partCodes[i]+"','";
				}
				if(!"".equalsIgnoreCase(partStr)){
					partStr = partStr.substring(0, partStr.length()-2);
				}
				if (!resb) {
					outNotice += "每次维修必须包含一个主因件!\n";
					WrFlag = false;
				}
			}
			/**
			 * 新增同一个VIN，里程，配件不能重复开单
			 */
			if(partStr.equals("'"))
			{
				partStr = partStr+"'";
			}
			
			List<Map<String,Object>> list = dao.isReport(vin,inMileage,partStr,"");
			if(list!=null&& list.size()>0){
				outNotice += "同一VIN,里程,配件只能开一次工单!\n";
				WrFlag = false;
			}
			if ((Constant.REPAIR_TYPE_07.equalsIgnoreCase(repairType)
					|| Constant.REPAIR_TYPE_02.equalsIgnoreCase(repairType)
					|| Constant.REPAIR_TYPE_01.equalsIgnoreCase(repairType)
					|| Constant.REPAIR_TYPE_03.equalsIgnoreCase(repairType) || Constant.REPAIR_TYPE_09.equalsIgnoreCase(repairType) || Constant.REPAIR_TYPE_06
						.equalsIgnoreCase(repairType))
					&& wrLabourCodes == null
					&& partCodes == null) {
				WrFlag = false;
				outNotice += "一般维修,外派服务,售前维修,特殊服务以及急件工单必须选择工时和配件!\n";
			}
			if (wrLabourCodes != null && partCodes != null) {
				for (int i = 0; i < wrLabourCodes.length; i++) {
					for (int j = 0; j < partCodes.length; j++) {
						if (wrLabourCodes[i].equalsIgnoreCase(labour[j])
								&& Integer.parseInt(partPayType[j]) == Constant.PAY_TYPE_02
								&& Integer.parseInt(itemPayType[i]) == Constant.PAY_TYPE_01) {
							WrFlag = false;
							outNotice += wrLabourCodes[i] + "工时选择自费时,配件不能索赔!\n";
							break;
						}
					}
				}
			}			
			// 根据得到的经销商代码进行查询售后经销商,如果为空,说明代码有问题，不能做工单
			TmDealerPO dd = new TmDealerPO();
			dd.setDealerCode(dealerCode);
			dd.setDealerType(Constant.DEALER_TYPE_DWR);
			List<TmDealerPO> dealerList1 = dao.select(dd);
			if (dealerList1 == null || dealerList1.size() < 1) {
				WrFlag = false;
				outNotice += "获取登录信息出错,请退出系统或者关闭浏览器后重新打开登录!\n";
			}
			TmDealerPO dd2 = new TmDealerPO();
			dd2.setDealerId(Long.valueOf(getCurrDealerId()));
			dd2.setDealerType(Constant.DEALER_TYPE_DWR);
			List<TmDealerPO> dealerList2 = dao.select(dd2);
			if (dealerList2 == null || dealerList1.size() < 1) {
				WrFlag = false;
				outNotice += "获取登录信息出错,请退出系统或者关闭浏览器后重新打开登录!\n";
			}

			TtAsWrWoorLevelPO lp = new TtAsWrWoorLevelPO();
			lp.setNum(Integer.parseInt(repairType));
			List<TtAsWrWoorLevelPO> lList = dao.select(lp);
			if (lList != null && lList.size() > 0) {
				lp = lList.get(0);
			}

			String appLevel = "";
			Long dealerId = super.getCurrDealerId();
			if (WrFlag) {
				int isClaimFore = 0;// 用于存储 需要索赔不需要预授权的字段
				int checkday = 999999;
				int accredit = 0;// 判断是否需要授权
				if (fore != null) {
					for (int i = 0; i < fore.length; i++) {
						if ("10041001".equalsIgnoreCase(fore[i])
								&& "11801002".equalsIgnoreCase(itemPayType[i])) {
							accredit = 1;
						}
					}
				}
				if (partFore != null) {
					for (int i = 0; i < partFore.length; i++) {
						if ("10041001".equalsIgnoreCase(partFore[i])
								&& "11801002".equalsIgnoreCase(partPayType[i])) {
							accredit = 1;
						}
					}
				}
				if (itemCodes != null && itemCodes.length > 0) {
					for (String itemCode : itemCodes) {
						if (itemCode.equals("QT001")) {
							accredit = 1;
							break;
						}
					}

				}

				if (partCodes != null) {
					for (int i = 0; i < partCodes.length; i++) {
						if (Constant.RESPONS_NATURE_STATUS_01.toString()
								.equalsIgnoreCase(nature[i])
								&& (Constant.PART_USE_TYPE_01
										.equals(partUseType[i]) || (!repairType.equals(Constant.REPAIR_TYPE_07))&&"0"
										.equalsIgnoreCase(quantitys[i]))) {// 如果是主因件，并且
																			// 为维修或者数量为0
							accredit = 1;
							appLevel = "800,";
							break;
						}
					}

				}
				// 预警预授权判定****************
				String codes = request.getParamValue("codes");
				String codes_type = request.getParamValue("codes_type");
				String labcodes = request.getParamValue("labcodes");
				String labcodes_type = request.getParamValue("labcodes_type");
				int isWarning = Constant.IF_TYPE_NO;//是否预警
				

				List<String> codelist = new ArrayList<String>();
				List<String> labcodelist = new ArrayList<String>();

				// 三包期判定--三包车辆维修时间
				TtAsWrVinRepairDaysPO daysPO = new TtAsWrVinRepairDaysPO();
				daysPO.setVin(vin);
				List<TtAsWrVinRepairDaysPO> vinlist = dao.select(daysPO);
				if (vinlist != null && vinlist.size() > 0) {
					daysPO = vinlist.get(0);
					int pepair = daysPO.getCurDays();
					String sql = "SELECT * from  TT_AS_WR_VIN_RULE t where t.VR_WARRANTY <= "
							+ pepair
							+ " and t.VR_TYPE = "
							+ Constant.VR_TYPE_1
							+ " order by t.VR_WARRANTY desc";
					List<TtAsWrVinRulePO> VinRulelist = dao.select(
							TtAsWrVinRulePO.class, sql, null);
					if (VinRulelist != null && VinRulelist.size() > 0) {
						if (!XHBUtil.IsNull(VinRulelist.get(0).getVrLevel())) {
							accredit = 1;
							isWarning = Constant.IF_TYPE_YES;
						}
					}
				}

				// 配件
				if (codes_type != null && codes_type.length() > 0
						&& codes != null && codes.length() > 0) {
					String[] code = codes.split(",");
					String[] code_type = codes_type.split(",");
					for (int i = 0; i < code_type.length; i++) {
						if (code_type[i].equals("" + Constant.PAY_TYPE_02)) {
							if (!"00-000".equalsIgnoreCase(code[i])) {
								codelist.add(code[i]);
							}
						}
					}
					List<TtAsWrVinPartRepairTimesvalPO> codeslist = dao
							.getCodes(vin, codelist);
					if (codeslist != null && codeslist.size() > 0) {
						for (TtAsWrVinPartRepairTimesvalPO trtp : codeslist) {
							if (!XHBUtil.IsNull(trtp.getNextLevel())) {
								accredit = 1;
								isWarning = Constant.IF_TYPE_YES;
								break;
							}
						}
					}
				}
				List<TtAsWrVinPartRepairTimesvalPO> codeslistnull = dao
						.getCodesNull(vin, codelist, 1);
				if (codeslistnull != null && codeslistnull.size() > 0) {
					for (TtAsWrVinPartRepairTimesvalPO trtp : codeslistnull) {
						if (!XHBUtil.IsNull(trtp.getNextLevel())) {
							accredit = 1;
							isWarning = Constant.IF_TYPE_YES;
							break;
						}
					}
				}

				// 工时
				if (labcodes_type != null && labcodes_type.length() > 0
						&& labcodes != null && labcodes.length() > 0) {
					String[] labcode = labcodes.split(",");
					String[] labcode_type = labcodes_type.split(",");
					for (int i = 0; i < labcode_type.length; i++) {
						if (labcode_type[i].equals("" + Constant.PAY_TYPE_02)) {
							labcodelist.add(labcode[i]);
						}
					}
					List<TtAsWrVinPartRepairTimesvalPO> labcodeslist = dao
							.getCodes(vin, labcodelist);
					if (labcodeslist != null && labcodeslist.size() > 0) {
						for (TtAsWrVinPartRepairTimesvalPO trtp : labcodeslist) {
							if (!XHBUtil.IsNull(trtp.getNextLevel())) {
								accredit = 1;
								isWarning = Constant.IF_TYPE_YES;
								break;
							}
						}
					}
				}
				List<TtAsWrVinPartRepairTimesvalPO> labcodeslistnull = dao
						.getCodesNull(vin, labcodelist, 2);
				if (labcodeslistnull != null && labcodeslistnull.size() > 0) {
					for (TtAsWrVinPartRepairTimesvalPO trtp : labcodeslistnull) {
						if (!XHBUtil.IsNull(trtp.getNextLevel())) {
							accredit = 1;
							isWarning = Constant.IF_TYPE_YES;
							break;
						}
					}
				}

				// 部位
				List<String> poslist = dao.getpart(codelist);
				if (poslist != null && poslist.size() > 0) {
					List<TtAsWrVinPartRepairTimesvalPO> posslist = dao
							.getCodes(vin, poslist);
					if (posslist != null && posslist.size() > 0) {
						for (TtAsWrVinPartRepairTimesvalPO trtp : posslist) {
							if (!XHBUtil.IsNull(trtp.getNextLevel())) {
								accredit = 1;
								isWarning = Constant.IF_TYPE_YES;
								break;
							}
						}
					}
				}
				List<TtAsWrVinPartRepairTimesvalPO> posslistnull = dao
						.getCodesNull(vin, poslist, 3);
				if (posslistnull != null && posslistnull.size() > 0) {
					for (TtAsWrVinPartRepairTimesvalPO trtp : posslistnull) {
						if (!XHBUtil.IsNull(trtp.getNextLevel())) {
							accredit = 1;
							isWarning = Constant.IF_TYPE_YES;
							break;
						}
					}
				}
				// 预警预授权判定结束****************

				// 判断VIN 和工单号是否为空
				if (Constant.REPAIR_TYPE_01.equals(repairType)) {// 一般维修
					startTime = null;
					endTime = null;
					outPerson = null;
					outSite = null;
					outLicenseno = null;
					fromAdress = null;
					endAdress = null;
					outMileage = null;
					campaignCode = null;
					freeTimes = null;
					roNo = Utility.GetBillNo("", loginUser.getDealerCode(),"SR");
				}else if (Constant.REPAIR_TYPE_09.equals(repairType)){//配件索赔
					campaignCode = null;
					freeTimes = null;
					roNo = Utility.GetBillNo("", loginUser.getDealerCode(),"PSR");
				} else if (Constant.REPAIR_TYPE_06.equals(repairType)) {// 特殊服务
					startTime = null;
					endTime = null;
					outPerson = null;
					outSite = null;
					outLicenseno = null;
					fromAdress = null;
					endAdress = null;
					outMileage = null;
					campaignCode = null;
					freeTimes = null;
					accredit = 1;
					appLevel += lp.getWoorLevel();
					// baoyang="该车超过整车三包期,需要预授权!";
					roNo = Utility.GetBillNo("", loginUser.getDealerCode(),
							"YR");
				} else if (Constant.REPAIR_TYPE_07.equals(repairType)) {// 急件
					startTime = null;
					endTime = null;
					outPerson = null;
					outSite = null;
					outLicenseno = null;
					fromAdress = null;
					endAdress = null;
					outMileage = null;
					campaignCode = null;
					freeTimes = null;
					accredit = 1;
					appLevel += lp.getWoorLevel();
					roNo = Utility.GetBillNo("", loginUser.getDealerCode(),
							"JR");
				} else if (Constant.REPAIR_TYPE_02.equals(repairType)) {// 外出维修
					campaignCode = null;
					freeTimes = null;
					roNo = Utility.GetBillNo("", loginUser.getDealerCode(),
							"WR");
				} else if (Constant.REPAIR_TYPE_03.equals(repairType)) {// 售前维修
					startTime = null;
					endTime = null;
					outPerson = null;
					outSite = null;
					outLicenseno = null;
					fromAdress = null;
					endAdress = null;
					outMileage = null;
					campaignCode = null;
					freeTimes = null;
					// accredit = 1;
					// accredit = 0;
					// appLevel = lp.getWoorLevel();
					roNo = Utility.GetBillNo("", loginUser.getDealerCode(),
							"TR");
				} else if (Constant.REPAIR_TYPE_04.equals(repairType)) {// 保养
					isClaimFore = 1;
					wrLabourCodes = null;
					partCodes = null;
					itemCodes = null;
					startTime = null;
					endTime = null;
					outPerson = null;
					outSite = null;
					outLicenseno = null;
					fromAdress = null;
					endAdress = null;
					outMileage = null;
					campaignCode = null;
					Date checkDate = new Date();
					if (Utility.testString(guaranteeDate)) {
						checkDate = Utility.getDate(guaranteeDate, 1);
					} else {
						// mileageDouble =Utility.getDouble(inMileage);
						TmVehiclePO tvp = new TmVehiclePO();
						tvp.setVin(vin);

						/***** add by liuxh 20131108判断车架号不能为空 *****/
						CommonUtils.jugeVinNull(vin);
						/***** add by liuxh 20131108判断车架号不能为空 *****/

						List<TmVehiclePO> lsv = dao.select(tvp);
						if (lsv != null) {
							if (lsv.size() > 0) {
								tvp = lsv.get(0);
							}
						}
						checkDate = tvp.getPurchasedDate(); // 保修改开始时间
					}
					Date now = new Date(); // 今天
					if (checkDate != null) {
						String formatStyle = "yyyy-MM-dd HH:mm";
						SimpleDateFormat df = new SimpleDateFormat(formatStyle);
						String d1 = df.format(checkDate);
						String d2 = df.format(now);
						// month = Utility.compareDate(d1,d2,1);
						// //取得今日和保养开始时间的插值
						checkday = Utility.compareDate1(d1, d2, 0); // 取得今日和保养开始时间的插值
																	// 天数
					}
					List<TtAsWrQamaintainPO> listFree = dao.getFree11(
							Integer.valueOf(freeTimes), companyId, 0, checkday,
							Double.valueOf(inMileage));
					if (Integer.parseInt(freeTimes) == 1) {
						if (listFree != null && listFree.size() > 0) {
							gree = 0;
						} else {
							appLevel += lp.getWoorLevel();
							accredit = 1;
							gree = 0;
							baoyang = "该车超过强保期,需要预授权!";
						}
					} else if (Integer.parseInt(freeTimes) > 1000) {
						isClaimFore = 0;
						if (listFree.size() == 0) {
							baoyang = "该车超过第 " + freeTimes + " 次定检期,需要预授权!";
							accredit = 1;
							appLevel += lp.getWoorLevel();
						} else {
							accredit = 0;
						}
					}
					roNo = Utility.GetBillNo("", loginUser.getDealerCode(),
							"QR");
				} else if (Constant.REPAIR_TYPE_08.equals(repairType)) {// PDI检测
					startTime = null;
					endTime = null;
					outPerson = null;
					outSite = null;
					outLicenseno = null;
					fromAdress = null;
					endAdress = null;
					outMileage = null;
					campaignCode = null;
					freeTimes = null;
					roNo = Utility.GetBillNo("", loginUser.getDealerCode(),
							"PR");
				} else if (Constant.REPAIR_TYPE_05.equals(repairType)) {// 服务活动
					isClaimFore = 1;
					freeTimes = null;
					startTime = null;
					endTime = null;
					outPerson = null;
					outSite = null;
					outLicenseno = null;
					fromAdress = null;
					endAdress = null;
					outMileage = null;
					roNo = Utility.GetBillNo("", loginUser.getDealerCode(),
							"HR");
				}

				roNo = roNo.toUpperCase();
				Long claimId = Utility.getLong(SequenceManager.getSequence("")); // 产生一个工单ID
				TtAsRepairOrderPO tawap = new TtAsRepairOrderPO();
				tawap.setIsWarning(isWarning);
				tawap.setTroubleDescriptions(TROUBLE_DESC);
				tawap.setTroubleReason(TROUBLE_REASON);
				tawap.setRemarks(APP_REMARK);
				tawap.setRepairMethod(REPAIR_METHOD);
				tawap.setCarUseType(carUseType == null ? 0 : Integer
						.parseInt(carUseType));
				tawap.setId(claimId);
				tawap.setCustomerDesc(appLevel);
				tawap.setBalanceYieldly(Integer.parseInt(yiedlyType));
				tawap.setVinIntype(Constant.IF_TYPE_YES);
				tawap.setIsCustomerInAsc(1);

				// 补偿费添加 zyw 2014-8-4==================================
				TtAsWrCompensationMoneyPO   compensationmoney = new TtAsWrCompensationMoneyPO();
				String[] supplier_code = request.getParamValues("supplier_code"); // 供应商代码
				String[] apply_price = request.getParamValues("apply_price"); // 获取工时名称
				String[] pass_price = request.getParamValues("pass_price"); // 获取金额
				String[] part_code = request.getParamValues("part_code_temp");
				double compensationMoney = 0d;
				if (supplier_code != null && supplier_code.length>0 ) {
					int temp=0;
					Date dateTemp = new Date();
					for (String supplierCode : supplier_code) {
						compensationmoney.setPkid(getSeq());
						compensationmoney.setApplyPrice(BaseUtils.ConvertDouble(apply_price[temp]));
						compensationmoney.setPassPrice(BaseUtils.ConvertDouble(pass_price[temp]));
						compensationmoney.setPartCode(part_code[temp]);
						compensationmoney.setSupplierCode(supplier_code[temp]);
						compensationmoney.setCreateDate(dateTemp);
						compensationmoney.setRoNo(roNo);
						compensationmoney.setPartName(null);
						compensationMoney+=BaseUtils.ConvertDouble(apply_price[temp]);//算出总的补偿费并设置到索赔单里
						dao.insert(compensationmoney);
						temp++;
					}
				}
				tawap.setCompensationMoney(compensationMoney);
				//==================================
				
				// 辅料
				TtAccessoryDtlPO accPo = new TtAccessoryDtlPO();
				String[] workhourCode = request
						.getParamValues("workHourCodeMap"); // 获取工时代码
				String[] workhourName = request.getParamValues("workhour_name"); // 获取工时名称
				String[] price = request.getParamValues("accessoriesPrice"); // 获取金额
				String[] accessoriesMainPartCode = request.getParamValues("accessoriesOutMainPart");
				double accPrice = 0d;
				if (workhourCode != null) {
					for (int i = 0; i < workhourCode.length; i++) {

						accPo.setRoNo(roNo);

						accPo.setWorkhourCode(workhourCode[i]);
						accPo.setWorkhourName(workhourName[i]);
						accPo.setPrice(Double.valueOf(price[i]));
						accPo.setMainPartCode(accessoriesMainPartCode[i]);

						accPo.setId(Long.parseLong(SequenceManager
								.getSequence("")));
						accPrice = accPrice + Double.valueOf(price[i]);
						dao.insert(accPo);
					}
				}

				tawap.setAccessoriesPrice(accPrice);
				// 将行驶里程插入tm_vehicle
				List<Map<String, Object>> listCount1 = dao.viewRoNoCount(roNo);
				if (Integer.valueOf(listCount1.get(0).get("COUNT").toString()) < 1) {
					TmVehiclePO po = new TmVehiclePO();
					TmVehiclePO po1 = new TmVehiclePO();
					po1.setVin(vin);
					po.setMileage(Double.valueOf(inMileage));
					po.setUpdateBy(getCurrDealerId());
					po.setUpdateDate(new Date());
					po.setLicenseNo(licenseNo);
					dao.updateMileage(po1, po);
				}

				/**
				 * 判断是否是一级经销商，如果不是则将经销商ID 改为一级经销商,同时保存二级经销商信息到工单表
				 */
				TmDealerPO dealer = new TmDealerPO();
				dealer.setDealerId(dealerId);
				dealer = (TmDealerPO) dao.select(dealer).get(0);
				System.out.println(dealer.getDealerLevel());
				System.out.println(Constant.DEALER_LEVEL_02 == dealer
						.getDealerLevel());
				if (Constant.DEALER_LEVEL_02.toString().equalsIgnoreCase(
						dealer.getDealerLevel().toString().trim())) {
					dealerId = dealer.getParentDealerD();
					TmDealerPO p2 = new TmDealerPO();
					p2.setDealerId(Long.valueOf(dealer.getParentDealerD()));
					p2 = (TmDealerPO) dao.select(p2).get(0);
					tawap.setDealerCode(p2.getDealerCode());
					tawap.setDealerId(p2.getDealerId()); // YH 2010.11.30

					TmDealerPO tmDealerPO = new TmDealerPO();
					tmDealerPO.setDealerId(Long.valueOf(dealerId));
					List<TmDealerPO> selectLists = dao.select(tmDealerPO);
					if (selectLists != null && selectLists.size() == 1) {
						tawap.setDealerName(selectLists.get(0).getDealerName());
						tawap.setDealerShortname(selectLists.get(0)
								.getDealerShortname());
					}

					tawap.setSecondDealerCode(dealer.getDealerCode());
					tawap.setSecondDealerId(dealer.getDealerId());
					tawap.setSecondDealerName(dealer.getDealerName());
				} else {
					tawap.setDealerId(dealerId); // YH 2010.11.30
					tawap.setDealerCode(dealerCode);
					TmDealerPO tmDealerPO = new TmDealerPO();
					tmDealerPO.setDealerId(Long.valueOf(dealerId));
					List<TmDealerPO> selectLists = dao.select(tmDealerPO);
					if (selectLists != null && selectLists.size() == 1) {
						tawap.setDealerName(selectLists.get(0).getDealerName());
						tawap.setDealerShortname(selectLists.get(0)
								.getDealerShortname());
					}
				}
				tawap.setPrimaryRoNo(hasWr);
				tawap.setRoNo(roNo);
				if (Constant.REPAIR_TYPE_02.equals(repairType)) { // 如果是外出
					TtAsWrOutrepairPO to = new TtAsWrOutrepairPO();
					to.setId(Utility.getLong(SequenceManager.getSequence("")));
					to.setRoNo(roNo);
					to.setStartTime(Utility.getDate(hhmm, 3));
					to.setEndTime(Utility.getDate(hhmm1, 3));
					to.setOutPerson(outPerson);
					to.setOutSite(outSite);
					to.setOutLicenseno(outLicenseno);
					to.setFromAdress(fromAdress);
					to.setEndAdress(endAdress);
					to.setOutMileage(Utility.getDouble(outMileage));
					to.setCreateBy(getCurrDealerId());
					to.setCreateDate(date);
					dao.insert(to);
				}
				tawap.setVin(vin);

				tawap.setRoCreateDate(new Date());
				tawap.setDeliveryDate(Utility.getDate(roEnddate, 1));
				serveAdvisor=loginUser.getName();
				tawap.setServiceAdvisor(serveAdvisor);
				tawap.setRepairTypeCode(repairType); // 维修类型
				tawap.setLicense(licenseNo);
				tawap.setBrand(brandCode);
				tawap.setModel(modelCode);
				tawap.setSeries(seriesCode);
				tawap.setEngineNo(engineNo);
				tawap.setGuaranteeDate(Utility.getDate(guaranteeDate, 1));
				tawap.setRemark(remark);
				tawap.setVer(0);
				tawap.setFreeTimes(Utility.getInt(freeTimes));
				tawap.setInMileage(Utility.getDouble(inMileage));
				tawap.setDeliverer(deliverer);
				tawap.setQuelityGrate(Integer.parseInt(quelityGrate));
				tawap.setDelivererPhone(delivererPhone);
				tawap.setDelivererMobile(delivererMobile);
				tawap.setDelivererAdress(delivererAdress);
				tawap.setRepairTypeCode(repairType);
				tawap.setCamCode(campaignCode);
				tawap.setActivityType(Integer.parseInt(activityType));
				tawap.setOrderValuableType(Constant.RO_PRO_STATUS_01);
				tawap.setRoStatus(Constant.RO_STATUS_01);
				tawap.setCreateBy(getCurrDealerId());
				tawap.setCreateDate(date);
				tawap.setAccreditAmount(gree);// 默认为不同意费用
				if (ownerName != null && !ownerName.equals("")) {
					tawap.setOwnerName(ownerName);
				}
				if (remark2 != null && !remark2.equals("")) {
					tawap.setRemark2(remark2);
				}
				// 主工时插入
				if (wrLabourCodes != null) {
					for (int i = 0; i < wrLabourCodes.length; i++) {
						// 主工时数量和金额
						labourHoursMain += Utility.getFloat(labourHours[i]);
						stdHourAmount += Utility.getDouble(labourAmounts[i]);

						// 累加总工时金额
						labourAmountMain += Utility.getDouble(labourAmounts[i]);
						TtAsRoLabourPO tawlp = new TtAsRoLabourPO();
						tawlp.setRoId(claimId);
						tawlp.setId(Utility.getLong(SequenceManager
								.getSequence("")));
						tawlp.setWrLabourcode(wrLabourCodes[i]);
						tawlp.setWrLabourname(wrLabournames[i]);
						tawlp.setLabourCode(wrLabourCodes[i]);
						tawlp.setLabourName(wrLabourCodes[i]);
						tawlp.setStdLabourHour(Utility
								.getDouble(labourHours[i]));
						tawlp.setLabourPrice(Utility.getFloat(labourPrices[i]));
						tawlp.setLabourAmount(Utility
								.getDouble(labourAmounts[i]));
						tawlp.setPayType(Utility.getInt(itemPayType[i]));
						tawlp.setForeLevel(level[i]);
						tawlp.setIsFore(Utility.getLong(fore[i]));
						tawlp.setActivityCode(campaignCode);
						tawlp.setMalFunction(Long.valueOf(malFunction[i]));
						tawlp.setQudDamage(Long.valueOf(malFunction[i]));
						if (Constant.PAY_TYPE_01 == Utility
								.getInt(itemPayType[i])) {
							tawlp.setChargePartitionCode("");
						} else if (Constant.PAY_TYPE_02 == Utility
								.getInt(itemPayType[i])) {
							tawlp.setChargePartitionCode("S");
							isClaimFore = 1;
						}
						if (Constant.REPAIR_TYPE_05.equals(repairType)) {// 如果是服务活动就将活动编码插入配件表和工时表
							tawlp.setActivityCode(activityCode);
						}
						if ("10041001".equalsIgnoreCase(fore[i])
								&& "11801002".equalsIgnoreCase(itemPayType[i])) {
							tawlp.setIsClaim(1);
						}
						tawlp.setCreateBy(getCurrDealerId());
						tawlp.setCreateDate(date);
						dao.insert(tawlp);
					}
				}

				tawap.setLabourPrice(Utility.getDouble("" + labourAmountMain));
				tawap.setLabourAmount(Utility.getDouble("" + labourAmountMain));
				// 配件插入
				if (partCodes != null) {
					for (int i = 0; i < partCodes.length; i++) {
						// 累计配件金额
						partsAmountMain += Utility.getDouble(amounts[i]);

						// 累计配件数量
						partsCount += Utility.getDouble(quantitys[i]);
						TtAsRoRepairPartPO tawp = new TtAsRoRepairPartPO();
						tawp.setRoId(claimId);
						tawp.setId(Utility.getLong(SequenceManager
								.getSequence("")));
						tawp.setPartNo(partCodes[i]);
						tawp.setPartName(partNames[i]);
						tawp.setPartQuantity(Utility.getFloat(quantitys[i]));
						tawp.setPartCostPrice(Utility.getDouble(prices[i]));
						tawp.setPartCostAmount(Utility.getDouble(amounts[i]));
						tawp.setPayType(Utility.getInt(partPayType[i]));
						tawp.setPartUseType(Integer.parseInt(partUseType[i]));
						tawp.setActivityCode(campaignCode);
						tawp.setHasPart(Integer.parseInt(hasPart[i]));
						if(Constant.PART_USE_TYPE_01.equals(partUseType[i])||"0".equalsIgnoreCase(quantitys[i])){
							tawp.setIsFore(Long.valueOf(Constant.IF_TYPE_NO.toString())	);
							tawp.setForeLevel("");
						}else{
							tawp.setIsFore(Utility.getLong(partFore[i])	);
							tawp.setForeLevel(partLevel[i]);
						}
						tawp.setMainPartCode(mainPartCode[i]);
						tawp.setRealPartId(Utility.getLong(realPartId[i]));
						tawp.setResponsNature(Utility.getInt(nature[i]));
						if (!"".equalsIgnoreCase(labour[i])) {
							tawp.setLabour(labour[i]);
							tawp.setLabourCode(labour[i]);
						}
						if ("10041001".equalsIgnoreCase(partFore[i])
								&& "11801002".equalsIgnoreCase(partPayType[i])) {
							tawp.setIsClaim(1);
						}
						if (Constant.PAY_TYPE_01 == Utility
								.getInt(partPayType[i])) {
							tawp.setChargePartitionCode("");
						} else if (Constant.PAY_TYPE_02 == Utility
								.getInt(partPayType[i])) {
							tawp.setChargePartitionCode("S");
							isClaimFore = 1;
						}

						// 判断是否配件是否在三包范围内
						if (guaNotice == null
								&& Constant.REPAIR_TYPE_06
										.equalsIgnoreCase(repairType)) {
							tawp.setIsGua(0); // 不在三包范围内
						} else {
							tawp.setIsGua(1); // 在三包范围内
						}
						if (Constant.REPAIR_TYPE_03.equals(repairType)
								|| Constant.REPAIR_TYPE_05.equals(repairType)) {// 售前和服务活动不管页面取得的是什么。都将其设置为在三包期内
							tawp.setIsGua(1);
						}
						if (Constant.REPAIR_TYPE_05.equals(repairType)) {// 如果是服务活动就将活动编码插入配件表和工时表
							tawp.setActivityCode(activityCode);

						}
						//保存新增3字段
						tawp.setTroubleDescribe(troubleDesc[i]);
						tawp.setTroubleReason(troubleReason[i]);
						tawp.setDealMethod(dealMethod[i]);
						tawp.setCreateBy(getCurrDealerId());
						tawp.setCreateDate(date);
						tawp.setZfRono(zfRoNo[i]);
						dao.insert(tawp);

					}
				}
				tawap.setRepairPartAmount(partsAmountMain);
				// 其他项目插入
				if (itemCodes != null) {
					for (int i = 0; i < itemCodes.length; i++) {
						// 累计其他项目总金额
						netItemAmount += Utility.getDouble(itemAmounts[i]);
						TtAsRoAddItemPO tawp = new TtAsRoAddItemPO();
						tawp.setRoId(claimId);
						tawp.setId(Utility.getLong(SequenceManager
								.getSequence("")));
						tawp.setAddItemCode(itemCodes[i]);
						tawp.setAddItemName(itemNames[i]);
						tawp.setAddItemAmount(Utility.getDouble(itemAmounts[i]));
						tawp.setRemark(itemRemarks[i]);
						tawp.setActivityCode(campaignCode);
						tawp.setMainPartCode(outMainPart);
						tawp.setPayType(Utility.getInt(otherPayType[i]));
						if (Constant.PAY_TYPE_01 == Utility
								.getInt(otherPayType[i])) {
							tawp.setChargePartitionCode("");
						} else if (Constant.PAY_TYPE_02 == Utility
								.getInt(otherPayType[i])) {
							tawp.setChargePartitionCode("S");
							isClaimFore = 1;
						}
						if (Constant.SERVICEACTIVITY_CAR_cms_02.toString()
								.equalsIgnoreCase(itemCodes[i])) {
							tawp.setDiscount(Double.valueOf(byAmount));
						}
						tawp.setCreateBy(getCurrDealerId());
						tawp.setCreateDate(date);
						dao.insert(tawp);
					}
				}
				// 附件功能
				String ywzj = tawap.getId().toString();
				String[] fjids = request.getParamValues("fjids");
				FileUploadManager.fileUploadByBusiness(ywzj, fjids, loginUser);

				repairTotal = labourAmountMain + partsAmountMain+ netItemAmount + accPrice+compensationMoney;//把补偿费加入进去
				tawap.setAddItemAmount(netItemAmount);
				tawap.setRepairAmount(repairTotal);
				tawap.setBalanceAmount(repairTotal);
				// 插入到索赔申请单表
				// 验证是否有重复工时如果存在就删除一个
				List<Map<String, Object>> listLabour = dao
						.viewRepeatedlyLabour(claimId);
				String labourCode = "";
				if (listLabour.size() > 0) {
					for (int i = 0; i < listLabour.size(); i++) {
						labourCode = listLabour.get(i).get("CODE").toString();
						List<Map<String, Object>> listLabourId = dao
								.viewLabourCode(claimId, labourCode);
						if (listLabourId.size() > 0) {
							dao.deleteLabourItem(
									listLabourId.get(0).get("LABOUR_ID")
											.toString(), claimId);
						}
					}
				}
				List<Map<String, Object>> listCount = dao.viewRoNoCount(roNo);
				if (Integer.valueOf(listCount.get(0).get("COUNT").toString()) < 1) {
					dao.insert(tawap);
				}

				if (!Constant.REPAIR_TYPE_05.equals(repairType)
						&& !Constant.REPAIR_TYPE_04.equals(repairType)) {
					RoRuleAudit ra = new RoRuleAudit(null, claimId);
					ra.run();
				}
				TtAsRepairOrderPO op = new TtAsRepairOrderPO();
				TtAsRepairOrderPO op2 = new TtAsRepairOrderPO();
				op.setId(claimId);
				TtAsRepairOrderPO role = (TtAsRepairOrderPO) dao.select(op)
						.get(0);
				System.out.println(role.getCustomerDesc() + "自动验证规则结果：");
				if (role.getCustomerDesc() != null
						&& !"".equalsIgnoreCase(role.getCustomerDesc())) {
					accredit = 1;
				}
				// ================================ zyw 2014-7-7 23:48:56
				/**
				 * 判断是否为自费的 不让其预授权
				 */
				if (wrLabourCodes != null && partCodes != null) {
					int temp = 0;
					int temp1 = 0;
					for (int i = 0; i < wrLabourCodes.length; i++) {
						for (int j = 0; j < partCodes.length; j++) {
							if(wrLabourCodes[i].equalsIgnoreCase(labour[j])){
							if ( Integer.parseInt(partPayType[j]) == Constant.PAY_TYPE_01&& Integer.parseInt(itemPayType[i]) == Constant.PAY_TYPE_01) {
								temp++;
							} else {
								temp1++;
							}
							}
						}
					}
					if (temp > 0 && temp1 == 0) {
						accredit = 0;
					}
				}
				//==================================
				//如果有补偿费的必须走预授权
				if (supplier_code != null && supplier_code.length>0 ) {
					accredit=1;
				}
				//==================================
				if(Constant.REPAIR_TYPE_05.equals(repairType)){//服务活动不走预授权
					accredit=0;
				}
				//如果要预授权就不能转索赔单
				if (accredit == 1) {
					isClaimFore = 0;
				}
//				else{
//					isClaimFore = 1;
//				}
				op2.setApprovalYn(accredit);
				op2.setIsClaimFore(isClaimFore);
				dao.update(op, op2);// 是否需要预授权

				act.setOutData("roNo", roNo);
				act.setOutData("ID", claimId);
				act.setOutData("repairType", repairType);
				str = "init";
				act.setOutData("accredit", accredit);
				act.setOutData("isClaimFore", isClaimFore);
			} else {
				str = "outin";
				act.setOutData("outNotice", outNotice);
			}
			act.setOutData("success", str);
			act.setOutData("baoyang", baoyang);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.UPDATE_FAILURE_CODE, "工单维护");
			logger.error(loginUser, e1);
			act.setOutData("err", e);
			act.setException(e1);
		}
	}

	/**
	 * 
	 * @Title: roUpdate
	 * @Description: TODO(工单修改)
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	@SuppressWarnings("unchecked")
	public void roUpdate() {

		StringBuffer con = new StringBuffer();
		Date date = new Date();
		try {

			Integer curPage = getCurrPage();
			Integer pageSize = 10;
			float labourHoursMain = 0f; // 基本工时数
			float reinForceHoursMain = 0f; // 附加工时数
			double stdHourAmount = 0; // 基本工时金额
			double reinHourAmount = 0; // 附加工时金额
			int partsCount = 0; // 总配件数
			float labourAmountMain = 0; // 总工时金额
			double partsAmountMain = 0; // 配件金额
			double netItemAmount = 0; // 其他项目金额
			double repairTotal = 0; // 索赔申请金额
			String roId = request.getParamValue("roId");
			String ownerName = request.getParamValue("CTM_NAME_1");// 车主姓名
			String id = request.getParamValue("ID");
			String roNo = request.getParamValue("RO_NO"); // 工单号
			String lineNo = request.getParamValue("LINE_NO");// 行号
			String roStartdate = request.getParamValue("RO_STARTDATE");// 工单开始时间
			String roEnddate = request.getParamValue("RO_ENDDATE");// 工单结束时间
			String guaranteeDate = request.getParamValue("GUARANTEE_DATE");// 保修开始时间
			String serveAdvisor = request.getParamValue("SERVE_ADVISOR");// 接待员
			String quelityGrate = request.getParamValue("QUELITY_GRATE");// z质量等级
			String vin = request.getParamValue("VIN");// VIN
			String remark = request.getParamValue("APP_REMARK");// 申请备注
			String claimType = request.getParamValue("CLAIM_TYPE"); // 索赔类型
			String deliverer = request.getParamValue("DELIVERER");
			String delivererPhone = request.getParamValue("DELIVERER_PHONE");
			String delivererMobile = request.getParamValue("DELIVERER_MOBILE");
			String delivererAdress = request.getParamValue("DELIVERER_ADRESS");
			String repairType = request.getParamValue("REPAIR_TYPE"); // 维修类型
			String APPROVAL_YN = request.getParamValue("APPROVAL_YN");
			//String TROUBLE_DESC = request.getParamValue("TROUBLE_DESC");// 故障描述
			//String TROUBLE_REASON = request.getParamValue("TROUBLE_REASON");// 故障原因
			//String REPAIR_METHOD = request.getParamValue("REPAIR_METHOD");// 故障措施
			//String APP_REMARK = request.getParamValue("APP_REMARK");// 申请备注
			String commit = request.getParamValue("commit"); // 判断是否是问题工单提报 YH
																// 2011.7.4
			/* 外出维修 */
			String startTime = request.getParamValue("START_DATE");
			String endTime = request.getParamValue("END_DATE");
			String outPerson = request.getParamValue("OUT_PERSON");
			String outSite = request.getParamValue("OUT_SITE");
			String outLicenseno = request.getParamValue("OUT_LICENSENO");
			String fromAdress = request.getParamValue("FROM_ADRESS");
			String endAdress = request.getParamValue("END_ADRESS");
			String outMileage = request.getParamValue("OUT_MILEAGE");
			//String troubleDesc = request.getParamValue("TROUBLE_DESC");
			//String troubleReason = request.getParamValue("TROUBLE_REASON");
			//String repairMethod = request.getParamValue("REPAIR_METHOD");
			String inMileage = request.getParamValue("IN_MILEAGE");
			String dealerCode = request.getParamValue("DEALER_CODE");

			String campaignCode = request.getParamValue("CAMPAIGN_CODE"); // 服务活动代码
			String campaignFee = request.getParamValue("CAMPAIGN_FEE"); // 服务活动费用
			String isFixing = request.getParamValue("IS_FIX"); // 服务活动是否固定费用1为固定费用
			String remark2 = request.getParamValue("remark_ysq");// 获取预授权申请内容
			String freeMAmount = request.getParamValue("FREE_M_AMOUNT"); // 免费保养次数
			String freeMPrice = request.getParamValue("FREE_M_PRICE"); // 免费保养费用
			String freeTimes = request.getParamValue("freeTimes"); // 免费保养次数

			String yieldly = request.getParamValue("YIELDLY"); // 产地
			String licenseNo = request.getParamValue("LICENSE_NO"); // 车牌号
			String brandCode = request.getParamValue("BRAND_CODE0"); // 品牌
			String brandName = request.getParamValue("BRAND_NAME0");
			String modelCode = request.getParamValue("MODEL_CODE0"); // 车型
			String modelName = request.getParamValue("MODEL_NAME0");
			String seriesCode = request.getParamValue("SERIES_CODE0");// 车系
			String seriesName = request.getParamValue("SERIES_NAME0");
			String engineNo = request.getParamValue("ENGINE_NO");
			String gearboxNo = request.getParamValue("GEARBOX_NO0"); // 变速箱
			String rearaxleNo = request.getParamValue("REARAXLE_NO0"); // 后桥
			String transferNo = request.getParamValue("TRANSFER_NO0"); // 分动器
			// 取得删除IDS
			String itemDel = request.getParamValue("ITEM_DEL");
			String[] labourIds = request.getParamValues("LABOUR_ID");
			String[] items = request.getParamValues("ITEM"); // 工时选择的CHECKBOX
			String[] wrLabourCodes = request.getParamValues("WR_LABOURCODE"); // 获取工时代码
			String[] wrLabournames = request.getParamValues("WR_LABOURNAME"); // 获取工时名称
			String[] labourHours = request.getParamValues("LABOUR_HOURS"); // 获取工时数
			String[] labourPrices = request.getParamValues("LABOUR_PRICE"); // 获取工时单价
			String[] labourAmounts = request.getParamValues("LABOUR_AMOUNT"); // 获取工时金额

			String[] malFunction = request.getParamValues("MALFUNCTION");// 故障代码
			String[] qudDamage = request.getParamValues("QUALITYDAMAGE");// 质损性质
			String[] itemPayType = request.getParamValues("PAY_TYPE_ITEM");// 付费方式

			String[] fore = request.getParamValues("FORE");// 获取工时是否需要预授权
			String[] level = request.getParamValues("LEVELS");// 获取预授权层级
			String[] labour = request.getParamValues("Labour0");// 所属维修项目
			String[] nature = request.getParamValues("RESPONS_NATURE");// 质损性质

			// 附加工时
			String[] bMlCode = request.getParamValues("B_ML_CODE");
			String[] labourIds0 = request.getParamValues("LABOUR_ID0"); // 附加工时ID
			String[] wrLabourCodes0 = request.getParamValues("WR_LABOURCODE0"); // 获取工时代码
			String[] wrLabournames0 = request.getParamValues("WR_LABOURNAME0"); // 获取工时名称
			String[] labourHours0 = request.getParamValues("LABOUR_HOURS0"); // 获取工时数
			String[] labourPrices0 = request.getParamValues("LABOUR_PRICE0"); // 获取工时单价
			String[] labourAmounts0 = request.getParamValues("LABOUR_AMOUNT0"); // 获取工时金额
			String[] hasPart = request.getParamValues("HAS_PART");// 得到配件是否有库存
			String[] partFore = request.getParamValues("PARTFORE");// 获取配件是否预授权、
			String[] partLevel = request.getParamValues("PARTLEVEL");// 获取配件预授权层级
			//
			String partDel = request.getParamValue("PART_DEL");
			String[] partIds = request.getParamValues("PART_ID"); // 取得PARTid
			String[] parts = request.getParamValues("PART");// 配件选择的CHECKBOX
			String[] partCodes = request.getParamValues("PART_CODE"); // 上件代码
			String[] partNames = request.getParamValues("PART_NAME"); // 上件名称
			String[] downPartCodes = request.getParamValues("DOWN_PART_CODE"); // 下件代码
			String[] downPartNames = request.getParamValues("DOWN_PART_NAME"); // 下件名称
			String[] mainPartCode = request.getParamValues("mainPartCode");// 次因件关联主因件
			String[] partUseType = request.getParamValues("PART_USE_TYPE");// 获取配件的使用类型
			String[] guaNotice = request.getParamValues("IS_GUA");
			
			String[] zfRoNo = request.getParamValues("zfRoNo");//配件索赔的自费工单号
			
			/**
			 * 将故障描述，原因等转移到每个主因件上
			 */
			String[] troubleDesc = request.getParamValues("TROUBLE_DESCRIBE");//配件故障描述
			String[] troubleReason = request.getParamValues("TROUBLE_REASON");//配件故障原因
			String[] dealMethod = request.getParamValues("DEAL_METHOD");//配件维修措施
			
			String[] realPartId = request.getParamValues("REAL_PART_ID");// 获取配件ID
			String[] quantitys = request.getParamValues("QUANTITY"); // 上件数量
			String[] prices = request.getParamValues("PRICE"); // 单价
			String[] amounts = request.getParamValues("AMOUNT"); // 数量
			String[] supplierCodes = request.getParamValues("PRODUCER_CODE");// 生产商代码
			String[] supplierNames = request.getParamValues("PRODUCER_NAME"); // 生产商名称
			String[] downSupplierCodes = request
					.getParamValues("PRODUCER_CODE1");// 生产商代码
			String[] downSupplierNames = request
					.getParamValues("PRODUCER_NAME1"); // 生产商名称
			String[] remarks = request.getParamValues("REMARK"); // 故障描述
			String[] appTime = request.getParamValues("appTime"); // 索赔工时
			String[] bMpCode = request.getParamValues("B_MP_CODE");// 所属主配件
			String[] partIsFore = request.getParamValues("PART_IS_FORE");// 是否授权
			String[] partPayType = request.getParamValues("PAY_TYPE_PART"); // 配件付费方式
			String[] isGua = request.getParamValues("IS_GUA");
			String otherDel = request.getParamValue("OTHER_DEL");
			String[] itemIds = request.getParamValues("OTHER_ID");
			String[] itemCodes = request.getParamValues("ITEM_CODE"); // 其他项目代码
			String[] itemNames = request.getParamValues("ITEM_NAME"); // 其他项目名称
			String[] itemAmounts = request.getParamValues("ITEM_AMOUNT");// 其他项目金额
			String[] itemRemarks = request.getParamValues("ITEM_REMARK");// 备注
			String[] otherIsFore = request.getParamValues("OTHER_IS_FORE");// 是否授权
			String[] otherPayType = request.getParamValues("PAY_TYPE_OTHER"); // 其他付费方式
			String outMainPart = request.getParamValue("OUT_MAIN_PART");// 外派关联主因件
			int is_gua = 0;// 配件是否三包
			int accredit = 0;// 是否预授权
			int isClaimFore = 0;// 需要索赔，但是不需要预授权
			boolean wrFlag = checkWrDate(vin, inMileage);
			boolean WrFlag = true;
			String outNotice = "";
			int checkday = 9999;
			int gree = 0;
			if (wrLabourCodes != null && partCodes == null) {
				outNotice += "有工时代码时,必须选择配件!\n";
				WrFlag = false;
			}
			if (wrLabourCodes == null && partCodes != null) {
				outNotice += "有配件时,必须选择作业代码!\n";
				WrFlag = false;
			}
			if (Constant.REPAIR_TYPE_05.equalsIgnoreCase(repairType)
					&& itemCodes == null) {
				WrFlag = false;
				outNotice += "该活动没有任何项目,请修改!\n";
			}
			if (Constant.REPAIR_TYPE_02.equalsIgnoreCase(repairType)
					&& (outMainPart == null || "".equalsIgnoreCase(outMainPart) || "-1"
							.equalsIgnoreCase(outMainPart))) {
				WrFlag = false;
				outNotice += "外派救援请选择外派费用关联的主因件!\n";
			}
			boolean flag = false;
			if (wrLabourCodes != null && partCodes != null) {
				for (int i = 0; i < wrLabourCodes.length; i++) {
					int temp = 0;
					flag = false;
					for (int j = 0; j < partCodes.length; j++) {
						if (wrLabourCodes[i].equalsIgnoreCase(labour[j])
								&& Constant.RESPONS_NATURE_STATUS_01.toString()
										.equalsIgnoreCase(nature[j])) {
							temp++;
						}
						if (wrLabourCodes[i].equalsIgnoreCase(labour[j])) {
							flag = true;
						}
					}
					if (temp > 1) {
						WrFlag = false;
						outNotice += "每一个工时最多对应一个主因件!\n";
						break;
					}
					if (!flag) {
						WrFlag = false;
						outNotice += "每个工时都必须对应一个配件!\n";
						break;
					}
				}
			}
			boolean resb = false;
			String partStr="'";
			if (partCodes != null) {
				for (int i = 0; i < partCodes.length; i++) {
					for (int j = i + 1; j < partCodes.length; j++) {
						if (partCodes[i].equalsIgnoreCase(partCodes[j])) {
							outNotice += "一张工单不能添加相同的配件!\n";
							WrFlag = false;
							break;
						}
					}
					if (Constant.RESPONS_NATURE_STATUS_01.toString()
							.equalsIgnoreCase(nature[i])) {
						if("".equalsIgnoreCase(troubleDesc[i])){
							outNotice += "主因件必须填写故障描述!\n";
							WrFlag = false;
							break;
						}
						if("".equalsIgnoreCase(troubleReason[i])){
							outNotice += "主因件必须填写故障原因!\n";
							WrFlag = false;
							break;
						}
						if("".equalsIgnoreCase(dealMethod[i])){
							outNotice += "主因件必须填写维修措施!\n";
							WrFlag = false;
							break;
						}
						resb = true;
					}
					if (Constant.RESPONS_NATURE_STATUS_02.toString()
							.equalsIgnoreCase(nature[i])
							&& "".equalsIgnoreCase(mainPartCode[i])) {
						outNotice += "每一个次因件必须选择对应的主因件!\n";
						WrFlag = false;
						break;
					}
					partStr = partStr+partCodes[i]+"','";
				}
				if(!"".equalsIgnoreCase(partStr)){
					partStr = partStr.substring(0, partStr.length()-2);
				}
				if(partStr.equals("'"))
				{
					partStr = partStr + "'";
				}
				if (!resb) {
					outNotice += "每次维修必须包含一个主因件!\n";
					WrFlag = false;
				}
			}
			/**
			 * 新增同一个VIN，里程，配件不能重复开单 再次修改时不需要判断 被zyw 2014-8-29注释掉
			 */
			/*List<Map<String,Object>> rolist = dao.isReport(vin,inMileage,partStr,roNo);
			if(rolist!=null&& rolist.size()>0){
				outNotice += "同一VIN,里程,配件只能开一次工单!\n";
				WrFlag = false;
			}*/
			if (wrLabourCodes != null && partCodes != null) {
				for (int i = 0; i < wrLabourCodes.length; i++) {
					for (int j = 0; j < partCodes.length; j++) {
						if (wrLabourCodes[i].equalsIgnoreCase(labour[j])
								&& Integer.parseInt(partPayType[j]) == Constant.PAY_TYPE_02
								&& Integer.parseInt(itemPayType[i]) == Constant.PAY_TYPE_01) {
							WrFlag = false;
							outNotice += "工时选择自费时,配件不能索赔!\n";
							break;
						}
					}
				}
			}
			if ((Constant.REPAIR_TYPE_07.equalsIgnoreCase(repairType)
					|| Constant.REPAIR_TYPE_02.equalsIgnoreCase(repairType)
					|| Constant.REPAIR_TYPE_01.equalsIgnoreCase(repairType)
					|| Constant.REPAIR_TYPE_03.equalsIgnoreCase(repairType) || Constant.REPAIR_TYPE_09.equalsIgnoreCase(repairType) || Constant.REPAIR_TYPE_06
						.equalsIgnoreCase(repairType))
					&& wrLabourCodes == null
					&& partCodes == null) {
				WrFlag = false;
				outNotice += "一般维修,外派服务,售前维修,特殊服务以及急件工单必须选择工时和配件!\n";
			}
			if (Constant.REPAIR_TYPE_08.equalsIgnoreCase(repairType)) {
				if (itemRemarks == null) {
					outNotice += "PDI必须填写项目备注!\n";
					WrFlag = false;
				}
			}

			// 根据得到的经销商代码进行查询售后经销商,如果为空,说明代码有问题，不能做工单
			TmDealerPO dd = new TmDealerPO();
			dd.setDealerCode(dealerCode);
			dd.setDealerType(Constant.DEALER_TYPE_DWR);
			List<TmDealerPO> dealerList1 = dao.select(dd);
			if (dealerList1 == null || dealerList1.size() < 1) {
				WrFlag = false;
				outNotice += "获取登录信息出错,请退出系统或者关闭浏览器后重新打开登录!\n";
			}
			TmDealerPO dd2 = new TmDealerPO();
			dd2.setDealerId(Long.valueOf(loginUser.getDealerId()));
			dd2.setDealerType(Constant.DEALER_TYPE_DWR);
			List<TmDealerPO> dealerList2 = dao.select(dd2);
			if (dealerList2 == null || dealerList1.size() < 1) {
				WrFlag = false;
				outNotice += "获取登录信息出错,请退出系统或者关闭浏览器后重新打开登录!\n";
			}
			String dealerId = loginUser.getDealerId();
			/**
			 * if(partCodes!=null){ outNotice = outNotice+"配件：\n"; for(int p
			 * =0;p<partCodes.length;p++){
			 * if("11801002".equalsIgnoreCase(partPayType
			 * [p])&&"95431001".equalsIgnoreCase(partUseType[p])){ outNotice =
			 * outNotice+partCodes[p]+":请选择自费.\n"; WrFlag = false; } } }
			 * if(wrFlag){//判断已经超过整车三包期 if(wrLabourCodes!=null){ outNotice =
			 * outNotice+"工时：\n"; for(int l =0;l<wrLabourCodes.length;l++){
			 * if("11801002".equalsIgnoreCase(itemPayType[l])){ outNotice =
			 * outNotice+wrLabourCodes[l]+":请选择自费.\n"; WrFlag = false; } } }
			 * if(partCodes!=null){ outNotice = outNotice+"配件：\n"; for(int p
			 * =0;p<partCodes.length;p++){
			 * if("11801002".equalsIgnoreCase(partPayType[p])){ outNotice =
			 * outNotice+partCodes[p]+":请选择自费.\n"; WrFlag = false; } } }
			 * if(itemCodes!=null){ outNotice = outNotice+"其他项目：\n"; for(int o
			 * =0;o<itemCodes.length;o++){
			 * if("11801002".equalsIgnoreCase(otherPayType[o])){ outNotice =
			 * outNotice+itemCodes[o]+":请选择自费.\n"; WrFlag = false; } } } }
			 */
			if (WrFlag) {
				String upsql = "update tt_AS_repair_order o set o.Remark1=null,o.Customer_Desc=null,o.Forl_Status=0 where o.ro_no='"
						+ roNo + "'";
				dao.update(upsql, null);
				TtAsWrWoorLevelPO wlp = new TtAsWrWoorLevelPO();
				wlp.setNum(Integer.parseInt(repairType));
				List<TtAsWrWoorLevelPO> lList = dao.select(wlp);
				if (lList != null && lList.size() > 0) {
					wlp = lList.get(0);
				}
				String appLevel = "";
			/*	// 根据工单去预授权主表查找。如果以前有过预授权记录，删除<并chaoeu到备份表中
				TtAsWrForeapprovalPO fp = new TtAsWrForeapprovalPO();
				if (roNo.equals("") || roNo == null) {
					throw new Exception("不能为空");
				}
				fp.setRoNo(roNo);
				List<TtAsWrForeapprovalPO> list = dao.select(fp);
				if (list != null && list.size() > 0) {// 由于工单修改后会重新进行预授权判定，所以需要将此前的预授权相关信息全部转移到备份表
					TtAsWrForeauthdetailPO hp = new TtAsWrForeauthdetailPO();
					hp.setFid(list.get(0).getId());

					String sql = "insert into Tt_As_Wr_Foreauthdetail_back  select * from Tt_As_Wr_Foreauthdetail a where a.fid="
							+ list.get(0).getId();
					dao.update(sql, null);
					dao.delete(hp);
				}
				String sql = "insert into tt_as_wr_foreapproval_back  select * from tt_as_wr_foreapproval a where a.ro_no='"
						+ roNo + "'";
				dao.update(sql, null);
				dao.delete(fp);
				// 删除预授权明细表数据
				TtAsWrForeapprovalitemPO fip = new TtAsWrForeapprovalitemPO();
				fip.setRoNo(roNo);
				dao.delete(fip);*/
				if (fore != null) {
					for (int i = 0; i < fore.length; i++) {
						if ("10041001".equalsIgnoreCase(fore[i])
								&& "11801002".equalsIgnoreCase(itemPayType[i])) {
							accredit = 1;
						}
					}
				}
				if (partFore != null) {
					for (int i = 0; i < partFore.length; i++) {
						if ("10041001".equalsIgnoreCase(partFore[i])
								&& "11801002".equalsIgnoreCase(partPayType[i])) {
							accredit = 1;
						}
					}
				}
				if (itemCodes != null && itemCodes.length > 0) {
					for (String itemCode : itemCodes) {
						if (itemCode.equals("QT001")) {
							accredit = 1;
							break;
						}
					}

				}
				if (partCodes != null) {
					for (int i = 0; i < partCodes.length; i++) {
						if (Constant.RESPONS_NATURE_STATUS_01.toString()
								.equalsIgnoreCase(nature[i])
								&& (Constant.PART_USE_TYPE_01
										.equals(partUseType[i]) || "0"
										.equalsIgnoreCase(quantitys[i]))) {// 如果是主因件，并且
																			// 为维修或者数量为0
							accredit = 1;
							appLevel = "800,";
							break;
						}
					}

				}
				
				// 预警预授权判定****************
				String codes = request.getParamValue("codes");
				String codes_type = request.getParamValue("codes_type");
				String labcodes = request.getParamValue("labcodes");
				String labcodes_type = request.getParamValue("labcodes_type");
				int isWarning = Constant.IF_TYPE_NO;//是否预警
				

				List<String> codelist = new ArrayList<String>();
				List<String> labcodelist = new ArrayList<String>();

				// 三包期判定--三包车辆维修时间
				TtAsWrVinRepairDaysPO daysPO = new TtAsWrVinRepairDaysPO();
				daysPO.setVin(vin);
				List<TtAsWrVinRepairDaysPO> vinlist = dao.select(daysPO);
				if (vinlist != null && vinlist.size() > 0) {
					daysPO = vinlist.get(0);
					int pepair = daysPO.getCurDays();
					String sql= "SELECT * from  TT_AS_WR_VIN_RULE t where t.VR_WARRANTY <= "
							+ pepair
							+ " and t.VR_TYPE = "
							+ Constant.VR_TYPE_1
							+ " order by t.VR_WARRANTY desc";
					List<TtAsWrVinRulePO> VinRulelist = dao.select(
							TtAsWrVinRulePO.class, sql, null);
					if (VinRulelist != null && VinRulelist.size() > 0) {
						if (!XHBUtil.IsNull(VinRulelist.get(0).getVrLevel())) {
							accredit = 1;
							isWarning = Constant.IF_TYPE_YES;
						}
					}
				}

				// 配件
				if (codes_type != null && codes_type.length() > 0
						&& codes != null && codes.length() > 0) {
					String[] code = codes.split(",");
					String[] code_type = codes_type.split(",");
					for (int i = 0; i < code_type.length; i++) {
						if (code_type[i].equals("" + Constant.PAY_TYPE_02)) {
							if (!"00-000".equalsIgnoreCase(code[i])) {
								codelist.add(code[i]);
							}
						}
					}
					List<TtAsWrVinPartRepairTimesvalPO> codeslist = dao
							.getCodes(vin, codelist);
					if (codeslist != null && codeslist.size() > 0) {
						for (TtAsWrVinPartRepairTimesvalPO trtp : codeslist) {
							if (!XHBUtil.IsNull(trtp.getNextLevel())) {
								accredit = 1;
								isWarning = Constant.IF_TYPE_YES;
								break;
							}
						}
					}
				}
				List<TtAsWrVinPartRepairTimesvalPO> codeslistnull = dao
						.getCodesNull(vin, codelist, 1);
				if (codeslistnull != null && codeslistnull.size() > 0) {
					for (TtAsWrVinPartRepairTimesvalPO trtp : codeslistnull) {
						if (!XHBUtil.IsNull(trtp.getNextLevel())) {
							accredit = 1;
							isWarning = Constant.IF_TYPE_YES;
							break;
						}
					}
				}

				// 工时
				if (labcodes_type != null && labcodes_type.length() > 0
						&& labcodes != null && labcodes.length() > 0) {
					String[] labcode = labcodes.split(",");
					String[] labcode_type = labcodes_type.split(",");
					for (int i = 0; i < labcode_type.length; i++) {
						if (labcode_type[i].equals("" + Constant.PAY_TYPE_02)) {
							labcodelist.add(labcode[i]);
						}
					}
					List<TtAsWrVinPartRepairTimesvalPO> labcodeslist = dao
							.getCodes(vin, labcodelist);
					if (labcodeslist != null && labcodeslist.size() > 0) {
						for (TtAsWrVinPartRepairTimesvalPO trtp : labcodeslist) {
							if (!XHBUtil.IsNull(trtp.getNextLevel())) {
								accredit = 1;
								isWarning = Constant.IF_TYPE_YES;
								break;
							}
						}
					}
				}
				List<TtAsWrVinPartRepairTimesvalPO> labcodeslistnull = dao
						.getCodesNull(vin, labcodelist, 2);
				if (labcodeslistnull != null && labcodeslistnull.size() > 0) {
					for (TtAsWrVinPartRepairTimesvalPO trtp : labcodeslistnull) {
						if (!XHBUtil.IsNull(trtp.getNextLevel())) {
							accredit = 1;
							isWarning = Constant.IF_TYPE_YES;
							break;
						}
					}
				}

				// 部位
				List<String> poslist = dao.getpart(codelist);
				if (poslist != null && poslist.size() > 0) {
					List<TtAsWrVinPartRepairTimesvalPO> posslist = dao
							.getCodes(vin, poslist);
					if (posslist != null && posslist.size() > 0) {
						for (TtAsWrVinPartRepairTimesvalPO trtp : posslist) {
							if (!XHBUtil.IsNull(trtp.getNextLevel())) {
								accredit = 1;
								isWarning = Constant.IF_TYPE_YES;
								break;
							}
						}
					}
				}
				List<TtAsWrVinPartRepairTimesvalPO> posslistnull = dao
						.getCodesNull(vin, poslist, 3);
				if (posslistnull != null && posslistnull.size() > 0) {
					for (TtAsWrVinPartRepairTimesvalPO trtp : posslistnull) {
						if (!XHBUtil.IsNull(trtp.getNextLevel())) {
							accredit = 1;
							isWarning = Constant.IF_TYPE_YES;
							break;
						}
					}
				}
				// 预警预授权判定结束****************

				
				// 由于在新增是已经更新过里程,在修改界面不能进行里程修改,所以这里不在更新
				String[] attIds = request.getParamValues("uploadFjid");
				if (Constant.REPAIR_TYPE_01.equals(repairType)) {// 一般维修
					startTime = null;
					endTime = null;
					outPerson = null;
					outSite = null;
					outLicenseno = null;
					fromAdress = null;
					endAdress = null;
					outMileage = null;
					campaignCode = null;
					freeTimes = null;
				} else if (Constant.REPAIR_TYPE_06.equals(repairType)) {// 特殊服务
					startTime = null;
					endTime = null;
					outPerson = null;
					outSite = null;
					outLicenseno = null;
					fromAdress = null;
					endAdress = null;
					outMileage = null;
					campaignCode = null;
					freeTimes = null;
					accredit = 1;
					appLevel = wlp.getWoorLevel();
				} else if (Constant.REPAIR_TYPE_07.equals(repairType)) {// 急件
					startTime = null;
					endTime = null;
					outPerson = null;
					outSite = null;
					outLicenseno = null;
					fromAdress = null;
					endAdress = null;
					outMileage = null;
					campaignCode = null;
					freeTimes = null;
					accredit = 1;
					appLevel = wlp.getWoorLevel();
				} else if (Constant.REPAIR_TYPE_02.equals(repairType)) {// 外出维修
					campaignCode = null;
					freeTimes = null;
					if (itemCodes != null) {
						for (int i = 0; i < itemCodes.length; i++) {
							if ("QT001".equalsIgnoreCase(itemCodes[i])) {
								accredit = 1;
								appLevel = wlp.getWoorLevel();
								break;
							}
						}
					}
				}else if (Constant.REPAIR_TYPE_09.equals(repairType)){//配件索赔
					campaignCode = null;
					freeTimes = null;
					if (itemCodes != null) {
						for (int i = 0; i < itemCodes.length; i++) {
							if ("QT001".equalsIgnoreCase(itemCodes[i])) {
								accredit = 1;
								appLevel = wlp.getWoorLevel();
								break;
							}
						}
					}
				} else if (Constant.REPAIR_TYPE_03.equals(repairType)) {// 售前维修
					startTime = null;
					endTime = null;
					outPerson = null;
					outSite = null;
					outLicenseno = null;
					fromAdress = null;
					endAdress = null;
					outMileage = null;
					campaignCode = null;
					freeTimes = null;
					// accredit = 1;
					// appLevel = wlp.getWoorLevel();
				} else if (Constant.REPAIR_TYPE_04.equals(repairType)) {// 保养
					wrLabourCodes = null;
					partCodes = null;
					itemCodes = null;
					startTime = null;
					endTime = null;
					outPerson = null;
					outSite = null;
					outLicenseno = null;
					fromAdress = null;
					endAdress = null;
					outMileage = null;
					campaignCode = null;
					Date checkDate = new Date();
					if (Utility.testString(guaranteeDate)) {
						checkDate = Utility.getDate(guaranteeDate, 1);
					} else {
						TmVehiclePO tvp = new TmVehiclePO();
						tvp.setVin(vin);

						/***** add by liuxh 20131108判断车架号不能为空 *****/
						CommonUtils.jugeVinNull(vin);
						/***** add by liuxh 20131108判断车架号不能为空 *****/

						List<TmVehiclePO> lsv = dao.select(tvp);
						if (lsv != null) {
							if (lsv.size() > 0) {
								tvp = lsv.get(0);
							}
						}
						checkDate = tvp.getPurchasedDate(); // 保修改开始时间
					}
					Date now = new Date(); // 今天
					if (checkDate != null) {
						String formatStyle = "yyyy-MM-dd HH:mm";
						SimpleDateFormat df = new SimpleDateFormat(formatStyle);
						String d1 = df.format(checkDate);
						String d2 = df.format(now);
						// month = Utility.compareDate(d1,d2,1);
						// //取得今日和保养开始时间的插值
						checkday = Utility.compareDate1(d1, d2, 0); // 取得今日和保养开始时间的插值
																	// 天数
					}
					List<TtAsWrQamaintainPO> listFree = dao.getFree11(
							Integer.valueOf(freeTimes), -1L, 0, checkday,
							Double.valueOf(inMileage));
					if (Integer.parseInt(freeTimes) == 1) {
						if (listFree != null && listFree.size() > 0) {
							gree = 0;
						} else {
							appLevel = wlp.getWoorLevel();
							accredit = 1;
							gree = 0;
						}
					} else if (Integer.parseInt(freeTimes) > 1) {
						isClaimFore = 0;
						if (listFree.size() == 0) {
							accredit = 1;
							appLevel = wlp.getWoorLevel();
						} else {
							accredit = 0;
						}
					}

					/**
					 * 工单修改时,不需要重新设置是否需要预授权,因为在新增时就判断好了,
					 */
					TtAsRepairOrderPO o = new TtAsRepairOrderPO();
					o.setId(Utility.getLong(id));
					o = (TtAsRepairOrderPO) dao.select(o).get(0);
					accredit = o.getApprovalYn();
					isClaimFore = o.getIsClaimFore();
				} else if (Constant.REPAIR_TYPE_05.equals(repairType)) {// 服务活动
					startTime = null;
					endTime = null;
					outPerson = null;
					outSite = null;
					outLicenseno = null;
					fromAdress = null;
					endAdress = null;
					outMileage = null;
					freeTimes = null;
					isClaimFore = 1;
				} else if (Constant.REPAIR_TYPE_08.equals(repairType)) {// PDI检测
					startTime = null;
					endTime = null;
					outPerson = null;
					outSite = null;
					outLicenseno = null;
					fromAdress = null;
					endAdress = null;
					outMileage = null;
					campaignCode = null;
					freeTimes = null;
					isClaimFore = 1;
				}
				// 索赔单ID
				Long claimId = Utility.getLong(id);


				TtAsRepairOrderPO tawap0 = new TtAsRepairOrderPO();
				tawap0.setId(claimId);
				TtAsRepairOrderPO tawap = new TtAsRepairOrderPO();
				tawap.setId(claimId);
				tawap.setRoNo(roNo);
				if (Constant.REPAIR_TYPE_02.equals(repairType) || Constant.REPAIR_TYPE_09.equals(repairType)) { // 如果是外出或则是配件索赔
					TtAsWrOutrepairPO to0 = new TtAsWrOutrepairPO();
					to0.setRoNo(roNo);
					List toLs = dao.select(to0);
					if (toLs != null) {
						if (toLs.size() > 0) {
							TtAsWrOutrepairPO to = new TtAsWrOutrepairPO();
							to.setId(Utility.getLong(SequenceManager
									.getSequence("")));
							to.setRoNo(roNo);
							to.setStartTime(Utility.getDate(startTime, 3));
							to.setEndTime(Utility.getDate(endTime, 3));
							to.setOutPerson(outPerson);
							to.setOutSite(outSite);
							to.setOutLicenseno(outLicenseno);
							to.setFromAdress(fromAdress);
							to.setEndAdress(endAdress);
							to.setOutMileage(Utility.getDouble(outMileage));
							to.setUpdateBy(getCurrDealerId());
							to.setUpdateDate(date);
							dao.update(to0, to);
						} else {
							TtAsWrOutrepairPO to = new TtAsWrOutrepairPO();
							to.setId(Utility.getLong(SequenceManager
									.getSequence("")));
							to.setRoNo(roNo);
							to.setStartTime(Utility.getDate(startTime, 3));
							to.setEndTime(Utility.getDate(endTime, 3));
							to.setOutPerson(outPerson);
							to.setOutSite(outSite);
							to.setOutLicenseno(outLicenseno);
							to.setFromAdress(fromAdress);
							to.setEndAdress(endAdress);
							to.setOutMileage(Utility.getDouble(outMileage));
							to.setCreateBy(getCurrDealerId());
							to.setCreateDate(date);
							dao.insert(to);
						}
					} else {
						TtAsWrOutrepairPO to = new TtAsWrOutrepairPO();
						to.setId(Utility.getLong(SequenceManager
								.getSequence("")));
						to.setRoNo(roNo);
						to.setStartTime(Utility.getDate(startTime, 3));
						to.setEndTime(Utility.getDate(endTime, 3));
						to.setOutPerson(outPerson);
						to.setOutSite(outSite);
						to.setOutLicenseno(outLicenseno);
						to.setFromAdress(fromAdress);
						to.setEndAdress(endAdress);
						to.setOutMileage(Utility.getDouble(outMileage));
						to.setCreateBy(getCurrDealerId());
						to.setCreateDate(date);
						dao.insert(to);
					}
				}
				tawap.setVin(vin);
				tawap.setDeliveryDate(Utility.getDate(roEnddate, 1));
				serveAdvisor=loginUser.getName();
				tawap.setServiceAdvisor(serveAdvisor);
				tawap.setRepairTypeCode(repairType); // 维修类型
				tawap.setLicense(licenseNo);
				tawap.setBrand(brandCode);
				tawap.setModel(modelCode);
				tawap.setSeries(seriesCode);
				tawap.setEngineNo(engineNo);
				tawap.setDeliverer(deliverer);
				tawap.setCustomerDesc(appLevel);
				tawap.setDelivererPhone(delivererPhone);
				tawap.setDelivererMobile(delivererMobile);
				tawap.setDelivererAdress(delivererAdress);
				tawap.setFreeTimes(Utility.getInt(freeTimes));
				tawap.setGuaranteeDate(Utility.getDate(guaranteeDate, 1));
				tawap.setRepairTypeCode(repairType);
				tawap.setCamCode(campaignCode);
				tawap.setRemark(remark);
				tawap.setQuelityGrate(Integer.parseInt(quelityGrate));
				tawap.setRemark1("");// YH 2011.7.29
				tawap.setInMileage(Utility.getDouble(inMileage));
				tawap.setUpdateBy(getCurrDealerId());
				tawap.setUpdateDate(date);
				if (ownerName != null && !ownerName.equals("")) {
					tawap.setOwnerName(ownerName);
				}

				if (remark2 != null && !remark2.equals("")) {
					tawap.setRemark2(remark2);
				}
				// 插入时需要对车辆信息表中的车牌号进行更新
				TmVehiclePO tvp = new TmVehiclePO();
				tvp.setVin(vin);
				TmVehiclePO tvpUp = new TmVehiclePO();
				tvpUp.setVin(vin);
				tvpUp.setLicenseNo(licenseNo);
				tvpUp.setUpdateBy(getCurrDealerId());
				tvpUp.setUpdateDate(date);
				dao.update(tvp, tvpUp);

				/**
				 * 不需要判断是否有删除，新增。直接将表中的数据删除后在新增
				 */
				TtAsRoLabourPO lp = new TtAsRoLabourPO();
				lp.setRoId(Utility.getLong(id));
				dao.delete(lp);
				if (wrLabourCodes != null) {
					for (int i = 0; i < wrLabourCodes.length; i++) {
						// 主工时数量和金额
						labourHoursMain += Utility.getFloat(labourHours[i]);
						stdHourAmount += Utility.getDouble(labourAmounts[i]);
						// 累加总工时金额
						labourAmountMain += Utility.getDouble(labourAmounts[i]);
						TtAsRoLabourPO tawlp = new TtAsRoLabourPO();

						tawlp.setId(Utility.getLong(SequenceManager
								.getSequence("")));
						tawlp.setRoId(claimId);
						tawlp.setWrLabourcode(wrLabourCodes[i]);
						tawlp.setWrLabourname(wrLabournames[i]);
						tawlp.setLabourCode(wrLabourCodes[i]);
						tawlp.setLabourName(wrLabournames[i]);
						tawlp.setStdLabourHour(Utility
								.getDouble(labourHours[i]));
						tawlp.setLabourPrice(Utility.getFloat(labourPrices[i]));
						tawlp.setLabourAmount(Utility
								.getDouble(labourAmounts[i]));
						tawlp.setPayType(Utility.getInt(itemPayType[i]));
						tawlp.setActivityCode(campaignCode);
						tawlp.setCreateDate(new Date());
						tawlp.setCreateBy(getCurrDealerId());
						tawlp.setMalFunction(Utility.getLong(malFunction[i]));
						tawlp.setQudDamage(Utility.getLong(malFunction[i]));
						if (Constant.PAY_TYPE_01 == Utility
								.getInt(itemPayType[i])) {
							tawlp.setChargePartitionCode("");
						} else if (Constant.PAY_TYPE_02 == Utility
								.getInt(itemPayType[i])) {
							tawlp.setChargePartitionCode("S");
							isClaimFore = 1;
						}
						if ("10041001".equalsIgnoreCase(fore[i])
								&& "11801002".equalsIgnoreCase(itemPayType[i])) {
							tawlp.setIsClaim(1);
						}
						tawlp.setIsFore(Utility.getLong(fore[i]));
						tawlp.setForeLevel(level[i]);
						tawlp.setUpdateBy(getCurrDealerId());
						tawlp.setUpdateDate(date);
						dao.insert(tawlp);
					}
				}
				tawap.setLabourPrice(Utility.getDouble("" + labourAmountMain));
				tawap.setLabourAmount(Utility.getDouble("" + labourAmountMain));

				/**
				 * 配件也一样 直接删除原来，然后在新增
				 */

				TtAsRoRepairPartPO pp = new TtAsRoRepairPartPO();
				pp.setRoId(Utility.getLong(id));
				dao.delete(pp);
				if (partCodes != null) {
					for (int i = 0; i < partCodes.length; i++) {
						// 累计配件金额
						partsCount += Utility.getDouble(quantitys[i]);
						// 累计配件数量
						partsAmountMain += Utility.getDouble(amounts[i]);
						TtAsRoRepairPartPO tawp = new TtAsRoRepairPartPO();

						tawp.setId(Utility.getLong(SequenceManager
								.getSequence("")));
						tawp.setRoId(claimId);
						tawp.setPartNo(partCodes[i]);
						tawp.setPartName(partNames[i]);
						tawp.setHasPart(Integer.parseInt(hasPart[i]));
						tawp.setPartQuantity(Utility.getFloat(quantitys[i]));
						tawp.setPartCostPrice(Utility.getDouble(prices[i]));
						tawp.setPartCostAmount(Utility.getDouble(amounts[i]));
						tawp.setPayType(Utility.getInt(partPayType[i]));
						tawp.setPartUseType(Integer.parseInt(partUseType[i]));
						tawp.setActivityCode(campaignCode);
						tawp.setMainPartCode(mainPartCode[i]);
						tawp.setCreateDate(new Date());
						tawp.setCreateBy(getCurrDealerId());
						tawp.setResponsNature(Utility.getInt(nature[i]));
						tawp.setRealPartId(Utility.getLong(realPartId[i]));
						if (!"".equalsIgnoreCase(labour[i])) {
							tawp.setLabour(labour[i]);
							tawp.setLabourCode(labour[i]);
						}
						if (Constant.PAY_TYPE_01 == Utility
								.getInt(partPayType[i])) {
							tawp.setChargePartitionCode("");
						} else if (Constant.PAY_TYPE_02 == Utility
								.getInt(partPayType[i])) {
							tawp.setChargePartitionCode("S");
							isClaimFore = 1;
						}
						if(Constant.PART_USE_TYPE_01.equals(partUseType[i])||"0".equalsIgnoreCase(quantitys[i])){
							tawp.setIsFore(Long.valueOf(Constant.IF_TYPE_NO.toString())	);
							tawp.setForeLevel("");
						}else{
							tawp.setIsFore(Utility.getLong(partFore[i])	);
							tawp.setForeLevel(partLevel[i]);
						}
						if ("10041001".equalsIgnoreCase(partFore[i])
								&& "11801002".equalsIgnoreCase(partPayType[i])) {
							tawp.setIsClaim(1);
						}
						// 判断是否配件是否在三包范围内
						// is_gua = partIsGua(guaranteeDate, inMileage, vin,
						// partCodes[i]);
						if (guaNotice == null
								|| Constant.REPAIR_TYPE_06
										.equalsIgnoreCase(repairType)) {
							tawp.setIsGua(0); // 不在三包范围内
						} else {
							// tawp.setIsGua(Integer.valueOf(guaNotice[i]));
							// //在三包范围内
							tawp.setIsGua(1); // 在三包范围内
						}
						if (Constant.REPAIR_TYPE_03.equals(repairType)
								|| Constant.REPAIR_TYPE_05.equals(repairType)) {// 售前和服务活动不管页面取得的是什么。都将其设置为在三包期内
							tawp.setIsGua(1);
						}

						// System.out.println(guaNotice[i]);
						// is_gua = partIsGua(guaranteeDate, inMileage, vin,
						// partCodes[i]);

						//保存新增3字段
						tawp.setTroubleDescribe(troubleDesc[i]);
						tawp.setTroubleReason(troubleReason[i]);
						tawp.setDealMethod(dealMethod[i]);
						
						tawp.setUpdateBy(getCurrDealerId());
						tawp.setUpdateDate(date);
						tawp.setZfRono(zfRoNo[i]);
						dao.insert(tawp);
					}
				}

				tawap.setRepairPartAmount(partsAmountMain);

				// 删除其他项目
				if (Utility.testString(otherDel)) {
					otherDel = otherDel.replaceFirst(",", "");
					String[] otherDels = otherDel.split(",");
					Set<String> otherSet = new HashSet<String>();
					// 避免重复ID
					for (int i = 0; i < otherDels.length; i++) {
						otherSet.add(otherDels[i]);
					}
					Iterator<String> it = otherSet.iterator();
					while (it.hasNext()) {
						TtAsRoAddItemPO t = new TtAsRoAddItemPO();
						t.setId(Utility.getLong(it.next()));
						dao.delete(t);
					}

				}
				// 其他项目插入
				if (itemIds != null) {
					for (int i = 0; i < itemIds.length; i++) {
						// 累计其他项目总金额
						netItemAmount += Utility.getDouble(itemAmounts[i]);
						TtAsRoAddItemPO tawp0 = new TtAsRoAddItemPO();
						tawp0.setId(Utility.getLong(itemIds[i]));
						TtAsRoAddItemPO tawp = new TtAsRoAddItemPO();
						tawp.setRoId(claimId);
						tawp.setId(Utility.getLong(itemIds[i]));

						tawp.setAddItemCode(itemCodes[i]);
						tawp.setAddItemName(itemNames[i]);
						tawp.setAddItemAmount(Utility.getDouble(itemAmounts[i]));
						tawp.setRemark(itemRemarks[i]);
						tawp.setMainPartCode(outMainPart);
						tawp.setPayType(Utility.getInt(otherPayType[i]));
						tawp.setActivityCode(campaignCode);
						if (Constant.PAY_TYPE_01 == Utility
								.getInt(otherPayType[i])) {
							tawp.setChargePartitionCode("");
						} else if (Constant.PAY_TYPE_02 == Utility
								.getInt(otherPayType[i])) {
							tawp.setChargePartitionCode("S");
							isClaimFore = 1;
						}
						tawp.setUpdateBy(getCurrDealerId());
						tawp.setUpdateDate(date);
						dao.update(tawp0, tawp);
					}
					if (itemCodes.length > itemIds.length) {
						for (int i = itemIds.length; i < itemCodes.length; i++) {
							// 累计其他项目总金额
							netItemAmount += Utility.getDouble(itemAmounts[i]);
							TtAsRoAddItemPO tawp = new TtAsRoAddItemPO();
							tawp.setRoId(claimId);
							tawp.setId(Utility.getLong(SequenceManager
									.getSequence("")));
							tawp.setAddItemCode(itemCodes[i]);
							tawp.setAddItemName(itemNames[i]);
							tawp.setMainPartCode(outMainPart);
							tawp.setAddItemAmount(Utility
									.getDouble(itemAmounts[i]));
							tawp.setRemark(itemRemarks[i]);
							tawp.setPayType(Utility.getInt(otherPayType[i]));
							tawp.setActivityCode(campaignCode);
							if (Constant.PAY_TYPE_01 == Utility
									.getInt(otherPayType[i])) {
								tawp.setChargePartitionCode("");
							} else if (Constant.PAY_TYPE_02 == Utility
									.getInt(otherPayType[i])) {
								tawp.setChargePartitionCode("S");
								isClaimFore = 1;
							}
							tawp.setCreateBy(getCurrDealerId());
							tawp.setCreateDate(date);
							dao.insert(tawp);
						}
					}
					// 如果为空，则全部为新增
				} else {
					if (itemCodes != null) {
						for (int i = 0; i < itemCodes.length; i++) {
							// 累计其他项目总金额
							netItemAmount += Utility.getDouble(itemAmounts[i]);
							TtAsRoAddItemPO tawp = new TtAsRoAddItemPO();
							tawp.setRoId(claimId);
							tawp.setId(Utility.getLong(SequenceManager
									.getSequence("")));
							tawp.setAddItemCode(itemCodes[i]);
							tawp.setAddItemName(itemNames[i]);
							tawp.setAddItemAmount(Utility
									.getDouble(itemAmounts[i]));
							tawp.setRemark(itemRemarks[i]);
							tawp.setMainPartCode(outMainPart);
							tawp.setPayType(Utility.getInt(otherPayType[i]));
							tawp.setActivityCode(campaignCode);
							if (Constant.PAY_TYPE_01 == Utility
									.getInt(otherPayType[i])) {
								tawp.setChargePartitionCode("");
							} else if (Constant.PAY_TYPE_02 == Utility
									.getInt(otherPayType[i])) {
								tawp.setChargePartitionCode("S");
								isClaimFore = 1;
							}
							tawp.setCreateBy(getCurrDealerId());
							tawp.setCreateDate(date);
							dao.insert(tawp);
						}
					}
				}
				// 附件
				String ywzj = tawap.getId().toString();
				String[] fjids = request.getParamValues("fjid");
				FileUploadManager.delAllFilesUploadByBusiness(ywzj, fjids);
				FileUploadManager.fileUploadByBusiness(ywzj, fjids, loginUser);

				// 补偿费添加 zyw 2014-8-4==================================
				TtAsWrCompensationMoneyPO   compensationmoney1 = new TtAsWrCompensationMoneyPO();
				compensationmoney1.setRoNo(roNo);
				dao.delete(compensationmoney1);
				TtAsWrCompensationMoneyPO   compensationmoney = new TtAsWrCompensationMoneyPO();
				String[] supplier_code = request.getParamValues("supplier_code"); // 供应商代码
				String[] apply_price = request.getParamValues("apply_price"); // 获取工时名称
				String[] pass_price = request.getParamValues("pass_price"); // 获取金额
				String[] part_code = request.getParamValues("part_code_temp");
				double compensationMoney = 0d;
				if (supplier_code != null && supplier_code.length>0 ) {
					int temp=0;
					Date dateTemp = new Date();
					for (String supplierCode : supplier_code) {
						compensationmoney.setPkid(getSeq());
						compensationmoney.setApplyPrice(BaseUtils.ConvertDouble(apply_price[temp]));
						compensationmoney.setPassPrice(BaseUtils.ConvertDouble(pass_price[temp]));
						compensationmoney.setPartCode(part_code[temp]);
						compensationmoney.setSupplierCode(supplier_code[temp]);
						compensationmoney.setCreateDate(dateTemp);
						compensationmoney.setRoNo(roNo);
						compensationmoney.setPartName(null);
						compensationMoney+=BaseUtils.ConvertDouble(apply_price[temp]);//算出总的补偿费并设置到索赔单里
						dao.insert(compensationmoney);
						temp++;
					}
				}
				tawap.setCompensationMoney(compensationMoney);
				//==================================
				
				// 辅料
				TtAccessoryDtlPO po = new TtAccessoryDtlPO();
				po.setRoNo(roNo);
				dao.delete(po);
				String[] workhourCode = request
						.getParamValues("workHourCodeMap"); // 获取工时代码
				String[] workhourName = request.getParamValues("workhour_name"); // 获取工时名称
				String[] price = request.getParamValues("accessoriesPrice"); // 获取金额
				String[] accessoriesMainPartCode = request
						.getParamValues("accessoriesOutMainPart");
				double accPrice = 0d;
				if (workhourCode != null) {
					for (int i = 0; i < workhourCode.length; i++) {
						po.setRoNo(roNo);
						po.setWorkhourCode(workhourCode[i]);
						po.setWorkhourName(workhourName[i]);
						po.setPrice(Double.valueOf(price[i]));
						po.setMainPartCode(accessoriesMainPartCode[i]);
						po.setId(Long.parseLong(SequenceManager.getSequence("")));
						accPrice = accPrice + Double.valueOf(price[i]);
						dao.insert(po);
					}
				}
				tawap.setAccessoriesPrice(accPrice);
				
				repairTotal = labourAmountMain + partsAmountMain+ netItemAmount + accPrice+compensationMoney;
				// + campaignFeeDb + freeMPriceDb;
				tawap.setAddItemAmount(netItemAmount);
				tawap.setRepairAmount(repairTotal);
				tawap.setBalanceAmount(repairTotal);
				// 验证是否有重复工时如果存在就删除一个
				List<Map<String, Object>> listLabour = dao
						.viewRepeatedlyLabour(claimId);
				String labourCode = "";
				if (listLabour.size() > 0) {
					for (int i = 0; i < listLabour.size(); i++) {
						labourCode = listLabour.get(i).get("CODE").toString();
						List<Map<String, Object>> listLabourId = dao
								.viewLabourCode(claimId, labourCode);
						if (listLabourId.size() > 0) {
							dao.deleteLabourItem(
									listLabourId.get(0).get("LABOUR_ID")
											.toString(), claimId);
						}
					}
				}
				// 插入到索赔申请单表
				dao.update(tawap0, tawap);
				if (null != commit && "1".equals(commit)) {
					this.DelProblemOrder(roNo); // 删除问题工单 YH 2011.7.4
				}

				if (!Constant.REPAIR_TYPE_04.equals(repairType)
						&& !Constant.REPAIR_TYPE_05.equals(repairType)) {
					RoRuleAudit ra = new RoRuleAudit(null, claimId);
					ra.run();
				}
				TtAsRepairOrderPO order = (TtAsRepairOrderPO) dao
						.select(tawap0).get(0);
				TtAsRepairOrderPO op = new TtAsRepairOrderPO();
				TtAsRepairOrderPO op2 = new TtAsRepairOrderPO();
				op.setId(claimId);
				TtAsRepairOrderPO role = (TtAsRepairOrderPO) dao.select(op)
						.get(0);
				System.out.println(role.getCustomerDesc() + "自动验证规则结果：");
				System.out.println("".equalsIgnoreCase(role.getCustomerDesc()));
				if (!(null == role.getCustomerDesc())
						&& !"".equalsIgnoreCase(role.getCustomerDesc())) {
					accredit = 1;
				}
				if (accredit == 1) {
					isClaimFore = 0;
				}
				// ================================ zyw 2014-7-7 23:48:56
				/**
				 * 判断是否为自费的 不让其预授权
				 */
				if (wrLabourCodes != null && partCodes != null) {
					int temp = 0;
					int temp1 = 0;
					for (int i = 0; i < wrLabourCodes.length; i++) {
						for (int j = 0; j < partCodes.length; j++) {
							if(wrLabourCodes[i].equalsIgnoreCase(labour[j])){
							if ( Integer.parseInt(partPayType[j]) == Constant.PAY_TYPE_01&& Integer.parseInt(itemPayType[i]) == Constant.PAY_TYPE_01) {
								temp++;
							} else {
								temp1++;
							}
							}
						}
					}
					if (temp > 0 && temp1 == 0) {
						accredit = 0;
					}
				}
				//==================================
				if(Constant.REPAIR_TYPE_05.equals(repairType)){//服务活动不走预授权
					accredit=0;
				}
				// ================================
				//如果有补偿费的必须走预授权
				if (supplier_code != null && supplier_code.length>0 ) {
					accredit=1;
				}
				if(accredit==1){
					isClaimFore = 0;
				}
//				else{
//					isClaimFore = 1;
//				}
				//==================================
				op2.setApprovalYn(accredit);
				op2.setIsClaimFore(isClaimFore);
				op2.setVer(order.getVer() + 1);
				op2.setIsWarning(isWarning);
				dao.update(op, op2);// 是否需要预授权

				act.setOutData("isClaimFore", isClaimFore);
				act.setOutData("success", "true");
				act.setOutData("roNo", roNo);
				act.setOutData("ID", claimId);
				act.setOutData("repairType", repairType);
				act.setOutData("accredit", accredit);
			} else {
				act.setOutData("success", "false");
				act.setOutData("outNotice", outNotice);
			}

		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.UPDATE_FAILURE_CODE, "工单维护");
			act.setOutData("success", false);
			logger.error(loginUser, e1);
			act.setOutData("outNotice", e);
			act.setException(e1);
		}
	}

	/**
	 * 
	 * @Title: queryRepairOrder
	 * @Description: TODO(查询工单)
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	public void queryRepairOrder() {

		String dealerId = loginUser.getDealerId();
		String dealerCode = loginUser.getDealerCode();
		try {

			Integer curPage = getCurrPage();
			String roNo = request.getParamValue("RO_NO");
			String vin = request.getParamValue("VIN");
			String repairType = request.getParamValue("REPAIR_TYPE");
			String roCreateDate = request.getParamValue("RO_CREATE_DATE");
			String deliveryDate = request.getParamValue("DELIVERY_DATE");
			String isForl = request.getParamValue("RO_FORE");
			String roStatus = request.getParamValue("RO_STATUS");
			String createDateStr = request.getParamValue("CREATE_DATE_STR");
			String createDateEnd = request.getParamValue("CREATE_DATE_END");
			String isChanghe = request.getParamValue("YIELDLY_TYPE");// 结算基地
			String isWaring = request.getParamValue("IS_WARNING");//是否预警
			/* String orderBy = request.getParamValue("orderBy"); */
			Map<String, String> map = new HashMap();
			map.put("roNo", roNo);
			map.put("vin", vin);
			map.put("repairType", repairType);
			map.put("roCreateDate", roCreateDate);
			map.put("deliveryDate", deliveryDate);
			map.put("isForl", isForl);
			map.put("roStatus", roStatus);
			map.put("dealerId", dealerId);
			map.put("createDateStr", createDateStr);
			map.put("createDateEnd", createDateEnd);
			map.put("isChanghe", isChanghe);
			map.put("isWaring", isWaring);
			/* map.put("orderBy", orderBy); */
			PageResult<TtAsRepairOrderExtPO> ps = dao.queryRepairOrderNew(map,
					Constant.PAGE_SIZE, curPage);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔单维护");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}

	// 结算查询
	public void queryRepairOrderSelf() {

		String dealerId = loginUser.getDealerId();
		String dealerCode = loginUser.getDealerCode();
		try {

			Integer curPage = getCurrPage();
			String roNo = request.getParamValue("RO_NO");
			String vin = request.getParamValue("VIN");
			String repairType = request.getParamValue("REPAIR_TYPE");
			String roCreateDate = request.getParamValue("RO_CREATE_DATE");
			String deliveryDate = request.getParamValue("DELIVERY_DATE");
			String isForl = request.getParamValue("RO_FORE");
			String roStatus = request.getParamValue("RO_STATUS");
			String createDateStr = request.getParamValue("CREATE_DATE_STR");
			String createDateEnd = request.getParamValue("CREATE_DATE_END");
			String isChanghe = request.getParamValue("YIELDLY_TYPE");// 结算基地
			String orderBy = request.getParamValue("orderBy");
			Map<String, String> map = new HashMap();
			map.put("roNo", roNo);
			map.put("vin", vin);
			map.put("repairType", repairType);
			map.put("roCreateDate", roCreateDate);
			map.put("deliveryDate", deliveryDate);
			map.put("isForl", isForl);
			map.put("roStatus", roStatus);
			map.put("dealerId", dealerId);
			map.put("createDateStr", createDateStr);
			map.put("createDateEnd", createDateEnd);
			map.put("isChanghe", isChanghe);
			map.put("orderBy", orderBy);
			PageResult<TtAsRepairOrderExtPO> ps = dao.queryRepairOrderSelf(map,
					Constant.PAGE_SIZE, curPage);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔单维护");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * 
	 * @Title: queryRepairOrder
	 * @Description: TODO(合并工单)
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	public void ROMerge() {

		String dealerId = loginUser.getDealerId();
		String dealerCode = loginUser.getDealerCode();
		boolean sucess = true;
		try {

			Integer curPage = getCurrPage();
			String id = request.getParamValue("ID");// 被合并的工单
			String roId = request.getParamValue("RO_ID");// 需要合并的工单
			dao.insertMergeAdditem(roId, id);
			TtAsRepairOrderPO po = new TtAsRepairOrderPO();
			po.setId(Long.valueOf(roId));
			TtAsRepairOrderPO po1 = new TtAsRepairOrderPO();
			po1.setForlStatus(Constant.RO_FORE_02);
			dao.update(po, po1);
			TrRepairMergePO merPo = new TrRepairMergePO();
			merPo.setId(Utility.getLong(SequenceManager.getSequence("")));
			merPo.setDrId(Long.valueOf(roId));
			merPo.setSrId(Long.valueOf(id));
			merPo.setCreateDate(new Date());
			merPo.setCreateBy(getCurrDealerId().toString());
			dao.insert(merPo);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "合并工单");
			logger.error(loginUser, e1);
			sucess = false;
			act.setException(e1);
		}
		act.setOutData("sucess", sucess);
	}

	public void queryRepairOrderSpecial() {

		String dealerId = loginUser.getDealerId();
		String dealerCode = loginUser.getDealerCode();
		try {

			Integer curPage = getCurrPage();
			String roNo = request.getParamValue("RO_NO");
			String vin = request.getParamValue("VIN");
			String repairType = request.getParamValue("REPAIR_TYPE");
			String roCreateDate = request.getParamValue("RO_CREATE_DATE");
			String deliveryDate = request.getParamValue("DELIVERY_DATE");
			String isForl = request.getParamValue("RO_FORE");
			String roStatus = request.getParamValue("RO_STATUS");
			String createDateStr = request.getParamValue("CREATE_DATE_STR");
			String createDateEnd = request.getParamValue("CREATE_DATE_END");
			String roId = request.getParamValue("RO_ID");
			Map<String, String> map = new HashMap();
			map.put("roNo", roNo);
			map.put("vin", vin);
			map.put("repairType", repairType);
			map.put("roCreateDate", roCreateDate);
			map.put("deliveryDate", deliveryDate);
			map.put("isForl", isForl);
			map.put("roStatus", roStatus);
			map.put("dealerId", dealerId);
			map.put("createDateStr", createDateStr);
			map.put("createDateEnd", createDateEnd);
			map.put("roId", roId);
			PageResult<TtAsRepairOrderExtPO> ps = dao.queryRepairOrderSpecial(
					map, Constant.PAGE_SIZE, curPage);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔单维护");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}

	/*********
	 * 下端工单预授权申请
	 */
	public void roViewForApplyView() {

		String dealerId = loginUser.getDealerId();
		String dealerCode = loginUser.getDealerCode();
		try {

			Integer curPage = getCurrPage();
			String roNo = request.getParamValue("RO_NO");
			String vin = request.getParamValue("VIN");
			String repairType = request.getParamValue("REPAIR_TYPE");
			String roCreateDate = request.getParamValue("RO_CREATE_DATE");
			String deliveryDate = request.getParamValue("DELIVERY_DATE");
			String isForl = request.getParamValue("RO_FORE");
			String roStatus = request.getParamValue("RO_STATUS");
			String createDateStr = request.getParamValue("CREATE_DATE_STR");
			String createDateEnd = request.getParamValue("CREATE_DATE_END");
			Map<String, String> map = new HashMap();
			map.put("roNo", roNo);
			map.put("vin", vin);
			map.put("repairType", repairType);
			map.put("roCreateDate", roCreateDate);
			map.put("deliveryDate", deliveryDate);
			map.put("isForl", isForl);
			map.put("roStatus", roStatus);
			map.put("dealerId", dealerId);
			map.put("createDateStr", createDateStr);
			map.put("createDateEnd", createDateEnd);
			PageResult<TtAsRepairOrderExtPO> ps = dao.queryRepairOrderApply(
					map, Constant.PAGE_SIZE, curPage);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔单维护");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * Iverson add with 2010-11-12
	 * 
	 * @Title: queryRepairOrderDelete
	 * @Description: TODO(查询工单可删除)
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	public void queryRepairOrderDelete() {

		String dealerId = loginUser.getDealerId();
		String dealerCode = loginUser.getDealerCode();
		try {

			Integer curPage = getCurrPage();
			String roNo = request.getParamValue("RO_NO");
			String vin = request.getParamValue("VIN");
			String repairType = request.getParamValue("REPAIR_TYPE");
			String roCreateDate = request.getParamValue("RO_CREATE_DATE");
			String deliveryDate = request.getParamValue("DELIVERY_DATE");
			String isForl = request.getParamValue("RO_FORE");
			String roStatus = request.getParamValue("RO_STATUS");
			String createDateStr = request.getParamValue("CREATE_DATE_STR");
			String createDateEnd = request.getParamValue("CREATE_DATE_END");
			String isChanghe = request.getParamValue("YIELDLY_TYPE");
			Map<String, String> map = new HashMap();
			map.put("roNo", roNo);
			map.put("vin", vin);
			map.put("repairType", repairType);
			map.put("roCreateDate", roCreateDate);
			map.put("deliveryDate", deliveryDate);
			map.put("isForl", isForl);
			map.put("roStatus", roStatus);
			map.put("dealerId", dealerId);
			map.put("createDateStr", createDateStr);
			map.put("createDateEnd", createDateEnd);
			map.put("isChanghe", isChanghe);
			PageResult<TtAsRepairOrderExtPO> ps = dao
					.queryDeleteRepairOrderNew(map, Constant.PAGE_SIZE, curPage);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "工单删除");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * 
	 * @Title: queryRepairOrder
	 * @Description: TODO(查询预授权(OEM))
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	public void queryRepairOrder1() {
		try {
			PageResult<TtAsWrForeapprovalBean> ps = dao.queryRepairOrder1(request, loginUser,Constant.PAGE_SIZE,getCurrPage());
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, "预授权维护");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * 
	 * @Title: queryRepairOrder
	 * @Description: TODO(经销商端查询预授权(OEM))
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	public void queryRepairOrderDealer() {

		String dealerId = loginUser.getDealerId();
		String dealerCode = loginUser.getDealerCode();
		try {

			Integer curPage = getCurrPage();
			String roNo = request.getParamValue("RO_NO");
			String vin = request.getParamValue("VIN");
			String repairType = request.getParamValue("REPAIR_TYPE");
			String roCreateDate = request.getParamValue("RO_CREATE_DATE");
			String deliveryDate = request.getParamValue("DELIVERY_DATE");
			String approveDate = request.getParamValue("approve_date");// 审核时间
			String approveDate2 = request.getParamValue("approve_date2");
			String isForl = request.getParamValue("RO_FORE");
			String roStatus = request.getParamValue("RO_STATUS");
			// String DId = request.getParamValue("dealerId");//经销商代码
			String Did = loginUser.getDealerId();
			String orgCode = request.getParamValue("orgCode");
			String isWarning = request.getParamValue("IS_WARNING");
			Map<String, String> map = new HashMap();
			map.put("roNo", roNo);
			map.put("vin", vin);
			map.put("repairType", repairType);
			map.put("roCreateDate", roCreateDate);
			map.put("deliveryDate", deliveryDate);
			map.put("isForl", isForl);
			map.put("roStatus", roStatus);
			map.put("dealerId", Did);
			map.put("orgCode", orgCode);
			map.put("approveDate", approveDate);
			map.put("approveDate2", approveDate2);
			map.put("isWarning", isWarning);
			PageResult<TtAsWrForeapprovalBean> ps = dao.queryRepairOrderDealer(
					map, Constant.PAGE_SIZE, curPage);
			act.setOutData("ps", ps);
			// act.setForword(RO_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "预授权维护");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}

	public void queryRepairOrderOEM() {

		String dealerId = loginUser.getDealerId();
		String dealerCode = loginUser.getDealerCode();
		try {

			Integer curPage = getCurrPage();
			String roNo = request.getParamValue("RO_NO");
			String vin = request.getParamValue("VIN");
			String repairType = request.getParamValue("REPAIR_TYPE");
			String roCreateDate = request.getParamValue("RO_CREATE_DATE");
			String deliveryDate = request.getParamValue("DELIVERY_DATE");
			String approveDate = request.getParamValue("approve_date");// 审核时间
			String approveDate2 = request.getParamValue("approve_date2");
			String auditName = request.getParamValue("auditName");// 授权人
			String isForl = request.getParamValue("RO_FORE");
			String roStatus = request.getParamValue("RO_STATUS");
			String DId = request.getParamValue("dealerId");// 经销商代码
			// String Did = loginUser.getDealerId();
			String orgCode = request.getParamValue("orgCode");
			String isWarning = request.getParamValue("IS_WARNING");
			String audit_date_end= request.getParamValue("audit_date_end");
			String audit_date_start = request.getParamValue("audit_date_start");
			String org_name = request.getParamValue("org_name");
			
			Map<String, String> map = new HashMap();
			map.put("org_name", org_name);
			map.put("audit_date_start", audit_date_start);
			map.put("audit_date_end", audit_date_end);
			map.put("roNo", roNo);
			map.put("vin", vin);
			map.put("repairType", repairType);
			map.put("roCreateDate", roCreateDate);
			map.put("deliveryDate", deliveryDate);
			map.put("isForl", isForl);
			map.put("roStatus", roStatus);
			map.put("dealerId", DId);
			map.put("orgCode", orgCode);
			map.put("approveDate", approveDate);
			map.put("approveDate2", approveDate2);
			map.put("auditName", auditName);
			map.put("orgId", loginUser.getOrgId().toString());
			map.put("isWarning", isWarning);
			PageResult<TtAsWrForeapprovalBean> ps = dao.queryRepairOrderOEM(
					map, loginUser, Constant.PAGE_SIZE, curPage);
			act.setOutData("ps", ps);
			// act.setForword(RO_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "预授权维护");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * 
	 * @Title: getOtherfeeStr
	 * @Description: TODO()
	 * @param
	 * @return 设定文件
	 * @return String 返回类型
	 * @throws
	 */
	public String getActivityStr() {
		List<TtAsActivityPO> seriesList = dao.queryActivityCombo();
		String retStr = "";
		retStr += "<option value=\',0,\'>-请选择-</option>";
		for (int i = 0; i < seriesList.size(); i++) {
			TtAsActivityPO bean = new TtAsActivityPO();
			bean = (TtAsActivityPO) seriesList.get(i);
			retStr += "<option value=\'" + bean.getActivityCode() + ","
					+ bean.getActivityFee() + "," + bean.getIsFixfee() + "\'>"
					+ bean.getActivityName() + "</option>";
		}
		return retStr;
	}

	/**
	 * 
	 * @Title: approve
	 * @Description: TODO(对工单授权)
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	public void approve() {

		try {

			String roNo = request.getParamValue("RO_NO");
			TtAsRepairOrderPO tarop = new TtAsRepairOrderPO();
			tarop.setRoNo(roNo);
			List<TtAsRepairOrderPO> ls = dao.select(tarop);
			String vin = "";
			Integer freeTimes = 0;

			if (!Utility.testString(roNo)) {
				act.setOutData("success", false);
				act.setOutData("reason", true); // 工单没有生成，不能授权
			} else {
				if (ls != null) {
					if (ls.size() > 0) {
						tarop = ls.get(0);
						vin = tarop.getVin();
						// freeTimes = tarop.getFreeTimes();
						TtAsRepairOrderPO tarop0 = new TtAsRepairOrderPO();
						tarop0.setRoNo(roNo);
						tarop0.setForlStatus(Constant.RO_FORE_01); // 工单预授权通过
						dao.update(tarop, tarop0);

						/** 预授权通过更新车辆信息表中的保养次数为工单中的保养次数 ****/
						TmVehiclePO tvps = new TmVehiclePO();
						tvps.setVin(vin);

						/***** add by liuxh 20131108判断车架号不能为空 *****/
						CommonUtils.jugeVinNull(vin);
						/***** add by liuxh 20131108判断车架号不能为空 *****/

						List<TmVehiclePO> lstvsp = dao.select(tvps);
						if (lstvsp != null) {
							if (lstvsp.size() > 0) {
								freeTimes = lstvsp.get(0).getFreeTimes();
							}
						}
						freeTimes = freeTimes + 1; // 更新车辆表中的保养次数为上次车辆表中的保养次数+1
						TmVehiclePO tvpsUp = new TmVehiclePO();
						if (Integer.valueOf(ls.get(0).getRepairTypeCode()
								.toString()) == 11441004) {
							tvpsUp.setFreeTimes(freeTimes);
						}
						dao.update(tvps, tvpsUp);
						act.setOutData("success", true);
						/*****************************************************/

					} else {
						act.setOutData("success", false);
						act.setOutData("reason", true); // 工单没有生成，不能授权

					}
				} else {
					act.setOutData("success", false);
					act.setOutData("reason", true); // 工单没有生成，不能授权
				}

			}

			// tarop.setIs

		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "质量信息");
			act.setOutData("success", false);
			act.setOutData("reason", false);
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * 
	 * @Title: verDupRo
	 * @Description: TODO(校验工单表中是否已经存在要插入的工单)
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	public void verDupRo() {

		try {

			String roNo = request.getParamValue("RO_NO");
			TtAsRepairOrderPO tarop = new TtAsRepairOrderPO();
			if (Utility.testString(roNo)) {
				tarop.setRoNo(roNo);
			} else {
				tarop.setRoNo("-1-1");
			}
			List<TtAsRepairOrderPO> ls = dao.select(tarop);
			if (ls != null) {
				if (ls.size() > 0) {
					act.setOutData("dup", true); // 有重复
				} else {
					act.setOutData("dup", false);
				}
			} else {
				act.setOutData("dup", false);
			}
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "质量信息");
			act.setOutData("success", false);
			logger.error(loginUser, e1);
			act.setException(e1);
		}

	}

	public void getFreeTimes() {

		int allTimes = 0;
		String vin = request.getParamValue("VIN"); // 车辆VIN
		List<Map<String, Object>> freeTimes = dao.viewFreeTime1(vin);
		if (freeTimes.get(0).get("FREE_TIMES") == null
				|| freeTimes.get(0).get("FREE_TIMES").equals("")) {
			allTimes = 0;
		} else {
			allTimes = Integer.valueOf(freeTimes.get(0).get("FREE_TIMES")
					.toString());
		}
		String free = getFreeAmount(vin);
		act.setOutData("needTime", allTimes + 1); // 需要做的保养次数
		act.setOutData("free",free );
	}

	//查询该车所在售后车型组保养费用
	public String getFreeAmount(String vin){
		List<Map<String, Object>> free = dao.viewFree(vin);
		if(free.size()>0&&free!=null){
			return free.get(0).get("FREE").toString();
		}
		return null;
	}
	
	/**
	 * 
	 * @Title: getFree
	 * @Description: TODO(根据OEMCOMPANYID取得保养是否需要授权)
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	public void getFree() {

		Long companyId = GetOemcompanyId.getOemCompanyId(loginUser); // 公司ID
		int freeTimesInApp = 0; // 申请过的免费保养次数
		int freeTimesInAppy = 0; // 预授权申请过的免费保养次数
		int freeTimesInAct = 0;
		int allTimes = 0;
		int day = 999999;
		int month = 999999; // 当前时间和保修开始时间相差月份
		int monthIsQa = 0; // 在规则表中的ont
		Double mileageDouble = 0.0;
		try {

			String vin = request.getParamValue("VIN"); // 车辆VIN
			String REPAIR_TYPE = request.getParamValue("REPAIR_TYPE");
			String bug = "";
			if("11441008".equals(REPAIR_TYPE))
			{
				TtAsRepairOrderPO orderPO = new TtAsRepairOrderPO();
				orderPO.setVin(vin);
				orderPO.setRepairTypeCode("11441003");
				List<TtAsRepairOrderPO> list= dao.select(orderPO);
				if(list.size() > 0 )
				{
					bug = "做了售前索赔的不能做pdi工单";
				}
				
			}
			if("11441003".equals(REPAIR_TYPE))
			{
				TtAsRepairOrderPO orderPO = new TtAsRepairOrderPO();
				orderPO.setVin(vin);
				orderPO.setRepairTypeCode("11441008");
				List<TtAsRepairOrderPO> list= dao.select(orderPO);
				if(list.size() > 0 )
				{
					orderPO = list.get(0);
					if(!(""+orderPO.getDealerId()).equals(""+loginUser.getDealerId()))
					{
						bug = "售前只能由做过PDI的服务站才能做";
					}
				}
			}
			
			String[] PAY_TYPE_PARTS = request.getParamValues("PAY_TYPE_PART");
			String[] PART_USE_TYPES = request.getParamValues("PART_USE_TYPE");
			String[] AMOUNTS = request.getParamValues("AMOUNT");
			if(PAY_TYPE_PARTS!=null&&PAY_TYPE_PARTS.length > 0 )
			{
				for( int i = 0 ; i < PAY_TYPE_PARTS.length;i++  )
				{
					if(PAY_TYPE_PARTS[i].equals("11801002") &&"95431002".equals(PART_USE_TYPES[i]) && AMOUNTS[i].equals("0.00"))
					{
						bug = "该件更换状态下材料费为零，请联系总部确认";
					}
				}
			}
			
			
			act.setOutData("bug", bug);
			
			String is_domestic = "";
			String repairasqcar = "";
			if (vin == null || vin.equals("")) {
				throw new Exception("----------未从页面获取VIN码-------------");
			}
			TmVehiclePO vehiclePO = new TmVehiclePO();
			vehiclePO.setVin(vin);
			List<TmVehiclePO> vinList = dao.select(vehiclePO);
			boolean fag = true;
			if (vinList.size() > 0) {
				int domestic = vinList.get(0).getIsDomestic();
				String  Qcar=""+ vinList.get(0).getRepairAsQcar();
				if (domestic == 1  || Qcar.equals("18051003")) {
					String[] PAY_TYPE_ITEM = request
							.getParamValues("PAY_TYPE_ITEM");
					String[] PAY_TYPE_PART = request
							.getParamValues("PAY_TYPE_PART");

					if (PAY_TYPE_ITEM.length > 0) {
						for (String pay_type_item : PAY_TYPE_ITEM) {
							if (pay_type_item.equals("11801002")) {
								fag = false;
								break;
							}
						}
					}

					if (PAY_TYPE_PART.length > 0) {
						for (String pay_type_part : PAY_TYPE_PART) {
							if(pay_type_part.equals("11801002"))
							{
								fag = false;
								break;
							}
							
						}
					}
					if (!fag) {
						is_domestic = "该车已脱保";
					}

				}
				
			}

			act.setOutData("is_domestic", is_domestic);

			String mileage = request.getParamValue("IN_MILEAGE");// 进厂里程数
			String do_no = request.getParamValue("Do_No");
			String purchasedDate = request.getParamValue("GUARANTEE_DATE"); // 保修开始时间
			List<Map<String, Object>> mileage_vin = dao.getMileage(vin);// 获取该车系统的最大行驶里程
			String db = request.getParamValue("DB");// 用于区分是修改页面还是新增页面
			Date date = new Date();
			if (Utility.testString(purchasedDate)) {
				date = Utility.getDate(purchasedDate, 1);
			} else {
				mileageDouble = Utility.getDouble(mileage);
				TmVehiclePO tvp = new TmVehiclePO();
				tvp.setVin(vin);

				/***** add by liuxh 20131108判断车架号不能为空 *****/
				CommonUtils.jugeVinNull(vin);
				/***** add by liuxh 20131108判断车架号不能为空 *****/

				List<TmVehiclePO> lsv = dao.select(tvp);
				if (lsv != null) {
					if (lsv.size() > 0) {
						tvp = lsv.get(0);
					}
				}
				date = tvp.getPurchasedDate(); // 保修改开始时间
			}
			Date now = new Date(); // 今天
			if (date != null) {
				String formatStyle = "yyyy-MM-dd HH:mm";
				SimpleDateFormat df = new SimpleDateFormat(formatStyle);
				String d1 = df.format(date);
				String d2 = df.format(now);
				month = Utility.compareDate(d1, d2, 1); // 取得今日和保养开始时间的插值
				day = Utility.compareDate(d1, d2, 0); // 取得今日和保养开始时间的插值 天数
			}
			// 以下freeTimesInApp，freeTimesInAppy在取出的值没有用到 将其注释掉
			// //工单中该车的保养次数(不需要预授权的)
			// TtAsRepairOrderPO tarop = new TtAsRepairOrderPO();
			// tarop.setVin(vin);
			// tarop.setApprovalYn(Constant.APPROVAL_YN_NO);
			// tarop.setRepairTypeCode(Constant.REPAIR_TYPE_04);
			// List<TtAsRepairOrderPO> lsa = dao.select(tarop);
			// if (lsa!=null) {
			// freeTimesInApp = lsa.size(); //取得免费保养申请次数
			// }
			// //需要预授权的
			// TtAsRepairOrderPO taropApp = new TtAsRepairOrderPO();
			// taropApp.setVin(vin);
			// taropApp.setApprovalYn(Constant.APPROVAL_YN_YES);
			// taropApp.setRepairTypeCode(Constant.REPAIR_TYPE_04);
			// taropApp.setForlStatus(Constant.RO_FORE_02);
			// List<TtAsRepairOrderPO> lsapp = dao.select(taropApp);
			// if (lsa!=null) {
			// freeTimesInAppy = lsapp.size(); //取得免费保养申请次数
			// }
			// 服务活动中该车的保养次数
			// freeTimesInAct = dao.getActFree(vin);
			// allTimes = freeTimesInApp + freeTimesInAct+freeTimesInAppy;
			List<Map<String, Object>> freeTimes = dao.viewFreeTime1(vin);
			List<TtAsWrQamaintainPO> lsq = null;
			int in_milage = 0;
			if (db != null && !db.equals("")) {
				List<Map<String, Object>> DOList = dao.viewFreeTimeDoNo(do_no);
				allTimes = Integer.valueOf(DOList.get(0).get("FREE_TIMES")
						.toString());
				lsq = dao.getFree(allTimes, companyId, month, day,
						Double.valueOf(mileage));
				in_milage = Integer.valueOf(DOList.get(0).get("IN_MILEAGE")
						.toString());
			} else {
				// 判断保养次数是否为空
				if (freeTimes.get(0).get("FREE_TIMES") == null
						|| freeTimes.get(0).get("FREE_TIMES").equals("")) {
					allTimes = 0;
				} else {
					allTimes = Integer.valueOf(freeTimes.get(0)
							.get("FREE_TIMES").toString());
				}

				lsq = dao.getFree(allTimes + 1, companyId, month, day,
						Double.valueOf(mileage));
			}
			/** 最新修改需要从车辆表中取保养次数+1 */
			/****************************************/

			if (lsq != null) {
				// 存在记录，不需要授权
				if (lsq.size() > 0) {
					act.setOutData("approve", false);
				} else {
					act.setOutData("approve", true);
				}
			} else {
				act.setOutData("approve", true);
			}
			Map mileageMap = new HashMap();
			mileageMap.put("MILEAGE", -1);
			act.setOutData("needTime", allTimes + 1); // 需要做的保养次数
			act.setOutData("in_milage", in_milage);
			act.setOutData("mileage_vin", mileage_vin.size() == 0 ? mileageMap
					: mileage_vin.get(0));// 最大行驶里程

			// zhumingwei 2012-12-25 判断这个vin是否在不在这个用户的业务范围里面
			String userId = getCurrDealerId().toString();
			String vin1 = request.getParamValue("VIN");
			act.setOutData("flag", "true");
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "质量信息");
			act.setOutData("success", false);
			logger.error(loginUser, e1);
			act.setException(e1);
		}

	}

	/**
	 * 
	 * @Title: getGuaFlag
	 * @Description: TODO(判断配件是否在三包期内)
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	public void getGuaFlag() {

		try {

			String repairType = request.getParamValue("repairType");
			String partCode = request.getParamValue("partCode");
			String vin = request.getParamValue("vin");
			String inMileage = request.getParamValue("inMileage");
			String purchasedDate = request.getParamValue("purchasedDate"); // 保修开始时间
			purchasedDate = purchasedDate + " 00:00";
			String noNo = "";
			String notice = "";
			boolean flag = true;
			/**
			 * 开始判断是否是配件三包期
			 */
			TmVehiclePO vp = new TmVehiclePO();
			vp.setVin(vin);
			/***** add by liuxh 20131108判断车架号不能为空 *****/
			CommonUtils.jugeVinNull(vin);
			/***** add by liuxh 20131108判断车架号不能为空 *****/
			vp = (TmVehiclePO) dao.select(vp).get(0);
			if (vp.getClaimTacticsId() != null && vp.getPurchasedDate() != null
					&& !Constant.REPAIR_TYPE_05.equalsIgnoreCase(repairType)) {
				// 比较购车日期和当前日期之间的差值
				String d2 = DateTimeUtil.parseDateToString2(new Date());// 将当前时间转化为
																		// 字符串
				int month = Utility.compareDate(purchasedDate, d2, 1); // 获取相差月数
				int day = Utility.compareDate(purchasedDate, d2, 0);// 获取相差天数
				if (month % 12 == 0 && day > (month % 12) * 365) {
					month = month + 1;
				}
				TtAsWrRuleListPO lp = new TtAsWrRuleListPO();
				List list = dao.getNoFamily(vp.getClaimTacticsId(), partCode);
				List<TtAsWrGamePO> gList = dao.isFamily(vp.getClaimTacticsId());
				if (gList != null && gList.size() > 0) {// 如果是家用车
					TtAsWrGamePO gp = (TtAsWrGamePO) gList.get(0);
					// 家用车三包判定
					TmPtPartBasePO bp = new TmPtPartBasePO();
					bp.setPartCode(partCode);
					bp = (TmPtPartBasePO) dao.select(bp).get(0);
					if (Constant.PART_WR_TYPE_2.equalsIgnoreCase(bp
							.getPartWarType().toString())) {// 如果是易损易耗件
						if (month <= bp.getWrMonths()
								&& Double.valueOf(inMileage) <= bp
										.getWrMileage()) {// 在配件三包内
							flag = true;
							noNo = "in";
						} else {
							flag = false;
							notice += "该配件已经超配件三包期!\n";
							noNo = "out";// 超过配件三包期
						}
					} else {// 不是易损易耗件,则按常规件判定，包括：保修期,配件三包规则中的较大者
						if (list != null && list.size() > 0) {// 如果在三包规则里面有维护,则取
																// 保修期,配件三包规则中的较大者
																// 进行比较
							lp = (TtAsWrRuleListPO) list.get(0);
							int month2 = Utility.compareIntMax(
									gp.getClaimMonth(), lp.getClaimMonth());
							double melieage = Utility.compareDoubleMax(
									gp.getClaimMelieage(),
									lp.getClaimMelieage());
							if (month <= month2
									&& Double.valueOf(inMileage) <= melieage) {// 在配件三包内
								flag = true;
								noNo = "in";
							} else {
								flag = false;
								notice += "该配件已经超配件三包期!\n";
								noNo = "out";// 超过配件三包期
							}

						} else {// 如果没有维护三包规则,直接取三包策略的包修期
							if (month <= gp.getClaimMonth()
									&& Double.valueOf(inMileage) <= gp
											.getClaimMelieage()) {// 在配件三包内
								flag = true;
								noNo = "in";
							} else {
								flag = false;
								notice += "该配件已经超配件三包期!\n";
								noNo = "out";// 超过配件三包期
							}
						}
					}
				} else {// 如果不是家用车,则直接按照配件三包规则判断
					if (list != null && list.size() > 0) {
						lp = (TtAsWrRuleListPO) list.get(0);
						if (month <= lp.getClaimMonth()
								&& Integer.parseInt(inMileage) <= lp
										.getClaimMelieage()) {// 在配件三包内
							flag = true;
							noNo = "in";
						} else {
							flag = false;
							notice += "该配件已经超配件三包期!\n";
							noNo = "out";// 超过配件三包期
						}
					} else {
						flag = false;
						notice += "该配件没有维护配件三包规则!\n";
						noNo = "NoGame";// 非家用车,没有维护配件三包规则
					}
				}
			} else {
				flag = true;
			}
			act.setOutData("flag", flag);
			act.setOutData("noNo", noNo);
			act.setOutData("notice", notice);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "质量信息");
			act.setOutData("success", false);
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	
	public void getGuaFlag1() {
		try {
			String repairType = request.getParamValue("repairType");
			String partCode = request.getParamValue("partCode");
			String vin = request.getParamValue("vin");
			String inMileage = request.getParamValue("inMileage");
			String zfRoNo = request.getParamValue("zfRoNo");
			String purchasedDate = request.getParamValue("purchasedDate"); // 保修开始时间
			purchasedDate = purchasedDate + " 00:00";
			String noNo = "";
			String notice = "";
			boolean flag = true;
			
			TtAsRepairOrderPO po = new TtAsRepairOrderPO();
			po.setRoNo(zfRoNo);
			TtAsRepairOrderPO poValue = (TtAsRepairOrderPO)dao.select(po).get(0);
			String beginTime=DateTimeUtil.parseDateToString2(poValue.getRoCreateDate());//工单开始时间
			double inMileage1 = poValue.getInMileage();//里程数
			double inMileage2 = Double.parseDouble(inMileage)-inMileage1;
			
			
			String d2 = DateTimeUtil.parseDateToString2(new Date());// 将当前时间转化为字符串
			int month = Utility.compareDate(purchasedDate, d2, 1); // 获取相差月数
			int day = Utility.compareDate(beginTime, d2, 0);// 获取相差天数
			if (month % 12 == 0 && day > (month % 12) * 365) {
				month = month + 1;
			}
			
			TtAsWrBackupListPO popo = new TtAsWrBackupListPO();
			popo.setPartCode(partCode);
			TtAsWrBackupListPO popoValue = (TtAsWrBackupListPO)dao.select(popo).get(0);
			int month1 = popoValue.getClaimMonth();
			double inMileage3 = popoValue.getClaimMelieage();
			
			if (month<=month1 && inMileage2<=inMileage3){
				flag=true;
				noNo = "NoGame";
			}else{
				flag=false;
				noNo = "NoGame";
				notice = "该配件已经超配件三包期!\n";
			}
			
			act.setOutData("flag", flag);
			act.setOutData("noNo", noNo);
			act.setOutData("notice", notice);
			
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "质量信息");
			act.setOutData("success", false);
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}

	/*******
	 * 预授权申请单审核通过
	 * 
	 * @throws Exception
	 */
	public void updateForStatus() throws Exception {

		String id = request.getParamValue("ID");// 获取工单ID
		String fid = request.getParamValue("fId");// 获取预授权ID
		String opintion = request.getParamValue("opintion1");// 审核内容
		Integer payment = Integer.parseInt(request.getParamValue("payment"));// 是否同意费用
		payment = 0;
		try {
			/**
			 * 开始判断是否还有下级审核人，依照次结果才能更新数据
			 */
			TtAsWrForeapprovalPO fp = new TtAsWrForeapprovalPO();
			fp.setId(Utility.getLong(fid));
			List<TtAsWrForeapprovalPO> foreList = dao.select(fp);
			String nowEditor = foreList.get(0).getCurrentAuditor();// 得到当前审核人
			String needEditor = foreList.get(0).getNeedAuditor();// 得到需要审核的人
			String nextEditor = "";
			String[] need = {};
			if (needEditor != "" && needEditor != null) {
				need = needEditor.split(",");
			}
			for (int i = 0; i < need.length; i++) {// 得到下一级审核代码
				if (nowEditor.trim().equalsIgnoreCase(need[i].trim())) {
					if ((i + 1) < need.length) {
						nextEditor = need[i + 1].trim();
					}
				}
			}
			// 开始更新数据

			if (nextEditor != "") {// 如果还有下一级审核代码，则 该预授权还未通过，
				TtAsWrForeapprovalPO p = new TtAsWrForeapprovalPO();
				TtAsWrForeapprovalPO p2 = new TtAsWrForeapprovalPO();
				p.setId(Utility.getLong(fid));
				p2.setCurrentAuditor(nextEditor);
				p2.setOpinion(opintion);
				p2.setAuditPerson(getCurrDealerId());
				p2.setUpdateBy(getCurrDealerId());
				p2.setUpdateDate(new Date());
				p2.setAccreditAmount(payment);
				dao.update(p, p2);// 更新预授权主表相关数据
			} else if (nextEditor == "") {
				System.out.println("木有下一级了！。。。。。。。。");
				String vin = "-1";
				Integer freeTimes = 0;

				TtAsRepairOrderPO tarop = new TtAsRepairOrderPO();
				tarop.setId(Utility.getLong(id));
				List<TtAsRepairOrderPO> ls = dao.select(tarop);
				if (ls != null) {
					if (ls.size() > 0) {
						tarop = ls.get(0);
						vin = tarop.getVin();
						freeTimes = tarop.getFreeTimes();
					}
				}
				/** 预授权通过更新车辆信息表中的保养次数为工单中的保养次数 ****/

				TmVehiclePO tvps = new TmVehiclePO();
				tvps.setVin(vin);

				/***** add by liuxh 20131108判断车架号不能为空 *****/
				CommonUtils.jugeVinNull(vin);
				/***** add by liuxh 20131108判断车架号不能为空 *****/

				List<TmVehiclePO> lstvsp = dao.select(tvps);

				TmVehiclePO tvpsUp = new TmVehiclePO();
				/**
				 * 更新车辆的保养次数改在了 结算和取消结算上面，为不影响数据 此处不再更新了
				 */
				// if(Integer.valueOf(ls.get(0).getRepairTypeCode().toString())==11441004)
				// {
				// if(Integer.parseInt(lstvsp.get(0).getFreeTimes().toString())<freeTimes){
				// tvpsUp.setFreeTimes(freeTimes);
				// //用于取购车时间
				// TmVehiclePO tvp = new TmVehiclePO();
				// tvp.setVin(vin);
				// List<TmVehiclePO> lsv = dao.select(tvp);
				// if(lsv!=null&&lsv.size()>0){
				// if(lsv.get(0).getPurchasedDate()!=null){
				// dao.update(tvps,tvpsUp);
				// }
				// }
				// }
				//
				// }
				dao.updateTtAsWrForeapproval2(fid, opintion, payment);
				dao.updateTtAsRepairOrder(fid, payment, loginUser);
				dao.updateOrder(id);
				if (Constant.REPAIR_TYPE_07.equalsIgnoreCase(foreList.get(0)
						.getApprovalType().toString())) {
					// Utility.setOrderSbjj(id,Constant.PART_CODE_RELATION_49,"",
					// conn);
					POFactory poFactory = POFactoryBuilder.getInstance();
					List ins = new LinkedList<Object>();
					ins.add(id.toString());
					ins.add(Constant.PART_CODE_RELATION_49);
					ins.add(1);
					ins.add(0);
					poFactory.callProcedure("P_CREATEORDER", ins, null);
				}
			}
			TtAsWrAuthinfoPO ap = new TtAsWrAuthinfoPO();
			ap.setApprovalLevelCode(nowEditor);
			ap.setType("0");
			ap = (TtAsWrAuthinfoPO) dao.select(ap).get(0);
			TtAsWrForeauthdetailPO afp = new TtAsWrForeauthdetailPO();
			afp.setId(Utility.getLong(SequenceManager.getSequence("")));
			afp.setFid(Utility.getLong(fid));
			afp.setApprovalLevelCode(nowEditor);
			afp.setAuditDate(new Date());
			afp.setAuditPerson(loginUser.getName());
			TcPosePO p = new TcPosePO();
			p.setPoseId(loginUser.getPoseId());
			List<TcPosePO> pList = dao.select(p);
			afp.setAuditLevelNmae(ap.getApprovalLevelName());
			afp.setAuditResult(Constant.RO_FORE_02.toString());
			afp.setRemark(opintion);
			afp.setCreateDate(new Date());
			afp.setCreateBy(getCurrDealerId());
			dao.insert(afp);
			//预授权对补偿费的审核===============zyw 2014-11-19
			String[] pass_price = DaoFactory.getParams(request, "pass_price");
			String[] pkid = DaoFactory.getParams(request, "pkid");
			if(pkid!=null && pkid.length>0){
				int temp=0;
				for (String pid : pkid) {
					TtAsWrCompensationMoneyPO com1=new TtAsWrCompensationMoneyPO();
					com1.setPkid(BaseUtils.ConvertLong(pid));
					TtAsWrCompensationMoneyPO com2=new TtAsWrCompensationMoneyPO();
					com2.setPassPrice(BaseUtils.ConvertDouble(pass_price[temp]));
					dao.update(com1, com2);
					temp++;
				}
			}
			//====================================
			act.setOutData("success", "审核成功!");
			act.setForword(RO_SHOP_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "质量信息");
			act.setOutData("success", false);
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * Iverson add By 2010-12-09 预授权申请单批量审核通过
	 */
	public void updateForStatus2() {

		Integer payment = 0;
		String recesel[] = request.getParamValues("recesel");// 获取一个集合预授权的ID
		String opintion = request.getParamValue("optiona");

		// 根据与授权ID得到工单号然后再根据工单号得到对应的工单ID
		TtAsWrForeapprovalPO po = new TtAsWrForeapprovalPO();
		TtAsWrForeapprovalPO poValue = new TtAsWrForeapprovalPO();
		TtAsRepairOrderPO orderPo = new TtAsRepairOrderPO();
		TtAsRepairOrderPO orderPoValue = new TtAsRepairOrderPO();
		for (String id : recesel) {
			po.setId(Long.parseLong(id));
			poValue = (TtAsWrForeapprovalPO) dao.select(po).get(0);
			String roNo = poValue.getRoNo();
			orderPo.setRoNo(roNo);
			orderPoValue = (TtAsRepairOrderPO) dao.select(orderPo).get(0);
			String orderId = orderPoValue.getId().toString();

			try {

				/**
				 * 开始判断是否还有下级审核人，依照次结果才能更新数据
				 */
				TtAsWrForeapprovalPO fp = new TtAsWrForeapprovalPO();
				fp.setId(Utility.getLong(id));
				List<TtAsWrForeapprovalPO> foreList = dao.select(fp);
				String nowEditor = foreList.get(0).getCurrentAuditor();// 得到当前审核人
				String needEditor = foreList.get(0).getNeedAuditor();// 得到需要审核的人
				String nextEditor = "";
				String[] need = {};
				if (needEditor != "" && needEditor != null) {
					need = needEditor.split(",");
				}

				for (int i = 0; i <= need.length - 1; i++) {// 得到下一级审核代码
					if (nowEditor.trim().equalsIgnoreCase(need[i].trim())) {
						if ((i + 1) < need.length) {
							System.out.println((i + 1) < need.length);
							nextEditor = need[i + 1].trim();
						}
					}
				}
				if (nextEditor != "") {// 如果还有下一级审核代码，则 该预授权还未通过，
					TtAsWrForeapprovalPO p = new TtAsWrForeapprovalPO();
					TtAsWrForeapprovalPO p2 = new TtAsWrForeapprovalPO();
					p.setId(Utility.getLong(id));
					p2.setCurrentAuditor(nextEditor);
					p2.setOpinion(opintion);
					p2.setUpdateBy(getCurrDealerId());
					p2.setUpdateDate(new Date());
					p2.setAccreditAmount(payment);
					dao.update(p, p2);// 更新预授权主表相关数据
				} else if (nextEditor == "") {
					String vin = "-1";
					Integer freeTimes = 0;
					TtAsRepairOrderPO tarop = new TtAsRepairOrderPO();
					tarop.setId(Utility.getLong(orderId));
					List<TtAsRepairOrderPO> ls = dao.select(tarop);
					if (ls != null) {
						if (ls.size() > 0) {
							tarop = ls.get(0);
							vin = tarop.getVin();
							freeTimes = tarop.getFreeTimes();
						}
					}
					/** 预授权通过更新车辆信息表中的保养次数为工单中的保养次数 ****/
					TmVehiclePO tvps = new TmVehiclePO();
					tvps.setVin(vin);

					/***** add by liuxh 20131108判断车架号不能为空 *****/
					CommonUtils.jugeVinNull(vin);
					/***** add by liuxh 20131108判断车架号不能为空 *****/

					List<TmVehiclePO> lstvsp = dao.select(tvps);
					// freeTimes=freeTimes+1; //更新车辆表中的保养次数为上次车辆表中的保养次数+1
					TmVehiclePO tvpsUp = new TmVehiclePO();
					int aaa = Integer.valueOf(ls.get(0).getRepairTypeCode()
							.toString());
					int bbb = Integer.valueOf(Constant.REPAIR_TYPE_04);

					if (Integer.parseInt(ls.get(0).getRepairTypeCode()
							.toString()) == Integer
							.parseInt(Constant.REPAIR_TYPE_04)) {
						if (Integer.parseInt(lstvsp.get(0).getFreeTimes()
								.toString()) < freeTimes) {
							tvpsUp.setFreeTimes(freeTimes);
							// 用于取购车时间
							TmVehiclePO tvp = new TmVehiclePO();
							tvp.setVin(vin);

							/***** add by liuxh 20131108判断车架号不能为空 *****/
							CommonUtils.jugeVinNull(vin);
							/***** add by liuxh 20131108判断车架号不能为空 *****/

							List<TmVehiclePO> lsv = dao.select(tvp);
							if (lsv != null && lsv.size() > 0) {
								if (lsv.get(0).getPurchasedDate() != null) {
									dao.update(tvps, tvpsUp);
								}
							}
						}

					}
					/** 预授权通过更新车辆信息表中的保养次数为工单中的保养次数 ****/
					dao.updateTtAsWrForeapproval2(id, opintion, payment);
					dao.updateTtAsRepairOrder(id, payment, loginUser);
					dao.updateOrder(orderId);
				}
				TtAsWrForeauthdetailPO afp = new TtAsWrForeauthdetailPO();
				afp.setId(Utility.getLong(SequenceManager.getSequence("")));
				afp.setFid(Utility.getLong(id));
				afp.setApprovalLevelCode(nowEditor);
				afp.setAuditDate(new Date());
				afp.setAuditPerson(loginUser.getName());
				TcPosePO p = new TcPosePO();
				p.setPoseId(loginUser.getPoseId());
				List<TcPosePO> pList = dao.select(p);
				afp.setAuditLevelNmae(pList.get(0).getPoseName());
				afp.setAuditResult(Constant.RO_FORE_02.toString());
				afp.setRemark(opintion);
				afp.setCreateDate(new Date());
				afp.setCreateBy(getCurrDealerId());
				dao.insert(afp);// 插入审核明细表，但是不更新工单
				act.setOutData("msg", "01");
			} catch (Exception e) {
				BizException e1 = new BizException(act, e,
						ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔预授权");
				act.setOutData("success", false);
				logger.error(loginUser, e1);
				act.setException(e1);
			}
		}
	}

	/*******
	 * 预授权申请单审核退回 只要有一次退回，整个申请被退回
	 * 
	 * @throws Exception
	 */
	public void backForStatus() throws Exception {

		String id = request.getParamValue("ID");// 获取公单ID
		String fid = request.getParamValue("fId");// 获取预授权ID
		String opintion = request.getParamValue("opintion1");// 审核内容
		dao.backTtAsWrForeapproval(fid, opintion);
		dao.backOrder(id);

		TtAsWrForeapprovalPO fp = new TtAsWrForeapprovalPO();
		fp.setId(Long.valueOf(fid));
		List<TtAsWrForeapprovalPO> foreList = dao.select(fp);
		String nowEditor = foreList.get(0).getCurrentAuditor();// 得到当前审核人
		String needEditor = foreList.get(0).getNeedAuditor();// 得到需要审核的人

		TtAsWrForeauthdetailPO afp = new TtAsWrForeauthdetailPO();
		afp.setId(Utility.getLong(SequenceManager.getSequence("")));
		afp.setFid(Long.valueOf(fid));
		afp.setApprovalLevelCode(nowEditor);
		afp.setAuditDate(new Date());
		afp.setAuditPerson(loginUser.getName());
		TtAsWrAuthinfoPO af = new TtAsWrAuthinfoPO();
		af.setApprovalLevelCode(nowEditor);
		List<TtAsWrAuthinfoPO> afList = dao.select(af);
		afp.setAuditLevelNmae(afList.get(0).getApprovalLevelName());
		afp.setAuditResult(Constant.RO_FORE_03.toString());
		afp.setRemark(opintion);
		afp.setCreateDate(new Date());
		afp.setCreateBy(getCurrDealerId());
		dao.insert(afp);// 插入审核明细表，
		act.setOutData("success", "退回成功!");
		act.setForword(RO_SHOP_URL);
	}// backForStatus

	/**
	 * 查询审核明细
	 * 
	 * @author KFQ
	 */
	public void authDetail() {

		try {

			String vin = request.getParamValue("VIN");
			String ro_no = DaoFactory.getParam(request, "RO_NO");
			List<Map<String, Object>> detail = dao.authDetail(vin,ro_no);
			// act.setOutData("detail", detail);
			request.setAttribute("detail", detail);
			act.setForword(this.AUTHDETAIL_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "车辆维修历史记录");
			act.setOutData("success", false);
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}

	public void applicationRoNo() {

		String roNo = request.getParamValue("roNo");
		List<Map<String, Object>> app = dao.getApplication(roNo);
		if (app.size() > 0) {
			act.setOutData("success", true);

		} else {

			act.setOutData("success", false);
		}

	}

	/**********
	 * 经销商dealerCode截取长度不足的补"X"
	 * 
	 * @return
	 */
	public String sequenceRoNo() {

		String dealerCode = loginUser.getDealerCode();
		int len = dealerCode.length();
		String code = "";
		String code1 = "";
		if (len < 8) {
			int i = 8 - len;
			for (int k = 0; k < i; k++) {
				String zero = "0";
				code1 += zero;
			}
			code = dealerCode.substring(0, len) + code1;
		}
		if (len >= 8) {
			code = dealerCode.substring(0, 8);

		}
		return code;
	}

	// 判断是否脱保
	public Integer isFree(String vin, Double mileage, long companyId)
			throws Exception {
		int allTimes = 0;
		Date date = new Date();
		int day = 999999;
		int month = 999999; // 当前时间和保修开始时间相差月份
		TmVehiclePO tvp = new TmVehiclePO();
		tvp.setVin(vin);

		/***** add by liuxh 20131108判断车架号不能为空 *****/
		CommonUtils.jugeVinNull(vin);
		/***** add by liuxh 20131108判断车架号不能为空 *****/

		List<TmVehiclePO> lsv = dao.select(tvp);
		if (lsv != null) {
			if (lsv.size() > 0) {
				tvp = lsv.get(0);
				date = tvp.getPurchasedDate(); // 保修改开始时间
			}
		}
		Date now = new Date(); // 今天
		if (date != null) {
			String formatStyle = "yyyy-MM-dd";
			SimpleDateFormat df = new SimpleDateFormat(formatStyle);
			String d1 = df.format(date);
			String d2 = df.format(now);
			month = Utility.compareDate(d1, d2, 1); // 取得今日和保养开始时间的插值
			day = Utility.compareDate(d1, d2, 0); // 取得今日和保养开始时间的插值 天数
		}
		List<Map<String, Object>> freeTimes = dao.viewFreeTime1(vin);
		// 判断保养次数是否为空
		if (freeTimes.get(0).get("FREE_TIMES") == null
				|| freeTimes.get(0).get("FREE_TIMES").equals("")) {
			allTimes = 0;
		} else {
			allTimes = Integer.valueOf(freeTimes.get(0).get("FREE_TIMES")
					.toString());
		}
		List<TtAsWrQamaintainPO> lsq = null;
		lsq = dao.getFree(allTimes + 1, companyId, month, day,
				Double.valueOf(mileage));
		if (lsq != null && lsq.size() > 0) {
			// 存在记录，不需要授权
			if (lsq.size() > 0) {
				return 1;
			} else {
				return 0;
			}
		} else {
			return 0;
		}
	}

	// 判断配件是否三包内的通用规则
	public Integer partIsGua(String purchasedDate, String inMileage,
			String vin, String partCode) {
		int isGua = 0;
		WrRuleUtil util = new WrRuleUtil();
		if (purchasedDate == null || purchasedDate.equals("")) {
			isGua = 0;
		} else {
			try {
				WarrantyPartVO wp = util.wrRuleCompute(inMileage,
						purchasedDate, vin, partCode);
				if (wp.getIsInWarranty() == Constant.IF_TYPE_YES) {

					isGua = 1;
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return isGua;
	}

	/**
	 * 删除工单信息
	 * 
	 * @throws BizException
	 */
	public void deleteRepairOrder() throws BizException {

		try {
			/**
			 * 判断先维修工单结算：未结算的单据且是该车系统里程数最大的一张可以作废， 预授权审核通过、审核中的单据不能作废，已保存
			 * 和预授权审核退回的可以作废 直接在工单操作页面增加作废按钮，作废后车辆里程需要回滚
			 */
			String vin = request.getParamValue("vin");// vin
			String id = request.getParamValue("ID");// 工单ID
			String ver = request.getParamValue("VER");
			TtAsRepairOrderPO temp = new TtAsRepairOrderPO();
			temp.setVin(vin);
			List<TtAsRepairOrderPO> listTemp = dao.select(temp);

			// 首先根据ID查询这条记录
			TtAsRepairOrderPO poById1 = new TtAsRepairOrderPO();
			poById1.setId(Long.parseLong(id));
			List<TtAsRepairOrderPO> list1 = dao.select(poById1);
			Double inMileage = list1.get(0).getInMileage();
			List<TtAsRepairOrderPO> select =null;
			String str = "";
			String sql = "select t.* from tt_as_repair_order t where t.create_date =(select max(r.create_date) from tt_as_repair_order r where  r.id not in ("
					+ id + ") and r.vin='" + vin + "')";
			 select = dao.select(TtAsRepairOrderPO.class, sql, null);
			if(select!=null && select.size()==0){
				String sql1 = "select t.* from tt_as_repair_order t where t.create_date =(select max(r.create_date) from tt_as_repair_order r where  r.id in ("
						+ id + ") and r.vin='" + vin + "')";
				select = dao.select(TtAsRepairOrderPO.class, sql1, null);
			}
			Double inMileage2 = select.get(0).getInMileage();
			for (TtAsRepairOrderPO ttAsRepairOrderPO : listTemp) {// 循环工单的
				if (ttAsRepairOrderPO.getInMileage() > inMileage) {
					str += ttAsRepairOrderPO.getRoNo() + ";";
				}
			}
			if (!str.equals("")) {
				act.setOutData("msgTemp", "单号为：" + str
						+ "大于了另外工单的里程，请先废弃大里程的工单");
			} else {
				act.setOutData("msgTemp", "");
				// 回滚最近的那个时间的里程
				TmVehiclePO pp = new TmVehiclePO();
				pp.setMileage(inMileage2);

				TmVehiclePO pp1 = new TmVehiclePO();
				pp1.setVin(vin);
				dao.update(pp1, pp);
				Boolean flag = true;
				// 首先根据ID查询这条记录
				TtAsRepairOrderPO poById = new TtAsRepairOrderPO();
				poById.setId(Long.parseLong(id));
				List list = dao.selectRepairOrderById(poById);
				TtAsRepairOrderPO poBackValue = (TtAsRepairOrderPO) list.get(0);
				if (poBackValue.getVer() != Integer.parseInt(ver == null ? "0"
						: ver)) {
					flag = false;
				}
				if (flag) {
					// 其次在备份表里面新增记录
					TtAsRepairOrderBackupPO poBack = new TtAsRepairOrderBackupPO();
					poBack.setId(Long.parseLong(id));
					poBack.setDealerCode(poBackValue.getDealerCode());
					poBack.setRoNo(poBackValue.getRoNo());
					poBack.setSalesPartNo(poBackValue.getSalesPartNo());
					poBack.setBookingOrderNo(poBackValue.getBookingOrderNo());
					poBack.setEstimateNo(poBackValue.getEstimateNo());
					poBack.setSoNo(poBackValue.getSoNo());
					poBack.setRoType(poBackValue.getRoType());
					poBack.setRepairTypeCode(poBackValue.getRepairTypeCode());
					poBack.setOtherRepairType(poBackValue.getOtherRepairType());
					poBack.setVehicleTopDesc(poBackValue.getVehicleTopDesc());
					poBack.setSequenceNo(poBackValue.getSequenceNo());
					poBack.setPrimaryRoNo(poBackValue.getPrimaryRoNo());
					poBack.setInsurationNo(poBackValue.getInsurationNo());
					poBack.setInsurationCode(poBackValue.getInsurationCode());
					poBack.setIsCustomerInAsc(poBackValue.getIsCustomerInAsc());
					poBack.setIsSeasonCheck(poBackValue.getIsSeasonCheck());
					poBack.setOilRemain(poBackValue.getOilRemain());
					poBack.setIsWash(poBackValue.getIsWash());
					poBack.setIsTrace(poBackValue.getIsTrace());
					poBack.setTraceTime(poBackValue.getTraceTime());
					poBack.setNoTraceReason(poBackValue.getNoTraceReason());
					poBack.setNeedRoadTest(poBackValue.getNeedRoadTest());
					poBack.setRecommendEmpName(poBackValue
							.getRecommendEmpName());
					poBack.setRecommendCustomerName(poBackValue
							.getRecommendCustomerName());
					poBack.setServiceAdvisor(poBackValue.getServiceAdvisor());
					poBack.setServiceAdvisorAss(poBackValue
							.getServiceAdvisorAss());
					poBack.setRoStatus(poBackValue.getRoStatus());
					poBack.setRoCreateDate(poBackValue.getRoCreateDate());
					poBack.setEndTimeSupposed(poBackValue.getEndTimeSupposed());
					poBack.setChiefTechnician(poBackValue.getChiefTechnician());
					poBack.setOwnerNo(poBackValue.getOwnerNo());
					poBack.setOwnerName(poBackValue.getOwnerName());
					poBack.setOwnerProperty(poBackValue.getOwnerProperty());
					poBack.setLicense(poBackValue.getLicense());
					poBack.setVin(poBackValue.getVin());
					poBack.setEngineNo(poBackValue.getEngineNo());
					poBack.setBrand(poBackValue.getBrand());
					poBack.setSeries(poBackValue.getSeries());
					poBack.setModel(poBackValue.getModel());
					poBack.setInMileage(poBackValue.getInMileage());
					poBack.setOutMileage(poBackValue.getOutMileage());
					poBack.setIsChangeOdograph(poBackValue
							.getIsChangeOdograph());
					poBack.setChangeMileage(poBackValue.getChangeMileage());
					poBack.setTotalChangeMileage(poBackValue
							.getTotalChangeMileage());
					poBack.setTotalMileage(poBackValue.getTotalMileage());
					poBack.setDeliverer(poBackValue.getDeliverer());
					poBack.setDelivererGender(poBackValue.getDelivererGender());
					poBack.setDelivererPhone(poBackValue.getDelivererPhone());
					poBack.setDelivererMobile(poBackValue.getDelivererMobile());
					poBack.setFinishUser(poBackValue.getFinishUser());
					poBack.setCompleteTag(poBackValue.getCompleteTag());
					poBack.setWaitInfoTag(poBackValue.getWaitInfoTag());
					poBack.setWaitPartTag(poBackValue.getWaitPartTag());
					poBack.setCompleteTime(poBackValue.getCompleteTime());
					poBack.setForBalanceTime(poBackValue.getForBalanceTime());
					poBack.setDeliveryTag(poBackValue.getDeliveryTag());
					poBack.setDeliveryDate(poBackValue.getDeliveryDate());
					poBack.setDeliveryUser(poBackValue.getDeliveryUser());
					poBack.setLabourPrice(poBackValue.getLabourPrice());
					poBack.setLabourAmount(poBackValue.getLabourAmount());
					poBack.setRepairPartAmount(poBackValue
							.getRepairPartAmount());
					poBack.setSalesPartAmount(poBackValue.getSalesPartAmount());
					poBack.setAddItemAmount(poBackValue.getAddItemAmount());
					poBack.setOverItemAmount(poBackValue.getOverItemAmount());
					poBack.setRepairAmount(poBackValue.getRepairAmount());
					poBack.setEstimateAmount(poBackValue.getEstimateAmount());
					poBack.setBalanceAmount(poBackValue.getBalanceAmount());
					poBack.setReceiveAmount(poBackValue.getReceiveAmount());
					poBack.setSubobbAmount(poBackValue.getSubobbAmount());
					poBack.setDerateAmount(poBackValue.getDerateAmount());
					poBack.setFirstEstimateAmount(poBackValue
							.getFirstEstimateAmount());
					poBack.setTraceTag(poBackValue.getTraceTag());
					poBack.setRemark(poBackValue.getRemark());
					poBack.setRemark1(poBackValue.getRemark1());
					poBack.setRemark2(poBackValue.getRemark2());
					poBack.setTestDriver(poBackValue.getTestDriver());
					poBack.setPrintRoTime(poBackValue.getPrintRoTime());
					poBack.setRoChargeType(poBackValue.getRoChargeType());
					poBack.setPrintRpTime(poBackValue.getPrintRpTime());
					poBack.setEstimateBeginTime(poBackValue
							.getEstimateBeginTime());
					poBack.setIsActivity(poBackValue.getIsActivity());
					poBack.setIsCloseRo(poBackValue.getIsCloseRo());
					poBack.setIsMaintain(poBackValue.getIsMaintain());
					poBack.setRosplitStatus(poBackValue.getRosplitStatus());
					poBack.setMemberNo(poBackValue.getMemberNo());
					poBack.setInReason(poBackValue.getInReason());
					poBack.setModifyNum(poBackValue.getModifyNum());
					poBack.setQuoteEndAccurate(poBackValue
							.getQuoteEndAccurate());
					poBack.setCustomerPreCheck(poBackValue
							.getCustomerPreCheck());
					poBack.setCheckedEnd(poBackValue.getCheckedEnd());
					poBack.setExplainedBalanceAccounts(poBackValue
							.getExplainedBalanceAccounts());
					poBack.setNotEnterWorkshop(poBackValue
							.getNotEnterWorkshop());
					poBack.setIsTakePartOld(poBackValue.getIsTakePartOld());
					poBack.setLockUser(poBackValue.getLockUser());
					poBack.setDownTimestamp(poBackValue.getDownTimestamp());
					poBack.setIsvalid(poBackValue.getIsvalid());
					poBack.setCreateBy(poBackValue.getCreateBy());
					poBack.setCreateDate(poBackValue.getCreateDate());
					poBack.setUpdateBy(poBackValue.getUpdateBy());
					poBack.setUpdateDate(poBackValue.getUpdateDate());
					poBack.setDelivererAdress(poBackValue.getDelivererAdress());
					poBack.setGuaranteeDate(poBackValue.getGuaranteeDate());
					poBack.setForlStatus(poBackValue.getForlStatus());
					poBack.setCamCode(poBackValue.getCamCode());
					poBack.setAuditContent(poBackValue.getAuditContent());
					poBack.setFreeTimes(poBackValue.getFreeTimes());
					poBack.setApprovalYn(poBackValue.getApprovalYn());
					poBack.setIfStatus(poBackValue.getIfStatus());
					poBack.setDataType(poBackValue.getDataType());// ////////////////
					poBack.setAccreditAmount(poBackValue.getAccreditAmount());
					poBack.setTroubleReason(poBackValue.getTroubleReason());
					poBack.setRepairMethod(poBackValue.getRepairMethod());
					poBack.setTroubleDescriptions(poBackValue
							.getTroubleDescriptions());
					poBack.setRemarks(poBackValue.getRemarks());
					// poBack.setIsDel(poBackValue.getIsDel());////////////////////////
					poBack.setOrderValuableType(poBackValue
							.getOrderValuableType());
					poBack.setDealerId(poBackValue.getDealerId());

					// add by 2011-03-03
					poBack.setUpdateBy(getCurrDealerId());
					poBack.setUpdateDate(new Date());
					// add by 2011-03-03 end

					dao.insertRepairOrder(poBack);
					//--往删除记录表里插入数据 
					dao.insert("insert into Tt_As_Repair_Order_delete select * from  Tt_As_Repair_Order o where o.ro_no='"+poBackValue.getRoNo()+"'");
					
					// 然后根据ID删除主表信息
					TtAsRepairOrderPO po = new TtAsRepairOrderPO();
					po.setId(Long.parseLong(id));
					List<TtAsRepairOrderPO> listpo = dao.select(po);
					TtAsRepairOrderPO po1 = null;
					if (listpo != null && listpo.size() > 0) {
						po1 = listpo.get(0);
					}
					Integer roStatus = po1.getRoStatus();
					if(String.valueOf(roStatus).equals("11591002")){//如果结算则执行新三包法的
						po1.setRoStatus(Constant.RO_STATUS_03);

						String[] PayType = request.getParamValues("PayType");

					

						if (PayType != null && !"".equals(PayType)) {
							for (String PayType1 : PayType) {
								if (PayType1
										.equals("" + (int) Constant.PAY_TYPE_02)) {
									dao.vehicleJude(po1.getVin(),
											"" + po1.getInMileage(), Utility
													.handleDate(po1
															.getRoCreateDate()),
											po1, loginUser);
									break;
								}
							}
						}

						boolean fage = true;
						String[] PayTypePart = request
								.getParamValues("PayTypePart");
						if (PayTypePart != null && PayTypePart.length > 0) {
							for (String PayTypePart1 : PayTypePart) {
								if (PayTypePart1.split("-")[0].equals(""
										+ (int) Constant.PAY_TYPE_02)) {
									dao.baseJude(PayTypePart, po1, loginUser);
									fage = false;
									break;
								}
							}
						}
						String[] BourPayType = request
								.getParamValues("BourPayType");
						if (fage) {
							if (BourPayType != null && BourPayType.length > 0) {
								for (String BourPayType1 : BourPayType) {
									if (BourPayType1.split("-")[0].equals(""
											+ (int) Constant.PAY_TYPE_02)) {
										dao.bourjude(BourPayType, po1, loginUser);
										break;
									}
								}
							}
						}
					}
					
					// 然后删除此工单生成过的预授权单子
					// add by zuoxj 存在预授权单子才执行删除操作
					if (dao.select(po).size() > 0) {
						TtAsRepairOrderPO poValue = (TtAsRepairOrderPO) dao
								.select(po).get(0);
						TtAsWrForeapprovalPO conPo = new TtAsWrForeapprovalPO();
						conPo.setRoNo(poValue.getRoNo());
						dao.delete(conPo);
						dao.delete(po);
					}
				}
				String dealerId = loginUser.getDealerId();
				if("".equals(dealerId)||dealerId==null ||dealerId=="null"){
					dealerId="";
				}
				act.setOutData("dealerId", dealerId);
				if (flag) {
					act.setOutData("msg", "true");
				} else {
					act.setOutData("msg", "false");
				}
			}
			// roDeleteForward();//跳转至首页
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "工单维护");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}

	/******** add by liuxh 20101117 增加车厂工单查询功能 *********/
	/**
	 * 
	 * @Title: queryRepairOrder
	 * @Description: TODO(查询工单)
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws 工单信息查询
	 */
	public void repairOrderQuery() {

		String dealerId = loginUser.getDealerId();
		String dealerCode = loginUser.getDealerCode();
		CommonUtilActions commonUtilActions = new CommonUtilActions();
		try {
			String action = CommonUtils.checkNull(act.getRequest()
					.getParamValue("action"));
			if ("query".equals(action)) {

				Integer curPage = getCurrPage();
				String roNo = request.getParamValue("RO_NO");
				String activityCode = request.getParamValue("ACTIVITY_CODE");
				String activityMain = request.getParamValue("ACTIVITY_MAIN");
				String vin = request.getParamValue("VIN");
				String model = request.getParamValue("model");
				String repairType = request.getParamValue("REPAIR_TYPE");
				String roCreateDate = request.getParamValue("RO_CREATE_DATE");
				String deliveryDate = request.getParamValue("DELIVERY_DATE");
				String isForl = request.getParamValue("RO_FORE");
				String roStatus = request.getParamValue("RO_STATUS");
				String orderStatus = request.getParamValue("ORDER_STATUS");
				String createDateStr = request.getParamValue("CREATE_DATE_STR");
				String createDateEnd = request.getParamValue("CREATE_DATE_END");
				String dealerIdCon = CommonUtils.checkNull(request
						.getParamValue("dealerId"));
				String Extype = request.getParamValue("Extype");
				// -------------------------wenyudan 2013/11/14--------------
				String areaCode = request.getParamValue("area_code");
				String groupCode = CommonUtils.checkNull(request
						.getParamValue("groupCode"));

				Map<String, String> map = new HashMap();
				map.put("roNo", roNo);
				map.put("activityMain", activityMain);
				map.put("activityCode", activityCode);
				map.put("vin", vin);
				map.put("model", model);
				map.put("repairType", repairType);
				map.put("roCreateDate", roCreateDate);
				map.put("deliveryDate", deliveryDate);
				map.put("isForl", isForl);
				map.put("orderStatus", orderStatus);
				map.put("roStatus", roStatus);
				map.put("dealerId", dealerId);
				map.put("createDateStr", createDateStr);
				map.put("createDateEnd", createDateEnd);
				map.put("dealerIdCon", dealerIdCon);
				map.put("poseBusType", loginUser.getPoseBusType().toString());
				map.put("areaCode", areaCode);
				map.put("groupCode", groupCode);
				PageResult<TtAsRepairOrderExtPO> ps = dao
						.queryRepairOrderNewQuery(map, Constant.PAGE_SIZE,
								curPage);
				act.setOutData("ps", ps);
			} else {
				//车型
				act.setOutData("startDate", DateUtil.getFirstMonthDay(1));
				act.setOutData("endDate", DateUtil.getYesToDay());
				act.setOutData("modelList", commonUtilActions.getAllModel());
				act.setForword("/jsp/repairorder/roMaintainQuery.jsp");
			}
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "工单查询");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}

	public void export() {

		try {

			String roNo = request.getParamValue("RO_NO");
			String vin = request.getParamValue("VIN");
			String repairType = request.getParamValue("REPAIR_TYPE");
			String roCreateDate = request.getParamValue("RO_CREATE_DATE");
			String deliveryDate = request.getParamValue("DELIVERY_DATE");
			String isForl = request.getParamValue("RO_FORE");
			String roStatus = request.getParamValue("RO_STATUS");
			String orderStatus = request.getParamValue("ORDER_STATUS");
			String createDateStr = request.getParamValue("CREATE_DATE_STR");
			String createDateEnd = request.getParamValue("CREATE_DATE_END");
			String dealerIdCon = CommonUtils.checkNull(request
					.getParamValue("dealerId"));
			String activityCode = request.getParamValue("ACTIVITY_CODE");
			String activityMain = request.getParamValue("ACTIVITY_MAIN");
			String Extype = request.getParamValue("Extype");
			String areaCode = request.getParamValue("area_code");
			String groupCode = CommonUtils.checkNull(request
					.getParamValue("groupCode"));
			Map<String, String> map = new HashMap();
			map.put("areaCode", areaCode);
			map.put("groupCode", groupCode);
			map.put("roNo", roNo);
			map.put("vin", vin);
			map.put("orderStatus", orderStatus);
			map.put("repairType", repairType);
			map.put("roCreateDate", roCreateDate);
			map.put("activityMain", activityMain);
			map.put("deliveryDate", deliveryDate);
			map.put("isForl", isForl);
			map.put("roStatus", roStatus);
			map.put("createDateStr", createDateStr);
			map.put("createDateEnd", createDateEnd);
			map.put("dealerIdCon", dealerIdCon);
			map.put("activityCode", activityCode);
			ClaimReportDao rDao = ClaimReportDao.getInstance();
			String name = "维修工单.xls";
			String[] head = new String[26];
			head[0] = "大区";
			head[1] = "经销商代码";
			head[2] = "经销商简称";
			head[3] = "工单号";
			head[4] = "维修类型";
			head[5] = "结算基地";
			head[6] = "车牌号";
			head[7] = "VIN";
			head[8] = "车系";
			head[9] = "车型";
			head[10] = "颜色";
			head[11] = "车主姓名";
			head[12] = "车主电话";
			head[13] = "送修人";
			head[14] = "送修人电话";
			head[15] = "送修人手机";
			head[16] = "开工单时间";
			head[17] = "进厂里程数";
			head[18] = "单据保养次数";
			head[19] = "工单状态";
			head[20] = "申请备注";
			head[21] = "活动编号";
			head[22] = "活动主题编号";
			head[23] = "维修材料费";
			head[24] = "维修工时费";
			head[25] = "活动/外出总费用";
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			List<Map<String, Object>> list2 = dao
					.queryRepairOrderNewQueryList(map);
			String setList = "0,1,2,3,4,7,8,9,10,11,12,13,14,15,17,18,19,20,21,22";
			List list1 = new ArrayList();
			if (list2 != null && list2.size() >= 0) {
				for (int i = 0; i < list2.size(); i++) {
					String[] detail = new String[26];
					detail[0] = (String) list2.get(i).get("ROOT_ORG_NAME");
					detail[1] = (String) list2.get(i).get("DEALER_CODE");
					detail[2] = (String) list2.get(i).get("DEALER_NAME");
					detail[3] = (String) list2.get(i).get("RO_NO");
					detail[4] = (String) list2.get(i).get("CODE_DESC1");// 维修类型
					detail[5] = (String) list2.get(i).get("CODE_DESC2");// 结算基地
					detail[6] = (String) list2.get(i).get("LICENSE_NO");
					detail[7] = (String) list2.get(i).get("VIN");
					detail[8] = (String) list2.get(i).get("GROUP_NAME");
					detail[9] = (String) list2.get(i).get("MODEL");
					detail[10] = (String) list2.get(i).get("COLOR_NAME");
					detail[11] = (String) list2.get(i).get("OWNER_NAME");
					detail[12] = (String) list2.get(i).get("OWNER_PHONE");

					detail[13] = (String) list2.get(i).get("DELIVERER");
					detail[14] = (String) list2.get(i).get("DELIVERER_PHONE");
					detail[15] = (String) list2.get(i).get("DELIVERER_MOBILE");

					detail[16] = (String) list2.get(i).get("START_DATE");
					detail[17] = list2.get(i).get("IN_MILEAGE").toString();
					if (Constant.REPAIR_TYPE_04.equalsIgnoreCase((String) list2
							.get(i).get("REPAIR_TYPE_CODE"))) {
						detail[18] = (String) list2.get(i).get("FREE_TIMES")
								.toString();
					} else {
						detail[18] = "";
					}
					detail[19] = (String) list2.get(i).get("CODE_DESC3");
					detail[20] = (String) list2.get(i).get("REMARKS");
					detail[21] = (String) list2.get(i).get("CAM_CODE");
					detail[22] = (String) list2.get(i).get("SUBJECT_NO");
					detail[23] = BaseUtils.checkNull( list2.get(i).get("REPAIR_PART_AMOUNT"));
					detail[24] = BaseUtils.checkNull(list2.get(i).get("LABOUR_AMOUNT"));
					detail[25] = BaseUtils.checkNull(list2.get(i).get("ADD_ITEM_AMOUNT"));

					list1.add(detail);
				}
				// dao.toExceVender(ActionContext.getContext().getResponse(),
				// req, head, list1,name);
			}
			rDao.toExceVender(ActionContext.getContext().getResponse(),
					request, head, list1, name, "维修工单", setList);
			act.setForword(MAIN_URL_TO);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "工单查询");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}

	/******** add by liuxh 20101117 增加车厂工单查询功能 *********/

	/******** add by liuxh 20101119 车辆车主信息查询功能 *********/
	/**
	 * 
	 * @Title: queryRepairOrder
	 * @Description: TODO(查询工单)
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws 工单信息查询
	 */
	public void vehicleCusInfoQuery() {

		String dealerId = loginUser.getDealerId();
		String dealerCode = loginUser.getDealerCode();
		try {
			String action = CommonUtils.checkNull(act.getRequest()
					.getParamValue("action"));
			if ("query".equals(action)) {

				Integer curPage = getCurrPage();
				String vin = CommonUtils
						.checkNull(request.getParamValue("VIN"));
				String carStatus = CommonUtils.checkNull(request
						.getParamValue("CAR_STATUS"));
				String yieldly = CommonUtils.checkNull(request
						.getParamValue("yieldly"));
				String carNo = CommonUtils.checkNull(request
						.getParamValue("CAR_NO"));
				String cusName = CommonUtils.checkNull(request
						.getParamValue("CUS_NAME"));
				String isPDI = CommonUtils.checkNull(request
						.getParamValue("isPDI"));

				Map<String, String> map = new HashMap();
				map.put("vin", vin);
				map.put("carStatus", carStatus);
				map.put("yieldly", yieldly);
				map.put("carNo", carNo);
				map.put("cusName", cusName);
				map.put("isPDI", isPDI);

				PageResult<Map> ps = dao.vehicleCusInfoQuery(map,
						Constant.PAGE_SIZE, curPage);
				act.setOutData("ps", ps);
			} else {

				TmBusinessAreaPO areaPO = new TmBusinessAreaPO();
				List<TmBusinessAreaPO> lista = dao.select(areaPO);
				act.setOutData("areaPO", lista);
				act.setForword("/jsp/repairorder/vehicleCusInfoQuery.jsp");
			}
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "工单查询");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}

	/******** add by liuxh 20101119 车辆车主信息查询功能 *********/
	/**
	 * 
	 * @Title: queryRepairOrder
	 * @Description: TODO(查询工单)
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws 工单信息查询
	 */
	public void vehicleCusInfoPDIQuery() {

		String dealerId = loginUser.getDealerId();
		String dealerCode = loginUser.getDealerCode();
		try {
			String action = CommonUtils.checkNull(act.getRequest()
					.getParamValue("action"));
			if ("query".equals(action)) {

				Integer curPage = getCurrPage();
				String vin = CommonUtils
						.checkNull(request.getParamValue("VIN"));
				String carStatus = CommonUtils.checkNull(request
						.getParamValue("CAR_STATUS"));
				String yieldly = CommonUtils.checkNull(request
						.getParamValue("yieldly"));
				String carNo = CommonUtils.checkNull(request
						.getParamValue("CAR_NO"));
				String cusName = CommonUtils.checkNull(request
						.getParamValue("CUS_NAME"));

				Map<String, String> map = new HashMap();
				map.put("vin", vin);
				map.put("carStatus", carStatus);
				map.put("yieldly", yieldly);
				map.put("carNo", carNo);
				map.put("cusName", cusName);

				PageResult<Map> ps = dao.vehicleCusInfoQuery(map,
						Constant.PAGE_SIZE, curPage);
				act.setOutData("ps", ps);
			} else {

				TmBusinessAreaPO areaPO = new TmBusinessAreaPO();
				List<TmBusinessAreaPO> lista = dao.select(areaPO);
				act.setOutData("areaPO", lista);
				act.setForword("/jsp/repairorder/vehicleCusInfoPDIQuery.jsp");
			}
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "工单查询");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}

	public void vehicleCusInfoDetail() {

		String vehicleId = CommonUtils.checkNull(act.getRequest()
				.getParamValue("vehicleId"));
		String vin = CommonUtils.checkNull(act.getRequest()
				.getParamValue("VIN"));
		Map map = (Map) dao.getVehcleCusDetial(Long.valueOf(vehicleId)).get(0);
		act.setOutData("detailMap", map);
		List<Map<String, Object>> freeMaintaimHisList = dao
				.freeMaintainHistory(vin);
		act.setOutData("freeMaintaimHisList", freeMaintaimHisList);
		act.setForword("/jsp/repairorder/vehicleCustomerDetail.jsp");
	}

	/******** add by liuxh 20101119 车辆车主信息查询功能 *********/

	/**
	 * Iverson add By 2010-12-08
	 * 
	 * @Title: queryRepairOrder
	 * @Description: TODO(车辆信息历史查询)
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	public void vehicleHistoryInfoQuery() {

		try {
			act.setForword("/jsp/repairorder/vehicleHistoryInfoQuery.jsp");
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "VIN码查车辆信息");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * yx add By 2010-12-17
	 * 
	 * @Title: vehiclePinReplyQuery
	 * @Description: TODO(PIN查询回复)
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	public void vehiclePinReplyQuery() {
		try {
			String COMMAND = CommonUtils.checkNull(request
					.getParamValue("COMMAND"));
			if (!"".equals(COMMAND)) {
				String companyName = CommonUtils.checkNull(request
						.getParamValue("companyName"));
				String vin = CommonUtils
						.checkNull(request.getParamValue("vin"));
				String pinCreateDate = CommonUtils.checkNull(request
						.getParamValue("PIN_CREATE_DATE"));
				String pinEndDate = CommonUtils.checkNull(request
						.getParamValue("PIN_END_DATE"));
				String status = CommonUtils.checkNull(request
						.getParamValue("status"));
				String pinCode = CommonUtils.checkNull(request
						.getParamValue("pinCode"));
				String dealerCode = CommonUtils.checkNull(request
						.getParamValue("dealerCode"));
				Integer curPage = getCurrPage();
				PageResult<Map<String, Object>> ps = dao
						.getVehclePinSubmitRequest(companyName, vin,
								pinCreateDate, pinEndDate, status, pinCode,
								dealerCode, curPage);
				act.setOutData("ps", ps);
			} else {
				act.setForword(VEHICLE_PIN_REPLY_QUERY);
			}
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "VIN码查车辆信息");
			logger.error(super.loginUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * yx add By 2010-12-17
	 * 
	 * @Title: vehiclePinReplyQueryDetail
	 * @Description: TODO(PIN回复查询明细)
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	public void vehiclePinReplyQueryDetail() {
		try {
			String COMMAND = CommonUtils.checkNull(request
					.getParamValue("COMMAND"));
			String pinId = CommonUtils
					.checkNull(request.getParamValue("pinId"));
			if (!"".equals(COMMAND)) {
				String pinNo = CommonUtils.checkNull(request
						.getParamValue("pinNo"));
				String replyRemark = CommonUtils.checkNull(request
						.getParamValue("replyRemark"));
				TmVehiclePinRequestPO pinReq = new TmVehiclePinRequestPO();
				pinReq.setPinId(Long.valueOf(pinId));
				TmVehiclePinRequestPO pinReqValue = new TmVehiclePinRequestPO();
				pinReqValue.setPin(pinNo);
				// 修改
				pinReqValue.setBackRemark(replyRemark);
				// 回复
				pinReqValue.setStatus(Constant.VEHICLE_PIN_02);

				pinReqValue.setUpdateBy(getCurrDealerId());
				pinReqValue.setUpdateDate(new Date());
				int msg = dao.update(pinReq, pinReqValue);
				act.setOutData("msg", msg);
			} else {

				Map<String, Object> ps = dao.getVehclePinDetail(pinId);
				act.setOutData("ps", ps);
				act.setForword(VEHICLE_PIN_REPLY_DETAIL);
			}
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "VIN码查车辆信息");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}

	/******** Iverson add by 2010-12-08 判断vin码在库中是否存在 *********/
	public void vehicleHistoryInfoQuery1() {

		try {
			String vin = request.getParamValue("vin");
			int count = dao.getCount(vin);
			if (count > 0) {
				act.setOutData("ok", "ok");
			}
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "VIN码查车辆信息");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}

	/******** Iverson add by 2010-12-08 判断vin码在库中是否存在 *********/

	/******** yx add by 2010-12-18 判断结算时配件是否属配件大类 *********/
	// 弹出窗方式
	public void checkRepairOrderPart() {
		try {
			// 判断登陆系统
			List<Map<String, Object>> ListCode = dao.queryTcCode();
			Integer code = Integer.valueOf(ListCode.get(0).get("CODE_ID")
					.toString());
			if (code == Constant.chana_wc) {

				// 检查工单配件是不是配件大类
				String roId = CommonUtils.checkNull(request
						.getParamValue("roId"));
				List<Map<String, String>> partCode = dao
						.getRepairOrderPartCheck(roId);
				String partStr = "";
				for (int j = 0; j < partCode.size(); j++) {
					if (partCode.get(j).get("PART_TYPE_ID") == null) {
						if ("".equals(partStr)) {
							partStr = partCode.get(j).get("PART_NO").toString();
						} else {
							partStr = partStr + ","
									+ partCode.get(j).get("PART_NO").toString();
						}
					}
				}
				act.setOutData("partStr", partStr);

			}
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "验证配件大类失败！");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}

	/******** yx add by 2010-12-18 判断结算时配件是否属配件大类 *********/

	// 删除问题工单 YH 2011.7.4
	private void DelProblemOrder(String roNo) throws Exception {
		TtAsRepairOrderProblemPO ttAsRepairOrderProblemPO = new TtAsRepairOrderProblemPO();
		ttAsRepairOrderProblemPO.setRoNo(roNo);
		dao.delete(ttAsRepairOrderProblemPO);
	}

	/**
	 * 关注件设置跳转
	 */

	public void foucsLabourSet() {

		try {

			act.setForword(MAIN_PARTCODE_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "预授权审核");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}

	public void foucsPositionSet() {

		try {

			act.setForword(MAIN_POSITION_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "预授权审核");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}

	public void foucsKdSet() {

		try {

			act.setForword(MAIN_KD_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "Kd维护");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * 配件查询
	 * 
	 */
	public void partQuery() {

		try {

			String code = request.getParamValue("PART_CODE");
			String name = request.getParamValue("PART_NAME");
			String PART_WAR_TYPE = request.getParamValue("PART_WAR_TYPE");
			Integer curPage = getCurrPage();
			PageResult<TmPtPartBasePO> ps = dao.getAllPart(code, name,
					PART_WAR_TYPE, Constant.PAGE_SIZE, curPage);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "预授权审核");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}

	public void positionQuery() {

		try {

			String code = request.getParamValue("POS_CODE");
			String name = request.getParamValue("POS_NAME");
			Integer curPage = getCurrPage();
			PageResult<TtAsWrMalfunctionPositionPO> ps = dao.getAllPosition(
					code, name, Constant.PAGE_SIZE, curPage);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "部位管理");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}

	public void judepositiontokd() {

		try {

			String[] id_vers = request.getParamValues("checkId");
			for (String id_ver : id_vers) {
				TtAsWrMalfunctionPositionPO asWrMalfunctionPositionPO = new TtAsWrMalfunctionPositionPO();
				String[] idver = id_ver.split("-");
				asWrMalfunctionPositionPO.setPosId(Long.parseLong(idver[0]));
				asWrMalfunctionPositionPO.setVer(Integer.parseInt(idver[1]));
				List<TtAsWrMalfunctionPositionPO> list = dao
						.select(asWrMalfunctionPositionPO);
				if (list.size() == 0) {
					act.setOutData("success", "yes");
					break;
				}
			}

		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "部位管理");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}

	public void judebasetoposition() {

		try {

			String[] id_vers = request.getParamValues("checkId");
			if (id_vers != null && id_vers.length > 0) {
				for (String id_ver : id_vers) {
					TmPtPartBasePO asWrMalfunctionPositionPO = new TmPtPartBasePO();
					String[] idver = id_ver.split("-");
					asWrMalfunctionPositionPO.setPartId(Long
							.parseLong(idver[0]));
					asWrMalfunctionPositionPO
							.setVer(Integer.parseInt(idver[1]));
					List<TmPtPartBasePO> list = dao
							.select(asWrMalfunctionPositionPO);
					if (list.size() == 0) {
						act.setOutData("success", "yes");
						break;
					}
				}
			}

		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "部位管理");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}

	public void kdQuery() {
		try {
			String code = request.getParamValue("KD_CODE");
			String name = request.getParamValue("KD_NAME");
			Integer curPage = getCurrPage();
			PageResult<TtAsWrKeyDesignPO> ps = dao.getAllKD(code, name,
					Constant.PAGE_SIZE, curPage);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "部位管理");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}

	public void baseIsNew() {
		try {
			act.setForword(Base_IS_NEW);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "新件首页出错");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}

	public void positionModForword() {
		try {

			String id = request.getParamValue("ID");
			act.setOutData("pId", id);
			act.setForword(MAIN_Base_POSITION);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "部位管理");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}

	public void addPositioninit() {
		try {
			act.setForword(ADD_POSITION);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "部位新增初始化");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}

	public void kdModForword() {
		try {
			String kdid = request.getParamValue("ID");
			act.setOutData("kdid", kdid);
			act.setForword(MAIN_POSITION_KD);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "部位管理");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}

	public void BaseToPosition() {
		try {
			String id = request.getParamValue("pid");
			String PART_CODE = request.getParamValue("PART_CODE");
			String PART_NAME = request.getParamValue("PART_NAME");
			Integer curPage = getCurrPage();
			PageResult<Map<String, Object>> ps = dao.getBase(50, PART_CODE,
					PART_NAME, curPage, id);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "部位管理");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}

	public void BaseToPositionIsNew() {
		try {
			String PART_CODE = request.getParamValue("PART_CODE");
			String PART_NAME = request.getParamValue("PART_NAME");
			String create_start = request.getParamValue("create_start");
			String create_end = request.getParamValue("create_end");
			Integer curPage = getCurrPage();
			PageResult<Map<String, Object>> ps = dao.getBase(20, PART_CODE,
					PART_NAME, create_start, create_end, curPage);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "部位管理");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}

	public void PositionTOKd() {
		try {
			String kdid = request.getParamValue("kdid");
			Integer curPage = getCurrPage();
			PageResult<Map<String, Object>> ps = dao.getPosition(50, curPage,
					kdid);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "KD管理");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}

	public void intercalate() {
		try {
			String dealerCode = request.getParamValue("dealerCode");
			String dealerName = request.getParamValue("dealerName");
			String COMM = request.getParamValue("COMM");
			if (COMM != null) {
				Integer curPage = getCurrPage();
				PageResult<Map<String, Object>> ps = dao.intercalate(10,
						curPage, dealerCode, dealerName);
				act.setOutData("ps", ps);
			} else {
				act.setForword(this.DEALER_INVOICE_INTERCALATE);
			}

		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "KD管理");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}

	public void addIntercalate() {

		try {

			String[] checkId = request.getParamValues("checkId");
			String[] allId = request.getParamValues("ALLID");
			StringBuffer sql = new StringBuffer();
			if (checkId != null && checkId.length > 0) {
				String dealerId = "(";
				for (int i = 0; i < checkId.length; i++) {
					if (i == checkId.length - 1) {
						dealerId = dealerId + checkId[i] + ")";
					} else {
						dealerId = dealerId + checkId[i] + ",";
					}
				}
				sql.append("DELETE from tm_dealer_invoice_intercalate t where t.DEALERID in "
						+ dealerId);
				dao.delete(sql.toString(), null);
			}

			if (allId != null && allId.length > 0) {

				for (String all : allId) {
					boolean fal = true;
					if (checkId != null && checkId.length > 0) {
						for (String check : checkId) {
							if (all.equals(check)) {
								fal = false;
								break;
							}
						}
					}

					if (fal) {
						TmDealerInvoiceIntercalatePO intercalatePO = new TmDealerInvoiceIntercalatePO();
						intercalatePO.setDealerid(Long.parseLong(all));
						TmDealerPO tm = new TmDealerPO();
						tm.setDealerId(Long.parseLong(all));
						List<TmDealerPO> list = dao.select(tm);
						intercalatePO.setUpdateDate(new Date());
						intercalatePO
								.setDealerCode(list.get(0).getDealerCode());
						intercalatePO.setUpdateBy(getCurrDealerId());
						dao.insert(intercalatePO);
					}
				}

			}

		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "KD管理");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}

	public void updatebaseisnew() {
		try {
			String[] baes = request.getParamValues("checkId");
			if (baes != null && baes.length > 0) {
				for (String bae : baes) {
					System.out.println(bae);

					TmPtPartBasePO tm = new TmPtPartBasePO();
					tm.setPartId(Long.parseLong(bae));
					TmPtPartBasePO tm1 = new TmPtPartBasePO();
					tm1.setIsPartNew(1);
					dao.update(tm, tm1);
				}
			}

		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "配件插入失败");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}

	public void insertbasetoposition() {
		try {
			String Pid = request.getParamValue("pid");
			String[] baes = request.getParamValues("checkId");
			String[] ALL = request.getParamValues("ALL");
			dao.basetoposition(baes, ALL, Pid);

		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "配件插入失败");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}

	public void insertpositiontokd() {
		try {
			String kdid = request.getParamValue("kdid");
			String[] baes = request.getParamValues("checkId");
			dao.positiontokd(baes, kdid);

		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "配件插入失败");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * 配件删除
	 * 
	 */
	public void partDel() {
		try {

			String id = request.getParamValue("ID");// 配件记录ID
			TmPtPartBasePO bp = new TmPtPartBasePO();
			TmPtPartBasePO bp2 = new TmPtPartBasePO();
			bp.setPartId(Utility.getLong(id));

			bp2.setIsDel(Constant.IS_DEL);// 逻辑删除表识：1
			bp2.setUpdateBy(getCurrDealerId());
			bp2.setUpdateDate(new Date());
			dao.update(bp, bp2);
			act.setOutData("success", "true");
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.DELETE_FAILURE_CODE, "关注配件维护");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}

	// 配件修改跳转
	public void partModForword() {
		try {

			String id = request.getParamValue("ID");// 配件记录ID
			TmPtPartBasePO bp = new TmPtPartBasePO();
			bp.setPartId(Utility.getLong(id));
			List<TmPtPartBasePO> list = dao.select(bp);
			TmPtPartBasePO hm = (TmPtPartBasePO) list.get(0);
			act.setOutData("SELMAP", hm);
			act.setForword(PART_WAR_TYEP_MODIFY);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.DELETE_FAILURE_CODE, "关注配件维护");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * 关注件更新
	 */
	public void foucsPartUpdate() {
		try {

			String id = request.getParamValue("ID");// 配件记录ID
			String partCode = request.getParamValue("partCode");// 配件代码
			String partWarType = request.getParamValue("partWarType");// 修改前
																		// 配件三包类型
			String type = request.getParamValue("PART_WAR_TYPE");
			String type1 = request.getParamValue("typename");
			TmPtPartBasePO bp2 = new TmPtPartBasePO();
			if (type1.equals("1")) {
				String wr_months1 = request.getParamValue("wr_months1");
				String wr_mileage1 = request.getParamValue("wr_mileage1");
				bp2.setWrMonths(Integer.parseInt(wr_months1));
				bp2.setWrMileage(Double.parseDouble(wr_mileage1));
			} else if (type1.equals("2")) {
				String wr_months2 = request.getParamValue("wr_months2");
				String wr_mileage2 = request.getParamValue("wr_mileage2");
				bp2.setWrMonths(Integer.parseInt(wr_months2));
				bp2.setWrMileage(Double.parseDouble(wr_mileage2));
			}
			TmPtPartBasePO bp = new TmPtPartBasePO();
			bp.setPartId(Utility.getLong(id));

			bp2.setPartWarType(Integer.parseInt(type));
			dao.update(bp, bp2);
			// add 2013-4-26 配件三包类型做了修改
			if (!type.equals(partWarType)) {
				if (type.equals(Constant.PART_WR_TYPE_1)) {// 易损件 --〉常规件
					// 三包规则表中删除此件数据
					dao.delNormalPartFromRule(partCode,
							Integer.parseInt(Constant.PART_WR_TYPE_2));
				} else if (type.equals(Constant.PART_WR_TYPE_2)) {// 常规件 --〉 易损件
					// 三包规则表中增加此件相关信息
					List<TtAsWrVinRulePO> tVinRuleList = dao.queryPartInRule(
							partCode, Integer.parseInt(type));
					if (tVinRuleList == null || tVinRuleList.size() == 0) {
						TtAsWrVinRulePO rpo = new TtAsWrVinRulePO();
						for (int i = 0; i < 3; i++) {
							rpo.setVrId(new Long(SequenceManager
									.getSequence("")));
							rpo.setVrCode("SBYJ" + rpo.getVrId());
							rpo.setVrType(Integer.parseInt(Constant.VR_TYPE_2));
							rpo.setPartWrType(Integer.parseInt(type));
							rpo.setVrPartCode(partCode);
							rpo.setVrLawStandard("汽车三包法规二十一条");
							if (i == 0) {
								rpo.setVrLevel(Integer
										.parseInt(Constant.VR_LEVEL_1));
								rpo.setVrWarranty(4);
							} else if (i == 1) {
								rpo.setVrLevel(Integer
										.parseInt(Constant.VR_LEVEL_2));
								rpo.setVrWarranty(3);
							} else if (i == 2) {
								rpo.setVrLevel(Integer
										.parseInt(Constant.VR_LEVEL_3));
								rpo.setVrWarranty(2);
							}
							rpo.setVrLaw(5);
							rpo.setCreateBy(getCurrDealerId());
							rpo.setCreateDate(new Date());
							dao.insert(rpo);
						}
					}
				}
			}

			act.setOutData("success", "true");
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.DELETE_FAILURE_CODE, "关注配件维护");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * 预授权申请跳转
	 */
	public void foreApply() {
		try {
			act.setForword(FORE_APPLY_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "索赔预授权");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * 预授权申请查询
	 */
	public void foreApplyQuery() {
		String dealerId = loginUser.getDealerId();
		String dealerCode = loginUser.getDealerCode();
		try {

			Integer curPage = getCurrPage();
			String roNo = request.getParamValue("RO_NO");
			String vin = request.getParamValue("VIN");
			String repairType = request.getParamValue("REPAIR_TYPE");
			String roCreateDate = request.getParamValue("RO_CREATE_DATE");
			String deliveryDate = request.getParamValue("DELIVERY_DATE");
			String isForl = request.getParamValue("RO_FORE");
			String roStatus = request.getParamValue("RO_STATUS");
			String createDateStr = request.getParamValue("CREATE_DATE_STR");
			String createDateEnd = request.getParamValue("CREATE_DATE_END");
			String isWarning = request.getParamValue("IS_WARNING");
			Map<String, String> map = new HashMap();
			map.put("roNo", roNo);
			map.put("vin", vin);
			map.put("repairType", repairType);
			map.put("roCreateDate", roCreateDate);
			map.put("deliveryDate", deliveryDate);
			map.put("isForl", isForl);
			map.put("roStatus", roStatus);
			map.put("dealerId", dealerId);
			map.put("createDateStr", createDateStr);
			map.put("createDateEnd", createDateEnd);
			map.put("isWarning", isWarning);
			PageResult<TtAsRepairOrderExtPO> ps = dao.getForeApply(map,
					Constant.PAGE_SIZE, curPage);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "预授权申请");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}

	// 故障代码维护查询跳转
	public void malFunction() {
		try {
			act.setForword(MAL_MAIN_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "故障代码维护");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}

	public void malQuery() {
		try {
			String code = request.getParamValue("MAL_CODE");
			String name = request.getParamValue("MAL_NAME");
			Integer curPage = getCurrPage();
			PageResult<TtAsWrMalfunctionBean> ps = dao.getAllMal(code, name,
					Constant.PAGE_SIZE, curPage);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "故障代码维护");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}

	public void malDel() {
		try {

			String id = request.getParamValue("ID");
			TtAsWrMalfunctionPO p = new TtAsWrMalfunctionPO();
			p.setMalId(Utility.getLong(id));
			dao.delete(p);
			act.setOutData("success", "true");
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "故障代码维护");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}

	// 故障代码维护跳转
	public void malForward() {
		try {
			String id = request.getParamValue("ID");
			String flag = request.getParamValue("flag");
			String qudSonCode = null;
			if (Integer.parseInt(flag) == 1) {
				TtAsWrMalfunctionPO p = new TtAsWrMalfunctionPO();
				p.setMalId(Utility.getLong(id));
				p = (TtAsWrMalfunctionPO) dao.select(p).get(0);

				request.setAttribute("ID", p.getMalId());
				request.setAttribute("code", p.getMalCode());
				request.setAttribute("name", p.getMalName());
			} else {

			}
			request.setAttribute("flag", flag);
			request.setAttribute("qudSonCodes", qudSonCode);
			act.setForword(MAL_ADD_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "故障代码维护");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}

	// 故障代码维护新增/修改保存
	public void malFunctionUpdate() {
		try {

			String id = request.getParamValue("ID");
			String flag = request.getParamValue("flag");
			String code = request.getParamValue("MAL_CODE");
			String name = request.getParamValue("MAL_NAME");
			TtAsWrMalfunctionPO p2 = new TtAsWrMalfunctionPO();
			TtAsWrMalfunctionPO p = new TtAsWrMalfunctionPO();
			p2.setMalCode(code);
			p2.setMalName(name);
			if (Integer.parseInt(flag) == 1) {
				p.setMalId(Utility.getLong(id));
				int ver = ((TtAsWrMalfunctionPO) dao.select(p).get(0)).getVer();
				p2.setUpdateBy(getCurrDealerId());
				p2.setOemCompanyId(loginUser.getCompanyId());
				p2.setUpdateDate(new Date());
				p2.setVer(ver + 1);
				dao.update(p, p2);
				act.setOutData("success", "true");
			} else {
				p2.setMalId(Utility.getLong(SequenceManager.getSequence("")));
				p2.setCreateBy(getCurrDealerId());
				p2.setCreateDate(new Date());
				p2.setVer(0);
				p2.setOemCompanyId(loginUser.getCompanyId());
				dao.insert(p2);
			}
			act.setOutData("success", "true");
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "故障代码维护");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}

	// 检查故障代码是否已经存在
	public void malFunctionCheck() {

		try {

			String code = request.getParamValue("code");
			TtAsWrMalfunctionPO p = new TtAsWrMalfunctionPO();
			p.setMalCode(code);
			List list = dao.select(p);
			if (list.size() > 0 && list != null) {
				act.setOutData("success", "true");
			} else {
				act.setOutData("success", "false");
			}
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "质损区域维护");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}

	// 质损区域维护查询跳转
	public void qualityDamage() {

		try {
			act.setForword(QUD_MAIN_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "质损区域维护");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}

	public void qudQuery() {

		try {

			String code = request.getParamValue("QUD_CODE");
			String name = request.getParamValue("QUD_NAME");
			String sonCode = request.getParamValue("QUD_SON_CODE");
			String sonName = request.getParamValue("QUD_SON_NAME");
			System.out.println(sonCode + "...." + sonName);
			Integer curPage = getCurrPage();
			PageResult<TtAsWrQualityDamagePO> ps = dao.getAllQud(code, name,
					sonCode, sonName, Constant.PAGE_SIZE, curPage);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "质损区域维护");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}

	public void qudDel() {

		try {

			String id = request.getParamValue("ID");
			TtAsWrQualityDamagePO p = new TtAsWrQualityDamagePO();
			p.setQudId(Utility.getLong(id));
			dao.delete(p);
			act.setOutData("success", "true");
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "质损区域维护");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}

	// 质损区域维护跳转
	public void qudForward() {

		try {

			String id = request.getParamValue("ID");
			String flag = request.getParamValue("flag");
			if (Integer.parseInt(flag) == 1) {
				TtAsWrQualityDamagePO p = new TtAsWrQualityDamagePO();
				p.setQudId(Utility.getLong(id));
				p = (TtAsWrQualityDamagePO) dao.select(p).get(0);
				request.setAttribute("ID", p.getQudId());
				request.setAttribute("code", p.getQudCode());
				request.setAttribute("name", p.getQudName());
				request.setAttribute("sonCode", p.getQudSonCode());
				request.setAttribute("sonName", p.getQudSonName());
			}
			request.setAttribute("flag", flag);
			act.setForword(QUD_ADD_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "质损区域维护");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}

	// 质损区域维护新增/修改保存
	public void qudUpdate() {

		try {

			String id = request.getParamValue("ID");
			String flag = request.getParamValue("flag");
			String code = request.getParamValue("QUD_CODE");
			String name = request.getParamValue("QUD_NAME");
			String sonCode = request.getParamValue("QUD_SON_CODE");
			String sonName = request.getParamValue("QUD_SON_NAME");
			TtAsWrQualityDamagePO p2 = new TtAsWrQualityDamagePO();
			TtAsWrQualityDamagePO p = new TtAsWrQualityDamagePO();
			p2.setQudCode(code);
			p2.setQudName(name);
			p2.setQudSonCode(sonCode);
			p2.setQudSonName(sonName);
			p2.setQudAllName(name + "--" + sonName);
			if (Integer.parseInt(flag) == 1) {
				p.setQudId(Utility.getLong(id));
				int ver = ((TtAsWrQualityDamagePO) dao.select(p).get(0))
						.getVer();
				p2.setUpdateBy(getCurrDealerId());
				p2.setOemCompanyId(loginUser.getCompanyId());
				p2.setUpdateDate(new Date());
				p2.setVer(ver + 1);
				dao.update(p, p2);
				act.setOutData("success", "true");
			} else {
				p2.setQudId(Utility.getLong(SequenceManager.getSequence("")));
				p2.setCreateBy(getCurrDealerId());
				p2.setCreateDate(new Date());
				p2.setVer(0);
				p2.setOemCompanyId(loginUser.getCompanyId());
				dao.insert(p2);
			}
			act.setOutData("success", "true");
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "质损区域维护");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}

	// 检查质损码是否已经存在
	public void qudCheck() {

		try {

			String code = request.getParamValue("code");
			TtAsWrQualityDamagePO p = new TtAsWrQualityDamagePO();
			p.setQudSonCode(code);
			List list = dao.select(p);
			if (list.size() > 0 && list != null) {
				act.setOutData("success", "true");
			} else {
				act.setOutData("success", "false");
			}
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "质损区域维护");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}

	public void checkLabour() {

		try {

			String[] partType = request.getParamValues("PAY_TYPE_PART");// 配件付费类型
			String[] itemType = request.getParamValues("PAY_TYPE_ITEM");// 工时付费类型
			String[] item = request.getParamValues("WR_LABOURCODE");// 工时
			String[] labour = request.getParamValues("Labour0");// 维修项目
			String[] resbonse = request.getParamValues("RESPONS_NATURE");// 得到
																			// 责任性质
			String str = "false";
			if (item != null) {
				for (int i = 0; i < item.length; i++) {
					if (labour != null) {
						for (int j = 0; j < labour.length; j++) {
							if ((item[i].equalsIgnoreCase(labour[j]))
									&& "11801002".equalsIgnoreCase(partType[j])
									&& "11801001".equalsIgnoreCase(itemType[i])) {
								str = "true";
								break;
							}
						}
					}
					if (str == "true") {
						break;
					}
				}
			}
			act.setOutData("flag", str);

		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "维修工单维护");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}

	// 判断一车一天
	public void checkOneDay() {
		try {
			String repairType = request.getParamValue("REPAIR_TYPE");
			String roStartdate = request.getParamValue("RO_STARTDATE");// 工单开始时间
			String[] partType = request.getParamValues("PAY_TYPE_PART");// 配件是否索赔
			String[] itemType = request.getParamValues("PAY_TYPE_ITEM");// 工时是否索赔
			String[] item = request.getParamValues("WR_LABOURCODE");// 选择的工时
			String[] partCodes = request.getParamValues("PART_CODE"); // 使用的配件代码
			String[] other = request.getParamValues("ITEM_CODE");// 获取其他项目金额
			String[] otherType = request.getParamValues("PAY_TYPE_OTHER");// 其他项目的付费类型
			String[] hasPart = request.getParamValues("HAS_PART");
			String vin = request.getParamValue("VIN");
			String roId = request.getParamValue("ID");
			if (roId == null || "".equalsIgnoreCase(roId)) {
				roId = "0";
			}
			System.out.println("...." + roStartdate);
			boolean oneFlag = false;
			boolean flag = false;
			String haspart = "";
			if (hasPart != null) {
				for (int o = 0; o < hasPart.length; o++) {
					if (Constant.IF_TYPE_YES.toString().equalsIgnoreCase(
							hasPart[o])) {
						haspart = "false";
					} else {
						haspart = "true";
					}
				}
			}
			if (other != null) {
				for (int i = 0; i < other.length; i++) {
					if ("11801002".equalsIgnoreCase(otherType[i])) {
						flag = true;
					}
				}
			}
			if (item != null) {
				for (int i = 0; i < item.length; i++) {
					if ("11801002".equalsIgnoreCase(itemType[i])) {
						flag = true;
					}
				}
			}
			if (partCodes != null) {
				for (int i = 0; i < partCodes.length; i++) {
					if ("11801002".equalsIgnoreCase(partCodes[i])) {
						flag = true;
					}
				}
			}
			if (flag) {
				RoOneDayOneVin oneVin = new RoOneDayOneVin(vin, repairType,
						Long.valueOf(roId), roStartdate.substring(0, 10));
				oneFlag = oneVin.autoAuditing();
			}
			if (oneFlag) {
				act.setOutData("flag", "true");
			} else {
				act.setOutData("flag", "false");
			}
			act.setOutData("haspart", haspart);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "维修工单维护");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * 故障代码维护时，级联质损大类-小类
	 */
	public void changequd() {
		act.getResponse().setContentType("application/json");
		try {
			String code = CommonUtils.checkNull(request.getParamValue("code"));
			act.setOutData("regionList", dao.getQud(code));
			act.setOutData("defaultValue", CommonUtils.checkNull(request
					.getParamValue("defaultValue")));
			act.setOutData("isdisabled",
					CommonUtils.checkNull(request.getParamValue("isdisabled")));
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "级联质损区域");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}

	/**
	 * 新增工单时，质损区域级联故障代码
	 */
	public void changeMalQud() {
		act.getResponse().setContentType("application/json");
		try {
			String code = CommonUtils.checkNull(request.getParamValue("code"));
			System.out.println(code);
			act.setOutData("regionList", dao.getMalQud(code));
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "级联质损区域-故障代码");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}

	public boolean checkWrDate(String vinParent, String wrMilege)
			throws Exception {
		String timeOut = "inTime";
		boolean flag = false;
		TmVehiclePO v = new TmVehiclePO();

		/***** add by liuxh 20131108判断车架号不能为空 *****/
		CommonUtils.jugeVinNull(vinParent);
		/***** add by liuxh 20131108判断车架号不能为空 *****/

		v.setVin(vinParent);
		v = (TmVehiclePO) dao.select(v).get(0);
		Date date = new Date();
		if (v.getPurchasedDate() != null) {
			date = v.getPurchasedDate();
		}
		Date now = new Date();
		String formatStyle = "yyyy-MM-dd";
		SimpleDateFormat df = new SimpleDateFormat(formatStyle);
		String d1 = df.format(date);
		String d2 = df.format(now);
		int month = Utility.compareDate(d1, d2, 1); // 取得购车时间到现在的月份相差
		TtAsWrGamePO g = new TtAsWrGamePO();
		g.setId(v.getClaimTacticsId());
		List<TtAsWrGamePO> gList = dao.select(g);
		if (gList != null && gList.size() > 0) {
			g = gList.get(0);
			if (month > g.getClaimMonth()) {
				flag = true;
			} else if (g.getWrMelieage() < Double.valueOf(wrMilege)) {
				flag = true;
			}
		} else {
			flag = false;
		}
		return flag;
	}

	// 新增配件导出
	public void exportExcel() {
		ClaimReportDao dao1 = ClaimReportDao.getInstance();
		try {

			String PART_CODE = request.getParamValue("PART_CODE");
			String PART_NAME = request.getParamValue("PART_NAME");
			String create_start = request.getParamValue("create_start");
			String create_end = request.getParamValue("create_end");
			String[] head = new String[3];
			head[0] = "配件代码";
			head[1] = "配件名称";
			head[2] = "新增时间";

			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			PageResult<Map<String, Object>> ps = dao.getBase(999999, PART_CODE,
					PART_NAME, create_start, create_end, 1);
			List<Map<String, Object>> list = ps.getRecords();
			List list1 = new ArrayList();
			if (list != null && list.size() != 0) {
				for (int i = 0; i < list.size(); i++) {
					Map map = (Map) list.get(i);
					String[] detail = new String[3];
					detail[0] = (String) map.get("PART_CODE");
					detail[1] = (String) map.get("PART_NAME");
					detail[2] = sdf.format((Date) map.get("CREATE_DATE"));
					list1.add(detail);
				}
			} else {
				String[] detail = new String[3];
				detail[0] = "";
				detail[1] = "";
				detail[2] = "";
				list1.add(detail);
			}
			String setList = "0,1,2";
			String name = "新增配件.xls";
			dao1.toExceVender(ActionContext.getContext().getResponse(),
					request, head, list1, name, "新增配件", setList);
			act.setForword(Base_IS_NEW);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "新增配件导出");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}

	public void showGroup() {
		try {
			List<Map<String, Object>> list = dao.getBrand();
			act.setOutData("list", list);
			act.setForword(showGroupUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "级联质损区域-故障代码");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}

	public void showSeriesList() {
		try {

			String id = request.getParamValue("ID");
			List<Map<String, Object>> list = dao.showSeriesList(id);
			if (!Utility.testString(id)) {
				list = null;
			}
			String str = dao.getStr(list);
			act.setOutData("seriesList", str);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "品牌带出车系");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}

	public void showGroupList() {
		ClaimReportDao dao1 = ClaimReportDao.getInstance();
		try {

			String groupCode = request.getParamValue("groupCode");
			String groupName = request.getParamValue("groupName");
			String series = request.getParamValue("series");
			String brand = request.getParamValue("brand");
			Integer curPage = getCurrPage();
			PageResult<Map<String, Object>> ps = dao.getGroup(series, brand,
					curPage);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "新增配件导出");
			logger.error(loginUser, e1);
			act.setException(e1);
		}

	}

	/**
	 * 
	 */
	public void checkActivetyOne() {
		try {
			String activityNum = CommonUtils.checkNull(request
					.getParamValue("activityNum"));
			String VIN = request.getParamValue("VIN");
			Integer curPage = getCurrPage();
			Map<String, Object> map = dao.checkActivetyOne(VIN);
			if (map != null && !map.isEmpty()) {
				String number = CommonUtils.checkNull(map.get("COUNTVIN")
						.toString());
				Integer activNum = Integer.valueOf(1);
				Integer numberTemp = Integer.valueOf(number);
				if (numberTemp >= activNum) {// 提示如果一台车有2次机会，如果已经做了2次那么应该大于等于台次时提醒
					act.setOutData("flag", "false");
				} else {
					act.setOutData("flag", "true");
				}
				act.setOutData("number", number);
			}
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "新增配件导出");
			logger.error(loginUser, e1);
			act.setException(e1);
		}

	}
	/**
	 * 总部端修改补偿费
	 */
	public void checkChangePrice(){
		try {
			String ro_no = DaoFactory.getParam(request, "ro_no");
			String id = DaoFactory.getParam(request, "id");
			String pass_price = DaoFactory.getParam(request, "pass_price");
			String countPrice = DaoFactory.getParam(request, "countPrice");
			//进行修改补偿费用
			TtAsRepairOrderPO ro1=new TtAsRepairOrderPO();
			ro1.setRoNo(ro_no);;
			TtAsRepairOrderPO ro2=new TtAsRepairOrderPO();
			ro2.setCompensationMoney(BaseUtils.ConvertDouble(countPrice));
			dao.update(ro1, ro2);
			//修改当前的补偿费的信息
			TtAsWrCompensationAppPO t1=new TtAsWrCompensationAppPO();
			t1.setPkid(BaseUtils.ConvertLong(id));
			TtAsWrCompensationAppPO t2=new TtAsWrCompensationAppPO();
			t2.setPassPrice(BaseUtils.ConvertDouble(pass_price));
			dao.update(t1, t2);
			act.setOutData("pass_price", "1");
		} catch (Exception e) {
			act.setOutData("pass_price", "0");
			e.printStackTrace();
		}
		
	}
	
	public void updateFileUploadLast() {
		try {
			String fjid = request.getParamValue("fjid");
			String pjid = request.getParamValue("pjid");
			String id = request.getParamValue("ID");
			if(fjid!=null && !fjid.equals("") && !fjid.equals("undefined")){
				FsFileuploadPO po1 = new FsFileuploadPO();
				FsFileuploadPO po2 = new FsFileuploadPO();
				po1.setFjid(Long.parseLong(fjid));
				po2.setYwzj(Long.parseLong(id));
				po2.setPjid(pjid);
				dao.update(po1, po2);
			}
			act.setOutData("result", "1");
		} catch (Exception e) {
			act.setOutData("result", "0");
			logger.error(loginUser, e);
			act.setException(e);
		}
	}
	/**
	 * 服务站申请工单废弃
	 */
	public void roCancelApply(){
		String flag = DaoFactory.getParam(request, "query");
		if("true".equals(flag)){
			PageResult<Map<String,Object>> ps = dao.roCancelApply(request,getCurrDealerId(),Constant.PAGE_SIZE, getCurrPage());
			act.setOutData("ps", ps);
		}
		super.sendMsgByUrl(sendUrl(ClaimManualAuditing.class, "roCancelApply"), "服务站申请工单废弃");
	}
	/**
	 * 服务站申请工单废弃确认
	 */
	public void roCancelApplyDo(){
		int res=1;
		try {
			String ro_no = DaoFactory.getParam(request, "ro_no");
			if(!"".equals(ro_no)){
				dao.update("update Tt_As_Repair_Order o set o.is_cancel=1 where o.ro_no='"+ro_no+"'", null);
			}
		} catch (Exception e) {
			res=-1;
			e.printStackTrace();
		}finally{
			super.setJsonSuccByres(res);
		}
	}
	/**
	 * 服务站申请索赔单废弃
	 */
	public void claimCancelApply(){
		String flag = DaoFactory.getParam(request, "query");
		if("true".equals(flag)){
			PageResult<Map<String,Object>> ps = dao.claimCancelApply(request,getCurrDealerId(),Constant.PAGE_SIZE, getCurrPage());
			act.setOutData("ps", ps);
		}
		super.sendMsgByUrl(sendUrl(ClaimManualAuditing.class, "claimCancelApply"), "服务站申请索赔单废弃");
	}
	
	public void roBalanceInterface(String[] PayType,String id,String freeTimes,String date,String[] PayTypePart,String[] BourPayType,String startDate,int pkg) {
		try {
			TtAsRepairOrderPO tarop = new TtAsRepairOrderPO();
			tarop.setId(Utility.getLong(id));
			
			List<TtAsRepairOrderPO> listData = dao.select(tarop);//查询查出来的工单数据
			String RepairTypeCode = listData.get(0).getRepairTypeCode();
			TtAsRepairOrderPO taropUp = new TtAsRepairOrderPO();
			Date forBalanceTime = listData.get(0).getForBalanceTime() ;
			if (forBalanceTime== null || "".equalsIgnoreCase(forBalanceTime.toString())) {
				taropUp.setForBalanceTime(Utility.getDate(date, 3));// 结算时间 第一次
			}else{
				taropUp.setForBalanceTime(forBalanceTime); //设置 为 第一次结算的时间
			}
			taropUp.setId(Utility.getLong(id));
			taropUp.setRoStatus(Constant.RO_STATUS_02); // 已结算
			listData.get(0).setRoStatus(Constant.RO_STATUS_02);
			taropUp.setVer(listData.get(0).getVer() + 1);// 将版本号加一
			taropUp.setPrintRoTime(listData.get(0).getCreateDate());

			//如果工单存在保养类型的，更新车辆保养次数
			List<Map<String, String>> partCode = dao
					.getRepairOrderPartCheck(id);
			String roPartType = "";
			boolean flag = false;
			boolean baoyangFlag = false;
			for (int j = 0; j < partCode.size(); j++) {
				if(!flag){
					if (partCode.get(j).get("PART_TYPE_ID") == null) {
						flag = true;
					}
				}
				//增加工单信息中配件的维修类型
				if(!baoyangFlag){
					if(partCode != null && partCode.get(j)!=null && partCode.get(j).get("REPAIRTYPECODE")!= null){
						if("93331002".equals(partCode.get(j).get("REPAIRTYPECODE").toString())){
							//如果单据中含有保养的  则更新保养次数
							baoyangFlag = true;
							if(baoyangFlag){
								doBaoYangUpdate(listData.get(0).getVin(),freeTimes);
							}
						}
						if("93331003".equals(partCode.get(j).get("REPAIRTYPECODE").toString())	){
							//如果单据中含有保养的  则更新保养次数
							baoyangFlag = true;
							if(baoyangFlag){
								doBaoYangUpdate(listData.get(0).getVin(),freeTimes);
							}
						}
					}
				}
			}
			if (!flag) {
				taropUp.setPartFlag(1); // 属于配件大类
			} else {
				taropUp.setPartFlag(0); // 不属于配件大类
			}
			if (PayType != null && PayType.length > 0 && pkg == 1 ) {
				for (String PayType1 : PayType) {
					if (PayType1.equals(Constant.PAY_TYPE_02.toString())) {
						Date roCreateDate = listData.get(0).getRoCreateDate();
						dao.vehicleJude(listData.get(0).getVin(), ""
								+ listData.get(0).getInMileage(), Utility.handleDate(roCreateDate),
								listData.get(0), loginUser);
						break;
					}
				}
			}
			boolean fage = true;
			if (PayTypePart != null && PayTypePart.length > 0 && pkg == 1 ) {
				for (String PayTypePart1 : PayTypePart) {
					if (PayTypePart1.split("-")[0].equals(""
							+ (int) Constant.PAY_TYPE_02)) {
						dao.baseJude(PayTypePart, listData.get(0), loginUser);
						fage = false;
						break;
					}
				}
			}
			if (fage) {
				if (BourPayType != null && BourPayType.length > 0 && pkg == 1) {
					for (String BourPayType1 : BourPayType) {
						if (BourPayType1.split("-")[0].equals(""
								+ (int) Constant.PAY_TYPE_02)) {
							dao.bourjude(BourPayType, listData.get(0),
									loginUser);
							break;
						}
					}
				}

			}
			dao.update(tarop, taropUp);
			return ;
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "工单结算");
			logger.error(loginUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 如果新的工单中含有保养类型的 则更新保养次数
	 * @param vin
	 * @param freeTimes
	 * @throws Exception
	 */
	public void doBaoYangUpdate(String vin,String freeTimes) throws Exception{
		TmVehiclePO pv = new TmVehiclePO();
		TmVehiclePO v = new TmVehiclePO();
		TmVehiclePO pv2 = new TmVehiclePO();
		pv.setVin(vin);
		v.setVin(vin);
		CommonUtils.jugeVinNull(vin);
		v = (TmVehiclePO) dao.select(v).get(0);
		if (Integer.parseInt(freeTimes) >= v.getFreeTimes()) {// 如果当前的次数
			pv2.setFreeTimes(Integer.parseInt(freeTimes)+1);
			dao.update(pv, pv2);
		}
	}
	
	/**
	 * 回滚工单中含有保养的次数
	 * @param vin
	 * @throws Exception 
	 */
	public void doBaoyangRollBack(String vin,String freeTimes) throws Exception{
		TmVehiclePO pv = new TmVehiclePO();
		TmVehiclePO pv3 = new TmVehiclePO();
		TmVehiclePO pv2 = new TmVehiclePO();
		pv.setVin(vin);
		pv3.setVin(vin);

		/***** add by liuxh 20131108判断车架号不能为空 *****/
		CommonUtils.jugeVinNull(vin);
		/***** add by liuxh 20131108判断车架号不能为空 *****/

		pv2.setFreeTimes(Integer.parseInt(freeTimes) - 1);
		pv3 = (TmVehiclePO) dao.select(pv3).get(0);
		if (Integer.parseInt(freeTimes) >= pv3.getFreeTimes()) {// 如果系统中的保养次数大于此次保养次数
																// 则不更新
			dao.update(pv, pv2);
		}
	}
	/**
	 * 重新结算的时候，如果是取消结算过则需要回滚新三包
	 * @param id
	 * @param freeTimes
	 * @param PayType
	 * @param PayTypePart
	 */
	public void roBalanceCancelAudit(String id,String freeTimes) {
		try {
			
			StringBuffer sql= new StringBuffer();
			sql.append("select distinct t.part_code,t.pay_type,t.repair_part_id from Tt_As_Ro_Old_Repair_Part t  where t.ro_id= "+id+"  and   t.pay_type = 11801002 and t.repairtypecode = 93331001 ");

			
			List<TtAsRoOldRepairPartPO> oList = (List<TtAsRoOldRepairPartPO>)dao.select(TtAsRoOldRepairPartPO.class, sql.toString(), null);
			if(null == oList || oList.size()==0){
				//判断如果不存在取消结算审核功能，则返回; 否则继续下面回滚历史工单里面的配件三包信息
				return;
			}
			
			TtAsRepairOrderPO tarop = new TtAsRepairOrderPO();
			tarop.setId(Utility.getLong(id));
			
			List<TtAsRepairOrderPO> list = dao.select(tarop);
			if (list != null && list.size() > 0) {
				tarop = list.get(0);
			}
			// 如果是保养，则将保养次数更新到vehicle表, 取消结算时，将车辆表的保养次数更新为改工单的前一次次数
			//如果原始工单还有保养的单子 需要回滚更新保养次数
			boolean baoyangFlag = false;
			TtAsRoOldRepairPartPO repairePartPO = null;
			//String[] PayType = new String[oList.size()];
			String[] PayTypePart = new String[oList.size()];
			tarop.setRoStatus(11591003);
			if (null != oList || oList.size() >  0) {
						dao.vehicleJude(tarop.getVin(),
								"" + tarop.getInMileage(),
								Utility.handleDate(tarop.getRoCreateDate()),
								tarop, loginUser);
			}
			boolean fage = true;
			if (null != oList || oList.size() >  0) {
				int k = 0 ;
				for(TtAsRoOldRepairPartPO po :oList )
				{
					PayTypePart[k] =  po.getPayType()+"-"+po.getRepairPartId();
					k++;
				}
				for (String PayTypePart1 : PayTypePart) {
					if (PayTypePart1.split("-")[0].equals(""
							+ (int) Constant.PAY_TYPE_02)) {
						dao.baseJude(PayTypePart, tarop, loginUser);
						fage = false;
						break;
					}
				}
			}
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "工单维护");
			logger.error(loginUser, e1);
			act.setException(e1);
		}

	}
	
}
