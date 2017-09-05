package com.infodms.dms.dao.parts.planManager;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.po.TtPartPlanScrollPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.StringUtil;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

/**
 * 备件计划管理Dao
 * @author fanzhineng
 * @Date 2017年4月11日09:53:35
 */
@SuppressWarnings("rawtypes")
public class PartPlanManagerDao extends BaseDao{
	/*=============================================================================*/
	public static final Logger logger = Logger.getLogger(PartPlanManagerDao.class);
	public static final PartPlanManagerDao dao = new PartPlanManagerDao();
	private PartPlanManagerDao(){
	}
	public static final PartPlanManagerDao getInstance(){
		return dao;
	}
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		return null;
	}
	/*=============================================================================*/
	/**
	 * 写入备件ID，收货库房，供应商，订单周期 ，修改总成件状态
	 * @param planId
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public void impExcelUpdatePart(String planId) throws Exception{
		//写入备件ID，收货库房，供应商，订单周期
        StringBuffer sql = new StringBuffer();
        sql.append("UPDATE TT_PART_PLAN_SCROLL_DEL TPSD\n");
        sql.append("   SET (TPSD.PART_ID, TPSD.WH_ID,TPSD.ORDER_PERIOD,TPSD.VENDER_ID) =\n");
        sql.append("       (SELECT D.PART_ID, D.WH_ID,D.ORDER_PERIOD,PKG_PART_UTIL.F_GET_PART_VENDER_ROWNUM1(D.PART_ID,1) FROM TT_PART_DEFINE D WHERE D.PART_OLDCODE = TPSD.PART_OLDCODE)\n");
        sql.append("WHERE TPSD.PLAN_ID = '"+planId+"'\n");
        this.update(sql.toString(), null);
        
        
	}
	
	/**
	 * 滚动计划导入相关
	 * @param planId
	 * @return
	 * 写入备件ID和仓库转单周期，修改总成件状态，查询错误数据
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> queryErr(String planId) {
        //查询错误数据
        StringBuffer sql2 = new StringBuffer();
        sql2.append("SELECT E.INFO FROM VW_PART_PLAN_IMP_ERR E WHERE PLAN_ID = '" + planId + "'");
        return this.pageQuery(sql2.toString(), null, this.getFunName());
    }
	/**
	 * 滚动计划导入相关
	 * @param planNo
	 * @return
	 * 只保留一条重复数据，即最后出现的数据，其他删除，查询视图打上错误标识
	 */
	@SuppressWarnings("unchecked")
	public void delErrPlan(String planId) {
		//删除重复数据，只保留一条
        StringBuffer sql = new StringBuffer();
        sql.append("DELETE\n");
        sql.append("FROM\n");
        sql.append("  TT_PART_PLAN_SCROLL_DEL DTL\n");
        sql.append("WHERE\n");
        sql.append("  DTL.ID IN (\n");
        sql.append("    SELECT\n");
        sql.append("      TEMP.ID\n");
        sql.append("    FROM(\n");
        sql.append("      SELECT\n");
        sql.append("        T1.ID,\n");
        sql.append("        ROWNUM AS R\n");
        sql.append("      FROM\n");
        sql.append("        TT_PART_PLAN_SCROLL_DEL T1\n");
        sql.append("      WHERE\n");
        sql.append("        T1.PLAN_ID = '"+planId+"'\n");
        sql.append("        AND T1.PART_ID IN(\n");
        sql.append("          SELECT\n");
        sql.append("            T2.PART_ID\n");
        sql.append("          FROM\n");
        sql.append("            TT_PART_PLAN_SCROLL_DEL T2\n");
        sql.append("          WHERE\n");
        sql.append("            T2.PLAN_ID = '"+planId+"'\n");
        sql.append("          GROUP BY\n");
        sql.append("            T2.PART_ID\n");
        sql.append("          HAVING\n");
        sql.append("            COUNT(T2.PART_ID) > 1\n");
        sql.append("        )\n");
        sql.append("      ORDER BY T1.ID ASC\n");
        sql.append("    ) TEMP\n");
        sql.append("    WHERE\n");
        sql.append("      TEMP.R>1\n");
        sql.append("  )\n");

        //查询视图，打上错误标识
        String sqlNew1 = "SELECT T.PLAN_LINE_ID FROM VW_PART_PLAN_IMP_ERR T WHERE T.PLAN_ID = '"+planId+"'";
        List<Map<String, Object>> lineIdList = this.pageQuery(sqlNew1, null, getFunName());
        if(lineIdList!=null && !lineIdList.isEmpty() && lineIdList.size()>0){
        	for (int j = 0; j < lineIdList.size(); j++) {
        		Map<String, Object> mapTemp = lineIdList.get(j);
        		StringBuffer sqlNew2 = new StringBuffer();
                sqlNew2.append("UPDATE TT_PART_PLAN_SCROLL_DEL D\n");
                sqlNew2.append("  SET D.sales_status = " + Constant.SCROLL_PLAN_CONFIRM_STATUS_05 + ",--销售导入错误\n");
                sqlNew2.append("      D.PURCHASE_STATUS = ''\n");
                sqlNew2.append("WHERE 1=1\n");
                sqlNew2.append("  AND D.ID = '"+CommonUtils.checkNull(mapTemp.get("PLAN_LINE_ID"))+"'\n");
                this.update(sqlNew2.toString(), null);
			}
        	
        }
    }
	
	/**
     * @param sql
     * @param OrgId
     * @param planTypes
     * @param dtlTypes
     */
    private void getSqlMain(StringBuffer sql, Long OrgId, Integer planTypes, Integer dtlTypes) {
        sql.append("SELECT *\n");
        sql.append("  FROM (SELECT T.PLAN_NO,\n");
        sql.append("               COUNT(*) PLAN_NUMS,\n");
        sql.append("               T.ID TID,\n");
        sql.append("               SUM(T1.PLAN_MONTH_ONE) PLAN_COUNT,\n");
        sql.append("               T.CREATE_DATE,\n");
        sql.append("               T.MONTH_DATE,\n");
        sql.append("               T.IS_SUBMIT,\n");
        sql.append("               T.SUBMIT_DATE\n");
        sql.append("          FROM TT_PART_PLAN_SCROLL T, TT_PART_PLAN_SCROLL_DEL T1\n");
        sql.append("         WHERE T.ID = T1.PLAN_ID\n");
        sql.append("           --AND T1.SALES_STATUS IN (92981005, 92981001)\n");
        sql.append("           AND T.PLAN_TYPES = " + planTypes + "\n");
        sql.append("         GROUP BY T.PLAN_NO, T.CREATE_DATE, T.MONTH_DATE, T.ID,T.IS_SUBMIT,T.SUBMIT_DATE) TT\n");
        sql.append(" WHERE 1 = 1\n");
        
        
    }
	
	@SuppressWarnings("unchecked")
	public PageResult<Map<String, Object>> queryScrollSalesMain(AclUserBean logonUser, String year, String month, int pageSize, int curPage, Integer planTypes, Integer dtlTypes, String planNo) {
        StringBuffer sql = new StringBuffer();
        getSqlMain(sql, logonUser.getOrgId(), planTypes, dtlTypes);

        if (!"".equals(planNo)) {
            sql.append(" and tt.plan_no like '%" + planNo + "%'");
        }
        if (!"".equals(year) && !"null".equals(year) && !"".equals(month) && !"null".equals(month) && null!= year && null!= month) {
            sql.append("  and tt.MONTH_DATE='" + year + month + "' \n");
        }
        if (!"".equals(year) && !"null".equals(year) && null!= year) {
            sql.append("  and tt.MONTH_DATE like '%" + year + "%' \n");
        }
        if (!"".equals(month) && !"null".equals(month) && null!= month) {
            sql.append("  and tt.MONTH_DATE like '%" + month + "%' \n");
        }
        sql.append(" order by plan_no");
        PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);

        return ps;
    }
	/**
	 * 查询待确认计划的备件数据
	 * @param request
	 * @param loginUser
	 * @param curPage
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public PageResult<Map<String, Object>> getPartPlanValidationInfo(RequestWrapper request, AclUserBean loginUser, Integer curPage, Integer pageSize) throws Exception{
		StringBuffer sql = new StringBuffer();
		List<Object> params = new ArrayList<Object>();
		
		String SUPERIOR_PURCHASING_SELECT = request.getParamValue("SUPERIOR_PURCHASING_SELECT");
		String PLAN_YEAR_SELECT = request.getParamValue("PLAN_YEAR_SELECT");
		String PLAN_MONTH_SELECT = request.getParamValue("PLAN_MONTH_SELECT");
		String yearMonth = CommonUtils.checkNull(PLAN_YEAR_SELECT)+CommonUtils.checkNull(PLAN_MONTH_SELECT);
		
		String PLAN_NO_SELECT = request.getParamValue("PLAN_NO_SELECT");
		String FIRST_CONFIRM_STATE_SELECT = request.getParamValue("FIRST_CONFIRM_STATE_SELECT");
		
		String PART_OLDCODE_SELECT = request.getParamValue("PART_OLDCODE_SELECT");
		String PART_CNAME_SELECT = request.getParamValue("PART_CNAME_SELECT");
		String PART_CODE_SELECT = request.getParamValue("PART_CODE_SELECT");
		
		if(Constant.PURCHASE_TYPE_01.toString().equals(SUPERIOR_PURCHASING_SELECT) || 
				Constant.PURCHASE_TYPE_02.toString().equals(SUPERIOR_PURCHASING_SELECT) || 
				Constant.PURCHASE_TYPE_04.toString().equals(SUPERIOR_PURCHASING_SELECT)){
			//涂装车间 或者 专用车厂 或者 总装车间
			StringBuffer sbSql = new StringBuffer();
			sbSql.append("SELECT\n" );
			sbSql.append("  COUNT(TT.USER_ID) AS USERC\n" );
			sbSql.append("FROM(\n" );
			sbSql.append("SELECT\n" );
			sbSql.append("  PU.DEFINE_ID,\n" );
			sbSql.append("  PU.USER_ID,\n" );
			sbSql.append("  PU.USER_TYPE,\n" );
			sbSql.append("  U . NAME,\n" );
			sbSql.append("  PF.FIX_GROUPNAME,\n" );
			sbSql.append("  PF.FIX_NAME,\n" );
			sbSql.append("  PU.STATE,\n" );
			sbSql.append("  PU.IS_LEADER,\n" );
			sbSql.append("  PU.IS_DIRECT,\n" );
			sbSql.append("  PU.IS_CHKZY\n" );
			sbSql.append("FROM\n" );
			sbSql.append("  TC_USER U,\n" );
			sbSql.append("  TT_PART_USERPOSE_DEFINE PU,\n" );
			sbSql.append("  TT_PART_FIXCODE_DEFINE PF\n" );
			sbSql.append("WHERE\n" );
			sbSql.append("  PU.USER_ID = U .USER_ID (+)\n" );
			sbSql.append("AND PF.FIX_VALUE = PU.USER_TYPE\n" );
			sbSql.append("AND U .USER_STATUS = '10011001'\n" );
			if(Constant.PURCHASE_TYPE_01.toString().equals(SUPERIOR_PURCHASING_SELECT)){
				sbSql.append("AND PF.FIX_VALUE = '10'\n" );
			}else{
				sbSql.append("AND PF.FIX_VALUE = '11'\n" );
			}
			sbSql.append("AND PU.STATE = 10011001\n" );
			sbSql.append("AND PF.FIX_GOUPTYPE = 92251001\n" );
			sbSql.append("ORDER BY\n" );
			sbSql.append("  pf.fix_name\n" );
			sbSql.append(")TT\n" );
			sbSql.append("WHERE\n" );
			sbSql.append("  TT.USER_ID = '"+CommonUtils.checkNull(loginUser.getUserId())+"'\n");
			Map<String,Object> map = pageQueryMap(sbSql.toString(), params, getFunName());
			String USERC = CommonUtils.checkNull(map.get("USERC"));
			if(Long.valueOf(USERC)<=0){
				return new PageResult<Map<String,Object>>();
			}
		}
		
		sql.append("SELECT\n");
		sql.append("  T1.ID AS PLAN_ID,\n");
		sql.append("  T1.PLAN_NO,\n");
		sql.append("  T1.MONTH_DATE,\n");
		sql.append("  T1.IS_SUBMIT,\n");
		sql.append("  TO_CHAR(T1.SUBMIT_DATE,'YYYY-MM-DD') AS SUBMIT_DATE,\n");
		sql.append("  T2.ID AS PLAN_LINE_ID,\n");
		sql.append("  T2.PLAN_MONTH_ONE,\n");
		sql.append("  T2.PLAN_MONTH_TOW,\n");
		sql.append("  T2.PLAN_MONTH_THREE,\n");
		sql.append("  T2.WEEK_ONE,\n");
		sql.append("  T2.WEEK_TOW,\n");
		sql.append("  T2.WEEK_THREE,\n");
		sql.append("  T2.WEEK_FOUR,\n");
		sql.append("  T2.WEEK_FIVE,\n");
		sql.append("  T2.FIRST_CONFIRM_STATE,\n");
		sql.append("  TO_CHAR(T2.FIRST_CONFIRM_DATE,'YYYY-MM-DD') AS FIRST_CONFIRM_DATE,\n");
		sql.append("  T2.FIRSR_CONFIRM_BY,\n");
		sql.append("  T2.FIRST_CONFIRM_REMARK,\n");
		sql.append("  T2.FIRST_CONFIRM_NUM,\n");
		sql.append("  T2.LAST_CONFIRM_STATE,\n");
		sql.append("  TO_CHAR(T2.LAST_CONFIRM_DATE,'YYYY-MM-DD') AS LAST_CONFIRM_DATE,\n");
		sql.append("  T2.LAST_CONFIRM_BY,\n");
		sql.append("  T2.LAST_CONFIRM_REMARK,\n");
		sql.append("  T2.LAST_CONFIRM_NUM,\n");
		sql.append("  TPD.PART_ID,\n");
		sql.append("  TPD.PART_OLDCODE,\n");
		sql.append("  TPD.PART_CNAME,\n");
		sql.append("  TPD.PART_CODE,\n");
		sql.append("  TPD.PART_TYPE,\n");
		sql.append("  F_GET_TCCODE_DESC(TPD.PART_TYPE) AS PART_TYPE_CN,\n");
		sql.append("  TPD.PRODUCE_STATE,\n");
		sql.append("  TPD.LC,\n");
		sql.append("  TPD.PRODUCE_FAC,\n");
		sql.append("  TPD.SUPERIOR_PURCHASING,\n");
		sql.append("  T2.ORDER_PERIOD,\n");
		sql.append("  F_GET_TCCODE_DESC(TPD.ORDER_PERIOD) AS ORDER_PERIOD_CN,\n");
		sql.append("  TPD.BUY_MIN_PKG\n");
		sql.append("FROM\n");
		sql.append("  TT_PART_PLAN_SCROLL T1,\n");
		sql.append("  TT_PART_PLAN_SCROLL_DEL T2,\n");
		sql.append("  TT_PART_DEFINE TPD\n");
		sql.append("WHERE\n");
		sql.append("  T1.ID = T2.PLAN_ID\n");
		sql.append("  AND T2.PART_ID = TPD.PART_ID\n");
		sql.append("  AND T1.IS_SUBMIT = 10041001\n");
		//sql.append("  AND NOT EXISTS(SELECT 1 FROM TT_PART_SPLIT_DEFINE SP WHERE SP.PART_ID = TPD.PART_ID)--不显示总成件，只显示拆分的分总成件\n");
		
		if(StringUtil.notNull(SUPERIOR_PURCHASING_SELECT)){
			sql.append("  AND TPD.SUPERIOR_PURCHASING = ?\n");
			params.add(SUPERIOR_PURCHASING_SELECT);
		}
		
		//if(Constant.PURCHASE_TYPE_03.toString().equals(SUPERIOR_PURCHASING_SELECT)){
			//sql.append("  AND T2.VENDER_ID = '"+CommonUtils.checkNull(loginUser.getVenderId())+"'\n");
		//}
		
		if(StringUtil.notNull(yearMonth)){
			sql.append("  AND T1.MONTH_DATE LIKE ?\n");
			params.add("%"+yearMonth+"%");
		}
		
		if(StringUtil.notNull(PLAN_NO_SELECT)){
			sql.append("  AND T1.PLAN_NO LIKE ?\n");
			params.add("%"+PLAN_NO_SELECT+"%");
		}
		if(StringUtil.notNull(FIRST_CONFIRM_STATE_SELECT)){
			sql.append("  AND T2.FIRST_CONFIRM_STATE = ?\n");
			params.add(FIRST_CONFIRM_STATE_SELECT);
		}
		if(StringUtil.notNull(PART_OLDCODE_SELECT)){
			sql.append("  AND TPD.PART_OLDCODE LIKE ?\n");
			params.add("%"+PART_OLDCODE_SELECT+"%");
		}
		if(StringUtil.notNull(PART_CNAME_SELECT)){
			sql.append("  AND TPD.PART_CNAME LIKE ?\n");
			params.add("%"+PART_CNAME_SELECT+"%");
		}
		if(StringUtil.notNull(PART_CODE_SELECT)){
			sql.append("  AND TPD.PART_CODE LIKE ?\n");
			params.add("%"+PART_CODE_SELECT+"%");
		}
		
		sql.append("ORDER BY\n");
		sql.append("  T1.PLAN_NO ASC,\n");
		sql.append("  TPD.PART_OLDCODE ASC\n");

		PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), params, getFunName(), pageSize, curPage);
		return ps;
	}
	/**
	 * 查询待确认计划的备件数据（计划室）
	 * @param request
	 * @param loginUser
	 * @param curPage
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public PageResult<Map<String, Object>> getPartPlanValidationInfoByJhs(RequestWrapper request, AclUserBean loginUser, Integer curPage, Integer pageSize) throws Exception{
		StringBuffer sql = new StringBuffer();
		List<Object> params = new ArrayList<Object>();
		
		String PLAN_YEAR_SELECT = request.getParamValue("PLAN_YEAR_SELECT");
		String PLAN_MONTH_SELECT = request.getParamValue("PLAN_MONTH_SELECT");
		String yearMonth = CommonUtils.checkNull(PLAN_YEAR_SELECT)+CommonUtils.checkNull(PLAN_MONTH_SELECT);
		
		String PLAN_NO_SELECT = request.getParamValue("PLAN_NO_SELECT");
		String LAST_CONFIRM_STATE_SELECT = request.getParamValue("LAST_CONFIRM_STATE_SELECT");
		
		String PART_OLDCODE_SELECT = request.getParamValue("PART_OLDCODE_SELECT");
		String PART_CNAME_SELECT = request.getParamValue("PART_CNAME_SELECT");
		String PART_CODE_SELECT = request.getParamValue("PART_CODE_SELECT");
		
		String FIRST_CONFIRM_STATE_SELECT = request.getParamValue("FIRST_CONFIRM_STATE_SELECT");
		String GYS_UPDATE_FIRST = request.getParamValue("GYS_UPDATE_FIRST");
		
		sql.append("SELECT\n");
		sql.append("  T1.ID AS PLAN_ID,\n");
		sql.append("  T1.PLAN_NO,\n");
		sql.append("  T1.MONTH_DATE,\n");
		sql.append("  T1.IS_SUBMIT,\n");
		sql.append("  TO_CHAR(T1.SUBMIT_DATE,'YYYY-MM-DD') AS SUBMIT_DATE,\n");
		sql.append("  T2.ID AS PLAN_LINE_ID,\n");
		sql.append("  T2.PLAN_MONTH_ONE,\n");
		sql.append("  T2.PLAN_MONTH_TOW,\n");
		sql.append("  T2.PLAN_MONTH_THREE,\n");
		sql.append("  T2.WEEK_ONE,\n");
		sql.append("  T2.WEEK_TOW,\n");
		sql.append("  T2.WEEK_THREE,\n");
		sql.append("  T2.WEEK_FOUR,\n");
		sql.append("  NVL(T2.WEEK_FIVE,0) AS WEEK_FIVE,\n");
		sql.append("  T2.FIRST_CONFIRM_STATE,\n");
		sql.append("  TO_CHAR(T2.FIRST_CONFIRM_DATE,'YYYY-MM-DD') AS FIRST_CONFIRM_DATE,\n");
		sql.append("  T2.FIRSR_CONFIRM_BY,\n");
		sql.append("  T2.FIRST_CONFIRM_REMARK,\n");
		sql.append("  T2.FIRST_CONFIRM_NUM,\n");
		sql.append("  T2.LAST_CONFIRM_STATE,\n");
		sql.append("  TO_CHAR(T2.LAST_CONFIRM_DATE,'YYYY-MM-DD') AS LAST_CONFIRM_DATE,\n");
		sql.append("  T2.LAST_CONFIRM_BY,\n");
		sql.append("  T2.LAST_CONFIRM_REMARK,\n");
		sql.append("  T2.LAST_CONFIRM_NUM,\n");
		sql.append("  TPD.PART_ID,\n");
		sql.append("  TPD.PART_OLDCODE,\n");
		sql.append("  TPD.PART_CNAME,\n");
		sql.append("  TPD.PART_CODE,\n");
		sql.append("  TPD.PART_TYPE,\n");
		sql.append("  TPD.PRODUCE_STATE,\n");
		sql.append("  TPD.LC,\n");
		sql.append("  TPD.PRODUCE_FAC,\n");
		sql.append("  TPD.SUPERIOR_PURCHASING,\n");
		sql.append("  TU1.NAME AS PLANER_ID_CN,\n");
		sql.append("  TU2.NAME AS BUYER_ID_CN,\n");
		sql.append("  T2.ORDER_PERIOD\n");
		sql.append("FROM\n");
		sql.append("  TT_PART_PLAN_SCROLL T1,\n");
		sql.append("  TT_PART_PLAN_SCROLL_DEL T2,\n");
		sql.append("  TT_PART_DEFINE TPD,\n");
		sql.append("  TC_USER TU1,\n");
		sql.append("  TC_USER TU2\n");
		
		sql.append("WHERE\n");
		sql.append("  T1.ID = T2.PLAN_ID\n");
		sql.append("  AND T2.PART_ID = TPD.PART_ID\n");
		sql.append("  AND T1.IS_SUBMIT = 10041001\n");
		sql.append("  AND NOT EXISTS(SELECT 1 FROM TT_PART_SPLIT_DEFINE SP WHERE SP.PART_ID = TPD.PART_ID)--不显示总成件，只显示拆分的分总成件\n");
		sql.append("  AND TPD.PLANER_ID = TU1.USER_ID(+)\n");
		sql.append("  AND TPD.BUYER_ID = TU2.USER_ID(+)\n");
		
		if(StringUtil.notNull(FIRST_CONFIRM_STATE_SELECT)){
			sql.append("  AND T2.FIRST_CONFIRM_STATE = ?\n");
			params.add(FIRST_CONFIRM_STATE_SELECT);
		}
		
		if(StringUtil.notNull(yearMonth)){
			sql.append("  AND T1.MONTH_DATE LIKE ?\n");
			params.add("%"+yearMonth+"%");
		}
		
		if(StringUtil.notNull(PLAN_NO_SELECT)){
			sql.append("  AND T1.PLAN_NO LIKE ?\n");
			params.add("%"+PLAN_NO_SELECT+"%");
		}
		if(StringUtil.notNull(LAST_CONFIRM_STATE_SELECT)){
			sql.append("  AND T2.LAST_CONFIRM_STATE = ?\n");
			params.add(LAST_CONFIRM_STATE_SELECT);
		}
		if(StringUtil.notNull(PART_OLDCODE_SELECT)){
			sql.append("  AND TPD.PART_OLDCODE LIKE ?\n");
			params.add("%"+PART_OLDCODE_SELECT+"%");
		}
		if(StringUtil.notNull(PART_CNAME_SELECT)){
			sql.append("  AND TPD.PART_CNAME LIKE ?\n");
			params.add("%"+PART_CNAME_SELECT+"%");
		}
		if(StringUtil.notNull(PART_CODE_SELECT)){
			sql.append("  AND TPD.PART_CODE LIKE ?\n");
			params.add("%"+PART_CODE_SELECT+"%");
		}
		if(StringUtil.notNull(GYS_UPDATE_FIRST)){
			if(Constant.IF_TYPE_YES.toString().equals(GYS_UPDATE_FIRST)){
				sql.append("  AND NVL(T2.PLAN_MONTH_ONE,0) <> NVL(T2.FIRST_CONFIRM_NUM,0)");
			}else{
				sql.append("  AND NVL(T2.PLAN_MONTH_ONE,0) = NVL(T2.FIRST_CONFIRM_NUM,0)");
			}
			
		}
		
		sql.append("ORDER BY\n");
		sql.append("  T1.PLAN_NO ASC,\n");
		sql.append("  TPD.PART_OLDCODE ASC\n");

		PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), params, getFunName(), pageSize, curPage);
		return ps;
	}
	
	/**
	 * 收货库房，供应商，订单周期,拆分总成件
	 * @param planId
	 * @param logonUser
	 */
	@SuppressWarnings("unchecked")
	public void allSub(String planId, AclUserBean logonUser) {
        //改状态销售
		StringBuffer sql = new StringBuffer();
        sql.append("update TT_PART_PLAN_SCROLL_DEL sd\n");
        sql.append("   set sd.sales_status    = 92981002,\n");
        sql.append("       sd.purchase_status = 92981001,\n");
        sql.append("       sd.Sales_Num = sd.plan_month_one\n");
        sql.append(" where exists (select 1\n");
        sql.append("          from tt_part_plan_scroll ps\n");
        sql.append("         where ps.id = sd.plan_id\n");
        sql.append("           and ps.id = '" + planId + "')\n");
        sql.append("       and sd.sales_status! = 92981005 and  sd.sales_status! = 92981006  and sd.purchase_status is null\n");
        this.update(sql.toString(), null);

        
        //收货库房，供应商，订单周期
        StringBuffer sbSql = new StringBuffer();
        sbSql.append("UPDATE TT_PART_PLAN_SCROLL_DEL TPSD\n");
        sbSql.append("   SET (TPSD.WH_ID,TPSD.ORDER_PERIOD,TPSD.VENDER_ID) =\n");
        sbSql.append("       (SELECT D.WH_ID,D.ORDER_PERIOD,PKG_PART_UTIL.F_GET_PART_VENDER_ROWNUM1(D.PART_ID,1) FROM TT_PART_DEFINE D WHERE D.PART_ID = TPSD.PART_ID)\n");
        sbSql.append("WHERE TPSD.PLAN_ID = '"+planId+"'\n");
		this.update(sbSql.toString(), null);
        
        //delErrPlan(planNo);
        //总成件不需要更新错误标志 mod by yuan 20161107
        StringBuffer sql4 = new StringBuffer();
        sql4.append("SELECT\n");
        sql4.append("  T.PLAN_LINE_ID\n");
        sql4.append("FROM\n");
        sql4.append("  VW_PART_PLAN_IMP_ERR T,\n");
        sql4.append("  TT_PART_PLAN_SCROLL_DEL DTL\n");
        sql4.append("WHERE\n");
        sql4.append("  T.PLAN_LINE_ID = DTL.ID\n");
        sql4.append("  AND T.PLAN_ID = '" + planId + "'\n");
        sql4.append("  AND DTL.SALES_STATUS != 92981007\n");

        List<Map<String, Object>> lineIdList = this.pageQuery(sql4.toString(), null, getFunName());
        if(lineIdList!=null && !lineIdList.isEmpty() && lineIdList.size()>0){
        	for (int j = 0; j < lineIdList.size(); j++) {
        		Map<String, Object> mapTemp = lineIdList.get(j);
        		StringBuffer sqlNew2 = new StringBuffer();
                sqlNew2.append("UPDATE TT_PART_PLAN_SCROLL_DEL D\n");
                sqlNew2.append("  SET D.SALES_STATUS = 92981005,--销售导入错误\n");
                sqlNew2.append("      D.PURCHASE_STATUS = ''\n");
                sqlNew2.append("WHERE 1=1\n");
                sqlNew2.append("  AND D.ID = '"+CommonUtils.checkNull(mapTemp.get("PLAN_LINE_ID"))+"'\n");
                this.update(sqlNew2.toString(), null);
			}
        	
        }
    }
	/**
	 * 查询可以生成采购订的数据
	 * @param request
	 * @param loginUser
	 * @param curPage
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public PageResult<Map<String, Object>> getPartPlanValidationInfoByJhsPur(RequestWrapper request, AclUserBean loginUser, Integer curPage, Integer pageSize) throws Exception{
		StringBuffer sql = new StringBuffer();
		List<Object> params = new ArrayList<Object>();
		
		String PLAN_YEAR_SELECT = request.getParamValue("PLAN_YEAR_SELECT");
		String PLAN_MONTH_SELECT = request.getParamValue("PLAN_MONTH_SELECT");
		String yearMonth = CommonUtils.checkNull(PLAN_YEAR_SELECT)+CommonUtils.checkNull(PLAN_MONTH_SELECT);
		
		String PLAN_NO_SELECT = request.getParamValue("PLAN_NO_SELECT");
		String LAST_CONFIRM_STATE_SELECT = request.getParamValue("LAST_CONFIRM_STATE_SELECT");
		
		String PART_OLDCODE_SELECT = request.getParamValue("PART_OLDCODE_SELECT");
		String PART_CNAME_SELECT = request.getParamValue("PART_CNAME_SELECT");
		String PART_CODE_SELECT = request.getParamValue("PART_CODE_SELECT");
		
		String FIRST_CONFIRM_STATE_SELECT = request.getParamValue("FIRST_CONFIRM_STATE_SELECT");
		String STATUS_SELECT = request.getParamValue("STATUS_SELECT");
		String ORDER_PERIOD_SELECT = request.getParamValue("ORDER_PERIOD_SELECT");
		
		String GYS_UPDATE_FIRST = request.getParamValue("GYS_UPDATE_FIRST");
		
		sql.append("SELECT\n");
		sql.append("  T1.ID AS PLAN_ID,\n");
		sql.append("  T1.PLAN_NO,\n");
		sql.append("  T1.MONTH_DATE,\n");
		sql.append("  T1.IS_SUBMIT,\n");
		sql.append("  TO_CHAR(T1.SUBMIT_DATE,'YYYY-MM-DD') AS SUBMIT_DATE,\n");
		sql.append("  T2.ID AS PLAN_LINE_ID,\n");
		sql.append("  T2.PLAN_MONTH_ONE,\n");
		sql.append("  T2.PLAN_MONTH_TOW,\n");
		sql.append("  T2.PLAN_MONTH_THREE,\n");
		sql.append("  T2.WEEK_ONE,\n");
		sql.append("  T2.WEEK_TOW,\n");
		sql.append("  T2.WEEK_THREE,\n");
		sql.append("  T2.WEEK_FOUR,\n");
		sql.append("  NVL(T2.WEEK_FIVE,0) AS WEEK_FIVE,\n");
		sql.append("  T2.FIRST_CONFIRM_STATE,\n");
		sql.append("  TO_CHAR(T2.FIRST_CONFIRM_DATE,'YYYY-MM-DD') AS FIRST_CONFIRM_DATE,\n");
		sql.append("  T2.FIRSR_CONFIRM_BY,\n");
		sql.append("  T2.FIRST_CONFIRM_REMARK,\n");
		sql.append("  T2.FIRST_CONFIRM_NUM,\n");
		sql.append("  T2.LAST_CONFIRM_STATE,\n");
		sql.append("  TO_CHAR(T2.LAST_CONFIRM_DATE,'YYYY-MM-DD') AS LAST_CONFIRM_DATE,\n");
		sql.append("  T2.LAST_CONFIRM_BY,\n");
		sql.append("  T2.LAST_CONFIRM_REMARK,\n");
		sql.append("  T2.LAST_CONFIRM_NUM,\n");
		sql.append("  TPD.PART_ID,\n");
		sql.append("  TPD.PART_OLDCODE,\n");
		sql.append("  TPD.PART_CNAME,\n");
		sql.append("  TPD.PART_CODE,\n");
		sql.append("  TPD.PART_TYPE,\n");
		sql.append("  TPD.PRODUCE_STATE,\n");
		sql.append("  TPD.LC,\n");
		sql.append("  TPD.PRODUCE_FAC,\n");
		sql.append("  TPD.SUPERIOR_PURCHASING,\n");
		sql.append("  TPD.BUY_MIN_PKG,\n");
		sql.append("  TU1.NAME AS PLANER_ID_CN,\n");
		sql.append("  TU2.NAME AS BUYER_ID_CN,\n");
		sql.append("  T2.STATUS,\n");
		sql.append("  T2.IS_WEEK1,\n");
		sql.append("  T2.IS_WEEK2,\n");
		sql.append("  T2.IS_WEEK3,\n");
		sql.append("  T2.IS_WEEK4,\n");
		sql.append("  T2.IS_WEEK5,\n");
		sql.append("  T2.ORDER_PERIOD,\n");
		sql.append("  PKG_PART_UTIL.F_GET_MONDAY_COUNT(T1.MONTH_DATE) AS WEEK_COUNT,--计划月份共有几个周一，共几周\n");
		sql.append("  TO_CHAR(NEXT_DAY(SYSDATE+2, '星期一') ,'YYYYMM') AS NEXT_WEEK_ONE_MONTH,--下个周一所在的月份\n");
		sql.append("  TO_CHAR(NEXT_DAY(SYSDATE+2, '星期一') ,'YYYY-MM-DD') AS NEXT_WEEK_ONE_DATE,--下个周一所在的时间\n");
		sql.append("  PKG_PART_UTIL.F_GET_WEEK_ONE_XH(TO_CHAR(NEXT_DAY(SYSDATE+2, '星期一') ,'YYYY-MM-DD')) AS NEXT_WEEK_ONE_NUM--下个周一是所在月的第几周\n");
		sql.append("FROM\n");
		sql.append("  TT_PART_PLAN_SCROLL T1,\n");
		sql.append("  TT_PART_PLAN_SCROLL_DEL T2,\n");
		sql.append("  TT_PART_DEFINE TPD,\n");
		sql.append("  TC_USER TU1,\n");
		sql.append("  TC_USER TU2\n");
		
		sql.append("WHERE\n");
		sql.append("  T1.ID = T2.PLAN_ID\n");
		sql.append("  AND T2.PART_ID = TPD.PART_ID\n");
		sql.append("  AND T1.IS_SUBMIT = 10041001\n");
		sql.append("  AND NOT EXISTS(SELECT 1 FROM TT_PART_SPLIT_DEFINE SP WHERE SP.PART_ID = TPD.PART_ID)--不显示总成件，只显示拆分的分总成件\n");
		sql.append("  AND TPD.PLANER_ID = TU1.USER_ID(+)\n");
		sql.append("  AND TPD.BUYER_ID = TU2.USER_ID(+)\n");
		
		if(StringUtil.notNull(FIRST_CONFIRM_STATE_SELECT)){
			sql.append("  AND T2.FIRST_CONFIRM_STATE = ?\n");
			params.add(FIRST_CONFIRM_STATE_SELECT);
		}
		
		if(StringUtil.notNull(yearMonth)){
			sql.append("  AND T1.MONTH_DATE LIKE ?\n");
			params.add("%"+yearMonth+"%");
		}
		
		if(StringUtil.notNull(PLAN_NO_SELECT)){
			sql.append("  AND T1.PLAN_NO LIKE ?\n");
			params.add("%"+PLAN_NO_SELECT+"%");
		}
		if(StringUtil.notNull(LAST_CONFIRM_STATE_SELECT)){
			sql.append("  AND T2.LAST_CONFIRM_STATE = ?\n");
			params.add(LAST_CONFIRM_STATE_SELECT);
		}
		if(StringUtil.notNull(PART_OLDCODE_SELECT)){
			sql.append("  AND TPD.PART_OLDCODE LIKE ?\n");
			params.add("%"+PART_OLDCODE_SELECT+"%");
		}
		if(StringUtil.notNull(PART_CNAME_SELECT)){
			sql.append("  AND TPD.PART_CNAME LIKE ?\n");
			params.add("%"+PART_CNAME_SELECT+"%");
		}
		if(StringUtil.notNull(PART_CODE_SELECT)){
			sql.append("  AND TPD.PART_CODE LIKE ?\n");
			params.add("%"+PART_CODE_SELECT+"%");
		}
		
		if(StringUtil.notNull(STATUS_SELECT)){
			if(Constant.IF_TYPE_YES.toString().equals(STATUS_SELECT)){
				/*sql.append("  AND T2.STATUS = '"+Constant.SCROLL_PLAN_STATUS_02+"'\n");*/
			}else{
				sql.append("  AND (T2.STATUS IS NULL ')\n"); //OR T2.STATUS <> '"+Constant.SCROLL_PLAN_STATUS_02+"
			}
			
		}
		if(StringUtil.notNull(ORDER_PERIOD_SELECT)){
			sql.append("  AND T2.ORDER_PERIOD = '"+ORDER_PERIOD_SELECT+"'\n");
		}
		
		if(StringUtil.notNull(GYS_UPDATE_FIRST)){
			if(Constant.IF_TYPE_YES.toString().equals(GYS_UPDATE_FIRST)){
				sql.append("  AND T2.PLAN_MONTH_ONE <> T2.LAST_CONFIRM_NUM\n");
			}else{
				sql.append("  AND T2.PLAN_MONTH_ONE = T2.LAST_CONFIRM_NUM\n");
			}
			
		}
		
		sql.append("ORDER BY\n");
		sql.append("  T1.PLAN_NO ASC,\n");
		sql.append("  TPD.PART_OLDCODE ASC\n");

		PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), params, getFunName(), pageSize, curPage);
		return ps;
	}
	/**
	 * 采购订单查询
	 * @param request
	 * @param loginUser
	 * @param curPage
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public PageResult<Map<String, Object>> getPurchaseOrderInit(RequestWrapper request, AclUserBean loginUser, Integer curPage, Integer pageSize) throws Exception{
		StringBuffer sql = new StringBuffer();
		List<Object> params = new ArrayList<Object>();
		
		String ORDER_CODE = request.getParamValue("ORDER_CODE");
		String VENDER_NAME = request.getParamValue("VENDER_NAME");
		String sCreateDate = request.getParamValue("sCreateDate");
		String eCreateDate = request.getParamValue("eCreateDate");
		
		String PART_OLDCODE = request.getParamValue("PART_OLDCODE");
		String PART_CNAME = request.getParamValue("PART_CNAME");
		String PART_CODE = request.getParamValue("PART_CODE");
		
		
		sql.append("SELECT\n");
		sql.append("  POP.PO_ID,\n");
		sql.append("  POP.ORDER_ID,\n");
		sql.append("  POP.ORDER_CODE,\n");
		sql.append("  POP.PLAN_ID,\n");
		sql.append("  POP.PLINE_ID,\n");
		sql.append("  POP.PLAN_CODE,\n");
		sql.append("  POP.PLANER_ID,\n");
		sql.append("  POP.PLANER,\n");
		sql.append("  POP.BUYER_ID,\n");
		sql.append("  POP.BUYER,\n");
		sql.append("  POP.PART_ID,\n");
		sql.append("  POP.PART_OLDCODE,\n");
		sql.append("  POP.PART_CNAME,\n");
		sql.append("  POP.PART_CODE,\n");
		sql.append("  POP.PART_TYPE,--01，02，03\n");
		sql.append("  POP.PRODUCE_FAC,--采购方式\n");
		sql.append("  POP.UNIT,\n");
		sql.append("  POP.VENDER_ID,\n");
		sql.append("  POP.VENDER_CODE,\n");
		sql.append("  POP.VENDER_NAME,\n");
		sql.append("  POP.PLAN_QTY,--计划数量\n");
		sql.append("  POP.BUY_QTY,--采购数量\n");
		sql.append("  POP.BUY_PRICE,--采购单价\n");
		sql.append("  POP.BUY_AMOUNT,--采购金额\n");
		sql.append("  (POP.CHECK_QTY-NVL(POP.IN_QTY,0)) AS DIN_QTY,--待验收数量\n");
		sql.append("  POP.IN_QTY,--已验收数量\n");
		sql.append("  TO_CHAR(POP.FORECAST_DATE,'YYYY-MM-DD')  FORECAST_DATE,--预计到货日期\n");
		sql.append("  POP.WH_ID,\n");
		sql.append("  POP.WH_NAME,\n");
		sql.append("  POP.REMARK,\n");
		sql.append("  TO_CHAR(POP.CREATE_DATE,'YYYY-MM-DD') AS CREATE_DATE,\n");
		sql.append("  POP.ORG_ID,\n");
		sql.append("  POP.STATE,\n");
		sql.append("  POP.LOC_ID,\n");
		sql.append("  POP.LOC_CODE\n");
		sql.append("FROM\n");
		sql.append("  TT_PART_OEM_PO POP\n");
		sql.append("WHERE\n");
		sql.append("  POP.ORG_ID = "+Constant.OEM_ACTIVITIES+"\n");
		sql.append("  AND POP.STATE = "+Constant.PART_PURCHASE_ORDERCHK_STATUS_01+"\n");
		if(StringUtil.notNull(ORDER_CODE)){
			sql.append("  AND POP.ORDER_CODE LIKE ?\n");
			params.add("%"+ORDER_CODE.toUpperCase()+"%");
		}
		if(StringUtil.notNull(VENDER_NAME)){
			sql.append("  AND POP.VENDER_NAME LIKE ?\n");
			params.add("%"+VENDER_NAME+"%");
		}
		
		if(StringUtil.notNull(sCreateDate)){
			sql.append("  AND TO_CHAR(POP.CREATE_DATE,'YYYY-MM-DD') >= ?\n");
			params.add(sCreateDate);
		}
		if(StringUtil.notNull(eCreateDate)){
			sql.append("  AND TO_CHAR(POP.CREATE_DATE,'YYYY-MM-DD') <= ?\n");
			params.add(eCreateDate);
		}
		if(StringUtil.notNull(PART_OLDCODE)){
			sql.append("  AND POP.PART_OLDCODE LIKE ?\n");
			params.add("%"+PART_OLDCODE.toUpperCase()+"%");
		}
		if(StringUtil.notNull(PART_CNAME)){
			sql.append("  AND POP.PART_CNAME LIKE ?\n");
			params.add("%"+PART_CNAME+"%");
		}
		if(StringUtil.notNull(PART_CODE)){
			sql.append("  AND POP.PART_CODE LIKE ?\n");
			params.add("%"+PART_CODE.toUpperCase()+"%");
		}
		
		sql.append("  AND (NVL(POP.CHECK_QTY,0) - NVL(POP.IN_QTY,0)) > 0 \n");
		
		sql.append(" ORDER BY POP.ORDER_CODE ASC,POP.CREATE_DATE ASC\n");

		PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), params, getFunName(), pageSize, curPage);
		return ps;
	}
	
	/**
	 * 是否锁定
	 * @param partId
	 * @param strWhId
	 * @param dealerId
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public boolean isLocked(Long partId, Long strWhId, Long dealerId) throws Exception {
		Map map = new HashMap();
        try {
            StringBuffer sql = new StringBuffer("");
            sql.append("SELECT V.IS_LOCKED  FROM VW_PART_STOCK V WHERE V.ORG_ID =");
            sql.append(dealerId);
            sql.append(" AND V.PART_ID = ").append(partId);
            sql.append(" AND V.WH_ID = ").append(strWhId);
            map = pageQueryMap(sql.toString(), null, getFunName());
            if (map != null && ((BigDecimal) map.get("IS_LOCKED")).intValue() == 1) {
                return true;
            }
        } catch (Exception e) {
            throw e;
        }
        return false;
	}
	/**
	 * 查询当前备件信息及其货位信息
	 * @param partId
	 * @param orgId
	 * @param whId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map queryPartAndLocationInfo(Long partId, Long orgId, Long whId) throws Exception{
		try {
            StringBuilder sql = new StringBuilder("SELECT A.IS_DIRECT, A.BUYER_ID, B.LOC_ID, B.LOC_CODE,B.LOC_NAME FROM TT_PART_DEFINE A, TT_PART_LOACTION_DEFINE B");
            sql.append(" WHERE A.PART_ID(+) = B.PART_ID");
            sql.append(" AND B.ORG_ID =").append(orgId);
            sql.append("  AND B.WH_ID =").append(whId);
            sql.append(" AND B.PART_ID=").append(partId);
            sql.append(" AND B.STATE=").append(Constant.STATUS_ENABLE);
            sql.append(" AND B.STATUS=1");
            logger.info("--------sql=" + sql);
            return pageQueryMap(sql.toString(), null, getFunName());
        } catch (Exception e) {
            throw e;
        }
	}
	/**
	 * 入库的时候要判断相同验收单号、相同备件、相同库房是否已经入过库了,
	 * 如果已经入库,那再次入库就更新入库数量,否则新增入库信息
	 * @param orderCode
	 * @param whId
	 * @param partId
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> getInfoByChkCodeAndWhIdAndPartId(String orderCode, Long whId, Long partId) throws Exception{
		try {
            StringBuffer sql = new StringBuffer("");
            sql.append("SELECT T.IN_ID,T.IN_CODE,T.IN_QTY,T.BUY_PRICE,T.IN_AMOUNT,T.BUY_PRICE_NOTAX,T.IN_AMOUNT_NOTAX FROM TT_PART_PO_IN T WHERE T.order_CODE=");
            sql.append("'" + orderCode + "'");
            sql.append(" AND T.WH_ID= ").append(whId);
            sql.append(" AND T.PART_ID=").append(partId);
            return pageQueryMap(sql.toString(), null, getFunName());
        } catch (Exception e) {
            throw e;
        }
	}
	@SuppressWarnings("unchecked")
	public Map<String, Object> getPoInfoByOrderAndPart(RequestWrapper request) throws Exception{
		StringBuffer sql = new StringBuffer();
		List<Object> params = new ArrayList<Object>();
		
		String orderCode = request.getParamValue("orderCode");
		
		sql.append("SELECT\n");
		sql.append("  PC.CON_ID,\n");
		sql.append("  PC.PART_ID,\n");
		sql.append("  PC.PART_OLDCODE,\n");
		sql.append("  PC.PART_CNAME,\n");
		sql.append("  PC.PART_CODE,\n");
		sql.append("  POP.CHECK_QTY,\n");
		sql.append("  PC.IN_QTY,\n");
		sql.append("  (NVL(PC.CON_QTY,0) - NVL(PC.IN_QTY,0)) AS KIN_QTY\n");
		sql.append("FROM\n");
		sql.append("  TT_PART_OEM_PO_CONFIRM PC,\n");
		sql.append("  TT_PART_OEM_PO POP\n");
		sql.append("WHERE\n");
		sql.append("  PC.PO_ID = POP.PO_ID\n");
		sql.append("  AND PC.STATUS = 10011001\n");
		sql.append("  AND PC.CON_ID = '"+orderCode+"'\n");
		
		Map<String, Object> map = pageQueryMap(sql.toString(), params, getFunName());
		
		return map;
	}

	/**
	 * 采购订单汇总
	 * @param request
	 * @param loginUser
	 * @param curPage
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public PageResult<Map<String, Object>> getPurcharOrderHz(RequestWrapper request, AclUserBean loginUser, Integer curPage, Integer pageSize) throws Exception{
		StringBuffer sql = new StringBuffer();
		List<Object> params = new ArrayList<Object>();
		
		String ORDER_CODE = request.getParamValue("ORDER_CODE");
		String PLAN_CODE = request.getParamValue("PLAN_CODE");
		String sCreateDate = request.getParamValue("sCreateDate");
		String eCreateDate = request.getParamValue("eCreateDate");
		
		String VENDER_CODE = request.getParamValue("VENDER_CODE");
		String VENDER_NAME = request.getParamValue("VENDER_NAME");
		String purUserId = request.getParamValue("purUserId");
		
		sql.append("SELECT\n");
		sql.append("  TEMP.*\n");
		sql.append("FROM(\n");
		sql.append("  SELECT\n");
		sql.append("    POP.ORDER_CODE,\n");
		sql.append("    POP.CREATE_DATE,\n");
		sql.append("    POP.PLAN_CODE,\n");
		sql.append("    POP.VENDER_CODE,\n");
		sql.append("    POP.VENDER_NAME,\n");
		sql.append("    COUNT(POP.PART_ID) AS PART_COUNT,\n");
		sql.append("    SUM(POP.CHECK_QTY) AS CHECK_QTY_SUM\n");
		sql.append("  FROM\n");
		sql.append("    TT_PART_OEM_PO POP\n");
		sql.append("  WHERE\n");
		sql.append("    1=1\n");
		if(StringUtil.notNull(ORDER_CODE)){
			sql.append("    AND POP.ORDER_CODE LIKE ?\n");
			params.add("%"+ORDER_CODE.trim().toUpperCase()+"%");
		}
		if(StringUtil.notNull(PLAN_CODE)){
			sql.append("    AND POP.PLAN_CODE LIKE ?\n");
			params.add("%"+PLAN_CODE.trim().toUpperCase()+"%");
		}
		if(StringUtil.notNull(sCreateDate)){
			sql.append("    AND TO_CHAR(POP.CREATE_DATE,'YYYY-MM-DD') >= ?\n");
			params.add(sCreateDate);
		}
		if(StringUtil.notNull(eCreateDate)){
			sql.append("    AND TO_CHAR(POP.CREATE_DATE,'YYYY-MM-DD') <= ?\n");
			params.add(eCreateDate);
		}
		
		if(StringUtil.notNull(VENDER_CODE)){
			sql.append("    AND POP.VENDER_CODE LIKE ?\n");
			params.add("%"+VENDER_CODE+"%");
		}
		if(StringUtil.notNull(VENDER_NAME)){
			sql.append("    AND POP.VENDER_NAME LIKE ?\n");
			params.add("%"+VENDER_NAME+"%");
		}
		if(StringUtil.notNull(purUserId)){
			sql.append("    AND POP.BUYER_ID = ?\n");
			params.add(purUserId);
		}
		
		if(StringUtil.notNull(loginUser.getDealerId())){
			/*if(loginUser.getPdealerType().equals(Constant.PART_SALE_PRICE_DEALER_TYPE_03)){
				sql.append("    AND POP.VENDER_ID = '"+loginUser.getVenderId()+"'\n");
			}*/
		}
		
		
		sql.append("  GROUP BY\n");
		sql.append("    POP.ORDER_CODE,\n");
		sql.append("    POP.CREATE_DATE,\n");
		sql.append("    POP.VENDER_CODE,\n");
		sql.append("    POP.VENDER_NAME,\n");
		sql.append("    POP.PLAN_CODE\n");
		sql.append(")TEMP\n");
		sql.append("ORDER BY\n");
		sql.append("  TEMP.CREATE_DATE DESC\n");

		PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), params, getFunName(), pageSize, curPage);
		return ps;
	}
	/**
	 * 根据订单号获取采购订单明细
	 * @param request
	 * @param loginUser
	 * @param curPage
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public PageResult<Map<String, Object>> getPurcharOrderMx(RequestWrapper request, AclUserBean loginUser, Integer curPage, Integer pageSize) throws Exception{
		StringBuffer sql = new StringBuffer();
		List<Object> params = new ArrayList<Object>();
		
		String ORDER_CODE = request.getParamValue("ORDER_CODE");
		
		String PART_OLDCODE = request.getParamValue("PART_OLDCODE");
		String PART_CNAME = request.getParamValue("PART_CNAME");
		String PART_CODE = request.getParamValue("PART_CODE");
		
		sql.append("SELECT\n");
		sql.append("  POP.*\n");
		sql.append("FROM\n");
		sql.append("  TT_PART_OEM_PO POP,\n");
		sql.append("  TT_PART_DEFINE TPD\n");
		sql.append("WHERE\n");
		sql.append("  POP.PART_ID = TPD.PART_ID\n");
		sql.append("  AND POP.ORDER_CODE = ?\n");
		params.add(ORDER_CODE);
		if(StringUtil.notNull(PART_OLDCODE)){
			sql.append("    AND POP.PART_OLDCODE LIKE ?\n");
			params.add("%"+PART_OLDCODE.trim().toUpperCase()+"%");
		}
		
		if(StringUtil.notNull(PART_CODE)){
			sql.append("    AND POP.PART_CODE LIKE ?\n");
			params.add("%"+PART_CODE.trim().toUpperCase()+"%");
		}
		
		if(StringUtil.notNull(PART_CNAME)){
			sql.append("    AND POP.PART_CNAME LIKE ?\n");
			params.add("%"+PART_CNAME.trim()+"%");
		}
		sql.append("ORDER BY\n");
		sql.append("  POP.PART_OLDCODE ASC\n");

		PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), params, getFunName(), pageSize, curPage);
		return ps;
	}
	/**
	 * 获取采购单主信息根据采购单号
	 * @param orderCode
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> getPurchaseOrderMainByOrderCode(String orderCode) throws Exception{
		StringBuffer sql = new StringBuffer();
		List<Object> params = new ArrayList<Object>();
		
		sql.append("SELECT\n");
		sql.append("    POP.ORDER_CODE,\n");
		sql.append("    POP.CREATE_DATE,\n");
		sql.append("    POP.PLAN_CODE,\n");
		sql.append("    POP.VENDER_CODE,\n");
		sql.append("    POP.VENDER_NAME,\n");

		sql.append("    COUNT(POP.PART_ID) AS PART_COUNT,\n");
		sql.append("    SUM(POP.CHECK_QTY) AS CHECK_QTY_SUM\n");
		sql.append("    ,\n");
		sql.append("    DECODE(\n");
		sql.append("      (SELECT COUNT(POPD.IN_DATE) FROM TT_PART_OEM_PO POPD WHERE POPD.ORDER_CODE = POP.ORDER_CODE),\n");
		sql.append("      (SELECT COUNT(POPD.PO_ID) FROM TT_PART_OEM_PO POPD WHERE POPD.ORDER_CODE = POP.ORDER_CODE),\n");
		sql.append("      (SELECT DISTINCT TO_CHAR(POPD.IN_DATE,'YYYY-MM-DD') FROM TT_PART_OEM_PO POPD WHERE POPD.ORDER_CODE = POP.ORDER_CODE),\n");
		sql.append("      TO_CHAR(SYSDATE,'YYYY-MM-DD')\n");
		sql.append("    )AS IN_DATE,\n");
		sql.append("    TO_CHAR(SYSDATE,'YYYY-MM-DD') AS PRINT_DATE\n");
		sql.append("  FROM\n");
		sql.append("    TT_PART_OEM_PO POP\n");
		sql.append("  WHERE\n");
		sql.append("    1=1\n");
		sql.append("    AND POP.ORDER_CODE = ?\n");
		params.add(orderCode);
		sql.append("  GROUP BY\n");
		sql.append("    POP.ORDER_CODE,\n");
		sql.append("    POP.CREATE_DATE,\n");
		sql.append("    POP.PLAN_CODE,\n");
		sql.append("    POP.VENDER_CODE,\n");
		sql.append("    POP.VENDER_NAME\n");
		Map<String, Object> map = pageQueryMap(sql.toString(), params, getFunName());
		return map;
	}
	@SuppressWarnings("unchecked")
	public List<Map<String,Object>> getPurchaseOrderMxByOrderCode(String orderCode) throws Exception{
		StringBuffer sql = new StringBuffer();
		List<Object> params = new ArrayList<Object>();
		
		sql.append("SELECT\n");
		sql.append("  POP.PART_ID,\n");
		sql.append("  POP.PART_OLDCODE,\n");
		sql.append("  POP.PART_CNAME,\n");
		sql.append("  TPD.PART_ENAME,\n");
		sql.append("  POP.PART_CODE,\n");
		sql.append("  POP.UNIT,\n");
		sql.append("  POP.CHECK_QTY,\n");
		sql.append("  POP.IN_QTY,\n");
		sql.append("  POP.REMARK,\n");
		sql.append("  TPD.MIDDLE_PACKAGE,--中包装数量\n");
		sql.append("  TPD.BOX_NUM,--装箱量：一个箱装的中包装数量（若无中包装，则为备件数量）\n");
		sql.append("  TPD.PALLET_NUM,--一个托装的箱子数\n");
		sql.append("  TPD.PALLET_BOX,--托盘箱（每层几个箱子-一个托盘几层）\n");
		sql.append("  MOD((POP.CHECK_QTY/decode(TPD.MIDDLE_PACKAGE,0,'1',TPD.MIDDLE_PACKAGE))/decode(TPD.BOX_NUM,0,'1',TPD.BOX_NUM),TPD.BOX_NUM)*TPD.BOX_NUM*NVL(TPD.MIDDLE_PACKAGE,'1') AS SYG,\n");
		sql.append("  FLOOR((POP.CHECK_QTY/decode(TPD.MIDDLE_PACKAGE,0,'1',TPD.MIDDLE_PACKAGE))/decode(TPD.BOX_NUM,0,'1',TPD.BOX_NUM)) AS BOXZ\n");
		sql.append("FROM\n");
		sql.append("  TT_PART_OEM_PO POP,\n");
		sql.append("  TT_PART_DEFINE TPD\n");
		sql.append("WHERE\n");
		sql.append("  POP.PART_ID = TPD.PART_ID\n");
		sql.append("  AND POP.ORDER_CODE = ?\n");
		params.add(orderCode);

		
		List<Map<String,Object>> list = pageQuery(sql.toString(), params, getFunName());
		return list;
	}
	
	/**
	 * 获取二维码信息组
	 * @param orderCode
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getPurcharOrderQrByOrderCode(AclUserBean loginUser,String inId,String partId) throws Exception {
		StringBuffer sql = new StringBuffer();
		List<Object> params = new ArrayList<Object>();
		//add 2017-05-17 add 调用过程产生单个备件的二维码信息
		ArrayList ins = new ArrayList();
        ins.add(0, inId);
        ins.add(1, loginUser.getUserId());
        ins.add(2, null);
        dao.callProcedure("PKG_PART_UTIL.PROC_CREATE_PO_QRCODE", ins, null);
		
        sql.append("select p.order_id,\n" );
		sql.append("       p.order_code,\n" );
		sql.append("       p.plan_id,\n" );
		sql.append("       p.buyer_id,\n" );
		sql.append("       p.buyer,\n" );
		sql.append("       p.part_type,\n" );
		sql.append("       p.produce_fac,\n" );
		sql.append("       p.wh_id,\n" );
		sql.append("       p.wh_name,\n" );
		sql.append("       p.remark,\n" );
		sql.append("       TO_CHAR(p.create_date, 'YYYY-MM-DD') as CREATE_DATE,\n" );
		sql.append("       p.state,\n" );
		sql.append("       p.status,\n" );
		sql.append("       p.pur_order_code,\n" );
		sql.append("       p.print_by,\n" );
		sql.append("       p.vender_id,\n" );
		sql.append("       p.buyer_type,\n" );
		sql.append("       TO_CHAR(p.PRINT_DATE, 'YYYY-MM-DD') as PRINT_DATE,\n" );
		sql.append("  (SELECT T.LOC_ID FROM TT_PART_LOACTION_DEFINE T WHERE T.PART_ID = p1.PART_ID AND T.ORG_ID = p1.ORG_ID AND T.WH_ID = p.WH_ID AND T.STATE = 10011001) AS LOC_ID,\n");
		sql.append("  (SELECT T.LOC_CODE FROM TT_PART_LOACTION_DEFINE T WHERE T.PART_ID = p1.PART_ID AND T.ORG_ID = p1.ORG_ID AND T.WH_ID = p.WH_ID AND T.STATE = 10011001) AS LOC_CODE,\n");
		sql.append("       p1.part_id,\n" );
		sql.append("       p1.part_code,\n" );
		sql.append("       p1.part_oldcode,\n" );
		sql.append("       p1.part_cname,\n" );
		sql.append("       p1.unit,\n" );
		sql.append("       p1.vender_name,\n" );
		sql.append("       p1.plan_qty,\n" );
		sql.append("       p1.buy_qty,\n" );
		sql.append("  TPD.MIDDLE_PACKAGE,--中包装数量\n");
		sql.append("  TPD.BOX_NUM,--装箱量：一个箱装的中包装数量（若无中包装，则为备件数量）\n");
		sql.append("  TPD.PALLET_NUM,--一个托装的箱子数\n");
		sql.append("  TPD.PALLET_BOX,--托盘箱（每层几个箱子-一个托盘几层）\n");
		sql.append("  MOD((p1.buy_qty/decode(TPD.MIDDLE_PACKAGE,0,'1',TPD.MIDDLE_PACKAGE))/decode(TPD.BOX_NUM,0,'1',TPD.BOX_NUM),TPD.BOX_NUM)*TPD.BOX_NUM*NVL(TPD.MIDDLE_PACKAGE,'1') AS SYG,\n");
		sql.append("  FLOOR((p1.buy_qty/decode(TPD.MIDDLE_PACKAGE,0,'1',TPD.MIDDLE_PACKAGE))/decode(TPD.BOX_NUM,0,'1',TPD.BOX_NUM)) AS BOXZ,\n");
		sql.append("       TO_CHAR(p1.forecast_date, 'YYYY-MM-DD') as forecast_date\n" );
		sql.append("  from tt_part_po_main p\n" );
		sql.append(" inner join tt_part_po_dtl p1\n" );
		sql.append("    on p.order_id = p1.order_id ");
		sql.append(" left join TT_PART_DEFINE TPD\n");
		sql.append("  on p1.PART_ID = TPD.PART_ID\n");
		sql.append("WHERE\n");
		sql.append("  p1.POLINE_ID = '"+inId+"'\n");
		sql.append(" ORDER BY p.status ASC,p.CREATE_DATE DESC\n");
		List<Map<String,Object>> list = pageQuery(sql.toString(), params, getFunName());
		return list;
	}
	/**
	 * 
	 * @param request
	 * @param loginUser
	 * @param curPage
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public PageResult<Map<String, Object>> getPurchaseOrderCMSTInfo(RequestWrapper request, AclUserBean loginUser, Integer curPage,Integer pageSize) throws Exception {
		StringBuffer sql = new StringBuffer();
		List<Object> params = new ArrayList<Object>();
		
		String ORDER_CODE = request.getParamValue("ORDER_CODE");
		String PLAN_CODE = request.getParamValue("PLAN_CODE");
		String sCreateDate = request.getParamValue("sCreateDate");
		String eCreateDate = request.getParamValue("eCreateDate");
		
		String PART_OLDCODE = request.getParamValue("PART_OLDCODE");
		String PART_CNAME = request.getParamValue("PART_CNAME");
		String PART_CODE = request.getParamValue("PART_CODE");
		
		
		String VENDER_NAME = request.getParamValue("VENDER_NAME");
		String CMST_CODE = request.getParamValue("CMST_CODE");
		String CMST_NAME = request.getParamValue("CMST_NAME");
		String sendstate = request.getParamValue("sendstate");
		String ORDER_PERIOD = request.getParamValue("ORDER_PERIOD");
		
		sql.append("SELECT\n");
		sql.append("  POP.PO_ID,\n");
		sql.append("  POP.ORDER_ID,\n");
		sql.append("  POP.ORDER_CODE,\n");
		sql.append("  POP.PLAN_ID,\n");
		sql.append("  POP.PLAN_CODE,\n");
		sql.append("  POP.PLINE_ID,\n");
		sql.append("  POP.PART_ID,\n");
		sql.append("  POP.PART_OLDCODE,\n");
		sql.append("  POP.PART_CNAME,\n");
		sql.append("  POP.PART_CODE,\n");
		sql.append("  POP.UNIT,\n");
		sql.append("  POP.CHECK_QTY,\n");
		sql.append("  POP.IN_QTY,\n");
		sql.append(" decode((POP.CHECK_QTY-POP.IN_QTY),0,'sendALL',POP.CHECK_QTY,'notsend','sendpart') sendstate,\n");
		sql.append("  POP.VENDER_NAME,\n");
		sql.append("  POP.VENDER_CODE,\n");
		sql.append("  TO_CHAR(POP.CREATE_DATE,'YYYY-MM-DD') AS CREATE_DATE,\n");
		sql.append("  POP.REMARK,\n");
		sql.append("  TPD.IS_CMST,\n");
		sql.append("  TMD.DEALER_ID AS CMST_ID,\n");
		sql.append("  TMD.DEALER_CODE AS CMST_CODE,\n");
		sql.append("  TMD.DEALER_NAME AS CMST_NAME,\n");
		sql.append("  DTL.ORDER_PERIOD,\n");
		sql.append("  DTL.WEEK_ONE,\n");
		sql.append("  DTL.WEEK_TOW,\n");
		sql.append("  DTL.WEEK_THREE,\n");
		sql.append("  DTL.WEEK_FOUR,\n");
		sql.append("  DTL.PLAN_TYPES,\n");
		sql.append("  (SELECT PS.PLAN_TYPES FROM TT_PART_PLAN_SCROLL PS WHERE PS.ID = DTL.PLAN_ID) AS PLAN_TYPE,\n");
		sql.append("  (SELECT SM.REMARK FROM TT_PART_PLAN_SCROLL SM WHERE SM.ID = POP.PLAN_ID) AS PLAN_NAME,\n");
		sql.append("  DTL.SALES_REMARK\n");
		sql.append("FROM\n");
		sql.append("  TT_PART_OEM_PO POP,\n");
		sql.append("  TT_PART_DEFINE TPD,\n");
		sql.append("  TT_PART_CMST CMST,\n");
		sql.append("  TM_DEALER TMD,\n");
		sql.append("  TT_PART_PLAN_SCROLL_DEL DTL\n");
		sql.append("WHERE\n");
		sql.append("  POP.PART_ID = TPD.PART_ID\n");
		sql.append("  AND POP.PART_ID = CMST.PART_ID\n");
		sql.append("  AND CMST.CMST_ID = TMD.DEALER_ID\n");
		if(StringUtil.notNull(loginUser.getDealerId())){
			sql.append("  AND CMST.CMST_ID = '"+CommonUtils.checkNull(loginUser.getDealerId())+"'\n");
		}
		sql.append("  AND CMST.STATE = 10041001--有效中储\n");
		sql.append("  AND TPD.IS_CMST = 10041001--备件是中储发货\n");
		sql.append("  AND POP.PLINE_ID = DTL.ID\n");
		
		if(StringUtil.notNull(ORDER_CODE)){
			sql.append("  AND POP.ORDER_CODE LIKE ?\n");
			params.add("%"+ORDER_CODE.trim().toUpperCase()+"%");
		}
		if(StringUtil.notNull(PLAN_CODE)){
			sql.append("  AND POP.PLAN_CODE LIKE ?\n");
			params.add("%"+PLAN_CODE.trim().toUpperCase()+"%");
		}
		if(StringUtil.notNull(sCreateDate)){
			sql.append("    AND TO_CHAR(POP.CREATE_DATE,'YYYY-MM-DD') >= ?\n");
			params.add(sCreateDate);
		}
		if(StringUtil.notNull(eCreateDate)){
			sql.append("    AND TO_CHAR(POP.CREATE_DATE,'YYYY-MM-DD') <= ?\n");
			params.add(eCreateDate);
		}
		
		if(StringUtil.notNull(PART_OLDCODE)){
			sql.append("  AND POP.PART_OLDCODE LIKE ?\n");
			params.add("%"+PART_OLDCODE.trim().toUpperCase()+"%");
		}
		if(StringUtil.notNull(PART_CNAME)){
			sql.append("  AND POP.PART_CNAME LIKE ?\n");
			params.add("%"+PART_CNAME+"%");
		}
		if(StringUtil.notNull(PART_CODE)){
			sql.append("  AND POP.PART_CODE LIKE ?\n");
			params.add("%"+PART_CODE.trim().toUpperCase()+"%");
		}
		
		if(StringUtil.notNull(VENDER_NAME)){
			sql.append("  AND POP.VENDER_NAME LIKE ?\n");
			params.add("%"+VENDER_NAME+"%");
		}
		if(StringUtil.notNull(CMST_CODE)){
			sql.append("  AND TMD.DEALER_CODE LIKE ?\n");
			params.add("%"+CMST_CODE.trim().toUpperCase()+"%");
		}
		if(StringUtil.notNull(CMST_NAME)){
			sql.append("  AND TMD.DEALER_NAME LIKE ?\n");
			params.add("%"+CMST_NAME+"%");
		}
		if(StringUtil.notNull(sendstate) && "notsendALL".equals(sendstate)){
			//查询未送货完毕的订单
			sql.append("  AND POP.CHECK_QTY-POP.IN_QTY >0 \n");
			 
		}else if(StringUtil.notNull(sendstate) && "All".equals(sendstate)){
			//查询所有
			//sql.append("  AND POP.CHECK_QTY-POP.IN_QTY >0 \n");
			 
		}else{
			sql.append(" and  decode(POP.CHECK_QTY-POP.IN_QTY,0,'sendALL',POP.CHECK_QTY,'notsend','sendpart')  = ?\n");
			params.add(sendstate);
		}
		
		if(StringUtil.notNull(ORDER_PERIOD)){
			sql.append("  AND DTL.ORDER_PERIOD  = ? \n");
			params.add(ORDER_PERIOD);
		}
		 

		sql.append("ORDER BY\n");
		sql.append("  POP.CREATE_DATE ASC\n");

		
		PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), params, getFunName(), pageSize, curPage);
		return ps;
	}
	/**
	 * 无备件供应商
	 * @param planId
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String,Object>> allVenderIn(String planId) throws Exception{
		
		StringBuffer sbSql = new StringBuffer();
		sbSql.append("SELECT\n" );
		sbSql.append("  DTL.ID,\n" );
		sbSql.append("  DTL.PLAN_ID,\n" );
		sbSql.append("  DTL.PART_ID,\n" );
		sbSql.append("  DTL.PART_OLDCODE,\n" );
		sbSql.append("  DTL.VENDER_ID\n" );
		sbSql.append("FROM\n" );
		sbSql.append("  TT_PART_PLAN_SCROLL_DEL DTL\n" );
		sbSql.append("WHERE\n" );
		sbSql.append("  DTL.PLAN_ID = '"+planId+"'\n");
		sbSql.append("  AND DTL.VENDER_ID = -1\n");
		List<Map<String,Object>> list = this.pageQuery(sbSql.toString(), null, getFunName());
		return list;
	}
	/**
	 * 分页显示二维码
	 * @param request
	 * @param loginUser
	 * @param curPage
	 * @param paravalue
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public PageResult<Map<String, Object>> getPurcharOrderQrMorePage(RequestWrapper request, AclUserBean loginUser, Integer curPage, String paravalue) throws Exception{
		StringBuffer sql = new StringBuffer();
		List<Object> params = new ArrayList<Object>();
		
		Integer pageSize = Integer.parseInt(paravalue);
		String inId =  CommonUtils.checkNull(request.getParamValue("inId"));
		String part_Id=CommonUtils.checkNull(request.getParamValue("part_Id"));
		
		//add 2017-05-17 add 调用过程产生单个备件的二维码信息
		ArrayList ins = new ArrayList();
        ins.add(0, inId);
        ins.add(1, loginUser.getUserId());
        ins.add(2, part_Id);
        dao.callProcedure("PKG_PART_UTIL.PROC_CREATE_PO_QRCODE", ins, null);
		
		sql.append("SELECT\n");
		sql.append("  POP.wh_id STOCK_IN,\n");
		sql.append("  POP.IN_CODE,\n");
		sql.append("  POP.PART_CODE,\n");
		sql.append("  POP.PART_OLDCODE,\n");
		sql.append("  POP.PART_CNAME,\n");
		sql.append("  POP.PART_ENAME,\n");
		sql.append("  POP.UNIT,\n");
		sql.append("  POP.IN_ID PO_ID,\n");
		sql.append("  TO_CHAR(POP.in_date,'YYYY-MM-DD') AS IN_DATE,\n");
		sql.append("  V.VENDER_CODE,\n");
		sql.append("  V.VENDER_NAME,\n");
		sql.append("  V.ADDR,\n");
		sql.append("  V.TEL,\n");
		sql.append("  POP.qty,\n");
		sql.append("  POP.PART_UNIONQ_CODE\n");
		sql.append("FROM\n");
		sql.append("  tt_part_po_in_qrcode POP\n");
		sql.append("  LEFT join tt_part_vender_define v on POP.VENDER_ID=V.VENDER_ID\n");
		sql.append("WHERE 1=1 \n");
		sql.append("  AND POP.in_ID = ?\n");
		params.add(inId);
		sql.append("  order by pop.PART_OLDCODE ,pop.pkg_level desc\n");
		
		PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), params, getFunName(), pageSize, curPage);
		return ps;
	}
	/**
	 * 涂装确认
	 * 01自制件与02自制件且已生成采购订单并且未入库的
	 * @param request
	 * @param loginUser
	 * @param curPage
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public PageResult<Map<String, Object>> getPurchaseOrderPaintingInfo(RequestWrapper request, AclUserBean loginUser, Integer curPage, Integer pageSize) throws Exception{
		StringBuffer sql = new StringBuffer();
		List<Object> params = new ArrayList<Object>();
		
		String ORDER_CODE = request.getParamValue("ORDER_CODE");
		String PLAN_CODE = request.getParamValue("PLAN_CODE");
		String sCreateDate = request.getParamValue("sCreateDate");
		String eCreateDate = request.getParamValue("eCreateDate");
		
		String PART_OLDCODE = request.getParamValue("PART_OLDCODE");
		String PART_CNAME = request.getParamValue("PART_CNAME");
		String PART_CODE = request.getParamValue("PART_CODE");
		
		
		String VENDER_NAME = request.getParamValue("VENDER_NAME");
		String IS_PAINTING = request.getParamValue("IS_PAINTING");
		
		sql.append("SELECT\n");
		sql.append("  POP.PO_ID,\n");
		sql.append("  POP.ORDER_ID,\n");
		sql.append("  POP.ORDER_CODE,\n");
		sql.append("  POP.PLAN_ID,\n");
		sql.append("  POP.PLAN_CODE,\n");
		sql.append("  POP.PART_ID,\n");
		sql.append("  POP.PART_OLDCODE,\n");
		sql.append("  POP.PART_CNAME,\n");
		sql.append("  POP.PART_CODE,\n");
		sql.append("  POP.PART_TYPE,\n");
		sql.append("  POP.PRODUCE_FAC,--01自制92811005/02自制92811006\n");
		sql.append("  POP.PRODUCE_STATE,--备件自制92631001/配套92631002\n");
		sql.append("  POP.VENDER_ID,\n");
		sql.append("  POP.VENDER_CODE,\n");
		sql.append("  POP.VENDER_NAME,\n");
		sql.append("  POP.CHECK_QTY,\n");
		sql.append("  POP.IN_QTY,\n");
		sql.append("  POP.UNIT,\n");
		sql.append("  TO_CHAR(POP.CREATE_DATE,'YYYY-MM-DD') AS CREATE_DATE,\n");
		sql.append("  POP.IS_PAINTING,\n");
		sql.append("  TU.NAME AS PAINTING_BY_CN,\n");
		sql.append("  TO_CHAR(POP.PAINTING_DATE,'YYYY-MM-DD') AS PAINTING_DATE,\n");
		sql.append("  POP.STATE\n");
		sql.append("FROM\n");
		sql.append("  TT_PART_OEM_PO POP,\n");
		sql.append("  TT_PART_DEFINE TPD,\n");
		sql.append("  TC_USER TU\n");
		sql.append("WHERE\n");
		sql.append("  POP.PART_ID = TPD.PART_ID\n");
		sql.append("  AND POP.PAINTING_BY = TU.USER_ID(+)\n");
		sql.append("  AND POP.PART_TYPE IN (92021001,92021002)\n");
		sql.append("  AND POP.PRODUCE_STATE = 92631001\n");
		
		if(StringUtil.notNull(ORDER_CODE)){
			sql.append("  AND POP.ORDER_CODE LIKE ?\n");
			params.add("%"+ORDER_CODE.trim().toUpperCase()+"%");
		}
		if(StringUtil.notNull(PLAN_CODE)){
			sql.append("  AND POP.PLAN_CODE LIKE ?\n");
			params.add("%"+PLAN_CODE.trim().toUpperCase()+"%");
		}
		if(StringUtil.notNull(sCreateDate)){
			sql.append("    AND TO_CHAR(POP.CREATE_DATE,'YYYY-MM-DD') >= ?\n");
			params.add(sCreateDate);
		}
		if(StringUtil.notNull(eCreateDate)){
			sql.append("    AND TO_CHAR(POP.CREATE_DATE,'YYYY-MM-DD') <= ?\n");
			params.add(eCreateDate);
		}
		
		if(StringUtil.notNull(PART_OLDCODE)){
			sql.append("  AND POP.PART_OLDCODE LIKE ?\n");
			params.add("%"+PART_OLDCODE.trim().toUpperCase()+"%");
		}
		if(StringUtil.notNull(PART_CNAME)){
			sql.append("  AND POP.PART_CNAME LIKE ?\n");
			params.add("%"+PART_CNAME+"%");
		}
		if(StringUtil.notNull(PART_CODE)){
			sql.append("  AND POP.PART_CODE LIKE ?\n");
			params.add("%"+PART_CODE.trim().toUpperCase()+"%");
		}
		
		if(StringUtil.notNull(VENDER_NAME)){
			sql.append("  AND POP.VENDER_NAME LIKE ?\n");
			params.add("%"+VENDER_NAME+"%");
		}
		if(StringUtil.notNull(IS_PAINTING)){
			sql.append("  AND POP.IS_PAINTING = ?\n");
			params.add(IS_PAINTING);
		}
		
		sql.append("ORDER BY POP.CREATE_DATE DESC\n");


		PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), params, getFunName(), pageSize, curPage);
		return ps;
	}
	/**
	 * 焊装确认
	 * 01自制件且已生成采购订单并且未入库的
	 * @param request
	 * @param loginUser
	 * @param curPage
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public PageResult<Map<String, Object>> getPurchaseOrderWeldingInfo(RequestWrapper request, AclUserBean loginUser, Integer curPage, Integer pageSize) throws Exception{
		StringBuffer sql = new StringBuffer();
		List<Object> params = new ArrayList<Object>();
		
		String ORDER_CODE = request.getParamValue("ORDER_CODE");
		String PLAN_CODE = request.getParamValue("PLAN_CODE");
		String sCreateDate = request.getParamValue("sCreateDate");
		String eCreateDate = request.getParamValue("eCreateDate");
		
		String PART_OLDCODE = request.getParamValue("PART_OLDCODE");
		String PART_CNAME = request.getParamValue("PART_CNAME");
		String PART_CODE = request.getParamValue("PART_CODE");
		
		
		String VENDER_NAME = request.getParamValue("VENDER_NAME");
		String IS_WELDING = request.getParamValue("IS_WELDING");
		
		sql.append("SELECT\n");
		sql.append("  POP.PO_ID,\n");
		sql.append("  POP.ORDER_ID,\n");
		sql.append("  POP.ORDER_CODE,\n");
		sql.append("  POP.PLAN_ID,\n");
		sql.append("  POP.PLAN_CODE,\n");
		sql.append("  POP.PART_ID,\n");
		sql.append("  POP.PART_OLDCODE,\n");
		sql.append("  POP.PART_CNAME,\n");
		sql.append("  POP.PART_CODE,\n");
		sql.append("  POP.PART_TYPE,\n");
		sql.append("  POP.PRODUCE_FAC,--01自制92811005/02自制92811006\n");
		sql.append("  POP.PRODUCE_STATE,--备件自制92631001/配套92631002\n");
		sql.append("  POP.VENDER_ID,\n");
		sql.append("  POP.VENDER_CODE,\n");
		sql.append("  POP.VENDER_NAME,\n");
		sql.append("  POP.CHECK_QTY,\n");
		sql.append("  POP.IN_QTY,\n");
		sql.append("  POP.UNIT,\n");
		sql.append("  TO_CHAR(POP.CREATE_DATE,'YYYY-MM-DD') AS CREATE_DATE,\n");
		sql.append("  POP.IS_WELDING,\n");
		sql.append("  TU.NAME AS WELDING_BY_CN,\n");
		sql.append("  TO_CHAR(POP.WELDING_DATE,'YYYY-MM-DD') AS WELDING_DATE,\n");
		sql.append("  POP.STATE\n");
		sql.append("FROM\n");
		sql.append("  TT_PART_OEM_PO POP,\n");
		sql.append("  TT_PART_DEFINE TPD,\n");
		sql.append("  TC_USER TU\n");
		sql.append("WHERE\n");
		sql.append("  POP.PART_ID = TPD.PART_ID\n");
		sql.append("  AND POP.WELDING_BY = TU.USER_ID(+)\n");
		sql.append("  AND POP.PART_TYPE IN (92021001)\n");//01件
		sql.append("  AND POP.PRODUCE_STATE = 92631001\n");//自制
		
		if(StringUtil.notNull(ORDER_CODE)){
			sql.append("  AND POP.ORDER_CODE LIKE ?\n");
			params.add("%"+ORDER_CODE.trim().toUpperCase()+"%");
		}
		if(StringUtil.notNull(PLAN_CODE)){
			sql.append("  AND POP.PLAN_CODE LIKE ?\n");
			params.add("%"+PLAN_CODE.trim().toUpperCase()+"%");
		}
		if(StringUtil.notNull(sCreateDate)){
			sql.append("    AND TO_CHAR(POP.CREATE_DATE,'YYYY-MM-DD') >= ?\n");
			params.add(sCreateDate);
		}
		if(StringUtil.notNull(eCreateDate)){
			sql.append("    AND TO_CHAR(POP.CREATE_DATE,'YYYY-MM-DD') <= ?\n");
			params.add(eCreateDate);
		}
		
		if(StringUtil.notNull(PART_OLDCODE)){
			sql.append("  AND POP.PART_OLDCODE LIKE ?\n");
			params.add("%"+PART_OLDCODE.trim().toUpperCase()+"%");
		}
		if(StringUtil.notNull(PART_CNAME)){
			sql.append("  AND POP.PART_CNAME LIKE ?\n");
			params.add("%"+PART_CNAME+"%");
		}
		if(StringUtil.notNull(PART_CODE)){
			sql.append("  AND POP.PART_CODE LIKE ?\n");
			params.add("%"+PART_CODE.trim().toUpperCase()+"%");
		}
		
		if(StringUtil.notNull(VENDER_NAME)){
			sql.append("  AND POP.VENDER_NAME LIKE ?\n");
			params.add("%"+VENDER_NAME+"%");
		}
		if(StringUtil.notNull(IS_WELDING)){
			sql.append("  AND POP.IS_WELDING = ?\n");
			params.add(IS_WELDING);
		}
		
		sql.append("ORDER BY POP.CREATE_DATE DESC\n");


		PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), params, getFunName(), pageSize, curPage);
		return ps;
	}
	/**
	 * 传入所有的待入库PoId返回可入库的02自制件poId
	 * @param cks
	 * @param request 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getPoIdByPoIds(String[] cks) throws Exception{
		String poIds = "";
		for (int i = 0; i < cks.length; i++) {
			if(StringUtil.isNull(poIds)){
				poIds = cks[i];
			}else{
				poIds = poIds+","+cks[i];
			}
		}
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT\n");
		sql.append("  DISTINCT\n");
		sql.append("  TEMP.PO_ID\n");
		sql.append("FROM(\n");
		sql.append("  SELECT DISTINCT T.STRVALUE AS PO_ID FROM TABLE(FN_SPLIT('"+poIds+"',',')) T\n");
		sql.append("  UNION ALL\n");
		sql.append("  SELECT DISTINCT PKG_PART_UTIL.F_GET_02ZZ_IN_PO_ID(T.STRVALUE) AS PO_ID FROM TABLE(FN_SPLIT('"+poIds+"',',')) T\n");
		sql.append(")TEMP\n");
		sql.append("WHERE TEMP.PO_ID IS NOT NULL\n");

		List<Map<String, Object>> list = pageQuery(sql.toString(), null, getFunName());
		return list;
	}
	/**
	 * 传入一个PoId如果是02自制件，返回所有的拆分件和拆分比例
	 * @param poId
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getSplitPartInfoByPoId(String poId) throws Exception{
		StringBuffer sql = new StringBuffer();
		
		sql.append("SELECT\n");
		sql.append("  DISTINCT\n");
		sql.append("  TO_CHAR(POP.PO_ID) AS PO_ID,\n");
		sql.append("  SD.SPLIT_NUM,\n");
		sql.append("  SD.COST_RATE\n");
		sql.append("FROM\n");
		sql.append("  TT_PART_OEM_PO POP,\n");
		sql.append("  TT_PART_SPLIT_DEFINE SD\n");
		sql.append("WHERE\n");
		sql.append("  POP.PART_ID = SD.SUBPART_ID\n");
		sql.append("  AND POP.PRODUCE_FAC = 92811006--自制件采购\n");
		sql.append("  AND (POP.PO_ID = '"+poId+"' OR POP.PO_ID =PKG_PART_UTIL.F_GET_02ZZ_IN_PO_ID ('"+poId+"'))");
		
		List<Map<String, Object>> list = pageQuery(sql.toString(), null, getFunName());
		return list;
	}
	/**
	 * 根据计划单号拆分4周
	 * @param planId
	 */
	@SuppressWarnings("unchecked")
	public void splitPartsByPlanId(String planId) throws Exception{
		StringBuffer sql = new StringBuffer();
		//分四周
		sql.append("UPDATE TT_PART_PLAN_SCROLL_DEL SD\n");
		sql.append("SET SD.WEEK_ONE = TRUNC (SD.PLAN_MONTH_ONE / 4),\n");
		sql.append(" SD.WEEK_TOW    = TRUNC (SD.PLAN_MONTH_ONE / 4),\n");
		sql.append(" SD.WEEK_THREE  = TRUNC (SD.PLAN_MONTH_ONE / 4),\n");
		sql.append(" SD.WEEK_FOUR   = TRUNC (SD.PLAN_MONTH_ONE / 4) + MOD (SD.PLAN_MONTH_ONE, 4)\n");
		sql.append("WHERE\n");
		sql.append("  SD.PLAN_ID = '"+planId+"'\n");
		sql.append("  AND (NVL(SD.WEEK_ONE,0) + NVL(SD.WEEK_TOW,0) + NVL(SD.WEEK_THREE,0) + NVL(SD.WEEK_FOUR,0)) <> SD.PLAN_MONTH_ONE\n");
        this.update(sql.toString(), null);
	}
	/**
	 * 计划室最终确认数量和计划数量不一致，重新拆分4周
	 * @param planLineId
	 */
	@SuppressWarnings("unchecked")
	public void againSplitPartsbyplanLineId(String planLineId) {
		StringBuffer sql = new StringBuffer();
		//分四周
		sql.append("UPDATE TT_PART_PLAN_SCROLL_DEL SD\n");
		sql.append("SET SD.WEEK_ONE = TRUNC (SD.LAST_CONFIRM_NUM / 4),\n");
		sql.append(" SD.WEEK_TOW    = TRUNC (SD.LAST_CONFIRM_NUM / 4),\n");
		sql.append(" SD.WEEK_THREE  = TRUNC (SD.LAST_CONFIRM_NUM / 4),\n");
		sql.append(" SD.WEEK_FOUR   = TRUNC (SD.LAST_CONFIRM_NUM / 4) + MOD (SD.LAST_CONFIRM_NUM, 4)\n");
		sql.append("WHERE\n");
		sql.append("  SD.ID = '"+planLineId+"'\n");
		sql.append("  AND (NVL(SD.WEEK_ONE,0) + NVL(SD.WEEK_TOW,0) + NVL(SD.WEEK_THREE,0) + NVL(SD.WEEK_FOUR,0)) <> SD.LAST_CONFIRM_NUM\n");
        this.update(sql.toString(), null);
	}
	/**
	 * 查询不存在的备件
	 * @param planId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getNotSaveParts(String planId) throws Exception{
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT\n");
		sql.append("  DISTINCT\n");
		sql.append("  SD.PART_ID,\n");
		sql.append("  SD.PART_OLDCODE\n");
		sql.append("FROM\n");
		sql.append("  TT_PART_PLAN_SCROLL_DEL SD\n");
		sql.append("WHERE\n");
		sql.append("  SD.PLAN_ID = '"+planId+"'\n");
		sql.append("  AND SD.PART_ID IS NULL\n");
		
		List<Map<String, Object>> list = pageQuery(sql.toString(), null, getFunName());
		return list;
	}
	/**
	 * 
	 * @param request
	 * @param loginUser
	 * @param curPage
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public PageResult<Map<String, Object>> getPurchaseOrderCMSTPrintInfo(RequestWrapper request, AclUserBean loginUser, Integer curPage,Integer pageSize) throws Exception {
		StringBuffer sql = new StringBuffer();
		List<Object> params = new ArrayList<Object>();
		
		String ORDER_CODE = request.getParamValue("ORDER_CODE");
		String PLAN_CODE = request.getParamValue("PLAN_CODE");
		String sCreateDate = request.getParamValue("sCreateDate");
		String eCreateDate = request.getParamValue("eCreateDate");
		
		String PART_OLDCODE = request.getParamValue("PART_OLDCODE");
		String PART_CNAME = request.getParamValue("PART_CNAME");
		String PART_CODE = request.getParamValue("PART_CODE");
		
		
		String VENDER_NAME = request.getParamValue("VENDER_NAME");
		String CMST_CODE = request.getParamValue("CMST_CODE");
		String CMST_NAME = request.getParamValue("CMST_NAME");
		String sendstate = request.getParamValue("sendstate");
		String ORDER_PERIOD = request.getParamValue("ORDER_PERIOD");
		String PRINT_FLAG = request.getParamValue("PRINT_FLAG");
		
		
		
		sql.append("SELECT\n");
		sql.append("  POP.PO_ID,\n");
		sql.append("  POP.ORDER_ID,\n");
		sql.append("  POP.ORDER_CODE,\n");
		sql.append("  POP.PLAN_ID,\n");
		sql.append("  POP.PLAN_CODE,\n");
		sql.append("  POP.PLINE_ID,\n");
		sql.append("  POP.PART_ID,\n");
		sql.append("  POP.PART_OLDCODE,\n");
		sql.append("  POP.PART_CNAME,\n");
		sql.append("  POP.PART_CODE,\n");
		sql.append("  POP.UNIT,\n");
		sql.append("  POP.CHECK_QTY,\n");
		sql.append("  POP.IN_QTY,\n");
		sql.append("  TO_CHAR(POP.FORECAST_DATE,'YYYY-MM-DD')  FORECAST_DATE,--预计到货日期\n");
		sql.append(" decode((POP.CHECK_QTY-POP.IN_QTY),0,'sendALL',POP.CHECK_QTY,'notsend','sendpart') sendstate,\n");
		sql.append("  (POP.CHECK_QTY- nvl(POP.IN_QTY,0)) PRINT_QTY,\n");
		sql.append("  POP.VENDER_NAME,\n");
		sql.append("  POP.VENDER_CODE,\n");
		sql.append("  TO_CHAR(POP.CREATE_DATE,'YYYY-MM-DD') AS CREATE_DATE,\n");
		sql.append("  POP.REMARK,\n");
		sql.append("  TPD.IS_CMST,\n");
		sql.append("  TMD.DEALER_ID AS CMST_ID,\n");
		sql.append("  TMD.DEALER_CODE AS CMST_CODE,\n");
		sql.append("  TMD.DEALER_NAME AS CMST_NAME,\n");
		sql.append("  DTL.ORDER_PERIOD,\n");
		sql.append("  DTL.WEEK_ONE,\n");
		sql.append("  DTL.WEEK_TOW,\n");
		sql.append("  DTL.WEEK_THREE,\n");
		sql.append("  DTL.WEEK_FOUR,\n");
		sql.append("  DTL.PLAN_TYPES,\n");
		sql.append("  (SELECT count(1) FROM tt_part_oem_po_send_print_log PS WHERE PS.po_ID = pop.po_ID) AS print_times,\n");
		sql.append("  (SELECT nvl(sum(print_qty),0) FROM tt_part_oem_po_send_print_log PS WHERE PS.po_ID = pop.po_ID) AS print_qtys,\n");
		sql.append("  (SELECT PS.PLAN_TYPES FROM TT_PART_PLAN_SCROLL PS WHERE PS.ID = DTL.PLAN_ID) AS PLAN_TYPE,\n");
		sql.append("  (SELECT SM.REMARK FROM TT_PART_PLAN_SCROLL SM WHERE SM.ID = POP.PLAN_ID) AS PLAN_NAME,\n");
		sql.append("  DTL.SALES_REMARK\n");
		sql.append("FROM\n");
		sql.append("  TT_PART_OEM_PO POP,\n");
		sql.append("  TT_PART_DEFINE TPD,\n");
		sql.append("  TT_PART_CMST CMST,\n");
		sql.append("  TM_DEALER TMD,\n");
		sql.append("  TT_PART_PLAN_SCROLL_DEL DTL\n");
		sql.append("WHERE\n");
		sql.append("  POP.PART_ID = TPD.PART_ID\n");
		sql.append("  AND POP.PART_ID = CMST.PART_ID\n");
		sql.append("  AND CMST.CMST_ID = TMD.DEALER_ID\n");
		if(StringUtil.notNull(loginUser.getDealerId())){
			sql.append("  AND CMST.CMST_ID = '"+CommonUtils.checkNull(loginUser.getDealerId())+"'\n");
		}
		sql.append("  AND CMST.STATE = 10041001--有效中储\n");
		sql.append("  AND TPD.IS_CMST = 10041001--备件是中储发货\n");
		sql.append("  AND POP.PLINE_ID = DTL.ID\n");
		
		if(StringUtil.notNull(ORDER_CODE)){
			sql.append("  AND POP.ORDER_CODE LIKE ?\n");
			params.add("%"+ORDER_CODE.trim().toUpperCase()+"%");
		}
		if(StringUtil.notNull(PLAN_CODE)){
			sql.append("  AND POP.PLAN_CODE LIKE ?\n");
			params.add("%"+PLAN_CODE.trim().toUpperCase()+"%");
		}
		if(StringUtil.notNull(sCreateDate)){
			sql.append("    AND TO_CHAR(POP.CREATE_DATE,'YYYY-MM-DD') >= ?\n");
			params.add(sCreateDate);
		}
		if(StringUtil.notNull(eCreateDate)){
			sql.append("    AND TO_CHAR(POP.CREATE_DATE,'YYYY-MM-DD') <= ?\n");
			params.add(eCreateDate);
		}
		
		if(StringUtil.notNull(PART_OLDCODE)){
			sql.append("  AND POP.PART_OLDCODE LIKE ?\n");
			params.add("%"+PART_OLDCODE.trim().toUpperCase()+"%");
		}
		if(StringUtil.notNull(PART_CNAME)){
			sql.append("  AND POP.PART_CNAME LIKE ?\n");
			params.add("%"+PART_CNAME+"%");
		}
		if(StringUtil.notNull(PART_CODE)){
			sql.append("  AND POP.PART_CODE LIKE ?\n");
			params.add("%"+PART_CODE.trim().toUpperCase()+"%");
		}
		
		if(StringUtil.notNull(VENDER_NAME)){
			sql.append("  AND POP.VENDER_NAME LIKE ?\n");
			params.add("%"+VENDER_NAME+"%");
		}
		if(StringUtil.notNull(CMST_CODE)){
			sql.append("  AND TMD.DEALER_CODE LIKE ?\n");
			params.add("%"+CMST_CODE.trim().toUpperCase()+"%");
		}
		if(StringUtil.notNull(CMST_NAME)){
			sql.append("  AND TMD.DEALER_NAME LIKE ?\n");
			params.add("%"+CMST_NAME+"%");
		}
		if(StringUtil.notNull(sendstate) && "notsendALL".equals(sendstate)){
			//查询未送货完毕的订单
			sql.append("  AND POP.CHECK_QTY-POP.IN_QTY >0 \n");
			 
		}else if(StringUtil.notNull(sendstate) && "All".equals(sendstate)){
			//查询所有
			//sql.append("  AND POP.CHECK_QTY-POP.IN_QTY >0 \n");
			 
		}else{
			sql.append(" and  decode(POP.CHECK_QTY-POP.IN_QTY,0,'sendALL',POP.CHECK_QTY,'notsend','sendpart')  = ?\n");
			params.add(sendstate);
		}
		
		if(StringUtil.notNull(ORDER_PERIOD)){
			sql.append("  AND DTL.ORDER_PERIOD  = ? \n");
			params.add(ORDER_PERIOD);
		}
		if(StringUtil.notNull(PRINT_FLAG)){
			if(Constant.IF_TYPE_YES.intValue()== Integer.valueOf(PRINT_FLAG).intValue()){
				sql.append("  AND  exists(select 1 from  tt_part_oem_po_send_print_log tl  where tl.po_id = pop.po_id) \n");
			}else{
				sql.append("  AND not  exists(select 1 from  tt_part_oem_po_send_print_log tl  where tl.po_id = pop.po_id) \n");
			}
		}
		
		
		 

		sql.append("ORDER BY\n");
		sql.append("  POP.CREATE_DATE ASC,pop.VENDER_CODE \n");

		
		PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), params, getFunName(), pageSize, curPage);
		return ps;
	}
	
	/**
	 * 查询采购到货待确认信息
	 * @param request
	 * @param loginUser
	 * @param curPage
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public PageResult<Map<String, Object>> getPurRcvInfo(RequestWrapper request, AclUserBean loginUser, Integer curPage, Integer pageSize) throws Exception{
		StringBuffer sql = new StringBuffer();
		List<Object> params = new ArrayList<Object>();
		
		String ORDER_CODE = request.getParamValue("ORDER_CODE");
		String VENDER_NAME = request.getParamValue("VENDER_NAME");
		String sCreateDate = request.getParamValue("sCreateDate");
		String eCreateDate = request.getParamValue("eCreateDate");
		
		String PART_OLDCODE = request.getParamValue("PART_OLDCODE");
		String PART_CNAME = request.getParamValue("PART_CNAME");
		String PART_CODE = request.getParamValue("PART_CODE");
		
		String IS_DQR = request.getParamValue("IS_DQR");
		String CMST_NAME = request.getParamValue("CMST_NAME");
		
		sql.append("SELECT TEMP.* FROM( \n");
		sql.append("SELECT\n");
		sql.append("  POP.PO_ID,\n");
		sql.append("  POP.ORDER_ID,\n");
		sql.append("  POP.ORDER_CODE,\n");
		sql.append("  POP.PLAN_ID,\n");
		sql.append("  POP.PLINE_ID,\n");
		sql.append("  POP.PLAN_CODE,\n");
		sql.append("  POP.PLANER_ID,\n");
		sql.append("  POP.PLANER,\n");
		sql.append("  POP.BUYER_ID,\n");
		sql.append("  POP.BUYER,\n");
		sql.append("  POP.PART_ID,\n");
		sql.append("  POP.PART_OLDCODE,\n");
		sql.append("  POP.PART_CNAME,\n");
		sql.append("  POP.PART_CODE,\n");
		sql.append("  POP.PART_TYPE,--01，02，03\n");
		sql.append("  F_GET_TCCODE_DESC(POP.PART_TYPE) AS PART_TYPE_CN,\n");
		sql.append("  POP.PRODUCE_FAC,--采购方式\n");
		sql.append("  POP.UNIT,\n");
		sql.append("  POP.VENDER_ID,\n");
		sql.append("  POP.VENDER_CODE,\n");
		sql.append("  POP.VENDER_NAME,\n");
		sql.append("  POP.PLAN_QTY,--计划数量\n");
		sql.append("  POP.BUY_QTY,--采购数量\n");
		sql.append("  POP.BUY_PRICE,--采购单价\n");
		sql.append("  POP.BUY_AMOUNT,--采购金额\n");
		sql.append("  (POP.CHECK_QTY-NVL(POP.IN_QTY,0)) AS DIN_QTY,--待验收数量\n");
		sql.append("  POP.IN_QTY,--已验收数量\n");
		sql.append("  POP.FORECAST_DATE,--预计到货日期\n");
		sql.append("  POP.WH_ID,\n");
		sql.append("  POP.WH_NAME,\n");
		sql.append("  POP.REMARK,\n");
		sql.append("  TO_CHAR(POP.CREATE_DATE,'YYYY-MM-DD') AS CREATE_DATE,\n");
		sql.append("  POP.ORG_ID,\n");
		sql.append("  POP.STATE,\n");
		sql.append("  F_GET_TCCODE_DESC(POP.STATE) AS STATE_CN,\n");
		sql.append("  (SELECT T.LOC_ID FROM TT_PART_LOACTION_DEFINE T WHERE T.PART_ID = POP.PART_ID AND T.ORG_ID = POP.ORG_ID AND T.WH_ID = POP.WH_ID) AS LOC_ID,\n");
		sql.append("  (SELECT T.LOC_CODE FROM TT_PART_LOACTION_DEFINE T WHERE T.PART_ID = POP.PART_ID AND T.ORG_ID = POP.ORG_ID AND T.WH_ID = POP.WH_ID) AS LOC_CODE,\n");
		sql.append("  NVL((SELECT SUM(PC.CON_QTY) FROM TT_PART_OEM_PO_CONFIRM PC WHERE PC.PO_ID = POP.PO_ID AND PC.STATUS = 10011001),0) AS YCON_QTY,\n");
		sql.append("  NVL(POP.CHECK_QTY,0) - NVL((SELECT SUM(PC.CON_QTY) FROM TT_PART_OEM_PO_CONFIRM PC WHERE PC.PO_ID = POP.PO_ID AND PC.STATUS = 10011001),0) AS DCON_QTY,\n");
		sql.append("  (SELECT SM.REMARK FROM TT_PART_PLAN_SCROLL SM WHERE SM.ID = POP.PLAN_ID) AS PLAN_NAME,\n");
		sql.append("  (SELECT SD.SALES_REMARK FROM TT_PART_PLAN_SCROLL_DEL SD WHERE SD.ID = POP.PLINE_ID) AS PLAN_REMARK,\n");
		sql.append("  CMSD.DEALER_NAME AS CMST_NAME\n");
		sql.append("FROM\n");
		sql.append("  TT_PART_OEM_PO POP,\n");
		sql.append("  TT_PART_CMST CMST,\n");
		sql.append("  TM_DEALER CMSD\n");
		sql.append("WHERE\n");
		sql.append("  POP.ORG_ID = "+Constant.OEM_ACTIVITIES+"\n");
		sql.append("  AND POP.PART_ID = CMST.PART_ID(+)\n");
		sql.append("  AND CMST.CMST_ID = CMSD.DEALER_ID(+)\n");
		sql.append("  AND POP.STATE = '"+Constant.PART_PURCHASE_ORDERCHK_STATUS_01+"'\n");
		sql.append("  AND NOT EXISTS(SELECT 1 FROM TT_PART_OEM_PO TPOP WHERE TPOP.PO_ID = POP.PO_ID AND TPOP.PART_OLDCODE LIKE '%-DY' AND TPOP.PRODUCE_FAC = 92811006)--自制件采购\n");
		if(StringUtil.notNull(ORDER_CODE)){
			sql.append("  AND POP.ORDER_CODE LIKE ?\n");
			params.add("%"+ORDER_CODE.toUpperCase()+"%");
		}
		if(StringUtil.notNull(VENDER_NAME)){
			sql.append("  AND POP.VENDER_NAME LIKE ?\n");
			params.add("%"+VENDER_NAME+"%");
		}
		
		if(StringUtil.notNull(sCreateDate)){
			sql.append("  AND TO_CHAR(POP.CREATE_DATE,'YYYY-MM-DD') >= ?\n");
			params.add(sCreateDate);
		}
		if(StringUtil.notNull(eCreateDate)){
			sql.append("  AND TO_CHAR(POP.CREATE_DATE,'YYYY-MM-DD') <= ?\n");
			params.add(eCreateDate);
		}
		if(StringUtil.notNull(PART_OLDCODE)){
			sql.append("  AND POP.PART_OLDCODE LIKE ?\n");
			params.add("%"+PART_OLDCODE.toUpperCase()+"%");
		}
		if(StringUtil.notNull(PART_CNAME)){
			sql.append("  AND POP.PART_CNAME LIKE ?\n");
			params.add("%"+PART_CNAME+"%");
		}
		if(StringUtil.notNull(PART_CODE)){
			sql.append("  AND POP.PART_CODE LIKE ?\n");
			params.add("%"+PART_CODE.toUpperCase()+"%");
		}
		if(StringUtil.notNull(CMST_NAME)){
			sql.append("  AND CMSD.DEALER_NAME LIKE ?\n");
			params.add("%"+CMST_NAME+"%");
		}
		sql.append("  AND (NVL(POP.CHECK_QTY,0) - NVL(POP.IN_QTY,0)) > 0 \n");
		
		sql.append(")TEMP\n");
		sql.append("WHERE 1=1\n");
		if(Constant.IF_TYPE_YES.toString().equals(IS_DQR)){
			sql.append(" AND DCON_QTY > 0\n");
		}else if(Constant.IF_TYPE_NO.toString().equals(IS_DQR)){
			sql.append(" AND DCON_QTY = 0\n");
		}else{
		}
		
		sql.append(" ORDER BY TEMP.ORDER_CODE ASC,TEMP.CREATE_DATE ASC\n");

		PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), params, getFunName(), pageSize, curPage);
		return ps;
	}
	/**
	 * 查询可确认数量根据PoId
	 * @param poId
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> getConfimOemPoInfoByPoId(String poId) throws Exception{
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT\n");
		sql.append("  POP.PO_ID,\n");
		sql.append("  POP.ORDER_ID,\n");
		sql.append("  POP.ORDER_CODE,\n");
		sql.append("  POP.PLAN_ID,\n");
		sql.append("  POP.PLINE_ID,\n");
		sql.append("  POP.PLAN_CODE,\n");
		sql.append("  POP.PLANER_ID,\n");
		sql.append("  POP.PLANER,\n");
		sql.append("  POP.BUYER_ID,\n");
		sql.append("  POP.BUYER,\n");
		sql.append("  POP.PART_ID,\n");
		sql.append("  POP.PART_OLDCODE,\n");
		sql.append("  POP.PART_CNAME,\n");
		sql.append("  POP.PART_CODE,\n");
		sql.append("  POP.PART_TYPE,--01，02，03\n");
		sql.append("  POP.PRODUCE_FAC,--采购方式\n");
		sql.append("  POP.UNIT,\n");
		sql.append("  POP.VENDER_ID,\n");
		sql.append("  POP.VENDER_CODE,\n");
		sql.append("  POP.VENDER_NAME,\n");
		sql.append("  POP.PLAN_QTY,--计划数量\n");
		sql.append("  POP.BUY_QTY,--采购数量\n");
		sql.append("  POP.BUY_PRICE,--采购单价\n");
		sql.append("  POP.BUY_AMOUNT,--采购金额\n");
		sql.append("  (POP.CHECK_QTY-NVL(POP.IN_QTY,0)) AS DIN_QTY,--待验收数量\n");
		sql.append("  POP.IN_QTY,--已验收数量\n");
		sql.append("  POP.FORECAST_DATE,--预计到货日期\n");
		sql.append("  POP.WH_ID,\n");
		sql.append("  POP.WH_NAME,\n");
		sql.append("  POP.REMARK,\n");
		sql.append("  TO_CHAR(POP.CREATE_DATE,'YYYY-MM-DD') AS CREATE_DATE,\n");
		sql.append("  POP.ORG_ID,\n");
		sql.append("  POP.STATE,\n");
		sql.append("  POP.LOC_ID,\n");
		sql.append("  POP.LOC_CODE,\n");
		sql.append("  NVL((SELECT SUM(PC.CON_QTY) FROM TT_PART_OEM_PO_CONFIRM PC WHERE PC.PO_ID = POP.PO_ID AND PC.STATUS = 10011001),0) AS YCON_QTY,\n");
		sql.append("  NVL(POP.CHECK_QTY,0) - NVL((SELECT SUM(PC.CON_QTY) FROM TT_PART_OEM_PO_CONFIRM PC WHERE PC.PO_ID = POP.PO_ID AND PC.STATUS = 10011001),0) AS DCON_QTY\n");
		sql.append("FROM\n");
		sql.append("  TT_PART_OEM_PO POP\n");
		sql.append("WHERE\n");
		sql.append("  POP.PO_ID = "+poId+"\n");
		
		Map<String, Object> map = pageQueryMap(sql.toString(), null, getFunName());
		return map;
	}
	/**
	 * 查询采购到货已确认信息
	 * @param request
	 * @param loginUser
	 * @param curPage
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public PageResult<Map<String, Object>> getPurRcvSelectInfo(RequestWrapper request, AclUserBean loginUser, Integer curPage, Integer pageSize) throws Exception{
		StringBuffer sql = new StringBuffer();
		List<Object> params = new ArrayList<Object>();
		
		String ORDER_CODE = request.getParamValue("ORDER_CODE");
		String VENDER_NAME = request.getParamValue("VENDER_NAME");
		String sCreateDate = request.getParamValue("sCreateDate");
		String eCreateDate = request.getParamValue("eCreateDate");
		
		String PART_OLDCODE = request.getParamValue("PART_OLDCODE");
		String PART_CNAME = request.getParamValue("PART_CNAME");
		String PART_CODE = request.getParamValue("PART_CODE");
		
		String IS_PRINT = request.getParamValue("IS_PRINT");
		
		sql.append("SELECT\n");
		sql.append("  POP.PO_ID,\n");
		sql.append("  POP.ORDER_ID,\n");
		sql.append("  POP.ORDER_CODE,\n");
		sql.append("  POP.PLAN_ID,\n");
		sql.append("  POP.PLINE_ID,\n");
		sql.append("  POP.PLAN_CODE,\n");
		sql.append("  POP.PLANER_ID,\n");
		sql.append("  POP.PLANER,\n");
		sql.append("  POP.BUYER_ID,\n");
		sql.append("  POP.BUYER,\n");
		sql.append("  POP.PART_ID,\n");
		sql.append("  POP.PART_OLDCODE,\n");
		sql.append("  POP.PART_CNAME,\n");
		sql.append("  POP.PART_CODE,\n");
		sql.append("  POP.PART_TYPE,--01，02，03\n");
		sql.append("  POP.PRODUCE_FAC,--采购方式\n");
		sql.append("  POP.UNIT,\n");
		sql.append("  POP.VENDER_ID,\n");
		sql.append("  POP.VENDER_CODE,\n");
		sql.append("  POP.VENDER_NAME,\n");
		sql.append("  POP.PLAN_QTY,--计划数量\n");
		sql.append("  POP.BUY_QTY,--采购数量\n");
		sql.append("  POP.BUY_PRICE,--采购单价\n");
		sql.append("  POP.BUY_AMOUNT,--采购金额\n");
		sql.append("  (POP.CHECK_QTY-NVL(POP.IN_QTY,0)) AS DIN_QTY,--待验收数量\n");
		sql.append("  POP.IN_QTY,--已验收数量\n");
		sql.append("  POP.FORECAST_DATE,--预计到货日期\n");
		sql.append("  POP.WH_ID,\n");
		sql.append("  POP.WH_NAME,\n");
		sql.append("  POP.ORG_ID,\n");
		sql.append("  POP.STATE,\n");
		sql.append("  (SELECT T.LOC_ID FROM TT_PART_LOACTION_DEFINE T WHERE T.PART_ID = POP.PART_ID AND T.ORG_ID = POP.ORG_ID AND T.WH_ID = POP.WH_ID) AS LOC_ID,\n");
		sql.append("  (SELECT T.LOC_CODE FROM TT_PART_LOACTION_DEFINE T WHERE T.PART_ID = POP.PART_ID AND T.ORG_ID = POP.ORG_ID AND T.WH_ID = POP.WH_ID) AS LOC_CODE,\n");
		sql.append("  TO_CHAR(PC.CREATE_DATE,'YYYY-MM-DD') AS CREATE_DATE,\n");
		sql.append("  TO_CHAR(PC.PRINT_DATE,'YYYY-MM-DD') AS PRINT_DATE,\n");
		sql.append("  PC.CON_ID,\n");
		sql.append("  PC.CON_QTY,\n");
		sql.append("  PC.IN_QTY AS CON_IN_QTY,\n");
		sql.append("  PC.REMARK,\n");
		sql.append("  PC.STATUS,\n");
		sql.append("  PC.IS_PRINT\n");
		sql.append("FROM\n");
		sql.append("  TT_PART_OEM_PO POP,\n");
		sql.append("  TT_PART_OEM_PO_CONFIRM PC\n");
		sql.append("WHERE\n");
		sql.append("  POP.PO_ID = PC.PO_ID\n");
		sql.append("  AND POP.ORG_ID = "+Constant.OEM_ACTIVITIES+"\n");
		sql.append("  AND PC.STATUS = '"+Constant.STATUS_ENABLE+"'\n");
		if(StringUtil.notNull(ORDER_CODE)){
			sql.append("  AND POP.ORDER_CODE LIKE ?\n");
			params.add("%"+ORDER_CODE.toUpperCase()+"%");
		}
		if(StringUtil.notNull(VENDER_NAME)){
			sql.append("  AND POP.VENDER_NAME LIKE ?\n");
			params.add("%"+VENDER_NAME+"%");
		}
		
		if(StringUtil.notNull(sCreateDate)){
			sql.append("  AND TO_CHAR(PC.CREATE_DATE,'YYYY-MM-DD') >= ?\n");
			params.add(sCreateDate);
		}
		if(StringUtil.notNull(eCreateDate)){
			sql.append("  AND TO_CHAR(PC.CREATE_DATE,'YYYY-MM-DD') <= ?\n");
			params.add(eCreateDate);
		}
		if(StringUtil.notNull(PART_OLDCODE)){
			sql.append("  AND POP.PART_OLDCODE LIKE ?\n");
			params.add("%"+PART_OLDCODE.toUpperCase()+"%");
		}
		if(StringUtil.notNull(PART_CNAME)){
			sql.append("  AND POP.PART_CNAME LIKE ?\n");
			params.add("%"+PART_CNAME+"%");
		}
		if(StringUtil.notNull(PART_CODE)){
			sql.append("  AND POP.PART_CODE LIKE ?\n");
			params.add("%"+PART_CODE.toUpperCase()+"%");
		}
		if(StringUtil.notNull(IS_PRINT)){
			sql.append("  AND PC.IS_PRINT = '"+IS_PRINT+"'\n");
		}
		
		sql.append(" ORDER BY PC.CON_ID ASC\n");
		PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), params, getFunName(), pageSize, curPage);
		return ps;
	}
	/**
	 * 查询确认到货的信息用于打印
	 * @param conId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> getPurConfirmPrintInfoByConId(String po_Id)throws Exception {
		StringBuffer sql = new StringBuffer();
		
		sql.append("select p.order_id,\n" );
		//sql.append("  SEQ_PRINTID.NEXTVAL AS PRINT_XH,--用于打印排序其他地方最好不要使用\n");
		sql.append("       p.order_code,\n" );
		sql.append("       p.plan_id,\n" );
		sql.append("       p.buyer_id,\n" );
		sql.append("       p.buyer,\n" );
		sql.append("       p.part_type,\n" );
		sql.append("       p.produce_fac,\n" );
		sql.append("       p.wh_id,\n" );
		sql.append("       p.wh_name,\n" );
		sql.append("       p1.remark,\n" );
		sql.append("       TO_CHAR(p.create_date, 'YYYY-MM-DD') as CREATE_DATE,\n" );
		sql.append("       p.state,\n" );
		sql.append("       p.status,\n" );
		sql.append("       p.pur_order_code,\n" );
		sql.append("       p.print_by,\n" );
		sql.append("       p.vender_id,\n" );
		sql.append("       p.buyer_type,\n" );
		sql.append("       TO_CHAR(p.PRINT_DATE, 'YYYY-MM-DD') as PRINT_DATE,\n" );
		sql.append("  F_GET_LOCS_CODE(p1.ORG_ID,p1.PART_ID,p.WH_ID) AS LOC_CODE,\n");
		sql.append("       p1.POLINE_ID,\n" );
		sql.append("       p1.part_id,\n" );
		sql.append("       p1.part_code,\n" );
		sql.append("       p1.part_oldcode,\n" );
		sql.append("       p1.part_cname,\n" );
		sql.append("       p1.unit,\n" );
		sql.append("       v.vender_code,\n" );
		sql.append("       v.vender_name,\n" );
		sql.append("       p1.plan_qty,\n" );
		sql.append("       p1.buy_qty,\n" );
		sql.append("  TPD.MIDDLE_PACKAGE,--中包装数量\n");
		sql.append("  TPD.BOX_NUM,--装箱量：一个箱装的中包装数量（若无中包装，则为备件数量）\n");
		sql.append("  TPD.PALLET_NUM,--一个托装的箱子数\n");
		sql.append("  TPD.PALLET_BOX,--托盘箱（每层几个箱子-一个托盘几层）\n");
		sql.append("  ceil(MOD((P1.BUY_QTY/decode(TPD.MIDDLE_PACKAGE,0,'1',TPD.MIDDLE_PACKAGE))/decode(TPD.BOX_NUM,0,'1',TPD.BOX_NUM),TPD.BOX_NUM)*TPD.BOX_NUM*NVL(TPD.MIDDLE_PACKAGE,'1')) AS SYG,\n");
		sql.append("  FLOOR((P1.BUY_QTY/decode(TPD.MIDDLE_PACKAGE,0,'1',TPD.MIDDLE_PACKAGE))/decode(TPD.BOX_NUM,0,'1',TPD.BOX_NUM)) AS BOXZ,\n");
		sql.append("       TO_CHAR(p1.forecast_date, 'YYYY-MM-DD') as forecast_date\n" );
		sql.append("  from tt_part_po_main p\n" );
		sql.append(" inner join tt_part_po_dtl p1\n" );
		sql.append("    on p.order_id = p1.order_id ");
		sql.append(" left join TT_PART_DEFINE TPD\n");
		sql.append("  on p1.PART_ID = TPD.PART_ID\n");
		sql.append(" left join tt_part_vender_define v\n");
		sql.append("  on p1.VENDER_ID = v.VENDER_ID\n");
		sql.append("WHERE\n");
		sql.append("  p1.POLINE_ID = '"+po_Id+"'\n");
		
		sql.append(" ORDER BY p.status ASC,p.CREATE_DATE DESC\n");
		Map<String, Object> map = this.pageQueryMap(sql.toString(), null, getFunName());
		return map;
	}
	
	/**
	 * @param request
	 * @param loginUser
	 * @param curPage
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public PageResult<Map<String, Object>> getPurRcvInwhInfo(RequestWrapper request, AclUserBean loginUser, Integer curPage, Integer pageSize) throws Exception{
		StringBuffer sql = new StringBuffer();
		List<Object> params = new ArrayList<Object>();
		
		String ORDER_CODE = request.getParamValue("ORDER_CODE");
		String VENDER_NAME = request.getParamValue("VENDER_NAME");
		String sCreateDate = request.getParamValue("sCreateDate");
		String eCreateDate = request.getParamValue("eCreateDate");
		
		String PART_OLDCODE = request.getParamValue("PART_OLDCODE");
		String PART_CNAME = request.getParamValue("PART_CNAME");
		String PART_CODE = request.getParamValue("PART_CODE");
		
		sql.append("SELECT\n");
		sql.append("  POP.PO_ID,\n");
		sql.append("  POP.ORDER_ID,\n");
		sql.append("  POP.ORDER_CODE,\n");
		sql.append("  POP.PLAN_ID,\n");
		sql.append("  POP.PLINE_ID,\n");
		sql.append("  POP.PLAN_CODE,\n");
		sql.append("  POP.PLANER_ID,\n");
		sql.append("  POP.PLANER,\n");
		sql.append("  POP.BUYER_ID,\n");
		sql.append("  POP.BUYER,\n");
		sql.append("  POP.PART_ID,\n");
		sql.append("  POP.PART_OLDCODE,\n");
		sql.append("  POP.PART_CNAME,\n");
		sql.append("  POP.PART_CODE,\n");
		sql.append("  POP.PART_TYPE,--01，02，03\n");
		sql.append("  POP.PRODUCE_FAC,--采购方式\n");
		sql.append("  POP.UNIT,\n");
		sql.append("  POP.VENDER_ID,\n");
		sql.append("  POP.VENDER_CODE,\n");
		sql.append("  POP.VENDER_NAME,\n");
		sql.append("  POP.PLAN_QTY,--计划数量\n");
		sql.append("  POP.BUY_QTY,--采购数量\n");
		sql.append("  POP.BUY_PRICE,--采购单价\n");
		sql.append("  POP.BUY_AMOUNT,--采购金额\n");
		sql.append("  (POP.CHECK_QTY-NVL(POP.IN_QTY,0)) AS DIN_QTY,--待验收数量\n");
		sql.append("  POP.IN_QTY,--已验收数量\n");
		sql.append("  POP.FORECAST_DATE,--预计到货日期\n");
		sql.append("  POP.WH_ID,\n");
		sql.append("  POP.WH_NAME,\n");
		sql.append("  POP.ORG_ID,\n");
		sql.append("  POP.STATE,\n");
		sql.append("  (SELECT T.LOC_ID FROM TT_PART_LOACTION_DEFINE T WHERE T.PART_ID = POP.PART_ID AND T.ORG_ID = POP.ORG_ID AND T.WH_ID = POP.WH_ID) AS LOC_ID,\n");
		sql.append("  (SELECT T.LOC_CODE FROM TT_PART_LOACTION_DEFINE T WHERE T.PART_ID = POP.PART_ID AND T.ORG_ID = POP.ORG_ID AND T.WH_ID = POP.WH_ID) AS LOC_CODE,\n");
		sql.append("  TO_CHAR(PC.CREATE_DATE,'YYYY-MM-DD') AS CREATE_DATE,\n");
		sql.append("  TO_CHAR(PC.PRINT_DATE,'YYYY-MM-DD') AS PRINT_DATE,\n");
		sql.append("  PC.CON_ID,\n");
		sql.append("  PC.CON_QTY,\n");
		sql.append("  PC.IN_QTY AS CON_IN_QTY,\n");
		sql.append("  NVL(PC.CON_QTY,0)-NVL(PC.IN_QTY,0) AS CDIN_QTY,\n");
		sql.append("  PC.REMARK,\n");
		sql.append("  PC.STATUS,\n");
		sql.append("  PC.IS_PRINT\n");
		sql.append("FROM\n");
		sql.append("  TT_PART_OEM_PO POP,\n");
		sql.append("  TT_PART_OEM_PO_CONFIRM PC\n");
		sql.append("WHERE\n");
		sql.append("  POP.PO_ID = PC.PO_ID\n");
		sql.append("  AND POP.ORG_ID = "+Constant.OEM_ACTIVITIES+"\n");
		sql.append("  AND PC.STATUS = '"+Constant.STATUS_ENABLE+"'\n");
		sql.append("  AND (NVL(PC.CON_QTY,0)-NVL(PC.IN_QTY,0)) >0\n");
		sql.append("  AND (NVL(POP.CHECK_QTY,0)-NVL(POP.IN_QTY,0)) >0\n");
		if(StringUtil.notNull(ORDER_CODE)){
			sql.append("  AND POP.ORDER_CODE LIKE ?\n");
			params.add("%"+ORDER_CODE.toUpperCase()+"%");
		}
		if(StringUtil.notNull(VENDER_NAME)){
			sql.append("  AND POP.VENDER_NAME LIKE ?\n");
			params.add("%"+VENDER_NAME+"%");
		}
		
		if(StringUtil.notNull(sCreateDate)){
			sql.append("  AND TO_CHAR(PC.CREATE_DATE,'YYYY-MM-DD') >= ?\n");
			params.add(sCreateDate);
		}
		if(StringUtil.notNull(eCreateDate)){
			sql.append("  AND TO_CHAR(PC.CREATE_DATE,'YYYY-MM-DD') <= ?\n");
			params.add(eCreateDate);
		}
		if(StringUtil.notNull(PART_OLDCODE)){
			sql.append("  AND POP.PART_OLDCODE LIKE ?\n");
			params.add("%"+PART_OLDCODE.toUpperCase()+"%");
		}
		if(StringUtil.notNull(PART_CNAME)){
			sql.append("  AND POP.PART_CNAME LIKE ?\n");
			params.add("%"+PART_CNAME+"%");
		}
		if(StringUtil.notNull(PART_CODE)){
			sql.append("  AND POP.PART_CODE LIKE ?\n");
			params.add("%"+PART_CODE.toUpperCase()+"%");
		}
		
		sql.append(" ORDER BY PC.CON_ID ASC\n");
		PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), params, getFunName(), pageSize, curPage);
		return ps;
	}
	
    @SuppressWarnings("unchecked")
	public List<Map<String, Object>> getPurchaseOrderCMSTPrintMain(String poInStr) {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT distinct POP.ORDER_ID,\n" );
        sql.append("                POP.ORDER_CODE,\n" );
        sql.append("                POP.VENDER_NAME,\n" );
        sql.append("                POP.VENDER_CODE,\n" );
        sql.append("                TO_CHAR(SYSDATE,'YYYY-MM-DD') PRINTDATE,\n" );
        sql.append("                TMD.DEALER_ID   AS CMST_ID,\n" );
        sql.append("                TMD.DEALER_CODE AS CMST_CODE,\n" );
        sql.append("                TMD.DEALER_NAME AS CMST_NAME\n" );
        sql.append("\n" );
        sql.append("  FROM TT_PART_OEM_PO POP,\n" );
        sql.append("       TT_PART_DEFINE TPD,\n" );
        sql.append("       TT_PART_CMST   CMST,\n" );
        sql.append("       TM_DEALER      TMD\n" );
        sql.append(" WHERE POP.PART_ID = TPD.PART_ID\n" );
        sql.append("   AND POP.PART_ID = CMST.PART_ID\n" );
        sql.append("   AND CMST.CMST_ID = TMD.DEALER_ID\n" );
        sql.append("   and pop.po_id in ("+poInStr+") ");
        sql.append(" order by  POP.VENDER_CODE \n");
       

        return this.pageQuery(sql.toString(), null, this.getFunName());
    }
    
    
    @SuppressWarnings("unchecked")
	public List<Map<String, Object>> getPurchaseOrderCMSTPrintDtl(String poInStr,String orderId) {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT POP.po_id,POP.ORDER_ID,\n" );
        sql.append("                POP.ORDER_CODE,\n" );
        sql.append("                POP.VENDER_NAME,\n" );
        sql.append("                POP.VENDER_CODE,\n" );
        sql.append("  				POP.PART_ID,\n");
		sql.append("  				POP.PART_OLDCODE,\n");
		sql.append("  				POP.PART_CNAME,\n");
		sql.append("  				POP.PART_CODE,\n");
		sql.append("  				POP.UNIT,\n");
		sql.append("  				POP.CHECK_QTY,\n");
		sql.append("  				POP.IN_QTY,\n");
		sql.append("  				(POP.CHECK_QTY - POP.IN_QTY ) lessQty,\n");
        sql.append("                TMD.DEALER_ID   AS CMST_ID,\n" );
        sql.append("                TMD.DEALER_CODE AS CMST_CODE,\n" );
        sql.append("                TMD.DEALER_NAME AS CMST_NAME\n" );
        sql.append("\n" );
        sql.append("  FROM TT_PART_OEM_PO POP,\n" );
        sql.append("       TT_PART_DEFINE TPD,\n" );
        sql.append("       TT_PART_CMST   CMST,\n" );
        sql.append("       TM_DEALER      TMD\n" );
        sql.append(" WHERE POP.PART_ID = TPD.PART_ID\n" );
        sql.append("   AND POP.PART_ID = CMST.PART_ID\n" );
        sql.append("   AND CMST.CMST_ID = TMD.DEALER_ID\n" );
        sql.append("   and pop.po_id in ("+poInStr+") ");
        sql.append("   and pop.ORDER_ID ="+orderId+" ");
        sql.append(" order by pop.PART_OLDCODE \n");
       

        return this.pageQuery(sql.toString(), null, this.getFunName());
    }
	
    /**
     * 创建采购订单打印历史记录
     * @param poInStr
     * @return
     */
    @SuppressWarnings("unchecked")
	public void createOemOrderPrintLog(String poInStr,AclUserBean loginUser,String printQty) {
        StringBuffer sql = new StringBuffer();
        List<Object> params  = new ArrayList<Object>();
        sql.append("insert into tt_part_oem_po_send_print_log\n" );
        sql.append("  (po_id, print_times, print_by, print_date, print_qty, print_id)\n" );
        sql.append("  select ?, count(1) + 1, ?, sysdate, ?, f_getid\n" );
        sql.append("    from tt_part_oem_po_send_print_log t\n" );
        sql.append("   where t.po_id = ? ");
        sql.append("\n");
        params.add(poInStr);
        params.add(loginUser.getUserId());
        params.add(printQty);
        params.add(poInStr);

        this.update(sql.toString(), params);
    }
    
	/**
	 * 
	 * @param request
	 * @param loginUser
	 * @param curPage
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public PageResult<Map<String, Object>> getPurRcvPrintInfo(RequestWrapper request, AclUserBean loginUser, Integer curPage, Integer pageSize) throws Exception {
		StringBuffer sql = new StringBuffer();
		List<Object> params = new ArrayList<Object>();
		
		String ORDER_CODE = request.getParamValue("ORDER_CODE");
		String VENDER_NAME = request.getParamValue("VENDER_NAME");
		String sCreateDate = request.getParamValue("sCreateDate");
		String eCreateDate = request.getParamValue("eCreateDate");
		
		String PART_OLDCODE = request.getParamValue("PART_OLDCODE");
		String PART_CNAME = request.getParamValue("PART_CNAME");
		String PART_CODE = request.getParamValue("PART_CODE");
		String inId =  request.getParamValue("inId");
		
		sql.append("select p.order_id,\n" );
		sql.append("       p.order_code,\n" );
		sql.append("       p.plan_id,\n" );
		sql.append("       p.buyer_id,\n" );
		sql.append("       p.buyer,\n" );
		sql.append("       p.part_type,\n" );
		sql.append("       p.produce_fac,\n" );
		sql.append("       p.wh_id,\n" );
		sql.append("       p.wh_name,\n" );
		sql.append("       p.remark,\n" );
		sql.append("       TO_CHAR(p.create_date, 'YYYY-MM-DD') as CREATE_DATE,\n" );
		sql.append("       p.state,\n" );
		sql.append("       p.status,\n" );
		sql.append("       p.pur_order_code,\n" );
		sql.append("       p.print_by,\n" );
		sql.append("       p.vender_id,\n" );
		sql.append("       p.buyer_type,\n" );
		sql.append("       TO_CHAR(p.PRINT_DATE, 'YYYY-MM-DD') as PRINT_DATE,\n" );
		sql.append("  F_GET_LOCS_CODE(p1.ORG_ID,p1.PART_ID,p.WH_ID) AS LOC_CODE,\n");
		sql.append("       p1.POLINE_ID,\n" );
		sql.append("       p1.part_id,\n" );
		sql.append("       p1.part_code,\n" );
		sql.append("       p1.part_oldcode,\n" );
		sql.append("       p1.part_cname,\n" );
		sql.append("       p1.unit,\n" );
		sql.append("       p1.vender_name,\n" );
		sql.append("       p1.plan_qty,\n" );
		sql.append("       p1.buy_qty,\n" );
		sql.append("  TPD.MIDDLE_PACKAGE,--中包装数量\n");
		sql.append("  TPD.BOX_NUM,--装箱量：一个箱装的中包装数量（若无中包装，则为备件数量）\n");
		sql.append("  TPD.PALLET_NUM,--一个托装的箱子数\n");
		sql.append("  TPD.PALLET_BOX,--托盘箱（每层几个箱子-一个托盘几层）\n");
		sql.append("  MOD((P1.BUY_QTY/decode(TPD.MIDDLE_PACKAGE,0,'1',TPD.MIDDLE_PACKAGE))/decode(TPD.BOX_NUM,0,'1',TPD.BOX_NUM),TPD.BOX_NUM)*TPD.BOX_NUM*NVL(TPD.MIDDLE_PACKAGE,'1') AS SYG,\n");
		sql.append("  FLOOR((P1.BUY_QTY/decode(TPD.MIDDLE_PACKAGE,0,'1',TPD.MIDDLE_PACKAGE))/decode(TPD.BOX_NUM,0,'1',TPD.BOX_NUM)) AS BOXZ,\n");
		sql.append("       TO_CHAR(p1.forecast_date, 'YYYY-MM-DD') as forecast_date\n" );
		sql.append("  from tt_part_po_main p\n" );
		sql.append(" inner join tt_part_po_dtl p1\n" );
		sql.append("    on p.order_id = p1.order_id ");
		sql.append(" left join TT_PART_DEFINE TPD\n");
		sql.append("  on p1.PART_ID = TPD.PART_ID where 1=1\n");
		
		if(StringUtil.notNull(ORDER_CODE)){
			sql.append("  AND p.order_code LIKE ?\n");
			params.add("%"+ORDER_CODE.toUpperCase()+"%");
		}
		if(StringUtil.notNull(VENDER_NAME)){
			sql.append("  AND p1.vender_name LIKE ?\n");
			params.add("%"+VENDER_NAME+"%");
		}
		
		if(StringUtil.notNull(sCreateDate)){
			sql.append("  AND TO_CHAR(p.CREATE_DATE,'YYYY-MM-DD') >= ?\n");
			params.add(sCreateDate);
		}
		if(StringUtil.notNull(eCreateDate)){
			sql.append("  AND TO_CHAR(p.CREATE_DATE,'YYYY-MM-DD') <= ?\n");
			params.add(eCreateDate);
		}
		if(StringUtil.notNull(PART_OLDCODE)){
			sql.append("  AND p1.part_oldcode LIKE ?\n");
			params.add("%"+PART_OLDCODE.toUpperCase()+"%");
		}
		if(StringUtil.notNull(PART_CNAME)){
			sql.append("  AND p1.part_cname LIKE ?\n");
			params.add("%"+PART_CNAME+"%");
		}
		if(StringUtil.notNull(PART_CODE)){
			sql.append("  AND p1.part_code LIKE ?\n");
			params.add("%"+PART_CODE.toUpperCase()+"%");
		}
		if(StringUtil.notNull(inId)){
			sql.append("  AND p1.POLINE_ID = "+inId+"\n");
		}
		
		sql.append(" ORDER BY p.status ASC,p.CREATE_DATE DESC\n");
		PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), params, getFunName(), pageSize, curPage);
		return ps;
	}
	
	/**
	 * 获取采购人员
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getPurUserInfos() throws Exception{
		StringBuffer sql = new StringBuffer();
		List<Object> params = new ArrayList<Object>();
		
		sql.append("SELECT\n");
		sql.append("  PU.DEFINE_ID,\n");
		sql.append("  PU.USER_ID,\n");
		sql.append("  PU.USER_TYPE,\n");
		sql.append("  U.NAME,\n");
		sql.append("  PF.FIX_GROUPNAME,\n");
		sql.append("  PF.FIX_NAME,\n");
		sql.append("  PU.STATE,\n");
		sql.append("  PU.IS_LEADER,\n");
		sql.append("  PU.IS_DIRECT,\n");
		sql.append("  PU.IS_CHKZY\n");
		sql.append("FROM\n");
		sql.append("  TC_USER U,\n");
		sql.append("  TT_PART_USERPOSE_DEFINE PU,\n");
		sql.append("  TT_PART_FIXCODE_DEFINE PF\n");
		sql.append("WHERE\n");
		sql.append("  PU.USER_ID = U .USER_ID\n");
		sql.append("AND PF.FIX_VALUE = PU.USER_TYPE\n");
		sql.append("AND U .USER_STATUS = '10011001'\n");//有效人员
		sql.append("AND PF.FIX_VALUE = '2'\n");
		sql.append("AND PU.STATE = 10011001\n");
		sql.append("AND PF.FIX_GOUPTYPE = 92251001\n");
		sql.append("AND PF.STATE = 10011001\n");//有效人员类型
		sql.append("ORDER BY\n");
		sql.append("  PF.FIX_NAME\n");

		List<Map<String, Object>> list = pageQuery(sql.toString(), params, getFunName());
		return list;
	}
	
	/**
	 * 
	 * @param planLineId
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public String getSplitErrInfoByPlanLineId(String planLineId) throws Exception{
		String rs = "";
		StringBuffer sql = new StringBuffer();
		List<Object> params = new ArrayList<Object>();
		
		sql.append("SELECT\n");
		sql.append("  (CASE\n");
		sql.append("    WHEN (NVL(DTL.WEEK_ONE,0)+ NVL(DTL.WEEK_TOW,0) + NVL(DTL.WEEK_THREE,0) + NVL(DTL.WEEK_FOUR,0) + NVL(DTL.WEEK_FIVE,0)) > NVL(DTL.PLAN_MONTH_ONE,0)\n");
		sql.append("    THEN '备件[' || DTL.PART_OLDCODE || ']拆分数量和大于计划数量，无法修改!<br>'\n");
		sql.append("    ELSE ''\n");
		sql.append("  END) AS ERRINFO\n");
		sql.append("FROM\n");
		sql.append("  TT_PART_PLAN_SCROLL_DEL DTL\n");
		sql.append("WHERE\n");
		sql.append("  DTL.ID = ?\n");
		params.add(planLineId);
		
		Map<String, Object> map = pageQueryMap(sql.toString(), params, getFunName());
		if(map!=null){
			rs = CommonUtils.checkNull(map.get("ERRINFO"));
		}
		return rs;
	}
	/**
	 * 计划全部确认
	 * @param loginUser 
	 * @param request
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public int optLastConfirmAll(AclUserBean loginUser, RequestWrapper request) throws Exception{
		String PLAN_YEAR_SELECT = request.getParamValue("PLAN_YEAR_SELECT");
		String PLAN_MONTH_SELECT = request.getParamValue("PLAN_MONTH_SELECT");
		
		if(StringUtil.isNull(PLAN_YEAR_SELECT)){
			throw new Exception("请选择计划年月-年");
		}
		if(StringUtil.isNull(PLAN_MONTH_SELECT)){
			throw new Exception("请选择计划年月-月");
		}
		
		String yearMonth = CommonUtils.checkNull(PLAN_YEAR_SELECT)+CommonUtils.checkNull(PLAN_MONTH_SELECT);
		
		//---------获取明细ID---------------------------------------
		StringBuffer sbSql = new StringBuffer();
		sbSql.append("    SELECT\n");
		sbSql.append("      T2.ID AS PLAN_LINE_ID\n");
		sbSql.append("    FROM\n");
		sbSql.append("      TT_PART_PLAN_SCROLL T1,\n");
		sbSql.append("      TT_PART_PLAN_SCROLL_DEL T2,\n");
		sbSql.append("      TT_PART_DEFINE TPD\n");
		sbSql.append("    WHERE\n");
		sbSql.append("      T1.ID = T2.PLAN_ID\n");
		sbSql.append("      AND T2.PART_ID = TPD.PART_ID\n");
		sbSql.append("      AND T1.PLAN_TYPES = 92111001--滚动计划\n");
		sbSql.append("      AND T1.IS_SUBMIT = 10041001--已提交\n");
		sbSql.append("      AND NOT EXISTS(SELECT 1 FROM TT_PART_SPLIT_DEFINE SP WHERE SP.PART_ID = TPD.PART_ID)--不确认总成只确认分总成\n");
		sbSql.append("      AND T2.FIRST_CONFIRM_STATE = 10041001--总装涂装轻型车供应商已确认\n");
		sbSql.append("      AND T2.LAST_CONFIRM_STATE = 10041002--计划室未确认\n");
		sbSql.append("      AND T1.MONTH_DATE = '"+yearMonth+"'\n");
		
		List<Map<String,Object>> list = pageQuery(sbSql.toString(), null, getFunName());
		//---------获取明细ID---------------------------------------
		
		StringBuffer sql = new StringBuffer();
		sql.append("UPDATE TT_PART_PLAN_SCROLL_DEL DTL\n");
		sql.append("SET DTL.LAST_CONFIRM_BY = '"+loginUser.getUserId()+"',\n");
		sql.append("    DTL.LAST_CONFIRM_DATE = SYSDATE,\n");
		sql.append("    DTL.LAST_CONFIRM_NUM = DTL.FIRST_CONFIRM_NUM,\n");
		sql.append("    DTL.LAST_CONFIRM_REMARK = '',\n");
		sql.append("    DTL.LAST_CONFIRM_STATE = 10041001\n");
		sql.append("WHERE\n");
		sql.append("  DTL.ID IN(\n");
		sql.append("    SELECT\n");
		sql.append("      T2.ID\n");
		sql.append("    FROM\n");
		sql.append("      TT_PART_PLAN_SCROLL T1,\n");
		sql.append("      TT_PART_PLAN_SCROLL_DEL T2,\n");
		sql.append("      TT_PART_DEFINE TPD\n");
		sql.append("    WHERE\n");
		sql.append("      T1.ID = T2.PLAN_ID\n");
		sql.append("      AND T2.PART_ID = TPD.PART_ID\n");
		sql.append("      AND T1.PLAN_TYPES = 92111001--滚动计划\n");
		sql.append("      AND T1.IS_SUBMIT = 10041001--已提交\n");
		sql.append("      AND NOT EXISTS(SELECT 1 FROM TT_PART_SPLIT_DEFINE SP WHERE SP.PART_ID = TPD.PART_ID)--不确认总成只确认分总成\n");
		sql.append("      AND T2.FIRST_CONFIRM_STATE = 10041001--总装涂装轻型车供应商已确认\n");
		sql.append("      AND T2.LAST_CONFIRM_STATE = 10041002--计划室未确认\n");
		sql.append("      AND T1.MONTH_DATE = '"+yearMonth+"'\n");
		sql.append(")\n");
		
		int rs = dao.update(sql.toString(), null);
		
		
		if(list!=null && !list.isEmpty() && list.size()>0){
			for (int i = 0; i < list.size(); i++) {
				Map<String,Object> map = list.get(i);
				String planLineId = CommonUtils.checkNull(map.get("PLAN_LINE_ID"));
				if(StringUtil.notNull(planLineId)){
					//自动平均拆分滚动周度订单数量
					List<Object> ins = new LinkedList<Object>();
					ins.add(planLineId);
					dao.callProcedure("PKG_PLAN.P_QXC_AUTO_PLAN_WEEK_SPLIT_MX",ins,null);
				}
			}
		}
		
		return rs;
	}
	/**
	 * 查询采购订单信息
	 * @param request
	 * @param loginUser
	 * @param curPage
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public PageResult<Map<String, Object>> getPurPickInfo(RequestWrapper request, AclUserBean loginUser, Integer curPage, Integer pageSize) throws Exception{
		StringBuffer sql = new StringBuffer();
		List<Object> params = new ArrayList<Object>();
		
		String ORDER_CODE = request.getParamValue("ORDER_CODE");
		String VENDER_NAME = request.getParamValue("VENDER_NAME");
		String sCreateDate = request.getParamValue("sCreateDate");
		String eCreateDate = request.getParamValue("eCreateDate");
		
		String PART_OLDCODE = request.getParamValue("PART_OLDCODE");
		String PART_CNAME = request.getParamValue("PART_CNAME");
		String PART_CODE = request.getParamValue("PART_CODE");
		
		String IS_PICKZ = request.getParamValue("IS_PICKZ");
		String BUYER_ID = request.getParamValue("BUYER_ID");
		
		sql.append("SELECT TEMP.* FROM(\n");
		sql.append("SELECT\n");
		sql.append("  POP.PO_ID,\n");
		sql.append("  POP.ORDER_ID,\n");
		sql.append("  POP.ORDER_CODE,\n");
		sql.append("  POP.PLAN_ID,\n");
		sql.append("  POP.PLINE_ID,\n");
		sql.append("  POP.PLAN_CODE,\n");
		sql.append("  POP.PLANER_ID,\n");
		sql.append("  POP.PLANER,\n");
		sql.append("  POP.BUYER_ID,\n");
		sql.append("  POP.BUYER,\n");
		sql.append("  POP.PART_ID,\n");
		sql.append("  POP.PART_OLDCODE,\n");
		sql.append("  POP.PART_CNAME,\n");
		sql.append("  POP.PART_CODE,\n");
		sql.append("  POP.PART_TYPE,--01，02，03\n");
		sql.append("  POP.PRODUCE_FAC,--采购方式\n");
		sql.append("  POP.UNIT,\n");
		sql.append("  POP.VENDER_ID,\n");
		sql.append("  POP.VENDER_CODE,\n");
		sql.append("  POP.VENDER_NAME,\n");
		sql.append("  POP.PLAN_QTY,--计划数量\n");
		sql.append("  POP.BUY_QTY,--采购数量\n");
		sql.append("  POP.BUY_PRICE,--采购单价\n");
		sql.append("  POP.BUY_AMOUNT,--采购金额\n");
		sql.append("  (POP.CHECK_QTY-NVL(POP.IN_QTY,0)) AS DIN_QTY,--待验收数量\n");
		sql.append("  POP.IN_QTY,--已验收数量\n");
		sql.append("  POP.FORECAST_DATE,--预计到货日期\n");
		sql.append("  POP.WH_ID,\n");
		sql.append("  POP.WH_NAME,\n");
		sql.append("  POP.REMARK,\n");
		sql.append("  TO_CHAR(POP.CREATE_DATE,'YYYY-MM-DD') AS CREATE_DATE,\n");
		sql.append("  POP.ORG_ID,\n");
		sql.append("  POP.STATE,\n");
		sql.append("  (SELECT T.LOC_ID FROM TT_PART_LOACTION_DEFINE T WHERE T.PART_ID = POP.PART_ID AND T.ORG_ID = POP.ORG_ID AND T.WH_ID = POP.WH_ID) AS LOC_ID,\n");
		sql.append("  (SELECT T.LOC_CODE FROM TT_PART_LOACTION_DEFINE T WHERE T.PART_ID = POP.PART_ID AND T.ORG_ID = POP.ORG_ID AND T.WH_ID = POP.WH_ID) AS LOC_CODE,\n");
		sql.append("  NVL((SELECT COUNT(1) FROM TT_PART_OEM_PO_PICK PC WHERE PC.PO_ID = POP.PO_ID AND PC.STATUS = 10011001),0) AS PICK_CS,\n");
		sql.append("  NVL((SELECT SUM(PC.PICK_QTY) FROM TT_PART_OEM_PO_PICK PC WHERE PC.PO_ID = POP.PO_ID AND PC.STATUS = 10011001),0) AS YPICK_QTY,\n");
		sql.append("  NVL(POP.CHECK_QTY,0) - NVL((SELECT SUM(PC.PICK_QTY) FROM TT_PART_OEM_PO_PICK PC WHERE PC.PO_ID = POP.PO_ID AND PC.STATUS = 10011001),0) AS DPICK_QTY\n");
		sql.append("FROM\n");
		sql.append("  TT_PART_OEM_PO POP\n");
		sql.append("WHERE\n");
		sql.append("  POP.ORG_ID = 2010010100070674\n");
		sql.append("  AND POP.STATE = "+Constant.PART_PURCHASE_ORDERCHK_STATUS_01+"\n");//待确认的才能入库
		sql.append("  AND NOT EXISTS(SELECT 1 FROM TT_PART_OEM_PO TPOP WHERE TPOP.PO_ID = POP.PO_ID AND TPOP.PART_OLDCODE LIKE '%-DY' AND TPOP.PRODUCE_FAC = 92811006)--自制件采购\n");
		sql.append("  AND (NVL(POP.CHECK_QTY,0) - NVL(POP.IN_QTY,0)) > 0\n");
		
		if(StringUtil.notNull(ORDER_CODE)){
			sql.append("  AND POP.ORDER_CODE LIKE ?\n");
			params.add("%"+ORDER_CODE.toUpperCase()+"%");
		}
		if(StringUtil.notNull(VENDER_NAME)){
			sql.append("  AND POP.VENDER_NAME LIKE ?\n");
			params.add("%"+VENDER_NAME+"%");
		}
		
		if(StringUtil.notNull(sCreateDate)){
			sql.append("  AND TO_CHAR(POP.CREATE_DATE,'YYYY-MM-DD') >= ?\n");
			params.add(sCreateDate);
		}
		if(StringUtil.notNull(eCreateDate)){
			sql.append("  AND TO_CHAR(POP.CREATE_DATE,'YYYY-MM-DD') <= ?\n");
			params.add(eCreateDate);
		}
		if(StringUtil.notNull(PART_OLDCODE)){
			sql.append("  AND POP.PART_OLDCODE LIKE ?\n");
			params.add("%"+PART_OLDCODE.toUpperCase()+"%");
		}
		if(StringUtil.notNull(PART_CNAME)){
			sql.append("  AND POP.PART_CNAME LIKE ?\n");
			params.add("%"+PART_CNAME+"%");
		}
		if(StringUtil.notNull(PART_CODE)){
			sql.append("  AND POP.PART_CODE LIKE ?\n");
			params.add("%"+PART_CODE.toUpperCase()+"%");
		}
		if(StringUtil.notNull(BUYER_ID)){
			sql.append("  AND POP.BUYER_ID = ?\n");
			params.add(BUYER_ID);
		}
		sql.append(")TEMP\n");
		sql.append("WHERE 1=1\n");
		
		if(Constant.IF_TYPE_YES.toString().equals(IS_PICKZ)){
			sql.append(" AND YPICK_QTY > 0\n");
		}else if(Constant.IF_TYPE_NO.toString().equals(IS_PICKZ)){
			sql.append(" AND YPICK_QTY = 0\n");
		}else{
		}
		
		sql.append(" ORDER BY TEMP.ORDER_CODE ASC,TEMP.CREATE_DATE ASC\n");
		
		PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), params, getFunName(), pageSize, curPage);
		return ps;
	}
	/**
	 * 查询采购提货确认信息
	 * @param request
	 * @param loginUser
	 * @param curPage
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public PageResult<Map<String, Object>> getPurPickSelectInfo(RequestWrapper request, AclUserBean loginUser, Integer curPage, Integer pageSize) throws Exception{
		StringBuffer sql = new StringBuffer();
		List<Object> params = new ArrayList<Object>();
		
		String ORDER_CODE = request.getParamValue("ORDER_CODE");
		String VENDER_NAME = request.getParamValue("VENDER_NAME");
		String sCreateDate = request.getParamValue("sCreateDate");
		String eCreateDate = request.getParamValue("eCreateDate");
		
		String PART_OLDCODE = request.getParamValue("PART_OLDCODE");
		String PART_CNAME = request.getParamValue("PART_CNAME");
		String PART_CODE = request.getParamValue("PART_CODE");
		
		String IS_PRINT = request.getParamValue("IS_PRINT");
		String BUYER_ID = request.getParamValue("BUYER_ID");
		
		sql.append("SELECT\n");
		sql.append("  POP.PO_ID,\n");
		sql.append("  POP.ORDER_ID,\n");
		sql.append("  POP.ORDER_CODE,\n");
		sql.append("  POP.PLAN_ID,\n");
		sql.append("  POP.PLINE_ID,\n");
		sql.append("  POP.PLAN_CODE,\n");
		sql.append("  POP.PLANER_ID,\n");
		sql.append("  POP.PLANER,\n");
		sql.append("  POP.BUYER_ID,\n");
		sql.append("  POP.BUYER,\n");
		sql.append("  POP.PART_ID,\n");
		sql.append("  POP.PART_OLDCODE,\n");
		sql.append("  POP.PART_CNAME,\n");
		sql.append("  POP.PART_CODE,\n");
		sql.append("  POP.PART_TYPE,--01，02，03\n");
		sql.append("  POP.PRODUCE_FAC,--采购方式\n");
		sql.append("  POP.UNIT,\n");
		sql.append("  POP.VENDER_ID,\n");
		sql.append("  POP.VENDER_CODE,\n");
		sql.append("  POP.VENDER_NAME,\n");
		sql.append("  POP.PLAN_QTY,--计划数量\n");
		sql.append("  POP.BUY_QTY,--采购数量\n");
		sql.append("  POP.BUY_PRICE,--采购单价\n");
		sql.append("  POP.BUY_AMOUNT,--采购金额\n");
		sql.append("  (POP.CHECK_QTY-NVL(POP.IN_QTY,0)) AS DIN_QTY,--待验收数量\n");
		sql.append("  POP.IN_QTY,--已验收数量\n");
		sql.append("  POP.FORECAST_DATE,--预计到货日期\n");
		sql.append("  POP.WH_ID,\n");
		sql.append("  POP.WH_NAME,\n");
		sql.append("  POP.ORG_ID,\n");
		sql.append("  POP.STATE,\n");
		sql.append("  (SELECT T.LOC_ID FROM TT_PART_LOACTION_DEFINE T WHERE T.PART_ID = POP.PART_ID AND T.ORG_ID = POP.ORG_ID AND T.WH_ID = POP.WH_ID) AS LOC_ID,\n");
		sql.append("  (SELECT T.LOC_CODE FROM TT_PART_LOACTION_DEFINE T WHERE T.PART_ID = POP.PART_ID AND T.ORG_ID = POP.ORG_ID AND T.WH_ID = POP.WH_ID) AS LOC_CODE,\n");
		sql.append("  TO_CHAR(PC.CREATE_DATE,'YYYY-MM-DD') AS CREATE_DATE,\n");
		sql.append("  TO_CHAR(PC.PRINT_DATE,'YYYY-MM-DD') AS PRINT_DATE,\n");
		sql.append("  PC.PICK_ID,\n");
		sql.append("  PC.PICK_QTY,\n");
		sql.append("  PC.REMARK,\n");
		sql.append("  PC.STATUS,\n");
		sql.append("  PC.IS_PRINT\n");
		sql.append("FROM\n");
		sql.append("  TT_PART_OEM_PO POP,\n");
		sql.append("  TT_PART_OEM_PO_PICK PC\n");
		sql.append("WHERE\n");
		sql.append("  POP.PO_ID = PC.PO_ID\n");
		sql.append("  AND POP.ORG_ID = "+Constant.OEM_ACTIVITIES+"\n");
		sql.append("  AND PC.STATUS = '"+Constant.STATUS_ENABLE+"'\n");
		if(StringUtil.notNull(ORDER_CODE)){
			sql.append("  AND POP.ORDER_CODE LIKE ?\n");
			params.add("%"+ORDER_CODE.toUpperCase()+"%");
		}
		if(StringUtil.notNull(VENDER_NAME)){
			sql.append("  AND POP.VENDER_NAME LIKE ?\n");
			params.add("%"+VENDER_NAME+"%");
		}
		
		if(StringUtil.notNull(sCreateDate)){
			sql.append("  AND TO_CHAR(PC.CREATE_DATE,'YYYY-MM-DD') >= ?\n");
			params.add(sCreateDate);
		}
		if(StringUtil.notNull(eCreateDate)){
			sql.append("  AND TO_CHAR(PC.CREATE_DATE,'YYYY-MM-DD') <= ?\n");
			params.add(eCreateDate);
		}
		if(StringUtil.notNull(PART_OLDCODE)){
			sql.append("  AND POP.PART_OLDCODE LIKE ?\n");
			params.add("%"+PART_OLDCODE.toUpperCase()+"%");
		}
		if(StringUtil.notNull(PART_CNAME)){
			sql.append("  AND POP.PART_CNAME LIKE ?\n");
			params.add("%"+PART_CNAME+"%");
		}
		if(StringUtil.notNull(PART_CODE)){
			sql.append("  AND POP.PART_CODE LIKE ?\n");
			params.add("%"+PART_CODE.toUpperCase()+"%");
		}
		if(StringUtil.notNull(IS_PRINT)){
			sql.append("  AND PC.IS_PRINT = '"+IS_PRINT+"'\n");
		}
		if(StringUtil.notNull(BUYER_ID)){
			sql.append("  AND POP.BUYER_ID = '"+BUYER_ID+"'\n");
		}
		sql.append(" ORDER BY PC.PICK_ID ASC\n");
		PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), params, getFunName(), pageSize, curPage);
		return ps;
	}
	/**
	 * 查询采购提货确认信息
	 * @param request
	 * @param loginUser
	 * @param curPage
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public PageResult<Map<String, Object>> getPurPickPrintInfo(RequestWrapper request, AclUserBean loginUser, Integer curPage, Integer pageSize) throws Exception{
		StringBuffer sql = new StringBuffer();
		List<Object> params = new ArrayList<Object>();
		
		String ORDER_CODE = request.getParamValue("ORDER_CODE");
		String VENDER_NAME = request.getParamValue("VENDER_NAME");
		String sCreateDate = request.getParamValue("sCreateDate");
		String eCreateDate = request.getParamValue("eCreateDate");
		
		String PART_OLDCODE = request.getParamValue("PART_OLDCODE");
		String PART_CNAME = request.getParamValue("PART_CNAME");
		String PART_CODE = request.getParamValue("PART_CODE");
		
		String IS_PRINT = request.getParamValue("IS_PRINT");
		String BUYER_ID = request.getParamValue("BUYER_ID");
		
		sql.append("SELECT\n");
		sql.append("  POP.PO_ID,\n");
		sql.append("  POP.ORDER_ID,\n");
		sql.append("  POP.ORDER_CODE,\n");
		sql.append("  POP.PLAN_ID,\n");
		sql.append("  POP.PLINE_ID,\n");
		sql.append("  POP.PLAN_CODE,\n");
		sql.append("  POP.PLANER_ID,\n");
		sql.append("  POP.PLANER,\n");
		sql.append("  POP.BUYER_ID,\n");
		sql.append("  POP.BUYER,\n");
		sql.append("  POP.PART_ID,\n");
		sql.append("  POP.PART_OLDCODE,\n");
		sql.append("  POP.PART_CNAME,\n");
		sql.append("  POP.PART_CODE,\n");
		sql.append("  POP.PART_TYPE,--01，02，03\n");
		sql.append("  POP.PRODUCE_FAC,--采购方式\n");
		sql.append("  POP.UNIT,\n");
		sql.append("  POP.VENDER_ID,\n");
		sql.append("  POP.VENDER_CODE,\n");
		sql.append("  POP.VENDER_NAME,\n");
		sql.append("  POP.PLAN_QTY,--计划数量\n");
		sql.append("  POP.BUY_QTY,--采购数量\n");
		sql.append("  POP.BUY_PRICE,--采购单价\n");
		sql.append("  POP.BUY_AMOUNT,--采购金额\n");
		sql.append("  (POP.CHECK_QTY-NVL(POP.IN_QTY,0)) AS DIN_QTY,--待验收数量\n");
		sql.append("  POP.IN_QTY,--已验收数量\n");
		sql.append("  POP.FORECAST_DATE,--预计到货日期\n");
		sql.append("  POP.WH_ID,\n");
		sql.append("  POP.WH_NAME,\n");
		sql.append("  POP.ORG_ID,\n");
		sql.append("  POP.STATE,\n");
		sql.append("  (SELECT T.LOC_ID FROM TT_PART_LOACTION_DEFINE T WHERE T.PART_ID = POP.PART_ID AND T.ORG_ID = POP.ORG_ID AND T.WH_ID = POP.WH_ID) AS LOC_ID,\n");
		sql.append("  (SELECT T.LOC_CODE FROM TT_PART_LOACTION_DEFINE T WHERE T.PART_ID = POP.PART_ID AND T.ORG_ID = POP.ORG_ID AND T.WH_ID = POP.WH_ID) AS LOC_CODE,\n");
		sql.append("  TO_CHAR(PC.CREATE_DATE,'YYYY-MM-DD') AS CREATE_DATE,\n");
		sql.append("  TO_CHAR(PC.PRINT_DATE,'YYYY-MM-DD') AS PRINT_DATE,\n");
		sql.append("  PC.PICK_ID,\n");
		sql.append("  PC.PICK_QTY,\n");
		sql.append("  PC.REMARK,\n");
		sql.append("  PC.STATUS,\n");
		sql.append("  PC.IS_PRINT,\n");
		sql.append("  PC.PRINT_NUM\n");
		sql.append("FROM\n");
		sql.append("  TT_PART_OEM_PO POP,\n");
		sql.append("  TT_PART_OEM_PO_PICK PC\n");
		sql.append("WHERE\n");
		sql.append("  POP.PO_ID = PC.PO_ID\n");
		sql.append("  AND POP.ORG_ID = "+Constant.OEM_ACTIVITIES+"\n");
		sql.append("  AND PC.STATUS = '"+Constant.STATUS_ENABLE+"'\n");
		if(StringUtil.notNull(ORDER_CODE)){
			sql.append("  AND POP.ORDER_CODE LIKE ?\n");
			params.add("%"+ORDER_CODE.toUpperCase()+"%");
		}
		if(StringUtil.notNull(VENDER_NAME)){
			sql.append("  AND POP.VENDER_NAME LIKE ?\n");
			params.add("%"+VENDER_NAME+"%");
		}
		
		if(StringUtil.notNull(sCreateDate)){
			sql.append("  AND TO_CHAR(PC.CREATE_DATE,'YYYY-MM-DD') >= ?\n");
			params.add(sCreateDate);
		}
		if(StringUtil.notNull(eCreateDate)){
			sql.append("  AND TO_CHAR(PC.CREATE_DATE,'YYYY-MM-DD') <= ?\n");
			params.add(eCreateDate);
		}
		if(StringUtil.notNull(PART_OLDCODE)){
			sql.append("  AND POP.PART_OLDCODE LIKE ?\n");
			params.add("%"+PART_OLDCODE.toUpperCase()+"%");
		}
		if(StringUtil.notNull(PART_CNAME)){
			sql.append("  AND POP.PART_CNAME LIKE ?\n");
			params.add("%"+PART_CNAME+"%");
		}
		if(StringUtil.notNull(PART_CODE)){
			sql.append("  AND POP.PART_CODE LIKE ?\n");
			params.add("%"+PART_CODE.toUpperCase()+"%");
		}
		if(StringUtil.notNull(IS_PRINT)){
			sql.append("  AND PC.IS_PRINT = '"+IS_PRINT+"'\n");
		}
		if(StringUtil.notNull(BUYER_ID)){
			sql.append("  AND POP.BUYER_ID = '"+BUYER_ID+"'\n");
		}
		
		sql.append(" ORDER BY PC.PICK_ID ASC\n");
		PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), params, getFunName(), pageSize, curPage);
		return ps;
	}
	
	/**
	 * 提货单打印
	 * @param pickIds
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getPurPickMain(String pickIds) throws Exception{
		StringBuffer sql = new StringBuffer();
        sql.append("SELECT distinct POP.ORDER_ID,\n" );
        sql.append("                POP.ORDER_CODE,\n" );
        sql.append("                POP.VENDER_NAME,\n" );
        sql.append("                POP.VENDER_CODE,\n" );
        sql.append("                TO_CHAR(SYSDATE,'YYYY-MM-DD') PRINTDATE,\n" );
        sql.append("                TMD.DEALER_ID   AS CMST_ID,\n" );
        sql.append("                TMD.DEALER_CODE AS CMST_CODE,\n" );
        sql.append("                TMD.DEALER_NAME AS CMST_NAME\n" );
        sql.append("\n" );
        sql.append("  FROM TT_PART_OEM_PO      POP,\n" );
        sql.append("       TT_PART_DEFINE      TPD,\n" );
        sql.append("       TT_PART_CMST        CMST,\n" );
        sql.append("       TM_DEALER           TMD,\n" );
        sql.append("       TT_PART_OEM_PO_PICK PCK\n");
        sql.append(" WHERE POP.PART_ID = TPD.PART_ID\n" );
        sql.append("   AND POP.PART_ID = CMST.PART_ID(+)\n" );
        sql.append("   AND CMST.CMST_ID = TMD.DEALER_ID(+)\n" );
        sql.append("   AND POP.PO_ID = PCK.PO_ID\n");
        sql.append("   AND PCK.PICK_ID IN ("+pickIds+") ");
        sql.append(" order by  POP.VENDER_CODE \n");
       
        return this.pageQuery(sql.toString(), null, this.getFunName());
	}
	/**
	 * 提货单打印
	 * @param pickIds
	 * @param oRDER_ID
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getPurPickDtl(String pickIds, String orderId)throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT\n");
		sql.append("  POP.PO_ID,\n");
		sql.append("  POP.ORDER_ID,\n");
		sql.append("  POP.ORDER_CODE,\n");
		sql.append("  POP.VENDER_NAME,\n");
		sql.append("  POP.VENDER_CODE,\n");
		sql.append("  POP.PART_ID,\n");
		sql.append("  POP.PART_OLDCODE,\n");
		sql.append("  POP.PART_CNAME,\n");
		sql.append("  POP.PART_CODE,\n");
		sql.append("  POP.UNIT,\n");
		sql.append("  POP.CHECK_QTY,\n");
		sql.append("  POP.IN_QTY,\n");
		sql.append("  TMD.DEALER_ID AS CMST_ID,\n");
		sql.append("  TMD.DEALER_CODE AS CMST_CODE,\n");
		sql.append("  TMD.DEALER_NAME AS CMST_NAME,\n");
		sql.append("  SUM(PCK.PICK_QTY) AS PICK_QTY\n");
        sql.append("FROM\n");
        sql.append("  TT_PART_OEM_PO POP,\n");
        sql.append("  TT_PART_DEFINE TPD,\n");
        sql.append("  TT_PART_CMST CMST,\n");
        sql.append("  TM_DEALER TMD,\n");
        sql.append("  TT_PART_OEM_PO_PICK PCK\n");
        sql.append(" WHERE\n");
        sql.append("   POP.PART_ID = TPD.PART_ID\n");
        sql.append("   AND POP.PART_ID = CMST.PART_ID(+)\n");
        sql.append("   AND CMST.CMST_ID = TMD.DEALER_ID(+)\n");
        sql.append("   AND POP.PO_ID = PCK.PO_ID\n");
        sql.append("   AND PCK.PICK_ID IN ("+pickIds+")\n");
        sql.append("   and pop.ORDER_ID ="+orderId+"\n");
        sql.append("GROUP BY\n");
        sql.append("  POP.PO_ID,\n");
        sql.append("  POP.ORDER_ID,\n");
        sql.append("  POP.ORDER_CODE,\n");
        sql.append("  POP.VENDER_NAME,\n");
        sql.append("  POP.VENDER_CODE,\n");
        sql.append("  POP.PART_ID,\n");
        sql.append("  POP.PART_OLDCODE,\n");
        sql.append("  POP.PART_CNAME,\n");
        sql.append("  POP.PART_CODE,\n");
        sql.append("  POP.UNIT,\n");
        sql.append("  POP.CHECK_QTY,\n");
        sql.append("  POP.IN_QTY,\n");
        sql.append("  TMD.DEALER_ID,\n");
        sql.append("  TMD.DEALER_CODE,\n");
        sql.append("  TMD.DEALER_NAME\n");
        sql.append("ORDER BY\n");
        sql.append("  POP.PART_OLDCODE\n");

       
        return this.pageQuery(sql.toString(), null, this.getFunName());
	}
	/**
	 * 查询采购订单
	 * @param request
	 * @param loginUser
	 * @param curPage
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public PageResult<Map<String, Object>> getPurOrderFactorySelectInfo(RequestWrapper request, AclUserBean loginUser, Integer curPage, Integer pageSize) throws Exception{
		StringBuffer sql = new StringBuffer();
		List<Object> params = new ArrayList<Object>();
		
		String ORDER_CODE = request.getParamValue("ORDER_CODE");
		String VENDER_NAME = request.getParamValue("VENDER_NAME");
		String sCreateDate = request.getParamValue("sCreateDate");
		String eCreateDate = request.getParamValue("eCreateDate");
		
		String PART_OLDCODE = request.getParamValue("PART_OLDCODE");
		String PART_CNAME = request.getParamValue("PART_CNAME");
		String PART_CODE = request.getParamValue("PART_CODE");
		String IN_WH_STATE = request.getParamValue("IN_WH_STATE");
		String SUPERIOR_PURCHASING_SELECT = request.getParamValue("SUPERIOR_PURCHASING_SELECT");
		
		sql.append("SELECT\n");
		sql.append("  POP.PO_ID,\n");
		sql.append("  POP.ORDER_ID,\n");
		sql.append("  POP.ORDER_CODE,\n");
		sql.append("  POP.PLAN_ID,\n");
		sql.append("  POP.PLINE_ID,\n");
		sql.append("  POP.PLAN_CODE,\n");
		sql.append("  POP.PLANER_ID,\n");
		sql.append("  POP.PLANER,\n");
		sql.append("  POP.BUYER_ID,\n");
		sql.append("  POP.BUYER,\n");
		sql.append("  POP.PART_ID,\n");
		sql.append("  POP.PART_OLDCODE,\n");
		sql.append("  POP.PART_CNAME,\n");
		sql.append("  POP.PART_CODE,\n");
		sql.append("  POP.PART_TYPE,--01，02，03\n");
		sql.append("  F_GET_TCCODE_DESC(POP.PART_TYPE) AS PART_TYPE_CN,\n");
		sql.append("  POP.PRODUCE_FAC,--采购方式\n");
		sql.append("  F_GET_TCCODE_DESC(POP.PRODUCE_STATE) AS PRODUCE_STATE_CN,\n");
		sql.append("  POP.UNIT,\n");
		sql.append("  POP.VENDER_ID,\n");
		sql.append("  POP.VENDER_CODE,\n");
		sql.append("  POP.VENDER_NAME,\n");
		sql.append("  POP.PLAN_QTY,--计划数量\n");
		sql.append("  POP.BUY_QTY,--采购数量\n");
		sql.append("  POP.BUY_PRICE,--采购单价\n");
		sql.append("  POP.BUY_AMOUNT,--采购金额\n");
		sql.append("  (POP.CHECK_QTY-NVL(POP.IN_QTY,0)) AS DIN_QTY,--待验收数量\n");
		sql.append("  POP.IN_QTY,--已验收数量\n");
		sql.append("  TO_CHAR(POP.FORECAST_DATE,'YYYY-MM-DD')  FORECAST_DATE,--预计到货日期\n");
		sql.append("  POP.WH_ID,\n");
		sql.append("  POP.WH_NAME,\n");
		sql.append("  POP.REMARK,\n");
		sql.append("  TO_CHAR(POP.CREATE_DATE,'YYYY-MM-DD') AS CREATE_DATE,\n");
		sql.append("  POP.ORG_ID,\n");
		sql.append("  POP.STATE,\n");
		sql.append("  F_GET_TCCODE_DESC(POP.STATE) AS STATE_CN,\n");
		sql.append("  (SELECT T.LOC_ID FROM TT_PART_LOACTION_DEFINE T WHERE T.PART_ID = POP.PART_ID AND T.ORG_ID = POP.ORG_ID AND T.WH_ID = POP.WH_ID) AS LOC_ID,\n");
		sql.append("  (SELECT T.LOC_CODE FROM TT_PART_LOACTION_DEFINE T WHERE T.PART_ID = POP.PART_ID AND T.ORG_ID = POP.ORG_ID AND T.WH_ID = POP.WH_ID) AS LOC_CODE,\n");
		sql.append("  POP.SUPERIOR_PURCHASING,\n");
		sql.append("  (SELECT SM.REMARK FROM TT_PART_PLAN_SCROLL SM WHERE SM.ID = POP.PLAN_ID) AS PLAN_NAME,\n");
		sql.append("  (SELECT SD.SALES_REMARK FROM TT_PART_PLAN_SCROLL_DEL SD WHERE SD.ID = POP.PLINE_ID) AS SALES_REMARK\n");
		sql.append("FROM\n");
		sql.append("  TT_PART_OEM_PO POP\n");
		sql.append("WHERE\n");
		sql.append("  POP.ORG_ID = "+Constant.OEM_ACTIVITIES+"\n");
		sql.append("  AND POP.SUPERIOR_PURCHASING = '"+CommonUtils.checkNull(SUPERIOR_PURCHASING_SELECT)+"'\n");
		if(StringUtil.notNull(ORDER_CODE)){
			sql.append("  AND POP.ORDER_CODE LIKE ?\n");
			params.add("%"+ORDER_CODE.toUpperCase()+"%");
		}
		if(StringUtil.notNull(VENDER_NAME)){
			sql.append("  AND POP.VENDER_NAME LIKE ?\n");
			params.add("%"+VENDER_NAME+"%");
		}
		
		if(StringUtil.notNull(sCreateDate)){
			sql.append("  AND TO_CHAR(POP.CREATE_DATE,'YYYY-MM-DD') >= ?\n");
			params.add(sCreateDate);
		}
		if(StringUtil.notNull(eCreateDate)){
			sql.append("  AND TO_CHAR(POP.CREATE_DATE,'YYYY-MM-DD') <= ?\n");
			params.add(eCreateDate);
		}
		if(StringUtil.notNull(PART_OLDCODE)){
			sql.append("  AND POP.PART_OLDCODE LIKE ?\n");
			params.add("%"+PART_OLDCODE.toUpperCase()+"%");
		}
		if(StringUtil.notNull(PART_CNAME)){
			sql.append("  AND POP.PART_CNAME LIKE ?\n");
			params.add("%"+PART_CNAME+"%");
		}
		if(StringUtil.notNull(PART_CODE)){
			sql.append("  AND POP.PART_CODE LIKE ?\n");
			params.add("%"+PART_CODE.toUpperCase()+"%");
		}
		if(StringUtil.notNull(IN_WH_STATE)){
			if("1".equals(IN_WH_STATE)){
				sql.append("  AND NVL(POP.IN_QTY,0) > 0 \n");
			}
			if("0".equals(IN_WH_STATE)){
				sql.append("  AND NVL(POP.IN_QTY,0) = 0 \n");
			}
		}
		
		sql.append(" ORDER BY POP.ORDER_CODE ASC,POP.CREATE_DATE ASC\n");

		PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), params, getFunName(), pageSize, curPage);
		return ps;
	}
	/**
	 * 上级采购单位
	 * @param loginUser
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getSupSj(AclUserBean loginUser) throws Exception{
		StringBuffer sql = new StringBuffer();
		List<Object> params = new ArrayList<Object>();
		sql.append("SELECT\n");
		sql.append("  DISTINCT\n");
		sql.append("  T.*\n");
		sql.append("FROM(\n");
		sql.append("  SELECT\n");
		sql.append("    DECODE(PF.FIX_VALUE, '10', '97141001', '11', '97141002', '13', '97141004', '') AS PURCHASE_TYPE,\n");
		sql.append("    F_GET_TCCODE_DESC(DECODE(PF.FIX_VALUE, '10', '97141001', '11', '97141002', '13', '97141004', '')) AS PURCHASE_TYPE_CN\n");
		sql.append("  FROM\n");
		sql.append("    TC_USER U,\n");
		sql.append("    TT_PART_USERPOSE_DEFINE PU,\n");
		sql.append("    TT_PART_FIXCODE_DEFINE PF\n");
		sql.append("  WHERE\n");
		sql.append("    PU.USER_ID = U .USER_ID\n");
		sql.append("    AND PF.FIX_VALUE = PU.USER_TYPE\n");
		sql.append("    AND U .USER_STATUS = '10011001'\n");
		sql.append("    AND PF.FIX_VALUE IN (10, 11, 13)\n");
		sql.append("    AND PU.STATE = 10011001\n");
		sql.append("    AND PF.FIX_GOUPTYPE = 92251001\n");
		sql.append("    AND PU.USER_ID = '"+loginUser.getUserId()+"'\n");
		sql.append(")T\n");
		sql.append("ORDER BY T.PURCHASE_TYPE ASC\n");
		
		List<Map<String, Object>> list = pageQuery(sql.toString(), params, getFunName());
		return list;
	}
	/**
	 * 查询采购可关闭订单信息
	 * @param request
	 * @param loginUser
	 * @param curPage
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public PageResult<Map<String, Object>> getPurOrderCloseInfo(RequestWrapper request, AclUserBean loginUser, Integer curPage, Integer pageSize) throws Exception{
		StringBuffer sql = new StringBuffer();
		List<Object> params = new ArrayList<Object>();
		
		String ORDER_CODE = request.getParamValue("ORDER_CODE");
		String VENDER_NAME = request.getParamValue("VENDER_NAME");
		String sCreateDate = request.getParamValue("sCreateDate");
		String eCreateDate = request.getParamValue("eCreateDate");
		
		String PART_OLDCODE = request.getParamValue("PART_OLDCODE");
		String PART_CNAME = request.getParamValue("PART_CNAME");
		String PART_CODE = request.getParamValue("PART_CODE");
		
		String STATE = request.getParamValue("STATE");
		

		sql.append(" SELECT distinct a.order_code, --采购单号\n");
		sql.append("        c.plan_no PLAN_CODE, --计划号\n");
		sql.append("		b.POLINE_ID, --采购单明细id\n");
		sql.append("        e.part_oldcode, --配件编码\n");
		sql.append("        e.part_cname, --配件名称\n");
		sql.append("        e.part_code, --配件件号\n");
		sql.append("        e.produce_state   PART_TYPE,");
		sql.append("        e.unit, --单位\n");
		sql.append("        a.state, --状态\n");
		sql.append("        a.REMARK, --备注\n");
		sql.append("        b.buy_qty, --采购数量\n");
		sql.append(  " 		b.CLOSE_REMARK,\n");
		sql.append("     	 b.OPEN_REMARK,\n");
		sql.append("        nvl(f.check_qty, 0) check_qty,--验收数量\n");
		sql.append("        d.vender_code, --供应商编码\n");
		sql.append("        d.vender_name, --供应商名称\n");
		sql.append("        a.create_date,\n");
		sql.append("        F_GET_TCUSER_NAME(e.planer_id) planer, --计划员\n");
		sql.append("        F_GET_TCUSER_NAME(e.buyer_id) buyer--采购员\n");

		sql.append("  FROM tt_part_po_main       a,\n" );
		sql.append("       tt_part_po_dtl        b,\n" );
		sql.append("       TT_PART_PLAN_SCROLL   c,\n" );
		sql.append("       tt_part_vender_define d,\n" );
		sql.append("       tt_part_define        e,\n" );
		sql.append("       tt_part_oem_po        f\n" );
		sql.append(" WHERE a.order_id = b.order_id\n" );
		sql.append("   and a.plan_id = c.id\n" );
		sql.append("   and b.vender_id = d.vender_id\n" );
		sql.append("   and b.part_id = e.part_id\n" );
		sql.append("   and f.order_id(+) = a.order_id\n" );
		if(StringUtil.notNull(ORDER_CODE)){
			sql.append("  AND a.ORDER_CODE LIKE ?\n");
			params.add("%"+ORDER_CODE.toUpperCase()+"%");
		}
		if(StringUtil.notNull(VENDER_NAME)){
			sql.append("  AND d.VENDER_NAME LIKE ?\n");
			params.add("%"+VENDER_NAME+"%");
		}
		
		if(StringUtil.notNull(sCreateDate)){
			sql.append("  AND TO_CHAR(a.CREATE_DATE,'YYYY-MM-DD') >= ?\n");
			params.add(sCreateDate);
		}
		if(StringUtil.notNull(eCreateDate)){
			sql.append("  AND TO_CHAR(a.CREATE_DATE,'YYYY-MM-DD') <= ?\n");
			params.add(eCreateDate);
		}
		if(StringUtil.notNull(PART_OLDCODE)){
			sql.append("  AND e.PART_OLDCODE LIKE ?\n");
			params.add("%"+PART_OLDCODE.toUpperCase()+"%");
		}
		if(StringUtil.notNull(PART_CNAME)){
			sql.append("  AND e.PART_CNAME LIKE ?\n");
			params.add("%"+PART_CNAME+"%");
		}
		if(StringUtil.notNull(PART_CODE)){
			sql.append("  AND e.PART_CODE LIKE ?\n");
			params.add("%"+PART_CODE.toUpperCase()+"%");
		}
		
		if(StringUtil.notNull(STATE)){
			sql.append("  AND b.STATE = ?\n");
			params.add(STATE);
		}
		
		sql.append(" ORDER BY a.ORDER_CODE ASC,a.CREATE_DATE ASC\n");

		PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), params, getFunName(), pageSize, curPage);
		return ps;
	}
	/**
	 * 查询所有的计划，制造物流处已确认，计划室已确认，滚动月度计划
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getCreatePurchaseAll(RequestWrapper request) throws Exception{
		StringBuffer sql = new StringBuffer();
		List<Object> params = new ArrayList<Object>();
		
		String PLAN_YEAR_SELECT = request.getParamValue("PLAN_YEAR_SELECT");
		String PLAN_MONTH_SELECT = request.getParamValue("PLAN_MONTH_SELECT");
		String yearMonth = CommonUtils.checkNull(PLAN_YEAR_SELECT)+CommonUtils.checkNull(PLAN_MONTH_SELECT);
		
		sql.append("SELECT\n");
		sql.append("  T1.ID AS PLAN_ID,\n");
		sql.append("  T1.PLAN_NO,\n");
		sql.append("  T1.MONTH_DATE,\n");
		sql.append("  T2.ID AS PLAN_LINE_ID,\n");
		sql.append("  T2.PLAN_MONTH_ONE,\n");
		sql.append("  T2.LAST_CONFIRM_STATE,\n");
		sql.append("  T2.LAST_CONFIRM_NUM,\n");
		sql.append("  TPD.PART_ID,\n");
		sql.append("  T2.STATUS,\n");
		sql.append("  T2.ORDER_PERIOD\n");
		sql.append("FROM\n");
		sql.append("  TT_PART_PLAN_SCROLL T1,\n");
		sql.append("  TT_PART_PLAN_SCROLL_DEL T2,\n");
		sql.append("  TT_PART_DEFINE TPD\n");
		sql.append("WHERE\n");
		sql.append("  T1.ID = T2.PLAN_ID\n");
		sql.append("  AND T2.PART_ID = TPD.PART_ID\n");
		sql.append("  AND NOT EXISTS(SELECT 1 FROM TT_PART_SPLIT_DEFINE SP WHERE SP.PART_ID = TPD.PART_ID)--不显示总成件，只显示拆分的分总成件\n");
		sql.append("  AND T1.IS_SUBMIT = 10041001\n");//已提交
		sql.append("  AND T2.FIRST_CONFIRM_STATE = 10041001\n");//供应商已确认
		sql.append("  AND T2.LAST_CONFIRM_STATE = 10041001\n");//计划室已确认
		sql.append("  AND (T2.STATUS IS NULL ')\n");//计划没下发OR T2.STATUS <> '"+Constant.SCROLL_PLAN_STATUS_02+"
		sql.append("  AND T1.MONTH_DATE = '"+yearMonth+"'\n");//计划月度
		sql.append("  AND T2.ORDER_PERIOD = '"+Constant.PRODUCT_ORDER_TYPE_STATUS_02+"'\n");//月度订单

		List<Map<String, Object>> list = pageQuery(sql.toString(), params, getFunName());
		return list;
	}
	
	/**
	 * 查询周度计划批量数据
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getPlanWeekUpdateTemplateInfo(RequestWrapper request,String planLineId) throws Exception{
		StringBuffer sql = new StringBuffer();
		List<Object> params = new ArrayList<Object>();
		
		String PLAN_YEAR_SELECT = request.getParamValue("PLAN_YEAR_SELECT");
		String PLAN_MONTH_SELECT = request.getParamValue("PLAN_MONTH_SELECT");
		String yearMonth = CommonUtils.checkNull(PLAN_YEAR_SELECT)+CommonUtils.checkNull(PLAN_MONTH_SELECT);
		
		sql.append("SELECT\n");
		sql.append("  T1.ID AS PLAN_ID,\n");
		sql.append("  T1.PLAN_NO,\n");
		sql.append("  T1.MONTH_DATE,\n");
		sql.append("  T1.IS_SUBMIT,\n");
		sql.append("  TO_CHAR(T1.SUBMIT_DATE,'YYYY-MM-DD') AS SUBMIT_DATE,\n");
		sql.append("  T2.ID AS PLAN_LINE_ID,\n");
		sql.append("  T2.PLAN_MONTH_ONE,\n");
		sql.append("  T2.PLAN_MONTH_TOW,\n");
		sql.append("  T2.PLAN_MONTH_THREE,\n");
		sql.append("  T2.WEEK_ONE,\n");
		sql.append("  T2.WEEK_TOW,\n");
		sql.append("  T2.WEEK_THREE,\n");
		sql.append("  T2.WEEK_FOUR,\n");
		sql.append("  NVL(T2.WEEK_FIVE,0) AS WEEK_FIVE,\n");
		sql.append("  T2.FIRST_CONFIRM_STATE,\n");
		sql.append("  TO_CHAR(T2.FIRST_CONFIRM_DATE,'YYYY-MM-DD') AS FIRST_CONFIRM_DATE,\n");
		sql.append("  T2.FIRSR_CONFIRM_BY,\n");
		sql.append("  T2.FIRST_CONFIRM_REMARK,\n");
		sql.append("  T2.FIRST_CONFIRM_NUM,\n");
		sql.append("  T2.LAST_CONFIRM_STATE,\n");
		sql.append("  TO_CHAR(T2.LAST_CONFIRM_DATE,'YYYY-MM-DD') AS LAST_CONFIRM_DATE,\n");
		sql.append("  T2.LAST_CONFIRM_BY,\n");
		sql.append("  T2.LAST_CONFIRM_REMARK,\n");
		sql.append("  T2.LAST_CONFIRM_NUM,\n");
		sql.append("  TPD.PART_ID,\n");
		sql.append("  TPD.PART_OLDCODE,\n");
		sql.append("  TPD.PART_CNAME,\n");
		sql.append("  TPD.PART_CODE,\n");
		sql.append("  TPD.PART_TYPE,\n");
		sql.append("  TPD.PRODUCE_STATE,\n");
		sql.append("  TPD.BUY_MIN_PKG,\n");
		sql.append("  TPD.LC,\n");
		sql.append("  TPD.PRODUCE_FAC,\n");
		sql.append("  TPD.SUPERIOR_PURCHASING,\n");
		sql.append("  TU1.NAME AS PLANER_ID_CN,\n");
		sql.append("  TU2.NAME AS BUYER_ID_CN,\n");
		sql.append("  T2.STATUS,\n");
		sql.append("  F_GET_TCCODE_DESC(T2.IS_WEEK1) AS IS_WEEK1,\n");
		sql.append("  F_GET_TCCODE_DESC(T2.IS_WEEK2) AS IS_WEEK2,\n");
		sql.append("  F_GET_TCCODE_DESC(T2.IS_WEEK3) AS IS_WEEK3,\n");
		sql.append("  F_GET_TCCODE_DESC(T2.IS_WEEK4) AS IS_WEEK4,\n");
		sql.append("  F_GET_TCCODE_DESC(T2.IS_WEEK5) AS IS_WEEK5,\n");
		sql.append("  T2.IS_WEEK1 AS W1,\n");
		sql.append("  T2.IS_WEEK2 AS W2,\n");
		sql.append("  T2.IS_WEEK3 AS W3,\n");
		sql.append("  T2.IS_WEEK4 AS W4,\n");
		sql.append("  T2.IS_WEEK5 AS W5,\n");
		sql.append("  T2.ORDER_PERIOD,\n");
		sql.append("  F_GET_TCCODE_DESC(T2.ORDER_PERIOD) AS ORDER_PERIOD_CN,\n");
		sql.append("  PKG_PART_UTIL.F_GET_MONDAY_COUNT(T1.MONTH_DATE) AS WEEK_COUNT,--计划月份共有几个周一，共几周\n");
		sql.append("  TO_CHAR(NEXT_DAY(SYSDATE+2, '星期一') ,'YYYYMM') AS NEXT_WEEK_ONE_MONTH,--下个周一所在的月份\n");
		sql.append("  TO_CHAR(NEXT_DAY(SYSDATE+2, '星期一') ,'YYYY-MM-DD') AS NEXT_WEEK_ONE_DATE,--下个周一所在的时间\n");
		sql.append("  PKG_PART_UTIL.F_GET_WEEK_ONE_XH(TO_CHAR(NEXT_DAY(SYSDATE+2, '星期一') ,'YYYY-MM-DD')) AS NEXT_WEEK_ONE_NUM,--下个周一是所在月的第几周\n");
		sql.append("  T1.REMARK AS PLAN_NAME,\n");
		sql.append("  T2.SALES_REMARK\n");
		sql.append("FROM\n");
		sql.append("  TT_PART_PLAN_SCROLL T1,\n");
		sql.append("  TT_PART_PLAN_SCROLL_DEL T2,\n");
		sql.append("  TT_PART_DEFINE TPD,\n");
		sql.append("  TC_USER TU1,\n");
		sql.append("  TC_USER TU2\n");
		sql.append("WHERE\n");
		sql.append("  T1.ID = T2.PLAN_ID\n");
		sql.append("  AND T2.PART_ID = TPD.PART_ID\n");
		sql.append("  AND T1.IS_SUBMIT = 10041001\n");
		sql.append("  AND NOT EXISTS(SELECT 1 FROM TT_PART_SPLIT_DEFINE SP WHERE SP.PART_ID = TPD.PART_ID)--不显示总成件，只显示拆分的分总成件\n");
		sql.append("  AND (T2.STATUS IS NULL ')\n");//计划未下发 OR T2.STATUS <> '"+Constant.SCROLL_PLAN_STATUS_02+"
		sql.append("  AND TPD.PLANER_ID = TU1.USER_ID(+)\n");
		sql.append("  AND TPD.BUYER_ID = TU2.USER_ID(+)\n");
		sql.append("  AND T2.FIRST_CONFIRM_STATE = 10041001\n");
		sql.append("  AND T2.LAST_CONFIRM_STATE = 10041001\n");
		sql.append("  AND T2.ORDER_PERIOD = '"+Constant.PRODUCT_ORDER_TYPE_STATUS_01+"'\n");//周度
		if(StringUtil.notNull(planLineId)){
			sql.append("  AND T2.ID = ?\n");
			params.add(planLineId);
		}else{
			sql.append("  AND T1.MONTH_DATE = '"+yearMonth+"'\n");
		}
		sql.append("ORDER BY\n");
		sql.append("  T1.PLAN_NO ASC,\n");
		sql.append("  TPD.PART_OLDCODE ASC\n");

		List<Map<String, Object>> list = pageQuery(sql.toString(), params, getFunName());
		return list;
	}
	
	/**
	 * 查询采购订单
	 * @param request
	 * @param loginUser
	 * @param curPage
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public PageResult<Map<String, Object>> getPurOrderFactoryPrintInfo(RequestWrapper request, AclUserBean loginUser, Integer curPage, Integer pageSize) throws Exception{
		StringBuffer sql = new StringBuffer();
		List<Object> params = new ArrayList<Object>();
		
		String ORDER_CODE = request.getParamValue("ORDER_CODE");
		String VENDER_NAME = request.getParamValue("VENDER_NAME");
		String sCreateDate = request.getParamValue("sCreateDate");
		String eCreateDate = request.getParamValue("eCreateDate");
		
		String PART_OLDCODE = request.getParamValue("PART_OLDCODE");
		String PART_CNAME = request.getParamValue("PART_CNAME");
		String PART_CODE = request.getParamValue("PART_CODE");
		String IN_WH_STATE = request.getParamValue("IN_WH_STATE");
		String SUPERIOR_PURCHASING_SELECT = request.getParamValue("SUPERIOR_PURCHASING_SELECT");
		
		String PRINT_STATE_S = request.getParamValue("PRINT_STATE_S");
		
		sql.append("SELECT\n");
		sql.append("  POP.PO_ID,\n");
		sql.append("  POP.ORDER_ID,\n");
		sql.append("  POP.ORDER_CODE,\n");
		sql.append("  POP.PLAN_ID,\n");
		sql.append("  POP.PLINE_ID,\n");
		sql.append("  POP.PLAN_CODE,\n");
		sql.append("  POP.PLANER_ID,\n");
		sql.append("  POP.PLANER,\n");
		sql.append("  POP.BUYER_ID,\n");
		sql.append("  POP.BUYER,\n");
		sql.append("  POP.PART_ID,\n");
		sql.append("  POP.PART_OLDCODE,\n");
		sql.append("  POP.PART_CNAME,\n");
		sql.append("  POP.PART_CODE,\n");
		sql.append("  POP.PART_TYPE,--01，02，03\n");
		sql.append("  POP.PRODUCE_FAC,--采购方式\n");
		sql.append("  POP.UNIT,\n");
		sql.append("  POP.VENDER_ID,\n");
		sql.append("  POP.VENDER_CODE,\n");
		sql.append("  POP.VENDER_NAME,\n");
		sql.append("  POP.PLAN_QTY,--计划数量\n");
		sql.append("  POP.BUY_QTY,--采购数量\n");
		sql.append("  POP.BUY_PRICE,--采购单价\n");
		sql.append("  POP.BUY_AMOUNT,--采购金额\n");
		sql.append("  (POP.CHECK_QTY-NVL(POP.IN_QTY,0)) AS DIN_QTY,--待验收数量\n");
		sql.append("  POP.IN_QTY,--已验收数量\n");
		sql.append("  TO_CHAR(POP.FORECAST_DATE,'YYYY-MM-DD')  FORECAST_DATE,--预计到货日期\n");
		sql.append("  POP.WH_ID,\n");
		sql.append("  POP.WH_NAME,\n");
		sql.append("  POP.REMARK,\n");
		sql.append("  TO_CHAR(POP.CREATE_DATE,'YYYY-MM-DD') AS CREATE_DATE,\n");
		sql.append("  POP.ORG_ID,\n");
		sql.append("  POP.STATE,\n");
		sql.append("  (SELECT T.LOC_ID FROM TT_PART_LOACTION_DEFINE T WHERE T.PART_ID = POP.PART_ID AND T.ORG_ID = POP.ORG_ID AND T.WH_ID = POP.WH_ID) AS LOC_ID,\n");
		sql.append("  (SELECT T.LOC_CODE FROM TT_PART_LOACTION_DEFINE T WHERE T.PART_ID = POP.PART_ID AND T.ORG_ID = POP.ORG_ID AND T.WH_ID = POP.WH_ID) AS LOC_CODE,\n");
		sql.append("  POP.SUPERIOR_PURCHASING,\n");
		sql.append("  (SELECT SM.REMARK FROM TT_PART_PLAN_SCROLL SM WHERE SM.ID = POP.PLAN_ID) AS PLAN_NAME,\n");
		sql.append("  (SELECT SD.SALES_REMARK FROM TT_PART_PLAN_SCROLL_DEL SD WHERE SD.ID = POP.PLINE_ID) AS SALES_REMARK,\n");
		sql.append("  (SELECT count(1) FROM tt_part_oem_po_send_print_log PS WHERE PS.po_ID = pop.po_ID) AS print_times,\n");
		sql.append("  (SELECT nvl(sum(print_qty),0) FROM tt_part_oem_po_send_print_log PS WHERE PS.po_ID = pop.po_ID) AS print_qtys,\n");
		sql.append("  (SELECT PS.PLAN_TYPES FROM TT_PART_PLAN_SCROLL PS WHERE PS.ID = POP.PLAN_ID) AS PLAN_TYPE,\n");
		sql.append("  TMD.DEALER_NAME AS CMST_NAME\n");
		sql.append("FROM\n");
		sql.append("  TT_PART_OEM_PO POP,\n");
		sql.append("  TT_PART_CMST CMST,\n");
		sql.append("  TM_DEALER TMD\n");
		sql.append("WHERE\n");
		sql.append("  POP.ORG_ID = "+Constant.OEM_ACTIVITIES+"\n");
		sql.append("  AND POP.SUPERIOR_PURCHASING = '"+CommonUtils.checkNull(SUPERIOR_PURCHASING_SELECT)+"'\n");
		sql.append("  AND POP.PART_ID = CMST.PART_ID(+)\n");
		sql.append("  AND CMST.CMST_ID = TMD.DEALER_ID(+)\n");
		if(StringUtil.notNull(ORDER_CODE)){
			sql.append("  AND POP.ORDER_CODE LIKE ?\n");
			params.add("%"+ORDER_CODE.toUpperCase()+"%");
		}
		if(StringUtil.notNull(VENDER_NAME)){
			sql.append("  AND POP.VENDER_NAME LIKE ?\n");
			params.add("%"+VENDER_NAME+"%");
		}
		
		if(StringUtil.notNull(sCreateDate)){
			sql.append("  AND TO_CHAR(POP.CREATE_DATE,'YYYY-MM-DD') >= ?\n");
			params.add(sCreateDate);
		}
		if(StringUtil.notNull(eCreateDate)){
			sql.append("  AND TO_CHAR(POP.CREATE_DATE,'YYYY-MM-DD') <= ?\n");
			params.add(eCreateDate);
		}
		if(StringUtil.notNull(PART_OLDCODE)){
			sql.append("  AND POP.PART_OLDCODE LIKE ?\n");
			params.add("%"+PART_OLDCODE.toUpperCase()+"%");
		}
		if(StringUtil.notNull(PART_CNAME)){
			sql.append("  AND POP.PART_CNAME LIKE ?\n");
			params.add("%"+PART_CNAME+"%");
		}
		if(StringUtil.notNull(PART_CODE)){
			sql.append("  AND POP.PART_CODE LIKE ?\n");
			params.add("%"+PART_CODE.toUpperCase()+"%");
		}
		if(StringUtil.notNull(IN_WH_STATE)){
			if("1".equals(IN_WH_STATE)){
				//未入库
				sql.append("  AND (NVL(POP.CHECK_QTY,0)-NVL(POP.IN_QTY,0)) > 0 \n");
			}
			if("2".equals(IN_WH_STATE)){
				//部分入库
				sql.append("  AND (NVL(POP.CHECK_QTY,0)-NVL(POP.IN_QTY,0)) > 0 \n");
				sql.append("  AND NVL(POP.IN_QTY,0) > 0 \n");
			}
			if("3".equals(IN_WH_STATE)){
				sql.append("  AND (NVL(POP.CHECK_QTY,0)-NVL(POP.IN_QTY,0)) = 0 \n");
			}
		}
		
		if(StringUtil.notNull(PRINT_STATE_S)){
			if(Constant.IF_TYPE_NO.toString().equals(PRINT_STATE_S)){
				sql.append("  and (SELECT count(1) FROM tt_part_oem_po_send_print_log PS WHERE PS.po_ID = pop.po_ID) = 0\n");
			}
			if(Constant.IF_TYPE_YES.toString().equals(PRINT_STATE_S)){
				sql.append("  and (SELECT count(1) FROM tt_part_oem_po_send_print_log PS WHERE PS.po_ID = pop.po_ID) > 0\n");
			}
		}
		
		sql.append(" ORDER BY POP.ORDER_CODE ASC,POP.CREATE_DATE ASC\n");

		PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), params, getFunName(), pageSize, curPage);
		return ps;
	}
	
	
	/**
	 * 专用车厂，总装车厂打印主数据
	 * @param poInStr
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getPurchaseOrderFactoryMain(String poInStr) {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT distinct POP.ORDER_ID,\n" );
        sql.append("                POP.ORDER_CODE,\n" );
        sql.append("                POP.VENDER_NAME,\n" );
        sql.append("                POP.VENDER_CODE,\n" );
        sql.append("                TO_CHAR(SYSDATE,'YYYY-MM-DD') PRINTDATE,\n" );
        sql.append("                TMD.DEALER_ID   AS CMST_ID,\n" );
        sql.append("                TMD.DEALER_CODE AS CMST_CODE,\n" );
        sql.append("                TMD.DEALER_NAME AS CMST_NAME\n" );
        sql.append("\n" );
        sql.append("  FROM TT_PART_OEM_PO POP,\n" );
        sql.append("       TT_PART_DEFINE TPD,\n" );
        sql.append("       TT_PART_CMST   CMST,\n" );
        sql.append("       TM_DEALER      TMD\n" );
        sql.append(" WHERE POP.PART_ID = TPD.PART_ID\n" );
        sql.append("   AND POP.PART_ID = CMST.PART_ID(+)\n" );
        sql.append("   AND CMST.CMST_ID = TMD.DEALER_ID(+)\n" );
        sql.append("   and pop.po_id in ("+poInStr+") ");
        sql.append(" order by  POP.VENDER_CODE \n");
       

        return this.pageQuery(sql.toString(), null, this.getFunName());
    }
	/**
	 * 专用车厂，总装车厂打印明细数据
	 * @param poInStr
	 * @param orderId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getPurchaseOrderFactoryDtl(String poInStr,String orderId) {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT POP.po_id,POP.ORDER_ID,\n" );
        sql.append("                POP.ORDER_CODE,\n" );
        sql.append("                POP.VENDER_NAME,\n" );
        sql.append("                POP.VENDER_CODE,\n" );
        sql.append("  				POP.PART_ID,\n");
		sql.append("  				POP.PART_OLDCODE,\n");
		sql.append("  				POP.PART_CNAME,\n");
		sql.append("  				POP.PART_CODE,\n");
		sql.append("  				POP.UNIT,\n");
		sql.append("  				POP.CHECK_QTY,\n");
		sql.append("  				POP.IN_QTY,\n");
		sql.append("  				(POP.CHECK_QTY - POP.IN_QTY ) lessQty,\n");
        sql.append("                TMD.DEALER_ID   AS CMST_ID,\n" );
        sql.append("                TMD.DEALER_CODE AS CMST_CODE,\n" );
        sql.append("                TMD.DEALER_NAME AS CMST_NAME\n" );
        sql.append("\n" );
        sql.append("  FROM TT_PART_OEM_PO POP,\n" );
        sql.append("       TT_PART_DEFINE TPD,\n" );
        sql.append("       TT_PART_CMST   CMST,\n" );
        sql.append("       TM_DEALER      TMD\n" );
        sql.append(" WHERE POP.PART_ID = TPD.PART_ID\n" );
        sql.append("   AND POP.PART_ID = CMST.PART_ID(+)\n" );
        sql.append("   AND CMST.CMST_ID = TMD.DEALER_ID(+)\n" );
        sql.append("   and pop.po_id in ("+poInStr+") ");
        sql.append("   and pop.ORDER_ID ="+orderId+" ");
        sql.append(" order by pop.PART_OLDCODE \n");
       

        return this.pageQuery(sql.toString(), null, this.getFunName());
    }
	/**
	 * 查询采购订单
	 * @param request
	 * @param loginUser
	 * @param curPage
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public PageResult<Map<String, Object>> getPurOrderVenderPrintInfo(RequestWrapper request, AclUserBean loginUser, Integer curPage, Integer pageSize) throws Exception{
		StringBuffer sql = new StringBuffer();
		List<Object> params = new ArrayList<Object>();
		
		String ORDER_CODE = request.getParamValue("ORDER_CODE");
		String VENDER_NAME = request.getParamValue("VENDER_NAME");
		String sCreateDate = request.getParamValue("sCreateDate");
		String eCreateDate = request.getParamValue("eCreateDate");
		
		String PART_OLDCODE = request.getParamValue("PART_OLDCODE");
		String PART_CNAME = request.getParamValue("PART_CNAME");
		String PART_CODE = request.getParamValue("PART_CODE");
		String IN_WH_STATE = request.getParamValue("IN_WH_STATE");
		String SUPERIOR_PURCHASING_SELECT = request.getParamValue("SUPERIOR_PURCHASING_SELECT");
		
		String PRINT_STATE_S = request.getParamValue("PRINT_STATE_S");
		
		sql.append("SELECT\n");
		sql.append("  POP.PO_ID,\n");
		sql.append("  POP.ORDER_ID,\n");
		sql.append("  POP.ORDER_CODE,\n");
		sql.append("  POP.PLAN_ID,\n");
		sql.append("  POP.PLINE_ID,\n");
		sql.append("  POP.PLAN_CODE,\n");
		sql.append("  POP.PLANER_ID,\n");
		sql.append("  POP.PLANER,\n");
		sql.append("  POP.BUYER_ID,\n");
		sql.append("  POP.BUYER,\n");
		sql.append("  POP.PART_ID,\n");
		sql.append("  POP.PART_OLDCODE,\n");
		sql.append("  POP.PART_CNAME,\n");
		sql.append("  POP.PART_CODE,\n");
		sql.append("  POP.PART_TYPE,--01，02，03\n");
		sql.append("  F_GET_TCCODE_DESC(POP.PART_TYPE) AS PART_TYPE_CN,\n");
		sql.append("  POP.PRODUCE_FAC,--采购方式\n");
		sql.append("  POP.UNIT,\n");
		sql.append("  POP.VENDER_ID,\n");
		sql.append("  POP.VENDER_CODE,\n");
		sql.append("  POP.VENDER_NAME,\n");
		sql.append("  POP.PLAN_QTY,--计划数量\n");
		sql.append("  POP.BUY_QTY,--采购数量\n");
		sql.append("  POP.BUY_PRICE,--采购单价\n");
		sql.append("  POP.BUY_AMOUNT,--采购金额\n");
		sql.append("  (POP.CHECK_QTY-NVL(POP.IN_QTY,0)) AS DIN_QTY,--待验收数量\n");
		sql.append("  POP.IN_QTY,--已验收数量\n");
		sql.append("  TO_CHAR(POP.FORECAST_DATE,'YYYY-MM-DD')  FORECAST_DATE,--预计到货日期\n");
		sql.append("  POP.WH_ID,\n");
		sql.append("  POP.WH_NAME,\n");
		sql.append("  POP.REMARK,\n");
		sql.append("  TO_CHAR(POP.CREATE_DATE,'YYYY-MM-DD') AS CREATE_DATE,\n");
		sql.append("  POP.ORG_ID,\n");
		sql.append("  POP.STATE,\n");
		sql.append("  F_GET_TCCODE_DESC(POP.STATE) AS STATE_CN,\n");
		sql.append("  (SELECT T.LOC_ID FROM TT_PART_LOACTION_DEFINE T WHERE T.PART_ID = POP.PART_ID AND T.ORG_ID = POP.ORG_ID AND T.WH_ID = POP.WH_ID) AS LOC_ID,\n");
		sql.append("  (SELECT T.LOC_CODE FROM TT_PART_LOACTION_DEFINE T WHERE T.PART_ID = POP.PART_ID AND T.ORG_ID = POP.ORG_ID AND T.WH_ID = POP.WH_ID) AS LOC_CODE,\n");
		sql.append("  POP.SUPERIOR_PURCHASING,\n");
		sql.append("  F_GET_TCCODE_DESC(POP.SUPERIOR_PURCHASING) AS SUPERIOR_PURCHASING_CN,\n");
		sql.append("  (SELECT SM.REMARK FROM TT_PART_PLAN_SCROLL SM WHERE SM.ID = POP.PLAN_ID) AS PLAN_NAME,\n");
		sql.append("  (SELECT SD.SALES_REMARK FROM TT_PART_PLAN_SCROLL_DEL SD WHERE SD.ID = POP.PLINE_ID) AS SALES_REMARK,\n");
		sql.append("  (SELECT count(1) FROM tt_part_oem_po_send_print_log PS WHERE PS.po_ID = pop.po_ID) AS print_times,\n");
		sql.append("  (SELECT nvl(sum(print_qty),0) FROM tt_part_oem_po_send_print_log PS WHERE PS.po_ID = pop.po_ID) AS print_qtys,\n");
		sql.append("  (SELECT PS.PLAN_TYPES FROM TT_PART_PLAN_SCROLL PS WHERE PS.ID = POP.PLAN_ID) AS PLAN_TYPE,\n");
		sql.append("  TMD.DEALER_NAME AS CMST_NAME\n");
		sql.append("FROM\n");
		sql.append("  TT_PART_OEM_PO POP,\n");
		sql.append("  TT_PART_CMST CMST,\n");
		sql.append("  TM_DEALER TMD\n");
		sql.append("WHERE\n");
		sql.append("  POP.ORG_ID = "+Constant.OEM_ACTIVITIES+"\n");
		sql.append("  AND POP.PART_ID = CMST.PART_ID(+)\n");
		sql.append("  AND CMST.CMST_ID = TMD.DEALER_ID(+)\n");
		sql.append("  AND POP.VENDER_ID = '"+CommonUtils.checkNull(loginUser.getVenderId())+"'\n");
		if(StringUtil.notNull(ORDER_CODE)){
			sql.append("  AND POP.ORDER_CODE LIKE ?\n");
			params.add("%"+ORDER_CODE.toUpperCase()+"%");
		}
		if(StringUtil.notNull(VENDER_NAME)){
			sql.append("  AND POP.VENDER_NAME LIKE ?\n");
			params.add("%"+VENDER_NAME+"%");
		}
		if(StringUtil.notNull(SUPERIOR_PURCHASING_SELECT)){
			sql.append("  AND POP.SUPERIOR_PURCHASING = ?\n");
			params.add(SUPERIOR_PURCHASING_SELECT);
		}
		if(StringUtil.notNull(sCreateDate)){
			sql.append("  AND TO_CHAR(POP.CREATE_DATE,'YYYY-MM-DD') >= ?\n");
			params.add(sCreateDate);
		}
		if(StringUtil.notNull(eCreateDate)){
			sql.append("  AND TO_CHAR(POP.CREATE_DATE,'YYYY-MM-DD') <= ?\n");
			params.add(eCreateDate);
		}
		if(StringUtil.notNull(PART_OLDCODE)){
			sql.append("  AND POP.PART_OLDCODE LIKE ?\n");
			params.add("%"+PART_OLDCODE.toUpperCase()+"%");
		}
		if(StringUtil.notNull(PART_CNAME)){
			sql.append("  AND POP.PART_CNAME LIKE ?\n");
			params.add("%"+PART_CNAME+"%");
		}
		if(StringUtil.notNull(PART_CODE)){
			sql.append("  AND POP.PART_CODE LIKE ?\n");
			params.add("%"+PART_CODE.toUpperCase()+"%");
		}
		if(StringUtil.notNull(IN_WH_STATE)){
			if("1".equals(IN_WH_STATE)){
				//未入库
				sql.append("  AND (NVL(POP.CHECK_QTY,0)-NVL(POP.IN_QTY,0)) > 0 \n");
			}
			if("2".equals(IN_WH_STATE)){
				//部分入库
				sql.append("  AND (NVL(POP.CHECK_QTY,0)-NVL(POP.IN_QTY,0)) > 0 \n");
				sql.append("  AND NVL(POP.IN_QTY,0) > 0 \n");
			}
			if("3".equals(IN_WH_STATE)){
				sql.append("  AND (NVL(POP.CHECK_QTY,0)-NVL(POP.IN_QTY,0)) = 0 \n");
			}
		}
		
		if(StringUtil.notNull(PRINT_STATE_S)){
			if(Constant.IF_TYPE_NO.toString().equals(PRINT_STATE_S)){
				sql.append("  and (SELECT count(1) FROM tt_part_oem_po_send_print_log PS WHERE PS.po_ID = pop.po_ID) = 0\n");
			}
			if(Constant.IF_TYPE_YES.toString().equals(PRINT_STATE_S)){
				sql.append("  and (SELECT count(1) FROM tt_part_oem_po_send_print_log PS WHERE PS.po_ID = pop.po_ID) > 0\n");
			}
		}
		
		sql.append(" ORDER BY POP.ORDER_CODE ASC,POP.CREATE_DATE ASC\n");

		PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), params, getFunName(), pageSize, curPage);
		return ps;
	}
	
	/**
	 * 查询滚动计划信息
	 * @param request
	 * @param loginUser
	 * @param curPage
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public PageResult<Map<String, Object>> getRollingPlanInfo(RequestWrapper request, AclUserBean loginUser, Integer curPage, Integer pageSize) throws Exception{
		StringBuffer sql = new StringBuffer();
		List<Object> params = new ArrayList<Object>();
		
		String PLAN_TYPES = request.getParamValue("PLAN_TYPES");
		String MYYEAR = request.getParamValue("MYYEAR");
		String MYMONTH = request.getParamValue("MYMONTH");
		String ym = MYYEAR+MYMONTH;
		String planNo = request.getParamValue("planNo");
		
		sql.append("SELECT *\n");
		sql.append("  FROM (SELECT T.PLAN_NO,\n");
		sql.append("               COUNT(*) PLAN_NUMS,\n");
		sql.append("               T.ID TID,\n");
		sql.append("               SUM(T1.PLAN_NUM) PLAN_COUNT,\n");
		sql.append("               T.CREATE_DATE,\n");
		sql.append("               T.MONTH_DATE,\n");
		sql.append("               T.IS_SUBMIT,\n");
		sql.append("               T.SUBMIT_DATE,t.status,t.PLAN_TYPES\n");
		sql.append("          FROM TT_PART_PLAN_SCROLL T, TT_PART_PLAN_SCROLL_DEL T1\n");
		sql.append("         WHERE T.ID = T1.PLAN_ID and t.status=0\n");
		sql.append("           AND T.PLAN_TYPES = '"+PLAN_TYPES+"'\n");
		sql.append("         GROUP BY T.PLAN_NO, T.CREATE_DATE, T.MONTH_DATE, T.ID,T.IS_SUBMIT,T.SUBMIT_DATE,t.status,t.PLAN_TYPES) TT\n");
		sql.append(" WHERE 1 = 1\n");
		if(StringUtil.notNull(ym)){
			sql.append("  and tt.MONTH_DATE LIKE ?\n");
			params.add("%"+ym+"%");
		}
		if(StringUtil.notNull(planNo)){
			sql.append("  AND TT.PLAN_NO LIKE ?\n");
			params.add("%"+planNo+"%");
		}
		sql.append(" order by plan_no desc\n");
		
		PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), params, getFunName(), pageSize, curPage);
		return ps;
	}
	/**
	 * 查询计划信息明细
	 * @param request
	 * @param loginUser
	 * @param curPage
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public PageResult<Map<String, Object>> getRollingPlanInfoById(RequestWrapper request, AclUserBean loginUser, Integer curPage, Integer pageSize) throws Exception{
		StringBuffer sql = new StringBuffer();
		List<Object> params = new ArrayList<Object>();
		String planId = request.getParamValue("planId");
		String PART_OLDCODE = request.getParamValue("PART_OLDCODE");
		String PART_CNAME = request.getParamValue("PART_CNAME");
		String PART_CODE = request.getParamValue("PART_CODE");
		
		sql.append("SELECT\n");
		sql.append("  SM.ID AS PLAN_ID,\n");
		sql.append("  SM.PLAN_NO,\n");
		sql.append("  SM.PLAN_TYPES,\n");
		sql.append("  SD.ID AS PLINE_ID,\n");
		sql.append("  SD.PART_ID,\n");
		sql.append("  SD.PART_OLDCODE,\n");
		sql.append("  TPD.PART_CNAME,\n");
		sql.append("  TPD.PART_CODE,\n");
		sql.append("  SD.ORDER_PERIOD,\n");
		sql.append("  SD.WH_ID,\n");
		sql.append("  (SELECT WD.WH_NAME FROM TT_PART_WAREHOUSE_DEFINE WD WHERE WD.WH_ID = SD.WH_ID) AS WH_NAME,\n");
		sql.append("  PKG_PART_UTIL.F_GET_PART_VENDER_ROWNUM1(TPD.PART_ID,1) AS VENDER_ID,\n");
		sql.append("  PKG_PART_UTIL.F_GET_PART_VENDER_ROWNUM1(TPD.PART_ID,2) AS VENDER_CODE,\n");
		sql.append("  PKG_PART_UTIL.F_GET_PART_VENDER_ROWNUM1(TPD.PART_ID,3) AS VENDER_NAME,\n");
		sql.append("  TPD.PART_TYPE,\n");
		sql.append("  TPD.PRODUCE_STATE,\n");
		sql.append("  TPD.PRODUCE_FAC,\n");
		sql.append("  TPD.SUPERIOR_PURCHASING,\n");
		sql.append("  SM.MONTH_DATE,\n");
		sql.append("  SD.PLAN_NUM,\n");
		sql.append("  TPD.BUY_MIN_PKG,\n");
		sql.append("  SD.PLAN_REMARK\n");
		sql.append("FROM\n");
		sql.append("  TT_PART_PLAN_SCROLL SM,\n");
		sql.append("  TT_PART_PLAN_SCROLL_DEL SD,\n");
		sql.append("  TT_PART_DEFINE TPD\n");
		sql.append("WHERE\n");
		sql.append("  SM.ID = SD.PLAN_ID\n");
		sql.append("  AND SD.PART_ID = TPD.PART_ID\n");
		sql.append("  AND SM.ID = '"+planId+"'\n");
		if(StringUtil.notNull(PART_OLDCODE)){
			sql.append("  AND TPD.PART_OLDCODE LIKE ?\n");
			params.add("%"+PART_OLDCODE.trim().toUpperCase()+"%");
		}
		if(StringUtil.notNull(PART_CNAME)){
			sql.append("  AND TPD.PART_CNAME LIKE ?\n");
			params.add("%"+PART_CNAME+"%");
		}
		if(StringUtil.notNull(PART_CODE)){
			sql.append("  AND TPD.PART_CODE LIKE ?\n");
			params.add("%"+PART_CODE.trim().toUpperCase()+"%");
		}
		sql.append(" ORDER BY SD.EXECL_LINE ASC,SD.PART_OLDCODE ASC\n");
		PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), params, getFunName(), pageSize, curPage);
		return ps;
	}
	
	/**
	 * 查询计划信息明细
	 * @param request
	 * @param loginUser
	 * @param curPage
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getPlanInfoById(RequestWrapper request, AclUserBean loginUser) throws Exception{
		StringBuffer sql = new StringBuffer();
		List<Object> params = new ArrayList<Object>();
		String planId = request.getParamValue("planIdTemp");
		
		sql.append("SELECT\n");
		sql.append("  SM.ID AS PLAN_ID,\n");
		sql.append("  SM.PLAN_NO,\n");
		sql.append("  SM.PLAN_TYPES,\n");
		sql.append("  SD.ID AS PLINE_ID,\n");
		sql.append("  SD.PART_ID,\n");
		sql.append("  SD.PART_OLDCODE,\n");
		sql.append("  TPD.PART_CNAME,\n");
		sql.append("  TPD.PART_CODE,\n");
		sql.append("  SD.ORDER_PERIOD,\n");
		sql.append("  SD.WH_ID,\n");
		sql.append("  (SELECT WD.WH_NAME FROM TT_PART_WAREHOUSE_DEFINE WD WHERE WD.WH_ID = SD.WH_ID) AS WH_NAME,\n");
		sql.append("  PKG_PART_UTIL.F_GET_PART_VENDER_ROWNUM1(TPD.PART_ID,1) AS VENDER_ID,\n");
		sql.append("  PKG_PART_UTIL.F_GET_PART_VENDER_ROWNUM1(TPD.PART_ID,2) AS VENDER_CODE,\n");
		sql.append("  PKG_PART_UTIL.F_GET_PART_VENDER_ROWNUM1(TPD.PART_ID,3) AS VENDER_NAME,\n");
		sql.append("  TPD.PART_TYPE,\n");
		sql.append("  TPD.PRODUCE_STATE,\n");
		sql.append("  TPD.PRODUCE_FAC,\n");
		sql.append("  TPD.SUPERIOR_PURCHASING,\n");
		sql.append("  SM.MONTH_DATE,\n");
		sql.append("  SD.PLAN_MONTH_ONE,\n");
		sql.append("  TPD.BUY_MIN_PKG,\n");
		sql.append("  SD.SALES_REMARK\n");
		sql.append("FROM\n");
		sql.append("  TT_PART_PLAN_SCROLL SM,\n");
		sql.append("  TT_PART_PLAN_SCROLL_DEL SD,\n");
		sql.append("  TT_PART_DEFINE TPD\n");
		sql.append("WHERE\n");
		sql.append("  SM.ID = SD.PLAN_ID\n");
		sql.append("  AND SD.PART_ID = TPD.PART_ID\n");
		sql.append("  AND SM.ID = '"+planId+"'\n");
		
		List<Map<String, Object>> list = pageQuery(sql.toString(), params, getFunName());
		return list;
	}
	/**
	 * 查询错误数据
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getPlanErrBy(RequestWrapper request) throws Exception{
		StringBuffer sql = new StringBuffer();
		List<Object> params = new ArrayList<Object>();
		String planId = request.getParamValue("planIdTemp");
		TtPartPlanScrollPO po = new TtPartPlanScrollPO();
		po.setId(Long.valueOf(planId));
		TtPartPlanScrollPO pos = (TtPartPlanScrollPO) dao.select(po).get(0);
		
		sql.append("SELECT\n");
		sql.append("  ROWNUM AS XH,\n");
		sql.append("  E.INFO,\n");
		sql.append("  E.PART_OLDCODE\n");
		sql.append("FROM\n");
		sql.append("  vw_part_plan_imp_ERR E\n");
		sql.append("WHERE\n");
		sql.append("  1 = 1\n");
		sql.append("AND EXISTS (\n");
		sql.append("  SELECT\n");
		sql.append("    *\n");
		sql.append("  FROM\n");
		sql.append("    tt_part_plan_scroll\n");
		sql.append("  WHERE\n");
		sql.append("    plan_no = E .plan_no\n");
		sql.append("    AND plan_type IN (93010005, 93010006)\n");
		sql.append("    AND plan_No = '" + pos.getPlanNo() + "'\n");
		sql.append(")\n");
		
		List<Map<String, Object>> list = pageQuery(sql.toString(), params, getFunName());
		return list;
	}
	
	
	/**
	 * 采购价
	 * @param planId
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String,Object>> allPriceId(String planId) throws Exception{
		
		StringBuffer sbSql = new StringBuffer();
		sbSql.append("SELECT\n" );
		sbSql.append("  DTL.ID,\n" );
		sbSql.append("  DTL.PLAN_ID,\n" );
		sbSql.append("  DTL.PART_ID,\n" );
		sbSql.append("  DTL.PART_OLDCODE,\n" );
		sbSql.append("  DTL.VENDER_ID\n" );
		sbSql.append("FROM\n" );
		sbSql.append("  TT_PART_PLAN_SCROLL_DEL DTL\n" );
		sbSql.append("WHERE\n" );
		sbSql.append("  DTL.PLAN_ID = '"+planId+"'\n");
		sbSql.append("  AND DTL.VENDER_ID = -1\n");
		List<Map<String,Object>> list = this.pageQuery(sbSql.toString(), null, getFunName());
		return list;
	}
	
}
