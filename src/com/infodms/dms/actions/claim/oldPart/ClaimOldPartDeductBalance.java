package com.infodms.dms.actions.claim.oldPart;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.bean.DeductBalanceBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.DBLockUtil;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.common.getCompanyId.GetOemcompanyId;
import com.infodms.dms.dao.claim.auditing.ClaimAuditingDao;
import com.infodms.dms.dao.claim.oldPart.ClaimOldPartDeductBalanceDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TmDealerPO;
import com.infodms.dms.po.TtAsWrApplicationPO;
import com.infodms.dms.po.TtAsWrApppaymentPO;
import com.infodms.dms.po.TtAsWrBalancePO;
import com.infodms.dms.po.TtAsWrDeductBalanceDetailPO;
import com.infodms.dms.po.TtAsWrDeductBalancePO;
import com.infodms.dms.po.TtAsWrDeductPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;

/**
 * 旧件抵扣单结算，抵扣过程同索赔单结算
 * @author XZM
 */
public class ClaimOldPartDeductBalance {
	public Logger logger = Logger.getLogger(ClaimOldPartDeductBalance.class);
	/** 旧件抵扣通知单查询首页 */
	private final String DEDUCTION_BALANCE_TOP = "/jsp/claim/oldPart/oldPartDeductBalance.jsp";
	private final String DEDUCTION_BALANCE_TOP_NEW = "/jsp/claim/oldPart/newOldPartDeductBalance.jsp";
	
