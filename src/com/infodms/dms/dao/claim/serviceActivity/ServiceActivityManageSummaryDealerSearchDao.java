/**********************************************************************
* <pre>
* FILE : ServiceActivityManageSummaryDealerSearchDao.java
* CLASS : ServiceActivityManageSummaryDealerSearchDao
*
* AUTHOR : PGM
*
* FUNCTION :  服务活动管理---服务活动评估经销商查询
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
 * $Id: ServiceActivityManageSummaryDealerSearchDao.java,v 1.1 2010/08/16 01:42:18 yuch Exp $
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
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;
/**
 * Function       :  服务活动管理---服务活动评估经销商查询
 * @author        :  PGM
 * CreateDate     :  2010-06-13
 * @version       :  0.1
 */
@SuppressWarnings("unchecked")
public class ServiceActivityManageSummaryDealerSearchDao extends BaseDao{
	public static Logger logger = Logger.getLogger(ServiceActivityManageSummaryDealerSearchDao.class);
	private static final ServiceActivityManageSummaryDealerSearchDao dao = new ServiceActivityManageSummaryDealerSearchDao ();
	public  static final ServiceActivityManageSummaryDealerSearchDao getInstance() {
		return dao;
	}

	/**
	 * Function         : 服务活动管理---服务活动评估经销商查询
	 * @param           : 活动编号
	 * @param           : 活动开始日期
	 * @param           : 活动结束日期
	 * @param           : 当前页码
	 * @param           : 每页显示记录数
	 * @return          : 满足条件的服务活动管理---服务活动评估经销商信息，包含分页信息
	 * @throws          : Exception
	 * LastUpdate       : 服务活动管理
	 */
	public  PageResult<Map<String, Object>>  getServiceActivityManageSummaryDealerSearchQuery(TtAsActivityBean ActivityBean, int curPage,int pageSize) throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT B.SUBJECT_ID,B.SUBJECT_NO,B.SUBJECT_NAME,B.ACTIVITY_TYPE,B.ACTIVITY_NUM,C.DEALER_CODE,C.DEALER_NAME," +
				"TO_CHAR(B.SUBJECT_START_DATE,'YYYY-MM-DD')" +
				" AS SUBJECT_START_DATE," +
				"TO_CHAR(B.SUBJECT_END_DATE,'YYYY-MM-DD') AS SUBJECT_END_DATE FROM TT_AS_ACTIVITY_EVALUATE A," +
				"TT_AS_ACTIVITY_SUBJECT B ,TM_DEALER C WHERE A.STATUS is not null and  A.SUBJECT_ID =B.SUBJECT_ID AND A.DEALER_ID=C.DEALER_ID ");
		
			sql.append("		AND C.DEALER_ID ="+ActivityBean.getDealerId()+"\n");
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
			sql.append("		AND B.SUBJECT_START_DATE >=to_date('"+ActivityBean.getSubject_start_date()+" 00:00:00', 'yyyy-MM-dd HH24:mi:ss') \n");
		}
		if(!"".equals(ActivityBean.getSubject_end_date())&&!(null==ActivityBean.getSubject_end_date())){         //活动结束日期不为空
			sql.append("	    AND b.SUBJECT_END_DATE  <= to_date('"+ActivityBean.getSubject_end_date()+" 23:59:59', 'yyyy-MM-dd HH24:mi:ss') \n");
		}
		sql.append("            ORDER BY B.SUBJECT_ID DESC  \n");
		PageResult<Map<String, Object>> ps = (PageResult<Map<String, Object>>) pageQuery(sql.toString(), null,  getFunName(), pageSize, curPage);
		return ps;
	}

	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}
	/**
	 * Function         : 服务活动管理---服务活动评估经销商查询[明细]查询方法
	 * @return          : 满足条件的服务活动管理信息
	 * @throws          : 
	 * LastUpdate       : 2010-06-13
	 */
	public TtAsActivityEvaluatePO serviceActivitySummaryQuery(String activityId){
		List<Object> params = new LinkedList();
		StringBuffer sql = new StringBuffer();
		sql.append("	SELECT A.ACTIVITY_NAME,  B.IN_AMOUNT, B.IN_GUARANTEE, B.OUT_GUARANTEE,  B.REPAIR_AMOUNT, B.MAINTAIN_AMOUNT,  B.ON_AMOUNT,  B.ON_CAMOUNT,B.EVALUATE,  B.MEASURES	,org.org_name	\n");//已经发布
		sql.append("	FROM  tm_dealer dealer, tm_dealer_org_relation org_relation, tm_org org,TT_AS_ACTIVITY A 	\n");
		sql.append("	LEFT OUTER JOIN TT_AS_ACTIVITY_EVALUATE B ON A.ACTIVITY_ID = B.ACTIVITY_ID	\n");
		sql.append("	WHERE A.STATUS="+Constant.SERVICEACTIVITY_STATUS_02+" AND A.IS_DEL="+Constant.IS_DEL_00+" 	\n");
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