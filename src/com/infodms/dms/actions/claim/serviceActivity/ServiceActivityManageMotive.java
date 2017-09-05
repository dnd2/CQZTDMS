package com.infodms.dms.actions.claim.serviceActivity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.bean.ttAsActivitySubjectBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.DateUtil;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.dao.claim.other.BonusDAO;
import com.infodms.dms.dao.claim.serviceActivity.ServiceActivityManageMotiveDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TcUserPO;
import com.infodms.dms.po.TmFineNewsPO;
import com.infodms.dms.po.TtAsActivityPO;
import com.infodms.dms.po.TtAsActivitySubjectPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;
	/**
	 * 
	 * @ClassName     : ServiceActivityManageMotive 
	 * @Description   : TODO 
	 * @author        : Administrator
	 * CreateDate     : 2013-4-1
	 */

public class ServiceActivityManageMotive
{
	private Logger logger = Logger.getLogger(ServiceActivityManageMotive.class);
	private ServiceActivityManageMotiveDao dao = ServiceActivityManageMotiveDao.getInstance();
	private ActionContext act = ActionContext.getContext();//获取ActionContext
	private AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
	private final String serviceActivityManageSummaryDealerSearchInitUrl = "/jsp/claim/serviceActivity/ServiceActivityManageMotive.jsp";//查询页面
	private final String serviceActivityManageSummaryDealerSearchAddInitUrl = "/jsp/claim/serviceActivity/ServiceActivityManageMotiveAdd.jsp";//查询页面
	private final String serviceActivityManageSummaryDealerSearchselInitUrl = "/jsp/claim/serviceActivity/serviceActivityTopicMotiveView.jsp";//查询页面
	private final String serviceActivityManageSummaryModifyUrl = "/jsp/claim/serviceActivity/serviceActivityTopicModify.jsp";//活动管理时间修改页面
	public void serviceActivityManageMotiveInit()
	{
		try
		{
			act.setOutData("typeleng", 0);
			act.setForword(serviceActivityManageSummaryDealerSearchInitUrl);
		}
		catch (Exception e)
		{
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"服务活动主题管理---服务活动服务活动主题管理页面初始化");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
		
	}
	
