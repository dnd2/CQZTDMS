/**********************************************************************
* <pre>
* FILE : ServiceActivityManage.java
* CLASS : ServiceActivityManage
*
* AUTHOR : PGM
*
* FUNCTION :服务活动管理.
*
*
*======================================================================
* CHANGE HISTORY LOG
*----------------------------------------------------------------------
* MOD. NO.| DATE     | NAME | REASON | CHANGE REQ.
*----------------------------------------------------------------------
*         |2010-05-17| PGM  | Created |
* DESCRIPTION:
* </pre>
***********************************************************************/
/**
 * $Id: ServiceActivityManage.java,v 1.18 2012/11/21 08:25:32 zmw Exp $
 */
package com.infodms.dms.actions.claim.serviceActivity;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.actions.common.FileUploadManager;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.bean.TtAsActivityBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.common.getCompanyId.GetOemcompanyId;
import com.infodms.dms.common.tag.BaseAction;
import com.infodms.dms.dao.claim.dealerClaimMng.ClaimBillMaintainDAO;
import com.infodms.dms.dao.claim.other.BonusDAO;
import com.infodms.dms.dao.claim.serviceActivity.ServiceActivityManageDao;
import com.infodms.dms.dao.claim.serviceActivity.ServiceActivityManageItemDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TmBusinessAreaPO;
import com.infodms.dms.po.TmFineNewsPO;
import com.infodms.dms.po.TmPtPartBasePO;
import com.infodms.dms.po.TtAsActivityAgePO;
import com.infodms.dms.po.TtAsActivityCharactorPO;
import com.infodms.dms.po.TtAsActivityDealerPO;
import com.infodms.dms.po.TtAsActivityMgroupPO;
import com.infodms.dms.po.TtAsActivityMilagePO;
import com.infodms.dms.po.TtAsActivityPO;
import com.infodms.dms.po.TtAsActivityProjectPO;
import com.infodms.dms.po.TtAsActivityRelationPO;
import com.infodms.dms.po.TtAsActivitySubjectPO;
import com.infodms.dms.po.TtAsActivityVehiclePO;
import com.infodms.dms.po.TtAsActivityYieldlyPO;
import com.infodms.dms.po.TtAsWrLabouritemRaplcePO;
import com.infodms.dms.po.TtAsWrPartsitemRaplcePO;
import com.infodms.dms.po.TtAsWrWrlabinfoPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.StringUtil;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;

/**
 * Function       :  服务活动管理
 * @author        :  PGM
 * CreateDate     :  2010-06-01
 * @version       :  0.1
 */
public class ServiceActivityManage extends BaseAction{
	private Logger logger = Logger.getLogger(ServiceActivityManage.class);
	private ServiceActivityManageDao dao = ServiceActivityManageDao.getInstance();
	private final ClaimBillMaintainDAO cDao = ClaimBillMaintainDAO.getInstance();
	private ServiceActivityManageItemDao ItemDao = ServiceActivityManageItemDao.getInstance();
	private ActionContext act = ActionContext.getContext();//获取ActionContext
	private AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
	private final String ServiceActivityInitUrl = "/jsp/claim/serviceActivity/serviceActivityManageIndex.jsp";//查询页面
	private final String serviceActivityVinCodeImportInit = "/jsp/claim/serviceActivity/serviceActivityVinCodeImport.jsp";//查询页面
	private final String ServiceActivityInitroleUrl = "/jsp/claim/serviceActivity/serviceActivityManagejudeIndex.jsp";//服务活动情况查询
	private final String ServiceActivityAddUrl = "/jsp/claim/serviceActivity/serviceActivityManageAdd.jsp";//新增页面
	private final String ServiceActivityUpdateUrl = "/jsp/claim/serviceActivity/serviceActivityManageModify.jsp";//修改页面
	private final String ServiceActivityAddUpdateUrl = "/jsp/claim/serviceActivity/serviceActivityManageAddMaintion.jsp";//新增维护子表数据页面
	private final String ServiceActivityInfoUrl = "/jsp/claim/serviceActivity/serviceActivityDetail.jsp";//详细页面
	private final String ServiceActivityInfoModifyUrl = "/jsp/claim/serviceActivity/serviceActivityModify.jsp";//修改时间页面
	private final String ServiceActivityInfoAdd = "/jsp/claim/serviceActivity/serviceActivityDetailAdd.jsp";//详细页面
	private final String ServiceActivityVehicleInfoUrl = "/jsp/claim/serviceActivity/vehicleInfo.jsp";//详细页面
	private final String milageURL = "/jsp/claim/serviceActivity/serviceActivityMilahe.jsp";//行驶里程限制页面
	private final String MAIN_Base_POSITION = "/jsp/claim/serviceActivity/BaseToPosition.jsp";
	private final String MAIN_LARGESS_POSITION = "/jsp/claim/serviceActivity/largessToPosition.jsp";
	private final String MAIN_CHECK_POSITION = "/jsp/claim/serviceActivity/checkToPosition.jsp";
	private final String MAIN_laber_POSITION = "/jsp/claim/serviceActivity/laberToPosition.jsp";
	
	
	
