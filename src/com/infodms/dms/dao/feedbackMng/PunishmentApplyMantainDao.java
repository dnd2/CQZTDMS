/**********************************************************************
* <pre>
* FILE : PunishmentApplyMantainDao.java
* CLASS : PunishmentApplyMantainDao
*
* AUTHOR : PGM
*
* FUNCTION : 奖惩审批表维护.
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
 * $Id: PunishmentApplyMantainDao.java,v 1.2 2010/09/29 13:08:05 lis Exp $
 */
package com.infodms.dms.dao.feedbackMng;

import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.PunishmentApplyMantainBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.po.TtIfRewardAuditPO;
import com.infodms.dms.po.TtIfRewardDealerPO;
import com.infodms.dms.po.TtIfRewardPO;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;
/**
 * Function       :  奖惩审批表维护
 * @author        :  PGM
 * CreateDate     :  2010-05-17
 * @version       :  0.1
 */
@SuppressWarnings("unchecked")
public class PunishmentApplyMantainDao extends BaseDao{
	public static Logger logger = Logger.getLogger(PunishmentApplyMantainDao.class);
	private static final PunishmentApplyMantainDao dao = new PunishmentApplyMantainDao ();
	public  static final PunishmentApplyMantainDao getInstance() {
		return dao;
	}

