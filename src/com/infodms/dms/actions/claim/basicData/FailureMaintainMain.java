/*
   * Title: FailureMaintainMain
   * Description: 严重安全性能故障模式
   * Copyright: Copyright (c) 2013
   * Company: 用友信息科技(上海)有限公司
   * Previous Version: 1.0
   * Previous Author: zhumingwei
   * Previous Date: 2013-04-12
   * @version: 1.0
   * @author: zhumingwei
   * Date: 2013-04-12
*/
package com.infodms.dms.actions.claim.basicData;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.text.AbstractDocument.Content;

import jxl.Cell;

import org.apache.log4j.Logger;

import com.infodms.dms.actions.claim.serviceActivity.BaseImport;
import com.infodms.dms.actions.sales.planmanage.PlanUtil.ExcelErrors;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.dao.claim.basicData.FailureMaintainDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TmPtFailureModePO;
import com.infodms.dms.po.TtAsWrFaultLegalPO;
import com.infodms.dms.po.TtAsWrFaultModeDetailPO;
import com.infodms.dms.po.TtAsWrFaultPartsPO;
import com.infodms.dms.po.TtAsWrFaultPartsTempPO;
import com.infodms.dms.po.TtAsWrFaultTypePO;
import com.infodms.dms.po.TtAsWrPartLegallDetailPO;
import com.infodms.dms.po.TtAsWrRuleListPO;
import com.infodms.dms.po.TtAsWrRuleListTmpPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.dms.chana.actions.OSC71;
import com.infoservice.dms.chana.actions.OSC72;
import com.infoservice.dms.chana.actions.OSC73;
import com.infoservice.dms.chana.actions.OSC74;
import com.infoservice.dms.chana.dao.DeCommonDao;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.FileObject;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;

import flex.messaging.io.ArrayList;

public class FailureMaintainMain extends BaseImport {
	private Logger logger = Logger.getLogger(FailureMaintainMain.class);
	private final FailureMaintainDao dao = FailureMaintainDao.getInstance();
	//严重安全性能故障维护
	private final String Failure_Maintain_Url = "/jsp/claim/basicData/failureMaintainIndex.jsp";//主页面
	private final String Failure_Maintain_Add_Url = "/jsp/claim/basicData/failureMaintainAdd.jsp";//新增页面
	private final String Failure_Maintain_update_Url = "/jsp/claim/basicData/failureMaintainUpdate.jsp";//修改页面
	
	//严重安全性能故障法定名称维护
	private final String Fault_Legal_Url = "/jsp/claim/basicData/faultLegalIndex.jsp";//主页面
	private final String Fault_Legal_Add_Url = "/jsp/claim/basicData/faultLegalAdd.jsp";//新增页面
	private final String Show_Failure_Url = "/jsp/claim/basicData/showFailureCode.jsp";//故障选择
	private final String add_part_Url = "/jsp/claim/basicData/addPartInfo.jsp";
	private final String add_part_FAILURE_Url = "/jsp/claim/basicData/addPartFailure.jsp";
	private final String part_import_Url = "/jsp/claim/basicData/partInfoImport.jsp";
	private final String part_import_Failure_Url = "/jsp/claim/basicData/partImportFailure.jsp";
	private final String add_fail_mode_Url = "/jsp/claim/basicData/addFailMode.jsp";
	private final String add_mode_detail_Url = "/jsp/claim/basicData/addModeDetail.jsp";
	//失效模式下发页面
	private final String Down_Failure_Legal = "/jsp/claim/basicData/downFailureLegal.jsp";
	
	//失效模式维护
	private final String failure_mode_url = "/jsp/claim/basicData/faultModeIndex.jsp";//主页面
	private final String failure_mode_add_url = "/jsp/claim/basicData/faultModeAdd.jsp";//新增页面
	
