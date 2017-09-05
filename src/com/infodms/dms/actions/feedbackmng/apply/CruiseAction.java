package com.infodms.dms.actions.feedbackmng.apply;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.bean.TAWCruiseAuditBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.dao.feedbackMng.CruiseDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TmChananelMileagePO;
import com.infodms.dms.po.TtAsWrCruiseAuditingPO;
import com.infodms.dms.po.TtAsWrCruisePO;
import com.infodms.dms.util.StringUtil;
import com.infodms.dms.util.UserProvinceRelation;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;

/**
 * 巡航服务线路申报,审批
 * @author nova
 *
 */
public class CruiseAction {
	private ActionContext act ;
	private RequestWrapper req ;
	AclUserBean logonUser ;
	private Logger logger = Logger.getLogger(CruiseAction.class);
	private CruiseDao dao = CruiseDao.getInstance() ;
	
	private final String CRUISE_APPLY = "/jsp/feedbackMng/apply/cruiseApplyMain.jsp" ;//航线申请页面
	private final String ADD_CRUISE = "/jsp/feedbackMng/apply/addCruise.jsp" ;//新增航行路线
	private final String CRUISE_DETAIL = "/jsp/feedbackMng/apply/cruiseDetail.jsp" ;//航行路线明细
	private final String CRUISE_AUDIT = "/jsp/feedbackMng/apply/cruiseAudit.jsp" ;//事业部审核主页面
	private final String CRUISE_AUDIT_1 = "/jsp/feedbackMng/apply/cruiseAudit01.jsp" ;//事业部审核页面
	private final String CRUISE_AUDIT_2 = "/jsp/feedbackMng/apply/cruiseAudit02.jsp" ;//总部审核页面
	private final String CRUISE_DETAIL_2 = "/jsp/feedbackMng/apply/cruiseDetail02.jsp" ;//OEM查询的明细页面
	
