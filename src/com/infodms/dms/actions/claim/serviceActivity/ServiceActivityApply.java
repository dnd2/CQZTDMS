package com.infodms.dms.actions.claim.serviceActivity;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.DateUtil;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.common.tag.BaseAction;
import com.infodms.dms.dao.claim.serviceActivity.ServiceActivityApplyDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TtServiceActivityApplyPO;
import com.infodms.dms.po.TtServiceActivityRecordPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.po3.bean.PageResult;

/**
 * Function       :  服务活动
 * @author        :  pb
 * CreateDate     :  2017-7-18
 * @version       :  0.1
 */
public class ServiceActivityApply extends BaseAction{
	private Logger logger = Logger.getLogger(ServiceActivityApply.class);
	private ActionContext act = ActionContext.getContext();//获取ActionContext
	private AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
	//申请查询页面
	private final String ServiceActivityInitUrl = "/jsp/claim/serviceActivity/serviceActivityApplyQuery.jsp";
	//审核查询页面
	private final String ServiceActivityAuditQueryUrl = "/jsp/claim/serviceActivity/serviceActivityAudit.jsp";
	
	//审核查询页面
	private final String ServiceActivityQueryUrl = "/jsp/claim/serviceActivity/serviceActivityQuery.jsp";
	//明细页面
	private final String ServiceActivityDetailUrl = "/jsp/claim/serviceActivity/serviceActivityApplyDetail.jsp";
	
	//审核页面
	private final String ServiceActivityAuditUrl = "/jsp/claim/serviceActivity/serviceActivityApplyAdt.jsp";
			
	//新增页面
	private final String ServiceActivityAddUrl = "/jsp/claim/serviceActivity/serviceActivityAdd.jsp";
	
	private ServiceActivityApplyDao dao=ServiceActivityApplyDao.getInstance();
	
