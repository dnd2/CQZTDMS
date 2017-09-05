package com.infodms.dms.actions.repairOrder;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.common.Constant;
import com.infodms.dms.common.DBLockUtil;
import com.infodms.dms.dao.claim.auditing.AutoAuditingDao;
import com.infoservice.dms.chana.actions.AbstractSendTask;

public class RoOneDayOneVin extends AbstractSendTask{
	private static Logger logger = Logger.getLogger(RoOneDayOneVin.class);
	
	private String vin;
	private Long  foreId;
	private String repairType;
	private String dealerId;
	private Long roId;
	private String roStartdate;
	public RoOneDayOneVin(String vin,String repairType,Long roId,String roStartdate){
		this.vin = vin;
		this.roId = roId;
		this.repairType = repairType;
		this.roStartdate = roStartdate;
	}
	public RoOneDayOneVin(Long  foreId,Long roId,String  dealerId){
		this.foreId = foreId;
		this.roId = roId;
		this.dealerId = dealerId;
	}
	@Override
	protected String handleExecute() throws Exception {
		logger.info("一天一车自动审核开始======>");
		//加入同步
		boolean isDeal = DBLockUtil.lock("10017", DBLockUtil.BUSINESS_TYPE_17);
		try{
			if(isDeal){ 
					if(!autoAuditing()){
						System.out.println("------------come in---------");
						RoDealerDqvCheck check = new RoDealerDqvCheck(this.dealerId,this.foreId,this.roId);
						check.execute();
					}
			}else{
				System.out.println("[100017]同步放弃本次执行!");
			}
		}catch(Exception e){
		}finally{
			DBLockUtil.freeLock("10017", DBLockUtil.BUSINESS_TYPE_17);
		}
		logger.info("一天一车自动审核结束<======");
		return "";
	}

	public boolean  autoAuditing(){
		AutoAuditingDao dao = AutoAuditingDao.getInstance();
		boolean  flag = false;
//		if((!Constant.REPAIR_TYPE_04.equalsIgnoreCase(repairType))&&(!Constant.REPAIR_TYPE_05.equalsIgnoreCase(this.repairType))){
//			List<Map<String,Object>> list = dao.getList(this.vin,this.roId,this.roStartdate);
//			if(list.size()>0 && list!=null){
//				flag=true;
//			}
//		}
		return flag;
	}
}
