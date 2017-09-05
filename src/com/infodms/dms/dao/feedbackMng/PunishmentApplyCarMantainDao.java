/**********************************************************************
* <pre>
* FILE : PunishmentApplyCarMantainDao.java
* CLASS : PunishmentApplyCarMantainDao
*
* AUTHOR : PGM
*
* FUNCTION : 奖惩审批查询（车厂端）.
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
 * $Id: PunishmentApplyCarMantainDao.java,v 1.1 2010/08/16 01:43:15 yuch Exp $
 */
package com.infodms.dms.dao.feedbackMng;

import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.PunishmentApplyCarMantainBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.dao.common.BaseDao;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;
/**
 * Function       :  奖惩审批表查询（车厂端）
 * @author        :  PGM
 * CreateDate     :  2010-05-21
 * @version       :  0.1
 */
@SuppressWarnings("unchecked")
public class PunishmentApplyCarMantainDao extends BaseDao{
	public static Logger logger = Logger.getLogger(PunishmentApplyMantainDao.class);
	private static final PunishmentApplyCarMantainDao dao = new PunishmentApplyCarMantainDao ();
	public  static final PunishmentApplyCarMantainDao getInstance() {
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
	public  PageResult<Map<String, Object>>  getAllpunishmentApplyCarMantainInfo(PunishmentApplyCarMantainBean CarMantainBean,int curPage,int pageSize) throws Exception {
		StringBuffer sql = new StringBuffer();
		List<Object> params = new LinkedList<Object>();
		sql.append("	select IF_REWARD.ORDER_ID,IF_REWARD.LINK_MAN, DEALER.DEALER_CODE,DEALER.DEALER_NAME ,IF_REWARD.REWARD_TYPE,to_char(IF_REWARD.REWARD_DATE,'yyyy-MM-dd')as REWARD_DATE,IF_REWARD.REWARD_STATUS from TT_IF_REWARD IF_REWARD  \n");
		sql.append("    left join TM_DEALER DEALER  on  IF_REWARD.DEALER_ID=DEALER.DEALER_ID   \n");
		sql.append("    left join TC_USER T on IF_REWARD.CREATE_BY=T.USER_ID \n");
		sql.append("    where IF_REWARD.IS_DEL=0 AND IF_REWARD.REWARD_STATUS in ("+Constant.AWARD_PUNISH_STATUS_REPORTED+","+Constant.AWARD_PUNISH_STATUS_SERVICE_REJECT+","+Constant.AWARD_PUNISH_STATUS_CAR_REJECT+","+Constant.AWARD_PUNISH_STATUS_SERVICE_PASS+","+Constant.AWARD_PUNISH_STATUS_CAR_PASS+")");
		if(!"".equals(CarMantainBean.getOrderId())&&!(null==CarMantainBean.getOrderId())){//工单号不为空
			sql.append("		AND UPPER(IF_REWARD.ORDER_ID) like UPPER('%"+CarMantainBean.getOrderId()+"%')\n");
		}if(Utility.testString(CarMantainBean.getDealerCode())){//经销商代码
			sql.append(Utility.getConSqlByParamForEqual(CarMantainBean.getDealerCode(), params, "DEALER", "dealer_code")); 
		}if(!"".equals(CarMantainBean.getDealerName())&&!(null==CarMantainBean.getDealerName())){//经销商名称不为空
			sql.append("		AND DEALER.DEALER_CODE = '"+CarMantainBean.getDealerName()+"'\n");
		}if(!"".equals(CarMantainBean.getBeginTime())&&!(null==CarMantainBean.getBeginTime())){//创建时间不为空
			sql.append("		AND IF_REWARD.CREATE_DATE >=to_date('"+CarMantainBean.getBeginTime()+"', 'yyyy-mm-dd hh24:mi:ss') \n");
		}
		if(!"".equals(CarMantainBean.getEndTime())&&!(null==CarMantainBean.getEndTime())){//创建时间不为空
			sql.append("		 AND IF_REWARD.CREATE_DATE  <= to_date('"+CarMantainBean.getEndTime()+"', 'yyyy-mm-dd hh24:mi:ss') \n");
		}
		if(!"".equals(CarMantainBean.getRewardType())&&!(null==CarMantainBean.getRewardType())){//类型不为空
			sql.append("		AND IF_REWARD.REWARD_TYPE="+CarMantainBean.getRewardType()+" \n");
		}if(!"".equals(CarMantainBean.getLinkMan())&&!(null==CarMantainBean.getLinkMan())){//申请单位不为空
			sql.append("		AND IF_REWARD.LINK_MAN like '%"+CarMantainBean.getLinkMan()+"%'\n");
		}if(null!=CarMantainBean.getCompanyId()&&!"".equals(CarMantainBean.getCompanyId())){
			sql.append("		AND IF_REWARD.company_id = "+CarMantainBean.getCompanyId());
		}
		sql.append("order by IF_REWARD.CREATE_DATE desc  \n");
		PageResult<Map<String, Object>> ps = (PageResult<Map<String, Object>>) pageQuery(sql.toString(), params, getFunName(), pageSize, curPage);
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
	public  PunishmentApplyCarMantainBean  getOrderIdInfo(String orderId){
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
		PunishmentApplyCarMantainBean MantainBean = new PunishmentApplyCarMantainBean();
		PageResult<PunishmentApplyCarMantainBean> rs = pageQuery(PunishmentApplyCarMantainBean.class,sql.toString(), null, 10, 1);
		List<PunishmentApplyCarMantainBean> ls = rs.getRecords();
		if (ls!=null){
			if (ls.size()>0) {
				MantainBean = ls.get(0);
			}
		}
		
		return MantainBean;
	}
	/**
	 * Function       :  查询奖惩详细信息 、查询超链接奖惩详细信息/审核页面详细信息(审批明细信息)
	 * @param         :  request-工单号
	 * @return        :  奖惩审批信息
	 * @throws        :  Exception
	 * LastUpdate     :  2010-05-21
	 */
	public  List<PunishmentApplyCarMantainBean>  getOrderIdInfoList(String orderId){
		StringBuilder sql = new StringBuilder();
		sql.append("  select REWARD_AUDIT.ORDER_ID,to_char(REWARD_AUDIT.AUDIT_DATE,'yyyy-MM-dd')as AUDIT_DATE,REWARD_AUDIT.AUDIT_BY,REWARD_AUDIT.AUDIT_STATUS,REWARD_AUDIT.AUDIT_CONTENT, ORG.ORG_NAME,T.NAME   \n");
		sql.append("  from TT_IF_REWARD_AUDIT REWARD_AUDIT ");
		sql.append("  left join TM_ORG ORG on REWARD_AUDIT.ORG_ID = ORG.ORG_ID ");
		sql.append("  left join TC_USER T on REWARD_AUDIT.AUDIT_BY = T.USER_ID  ");
		if (orderId!=null&&!("").equals(orderId)){
		sql.append("  where REWARD_AUDIT.ORDER_ID like '%");
		sql.append(orderId);
		sql.append("%'");
		sql.append("order by REWARD_AUDIT.AUDIT_DATE desc ");
		}
		PageResult<PunishmentApplyCarMantainBean> rs = pageQuery(PunishmentApplyCarMantainBean.class,sql.toString(), null, 10, 1);
		List<PunishmentApplyCarMantainBean> list = rs.getRecords();
		return list;
	}
}