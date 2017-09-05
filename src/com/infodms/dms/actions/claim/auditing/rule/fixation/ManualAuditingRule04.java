package com.infodms.dms.actions.claim.auditing.rule.fixation;

import java.util.ArrayList;
import java.util.List;

import com.infodms.dms.actions.claim.auditing.ClaimOrderVO;
import com.infodms.dms.actions.claim.auditing.rule.AbstractAuditingRule;
import com.infodms.dms.dao.claim.auditing.ClaimAuditingDao;
import com.infodms.dms.po.TmDealerLabourDetailPO;
import com.infodms.dms.po.TmDealerPO;
import com.infodms.dms.po.TtAsWrLabouritemPO;
import com.infoservice.po3.bean.PO;

/**
 * 判断此经销商是否可以申请此索赔单中选择的工时
 * @author zuoxj
 */
public class ManualAuditingRule04 extends AbstractAuditingRule {
	
	@Override
	protected String getRuleDesc() {
		return "存在经销商等级不允许索赔的工时！";
	}

	/**
	 * 没有前期判断需求
	 * 经销商作业等级为空  
	 */
	@Override
	protected boolean preConditionCheck(ClaimOrderVO orderVO) {
		
		boolean result = false ;
		
		TmDealerPO dealer = orderVO.getDealerPO() ;
		
		if(dealer.getDealerLabourType()!=null){
			if(dealer.getDealerLabourType()!=0){
				result = true ;
			}
		}
		
		return result ;
	}

	/**
	 * 不需要检查
	 */
	@Override
	protected boolean ruleCheck(ClaimOrderVO orderVO) {
		
		boolean result = false ;
		
		this.backInfo = "存在经销商等级不允许索赔的工时！ ";
		
		ClaimAuditingDao dao = ClaimAuditingDao.getInstance() ;
		
		//根据此索赔单查询经销商
		TmDealerPO dealer = orderVO.getDealerPO() ;
		
		//根据此索赔单查询索赔工时
		List<PO> laborList = orderVO.getMainLabourList();
		
		//查询此经销商对应工时作业级别的所有作业类型
		TmDealerLabourDetailPO po = new TmDealerLabourDetailPO();
		po.setDealerLabourType(dealer.getDealerLabourType());
		List<TmDealerLabourDetailPO> list = dao.select(po) ;
		
		//构建一个list存放此作业等级下的工时
		List<String> strList = new ArrayList<String>();
		if(list!=null && list.size()>0){
			for(int i = 0 ; i < list.size() ; i++){
				strList.add(list.get(i).getLabourCode());
			}
		}
		
		TtAsWrLabouritemPO info = null ;
		if(laborList!=null && laborList.size()>0){
			for(int i = 0 ; i < laborList.size() ; i++){
				info = (TtAsWrLabouritemPO)laborList.get(i);
				if(list!=null && list.size()>0){
					for(int j = 0 ; j < list.size() ; j++){
						if(!strList.contains(info.getWrLabourcode())){
							result = true ;
							break ;
						}
					}
				}
			}
		}
		
		return result;
	}
}
