package com.infodms.dms.actions.partsmanage.infoSearch;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.bean.PartinfoBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.constants.PTConstants;
import com.infodms.dms.dao.partinfo.PartinfoDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TmPtOrderParamPO;
import com.infodms.dms.po.TmPtOrdparamDatePO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;

/**
 * @Title: PartsmanageOrderParamInfo.java
 *
 * @Description:CHANADMS
 *
 * @Copyright: Copyright (c) 2010
 *
 * @Company: www.infoservice.com.cn
 * @Date: 2010-6-6
 *
 * @author lishuai 
 * @mail   lishuai103@yahoo.cn	
 * @version 1.0
 * @remark 
 */
public class PartsmanageOrderParamInfo implements PTConstants {
	
	public Logger logger = Logger.getLogger(PartsmanageOrderParamInfo.class);
	
	/**
	 * 配件订单业务参数初始化
	 */
	public void partsmanageOrderParamInfoInit(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			act.setForword(opinitUrl);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"配件订单业务参数");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 配件订单业务参数添加初始化
	 */
	public void addRule(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			act.setForword(addUrl);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ADD_FAILURE_CODE,"配件订单业务参数");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 配件订单业务参数添加
	 */
	public void insertRule(){
		ActionContext act = ActionContext.getContext();
		RequestWrapper request = act.getRequest();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			String dealerId = request.getParamValue("dealerId");   //经销商ID
			String orderMaxLines = request.getParamValue("orderMaxLines");  //上报最大行数
			String allowSubmitTimes = request.getParamValue("allowSubmitTimes");//周期内最大上报次数
			String discountRate = request.getParamValue("discountRate");  //折扣
			String cycleType = request.getParamValue("cycleType");  //周期类型
			String[] startDate = request.getParamValues("START_DATE");//上报开始时间
			String[] endDate = request.getParamValues("END_DATE"); //上报结束时间
			String[] handleDate = request.getParamValues("HANDLE_DATE"); //上报时间
			String[] id = dealerId.split(",");
			Date createDate = new Date(System.currentTimeMillis());
			PartinfoDao dao = new PartinfoDao();
			if(id != null && !id.equals("")){
				for(int i=0;i<id.length;i++){
					TmPtOrderParamPO po = new TmPtOrderParamPO();
					po.setDealerId(Long.parseLong(id[i]));
					dao.delete(po);//添加前先删除
					Long paramId = new Long(SequenceManager.getSequence(""));
					po.setParamId(paramId);
					po.setDealerId(new Long(id[i]));
					po.setOrderMaxLines(new Integer(orderMaxLines));
					po.setAllowSubmitTimes(new Integer(allowSubmitTimes));
					po.setDiscountRate(new Float(discountRate));
					po.setCycleType(new Integer(cycleType));
					po.setCreateBy(logonUser.getUserId());
					po.setCreateDate(createDate);
					dao.insertOrderParam(po);
					if(startDate != null && !startDate.equals("")){
						for(int j=0;j<startDate.length;j++){//添加订单上报处理日期
							TmPtOrdparamDatePO tt = new TmPtOrdparamDatePO();
							tt.setProcessId(new Long(SequenceManager.getSequence("")));
							tt.setParamId(paramId);
							tt.setStartDate(new Integer(startDate[j]));
							tt.setEndDate(new Integer(endDate[j]));
							tt.setHandleDate(new Integer(handleDate[j]));
							tt.setCreateBy(logonUser.getUserId());
							tt.setCreateDate(createDate);
							dao.insertOrderParamDate(tt);
						}
					}   
				}
			}
			
			act.setForword(opinitUrl);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ADD_FAILURE_CODE,"配件订单业务参数");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 配件订单业务参数查询
	 */
	public void queryOrderParam(){
		ActionContext act = ActionContext.getContext();
		RequestWrapper request = act.getRequest();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			String dealerCode = CommonUtils.checkNull(request.getParamValue("dealerCode"));//经销商代码
			String dealerName = CommonUtils.checkNull(request.getParamValue("dealerName"));//经销商名称
			if(!dealerCode.equals("")){//截串加单引号
				String[] supp = dealerCode.split(",");
				dealerCode = "";
				for(int i=0;i<supp.length;i++){
					supp[i] = "'"+supp[i]+"'";
					if(!dealerCode.equals("")){
						dealerCode += "," + supp[i];
					}else{
						dealerCode = supp[i];
					}
				}
			}

			PartinfoBean bean = new PartinfoBean();
			bean.setDealerCode(dealerCode);
			bean.setDealerName(dealerName);
			PartinfoDao dao = new PartinfoDao();
			//分页方法 begin
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1; // 处理当前页	
			PageResult<PartinfoBean> ps = dao.queryOrderParamInfo(bean,curPage,Constant.PAGE_SIZE);
			//分页方法 end
			act.setOutData("ps", ps);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"配件订单业务参数查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 规则修改初始化
	 */
	public void updateRule(){
		ActionContext act = ActionContext.getContext();
		RequestWrapper request = act.getRequest();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			String[] dealerId = request.getParamValues("dealerIds");   //经销商ID
			String id = "";
			if(dealerId != null && !dealerId.equals("")){
				for(int i=0;i<dealerId.length;i++){
					if(!id.equals("")){
						id += "," + dealerId[i];
					}else{
						id = dealerId[i];
					}
				}
			}
			PartinfoDao dao = new PartinfoDao();
			List<Map<String, Object>> list = dao.getHaveParamDealer(id);
			act.setOutData("list", list);
			act.setOutData("id", id);
			act.setForword(updateUrl);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ADD_FAILURE_CODE,"配件订单业务参数修改");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	
	/**
	 * 规则修改
	 */
	public void udateOrderParamDo(){
		ActionContext act = ActionContext.getContext();
		RequestWrapper request = act.getRequest();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			String dealerId = request.getParamValue("dealerId");   //经销商ID
			String orderMaxLines = request.getParamValue("orderMaxLines");  //上报最大行数
			String allowSubmitTimes = request.getParamValue("allowSubmitTimes");//周期内最大上报次数
			String discountRate = request.getParamValue("discountRate");  //折扣
			String cycleType = request.getParamValue("cycleType");  //周期类型
			String[] startDate = request.getParamValues("START_DATE");//上报开始时间
			String[] endDate = request.getParamValues("END_DATE"); //上报结束时间
			String[] handleDate = request.getParamValues("HANDLE_DATE"); //上报时间
			String[] id = dealerId.split(",");
			Date createDate = new Date(System.currentTimeMillis());
			PartinfoDao dao = new PartinfoDao();
			dao.delParamDealerId(dealerId);
			if(id != null && !id.equals("")){
				for(int i=0;i<id.length;i++){
					TmPtOrderParamPO po = new TmPtOrderParamPO();
					Long paramId = new Long(SequenceManager.getSequence(""));
					po.setParamId(paramId);
					po.setDealerId(new Long(id[i]));
					po.setOrderMaxLines(new Integer(orderMaxLines));
					po.setAllowSubmitTimes(new Integer(allowSubmitTimes));
					po.setDiscountRate(new Float(discountRate));
					po.setCycleType(new Integer(cycleType));
					po.setCreateBy(logonUser.getUserId());
					po.setCreateDate(createDate);
					dao.insertOrderParam(po);
					if(startDate != null && !startDate.equals("")){
						for(int j=0;j<startDate.length;j++){
							TmPtOrdparamDatePO tt = new TmPtOrdparamDatePO();
							tt.setProcessId(new Long(SequenceManager.getSequence("")));
							tt.setParamId(paramId);
							tt.setStartDate(new Integer(startDate[j]));
							tt.setEndDate(new Integer(endDate[j]));
							tt.setHandleDate(new Integer(handleDate[j]));
							tt.setCreateBy(logonUser.getUserId());
							tt.setCreateDate(createDate);
							dao.insertOrderParamDate(tt);
						}
					}   
				}
			}
			
			act.setForword(opinitUrl);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.UPDATE_FAILURE_CODE,"配件订单业务参数");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
}
