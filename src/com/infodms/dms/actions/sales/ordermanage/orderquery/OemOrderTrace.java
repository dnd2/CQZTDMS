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
import com.infodms.dms.common.materialManager.MaterialGroupManagerDao;
import com.infodms.dms.dao.sales.ordermanage.orderquery.DealerOrderTraceDao;
import com.infodms.dms.dao.sales.ordermanage.orderquery.OemOrderTraceDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TmDealerPO;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;
import com.infoservice.po3.bean.PageResult;

/**
 * 该类用于车厂用户跟踪已经提报的订单执行情况
 * 为长安OTD系统功能的一部分，在DMS系统中提供远程调用
 * @author Devin Qin
 *
 */
public class OemOrderTrace {
	 
	private Logger logger = Logger.getLogger(OemOrderTrace.class);
	private ActionContext act = ActionContext.getContext();
	RequestWrapper request = act.getRequest();
	ResponseWrapper response = act.getResponse();
	private final  OemOrderTraceDao dao=OemOrderTraceDao.getInstance();
	//经销商选择页面
	private final String initUrl="/jsp/sales/ordermanage/orderquery/oemSelectDealerOrderTrace.jsp"; 
	 
	//获取当前登录用户
	AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
	//操作前调用方法
	public void oemOrderDealerTracePre() {
					act.setForword(initUrl);
	}
	/**
	 * 经销商信息查询
	 */
	public void getDealerInfo(){
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
			String strdealerCode = request.getParamValue("dealerCode");		//经销商CODE
			int currPage=request.getParamValues("curPage")!=null?Integer
					 .parseInt(request.getParamValue("curPage")):1; //处理当前页
			PageResult<Map<String, Object>> ps = dao.getDealerInfoByCode(strdealerCode,10, currPage);
			act.setOutData("ps", ps);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"经销商查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
}
