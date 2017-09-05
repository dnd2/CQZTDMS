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
import com.infodms.dms.dao.claim.authorization.ClaimLaborWatchDao;
import com.infodms.dms.dao.claim.preAuthorization.LabourMaintainDAO;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TtAsWrForeapprovallabExtPO;
import com.infodms.dms.po.TtAsWrForeapprovallabPO;
import com.infodms.dms.po.TtAsWrWrlabinfoPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.dms.chana.actions.OSC09;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;

public class LabourMaintain {
	private Logger logger = Logger.getLogger(LabourMaintain.class);
	private final ClaimLaborWatchDao dao = ClaimLaborWatchDao.getInstance();
	private final LabourMaintainDAO dao1 = LabourMaintainDAO.getInstance();
	private final String CLAIM_LABOR_WATCH_URL = "/jsp/claim/preAuthorization/labourMaintain.jsp";//主页面（查询）
	private final String CLAIM_LABOR_WATCH_ADD_URL = "/jsp/claim/preAuthorization/labourMaintainAdd.jsp";//新增页面
	private final String CLAIM_LABOR_MODEL_QUERY_URL ="/jsp/claim/authorization/claimLaborModelQuery.jsp";//工时选择页面
//	private final String CLAIM_PART_WATCH_UPDATE_URL = "/jsp/claim/authorization/claimPartWatchModify.jsp";//修改页面
	private final String toDispatchQueryInitURL = "/jsp/claim/preAuthorization/labourMaintain_DispatchQueryInit.jsp";//预授权维修项目维护--下发页面初始化
	private ClaimCommonAction claimCommon = new ClaimCommonAction();
	/**
	 * 
	* @Title: labourMaintainForward 
	* @Description: TODO(预授权维修项目维护) 
	* @param    
	* @return void  
	* @throws
	 */
	public void labourMaintainForward(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		Long oemCompanyId = GetOemcompanyId.getOemCompanyId(logonUser);
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
	* @Title: labourMaintainAddForward 
	* @Description: TODO(跳转到新增页面) 
	* @param     设定文件 
	* @return void    返回类型 
	* @throws
	 */
	public void labourMaintainAddForward(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			Long oemCompanyId = GetOemcompanyId.getOemCompanyId(logonUser);
			act.setOutData("wrmodelgrouplist",claimCommon.getWrModelGroupList("0",Constant.WR_MODEL_GROUP_TYPE_01,oemCompanyId));
			act.setForword(CLAIM_LABOR_WATCH_ADD_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"监控工时维护");
			logger.error(logonUser,e1);
			act.setException(e1);
		}	
	}
	
