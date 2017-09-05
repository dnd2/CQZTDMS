package com.infodms.dms.actions.report.jcafterservicereport;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.bean.TtAsWrApplicationExtBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.report.jcafterservicereport.ServicerBalanceReportDao;
import com.infodms.dms.dao.report.jcafterservicereport.SpecialCostReportDao;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;

import flex.messaging.io.ArrayList;

public class ServicerBalanceReport {
	public Logger logger = Logger.getLogger(SpecialCostReport.class);
	SpecialCostReportDao dao = SpecialCostReportDao.getInstance();
	private final String initUrl = "/jsp/report/jcafterservicereport/servicerBalance.jsp";
	
	/********
	 * 首页跳转界面
	 */
	public void mainUrl(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		act.setForword(initUrl);
	}
	/**********
	 * 查询功能
	 */
	public void viewServicerStatus(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		Integer curPage = request.getParamValue("curPage") != null ? Integer
				.parseInt(request.getParamValue("curPage"))
				: 1; // 处理当前页
		String dealerCode = request.getParamValue("DEALER_CODE");
		String dealerName = request.getParamValue("DEALER_NAME");
		String orgId  = request.getParamValue("orgId");
		String PROVICE_ID  = request.getParamValue("PROVICE_ID");
		String YILYIE  = request.getParamValue("YILYIE");
		String year = request.getParamValue("year");
		ServicerBalanceReportDao rd = new ServicerBalanceReportDao();
		PageResult<Map<String,Object>> ps = rd.viewServicer(dealerCode, dealerName, orgId, PROVICE_ID, YILYIE, year, Constant.PAGE_SIZE, curPage);
		//PageResult<TtAsWrApplicationExtBean> ps = dao.queryApplication1(
			//	logonUser, map,params, Constant.PAGE_SIZE, curPage);
		act.setOutData("ps", ps);
        act.setForword(initUrl);
	}
	/*********
	 * 下载功能
	 */
	public void viewServicerReportExcel(){
		ActionContext act = ActionContext.getContext();
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		RequestWrapper request = act.getRequest();
		Integer curPage = request.getParamValue("curPage") != null ? Integer
				.parseInt(request.getParamValue("curPage"))
				: 1; // 处理当前页
		String dealerCode = request.getParamValue("DEALER_CODE");
		String dealerName = request.getParamValue("DEALER_NAME");
		String orgId  = request.getParamValue("orgId");
		String PROVICE_ID  = request.getParamValue("PROVICE_ID");
		String YILYIE  = request.getParamValue("YILYIE");
		String year = request.getParamValue("year");
		ServicerBalanceReportDao rd = new ServicerBalanceReportDao();
		String head[] = new String[19];
		//head[0]="序号";
		head[1]="服务商代码";
		head[2]="服务商名称";
		head[3]="厂家";
		head[4]="大区";
		head[5]="省份";
		head[6]="一月";
		head[7]="二月";
		head[8]="三月";
		head[9]="四月";
		head[10]="五月";
		head[11]="六月";
		head[12]="七月";
		head[13]="八月";
		head[14]="九月";
		head[15]="十月";
		head[16]="十一月";
		head[17]="十二月上";
		head[18]="十二月下";
		
		List<Map<String,Object>> list = rd.viewServicerExportExcel(dealerCode, dealerName, orgId, PROVICE_ID, YILYIE, year, Constant.PAGE_SIZE, curPage);
		  List list1=new ArrayList();
		    if(list!=null&&list.size()!=0){
				for(int i=0;i<list.size();i++){
			    	Map map =(Map)list.get(i);
			    	if(map!=null&&map.size()!=0){
						String[]detail=new String[19];
							detail[1] = String.valueOf(map.get("DEALER_CODE"));
							detail[2] = String.valueOf(map.get("DEALER_NAME"));
							detail[3] = dao.conversionCode(String.valueOf(map.get("YIELDLY")));
							detail[4] = String.valueOf(map.get("ROOT_ORG_NAME"));
							detail[5] = String.valueOf(map.get("REGION_NAME"));
							detail[6] = dao.conversionCode(String.valueOf(map.get("STATUS_1")));
							detail[7] = dao.conversionCode(String.valueOf(map.get("STATUS_2")));
							detail[8] = dao.conversionCode(String.valueOf(map.get("STATUS_3")));
							detail[9] = dao.conversionCode(String.valueOf(map.get("STATUS_4")));
							detail[10] =dao.conversionCode( String.valueOf(map.get("STATUS_5")));
							detail[11] =dao.conversionCode( String.valueOf(map.get("STATUS_6")));
							detail[12] = dao.conversionCode(String.valueOf(map.get("STATUS_7")));
							detail[13] = dao.conversionCode(String.valueOf(map.get("STATUS_8")));
							detail[14] = dao.conversionCode(String.valueOf(map.get("STATUS_9")));
							detail[15] = dao.conversionCode(String.valueOf(map.get("STATUS_10")));
							detail[16] = dao.conversionCode(String.valueOf(map.get("STATUS_11")));
							detail[17] = dao.conversionCode(String.valueOf(map.get("STATUS_12")));
							detail[18] = dao.conversionCode(String.valueOf(map.get("STATUS_13")));
							
							list1.add(detail);
						
			    	}
				}
		    }
		    try {
				com.infodms.dms.actions.claim.basicData.ToExcel.toNewExcel(ActionContext.getContext().getResponse(), request, head, list1, "服务商三包结算总表.xls");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	      
		//PageResult<TtAsWrApplicationExtBean> ps = dao.queryApplication1}
	
	}
	
 
}
