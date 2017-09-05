/**********************************************************************
 * <pre>
 * FILE : ServiceActivityManageDao.java
 * CLASS : ServiceActivityManageDao
 *
 * AUTHOR : PGM
 *
 * FUNCTION : 服务活动管理.
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
 * $Id: ServiceActivityManageDao.java,v 1.12 2011/02/15 09:40:53 zuoxj Exp $
 */
package com.infodms.dms.dao.claim.serviceActivity;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.TtAsActivityBean;
import com.infodms.dms.bean.ttAsActivitySubjectBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TcUserPO;
import com.infodms.dms.po.TtAsActivityCharactorPO;
import com.infodms.dms.po.TtAsActivityMilagePO;
import com.infodms.dms.po.TtAsActivityPO;
import com.infodms.dms.po.TtAsActivityProjectPO;
import com.infodms.dms.po.TtAsActivitySubjectPO;
import com.infodms.dms.po.TtAsSubjietNumPO;
import com.infodms.dms.po.TtAsWrLabouritemRaplcePO;
import com.infodms.dms.po.TtAsWrPartsitemRaplcePO;
import com.infodms.dms.util.StringUtil;
import com.infodms.yxdms.utils.DaoFactory;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;
import com.infoservice.po3.core.context.DBService;
import com.infoservice.po3.core.context.POContext;

/**
 * Function : 服务活动管理
 * 
 * @author : PGM CreateDate : 2010-06-01
 * @version : 0.1
 */
@SuppressWarnings("unchecked")
public class ServiceActivityManageDao extends BaseDao {
	public static Logger logger = Logger
			.getLogger(ServiceActivityManageDao.class);
	private static final ServiceActivityManageDao dao = new ServiceActivityManageDao();

	public static final ServiceActivityManageDao getInstance() {
		return dao;
	}

