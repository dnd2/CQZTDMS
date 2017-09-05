/**********************************************************************
* <pre>
* FILE : ServiceActivityManageCharactorDao.java
* CLASS : ServiceActivityManageCharactorDao
*
* AUTHOR : PGM
*
* FUNCTION : 服务活动管理--车辆性质列表.
*
*
*======================================================================
* CHANGE HISTORY LOG
*----------------------------------------------------------------------
* MOD. NO.| DATE     | NAME | REASON | CHANGE REQ.
*----------------------------------------------------------------------
*         |2010-06-03| PGM  | Created |
* DESCRIPTION:
* </pre>
***********************************************************************/
/**
 * $Id: ServiceActivityManageCharactorDao.java,v 1.1 2010/08/16 01:42:19 yuch Exp $
 */
package com.infodms.dms.dao.claim.serviceActivity;
import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;
import org.apache.log4j.Logger;

import com.infodms.dms.bean.TtAsActivityBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.po.TtAsActivityCharactorPO;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.po3.bean.PO;
/**
 * Function       :  服务活动管理--车龄定义列表
 * @author        :  PGM
 * CreateDate     :  2010-06-03
 * @version       :  0.1
 */
@SuppressWarnings("unchecked")
public class ServiceActivityManageCharactorDao extends BaseDao{
	public static Logger logger = Logger.getLogger(ServiceActivityManageCharactorDao.class);
	private static final ServiceActivityManageCharactorDao dao = new ServiceActivityManageCharactorDao ();
	public  static final ServiceActivityManageCharactorDao getInstance() {
		return dao;
	}

	/**
	 * Function         : 服务活动管理--车辆性质列表
	 * @param           ：服务活动管理--车辆性质状态：1072
	 * @param           : 当前页码
	 * @param           : 每页显示记录数
	 * @return          : 满足条件的服务活动管理信息---车型列表，包含分页信息
	 * @throws          : Exception
	 * LastUpdate       : 服务活动管理---车辆性质列表
	 */
	public  List<TtAsActivityBean>  getAllServiceActivityManageCharactorInfo(String activityId){
		List<Object> params = new LinkedList();
		StringBuilder sql = new StringBuilder();
		sql.append("  SELECT CODES.CODE_ID,CODES.CODE_DESC   \n");
		sql.append("  FROM TC_CODE CODES \n");
		sql.append("  WHERE CODES.TYPE = "+Constant.SERVICEACTIVITY_CHARACTOR);
		sql.append(" ORDER BY CODES.CODE_ID desc \n");
		List list = dao.select(TtAsActivityBean.class, sql.toString(), params);
		System.out.println(list.size());
	    return list;
	}
	/**
	 * Function         : 服务活动管理--车辆性质列表TT_AS_ACTIVITY_CHARACTOR
	 * @param           ：
	 * @param           : 当前页码
	 * @param           : 每页显示记录数
	 * @return          : 满足条件的服务活动管理信息---车型列表，包含分页信息
	 * @throws          : Exception
	 * LastUpdate       : 服务活动管理---车辆性质列表
	 */
	public  List<TtAsActivityBean>  getAllServiceActivityManageChangeCodeInfo(String activityId){
		List<Object> params = new LinkedList();
		StringBuilder sql = new StringBuilder();
		sql.append("	SELECT ID,  CREATE_DATE, UPDATE_BY,  CREATE_BY, UPDATE_DATE, ACTIVITY_ID, CAR_CHARACTOR  \n");
		sql.append("    FROM TT_AS_ACTIVITY_CHARACTOR \n");
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
	 * Function       :  由车厂端[服务营销部]增加服务活动管理信息---车辆性质列表
	 * @param         :  request-车型代码、车型名称
	 * @return        :  服务活动管理--车辆性质列表
	 * @throws        :  Exception
	 * LastUpdate     :  2010-06-03
	 */
	public static void serviceActivityManageCharactorOption(String []carCharactorArray,TtAsActivityCharactorPO CharactorPO) {
		TtAsActivityCharactorPO CPo=new TtAsActivityCharactorPO();
		CPo.setActivityId(CharactorPO.getActivityId());
		dao.delete(CPo);
		for (int i = 0;i<carCharactorArray.length;i++) {
			CharactorPO.setId(Long.parseLong(SequenceManager.getSequence("")));
			CharactorPO.setCarCharactor(Integer.parseInt(carCharactorArray[i]));
			dao.insert(CharactorPO);
		}
    }
	/**
	 * Function         : 服务活动管理--车辆性质列表TT_AS_ACTIVITY_CHARACTOR
	 * @param           ：ACTIVITY_ID
	 * @param           : 当前页码
	 * @throws          : Exception
	 * LastUpdate       : 服务活动管理---服务活动主表
	 */
	public  TtAsActivityBean  getChangeBeforeVehicle(String activityId){
		List<Object> params = new LinkedList();
		StringBuilder sql = new StringBuilder();
		sql.append("	SELECT ACTIVITY_ID,  vehicle_area  \n");
		sql.append("    FROM TT_AS_ACTIVITY \n");
		if(!"".equals(activityId)&&!(null==activityId)){//活动ID
			sql.append(" WHERE ACTIVITY_ID = ? \n");
			params.add(activityId);
		}
		sql.append("     ORDER BY ACTIVITY_ID DESC  \n");
		List list = dao.select(TtAsActivityBean.class, sql.toString(), params);
		TtAsActivityBean ActivityBean=new TtAsActivityBean();
		if (list!=null){
			if (list.size()>0) {
				ActivityBean = (TtAsActivityBean) list.get(0);
			}
		}
		return ActivityBean;
	}
	
}