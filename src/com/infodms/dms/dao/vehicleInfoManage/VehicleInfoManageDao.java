package com.infodms.dms.dao.vehicleInfoManage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.bean.TtAsBarcodeApplyBean;
import com.infodms.dms.bean.TtAsPackgeChangeDetailBean;
import com.infodms.dms.bean.TtVehicleChangeBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.common.materialManager.MaterialGroupManagerDao;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.po.FsFileuploadPO;
import com.infodms.dms.po.TmVehiclePinRequestPO;
import com.infodms.dms.po.TtAsRepairOrderPO;
import com.infodms.dms.po.TtAsWrGamePO;
import com.infodms.dms.po.TtVehicleChangePO;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

public class VehicleInfoManageDao extends BaseDao {
	public static final Logger logger = Logger.getLogger(VehicleInfoManageDao.class);
	private static final VehicleInfoManageDao dao = new VehicleInfoManageDao();
	MaterialGroupManagerDao dao1 =MaterialGroupManagerDao.getInstance();
	public static final VehicleInfoManageDao getInstance() {
		return dao;
	}
	/**
	 * 
	* @Title: queryVehicleInfoByVin 
	* @Description: TODO(根据VIN查询车辆信息) 
	* @param @param vin
	* @return Map<String,Object>    返回类型 
	* @throws
	 */
	public Map<String, Object> queryVehicleInfoByVin(String vin) {
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT A.VIN, A.ENGINE_NO, TO_CHAR(A.PURCHASED_DATE, 'YYYY-MM-DD') AS PURCHASED_DATE, nvl(A.MILEAGE,0) mileage, nvl(A.FREE_TIMES,0) free_times, A.SERIES_ID, A.MODEL_ID,\n" );
		sql.append("       a.license_no VEHICLE_NO, ba.area_name AS YIELDLY, ba.area_id AS Y_ID, D1.GROUP_NAME AS SERIES_CODE, D2.GROUP_NAME AS MODEL_CODE,\n" );
		sql.append("       E.CTM_NAME, E.MAIN_PHONE, E.CTM_ID, E.ADDRESS, F.GAME_CODE, F.ID AS GAME_ID, G.RULE_CODE\n" );
		sql.append("  FROM TM_VEHICLE A, TT_DEALER_ACTUAL_SALES B, tm_business_area ba, TM_VHCL_MATERIAL_GROUP D1, TM_VHCL_MATERIAL_GROUP D2,\n" );
		sql.append("       TT_CUSTOMER E, TT_AS_WR_GAME F, TT_AS_WR_RULE G\n" );
		sql.append(" WHERE A.VIN = '" ).append(vin).append("'\n");
		sql.append("   AND A.VEHICLE_ID = B.VEHICLE_ID(+)\n" );
		sql.append("   AND A.YIELDLY = ba.area_id(+)\n" );
		sql.append("   AND A.SERIES_ID = D1.GROUP_ID(+)\n" );
		sql.append("   AND A.MODEL_ID = D2.GROUP_ID(+)\n" );
		sql.append("   AND A.CLAIM_TACTICS_ID = F.ID(+)\n" );
		sql.append("   AND B.CTM_ID = E.CTM_ID(+)\n" );
		sql.append("   AND F.RULE_ID = G.ID(+)");
		Map<String, Object> ps = pageQueryMap(sql.toString(), null, getFunName());
		return ps;
	}
	/**
	 * 
	* @Title: queryWrGame 
	* @Description: TODO(根据公司ID查询三包策略,包括当前策略和基础策略) 
	* @param @param companyId 公司ID
	* @return List<TtAsWrGamePO>    返回类型 
	 */
	public List<TtAsWrGamePO> queryWrGame(Long companyId) {
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT A.GAME_CODE, A.COMPANY_ID\n" );
		sql.append("  FROM TT_AS_WR_GAME A\n" );
		sql.append(" WHERE A.GAME_STATUS = " ).append(Constant.STATUS_ENABLE).append("\n");
		sql.append("   AND A.COMPANY_ID = " ).append(companyId);
		List<TtAsWrGamePO> list = select(TtAsWrGamePO.class, sql.toString(), null);
		return list;
	}
	
	/**
	 * 
	* @Title: queryVehicleChangeInfo 
	* @Description: TODO(分页查询车辆更改信息) 
	* @param @return    设定文件 
	* @return List<Map<String,Object>>    返回类型 
	* @throws
	 */
	@SuppressWarnings("unchecked")
	public PageResult<Map<String, Object>> queryVehicleChangeInfo(TtVehicleChangeBean bean, int curPage, int pageSize) {
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT A.ID, A.VIN, A.VEHICLE_NO, TO_CHAR(A.PURCHASED_DATE, 'YYYY-MM-DD') AS PURCHASED_DATE, A.MILEAGE, A.FREE_TIMES, A.APPLY_DATA, \n" );
		sql.append("       A.APPLY_TYPE, A.STATUS,a.apply_date,a.check_date, \n");
		sql.append("       B.CTM_NAME, C.GROUP_NAME AS MODEL_CODE,(select t.dealer_shortname from tm_dealer t where t.dealer_id=a.APPLY_PERSON) as dealer_name,(select t.dealer_shortname from tm_dealer t where t.dealer_id=a.ERROR_DEALER_ID) as ERROR_DEALER_NAME\n" );
		sql.append("  FROM TM_VHCL_MATERIAL_GROUP C, TT_CUSTOMER B, TT_VEHICLE_CHANGE A\n" );
		sql.append(" WHERE A.CTM_ID = B.CTM_ID(+)\n" );
		sql.append("   AND A.MODEL_ID = C.GROUP_ID\n" );
		sql.append("   AND A.IS_DEL = ").append(Constant.STATUS_ENABLE).append("\n");
		sql.append("   AND A.STATUS <>"+Constant.VEHICLE_CHANGE_TYPE_04+"\n" );
		sql.append("   AND A.APPLY_TYPE IN('"+Constant.VEHICLE_CHANGE_TYPE_01+"','"+Constant.VEHICLE_CHANGE_TYPE_02+"','"+Constant.VEHICLE_CHANGE_TYPE_03+"','"+Constant.VEHICLE_CHANGE_TYPE_05+"')\n" );
		appendShowQuery(bean, sql);
		appendBeanQuery(bean, sql);
		sql.append("  ORDER BY a.create_date desc  ");//DECODE(A.UPDATE_DATE, NULL, A.CREATE_DATE, A.UPDATE_DATE) DESC
		PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
		return ps;
	}
	/**
	 * 
	* @Title: appendShowQuery 
	* @Description: TODO(页面上的查询条件) 
	* @throws
	 */
	private void appendShowQuery(TtVehicleChangeBean bean, StringBuffer sql) {
		if (Utility.testString(bean.getVin())) {//根据vin查询
			sql.append("   AND A.VIN LIKE '%").append(bean.getVin()).append("%'\n");
		}
		if (Utility.testString(bean.getVehicleNo())) {//车牌号查询
			sql.append("   AND A.VEHICLE_NO LIKE '%").append(bean.getVehicleNo()).append("%'\n");
		}
		if (null != bean.getApplyType()) {//根据申请类型查询
			sql.append("   AND A.APPLY_TYPE = ").append(bean.getApplyType()).append("\n");
		}
		if (null != bean.getStatus()) {//根据状态查询
			sql.append("   AND A.STATUS = ").append(bean.getStatus()).append("\n");
		}
		if (null != bean.getApplyPerson()) {//提报经销商查询
			sql.append("   AND A.APPLY_PERSON = ").append(bean.getApplyPerson()).append("\n");
		}
		if (null != bean.getErrorDealerId()) {//根据错误数据提报经销商查询
			sql.append("   AND A.ERROR_DEALER_ID = ").append(bean.getErrorDealerId()).append("\n");
		}
		if (Utility.testString(bean.getApplyStartDate())) {//提报开始日期
			sql.append("   AND A.APPLY_DATE >= TO_DATE('").append(bean.getApplyStartDate().trim().concat(" 00:00:00")).append("','YYYY-MM-DD HH24:MI:SS') \n");
		}
		if (Utility.testString(bean.getApplyEndDate())) {//提报结束日期
			sql.append("   AND A.APPLY_DATE <= TO_DATE('").append(bean.getApplyEndDate().trim().concat(" 23:59:59")).append("','YYYY-MM-DD HH24:MI:SS') \n");
		}
	}
	/**
	 * 
	* @Title: appendBeanQuery 
	* @Description: TODO(后台的一些逻辑查询条件) 
	* @return StringBuffer    返回类型 
	* @throws
	 */
	private void appendBeanQuery(TtVehicleChangeBean bean, StringBuffer sql) {
		if (Utility.testString(bean.getCheckQueryStatus())) {//车厂审核查询页面 过滤状态为已保存
			sql.append("   AND A.STATUS <> ").append(Constant.VEHICLE_CHANGE_STATUS_01).append("\n");
		}
		if (null != bean.getCreateBy()) {//根据创建者查询
			sql.append("   AND A.CREATE_BY = ").append(bean.getCreateBy()).append("\n");
		}
	}
	
