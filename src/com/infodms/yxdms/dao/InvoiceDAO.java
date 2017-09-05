package com.infodms.yxdms.dao;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Arith;
import com.infodms.dms.po.FsFileuploadPO;
import com.infodms.dms.po.TtAsPaymentPO;
import com.infodms.dms.po.TtAsWrApplicationPO;
import com.infodms.dms.po.TtAsWrClaimBalancePO;
import com.infodms.yxdms.entity.ysq.TtAsComRecordPO;
import com.infodms.yxdms.utils.BaseUtils;
import com.infodms.yxdms.utils.DaoFactory;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("rawtypes")
public class InvoiceDAO extends IBaseDao{
	
	private static InvoiceDAO dao = new InvoiceDAO();
	public static final InvoiceDAO getInstance(){
		dao = (dao==null)?new InvoiceDAO():dao;
		return dao;
	}
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		
		return null;
	}

	@SuppressWarnings("unchecked")
	public Double findPdiAndKeepFit(String no) {
		StringBuffer sb= new StringBuffer();
		sb.append("select nvl(sum(a.balance_amount), 0) as balance_amount\n" );
		sb.append("  from Tt_As_Wr_Application a\n" );
		sb.append(" where a.status = 10791007\n" );
		sb.append("   and a.is_invoice = 1\n" );
		sb.append("   and a.is_import = 10041002\n" );
		sb.append("   and a.claim_type in (10661011, 10661002)\n" );
		sb.append(" and  a.balance_no ='"+no+"'");
		List<TtAsWrApplicationPO> list = this.select(TtAsWrApplicationPO.class, sb.toString(), null);
		Double balanceAmount = list.get(0).getBalanceAmount();
		return balanceAmount;
	}

	@SuppressWarnings("unchecked")
	public Double findPartAndAcc(String no) {
		StringBuffer sb= new StringBuffer();
		sb.append("select nvl(sum(a.balance_part_amount), 0) as balance_part_amount,\n" );
		sb.append("       nvl(sum(a.accessories_price), 0) as accessories_price\n" );
		sb.append("  from Tt_As_Wr_Application a\n" );
		sb.append(" where a.status = 10791007\n" );
		sb.append("   and a.is_invoice = 1\n" );
		sb.append("   and a.is_import = 10041002\n" );
		sb.append("   and a.claim_type not in (10661011, 10661002)\n" );
		sb.append(" and a.balance_no ='"+no+"'");
		List<TtAsWrApplicationPO> list = this.select(TtAsWrApplicationPO.class, sb.toString(), null);
		if(list!=null && list.size()>0){
			Double balancePartAmount = list.get(0).getBalancePartAmount();
			Double accessoriesPrice = list.get(0).getAccessoriesPrice();
			return Arith.add(balancePartAmount,accessoriesPrice);
		}else{
			return 0.0d;
		}
	}

	@SuppressWarnings("unchecked")
	public Double findAll(String no) {
		StringBuffer sb= new StringBuffer();
		sb.append("select nvl(sum(a.AMOUNT_SUM), 0) as AMOUNT_SUM from tt_as_wr_claim_balance a where 1=1 \n" );
		sb.append(" and a.remark ='"+no+"'");
		List<TtAsWrClaimBalancePO> list = this.select(TtAsWrClaimBalancePO.class, sb.toString(), null);
		Double balanceAmount = list.get(0).getAmountSum();
		return balanceAmount;
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> invoiceInfoSecondTimeByNo(String no) {
		StringBuffer sb= new StringBuffer();
		sb.append("select tas.id,tas.claim_id,taw.claim_no claim_no,to_char(tas.create_date,'yyyy-mm-dd') create_date,td.dealer_shortname,tas.dealer_code,tas.part_code,(select tc.code_desc from  tc_code tc where tc.code_id=tas.is_main_code) as is_main_code,tas.amount,tas.balance_no,tas.remark\n" );
		sb.append("  from tt_as_second_in_store_detail tas\n" );
		sb.append("  left join tt_as_wr_application taw\n" );
		sb.append("    on tas.claim_id = taw.id\n" );
		sb.append("  left join tm_dealer td\n" );
		sb.append("    on tas.dealer_id = td.dealer_id \n");
		sb.append("  WHERE 1 = 1\n" );
		sb.append(" and tas.balance_no ='"+no+"'");
		List<Map<String, Object>> list = this.pageQuery(sb.toString(), null, this.getFunName());
		return list;
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> returnShow(String no) {
		StringBuffer sb= new StringBuffer();
		sb.append("select distinct d.auth_price,r.balance_oder,d.return_no,(select c.code_desc from tc_code c where c.code_id=d.return_type) as return_type\n" );
		sb.append("            from tt_balance_return_relation r, tt_as_wr_old_returned d\n" );
		sb.append("           where d.id = r.id and r.balance_oder='"+no+"'");
		List<Map<String, Object>> list = this.pageQuery(sb.toString(), null, this.getFunName());
		return list;
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> findSpecialAmount(String dealerId,String startTime, String endTime, Long yieldly) {
		StringBuffer sb= new StringBuffer();
		//退换车要结算and  t.special_type=1
		sb.append("select * from tt_as_wr_special_apply t where 1=1  and t.status=20501005 \n");//退换车审核金额不结算
		DaoFactory.getsql(sb, "t.audit_date", startTime, 3);
		DaoFactory.getsql(sb, "t.audit_date", endTime, 4);
		DaoFactory.getsql(sb, "t.dealer_id", dealerId, 1);
		DaoFactory.getsql(sb, "t.yieldly", yieldly.toString(), 1);
		List<Map<String, Object>> list = this.pageQuery(sb.toString(), null, this.getFunName());
		return list;
	}
	@SuppressWarnings("unchecked")
	public Double findFJcount(String no) {
		StringBuffer sb= new StringBuffer();
		sb.append("select count(*) as count\n" );
		sb.append("  from (select distinct f.*\n" );
		sb.append("          from tt_balance_return_relation rr,\n" );
		sb.append("               tt_as_wr_old_returned      r,\n" );
		sb.append("               Tt_As_Wr_Claim_Balance     b,\n" );
		sb.append("               FS_FILEUPLOAD              f\n" );
		sb.append("         where rr.id = r.id\n" );
		sb.append("           and rr.id = f.ywzj\n" );
		sb.append("           and b.remark = rr.balance_oder ");
		sb.append(" and b.remark ='"+no+"'\n");
		sb.append("           )");
		Map<String, Object> pageQueryMap = this.pageQueryMap(sb.toString(), null, getFunName());
		BigDecimal object = (BigDecimal) pageQueryMap.get("COUNT");
		return object.doubleValue();
	}
	@SuppressWarnings("unchecked")
	public List<FsFileuploadPO> AppprintFJ(String balanecNo) {
		StringBuffer sb= new StringBuffer();
		sb.append("select distinct f.*\n" );
		sb.append("          from tt_balance_return_relation rr,\n" );
		sb.append("               tt_as_wr_old_returned      r,\n" );
		sb.append("               Tt_As_Wr_Claim_Balance     b,\n" );
		sb.append("               FS_FILEUPLOAD              f\n" );
		sb.append("         where rr.id = r.id\n" );
		sb.append("           and rr.id = f.ywzj\n" );
		sb.append("           and b.remark = rr.balance_oder  and b.remark ='"+balanecNo+"'\n");
		List<FsFileuploadPO> ls = new ArrayList<FsFileuploadPO>();
		ls = this.select(FsFileuploadPO.class, sb.toString(), null);
		return ls;
	}
	@SuppressWarnings("unchecked")
	public List<TtAsPaymentPO> queryPaymentByid(RequestWrapper request,AclUserBean loginUser) {
	   String balance_oder = 	DaoFactory.getParam(request, "balance_oder");
	   TtAsPaymentPO asPaymentPO = new TtAsPaymentPO();
	   asPaymentPO.setBalanceOder(balance_oder);
		return this.select(asPaymentPO);
	}
	@SuppressWarnings("unchecked")
	public int addsureRemarkPayment(RequestWrapper request,AclUserBean loginUser) {
		TtAsPaymentPO po = new TtAsPaymentPO();
		TtAsPaymentPO po1 = new TtAsPaymentPO();
		po.setBalanceOder(DaoFactory.getParam(request, "balance_oder"));
		List<TtAsPaymentPO> paymentPOs =  this.select(po);
	    String remark =	DaoFactory.getParam(request, "remark");
	    try {
			po1.setNotes(BaseUtils.checkNull(remark));
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
		int res =this.update(po, po1);//添加备注
		//插入日志
		TtAsPaymentPO TtAsPayment = paymentPOs.get(0);
	    Long TtAsPaymentid =	TtAsPayment.getId();
	    Integer type = loginUser.getUserType();
	    Integer status = TtAsPayment.getStatus();
	    TtAsComRecordPO recordPO = new TtAsComRecordPO();
	    recordPO.setId(DaoFactory.getPkId());
	    recordPO.setBizId(TtAsPaymentid);
	    recordPO.setCreateBy(loginUser.getUserId());
	    recordPO.setCreateDate(new Date());
	    recordPO.setRemark("添加备注");
	    recordPO.setStatus(Integer.valueOf(status.toString()));
	    recordPO.setRoleName(type.toString());
	    this.insert(recordPO);
		return res; 
	}
	@SuppressWarnings("unchecked")
	public String checkticeksByBalanceNo(RequestWrapper request) {
		String res ="";
		String start_date = DaoFactory.getParam(request, "START_DATE");
		String end_date = DaoFactory.getParam(request, "END_DATE");
		String dealer_id = DaoFactory.getParam(request, "id");
		StringBuffer sb= new StringBuffer();
		sb.append("select max(a.balance_no) as balance_no,sum(a.balance_amount) as balance_amount\n" );
		sb.append("  from Tt_As_Wr_Application a\n" );
		sb.append(" where 1=1 \n" );
		sb.append("   and a.status = 10791007\n" );
		sb.append("   and a.dealer_id = '"+dealer_id+"'\n" );
		sb.append("   and a.report_date >= to_date('"+start_date+"', 'yyyy-mm-dd')\n" );
		sb.append("   and a.report_date < to_date('"+end_date+"', 'yyyy-mm-dd') + 1");
		Map<String,Object> map = this.pageQueryMap(sb.toString(), null, getFunName());
		String balaceNo=BaseUtils.checkNull(map.get("BALANCE_NO"));
		
		
		//===================================================
		
		
		
		StringBuffer sql= new StringBuffer();
		sql.append("select sum(t.balance_amount) as balance_amount\n" );
		sql.append("  from (select sum(a.balance_amount) as balance_amount\n" );
		sql.append("          from Tt_As_Wr_Application a\n" );
		sql.append("         where a.claim_type not in (10661002, 10661006, 10661011)\n" );
		sql.append("           and a.status = 10791007\n" );
		DaoFactory.getsql(sql, "a.balance_no", balaceNo, 1);
		/*sql.append("        union all\n" );
		sql.append("        select nvl(sum(t.approval_amount), 0) as balance_amount\n" );
		sql.append("          from tt_as_wr_special_apply t\n" );
		sql.append("         where 1 = 1\n" );
//		sql.append("           and t.special_type = 1\n" );//放开退换车
		sql.append("           and t.status = 20501005\n" );
		DaoFactory.getsql(sql, "t.dealer_id", dealer_id, 1);
		sql.append("   and t.audit_date >= to_date('"+start_date+"', 'yyyy-mm-dd')\n" );
		sql.append("   and t.audit_date < to_date('"+end_date+"', 'yyyy-mm-dd') + 1");*/
		sql.append("        union all\n" );
		sql.append("  select 0-nvl(sum(a.balance_amount),0) as balance_amount\n" );
		sql.append("          from tt_as_wr_application_counter a\n" );
		sql.append("         where a.claim_type not in (10661002,10661011)\n" );
		sql.append("           and a.status = 10791007\n" );
		DaoFactory.getsql(sql, "a.balance_no", balaceNo, 1);
		sql.append("        union all\n" );
		sql.append("        select nvl(sum(d.amount), 0) as balance_amount\n" );
		sql.append("          from tt_as_second_in_store_detail d\n" );
		sql.append("         where 1=1 ");
		DaoFactory.getsql(sql, "d.balance_no", balaceNo, 1);
		sql.append(" ) t\n" );
		sql.append(" where 1 = 1");
		Map<String,Object> mapTemp = this.pageQueryMap(sql.toString(), null, getFunName());
		BigDecimal object = (BigDecimal) mapTemp.get("BALANCE_AMOUNT");
		Double balance_amount = object.doubleValue();
		
		
		
		StringBuffer sb1= new StringBuffer();
		sb1.append("select sum(t.balance_amount-t.APPEND_AMOUNT) as balance_amount  from (\n" );//APPEND_AMOUNT为反索赔费用，需扣除
		sb1.append("select sum(a.balance_amount) as balance_amount, 0 as APPEND_AMOUNT\n" );
		sb1.append("  from Tt_As_Wr_Application a\n" );
		sb1.append(" where a.claim_type not in (10661002, 10661006, 10661011)\n" );
		sb1.append("   and a.status = 10791007\n" );
		DaoFactory.getsql(sb1, "a.balance_no", balaceNo, 1);
		sb1.append("union all\n" );
		sb1.append("select (nvl(A.special_labour_sum, 0) + nvl(A.special_datum_sum, 0)+nvl(A.compensation_dealer_money, 0) -\n" );
		sb1.append("       nvl(A.OLD_DEDUCT, 0)) balance_amount,\n" );
		sb1.append("       nvl(a.APPEND_AMOUNT, 0) as APPEND_AMOUNT\n" );
		sb1.append("  from TT_AS_WR_CLAIM_BALANCE A\n" );
		sb1.append(" where  1=1 ");
		DaoFactory.getsql(sb1, "a.remark", balaceNo, 1);
		sb1.append(" ) t\n" );
		sb1.append(" where 1 = 1");
		Map<String,Object> mapTemp1 = this.pageQueryMap(sb1.toString(), null, getFunName());
		BigDecimal object1 = (BigDecimal) mapTemp1.get("BALANCE_AMOUNT");
		Double balance_amount1 = object1.doubleValue();
		Double d = balance_amount-balance_amount1;
		if(d!=0.0){
			res+="提示：明细和结算差："+Arith.round(d, 2);
		}
		return res;
	}

}