	/**
	 * Function       :  服务活动管理页面初始化
	 * @param         :  
	 * @return        :  serviceActivityManageInit
	 * @throws        :  Exception
	 * LastUpdate     :  2010-06-01
	 */
	public void serviceActivityManageInit(){
		try {
			List<TtAsActivitySubjectPO> list = dao.getTtAsActivitySubjectPo(0);
			act.setOutData("ttAsActivitySubjectPO", list);
			act.setOutData("typeleng",0);
			act.setForword(ServiceActivityInitUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"服务活动管理");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 关系导入初始页面
	 */
	public void serviceActivityVinCodeImportInit(){
		try {
			act.setForword(serviceActivityVinCodeImportInit);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"服务活动管理");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	public void serviceActivityManageMarkfwInit()
	{
		try {
			List<TtAsActivitySubjectPO> list = dao.getTtAsActivitySubjectPo(1);
			act.setOutData("ttAsActivitySubjectPO", list);
			act.setOutData("typeleng",1);
			act.setForword(ServiceActivityInitUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"服务活动管理");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	public void serviceActivityManageroleInit(){
		try {
			List<TtAsActivitySubjectPO> list = dao.getTtAsActivitySubjectPo(1);
			act.setOutData("ttAsActivitySubjectPO", list);
			act.setForword(ServiceActivityInitroleUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"服务活动管理");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	public void serviceActivityManageroleQuery()
	{
		RequestWrapper request = act.getRequest();
		String subjectId= request.getParamValue("subjectId");
		String activity_name= request.getParamValue("activity_name");
		String status= request.getParamValue("activityType");
		String activityCode= request.getParamValue("activityCode");
		String startDate= request.getParamValue("checkSDate");
		String endDate= request.getParamValue("checkEDate");
		TtAsActivityBean ActivityBean = new TtAsActivityBean();
		ActivityBean.setSubjectId(subjectId);
		ActivityBean.setActivityName(activity_name);
		ActivityBean.setStatus(status);
		ActivityBean.setActivityCode(activityCode);
		ActivityBean.setStartdate(startDate);
		ActivityBean.setEnddate(endDate);
		Integer curPage = request.getParamValue("curPage") !=null ? Integer.parseInt(request.getParamValue("curPage")) : 1;
		PageResult<Map<String, Object>> ps = dao.getTtAsActivityBeanInfo(ActivityBean, Constant.PAGE_SIZE,curPage);
		act.setOutData("ps", ps);
	}
	
	/**
	 * Function       :  根据条件查询服务活动管理中符合条件的信息，其中包括：尚未发布状态
	 * @param         :  request-活动编号、活动开始日期、活动结束日期
	 * @return        :  服务活动管理
	 * @throws        :  Exception
	 * LastUpdate     :  2010-06-01
	 */
	public void serviceActivityManageQuery(){
		try {
			RequestWrapper request = act.getRequest();
			TtAsActivityBean ActivityBean = new TtAsActivityBean();
			String activityCode = request.getParamValue("activityCode");  
			String typeleng = request.getParamValue("typeleng");    
			String activityName = request.getParamValue("activity_name");   
			String subjectId = request.getParamValue("subjectId");          
			String activityType = request.getParamValue("activityType");     
			ActivityBean.setActivityCode(activityCode);
			ActivityBean.setActivityName(activityName);
			ActivityBean.setSubjectId(subjectId);
			ActivityBean.setStatus(activityType);
			ActivityBean.setActivityFee(typeleng);
			Integer curPage = request.getParamValue("curPage") !=null ? Integer.parseInt(request.getParamValue("curPage")) : 1;
			PageResult<Map<String, Object>> ps = dao.getAllServiceActivityManageInfo(ActivityBean,curPage,Constant.PAGE_SIZE );
			act.setOutData("ps", ps);
			act.setForword(ServiceActivityInitUrl);
		}catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"服务活动管理");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/*
	 * 服务活动主页面第一次查询2
	 * 无经销商关联
	 */
	public void serviceActivityManageQuery2(){
		try {
			act.getResponse().setContentType("application/json");
			RequestWrapper request = act.getRequest();
			String activityCode = request.getParamValue("activityCode");    //活动编号
			String activityName = request.getParamValue("activity_name");   //活动名称
			String startdate = request.getParamValue("startDate");          //活动开始日期
			String enddate = request.getParamValue("endDate");             //活动结束日期
			String type = request.getParamValue("activityType");         //活动类型
			
			StringBuffer con = new StringBuffer();
			if(StringUtil.notNull(activityCode))
				con.append("and activity_code like '%").append(activityCode).append("%'\n");
			if(StringUtil.notNull(activityName))
				con.append("and activity_name like '%").append(activityName).append("%'\n");
			if(StringUtil.notNull(type))
				con.append("and activity_type = ").append(type).append("\n");
			if(StringUtil.notNull(startdate))
				con.append("and startdate>=to_date('").append(startdate).append(" 00:00:00','yyyy-MM-dd hh24:mi:ss')\n");
			if(StringUtil.notNull(enddate))
				con.append("and enddate<=to_date('").append(enddate).append(" 23:59:59','yyyy-MM-dd hh24:mi:ss')\n");
			int curPage = request.getParamValue("curPage") !=null ? Integer.parseInt(request.getParamValue("curPage")) : 1;
			int pageSize = 15 ;
			PageResult<Map<String,Object>> ps = dao.getServiceActivityInfo(con.toString(), pageSize, curPage) ;
			act.setOutData("ps", ps);
		}catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"服务活动管理");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * Function       :  增加服务活动管理信息初始化页面---服务活动范围TC_CODE查询
	 * @param         :  request
	 * @return        :  服务活动管理
	 * @throws        :  Exception
	 * LastUpdate     :  2010-06-01
	 */
	public void serviceActivityManageArea(){
		RequestWrapper request = act.getRequest();
		try {
			TtAsActivityBean beforeVehicle=dao.serviceActivityVehicleBeforeVehicleArea();//售前车
		    TtAsActivityBean afterVehicle=dao.serviceActivityVehicleAfterVehicleArea();//售后车
		    request.setAttribute("beforeVehicle", beforeVehicle);//查询服务活动车辆范围---售前车
		    request.setAttribute("afterVehicle", afterVehicle);//查询服务活动车辆范围---售后车
		}catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"服务活动管理");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	public void get_part()
	{

		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			String largess_type = request.getParamValue("largess_type");
			String activityId = request.getParamValue("activityId");
			String is_add= request.getParamValue("is_add");
			act.setOutData("is_add", is_add);
			act.setOutData("largess_type", largess_type);
			act.setOutData("activityId", activityId);
			act.setForword(MAIN_Base_POSITION);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "部位管理");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	
	public void open_part()
	{

		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			String largess_type = request.getParamValue("largess_type");
			String activityId = request.getParamValue("activityId");
			String PART_CODE = request.getParamValue("PART_CODE");
			String PART_NAME = request.getParamValue("PART_NAME");
			Integer curPage = request.getParamValue("curPage") !=null ? Integer.parseInt(request.getParamValue("curPage")) : 1;
			PageResult<Map<String, Object>> ps = dao.getBase(Constant.PAGE_SIZE, largess_type, activityId, PART_CODE, PART_NAME, curPage);
			act.setOutData("ps",ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "部位管理");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	public void add_part()
	{
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			String largess_type = request.getParamValue("largess_type");
			String activityId = request.getParamValue("activityId");
			String[] project_ids  = request.getParamValues("checkId");
			String[]  checkIds = request.getParamValues("checkIds");
			String str = "";
			if(project_ids != null && project_ids.length > 0 )
			{
				for(int j = 0 ; j < checkIds.length; j++)
				{
					boolean flg = true;
					
					for(int i = 0 ; i < project_ids.length; i++ )
					{
						 if(checkIds[j].trim().equals(project_ids[i].trim()))
						 {
							 flg = false;
							break;
						 }
					}
					
					 if(flg)
					 {
							 str = str + checkIds[j]+",";
					 }
					
				}
				if(str.length() > 2)
				{
					System.out.println(str);
					StringBuffer sql= new StringBuffer();
					sql.append("DELETE  from tt_as_activity_relation t where t.ACTIVITY_ID = "+activityId+" and t.LARGESS_TYPE = "+largess_type+" and t.PROJECT_ID  in ("+str.substring(0,str.length()-1)+")");
					dao.delete(sql.toString(), null);
				}
				
			}else
			{
				if(checkIds != null)
				{
					for(int i = 0 ; i < checkIds.length; i++ )
					{
						 if(i == checkIds.length -1)
						 {
							 str = str + checkIds[i];
						 }else
						 {
							 str = str + checkIds[i]+",";
						 }
						
					}
					StringBuffer sql= new StringBuffer();
					sql.append("DELETE  from tt_as_activity_relation t where t.ACTIVITY_ID = "+activityId+" and t.LARGESS_TYPE = "+largess_type+" and t.PROJECT_ID  in ("+str+")");
					dao.delete(sql.toString(), null);
					
				}
				
			}
			if(project_ids != null && project_ids.length > 0)
			{
				for(String project_id : project_ids )
				{
					TtAsActivityRelationPO relationPO = new TtAsActivityRelationPO();
					relationPO.setActivityId(Long.parseLong(activityId));
					relationPO.setLargessType(Long.parseLong(largess_type));
					relationPO.setProjectId(Long.parseLong(project_id));
					if(dao.select(relationPO).size() == 0)
					{
						TmPtPartBasePO basePO  = new TmPtPartBasePO();
						basePO.setPartId(Long.parseLong(project_id));
						List<TmPtPartBasePO> list= dao.select(basePO);
						basePO = list.get(0);
						relationPO.setProjectCode(basePO.getPartCode());
						relationPO.setProjectName(basePO.getPartName());
						dao.insert(relationPO);
					}
					
				}
			}
			
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "部位管理");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	
	public void get_largess()
	{

		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			String largess_type = request.getParamValue("largess_type");
			String activityId = request.getParamValue("activityId");
			act.setOutData("largess_type", largess_type);
			act.setOutData("activityId", activityId);
			String is_add= request.getParamValue("is_add");
			act.setOutData("is_add", is_add);
			TtAsActivityRelationPO relationPO = new TtAsActivityRelationPO();
			relationPO.setActivityId(Long.parseLong(activityId));
			relationPO.setLargessType(Long.parseLong(largess_type));
			List<TtAsActivityRelationPO> list= dao.select(relationPO);
			act.setOutData("largess", list);
			act.setForword(MAIN_LARGESS_POSITION);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "部位管理");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	public void add_largess()
	{
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			String largess_type = request.getParamValue("largess_type");
			String activityId = request.getParamValue("activityId");
			String[] gift_names= request.getParamValues("gift_name");
			String[] project_names= request.getParamValues("project_name");
			String[] project_amounts= request.getParamValues("project_amount");
			String[] remarks = request.getParamValues("remark");
			TtAsActivityRelationPO relationPO = new TtAsActivityRelationPO();
			relationPO.setActivityId(Long.parseLong(activityId));
			relationPO.setLargessType(Long.parseLong(largess_type));
			dao.delete(relationPO);
			double num = 0d;
			if(gift_names != null && gift_names.length >0)
			{
				for(int i = 0 ;i < gift_names.length; i++)
				{
					num = num + Double.parseDouble(project_amounts[i]);
					relationPO.setGiftName(gift_names[i]);
					relationPO.setProjectName(project_names[i]);
					relationPO.setProjectAmount(Double.parseDouble(project_amounts[i]));
					relationPO.setRemark(remarks[i]);
					dao.insert(relationPO);
					
				}
			}
			
			act.setOutData("num",num);
			
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "部位管理");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	public void get_check()
	{
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			String largess_type = request.getParamValue("largess_type");
			String activityId = request.getParamValue("activityId");
			act.setOutData("largess_type", largess_type);
			act.setOutData("activityId", activityId);
			String is_add= request.getParamValue("is_add");
			act.setOutData("is_add", is_add);
			TtAsActivityRelationPO relationPO = new TtAsActivityRelationPO();
			relationPO.setActivityId(Long.parseLong(activityId));
			relationPO.setLargessType(Long.parseLong(largess_type));
			List<TtAsActivityRelationPO> list= dao.select(relationPO);
			act.setOutData("largess", list);
			act.setForword(MAIN_CHECK_POSITION);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "部位管理");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	
	public void add_check()
	{
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			String largess_type = request.getParamValue("largess_type");
			String activityId = request.getParamValue("activityId");
			String[] project_names= request.getParamValues("project_name");
			String[] project_amounts= request.getParamValues("project_amount");
			String[] remarks = request.getParamValues("remark");
			TtAsActivityRelationPO relationPO = new TtAsActivityRelationPO();
			relationPO.setActivityId(Long.parseLong(activityId));
			relationPO.setLargessType(Long.parseLong(largess_type));
			dao.delete(relationPO);
			double num = 0d;
			if(project_names != null && project_names.length >0)
			{
				for(int i = 0 ;i < project_names.length; i++)
				{
					num = num + Double.parseDouble(project_amounts[i]);
					relationPO.setProjectName(project_names[i]);
					relationPO.setProjectAmount(Double.parseDouble(project_amounts[i]));
					relationPO.setRemark(remarks[i]);
					dao.insert(relationPO);
				}
			}
			
			act.setOutData("num",num);
			
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "部位管理");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	public void get_laber()
	{

		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			String largess_type = request.getParamValue("largess_type");
			String activityId = request.getParamValue("activityId");
			String is_add= request.getParamValue("is_add");
			act.setOutData("is_add", is_add);
			act.setOutData("largess_type", largess_type);
			act.setOutData("activityId", activityId);
			act.setForword(MAIN_laber_POSITION);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "部位管理");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	
	public void open_laber()
	{

		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(
				Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			String largess_type = request.getParamValue("largess_type");
			String activityId = request.getParamValue("activityId");
			String PART_CODE = request.getParamValue("PART_CODE");
			String PART_NAME = request.getParamValue("PART_NAME");
			Integer curPage = request.getParamValue("curPage") !=null ? Integer.parseInt(request.getParamValue("curPage")) : 1;
			PageResult<Map<String, Object>> ps = dao.getLaber(Constant.PAGE_SIZE, largess_type, activityId, PART_CODE, PART_NAME, curPage);
			act.setOutData("ps",ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "部位管理");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	public void open_subject(){
		try {
			PageResult<Map<String, Object>> ps = dao.getsubject(request,Constant.PAGE_SIZE, getCurrPage());
			act.setOutData("ps",ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "部位管理");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	
	public void add_laber()
	{
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			RequestWrapper request = act.getRequest();
			String largess_type = request.getParamValue("largess_type");
			String activityId = request.getParamValue("activityId");
			String[] project_ids  = request.getParamValues("checkId");
			String[]  checkIds = request.getParamValues("checkIds");
			String str = "";
			if(project_ids != null && project_ids.length > 0 )
			{
				for(int j = 0 ; j < checkIds.length; j++)
				{
					boolean flg = true;
					
					for(int i = 0 ; i < project_ids.length; i++ )
					{
						 if(checkIds[j].trim().equals(project_ids[i].trim()))
						 {
							 flg = false;
							break;
						 }
					}
					
					 if(flg)
					 {
							 str = str + checkIds[j]+",";
					 }
					
				}
				if(str.length() > 2)
				{
					System.out.println(str);
					StringBuffer sql= new StringBuffer();
					sql.append("DELETE  from tt_as_activity_relation t where t.ACTIVITY_ID = "+activityId+" and t.LARGESS_TYPE = "+largess_type+" and t.PROJECT_ID  in ("+str.substring(0,str.length()-1)+")");
					dao.delete(sql.toString(), null);
				}
				
			}else
			{
				for(int i = 0 ; i < checkIds.length; i++ )
				{
					 if(i == checkIds.length -1)
					 {
						 str = str + checkIds[i];
					 }else
					 {
						 str = str + checkIds[i]+",";
					 }
					
				}
				StringBuffer sql= new StringBuffer();
				sql.append("DELETE  from tt_as_activity_relation t where t.ACTIVITY_ID = "+activityId+" and t.LARGESS_TYPE = "+largess_type+" and t.PROJECT_ID  in ("+str+")");
				dao.delete(sql.toString(), null);
				
			}
			if(project_ids != null && project_ids.length > 0)
			{
				for(String project_id : project_ids )
				{
					TtAsActivityRelationPO relationPO = new TtAsActivityRelationPO();
					relationPO.setActivityId(Long.parseLong(activityId));
					relationPO.setLargessType(Long.parseLong(largess_type));
					relationPO.setProjectId(Long.parseLong(project_id));
					if(dao.select(relationPO).size() == 0)
					{
						TtAsWrWrlabinfoPO basePO  = new TtAsWrWrlabinfoPO();
						basePO.setId(Long.parseLong(project_id));
						List<TtAsWrWrlabinfoPO> list= dao.select(basePO);
						basePO = list.get(0);
						relationPO.setProjectCode(basePO.getLabourCode());
						relationPO.setProjectName(basePO.getCnDes());
						dao.insert(relationPO);
					}
				}
			}
			
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "部位管理");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	
	/**
	 * Function       :  增加服务活动管理信息初始化页面
	 * @param         :  request
	 * @return        :  服务活动管理
	 * @throws        :  Exception
	 * LastUpdate     :  2010-06-01
	 */
	public void serviceActivityManageAddInit(){
		RequestWrapper request = act.getRequest();
		try {
			this.serviceActivityManageArea();//服务活动范围TC_CODE查询方法

			TmBusinessAreaPO areaPO = new TmBusinessAreaPO();
			List<TmBusinessAreaPO> lista = dao.select(areaPO);
			List<TtAsActivitySubjectPO> list = dao.getTtAsActivitySubjectPo(Integer.parseInt(request.getParamValue("typeleng")));
			
			Integer curPage = request.getParamValue("curPage") !=null ? Integer.parseInt(request.getParamValue("curPage")) : 1;
			List<Map<String, Object>> acCodes = dao.getActCodes(Constant.ACTIVITY_PKG, Constant.PAGE_SIZE, curPage);
			
			String activityId =SequenceManager.getSequence(""); 
			act.setOutData("activityId", activityId);
			act.setOutData("acCodes", acCodes);
			act.setOutData("areaPO", lista);
			act.setOutData("typeleng", request.getParamValue("typeleng"));
			act.setOutData("ttAsActivitySubjectPO", list);
		    act.setForword(ServiceActivityAddUrl);
		}catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"服务活动管理");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	public void ServiceActivityManageTime()
	{
		RequestWrapper request = act.getRequest();
		try {
			String id  = request.getParamValue("id");
			TtAsActivitySubjectPO subjectPO = new TtAsActivitySubjectPO();
			subjectPO.setSubjectId(Long.parseLong(id));
			List<TtAsActivitySubjectPO> list= dao.select(subjectPO);
			subjectPO = list.get(0);
			act.setOutData("StartDate", Utility.handleDate2( subjectPO.getSubjectStartDate()));
			act.setOutData("EndDate", Utility.handleDate2(subjectPO.getSubjectEndDate()));
			
			String factStartDate = "";
			String factEndDate = "";
			if(null != subjectPO.getFactStartDate() && !"".equals(subjectPO.getFactStartDate())){
				factStartDate = Utility.handleDate2( subjectPO.getFactStartDate());
			}
			if(null != subjectPO.getFactEndDate() && !"".equals(subjectPO.getFactEndDate())){
				factEndDate = Utility.handleDate2( subjectPO.getFactEndDate());
			}
			
			act.setOutData("factStartDate", factStartDate);
			act.setOutData("factEndDate", factEndDate);
		}catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"服务活动管理");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * Function       :  增加服务活动管理信息
	 * @param         :  request---活动ID、活动编号、活动名称、活动类别、活动开始日期、活动结束日期
	 *                             处理方式、距活动结束日期几天上传活动总结、配件费用、工时费用
	 *                             索赔、索赔是否为固定费用、解决方案说明、索赔申请填写指导、服务活动活动状态
	 * @return        :  服务活动管理
	 * @throws        :  Exception
	 * LastUpdate     :  2010-06-01
	 */
	@SuppressWarnings({ "static-access", "unchecked" })
	public void serviceActivityManageAdd(){
		try {
			
			RequestWrapper request = act.getRequest();
			String[] newsIds = request.getParamValues("newsId");
			long newsid =Utility.getLong(SequenceManager.getSequence(""));
			
			if(newsIds!=null){
				for(int i = 0; i < newsIds.length; i++){
					TmFineNewsPO tfn = new TmFineNewsPO();
					tfn.setFineNewsId(newsid);
					tfn.setNewsId(Long.valueOf(newsIds[i]));
					tfn.setCreateBy(logonUser.getUserId());
					tfn.setCreateDate(new Date());
					dao.insert(tfn);
				}
			}
			
			String activityId = request.getParamValue("activityId");             //活动ID
			String subjectId = request.getParamValue("subjectId");
			String repalce_activity_code = CommonUtils.checkNull(request.getParamValue("repalce_activity_code"));//替换件服务活动编码
			String typetowactivity = request.getParamValue("typetowactivity");
			String activityType = CommonUtils.checkNull(request.getParamValue("activityType"));
			String activityCode="";
			if(activityType.equals("10561005")){
				activityCode=Utility.GetClaimBillNo("","YX","THJ");
			}else{
				activityCode = request.getParamValue("activityCode");  //活动编号
			}
			System.out.println("日志==================================="+activityCode);
			String activityName = request.getParamValue("activityName");    //活动名称
			String startdate = request.getParamValue("startDate");          //活动开始日期
			String enddate = request.getParamValue("endDate");              //活动结束日期
			String factstartdate = request.getParamValue("factstartdate");          //活动开始日期
			String factenddate = request.getParamValue("factenddate");              //活动结束日期
			String beforeVehicle = request.getParamValue("beforeVehicle"); //售前车
			String afterVehicle = request.getParamValue("afterVehicle");   //售后车
			String defaultA = request.getParamValue("default");            //结算指向
			String setDirect = "0" ;      //如果是按生产基地结算,则插入0.否则按生产基地
			if(defaultA.equals("2"))
				setDirect = request.getParamValue("yieldly");
			String maxCar = request.getParamValue("car_max");             //单经销商活动总活动次数
			String solution = request.getParamValue("solution");            //解决方案说明
			String claimGuide = request.getParamValue("claimGuide");        //索赔申请填写指导
			//字段
			String troubleDesc = CommonUtils.checkNull(request.getParamValue("TROUBLE_DESC"));
			String troubleReason = CommonUtils.checkNull(request.getParamValue("TROUBLE_REASON"));
			String repairMethod = CommonUtils.checkNull(request.getParamValue("REPAIR_METHOD"));
			String appRemark = CommonUtils.checkNull(request.getParamValue("APP_REMARK"));
			//工时信息
			String [] WR_LABOURCODE0 = request.getParamValues("WR_LABOURCODE");
			String [] WR_LABOURNAME0 = request.getParamValues("WR_LABOURNAME");
			String [] LABOUR_AMOUNT0 = request.getParamValues("LABOUR_AMOUNT0");
			String [] PAY_TYPE_ITEM = request.getParamValues("PAY_TYPE_ITEM");
			String [] MALFUNCTION = request.getParamValues("MALFUNCTION");
			String [] malFunctionValue = request.getParamValues("MALFUNCTIONS");
			if(WR_LABOURCODE0!=null && WR_LABOURCODE0.length>0){
			int temp=0;
			for (String wr_labourcode0 : WR_LABOURCODE0) {
				TtAsWrLabouritemRaplcePO p=new TtAsWrLabouritemRaplcePO();
				p.setLabourId(Utility.getLong(SequenceManager.getSequence("")));
				p.setId(Long.parseLong(activityId));
				p.setWrLabourcode(wr_labourcode0);
				p.setWrLabourname(WR_LABOURNAME0[temp]);
				p.setLabourAmount(Double.parseDouble(LABOUR_AMOUNT0[temp]));
				p.setPayType(Integer.parseInt(PAY_TYPE_ITEM[temp]));
				p.setMalFunctionId(Long.parseLong(MALFUNCTION[temp]));
				p.setMalFunctionValue(malFunctionValue[temp]);
				dao.insert(p);
				temp++;
			}
			//配件信息
			String [] def_id = request.getParamValues("def_id");//替换件活动的ID
			String [] PART_CODE = request.getParamValues("PART_CODE");
			String [] part_ids = request.getParamValues("part_id");
			String [] PART_NAME = request.getParamValues("PART_NAME");
			String [] PART_CODE_OLD = request.getParamValues("PART_CODE_OLD");
			String [] PART_NAME_OLD = request.getParamValues("PART_NAME_OLD");
			String [] QUANTITY = request.getParamValues("QUANTITY");
			String [] PAY_TYPE_PART = request.getParamValues("PAY_TYPE_PART");
			String [] Labour0 = request.getParamValues("Labour0");
			String [] mainPartCode = request.getParamValues("mainPartCode");
			String [] RESPONS_NATURE = request.getParamValues("RESPONS_NATURE");
			String [] HAS_PART = request.getParamValues("HAS_PART");
			String [] PART_USE_TYPE = request.getParamValues("PART_USE_TYPE");
			if(PART_CODE!=null && PART_CODE.length>0){
				int temp1=0;
					for (String part_code : PART_CODE) {
						try {
							TtAsWrPartsitemRaplcePO p=new TtAsWrPartsitemRaplcePO();
							p.setPartId(Utility.getLong(SequenceManager.getSequence("")));
							p.setPartReplceId(part_ids[temp1]);//新加的数据
							p.setId(Long.parseLong(activityId));
							p.setRealPartId(Long.parseLong(def_id[temp1]));
							p.setPartCode(part_code);
							p.setPartName(PART_NAME[temp1]);
							p.setDownPartCode(PART_CODE_OLD[temp1]);
							p.setDownPartName(PART_NAME_OLD[temp1]);
							p.setQuantity(Integer.parseInt(QUANTITY[temp1]));
							p.setPayType(Integer.parseInt(PAY_TYPE_PART[temp1]));
							p.setLabourCode(Labour0[temp1]);
							p.setMainPartCode(mainPartCode[temp1]);
							p.setResponsibilityType(Integer.parseInt(RESPONS_NATURE[temp1]));
							p.setHasPart(Integer.parseInt(HAS_PART[temp1]));
							p.setPartUseType(Integer.parseInt(PART_USE_TYPE[temp1]));
							dao.insert(p);
						} catch (Exception e) {
							e.printStackTrace();
						}
						temp1++;
					}
				}
			}
			Integer status=Constant.SERVICEACTIVITY_STATUS_01;              //服务活动管理--服务活动活动状态:[10681001:尚未发布;10681002:已经发布;10681003:重新发布]
			TtAsActivitySubjectPO subjectPO = new TtAsActivitySubjectPO();
			subjectPO.setSubjectId(Long.parseLong(subjectId));
			List<TtAsActivitySubjectPO> list= dao.select(subjectPO);
			int ACTIVITY_TYPE = 0;
			if(list != null && list.size() >0 )
			{
				subjectPO = list.get(0);
				ACTIVITY_TYPE = subjectPO.getActivityType();
			}
			TtAsActivityPO ActivityPO=new TtAsActivityPO();
			ActivityPO.setActivityId(Long.parseLong(activityId));
			ActivityPO.setSubjectId(Long.parseLong(subjectId));
			ActivityPO.setActivityType(ACTIVITY_TYPE);
			ActivityPO.setActivityName(activityName);
			//添加字段
			ActivityPO.setTroubleDesc(troubleDesc);
			ActivityPO.setTroubleReason(troubleReason);
			ActivityPO.setRepairMethod(repairMethod);
			ActivityPO.setAppRemark(appRemark);
			//ActivityPO.setRepalceActivityCode(repalce_activity_code);
			
			if(null!=startdate){
				DateFormat df=new SimpleDateFormat("yyyy-MM-dd");
				ActivityPO.setStartdate(df.parse(startdate));
			}
			if(null!=enddate){
				DateFormat df=new SimpleDateFormat("yyyy-MM-dd");
				ActivityPO.setEnddate(df.parse(enddate));
			}
			
			

			if(null!=factstartdate){
				DateFormat df=new SimpleDateFormat("yyyy-MM-dd");
				ActivityPO.setFactstartdate(df.parse(factstartdate));
			}
			if(null!=factenddate){
				DateFormat df=new SimpleDateFormat("yyyy-MM-dd");
				ActivityPO.setFactenddate(df.parse(factenddate));
			}
			
			String VehicleArea =beforeVehicle+","+afterVehicle;//服务活动车辆范围：售前车\售后车
			if(!"".equals(beforeVehicle)&&null!=beforeVehicle&&null!=afterVehicle&&!"".equals(afterVehicle)){
				ActivityPO.setVehicleArea(VehicleArea);
			}
			if((!"".equals(beforeVehicle)||null!=beforeVehicle)&&(null==afterVehicle||"".equals(afterVehicle))){
				ActivityPO.setVehicleArea(beforeVehicle);
			}
			if(("".equals(beforeVehicle)||null==beforeVehicle)&&(null!=afterVehicle||!"".equals(afterVehicle))){
				ActivityPO.setVehicleArea(afterVehicle);
			}
			if (activityCode!=null&&!"".equals(activityCode)) 
			{
				ActivityPO.setActivityCode(activityCode);
			}else{
				ActivityPO.setActivityCode("11111");
			}
			ActivityPO.setSolution(solution);
			if(typetowactivity != null && typetowactivity.length()>0)
			{
				ActivityPO.setTowTypeActivity(Integer.parseInt(typetowactivity));
			}
			ActivityPO.setClaimGuide(claimGuide);
			ActivityPO.setStatus(status);
			ActivityPO.setCompanyId(logonUser.getCompanyId());//公司ID
			ActivityPO.setCreateBy(logonUser.getUserId());
			ActivityPO.setCreateDate(new Date());
			ActivityPO.setNewId(newsid);
			ActivityPO.setUpdateBy(logonUser.getUserId());
			ActivityPO.setUpdateDate(new Date());
			ActivityPO.setIsDel(Integer.parseInt(Constant.IS_DEL_00));//IS_DEL_01 = "1":逻辑删除;IS_DEL_00 = "0":逻辑未删除
			Long companyId=GetOemcompanyId.getOemCompanyId(logonUser);     //公司ID
			ActivityPO.setCompanyId(companyId);
			if(StringUtil.notNull(maxCar))
				ActivityPO.setMaxCar(Integer.parseInt(maxCar));
			
			if(StringUtil.notNull(setDirect))
				ActivityPO.setSetDirect(Long.parseLong(setDirect));
			ActivityPO.setMilageConfine(Integer.parseInt("1"));
			dao.serviceActivityManageAdd(ActivityPO);
			
			String[] pro_code = request.getParamValues("pro_codev");
			String[] amount = request.getParamValues("amount");
			String[] paid = request.getParamValues("paid");
			String[] dealwith = request.getParamValues("dealwith");
			String[] zhek = request.getParamValues("zhek");
			String type = CommonUtils.checkNull(request.getParamValue("ACTIVITY_CODE"));
			if(pro_code!= null &&  pro_code.length>0)
			{
				for(int i = 0;i<pro_code.length; i++)
				{
					TtAsActivityProjectPO ttAsActivityProjectPO = new TtAsActivityProjectPO();
					ttAsActivityProjectPO.setId(Long.parseLong(SequenceManager.getSequence("")));
					ttAsActivityProjectPO.setActivityId(Long.parseLong(activityId));
					ttAsActivityProjectPO.setProCode(Integer.parseInt(pro_code[i]));
					ttAsActivityProjectPO.setPaid(Long.parseLong(paid[i]));
					ttAsActivityProjectPO.setDealWay(Integer.parseInt(dealwith[i]));
					if(! pro_code[i].equals("3537005"))
					{
						ttAsActivityProjectPO.setAmount(Double.parseDouble(amount[i]));
					}
					
					if( pro_code[i].equals("3537002"))
					{
						ttAsActivityProjectPO.setMaintainTime(Integer.parseInt(zhek[i]));
					}
					ttAsActivityProjectPO.setCreateBy(logonUser.getUserId());
					ttAsActivityProjectPO.setCreateDate(new Date());
					dao.insert(ttAsActivityProjectPO);
				}
				
			}
			if (activityCode!=null&&!"".equals(activityCode)) 
			{
			}else
			{
				String num = dao.getNum(subjectId);
				int year = new Date().getYear()+1900;
				activityCode = "HDFW"+year+ACTIVITY_TYPE+num+type;
				
				System.out.println("日志==================================="+activityCode);
				TtAsActivityPO ttAsActivityPO = new TtAsActivityPO();
				ttAsActivityPO.setActivityId(ActivityPO.getActivityId());
				
				TtAsActivityPO ttAsActivityPO1 = new TtAsActivityPO();
				ttAsActivityPO1.setActivityCode(activityCode);
				dao.update(ttAsActivityPO, ttAsActivityPO1);
			}
			
			
			/**xiongchuan add 2011-7-5**/
			String ywzj = activityId;
			String[] fjids = request.getParamValues("fjids");
			FileUploadManager.fileUploadByBusiness(ywzj, fjids, logonUser);
			String typeleng= request.getParamValue("typeleng");
			this.serviceActivityManageAddAfter(activityId,typeleng); //跳转到修改页面，维护车辆信息

			
		}catch (Exception e) {
			e.printStackTrace();
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"服务活动管理信息增加");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * Function       :  新增之后---查询服务管理活动信息
	 * @param         :  request-活动ID
	 * @return        :  服务管理活动信息
	 * @throws        :  Exception
	 * LastUpdate     :  2010-06-01
	 */
	public void serviceActivityManageAddAfter(String activityId,String type){ 
		RequestWrapper request = act.getRequest();
		try {
			List<TtAsActivitySubjectPO> list = dao.getTtAsActivitySubjectPo(Integer.parseInt(type));
			TtAsActivityBean ActivityBean=new TtAsActivityBean();
			ActivityBean=dao.serviceActivityManageCommon(activityId);
			if(ActivityBean.getSubjectId()!=null && ActivityBean.getSubjectId().length()>0)
			{
				long subject= Long.parseLong(ActivityBean.getSubjectId());
				for(int i = 0 ;i<list.size();i++)
				{
					if(subject == list.get(i).getSubjectId())
					{
						TtAsActivitySubjectPO po = list.get(0);
						list.set(0, list.get(i));
						list.set(i,po);
					}
				}
			}
			
			List<TtAsActivityProjectPO> project = dao.getTtAsActivityProjectPo(activityId);
			request.setAttribute("ActivityPO", ActivityBean);
			
			List<TtAsWrPartsitemRaplcePO> rp=dao.getRaplceP(activityId);//获取配件
			List<TtAsWrLabouritemRaplcePO> lp=dao.getRaplceL(activityId);//获取工时
			request.setAttribute("rp", rp);
			request.setAttribute("lp", lp);
			this.serviceActivityManageArea();//服务活动范围TC_CODE查询方法
			TtAsActivityBean VehicleArea  = dao.serviceActivityVehicleArea(activityId);//服务活动范围方法,用于回显
			String vicle=VehicleArea.getVehicleArea();
			String vicle1="";
			String vicle2="";
			if(!"".equals(vicle)&&null!=vicle){
				if(vicle.indexOf(",")==-1){
					vicle1=vicle;
					vicle2=vicle;
				}else{
				String vv[]=vicle.split(",");
				vicle1=vv[0];
				vicle2=vv[1];
				}
			}
			
			TmBusinessAreaPO areaPO = new TmBusinessAreaPO();
			List<TmBusinessAreaPO> lista = dao.select(areaPO);
			for(int i = 0 ;i<lista.size(); i++)
			{
				if(lista.get(i).getAreaId() == Long.parseLong(""+ActivityBean.getSetDirect()))
				{
					TmBusinessAreaPO arepo1 = lista.get(0);
					lista.set(0, lista.get(i));
					lista.set(i, arepo1);
					break;
				}
			}
			
			BonusDAO bonusDao = new BonusDAO();
			List<Map<String,Object>> listNews = bonusDao.dlrQueryEncourageNewsDetail(activityId);
			request.setAttribute("vicle1", vicle1);
			request.setAttribute("vicle2", vicle2);
			act.setOutData("listNews", listNews);
			act.setOutData("areaPO", lista);
			act.setOutData("activityId", activityId);
			act.setOutData("ttAsActivitySubjectPO", list);
			act.setOutData("project", project);
			act.setOutData("typeleng", type);
			act.setForword(ServiceActivityUpdateUrl);
			
		} catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"服务活动管理信息");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * Function       :  修改前置---查询服务管理活动信息
	 * @param         :  request-活动ID
	 * @return        :  服务管理活动信息
	 * @throws        :  Exception
	 * LastUpdate     :  2010-06-01
	 */
	public void serviceActivityManageUpdateInit(){ 
		RequestWrapper request = act.getRequest();
		try {
			String activityId   = request.getParamValue("activityId");      //活动ID
			String typeleng   = request.getParamValue("typeleng"); 
			
			List<TtAsActivitySubjectPO> list = dao.getTtAsActivitySubjectPo(Integer.parseInt(typeleng));
			TtAsActivityBean ActivityBean=new TtAsActivityBean();
			ActivityBean=dao.serviceActivityManageCommon(activityId);
			if(ActivityBean.getSubjectId()!=null && ActivityBean.getSubjectId().length()>0)
			{
				long subject= Long.parseLong(ActivityBean.getSubjectId());
				for(int i = 0 ;i<list.size();i++)
				{
					if(subject == list.get(i).getSubjectId())
					{
						TtAsActivitySubjectPO po = list.get(0);
						list.set(0, list.get(i));
						list.set(i,po);
					}
				}
			}
			
			List<TtAsActivityProjectPO> project = dao.getTtAsActivityProjectPo(activityId);
			request.setAttribute("ActivityPO", ActivityBean);
			List<TtAsWrPartsitemRaplcePO> rp=dao.getRaplceP(activityId);//获取配件
			List<TtAsWrLabouritemRaplcePO> lp=dao.getRaplceL(activityId);//获取工时
			request.setAttribute("rp", rp);
			request.setAttribute("lp", lp);
			
			this.serviceActivityManageArea();//服务活动范围TC_CODE查询方法
			TtAsActivityBean VehicleArea  = dao.serviceActivityVehicleArea(activityId);//服务活动范围方法,用于回显
			String vicle=VehicleArea.getVehicleArea();
			String vicle1="";
			String vicle2="";
			if(!"".equals(vicle)&&null!=vicle){
				if(vicle.indexOf(",")==-1){
					vicle1=vicle;
					vicle2=vicle;
				}else{
				String vv[]=vicle.split(",");
				vicle1=vv[0];
				vicle2=vv[1];
				}
			}
			
			TmBusinessAreaPO areaPO = new TmBusinessAreaPO();
			List<TmBusinessAreaPO> lista = dao.select(areaPO);
			for(int i = 0 ;i<lista.size(); i++)
			{
				if(lista.get(i).getAreaId() == Long.parseLong(""+ActivityBean.getSetDirect()))
				{
					TmBusinessAreaPO arepo1 = lista.get(0);
					lista.set(0, lista.get(i));
					lista.set(i, arepo1);
					break;
				}
			}
			
			
			BonusDAO bonusDao = new BonusDAO();
			List<Map<String,Object>> listNews = bonusDao.dlrQueryEncourageNewsDetail(Long.parseLong(ActivityBean.getNew_id()));
			request.setAttribute("vicle1", vicle1);
			request.setAttribute("vicle2", vicle2);
			act.setOutData("listNews", listNews);
			act.setOutData("areaPO", lista);
			act.setOutData("tow_type_activity", ActivityBean.getTow_type_activity());
			act.setOutData("activityId", activityId);
			act.setOutData("ttAsActivitySubjectPO", list);
			act.setOutData("project", project);
			act.setOutData("beanDirect", ActivityBean.getSetDirect());
			act.setOutData("typeleng", typeleng);
			act.setForword(ServiceActivityUpdateUrl);
			
		} catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"服务活动管理信息");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	
	public void ServiceActivityManageDeletedelp()
	{
		try {
		RequestWrapper request = act.getRequest();
		TtAsActivityProjectPO activityProjectPO = new TtAsActivityProjectPO();
		
		String activityId= request.getParamValue("activityId");
		String pro_code = request.getParamValue("type");
		activityProjectPO.setActivityId(Long.parseLong(activityId));
		activityProjectPO.setProCode(Integer.parseInt(pro_code));
		dao.delete(activityProjectPO);
		}catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"活动项目删除失败");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	
	/**
	 * Function       :  修改服务活动管理信息
	 * @param         :  request---活动ID、活动编号、活动名称、活动类型、活动类别、活动开始日期、活动结束日期
	 *                             处理方式、距活动结束日期几天上传活动总结、配件费用、工时费用、索赔、索赔是否为固定费用
	 *                             解决方案说明、索赔申请填写指导、服务活动活动状态、修改内容
	 * @return        :  服务活动管理
	 * @throws        :  Exception
	 * LastUpdate     :  2010-06-01
	 */
	@SuppressWarnings({ "static-access", "unchecked" })
	public void serviceActivityManageUpdate(){
		try {
			RequestWrapper request = act.getRequest();
			String activityId   = request.getParamValue("activityId");      //活动ID
			String type = request.getParamValue("type");
			String[] newsIds = request.getParamValues("newsId");
			long newsid =Utility.getLong(SequenceManager.getSequence(""));
			
			if(newsIds!=null){
				for(int i = 0; i < newsIds.length; i++){
					TmFineNewsPO tfn = new TmFineNewsPO();
					tfn.setFineNewsId(newsid);
					tfn.setNewsId(Long.valueOf(newsIds[i]));
					tfn.setCreateBy(logonUser.getUserId());
					tfn.setCreateDate(new Date());
					dao.insert(tfn);
				}
			}
			String subjectId = request.getParamValue("subjectId");    //活动主题编号
			String activityName = request.getParamValue("activityName");    //活动名称
			String startdate = request.getParamValue("startDate");          //活动开始日期
			String enddate = request.getParamValue("endDate");              //活动结束日期
			String factstartdate = request.getParamValue("factstartdate");          //活动开始日期
			String factenddate = request.getParamValue("factenddate");              //活动结束日期
			String typetowactivity = request.getParamValue("typetowactivity");
			String beforeVehicle = request.getParamValue("beforeVehicle"); //售前车
			String afterVehicle = request.getParamValue("afterVehicle");   //售后车
			String max_car = request.getParamValue("car_max");             //单经销商活动次数
			String defaultA = request.getParamValue("default");            //结算指向
			String setDirect = "0" ;      //如果是按生产基地结算,则插入0.否则按生产基地
			if(defaultA.equals("2"))
				setDirect = request.getParamValue("yieldly");
			String solution = request.getParamValue("solution");            //解决方案说明
			String claimGuide = request.getParamValue("claimGuide");        //索赔申请填写指导
			
			TtAsActivityPO ActivityPOCon=new TtAsActivityPO();
			ActivityPOCon.setActivityId(Long.parseLong(activityId));        //以活动ID为条件进行修改
			TtAsActivityPO ActivityPO=new TtAsActivityPO();                 //修改内容
			TtAsActivitySubjectPO subjectPO = new TtAsActivitySubjectPO();
			subjectPO.setSubjectId(Long.parseLong(subjectId));
			List<TtAsActivitySubjectPO> list= dao.select(subjectPO);
			Integer ACTIVITY_TYPE = 0;
			if(list != null && list.size() >0 )
			{
				subjectPO = list.get(0);
				ACTIVITY_TYPE = subjectPO.getActivityType();
			}
			//字段
			String troubleDesc = CommonUtils.checkNull(request.getParamValue("TROUBLE_DESC"));
			String troubleReason = CommonUtils.checkNull(request.getParamValue("TROUBLE_REASON"));
			String repairMethod = CommonUtils.checkNull(request.getParamValue("REPAIR_METHOD"));
			String appRemark = CommonUtils.checkNull(request.getParamValue("APP_REMARK"));
			if(Constant.SERVICEACTIVITY_TYPE_05.equals(ACTIVITY_TYPE)){
			//工时信息
			String [] WR_LABOURCODE0 = request.getParamValues("WR_LABOURCODE");
			String [] WR_LABOURNAME0 = request.getParamValues("WR_LABOURNAME");
			String [] LABOUR_AMOUNT0 = request.getParamValues("LABOUR_AMOUNT");
			String [] PAY_TYPE_ITEM = request.getParamValues("PAY_TYPE_ITEM");
			String [] MALFUNCTION = request.getParamValues("MALFUNCTION");
			String [] malFunctionValue = request.getParamValues("MALFUNCTIONS");
			if(WR_LABOURCODE0!=null && WR_LABOURCODE0.length>0){
				TtAsWrLabouritemRaplcePO t=new TtAsWrLabouritemRaplcePO();
				t.setId(Long.parseLong(activityId));
				dao.delete(t);
				int temp=0;
				for (String wr_labourcode0 : WR_LABOURCODE0) {
					TtAsWrLabouritemRaplcePO p=new TtAsWrLabouritemRaplcePO();
					p.setLabourId(Utility.getLong(SequenceManager.getSequence("")));
					p.setId(Long.parseLong(activityId));
					p.setWrLabourcode(wr_labourcode0);
					p.setWrLabourname(WR_LABOURNAME0[temp]);
					p.setLabourAmount(Double.parseDouble(LABOUR_AMOUNT0[temp]));
					p.setPayType(Integer.parseInt(PAY_TYPE_ITEM[temp]));
					p.setMalFunctionId(Long.parseLong(MALFUNCTION[temp]));
					p.setMalFunctionValue(malFunctionValue[temp]);
					dao.insert(p);
					temp++;
				}
				//配件信息
				String [] def_id = request.getParamValues("def_id");
				String [] PART_CODE = request.getParamValues("PART_CODE");
				String [] PART_NAME = request.getParamValues("PART_NAME");
				String [] PART_CODE_OLD = request.getParamValues("PART_CODE_OLD");
				String [] PART_NAME_OLD = request.getParamValues("PART_NAME_OLD");
				String [] QUANTITY = request.getParamValues("QUANTITY");
				String [] PAY_TYPE_PART = request.getParamValues("PAY_TYPE_PART");
				String [] Labour0 = request.getParamValues("Labour0");
				String [] mainPartCode = request.getParamValues("mainPartCode");
				String [] RESPONS_NATURE = request.getParamValues("RESPONS_NATURE");
				String [] HAS_PART = request.getParamValues("HAS_PART");
				String [] PART_USE_TYPE = request.getParamValues("PART_USE_TYPE");
				TtAsWrPartsitemRaplcePO t1=new TtAsWrPartsitemRaplcePO();
				t1.setId(Long.parseLong(activityId));
				dao.delete(t1);
				int temp1=0;
				if(PART_CODE!=null && PART_CODE.length>0){
					for (String part_code : PART_CODE) {
							TtAsWrPartsitemRaplcePO p=new TtAsWrPartsitemRaplcePO();
							p.setPartId(Utility.getLong(SequenceManager.getSequence("")));
							p.setId(Long.parseLong(activityId));
							p.setRealPartId(Long.parseLong(def_id[temp1]));
							p.setPartCode(part_code);
							p.setPartName(PART_NAME[temp1]);
							p.setDownPartCode(PART_CODE_OLD[temp1]);
							p.setDownPartName(PART_NAME_OLD[temp1]);
							p.setQuantity(Integer.parseInt(QUANTITY[temp1]));
							p.setPayType(Integer.parseInt(PAY_TYPE_PART[temp1]));
							p.setLabourCode(Labour0[temp1]);
							p.setMainPartCode(mainPartCode[temp1]);
							p.setResponsibilityType(Integer.parseInt(RESPONS_NATURE[temp1]));
							p.setHasPart(Integer.parseInt(HAS_PART[temp1]));
							p.setPartUseType(Integer.parseInt(PART_USE_TYPE[temp1]));
							dao.insert(p);
							temp1++;
						}
					}
				}
			}
			
			ActivityPO.setSubjectId(Long.parseLong(subjectId));
			ActivityPO.setActivityType(ACTIVITY_TYPE);
			ActivityPO.setActivityName(activityName);
			ActivityPO.setNewId(newsid);
			if(typetowactivity !=null && typetowactivity.length()>0){
				ActivityPO.setTowTypeActivity(Integer.parseInt(typetowactivity));
			}
			
			if(null!=startdate){
				DateFormat df=new SimpleDateFormat("yyyy-MM-dd");
				ActivityPO.setStartdate(df.parse(startdate));
			}
			if(null!=enddate){
				DateFormat df=new SimpleDateFormat("yyyy-MM-dd");
				ActivityPO.setEnddate(df.parse(enddate));
			}
			
			
			if(null!=factstartdate){
				DateFormat df=new SimpleDateFormat("yyyy-MM-dd");
				ActivityPO.setFactstartdate(df.parse(factstartdate));
			}
			if(null!=factenddate){
				DateFormat df=new SimpleDateFormat("yyyy-MM-dd");
				ActivityPO.setFactenddate(df.parse(factenddate));
			}
			
			String VehicleArea =beforeVehicle+","+afterVehicle;//服务活动车辆范围：售前车、售后车
			if(!"".equals(beforeVehicle)&&null!=beforeVehicle&&null!=afterVehicle&&!"".equals(afterVehicle)){
				ActivityPO.setVehicleArea(VehicleArea);
			}
			if((!"".equals(beforeVehicle)||null!=beforeVehicle)&&(null==afterVehicle||"".equals(afterVehicle))){
				ActivityPO.setVehicleArea(beforeVehicle);
			}
			if(("".equals(beforeVehicle)||null==beforeVehicle)&&(null!=afterVehicle||!"".equals(afterVehicle))){
				ActivityPO.setVehicleArea(afterVehicle);
			}
			if(type != null && type.length() >0 )
			{
				ActivityPO.setStatus(Constant.SERVICEACTIVITY_STATUS_02);
			}
			if(StringUtil.notNull(max_car))
				ActivityPO.setMaxCar(Integer.parseInt(max_car));
			
			ActivityPO.setSetDirect(Long.parseLong(setDirect));
			ActivityPO.setSolution(solution);
			ActivityPO.setClaimGuide(claimGuide);
			ActivityPO.setUpdateBy(logonUser.getUserId());
			ActivityPO.setUpdateDate(new Date());
			//添加字段
			ActivityPO.setTroubleDesc(troubleDesc);
			ActivityPO.setTroubleReason(troubleReason);
			ActivityPO.setRepairMethod(repairMethod);
			ActivityPO.setAppRemark(appRemark);
			TtAsActivityPO ac=new TtAsActivityPO();
			ac.setActivityId(Long.parseLong(activityId));        //以活动ID为条件进行修改
			ac.setActivityType(ACTIVITY_TYPE);
			dao.serviceActivityManageUpdate(ac,ActivityPO);
			
			String[] pro_code = request.getParamValues("pro_codev");
			String[] amount = request.getParamValues("amount");
			String[] dealwith = request.getParamValues("dealwith");
			String[] zhek = request.getParamValues("zhek");
			String[] paid = request.getParamValues("paid");
			if(pro_code != null && pro_code.length>0)
			{
				for(int i = 0;i<pro_code.length; i++)
				{
					
					TtAsActivityProjectPO ttAsActivityProjectPO = new TtAsActivityProjectPO();
					ttAsActivityProjectPO.setId(Long.parseLong(SequenceManager.getSequence("")));
					ttAsActivityProjectPO.setActivityId(Long.parseLong(activityId));
					ttAsActivityProjectPO.setProCode(Integer.parseInt(pro_code[i]));
					if(! pro_code[i].equals("3537005"))
					{
						ttAsActivityProjectPO.setAmount(Double.parseDouble(amount[i]));
					}
					
					if( pro_code[i].equals("3537002"))
					{
						ttAsActivityProjectPO.setMaintainTime(Integer.parseInt(zhek[i]));
					}
					
					ttAsActivityProjectPO.setDealWay(Integer.parseInt(dealwith[i]));
					ttAsActivityProjectPO.setPaid(Long.parseLong(paid[i]));
					ttAsActivityProjectPO.setCreateBy(logonUser.getUserId());
					ttAsActivityProjectPO.setCreateDate(new Date());
					dao.insert(ttAsActivityProjectPO);
				}
			}
			String[] idMy = request.getParamValues("idMy");
			String[] amountMy = request.getParamValues("amountMy");
			String[] wuyong = request.getParamValues("wuyong");
			String[] proCodeMy = request.getParamValues("proCodeMy");
			String[] fufei = request.getParamValues("fufei");
			String[] zhekname = request.getParamValues("zhekname");
			
			if(idMy != null && idMy.length > 0)
			{
				int j = 0;
				for(int i = 0 ; i< idMy.length ; i++)
				{
					TtAsActivityProjectPO ttAsActivityProjectPO1 = new TtAsActivityProjectPO();
					ttAsActivityProjectPO1.setId(Long.parseLong(idMy[i]));
					TtAsActivityProjectPO ttAsActivityProjectPO = new TtAsActivityProjectPO();
					ttAsActivityProjectPO.setId(Long.parseLong(idMy[i]));
					ttAsActivityProjectPO.setDealWay(Integer.parseInt(wuyong[i]));
					ttAsActivityProjectPO.setPaid(Long.parseLong(fufei[i]));
					if(proCodeMy[i].equals("3537002"))
					{
						ttAsActivityProjectPO .setMaintainTime(Integer.parseInt(zhekname[j]));
						j++;
					}
					if(! proCodeMy[i].equals("3537005"))
					{
						ttAsActivityProjectPO.setAmount(Double.parseDouble(amountMy[i]));
					}
					
					
					dao.update(ttAsActivityProjectPO1, ttAsActivityProjectPO);
				}
				
				
			}
			
			
			Long acid = Long.parseLong(activityId);
			if(type !=null && type.length()>0)
			{
				dao.executeXYZ(acid);
			}
			act.setOutData("json", 1);
			if(request.getParamValue("typeleng").equals("0")){
				serviceActivityManageInit();
			}else{
				serviceActivityManageMarkfwInit();
			}
			
				
			
		}catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"服务活动管理信息增加");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * Function       :  删除服务活动管理信息
	 * @param         :  request-活动ID
	 * @return        :  服务活动管理信息
	 * @throws        :  ParseException
	 * LastUpdate     :  2010-06-01
	 */
	@SuppressWarnings("static-access")
	public void serviceActivityManageDelete(){ 
		RequestWrapper request = act.getRequest();
		String activityId=request.getParamValue("activityId");//活动ID
		TtAsActivityPO  ActivityPoContent =new TtAsActivityPO();
		try{
			ActivityPoContent.setUpdateBy(logonUser.getUserId());//修改人
			ActivityPoContent.setUpdateDate(new Date());//修改时间
			ActivityPoContent.setIsDel(Integer.parseInt(Constant.IS_DEL_01));//IS_DEL_01 = "1" 逻辑删除;IS_DEL_00 = "0" 逻辑未删除
			dao.serviceActivityManageDelete(activityId,ActivityPoContent);
			act.setOutData("returnValue", 1);//returnValue 值：1，表示成功
			//act.setForword(ServiceActivityInitUrl);
		  }catch(Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.DELETE_FAILURE_CODE,"删除服务活动信息");
			logger.error(logonUser,e1);
		 	act.setException(e1);
		  }
	}
	
	@SuppressWarnings("static-access")
	public void serviceActivitycommit(){ 
		RequestWrapper request = act.getRequest();
		String activityId=request.getParamValue("activityId");//活动ID
		TtAsActivityPO  ActivityPoContent =new TtAsActivityPO();
		try{
			ActivityPoContent.setActivityId(Long.parseLong(activityId));
			TtAsActivityPO ttAsActivityPO = new TtAsActivityPO();
			List<TtAsActivityPO> list = dao.select(ActivityPoContent);
			int status= list.get(0).getStatus();
			if(status == 10681001)
			{
				ttAsActivityPO.setStatus(10681002);
			}
			ttAsActivityPO.setUpdateBy(logonUser.getUserId());//修改人
			ttAsActivityPO.setUpdateDate(new Date());//修改时间
			dao.update(ActivityPoContent, ttAsActivityPO);
			act.setOutData("returnValue", 1);//returnValue 值：1，表示成功
			//act.setForword(ServiceActivityInitUrl);
		  }catch(Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.DELETE_FAILURE_CODE,"删除服务活动信息");
			logger.error(logonUser,e1);
		 	act.setException(e1);
		  }
	}
	public void ServiceActivityManageDeleteNew()
	{
		RequestWrapper request = act.getRequest();
		String activityId=request.getParamValue("activityId");//活动ID
		String newid = request.getParamValue("newid");
		TtAsActivityPO  ActivityPoContent =new TtAsActivityPO();
		ActivityPoContent.setActivityId(Long.parseLong(activityId));
		ActivityPoContent.setNewId(Long.parseLong(newid));
		TtAsActivityPO ttAsActivityPO = new TtAsActivityPO();
		ttAsActivityPO.setNewId(Long.parseLong("0"));
		dao.update(ActivityPoContent, ttAsActivityPO);
		act.setForword(ServiceActivityUpdateUrl);
		
	}
	public void ServiceActivityManagejudeMy() throws Exception
	{
		RequestWrapper request = act.getRequest();
		String subjectId = request.getParamValue("subjectId");
		String startDate = request.getParamValue("startDate");
		String endDate = request.getParamValue("endDate");
		long activityId = Long.parseLong(request.getParamValue("activityId"));
		
		TtAsActivityMgroupPO activityMgroupPO = new TtAsActivityMgroupPO();
		activityMgroupPO.setActivityId(activityId);
		boolean fage =false; 
		if(dao.select(activityMgroupPO) != null &&  dao.select(activityMgroupPO).size()> 0)
		{
			fage = true;
		}
		
		TtAsActivityAgePO activityAgePO = new TtAsActivityAgePO();
		activityAgePO.setActivityId(activityId);
		
		if(dao.select(activityAgePO) != null &&  dao.select(activityAgePO).size()> 0)
		{
			fage = true;
		}
		TtAsActivityMilagePO activityMilagePO = new TtAsActivityMilagePO();
		activityMilagePO.setActivityId(activityId);
		
		if(dao.select(activityMilagePO) != null &&  dao.select(activityMilagePO).size()> 0)
		{
			fage = true;
		}
		TtAsActivityYieldlyPO activityYieldlyPO = new TtAsActivityYieldlyPO();
		activityYieldlyPO.setActivityId(activityId);
		if(dao.select(activityYieldlyPO) != null &&  dao.select(activityYieldlyPO).size()> 0)
		{
			fage = true;
		}
		TtAsActivityCharactorPO activityCharactorPO = new TtAsActivityCharactorPO();
		activityCharactorPO.setActivityId(activityId);
		if(dao.select(activityCharactorPO) != null &&  dao.select(activityCharactorPO).size()> 0)
		{
			fage = true;
		}
		
		
		boolean vfage = false;
		TtAsActivityVehiclePO activityVehiclePO = new TtAsActivityVehiclePO();
		activityVehiclePO.setActivityId(activityId);
		if(dao.select(activityVehiclePO) != null &&  dao.select(activityVehiclePO).size()> 0)
		{
			vfage = true;
		}
		
		
		boolean dfage = false;
		TtAsActivityDealerPO activityDealerPO = new TtAsActivityDealerPO();
		activityDealerPO.setActivityId(activityId);
		if(dao.select(activityDealerPO) != null &&  dao.select(activityDealerPO).size()> 0)
		{
			dfage = true;
		}
		
		if(!((dfage && vfage) || (dfage && fage)) )
		{
			act.setOutData("paduan",0);
		}
		
		TtAsActivitySubjectPO ttAsActivitySubjectPO = new TtAsActivitySubjectPO();
		ttAsActivitySubjectPO.setSubjectId(Long.parseLong(subjectId));
		
		List<TtAsActivitySubjectPO> list = dao.select(ttAsActivitySubjectPO);
		ttAsActivitySubjectPO = list.get(0);
		int fagg = 1;
		if(jude(ttAsActivitySubjectPO.getSubjectStartDate().getTime(),startDate) == 0 )
		{
			fagg = 0;
		}
		
		if(jude(ttAsActivitySubjectPO.getSubjectEndDate().getTime(),endDate) == 2 )
		{
			fagg = 0;
		}
		act.setOutData("fuwu",fagg);
	}
	
	@SuppressWarnings("unchecked")
	public void ServiceActivityManagejude() throws Exception
	{
		RequestWrapper request = act.getRequest();
		String activity_code=request.getParamValue("id");
		String subjectId = request.getParamValue("subjectId");
		String startDate = request.getParamValue("startDate");
		String endDate = request.getParamValue("endDate");
		
		TtAsActivitySubjectPO ttAsActivitySubjectPO = new TtAsActivitySubjectPO();
		ttAsActivitySubjectPO.setSubjectId(Long.parseLong(subjectId));
		
		List<TtAsActivitySubjectPO> list = dao.select(ttAsActivitySubjectPO);
		ttAsActivitySubjectPO = list.get(0);
		
		int fagg = 1;
		if(jude(ttAsActivitySubjectPO.getSubjectStartDate().getTime(),startDate) == 0 )
		{
			fagg = 0;
		}
		
		if(jude(ttAsActivitySubjectPO.getSubjectEndDate().getTime(),endDate) == 2 )
		{
			fagg = 0;
		}
		act.setOutData("fuwu",fagg);
		
		boolean fag = dao.ServiceActivityManagejude(activity_code);
		if(fag)
		{
			act.setOutData("success", 1);
		}else
		{
			act.setOutData("success", 0);
		}
		
		String[] pro_codev = request.getParamValues("pro_codev");
		String activityId = request.getParamValue("activityId");
		if(pro_codev != null && pro_codev.length > 0 )
		{
			for(String pro_code : pro_codev)
			{
				if(pro_code.equals("3537005"))
				{
					TtAsActivityRelationPO activityRelationPO = new TtAsActivityRelationPO();
					activityRelationPO.setActivityId(Long.parseLong(activityId));
					activityRelationPO.setLargessType(Long.parseLong(pro_code));
					if(dao.select(activityRelationPO).size() == 0)
					{
						act.setOutData("xianmu", 0);
					}else
					{
						act.setOutData("xianmu", 1);
					}
					
				}else
				{
					act.setOutData("xianmu", 1);
				}
			}
			
		}else
		{
			act.setOutData("xianmu", 1);
		}
		 
		
		
	}
	public int jude(Long sdate , String fdate) throws Exception
	{
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		Date fdate1 = sf.parse(fdate);
		if(sdate > fdate1.getTime())
		{
			return 0;
		}else if(sdate == fdate1.getTime())
		{
			return 1;
		}else
		{
			return 2;
		}
		
	}
	
	public void ServiceActivityManageDeleteProject()
	{
		RequestWrapper request = act.getRequest();
		String id=request.getParamValue("id");
		String objv=request.getParamValue("objv");
		String activityId=request.getParamValue("activityId");
		if(id != null && id.length() > 0)
		{
			TtAsActivityProjectPO  ActivityPoContent =new TtAsActivityProjectPO();
			ActivityPoContent.setId(Long.parseLong(id));
			List<TtAsActivityProjectPO>  list= dao.select(ActivityPoContent);
			if( list.size() > 0 )
			{
				ActivityPoContent = list.get(0);
				TtAsActivityRelationPO relationPO = new TtAsActivityRelationPO();
				relationPO.setLargessType(Long.parseLong(""+ActivityPoContent.getProCode()));
				relationPO.setActivityId(Long.parseLong(activityId));
				dao.delete(relationPO);
			}
		
			
			ActivityPoContent =new TtAsActivityProjectPO();
			ActivityPoContent.setId(Long.parseLong(id));
			dao.delete(ActivityPoContent);
		}else if(objv != null && objv.length() > 0)
		{
			TtAsActivityRelationPO relationPO = new TtAsActivityRelationPO();
			relationPO.setLargessType(Long.parseLong(objv));
			relationPO.setActivityId(Long.parseLong(activityId));
			dao.delete(relationPO);
		}
		
		
		act.setForword(ServiceActivityUpdateUrl);
		
	}
	/**
	 * Function       :  查询服务活动信息-明细
	 * @param         :  request-活动ID、活动工时、活动配件、活动其它项目、车型列表、车龄定义列表、车辆性质
	 * @return        :  服务活动信息
	 * @throws        :  Exception
	 * LastUpdate     :  2010-06-01
	 */
	public void getActivityIdInfo(){ 
		RequestWrapper request = act.getRequest();
		String activityId=request.getParamValue("activityId");//活动ID
		try {
			TtAsActivityYieldlyPO ttAsActivityYieldlyPO = new TtAsActivityYieldlyPO();
			ttAsActivityYieldlyPO.setActivityId(Long.parseLong(activityId));
			List<TtAsActivityYieldlyPO> list = dao.select(ttAsActivityYieldlyPO);
			String spend = "";
			if(list != null && list.size()>0)
			{
				for (int i = 0;list.size()> i;i++)
				{
					if(list.size() -1 == i)
					{
						spend = spend +list.get(i).getCarYieldly();
					}else
					{
						spend = spend + list.get(i).getCarYieldly() +",";
					}
					
				}
			}
			
			TtAsActivityBean ActivityBean=dao.getServiceActivityByActivityIdInfo(activityId);//查询服务活动信息-明细
			
			List<TtAsWrPartsitemRaplcePO> rp=dao.getRaplceP(activityId);//获取配件
			List<TtAsWrLabouritemRaplcePO> lp=dao.getRaplceL(activityId);//获取工时
			request.setAttribute("rp", rp);
			request.setAttribute("lp", lp);	
			TmBusinessAreaPO areaPO = new TmBusinessAreaPO();
			areaPO.setAreaId(ActivityBean.getSetDirect());
			List<TmBusinessAreaPO> listareaPO = dao.select(areaPO);
			if(listareaPO != null && listareaPO.size()>0)
			{
					String areaName= listareaPO.get(0).getAreaName();
					request.setAttribute("areaName", areaName);
			}
			
			
			
			TtAsActivitySubjectPO ttAsActivitySubjectPO = new TtAsActivitySubjectPO();
			if(spend.length() > 0)
			{
				String sql = "SELECT * from TM_BUSINESS_AREA t where t.AREA_ID in ("+spend+" ) ";
				List<TmBusinessAreaPO> aList= dao.select(TmBusinessAreaPO.class , sql ,null);
				act.setOutData("ttAsActivityYieldlyPO", aList);
			}
		
			ttAsActivitySubjectPO.setSubjectId(Long.parseLong(ActivityBean.getSubjectId()));
			
			ttAsActivitySubjectPO = (TtAsActivitySubjectPO)dao.select(ttAsActivitySubjectPO).get(0);
			List<TtAsActivityBean> ActivityBeanList=dao.getWorkingHoursInfoList(activityId);//活动工时
			List<TtAsActivityBean> ActivityPartsList=dao.getPartsList(activityId);//活动配件
			List<TtAsActivityBean> ActivityNetItemList=dao.getNetItemList(activityId);//活动其它项目
			List<TtAsActivityBean> ActivityVhclMaterialGroupList=dao.getVhclMaterialGroupList(activityId);//车型列表
			List<TtAsActivityBean> ActivitygetActivityAgeList=dao.getActivityAgeList(activityId);//车龄定义列表
			List<TtAsActivityBean> ActivityCharactorList=dao.getActivityCharactorList(activityId);//车辆性质//getMilageActivity
			 List<Map<String, Object>> ActivityMileageList=dao.getMilageActivity(Long.valueOf(activityId));//里程限制
			if(ActivityCharactorList!=null){
			Iterator<TtAsActivityBean> it=ActivityCharactorList.iterator();
			while(it.hasNext()){
				TtAsActivityBean ActivityBeans =it.next();
				if(String.valueOf(Constant.SERVICEACTIVITY_CHARACTOR_01).equals(ActivityBeans.getCarCharactor())){//出租车
					ActivityBeans.setCodeDesc("出租车");
				}else if(String.valueOf(Constant.SERVICEACTIVITY_CHARACTOR_02).equals(ActivityBeans.getCarCharactor())){//私家车
					ActivityBeans.setCodeDesc("私家车");
				}else if(String.valueOf(Constant.SERVICEACTIVITY_CHARACTOR_03).equals(ActivityBeans.getCarCharactor())){
					ActivityBeans.setCodeDesc("公务车");
				}
			}
			}
			request.setAttribute("ActivityBean", ActivityBean);
			request.setAttribute("ActivityBeanList", ActivityBeanList);
			request.setAttribute("ActivityPartsList", ActivityPartsList);
			request.setAttribute("ActivityNetItemList", ActivityNetItemList);
			request.setAttribute("ActivityVhclMaterialGroupList", ActivityVhclMaterialGroupList);
			request.setAttribute("ActivitygetActivityAgeList", ActivitygetActivityAgeList);
			request.setAttribute("ActivityCharactorList", ActivityCharactorList);
			act.setOutData("ActivityMileageList", ActivityMileageList);
			this.serviceActivityManageArea();//服务活动范围TC_CODE查询方法
			TtAsActivityBean VehicleArea  = dao.serviceActivityVehicleArea(activityId);//服务活动范围方法,用于回显
			String vicle=VehicleArea.getVehicleArea();
			String vicle1="";
			String vicle2="";
			if(vicle.indexOf(",")==-1){
				vicle1=vicle;
				vicle2=vicle;
			}else{
			String vv[]=vicle.split(",");
			vicle1=vv[0];
			vicle2=vv[1];
			}
			List<TtAsActivityProjectPO> project = dao.getTtAsActivityProjectPo(activityId);
			BonusDAO bonusDao = new BonusDAO();
			System.out.println(ActivityBean.getNew_id());
			List<Map<String,Object>> listNews = bonusDao.dlrQueryEncourageNewsDetail(Long.parseLong(ActivityBean.getNew_id()));
			request.setAttribute("listNews",listNews);
			request.setAttribute("vicle1", vicle1);
			request.setAttribute("vicle2", vicle2);		
			act.setOutData("ttAsActivitySubjectPO", ttAsActivitySubjectPO);
			act.setOutData("project", project);
			act.setForword(ServiceActivityInfoUrl);
		} catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"服务活动信息详细信息");
			logger.error(logonUser,e1);
			act.setException(e1);
		 }
		}
	
	/**
	 * 
	* @Title: activityIdInfoModifyPage 
	* @author: xyfue
	* @Description: 进入活动修改页面
	* @param     设定文件 
	* @date 2014年10月16日 下午5:18:47 
	* @return void    返回类型 
	* @throws
	 */
	public void activityIdInfoModifyPage(){ 
		RequestWrapper request = act.getRequest();
		String activityId=request.getParamValue("activityId");//活动ID
		try {
			TtAsActivityYieldlyPO ttAsActivityYieldlyPO = new TtAsActivityYieldlyPO();
			ttAsActivityYieldlyPO.setActivityId(Long.parseLong(activityId));
			List<TtAsActivityYieldlyPO> list = dao.select(ttAsActivityYieldlyPO);
			String spend = "";
			if(list != null && list.size()>0)
			{
				for (int i = 0;list.size()> i;i++)
				{
					if(list.size() -1 == i)
					{
						spend = spend +list.get(i).getCarYieldly();
					}else
					{
						spend = spend + list.get(i).getCarYieldly() +",";
					}
					
				}
			}
			
			TtAsActivityBean ActivityBean=dao.getServiceActivityByActivityIdInfo(activityId);//查询服务活动信息-明细
			
			List<TtAsWrPartsitemRaplcePO> rp=dao.getRaplceP(activityId);//获取配件
			List<TtAsWrLabouritemRaplcePO> lp=dao.getRaplceL(activityId);//获取工时
			request.setAttribute("rp", rp);
			request.setAttribute("lp", lp);	
			TmBusinessAreaPO areaPO = new TmBusinessAreaPO();
			areaPO.setAreaId(ActivityBean.getSetDirect());
			List<TmBusinessAreaPO> listareaPO = dao.select(areaPO);
			if(listareaPO != null && listareaPO.size()>0)
			{
					String areaName= listareaPO.get(0).getAreaName();
					request.setAttribute("areaName", areaName);
			}
			
			
			
			TtAsActivitySubjectPO ttAsActivitySubjectPO = new TtAsActivitySubjectPO();
			if(spend.length() > 0)
			{
				String sql = "SELECT * from TM_BUSINESS_AREA t where t.AREA_ID in ("+spend+" ) ";
				List<TmBusinessAreaPO> aList= dao.select(TmBusinessAreaPO.class , sql ,null);
				act.setOutData("ttAsActivityYieldlyPO", aList);
			}
		
			ttAsActivitySubjectPO.setSubjectId(Long.parseLong(ActivityBean.getSubjectId()));
			
			ttAsActivitySubjectPO = (TtAsActivitySubjectPO)dao.select(ttAsActivitySubjectPO).get(0);
			List<TtAsActivityBean> ActivityBeanList=dao.getWorkingHoursInfoList(activityId);//活动工时
			List<TtAsActivityBean> ActivityPartsList=dao.getPartsList(activityId);//活动配件
			List<TtAsActivityBean> ActivityNetItemList=dao.getNetItemList(activityId);//活动其它项目
			List<TtAsActivityBean> ActivityVhclMaterialGroupList=dao.getVhclMaterialGroupList(activityId);//车型列表
			List<TtAsActivityBean> ActivitygetActivityAgeList=dao.getActivityAgeList(activityId);//车龄定义列表
			List<TtAsActivityBean> ActivityCharactorList=dao.getActivityCharactorList(activityId);//车辆性质//getMilageActivity
			 List<Map<String, Object>> ActivityMileageList=dao.getMilageActivity(Long.valueOf(activityId));//里程限制
			if(ActivityCharactorList!=null){
			Iterator<TtAsActivityBean> it=ActivityCharactorList.iterator();
			while(it.hasNext()){
				TtAsActivityBean ActivityBeans =it.next();
				if(String.valueOf(Constant.SERVICEACTIVITY_CHARACTOR_01).equals(ActivityBeans.getCarCharactor())){//出租车
					ActivityBeans.setCodeDesc("出租车");
				}else if(String.valueOf(Constant.SERVICEACTIVITY_CHARACTOR_02).equals(ActivityBeans.getCarCharactor())){//私家车
					ActivityBeans.setCodeDesc("私家车");
				}else if(String.valueOf(Constant.SERVICEACTIVITY_CHARACTOR_03).equals(ActivityBeans.getCarCharactor())){
					ActivityBeans.setCodeDesc("公务车");
				}
			}
			}
			request.setAttribute("ActivityBean", ActivityBean);
			request.setAttribute("ActivityBeanList", ActivityBeanList);
			request.setAttribute("ActivityPartsList", ActivityPartsList);
			request.setAttribute("ActivityNetItemList", ActivityNetItemList);
			request.setAttribute("ActivityVhclMaterialGroupList", ActivityVhclMaterialGroupList);
			request.setAttribute("ActivitygetActivityAgeList", ActivitygetActivityAgeList);
			request.setAttribute("ActivityCharactorList", ActivityCharactorList);
			act.setOutData("ActivityMileageList", ActivityMileageList);
			this.serviceActivityManageArea();//服务活动范围TC_CODE查询方法
			TtAsActivityBean VehicleArea  = dao.serviceActivityVehicleArea(activityId);//服务活动范围方法,用于回显
			String vicle=VehicleArea.getVehicleArea();
			String vicle1="";
			String vicle2="";
			if(vicle.indexOf(",")==-1){
				vicle1=vicle;
				vicle2=vicle;
			}else{
			String vv[]=vicle.split(",");
			vicle1=vv[0];
			vicle2=vv[1];
			}
			List<TtAsActivityProjectPO> project = dao.getTtAsActivityProjectPo(activityId);
			BonusDAO bonusDao = new BonusDAO();
			System.out.println(ActivityBean.getNew_id());
			List<Map<String,Object>> listNews = bonusDao.dlrQueryEncourageNewsDetail(Long.parseLong(ActivityBean.getNew_id()));
			request.setAttribute("listNews",listNews);
			request.setAttribute("vicle1", vicle1);
			request.setAttribute("vicle2", vicle2);		
			act.setOutData("ttAsActivitySubjectPO", ttAsActivitySubjectPO);
			SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
			String subEndDate = sf.format(ttAsActivitySubjectPO.getSubjectEndDate());
			act.setOutData("subEndDate", subEndDate);
			act.setOutData("endDate", ActivityBean.getEnddate());
			
			act.setOutData("project", project);
			act.setForword(ServiceActivityInfoModifyUrl);
		} catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"服务活动信息详细信息");
			logger.error(logonUser,e1);
			act.setException(e1);
		 }
		}
	
	public void getActivityIdInfoAdd(){ 
		RequestWrapper request = act.getRequest();
		String activityId=request.getParamValue("activityId");//活动ID
		try {
			TtAsActivityYieldlyPO ttAsActivityYieldlyPO = new TtAsActivityYieldlyPO();
			ttAsActivityYieldlyPO.setActivityId(Long.parseLong(activityId));
			List<TtAsActivityYieldlyPO> list = dao.select(ttAsActivityYieldlyPO);
			String spend = "";
			if(list != null && list.size()>0)
			{
				for (int i = 0;list.size()> i;i++)
				{
					if(list.size() -1 == i)
					{
						spend = spend +list.get(i).getCarYieldly();
					}else
					{
						spend = spend + list.get(i).getCarYieldly() +",";
					}
					
				}
			}
			
			TtAsActivityBean ActivityBean=dao.getServiceActivityByActivityIdInfo(activityId);//查询服务活动信息-明细
			
			List<TtAsWrPartsitemRaplcePO> rp=dao.getRaplceP(activityId);//获取配件
			List<TtAsWrLabouritemRaplcePO> lp=dao.getRaplceL(activityId);//获取工时
			request.setAttribute("rp", rp);
			request.setAttribute("lp", lp);	
			TmBusinessAreaPO areaPO = new TmBusinessAreaPO();
			areaPO.setAreaId(ActivityBean.getSetDirect());
			List<TmBusinessAreaPO> listareaPO = dao.select(areaPO);
			if(listareaPO != null && listareaPO.size()>0)
			{
					String areaName= listareaPO.get(0).getAreaName();
					request.setAttribute("areaName", areaName);
			}
			
			
			
			TtAsActivitySubjectPO ttAsActivitySubjectPO = new TtAsActivitySubjectPO();
			if(spend.length() > 0)
			{
				String sql = "SELECT * from TM_BUSINESS_AREA t where t.AREA_ID in ("+spend+" ) ";
				List<TmBusinessAreaPO> aList= dao.select(TmBusinessAreaPO.class , sql ,null);
				act.setOutData("ttAsActivityYieldlyPO", aList);
			}
		
			ttAsActivitySubjectPO.setSubjectId(Long.parseLong(ActivityBean.getSubjectId()));
			ttAsActivitySubjectPO = (TtAsActivitySubjectPO)dao.select(ttAsActivitySubjectPO).get(0);
			List<TtAsActivityBean> ActivityBeanList=dao.getWorkingHoursInfoList(activityId);//活动工时
			List<TtAsActivityBean> ActivityPartsList=dao.getPartsList(activityId);//活动配件
			List<TtAsActivityBean> ActivityNetItemList=dao.getNetItemList(activityId);//活动其它项目
			List<TtAsActivityBean> ActivityVhclMaterialGroupList=dao.getVhclMaterialGroupList(activityId);//车型列表
			List<TtAsActivityBean> ActivitygetActivityAgeList=dao.getActivityAgeList(activityId);//车龄定义列表
			List<TtAsActivityBean> ActivityCharactorList=dao.getActivityCharactorList(activityId);//车辆性质//getMilageActivity
			 List<Map<String, Object>> ActivityMileageList=dao.getMilageActivity(Long.valueOf(activityId));//里程限制
			if(ActivityCharactorList!=null){
			Iterator<TtAsActivityBean> it=ActivityCharactorList.iterator();
			while(it.hasNext()){
				TtAsActivityBean ActivityBeans =it.next();
				if(String.valueOf(Constant.SERVICEACTIVITY_CHARACTOR_01).equals(ActivityBeans.getCarCharactor())){//出租车
					ActivityBeans.setCodeDesc("出租车");
				}else if(String.valueOf(Constant.SERVICEACTIVITY_CHARACTOR_02).equals(ActivityBeans.getCarCharactor())){//私家车
					ActivityBeans.setCodeDesc("私家车");
				}else if(String.valueOf(Constant.SERVICEACTIVITY_CHARACTOR_03).equals(ActivityBeans.getCarCharactor())){
					ActivityBeans.setCodeDesc("公务车");
				}
			}
			}
			request.setAttribute("ActivityBean", ActivityBean);
			request.setAttribute("ActivityBeanList", ActivityBeanList);
			request.setAttribute("ActivityPartsList", ActivityPartsList);
			request.setAttribute("ActivityNetItemList", ActivityNetItemList);
			request.setAttribute("ActivityVhclMaterialGroupList", ActivityVhclMaterialGroupList);
			request.setAttribute("ActivitygetActivityAgeList", ActivitygetActivityAgeList);
			request.setAttribute("ActivityCharactorList", ActivityCharactorList);
			act.setOutData("ActivityMileageList", ActivityMileageList);
			this.serviceActivityManageArea();//服务活动范围TC_CODE查询方法
			TtAsActivityBean VehicleArea  = dao.serviceActivityVehicleArea(activityId);//服务活动范围方法,用于回显
			String vicle=VehicleArea.getVehicleArea();
			String vicle1="";
			String vicle2="";
			if(vicle.indexOf(",")==-1){
				vicle1=vicle;
				vicle2=vicle;
			}else{
			String vv[]=vicle.split(",");
			vicle1=vv[0];
			vicle2=vv[1];
			}
			List<TtAsActivityProjectPO> project = dao.getTtAsActivityProjectPo(activityId);
			BonusDAO bonusDao = new BonusDAO();
			System.out.println(ActivityBean.getNew_id());
			List<Map<String,Object>> listNews = bonusDao.dlrQueryEncourageNewsDetail(Long.parseLong(ActivityBean.getNew_id()));
			request.setAttribute("listNews",listNews);
			request.setAttribute("vicle1", vicle1);
			request.setAttribute("vicle2", vicle2);		
			act.setOutData("ttAsActivitySubjectPO", ttAsActivitySubjectPO);
			act.setOutData("project", project);
			act.setOutData("activityId",activityId);
			act.setForword(ServiceActivityInfoAdd);
		} catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"服务活动信息详细信息");
			logger.error(logonUser,e1);
			act.setException(e1);
		 }
		}
	
	
		/**
		 * Function       :  查询服务活动信息-车辆信息
		 * @param         :  request-活动ID
		 * @return        :  服务活动信息
		 * @throws        :  Exception
		 * LastUpdate     :  2010-06-01
		 */
	
		public void getActivityVehicleListInfo(){ 
			RequestWrapper request = act.getRequest();
			String activityId=request.getParamValue("activityId");//活动ID
			try {
				Integer curPage = request.getParamValue("curPage") !=null ? Integer.parseInt(request.getParamValue("curPage")) : 1;
				PageResult<Map<String, Object>> ps = dao.getActivityVehicleList(activityId,request.getParamValue("vin"),curPage,Constant.PAGE_SIZE );
				act.setOutData("ps", ps);
				request.setAttribute("activityId", activityId);
				act.setForword(ServiceActivityVehicleInfoUrl);
			} catch(Exception e){
				BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"服务活动信息详细信息--车辆信息");
				logger.error(logonUser,e1);
				act.setException(e1);
			}
	}
		/**
		 * Function       :  查询服务活动信息-车辆信息初始化页面
		 * @param         :  request-活动ID
		 * @return        :  服务活动信息
		 * @throws        :  Exception
		 * LastUpdate     :  2010-06-01
		 */
		public void getActivityVehicleListInfoInit(){ 
			RequestWrapper request = act.getRequest();
			String activityId=request.getParamValue("activityId");//活动ID
			request.setAttribute("activityId", activityId);
			act.setForword(ServiceActivityVehicleInfoUrl);
		}
		
		
		
		public void getActivityMilage(){
			RequestWrapper request = act.getRequest();
			String activeId  = request.getParamValue("activeId");//获取服务活动ID
			String type  = request.getParamValue("type");
			act.setOutData("type", type);
			Integer curPage = request.getParamValue("curPage") !=null ? Integer.parseInt(request.getParamValue("curPage")) : 1;
			try {
				TtAsActivityMilagePO milagePO = new TtAsActivityMilagePO();
				milagePO.setActivityId(Long.parseLong(activeId));
				List<TtAsActivityMilagePO> list= dao.select(milagePO);
				if(list.size() > 0)
				{
					act.setOutData("MILAGE_START", list.get(0).getMilageStart());
					act.setOutData("MILAGE_END", list.get(0).getMilageEnd());
				}
				act.setOutData("activeId", activeId);
				act.setForword(milageURL);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		public void getActivityMilagejude()
		{
			RequestWrapper request = act.getRequest();
			String activityId  = request.getParamValue("activityId");//获取服务活动ID
			try {
				TtAsActivityMilagePO milagePO = new TtAsActivityMilagePO();
				milagePO.setActivityId(Long.parseLong(activityId));
				if(dao.select(milagePO).size() == 0)
				{
					act.setOutData("ret","false");
				}else
				{
					act.setOutData("ret","true");
				} 
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
		public void insertActivityMilage() throws NumberFormatException, Exception{
			try{
				RequestWrapper request = act.getRequest();
				String  activeId = request.getParamValue("activeId");
				String MILAGE_START = request.getParamValue("MILAGE_START");
				String MILAGE_END = request.getParamValue("MILAGE_END");
				dao.deleteMilage(activeId);
				TtAsActivityMilagePO milagePO = new TtAsActivityMilagePO();
				milagePO.setId(Long.parseLong(SequenceManager.getSequence(""))) ;
				milagePO.setActivityId(Long.parseLong(activeId));
				milagePO.setMilageStart(Double.parseDouble(MILAGE_START));
				milagePO.setMilageEnd(Double.parseDouble(MILAGE_END));
				dao.insert(milagePO);
				TtAsActivityPO activityPO = new TtAsActivityPO();
				activityPO.setActivityId(Long.parseLong(activeId));
				TtAsActivityPO activityPO1 = new TtAsActivityPO();
				activityPO1.setMilageConfine(0);
				dao.update(activityPO, activityPO1);
				
			}
			catch(Exception e){
				BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"服务活动信息详细信息--车辆信息");
				logger.error(logonUser,e1);
				act.setException(e1);
			}
		}
		
		public void ServiceActivityManageModify() throws Exception
		{
			RequestWrapper request = act.getRequest();
			String activityId=request.getParamValue("activityId");//主题ID
			String modifyEndDate=request.getParamValue("modifyEndDate");//主题ID
			
			TtAsActivityPO ttAsActivityPO = new TtAsActivityPO();
			ttAsActivityPO.setUpdateBy(logonUser.getUserId());
			ttAsActivityPO.setUpdateDate(new Date());
			DateFormat format = new SimpleDateFormat("yyyy-MM-dd"); 
			Date parseDate1 = format.parse(modifyEndDate);
			ttAsActivityPO.setEnddate(parseDate1);;
			
			dao.serviceActivityManageUpdate(activityId, ttAsActivityPO);
			act.setOutData("returnValue", 1);//returnValue 值：1，表示成功
			
			
		}
}