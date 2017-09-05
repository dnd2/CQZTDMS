package com.infodms.dms.actions.claim.auditing.rule.custom;

import java.util.List;

import org.apache.log4j.Logger;

import com.infodms.dms.actions.claim.auditing.ClaimOrderVO;
import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.claim.auditing.ClaimAuditingDao;
import com.infodms.dms.po.TtAsWrAuthmonitorlabPO;
import com.infoservice.po3.bean.PO;

/**
 * 监控工时审核
 * 注：使用 索赔授权管理 中 监控工时维护 的工时监控规则，处理对应工单需要审核的
 * 人员;只出来索赔申请单中的主要工时
 * @author XZM
 */
public class MonitorManHourAuditing {
	
	private Logger logger = Logger.getLogger(MonitorManHourAuditing.class);

	public String getRuleDesc() {
		return "监控工时审核";
	}

	/**
	 * 查询对应主要工时设定的监控规则，提取其中需要通过的审核级别
	 * @param orderVO
	 * @return AuditingVO
	 */
	public AuditingVO deal(ClaimOrderVO orderVO) {
		
		logger.info("监控工时审核 开始>>>>");
		
		AuditingVO resultVO = new AuditingVO();
		try{
			
			ClaimAuditingDao auditingDao = ClaimAuditingDao.getInstance();
			
			//查询主要工时
			List<PO> mainLabourList = orderVO.getMainLabourList();
			
			if(mainLabourList==null || mainLabourList.size()<1)//没有主要工时时自动通过
				return resultVO;
			
			//查找车型组信息
			String wrGroup = orderVO.getWrGroupId();
			
			if(wrGroup==null || "".equals(wrGroup))//没有车型组信息
				return resultVO;
			
			//统计主要工时需要人工审核信息
			List<TtAsWrAuthmonitorlabPO> monitorLabList = auditingDao.queryMonitorLabour(wrGroup,orderVO.getCompanyId(),
					orderVO.getClaimPO().getId(),Constant.IS_MAIN_TROUBLE);
			if(monitorLabList!=null && monitorLabList.size()>0){
				for (TtAsWrAuthmonitorlabPO monitorLabourPO : monitorLabList) {
					if(monitorLabourPO!=null && monitorLabourPO.getApprovalLevel()!=null){
						resultVO.setReasions(resultVO.getReasions() + monitorLabourPO.getLabourOperationName());
						resultVO.setRoles(resultVO.getRoles() + "," + monitorLabourPO.getApprovalLevel());
					}
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		}	
		logger.info("监控工时审核 结束<<<<");
		
		return resultVO;
	}

}
