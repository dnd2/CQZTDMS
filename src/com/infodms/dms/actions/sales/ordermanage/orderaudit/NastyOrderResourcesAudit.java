/**********************************************************************
* <pre>
* FILE : NastyOrderResourcesAudit.java
* CLASS : NastyOrderResourcesAudit
* AUTHOR : 
* FUNCTION : 订单调整
*======================================================================
* CHANGE HISTORY LOG
*----------------------------------------------------------------------
* MOD. NO.|   DATE   |    NAME    | REASON  |  CHANGE REQ.
*----------------------------------------------------------------------
*         |2010-05-21|            | Created |
* DESCRIPTION:
* </pre>
***********************************************************************/
package com.infodms.dms.actions.sales.ordermanage.orderaudit;

import java.io.OutputStream;
import java.sql.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.getCompanyId.GetOemcompanyId;
import com.infodms.dms.common.materialManager.MaterialGroupManagerDao;
import com.infodms.dms.dao.sales.ordermanage.audit.OrderAuditDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TmVhclMaterialGroupRPO;
import com.infodms.dms.po.TmVhclMaterialPO;
import com.infodms.dms.util.csv.CsvWriterUtil;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;
import com.infoservice.po3.bean.PageResult;

/**
 * @Title: 补充订单可提报资源设定Action
 * @Description:InfoFrame3.0.V01
 * @Copyright: Copyright (c) 2010
 * @Company: www.infoservice.com.cn
 * @Date: 2010-5-28
 * @author 
 * @mail   
 * @version 1.0
 * @remark 
 */
public class NastyOrderResourcesAudit {
	
	public Logger logger = Logger.getLogger(NastyOrderResourcesAudit.class);   
	OrderAuditDao dao  = OrderAuditDao.getInstance();
	private ActionContext act = ActionContext.getContext();
	RequestWrapper request = act.getRequest();
	