	/*
	 * DLR巡航服务线路申报主页面跳转方法
	 */
	public void cruiseApplyInit(){
		act = ActionContext.getContext();
		logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			act.setForword(CRUISE_APPLY);
		}catch(Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"巡航服务线路提报");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/*
	 * DLR巡航服务线路申报  查询方法
	 */
	public void queryCruise(){
		act = ActionContext.getContext();
		req = act.getRequest() ;
		logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			String crNo = req.getParamValue("cr_no");
			String whither = req.getParamValue("whither");
			String status = req.getParamValue("status");
			StringBuffer con = new StringBuffer();
			con.append("  and c.dealer_id = ").append(logonUser.getDealerId()).append("\n");
			if(StringUtil.notNull(crNo))
				con.append("  and c.cr_no like '%").append(crNo).append("%'\n");
			if(StringUtil.notNull(whither))
				con.append("  and c.whither like '%").append(whither).append("%'\n");
			if(StringUtil.notNull(status))
				con.append("  and c.status=").append(status).append("\n");
			Integer curPage = req.getParamValue("curPage") != null ? Integer
					.parseInt(req.getParamValue("curPage")): 1; 
			int pageSize = 10 ;
			PageResult<Map<String,Object>> ps = dao.queryCruiseApply(con.toString(), pageSize, curPage) ;
			act.setOutData("ps", ps);
		}catch(Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"巡航服务线路提报");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/*
	 * 新增巡航服务线路跳转方法
	 */
	public void addCruiseInit(){
		act = ActionContext.getContext();
		logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			Map<String, Object> map = dao.getDealerInfo(logonUser.getDealerId());
			act.setOutData("map", map);
			act.setOutData("date", new Date());
			act.setForword(ADD_CRUISE);
		}catch(Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"巡航服务线路提报");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/*
	 * 新增或提报巡航服务线路
	 */
	public void addOrApplyCruise(){
		act = ActionContext.getContext();
		req = act.getRequest();
		logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			String address = req.getParamValue("address");
			String days = req.getParamValue("days");
			String mileage = req.getParamValue("mileage");
			String man = req.getParamValue("man");
			String phone = req.getParamValue("phone");
			String cause = req.getParamValue("cause");
			String type = req.getParamValue("type");
			String point = req.getParamValue("point");
			Integer status = Constant.CRUISE_STATUS_01 ;
			if("2".equals(type))
				status = Constant.CRUISE_STATUS_02;
			
			TtAsWrCruisePO po = new TtAsWrCruisePO();
			po.setId(Long.parseLong(SequenceManager.getSequence("")));
			po.setCrNo(SequenceManager.getSequence("XHFW"));
			po.setDealerId(Long.parseLong(logonUser.getDealerId()));
			po.setCompanyId(logonUser.getCompanyId());
			po.setCrCause(cause);
			po.setCrDay(Double.parseDouble(days));
			po.setCreateBy(logonUser.getUserId());
			po.setCreateDate(new Date());
			po.setCrMileage(Double.parseDouble(mileage));
			po.setCrPhone(phone);
			po.setCrPrincipal(man);
			po.setCrWhither(address);
			po.setMakeDate(new Date());
			po.setStatus(status) ;
			po.setFixPointDate(point);
			
			dao.insert(po);
			act.setOutData("flag", type);
		}catch(Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"巡航服务线路提报");
			logger.error(logonUser,e1);
			act.setOutData("flag",false);
			act.setException(e1);
		}
	}
	/*
	 * 巡航服务线路明细
	 */
	public void showCruise(){
		act = ActionContext.getContext();
		req = act.getRequest();
		logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			String id = req.getParamValue("id");
			TtAsWrCruisePO po = new TtAsWrCruisePO();
			po.setId(Long.parseLong(id));
			List list = dao.select(po);
			if(list.size()>0){
				po = (TtAsWrCruisePO)list.get(0);
				Map<String, Object> map = dao.getDealerInfo(po.getDealerId().toString());
				act.setOutData("map", map);
			}
			act.setOutData("cruise", po);
			act.setForword(CRUISE_DETAIL);
		}catch(Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"巡航服务线路提报");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/*
	 * 修改巡航服务线路
	 */
	public void updateOrApplyCruise(){
		act = ActionContext.getContext();
		req = act.getRequest();
		logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			String id = req.getParamValue("id");
			String address = req.getParamValue("address");
			String days = req.getParamValue("days");
			String mileage = req.getParamValue("mileage");
			String man = req.getParamValue("man");
			String phone = req.getParamValue("phone");
			String cause = req.getParamValue("cause");
			String type = req.getParamValue("type");
			String point = req.getParamValue("point");
			Integer status = Constant.CRUISE_STATUS_01 ;
			if("2".equals(type))
				status = Constant.CRUISE_STATUS_02;
			
			TtAsWrCruisePO po = new TtAsWrCruisePO();
			TtAsWrCruisePO po2 = new TtAsWrCruisePO();
			po2.setId(Long.parseLong(id));
			po.setId(Long.parseLong(id));
			po.setCompanyId(logonUser.getCompanyId());
			po.setCrCause(cause);
			po.setCrDay(Double.parseDouble(days));
			po.setUpdateBy(logonUser.getUserId());
			po.setUpdateDate(new Date());
			po.setCrMileage(Double.parseDouble(mileage));
			po.setCrPhone(phone);
			po.setCrPrincipal(man);
			po.setCrWhither(address);
			po.setMakeDate(new Date());
			po.setStatus(status) ;
			po.setFixPointDate(point);
			
			dao.update(po2, po);
			act.setOutData("flag", type);
		}catch(Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"巡航服务线路提报");
			logger.error(logonUser,e1);
			act.setOutData("flag",false);
			act.setException(e1);
		}
	}
	/************************OEM*******************************/
	/*
	 * 事业部审核页面初始化
	 */
	public void cruiseAuditInit(){
		act = ActionContext.getContext();
		logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			act.setOutData("flag", 1);
			act.setForword(CRUISE_AUDIT);
		}catch(Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"巡航服务线路提报");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/*
	 * 总部审核页面初始化
	 */
	public void cruiseAuditInit2(){
		act = ActionContext.getContext();
		logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			act.setForword(CRUISE_AUDIT);
		}catch(Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"巡航服务线路提报");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/*
	 * 审核页面主查询
	 */
	public void queryCruise4Audit(){
		act = ActionContext.getContext();
		req = act.getRequest() ;
		logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			String code = req.getParamValue("cr_no");
			String address = req.getParamValue("whither");
			String status = req.getParamValue("status");
			String flag = req.getParamValue("flag");//事业部与总部的区分
			
			StringBuffer con = new StringBuffer();
			if(StringUtil.notNull(code))
				con.append("  and c.cr_no like '%"+code+"%'\n");
			if(StringUtil.notNull(address))
				con.append("  and c.cr_whither like '%"+address+"%'\n");
			if(StringUtil.notNull(status))
				con.append("  and c.status="+status+"\n");
			con.append("  and c.dealer_id = s.dealer_id\n");
			con.append("  and s.root_org_id =").append(logonUser.getOrgId()).append("\n");
			Integer curPage = req.getParamValue("curPage") != null ? Integer
					.parseInt(req.getParamValue("curPage")): 1; 
			int pageSize = 10 ;
			PageResult<Map<String,Object>> ps = dao.queryCruiseApply(con.toString(), pageSize, curPage) ;
			act.setOutData("ps", ps);
		}catch(Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"巡航服务线路提报");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/*
	 * 审核页面主查询
	 */
	public void queryCruise4Audit1(){
		act = ActionContext.getContext();
		req = act.getRequest() ;
		logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			String code = req.getParamValue("cr_no");
			String address = req.getParamValue("whither");
			String status = req.getParamValue("status");
			String flag = req.getParamValue("flag");//事业部与总部的区分 1 ：事业部
			
			StringBuffer con = new StringBuffer();
			if(StringUtil.notNull(code))
				con.append("  and c.cr_no like '%"+code+"%'\n");
			if(StringUtil.notNull(address))
				con.append("  and c.cr_whither like '%"+address+"%'\n");
			if(StringUtil.notNull(status))
				con.append("  and c.status="+status+"\n");
			if("1".equals(flag)){
				con.append(UserProvinceRelation.getDealerIds(logonUser.getUserId(),"c"));
			}
			Integer curPage = req.getParamValue("curPage") != null ? Integer
					.parseInt(req.getParamValue("curPage")): 1; 
			int pageSize = 10 ;
			PageResult<Map<String,Object>> ps = dao.queryCruiseApply1(con.toString(), pageSize, curPage) ;
			act.setOutData("ps", ps);
		}catch(Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"巡航服务线路提报");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/*
	 * 审核页面初始化
	 */
	public void showCruise4Audit(){
		act = ActionContext.getContext();
		req = act.getRequest();
		logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			String id = req.getParamValue("id");
			String type=req.getParamValue("type");
			TtAsWrCruisePO po = new TtAsWrCruisePO();
			po.setId(Long.parseLong(id));
			List list = dao.select(po);
			if(list.size()>0){
				po = (TtAsWrCruisePO)list.get(0);
				Map<String, Object> map = dao.getDealerInfo(po.getDealerId().toString());
				act.setOutData("map", map);
			}
			act.setOutData("cruise", po);
			
			//审核明细查询
			List<TAWCruiseAuditBean> list2 = dao.getAuditInfo(po.getId());
			act.setOutData("list2", list2);
						
			if("1".equals(type))
				act.setForword(CRUISE_AUDIT_1);
			else if("3".equals(type))
				act.setForword(CRUISE_AUDIT_2);
			else
				act.setForword(CRUISE_DETAIL_2);
		}catch(Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"巡航服务线路提报");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/*
	 * 审核方法
	 */
	public void auditCruise(){
		act = ActionContext.getContext();
		req = act.getRequest();
		logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			String id = req.getParamValue("id");
			String option = req.getParamValue("option");
			String type=req.getParamValue("type");
			Integer status = Constant.CRUISE_STATUS_04 ;
			if("2".equals(type))
				status = Constant.CRUISE_STATUS_03 ;
			else if("3".equals(type))
				status = Constant.CRUISE_STATUS_06 ;
			else if("4".equals(type))
				status = Constant.CRUISE_STATUS_07 ;
			else if("5".equals(type))
				status = Constant.CRUISE_STATUS_05 ;
					
			TtAsWrCruisePO po = new TtAsWrCruisePO();
			TtAsWrCruisePO po2 = new TtAsWrCruisePO();
			po2.setId(Long.parseLong(id));
			po.setId(Long.parseLong(id));
			po.setAuditDate(new Date());
			po.setAuditingOpinion(option);
			po.setUpdateBy(logonUser.getUserId());
			po.setUpdateDate(new Date());
			po.setStatus(status);
			if("5".equals(type)){
				po.setIsSuspend(1);
				po.setSuspendDate(new Date());
			}
			
			//将信息写入审核明细表
			TtAsWrCruiseAuditingPO audit = new TtAsWrCruiseAuditingPO();
			audit.setAuditingDate(new Date());
			audit.setAuditingOpinion(option);
			audit.setAuditingPerson(logonUser.getUserId());
			audit.setCreateBy(logonUser.getUserId());
			audit.setCreateDate(new Date());
			audit.setCrId(po.getId());
			audit.setId(Long.parseLong(SequenceManager.getSequence("")));
			audit.setStatus(status);
			dao.insert(audit);
			dao.update(po2,po);
			act.setOutData("flag", type);
		}catch(Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"巡航服务线路提报");
			logger.error(logonUser,e1);
			act.setOutData("flag", false);
			act.setException(e1);
		}
	}
	
	/*
	 * 外出天数的判断
	 */
	public void checkOutDays(){
		act = ActionContext.getContext();
		req = act.getRequest() ;
		logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			String mileage = req.getParamValue("mileage");
			TmChananelMileagePO po = new TmChananelMileagePO();
			po.setMileage(Long.parseLong(mileage));
			po.setChananelType(Integer.parseInt(Constant.FEE_CHANNEL_03));
			List list = dao.select(po);
			if(list.size()<=0)
				act.setOutData("flag",true);
			else
				act.setOutData("outDay", ((TmChananelMileagePO)list.get(0)).getOutDays());
			act.setOutData("type", req.getParamValue("type"));
		}catch(Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"巡航服务线路提报");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
}
