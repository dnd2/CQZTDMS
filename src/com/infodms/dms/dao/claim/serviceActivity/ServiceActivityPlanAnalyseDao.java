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
 * $Id: ServiceActivityPlanAnalyseDao.java,v 1.2 2010/11/25 13:34:11 yangheng Exp $
 */
package com.infodms.dms.dao.claim.serviceActivity;
import java.sql.ResultSet;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import com.infodms.dms.bean.TtAsActivityBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.po.TtAsActivityPO;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;
/**
 * Function       :  服务活动管理---服务活动计划分析
 * @author        :  PGM
 * CreateDate     :  2010-06-13
 * @version       :  0.1
 */
@SuppressWarnings("unchecked")
public class ServiceActivityPlanAnalyseDao extends BaseDao{
	public static Logger logger = Logger.getLogger(ServiceActivityPlanAnalyseDao.class);
	private static final ServiceActivityPlanAnalyseDao dao = new ServiceActivityPlanAnalyseDao ();
	public  static final ServiceActivityPlanAnalyseDao getInstance() {
		return dao;
	}

	/**
	 * Function         : 服务活动管理---服务活动计划分析
	 * @param           : 活动编号
	 * @param           : 活动开始日期
	 * @param           : 活动结束日期
	 * @param           : 当前页码
	 * @param           : 每页显示记录数
	 * @return          : 满足条件的服务活动管理信息，包含分页信息
	 * @throws          : Exception
	 * LastUpdate       : 服务活动管理
	 */
	public  PageResult<Map<String, Object>>  getServiceActivityPlanAnalyseQuery(TtAsActivityBean ActivityBean,String whereSql,List<Object> params, int curPage,int pageSize) throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append("	  SELECT NVL(a.dealer_code, '---') AS DEALER_CODE,  NVL(a.dealer_shortname, '---') AS DEALER_NAME, NVL(b.num, 0) AS bn, NVL(c.num, 0) AS cn, NVL(b.num, 0) - NVL(c.num, 0) AS en,DECODE(NVL(c.num, 0), 0, 0, NVL(c.num, 0) / NVL(b.num, 0) * 100 ) as pers  \n");
		sql.append("      FROM (SELECT a.dealer_id, b.dealer_code, b.dealer_shortname   FROM  tt_as_activity_dealer a     \n");
		sql.append("      left join  tm_dealer b on a.dealer_id=b.dealer_id   left join tt_as_activity c on a.activity_id=c.activity_id where 1=1    and c.status in("+Constant.SERVICEACTIVITY_STATUS_02+","+Constant.SERVICEACTIVITY_STATUS_04+") \n");//已经发布、已经完成
		if(!"".equals(ActivityBean.getCompanyId())&&!(null==ActivityBean.getCompanyId())){//CompanyId不为空
				sql.append("		AND  c.COMPANY_ID ='"+ActivityBean.getCompanyId()+"' \n");
		}
		if(null!=ActivityBean.getActivityId()&&whereSql != null &&null!=ActivityBean.getDealerName()){//1.活动ID不为空;2.经销商编号不为空;3、经销商名称不能为空;
			sql.append("		AND   a.activity_id = "+ActivityBean.getActivityId()+" "+whereSql+"  AND a.dealer_name like '%"+ActivityBean.getDealerName()+"%' ) a \n");
		}
		
		else if(null!=ActivityBean.getActivityId()&&whereSql != null&&null==ActivityBean.getDealerName()){//1.活动ID不为空;2.经销商编号为空;3、经销商名称为空;
			sql.append("		AND  a.activity_id  =  '"+ActivityBean.getActivityId()+"' "+whereSql+"   ) a \n");
		}
		else if(null==ActivityBean.getActivityId()&&whereSql != null&&null!=ActivityBean.getDealerName()){//1.活动ID不为空;2.经销商编号不为空;3、经销商名称不能为空;
			sql.append("	 "+whereSql+" AND a.dealer_name like '%"+ActivityBean.getDealerName()+"%' ) a \n");
		}
		else if(null!=ActivityBean.getActivityId()&&null==ActivityBean.getDealerCode()&&null!=ActivityBean.getDealerName()){//1.活动ID为空;2.经销商编号为空;3、经销商名称为空;
			sql.append("	AND  a.activity_id  =  '"+ActivityBean.getActivityId()+"'	AND a.dealer_name like '%"+ActivityBean.getDealerName()+"%'  ) a \n");
		}
		else {//1.活动ID为空;2.经销商编号为空;3、经销商名称为空;
			sql.append("		) a \n");
		}
		sql.append("	        LEFT OUTER JOIN (SELECT COUNT(*) AS num, dealer_id  FROM tt_as_activity_vehicle e left join tt_as_activity c on e.activity_id=c.activity_id where 1=1 and c.COMPANY_ID ="+ActivityBean.getCompanyId()+"   and c.status in("+Constant.SERVICEACTIVITY_STATUS_02+","+Constant.SERVICEACTIVITY_STATUS_04+") \n");
		if(!"".equals(ActivityBean.getCompanyId())&&!(null==ActivityBean.getCompanyId())){//CompanyId不为空
			sql.append("		AND  c.COMPANY_ID ='"+ActivityBean.getCompanyId()+"' \n");
	    }
		if(!"".equals(ActivityBean.getActivityId())&&!(null==ActivityBean.getActivityId())&&!"0".equals(ActivityBean.getActivityId())){//活动ID不为空;其中0：表示下拉列表中的请选择
			sql.append("		AND  e.activity_id = "+ActivityBean.getActivityId()+" \n");
		}
		sql.append("		 GROUP BY dealer_id \n");
		sql.append("		 ORDER BY dealer_id ) b  ON a.dealer_id = b.dealer_id \n");
		
