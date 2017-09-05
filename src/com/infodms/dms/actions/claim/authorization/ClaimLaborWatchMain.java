/**   
* @Title: ClaimLaborWatchMain.java 
* @Package com.infodms.dms.actions.claim.authorization 
* @Description: TODO(监控工时维护Action) 
* @author Administrator   
* @date 2010-6-8 下午02:34:50 
* @version V1.0   
*/
package com.infodms.dms.actions.claim.authorization;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.actions.common.ClaimCommonAction;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.common.getCompanyId.GetOemcompanyId;
import com.infodms.dms.dao.claim.authorization.ClaimLaborWatchDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TtAsWrAuthmonitorlabPO;
import com.infodms.dms.po.TtAsWrWrlabinfoPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;

/** 
 * @ClassName: ClaimLaborWatchMain 
 * @Description: TODO(监控工时维护Action) 
 * @author Administrator 
 * @date 2010-6-8 下午02:34:50 
 *  
 */
public class ClaimLaborWatchMain {
	private Logger logger = Logger.getLogger(ClaimLaborWatchMain.class);
	private final ClaimLaborWatchDao dao = ClaimLaborWatchDao.getInstance();
	private final String CLAIM_LABOR_WATCH_URL = "/jsp/claim/authorization/claimLaborWatchIndex.jsp";//主页面（查询）
	private final String CLAIM_LABOR_WATCH_ADD_URL = "/jsp/claim/authorization/claimLaborWatchAdd.jsp";//新增页面
	private final String CLAIM_LABOR_MODEL_QUERY_URL ="/jsp/claim/authorization/claimLaborModelQuery.jsp";//工时选择页面
	private final String CLAIM_LABOR_WATCH_UPDATE_URL = "/jsp/claim/authorization/claimLaborWatchModify.jsp";//修改页面
	private final ClaimCommonAction claimCommon = ClaimCommonAction.getInstance();
	/**
	 * 
	* @Title: claimLaborWatchInit 
	* @Description: TODO(监控工时维护初始化) 
	* @param    
	* @return void  
	* @throws
	 */
	public void claimLaborWatchInit(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		Long oemCompanyId = GetOemcompanyId.getOemCompanyId(logonUser);
		List list = dao.getLevelList(oemCompanyId,Constant.AUDIT_TYPE_01.toString());
		act.setOutData("LEVELLIST", list);
		act.setOutData("wrmodelgrouplist",claimCommon.getWrModelGroupList("1",Constant.WR_MODEL_GROUP_TYPE_01,oemCompanyId));
		
		try {
			act.setForword(CLAIM_LABOR_WATCH_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"监控工时维护");
			logger.error(logonUser,e1);
			act.setException(e1);
		}		
		
	}
	/**
	 * 
	* @Title: claimLaborWatchQuery 
	* @Description: TODO(监控工时维护查询) 
	* @param    
	* @return void  
	* @throws
	 */
	public void claimLaborWatchQuery() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		
		List<Object> params = new LinkedList<Object>();
		StringBuffer sb = new StringBuffer();
		try {
			RequestWrapper request = act.getRequest();
			act.getResponse().setContentType("application/json");
			if("1".equals(request.getParamValue("COMMAND"))){ //开始查询
				
				// 从session中取得公司ID,add by zouchao 2010-07-19 begin
				Long oemCompanyId = GetOemcompanyId.getOemCompanyId(logonUser);
				
				//工时代码
				String labourOperationNo = request.getParamValue("LABOUR_OPERATION_NO");//工时代码
				//工时名称
				String labourOperationName = request.getParamValue("LABOUR_OPERATION_NAME");//工时名称
				//车型组id
				String wrgroupId = request.getParamValue("WRGROUP_ID");//索赔车型组ID
				String delFlag = request.getParamValue("selDelFlag") ;
				//拼sql的查询条件
				if (Utility.testString(labourOperationNo)) {
					sb.append(" and tawal.labour_operation_no like ? ");
					params.add("%"+labourOperationNo+"%");
				}
				if (Utility.testString(labourOperationName)) {
					sb.append(" and tawal.labour_operation_name like ? ");
					params.add("%"+labourOperationName+"%");
				}
				if (Utility.testString(wrgroupId)) {
					sb.append(" and tawal.model_group = ? ");
					params.add(wrgroupId);
				}		
				
				if (Utility.testString(delFlag)) {
					sb.append(" and tawal.is_del = ? ");
					params.add(delFlag);
				}		
				Integer curPage = request.getParamValue("curPage") != null ? Integer
						.parseInt(request.getParamValue("curPage"))
						: 1; // 处理当前页
				PageResult<Map<String, Object>> ps = dao.claimLaborWatchQuery(Constant.PAGE_SIZE, curPage,sb.toString(),params,oemCompanyId);
				act.setOutData("ps", ps);
			}
		} catch(BizException e){
			logger.error(logonUser,e);
			act.setException(e);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"监控工时维护");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 
	* @Title: claimLaborWatchAddInit 
	* @Description: TODO(监控工时维护新增初始化) 
	* @param    
	* @return void  
	* @throws
	 */
	public void claimLaborWatchAddInit(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			Long oemCompanyId = GetOemcompanyId.getOemCompanyId(logonUser);
			List list = dao.getLevelList(oemCompanyId,Constant.AUDIT_TYPE_01.toString());
			act.setOutData("LEVELLIST", list);
			act.setOutData("wrmodelgrouplist",claimCommon.getWrModelGroupList("0",Constant.WR_MODEL_GROUP_TYPE_01,oemCompanyId));
			act.setForword(CLAIM_LABOR_WATCH_ADD_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"监控工时维护");
			logger.error(logonUser,e1);
			act.setException(e1);
		}		
		
	}
	/**
	 * 监控工时新增时,选择工时
	* @Title: claimLaborSelect 
	* @Description: TODO(索赔工时查询) 
	* @param    
	* @return void  
	* @throws
	 */
	public void claimLaborSelect(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		Long oemCompanyId = GetOemcompanyId.getOemCompanyId(logonUser);
		List<Object> params = new LinkedList<Object>();
		StringBuffer sb = new StringBuffer();
		try {
			RequestWrapper request = act.getRequest();
			String groupid = request.getParamValue("ID");
			//String labourCode = request.getParamValue("laborcode");//工时代码
			//String cnDes = Utility.getString(request.getParamValue("laborname"));//工时名称
			if(Utility.testString(groupid)){
				sb.append(" and taww.wrgroup_id = ? \n");
				params.add(groupid);
			}			
			//if(Utility.testString(labourCode)){
				//sb.append(" and taww.labour_code like ? \n");
				//params.add("%"+labourCode+"%");
			//}
			//if(Utility.testString(cnDes)){
				//sb.append(" and taww.cn_des like ? \n");
				//params.add("%"+cnDes+"%");
			//}			
			params.add(groupid);
			//List ps = dao.getModelGroupById2(oemCompanyId,sb.toString(),params);
			//act.setOutData("labourCode", labourCode);
			//act.setOutData("cnDes", cnDes);
			//act.setOutData("ADDLIST", ps);
			act.setOutData("MODELID", groupid);
			act.setForword(CLAIM_LABOR_MODEL_QUERY_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"监控工时维护");
			logger.error(logonUser,e1);
			act.setException(e1);
		}	
	}
	
	
	public void claimLaborSelect11(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		Long oemCompanyId = GetOemcompanyId.getOemCompanyId(logonUser);
		List<Object> params = new LinkedList<Object>();
		StringBuffer sb = new StringBuffer();
		try {
			RequestWrapper request = act.getRequest();
			act.getResponse().setContentType("application/json");
			String groupid = request.getParamValue("ID");
			String labourCode = request.getParamValue("laborcode");//工时代码
			String cnDes = Utility.getString(request.getParamValue("laborname"));//工时名称
			params.add(groupid);
			if(Utility.testString(groupid)){
				sb.append(" and taww.wrgroup_id = ? \n");
				params.add(groupid);
			}			
			if(Utility.testString(labourCode)){
				sb.append(" and taww.labour_code like ? \n");
				params.add("%"+labourCode+"%");
			}
			if(Utility.testString(cnDes)){
				sb.append(" and taww.cn_des like ? \n");
				params.add("%"+cnDes+"%");
			}			
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1; // 处理当前页
			PageResult<Map<String, Object>> ps = dao.getModelGroupById2(oemCompanyId,sb.toString(),params,Constant.PAGE_SIZE,curPage);
			act.setOutData("labourCode", labourCode);
			act.setOutData("cnDes", cnDes);
			act.setOutData("ADDLIST", ps);
			act.setOutData("MODELID", groupid);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"监控工时维护");
			logger.error(logonUser,e1);
			act.setException(e1);
		}	
	}
	/**
	 * 
	* @Title: claimLaborWatchAdd 
	* @Description: TODO(监控工时维护新增) 
	* @param    
	* @return void  
	* @throws
	 */
	@SuppressWarnings("unchecked")
	public void claimLaborWatchAdd(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		TtAsWrWrlabinfoPO selpo = null;
		TtAsWrAuthmonitorlabPO addpo = null;
		String errorCode = null;
		String errorExist = null;
		try {
			RequestWrapper request = act.getRequest();
			// 从session中取得公司ID,add by zouchao 2010-07-19 begin
			Long oemCompanyId = GetOemcompanyId.getOemCompanyId(logonUser);
			// 2010-07-19 end			
			List list = dao.getLevelList(oemCompanyId,Constant.AUDIT_TYPE_01.toString());//授权级别列表
			String[] level = new String[list.size()];
			for(int i=0; i<list.size(); i++){
				HashMap levlmap = (HashMap)list.get(i);
				level[i] = CommonUtils.checkNull(request.getParamValue(levlmap.get("APPROVAL_LEVEL_CODE").toString()));  //获得索赔基本参数对应的value
			}
			

			String wrgroupId = request.getParamValue("WRGROUP_ID");//工时代码
			String labourCode = request.getParamValue("LABOUR_OPERATION_NO");//工时代码
			String cnDes = request.getParamValue("LABOUR_OPERATION_NAME");//工时名称
			selpo = new TtAsWrWrlabinfoPO();
			selpo.setWrgroupId(Utility.getLong(wrgroupId));
			selpo.setLabourCode(labourCode);
			selpo.setIsDel(Utility.getInt(Constant.IS_DEL_00));
			//判断车型组和索赔工时是否关联
			List sellist = dao.select(selpo);
			if(sellist != null && sellist.size() > 0){
				//判断该工时是否在监控工时中维护了
				List existlist = dao.getExistPO(wrgroupId, labourCode);
				if(existlist != null && existlist.size() > 0){
					errorExist = labourCode;
				}else{
					addpo = new TtAsWrAuthmonitorlabPO();
					//构造授权级别，多个用“,”分隔
					String levelStr = "";
					for(int i=0;i<level.length - 1;i++){
						if(Utility.testString(level[i])){
							levelStr += level[i];
						}
						if(Utility.testString(level[i+1])){
							levelStr += ",";
						}
					}
					if(Utility.testString(level[level.length - 1])){
						levelStr += level[level.length - 1];
					}
					//索赔授权监控工时表添加一条记录
					addpo.setApprovalLevel(levelStr);//授权级别
					addpo.setCreateBy(logonUser.getUserId());
					addpo.setCreateDate(new Date());
					addpo.setId(Long.parseLong(SequenceManager.getSequence("")));
					addpo.setLabourOperationName(cnDes);//索赔工时名称
					addpo.setLabourOperationNo(labourCode);//索赔工时代码
					addpo.setModelGroup(Utility.getLong(wrgroupId));
					addpo.setOemCompanyId(oemCompanyId);
					
					dao.insert(addpo);
				}
			}else{
				errorCode = labourCode;
			}
			act.setOutData("errorCode", errorCode);//索赔工时不在选择的车型组中
			act.setOutData("errorExist", errorExist);//索赔工时已经维护过
			act.setOutData("success", "true");
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ADD_FAILURE_CODE,"监控工时维护");
			logger.error(logonUser,e1);
			act.setException(e1);
		}		
		
	}
	/**
	 * 
	* @Title: claimLaborWatchUpdateInit 
	* @Description: TODO(监控工时维护修改初始化) 
	* @param    
	* @return void  
	* @throws
	 */
	public void claimLaborWatchUpdateInit(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			Long oemCompanyId = GetOemcompanyId.getOemCompanyId(logonUser);
			List list = dao.getLevelList(oemCompanyId,Constant.AUDIT_TYPE_01.toString());//授权级别列表
			String id=request.getParamValue("ID");
			HashMap hm = null;
			hm = dao.claimLaborWatchQueryById(id,oemCompanyId);
			act.setOutData("LEVELLIST", list);//授权级别列表
			act.setOutData("SELMAP", hm);//对应的监控工时
			act.setForword(CLAIM_LABOR_WATCH_UPDATE_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"监控工时维护");
			logger.error(logonUser,e1);
			act.setException(e1);
		}		
		
	}
	
	/**
	 * 
	* @Title: claimLaborWatchUpdate 
	* @Description: TODO(监控工时维护修改) 
	* @param    
	* @return void  
	* @throws
	 */
	@SuppressWarnings("unchecked")
	public void claimLaborWatchUpdate(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		TtAsWrAuthmonitorlabPO selpo = null;
		TtAsWrAuthmonitorlabPO updatepo = null;
		try {
			RequestWrapper request = act.getRequest();
			Long oemCompanyId = GetOemcompanyId.getOemCompanyId(logonUser);
			List list = dao.getLevelList(oemCompanyId,Constant.AUDIT_TYPE_01.toString());//授权级别列表
			String[] level = new String[list.size()];
			for(int i=0; i<list.size(); i++){
				HashMap levlmap = (HashMap)list.get(i);
				level[i] = CommonUtils.checkNull(request.getParamValue(levlmap.get("APPROVAL_LEVEL_CODE").toString()));  //获得索赔基本参数对应的value
			}
			String id = request.getParamValue("ID");//监控工时记录ID
			selpo = new TtAsWrAuthmonitorlabPO();
			selpo.setId(Utility.getLong(id));
			
			updatepo = new TtAsWrAuthmonitorlabPO();
			//构造授权级别，多个用“,”分隔
			String levelStr = "";
			for(int i=0;i<level.length - 1;i++){
				if(Utility.testString(level[i])){
					levelStr += level[i];
				}
				if(Utility.testString(level[i+1])){
					levelStr += ",";
				}
			}
			if(Utility.testString(level[level.length - 1])){
				levelStr += level[level.length - 1];
			}
			//索赔授权监控工时表修改po  
			updatepo.setApprovalLevel(levelStr);//授权级别
			updatepo.setUpdateBy(logonUser.getUserId());
			updatepo.setUpdateDate(new Date());
			dao.update(selpo, updatepo);
			act.setOutData("success", "true");
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.UPDATE_FAILURE_CODE,"监控工时维护");
			logger.error(logonUser,e1);
			act.setException(e1);
		}		
		
	}
	/**
	 * 
	* @Title: claimLaborWatchDel 
	* @Description: TODO(监控工时维护逻辑删除) 
	* @param    
	* @return void  
	* @throws
	 */
	@SuppressWarnings("unchecked")
	public void claimLaborWatchDel(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		TtAsWrAuthmonitorlabPO selpo = null;
		TtAsWrAuthmonitorlabPO updatepo = null;
		try {
			RequestWrapper request = act.getRequest();
			String id = request.getParamValue("ID");//监控工时记录ID
			String delFlag = request.getParamValue("labelDelFlag");
			
			if(Constant.IS_DEL_01.equals(delFlag)) {
				delFlag = Constant.IS_DEL_00 ;
			} else {
				delFlag = Constant.IS_DEL_01 ;
			}
			
			selpo = new TtAsWrAuthmonitorlabPO();
			selpo.setId(Utility.getLong(id));
			
			updatepo = new TtAsWrAuthmonitorlabPO();

			//索赔授权监控工时表修改po
			updatepo.setIsDel(Integer.parseInt(delFlag));//逻辑删除表识：1
			updatepo.setUpdateBy(logonUser.getUserId());
			updatepo.setUpdateDate(new Date());
			dao.update(selpo, updatepo);
			act.setOutData("success", "true");
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.DELETE_FAILURE_CODE,"监控工时维护");
			logger.error(logonUser,e1);
			act.setException(e1);
		}		
		
	}
	

}
