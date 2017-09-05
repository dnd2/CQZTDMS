/**********************************************************************
* <pre>
* FILE : ServiceActivityManageDealer.java
* CLASS : ServiceActivityManageDealer
*
* AUTHOR : PGM
*
* FUNCTION :服务活动管理---经销商管理
*
*
*======================================================================
* CHANGE HISTORY LOG
*----------------------------------------------------------------------
* MOD. NO.| DATE     | NAME | REASON | CHANGE REQ.
*----------------------------------------------------------------------
*         |2010-06-09| PGM  | Created |
* DESCRIPTION:
* </pre>
***********************************************************************/
/**
 * $Id: ServiceActivityManageDealer.java,v 1.9 2010/12/28 10:15:01 zuoxj Exp $
 */
package com.infodms.dms.actions.claim.serviceActivity;
import java.io.ByteArrayInputStream;
import java.util.Date;
import java.util.List;
import java.util.Map;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.bean.ActivityDealerBean;
import com.infodms.dms.bean.TtAsActivityBean;
import com.infodms.dms.bean.TtAsActivityDealerBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.common.getCompanyId.GetOemcompanyId;
import com.infodms.dms.dao.claim.serviceActivity.ServiceActivityManageDealerDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TmDealerPO;
import com.infodms.dms.po.TmOrgPO;
import com.infodms.dms.po.TtAsActivityDealerPO;
import com.infodms.dms.util.StringUtil;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.FileObject;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;

/**
 * Function       :  服务活动管理---经销商管理
 * @author        :  PGM
 * CreateDate     :  2010-06-09
 * @version       :  0.1
 */
public class ServiceActivityManageDealer {
	private Logger logger = Logger.getLogger(ServiceActivityManageDealer.class);
	private ServiceActivityManageDealerDao dao = ServiceActivityManageDealerDao.getInstance();
	private ActionContext act = ActionContext.getContext();//获取ActionContext
	private AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
	private final String ServiceActivityInitUrl = "/jsp/claim/serviceActivity/serviceActivityManageDealer.jsp";//查询页面
	private final String ServiceActivityInitUrl2 = "/jsp/claim/serviceActivity/serviceActivityManageDealer2.jsp";//查询页面--ver 2
	private final String ServiceActivityMaintionInitUrl = "/jsp/claim/serviceActivity/serviceActivityManageDealerMaintion.jsp";//查询参与本次活动的经销商
	private final String ServiceActivityNoExitsInitUrl = "/jsp/claim/serviceActivity/serviceActivityManageDealerSelect.jsp";//查询未参与本次活动的经销商
	private final String AREA_MODIFY_URL = "/jsp/claim/serviceActivity/serviceActivityManageAreaModify.jsp" ;//区域维护页面
	private final String AREA_SELECT_URL = "/jsp/claim/serviceActivity/serviceActivityManageAreaSel.jsp" ;//区域选择页面

