package com.infodms.dms.actions.sales.planmanage.PlanUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.infodms.dms.common.Constant;
import com.infodms.dms.po.TmBusinessParaPO;
import com.infodms.dms.po.TmDateSetPO;
import com.infodms.dms.po.TmOrgPO;



public class PlanUtil {

	/**
	 * 取得当年和下一年的option列表
	 * year，如果需要确认select位置传入参数，否则为""
	 * @return
	 */
	public static  String getPlanYearString(String year){
		StringBuffer sbf=new StringBuffer("");
		Calendar calendar=Calendar.getInstance();
		int s=calendar.get(Calendar.YEAR);
		sbf.append("<option value='"+calendar.get(Calendar.YEAR)+"'");
		if(!"".equals(year)&&year.equals(new Integer(s).toString())){
			sbf.append("selected='true'");
		}
		sbf.append(">");
		sbf.append(calendar.get(Calendar.YEAR)+"");
		sbf.append("</option>");
		calendar.add(Calendar.YEAR, 1);
		int ss=calendar.get(Calendar.YEAR);
		sbf.append("<option value='"+calendar.get(Calendar.YEAR)+"'");
		if(!"".equals(year)&&year.equals(new Integer(ss).toString())){
			sbf.append("selected='true'");
		}
		sbf.append(">");
		sbf.append(calendar.get(Calendar.YEAR)+"");
		sbf.append("</option>");
		return sbf.toString();
	}
	/*
	 * 当年
	 */
	public static String getCurrentYear(){
		String year="";
		Calendar calendar=Calendar.getInstance();
		year=calendar.get(Calendar.YEAR)+"";
		return year;
	}
	
	/*
	 * 当年当月
	 */
	public static String getCurrentMonth(){
		String month="0";
		Calendar calendar=Calendar.getInstance();
		month=calendar.get(Calendar.MONTH)+1+"";
		return month;
	}
	/*
	 * 当日
	 */
	public static int getCurrentDay(){
		int day=0;
		Calendar calendar=Calendar.getInstance();
		day=calendar.get(Calendar.DAY_OF_MONTH);
		return day;
	}
	
