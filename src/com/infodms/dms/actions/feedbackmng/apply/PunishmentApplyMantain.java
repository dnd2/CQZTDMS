/**********************************************************************
* <pre>
* FILE : PunishmentApplyMantain.java
* CLASS : PunishmentApplyMantain
*
* AUTHOR : PGM
*
* FUNCTION : 奖惩审批表维护.
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
 * $Id: PunishmentApplyMantain.java,v 1.3 2010/09/29 13:07:59 lis Exp $
 */
package com.infodms.dms.actions.feedbackmng.apply;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.bean.PunishmentApplyMantainBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.getCompanyId.GetOemcompanyId;
import com.infodms.dms.dao.feedbackMng.PunishmentApplyMantainDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TtIfRewardAuditPO;
import com.infodms.dms.po.TtIfRewardDealerPO;
import com.infodms.dms.po.TtIfRewardPO;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;

/**
 * Function       :  奖惩审批表维护
 * @author        :  PGM
 * CreateDate     :  2010-05-17
 * @version       :  0.1
 */
public class PunishmentApplyMantain {
	private Logger logger = Logger.getLogger(PunishmentApplyMantain.class);
	private PunishmentApplyMantainDao dao = PunishmentApplyMantainDao.getInstance();
	private PunishmentApplyMantainDao punishDao = PunishmentApplyMantainDao.getInstance();
	private ActionContext act = ActionContext.getContext();//获取ActionContext
	private AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
	private final String MantainInit = "/jsp/feedbackMng/apply/punishmentApplyMantain.jsp";//查询页面
	private final String MantainAdd = "/jsp/feedbackMng/apply/punishmentApplyMantainAdd.jsp";//新增页面
	private final String MantainUpdate = "/jsp/feedbackMng/apply/punishmentApplyMantainUpdate.jsp";//修改页面
	private final String Mantaininfo = "/jsp/feedbackMng/apply/punishmentMantainDetail.jsp";//详细页面
	
