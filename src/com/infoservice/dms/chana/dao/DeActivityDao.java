package com.infoservice.dms.chana.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import org.apache.log4j.Logger;
import com.infodms.dms.common.Constant;
import com.infodms.dms.po.TpAsServiceVehiclePO;
import com.infoservice.dms.chana.common.DEConstant;
import com.infoservice.dms.chana.common.DEUtil;
import com.infoservice.dms.chana.common.RpcException;
import com.infoservice.dms.chana.vo.ActivityLabourVO;
import com.infoservice.dms.chana.vo.ActivityModelVO;
import com.infoservice.dms.chana.vo.ActivityPartVO;
import com.infoservice.dms.chana.vo.ActivityVO;
import com.infoservice.dms.chana.vo.ActivityVehicleVO;
import com.infoservice.dms.chana.vo.ActivityYieldlyVO;
import com.infoservice.dms.chana.vo.BaseVO;
import com.infoservice.po3.bean.PO;

public class DeActivityDao extends AbstractIFDao {
	public static Logger logger = Logger.getLogger(DeActivityDao.class);
	private static final DeActivityDao dao = new DeActivityDao();
	private static final int ACTIVITY = 1;
	private static final int ACTIVITY_LABOUR = 2;
	private static final int ACTIVITY_PART = 3;
	private static final int ACTIVITY_MODEL = 4;
	private static final int ACTIVITY_VEHICLE = 5;
	private static final int ACTIVITY_VEHICLE_SEND = 6;
	private static final int ACTIVITY_ID = 7;
	private static final int ACTIVITY_VEHICLE_READY = 8;
	private static final int ACTIVITY_VEHICLE_YIELDLY = 9;//根据活动id查询产地
	public static final DeActivityDao getInstance() {
		return dao;
	}
	/**
	 * 
	* @Title: queryActivity 
	* @Description: TODO(查询未下发的服务活动) 
	* @param @return    设定文件 
	* @return List<ActivityVO>    返回类型 
	* @throws
	 */
	public List<ActivityVO> queryActivity(Long id) {
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT A.ACTIVITY_ID, A.ACTIVITY_CODE, A.ACTIVITY_NAME, A.ACTIVITY_TYPE, A.ACTIVITY_KIND,\n" );
		sql.append("       A.CREATE_BY, A.STARTDATE, A.ENDDATE, A.RELEASEDATE, A.STATUS, A.ACTIVITY_FEE,\n" );
		sql.append("       A.OTHER_FEE, A.IS_CLAIM, A.IS_DEL, A.SINGLE_CAR_NUM, A.SET_DIRECT, A.MAX_CAR,\n" );
		sql.append("       B.SALE_DATE_START AS S_START, B.SALE_DATE_END AS S_END,\n" );
		sql.append("       C.SALE_DATE_START AS C_START, C.SALE_DATE_END AS C_END,\n" );
		sql.append("       D.CAR_CHARACTOR\n" );
		sql.append(" FROM TT_AS_ACTIVITY A,\n" );
		sql.append(" /*车辆销售日期*/\n" );
		sql.append(" (SELECT ACTIVITY_ID, SALE_DATE_START, SALE_DATE_END FROM TT_AS_ACTIVITY_AGE WHERE DATE_TYPE = 10801001)B,\n" );
		sql.append(" /*车辆生产日期*/\n" );
		sql.append(" (SELECT ACTIVITY_ID, SALE_DATE_START, SALE_DATE_END FROM TT_AS_ACTIVITY_AGE WHERE DATE_TYPE = 10801002)C,\n" );
		sql.append(" TT_AS_ACTIVITY_CHARACTOR D\n" );
		sql.append(" WHERE A.ACTIVITY_ID = " ).append(id).append("\n");
		sql.append(" AND A.ACTIVITY_ID = B.ACTIVITY_ID(+)\n" );
		sql.append(" AND A.ACTIVITY_ID = C.ACTIVITY_ID(+)\n" );
		sql.append(" AND A.ACTIVITY_ID = D.ACTIVITY_ID(+)");
		List<ActivityVO> vos = select(sql.toString(), null, ACTIVITY);
		return vos;
	}
	
	/**
	 * 
	* @Title: queryActivityById 
	* @Description: TODO(根据activeId查询服务活动) 
	* @param 
	* @return List<ActivityVO>    返回类型 
	* @throws
	 */
	public ActivityVO queryActivityById(Long activityId) {
		StringBuilder str = new StringBuilder();
		str.append("SELECT ACTIVITY_CODE, ACTIVITY_NAME \n");
		str.append(" FROM TT_AS_ACTIVITY");
		str.append(" WHERE ACTIVITY_ID = ").append(activityId);
		ActivityVO vos = (ActivityVO) select(str.toString(), null, ACTIVITY_ID).get(0);
		return vos;
	}
	
