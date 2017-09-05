
/**********************************************************************
* <pre>
* FILE : SgmOrgMng.java
* CLASS : SgmOrgMng
* 
* AUTHOR : ChenLiang
*
* FUNCTION : SGM部门设定Action.
*
*
*======================================================================
* CHANGE HISTORY LOG
*----------------------------------------------------------------------
* MOD. NO.| DATE     |    NAME    | REASON | CHANGE REQ.
*----------------------------------------------------------------------
*         |2009-08-26| ChenLiang  | Created |
* DESCRIPTION:
* </pre>
***********************************************************************/

package com.infodms.dms.actions.customerRelationships.baseSetting;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.MsgCarrier;
import com.infodms.dms.common.ValidateCodeConstant;
import com.infodms.dms.dao.common.CommonUtilDao;
import com.infodms.dms.dao.customerRelationships.OrgCustomSearchDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TmOrgCustomPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.businessUtil.Validate;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;

public class OrgCustomSearch {

	public Logger logger = Logger.getLogger(OrgCustomSearch.class);
	private final String orgCustomSearchInitUrl = "/jsp/customerRelationships/baseSetting/orgCustomSearch.jsp";
	private final String orgCustomSearchAddOrUpdateUrl = "/jsp/customerRelationships/baseSetting/orgCustomSearchUpdate.jsp";

	ActionContext act = ActionContext.getContext();
	AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
	RequestWrapper request = act.getRequest();
	
	/**
	 * 查询部门维护初始化
	 * @param null
	 * @return void
	 * @throws Exception
	 */
	public void orgCustomSearchInit() {
		try {
			act.setForword(orgCustomSearchInitUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"部门维护");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}

	
	/**
	 * 部门维护新增修改
	 * @param null
	 * @return void
	 * @throws Exception
	 */
	public void orgCustomSearchAddOrUpdate() {
		String orgid = request.getParamValue("orgid");
		act.setOutData("orgTypeList", selectOrgType());
		//修改
		if(!"".equals(orgid) && orgid != null && !(orgid.equals("null")) ){
			OrgCustomSearchDao dao = OrgCustomSearchDao.getInstance();
			Map<String, Object> map =  dao.getOrgCustomByOrgId(Long.parseLong(orgid));
			act.setOutData("orgCustomSearch", map);
		}
			act.setForword(orgCustomSearchAddOrUpdateUrl);
	}
	
	
	/**
	 * 部门维护更新提交
	 * @param null
	 * @return void
	 * @throws Exception
	 */
	public void orgCustomSearchAddOrUpdateSubmit() {

		try {
			act.getResponse().setContentType("application/json");
			String orgid = CommonUtils.checkNull(request.getParamValue("D_ID"));
			String orgcode = CommonUtils.checkNull(request.getParamValue("ORG_CODE"));
			String orgname = CommonUtils.checkNull(request.getParamValue("DEPT_NNAME"));
			String orgparentId = CommonUtils.checkNull(request.getParamValue("DEPT_ID"));
			String state = CommonUtils.checkNull(request.getParamValue("DEPT_STATE"));
			String orgtype = CommonUtils.checkNull(request.getParamValue("ORG_TYPE"));
			
			if(orgtype.equals("部门")){
				orgtype = "1";
			}else if(orgtype.equals("处级")){
				orgtype = "2";
			}
			
			OrgCustomSearchDao orgCustomSearchDao = new OrgCustomSearchDao();
			//新增
			if("".equals(orgid)){
				TmOrgCustomPO tmOrgCustomPO = new TmOrgCustomPO();
				tmOrgCustomPO.setOrgCode(orgcode);
				CommonUtilDao commonUtilDao = CommonUtilDao.getInstance();
				tmOrgCustomPO.setOrgId(commonUtilDao.getID());
				tmOrgCustomPO.setOrgLevel((orgtype.equals("")||orgtype==null)?0:Integer.parseInt(orgtype));
				tmOrgCustomPO.setOrgName(orgname);
				tmOrgCustomPO.setParentOrgId((orgparentId.equals("")||orgparentId==null)?0:Long.parseLong(orgparentId));
				tmOrgCustomPO.setStatus(Integer.parseInt(state));
				
				tmOrgCustomPO.setCreateBy(logonUser.getUserId());
				tmOrgCustomPO.setCreateDate(new Date());
				orgCustomSearchDao.insert(tmOrgCustomPO);
			//修改
			}else{
				
				TmOrgCustomPO tmOrgCustomPO1 = new TmOrgCustomPO();
				TmOrgCustomPO tmOrgCustomPO2 = new TmOrgCustomPO();
				tmOrgCustomPO1.setOrgId(Long.parseLong(orgid));
				
				tmOrgCustomPO2.setOrgCode(orgcode);
				tmOrgCustomPO2.setOrgLevel(Integer.parseInt(orgtype));
				tmOrgCustomPO2.setOrgName(orgname);
				tmOrgCustomPO2.setParentOrgId(Long.parseLong(orgparentId));
				tmOrgCustomPO2.setStatus(Integer.parseInt(state));
				tmOrgCustomPO2.setCreateBy(logonUser.getUserId());
				tmOrgCustomPO2.setCreateDate(new Date());
								
				orgCustomSearchDao.update(tmOrgCustomPO1, tmOrgCustomPO2);
			}
			act.setOutData("success", "true");

		}  catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"部门维护更新");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}

	
	private List<Map<String,Object>> selectOrgType(){
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("typeId", 1);
		map.put("typeName", "部门");
		list.add(map);
		map = new HashMap<String, Object>();
		map.put("typeId", 2);
		map.put("typeName", "处级");
		list.add(map);
		return list;	
	}
	
