package com.infodms.dms.actions.crm.defeat;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;





import org.apache.log4j.Logger;

import com.infodms.dms.actions.crm.follow.FollowManage;
import com.infodms.dms.actions.sales.ordermanage.orderaudit.OrderResourceReserveFirst;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.dao.crm.defeat.DefeatDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TPcCustomerPO;
import com.infodms.dms.po.TPcDefeatVehiclePO;
import com.infodms.dms.po.TPcDefeatfailureAuditPO;
import com.infodms.dms.po.TPcDefeatfailurePO;
import com.infodms.dms.po.TPcFollowPO;
import com.infodms.dms.po.TPcIntentVehiclePO;
import com.infodms.dms.po.TPcInvitePO;
import com.infodms.dms.po.TPcLeadsAllotPO;
import com.infodms.dms.po.TPcLeadsPO;
import com.infodms.dms.po.TcCodePO;
import com.infodms.dms.po.TcUserPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;

public class DefeatManage {
	private Logger logger = Logger.getLogger(OrderResourceReserveFirst.class);
	private ActionContext act = ActionContext.getContext();
	RequestWrapper request = act.getRequest();
	private final DefeatDao dao = DefeatDao.getInstance();

	private final String DEFEAT_QUERY_URL = "/jsp/crm/defeat/defeatInit.jsp";// 战败审核查询界面
	private final String DEFEAT_CHECK = "/jsp/crm/defeat/defeatCheckDetail.jsp";// 战败审核界面
	private final String DEFEAT_CHECK_REDIRCT = "/jsp/crm/defeat/defeatCheckDetailRedirect.jsp";// 战败审核界面
	private final String DCRC_QUERY_URL = "/jsp/crm/defeat/dcrcInit.jsp";// dcrc抽查查询界面
	private final String DCRC_DETAIL = "/jsp/crm/defeat/dcrcDetail.jsp";// dcrc抽查审核界面
	private final String TO_ADVISER_DETAIL = "/jsp/crm/defeat/adviserList.jsp";// dcrc抽查审核界面
	private final String DEFEAT_DETAIL = "/jsp/crm/defeat/defeatDetail.jsp";// 战败界面
	
