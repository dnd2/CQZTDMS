package com.infodms.dms.actions.sysbusinesparams.businesparamsmanage;

import java.util.Date;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.dao.sysbusinesparams.businesparamsmanage.VariableParaManageDAO;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TmVariableParaPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;

/**
 * 
 * <p>Title:VariableParaManage.java</p>
 *
 * <p>Description: 可变代码维护功能业务逻辑处理层</p>
 *
 * <p>Copyright: Copyright (c) 2010</p>
 *
 * <p>Company: www.infoservice.com.cn</p>
 * <p>Date:2010-7-9</p>
 *
 * @author zouchao
 * @version 1.0
 * @remark
 */
public class VariableParaManage {
	
	public Logger logger = Logger.getLogger(VariableParaManage.class);
	private ActionContext act = ActionContext.getContext();
	RequestWrapper request = act.getRequest();
	private final VariableParaManageDAO dao = VariableParaManageDAO.getInstance();
	
	// 可变代码维护初始化页面
	private final String variableParaInitUrl = "/jsp/sysbusinesparams/businesparamsmanage/variableParaInit.jsp";
	// 可变代码新增页面
	private final String variableParaAddUrl = "/jsp/sysbusinesparams/businesparamsmanage/variableParaAdd.jsp";
	// 可变代码修改页面
	private final String variableParaModifyUrl = "/jsp/sysbusinesparams/businesparamsmanage/variableParaMod.jsp";
	
	/**
	 * 可变代码维护初始化
	 */
	public void variableParaManageInit(){
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try{
			act.setForword(variableParaInitUrl);
		}catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ACTION_NAME_ERROR_CODE,"可变代码维护");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	
	/**
	 * 查询可变代码
	 */
	public void queryVariablePara(){
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try{
			
			// 页面查询条件
			String paraType = CommonUtils.checkNull(request.getParamValue("paraType"));  	    //参数类型
			
			// 从session中获取companyId
			String oemCompanyId = String.valueOf(logonUser.getCompanyId());
			
			
			//分页方法 begin
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage"))	: 1; // 处理当前页	
				
			PageResult<Map<String, Object>> ps = dao.queryVariablePara(paraType,oemCompanyId,Constant.PAGE_SIZE,curPage);
			//分页方法 end
			
			act.setOutData("ps", ps);     
			
		}catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"可变代码查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	
	
	/**
	 * 可变代码新增初始化
	 */
	public void addVariableParaInit(){
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try{
			String paraType = CommonUtils.checkNull(request.getParamValue("paraType"));  	    //参数类型
			
			act.setOutData("paraType", paraType);
			act.setForword(variableParaAddUrl);
		}catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ACTION_NAME_ERROR_CODE,"可变代码新增");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	
	/**
	 * 可变代码新增
	 */
	public void saveVariablePara(){
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try{
			String paraType = CommonUtils.checkNull(request.getParamValue("paraType"));  	    //参数类型
			String paraCode = CommonUtils.checkNull(request.getParamValue("paraCode"));	    	//参数代码
			String paraName = CommonUtils.checkNull(request.getParamValue("paraName")); 		//参数名称 
			String status = CommonUtils.checkNull(request.getParamValue("status"));         	//状态
			String issue = CommonUtils.checkNull(request.getParamValue("issue"));       		//是否需要下发
			String remark = CommonUtils.checkNull(request.getParamValue("remark"));           	//备注
			
			// 从session中获取companyId
			Long oemCompanyId = logonUser.getCompanyId();
			
			
			Map<String, Object> paraMap = dao.searchParaByCode(String.valueOf(oemCompanyId),paraType, paraCode);
			
			if(null!=paraMap){
				act.setOutData("returnValue", 0);
			}else{
			
				// Sequence生成ID
				String paraId = SequenceManager.getSequence(""); 
				
				TmVariableParaPO po = new TmVariableParaPO();
				po.setParaId(new Long(paraId));
				po.setParaType(new Integer(paraType));
				po.setParaCode(paraCode);
				po.setParaName(paraName);
				po.setStatus(new Integer(status));
				po.setIssue(new Integer(issue));
				po.setRemark(remark);
				po.setCreateBy(logonUser.getUserId());
				po.setCreateDate(new Date(System.currentTimeMillis()));
				po.setOemCompanyId(oemCompanyId);
				
				
				dao.addNewPara(po);
				act.setOutData("returnValue", 1);
			}
		}catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ADD_FAILURE_CODE,"可变代码新增");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 可变代码修改初始化
	 */
	public void updateVarParaInit(){
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try{
			String paraId = CommonUtils.checkNull(request.getParamValue("paraId"));  	    //参数ID
			
			Map<String, Object> paraMap = dao.searchParaById(paraId); 
			
			act.setOutData("paraMap", paraMap);
			act.setForword(variableParaModifyUrl);
		}catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ACTION_NAME_ERROR_CODE,"可变代码修改");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	
	/**
	 * 可变代码更新
	 */
	public void updatePara(){
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		
		try{
			String paraId = CommonUtils.checkNull(request.getParamValue("paraId"));  	        //修改的参数id
			String paraCode = CommonUtils.checkNull(request.getParamValue("paraCode"));	    	//参数代码
			String paraName = CommonUtils.checkNull(request.getParamValue("paraName")); 		//参数名称 
			String status = CommonUtils.checkNull(request.getParamValue("status"));         	//状态
			String issue = CommonUtils.checkNull(request.getParamValue("issue"));       		//是否需要下发
			String remark = CommonUtils.checkNull(request.getParamValue("remark"));           	//备注
			
			
			TmVariableParaPO po1 = new TmVariableParaPO();
			po1.setParaId(new Long(paraId));
				
			TmVariableParaPO po2 = new TmVariableParaPO();
			po2.setParaCode(paraCode);
			po2.setParaName(paraName);
			po2.setStatus(new Integer(status));
			po2.setIssue(new Integer(issue));
			po2.setRemark(remark);
			po2.setUpdateBy(logonUser.getUserId());
			po2.setUpdateDate(new Date(System.currentTimeMillis()));
			
			dao.updatePara(po1, po2);
			act.setOutData("returnValue", 1);
			
		}catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ADD_FAILURE_CODE,"可变代码新增");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
}
