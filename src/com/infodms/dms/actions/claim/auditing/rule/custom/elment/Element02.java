package com.infodms.dms.actions.claim.auditing.rule.custom.elment;

import com.infodms.dms.actions.claim.auditing.ClaimOrderVO;
import com.infodms.dms.actions.repairOrder.RoOrderVO;

/**
 * 维修项目总金额
 * @author XZM
 */
public class Element02 extends AbstractElement {

	/**
	 * 维修项目总金额
	 */
	@Override
	public Object getElementValue(RoOrderVO orderVO) {
		return orderVO.getLabourAmount();
	}

	@Override
	protected Object getElementValue(ClaimOrderVO orderVO) {
		// TODO Auto-generated method stub
		return null;
	}

}