	/**
	 * 进入战败审核查询
	 */
	public void doInit() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
				//获取当前经销商对应的有效顾问
				Map<String,String> map=new HashMap<String, String>();
				map.put("userId", logonUser.getUserId().toString());
				map.put("dealerId", logonUser.getDealerId());
				List<Map<String,Object>> userList=CommonUtils.queryUser(map);
				FollowManage.getManager(logonUser, act);
				act.setOutData("userList", userList);
				act.setForword(DEFEAT_QUERY_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "进入战败审核查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * DCRC抽查
	 */
	public void doCheckInit() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			Map<String,String> map=new HashMap<String, String>();
			//map.put("userId", logonUser.getUserId().toString());
			map.put("dealerId", logonUser.getDealerId());
			List<Map<String,Object>> userList=CommonUtils.queryUser(map);
			FollowManage.getManager(logonUser, act);
			act.setOutData("userList", userList);
			act.setForword(DCRC_QUERY_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "经销商用户组查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * FUNCTION		:	查询战败审核的数据列表
	 * LastUpdate	:	2017
	 */
	public void defeatQueryList(){
		AclUserBean logonUser = null;
		try {
			RequestWrapper request = act.getRequest();
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);	
			String telephone = CommonUtils.checkNull(request.getParamValue("telephone"));		//客户电话
			String ctmName =  CommonUtils.checkNull(request.getParamValue("ctmName"));			//客户名称
			String defeatDate =  CommonUtils.checkNull(request.getParamValue("defeatDate"));	//战败时间
			String defeatDateEnd =  CommonUtils.checkNull(request.getParamValue("defeatDateEnd"));	//战败时间
			String ctmRank=CommonUtils.checkNull(request.getParamValue("ctmRank"));             //意向等级
			String salesProgress=CommonUtils.checkNull(request.getParamValue("salesProgress"));             //流程进度
			String adviser=CommonUtils.checkNull(request.getParamValue("adviser")); //销售顾问
			String opType=CommonUtils.checkNull(request.getParamValue("opType"));//战败或者实效
			String groupId=CommonUtils.checkNull(request.getParamValue("groupId"));//组
			String dealerId=logonUser.getDealerId();
			Map<String ,String > map=new HashMap<String,String>();
			map.put("telephone", telephone);
			map.put("ctmName", ctmName);
			map.put("defeatDate", defeatDate);
			map.put("defeatDateEnd", defeatDateEnd);
			map.put("ctmRank", ctmRank);
			map.put("salesProgress", salesProgress);
			map.put("adviser", adviser);
			map.put("dealerId", dealerId);
			map.put("opType", opType);
			map.put("logonId", logonUser.getUserId().toString());
			map.put("groupId", groupId);
			map.put("poseRank", CommonUtils.getPoseRank(logonUser));
			String pageSize=CommonUtils.checkNull(request.getParamValue("pageSize"));	
			pageSize=pageSize==null||"".equals(pageSize)?"10":pageSize;
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
			PageResult<Map<String, Object>> ps = dao.getDefeatQueryList(map, Integer.parseInt(pageSize), curPage);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "战败审核查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	} 
	/**
	 * FUNCTION		:	查询战败审核的数据
	 * @param 		:	
	 * @return		:
	 * @throws		:	
	 * LastUpdate	:	2010-8-30
	 */
	public void dcrcQueryList(){
		AclUserBean logonUser = null;
		try {
			RequestWrapper request = act.getRequest();
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);	
			String telephone = CommonUtils.checkNull(request.getParamValue("telephone"));		//客户电话
			String ctmName =  CommonUtils.checkNull(request.getParamValue("ctmName"));			//客户名称
			String defeatDate =  CommonUtils.checkNull(request.getParamValue("defeatDate"));	//战败时间
			String defeatDateEnd =  CommonUtils.checkNull(request.getParamValue("defeatDateEnd"));	//战败时间
			String ctmRank=CommonUtils.checkNull(request.getParamValue("ctmRank"));             //意向等级
			String salesProgress=CommonUtils.checkNull(request.getParamValue("salesProgress"));             //流程进度
			String adviser=CommonUtils.checkNull(request.getParamValue("adviser")); //销售顾问
			String groupId=CommonUtils.checkNull(request.getParamValue("groupId")); //组
			String opType=CommonUtils.checkNull(request.getParamValue("opType"));//战败或者实效
			
			Map<String ,String > map=new HashMap<String,String>();
			map.put("telephone", telephone);
			map.put("ctmName", ctmName);
			map.put("defeatDate", defeatDate);
			map.put("defeatDateEnd", defeatDateEnd);
			map.put("ctmRank", ctmRank);
			map.put("salesProgress", salesProgress);
			map.put("adviser", adviser);
			map.put("logonId", logonUser.getUserId().toString());
			map.put("dealerId", logonUser.getDealerId());
			map.put("opType", opType);
			map.put("groupId", groupId);
			String pageSize=CommonUtils.checkNull(request.getParamValue("pageSize"));	
			pageSize=pageSize==null||"".equals(pageSize)?"10":pageSize;
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage")) 
					: 1; // 处理当前页
			PageResult<Map<String, Object>> ps = dao.getDcrcQueryList(map, Integer.parseInt(pageSize), curPage);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "战败审核查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	} 
	
	/**
	 * 战败明细审核界面
	 */
	public void detailInit() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
				String defeatId=CommonUtils.checkNull(request.getParamValue("defeatId"));
				//获取战败数据
				TPcDefeatfailurePO tpd=new TPcDefeatfailurePO();
				tpd.setDefeatfailureId(new Long(defeatId));
				tpd=(TPcDefeatfailurePO) dao.select(tpd).get(0);
				//获取客户数据
				TPcCustomerPO tpc=new TPcCustomerPO();
				tpc.setCustomerId(tpd.getCustomerId());
				tpc=(TPcCustomerPO) dao.select(tpc).get(0);
				//获取相关时间
				Map<String,String> map=new HashMap<String,String>();
				SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
				String startDate="";
				String endDate="";
				if(tpd.getDefeatStartDate()!=null&&!"".equals(tpd.getDefeatStartDate())){
					startDate=sdf.format(tpd.getDefeatStartDate());
				}
				if(tpd.getDefeatEndDate()!=null&&!"".equals(tpd.getDefeatEndDate())){
					endDate=sdf.format(tpd.getDefeatEndDate());
				}
				//获取意向车型
				TPcIntentVehiclePO tpiv=new TPcIntentVehiclePO();
				if(tpc.getIntentVehicle()!=null&&!"".equals(tpc.getIntentVehicle())){
					tpiv.setSeriesId(new Long(tpc.getIntentVehicle()));
					tpiv=(TPcIntentVehiclePO) dao.select(tpiv).get(0);
				}
				//获取顾问
				TcUserPO tu=new TcUserPO();
				if(tpc.getAdviser()!=null){
					tu.setUserId(tpc.getAdviser());
					tu=(TcUserPO) dao.select(tu).get(0);
				}
				//获取战败形式
				TcCodePO tc=new TcCodePO();
				if(tpd.getDefeatWay()!=null&&!"".equals(tpd.getDefeatWay())){
					tc.setCodeId(tpd.getDefeatWay());
					tc=(TcCodePO) dao.select(tc).get(0);
				}
				String defeatReason=CommonUtils.getCodeDesc(tpd.getDefeatType());
				map.put("startDate", startDate);
				map.put("endDate", endDate);
				map.put("dealerId", logonUser.getDealerId());
				//获取战败车型
				TPcDefeatVehiclePO tpdv=new TPcDefeatVehiclePO();
				if(tpd.getDefeatModel()!=null&&!"".equals(tpd.getDefeatModel())){
					tpdv.setSeriesId(new Long(tpd.getDefeatModel()));
					tpdv=(TPcDefeatVehiclePO) dao.select(tpdv).get(0);
				}
				//获取实效时间
				String failureDate="";
				if(tpd.getFailureDate()!=null&&!"".equals(tpd.getFailureDate())){
					failureDate=sdf.format(tpd.getFailureDate());
				}
				map.put("userId", logonUser.getUserId().toString());
				List<Map<String,Object>> userList=CommonUtils.queryUser(map);
				act.setOutData("userList", userList);
				act.setOutData("map", map);
				act.setOutData("tpc", tpc);
				act.setOutData("tpd", tpd);
				act.setOutData("tu", tu);
				act.setOutData("tpdv", tpdv);
				act.setOutData("tpiv", tpiv);
				act.setOutData("tc", tc);
				act.setOutData("defeatReason", defeatReason);
				act.setOutData("failureDate", failureDate);
				act.setForword(DEFEAT_CHECK);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "战败审核界面");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 战败明细审核界面(直接战败)
	 */
	public void detailInitRedirect() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
				String defeatId=CommonUtils.checkNull(request.getParamValue("defeatId"));
				//查询线索信息
				TPcLeadsPO tpc = new TPcLeadsPO();
				tpc.setLeadsCode(Long.parseLong(defeatId));
				tpc=(TPcLeadsPO)dao.select(tpc).get(0);
				//分线索信息
				TPcLeadsAllotPO tpap = new TPcLeadsAllotPO();
				tpap.setLeadsCode(tpc.getLeadsCode());
				tpap=(TPcLeadsAllotPO)dao.select(tpap).get(0);
				//获取相关时间
				Map<String,String> map=new HashMap<String,String>();
				//获取意向车型
				TPcIntentVehiclePO tpiv=new TPcIntentVehiclePO();
				if(tpc.getIntentVehicle()!=null&&!"".equals(tpc.getIntentVehicle())){
					tpiv.setSeriesId(new Long(tpc.getIntentVehicle()));
					tpiv=(TPcIntentVehiclePO) dao.select(tpiv).get(0);
				}
				//获取顾问
				TcUserPO tu=new TcUserPO();
				if(tpap.getAdviser()!=null){
					tu.setUserId(Long.parseLong(tpap.getAdviser()));
					tu=(TcUserPO) dao.select(tu).get(0);
				}
				//获取战败车型
				TPcDefeatVehiclePO tpdv=new TPcDefeatVehiclePO();
				if(tpc.getDefeatModel()!=null&&!"".equals(tpc.getDefeatModel())){
					tpdv.setSeriesId(new Long(tpc.getDefeatModel()));
					tpdv=(TPcDefeatVehiclePO) dao.select(tpdv).get(0);
				}
				map.put("dealerId", logonUser.getDealerId());
				map.put("userId", Long.toString(logonUser.getUserId()));
				String audit_result=CommonUtils.getCodeDesc(tpc.getAuditStatus().toString());
				List<Map<String,Object>> userList=CommonUtils.queryUser(map);
				//查询审核的记录
				map.put("defeatId", defeatId);
				List<Map<String,Object>> auditList=dao.getAuditList(map);
				//准备一个原因分析
				TPcDefeatfailurePO tpd = new TPcDefeatfailurePO();
				tpd.setReasonAnalysis(tpc.getDefaultRemark());
				act.setOutData("auditList", auditList);
				act.setOutData("userList", userList);
				act.setOutData("map", map);
				act.setOutData("tpc", tpc);
				act.setOutData("tpdv", tpdv);
				act.setOutData("tu", tu);
				act.setOutData("tpd", tpd);
				act.setOutData("tpiv", tpiv);
				act.setOutData("tpap", tpap);
				act.setOutData("defeatReason", CommonUtils.getCodeDesc(tpc.getDefeatReason().toString()));
				act.setOutData("audit_result", audit_result);
				act.setForword(DEFEAT_CHECK_REDIRCT);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "战败审核界面");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 战败明细界面
	 */
	public void defeatDetailInit() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
				String defeatId=CommonUtils.checkNull(request.getParamValue("defeatId"));
				//获取战败数据
				TPcDefeatfailurePO tpd=new TPcDefeatfailurePO();
				tpd.setDefeatfailureId(new Long(defeatId));
				tpd=(TPcDefeatfailurePO) dao.select(tpd).get(0);
				//获取客户数据
				TPcCustomerPO tpc=new TPcCustomerPO();
				tpc.setCustomerId(tpd.getCustomerId());
				tpc=(TPcCustomerPO) dao.select(tpc).get(0);
				//获取相关时间
				Map<String,String> map=new HashMap<String,String>();
				SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
				String startDate="";
				String endDate="";
				if(tpd.getDefeatStartDate()!=null&&!"".equals(tpd.getDefeatStartDate())){
					startDate=sdf.format(tpd.getDefeatStartDate());
				}
				if(tpd.getDefeatEndDate()!=null&&!"".equals(tpd.getDefeatEndDate())){
					endDate=sdf.format(tpd.getDefeatEndDate());
				}
				//获取意向车型
				TPcIntentVehiclePO tpiv=new TPcIntentVehiclePO();
				if(tpc.getIntentVehicle()!=null&&!"".equals(tpc.getIntentVehicle())){
					tpiv.setSeriesId(new Long(tpc.getIntentVehicle()));
					tpiv=(TPcIntentVehiclePO) dao.select(tpiv).get(0);
				}
				//获取顾问
				TcUserPO tu=new TcUserPO();
				if(tpc.getAdviser()!=null){
					tu.setUserId(tpc.getAdviser());
					tu=(TcUserPO) dao.select(tu).get(0);
				}
				//获取战败形式
				TcCodePO tc=new TcCodePO();
				if(tpd.getDefeatWay()!=null&&!"".equals(tpd.getDefeatWay())){
					tc.setCodeId(tpd.getDefeatWay());
					tc=(TcCodePO) dao.select(tc).get(0);
				}
				String defeatReason=CommonUtils.getCodeDesc(tpd.getDefeatType());
				String defeatTime="";
				if(tpd.getDefeatEndDate()!=null&&!"".equals(tpd.getDefeatEndDate())){
					defeatTime=sdf.format(tpd.getDefeatEndDate());
				}
				//获取战败车型
				TPcDefeatVehiclePO tpdv=new TPcDefeatVehiclePO();
				if(tpd.getDefeatModel()!=null&&!"".equals(tpd.getDefeatModel())){
					tpdv.setSeriesId(new Long(tpd.getDefeatModel()));
					tpdv=(TPcDefeatVehiclePO) dao.select(tpdv).get(0);
				}
				//获取实效时间
				String failureDate="";
				if(tpd.getFailureDate()!=null&&!"".equals(tpd.getFailureDate())){
					failureDate=sdf.format(tpd.getFailureDate());
				}
				map.put("startDate", startDate);
				map.put("endDate", endDate);
				map.put("dealerId", logonUser.getDealerId());
				map.put("userId", Long.toString(logonUser.getUserId()));
				String audit_result=CommonUtils.getCodeDesc(tpd.getAuditStatus().toString());
				List<Map<String,Object>> userList=CommonUtils.queryUser(map);
				//查询审核的记录
				map.put("defeatId", defeatId);
				List<Map<String,Object>> auditList=dao.getAuditList(map);
				act.setOutData("auditList", auditList);
				act.setOutData("userList", userList);
				act.setOutData("map", map);
				act.setOutData("tpc", tpc);
				act.setOutData("tpd", tpd);
				act.setOutData("tpdv", tpdv);
				act.setOutData("tu", tu);
				act.setOutData("tpiv", tpiv);
				act.setOutData("tc", tc);
				act.setOutData("failureDate", failureDate);
				act.setOutData("defeatTime", defeatTime);
				act.setOutData("defeatReason", defeatReason);
				act.setOutData("audit_result", audit_result);
				act.setForword(DEFEAT_DETAIL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "战败审核界面");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 战败明细界面（直接战败）
	 */
	public void defeatDetailRedirect() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
				String defeatId=CommonUtils.checkNull(request.getParamValue("defeatId"));
				//查询线索信息
				TPcLeadsPO tpc = new TPcLeadsPO();
				tpc.setLeadsCode(Long.parseLong(defeatId));
				tpc=(TPcLeadsPO)dao.select(tpc).get(0);
				//分线索信息
				TPcLeadsAllotPO tpap = new TPcLeadsAllotPO();
				tpap.setLeadsCode(tpc.getLeadsCode());
				tpap=(TPcLeadsAllotPO)dao.select(tpap).get(0);
				
				//获取相关时间
				Map<String,String> map=new HashMap<String,String>();
				//获取意向车型
				TPcIntentVehiclePO tpiv=new TPcIntentVehiclePO();
				if(tpc.getIntentVehicle()!=null&&!"".equals(tpc.getIntentVehicle())){
					tpiv.setSeriesId(new Long(tpc.getIntentVehicle()));
					tpiv=(TPcIntentVehiclePO) dao.select(tpiv).get(0);
				}
				//获取顾问
				TcUserPO tu=new TcUserPO();
				if(tpap.getAdviser()!=null){
					tu.setUserId(Long.parseLong(tpap.getAdviser()));
					tu=(TcUserPO) dao.select(tu).get(0);
				}
				//获取战败车型
				TPcDefeatVehiclePO tpdv=new TPcDefeatVehiclePO();
				if(tpc.getDefeatModel()!=null&&!"".equals(tpc.getDefeatModel())){
					tpdv.setSeriesId(new Long(tpc.getDefeatModel()));
					tpdv=(TPcDefeatVehiclePO) dao.select(tpdv).get(0);
				}
				map.put("dealerId", logonUser.getDealerId());
				map.put("userId", Long.toString(logonUser.getUserId()));
				String audit_result=CommonUtils.getCodeDesc(tpc.getAuditStatus().toString());
				List<Map<String,Object>> userList=CommonUtils.queryUser(map);
				//查询战败记录
				TPcDefeatfailurePO tdp = new TPcDefeatfailurePO();
				tdp.setLeadsCode(tpc.getLeadsCode());
				tdp = (TPcDefeatfailurePO)dao.select(tdp).get(0);
				//查询审核的记录
				map.put("defeatId", tdp.getDefeatfailureId()+"");
				List<Map<String,Object>> auditList=dao.getAuditList(map);
				//准备一个原因分析
				TPcDefeatfailurePO tpd = new TPcDefeatfailurePO();
				tpd.setReasonAnalysis(tpc.getDefaultRemark());
				act.setOutData("auditList", auditList);
				act.setOutData("userList", userList);
				act.setOutData("map", map);
				act.setOutData("tpc", tpc);
				act.setOutData("tpdv", tpdv);
				act.setOutData("tu", tu);
				act.setOutData("tpd", tpd);
				act.setOutData("tpiv", tpiv);
				act.setOutData("defeatReason", CommonUtils.getCodeDesc(tpc.getDefeatReason().toString()));
				act.setOutData("audit_result", audit_result);
				act.setForword(DEFEAT_DETAIL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "战败审核界面");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	
	/**
	 * DCRC抽查
	 */
	public void detailCheckInit() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String defeatId=CommonUtils.checkNull(request.getParamValue("defeatId"));
			//获取战败数据
			TPcDefeatfailurePO tpd=new TPcDefeatfailurePO();
			tpd.setDefeatfailureId(new Long(defeatId));
			tpd=(TPcDefeatfailurePO) dao.select(tpd).get(0);
			//获取客户数据
			TPcCustomerPO tpc=new TPcCustomerPO();
			tpc.setCustomerId(tpd.getCustomerId());
			tpc=(TPcCustomerPO) dao.select(tpc).get(0);
			//获取相关时间
			Map<String,String> map=new HashMap<String,String>();
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
			String startDate="";
			String endDate="";
			if(tpd.getDefeatStartDate()!=null&&!"".equals(tpd.getDefeatStartDate())){
				startDate=sdf.format(tpd.getDefeatStartDate());
			}
			if(tpd.getDefeatEndDate()!=null&&!"".equals(tpd.getDefeatEndDate())){
				endDate=sdf.format(tpd.getDefeatEndDate());
			}
			//获取意向车型
			TPcIntentVehiclePO tpiv=new TPcIntentVehiclePO();
			if(tpc.getIntentVehicle()!=null&&!"".equals(tpc.getIntentVehicle())){
				tpiv.setSeriesId(new Long(tpc.getIntentVehicle()));
				tpiv=(TPcIntentVehiclePO) dao.select(tpiv).get(0);
			}
			//获取顾问
			TcUserPO tu=new TcUserPO();
			if(tpc.getAdviser()!=null){
				tu.setUserId(tpc.getAdviser());
				tu=(TcUserPO) dao.select(tu).get(0);
			}
			map.put("startDate", startDate);
			map.put("endDate", endDate);
			//获取战败形式
			TcCodePO tc=new TcCodePO();
			if(tpd.getDefeatWay()!=null&&!"".equals(tpd.getDefeatWay())){
				tc.setCodeId(tpd.getDefeatWay());
				tc=(TcCodePO) dao.select(tc).get(0);
			}
			String defeatReason=CommonUtils.getCodeDesc(tpd.getDefeatType());
			//获取审核的意见
			String auditRemark="";
			map.put("defeatId", defeatId);
			auditRemark=dao.queryAudit(map);
			//获取战败车型
			TPcDefeatVehiclePO tpdv=new TPcDefeatVehiclePO();
			if(tpd.getDefeatModel()!=null&&!"".equals(tpd.getDefeatModel())){
				tpdv.setSeriesId(new Long(tpd.getDefeatModel()));
				tpdv=(TPcDefeatVehiclePO) dao.select(tpdv).get(0);
			}
			//获取实效时间
			String failureDate="";
			if(tpd.getFailureDate()!=null&&!"".equals(tpd.getFailureDate())){
				failureDate=sdf.format(tpd.getFailureDate());
			}
			act.setOutData("failureDate", failureDate);
			act.setOutData("auditRemark", auditRemark);
			act.setOutData("map", map);
			act.setOutData("tpc", tpc);
			act.setOutData("tpd", tpd);
			act.setOutData("tu", tu);
			act.setOutData("tpiv", tpiv);
			act.setOutData("tpdv", tpdv);
			act.setOutData("tc", tc);
			act.setOutData("defeatReason", defeatReason);
			act.setForword(DCRC_DETAIL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "DCRC抽查");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 战败审核
	 */
	public void defeatAudit(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
				String defeatId=CommonUtils.checkNull(request.getParamValue("defeatId"));
				String status=CommonUtils.checkNull(request.getParamValue("status"));
				String auditRemark=CommonUtils.checkNull(request.getParamValue("auditRemark"));
				//获取审核前的状态
				Long beforeStatus=null;
				TPcDefeatfailurePO tpd0=new TPcDefeatfailurePO();
				tpd0.setDefeatfailureId(new Long(defeatId));
				tpd0=(TPcDefeatfailurePO) dao.select(tpd0).get(0);
				beforeStatus=tpd0.getAuditStatus();
				TPcDefeatfailurePO tpd=new TPcDefeatfailurePO();
				tpd.setDefeatfailureId(new Long(defeatId));
				TPcDefeatfailurePO tpd1=new TPcDefeatfailurePO();
				tpd1.setAuditStatus(new Long(status));
				String remark="经理审核："+status;
				if(status=="60401002"||"60401002".equals(status)){
					tpd1.setMgrConDate(new Date());
					//修改客户状态为战败客户
					TPcCustomerPO tp1=new TPcCustomerPO();
					tp1.setCustomerId(tpd0.getCustomerId());
					TPcCustomerPO tp2=new TPcCustomerPO();
					if(Constant.FAILURE_TYPE_01.toString().equals(tpd0.getDefeatfailureType().toString())){
						remark=remark+"-战败";
						tp2.setCtmType(Constant.CTM_TYPE_04.toString());
						tp2.setCtmRank(Constant.INTENT_TYPE_E.toString());
						CommonUtils.setRemindDone(defeatId,Constant.REMIND_TYPE_10.toString());//结束原来的提醒
					}else{
						remark=remark+"-失效";
						tp2.setCtmType(Constant.CTM_TYPE_05.toString());
						tp2.setCtmRank(Constant.INTENT_TYPE_L.toString());
						CommonUtils.setRemindDone(defeatId,Constant.REMIND_TYPE_11.toString());//结束原来的提醒
					}
					tp2.setDefeatRemark(remark);
					dao.update(tp1, tp2);
				}
				dao.update(tpd, tpd1);
				String seqId=SequenceManager.getSequence("");
				TPcDefeatfailureAuditPO tpda=new  TPcDefeatfailureAuditPO();
				tpda.setManagerAuditId(new Long(seqId));
				tpda.setDefeatfailureId(new Long(defeatId));
				tpda.setAuditRemark(auditRemark);
				tpda.setCreateBy(logonUser.getUserId());
				tpda.setCreateDate(new Date());
				tpda.setBeforeAudit(beforeStatus.toString());
				tpda.setStatus(new Long(Constant.STATUS_ENABLE));
				tpda.setAfterAudit(status);
				tpda.setAuditType(Constant.DEFEAT_AUDIT_TYPE_01.toString());
				dao.insert(tpda);
				//战败审核通过结束提醒
				if("60401002".equals(status)){
					CommonUtils.setRemindDone(defeatId,Constant.REMIND_TYPE_10.toString());//结束原来的提醒
					CommonUtils.addContackPoint(Constant.POINT_WAY_10, auditRemark, tpd0.getCustomerId().toString(), logonUser.getUserId().toString(), logonUser.getDealerId());
				}else{
					CommonUtils.addContackPoint(Constant.POINT_WAY_11, auditRemark, tpd0.getCustomerId().toString(), logonUser.getUserId().toString(), logonUser.getDealerId());
				}
				act.setOutData("flag", 1);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "战败审核");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 战败审核(直接战败)
	 */
	public void defeatAuditRedirect(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
				String defeatId=CommonUtils.checkNull(request.getParamValue("defeatId"));
				String status=CommonUtils.checkNull(request.getParamValue("status"));
				String auditRemark=CommonUtils.checkNull(request.getParamValue("auditRemark"));
				//修改记录
				TPcLeadsAllotPO tpc=new TPcLeadsAllotPO();
				tpc.setLeadsCode(Long.parseLong(defeatId));
				TPcLeadsAllotPO tpc1=new TPcLeadsAllotPO();
				tpc1.setIfConfirm(Constant.ADVISER_CONFIRM_02);
				dao.update(tpc, tpc1);
				TPcLeadsPO tpdf=new TPcLeadsPO();
				tpdf.setLeadsCode(Long.parseLong(defeatId));
				TPcLeadsPO tpdf1=new TPcLeadsPO();
				tpdf1.setLeadsStatus(Constant.LEADS_STATUS_02.longValue());
				tpdf1.setAuditStatus(Constant.FAILURE_AUDIT_02);
				tpdf1.setIfHandle(Constant.IF_TYPE_YES); // 是否已处理
				dao.update(tpdf, tpdf1);
				//查询战败记录
				TPcDefeatfailurePO tdp = new TPcDefeatfailurePO();
				tdp.setLeadsCode(tpc.getLeadsCode());
				TPcDefeatfailurePO tdp1 = new TPcDefeatfailurePO();
				tdp1.setAuditStatus(Constant.FAILURE_AUDIT_02.longValue());
				dao.update(tdp, tdp1);
				tdp = (TPcDefeatfailurePO)dao.select(tdp).get(0);
				//插入审核记录
				String seqId=SequenceManager.getSequence("");
				TPcDefeatfailureAuditPO tpda=new  TPcDefeatfailureAuditPO();
				tpda.setManagerAuditId(new Long(seqId));
				tpda.setDefeatfailureId(tdp.getDefeatfailureId());
				tpda.setAuditRemark(auditRemark);
				tpda.setCreateBy(logonUser.getUserId());
				tpda.setCreateDate(new Date());
				tpda.setBeforeAudit(Constant.FAILURE_AUDIT_01+"");
				tpda.setStatus(new Long(Constant.STATUS_ENABLE));
				tpda.setAfterAudit(status); //经理审核通过
				tpda.setAuditType(Constant.DEFEAT_AUDIT_TYPE_01.toString());
				dao.insert(tpda);
				act.setOutData("flag", 1);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "战败审核");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 驳回战败跟进
	 */
	public void defeatFollow(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
				String defeatId=CommonUtils.checkNull(request.getParamValue("defeatId"));
				String status=CommonUtils.checkNull(request.getParamValue("status"));
				String auditRemark=CommonUtils.checkNull(request.getParamValue("auditRemark"));
				String adviser=CommonUtils.checkNull(request.getParamValue("adviser"));
				String ctm_rank=CommonUtils.checkNull(request.getParamValue("ctm_rank"));
				//获取审核前的状态
				Long beforeStatus=null;
				TPcDefeatfailurePO tpd0=new TPcDefeatfailurePO();
				tpd0.setDefeatfailureId(new Long(defeatId));
				tpd0=(TPcDefeatfailurePO) dao.select(tpd0).get(0);
				beforeStatus=tpd0.getAuditStatus();
				TPcDefeatfailurePO tpd=new TPcDefeatfailurePO();
				tpd.setDefeatfailureId(new Long(defeatId));
				TPcDefeatfailurePO tpd1=new TPcDefeatfailurePO();
				tpd1.setAuditStatus(new Long(status));
				dao.update(tpd, tpd1);
				String seqId=SequenceManager.getSequence("");
				TPcDefeatfailureAuditPO tpda=new  TPcDefeatfailureAuditPO();
				tpda.setManagerAuditId(new Long(seqId));
				tpda.setDefeatfailureId(new Long(defeatId));
				tpda.setAuditRemark(auditRemark);
				tpda.setCreateBy(logonUser.getUserId());
				tpda.setCreateDate(new Date());
				tpda.setBeforeAudit(beforeStatus.toString());
				tpda.setStatus(new Long(Constant.STATUS_ENABLE));
				tpda.setAfterAudit(status);
				tpda.setAuditType(Constant.DEFEAT_AUDIT_TYPE_01.toString());
				dao.insert(tpda);
				//修改顾问
				TPcCustomerPO tpc=new TPcCustomerPO();
				tpc.setCustomerId(tpd0.getCustomerId());
				TPcCustomerPO tpc1=new TPcCustomerPO();
				tpc1.setAdviser(new Long(adviser));
				//修改客户状态有望客户
				tpc1.setCtmType(Constant.CTM_TYPE_02.toString());
				tpc1.setCtmRank(ctm_rank);
				dao.update(tpc, tpc1);
				
				//开启一个跟进任务
				String nextFollowDate = act.getRequest().getParamValue("next_follow_date");//下次跟进时间
				String followType = act.getRequest().getParamValue("follow_type");//跟进方式
				String followPlan = act.getRequest().getParamValue("follow_plan");//跟进计划
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				//新增下次跟进任务表
				String newTaskId=SequenceManager.getSequence("");
				
				TPcDefeatfailurePO tpdf=new TPcDefeatfailurePO();
				tpdf.setDefeatfailureId(new Long(defeatId));
				TPcDefeatfailurePO tpdf1=new TPcDefeatfailurePO();
				tpdf1.setNewLevel(ctm_rank);//设置战败失效任务的新的意向等级
				tpdf1.setNextTask(Long.parseLong(newTaskId));//设置战败任务的下次任务是跟进的任务ID
				dao.update(tpdf, tpdf1);
				
				TPcFollowPO followPo = new TPcFollowPO();
				followPo.setFollowId(Long.parseLong(newTaskId));
				followPo.setCustomerId(tpd0.getCustomerId());
				followPo.setFollowDate(sdf.parse(nextFollowDate));
				followPo.setOldLevel(ctm_rank);
				followPo.setNewLevel(null);
				followPo.setTaskStatus(Constant.TASK_STATUS_01);
				followPo.setFollowType(followType);
				followPo.setMgrTip(followPlan);
				followPo.setCreateDate(new Date());
				followPo.setCreateBy(logonUser.getUserId().toString());
				followPo.setPreviousTask(tpd0.getPreviousTask());
				followPo.setOldSalesProgress(tpd0.getOldSalesProgress());
				
				//如果是战败
				if("60391001".equals(tpd0.getDefeatfailureType().toString())){
					followPo.setRestart_type(Constant.RESTART_TYPE_02.toString());
					CommonUtils.setRemindDone(defeatId,Constant.REMIND_TYPE_10.toString());//结束原来的提醒
				}else{
					followPo.setRestart_type(Constant.RESTART_TYPE_03.toString());
					CommonUtils.setRemindDone(defeatId,Constant.REMIND_TYPE_11.toString());//结束原来的提醒
				}
				dao.insert(followPo);
				
				
				//开启一个新的跟进提醒
				CommonUtils.addRemindInfo(Constant.REMIND_TYPE_05.toString(), newTaskId, tpd0.getCustomerId().toString(), logonUser.getDealerId(), adviser, nextFollowDate,"");
				act.setOutData("flag", 1);
				
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "战败审核");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 驳回战败跟进(直接战败)
	 */
	public void defeatFollowRedirect(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
				String defeatId=CommonUtils.checkNull(request.getParamValue("defeatId"));
				String status=CommonUtils.checkNull(request.getParamValue("status"));
				String auditRemark=CommonUtils.checkNull(request.getParamValue("auditRemark"));
				String adviser=CommonUtils.checkNull(request.getParamValue("adviser"));
				String ctm_rank=CommonUtils.checkNull(request.getParamValue("ctm_rank"));
				String nextFollowDate = act.getRequest().getParamValue("next_follow_date");//下次跟进时间
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				//修改顾问
				TPcLeadsAllotPO tpc=new TPcLeadsAllotPO();
				tpc.setLeadsCode(Long.parseLong(defeatId));
				TPcLeadsAllotPO tpc1=new TPcLeadsAllotPO();
				tpc1.setAdviser(adviser);
				tpc1.setAllotAdviserDate(sdf.parse(nextFollowDate));
				//修改客户状态有望客户
				tpc1.setStatus(Constant.STATUS_ENABLE);
				tpc1.setIfConfirm(Constant.ADVISER_CONFIRM_01);
				dao.update(tpc, tpc1);
				TPcLeadsPO tpdf=new TPcLeadsPO();
				tpdf.setLeadsCode(Long.parseLong(defeatId));
				TPcLeadsPO tpdf1=new TPcLeadsPO();
				tpdf1.setIntentType(ctm_rank);//设置战败失效任务的新的意向等级
				tpdf1.setLeadsStatus(Constant.LEADS_STATUS_01.longValue());
				// tpdf1.setIsDirectDefeat(Constant.STATUS_ENABLE);
				tpdf1.setAuditStatus(Constant.FAILURE_AUDIT_01);
				tpdf1.setIfHandle(Constant.IF_TYPE_NO); // 是否已处理
				dao.update(tpdf, tpdf1);
				//查询战败记录
				TPcDefeatfailurePO tdp = new TPcDefeatfailurePO();
				tdp.setLeadsCode(tpdf.getLeadsCode());
				TPcDefeatfailurePO tdp1 = new TPcDefeatfailurePO();
				tdp1.setAuditStatus(Constant.FAILURE_AUDIT_03.longValue());
				dao.update(tdp, tdp1);
				tdp = (TPcDefeatfailurePO)dao.select(tdp).get(0);
				//插入审核记录
				String seqId=SequenceManager.getSequence("");
				TPcDefeatfailureAuditPO tpda=new  TPcDefeatfailureAuditPO();
				tpda.setManagerAuditId(new Long(seqId));
				tpda.setDefeatfailureId(tdp.getDefeatfailureId());
				tpda.setAuditRemark(auditRemark);
				tpda.setCreateBy(logonUser.getUserId());
				tpda.setCreateDate(new Date());
				tpda.setBeforeAudit(Constant.FAILURE_AUDIT_01+"");
				tpda.setStatus(new Long(Constant.STATUS_ENABLE));
				tpda.setAfterAudit(status); //经理审核通过
				tpda.setAuditType(Constant.DEFEAT_AUDIT_TYPE_01.toString());
				dao.insert(tpda);
				act.setOutData("flag", 1);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "战败审核");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 战败邀约
	 */
	public void defeatInvite(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
				String defeatId=CommonUtils.checkNull(request.getParamValue("defeatId"));
				String status=CommonUtils.checkNull(request.getParamValue("status"));
				String auditRemark=CommonUtils.checkNull(request.getParamValue("auditRemark"));
				String adviser=CommonUtils.checkNull(request.getParamValue("adviser"));
				String ctm_rank=CommonUtils.checkNull(request.getParamValue("ctm_rank"));
				String inviteTip=CommonUtils.checkNull(request.getParamValue("inviteTip"));
				//获取审核前的状态
				Long beforeStatus=null;
				TPcDefeatfailurePO tpd0=new TPcDefeatfailurePO();
				tpd0.setDefeatfailureId(new Long(defeatId));
				tpd0=(TPcDefeatfailurePO) dao.select(tpd0).get(0);
				beforeStatus=tpd0.getAuditStatus();
				TPcDefeatfailurePO tpd=new TPcDefeatfailurePO();
				tpd.setDefeatfailureId(new Long(defeatId));
				TPcDefeatfailurePO tpd1=new TPcDefeatfailurePO();
				tpd1.setAuditStatus(new Long(status));
				dao.update(tpd, tpd1);
				String seqId=SequenceManager.getSequence("");
				TPcDefeatfailureAuditPO tpda=new  TPcDefeatfailureAuditPO();
				tpda.setManagerAuditId(new Long(seqId));
				tpda.setDefeatfailureId(new Long(defeatId));
				tpda.setAuditRemark(auditRemark);
				tpda.setCreateBy(logonUser.getUserId());
				tpda.setCreateDate(new Date());
				tpda.setBeforeAudit(beforeStatus.toString());
				tpda.setStatus(new Long(Constant.STATUS_ENABLE));
				tpda.setAfterAudit(status);
				tpda.setAuditType(Constant.DEFEAT_AUDIT_TYPE_01.toString());
				dao.insert(tpda);
				//修改顾问
				TPcCustomerPO tpc=new TPcCustomerPO();
				tpc.setCustomerId(tpd0.getCustomerId());
				TPcCustomerPO tpc1=new TPcCustomerPO();
				tpc1.setAdviser(new Long(adviser));
				//修改客户状态有望客户
				tpc1.setCtmType(Constant.CTM_TYPE_02.toString());
				tpc1.setCtmRank(ctm_rank);
				dao.update(tpc, tpc1);
				//开启邀约任务
				String xqfx = act.getRequest().getParamValue("next_follow_date"); //需求分析
				String yymb = act.getRequest().getParamValue("yymb"); //邀约目标
				String ydkhxrsj = act.getRequest().getParamValue("ydkhxrsj"); //赢得客户信任设计
				String gdkhqjsj = act.getRequest().getParamValue("gdkhqjsj"); //感动客户情景设计
				String inviteType = act.getRequest().getParamValue("invite_type"); //邀约类型
				String inviteRemark = act.getRequest().getParamValue("invite_info"); //邀约成效
				String inviteShopRemark = act.getRequest().getParamValue("invite_shop_info"); //邀约到店成效
				String planInviteDate = act.getRequest().getParamValue("plan_invite_date"); //计划邀约时间
				String planMeetDate = act.getRequest().getParamValue("plan_meet_date"); //计划见面时间
				String inviteTypeNew = act.getRequest().getParamValue("invite_type_new"); //邀约方式
				String inviteShopDate = act.getRequest().getParamValue("invite_shop_date"); //预约到店时间
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				TPcInvitePO invitePo = new TPcInvitePO();
				String newTaskId=SequenceManager.getSequence("");

				TPcDefeatfailurePO tpdf=new TPcDefeatfailurePO();
				tpdf.setDefeatfailureId(new Long(defeatId));
				TPcDefeatfailurePO tpdf1=new TPcDefeatfailurePO();
				tpdf1.setNewLevel(ctm_rank);//设置战败失效任务的新的意向等级
				tpdf1.setNextTask(Long.parseLong(newTaskId));//设置战败任务的下次任务是邀约任务ID
				dao.update(tpdf, tpdf1);
				invitePo.setInviteId(Long.parseLong(newTaskId));
				invitePo.setCustomerId(tpd0.getCustomerId());
				invitePo.setCreateDate(new Date());
				invitePo.setCreateBy(logonUser.getUserId().toString());
				invitePo.setPlanInviteDate(sdf.parse(planInviteDate));
				invitePo.setPlanMeetDate(sdf.parse(planMeetDate));
				invitePo.setTaskStatus(Constant.TASK_STATUS_01);
				invitePo.setInviteType(Integer.parseInt(inviteTypeNew));
				invitePo.setDirectorAudit(Constant.DIRECTOR_AUDIT_04);
				invitePo.setIfPlan(Constant.IF_TYPE_NO);
				invitePo.setOldLevel(ctm_rank);
				invitePo.setNewLevel(null);
				invitePo.setPreviousTask(tpd0.getPreviousTask());
				invitePo.setMgrTip(inviteTip);
				invitePo.setOldSalesProgress(tpd0.getSalesProgress());
				//判断是否做了计划（4个框有值说明为做了计划）
				if(xqfx!=null&&!"".equals(xqfx)) {
					invitePo.setIfPlan(Constant.IF_TYPE_YES);
					invitePo.setRequirement(xqfx);
					invitePo.setInviteTarget(yymb);
					invitePo.setTrustDesign(ydkhxrsj);
					invitePo.setSceneDesign(gdkhqjsj);
					invitePo.setDirectorAudit(Constant.DIRECTOR_AUDIT_01);
				}
				//如果是战败
				if("60391001".equals(tpd0.getDefeatfailureType().toString())){
					invitePo.setRestart_type(Constant.RESTART_TYPE_02.toString());
					CommonUtils.setRemindDone(defeatId,Constant.REMIND_TYPE_10.toString());//结束原来的提醒
				}else{
					invitePo.setRestart_type(Constant.RESTART_TYPE_03.toString());
					CommonUtils.setRemindDone(defeatId,Constant.REMIND_TYPE_11.toString());//结束原来的提醒
				}
				dao.insert(invitePo);
				//战败审核通过结束提醒
				//CommonUtils.setRemindDone(defeatId,Constant.REMIND_TYPE_10.toString());//结束原来的提醒
				//开启一个新的跟进提醒
				CommonUtils.addRemindInfo(Constant.REMIND_TYPE_06.toString(), newTaskId, tpd0.getCustomerId().toString(), logonUser.getDealerId(), adviser, planInviteDate,"");
				act.setOutData("flag", 1);
				//act.setOutData("flag", 1);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "战败审核");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * dcrc审核
	 */
	public void dcrcAudit(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
				String defeatId=CommonUtils.checkNull(request.getParamValue("defeatId"));
				String status=CommonUtils.checkNull(request.getParamValue("status"));
				String auditRemark=CommonUtils.checkNull(request.getParamValue("auditRemark"));
				//获取审核前的状态
				Long beforeStatus=null;
				TPcDefeatfailurePO tpd0=new TPcDefeatfailurePO();
				tpd0.setDefeatfailureId(new Long(defeatId));
				tpd0=(TPcDefeatfailurePO) dao.select(tpd0).get(0);
				beforeStatus=tpd0.getAuditStatus();
				TPcDefeatfailurePO tpd=new TPcDefeatfailurePO();
				tpd.setDefeatfailureId(new Long(defeatId));
				TPcDefeatfailurePO tpd1=new TPcDefeatfailurePO();
				tpd1.setAuditStatus(new Long(status));
				if("60401004".equals(status)){
					tpd1.setDcrcConDate(new Date());
					
				}
				dao.update(tpd, tpd1);
				String seqId=SequenceManager.getSequence("");
				TPcDefeatfailureAuditPO tpda=new  TPcDefeatfailureAuditPO();
				tpda.setManagerAuditId(new Long(seqId));
				tpda.setDefeatfailureId(new Long(defeatId));
				tpda.setAuditRemark(auditRemark);
				tpda.setCreateBy(logonUser.getUserId());
				tpda.setCreateDate(new Date());
				tpda.setBeforeAudit(beforeStatus.toString());
				tpda.setAfterAudit(status);
				tpda.setStatus(new Long(Constant.STATUS_ENABLE));
				tpda.setAuditType(Constant.DEFEAT_AUDIT_TYPE_02.toString());
				dao.insert(tpda);
				//获取客户信息
				TPcCustomerPO tpc=new TPcCustomerPO();
				tpc.setCustomerId(tpd0.getCustomerId());
				tpc=(TPcCustomerPO) dao.select(tpc).get(0);
				//战败审核通过结束提醒
				if("60401004".equals(status)){
					if("60391001".equals(tpd0.getDefeatfailureType().toString())){
						CommonUtils.addRemindInfo(Constant.REMIND_TYPE_10.toString(), defeatId, tpd0.getCustomerId().toString(), logonUser.getDealerId(),"" , new Date().toString(),"");
					}else{
						CommonUtils.addRemindInfo(Constant.REMIND_TYPE_11.toString(), defeatId, tpd0.getCustomerId().toString(), logonUser.getDealerId(),"" , new Date().toString(),"");
						
					}
					CommonUtils.addContackPoint(Constant.POINT_WAY_14, auditRemark, tpd0.getCustomerId().toString(), logonUser.getUserId().toString(), logonUser.getDealerId());
				}else{
					CommonUtils.addContackPoint(Constant.POINT_WAY_15, auditRemark, tpd0.getCustomerId().toString(), logonUser.getUserId().toString(), logonUser.getDealerId());
					
				}
				act.setOutData("flag", 1);
				
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "战败审核");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * FUNCTION : 
	 * 
	 * @param :
	 * @return :跳转到重新分配顾问
	 * @throws :
	 *             
	 */
	public void toAdviserInit() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = null;
		RequestWrapper request = act.getRequest();
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		
			String ctmId=request.getParamValue("ctmId");
			Map<String,String> map=new HashMap<String,String>();
			map.put("dealerId", logonUser.getDealerId());
			List<Map<String,Object>> userList=CommonUtils.queryUser(map);
			act.setOutData("userList", userList);
			act.setOutData("ctmId", ctmId);
			act.setForword(TO_ADVISER_DETAIL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "实销信息上报:查询销售顾问列表");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * FUNCTION : 
	 * 
	 * @param :
	 * @return :重新分配顾问的方法
	 * @throws :
	 *             
	 */
	public void dispatchAdviser() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = null;
		RequestWrapper request = act.getRequest();
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		
			String ctmId=request.getParamValue("ctmId");
			String adviser=request.getParamValue("adviser");
			TPcCustomerPO tpc=new TPcCustomerPO();
			tpc.setCustomerId(new Long(ctmId));
			TPcCustomerPO tpc1=new TPcCustomerPO();
			tpc1.setAdviser(new Long(adviser));
			int i=dao.update(tpc, tpc1);
			act.setOutData("flag", i);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "重新分配顾问的方法");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	
}
