package com.infodms.dms.actions.claim.auditing.rule.fixation;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.infodms.dms.actions.claim.auditing.ClaimOrderVO;
import com.infodms.dms.actions.claim.auditing.rule.AbstractAuditingRule;
import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.claim.auditing.ClaimAuditingDao;
import com.infodms.dms.po.TtAsWrApplicationPO;
import com.infodms.dms.po.TtAsWrLabouritemPO;
import com.infoservice.po3.bean.PO;

/**
 * 超过90天不允许更换车门调校
 * <pre>
 * 前提:
 *     申请单类型为: 一般维修了，外出维修
 * 规则：
 *    
 *    
 * </pre>
 * @author XZM
 */
public class RefuseAuditingRule08 extends AbstractAuditingRule {

	private ClaimAuditingDao auditingDao = ClaimAuditingDao.getInstance();
	
	@Override
	protected String getRuleDesc() {
		return "购车未超过90天不允许维修车门";
	}

	/**
	 * 前提:
	 *     申请单类型为: 免费保养
	 */
	@Override
	protected boolean preConditionCheck(ClaimOrderVO orderVO) {
		
		boolean result = true;
		if(orderVO!=null && orderVO.getClaimPO()!=null){
			TtAsWrApplicationPO claimPO = orderVO.getClaimPO();
			if(Constant.CLA_TYPE_01.equals(claimPO.getClaimType())||Constant.CLA_TYPE_09.equals(claimPO.getClaimType())){
				Date date1 = (Date) orderVO.getClaimPO().getRoStartdate();
				Date date2 = (Date) orderVO.getVehiclePO().getPurchasedDate();
				    long l = date1.getTime() - date2.getTime();
				   long i = l / (1000 * 60 * 60 * 24);
				   if(i>90){
					   result=false;
				   }
				   else{
					   result = true;
				   }
			}else{
				result = false;
			}
		}else{
			result = false;
		}
		return result;
	}

	@Override
	protected boolean ruleCheck(ClaimOrderVO orderVO) {
		
		boolean result = false;
		
		this.backInfo = "执行（96）关于加强临卖状态检查及交车确认检查的通知";
		
		if(orderVO!=null && orderVO.getClaimPO()!=null){
			List<PO> laborList = orderVO.getMainLabourList();
			TtAsWrLabouritemPO info = null ;
			List<String> strList = new ArrayList<String>();
			strList.add("210043");
			strList.add("340062");
			strList.add("170018");
			
			for (int i=0;laborList.size()>0;i++){
				info = (TtAsWrLabouritemPO)laborList.get(0);
				if(!strList.contains(info.getWrLabourcode())){
					result = false ;
					break ;
				}
				else{
					result = true; 
					break;
				}
				
			}
		}
		return result;
	}
}
