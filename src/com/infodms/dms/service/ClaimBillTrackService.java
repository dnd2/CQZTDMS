package com.infodms.dms.service;

import java.util.List;

import com.infodms.dms.po.TtAsWrApplicationCounterPO;
import com.infodms.dms.po.TtAsWrApplicationPO;

public interface ClaimBillTrackService {
	/**
	 * 根据反索赔对象的条件查询符合条件的反索赔集合
	 * @param obj
	 * @return
	 * @author chenyub@yonyou.com
	 * 下午1:43:20
	 */
	public List<TtAsWrApplicationCounterPO> queryClaimCounterByCounterObj(TtAsWrApplicationCounterPO obj);
	
	/**
	 * 根据索赔对象的条件查询符合条件的反索赔集合
	 * @param obj
	 * @return
	 * @author chenyub@yonyou.com
	 * 下午1:44:02
	 */
	public List<TtAsWrApplicationCounterPO> queryClaimCounterByObj(TtAsWrApplicationPO obj);
	
	/**
	 * 对指定索赔单进行反索赔
	 * @param obj 执行索赔单的条件
	 * @param counterRemark  反索赔备注
	 * @return true:成功;false:失败
	 * @throws Exception
	 * @author chenyub@yonyou.com
	 * 2015年11月9日  下午2:37:53
	 */
	public boolean claimCounter(TtAsWrApplicationCounterPO obj,String counterRemark) throws Exception;
}