	/**
	 * Function : 服务活动管理
	 * 
	 * @param : 活动编号
	 * @param : 活动开始日期
	 * @param : 活动结束日期
	 * @param : 当前页码
	 * @param : 每页显示记录数
	 * @return : 满足条件的服务活动管理信息，包含分页信息
	 * @throws : Exception LastUpdate : 服务活动管理
	 */
	public PageResult<Map<String, Object>> getAllServiceActivityManageInfo(
			TtAsActivityBean ActivityBean, int curPage, int pageSize)
			throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append("	SELECT A.ACTIVITY_ID,A.ACTIVITY_NAME, A.ACTIVITY_CODE, A.STATUS,B.SUBJECT_NO,B.SUBJECT_ID,B.SUBJECT_NAME  FROM TT_AS_ACTIVITY A  \n");
		sql.append("    , TT_AS_ACTIVITY_SUBJECT B  where  A.SUBJECT_ID = B.SUBJECT_ID   \n");
		sql.append("    and  1=1 AND A.IS_DEL = 0 ");// 服务活动管理--服务活动活动状态:[10681001:尚未发布;10681002:已经发布;10681003:重新发布]
		/*if(ActivityBean.getActivityFee().equals("0"))
		{
			sql.append(" AND B.ACTIVITY_TYPE = 10561001 ");
		}else
		{
			sql.append(" AND B. ACTIVITY_TYPE != 10561001 ");
		}*/
		if (!"".equals(ActivityBean.getActivityCode())
				&& !(null == ActivityBean.getActivityCode())) {// 活动编号不为空
			sql.append("		AND UPPER(A.ACTIVITY_CODE) like UPPER('%"
					+ ActivityBean.getActivityCode() + "%')\n");
		}
		if (!"".equals(ActivityBean.getActivityName())
				&& !(null == ActivityBean.getActivityName())) {// 活动编号不为空
			sql.append("		AND UPPER(A.ACTIVITY_NAME) like UPPER('%"
					+ ActivityBean.getActivityName() + "%')\n");
		}
		if (!"".equals(ActivityBean.getSubjectId())
				&& !(null == ActivityBean.getSubjectId())) { // 主题名称
			sql.append("		AND A.SUBJECT_ID=" + ActivityBean.getSubjectId()
					+ "\n");
		}
		if (!"".equals(ActivityBean.getStatus())
				&& !(null == ActivityBean.getStatus())) { // 活动类型
			sql.append("		AND A.STATUS=" + ActivityBean.getStatus() + "\n");
		}
		sql.append("            ORDER BY trim(A.ACTIVITY_CODE) desc  \n");
		PageResult<Map<String, Object>> ps = (PageResult<Map<String, Object>>) pageQuery(
				sql.toString(), null, getFunName(), pageSize, curPage);
		return ps;
	}
   public String getNum(String subjectId)
   {
	   String  num = "";
	   TtAsSubjietNumPO numPO = new TtAsSubjietNumPO();
	   List<TtAsSubjietNumPO> list= dao.select(numPO);
	   if(list.size() == 0)
	   {
		   num = "001";
		   numPO.setNum(2l);
		   dao.insert(numPO);
		   
	   }else
	   {
		   num = ""+list.get(0).getNum();
		   for(int i = num.length() ;i< 3;i++)
		   {
			   num = "0" +num;
		   }
		   dao.delete(numPO);
		   numPO.setNum(list.get(0).getNum() + 1l);
		   dao.insert(numPO);
	   }
	   return num;
   }
	
	
	public PageResult<Map<String, Object>> getServiceActivityInfo(String con,
			int pageSize, int curPage) {
		StringBuffer sql = new StringBuffer("\n");
		sql.append("select activity_id,activity_code,activity_name,status,\n");
		sql.append("to_char(startdate,'yyyy-MM-dd') as startdate,\n");
		sql.append("to_char(enddate,'yyyy-MM-dd') as enddate\n");
		sql.append("from tt_as_activity where 1=1\n");
		sql.append(con).append("order by activity_code\n");
		return (PageResult<Map<String, Object>>) pageQuery(sql.toString(),
				null, getFunName(), pageSize, curPage);
	}

	public PageResult<Map<String, Object>> getTtAsActivityBeanInfo(
			TtAsActivityBean ttAsActivityBean, int pageSize, int curPage) {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT min(y.ACTIVITY_ID) AS activityId,min(y.vehicle_num) AS vehicle_num,min(y.status) AS status, COUNT(o.vin) AS fsumcar, min(y.activity_code) AS activityCode, min(y.activity_name) AS activityName,to_char(min(y.startdate),'yyyy-mm-dd') AS startdate,to_char(min(y.enddate),'yyyy-mm-dd') as enddate \n");
		sql.append(" FROM tt_as_activity y left join tt_as_repair_order o on y.activity_code = o.cam_code  AND o.RO_STATUS = 11591002   where 1=1 AND y.IS_DEL=0 AND (y.STATUS = 10681002 or y.STATUS=10681004) \n");
		if (!"".equals(ttAsActivityBean.getActivityCode())
				&& !(null == ttAsActivityBean.getActivityCode())) {// 活动编号不为空
			sql.append("		AND UPPER(y.ACTIVITY_CODE) like UPPER('%"
					+ ttAsActivityBean.getActivityCode() + "%')\n");
		}
		if (!"".equals(ttAsActivityBean.getActivityName())
				&& !(null == ttAsActivityBean.getActivityName())) {// 活动编号不为空
			sql.append("		AND UPPER(y.ACTIVITY_NAME) like UPPER('%"
					+ ttAsActivityBean.getActivityName() + "%')\n");
		}
		if (!"".equals(ttAsActivityBean.getSubjectId())
				&& !(null == ttAsActivityBean.getSubjectId())) {// 活动编号不为空
			sql.append("		AND y.SUBJECT_ID=" + ttAsActivityBean.getSubjectId()
					+ "\n");
		}
		if (!"".equals(ttAsActivityBean.getStatus())
				&& !(null == ttAsActivityBean.getStatus())) {// 活动编号不为空
			sql.append("		AND y.STATUS=" + ttAsActivityBean.getStatus() + "\n");
		}
		if (!"".equals(ttAsActivityBean.getStartdate())
				&& !(null == ttAsActivityBean.getStartdate())) { // 活动开始日期不为空
			sql.append(" AND y.STARTDATE >=to_date('"
					+ ttAsActivityBean.getStartdate() + "', 'yyyy-MM-dd') \n");
		}
		if (!"".equals(ttAsActivityBean.getEnddate())
				&& !(null == ttAsActivityBean.getEnddate())) { // 活动结束日期不为空
			sql.append(" AND y.ENDDATE  <= to_date('"
					+ ttAsActivityBean.getEnddate() + "', 'yyyy-MM-dd') \n");
		}
		sql.append(" group by y.activity_code");
		PageResult<Map<String, Object>> pageQuery = (PageResult<Map<String, Object>>) pageQuery(
				sql.toString(), null, getFunName(), pageSize, curPage);
		PageResult<Map<String, Object>> ps = pageQuery;
		return ps;
	}

	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Function : 由车厂端[服务营销部]增加服务活动管理信息
	 * 
	 * @param : request-活动编号、活动名称、活动类型、活动类别、活动开始日期、活动结束日期、处理方式、距活动结束日期几天上传活动总结
	 * @param : 配件费用、工时费用、索赔、索赔是否为固定费用、解决方案说明、索赔申请填写指导
	 * @return : 服务活动管理信息
	 * @throws : Exception LastUpdate : 2010-06-01
	 */
	public static void serviceActivityManageAdd(TtAsActivityPO ActivityPO) {
		dao.insert(ActivityPO);
	}

	/**
	 * Function : 新增之后---查询服务管理活动信息
	 * 
	 * @param : request-活动ID
	 * @return : 服务管理活动信息
	 * @throws : Exception LastUpdate : 2010-06-01
	 */
	public TtAsActivityBean serviceActivityManageCommon(String activityId) {
		List<Object> params = new LinkedList();
		StringBuilder sql = new StringBuilder("\n");
		sql.append("  SELECT STATUS,  COMPANY_ID,TOW_TYPE_ACTIVITY, CREATE_DATE, UPDATE_BY,  CREATE_By, UPDATE_DATE,  CLAIM_TYPE,VEHICLE_AREA,single_car_num,max_car,set_direct, \n");
		sql.append("  ACTIVITY_CODE, ACTIVITY_TYPE, to_char(STARTDATE,'yyyy-MM-dd')as STARTDATE,to_char(ENDDATE,'yyyy-MM-dd')as ENDDATE ,to_char(FACTSTARTDATE,'yyyy-MM-dd')as FACTSTARTDATE,to_char(FACTENDDATE,'yyyy-MM-dd')as FACTENDDATE, ACTIVITY_ID, ACTIVITY_KIND,   \n");
		sql.append("  ACTIVITY_NAME, WORKTIME_FEE,  PART_FEE,  DEALWITH,  UPLOAD_PRE_PERIOD, TROUBLE_DESC,TROUBLE_REASON,REPAIR_METHOD,APP_REMARK,  \n");
		sql.append("  SOLUTION, CLAIM_GUIDE,  IS_CLAIM,  IS_FIXFEE, CAR_NUM,  RELEASEDATE, OTHER_FEE ,ACTIVITY_FEE,MILAGE_CONFINE,SUBJECT_ID,NEW_ID  \n");
		sql.append("  FROM TT_AS_ACTIVITY \n");
		sql.append("  WHERE  1=1 ");
		if (activityId != null && !("").equals(activityId)) {
			sql.append(" AND ACTIVITY_ID = ? ");
			params.add(activityId);
		}
		TtAsActivityBean ActivityBean = new TtAsActivityBean();
		List list = dao.select(TtAsActivityBean.class, sql.toString(), params);
		if (list != null) {
			if (list.size() > 0) {
				ActivityBean = (TtAsActivityBean) list.get(0);
			}
		}
		/*
		 * PageResult<TtAsActivityBean> rs =
		 * pageQuery(TtAsActivityBean.class,sql.toString(), null, 10, 1);
		 * List<TtAsActivityBean> ls = rs.getRecords(); if (ls!=null){ if
		 * (ls.size()>0) { ActivityBean = ls.get(0); } }
		 */
		return ActivityBean;
	}

	/**
	 * Function : 修改服务管理活动信息
	 * 
	 * @param : request-活动编号、活动名称、活动类型、活动类别、活动开始日期、活动结束日期、处理方式、距活动结束日期几天上传活动总结
	 * @param : 配件费用、工时费用、索赔、索赔是否为固定费用、解决方案说明、索赔申请填写指导
	 * @return : 服务管理活动信息
	 * @throws : Exception LastUpdate : 2010-06-01
	 */
	public static void serviceActivityManageUpdate(
			TtAsActivityPO ActivityPOCon, TtAsActivityPO ActivityPO) {
		dao.update(ActivityPOCon, ActivityPO);
	}

	/**
	 * Function : 删除服务管理活动信息
	 * 
	 * @param : request-工单号
	 * @return : 服务管理活动信息
	 * @throws : ParseException LastUpdate : 2010-06-01
	 */
	public static void serviceActivityManageDelete(String activityId,
			TtAsActivityPO ActivityPoContent) {
		TtAsActivityPO ActivityPO = new TtAsActivityPO();// activityId条件
		ActivityPO.setActivityId(Long.parseLong(activityId));
		dao.update(ActivityPO, ActivityPoContent);
	}

	public void executeXYZ(Long activityId) throws SQLException {
		// List listtypes = new ArrayList();
		// listtypes.add(java.sql.Types.INTEGER);
		//
		// List listvalues = new ArrayList();
		// listvalues.add(activityId);
		// callProcedure("PKG_SALE_SERVICE.P_ACTIVITY",listtypes,listvalues);
		Connection conn = null;
		CallableStatement proc = null;
		try {
			conn = DBService.getInstance().getConnection();
			proc = conn.prepareCall("{ call PKG_SALE_SERVICE.P_ACTIVITY(?) }");
			proc.setLong(1, activityId);
			proc.execute();
			POContext.endTxn(true);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			POContext.endTxn(false);
			e.printStackTrace();
		}finally{
			try {
				if(proc!=null) proc.close();
				if(conn!=null) conn.close();
                POContext.cleanTxn();
            } catch (Exception ex) {
            	ex.printStackTrace();
            }
			
		}

	}

	/**
	 * Function : 服务管理活动信息
	 * 
	 * @param : 活动ID
	 * @return : 根据活动ID查询出具体 服务管理活动信息
	 * @throws : Exception LastUpdate : 2010-06-01
	 */
	public TtAsActivityBean getServiceActivityByActivityIdInfo(String activityId) {
		List<Object> params = new LinkedList();
		StringBuilder sql = new StringBuilder();
		sql.append("  SELECT STATUS,  COMPANY_ID,TOW_TYPE_ACTIVITY, CREATE_DATE, UPDATE_BY,  CREATE_By, UPDATE_DATE,  CLAIM_TYPE, single_car_num,max_car,set_direct,   \n");
		sql.append("  ACTIVITY_CODE, ACTIVITY_TYPE, to_char(STARTDATE,'yyyy-MM-dd')as STARTDATE,to_char(ENDDATE,'yyyy-MM-dd')as ENDDATE , ACTIVITY_ID, ACTIVITY_KIND,   \n");
		sql.append("  ACTIVITY_NAME, WORKTIME_FEE,  PART_FEE,  DEALWITH,  UPLOAD_PRE_PERIOD,   \n");
		sql.append("  SOLUTION, CLAIM_GUIDE,TROUBLE_DESC,TROUBLE_REASON,REPAIR_METHOD,APP_REMARK, IS_CLAIM,  IS_FIXFEE, CAR_NUM,  RELEASEDATE, OTHER_FEE ,ACTIVITY_FEE,MILAGE_CONFINE ,SUBJECT_ID,NEW_ID \n");
		sql.append("  FROM TT_AS_ACTIVITY \n");
		sql.append("  WHERE  1=1 ");
		if (activityId != null && !("").equals(activityId)) {
			sql.append(" AND ACTIVITY_ID = ?  \n");
			params.add(activityId);
		}
		TtAsActivityBean ActivityBean = new TtAsActivityBean();
		List list = dao.select(TtAsActivityBean.class, sql.toString(), params);
		if (list != null) {
			if (list.size() > 0) {
				ActivityBean = (TtAsActivityBean) list.get(0);
			}
		}

		return ActivityBean;
	}

	public boolean ServiceActivityManagejude(String activity_code) {
		List<Object> params = new LinkedList();
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT * FROM TT_AS_ACTIVITY T WHERE T.ACTIVITY_CODE = '"
				+ activity_code + "'");
		List list = dao.select(TtAsActivityPO.class, sql.toString(), params);
		if (list != null && list.size() > 0) {
			return false;
		}
		return true;
	}

	/**
	 * Function : 服务管理活动信息---工时查询WorkingHours
	 * 
	 * @param : request-活动ID
	 * @return : 服务管理活动信息
	 * @throws : Exception LastUpdate : 2010-06-01
	 */
	public List<TtAsActivityBean> getWorkingHoursInfoList(String activityId) {
		List<Object> params = new LinkedList();
		StringBuilder sql = new StringBuilder();
		sql.append("  SELECT UPDATE_DATE,  CREATE_DATE, CREATE_BY, UPDATE_BY, ITEM_CODE, ITEM_ID, ACTIVITY_ID, ITEM_NAME, LABOR_FEE,  NORMAL_LABOR,  STAT_TITLE   \n");
		sql.append("  FROM TT_AS_ACTIVITY_REPAIRITEM \n");
		if (activityId != null && !("").equals(activityId)) {
			sql.append("  WHERE  ACTIVITY_ID = ? ");
			params.add(activityId);
		}
		sql.append("order by ACTIVITY_ID desc ");
		List list = dao.select(TtAsActivityBean.class, sql.toString(), params);
		return list;
	}

	/**
	 * Function : 服务管理活动信息---活动配件
	 * 
	 * @param : request-活动ID
	 * @return : 服务管理活动信息
	 * @throws : Exception LastUpdate : 2010-06-01
	 */
	public List<TtAsActivityBean> getPartsList(String activityId) {
		List<Object> params = new LinkedList();
		StringBuilder sql = new StringBuilder();
		sql.append("  SELECT UPDATE_DATE,  CREATE_DATE, CREATE_BY, UPDATE_BY, PART_NO, PART_NAME, IS_MAIN_PART, PART_AMOUNT, ACTIVITY_ID, PART_PRICE, PART_QUANTITY, PART_UNIT, PARTS_ID   \n");
		sql.append("  FROM TT_AS_ACTIVITY_PARTS \n");
		if (activityId != null && !("").equals(activityId)) {
			sql.append("  WHERE  ACTIVITY_ID = ? \n");
			params.add(activityId);
		}
		sql.append("order by ACTIVITY_ID desc ");
		List list = dao.select(TtAsActivityBean.class, sql.toString(), params);
		return list;
	}

	/**
	 * Function : 服务管理活动信息---活动其它项目
	 * 
	 * @param : request-活动ID
	 * @return : 服务管理活动信息
	 * @throws : Exception LastUpdate : 2010-06-01
	 */
	public List<TtAsActivityBean> getNetItemList(String activityId) {
		List<Object> params = new LinkedList();
		StringBuilder sql = new StringBuilder();
		sql.append("  select ID,  UPDATE_DATE, REMARK,  CREATE_DATE, CREATE_BY, UPDATE_BY, ITEM_CODE AS ITEM_CODES, ITEM_DESC,  AMOUNT, ACTIVITY_ID   \n");
		sql.append("  FROM TT_AS_ACTIVITY_NETITEM \n");
		if (activityId != null && !("").equals(activityId)) {
			sql.append("  WHERE  ACTIVITY_ID = ? \n ");
			params.add(activityId);
		}
		sql.append("  ORDER BY ACTIVITY_ID desc \n");
		List list = dao.select(TtAsActivityBean.class, sql.toString(), params);
		return list;
	}

	/**
	 * Function : 服务管理活动信息---车型列表
	 * 
	 * @param : request-活动ID
	 * @return : 服务管理活动信息
	 * @throws : Exception LastUpdate : 2010-06-01
	 */
	public List<TtAsActivityBean> getVhclMaterialGroupList(String activityId) {
		StringBuilder sql = new StringBuilder("\n");
		sql.append("select distinct gp.wrgroup_name as group_code,mg2.group_name group_name,mg.group_name parent_group_name,mg.group_code   parent_group_code\n");
		sql.append("from tt_as_wr_model_group gp,\n");
		sql.append("tt_as_wr_model_item mi,\n");
		sql.append(" tm_vhcl_material_group mg, TM_VHCL_MATERIAL_GROUP mg2,\n");
		sql.append("TT_AS_ACTIVITY_MGROUP m\n");
		sql.append("where gp.wrgroup_type = ")
				.append(Constant.WR_MODEL_GROUP_TYPE_01).append("\n");
		sql.append("and gp.wrgroup_id = mi.wrgroup_id\n");
		sql.append("and mi.model_id=mg.group_id\n");
		sql.append("and mg.parent_group_id = mg2.group_id\n");
		sql.append("and mi.model_id = m.material_group_id\n");
		if (activityId != null && !("").equals(activityId)) {
			sql.append("   and m.activity_id=").append(activityId).append("\n");
		}
		sql.append(" order by parent_group_name desc \n");
		List list = dao.select(TtAsActivityBean.class, sql.toString(), null);
		return list;
	}

	/**
	 * Function : 服务管理活动信息---车龄定义列表
	 * 
	 * @param : request-活动ID
	 * @return : 服务管理活动信息
	 * @throws : Exception LastUpdate : 2010-06-01
	 */
	public List<TtAsActivityBean> getActivityAgeList(String activityId) {
		List<Object> params = new LinkedList();
		StringBuilder sql = new StringBuilder();
		sql.append("  SELECT ID as ID_AGE, UPDATE_DATE, CREATE_DATE,  CREATE_BY,  UPDATE_BY, ACTIVITY_ID,  to_char(SALE_DATE_END,'yyyy-MM-dd')as SALE_DATE_END,  to_char(SALE_DATE_START,'yyyy-MM-dd')as SALE_DATE_START, CUSTOMER_TYPE ,date_type  \n");
		sql.append("  FROM TT_AS_ACTIVITY_AGE \n");
		if (activityId != null && !("").equals(activityId)) {
			sql.append("  WHERE ACTIVITY_ID = ?  \n");
			params.add(activityId);
		}
		sql.append(" ORDER BY ACTIVITY_ID desc \n");
		List list = dao.select(TtAsActivityBean.class, sql.toString(), params);
		return list;
	}

	/**
	 * Function : 服务管理活动信息---车辆性质
	 * 
	 * @param : request-活动ID
	 * @return : 服务管理活动信息
	 * @throws : Exception LastUpdate : 2010-06-01
	 */
	public List<TtAsActivityBean> getActivityCharactorList(String activityId) {
		List<Object> params = new LinkedList();
		StringBuilder sql = new StringBuilder();
		sql.append("	SELECT ID,  CREATE_DATE, UPDATE_BY,  CREATE_BY, UPDATE_DATE, ACTIVITY_ID, CAR_CHARACTOR  \n");
		sql.append("    FROM TT_AS_ACTIVITY_CHARACTOR \n");
		if (!"".equals(activityId) && !(null == activityId)) {// 活动ID
			sql.append(" WHERE ACTIVITY_ID = ? \n");
			params.add(activityId);
		}
		sql.append("     ORDER BY ACTIVITY_ID DESC  \n");
		List list = dao.select(TtAsActivityBean.class, sql.toString(), params);
		return list;
	}

	/**
	 * Function : 服务管理活动信息---车辆信息
	 * 
	 * @param : request-活动ID
	 * @return : 服务管理活动信息
	 * @throws : Exception LastUpdate : 2010-06-01
	 */
	public PageResult<Map<String, Object>> getActivityVehicleList(
			String activityId, String vin, int curPage, int pageSize)
			throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append("  SELECT a.VIN, b.DEALER_CODE, b.DEALER_SHORTNAME, a.CUSTOMER_NAME,a.LINKMAN_MOBILE,a.CUSTOMER_ADDRESS, a.POSTAL_CODE, a.LINKMAN,   \n");
		sql.append("  a.LINKMAN_ZONE_NUM, a.LINKMAN_OFFICE_PHONE, a.LINKMAN_FAMILY_PHONE, a.EMAIL,a.PROVINCE,AREA,a.TOWN,a.LINCENSE_TAG , \n");
		sql.append("  CASE WHEN (SELECT count(*) FROM TM_VEHICLE TV WHERE TV.VIN = a.vin) > 0 THEN 10491001  ELSE 10491002 END SALE_STATUS,\n");
		sql.append("  CASE WHEN (SELECT count(*) FROM Tt_As_Repair_Order TVRO WHERE TVRO.VIN = a.vin and TVRO.Cam_Code = (SELECT TAA.ACTIVITY_CODE FROM TT_AS_ACTIVITY TAA WHERE TAA.ACTIVITY_ID = a.activity_id) ) > 0 THEN 10481001 ELSE 10481002 END REPAIR_STATUS \n");
		sql.append("  FROM TT_AS_ACTIVITY_VEHICLE  a left join TM_DEALER  b  on a.dealer_id=b.dealer_id where 1=1\n");
		if (null != activityId && !"".equals(activityId)) {
			sql.append("  and ACTIVITY_ID = ' ");
			sql.append(activityId);
			sql.append("' \n");
		}
		if (StringUtil.notNull(vin))
			sql.append(" and a.vin like '%").append(vin).append("%'\n");
		sql.append(" ORDER BY a.VIN desc \n");
		PageResult<Map<String, Object>> ps = (PageResult<Map<String, Object>>) pageQuery(
				sql.toString(), null, getFunName(), pageSize, curPage);
		return ps;
	}

	/**
	 * Function : 新增之后---查询服务管理活动信息---售前车
	 * 
	 * @param : request-活动ID
	 * @return : 服务管理活动信息
	 * @throws : Exception LastUpdate : 2010-06-01
	 */
	public TtAsActivityBean serviceActivityVehicleBeforeVehicleArea() {
		List<Object> params = new LinkedList();
		StringBuilder sql = new StringBuilder();
		sql.append("  SELECT CODES.CODE_ID,CODES.CODE_DESC   \n");
		sql.append("  FROM TC_CODE CODES \n");
		sql.append("  WHERE CODES.CODE_ID = "
				+ Constant.SERVICEACTIVITY_VEHICLE_AREA_01);// 服务活动车辆范围代码:售前车
		sql.append(" ORDER BY CODES.CODE_ID desc \n");
		TtAsActivityBean ActivityBean = new TtAsActivityBean();
		List list = dao.select(TtAsActivityBean.class, sql.toString(), params);
		if (list != null) {
			if (list.size() > 0) {
				ActivityBean = (TtAsActivityBean) list.get(0);
			}
		}
		return ActivityBean;
	}

	/**
	 * Function : 新增之后---查询服务管理活动信息---售后车
	 * 
	 * @param : request-活动ID
	 * @return : 服务管理活动信息
	 * @throws : Exception LastUpdate : 2010-06-01
	 */
	public TtAsActivityBean serviceActivityVehicleAfterVehicleArea() {
		List<Object> params = new LinkedList();
		StringBuilder sql = new StringBuilder();
		sql.append("  SELECT CODES.CODE_ID,CODES.CODE_DESC   \n");
		sql.append("  FROM TC_CODE CODES \n");
		sql.append("  WHERE CODES.CODE_ID = "
				+ Constant.SERVICEACTIVITY_VEHICLE_AREA_02);// 服务活动车辆范围代码:售后车
		sql.append(" ORDER BY CODES.CODE_ID desc \n");
		TtAsActivityBean ActivityBean = new TtAsActivityBean();
		List list = dao.select(TtAsActivityBean.class, sql.toString(), params);
		if (list != null) {
			if (list.size() > 0) {
				ActivityBean = (TtAsActivityBean) list.get(0);
			}
		}
		return ActivityBean;
	}

	/**
	 * Function : 新增之后---查询服务管理活动信息---查询业务表中的服务活动车辆范围
	 * 
	 * @param : request-活动ID
	 * @return : 服务管理活动信息
	 * @throws : Exception LastUpdate : 2010-06-01
	 */
	public TtAsActivityBean serviceActivityVehicleArea(String activityId) {
		List<Object> params = new LinkedList();
		StringBuilder sql = new StringBuilder();
		sql.append("  SELECT ACTIVITY_ID, VEHICLE_AREA   \n");
		sql.append("  FROM TT_AS_ACTIVITY \n");
		sql.append("  WHERE 1=1   \n");// 服务活动车辆范围代码:售前车
		if (!"".equals(activityId) && null != activityId) {
			sql.append("  AND  ACTIVITY_ID=" + activityId);// 服务活动车辆范围代码:售前车
		}
		sql.append(" ORDER BY ACTIVITY_ID desc \n");
		TtAsActivityBean ActivityBean = new TtAsActivityBean();
		List list = dao.select(TtAsActivityBean.class, sql.toString(), params);
		if (list != null) {
			if (list.size() > 0) {
				ActivityBean = (TtAsActivityBean) list.get(0);
			}
		}
		return ActivityBean;
	}

	/**
	 * Function : 删除---车辆性质列表
	 * 
	 * @param : request-activity_ID
	 * @return : 服务活动管理--车辆性质列表
	 * @throws : Exception LastUpdate : 2010-07-28
	 */
	public static void serviceActivityManageCharactorDelete(
			TtAsActivityCharactorPO CharactorPO) {
		TtAsActivityCharactorPO CPo = new TtAsActivityCharactorPO();
		CPo.setActivityId(CharactorPO.getActivityId());
		dao.delete(CPo);
	}

	public PageResult<Map<String, Object>> getMilage(int pageSize, int curPage,
			String id) throws Exception {
		PageResult<Map<String, Object>> result = null;
		StringBuilder sql = new StringBuilder();

		sql.append("select q.*,(select count(*) from tt_as_activity_milage\n");
		sql.append(" where free_times=q.free_times and activity_id=" + id
				+ ") AS A\n");
		sql.append(" from tt_as_wr_qamaintain q");

		// sql.append(" select wno.news	_id from tt_as_wr_news_org  wno where wno.dealer_id="+dealerId+"  and status="+Constant.STATUS_ENABLE+") \n");
		// "com.infodms.dms.dao.claim.basicData.ClaimBasicParamsDao.claimBasicParamsQuery"
		result = (PageResult<Map<String, Object>>) pageQuery(sql.toString(),
				null, getFunName(), pageSize, curPage);
		return result;
	}

	public List<Map<String, Object>> getMilageId(long id) throws Exception {
		PageResult<Map<String, Object>> result = null;
		StringBuilder sb = new StringBuilder();
		sb.append(" select * from  tt_as_wr_qamaintain where id= " + id);
		List<Map<String, Object>> list = pageQuery(sb.toString(), null,
				getFunName());
		return list;
	}

	public List<TtAsActivitySubjectPO> getTtAsActivitySubjectPo(int type) {
		List<Object> params = new LinkedList();
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT A.SUBJECT_ID,A.SUBJECT_NAME FROM TT_AS_ACTIVITY_SUBJECT A where A.IS_DEL != 1");
		/*if(type == 0)
		{
			sql.append(" and a.ACTIVITY_TYPE = " + Constant.SERVICEACTIVITY_TYPE_01);
		}else
		{
			sql.append(" and a.ACTIVITY_TYPE != " + Constant.SERVICEACTIVITY_TYPE_01);
		}*/
		List<TtAsActivitySubjectPO> list = dao.select(
				TtAsActivitySubjectPO.class, sql.toString(), params);
		return list;

	}

	public PageResult<Map<String, Object>> getBase(int pageSize,String largess_type,String activityId,String PART_CODE,String PART_NAME, int curPage) throws Exception {
		PageResult<Map<String, Object>> result = null;
		StringBuilder sql = new StringBuilder();

		sql.append("select q.*,(select count(*) from TT_AS_ACTIVITY_RELATION C\n");
		sql.append(" where C.project_id =q.PART_ID and C.ACTIVITY_ID="+activityId + " and C.largess_type = " +largess_type
				+ ") AS A\n");
		sql.append(" from TM_PT_PART_BASE q where 1=1");
		if(largess_type.equals("3537005"))
		{
			sql.append(" and  q.PART_ID not in (select A.project_id from  TT_AS_ACTIVITY_RELATION A where A.largess_type = 3537007 and A.ACTIVITY_ID = "+activityId+" )");
		}else if(largess_type.equals("3537007"))
		{
			sql.append(" and  q.PART_ID not in (select B.project_id from  TT_AS_ACTIVITY_RELATION B where B.largess_type = 3537005 and B.ACTIVITY_ID = "+activityId+" )");
		}
		
		
		if(Utility.testString(PART_CODE))
		{
			sql.append(" and  q.PART_CODE like '%"+PART_CODE.toUpperCase()+"%'");
		}
		if(Utility.testString(PART_NAME)){
			sql.append(" and q.PART_NAME like '%"+PART_NAME+"%'");
		}
		sql.append(" ORDER by A  DESC ,Q.PART_ID DESC ");
		result = (PageResult<Map<String, Object>>) pageQuery(sql.toString(),
				null, getFunName(), pageSize, curPage);
		return result;
	}
	
	public PageResult<Map<String, Object>> getLaber(int pageSize,String largess_type,String activityId,String PART_CODE,String PART_NAME, int curPage) throws Exception {
		PageResult<Map<String, Object>> result = null;
		StringBuilder sql = new StringBuilder();

		sql.append("select q.*,(select count(*) from TT_AS_ACTIVITY_RELATION C\n");
		sql.append(" where C.project_id =q.ID and C.ACTIVITY_ID="+activityId + " and C.largess_type = " +largess_type
				+ ") AS A\n");
		sql.append(" from tt_as_wr_wrlabinfo q where 1=1 and q.TREE_CODE =3 ");
		if(Utility.testString(PART_CODE))
		{
			sql.append(" and  q.LABOUR_CODE like '%"+PART_CODE.toUpperCase()+"%'");
		}
		if(Utility.testString(PART_NAME)){
			sql.append(" and q.CN_DES like '%"+PART_NAME+"%'");
		}
		sql.append("  ORDER by  A desc,LABOUR_CODE ");
		result = (PageResult<Map<String, Object>>) pageQuery(sql.toString(),
				null, getFunName(), pageSize, curPage);
		return result;
	}
	
	public List<Map<String, Object>> getActCodes(Integer code_type, int pageSize,int curPage) throws Exception {
		List<Map<String, Object>> result = null;
		StringBuilder sql = new StringBuilder();

		sql.append("SELECT t.code_desc AS NAME FROM tc_code t WHERE 1=1\n");
		if(null != code_type)
		{
			sql.append(" and  t.TYPE = "+code_type);
		}
		sql.append("  ORDER BY T.NUM ");
		result = pageQuery(sql.toString(), null, super.getFunName());
		return result;
	}
	
	public PageResult<Map<String, Object>> getsubject(RequestWrapper request,int pageSize, int curPage) throws Exception {
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT * from tt_as_activity_subject t where 1=1 and  t.is_del = 0 ");
		//DaoFactory.getsql(sql, "t.activity_type", "10561006", 7);
		DaoFactory.getsql(sql, "t.subject_name", DaoFactory.getParam(request, "subjectName"), 2);
		DaoFactory.getsql(sql, "t.subject_no", DaoFactory.getParam(request, "subjectCOde"), 2);
		sql.append("  order by t.create_date desc ");
		return pageQuery(sql.toString(),null, getFunName(), pageSize, curPage);
	}
	
	public List<TtAsActivityProjectPO> getTtAsActivityProjectPo(
			String activityId) {
		List<Object> params = new LinkedList();
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT * FROM TT_AS_ACTIVITY_PROJECT A where A.ACTIVITY_ID="
				+ activityId);
		List<TtAsActivityProjectPO> list = dao.select(
				TtAsActivityProjectPO.class, sql.toString(), params);
		return list;

	}

	public List<Map<String, Object>> getMilageActivity(long id)
			throws Exception {
		PageResult<Map<String, Object>> result = null;
		StringBuilder sb = new StringBuilder();
		sb.append(" SELECT * from Tt_As_Activity_Milage where activity_id= "
				+ id);
		List<Map<String, Object>> list = pageQuery(sb.toString(), null,
				getFunName());
		return list;
	}

	public void insertMilage(TtAsActivityMilagePO po) {
		factory.insert(po);
	}

	public void deleteMilage(String id) {
		StringBuilder sb = new StringBuilder();
		sb.append("delete from tt_as_activity_milage where activity_id= " + id);
		factory.delete(sb.toString(), null);
	}
	/**
	 * 查询配件
	 * @param activityId
	 * @return
	 */
	public List<TtAsWrPartsitemRaplcePO> getRaplceP(String activityId) {
		Long id = Long.parseLong(activityId);
		TtAsWrPartsitemRaplcePO po=new TtAsWrPartsitemRaplcePO();
		po.setId(id);
		return dao.select(po);
	}
	/**
	 * 查询工时
	 * @param activityId
	 * @return
	 */
	public List<TtAsWrLabouritemRaplcePO> getRaplceL(String activityId) {
		Long id = Long.parseLong(activityId);
		TtAsWrLabouritemRaplcePO po=new TtAsWrLabouritemRaplcePO();
		po.setId(id);
		return dao.select(po);
	}
	
	@SuppressWarnings("unchecked")
	public void serviceActivityManageUpdate(String activity_id ,TtAsActivityPO ttAsActivityPO)
	{
		TtAsActivityPO ttAsActivityPOPO1 = new TtAsActivityPO();
		ttAsActivityPOPO1.setActivityId(Long.parseLong(activity_id));
		dao.update(ttAsActivityPOPO1, ttAsActivityPO);
	}
}