package com.infodms.dms.actions.claim.auditing.rule.custom;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.actions.claim.auditing.ClaimOrderVO;
import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.claim.auditing.ClaimAuditingDao;
import com.infodms.dms.dao.claim.dealerClaimMng.DealerClaimReportDao;
import com.infodms.dms.po.TmPtDealerPartPO;
import com.infodms.dms.po.TtAsWrAuthmonitorpartPO;
import com.infodms.dms.po.TtAsWrWrauthorizationPO;
import com.infoservice.po3.bean.PO;

/**
 * 监控配件审核
 * 注：使用 索赔授权管理 中 监控配件维护 的配件监控规则，处理对应工单需要审核的
 * 人员;只出来索赔申请单中的主要配件
 * @author XZM
 */
public class MonitorPartAuditing {
	
	private Logger logger = Logger.getLogger(MonitorPartAuditing.class);
	
	public String getRuleDesc() {
		return "监控配件审核";
	}

	/**
	 * 查询对应主要配件设定的监控规则，提取其中需要通过的审核级别
	 * @param orderVO
	 * @return AuditingVO
	 */
	public AuditingVO deal(ClaimOrderVO orderVO) {
		
		logger.info("监控配件审核 开始>>>>");
		
		AuditingVO resultVO = new AuditingVO();
		try{
			ClaimAuditingDao auditingDao = ClaimAuditingDao.getInstance();
			
			//查询主要配件
			List<PO> mainPartList = orderVO.getMainPartList();
			
			if(mainPartList==null || mainPartList.size()<1)//没有主要配件自动通过
				return resultVO;
			
			//查找车型组信息
			//String wrGroup = orderVO.getWrPartGroupId();
			
			//if(wrGroup==null || "".equals(wrGroup))//没有车型组信息自动通过
				//return resultVO;
			//查询需要人工监控的主要配件
			//List<TtAsWrAuthmonitorpartPO> monitorPartList = auditingDao.queryMonitorPart(orderVO.getCompanyId(),wrGroup,
					//orderVO.getClaimPO().getId(),Constant.IS_MAIN_TROUBLE);
			//MODIFY BY XZM 20100917 技术授权规则修改为不使用车型组
			List<TtAsWrAuthmonitorpartPO> monitorPartList = auditingDao.queryMonitorPart(orderVO.getCompanyId(),
					orderVO.getClaimPO().getId(),Constant.IS_MAIN_TROUBLE);
			
			if(monitorPartList!=null && monitorPartList.size()>0){
				for (TtAsWrAuthmonitorpartPO monitorPartPO : monitorPartList) {
					if(monitorPartPO!=null && monitorPartPO.getApprovalLevel()!=null){
						resultVO.setReasions(resultVO.getReasions() + monitorPartPO.getPartName());
						resultVO.setRoles(resultVO.getRoles() + "," + monitorPartPO.getApprovalLevel());
					}
				}
			}
			DealerClaimReportDao reportDao = new DealerClaimReportDao();
			List<TmPtDealerPartPO> listPart = reportDao.viewDealerPart(orderVO.getClaimPO().getId());
			if(listPart!=null && listPart.size()>0){
				for (TmPtDealerPartPO PartPO : listPart) {
					if(PartPO!=null && PartPO.getApprovalCode()!=null){
						resultVO.setReasions(resultVO.getReasions() + PartPO.getPartCode());
						resultVO.setRoles(resultVO.getRoles() + "," + PartPO.getApprovalCode());
					}
				}
			}
		
			
		} catch(Exception e){
			e.printStackTrace();
		}
		
		logger.info("监控配件审核 结束<<<<");
		return resultVO;
	}
}