	/**
	 * 
	* @Title: queryVehicleChangeInfoById 
	* @Description: TODO(根据ID查询车辆变更信息) 
	* @param @return    设定文件 
	* @return Map<String,Object>    返回类型 
	* @throws
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> queryVehicleChangeInfoById(Long id) {
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT A.ID,TO_CHAR(a.create_date,'yyyy-MM-dd') create_date,A.CTM_ID, A.VIN, A.ENGINE_NO, A.VEHICLE_NO, TO_CHAR(A.PURCHASED_DATE, 'YYYY-MM-DD') AS PURCHASED_DATE, A.MILEAGE,\n" );
		sql.append("       A.FREE_TIMES, A.APPLY_DATA, A.APPLY_REMARK, A.CHECK_REMARK, A.STATUS, A.ERROR_RO_CODE, A.RO_DEALER_CODE,\n" );
		sql.append("       A.RO_FREE_TIMES, A.C_FREE_TIMES, A.RO_MILEAGE, A.C_MILEAGE,A.C_CTM_NAME,A.C_CTM_PHONE,A.C_CTM_ADDRESS,a.old_ctm_name,a.old_ctm_phone,a.old_ctm_address,A.C_PURCHASE_DATE,\n" );
		sql.append("       B.CTM_NAME, B.MAIN_PHONE, B.ADDRESS,\n" );
		sql.append("       C1.GROUP_NAME AS SERIES_NAME, C2.GROUP_NAME AS MODEL_NAME,\n" );
		sql.append("       BA.AREA_NAME AS YIELDLY, D2.CODE_DESC AS APPLY_TYPE, D2.CODE_ID AS APPLY_ID, D3.CODE_DESC AS ERROR_TYPE, D3.CODE_ID AS ERROR_ID,\n" );
		sql.append("       E.GAME_CODE, F.DEALER_NAME, F.DEALER_CODE, G.RULE_CODE, F1.DEALER_NAME AS DN\n" );
		sql.append("  FROM TT_AS_WR_RULE G, TM_DEALER F, TM_DEALER F1, TT_AS_WR_GAME E,tm_business_area BA, TC_CODE D2, TC_CODE D3,\n" );
		sql.append("       TM_VHCL_MATERIAL_GROUP C1, TM_VHCL_MATERIAL_GROUP C2, TT_CUSTOMER B, TT_VEHICLE_CHANGE A\n" );
		sql.append(" WHERE A.CTM_ID = B.CTM_ID(+)\n" );
		sql.append("   AND A.SERIES_ID = C1.GROUP_ID\n" );
		sql.append("   AND A.MODEL_ID = C2.GROUP_ID\n" );
		sql.append("   AND A.YIELDLY = BA.AREA_ID\n" );
		sql.append("   AND A.APPLY_TYPE = D2.CODE_ID\n" );
		sql.append("   AND A.ERROR_TYPE = D3.CODE_ID(+)\n" );
		sql.append("   AND A.CLAIM_TACTICS_ID = E.ID(+)\n" );
		sql.append("   AND A.ERROR_DEALER_ID = F.DEALER_ID(+)\n" );
		sql.append("   AND E.RULE_ID = G.ID(+)\n" );
		sql.append("   AND A.RO_DEALER_CODE = F1.DEALER_CODE(+)\n");
		sql.append("   AND A.ID = ").append(id);
		Map<String, Object> ps = pageQueryMap(sql.toString(), null, getFunName());
		return ps;
	}
	
	/**
	 * 
	* @Title: queryRoByVin 
	* @Description: TODO(根据VIN查询工单表 行驶里程变更) 
	* @return PageResult<Map<String,Object>>    返回类型 
	* @throws
	 */
	@SuppressWarnings("unchecked")
	public PageResult<Map<String, Object>> queryRoByVin(String vin, String dealerId,int curPage, int pageSize) {
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT T.DEALER_CODE, T.RO_NO, T.REPAIR_TYPE_CODE, TO_CHAR(T.CREATE_DATE, 'YYYY-MM-dd hh24:mi') AS CREATE_DATE,\n" );
		sql.append("       T.IN_MILEAGE, T.FREE_TIMES, T.ORDER_VALUABLE_TYPE, D.DEALER_NAME\n" );
		sql.append("  FROM TT_AS_REPAIR_ORDER T, TM_DEALER D\n" );
		sql.append(" WHERE VIN = '").append(vin).append("'\n");
		sql.append("   AND T.Dealer_Id = D.DEALER_ID\n" );
		sql.append("and t.repair_type_code != "+Constant.REPAIR_TYPE_04+"\n"); 
		sql.append("and t.is_customer_in_asc=1\n"); 
		sql.append("and t.create_date >= to_date('2013-08-25 00:00:00','yyyy-mm-dd hh24:mi:ss')\n"); //排除老数据
		sql.append("and t.ro_status="+Constant.RO_STATUS_02+" \n"); 
		sql.append("and (t.dealer_id ="+dealerId+" or t.second_dealer_id="+dealerId+")"); 
		sql.append("and t.ro_no not in (\n");
		sql.append(" select a.ro_no from tt_as_wr_application a where  a.dealer_id = t.dealer_id and a.status !="+Constant.CLAIM_APPLY_ORD_TYPE_01+"\n");
		sql.append("    )"); 
		sql.append("ORDER BY CREATE_DATE DESC,IN_MILEAGE DESC");
		PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
		return ps;
	}
	/**
	 * 
	* @Title: queryRoByVin1 
	* @Description: TODO(根据VIN和保养次数查询工单表 保养次数变更,只查询工单类型为保养和服务活动，服务活动类型为免费保养) 
	* @return PageResult<Map<String,Object>>    返回类型 
	* @throws
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> queryRoByVinAndFreeTimes(String vin, int freeTimes) {
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT T.DEALER_CODE,T.RO_NO,T.REPAIR_TYPE_CODE,TO_CHAR(T.CREATE_DATE, 'YYYY-MM-dd hh24:mi') AS CREATE_DATE,\n" );
		sql.append("       T.IN_MILEAGE,T.FREE_TIMES,T.ORDER_VALUABLE_TYPE,D.DEALER_NAME\n" );
		sql.append("  FROM TT_AS_REPAIR_ORDER T  left join tt_as_wr_application a on a.ro_no = t.ro_no, TM_DEALER D\n" );
		sql.append(" WHERE (T.VIN = '").append(vin).append("')\n");
		sql.append("	and nvl(a.status,"+Constant.CLAIM_APPLY_ORD_TYPE_01+")="+Constant.CLAIM_APPLY_ORD_TYPE_01+" \n"); 
		sql.append("   and t.order_valuable_type !="+Constant.RO_PRO_STATUS_02+"\n");
		sql.append("   AND T.DEALER_CODE = D.DEALER_CODE\n");
		sql.append("AND t.create_date>=to_date('2013-08-26','yyyy-mm-dd')\n"); 
		sql.append(" and t.is_customer_in_asc=1\n"); 
		sql.append("AND t.ro_status="+Constant.RO_STATUS_02+"\n"); 

		sql.append("   AND T.FREE_TIMES = ").append(freeTimes).append("\n");
		sql.append("   AND (T.REPAIR_TYPE_CODE = " ).append(Constant.REPAIR_TYPE_04).append("\n");
		sql.append("        OR (T.REPAIR_TYPE_CODE = " ).append(Constant.REPAIR_TYPE_05).append("\n");
		sql.append("        /*只查询免费保养的服务活动*/\n" );
		sql.append("           AND T.RO_NO IN\n" );
		sql.append("       (SELECT A.RO_NO FROM TT_AS_REPAIR_ORDER A, TT_AS_ACTIVITY B\n" );
		sql.append("                   WHERE A.CAM_CODE = B.ACTIVITY_CODE\n" );
		sql.append("                  AND B.ACTIVITY_KIND = ").append(Constant.SERVICEACTIVITY_KIND_02).append(")))\n");
		sql.append(" ORDER BY CREATE_DATE DESC");
		Map<String, Object> ps = pageQueryMap(sql.toString(), null, getFunName());
		return ps;
	}
	
	/**
	 * 经销商申请查询通过VIN查询车辆PIN码要求查询
	 */
	@SuppressWarnings("unchecked")
	public PageResult<Map<String, Object>> queryVehiclePin(String pinCode, String vin, String dealerId,
			String pinCreateDate, String pinEndDate, String status, Integer curPage) {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT A.PIN_ID,\n");
		sql.append("       A.DEALER_ID,\n");
		sql.append("       A.PIN_CODE,\n");
		sql.append("       A.MAKE_DATE,\n");
		sql.append("       A.VIN,\n");
		sql.append("       A.REMARK,\n");
		sql.append("       A.BACK_REMARK,\n");
		sql.append("       A.STATUS,\n");
		sql.append("       A.CREATE_BY,\n");
		sql.append("       A.CREATE_DATE,\n");
		sql.append("       B.DEALER_CODE,\n");
		sql.append("       B.DEALER_SHORTNAME,\n");
		sql.append("       D.COMPANY_CODE,\n");
		sql.append("       D.COMPANY_SHORTNAME,\n");
		sql.append("       (SELECT NAME FROM TC_USER C WHERE C.USER_ID=A.CREATE_BY) AS USER_NAME\n");
		sql.append("  FROM TM_VEHICLE_PIN_REQUEST A, TM_DEALER B,TM_COMPANY D\n");
		sql.append(" WHERE A.DEALER_ID=B.DEALER_ID\n");
		sql.append(" AND B.COMPANY_ID=D.COMPANY_ID\n");
		sql.append(" AND A.DEALER_ID="+dealerId+"\n");
		sql.append(" AND B.DEALER_TYPE='"+Constant.DEALER_TYPE_DWR+"'\n");
		
		if(Utility.testString(pinCode)){
			sql.append(" AND A.PIN_CODE LIKE '%"+pinCode+"%' \n");
		}
		if(Utility.testString(vin)){
			sql.append(" AND A.VIN LIKE '%"+vin+"%' \n");
		}
		if(Utility.testString(pinCreateDate)){
			sql.append(" AND A.CREATE_DATE >= to_date('"+pinCreateDate+"', 'yyyy-mm-dd hh24:mi:ss') \n");
		}
		if(Utility.testString(pinEndDate)){
			sql.append(" AND A.CREATE_DATE <= to_date('"+pinEndDate+" 23:59:59', 'yyyy-mm-dd hh24:mi:ss') \n");
		}
		if(Utility.testString(status)){
			sql.append(" AND A.STATUS LIKE '%"+status+"%' \n");
		}
		sql.append(" ORDER BY A.CREATE_DATE DESC \n");
		PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null, getFunName(), Constant.PAGE_SIZE, curPage);
		return ps;
	}
	
	/**
	 * 添加车辆PIN申请
	 */
	@SuppressWarnings("unchecked")
	public String addVehiclePin(TmVehiclePinRequestPO pinReq) {
		
		this.insert(pinReq);
		return "1";
	}
	/**
	 * 修改车辆PIN申请
	 */
	@SuppressWarnings("unchecked")
	public int modifyVehiclePin(TmVehiclePinRequestPO pinReq,TmVehiclePinRequestPO pinReqValue) {
		
		return this.update(pinReq, pinReqValue);
	}
	/**
	 * 添加车辆PIN申请用户信息查询
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> addVehiclePinQueryUserInfo(long userId) {
		
//		StringBuffer sql = new StringBuffer();
//		sql.append("SELECT D.NAME,\n");
//		sql.append("       D.ACNT,\n");
//		sql.append("       D.USER_ID,\n");
//		sql.append("       F.COMPANY_CODE,\n");
//		sql.append("       F.COMPANY_NAME,\n");
//		sql.append("       F.COMPANY_ID\n");
//		sql.append("  FROM TC_USER D, TM_COMPANY F\n");
//		sql.append(" WHERE F.COMPANY_ID = D.COMPANY_ID\n");
//		sql.append("   AND D.USER_ID = "+userId+"\n");
		
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT E.NAME,E.ACNT,E.USER_ID,D.DEALER_CODE,D.DEALER_NAME,D.DEALER_ID\n");
		sql.append("  FROM TM_DEALER D, TC_USER E\n");
		sql.append(" WHERE D.COMPANY_ID = E.COMPANY_ID\n");
		sql.append("   AND E.USER_ID='"+userId+"'\n");
		sql.append("   AND D.DEALER_TYPE = '"+Constant.DEALER_TYPE_DWR+"'\n");

		Map<String, Object> ps = pageQueryMap(sql.toString(), null, getFunName());
		return ps;
	}
	
	/**
	 * 经销商添加车辆PIN申请信息查询
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> viewVehiclePinQuery(String pinId,String userId,String dealerId) {
		
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT A.NAME,A.ACNT,A.USER_ID,B.DEALER_CODE,B.DEALER_NAME,B.COMPANY_ID,\n");
		sql.append("C.PIN_ID,C.PIN_CODE,C.MAKE_DATE,C.VIN,C.REMARK,C.BACK_REMARK,C.CREATE_BY,C.STATUS,C.PIN\n");
		sql.append(" FROM TC_USER A,TM_DEALER B,TM_VEHICLE_PIN_REQUEST C\n");
		sql.append("WHERE A.COMPANY_ID=B.COMPANY_ID AND C.PIN_ID='"+pinId+"' AND A.USER_ID="+userId+" AND B.DEALER_ID='"+dealerId+"' AND B.DEALER_TYPE='"+Constant.DEALER_TYPE_DWR+"'");

		Map<String, Object> ps = pageQueryMap(sql.toString(), null, getFunName());
		return ps;
	}
	
	/**
	 * 
	* @Title: queryWrGame 
	* @Description: TODO(根据公司ID查询三包策略,包括当前策略和基础策略) 
	* @param @param companyId 公司ID
	* @return List<TtAsWrGamePO>    返回类型 
	 */
	public Map<String, Object> queryRuleByGameId(String changeId, Long companyId) {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT C.VIN,\n");
		sql.append("       C.ENGINE_NO,\n");
		sql.append("       C.ID AS VEHICLE_ID,\n");
		sql.append("       A.ID AS RULE_ID,\n");
		sql.append("       A.RULE_CODE,\n");
		sql.append("       A.RULE_NAME,\n");
		sql.append("       B.ID AS GAME_ID,\n");
		sql.append("       B.GAME_CODE,\n");
		sql.append("       B.GAME_NAME\n");
		sql.append("  FROM TT_AS_WR_RULE A, TT_AS_WR_GAME B, TT_VEHICLE_CHANGE C\n");
		sql.append(" WHERE A.RULE_STATUS = '10011001'\n");
		sql.append("   AND B.GAME_STATUS = '10011001'\n");
		sql.append("   AND B.RULE_ID = A.ID\n");
		sql.append("   AND C.CLAIM_TACTICS_ID = B.ID\n");
		sql.append("   AND C.ID = "+changeId+"\n");
		sql.append("   AND B.COMPANY_ID = " ).append(companyId);
		Map<String, Object> ps = pageQueryMap(sql.toString(), null, getFunName());
		return ps;
	}
	
	/**
	 * 
	* @Title: queryWrGame 
	* @Description: TODO(根据公司ID查询三包策略,包括当前策略和基础策略) 
	* @param @param companyId 公司ID
	* @return List<TtAsWrGamePO>    返回类型 
	 */
	public Map<String, Object> queryRuleByVin(String vin, Long companyId) {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT C.VIN,\n");
		sql.append("       C.ENGINE_NO,\n");
		sql.append("       C.ID AS VEHICLE_ID,\n");
		sql.append("       A.ID AS RULE_ID,\n");
		sql.append("       A.RULE_CODE,\n");
		sql.append("       A.RULE_NAME,\n");
		sql.append("       B.ID AS GAME_ID,\n");
		sql.append("       B.GAME_CODE,\n");
		sql.append("       B.GAME_NAME\n");
		sql.append("  FROM TT_AS_WR_RULE A, TT_AS_WR_GAME B, TT_VEHICLE_CHANGE C\n");
		sql.append(" WHERE A.RULE_STATUS = '10011001'\n");
		sql.append("   AND B.GAME_STATUS = '10011001'\n");
		sql.append("   AND B.RULE_ID = A.ID\n");
		sql.append("   AND C.CLAIM_TACTICS_ID = B.ID\n");
		sql.append("   AND C.VIN = "+vin+"\n");
		sql.append("   AND B.COMPANY_ID = " ).append(companyId);
		Map<String, Object> ps = pageQueryMap(sql.toString(), null, getFunName());
		return ps;
	}
	
	/**
	 * 
	* @Title: queryWrGame 
	* @Description: TODO(根据公司ID查询三包策略,包括当前策略和基础策略) 
	* @param @param companyId 公司ID
	* @return List<TtAsWrGamePO>    返回类型 
	 */
	public Map<String, Object> queryRuleListByRuleId(String ruleId) {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT B.PART_CODE,\n");
		sql.append("       B.PART_NAME,\n");
		sql.append("       B.CLAIM_MONTH,\n");
		sql.append("       B.CLAIM_MELIEAGE,\n");
		sql.append("       B.ID,\n");
		sql.append("  FROM TT_AS_WR_RULE_LIST B\n");
		sql.append(" WHERE \n");
		sql.append("   B.RULE_ID="+ruleId+"\n");

		Map<String, Object> ps = pageQueryMap(sql.toString(), null, getFunName());
		return ps;
	}
	
	/**
	 * 车辆三包里程时间变化申请查询
	* @throws
	 */
	@SuppressWarnings("unchecked")
	public PageResult<Map<String, Object>> queryVehicleRuleChangeInfo(String vin,String vehicleNo,String status,
			String beginDate,String endDate,String dealerId, int curPage, int pageSize) {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT A.ID,\n");
		sql.append("       A.VIN,\n");
		sql.append("       A.VEHICLE_NO,\n");
		sql.append("       TO_CHAR(A.PURCHASED_DATE, 'YYYY-MM-DD') AS PURCHASED_DATE,\n");
		sql.append("       A.MILEAGE,\n");
		sql.append("       A.FREE_TIMES,\n");
		sql.append("       A.APPLY_DATA,\n");
		sql.append("       A.APPLY_TYPE,\n");
		sql.append("       A.STATUS,\n");
		sql.append("       A.CLAIM_MONTH_NEW,\n");
		sql.append("       A.CLAIM_MELIEAGE_NEW,\n");
		sql.append("       TO_CHAR(A.CREATE_DATE, 'yyyy-MM-dd') AS CREATE_DATE,\n");
		sql.append("       TO_CHAR(A.CHECK_DATE, 'yyyy-MM-dd') AS CHECK_DATE,\n");
		//新加入原3包月份3包里程
		sql.append("       (SELECT D.CLAIM_MONTH FROM TT_AS_WR_RULE_LIST D WHERE D.ID=A.RULE_LIST_ID) AS CLAIM_MONTH,\n");
		sql.append("       (SELECT D.CLAIM_MELIEAGE FROM TT_AS_WR_RULE_LIST D WHERE D.ID=A.RULE_LIST_ID) AS CLAIM_MELIEAGE,\n");
		sql.append("       (SELECT D.PART_CODE FROM TT_AS_WR_RULE_LIST D WHERE D.ID=A.RULE_LIST_ID) AS PART_CODE,\n");
		sql.append("       (select ff.part_name from tm_pt_part_base ff where ff.part_code=(SELECT D.PART_CODE FROM TT_AS_WR_RULE_LIST D WHERE D.ID=A.RULE_LIST_ID) and ff.is_del=0) AS PART_NAME,\n");
		sql.append("       (SELECT D.DEALER_CODE FROM TM_DEALER D WHERE D.DEALER_ID=A.APPLY_PERSON) AS DEALER_CODE,\n");
		sql.append("       (SELECT D.DEALER_SHORTNAME FROM TM_DEALER D WHERE D.DEALER_ID=A.APPLY_PERSON) AS DEALER_SHORTNAME,\n");
		sql.append("       (SELECT D.PHONE FROM TM_DEALER D WHERE D.DEALER_ID=A.APPLY_PERSON) AS PHONE,\n");
		sql.append("       (SELECT D.NAME FROM TC_USER D WHERE D.USER_ID=A.CHECK_PERSON) AS CHECK_PERSON,\n");
		sql.append("       F.CTM_NAME,\n"); 
		sql.append("       E.GROUP_NAME AS MODEL_CODE,a.apply_date,(select mm.material_code from tm_vehicle vv,Tm_Vhcl_Material mm where vv.vin=a.vin and vv.material_id=mm.material_id) material_code,tt.product_date\n");
		sql.append("  FROM TT_VEHICLE_CHANGE      A,\n");
		sql.append("       TT_AS_WR_GAME          B,\n");
		sql.append("       TT_AS_WR_RULE          C,\n");
		sql.append("       TM_VHCL_MATERIAL_GROUP E,\n");
		sql.append("       TT_CUSTOMER            F,Tm_VEHICLE tt\n");
		sql.append(" WHERE tt.vin=a.vin and A.CLAIM_TACTICS_ID = B.ID\n");
		sql.append("   AND B.RULE_ID = C.ID\n");
		sql.append("   AND A.MODEL_ID = E.GROUP_ID(+)\n");
		sql.append("   AND A.CTM_ID = F.CTM_ID(+)\n");
		sql.append("   AND A.IS_DEL = "+Constant.STATUS_ENABLE+"\n");
		sql.append("   AND A.APPLY_TYPE = "+Constant.VEHICLE_CHANGE_TYPE_04+"\n");
		if(Utility.testString(dealerId)){
			
			sql.append("   AND A.CREATE_BY = "+dealerId+"\n");
		}
		if(Utility.testString(vin)){
			sql.append(" AND A.VIN LIKE '%"+vin+"%' \n");
		}
		if(Utility.testString(vehicleNo)){
			sql.append(" AND A.VEHICLE_NO LIKE '%"+vehicleNo+"%' \n");
		}
		if(Utility.testString(status)){
			sql.append(" AND A.STATUS = '"+status+"' \n");
		}
		if(Utility.testString(beginDate)){
			sql.append(" AND A.APPLY_DATE >= to_date('"+beginDate+"', 'yyyy-mm-dd hh24:mi:ss') \n");
		}
		if(Utility.testString(endDate)){
			sql.append(" AND A.APPLY_DATE <= to_date('"+endDate+" 23:59:59', 'yyyy-mm-dd hh24:mi:ss') \n");
		}

		sql.append("  ORDER BY a.apply_date asc");// A.CHECK_DATE,a.rowid DESC
		PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
		return ps;
	}
	
	
	/**
	 *  
	* @Title: queryWrGame 
	* @Description: TODO(根据公司ID查询三包策略,包括当前策略和基础策略) 
	* @param @param companyId 公司ID
	* @return List<TtAsWrGamePO>    返回类型 
	 */
	public Map<String, Object> queryVehicleRuleChangeAndRuleListDetailByChangeId(Long userId,String changeId) {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT A.ID AS CHANGE_ID,\n");
		sql.append("       A.VIN,\n");
		sql.append("       A.ENGINE_NO,\n");
		sql.append("       A.YIELDLY,\n");
		sql.append("       A.FREE_TIMES,\n");
		sql.append("       E.GROUP_NAME AS MODEL_NAME,\n");
		sql.append("       G.GROUP_NAME AS SERIES_NAME,\n");
		sql.append("       A.VEHICLE_NO,\n");
		sql.append("       TO_CHAR(A.PURCHASED_DATE, 'YYYY-MM-DD') AS PURCHASED_DATE,\n");
		sql.append("       A.MILEAGE,\n");
		sql.append("       A.FREE_TIMES,\n");
		sql.append("       A.APPLY_DATA,\n");
		sql.append("       (SELECT H.DEALER_SHORTNAME FROM TM_DEALER H WHERE H.DEALER_ID=A.APPLY_PERSON) AS APPLY_PERSON,\n");
		sql.append("       A.APPLY_DATE,\n");
		sql.append("       A.APPLY_TYPE,\n");
		sql.append("       A.STATUS,\n");
		sql.append("       A.CLAIM_MONTH_NEW,\n");
		sql.append("       A.CLAIM_MELIEAGE_NEW,\n");
		sql.append("       A.APPLY_REMARK,\n");
		sql.append("       A.CHECK_REMARK,\n");
		sql.append("       A.RULE_LIST_ID,\n");
		sql.append("       TO_CHAR(A.CREATE_DATE, 'yyyy-MM-dd') AS CREATE_DATE,\n");
		sql.append("       (SELECT D.CLAIM_MONTH FROM TT_AS_WR_RULE_LIST D WHERE D.ID=A.RULE_LIST_ID) AS CLAIM_MONTH,\n");
		sql.append("       (SELECT D.CLAIM_MELIEAGE FROM TT_AS_WR_RULE_LIST D WHERE D.ID=A.RULE_LIST_ID) AS CLAIM_MELIEAGE,\n");
		sql.append("       (SELECT D.PART_NAME FROM TT_AS_WR_RULE_LIST D WHERE D.ID=A.RULE_LIST_ID) AS PART_NAME,\n");
		sql.append("       (SELECT D.PART_CODE FROM TT_AS_WR_RULE_LIST D WHERE D.ID=A.RULE_LIST_ID) AS PART_CODE,\n");
		sql.append("       F.CTM_NAME,\n");
		sql.append("       F.MAIN_PHONE,\n");
		sql.append("       E.GROUP_NAME AS MODEL_CODE,\n");
		sql.append("       B.ID AS GAME_ID,\n");
		sql.append("       B.GAME_CODE,\n");
		sql.append("       B.GAME_NAME,\n");
		sql.append("       C.RULE_CODE,\n");
		sql.append("       (SELECT H.COMPANY_SHORTNAME FROM TM_COMPANY H,TM_DEALER I WHERE H.COMPANY_ID=I.COMPANY_ID AND I.DEALER_ID=A.APPLY_PERSON) AS COM_NAME\n");
		sql.append("  FROM TT_VEHICLE_CHANGE      A,\n");
		sql.append("       TT_AS_WR_GAME          B,\n");
		sql.append("       TT_AS_WR_RULE          C,\n");
		sql.append("       TM_VHCL_MATERIAL_GROUP E,\n");
		sql.append("       TM_VHCL_MATERIAL_GROUP G,\n");
		sql.append("       TT_CUSTOMER            F\n");
		sql.append(" WHERE A.CLAIM_TACTICS_ID = B.ID\n");
		sql.append("   AND B.RULE_ID = C.ID\n");
		sql.append("   AND A.MODEL_ID = E.GROUP_ID(+)\n");
		sql.append("   AND A.SERIES_ID = G.GROUP_ID(+)\n");
		sql.append("   AND A.CTM_ID = F.CTM_ID(+)\n");
		sql.append("   AND A.IS_DEL = "+Constant.STATUS_ENABLE+"\n");
		sql.append("   AND A.APPLY_TYPE = "+Constant.VEHICLE_CHANGE_TYPE_04+"\n");
		sql.append("   AND A.ID = "+changeId+"\n");
		if(userId!=0){
			sql.append("   AND A.CREATE_BY = "+userId+"\n");
		}

		Map<String, Object> ps = pageQueryMap(sql.toString(), null, getFunName());
		return ps;
	}
	
	/**
	 *  
	* @Title: queryWrGame 
	* @Description: TODO(通过VIN查询规则明细配件) 
	* @param @param companyId 公司ID
	* @return List<TtAsWrGamePO>    返回类型 
	 */
	public Map<String, Object> queryPartByVin(String vin,String changeId) {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT D.ID AS RULE_LIST_ID,\n");
		sql.append("       D.PART_CODE,\n");
		sql.append("       D.PART_NAME,\n");
		sql.append("       D.CLAIM_MONTH,\n");
		sql.append("       D.CLAIM_MELIEAGE,\n");
		sql.append("       E.CLAIM_MONTH_NEW,\n");
		sql.append("       E.CLAIM_MELIEAGE_NEW,\n");
		sql.append("       (SELECT F.DEALER_SHORTNAME FROM TM_DEALER F WHERE F.DEALER_ID=E.APPLY_PERSON) AS APPLY_PERSON,\n");
		sql.append("       E.APPLY_DATE,\n");
		sql.append("       E.APPLY_REMARK,\n");
		sql.append("       E.CHECK_REMARK,\n");
		sql.append("       E.ID AS CHANGE_ID,\n");
		sql.append("       (SELECT G.COMPANY_SHORTNAME FROM TM_COMPANY G,TM_DEALER H WHERE G.COMPANY_ID=H.COMPANY_ID AND H.DEALER_ID=E.APPLY_PERSON) AS COM_NAME\n");
		sql.append("  FROM TT_AS_WR_RULE C, TT_AS_WR_RULE_LIST D,TT_VEHICLE_CHANGE E\n");
		sql.append(" WHERE \n");
		sql.append("    C.ID = D.RULE_ID\n");
		sql.append("   AND E.RULE_LIST_ID = D.ID\n");
		sql.append("   AND E.ID = '"+changeId+"'\n");


		Map<String, Object> ps = pageQueryMap(sql.toString(), null, getFunName());
		return ps;
	}
	/**
	 * 
	* @Title: queryWrGame 
	* @Description: TODO(车辆配件通用三包规则查询) 
	* @param @param companyId 公司ID
	* @return List<TtAsWrGamePO>    返回类型 
	 */
	public Map<String, Object> queryVehicleUsualRuleChangeAndRuleListDetailByChangeId(String partCode) {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT D.CLAIM_MONTH,D.CLAIM_MELIEAGE,D.ID AS LIST_ID\n");
		sql.append("  FROM  TT_AS_WR_GAME B, TT_AS_WR_RULE C, TT_AS_WR_RULE_LIST D\n");
		sql.append(" WHERE B.RULE_ID = C.ID\n");
		sql.append("   AND C.ID = D.RULE_ID\n");
		sql.append("   AND C.RULE_CODE='20060101'\n");
		sql.append("   AND d.part_code='"+partCode+"'\n");



		Map<String, Object> ps = pageQueryMap(sql.toString(), null, getFunName());
		return ps;
	}
	/**
	 * 
	* @Title: queryWrGame 
	* @Description: TODO(查询所有车辆策略) 
	* @param @param companyId 公司ID
	* @return List<TtAsWrGamePO>    返回类型 
	 */
	public List queryVehicleGame() {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT A.ID AS GAME_ID, A.GAME_CODE, A.GAME_NAME FROM TT_AS_WR_GAME A WHERE 1=1\n");

		List listGame =  pageQuery(sql.toString(), null, getFunName());
		return listGame;
	}
	
	/**
	 * 
	* @Title: queryWrGame 
	* @Description: TODO(根据车辆变更申请ID查询策略) 
	* @param @param companyId 公司ID
	* @return List<TtAsWrGamePO>    返回类型 
	 */
	public List queryVehicleGameByChangeId(String changeId) {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT E.ID AS GAME_ID, E.GAME_CODE, E.GAME_NAME\n");
		sql.append("  FROM TT_AS_WR_GAME E,TT_AS_WR_RULE F\n");
		sql.append("  WHERE E.RULE_ID=F.ID AND F.RULE_CODE<>'"+Constant.COMMON_RULE+"'\n");
		
		List listGame =  pageQuery(sql.toString(), null, getFunName());
		return listGame;
	}
	
	/**
	 * 
	* @Title: queryWrGame 
	* @Description: TODO(根据策略查询规则明细) 
	* @param @param companyId 公司ID
	* @return List<TtAsWrGamePO>    返回类型 
	 */
	public Map<String,Object> queryVehicleRuleDetailByGameId(String gameId,String partCode) {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT C.PART_CODE,\n");
		sql.append("       C.PART_NAME,\n");
		sql.append("       C.CLAIM_MONTH,\n");
		sql.append("       C.CLAIM_MELIEAGE,\n");
		sql.append("       C.ID AS LISTID,\n");
		sql.append("       B.ID AS RULE_ID,\n");
		sql.append("       B.RULE_CODE,\n");
		sql.append("       B.RULE_NAME,\n");
		sql.append("       A.ID AS GAME_ID,\n");
		sql.append("       A.GAME_CODE,\n");
		sql.append("       A.GAME_NAME\n");
		sql.append("  FROM TT_AS_WR_GAME A, TT_AS_WR_RULE B, TT_AS_WR_RULE_LIST C\n");
		sql.append(" WHERE A.RULE_ID = B.ID\n");
		sql.append("   AND B.ID = C.RULE_ID\n");
		sql.append("   AND A.ID = "+gameId+"\n");
		sql.append("   AND C.PART_CODE = '"+partCode+"'\n");


		
		Map<String, Object> mapRuleDetail =  pageQueryMap(sql.toString(), null, getFunName());
		return mapRuleDetail;
	}
	/**
	 * 
	* @Title: queryWrGame 
	* @Description: TODO(根据策略ID查询是否是三包通用规则) 
	* @param @param companyId 公司ID
	* @return List<TtAsWrGamePO>    返回类型 
	 */
	public Map<String,Object> queryVehicleRuleCodeByGameId(String gameId) {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT A.ID,A.GAME_CODE,B.RULE_CODE FROM TT_AS_WR_GAME A,TT_AS_WR_RULE B\n");
		sql.append("WHERE A.RULE_ID=B.ID AND A.ID="+gameId+"\n");



		
		Map<String, Object> mapRuleDetail =  pageQueryMap(sql.toString(), null, getFunName());
		return mapRuleDetail;
	}
	
