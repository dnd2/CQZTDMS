/**********************************************************************
* <pre>
* FILE : ServiceActivityManageIssuedDao.java
* CLASS : ServiceActivityManageIssuedDao
*
* AUTHOR : PGM
*
* FUNCTION :  服务活动管理---服务活动计划下发
*
*
*======================================================================
* CHANGE HISTORY LOG
*----------------------------------------------------------------------
* MOD. NO.| DATE     | NAME | REASON | CHANGE REQ.
*----------------------------------------------------------------------
*         |2010-05-17| PGM  | Created |
* DESCRIPTION:
* </pre>
***********************************************************************/
/**
 * $Id: ServiceActivityManageIssuedDao.java,v 1.4 2011/02/24 06:11:58 zuoxj Exp $
 */
package com.infodms.dms.dao.claim.serviceActivity;
import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.TtAsActivityBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.po.TtAsActivityAgePO;
import com.infodms.dms.po.TtAsActivityCharactorPO;
import com.infodms.dms.po.TtAsActivityDealerPO;
import com.infodms.dms.po.TtAsActivityMgroupPO;
import com.infodms.dms.po.TtAsActivityPO;
import com.infodms.dms.po.TtAsActivityVehiclePO;
import com.infodms.dms.po.TtAsActivityYieldlyPO;
import com.infodms.dms.util.StringUtil;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;
/**
 * Function       :  服务活动管理---服务活动计划下发
 * @author        :  PGM
 * CreateDate     :  2010-06-10
 * @version       :  0.1
 */
