package com.infodms.dms.actions.repairOrder;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.log4j.Logger;

import com.infodms.dms.actions.claim.auditing.rule.custom.AuditingVO;
import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.claim.auditing.AutoAuditingDao;
import com.infodms.dms.po.TmDealerPO;
import com.infodms.dms.po.TmVehiclePO;
import com.infodms.dms.po.TtAsRepairOrderPO;
import com.infodms.dms.po.TtAsRoRepairPartPO;
import com.infodms.dms.po.TtAsWrForeapprovalPO;
import com.infodms.dms.po.TtAsWrVinRepairDaysPO;
import com.infoservice.po3.core.context.POContext;

public class RoRuleAudit {
	private Logger logger = Logger.getLogger(RoRuleAudit.class);
	private Long foreId;
	private Long roId;
	public RoRuleAudit(Long foreId,Long roId){
		this.foreId = foreId;
		this.roId=roId;
	}
	
	@SuppressWarnings("unchecked")
	public void run() throws Exception{
		logger.info("[预授权ID：" + this.foreId + "][工单ID："+this.roId+"]预授权自动审核开始==>");
		
		try{	
			//加载预授权申请单信息
			RoOrderVO orderVO = this.loadRoOrder();
			
			
			if(orderVO.getForePo()==null&&orderVO.getRepaitPo()==null){
				logger.error("[预授权ID：" + this.foreId + "][工单ID："+this.roId+"] 均未查询到该预授权申请单信息!");
				return;
		    }		
			System.out.println(this.roId+",,,,规则开始工作咯！！！！");
			AutoAuditingDao dao = AutoAuditingDao.getInstance();
			RuleMonitorAuditing ruleAuditing = new RuleMonitorAuditing();
			AuditingVO auditingVO = ruleAuditing.deal(orderVO);
			TreeSet<Integer> roles = new TreeSet<Integer>();
			StringBuilder sbuilder = new StringBuilder();
			this.dealRole(roles, auditingVO);
			this.dealReason(sbuilder, auditingVO);
			String role = this.getRoleStr(roles);
			
			if(role!=null && !"".equals(role)){//如果需授权角色不为空,说明需要预授权,则将预授权角色加载到预授权申请
				System.out.println("验证结果为：：：："+role);
				if(role.substring(role.length()-1, role.length()).equals(","))
				{
					role = role.substring(0,role.length()-1 );
				}
				String sql = "update tt_AS_repair_order o set o.Customer_Desc = decode(o.Customer_Desc,null,'',o.Customer_Desc)||','|| '"+role+"' where o.id="+this.roId;
//				TtAsRepairOrderPO op = new TtAsRepairOrderPO();
//				TtAsRepairOrderPO op2 = new TtAsRepairOrderPO();
//				op.setId(this.roId);
//				op2.setCustomerDesc(role);
//				dao.update(op, op2);
				dao.update(sql, null);
			}
		} catch (Exception e) {
			logger.error("自动审核规则验证错误,预授权ID="+this.foreId);
			logger.error(e);
			POContext.endTxn(false);
			e.printStackTrace();
			throw e;
		}
		logger.info("[预授权ID：" + this.foreId + "]预授权自动审核结束==<");
	}
	
	/**
	 * 将集合中的角色按顺序拼成字符串
	 * 格式：角色1+","+角色2+","+ ...
	 * @param roles
	 * @return
	 */
	private String getRoleStr(TreeSet<Integer> roles){
		String role = "";
		if(roles!=null && roles.size()>0){
			Iterator<Integer> iter = roles.iterator();
			while(iter.hasNext()){//如果存在100则拒绝该工单
				Integer tempRole = iter.next();
				role = role + tempRole + ",";
			}
		}
		return role;
	}
	
	/**
	 * 将某一规则需要审核的角色，放入到审核角色容器中
	 * @param set 审核角色容器
	 * @param auditingVO 审核需要角色和原因（某一规则）
	 */
	private void dealRole(TreeSet<Integer> set,AuditingVO auditingVO){
		if(auditingVO!=null && auditingVO.getRoles()!=null){
			SortedSet<String> ss =auditingVO.rolesToArrays();
			Iterator<String> iter = ss.iterator();
			while(iter.hasNext()){
				try{
					Integer role = Integer.parseInt(iter.next());
					if(!set.contains(role))
						set.add(role);
				}catch(Exception e){
				}
			}
		}
	}
	
	/**
	 * 将某一规则需要审核的原因，放入到审核角色容器中
	 * @param sbuilder 审核原因容器
	 * @param auditingVO 审核需要角色和原因（某一规则）
	 */
	private void dealReason(StringBuilder sbuilder,AuditingVO auditingVO){
		if(auditingVO!=null && auditingVO.getReasions()!=null && !"".equals(auditingVO.getReasions())){
			sbuilder.append(auditingVO.getReasions()).append(" \n");
		}
	}
	
