/**********************************************************************
* <pre>
* FILE : ServiceActivityManageSummarySearchDao.java
* CLASS : ServiceActivityManageSummarySearchDao
*
* AUTHOR : PGM
*
* FUNCTION :  服务活动管理---服务活动评估查询
*
*
*======================================================================
* CHANGE HISTORY LOG
*----------------------------------------------------------------------
* MOD. NO.| DATE     | NAME | REASON | CHANGE REQ.
*----------------------------------------------------------------------
*         |2010-06-13| PGM  | Created |
* DESCRIPTION:
* </pre>
***********************************************************************/
/**
 * $Id: ServiceActivityManageSummarySearchDao.java,v 1.1 2010/08/16 01:42:18 yuch Exp $
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
import com.infodms.dms.po.TtAsActivityEvaluatePO;
import com.infodms.dms.po.TtAsWrApplicationPO;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;
/**
 * Function       :  服务活动管理---服务活动评估查询
 * @author        :  PGM
 * CreateDate     :  2010-06-13
 * @version       :  0.1
 */
@SuppressWarnings("unchecked")
public class ServiceActivityManageSummarySearchDao extends BaseDao{
	public static Logger logger = Logger.getLogger(ServiceActivityManageSummarySearchDao.class);
	private static final ServiceActivityManageSummarySearchDao dao = new ServiceActivityManageSummarySearchDao ();
	public  static final ServiceActivityManageSummarySearchDao getInstance() {
		return dao;
	}

	/**
	 * Function         : 服务活动管理---服务活动评估查询
	 * @param           : 活动编号
	 * @param           : 活动开始日期
	 * @param           : 活动结束日期
	 * @param           : 当前页码
	 * @param           : 每页显示记录数
	 * @return          : 满足条件的服务活动管理---服务活动评估信息，包含分页信息
	 * @throws          : Exception
	 * LastUpdate       : 服务活动管理
	 */
	public  PageResult<Map<String, Object>>  getServiceActivityManageSummarySearchQuery(TtAsActivityBean ActivityBean, int curPage,int pageSize) throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append("	  select b.ACTIVITY_ID, b.ACTIVITY_CODE,  b.ACTIVITY_NAME, c.DEALER_CODE,  c.DEALER_NAME,c.DEALER_SHORTNAME,  r.REGION_NAME,  a.IN_AMOUNT,  to_char(b.STARTDATE,'yyyy-MM-dd')as STARTDATE,  to_char(b.ENDDATE,'yyyy-MM-dd')as ENDDATE \n");
		sql.append("      from TT_AS_ACTIVITY_EVALUATE a,  tt_as_activity  b, tm_dealer  c, tm_region r  \n");
		sql.append("      WHERE a.activity_id = b.activity_id AND  c.dealer_id = a.dealer_id AND c.province_id = r.region_id(+)  AND b.STATUS='"+Constant.SERVICEACTIVITY_STATUS_02+"'  AND  b.IS_DEL='"+Constant.IS_DEL_00+"' \n");//活动状态为：已经发布; 删除标志为：逻辑未删除;
		if(!"".equals(ActivityBean.getCompanyId())&&!(null==ActivityBean.getCompanyId())){//CompanyId不为空
			sql.append("		AND  b.COMPANY_ID ='"+ActivityBean.getCompanyId()+"' \n");
		}
		if(!"".equals(ActivityBean.getDealerCode())&&null!=ActivityBean.getDealerCode()){//经销商代码不为空
			sql.append("		AND C.DEALER_CODE in ( '"+ActivityBean.getDealerCode()+"' ) \n");
		}
		if(!"".equals(ActivityBean.getActivityCode())&&null!=ActivityBean.getActivityCode()){//活动编号不为空
			sql.append("		AND b.ACTIVITY_CODE like '% "+ActivityBean.getActivityCode()+" %'  \n");
		}
		