	/**
	 * 
	* @Title: queryActivityRepairItem 
	* @Description: TODO(根据服务活动id查询维修项目) 
	* @param @return    设定文件 
	* @return List<ActivityLabourVO>    返回类型 
	* @throws
	 */
	private List<BaseVO> queryActivityRepairItem(Long activityId) {
		StringBuilder str = new StringBuilder();
		str.append("SELECT ITEM_CODE, ITEM_NAME, NORMAL_LABOR, LABOR_FEE \n");
		str.append(" FROM TT_AS_ACTIVITY_REPAIRITEM T");
		str.append(" WHERE T.ACTIVITY_ID = ").append(activityId);
		List<BaseVO> vos = select(str.toString(), null, ACTIVITY_LABOUR);
		return vos;
	}
	
	/**
	 * 
	* @Title: queryActivityRepairItem 
	* @Description: TODO(根据服务活动id查询活动配件) 
	* @param @return    设定文件 
	* @return List<ActivityLabourVO>    返回类型 
	* @throws
	 */
	private List<BaseVO> queryActivityParts(Long activityId) {
		StringBuilder str = new StringBuilder();
		str.append("SELECT PART_NO, PART_NAME, PART_UNIT, PART_QUANTITY, PART_PRICE, PART_AMOUNT \n");
		str.append(" FROM TT_AS_ACTIVITY_PARTS T");
		str.append(" WHERE T.ACTIVITY_ID = ").append(activityId);
		List<BaseVO> vos = select(str.toString(), null, ACTIVITY_PART);
		return vos;
	}
	
	/**
	 * 
	* @Title: queryActivityModel 
	* @Description: TODO(根据服务活动id活动物料组) 
	* @param activityId 活动ID
	* @return List<ActivityModelVO>    返回类型 
	* @throws
	 */
	private List<BaseVO> queryActivityModel(Long activityId) {
		StringBuilder str = new StringBuilder();
		str.append("SELECT A.GROUP_CODE FROM \n"); 
		str.append(" TM_VHCL_MATERIAL_GROUP A, \n");
		str.append("(SELECT MATERIAL_GROUP_ID FROM TT_AS_ACTIVITY_MGROUP WHERE ACTIVITY_ID = ");
		str.append(activityId).append(") B ");
		str.append("WHERE A.GROUP_ID = B.MATERIAL_GROUP_ID");
		List<BaseVO> vos = select(str.toString(), null, ACTIVITY_MODEL);
		return vos;
	}
	
	/**
	 * 
	* @Title: queryActivityVehicle 
	* @Description: TODO(根据服务活动id查询未下发的活动车辆) 
	* @param activityId 活动ID
	* @return List<ActivityModelVO>    返回类型 
	* @throws
	 */
	private List<BaseVO> queryActivityVehicle(Long activityId) {
		StringBuilder str = new StringBuilder();
		str.append("SELECT A.CUSTOMER_NAME, A.VIN, A.LINCENSE_TAG, A.LINKMAN, \n");
		str.append("A.LINKMAN_OFFICE_PHONE, A.LINKMAN_MOBILE, A.PROVINCE, A.AREA, \n"); 
		str.append("A.TOWN, A.POSTAL_CODE, A.CUSTOMER_ADDRESS, B.DEALER_CODE, B.DEALER_NAME \n");
		str.append("FROM TT_AS_ACTIVITY_VEHICLE A, TM_DEALER B \n");
		str.append("WHERE A.DEALER_ID = B.DEALER_ID \n");
		str.append("AND A.ACTIVITY_ID = ").append(activityId).append("\n");
		//未下发的服务活动车
		str.append("AND A.CAR_STATUS = ").append(Constant.SERVICEACTIVITY_STATUS_01);
		List<BaseVO> vos = select(str.toString(), null, ACTIVITY_VEHICLE);
		return vos;
	}
	
