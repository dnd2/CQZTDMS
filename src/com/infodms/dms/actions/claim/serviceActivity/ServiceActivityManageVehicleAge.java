/**********************************************************************
* <pre>
* FILE : ServiceActivityManageVehicleAge.java
* CLASS : ServiceActivityManageVehicleAge
*
* AUTHOR : PGM
*
* FUNCTION :服务活动管理--车龄定义列表.
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
 * $Id: ServiceActivityManageVehicleAge.java,v 1.2 2010/09/15 07:51:29 zuoxj Exp $
 */
package com.infodms.dms.actions.claim.serviceActivity;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.bean.TtAsActivityBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.dao.claim.serviceActivity.ServiceActivityManageVehicleAgeDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TtAsActivityAgePO;
import com.infodms.dms.util.StringUtil;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;

/**
 * Function       :  服务活动管理--车龄定义列表
 * @author        :  PGM
 * CreateDate     :  2010-06-02
 * @version       :  0.1
 */
public class ServiceActivityManageVehicleAge {
	private Logger logger = Logger.getLogger(ServiceActivityManageVehicleAge.class);
	private ServiceActivityManageVehicleAgeDao dao = ServiceActivityManageVehicleAgeDao.getInstance();
	private ActionContext act = ActionContext.getContext();//获取ActionContext
	private AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
	private final String ServiceActivityVehicleAgeInitUrl = "/jsp/claim/serviceActivity/serviceActivityVehicleAge.jsp";//查询页面
	private final String ServiceActivityVehicleAgeInitUrl2 = "/jsp/claim/serviceActivity/serviceActivityVehicleAge2.jsp";//查询页面
	private final String ServiceActivityModelSuccessUrl = "/jsp/claim/serviceActivity/serviceActivityAgeSuccess.jsp";//查询页面
	
