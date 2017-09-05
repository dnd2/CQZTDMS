/**********************************************************************
* <pre>
* FILE : ServiceActivityManageVehicleAgeDao.java
* CLASS : ServiceActivityManageVehicleAgeDao
*
* AUTHOR : PGM
*
* FUNCTION : 服务活动管理--车龄定义列表.
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
 * $Id: ServiceActivityManageVehicleAgeDao.java,v 1.1 2010/08/16 01:42:18 yuch Exp $
 */
package com.infodms.dms.dao.claim.serviceActivity;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import org.apache.log4j.Logger;

import com.infodms.dms.bean.TtAsActivityBean;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.po.TtAsActivityAgePO;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;
/**
 * Function       :  服务活动管理--车龄定义列表
 * @author        :  PGM
 * CreateDate     :  2010-06-02
 * @version       :  0.1
 */
@SuppressWarnings("unchecked")
public class ServiceActivityManageVehicleAgeDao extends BaseDao{
	public static Logger logger = Logger.getLogger(ServiceActivityManageVehicleAgeDao.class);
	private static final ServiceActivityManageVehicleAgeDao dao = new ServiceActivityManageVehicleAgeDao ();
	public  static final ServiceActivityManageVehicleAgeDao getInstance() {
		return dao;
	}

	/**
	 * Function         : 服务活动管理--车龄定义列表
	 * @param           ：车系
	 * @param           : 当前页码
	 * @param           : 每页显示记录数
	 * @return          : 满足条件的服务活动管理信息---车型列表，包含分页信息
	 * @throws          : Exception
	 * LastUpdate       : 服务活动管理---车型列表
	 */
	public  List<TtAsActivityBean>  getAllServiceActivityManageVehicleAgeInfo(String activityId){
		StringBuilder sql = new StringBuilder();
		sql.append("	SELECT  ID,  CREATE_DATE, UPDATE_BY,  CREATE_BY, UPDATE_DATE, ACTIVITY_ID, to_char(SALE_DATE_END,'yyyy-MM-dd')as SALE_DATE_END ,  to_char(SALE_DATE_START,'yyyy-MM-dd') as SALE_DATE_START,CUSTOMER_TYPE ,DATE_TYPE  \n");
		sql.append("    FROM  TT_AS_ACTIVITY_AGE \n");
		if(!"".equals(activityId)&&!(null==activityId)){//活动ID
			sql.append(" WHERE ACTIVITY_ID = '"+activityId+"' \n");
		}
		sql.append("     ORDER BY ACTIVITY_ID DESC  \n");
		PageResult<TtAsActivityBean> rs = pageQuery(TtAsActivityBean.class,sql.toString(), null, 10, 1);
		List<TtAsActivityBean> list = rs.getRecords();
		return list;
	}
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}
	/**
	 * Function       :  由车厂端[服务营销部]增加服务活动管理信息---车型列表
	 * @param         :  request-车型代码、车型名称
	 * @return        :  服务活动管理--车龄定义列表
	 * @throws        :  Exception
	 * LastUpdate     :  2010-06-02
	 */
	public static void serviceActivityManageVehicleAgeOption(String []startdate,String []enddate,String []dateType,TtAsActivityAgePO APO) throws   ParseException {
		TtAsActivityAgePO AgePO=new TtAsActivityAgePO();
		AgePO.setActivityId(APO.getActivityId());
		dao.delete(AgePO);
		for (int i = 0;i<startdate.length;i++) {
			APO.setId(Long.parseLong(SequenceManager.getSequence("")));
			DateFormat df=new SimpleDateFormat("yyyy-MM-dd");
			APO.setSaleDateStart(df.parse(startdate[i]));
			APO.setSaleDateEnd(df.parse(enddate[i]));
		    APO.setDateType(Long.parseLong(dateType[i]));
			dao.insert(APO);
		}
    }
}