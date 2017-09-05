/**********************************************************************
* <pre>
* FILE : ServiceActivityManageSummary.java
* CLASS : ServiceActivityManageSummary
*
* AUTHOR : PGM
*
* FUNCTION :服务活动管理---服务活动计划分析
*
*
*======================================================================
* CHANGE HISTORY LOG
*----------------------------------------------------------------------
* MOD. NO.| DATE     | NAME | REASON | CHANGE REQ.
*----------------------------------------------------------------------
*         |2010-06-13| PGM  | Created |
* DESCRIPTION:
* </pre>
***********************************************************************/
/**
 * $Id: ServiceActivityManageSummary.java,v 1.1 2010/08/16 01:44:11 yuch Exp $
 */
package com.infodms.dms.actions.claim.serviceActivity;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.actions.common.FileUploadManager;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.bean.TtAsActivityBean;
import com.infodms.dms.bean.ttAsActivitySubjectBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.common.getCompanyId.GetOemcompanyId;
import com.infodms.dms.dao.claim.serviceActivity.ServiceActivityManageSummaryDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.FsFileuploadPO;
import com.infodms.dms.po.TtAsActivityConductPO;
import com.infodms.dms.po.TtAsActivityDealerPO;
import com.infodms.dms.po.TtAsActivityEvaluatePO;
import com.infodms.dms.po.TtAsActivityPerPO;
import com.infodms.dms.po.TtAsActivityTypePO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;
import com.infoservice.po3.core.context.DBService;
/**
 * Function       :  服务活动管理---服务活动评估
 * @author        :  PGM
 * CreateDate     :  2010-06-13
 * @version       :  0.1
 */
