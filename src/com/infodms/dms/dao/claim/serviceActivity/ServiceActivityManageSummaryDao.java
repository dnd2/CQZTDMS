/**********************************************************************
* <pre>
* FILE : ServiceActivityPlanAnalyseDao.java
* CLASS : ServiceActivityPlanAnalyseDao
*
* AUTHOR : PGM
*
* FUNCTION :  服务活动管理---服务活动计划分析
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
 * $Id: ServiceActivityManageSummaryDao.java,v 1.1 2010/08/16 01:42:18 yuch Exp $
 */
package com.infodms.dms.dao.claim.serviceActivity;
import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;

import com.infodms.dms.bean.TtAsActivityBean;
import com.infodms.dms.bean.ttAsActivitySubjectBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.po.TtAsActivityDealerPO;
import com.infodms.dms.po.TtAsActivityEvaluatePO;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;
/**
 * Function       :  服务活动管理---服务活动评估
 * @author        :  PGM
 * CreateDate     :  2010-06-13
 * @version       :  0.1
 */
@SuppressWarnings("unchecked")
public class ServiceActivityManageSummaryDao extends BaseDao{
	public static Logger logger = Logger.getLogger(ServiceActivityManageSummaryDao.class);
	private static final ServiceActivityManageSummaryDao dao = new ServiceActivityManageSummaryDao ();
	public  static final ServiceActivityManageSummaryDao getInstance() {
		return dao;
	}

	/**
	 * Function         : 服务活动管理---服务活动评估
	 * @param           : 活动编号
	 * @param           : 活动开始日期
	 * @param           : 活动结束日期
	 * @param           : 当前页码
	 * @param           : 每页显示记录数
	 * @return          : 满足条件的服务活动管理---服务活动评估信息，包含分页信息
	 * @throws          : Exception
	 * LastUpdate       : 服务活动管理
	 */
	public  PageResult<Map<String, Object>>  getServiceActivityManageSummaryQuery(TtAsActivityBean ActivityBean, int curPage,int pageSize) throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT A.SUBJECT_ID,A.SUBJECT_NO,A.SUBJECT_NAME,A.ACTIVITY_TYPE,A.DUTY_PERSON,A.SUBJECT_START_DATE,A.SUBJECT_END_DATE,B.ON_AMOUNT,B.ON_CAMOUNT,B.EVALUATE,B.MEASURES,B.ID FROM");
		sql.append("  TT_AS_ACTIVITY_SUBJECT A LEFT JOIN TT_AS_ACTIVITY_EVALUATE B ON A.SUBJECT_ID=B.SUBJECT_ID WHERE 1=1 AND A.IS_DEL=0");
		
		if(!"".equals(ActivityBean.getSubjectId())&&null!=ActivityBean.getSubjectId())
		{
			sql.append("		AND A.SUBJECT_ID = "+ActivityBean.getSubjectId()+"   \n");
		}
		
		if(!"".equals(ActivityBean.getSubject_no())&&null!=ActivityBean.getSubject_no()){//主题编号不为空
			sql.append("		AND A.SUBJECT_NO like '% "+ActivityBean.getSubject_no()+" %'  \n");
		}
		if(!"".equals(ActivityBean.getSubject_name())&&null!=ActivityBean.getSubject_name()){//主题编号不为空
			sql.append("		AND A.SUBJECT_NO like '% "+ActivityBean.getSubject_name()+" %'  \n");
		}
		
		
		
