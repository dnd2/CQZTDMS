package com.infodms.dms.actions.scsj.scsjManager;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.actions.parts.baseManager.activityPartSet.activityPartSetAction;
import com.infodms.dms.actions.report.dmsReport.ApplicationDao;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.tag.BaseUtils;
import com.infodms.dms.dao.sales.salesInfoManage.SalesInfoQueryDAO;
import com.infodms.dms.dao.scsj.ScsjManagerDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TtScsjyydjPO;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.infox.util.StringUtil;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;

public class scsjManagerAction {

	public Logger logger = Logger.getLogger(activityPartSetAction.class);
	private static final String SCSJ_INIT_JSP_FAC = "/jsp/scsj/scsjManager/scsjManagerInitForFac.jsp";//供应商选择页面
	private static final String SCSJ_INIT_JSP_DLR = "/jsp/scsj/scsjManager/scsjManagerInit.jsp";//供应商选择页面
	private static final String SCSJ_SHOWDLR = "/jsp/scsj/scsjManager/showDlr.jsp";//供应商选择页面
	private static final ScsjManagerDao dao = ScsjManagerDao.getInstance();
	private final static Map<String, String> stateMap1 = new LinkedHashMap<String, String>();
    private final static Map<String, String> stateMapByDlr = new LinkedHashMap<String, String>();
    private final static Map<String,String> stateMap = new  LinkedHashMap<String, String>();

