/**********************************************************************
* <pre>
* FILE : ServiceActivityManageTotalIssuedDao.java
* CLASS : ServiceActivityManageTotalIssuedDao
*
* AUTHOR : PGM
*
* FUNCTION :  服务活动管理---服务活动计划下发
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
 * $Id: ServiceActivityManageTotalIssuedDao.java,v 1.1 2010/08/16 01:42:18 yuch Exp $
 */
package com.infodms.dms.dao.claim.serviceActivity;
import java.sql.ResultSet;
import java.text.ParseException;
import java.util.List;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.TtAsActivityBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.po.TmDealerPO;
import com.infodms.dms.po.TtAsActivityDownlogPO;
import com.infodms.dms.po.TtAsActivityPO;
import com.infoservice.po3.bean.PO;
/**
 * Function       :  服务活动管理---服务活动计划全量下发
 * @author        :  PGM
 * CreateDate     :  2010-06-10
 * @version       :  0.1
 */
@SuppressWarnings("unchecked")
public class ServiceActivityManageTotalIssuedDao extends BaseDao{
	public static Logger logger = Logger.getLogger(ServiceActivityManageTotalIssuedDao.class);
	private static final ServiceActivityManageTotalIssuedDao dao = new ServiceActivityManageTotalIssuedDao ();
	public  static final ServiceActivityManageTotalIssuedDao getInstance() {
		return dao;
	}
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}
	/**
	 * Function         : 服务活动管理---查询活动编码
	 * @return          : 满足条件的服务活动管理信息[活动编码]
	 * @throws          : 
	 * LastUpdate       : 2010-06-10
	 */
	public List serviceActivityCode(){
		StringBuffer sql = new StringBuffer();
		sql.append("select * from tt_as_activity where STATUS="+Constant.SERVICEACTIVITY_STATUS_02+" and IS_DEL="+Constant.IS_DEL_00+" \n");//已经发布
		sql.append("order by activity_code desc  \n");
        List list = dao.select(TtAsActivityPO.class, sql.toString(), null);
        return list;
	}
	/**
	 * Function       :  全量下发
	 * @param         :  request-activityId,dealerIds
	 * @return        :  服务活动管理--全量下发
	 * @throws        :  Exception
	 * LastUpdate     :  2010-07-22
	 */
	public  List<TtAsActivityDownlogPO> TotalIssued(String activityId,String dealerIds) throws   ParseException {
		StringBuffer sql = new StringBuffer();
		List list = null;
			sql.append("select * from TT_AS_ACTIVITY_DOWNLOG where  ACTIVITY_ID="+activityId+" and DEALER_ID ="+dealerIds+" \n");//已经发布
			list=dao.select(TtAsActivityDownlogPO.class, sql.toString(), null);
		return list;
    }
	/**
	 * Function       :  查询经销商代码、名称
	 * @param         :  request-dealerIds
	 * @return        :  服务活动管理--全量下发
	 * @throws        :  Exception
	 * LastUpdate     :  2010-07-22
	 */
	public  TmDealerPO queryDealersCodeOrName(String dealerIds) throws   ParseException {
		StringBuffer sql = new StringBuffer();
		List list = null;
		sql.append("select * from TM_DEALER where  DEALER_ID="+dealerIds+" \n");
		list=dao.select(TmDealerPO.class, sql.toString(), null);
		TmDealerPO  dealerPO = new TmDealerPO();
		if (list!=null){
			if (list.size()>0) {
				dealerPO = (TmDealerPO) list.get(0);
			}
		}
		return dealerPO;
    }
}