	/**
	* @Description: 失效模式初始化
	* @param 
	* @date 2013-11-25下午3:09:18
	* @throws luole
	*
	 */
	public void FailureModeInit(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			act.setForword(failure_mode_url);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.FAILURE_CODE,"失效模式");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	public void failureModeSave(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		try {
			String modeCode = request.getParamValue("modeCode");
			String modeName = request.getParamValue("modeName");
			TmPtFailureModePO po  = new TmPtFailureModePO();
			po.setFailureCode(modeCode);
			List list = dao.select(po);
			if(list==null || list.size()<=0){
				po.setId(Long.parseLong(SequenceManager.getSequence("")));
				po.setFailureName(modeName);
				po.setStatus(1);
				po.setIsDe(0);
				po.setCreateDate(new Date());
				po.setCreateBy(-1L);
				dao.insert(po);
				act.setOutData("success", "true");
			}else{
				act.setOutData("returnValue", modeCode);
			}
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.FAILURE_CODE,"失效模式");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	//失效模式查询
	public void FaultModeQuery() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
				String modeCode = request.getParamValue("modeCode");
				String modeName = request.getParamValue("modeName");
				Integer curPage = request.getParamValue("curPage") != null ? Integer
						.parseInt(request.getParamValue("curPage"))
						: 1; // 处理当前页
				
				PageResult<Map<String, Object>> ps = dao.failureModeQuery(modeCode,modeName,Constant.PAGE_SIZE, curPage);
				act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"严重安全性能故障维护");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	public void FaultModeDel(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		try {
			String id = request.getParamValue("id");
			TmPtFailureModePO po = new TmPtFailureModePO();
			po.setId(Utility.getLong(id));
			dao.delete(po);
			act.setOutData("msg", true);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"严重安全性能故障维护");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	public void faultModeAdd(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		try {
			act.setForword(failure_mode_add_url);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"严重安全性能故障维护");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	//初始页面
	public void FailureMaintainInit(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			act.setForword(Failure_Maintain_Url);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.FAILURE_CODE,"严重安全性能故障维护");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	//2013-10-17    失效模式下发
	public void downFaultLegalInit(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			act.setForword(Down_Failure_Legal);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.FAILURE_CODE,"失效模式下发");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	public void downFailMode(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			String value =   CommonUtils.checkNull(request.getParamValue("value"));
			String msg = "";
			if(value.equals("0")){
				new OSC71().execute();
				new OSC72().execute();
				msg=msg+"给所有经销商下发新增失效模式 下发成功";
			}else{
				String dealer_code =   CommonUtils.checkNull(request.getParamValue("dealer_code"));
				String codes[] = dealer_code.split(",");
				DeCommonDao deCommonDaoDe = DeCommonDao.getInstance();
				String seccussMsg = "";
				for(String code : codes){
					Map<String, Object> map = deCommonDaoDe.getDmsDlrCode(code);
					if(map!=null){
						String DMScode = String.valueOf(map.get("DMS_CODE"));
						seccussMsg=seccussMsg+String.valueOf(map.get("COMPANY_SHORTNAME"))+"\n";
						new OSC73().handleExecute(DMScode);
						new OSC74().handleExecute(DMScode);
					}
				}
				msg=msg+"失效模式下发成功的有：\n"+seccussMsg;
			}
			act.setOutData("msg", msg);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.FAILURE_CODE,"失效模式下发失败");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	//查询分页页面
	public void failureMaintainQuery() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		StringBuffer sb = new StringBuffer();
		try {
			RequestWrapper request = act.getRequest();
			act.getResponse().setContentType("application/json");
			if("1".equals(request.getParamValue("COMMAND"))){ //开始查询
				String failureCode = request.getParamValue("failureCode");
				String failureName = request.getParamValue("failureName");
				String status = request.getParamValue("status");
				//拼sql的查询条件
				if (Utility.testString(failureCode)) {
					sb.append(" and a.fault_type_code like '%"+failureCode+"%' ");
				}
				if (Utility.testString(failureName)) {
					sb.append(" and a.fault_type_name like '%"+failureName+"%' ");
				}
				if (Utility.testString(status)) {
					sb.append(" and a.status = "+status+" ");
				}				
				Integer curPage = request.getParamValue("curPage") != null ? Integer
						.parseInt(request.getParamValue("curPage"))
						: 1; // 处理当前页
				
				PageResult<Map<String, Object>> ps = dao.failureMaintainQuery(Constant.PAGE_SIZE, curPage ,sb.toString());
				act.setOutData("ps", ps);
			}
		} catch(BizException e){
			logger.error(logonUser,e);
			act.setException(e);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"严重安全性能故障维护");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	//新增跳转页
	public void failureMaintainAddInit(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			act.setForword(Failure_Maintain_Add_Url);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"严重安全性能故障维护");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	//执行新增操作
	public void failureMaintainAdd(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			
			String failureCode = request.getParamValue("failureCode");
			String failureName = request.getParamValue("failureName");
			
			List list = dao.isExist(failureCode);
			if(list != null && list.size() > 0){//存在
				act.setOutData("returnValue", failureCode);
			}else{
				TtAsWrFaultTypePO po = new TtAsWrFaultTypePO();
				po.setFaultTypeId(Long.parseLong(SequenceManager.getSequence("")));
				po.setFaultTypeCode(failureCode);
				po.setFaultTypeName(failureName);
				po.setStatus(Constant.STATUS_ENABLE);
				po.setIsDe(0);
				po.setCreateBy(logonUser.getUserId());
				po.setCreateDate(new Date());
				dao.insert(po);
			}
			act.setOutData("success", "true");
		}  catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ADD_FAILURE_CODE,"严重安全性能故障维护");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	//修改跳转页面
	public void failureMaintainUpdateInit(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		TtAsWrFaultTypePO tbccpo = null;
		List list = null;
		try {
			RequestWrapper request = act.getRequest();
			String faultId = request.getParamValue("faultId");
			tbccpo = new TtAsWrFaultTypePO();
			tbccpo.setFaultTypeId(new Long(faultId));
			list = dao.select(tbccpo);
			TtAsWrFaultTypePO result = new TtAsWrFaultTypePO();
			if(list != null && list.size() > 0){
				result = (TtAsWrFaultTypePO)list.get(0);
			}
			request.setAttribute("faultList", result);
			act.setForword(Failure_Maintain_update_Url);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"严重安全性能故障维护");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	//执行修改操作
	public void failureMaintainUpdate() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		TtAsWrFaultTypePO tbccpo = null;
		TtAsWrFaultTypePO updatetbccpo = null;
		try {
			RequestWrapper request = act.getRequest();
			String faultId = request.getParamValue("faultId");
			String failureCode = request.getParamValue("failureCode");
			String failureName = request.getParamValue("failureName");
			String status = request.getParamValue("status");
			
			tbccpo = new TtAsWrFaultTypePO();
			tbccpo.setFaultTypeId(new Long(faultId));
			updatetbccpo = new TtAsWrFaultTypePO();
			updatetbccpo.setFaultTypeCode(failureCode);
			updatetbccpo.setFaultTypeName(failureName);
			updatetbccpo.setStatus(Integer.parseInt(status));
			updatetbccpo.setUpdateBy(logonUser.getUserId());
			updatetbccpo.setUpdateDate(new Date());
			dao.update(tbccpo,updatetbccpo);
			act.setOutData("success", "true");
		}catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.UPDATE_FAILURE_CODE,"严重安全性能故障维护");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	//执行删除操作
	public void failureMaintainDel(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		String recesel [] = request.getParamValues("recesel");//获取一个集合ID
		
		TtAsWrFaultTypePO po = new TtAsWrFaultTypePO();
		for(String id:recesel){
			po.setFaultTypeId(Long.parseLong(id));
			try {
				TtAsWrFaultTypePO poValue = new TtAsWrFaultTypePO();
				poValue.setStatus(Constant.STATUS_DISABLE);
				poValue.setUpdateBy(logonUser.getUserId());
				poValue.setUpdateDate(new Date());
				dao.update(po,poValue);
				act.setOutData("msg", "01");
			}catch (Exception e) {
				BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, "严重安全性能故障维护");
				logger.error(logonUser, e1);
				act.setException(e1);
			}
		}
	}
	