//	/**
//	 * 添加三包期里程变更申请
//	 * 
//	 */
//	@SuppressWarnings("unchecked")
//	public String applyRuleChange(TtVehicleChangePO change) {
//		
//		this.insert(change);
//		return "1";
//	}
	/**
	 * 更改三包期里程变更申请
	 * 
	 */
	@SuppressWarnings("unchecked")
	public int checkRuleChange(TtVehicleChangePO change,TtVehicleChangePO changeValue) {
		return this.update(change, changeValue);
	}
	
	
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		TtAsWrGamePO game = new TtAsWrGamePO();
		try {
			game.setId(rs.getLong("ID"));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return game;
	}

	public void myInsert(String sql){
		this.insert(sql);
	}
	
	///////////////////
	public PageResult<Map<String, Object>> queryOldChangeInfo(String dealerId,String changeTime,String changeTime1,String yieldly,String change_date,String change_review_date,String type,int curPage, int pageSize) {
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT A.ID,T.DEALER_CODE,T.DEALER_NAME,A.YIELDLY,TO_CHAR(A.CHANGE_TIME,'YYYY-MM-DD') AS CHANGE_TIME,U.NAME,TO_CHAR(A.CHANGE_DATE,'YYYY-MM-DD') AS CHANGE_DATE,TO_CHAR(CHANGE_REVIEW_DATE,'YYYY-MM-DD') AS CHANGE_REVIEW_DATE,A.STATUS FROM tt_as_changedate A,TM_DEALER T,TC_USER U WHERE A.DEALER_ID=T.DEALER_ID AND U.USER_ID=A.CHANGE_BY\n" );
		if(Utility.testString(dealerId)){
			sql.append(" AND A.DEALER_ID='"+dealerId+"'\n");
		}
		if(Utility.testString(changeTime)){
			sql.append(" AND A.CHANGE_TIME>=TO_DATE('"+changeTime+" 00:00:00','YYYY-MM-DD HH24:MI:SS')\n");
		}
		if(Utility.testString(changeTime1)){
			sql.append(" AND A.CHANGE_TIME<=TO_DATE('"+changeTime1+" 23:59:59','YYYY-MM-DD HH24:MI:SS')\n");
		}
		if(Utility.testString(yieldly)){
			sql.append(" AND A.YIELDLY LIKE '%"+yieldly+"%'\n");
		}
		if(Utility.testString(change_date)){
			sql.append(" AND A.CHANGE_DATE>=TO_DATE('"+change_date+" 00:00:00','YYYY-MM-DD HH24:MI:SS')\n");
		}
		if(Utility.testString(change_review_date)){
			sql.append(" AND A.CHANGE_REVIEW_DATE<=TO_DATE('"+change_review_date+" 23:59:59','YYYY-MM-DD HH24:MI:SS')\n");
		}
		if(Utility.testString(type)){
			sql.append(" AND A.status='"+type+"'\n");
		}
		System.out.println("sqlsql=="+sql.toString());
		PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
		return ps;
	}
	
	public Map<String , Object> queryChangeDateInfo(String id){
		StringBuffer sql = new StringBuffer();
		sql.append("select a.*,t.name from tt_as_changedate a,tc_user t where a.change_by=t.user_id and a.id="+id+"");
		Map<String , Object> ps = this.pageQueryMap(sql.toString(), null, this.getFunName());
		return ps;
	}
	
	/*
	 * 此方法主要判断目前两日期下，是否存在费用
	 */
	public Map<String , Object> checkFee(String beginDate,String endDate,String id,String yieldly){
		StringBuffer sql = new StringBuffer("\n");
		sql.append("select sum(nvl(money,0)) money\n");
		sql.append("  from (select sum(nvl(a.apply_repair_total, 0)) money\n");  
		sql.append("          from tt_as_wr_application a\n");  
		sql.append("         where a.account_date between\n");  //auditing_date把审核时间改为结算支付时间
		sql.append("               to_date('"+beginDate+" 00:00:00', 'yyyy-MM-dd HH24:mi:ss') and\n");  
		sql.append("               to_date('"+endDate+" 23:59:59', 'yyyy-MM-dd HH24:mi:ss')\n");  
		sql.append("           and a.status = "+Constant.CLAIM_APPLY_ORD_TYPE_07+"\n");  
		sql.append("           and a.yieldly = ").append(yieldly).append("\n") ;
		sql.append("           and not exists (select 1 from tr_balance_claim bd where bd.claim_id = a.id)\n");
		sql.append("           and a.dealer_id = "+id);
		sql.append("        union\n");  
		sql.append("        select sum(nvl(s.declare_sum, 0)) money\n");  
		sql.append("          from tt_as_wr_spefee s\n");  
		sql.append("         where ((s.fee_type = "+Constant.FEE_TYPE_02+" and s.status = "+Constant.SPEFEE_STATUS_04+" and\n");  
		sql.append("               s.update_date between\n");  
		sql.append("               to_date('"+beginDate+" 00:00:00', 'yyyy-MM-dd HH24:mi:ss') and\n");  
		sql.append("               to_date('"+endDate+" 23:59:59', 'yyyy-MM-dd HH24:mi:ss')) or\n");  
		sql.append("               (s.fee_type = "+Constant.FEE_TYPE_01+" and s.status = "+Constant.SPEFEE_STATUS_02+" and\n");  
		sql.append("               s.make_date between\n");  
		sql.append("               to_date('"+beginDate+" 00:00:00', 'yyyy-MM-dd HH24:mi:ss') and\n");  
		sql.append("               to_date('"+endDate+" 23:59:59', 'yyyy-MM-dd HH24:mi:ss')))\n");  
		sql.append("           and s.claimbalance_id is null\n");
		sql.append("           and s.yield = ").append(yieldly).append("\n") ;
		sql.append("           and s.dealer_id = "+id+")");
		Map<String , Object> ps = this.pageQueryMap(sql.toString(), null, this.getFunName());
		return ps ;
	}
	/*
	 * 此方法主要判断目前两日期下，是否存在费用
	 */
	public Map<String , Object> checkOld(String dealer_id,String yieldly,String beginDate,String endDate){
		StringBuffer sql = new StringBuffer("\n");
		/*sql.append("SELECT nvl(sum(nvl(p.BALANCE_QUANTITY,0)),0) money\n");
		sql.append("  FROM TT_AS_WR_APPLICATION A, TT_AS_WR_PARTSITEM P, TM_PT_PART_BASE PB\n");  
		sql.append(" WHERE 1 = 1\n");  
		sql.append("   AND A.ID = P.ID\n");  
		sql.append("   AND P.PART_CODE = PB.PART_CODE\n");  
		sql.append("   AND PB.IS_RETURN = 1\n");  
		sql.append("   AND A.YIELDLY = "+yieldly+"\n");  
		sql.append("   AND NVL(P.BALANCE_QUANTITY, 0) > NVL(P.RETURN_NUM, 0)\n");  
		sql.append("   AND A.STATUS IN ("+Constant.CLAIM_APPLY_ORD_TYPE_04+","+Constant.CLAIM_APPLY_ORD_TYPE_07+","+Constant.CLAIM_APPLY_ORD_TYPE_08+")\n");  
		sql.append("   AND A.DEALER_ID = "+dealer_id+"\n");  
		sql.append("   AND trunc(A.AUDITING_DATE) >= TO_DATE('"+beginDate+"', 'YYYY-MM-dd')\n");  
		sql.append("   AND trunc(A.AUDITING_DATE) < TO_DATE('"+endDate+"', 'YYYY-MM-dd')\n");*/
		sql.append("SELECT nvl(sum(nvl(p.BALANCE_QUANTITY,0)),0) money\n");
		sql.append("  FROM TT_AS_WR_APPLICATION A, TT_AS_WR_PARTSITEM P, TM_PT_PART_BASE PB\n");  
		sql.append(" WHERE 1 = 1\n");  
		sql.append("   AND A.ID = P.ID\n");  
		sql.append("   AND P.PART_CODE = PB.PART_CODE\n");  
		sql.append("   AND PB.IS_RETURN = 1\n");  
		sql.append("   AND A.YIELDLY = "+yieldly+"\n");  
		sql.append("   AND NVL(P.BALANCE_QUANTITY, 0) > NVL(P.RETURN_NUM, 0)\n");  
		sql.append("   AND A.STATUS = '"+Constant.CLAIM_APPLY_ORD_TYPE_07+"'\n");  
		sql.append("   AND A.DEALER_ID = "+dealer_id+"\n");  
		sql.append("   AND trunc(A.account_date) >= TO_DATE('"+beginDate+"', 'YYYY-MM-dd')\n");  
		sql.append("   AND trunc(A.account_date) < TO_DATE('"+endDate+"', 'YYYY-MM-dd')\n");
		Map<String , Object> ps = this.pageQueryMap(sql.toString(), null, this.getFunName());
		
		return ps ;
	}
	
	//区分轿车微车用(此方法微车用)
	public Map<String , Object> checkOld2(String dealer_id,String yieldly,String beginDate,String endDate){
		StringBuffer sql = new StringBuffer("\n");
		sql.append("SELECT nvl(sum(nvl(p.BALANCE_QUANTITY,0)),0) money\n");
		sql.append("  FROM TT_AS_WR_APPLICATION A, TT_AS_WR_PARTSITEM P, TM_PT_PART_BASE PB,tm_pt_part_type pt\n");  
		sql.append(" WHERE 1 = 1\n");  
		sql.append("   AND A.ID = P.ID and pt.id=pb.part_type_id\n");  
		sql.append("   AND P.PART_CODE = PB.PART_CODE\n");  
		sql.append("   AND pt.IS_RETURN = 1\n");  
		sql.append("   AND A.YIELDLY = "+yieldly+"\n");  
		sql.append("   AND NVL(P.BALANCE_QUANTITY, 0) > NVL(P.RETURN_NUM, 0)\n");  
		sql.append("   AND A.STATUS = "+Constant.CLAIM_APPLY_ORD_TYPE_07+"\n");  
		sql.append("   AND A.DEALER_ID = "+dealer_id+"\n");  
		sql.append("   AND trunc(A.account_date) >= TO_DATE('"+beginDate+"', 'YYYY-MM-dd')\n");  
		sql.append("   AND trunc(A.account_date) < TO_DATE('"+endDate+"', 'YYYY-MM-dd')\n");
		Map<String , Object> ps = this.pageQueryMap(sql.toString(), null, this.getFunName());
		return ps ;
	}
	
	//三包信息变更根据PART_CODE查询配件信息
	//查询配件详细信息
	public Map<String, Object> getPartDetailByPartCode(String partCode){
		StringBuffer sql = new StringBuffer("");
		sql.append("SELECT A.PART_ID,  A.PART_CODE, NVL(B.PART_CODE,'无') REPLACE_PART_ID,\n" );
		sql.append("       A.PART_NAME, A.PART_TYPE, A.UNIT,\n" );
		sql.append("       A.MINI_PACK, A.CHANGE_CODE, \n" );
		sql.append("       A.CAR_AMOUNT, A.STOCK_PRICE,\n" );
		sql.append("       A.SALE_PRICE, A.CUSTOMER_PRICE, A.CLAIM_PRICE,\n" );
		sql.append("       DECODE(A.STOP_FLAG,1,'是',0,'否') STOP_FLAG, A.REMARK, A.IS_RETURN,A.IS_NEW_PART\n" );
		sql.append("FROM TM_PT_PART_BASE A, TM_PT_PART_BASE B\n" );
		sql.append("WHERE A.REPLACE_PART_ID = B.PART_ID(+)\n" );
		sql.append("AND A.IS_DEL = ").append(Constant.IS_DEL_00).append("\n");
		sql.append("AND NVL(B.IS_DEL,").append(Constant.IS_DEL_00).append(")=").append(Constant.IS_DEL_00).append("\n");
		sql.append("AND A.PART_CODE = '").append(partCode+"'\n");
		Map<String, Object> map = pageQueryMap(sql.toString(), null, getFunName());
		return map;
	}
	//查询条码申请
	public PageResult<Map<String, Object>> queryBarCode(String vin,String status,String dealerId, int curPage, int pageSize) {
		StringBuffer sql= new StringBuffer();
		sql.append("select a.ID, D.DEALER_CODE,a.dealer_shortname DEALER_NAME,a.sign_status,a.sign_name,a.sign_date,A.VIN,TO_CHAR(A.APPLY_DATE,'YYYY-MM-DD') APP_DATE,\n");
		sql.append("A.AUDIT_ACOUNT,A.APPLY_STATUS,U.NAME AUDIT_NAME,TO_CHAR(A.AUDIT_DATE,'YYYY-MM-DD') AUDIT_TIME,\n");
		sql.append("U1.NAME PRINT_NAME,TO_CHAR(A.PRINT_DATE,'YYYY-MM-DD') PRINT_TIME\n");
		sql.append("from tt_as_barcode_apply a\n");
		sql.append("LEFT JOIN TC_USER U ON A.Audit_By = U.USER_ID\n");
		sql.append("LEFT JOIN  TC_USER U1 ON U1.USER_ID = A.PRINT_BY,\n");
		sql.append("TM_DEALER D\n");
		sql.append("WHERE D.DEALER_ID = A.APPLY_BY\n");
		sql.append(" AND D.DEALER_ID ="+dealerId+"\n");
		if(Utility.testString(vin)){
			sql.append("AND A.VIN like '%"+vin+"%'\n");
		}
		if(Utility.testString(status)){
			sql.append("AND A.APPLY_STATUS="+status); 
		}
		sql.append(" order by a.apply_date desc");
		PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
		return ps;
	}
	public PageResult<Map<String, Object>> queryBarCode2(String sDate,String eDate,String vin,String status,String dealerId,String name,AclUserBean logonUser, int curPage, int pageSize) {
		return this.queryBarCode2(sDate, eDate, vin, status, dealerId, name, logonUser, curPage, pageSize,null,null,null,null,null,null);
	}
	//查询条码申请
	@SuppressWarnings({ "unchecked", "static-access" })
	public PageResult<Map<String, Object>> queryBarCode2(String sDate,String eDate,String vin,String status,String dealerId,String name,AclUserBean logonUser, int curPage, int pageSize,String signSDate,String signEDate,String signName,String signStatus,String groupName,String chapterCode) {
		StringBuffer sql= new StringBuffer();
		sql.append("select a.ID, a.EXPRESS_MAIL,a.sign_name,a.sign_date,a.sign_status,D.DEALER_CODE,D.dealer_shortname DEALER_NAME,a.PRINT_TIMES,A.VIN,TO_CHAR(A.APPLY_DATE,'YYYY-MM-DD') APP_DATE,\n");
		sql.append("A.AUDIT_ACOUNT,A.APPLY_STATUS,U.NAME AUDIT_NAME,TO_CHAR(A.AUDIT_DATE,'YYYY-MM-DD') AUDIT_TIME,\n");
		sql.append("U1.NAME PRINT_NAME,TO_CHAR(A.PRINT_DATE,'YYYY-MM-DD') PRINT_TIME,c.code_desc\n");
		sql.append("from tt_as_barcode_apply a\n");
		sql.append("LEFT JOIN TC_USER U ON A.Audit_By = U.USER_ID\n");
		sql.append("left join tc_code c on c.code_id = a.apply_status\n"); 
		sql.append("LEFT JOIN  TC_USER U1 ON U1.USER_ID = A.PRINT_BY \n");
		sql.append("LEFT JOIN  tm_vehicle tv ON a.vin = tv.vin \n");
		sql.append("LEFT JOIN  TM_VHCL_MATERIAL_GROUP tvm ON tvm.group_id = tv.model_id,\n");
		sql.append("TM_DEALER D\n");
		sql.append("WHERE D.DEALER_ID = A.APPLY_BY\n");
		sql.append(" AND A.APPLY_STATUS !="+Constant.BARCODE_APPLY_STATUS_01+"\n");
		if(Utility.testString(sDate)){
			sql.append("AND A.PRINT_DATE >= to_date('"+sDate+"','yyyy-mm-dd')\n");
		}
		if(Utility.testString(eDate)){
			sql.append("AND A.PRINT_DATE <= to_date('"+eDate+"','yyyy-mm-dd')+1\n");
		}
		if(Utility.testString(signSDate)){
			sql.append("AND A.SIGN_DATE >= to_date('"+signSDate+"','yyyy-mm-dd')\n");
		}
		if(Utility.testString(signEDate)){
			sql.append("AND A.SIGN_DATE <= to_date('"+signEDate+"','yyyy-mm-dd')+1\n");
		}
		if(Utility.testString(vin)){
			sql.append("AND A.VIN like '%"+vin+"%'\n");
		}
		if(Utility.testString(signName)){
			sql.append("AND A.SIGN_NAME like '%"+signName+"%'\n");
		}
		if(Utility.testString(status)){
			sql.append("AND A.APPLY_STATUS="+status); 
		}
		if(Utility.testString(signStatus)){
			sql.append("AND A.SIGN_STATUS="+signStatus); 
		}
		if(Utility.testString(dealerId)){
			sql.append("AND D.DEALER_ID in ("+dealerId+")\n"); 
		}
		if(Utility.testString(name)){
			sql.append("AND d.dealer_name like '%"+name+"%'\n");
		}
		if(Utility.testString(groupName)){
			sql.append("AND tvm.group_name like '%"+groupName+"%'\n");
		}
		if(Utility.testString(chapterCode)){
			sql.append("AND a.chapter_code like '%"+chapterCode+"%'\n");
		}
		sql.append(dao1.getOrgDealerLimitSqlByService("d", logonUser));
		sql.append(" order by a.print_date desc ");
		PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
		return ps;
	}
	@SuppressWarnings("unchecked")
	public List<FsFileuploadPO> queryAttById(String id) {
		StringBuffer sql = new StringBuffer();
		List<FsFileuploadPO> ls = new ArrayList<FsFileuploadPO>();
		sql.append(" SELECT A.* FROM FS_FILEUPLOAD A ");
		// sql.append(" LEFT OUTER JOIN FS_FILEUPLOAD B ON B.YWZJ = A.ID ");
		sql.append(" WHERE 1=1 ");
		sql.append(" AND A.YWZJ='" + id + "'");
		ls = select(FsFileuploadPO.class, sql.toString(), null);
		return ls;
	}
	//三包凭证补办申请,查询车辆信息
	public Map<String, Object> queryVehicleInfoByVin2(String vin) {
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT A.VIN, A.ENGINE_NO, TO_CHAR(A.PURCHASED_DATE, 'YYYY-MM-DD') AS PURCHASED_DATE, nvl(A.MILEAGE,0) mileage, nvl(A.FREE_TIMES,0) free_times, A.SERIES_ID, A.MODEL_ID,\n" );
		sql.append("       a.license_no VEHICLE_NO, ba.area_name AS YIELDLY, ba.area_id AS Y_ID, D1.GROUP_NAME AS SERIES_CODE, D2.GROUP_NAME AS MODEL_CODE,\n" );
		sql.append("       E.CTM_NAME, E.MAIN_PHONE, b.car_charactor ,c.code_desc , E.CTM_ID, E.ADDRESS, F.GAME_CODE, F.ID AS GAME_ID, G.RULE_CODE\n" );
		sql.append("  FROM TM_VEHICLE A,   tc_code c ,TT_DEALER_ACTUAL_SALES B, tm_business_area ba, TM_VHCL_MATERIAL_GROUP D1, TM_VHCL_MATERIAL_GROUP D2,\n" );
		sql.append("       TT_CUSTOMER E, TT_AS_WR_GAME F, TT_AS_WR_RULE G\n" );
		sql.append(" WHERE A.VIN = '" ).append(vin).append("'\n");
		sql.append("   AND A.VEHICLE_ID = B.VEHICLE_ID(+) and  b.car_charactor=c.code_id(+)\n" );
		sql.append("   AND A.YIELDLY = ba.area_id(+)\n" );
		sql.append("   AND A.SERIES_ID = D1.GROUP_ID(+)\n" );
		sql.append("   AND A.MODEL_ID = D2.GROUP_ID(+)\n" );
		sql.append("   AND A.CLAIM_TACTICS_ID = F.ID(+)\n" );
		sql.append("   AND B.CTM_ID = E.CTM_ID(+)\n" );
		sql.append("   AND F.RULE_ID = G.ID(+)");
		Map<String, Object> ps = pageQueryMap(sql.toString(), null, getFunName());
		return ps;
	}
	//查询是否是售前车
	public Map<String, Object> checkVin(String vin) {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT  *\n");
		sql.append("FROM TM_VEHICLE A, TT_DEALER_ACTUAL_SALES B,   TT_CUSTOMER E\n");
		sql.append(" WHERE A.VIN = '"+vin+"'\n");
		sql.append("   AND A.VEHICLE_ID = B.VEHICLE_ID\n");
		sql.append("   AND B.CTM_ID = E.CTM_ID"); 

		Map<String, Object> ps = pageQueryMap(sql.toString(), null, getFunName());
		return ps;
	}
	public PageResult<Map<String, Object>> queryPackgeApply(String vin,String status,String dealerId, int curPage, int pageSize) {
		StringBuffer sql= new StringBuffer();
		sql.append("select  a.id,a.vin, to_char(a.apply_date,'yyyy-mm-dd hh24:mi') app_date, a.apply_status,d.dealer_code,d.dealer_shortname dealer_name,a.audit_acount\n");
		sql.append("from  tt_as_packge_change_apply a ,tm_dealer d\n");
		sql.append("where d.dealer_id = a.apply_by"); 
		sql.append(" AND D.DEALER_ID ="+dealerId+"\n");
		if(Utility.testString(vin)){
			sql.append("AND A.VIN like '%"+vin+"%'\n");
		}
		if(Utility.testString(status)){
			sql.append("AND A.APPLY_STATUS="+status); 
		}
		
		PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
		return ps;
	}
	public PageResult<Map<String, Object>> queryPackgeApply2(String vin,String status,Long orgId,String dealerId,String dealerName, int curPage, int pageSize) {
		StringBuffer sql= new StringBuffer();
		sql.append("select  a.id,a.vin, to_char(a.apply_date,'yyyy-mm-dd hh24:mi') app_date, a.apply_status,d.dealer_code,d.dealer_shortname dealer_name,a.audit_acount\n");
		sql.append("from  tt_as_packge_change_apply a ,tm_dealer d\n");
		sql.append("where d.dealer_id = a.apply_by\n");
		sql.append("and d.dealer_id in (\n");
		sql.append("   select r.dealer_id from tm_dealer_org_relation r, (select org_id from tm_org o where o.org_level in (2,3) start with  o.org_id=2013061719376134  connect by prior  o.org_id = o.parent_org_id) xo\n");
		sql.append("where xo.org_id = r.org_id\n");
		if(Utility.testString(dealerId)){
			sql.append("and r.dealer_id in ("+dealerId+")\n");
		}
		sql.append(")"); 
		sql.append(" and a.APPLY_STATUS in("+Constant.PACKGE_CHANGE_STATUS_02+","+Constant.PACKGE_CHANGE_STATUS_03+","+Constant.PACKGE_CHANGE_STATUS_04+")\n");
		if(Utility.testString(vin)){
			sql.append("AND A.VIN like '%"+vin+"%'\n");
		}
		if(Utility.testString(status)){
			sql.append("AND A.APPLY_STATUS="+status); 
		}
		if(Utility.testString(dealerName)){
			sql.append("AND d.dealer_name like '%"+dealerName+"%'\n");
		}
		PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
		return ps;
	}
	public PageResult<Map<String, Object>> queryPackgeApply3(String vin,String status,String dealerId,String dealerName, int curPage, int pageSize) {
		StringBuffer sql= new StringBuffer();
		sql.append("select  a.id,a.vin, to_char(a.apply_date,'yyyy-mm-dd hh24:mi') app_date,  to_char(a.print_date, 'yyyy-mm-dd hh24:mi') print_time,  a.print_times,   u.name, a.apply_status,d.dealer_code,d.dealer_shortname dealer_name,a.audit_acount\n");
		sql.append("from  tt_as_packge_change_apply a ,tc_user u,tm_dealer d\n");
		sql.append("where d.dealer_id = a.apply_by  and a.print_by = u.user_id(+)\n");
		
		if(Utility.testString(dealerId)){
			sql.append("and d.dealer_id in ("+dealerId+")\n");
		}
		sql.append(" and a.APPLY_STATUS in("+Constant.PACKGE_CHANGE_STATUS_04+","+Constant.PACKGE_CHANGE_STATUS_05+","+Constant.PACKGE_CHANGE_STATUS_06+","+Constant.PACKGE_CHANGE_STATUS_02+","+Constant.PACKGE_CHANGE_STATUS_03+")\n");
		if(Utility.testString(vin)){
			sql.append("AND A.VIN like '%"+vin+"%'\n");
		}
		if(Utility.testString(status)){
			sql.append("AND A.APPLY_STATUS="+status); 
		}
		if(Utility.testString(dealerName)){
			sql.append("AND d.dealer_name like '%"+dealerName+"%'\n");
		}
		PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
		return ps;
	}
	//三包凭证打印
	public List<TtAsPackgeChangeDetailBean> getPrintDetail(String vin){
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT V.vin,   vw.model_code , to_char(v.purchased_date,'yyyy-mm-dd') sale_date,  d.dealer_name,\n");
		sql.append("      d.zip_code,d.address,d.phone,to_char(v.product_date,'yyyy-mm-dd') product_Dates\n");
		sql.append("  FROM TM_VEHICLE V\n");
		sql.append("  LEFT OUTER JOIN VW_MATERIAL_GROUP_service vw ON vw.package_id =\n");
		sql.append("                                                  v.package_id\n");
		sql.append("  LEFT OUTER JOIN TT_DEALER_ACTUAL_SALES A ON V.VEHICLE_ID = A.VEHICLE_ID\n");
		sql.append("  left join tm_dealer d on d.dealer_id = a.dealer_id\n");
		sql.append(" WHERE 1 = 1\n");
		sql.append("   and v.VIN = '"+vin+"'"); 

		return this.select(TtAsPackgeChangeDetailBean.class, sql.toString(), null);
	}
	//查询审核明细
	public List<TtAsPackgeChangeDetailBean> getAuditDetail(String id){
		StringBuffer sql = new StringBuffer();
		sql.append("select d.audit_remark,d.audit_status,d.audit_acount,to_char(d.audit_date,'yyyy-mm-dd hh24:mi') audit_time,u.name audit_name\n");
		sql.append("from tt_as_packge_change_detail d\n");
		sql.append(" left join tc_user u on u.user_id = d.audit_person\n");
		sql.append(" where 1=1\n");
		sql.append(" and d.apply_id="+id); 
		sql.append(" order by d.audit_date desc");
		return this.select(TtAsPackgeChangeDetailBean.class, sql.toString(), null);
	}
	public PageResult<Map<String, Object>> getDetail(String dealerName,String dealerId,String startDate,String endDate, int curPage, int pageSize) {
		StringBuffer sql= new StringBuffer();
		sql.append("select d.dealer_code,d.dealer_shortname DEALER_NAME,t.vin , to_char(t.create_date,'yyyy-mm-dd hh24:mi') search_date,t.pin_no\n");
		sql.append("from tt_as_pin_search_detail t,tm_dealer d\n");
		sql.append("where 1=1 and t.dealer_id  = d.dealer_id\n"); 

		if(Utility.testString(dealerName)){
			sql.append(" and d.dealer_name like '%"+dealerName+"%'\n");
		}
		if(Utility.testString(dealerId)){
			sql.append("and d.dealer_id in ("+dealerId+")\n");
		}
		if(Utility.testString(startDate)){
			sql.append(" and to_char(t.create_date,'yyyy-mm-dd')>='"+startDate+"'\n");
		}
		if(Utility.testString(endDate)){
			sql.append(" and to_char(t.create_date,'yyyy-mm-dd')<='"+endDate+"'\n"); 
		}
		sql.append(" order by t.create_date desc"); 
		PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
		return ps;
	}
	public List<TtAsBarcodeApplyBean> getList(String ids){
		StringBuffer sql = new StringBuffer();
		sql.append("select t.*,d.dealer_code, tc.ctm_name own_name from Tt_As_Barcode_Apply t\n");
		sql.append("left join tm_dealer d  on t.apply_by = d.dealer_id,\n");
		sql.append("tm_vehicle v  left join tt_dealer_actual_sales a on a.vehicle_id=v.vehicle_id  and a.is_return="+Constant.IF_TYPE_NO+"\n");
		sql.append("left join tt_customer tc on tc.ctm_id=a.ctm_id\n");
		sql.append("where 1=1\n");
		sql.append("and v.vin = t.vin\n");
		sql.append("and t.id in ("+ids+")"); 
		return this.select(TtAsBarcodeApplyBean.class, sql.toString(), null);
	}
	//如果是保养次数变更,则根据错误的工单号将里程变更为系统上次进厂维修时的里程
	public Double getMileage(String roNo,String vin){
		StringBuffer sql = new StringBuffer();
		sql.append("select  max(o.in_mileage)in_mileage  from tt_as_repair_order o\n");
		sql.append("where o.vin='"+vin+"'\n");
		sql.append("and o.ro_no not in('"+roNo+"')"); 
		sql.append(" and o.order_valuable_type != "+Constant.RO_PRO_STATUS_02+"\n");
		List<TtAsRepairOrderPO>  list = this.select(TtAsRepairOrderPO.class, sql.toString(), null);
		if(list!=null && list.size()>0){
			System.out.println(list.get(0).getInMileage());
			return list.get(0).getInMileage();
		}else return -1.0;
	}
	
}
