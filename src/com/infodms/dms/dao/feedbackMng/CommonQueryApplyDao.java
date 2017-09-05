/**********************************************************************
* <pre>
* FILE : CommonQueryApplyDao.java
* CLASS : CommonQueryApplyDao
*
* AUTHOR : PGM
*
* FUNCTION :  奖惩审批表维护（申请单位）.
*
*
*======================================================================
* CHANGE HISTORY LOG
*----------------------------------------------------------------------
* MOD. NO.| DATE     | NAME | REASON | CHANGE REQ.
*----------------------------------------------------------------------
*         |2010-05-21| PGM  | Created |
* DESCRIPTION:
* </pre>
***********************************************************************/
/**
 * $Id: CommonQueryApplyDao.java,v 1.1 2010/08/16 01:43:15 yuch Exp $
 */
package com.infodms.dms.dao.feedbackMng;

import java.sql.ResultSet;
import java.util.Map;
import org.apache.log4j.Logger;

import com.infodms.dms.bean.CommonQueryApplyBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.common.BaseDao;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;
/**
 * Function       :  查询申请单位代码、名称
 * @author        :  PGM
 * CreateDate     :  2010-05-21
 * @version       :  0.1
 */
@SuppressWarnings("unchecked")
public class CommonQueryApplyDao extends BaseDao{
	public static Logger logger = Logger.getLogger(CommonQueryApplyDao.class);
	private static final CommonQueryApplyDao dao = new CommonQueryApplyDao ();
	public  static final CommonQueryApplyDao getInstance() {
		return dao;
	}
	/**
	 * Function       :  查询申请单位名称弹出框
	 * @param         :  request-工单号
	 * @return        :  奖惩审批信息
	 * @throws        :  Exception
	 * LastUpdate     :  2010-05-21
	 */
	public  PageResult<Map<String, Object>>  queryApplyMantain(CommonQueryApplyBean ApplyBean, int curPage,int pageSize) throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append("	select  u.user_id,u.name,u.phone from tc_user u ,tr_user_pose r,tc_pose p ,tm_org o  \n");
		sql.append("    where o.org_id=p.org_id and p.pose_id=r.pose_id and r.user_id=u.user_id and o.org_type="+Constant.SERVICE_AREA);
		if(!"".equals(ApplyBean.getUserId())&&!(null==ApplyBean.getUserId())){//用户ID不为空
			sql.append("		AND  u.user_id like '%"+ApplyBean.getUserId()+"%'\n");
		}if(!"".equals(ApplyBean.getName())&&!(null==ApplyBean.getName())){//用户名称不为空
			sql.append("		AND u.name like '%"+ApplyBean.getName()+"%'\n");
		}
		if(!"".equals(ApplyBean.getPhone())&&!(null==ApplyBean.getPhone())){//用户电话不为空
			sql.append("		AND u.phone like '%"+ApplyBean.getPhone()+"%'\n");
		}
		sql.append("order by u.user_id desc  \n");
		PageResult<Map<String, Object>> ps = (PageResult<Map<String, Object>>) pageQuery(sql.toString(), null, "com.infodms.dms.dao.feedbackMng.CommonQueryApplyDao.queryApplyMantain", pageSize, curPage);
		return ps;
	}
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}
}