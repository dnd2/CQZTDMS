package com.infodms.dms.actions.claim.auditing.rule.custom.elment;

import java.util.ArrayList;
import java.util.List;

import com.infodms.dms.actions.claim.auditing.ClaimOrderVO;
import com.infodms.dms.actions.repairOrder.RoOrderVO;
import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.claim.auditing.AutoAuditingDao;
import com.infodms.dms.po.TtAsRoRepairPartPO;
import com.infodms.dms.po.TtAsWrVinPartRepairTimesPO;

/**
 * 主要件次数
 */
public class Element03 extends AbstractElement {

	/**
	 * 主要件次数
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Object getElementValue(RoOrderVO orderVO) {
		List<TtAsRoRepairPartPO >  list = orderVO.getPartsList();
		AutoAuditingDao  dao  = AutoAuditingDao.getInstance();
		String vin = orderVO.getVehiclePO().getVin();
		List<Integer> mainPartList = new ArrayList<Integer>();
		//主要件次数和费主要件次数 均不包含 特殊服务。
		if(list!=null&&list.size()>0 && !Constant.REPAIR_TYPE_06.equalsIgnoreCase(orderVO.getRepaitPo().getRepairTypeCode())){
			for(int i=0;i<list.size();i++){
				String partCode= list.get(i).getPartNo();
				TtAsWrVinPartRepairTimesPO tp = new TtAsWrVinPartRepairTimesPO();
				tp.setVin(vin);
				tp.setPartCode(partCode);
			List<TtAsWrVinPartRepairTimesPO> tList = dao.select(tp);
			boolean isMainPart = dao.isMainPart(partCode);
			if(tList!=null && tList.size()>0&&isMainPart){
				mainPartList.add(tList.get(0).getCurTimes()+1);
			}else{
				if(!isMainPart){
					mainPartList.add(0);
				}else{
					mainPartList.add(1);
				}
				
			}
			}
		}
 		return mainPartList;
	}

	@Override
	protected Object getElementValue(ClaimOrderVO orderVO) {
		// TODO Auto-generated method stub
		return null;
	}
}