	/*
	 * 当日是星期几
	 */
	public static String getCurrentWeek(){
		int day=0;
		String s="";
		Calendar calendar=Calendar.getInstance();
		day=calendar.get(Calendar.DAY_OF_WEEK)-1;
		switch (day) {
		case 0:
			s="星期日";
			break;
		case 1:
			s="星期一";
			break;
		case 2:
			s="星期二";
			break;
		case 3:
			s="星期三";
			break;
		case 4:
			s="星期四";
			break;
		case 5:
			s="星期五";
			break;
		case 6:
			s="星期六";
			break;
		default:
			break;
		}
		return s;
	}
	/*
	 * 返回INT类型的星期几
	 */
	public static int getCurIntWeek(){
		int day=0;
		Calendar calendar=Calendar.getInstance();
		day=calendar.get(Calendar.DAY_OF_WEEK)-1;
		return day;
	}
	/*
	 * 返回Calendar当前周是年中第几周
	 */
	public static int getCurWeekOfYear(){
		int weekIdx=0;
		Calendar calendar=Calendar.getInstance();
		weekIdx=calendar.get(Calendar.WEEK_OF_YEAR);
		return weekIdx;
	}
	/*
	 * 取当前周次下N周的周日-周六 星期，格式(month.date,周几)字符串
	 */
	public static List<String> getAimDate(int n){
		List<String> list=new ArrayList<String>();
		String dateStr="";
		Calendar calendar=Calendar.getInstance();
		int week=calendar.get(Calendar.WEEK_OF_YEAR);
		calendar.set(Calendar.WEEK_OF_YEAR, week+n);
		int dayOfWeek=calendar.get(Calendar.DAY_OF_WEEK);
	    calendar.add(Calendar.DAY_OF_WEEK, -dayOfWeek);
		for(int i=0;i<7;i++){
			calendar.add(Calendar.DAY_OF_WEEK, 1);
			dateStr=calendar.get(Calendar.MONTH)+1+"."+calendar.get(Calendar.DAY_OF_MONTH);
			list.add(dateStr);
		}
		return list;
	}
	/*
	 * 查询当N+X天MMDD
	 */
	public static String getNextNDay(int n){
		Calendar calendar=Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_YEAR, n);
		int m=calendar.get(Calendar.MONTH)+1;
		String mm=m>9?m+"":"0"+m+"";
		int d=calendar.get(Calendar.DAY_OF_MONTH);
		String dd=d>9?d+"":"0"+d;
		String dateStr=mm+"."+dd;
		return dateStr;
	}
	/*
	 * 取得下N+X个月的月份的日期
	 */
	public static String getRadomDate(int idx,String type){
		Calendar calendar=Calendar.getInstance();
		calendar.add(Calendar.MONTH, idx);
		if("year".equals(type)){
			return calendar.get(Calendar.YEAR)+"";
		}else{
			return calendar.get(Calendar.MONTH)+1+"";
		}
		
	}
	/*
	 *  取得下N+X个月的月份的日期
	 */
	public static Map<String, Object> getMapDate(int idx){
		Map<String, Object> map=new HashMap<String, Object>();
		Calendar calendar=Calendar.getInstance();
		calendar.add(Calendar.MONTH, idx);
		map.put("YEAR",calendar.get(Calendar.YEAR)+"");
		map.put("MONTH",calendar.get(Calendar.MONTH)+1+"");
		return map;
	}
	/*
	 * 取月份option
	 */
	public static String getMonthOptions(String month){
		if(null==month||"".equals(month)){
			month="0";
		}
		Calendar calendar=Calendar.getInstance();
		int curMon=calendar.get(Calendar.MONTH)+1;
		StringBuffer options=new StringBuffer("");
		for(int i=curMon;i<13;i++){
			options.append("<option value='"+i+"'");
			if(i==new Integer(month).intValue()){
				options.append(" selected='true'");
			}
			options.append(">"+i+"</option>");
		}
		
		return options.toString();
	}
	/*
	 * 校验组织是否与业务范围匹配
	 */
	public static boolean isOrgInArea(String orgCode,Long areaId){
		PlanUtilDao dao=PlanUtilDao.getInstance();
		return dao.isOrgInArea(orgCode,areaId);
	}
	/*
	 * 校验车系是否与业务范围匹配
	 */
	public static boolean isGroupInArea(String groupCode,Long areaId){
		PlanUtilDao dao=PlanUtilDao.getInstance();
		return dao.isGroupInArea(groupCode,areaId);
	}
	/*
	 * 以字符串的形式返回一个包含业务范围下所有车系
	 * actType返回GROUP_ID还是GROUP_CODE的串 1:GROUP_ID,2:GROUP_CODE
	 * groupLevel 1:取品牌 2:取车系 3:取车型 4:取配置
	 */
    public static String getAllGroupInArea( String companyId,String actType,int groupLevel){
    	String getType="";
    	if("1".equals(actType)){
    		getType="GROUP_ID";
    	}else{
    		getType="GROUP_CODE";
    	}
    	StringBuffer groupArea=new StringBuffer("");
		List<Map<String, Object>> groupAreaList=PlanUtil.selectAreaGroup(companyId,groupLevel);
		if(null==groupAreaList){
			groupAreaList=new ArrayList<Map<String,Object>>();
		}
		for(int i=0;i<groupAreaList.size();i++){
			Map<String, Object> map=groupAreaList.get(i);
			groupArea.append(map.get(getType).toString()+",");
		}
		if(groupArea.indexOf(",")!=-1){
			groupArea.delete(groupArea.length()-1, groupArea.length());
		}
		return groupArea.toString();
    }
	/*
	 * 查询业务范围内的车系
	 */
	public static List<Map<String, Object>> selectAreaGroup( String companyId,int groupLevel){
		PlanUtilDao dao=PlanUtilDao.getInstance();
		return dao.selectAreaGroup(companyId,groupLevel);
	}
	/*
	 * 查询配置是否在业务范围内
	 * groupId配置ID
	 * areaId业务范围ID
	 * return: boolean true否在，false不否在
	 */
	public static boolean isConfigInArea(String groupId,String groupCode,String areaId){
		PlanUtilDao dao=PlanUtilDao.getInstance();
		return dao.isConfigInArea(groupId,groupCode, areaId);
	}
	
	/**
	 * 判断经销商和业务范围是否相符
	 * @param dealerCode
	 * @param areaId
	 * @return
	 */
	public static boolean isDealerAreaAccord(String dealerCode, String areaId){
		PlanUtilDao dao = PlanUtilDao.getInstance();
		return dao.isDealerAreaAccord(dealerCode, areaId);
	}
	
	/**
	 * 判断区域代码和业务范围是否相符
	 * @param orgCode
	 * @param areaId
	 * @return
	 */
	public static boolean isOrgAreaAccord(String orgCode, String areaId){
		PlanUtilDao dao = PlanUtilDao.getInstance();
		return dao.isOrgAreaAccord(orgCode, areaId);
	}
	
	/**
	 * 判断经销商是否在此区域内
	 * @param orgId
	 * @param dealerCode
	 * @return
	 */
	public static boolean isOrgDealerAccord(String orgId, String dealerCode){
		PlanUtilDao dao = PlanUtilDao.getInstance();
		return dao.isOrgDealerAccord(orgId, dealerCode);
	}
	
	/*
	 * 查询预测参数
	 */
	public static PlanParaBean selectForecastParas(){
		PlanUtilDao dao=PlanUtilDao.getInstance();
		PlanParaBean bean=new PlanParaBean();
		List<TmBusinessParaPO> list=dao.selectForecastPara();
		if(null==list){
			return null;
		}
		for(int i=0;i<list.size();i++){
			TmBusinessParaPO po=list.get(i);
			int paraId=po.getParaId().intValue();
			switch (paraId) {
			case 20061001:
				bean.setForecastPreMonth(po.getParaValue()==null?0:new Integer(po.getParaValue()).intValue());
				break;
			case 20061002:
				bean.setForecastMonthAmt(po.getParaValue()==null?0:new Integer(po.getParaValue()).intValue());
				break;
			case 20061003:
				bean.setShowHistoryMonthAmt(po.getParaValue()==null?0:new Integer(po.getParaValue()).intValue());
				break;
			case 20071001:
				bean.setDealerForecastStartDay(po.getParaValue()==null?0:new Integer(po.getParaValue()).intValue());
				break;
			case 20071002:
				bean.setDealerForecastEndDay(po.getParaValue()==null?0:new Integer(po.getParaValue()).intValue());
				break;
			case 20151001:
				bean.setSbuForecastStartDay(po.getParaValue()==null?0:new Integer(po.getParaValue()).intValue());
				break;
			case 20151002:
				bean.setSbuForecastEndDay(po.getParaValue()==null?0:new Integer(po.getParaValue()).intValue());
				break;
			default:
				break;
			}
		}
		return bean;
	}

	/*
	 * 查询ORGID，如果是部门返回公司ORGID,否则返回原ORGID
	 */
	public static Long getRootOrgId(Long orgId){
		if(null==orgId){
			return null;
		}
		PlanUtilDao dao=PlanUtilDao.getInstance();
		TmOrgPO conPo=new TmOrgPO();
		conPo.setOrgId(orgId);
		List<TmOrgPO> list=dao.select(conPo);
		if(null!=list&&list.size()>0){
			TmOrgPO po=list.get(0);
			if(po.getDutyType()==Constant.DUTY_TYPE_DEPT){
				orgId=po.getParentOrgId();
			}
		}
		return orgId;
	}
	/*
	 * 查询工作日历周次
	 */
	public static List<Map<String, Object>> selectDateSetWeekList(TmDateSetPO conPo){
		PlanUtilDao dao=PlanUtilDao.getInstance();
		return dao.selectDateSetWeekList(conPo);
	}
	//查询YYYYMMDD的周次
	public static Map<String, Object> getSetDateCurWeek(String setDate,String companyId){
		PlanUtilDao dao=PlanUtilDao.getInstance();
		return dao. getSetDateCurWeek(setDate,companyId);
	}
	/*
	 * 取得下一周次的，年周，如果下周已经超出年最大周次，就返回下一年第一周
	 */
	public static Map<String, Object> getSetDateNextWeekMap(String year,String week,String companyId){
		PlanUtilDao dao=PlanUtilDao.getInstance();
		int nextWeek=Integer.parseInt(week)+1;
		Map<String, Object> map=dao.getSetDateMaxWeek(year,companyId);
		int maxWeek=Integer.parseInt(map.get("MAX_WEEK").toString());
		if(nextWeek>maxWeek){
			year=Integer.parseInt(year)+1+"";
			nextWeek=1;
		}
		String month=getDateSetMonth(year, nextWeek+"");
		Map<String, Object> weekMap=new HashMap<String, Object>();
		weekMap.put("YEAR", year);
		weekMap.put("MONTH", month);
		weekMap.put("WEEK", nextWeek+"");
		return weekMap;
	}
	/*
	 * 查询SET_DATE为YYYYMM的所有周次
	 */
	public List<Map<String, Object>>  getSetDateCurWeekList(String setDate){
		PlanUtilDao dao=PlanUtilDao.getInstance();
		return dao.getSetDateWeekList(setDate);
	}
	/*
	 * 查询工作日历月份
	 */
	public static String getDateSetMonth(String year,String week){
		String month="0";
		TmDateSetPO conpo=new TmDateSetPO();
		conpo.setSetYear(year);
		conpo.setSetWeek(week);
		PlanUtilDao dao=PlanUtilDao.getInstance();
		Map<String, Object> map=dao.selectDateSetMonth(conpo);
		month=map.get("SET_MONTH").toString();
		return month;
	}
	/*
	 * 返回SQL查询中IN()参数，这个参数不能以params.ad()方式添加
	 */
	public static String createSqlStr(String s){
		StringBuffer rs=new StringBuffer("");
		if(null==s){
			return rs.toString();
		}
		if(s.indexOf(",")==-1){
			return "'"+s+"'";
		}
		String[] arr=s.split(",");
		for(int i=0;i<arr.length;i++){
			rs.append("'"+arr[i]+"',");
		}
		rs.delete(rs.lastIndexOf(","), rs.length());
		return rs.toString();
	}
	/*
	 * 查询所有车系
	 */
	public static List<Map<String, Object>> selectSeries(){
		PlanUtilDao dao=PlanUtilDao.getInstance();
		return dao.selectSeries();
	}
	/*
	 * 取年-周次的七天日期
	 */
	public static List<String> getWeekDate(String year,String week){
		String dateStr="";
		if(null==year||null==week){
			return null;
		}
		List<String> list=new ArrayList();
		Calendar calendar=Calendar.getInstance();
		calendar.set(Calendar.YEAR, new Integer(year).intValue());
		//calendar.set(Calendar.MONTH, 7);
		calendar.set(Calendar.WEEK_OF_YEAR, new Integer(week).intValue());
		int dayOfWeek=calendar.get(Calendar.DAY_OF_WEEK);
	    calendar.add(Calendar.DAY_OF_WEEK, -dayOfWeek);
		for(int i=0;i<7;i++){
			calendar.add(Calendar.DAY_OF_WEEK, 1);
			dateStr=calendar.get(Calendar.MONTH)+1+"."+calendar.get(Calendar.DAY_OF_MONTH);
			System.out.println(dateStr);
			list.add(dateStr);
		}
		return list;
	}
	/*
	 * 查询所有车系
	 */
	public static List<Map<String, Object>> selectSeries(String companyId){
		PlanUtilDao dao=PlanUtilDao.getInstance();

		return dao.selectSeries(companyId);
	}
	/*
	 * 查询TM_BUSINESS_PARA
	 * codeStr :对应表内CODE_TYPE 如果多个CODE_TYPE,用“，”分开
	 * oemCompanyId 车厂公司ID
	 */
	public static Map<String, Object>  selectBussinessPara(String codeStr,String oemCompanyId){
		PlanUtilDao dao=PlanUtilDao.getInstance();
		List<Map<String, Object>> list=dao.selectBussinessPara(codeStr, oemCompanyId);
		Map<String, Object> map=new HashMap<String, Object>();
		if(null!=list&&list.size()>0){
			for(int i=0;i<list.size();i++){
				Map<String, Object> m=list.get(i);
				map.put(m.get("PARA_ID").toString(), m.get("PARA_VALUE").toString());
			}
		}else{
			return null;
		}
		return map;
	}
	/*
	 * 校验组织与登陆用户是否在同一业务范围
	 */
	public static boolean isSameBussArea(String orgCode,String orgId,String areaId){
		return false;
	}
	public static void main(String[] args){
		getNextNDay(7);
	}
}
