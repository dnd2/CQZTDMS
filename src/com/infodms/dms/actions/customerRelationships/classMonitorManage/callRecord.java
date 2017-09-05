package com.infodms.dms.actions.customerRelationships.classMonitorManage;

import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import jxl.Workbook;
import jxl.write.Label;

import net.sf.json.JSONArray;

import org.apache.log4j.Logger;
import org.tempuri.UltraCRMWebservice;
import org.tempuri.UltraCRMWebserviceSoap;

import com.infodms.dms.actions.sales.planmanage.PlanUtil.BaseImport;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.dao.customerRelationships.CallRecordDao;
import com.infodms.dms.dao.tccode.TcCodeDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TtCrmCallRecordPO;
import com.infodms.dms.po.TtCrmSeatsPO;
import com.infodms.dms.util.CalendarUtil;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;
import com.infoservice.po3.bean.PageResult;
/**
 * 
 * @ClassName     : callRecord 
 * @Description   : 通话记录查询
 * @author        : wangming
 * CreateDate     : 2013-4-7
 */
public class callRecord extends BaseImport{
	private static Logger logger = Logger.getLogger(callRecord.class);
	// 通话记录初始化页面
	private final String callRecordUrl = "/jsp/customerRelationships/classMonitorManage/callRecord.jsp";
	private final String callRecordReplayUrl = "/jsp/customerRelationships/classMonitorManage/callRecordReplayer.jsp";
	

