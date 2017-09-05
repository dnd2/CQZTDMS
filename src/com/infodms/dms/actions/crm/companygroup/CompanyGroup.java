package com.infodms.dms.actions.crm.companygroup;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.actions.sales.ordermanage.orderaudit.OrderResourceReserveFirst;
import com.infodms.dms.actions.util.LockControl;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.dao.crm.basedata.PcGroupDao;
import com.infodms.dms.dao.crm.comapanygroup.CompanyGroupDao;
import com.infodms.dms.dao.sales.financemanage.AccountBalanceDetailDao;
import com.infodms.dms.dao.sales.ordermanage.audit.OrderAuditDao;
import com.infodms.dms.dao.sales.ordermanage.orderreport.OrderReportDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TPcCompanyGroupPO;
import com.infodms.dms.po.TPcGroupPO;
import com.infodms.dms.po.TmDealerPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

public class CompanyGroup {
	private Logger logger = Logger.getLogger(OrderResourceReserveFirst.class);
	private ActionContext act = ActionContext.getContext();
	RequestWrapper request = act.getRequest();
	private final CompanyGroupDao dao = CompanyGroupDao.getInstance();

	private final String GROUP_QUERY_URL = "/jsp/crm/companygroup/companyGroupInit.jsp";// 经销商集团组维护查询页面
	private final String GROUP_ADD_INIT = "/jsp/crm/companygroup/companyGroupAdd.jsp";// 经销商集团组维护查询页面
	private final String GROUP_UPDATE_INIT = "/jsp/crm/companygroup/companyGroupUpdate.jsp";// 经销商集团组维护查询页面
	public void doInit() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String funcStr=CommonUtils.judgeUserHasFunc(logonUser);
				act.setOutData("funcStr", funcStr);
				act.setForword(GROUP_QUERY_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "经销商用户组查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * FUNCTION		:	查询经销商所有的组
	 * @param 		:	
	 * @return		:
	 * @throws		:	
	 * LastUpdate	:	2010-8-30
	 */
	public void groupQueryList(){
		AclUserBean logonUser = null;
		try {
			RequestWrapper request = act.getRequest();
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);	
			String status = CommonUtils.checkNull(request.getParamValue("status"));		//经销商用户组的状态
			String groupName =  CommonUtils.checkNull(request.getParamValue("groupName"));			//组名称
			Map<String ,String > map=new HashMap<String,String>();
			map.put("status", status);
			map.put("groupName", groupName);
			map.put("dealerId", logonUser.getDealerId());
			String pageSize=CommonUtils.checkNull(request.getParamValue("pageSize"));	
			pageSize=pageSize==null||"".equals(pageSize)?"10":pageSize;
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage")) 
					: 1; // 处理当前页
			PageResult<Map<String, Object>> ps = dao.getGroupQueryList(map, Integer.parseInt(pageSize), curPage);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "所有车辆资源查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	} 
	/**
	 * 用户组新增界面
	 */
	public void groupAddInit() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
				act.setForword(GROUP_ADD_INIT);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "经销商用户组查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 经销商用户组的添加
	 */
	public void groupAdd(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
				String groupName=CommonUtils.checkNull(request.getParamValue("groupName"));
				String dealerCode=CommonUtils.checkNull(request.getParamValue("dealerCode"));
				String dealerCodes=CommonUtils.checkNull(request.getParamValue("dealerCodes"));
				String []codes=dealerCodes.split(",");
				//获取上级经销商id
				TmDealerPO tm=new TmDealerPO();
				tm.setDealerCode(dealerCode);
				tm=(TmDealerPO) dao.select(tm).get(0);
				String groupId=SequenceManager.getSequence("");
				for(int k=0;k<codes.length;k++){
					TPcCompanyGroupPO tgp=new TPcCompanyGroupPO();
					Long dealerId=null;
					dealerId=CommonUtils.getDealerId(codes[k]);
					String comapnyGroupId=SequenceManager.getSequence("");
					tgp.setCompanyGroupId(new Long(comapnyGroupId));
					tgp.setGroupId(new Long(groupId));
					tgp.setGroupName(groupName);
					tgp.setCreateDate(new Date());
					tgp.setCreateBy(logonUser.getUserId());
					tgp.setStatus(new Long(10011001));
					tgp.setParDealerId(tm.getDealerId());
					tgp.setDealerIds(dealerId);
					tgp.setDealerCodes(codes[k]);
					dao.insert(tgp);
				}
				act.setOutData("flag", 1);
				
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "经销商用户组查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 用户组修改初始化界面
	 */
	public void groupUpdateInit() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
				String groupId=CommonUtils.checkNull(request.getParamValue("groupId"));
				String status="";
				TPcCompanyGroupPO tgp0=new TPcCompanyGroupPO();
				tgp0.setGroupId(new Long(groupId));
				tgp0=(TPcCompanyGroupPO) dao.select(tgp0).get(0);
				TPcCompanyGroupPO tgp=new TPcCompanyGroupPO();
				tgp.setGroupId(new Long(groupId));
				String dealer_codes="";
				List<PO> list=dao.select(tgp);
				for(int k=0;k<list.size();k++){
					TPcCompanyGroupPO tempPo=(TPcCompanyGroupPO) list.get(k);
					if(k==(list.size()-1)){
						dealer_codes+=tempPo.getDealerCodes();
					}else{
						dealer_codes+=tempPo.getDealerCodes()+",";
					}
				}
				status=CommonUtils.getCodeDesc(tgp0.getStatus().toString());
				//获取经销商代码
				String dealerCode="";
				TmDealerPO td =new TmDealerPO();
				td.setDealerId(tgp0.getParDealerId());
				td=(TmDealerPO) dao.select(td).get(0);
				dealerCode=td.getDealerCode();
				act.setOutData("dealerCode", dealerCode);
				act.setOutData("dealerCodes", dealer_codes);
				act.setOutData("status", status);
				act.setOutData("po", tgp0);
				act.setForword(GROUP_UPDATE_INIT);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "经销商用户组查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 经销商用户组的添加
	 */
	public void groupUpdate(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
				String groupName=CommonUtils.checkNull(request.getParamValue("groupName"));
				String dealerCode=CommonUtils.checkNull(request.getParamValue("dealerCode"));
				String dealerCodes=CommonUtils.checkNull(request.getParamValue("dealerCodes"));
				String status=CommonUtils.checkNull(request.getParamValue("status"));
				String groupId=CommonUtils.checkNull(request.getParamValue("groupId"));
				String [] codeArray=dealerCodes.split(",");
				//删除原来的数据
				TPcCompanyGroupPO tgp0=new TPcCompanyGroupPO();
				tgp0.setGroupId(new Long(groupId));
				dao.delete(tgp0);
				//获取上级经销商id
				TmDealerPO tm=new TmDealerPO();
				tm.setDealerCode(dealerCode);
				tm=(TmDealerPO) dao.select(tm).get(0);
				for(int k=0;k<codeArray.length;k++){
					Long dealerId=null;
					dealerId=CommonUtils.getDealerId(codeArray[k]);
					Long companyGroupId=new Long(SequenceManager.getSequence(""));
					TPcCompanyGroupPO tgp=new TPcCompanyGroupPO();
					tgp.setCompanyGroupId(companyGroupId);
					tgp.setGroupName(groupName);
					tgp.setGroupId(new Long(groupId));
					tgp.setDealerCodes(codeArray[k]);
					tgp.setParDealerId(tm.getDealerId());
					tgp.setCreateDate(new Date());
					tgp.setCreateBy(logonUser.getUserId());
					tgp.setUpdateDate(new Date());
					tgp.setUpdateBy(logonUser.getUserId());
					tgp.setStatus(new Long(status));
					tgp.setDealerIds(dealerId);
					dao.insert(tgp);
				}
				act.setOutData("flag", 1);
				
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "经销商用户组查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	//验证组名称
	public void checkGroupName() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
				String groupName=request.getParamValue("groupName");
				String groupId=request.getParamValue("groupId");
				Map<String,String> map=new HashMap<String, String>();
				map.put("groupName", groupName);
				map.put("dealerId", logonUser.getDealerId());
				map.put("groupId", groupId);
				int count=dao.checkGroupName(map);
//				if(count<1){
					act.setOutData("flag", "1");
//				}else{
//					act.setOutData("flag", "2");
//				}
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "经销商用户组查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
}
