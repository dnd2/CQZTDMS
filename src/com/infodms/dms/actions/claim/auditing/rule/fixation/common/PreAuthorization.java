package com.infodms.dms.actions.claim.auditing.rule.fixation.common;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.infodms.dms.actions.claim.auditing.ClaimOrderVO;
import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.claim.auditing.ClaimAuditingDao;
import com.infodms.dms.po.TtAsWrPartsitemPO;
import com.infoservice.po3.bean.PO;

/**
 * 预授权检查
 * ：按现有数据库设计规则，只有保证预授权申请时只能申请一辆车对应的配件
 *     该规则才有效，原因由于现在预授权申请工单数据模型中标识不到配件是那种车型，而不同
 *     车型的配件代码可能相同。
 * [修改] 2010-07-20 XZM
 *        对应主要配件和主要工时，现在每个索赔单都存在多个，原来只有一个，根据新需求修改
 * @author XZM
 */
public class PreAuthorization {
	
	private ClaimAuditingDao auditingDao = ClaimAuditingDao.getInstance();
	
	/**
	 * 查询所有主要配件是否有预售权
	 * @param orderVO
	 * @param isMain
	 * @return true : 不是主要配件都有预售权
	 *         false : 主要配件都有预授权
	 */
	public boolean partHasAuth(ClaimOrderVO orderVO){
		
		boolean result = true;
		//查询该申请单需要授权的配件
		String roNo = orderVO.getClaimPO().getRoNo();//索赔申请单 工单号
		String vin = orderVO.getClaimPO().getVin();//车辆代码
		
		//查询该申请单已经授权的配件
		List<Map<String,Object>> authList = 
			this.auditingDao.queryAuthInfoByClaimId(roNo, 
					Constant.PRE_AUTH_ITEM_02.toString(), 
					Constant.PRECLAIM_AUDIT_01.toString(),
					vin,orderVO.getCompanyId());
		
		List<PO> mainPartList = orderVO.getMainPartList();
		if(mainPartList==null || mainPartList.size()==0)//如果对应索赔单不存在配件，默认有预授权 
			return false;
		
		if(authList!=null && authList.size()>0){
			int count = 0;
			Map<String,String> mainPartMap = new HashMap<String, String>();
			
			for (Map<String, Object> needAuthM : authList) {//遍历已经授权的配件
				String pc = (String)needAuthM.get("ITEM_CODE");
				mainPartMap.put(pc, pc);
			}
			
			for (PO po : mainPartList) {
				TtAsWrPartsitemPO mainPO = (TtAsWrPartsitemPO) po;
				if(mainPartMap.containsKey(mainPO.getPartCode())){//主要配件存在预售权
					count++;
				}
			}
			
			if(mainPartList.size()<=count)//所有配件都存在预授权
				result = false;
		}
		
		return result;
	}
	
	/**
	 * 配件预授权检查
	 * 注：目前只审查主要配件
	 * @param orderVO 索赔申请单信息
	 * @param isMain 是否只判断主要配件 true:判断主要配件 false:判断全部
	 * @return boolean true:需要预售权配件都已经授权
	 * 				   false:存在需要授权的配件但没有授权
	 */
	public boolean checkPart(ClaimOrderVO orderVO,boolean isMain){
		
		boolean result = false;
		
		//查询该申请单需要授权的配件
		String claimId = orderVO.getClaimPO().getId().toString();//索赔申请单ID
		String roNo = orderVO.getClaimPO().getRoNo();//索赔申请单 工单号
		String wrgroupId = orderVO.getWrPartGroupId();//索赔之配件车型组ID
		String vin = orderVO.getClaimPO().getVin();//车辆代码
		
		
		Integer isMainFlag = null;
		if(isMain)
			isMainFlag = Constant.IS_MAIN_TROUBLE;

		//查询需要预授权的配件
		List<Map<String,Object>> needAuthList = 
			this.auditingDao.queryNeedAuthPartByClaimId(claimId,wrgroupId,
					isMainFlag,orderVO.getCompanyId());
		
		if(needAuthList!=null && needAuthList.size()>0){
			
			//查询该申请单已经授权的配件
			List<Map<String,Object>> authList = 
				this.auditingDao.queryAuthInfoByClaimId(roNo, 
						Constant.PRE_AUTH_ITEM_02.toString(), 
						Constant.PRECLAIM_AUDIT_01.toString(),
						vin,orderVO.getCompanyId());
			
			if(authList!=null && authList.size()>0){
				Map<String,String> authMap = new HashMap<String, String>();
				for (Map<String, Object> authM : authList) {//遍历已经授权的配件
					String pc2 = (String)authM.get("ITEM_CODE");
					authMap.put(pc2, pc2);
				}
				//检查需要授权的配件是否都已经授权
				result = true;
				for (Map<String, Object> needAuthM : needAuthList) {//遍历需要授权的配件
					String pc = (String)needAuthM.get("PART_CODE");
					if(!authMap.containsKey(pc)){//存在某个配件不存在授权信息
						result = false;
						break;
					}
				}

			}else{//所有项都没有授权
				result = false;
			}
		}else{//不需要预售权
			result = true;
		}
		
		return result;
	}
	
