package com.infodms.dms.actions.repairOrder;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.infodms.dms.common.Constant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.dao.claim.dealerClaimMng.ClaimBillMaintainDAO;
import com.infodms.dms.po.TmVehiclePO;
import com.infodms.dms.po.TtAsWrRuleListPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.DateTimeUtil;
import com.infodms.dms.util.StringUtil;
import com.infoservice.dms.chana.vo.WarrantyPartVO;

/**
 * 
* @ClassName: WrRuleUtil 
* @Description: TODO(配件三包规则计算) 
* @author liuqiang 
* @date Sep 20, 2010 5:08:39 PM 
*
 */
public class WrRuleUtil {
	public Logger logger = Logger.getLogger(WrRuleUtil.class);
	private final ClaimBillMaintainDAO dao = ClaimBillMaintainDAO.getInstance();
	
	/**
	 * 
	* @Title: wrRuleCompute 
	* @Description: TODO(计算配件三包期规则) 
	* @param @param inMileage 行驶里程
	* @param @param purchasedDate 购车日期
	* @param @param vin 车辆VIN
	* @param @param partCode 配件代码
	* @param @return
	* @return WarrantyPartVO  返回类型 (是否过三包期 开始里程 结束里程 开始时间 结束时间 超出里程 超出时间)
	* @throws
	 */
	public WarrantyPartVO wrRuleCompute(String inMileage, String purchasedDate, String vin, String partCode) throws Exception {
		WarrantyPartVO wp = new WarrantyPartVO();
		int month = getDifferMonth(purchasedDate); //三包日期和当前日期相差几个月
		int day = getDifferDay(purchasedDate);
		List<TtAsWrRuleListPO> ls = dao.getPartGuaListByVin(vin, partCode);//获取配件的相关三包规则
		TtAsWrRuleListPO tap = new TtAsWrRuleListPO();
		TmVehiclePO tvp = new TmVehiclePO();
        String formatStyle ="yyyy-MM-dd";   
        DateFormat df = new SimpleDateFormat(formatStyle);
        String d2 = df.format(new Date());
        String d1 = df.format(df.parse(purchasedDate));
		int freeTimes = 0;
		
		/*****add by liuxh 20131108判断车架号不能为空*****/
		CommonUtils.jugeVinNull(vin);
		/*****add by liuxh 20131108判断车架号不能为空*****/
		
		if (Utility.testString(vin)){
			tvp.setVin(vin);
			List<TmVehiclePO> ls00 = dao.select(tvp);
			if (ls00!=null&&ls00.size()>0){
				tvp = ls00.get(0);
				freeTimes = tvp.getFreeTimes();
			}
		}
		int getFree = dao.getOutGuaMile(inMileage); //根据里程取得应该有过的保养次数
		if (dao.getOutGuaTime(day)>getFree) {
			getFree = dao.getOutGuaTime(day);//根据相隔天数取得应该有过的保养次数
		}
		if (freeTimes < getFree) { //脱保：车辆表中的保养次数<应该保养次数(里程/时间)
			logger.info("改配件已经脱保 vin = " + vin + ", partCode = " + partCode);
		}else{
			if (ls != null && ls.size() > 0) {
				tap = ls.get(0);
				//判断是否在三包期内 1:时间是否过三包期 2:行驶里程是否过三包
				boolean b =Utility.checkDate(d1,d2,Integer.parseInt(tap.getClaimMonth().toString()));
				if (b && Utility.getDouble(inMileage) <= tap.getClaimMelieage()) {
					wp.setIsInWarranty(Constant.IF_TYPE_YES);
				} else {
					wp.setIsInWarranty(Constant.IF_TYPE_NO);
				}
			} else {
				List<TtAsWrRuleListPO> ls1 = dao.getPartGuaCommonListByVin(partCode); //在通用三包规则中查询
				if (ls1 != null && ls1.size() > 0) {
					tap = ls1.get(0);
					//判断是否在三包期内 1:时间是否过三包期 2:行驶里程是否过三包
					boolean b =Utility.checkDate(purchasedDate,d2,Integer.parseInt(tap.getClaimMonth().toString()));
					if (b && Utility.getDouble(inMileage) <= tap.getClaimMelieage()) {
						wp.setIsInWarranty(Constant.IF_TYPE_YES);
					} else {
						wp.setIsInWarranty(Constant.IF_TYPE_NO);
					}
				}else {
					logger.info("没有为配件设定三包规则 vin = " + vin + ", partCode = " + partCode);
				}
			}		
		}
		Date startDate = DateTimeUtil.stringToDate(purchasedDate);
		Date endDate = null;
		if( null != tap.getClaimMonth()){
		 endDate = MonthAdd(purchasedDate, Integer.parseInt(String.valueOf(tap.getClaimMonth())));//车购买时间加上三包期最大月份,算出三包期结束时间
		}
		wp.setWarrantyBeginDate(startDate);//三包开始日期
		wp.setWarrantyEndDate(endDate);//三包结束日期
		// 超出天数  三包期结束日期-开始日期
		if(null != endDate){ //YH 2010.11.29
		 wp.setOverDay(overDays(startDate.getTime(), endDate.getTime()));
		}
		Double startMile = Double.parseDouble(inMileage);//进厂里程数
		Double endMile = tap.getClaimMelieage();//三包期最大里程
		wp.setWarrantyBeginMileage(startMile);
		wp.setWarrantyEndMileage(endMile);
		if( null != endMile){ //YH 2010.11.30
		//超出里程  进厂里程数-三包期最大里程 如果<0 超出为0
		 wp.setOverMileage((startMile - endMile) < 0 ? 0 : (startMile - endMile));
		}
		return wp;
	}
	public WarrantyPartVO wrRuleCompute2(String date,String inMileage, String purchasedDate, String vin, String partCode) throws Exception {
		WarrantyPartVO wp = new WarrantyPartVO();
		if(StringUtil.isNull(purchasedDate)){
			wp.setIsInWarranty(Constant.IF_TYPE_YES);
			return wp ;
		}
		int month = getDifferMonth(purchasedDate); //三包日期和当前日期相差几个月
		int	day = getDifferDay(purchasedDate);
		List<TtAsWrRuleListPO> ls = dao.getPartGuaListByVin(vin, partCode);//获取配件的相关三包规则
		TtAsWrRuleListPO tap = new TtAsWrRuleListPO();
		TmVehiclePO tvp = new TmVehiclePO();
        String formatStyle ="yyyy-MM-dd";   
        DateFormat df = new SimpleDateFormat(formatStyle);
        String d2 = date ;
        String d1 = df.format(df.parse(purchasedDate));
		int freeTimes = 0;
		
		/*****add by liuxh 20131108判断车架号不能为空*****/
		CommonUtils.jugeVinNull(vin);
		/*****add by liuxh 20131108判断车架号不能为空*****/
		
		if (Utility.testString(vin)){
			tvp.setVin(vin);
			List<TmVehiclePO> ls00 = dao.select(tvp);
			if (ls00!=null&&ls00.size()>0){
				tvp = ls00.get(0);
				freeTimes = tvp.getFreeTimes();
			}
		}
		int getFree = dao.getOutGuaMile(inMileage); //根据里程取得应该有过的保养次数
		if (dao.getOutGuaTime(day)>getFree) {
			getFree = dao.getOutGuaTime(day);//根据相隔天数取得应该有过的保养次数
		}
		//if (freeTimes < getFree) { //脱保：车辆表中的保养次数<应该保养次数(里程/时间)
		//	logger.info("改配件已经脱保 vin = " + vin + ", partCode = " + partCode);
		//}else{
			if (ls != null && ls.size() > 0) {
				tap = ls.get(0);
				//判断是否在三包期内 1:时间是否过三包期 2:行驶里程是否过三包
				boolean b =Utility.checkDate(d1,d2,Integer.parseInt(tap.getClaimMonth().toString()));
				if (b && Utility.getDouble(inMileage) <= tap.getClaimMelieage()) {
					wp.setIsInWarranty(Constant.IF_TYPE_YES);
				} else {
					wp.setIsInWarranty(Constant.IF_TYPE_NO);
				}
			} else {
				List<TtAsWrRuleListPO> ls1 = dao.getPartGuaCommonListByVin(partCode); //在通用三包规则中查询
				if (ls1 != null && ls1.size() > 0) {
					tap = ls1.get(0);
					//判断是否在三包期内 1:时间是否过三包期 2:行驶里程是否过三包
					boolean b =Utility.checkDate(purchasedDate,d2,Integer.parseInt(tap.getClaimMonth().toString()));
					if (b && Utility.getDouble(inMileage) <= tap.getClaimMelieage()) {
						wp.setIsInWarranty(Constant.IF_TYPE_YES);
					} else {
						wp.setIsInWarranty(Constant.IF_TYPE_NO);
					}
				}else {
					logger.info("没有为配件设定三包规则 vin = " + vin + ", partCode = " + partCode);
				}
			}		
		//}
		Date startDate = DateTimeUtil.stringToDate(purchasedDate);
		Date endDate = null;
		if( null != tap.getClaimMonth()){
		 endDate = MonthAdd(purchasedDate, Integer.parseInt(String.valueOf(tap.getClaimMonth())));//车购买时间加上三包期最大月份,算出三包期结束时间
		}
		wp.setWarrantyBeginDate(startDate);//三包开始日期
		wp.setWarrantyEndDate(endDate);//三包结束日期
		// 超出天数  三包期结束日期-开始日期
		if(null != endDate){ //YH 2010.11.29
		 wp.setOverDay(overDays(startDate.getTime(), endDate.getTime()));
		}
		Double startMile = Double.parseDouble(inMileage);//进厂里程数
		Double endMile = tap.getClaimMelieage();//三包期最大里程
		wp.setWarrantyBeginMileage(startMile);
		wp.setWarrantyEndMileage(endMile);
		if( null != endMile){ //YH 2010.11.30
		//超出里程  进厂里程数-三包期最大里程 如果<0 超出为0
		 wp.setOverMileage((startMile - endMile) < 0 ? 0 : (startMile - endMile));
		}
		return wp;
	}
	