	/**
	 * 加载相关信息
	 * @return ClaimOrderVO
	 */
	@SuppressWarnings("unchecked")
	private RoOrderVO loadRoOrder() throws Exception{
		
		RoOrderVO result = new RoOrderVO();
		String typeCode = null;
		//加载预授权申请单信息 1
		AutoAuditingDao  dao  = AutoAuditingDao.getInstance();
		TtAsWrForeapprovalPO forePo = new TtAsWrForeapprovalPO();
		if(this.foreId!=null){
			 forePo = dao.queryForeById(this.foreId);
			if(forePo!=null){//没有查询到预授权申请单则返回
				result.setForePo(forePo);
				typeCode = forePo.getApprovalType().toString();
				}
		}
		
		
		logger.info("加载预授权结束"+this.foreId);
		//加载工单信息
		TtAsRepairOrderPO op = dao.queryROById(this.roId);
		if(op!=null){
			result.setRepaitPo(op);
			typeCode = op.getRepairTypeCode();
		}
		logger.info("加载工单结束"+this.roId);
		
		//如果 预授权申请单 和 工单 都为空了，则直接返回 结果，无需后续处理了
		if(forePo ==null &&  op ==null)
			return result;
		
		
		//加载车辆信息 1
		String vin = op.getVin();
		if(vin!=null && !"".equals(vin)){
			TmVehiclePO vehiclePO = dao.queryVehicle(vin);
			if(vehiclePO!=null){//存在对应车辆信息
				result.setVehiclePO(vehiclePO);
			}
		}
		logger.info("加载车子结束"+vin);
		//加载预授权申请单对应经销商信息和公司信息 1
		List<TmDealerPO> resList = dao.queryDealerById(op.getDealerId());
		if(resList!=null && resList.size()>0){
			TmDealerPO dealerPO = (TmDealerPO) resList.get(0);
			result.setDealerPO(dealerPO);
		}
		
		
		
		
		
		//加载配件在三包期内总费用
		Double warAmount=0.0;
		List<TtAsRoRepairPartPO> pList = dao.queryPartById(op.getRoNo());
		if(pList!=null&&pList.size()>0){//循环结算在三包期内的配件总金额
				for(int j=0;j<pList.size();j++){
					warAmount += ((TtAsRoRepairPartPO)pList.get(j)).getPartCostAmount();
				}
			result.setPartsList(pList);
		}
		
		logger.info("加载三包期费用::::"+warAmount);
		
		Double labourAmount  = dao.queryLabourById(op.getRoNo());
		logger.info("加载总工时费用::::"+labourAmount);
		/**
		 * 开始判断维修类型，以对应相应的预授权规则 ,由于现在规则费用都是针对所有类型,所以不在按照以前的规则进行工时和材料的判断
		 */
//		if(Constant.REPAIR_TYPE_06.equals(typeCode)||Constant.REPAIR_TYPE_03.equals(typeCode)){
//			if(warAmount==0.0&&labourAmount>0){//售前维修,特殊服务： 如果材料费>0  则管控材料费。 否则就管控工时费
//				result.setWarAmount(null);
//				result.setLabourAmount(labourAmount);
//			}else if(warAmount>0.0){
//				result.setLabourAmount(null);
//				result.setWarAmount(warAmount);
//			}
//		}else if(Constant.REPAIR_TYPE_01.equals(typeCode)){//如果是正常维修，当材料费为0时，管控工时，  否则 材料费，工时费 均不管控
//			if(warAmount==0.0&&labourAmount>0){
//				result.setWarAmount(null);
//				result.setLabourAmount(labourAmount);
//			}else if(warAmount>0.0){
//				result.setWarAmount(null);
//				result.setLabourAmount(null);
//			}
//		}
		result.setWarAmount(warAmount);
		result.setLabourAmount(labourAmount);
		
		
		//加载累计维修天数
		Integer days = null;
		TtAsWrVinRepairDaysPO dp = new TtAsWrVinRepairDaysPO();
		dp.setVin(op.getVin());
		List<TtAsWrVinRepairDaysPO> dList = dao.select(dp);
		if(dList!=null&&dList.size()>0){
			days = dList.get(0).getCurDays();
		}
		result.setRepairDays(days);
		logger.info("加载维修天数::::"+days);
		//加载结束
		
		//加载维修总费用
		//总费用目前只针对外派有效(包括：工时费,材料费,外出其他费)
		Double repairTotal  = dao.queryRepirAllById(op.getId());
//		if(repairTotal==0.0){
//			result.setRepairTotal(null);
//		}else if(Constant.REPAIR_TYPE_02.equals(typeCode)) {
//			result.setRepairTotal(repairTotal);
//		}else{
//			result.setRepairTotal(null);
//		}
		/**
		 * 总费用也一样,针对所有类型
		 */
		if(repairTotal==0.0){
			result.setRepairTotal(null);
		}else  {
			result.setRepairTotal(repairTotal);
		}
		
		logger.info("加载总维修费用::::"+repairTotal);
		return result;
	}
}
