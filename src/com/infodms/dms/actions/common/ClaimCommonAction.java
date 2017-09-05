/**   
* @Title: ClaimCommonAction.java 
* @Package com.infodms.dms.actions.common 
* @Description: TODO(售后索赔公共Action) 
* @author wangjinbao   
* @date 2010-6-4 上午10:07:43 
* @version V1.0   
*/
package com.infodms.dms.actions.common;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


import com.infodms.dms.dao.common.ClaimUtilDao;

/** 
 * @ClassName: ClaimCommonAction 
 * @Description: TODO(售后索赔公共Action) 
 * @author wangjinbao 
 * @date 2010-6-4 上午10:07:43 
 *  
 */
public class ClaimCommonAction {
	private final ClaimUtilDao dao = ClaimUtilDao.getInstance();
	private static final ClaimCommonAction action = new ClaimCommonAction ();
	public static final ClaimCommonAction getInstance() {
		return action;
	}
	/**
	 * 
	* @Title: getWrModelGroupList 
	* @Description: TODO(获得索赔车型组下了框) 
	* @param @param type           ：是否加入"请选择"：“1”(加入)
	* @param @param oemCompanyId   ：对应的公司ID
	* @param @return   
	* @return String  
	* @throws
	 */
	public String getWrModelGroupList(String type,Integer typeid,Long oemCompanyId){
		List list = dao.getClaimWrModelGroup(typeid,oemCompanyId);
		String retStr="";
		if("1".equals(type)){
			retStr+="<option value=\'\'>-请选择-</option>";
		}
		if(list != null && list.size() > 0){
			for(int i=0;i<list.size();i++){
				HashMap map = new HashMap();
				map=(HashMap)list.get(i);
				retStr+="<option value=\'"+map.get("WRGROUP_ID")+"\'>"+map.get("WRGROUP_CODE")+" - "+map.get("WRGROUP_NAME")+"</option>";
			}
		}
		return retStr;
	}
	/**
	 * 
	* @Title: getWrModelGroupListCallBack 
	* @Description: TODO(获得索赔车型组下了框回显) 
	* @param @param id             ：对应选择的车型组id
	* @param @param typeid         ：对应车型组类型
	* @param @param oemCompanyId   ：对应的公司ID
	* @param @return   
	* @return String  
	* @throws
	 */
	public String getWrModelGroupListCallBack(String id,Integer typeid,Long oemCompanyId){
		List list = dao.getClaimWrModelGroup(typeid,oemCompanyId);
		String retStr="";
		if(list != null && list.size() > 0){
			for(int i=0;i<list.size();i++){
				HashMap map = new HashMap();
				map=(HashMap)list.get(i);
				retStr+="<option value=\'"+map.get("WRGROUP_ID")+"\' ";
				if(id.equals(map.get("WRGROUP_ID").toString())){
					retStr+=" selected ";
				}
				retStr+=" >"+map.get("WRGROUP_CODE")+"</option>";
			}
		}
		return retStr;
	}
	/**
	 * 
	* @Title: getWrLevelList 
	* @Description: TODO(获得售后授权级别下拉框) 
	* @param @param type
	* @param @return   
	* @return String  
	* @throws
	 */
	public String getWrLevelList(String type, String flag,Long oemCompanyId){
		List list = dao.getClaimWrLevel(oemCompanyId, flag);
		String retStr="";
		if("1".equals(type)){
			retStr+="<option value=\'\'>-请选择-</option>";
		}
		if(list != null && list.size() > 0){
			for(int i=0;i<list.size();i++){
				HashMap map = new HashMap();
				map=(HashMap)list.get(i);
				retStr+="<option value=\'"+map.get("APPROVAL_LEVEL_CODE")+"\'>"+map.get("APPROVAL_LEVEL_NAME")+"</option>";
			}
		}
		return retStr;
	}
	/**
	 * 
	* @Title: getWrLevelListCallBack 
	* @Description: TODO(获得售后授权级别下拉框回显) 
	* @param @param code
	* @param @return   
	* @return String  
	* @throws
	 */
	public String getWrLevelListCallBack(String code,String auditFlag, Long oemCompanyId){
		List list = dao.getClaimWrLevel(oemCompanyId, auditFlag);
		String retStr="";
		if(list != null && list.size() > 0){
			for(int i=0;i<list.size();i++){
				HashMap map = new HashMap();
				map=(HashMap)list.get(i);
				retStr+="<option value=\'"+map.get("APPROVAL_LEVEL_CODE")+"\' ";
				if(code.equals(map.get("APPROVAL_LEVEL_CODE"))){
					retStr+=" selected ";
				}
				retStr+=" >"+map.get("APPROVAL_LEVEL_NAME")+"</option>";
			}
		}
		return retStr;
	}
	/**
	 * 
	* @Title: getDealerMap 
	* @Description: TODO(根据经销商ID获得经销商信息) 
	* @param @param dealerId     ：经销商ID
	* @param @return   
	* @return Map<String,Object>  
	* @throws 
	 */
	public Map<String, Object> getDealerMap(String dealerId){
		
		return dao.getDealerMap(dealerId);
	}
	
	/**
	 * 
	* @Title: getUserMap 
	* @Description: TODO(根据用户ID获得用户信息) 
	* @param @param userId
	* @param @return   
	* @return Map<String,Object>  
	* @throws
	 */
	public Map<String, Object> getUserMap(Long userId){
		
		return dao.getUserInfo(userId);
	}
	
	

}