	/**
	 * 
	* @Title: queryActivityVehicle 
	* @Description: TODO(根据服务活动id和车辆id查询车辆信息) 
	* @param activityId 活动ID, vehicleId 车辆ID
	* @return List<ActivityVehicleVO>    返回类型 
	* @throws
	 */
	/*
	private List<BaseVO> queryActivityVehicle(Long activityId, Long vehicleId) {
		StringBuilder str = new StringBuilder();
		str.append("SELECT A.CUSTOMER_NAME, A.VIN, A.LINCENSE_TAG, A.LINKMAN, \n");
		str.append("A.LINKMAN_OFFICE_PHONE, A.LINKMAN_MOBILE, A.PROVINCE, A.AREA, \n"); 
		str.append("A.TOWN, A.POSTAL_CODE, A.CUSTOMER_ADDRESS, B.DEALER_CODE, B.DEALER_NAME \n");
		str.append("FROM TT_AS_ACTIVITY_VEHICLE A, TM_DEALER B \n");
		str.append("WHERE A.DEALER_ID = B.DEALER_ID \n");
		str.append("AND A.ACTIVITY_ID = ").append(activityId);
		str.append(" AND A.ID = ").append(vehicleId);
		List<BaseVO> vos = select(str.toString(), null, ACTIVITY_VEHICLE);
		return vos;
	}
	*/
	
	/**
	 * 
	* @Title: queryActivityReadyVehicle 
	* @Description: TODO(查询经销商修好上报需要下发的服务活动车辆) 
	* @param @param activityCode
	* @param @return    设定文件 
	* @return List<BaseVO>    返回类型 
	* @throws
	 */
	private List<BaseVO> queryActivityReadyVehicle(String activityCode) {
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT A.VIN, A.CAMPAIGN_DATE, B.DEALER_CODE, B.DEALER_NAME, COUNT(C.ID) AS C_ID\n" );
		sql.append("  FROM TP_AS_SERVICE_VEHICLE A, TM_DEALER B, TT_AS_REPAIR_ORDER C\n" );
		sql.append(" WHERE A.OPERATE_SST_CODE = B.DEALER_CODE\n" );
		sql.append("   AND A.ACTIVITY_CODE = '" ).append(activityCode).append("'\n");
		sql.append("   AND A.VIN = C.VIN\n" );
		sql.append("   AND C.CAM_CODE = '" ).append(activityCode).append("'\n");
		sql.append("   AND A.IF_STATUS = ").append(DEConstant.IF_STATUS_0).append("\n");
		sql.append(" GROUP BY A.VIN, B.DEALER_CODE, B.DEALER_NAME, A.CAMPAIGN_DATE");
		List<BaseVO> vos = select(sql.toString(), null, ACTIVITY_VEHICLE_READY);
		return vos;
	}
	
	/**
	 * 
	* @Title: queryActivityResultVO 
	* @Description: TODO(查询已经修好上报的服务车辆) 
	* @param @return  
	* @return List<ActivityVehicleVO>    返回类型 
	* @throws
	 */
	public List<ActivityVO> queryActivityResultVO() {
		StringBuilder str = new StringBuilder();	
		str.append("SELECT A.ACTIVITY_CODE, A.ACTIVITY_NAME, B.OPERATE_SST_CODE \n");
		str.append("  FROM TT_AS_ACTIVITY A, \n");
		str.append("  (SELECT ACTIVITY_CODE, OPERATE_SST_CODE FROM TP_AS_SERVICE_VEHICLE WHERE IF_STATUS = ");
		str.append(DEConstant.IF_STATUS_0).append(" ) B \n");
		str.append(" WHERE A.ACTIVITY_CODE = B.ACTIVITY_CODE");		
		List<ActivityVO> vos = select(str.toString(), null, ACTIVITY_VEHICLE_SEND);
		return vos;
	}
	
	/**
	 * 
	* @Title: queryActivityYieldly 
	* @Description: TODO(根据服务活动id查询车辆产地列表) 
	* @param activityId 活动ID
	* @return List<ActivityModelVO>    返回类型 
	* @throws
	 */
	private List<BaseVO> queryActivityYieldly(Long activityId) {
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT B.CODE_ID, B.CODE_DESC\n" );
		sql.append("  FROM TT_AS_ACTIVITY_YIELDLY A, TC_CODE B\n" );
		sql.append(" WHERE A.ACTIVITY_ID = \n" ).append(activityId);
		sql.append("   AND A.CAR_YIELDLY = B.CODE_ID");
		List<BaseVO> vos = select(sql.toString(), null, ACTIVITY_VEHICLE_YIELDLY);
		return vos;
	}
	
	protected PO wrapperPO(ResultSet rs, int idx) {
		TpAsServiceVehiclePO po = new TpAsServiceVehiclePO();
		try {
			po.setActivityCode(rs.getString("Activity_Code"));
		} catch (SQLException e) {
			throw new RpcException(e.getMessage());
		}
		return po;
	}
	
