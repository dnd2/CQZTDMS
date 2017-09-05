package com.infodms.dms.dao.claim.auditing;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.log4j.Logger;

import com.infodms.dms.actions.claim.application.BalanceStatusRecord;
import com.infodms.dms.actions.claim.application.DealerBalance;
import com.infodms.dms.actions.claim.application.DealerNewKp;
import com.infodms.dms.actions.claim.auditing.rule.custom.RuleVO;
import com.infodms.dms.actions.claim.balanceAuditing.BalanceAuditing;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.DBLockUtil;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.common.getCompanyId.GetOemcompanyId;
import com.infodms.dms.dao.claim.application.ClaimManualAuditingDao;
import com.infodms.dms.dao.claim.application.DealerBalanceDao;
import com.infodms.dms.dao.claim.application.DealerKpDao;
import com.infodms.dms.dao.claim.application.DealerNewKpUpdateDAO;
import com.infodms.dms.dao.claim.authorization.BalanceMainDao;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.dao.feedbackMng.MarketQuesOrderDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TcCodePO;
import com.infodms.dms.po.TcUserPO;
import com.infodms.dms.po.TmBusinessParaPO;
import com.infodms.dms.po.TmClaimContactPhonePO;
import com.infodms.dms.po.TmDealerPO;
import com.infodms.dms.po.TmPtDcPO;
import com.infodms.dms.po.TmPtOlderItemPO;
import com.infodms.dms.po.TmRegionPO;
import com.infodms.dms.po.TtAsDealerCheckPO;
import com.infodms.dms.po.TtAsDealerTypePO;
import com.infodms.dms.po.TtAsWrAdminDeductPO;
import com.infodms.dms.po.TtAsWrApplicationPO;
import com.infodms.dms.po.TtAsWrCheckApplicationPO;
import com.infodms.dms.po.TtAsWrCheckDetailPO;
import com.infodms.dms.po.TtAsWrClaimBalancePO;
import com.infodms.dms.po.TtAsWrClaimBalanceTmpPO;
import com.infodms.dms.po.TtAsWrDeductBalancePO;
import com.infodms.dms.po.TtAsWrFinePO;
import com.infodms.dms.po.TtAsWrRuleitemPO;
import com.infodms.dms.po.TtAsWrRulemappingPO;
import com.infodms.dms.po.TtAsWrSpefeePO;
import com.infodms.dms.po.TtIfMarketPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.core.context.POContext;

/**
 * 结算审核授权规则
 */
@SuppressWarnings("unchecked")
public class autoBlanaceDao extends BaseDao {
	private Logger logger = Logger.getLogger(autoBlanaceDao.class);
	private static autoBlanaceDao autoBlanaceDao;
	private final DealerNewKpUpdateDAO daoKP = DealerNewKpUpdateDAO.getInstance();
	private autoBlanaceDao(){
		super();
	}
	
	public static autoBlanaceDao getInstance(){
		if(autoBlanaceDao==null){
			autoBlanaceDao = new autoBlanaceDao();
		}
		return autoBlanaceDao;
	}
	
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public Date lastDayOfMonth(Date date) { 
		Calendar cal = Calendar.getInstance(); 
		cal.setTime(date); 
		cal.set(Calendar.DAY_OF_MONTH, 1); 
		cal.roll(Calendar.DAY_OF_MONTH, -1); 
		
		return cal.getTime(); 
	} 
	public Date lastDayOfMonth2(Date date) { 
		Calendar cal = Calendar.getInstance(); 
		cal.setTime(date); 
		cal.set(Calendar.DAY_OF_MONTH, 1); 
		cal.add(Calendar.MONTH, -1);
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		format.format(cal.getTime());
		System.out.println("-----------------------"+format.format(cal.getTime()));
		cal.add(Calendar.MONTH, 1);
		cal.add(Calendar.DAY_OF_MONTH, -1);

		System.out.println("-----------------------"+format.format(cal.getTime()));
		return cal.getTime(); 
	} 

	public TtAsDealerTypePO showDateAutoBlanace(String yieldly,String dealerId ) throws ParseException{
		ActionContext act = ActionContext.getContext();
		RequestWrapper request = act.getRequest();
		TtAsDealerTypePO po = new TtAsDealerTypePO();
		po.setDealerId(Long.parseLong(dealerId));
		po.setYieldly(yieldly);
		TtAsDealerTypePO poValue = (TtAsDealerTypePO)factory.select(po).get(0);
		
		Date balanceReViewDate=poValue.getBalanceReviewDate();
		Date oldReViewDate=poValue.getOldReviewDate();
		
		Calendar calendar = Calendar.getInstance();//公用类，加年月日的
		calendar.setTime(balanceReViewDate);
		calendar.add(Calendar.DAY_OF_MONTH,1);//当月加一天
		balanceReViewDate = calendar.getTime();//得到加一天后的值
		
		String place="";
		if(yieldly.equals(Constant.SERVICEACTIVITY_CAR_YIELDLY_01.toString())) place=Constant.PLACE_OF_CQ;
		if(yieldly.equals(Constant.SERVICEACTIVITY_CAR_YIELDLY_02.toString())) place=Constant.PLACE_OF_HB;
		if(yieldly.equals(Constant.SERVICEACTIVITY_CAR_YIELDLY_03.toString())) place=Constant.PLACE_OF_NJ;
		if(yieldly.equals(Constant.SERVICEACTIVITY_CAR_YIELDLY_03.toString())) place=Constant.PLACE_OF_CH;
		
//		TcCodePO code=new TcCodePO();
//		code.setCodeId(place);
//		code=(TcCodePO)factory.select(code).get(0);
//		String month=code.getCodeDesc();//通过产地取得月数
		
		TmPtOlderItemPO item=new TmPtOlderItemPO();
		item.setYieldly(Long.parseLong(yieldly));
		item.setDealerId(Long.parseLong(dealerId));
		item=(TmPtOlderItemPO)factory.select(item).get(0);
		String month=String.valueOf(item.getReturnDate());
		
		Calendar calendarOldRe = Calendar.getInstance(); 
		calendarOldRe.setTime(oldReViewDate);
		calendarOldRe.add(Calendar.MONTH, Integer.parseInt(month));
		Date tmpDate=calendarOldRe.getTime();
		System.out.println("--------------"+tmpDate);
		Date currDate=new Date();
		Date endDate=null;
		Date endDate2 = tmpDate;
		Date kkkDate=lastDayOfMonth(tmpDate);
  		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		if(format.parse(format.format(kkkDate)).after(format.parse(format.format(currDate)))||(!format.parse(format.format(kkkDate)).after(format.parse(format.format(currDate)))&&!format.parse(format.format(currDate)).after(format.parse(format.format(kkkDate))))){//tmpDate大于currDate或tmpDate等于currDate
			Calendar calendarOldRe2 = Calendar.getInstance();
			calendarOldRe2.setTime(new Date());
			//calendarOldRe2.add(Calendar.MONTH, -1);//当前月减1月
			calendarOldRe2.setTime(lastDayOfMonth(calendarOldRe2.getTime()));
			endDate=calendarOldRe2.getTime();
		}else{
			
			endDate=lastDayOfMonth(endDate2);
		}
		Calendar calendarDay=Calendar.getInstance();
		calendarDay.setTime(endDate);
		
		TcCodePO code=new TcCodePO();
		code.setCodeId(Constant.DAY_12);
		code=(TcCodePO)factory.select(code).get(0);
		String dayStr=code.getCodeDesc();
		int monthCur=calendarDay.get(Calendar.MONTH)+1;
		//if(monthCur==12){//如果是12月
		//	calendarDay.set(Calendar.DAY_OF_MONTH, Integer.parseInt(dayStr));
		//}
		int yearStart=calendar.get(Calendar.YEAR);
		int yearEnd=calendarDay.get(Calendar.YEAR);
		int dayStart=calendarDay.get(Calendar.DAY_OF_MONTH);
		int startDate = calendar.get(Calendar.DAY_OF_MONTH);
		System.out.println("-----------"+yearStart+"--------------"+yearEnd);
		//判断是否跨年
		if(yearEnd>yearStart){//是跨年
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
			Date date22=sdf.parse(yearStart+"-12-"+dayStr);
			//判断跨年后开始时间天数大约结束时间天数不能强制设置为12月的结算点
			Calendar calendarDay1=Calendar.getInstance();
			calendarDay1.setTime(date22);
			Date dateAdd = null;
			int newDay = calendarDay1.get(Calendar.DAY_OF_MONTH);
			if(startDate<newDay){
				
				calendarDay=Calendar.getInstance();
				calendarDay.setTime(date22);
			}else{
				Calendar calendarDate=Calendar.getInstance();
				calendarDate.setTime(endDate);
				int year1 = calendarDate.get(Calendar.YEAR);
				int mouth1 = calendarDate.get(Calendar.MONTH)+1;
				int day1 = calendarDate.get(Calendar.DAY_OF_MONTH);
				Calendar calendarDateNow=Calendar.getInstance();
				calendarDateNow.setTime(new Date());
				int dayNow = calendarDateNow.get(Calendar.DAY_OF_MONTH);
				System.out.println("-----------"+year1+mouth1+day1);
				
				
				if(mouth1==12){
					if(Integer.valueOf(dayStr)<day1&&dayNow>Integer.valueOf(dayStr)){
						Date d =sdf.parse(year1+"-12-"+dayStr);
						calendarDay.setTime(d);
					}else{
						if(format.parse(format.format(endDate)).after(format.parse(format.format(new Date())))||(!format.parse(format.format(endDate)).after(format.parse(format.format(new Date())))&&!format.parse(format.format(new Date())).after(format.parse(format.format(endDate))))){//tmpDate大于currDate或tmpDate等于currDate
						calendarDay.setTime(lastDayOfMonth2(endDate));
						}else{
							calendarDay.setTime(endDate);	
						}
					}
				}else{
					calendarDay=Calendar.getInstance();
					System.out.println("--------------------------"+lastDayOfMonth2(endDate));
					if(format.parse(format.format(endDate)).after(format.parse(format.format(new Date())))||(!format.parse(format.format(endDate)).after(format.parse(format.format(new Date())))&&!format.parse(format.format(new Date())).after(format.parse(format.format(endDate))))){//tmpDate大于currDate或tmpDate等于currDate
						calendarDay.setTime(lastDayOfMonth2(endDate));
						}else{
							calendarDay.setTime(endDate);	
						}
				}
				
			}
			
			
		}
		if(yearEnd==yearStart){ 
			if(monthCur==12){
				SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
				Date date22=sdf.parse(yearStart+"-12-"+dayStr);
				//判断跨年后开始时间天数大约结束时间天数不能强制设置为12月的结算点
				Calendar calendarDay1=Calendar.getInstance();
				Calendar calendarDayNow=Calendar.getInstance();
				calendarDayNow.setTime(new Date());
				calendarDay1.setTime(date22);
				int newDay = calendarDay1.get(Calendar.DAY_OF_MONTH);
				int nowDate2 =calendarDayNow.get(Calendar.DAY_OF_MONTH);
				if(newDay>=nowDate2 ) System.out.println("-----------------------"+format.format(date22));
				if(startDate<newDay){
					calendarDay=Calendar.getInstance();
					if(nowDate2<=newDay ){
						calendarDay.setTime(lastDayOfMonth2(date22));
					}
					else{
					  calendarDay.setTime(date22);
					  
					  
					}
					
				}else{
					calendarDay.setTime(endDate);
				}
				
			}else{
				if(format.parse(format.format(endDate)).after(format.parse(format.format(new Date())))||(!format.parse(format.format(endDate)).after(format.parse(format.format(new Date())))&&!format.parse(format.format(new Date())).after(format.parse(format.format(endDate))))){//tmpDate大于currDate或tmpDate等于currDate
					calendarDay.setTime(lastDayOfMonth2(endDate));
				}else{
					calendarDay.setTime(endDate);
				}
			}
		}
		if(yearEnd<yearStart){
			calendarDay.setTime(endDate);
			
		}
		TtAsDealerTypePO typo =new TtAsDealerTypePO();
		typo.setBalanceReviewDate(balanceReViewDate);
		typo.setBalanceDate(calendarDay.getTime());
		return typo;
		//act.setOutData("balanceDateEnd", "2011-03-31");
	}
	
