package com.infodms.dms.actions.materialGroup;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.getCompanyId.GetOemcompanyId;
import com.infodms.dms.common.materialManager.MaterialGroupManagerDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TmVhclMaterialGroupPO;
import com.infodms.dms.util.ActionUtil;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;

/**
 * @Title: CHANADMS
 * @Description:
 * @Copyright: Copyright (c) 2010
 * @Company: www.infoservice.com.cn
 * @Date: 2010-5-27
 * @author zjy
 * @mail zhaojinyu@infoservice.com.cn
 * @version 1.0
 * @remark
 */
public class MaterialGroupTree {
	public Logger logger = Logger.getLogger(MaterialGroupTree.class);
	MaterialGroupManagerDao dao = MaterialGroupManagerDao.getInstance();
	private ActionContext act = ActionContext.getContext();
	RequestWrapper request = act.getRequest();
	
	private final String initUrl = "/dialog/showMaterialGroup.jsp";
	private final String confUrl = "/dialog/showMaterialGroupByConf.jsp";
	
	/**
	 * 物料组织树查询页面初始化
	 */
	public void MaterialGroupTreeInit()
	{
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try
		{
			act.setForword(initUrl);
		}
		catch (Exception e)
		{//异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "物料组织树查询页面初始化");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	
	/**
	 * 页面物料组树查询BY订单
	 */
	public void materialGroupByAddOrderQuery()
	{
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		Long poseId = logonUser.getPoseId();
		try
		{
			String inputId = CommonUtils.checkNull(request.getParamValue("INPUTID"));	//回显输入框ID
			String inputName = CommonUtils.checkNull(request.getParamValue("INPUTNAME"));	//回显输入框ID
			String groupLevel = CommonUtils.checkNull(request.getParamValue("GROUPLEVEL"));	//物料组名称
			String isMulti = CommonUtils.checkNull(request.getParamValue("ISMULTI"));	//单选，多选
			String groupLevel1 = CommonUtils.checkNull(request.getParamValue("GROUPLEVEL"));	//物料组等级
			String isAllArea = CommonUtils.checkNull(request.getParamValue("ISALLAREA"));	//是否过滤业务范围
			Long companyId = GetOemcompanyId.getOemCompanyId(logonUser);
			List<TmVhclMaterialGroupPO> list = dao.getMaterialGroupTreeByOrder(poseId, groupLevel1, companyId,
							isAllArea, logonUser);
			act.setOutData("INPUTID", inputId);
			act.setOutData("INPUTNAME", inputName);
			act.setOutData("GROUPLEVEL", groupLevel1);
			act.setOutData("ISMULTI", isMulti);
			act.setOutData("list", list);
			ActionUtil.setCheckedValueToOutData(act);
			//act.setForword(initUrl);
		}
		catch (Exception e)
		{//异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "页面物料组树查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	
	/**
	 * 页面物料组树查询
	 */
	public void materialGroupQuery()
	{
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		Long poseId = logonUser.getPoseId();
		try
		{
			String inputId = CommonUtils.checkNull(request.getParamValue("INPUTID"));	//回显输入框ID
			String inputName = CommonUtils.checkNull(request.getParamValue("INPUTNAME"));	//回显输入框ID
			String groupLevel = CommonUtils.checkNull(request.getParamValue("GROUPLEVEL"));	//物料组名称
			String isMulti = CommonUtils.checkNull(request.getParamValue("ISMULTI"));	//单选，多选
			String groupLevel1 = CommonUtils.checkNull(request.getParamValue("GROUPLEVEL"));	//物料组等级
			String isAllArea = CommonUtils.checkNull(request.getParamValue("ISALLAREA"));	//是否过滤业务范围
			Long companyId = GetOemcompanyId.getOemCompanyId(logonUser);
			List<TmVhclMaterialGroupPO> list = dao.getMaterialGroupTreeByPoseArea(poseId, groupLevel1, companyId,
							isAllArea, logonUser);
			act.setOutData("INPUTID", inputId);
			act.setOutData("INPUTNAME", inputName);
			act.setOutData("GROUPLEVEL", groupLevel1);
			act.setOutData("ISMULTI", isMulti);
			act.setOutData("list", list);
			ActionUtil.setCheckedValueToOutData(act);
			//act.setForword(initUrl);
		}
		catch (Exception e)
		{//异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "页面物料组树查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 页面配置(物料组)树查询
	 */
	public void materialGroupQueryByConf()
	{
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		Long poseId = logonUser.getPoseId();
		try
		{
			String inputId = CommonUtils.checkNull(request.getParamValue("INPUTID"));	//回显输入框ID
			String inputName = CommonUtils.checkNull(request.getParamValue("INPUTNAME"));	//回显输入框ID
			String groupLevel = CommonUtils.checkNull(request.getParamValue("GROUPLEVEL"));	//车系
			String isMulti = CommonUtils.checkNull(request.getParamValue("ISMULTI"));	//单选，多选
			String groupLevel1 = CommonUtils.checkNull(request.getParamValue("GROUPLEVEL"));	//物料组等级
			String areaId = CommonUtils.checkNull(request.getParamValue("AREAID"));	//是否过滤业务范围
			Long companyId = GetOemcompanyId.getOemCompanyId(logonUser);
			List<TmVhclMaterialGroupPO> list = dao.getMaterialGroupTreeByPoseAreaAndConf(poseId, groupLevel1,
							companyId, areaId, logonUser);
			act.setOutData("INPUTID", inputId);
			act.setOutData("INPUTNAME", inputName);
			act.setOutData("GROUPLEVEL", groupLevel1);
			act.setOutData("AREAID", areaId);
			act.setOutData("list", list);
			//act.setForword(initUrl);
		}
		catch (Exception e)
		{//异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "页面物料组树查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 供报表使用的物料组树查询 2012-02-07 HXY
	 */
	public void materialGroupQueryByReport()
	{
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		Long poseId = logonUser.getPoseId();
		try
		{
			String inputId = CommonUtils.checkNull(request.getParamValue("INPUTID"));	//回显输入框ID
			String inputName = CommonUtils.checkNull(request.getParamValue("INPUTNAME"));	//回显输入框ID
			String groupLevel = CommonUtils.checkNull(request.getParamValue("GROUPLEVEL"));	//物料组名称
			String isMulti = CommonUtils.checkNull(request.getParamValue("ISMULTI"));	//单选，多选
			String groupLevel1 = CommonUtils.checkNull(request.getParamValue("GROUPLEVEL"));	//物料组等级
			String isAllArea = CommonUtils.checkNull(request.getParamValue("ISALLAREA"));	//是否过滤业务范围
			Long companyId = GetOemcompanyId.getOemCompanyId(logonUser);
			List<TmVhclMaterialGroupPO> list = dao.getMaterialGroupTreeByReport(poseId, groupLevel, companyId,
							isAllArea);
			act.setOutData("INPUTID", inputId);
			act.setOutData("INPUTNAME", inputName);
			act.setOutData("GROUPLEVEL", groupLevel1);
			act.setOutData("ISMULTI", isMulti);
			act.setOutData("list", list);
			//act.setForword(initUrl);
		}
		catch (Exception e)
		{//异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "页面物料组树查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 供报表使用的物料组树查询结果 2012-02-07 HXY
	 */
	public void groupListQueryByReport()
	{
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		Long poseId = logonUser.getPoseId();
		try
		{
			String inputId = CommonUtils.checkNull(request.getParamValue("INPUTID"));	//回显输入框ID
			String inputName = CommonUtils.checkNull(request.getParamValue("INPUTNAME"));	//回显输入框ID
			String groupId = CommonUtils.checkNull(request.getParamValue("groupId"));	//物料组ID
			String groupCode = CommonUtils.checkNull(request.getParamValue("groupCode"));	//物料组代码
			String groupName = CommonUtils.checkNull(request.getParamValue("groupName"));	//物料组名称
			String groupLevel = CommonUtils.checkNull(request.getParamValue("GROUPLEVEL"));	//物料组等级
			String isMulti = CommonUtils.checkNull(request.getParamValue("ISMULTI"));	//单选，多选
			String isAllArea = CommonUtils.checkNull(request.getParamValue("ISALLAREA"));	//是否过滤业务范围
			Long companyId = GetOemcompanyId.getOemCompanyId(logonUser);
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request
							.getParamValue("curPage")) : 1;
			PageResult<Map<String, Object>> ps = dao.getGroupListByReport(poseId.toString(), groupId, groupCode,
							groupName, groupLevel, curPage, Constant.PAGE_SIZE, companyId, isAllArea);
			act.setOutData("ps", ps);
			act.setOutData("INPUTID", inputId);
			act.setOutData("INPUTNAME", inputName);
			act.setOutData("GROUPLEVEL", groupLevel);
			act.setOutData("ISMULTI", isMulti);
			act.setForword(initUrl);
		}
		catch (Exception e)
		{//异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "页面物料组树查询结果");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 订做车需求提报配置(物料组)树查询结果 2012-05-10 HXY
	 */
	public void groupListQueryByConf()
	{
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		Long poseId = logonUser.getPoseId();
		try
		{
			String inputId = CommonUtils.checkNull(request.getParamValue("INPUTID"));	//回显输入框ID
			String inputName = CommonUtils.checkNull(request.getParamValue("INPUTNAME"));	//回显输入框ID
			String groupId = CommonUtils.checkNull(request.getParamValue("groupId"));	//物料组ID
			String groupCode = CommonUtils.checkNull(request.getParamValue("groupCode"));	//物料组代码
			String groupName = CommonUtils.checkNull(request.getParamValue("groupName"));	//物料组名称
			String groupLevel = CommonUtils.checkNull(request.getParamValue("GROUPLEVEL2"));	//物料组等级
			String isMulti = CommonUtils.checkNull(request.getParamValue("ISMULTI"));	//单选，多选
			String areaId = CommonUtils.checkNull(request.getParamValue("AREAID"));	//业务范围
			Long companyId = GetOemcompanyId.getOemCompanyId(logonUser);
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request
							.getParamValue("curPage")) : 1;
			PageResult<Map<String, Object>> ps = dao.getGroupListByConf(poseId.toString(), groupId, groupCode,
							groupName, groupLevel, curPage, Constant.PAGE_SIZE, companyId, areaId, logonUser);
			act.setOutData("ps", ps);
			act.setOutData("INPUTID", inputId);
			act.setOutData("INPUTNAME", inputName);
			act.setOutData("GROUPLEVEL", groupLevel);
			act.setOutData("ISMULTI", isMulti);
			act.setForword(initUrl);
		}
		catch (Exception e)
		{//异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "页面物料组树查询结果");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 页面物料组树查询(服务活动管理)
	 */
	public void serviceMaterialGroupQuery()
	{
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		Long poseId = logonUser.getPoseId();
		try
		{
			String inputId = CommonUtils.checkNull(request.getParamValue("INPUTID"));	//回显输入框ID
			String inputName = CommonUtils.checkNull(request.getParamValue("INPUTNAME"));	//回显输入框ID
			String groupLevel = CommonUtils.checkNull(request.getParamValue("GROUPLEVEL"));	//物料组名称
			String isMulti = CommonUtils.checkNull(request.getParamValue("ISMULTI"));	//单选，多选
			String groupLevel1 = CommonUtils.checkNull(request.getParamValue("GROUPLEVEL"));	//物料组等级
			Long companyId = GetOemcompanyId.getOemCompanyId(logonUser);
			List<TmVhclMaterialGroupPO> list = dao.getMaterialGroupTree(poseId, groupLevel1, companyId);
			act.setOutData("INPUTID", inputId);
			act.setOutData("INPUTNAME", inputName);
			act.setOutData("GROUPLEVEL", groupLevel1);
			act.setOutData("ISMULTI", isMulti);
			act.setOutData("list", list);
			//act.setForword(initUrl);
		}
		catch (Exception e)
		{//异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "页面物料组树查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 页面物料组树查询:过滤已有物料组
	 */
	public void materialGroupQuery_Sel()
	{
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		Long poseId = logonUser.getPoseId();
		try
		{
			String area_id = CommonUtils.checkNull(request.getParamValue("area_id"));
			String inputId = CommonUtils.checkNull(request.getParamValue("INPUTID"));	//回显输入框ID
			String inputName = CommonUtils.checkNull(request.getParamValue("INPUTNAME"));	//回显输入框ID
			String groupLevel = CommonUtils.checkNull(request.getParamValue("GROUPLEVEL"));	//物料组名称
			String isMulti = CommonUtils.checkNull(request.getParamValue("ISMULTI"));	//单选，多选
			String groupLevel1 = CommonUtils.checkNull(request.getParamValue("GROUPLEVEL"));	//物料组等级
			
			Long companyId = GetOemcompanyId.getOemCompanyId(logonUser);
			List<TmVhclMaterialGroupPO> list = dao.getMaterialGroupTree_Sel(poseId, groupLevel1, area_id, companyId);
			act.setOutData("INPUTID", inputId);
			act.setOutData("INPUTNAME", inputName);
			//act.setOutData("GROUPLEVEL", groupLevel1);
			act.setOutData("ISMULTI", isMulti);
			act.setOutData("list", list);
			//act.setForword(initUrl);
		}
		catch (Exception e)
		{//异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "页面物料组树查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 页面物料组树查询:过滤已有物料组
	 */
	public void materialCarTypeGroupQuery_Sel()
	{
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		Long poseId = logonUser.getPoseId();
		try
		{
			String area_id = CommonUtils.checkNull(request.getParamValue("area_id"));
			String inputId = CommonUtils.checkNull(request.getParamValue("INPUTID"));	//回显输入框ID
			String inputName = CommonUtils.checkNull(request.getParamValue("INPUTNAME"));	//回显输入框ID
			String groupLevel = CommonUtils.checkNull(request.getParamValue("GROUPLEVEL"));	//物料组名称
			String isMulti = CommonUtils.checkNull(request.getParamValue("ISMULTI"));	//单选，多选
			String groupLevel1 = CommonUtils.checkNull(request.getParamValue("GROUPLEVEL"));	//物料组等级
			
			Long companyId = GetOemcompanyId.getOemCompanyId(logonUser);
			List<TmVhclMaterialGroupPO> list = dao.getMaterialCarTypeGroupTree_Sel(poseId, groupLevel1, area_id,
							companyId);
			act.setOutData("INPUTID", inputId);
			act.setOutData("INPUTNAME", inputName);
			//act.setOutData("GROUPLEVEL", groupLevel1);
			act.setOutData("ISMULTI", isMulti);
			act.setOutData("list", list);
			//act.setForword(initUrl);
		}
		catch (Exception e)
		{//异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "页面物料组树查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 物料组树查询结果
	 */
	public void groupListQuery()
	{
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		Long poseId = logonUser.getPoseId();
		try
		{
			String inputId = CommonUtils.checkNull(request.getParamValue("INPUTID"));	//回显输入框ID
			String inputName = CommonUtils.checkNull(request.getParamValue("INPUTNAME"));	//回显输入框ID
			String groupId = CommonUtils.checkNull(request.getParamValue("groupId"));	//物料组ID
			String groupCode = CommonUtils.checkNull(request.getParamValue("groupCode"));	//物料组代码
			String groupName = CommonUtils.checkNull(request.getParamValue("groupName"));	//物料组名称
			String groupLevel = CommonUtils.checkNull(request.getParamValue("GROUPLEVEL"));	//物料组等级
			String groupLevel2 = CommonUtils.checkNull(request.getParamValue("GROUPLEVEL2"));	//物料组等级
			String isMulti = CommonUtils.checkNull(request.getParamValue("ISMULTI"));	//单选，多选
			String isAllArea = CommonUtils.checkNull(request.getParamValue("ISALLAREA"));	//是否过滤业务范围
			Long companyId = GetOemcompanyId.getOemCompanyId(logonUser);
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request
							.getParamValue("curPage")) : 1;
			PageResult<Map<String, Object>> ps = dao.getGroupListByPoseArea(poseId.toString(), groupId, groupCode,
							groupName, groupLevel2, curPage, ActionUtil.getPageSize(request), companyId, isAllArea);
			act.setOutData("ps", ps);
			act.setOutData("INPUTID", inputId);
			act.setOutData("INPUTNAME", inputName);
			act.setOutData("GROUPLEVEL", groupLevel);
			act.setOutData("ISMULTI", isMulti);
			act.setForword(initUrl);
			// 复选框翻页选中 by chenyub@yonyou.com
			ActionUtil.setCheckedValueToOutData(act);
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
		{//异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "页面物料组树查询结果");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 物料组查询 计划处，K机国机设置
	 */
	public void groupListQueryPlanSet()
	{
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		Long poseId = logonUser.getPoseId();
		try
		{
			String inputId = CommonUtils.checkNull(request.getParamValue("INPUTID"));	//回显输入框ID
			String inputName = CommonUtils.checkNull(request.getParamValue("INPUTNAME"));	//回显输入框ID
			String groupId = CommonUtils.checkNull(request.getParamValue("groupId"));	//物料组ID
			String groupCode = CommonUtils.checkNull(request.getParamValue("groupCode"));	//物料组代码
			String groupName = CommonUtils.checkNull(request.getParamValue("groupName"));	//物料组名称
			String groupLevel = CommonUtils.checkNull(request.getParamValue("GROUPLEVEL"));	//物料组等级
			String groupLevel2 = CommonUtils.checkNull(request.getParamValue("GROUPLEVEL2"));	//物料组等级
			String isMulti = CommonUtils.checkNull(request.getParamValue("ISMULTI"));	//单选，多选
			String isAllArea = CommonUtils.checkNull(request.getParamValue("ISALLAREA"));	//是否过滤业务范围
			Long companyId = GetOemcompanyId.getOemCompanyId(logonUser);
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request
							.getParamValue("curPage")) : 1;
			PageResult<Map<String, Object>> ps = dao.getGroupListByPoseArea(poseId.toString(), groupId, groupCode,
							groupName, groupLevel2, curPage, Constant.PAGE_SIZE_MAX, companyId, isAllArea);
			act.setOutData("ps", ps);
			act.setOutData("INPUTID", inputId);
			act.setOutData("INPUTNAME", inputName);
			act.setOutData("GROUPLEVEL", groupLevel);
			act.setOutData("ISMULTI", isMulti);
			act.setForword(initUrl);
		}
		catch (Exception e)
		{//异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "页面物料组树查询结果");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	
	/**
	 * 物料组树查询结果
	 */
	public void groupListByAreaQuery()
	{
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		Long poseId = logonUser.getPoseId();
		try
		{
			String inputId = CommonUtils.checkNull(request.getParamValue("INPUTID"));	//回显输入框ID
			String inputName = CommonUtils.checkNull(request.getParamValue("INPUTNAME"));	//回显输入框ID
			String groupId = CommonUtils.checkNull(request.getParamValue("groupId"));	//物料组ID
			String groupCode = CommonUtils.checkNull(request.getParamValue("groupCode"));	//物料组代码
			String groupName = CommonUtils.checkNull(request.getParamValue("groupName"));	//物料组名称
			String groupLevel = CommonUtils.checkNull(request.getParamValue("GROUPLEVEL"));	//物料组等级
			String groupLevel2 = CommonUtils.checkNull(request.getParamValue("GROUPLEVEL2"));	//物料组等级
			String isMulti = CommonUtils.checkNull(request.getParamValue("ISMULTI"));	//单选，多选
			String isAllArea = CommonUtils.checkNull(request.getParamValue("ISALLAREA"));	//是否过滤业务范围
			Long companyId = GetOemcompanyId.getOemCompanyId(logonUser);
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request
							.getParamValue("curPage")) : 1;
			PageResult<Map<String, Object>> ps = dao.getGroupListByArea(poseId.toString(), groupId, groupCode,
							groupName, groupLevel2, curPage, Constant.PAGE_SIZE, companyId, isAllArea);
			act.setOutData("ps", ps);
			act.setOutData("INPUTID", inputId);
			act.setOutData("INPUTNAME", inputName);
			act.setOutData("GROUPLEVEL", groupLevel);
			act.setOutData("ISMULTI", isMulti);
			act.setForword(initUrl);
		}
		catch (Exception e)
		{//异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "页面物料组树查询结果");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	public void groupListByAreaQueryPro()
	{//承诺选择车系专用
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		Long poseId = logonUser.getPoseId();
		try
		{
			String inputId = CommonUtils.checkNull(request.getParamValue("INPUTID"));	//回显输入框ID
			String inputName = CommonUtils.checkNull(request.getParamValue("INPUTNAME"));	//回显输入框ID
			String groupId = CommonUtils.checkNull(request.getParamValue("groupId"));	//物料组ID
			String groupCode = CommonUtils.checkNull(request.getParamValue("groupCode"));	//物料组代码
			String groupName = CommonUtils.checkNull(request.getParamValue("groupName"));	//物料组名称
			String groupLevel = CommonUtils.checkNull(request.getParamValue("GROUPLEVEL"));	//物料组等级
			String groupLevel2 = CommonUtils.checkNull(request.getParamValue("GROUPLEVEL2"));	//物料组等级
			String isMulti = CommonUtils.checkNull(request.getParamValue("ISMULTI"));	//单选，多选
			String isAllArea = CommonUtils.checkNull(request.getParamValue("ISALLAREA"));	//是否过滤业务范围
			Long companyId = GetOemcompanyId.getOemCompanyId(logonUser);
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request
							.getParamValue("curPage")) : 1;
			PageResult<Map<String, Object>> ps = dao.getGroupListByAreaPro(poseId.toString(), groupId, groupCode,
							groupName, groupLevel2, curPage, Constant.PAGE_SIZE, companyId, isAllArea);
			act.setOutData("ps", ps);
			act.setOutData("INPUTID", inputId);
			act.setOutData("INPUTNAME", inputName);
			act.setOutData("GROUPLEVEL", groupLevel);
			act.setOutData("ISMULTI", isMulti);
			act.setForword(initUrl);
		}
		catch (Exception e)
		{//异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "页面物料组树查询结果");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	//zhumingwei add 2011-6-22
	public void groupListQuery1()
	{
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		Long poseId = logonUser.getPoseId();
		try
		{
			String inputId = CommonUtils.checkNull(request.getParamValue("INPUTID"));	//回显输入框ID
			String inputName = CommonUtils.checkNull(request.getParamValue("INPUTNAME"));	//回显输入框ID
			String groupId = CommonUtils.checkNull(request.getParamValue("groupId"));	//物料组ID
			String groupCode = CommonUtils.checkNull(request.getParamValue("groupCode"));	//物料组代码
			String groupName = CommonUtils.checkNull(request.getParamValue("groupName"));	//物料组名称
			String groupLevel = CommonUtils.checkNull(request.getParamValue("GROUPLEVEL"));	//物料组等级
			String groupLevel2 = CommonUtils.checkNull(request.getParamValue("GROUPLEVEL2"));	//物料组等级
			String isMulti = CommonUtils.checkNull(request.getParamValue("ISMULTI"));	//单选，多选
			String isAllArea = CommonUtils.checkNull(request.getParamValue("ISALLAREA"));	//是否过滤业务范围
			
			String groupIdXi = CommonUtils.checkNull(request.getParamValue("groupIdXi"));	//是否过滤业务范围
			String cxzid = CommonUtils.checkNull(request.getParamValue("cxzid"));	//是否过滤业务范围
			
			Long companyId = GetOemcompanyId.getOemCompanyId(logonUser);
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request
							.getParamValue("curPage")) : 1;
			PageResult<Map<String, Object>> ps = dao.getGroupListByPoseArea1(groupIdXi, cxzid, poseId.toString(),
							groupId, groupCode, groupName, groupLevel2, curPage, Constant.PAGE_SIZE, companyId,
							isAllArea);
			act.setOutData("ps", ps);
			act.setOutData("INPUTID", inputId);
			act.setOutData("INPUTNAME", inputName);
			act.setOutData("GROUPLEVEL", groupLevel);
			act.setOutData("ISMULTI", isMulti);
			act.setForword(initUrl);
		}
		catch (Exception e)
		{//异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "页面物料组树查询结果");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 物料组树查询结果:过滤已有物料组树
	 */
	public void groupListQuery_Sel()
	{
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		Long poseId = logonUser.getPoseId();
		try
		{
			String area_id = CommonUtils.checkNull(request.getParamValue("area_id"));
			String inputId = CommonUtils.checkNull(request.getParamValue("INPUTID"));	//回显输入框ID
			String inputName = CommonUtils.checkNull(request.getParamValue("INPUTNAME"));	//回显输入框ID
			String groupId = CommonUtils.checkNull(request.getParamValue("groupId"));	//物料组ID
			String groupCode = CommonUtils.checkNull(request.getParamValue("groupCode"));	//物料组代码
			String groupName = CommonUtils.checkNull(request.getParamValue("groupName"));	//物料组名称
			String groupLevel = CommonUtils.checkNull(request.getParamValue("GROUPLEVEL"));	//物料组等级
			String groupLevel2 = CommonUtils.checkNull(request.getParamValue("GROUPLEVEL2"));	//物料组等级
			String isMulti = CommonUtils.checkNull(request.getParamValue("ISMULTI"));	//单选，多选
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request
							.getParamValue("curPage")) : 1;
			Long companyId = GetOemcompanyId.getOemCompanyId(logonUser);
			//List<Map<String,Object>> treeCodeList = dao.getTreeCodeByAreaId(area_id);
			
			PageResult<Map<String, Object>> ps = dao.getGroupList_Sel(groupId, groupCode, groupName, groupLevel2,
							area_id, curPage, Constant.PAGE_SIZE, companyId);
			act.setOutData("ps", ps);
			act.setOutData("INPUTID", inputId);
			act.setOutData("INPUTNAME", inputName);
			act.setOutData("GROUPLEVEL", groupLevel);
			act.setOutData("ISMULTI", isMulti);
			act.setForword(initUrl);
		}
		catch (Exception e)
		{//异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "页面物料组树查询结果");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	//zhumingwei 2011-10-24
	public void groupListQuery_Sel111()
	{
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		Long poseId = logonUser.getPoseId();
		try
		{
			String area_id = CommonUtils.checkNull(request.getParamValue("area_id"));
			String inputId = CommonUtils.checkNull(request.getParamValue("INPUTID"));	//回显输入框ID
			String inputName = CommonUtils.checkNull(request.getParamValue("INPUTNAME"));	//回显输入框ID
			String groupId = CommonUtils.checkNull(request.getParamValue("groupId"));	//物料组ID
			String groupCode = CommonUtils.checkNull(request.getParamValue("groupCode"));	//物料组代码
			String groupName = CommonUtils.checkNull(request.getParamValue("groupName"));	//物料组名称
			String groupLevel = CommonUtils.checkNull(request.getParamValue("GROUPLEVEL"));	//物料组等级
			String groupLevel2 = CommonUtils.checkNull(request.getParamValue("GROUPLEVEL2"));	//物料组等级
			String isMulti = CommonUtils.checkNull(request.getParamValue("ISMULTI"));	//单选，多选
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request
							.getParamValue("curPage")) : 1;
			Long companyId = GetOemcompanyId.getOemCompanyId(logonUser);
			//List<Map<String,Object>> treeCodeList = dao.getTreeCodeByAreaId(area_id);
			
			PageResult<Map<String, Object>> ps = dao.getGroupList_Sel111(groupId, groupCode, groupName, groupLevel2,
							area_id, curPage, Constant.PAGE_SIZE, companyId);
			act.setOutData("ps", ps);
			act.setOutData("INPUTID", inputId);
			act.setOutData("INPUTNAME", inputName);
			act.setOutData("GROUPLEVEL", groupLevel);
			act.setOutData("ISMULTI", isMulti);
			act.setForword(initUrl);
		}
		catch (Exception e)
		{//异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "页面物料组树查询结果");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 物料组树查询结果:过滤业务范围内的车型
	 */
	public void groupListCarTypeQuery_Sel()
	{
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		Long poseId = logonUser.getPoseId();
		try
		{
			String area_id = CommonUtils.checkNull(request.getParamValue("area_id"));
			String inputId = CommonUtils.checkNull(request.getParamValue("INPUTID"));	//回显输入框ID
			String inputName = CommonUtils.checkNull(request.getParamValue("INPUTNAME"));	//回显输入框ID
			String groupId = CommonUtils.checkNull(request.getParamValue("groupId"));	//物料组ID
			String groupCode = CommonUtils.checkNull(request.getParamValue("groupCode"));	//物料组代码
			String groupName = CommonUtils.checkNull(request.getParamValue("groupName"));	//物料组名称
			String groupLevel = CommonUtils.checkNull(request.getParamValue("GROUPLEVEL"));	//物料组等级
			String isMulti = CommonUtils.checkNull(request.getParamValue("ISMULTI"));	//单选，多选
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request
							.getParamValue("curPage")) : 1;
			Long companyId = GetOemcompanyId.getOemCompanyId(logonUser);
			//List<Map<String,Object>> treeCodeList = dao.getTreeCodeByAreaId(area_id);
			
			PageResult<Map<String, Object>> ps = dao.getGroupCarTypeList_Sel(groupId, groupCode, groupName, groupLevel,
							area_id, curPage, Constant.PAGE_SIZE, companyId);
			act.setOutData("ps", ps);
			act.setOutData("INPUTID", inputId);
			act.setOutData("INPUTNAME", inputName);
			act.setOutData("GROUPLEVEL", groupLevel);
			act.setOutData("ISMULTI", isMulti);
			act.setForword(initUrl);
		}
		catch (Exception e)
		{//异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "页面物料组树查询结果");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 页面物料树查询
	 */
	public void materialQuery()
	{
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		Long poseId = logonUser.getPoseId();
		try
		{
			String inputId = CommonUtils.checkNull(request.getParamValue("INPUTID"));	//回显输入框ID
			String inputName = CommonUtils.checkNull(request.getParamValue("INPUTNAME"));	//回显输入框ID
			String groupLevel = CommonUtils.checkNull(request.getParamValue("GROUPLEVEL"));	//物料组名称
			String isMulti = CommonUtils.checkNull(request.getParamValue("ISMULTI"));	//单选，多选
			String isAllArea = CommonUtils.checkNull(request.getParamValue("ISALLAREA"));	//是否过滤业务范围
			String groupLevel1 = "4";	//物料组等级
			Long companyId = GetOemcompanyId.getOemCompanyId(logonUser);
			List<TmVhclMaterialGroupPO> list = dao.getMaterialGroupTreeByPoseArea(poseId, groupLevel, companyId,
							isAllArea, logonUser);
			act.setOutData("INPUTID", inputId);
			act.setOutData("INPUTNAME", inputName);
			act.setOutData("ISMULTI", isMulti);
			act.setOutData("list", list);
			//act.setForword(initUrl);
		}
		catch (Exception e)
		{//异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "页面物料树查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 物料树查询结果
	 */
	public void materialListQuery()
	{
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		Long poseId = logonUser.getPoseId();
		try
		{
			String inputId = CommonUtils.checkNull(request.getParamValue("INPUTID"));	//回显输入框ID
			String inputName = CommonUtils.checkNull(request.getParamValue("INPUTNAME"));	//回显输入框ID
			String groupId = CommonUtils.checkNull(request.getParamValue("groupId"));	//物料组ID
			String materialCode = CommonUtils.checkNull(request.getParamValue("materialCode"));	//物料组代码
			String materialName = CommonUtils.checkNull(request.getParamValue("materialName"));	//物料组名称
			String isMulti = CommonUtils.checkNull(request.getParamValue("ISMULTI"));	//单选，多选
			String isAllArea = CommonUtils.checkNull(request.getParamValue("ISALLAREA"));	//是否过滤业务范围
			String inputColorName = CommonUtils.checkNull(request.getParamValue("inputColorName"));	//是否过滤业务范围
			Long companyId = GetOemcompanyId.getOemCompanyId(logonUser);
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request
							.getParamValue("curPage")) : 1;
			PageResult<Map<String, Object>> ps = dao.getMaterialListByPoseArea(logonUser.getPoseId().toString(),
							groupId, materialCode, materialName, curPage, ActionUtil.getPageSize(request), companyId, isAllArea);
			act.setOutData("ps", ps);
			act.setOutData("INPUTID", inputId);
			act.setOutData("INPUTNAME", inputName);
			act.setOutData("ISMULTI", isMulti);
			act.setOutData("inputColorName", inputColorName);
			act.setForword(initUrl);
			// 复选框翻页选中 by chenyub@yonyou.com
			ActionUtil.setCheckedValueToOutData(act);
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
		{//异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "物料树查询结果");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 物料树查询结果-根据仓库筛选
	 */
	public void materialListQueryByware()
	{
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		Long poseId = logonUser.getPoseId();
		try
		{
			String inputId = CommonUtils.checkNull(request.getParamValue("INPUTID"));	//回显输入框ID
			String inputName = CommonUtils.checkNull(request.getParamValue("INPUTNAME"));	//回显输入框ID
			String groupId = CommonUtils.checkNull(request.getParamValue("groupId"));	//物料组ID
			String materialCode = CommonUtils.checkNull(request.getParamValue("materialCode"));	//物料组代码
			String materialName = CommonUtils.checkNull(request.getParamValue("materialName"));	//物料组名称
			String isMulti = CommonUtils.checkNull(request.getParamValue("ISMULTI"));	//单选，多选
			//String isAllArea = CommonUtils.checkNull(request.getParamValue("ISALLAREA"));	//是否过滤业务范围
			String sendWare = CommonUtils.checkNull(request.getParamValue("SENDWARE"));	//发运仓库
			String receiveWare = CommonUtils.checkNull(request.getParamValue("RECEIVEWARE"));	//收货仓库
			//String inputColorName = CommonUtils.checkNull(request.getParamValue("inputColorName"));	//是否过滤业务范围
			Long companyId = GetOemcompanyId.getOemCompanyId(logonUser);
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request
							.getParamValue("curPage")) : 1;
			PageResult<Map<String, Object>> ps = dao.getMaterialListByWarehouse(groupId, materialCode, materialName, curPage, ActionUtil.getPageSize(request), sendWare, receiveWare);
			act.setOutData("ps", ps);
			act.setOutData("INPUTID", inputId);
			act.setOutData("INPUTNAME", inputName);
			act.setOutData("ISMULTI", isMulti);
			//act.setOutData("inputColorName", inputColorName);
			act.setForword(initUrl);
			// 复选框翻页选中 by chenyub@yonyou.com
			ActionUtil.setCheckedValueToOutData(act);
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
		{//异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "物料树查询结果");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	public void QueryColorName()
	{
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		Long poseId = logonUser.getPoseId();
		try
		{
			String group_id = CommonUtils.checkNull(request.getParamValue("group_id"));	//回显输入框ID
			String inputName = CommonUtils.checkNull(request.getParamValue("INPUTNAME"));	//回显输入框ID
			String groupId = CommonUtils.checkNull(request.getParamValue("groupId"));	//物料组ID
			String materialCode = CommonUtils.checkNull(request.getParamValue("materialCode"));	//物料组代码
			String materialName = CommonUtils.checkNull(request.getParamValue("materialName"));	//物料组名称
			String isMulti = CommonUtils.checkNull(request.getParamValue("ISMULTI"));	//单选，多选
			String isAllArea = CommonUtils.checkNull(request.getParamValue("ISALLAREA"));	//是否过滤业务范围
			String inputColorName = CommonUtils.checkNull(request.getParamValue("inputColorName"));	//是否过滤业务范围
			Long companyId = GetOemcompanyId.getOemCompanyId(logonUser);
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request
							.getParamValue("curPage")) : 1;
			PageResult<Map<String, Object>> ps = dao.QueryColorName(group_id,logonUser.getPoseId().toString(),
							groupId, materialCode, materialName, curPage, Constant.PAGE_SIZE, companyId, isAllArea);
			act.setOutData("ps", ps);
//			act.setOutData("INPUTID", inputId);
			act.setOutData("INPUTNAME", inputName);
			act.setOutData("ISMULTI", isMulti);
			act.setOutData("inputColorName", inputColorName);
			act.setForword(initUrl);
		}
		catch (Exception e)
		{//异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "物料树查询结果");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	public void QueryPackageName()
	{
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		Long poseId = logonUser.getPoseId();
		try
		{
			String group_id = CommonUtils.checkNull(request.getParamValue("group_id"));	//回显输入框ID
			String inputName = CommonUtils.checkNull(request.getParamValue("INPUTNAME"));	//回显输入框ID
			String groupId = CommonUtils.checkNull(request.getParamValue("groupId"));	//物料组ID
			String materialCode = CommonUtils.checkNull(request.getParamValue("materialCode"));	//物料组代码
			String materialName = CommonUtils.checkNull(request.getParamValue("materialName"));	//物料组名称
			String isMulti = CommonUtils.checkNull(request.getParamValue("ISMULTI"));	//单选，多选
			String isAllArea = CommonUtils.checkNull(request.getParamValue("ISALLAREA"));	//是否过滤业务范围
			String inputColorName = CommonUtils.checkNull(request.getParamValue("inputColorName"));	//是否过滤业务范围
			Long companyId = GetOemcompanyId.getOemCompanyId(logonUser);
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request
							.getParamValue("curPage")) : 1;
			PageResult<Map<String, Object>> ps = dao.QueryPackageName(group_id,logonUser.getPoseId().toString(),
							groupId, materialCode, materialName, curPage, Constant.PAGE_SIZE, companyId, isAllArea);
			act.setOutData("ps", ps);
			act.setOutData("INPUTNAME", inputName);
			act.setOutData("ISMULTI", isMulti);
			act.setOutData("inputColorName", inputColorName);
			act.setForword(initUrl);
		}
		catch (Exception e)
		{//异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "物料树查询结果");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 页面物料树查询by areaId
	 */
	public void materialQueryByAreaId()
	{
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		Long poseId = logonUser.getPoseId();
		try
		{
			String inputId = request.getParamValue("INPUTID");	//回显输入框ID
			String inputName = request.getParamValue("INPUTNAME");	//回显输入框ID
			String groupLevel = request.getParamValue("GROUPLEVEL");	//物料组名称
			String isMulti = request.getParamValue("ISMULTI");	//单选，多选
			String areaId = request.getParamValue("areaId"); //业务范围id
			String groupLevel1 = "4";	//物料组等级
			Long companyId = GetOemcompanyId.getOemCompanyId(logonUser);
			List<TmVhclMaterialGroupPO> list = dao.getMaterialGroupTreeByAreaId(areaId, companyId, groupLevel,
							logonUser);
			act.setOutData("INPUTID", inputId);
			act.setOutData("INPUTNAME", inputName);
			act.setOutData("ISMULTI", isMulti);
			act.setOutData("list", list);
			//act.setForword(initUrl);
		}
		catch (Exception e)
		{//异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "页面物料树查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 页面物料树查询by areaId--微车
	 */
	public void materialQueryByAreaId_Mini()
	{
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		Long poseId = logonUser.getPoseId();
		try
		{
			String inputId = request.getParamValue("INPUTID");	//回显输入框ID
			String inputName = request.getParamValue("INPUTNAME");	//回显输入框ID
			String groupLevel = request.getParamValue("GROUPLEVEL");	//物料组名称
			String isMulti = request.getParamValue("ISMULTI");	//单选，多选
			String areaId = request.getParamValue("areaId"); //业务范围id
			String groupLevel1 = "4";	//物料组等级
			Long companyId = GetOemcompanyId.getOemCompanyId(logonUser);
			List<TmVhclMaterialGroupPO> list = dao.getMaterialGroupTreeByAreaId_Mini(areaId, companyId);
			act.setOutData("INPUTID", inputId);
			act.setOutData("INPUTNAME", inputName);
			act.setOutData("ISMULTI", isMulti);
			act.setOutData("list", list);
			//act.setForword(initUrl);
		}
		catch (Exception e)
		{//异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "页面物料树查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 物料树查询结果
	 */
	public void materialListQueryByAreaId()
	{
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		Long poseId = logonUser.getPoseId();
		try
		{
			String inputId = request.getParamValue("INPUTID");	//回显输入框ID
			String inputName = request.getParamValue("INPUTNAME");	//回显输入框ID
			String productId = request.getParamValue("productId");
			String groupId = request.getParamValue("groupId");	//物料组ID
			String materialCode = request.getParamValue("materialCode");	//物料组代码
			String materialName = request.getParamValue("materialName");	//物料组名称
			String isMulti = request.getParamValue("ISMULTI");	//单选，多选
			String areaId = request.getParamValue("areaId"); //业务范围id
			String ids = request.getParamValue("ids"); //业务范围id
			String matType = request.getParamValue("mat_type"); //物料类型
			Long companyId = GetOemcompanyId.getOemCompanyId(logonUser);
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request
							.getParamValue("curPage")) : 1;
			PageResult<Map<String, Object>> ps = dao.getMaterialListByAreaId(productId, areaId, ids, groupId,
							materialCode, materialName, curPage, Constant.PAGE_SIZE, companyId, logonUser,matType);
			act.setOutData("ps", ps);
			act.setOutData("INPUTID", inputId);
			act.setOutData("INPUTNAME", inputName);
			act.setOutData("ISMULTI", isMulti);
			act.setForword(initUrl);
		}
		catch (Exception e)
		{//异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "物料树查询结果");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 查询可用于生产的物料数据
	 */
	public void materialListQueryByAreaIdForProduct()
	{
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		Long poseId = logonUser.getPoseId();
		try
		{
			String inputId = request.getParamValue("INPUTID");	//回显输入框ID
			String inputName = request.getParamValue("INPUTNAME");	//回显输入框ID
			String groupId = request.getParamValue("groupId");	//物料组ID
			String materialCode = request.getParamValue("materialCode");	//物料组代码
			String materialName = request.getParamValue("materialName");	//物料组名称
			String isMulti = request.getParamValue("ISMULTI");	//单选，多选
			String areaId = request.getParamValue("areaId"); //业务范围id

			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request
							.getParamValue("curPage")) : 1;
			
			Map<String, Object> paramMap = new HashMap<String, Object>();
			
			paramMap.put("groupId", groupId);
			paramMap.put("materialCode", materialCode);
			paramMap.put("materialName", materialName);
			paramMap.put("areaId", areaId);
			
			PageResult<Map<String, Object>> ps = dao.getMaterialListByAreaIdForProduct(paramMap, curPage, Constant.PAGE_SIZE, logonUser);
			act.setOutData("ps", ps);
			act.setOutData("INPUTID", inputId);
			act.setOutData("INPUTNAME", inputName);
			act.setOutData("ISMULTI", isMulti);
			act.setForword(initUrl);
		}
		catch (Exception e)
		{
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "物料树查询结果");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 物料树查询结果(带价格)
	 */
	public void materialListQueryWithPriceByAreaId()
	{
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		Long poseId = logonUser.getPoseId();
		try
		{
			String inputId = request.getParamValue("INPUTID");	//回显输入框ID
			String inputName = request.getParamValue("INPUTNAME");	//回显输入框ID
			String productId = request.getParamValue("productId");
			String groupId = request.getParamValue("groupId");	//物料组ID
			String materialCode = request.getParamValue("materialCode");	//物料组代码
			String materialName = request.getParamValue("materialName");	//物料组名称
			String isMulti = request.getParamValue("ISMULTI");	//单选，多选
			String areaId = request.getParamValue("areaId"); //业务范围id
			String dealerId = logonUser.getDealerId();
			String ids = request.getParamValue("ids"); //业务范围id
			String resAmount = CommonUtils.checkNull(request.getParamValue("resAmount")); //资源情况
			Long companyId = GetOemcompanyId.getOemCompanyId(logonUser);
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request
							.getParamValue("curPage")) : 1;
			PageResult<Map<String, Object>> ps = dao.getMaterialListWithPriceByAreaId(productId, areaId, dealerId, ids,
							groupId,
							materialCode, materialName, curPage, Constant.PAGE_SIZE, companyId, logonUser,resAmount);
			act.setOutData("ps", ps);
			act.setOutData("INPUTID", inputId);
			act.setOutData("INPUTNAME", inputName);
			act.setOutData("ISMULTI", isMulti);
			act.setForword(initUrl);
		}
		catch (Exception e)
		{//异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "物料树查询结果");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 物料树查询结果
	 */
	public void materialListQueryByAreaIdAndGroupId()
	{
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		Long poseId = logonUser.getPoseId();
		try
		{
			String inputId = request.getParamValue("INPUTID");	//回显输入框ID
			String inputName = request.getParamValue("INPUTNAME");	//回显输入框ID
			String productId = request.getParamValue("productId");
			String groupId = request.getParamValue("groupId");	//物料组ID
			String materialCode = request.getParamValue("materialCode");	//物料组代码
			String materialName = request.getParamValue("materialName");	//物料组名称
			String isMulti = request.getParamValue("ISMULTI");	//单选，多选
			String areaId = request.getParamValue("areaId"); //业务范围id
			String gids = request.getParamValue("gids"); //物料组ID
			Long companyId = GetOemcompanyId.getOemCompanyId(logonUser);
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request
							.getParamValue("curPage")) : 1;
			PageResult<Map<String, Object>> ps = dao.getMaterialListByAreaIdAndGroupId(productId, areaId, gids,
							groupId, materialCode, materialName, curPage, Constant.PAGE_SIZE, companyId);
			act.setOutData("ps", ps);
			act.setOutData("INPUTID", inputId);
			act.setOutData("INPUTNAME", inputName);
			act.setOutData("ISMULTI", isMulti);
			act.setForword(initUrl);
		}
		catch (Exception e)
		{//异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "物料树查询结果");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 物料树查询结果
	 */
	public void materialListQueryByAreaIdAndOrderType()
	{
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try
		{
			String inputId = request.getParamValue("INPUTID");	//回显输入框ID
			String inputName = request.getParamValue("INPUTNAME");	//回显输入框ID
			String productId = request.getParamValue("productId");
			String groupId = request.getParamValue("groupId");	//物料组ID
			String materialCode = request.getParamValue("materialCode");	//物料组代码
			String materialName = request.getParamValue("materialName");	//物料组名称
			String isMulti = request.getParamValue("ISMULTI");	//单选，多选
			String areaId = request.getParamValue("areaId"); //业务范围id
			String ids = request.getParamValue("ids"); //业务范围id
			String orderType = request.getParamValue("orderType"); //订单类型
			Long companyId = GetOemcompanyId.getOemCompanyId(logonUser);
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request
							.getParamValue("curPage")) : 1;
			PageResult<Map<String, Object>> ps = dao.getMaterialListByAreaIdAndOrderType(productId, areaId, ids,
							groupId, materialCode, materialName, orderType, curPage, Constant.PAGE_SIZE, companyId);
			act.setOutData("ps", ps);
			act.setOutData("INPUTID", inputId);
			act.setOutData("INPUTNAME", inputName);
			act.setOutData("ISMULTI", isMulti);
			act.setForword(initUrl);
		}
		catch (Exception e)
		{//异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "物料树查询结果");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 物料树查询结果--微车
	 */
	public void materialListQueryByAreaId_Mini()
	{
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		Long poseId = logonUser.getPoseId();
		try
		{
			String inputId = request.getParamValue("INPUTID");	//回显输入框ID
			String inputName = request.getParamValue("INPUTNAME");	//回显输入框ID
			String groupId = request.getParamValue("groupId");	//物料组ID
			String productId = request.getParamValue("productId");
			String materialCode = request.getParamValue("materialCode");	//物料组代码
			String materialName = request.getParamValue("materialName");	//物料组名称
			String isMulti = request.getParamValue("ISMULTI");	//单选，多选
			String areaId = request.getParamValue("areaId"); //业务范围id
			String ids = request.getParamValue("ids"); //业务范围id
			Long companyId = GetOemcompanyId.getOemCompanyId(logonUser);
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request
							.getParamValue("curPage")) : 1;
			PageResult<Map<String, Object>> ps = dao.getMaterialListByAreaId_Mini(productId, areaId, ids, groupId,
							materialCode, materialName, curPage, Constant.PAGE_SIZE, companyId);
			act.setOutData("ps", ps);
			act.setOutData("INPUTID", inputId);
			act.setOutData("INPUTNAME", inputName);
			act.setOutData("ISMULTI", isMulti);
			act.setForword(initUrl);
		}
		catch (Exception e)
		{//异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "物料树查询结果");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 页面物料组树查询
	 */
	public void materialCarTypeGroupQuery()
	{
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		Long poseId = logonUser.getPoseId();
		try
		{
			String inputId = CommonUtils.checkNull(request.getParamValue("INPUTID"));	//回显输入框ID
			String inputName = CommonUtils.checkNull(request.getParamValue("INPUTNAME"));	//回显输入框ID
			String groupLevel = CommonUtils.checkNull(request.getParamValue("GROUPLEVEL"));	//物料组名称
			String isMulti = CommonUtils.checkNull(request.getParamValue("ISMULTI"));	//单选，多选
			String groupLevel1 = CommonUtils.checkNull(request.getParamValue("GROUPLEVEL"));	//物料组等级
			String isAllArea = CommonUtils.checkNull(request.getParamValue("ISALLAREA"));	//是否过滤业务范围
			Long companyId = GetOemcompanyId.getOemCompanyId(logonUser);
			List<TmVhclMaterialGroupPO> list = dao.getMaterialGroupTreeByPoseArea(poseId, groupLevel1, companyId,
							isAllArea, logonUser);
			act.setOutData("INPUTID", inputId);
			act.setOutData("INPUTNAME", inputName);
			act.setOutData("GROUPLEVEL", groupLevel1);
			act.setOutData("ISMULTI", isMulti);
			act.setOutData("list", list);
			//act.setForword(initUrl);
		}
		catch (Exception e)
		{//异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "页面物料组树查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	public void querySubMaterialList()
	{
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		Long poseId = logonUser.getPoseId();
		try
		{
			
			String groupId = request.getParamValue("groupId");
			String groupLevel = request.getParamValue("groupLevel");
			String isAllArea = CommonUtils.checkNull(request.getParamValue("ISALLAREA"));	//是否过滤业务范围
			
			List mmmList = dao.querySubMaterialGroupList(poseId.toString(), groupId, groupLevel, isAllArea);
			act.setOutData("subNodeList", mmmList);
		}
		catch (Exception e)
		{//异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "页面物料组树查询结果");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 订做车需求提报查询配置(物料组)
	 */
	public void querySubMaterialListByConf()
	{
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		Long poseId = logonUser.getPoseId();
		try
		{
			
			String groupId = request.getParamValue("groupId");
			String groupLevel = request.getParamValue("groupLevel");
			String areaId = CommonUtils.checkNull(request.getParamValue("AREAID"));	//业务范围
			
			List mmmList = dao.querySubMaterialGroupListByConf(poseId.toString(), groupId, groupLevel, areaId);
			act.setOutData("subNodeList", mmmList);
		}
		catch (Exception e)
		{//异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "订做车需求提报物料组树查询结果");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 订单物料树查询结果
	 */
	public void materialListQueryByOrder()
	{
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		Long poseId = logonUser.getPoseId();
		try
		{
			String inputId = CommonUtils.checkNull(request.getParamValue("INPUTID"));	//回显输入框ID
			String inputName = CommonUtils.checkNull(request.getParamValue("INPUTNAME"));	//回显输入框ID
			String groupId = CommonUtils.checkNull(request.getParamValue("groupId"));	//物料组ID
			String materialCode = CommonUtils.checkNull(request.getParamValue("materialCode"));	//物料组代码
			String materialName = CommonUtils.checkNull(request.getParamValue("materialName"));	//物料组名称
			String isMulti = CommonUtils.checkNull(request.getParamValue("ISMULTI"));	//单选，多选
			String isAllArea = CommonUtils.checkNull(request.getParamValue("ISALLAREA"));	//仓库
			String inputColorName = CommonUtils.checkNull(request.getParamValue("inputColorName"));	//是否过滤业务范围
			String ids=CommonUtils.checkNull(request.getParamValue("ids"));	//选中的物料
			Long companyId = GetOemcompanyId.getOemCompanyId(logonUser);
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request
							.getParamValue("curPage")) : 1;
			PageResult<Map<String, Object>> ps = dao.getMaterialListByOrder(logonUser.getPoseId().toString(),
							groupId, materialCode, materialName, curPage, ActionUtil.getPageSize(request), companyId, isAllArea,ids);
			act.setOutData("ps", ps);
			act.setOutData("INPUTID", inputId);
			act.setOutData("INPUTNAME", inputName);
			act.setOutData("ISMULTI", isMulti);
			act.setOutData("inputColorName", inputColorName);
			act.setForword(initUrl);
			// 复选框翻页选中 by chenyub@yonyou.com
			ActionUtil.setCheckedValueToOutData(act);
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
		{//异常方法
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "物料树查询结果");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
}
