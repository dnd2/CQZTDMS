package com.infodms.dms.dao.claim.auditing;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.infodms.dms.actions.claim.auditing.rule.custom.RuleVO;
import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.po.TcCodePO;
import com.infodms.dms.po.TmDealerPO;
import com.infodms.dms.po.TmPtPartBasePO;
import com.infodms.dms.po.TmVehiclePO;
import com.infodms.dms.po.TtAsRepairOrderPO;
import com.infodms.dms.po.TtAsRoAddItemPO;
import com.infodms.dms.po.TtAsRoLabourPO;
import com.infodms.dms.po.TtAsRoRepairPartPO;
import com.infodms.dms.po.TtAsWrForeapprovalPO;
import com.infodms.dms.po.TtAsWrRuleitemPO;
import com.infodms.dms.po.TtAsWrRulemappingPO;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("unchecked")
public class AutoAuditingDao extends BaseDao {
	private static AutoAuditingDao auditiongDao;
	
	private AutoAuditingDao(){
		super();
	}
	public static AutoAuditingDao getInstance(){
		if(auditiongDao==null){
			auditiongDao = new AutoAuditingDao();
		}
		return auditiongDao;
	}
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		return null;
	}
//根据VIN 判断 当天的开工单数量
	public List<Map<String,Object>> getList(String vin,Long roId ,String roStartdate){
		StringBuffer sql = new StringBuffer();
		sql.append("select *\n");
		sql.append(" from tt_as_repair_order o\n");
		sql.append(" where  1=1\n");
		sql.append(" and o.vin ='"+vin+"'\n");
		sql.append(" and o.approval_yn=1\n"); 
		sql.append("and  o.ro_create_date >= to_date('"+roStartdate+" 00:00:00','yyyy-mm-dd hh24:mi:ss')\n");
		sql.append("and  o.ro_create_date <= to_date('"+roStartdate+" 23:59:59','yyyy-mm-dd hh24:mi:ss')"); 

		if(roId!=null&&roId!=0){
		sql.append(" and o.id!="+roId);
		}
		return this.pageQuery(sql.toString(), null, this.getFunName());
	}
	// 如果是DQV认证经销商，预授权直接通过
	public void updateOrder(String ro_no){
		StringBuffer sql = new StringBuffer();
		sql.append("update TT_AS_REPAIR_ORDER ro set ro.forl_status="+Constant.RO_FORE_02+" where ro_no='"+ro_no+"'");
		factory.update(sql.toString(), null);
	}
	// 更新预授权申请单状态
	public void updateTtAsWrForeapproval2(String fid,String opintion){
		StringBuffer sql = new StringBuffer();
		sql.append("update tt_as_wr_foreapproval wf set wf.report_status="+Constant.RO_FORE_02+",wf.update_date=sysdate ,WF.AUDIT_PERSON='自动审核',wf.opinion='DQV认证经销商,直接通过' where id="+fid);
		factory.update(sql.toString(), null);
	}
	//根据预授权ID加载预授权申请明细
	public TtAsWrForeapprovalPO queryForeById(Long claimId) throws Exception{
		
		TtAsWrForeapprovalPO resultPO = null;
		TtAsWrForeapprovalPO conditionPO = new TtAsWrForeapprovalPO();
		if(claimId==null) throw new Exception("不能为空");
		conditionPO.setId(claimId);
		
		List<PO> resultList = this.select(conditionPO);
		
		if(resultList!=null && resultList.size()>0){
			resultPO = (TtAsWrForeapprovalPO) resultList.get(0);
		}
		
		return resultPO;
	}
	//根据预授权ID加载预授权申请明细工单
	public TtAsRepairOrderPO queryROById(Long roId){
		
		TtAsRepairOrderPO resultPO = null;
		TtAsRepairOrderPO conditionPO = new TtAsRepairOrderPO();
		conditionPO.setId(roId);
		
		List<PO> resultList = this.select(conditionPO);
		
		if(resultList!=null && resultList.size()>0){
			resultPO = (TtAsRepairOrderPO) resultList.get(0);
		}
		
		return resultPO;
	}
	 /**
     * 根据VIN查询车辆信息
     * @param vin 车辆唯一标识码
     * @return TmVehiclePO
     */
    public TmVehiclePO queryVehicle(String vin){
    	
    	TmVehiclePO paramPO = new TmVehiclePO();
    	paramPO.setVin(vin);
    	try {
    		CommonUtils.jugeVinNull(vin);
		} catch (Exception e) {
			// TODO: handle exception
		}
    	
    	
    	List<PO> resList = this.select(paramPO);
    	
    	TmVehiclePO resultPO = null;
    	
    	if(resList!=null && resList.size()>0){
    		resultPO = (TmVehiclePO) resList.get(0);
    	}
    	
    	return resultPO;
    }
    /**
     * 查询指定状态的经销商是否存在
     * @param dealerId 经销商ID
     * @return List<PO>
     */
    public List<TmDealerPO> queryDealerById(Long dealerId){
    	
    	TmDealerPO dealerPO = new TmDealerPO();
    	dealerPO.setDealerId(dealerId);
    	
    	return this.select(dealerPO);
    }
    
	 /**
     * 
     */
    public Double queryLabourById(String roNo){
    	Double labourAmount = 0.0;
    	TtAsRoLabourPO lp = new TtAsRoLabourPO();
    	TtAsRepairOrderPO op = new TtAsRepairOrderPO();
    	op.setRoNo(roNo);
    	List<TtAsRepairOrderPO> lList = this.select(op);
    	if(lList!=null&& lList.size()>0){
    		lp.setRoId(lList.get(0).getId());
    		lp.setPayType(Constant.PAY_TYPE_02);
    		List<TtAsRoLabourPO> list = this.select(lp);
    		if(list!=null&&list.size()>0){
    			for(int i=0;i<list.size();i++){
    				labourAmount += list.get(i).getLabourAmount();
    			}
    		}
    	}
    	return labourAmount;
    }
    public Double queryRepirAllById(Long id ){
    	Double labourAmount = 0.0;//工时费
    	Double partAmount   = 0.0;//材料费
    	Double otherAmount  = 0.0;//其他费用
    	TtAsRoLabourPO lp = new TtAsRoLabourPO();
    	TtAsRoRepairPartPO pp = new TtAsRoRepairPartPO();
    	TtAsRoAddItemPO ip = new TtAsRoAddItemPO();
    	lp.setRoId(id);
    	lp.setPayType(Constant.PAY_TYPE_02);
    	pp.setRoId(id);
    	pp.setPayType(Constant.PAY_TYPE_02);
    	ip.setRoId(id);
    	ip.setPayType(Constant.PAY_TYPE_02);
    		List<TtAsRoLabourPO> list = this.select(lp);
    		if(list!=null&&list.size()>0){
    			for(int i=0;i<list.size();i++){
    				labourAmount += list.get(i).getLabourAmount();
    			}
    			System.out.println("工时费："+labourAmount);
    		}
    		List<TtAsRoRepairPartPO> pList = this.select(pp);
    		if(pList!=null&&pList.size()>0){
    			for(int i=0;i<pList.size();i++){
    				partAmount+=pList.get(i).getPartCostAmount();
    			}
    			System.out.println("材料费："+partAmount);
    		}
    		List<TtAsRoAddItemPO> iList = this.select(ip);
    		if(iList!=null&&iList.size()>0){
    			for(int i=0;i<iList.size();i++){
    				otherAmount+=iList.get(i).getAddItemAmount();
    			}
    			System.out.println("其他费："+otherAmount);
    		}
    	
    	return labourAmount+partAmount+otherAmount;
    }
    /**
     * 得到所有索赔的配件
     */
  public List<TtAsRoRepairPartPO> queryPartById(String roNo){
	  	TtAsRoRepairPartPO pp = new TtAsRoRepairPartPO();
	  	TtAsRepairOrderPO op = new TtAsRepairOrderPO();
  		op.setRoNo(roNo);
  		List<TtAsRepairOrderPO> oList = this.select(op);
    	if(oList!=null&&oList.size()>0){
    		pp.setRoId(oList.get(0).getId());
    		pp.setPayType(Constant.PAY_TYPE_02);
    		return this.select(pp);
    	}
    	return null;
    }
  /**
   * 查询授权规则明细（将对应规则的条件明细封装到授权规则对象中）
   * @return Map<String,RuleVO>
   */
  public Map<String,RuleVO> loadAuthRuleDetail(String type){
  	
  	Map<String,RuleVO> resultMap = new HashMap<String, RuleVO>();//存在规则
  	
  	//查询授权规则信息
  	List<PO> ruleList = this.queryAuthRule(type);
  	
  	if(ruleList==null || ruleList.size()<=0)
  		return resultMap;
  	//查询授权规则对应明细信息
  	List<TtAsWrRuleitemPO> ruleItemList = this.queryAuthRuleItem(type);
  	
  	if(ruleItemList==null || ruleItemList.size()<=0)
  		return resultMap;
  	
  	Map<String,Map<Integer,TtAsWrRuleitemPO>> tempMap = new HashMap<String, Map<Integer,TtAsWrRuleitemPO>>();
  	//将规则对应的条件明细封装到对应规则下
  	for (TtAsWrRuleitemPO ttAsWrRuleitemPO : ruleItemList) {
			String roNO = ttAsWrRuleitemPO.getRuleNo().toString();//规则号
			Map<Integer,TtAsWrRuleitemPO> temp = null;//存在规则明细
			if(tempMap.containsKey(roNO)){
				temp = tempMap.get(roNO);
			}else{
				temp = new TreeMap<Integer, TtAsWrRuleitemPO>();//要求带排序
				tempMap.put(roNO, temp);
			}
			temp.put(ttAsWrRuleitemPO.getElementPosition(), ttAsWrRuleitemPO);
		}
  //将规则同条件明细封装到一起
	for (PO po : ruleList) {
		TtAsWrRulemappingPO rulePO = (TtAsWrRulemappingPO)po;
		String roNO = rulePO.getRuleElement().toString();
		RuleVO ruleVO = new RuleVO();
		ruleVO.setRuleVO(rulePO);
		if(tempMap.containsKey(roNO))
			ruleVO.setConditiongMap(tempMap.get(roNO));
		
		resultMap.put(rulePO.getRuleElement().toString(), ruleVO);
	}
	
	return resultMap;
}
  /**
   * 查询授权规则
   * 注：结算审核授权规则不关联车型组
   * @param wrGroupId 车型组ID
   * @param companyId 所属公司ID区分轿车和微车
   * @param type 0 技术授权 1 结算授权
   * @return List<PO>
   */
  public List<PO> queryAuthRule(String type){
  	
  	//查询规则信息
  	TtAsWrRulemappingPO conditionPO = new TtAsWrRulemappingPO();
  	conditionPO.setType(CommonUtils.checkNull(type));
  	
  	List<PO> ruleList = this.select(conditionPO);
  	return ruleList;
  }
  /**
   * 查询授权规则对应条件明细
   * 注：结算审核授权规则不关联车型组
   * @param wrGroupId 车型组ID
   * @param companyId 所属公司ID区分轿车和微车
   * @param type 0 技术授权 1 结算授权
   * @return List<TtAsWrRuleitemPO>
   */
  public List<TtAsWrRuleitemPO> queryAuthRuleItem(String type){
  	
  	//查询规则对应条件信息
  	String sql = "SELECT B.* \n"+
						" FROM TT_AS_WR_RULEMAPPING A,TT_AS_WR_RULEITEM B\n"+
						" WHERE 1=1\n"+
						" AND A.RULE_ELEMENT = B.RULE_NO\n"+
						" AND A.TYPE = ?\n" +
						" ORDER BY B.RULE_NO,B.ELEMENT_POSITION";
  	
  	List<Object> paramList = new ArrayList<Object>();
  	paramList.add(CommonUtils.checkNull(type));
  	List<TtAsWrRuleitemPO> ruleItemList = this.select(TtAsWrRuleitemPO.class, sql, paramList);
  	
  	return ruleItemList;
  }
  /**
   * 加载 授权项
   * 授权项 为授权规则维护中 授权项信息
   * @param type 对应TC_CODE中的类别
   * @return List<PO> TcCodePO
   */
  public List<PO> loadAuthItem(String type){
  	
  	TcCodePO conditionPO = new TcCodePO();
  	conditionPO.setType(type);
  	conditionPO.setStatus(10011001);
  	return this.select(conditionPO);
  }
  /**
   * 查询授权项对应比较符信息
   * @param typeList 比较符类别
   * @return Map<String,String> 
   *         KEY:TC_CODE.CODE_ID
   *         VALUE:TC_CODE.CODE_DESC
   */
  public Map<String,String> loadCompareOperator(List<Integer> typeList){
  	
  	Map<String,String> resMap = new HashMap<String, String>();
  	if(typeList==null || typeList.size()<=0)
  		return resMap;
  	
  	for (Integer type : typeList) {
			List<PO> coList = this.loadAuthItem(type.toString());
			if(coList!=null && coList.size()>0){
				for (PO po : coList) {
					TcCodePO codePO = (TcCodePO) po;
					resMap.put(codePO.getCodeId(), codePO.getCodeDesc());
				}
			}
		}
  	
  	return resMap;
  }
  /**
   * 判断是否是主要配件
   */
  public boolean isMainPart(String partCode){
	  StringBuffer sql = new StringBuffer();
		sql.append("SELECT * FROM tm_pt_part_base t where t.pos_id is not null and t.part_code='"+partCode+"'");
		List<TmPtPartBasePO> list = this.select(TmPtPartBasePO.class,sql.toString(),null);
		if(list!=null&&list.size()>0){
			return true;
		}
	  return false;
  }
}