	/**
	 * 呈现旧件抵扣通知单查询首页
	 */
	public void deductBalanceInit(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try{
			act.setForword(this.DEDUCTION_BALANCE_TOP);
		} catch (Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"旧件抵扣单结算");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/*
	 * new 呈现旧件抵扣通知单查询首页
	 * add by lishuai103@yahoo.cn
	 */
	
	public void deductBalanceNewInit(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try{
			//取得该用户拥有的产地权限
			String yieldly = CommonUtils.findYieldlyByPoseId(logonUser.getPoseId());
			
			act.setOutData("yieldly", yieldly);
			act.setForword(this.DEDUCTION_BALANCE_TOP_NEW);
		} catch (Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"旧件抵扣单结算");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 统计查询要结算经销商的抵扣单结算日期
	 * 其他限制：
	 *      抵扣单通知日期超过7日的可以结算(留给经销商和车厂的沟通时间)
	 *      现在在页面控制用户选择七天前的结算日期
	 * [修改] 2010-07-28 加入产地，按产地进行结算，将各个经销商对应产地区分开结算
	 *        注：现在产地信息从索赔单中取得，依赖一抵扣单对应一索赔单，如有变动，需调整
	 */
	public void queryDeductBalance(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		Long companyId=GetOemcompanyId.getOemCompanyId(logonUser);
		try{
			RequestWrapper request = act.getRequest();
			String dealerCodes = request.getParamValue("DEALER_CODE");//经销商代码集合(以","分隔)
			String dealerName = request.getParamValue("DEALER_NAME");//经销商名称
			String lastDay = request.getParamValue("LAST_DAY");//截止日期
			String deductStatus = request.getParamValue("DEDUCT_STATUS");//抵扣单状态
			String yieldly = request.getParamValue("YIELDLY");//产地
			Integer curPage = request.getParamValue("curPage")==null?1
					:Integer.parseInt(request.getParamValue("curPage"));//分页首页代码
			
			if(Utility.testString(lastDay))
				lastDay = lastDay + " 23:59:59";
			
			DeductBalanceBean conditionBean = new DeductBalanceBean();
			conditionBean.setDealerCodes(dealerCodes);
			conditionBean.setDealerName(dealerName);
			conditionBean.setLastDay(lastDay);
			conditionBean.setDeductStatus(deductStatus);
			conditionBean.setIsDel(Constant.IS_DEL_00);//未删除的抵扣单
			conditionBean.setCompanyId(companyId);
			conditionBean.setYieldly(yieldly);
			
			ClaimOldPartDeductBalanceDao deductBalanceDao = ClaimOldPartDeductBalanceDao.getInstance();
			//统计要结算经销商对应的抵扣单信息
			PageResult<Map<String,Object>> result = deductBalanceDao.queryDeductBalance(conditionBean,
					curPage, Constant.PAGE_SIZE);
			
			act.setOutData("ps", result);
		} catch (Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"旧件抵扣单结算");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/*
	 * new queryDeductBalance
	 */
	
	public void newQueryDeductBalance(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		Long companyId=GetOemcompanyId.getOemCompanyId(logonUser);
		try{
			RequestWrapper request = act.getRequest();
			String dealerCodes = request.getParamValue("DEALER_CODE");//经销商代码集合(以","分隔)
			String dealerName = request.getParamValue("DEALER_NAME");//经销商名称
			String lastDay = request.getParamValue("LAST_DAY");//截止日期
			String deductStatus = request.getParamValue("DEDUCT_STATUS");//抵扣单状态
			String yieldly = request.getParamValue("YIELDLY");//产地
			String yieldlys = CommonUtils.findYieldlyByPoseId(logonUser.getPoseId());       //该用户拥有的产地权限
			Integer curPage = request.getParamValue("curPage")==null?1
					:Integer.parseInt(request.getParamValue("curPage"));//分页首页代码
			
			if(Utility.testString(lastDay))
				lastDay = lastDay + " 23:59:59";
			
			DeductBalanceBean conditionBean = new DeductBalanceBean();
			conditionBean.setDealerCodes(dealerCodes);
			conditionBean.setDealerName(dealerName);
			conditionBean.setLastDay(lastDay);
			conditionBean.setDeductStatus(deductStatus);
			conditionBean.setIsDel(Constant.IS_DEL_00);//未删除的抵扣单
			conditionBean.setCompanyId(companyId);
			conditionBean.setYieldly(yieldly);
			conditionBean.setYieldlys(yieldlys);
			
			ClaimOldPartDeductBalanceDao deductBalanceDao = ClaimOldPartDeductBalanceDao.getInstance();
			//统计要结算经销商对应的抵扣单信息
			PageResult<Map<String,Object>> result = deductBalanceDao.queryDeductBalance(conditionBean,
					curPage, Constant.PAGE_SIZE);
			
			act.setOutData("ps", result);
		} catch (Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"旧件抵扣单结算");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 *  结算满足用户选择条件的抵扣单
	 *  <pre>
	 *  步骤：
	 *  	1、统计需要结算的经销商信息
	 *      2、逐个经销商进行结算
	 *      21、生成结算支付信息
	 *      22、根据结算支付信息累计，生成对应经销商抵扣单信息
	 * [修改] 2010-07-28 加入产地，按产地进行结算，将各个经销商对应产地区分开结算
	 *        注：现在产地信息从索赔单中取得，依赖一抵扣单对应一索赔单，如有变动，需调整
	 *  </pre>
	 */
	public void blance(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		Long companyId=GetOemcompanyId.getOemCompanyId(logonUser);
		boolean isDeal = DBLockUtil.lock("10000", DBLockUtil.BUSINESS_TYPE_07);
		String SUCCESS = "SUCCESS";
		if(isDeal){//防止多用户同时点击结算，造成结算重复
			try{
				RequestWrapper request = act.getRequest();
				String dealerCodes = request.getParamValue("DEALER_CODE");//经销商代码集合(以","分隔)
				String dealerName = request.getParamValue("DEALER_NAME");//经销商名称
				String lastDay = request.getParamValue("LAST_DAY");//截止日期
				String deductStatus = request.getParamValue("DEDUCT_STATUS");//抵扣单状态
				String yieldly = request.getParamValue("YIELDLY");//产地
				
				DeductBalanceBean conditionBean = new DeductBalanceBean();
				conditionBean.setDealerCodes(dealerCodes);
				conditionBean.setDealerName(dealerName);
				conditionBean.setLastDay(lastDay);
				conditionBean.setDeductStatus(deductStatus);
				conditionBean.setIsDel(Constant.IS_DEL_00);//未删除的抵扣单
				conditionBean.setCompanyId(companyId);
				conditionBean.setYieldly(yieldly);
				
				ClaimOldPartDeductBalanceDao deductBalanceDao = ClaimOldPartDeductBalanceDao.getInstance();
			    //统计需要结算的经销商信息
				List<TmDealerPO> dealerList = deductBalanceDao.loadTmDealer(conditionBean);
				if(dealerList!=null && dealerList.size()>0){
					for (TmDealerPO tmDealerPO : dealerList) {
						List<Map<String,Object>> deductList = null;
						deductList = deductBalanceDao.loadDeductByDealerId(conditionBean,tmDealerPO.getDealerId());
						this.createBlanceOrder(deductList,logonUser,tmDealerPO.getDealerId());
					}
				}
	
				act.setOutData("ACTION_RESULT", "1");
			} catch (Exception e){
				BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"旧件抵扣单结算");
				logger.error(logonUser,e1);
				act.setException(e1);
			}	
		}else{//其他人正在处理
			SUCCESS = "DEALED";
		}
		act.setOutData("SUCCESS", SUCCESS);
		DBLockUtil.freeLock("10000", DBLockUtil.BUSINESS_TYPE_07);
	}
	
	/*
	 * new Blance
	 */
	
	public void newBlance(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		Long companyId=GetOemcompanyId.getOemCompanyId(logonUser);
		boolean isDeal = DBLockUtil.lock("10000", DBLockUtil.BUSINESS_TYPE_07);
		String SUCCESS = "SUCCESS";
		if(isDeal){//防止多用户同时点击结算，造成结算重复
			try{
				RequestWrapper request = act.getRequest();
				String dealerCodes = request.getParamValue("DEALER_CODE");//经销商代码集合(以","分隔)
				String dealerName = request.getParamValue("DEALER_NAME");//经销商名称
				String lastDay = request.getParamValue("LAST_DAY");//截止日期
				String deductStatus = request.getParamValue("DEDUCT_STATUS");//抵扣单状态
				String yieldly = request.getParamValue("YIELDLY");//产地
				String yieldlys = CommonUtils.findYieldlyByPoseId(logonUser.getPoseId());       //该用户拥有的产地权限
				
				if(Utility.testString(lastDay))
					lastDay = lastDay + " 23:59:59";
				
				DeductBalanceBean conditionBean = new DeductBalanceBean();
				conditionBean.setDealerCodes(dealerCodes);
				conditionBean.setDealerName(dealerName);
				conditionBean.setLastDay(lastDay);
				conditionBean.setDeductStatus(deductStatus);
				conditionBean.setIsDel(Constant.IS_DEL_00);//未删除的抵扣单
				conditionBean.setCompanyId(companyId);
				conditionBean.setYieldly(yieldly);
				conditionBean.setYieldlys(yieldlys);
				
				ClaimOldPartDeductBalanceDao deductBalanceDao = ClaimOldPartDeductBalanceDao.getInstance();
			    //统计需要结算的经销商信息
				List<Map<String,Object>> deductList = deductBalanceDao.newLoadDeductByDealerId(conditionBean);
				this.newCreateBlanceOrder(deductList,logonUser,conditionBean);//开始结算， 插入数据
	
				act.setOutData("ACTION_RESULT", "1");
			} catch (Exception e){
				SUCCESS = "ERROR";
				BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"旧件抵扣单结算");
				logger.error(logonUser,e1);
				act.setException(e1);
			}	
		}else{//其他人正在处理
			SUCCESS = "DEALED";
		}
		DBLockUtil.freeLock("10000", DBLockUtil.BUSINESS_TYPE_07);
		act.setOutData("SUCCESS", SUCCESS);
	}
	
	/*
	 * new CreateBlanceOrder
	 * 结算方法
	 */
	@SuppressWarnings("unchecked")
	public void newCreateBlanceOrder(List<Map<String,Object>> list,AclUserBean logonUser,DeductBalanceBean bean){
		if(list!=null && list.size()>0){
			ClaimOldPartDeductBalanceDao dao = new ClaimOldPartDeductBalanceDao();
			for(int i = 0 ; i < list.size(); i++){
				Date date;
				try {
					TtAsWrDeductBalancePO po = new TtAsWrDeductBalancePO();
					long deductBalanceId=Long.parseLong(SequenceManager.getSequence(""));
					po.setId(deductBalanceId);
					po.setBalanceNo(SequenceManager.getSequence("HO"));
					po.setYieldly(Long.parseLong(String.valueOf(list.get(i).get("YIELDLY"))));
					po.setDealerCode(String.valueOf(list.get(i).get("DEALER_CODE")));
					po.setDealerName(String.valueOf(list.get(i).get("DEALER_NAME")));
					po.setStatus(Constant.STATUS_ENABLE);
					po.setDeductCount(Long.parseLong(String.valueOf(list.get(i).get("NO_COUNT"))));
					po.setLabourAmount(Double.parseDouble(String.valueOf(list.get(i).get("MANHOUR_MONEY"))));
					po.setPartAmount(Double.parseDouble(String.valueOf(list.get(i).get("MATERIAL_MONEY"))));
					po.setOtherAmount(Double.parseDouble(String.valueOf(list.get(i).get("OTHER_MONEY"))));
					po.setTotalAmount(Double.parseDouble(String.valueOf(list.get(i).get("TOTALMONEY"))));
					if(Utility.testString(bean.getLastDay())){
						date = Utility.getDate(bean.getLastDay(), 1);
						po.setEndTime(date);
					}
					Long companyId = GetOemcompanyId.getOemCompanyId(logonUser);//用户所属公司
					po.setOemCompanyId(companyId);
					po.setCreateBy(logonUser.getUserId());
					Date d = new Date();
					po.setCreateDate(d);
					dao.insert(po);//插入主表信息
					
					String dealerId = String.valueOf(list.get(i).get("DEALER_ID"));
					String yieldly = String.valueOf(po.getYieldly());
					String lastdate = bean.getLastDay();
					String status = bean.getDeductStatus();
					//根据信息查询相应抵扣单ID
					List<Map<String, Object>> tm = dao.getTMList(dealerId, yieldly, lastdate, status);
					if(tm!=null && tm.size()>0){
						for(int j = 0 ; j < tm.size(); j++){
							TtAsWrDeductBalanceDetailPO tp = new TtAsWrDeductBalanceDetailPO();
							tp.setId(Long.parseLong(SequenceManager.getSequence("")));
							tp.setBalanceId(deductBalanceId);
							tp.setDeductId(Long.parseLong(String.valueOf(tm.get(j).get("ID"))));
							tp.setCreateBy(logonUser.getUserId());
							tp.setCreateDate(d);
							dao.insert(tp);//插入子表信息
							TtAsWrDeductPO td = new TtAsWrDeductPO();
							TtAsWrDeductPO ta = new TtAsWrDeductPO();
							td.setId(tp.getDeductId());
							ta.setIsBal(1);//已结算标示 0 未结算  1已结算
							dao.update(td, ta);//修改抵扣表数据的状态，修改成已结算
							
							/***********Iverson add By 2010-12-24 添加旧件和旧件结算汇总关系********************/
							TtAsWrDeductPO popo = new TtAsWrDeductPO();
							popo.setId(Long.parseLong(String.valueOf(tm.get(j).get("ID"))));
							TtAsWrDeductPO popoValue = new TtAsWrDeductPO();
							popoValue.setDeductBalanceId(deductBalanceId);
							dao.update(popo, popoValue);
							/***********Iverson add By 2010-12-24 添加旧件和旧件结算汇总关系********************/
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * 控制生成结算工单
	 * [修改] 2010-07-28 加入产地，按产地进行结算，将各个经销商对应产地区分开结算
	 *        注：现在产地信息从索赔单中取得，依赖一抵扣单对应一索赔单，如有变动，需调整
	 * @param deductList
	 */
	@SuppressWarnings("unchecked")
	private void createBlanceOrder(List<Map<String,Object>> deductList,AclUserBean logonUser,
			Long dealerId){
		if(deductList!=null && deductList.size()>0){
			ClaimOldPartDeductBalanceDao deductBalanceDao = ClaimOldPartDeductBalanceDao.getInstance();
			ClaimAuditingDao auditiongDao = ClaimAuditingDao.getInstance();
			for (Map<String, Object> map : deductList) {
				String claimId = dealObj(map.get("CLAIM_ID"));
				String deductId = dealObj(map.get("ID"));//抵扣单ID
				String isClaim = dealObj(map.get("ISCLAIM"));//0按索赔单ID统计
				String isID = dealObj(map.get("ISID"));//0为按抵扣单统计
				String isYieldly = dealObj(map.get("ISYIELDLY"));//0为按产地统计
				String labourMoney = dealObj(map.get("MANHOUR_MONEY"));//抵扣工时费用
				String partMoney = dealObj(map.get("MATERIAL_MONEY"));//抵扣配件费用
				String otherMoney = dealObj(map.get("OTHER_MONEY"));//其他项目费用
				String totalMoney = dealObj(map.get("TOTAL_MONEY"));//总金额
				String yieldly = dealObj(map.get("YIELDLY"));//产地代码
				Long companyId = GetOemcompanyId.getOemCompanyId(logonUser);//用户所属公司
				if("1".equals(isYieldly) && "0".equals(isClaim) && "1".equals(isID)){//记录索赔支付明细
					TtAsWrApplicationPO claimPO = auditiongDao.queryClaimById(claimId);
					TtAsWrApppaymentPO payPO = new TtAsWrApppaymentPO();
					String id = SequenceManager.getSequence("");
					payPO.setId(Long.parseLong(id));
					payPO.setClaimId(Long.parseLong(claimId));
					payPO.setRoNo(claimPO.getRoNo());
					payPO.setLineNo(claimPO.getLineNo().longValue());
					payPO.setDealerId(dealerId);
					payPO.setPartsAmount(Double.parseDouble(partMoney));
					payPO.setLabourAmount(Double.parseDouble(labourMoney));
					payPO.setOtheritemAmount(Double.parseDouble(otherMoney));
					payPO.setLinetotal(Double.parseDouble(totalMoney));
					payPO.setReductionFlag(Constant.DEDUCT_STATUS);//抵扣结算
					payPO.setCreateBy(logonUser.getUserId());
					payPO.setCreateDate(new Date());
					deductBalanceDao.insert(payPO);
				}else if("0".equals(isYieldly) && "1".equals(isClaim) && "1".equals(isID)){//生成结算工单
					TtAsWrBalancePO balancePO = new TtAsWrBalancePO();
					String id = SequenceManager.getSequence("");
					balancePO.setId(Long.parseLong(id));
					balancePO.setBalanceDate(new Date());
					balancePO.setBalance(Double.parseDouble(totalMoney));
					balancePO.setReductionFlag(Constant.DEDUCT_STATUS);//抵扣结算
					balancePO.setDealerId(dealerId);
					balancePO.setBalanceNo(SequenceManager.getSequence("BO"));//生成结算单号
					balancePO.setCreateBy(logonUser.getUserId());
					balancePO.setCreateDate(new Date());
					balancePO.setOemCompanyId(companyId);
					balancePO.setProcFactory(CommonUtils.checkNull(yieldly));
					deductBalanceDao.insert(balancePO);
				}else if("0".equals(isYieldly) && "0".equals(isClaim) && "0".equals(isID)){//更新抵扣单状态
					TtAsWrDeductPO deductPO = new TtAsWrDeductPO();
					deductPO.setId(Long.parseLong(deductId));
					TtAsWrDeductPO paramPO = new TtAsWrDeductPO();
					paramPO.setIsBal(1);
					paramPO.setUpdateBy(logonUser.getUserId());
					paramPO.setUpdateDate(new Date());
					deductBalanceDao.update(deductPO, paramPO);
				}
			}
		}
	}
	
	private String dealObj(Object obj){
		String result = null;
		if(obj!=null)
			result = obj.toString();
		return result;
	}
	
}
