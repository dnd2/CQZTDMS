package com.infodms.dms.actions.customerRelationships.classMonitorManage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.dao.customerRelationships.CRMSortDAO;
import com.infodms.dms.dao.customerRelationships.CallRecordDao;
import com.infodms.dms.dao.customerRelationships.SeatsSetDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TtCrmSeatsPO;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;
/**
 * 
 * @ClassName     : ClassMonitorManage 
 * @Description   : 班长监控
 * @author        : wangming
 * CreateDate     : 2013-4-8
 */
public class ClassMonitorManage{
	private static Logger logger = Logger.getLogger(ClassMonitorManage.class);
	// 班长监控初始化页面
	private final String classMonitorManageUrl = "/jsp/customerRelationships/classMonitorManage/classMonitorManage.jsp";
	

	ActionContext act = ActionContext.getContext();
	AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
	RequestWrapper request = act.getRequest();
	
	
	/**
	 * 班长监控初始化
	 */
	public void classMonitorManageInit(){		
		try{
			List<List<TtCrmSeatsPO>> list = refreshSeatsData();
			act.setOutData("ttCrmSeatsPOList", list);
			act.setOutData("incomingData", incomingData());
			act.setOutData("freeData", freeData());
			act.setOutData("busyData", busyData());
			act.setForword(classMonitorManageUrl);
		}catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ACTION_NAME_ERROR_CODE,"班长监控初始化");
			logger.error(logger,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 
	 * @Title      : refreshSeatsData
	 * @Description: 刷新坐席数据并返回处理后的前台显示数据
	 * @param      : @return List<List<TtCrmSeatsPO>>   
	 * LastDate    : 2013-4-8
	 */
	private List<List<TtCrmSeatsPO>> refreshSeatsData(){
		SeatsSetDao dao = SeatsSetDao.getInstance();
		List<TtCrmSeatsPO> list = dao.queryTtCrmSeatsPONOAdminAll();
		
		List<List<TtCrmSeatsPO>> data = new ArrayList<List<TtCrmSeatsPO>>();
		final int rowNum = 5; //每行多少数据
		int row = list.size()/rowNum+(list.size()%rowNum>0?1:0); //总共多少行
		for(int i=0;i<row;i++){//循环每行分别添加到每行的LIST
			if(row == (i+1) )
				//处理只有一行 或 多行中最后一行的数据
				data.add(list.subList(i*rowNum, list.size()));
			else
				data.add(list.subList(i*rowNum, i*rowNum+rowNum));
		}
		return data;
	}
	/**
	 * 
	 * @Title      : ChangeWorkStatus
	 * @Description: 改变用户工作状态 
	 * @param      : @param account 用户userId
	 * @param      : @param seatStatus 工作状态值  
	 * LastDate    : 2013-4-17
	 * @author wangming
	 */
	public void changeWorkStatus(long userId,int seatStatus){
		SeatsSetDao dao = SeatsSetDao.getInstance();
		dao.changWorkStatus(userId,seatStatus);
	}
	/**
	 * 判断该用户是否是当天排班的坐席人员
	 * 
	 * @return int 0->不是坐席人员 ; 1->是当天排班的坐席人员; 2->不是当天排天的坐席人员
	 */
	public int seatLogin(){
		SeatsSetDao dao= SeatsSetDao.getInstance();
		boolean isSeat = dao.isSeat(logonUser.getUserId());
		if(isSeat){
			CRMSortDAO crmDao = CRMSortDAO.getInstance();
			if(crmDao.isSortCurrentDay(logonUser.getUserId())){
				return 1;
			}else return 2;
		}else{
			return 0;
		}
	}
	
	
	public int isSeatManeger()
	{
		SeatsSetDao dao= SeatsSetDao.getInstance();
		boolean isSeat = dao.isSeatManeger(logonUser.getUserId());
		if(isSeat)
		{
			return 1;
		}else
		{
			return 0;
		}
	}
	
	
	private int incomingData(){
		SeatsSetDao dao = SeatsSetDao.getInstance();
		return dao.queryIncomingCount();
	}
	
	private int freeData(){
		SeatsSetDao dao = SeatsSetDao.getInstance();
		return dao.queryFreeCount();
	}
	
	private int busyData(){
		SeatsSetDao dao = SeatsSetDao.getInstance();
		return dao.queryBusyCount();
	}
	
	public void logupdate()
	{
		try{
			String RpcStat = CommonUtils.checkNull(request.getParamValue("RpcStat")); 
			logonUser.setRpcStat(RpcStat) ;
			
		}catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.SPECIAL_MEG,"强插");
			logger.error(logger,e1);
			act.setException(e1);
		}
	}
	
	public void pullSta(){
		try{
			String actn = CommonUtils.checkNull(request.getParamValue("actn")); 
			String stat = CommonUtils.checkNull(request.getParamValue("stat"));
			SeatsSetDao dao = SeatsSetDao.getInstance();
			StringBuffer sb= new StringBuffer();
			sb.append("select t.SE_WORK_STATUS from tt_crm_seats t where t.SE_SEATS_NO =" + actn);
			List<TtCrmSeatsPO> list= dao.select(TtCrmSeatsPO.class, sb.toString(), null);
			if((""+list.get(0).getSeWorkStatus()).equals("95131001") )
			{
				act.setOutData("act",actn );
			}else
			{
				StringBuffer sql= new StringBuffer();
				sql.append("UPDATE TT_CRM_SEATS t set t.SE_WORK_STATUS ="+stat+"  where t.SE_SEATS_NO ='"+actn+"'");
				dao.update(sql.toString(), null);
			}

		
		}catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.SPECIAL_MEG,"强插");
			logger.error(logger,e1);
			act.setException(e1);
		}
	}
	//强插
	public void forceInsert(){
		try{
			String actn = CommonUtils.checkNull(request.getParamValue("actn")); 
			TtCrmSeatsPO seatsPO = new TtCrmSeatsPO();
			seatsPO.setSeSeatsNo(actn);
			SeatsSetDao dao = SeatsSetDao.getInstance();
			List<TtCrmSeatsPO> list= dao.select(seatsPO);
			act.setOutData("success", "true");
			act.setOutData("actn", actn);
			act.setOutData("SE_EXT", list.get(0).getSeExt());
		}catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.SPECIAL_MEG,"强插");
			logger.error(logger,e1);
			act.setException(e1);
		}
	}
	//挂断
	public void hangup(){
		try{
			String ids = CommonUtils.checkNull(request.getParamValue("ids")); 
			act.setOutData("success", "true");
		}catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.SPECIAL_MEG,"挂断");
			logger.error(logger,e1);
			act.setException(e1);
		}
	}
	//强制离席(强拆)
	public void forceMoveSeatPull(){
		try{
			String ids = CommonUtils.checkNull(request.getParamValue("ids")); 
			act.setOutData("success", "true");
		}catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.SPECIAL_MEG,"强制离席(强拆)");
			logger.error(logger,e1);
			act.setException(e1);
		}
	}
	//拦截
	public void intercept(){
		try{
			String ids = CommonUtils.checkNull(request.getParamValue("ids")); 
			act.setOutData("success", "true");
		}catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.SPECIAL_MEG,"拦截");
			logger.error(logger,e1);
			act.setException(e1);
		}
	}
	//强制离席
	public void forceMoveSeat(){
		try{
			String ids = CommonUtils.checkNull(request.getParamValue("ids")); 
			act.setOutData("success", "true");
		}catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.SPECIAL_MEG,"强制离席");
			logger.error(logger,e1);
			act.setException(e1);
		}
	}
	//置闲
	public void setFree(){
		try{
			String ids = CommonUtils.checkNull(request.getParamValue("ids")); 
			act.setOutData("success", "true");
		}catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.SPECIAL_MEG,"置闲");
			logger.error(logger,e1);
			act.setException(e1);
		}
	}
	//置忙
	public void setBusy(){
		try{
			String ids = CommonUtils.checkNull(request.getParamValue("ids")); 
			act.setOutData("success", "true");
		}catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.SPECIAL_MEG,"置忙");
			logger.error(logger,e1);
			act.setException(e1);
		}
	}
}