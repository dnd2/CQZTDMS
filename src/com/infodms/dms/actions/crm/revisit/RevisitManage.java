package com.infodms.dms.actions.crm.revisit;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.actions.crm.follow.FollowManage;
import com.infodms.dms.actions.sales.ordermanage.orderaudit.OrderResourceReserveFirst;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.dao.crm.revisit.ReVisitDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TPcCustomerPO;
import com.infodms.dms.po.TPcRevisitPO;
import com.infodms.dms.po.TPcRevistDetailPO;
import com.infodms.dms.po.TcUserPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;

public class RevisitManage {
	private Logger logger = Logger.getLogger(OrderResourceReserveFirst.class);
	private ActionContext act = ActionContext.getContext();
	RequestWrapper request = act.getRequest();
	private final ReVisitDao dao = ReVisitDao.getInstance();

	private final String REVISIT_QUERY_URL = "/jsp/crm/revisit/revisitInit.jsp";// 回访查询界面
	private final String REVISIT_DETAIL = "/jsp/crm/revisit/revisitDetail.jsp";// 回访详情页面
	private final String REVISIT_BACK = "/jsp/crm/revisit/revisitBack.jsp";// 回访填写界面
	public void doInit() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			Map<String,String> map=new HashMap<String, String>();
			map.put("dealerId", logonUser.getDealerId());
			