		if(!"".equals(ActivityBean.getStartdate())&&!(null==ActivityBean.getStartdate())){      //活动开始日期不为空
			sql.append("		AND A.STARTDATE >=to_date('"+ActivityBean.getStartdate()+" 00:00:00', 'yyyy-MM-dd HH24:mi:ss') \n");
		}
		if(!"".equals(ActivityBean.getEnddate())&&!(null==ActivityBean.getEnddate())){         //活动结束日期不为空
			sql.append("	    AND A.ENDDATE  <= to_date('"+ActivityBean.getEnddate()+" 23:59:59', 'yyyy-MM-dd HH24:mi:ss') \n");
		}
		sql.append("           GROUP BY A.ACTIVITY_ID,  A.ACTIVITY_CODE,  A.ACTIVITY_NAME,  STARTDATE,  ENDDATE  \n");
		PageResult<Map<String, Object>> ps = (PageResult<Map<String, Object>>) pageQuery(sql.toString(), null,  getFunName(), pageSize, curPage);
		return ps;
	}

	@SuppressWarnings("unchecked")
	public  PageResult<Map<String, Object>>  getAllServiceActivityManageMotive(ttAsActivitySubjectBean ttAsActivityBean,int curPage,int pageSize)
	{
		StringBuffer sql = new StringBuffer();
		sql.append(" select TT.* from TT_AS_ACTIVITY_SUBJECT TT left join TT_AS_ACTIVITY_EVALUATE EE on EE.Subject_Id = TT.Subject_Id and EE.DEALER_ID= "+ttAsActivityBean.getRemark()+"  where ((EE.Status is null and  TT.IS_DEL != 1) or  EE.Status !="+ Constant.SERVICEACTIVITY_REPAIR_STATUS_01+ ")");
		
		sql.append("ANd TT.SUBJECT_ID in\n" );
		sql.append("       (SELECT A.SUBJECT_ID\n" );
		sql.append("          from TT_AS_ACTIVITY A, TT_AS_ACTIVITY_DEALER B\n" );
		sql.append("         where B.DEALER_ID = "+ttAsActivityBean.getRemark()+"\n" );
		sql.append("           AND A.ACTIVITY_ID = B.ACTIVITY_ID)");

		if(!"".equals(ttAsActivityBean.getSubjectNo())){
			sql.append(" AND UPPER(TT.SUBJECT_NO) like UPPER('%"+ttAsActivityBean.getSubjectNo()+"%')\n");
		}
		
		if(!"".equals(ttAsActivityBean.getActivityType()) &&!(null==ttAsActivityBean.getActivityType())){
			sql.append(" AND TT.ACTIVITY_TYPE = '"+ttAsActivityBean.getActivityType()+"'  \n");
		}
		
		if(!"".equals(ttAsActivityBean.getSubjectName())){
			sql.append(" AND UPPER(TT.SUBJECT_NAME) like UPPER('%"+ttAsActivityBean.getSubjectName()+"%')\n");
		}
		
		if(!"".equals(ttAsActivityBean.getSubjectStartDate()) &&!(null==ttAsActivityBean.getSubjectStartDate())){      //活动开始日期不为空
			sql.append(" AND TT.SUBJECT_START_DATE >=to_date('"+ttAsActivityBean.getSubjectStartDate()+" 00:00:00', 'yyyy-MM-dd HH24:mi:ss') \n");
		}
		if(!"".equals(ttAsActivityBean.getSubjectEndDate()) &&!(null==ttAsActivityBean.getSubjectEndDate())){         //活动结束日期不为空
			sql.append(" AND TT.SUBJECT_END_DATE  <= to_date('"+ttAsActivityBean.getSubjectEndDate()+" 23:59:59', 'yyyy-MM-dd HH24:mi:ss') \n");
		}
	// 	sql.append(" AND TT.ACTIVITY_TYPE != 10561001");
		PageResult<Map<String, Object>> pageQuery = (PageResult<Map<String, Object>>) pageQuery(sql.toString(), null,  getFunName(), pageSize, curPage);
		PageResult<Map<String, Object>> ps = pageQuery;
		return ps;
	}
	
	public List<Map<String,Object>> SType(String subjetId,String dealerId)
	{
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT F.add_item_code ADD_ITEM_CODE,\n" );
		sql.append("       MIN( F.ACOUNT) ACOUNT ,\n" );
		sql.append("       SUM(F.ADD_ITEM_AMOUNT) ADD_ITEM_AMOUNT,\n" );
		sql.append("       SUM(NVL(T.COUNT_SUM, 0)) AS YACCOUNT,\n" );
		sql.append("       SUM(NVL(T.MONNEY, 0)) YAMOUNT FROM ( \n" );
		
		sql.append("  SELECT count(A.add_item_code) ACOUNT ,    SUM( decode ( A.add_item_code,3537007 , D.PART_DOWN ,3537006,D.LABOUR_DOWN , A.ADD_ITEM_AMOUNT )  ) ADD_ITEM_AMOUNT,    ");
		sql.append("  A.add_item_code add_item_code , MIN(C.SUBJECT_ID) SUBJECT_ID  ");
		sql.append("  FROM    TT_AS_REPAIR_ORDER  G , TT_AS_WR_APPLICATION D ,TT_AS_RO_ADD_ITEM A\n" );
		sql.append("  JOIN TT_AS_ACTIVITY B ON A.ACTIVITY_CODE = B.ACTIVITY_CODE\n" );
		sql.append("  JOIN TT_AS_ACTIVITY_SUBJECT C ON B.SUBJECT_ID = C.SUBJECT_ID\n" );
		sql.append("                               AND C.ACTIVITY_TYPE != 10561001\n" );
		sql.append(" WHERE A.CHARGE_PARTITION_CODE = 'S'\n" );
		sql.append("   and D.RO_NO = G.RO_NO  ");
		sql.append("   and  G.ID = A.RO_ID and  G.DEALER_ID =  " + dealerId); 
		sql.append("   AND C.SUBJECT_ID = "+subjetId+"  GROUP BY a.add_item_code   ) F \n" );
		
		sql.append("  LEFT JOIN TT_AS_ACTIVITY_TYPE T ON F.add_item_code = T.CODE_TYPE\n" );
		sql.append("                                 AND F.SUBJECT_ID = T.SUMMARY_ID AND T.ACT_TYPE = 0 AND T.DEALER_ID = \n"+ dealerId );
		
		sql.append(" GROUP BY F.add_item_code  ");
		List<Map<String,Object>> map= this.pageQuery(sql.toString(),null, getFunName());
		return map;

	}
	
	public List<Map<String,Object>> SAmount(String subjetId ,String dealerId)
	{
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT \n" );
		sql.append("       MIN( F.ACOUNT) ACOUNT ,\n" );
		sql.append("       SUM(F.ADD_ITEM_AMOUNT) ADD_ITEM_AMOUNT,\n" );
		sql.append("       SUM(NVL(T.COUNT_SUM, 0)) AS YACCOUNT,\n" );
		sql.append("       SUM(NVL(T.MONNEY, 0)) YAMOUNT FROM ( \n" );
		
		sql.append("  SELECT count(A.add_item_code) ACOUNT , SUM( decode ( A.add_item_code,3537007 , D.PART_DOWN ,3537006,D.LABOUR_DOWN , A.ADD_ITEM_AMOUNT )  ) ADD_ITEM_AMOUNT,   ");
		sql.append("  A.add_item_code add_item_code , MIN(C.SUBJECT_ID) SUBJECT_ID  ");
		sql.append("  FROM    TT_AS_REPAIR_ORDER  G  ,TT_AS_WR_APPLICATION D ,TT_AS_RO_ADD_ITEM A\n" );
		sql.append("  JOIN TT_AS_ACTIVITY B ON A.ACTIVITY_CODE = B.ACTIVITY_CODE\n" );
		sql.append("  JOIN TT_AS_ACTIVITY_SUBJECT C ON B.SUBJECT_ID = C.SUBJECT_ID\n" );
		sql.append("                               AND C.ACTIVITY_TYPE != 10561001\n" );
		sql.append(" WHERE A.CHARGE_PARTITION_CODE = 'S'  and D.RO_NO = G.RO_NO  \n" );
		sql.append("  and  G.ID = A.RO_ID and  G.DEALER_ID =  " + dealerId); 
		sql.append("   AND C.SUBJECT_ID = "+subjetId+"  GROUP BY a.add_item_code   ) F \n" );
		
		sql.append("  LEFT JOIN TT_AS_ACTIVITY_TYPE T ON F.add_item_code = T.CODE_TYPE\n" );
		sql.append("                                 AND F.SUBJECT_ID = T.SUMMARY_ID AND T.ACT_TYPE = 0 AND T.DEALER_ID = \n"+ dealerId );
		
	//	sql.append(" GROUP BY F.add_item_code  ");
		List<Map<String,Object>> map= this.pageQuery(sql.toString(),null, getFunName());
		return map;


	}
	
	public List<Map<String,Object>> ZAmount(String subjetId ,String dealerId)
	{
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT \n" );
		sql.append("       MIN( F.ACOUNT) ACOUNT ,\n" );
		sql.append("       SUM(F.ADD_ITEM_AMOUNT) ADD_ITEM_AMOUNT,\n" );
		sql.append("       SUM(NVL(T.COUNT_SUM, 0)) AS YACCOUNT,\n" );
		sql.append("       SUM(NVL(T.MONNEY, 0)) YAMOUNT FROM ( \n" );
		
		sql.append("  SELECT  count(A.add_item_code) ACOUNT , SUM( decode ( A.add_item_code,3537007 , D.PART_DOWN ,3537006,D.LABOUR_DOWN , A.ADD_ITEM_AMOUNT )  ) ADD_ITEM_AMOUNT,   ");
		sql.append("  A.add_item_code add_item_code , MIN(C.SUBJECT_ID)  SUBJECT_ID ");
		sql.append("  FROM    TT_AS_REPAIR_ORDER  G ,TT_AS_WR_APPLICATION D ,TT_AS_RO_ADD_ITEM A\n" );
		sql.append("  JOIN TT_AS_ACTIVITY B ON A.ACTIVITY_CODE = B.ACTIVITY_CODE\n" );
		sql.append("  JOIN TT_AS_ACTIVITY_SUBJECT C ON B.SUBJECT_ID = C.SUBJECT_ID\n" );
		sql.append("                               AND C.ACTIVITY_TYPE != 10561001\n" );
		sql.append(" WHERE A.CHARGE_PARTITION_CODE  IS NULL  and D.RO_NO = G.RO_NO  \n" );
		sql.append("  and  G.ID = A.RO_ID and  G.DEALER_ID =  " + dealerId); 
		sql.append("   AND C.SUBJECT_ID = "+subjetId+"  GROUP BY a.add_item_code   ) F \n" );
		
		sql.append("  LEFT JOIN TT_AS_ACTIVITY_TYPE T ON F.add_item_code = T.CODE_TYPE\n" );
		sql.append("                                 AND F.SUBJECT_ID = T.SUMMARY_ID AND T.ACT_TYPE = 1 AND T.DEALER_ID = \n"+ dealerId );
		
		// sql.append(" GROUP BY F.add_item_code  ");
		List<Map<String,Object>> map= this.pageQuery(sql.toString(),null, getFunName());
		return map;
	}
	
	public List<Map<String,Object>> ZType(String subjetId,String dealerId)
	{
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT F.add_item_code ADD_ITEM_CODE,\n" );
		sql.append("       MIN( F.ACOUNT) ACOUNT ,\n" );
		sql.append("       SUM(F.ADD_ITEM_AMOUNT) ADD_ITEM_AMOUNT,\n" );
		sql.append("       SUM(NVL(T.COUNT_SUM, 0)) AS YACCOUNT,\n" );
		sql.append("       SUM(NVL(T.MONNEY, 0)) YAMOUNT FROM ( \n" );
		
		sql.append("  SELECT  count(A.add_item_code) ACOUNT , SUM( decode ( A.add_item_code,3537007 , D.PART_DOWN ,3537006,D.LABOUR_DOWN , A.ADD_ITEM_AMOUNT )  ) ADD_ITEM_AMOUNT,  ");
		sql.append("  A.add_item_code add_item_code , MIN(C.SUBJECT_ID)  SUBJECT_ID ");
		sql.append("  FROM    TT_AS_REPAIR_ORDER  G , TT_AS_WR_APPLICATION D ,TT_AS_RO_ADD_ITEM A\n" );
		sql.append("  JOIN TT_AS_ACTIVITY B ON A.ACTIVITY_CODE = B.ACTIVITY_CODE\n" );
		sql.append("  JOIN TT_AS_ACTIVITY_SUBJECT C ON B.SUBJECT_ID = C.SUBJECT_ID\n" );
		sql.append("                               AND C.ACTIVITY_TYPE != 10561001\n" );
		sql.append(" WHERE A.CHARGE_PARTITION_CODE  IS NULL and D.RO_NO = G.RO_NO  \n" );
		sql.append("  and  G.ID = A.RO_ID and  G.DEALER_ID =  " + dealerId); 
		sql.append("   AND C.SUBJECT_ID = "+subjetId+"  GROUP BY a.add_item_code   ) F \n" );
		
		sql.append("  LEFT JOIN TT_AS_ACTIVITY_TYPE T ON F.add_item_code = T.CODE_TYPE\n" );
		sql.append("                                 AND F.SUBJECT_ID = T.SUMMARY_ID AND T.ACT_TYPE = 1 AND T.DEALER_ID = \n"+ dealerId );
		
		sql.append(" GROUP BY F.add_item_code  ");
		List<Map<String,Object>> map= this.pageQuery(sql.toString(),null, getFunName());
		return map;


	}
	
	
	
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}
	/**
	 * Function         : 服务活动管理---服务活动评估[编辑]查询方法：状态为：[已经下发]的活动
	 * @return          : 满足条件的服务活动管理信息
	 * @throws          : 
	 * LastUpdate       : 2010-06-13
	 */
	public TtAsActivityBean serviceActivitySummaryQuery(String subjectid,String dealerId){
		List<Object> params = new LinkedList();
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT A.SUBJECT_ID,A.SUBJECT_NO,A.SUBJECT_NAME,nvl(B.re_status,2) re_status,A.ACTIVITY_TYPE,A.DUTY_PERSON,to_char(A.SUBJECT_START_DATE,'yyyy-mm-dd') AS SUBJECT_START_DATE,to_char(A.SUBJECT_END_DATE,'yyyy-mm-dd') AS SUBJECT_END_DATE, ((SYSDATE - A.SUBJECT_END_DATE) - A.Days) Days ,B.ON_AMOUNT,B.ON_CAMOUNT,B.EVALUATE,B.MEASURES,B.ID AS EVALUATEID,B.DEALER_ID, ");
		sql.append(" B.pull_in_Num,B.pull_in_mean,B.pull_in_region,B.pull_in_incre ,");
		sql.append(" B.customer_Num,B.customer_mean ,B.customer_region ,B.customer_incre , ");
		sql.append(" B.price_Num ,B.price_mean,B.price_region ,B.price_incre , ");
		sql.append(" B.open_Num ,B.open_mean ,B.open_region ,B.open_incre ");
		sql.append(" FROM");
		sql.append("  TT_AS_ACTIVITY_SUBJECT A LEFT JOIN TT_AS_ACTIVITY_EVALUATE B ON A.SUBJECT_ID=B.SUBJECT_ID   ");
		if (dealerId!=null&&!("").equals(dealerId)){
			sql.append(" AND B.DEALER_ID = "+dealerId+"  \n");
			}
		
		sql.append("  WHERE 1=1  ") ; 
		if (subjectid!=null&&!("").equals(subjectid)){
			sql.append(" AND A.SUBJECT_ID = "+subjectid+"  \n");
			}
		TtAsActivityBean evaluatePO=new TtAsActivityBean();
		List list = dao.select(TtAsActivityBean.class, sql.toString(), params);
			if (list!=null){
				if (list.size()>0) {
					evaluatePO = (TtAsActivityBean) list.get(0);
				}
			}
	   
        return evaluatePO;
	}
	
	
	public List<TtAsActivityBean> serviceActivitypart(String subjectid,String dealerId){
		List<Object> params = new LinkedList();
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT Y.ACTIVITY_CODE,Y.ACTIVITY_NAME,A.PART_NUM,B.PART_NUM_W FROM TT_AS_ACTIVITY Y,TT_AS_ACTIVITY_DEALER AD,");
		sql.append("  (SELECT R.CAM_CODE, SUM(P.PART_QUANTITY) AS PART_NUM  FROM   TT_AS_REPAIR_ORDER R, TT_AS_RO_REPAIR_PART P ");
		sql.append("  WHERE  R.DEALER_ID = "+dealerId+" AND R.ID = P.RO_ID  AND R.RO_STATUS = 11591002 AND R.CAM_CODE IS NOT NULL   AND P.RO_ID IN");
		sql.append(" (SELECT RO_ID FROM TT_AS_RO_REPAIR_PART WHERE PAY_TYPE = 11801002) GROUP BY R.CAM_CODE) A ,");
		sql.append(" (SELECT R.CAM_CODE, SUM(P.PART_QUANTITY) AS PART_NUM_W   FROM TT_AS_REPAIR_ORDER R, TT_AS_RO_REPAIR_PART P WHERE R.ID = P.RO_ID");
		sql.append(" AND R.DEALER_ID = "+dealerId+" AND R.RO_STATUS = 11591002 AND R.CAM_CODE IS NOT NULL AND P.RO_ID IN  (SELECT RO_ID  FROM TT_AS_RO_REPAIR_PART WHERE RO_ID IN (SELECT TMP.RO_ID");
		sql.append("  FROM (SELECT RO_ID, MAX(TT.PAY_TYPE)  FROM TT_AS_RO_REPAIR_PART TT WHERE PAY_TYPE = 11801001  GROUP BY TT.RO_ID) TMP))");
		sql.append("  GROUP BY R.CAM_CODE)  B  WHERE Y.ACTIVITY_CODE=A.CAM_CODE(+) AND Y.ACTIVITY_CODE=B.CAM_CODE(+) ");
		if (subjectid!=null&&!("").equals(subjectid)){
			sql.append(" AND Y.SUBJECT_ID = "+subjectid+"  AND Y.IS_DEL=0  AND Y.ACTIVITY_ID=AD.ACTIVITY_ID AND AD.DEALER_ID="+dealerId+" \n");
			}
		List list = dao.select(TtAsActivityBean.class, sql.toString(), params);
        return list;
	}
	
	
	public List<TtAsActivityBean> serviceActivitypart(String subjectid){
		List<Object> params = new LinkedList();
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT Y.ACTIVITY_CODE,Y.ACTIVITY_NAME,A.PART_NUM,B.PART_NUM_W FROM TT_AS_ACTIVITY Y,");
		sql.append("  (SELECT R.CAM_CODE, SUM(P.PART_QUANTITY) AS PART_NUM  FROM   TT_AS_REPAIR_ORDER R, TT_AS_RO_REPAIR_PART P ");
		sql.append("  WHERE   R.ID = P.RO_ID  AND R.RO_STATUS = 11591002 AND R.CAM_CODE IS NOT NULL   AND P.RO_ID IN");
		sql.append(" (SELECT RO_ID FROM TT_AS_RO_REPAIR_PART WHERE PAY_TYPE = 11801002) GROUP BY R.CAM_CODE) A ,");
		sql.append(" (SELECT R.CAM_CODE, SUM(P.PART_QUANTITY) AS PART_NUM_W   FROM TT_AS_REPAIR_ORDER R, TT_AS_RO_REPAIR_PART P WHERE R.ID = P.RO_ID");
		sql.append("  AND R.RO_STATUS = 11591002 AND R.CAM_CODE IS NOT NULL AND P.RO_ID IN  (SELECT RO_ID  FROM TT_AS_RO_REPAIR_PART WHERE RO_ID IN (SELECT TMP.RO_ID");
		sql.append("  FROM (SELECT RO_ID, MAX(TT.PAY_TYPE)  FROM TT_AS_RO_REPAIR_PART TT WHERE PAY_TYPE = 11801001  GROUP BY TT.RO_ID) TMP))");
		sql.append("  GROUP BY R.CAM_CODE)  B  WHERE Y.ACTIVITY_CODE=A.CAM_CODE(+) AND Y.ACTIVITY_CODE=B.CAM_CODE(+) ");
		if (subjectid!=null&&!("").equals(subjectid)){
			sql.append(" AND Y.SUBJECT_ID = "+subjectid+"  AND Y.IS_DEL=0 \n");
			}
		List list = dao.select(TtAsActivityBean.class, sql.toString(), params);
        return list;
	}
	
	/**
	 * Function         : 服务活动管理---服务活动评估总结上报
	 * @return          : 满足条件的服务活动管理信息
	 * @throws          : 
	 * LastUpdate       : 2010-06-13
	 */
	public void serviceActivitySummaryOption(TtAsActivityEvaluatePO evaluatePOContent){
		
		dao.insert(evaluatePOContent);
	}
	/**
	 * Function         : 服务活动管理---服务活动评估总结上报更新状态
	 * @return          : 满足条件的服务活动管理信息
	 * @throws          : 
	 * LastUpdate       : 2010-06-13
	 */
	public void serviceActivitySummaryOptionStatus(TtAsActivityDealerPO DealerPO,TtAsActivityDealerPO DealerPOCon){
		dao.update(DealerPO,DealerPOCon);
	}
}