		sql.append("		  LEFT OUTER JOIN (SELECT COUNT(*) AS num, dealer_id FROM tt_as_activity_vehicle e left join tt_as_activity c on e.activity_id=c.activity_id  \n");
		sql.append("		  WHERE c.status in("+Constant.SERVICEACTIVITY_STATUS_02+","+Constant.SERVICEACTIVITY_STATUS_04+") and dealer_id = dealer_id  and dealer_id IS NOT NULL \n");
		if(!"".equals(ActivityBean.getCompanyId())&&!(null==ActivityBean.getCompanyId())){//CompanyId不为空
			sql.append("		AND  c.COMPANY_ID ='"+ActivityBean.getCompanyId()+"' \n");
	    }
		if(!"".equals(ActivityBean.getActivityId())&&!(null==ActivityBean.getActivityId())&&!"0".equals(ActivityBean.getActivityId())){//活动ID不为空;其中0：表示下拉列表中的请选择
			sql.append("		AND  e.activity_id = "+ActivityBean.getActivityId()+" \n");
		}
		sql.append("		 AND  car_status = '"+Constant.SERVICEACTIVITY_CAR_STATUS_02+"' \n");//11051203:已经完成
		sql.append("		 GROUP BY dealer_id \n");
		sql.append("		 ORDER BY dealer_id) c   ON a.dealer_id = c.dealer_id \n");
		
		sql.append("		 LEFT OUTER JOIN (SELECT COUNT(*) AS num,  dealer_id FROM tt_as_activity_vehicle e   left join tt_as_activity c  on e.activity_id=c.activity_id   \n");
		sql.append("		 WHERE  c.status in("+Constant.SERVICEACTIVITY_STATUS_02+","+Constant.SERVICEACTIVITY_STATUS_04+") and dealer_id <> dealer_id   AND operate_dealer_code IS NOT NULL  \n");
		if(!"".equals(ActivityBean.getCompanyId())&&!(null==ActivityBean.getCompanyId())){//CompanyId不为空
			sql.append("		AND  c.COMPANY_ID ='"+ActivityBean.getCompanyId()+"' \n");
	    }
		if(!"".equals(ActivityBean.getActivityId())&&!(null==ActivityBean.getActivityId())&&!"0".equals(ActivityBean.getActivityId())){//活动ID不为空;其中0：表示下拉列表中的请选择
			sql.append("		AND  e.activity_id = "+ActivityBean.getActivityId()+" \n");
		}
		sql.append("		AND  car_status = '"+Constant.SERVICEACTIVITY_CAR_STATUS_02+"' \n");//11051203:已经完成
		sql.append("		  GROUP BY  dealer_id \n");
		sql.append("		 ORDER BY dealer_id) d   ON a.dealer_id = d.dealer_id order by dealer_code\n");
		
		PageResult<Map<String, Object>> ps = (PageResult<Map<String, Object>>) pageQuery(sql.toString(), params,  getFunName(), pageSize, curPage);
		return ps;
	}

	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}
	/**
	 * Function         : 服务活动管理---查询活动编码，活动状态为：1068100：已经发布
	 * @return          : 满足条件的服务活动管理信息
	 * @throws          : 
	 * LastUpdate       : 2010-06-13
	 */
	public List serviceActivityCode(){
		StringBuffer sql = new StringBuffer();
		sql.append("select * from tt_as_activity where STATUS="+Constant.SERVICEACTIVITY_STATUS_02+" and IS_DEL="+Constant.IS_DEL_00+" \n");//尚未发布
		sql.append("order by activity_code desc  \n");
        List list = dao.select(TtAsActivityPO.class, sql.toString(), null);
        return list;
	}
	
   public PageResult<Map<String, Object>> serviceActivityName(String activityCode,String activity_name,int curPage){		
		StringBuffer sql = new StringBuffer();
		sql.append("select * from tt_as_activity where STATUS="+Constant.SERVICEACTIVITY_STATUS_02+" and IS_DEL="+Constant.IS_DEL_00+" \n");//尚未发布
		if( null != activityCode){
		  sql.append(" AND ACTIVITY_CODE ='"+activityCode+"'");
		}
		if(null != activity_name){
		  sql.append(" AND ACTIVITY_NAME ='"+activity_name+"'");
		}
		sql.append("order by activity_code desc  \n");
        PageResult<Map<String, Object>> ps = (PageResult<Map<String, Object>>) pageQuery(sql.toString(), null,  getFunName(),Constant.PAGE_SIZE,curPage);
        return ps;
	}
}