@SuppressWarnings("unchecked")
public class ServiceActivityManageIssuedDao extends BaseDao{
	public static Logger logger = Logger.getLogger(ServiceActivityManageIssuedDao.class);
	private static final ServiceActivityManageIssuedDao dao = new ServiceActivityManageIssuedDao ();
	public  static final ServiceActivityManageIssuedDao getInstance() {
		return dao;
	}
	/**
	 * Function         : 服务活动管理---查询活动编码
	 * @return          : 满足条件的服务活动管理信息[活动编码]
	 * @throws          : 
	 * LastUpdate       : 2010-06-10
	 */
	public List serviceActivityCode(){
		StringBuffer sql = new StringBuffer();
		sql.append("select * from tt_as_activity where STATUS="+Constant.SERVICEACTIVITY_STATUS_02+" and IS_DEL="+Constant.IS_DEL_00+" \n");//已经发布
		sql.append("order by activity_code desc  \n");
        List list = dao.select(TtAsActivityPO.class, sql.toString(), null);
        return list;
	}
	/**
	 * Function         : 服务活动管理---服务活动计划下发
	 * @param           : 活动编号
	 * @param           : 活动开始日期
	 * @param           : 活动结束日期
	 * @param           : 当前页码
	 * @param           : 每页显示记录数
	 * @return          : 满足条件的服务活动管理信息，包含分页信息
	 * @throws          : Exception
	 * LastUpdate       : 服务活动管理
	 */
	public  PageResult<Map<String, Object>>  getServiceActivityManageIssuedQuery(TtAsActivityBean ActivityBean, int curPage,int pageSize) throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append("	SELECT ACTIVITY_CODE , ACTIVITY_NAME ,to_char(RELEASEDATE,'yyyy-MM-dd')as RELEASEDATE, STATUS , to_char(STARTDATE,'yyyy-MM-dd')as STARTDATE ,ACTIVITY_ID,ACTIVITY_TYPE    \n");
		sql.append("     FROM TT_AS_ACTIVITY \n");
		sql.append("   WHERE  STATUS in( "+Constant.SERVICEACTIVITY_STATUS_01+","+Constant.SERVICEACTIVITY_STATUS_02+") and IS_DEL="+Constant.IS_DEL_00+" \n");//服务活动管理--服务活动活动状态:[10681001:尚未发布;10681002:已经发布;10681003:重新发布]
		//sql.append("  AND  IF_STATUS=2  \n");//IF_STATUS=2 服务活动再次下发的时候，查询接口状态为2的字段，置成1
		if(!"".equals(ActivityBean.getCompanyId())&&!(null==ActivityBean.getCompanyId())){//CompanyId不为空
			sql.append("		AND  COMPANY_ID ='"+ActivityBean.getCompanyId()+"' \n");
		}
		if(!"".equals(ActivityBean.getActivityCode())&&null!=ActivityBean.getActivityCode()){//活动编号不为空
			sql.append("		AND UPPER(ACTIVITY_CODE) like UPPER('%"+ActivityBean.getActivityCode()+"%')\n");
		}
		if(StringUtil.notNull(ActivityBean.getActivityName()))
			sql.append("        AND ACTIVITY_NAME LIKE '%").append(ActivityBean.getActivityName()).append("%'\n");
		if(!"".equals(ActivityBean.getStartdate())&&null!=ActivityBean.getStartdate()){      //活动开始日期不为空
			sql.append("		AND STARTDATE >=to_date('"+ActivityBean.getStartdate()+" 00:00:00', 'yyyy-MM-dd HH24:mi:ss') \n");
		}
		if(!"".equals(ActivityBean.getEnddate())&&null!=ActivityBean.getEnddate()){         //活动结束日期不为空
			sql.append("	    AND ENDDATE  <= to_date('"+ActivityBean.getEnddate()+" 23:59:59', 'yyyy-MM-dd HH24:mi:ss') \n");
		}
		if(!"".equals(ActivityBean.getStatus())&&!(null==ActivityBean.getStatus())&&!"1".equals(ActivityBean.getStatus())){ //活动状态
			sql.append("	    AND STATUS = '"+ActivityBean.getStatus()+"' \n");
		}
		sql.append("            ORDER BY ACTIVITY_ID desc  \n");
		PageResult<Map<String, Object>> ps = (PageResult<Map<String, Object>>) pageQuery(sql.toString(), null,  getFunName(), pageSize, curPage);
		return ps;
	}

	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}
	/**
	 * Function         : 服务管理活动信息
	 * @param           : 活动ID
	 * @return          : 根据活动ID查询出具体 服务管理活动信息
	 * @throws          : Exception
	 * LastUpdate       : 2010-06-10
	 */
	public  TtAsActivityBean  getServiceActivityByActivityIdInfo(String activityId){
		List<Object> params = new LinkedList();
		StringBuilder sql = new StringBuilder();
		sql.append("  SELECT STATUS,max_car,single_car_num,  COMPANY_ID,  CREATE_DATE, UPDATE_BY,  CREATE_By, UPDATE_DATE,  CLAIM_TYPE,   \n");
		sql.append("  ACTIVITY_CODE, ACTIVITY_TYPE, to_char(STARTDATE,'yyyy-MM-dd')as STARTDATE,to_char(ENDDATE,'yyyy-MM-dd')as ENDDATE , ACTIVITY_ID, ACTIVITY_KIND,   \n");
		sql.append("  ACTIVITY_NAME, WORKTIME_FEE,  PART_FEE,  DEALWITH,  UPLOAD_PRE_PERIOD,   \n");
		sql.append("  SOLUTION, CLAIM_GUIDE,  IS_CLAIM,  IS_FIXFEE, CAR_NUM,  RELEASEDATE, OTHER_FEE,ACTIVITY_FEE   \n");
		sql.append("  FROM TT_AS_ACTIVITY \n");
		sql.append("  WHERE  1=1 ");
		if (activityId!=null&&!("").equals(activityId)){
		sql.append(" AND ACTIVITY_ID = ?  \n");
		params.add(activityId);
		}
		TtAsActivityBean ActivityBean=new TtAsActivityBean();
		List list = dao.select(TtAsActivityBean.class, sql.toString(), params);
		if (list!=null){
			if (list.size()>0) {
				ActivityBean = (TtAsActivityBean) list.get(0);
			}
		}
		
		return ActivityBean;
	}
	/**
	 * Function       :  服务管理活动信息---工时查询WorkingHours
	 * @param         :  request-活动ID
	 * @return        :  服务管理活动信息
	 * @throws        :  Exception
	 * LastUpdate     :  2010-06-10
	 */
	public  List<TtAsActivityBean>  getWorkingHoursInfoList(String activityId){
		List<Object> params = new LinkedList();
		StringBuilder sql = new StringBuilder();
		sql.append("  SELECT UPDATE_DATE,  CREATE_DATE, CREATE_BY, UPDATE_BY, ITEM_CODE, ITEM_ID, ACTIVITY_ID, ITEM_NAME, LABOR_FEE,  NORMAL_LABOR,  STAT_TITLE   \n");
		sql.append("  FROM TT_AS_ACTIVITY_REPAIRITEM \n");
		if (activityId!=null&&!("").equals(activityId)){
		sql.append("  WHERE  ACTIVITY_ID = ? ");
		params.add(activityId);
		}
		sql.append("order by ACTIVITY_ID desc ");
		List list = dao.select(TtAsActivityBean.class, sql.toString(), params);
        return list;
	}
	/**
	 * Function       :  服务管理活动信息---活动配件
	 * @param         :  request-活动ID
	 * @return        :  服务管理活动信息
	 * @throws        :  Exception
	 * LastUpdate     :  2010-06-10
	 */
	public  List<TtAsActivityBean>  getPartsList(String activityId){
		List<Object> params = new LinkedList();
		StringBuilder sql = new StringBuilder();
		sql.append("  SELECT UPDATE_DATE,  CREATE_DATE, CREATE_BY, UPDATE_BY, PART_NO, PART_NAME, IS_MAIN_PART, PART_AMOUNT, ACTIVITY_ID, PART_PRICE, PART_QUANTITY, PART_UNIT, PARTS_ID   \n");
		sql.append("  FROM TT_AS_ACTIVITY_PARTS \n");
		if (activityId!=null&&!("").equals(activityId)){
		sql.append("  WHERE  ACTIVITY_ID = ? \n");
		params.add(activityId);
		}
		sql.append("order by ACTIVITY_ID desc ");
		List list = dao.select(TtAsActivityBean.class, sql.toString(), params);
        return list;
	}
	/**
	 * Function       :  服务管理活动信息---活动其它项目
	 * @param         :  request-活动ID
	 * @return        :  服务管理活动信息
	 * @throws        :  Exception
	 * LastUpdate     :  2010-06-10
	 */
	public  List<TtAsActivityBean>  getNetItemList(String activityId){
		List<Object> params = new LinkedList();
		StringBuilder sql = new StringBuilder();
		sql.append("  select ID,  UPDATE_DATE, REMARK,  CREATE_DATE, CREATE_BY, UPDATE_BY, ITEM_CODE AS ITEM_CODES, ITEM_DESC,  AMOUNT, ACTIVITY_ID   \n");
		sql.append("  FROM TT_AS_ACTIVITY_NETITEM \n");
		if (activityId!=null&&!("").equals(activityId)){
		sql.append("  WHERE  ACTIVITY_ID = ? \n ");
		params.add(activityId);
		}
		sql.append("  ORDER BY ACTIVITY_ID desc \n");
		List list = dao.select(TtAsActivityBean.class, sql.toString(), params);
        return list;
	}
	/**
	 * Function       :  服务管理活动信息---车型列表
	 * @param         :  request-活动ID
	 * @return        :  服务管理活动信息
	 * @throws        :  Exception
	 * LastUpdate     :  2010-06-10
	 */
	public  List<TtAsActivityBean>  getVhclMaterialGroupList(String activityId){
		List<Object> params = new LinkedList();
		StringBuilder sql = new StringBuilder();
		sql.append("  SELECT MATERIAL_GROUP.GROUP_CODE, MATERIAL_GROUP.GROUP_NAME   \n");
		sql.append("  FROM TT_AS_ACTIVITY_MGROUP ACTIVITY_GROUP,Tm_Vhcl_Material_Group   MATERIAL_GROUP \n");
		if (activityId!=null&&!("").equals(activityId)){
		sql.append("  WHERE Material_Group.GROUP_ID = ACTIVITY_GROUP.MATERIAL_GROUP_ID AND Material_Group.GROUP_LEVEL = 3 AND  ACTIVITY_GROUP.ACTIVITY_ID = ? \n");
		params.add(activityId);
		}
		sql.append(" ORDER BY ACTIVITY_GROUP.ACTIVITY_ID desc \n");
		List list = dao.select(TtAsActivityBean.class, sql.toString(), params);
        return list;
	}
	/**
	 * Function       :  服务管理活动信息---车辆性质
	 * @param         :  request-活动ID
	 * @return        :  服务管理活动信息
	 * @throws        :  Exception
	 * LastUpdate     :  2010-06-10
	 */
	public  List<TtAsActivityBean>  getActivityCharactorList(String activityId){
		List<Object> params = new LinkedList();
		StringBuilder sql = new StringBuilder();
		sql.append("	SELECT ID,  CREATE_DATE, UPDATE_BY,  CREATE_BY, UPDATE_DATE, ACTIVITY_ID, CAR_CHARACTOR  \n");
		sql.append("    FROM TT_AS_ACTIVITY_CHARACTOR \n");
		if(!"".equals(activityId)&&!(null==activityId)){//活动ID
			sql.append(" WHERE ACTIVITY_ID = ? \n");
			params.add(activityId);
		}
		sql.append("     ORDER BY ACTIVITY_ID DESC  \n");
		List list = dao.select(TtAsActivityBean.class, sql.toString(), params);
        return list;
	}
	/**
	 * Function       :  服务管理活动信息---车龄定义列表
	 * @param         :  request-活动ID
	 * @return        :  服务管理活动信息
	 * @throws        :  Exception
	 * LastUpdate     :  2010-06-10
	 */
	public  List<TtAsActivityBean>  getActivityAgeList(String activityId){
		List<Object> params = new LinkedList();
		StringBuilder sql = new StringBuilder();
		sql.append("  SELECT ID as ID_AGE, UPDATE_DATE, CREATE_DATE,  CREATE_BY,  UPDATE_BY, ACTIVITY_ID,  to_char(SALE_DATE_END,'yyyy-MM-dd')as SALE_DATE_END,  to_char(SALE_DATE_START,'yyyy-MM-dd')as SALE_DATE_START, CUSTOMER_TYPE   \n");
		sql.append("  FROM TT_AS_ACTIVITY_AGE \n");
		if (activityId!=null&&!("").equals(activityId)){
		sql.append("  WHERE ACTIVITY_ID = ?  \n");
		params.add(activityId);
		}
		sql.append(" ORDER BY ACTIVITY_ID desc \n");
		List list = dao.select(TtAsActivityBean.class, sql.toString(), params);
        return list;
	}
	/**
	 * Function       :  服务管理活动信息---车辆信息
	 * @param         :  request-活动ID
	 * @return        :  服务管理活动信息
	 * @throws        :  Exception
	 * LastUpdate     :  2010-06-10
	 */
	public  PageResult<Map<String, Object>>  getActivityVehicleList(String activityId, int curPage,int pageSize) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append("  SELECT VIN, CUSTOMER_NAME,LINKMAN_MOBILE,CUSTOMER_ADDRESS, POSTAL_CODE, LINKMAN,   \n");
		sql.append("  LINKMAN_ZONE_NUM, LINKMAN_OFFICE_PHONE, LINKMAN_FAMILY_PHONE, EMAIL,PROVINCE,AREA,TOWN,LINCENSE_TAG \n");
		sql.append("  FROM TT_AS_ACTIVITY_VEHICLE \n");
		if (activityId!=null&&!("").equals(activityId)){
			sql.append("  WHERE ACTIVITY_ID = ' ");
			sql.append(activityId);
			sql.append("' \n");
			sql.append(" ORDER BY VIN desc \n");
			}
		PageResult<Map<String, Object>> ps = (PageResult<Map<String, Object>>) pageQuery(sql.toString(), null,  getFunName(), pageSize, curPage);
		return ps;
	}
	/**
	 * Function       :  修改服务活动计划下发-首次下发状态为:已经下发
	 * @param         :  request-活动ID
	 * @return        :  服务活动计划下发
	 * @throws        :  Exception
	 * LastUpdate     :  2010-06-10
	 */
	public static void  ServiceActivityManageIssuedUpdateStatus(String activityId,TtAsActivityPO ActivityPO){
		TtAsActivityPO ActivityPOc=new TtAsActivityPO();//条件
		ActivityPOc.setActivityId(Long.parseLong(activityId));
		dao.update(ActivityPOc, ActivityPO);
	}
	/**
	 * Function       :  修改服务活动计划下发-修改车辆状态为：已经下发
	 * @param         :  request-活动ID
	 * @return        :  服务活动计划下发
	 * @throws        :  Exception
	 * LastUpdate     :  2010-06-10
	 */
	public static void  ServiceActivityManageIssuedUpdateCarStatus(TtAsActivityVehiclePO VehiclePo,TtAsActivityVehiclePO VehiclePoContent){
		dao.update(VehiclePo, VehiclePoContent);
	}
	/**
	 * Function       :  修改服务管理活动信息
	 * @param         :  request-活动编号、活动名称、活动类型、活动类别、活动开始日期、活动结束日期、处理方式、距活动结束日期几天上传活动总结
	 * @param         :          配件费用、工时费用、索赔、索赔是否为固定费用、解决方案说明、索赔申请填写指导
	 * @return        :  服务管理活动信息
	 * @throws        :  Exception
	 * LastUpdate     :  2010-06-01
	 */
	public static void  serviceActivityManageUpdate(TtAsActivityPO ActivityPOCon,TtAsActivityPO ActivityPO){
		dao.update(ActivityPOCon, ActivityPO);
	}
	/**
	 * Function       :  修改车辆状态为：已经下发
	 * @param         :  request-活动ID
	 * @return        :  服务活动计划下发
	 * @throws        :  Exception
	 * LastUpdate     :  2010-06-09
	 */
	public static void  serviceActivityManageVehicleCarStatus(TtAsActivityVehiclePO VehiclePo,TtAsActivityVehiclePO VehiclePoContent){
		dao.update(VehiclePo, VehiclePoContent);
	}
	/**
	 * Function       :  服务管理活动信息---判断经销商是否存在；描述：活动执行经销商情况：1.存在，可以下发；2.不存在，不可以下发；
	 * @param         :  request-活动ID
	 * @return        :  服务管理活动信息
	 * @throws        :  Exception
	 * LastUpdate     :  2010-06-10
	 */
	public  List<TtAsActivityDealerPO>  dealerList(String activityId){
		List<Object> params = new LinkedList();
		StringBuilder sql = new StringBuilder();
		sql.append("  select Status, Dealer_Code,  Dealer_Id, Create_Date, Update_By, Create_By, Update_Date,  Dealer_Name, Activity_Id   \n");
		sql.append("   from Tt_As_Activity_Dealer WHERE 1=1 \n");
		if (activityId!=null&&!("").equals(activityId)){
		sql.append("   and ACTIVITY_ID = ?  \n");
		params.add(activityId);
		}
		sql.append(" ORDER BY ACTIVITY_ID desc \n");
		List list = dao.select(TtAsActivityDealerPO.class, sql.toString(), params);
        return list;
	}
	/**
	 * Function       :  服务管理活动信息---判断车龄是否存在；描述：活动车龄情况：1.存在，可以下发；2.不存在，不可以下发；
	 * @param         :  request-活动ID
	 * @return        :  服务管理活动信息
	 * @throws        :  Exception
	 * LastUpdate     :  2010-06-10
	 */
	public  List<TtAsActivityAgePO>  ageList(String activityId){
		List<Object> params = new LinkedList();
		StringBuilder sql = new StringBuilder();
		sql.append("   select Id, Create_Date, Update_By, Create_By,  Update_Date, Activity_Id, Sale_Date_End, Sale_Date_Start, Customer_Type   \n");
		sql.append("   from Tt_As_Activity_Age WHERE 1=1 \n");
		if (activityId!=null&&!("").equals(activityId)){
		sql.append("   and ACTIVITY_ID = ?  \n");
		params.add(activityId);
		}
		sql.append(" ORDER BY ACTIVITY_ID desc \n");
		List list = dao.select(TtAsActivityAgePO.class, sql.toString(), params);
        return list;
	}
	/**
	 * Function       :  服务管理活动信息---判断车型是否存在；描述：活动车型情况：1.存在，可以下发；2.不存在，不可以下发；
	 * @param         :  request-活动ID
	 * @return        :  服务管理活动信息
	 * @throws        :  Exception
	 * LastUpdate     :  2010-06-10
	 */
	public  List<TtAsActivityMgroupPO>  mgroupList(String activityId){
		List<Object> params = new LinkedList();
		StringBuilder sql = new StringBuilder();
		sql.append("    select Id, Create_Date, Update_By,  Create_By,  Update_Date,  Material_Group_Id, Activity_Id   \n");
		sql.append("    from   Tt_As_Activity_Mgroup WHERE 1=1 \n");
		if (activityId!=null&&!("").equals(activityId)){
		sql.append("   and ACTIVITY_ID = ?  \n");
		params.add(activityId);
		}
		sql.append(" ORDER BY ACTIVITY_ID desc \n");
		List list = dao.select(TtAsActivityMgroupPO.class, sql.toString(), params);
        return list;
	}
	/**
	 * Function       :  服务管理活动信息---判断车辆性质是否存在；描述：活动车辆性质情况：1.存在，可以下发；2.不存在，不可以下发；
	 * @param         :  request-活动ID
	 * @return        :  服务管理活动信息
	 * @throws        :  Exception
	 * LastUpdate     :  2010-06-10
	 */
	public  List<TtAsActivityCharactorPO>  charactorList(String activityId){
		List<Object> params = new LinkedList();
		StringBuilder sql = new StringBuilder();
		sql.append("    select Id,  Create_Date, Update_By,  Create_By, Update_Date, Activity_Id,  Car_Charactor  \n");
		sql.append("     from Tt_As_Activity_Charactor WHERE 1=1 \n");
		if (activityId!=null&&!("").equals(activityId)){
		sql.append("   and ACTIVITY_ID = ?  \n");
		params.add(activityId);
		}
		sql.append(" ORDER BY ACTIVITY_ID desc \n");
		List list = dao.select(TtAsActivityCharactorPO.class, sql.toString(), params);
        return list;
	}
	/**
	 * Function       :  服务管理活动信息---判断生产基地是否存在；描述：活动车辆性质情况：1.存在，可以下发；2.不存在，不可以下发；
	 * @param         :  request-活动ID
	 * @return        :  服务管理活动信息
	 * @throws        :  Exception
	 * LastUpdate     :  2010-06-10
	 */
	public  List<TtAsActivityYieldlyPO>  yieldlyList(String activityId){
		List<Object> params = new LinkedList();
		StringBuilder sql = new StringBuilder();
		sql.append("    select Id,  Create_Date, Update_By,  Create_By, Update_Date, Activity_Id,  Car_yieldly  \n");
		sql.append("     from Tt_As_Activity_yieldly WHERE 1=1 \n");
		if (activityId!=null&&!("").equals(activityId)){
		sql.append("   and ACTIVITY_ID = ?  \n");
		params.add(activityId);
		}
		sql.append(" ORDER BY ACTIVITY_ID desc \n");
		List list = dao.select(TtAsActivityYieldlyPO.class, sql.toString(), params);
        return list;
	}
	/**
	 * Function       :  服务管理活动信息---判断VIN(车辆)是否存在；描述：活动VIN(车辆)情况：1.存在，可以下发；2.不存在，不可以下发；
	 * @param         :  request-活动ID
	 * @return        :  服务管理活动信息
	 * @throws        :  Exception
	 * LastUpdate     :  2010-06-10
	 */
	public  List<TtAsActivityVehiclePO>  vehicleList(String activityId){
		List<Object> params = new LinkedList();
		StringBuilder sql = new StringBuilder();
		sql.append("     select Status,  Id,   Create_Date,  Update_By, Email,  Create_By, Update_Date, Province, Buy_Date,   \n");
		sql.append("     Vin, Memo, Report_Date,   Activity_Id, Car_Status,  Area, Customer_Address,  \n");
		sql.append("     Customer_Name,  Havingcustomer,  Lincense_Tag,  Linkman,  Linkman_Family_Phone, Linkman_Mobile,  Linkman_Office_Phone,  \n");
		sql.append("     Linkman_Zone_Num,  Postal_Code,  Town, Is_Send_Result, Repair_Status,  Sale_Status, Repair_Date,  Operate_Dealer_Code, \n");
		sql.append("     Is_Import,  Repair_Kilometre, Bill_No, Issendout  \n");
		sql.append("     from Tt_As_Activity_Vehicle  WHERE 1=1  \n");
		if (activityId!=null&&!("").equals(activityId)){
		sql.append("   and ACTIVITY_ID = ?  \n");
		params.add(activityId);
		}
		sql.append(" ORDER BY ACTIVITY_ID desc \n");
		List list = dao.select(TtAsActivityVehiclePO.class, sql.toString(), params);
        return list;
	}
	/**
	 * Function       :  新增之后---查询服务管理活动信息---售前车
	 * @param         :  request-活动ID
	 * @return        :  服务管理活动信息
	 * @throws        :  Exception
	 * LastUpdate     :  2010-06-01
	 */
	public  TtAsActivityBean  serviceActivityVehicleBeforeVehicleArea(){
		List<Object> params = new LinkedList();
		StringBuilder sql = new StringBuilder();
		sql.append("  SELECT CODES.CODE_ID,CODES.CODE_DESC   \n");
		sql.append("  FROM TC_CODE CODES \n");
		sql.append("  WHERE CODES.CODE_ID = "+Constant.SERVICEACTIVITY_VEHICLE_AREA_01);//服务活动车辆范围代码:售前车
		sql.append(" ORDER BY CODES.CODE_ID desc \n");
		TtAsActivityBean ActivityBean=new TtAsActivityBean();
		List list = dao.select(TtAsActivityBean.class, sql.toString(), params);
		if (list!=null){
			if (list.size()>0) {
				ActivityBean = (TtAsActivityBean) list.get(0);
			}
		}
		return ActivityBean;
	}
	/**
	 * Function       :  新增之后---查询服务管理活动信息---售后车
	 * @param         :  request-活动ID
	 * @return        :  服务管理活动信息
	 * @throws        :  Exception
	 * LastUpdate     :  2010-06-01
	 */
	public  TtAsActivityBean  serviceActivityVehicleAfterVehicleArea(){
		List<Object> params = new LinkedList();
		StringBuilder sql = new StringBuilder();
		sql.append("  SELECT CODES.CODE_ID,CODES.CODE_DESC   \n");
		sql.append("  FROM TC_CODE CODES \n");
		sql.append("  WHERE CODES.CODE_ID = "+Constant.SERVICEACTIVITY_VEHICLE_AREA_02);//服务活动车辆范围代码:售后车
		sql.append(" ORDER BY CODES.CODE_ID desc \n");
		TtAsActivityBean ActivityBean=new TtAsActivityBean();
		List list = dao.select(TtAsActivityBean.class, sql.toString(), params);
		if (list!=null){
			if (list.size()>0) {
				ActivityBean = (TtAsActivityBean) list.get(0);
			}
		}
		return ActivityBean;
	}
	/**
	 * Function       :  新增之后---查询服务管理活动信息---查询业务表中的服务活动车辆范围
	 * @param         :  request-活动ID
	 * @return        :  服务管理活动信息
	 * @throws        :  Exception
	 * LastUpdate     :  2010-06-01
	 */
	public  TtAsActivityBean  serviceActivityVehicleArea(String activityId){
		List<Object> params = new LinkedList();
		StringBuilder sql = new StringBuilder();
		sql.append("  SELECT ACTIVITY_ID, VEHICLE_AREA   \n");
		sql.append("  FROM TT_AS_ACTIVITY \n");
		sql.append("  WHERE 1=1   \n");//服务活动车辆范围代码:售前车
		if(!"".equals(activityId)&&null!=activityId){
			sql.append("  AND  ACTIVITY_ID="+activityId);//服务活动车辆范围代码:售前车
		}
		sql.append(" ORDER BY ACTIVITY_ID desc \n");
		TtAsActivityBean ActivityBean=new TtAsActivityBean();
		List list = dao.select(TtAsActivityBean.class, sql.toString(), params);
		if (list!=null){
			if (list.size()>0) {
				ActivityBean = (TtAsActivityBean) list.get(0);
			}
		}
		return ActivityBean;
	}
	/** 
	 * Function       :  服务活动管理---首次下发时，向【TT_AS_ACTIVITY_DOWNLOG】中插入全部dealerId,全量下发经销商
	 * @param         :  activityId,
	 * @return        :  serviceActivityVinImportVehicle
	 * @throws        :  Exception
	 * LastUpdate     :  2010-07-22
	 */
	public  void  serviceActivityDealerIdInsert(String activityId,Long createBy,Long updateBy){
		StringBuffer sql = new StringBuffer();
		sql.append("\n" );
		sql.append(" INSERT into TT_AS_ACTIVITY_DOWNLOG(ID,ACTIVITY_ID,DEALER_ID,UPDATE_BY,UPDATE_DATE,CREATE_BY,CREATE_DATE) \n" );
		sql.append(" select f_getid,"+activityId+",b.DEALER_ID,"+createBy+",sysdate,"+updateBy+",sysdate from  TM_DEALER_ORG_RELATION a, TM_DEALER b, TM_ORG c \n" );
		sql.append(" where a.DEALER_ID = b.DEALER_ID and b.DEALER_TYPE=10771002  and c.ORG_ID = a.ORG_ID");
	    dao.update(sql.toString(), null);
     	}
	}