public class ServiceActivityManageSummary {
	private Logger logger = Logger.getLogger(ServiceActivityManageSummary.class);
	private ServiceActivityManageSummaryDao dao = ServiceActivityManageSummaryDao.getInstance();
	private ActionContext act = ActionContext.getContext();//获取ActionContext
	private AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
	private final String serviceActivityManageSummaryDealerSearchInitUrl = "/jsp/claim/serviceActivity/serviceActivityManageSummaryDealerSearch.jsp";
	private final String ServiceActivityInitUrl = "/jsp/claim/serviceActivity/serviceActivityManageSummary.jsp";//查询页面
	private final String ServiceActivityOptionUrl = "/jsp/claim/serviceActivity/serviceActivityManageSummaryOption.jsp";//编辑页面
	/**
	 * Function       :  服务活动管理---服务活动评估页面初始化
	 * @param         :  
	 * @return        :  serviceActivityManageSummaryInit
	 * @throws        :  Exception
	 * LastUpdate     :  2010-06-13
	 */
	public void serviceActivityManageSummaryInit(){
		try {
			act.setForword(ServiceActivityInitUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"服务活动管理---服务活动评估页面初始化");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * Function       :  根据条件查询服务活动管理---服务活动评估中符合条件的信息
	 * @param         :  request-活动编号、活动开始日期、活动结束日期
	 * @return        :  服务活动管理
	 * @throws        :  Exception
	 * LastUpdate     :  2010-06-13
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
			ttAsActivitySubjectBean ttAsActivityBean = new ttAsActivitySubjectBean();
			ttAsActivityBean.setSubjectNo(activityCode);
			if(!activityType.equals(""))
			{
				ttAsActivityBean.setActivityType(Integer.parseInt(activityType));
			}
			ttAsActivityBean.setSubjectName(activity_name);
			ttAsActivityBean.setSubjectStartDate(checkSDate);
			ttAsActivityBean.setSubjectEndDate(checkEDate);
			ttAsActivityBean.setRemark(logonUser.getDealerId());
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
	
	
	public void serviceActivityManageSummaryQuery(){
		try {
			RequestWrapper request = act.getRequest();
			String subject_no = request.getParamValue("activityCode");    //主题编号
			String subject_start_date = request.getParamValue("checkSDate");          //活动开始日期
			String subject_end_date = request.getParamValue("checkEDate");              //活动结束日期
			String activityType = request.getParamValue("activityType");
			String subject_name = request.getParamValue("activity_name");
			String subjectid =  request.getParamValue("subjectid");
			
			Long companyId=GetOemcompanyId.getOemCompanyId(logonUser);      //公司ID
			TtAsActivityBean ActivityBean = new TtAsActivityBean();
			ActivityBean.setSubject_no(subject_no);
			ActivityBean.setSubject_start_date(subject_start_date);
			ActivityBean.setSubject_end_date(subject_end_date);
			ActivityBean.setActivityType(activityType);
			ActivityBean.setSubject_name(subject_name);
			ActivityBean.setSubjectId(subjectid);
			
			
			ActivityBean.setDealerId(logonUser.getDealerId());
			ActivityBean.setCompanyId(String.valueOf(companyId));
			Integer curPage = request.getParamValue("curPage") !=null ? Integer.parseInt(request.getParamValue("curPage")) : 1;
			PageResult<Map<String, Object>> ps = dao.getServiceActivityManageSummaryQuery(ActivityBean,curPage,Constant.PAGE_SIZE );
			act.setOutData("ps", ps);
			act.setForword(ServiceActivityInitUrl);
		}catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"服务活动管理---服务活动评估");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	
	
	
	/**
	 * Function         : 服务活动管理---服务活动评估[编辑]查询方法：状态为：[已经下发]的活动
	 * @param           : request-活动ID
	 * @return          : 满足条件的服务活动管理信息
	 * @throws          : Exception
	 * LastUpdate       : 2010-06-13
	 */
	public void	serviceActivitySummaryQuery(){
		try {
			RequestWrapper request = act.getRequest();
			String subjectid = request.getParamValue("subjectid");    //活动ID
			String dealerId = logonUser.getDealerId();//经销商ID
			String sql = "select  t.root_org_name  from vw_org_dealer_service t where t.root_dealer_id="+dealerId;
			List<Map<String,Object>> listt = dao.pageQuery(sql, null, dao.getFunName());
			//ResultSet rs= conn.prepareStatement(sql).executeQuery();
//			if(rs.next())
//			{
//				String org_name= rs.getString(1);
//				act.setOutData("org_name", org_name);
//			}
			if(listt!=null && listt.size()>0){
				act.setOutData("org_name", listt.get(0).get("ROOT_ORG_NAME"));
			}
			//conn.prepareStatement(sql).executeQuery();
			TtAsActivityBean ttAsActivityBean=dao.serviceActivitySummaryQuery(subjectid,dealerId);//调用服务活动评估[编辑]查询方法
			String  Subject_end_date= ttAsActivityBean.getSubject_end_date();
			int  days = ttAsActivityBean.getDays();
			Date end_date = Utility.getDate(Subject_end_date, 1);
			if(end_date.after(new Date()))
			{
				act.setOutData("typeDate","NO");
			}else
			{
				act.setOutData("typeDate","YES");
			}
			act.setOutData("ReStatus",""+ttAsActivityBean.getReStatus() );
			
			if(days <= 0 )
			{
				act.setOutData("typeDate1","YES");
			}else
			{
				act.setOutData("typeDate1","NO");
			}
			
			
			List<Map<String,Object>> SList= dao.SType(subjectid,dealerId);
			List<Map<String,Object>> ZList= dao.ZType(subjectid,dealerId);
			
			StringBuffer sb = new StringBuffer();
			sb.append("select * from tt_as_activity_per t where t.SUMMARY_ID ="+subjectid+" and t.DEALER_ID = "+dealerId+"   order by t.CODE_TYPE ");
		    List<TtAsActivityPerPO> perList= dao.select(TtAsActivityPerPO.class, sb.toString(),null);
			act.setOutData("perList", perList);
			
			TtAsActivityConductPO conductPO = new TtAsActivityConductPO();
			conductPO.setSummaryId(Long.parseLong(subjectid));
			List<TtAsActivityConductPO> conList= dao.select(conductPO);

			sb = new StringBuffer();
			sb.append("select t.W_name WNAME , t.W_add WADD,to_char(t.publish_date,'yyyy-mm-dd') PUBLISHDATE,t.conduct_cont  CONDUCTCONT ,t.media_name from TT_AS_activity_conduct t where  t.DEALER_ID = "+dealerId+"   and  t.SUMMARY_ID ="+subjectid+"");
			act.setOutData("conList", dao.pageQuery(sb.toString(),null, dao.getFunName()));

			
			if(SList.size() > 0)
			{
				List<Map<String,Object>> SAmount= dao.SAmount(subjectid,dealerId);
				act.setOutData("SAmount", SAmount.get(0));
			}else
			{
				act.setOutData("Stype", 0);
			}
			if(ZList.size() > 0)
			{
				List<Map<String,Object>> ZAmount= dao.ZAmount(subjectid,dealerId);
				act.setOutData("ZAmount", ZAmount.get(0));
			}else{
				act.setOutData("Ztype", 0);
			}
			
			
			act.setOutData("SList", SList);
			act.setOutData("ZList", ZList);
			FsFileuploadPO po = new FsFileuploadPO();
			if(ttAsActivityBean.getEvaluateid() != null )
			{
				po.setYwzj(Long.parseLong(ttAsActivityBean.getEvaluateid()));
				List<FsFileuploadPO> fsFileuploadPO= dao.select(po);
				act.setOutData("FileuploadPO", fsFileuploadPO);
			}else
			{
				po.setYwzj(Long.parseLong("111111"));
				List<FsFileuploadPO> fsFileuploadPO= dao.select(po);
				act.setOutData("FileuploadPO", fsFileuploadPO );
			}
			act.setOutData("ttAsActivityBean", ttAsActivityBean);
		
			act.setForword(ServiceActivityOptionUrl);//跳转到服务活动评估[编辑]页面
		}catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"服务活动管理---服务活动评估[编辑]查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	public void ServiceActivityManagedel()
	{
		RequestWrapper request = act.getRequest();
		String id= request.getParamValue("id");
		FsFileuploadPO po = new FsFileuploadPO();
		po.setFjid(Long.parseLong(id));
		dao.delete(po);
	}
	public void TT_AS_activity_perin(String[] code_type,String[] part_name,String[] per_count,String subjectid,String dealerId) throws Exception
	{
		TtAsActivityPerPO perPO = new TtAsActivityPerPO();
		perPO.setSummaryId(Long.parseLong(subjectid));
		perPO.setDealerId(Long.parseLong(dealerId));
		dao.delete(perPO);
		if(code_type != null && code_type.length > 0)
		{
			for(int i = 0 ; i < code_type.length; i++)
			{
				perPO.setCodeType(Long.parseLong(code_type[i]));
				perPO.setId(Utility.getLong(SequenceManager.getSequence("")));
				perPO.setPartName(part_name[i]);
				perPO.setPerCount(Long.parseLong(per_count[i]));
				perPO.setSummaryId(Long.parseLong(subjectid));
				dao.insert(perPO);
			}
		}
		
	}
	
	public void TT_AS_activity_conductin(String[] W_name,String[] W_add,String[] publish_date,String[] conduct_cont,String[] media_name,String subjectid,String dealerId) throws Exception
	{
		TtAsActivityConductPO conductPO = new TtAsActivityConductPO();
		conductPO.setSummaryId(Long.parseLong(subjectid));
		conductPO.setDealerId(Long.parseLong(dealerId));
		dao.delete(conductPO);
		if(W_name != null && W_name.length > 0)
		{
			for(int i = 0 ; i < W_name.length; i++)
			{
				conductPO.setWName(W_name[i]);
				conductPO.setWAdd(W_add[i]);
				conductPO.setPublishDate(Utility.getDate(publish_date[i], 1));
				conductPO.setConductCont(conduct_cont[i]);
				conductPO.setMediaName(media_name[i]);
				conductPO.setId(Utility.getLong(SequenceManager.getSequence("")));
				dao.insert(conductPO);
			}
		}
		
	}
	
	
	public void TT_AS_activity_TYPEin(String[] ADD_ITEM_CODE,String[] count_sum , String[] monney,int type,String subjectid, String DealerId) throws Exception
	{
		TtAsActivityTypePO typePO = new TtAsActivityTypePO();
		typePO.setSummaryId(Long.parseLong(subjectid));
		typePO.setActType(type);
		typePO.setDealerId(Long.parseLong(DealerId));
		dao.delete(typePO);
		if(ADD_ITEM_CODE != null && ADD_ITEM_CODE.length > 0)
		{
			for(int i = 0 ;i < ADD_ITEM_CODE.length ; i++)
			{
				typePO.setId(Utility.getLong(SequenceManager.getSequence("")));
				typePO.setActType(type);
				typePO.setCodeType(Long.parseLong(ADD_ITEM_CODE[i]));
				typePO.setCountSum(Long.parseLong(count_sum[i]));
				typePO.setMonney(Double.parseDouble(monney[i]));
				typePO.setDealerId(Long.parseLong(DealerId));
				dao.insert(typePO);
			}
			
		}
	}
	
	public void ServiceActivityManagedbao(){
		try {
			int back = 0;
			RequestWrapper request = act.getRequest();
			String onAmount = request.getParamValue("onAmount");            //老带新信息留存数
			String onCamount = request.getParamValue("onCamount");          //老带新成交数
			String evaluate = request.getParamValue("evaluate");            //活动效果自我评价
			String measures = request.getParamValue("measures");            //建议及改进措施
			String subjectid = request.getParamValue("subjectid");
			String evaluateid = request.getParamValue("evaluateid");
			
			String[] ADD_ITEM_CODE =  request.getParamValues("ADD_ITEM_CODE");
			String[] count_sum = request.getParamValues("count_sum");
			String[] monney = request.getParamValues("monney");
			String[] ADD_ITEM_CODE1 = request.getParamValues("ADD_ITEM_CODE1");
			String[] count_sum1 = request.getParamValues("count_sum1");
			String[] monney1 = request.getParamValues("monney1");
			//新增项目
			String[] code_type =  request.getParamValues("code_typena");
			String[] part_name =  request.getParamValues("part_name");
			String[] per_count =  request.getParamValues("per_count");
			//活动宣传
			String[] W_name = request.getParamValues("W_name");
			String[] W_add = request.getParamValues("W_add");
			String[] media_name = request.getParamValues("media_name");
			
			String[] publish_date = request.getParamValues("publish_date");
			String[] conduct_cont = request.getParamValues("conduct_cont");
			//提升项目名称
			String pull_in_Num = request.getParamValue("pull_in_Num");
			String pull_in_mean = request.getParamValue("pull_in_mean");
			String pull_in_region = request.getParamValue("pull_in_region");
			String pull_in_incre = request.getParamValue("pull_in_incre");
			String customer_Num = request.getParamValue("customer_Num");
			String customer_mean = request.getParamValue("customer_mean");
			String customer_region = request.getParamValue("customer_region");
			String customer_incre = request.getParamValue("customer_incre");
			String price_Num = request.getParamValue("price_Num");
			String price_mean = request.getParamValue("price_mean");
			String price_region = request.getParamValue("price_region");
			String price_incre = request.getParamValue("price_incre");
			String open_Num = request.getParamValue("open_Num");
			String open_mean = request.getParamValue("open_mean");
			String open_region = request.getParamValue("open_region");
			String open_incre = request.getParamValue("open_incre");
			
		    //新增对应关系
			TT_AS_activity_perin(code_type, part_name, per_count, subjectid,logonUser.getDealerId());
			TT_AS_activity_conductin(W_name, W_add, publish_date, conduct_cont,media_name, subjectid,logonUser.getDealerId());
			TT_AS_activity_TYPEin(ADD_ITEM_CODE, count_sum, monney, 0, subjectid,logonUser.getDealerId());
			TT_AS_activity_TYPEin(ADD_ITEM_CODE1, count_sum1, monney1, 1, subjectid,logonUser.getDealerId());

			
			TtAsActivityEvaluatePO evaluatePOContent =new TtAsActivityEvaluatePO();
			
			TtAsActivityEvaluatePO evaluatePOContent1 =new TtAsActivityEvaluatePO();
			
			
			if(!"".equals(pull_in_Num)&&null!=pull_in_Num){
				evaluatePOContent.setPullInNum(Integer.parseInt(pull_in_Num)); 
			}
			if(!"".equals(pull_in_mean)&&null!=pull_in_mean){
				evaluatePOContent.setPullInMean(Integer.parseInt(pull_in_mean)); 
			}
			if(!"".equals(pull_in_region)&&null!=pull_in_region){
				evaluatePOContent.setPullInRegion(Integer.parseInt(pull_in_region)); 
			}
			if(!"".equals(pull_in_incre)&&null!=pull_in_incre){
				evaluatePOContent.setPullInIncre(Float.parseFloat(pull_in_incre)); 
			}
			if(!"".equals(customer_Num)&&null!=customer_Num){
				evaluatePOContent.setCustomerNum(Integer.parseInt(customer_Num)); 
			}
			if(!"".equals(customer_mean)&&null!=customer_mean){
				evaluatePOContent.setCustomerMean(Integer.parseInt(customer_mean)); 
			}
			if(!"".equals(customer_region)&&null!=customer_region){
				evaluatePOContent.setCustomerRegion(Integer.parseInt(customer_region)); 
			}
			if(!"".equals(customer_incre)&&null!=customer_incre){
				evaluatePOContent.setCustomerIncre(Float.parseFloat(customer_incre)); 
			}
			if(!"".equals(price_Num)&&null!=price_Num){
				evaluatePOContent.setPriceNum(Integer.parseInt(price_Num)); 
			}
			if(!"".equals(price_mean)&&null!=price_mean){
				evaluatePOContent.setPriceMean(Integer.parseInt(price_mean)); 
			}
			if(!"".equals(price_region)&&null!=price_region){
				evaluatePOContent.setPriceRegion(Integer.parseInt(price_region)); 
			}
			if(!"".equals(price_incre)&&null!=price_incre){
				evaluatePOContent.setPriceIncre(Float.parseFloat(price_incre)); 
			}
			if(!"".equals(open_Num)&&null!=open_Num){
				evaluatePOContent.setOpenNum(Integer.parseInt(open_Num)); 
			}
			if(!"".equals(open_mean)&&null!=open_mean){
				evaluatePOContent.setOpenMean(Integer.parseInt(open_mean)); 
			}
			if(!"".equals(open_region)&&null!=open_region){
				evaluatePOContent.setOpenRegion(Integer.parseInt(open_region)); 
			}
			if(!"".equals(open_incre)&&null!=open_incre){
				evaluatePOContent.setOpenIncre(Float.parseFloat(open_incre)); 
			}
			
			
		
			if(!"".equals(onAmount)&&null!=onAmount){
				evaluatePOContent.setOnAmount(Integer.parseInt(onAmount)); 
			}
			if(!"".equals(onCamount)&&null!=onCamount){
				evaluatePOContent.setOnCamount(Integer.parseInt(onCamount));
			}
			evaluatePOContent.setEvaluate(evaluate);
			evaluatePOContent.setMeasures(measures);
			if( evaluateid== null || evaluateid.length()==0 )
			{
				evaluateid = SequenceManager.getSequence("");
				evaluatePOContent.setId(Utility.getLong(evaluateid));
				evaluatePOContent.setDealerId(Long.parseLong(logonUser.getDealerId()));
				evaluatePOContent.setSubjectId(Long.parseLong(subjectid));
				evaluatePOContent.setCreateBy(logonUser.getUserId());
				evaluatePOContent.setCreateDate(new Date());
				dao.insert(evaluatePOContent);
				back = 1;
				
			}else
			{
				evaluatePOContent1.setId(Long.parseLong(evaluateid));	
				evaluatePOContent.setUpdateBy(logonUser.getUserId());
				evaluatePOContent.setUpdateDate(new Date());
				dao.update(evaluatePOContent1, evaluatePOContent);
				back = 1;
			}
			String[] fjids = request.getParamValues("fjid");//获取文件ID
			FileUploadManager.delAllFilesUploadByBusiness(evaluateid,fjids);//删除附件
			FileUploadManager.fileUploadByBusiness(evaluateid, fjids, logonUser);	
			act.setOutData("back", back);
			
			}catch (Exception e) {
				BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"服务活动管理---服务活动评估总结上报");
				logger.error(logonUser,e1);
				act.setException(e1);
			}
		
		
	}
	/**
	 * Function         : 服务活动管理---服务活动评估总结上报
	 * @param           : request-活动ID、老带新信息留存数、老带新成交数、活动效果自我评价、建议及改进措施
	 * @return          : 满足条件的服务活动管理信息
	 * @throws          : Exception
	 * LastUpdate       : 2010-06-13
	 */
	public void serviceActivitySummaryOption(){
		try {
			int back = 0;
			RequestWrapper request = act.getRequest();
			String onAmount = request.getParamValue("onAmount");            //老带新信息留存数
			String onCamount = request.getParamValue("onCamount");          //老带新成交数
			String evaluate = request.getParamValue("evaluate");            //活动效果自我评价
			String measures = request.getParamValue("measures");            //建议及改进措施
			String subjectid = request.getParamValue("subjectid");
			String evaluateid = request.getParamValue("evaluateid");
			
			String[] ADD_ITEM_CODE =  request.getParamValues("ADD_ITEM_CODE");
			String[] count_sum = request.getParamValues("count_sum");
			String[] monney = request.getParamValues("monney");
			String[] ADD_ITEM_CODE1 = request.getParamValues("ADD_ITEM_CODE1");
			String[] count_sum1 = request.getParamValues("count_sum1");
			String[] monney1 = request.getParamValues("monney1");
			//新增项目
			String[] code_type =  request.getParamValues("code_typena");
			String[] part_name =  request.getParamValues("part_name");
			String[] per_count =  request.getParamValues("per_count");
			//活动宣传
			String[] W_name = request.getParamValues("W_name");
			String[] W_add = request.getParamValues("W_add");
			String[] media_name = request.getParamValues("media_name");
			String[] publish_date = request.getParamValues("publish_date");
			String[] conduct_cont = request.getParamValues("conduct_cont");
			//提升项目名称
			String pull_in_Num = request.getParamValue("pull_in_Num");
			String pull_in_mean = request.getParamValue("pull_in_mean");
			String pull_in_region = request.getParamValue("pull_in_region");
			String pull_in_incre = request.getParamValue("pull_in_incre");
			String customer_Num = request.getParamValue("customer_Num");
			String customer_mean = request.getParamValue("customer_mean");
			String customer_region = request.getParamValue("customer_region");
			String customer_incre = request.getParamValue("customer_incre");
			String price_Num = request.getParamValue("price_Num");
			String price_mean = request.getParamValue("price_mean");
			String price_region = request.getParamValue("price_region");
			String price_incre = request.getParamValue("price_incre");
			String open_Num = request.getParamValue("open_Num");
			String open_mean = request.getParamValue("open_mean");
			String open_region = request.getParamValue("open_region");
			String open_incre = request.getParamValue("open_incre");
			
		    //新增对应关系
			TT_AS_activity_perin(code_type, part_name, per_count, subjectid,logonUser.getDealerId());
			TT_AS_activity_conductin(W_name, W_add, publish_date, conduct_cont,media_name, subjectid,logonUser.getDealerId());
			TT_AS_activity_TYPEin(ADD_ITEM_CODE, count_sum, monney, 0, subjectid,logonUser.getDealerId());
			TT_AS_activity_TYPEin(ADD_ITEM_CODE1, count_sum1, monney1, 1, subjectid,logonUser.getDealerId());

			
			TtAsActivityEvaluatePO evaluatePOContent =new TtAsActivityEvaluatePO();
			
			TtAsActivityEvaluatePO evaluatePOContent1 =new TtAsActivityEvaluatePO();
			
			
			if(!"".equals(pull_in_Num)&&null!=pull_in_Num){
				evaluatePOContent.setPullInNum(Integer.parseInt(pull_in_Num)); 
			}
			if(!"".equals(pull_in_mean)&&null!=pull_in_mean){
				evaluatePOContent.setPullInMean(Integer.parseInt(pull_in_mean)); 
			}
			if(!"".equals(pull_in_region)&&null!=pull_in_region){
				evaluatePOContent.setPullInRegion(Integer.parseInt(pull_in_region)); 
			}
			if(!"".equals(pull_in_incre)&&null!=pull_in_incre){
				evaluatePOContent.setPullInIncre(Float.parseFloat(pull_in_incre)); 
			}
			if(!"".equals(customer_Num)&&null!=customer_Num){
				evaluatePOContent.setCustomerNum(Integer.parseInt(customer_Num)); 
			}
			if(!"".equals(customer_mean)&&null!=customer_mean){
				evaluatePOContent.setCustomerMean(Integer.parseInt(customer_mean)); 
			}
			if(!"".equals(customer_region)&&null!=customer_region){
				evaluatePOContent.setCustomerRegion(Integer.parseInt(customer_region)); 
			}
			if(!"".equals(customer_incre)&&null!=customer_incre){
				evaluatePOContent.setCustomerIncre(Float.parseFloat(customer_incre)); 
			}
			if(!"".equals(price_Num)&&null!=price_Num){
				evaluatePOContent.setPriceNum(Integer.parseInt(price_Num)); 
			}
			if(!"".equals(price_mean)&&null!=price_mean){
				evaluatePOContent.setPriceMean(Integer.parseInt(price_mean)); 
			}
			if(!"".equals(price_region)&&null!=price_region){
				evaluatePOContent.setPriceRegion(Integer.parseInt(price_region)); 
			}
			if(!"".equals(price_incre)&&null!=price_incre){
				evaluatePOContent.setPriceIncre(Float.parseFloat(price_incre)); 
			}
			if(!"".equals(open_Num)&&null!=open_Num){
				evaluatePOContent.setOpenNum(Integer.parseInt(open_Num)); 
			}
			if(!"".equals(open_mean)&&null!=open_mean){
				evaluatePOContent.setOpenMean(Integer.parseInt(open_mean)); 
			}
			if(!"".equals(open_region)&&null!=open_region){
				evaluatePOContent.setOpenRegion(Integer.parseInt(open_region)); 
			}
			if(!"".equals(open_incre)&&null!=open_incre){
				evaluatePOContent.setOpenIncre(Float.parseFloat(open_incre)); 
			}
			
			
		
			if(!"".equals(onAmount)&&null!=onAmount){
				evaluatePOContent.setOnAmount(Integer.parseInt(onAmount)); 
			}
			if(!"".equals(onCamount)&&null!=onCamount){
				evaluatePOContent.setOnCamount(Integer.parseInt(onCamount));
			}
			evaluatePOContent.setEvaluate(evaluate);
			evaluatePOContent.setMeasures(measures);
			if( evaluateid== null || evaluateid.length()==0 )
			{
				evaluateid = SequenceManager.getSequence("");
				evaluatePOContent.setId(Utility.getLong(evaluateid));
				evaluatePOContent.setDealerId(Long.parseLong(logonUser.getDealerId()));
				evaluatePOContent.setSubjectId(Long.parseLong(subjectid));
				evaluatePOContent.setCreateBy(logonUser.getUserId());
				evaluatePOContent.setStatus(Constant.SERVICEACTIVITY_REPAIR_STATUS_01);
				evaluatePOContent.setReStatus(1);
				evaluatePOContent.setCreateDate(new Date());
				dao.insert(evaluatePOContent);
				
			}else
			{
				evaluatePOContent1.setId(Long.parseLong(evaluateid));	
				evaluatePOContent.setUpdateBy(logonUser.getUserId());
				evaluatePOContent.setUpdateDate(new Date());
				evaluatePOContent.setStatus(Constant.SERVICEACTIVITY_REPAIR_STATUS_01);
				evaluatePOContent.setReStatus(1);
				dao.update(evaluatePOContent1, evaluatePOContent);
				back = 1;
			}
			String[] fjids = request.getParamValues("fjid");//获取文件ID
			FileUploadManager.delAllFilesUploadByBusiness(evaluateid,fjids);//删除附件
			FileUploadManager.fileUploadByBusiness(evaluateid, fjids, logonUser);	
		
		act.setForword(serviceActivityManageSummaryDealerSearchInitUrl);
		}catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"服务活动管理---服务活动评估总结上报");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	
}