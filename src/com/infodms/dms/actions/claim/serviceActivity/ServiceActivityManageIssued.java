/**********************************************************************
* <pre>
* FILE : ServiceActivityManageIssued.java
* CLASS : ServiceActivityManageIssued
*
* AUTHOR : PGM
*
* FUNCTION :服务活动管理---服务活动计划下发
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
 * $Id: ServiceActivityManageIssued.java,v 1.9 2011/07/28 02:49:34 xiongc Exp $
 */
package com.infodms.dms.actions.claim.serviceActivity;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.bean.TtAsActivityBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.common.getCompanyId.GetOemcompanyId;
import com.infodms.dms.dao.claim.dealerClaimMng.ClaimBillMaintainDAO;
import com.infodms.dms.dao.claim.other.BonusDAO;
import com.infodms.dms.dao.claim.serviceActivity.ServiceActivityManageDealerDao;
import com.infodms.dms.dao.claim.serviceActivity.ServiceActivityManageIssuedDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.FsFileuploadPO;
import com.infodms.dms.po.TmDealerPO;
import com.infodms.dms.po.TtAsActivityAgePO;
import com.infodms.dms.po.TtAsActivityCharactorPO;
import com.infodms.dms.po.TtAsActivityDealerPO;
import com.infodms.dms.po.TtAsActivityMgroupPO;
import com.infodms.dms.po.TtAsActivityPO;
import com.infodms.dms.po.TtAsActivityVehiclePO;
import com.infodms.dms.po.TtAsActivityYieldlyPO;
import com.infodms.dms.util.StringUtil;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.dms.chana.actions.OSC21;
import com.infoservice.dms.chana.dao.CommonDao;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;

/**
 * Function       :  服务活动管理---服务活动计划下发
 * @author        :  PGM
 * CreateDate     :  2010-06-10
 * @version       :  0.1
 */