	/**
	 * 
	* @Title: claimLaborWatchQuery 
	* @Description: TODO(工时查询) 
	* @param     设定文件 
	* @return void    返回类型 
	* @throws
	 */
	public void labourQuery() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		Long oemCompanyId = GetOemcompanyId.getOemCompanyId(logonUser);
		try {
			RequestWrapper request = act.getRequest();
			act.getResponse().setContentType("application/json");
			if("1".equals(request.getParamValue("COMMAND"))){ //开始查询
				//工时代码
				String labourOperationNo = request.getParamValue("LABOUR_OPERATION_NO");//工时代码
				//工时名称
				String labourOperationName = request.getParamValue("LABOUR_OPERATION_NAME");//工时名称
				//车型组id
				String wrgroupId = request.getParamValue("WRGROUP_ID");//索赔车型组ID
				//拼sql的查询条件
							
				Integer curPage = request.getParamValue("curPage") != null ? Integer
						.parseInt(request.getParamValue("curPage"))
						: 1; // 处理当前页
						
				Map<String,String> map = new HashMap<String, String>();
				map.put("OPERATION_CODE", labourOperationNo);
				map.put("OPERATION_DESC", labourOperationName);
				map.put("WRGROUP_ID", wrgroupId);
				PageResult<TtAsWrForeapprovallabExtPO> ps = dao1.queryLabour(map,oemCompanyId, Constant.PAGE_SIZE, curPage);
				act.setOutData("ps", ps);
			}
		} catch(Exception e){
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "预授权工时");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 
	* @Title: labourDel 
	* @Description: TODO(删除工时) 
	* @param     设定文件 
	* @return void    返回类型 
	* @throws
	 */
	public void labourDel () {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
				//工时id
				String id = request.getParamValue("ID");//工时id
				TtAsWrForeapprovallabPO t = new TtAsWrForeapprovallabPO();
				t.setId(Utility.getLong(id));
				dao1.delete(t);
				act.setOutData("success", true);
		} catch(Exception e){
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "预授权工时");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 
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
			String labourCode = request.getParamValue("laborcode");//工时代码
			String cnDes = Utility.getString(request.getParamValue("laborname"));//工时名称
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
			List ps = dao.getModelGroupById(oemCompanyId,sb.toString(),params);
			act.setOutData("labourCode", labourCode);
			act.setOutData("cnDes", cnDes);
			act.setOutData("ADDLIST", ps);
			act.setOutData("MODELID", groupid);
			act.setForword(CLAIM_LABOR_MODEL_QUERY_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"监控工时维护");
			logger.error(logonUser,e1);
			act.setException(e1);
		}	
	}
	
	/**
	 * 
	* @Title: labourAdd 
	* @Description: TODO(增加工时)  
	* @param     设定文件 
	* @return void    返回类型 
	* @throws
	 */
	public void labourAdd() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		Long oemCompanyId = GetOemcompanyId.getOemCompanyId(logonUser);
		TtAsWrWrlabinfoPO selpo = null;
		TtAsWrForeapprovallabPO addpo = null;
		String errorCode = null;
		String errorExist = null;
		try {
			RequestWrapper request = act.getRequest();
			
			String wrgroupId = request.getParamValue("WRGROUP_ID");//工时代码
			String labourCode = request.getParamValue("LABOUR_OPERATION_NO");//工时代码
			String cnDes = request.getParamValue("LABOUR_OPERATION_NAME");//工时名称
			selpo = new TtAsWrWrlabinfoPO();
			selpo.setWrgroupId(Utility.getLong(wrgroupId));
			selpo.setLabourCode(labourCode);
			selpo.setIsDel(Utility.getInt(Constant.IS_DEL_00));
			List sellist = dao.select(selpo);
			if(sellist != null && sellist.size() > 0){
				List existlist = dao1.getExistPO(wrgroupId, labourCode);
				if(existlist != null && existlist.size() > 0){
					errorExist = labourCode;
				}else{
					addpo = new TtAsWrForeapprovallabPO();
					//构造授权级别，多个用“,”分隔
					//索赔授权监控工时表添加一条记录
					addpo.setCreateBy(logonUser.getUserId());
					addpo.setCreateDate(new Date());
					addpo.setId(Long.parseLong(SequenceManager.getSequence("")));
					addpo.setOperationDesc(cnDes);//索赔工时名称
					addpo.setOperationCode(labourCode);//索赔工时代码
					addpo.setWrgroupId(Utility.getLong(wrgroupId));
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
	 * 预授权维修项目维护--下发页面初始化
	 */
	public void toDispatchQueryInit(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
			Long oemCompanyId = GetOemcompanyId.getOemCompanyId(logonUser);
			act.setOutData("wrmodelgrouplist",claimCommon.getWrModelGroupList("1",Constant.WR_MODEL_GROUP_TYPE_01,oemCompanyId));
			act.setForword(toDispatchQueryInitURL);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ADD_FAILURE_CODE,"预授权维修项目维护--下发页面初始化");
			logger.error(logonUser,e1);
			act.setException(e1);
		}	
	}
	/**
	 * 预授权维修项目维护--查询可下发项目
	 */
	public void toLabourDispatchQuery(){
		AclUserBean logonUser = null;
	    ActionContext act = ActionContext.getContext();
		try {
			RequestWrapper request = act.getRequest();
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);	
			Long oemCompanyId = GetOemcompanyId.getOemCompanyId(logonUser);
			String labourOperationNo = CommonUtils.checkNull(request.getParamValue("LABOUR_OPERATION_NO"));    //工时代码
			String labourOperationName = CommonUtils.checkNull(request.getParamValue("LABOUR_OPERATION_NAME"));//工时名称
			String wrgroupId = CommonUtils.checkNull(request.getParamValue("WRGROUP_ID")) ;                    //索赔车型组ID
						
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1; // 处理当前页
			PageResult<Map<String,Object>> ps = LabourMaintainDAO.getCanLabourDispatchList(oemCompanyId,labourOperationNo,labourOperationName,wrgroupId, Constant.PAGE_SIZE, curPage);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ADD_FAILURE_CODE,"预授权维修项目维护--查询可下发项目");
			logger.error(logonUser,e1);
			act.setException(e1);
		}	
	}
	/**
	 * 预授权维修项目维护--下发提交
	 */
	public void labourDispatch(){
		AclUserBean logonUser = null;
	    ActionContext act = ActionContext.getContext();
	    try {
	    	RequestWrapper request = act.getRequest();
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);	
			String ids = CommonUtils.checkNull(request.getParamValue("ids"));    
			if (null != ids && !"".equals(ids)) {
				String[] ids__ =  ids.split(",");
				for (int i = 0; i < ids__.length; i++) {
					TtAsWrForeapprovallabPO tempPO = new TtAsWrForeapprovallabPO();
					tempPO.setId(Long.parseLong(ids__[i]));
					TtAsWrForeapprovallabPO valuePO = new TtAsWrForeapprovallabPO();
					valuePO.setIsSend(Constant.DOWNLOAD_CODE_STATUS_03);
					valuePO.setUpdateDate(new Date());
					dao.update(tempPO, valuePO);
				}
				//接口发送数据 Start
				OSC09 os = new OSC09();
				os.execute(ids);
				//接口发送数据 End
				act.setOutData("returnValue", 1);
			}
			
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ADD_FAILURE_CODE,"预授权维修项目维护--下发提交");
			logger.error(logonUser,e1);
			act.setException(e1);
		}	
	}
}
