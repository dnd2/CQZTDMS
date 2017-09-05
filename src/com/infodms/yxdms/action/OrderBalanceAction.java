package com.infodms.yxdms.action;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.actions.repairOrder.RoMaintainMain;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.common.tag.BaseAction;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.exception.UserException;
import com.infodms.dms.po.TmVehiclePO;
import com.infodms.dms.po.TtAsCancelBalanceRecordPO;
import com.infodms.dms.po.TtAsRepairOrderPO;
import com.infodms.dms.po.TtAsRoOldRepairPartPO;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infodms.yxdms.dao.OrderBalanceDAO;
import com.infodms.yxdms.utils.DaoFactory;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

public class OrderBalanceAction  extends BaseAction{
	public Logger logger = Logger.getLogger(OrderBalanceAction.class); 
	private ActionContext act = ActionContext.getContext();
	private OrderBalanceDAO dao = OrderBalanceDAO.getInstance();
	
	private final String initUrl = "/jsp_new/order/ro_balance_list.jsp";
	private final String initAuditUrl = "/jsp_new/order/ro_balance_audit_list.jsp";
	
	/**
	 * 初始化页面
	 */
	public void orderBalanceList(){
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try{
			String flag = getParam("query");
			if("true".equals(flag)){
				PageResult<Map<String, Object>> list=null;
				//list=orderservice.orderList(loginUser,request,Constant.PAGE_SIZE,getCurrPage());
				act.setOutData("ps", list);
			}
			act.setForword(initUrl);
		}catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"工单结算查询页面");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	@SuppressWarnings("unchecked")
	public void roBalance() throws ParseException{
			//配件的 （三种类型）
			//part_code_1 --正常维修
			//part_code_2 --免费保养
			//part_code_3 --自费保养
			String[] part_id_1 = DaoFactory.getParams(request, "part_id_1");
			String[] part_id_2 = DaoFactory.getParams(request, "part_id_2");
			String[] part_id_3 = DaoFactory.getParams(request, "part_id_3");
			String[] ro_repair_part_1 = DaoFactory.getParams(request, "ro_repair_part_1");
			String[] ro_repair_part_2 = DaoFactory.getParams(request, "ro_repair_part_2");
			String[] ro_repair_part_3 = DaoFactory.getParams(request, "ro_repair_part_3");
			String[] pay_type_1 = DaoFactory.getParams(request, "pay_type_1");
			String[] pay_type_2 = DaoFactory.getParams(request, "pay_type_2");
			String[] pay_type_3 = DaoFactory.getParams(request, "pay_type_3");
			//工单信息
			String vin = DaoFactory.getParam(request, "vin");
			String free_times = DaoFactory.getParam(request, "free_times");
			String date = DaoFactory.getParam(request, "for_balance_time");//工单结算时间 第一次结算时设置为当前时间 （taropUp）
			date=date.replaceAll("\\+", " ");
			String startDate =  DaoFactory.getParam(request, "ro_create_date");
			String roNo = DaoFactory.getParam(request, "ro_no");
			String id = DaoFactory.getParam(request, "id");
			StringBuffer res = new StringBuffer();
			//todo 
			//针对保养的工单，只更新状态
			//针对正常维修的工单,结算的时候更新索赔件的三包(售前车不更新),更新单据状态
			//取消结算，正常维修的工单备份,重新结算的时候获取备份数据取消结算,结算的时候重新跑三包 ,更新单据状态
			AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
			try{
				TmVehiclePO tvPO = new TmVehiclePO();
				tvPO.setVin(vin);
				List<PO> list = dao.select(tvPO);
				
				TtAsRepairOrderPO tarop = new TtAsRepairOrderPO();
				tarop.setId(Long.parseLong(id));
				List<TtAsRepairOrderPO> listData = dao.select(tarop);
				int amount = 0;
				int index = 0;
				if(null != part_id_1){
					amount += part_id_1.length;
				}
				if(null != part_id_2){
					amount += part_id_2.length;
				}
				if(null != part_id_3){
					amount += part_id_3.length;
				}
				String[] payType = new String[amount];
				String[] payTypePart = new String[amount];
				// 判断是否是正常维修  如果是者做新三包预警
				int flag = 0 ;
				if(null != part_id_1 && null != pay_type_1){
					flag = 1;
					for(int j=0;j<part_id_1.length;j++){
						payType[index] = pay_type_1[j];
						payTypePart[index] = pay_type_1[j]+"-"+ro_repair_part_1[j];
						index++;
					}
				}
				if(null != part_id_2 && null != pay_type_2){
					for(int j=0;j<part_id_2.length;j++){
						payType[index] = pay_type_2[j];
						payTypePart[index] = pay_type_2[j]+"-"+ro_repair_part_2[j];
						index++;
					}
				}
				if(null != part_id_3 && null != pay_type_3){
					for(int j=0;j<part_id_3.length;j++){
						payType[index] = pay_type_3[j];
						payTypePart[index] = pay_type_3[j]+"-"+ro_repair_part_3[j];
						index++;
					}
				}
				if(null!=list && list.size()>0){
					//todo 三包规则更新
					TmVehiclePO tempPO = (TmVehiclePO)list.get(0);
					if(tempPO.getLifeCycle().toString().equals(Constant.VEHICLE_LIFE_04.toString())){
						//实销车辆三包规则开始
						RoMaintainMain roMain = new RoMaintainMain();
						//roMain.roBalanceCancelAudit(id,free_times);
						roMain.roBalanceInterface(payType,id,free_times,date,payTypePart,null,startDate,flag);
					}else{
						if(tempPO.getLifeCycle().toString().equals(Constant.VEHICLE_LIFE_06.toString())){
							res.append("该车辆目前是无效状态,工单无法结算,请联系厂家确认!");
						}else{//售前车辆 更新单据状态
							//单据类型
							TtAsRepairOrderPO aopPO = new TtAsRepairOrderPO();
							aopPO.setId(Long.parseLong(id));
							TtAsRepairOrderPO aRoPO = new TtAsRepairOrderPO();
							aRoPO.setRoStatus(Constant.RO_STATUS_02);
							Date forBalanceTime = listData.get(0).getForBalanceTime() ;
							if (forBalanceTime== null || "".equalsIgnoreCase(forBalanceTime.toString())) {
								aRoPO.setForBalanceTime(Utility.parseString2DateTime(date, "yyyy-MM-dd HH:mm"));// 结算时间 第一次
							}else{
								aRoPO.setForBalanceTime(forBalanceTime); //设置 为 第一次结算的时间
							}
							aRoPO.setId(Long.parseLong(id));
							aRoPO.setRoStatus(Constant.RO_STATUS_02); // 已结算
							listData.get(0).setRoStatus(Constant.RO_STATUS_02);
							aRoPO.setVer(listData.get(0).getVer() + 1);// 将版本号加一
							aRoPO.setForBalanceTime(forBalanceTime);
							aRoPO.setPrintRoTime(listData.get(0).getCreateDate());
							dao.update(aopPO, aRoPO);
						}
					}
				}else{
					res.append("查询不到该车辆的信息,请确认车价后信息是否有误!");
				}
				
				TtAsCancelBalanceRecordPO cbRPO = new TtAsCancelBalanceRecordPO();
				cbRPO.setCreateBy(loginUser.getUserId());
				cbRPO.setCreateDate(new Date());
				cbRPO.setRecordId(Long.parseLong(SequenceManager.getSequence("")));
				cbRPO.setRecordStatus(Constant.RO_STATUS_02);
				cbRPO.setRoId(Long.parseLong(id));
				cbRPO.setRoNo(roNo);
				cbRPO.setRecordType(Long.parseLong(Constant.IF_TYPE_NO.toString()));
				dao.insert(cbRPO);
				if(res.toString().length()>0){
					act.setOutData("BALANCE_SUCCESS", res.toString());
				}
				act.setOutData("BALANCE_SUCCESS", "工单结算成功! 如需索赔,请及时编辑索赔单!");
			}catch (Exception e) {
				BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"工单结算查询页面");
				logger.error(logonUser,e1);
				act.setException(e1);
			}
		}
	
	/**
	 * 取消结算
	 * @param id
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public void roCancelBalance(){
		String id = DaoFactory.getParam(request, "id");
		String roNo = DaoFactory.getParam(request, "ro_no");
		try{
			//判断工单是否已转索赔单
			boolean appFlag = dao.checkRoApplication(id);
			if(!appFlag){
				act.setOutData("info", "该工单已转索赔单,工单不能取消结算!");
				act.setForword(initUrl);
			}
			TtAsRepairOrderPO tarop = new TtAsRepairOrderPO();
			tarop.setId(Utility.getLong(id));
			TtAsRepairOrderPO taropUp = new TtAsRepairOrderPO();
			taropUp.setId(Utility.getLong(id));
			taropUp.setRoStatus(Constant.RO_STATUS_04); // 取消结算待审核
			dao.update(tarop, taropUp);
			TtAsCancelBalanceRecordPO cbRPO = new TtAsCancelBalanceRecordPO();
			cbRPO.setCreateBy(loginUser.getUserId());
			cbRPO.setCreateDate(new Date());
			cbRPO.setRecordId(Long.parseLong(SequenceManager.getSequence("")));
			cbRPO.setRecordStatus(Constant.RO_STATUS_04);
			cbRPO.setRoId(Long.parseLong(id));
			cbRPO.setRoNo(roNo);
			//结算日志
			cbRPO.setRecordType(Long.parseLong(Constant.IF_TYPE_NO.toString()));
			dao.insert(cbRPO);
			act.setOutData("info", "工单取消结算成功! 请前往取消结算审核处审核!");
			act.setForword(initUrl);
		}catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"工单取消结算页面");
			logger.error(loginUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 取消结算审核初始页面
	 */
	public void roCancelAuditInit(){
		AclUserBean logonUser = (AclUserBean)act.getSession().get(Constant.LOGON_USER);
		try{
			String flag = getParam("query");
			if("true".equals(flag)){
				PageResult<Map<String, Object>> list=null;
				list=dao.orderList(loginUser,request,Constant.PAGE_SIZE,getCurrPage());
				act.setOutData("ps", list);
			}
			act.setForword(initAuditUrl);
		}catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"工单结算查询页面");
			logger.error(logonUser,e1);
			act.setException(e1);
		}
	}
	
	/**
	 * 取消结算审核逻辑
	 */
	@SuppressWarnings("unchecked")
	public void roCancelBalanceAudit(){
		String id = DaoFactory.getParam(request, "id");
		String roNo = DaoFactory.getParam(request, "ro_no");
		String ro_type = DaoFactory.getParam(request, "ro_type");
		String vin = DaoFactory.getParam(request, "vin");
		String free_times = DaoFactory.getParam(request, "for_balance_time");
		
		
		
		String checkStatus = DaoFactory.getParam(request, "check_status");
		int ro_status = 0;
		try{
			if(checkStatus.equals("1")){//审核通过
				if(ro_type != null && ro_type.length() > 0 ){
						String[] split = ro_type.split(",");
						if( vin != null ){
							StringBuffer sql= new StringBuffer();
							if(split.length==1){
								sql.append("update tm_vehicle t set t.free_times = nvl(t.free_times,0) - 1 where t.vin = '"+vin+"'");
							}
							if(split.length==2){
								sql.append("update tm_vehicle t set t.free_times = nvl(t.free_times,0) - 2 where t.vin = '"+vin+"'");
							}
                            dao.update(sql.toString(), null);
					}
				}else{
					TmVehiclePO tempPO = new TmVehiclePO ();
					tempPO.setVin(vin);
					tempPO =(TmVehiclePO) dao.select(tempPO).get(0);
					if(tempPO.getLifeCycle().toString().equals(Constant.VEHICLE_LIFE_04.toString())){
						RoMaintainMain roMain = new RoMaintainMain();
						roMain.roBalanceCancelAudit(id,free_times);
					}			
            
				}
				ro_status = Constant.RO_STATUS_01;
				//保存原始工单的配件信息用于再次结算的时候回滚操作
				TtAsRoOldRepairPartPO oldRepairePartPO = new TtAsRoOldRepairPartPO();
				oldRepairePartPO.setRoId(Long.parseLong(id));
				oldRepairePartPO.setRollStatus(Constant.IF_TYPE_NO);
				List<PO> t = dao.select(oldRepairePartPO);
				//经检查这段代码存正问题
				//保存原始工单配件历史 用于三包回滚操作
					List<Map<String,String>> list = dao.getRoParts(id);
					for (int j = 0; j < list.size(); j++) {
						if (list.get(j).get("PART_TYPE_ID") == null) {
						}
						TtAsRoOldRepairPartPO tmpRepairePartPO = new TtAsRoOldRepairPartPO();
						tmpRepairePartPO.setCreateBy(loginUser.getUserId());
						tmpRepairePartPO.setCreateDate(new Date());
						tmpRepairePartPO.setDetailId(Long.parseLong(SequenceManager.getSequence("")));
						tmpRepairePartPO.setPartCode(list.get(j).get("PART_NO"));
						tmpRepairePartPO.setPartName(list.get(j).get("PART_NAME"));
						tmpRepairePartPO.setRoId(Long.parseLong(id));
						Object ob = list.get(j).get("REAL_PART_ID");
						tmpRepairePartPO.setPartId(Long.parseLong(ob.toString()));
						tmpRepairePartPO.setRepairtypecode(Long.parseLong(list.get(j).get("REPAIRTYPECODE")));
						ob = list.get(j).get("PAY_TYPE");
						tmpRepairePartPO.setPayType(Long.parseLong(ob.toString()));
						ob = list.get(j).get("REPAIR_PART_ID");
						tmpRepairePartPO.setRepairPartId(Long.parseLong(ob.toString()));
						dao.insert(tmpRepairePartPO);
					}
				
			}else if(checkStatus.equals("2")){//审核驳回/工单处于结算状态
				ro_status = Constant.RO_STATUS_02;
			}
			if(ro_status==0){
				throw new UserException("审核参数丢失，请刷新页面再试!");
			}
			TtAsRepairOrderPO tarop = new TtAsRepairOrderPO();
			tarop.setId(Utility.getLong(id));
			TtAsRepairOrderPO taropUp = new TtAsRepairOrderPO();
			taropUp.setId(Utility.getLong(id));
			taropUp.setRoStatus(ro_status); // 待审核
			dao.update(tarop, taropUp);
			TtAsCancelBalanceRecordPO cbRPO = new TtAsCancelBalanceRecordPO();
			cbRPO.setCreateBy(loginUser.getUserId());
			cbRPO.setCreateDate(new Date());
			cbRPO.setRecordId(Long.parseLong(SequenceManager.getSequence("")));
			cbRPO.setRecordStatus(ro_status);
			cbRPO.setRoId(Long.parseLong(id));
			cbRPO.setRoNo(roNo);
			//审核日志
			cbRPO.setRecordType(Long.parseLong(Constant.IF_TYPE_YES.toString()));
			dao.insert(cbRPO);
			act.setOutData("info", "工单取消结算审核成功!");
			act.setForword(initAuditUrl);
		}catch (Exception e) {
			BizException e1 = new BizException(act,e,ErrorCodeConstant.QUERY_FAILURE_CODE,"工单取消结算页面");
			logger.error(loginUser,e1);
			act.setException(e1);
		}
	}
}
