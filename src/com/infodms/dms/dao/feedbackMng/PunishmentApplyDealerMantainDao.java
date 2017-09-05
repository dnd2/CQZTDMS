/**********************************************************************
* <pre>
* FILE : PunishmentApplyDealerMantainDao.java
* CLASS : PunishmentApplyDealerMantainDao
*
* AUTHOR : PGM
*
* FUNCTION : 奖惩审批查询(经销商端).
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
 * $Id: PunishmentApplyDealerMantainDao.java,v 1.2 2010/09/29 12:29:19 zuoxj Exp $
 */
package com.infodms.dms.dao.feedbackMng;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import com.infodms.dms.bean.PunishmentApplyDealerMantainBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.common.BaseDao;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;
/**
 * Function       :  奖惩审批表查询(经销商端)
 * @author        :  PGM
 * CreateDate     :  2010-05-21
 * @version       :  0.1
 */
@SuppressWarnings("unchecked")
public class PunishmentApplyDealerMantainDao extends BaseDao{
	public static Logger logger = Logger.getLogger(PunishmentApplyMantainDao.class);
	private static final PunishmentApplyDealerMantainDao dao = new PunishmentApplyDealerMantainDao ();
	public  static final PunishmentApplyDealerMantainDao getInstance() {
		return dao;
	}

