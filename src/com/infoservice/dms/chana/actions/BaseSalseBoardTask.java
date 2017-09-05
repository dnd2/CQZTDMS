package com.infoservice.dms.chana.actions;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import com.infoservice.dms.chana.dao.SalseBoardDao;


 
/**
 * 每天定时对TT_VS_DASHBOARD(销售看板表)插入当天的数据
 * @author hezongkun
 *
 */
public class BaseSalseBoardTask extends AbstractSendTask{
    private static final Logger logger = Logger.getLogger(BaseSalseBoardTask.class);
	private SalseBoardDao dao = SalseBoardDao.getInstance();	
	
	@Override
	protected  String handleExecute() throws Exception{
		logger.info("BaseSalseBoardTask[START]======>");
		
		/** wangsw 修改开始 */
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		
		// 获取数据库服务器的时间
		String dbDateSting = dao.getDbDate();
		
		String compareTimeString = dbDateSting.split(" ")[0] + " 11:50:00";
		Date compareTime = sdf.parse(compareTimeString);
		Date dbDate = sdf.parse(dbDateSting);
		
		if (dbDate.getTime() > compareTime.getTime())
		{
			List<Map<String, Object>> list = dao.getTodayInvoAndSales();
			// 插入临时销售数据
			dao.insertIntoBaseBoard(list);
		}
		
		/** wangsw 修改结束*/
		
//			//查询启票信息，先插入看板表
//			List<Map<String,Object>> list1= dao.getOrderDetail();
//			dao.insertIntoBaseBoardByOrder(list1);
//			
//			//查询实销信息，如果已有相应记录则只需更新，如果没有则需插入
//			List<Map<String,Object>> list2= dao.getCarSalesDetail();
//			if (list2.size() > 0) {
//				for (int i = 0; i < list2.size(); i++) {
//					String dealerCode = list2.get(i).get("DEALER_CODE").toString();
//					String seriesId = list2.get(i).get("SERIES_ID").toString();
//					String year = list2.get(i).get("NYEAR").toString();
//					String month = list2.get(i).get("NMONTH").toString();
//					String day = list2.get(i).get("NDAY").toString();
//					String todaySalesAmount = list2.get(i).get("TODAY_SALES_AMOUNT").toString();
//					List<Map<String, Object>> list = dao.selectBaseBoard(dealerCode, seriesId, year, month, day);
//					if (list.size() == 0) {// 如果没查到数据则插入此数据
//						dao.insertIntoBaseBoardByCarSales(list2.get(i));
//					} else {// 否则更新
//						dao.updateBaseBoardBySales(dealerCode, seriesId, year,month, day, todaySalesAmount);
//					}
//				}
//			}
	        //查询月度计划，如果已有相应记录则只需更新，如果没有则需插入
//	        List<Map<String,Object>> list3 = dao.getSalesPlanDetail();
//			if (list3.size() > 0) {
//				for (int j = 0; j < list3.size(); j++) {
//					String dealerCode = list3.get(j).get("DEALER_CODE").toString();
//					String seriesId = list3.get(j).get("SERIES_ID").toString();
//					String year = list3.get(j).get("NYEAR").toString();
//					String month = list3.get(j).get("NMONTH").toString();
//					String day = list3.get(j).get("NDAY").toString();
//					String planSalesAmount = list3.get(j).get("MONTH_BILL_AMOUNT1").toString();
//					List<Map<String, Object>> list = dao.selectBaseBoard(dealerCode, seriesId, year, month, day);
//					if (list.size() == 0) {// 如果没查到数据则插入此数据
//						dao.insertIntoBaseBoardByPlan(list3.get(j));
//					} else {// 否则更新
//						dao.updateBaseBoardByPlan(dealerCode, seriesId, year, month, day, planSalesAmount);
//					}
//				}
//			}
//			dao.updateMonthData();
//			dao.updateYearData();
			
		logger.info("======>BaseSalseBoardTask[END]");
		
		return null;
	}

}
