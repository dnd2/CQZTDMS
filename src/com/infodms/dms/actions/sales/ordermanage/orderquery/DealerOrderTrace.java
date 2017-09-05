/**
 * 
 */
package com.infodms.dms.actions.sales.ordermanage.orderquery;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.dao.sales.ordermanage.orderquery.DealerOrderTraceDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TmDealerPO;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;

/**
 * 该类用于经销商跟踪已经提报的订单执行情况
 * 为长安OTD系统功能的一部分，在DMS系统中提供远程调用
 * @author Devin Qin
 *
 */
public class DealerOrderTrace {
	 
	private Logger logger = Logger.getLogger(DealerOrderTrace.class);
	private ActionContext act = ActionContext.getContext();
	RequestWrapper request = act.getRequest();
	ResponseWrapper response = act.getResponse();
	//经销商选择页面
	private final String initUrl="/jsp/sales/ordermanage/orderquery/dealerSelectOrderTrace.jsp"; 
	//OTD订单监控调用页面
	private final String order_OTD_Url="//www.baidu.com"; 
	private final  DealerOrderTraceDao dao=DealerOrderTraceDao.getInstance();
	//获取当前登录用户
	AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
	//操作前调用方法
	public void dealerOrderTracePre() {
		try{
			//调用数据操纵层方法获取当前登录用户对应的经销商列表
 			List<TmDealerPO> dealerList= dao.getDealerInfo(logonUser);
 			//经销商列表非空时进行逻辑判断操作
			if(dealerList.size()>0 && dealerList.get(0)!=null)
			{
				//若当前登录用户只对应于一个经销商，则调用跳转到OTD订单监控的方法
//				 if(dealerList.size() == 1){
//					 TmDealerPO dealerPO = (TmDealerPO) dealerList.get(0);
//					 String dealer_id = dealerPO.getDealerId().toString();
//					 call_OTD_OrderMonitor(dealer_id);
//				 }	
				//若当前登录用户对应于多个经销商则跳转到经销商选择界面以选择经销商	
//				else {
					act.setOutData("dealerList", dealerList);
					act.setForword(initUrl);
//				}	
		}
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"经销商订单跟踪查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	//经销商选择后调用方法
	public void dealerSelectedTrace() {
		try{
				String dealer_id = act.getRequest().getParamValue("dealerId");
				call_OTD_OrderMonitor(dealer_id);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"经销商订单跟踪查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	private void call_OTD_OrderMonitor(String dealerId) {
		try {
				act.setForword(order_OTD_Url);	
		} catch (Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"调用OTD订单监控接口");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}

}