	/**
	 * Function         : 奖惩审批查询信息
	 * @param           : 工单号
	 * @param           : 服务中心名称
	 * @param           : 提报时间
	 * @param           : 类型
	 * @param           : 申请单位
	 * @param           : 当前页码
	 * @param           : 每页显示记录数
	 * @return          : 满足条件的奖惩信息，包含分页信息
	 * @throws          : Exception
	 * LastUpdate       : 2010-05-21
	 */
	public  PageResult<Map<String, Object>>  getAllPunishmentApplyDealerMantainInfo(PunishmentApplyDealerMantainBean DealerMantainBean,int curPage,int pageSize) throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append("	select IF_REWARD.ORDER_ID,IF_REWARD.LINK_MAN, DEALER.DEALER_CODE,DEALER.DEALER_NAME ,IF_REWARD.REWARD_TYPE,to_char(IF_REWARD.REWARD_DATE,'yyyy-MM-dd')as REWARD_DATE,IF_REWARD.REWARD_STATUS from TT_IF_REWARD IF_REWARD\n");
		sql.append("    left join TM_DEALER DEALER  on  IF_REWARD.DEALER_ID=DEALER.DEALER_ID   \n");
		sql.append("    left join TC_USER T on IF_REWARD.CREATE_BY=T.USER_ID \n");
		sql.append("    where IF_REWARD.IS_DEL=0 AND IF_REWARD.REWARD_STATUS in ("+Constant.AWARD_PUNISH_STATUS_REPORTED+","+Constant.AWARD_PUNISH_STATUS_SERVICE_REJECT+","+Constant.AWARD_PUNISH_STATUS_CAR_REJECT+","+Constant.AWARD_PUNISH_STATUS_SERVICE_PASS+","+Constant.AWARD_PUNISH_STATUS_CAR_PASS+")");
		if(!"".equals(DealerMantainBean.getDealerId())&&!(null==DealerMantainBean.getDealerId())){//经销商ID不为空
			sql.append("		AND "+DealerMantainBean.getDealerId()+" in (select x.dealer_id from tt_if_reward_dealer x where x.order_id = if_reward.order_id) \n");
		}
		if(!"".equals(DealerMantainBean.getOrderId())&&!(null==DealerMantainBean.getOrderId())){//工单号不为空
			sql.append("		AND UPPER(IF_REWARD.ORDER_ID) like UPPER('%"+DealerMantainBean.getOrderId()+"%')\n");
		}if(!"".equals(DealerMantainBean.getUserId())&&!(null==DealerMantainBean.getUserId())){//申请单位ID不为空
			sql.append("		AND T.USER_ID like '%"+DealerMantainBean.getUserId()+"%'\n");
		}if(!"".equals(DealerMantainBean.getBeginTime())&&!(null==DealerMantainBean.getBeginTime())){//创建时间不为空
			sql.append("		AND IF_REWARD.CREATE_DATE >=to_date('"+DealerMantainBean.getBeginTime()+"', 'yyyy-mm-dd hh24:mi:ss')  \n");
		}
		if(!"".equals(DealerMantainBean.getEndTime())&&!(null==DealerMantainBean.getEndTime())){//创建时间不为空
			sql.append("		AND IF_REWARD.CREATE_DATE  <= to_date('"+DealerMantainBean.getEndTime()+"', 'yyyy-mm-dd hh24:mi:ss') \n");
		}
		if(!"".equals(DealerMantainBean.getRewardType())&&!(null==DealerMantainBean.getRewardType())){//类型不为空
			sql.append("		AND IF_REWARD.REWARD_TYPE="+DealerMantainBean.getRewardType()+" \n");
		}if(!"".equals(DealerMantainBean.getLinkMan())&&!(null==DealerMantainBean.getLinkMan())){//申请单位不为空
			sql.append("		AND IF_REWARD.LINK_MAN like '%"+DealerMantainBean.getLinkMan()+"%'\n");
		}
		sql.append("order by IF_REWARD.CREATE_DATE desc  \n");
		PageResult<Map<String, Object>> ps = (PageResult<Map<String, Object>>) pageQuery(sql.toString(), null, "com.infodms.dms.dao.feedbackMng.PunishmentApplyCarMantainDao.getAllpunishmentApplyCarMantainInfo", pageSize, curPage);
		return ps;
	}
	/**
	 * Function       :  奖惩审批信息
	 * @param         :  
	 * @return        :  奖惩审批信息
	 * @throws        :  
	 * LastUpdate     :  2010-05-21
	 */
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}
	/**
	 * Function         : 奖惩审批查询信息
	 * @param           : 工单号
	 * @return          : 根据工单号查询出具体 奖惩信息
	 * @throws          : Exception
	 * LastUpdate       : 2010-05-21
	 */
	public  PunishmentApplyDealerMantainBean  getOrderIdInfo(String orderId){
		StringBuilder sql = new StringBuilder();
		sql.append("  select IF_REWARD.ORDER_ID, DEALER.DEALER_CODE,DEALER.DEALER_NAME ,IF_REWARD.REWARD_TYPE,IF_REWARD.LINK_MAN,IF_REWARD.TEL,IF_REWARD.REWARD_MODE,IF_REWARD.REWARD_MONEY,to_char(IF_REWARD.REWARD_DATE,'yyyy-MM-dd')as REWARD_DATE,IF_REWARD.REWARD_CONTENT,IF_REWARD.REWARD_STATUS  \n");
		sql.append("  from TT_IF_REWARD IF_REWARD ");
		sql.append("  left join TM_DEALER DEALER  on  IF_REWARD.DEALER_ID=DEALER.DEALER_ID");
		sql.append("  left join TC_USER T on IF_REWARD.CREATE_BY=T.USER_ID ");
		sql.append("  where  1=1 ");
		if (orderId!=null&&!("").equals(orderId)){
		sql.append(" AND IF_REWARD.ORDER_ID like '%");
		sql.append(orderId);
		sql.append("%'");
		}
		PunishmentApplyDealerMantainBean DealerMantainBean = new PunishmentApplyDealerMantainBean();
		PageResult<PunishmentApplyDealerMantainBean> rs = pageQuery(PunishmentApplyDealerMantainBean.class,sql.toString(), null, 10, 1);
		List<PunishmentApplyDealerMantainBean> ls = rs.getRecords();
		if (ls!=null){
			if (ls.size()>0) {
				DealerMantainBean = ls.get(0);
			}
		}
		
		return DealerMantainBean;
	}
	/**
	 * Function       :  查询奖惩详细信息 、查询超链接奖惩详细信息/审核页面详细信息(审批明细信息)
	 * @param         :  request-工单号
	 * @return        :  奖惩审批信息
	 * @throws        :  Exception
	 * LastUpdate     :  2010-05-21
	 */
	public  List<PunishmentApplyDealerMantainBean>  getOrderIdInfoList(String orderId){
		StringBuilder sql = new StringBuilder();
		sql.append("  select REWARD_AUDIT.ORDER_ID,to_char(REWARD_AUDIT.AUDIT_DATE,'yyyy-MM-dd')as AUDIT_DATE,REWARD_AUDIT.AUDIT_BY,REWARD_AUDIT.AUDIT_STATUS,REWARD_AUDIT.AUDIT_CONTENT, ORG.ORG_NAME,T.NAME   \n");
		sql.append("  from TT_IF_REWARD_AUDIT REWARD_AUDIT ");
		sql.append("  left join TM_ORG ORG on REWARD_AUDIT.ORG_ID = ORG.ORG_ID ");
		sql.append("  left join TC_USER T on REWARD_AUDIT.AUDIT_BY = T.USER_ID  ");
		if (orderId!=null&&!("").equals(orderId)){
		sql.append("  where REWARD_AUDIT.ORDER_ID = '");
		sql.append(orderId);
		sql.append("'");
		sql.append("order by REWARD_AUDIT.AUDIT_DATE desc ");
		}
		PageResult<PunishmentApplyDealerMantainBean> rs = pageQuery(PunishmentApplyDealerMantainBean.class,sql.toString(), null, 10, 1);
		List<PunishmentApplyDealerMantainBean> list = rs.getRecords();
		return list;
	}
}