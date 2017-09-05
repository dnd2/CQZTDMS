package com.infodms.dms.actions.claim.auditing.rule.custom;

import java.util.List;

import org.apache.log4j.Logger;

import com.infodms.dms.actions.claim.auditing.ClaimOrderVO;
import com.infodms.dms.dao.claim.auditing.ClaimAuditingDao;
import com.infodms.dms.po.TmPtPartTypePO;
import com.infodms.dms.po.TtAsWrAuthmonitortypePO;
import com.infoservice.po3.bean.PO;

/**
 * 监控配件大类审核
 * 注：使用索赔授权管理中监控配件大类维护的配件监控规则,处理对应工单需要审核的
 * @author Iverson
 */
public class MonitorPartBigAuditing {
	private Logger logger = Logger.getLogger(MonitorPartBigAuditing.class);
	public String getRuleDesc() {
		return "监控配件大类审核";
	}
	/**
	 * 查询对应主要配件设定的监控规则，提取其中需要通过的审核级别
	 * @param orderVO
	 * @return AuditingVO
	 */
	public AuditingVO deal(ClaimOrderVO orderVO) {
		logger.info("监控配件大类审核 开始>>>>");	
		AuditingVO resultVO = new AuditingVO();//实例类
		try{
			ClaimAuditingDao auditingDao = ClaimAuditingDao.getInstance();//实例类
			//查询主要配件
			List<PO> mainPartList = orderVO.getMainPartList();
			if(mainPartList==null || mainPartList.size()<1)//没有主要配件自动通过
				return resultVO;
			List<TmPtPartTypePO> monitorPartBigList = auditingDao.queryMonitorPartBig(orderVO.getCompanyId(),
					orderVO.getClaimPO().getId());
			if(monitorPartBigList!=null && monitorPartBigList.size()>0){
				for (TmPtPartTypePO monitorPartBigPO : monitorPartBigList) {
					if(monitorPartBigPO!=null){
						resultVO.setReasions(resultVO.getReasions() + monitorPartBigPO.getParttypeName());
						resultVO.setRoles(resultVO.getRoles() + "," + "200");
					}
				}
			}
		} catch(Exception e){
			e.printStackTrace();
		}
		logger.info("监控配件大类审核 结束<<<<");
		return resultVO;
	}
}
