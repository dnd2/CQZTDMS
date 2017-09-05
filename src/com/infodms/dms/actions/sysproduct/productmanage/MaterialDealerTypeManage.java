/**
 * @Title: MaterialManage.java
 * 
 * @Description:
 * 
 * @Copyright: Copyright (c) 2010
 * 
 * @Company: www.infoservice.com.cn
 * @Date: 2010-7-1
 * 
 * @author yuyong
 * @mail yuyong@infoservice.com.cn
 * @version 1.0
 * @remark
 */
package com.infodms.dms.actions.sysproduct.productmanage;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.dao.productmanage.MaterialDealerTypeDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;
import com.infoservice.po3.bean.PageResult;

/**
 * 经销商物料类型设置
 * @author ranj 2013 - 12 - 13
 */
public class MaterialDealerTypeManage {
	private Logger logger = Logger.getLogger(MaterialDealerTypeManage.class);
	private ActionContext act = ActionContext.getContext();
	RequestWrapper request = act.getRequest();
	ResponseWrapper response = act.getResponse();
	private final MaterialDealerTypeDao dao = MaterialDealerTypeDao.getInstance();
	
	private final String MATERIAL_MANAGE_QUERY_URL = "/jsp/sysproduct/productmanage/materialDealerTypeQuery.jsp";// 物料维护查询页面
	/**
	 * 经销商物料类型初始化
	 * ranj
	 */
	
	public void materialDealerTypeInit()
	{
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try
		{
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request
							.getParamValue("curPage")) : 1;
			act.setOutData("curPage", curPage);
			act.setForword(MATERIAL_MANAGE_QUERY_URL);
		}
		catch (Exception e)
		{
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "物料维护");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 经销商物料类型查询
	 * ranj
	 */
	public void materialDealerTypeQuery()
	{
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try
		{
			String dealerCode = CommonUtils.checkNull(request.getParamValue("dealerCode"));
			String dealerName = CommonUtils.checkNull(request.getParamValue("dealerName"));
			String status = CommonUtils.checkNull(request.getParamValue("status"));
			String is_rule_seach = CommonUtils.checkNull(request.getParamValue("is_rule_seach"));
			String is_export_seach = CommonUtils.checkNull(request.getParamValue("is_export_seach"));
			String is_Insale_seach = CommonUtils.checkNull(request.getParamValue("is_insale_seach"));	
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("dealerCode", dealerCode);
			map.put("dealerName", dealerName);
			map.put("status", status);
			map.put("is_rule_seach", is_rule_seach);
			map.put("is_export_seach", is_export_seach);
			map.put("is_Insale_seach", is_Insale_seach);
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request
							.getParamValue("curPage")) : 1;
			PageResult<Map<String, Object>> ps = dao.getMaterialDealerTypeQueryList(map, curPage, 20);
			act.setOutData("ps", ps);
		}
		catch (Exception e)
		{
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "物料维护");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 经销商物料类型（批量设置 是否内销，是否出口）[单条处理]
	 * 
	 * @author ranj
	 */
	public void materialManageSetOne()
	{
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try
		{
			String isRuleMat = CommonUtils.checkNull(request.getParamValue("IS_RULE_MAT"));//是否常规
			String exportSalesFlag = CommonUtils.checkNull(request.getParamValue("EXPORT_SALES_FLAG"));//是否出口
			String isInsale = CommonUtils.checkNull(request.getParamValue("IS_INSALE"));//是否内销
			String dealerId = CommonUtils.checkNull(request.getParamValue("dealerId"));//经销商ID
			String id = CommonUtils.checkNull(request.getParamValue("ID"));//类型表ID
			
			Map<String, Object> dataMap = new HashMap<String, Object>();
			dataMap.put("isRuleMat", isRuleMat);
			dataMap.put("exportSalesFlag", exportSalesFlag);
			dataMap.put("isInsale", isInsale);
			dataMap.put("dealerId", dealerId);
			dataMap.put("id", id);
			dataMap.put("userId", logonUser.getUserId());
			dao.setMaterialStatusOne(dataMap);
			act.setOutData("message", "状态设置成功!");
		}
		catch (Exception _ex)
		{
			BizException e1 = new BizException(act, ErrorCodeConstant.SPECIAL_MEG, 2, _ex.getMessage());
			logger.error(logonUser, _ex);
			act.setException(e1);
		}
	}
	/**
	 * 经销商物料类型（批量设置 是否内销，是否出口）[单条处理]
	 * 
	 * @author ranj
	 */
	public void materialManageSet()
	{
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try
		{
			String isRuleMat = CommonUtils.checkNull(request.getParamValue("isRuleMat"));//是否常规
			String exportSalesFlag = CommonUtils.checkNull(request.getParamValue("esflag"));//是否出口
			String isInsale = CommonUtils.checkNull(request.getParamValue("isInsale"));//是否内销
			String[] mid = request.getParamValues("mid");//经销商ID
			String dealerId = "";//经销商ID
			String id = "";//类型表ID
			if(mid!=null){
				for(int i=0;i<mid.length;i++){
					String[] stb=mid[i].split("#");
					dealerId = CommonUtils.checkNull(stb[0]);//经销商ID
					id = CommonUtils.checkNull(stb[1]);//类型表ID
					Map<String, Object> dataMap = new HashMap<String, Object>();
					dataMap.put("isRuleMat", isRuleMat);
					dataMap.put("exportSalesFlag", exportSalesFlag);
					dataMap.put("isInsale", isInsale);
					dataMap.put("dealerId", dealerId);
					dataMap.put("id", id);
					dataMap.put("userId", logonUser.getUserId());
					dao.setMaterialStatusOne(dataMap);
				}
				act.setOutData("message", "状态设置成功!");
			}else{
				act.setOutData("message", "状态设置失败，无经销商选择!");
			}
			
		}
		catch (Exception _ex)
		{
			BizException e1 = new BizException(act, ErrorCodeConstant.SPECIAL_MEG, 2, _ex.getMessage());
			logger.error(logonUser, _ex);
			act.setException(e1);
		}
	}
	
}