	public void serviceActivityManageMarkInit()
	{
		try
		{
			act.setOutData("typeleng", 1);
			act.setForword(serviceActivityManageSummaryDealerSearchInitUrl);
		}
		catch (Exception e)
		{
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"服务活动主题管理---服务活动服务活动主题管理页面初始化");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
		
	}
	/**
	 * 服务活动主题查询
	 * @Title      : 
	 * @Description: TODO 
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-1
	 */
	public void ServiceActivityManageMotiveQuery()
	{
		try
		{
			
			RequestWrapper request= act.getRequest();
			String activityCode = CommonUtils.checkNull(request.getParamValue("activityCode"));
			String activityType = CommonUtils.checkNull(request.getParamValue("activityType"));
			String activity_name = CommonUtils.checkNull(request.getParamValue("activity_name"));
			String checkSDate = request.getParamValue("checkSDate");
			String checkEDate = request.getParamValue("checkEDate");
			String typeleng = request.getParamValue("typeleng");
			ttAsActivitySubjectBean ttAsActivityBean = new ttAsActivitySubjectBean();
			ttAsActivityBean.setSubjectNo(activityCode);
			if(!activityType.equals(""))
			{
				ttAsActivityBean.setActivityType(Integer.parseInt(activityType));
			}
			ttAsActivityBean.setSubjectName(activity_name);
			ttAsActivityBean.setSubjectStartDate(checkSDate);
			ttAsActivityBean.setSubjectEndDate(checkEDate);
			ttAsActivityBean.setRemark(typeleng);
			Integer curPage = request.getParamValue("curPage") !=null ? Integer.parseInt(request.getParamValue("curPage")) : 1;
			PageResult<Map<String, Object>> ps = dao.getAllServiceActivityManageMotive(ttAsActivityBean, curPage,Constant.PAGE_SIZE);
			act.setOutData("ps", ps);
			act.setForword(serviceActivityManageSummaryDealerSearchInitUrl);
		}
		catch (Exception e)
		{
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"服务主题活动查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * 活动主题删除
	 * @Title      : 
	 * @Description: TODO 
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-3
	 */
	public void ServiceActivityManageMotiveDelete()
	{
		RequestWrapper request = act.getRequest();
		String subject_id=request.getParamValue("subject_id");//主题ID
		TtAsActivityPO activityPO = new TtAsActivityPO();
		activityPO.setSubjectId(Long.parseLong(subject_id));
		activityPO.setIsDel(0);
		List<TtAsActivityPO> list = dao.select(activityPO);
		if(list!=null && list.size()>0)
		{
			act.setOutData("returnValue", 0);//returnValue 值：1，表示成功
		}else
		{
			TtAsActivitySubjectPO ttAsActivitySubjectPO = new TtAsActivitySubjectPO();
			ttAsActivitySubjectPO.setUpdateBy(logonUser.getUserId());
			ttAsActivitySubjectPO.setUpdateDate(new Date());
			ttAsActivitySubjectPO.setIsDel(Integer.parseInt(Constant.IS_DEL_01));
			dao.serviceActivityManageDelete(subject_id, ttAsActivitySubjectPO);
			act.setOutData("returnValue", 1);//returnValue 值：1，表示成功
		}
		
	}
	
	
	public void ServiceActivityManageMotivejude()
	{
		RequestWrapper request = act.getRequest();
		String activityNO= request.getParamValue("activityName");
		String SUBJECT_NAME= request.getParamValue("activityName2");
		
		TtAsActivitySubjectPO activitySubjectPO = new TtAsActivitySubjectPO();
		activitySubjectPO.setSubjectNo(activityNO);
		TtAsActivitySubjectPO subjectPO = new TtAsActivitySubjectPO();
		subjectPO.setSubjectName(SUBJECT_NAME);
		List<TtAsActivitySubjectPO> list= dao.select(activitySubjectPO);
		List<TtAsActivitySubjectPO> list1 =dao.select(subjectPO);
		if((list != null && list.size()>0)||(list1 != null && list1.size()>0))
		{
			act.setOutData("back", 0);
		}else{
			act.setOutData("back", 1);
		}
		
		
	}
	
	/**
	 * 
	 * @Title      : 服务活动服务活动主题管理新增页面初始化
	 * @Description: TODO 
	 * @param      :       
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-3
	 */
	public void serviceActivityManageMotiveAddInit()
	{
		try
		{
			act.setOutData("user", dao.selectUser());
			 RequestWrapper request= act.getRequest();
			 act.setOutData("typeleng",request.getParamValue("typeleng") );
			act.setForword(serviceActivityManageSummaryDealerSearchAddInitUrl);
		}
		catch (Exception e)
		{
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"服务活动主题管理---服务活动服务活动主题管理新增页面初始化");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	@SuppressWarnings("unchecked")
	public void ServiceActivityManageMotiveAdd() throws Exception
	{
		RequestWrapper request = act.getRequest();
		String activityName = request.getParamValue("activityName");
		String activityName2 = request.getParamValue("activityName2");
		String activityType = request.getParamValue("activityType");
		String checkSDate = request.getParamValue("checkSDate");
		String checkEDate = request.getParamValue("checkEDate");
		logger.info("后台接受开始时间："+checkSDate+"===="+"后台接受结束时间："+checkEDate);
		String factStartDate = request.getParamValue("factStartDate");
		String factEndDate = request.getParamValue("factEndDate");
		String single_num = request.getParamValue("single_num");
		String uploadPrePeriod = request.getParamValue("uploadPrePeriod");
		String select2 = request.getParamValue("select2");
		long newid = Utility.getLong(SequenceManager.getSequence(""));
		
		String[] newsIds = request.getParamValues("newsId");
		if(newsIds!=null){
			for(int i = 0; i < newsIds.length; i++){
				TmFineNewsPO tfn = new TmFineNewsPO();
				tfn.setFineNewsId(newid);
				tfn.setNewsId(Long.valueOf(newsIds[i]));
				tfn.setCreateBy(logonUser.getUserId());
				tfn.setCreateDate(new Date());
				dao.insert(tfn);
			}
		}
		
		
		TtAsActivitySubjectPO activitySubjectPO = new TtAsActivitySubjectPO();
		long activityId = Long.parseLong(SequenceManager.getSequence(""));
		activitySubjectPO.setSubjectId(activityId);
		activitySubjectPO.setCompanyId(logonUser.getCompanyId());
		activitySubjectPO.setNewsId(newid);
		activitySubjectPO.setSubjectNo(activityName);
		activitySubjectPO.setSubjectName(activityName2);
		activitySubjectPO.setActivityType(Integer.parseInt(activityType));
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd"); 
		Date parseDate1 = format.parse(checkSDate);
		Date parseDate2 = format.parse(checkEDate);
		Date parseDate3 = format.parse(factStartDate);
		Date parseDate4 = format.parse(factEndDate);
		activitySubjectPO.setSubjectStartDate(parseDate1);
		activitySubjectPO.setSubjectEndDate(parseDate2);
		
		activitySubjectPO.setFactStartDate(parseDate3);
		activitySubjectPO.setFactEndDate(parseDate4);
		activitySubjectPO.setActivityNum(Integer.parseInt(single_num));
		activitySubjectPO.setDays(Integer.parseInt(uploadPrePeriod));
		activitySubjectPO.setDutyPerson(Long.parseLong(select2));
		activitySubjectPO.setCreateDate(new Date());
		activitySubjectPO.setCreateBy(logonUser.getUserId());
		dao.insert(activitySubjectPO);
		act.setOutData("success", "yes");//returnValue 值：1，表示成功
	}
	public static void main(String[] args) {
		String checkSDate="2014-6-12";
		Date parseDate = CommonUtils.parseDate(checkSDate);
	}
	public void ServiceActivityManageMotiveSelcet()
	{
		RequestWrapper request = act.getRequest();
		String subject_id= request.getParamValue("activityId");
		TtAsActivitySubjectPO activitySubjectPO = new TtAsActivitySubjectPO();
		activitySubjectPO.setSubjectId(Long.parseLong(subject_id));
		List<TtAsActivitySubjectPO> list= dao.select(activitySubjectPO);
		activitySubjectPO = list.get(0);
		Long userId = activitySubjectPO.getDutyPerson();
		if(userId != null && userId > 0)
		{
			TcUserPO userPO = new TcUserPO();
			userPO.setUser_id(userId);
			List<TcUserPO> userList= dao.select(userPO);
			if(userList != null && userList.size()>0){
				userPO = userList.get(0);
				act.setOutData("username", userPO.getName());
			}
		}
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		String stratDate = sf.format(activitySubjectPO.getSubjectStartDate());
		String endDate = sf.format(activitySubjectPO.getSubjectEndDate());
		String factStartDate = null;
		String factEndDate = null;
		if( null != activitySubjectPO.getFactStartDate() && !"".equals(activitySubjectPO.getFactStartDate())){
			factStartDate = sf.format(activitySubjectPO.getFactStartDate());
		}
        if(null != activitySubjectPO.getFactEndDate() && !"".equals(activitySubjectPO.getFactEndDate())) {
        	factEndDate = sf.format(activitySubjectPO.getFactEndDate());
        }
		
		Long newsid= activitySubjectPO.getNewsId();
		BonusDAO bonusDao = new BonusDAO();
		List<Map<String, Object>> listNews = bonusDao.dlrQueryEncourageNewsDetail(newsid);
		act.setOutData("activitySubjectPO",activitySubjectPO);
		act.setOutData("stratDate",stratDate);
		act.setOutData("endDate",endDate);
		act.setOutData("factStartDate",factStartDate);
		act.setOutData("factEndDate",factEndDate);
		request.setAttribute("listNews", listNews);
		act.setForword(serviceActivityManageSummaryDealerSearchselInitUrl);
	}
	
	/**
	 * 
	* @Title: ServiceActivityManageModify 
	* @author: xyfue
	* @Description: 进入活动管理修改界面 
	* @param     设定文件 
	* @date 2014年10月16日 下午2:15:18 
	* @return void    返回类型 
	* @throws
	 */
	public void ServiceActivityManageModifyPage()
	{
		RequestWrapper request = act.getRequest();
		String subject_id= request.getParamValue("activityId");
		TtAsActivitySubjectPO activitySubjectPO = new TtAsActivitySubjectPO();
		activitySubjectPO.setSubjectId(Long.parseLong(subject_id));
		List<TtAsActivitySubjectPO> list= dao.select(activitySubjectPO);
		activitySubjectPO = list.get(0);
		Long userId = activitySubjectPO.getDutyPerson();
		if(userId != null && userId > 0)
		{
			TcUserPO userPO = new TcUserPO();
			userPO.setUser_id(userId);
			List<TcUserPO> userList= dao.select(userPO);
			if(userList != null && userList.size()>0){
				userPO = userList.get(0);
				act.setOutData("username", userPO.getName());
			}
		}
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		String stratDate = sf.format(activitySubjectPO.getSubjectStartDate());
		String endDate = sf.format(activitySubjectPO.getSubjectEndDate());
		String factStartDate = null;
		String factEndDate = null;
		if( null != activitySubjectPO.getFactStartDate() && !"".equals(activitySubjectPO.getFactStartDate())){
			factStartDate = sf.format(activitySubjectPO.getFactStartDate());
		}
        if(null != activitySubjectPO.getFactEndDate() && !"".equals(activitySubjectPO.getFactEndDate())) {
        	factEndDate = sf.format(activitySubjectPO.getFactEndDate());
        }
		
		Long newsid= activitySubjectPO.getNewsId();
		BonusDAO bonusDao = new BonusDAO();
		List<Map<String, Object>> listNews = bonusDao.dlrQueryEncourageNewsDetail(newsid);
		act.setOutData("activitySubjectPO",activitySubjectPO);
		act.setOutData("stratDate",stratDate);
		act.setOutData("endDate",endDate);
		act.setOutData("factStartDate",factStartDate);
		act.setOutData("factEndDate",factEndDate);
		request.setAttribute("listNews", listNews);
		act.setForword(serviceActivityManageSummaryModifyUrl);
	}
	
	/**
	 * 
	* @Title: ServiceActivityManageModify 
	* @author: xyfue
	* @Description: 修改活动
	* @param     设定文件 
	* @date 2014年10月16日 下午4:02:34 
	* @return void    返回类型 
	* @throws
	 */
	public void ServiceActivityManageModify() throws Exception
	{
		RequestWrapper request = act.getRequest();
		String subject_id=request.getParamValue("subject_id");//主题ID
		String modifyEndDate=request.getParamValue("modifyEndDate");//主题ID
		String modifyFactEndDate=request.getParamValue("modifyFactEndDate");//主题ID
		System.err.println(modifyEndDate);
		System.err.println(modifyFactEndDate);
		
		
		TtAsActivitySubjectPO ttAsActivitySubjectPO = new TtAsActivitySubjectPO();
		ttAsActivitySubjectPO.setUpdateBy(logonUser.getUserId());
		ttAsActivitySubjectPO.setUpdateDate(new Date());
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd"); 
		Date parseDate1 = format.parse(modifyEndDate);
		Date parseDate2 = format.parse(modifyFactEndDate);
		ttAsActivitySubjectPO.setSubjectEndDate(parseDate1);
		ttAsActivitySubjectPO.setFactEndDate(parseDate2);
		
		dao.serviceActivityManageUpdate(subject_id, ttAsActivitySubjectPO);
		act.setOutData("returnValue", 1);//returnValue 值：1，表示成功
		
		
	}
}