	/**
	 * Function       :  根据条件查询服务活动管理中符合条件的信息，其中包括：车型列表 
	 * @param         :  request-活动ID
	 * @return        :  服务活动管理--车龄定义列表
	 * @throws        :  Exception
	 * LastUpdate     :  2010-06-02
	 */
	public void serviceActivityManageVehicleAgeQuery(){ 
		RequestWrapper request = act.getRequest();
		String activityId=request.getParamValue("activityId");//活动ID
		String beforeVehicle=request.getParamValue("beforeVehicle");//售前车
		String type=request.getParamValue("type");
		act.setOutData("type", type);
		try {
			List<TtAsActivityBean> VehicleAgeList=dao.getAllServiceActivityManageVehicleAgeInfo(activityId);//车辆性质
			act.setOutData("VehicleAgeList", VehicleAgeList);
			StringBuffer str = new StringBuffer() ;
			String b1 = "";
			String b2 = "" ;
			String e1 = "" ;
			String e2 = "" ;
			if(VehicleAgeList!=null)
				for(int i=0;i<VehicleAgeList.size();i++){
					if(VehicleAgeList.get(i).getDateType().equals(Constant.SERVICEACTIVITY_DATE_TYPE_01.toString())){
						b2 = VehicleAgeList.get(i).getSaleDateStart() ;
						e2 = VehicleAgeList.get(i).getSaleDateEnd() ;
					}
					if(VehicleAgeList.get(i).getDateType().equals(Constant.SERVICEACTIVITY_DATE_TYPE_02.toString())){
						b1 = VehicleAgeList.get(i).getSaleDateStart() ;
						e1 = VehicleAgeList.get(i).getSaleDateEnd() ;
					}
				}
			str.append("?b1=").append(b1).append("&b2=").append(b2);
			str.append("&e1=").append(e1).append("&e2=").append(e2);
			request.setAttribute("activityId", activityId);
			request.setAttribute("beforeVehicle", beforeVehicle);
			act.setForword(ServiceActivityVehicleAgeInitUrl2+str.toString());
			//act.setForword(ServiceActivityVehicleAgeInitUrl);
		} catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"服务活动管理--车龄定义列表");
			logger.error(logonUser,e1);
			act.setException(e1);
		 }
		}
	/**
	 * Function       :  增加服务活动管理信息---车龄定义
	 * @param         :  request--活动ID、活动开始日期、活动结束日期、类型
	 * @return        :  服务活动管理
	 * @throws        :  Exception
	 * LastUpdate     :  2010-06-02
	 */
	@SuppressWarnings("static-access")
	public void serviceActivityManageVehicleAgeOption(){
		try {
			RequestWrapper request = act.getRequest();
			String activityId =request.getParamValue("activityId");             //活动ID
			String []startdate =request.getParamValues("startdate");            //活动开始日期
			String []enddate =request.getParamValues("enddate");                //活动结束日期
			String []dateType =request.getParamValues("dateType");              //类型
			if (startdate!=null&&!"".equals(startdate)) {
				TtAsActivityAgePO AgePO=new TtAsActivityAgePO();
				AgePO.setActivityId(Long.parseLong(activityId));
				AgePO.setCreateBy(logonUser.getUserId());
				AgePO.setCreateDate(new Date());
				AgePO.setUpdateBy(logonUser.getUserId());
				AgePO.setUpdateDate(new Date());
			    dao.serviceActivityManageVehicleAgeOption(startdate,enddate,dateType,AgePO);
				act.setRedirect(ServiceActivityModelSuccessUrl);
			}
		}catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"服务活动管理车龄定义信息增加");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	public void  serviceActivityManageVehicleAgejude()
	{
		try {
			RequestWrapper request = act.getRequest();
			String activityId =request.getParamValue("activityId");             //活动ID
				TtAsActivityAgePO AgePO=new TtAsActivityAgePO();
				AgePO.setActivityId(Long.parseLong(activityId));
				if (dao.select(AgePO).size() == 0)
				{
					act.setOutData("ret","false" );
				}else
				{
					act.setOutData("ret","true" );
				}
		}catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"服务活动管理车龄定义信息增加");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/*
	 * 服务活动--车龄定义方法2
	 */
	public void serviceActivityVhclAgeDefine(){
		RequestWrapper req = act.getRequest() ;
		try{
			String activityId = req.getParamValue("activityId");//活动ID
			String beginDate1 = req.getParamValue("beginDate");//生产日期开始时间
			String beginDate2 = req.getParamValue("beginDate2");//销售日期开始时间
			String endDate1 = req.getParamValue("endDate");//生产日期结束时间
			String endDate2 = req.getParamValue("endDate2");//销售日期结束时间
			
			//判断生产日期是否为空
			if(StringUtil.notNull(beginDate1)||StringUtil.notNull(endDate1)){
				SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
				Date d = null ;
				TtAsActivityAgePO po = new TtAsActivityAgePO();
				po.setActivityId(Long.parseLong(activityId));
				po.setDateType(new Long(Constant.SERVICEACTIVITY_DATE_TYPE_02));
				List<TtAsActivityAgePO> lists = dao.select(po);
				//此次服务活动的车龄定义中,生产日期已定义刚修改,否则添加
				if(lists.size()>0){
					TtAsActivityAgePO po1 = lists.get(0);
					po1.setUpdateBy(logonUser.getUserId());
					po1.setUpdateDate(new Date());
					if(StringUtil.notNull(beginDate1)){
						d = fmt.parse(beginDate1);
						po1.setSaleDateStart(d);
					}
					if(StringUtil.notNull(endDate1)){
						d = fmt.parse(endDate1);
						po1.setSaleDateEnd(d);
					}
					dao.update(po, po1);
				}else{
					TtAsActivityAgePO po1 = new TtAsActivityAgePO();
					po1.setActivityId(Long.parseLong(activityId));
					po1.setCreateBy(logonUser.getUserId());
					po1.setCreateDate(new Date());
					po1.setDateType(new Long(Constant.SERVICEACTIVITY_DATE_TYPE_02));
					po1.setId(Utility.getLong(SequenceManager.getSequence("")));
					if(StringUtil.notNull(beginDate1)){
						d = fmt.parse(beginDate1);
						po1.setSaleDateStart(d);
					}
					if(StringUtil.notNull(endDate1)){
						d = fmt.parse(endDate1);
						po1.setSaleDateEnd(d);
					}
					dao.insert(po1);
				}
			}
			
			//判断销售日期是否为空
			if(StringUtil.notNull(beginDate2)||StringUtil.notNull(endDate2)){
				SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
				Date d = null ;
				TtAsActivityAgePO po = new TtAsActivityAgePO();
				po.setActivityId(Long.parseLong(activityId));
				po.setDateType(new Long(Constant.SERVICEACTIVITY_DATE_TYPE_01));
				List<TtAsActivityAgePO> lists = dao.select(po);
				//此次服务活动的车龄定义中,销售日期已定义刚修改,否则添加
				if(lists.size()>0){
					TtAsActivityAgePO po1 = lists.get(0);
					po1.setUpdateBy(logonUser.getUserId());
					po1.setUpdateDate(new Date());
					if(StringUtil.notNull(beginDate2)){
						d = fmt.parse(beginDate2);
						po1.setSaleDateStart(d);
					}
					if(StringUtil.notNull(endDate2)){
						d = fmt.parse(endDate2);
						po1.setSaleDateEnd(d);
					}
					dao.update(po, po1);
				}else{
					TtAsActivityAgePO po1 = new TtAsActivityAgePO();
					po1.setActivityId(Long.parseLong(activityId));
					po1.setCreateBy(logonUser.getUserId());
					po1.setCreateDate(new Date());
					po1.setDateType(new Long(Constant.SERVICEACTIVITY_DATE_TYPE_01));
					po1.setId(Utility.getLong(SequenceManager.getSequence("")));
					if(StringUtil.notNull(beginDate2)){
						d = fmt.parse(beginDate2);
						po1.setSaleDateStart(d);
					}
					if(StringUtil.notNull(endDate2)){
						d = fmt.parse(endDate2);
						po1.setSaleDateEnd(d);
					}
					dao.insert(po1);
				}
			}
			act.setRedirect(ServiceActivityModelSuccessUrl);
		} catch(Exception e){
			BizException be = new BizException(act,e,ErrorCodeConstant.ADD_FAILURE_CODE,"服务活动管理车龄定义");
			logger.error(logonUser,be);
			act.setException(be);
		}
	}
}