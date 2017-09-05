package com.infodms.dms.actions.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.bean.CompanyBean;
import com.infodms.dms.bean.DlrBean;
import com.infodms.dms.bean.OrgBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.dao.common.CommonDAO;
import com.infodms.dms.dao.sysposition.SysPositionDAO;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TmOrgPO;
import com.infodms.dms.po.TmPoseBusinessAreaPO;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.convert.JsonConverter;
import com.infoservice.po3.bean.PageResult;

/**
 * 经销商选择公用模块
 * @author ZhaoLi
 *
 */
public class OrgMng {
	public Logger logger = Logger.getLogger(OrgMng.class);
	private SysPositionDAO sysPositionDAO = SysPositionDAO.getInstance();
	
	public void queryOrgs() throws Exception{
		ActionContext act = ActionContext.getContext();
		List<OrgBean> list = CommonDAO.selOrgs();
		HashMap<String,Object> pa = new HashMap<String,Object>();
		pa.put("orgs", list);
		JsonConverter jc = new JsonConverter();
		act.setOutData("orgs", new String(jc.sourceToDest(pa),"utf-8"));
		act.setForword("/dialog/TestOrg.jsp");
	}
	
	
	
	public void queryDealer() throws Exception{
		ActionContext act = ActionContext.getContext();
		List<OrgBean> list = CommonDAO.selTreeOrgs();
		HashMap<String,Object> pa = new HashMap<String,Object>();
		pa.put("orgs", list);
		JsonConverter jc = new JsonConverter();
		act.setOutData("orgs", new String(jc.sourceToDest(pa),"utf-8"));
		act.setForword("/dialog/dealerSearch.jsp");
	}
	
	public void queryDealers() throws Exception{
		ActionContext act = ActionContext.getContext();
		List<OrgBean> list = CommonDAO.selTreeOrgs();
		HashMap<String,Object> pa = new HashMap<String,Object>();
		pa.put("orgs", list);
		RequestWrapper request = act.getRequest();
		String dealerIds = CommonUtils.checkNull(request.getParamValue("DEALER_IDS"));
		String brandIds = CommonUtils.checkNull(request.getParamValue("BRAND_IDS"));
		act.setOutData("BRAND_IDS", brandIds);
		act.setOutData("DEALER_IDS", dealerIds);
		JsonConverter jc = new JsonConverter();
		act.setOutData("orgs", new String(jc.sourceToDest(pa),"utf-8"));
		act.setForword("/dialog/dealersSearch.jsp");
	}
	
	
	public void queryCompany() throws Exception{
		ActionContext act = ActionContext.getContext();
		act.setForword("/dialog/companySearch.jsp");
	}
	
	
	public void queryCompany2() throws Exception{  //YH 2011.5.12
		ActionContext act = ActionContext.getContext();
		act.setForword("/dialog/companySearch2.jsp");
	}
	
	public void queryCompanyA() throws Exception{
		ActionContext act = ActionContext.getContext();
		String command = act.getRequest().getParamValue("command");
		act.setOutData("command", command);
		act.setForword("/dialog/companySearchA.jsp");
	}
	
	//zhumingwei add by 2011-02-25
	public void queryCompanyA111() throws Exception{
		ActionContext act = ActionContext.getContext();
		act.setForword("/dialog/companySearchA111.jsp");
	}
	
	
	public void queryCom(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		String companyCode = request.getParamValue("companyCode");
		String companyName = request.getParamValue("companyName");
		Long companyId=logonUser.getCompanyId();
		Integer curPage = request.getParamValue("curPage") !=null ? Integer.parseInt(request.getParamValue("curPage")) : 1;
		PageResult<CompanyBean> list = CommonDAO.selCom(companyCode, companyName, Constant.PAGE_SIZE, curPage,companyId);		
		act.setOutData("ps", list);
	}
	
	@SuppressWarnings("unchecked")
	public void queryComByBrand(){  //YH 2011.5.12
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		String companyCode = request.getParamValue("companyCode");
		String companyName = request.getParamValue("companyName");
		Long companyId=logonUser.getCompanyId();
		Integer curPage = request.getParamValue("curPage") !=null ? Integer.parseInt(request.getParamValue("curPage")) : 1;
		PageResult<CompanyBean> list = CommonDAO.selCom(companyCode, companyName, Constant.PAGE_SIZE, curPage,companyId);

		List final_list = new ArrayList();
		PageResult<Map<String, Object>> final_ps = new PageResult<Map<String, Object>>();
		for(int i=0;i<list.getRecords().size();i++){
		   CompanyBean cb = list.getRecords().get(i);		  
		   Map map = new HashMap();
		   map.put("COMPANY_ID",cb.getCompanyId());
		   map.put("COMPANY_CODE", cb.getCompanyCode());	
		   map.put("COMPANY_NAME",cb.getCompanyName());
		   map.put("COMPANY_SHORTNAME",cb.getCompanyShortname());
		   map.put("AREAS",this.join(this.checkBrand(cb.getCompanyId()),","));
		   final_list.add(map);
		}
		final_ps.setCurPage(list.getCurPage());
		final_ps.setPageSize(list.getPageSize());
		final_ps.setTotalPages(list.getTotalPages());
		final_ps.setTotalRecords(list.getTotalRecords());
		final_ps.setRecords(final_list);
		act.setOutData("ps", final_ps);
	}
	//根据List组装成字符串
	public  String join(List list, String delim) {
		if (list == null || list.size() < 1)
			return null;
		StringBuffer buf = new StringBuffer();
		Iterator i = list.iterator();

		while (i.hasNext())
		{
			Map map = (Map)i.next();
			buf.append(map.get("AREA_ID"));
			if (i.hasNext())
				buf.append(delim);
		}
		return buf.toString();
	}
	
