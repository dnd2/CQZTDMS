package com.infodms.dms.actions.customerRelationships.classMonitorManage;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.dao.customerRelationships.CallRecordDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.util.CalendarUtil;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;
/**
 * 
 * @ClassName     : ClassMonitorManage 
 * @Description   : 坐席业务看板
 * @author        : wangming
 * CreateDate     : 2013-4-8
 */
public class SeatsInformation{
	private static Logger logger = Logger.getLogger(SeatsInformation.class);
	// 坐席业务看板初始化页面
	private final String seatsInformationUrl = "/jsp/customerRelationships/classMonitorManage/seatsInformation.jsp";
	

	ActionContext act = ActionContext.getContext();
	AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
	RequestWrapper request = act.getRequest();
	
	
	/**
	 * 坐席业务看板初始化
	 */
	public void seatsInformationInit(){		
		try{
			String times = CommonUtils.checkNull(request.getParamValue("times"));  
			if(times.equals("")) times = String.valueOf(10);
			act.setOutData("times", times);
			act.setForword(seatsInformationUrl);
		}catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ACTION_NAME_ERROR_CODE,"坐席业务看板初始化");
			logger.error(logger,e1);
			act.setException(e1);
		}
	}
	/**
	 * 查询刷新内容
	 */
	public void querySeatsInformation(){
		String times = CommonUtils.checkNull(request.getParamValue("times"));  
		if(times.equals("")) times = String.valueOf(10);
		CallRecordDao dao = CallRecordDao.getInstance();	
		PageResult<Map<String,Object>> callRecordData = dao.querySeatsInformation(Constant.PAGE_SIZE_MAX,1);
//		List<Map<String, Object>> list = callRecordData.getRecords();
//		SimpleDateFormat formate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		SimpleDateFormat formate1 = new SimpleDateFormat("HH:mm:ss");
//		
//		if(list!=null && list.size()>0){
//			for(Map<String, Object> map : list){
//				Date laseDate = null;
//				long seid = ((BigDecimal)map.get("SEID")).longValue();
//				List<Map<String, Object>> callList = dao.getSeatsInformationList(seid);
//				for(Map<String, Object> call : callList){
//					String talkTimeStr = (String)call.get("TALKTIME");
//					try {
//						Date date = formate.parse(talkTimeStr);
//						if(laseDate == null){
//							laseDate = date;
//						}else{
//							laseDate = CalendarUtil.getDateAndDate(date, laseDate);
//						}
//					} catch (ParseException e) {
//						e.printStackTrace();
//					}
//				}
//				String dateStr = "";
//				if(laseDate!=null){
//					dateStr = formate1.format(laseDate);
//				}
//				map.put("TALKDATE", dateStr);
//			}
//		}
		act.setOutData("ps", callRecordData);
		act.setOutData("times", times);
	}
 
	
}