	/**
	 * 
	* @Title: isProtected 
	* @Description: TODO(计算整车是否脱保) 
	* @param @param inMileage 行驶里程
	* @param @param purchasedDate 购车日期
	* @param @param freeTimes 车辆保养次数
	* @param @return
	* @return Integer  返回类型 (整车是否脱保)
	* @throws
	 */
	public Integer isProtected(int freeTimes,String inMileage, String purchasedDate)throws Exception{
		int day = getDifferDay(purchasedDate);
		int getFree = dao.getOutGuaMile(inMileage); //根据里程取得应该有过的保养次数
		if (dao.getOutGuaTime(day)>getFree) {
			getFree = dao.getOutGuaTime(day);//根据相隔天数取得应该有过的保养次数
		}
		if (freeTimes < getFree) { //脱保：车辆表中的保养次数<应该保养次数(里程/时间)
			return Constant.STATUS_DISABLE;
		}
		return Constant.STATUS_ENABLE;
	}
	/**
	 * 
	* @Title: getDifferMonth 
	* @Description: TODO(获取当前日期和三包开始日期相差的月数) 
	* @param @param purchasedDate 三包开始日期
	* @return int  相差月数
	* @throws
	 */
	private int getDifferMonth(String purchasedDate) throws ParseException {
		if (!Utility.testString(purchasedDate)) {		
			throw new IllegalArgumentException("没有购车日期" + purchasedDate);
		} 
		String d1 = purchasedDate;
		String d2 = DateTimeUtil.parseDateToString(new Date());
		int month  = Utility.compareDate(d1,d2,1); //获取相差月数
		int day  = Utility.compareDate(d1,d2,2);//获取相差天数
		if (day >= 1) {
			month = month+1;
		}
		return month;
	}
	/**
	 * 
	* @Title: getDifferDay 
	* @Description: TODO(这里用一句话描述这个方法的作用) 
	* @param @param purchasedDate
	* @param @return
	* @param @throws ParseException    设定文件 
	* @return int    返回类型 
	* @throws
	 */
	private int getDifferDay(String purchasedDate) throws ParseException {
		if (!Utility.testString(purchasedDate)) {		
			throw new IllegalArgumentException("没有购车日期" + purchasedDate);
		} 
		String d1 = purchasedDate;
		String d2 = DateTimeUtil.parseDateToString(new Date());
		//int month  = Utility.compareDate(d1,d2,1); //获取相差月数
		int day  = Utility.compareDate(d1,d2,2);//获取相差天数
		return day;
	}
	
