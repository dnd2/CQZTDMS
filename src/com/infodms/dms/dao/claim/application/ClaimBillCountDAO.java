package com.infodms.dms.dao.claim.application;

import java.sql.ResultSet;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.common.getCompanyId.GetOemcompanyId;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.po.TtAsWrApplicationExtPO;
import com.infodms.dms.po.TtAsWrApplicationPO;
import com.infodms.dms.po.TtAsWrApppaymentPO;
import com.infodms.dms.po.TtAsWrBalancePO;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;
/**
 * 
* @ClassName: ClaimBillCountDAO 
* @Description: TODO(索赔但结算DAO) 
* @author wangchao 
* @date Jun 25, 2010 4:25:42 PM 
*
 */
public class ClaimBillCountDAO extends BaseDao {

	public static Logger logger = Logger.getLogger(ClaimBillCountDAO.class);
	private static final ClaimBillCountDAO dao = new ClaimBillCountDAO();

	public static final ClaimBillCountDAO getInstance() {
		return dao;
	}

	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 
	 * @Title: query
	 * @Description: TODO(查询要结算的索赔单)
	 * @param
	 * @return 设定文件
	 * @return PageResult 返回类型
	 * @throws
	 */
	public PageResult<TtAsWrApplicationExtPO> query(Map<String, String> map,
			int pageSize, int curPage) {
		StringBuffer sql = new StringBuffer();
		StringBuffer con1 = new StringBuffer();
		StringBuffer con2 = new StringBuffer();
		List params = new LinkedList();
		sql.append(" select convertstr('id','");
		
		
		con2.append(" tt_as_wr_application t ");
		con2.append("  left outer join tm_dealer d on t.dealer_id=d.dealer_id ");
		con1.append(" where 1=1 \n");
		con2.append(" where 1=1 ");
		if (Utility.testString(map.get("dealerCode"))) {
			con1.append(Utility.getConSqlByParamForEqual(map.get("dealerCode"), params, "d", "dealer_code"));
			String[] dealerCodes = map.get("dealerCode").split(",");
			StringBuffer dealerSb = new StringBuffer();
			for (int i=0;i<dealerCodes.length;i++) {
				dealerSb.append(",''");
				dealerSb.append(dealerCodes[i]);
				dealerSb.append("''");
			}
			String dcon = dealerSb.toString();
			dcon = dcon.replaceFirst(",", "");
			con2.append("and d.dealer_code in ("+dcon+") ");
			//sql.append(Utility.getConSqlByParamForEqual(map.get("dealerCode"), params, "d", "dealer_code"));
		}
		if (Utility.testString(map.get("yieldly"))) {
			con1.append(" and T.YIELDLY ="+map.get("yieldly")+" ");
			con2.append(" and T.YIELDLY ="+map.get("yieldly")+" ");
		}
		if (Utility.testString(map.get("dealerName"))) {
			con1.append(" and d.dealer_name like '%"+map.get("dealerName")+"%'");
			con2.append(" and d.dealer_name like ''%"+map.get("dealerName")+"%''");
		}
		if (Utility.testString(map.get("claimType"))) {
			con1.append(" and t.claim_type=?");
			params.add(map.get("claimType"));
			con2.append(" and t.claim_type="+map.get("claimType")+" ");
		}	
		if (Utility.testString(map.get("endDate"))) {
			con1.append(" and t.create_date<=to_date('"+map.get("endDate")+ "', 'yyyy-mm-dd hh24:mi:ss') ");
			con2.append(" and t.create_date<=to_date(''"+map.get("endDate")+ "'', ''yyyy-mm-dd hh24:mi:ss'') ");
			//params.add(map.get("endDate"));
		}
		con1.append(" and t.status = ?");
		params.add(Constant.CLAIM_APPLY_ORD_TYPE_04);
		con2.append(" and t.status ="+Constant.CLAIM_APPLY_ORD_TYPE_04+" ");
		sql.append(con2);
		sql.append("',',') as ids,t.dealer_id as dealer_id,t.yieldly as yieldly,d.dealer_shortname as dealer_shortname,d.dealer_code as dealer_code,min(claim_no) as start_claim,max(claim_no) as end_claim,count(*) as counts,sum(LABOUR_AMOUNT) as labour_amounts,sum(PART_AMOUNT) as  part_amounts,sum(NETITEM_AMOUNT) as netitem_amounts,sum(REPAIR_TOTAL) as repair_totals,sum(TAX_SUM) as tax_sums,sum(GROSS_CREDIT) as gross_credits \n");
		sql.append( " from tt_as_wr_application t \n");
		sql
				.append(" left outer join tm_dealer d on t.dealer_id=d.dealer_id \n");
		sql.append(con1);
		sql.append(" group by dealer_shortname,dealer_code,t.dealer_id,yieldly ");
		PageResult<TtAsWrApplicationExtPO> ps = pageQuery(
				TtAsWrApplicationExtPO.class, sql.toString(), params, pageSize,
				curPage);
		return ps;
	}
	
