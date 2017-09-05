package com.infodms.dms.actions.claim.oldPart;

import java.util.Date;

import com.infodms.dms.dao.claim.application.DealerBalanceDao;
import com.infodms.dms.po.TtAsWrReturnAuthitemPO;
import com.infodms.dms.util.CommonUtils;

/**
 * 记录结算单状态变化记录
 * @author Administrator
 */
public class ReturnStatusRecord {
	
	/** 部分签收 **/
	public static Integer BACK_LIST_STATUS_03 = 10811003;
	/** 全部签收 **/
	public static Integer BACK_LIST_STATUS_04 = 10811004;
	
	/**
	 * 记录结算单状态
	 * @param balanceId 结算单ID
	 * @param userId 操作用户ID
	 * @param userName 操作用户名称
	 * @param orgId 用户所属组织Id 
	 * @param status 操作后结算单状态 @see BalanceStatusRecord.STATUS_XX
	 */
	@SuppressWarnings("unchecked")
	public static void recordStatus(Long returnId,Long userId,String userName,Long orgId,Integer status,String boxNo){
		if(returnId==null || status ==null)
			return;
		if(userId==null)
			userId = -1L;
		if(orgId==null)
			orgId = -1L;
		userName = CommonUtils.checkNull(userName);
		Date nowdate = new Date();
		TtAsWrReturnAuthitemPO authPO = new TtAsWrReturnAuthitemPO();
		authPO.setReturnId(returnId);
		authPO.setUpdateBy(userId);
		authPO.setAuthPersonId(userId);
		authPO.setAuthPersonName(userName);
		authPO.setAuthStatus(status);
		authPO.setAuthTime(nowdate);
		authPO.setUpdateBy(userId);
		authPO.setUpdateDate(nowdate);
		authPO.setCreateBy(userId);
		authPO.setCreateDate(nowdate);
		authPO.setAuthOrgId(orgId);
		authPO.setAuthBoxNo(boxNo);
		
		DealerBalanceDao balanceDao = DealerBalanceDao.getInstance();
		balanceDao.insert(authPO);
	}
	
	public static void recordStatus1(Long returnId,Long userId,String userName,Long orgId,Integer status,String isReported){
		if(returnId==null || status ==null)
			return;
		if(userId==null)
			userId = -1L;
		if(orgId==null)
			orgId = -1L;
		userName = CommonUtils.checkNull(userName);
		Date nowdate = new Date();
		TtAsWrReturnAuthitemPO authPO = new TtAsWrReturnAuthitemPO();
		authPO.setReturnId(returnId);
		authPO.setUpdateBy(userId);
		authPO.setAuthPersonId(userId);
		authPO.setAuthPersonName(userName);
		authPO.setAuthStatus(status);
		authPO.setAuthTime(nowdate);
		authPO.setUpdateBy(userId);
		authPO.setUpdateDate(nowdate);
		authPO.setCreateBy(userId);
		authPO.setCreateDate(nowdate);
		authPO.setAuthOrgId(orgId);
		authPO.setIsReported(isReported);
		
		DealerBalanceDao balanceDao = DealerBalanceDao.getInstance();
		balanceDao.insert(authPO);
	}
}