	//严重安全性能故障法定名称维护(初始页面)
	public void FaultLegalInit(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			Long userId = logonUser.getUserId();
			act.setOutData("userId", userId);
			act.setForword(Fault_Legal_Url);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"严重安全性能故障维护");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	//严重安全性能故障法定名称维护(新增跳转页)
	public void FaultLegalAddInit(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			act.setForword(Fault_Legal_Add_Url);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"严重安全性能故障法定名称维护");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	//跳转至选择故障模式界面
	public void selectFailureForward() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			act.setForword(Show_Failure_Url);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, "严重安全性能故障法定名称维护");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	//查询故障模式数据提供选择
	public void queryFailureFromBase() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1; // 处理当前页
			String failureCode = request.getParamValue("failureCode");
			String failureName = request.getParamValue("failureName");
			PageResult<TtAsWrFaultTypePO> ps = dao.queryFailureChange(failureCode,failureName, Constant.PAGE_SIZE, curPage);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, "严重安全性能故障法定名称维护");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	//根据选择的故障模式确定某一个模式
	public void queryFaultNameByFaultCode() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			String faultCode = request.getParamValue("faultCode");
			Map<String,Object> ps = dao.queryNameByCode(logonUser,faultCode);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, "严重安全性能故障法定名称维护");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	//新增故障法定名称操作
	public void FaultLegalAdd(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			
			String legalCode = request.getParamValue("legalCode");
			String legalName = request.getParamValue("legalName");
			String failureCode = request.getParamValue("failureCode");
			
			List list = dao.isExistLegal(legalCode);
			if(list != null && list.size() > 0){//存在
				act.setOutData("returnValue", legalCode);
			}else{
				TtAsWrFaultLegalPO po = new TtAsWrFaultLegalPO();
				po.setFaultId(Long.parseLong(SequenceManager.getSequence("")));
				/*****根据故障模式代码找故障模式id****begin*/
				TtAsWrFaultTypePO popo = new TtAsWrFaultTypePO();
				popo.setFaultTypeCode(failureCode);
				TtAsWrFaultTypePO poValue = (TtAsWrFaultTypePO)dao.select(popo).get(0);
				/*****根据故障模式代码找故障模式id****end*/
				po.setFaultTypeId(poValue.getFaultTypeId());
				po.setFaultCode(legalCode);
				po.setFaultName(legalName);
				po.setStatus(Constant.STATUS_ENABLE);
				po.setIsDe(0);
				po.setCreateBy(logonUser.getUserId());
				po.setCreateDate(new Date());
				dao.insert(po);
			}
			act.setOutData("success", "true");
		}  catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ADD_FAILURE_CODE,"严重安全性能故障法定名称维护");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	//严重安全性能故障法定名称维护(查询分页)
	public void FaultLegalQuery() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		StringBuffer sb = new StringBuffer();
		try {
			RequestWrapper request = act.getRequest();
			act.getResponse().setContentType("application/json");
			if("1".equals(request.getParamValue("COMMAND"))){ //开始查询
				String legalCode = request.getParamValue("legalCode");
				String legalName = request.getParamValue("legalName");
				String failureName = request.getParamValue("failureName");
				String status = request.getParamValue("status");
				//拼sql的查询条件
				if (Utility.testString(legalCode)) {
					sb.append(" and a.fault_code like '%"+legalCode+"%' ");
				}
				if (Utility.testString(legalName)) {
					sb.append(" and a.fault_name like '%"+legalName+"%' ");
				}
				if (Utility.testString(status)) {
					sb.append(" and a.status = "+status+" ");
				}				
				Integer curPage = request.getParamValue("curPage") != null ? Integer
						.parseInt(request.getParamValue("curPage"))
						: 1; // 处理当前页
				
				PageResult<Map<String, Object>> ps = dao.faultLegalQuery(Constant.PAGE_SIZE, curPage ,sb.toString(),failureName);
				act.setOutData("ps", ps);
			}
		} catch(BizException e){
			logger.error(logonUser,e);
			act.setException(e);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"严重安全性能故障法定名称维护");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	//执行删除操作(严重安全性能故障法定名称维护)
	public void FaultLegalDel(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		String recesel [] = request.getParamValues("recesel");//获取一个集合ID
		
		TtAsWrFaultLegalPO po = new TtAsWrFaultLegalPO();
		for(String id:recesel){
			po.setFaultId(Long.parseLong(id));
			try {
				TtAsWrFaultLegalPO poValue = new TtAsWrFaultLegalPO();
				poValue.setStatus(Constant.STATUS_DISABLE);
				poValue.setUpdateBy(logonUser.getUserId());
				poValue.setUpdateDate(new Date());
				dao.update(po,poValue);
				act.setOutData("msg", "01");
			}catch (Exception e) {
				BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, "严重安全性能故障法定名称维护");
				logger.error(logonUser, e1);
				act.setException(e1);
			}
		}
	}
	
	//添加配件信息跳转页面
	public void AddPartInfoInit(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		TtAsWrFaultLegalPO tbccpo = null;
		List list = null;
		try {
			RequestWrapper request = act.getRequest();
			String faultId = request.getParamValue("faultId");
			tbccpo = new TtAsWrFaultLegalPO();
			tbccpo.setFaultId(new Long(faultId));
			list = dao.select(tbccpo);
			TtAsWrFaultLegalPO result = new TtAsWrFaultLegalPO();
			if(list != null && list.size() > 0){
				result = (TtAsWrFaultLegalPO)list.get(0);
			}
			request.setAttribute("faultList", result);
			request.setAttribute("faultId", result.getFaultId());
			act.setForword(add_part_Url);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	public void queryFaultPart() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1; // 处理当前页
			String faultId = request.getParamValue("faultId");
			String partCode = request.getParamValue("partCode");
			String partName = request.getParamValue("partName");
			PageResult<TtAsWrFaultPartsPO> ps = dao.queryFaultPart(faultId,partCode,partName, Constant.PAGE_SIZE, curPage);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, "严重安全性能故障法定名称维护");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	//执行批量删除故障配件信息
	public void FaultPartDel(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		String recesel [] = request.getParamValues("recesel");//获取一个集合ID
		
		TtAsWrFaultPartsPO po = new TtAsWrFaultPartsPO();
		for(String id:recesel){
			po.setId(Long.parseLong(id));
			try {
				TtAsWrFaultPartsPO poValue = new TtAsWrFaultPartsPO();
				poValue.setStatus(Constant.STATUS_DISABLE);
				poValue.setIsDe(0);
				poValue.setUpdateBy(logonUser.getUserId());
				poValue.setUpdateDate(new Date());
				dao.update(po, poValue);
				act.setOutData("msg", "01");
			}catch (Exception e) {
				BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, "严重安全性能故障法定名称维护");
				logger.error(logonUser, e1);
				act.setException(e1);
			}
		}
		new OSC71().execute();
	}
	
	//导入故障法定名称配件信息（跳转页）
	public void partImport(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		try {
	    	String faultId=request.getParamValue("faultId");//故障法定名称Id
	    	request.setAttribute("faultId", faultId);
			act.setForword(part_import_Url);
		} catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	//新增配件
	public void addParts(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		try {
	    	String faultId=request.getParamValue("faultId");//故障法定名称Id
	    	act.setOutData("faultId", faultId);
	    	TtAsWrFaultLegalPO tbccpo = new TtAsWrFaultLegalPO();
			tbccpo.setFaultId(Utility.getLong(faultId));
			List<TtAsWrFaultLegalPO> list = dao.select(tbccpo);
			TtAsWrFaultLegalPO result = new TtAsWrFaultLegalPO();
			if(list != null && list.size() > 0){
				result = (TtAsWrFaultLegalPO)list.get(0);
			}
			request.setAttribute("faultList", result);
			request.setAttribute("faultId", faultId);
			act.setForword(add_part_FAILURE_Url);
		} catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	public void saveAddParts(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			String faultId = request.getParamValue("ID");
			String partCode = request.getParamValue("PART_CODE");
			String partName = request.getParamValue("PART_NAME");
			TtAsWrFaultPartsPO po = new TtAsWrFaultPartsPO();
			po.setPartCode(partCode);
			po.setFaultId(Long.parseLong(faultId));
			List<TtAsWrPartLegallDetailPO> list = dao.select(po);
			if(list==null || list.size()<=0){
				po.setCreateBy(logonUser.getUserId());
				po.setCreateDate(new Date());
				po.setId(Utility.getLong(SequenceManager.getSequence("")));
				po.setFaultId(Utility.getLong(faultId));
				po.setPartCode(partCode);
				po.setPartName(partName);
				po.setStatus(Constant.STATUS_ENABLE);
				po.setIsDe(0);
				dao.insert(po);
				act.setOutData("fId", faultId);
				act.setOutData("flag", true);
			}else{
				act.setOutData("flag", false);
				act.setOutData("msg", "新增配件已在该故障法定名称下");
			}
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"");
			logger.error(logonUser,e1);
			act.setException(e1);
		}		
	}
	public void partsImportOption(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		try {
			String faultId=request.getParamValue("faultId");
			TtAsWrFaultPartsPO PartListPO = new TtAsWrFaultPartsPO();
			if (null!=faultId&&!"".equals(faultId)) {
				PartListPO.setFaultId(Long.parseLong(faultId));
			}
			//清空临时表信息
			TtAsWrFaultPartsTempPO RuleListTmpPO = new TtAsWrFaultPartsTempPO();
			dao.claimRulePartsImportDelete(RuleListTmpPO);

			long maxSize=1024*1024*5;
			int errNum=insertIntoTmp(request, "importFile",4,3,maxSize);
			if(errNum!=0){
				List<ExcelErrors> errorList=new ArrayList();
				ExcelErrors ees=new ExcelErrors();
				ees.setRowNum(0);
				switch (errNum) {
				case 1:
					ees.setErrorDesc("文件列数过多");
					break;
				case 2:
					ees.setErrorDesc("空行不能大于三行");
					break;
				case 3:
					ees.setErrorDesc("文件不能为空");
					break;
				case 4:
					ees.setErrorDesc("文件不能为空");
					break;
				case 5:
					ees.setErrorDesc("文件不能大于"+maxSize);
					break;
				default:
					break;
				}
				errorList.add(ees);
				act.setOutData("errorList", errorList);
			    request.setAttribute("faultId", faultId);
			    if(1==errNum){
					request.setAttribute("ees", ees);
					act.setForword(part_import_Failure_Url);
				}
			    else if(2==errNum){
					request.setAttribute("ees", ees);
					act.setForword(part_import_Failure_Url);
				}
			    else if(3==errNum){
					request.setAttribute("ees", ees);
					act.setForword(part_import_Failure_Url);
				}
			    else if(4==errNum){
					request.setAttribute("ees", ees);
					act.setForword(part_import_Failure_Url);
				}
			    else if(5==errNum){
					request.setAttribute("ees", ees);
					act.setForword(part_import_Failure_Url);
				}
				else{
					act.setForword(add_part_Url);
				}
			}else{
				List<Map> list=getMapList();
				//将数据插入临时表
				insertTtAsWrRuleList(list,faultId);
			}
		}catch(Exception e){   
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"配件清单导入");
			logger.error(logonUser,e1);
			act.setException(e1);
		} 	
	}
	
	private void insertTtAsWrRuleList(List<Map> list,String faultId) throws Exception{
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		boolean fl=true;
		StringBuffer sb=new StringBuffer();
		if(null==list){
			list=new ArrayList();
		}
		for(int i=0;i<list.size();i++){
			Map map=list.get(i);
			if(null==map){
				map=new HashMap<String, Cell[]>();
			}
			Set<String> keys=map.keySet();
			Iterator it=keys.iterator();
			String key="";
			while(it.hasNext()){
				key=(String)it.next();
				Cell[] cells=(Cell[])map.get(key);
				int partCode =cells[0].getContents().length();
				if(partCode >50){
					sb.append("配件信息代码超过20位!");
					fl=false;
					break;
				}
				int partName =cells[1].getContents().length();
				if(partName >200){
					sb.append("配件信息名称超过200位!");
					fl=false;
					break;
				}
			}
		}
		if(fl==true){
			for(int i=0;i<list.size();i++){
				Map map=list.get(i);
				if(null==map){
					map=new HashMap<String, Cell[]>();
				}
				Set<String> keys=map.keySet();
				Iterator it=keys.iterator();
				String key="";
				while(it.hasNext()){
					key=(String)it.next();
					Cell[] cells=(Cell[])map.get(key);
					parseCells(key, cells,faultId);
				}
			}
			request.setAttribute("faultId", faultId);
			TtAsWrFaultLegalPO result = new TtAsWrFaultLegalPO();
			result.setFaultId(Long.parseLong(faultId));
			TtAsWrFaultLegalPO resultValue = (TtAsWrFaultLegalPO)dao.select(result).get(0);
			request.setAttribute("faultList", resultValue);
			new OSC71().execute();
			act.setForword(add_part_Url);
		}else{
			ExcelErrors ees=new ExcelErrors();
			request.setAttribute("ees", ees);
			request.setAttribute("sb", sb.toString());
			act.setForword(part_import_Failure_Url);
		}
	}
	
	private void parseCells(String rowNum,Cell[] cells,String faultId) throws Exception{
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		TtAsWrFaultPartsTempPO RuleListPO=new TtAsWrFaultPartsTempPO();
		StringBuffer sb=new StringBuffer();
		if(cells[0].getContents().equals("")||cells[0].getContents()==null){
			sb.append("配件代码为空!"+",");
		}else{
			RuleListPO.setPartCode(subCell(cells[0].getContents().trim()));//配件代码
		}
		if(cells[1].getContents().equals("")||cells[1].getContents()==null){
			sb.append("配件名称为空!"+",");
		}else{
			RuleListPO.setPartName(subCell(cells[1].getContents().trim()));//配件名称
		}
		RuleListPO.setId(Long.parseLong(SequenceManager.getSequence("")));//ID
		RuleListPO.setFaultId(Long.parseLong(faultId));
		RuleListPO.setStatus(Constant.STATUS_ENABLE);
		RuleListPO.setIsDe(0);
		RuleListPO.setUpdateBy(logonUser.getUserId());
		RuleListPO.setUpdateDate(new Date());
		RuleListPO.setCreateBy(logonUser.getUserId());
		RuleListPO.setCreateDate(new Date());
		String ss=null;
		if(sb.toString().lastIndexOf(",")!=-1){
			if(sb.toString().lastIndexOf(",")==sb.toString().length()-1){
				ss=sb.toString().substring(0, sb.toString().lastIndexOf(","));
			}else{
				ss=sb.toString();
			}
		}
		dao.claimRulePartsImportAdd(RuleListPO);//把数据插入临时表
		dao.claimRulePartsImportAddMerge(RuleListPO);//把临时表的数据导入正式表，（3种选择可以成功导入，配件code相同但名称不同或者名称相同code不同，或者code和名称都不同）
	}
	
	private String subCell(String orgAmt){
		String newAmt="";
		if(null==orgAmt||"".equals(orgAmt)){
			return newAmt;
		}
		if(orgAmt.length()>20){
			newAmt=orgAmt.substring(0,20);
		}else{
			newAmt=orgAmt;
		}
		return newAmt;
	}
	
	//添加故障法定名称失效模式跳转页面
	public void AddFailModeInit(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		TtAsWrFaultLegalPO tbccpo = null;
		List list = null;
		try {
			RequestWrapper request = act.getRequest();
			String faultId = request.getParamValue("faultId");
			tbccpo = new TtAsWrFaultLegalPO();
			tbccpo.setFaultId(new Long(faultId));
			list = dao.select(tbccpo);
			TtAsWrFaultLegalPO result = new TtAsWrFaultLegalPO();
			if(list != null && list.size() > 0){
				result = (TtAsWrFaultLegalPO)list.get(0);
			}
			request.setAttribute("faultList", result);
			request.setAttribute("faultId", result.getFaultId());
			act.setForword(add_fail_mode_Url);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	public void queryFailMode() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1; // 处理当前页
			String faultId = request.getParamValue("faultId");
			String failCode = request.getParamValue("failCode");
			String failName = request.getParamValue("failName");
			PageResult<TtAsWrFaultModeDetailPO> ps = dao.queryFailMode(faultId,failCode,failName, Constant.PAGE_SIZE, curPage);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, "严重安全性能故障法定名称维护");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	public void failModeDel(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		String recesel [] = request.getParamValues("recesel");//获取一个集合ID
		
		TtAsWrFaultModeDetailPO po = new TtAsWrFaultModeDetailPO();
		for(String id:recesel){
			po.setId(Long.parseLong(id));
			try {
				TtAsWrFaultModeDetailPO poValue = new TtAsWrFaultModeDetailPO();
				poValue.setStatus(Constant.STATUS_DISABLE);
				poValue.setIsDe(0);
				poValue.setUpdateBy(logonUser.getUserId());
				poValue.setUpdateDate(new Date());
				dao.update(po,poValue);
				act.setOutData("msg", "01");
			}catch (Exception e) {
				BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, "严重安全性能故障法定名称维护");
				logger.error(logonUser, e1);
				act.setException(e1);
			}
		}
		new OSC72().execute();
	}
	
	public void addModeQuery(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		try {
			String faultId=request.getParamValue("faultId");
			request.setAttribute("faultId", faultId);
			act.setForword(add_mode_detail_Url);
		} catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	public void failModeDetailQuery(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		try {
			String faultId = request.getParamValue("faultId");
			String failCode = request.getParamValue("failCode");
			String failName = request.getParamValue("failName");
			TmPtFailureModePO RuleListPO =new TmPtFailureModePO();
			if(!"".equals(failCode)&&null!=failCode){
				RuleListPO.setFailureCode(failCode);
			}
			if(!"".equals(failName)&&null!=failName){
				RuleListPO.setFailureName(failName);
			}
			Integer curPage = request.getParamValue("curPage") !=null ? Integer.parseInt(request.getParamValue("curPage")) : 1;
			PageResult<Map<String, Object>> ps = dao.failModeDetailQuery(RuleListPO,curPage,Constant.PAGE_SIZE );
			act.setOutData("ps", ps);
		    request.setAttribute("faultId", faultId);
			act.setForword(add_mode_detail_Url);
		}catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	public void insertFailModeDetail(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		
		String faultId = request.getParamValue("faultId");
		String recesel [] = request.getParamValues("orderIds");//获取一个集合ID
			
		for(String id:recesel){
			try {
				TtAsWrFaultModeDetailPO po = new TtAsWrFaultModeDetailPO();
				po.setFailureModeId(Long.parseLong(id));
				po.setFaultId(Long.parseLong(faultId));
				List list = dao.select(po);
				if(list.size()<=0){
					TmPtFailureModePO pop = new TmPtFailureModePO();
					pop.setId(Long.parseLong(id));
					TmPtFailureModePO poValue = (TmPtFailureModePO)dao.select(pop).get(0);
					TtAsWrFaultModeDetailPO popo = new TtAsWrFaultModeDetailPO();
					popo.setId(Long.parseLong(SequenceManager.getSequence("")));
					popo.setFailureModeId(poValue.getId());
					popo.setFaultId(Long.parseLong(faultId));
					popo.setFailureModeCode(poValue.getFailureCode());
					popo.setFailureModeName(poValue.getFailureName());
					popo.setStatus(Constant.STATUS_ENABLE);
					popo.setIsDe(0);
					popo.setCreateBy(logonUser.getUserId());
					popo.setCreateDate(new Date());
					dao.insert(popo);
				}
			}catch (Exception e) {
				BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, "严重安全性能故障法定名称维护");
				logger.error(logonUser, e1);
				act.setException(e1);
			}
		}
		new OSC72().execute();
	}
}
