package com.infodms.dms.actions.sales.ordermanage.orderdetail;

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
import com.infodms.dms.dao.sales.ordermanage.orderdetail.DsCompanyCoopDao;
import com.infodms.dms.dao.sales.ordermanage.orderreport.OrderReportDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TPcCompanyGroupPO;
import com.infodms.dms.po.TPcGroupPO;
import com.infodms.dms.po.TmCoopDealerPO;
import com.infodms.dms.po.TmDealerPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.dao.sales.usermng.UserManageDao;
import com.infodms.dms.po.TrRoleFuncPO;
import com.infodms.eai.po.TrRolePosePO;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

public class DsCompanyCoop {
	private Logger logger = Logger.getLogger(OrderResourceReserveFirst.class);
	private ActionContext act = ActionContext.getContext();
	RequestWrapper request = act.getRequest();
	private final DsCompanyCoopDao dao = DsCompanyCoopDao.getInstance();

	private final String GROUP_QUERY_URL = "/jsp/sales/ordermanage/orderdetail/dsCompanyCoopInit.jsp";// 电商经销商查询页面
	private final String GROUP_ADD_INIT = "/jsp/sales/ordermanage/orderdetail/dsCompanyCoopAdd.jsp";// 电商经销商新增
	private final String GROUP_UPDATE_INIT = "/jsp/sales/ordermanage/orderdetail/dsCompanyCoopUpdate.jsp";// 电商经销商修改
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
	 * FUNCTION		:	查询合作经销商所有的组
	 * @param 		:	
	 * @return		:
	 * @throws		:	
	 * LastUpdate	:	2010-8-30
	 */
	public void coopQueryList(){
		AclUserBean logonUser = null;
		try {
			RequestWrapper request = act.getRequest();
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);	
			String status = CommonUtils.checkNull(request.getParamValue("status"));//合作经销商的状态
			String coopName =  CommonUtils.checkNull(request.getParamValue("coopName"));//关系名称
			Map<String ,String > map=new HashMap<String,String>();
			map.put("status", status);
			map.put("coopName", coopName);
			map.put("dealerId", logonUser.getDealerId());
			String pageSize=CommonUtils.checkNull(request.getParamValue("pageSize"));	
			pageSize=pageSize==null||"".equals(pageSize)?"10":pageSize;
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage")) 
					: 1; // 处理当前页
			PageResult<Map<String, Object>> ps = dao.getCoopQueryList(map, Integer.parseInt(pageSize), curPage);
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
	public void coopAddInit() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		String orgId = logonUser.getOrgId().toString();
		
		try {
				act.setOutData("orgId", orgId);
				act.setForword(GROUP_ADD_INIT);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "经销商用户组查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 合作经销商的添加
	 */
	public void coopAdd(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
				String coopName=CommonUtils.checkNull(request.getParamValue("coopName"));
				String dealerCode=CommonUtils.checkNull(request.getParamValue("dealerCode"));
				String dealerCodes=CommonUtils.checkNull(request.getParamValue("dealerCodes"));
				String []codes=dealerCodes.split(",");
				//获取电商经销商id
				TmDealerPO tm=new TmDealerPO();
				tm.setDealerCode(dealerCode);
				tm=(TmDealerPO) dao.select(tm).get(0);
				String dsDealerId=tm.getDealerId().toString();
				for(int k=0;k<codes.length;k++){
					//获取合作经销商id
					TmCoopDealerPO tgp=new TmCoopDealerPO();
					Long dealerId=null;
					dealerId=CommonUtils.getDealerId(codes[k]);
					String coopId=SequenceManager.getSequence("");
					tgp.setCoopId(new Long(coopId));
					tgp.setDsDealerId(new Long(dsDealerId));
					tgp.setCoopName(coopName);
					tgp.setCreateDate(new Date());
					tgp.setCreateBy(logonUser.getUserId());
					tgp.setStatus(new Integer(10011001));
					tgp.setCoopDealerId(dealerId);
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
	public void coopUpdateInit() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		String orgId = logonUser.getOrgId().toString();
		try {
				String dsDealerId=CommonUtils.checkNull(request.getParamValue("dsDealerId"));
				String status="";
				TmCoopDealerPO tgp0=new TmCoopDealerPO();
				tgp0.setDsDealerId(new Long(dsDealerId));
				tgp0=(TmCoopDealerPO) dao.select(tgp0).get(0);
				TmCoopDealerPO tgp=new TmCoopDealerPO();
				tgp.setDsDealerId(new Long(dsDealerId));
				String dealer_codes="";
				List<PO> list=dao.select(tgp);
				for(int k=0;k<list.size();k++){
					TmCoopDealerPO tempPo=(TmCoopDealerPO) list.get(k);
					if(k==(list.size()-1)){
						dealer_codes+=CommonUtils.getDealerCode(tempPo.getCoopDealerId().toString());
					}else{
						dealer_codes+=CommonUtils.getDealerCode(tempPo.getCoopDealerId().toString())+",";
					}
				}
				status=tgp0.getStatus().toString();
				//获取经销商代码
				String dealerCode="";
				TmDealerPO td =new TmDealerPO();
				td.setDealerId(tgp0.getDsDealerId());
				td=(TmDealerPO) dao.select(td).get(0);
				dealerCode=td.getDealerCode();
				String dealerName = td.getDealerShortname();
				act.setOutData("dealerCode", dealerCode);
				act.setOutData("dealerCodes", dealer_codes);
				act.setOutData("dealerName", dealerName);
				act.setOutData("status", status);
				act.setOutData("po", tgp0);
				act.setOutData("orgId",orgId);
				act.setForword(GROUP_UPDATE_INIT);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "经销商用户组查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 合作经销商修改
	 */
	public void coopUpdate(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String coopName=CommonUtils.checkNull(request.getParamValue("coopName"));
			String dealerCode=CommonUtils.checkNull(request.getParamValue("dealerCode"));
			String status=CommonUtils.checkNull(request.getParamValue("status"));
			String dealerCodes=CommonUtils.checkNull(request.getParamValue("dealerCodes"));
			String cur_dsDealerId=CommonUtils.checkNull(request.getParamValue("dsDealerId"));
			TmCoopDealerPO temp0=new TmCoopDealerPO();
			temp0.setDsDealerId(new Long(cur_dsDealerId));
			dao.delete(temp0);
			String []codes=dealerCodes.split(",");
			//获取电商经销商id
			TmDealerPO tm=new TmDealerPO();
			tm.setDealerCode(dealerCode);
			tm=(TmDealerPO) dao.select(tm).get(0);
			String dsDealerId=tm.getDealerId().toString();
			for(int k=0;k<codes.length;k++){
				//获取合作经销商id
				TmCoopDealerPO tgp=new TmCoopDealerPO();
				Long dealerId=null;
				dealerId=CommonUtils.getDealerId(codes[k]);
				String coopId=SequenceManager.getSequence("");
				tgp.setCoopId(new Long(coopId));
				tgp.setDsDealerId(new Long(dsDealerId));
				tgp.setCoopName(coopName);
				tgp.setCreateDate(new Date());
				tgp.setCreateBy(logonUser.getUserId());
				tgp.setStatus(new Integer(status));
				tgp.setCoopDealerId(dealerId);
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
	public void checkCoopName() {
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
	
	//获取合作经销商信息
	public void getCoopDealerByDealerId() {
		AclUserBean logonUser = null;
		try {
			RequestWrapper request = act.getRequest();
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			
			String dsDealerId = CommonUtils.checkNull(request.getParamValue("dsDealerId"));//合作经销商的状态
			StringBuffer sql = new StringBuffer();
			sql.append("SELECT tcd.COOP_ID, td.DEALER_CODE,td.DEALER_SHORTNAME FROM tm_coop_dealer tcd ");
			sql.append("LEFT JOIN tm_dealer td ON tcd.coop_dealer_id = td.dealer_id ");
			sql.append("WHERE tcd.ds_dealer_id = ").append(dsDealerId);
			
			String pageSize=CommonUtils.checkNull(request.getParamValue("pageSize"));	
			pageSize=pageSize==null||"".equals(pageSize)?"10":pageSize;
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
			
			PageResult<Map<String, Object>> ps = dao.pageQuery(sql.toString(), null, dao.getFunName(), Integer.parseInt(pageSize), curPage);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "所有车辆资源查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 获取当前用户所有的功能
	 * @param reqId
	 * @param status
	 * @return
	 */
	public static String judgeUserHasFunc(AclUserBean logonUser) {
		String funcStr="";
		BaseDao dao=new  UserManageDao();
		//获取pose_id
		Long poseId=	logonUser.getPoseId();
		TrRolePosePO trp=new TrRolePosePO();
		trp.setPoseId(poseId);
		trp=(TrRolePosePO) dao.select(trp).get(0);
		TrRoleFuncPO trf=new TrRoleFuncPO();
		trf.setRoleId(trp.getRoleId());
		trf.setOpType(1);
		List<PO> list=dao.select(trf);
		for(int k=0;k<list.size();k++){
			trf=(TrRoleFuncPO) list.get(k);
			funcStr+=trf.getFuncId()+",";
		}
		return funcStr;
	}
}
