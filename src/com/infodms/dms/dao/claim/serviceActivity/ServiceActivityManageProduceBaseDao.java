/**********************************************************************
* <pre>
* FILE : ServiceActivityManageProduceBaseDao.java
* CLASS : ServiceActivityManageProduceBaseDao
*
* AUTHOR : PGM
*
* FUNCTION : 服务活动管理--生产基地列表.
*
*
*======================================================================
* CHANGE HISTORY LOG
*----------------------------------------------------------------------
* MOD. NO.| DATE     | NAME | REASON | CHANGE REQ.
*----------------------------------------------------------------------
*         |2010-07-09| PGM  | Created |
* DESCRIPTION:
* </pre>
***********************************************************************/
/**
 * $Id: ServiceActivityManageProduceBaseDao.java,v 1.1 2010/08/16 01:42:19 yuch Exp $
 */
package com.infodms.dms.dao.claim.serviceActivity;
import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;
import org.apache.log4j.Logger;

import com.infodms.dms.bean.TtAsActivityBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.po.TtAsActivityYieldlyPO;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.po3.bean.PO;
/**
 * Function       :  服务活动管理--生产基地列表
 * @author        :  PGM
 * CreateDate     :  2010-07-09
 * @version       :  0.1
 */
@SuppressWarnings("unchecked")
public class ServiceActivityManageProduceBaseDao extends BaseDao{
	public static Logger logger = Logger.getLogger(ServiceActivityManageProduceBaseDao.class);
	private static final ServiceActivityManageProduceBaseDao dao = new ServiceActivityManageProduceBaseDao ();
	public  static final ServiceActivityManageProduceBaseDao getInstance() {
		return dao;
	}

	/**
	 * Function         : 服务活动管理--生产基地列表
	 * @param           ：服务活动管理--生产基地状态：1072
	 * @param           : 当前页码
	 * @param           : 每页显示记录数
	 * @return          : 满足条件的服务活动管理信息---生产基地列，包含分页信息
	 * @throws          : Exception
	 * LastUpdate       : 服务活动管理---生产基地列表
	 */
	public  List<TtAsActivityBean>  getAllServiceActivityManageProduceBaseInfo(String activityId){
		List<Object> params = new LinkedList();
		StringBuilder sql = new StringBuilder();
		sql.append("  SELECT CODES.CODE_ID,CODES.CODE_DESC   \n");
		sql.append("  FROM TC_CODE CODES \n");
		sql.append("  WHERE CODES.TYPE = "+Constant.SERVICEACTIVITY_CAR_YIELDLY);//生产基地代码
		sql.append(" ORDER BY CODES.CODE_ID desc \n");
		List list = dao.select(TtAsActivityBean.class, sql.toString(), params);
	    return list;
	}
	/**
	 * Function         : 服务活动管理--生产基地列表TT_AS_ACTIVITY_CHARACTOR
	 * @param           ：
	 * @param           : 当前页码
	 * @param           : 每页显示记录数
	 * @return          : 满足条件的服务活动管理信息---生产基地列表，包含分页信息
	 * @throws          : Exception
	 * LastUpdate       : 服务活动管理---生产基地列表
	 */
	public  List<TtAsActivityBean>  getAllServiceActivityManageChangeCodeInfo(String activityId){
		List<Object> params = new LinkedList();
		StringBuilder sql = new StringBuilder();
		sql.append("	SELECT ID,  CREATE_DATE, UPDATE_BY,  CREATE_BY, UPDATE_DATE, ACTIVITY_ID, car_yieldly  \n");
		sql.append("    FROM TT_AS_ACTIVITY_YIELDLY \n");
		if(!"".equals(activityId)&&!(null==activityId)){//活动ID
			sql.append(" WHERE ACTIVITY_ID = ? \n");
			params.add(activityId);
		}
		sql.append("     ORDER BY ACTIVITY_ID DESC  \n");
		List list = dao.select(TtAsActivityBean.class, sql.toString(), params);
	    return list;
	}
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}
	/**
	 * Function       :  由车厂端[服务营销部]增加服务活动管理信息---生产基地质列表
	 * @param         :  request-车型代码、车型名称
	 * @return        :  服务活动管理--生产基地列表
	 * @throws        :  Exception
	 * LastUpdate     :  2010-07-09
	 */
	public static void serviceActivityManageCarYieldlyOption(String []carYieldlyArray,TtAsActivityYieldlyPO CarYieldlyPO) {
		TtAsActivityYieldlyPO CPo=new TtAsActivityYieldlyPO();
		CPo.setActivityId(CarYieldlyPO.getActivityId());
		dao.delete(CPo);
		for (int i = 0;i<carYieldlyArray.length;i++) {
			CarYieldlyPO.setId(Long.parseLong(SequenceManager.getSequence("")));
			CarYieldlyPO.setCarYieldly(Long.parseLong(carYieldlyArray[i]));
			dao.insert(CarYieldlyPO);
		}
    }
}