	protected BaseVO wrapperVO(ResultSet rs, int idx, int flag) {
		switch (flag) {
		//服务活动
		case ACTIVITY :
			return wrapperActivityVO(rs, idx);
		//维修项目
		case ACTIVITY_LABOUR : 
			return wrapperActivityLabourVO(rs, idx);
		//维修配件
		case ACTIVITY_PART : 
			return wrapperActivityPartsVO(rs, idx);
		//服务车型
		case ACTIVITY_MODEL :
			return wrapperActivityModelVO(rs, idx);
		//服务车辆
		case ACTIVITY_VEHICLE : 
			return wrapperActivityVehicleVO(rs, idx);
		//已经由下端修好的服务车辆
		case ACTIVITY_VEHICLE_SEND :
			return wrapperActivityResultVO(rs, idx);
		//根据服务活动Id查询的服务活动
		//case ACTIVITY_ID :
			//return wrapperActivityVOById(rs, idx);
		case ACTIVITY_VEHICLE_READY : 
			return wrapperActivityReadyVehicleVO(rs, idx);
		case ACTIVITY_VEHICLE_YIELDLY : 
			return wrapperActivityYieldlyVO(rs, idx);
		}
		return null;
	}
	
	private BaseVO wrapperActivityVO(ResultSet rs, int idx) {
		ActivityVO vo = new ActivityVO();
		try {
			vo.setActivityCode(rs.getString("ACTIVITY_CODE"));
			vo.setActivityName(rs.getString("ACTIVITY_NAME"));
			vo.setActivityType(rs.getInt("ACTIVITY_TYPE"));
			vo.setActivityKind(rs.getString("ACTIVITY_KIND"));
			vo.setBeginDate(rs.getDate("STARTDATE"));
			vo.setEndDate(rs.getDate("ENDDATE"));
			vo.setReleaseDate(rs.getDate("RELEASEDATE"));
			vo.setReleaseTag(rs.getInt("STATUS"));
//			vo.setLabourAmount(rs.getDouble("ACTIVITY_FEE"));
//			vo.setRepairPartAmount(rs.getDouble("ACTIVITY_FEE"));
			vo.setActivityAmount(rs.getDouble("ACTIVITY_FEE"));
			vo.setOtherFee(rs.getDouble("OTHER_FEE"));
			vo.setIsClaim(rs.getInt("IS_CLAIM") == 0 ? Constant.IF_TYPE_YES : Constant.IF_TYPE_NO);
			vo.setIsValid(rs.getInt("IS_DEL"));
			vo.setDownTimestamp(downTimestamp);
			vo.setSalesDateBegin(rs.getDate("S_START"));
			vo.setSalesDateEnd(rs.getDate("S_END"));
			vo.setProductDateBegin(rs.getDate("C_START"));
			vo.setProductDateEnd(rs.getDate("C_END"));
			vo.setRepairCount(rs.getInt("MAX_CAR"));
			vo.setSingleCount(rs.getInt("SINGLE_CAR_NUM"));
			vo.setVehiclePurpose(rs.getInt("CAR_CHARACTOR"));
			vo.setLabourVoList(DEUtil.transType(queryActivityRepairItem(rs.getLong("ACTIVITY_ID"))));
			vo.setPartVoList(DEUtil.transType(queryActivityParts(rs.getLong("ACTIVITY_ID"))));
			vo.setModelVoList(DEUtil.transType(queryActivityModel(rs.getLong("ACTIVITY_ID"))));
			vo.setVehicleVoList(DEUtil.transType(queryActivityVehicle(rs.getLong("ACTIVITY_ID"))));
			vo.setYieldlyVoList(DEUtil.transType(queryActivityYieldly(rs.getLong("ACTIVITY_ID"))));
		} catch (SQLException e) {
			throw new RpcException(e.getMessage());
		}
		return vo;
	}
	
	@SuppressWarnings("unused")
	private BaseVO wrapperActivityVOById(ResultSet rs, int idx) {
		ActivityVO vo = new ActivityVO();
		try {
			vo.setActivityCode(rs.getString("ACTIVITY_CODE"));
			vo.setActivityName(rs.getString("ACTIVITY_NAME"));
		} catch (SQLException e) {
			throw new RpcException(e.getMessage());
		}
		return vo;
	}
	
	private BaseVO wrapperActivityLabourVO(ResultSet rs, int idx) {
		ActivityLabourVO vo = new ActivityLabourVO();
		try {
			vo.setLabourCode(rs.getString("ITEM_CODE"));
			vo.setLabourName(rs.getString("ITEM_NAME"));
			vo.setStdLabourHour(rs.getDouble("NORMAL_LABOR"));
			vo.setLabourAmount(rs.getDouble("LABOR_FEE"));
		} catch (SQLException e) {
			throw new RpcException(e.getMessage());
		}
		return vo;
	}
	