	/**
	 * Function       :  奖惩审批表页面初始化
	 * @param         :  
	 * @return        :  奖惩审批表
	 * @throws        :  Exception
	 * LastUpdate     :  2010-05-17
	 */
	public void punishmentApplyMantainInit(){
		try {
			act.setForword(MantainInit);
		} catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"奖惩审批表");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * Function       :  根据条件查询奖惩审批表中符合条件的信息，其中包括：服务大区待上报、轿车公司审核驳回
	 * @param         :  request-工单号、服务中心名称、创建时间、类型
	 * @return        :  奖惩审批信息
	 * @throws        :  Exception
	 * LastUpdate     :  2010-05-17
	 */
	public void punishmentApplyMantainQuery(){
		try {
			RequestWrapper request = act.getRequest();
			String orderId = request.getParamValue("orderId");             //工单号
			String dealerCode = request.getParamValue("dealerCode");       //经销商代码
			String dealerName = request.getParamValue("dealerName");       //经销商ID
			String beginTime = request.getParamValue("beginTime");         //创建开始时间
			String endTime = request.getParamValue("endTime");             //创建结束始时间
			String rewardType = request.getParamValue("rewardType");       //类型
			
			//当开始时间和结束时间相同时
			if(null!=beginTime&&!"".equals(beginTime)&&null!=endTime&&!"".equals(endTime)){
					beginTime = beginTime+" 00:00:00";
					endTime = endTime+" 23:59:59";
			}
			PunishmentApplyMantainBean MantainBean = new PunishmentApplyMantainBean();
			MantainBean.setOrderId(orderId);
			MantainBean.setDealerCode(dealerCode);
			MantainBean.setDealerName(dealerName);
			MantainBean.setBeginTime(beginTime);
			MantainBean.setEndTime(endTime);
			MantainBean.setRewardType(rewardType);
			Integer curPage = request.getParamValue("curPage") !=null ? Integer.parseInt(request.getParamValue("curPage")) : 1;
			PageResult<Map<String, Object>> ps = dao.getAllPunishmentApplyInfo(MantainBean,curPage,Constant.PAGE_SIZE );
			act.setOutData("ps", ps);
			act.setForword(MantainInit);
		}catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"奖惩审批");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * Function       :  获得申请单位(默认登陆人姓名）、联系电话(默认登录人电话)
	 * @param         :  response-申请单位（默认登陆人姓名）、联系电话(默认登录人电话)
	 * @return        :  奖惩审批信息
	 * @throws        :  ParseException
	 * LastUpdate     :  2010-05-17
	 */
	public void getPunishmentApplyMantainInfo(){
		String linkMan=logonUser.getName();//申请单位（默认登陆人姓名）
	  //String tel=logonUser.getTel();//联系电话(默认登录人电话)
		act.setOutData("linkMan", linkMan);
		act.setForword(MantainAdd);
	}
	/**
	 * Function       :  由服务大区增加奖惩审批信息
	 * @param         :  request-工单号、服务中心名称、类型、申请单位、联系电话、奖励方式（处罚方式）、奖励金额（处罚金额）、申请日期、申请内容
	 * @return        :  奖惩审批信息
	 * @throws        :  ParseException
	 * LastUpdate     :  2010-08-11
	 */
	public void punishmentApplyMantainAdd() throws ParseException{
		RequestWrapper request = act.getRequest();
		Long companyId=GetOemcompanyId.getOemCompanyId(logonUser);
		String orderId = SequenceManager.getSequence("JCAO"); //调用公共方法，工单号系统自动生成
		String rewardType = request.getParamValue("rewardType"); //类型
		String linkMan = request.getParamValue("linkMan"); //申请单位(默认登陆人姓名)
		String tel = request.getParamValue("tel");  //联系电话(默认登录人电话)
		String rewardMode = request.getParamValue("rewardMode"); //奖励方式
		String rewardModePunish = request.getParamValue("rewardModePunish"); //惩罚方式
		String rewardMoney = request.getParamValue("rewardMoney"); //奖励金额
		String rewardMoneyPunish = request.getParamValue("rewardMoneyPunish"); //惩罚金额
		String rewardDate = request.getParamValue("rewardDate"); //申请日期
		String rewardContent = request.getParamValue("rewardContent"); //申请内容
		String dealerId = request.getParamValue("dealerId"); //经销商ID
		
		TtIfRewardPO  RewardPO =new TtIfRewardPO();
		TtIfRewardDealerPO RewardDealerPO  = new TtIfRewardDealerPO();
		RewardPO.setOrderId(orderId);
		if(null!=rewardType){
			RewardPO.setRewardType(Integer.parseInt(rewardType));
		}
		if(rewardType.equals(Constant.PUNISHMENT_01)){//类型:奖励
			RewardPO.setRewardMode(Integer.parseInt(rewardMode));
			if(rewardMode.equals(Constant.REWARD_02)){//现金奖励
			RewardPO.setRewardMoney(Double.parseDouble(rewardMoney));
			}
		}else if(rewardType.equals(Constant.PUNISHMENT_02)){//类型:处罚
			RewardPO.setRewardMode(Integer.parseInt(rewardModePunish));
			if(rewardModePunish.equals(Constant.PUNISH_03)){//罚款
			RewardPO.setRewardMoney(Double.parseDouble(rewardMoneyPunish));
			}
		}
		RewardPO.setLinkMan(linkMan);
		RewardPO.setTel(tel);
		if(null!=rewardDate){
			DateFormat df=new SimpleDateFormat("yyyy-MM-dd");
			RewardPO.setRewardDate(df.parse(rewardDate));
		}
		RewardPO.setCompanyId(companyId);
		RewardPO.setRewardContent(rewardContent);//申请内容
		RewardPO.setCreateDate(new Date());//创建时间
		RewardPO.setCreateBy(logonUser.getUserId());//创建人（当前系统登录人）
		RewardPO.setUpdateDate(new Date());//修改时间
		RewardPO.setUpdateBy(logonUser.getUserId());//修改人
//		RewardPO.setDealerId(Long.parseLong(dealerId));//经销商ID
		RewardPO.setRewardStatus(Constant.AWARD_PUNISH_STATUS_UNREPORT);//工单状态 ：10151001， 表示：待上报
		//modify by xiayanpeng begin 新增时插入 IS_DEL=0
		RewardPO.setIsDel(new Integer(Constant.IS_DEL_00));
		//奖惩供应商对应表
		RewardDealerPO.setCreateDate(new Date());//创建时间
		RewardDealerPO.setCreateBy(logonUser.getUserId());//创建人（当前系统登录人）
		RewardDealerPO.setUpdateDate(new Date());//修改时间
		RewardDealerPO.setUpdateBy(logonUser.getUserId());//修改人
		RewardDealerPO.setOrderId(orderId);
		//modify by xiayanpeng end 
		try{
		    PunishmentApplyMantainDao.punishmentApplyMantainAdd(RewardPO);
		    if(null!=dealerId&&!"".equals(dealerId)){
		    	String[] dealerArr = dealerId.split(",");
		    	for(int i=0;i<dealerArr.length;i++){
		    		RewardDealerPO.setDealerId(Long.parseLong(dealerArr[i]));
		    		PunishmentApplyMantainDao.punishmentApplyDealerAdd(RewardDealerPO);
		    	}
		    }
		    act.setRedirect(MantainInit);//新增---重定向(解决新增刷新增加信息)
		  }catch(Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.DELETE_FAILURE_CODE,"增加奖惩审批信息");
			logger.error(logonUser,e1);
		 	act.setException(e1);
		  }
	}
	/**
	 * Function       :  查询奖惩详细信息
	 * @param         :  request-工单号
	 * @return        :  奖惩审批信息
	 * @throws        :  Exception
	 * LastUpdate     :  2010-08-12
	 */
	public void getOrderIdInfo(){ 
		RequestWrapper request = act.getRequest();
		String orderId=request.getParamValue("orderId");//工单号
		try {
			PunishmentApplyMantainBean MantainBean=dao.getOrderIdInfo(orderId);
			List<PunishmentApplyMantainBean> dealerList = dao.getDealerName(orderId);
			StringBuffer dealerName = new StringBuffer("");
			if(null!=dealerList&&dealerList.size()!=0){
					for(PunishmentApplyMantainBean  dealerNameBean:dealerList){
						if(null!=dealerNameBean.getDealerName()){
							dealerName.append(dealerNameBean.getDealerName()+",");
						}
					}
					if(null!=dealerName){
						 dealerName.deleteCharAt(dealerName.length()-1);
						 MantainBean.setDealerName(dealerName.toString());
					}
			}
			List<PunishmentApplyMantainBean> MantainList=dao.getOrderIdInfoList(orderId);
			request.setAttribute("MantainBean", MantainBean);
			request.setAttribute("MantainList", MantainList);
			dealerName.delete(0,dealerName.length());
			act.setForword(Mantaininfo);
		} catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"奖惩工单详细信息");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * Function       :  修改前置查询---查询奖惩详细信息
	 * @param         :  request-工单号
	 * @return        :  奖惩审批信息/审批历史记录
	 * @throws        :  Exception
	 * LastUpdate     :  2010-05-17
	 */
	public void punishmentApplyMantainUpdateInit(){ 
		RequestWrapper request = act.getRequest();
		String orderId=request.getParamValue("orderId");//工单号
		try {
			PunishmentApplyMantainBean RewardPO = new PunishmentApplyMantainBean();
			RewardPO=dao.punishmentApplyMantainUpdateInit(orderId);
			List<PunishmentApplyMantainBean> dealerList = punishDao.getDealerName(orderId);
			StringBuffer dealerCode = new StringBuffer("");
			StringBuffer dealerId = new StringBuffer("");
			if(null!=dealerList&&dealerList.size()!=0){
					for(PunishmentApplyMantainBean  dealerNameBean:dealerList){
						if(null!=dealerNameBean.getDealerName()){
							dealerCode.append(dealerNameBean.getDealerName()+",");
							dealerId.append(dealerNameBean.getDealerId()+",");
						}
					}
					if(null!=dealerCode){
						dealerCode.deleteCharAt(dealerCode.length()-1);
						dealerId.deleteCharAt(dealerId.length()-1);
						 RewardPO.setDealerCode(dealerCode.toString());
						 RewardPO.setDealerId(dealerId.toString());
					}
			}
			request.setAttribute("RewardPO", RewardPO);
			dealerCode.delete(0,dealerCode.length());
			dealerId.delete(0, dealerId.length());
			act.setForword(MantainUpdate);
		} catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"奖惩工单详细信息");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	/**
	 * Function       :  修改奖惩细信息
	 * @param         :  request-工单号、服务中心名称、类型、申请单位、联系电话、奖励方式（处罚方式）、奖励金额（处罚金额）、申请日期、申请内容
	 * @return        :  奖惩审批信息
	 * @throws        :  ParseException
	 * LastUpdate     :  2010-05-17
	 */
	public void punishmentApplyMantainUpdate() throws ParseException{ 
		RequestWrapper request = act.getRequest();
		TtIfRewardPO  RewardPO =new TtIfRewardPO();//修改内容
		TtIfRewardPO  RewardPOCon =new TtIfRewardPO();//条件
		String orderId=request.getParamValue("orderId");//工单号
		String rewardType = request.getParamValue("rewardType"); //类型
		String linkMan = request.getParamValue("linkMan"); //申请单位(默认登陆人姓名)
		String tel = request.getParamValue("tel");  //联系电话(默认登录人电话)
		String rewardMode = request.getParamValue("rewardMode"); //奖励方式
		String rewardModePunish = request.getParamValue("rewardModePunish"); //惩罚方式
		String rewardMoney = request.getParamValue("rewardMoney"); //奖励金额
		String rewardMoneyPunish = request.getParamValue("rewardMoneyPunish"); //惩罚金额
		String rewardDate = request.getParamValue("rewardDate"); //申请日期
		String rewardContent = request.getParamValue("rewardContent"); //申请内容
		String dealerId = request.getParamValue("dealerId"); //经销商ID

		if(rewardType.equals(Constant.PUNISHMENT_01)){//类型:奖励
			RewardPO.setRewardMode(new Integer(rewardMode));
			if(rewardMode.equals(Constant.REWARD_02)){//现金奖励
				RewardPO.setRewardMoney(Double.parseDouble(rewardMoney));
			   }
		}else if(rewardType.equals(Constant.PUNISHMENT_02)){//类型:处罚
			RewardPO.setRewardMode(new Integer(rewardModePunish));
			if(rewardModePunish.equals(Constant.PUNISH_03)){//罚款
				RewardPO.setRewardMoney(Double.parseDouble(rewardMoneyPunish));
			  }
		}
		
		if(null!=rewardType){
			RewardPO.setRewardType(Integer.parseInt(rewardType));
		}
		RewardPO.setLinkMan(linkMan);
		RewardPO.setTel(tel);
		if(null!=rewardDate){
			DateFormat df=new SimpleDateFormat("yyyy-MM-dd");
			RewardPO.setRewardDate(df.parse(rewardDate));
		}
		RewardPO.setRewardContent(rewardContent);//申请内容
		RewardPO.setUpdateDate(new Date());//修改时间
		RewardPO.setUpdateBy(logonUser.getUserId());//修改人
		RewardPO.setRewardStatus(Constant.AWARD_PUNISH_STATUS_UNREPORT);//工单状态 ：10151001， 表示：待上报
//		RewardPO.setDealerId(Long.parseLong(dealerId));//经销商ID
		RewardPOCon.setOrderId(orderId);
		List<TtIfRewardDealerPO> list = new ArrayList<TtIfRewardDealerPO>();
		try{
			PunishmentApplyMantainDao.punishmentApplyMantainUpdate(RewardPOCon,RewardPO);
		    if(null!=dealerId&&!"".equals(dealerId)){
		    	String[] dealerArr = dealerId.split(",");
		    	for(int i=0;i<dealerArr.length;i++){
		    		TtIfRewardDealerPO RewardDealerPO  = new TtIfRewardDealerPO();//修改奖惩经销商对应表
		    		//奖惩供应商对应表
		    		RewardDealerPO.setCreateDate(new Date());//创建时间
		    		RewardDealerPO.setCreateBy(logonUser.getUserId());//创建人（当前系统登录人）
		    		RewardDealerPO.setUpdateDate(new Date());//修改时间
		    		RewardDealerPO.setUpdateBy(logonUser.getUserId());//修改人
		    		RewardDealerPO.setOrderId(orderId);//orderId
		    		RewardDealerPO.setDealerId(Long.parseLong(dealerArr[i]));
		    		list.add(RewardDealerPO);
		    	}
		    	PunishmentApplyMantainDao.punishmentApplyDealerUpdate(orderId,list);
		    }
			punishmentApplyMantainInit();
		  }catch(Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.DELETE_FAILURE_CODE,"修改奖惩审批信息");
			logger.error(logonUser,e1);
		 	act.setException(e1);
		  }
	}
	/**
	 * Function       :  删除奖惩细信息
	 * @param         :  request-工单号
	 * @return        :  奖惩审批信息
	 * @throws        :  ParseException
	 * LastUpdate     :  2010-05-17
	 */
	public void punishmentApplyMantainDelete(){ 
		RequestWrapper request = act.getRequest();
		String orderId=request.getParamValue("orderIds");//工单号
		TtIfRewardPO  RewardPO =new TtIfRewardPO();//修改内容
		try{
			if (orderId!=null&&!"".equals(orderId)) {
				String [] orderIdArray = orderId.split(","); //取得所有orderId放在数组中
				RewardPO.setUpdateDate(new Date());//修改时间
				RewardPO.setIsDel(Integer.parseInt(Constant.IS_DEL_01));//IS_DEL_01 = "1" 逻辑删除;IS_DEL_00 = "0" 逻辑未删除
				RewardPO.setUpdateBy(logonUser.getUserId());//修改人
				PunishmentApplyMantainDao.punishmentApplyMantainDelete(orderIdArray,RewardPO);
				PunishmentApplyMantainDao.punishmentApplyDealerDelete(orderIdArray);
			}
			act.setOutData("returnValue", 1);//returnValue 值：1，表示成功
		  }catch(Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.DELETE_FAILURE_CODE,"删除奖惩细信息");
			logger.error(logonUser,e1);
		 	act.setException(e1);
		  }
	}
	/**
	 * Function       :  上报奖惩细信息，同时将上报信息维护到奖惩审批明细表中
	 * @param         :  request-工单号
	 * @return        :  奖惩审批信息
	 * @throws        :  ParseException
	 * LastUpdate     :  2010-05-17
	 */
	public void punishmentApplyMantainReport(){ 
		RequestWrapper request = act.getRequest();
		String orderId=request.getParamValue("orderIds");//工单号
		TtIfRewardPO  RewardPO =new TtIfRewardPO();//上报内容
		try{
			if (orderId!=null&&!"".equals(orderId)) {
				String [] orderIdArray = orderId.split(","); //取得所有orderId放在数组中
				RewardPO.setUpdateDate(new Date());//上报时间
				RewardPO.setUpdateBy(logonUser.getUserId());//上报人
				
				TtIfRewardAuditPO  RewardAuditPO =new TtIfRewardAuditPO();//奖惩审批明细表
				RewardAuditPO.setAuditDate(new Date());//审核时间
				RewardAuditPO.setAuditBy(logonUser.getUserId());//审核人
				RewardAuditPO.setOrgId(logonUser.getOrgId());//组织ID
				RewardAuditPO.setAuditStatus(new Integer(Constant.AWARD_PUNISH_STATUS_REPORTED));//审批状态:10151002, 表示：已上报
				PunishmentApplyMantainDao.punishmentApplyMantainReport(orderIdArray,RewardPO,RewardAuditPO);
			}
			act.setOutData("returnValue", 1);//returnValue 值：1，表示成功
		  }catch(Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.DELETE_FAILURE_CODE,"上报奖惩细信息");
			logger.error(logonUser,e1);
		 	act.setException(e1);
		  }
	}
}