	/**
	 * Function         : 奖惩审批信息
	 * @param           : 工单号
	 * @param           : 服务中心名称
	 * @param           : 创建时间
	 * @param           : 类型
	 * @param           : 当前页码
	 * @param           : 每页显示记录数
	 * @return          : 满足条件的奖惩信息，包含分页信息
	 * @throws          : Exception
	 * LastUpdate       : 2010-05-17
	 */
	public  PageResult<Map<String, Object>>  getAllPunishmentApplyInfo(PunishmentApplyMantainBean MantainBean, int curPage,int pageSize) throws Exception {
		StringBuffer sql = new StringBuffer();
		List<Object> params = new LinkedList<Object>();
		sql.append("	select IF_REWARD.ORDER_ID,IF_REWARD.LINK_MAN, DEALER.DEALER_CODE,DEALER.DEALER_NAME ,IF_REWARD.REWARD_TYPE,to_char(IF_REWARD.CREATE_DATE,'yyyy-MM-dd') as CREATE_DATE,IF_REWARD.REWARD_STATUS from TT_IF_REWARD IF_REWARD  \n");
		sql.append("    left join TM_DEALER DEALER  on  IF_REWARD.DEALER_ID=DEALER.DEALER_ID   \n");
		sql.append("    left join TC_USER T on IF_REWARD.CREATE_BY=T.USER_ID \n");
		sql.append("    where IF_REWARD.IS_DEL=0 AND  IF_REWARD.REWARD_STATUS in ("+Constant.AWARD_PUNISH_STATUS_UNREPORT+","+Constant.AWARD_PUNISH_STATUS_SERVICE_REJECT+","+Constant.AWARD_PUNISH_STATUS_CAR_REJECT+")");
		if(!"".equals(MantainBean.getOrderId())&&!(null==MantainBean.getOrderId())){//工单号不为空
			sql.append("		AND UPPER(IF_REWARD.ORDER_ID) like UPPER('%"+MantainBean.getOrderId()+"%')\n");
		}if(Utility.testString(MantainBean.getDealerCode())){//经销商代码
			sql.append(Utility.getConSqlByParamForEqual(MantainBean.getDealerCode(), params, "DEALER", "dealer_code")); 
		}if(!"".equals(MantainBean.getDealerName())&&!(null==MantainBean.getDealerName())){//经销商名称不为空
			sql.append("		AND DEALER.DEALER_NAME like '%"+MantainBean.getDealerName()+"%'\n");
		}if(!"".equals(MantainBean.getBeginTime())&&!(null==MantainBean.getBeginTime())){//创建开始时间不为空
			sql.append("		AND IF_REWARD.CREATE_DATE >=to_date('"+MantainBean.getBeginTime()+"', 'yyyy-MM-dd HH24:mi:ss') \n");
		}
		if(!"".equals(MantainBean.getEndTime())&&!(null==MantainBean.getEndTime())){//创建结束时间不为空
			sql.append("	    AND IF_REWARD.CREATE_DATE  <= to_date('"+MantainBean.getEndTime()+"', 'yyyy-MM-dd HH24:mi:ss') \n");
		}
		if(!"".equals(MantainBean.getRewardType())&&!(null==MantainBean.getRewardType())){//类型不为空
			sql.append("		AND IF_REWARD.REWARD_TYPE="+MantainBean.getRewardType()+" \n");
		}
		sql.append("order by IF_REWARD.CREATE_DATE desc  \n");
		PageResult<Map<String, Object>> ps = (PageResult<Map<String, Object>>) pageQuery(sql.toString(), params,  getFunName(), pageSize, curPage);
		return ps;
	}
	/**
	 * Function       :  奖惩审批信息
	 * @param         :  
	 * @return        :  奖惩审批信息
	 * @throws        :  
	 * LastUpdate     :  2010-05-17
	 */
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		return null;
	}
	/**
	 * Function       :  由服务大区增加奖惩审批信息
	 * @param         :  request-工单号、服务中心名称、类型、申请单位、联系电话、奖励方式（处罚方式）、奖励金额（处罚金额）、申请日期、申请内容
	 * @return        :  奖惩审批信息
	 * @throws        :  ParseException
	 * LastUpdate     :  2010-05-17
	 */
	public static void punishmentApplyMantainAdd(TtIfRewardPO RewardPO) {
		dao.insert(RewardPO);
    }
	/**
	 * Function       :  由服务大区增加奖惩供应商对应表
	 * @param         :  request-工单号、供应商Id
	 * @return        :  奖惩审批信息
	 * @throws        :  ParseException
	 * LastUpdate     :  2010-08-11
	 */
	public static void punishmentApplyDealerAdd(TtIfRewardDealerPO RewardDealerPO) {
		String activityId =SequenceManager.getSequence(""); 
		if(null!=activityId&&!"".equals(activityId))
			RewardDealerPO.setId(Long.parseLong(activityId));
			dao.insert(RewardDealerPO);
    }
	/**
	 * Function       :  由服务大区更改奖惩供应商对应表
	 * @param         :  request-工单号、list
	 * @return        :  奖惩审批信息
	 * @throws        :  ParseException
	 * LastUpdate     :  2010-08-12
	 */
	public static void punishmentApplyDealerUpdate(String orderId,List<TtIfRewardDealerPO> list) {
		StringBuffer s = new StringBuffer("");
		s.append("delete from tt_if_reward_dealer t");
		if(null!=orderId&&!"".equals(orderId)){
		s.append("	where  t.order_id = '");
		s.append(orderId);
		s.append("'");
		}
		dao.delete(s.toString(),null);
		if(null!=list&&list.size()!=0){
			for(TtIfRewardDealerPO PO:list){
				String activityId =SequenceManager.getSequence(""); 
				if(null!=activityId&&!"".equals(activityId))
				PO.setId(Long.parseLong(activityId));
				dao.insert(PO);
			}
		}
    }
	/**
	 * Function         : 奖惩审批信息
	 * @param           : 工单号
	 * @return          : 根据工单号查询出具体 奖惩信息
	 * @throws          : Exception
	 * LastUpdate       : 2010-09-12
	 */
	public  PunishmentApplyMantainBean  getOrderIdInfo(String orderId){
		StringBuilder sql = new StringBuilder();
		sql.append("  select IF_REWARD.ORDER_ID, DEALER.DEALER_CODE,DEALER.DEALER_NAME ,IF_REWARD.REWARD_TYPE,IF_REWARD.LINK_MAN,IF_REWARD.TEL,IF_REWARD.REWARD_MODE,IF_REWARD.REWARD_MONEY,to_char(IF_REWARD.REWARD_DATE,'yyyy-MM-dd') as REWARD_DATE,IF_REWARD.REWARD_CONTENT,IF_REWARD.REWARD_STATUS   \n");
		sql.append("  from TT_IF_REWARD IF_REWARD ");
		sql.append("  left join TM_DEALER DEALER  on  IF_REWARD.DEALER_ID=DEALER.DEALER_ID");
		sql.append("  left join TC_USER T on IF_REWARD.CREATE_BY=T.USER_ID ");
		sql.append("  where  1=1 ");
		if (orderId!=null&&!("").equals(orderId)){
		sql.append(" AND IF_REWARD.ORDER_ID like '%");
		sql.append(orderId);
		sql.append("%'");
		}
		PunishmentApplyMantainBean MantainBean = new PunishmentApplyMantainBean();
		PageResult<PunishmentApplyMantainBean> rs = pageQuery(PunishmentApplyMantainBean.class,sql.toString(), null, 10, 1);
		List<PunishmentApplyMantainBean> ls = rs.getRecords();
		if (ls!=null){
			if (ls.size()>0) {
				MantainBean = ls.get(0);
			}
		}
		
		return MantainBean;
	}
	/**
	 * Function       :  奖惩审批---查询超链接奖惩详细信息/审核页面详细信息(审批明细信息)
	 * @param         :  request-工单号
	 * @return        :  奖惩审批信息
	 * @throws        :  Exception
	 * LastUpdate     :  2010-05-19
	 */
	public  List<PunishmentApplyMantainBean>  getOrderIdInfoList(String orderId){
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
		PageResult<PunishmentApplyMantainBean> rs = pageQuery(PunishmentApplyMantainBean.class,sql.toString(), null, 10, 1);
		List<PunishmentApplyMantainBean> list = rs.getRecords();
		return list;
	}
	/**
	 * Function       :  修改查询奖惩详细信息
	 * @param         :  request-工单号
	 * @return        :  奖惩审批信息/审批历史记录
	 * @throws        :  Exception
	 * LastUpdate     :  2010-05-17
	 */
	public  PunishmentApplyMantainBean  punishmentApplyMantainUpdateInit(String orderId){
		StringBuilder sql = new StringBuilder();
		sql.append("  select IF_REWARD.ORDER_ID,DEALER.DEALER_ID ,DEALER.DEALER_CODE,DEALER.DEALER_NAME ,IF_REWARD.REWARD_TYPE,IF_REWARD.LINK_MAN,IF_REWARD.TEL,IF_REWARD.REWARD_MODE,IF_REWARD.REWARD_MONEY,to_char(IF_REWARD.REWARD_DATE,'yyyy-MM-dd') as REWARD_DATE,IF_REWARD.REWARD_CONTENT,IF_REWARD.REWARD_STATUS,REWARD_AUDIT.AUDIT_DATE,REWARD_AUDIT.AUDIT_BY,REWARD_AUDIT.AUDIT_STATUS,REWARD_AUDIT.AUDIT_CONTENT   \n");
		sql.append("  from TT_IF_REWARD IF_REWARD ");
		sql.append("  left join TT_IF_REWARD_AUDIT REWARD_AUDIT on IF_REWARD.ORDER_ID=REWARD_AUDIT.ORDER_ID ");
		sql.append("  left join TM_DEALER DEALER  on  IF_REWARD.DEALER_ID=DEALER.DEALER_ID");
		sql.append("  left join TC_USER T on REWARD_AUDIT.AUDIT_BY=T.USER_ID ");
		sql.append("  where  1=1 ");
		if (orderId!=null&&!("").equals(orderId)){
		sql.append(" AND IF_REWARD.ORDER_ID like '%");
		sql.append(orderId);
		sql.append("%'");
		}
		PunishmentApplyMantainBean MantainBean = new PunishmentApplyMantainBean();
		PageResult<PunishmentApplyMantainBean> rs = pageQuery(PunishmentApplyMantainBean.class,sql.toString(), null, 10, 1);
		List<PunishmentApplyMantainBean> ls = rs.getRecords();
		if (ls!=null){
			if (ls.size()>0) {
				MantainBean = ls.get(0);
			}
		}
		
		return MantainBean;
	}
	/**
	 * Function       :  修改奖惩细信息
	 * @param         :  request-工单号、服务中心名称、类型、申请单位、联系电话、奖励方式（处罚方式）、奖励金额（处罚金额）、申请日期、申请内容
	 * @return        :  奖惩审批信息
	 * @throws        :  Exception
	 * LastUpdate     :  2010-05-17
	 */
	public static void  punishmentApplyMantainUpdate(TtIfRewardPO RewardPOCon,TtIfRewardPO RewardPO){
		dao.update(RewardPOCon, RewardPO);
	}
	
	/**
	 * Function       :  删除奖惩细信息
	 * @param         :  request-工单号
	 * @return        :  奖惩审批信息
	 * @throws        :  ParseException
	 * LastUpdate     :  2010-05-17
	 */
	public static void  punishmentApplyMantainDelete(String[] orderIds,TtIfRewardPO RewardPO){
		for (int i = 0;i<orderIds.length;i++) {
			TtIfRewardPO  RewardPOCon =new TtIfRewardPO();//orderId条件
			RewardPOCon.setOrderId(orderIds[i]);
			TtIfRewardPO  RewardPOContent  =new TtIfRewardPO();//orderId条件
			RewardPOContent.setIsDel(RewardPO.getIsDel());
			RewardPOContent.setUpdateBy(RewardPO.getUpdateBy());
			RewardPOContent.setUpdateDate(RewardPO.getUpdateDate());
			dao.update(RewardPOCon, RewardPOContent);
		}
	}
	/**
	 * Function       :  删除奖惩经销商对应信息
	 * @param         :  request-工单号数组
	 * @return        :  奖惩审批信息
	 * @throws        :  ParseException
	 * LastUpdate     :  2010-08-12
	 */
	public static void  punishmentApplyDealerDelete(String[] orderIds){
		StringBuffer s = new StringBuffer("");
		s.append("delete from tt_if_reward_dealer t");
		for (int i = 0;i<orderIds.length;i++) {
			s.append(" where t.order_id = '");
			if(null!=orderIds[i]&&!"".equals(orderIds[i])){
				s.append(orderIds[i]);
				s.append("'");
				dao.update(s.toString(),null);
			}
		}
	}
	/**
	 * Function       :  上报奖惩细信息，同时将上报信息维护到奖惩审批明细表中
	 * @param         :  request-工单号
	 * @return        :  奖惩审批信息
	 * @throws        :  ParseException
	 * LastUpdate     :  2010-05-17
	 */
	public static void  punishmentApplyMantainReport(String[] orderIds,TtIfRewardPO RewardPO ,TtIfRewardAuditPO RewardAuditPO){
		for (int i = 0;i<orderIds.length;i++) {
			TtIfRewardPO  RewardPOCon =new TtIfRewardPO();//orderId条件
			RewardPOCon.setOrderId(orderIds[i]);
			TtIfRewardPO  RewardPOContent  =new TtIfRewardPO();//内容
			RewardPOContent.setRewardStatus(Constant.AWARD_PUNISH_STATUS_REPORTED);//工单状态 ：10151002， 表示：待上报
			RewardPOContent.setUpdateBy(RewardPO.getUpdateBy());//修改人
			RewardPOContent.setUpdateDate(RewardPO.getUpdateDate());//修改时间
			dao.update(RewardPOCon, RewardPOContent);
			
			TtIfRewardAuditPO  RewardAudit =new TtIfRewardAuditPO();//内容
			RewardAudit.setOrderId(orderIds[i]);//orderId
			RewardAudit.setAuditBy(RewardAuditPO.getAuditBy());//审核人
			RewardAudit.setAuditDate(RewardAuditPO.getAuditDate());//审核时间
			RewardAudit.setId(Long.parseLong(SequenceManager.getSequence("")));//ID
			RewardAudit.setAuditStatus(RewardAuditPO.getAuditStatus());//审核状态
			RewardAudit.setOrgId(RewardAuditPO.getOrgId());//组织ID
			dao.insert(RewardAudit);
		}
	}
	/**
	 * Function       :  奖惩审批---经销商名称信息
	 * @param         :  request-工单号
	 * @return        :  奖惩审批信息
	 * @throws        :  Exception
	 * LastUpdate     :  2010-08-12
	 */
	public  List<PunishmentApplyMantainBean>  getDealerName(String orderId){
		StringBuilder sql = new StringBuilder();
		sql.append(" select d.dealer_id,d.dealer_code,d.dealer_name  \n");
		sql.append("   from tt_if_reward_dealer t, tm_dealer d ");
		sql.append("  where t.dealer_id=d.dealer_id ");
		if (orderId!=null&&!("").equals(orderId)){
		sql.append(" and t.order_id = '");
		sql.append(orderId);
		sql.append("'");
		sql.append(" order by t.dealer_id desc ");
		}
		String s = sql.toString();
		PageResult<PunishmentApplyMantainBean> rs = pageQuery(PunishmentApplyMantainBean.class,sql.toString(), null, 10, 1);
		List<PunishmentApplyMantainBean> list = rs.getRecords();
		return list;
	}
}