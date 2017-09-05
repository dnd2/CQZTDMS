package com.infodms.dms.dao.afterSales;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.po.TtAsWrActivityVinTempPO;
import com.infodms.dms.util.StringUtil;
import com.infodms.yxdms.utils.DaoFactory;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

import antlr.StringUtils;


public class ServiceActivityDao extends BaseDao{
		
		private static final ServiceActivityDao dao = new ServiceActivityDao();
		
		public static final ServiceActivityDao getInstance() {
			return dao;
		}
		
		@Override
		protected PO wrapperPO(ResultSet rs, int idx) {
			return null;
		}
		
		//查询工时
		@SuppressWarnings("unchecked")
		public PageResult<Map<String, Object>> addLabour(RequestWrapper request,String cnt,Integer pageSize, Integer currPage) {
			StringBuffer sql= new StringBuffer();


			sql.append(" SELECT DISTINCT TAWW.LABOUR_CODE,\n");
			sql.append("                 TAWW.CN_DES,\n");
			sql.append("                 TRIM(TO_CHAR(TAWW.LABOUR_HOUR, '999990.00')) AS LABOUR_HOUR\n");
			sql.append("   FROM TT_AS_WR_WRLABINFO TAWW\n");
			sql.append("  WHERE 1 = 1\n");
			sql.append("    AND TAWW.TREE_CODE = '3'\n");
			sql.append("    AND TAWW.IS_DEL = '0'");

			if(StringUtil.notNull(cnt)){
				cnt=cnt.substring(0,cnt.length()-1);
				cnt=cnt.replace(",", "','");
			sql.append("	and TAWW.LABOUR_CODE NOT in('"+cnt+"')\n");
			}
			DaoFactory.getsql(sql, "TAWW.labour_code", DaoFactory.getParam(request, "labour_code"), 2);
			DaoFactory.getsql(sql, "TAWW.cn_des", DaoFactory.getParam(request, "cn_des"), 2);
			sql.append(" order by TAWW.labour_code");
			PageResult<Map<String, Object>> list=pageQuery(sql.toString(), null,getFunName(), pageSize, currPage);
			return list;
		}

		@SuppressWarnings("unchecked")
		public PageResult<Map<String, Object>> addPart(RequestWrapper request,String cnt,Integer pageSize, Integer currPage) {
			StringBuffer sb= new StringBuffer();
			sb.append("select a.*,\n" );
			sb.append("       CASE\n" );
			sb.append("         WHEN (trunc(sysdate) >= sp.valid_date and sp.history_flag = 1) or\n" );
			sb.append("              sp.history_flag = 0 THEN\n" );
			sb.append("          round(nvl(sp.sale_price1, 0) *\n" );
			sb.append("                (1 + 0 / 100),\n" );
			sb.append("                2)\n" );
			sb.append("         ELSE\n" );
			sb.append("          round(nvl(decode(sp.history_price,\n" );
			sb.append("                           null,\n" );
			sb.append("                           sp.sale_price1,\n" );
			sb.append("                           sp.history_price),\n" );
			sb.append("                    0) * (1 + 0 / 100),\n" );
			sb.append("                2)\n" );
			sb.append("       END claim_Price_param\n" );
			sb.append("  from TM_PT_PART_BASE a\n" );
			sb.append("  left join tt_part_sales_price sp\n" );
			sb.append("    on sp.part_id = a.part_id\n" );
			sb.append(" where 1 = 1 and a.is_del = 0\n" );
			sb.append("   AND A.Part_Is_Changhe = '95411001'");
			if(StringUtil.notNull(cnt)){
				cnt=cnt.substring(0,cnt.length()-1);
				sb.append("	and a.part_id not in("+cnt+")\n");
			}
			DaoFactory.getsql(sb, "a.part_code", DaoFactory.getParam(request, "part_code"), 2);
			DaoFactory.getsql(sb, "a.part_name", DaoFactory.getParam(request, "part_name"), 2);
			PageResult<Map<String, Object>> list=pageQuery(sb.toString(), null,getFunName(), pageSize, currPage);
			return list;
		}
		
		
		public PageResult<Map<String, Object>> getDealer(Map<String, Object> params, Integer pageSize, Integer curPage)throws Exception {
			StringBuffer sql= new StringBuffer();
			sql.append("select\n" );
			sql.append("      td.dealer_id,\n" );
			sql.append("      td.dealer_code,\n" );
			sql.append("      td.dealer_name\n" );
			sql.append("  from tm_dealer td\n" );
			sql.append("  where td.dealer_type="+Constant.MSG_TYPE_2+"");
			List<Object> paramList = new ArrayList<Object>();
			if(StringUtil.notNull((String)params.get("code"))){
				sql.append(" and td.dealer_code like ?\n");
				paramList.add("%"+params.get("code")+"%");
			}
			if(StringUtil.notNull((String)params.get("name"))){
				sql.append(" and td.dealer_name like ?\n");
				paramList.add("%"+params.get("name")+"%");
			}
			if(StringUtil.notNull((String)params.get("codes"))){
				sql.append(" and td.dealer_code = ?\n");
				paramList.add(params.get("codes"));
			}
			return dao.pageQuery(sql.toString(), paramList, getFunName(), pageSize, curPage);
		}

