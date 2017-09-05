package com.infodms.dms.actions.customerRelationships.classMonitorManage;

import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import jxl.Workbook;
import jxl.write.Label;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.dao.customerRelationships.CRMSortDAO;
import com.infodms.dms.dao.customerRelationships.CallRecordDao;
import com.infodms.dms.dao.customerRelationships.SeatsSetDao;
import com.infodms.dms.dao.tccode.TcCodeDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TtCrmSeatsPO;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;
import com.infoservice.po3.bean.PageResult;
/**
 * 
 * @ClassName     : NoAcntCallRecord 
 * @Description   : 无工号通话记录查询
 * @author        : wangming
 * CreateDate     : 2013-10-2
 */
public class NoAcntCallRecord{
	private static Logger logger = Logger.getLogger(NoAcntCallRecord.class);
	// 无工号通话记录查询初始化页面
	private final String noAcntCallRecordURL = "/jsp/customerRelationships/classMonitorManage/noAcntCallRecord.jsp";
	

	ActionContext act = ActionContext.getContext();
	AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
	RequestWrapper request = act.getRequest();
	
	
	/**
	 * 无工号通话记录查询
	 */
	public void NoAcntCallRecordInit(){		
		try{
			act.setForword(noAcntCallRecordURL);
		}catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ACTION_NAME_ERROR_CODE,"无工号通话记录查询初始化");
			logger.error(logger,e1);
			act.setException(e1);
		}
	}
	
	public void queryNoAcntCallRecord(){
		act.getResponse().setContentType("application/json");
		try{
			
			String dateStartOne = CommonUtils.checkNull(request.getParamValue("dateStartOne"));  				
			String dateStartTwo = CommonUtils.checkNull(request.getParamValue("dateStartTwo"));  				
			String inComeType = CommonUtils.checkNull(request.getParamValue("inComeType"));  				
			String callType = CommonUtils.checkNull(request.getParamValue("callType"));  				
			String point = CommonUtils.checkNull(request.getParamValue("point"));  				

			
			CallRecordDao dao = CallRecordDao.getInstance();
			//分页方法 begin
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage"))	: 1; // 处理当前页	
				
			PageResult<Map<String,Object>> callRecordData = dao.queryNoAcntCallRecord(dateStartOne,dateStartTwo,
					inComeType,callType,point,Constant.PAGE_SIZE,curPage);
			
			act.setOutData("ps", callRecordData);
		} catch(Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"通话记录查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	
	
	public void noAcntCallRecordDownExcel(){
		//act.getResponse().setContentType("application/json");
		try{
			String dateStartOne = CommonUtils.checkNull(request.getParamValue("dateStartOne"));  				
			String dateStartTwo = CommonUtils.checkNull(request.getParamValue("dateStartTwo"));  				
			String inComeType = CommonUtils.checkNull(request.getParamValue("inComeType"));  				
			String callType = CommonUtils.checkNull(request.getParamValue("callType"));  				
			String point = CommonUtils.checkNull(request.getParamValue("point"));  	

			
			CallRecordDao dao = CallRecordDao.getInstance();
						
			List<Map<String, Object>> callRecordData = dao.queryNoAcntCallRecord(dateStartOne,dateStartTwo,
					inComeType,callType,point);
			if(callRecordData!=null && callRecordData.size()>0){

				callRecordDataToExcel(callRecordData);
				act.setOutData("success", "true");
				act.setForword(noAcntCallRecordURL);
			}else {
				act.setOutData("noresult", "true");
				act.setForword(noAcntCallRecordURL);
			}	
		}catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.SPECIAL_MEG,"转Excel失败!");
			logger.error(logonUser,e1);
			act.setException(e1);
		}

	}
	
	private void callRecordDataToExcel(List<Map<String, Object>> list) throws Exception{
		TcCodeDao tcCodeDao = TcCodeDao.getInstance();
		String[] head=new String[8];
		head[0]="主叫号码";
		head[1]="开始时间";
		head[2]="结束时间";
		head[3]="通话时长";
		head[4]="分机号";
		head[5]="呼叫类型";
		head[6]="来电类型";
		head[7]="服务质量";
		
		// 艾春 2013.11.30 添加 呼叫类型/来电类型/评分
		List<Map<String, Object>> ctts = tcCodeDao.getTcCodesByType(Constant.CALL_IN_OUT_TYPE);
		List<Map<String, Object>> ctis = tcCodeDao.getTcCodesByType(Constant.CALL_TYPE);
		List<Map<String, Object>> ctps = tcCodeDao.getTcCodesByType(Constant.GRADE_TYPE);
		
	    List list1=new ArrayList();
	    if(list!=null&&list.size()!=0){
			for(int i=0;i<list.size();i++){
		    	Map map =(Map)list.get(i);
		    	if(map!=null&&map.size()!=0){
					String[]detail=new String[8];
					detail[0] = CommonUtils.checkNull(map.get("CR_NUMBER"));
					detail[1] = CommonUtils.checkNull(map.get("CR_STA_DATE"));
					detail[2] = CommonUtils.checkNull(map.get("CR_END_DATE"));
					detail[3] = CommonUtils.checkNull(map.get("CR_TALK_TIME"));
					detail[4] = CommonUtils.checkNull(map.get("CR_EXT"));
					detail[5] = tcCodeDao.getCodeDescByCodeId(CommonUtils.checkNull(map.get("CR_CALL_TYPE")), ctts);
					detail[6] = tcCodeDao.getCodeDescByCodeId(CommonUtils.checkNull(map.get("CR_INCOME_TYPE")), ctis);
					detail[7] = tcCodeDao.getCodeDescByCodeId(CommonUtils.checkNull(map.get("CR_POINTS")), ctps);
					list1.add(detail);
		    	}
		    }
			this.exportEx(ActionContext.getContext().getResponse(), request, head, list1);
	    }
	}	

	public static Object exportEx(ResponseWrapper response,
			RequestWrapper request, String[] head, List<String[]> list)
			throws Exception {

		String name = "客户无工号通话记录查询清单.xls";
		jxl.write.WritableWorkbook wwb = null;
		OutputStream out = null;
		try {
			response.setContentType("application/octet-stream");
		    response.addHeader("Content-disposition", "attachment;filename="+URLEncoder.encode(name, "utf-8"));
			out = response.getOutputStream();
			wwb = Workbook.createWorkbook(out);
			jxl.write.WritableSheet ws = wwb.createSheet("sheettest", 0);

			if (head != null && head.length > 0) {
				for (int i = 0; i < head.length; i++) {
					ws.addCell(new Label(i, 0, head[i]));
				}
			}
			int pageSize=list.size()/30000;
			for (int z = 1; z < list.size() + 1; z++) {
				String[] str = list.get(z - 1);
				for (int i = 0; i < str.length; i++) {
					ws.addCell(new Label(i, z, str[i]));
				}
			}
			wwb.write();
			out.flush();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			if (null != wwb) {
				wwb.close();
			}
			if (null != out) {
				out.close();
			}
		}
		return null;
	}

}