			String poseRank=CommonUtils.getPoseRank(logonUser);
			if(!Constant.DEALER_USER_LEVEL_05.toString().equals(poseRank)){
				map.put("userId", null);
			}
			List<Map<String,Object>> userList=CommonUtils.queryUser(map);
			FollowManage.getManager(logonUser, act);
			act.setOutData("userList", userList);
			act.setOutData("poseRank", poseRank);
				act.setForword(REVISIT_QUERY_URL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "经销商用户组查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * FUNCTION		:	回访执行的查询方法
	 * @param 		:	
	 * @return		:
	 * @throws		:	
	 * LastUpdate	:	2010-8-30
	 */
	public void revisitQueryList(){
		AclUserBean logonUser = null;
		try {
			RequestWrapper request = act.getRequest();
			logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);	
			String ctmName = CommonUtils.checkNull(request.getParamValue("ctmName"));		//客户名称
			String telephone =  CommonUtils.checkNull(request.getParamValue("telephone"));	//手机
			String startTime = CommonUtils.checkNull(request.getParamValue("startTime"));//回访开始时间
			String endTime =  CommonUtils.checkNull(request.getParamValue("endTime"));	//回访结束时间
			String dealerCode = CommonUtils.checkNull(request.getParamValue("dealerCode"));		//经销商
			String adviser =  CommonUtils.checkNull(request.getParamValue("adviser"));	//顾问
			String revisitType=CommonUtils.checkNull(request.getParamValue("revisitType"));	//回访类型
			String groupId=CommonUtils.checkNull(request.getParamValue("groupId"));//组
			Map<String ,String > map=new HashMap<String,String>();
			map.put("ctmName", ctmName);
			map.put("telephone", telephone);
			map.put("startTime", startTime);
			map.put("endTime", endTime);
			map.put("dealerCode", dealerCode);
			map.put("adviser", adviser);
			map.put("revisitType", revisitType);
			map.put("dealerId", logonUser.getDealerId());
			String poseRank=CommonUtils.getPoseRank(logonUser);
			if(!Constant.DEALER_USER_LEVEL_05.toString().equals(poseRank)){
				map.put("logonId", logonUser.getUserId().toString());
			}
			map.put("groupId", groupId);
			String pageSize=CommonUtils.checkNull(request.getParamValue("pageSize"));	
			pageSize=pageSize==null||"".equals(pageSize)?"10":pageSize;
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage")) 
					: 1; // 处理当前页
			PageResult<Map<String, Object>> ps = dao.getRevisitQueryList(map, Integer.parseInt(pageSize), curPage);
			act.setOutData("ps", ps);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e,
					ErrorCodeConstant.QUERY_FAILURE_CODE, "所有车辆资源查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	} 
	/**
	 * 点击详情
	 */
	public void detailInit() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
				String revisitId=CommonUtils.checkNull(request.getParamValue("revisitId"));
				
				//查询出详情页面
				Map<String,String> map=new HashMap<String,String>();
				map.put("revisitId",revisitId );
				//查询回访主表数据
				List<Map<String,Object>> list=dao.queryDataList(map);
				act.setOutData("dataList", list);
				//查询回访字表数据
				List<Map<String,Object>> detaillist=dao.queryDetailList(map);
				act.setOutData("detailList", detaillist);
				act.setForword(REVISIT_DETAIL);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "经销商用户组查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	/**
	 * 点击回访执行的方法
	 */
	public void detailRevisit() {
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
				String revisitId=CommonUtils.checkNull(request.getParamValue("revisitId"));
				String typeFrom=CommonUtils.checkNull(request.getParamValue("typeFrom"));
				String adviserLogon = "no";//顾问登陆标志
				//查询出详情页面
				Map<String,String> map=new HashMap<String,String>();
				map.put("revisitId",revisitId );
				List<Map<String,Object>> list=dao.queryDataList(map);
				
				//判断是否顾问登陆
				if(CommonUtils.judgeAdviserLogin(logonUser.getUserId().toString())) {
				    adviserLogon = "yes";
				}
				act.setOutData("adviserLogon", adviserLogon);
				act.setOutData("typeFrom", typeFrom);
				act.setOutData("dataList", list);
				act.setForword(REVISIT_BACK);
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "经销商用户组查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	
	/**
	 * 详情页面点击保存时执行的方法
	 */
	public void detailUpdate(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
			    String revisitId=CommonUtils.checkNull(request.getParamValue("revisitId"));//获取回访id
				String prologue=CommonUtils.checkNull(request.getParamValue("prologue"));//开场白
				String conclusion=CommonUtils.checkNull(request.getParamValue("conclusion"));//结束语
				String reason=CommonUtils.checkNull(request.getParamValue("reason"));//为成功原因
				String comment=CommonUtils.checkNull(request.getParamValue("comment"));//总体评价
				String tacks=CommonUtils.checkNull(request.getParamValue("tacks"));//处理项
				String tacksResult=CommonUtils.checkNull(request.getParamValue("tacksResult"));//处理结果
				String tacksFinish=CommonUtils.checkNull(request.getParamValue("tacksFinish"));//处理完成
				String ifVisit=CommonUtils.checkNull(request.getParamValue("ifVisit"));//是否成功
				String [] comments=request.getParamValues("comments");
				String [] tips=request.getParamValues("tips");
				String [] revisitNums=request.getParamValues("revisitNum");
				String [] revisitCycles=request.getParamValues("revisitCycle");
				String [] revisitSpeaks=request.getParamValues("revisitSpeak");
				//根据上一次任务的类型得到下一下任务的类型从而判断需要加多少天
				//通过revisitId 获取回访的数据
				TPcRevisitPO tpr0=new TPcRevisitPO();
				tpr0.setRevisitId(new Long(revisitId));
				tpr0=(TPcRevisitPO) dao.select(tpr0).get(0);
				//定义需要加的日期
				int nextDate=0;
				//获取下次任务类型
				String nextType="0";
				//如果是季度回访增加下一个季度回访任务
		       if("60431004".equals(tpr0.getRevisitType())){
					nextDate=90;
					nextType="60431004";
				}
				TPcRevisitPO tpr=new TPcRevisitPO();
				tpr.setRevisitId(new Long(revisitId));
				TPcRevisitPO tpr1=new TPcRevisitPO();
				tpr1.setPrologue(prologue);
				tpr1.setConclusions(conclusion);
				tpr1.setFailReason(reason);
				tpr1.setTracksResult(tacksResult);
				tpr1.setTracksFinish(tacksFinish);
				tpr1.setEvaluate(comment);
				tpr1.setTracks(tacks);
				tpr1.setTaskStatus(Constant.TASK_STATUS_02);
				tpr1.setFinishDate(new Date());
				if(ifVisit!=null&&!"".equals(ifVisit)){
					tpr1.setIfRevisit(new Integer(ifVisit));
				}
				int flag=dao.update(tpr, tpr1);
				
				//新增回访子表数据
				for(int i=0;i<comments.length;i++){
					String detail_id=SequenceManager.getSequence("");
					TPcRevistDetailPO tprd=new TPcRevistDetailPO();
					tprd.setDetailId(new Long(detail_id));
					tprd.setRevisitId(new Long(revisitId));
					tprd.setComments(comments[i]);
					tprd.setClientTip(tips[i]);
					tprd.setCreateDate(new Date());
					tprd.setCreateBy(logonUser.getUserId());
					tprd.setStatus(new Long(10011001));
					tprd.setRevisitCycle(revisitCycles[i]);
					tprd.setRevisitNum(revisitNums[i]);
					tprd.setRevisitSpeak(revisitSpeaks[i]);
					dao.insert(tprd);
				}
				
				//获取客户信息
				TPcCustomerPO tpc=new TPcCustomerPO();
				tpc.setCustomerId(tpr0.getCustomerId());
				tpc=(TPcCustomerPO) dao.select(tpc).get(0);
				SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
				//生成下一个季度回访回访任务
				if(Constant.REVISIT_TYPE_04.toString().equals(tpr0.getRevisitType())){
					TPcRevisitPO tpr2=new TPcRevisitPO();
					String seqId=SequenceManager.getSequence("");
					tpr2.setRevisitId(new Long(seqId));
					tpr2.setCreateBy(logonUser.getUserId().toString());
					tpr2.setCreateDate(new Date());
					//获取下次回访时间
					Date d=new Date();
					d=tpr0.getRevisitDate();
					Calendar c=Calendar.getInstance();
					c.setTime(d);
					c.add(Calendar.DAY_OF_MONTH, nextDate);
					tpr2.setRevisitDate(c.getTime());
					tpr2.setRevisitType(nextType);
					tpr2.setCustomerId(tpr0.getCustomerId());
					tpr2.setPreviousTask(new Long(revisitId));
					tpr2.setRemark(tpr0.getRemark());
					tpr2.setBuyDate(tpr0.getBuyDate());
					tpr2.setVinId(tpr0.getVinId());
					tpr2.setTaskStatus(Constant.TASK_STATUS_01);
					tpr2.setRevisitDealer(tpr0.getRevisitDealer());
					tpr2.setRevisitAdviser(tpr0.getRevisitAdviser());
					dao.insert(tpr2);
					//新增提醒信息
					CommonUtils.addRemindInfo(Constant.REMIND_TYPE_12.toString(), revisitId, tpc.getCustomerId().toString(), logonUser.getDealerId(), tpc.getAdviser().toString(), sdf.format(c.getTime()),"");
				}
				CommonUtils.setRemindDone(revisitId,Constant.REMIND_TYPE_12.toString());
				CommonUtils.addContackPoint(Constant.POINT_WAY_09, "", tpc.getCustomerId().toString(), logonUser.getUserId().toString(), logonUser.getDealerId());
				act.setOutData("flag", flag);
				
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "经销商用户组查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 经销商用户组的添加
	 */
	public void initData(){
		AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
		try {
				String codeId=request.getParamValue("codeId");
				Map<String,String> map=new HashMap<String,String>();
				map.put("codeId", codeId);
				List<Map<String,Object>> list=dao.queryDataList(map);
				act.setOutData("dataList", list);
				
		} catch (Exception e) {
			BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "经销商用户组查询");
			logger.error(logonUser, e1);
			act.setException(e1);
		}
	}
	
}
