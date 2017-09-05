package com.infodms.dms.actions.crm.basedata;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.actions.sales.ordermanage.orderaudit.OrderResourceReserveFirst;
import com.infodms.dms.actions.util.LockControl;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.dao.crm.basedata.PcGroupDao;
import com.infodms.dms.dao.crm.basedata.PcVechileDao;
import com.infodms.dms.dao.sales.financemanage.AccountBalanceDetailDao;
import com.infodms.dms.dao.sales.ordermanage.audit.OrderAuditDao;
import com.infodms.dms.dao.sales.ordermanage.orderreport.OrderReportDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TPcCompetVechilePO;
import com.infodms.dms.po.TPcGroupPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;

public class PcCompetVechile {
	private Logger logger = Logger.getLogger(OrderResourceReserveFirst.class);
	private ActionContext act = ActionContext.getContext();
	RequestWrapper request = act.getRequest();
	private final PcVechileDao dao = PcVechileDao.getInstance();

	private final String VECHILE_QUERY_URL = "/jsp/crm/basedata/pcVechileInit.jsp";// 竞品维护维护查询页面
	private final String VECHILE_ADD_INIT = "/jsp/crm/basedata/pcVechileAdd.jsp";// 竞品维护新增页面
	private final String VECHILE_UPDATE_INIT = "/jsp/crm/basedata/pcVechileUpdate.jsp";// 竞品维护修改页面
	private final String VECHILE_LIST_INIT = "/jsp/crm/basedata/vechileList.jsp";// 竞品维护修改页面
	public void doInit() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			String funcStr=CommonUtils.judgeUserHasFunc(logonUser);
				act.setOutData("funcStr", funcStr);
				act.setForword(VECHILE_QUERY_URL);
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
	public void vechileQueryList(){
		AclUserBean logonUser = null;
		try {
			RequestWrapper request = act.getRequest();
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);	
			String status = CommonUtils.checkNull(request.getParamValue("status"));		//经销商用户组的状态
			String competName =  CommonUtils.checkNull(request.getParamValue("competName"));			//组名称
			Map<String ,String > map=new HashMap<String,String>();
			map.put("status", status);
			map.put("competName", competName);
			String pageSize=CommonUtils.checkNull(request.getParamValue("pageSize"));	
			pageSize=pageSize==null||"".equals(pageSize)?"10":pageSize;
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage")) 
					: 1; // 处理当前页
			PageResult<Map<String, Object>> ps = dao.getVechileQueryList(map, Integer.parseInt(pageSize), curPage);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "所有车辆资源查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	} 
	/**
	 * 跳转到竞品添加界面
	 */
	public void vechileAddInit() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
				act.setForword(VECHILE_ADD_INIT);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "经销商用户组查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 竞品的添加
	 */
	public void vechileAdd(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
				String competCode=CommonUtils.checkNull(request.getParamValue("competCode"));
				String competName=CommonUtils.checkNull(request.getParamValue("competName"));
				String parId=CommonUtils.checkNull(request.getParamValue("parId"));
				String competLevel="1";
				//如果parId没有值就表示第一级
				if(parId==null||"".equals(parId)){
					parId="0";
				}else{
					//根据parid 获取competLevel
					TPcCompetVechilePO tgv=new TPcCompetVechilePO();
					tgv.setCompetId(new Long(parId));
					tgv=(TPcCompetVechilePO) dao.select(tgv).get(0);
					competLevel=(tgv.getCompetLevel().intValue()+1)+"";
					
				}
				TPcCompetVechilePO tgp=new TPcCompetVechilePO();
				String seq=SequenceManager.getSequence("");
				tgp.setCompetId(new Long(seq));
				tgp.setCompetCode(competCode);
				tgp.setCompetName(competName);
				tgp.setParId(new Long(parId));
				tgp.setCreateDate(new Date());
				tgp.setCompetLevel(new Integer(competLevel));
				tgp.setCreateBy(logonUser.getUserId());
				tgp.setStatus(new Long(10011001));
				int a=0;
				try {
					dao.insert(tgp);
					a=1;
				} catch (Exception e) {
					a=0;
				}
				act.setOutData("flag", a);
				
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "经销商用户组查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 竞品新增界面
	 */
	public void vechileUpdateInit() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
				String competId=CommonUtils.checkNull(request.getParamValue("competId"));
				TPcCompetVechilePO tgp=new TPcCompetVechilePO();
				tgp.setCompetId(new Long(competId));
				tgp=(TPcCompetVechilePO) dao.select(tgp).get(0);
				TPcCompetVechilePO tgp1=new TPcCompetVechilePO();
				tgp1.setCompetId(new Long(tgp.getParId()));
				if(tgp.getParId().longValue()!=0){
					tgp1=(TPcCompetVechilePO) dao.select(tgp1).get(0);
				}
				act.setOutData("tgp", tgp);
				act.setOutData("tgp1", tgp1);
				act.setForword(VECHILE_UPDATE_INIT);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "经销商用户组查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 竞品的添加
	 */
	public void vechileUpdate(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
				String competCode=CommonUtils.checkNull(request.getParamValue("competCode"));
				String competId=CommonUtils.checkNull(request.getParamValue("competId"));
				String competName=CommonUtils.checkNull(request.getParamValue("competName"));
				String parId=CommonUtils.checkNull(request.getParamValue("parId"));
				String status=CommonUtils.checkNull(request.getParamValue("status"));
				String competLevel="1";
				//如果parId没有值就表示第一级
				if(parId==null||"".equals(parId)||"0".equals(parId)){
					parId="0";
				}else{
					//根据parid 获取competLevel
					TPcCompetVechilePO tgv=new TPcCompetVechilePO();
					tgv.setCompetId(new Long(parId));
					tgv=(TPcCompetVechilePO) dao.select(tgv).get(0);
					competLevel=(tgv.getCompetLevel().intValue()+1)+"";
					
				}
				TPcCompetVechilePO tgp=new TPcCompetVechilePO();
				tgp.setCompetCode(competCode);
				tgp.setCompetName(competName);
				tgp.setParId(new Long(parId));
				tgp.setUpdateDate(new Date());
				tgp.setCompetLevel(new Integer(competLevel));
				tgp.setUpdateBy(logonUser.getUserId());
				tgp.setStatus(new Long(status));
				TPcCompetVechilePO tgp0=new TPcCompetVechilePO();
				tgp0.setCompetId(new Long(competId));
				int a=0;
				try {
					a=dao.update(tgp0,tgp);
				} catch (Exception e) {
					a=0;
				}
				act.setOutData("flag", a);
				
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "经销商用户组查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * FUNCTION : 实销信息上报:查询销售顾问列表
	 * 
	 * @param :
	 * @return :
	 * @throws :
	 *             LastUpdate : 2010-6-18
	 */
	public void toCompetVechileList() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = null;
		try {
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			act.getSession().get(Constant.LOGON_USER);
			String level=request.getParamValue("level");
			act.setOutData("competLevel", level);
			act.setForword(VECHILE_LIST_INIT);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "实销信息上报:查询销售顾问列表");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * FUNCTION : 实销信息上报:查询大客户列表结果展示
	 * 
	 * @param :
	 * @return :
	 * @throws :
	 *             LastUpdate : 2010-6-18
	 */
	public void getCompetVechileList() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = null;
		try {
			RequestWrapper request = act.getRequest();
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			act.getSession().get(Constant.LOGON_USER);
			String competCode = CommonUtils.checkNull(request.getParamValue("competCode"));
			String competName = CommonUtils.checkNull(request.getParamValue("competName"));
			String competLevel=CommonUtils.checkNull(request.getParamValue("competLevel"));
			Map<String,String> map=new HashMap<String,String>();
			map.put("competCode", competCode);
			map.put("competName", competName);
			map.put("competLevel", competLevel);
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
			PageResult<Map<String, Object>> ps = dao.getCompetVechileList(map,Constant.PAGE_SIZE, curPage);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "实销信息上报:查询销售顾问列表结果展示");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	
}
