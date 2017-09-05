package com.infodms.dms.actions.claim.preAuthorization;

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
import com.infodms.dms.dao.claim.authorization.ClaimPartWatchDao;
import com.infodms.dms.dao.claim.preAuthorization.PartMaintainDAO;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TtAsWrForeapprovalptPO;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.dms.chana.actions.OSC10;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;

public class PartMaintain {
	private Logger logger = Logger.getLogger(PartMaintain.class);
	private final ClaimPartWatchDao dao = ClaimPartWatchDao.getInstance();
	private final PartMaintainDAO dao1 = PartMaintainDAO.getInstance();
	private final String CLAIM_PART_WATCH_URL = "/jsp/claim/preAuthorization/partMaintain.jsp";//主页面（查询）
	private final String toDispatchPartQueryInitURL = "/jsp/claim/preAuthorization/partMaintain_DispatchInit.jsp";//配件下发初始页面
	private final String CLAIM_PART_WATCH_ADD_URL = "/jsp/claim/preAuthorization/partMaintainAdd.jsp";//新增页面
	private final String CLAIM_PART_MODEL_QUERY_URL ="/jsp/claim/authorization/claimPartModelQuery.jsp";//工时选择页面
	private ClaimCommonAction claimCommon = new ClaimCommonAction();
	/**
	 * 
	* @Title: partMaintainForward 
	* @Description: TODO(配件维护跳转) 
	* @param     设定文件 
	* @return void    返回类型 
	* @throws
	 */
	public void partMaintainForward() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			ClaimCommonAction claimCommon = ClaimCommonAction.getInstance();
			Long oemCompanyId = GetOemcompanyId.getOemCompanyId(logonUser);
			act.setOutData("wrmodelgrouplist",claimCommon.getWrModelGroupList("1",Constant.WR_MODEL_GROUP_TYPE_02,oemCompanyId));//配件车型组
			act.setForword(CLAIM_PART_WATCH_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"监控配件维护");
			logger.error(logonUser,e1);
			act.setException(e1);
		}		
	}

	/**
	 * 
	* @Title: claimPartWatchQuery 
	* @Description: TODO(配件维护查询) 
	* @param    
	* @return void  
	* @throws
	 */
	public void claimPartWatchQuery() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		Long oemCompanyId = GetOemcompanyId.getOemCompanyId(logonUser);
		List<Object> params = new LinkedList<Object>();
		StringBuffer sb = new StringBuffer();
		Map <String,String> map = new HashMap<String,String>();
		try {
			RequestWrapper request = act.getRequest();
			act.getResponse().setContentType("application/json");
			if("1".equals(request.getParamValue("COMMAND"))){ //开始查询
				//配件代码
				String partCode = request.getParamValue("PART_CODE");//配件代码
				//配件名称
				String partName = request.getParamValue("PART_NAME");//配件名称
				String wrgroupId = request.getParamValue("WRGROUP_ID");//索赔配件车型组ID
				
				if (Utility.testString(partCode)) {
					map.put("partCode",partCode );
				}
				if (Utility.testString(partName)) {
					map.put("partName", partName);
				}
				if (Utility.testString(wrgroupId)) {
					map.put("wrGroupId", wrgroupId);
				}
				
				Integer curPage = request.getParamValue("curPage") != null ? Integer
						.parseInt(request.getParamValue("curPage"))
						: 1; // 处理当前页
				PageResult<Map<String,Object>> ps = dao1.claimPartWatchQuery(oemCompanyId,map,Constant.PAGE_SIZE, curPage,sb.toString(),params);
				act.setOutData("ps", ps);
			}
		} catch(BizException e){
			logger.error(logonUser,e);
			act.setException(e);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"监控配件维护");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 
	* @Title: toDispatchQueryInit 
	* @Description: TODO(配件维护：查询可下发的配件页面初始化) 
	* @param    
	* @return void  
	* @throws
	 */
	public void toDispatchQueryInit() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		List<Object> params = new LinkedList<Object>();
		StringBuffer sb = new StringBuffer();
		Map <String,String> map = new HashMap<String,String>();
		try {
			RequestWrapper request = act.getRequest();
			act.getResponse().setContentType("application/json");
			Long oemCompanyId = GetOemcompanyId.getOemCompanyId(logonUser);
			act.setOutData("wrmodelgrouplist",claimCommon.getWrModelGroupList("1",Constant.WR_MODEL_GROUP_TYPE_02,oemCompanyId));//配件车型组
			act.setForword(toDispatchPartQueryInitURL);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"配件维护：查询可下发的配件页面初始化");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 
	* @Title: toDispatchQuery
	* @Description: TODO(配件维护：查询可下发的配件) 
	* @param    
	* @return void  
	* @throws
	 */
	public void toDispatchQuery() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		Long oemCompanyId = GetOemcompanyId.getOemCompanyId(logonUser);
		List<Object> params = new LinkedList<Object>();
		StringBuffer sb = new StringBuffer();
		Map <String,String> map = new HashMap<String,String>();
		try {
			RequestWrapper request = act.getRequest();
			act.getResponse().setContentType("application/json");
			if("1".equals(request.getParamValue("COMMAND"))){ //开始查询
				//配件代码
				String partCode = request.getParamValue("PART_CODE");//配件代码
				//配件名称
				String partName = request.getParamValue("PART_NAME");//配件名称
				String wrgroupId = request.getParamValue("WRGROUP_ID");//索赔配件车型组ID
				
				if (Utility.testString(partCode)) {
					map.put("partCode",partCode );
				}
				if (Utility.testString(partName)) {
					map.put("partName", partName);
				}
				if (Utility.testString(wrgroupId)) {
					map.put("wrGroupId", wrgroupId);
				}
				
				Integer curPage = request.getParamValue("curPage") != null ? Integer
						.parseInt(request.getParamValue("curPage"))
						: 1; // 处理当前页
				PageResult<Map<String,Object>> ps = dao1.claimPartDispatchQueryInit(oemCompanyId,map,Constant.PAGE_SIZE, curPage,sb.toString(),params);
				act.setOutData("ps", ps);
			}
		} catch(BizException e){
			logger.error(logonUser,e);
			act.setException(e);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"监控配件维护");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 
	* @Title: claimPartWatchAddForward 
	* @Description: TODO(监控配件维护新增初始化) 
	* @param    
	* @return void  
	* @throws
	 */
	public void claimPartWatchAddForward(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			ClaimCommonAction claimCommon = ClaimCommonAction.getInstance();
			Long oemCompanyId = GetOemcompanyId.getOemCompanyId(logonUser);
			act.setOutData("wrmodelgrouplist",claimCommon.getWrModelGroupList("0",Constant.WR_MODEL_GROUP_TYPE_02,oemCompanyId));
			act.setForword(CLAIM_PART_WATCH_ADD_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"监控配件维护");
			logger.error(logonUser,e1);
			act.setException(e1);
		}		
		
	}
	/**
	 * 
	* @Title: claimPartSelect 
	* @Description: TODO(配件查询) 
	* @param    
	* @return void  
	* @throws
	 */
	public void claimPartSelect(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		Long oemCompanyId = GetOemcompanyId.getOemCompanyId(logonUser);
		List<Object> params = new LinkedList<Object>();
		StringBuffer sb = new StringBuffer();
		try {
			RequestWrapper request = act.getRequest();
			String groupid = request.getParamValue("ID");
			String partCode = request.getParamValue("partcode");//配件代码
			String partName = Utility.getString(request.getParamValue("partname"));//配件名称
//			if(Utility.testString(groupid)){
//				sb.append(" and tawmi.wrgroup_id  = ? \n");
//				params.add(groupid);
//			}			
			if(Utility.testString(partCode)){
				sb.append(" and tppb.part_code like ? \n");
				params.add("%"+partCode+"%");
			}
			if(Utility.testString(partName)){
				sb.append(" and tppb.part_name like ? \n");
				params.add("%"+partName+"%");
			}			
			List ps = dao.getPartById(oemCompanyId,sb.toString(),params);
			act.setOutData("ADDLIST", ps);
			act.setOutData("MODELID", groupid);
			act.setForword(CLAIM_PART_MODEL_QUERY_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"监控配件维护");
			logger.error(logonUser,e1);
			act.setException(e1);
		}	
	}
	/**
	 * 
	* @Title: claimPartWatchAdd 
	* @Description: TODO(配件维护新增) 
	* @param    
	* @return void  
	* @throws
	 */
	@SuppressWarnings("unchecked")
	public void claimPartWatchAdd(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		Long oemCompanyId = GetOemcompanyId.getOemCompanyId(logonUser);
		TtAsWrForeapprovalptPO addpo = null;
		String errorCode = null;
		String errorExist = null;
		try {
			RequestWrapper request = act.getRequest();
			
			String partCode = request.getParamValue("PART_CODE");//配件代码
			String partName = request.getParamValue("PART_NAME");//配件名称
			String wrgroupId = request.getParamValue("WRGROUP_ID");//配件车型组id
			
			List existlist = dao1.queryMonitorPart(wrgroupId, partCode);
			if(existlist==null || existlist.size()<1){
				addpo = new TtAsWrForeapprovalptPO();
						
				//预授权监控配件表添加一条记录
				addpo.setCreateBy(logonUser.getUserId());
				addpo.setCreateDate(new Date());
				addpo.setId(Long.parseLong(SequenceManager.getSequence("")));
				addpo.setPartName(partName);//配件名称
				addpo.setPartCode(partCode);//配件代码
				addpo.setModelGroup(Long.parseLong(wrgroupId));
				addpo.setOemCompanyId(oemCompanyId);
				dao1.insert(addpo);
			}else{//对应配件已经设置过预授权监控
				errorExist = partCode;
			}

			act.setOutData("errorCode", errorCode);//配件不在选择的车型组中
			act.setOutData("errorExist", errorExist);//配件数据库已经存在
			act.setOutData("success", true);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ADD_FAILURE_CODE,"监控配件维护");
			logger.error(logonUser,e1);
			act.setException(e1);
		}		
		
	}
	
	/**
	 * 
	* @Title: claimPartWatchDel 
	* @Description: TODO(配件维护逻辑删除) 
	* @param    
	* @return void  
	* @throws
	 */
	@SuppressWarnings("unchecked")
	public void claimPartWatchDel(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		TtAsWrForeapprovalptPO selpo = null;
		try {
			RequestWrapper request = act.getRequest();
			String id = request.getParamValue("ID");//监控配件记录ID
			selpo = new TtAsWrForeapprovalptPO();
			selpo.setId(Utility.getLong(id));
			dao1.delete(selpo);
			act.setOutData("success", true);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.DELETE_FAILURE_CODE,"监控配件维护");
			logger.error(logonUser,e1);
			act.setException(e1);
		}		
	}
	
	/*
	 * 预授权监控配件维护:下发提交
	 * */
	public void partDispatch(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			String ids = request.getParamValue("ids");
			if (null != ids && !"".equals(ids)) {
				String[] ids__ = ids.split(",");
				for (int i = 0; i < ids__.length; i++) {
					TtAsWrForeapprovalptPO tempPO = new TtAsWrForeapprovalptPO();
					tempPO.setId(Long.parseLong(ids__[i]));
					TtAsWrForeapprovalptPO valuePO = new TtAsWrForeapprovalptPO();
					valuePO.setIsSend(Constant.DOWNLOAD_CODE_STATUS_03);
					valuePO.setUpdateDate(new Date());
					dao.update(tempPO, valuePO);
				}
				//接口发送数据 Start
				OSC10 os = new OSC10();
				os.execute(ids);
				//接口发送数据 End
			}
			act.setOutData("returnValue", 1);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.DELETE_FAILURE_CODE,"监控配件维护");
			logger.error(logonUser,e1);
			act.setException(e1);
		}	
	}
}
