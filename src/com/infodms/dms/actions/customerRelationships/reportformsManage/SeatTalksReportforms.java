package com.infodms.dms.actions.customerRelationships.reportformsManage;

import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import jxl.Workbook;
import jxl.write.Label;

import org.apache.log4j.Logger;

import com.infodms.dms.actions.customerRelationships.baseSetting.seatsSet;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.dao.customerRelationships.SeatTalksReportformsDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;
import com.infoservice.po3.bean.PageResult;

/**
 * 坐席话务量统计
 * @author wangming
 *
 */
public class SeatTalksReportforms {
	private static Logger logger = Logger.getLogger(seatsSet.class);
	//坐席话务量统计初始化页面
	private final String seatTalksReportformsUrl = "/jsp/customerRelationships/reportformsManage/seatTalksReportforms.jsp";

	ActionContext act = ActionContext.getContext();
	AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
	RequestWrapper request = act.getRequest();
	
	public void seatTalksReportformsInit(){
		try{
			act.setForword(seatTalksReportformsUrl);
		}catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ACTION_NAME_ERROR_CODE,"坐席话务量统计初始化");
			logger.error(logger,e1);
			act.setException(e1);
		}
	}
	
	public void querySeatTalksReportforms(){
		try{
			String dealName = CommonUtils.checkNull(request.getParamValue("dealName"));  
			String dateStart = CommonUtils.checkNull(request.getParamValue("dateStart"));  				
			String dateEnd = CommonUtils.checkNull(request.getParamValue("dateEnd"));  
			
			String paiEnd = CommonUtils.checkNull(request.getParamValue("paiEnd"));  				
			String paiStart = CommonUtils.checkNull(request.getParamValue("paiStart"));  
						
			SeatTalksReportformsDao dao = SeatTalksReportformsDao.getInstance();
			//分页方法 begin
			Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage"))	: 1; // 处理当前页				
		
			PageResult<Map<String,Object>> seatTalksReportformsData = dao.querySeatTalksReportformsDao(dealName,dateStart,dateEnd,paiStart,paiEnd,Constant.PAGE_SIZE_MAX,curPage);
//			if(seatTalksReportformsData!=null){
//				List<Map<String, Object>> list = seatTalksReportformsData.getRecords();
//				if( list != null && list.size()>0){
//					list.get(list.size()-1).put("NAME", "");
//					putDatas(list, dao, dateStart, dateEnd);
//					act.setOutData("ps", seatTalksReportformsData);
//				}
//				
//			}	
			act.setOutData("ps", seatTalksReportformsData);
		}catch(Exception e){
			BizException e1 = new BizException(act,e,ErrorCodeConstant.ACTION_NAME_ERROR_CODE,"坐席话务量统计查询");
			logger.error(logger,e1);
			act.setException(e1);
		}
	}
	
	private void putDatas(List<Map<String, Object>> list,SeatTalksReportformsDao dao,String dateStart,String dateEnd){
		//总计总通话时长
		String totalTalkTimeALL = null;
		//总计呼出时长
		String outTalkTimeALL = null;
		//总计呼入时长
		String inTalkTimeALL = null;
		
		//总计平均总通话时长
		String totalTalkTimeAVGALL = null;
		//总计平均呼出时长
		String outTalkTimeAVGALL = null;
		//总计平均呼入时长
		String inTalkTimeAVGALL = null;
		
		for(Map<String, Object> map : list){
			String acc = (String)map.get("ACC");
			if(acc.equals("合计")) continue;
			
			List<Map<String,Object>> ACCList = dao.getCallRecordsByACC(acc, dateStart, dateEnd);
			//总通话时长
			String totalTalkTime = null;
			//呼出时长
			String outTalkTime = null;
			//呼入时长
			String inTalkTime = null;
			
			for(Map<String,Object> accMap : ACCList){
				String talkTime = (String)accMap.get("TALKTIME");
				String callType = ((BigDecimal)accMap.get("CALLTYPE")).toString();
				totalTalkTime  = addTalkTime(talkTime, totalTalkTime);
				if(callType.equals(Constant.CALL_IN_TYPE)) inTalkTime  = addTalkTime(talkTime, inTalkTime);
				if(callType.equals(Constant.CALL_OUT_TYPE)) outTalkTime  = addTalkTime(talkTime, outTalkTime);
			}
			
			map.put("TOTALTALKTIME", totalTalkTime);
			map.put("OUTTALKTIME", outTalkTime);
			map.put("INTALKTIME", inTalkTime);
			
			map.put("TOTALTALKTIMEAVG", avgTalkTime(totalTalkTime,((BigDecimal)map.get("TOTALC")) == null ?null: ((BigDecimal)map.get("TOTALC")).toString()));
			map.put("OUTTALKTIMEAVG", avgTalkTime(outTalkTime, ((BigDecimal)map.get("OUTC")) == null ?null:((BigDecimal)map.get("OUTC")).toString()));
			map.put("INTALKTIMEAVG", avgTalkTime(inTalkTime, ((BigDecimal)map.get("INC")) == null ?null:((BigDecimal)map.get("INC")).toString()));
			
			totalTalkTimeALL  = addTalkTime(totalTalkTime, totalTalkTimeALL);
			outTalkTimeALL  = addTalkTime(outTalkTime, outTalkTimeALL);
			inTalkTimeALL  = addTalkTime(inTalkTime, inTalkTimeALL);
		
		}
		
		//总计PUT
		list.get(list.size()-1).put("TOTALTALKTIME", totalTalkTimeALL);
		list.get(list.size()-1).put("OUTTALKTIME", outTalkTimeALL);
		list.get(list.size()-1).put("INTALKTIME", inTalkTimeALL);
		
		list.get(list.size()-1).put("TOTALTALKTIMEAVG", totalTalkTimeAVGALL);
		list.get(list.size()-1).put("OUTTALKTIMEAVG", outTalkTimeAVGALL);
		list.get(list.size()-1).put("INTALKTIMEAVG", inTalkTimeAVGALL);
	
	}
	
	private String addAvgTime(String totalTime,String newTime){
		if(totalTime == null) return newTime;
		if(newTime == null || "".equals(newTime)) return totalTime;
		
		return String.valueOf((Integer.parseInt(totalTime)+Integer.parseInt(newTime)));
	}
	
	//时长相加算法
	private String addTalkTime(String talkTime,String tempTalkTime){
		if(talkTime == null || "".equals(talkTime)) return tempTalkTime;
		if(tempTalkTime == null || "".equals(tempTalkTime)) return talkTime;
		
		int hours  =Integer.parseInt(talkTime.split(":")[0]); //初始小时
		int min  =Integer.parseInt(talkTime.split(":")[1]);   //初始分钟
		int sec  =Integer.parseInt(talkTime.split(":")[2]);   //初始秒
		
		int hoursTemp  = Integer.parseInt(tempTalkTime.split(":")[0]); //初始昨时小时
		int minTemp  = Integer.parseInt(tempTalkTime.split(":")[1]);   //初始昨时分钟
		int secTemp  = Integer.parseInt(tempTalkTime.split(":")[2]);   //初始昨时秒
		
		int endSec = (sec+secTemp)%60;
		int endmin = ((sec+secTemp)/60+min+minTemp)%60;
		int endHours = ((sec+secTemp)/60+min+minTemp)/60+hours+hoursTemp;
		
		return endHours+":"+endmin+":"+endSec;
	}
	
	//求平均时间 秒
	private String avgTalkTime(String talkTime,String times){
		if(talkTime == null || "".equals(talkTime)) return null;
		if(times == null || "".equals(times)) return null;
		
		int timesT = Integer.parseInt(times);
		
		int hours  =Integer.parseInt(talkTime.split(":")[0]); //初始小时
		int min  =Integer.parseInt(talkTime.split(":")[1]);   //初始分钟
		int sec  =Integer.parseInt(talkTime.split(":")[2]);   //初始秒
		
		return (hours*60*60+min*60+sec)/timesT+"";
	}
	
	public void seatTalksReportformsExcel(){
		try{
			String dealName = CommonUtils.checkNull(request.getParamValue("dealName"));  
			String dateStart = CommonUtils.checkNull(request.getParamValue("dateStart"));  				
			String dateEnd = CommonUtils.checkNull(request.getParamValue("dateEnd"));  
			String paiStart = CommonUtils.checkNull(request.getParamValue("paiStart"));  				
			String paiEnd = CommonUtils.checkNull(request.getParamValue("paiEnd"));  
						
			SeatTalksReportformsDao dao = SeatTalksReportformsDao.getInstance(); 
			
			List<Map<String,Object>> list = dao.querySeatTalksReportformsDao(dealName,dateStart,dateEnd,paiStart,paiEnd);

			if( list != null && list.size()>0){
//				list.get(list.size()-1).put("NAME", "");
//				putDatas(list, dao, dateStart, dateEnd);
				seatTalksReportformsDataToExcel(list);
				if(list.size()>10000) {act.setOutData("DataLonger", "数据超过10000条,最高导出10000条数据");}
			}
				
			act.setForword(seatTalksReportformsUrl);
		}catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.SPECIAL_MEG,"转Excel失败!");
			logger.error(logonUser,e1);
			act.setException(e1);
		}

	}
	
	private void seatTalksReportformsDataToExcel(List<Map<String, Object>> list) throws Exception{
		String[] head=new String[11];
		head[0]="工号";
		head[1]="姓名";
		head[2]="总通话次数";
		head[3]="总通话时长";
		head[4]="均通话时长";
		head[5]="呼出次数";
		head[6]="呼出时长";
		head[7]="均呼出时长";
		head[8]="呼入次数";
		head[9]="呼入时长";
		head[10]="均呼入时长";

		
	    List list1=new ArrayList();
	    if(list!=null&&list.size()!=0){
			for(int i=0;i<list.size();i++){
		    	Map map =(Map)list.get(i);
		    	if(map!=null&&map.size()!=0){
					String[] detail=new String[11];
					detail[0] = CommonUtils.checkNull(map.get("ACC")== null ? "" :  map.get("ACC").toString());
					detail[1] = CommonUtils.checkNull(map.get("NAME")== null ? "" : map.get("NAME").toString());
					detail[2] = CommonUtils.checkNull(map.get("TOTALC")== null ? "" : map.get("TOTALC").toString());
					detail[3] = CommonUtils.checkNull(map.get("TOTALTALKTIME")== null ? "" : map.get("TOTALTALKTIME").toString());
					detail[4] = CommonUtils.checkNull(map.get("TOTALTALKTIMEAVG")== null ? "" : map.get("TOTALTALKTIMEAVG").toString());
					detail[5] = CommonUtils.checkNull(map.get("OUTC")== null ? "" : map.get("OUTC").toString());
					detail[6] = CommonUtils.checkNull(map.get("OUTTALKTIME")== null ? "" : map.get("OUTTALKTIME").toString());
					detail[7] = CommonUtils.checkNull(map.get("OUTTALKTIMEAVG")== null ? "" : map.get("OUTTALKTIMEAVG").toString());
					detail[8] = CommonUtils.checkNull(map.get("INC")== null ? "" : map.get("INC").toString());
					detail[9] = CommonUtils.checkNull(map.get("INTALKTIME")== null ? "" : map.get("INTALKTIME").toString());
					detail[10] = CommonUtils.checkNull(map.get("INTALKTIMEAVG")== null ? "" : map.get("INTALKTIMEAVG").toString());

					list1.add(detail);
		    	}
		    }
			this.exportEx(ActionContext.getContext().getResponse(), request, head, list1);
	    }
	}	

	public static Object exportEx(ResponseWrapper response,
			RequestWrapper request, String[] head, List<String[]> list)
			throws Exception {

		String name = "坐席话务量统计.xls";
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
