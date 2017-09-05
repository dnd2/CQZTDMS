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

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.dao.common.CommonDAO;
import com.infodms.dms.dao.productmanage.ProductManageDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TmVhclMaterialGroupPO;
import com.infodms.dms.po.TmVhclMaterialGroupRPO;
import com.infodms.dms.po.TmVhclMaterialPO;
import com.infodms.dms.util.ActionUtil;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.DateTimeUtil;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.dms.chana.actions.OSA12;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

/**
 * @author yuyong
 */
public class MaterialManage {
	private Logger logger = Logger.getLogger(MaterialManage.class);
	private ActionContext act = ActionContext.getContext();
	RequestWrapper request = act.getRequest();
	ResponseWrapper response = act.getResponse();
	private final ProductManageDao dao = ProductManageDao.getInstance();
	
	private final String MATERIAL_MANAGE_QUERY_URL = "/jsp/sysproduct/productmanage/materialManageQuery.jsp";// 物料维护查询页面
	private final String MATERIAL_MANAGE_ADD_URL = "/jsp/sysproduct/productmanage/materialManageAdd.jsp";// 物料维护新增页面
	private final String MATERIAL_MANAGE_MOD_URL = "/jsp/sysproduct/productmanage/materialManageMod.jsp";// 物料维护修改页面
	
	// 物料维护pre
	public void materialManageQueryPre()
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
	
	// 物料维护查询
	public void materialManageQuery()
	{
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try
		{
			String materialCode = CommonUtils.checkNull(request.getParamValue("materialCode"));
			String materialName = CommonUtils.checkNull(request.getParamValue("materialName"));
			String status = CommonUtils.checkNull(request.getParamValue("status"));
			String groupCode = CommonUtils.checkNull(request.getParamValue("groupCode"));
			String companyId = logonUser.getCompanyId().toString();
			String ERPName = CommonUtils.checkNull(request.getParamValue("ERPName"));
			
			String orderFlag = CommonUtils.checkNull(request.getParamValue("orderFlag"));
			String procuctFlag = CommonUtils.checkNull(request.getParamValue("procuctFlag"));
			String isExport = CommonUtils.checkNull(request.getParamValue("is_export"));//是否出口
			String mtseach = CommonUtils.checkNull(request.getParamValue("mtseach"));
			
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("materialCode", materialCode);
			map.put("materialName", materialName);
			map.put("status", status);
			map.put("groupCode", groupCode);
			map.put("companyId", companyId);
			map.put("ERPName", ERPName);
			map.put("orderFlag", orderFlag);
			map.put("procuctFlag", procuctFlag);
			map.put("isExport", isExport);
			map.put("mtseach", mtseach);
			
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request
							.getParamValue("curPage")) : 1;
			PageResult<Map<String, Object>> ps = dao.getMaterialManageQueryList(map, curPage, ActionUtil.getPageSize(request));
			act.setOutData("ps", ps);
			// 自定义每页大小 by chenyub@yonyou.com
			ActionUtil.setCustomPageSizeFlag(act, true);
			// 调整列宽功能 by chenyub@yonyou.com
			ActionUtil.setResizeColumnWidthFlag(act, true);
			// 表格列排序功能 by chenyub@yonyou.com
			ActionUtil.setTableSortFlag(act, true);
			// 表格交换列功能 by chenyub@yonyou.com
			ActionUtil.setSwapColumnFlag(act, true);
		}
		catch (Exception e)
		{
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "物料维护");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 物料维护批量设置可生产和可提报状态
	 * 
	 * @author wangsongwei
	 */
	public void materialManageSet()
	{
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try
		{
			String orderSet = CommonUtils.checkNull(request.getParamValue("orderSet"));
			String procuctSet = CommonUtils.checkNull(request.getParamValue("procuctSet"));
			String statusUp = CommonUtils.checkNull(request.getParamValue("status_up"));
			String matType = CommonUtils.checkNull(request.getParamValue("matType"));
			String[] mid = request.getParamValues("mid");
			
			System.out.println("mid : " + mid.length);
			System.out.println("procuctSet : " + procuctSet);
			
			Map<String, Object> dataMap = new HashMap<String, Object>();
			dataMap.put("orderSet", orderSet);
			dataMap.put("procuctSet", procuctSet);
			dataMap.put("statusUp", statusUp);
			dataMap.put("mid", mid);
			dataMap.put("matType", matType);
			
			dao.setMaterialStatus(dataMap);
			
			act.setOutData("message", "状态设置成功!");
		}
		catch (Exception _ex)
		{
		//	BizException e1 = new BizException(act, ErrorCodeConstant.SPECIAL_MEG, 2, _ex.getMessage());
			logger.error(logonUser, _ex);
			//act.setException(e1);
		}
	}
	