		public  PageResult<Map<String, Object>> getActivityVin(
				Map<String, Object> map, Integer pageSize, Integer curPage) throws Exception {
			List<Object> paramList = new ArrayList<Object>();
			StringBuffer sql= new StringBuffer();
			sql.append("select t.id,t.vin,t.dealer_id,t.activity_number,NVL(t.ACTIVITY_COMPLETED,0) ACTIVITY_COMPLETED,tm.dealer_code,tm.dealer_name from tt_as_wr_activity_vin t left join tm_dealer tm on t.dealer_id=tm.dealer_id where 1=1");
			if(StringUtil.notNull((String) map.get("activityId"))){
				sql.append(" and t.activity_id=?");
				paramList.add(map.get("activityId"));
			}
			if(StringUtil.notNull((String) map.get("vin"))){
				sql.append(" and t.vin like ?");
				paramList.add("%" + map.get("vin") + "%");
			}
			if(StringUtil.notNull((String) map.get("code"))){
				sql.append(" and tm.dealer_name like ?");
				paramList.add("%" + map.get("code") + "%");
			}
			return dao.pageQuery(sql.toString(), paramList, getFunName(), pageSize, curPage);
		}
		
		public PageResult<Map<String, Object>> getVehicleVin(
				Map<String, Object> params, Integer pageSize, Integer curPage)
				throws Exception {
			StringBuffer sql= new StringBuffer();
			sql.append("select\n" );
			sql.append("      t.vin\n" );
			sql.append("  from tm_vehicle t\n" );
			sql.append("  where 1=1");
			List<Object> paramList = new ArrayList<Object>();
			if(StringUtil.notNull((String)params.get("vin"))){
				sql.append(" and t.vin = ?\n");
				paramList.add(params.get("vin"));
			}
			return dao.pageQuery(sql.toString(), paramList, getFunName(), pageSize, curPage);
		}

		public void addData(TtAsWrActivityVinTempPO po) throws Exception {
			dao.insert(po);
		}

		public void deleteTempData(String activityId) throws Exception {
			String sql = "delete from TT_AS_WR_ACTIVITY_VIN_TEMP where activity_id = "+activityId+"";
			dao.delete(sql,null);
		}
		
		public void deleteTempVINData() throws Exception {
			StringBuffer sql= new StringBuffer();
			sql.append(" DELETE FROM TT_AS_WR_ACTIVITY_VIN T\n");
			sql.append("  WHERE NOT EXISTS (SELECT 1\n");
			sql.append("           FROM TT_AS_WR_ACTIVITY T2\n");
			sql.append("          WHERE T.ACTIVITY_ID = T2.ACTIVITY_ID)");
			dao.delete(sql.toString(),null);
		}
		
		public List<Map<String, Object>> getTempData(String activityId) throws Exception {
			StringBuffer sql= new StringBuffer();
			sql.append("select k.vin\n" );
			sql.append("       from ( select t.vin,t.activity_id\n" );
			sql.append("         from TT_AS_WR_ACTIVITY_VIN_TEMP t\n" );
			sql.append("         where 1=1\n" );
			sql.append("         and t.activity_id="+activityId+"\n" );
			sql.append("      union all\n" );
			sql.append("         select t.vin,t.activity_id\n" );
			sql.append("         from TT_AS_WR_ACTIVITY_VIN t\n" );
			sql.append("         where 1=1\n" );
			sql.append("         and t.activity_id="+activityId+") k\n" );
			sql.append("    group by k.vin,k.activity_id having count(1) > 1");
			return dao.pageQuery(sql.toString(), null, getFunName());
		}
		
