package com.infodms.dms.actions.claim.auditing.rule.custom.elment;

import com.infodms.dms.actions.claim.auditing.ClaimOrderVO;
import com.infodms.dms.actions.repairOrder.RoOrderVO;

/**
 * 修理累计时间
 */
public class Element05 extends AbstractElement {

	/**
	 * 修理累计时间
	 */
	@Override
	public Object getElementValue(RoOrderVO orderVO) {
		return orderVO.getRepairDays();
	}

	@Override
	protected Object getElementValue(ClaimOrderVO orderVO) {
		// TODO Auto-generated method stub
		return null;
	}

}
