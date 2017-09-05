/**********************************************************************
* <pre>
* FILE :  PunishmentApprovalLeadDao.java
* CLASS : PunishmentApprovalLeadDao
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
 * $Id: PunishmentApprovalLeadDao.java,v 1.1 2010/08/16 01:43:15 yuch Exp $
 */
package com.infodms.dms.dao.feedbackMng;

import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.PunishmentApprovalLeadBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.po.TtIfRewardAuditPO;
import com.infodms.dms.po.TtIfRewardPO;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;
/**
 * Function       :  轿车公司奖惩审批
 * @author        :  PGM
 * CreateDate     :  2010-05-19
 * @version       :  0.1
 */
@SuppressWarnings("unchecked")
public class PunishmentApprovalLeadDao extends BaseDao{
	public static Logger logger = Logger.getLogger(PunishmentApprovalLeadDao.class);
	private static final PunishmentApprovalLeadDao dao = new PunishmentApprovalLeadDao ();
	public  static final PunishmentApprovalLeadDao getInstance() {
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
	public  PageResult<Map<String, Object>>  getAllpunishmentApprovalLeadInfo(PunishmentApprovalLeadBean ApprovalLeadBean, int curPage,int pageSize) throws Exception {
		StringBuffer sql = new StringBuffer();
		List<Object> params = new LinkedList<Object>();
		sql.append("	select IF_REWARD.ORDER_ID,IF_REWARD.LINK_MAN, DEALER.DEALER_CODE,DEALER.DEALER_NAME ,IF_REWARD.REWARD_TYPE,to_char(IF_REWARD.REWARD_DATE,'yyyy-MM-dd')as REWARD_DATE,IF_REWARD.REWARD_STATUS from TT_IF_REWARD IF_REWARD  \n");
		sql.append("    left join TM_DEALER DEALER  on  IF_REWARD.DEALER_ID=DEALER.DEALER_ID   \n");
		sql.append("    left join TC_USER T on IF_REWARD.CREATE_BY=T.USER_ID \n");
		sql.append("    where IF_REWARD.IS_DEL=0 AND IF_REWARD.REWARD_STATUS ="+Constant.AWARD_PUNISH_STATUS_SERVICE_PASS+" \n");
		if(!"".equals(ApprovalLeadBean.getOrderId())&&!(null==ApprovalLeadBean.getOrderId())){//工单号不为空
			sql.append("		AND UPPER(IF_REWARD.ORDER_ID) like UPPER('%"+ApprovalLeadBean.getOrderId()+"%')\n");
		}if(Utility.testString(ApprovalLeadBean.getDealerCode())){//经销商代码
			sql.append(Utility.getConSqlByParamForEqual(ApprovalLeadBean.getDealerCode(), params, "DEALER", "dealer_code")); 
		}if(!"".equals(ApprovalLeadBean.getDealerName())&&!(null==ApprovalLeadBean.getDealerName())){//经销商ID不为空
			sql.append("		AND DEALER.DEALER_NAME like '%"+ApprovalLeadBean.getDealerName()+"%'\n");
		}if(!"".equals(ApprovalLeadBean.getLinkMan())&&!(null==ApprovalLeadBean.getLinkMan())){//申请单位不为空
			sql.append("		AND IF_REWARD.LINK_MAN like '%"+ApprovalLeadBean.getLinkMan()+"%'\n");
		}
		if(!"".equals(ApprovalLeadBean.getBeginTime())&&!(null==ApprovalLeadBean.getBeginTime())){//创建时间不为空
			sql.append("		AND IF_REWARD.CREATE_DATE >=to_date('"+ApprovalLeadBean.getBeginTime()+"', 'yyyy-mm-dd hh24:mi:ss')  \n");
		}
		if(!"".equals(ApprovalLeadBean.getEndTime())&&!(null==ApprovalLeadBean.getEndTime())){//创建时间不为空
			sql.append("		AND IF_REWARD.CREATE_DATE  <= to_date('"+ApprovalLeadBean.getEndTime()+"', 'yyyy-mm-dd hh24:mi:ss') \n");
		}
		if(!"".equals(ApprovalLeadBean.getRewardType())&&!(null==ApprovalLeadBean.getRewardType())){//类型不为空
			sql.append("		AND IF_REWARD.REWARD_TYPE="+ApprovalLeadBean.getRewardType()+" \n");
		}
		if(!"".equals(ApprovalLeadBean.getRewardStatus())&&!(null==ApprovalLeadBean.getRewardStatus())){//工单状态不为空
			sql.append("		AND IF_REWARD.REWARD_STATUS like '%"+ApprovalLeadBean.getRewardStatus()+"%'\n");
		}
		if(null!=ApprovalLeadBean.getCompanyId()&&!"".equals(ApprovalLeadBean.getCompanyId())){
			sql.append("		AND IF_REWARD.company_id = "+ApprovalLeadBean.getCompanyId());
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
	 * Function       :  奖惩审批表轿车厂审核---查询超链接奖惩详细信息/审核页面详细信息
	 * @param         :  request-工单号
	 * @return        :  奖惩审批信息
	 * @throws        :  Exception
	 * LastUpdate     :  2010-05-19
	 */
	public  PunishmentApprovalLeadBean  getOrderIdInfo(String orderId){
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
		PunishmentApprovalLeadBean LeadBean = new PunishmentApprovalLeadBean();
		PageResult<PunishmentApprovalLeadBean> rs = pageQuery(PunishmentApprovalLeadBean.class,sql.toString(), null, 10, 1);
		List<PunishmentApprovalLeadBean> ls = rs.getRecords();
		if (ls!=null){
			if (ls.size()>0) {
				LeadBean = ls.get(0);
			}
		}
		return LeadBean;
	}
	/**
	 * Function       :  奖惩审批表轿车厂审核---查询超链接奖惩详细信息/审核页面详细信息(审批明细信息)
	 * @param         :  request-工单号
	 * @return        :  奖惩审批信息
	 * @throws        :  Exception
	 * LastUpdate     :  2010-05-19
	 */
	public  List<PunishmentApprovalLeadBean>  getOrderIdInfoList(String orderId){
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
		PageResult<PunishmentApprovalLeadBean> rs = pageQuery(PunishmentApprovalLeadBean.class,sql.toString(), null, 10, 1);
		List<PunishmentApprovalLeadBean> list = rs.getRecords();
		return list;
	}
	/**
	 * Function       :  审核通过/驳回奖惩细信息
	 * @param         :  request-工单号
	 * @return        :  奖惩审批信息
	 * @throws        :  Exception
	 * LastUpdate     :  2010-05-19
	 */
	public void  punishmentApprovalLeadPass(String[] orderIds,TtIfRewardAuditPO AuditPO ){
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
			RewardPOContent.setRewardStatus(AuditPO.getAuditStatus());//审批状态:10151005,表示：轿车公司审核通过;审批状态:10151006,表示：轿车公司审核驳回
			dao.update(RewardPOCon, RewardPOContent);
		}
	}
}