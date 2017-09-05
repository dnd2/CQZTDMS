/*
 * Title: FailureMaintainMain
 * Description: 三包预警规则设置
 * Copyright: Copyright (c) 2013
 * Company: 用友信息科技(上海)有限公司
 * Previous Version: 1.0
 * Previous Author: zhumingwei
 * Previous Date: 2013-04-19
 * @version: 1.0
 * @author: zhumingwei
 * Date: 2013-04-19
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
import com.infodms.dms.common.getCompanyId.GetOemcompanyId;
import com.infodms.dms.dao.claim.basicData.WarningRepairDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TmVehiclePO;
import com.infodms.dms.po.TtAsWarningRepairDetailPO;
import com.infodms.dms.po.TtAsWarningRepairPO;
import com.infodms.dms.po.TtAsWrFaultTypePO;
import com.infodms.dms.po.TtAsWrGamePO;
import com.infodms.dms.po.TtAsWrPartsAssemblyPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;

import flex.messaging.io.ArrayList;

public class WarningRepairMain {
	private Logger logger = Logger.getLogger(WarningRepairMain.class);
	private final WarningRepairDao dao = WarningRepairDao.getInstance();
	
	private final String warning_repair_Url = "/jsp/claim/basicData/warningRepairIndex.jsp";//主页面
	private final String warning_repair_add_Url = "/jsp/claim/basicData/warningRepairAdd.jsp";
	private final String warning_repair_update_Url = "/jsp/claim/basicData/warningRepairUpdate.jsp";
	private final String car_warning_query_Url = "/jsp/claim/basicData/carWarningVin.jsp";//主页面
	
	//三包预警规则设置(初始页面)
	public void WarningRepairInit(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			act.setForword(warning_repair_Url);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"三包预警规则设置");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	public void WarningRepairQuery() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		StringBuffer sb = new StringBuffer();
		try {
			RequestWrapper request = act.getRequest();
			act.getResponse().setContentType("application/json");
			if("1".equals(request.getParamValue("COMMAND"))){ //开始查询
				String WARNING_CODE = request.getParamValue("WARNING_CODE");
				String WAINING_REMARK = request.getParamValue("WAINING_REMARK");
				String WAINING_LEVEL = request.getParamValue("WAINING_LEVEL");
				String WARNING_TYPE = request.getParamValue("WARNING_TYPE");
				String is_Accumulative = request.getParamValue("is_Accumulative");
				String STATUS = request.getParamValue("STATUS");
				//拼sql的查询条件
				if (Utility.testString(WARNING_CODE)) {
					sb.append(" and a.WARNING_CODE like '%"+WARNING_CODE+"%' ");
				}
				if (Utility.testString(WAINING_REMARK)) {
					sb.append(" and a.WAINING_REMARK like '%"+WAINING_REMARK+"%' ");
				}
				if (Utility.testString(WAINING_LEVEL)) {
					sb.append(" and a.WAINING_LEVEL = "+WAINING_LEVEL+" ");
				}
				if (Utility.testString(WARNING_TYPE)) {
					sb.append(" and a.WARNING_TYPE = "+WARNING_TYPE+" ");
				}
				if (Utility.testString(is_Accumulative)) {
					sb.append(" and a.is_Accumulative = "+is_Accumulative+" ");
				}
				if (Utility.testString(STATUS)) {
					sb.append(" and a.STATUS = "+STATUS+" ");
				}
				Integer curPage = request.getParamValue("curPage") != null ? Integer
						.parseInt(request.getParamValue("curPage"))
						: 1; // 处理当前页
				
				PageResult<Map<String, Object>> ps = dao.WarningRepairQuery(Constant.PAGE_SIZE, curPage ,sb.toString());
				act.setOutData("ps", ps);
			}
		} catch(BizException e){
			logger.error(logonUser,e);
			act.setException(e);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"三包预警规则设置");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	public void WarningRepairAddInit(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			List<Map<String, Object>>  assemblyDetail = dao.getAssemblyDetail();
			List<Map<String, Object>>  faultDetail = dao.getFaultDetail();
			Long oemCompanyId = GetOemcompanyId.getOemCompanyId(logonUser);
			List list = dao.getLevelList(oemCompanyId,Constant.AUDIT_TYPE_01.toString());
			act.setOutData("LEVELLIST", list);
			act.setOutData("assemblyDetail", assemblyDetail);
			act.setOutData("faultDetail", faultDetail);
			act.setForword(warning_repair_add_Url);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"三包预警规则设置");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	public void WarningRepairAdd(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			
			String WARNING_TYPE = request.getParamValue("WARNING_TYPE");
			String WAINING_LEVEL = request.getParamValue("WAINING_LEVEL");
			String IS_ACCUMULATIVE = request.getParamValue("IS_ACCUMULATIVE");
			String WARNING_CODE = request.getParamValue("WARNING_CODE");
			String WAINING_REMARK = request.getParamValue("WAINING_REMARK");
			String WARNING_NUM_START = request.getParamValue("WARNING_NUM_START");
			String WARNING_NUM_END = request.getParamValue("WARNING_NUM_END");
			String VALID_DATE = request.getParamValue("VALID_DATE");
			String VALID_START_DATE = request.getParamValue("VALID_START_DATE");
			String VALID_MILEAGE = request.getParamValue("VALID_MILEAGE");
			String VALID_START_MILEAGE = request.getParamValue("VALID_START_MILEAGE");
			String CLAUSE_STATUTE = request.getParamValue("CLAUSE_STATUTE");
			
			/******授权级别********************/
			Long oemCompanyId = GetOemcompanyId.getOemCompanyId(logonUser);
			List list = dao.getLevelList(oemCompanyId,Constant.AUDIT_TYPE_01.toString());//授权级别列表
			String[] level = new String[list.size()];
			for(int i=0; i<list.size(); i++){
				HashMap levlmap = (HashMap)list.get(i);
				level[i] = CommonUtils.checkNull(request.getParamValue(levlmap.get("APPROVAL_LEVEL_CODE").toString()));  //获得索赔基本参数对应的value
			}
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
			/******授权级别********************/
			
			List listRepair = dao.isExistRepair(WARNING_CODE);
			if(listRepair != null && listRepair.size() > 0){//存在
				act.setOutData("returnValue", WARNING_CODE);
			}else{
				TtAsWarningRepairPO po = new TtAsWarningRepairPO();
				po.setWarningRepairId(Long.parseLong(SequenceManager.getSequence("")));
				po.setWarningCode(WARNING_CODE);
				po.setWainingRemark(WAINING_REMARK);
				po.setWarningType(Integer.parseInt(WARNING_TYPE));
				po.setWainingLevel(Integer.parseInt(WAINING_LEVEL));
				po.setWarningNumStart(Integer.parseInt(WARNING_NUM_START));
				po.setWarningNumEnd(Integer.parseInt(WARNING_NUM_END));
				po.setValidDate(Integer.parseInt(VALID_DATE));
				po.setValidStartDate(Integer.parseInt(VALID_START_DATE));
				po.setValidMileage(Integer.parseInt(VALID_MILEAGE));
				po.setValidStartMileage(Integer.parseInt(VALID_START_MILEAGE));
				po.setPprovalLeverCode(levelStr);
				po.setClauseStatute(CLAUSE_STATUTE);
				po.setStatus(Constant.STATUS_ENABLE);
				po.setCreateBy(logonUser.getUserId());
				po.setCreateDate(new Date());
				po.setIsAccumulative(Integer.parseInt(IS_ACCUMULATIVE));
				dao.insert(po);
				
				if(Constant.WANINGTIME_TYPE_01.toString().equals(WARNING_TYPE)){
					TtAsWrPartsAssemblyPO pop = new TtAsWrPartsAssemblyPO();
					pop.setPartsAssemblyId(Long.parseLong(request.getParamValue("ASSEMBLY")));
					TtAsWrPartsAssemblyPO popValue = (TtAsWrPartsAssemblyPO)dao.select(pop).get(0);
					
					TtAsWarningRepairDetailPO popo = new TtAsWarningRepairDetailPO();
					popo.setId(Long.parseLong(SequenceManager.getSequence("")));
					popo.setWarningRepairId(po.getWarningRepairId());
					popo.setChangeCode(popValue.getPartsAssemblyCode());
					popo.setChangeName(popValue.getPartsAssemblyName());
					popo.setChangeId(pop.getPartsAssemblyId());
					popo.setStatus(Constant.STATUS_ENABLE);
					popo.setCreateBy(logonUser.getUserId());
					popo.setCreateDate(new Date());
					dao.insert(popo);
				}
				else if(Constant.WANINGTIME_TYPE_02.toString().equals(WARNING_TYPE)){
					TtAsWrFaultTypePO pop = new TtAsWrFaultTypePO();
					pop.setFaultTypeId(Long.parseLong(request.getParamValue("FAULT")));
					TtAsWrFaultTypePO popValue = (TtAsWrFaultTypePO)dao.select(pop).get(0);
					
					TtAsWarningRepairDetailPO popo = new TtAsWarningRepairDetailPO();
					popo.setId(Long.parseLong(SequenceManager.getSequence("")));
					popo.setWarningRepairId(po.getWarningRepairId());
					popo.setChangeCode(popValue.getFaultTypeCode());
					popo.setChangeName(popValue.getFaultTypeName());
					popo.setChangeId(pop.getFaultTypeId());
					popo.setStatus(Constant.STATUS_ENABLE);
					popo.setCreateBy(logonUser.getUserId());
					popo.setCreateDate(new Date());
					dao.insert(popo);
				}else{
					
				}
			}
			act.setOutData("success", "true");
		}  catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ADD_FAILURE_CODE,"三包预警规则设置");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	public void warningRepairDel(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		String recesel [] = request.getParamValues("recesel");//获取一个集合ID
		
		TtAsWarningRepairPO po = new TtAsWarningRepairPO();
		for(String id:recesel){
			po.setWarningRepairId(Long.parseLong(id));
			try {
				TtAsWarningRepairPO poValue = new TtAsWarningRepairPO();
				poValue.setStatus(Constant.STATUS_DISABLE);
				poValue.setUpdateBy(logonUser.getUserId());
				poValue.setUpdateDate(new Date());
				dao.update(po,poValue);
				act.setOutData("msg", "01");
			}catch (Exception e) {
				BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, "三包预警规则设置");
				logger.error(logonUser, e1);
				act.setException(e1);
			}
		}
	}
	
	public void warningRepairUpdateInit(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		
		List list = null;
		try {
			RequestWrapper request = act.getRequest();
			Long oemCompanyId = GetOemcompanyId.getOemCompanyId(logonUser);
			String warningId = request.getParamValue("warningId");
			
			HashMap hm = null;
			hm = dao.warningRepairQueryById(warningId,oemCompanyId);
			act.setOutData("SELMAP", hm);
			
			//List<Map<String, Object>>  assemblyDetail = dao.getAssemblyDetail();
			//List<Map<String, Object>>  faultDetail = dao.getFaultDetail();
			//act.setOutData("assemblyDetail", assemblyDetail);
			//act.setOutData("faultDetail", faultDetail);
			
			
			List list1 = dao.getLevelList(oemCompanyId,Constant.AUDIT_TYPE_01.toString());//授权级别列表
			act.setOutData("LEVELLIST", list1);//授权级别列表
			
			act.setForword(warning_repair_update_Url);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"三包预警规则设置");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	public void doWaningUpdate() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			String waningId = request.getParamValue("waningId");
			//String ID = request.getParamValue("ID");
			//String WARNING_TYPE = request.getParamValue("WARNING_TYPE");
			String WAINING_LEVEL = request.getParamValue("WAINING_LEVEL");
			String IS_ACCUMULATIVE = request.getParamValue("IS_ACCUMULATIVE");
			String WAINING_REMARK = request.getParamValue("WAINING_REMARK");
			String WARNING_NUM_START = request.getParamValue("WARNING_NUM_START");
			String WARNING_NUM_END = request.getParamValue("WARNING_NUM_END");
			String VALID_DATE = request.getParamValue("VALID_DATE");
			String VALID_START_DATE = request.getParamValue("VALID_START_DATE");
			String VALID_MILEAGE = request.getParamValue("VALID_MILEAGE");
			String VALID_START_MILEAGE = request.getParamValue("VALID_START_MILEAGE");
			String CLAUSE_STATUTE = request.getParamValue("CLAUSE_STATUTE");
			String STATUS = request.getParamValue("STATUS");
			
			
			/******授权级别********************/
			Long oemCompanyId = GetOemcompanyId.getOemCompanyId(logonUser);
			List list = dao.getLevelList(oemCompanyId,Constant.AUDIT_TYPE_01.toString());//授权级别列表
			String[] level = new String[list.size()];
			for(int i=0; i<list.size(); i++){
				HashMap levlmap = (HashMap)list.get(i);
				level[i] = CommonUtils.checkNull(request.getParamValue(levlmap.get("APPROVAL_LEVEL_CODE").toString()));  //获得索赔基本参数对应的value
			}
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
			/******授权级别********************/
			
			TtAsWarningRepairPO po = new TtAsWarningRepairPO();
			po.setWarningRepairId(Long.parseLong(waningId));
			TtAsWarningRepairPO poValue = new TtAsWarningRepairPO();
			poValue.setWainingRemark(WAINING_REMARK);
			poValue.setWainingLevel(Integer.parseInt(WAINING_LEVEL));
			poValue.setWarningNumStart(Integer.parseInt(WARNING_NUM_START));
			poValue.setWarningNumEnd(Integer.parseInt(WARNING_NUM_END));
			poValue.setValidDate(Integer.parseInt(VALID_DATE));
			poValue.setValidStartDate(Integer.parseInt(VALID_START_DATE));
			poValue.setValidMileage(Integer.parseInt(VALID_MILEAGE));
			poValue.setValidStartMileage(Integer.parseInt(VALID_START_MILEAGE));
			poValue.setPprovalLeverCode(levelStr);
			poValue.setClauseStatute(CLAUSE_STATUTE);
			poValue.setStatus(Integer.parseInt(STATUS));
			poValue.setUpdateBy(logonUser.getUserId());
			poValue.setUpdateDate(new Date());
			poValue.setIsAccumulative(Integer.parseInt(IS_ACCUMULATIVE));
			dao.update(po,poValue);
			act.setOutData("success", "true");
				
		}catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.UPDATE_FAILURE_CODE,"三包预警规则设置");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	//车辆进站预警检查(初始页面)
	public void carWarningQueryInit(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			act.setForword(car_warning_query_Url);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	//判断车是否是在三包预警范围
	public void isWarning() { 
		ActionContext act = ActionContext.getContext();
	    AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
	    try {
	      RequestWrapper request = act.getRequest();
	      String vin = request.getParamValue("VIN");
	      
		  act.setOutData("vin", vin);
		  
	      TmVehiclePO po = new TmVehiclePO();
	      po.setVin(Utility.stringVin(vin));
	      TmVehiclePO poValue = (TmVehiclePO)dao.select(po).get(0);
	      TtAsWrGamePO game = new TtAsWrGamePO();
	      game.setId(poValue.getClaimTacticsId());
	      List list = dao.select(game);
	      if(list.size()>0){
	    	  TtAsWrGamePO gameValue = (TtAsWrGamePO)list.get(0);
		      act.setOutData("gameValue", gameValue.getIsNew());
	      }else{
	    	  act.setOutData("gameValue", "false");
	      }
	      act.setForword(car_warning_query_Url);
	    }catch (Exception e) {
	      BizException e1 = new BizException(act, e,ErrorCodeConstant.QUERY_FAILURE_CODE, "车是否是在三包预警范围");
	    }
	}
}