	/**
	 * Function       :  服务活动管理---经销商管理页面初始化
	 * @param         :  
	 * @return        :  serviceActivityManageDealerInit
	 * @throws        :  Exception
	 * LastUpdate     :  2010-06-09
	 */
	public void serviceActivityManageDealerInit(){
		try {
			act.setOutData("type", 0);
			act.setForword(ServiceActivityInitUrl2);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"服务活动管理---经销商管理页面初始化");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	public void serviceActivityManageDealerFWCInit(){
		try {
			act.setOutData("type", 1);
			act.setForword(ServiceActivityInitUrl2);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"服务活动管理---经销商管理页面初始化");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	
	/**
	 * Function       :  根据条件查询服务活动管理---经销商管理中符合条件的信息
	 * @param         :  request-活动编号、活动开始日期、活动结束日期
	 * @return        :  服务活动管理
	 * @throws        :  Exception
	 * LastUpdate     :  2010-06-09
	 */
	public void serviceActivityManageDealerQuery(){
		try {
			RequestWrapper request = act.getRequest();
			String activityCode = request.getParamValue("activityCode");    //活动编号
			String startdate = request.getParamValue("startDate");         //活动开始日期
			String enddate = request.getParamValue("endDate");            //活动结束日期
			Long companyId=GetOemcompanyId.getOemCompanyId(logonUser);      //公司ID
			TtAsActivityBean ActivityBean = new TtAsActivityBean();
			ActivityBean.setActivityCode(activityCode);
			ActivityBean.setStartdate(startdate);
			ActivityBean.setEnddate(enddate);
			ActivityBean.setCompanyId(String.valueOf(companyId));
			Integer curPage = request.getParamValue("curPage") !=null ? Integer.parseInt(request.getParamValue("curPage")) : 1;
			PageResult<Map<String, Object>> ps = dao.getServiceActivityManageDealerQuery(ActivityBean,curPage,Constant.PAGE_SIZE );
			act.setOutData("ps", ps);
			act.setForword(ServiceActivityInitUrl);
		}catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"服务活动管理---经销商管理");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * Function       :  根据条件查询服务活动管理---查询参与本次活动的经销商
	 * @param         :  request-活动ID
	 * @return        :  服务活动管理
	 * @throws        :  Exception
	 * LastUpdate     :  2010-06-09
	 */
	public void serviceActivityManageDealerMaintionQuery(){
		try {
			RequestWrapper request = act.getRequest();
			String activityId = (String) (request.getParamValue("activityId")==null?request.getAttribute("activityId"):request.getParamValue("activityId"));    //活动ID
			TtAsActivityDealerBean DealerPO = new TtAsActivityDealerBean();
			if(!"".equals(activityId)&&null!=activityId){//活动ID不为空
			DealerPO.setActivityId(Long.parseLong(activityId));
			}
			Integer curPage = request.getParamValue("curPage") !=null ? Integer.parseInt(request.getParamValue("curPage")) : 1;
			PageResult<Map<String, Object>> ps = dao.getserviceActivityManageDealerMaintionQuery(DealerPO,curPage,Constant.PAGE_SIZE );
			act.setOutData("ps", ps);
			request.setAttribute("activityId", activityId);
			act.setOutData("type", request.getParamValue("type"));
			act.setForword(ServiceActivityMaintionInitUrl);
		}catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"服务活动管理---经销商管理");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * Function       :  删除服务活动管理---经销商管理
	 * @param         :  request-活动ID、经销商ID
	 * @return        :  经销商管理
	 * @throws        :  ParseException
	 * LastUpdate     :  2010-06-09
	 */
	@SuppressWarnings("static-access")
	public void serviceActivityManageDealerDelete(){ 
		RequestWrapper request = act.getRequest();
		String dealerId=request.getParamValue("dealerId");          //DealerId
		String activityId = request.getParamValue("activityId");    //活动ID
		try{
			if (dealerId!=null&&!"".equals(dealerId)) {
				String [] dealerIdArray = dealerId.split(","); //取得所有orderId放在数组中
				Long updateBy=logonUser.getUserId();
				dao.serviceActivityManageDealerDelete(dealerIdArray,activityId);
				//dao.serviceActivityManageDealerUpdate(dealerIdArray,activityId,updateBy);
			}
			request.setAttribute("activityId", activityId);
			act.setOutData("returnValue", 1);//returnValue 值：1，表示成功
		  }catch(Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.DELETE_FAILURE_CODE,"服务活动管理---经销商管理");
			logger.error(logonUser,e1);
		 	act.setException(e1);
		  }
	}
	/**
	 * Function       :  服务活动管理---经销商管理(查询未参加的活动)
	 * @param         :  request-活动ID、经销商代码、经销商名称、区域ID
	 * @return        :  服务活动管理---经销商管理信息
	 * @throws        :  Exception
	 * LastUpdate     :  2010-06-09
	 */
	@SuppressWarnings("unchecked")
	public void serviceActivityManageDealerNoExitsQuery(){
		RequestWrapper request = act.getRequest();
		String activityId = request.getParamValue("activityId");     //活动ID
		String dealerCode = request.getParamValue("dealerCodes");    //经销商代码
		String dealerName = request.getParamValue("dealerNames");    //经销商名称
		String orgId = request.getParamValue("orgId");               //区域ID
		String regionName = request.getParamValue("regionName");     //省份
		
		try{
				List<TtAsActivityDealerPO> querylist=dao.serviceActivityManageDealerNoExitsQuery(activityId);//服务活动管理---经销商管理(查询经销商代码)
				List<TmOrgPO> listSelected=dao.serviceActivityManageAreaSelected();//下拉列表查询
				Integer curPage = request.getParamValue("curPage") !=null ? Integer.parseInt(request.getParamValue("curPage")) : 1;
				PageResult<Map<String, Object>> ps = dao.serviceActivityManageDealerNoQuery(querylist,dealerCode,activityId,dealerName,orgId,regionName,curPage,10);
				act.setOutData("ps", ps);
				request.setAttribute("orgIdName", orgId);//下拉列表回显
				request.setAttribute("listSelected", listSelected);
				request.setAttribute("activityId",activityId);
				act.setForword(ServiceActivityNoExitsInitUrl);
		  }catch(Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.DELETE_FAILURE_CODE,"服务活动管理---经销商管理信息");
			logger.error(logonUser,e1);
		 	act.setException(e1);
		  }
	}
	/**
	 * Function       :  增加服务活动管理---经销商信息
	 * @param         :  request---活动ID、groupId、经销商代码、经销商名称
	 * @return        :  服务活动管理
	 * @throws        :  Exception
	 * LastUpdate     :  2010-06-09
	 */
	@SuppressWarnings("static-access")
	public void serviceActivityManageDealerOption(){
		try {
			RequestWrapper request = act.getRequest();
			String activityId =request.getParamValue("activityId");         //活动ID
			String groupIds =request.getParamValue("groupIds");            //dealerId
			String dealerCode=request.getParamValue("dealerCode");         //经销商代码
			String dealerName=request.getParamValue("dealerName");         //经销商名称
			String [] dealerCodeArray=null;
			String [] dealerNameArray=null;
			if (groupIds!=null&&!"".equals(groupIds)) {
				String [] groupIdsArray = groupIds.split(",");    //取得所有groupIds放在数组中
				if(null!=dealerCode&&!"".equals(dealerCode)){
				 dealerCodeArray =dealerCode.split(",");          //取得所有dealerCodeArray放在数组中
				}
				if(null!=dealerCode&&!"".equals(dealerCode)){
				 dealerNameArray =dealerName.split(",");          //取得所有dealerNameArray放在数组中
				}
				TtAsActivityDealerPO DealerPO=new TtAsActivityDealerPO();
				DealerPO.setActivityId(Long.parseLong(activityId));
				DealerPO.setCreateBy(logonUser.getUserId());
				DealerPO.setCreateDate(new Date());
				DealerPO.setUpdateBy(logonUser.getUserId());
				DealerPO.setUpdateDate(new Date());
				DealerPO.setStatus(Constant.SUMMARY_STATUS_02);//服务活动评估状态:未评估
				dao.serviceActivityManageDealerOption(groupIdsArray,dealerCodeArray,dealerNameArray,DealerPO);
				request.setAttribute("activityId",activityId);
				act.setOutData("returnValue", 1);
			}
		}catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"服务活动管理车型列表信息增加");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}

	public void serviceActivityManageDealerUpload()
	{
		try {
			RequestWrapper request = act.getRequest();
			String activityId =request.getParamValue("activityId");         //活动ID
			FileObject uploadFile = request.getParamObject("uploadFile");
			ByteArrayInputStream is = new ByteArrayInputStream(uploadFile.getContent());
			Workbook wb = Workbook.getWorkbook(is);
			Sheet[] sheets = wb.getSheets();
			Sheet sheet = sheets[0];
			int totalRows = sheet.getRows();
			for(int j = 1; j < totalRows; j++)
			{
				Cell[] cells = sheet.getRow(j);
				String dealerCode= cells[0].getContents().trim();
				TmDealerPO dealerPO = new TmDealerPO();
				dealerPO.setDealerCode(dealerCode);
				List<TmDealerPO> list= dao.select(dealerPO);
				if(list.size() > 0)
				{
					TtAsActivityDealerPO DealerPO1=new TtAsActivityDealerPO();
					DealerPO1.setActivityId(Long.parseLong(activityId));
					DealerPO1.setDealerId(list.get(0).getDealerId());
					List<TtAsActivityDealerPO>  listd= dao.select(DealerPO1);
					if(listd.size() == 0)
					{
						TtAsActivityDealerPO DealerPO=new TtAsActivityDealerPO();
						DealerPO.setActivityId(Long.parseLong(activityId));
						DealerPO.setDealerId(list.get(0).getDealerId());
						DealerPO.setAdId(Long.parseLong(SequenceManager.getSequence("")));
						DealerPO.setCreateBy(logonUser.getUserId());
						DealerPO.setCreateDate(new Date());
						DealerPO.setUpdateBy(logonUser.getUserId());
						DealerPO.setUpdateDate(new Date());
						DealerPO.setStatus(Constant.SUMMARY_STATUS_02);
						DealerPO.setDealerCode(dealerCode);
						DealerPO.setDealerName(list.get(0).getDealerName());
						dao.insert(DealerPO);
					}
					
				}
			}
			serviceActivityManageDealerMaintionQuery1(activityId);
			
		}catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"服务活动管理车型列表信息增加");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	public void serviceActivityManageDealerUploadAll()
	{
		try {
			RequestWrapper request = act.getRequest();
			String[] ACTIVITY_ID =request.getParamValues("ACTIVITY_ID");         //活动ID
			FileObject uploadFile = request.getParamObject("uploadFile");
			String type= request.getParamValue("type");
			ByteArrayInputStream is = new ByteArrayInputStream(uploadFile.getContent());
			Workbook wb = Workbook.getWorkbook(is);
			Sheet[] sheets = wb.getSheets();
			Sheet sheet = sheets[0];
			int totalRows = sheet.getRows();
			for(String activityId : ACTIVITY_ID)
			{
				for(int j = 1; j < totalRows; j++)
				{
					Cell[] cells = sheet.getRow(j);
					String dealerCode= cells[0].getContents().trim();
					TmDealerPO dealerPO = new TmDealerPO();
					dealerPO.setDealerCode(dealerCode);
					List<TmDealerPO> list= dao.select(dealerPO);
					if(list.size() > 0)
					{
						TtAsActivityDealerPO DealerPO1=new TtAsActivityDealerPO();
						DealerPO1.setActivityId(Long.parseLong(activityId));
						DealerPO1.setDealerId(list.get(0).getDealerId());
						List<TtAsActivityDealerPO>  listd= dao.select(DealerPO1);
						if(listd.size() == 0)
						{
							TtAsActivityDealerPO DealerPO=new TtAsActivityDealerPO();
							DealerPO.setActivityId(Long.parseLong(activityId));
							DealerPO.setDealerId(list.get(0).getDealerId());
							DealerPO.setAdId(Long.parseLong(SequenceManager.getSequence("")));
							DealerPO.setCreateBy(logonUser.getUserId());
							DealerPO.setCreateDate(new Date());
							DealerPO.setUpdateBy(logonUser.getUserId());
							DealerPO.setUpdateDate(new Date());
							DealerPO.setStatus(Constant.SUMMARY_STATUS_02);
							DealerPO.setDealerCode(dealerCode);
							DealerPO.setDealerName(list.get(0).getDealerName());
							dao.insert(DealerPO);
						}
						
					}
				}
			
			}
			act.setOutData("type", type);
			act.setForword(ServiceActivityInitUrl2);
			
		}catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"服务活动管理车型列表信息增加");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	
	
	public void serviceActivityManageDealerMaintionQuery1(String activityId){
		try {
			RequestWrapper request = act.getRequest();
			TtAsActivityDealerBean DealerPO = new TtAsActivityDealerBean();
			if(!"".equals(activityId)&&null!=activityId){//活动ID不为空
			DealerPO.setActivityId(Long.parseLong(activityId));
			}
			Integer curPage = request.getParamValue("curPage") !=null ? Integer.parseInt(request.getParamValue("curPage")) : 1;
			PageResult<Map<String, Object>> ps = dao.getserviceActivityManageDealerMaintionQuery(DealerPO,curPage,Constant.PAGE_SIZE );
			act.setOutData("ps", ps);
			request.setAttribute("activityId", activityId);
			act.setOutData("type", request.getParamValue("type"));
			act.setForword(ServiceActivityMaintionInitUrl);
		}catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"服务活动管理---经销商管理");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/*
	 * 主页面第一次查询
	 */
	public void mainQuery(){
		RequestWrapper req = act.getRequest() ;
		try{
			act.getResponse().setContentType("application/json");
			String activityCode = req.getParamValue("activityCode");
			String activityName = req.getParamValue("activityName");
			String subjiectNO = req.getParamValue("subjiectNO");
			String subjiectName = req.getParamValue("subjiectName");
			String startDate = req.getParamValue("startDate");
			String endDate = req.getParamValue("endDate");
			String dealerId = req.getParamValue("dealer_id");
			String orgCode = req.getParamValue("area_code");
			String flag = req.getParamValue("flag");
			String type = req.getParamValue("type");
			StringBuffer con = new StringBuffer();
			if(StringUtil.notNull(activityCode))
				con.append(" and x.activity_code like '%").append(activityCode).append("%'\n");
			
			if(StringUtil.notNull(subjiectNO))
				con.append(" and z.SUBJECT_NO like '%").append(subjiectNO).append("%'\n");
			if(StringUtil.notNull(subjiectName))
				con.append(" and z.SUBJECT_NAME like '%").append(subjiectName).append("%'\n");
			
			if(StringUtil.notNull(activityName))
				con.append(" and x.activity_name like '%").append(activityName).append("%'\n");
			if(StringUtil.notNull(startDate)){
				con.append(" and x.startdate<=to_date('").append(startDate).append(" 23:59:59','yyyy-MM-dd HH24:mi:ss')\n");
				con.append(" and x.enddate>=to_date('").append(startDate).append(" 00:00:00','yyyy-MM-dd HH24:mi:ss')\n");
			}
			if(StringUtil.notNull(endDate)){
				con.append(" and x.startdate<=to_date('").append(endDate).append(" 23:59:59','yyyy-MM-dd HH24:mi:ss')\n");
				con.append(" and x.enddate>=to_date('").append(endDate).append(" 00:00:00','yyyy-MM-dd HH24:mi:ss')\n");
			}
			if("1".equals(flag)){
				if(StringUtil.notNull(orgCode)){
					con.append(" and y.area_id in ( select org_id from tm_org where org_code in ('").append(orgCode.replaceAll(",","','")).append("'))\n");
				}
			}
			if("2".equals(flag)){
				if(StringUtil.notNull(dealerId))
					con.append(" and y.dealer_id in (").append(dealerId).append(")\n");
			}
			/*if(type.equals("0")){ 2014-5-4 by zyw
				con.append(" and z.ACTIVITY_TYPE  = 10561001\n");
			}else
			{
				con.append(" and z.ACTIVITY_TYPE  != 10561001 \n");
			}*/
			int pageSize = 15;
			Integer curPage = req.getParamValue("curPage") !=null ? Integer.parseInt(req.getParamValue("curPage")) : 1;
			PageResult<Map<String,Object>> pageResult = dao.mainQUery(con.toString(), pageSize, curPage);
			act.setOutData("ps", pageResult);
		} catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"服务活动管理主查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/*
	 * 区域维护前置操作
	 */
	public void areaModify(){
		RequestWrapper req = act.getRequest() ;
		try{
			String activityId = req.getParamValue("activityId");
			List<ActivityDealerBean> list = dao.getAreaList(activityId);
			act.setOutData("type",req.getParamValue("type"));
			act.setOutData("list",list);
			req.setAttribute("activityId", activityId);
			act.setForword(AREA_MODIFY_URL);
		} catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"服务活动管理区域维护");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/*
	 * 区域选择页面初始化
	 */
	public void areaSelUrlInit(){
		try{
			act.setForword(AREA_SELECT_URL);
		}catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"服务活动管理区域维护");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/*
	 * 区域弹出框查询方法
	 */
	public void areaQuery(){
		RequestWrapper req = act.getRequest() ;
		try{
			String code = req.getParamValue("code");
			String name = req.getParamValue("name");
			StringBuffer con = new StringBuffer();
			if(StringUtil.notNull(code))
				con.append("and org_code like '%").append(code).append("%'\n");
			if(StringUtil.notNull(name))
				con.append("and org_name like '%").append(name).append("%'\n");
			con.append("and duty_type=").append(Constant.DUTY_TYPE_LARGEREGION).append("\n");
			PageResult<TmOrgPO> ps = dao.getOrg(con.toString(),1000,1) ;
			act.setOutData("ps", ps);
		}catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"服务活动管理区域查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/*
	 * 服务活动区域维护操作
	 */
	public void areaSure(){
		RequestWrapper req = act.getRequest() ;
		try{
			String activityId = req.getParamValue("activityId");
			String[] orgIds = req.getParamValues("org_id");
			List<ActivityDealerBean> list = dao.getAreaList(activityId);
			if(orgIds!=null){
				for(int i = 0;i<orgIds.length;i++){
					//查询此大区下面的所有经销商
					List<TmDealerPO> dealerList = dao.getDealerFromORG(orgIds[i]) ; 
					TtAsActivityDealerPO po = null ;
					for(int j=0;j<dealerList.size();j++){
						//判断数据库中是否存在此经销商
						po = new TtAsActivityDealerPO();
						po.setActivityId(Long.parseLong(activityId));
						po.setDealerId(dealerList.get(j).getDealerId());
						//po.setAreaId(Long.parseLong(orgIds[i]));
						if(dao.select(po).size()==0){
							po.setCreateBy(logonUser.getUserId());
							po.setCreateDate(new Date());
							po.setDealerCode(dealerList.get(j).getDealerCode());
							po.setDealerName(dealerList.get(j).getDealerName());
							po.setAdId(Utility.getLong(SequenceManager.getSequence("")));
							po.setStatus(Constant.SUMMARY_STATUS_02);
							po.setAreaId(Long.parseLong(orgIds[i]));
							dao.insert(po);
						}
					}
				}
			}
			if(req.getParamValue("type").equals("0"))
			{
				serviceActivityManageDealerInit();
				
			}else
			{
				serviceActivityManageDealerFWCInit();
			}
		}catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ADD_FAILURE_CODE,"服务活动管理区域添加");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/*
	 * 删除所选区域下的所有经销商
	 */
	public void deleteArea(){
		RequestWrapper req = act.getRequest();
		try{
			String orgId = req.getParamValue("orgId");
			String activityId = req.getParamValue("activityId");
			TtAsActivityDealerPO po = new TtAsActivityDealerPO();
			po.setAreaId(Long.parseLong(orgId));
			po.setActivityId(Long.parseLong(activityId));
			dao.delete(po);
			act.setOutData("idx", req.getParamValue("idx"));
			act.setOutData("flag", true);
		}catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.DELETE_FAILURE_CODE,"服务活动管理区域维护");
			logger.error(logonUser,e1);
			act.setException(e1);
			act.setOutData("flag", false);
		}
	}
}