		if(!"".equals(ActivityBean.getStartdate())&&!(null==ActivityBean.getStartdate())){      //活动开始日期不为空
			sql.append("		AND b.STARTDATE >=to_date('"+ActivityBean.getStartdate()+" 00:00:00', 'yyyy-MM-dd HH24:mi:ss') \n");
		}
		if(!"".equals(ActivityBean.getEnddate())&&!(null==ActivityBean.getEnddate())){         //活动结束日期不为空
			sql.append("	    AND b.ENDDATE  <= to_date('"+ActivityBean.getEnddate()+" 23:59:59', 'yyyy-MM-dd HH24:mi:ss') \n");
		}
		if(!"".equals(ActivityBean.getDealerName())&&null!=ActivityBean.getDealerName()){//经销商名称不为空
			sql.append("		AND C.DEALER_NAME like '% "+ActivityBean.getDealerName()+" %'  \n");
		}
		sql.append("            ORDER BY A.ACTIVITY_ID DESC  \n");
		PageResult<Map<String, Object>> ps = (PageResult<Map<String, Object>>) pageQuery(sql.toString(), null,  getFunName(), pageSize, curPage);
		return ps;
	}

	
	public  PageResult<Map<String, Object>>  getServiceActivityManageSummaryEvaluateQuery(TtAsActivityBean ActivityBean, int curPage,int pageSize) throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT B.SUBJECT_ID,max(B.SUBJECT_NO) SUBJECT_NO ,max(B.SUBJECT_NAME) SUBJECT_NAME,max(B.ACTIVITY_TYPE) ACTIVITY_TYPE ,max(B.ACTIVITY_NUM) ACTIVITY_NUM," +
				"TO_CHAR(max(B.SUBJECT_START_DATE),'YYYY-MM-DD')" +
				" AS SUBJECT_START_DATE," +
				"TO_CHAR(max(B.SUBJECT_END_DATE),'YYYY-MM-DD') AS SUBJECT_END_DATE FROM TT_AS_ACTIVITY_EVALUATE A," +
				"TT_AS_ACTIVITY_SUBJECT B  WHERE B.EVALUATE = 0 AND  A.STATUS is not null AND A.SUBJECT_ID =B.SUBJECT_ID  ");
		
		if(!"".equals(ActivityBean.getSubject_no())&&null!=ActivityBean.getSubject_no()){//主题编号
			sql.append("		AND B.SUBJECT_NO like '%"+ActivityBean.getSubject_no()+"%'  \n");
		}
		if(!"".equals(ActivityBean.getSubject_name())&&null!=ActivityBean.getSubject_name()){//
			sql.append("		AND B.SUBJECT_NAME like '%"+ActivityBean.getSubject_name()+"%'  \n");
		}
		
		if(!"".equals(ActivityBean.getActivityType())&&null!=ActivityBean.getActivityType()){//
			sql.append("		AND B.ACTIVITY_TYPE ="+ActivityBean.getActivityType()+" \n");
		}
		
		if(!"".equals(ActivityBean.getSubject_start_date())&&!(null==ActivityBean.getSubject_start_date())){      //活动开始日期不为空
			sql.append("		AND B.SUBJECT_START_DATE <=to_date('"+ActivityBean.getSubject_start_date()+" 00:00:00', 'yyyy-MM-dd HH24:mi:ss') \n");
		}
		if(!"".equals(ActivityBean.getSubject_end_date())&&!(null==ActivityBean.getSubject_end_date())){         //活动结束日期不为空
			sql.append("	    AND b.SUBJECT_END_DATE  >= to_date('"+ActivityBean.getSubject_end_date()+" 23:59:59', 'yyyy-MM-dd HH24:mi:ss') \n");
		}
		sql.append("       group by  B.SUBJECT_ID      ORDER BY B.SUBJECT_ID DESC  \n");
		PageResult<Map<String, Object>> ps = (PageResult<Map<String, Object>>) pageQuery(sql.toString(), null,  getFunName(), pageSize, curPage);
		return ps;
	}
	
	
	public  PageResult<Map<String, Object>>  getServiceActivityManageSummaryEvaluateQueryZR(TtAsActivityBean ActivityBean, int curPage,int pageSize) throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT B.SUBJECT_ID,max(B.SUBJECT_NO) SUBJECT_NO,max(B.SUBJECT_NAME) SUBJECT_NAME,max(B.ACTIVITY_TYPE) ACTIVITY_TYPE,max(B.ACTIVITY_NUM) ACTIVITY_NUM," +
				"TO_CHAR(max(B.SUBJECT_START_DATE),'YYYY-MM-DD')" +
				" AS SUBJECT_START_DATE," +
				"TO_CHAR(max(B.SUBJECT_END_DATE),'YYYY-MM-DD') AS SUBJECT_END_DATE FROM TT_AS_ACTIVITY_EVALUATE A," +
				"TT_AS_ACTIVITY_SUBJECT B  WHERE B.EVALUATE = 1 AND  A.STATUS is not null AND A.SUBJECT_ID =B.SUBJECT_ID  ");
		
		if(!"".equals(ActivityBean.getSubject_no())&&null!=ActivityBean.getSubject_no()){//主题编号
			sql.append("		AND B.SUBJECT_NO like '%"+ActivityBean.getSubject_no()+"%'  \n");
		}
		if(!"".equals(ActivityBean.getSubject_name())&&null!=ActivityBean.getSubject_name()){//
			sql.append("		AND B.SUBJECT_NAME like '%"+ActivityBean.getSubject_name()+"%'  \n");
		}
		
		if(!"".equals(ActivityBean.getActivityType())&&null!=ActivityBean.getActivityType()){//
			sql.append("		AND B.ACTIVITY_TYPE ="+ActivityBean.getActivityType()+" \n");
		}
		
		if(!"".equals(ActivityBean.getSubject_start_date())&&!(null==ActivityBean.getSubject_start_date())){      //活动开始日期不为空
			sql.append("		AND B.SUBJECT_START_DATE <=to_date('"+ActivityBean.getSubject_start_date()+" 00:00:00', 'yyyy-MM-dd HH24:mi:ss') \n");
		}
		if(!"".equals(ActivityBean.getSubject_end_date())&&!(null==ActivityBean.getSubject_end_date())){         //活动结束日期不为空
			sql.append("	    AND b.SUBJECT_END_DATE  >= to_date('"+ActivityBean.getSubject_end_date()+" 23:59:59', 'yyyy-MM-dd HH24:mi:ss') \n");
		}
		sql.append("        group by   B.SUBJECT_ID  ORDER BY B.SUBJECT_ID DESC  \n");
		PageResult<Map<String, Object>> ps = (PageResult<Map<String, Object>>) pageQuery(sql.toString(), null,  getFunName(), pageSize, curPage);
		return ps;
	}
	
	public  PageResult<Map<String, Object>>  getServiceActivityManageSummaryEvaluateQueryChaxun(TtAsActivityBean ActivityBean, int curPage,int pageSize) throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT B.SUBJECT_ID,max(B.SUBJECT_NO)  SUBJECT_NO , max(B.SUBJECT_NAME) SUBJECT_NAME,max(B.ACTIVITY_TYPE) ACTIVITY_TYPE,max(B.ACTIVITY_NUM) ACTIVITY_NUM,max(B.EVALUATE) EVALUATE," +
				"TO_CHAR(max(B.SUBJECT_START_DATE),'YYYY-MM-DD')" +
				" AS SUBJECT_START_DATE," +
				"TO_CHAR(max(B.SUBJECT_END_DATE),'YYYY-MM-DD') AS SUBJECT_END_DATE FROM TT_AS_ACTIVITY_EVALUATE A," +
				"TT_AS_ACTIVITY_SUBJECT B  WHERE   A.STATUS is not null AND A.SUBJECT_ID =B.SUBJECT_ID  ");
		
		if(!"".equals(ActivityBean.getSubject_no())&&null!=ActivityBean.getSubject_no()){//主题编号
			sql.append("		AND B.SUBJECT_NO like '%"+ActivityBean.getSubject_no()+"%'  \n");
		}
		if(!"".equals(ActivityBean.getSubject_name())&&null!=ActivityBean.getSubject_name()){//
			sql.append("		AND B.SUBJECT_NAME like '%"+ActivityBean.getSubject_name()+"%'  \n");
		}
		
		if(!"".equals(ActivityBean.getActivityType())&&null!=ActivityBean.getActivityType()){//
			sql.append("		AND B.ACTIVITY_TYPE ="+ActivityBean.getActivityType()+" \n");
		}
		
		if(!"".equals(ActivityBean.getSubject_start_date())&&!(null==ActivityBean.getSubject_start_date())){      //活动开始日期不为空
			sql.append("		AND B.SUBJECT_START_DATE <=to_date('"+ActivityBean.getSubject_start_date()+" 00:00:00', 'yyyy-MM-dd HH24:mi:ss') \n");
		}
		if(!"".equals(ActivityBean.getSubject_end_date())&&!(null==ActivityBean.getSubject_end_date())){         //活动结束日期不为空
			sql.append("	    AND b.SUBJECT_END_DATE  >= to_date('"+ActivityBean.getSubject_end_date()+" 23:59:59', 'yyyy-MM-dd HH24:mi:ss') \n");
		}
		sql.append("         group by  B.SUBJECT_ID  ORDER BY B.SUBJECT_ID DESC  \n");
		PageResult<Map<String, Object>> ps = (PageResult<Map<String, Object>>) pageQuery(sql.toString(), null,  getFunName(), pageSize, curPage);
		return ps;
	}
    public List<Map<String, Object>> serviceActivityEvaluateQuery(String subject_id)
    {
    	StringBuffer sql= new StringBuffer();
    	sql.append("SELECT rownum as ID, a.* from (\n" );
    	sql.append("SELECT D.DEALER_CODE,\n" );
    	sql.append("       D.DEALER_NAME,\n" );
    	sql.append("       D.DEALER_ID,\n" );
    	sql.append("       R.SUBJECT_ID,\n" );
    	sql.append("       min(F.EVALUATE_RES) EVALUATE_RES,\n" );
    	sql.append("       min(nvl(F.BALANCE_AMOUNT,0)) EVALUATE_AMOUNT,min(F.TAX_RATE_FEE) TAX_RATE_FEE, \n" );
    	sql.append("  min(R.SUBJECT_NO) SUBJECT_NO,");
    	sql.append("  min(R.ACTIVITY_TYPE) ACTIVITY_TYPE   , ");
    	sql.append(" min(R.SUBJECT_NAME) SUBJECT_NAME, ");
    	sql.append("       SUM(NVL(R.BALANCE_AMOUNT, 0)) AS BALANCE_AMOUNT,  SUM(NVL(R.REPAIR_TOTAL, 0))  REPAIR_TOTAL \n" );
    	sql.append("  FROM TM_DEALER D\n" );
    	sql.append("  , (SELECT A.DEALER_ID, A.BALANCE_AMOUNT,A.REPAIR_TOTAL,S.SUBJECT_ID,S.SUBJECT_NO,S.ACTIVITY_TYPE,S.SUBJECT_NAME\n" );
    	sql.append("               FROM TT_AS_WR_APPLICATION   A,\n" );
    	sql.append("                    TT_AS_ACTIVITY         T,\n" );
    	sql.append("                    TT_AS_ACTIVITY_SUBJECT S,\n" );
    	sql.append("                    tt_as_activity_evaluate E\n" );
    	sql.append("              WHERE A.CAMPAIGN_CODE = T.ACTIVITY_CODE\n" );
    	sql.append("                AND T.SUBJECT_ID = S.SUBJECT_ID\n" );
    	sql.append("                AND S.ACTIVITY_TYPE != 10561001\n" );
    	 sql.append("                AND A.STATUS = 10791013\n" );
    	sql.append("                AND s.subject_id = E.SUBJECT_ID\n" );
    	sql.append("                AND A.DEALER_ID = E.DEALER_ID\n" );
    	sql.append("                AND E.STATUS is not null\n" );
    	sql.append("             ) R left join TT_AS_SUBJIET_evaluate F on F.SUBJECT_ID = R.SUBJECT_ID AND F.DEALER_ID = R.DEALER_ID\n" );
    	sql.append(" WHERE D.DEALER_LEVEL = 10851001 and D.DEALER_ID = R.DEALER_ID\n" );
    	sql.append(" AND R.SUBJECT_ID = "+ subject_id);
    	sql.append(" GROUP BY D.DEALER_CODE, D.DEALER_NAME,D.DEALER_ID,R.subject_id) a");
    	return dao.pageQuery(sql.toString(), null, this.getFunName());

    }
	
    public List<Map<String, Object>> serviceActivityEvaluateQueryInfor(String subject_id,String DEALER_ID)
    {
    	StringBuffer sql= new StringBuffer();
    	sql.append("SELECT *\n" );
    	sql.append("  FROM (SELECT CASE\n" );
    	sql.append("                 WHEN GROUPING(D.DEALER_CODE) = 0 AND\n" );
    	sql.append("                      GROUPING(R.SECOND_DEALER_CODE) = 1 THEN\n" );
    	sql.append("                  D.DEALER_NAME || '(' || D.DEALER_CODE || ')' || '小计:'\n" );
    	sql.append("                 ELSE\n" );
    	sql.append("                  DECODE(R.SECOND_DEALER_NAME,\n" );
    	sql.append("                         NULL,\n" );
    	sql.append("                         NULL,\n" );
    	sql.append("                         '  ' || r.l || R.SECOND_DEALER_NAME || '(' ||\n" );
    	sql.append("                         R.SECOND_DEALER_CODE || ')')\n" );
    	sql.append("               END DEALER_NAME,\n" );
    	sql.append("               SUM(NVL(R.BALANCE_AMOUNT, 0)) AS BALANCE_AMOUNT\n" );
    	sql.append("          FROM TM_DEALER D\n" );
    	sql.append("          LEFT JOIN (SELECT A.DEALER_ID,\n" );
    	sql.append("                           NVL(A.SECOND_DEALER_CODE, D.DEALER_CODE) SECOND_DEALER_CODE,\n" );
    	sql.append("                           NVL(A.SECOND_DEALER_NAME, D.DEALER_NAME) SECOND_DEALER_NAME,\n" );
    	sql.append("                           decode(A.SECOND_DEALER_CODE, NULL, '一级', '二级') l,\n" );
    	sql.append("                           A.BALANCE_AMOUNT\n" );
    	sql.append("                      FROM TT_AS_WR_APPLICATION   A,\n" );
    	sql.append("                           TT_AS_ACTIVITY         T,\n" );
    	sql.append("                           TT_AS_ACTIVITY_SUBJECT S,\n" );
    	sql.append("                           TM_DEALER              D\n" );
    	sql.append("                     WHERE A.DEALER_ID = D.DEALER_ID\n" );
    	sql.append("                       AND A.CAMPAIGN_CODE = T.ACTIVITY_CODE\n" );
    	sql.append("                       AND T.SUBJECT_ID = S.SUBJECT_ID\n" );
    	sql.append("                       AND S.SUBJECT_ID = "+subject_id+"\n" );
    	sql.append("                       AND S.ACTIVITY_TYPE != 10561001\n" );
    	sql.append("                       AND A.STATUS = 10791013) R ON D.DEALER_ID =\n" );
    	sql.append("                                                     R.DEALER_ID\n" );
    	sql.append("         WHERE D.DEALER_LEVEL = 10851001\n" );
    	sql.append("           and D.DEALER_ID = "+DEALER_ID+"\n" );
    	sql.append("         GROUP BY ROLLUP((D.DEALER_CODE, D.DEALER_NAME),\n" );
    	sql.append("                         (r.l, R.SECOND_DEALER_CODE, R.SECOND_DEALER_NAME)))\n" );
    	sql.append(" WHERE DEALER_NAME IS NOT NULL");
    	return dao.pageQuery(sql.toString(), null, this.getFunName());
    }
    
	public  PageResult<Map<String, Object>>  getServiceActivityManageSummaryDealerSearchQuery(TtAsActivityBean ActivityBean, int curPage,int pageSize) throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT B.SUBJECT_ID,B.SUBJECT_NO,B.SUBJECT_NAME,B.ACTIVITY_TYPE,B.ACTIVITY_NUM,C.DEALER_CODE,C.DEALER_NAME,C.DEALER_ID," +
				"TO_CHAR(B.SUBJECT_START_DATE,'YYYY-MM-DD')" +
				" AS SUBJECT_START_DATE," +
				"TO_CHAR(B.SUBJECT_END_DATE,'YYYY-MM-DD') AS SUBJECT_END_DATE FROM TT_AS_ACTIVITY_EVALUATE A," +
				"TT_AS_ACTIVITY_SUBJECT B ,TM_DEALER C WHERE A.STATUS is not null AND A.SUBJECT_ID =B.SUBJECT_ID AND A.DEALER_ID=C.DEALER_ID ");
		
		if(!"".equals(ActivityBean.getSubject_no())&&null!=ActivityBean.getSubject_no()){//主题编号
			sql.append("		AND B.SUBJECT_NO like '%"+ActivityBean.getSubject_no()+"%'  \n");
		}
		if(!"".equals(ActivityBean.getSubject_name())&&null!=ActivityBean.getSubject_name()){//
			sql.append("		AND B.SUBJECT_NAME like '%"+ActivityBean.getSubject_name()+"%'  \n");
		}
		
		if(!"".equals(ActivityBean.getDealerCode())&&null!=ActivityBean.getDealerCode()){//
			sql.append("		AND C.DEALER_CODE ="+ActivityBean.getDealerCode()+" \n");
		}
		
		if(!"".equals(ActivityBean.getDealerName())&&null!=ActivityBean.getDealerName()){//
			sql.append("		AND C.DEALER_NAME like '%"+ActivityBean.getDealerName()+"%'  \n");
		}
		
		if(!"".equals(ActivityBean.getActivityType())&&null!=ActivityBean.getActivityType()){//
			sql.append("		AND B.ACTIVITY_TYPE ="+ActivityBean.getActivityType()+" \n");
		}
		
		if(!"".equals(ActivityBean.getSubject_start_date())&&!(null==ActivityBean.getSubject_start_date())){      //活动开始日期不为空
			sql.append("		AND B.SUBJECT_START_DATE <=to_date('"+ActivityBean.getSubject_start_date()+" 00:00:00', 'yyyy-MM-dd HH24:mi:ss') \n");
		}
		if(!"".equals(ActivityBean.getSubject_end_date())&&!(null==ActivityBean.getSubject_end_date())){         //活动结束日期不为空
			sql.append("	    AND b.SUBJECT_END_DATE  >= to_date('"+ActivityBean.getSubject_end_date()+" 23:59:59', 'yyyy-MM-dd HH24:mi:ss') \n");
		}
		sql.append("            ORDER BY B.SUBJECT_ID DESC  \n");
		PageResult<Map<String, Object>> ps = (PageResult<Map<String, Object>>) pageQuery(sql.toString(), null,  getFunName(), pageSize, curPage);
		return ps;
	}

	
	public  List<TtAsWrApplicationPO> getApplication(String subject_id, String DEALER_ID)
	{
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT a.* \n" );
		sql.append("  FROM TT_AS_WR_APPLICATION A, TT_AS_ACTIVITY T, TT_AS_ACTIVITY_SUBJECT S\n" );
		sql.append("\n" );
		sql.append(" WHERE A.CAMPAIGN_CODE = T.ACTIVITY_CODE\n" );
		sql.append("   AND T.SUBJECT_ID = S.SUBJECT_ID\n" );
		sql.append("   AND S.ACTIVITY_TYPE != 10561001\n" );
		sql.append("   AND A.STATUS = 10791013\n" );
		sql.append("   AND s.subject_id = "+subject_id+"\n" );
		sql.append("   And A.DEALER_ID = "+DEALER_ID+"");
		List<TtAsWrApplicationPO> list = dao.select(TtAsWrApplicationPO.class, sql.toString(), null);
		return list;

	} 
	
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}
	/**
	 * Function         : 服务活动管理---服务活动评估查询[明细]查询方法
	 * @return          : 满足条件的服务活动管理信息
	 * @throws          : 
	 * LastUpdate       : 2010-06-13
	 */
	public TtAsActivityEvaluatePO serviceActivitySummaryQuery(String activityId,Long companyId){
		List<Object> params = new LinkedList();
		StringBuffer sql = new StringBuffer();
		sql.append("	SELECT A.ACTIVITY_NAME,  B.IN_AMOUNT, B.IN_GUARANTEE, B.OUT_GUARANTEE,  B.REPAIR_AMOUNT, B.MAINTAIN_AMOUNT,  B.ON_AMOUNT,  B.ON_CAMOUNT,B.EVALUATE,  B.MEASURES	,org.org_name	\n");//已经发布
		sql.append("	FROM  tm_dealer dealer, tm_dealer_org_relation org_relation, tm_org org,TT_AS_ACTIVITY A 	\n");
		sql.append("	LEFT OUTER JOIN TT_AS_ACTIVITY_EVALUATE B ON A.ACTIVITY_ID = B.ACTIVITY_ID	\n");
		sql.append("	WHERE A.STATUS="+Constant.SERVICEACTIVITY_STATUS_02+" AND A.IS_DEL="+Constant.IS_DEL_00+" 	\n");
		if(!"".equals(companyId.toString())&&!(null==companyId.toString())){//公司ID不为空
			sql.append("		AND A.COMPANY_ID = '"+companyId.toString()+"'  \n");
		}
		sql.append("    AND  dealer.dealer_id = org_relation.dealer_id    \n");
		sql.append("    AND  org_relation.org_id = org.org_id     \n");
		sql.append("    AND dealer.dealer_id = B.Dealer_Id     \n");
		if (activityId!=null&&!("").equals(activityId)){
			sql.append(" AND A.ACTIVITY_ID = ?  \n");
			params.add(activityId);
			}
		TtAsActivityEvaluatePO evaluatePO=new TtAsActivityEvaluatePO();
		List list = dao.select(TtAsActivityEvaluatePO.class, sql.toString(), params);
			if (list!=null){
				if (list.size()>0) {
					evaluatePO = (TtAsActivityEvaluatePO) list.get(0);
				}
			}
        return evaluatePO;
	}
}