	public Boolean getFalg(TtAsDealerTypePO po){
		
		DealerBalanceDao balanceDao = DealerBalanceDao.getInstance();
		Boolean falg=true;
		try{
			TmBusinessParaPO poCon = new TmBusinessParaPO();
			poCon.setTypeCode(Constant.TYPE_CODE);
			TmBusinessParaPO poConValue = (TmBusinessParaPO)balanceDao.select(poCon).get(0);
			String timeCon=poConValue.getParaValue();
			timeCon=timeCon.substring(11, timeCon.length());
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date timeCon2=sdf.parse(timeCon);
			Calendar calendar = Calendar.getInstance();//公用类，加年月日的
			calendar.setTime(timeCon2);
			calendar.add(Calendar.DAY_OF_MONTH, 1);//当月加一天(如果是本月的最后一天那么结果会是下月的第一天)
			timeCon2 = calendar.getTime();//得到加一天后的值
			String endBalanceDate =  sdf.format(po.getBalanceReviewDate());
			String CON_END_DAY = sdf.format(po.getBalanceDate());//选择的时间
			Date endBalanceDate1=sdf.parse(endBalanceDate);
			if(po.getBalanceDate().before(po.getBalanceReviewDate())){
				falg=false;
			}
			if(endBalanceDate1.before(timeCon2)){
				falg=false;
			}
			//判断选择时间不能大于等于当前时间
			Date time = new Date();
			String time1 = sdf.format(time);
			Date time2 = sdf.parse(time1);//当前时间
			if(sdf.parse(CON_END_DAY).after(time2) || CON_END_DAY.equals(time1)){
				falg=false;
			}
			
			SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd"); 
	        long monthday; 
	        try { 
	            Date startDate1 = f.parse(endBalanceDate); 
	            //开始时间与今天相比较 
	            Date endDate1 = f.parse(CON_END_DAY); 

	            Calendar starCal = Calendar.getInstance(); 
	            starCal.setTime(startDate1); 

	            int sYear = starCal.get(Calendar.YEAR); 
	            int sMonth = starCal.get(Calendar.MONTH); 
	            int sDay = starCal.get(Calendar.DATE); 

	            Calendar endCal = Calendar.getInstance(); 
	            endCal.setTime(endDate1); 
	            int eYear = endCal.get(Calendar.YEAR); 
	            int eMonth = endCal.get(Calendar.MONTH); 
	            int eDay = endCal.get(Calendar.DATE); 

	            monthday = ((eYear - sYear) * 12 + (eMonth - sMonth)); 

	            if (sDay < eDay) {//此如果各位用的话，请把12月的那个月测一下，我原来记得这个if是有问题的，后来怎么都测不出来，现在用时加不加似乎都没问题，最好加上 
	                monthday = monthday + 1; 
	            } 
	            monthday=Math.abs(monthday);//如果月分为负数，则转为正数 
	            if(monthday>12){
	            	falg=false;
	            }
	        } catch (Exception e) { 
	            monthday = 0; 
	        } 
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return falg;
	}
	/***********
	 * 
	 * 判断经销商和产地是否有复核开票的单据
	 */
	
	public  Boolean countDealerKp(long dealerId,String yiyle,TtAsDealerTypePO po){
			Boolean flag=false;
			
		try {
			StringBuffer sql= new StringBuffer();
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			sql.append("select count(1) cou from Tt_As_Wr_Application wa where wa.account_date>=to_date('"+format.format(po.getBalanceReviewDate())+" 00:00:00','YYYY-MM-DD HH24:MI:SS')\n" );
			
				sql.append("and wa.account_date<=to_date('"+format.format(po.getBalanceDate())+" 23:59:59','YYYY-MM-DD HH24:MI:SS')\n" );
			
			sql.append("and wa.status=10791007 and wa.is_invoice=0\n" );
			sql.append("and wa.dealer_id="+dealerId+" and wa.yieldly="+yiyle+"");
			StringBuffer sql2= new StringBuffer();
			sql2.append("select count(1) cou from Tt_As_Wr_Spefee f where f.dealer_id="+dealerId+" and f.yield="+yiyle+" and f.balance_audit_date>=to_date('"+format.format(po.getBalanceReviewDate())+" 00:00:00','YYYY-MM-DD HH24:MI:SS')\n" );
			sql2.append("and f.balance_audit_date<=to_date('"+format.format(po.getBalanceDate())+"23:59:59','YYYY-MM-DD HH24:MI:SS')\n" );
			sql2.append("and f.status=11841006 and  f.claimbalance_id is null ");
			
			if(getFalg(po)){
				List<Map<String, Object>> list =  this.pageQuery(sql.toString(), null, this.getFunName());
				if(Integer.valueOf(list.get(0).get("COU").toString())>0){
					flag=true;
				}else{
					List<Map<String, Object>> list2 =  this.pageQuery(sql2.toString(), null, this.getFunName());
					if( Integer.valueOf(list2.get(0).get("COU").toString())>0){
						flag=true;
					}
				}	
			}
			
			
		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return flag;
		
	}
	//生成开票单逻辑
	public void autoBalanceView(TtAsDealerTypePO tpo,String yieldly,String dealerId ){
		
		try {
			/***********Iverson add By 2010-11-17******************/
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			String endBalanceDate = format.format(tpo.getBalanceReviewDate());//结算起始时间
			String conEndDay = format.format(tpo.getBalanceDate());//结算终止时间
			/***********Iverson add By 2010-11-17******************/
			TmDealerPO dpo = new TmDealerPO();
			dpo.setDealerId(Long.valueOf(dealerId));
			List<TmDealerPO> listDpo = this.select(dpo);
			Long companyId = listDpo.get(0).getOemCompanyId();//用户所属公司
			DealerKpDao balanceDao = DealerKpDao.getInstance();
			
			String startTime = "";
			String endTime = "";
			TmDealerPO dealerPO = new TmDealerPO();
			List<Map<String,Object>> feeList = new ArrayList<Map<String,Object>>();
			TcUserPO userPO = new TcUserPO();
			TmRegionPO regionPO = new TmRegionPO();
			List<Map<String,Object>> balanceDetail = new ArrayList<Map<String,Object>>();
			TmDealerPO invoiceMaker = new TmDealerPO();
			Map<String,Object> feeMap = new HashMap<String, Object>();
			String isReturn = "true";//控制是否可以生成结算单 true : 可以：; false ：不可以
			
			/***********Iverson add By 2010-11-17******************/
			if(Utility.testString(endBalanceDate))
				startTime = endBalanceDate + " 00:00:00";
			
			if(Utility.testString(conEndDay))
				endTime = conEndDay + " 23:59:59";
			/***********Iverson add By 2010-11-17******************/
			
			//取得当前系统时间
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String today = sdf.format(new Date());
			
			//if(partList==null || partList.size()<1){//存在需要回运但未回运的配件
				//查询经销商信息
				ClaimAuditingDao auditingDao = ClaimAuditingDao.getInstance();
				List<PO> dealerList = auditingDao.queryDealerById(Long.parseLong(dealerId));
				
				if(dealerList!=null && dealerList.size()>0)
					dealerPO = (TmDealerPO)dealerList.get(0);
				//根据经销商信息查询开票单位
				invoiceMaker = balanceDao.queryInvoiceMaker(Long.parseLong(dealerId)); //开票经销商
				TmDealerPO balanceMaker = balanceDao.queryBalanceMaker(Long.parseLong(dealerId)); //结算经销商
				
				//加载保养费用中固定的工时费用所占额度
				Double fixedAmount = balanceDao.queryFreeClaimFixedAmount(Constant.FREE_PARA_TYPE);
				
				//查询相关费用信息
//				feeList = balanceDao.queryBalanceStatis(Long.parseLong(dealerId), yieldly,
//						startTime, endTime, companyId,fixedAmount);
				if(feeList!=null && feeList.size()>0)
//					feeMap = feeList.get(0);
	
				//取得登陆用户信息
				//ClaimManualAuditingDao maDao = new ClaimManualAuditingDao();
				//userPO = maDao.queryUserById(logonUser.getUserId());
				
				//查询省份信息
//				String regionCode = "";
//				if(dealerPO!=null && dealerPO.getProvinceId()!=null)
//					regionCode = dealerPO.getProvinceId().toString();
//				regionPO = balanceDao.queryProvince(regionCode);
				
				//按车系汇总结算信息
				balanceDetail = balanceDao.queryBalanceGroupSeries(Long.parseLong(dealerId), yieldly, 
						startTime, endTime, companyId,fixedAmount);
				
				//市场公单费用
				/******add by liuxh 20101116 修改市场公单费用*********/
				Map<String,Object> specialFeeMap=new HashMap<String,Object>();
				Map<String,Object> specialMarketFeeMap = balanceDao.queryMarketFeeToBalanceOrder2(Long.parseLong(dealerId),endBalanceDate,conEndDay,yieldly);
				//特殊公单费用
				Map<String,Object> specialOutFeeMap = balanceDao.querySpecialFeeToBalanceOrder2(Long.parseLong(dealerId),endBalanceDate,conEndDay,yieldly);
				specialFeeMap.put("MARKETFEE", specialMarketFeeMap.get("MARKETFEE"));
				specialFeeMap.put("OUTFEE", specialOutFeeMap.get("OUTFEE"));
				/******add by liuxh 20101116 修改市场公单费用*********/
				//将特殊费用加入到总费用中
				/******mod by liuxh 20101213 无索赔单也可做结算单******/
				//if(feeMap!=null && feeMap.containsKey("BALANCE_AMOUNT")){
					//Object marketFee = CommonUtils.getDataFromMap(specialMarketFeeMap, "MARKETFEE");
					//Object outFee = CommonUtils.getDataFromMap(specialOutFeeMap, "OUTFEE");
					//Object totalFee = CommonUtils.getDataFromMap(feeMap, "BALANCE_AMOUNT");
					Object marketFee = CommonUtils.getDataFromMap(specialMarketFeeMap, "MARKETFEE");
					marketFee=marketFee.toString().equals("")?"0":marketFee;
					Object outFee = CommonUtils.getDataFromMap(specialOutFeeMap, "OUTFEE").toString();
					outFee=outFee.toString().equals("")?"0":outFee;
					Object totalFee = CommonUtils.getDataFromMap(feeMap, "BALANCE_AMOUNT").toString();
					totalFee=totalFee.toString().equals("")?"0":totalFee;
					Double totalFeeInt = 0d;
					BigDecimal bd = new BigDecimal("0");
					if(!"".equals(totalFee) && totalFee!=null){
						bd = bd.add(new BigDecimal(totalFee.toString()));
					}
					if(!"".equals(marketFee) && marketFee!=null){
						bd = bd.add(new BigDecimal(marketFee.toString()));
					}
					if(!"".equals(outFee) && outFee!=null){
						bd = bd.add(new BigDecimal(outFee.toString()));
					}
					totalFeeInt = bd.doubleValue();
					feeMap.put("BALANCE_AMOUNT", totalFeeInt);
			
			
			
			
			dealerId = String.valueOf(dealerPO.getDealerId());//request.getParamValue("DEALER_ID");//经销商代码
			String startDate = endBalanceDate;//request.getParamValue("START_DATE");//开始时间
			String endDate = conEndDay;//request.getParamValue("END_DATE");//结束日期
			if(Utility.testString(startDate))
				startDate = startDate + " 00:00:00";
			if(Utility.testString(endDate))
				endDate = endDate + " 23:59:59";
			//DealerKpDao balanceDao = DealerKpDao.getInstance();
			String SUCCESS="";
			/*********Iverson add 2010-12-01 根据经销商ID和基地查询吃此经销商是否被停止结算*********/
			String statusStr=balanceDao.getDealerStatus(Long.valueOf(dealerId), Long.valueOf(yieldly));
			if(statusStr.equals(Constant.IF_TYPE_YES.toString())){
				SUCCESS="NO";
			}
			TtAsWrClaimBalanceTmpPO balancePO = new TtAsWrClaimBalanceTmpPO();
			String balanceNO = SequenceManager.getSequence("BO");//;request.getParamValue("BALANACE_NO");//结算单号
			String dealerCode = dealerPO.getDealerCode();//request.getParamValue("DEALER_CODE");//经销商代码
			String dealerName = dealerPO.getDealerName();//request.getParamValue("DEALER_NAME");//经销商名称
			//String status = Constant.ACC_STATUS_06;//结算单待收单
			String status = Constant.ACC_STATUS_04;//结算单未上报
			String invoiceMakerStr = invoiceMaker.getDealerName()==null?"":invoiceMaker.getDealerName();//request.getParamValue("INVOICE_MAKER");//开票单位
			String labourAmount = String.valueOf(CommonUtils.getDataFromMap(feeMap,"LABOUR_AMOUNT"));//request.getParamValue("LABOUR_AMOUNT");//工时金额
			String partAmount = String.valueOf(CommonUtils.getDataFromMap(feeMap,"PART_AMOUNT"));//request.getParamValue("PART_AMOUNT");//配件金额
			String otherAmount = String.valueOf(CommonUtils.getDataFromMap(feeMap,"NETITEM_AMOUNT"));//request.getParamValue("OTHER_AMOUNT");//其他费用金额
			String freeAmount = String.valueOf(CommonUtils.getDataFromMap(feeMap,"FREE_M_PRICE"));//request.getParamValue("FREE_AMOUNT");//免费保养金额
			String serviceAmount = String.valueOf(CommonUtils.getDataFromMap(feeMap,"CAMPAIGN_FEE"));//request.getParamValue("SERVICE_AMOUNT");//服务活动金额
			String serviceLabourAmount = String.valueOf(CommonUtils.getDataFromMap(feeMap,"SERVICE_LABOUR_AMOUNT"));//request.getParamValue("SERVICE_LABOUR_AMOUNT");//服务活动工时金额
			String servicePartAmount = String.valueOf(CommonUtils.getDataFromMap(feeMap,"SERVICE_PART_AMOUNT"));//request.getParamValue("SERVICE_PART_AMOUNT");//服务活动配件金额
			String serviceOtherAmount = String.valueOf(CommonUtils.getDataFromMap(feeMap,"SERVICE_NETITEM_AMOUNT"));//request.getParamValue("SERVICE_OTHER_AMOUNT");//服务活动其他金额
			String serviceTotalAmount = String.valueOf(CommonUtils.getDataFromMap(feeMap,"SERVICE_TOTAL_AMOUNT"));//request.getParamValue("SERVICE_TOTAL_AMOUNT");//服务活动总金额
			String appendAmount = String.valueOf(CommonUtils.getDataFromMap(feeMap,"APPEND_AMOUNT"));//request.getParamValue("APPEND_AMOUNT");//追加费用（特殊费用）
			String appendLabourAmount = String.valueOf(CommonUtils.getDataFromMap(feeMap,"APPENDLABOUR_AMOUNT"));//request.getParamValue("APPENDLABOUR_AMOUNT");//追加工时费用（特殊费用）
			//String returnAmount = request.getParamValue("RETURN_AMOUNT");//运费  现无运费
			String claimCount = String.valueOf(CommonUtils.getDataFromMap(feeMap,"CLAIMCOUNT").toString().equals("")?"0":CommonUtils.getDataFromMap(feeMap,"CLAIMCOUNT"));//request.getParamValue("CLAIM_COUNT");//索赔单数
			//String totalAmount = request.getParamValue("TOTAL_AMOUNT");//总金额
			String remark = "";//request.getParamValue("REMARK");//备注信息
			String stationerTel = viewClaimTel(dealerId,Constant.STATION_HEAD.toString());//request.getParamValue("STATIONER_TEL");//站长电话
			String claimerTel = viewClaimTel(dealerId,Constant.AEGIS.toString());//request.getParamValue("CLAIMER_TEL");//索赔员电话
			String province = CommonUtils.checkNull(regionPO.getRegionCode());//request.getParamValue("PROVINCE");//省份ID
			String balanceAmount = String.valueOf(CommonUtils.getDataFromMap(feeMap,"BALANCE_AMOUNT").toString().equals("")?"0":CommonUtils.getDataFromMap(feeMap,"BALANCE_AMOUNT"));//request.getParamValue("BALANCE_AMOUNT");//结算金额
			String freeLabourAmount = String.valueOf(CommonUtils.getDataFromMap(feeMap,"FREE_LABOUR_AMOUNT"));//request.getParamValue("FREE_LABOUR_AMOUNT");//保养对应工时费用
			String freePartAmount = String.valueOf(CommonUtils.getDataFromMap(feeMap,"FREE_PART_AMOUNT"));//request.getParamValue("FREE_PART_AMOUNT");//保养对应配件费用
			
			balancePO.setBalanceNo(CommonUtils.checkNull(balanceNO));
			balancePO.setDealerCode(CommonUtils.checkNull(dealerCode));
			balancePO.setDealerName(CommonUtils.checkNull(dealerName));
			balancePO.setStatus(CommonUtils.parseInteger(status));
			balancePO.setInvoiceMaker(CommonUtils.checkNull(invoiceMakerStr));
			balancePO.setLabourAmount(CommonUtils.parseDouble(checkNull(labourAmount,"0")));
			balancePO.setPartAmount(CommonUtils.parseDouble(checkNull(partAmount,"0")));
			balancePO.setOtherAmount(CommonUtils.parseDouble(checkNull(otherAmount,"0")));
			balancePO.setFreeAmount(CommonUtils.parseDouble(checkNull(freeAmount,"0")));
			balancePO.setServiceFixedAmount(CommonUtils.parseDouble(checkNull(serviceAmount,"0")));
			balancePO.setServiceLabourAmount(CommonUtils.parseDouble(checkNull(serviceLabourAmount,"0")));
			balancePO.setServicePartAmount(CommonUtils.parseDouble(checkNull(servicePartAmount,"0")));
			balancePO.setServiceOtherAmount(CommonUtils.parseDouble(checkNull(serviceOtherAmount,"0")));
//			if(returnAmount!=null)
//				balancePO.setReturnAmount(CommonUtils.parseDouble(checkNull(returnAmount,"0")));
			balancePO.setClaimCount(CommonUtils.parseLong(checkNull(claimCount,"0")));
			balancePO.setAmountSum(CommonUtils.parseDouble(checkNull(balanceAmount,"0")));
			balancePO.setRemark(CommonUtils.checkNull(remark));
			balancePO.setStationerTel(CommonUtils.checkNull(stationerTel));
			balancePO.setClaimerTel(CommonUtils.checkNull(claimerTel));

			if(!Utility.testString(province))
				province = "-1";
			balancePO.setProvince(CommonUtils.parseLong(checkNull(province,"-1")));
			balancePO.setBalanceAmount(CommonUtils.parseDouble(checkNull(balanceAmount,"0")));
			balancePO.setAppendAmount(CommonUtils.parseDouble(checkNull(appendAmount,"0")));
			balancePO.setAppendLabourAmount(CommonUtils.parseDouble(checkNull(appendLabourAmount,"0")));
			balancePO.setServiceTotalAmount(CommonUtils.parseDouble(checkNull(serviceTotalAmount,"0")));
			balancePO.setFreeLabourAmount(CommonUtils.parseDouble(checkNull(freeLabourAmount,"0")));
			balancePO.setFreePartAmount(CommonUtils.parseDouble(checkNull(freePartAmount,"0")));
			
			Long topDealerId = -1L;
			Integer dealerLevel = Constant.DEALER_LEVEL_02;
			//11、查询对应经销商信息
			TmDealerPO conditionPO = new TmDealerPO();
			conditionPO.setDealerId(Long.parseLong(dealerId));
			List<TmDealerPO> dealerList2 = balanceDao.select(conditionPO);
			if(dealerList2!=null && dealerList2.size()>0){
				TmDealerPO tempPO = dealerList2.get(0);
				if(tempPO.getDealerLevel()!=null)
					dealerLevel = tempPO.getDealerLevel();
			}
					
			//12、查询上级经销商信息
			TmDealerPO parentDealerPO = balanceDao.queryInvoiceMaker(Long.parseLong(dealerId));
			if(parentDealerPO!=null && parentDealerPO.getDealerId()!=null){
				topDealerId = parentDealerPO.getDealerId();
			}
			//根据经销商信息查询开票单位
			TmDealerPO invoiceMaker2 = balanceDao.queryInvoiceMaker(Long.parseLong(dealerId)); //开票经销商
			TmDealerPO balanceMaker2 = balanceDao.queryBalanceMaker(Long.parseLong(dealerId)); //结算经销商
			
			Long balanceId = CommonUtils.parseLong(SequenceManager.getSequence(""));//结算单ID
			balancePO.setId(balanceId);
			balancePO.setOemCompanyId(companyId);
			balancePO.setCreateDate(new Date());
			balancePO.setDealerLevel(dealerLevel);
			/*******mod by liuxh 20101115 修改结算和开票经销商*******/
			balancePO.setTopDealerId(balanceMaker2.getDealerId());  //结算经销商
			balancePO.setKpDealerId(invoiceMaker2.getDealerId());   //开票经销商
			/*******mod by liuxh 20101115 修改结算和开票经销商*******/
			
			balancePO.setYieldly(CommonUtils.parseLong(checkNull(yieldly,"-1")));
			balancePO.setDealerId(CommonUtils.parseLong(dealerId));
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			balancePO.setStartDate(formatter.parse(startDate));
			balancePO.setEndDate(formatter.parse(endDate));
			factory.insert(balancePO);
			
			/******运费******/
			StringBuffer sbSqlRet=new StringBuffer();
			sbSqlRet.append("UPDATE TT_AS_WR_CLAIM_BALANCE_TMP SET (RETURN_AMOUNT_BAK,RETURN_AMOUNT)= \n" );
			sbSqlRet.append("(SELECT SUM(NVL(OLD_PRICE,0)),SUM(NVL(NEW_PRICE,0)) FROM TT_AS_WR_AUTH_PRICE WHERE \n");
			sbSqlRet.append("      YIELYLD=? \n");
			sbSqlRet.append("      AND DEALER_ID=? \n");
			sbSqlRet.append("      AND STATUS=?) \n");
			sbSqlRet.append("WHERE ID=? \n");
			List listParRet=new ArrayList();
			listParRet.add(balancePO.getYieldly().intValue());
			listParRet.add(balancePO.getDealerId());
			listParRet.add(Constant.OLD_PRICE_1);//未开票
			listParRet.add(balancePO.getId());
			factory.update(sbSqlRet.toString(), listParRet);
			/******运费******/
			
			//System.out.println("11:"+balanceDao.testClaimBalance(balanceId));
			
			//2、统计满足条件索赔单信息
			balanceDao.saveRelaBetweenClaimAndBalance(CommonUtils.parseLong(dealerId), yieldly, startDate,
					endDate, companyId, 222222222222l, balanceId.toString());
			//21、更改索赔单状态(将统计过的索赔单状态修改为"结算审核中")    ???????????????????????????????????????????????????????????
			//balanceDao.updateClaimStatus(balanceId,Constant.CLAIM_APPLY_ORD_TYPE_08);
			
			//加载保养费用中固定的工时费用
			Double fixedAmount2 = balanceDao.queryFreeClaimFixedAmount(Constant.FREE_PARA_TYPE);
				
			//3、生成结算单明细
			balanceDao.saveBalanceDetail(balanceId,222222222222l,fixedAmount2);
			
			//31、将备注信息更新到结算单明细中
			//this.modifyBalanceDetail(request, balanceId, balanceDao);
			//5、将特殊费用统计对应结算单中，同时标识对应特殊费用合计到该结算单
			/*******mod by liuxh 20101116 增加开始时间和结束时间两参数*********/
			this.addSpecialFeeToBalanceOrder(Long.parseLong(dealerId),balanceId,startDate,endDate,yieldly);
			
			//6、重新结算结算单中数据
			balanceDao.reCheckBalanceAmount(balanceId,fixedAmount2);
			
			//5、调用结算单自动审核 （ 修改 20100909 结算单生成时 先不做自动审核  当收单确认后 再进行审核）
			//this.autoAuditing(balanceId);
			/****add by liu 20101129 经销商增加结算单时保存申请金额备份字段****/
			//balanceDao.updateBalanceBak(balanceId);
			
			//System.out.println("66:"+balanceDao.testClaimBalance(balanceId));
			
			/****add by liu 20101129 经销商增加结算单时保存申请金额备份字段****/
			//balanceDao.balancrApplyAmount(balanceId);//重新统计APPLY_AMOUNT
			
			/****add by xiongc 2011--8-11 经销商增加结算单时保存申请金额备份字段****/
			
			balanceDao.updateBalanceetailBakAmount(balanceId);
			
			balanceDao.updateBalanceBakAmount(balanceId);
			//System.out.println("77:"+balanceDao.testClaimBalance(balanceId));
			
			//7、记录审核结果
//			BalanceStatusRecord.recordStatus(balanceId, null, logonUser.getName(), 
//					logonUser.getOrgId(), BalanceStatusRecord.STATUS_01);
			/******************************/
			settComplete(balanceId.toString());//点击"完成"动作
			//System.out.println("88:"+balanceDao.testClaimBalance(balanceId));
			/******************************/
			
			/******复核申请环节******/
			//统计抵扣金额
			DealerKpDao dao = new DealerKpDao();
			Map<String, Object> deductAmountMap = balanceDao.getBalanceMainMap(String.valueOf(balanceId));
			
			String jiujianCount = CommonUtils.getDataFromMap(deductAmountMap, "TOTAL_AMOUNT").toString();//旧件扣款
			String kaoheCount="";
			TcCodePO code = new TcCodePO() ;
			code.setType("8008") ;
			List listCode = dao.select(code) ;
			if(listCode.size()>0){
				code = (TcCodePO)listCode.get(0);
			
				//轿车添加配件是不是监控判断
				if(Constant.chana_jc==Integer.parseInt(code.getCodeId())){
					 kaoheCount = CommonUtils.getDataFromMap(deductAmountMap, "FINE_SUM").toString();//考核扣款
				 }
				else{
					
					kaoheCount = dao.viewFineDecude(dealerId, yieldly);
				}
			}
			
			
			String xingzhengCount = CommonUtils.getDataFromMap(deductAmountMap, "DAMOUNT").toString();//行政扣款
			jiujianCount = Utility.testString(jiujianCount)?jiujianCount:"0";
			kaoheCount = Utility.testString(kaoheCount)?kaoheCount:"0";
			xingzhengCount = Utility.testString(xingzhengCount)?xingzhengCount:"0";
			
			TtAsWrClaimBalanceTmpPO po = new TtAsWrClaimBalanceTmpPO();
			TtAsWrClaimBalanceTmpPO tp = new TtAsWrClaimBalanceTmpPO();
			po.setId(balanceId);
			//tp.setReturnAmount(Double.parseDouble(returnAmount));
			tp.setOldDeduct(Double.parseDouble(jiujianCount));
			tp.setCheckDeduct(Double.parseDouble(kaoheCount));
			tp.setAdminDeduct(Double.parseDouble(xingzhengCount));
			//tp.setFreeDeduct(Double.parseDouble(repairAmount));
			//tp.setServiceDeduct(Double.parseDouble(activityAmount));
			//tp.setMarketAmount(authMarketFee);
			//tp.setSpeoutfeeAmount(authOuterFee);
			//tp.setRemark(remark);
			//tp.setVar(Integer.parseInt(var)+1);
			//tp.setStatus(Integer.parseInt(Constant.ACC_STATUS_03));
			//tp.setAmountSum(Double.parseDouble(feiyongCount)+chanageFee);//加入特殊费用变动
			//tp.setBalanceAmount(Double.parseDouble(zongjiCount)+chanageFee);//加入特殊费用变动
			/********Ivesosn add by 2011-01-20****************/
			tp.setReviewApplicationTime(new Date());//同时更新结算单复合申请时间
			/********Ivesosn add by 2011-01-20****************/
			
			balanceDao.update(po, tp);//更新TT_AS_WR_CLAIM_BALANCE
			
			/**********add by liuxh 20101127 复核申请完成更新 BALANCE_AMOUNT************/
			// BALANCE_AMOUNT减 保养费扣款 -服务活动单扣款-考核扣款-旧件扣款-行政抵扣-财务扣款
			StringBuffer sbSql=new StringBuffer();
//			sbSql.append("UPDATE TT_AS_WR_CLAIM_BALANCE SET BALANCE_AMOUNT=");
//			sbSql.append("(BALANCE_AMOUNT-(NVL(FREE_DEDUCT,0)+NVL(SERVICE_DEDUCT,0)+NVL(CHECK_DEDUCT,0)+NVL(OLD_DEDUCT,0)+NVL(ADMIN_DEDUCT,0)+NVL(FINANCIAL_DEDUCT,0))) ");
//			sbSql.append("WHERE ID=?");
			/*******mod by liuxh 201011201 复核申请结算金额重新计算再   -服务活动单扣款-考核扣款-旧件扣款-行政抵扣-财务扣款 ********/
			if(Constant.chana_jc==Integer.parseInt(code.getCodeId())){
				sbSql.append("UPDATE TT_AS_WR_CLAIM_BALANCE_TMP SET BALANCE_AMOUNT=((NVL(LABOUR_AMOUNT,0)+NVL(PART_AMOUNT,0)+NVL(OTHER_AMOUNT,0)+NVL(FREE_AMOUNT,0)");
				sbSql.append("+NVL(RETURN_AMOUNT,0)+NVL(MARKET_AMOUNT,0)+NVL(SPEOUTFEE_AMOUNT,0)+NVL(APPEND_AMOUNT,0)+NVL(SERVICE_TOTAL_AMOUNT,0))");
				sbSql.append("-(NVL(FREE_DEDUCT,0)+NVL(SERVICE_DEDUCT,0)+NVL(OLD_DEDUCT,0)+NVL(ADMIN_DEDUCT,0)+NVL(CHECK_DEDUCT,0)+NVL(FINANCIAL_DEDUCT,0)))\n");
				sbSql.append("WHERE ID=?");
			}else{
				sbSql.append("UPDATE TT_AS_WR_CLAIM_BALANCE_TMP SET BALANCE_AMOUNT=((NVL(LABOUR_AMOUNT,0)+NVL(PART_AMOUNT,0)+NVL(OTHER_AMOUNT,0)+NVL(FREE_AMOUNT,0)");
				sbSql.append("+NVL(RETURN_AMOUNT,0)+NVL(MARKET_AMOUNT,0)+NVL(SPEOUTFEE_AMOUNT,0)+NVL(APPEND_AMOUNT,0)+NVL(CHECK_DEDUCT,0)+NVL(SERVICE_TOTAL_AMOUNT,0))");
				sbSql.append("-(NVL(FREE_DEDUCT,0)+NVL(SERVICE_DEDUCT,0)+NVL(OLD_DEDUCT,0)+NVL(ADMIN_DEDUCT,0)+NVL(FINANCIAL_DEDUCT,0)))\n");
				sbSql.append("WHERE ID=?");
			}
			List parList=new ArrayList();
			parList.add(balanceId);
			balanceDao.update(sbSql.toString(), parList);
			
			/******更新开票金额*******/
			String sql="UPDATE TT_AS_WR_CLAIM_BALANCE_TMP SET NOTE_AMOUNT=BALANCE_AMOUNT WHERE ID=?";
			List listParNote=new ArrayList();
			listParNote.add(balanceId);
			balanceDao.update(sql, listParNote);

			/******更新开票金额*******/
			
			/**********add by liuxh 20101127 复核申请完成更新 BALANCE_AMOUNT************/
			
			
			
			/******复核申请环节******/
			
			
			Map<String, Object> map = balanceDao.getBalanceMainMapView(balanceId.toString());//结算单主表信息
			List<Map<String, Object>> list = balanceDao.getBalanceMainList(balanceId.toString());//结算单子表信息
			
			/******
			 * 如果map.get("BALANCE_AMOUNT").toString()<0那么就转行政扣款
			 */
			if(CommonUtils.parseDouble(checkNull(map.get("BALANCE_AMOUNT").toString(),"0"))>=0){
				doBalanceKp(Long.valueOf(map.get("ID").toString()));
			}else{
				TtAsWrClaimBalanceTmpPO cpo = new TtAsWrClaimBalanceTmpPO();
				cpo.setId(Long.valueOf(balanceId.toString()));
				TtAsWrClaimBalanceTmpPO cp1 = (TtAsWrClaimBalanceTmpPO)this.select(cpo).get(0);
				deductBalanceAudit(cp1);
			}
			
		
			
			
		} catch (Exception e) {
			logger.error(null, e);
			e.printStackTrace();
		}	
	}
	private String checkNull(Object obj,String repalce){
		String result = "";
		if(obj==null || "".equals(obj.toString()) || "null".equalsIgnoreCase(obj.toString())){
			result = repalce;
		}else{
			result = obj.toString();
		}
		return result;
	}
	
	/**
	 * 将特殊费用统计对应结算单中，同时标识对应特殊费用合计到该结算单
	 * @param dealerId 经销商ID
	 * @param balanceId 结算单ID
	 */
private void addSpecialFeeToBalanceOrder(Long dealerId,Long balanceId,String startDate,String endDate,String yieldly){
		
		if(dealerId==null || balanceId==null)
			return;
		DealerKpDao balanceDao = DealerKpDao.getInstance();
		//1、根据经销商将其他对应非费用标识到该结算单中
		/********mod by liuxh 20101116 增加时间段参数*******/
		balanceDao.markSpecialFeeToBalanceOrder(dealerId, balanceId,startDate,endDate,yieldly);
		//2、根据标识的状态和特殊费用的状态统计特殊费用
		List<Map<String,Object>> feeList = balanceDao.getSpecialFeeByBalanceId(balanceId);
		double marketFee = 0;//市场工单费用
		double outFee = 0;   //特殊外出费用
		double marketFeeBak= 0;
		double outFeeBak = 0;
		int feeCount = 0;
		if(feeList!=null && feeList.size()>0){
			for (Map<String, Object> feeMap : feeList) {
				if(feeMap.containsKey("FEE_TYPE")){
					if((Constant.FEE_TYPE_01).equals(feeMap.get("FEE_TYPE").toString())){
						if(feeMap.containsKey("DECLARE_SUM")){
							marketFee = ((BigDecimal)feeMap.get("DECLARE_SUM")).doubleValue();
							marketFeeBak = ((BigDecimal)feeMap.get("DECLARE_SUM1")).doubleValue();
						}
						if(feeMap.containsKey("FEETYPECOUNT")){
							feeCount = feeCount + ((BigDecimal)feeMap.get("FEETYPECOUNT")).intValue();
						}
					}else if(Constant.FEE_TYPE_02.equals(feeMap.get("FEE_TYPE").toString())){
						if(feeMap.containsKey("DECLARE_SUM")){
							outFee = ((BigDecimal)feeMap.get("DECLARE_SUM")).doubleValue();
							outFeeBak = ((BigDecimal)feeMap.get("DECLARE_SUM")).doubleValue();
						}
						if(feeMap.containsKey("FEETYPECOUNT")){
							feeCount = feeCount + ((BigDecimal)feeMap.get("FEETYPECOUNT")).intValue();
						}
					}
				}
			}
		}else{
			feeCount = 0;
		}
		
		//3、将特殊费用合计到结算单中
		balanceDao.addSpecialFeeToBalanceOrder2(balanceId, marketFee, outFee,marketFeeBak,outFeeBak, feeCount);
	}
/********add by liuxh 20101126 增加结算室审核完成功能  调用更新结算标志和重新计算**********/
public void settComplete(String blanceId){
	ActionContext act = ActionContext.getContext();
	try
	{	
		RequestWrapper request = act.getRequest();
		//String blanceId=CommonUtils.checkNull(request.getParamValue("blanceId"));
		BalanceAuditing bAuditing = new BalanceAuditing("","");
		
		DealerKpDao balanceDao = DealerKpDao.getInstance();
		
//		/*********判断索赔单是否全部审核完成**********/
//		boolean flagClaim=balanceDao.queryClaimByBanlanceId(Long.parseLong(blanceId));
//		boolean flagSp=balanceDao.querySpecByBanlanceId(Long.parseLong(blanceId));
//		if(!flagClaim){
//			throw new BizException("结算单下的索赔单未完全审核完成!");
//		}
//		if(!flagSp){
//			throw new BizException("结算单下的特殊费用工单未完全审核完成!");
//		}
		/*********判断索赔单是否全部审核完成**********/
		
		
		//2、根据标识的状态和特殊费用的状态统计特殊费用
		List<Map<String,Object>> feeList = balanceDao.getSpecialFeeByBalanceIdStatus(Long.parseLong(blanceId));
		double marketFee = 0;//市场工单费用
		double outFee = 0;   //特殊外出费用
		int feeCount = 0;
		if(feeList!=null && feeList.size()>0){
			for (Map<String, Object> feeMap : feeList) {
				if(feeMap.containsKey("FEE_TYPE")){
					if((Constant.FEE_TYPE_01).equals(feeMap.get("FEE_TYPE").toString())){
						if(feeMap.containsKey("DECLARE_SUM")){
							marketFee = ((BigDecimal)feeMap.get("DECLARE_SUM")).doubleValue();
						}
						if(feeMap.containsKey("FEETYPECOUNT")){
							feeCount = feeCount + ((BigDecimal)feeMap.get("FEETYPECOUNT")).intValue();
						}
					}else if(Constant.FEE_TYPE_02.equals(feeMap.get("FEE_TYPE").toString())){
						if(feeMap.containsKey("DECLARE_SUM")){
							outFee = ((BigDecimal)feeMap.get("DECLARE_SUM")).doubleValue();
						}
						if(feeMap.containsKey("FEETYPECOUNT")){
							feeCount = feeCount + ((BigDecimal)feeMap.get("FEETYPECOUNT")).intValue();
						}
					}
				}
			}
		}else{
			feeCount = 0;
		}
		
		//3、将特殊费用合计到结算单中
		balanceDao.addSpecialFeeToBalanceOrder(Long.parseLong(blanceId), marketFee, outFee, feeCount);
		balanceDao.updateMarkSpeeActiveFee(Long.parseLong(blanceId));//add by liuxh 20101227 完成时记录市场公单和外出公单费用
		
		//bAuditing.modifyBalanceStatus(Long.parseLong(blanceId), true,logonUser);
		//DealerBalanceDao balanceDao = DealerBalanceDao.getInstance();
		//加载保养费用中固定的工时费用
		Double fixedAmount = balanceDao.queryFreeClaimFixedAmount(Constant.FREE_PARA_TYPE);
		//更新结算主表信息
		balanceDao.reCheckBalanceAmount(new Long(blanceId),fixedAmount);
		//更新结算明细表信息
		balanceDao.reCheckBalanceDetail(new Long(blanceId),fixedAmount);
		
	}
	catch(Exception e){
		e.printStackTrace();
		logger.error(e);
	}
	
	
}
public void doBalanceKp(long balanceId){
	try{
	DealerKpDao balanceDao = DealerKpDao.getInstance();

	StringBuffer sbSql=new StringBuffer();
	sbSql.append("INSERT INTO TT_AS_WR_CLAIM_BALANCE  \n" );
	sbSql.append("   SELECT   * \n");
	sbSql.append("     FROM   TT_AS_WR_CLAIM_BALANCE_TMP \n");
	sbSql.append("    WHERE   ID = ? \n");
	List listPar=new ArrayList();
	listPar.add(balanceId);
	balanceDao.update(sbSql.toString(), listPar); //插入结算主信息
	balanceDao.delete("DELETE FROM TT_AS_WR_CLAIM_BALANCE_TMP WHERE ID=?", listPar);
	TtAsWrClaimBalancePO po1 = new TtAsWrClaimBalancePO();
	po1.setId(balanceId);
	List<TtAsWrClaimBalancePO> list =  balanceDao.select(po1);
	List<Map<String, Object>> listBalance = balanceDao.matchBalanceId(String.valueOf(balanceId));
	if(listBalance.size()!=list.get(0).getClaimCount()){
	 throw new Exception("未知错误，请退出系统重新生成开票单！");
	}
	
	
	StringBuffer sbSqlTr=new StringBuffer();
	sbSqlTr.append("INSERT INTO TR_BALANCE_CLAIM  \n" );
	sbSqlTr.append("   SELECT   * \n");
	sbSqlTr.append("     FROM   TR_BALANCE_CLAIM_TMP \n");
	sbSqlTr.append("    WHERE   BALANCE_ID = ? \n");

	balanceDao.update(sbSqlTr.toString(), listPar); //插入结算与索赔单关系
	balanceDao.delete("DELETE FROM TR_BALANCE_CLAIM_TMP WHERE BALANCE_ID=?", listPar);
	
	
	StringBuffer sbSqlDetail=new StringBuffer();
	sbSqlDetail.append("INSERT INTO TT_AS_WR_CLAIM_BALANCE_DETAIL  \n" );
	sbSqlDetail.append("   SELECT   * \n");
	sbSqlDetail.append("     FROM   TT_CLAIM_BALANCE_DETAIL_TMP \n");
	sbSqlDetail.append("    WHERE   BALANCE_ID = ? \n");

	balanceDao.update(sbSqlDetail.toString(), listPar); //插入结算明细信息
	balanceDao.delete("DELETE FROM TT_CLAIM_BALANCE_DETAIL_TMP WHERE BALANCE_ID=?", listPar);
	
	
	StringBuffer sbSqlDetailBak=new StringBuffer();
	sbSqlDetailBak.append("INSERT INTO TT_AS_WR_BALANCE_DETAIL_BAK  \n" );
	sbSqlDetailBak.append("   SELECT   * \n");
	sbSqlDetailBak.append("     FROM   TT_BALANCE_DETAIL_BAK_TMP \n");
	sbSqlDetailBak.append("    WHERE   BALANCE_ID = ? \n");
	balanceDao.update(sbSqlDetailBak.toString(), listPar); //插入结算明细(备份)信息
	balanceDao.delete("DELETE FROM TT_BALANCE_DETAIL_BAK_TMP WHERE BALANCE_ID=?", listPar);
	
	
	StringBuffer sbSql2=new StringBuffer();
	sbSql2.append("UPDATE   TT_AS_WR_APPLICATION A \n" );
	sbSql2.append("   SET   IS_INVOICE = 1, \n");
	sbSql2.append("   CLAIM_BLANCE_ID=? \n");
	sbSql2.append(" WHERE   EXISTS (SELECT   * \n");
	sbSql2.append("                   FROM   TR_BALANCE_CLAIM B \n");
	sbSql2.append("                  WHERE   A.ID = B.CLAIM_ID AND B.BALANCE_ID = ?) \n");
	List listPar2=new ArrayList();
	listPar2.add(balanceId);
	listPar2.add(balanceId);
	balanceDao.update(sbSql2.toString(), listPar2);

	StringBuffer sbSqlFee=new StringBuffer();
	sbSqlFee.append("UPDATE   TT_AS_WR_SPEFEE \n" );
	sbSqlFee.append("   SET   CLAIMBALANCE_ID = CLAIMBALANCE_ID_TMP \n");
	sbSqlFee.append(" WHERE   CLAIMBALANCE_ID_TMP = ? \n");
	
	List listParFee=new ArrayList();
	listParFee.add(balanceId);
	balanceDao.update(sbSqlFee.toString(), listParFee);
	

	/********add 20101201 zhumy**********/
	TtAsWrClaimBalancePO po = new TtAsWrClaimBalancePO();
	po.setId(balanceId);
	TtAsWrClaimBalancePO sel=(TtAsWrClaimBalancePO)balanceDao.select(po).get(0);
	Date endDate=sel.getEndDate();
	TtAsDealerTypePO typeCon=new TtAsDealerTypePO();
	typeCon.setDealerId(sel.getDealerId());
	typeCon.setYieldly(sel.getYieldly().toString());
	TtAsDealerTypePO typeValue=new TtAsDealerTypePO();
	typeValue.setBalanceReviewDate(endDate);
	typeValue.setUpdateDate(new Date());
	balanceDao.update(typeCon, typeValue);
	
	/********add 20101201 zhumy**********/
	BalanceMainDao dao = new BalanceMainDao();
	TtAsWrClaimBalancePO balancePO = dao.getClaimBalancePOById(balanceId);
	Long yieldly = -1L;
	if(balancePO!=null && balancePO.getYieldly()!=null)
		yieldly = balancePO.getYieldly();
	
	
	String sqlUp="UPDATE tt_as_dealer_type SET BALANCE_DATE=?,BALANCE_REVIEW_DATE=? WHERE " +
			" dealer_id=? and yieldly=?";
	List listUp6=new ArrayList();
	listUp6.add(balancePO.getEndDate());
	listUp6.add(balancePO.getEndDate());
	listUp6.add(balancePO.getDealerId());
	listUp6.add(balancePO.getYieldly());
	factory.update(sqlUp, listUp6);
	
	
	/*****运费****/
	StringBuffer sbSqlRet=new StringBuffer();
	sbSqlRet.append("UPDATE TT_AS_WR_AUTH_PRICE SET STATUS=?,CLAIMBALANCE_ID=? WHERE \n");
	sbSqlRet.append("      YIELYLD=? \n");
    sbSqlRet.append("      AND DEALER_ID=? \n");
    sbSqlRet.append("      AND STATUS=? \n");
	List parRet=new ArrayList();
	parRet.add(Constant.OLD_PRICE_2);
	parRet.add(balanceId);
	parRet.add(balancePO.getYieldly());
	parRet.add(balancePO.getDealerId());
	parRet.add(Constant.OLD_PRICE_1);
    factory.update(sbSqlRet.toString(), parRet);
	/*****运费****/
	
	//将处理过的"特殊费用"标识为"已支付"
	TtAsWrSpefeePO aa = new TtAsWrSpefeePO();
	aa.setYield(yieldly);
	aa.setDealerId(balancePO.getDealerId());
	aa.setClaimbalanceId(balanceId);
	aa.setStatus(Integer.parseInt(Constant.SPEFEE_STATUS_06));//审核后的
	
	TtAsWrSpefeePO bb = new TtAsWrSpefeePO();
	bb.setStatus(Integer.parseInt(Constant.SPEFEE_STATUS_07));//锁定状态
	bb.setUpdateDate(new Date());
	dao.update(aa, bb);
	
	/*
	//将这个特殊费用的此次更改写入特殊费用审核明细表中
	TtAsWrSpefeePO pp = new TtAsWrSpefeePO();
	pp.setClaimbalanceId(Long.parseLong(id));
	List ppList = dao.select(pp);
	if(ppList.size()>0){
		for(int i=0;i<ppList.size();i++){
			pp = (TtAsWrSpefeePO)ppList.get(i) ;
			TtAsWrSpefeeAuditingPO auditPO = new TtAsWrSpefeeAuditingPO();
			auditPO.setAuditingDate(updateDate);
			auditPO.setAuditingPerson(logonUser.getName());
			auditPO.setCreateBy(logonUser.getUserId());
			auditPO.setCreateDate(updateDate);
			auditPO.setFeeId(pp.getId());
			auditPO.setId(Long.parseLong(SequenceManager.getSequence("")));
			auditPO.setStatus(Integer.parseInt(Constant.SPEFEE_STATUS_07));
			
			dao.insert(auditPO);
		}
	}*/
	
	//将处理过的抵扣单修改为"无效"
	TtAsWrDeductBalancePO tw = new TtAsWrDeductBalancePO();
	tw.setStatus(Constant.STATUS_ENABLE);
	tw.setDealerCode(balancePO.getDealerCode());
	tw.setYieldly(yieldly);
	
	TtAsWrDeductBalancePO td = new TtAsWrDeductBalancePO();
	td.setStatus(Constant.STATUS_DISABLE);
	td.setClaimbalanceId(balanceId);
	td.setUpdateDate(new Date());
	
	dao.update(tw, td);//更新TT_AS_WR_DEDUCT_BALANCE
	
	//将处理过的奖惩记录修改为"无效"
	TtAsWrFinePO ta = new TtAsWrFinePO();
	TtAsWrFinePO tf = new TtAsWrFinePO();
	ta.setDealerId(balancePO.getDealerId());
	ta.setYieldly(yieldly);
	ta.setPayStatus(Integer.parseInt(Constant.PAY_STATUS_01));
	tf.setPayStatus(Integer.parseInt(Constant.PAY_STATUS_02));
	tf.setClaimbalanceId(balanceId);
	tf.setUpdateDate(new Date());
	dao.update(ta, tf);//更新TT_AS_WR_FINE
	
	/*
	 * add by zuoxj 2010-12-18  14：35
	 * 每次复核申请，都必将上次行政扣款转为已结算
	 */
	TtAsWrAdminDeductPO tc1 = new TtAsWrAdminDeductPO();
	TtAsWrAdminDeductPO ts1 = new TtAsWrAdminDeductPO();
	tc1.setDealerId(balancePO.getDealerId());
	tc1.setYieldly(yieldly);
	tc1.setDeductStatus(Integer.parseInt(Constant.ADMIN_STATUS_01));
	ts1.setDeductStatus(Integer.parseInt(Constant.ADMIN_STATUS_02));
	ts1.setClaimbalanceId(balanceId);
	ts1.setUpdateDate(new Date());//更新TT_AS_WR_ADMIN_DEDUCT
	dao.update(tc1, ts1);
	
	/*if(Double.parseDouble(zongjiCount)<0){
		TtAsWrAdminDeductPO tc = new TtAsWrAdminDeductPO();
		tc.setId(Long.parseLong(SequenceManager.getSequence("")));
		tc.setDealerId(Long.parseLong(dealerId));
		TmDealerPO dpo = new TmDealerPO();
		dpo.setDealerId(Long.parseLong(dealerId));
		List dlist = dao.select(dpo);
		if(dlist.size()>0){
			dpo = (TmDealerPO)dlist.get(0);
			tc.setDealerCode(dpo.getDealerCode());
			tc.setDealerName(dpo.getDealerName());
		}
		tc.setYieldly(yieldly);
		 //将这次的 结算金额录为下次行政扣款金额
		tc.setDeductAmount(Double.parseDouble(zongjiCount)*(-1));
		tc.setDeductStatus(Integer.parseInt(Constant.ADMIN_STATUS_01));
		tc.setFromClaimbalanceId(Long.parseLong(id));
		tc.setOemCompanyId(Long.parseLong(logonUser.getOemCompanyId()));
		tc.setCreateBy(tp.getUpdateBy());
		tc.setCreateDate(updateDate);//更新TT_AS_WR_ADMIN_DEDUCT
		dao.insert(tc);
	}*/
	updateMarketStatus(balancePO.getDealerId());//更新TT_IF_MARKET
	/*******复核申请环节********/
	
	
	BalanceStatusRecord.recordStatus(balanceId, null, null, 
			null, BalanceStatusRecord.STATUS_04); //记录结算室审核完成记录
	BalanceStatusRecord.recordStatus(balanceId, null, null, 
			null, BalanceStatusRecord.STATUS_06); //记录复核申请完成记录
	BalanceStatusRecord.recordStatus(balanceId, null, null,
			null, BalanceStatusRecord.STATUS_07); //记录通知开票完成记录
//	BalanceStatusRecord.recordStatus(balanceId, logonUser.getUserId(), logonUser.getName(), 
//			logonUser.getOrgId(), BalanceStatusRecord.STATUS_08); //记录经销商确认信息
	
	//zhumingwei 2011-10-28 ADD 随机抽查功能
	TtAsDealerCheckPO poCheck = new TtAsDealerCheckPO();
	poCheck.setYieldly(sel.getYieldly());
	poCheck.setDealerId(balancePO.getDealerId());
	poCheck.setStatus(Constant.STATUS_ENABLE);
	TtAsDealerCheckPO poCheckValue=(TtAsDealerCheckPO)daoKP.select(poCheck).get(0);
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	Date lastDate = poCheckValue.getLastCheckDate();//上次抽查时间
	Calendar calendar = Calendar.getInstance();//公用类，加年月日的
	calendar.setTime(lastDate);
	calendar.add(Calendar.DAY_OF_MONTH, 1);//当月加一天(如果是本月的最后一天那么结果会是下月的第一天)
	lastDate = calendar.getTime();//得到加一天后的值
	int checkCount = poCheckValue.getCheckCount();//达到此抽查数量
	//int checkPercentage = (int)Math.ceil(Integer.parseInt(poCheckValue.getCheckPercentage()) * 0.01);//抽查百分比
	String checkPercentage = poCheckValue.getCheckPercentage();//抽查百分比
	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	String wxEndDate=format.format(list.get(0).getEndDate());//取得页面维修时间止
	List<Map<String, Object>> count = daoKP.getApplicationCount(sdf.format(lastDate),wxEndDate,poCheckValue.getDealerId().toString(),sel.getYieldly());
	Map map = (Map)count.get(0);
	int countCon = ((BigDecimal)map.get("COUNT")).intValue();//得到符合条件查询出来的数量
	if(countCon>checkCount){//跟抽查数量对比，如果大于抽查数量，开始抽查
		TtAsWrCheckApplicationPO checkApp = new TtAsWrCheckApplicationPO();
		long id = Long.parseLong(SequenceManager.getSequence(""));
		checkApp.setId(id);
		checkApp.setCheckNo(SequenceManager.getSequence("LO"));
		int con = (int)Math.ceil((Integer.parseInt(checkPercentage)*countCon)*0.01);
		checkApp.setCheckCount(con);
		checkApp.setBalanceNo(sel.getBalanceNo());
		checkApp.setDelaerId(Long.parseLong(poCheckValue.getDealerId().toString()));
		checkApp.setCheckDate(new Date());
		checkApp.setStatus(Constant.CHECK_APP_STATUS_1);
		checkApp.setIsFrint(0);
		checkApp.setCreateDate(new Date());
		daoKP.insert(checkApp);
		
		List<Map<String, Object>> detail = daoKP.getApplicationDetail(sdf.format(lastDate),wxEndDate,poCheckValue.getDealerId().toString(),sel.getYieldly(),con+1);
		for(int i=0;i<detail.size();i++){
			TtAsWrCheckDetailPO poDetail = new TtAsWrCheckDetailPO();
			poDetail.setId(Long.parseLong(SequenceManager.getSequence("")));
			poDetail.setCheckId(id);
			poDetail.setClaimId(((BigDecimal)detail.get(i).get("ID")).longValue());
			poDetail.setStatus(Constant.CHECK_APP_DETAIL_STATUS_1);
			poDetail.setCreateDate(new Date());
			daoKP.insert(poDetail);
		}
		//回写抽查截止时间
		TtAsDealerCheckPO poCheckCon = new TtAsDealerCheckPO();
		poCheckCon.setId(poCheckValue.getId());
		TtAsDealerCheckPO poCheckConValue = new TtAsDealerCheckPO();
		poCheckConValue.setLastCheckDate(sdf.parse(wxEndDate));
		poCheckConValue.setUpdateDate(new Date());
		daoKP.update(poCheckCon, poCheckConValue);
	}
	//zhumingwei 2011-10-28 ADD 随机抽查功能
	}catch(Exception e){
		e.printStackTrace();
		logger.error(e);
	}
}
/**
 * 
* @Title: updateMarketStatus 
* @Description: TODO(点完复核申请,将状态更新成已结算) 
* @param @param dealerId    经销商ID
* @return void    返回类型 
* @throws
 */
	private void updateMarketStatus(Long dealerId) {
		TtIfMarketPO po = new TtIfMarketPO();
		po.setDealerId(dealerId);
		po.setStatus(Constant.MARKET_BACK_STATUS_TECH_PASS);
		TtIfMarketPO npo = new TtIfMarketPO();
		npo.setStatus(Constant.MARKET_BACK_STATUS_BALANCE);
		MarketQuesOrderDao dao = MarketQuesOrderDao.getInstance();
		dao.update(po, npo);
	}
	/*
	 * 转行政扣款
	 */
	public void deductBalanceAudit(TtAsWrClaimBalanceTmpPO ppo){
		String id = ppo.getId().toString();
		String SUCCESS = "SUCCESS";
		try {
			BalanceMainDao dao = new BalanceMainDao();
				doBalanceKp(Long.valueOf(id));
					String deductAmount = ppo.getBalanceAmount().toString();
					String dealerId = CommonUtils.checkNull(ppo.getDealerId());
					String dealerCode = CommonUtils.checkNull(ppo.getDealerCode());
					String dealerName = CommonUtils.checkNull(ppo.getDealerName());  
					TtAsWrClaimBalancePO po = new TtAsWrClaimBalancePO();
					TtAsWrClaimBalancePO tp = new TtAsWrClaimBalancePO();
					po.setId(Long.parseLong(id));
					tp.setNoteAmount(0d);
					tp.setStatus(Integer.parseInt(Constant.ACC_STATUS_04));
					Date updateDate = new Date();
					tp.setUpdateDate(updateDate);
					dao.update(po, tp);//更新TT_AS_WR_CLAIM_BALANCE	
					
					//结算单信息
					TtAsWrClaimBalancePO balancePO = dao.getClaimBalancePOById(Long.parseLong(id));
					Long yieldly = -1L;
					if(balancePO!=null && balancePO.getYieldly()!=null)
						yieldly = balancePO.getYieldly();
					
					Double zjCount = Math.abs(Utility.getDouble(deductAmount));
					
					TtAsWrAdminDeductPO dp = new TtAsWrAdminDeductPO();
					dp.setId(Long.parseLong(SequenceManager.getSequence("")));
					dp.setDealerId(Long.parseLong(dealerId));
					dp.setDealerCode(dealerCode);
					dp.setDealerName(dealerName);
					dp.setDeductAmount(zjCount);
					dp.setDeductStatus(Integer.parseInt(Constant.ADMIN_STATUS_01));
					dp.setFromClaimbalanceId(Long.parseLong(id));
					dp.setCreateBy(tp.getUpdateBy());
					dp.setCreateDate(tp.getUpdateDate());
					TmDealerPO dpo = new TmDealerPO();
					dpo.setDealerId(Long.valueOf(dealerId));
					List<TmDealerPO> listDpo = this.select(dpo);
					Long companyId = listDpo.get(0).getCompanyId();//用户所属公司
					dp.setOemCompanyId(companyId);
					dp.setYieldly(yieldly);
					dao.insert(dp);//新增TT_AS_WR_ADMIN_DEDUCT
					
					//zhumingwei 2011-10-29 ADD 随机抽查功能
					TtAsDealerCheckPO poCheck = new TtAsDealerCheckPO();
					poCheck.setDealerId(Long.parseLong(dealerId));
					poCheck.setYieldly(yieldly);
					poCheck.setStatus(Constant.STATUS_ENABLE);
					TtAsDealerCheckPO poCheckValue=(TtAsDealerCheckPO)daoKP.select(poCheck).get(0);
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					Date lastDate = poCheckValue.getLastCheckDate();//上次抽查时间
					Calendar calendar = Calendar.getInstance();//公用类，加年月日的
					calendar.setTime(lastDate);
					calendar.add(Calendar.DAY_OF_MONTH, 1);//当月加一天(如果是本月的最后一天那么结果会是下月的第一天)
					lastDate = calendar.getTime();//得到加一天后的值
					int checkCount = poCheckValue.getCheckCount();//达到此抽查数量
					String checkPercentage = poCheckValue.getCheckPercentage();//抽查百分比
					String wxEndDate=sdf.format(ppo.getEndDate());//取得页面维修时间止
					List<Map<String, Object>> count = daoKP.getApplicationCount(sdf.format(lastDate),wxEndDate,dealerId,yieldly);
					Map map = (Map)count.get(0);
					int countCon = ((BigDecimal)map.get("COUNT")).intValue();//得到符合条件查询出来的数量
					if(countCon>checkCount){//跟抽查数量对比，如果大于抽查数量，开始抽查
						TtAsWrCheckApplicationPO checkApp = new TtAsWrCheckApplicationPO();
						long checkId = Long.parseLong(SequenceManager.getSequence(""));
						checkApp.setId(checkId);
						checkApp.setCheckNo(SequenceManager.getSequence("LO"));
						int con = (int)Math.ceil((Integer.parseInt(checkPercentage)*countCon)*0.01);
						checkApp.setCheckCount(con);
						checkApp.setBalanceNo(balancePO.getBalanceNo());
						checkApp.setDelaerId(Long.parseLong(dealerId));
						checkApp.setCheckDate(new Date());
						checkApp.setStatus(Constant.CHECK_APP_STATUS_1);
						checkApp.setIsFrint(0);
						checkApp.setCreateDate(new Date());
						daoKP.insert(checkApp);
						
						List<Map<String, Object>> detail = daoKP.getApplicationDetail(sdf.format(lastDate),wxEndDate,dealerId,yieldly,con+1);
						for(int i=0;i<detail.size();i++){
							TtAsWrCheckDetailPO poDetail = new TtAsWrCheckDetailPO();
							poDetail.setId(Long.parseLong(SequenceManager.getSequence("")));
							poDetail.setCheckId(checkId);
							poDetail.setClaimId(((BigDecimal)detail.get(i).get("ID")).longValue());
							poDetail.setStatus(Constant.CHECK_APP_DETAIL_STATUS_1);
							poDetail.setCreateDate(new Date());
							daoKP.insert(poDetail);
						}
					}
					//zhumingwei 2011-10-29 ADD 随机抽查功能
					
					//记录日志表
					BalanceStatusRecord.recordStatus(Long.parseLong(id), null, null, null, BalanceStatusRecord.STATUS_07);
					
					
		}catch(Exception e) {//异常方法
			SUCCESS = "ERROR";
			e.printStackTrace();
		}
	}
	/***addUser xiongchuan 2012-06-25****查询站长电话和索赔员电话*****START**********/
	public String viewClaimTel(String dealerId,String perpose){
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT   p.per_phone\n" );
		sql.append(" FROM Tm_Claim_Contact_Phone p\n" );
		sql.append("WHERE 1 = 1\n" );
		sql.append("  AND p.Dealer_Id = "+dealerId+"  and p.per_pose="+perpose+" order by p.create_date");
		List<Map<String,Object>> list = this.pageQuery(sql.toString(), null, this.getFunName());
		if(list.size()>0&&list.get(0).get("PER_PHONE")!=""&&list.get(0).get("PER_PHONE")!=null){
			return list.get(0).get("PER_PHONE").toString();
		}else{
			return  null;
		}
		
		
	}
	
	/***addUser xiongchuan 2012-06-25****查询站长电话和索赔员电话*****END**********/
	
	public List<TtAsDealerTypePO>  viewDealerType(){
		StringBuffer sql= new StringBuffer();
		sql.append("select * from Tt_As_Dealer_Type t   where t.is_notice=0 and t.status=10041002  and t.balance_date is not null and t.balance_review_date is not null and rownum<21");
		List<TtAsDealerTypePO>  list = this.select(TtAsDealerTypePO.class, sql.toString(), null);
		return list;
	}
	public void updateIsNotice(TtAsDealerTypePO po){
		StringBuffer sql= new StringBuffer();
		sql.append("update Tt_As_Dealer_Type t set t.is_notice=1 where  t.dealer_id="+po.getDealerId()+" and t.yieldly="+po.getYieldly()+"");
		this.update(sql.toString(), null);
		
	}
	
}