	/**
	 * 
	* @Title: MonthAdd 
	* @Description: TODO(计算三包期到期时间) 
	* @param @param purchasedDate 三包期开始时间
	* @param @param month 三包期月数
	* @return Date    三包期结束时间
	* @throws
	 */
	public static Date MonthAdd(String purchasedDate, int month) throws ParseException{
		Date date = DateTimeUtil.stringToDate(purchasedDate);
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.MONTH, month);
		c.add(Calendar.DATE, -1);
		return c.getTime();
	}
	/**
	 * 
	* @Title: overDays 
	* @Description: TODO(计算三包期超出天数) 
	* @param @param startTime 三包期开始时间
	* @param @param endTime 三包期结束时间
	* @return Double  超出天数
	* @throws
	 */
	private Double overDays(long startTime, long endTime) {
		long time = (startTime - endTime) / (1000*60*60*24);
		return Double.parseDouble(String.valueOf(time)) > 0 ? Double.parseDouble(String.valueOf(time))
				: 0.0;
	}
	
	/**
	 * 
	* @Title: milesAdd 
	* @Description: TODO(根据进厂行驶公里数减去出厂公里数) 
	* @param @param aMile 实际行驶公里数
	* @param @param cMile  出厂公里数
	* @param @return startMile + endMile
	* @return Date    返回类型 
	* @throws
	 */
	public static Double milesCut(Double aMile, Double cMile) {
		return aMile - cMile;
	}
	/**
	 * 
	* @Title: add by tanv 返回三包规则 
	* @throws
	 */
	public List<TtAsWrRuleListPO> getWrRule(String vin, String partNo) {
		List<TtAsWrRuleListPO> ls = dao.getPartGuaListByVin(vin, partNo);
		if(ls==null || ls.size()==0){
			ls = dao.getPartGuaCommonListByVin(partNo); //在通用三包规则中查询
		}
		return ls;
	}
}