	private BaseVO wrapperActivityPartsVO(ResultSet rs, int idx) {
		ActivityPartVO vo = new ActivityPartVO();
		try {
			vo.setPartNo(rs.getString("PART_NO"));
			vo.setPartName(rs.getString("PART_NAME"));
			vo.setUnitName(rs.getString("PART_UNIT"));
			vo.setPartQuantity(rs.getFloat("PART_QUANTITY"));
			vo.setPartSalesPrice(rs.getDouble("PART_PRICE"));
			vo.setPartSalesAmount(rs.getDouble("PART_AMOUNT"));
		} catch (SQLException e) {
			throw new RpcException(e.getMessage());
		}
		return vo;
	}
	
	private BaseVO wrapperActivityModelVO(ResultSet rs, int idx) {
		ActivityModelVO vo = new ActivityModelVO();
		try {
			vo.setModelCode(rs.getString("GROUP_CODE"));
		} catch (SQLException e) {
			throw new RpcException(e.getMessage());
		}
		return vo;
	}
	
	private BaseVO wrapperActivityVehicleVO(ResultSet rs, int idx) {
		ActivityVehicleVO vo = new ActivityVehicleVO();
		try {
			//Map<String, Object> map = deCommonDao.getDmsDealerCode(rs.getString("DEALER_CODE"));
			//vo.setDutyEntityCode(map.get("DMS_CODE").toString());
			//vo.setDutyEntityName(map.get("COMPANY_SHORTNAME").toString());
			vo.setCustomerName(rs.getString("CUSTOMER_NAME"));
			vo.setVin(rs.getString("VIN"));
			vo.setLicense(rs.getString("LINCENSE_TAG"));
			vo.setContactorName(rs.getString("LINKMAN"));
			vo.setContactorPhone(rs.getString("LINKMAN_OFFICE_PHONE"));
			vo.setContactorMobile(rs.getString("LINKMAN_MOBILE"));
			vo.setZipCode(rs.getString("POSTAL_CODE"));
			StringBuilder address = new StringBuilder();
			address.append(rs.getString("PROVINCE")).append(" ").append(rs.getString("AREA"))
				.append(" ").append(rs.getString("TOWN")).append(" ").append(rs.getString("CUSTOMER_ADDRESS"));
			vo.setAddress(address.toString());
		} catch (Exception e) {
			throw new RpcException(e.getMessage());
		}
		return vo;
	}
	
	/**
	 * 
	* @Title: wrapperActivityVehicleVO 
	* @Description: TODO(组装已经修理完成待下发的车辆) 
	* @param @return    设定文件 
	* @return BaseVO    返回类型 
	* @throws
	 */
	private BaseVO wrapperActivityResultVO(ResultSet rs, int idx) {
		ActivityVO vo = new ActivityVO();
		try {
			vo.setActivityCode(rs.getString("ACTIVITY_CODE"));
			vo.setActivityName(rs.getString("ACTIVITY_NAME"));
			//vo.setEntityCode(rs.getString("OPERATE_SST_CODE"));
			vo.setDownTimestamp(downTimestamp);
			vo.setVehicleVoList(DEUtil.transType(queryActivityReadyVehicle(rs.getString("ACTIVITY_CODE"))));
		} catch (SQLException e) {
			throw new RpcException(e.getMessage());
		}
		return vo;
	}
	
	private BaseVO wrapperActivityReadyVehicleVO(ResultSet rs, int idx) {
		ActivityVehicleVO vo = new ActivityVehicleVO();
		try {
			vo.setVin(rs.getString("VIN"));
			vo.setRealEntityCode(rs.getString("DEALER_CODE"));
			vo.setRealEntityName(rs.getString("DEALER_NAME"));
			vo.setSingleSum(rs.getInt("C_ID"));
			vo.setCampaignDate(rs.getDate("CAMPAIGN_DATE"));
		} catch (Exception e) {
			throw new RpcException(e.getMessage());
		}
		return vo;
	}

	private BaseVO wrapperActivityYieldlyVO(ResultSet rs, int idx) {
		ActivityYieldlyVO vo = new ActivityYieldlyVO();
		try {
			vo.setYieldlyCode(rs.getInt("CODE_ID"));
			vo.setYieldlyName(rs.getString("CODE_DESC"));
		} catch (Exception e) {
			throw new RpcException(e.getMessage());
		}
		return vo;
	}

}
