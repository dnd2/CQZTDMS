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
*         |2010-05-19| PGM  | Created |
* DESCRIPTION:
* </pre>
***********************************************************************/
/**
 * $Id: PunishmentApprovalDao.java,v 1.1 2010/08/16 01:43:15 yuch Exp $
 */
package com.infodms.dms.dao.feedbackMng;

import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.PunishmentApprovalBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.po.TtIfRewardAuditPO;
import com.infodms.dms.po.TtIfRewardPO;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;
/**
 * Function       :  奖惩审批表维护
 * @author        :  PGM
 * CreateDate     :  2010-05-19
 * @version       :  0.1
 */
@SuppressWarnings("unchecked")
public class PunishmentApprovalDao extends BaseDao{
	public static Logger logger = Logger.getLogger(PunishmentApprovalDao.class);
	private static final PunishmentApprovalDao dao = new PunishmentApprovalDao ();
	public  static final PunishmentApprovalDao getInstance() {
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
	 * LastUpdate       : 2010-05-19
	 */
	public  PageResult<Map<String, Object>>  getAllpunishmentApprovalInfo(PunishmentApprovalBean ApprovalBean, int curPage,int pageSize) throws Exception {
		StringBuffer sql = new StringBuffer();
		List<Object> params = new LinkedList<Object>();
		sql.append("	select IF_REWARD.ORDER_ID,IF_REWARD.LINK_MAN, DEALER.DEALER_CODE,DEALER.DEALER_NAME ,IF_REWARD.REWARD_TYPE,to_char(IF_REWARD.REWARD_DATE,'yyyy-MM-dd')as REWARD_DATE,IF_REWARD.REWARD_STATUS from TT_IF_REWARD IF_REWARD  \n");
		sql.append("    left join TM_DEALER DEALER  on  IF_REWARD.DEALER_ID=DEALER.DEALER_ID   \n");
		sql.append("    left join TC_USER T on IF_REWARD.CREATE_BY=T.USER_ID \n");
		sql.append("    where IF_REWARD.IS_DEL=0 AND IF_REWARD.REWARD_STATUS ="+Constant.AWARD_PUNISH_STATUS_REPORTED+" \n");
		if(!"".equals(ApprovalBean.getOrderId())&&!(null==ApprovalBean.getOrderId())){//工单号不为空
			sql.append("		AND UPPER(IF_REWARD.ORDER_ID) like UPPER('%"+ApprovalBean.getOrderId()+"%')\n");
		}if(!"".equals(ApprovalBean.getDealerName())&&!(null==ApprovalBean.getDealerName())){//经销商名称不为空
			sql.append("		AND DEALER.DEALER_NAME like '%"+ApprovalBean.getDealerName()+"%'\n");
		}if(!"".equals(ApprovalBean.getLinkMan())&&!(null==ApprovalBean.getLinkMan())){//申请单位不为空
			sql.append("		AND IF_REWARD.LINK_MAN like '%"+ApprovalBean.getLinkMan()+"%'\n");
		}
		if(!"".equals(ApprovalBean.getBeginTime())&&!(null==ApprovalBean.getBeginTime())){//创建时间不为空
			sql.append("		AND IF_REWARD.CREATE_DATE >=to_date('"+ApprovalBean.getBeginTime()+"', 'yyyy-mm-dd hh24:mi:ss')\n");
		}
		if(!"".equals(ApprovalBean.getEndTime())&&!(null==ApprovalBean.getEndTime())){//创建时间不为空
			sql.append("		AND IF_REWARD.CREATE_DATE  <= to_date('"+ApprovalBean.getEndTime()+"', 'yyyy-mm-dd hh24:mi:ss') \n");
		}
		if(!"".equals(ApprovalBean.getRewardType())&&!(null==ApprovalBean.getRewardType())){//类型不为空
			sql.append("		AND IF_REWARD.REWARD_TYPE="+ApprovalBean.getRewardType()+" \n");
		}
		if(!"".equals(ApprovalBean.getRewardStatus())&&!(null==ApprovalBean.getRewardStatus())){//工单状态不为空
			sql.append("		AND IF_REWARD.REWARD_STATUS like '%"+ApprovalBean.getRewardStatus()+"%'\n");
		}
		if(null!=ApprovalBean.getCompanyId()&&!"".equals(ApprovalBean.getCompanyId())){
			sql.append("		AND IF_REWARD.company_id = "+ApprovalBean.getCompanyId());
		}
		if(Utility.testString(ApprovalBean.getDealerCode())){//经销商代码
			sql.append(Utility.getConSqlByParamForEqual(ApprovalBean.getDealerCode(), params, "DEALER", "dealer_code")); 
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
	 * LastUpdate     :  2010-05-19
	 */
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}
	/**
	 * Function       :  奖惩审批表售后服务部审核---查询超链接奖惩详细信息/审核页面详细信息
	 * @param         :  request-工单号
	 * @return        :  奖惩审批信息
	 * @throws        :  Exception
	 * LastUpdate     :  2010-05-19
	 */
	public  PunishmentApprovalBean  getOrderIdInfo(String orderId){
		StringBuilder sql = new StringBuilder();
		sql.append("  select IF_REWARD.ORDER_ID, DEALER.DEALER_CODE,DEALER.DEALER_NAME ,IF_REWARD.REWARD_TYPE,IF_REWARD.LINK_MAN,IF_REWARD.TEL,IF_REWARD.REWARD_MODE,IF_REWARD.REWARD_MONEY,to_char(IF_REWARD.REWARD_DATE,'yyyy-MM-dd')as REWARD_DATE,IF_REWARD.REWARD_CONTENT,IF_REWARD.REWARD_STATUS \n");
		sql.append("  from TT_IF_REWARD IF_REWARD ");
		sql.append("  left join TM_DEALER DEALER  on  IF_REWARD.DEALER_ID=DEALER.DEALER_ID");
		sql.append("  left join TC_USER T on IF_REWARD.CREATE_BY=T.USER_ID ");
		sql.append("  where  1=1 ");
		if (orderId!=null&&!("").equals(orderId)){
		sql.append(" AND IF_REWARD.ORDER_ID like '%");
		sql.append(orderId);
		sql.append("%'");
		}
		PunishmentApprovalBean ApprovalBean = new PunishmentApprovalBean();
		PageResult<PunishmentApprovalBean> rs = pageQuery(PunishmentApprovalBean.class,sql.toString(), null, 10, 1);
		List<PunishmentApprovalBean> ls = rs.getRecords();
		if (ls!=null){
			if (ls.size()>0) {
				ApprovalBean = ls.get(0);
			}
		}
		return ApprovalBean;
	}
	/**
	 * Function       :  奖惩审批表售后服务部审核---查询超链接奖惩详细信息/审核页面详细信息(审批明细信息)
	 * @param         :  request-工单号
	 * @return        :  奖惩审批信息
	 * @throws        :  Exception
	 * LastUpdate     :  2010-05-19
	 */
	public  List<PunishmentApprovalBean>  getOrderIdInfoList(String orderId){
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
		PageResult<PunishmentApprovalBean> rs = pageQuery(PunishmentApprovalBean.class,sql.toString(), null, 10, 1);
		List<PunishmentApprovalBean> list = rs.getRecords();
		return list;
	}
	/**
	 * Function       :  审核通过/驳回奖惩细信息
	 * @param         :  request-工单号
	 * @return        :  奖惩审批信息
	 * @throws        :  Exception
	 * LastUpdate     :  2010-05-19
	 */
	public static void  punishmentApprovalPass(String[] orderIds,TtIfRewardAuditPO AuditPO ){
		for (int i = 0;i<orderIds.length;i++) {
			//新增奖惩审批明细表
			TtIfRewardAuditPO  AuditPOCon =new TtIfRewardAuditPO();//内容
			AuditPOCon.setId(Long.parseLong(SequenceManager.getSequence("")));
			AuditPOCon.setOrderId(orderIds[i]);
			AuditPOCon.setAuditBy(AuditPO.getAuditBy());//审核人
			AuditPOCon.setAuditDate(AuditPO.getAuditDate());//审核时间
			AuditPOCon.setAuditContent(AuditPO.getAuditContent());//审核意见
			AuditPOCon.setAuditStatus(AuditPO.getAuditStatus());//审核状态
			AuditPOCon.setOrgId(AuditPO.getOrgId());//组织ID
			dao.insert(AuditPOCon);
			//修改奖惩审批表
			TtIfRewardPO  RewardPOCon =new TtIfRewardPO();//orderId条件
			RewardPOCon.setOrderId(orderIds[i]);
			TtIfRewardPO  RewardPOContent  =new TtIfRewardPO();//内容
			RewardPOContent.setRewardStatus(AuditPO.getAuditStatus());//工单状态:10151003.表示：售后服务部审核通过;审批状态:10151004,表示： 售后服务部审核驳回
			dao.update(RewardPOCon, RewardPOContent);
		}
	}
}