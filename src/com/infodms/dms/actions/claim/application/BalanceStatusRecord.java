package com.infodms.dms.actions.claim.application;

import java.util.Date;

import com.infodms.dms.dao.claim.application.DealerBalanceDao;
import com.infodms.dms.po.TtAsWrBalanceAuthitemPO;
import com.infodms.dms.util.CommonUtils;

/**
 * 记录结算单状态变化记录
 * @author Administrator
 */
public class BalanceStatusRecord {
	
	/** 保存 */
	public static Integer STATUS_01 = 11861001;
	/** 上报 */
	public static Integer STATUS_02 = 11861002;
	/** 收单完成 */
	public static Integer STATUS_03 = 11861003;
	/** 审核完成 */
	public static Integer STATUS_04 = 11861004;
	/** 复核申请上报 */
	public static Integer STATUS_05 = 11861005;
	/** 复核申请完成 */
	public static Integer STATUS_06 = 11861006;
	/** 开票通知下发 (开票通知,行政扣款两个状态都用11861007) */
	public static Integer STATUS_07 = 11861007;
	/** 开票通知单确认 */
	public static Integer STATUS_08 = 11861008;
	/** 应税劳务清单生成 */
	public static Integer STATUS_09 = 11861009;
	/** 应税劳务汇总单生成 */
	public static Integer STATUS_10 = 11861010;
	/** 票据上传 */
	public static Integer STATUS_11 = 11861011;
	/** 收票完成 */
	public static Integer STATUS_12 = 11861012;
	/** 收票确认 */
	public static Integer STATUS_13 = 11861013;
	/** 一级经销商完成确认(对应结算单 "已完成" 状态) */
	public static Integer STATUS_14 = 11861014;
	
	/** 复核申请退回结算室审核 */
	public static Integer STATUS_15 = 11861015;
	/** 一级经销商完成确认(对应结算单 "已完成" 状态) */
	public static Integer STATUS_16 = 11861016;
	
	public static Integer STATUS_17 = 13581001;
	
	/*** 结算室回退状态****/
	public static Integer STATUS_BACK = 13851001;
	/**
	 * 记录结算单状态
	 * @param balanceId 结算单ID
	 * @param userId 操作用户ID
	 * @param userName 操作用户名称
	 * @param orgId 用户所属组织Id 
	 * @param status 操作后结算单状态 @see BalanceStatusRecord.STATUS_XX
	 */
	@SuppressWarnings("unchecked")
	public static void recordStatus(Long balanceId,Long userId,String userName,
			Long orgId,Integer status){
		if(balanceId==null || status ==null)
			return;
		
		if(userId==null)
			userId = -1L;
		
		if(orgId==null)
			orgId = -1L;
		
		userName = CommonUtils.checkNull(userName);
		
		Date nowdate = new Date();
		TtAsWrBalanceAuthitemPO authPO = new TtAsWrBalanceAuthitemPO();
		authPO.setBalanceId(balanceId);
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
		
		DealerBalanceDao balanceDao = DealerBalanceDao.getInstance();
		balanceDao.insert(authPO);
	}
}