		public PageResult<Map<String, Object>> getVin(Map<String, Object> params,
				Integer pageSize, Integer curPage) throws Exception {
			StringBuffer sql= new StringBuffer();
			sql.append("select\n" );
			sql.append("      t.id\n" );
			sql.append("  from tt_as_wr_activity_vin t\n" );
			sql.append("  where 1=1");
			List<Object> paramList = new ArrayList<Object>();
			if(StringUtil.notNull((String)params.get("vin"))){
				sql.append(" and t.vin = ?\n");
				paramList.add(params.get("vin"));
			}
			if(StringUtil.notNull((String)params.get("id"))){
				sql.append(" and t.activity_id = ?\n");
				paramList.add(params.get("id"));
			}
			return dao.pageQuery(sql.toString(), paramList, getFunName(), pageSize, curPage);
		}
		
		public PageResult<Map<String, Object>> getServiceActivityInfos(Map<String, Object> params,
				Integer pageSize, Integer curPage) throws Exception {
			StringBuffer sql= new StringBuffer();
			
			sql.append(" SELECT T.ACTIVITY_ID,\n");
			sql.append("        T.ACTIVITY_NAME,\n");
			sql.append("        T.ACTIVITY_CODE,\n");
			sql.append("        TO_CHAR(T.ACTIVITY_STRATE_DATE, 'yyyy-mm-dd ') ACTIVITY_STRATE_DATE,\n");
			sql.append("        TO_CHAR(T.ACTIVITY_END_DATE, 'yyyy-mm-dd ') ACTIVITY_END_DATE,\n");
			sql.append("        T.ACTIVITY_STRATE_MILEAGE,\n");
			sql.append("        T.ACTIVITY_END_MILEAGE,\n");
			sql.append("        TO_CHAR(T.ACTIVITY_SALES_STRATE_DATE, 'yyyy-mm-dd ') ACTIVITY_SALES_STRATE_DATE,\n");
			sql.append("        TO_CHAR(T.ACTIVITY_SALES_END_DATE, 'yyyy-mm-dd ') ACTIVITY_SALES_END_DATE,\n");
			sql.append("        T.ACTIVITY_DISCOUNT,\n");
			sql.append("        T.ACTIVITY_STATUS,\n");
			sql.append("        T.ACTIVITY_TYPE,\n");
			sql.append("        T.MAINTAIN_NUMBER,\n");
			sql.append("        T.MAINTAIN_MONEY,\n");
			sql.append("        T.DETECTION_MONEY,\n");
			sql.append("        T.MAINTENANCE_MEASURES,\n");
			sql.append("        F_GET_TCUSER_NAME(T.RELEASE_BY) RELEASE_BY,\n");
			sql.append("        TO_CHAR(T.RELEASE_DATE, 'yyyy-mm-dd ') RELEASE_DATE,\n");
			sql.append("        T.FAULT_PROBLEM,\n");
			sql.append("        T.FAULT_DESCRIPTION\n");
			sql.append("   FROM TT_AS_WR_ACTIVITY T\n");
			sql.append("  WHERE 1 = 1\n");
			List<Object> paramList = new ArrayList<Object>();
			if(StringUtil.notNull((String)params.get("activity_code"))){
				sql.append(" and t.activity_code=? \n");
				paramList.add(params.get("activityCode"));
			}
			DaoFactory.getsql(sql, "t.ACTIVITY_NAME",(String)params.get("activityName") , 2);

			DaoFactory.getsql(sql, "t.ACTIVITY_STATUS",(String)params.get("activityStatus") , 1);
			
			DaoFactory.getsql(sql, "and t.ACTIVITY_TYPE",(String)params.get("activityType") , 1);

			if(StringUtil.notNull((String)params.get("id"))){
				sql.append(" and t.activity_id=? \n");
				paramList.add(params.get("id"));
			}
			sql.append(" ORDER BY T.CREATE_DATE DESC");
			return dao.pageQuery(sql.toString(), paramList, getFunName(), pageSize, curPage);
		}
		public List<Map<String, Object>> getActivityPart(String id)
				throws Exception {
			List<Object> paramList = new ArrayList<Object>();
			StringBuffer sql= new StringBuffer();

		sql.append(" SELECT DISTINCT T.PART_CODE,\n");
		sql.append("                 T.PART_NAME,\n");
		sql.append("                 T.APPLY_PART_COUNT,\n");
		sql.append("                 T.IS_MAIN,\n");
		sql.append("                 T.PART_MAIN_ID,\n");
		sql.append("                 T1.HOURS_NAME,\n");
		sql.append("                 (SELECT T3.PART_NAME FROM TM_PT_PART_BASE T3 WHERE T3.PART_ID=T.PART_MAIN_ID) PART_MAIN_NAME,\n");
		sql.append("                 F_GET_TCCODE_DESC(T.PART_USE_TYPE) PARTUSER\n");
		sql.append("   FROM TT_AS_WR_ACTIVITY_PART T, TT_AS_WR_ACTIVITY_HOURS T1\n");
		sql.append("  WHERE T.ACTIVITY_ID = T1.ACTIVITY_ID\n");
		sql.append("    AND T.ACTIVITY_HOURS_CODE = T1.HOURS_CODE\n");

			if(StringUtil.notNull(id)){
				sql.append(" and t.activity_id=?");
				paramList.add(id);
			}
			return dao.pageQuery(sql.toString(), paramList, dao.getFunName());
		}
		
