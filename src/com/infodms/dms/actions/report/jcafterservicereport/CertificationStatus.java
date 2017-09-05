package com.infodms.dms.actions.report.jcafterservicereport;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.bean.StandardVipApplyManagerBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.dao.report.jcafterservicereport.InterfaceDataQueryDao;
import com.infodms.dms.dao.report.jcafterservicereport.SpecialCostReportDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TmDealerPO;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;

import flex.messaging.io.ArrayList;
/**
 *合格证寄送情况
 */
public class CertificationStatus {

	public Logger logger = Logger.getLogger(SpecialCostReport.class);
	InterfaceDataQueryDao dao = new InterfaceDataQueryDao();
	private final String initUrl = "/jsp/report/jcafterservicereport/certificationstatus.jsp";
	
	public void CertificationStatusInit(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try {
            List<Map<String, Object>> seriesList = dao.getSeriesList();//车系
           
			String yieldly = CommonUtils.findYieldlyByPoseId(logonUser.getPoseId()); //取得该用户拥有的产地权限

			act.setOutData("yieldly", yieldly);
			act.setOutData("seriesList", seriesList);
			act.setForword(initUrl);
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"合格证寄送情况");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	public void queryCertificationStatus(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		try {
			//CommonUtils.checkNull() 校验是否为空				
			String beginTime = request.getParamValue("beginTime"); //生产日期
			String endTime = request.getParamValue("endTime");
			
			String dmsOrderCode = request.getParamValue("dmsOrderCode");//票据号
			String caErpOrgCode = request.getParamValue("caErpOrgCode");//票据类型
			
			String dealerCode = logonUser.getDealerCode();
			String dealerERPcode = "";
			TmDealerPO td  =  new TmDealerPO();
			td.setDealerCode(dealerCode);
			List tds = dao.select(td);
			if(tds.size() > 0 ){
				TmDealerPO td2 = (TmDealerPO)tds.get(0);
				dealerERPcode = td2.getErpCode();
			}
				 
			//生产日期开始时间和结束时间相同时
			if(null!=beginTime&&!"".equals(beginTime)&&null!=endTime&&!"".equals(endTime)){
					beginTime = beginTime+" 00:00:00";
					endTime = endTime+" 23:59:59";
			}		
			
			//分页方法 begin
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1; // 处理当前页	
			PageResult<Map<String, Object>> ps = dao.queryCertificationStatus(beginTime, endTime, dmsOrderCode, caErpOrgCode,dealerERPcode ,curPage,Constant.PAGE_SIZE);
			//分页方法 end
			act.setOutData("ps", ps);     //向前台传的list 名称是固定的不可改必须用 ps
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"合格证寄送情况");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	public void queryCertificationDetail(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		try {						
			String DMS_ORDER_CODE = request.getParamValue("ORDER");//票据号
			String CA_ERP_ORG_CODE = request.getParamValue("ORG");//票据类型
		
			//分页方法 begin
			Integer curPage = request.getParamValue("curPage") != null ? Integer
					.parseInt(request.getParamValue("curPage"))
					: 1; // 处理当前页	
			PageResult<Map<String, Object>> ps = dao.queryCertificationDetail(DMS_ORDER_CODE, CA_ERP_ORG_CODE, curPage, 500);
			//分页方法 end
			act.setOutData("ps", ps);     //向前台传的list 名称是固定的不可改必须用 ps
		}catch(Exception e) {//异常方法
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"合格证寄送情况");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	public void CertificationStatusExcel(){
		
	}
}