	/**
	 * 检验经销商业务范围 如果一个公司已选了业务范围 在新增的时候
	 * 已选的业务范围将不显示
	 * YH 2011.5.11
	 * @param null
	 * @return void
	 * @throws Exception
	 */
	public List checkBrand(String companyId) { //YH 2011.5.12
		List<Map<String, Object>> list = null;  
		try {
			String comId = companyId; // 取位置业务范围
			list =  sysPositionDAO.findPoseArea2(comId);	
		} catch (Exception e) {
            e.printStackTrace();
      }
		return list;
	}
	
	public void queryCom111(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		String companyCode = request.getParamValue("companyCode");
		String companyName = request.getParamValue("companyName");
		Long companyId=Long.parseLong(logonUser.getOemCompanyId());
		Integer curPage = request.getParamValue("curPage") !=null ? Integer.parseInt(request.getParamValue("curPage")) : 1;
		PageResult<CompanyBean> list = CommonDAO.selCom(companyCode, companyName, Constant.PAGE_SIZE, curPage,companyId);
		act.setOutData("ps", list);
	}
	public void queryDealerTree() throws Exception{
		ActionContext act = ActionContext.getContext();
		act.setForword("/dialog/dealerTree.jsp");
	}
	
	
	public void queryDlr(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		String orgId = request.getParamValue("orgId");
		String dlrCode = request.getParamValue("dlrCode");
		String dlrName = request.getParamValue("dlrName");
		Integer curPage = request.getParamValue("curPage") !=null ? Integer.parseInt(request.getParamValue("curPage")) : 1;
		PageResult<DlrBean> list = CommonDAO.selDlr(logonUser.getCompanyId(),orgId, dlrCode, dlrName, Constant.PAGE_SIZE, curPage);
		act.setOutData("ps", list);
	}
	
	public void queryDlrs(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		String orgId = request.getParamValue("orgId");
		String dealerIds = request.getParamValue("DEALER_IDS");
		String brandIds = request.getParamValue("BRAND_IDS");
		String dlrCode = request.getParamValue("dlrCode");
		String dlrName = request.getParamValue("dlrName");
		Integer curPage = request.getParamValue("curPage") !=null ? Integer.parseInt(request.getParamValue("curPage")) : 1;
		PageResult<DlrBean> list = CommonDAO.selDlrs(logonUser.getCompanyId(),orgId,brandIds,dealerIds, dlrCode, dlrName, Constant.PAGE_SIZE, curPage);
		act.setOutData("ps", list);
	}
	
	public void queryAllDlrs(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		String orgId = request.getParamValue("orgId");
		String dealerIds = request.getParamValue("DEALER_IDS");
		String brandIds = request.getParamValue("BRAND_IDS");
		String dlrCode = request.getParamValue("dlrCode");
		String dlrName = request.getParamValue("dlrName");
		List <DlrBean> dealerList = CommonDAO.selDlrs(logonUser.getCompanyId(),orgId,brandIds,dealerIds, dlrCode, dlrName);
		act.setOutData("dealerList", dealerList);
	}
	
	public void addDealerQueryByIDealerIds() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			act.getResponse().setContentType("application/json");
			RequestWrapper request = act.getRequest();
			String dealerIds = request.getParamValue("DEALER_IDS");
			
			List<DlrBean> ps = CommonDAO.getDealerQueryByIDealerIds(dealerIds);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"系统用户");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	public void queryOrg()throws Exception{
		ActionContext act = ActionContext.getContext();
		List<OrgBean> list = CommonDAO.selOrgs();
		HashMap<String,Object> pa = new HashMap<String,Object>();
		pa.put("orgs", list);
		JsonConverter jc = new JsonConverter();
		act.setOutData("orgs", new String(jc.sourceToDest(pa),"utf-8"));
		act.setForword("/dialog/Org.jsp");
	}
	
	/*
	 * create by     LAX
	 * create_date   2009-08-25
	 * 根据组织ID判断其是否有子组织－－ADD
	 * */
	public void queryOrgChild(){
		ActionContext act = ActionContext.getContext();
		RequestWrapper request = act.getRequest();
		try {
			String orgId = request.getParamValue("orgId");
			
			List<TmOrgPO> list = CommonDAO.getOrgChild(orgId);
			if(list.size() == 1){
				act.setOutData("ACTION_RESULT","1");
			}
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			act.setException(e);
			act.setForword("/error.jsp");
		}
		
	}
	
}
