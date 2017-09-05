package com.infodms.dms.actions.claim.auditing.rule.custom.elment;

import com.infodms.dms.actions.claim.auditing.ClaimOrderVO;
import com.infodms.dms.actions.repairOrder.RoOrderVO;


public class Element06 extends AbstractElement{

	/**
	 * 维修总费用
	 */
	@Override
	public Object getElementValue(RoOrderVO orderVO) {
		return orderVO.getRepairTotal();
	}

	@Override
	protected Object getElementValue(ClaimOrderVO orderVO) {
		// TODO Auto-generated method stub
		return null;
	}

}