public class ServiceActivityManageIssued {
	private Logger logger = Logger.getLogger(ServiceActivityManageIssued.class);
	private ServiceActivityManageIssuedDao dao = ServiceActivityManageIssuedDao.getInstance();
	private final ClaimBillMaintainDAO cDao = ClaimBillMaintainDAO.getInstance();
	private CommonDao commonDao = CommonDao.getInstance();
	private ActionContext act = ActionContext.getContext();//获取ActionContext
	private AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
	private final String serviceActivityManageIssuedInitUrl = "/jsp/claim/serviceActivity/serviceActivityManageIssued.jsp";//查询页面
	private final String serviceActivityManageIssuedDetailUrl = "/jsp/claim/serviceActivity/serviceActivityManageIssuedDetail.jsp";//明细页面
	private final String ServiceActivityVehicleInfoUrl = "/jsp/claim/serviceActivity/vehicleInfo.jsp";//详细页面
	private final String ServiceActivityVehicleUpdateInfoUrl = "/jsp/claim/serviceActivity/serviceActivityManageIssuedActivityInfo.jsp";//重新发布页面
	/**
	 * Function       :  服务活动管理---服务活动计划下发页面初始化
	 * @param         :  
	 * @return        :  serviceActivityManageIssuedInit
	 * @throws        :  Exception
	 * LastUpdate     :  2010-06-09
	 */
	@SuppressWarnings("unchecked")
	public void serviceActivityManageIssuedInit(){
		try {
			RequestWrapper request =act.getRequest();
			List<TtAsActivityPO> list=dao.serviceActivityCode();
			request.setAttribute("list", list);
			act.setForword(serviceActivityManageIssuedInitUrl);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"服务活动管理---服务活动计划下发页面初始化");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * Function       :  根据条件查询服务活动管理---服务活动计划下发查询中符合条件的信息
	 * @param         :  request-活动编号、活动状态、活动开始时间、活动结束时间
	 * @return        :  服务活动管理
	 * @throws        :  Exception
	 * LastUpdate     :  2010-06-09
	 */
	public void serviceActivityManageIssuedQuery(){
		try {
			RequestWrapper request = act.getRequest();
			String activityCode = request.getParamValue("activityCode");    //活动编号
			String activityName = request.getParamValue("activityName");    //活动名称
			String status = request.getParamValue("status");                //活动状态
			String startdate = request.getParamValue("startdate");         //活动开始时间
			String enddate = request.getParamValue("enddate");             //活动结束时间
			Long companyId=GetOemcompanyId.getOemCompanyId(logonUser);      //公司ID
			TtAsActivityBean ActivityBean = new TtAsActivityBean();
			ActivityBean.setActivityCode(activityCode);
			ActivityBean.setActivityName(activityName);
			ActivityBean.setStatus(status);
			ActivityBean.setStartdate(startdate);
			ActivityBean.setEnddate(enddate);
			ActivityBean.setCompanyId(String.valueOf(companyId));
			Integer curPage = request.getParamValue("curPage") !=null ? Integer.parseInt(request.getParamValue("curPage")) : 1;
			PageResult<Map<String, Object>> ps = dao.getServiceActivityManageIssuedQuery(ActivityBean,curPage,Constant.PAGE_SIZE );
			act.setOutData("ps", ps);
			act.setForword(serviceActivityManageIssuedInitUrl);
		}catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"服务活动管理---服务活动计划下发");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * Function       :  根据ID查询服务活动管理---活动对应的明细信息
	 * @param         :  request-活动ID
	 * @return        :  服务活动管理
	 * @throws        :  Exception
	 * LastUpdate     :  2010-06-09
	 * @deprecated As of another Method , replaced by 
     * <%=contextPath%>/claim/serviceActivity/ServiceActivityManage/getActivityIdInfo.do
     * @see /claim/serviceActivity/ServiceActivityManageIssued/serviceActivityManageIssuedInfo.do
	 */
	public void serviceActivityManageIssuedInfo(){
		RequestWrapper request = act.getRequest();
		String activityId=request.getParamValue("activityId");//活动ID
		try {
			TtAsActivityBean ActivityBean=dao.getServiceActivityByActivityIdInfo(activityId);//查询服务活动信息-明细
			List<TtAsActivityBean> ActivityBeanList=dao.getWorkingHoursInfoList(activityId);//活动工时
			List<TtAsActivityBean> ActivityPartsList=dao.getPartsList(activityId);//活动配件
			List<TtAsActivityBean> ActivityNetItemList=dao.getNetItemList(activityId);//活动其它项目
			List<TtAsActivityBean> ActivityVhclMaterialGroupList=dao.getVhclMaterialGroupList(activityId);//车型列表
			List<TtAsActivityBean> ActivitygetActivityAgeList=dao.getActivityAgeList(activityId);//车龄定义列表
			List<TtAsActivityBean> ActivityCharactorList=dao.getActivityCharactorList(activityId);//车辆性质
			if(ActivityCharactorList!=null){
			Iterator<TtAsActivityBean> it=ActivityCharactorList.iterator();
			while(it.hasNext()){
				TtAsActivityBean ActivityBeans =it.next();
				if(String.valueOf(Constant.SERVICEACTIVITY_CHARACTOR_01).equals(ActivityBeans.getCarCharactor())){//出租车
					ActivityBeans.setCodeDesc("出租车");
				}else if(String.valueOf(Constant.SERVICEACTIVITY_CHARACTOR_02).equals(ActivityBeans.getCarCharactor())){//私家车
					ActivityBeans.setCodeDesc("私家车");
				}else{//公务车
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
			act.setForword(serviceActivityManageIssuedDetailUrl);
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
			PageResult<Map<String, Object>> ps = dao.getActivityVehicleList(activityId,curPage,Constant.PAGE_SIZE );
			act.setOutData("ps", ps);
			act.setForword(ServiceActivityVehicleInfoUrl);
		} catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"奖惩工单详细信息");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
    }
	/**
	 * Function       :  服务活动计划下发-首次下发
	 * @param         :  request-活动ID
	 * @return        :  服务活动信息
	 * @throws        :  Exception
	 * LastUpdate     :  2010-06-01
	 */
	@SuppressWarnings("static-access")
	public void serviceActivityManageIssuedUpdateStatus(){
		RequestWrapper request = act.getRequest();
		String activityId=request.getParamValue("activityId");//活动ID
		try {
			this.serviceActivityManageCheckMethod(activityId);//验证发布信息发放
		} catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"奖惩工单详细信息");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * Function       :  服务活动计划下发[重新发布页面]-查询服务活动主信息
	 * @param         :  request-活动ID
	 * @return        :  服务活动信息
	 * @throws        :  Exception
	 * LastUpdate     :  2010-06-01
	 */
	public void serviceActivityManageIssuedInfoQuery(){
		RequestWrapper request = act.getRequest();
		String activityId=request.getParamValue("activityId");//活动ID
		try {
			TtAsActivityBean ActivityBean=dao.getServiceActivityByActivityIdInfo(activityId);//查询服务活动信息-明细
			request.setAttribute("ActivityBean", ActivityBean);
			request.setAttribute("activityId", activityId);
			
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
			BonusDAO bonusDao = new BonusDAO();
			List<Map<String,Object>> listNews = bonusDao.dlrQueryEncourageNewsDetail(activityId);
			request.setAttribute("activityId", activityId);
			request.setAttribute("vicle1", vicle1);
			request.setAttribute("vicle2", vicle2);
			act.setOutData("listNews", listNews);
			act.setForword(ServiceActivityVehicleUpdateInfoUrl);
		} catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"查询服务活动主信息");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * Function       :  修改服务活动管理信息【当重新下发服务活动时，为增量下发，将【重新发布】状态提供给下端接口】
	 * @param         :  request--活动ID、活动编号、活动名称、活动类型、活动类别、活动开始日期、活动结束日期、处理方式
	 *                            距活动结束日期几天上传活动总结、配件费用、工时费用、索赔、索赔是否为固定费用
	 *                            解决方案说明、索赔申请填写指导、修改内容、服务活动活动状态
	 * @return        :  服务活动管理
	 * @throws        :  Exception
	 * LastUpdate     :  2010-06-01
	 */
	@SuppressWarnings("static-access")
	public void serviceActivityManageUpdate(){
		try {
			RequestWrapper request = act.getRequest();
			String activityId   = request.getParamValue("activityId");      //活动ID
			String activityCode = request.getParamValue("activityCode");    //活动编号
			String activityName = request.getParamValue("activityName");    //活动名称
			String activityType = request.getParamValue("activityType");    //活动类型
			String activityKind = request.getParamValue("activityKind");    //活动类别
			String single = request.getParamValue("single_car") ;           //单台次活动次数
			String max = request.getParamValue("max_car") ;                 //单经销商活动总次数
			String startdate = request.getParamValue("startDate");          //活动开始日期
			String enddate = request.getParamValue("endDate");              //活动结束日期
			String dealwith = request.getParamValue("dealwith");            //处理方式
			String uploadPrePeriod = request.getParamValue("uploadPrePeriod");//距活动结束日期几天上传活动总结
			String partFee = request.getParamValue("partFee");              //配件费用
			String worktimeFee = request.getParamValue("worktimeFee");      //工时费用
			String isClaim = request.getParamValue("isClaim");              //索赔
			String isFixfee = request.getParamValue("isFixfee");            //索赔是否为固定费用
			String solution = request.getParamValue("solution");            //解决方案说明
			String claimGuide = request.getParamValue("claimGuide");        //索赔申请填写指导
			Integer status=Constant.SERVICEACTIVITY_STATUS_02;              //服务活动管理--服务活动活动状态:[10681001:尚未发布;10681002:已经发布;10681003:重新发布]
			TtAsActivityPO ActivityPOCon=new TtAsActivityPO();
			ActivityPOCon.setActivityId(Long.parseLong(activityId));        //以活动ID为条件进行修改
			TtAsActivityPO ActivityPO=new TtAsActivityPO();                 //修改内容
			ActivityPO.setActivityCode(activityCode);
			ActivityPO.setActivityName(activityName);
			//ActivityPO.setIfStatus(1);                                      //(重新下发)查询接口状态为2的字段，置成1
			if (activityType!=null&&!"".equals(activityType)) {
			ActivityPO.setActivityType(Integer.parseInt(activityType));
			}
			if(StringUtil.notNull(single))
				ActivityPO.setSingleCarNum(Integer.parseInt(single)) ;
			if(StringUtil.notNull(max))
				ActivityPO.setMaxCar(Integer.parseInt(max)) ;
			if (activityKind!=null&&!"".equals(activityKind)) {
			ActivityPO.setActivityKind(Integer.parseInt(activityKind));
			}
			if(null!=startdate){
				DateFormat df=new SimpleDateFormat("yyyy-MM-dd");
				ActivityPO.setStartdate(df.parse(startdate));
			}
			if(null!=enddate){
				DateFormat df=new SimpleDateFormat("yyyy-MM-dd");
				ActivityPO.setEnddate(df.parse(enddate));
			}
			if (dealwith!=null&&!"".equals(dealwith)) {
			ActivityPO.setDealwith(Integer.parseInt(dealwith));
			}
			if (uploadPrePeriod!=null&&!"".equals(uploadPrePeriod)) {
			ActivityPO.setUploadPrePeriod(Integer.parseInt(uploadPrePeriod));
			}
			if (partFee!=null&&!"".equals(partFee)) {
			ActivityPO.setPartFee(Float.parseFloat(partFee));
			}
			if (worktimeFee!=null&&!"".equals(worktimeFee)) {
			ActivityPO.setWorktimeFee(Float.parseFloat(worktimeFee));
			}
			if (isClaim!=null&&!"".equals(isClaim)) {
				ActivityPO.setIsClaim(Integer.parseInt(isClaim));
			}else{
				ActivityPO.setIsClaim(1);
			}
			if (isFixfee!=null&&!"".equals(isFixfee)) {
				ActivityPO.setIsFixfee(Integer.parseInt(isFixfee));
			}else{
				ActivityPO.setIsFixfee(0);
			}
			ActivityPO.setSolution(solution);
 			ActivityPO.setClaimGuide(claimGuide);
 			ActivityPO.setStatus(status);
 			ActivityPO.setUpdateBy(logonUser.getUserId());
 			ActivityPO.setUpdateDate(new Date());
 			ActivityPO.setReleasedate(new Date());
 			//如果此服务活动是区域性维护，执行区域下所有经销商录入
			/*
			 * 功能：判断执行经销商是否存在;
			 * 描述：活动执行经销商情况：1.存在，可以下发;2.不存在，不可以下发;
			 */
			List<TtAsActivityDealerPO> dealerList= dao.dealerList(activityId);
			/*
			* 功能：判断车型是否存在;
			* 描述：活动车型情况：1.存在，可以下发;2.不存在，不可以下发;
			*/
			List<TtAsActivityMgroupPO> mgroupList=dao.mgroupList(activityId);
			/*
			* 功能：判断车龄是否存在;
			* 描述：活动车龄情况：1.存在，可以下发;2.不存在，不可以下发;
			*/
			List<TtAsActivityAgePO> ageList=dao.ageList(activityId);
			
			/*
			* 功能：判断车辆性质是否存在;
			* 描述：活动车辆性质情况：1.存在，可以下发;2.不存在，不可以下发;
			*/
			List<TtAsActivityCharactorPO>   charactorList=dao.charactorList(activityId);
			
			/*
			* 功能：判断车辆性质是否存在;
			* 描述：活动车辆性质情况：1.存在，可以下发;2.不存在，不可以下发;
			*/
			List<TtAsActivityYieldlyPO>   yieldlyList=dao.yieldlyList(activityId);
			
			
			/* 功能：判断VIN(车辆)是否存在;
			 * 描述：活动VIN(车辆)情况：1.存在，可以下发;2.不存在，不可以下发;
			 */
			List<TtAsActivityVehiclePO>   vehicleList=dao.vehicleList(activityId);
		    /*
		     * 功能：检查执行经销商是否存在，不存在提示不存在(2:表示执行经销商不存在)
			 * 描述：首先：检查经销商是否存在(执行经销商存在是校验数据能否下发的前提！)
		     */
			if(null==dealerList||dealerList.size()==0||"".equals(dealerList)){
				act.setOutData("returnValue", 2);
			}else{
			 /*
		     * 功能：检查车龄,车型,车辆性质是否存在;
			 * 描述：不存在提示不存在(3、4、5 分别表示：车型,车龄,车辆性质不存在);
		     */
	         if(mgroupList.size()>0||ageList.size()>0||yieldlyList.size()>0||vehicleList.size()>0){
	        		 /*
	     		     * 功能：车龄,车型,车辆性质同时存在;
	     			 * 描述：下发服务活动;
	     		     */
	     			dao.serviceActivityManageUpdate(ActivityPOCon,ActivityPO);//功能：调用修改方法;描述：重新发布服务活动;
	     			TtAsActivityVehiclePO VehiclePo =new TtAsActivityVehiclePO();//条件
	         		VehiclePo.setActivityId(Long.parseLong(activityId));
	         		TtAsActivityVehiclePO VehiclePoContent =new TtAsActivityVehiclePO();//内容
	         		VehiclePoContent.setUpdateBy(logonUser.getUserId()); //修改人
	     			VehiclePoContent.setUpdateDate(new Date());          //修改时间
	     			VehiclePoContent.setCarStatus(Constant.SERVICEACTIVITY_CAR_STATUS_01);//已经下发
	     			dao.serviceActivityManageVehicleCarStatus(VehiclePo, VehiclePoContent);
	     			//commonDao.updateSendingFromComplete(tableName, colName, colValue)//将状态为2的更新为1
	     			//invoke interface 
	     			//commonDao.updateComplete("tt_as_activity", "id", activityId);//更新状态为2
	     			TtAsActivityPO ActivityPOOS=new TtAsActivityPO();
	     			ActivityPOOS.setActivityId(Long.parseLong(activityId));
	     			OSC21 os = new OSC21();
	     			os.execute(ActivityPOOS);
	     			act.setOutData("returnValue", 1);
	     			
	        	 }else{
	        		 act.setOutData("returnValue", 6);
	        		 /*if(mgroupList.size()==0){//车型不存在
	        			 act.setOutData("returnValue", 3);
	        		 }else{
	        			 act.setOutData("returnValue", 1);
	        		 }
	        		 if(ageList.size()==0){//车龄不存在
	        			 act.setOutData("returnValue", 4);
	        		 }else{
	        			 act.setOutData("returnValue", 1);
	        		 }
	        		 if(charactorList.size()==0){//车辆性质不存在
	        			 act.setOutData("returnValue", 5);
	        		 }else{
	        			 act.setOutData("returnValue", 1);
	        		 }*/
	        	 }
	         }
	         /*
			     * 功能：检查VIN与车龄必须有一个不为空，否则提示
				 * 描述：已保证"车龄,车型,车辆性质"同时存在或不存在，检测VIN与车龄必须有一个不为空，否则提示
			     */
	          /*if(ageList.size()==0&&vehicleList.size()==0){
	         	 act.setOutData("returnValue", 6);//"车龄,车型,车辆性质"与VIN必须存在一个
	          }else if(vehicleList.size()>0){
	    		
	   		     // 功能：VIN(车辆)存在;
	   			 // 描述：下发服务活动;
	   		    
	  			dao.serviceActivityManageUpdate(ActivityPOCon,ActivityPO);//功能：调用修改方法;描述：重新发布服务活动;
	  			
	  			TtAsActivityVehiclePO VehiclePo =new TtAsActivityVehiclePO();//条件
	      		VehiclePo.setActivityId(Long.parseLong(activityId));
	      		TtAsActivityVehiclePO VehiclePoContent =new TtAsActivityVehiclePO();//内容
	      		VehiclePoContent.setUpdateBy(logonUser.getUserId()); //修改人
	  			VehiclePoContent.setUpdateDate(new Date());          //修改时间
	  			VehiclePoContent.setCarStatus(Constant.SERVICEACTIVITY_CAR_STATUS_01);//已经下发
	  			dao.serviceActivityManageVehicleCarStatus(VehiclePo, VehiclePoContent);
	  		   //commonDao.updateSendingFromComplete(tableName, colName, colValue)//将状态为2的更新为1
	  		   //invoke interface 
	  		  //commonDao.updateComplete("tt_as_activity", "id", activityId);//更新状态为2
	  			TtAsActivityPO ActivityPOOS=new TtAsActivityPO();
     			ActivityPOOS.setActivityId(Long.parseLong(activityId));
     			OSC21 os = new OSC21();
     			os.execute(ActivityPOOS);
	   			act.setOutData("returnValue", 1);
	    	 }*/
			
			/*
			 * 服务活动重新下发时 
			 * 如果经销商维护里面，有按区域维护
			 * 则在重新下发时，将已维护了的区域下的所有经销商维护至主表中
			 */
//			ServiceActivityManageDealerDao dao2 = ServiceActivityManageDealerDao.getInstance() ;
//			List<TtAsActivityDealerPO> tdList = dao2.getArea(activityId);
//			if(tdList.size()>0){
//				TtAsActivityDealerPO tdpo ;
//				for(int i = 0 ;i<tdList.size();i++){
//					tdpo = tdList.get(i) ;
//					List<TmDealerPO> dealerList2 = dao2.getDealerFromORG(tdpo.getAreaId().toString()) ; 
//					TtAsActivityDealerPO po = null ;
//					for(int j=0;j<dealerList2.size();j++){
//						//判断数据库中是否存在此经销商
//						po = new TtAsActivityDealerPO();
//						po.setActivityId(Long.parseLong(activityId));
//						po.setDealerId(dealerList2.get(j).getDealerId());
//						//po.setAreaId(Long.parseLong(orgIds[i]));
//						if(dao.select(po).size()==0){
//							po.setCreateBy(logonUser.getUserId());
//							po.setCreateDate(new Date());
//							po.setDealerCode(dealerList2.get(j).getDealerCode());
//							po.setDealerName(dealerList2.get(j).getDealerName());
//							po.setAdId(Utility.getLong(SequenceManager.getSequence("")));
//							po.setStatus(Constant.SUMMARY_STATUS_02);
//							po.setAreaId(tdpo.getAreaId());
//							dao.insert(po);
//						}
//					}
//				}
//			}
			
			act.setForword(serviceActivityManageIssuedInitUrl);//返回查询主界面
		}catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"服务活动管理信息增加");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * Function       :  验证发布信息发放
	 * @param         :  request--活动ID、活动编号、活动名称、活动类型、活动类别、活动开始日期、活动结束日期、处理方式
	 *                            距活动结束日期几天上传活动总结、配件费用、工时费用、索赔、索赔是否为固定费用
	 *                            解决方案说明、索赔申请填写指导、修改内容、服务活动活动状态
	 * @return        :  服务活动管理
	 * @throws Exception 
	 * @throws        :  Exception
	 * LastUpdate     :  2010-06-01
	 */
	@SuppressWarnings("static-access")
	public void serviceActivityManageCheckMethod(String activityId) throws Exception{
		/*
		 * 功能：判断执行经销商是否存在;
		 * 描述：活动执行经销商情况：1.存在，可以下发;2.不存在，不可以下发;
		 */
		List<TtAsActivityDealerPO> dealerList= dao.dealerList(activityId);
		/*
		* 功能：判断车型是否存在;
		* 描述：活动车型情况：1.存在，可以下发;2.不存在，不可以下发;
		*/
		List<TtAsActivityMgroupPO> mgroupList=dao.mgroupList(activityId);
		/*
		* 功能：判断车龄是否存在;
		* 描述：活动车龄情况：1.存在，可以下发;2.不存在，不可以下发;
		*/
		List<TtAsActivityAgePO> ageList=dao.ageList(activityId);
		
		/*
		* 功能：判断车辆性质是否存在;
		* 描述：活动车辆性质情况：1.存在，可以下发;2.不存在，不可以下发;
		*/
		List<TtAsActivityCharactorPO>   charactorList=dao.charactorList(activityId);
		
		/*
		* 功能：判断车辆性质是否存在;
		* 描述：活动车辆性质情况：1.存在，可以下发;2.不存在，不可以下发;
		*/
		List<TtAsActivityYieldlyPO>   yieldlyList=dao.yieldlyList(activityId);
		
		/* 功能：判断VIN(车辆)是否存在;
		 * 描述：活动VIN(车辆)情况：1.存在，可以下发;2.不存在，不可以下发;
		 */
		List<TtAsActivityVehiclePO>   vehicleList=dao.vehicleList(activityId);
	    /*
	     * 功能：检查执行经销商是否存在，不存在提示不存在(2:表示执行经销商不存在)
		 * 描述：首先：检查经销商是否存在(执行经销商存在是校验数据能否下发的前提！)
	     */
		if(null==dealerList||dealerList.size()==0||"".equals(dealerList)){
			act.setOutData("returnValue", 2);
		}else{
		 /*
	     * 功能：检查车龄,车型,车辆性质是否存在;
		 * 描述：不存在提示不存在(3、4、5 分别表示：车型,车龄,车辆性质不存在);
	     */
         if(mgroupList.size()>0||ageList.size()>0||yieldlyList.size()>0||vehicleList.size()>0){
        	 //if(mgroupList.size()>0&&ageList.size()>0&&charactorList.size()>0){
        		 /*
        		  * 更改此功能，要是车龄，车型,生产基地存在一个。则可以下发。
        		  */
        		TtAsActivityPO ActivityPO=new TtAsActivityPO();
     			ActivityPO.setStatus(Constant.SERVICEACTIVITY_STATUS_02);//状态为: 已经下发
     			ActivityPO.setUpdateBy(logonUser.getUserId());           //修改人
     			ActivityPO.setUpdateDate(new Date());                    //修改时间
     			ActivityPO.setReleasedate(new Date());                   //活动发布时间
     			//ActivityPO.setIfStatus(1);                               //(首次下发)当点下发服务活动的时候把这个状态置成1 
     			dao.ServiceActivityManageIssuedUpdateStatus(activityId,ActivityPO); //修改活动状态[首次下发]状态为: 已经下发
     			TtAsActivityVehiclePO VehiclePo =new TtAsActivityVehiclePO();       //条件
     			VehiclePo.setActivityId(Long.parseLong(activityId));
     			TtAsActivityVehiclePO VehiclePoContent =new TtAsActivityVehiclePO();//内容
     			VehiclePoContent.setUpdateBy(logonUser.getUserId());                //修改人
     			VehiclePoContent.setUpdateDate(new Date());                         //修改时间
     			VehiclePoContent.setCarStatus(Constant.SERVICEACTIVITY_CAR_STATUS_01);//已经下发
     			dao.ServiceActivityManageIssuedUpdateCarStatus(VehiclePo,VehiclePoContent); //修改车辆状态[]为：已经下发
     			Long createBy=logonUser.getUserId();
     			Long updateBy=logonUser.getUserId();
     			dao.serviceActivityDealerIdInsert(activityId,createBy,updateBy);//调用方法：全量下发所有经销商（ID）
     			//(首次下发)当点下发服务活动的时候把这个状态置成1 
     			TtAsActivityPO ActivityPOOS=new TtAsActivityPO();
     			ActivityPOOS.setActivityId(Long.parseLong(activityId));
     			OSC21 os = new OSC21();
     			os.execute(ActivityPOOS);
     			act.setOutData("returnValue", 1);
        	 }else{
        		 // 需求更改，此外返回一个10代表未下发。。。
        		 act.setOutData("returnValue",6);
        		/* if(mgroupList.size()==0){//车型不存在
        			 act.setOutData("returnValue", 3);
        		 }else{
        			 act.setOutData("returnValue", 1);
        		 }
        		 if(ageList.size()==0){//车龄不存在
        			 act.setOutData("returnValue", 4);
        		 }else{
        			 act.setOutData("returnValue", 1);
        		 }
        		 if(charactorList.size()==0){//车辆性质不存在
        			 act.setOutData("returnValue", 5);
        		 }else{
        			 act.setOutData("returnValue", 1);
        		 }*/
        	 }
         }
         /*
		     * 功能：检查VIN与车龄必须有一个不为空，否则提示
			 * 描述：已保证"车龄,车型,车辆性质"同时存在或不存在，检测VIN与车龄必须有一个不为空，否则提示
		     */
         /*if(vehicleList.size()==0){
         	 //act.setOutData("returnValue", 6);//"车龄,车型,车辆性质"与VIN必须存在一个
          }else if(vehicleList.size()>0){
    		 
   		     // 功能：VIN(车辆)存在;
   			 // 描述：下发服务活动;
   		     
      		TtAsActivityPO ActivityPO=new TtAsActivityPO();
   			ActivityPO.setStatus(Constant.SERVICEACTIVITY_STATUS_02);//状态为: 已经下发
   			ActivityPO.setUpdateBy(logonUser.getUserId());           //修改人
   			ActivityPO.setUpdateDate(new Date());                    //修改时间
   			ActivityPO.setReleasedate(new Date());                   //活动发布时间
   		    //ActivityPO.setIfStatus(1);                               //(首次下发)当点下发服务活动的时候把这个状态置成1 
   			dao.ServiceActivityManageIssuedUpdateStatus(activityId,ActivityPO); //修改活动状态[首次下发]状态为: 已经下发
   			
   			TtAsActivityVehiclePO VehiclePo =new TtAsActivityVehiclePO();       //条件
   			VehiclePo.setActivityId(Long.parseLong(activityId));
   			TtAsActivityVehiclePO VehiclePoContent =new TtAsActivityVehiclePO();//内容
   			VehiclePoContent.setUpdateBy(logonUser.getUserId());                //修改人
   			VehiclePoContent.setUpdateDate(new Date());                         //修改时间
   			VehiclePoContent.setCarStatus(Constant.SERVICEACTIVITY_CAR_STATUS_01);//已经下发
   			dao.ServiceActivityManageIssuedUpdateCarStatus(VehiclePo,VehiclePoContent); //修改车辆状态[]为：已经下发
   			Long createBy=logonUser.getUserId();
 			Long updateBy=logonUser.getUserId();
 			dao.serviceActivityDealerIdInsert(activityId,createBy,updateBy);//调用方法：全量下发所有经销商（ID）
 			//(首次下发)当点下发服务活动的时候把这个状态置成1 
 			//commonDao.updateSending("tt_as_activity", "id", activityId);//更新状态为1
 			//invoke interface 
 			//commonDao.updateComplete("tt_as_activity", "id", activityId);//更新状态为2
 			TtAsActivityPO ActivityPOOS=new TtAsActivityPO();
 			ActivityPOOS.setActivityId(Long.parseLong(activityId));
 			OSC21 os = new OSC21();
 			os.execute(ActivityPOOS);
   			act.setOutData("returnValue", 1);
    	 }*/
		
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
}