	/**
	 * 工时预授权检查
	 * @param claimPO 索赔申请单信息
	 * @param isMain 是否只判断主要配件 true:判断主要配件 false:判断全部
	 * @return true:需要预售权工时都已经授权
	 * 		   false:存在需要授权的工时但没有授权
	 */
	public boolean checkManHour(ClaimOrderVO orderVO,boolean isMain){
		boolean result = false;
		
		//查询该申请单需要授权的工时
		String claimId = orderVO.getClaimPO().getId().toString();//索赔申请单ID
		String roNo = orderVO.getClaimPO().getRoNo();//索赔申请单 工单号
		String wrgroupId = orderVO.getWrGroupId();//车型组ID
		String vin = orderVO.getClaimPO().getVin();//车辆代码
		
		//情况一：查询不到车型组，预售权监控工时都是按车型组设定
		if(wrgroupId==null || "".equals(wrgroupId))//当车型组信息没有时，直接通过
			return true;
		
		Integer isMainFlag = null;
		if(isMain)
			isMainFlag = Constant.IS_MAIN_TROUBLE;
		
		//查询需要预授权的主故障工时
		List<Map<String,Object>> needAuthList = 
			this.auditingDao.queryNeedAuthManHourByClaimId(claimId,wrgroupId,isMainFlag,orderVO.getCompanyId());
		
		if(needAuthList!=null && needAuthList.size()>0){
			
			//查询该申请单已经授权的工时
			List<Map<String,Object>> authList = 
				this.auditingDao.queryAuthInfoByClaimId(roNo, 
						Constant.PRE_AUTH_ITEM_01.toString(), 
						Constant.PRECLAIM_AUDIT_01.toString(),
						vin,orderVO.getCompanyId());
			
			if(authList!=null && authList.size()>0){
				Map<String,String> authMap = new HashMap<String, String>();
				for (Map<String, Object> authM : authList) {//遍历已经授权的配件
					String pc2 = (String)authM.get("ITEM_CODE");
					authMap.put(pc2, pc2);
				}
				//检查需要授权的工时是否都已经授权
				result = true;
				for (Map<String, Object> needAuthM : needAuthList) {//遍历需要授权的工时
					String pc = (String)needAuthM.get("WR_LABOURCODE");
					if(!authMap.containsKey(pc)){//存在某个工时不存在授权信息
						result = false;
					}
				}
			}else{//所有项都没有授权
				result = false;
			}
		}else{//不需要预售权
			result = true;
		}
		
		return result;
	}
	
	/**
	 * 其他项目预授权检查
	 * @param claimPO
	 * @return
	 */
	public boolean checkOhter(ClaimOrderVO orderVO){
		boolean result = false;
		
		//查询该申请单需要授权的工时
		String claimId = orderVO.getClaimPO().getId().toString();//索赔申请单ID
		String roNo = orderVO.getClaimPO().getRoNo();//索赔申请单 工单号
		String vin = orderVO.getClaimPO().getVin();//车辆代码
		
		List<Map<String,Object>> needAuthList = 
			this.auditingDao.queryNeedAuthOtherByClaimId(claimId,orderVO.getCompanyId());
		
		if(needAuthList!=null && needAuthList.size()>0){
			
			//查询该申请单已经授权的工时
			List<Map<String,Object>> authList = 
				this.auditingDao.queryAuthInfoByClaimId(roNo, 
						Constant.PRE_AUTH_ITEM_03.toString(), 
						Constant.PRECLAIM_AUDIT_01.toString(),
						vin,orderVO.getCompanyId());
			
			if(authList!=null && authList.size()>0){
				int count = 0;
				//检查需要授权的其他项目是否都已经授权
				for (Map<String, Object> needAuthM : needAuthList) {
					String pc = (String)needAuthM.get("ITEM_CODE");
					for (Map<String, Object> authM : authList) {
						String pc2 = (String)authM.get("ITEM_CODE");
						if(pc.equals(pc2)){
							count++;
						}
					}
				}
				
				if(!result && needAuthList.size()==count){//需要授权的都已经授权
					result = true;
				}
			}else{//所有项都没有授权
				result = false;
			}
		}else{//不需要预售权
			result = true;
		}
		
		return result;
	}
}