	public List<Map<String, Object>> getActivityLabour(String id) throws Exception {
		List<Object> paramList = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT *\n");
		sql.append("   FROM TT_AS_WR_ACTIVITY_HOURS T\n");
		sql.append("  WHERE 1 = 1\n");
		if (StringUtil.notNull(id)) {
			sql.append(" and t.activity_id=?");
			paramList.add(id);
		}
		return dao.pageQuery(sql.toString(), paramList, dao.getFunName());
	}
		public List<Map<String, Object>> getActivityDealer(String id)
				throws Exception {
			List<Object> paramList = new ArrayList<Object>();
			StringBuffer sql= new StringBuffer();
			sql.append("select t.id,t.dealer_id,tm.dealer_code,tm.dealer_name from tt_as_wr_activity_dealer t left join tm_dealer tm on t.dealer_id=tm.dealer_id where 1=1");
			if(StringUtil.notNull(id)){
				sql.append(" and t.activity_id=?");
				paramList.add(id);
			}
			return dao.pageQuery(sql.toString(), paramList, dao.getFunName());
		}
		public List<Map<String, Object>> getActivityModel(String id)
				throws Exception {
			List<Object> paramList = new ArrayList<Object>();
			StringBuffer sql= new StringBuffer();

		sql.append(" SELECT T.ID, T.MODE_ID, TM.WRGROUP_CODE\n");
		sql.append("   FROM TT_AS_WR_ACTIVITY_MODEL T\n");
		sql.append("   LEFT JOIN TT_AS_WR_MODEL_GROUP TM\n");
		sql.append("     ON T.MODE_ID = TM.WRGROUP_ID\n");
		sql.append("  WHERE 1 = 1");
			if(StringUtil.notNull(id)){
				sql.append(" and t.activity_id=?");
				paramList.add(id);
			}
			return dao.pageQuery(sql.toString(), paramList, dao.getFunName());
		}
		
		public void changeStatus(String id,Long userId) throws Exception {
			String sqt = "update tt_as_wr_activity set activity_status="+Constant.SERVICEACTIVITY_STATUS_NEW_02+",RELEASE_BY="+userId+",RELEASE_DATE=sysdate where activity_id="+id+"" ;
			dao.update(sqt, null);
			
			StringBuffer sql= new StringBuffer();
			sql.append(" UPDATE TT_AS_WR_ACTIVITY_VIN T\r\n");
			sql.append("    SET T.ACTIVITY_NUMBER =\r\n");
			sql.append("        (SELECT DECODE(T1.ACTIVITY_TYPE, '96281002', T1.MAINTAIN_NUMBER, 1)\r\n");
			sql.append("           FROM TT_AS_WR_ACTIVITY T1\r\n");
			sql.append("          WHERE T1.ACTIVITY_ID = "+id+")\r\n");
			sql.append("  WHERE T.ACTIVITY_ID = "+id+"");
			dao.update(sql.toString(), null);
		}
}