	ActionContext act = ActionContext.getContext();
	AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
	RequestWrapper request = act.getRequest();
	
	
	/**
	 * 通话记录初始化
	 */
	public String[] to3days()
	{
		SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd");
		Timestamp ts = new Timestamp(System.currentTimeMillis());
		ts.setDate(ts.getDate()+3);
		
		String afterDate = sf.format(ts.getTime());
		ts.setDate(ts.getDate()-6);
		String beforDate = sf.format(ts.getTime());
		String[] str = new String[2];
		str[0] = afterDate;
		str[1] = beforDate;
		return str;
		
	}
	public void callRecordInit(){		
		try{
			act.setForword(callRecordUrl);
		}catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ACTION_NAME_ERROR_CODE,"通话记录初始化");
			logger.error(logger,e1);
			act.setException(e1);
		}
	}
	
	public Integer getCallType(String MSG_TYPE)
	{
		if(MSG_TYPE.equals("1") ||MSG_TYPE.equals("3") ||MSG_TYPE.equals("11") )
		{
			return 95101001;
		}else
		{
			return 95101002;
		}
	}
	public Integer getIncomeType(String MSG_TYPE)
	{
		if(MSG_TYPE.equals("11")   )
		{
			return 95091001;
		}else
		{
			return 95091002;
		}
	}
	
	public void queryCallRecord(){
		act.getResponse().setContentType("application/json");
		try{
			
			String account = CommonUtils.checkNull(request.getParamValue("account"));  				
			String ext = CommonUtils.checkNull(request.getParamValue("ext"));  				
			String dateStartOne = CommonUtils.checkNull(request.getParamValue("dateStartOne"));  				
			String dateStartTwo = CommonUtils.checkNull(request.getParamValue("dateStartTwo"));  				
			String number = CommonUtils.checkNull(request.getParamValue("number"));  				
			String inComeType = CommonUtils.checkNull(request.getParamValue("inComeType"));  				
			String callType = CommonUtils.checkNull(request.getParamValue("callType"));  				
			String point = CommonUtils.checkNull(request.getParamValue("point"));  				
			// 艾春2013.11.30 添加坐席未接电话类型查询条件
			String missedType = CommonUtils.checkNull(request.getParamValue("missedType"));
			String if_type = CommonUtils.checkNull(request.getParamValue("if_type"));
			
			CallRecordDao dao = CallRecordDao.getInstance();
			//分页方法 begin
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage"))	: 1; // 处理当前页	
				
			PageResult<Map<String,Object>> callRecordData = dao.queryCallRecord(account,ext,dateStartOne,dateStartTwo,
					number,inComeType,callType,point,missedType,if_type,Constant.PAGE_SIZE,curPage);
			List<Map<String, Object>> list = callRecordData.getRecords();
			setTalkTime(list);
			act.setOutData("ps", callRecordData);
		} catch(Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"通话记录查询");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	private void setTalkTime(List<Map<String, Object>> list){
		
		SimpleDateFormat formate1 = new SimpleDateFormat("HH:mm:ss");
		
		if(list!=null && list.size()>0){
			for(Map<String, Object> map : list){
				
				String dateStr = "";
				if(map.get("TALKTIME")!=null){
					dateStr = formate1.format(map.get("TALKTIME"));
				}
				map.put("TALKTIME", dateStr);
			}
		}
	}
	
	public void callRecordReplay(){
		String url = request.getParamValue("url");
		try{
			act.setOutData("url", url);
			act.setForword(callRecordReplayUrl);
		}catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ACTION_NAME_ERROR_CODE,"通话记录初始化");
			logger.error(logger,e1);
			act.setException(e1);
		}
	}
	
	public void callRecordDownExcel(){
		//act.getResponse().setContentType("application/json");
		try{
			String account = CommonUtils.checkNull(request.getParamValue("account"));  				
			String ext = CommonUtils.checkNull(request.getParamValue("ext"));  				
			String dateStartOne = CommonUtils.checkNull(request.getParamValue("dateStartOne"));  				
			String dateStartTwo = CommonUtils.checkNull(request.getParamValue("dateStartTwo"));  				
			String number = CommonUtils.checkNull(request.getParamValue("number"));  				
			String inComeType = CommonUtils.checkNull(request.getParamValue("inComeType"));  				
			String callType = CommonUtils.checkNull(request.getParamValue("callType"));  				
			String point = CommonUtils.checkNull(request.getParamValue("point"));  		
			
			// 艾春2013.11.30 添加坐席未接电话类型查询条件
			String missedType = CommonUtils.checkNull(request.getParamValue("missedType"));

			
			CallRecordDao dao = CallRecordDao.getInstance();
			
				
			List<Map<String, Object>> callRecordData = dao.queryCallRecord(account,ext,dateStartOne,dateStartTwo,
					number,inComeType,callType,point,missedType);
			if(callRecordData!=null && callRecordData.size()>0){
				setTalkTime(callRecordData);
				callRecordDataToExcel(callRecordData);
				act.setOutData("success", "true");
				act.setForword(callRecordUrl);
			}else {
				act.setOutData("noresult", "true");
				act.setForword(callRecordUrl);
			}	
		}catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.SPECIAL_MEG,"转Excel失败!");
			logger.error(logonUser,e1);
			act.setException(e1);
		}

	}
	
	private void callRecordDataToExcel(List<Map<String, Object>> list) throws Exception{
		TcCodeDao tcCodeDao = TcCodeDao.getInstance();
		String[] head=new String[12];
		head[0]="主叫号码";
		head[1]="开始时间";
		head[2]="结束时间";
		head[3]="通话时长";
		head[4]="座席";
		head[5]="分机号";
		head[6]="转移目标号码";
		head[7]="呼叫类型";
		head[8]="来电类型";
		head[9]="评分";
		head[10]="振铃时长";
		head[11]="等待时长";
		// 艾春 2013.11.30 添加 呼叫类型/来电类型/评分
		List<Map<String, Object>> ctts = tcCodeDao.getTcCodesByType(Constant.CALL_IN_OUT_TYPE);
		List<Map<String, Object>> ctis = tcCodeDao.getTcCodesByType(Constant.CALL_TYPE);
		List<Map<String, Object>> ctps = tcCodeDao.getTcCodesByType(Constant.GRADE_TYPE);
		
	    List list1=new ArrayList();
	    if(list!=null&&list.size()!=0){
			for(int i=0;i<list.size();i++){
		    	Map map =(Map)list.get(i);
		    	if(map!=null&&map.size()!=0){
					String[]detail=new String[12];
					detail[0] = CommonUtils.checkNull(map.get("CRNUMBER"));
					detail[1] = CommonUtils.checkNull(map.get("STADATE"));
					detail[2] = CommonUtils.checkNull(map.get("ENDDATE"));
					detail[3] = CommonUtils.checkNull(map.get("TALKTIME"));
					detail[4] = CommonUtils.checkNull(map.get("SEATSNO"));
					detail[5] = CommonUtils.checkNull(map.get("EXT"));
					detail[6] = CommonUtils.checkNull(map.get("MOVENUMBER"));
					detail[7] = tcCodeDao.getCodeDescByCodeId(CommonUtils.checkNull(map.get("CALLTYPE")),ctts);
					detail[8] = tcCodeDao.getCodeDescByCodeId(CommonUtils.checkNull(map.get("INCOMETYPE")),ctis);
					detail[9] = tcCodeDao.getCodeDescByCodeId(CommonUtils.checkNull(map.get("POINTS")),ctps);
					detail[10] = CommonUtils.checkNull(map.get("RINGTIME"));
					detail[11] = CommonUtils.checkNull(map.get("WAITTIME"));
					list1.add(detail);
		    	}
		    }
			this.exportEx(ActionContext.getContext().getResponse(), request, head, list1);
	    }
	}	

	public static Object exportEx(ResponseWrapper response,
			RequestWrapper request, String[] head, List<String[]> list)
			throws Exception {

		String name = "客户通话记录清单.xls";
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