    static{
        stateMap.put("1", "系统已处理");
        stateMap.put("2", "已跟进");
//        stateMap.put("3", "滞后");
        stateMap1.put("0", "已转经销商");
        stateMap1.put("1", "未转经销商");
        stateMapByDlr.put("0", "上报");
        stateMapByDlr.put("1", "已跟进");
        stateMapByDlr.put("2", "抢单");
    }
	/**
	 * 
	 * @Title : 跳转至试乘试驾初始化页面
	 * @param :
	 * @return :
	 * @throws : LastDate : 2014-5-22
	 */
	public void scsjInit() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
            act.setOutData("stateMap", stateMap);
            act.setOutData("stateMap1", stateMap1);
            act.setOutData("stateMapByDlr", stateMapByDlr);
            act.setOutData("fromInfo", dao.getFormInfo());
			if(StringUtil.isEmpty(logonUser.getDealerId())){
				act.setForword(SCSJ_INIT_JSP_FAC);
			}else{
				act.setForword(SCSJ_INIT_JSP_DLR);
			}
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "试乘试驾初始化");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 
	 * @Title : 经销商抢单
	 * @param :
	 * @return :
	 * @throws : LastDate : 2014-5-22
	 */
	public void mod1() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		try {
			//TODO 判断是否已经被别人抢了
			String ksId = CommonUtils.checkNull(request.getParamValue("ksId"));
			TtScsjyydjPO ttScsjyydjPO1 = new TtScsjyydjPO();
			TtScsjyydjPO ttScsjyydjPO2 = new TtScsjyydjPO();
			ttScsjyydjPO1.setKsid(ksId);
			ttScsjyydjPO2.setDealerId(Long.valueOf(logonUser.getDealerId()));
			ttScsjyydjPO2.setOrgCode("");
			ttScsjyydjPO2.setRootOrgId(0l);
			ttScsjyydjPO2.setTime3(new Date());
			ttScsjyydjPO2.setUpdateBy(logonUser.getUserId());
			ttScsjyydjPO2.setIsAllocation(Constant.IF_TYPE_YES);
			dao.update(ttScsjyydjPO1, ttScsjyydjPO2);
			act.setOutData("stateMap", stateMap);
            act.setOutData("stateMap1", stateMap1);
            act.setOutData("stateMapByDlr", stateMapByDlr);
			act.setForword(SCSJ_INIT_JSP_DLR);
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "试乘试驾初始化");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 
	 * @Title : 经销商上报
	 * @param :
	 * @return :
	 * @throws : LastDate : 2014-5-22
	 */
	public void mod2() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		try {
			String ksId = CommonUtils.checkNull(request.getParamValue("ksId"));
			TtScsjyydjPO ttScsjyydjPO1 = new TtScsjyydjPO();
			TtScsjyydjPO ttScsjyydjPO2 = new TtScsjyydjPO();
			ttScsjyydjPO1.setKsid(ksId);
			ttScsjyydjPO2.setStatus("2");
			ttScsjyydjPO2.setTime2(new Date());//经销商上报时间
			ttScsjyydjPO2.setUpdateBy(logonUser.getUserId());

			dao.update(ttScsjyydjPO1, ttScsjyydjPO2);
			act.setOutData("stateMap", stateMap);
            act.setOutData("stateMap1", stateMap1);
            act.setOutData("stateMapByDlr", stateMapByDlr);
			act.setForword(SCSJ_INIT_JSP_DLR);
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "试乘试驾初始化");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 
	 * @Title :省份or大区转经销商
	 * @param :
	 * @return :
	 * @throws : LastDate : 2014-5-22
	 */
	public void mod4() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		try {
			String ksId = CommonUtils.checkNull(request.getParamValue("ksID"));
			String dlrId = CommonUtils.checkNull(request.getParamValue("dlrId"));
			TtScsjyydjPO ttScsjyydjPO1 = new TtScsjyydjPO();
			TtScsjyydjPO ttScsjyydjPO2 = new TtScsjyydjPO();
			ttScsjyydjPO1.setKsid(ksId);
			ttScsjyydjPO2.setStatus("1");
			ttScsjyydjPO2.setDealerId(Long.valueOf(dlrId));
			ttScsjyydjPO2.setOrgCode("");
			ttScsjyydjPO2.setRootOrgId(0L);
			ttScsjyydjPO2.setIsAllocation(Constant.IF_TYPE_YES);
			ttScsjyydjPO2.setTime3(new Date());//分配时间
			ttScsjyydjPO2.setUpdateBy(logonUser.getUserId());
			dao.update(ttScsjyydjPO1, ttScsjyydjPO2);
			act.setOutData("stateMap", stateMap);
            act.setOutData("stateMap1", stateMap1);
            act.setOutData("stateMapByDlr", stateMapByDlr);
			act.setForword(SCSJ_INIT_JSP_FAC);
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "试乘试驾初始化");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 
	 * @Title : 查询未处理试乘试驾信息
	 * @param :
	 * @return :
	 * @throws : LastDate : 2014-5-22
	 */
	public void scsjManagerQuery() {
		ActionContext act = ActionContext.getContext();
		RequestWrapper request = act.getRequest();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1; // 处理当前页
			String dealerId = logonUser.getDealerId();
			String beginTime = CommonUtils.checkNull(request.getParamValue("beginTime"));
			String fromInfo = CommonUtils.checkNull(request.getParamValue("fromInfo"));
			String endTime = CommonUtils.checkNull(request.getParamValue("endTime"));
			String cmd = CommonUtils.checkNull(request.getParamValue("cmd"));
			PageResult<Map<String, Object>> ps = new PageResult<Map<String,Object>>();
			if(cmd.equals("1")){
				ApplicationDao application = new ApplicationDao();
				List<Map<String, Object>> mapList;
				if(dealerId!=null){
					 //经销商
					 mapList = dao.queryScsjInfoByDownLoad(fromInfo,dealerId,beginTime,endTime,Constant.PAGE_SIZE_MAX, curPage).getRecords();
					 String[] head={"操作","品牌","车型","意向购车时间","客户名称","客户性别","客户年龄","联系方式","信息来源","省份","市","区县","预约时间","是否分配","分配时间","是否滞后","经销商处理时间","处理人"};
						List<Map<String, Object>> records = mapList;
						List params=new ArrayList();
						if(records!=null &&records.size()>0){
							for (Map<String, Object> map1 : records) {
								String CAOZUO = BaseUtils.checkNull(map1.get("CAOZUO"));
								String PPNAME = BaseUtils.checkNull(map1.get("PPNAME"));
//								String CXMC = BaseUtils.checkNull(map1.get("CXMC"));
								String MOTONAME = BaseUtils.checkNull(map1.get("MOTONAME"));
								String WANADATE = BaseUtils.checkNull(map1.get("WANADATE"));
								String CUSNAME = BaseUtils.checkNull(map1.get("CUSNAME"));
								String SEX = BaseUtils.checkNull(map1.get("SEX"));
								String AGE = BaseUtils.checkNull(map1.get("AGE"));
								String CONTACT = BaseUtils.checkNull(map1.get("CONTACT"));
								String FROMINFO = BaseUtils.checkNull(map1.get("FROMINFO"));
								String PROVINCE = BaseUtils.checkNull(map1.get("PROVINCE"));
								String CITY = BaseUtils.checkNull(map1.get("CITY"));
								String TOWN = BaseUtils.checkNull(map1.get("TOWN"));
								String TIME1 = BaseUtils.checkNull(map1.get("TIME1"));
								String IS_ALLOCATION = BaseUtils.checkNull(map1.get("IS_ALLOCATION"));
								String TIME3 = BaseUtils.checkNull(map1.get("TIME3"));
								String IS_DELAY = BaseUtils.checkNull(map1.get("IS_DELAY"));
								String TIME2 = BaseUtils.checkNull(map1.get("TIME2"));
								String UPDATE_BY_NAME = BaseUtils.checkNull(map1.get("UPDATE_BY_NAME"));
								String[] detail={CAOZUO,
										PPNAME,MOTONAME,
										WANADATE,CUSNAME,SEX,AGE,CONTACT,FROMINFO,PROVINCE,CITY,TOWN,TIME1,IS_ALLOCATION,TIME3,IS_DELAY,TIME2,UPDATE_BY_NAME};
								params.add(detail);
							}
						}
						application.toExcel(act, head, params,null,"试乘试驾信息导出");
				}else{
					 //车厂
					 mapList = dao.queryScsjInfoForFacByDownload(fromInfo,beginTime,endTime,Constant.PAGE_SIZE_MAX, curPage,logonUser).getRecords();
					 String[] head={"操作","状态","经销商代码","经销商名称","品牌","车型","意向购车时间","客户名称","客户性别","客户年龄","联系方式","信息来源","省份","市","区县","预约时间","是否分配","分配时间","是否滞后","经销商处理时间","处理人"};
						List<Map<String, Object>> records = mapList;
						List params=new ArrayList();
						if(records!=null &&records.size()>0){
							for (Map<String, Object> map1 : records) {
								String CAOZUO = BaseUtils.checkNull(map1.get("CAOZUO"));
								String STATUS = BaseUtils.checkNull(map1.get("STATUS"));
								String DEALER_CODE = BaseUtils.checkNull(map1.get("DEALER_CODE"));
								String DEALER_NAME = BaseUtils.checkNull(map1.get("DEALER_NAME"));
								String PPNAME = BaseUtils.checkNull(map1.get("PPNAME"));
//								String CXMC = BaseUtils.checkNull(map1.get("CXMC"));
								String MOTONAME = BaseUtils.checkNull(map1.get("MOTONAME"));
								String WANADATE = BaseUtils.checkNull(map1.get("WANADATE"));
								String CUSNAME = BaseUtils.checkNull(map1.get("CUSNAME"));
								String SEX = BaseUtils.checkNull(map1.get("SEX"));
								String AGE = BaseUtils.checkNull(map1.get("AGE"));
								String CONTACT = BaseUtils.checkNull(map1.get("CONTACT"));
								String FROMINFO = BaseUtils.checkNull(map1.get("FROMINFO"));
								String PROVINCE = BaseUtils.checkNull(map1.get("PROVINCE"));
								String CITY = BaseUtils.checkNull(map1.get("CITY"));
								String TOWN = BaseUtils.checkNull(map1.get("TOWN"));
								String TIME1 = BaseUtils.checkNull(map1.get("TIME1"));
								String IS_ALLOCATION = BaseUtils.checkNull(map1.get("IS_ALLOCATION"));
								String TIME3 = BaseUtils.checkNull(map1.get("TIME3"));
								String IS_DELAY = BaseUtils.checkNull(map1.get("IS_DELAY"));
								String TIME2 = BaseUtils.checkNull(map1.get("TIME2"));
								String UPDATE_BY_NAME = BaseUtils.checkNull(map1.get("UPDATE_BY_NAME"));
								String[] detail={CAOZUO,STATUS,DEALER_CODE,DEALER_NAME,
										PPNAME,MOTONAME,
										WANADATE,CUSNAME,SEX,AGE,CONTACT,FROMINFO,PROVINCE,CITY,TOWN,TIME1,IS_ALLOCATION,TIME3,IS_DELAY,TIME2,UPDATE_BY_NAME};
								params.add(detail);
							}
						}
						application.toExcel(act, head, params,null,"试乘试驾信息导出");
				}
			}else{
				if(dealerId!=null){
					//经销商
					ps = dao.queryScsjInfo(fromInfo,dealerId,beginTime,endTime,Constant.PAGE_SIZE, curPage);
				}else{
					//车厂
					ps = dao.queryScsjInfoForFac(fromInfo,beginTime,endTime,Constant.PAGE_SIZE, curPage,logonUser);
				}
				
				act.setOutData("ps", ps);
			}
		} catch (Exception e) {// 异常方法
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "查询试乘试驾信息失败");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	public void selectDlr() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			String ksId = request.getParamValue("ksId");
			act.setOutData("ksId", ksId);
			act.setForword(SCSJ_SHOWDLR);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "经销商查询失败");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}

	public void queryDlr() {
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			
			RequestWrapper request = act.getRequest();
			
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1; // 处理当前页
			Integer pageSize = 10;

		
			PageResult<Map<String, Object>> ps = dao.queryDlr(pageSize, curPage);
			String ksId = request.getParamValue("ksId");
			act.setOutData("ksId", ksId);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "经销商查询失败");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
}
