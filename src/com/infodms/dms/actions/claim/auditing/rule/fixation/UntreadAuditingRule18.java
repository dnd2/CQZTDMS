package com.infodms.dms.actions.claim.auditing.rule.fixation;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.infodms.dms.actions.claim.auditing.ClaimOrderVO;
import com.infodms.dms.actions.claim.auditing.rule.AbstractAuditingRule;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.dao.claim.auditing.ClaimAuditingDao;
import com.infodms.dms.dao.claim.dealerClaimMng.ClaimBillMaintainDAO;
import com.infodms.dms.po.TtAsRepairOrderPO;
import com.infodms.dms.po.TtAsWrQamaintainPO;

/**
 * 需要预授权项目未通过预授权申请
 * <pre>
 * 前提：
 *     无
 * 规则：
 * 规则：
 *    判断申请单中工时是否有预授权
 *    判断申请单中配件是否有预授权
 *    判断申请单的其它项目是否有预授权
 *    判断对应工单是否申请过预售权
 *    如果 对应 工单申请过预售权 且审核通过 那么 这审核通过
 *    如果 对应 工单未申请过预售权或未审核通过
 *         检测对应需要申请预售权的项目(配件、工时和其他项目)是否都已经
 *         申请过预售权，如果存在未申请过，那么 退回
 * 注：现在只监控对应需要预授权的配件、工时、其他项目或整单，如果都不再监控范围内
 *     不与检查。
 * </pre>
 * @author XZM
 */
public class UntreadAuditingRule18 extends AbstractAuditingRule {
	
	@Override
	protected String getRuleDesc() {
		return "需要预授权项目未通过预授权申请";
	}

	/**
	 * 无
	 */
	@Override
	protected boolean preConditionCheck(ClaimOrderVO orderVO) {
		return true;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected boolean ruleCheck(ClaimOrderVO orderVO) {
		
		boolean result = false;
		
		this.backInfo = "需要预授权项目未通过预授权申请";
		
		String orderNO = orderVO.getClaimPO().getRoNo();
		if(!Utility.testString(orderNO))//索赔单没用工单号，默认通过
			return false;
		
		//查询工单信息
		ClaimAuditingDao auditingDao = ClaimAuditingDao.getInstance();
		TtAsRepairOrderPO conditionPO = new TtAsRepairOrderPO();
		conditionPO.setRoNo(orderNO);
		List<TtAsRepairOrderPO> orderList = auditingDao.select(conditionPO);
		TtAsRepairOrderPO workOrderPO = new TtAsRepairOrderPO();
		if(orderList!=null && orderList.size()>0)
			workOrderPO = orderList.get(0);
		else//没有查询到工单信息，默认通过
			return false;
		
		
		Integer status = workOrderPO.getForlStatus();
		if(Constant.RO_FORE_02.equals(status)){//如果预售权审核通过
			result = false;
		}else if(this.isNeedAuth(orderVO,workOrderPO)){//1、检测对应工单是否需要 做整单预授权
		    //11、(需要整单预授权) 检测对应工单是否已经做过整单预售权申请并且审核通过
			result = true;
		}else{//不需要申请预售权则直接通过
			result = false;
		}
		
		//12、(不需要整单预授权) 检测是否需要项目预售权
		/*{
			PreAuthorization preAuditing = new PreAuthorization();
			result = preAuditing.checkPart(orderVO, false);
			result = result && preAuditing.checkManHour(orderVO, false);
			result = result && preAuditing.checkOhter(orderVO);
			result = !result;
		}
		*/
		
		return result;
	}
	
	/**
	 * 是否需要预授权
	 * @param orderVO
	 * @param workOrderPO
	 * @return boolean false : 不需要 true :需要
	 */
	private boolean isNeedAuth(ClaimOrderVO orderVO,TtAsRepairOrderPO workOrderPO){
		
		boolean result = false;
		
		//现在不使用自己坚持是否需要预售权,直接使用工单中的字段判断
		Integer isNeed = null;
		
		if(workOrderPO!=null && workOrderPO.getApprovalYn()!=null)
			isNeed = workOrderPO.getApprovalYn();
		
		if(isNeed!=null && Constant.APPROVAL_YN_YES.equals(isNeed)){
			result = true;
		}else{
			result = false;
		}
		//result = this.checkOrderType(workOrderPO)
				 //|| this.checkMaintain(orderVO, workOrderPO);
		
		return result;
	}
	
	/**
	 * 检测对应工单类型是否需要预授权
	 * @param workOrderPO 工单信息
	 * @return boolean false : 不需要 true :需要
	 */
	@SuppressWarnings("unused")
	@Deprecated
	private boolean checkOrderType(TtAsRepairOrderPO workOrderPO){
		
		boolean result = false;

		if(Constant.REPAIR_TYPE_02.equals(workOrderPO.getRepairTypeCode()))
			result = true;
		
		return result;
	}
	
	/**
	 * 检测对应次数保养，该车辆是否需要预授权
	 * 规则：
	 *     如果对应车辆在对应保修次数的进厂里程超过该次预定的里程，
	 *     那么需要申请预授权。
	 * 注：只检测工单类型是"保养"，其他类型直接通过
	 * @param orderVO 索赔单信息
	 * @param workOrderPO 工单信息
	 * @return  boolean false : 不需要 true :需要
	 */
	@SuppressWarnings({ "unused", "unchecked" })
	@Deprecated
	private boolean checkMaintain(ClaimOrderVO orderVO,TtAsRepairOrderPO workOrderPO){
		
		boolean result = false;
		
		if(!Constant.REPAIR_TYPE_04.equals(workOrderPO.getRepairTypeCode()))//不是保养类型的工单直接通过
			return result;
		
		Long companyId = orderVO.getCompanyId();//经销商所属公司ID
		Double inMileage = orderVO.getClaimPO().getInMileage();//进厂里程
		Date stateDate = orderVO.getVehiclePO().getPurchasedDate();//保修开始时间
		String vin = orderVO.getClaimPO().getVin();//VIN
		
		//1、取得保养总次数
		int totalCount = 0;
		ClaimBillMaintainDAO cbmDao = ClaimBillMaintainDAO.getInstance();
		TtAsRepairOrderPO conditionPO = new TtAsRepairOrderPO();
		conditionPO.setVin(vin);
		conditionPO.setRepairTypeCode(Constant.REPAIR_TYPE_04);
		//11、取的对应VIN车辆的"保养"的维修工单数
		List<TtAsRepairOrderPO> lsa = cbmDao.select(conditionPO);
		if (lsa!=null) {
			totalCount = totalCount + lsa.size(); //取得免费保养申请次数
		}
		//12、服务活动中该车的保养次数(服务活动类型为"保养")
		totalCount = totalCount + cbmDao.getActFree(vin);
		
		//2、取得车辆从保修日开始使用时间（月）
		int day = 999999;//默认没有保修开始时间，则需要授权
		Date now = new Date();
		if (stateDate!=null) {
			String formatStyle ="yyyy-MM-dd";  
			SimpleDateFormat df = new SimpleDateFormat(formatStyle);  
			String d1 = df.format(stateDate);
			String d2 = df.format(now);
			day  = Utility.compareDate(d1,d2,1);
		}
		
		//3、根据保养次数、所属公司、购车月数和进厂里程 
		//   判断该车是否在保修时已经按照规则做过免费保养
		List<TtAsWrQamaintainPO> lsq =  cbmDao.getFree(totalCount,companyId,day,day,inMileage);
		
		if (lsq!=null && lsq.size()>0) {//不需要授权
			result = false;
		}else {//需要授权
			result = true;
		}
		
		return result;
	}
}