	/**
	 * 构造部门维护树形菜单
	 * @param null
	 * @return void
	 * @throws Exception
	 */
	public void initOrgTree() {
		try {
			act.getResponse().setContentType("application/json");
			String rootId = request.getParamValue("tree_root_id"); // 得到树的根节点ID
			String levelType = request.getParamValue("levelType"); // 得到树的根节点ID
			
			List<MsgCarrier> vlist = new ArrayList<MsgCarrier>();
			vlist.add(MsgCarrier.getInstance(ValidateCodeConstant.DIGIT_PATTERN,"部门ID",0,Constant.Length_Check_Char_10,rootId));
			Validate.doValidate(act, vlist);
			OrgCustomSearchDao orgCustomSearchDao = new OrgCustomSearchDao();
			String level = "";
			if(levelType.equals("all")){
				level = "1,2,3";
			}else if(levelType.equals("updateType")){
				level = "1,2";
			}			
			List<Map<String,Object>> funList = orgCustomSearchDao.getOrgCustomSelete(level);
			act.setOutData("funlist", funList);
		} catch(BizException e){
			logger.error(logonUser,e);
			act.setException(e);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"部门维护");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}

		
	/**
	 * 查询部门维护
	 * @param null
	 * @return void
	 * @throws Exception
	 */
	public void queryOrgCustomSearch() {

		try {
			act.getResponse().setContentType("application/json");
			if("1".equals(request.getParamValue("COMMAND"))){ // 表格请求处理
				// 上级部门
				String porgId = request.getParamValue("DEPT_ID");
				if(porgId == null) porgId = "2010010100070674";
				
				String orderName = request.getParamValue("orderCol");
				String da = request.getParamValue("order");
				String orgCode = request.getParamValue("orgCode");
				Integer curPage = request.getParamValue("curPage") != null ? Integer
						.parseInt(request.getParamValue("curPage"))
						: 1; // 处理当前页
				OrgCustomSearchDao dao = new OrgCustomSearchDao();
				PageResult<Map<String,Object>> ps = dao.queryOrgCustom(porgId,orgCode,orderName,da, Constant.PAGE_SIZE, curPage);
				
				act.setOutData("ps", ps);
			}
		}  catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"查询部门维护");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 查询部门维护
	 * @param null
	 * @return void
	 * @throws Exception
	 */
	public void queryOrgCustomUser() {

		try {
			act.getResponse().setContentType("application/json");
			AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
			// 上级部门
			String porgId = request.getParamValue("DEPT_ID");
			if(porgId == null) porgId = "2010010100070674";
			
			Integer curPage = request.getParamValue("curPage2") != null ? Integer
					.parseInt(request.getParamValue("curPage2"))
					: 1; // 处理当前页
			OrgCustomSearchDao dao = new OrgCustomSearchDao();
			PageResult<Map<String,Object>> ps = dao.queryOrgCustomUser(act.getRequest(), logonUser, Constant.PAGE_SIZE_MAX, curPage);
			
			act.setOutData("ps", ps);
			
		}  catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"查询部门维护");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	
}