	/**
	 * 
	 * @Title: query
	 * @Description: TODO(查询要结算的索赔单ID)
	 * @param
	 * @return 设定文件
	 * @return PageResult 返回类型
	 * @throws
	 */
	public List<TtAsWrApplicationPO> queryId(Map<String, String> map) {
		StringBuffer sql = new StringBuffer();
		List params = new LinkedList();
		sql
				.append(" select t.id \n"
						+ " from tt_as_wr_application t \n");
		sql
				.append(" left outer join tm_dealer d on t.dealer_id=d.dealer_id \n");
		sql.append(" where 1=1 \n");
		if (Utility.testString(map.get("dealerCode"))) {
			sql.append(Utility.getConSqlByParamForEqual(map.get("dealerCode"), params, "d", "dealer_code"));
		}
		if (Utility.testString(map.get("dealerName"))) {
			sql.append(" and d.dealer_name like '%"+map.get("dealerName")+"%'");
		}
		if (Utility.testString(map.get("claimType"))) {
			sql.append(" and t.claim_type=?");
			params.add(map.get("claimType"));
		}	
		if (Utility.testString(map.get("endDate"))) {
			sql.append(" and t.create_date<=to_date('"+map.get("endDate")+ "', 'yyyy-mm-dd hh24:mi:ss') ");
			//params.add(map.get("endDate"));
		}
		sql.append(" and t.status <> ?");
		params.add(Constant.CLAIM_APPLY_ORD_TYPE_07);
		List<TtAsWrApplicationPO> ps = select(
				TtAsWrApplicationPO.class, sql.toString(), params);
		return ps;
	}
	/**
	 * 
	* @Title: count 
	* @Description: TODO(申请单结算) 
	* @param @param ls //要更新的索赔单ID
	* @param @param dealerIds // 要插入的经销商 TT_AS_WR_BALANCE
	* @param @param fees //要插入的结算费用 TT_AS_WR_BALANCE
	* @param @param user    设定文件 
	* @return void    返回类型 
	* @throws
	 */
	public void count(String[] ls,String[] dealerIds,String[] fees,String[] yieldly,AclUserBean user) {
		Date date = new Date();
		Long companyId=GetOemcompanyId.getOemCompanyId(user);    
		try{
		//更新TT_AS_WR_APPLICATION
		if (ls!=null) {
			if (ls.length>0) {
				for (int i = 0;i<ls.length;i++) {
					TtAsWrApplicationPO t1 = new TtAsWrApplicationPO();
					t1.setId(Utility.getLong(ls[i]));
					TtAsWrApplicationPO tu = new TtAsWrApplicationPO();
					tu.setId(Utility.getLong(ls[i]));
					tu.setStatus(Constant.CLAIM_APPLY_ORD_TYPE_07);
					tu.setUpdateBy(user.getUserId());
					tu.setUpdateDate(date);
					update(t1, tu);
					//插入到TT_AS_WR_APPPAYMENT
					TtAsWrApppaymentPO tabp = new TtAsWrApppaymentPO();
					List<TtAsWrApplicationPO> la = select(t1); //根据索赔单ID查询PO
					if (la!=null) {
						if (la.size()>0) {
							t1 = la.get(0);
							tabp.setId(Utility.getLong(SequenceManager.getSequence("")));//索赔序列号
							tabp.setClaimId(t1.getId());
							tabp.setRoNo(t1.getRoNo());
							tabp.setLineNo(t1.getLineNo().longValue());
							tabp.setDealerId(t1.getDealerId());
							tabp.setClaimType(t1.getClaimType());
							tabp.setPartsAmount(t1.getPartAmount());
							tabp.setLabourAmount(t1.getStdLabourAmount());
							tabp.setOtherlabourAmount(t1.getReinLabourAmount());
							tabp.setOtheritemAmount(t1.getNetitemAmount());
							tabp.setLinetotal(t1.getRepairTotal());
							tabp.setGrossCredit(t1.getGrossCredit());
							tabp.setCreateBy(user.getUserId());
							tabp.setCreateDate(date);
							insert(tabp);
						}
					}
				}
			}
		}
		//插入到TT_AS_WR_BALANCE
		if (dealerIds!=null) {
			for (int i=0;i<dealerIds.length;i++) {
				TtAsWrBalancePO tabp = new TtAsWrBalancePO();
				tabp.setId(Utility.getLong(SequenceManager.getSequence("")));
				tabp.setProcFactory(yieldly[i]);
				tabp.setBalanceNo(SequenceManager.getSequence("BO"));
				tabp.setDealerId(Utility.getLong(dealerIds[i]));
				tabp.setBalance(Utility.getDouble(fees[i]));
				tabp.setOemCompanyId(companyId);
				tabp.setBalanceDate(date);
				tabp.setCreateBy(user.getUserId());
				tabp.setCreateDate(date);
				insert(tabp);
			}
		}
		
		
		}catch(Exception e) {
			e.printStackTrace();
		}
		
	}

}
