package com.infodms.dms.actions.sales.fleetmanage.fleetInfoManage;

import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.bean.FleetInfoBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.dao.sales.fleetmanage.fleetinfomanage.FleetEditLogDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;

/**
 * 
 * <p>Title:FleetInfoModifySearch.java</p>
 *
 * <p>Description: 集团客户报备更改查询(车厂端)业务逻辑</p>
 *
 * <p>Copyright: Copyright (c) 2010</p>
 *
 * <p>Company: www.infoservice.com.cn</p>
 * <p>Date:2010-6-21</p>
 *
 * @author zouchao
 * @version 1.0
 * @remark
 */
public class FleetInfoModifySearch {
	
	
	private static Logger logger = Logger.getLogger(FleetInfoModifySearch.class);
	ActionContext act = ActionContext.getContext();
	AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
	RequestWrapper request = act.getRequest();
	
	// 集团客户报备更改查询页面
	private final String fleetModifySearchInitUrl = "/jsp/sales/fleetmanage/fleetinfomanage/fleetModifySearchOEM.jsp";
	
	// 集团客户报备更改查看详细页面
	private final String fleetModifyDetailUrl = "/jsp/sales/fleetmanage/fleetinfomanage/viewFleetModifyDetail.jsp";
	
	
	/**
	 * 集团客户报备更改查询初始化页面
	 */
	public void searchModifyInit(){
		try{
			act.setForword(fleetModifySearchInitUrl);
		}catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ACTION_NAME_ERROR_CODE,"集团客户报备更改查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 集团客户报备更改查询
	 */
	public void queryFleetInfoModify(){
		try{
			//CommonUtils.checkNull() 校验是否为空
			String fleetName = CommonUtils.checkNull(request.getParamValue("fleetName"));  	    //客户名称
			String fleetType = CommonUtils.checkNull(request.getParamValue("fleetType"));	    //客户类型
			String beginTime = CommonUtils.checkNull(request.getParamValue("beginTime"));       //申请开始日期
			String endTime = CommonUtils.checkNull(request.getParamValue("endTime"));           //申请结束日期
			String dealerCode = CommonUtils.checkNull(request.getParamValue("dealerCode"));     //经销商公司
			String auditStatus = CommonUtils.checkNull(request.getParamValue("auditStatus"));   //审核状态
			
			if(!dealerCode.equals("")){////截串加单引号
				String[] supp = dealerCode.split(",");
				dealerCode = "";
				for(int i=0;i<supp.length;i++){
					supp[i] = "'"+supp[i]+"'";
					if(!dealerCode.equals("")){
						dealerCode += "," + supp[i];
					}else{
						dealerCode = supp[i];
					}
				}
			}
			
			// 传入查询条件
			FleetInfoBean fibean = new FleetInfoBean();
			fibean.setFleetName(fleetName);
			fibean.setFleetType(fleetType);
			fibean.setBeginTime(beginTime);
			fibean.setEndTime(endTime);
			fibean.setDlrCompanyCode(dealerCode);
			fibean.setStatus(auditStatus);
			fibean.setDutyType(logonUser.getDutyType());
			fibean.setOrgId(String.valueOf(logonUser.getOrgId()));
			FleetEditLogDao logdao = new FleetEditLogDao();
			//分页方法 begin
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage"))	: 1; // 处理当前页	
				
			PageResult<Map<String, Object>> ps = logdao.queryInfoForModify(fibean,Constant.PAGE_SIZE,curPage);
			//分页方法 end
			
			//向前台传的list 名称是固定的不可改必须用 ps
			act.setOutData("ps", ps);     
			
			act.setForword(fleetModifySearchInitUrl);
		}catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"集团客户报备更改查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	
	/**
	 * 集团客户报备明细查看
	 */
	public void viewModifyDetail(){
		try{
			// 取得页面传参
			String editId = CommonUtils.checkNull(request.getParamValue("editId"));  	 
			
			FleetEditLogDao logdao = new FleetEditLogDao();
			
			// 根据集团客户修改ID取得集团客户详细信息
			Map<String, Object> fleetlogMap = logdao.getModifyInfobyId(editId);
			
			act.setOutData("fleetlogMap", fleetlogMap);
			
			act.setForword(fleetModifyDetailUrl);
			
		}catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"集团客户更改信息详情");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
}
