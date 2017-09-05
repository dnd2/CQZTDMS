/**********************************************************************
* <pre>
* FILE : ServiceActivityVehicleSituationDao.java
* CLASS : ServiceActivityVehicleSituationDao
*
* AUTHOR : PGM
*
* FUNCTION :  服务活动管理---服务活动及车辆情况查询
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
 * $Id: ServiceActivityVehicleSituationDao.java,v 1.2 2010/10/15 02:05:51 zuoxj Exp $
 */
package com.infodms.dms.dao.claim.serviceActivity;
import java.sql.ResultSet;
import java.util.Map;
import org.apache.log4j.Logger;
import com.infodms.dms.bean.TtAsActivityBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.util.StringUtil;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;
/**
 * Function       :  服务活动管理---服务活动及车辆情况查询
 * @author        :  PGM
 * CreateDate     :  2010-06-01
 * @version       :  0.1
 */
@SuppressWarnings("unchecked")
public class ServiceActivityVehicleSituationDao extends BaseDao{
	public static Logger logger = Logger.getLogger(ServiceActivityVehicleSituationDao.class);
	private static final ServiceActivityVehicleSituationDao dao = new ServiceActivityVehicleSituationDao ();
	public  static final ServiceActivityVehicleSituationDao getInstance() {
		return dao;
	}

	/**
	 * Function         : 服务活动管理---服务活动及车辆情况查询
	 * @param           : 活动编号
	 * @param           : 活动开始日期
	 * @param           : 活动结束日期
	 * @param           : 当前页码
	 * @param           : 每页显示记录数
	 * @return          : 满足条件的服务活动管理信息，包含分页信息
	 * @throws          : Exception
	 * LastUpdate       : 服务活动管理
	 */
	public  PageResult<Map<String, Object>>  getServiceActivityVechileSituationQuery(TtAsActivityBean ActivityBean, int curPage,int pageSize) throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append("	SELECT a.ACTIVITY_ID,  a.ACTIVITY_CODE,  a.ACTIVITY_NAME,  a.STATUS,to_char(a.STARTDATE, 'yyyy-MM-dd') as STARTDATE,to_char(a.ENDDATE, 'yyyy-MM-dd') as ENDDATE,  DECODE(a.CAR_NUM ,'','---',a.CAR_NUM,a.CAR_NUM) as CAR_NUM,       \n");
		sql.append("    (SELECT COUNT(*) AS NUM  FROM TT_AS_ACTIVITY_VEHICLE WHERE  CAR_STATUS = '"+Constant.SERVICEACTIVITY_CAR_STATUS_02+"' AND EXISTS (SELECT ACTIVITY_ID  FROM TT_AS_ACTIVITY_VEHICLE  GROUP BY ACTIVITY_ID)  AND ACTIVITY_ID = a.activity_id  )as NUM  \n");
		sql.append("     FROM TT_AS_ACTIVITY a   \n");
		sql.append("    WHERE 1 = 1  and a.STATUS in( "+Constant.SERVICEACTIVITY_STATUS_02+","+Constant.SERVICEACTIVITY_STATUS_04+") \n");//服务活动状态：已经发布、已经结束
		if(!"".equals(ActivityBean.getCompanyId())&&!(null==ActivityBean.getCompanyId())){//CompanyId不为空
			sql.append("		AND  a.COMPANY_ID ='"+ActivityBean.getCompanyId()+"' \n");
		}
		if(!"".equals(ActivityBean.getActivityCode())&&!(null==ActivityBean.getActivityCode())){//活动编号不为空
			sql.append("		AND UPPER(ACTIVITY_CODE) like UPPER('%"+ActivityBean.getActivityCode()+"%')\n");
		}
		if(StringUtil.notNull(ActivityBean.getActivityName())){
			sql.append("        AND ACTIVITY_NAME LIKE '%").append(ActivityBean.getActivityName()).append("%'\n");
		}
		if(!"".equals(ActivityBean.getStatus())&&!(null==ActivityBean.getStatus())){//活动状态不为空
			sql.append("		AND STATUS = '"+ActivityBean.getStatus()+"' \n");
		}
		if(!"".equals(ActivityBean.getStartdate())&&!(null==ActivityBean.getStartdate())){      //活动开始日期不为空
			sql.append("		AND STARTDATE >=to_date('"+ActivityBean.getStartdate()+" 00:00:00', 'yyyy-MM-dd HH24:mi:ss') \n");
		}
		if(!"".equals(ActivityBean.getEnddate())&&!(null==ActivityBean.getEnddate())){         //活动结束日期不为空
			sql.append("	    AND ENDDATE  <= to_date('"+ActivityBean.getEnddate()+" 23:59:59', 'yyyy-MM-dd HH24:mi:ss') \n");
		}
		sql.append("            ORDER BY trim(a.ACTIVITY_ID) desc  \n");
		PageResult<Map<String, Object>> ps = (PageResult<Map<String, Object>>) pageQuery(sql.toString(), null,  getFunName(), pageSize, curPage);
		return ps;
	}

	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}
}