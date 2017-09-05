package com.infodms.dms.actions.customerRelationships.baseSetting;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.tempuri.UltraCRMWebservice;
import org.tempuri.UltraCRMWebserviceSoap;

import com.infodms.dms.dao.customerRelationships.CustomerRelTaskDao;
import com.infodms.dms.po.TtCrmSeatsTeamPO;
import com.infoservice.dms.chana.actions.AbstractSendTask;

public class CustomerRelAutoJob  extends AbstractSendTask {
	private static Logger logger = Logger.getLogger(CustomerRelAutoJob.class);
	
	private CustomerRelTaskDao dao = CustomerRelTaskDao.getInstance();
	
	protected synchronized String handleExecute() throws Exception {
		logger.info("CustomerRelAutoJob[START]======>");

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

		// 获取数据库服务器的时间
		String dbDateSting = dao.getDbDate();

		String compareTimeString = dbDateSting.split(" ")[0] + " 18:00:00";
		Date compareTime = sdf.parse(compareTimeString);
		Date dbDate = sdf.parse(dbDateSting);
		
		if (dbDate.getTime() > compareTime.getTime())		{
			System.out.println("当前时间超过18:00点，可以开始执行任务");
			
			// 1.首先得到第二天排班的坐席和组信息
			List<Map<String,Object>> seats = dao.selectSecDaySeats();
			
			// 2.获得WebService链接
			UltraCRMWebservice ultraCRMWebservice = new UltraCRMWebservice();
			UltraCRMWebserviceSoap soap = ultraCRMWebservice.getUltraCRMWebserviceSoap();
			
			// 3.循环坐席和组信息
			if(null != seats && seats.size() > 0){
				for (Map<String, Object> map : seats) {
					
					// 4.取得坐席代码, 坐席名字, 组代码, 组名字
					String seAccount = map.get("SE_ACCOUNT").toString();
					String seName = map.get("SE_NAME").toString();
					String seExt = map.get("SE_EXT").toString();
					String stCode = map.get("ST_CODE").toString();
					String stName = map.get("ST_NAME").toString();
					
					//更新Avaya上的坐席
					soap.editAgentInfo(seAccount+"|"+seName+"|"+"1234"+"|"+seName+"|"+seExt , seAccount+"|1|1|1|1", 1);
					
					//更新Avaya上的坐席组成员
					soap.magACDGPMember(seAccount+"|"+seName+"|"+stCode+"|"+stName+"|"+"0",seAccount+"|1|1|1|1" , 1);
				}
			}
			
			System.out.println("执行任务结束!");
		}
		
		logger.info("======>CustomerRelAutoJob[END]");
		 return null;
	}

}