	/**
	 * Function       :  服务活动申请页面初始化
	 * @param         :  null
	 * @return        :  getServiceActivityApply
	 * @throws        :  Exception
	 * LastUpdate     :  2017-7-18
	 */
	public void getServiceActivityApply(){
		try {
			act.setForword(ServiceActivityInitUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"服务活动申请页面初始化");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * Function       :  服务活动查询页面初始化
	 * @param         :  null
	 * @return        :  getServiceActivityQuery
	 * @throws        :  Exception
	 * LastUpdate     :  2017-7-18
	 */
	public void getServiceActivityQuery(){
		try {
			act.setForword(ServiceActivityQueryUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"服务活动查询页面初始化");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * Function       :  服务活动新增页面跳转
	 * @param         :  null
	 * @return        :  serviceActivityApplyAddInit
	 * @throws        :  Exception
	 * LastUpdate     :  2017-7-18
	 */
	public void serviceActivityApplyAddInit(){
		try{
			Map<String, Object> map=dao.queryDealerInfo(logonUser.getDealerId());
			//经销商基础信息
			act.setOutData("flag", "1");
			act.setOutData("map", map);
			act.setForword(ServiceActivityAddUrl);
		}catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ACTION_NAME_ERROR_CODE,"服务活动新增页面跳转");
			logger.error(logger,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * Function       :  服务活动修改页面跳转
	 * @param         :  null
	 * @return        :  serviceActivityApplyUpdateInit
	 * @throws        :  Exception
	 * LastUpdate     :  2017-7-18
	 */
	public void serviceActivityApplyUpdateInit(){
		try{
			String id = CommonUtils.checkNull(request.getParamValue("activityId"));  
			Map<String, Object> map=dao.queryserviceActivityInfo(id);
			List<Map<String, Object>> list=dao.queryApplyRecord(Long.parseLong(id));
			if(list.size()>0&&list!=null){
				act.setOutData("flag", "0");
			}else{
				act.setOutData("flag", "1");
			}
			act.setOutData("map", map);
			act.setOutData("applyRecordList", list);
			act.setForword(ServiceActivityAddUrl);
		}catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ACTION_NAME_ERROR_CODE,"服务活动修改页面跳转");
			logger.error(logger,e1);
			act.setException(e1);
		}
	}
	/**
	 * Function       :  服务活动审核页面跳转
	 * @param         :  null
	 * @return        :  serviceActivityApplyAuditInit
	 * @throws        :  Exception
	 * LastUpdate     :  2017-7-18
	 */
	public void serviceActivityApplyAuditInit(){
		try{
			String id = CommonUtils.checkNull(request.getParamValue("activityId"));  
			Map<String, Object> map=dao.queryserviceActivityInfo(id);
			act.setOutData("applyRecordList", dao.queryApplyRecord(Long.parseLong(id)));
			act.setOutData("map", map);
			act.setForword(ServiceActivityAuditUrl);
		}catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ACTION_NAME_ERROR_CODE,"服务活动审核页面跳转");
			logger.error(logger,e1);
			act.setException(e1);
		}
	}	
	
	
	/**
	 * Function       :  服务活动明细查询
	 * @param         :  null
	 * @return        :  serviceActivityApplyUpdateInit
	 * @throws        :  Exception
	 * LastUpdate     :  2017-7-18
	 */
	public void serviceActivityApplySearchInit(){
		try{
			String id = CommonUtils.checkNull(request.getParamValue("activityId"));  
			Map<String, Object> map=dao.queryserviceActivityInfo(id);
			List<Map<String, Object>> list=dao.queryApplyRecord(Long.parseLong(id));
			if(list.size()>0&&list!=null){
				act.setOutData("flag", "0");
			}else{
				act.setOutData("flag", "1");
			}
			act.setOutData("map", map);
			act.setOutData("applyRecordList", list);
			act.setForword(ServiceActivityDetailUrl);
		}catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.DETAIL_FAILURE_CODE,"服务活动明细查询");
			logger.error(logger,e1);
			act.setException(e1);
		}
	}
	/**
	 * Function       :  服务活动申请新增
	 * @param         :  null
	 * @return        :  saveServiceActivityApply
	 * @throws        :  Exception
	 * LastUpdate     :  2017-7-18
	 */
	public void saveServiceActivityApply(){
		try{
			String dealerId = CommonUtils.checkNull(request.getParamValue("dealerId"));  //经销商ID
			String dealerCode = CommonUtils.checkNull(request.getParamValue("dealerCode"));  //经销商代码			
			String dealerName = CommonUtils.checkNull(request.getParamValue("dealerName")); //经销商名称
			String createDate = CommonUtils.checkNull(request.getParamValue("createDate")); //申请时间
			String acceptStart = CommonUtils.checkNull(request.getParamValue("acceptStart"));  //活动开始时间
			String acceptEnd = CommonUtils.checkNull(request.getParamValue("acceptEnd"));  	//结束时间			
			String orgId = CommonUtils.checkNull(request.getParamValue("orgId"));  	//大区ID			
			String orgCode = CommonUtils.checkNull(request.getParamValue("orgCode"));//大区代码
			String orgName = CommonUtils.checkNull(request.getParamValue("orgName"));//大区名称
			String addtext = CommonUtils.checkNull(request.getParamValue("addtext"));  //获得内容
			String id = CommonUtils.checkNull(request.getParamValue("id"));  //id
			TtServiceActivityApplyPO ttpo=new TtServiceActivityApplyPO();
			ttpo.setActivityContent(addtext);
			ttpo.setDealerId(Long.valueOf(dealerId));
			ttpo.setDealerCode(dealerCode);
			ttpo.setDealerName(dealerName);
			ttpo.setOrgId(Long.valueOf(orgId));
			ttpo.setOrgCode(orgCode);
			ttpo.setOrgName(orgName);
			ttpo.setStartDate(DateUtil.str2Date(acceptStart, "-"));
			ttpo.setStatus(Constant.SERVICEACTIVITYAPPLY_STATUS_01);
			ttpo.setEndDate(DateUtil.str2Date(acceptEnd,"-"));
			if("".equals(id)){
				ttpo.setId(Long.valueOf(SequenceManager.getSequence("")));
				ttpo.setCreateBy(logonUser.getUserId());
				ttpo.setCreateDate(new Date());
				ttpo.setApplyNo(Utility.GetApplyNo(dealerCode, "FWSQ"));
				dao.insert(ttpo);
			}else{
				ttpo.setUpdateBy(logonUser.getUserId());
				ttpo.setUpdateDate(new Date());
				TtServiceActivityApplyPO ttpo1=new TtServiceActivityApplyPO();
				ttpo1.setId(Long.valueOf(id));
				dao.update(ttpo1, ttpo);
			}
			act.setOutData("isSuccess", 0);
		}catch(Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.UPDATE_FAILURE_CODE,"服务活动申请新增");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 服务活动申请上报
	 * @param null
	 * @return void
	 * @throws Exception
	 */
	public void reportServiceActivity(){
		try{	
			String id = CommonUtils.checkNull(request.getParamValue("activityId"));  
			TtServiceActivityApplyPO ttpo=new TtServiceActivityApplyPO();
			TtServiceActivityApplyPO ttpo1=new TtServiceActivityApplyPO();
			ttpo.setId(Long.valueOf(id));
			ttpo1.setStatus(Constant.SERVICEACTIVITYAPPLY_STATUS_02);
			dao.update(ttpo, ttpo1);
			act.setOutData("isSuccess", 0);		
		} catch(Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.UPDATE_FAILURE_CODE,"服务活动申请上报");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 服务活动审核
	 * @param null
	 * @return void
	 * @throws Exception
	 */
	public void audittServiceActivity(){
		try{	
			String id = CommonUtils.checkNull(request.getParamValue("activityId")); 
			String auditAdvice = CommonUtils.checkNull(request.getParamValue("auditAdvice")); //审核内容
			String isPass = CommonUtils.checkNull(request.getParamValue("isPass")); //判断操作类型
			int status = 0;
			String message="";
			if("0".equals(isPass)){
				status=Constant.SERVICEACTIVITYAPPLY_STATUS_05;//通过
				message="通过成功!";
			} 
			if("1".equals(isPass)){
				status=Constant.SERVICEACTIVITYAPPLY_STATUS_03;//驳回
				message="驳回成功!";
			} 
			if("2".equals(isPass)){
				status=Constant.SERVICEACTIVITYAPPLY_STATUS_04;//拒绝
				message="拒绝成功!";
			}
			TtServiceActivityApplyPO ttpo=new TtServiceActivityApplyPO();
			TtServiceActivityApplyPO ttpo1=new TtServiceActivityApplyPO();
			ttpo.setId(Long.valueOf(id));
			ttpo1.setStatus(status);
			dao.update(ttpo, ttpo1);
			//审核记录
			TtServiceActivityRecordPO po=new TtServiceActivityRecordPO();
			po.setAcId(Long.valueOf(id));
			po.setAuditContent(auditAdvice);
			po.setId(Long.valueOf(SequenceManager.getSequence("")));
			po.setStatus(status);
			po.setCreateBy(logonUser.getUserId());
			po.setCreateDate(new Date());
			dao.insert(po);
			act.setOutData("isSuccess", 0);
			act.setOutData("message", message);	
		} catch(Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.UPDATE_FAILURE_CODE,"服务活动审核");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	
	/**
	 * 服务活动未上报的删除
	 * @param null
	 * @return void
	 * @throws Exception
	 */
	public void deleteServiceActivity(){
		try{	
			String id = CommonUtils.checkNull(request.getParamValue("activityId"));  
			TtServiceActivityApplyPO ttpo=new TtServiceActivityApplyPO();
			TtServiceActivityApplyPO ttpo1=new TtServiceActivityApplyPO();
			ttpo.setId(Long.valueOf(id));
			ttpo1.setStatus(0);//状态为"0",废数据
			dao.update(ttpo, ttpo1);
			act.setOutData("isSuccess", 0);		
		} catch(Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.UPDATE_FAILURE_CODE,"服务活动未上报的删除");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * Function       :  服务活动审核页面初始化
	 * @param         :  null
	 * @return        :  getServiceActivityApply
	 * @throws        :  Exception
	 * LastUpdate     :  2017-7-18
	 */
	public void getServiceActivityAudit(){
		try {
			act.setForword(ServiceActivityAuditQueryUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"服务活动审核页面初始化");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 服务活动申请查询
	 * @param null
	 * @return void
	 * @throws Exception
	 */
	public void serviceActivityApplyQuery(){
		try{
			String applyNo = CommonUtils.checkNull(request.getParamValue("applyNo"));//单据号  				
			String addStart = CommonUtils.checkNull(request.getParamValue("addStart")); //单据类型 
			String addEnd = CommonUtils.checkNull(request.getParamValue("addEnd")); //单据类型 
			String status = CommonUtils.checkNull(request.getParamValue("status")); //单据状态 				
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("applyNo", applyNo);
			map.put("addStart", addStart);
			map.put("addEnd", addEnd);
			map.put("status", status);
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage"))	: 1; // 处理当前页	
			PageResult<Map<String,Object>> ps = dao.queryServiceActivityApply(map,logonUser.getDealerId(),Constant.PAGE_SIZE,curPage);
			act.setOutData("ps", ps);
		} catch(Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"服务活动申请查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 服务活动审核查询
	 * @param null
	 * @return void
	 * @throws Exception
	 */
	public void serviceActivityAuditQuery(){
		try{
			String dealerCode = CommonUtils.checkNull(request.getParamValue("DealerCode"));//服务站 	
			String applyNo = CommonUtils.checkNull(request.getParamValue("applyNo"));//单据号  				
			String addStart = CommonUtils.checkNull(request.getParamValue("addStart")); //单据类型 
			String addEnd = CommonUtils.checkNull(request.getParamValue("addEnd")); //单据类型 
			String status = CommonUtils.checkNull(request.getParamValue("status")); //单据状态 				
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("applyNo", applyNo);
			map.put("addStart", addStart);
			map.put("addEnd", addEnd);
			map.put("status", status);
			map.put("dealerCode", dealerCode);
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage"))	: 1; // 处理当前页	
			PageResult<Map<String,Object>> ps = dao.queryServiceActivityAudit(map,Constant.PAGE_SIZE,curPage);
			act.setOutData("ps", ps);
		} catch(Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"服务活动申请查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
}