	// 物料维护新增pre
	public void materialManageAddPre()
	{
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try
		{
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request
							.getParamValue("curPage")) : 1;
			
			act.setOutData("curPage", curPage);
			act.setForword(MATERIAL_MANAGE_ADD_URL);
		}
		catch (Exception e)
		{
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "物料维护");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	// 物料维护新增
	public void materialManageAdd()
	{
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try
		{
			String materialCode = CommonUtils.checkNull(request.getParamValue("materialCode"));
			String materialName = CommonUtils.checkNull(request.getParamValue("materialName"));
			String modelCode = CommonUtils.checkNull(request.getParamValue("modelCode"));
			//String trimCode = CommonUtils.checkNull(request.getParamValue("trimCode"));//内饰代码
			String colorCode = CommonUtils.checkNull(request.getParamValue("colorCode"));
			//colorCode = colorCode.split("_")[0];
			String colorName = CommonUtils.checkNull(request.getParamValue("colorName"));
			//String modelYear = CommonUtils.checkNull(request.getParamValue("modelYear"));
			String issueDate = CommonUtils.checkNull(request.getParamValue("issueDate"));
			//String enableDate = CommonUtils.checkNull(request.getParamValue("enableDate"));
			//String disableDate = CommonUtils.checkNull(request.getParamValue("disableDate"));
			String status = CommonUtils.checkNull(request.getParamValue("status"));
			//String rushOrderFlag = CommonUtils.checkNull(request.getParamValue("rushOrderFlag"));
			String groupCode = CommonUtils.checkNull(request.getParamValue("groupCode"));
			String orderFlag = CommonUtils.checkNull(request.getParamValue("orderFlag"));
			//String ERPName = CommonUtils.checkNull(request.getParamValue("ERPName")); // 装配状态代码
			//String matType = CommonUtils.checkNull(request.getParamValue("mat_type")); // 物料类型
			
			//String erpPackage = CommonUtils.checkNull(request.getParamValue("erpPackage")).trim();// 内部型号
			String remark2 = CommonUtils.checkNull(request.getParamValue("remark2")).trim();// 备注
			String procuctFlag = CommonUtils.checkNull(request.getParamValue("procuctFlag"));// 生产状态
			//String isExport = CommonUtils.checkNull(request.getParamValue("is_export"));// 是否进口
			//String isInsale = CommonUtils.checkNull(request.getParamValue("isInsale"));// 是否内销
			
			
			TmVhclMaterialGroupPO group = new TmVhclMaterialGroupPO();
			group.setGroupCode(groupCode);
			group = dao.getTmVhclMaterialGroupPO(group);
			
			TmVhclMaterialGroupPO groupPo2 = new TmVhclMaterialGroupPO();
			groupPo2.setGroupId(group.getParentGroupId());
			groupPo2 = dao.getTmVhclMaterialGroupPO(groupPo2);
			
			// 物料保存
			TmVhclMaterialPO po = new TmVhclMaterialPO();
			String seqId = SequenceManager.getSequence("");
			po.setMaterialId(new Long(seqId));
			po.setMaterialCode(materialCode);
			po.setMaterialName(materialName);
//			po.setErpPackage(erpPackage);
			po.setRemark2(remark2);
			po.setModelCode(modelCode);
			po.setErpModel(groupPo2.getGroupCode());
			po.setProcuctFlag(new Integer(procuctFlag));
			
//			po.setIsInsale(Integer.parseInt(isInsale));//是否内销
//			
//			if (!CommonUtils.isNullString(trimCode.trim()))
//			{
//				po.setTrimCode(trimCode);
//			}
			
			po.setColorCode(colorCode);
			po.setColorName(colorName);
//			po.setModelYear(modelYear);
			
			if (!CommonUtils.isNullString(issueDate.trim()))
			{
				po.setIssueDate(DateTimeUtil.stringToDate(issueDate));
			}
			
//			if (!CommonUtils.isNullString(enableDate.trim()))
//			{
//				po.setEnableDate(DateTimeUtil.stringToDate(enableDate));
//			}
//			
//			if (!CommonUtils.isNullString(disableDate.trim()))
//			{
//				po.setDisableDate(DateTimeUtil.stringToDate(disableDate));
//			}
			
			po.setStatus(new Integer(status));
			// po.setRushOrderFlag(new Integer(rushOrderFlag));
			po.setCreateDate(new Date());
			po.setCreateBy(logonUser.getUserId());
			po.setCompanyId(logonUser.getCompanyId());
			po.setOrderFlag(new Integer(orderFlag));
//			po.setErpName(ERPName);
//			po.setMatType(new Integer(matType));
			po.setRushOrderFlag(Constant.NASTY_ORDER_REPORT_TYPE_01);
//			po.setExportSalesFlag(Integer.parseInt(isExport));//是否出口
			dao.insert(po);
			
			// 物料与物料组关系保存
			TmVhclMaterialGroupRPO rpo = new TmVhclMaterialGroupRPO();
			rpo.setId(new Long(SequenceManager.getSequence("")));
			rpo.setMaterialId(po.getMaterialId());
			rpo.setGroupId(group.getGroupId());
			rpo.setCreateDate(new Date());
			rpo.setCreateBy(logonUser.getUserId());
			
			dao.insert(rpo);
			// 调接口 Start
			// OSA12 o = new OSA12();
			// o.execute(po);
			// 调接口 End
			act.setOutData("returnValue", 1);
			
		}
		catch (Exception e)
		{
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "物料维护");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	// 物料维护修改pre
	public void materialManageModPre()
	{
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try
		{
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request
							.getParamValue("curPage")) : 1;
			
			String materialId = CommonUtils.checkNull(request.getParamValue("materialId"));
			// 物料po
			TmVhclMaterialPO po = new TmVhclMaterialPO();
			po.setMaterialId(new Long(materialId));
			po = dao.getTmVhclMaterialPO(po);
			
			String issueDate = DateTimeUtil.parseDateToDate(po.getIssueDate() == null ? po.getCreateDate() : po
							.getIssueDate());
			String enableDate = DateTimeUtil.parseDateToDate(po.getEnableDate() == null ? po.getCreateDate() : po
							.getEnableDate());
			String disableDate = DateTimeUtil.parseDateToDate(po.getDisableDate());
			
			String trimCode = po.getTrimCode();
			
			if (CommonUtils.isNullString(trimCode) || "".equals(trimCode.trim()))
			{
				String materialCode = po.getMaterialCode();
				
				String[] materialSplit = materialCode.split("\\.");
				
				int len = materialSplit.length;
				
				if (len == 3)
				{
					trimCode = materialSplit[1];
				}
			}
			
			act.setOutData("trimCode", trimCode);
			
			// 物料与物料组关系po
			TmVhclMaterialGroupRPO rpo = new TmVhclMaterialGroupRPO();
			rpo.setMaterialId(new Long(materialId));
			rpo = dao.getTmVhclMaterialGroupRPO(rpo);
			if (rpo != null)
			{
				// 物料组po
				TmVhclMaterialGroupPO gpo = new TmVhclMaterialGroupPO();
				gpo.setGroupId(rpo.getGroupId());
				
				gpo = dao.getTmVhclMaterialGroupPO(gpo);
				act.setOutData("gpo", gpo);
			}
			
			String para = CommonDAO.getPara(Constant.CHANA_SYS.toString());
			
			int isFlag;
			
			if (Constant.COMPANY_CODE_JC.equals(para.toUpperCase()))
			{
				isFlag = 1;
			}
			else if (Constant.COMPANY_CODE_CVS.equals(para.toUpperCase()))
			{
				isFlag = 0;
				
				act.setOutData("normalOrderFlag", po.getNormalOrderFlag());
			}
			else
			{
				throw new RuntimeException("判断当前系统的系统参数错误！");
			}
			
			act.setOutData("isFlag", isFlag);
			
			act.setOutData("po", po);
			act.setOutData("rpo", rpo);
			act.setOutData("issueDate", issueDate);
			act.setOutData("enableDate", enableDate);
			act.setOutData("disableDate", disableDate);
			act.setForword(MATERIAL_MANAGE_MOD_URL);
		}
		catch (Exception e)
		{
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "物料维护");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	// 物料维护修改
	public void materialManageMod()
	{
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try
		{
			String materialId = CommonUtils.checkNull(request.getParamValue("materialId"));
			String materialCode = CommonUtils.checkNull(request.getParamValue("materialCode"));
			String materialName = CommonUtils.checkNull(request.getParamValue("materialName"));
			String modelCode = CommonUtils.checkNull(request.getParamValue("modelCode"));
//			String trimCode = CommonUtils.checkNull(request.getParamValue("trimCode"));
			String colorCode = CommonUtils.checkNull(request.getParamValue("colorCode"));
//			colorCode = colorCode.split("_")[0];
			String colorName = CommonUtils.checkNull(request.getParamValue("colorName"));
//			String modelYear = CommonUtils.checkNull(request.getParamValue("modelYear"));
			String issueDate = CommonUtils.checkNull(request.getParamValue("issueDate"));
//			String enableDate = CommonUtils.checkNull(request.getParamValue("enableDate"));
//			String disableDate = CommonUtils.checkNull(request.getParamValue("disableDate"));
			String status = CommonUtils.checkNull(request.getParamValue("status"));
//			String rushOrderFlag = CommonUtils.checkNull(request.getParamValue("rushOrderFlag"));
			String groupCode = CommonUtils.checkNull(request.getParamValue("groupCode"));
			String orderFlag = CommonUtils.checkNull(request.getParamValue("orderFlag"));
//			String matType = CommonUtils.checkNull(request.getParamValue("mat_type")); // 物料类型
			String normalOrderFlag = CommonUtils.checkNull(request.getParamValue("normalOrderFlag"));
//			String ERPName = CommonUtils.checkNull(request.getParamValue("ERPName2"));
//			String erpPackage = CommonUtils.checkNull(request.getParamValue("erpPackage")).trim();// 内部型号
			String remark2 = CommonUtils.checkNull(request.getParamValue("remark2")).trim();// 备注
			String procuctFlag = CommonUtils.checkNull(request.getParamValue("procuctFlag"));// 生产状态
//			String isExport = CommonUtils.checkNull(request.getParamValue("is_export"));// 是否进口
//			String isInsale = CommonUtils.checkNull(request.getParamValue("isInsale"));// 是否内销
			
			// 获得物料po
			TmVhclMaterialPO material = new TmVhclMaterialPO();
			material.setMaterialId(new Long(materialId));
			material = dao.getTmVhclMaterialPO(material);
			
			// 获得物料与物料组关系po
			TmVhclMaterialGroupRPO rpo = new TmVhclMaterialGroupRPO();
			rpo.setMaterialId(new Long(materialId));
			rpo = dao.getTmVhclMaterialGroupRPO(rpo);
			
			// 获得物料组po
			TmVhclMaterialGroupPO group = new TmVhclMaterialGroupPO();
			group.setGroupCode(groupCode);
			group = dao.getTmVhclMaterialGroupPO(group);
			
			TmVhclMaterialGroupPO groupPo2 = new TmVhclMaterialGroupPO();
			groupPo2.setGroupId(group.getParentGroupId());
			groupPo2 = dao.getTmVhclMaterialGroupPO(groupPo2);
			
			// 物料更新
			TmVhclMaterialPO mCondition = new TmVhclMaterialPO();
			mCondition.setMaterialId(material.getMaterialId());
			
			TmVhclMaterialPO mValue = new TmVhclMaterialPO();
			mValue.setMaterialCode(materialCode);
			mValue.setMaterialName(materialName);
			mValue.setModelCode(modelCode);
//			mValue.setErpPackage(erpPackage);
			mValue.setRemark2(remark2);
			mValue.setProcuctFlag(new Integer(procuctFlag));
			
//			mValue.setTrimCode(trimCode == null ? null : trimCode.trim());
			
			mValue.setColorCode(colorCode);
			mValue.setColorName(colorName);
//			mValue.setModelYear(modelYear);
			
			mValue.setIssueDate(DateTimeUtil.stringToDate(issueDate));
//			mValue.setEnableDate(DateTimeUtil.stringToDate(enableDate));
//			mValue.setDisableDate(DateTimeUtil.stringToDate(disableDate));
			mValue.setErpModel(groupPo2.getGroupCode());
			mValue.setStatus(new Integer(status));
			mValue.setRushOrderFlag(Constant.NASTY_ORDER_REPORT_TYPE_01);
			mValue.setUpdateDate(new Date());
			mValue.setUpdateBy(logonUser.getUserId());
			mValue.setOrderFlag(new Integer(orderFlag));
//			mValue.setErpName(ERPName);
//			mValue.setMatType(new Integer(matType));
//			mValue.setExportSalesFlag(Integer.parseInt(isExport));//是否出口
//			mValue.setIsInsale(Integer.parseInt(isInsale));//是否内销
			if (!"".equals(normalOrderFlag))
			{
				mValue.setNormalOrderFlag(Integer.parseInt(normalOrderFlag));
			}
			
			dao.update(mCondition, mValue);
			
			// 物料与物料组关系更新
			TmVhclMaterialGroupPO rCondition1 = new TmVhclMaterialGroupPO();
			rCondition1.setGroupCode(groupCode);
			TmVhclMaterialGroupPO rCondition2 = (TmVhclMaterialGroupPO) dao.select(rCondition1).get(0);
			
			if (rpo != null)
			{
				TmVhclMaterialGroupRPO rCondition = new TmVhclMaterialGroupRPO();
				rCondition.setId(rpo.getId());
				
				TmVhclMaterialGroupRPO rValue = new TmVhclMaterialGroupRPO();
				rValue.setGroupId(group.getGroupId());
				rValue.setUpdateDate(new Date());
				rValue.setUpdateBy(logonUser.getUserId());
				
				dao.update(rCondition, rValue);
			}
			else
			{
				TmVhclMaterialGroupRPO rCondition = new TmVhclMaterialGroupRPO();
				rCondition.setId(Long.parseLong(SequenceManager.getSequence("")));
				rCondition.setMaterialId(material.getMaterialId());
				rCondition.setGroupId(rCondition2.getGroupId());
				rCondition.setUpdateDate(new Date());
				rCondition.setUpdateBy(logonUser.getUserId());
				dao.insert(rCondition);
				
			}
			// 调接口 Start
			// OSA12 o = new OSA12();
			// o.execute(mCondition);
			// //调接口 End
			act.setOutData("returnValue", 1);
			
		}
		catch (Exception e)
		{
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "物料维护");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * json获取颜色
	 */
	public void getColorByGroupCode()
	{
		String groupCode = CommonUtils.checkNull(request.getParamValue("groupCode"));
		if (!"".equals(groupCode))
		{
			List<Map<String, Object>> colorList = dao.getColorByGroupCode(groupCode);
			act.setOutData("colorList", colorList);
		}
	}
	
	/**
	 * json 获取配置名称
	 *
	 * @author wangsongwei
	 */
	public void getMaterialNameByGroupCode() {
		
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try
		{
			String groupCode = CommonUtils.checkNull(request.getParamValue("groupCode"));
			
			if (!"".equals(groupCode))
			{
				act.setOutData("name", dao.getMaterialNameByCode(groupCode));
			}
		}
		catch (Exception e)
		{
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "根据配置代码查询配置名称");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
		
	}
}