	private final String initUrl = "/jsp/sales/ordermanage/orderaudit/nastyOrderResourcesAudit.jsp";
	private final String detailUrl = "/jsp/sales/ordermanage/orderaudit/nastyOrderMaterialAudit.jsp";
	/**
	 * 补充订单可提报资源设定页面初始化
	 */
	public void resourcesAuditInit(){
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			act.setForword(initUrl);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"补充订单可提报资源设定");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 补充订单可提报资源设定查询---物料组
	 */
	public void resourcesAuditQuery(){
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			String groupCode = request.getParamValue("groupCode");  //物料组CODE
			Integer curPage = request.getParamValue("curPage") !=null ? Integer.parseInt(request.getParamValue("curPage")) : 1;
			List<Map<String, Object>> areaList=MaterialGroupManagerDao.getPoseIdBusiness(logonUser.getPoseId().toString());
			String areaIds="";
			if(areaList.size()>0){
				for(int i=0;i<areaList.size();i++){
					if("".equals(areaIds)){
						areaIds = areaList.get(i).get("AREA_ID").toString();
					}else{
						areaIds = areaList.get(i).get("AREA_ID").toString()+","+areaIds;
					}
				}
				
			}
			Long companyId=GetOemcompanyId.getOemCompanyId(logonUser);
			PageResult<Map<String, Object>> ps = dao.getOrderResourceList(groupCode, areaIds,companyId,curPage, Constant.PAGE_SIZE);
			act.setOutData("ps", ps);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"补充订单可提报资源设定");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 补充订单可提报资源设定下载---物料组
	 */
	public void resourcesAuditDown(){
		OutputStream os = null;
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			Map map=new HashMap();
			String groupCode = request.getParamValue("groupCode");  //物料组CODE
			Integer curPage = request.getParamValue("curPage") !=null ? Integer.parseInt(request.getParamValue("curPage")) : 1;
			List<Map<String, Object>> areaList=MaterialGroupManagerDao.getPoseIdBusiness(logonUser.getPoseId().toString());
			String areaIds="";
			if(areaList.size()>0){
				for(int i=0;i<areaList.size();i++){
					if("".equals(areaIds)){
						areaIds = areaList.get(i).get("AREA_ID").toString();
					}else{
						areaIds = areaList.get(i).get("AREA_ID").toString()+","+areaIds;
					}
				}
			}
			Long companyId=GetOemcompanyId.getOemCompanyId(logonUser);
			PageResult<Map<String, Object>> ps = dao.getOrderResourceList(groupCode, areaIds,companyId,curPage, 99999);
			ResponseWrapper response = act.getResponse();
			// 导出的文件名
			String fileName = "补充订单可提报资源下载.csv";
			// 导出的文字编码
			fileName = new String(fileName.getBytes("GB2312"), "ISO8859-1");
			response.setContentType("Application/text/csv");
			response.addHeader("Content-Disposition", "attachment;filename=" + fileName);
			// 定义一个集合
			List<List<Object>> list = new LinkedList<List<Object>>();
			// 标题
			List<Object> listTemp = new LinkedList<Object>();
			listTemp.add("车系代码");
			listTemp.add("车系名称");
			listTemp.add("车型代码");
			listTemp.add("车型名称");
			listTemp.add("配置代码");
			listTemp.add("配置名称");
			listTemp.add("当前状态");
			list.add(listTemp);
			List<Map<String, Object>> rslist = ps.getRecords();
			if(rslist!=null){
			for (int i = 0; i < rslist.size(); i++) {
				map = rslist.get(i);
				List<Object> listValue = new LinkedList<Object>();
				listValue = new LinkedList<Object>();
				listValue.add(map.get("SERIESE_CODE") != null ? map.get("SERIESE_CODE") : "");
				listValue.add(map.get("SERIESE_NAME") != null ? map.get("SERIESE_NAME") : "");
				listValue.add(map.get("MODEL_CODE") != null ? map.get("MODEL_CODE") : "");
				listValue.add(map.get("MODEL_NAME") != null ? map.get("MODEL_NAME") : "");
				listValue.add(map.get("CONFIG_CODE") != null ? map.get("CONFIG_CODE") : "");
				listValue.add(map.get("CONFIG_NAME") != null ? map.get("CONFIG_NAME") : "");
				listValue.add(map.get("RUSH_ORDER_FLAG") !=null ? map.get("RUSH_ORDER_FLAG"):"");
				list.add(listValue);
			}
			}
			os = response.getOutputStream();
			CsvWriterUtil.writeCsv(list, os);
			os.flush();
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"补充订单可提报资源设定");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 补充订单可提报资源设定---物料组
	 */
	public void resourcesAuditSubmit(){
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		Long userId = logonUser.getUserId();
		try {
			String groupIds = request.getParamValue("groupIds");			//物料组ID
			String reportStatus = request.getParamValue("reportStatus");	//设定状态
			String []groupId = groupIds.split(",");
			for(int i=0; i<groupId.length; i++){
				TmVhclMaterialGroupRPO tvmgrp = new TmVhclMaterialGroupRPO();
				tvmgrp.setGroupId(Long.parseLong(groupId[i]));
				List list = dao.select(tvmgrp);
				if(list.size()>0){
					for(int j=0; j<list.size(); j++){
						TmVhclMaterialPO tvmpContion = new TmVhclMaterialPO();
						TmVhclMaterialPO tvmpValue = new TmVhclMaterialPO();
						tvmgrp = (TmVhclMaterialGroupRPO)list.get(j);
						tvmpContion.setMaterialId(tvmgrp.getMaterialId());
						tvmpValue.setRushOrderFlag(Integer.parseInt(reportStatus));
						tvmpValue.setUpdateBy(userId);
						tvmpValue.setUpdateDate(new Date(System.currentTimeMillis()));
						dao.update(tvmpContion, tvmpValue);
					}
				}
			}
			act.setOutData("returnValue", 1);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"补充订单可提报资源设定");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 *补充订单可提报资源设定页面初始化---物料
	 */
	public void materialAuditInit(){
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			String groupId = request.getParamValue("GROUP_ID");		  //物料组ID
			act.setOutData("groupId", groupId);
			act.setForword(detailUrl);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"补充订单可提报资源设定");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 补充订单可提报资源设定查询---物料
	 */
	public void materialAuditQuery(){
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			String groupId = request.getParamValue("groupId");		//物料组ID
			Integer curPage = request.getParamValue("curPage") !=null ? Integer.parseInt(request.getParamValue("curPage")) : 1;
			List<Map<String, Object>> areaList=MaterialGroupManagerDao.getPoseIdBusiness(logonUser.getPoseId().toString());
			String areaIds="";
			if(areaList.size()>0){
				for(int i=0;i<areaList.size();i++){
					if("".equals(areaIds)){
						areaIds = areaList.get(i).get("AREA_ID").toString();
					}else{
						areaIds = areaList.get(i).get("AREA_ID").toString()+","+areaIds;
					}
				}
				
			}
			Long companyId=GetOemcompanyId.getOemCompanyId(logonUser);
			PageResult<Map<String, Object>> ps = dao.getMaterialList(groupId,areaIds,companyId,curPage, Constant.PAGE_SIZE);
			act.setOutData("ps", ps);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"补充订单可提报资源设定");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 补充订单可提报资源设定---物料
	 */
	public void materialAuditSubmit(){
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		Long userId = logonUser.getUserId();
		try {
			String materialIds = request.getParamValue("materialIds");		//物料ID
			String reportStatus = request.getParamValue("reportStatus");	//设定状态
			String []materialId = materialIds.split(",");
			for(int i=0; i<materialId.length; i++){
				TmVhclMaterialPO tvmpContion = new TmVhclMaterialPO();
				TmVhclMaterialPO tvmpValue = new TmVhclMaterialPO();
				tvmpContion.setMaterialId(Long.parseLong(materialId[i]));
				tvmpValue.setRushOrderFlag(Integer.parseInt(reportStatus));
				tvmpValue.setUpdateBy(userId);
				tvmpValue.setUpdateDate(new Date(System.currentTimeMillis()));
				dao.update(tvmpContion,tvmpValue);
			}
			act.setOutData("returnValue", 1);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"补充订单可提报资源设定");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
}
