package com.infodms.dms.actions.report.dmsReport;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.tag.BaseUtils;
import com.infodms.dms.common.tag.DaoFactory;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.po.TcCodePO;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;


@SuppressWarnings("rawtypes")
public class ClaimQueryDao extends BaseDao{

	
private static final ClaimQueryDao dao = new ClaimQueryDao();
	
	public static ClaimQueryDao getInstance(){
		if (dao == null) {
			return new ClaimQueryDao();
		}
		return dao;
	}
	
	
	public StringBuffer sqlString(RequestWrapper request, AclUserBean loginUser){
		
		String beginTime = CommonUtils.checkNull(request.getParamValue("beginTime"));
		String endTime = CommonUtils.checkNull(request.getParamValue("endTime"));
		String transBeginTime = CommonUtils.checkNull(request.getParamValue("transBeginTime"));
		String transEndTime = CommonUtils.checkNull(request.getParamValue("transEndTime"));
		String isInvoice = CommonUtils.checkNull(request.getParamValue("isInvoice"));
		String dealerCode = CommonUtils.checkNull(request.getParamValue("dealerCode"));
		String dealerName = CommonUtils.checkNull(request.getParamValue("dealerName"));
		String balanceNo = CommonUtils.checkNull(request.getParamValue("balanceNo"));
		String applyPersonName = CommonUtils.checkNull(request.getParamValue("applyPersonName"));
		
		StringBuffer sql = new StringBuffer();
		
		sql.append("select distinct \n");
		sql.append("       decode(vd.is_invoice, 0, '已审核', 1, '已结算', 2, '已开票', 3, '已收票', 4, '已验票', 5, '已转账' ）status, \n");
		sql.append("       vd.sub_date, \n");
		sql.append("       d.dealer_code, \n");
		sql.append("       d.dealer_shortname, \n");
		sql.append("       vd.balance_no, \n");
		sql.append("       nvl(vd.free_amount,0) free_amount, \n");
		sql.append("       nvl(vd.pdi_amount,0) pdi_amount, \n");
		sql.append("       nvl(vd.part_amount,0) part_amount, \n");
		sql.append("       nvl(vd.labour_amount,0) labour_amount, \n");
		sql.append("       nvl(vd.out_amount,0) out_amount, \n");
		sql.append("       nvl(vd.accessories_amount,0) accessories_amount, \n");
		sql.append("       nvl(vd.service_amount,0) service_amount, \n");
		sql.append("       nvl(vd.other_amount,0) other_amount, \n");
		sql.append("       nvl(vd.return_amount,0) return_amount, \n");
		sql.append("       nvl(vd.fine_amount,0) fine_amount, \n");
		sql.append("       nvl(vd.acc_amount,0) acc_amount, \n");
		sql.append("       nvl(vd.free_amount,0)+nvl(vd.pdi_amount,0)+nvl(vd.part_amount,0)+nvl(vd.labour_amount,0)+nvl(vd.out_amount,0)+nvl(vd.accessories_amount,0)+nvl(vd.service_amount,0) \n");
		sql.append("       +nvl(vd.other_amount,0)+nvl(vd.return_amount,0)+nvl(vd.fine_amount,0)+nvl(vd.acc_amount,0) sum_amount, \n");
		sql.append("       nvl(vd.bytc,0) bytc, \n");
		sql.append("       nvl(vd.pditc,0) pditc, \n");
		sql.append("       nvl(vd.sptc,0) sptc, \n");
		sql.append("       nvl(vd.hdtc,0) hdtc, \n");
		sql.append("       acb.apply_person_name claim_by, \n");
		sql.append("       acb.create_date claim_date, \n");
		//sql.append("       decode(vd.is_invoice,1,'',(select distinct u.name from tc_user u where u.user_id=p.create_by)) kp_by, \n");
		sql.append("       decode(vd.is_invoice,1,'',to_char(p.creat_date,'yyyy-mm-dd')) kp_date, \n");
		sql.append("       (select distinct u.name from tc_user u where u.user_id=p.collect_tickets) sp_by, \n");
		sql.append("       p.collect_tickets_date sp_date, \n");
		sql.append("       (select distinct u.name from tc_user u where u.user_id=p.check_tickets) yp_by, \n");
		sql.append("       p.check_tickets_date yp_date, \n");
		sql.append("       (select distinct u.name from tc_user u where u.user_id=p.transfer_tickets) zz_by, \n");
		sql.append("       p.transfer_tickets_date zz_date \n");
		sql.append("  from vw_claim_summary_data vd, \n");
		sql.append("       tm_dealer d, \n");
		sql.append("       tt_as_wr_claim_balance acb, \n");
		sql.append("       tt_as_payment p \n");
		sql.append(" where vd.dealer_id = d.dealer_id \n");
		sql.append("   and vd.balance_no=acb.remark(+) \n");
		sql.append("   and vd.balance_no=p.balance_oder(+) \n");
		
		
		if (this.getPoseRoleId(loginUser.getPoseId().toString()).equals(Constant.FWJL_ROLE_ID)) {
            sql.append(CommonUtils.getOrgDealerLimitSqlByPose("d", loginUser));
        }
		 
		//转账时间
		if(!"".equals(transBeginTime)){
			sql.append("   and trunc(p.transfer_tickets_date) >= to_date('" + transBeginTime + "','yyyy-mm-dd') \n");
			
		}
		if(!"".equals(transEndTime)){
			sql.append("   and trunc(p.transfer_tickets_date) <= to_date('" + transEndTime + "','yyyy-mm-dd') \n");
		}
		 
		 
		//服务站号
		if(!"".equals(dealerCode)){
			sql.append("   and d.dealer_code like '%" + dealerCode + "%' \n");
		}
		//服务站简称
		
		if(!"".equals(dealerName))
		sql.append("   and d.dealer_name like '%" + dealerName + "%' \n");
		
		//结算状态0, '已审核', 1, '已结算', 2, '已开票', 3, '已收票', 4, '已验票', 5, '已转账'
		if(!"".equals(isInvoice)){
			sql.append("   and vd.is_invoice = " + isInvoice + " \n");
		}
		
		//结算编号
		if(!"".equals(balanceNo)){
			sql.append("   and vd.balance_no like '%" + balanceNo + "%' \n");
		}
		//结算人
		if(!"".equals(applyPersonName)){
			sql.append("   and acb.apply_person_name like '%" + applyPersonName + "%' \n");
		}
		//结算单创建时间
		if(!"".equals(beginTime)){
			sql.append("   and trunc(acb.create_date) >= to_date('" + beginTime + "','yyyy-mm-dd') \n");
			
		}
		if(!"".equals(endTime)){
			sql.append("   and trunc(acb.create_date) <= to_date('" + endTime + "','yyyy-mm-dd') \n");
		}
		sql.append("  order by vd.sub_date,d.dealer_code,vd.balance_no asc \n");
		
		return sql;
	}

	@SuppressWarnings("unchecked")
	public PageResult<Map<String, Object>>   claimAccountQuery(RequestWrapper request,AclUserBean loginUser, int pageSize, int currPage) {
		StringBuffer sql= this.sqlString(request,loginUser);
		PageResult<Map<String, Object>> list=pageQuery(sql.toString(), null,getFunName(), pageSize, currPage);
		return list;
	}
	
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> sumQuery(RequestWrapper request, AclUserBean loginUser) {
		
		StringBuffer sql= new StringBuffer();
		
		sql.append("SELECT nvl(SUM(A.sum_amount),0) AMOUNT, COUNT(1) TS \n");
		sql.append("FROM \n");
		sql.append("( \n");

		sql.append(this.sqlString(request,loginUser));
		
		sql.append(") A \n");
		
		List<Map<String, Object>> list= pageQuery(sql.toString(), null, getFunName());
		return list;
	}
	

	
	@SuppressWarnings("unchecked")
	public void expotDataClaimAccount(ActionContext act, PageResult<Map<String, Object>> list) {
		try {
			String[] head=new String[31];
			head[0]="状态";
			head[1]="索赔月份";
			head[2]="商家代码";
			head[3]="商家简称";
			head[4]="结算编号";
			head[5]="保养费";
			head[6]="PDI费";
			head[7]="索赔材料费";
			head[8]="索赔工时费 ";
			head[9]="外出救援";
			head[10]="辅料";
			head[11]="服务活动费";
			head[12]="其他金额";
			head[13]="旧件运费";
			head[14]="误判金额";
			head[15]="正负激励费用";
			head[16]="合计";
			head[17]="保养台次";
			head[18]="PDI台次";
			head[19]="索赔台次";
			head[20]="活动台次";
			head[21]="结算日期";
			head[22]="结算人";
			head[23]="开票日期";
			head[24]="开票人";
			head[25]="收票日期";
			head[26]="收票人";
			head[27]="验票日期";
			head[28]="验票人";
			head[29]="转款日期";
			head[30]="转款人";
			
			
			List<Map<String, Object>> records = list.getRecords();
			List params=new ArrayList();
			if(records!=null &&records.size()>0){
				for (Map<String, Object> map : records) {
					String[] detail=new String[31];
					detail[0]=BaseUtils.checkNull(map.get("STATUS"));
					detail[1]=BaseUtils.checkNull(map.get("SUB_DATE"));
					detail[2]=BaseUtils.checkNull(map.get("DEALER_CODE"));
					detail[3]=BaseUtils.checkNull(map.get("DEALER_SHORTNAME"));
					detail[4]=BaseUtils.checkNull(map.get("BALANCE_NO"));
					detail[5]=BaseUtils.checkNull(map.get("FREE_AMOUNT"));
					detail[6]=BaseUtils.checkNull(map.get("PDI_AMOUNT"));
					detail[7]=BaseUtils.checkNull(map.get("PART_AMOUNT"));
					detail[8]=BaseUtils.checkNull(map.get("LABOUR_AMOUNT"));
					detail[9]=BaseUtils.checkNull(map.get("OUT_AMOUNT"));
					detail[10]=BaseUtils.checkNull(map.get("ACCESSORIES_AMOUNT"));
					detail[11]=BaseUtils.checkNull(map.get("SERVICE_AMOUNT"));
					detail[12]=BaseUtils.checkNull(map.get("OTHER_AMOUNT"));
					detail[13]=BaseUtils.checkNull(map.get("RETURN_AMOUNT"));
					detail[14]=BaseUtils.checkNull(map.get("FINE_AMOUNT"));
					detail[15]=BaseUtils.checkNull(map.get("ACC_AMOUNT"));
					detail[16]=BaseUtils.checkNull(map.get("SUM_AMOUNT"));
					detail[17]=BaseUtils.checkNull(map.get("BYTC"));
					detail[18]=BaseUtils.checkNull(map.get("PDITC"));
					detail[19]=BaseUtils.checkNull(map.get("SPTC"));
					detail[20]=BaseUtils.checkNull(map.get("HDTC"));
					detail[21]=BaseUtils.checkNull(map.get("CLAIM_DATE"));
					detail[22]=BaseUtils.checkNull(map.get("CLAIM_BY"));
					detail[23]=BaseUtils.checkNull(map.get("KP_DATE"));
					detail[24]=BaseUtils.checkNull(map.get("KP_BY"));
					detail[25]=BaseUtils.checkNull(map.get("SP_DATE"));
					detail[26]=BaseUtils.checkNull(map.get("SP_BY"));
					detail[27]=BaseUtils.checkNull(map.get("YP_DATE"));
					detail[28]=BaseUtils.checkNull(map.get("YP_BY"));
					detail[29]=BaseUtils.checkNull(map.get("ZZ_DATE"));
					detail[30]=BaseUtils.checkNull(map.get("ZZ_BY"));
					params.add(detail);
				}
			}
			BaseUtils.toExcel(act.getResponse(), act.getRequest(), head, params, "导出索赔结算总汇数据.xls", "导出数据",null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("unchecked")
	public PageResult<Map<String, Object>>   partMonthlyClaimTOP20Query(RequestWrapper request,int pageSize, int currPage) {
		
		String beginTime = CommonUtils.checkNull(request.getParamValue("beginTime"));
		String endTime = CommonUtils.checkNull(request.getParamValue("endTime"));
		String partCode = CommonUtils.checkNull(request.getParamValue("partCode"));
		String partName = CommonUtils.checkNull(request.getParamValue("partName"));
		String carModel = DaoFactory.getParam(request, "carModel");//新增车型条件查询
		String carRebate = DaoFactory.getParam(request, "carRebate");//新增车系条件查询
		
		StringBuffer sql= new StringBuffer();
		
		sql.append("with allsum as \n");
		sql.append(" (select sum(awp.balance_quantity) quantity, sum(awp.balance_amount) amount \n");
		sql.append("    from tt_as_wr_application awa, tt_as_wr_partsitem awp \n");
		sql.append("   where awa.status = 10791007 \n");
		sql.append("     and awa.claim_type <> 10661006 \n");
		sql.append("     and awa.id = awp.id \n");
		sql.append("     and awp.responsibility_type = 94001001 \n");
		if(!"".equals(carModel)||!"".equals(carRebate)){
			sql.append("and awa.vin in (select v.vin\n" );
			sql.append("                     from vw_material_group_service vw, tm_vehicle v\n" );
			sql.append("                    where v.package_id = vw.PACKAGE_ID(+) ");
			DaoFactory.getsql(sql, "vw.model_code", DaoFactory.getSqlByarrIn(carModel), 6);
			DaoFactory.getsql(sql, "vw.series_name", carRebate, 2);
			sql.append("  )\n");
		}
		//结算时间
		if(!"".equals(beginTime)){
			sql.append("     and trunc(awa.sub_date) >= to_date('" + beginTime + "', 'yyyy-mm-dd') \n");
		}
		if(!"".equals(endTime)){
			sql.append("     and trunc(awa.sub_date) <= to_date('" + endTime + "', 'yyyy-mm-dd') \n");
		}
		
		sql.append("  ), \n");
		sql.append("partsum as \n");
		sql.append(" (select awp.down_part_code part_code, \n");
		sql.append("         awp.down_part_name part_name, \n");
		sql.append("         sum(awp.balance_quantity) quantity, \n");
		sql.append("         sum(awp.balance_amount) amount \n");
		sql.append("    from tt_as_wr_application awa, tt_as_wr_partsitem awp \n");
		sql.append("   where awa.status = 10791007 \n");
		sql.append("     and awa.claim_type <> 10661006 \n");
		sql.append("     and awa.id = awp.id \n");
		sql.append("     and awp.responsibility_type = 94001001 \n");
		
		if(!"".equals(carModel)||!"".equals(carRebate)){
			sql.append("and awa.vin in (select v.vin\n" );
			sql.append("                     from vw_material_group_service vw, tm_vehicle v\n" );
			sql.append("                    where v.package_id = vw.PACKAGE_ID(+) ");
			DaoFactory.getsql(sql, "vw.model_code", DaoFactory.getSqlByarrIn(carModel), 6);
			DaoFactory.getsql(sql, "vw.series_name", carRebate, 2);
			sql.append("  )\n");
		}
		//结算时间
		if(!"".equals(beginTime)){
			sql.append("     and trunc(awa.sub_date) >= to_date('" + beginTime + "', 'yyyy-mm-dd') \n");
		}
		if(!"".equals(endTime)){
			sql.append("     and trunc(awa.sub_date) <= to_date('" + endTime + "', 'yyyy-mm-dd') \n");
		}
		
		sql.append("   group by awp.down_part_code, awp.down_part_name), \n");
		sql.append("top20 as \n");
		sql.append(" (select * \n");
		sql.append("    from (select p.*, \n");
		sql.append("                 to_char(p.quantity / a.quantity * 100, 'FM90.00') || '%' slzb \n");
		sql.append("            from partsum p, allsum a \n");
		sql.append("           where 1 = 1 \n");
		
		if(!"".equals(partCode)){
			sql.append("            and p.part_code like '%" + partCode + "%' \n");//零件号条件
		}
		if(!"".equals(partName)){
			sql.append("            and p.part_name like '%" + partName + "%' \n");//零件名条件
		}
		
		sql.append("           order by p.quantity / a.quantity * 100 desc) tmp \n");
		sql.append("   where rownum <= 20) \n");
		sql.append("select tmp.part_code, \n");// 主损件代码
		sql.append("       tmp.part_name, \n");// 主损件名称 
		sql.append("       tmp.quantity, \n");// 主损数量
		sql.append("       tmp.amount, \n");// 金额
		sql.append("       tmp.slzb, \n");// 数量占比
		sql.append("       decode(tmp.part_name, \n");
		sql.append("              '索赔零件TOP20', \n");
		sql.append("              to_char((select sum(t.quantity) from top20 t) / \n");
		sql.append("                      (select a.quantity from allsum a) * 100, \n");
		sql.append("                      'FM90.00') || '%(数量占比)', \n");
		sql.append("              '总计', \n");
		sql.append("              to_char((select sum(t.amount) from top20 t) / \n");
		sql.append("                      (select a.amount from allsum a) * 100, \n");
		sql.append("                      'FM90.00') || '%(金额占比)', \n");
		sql.append("              '') remark \n");// 备注
		sql.append("  from (select * \n");
		sql.append("          from top20 \n");
		sql.append("        union all \n");
		sql.append("        select '', '索赔零件TOP20', sum(quantity), sum(amount), '' \n");
		sql.append("          from top20 t \n");
		sql.append("        union all \n");
		sql.append("        select '', '总计', sum(quantity), sum(amount), '' from allsum a) tmp \n");
		
		PageResult<Map<String, Object>> list=pageQuery(sql.toString(), null,getFunName(), pageSize, currPage);
		return list;
	}
	
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>>   partMonthlyClaimTOP20Dtl(RequestWrapper request,int pageSize, int currPage) {
		
		String beginTime = CommonUtils.checkNull(request.getParamValue("beginTime"));
		String endTime = CommonUtils.checkNull(request.getParamValue("endTime"));
		String partCode = CommonUtils.checkNull(request.getParamValue("partCode"));
		String carRebate = DaoFactory.getParam(request, "carRebate");//新增车系条件查询
		
		StringBuffer sql= new StringBuffer();
		sql.append("select d.dealer_code, \n");//经销商代码
		sql.append("       d.dealer_shortname, \n");//经销商简称
		sql.append("       awa.claim_no, \n");//索赔单号
		sql.append("       awp.down_part_code, \n");//配件代码
		sql.append("       awp.down_part_name, \n");//配件名称
		sql.append("       awp.balance_quantity, \n");//索赔数量
		sql.append("       awp.balance_amount, \n");//索赔金额
		sql.append("       awp.down_product_code, \n");//供应商代码
		sql.append("       awp.down_product_name \n");//供应商名称
		sql.append("  from tt_as_wr_application awa, tt_as_wr_partsitem awp, tm_dealer d \n");
		sql.append(" where awa.status = 10791007 \n");
		sql.append("   and awa.claim_type <> 10661006 \n");
		sql.append("   and awa.id = awp.id \n");
		sql.append("   and awa.dealer_id = d.dealer_id \n");
		sql.append("   and awp.responsibility_type = 94001001 \n");
		sql.append("   and awp.down_part_code = '" + partCode + "' \n");//配件代码条件
		if(!"".equals(carRebate)){
			sql.append("and awa.vin in (select v.vin\n" );
			sql.append("                     from vw_material_group_service vw, tm_vehicle v\n" );
			sql.append("                    where v.package_id = vw.PACKAGE_ID(+) ");
			DaoFactory.getsql(sql, "vw.series_name", carRebate, 2);
			sql.append("  )\n");
		}
		
		//结算时间
		if(!"".equals(beginTime)){
			sql.append("     and trunc(awa.sub_date) >= to_date('" + beginTime + "', 'yyyy-mm-dd') \n");
		}
		if(!"".equals(endTime)){
			sql.append("     and trunc(awa.sub_date) <= to_date('" + endTime + "', 'yyyy-mm-dd') \n");
		}
		
		List<Map<String, Object>> list=pageQuery(sql.toString(), null,getFunName());
		return list;
	}
	
	@SuppressWarnings("unchecked")
	public void expotDataPartMonthlyClaimTOP20(ActionContext act, PageResult<Map<String, Object>> list) {
		try {
			String[] head=new String[6];
			head[0]="主损件代码";
			head[1]="主损件名称";
			head[2]="主损件数量";
			head[3]="金额";
			head[4]="数量占比";
			head[5]="备注";
			
			List<Map<String, Object>> records = list.getRecords();
			List params=new ArrayList();
			if(records!=null &&records.size()>0){
				for (Map<String, Object> map : records) {
					String[] detail=new String[31];
					detail[0]=BaseUtils.checkNull(map.get("PART_CODE"));
					detail[1]=BaseUtils.checkNull(map.get("PART_NAME"));
					detail[2]=BaseUtils.checkNull(map.get("QUANTITY"));
					detail[3]=BaseUtils.checkNull(map.get("AMOUNT"));
					detail[4]=BaseUtils.checkNull(map.get("SLZB"));
					detail[5]=BaseUtils.checkNull(map.get("REMARK"));
					params.add(detail);
				}
			}
			BaseUtils.toExcel(act.getResponse(), act.getRequest(), head, params, "导出月索赔配件数量TOP20数据.xls", "导出数据",null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	@SuppressWarnings("unchecked")
	public void expotDataPartMonthlyClaimTOP20Dtl(ActionContext act, List<Map<String, Object>> list) {
		try {
			String[] head=new String[9];
			head[0]="经销商代码";
			head[1]="经销商名称";
			head[2]="索赔单号";
			head[3]="配件编码";
			head[4]="配件名称";
			head[5]="索赔数量";
			head[6]="索赔金额";
			head[7]="供应商代码";
			head[8]="供应商名称";
			
			List params=new ArrayList();
			if(list!=null &&list.size()>0){
				for (Map<String, Object> map : list) {
					String[] detail=new String[31];
					detail[0]=BaseUtils.checkNull(map.get("DEALER_CODE"));
					detail[1]=BaseUtils.checkNull(map.get("DEALER_SHORTNAME"));
					detail[2]=BaseUtils.checkNull(map.get("CLAIM_NO"));
					detail[3]=BaseUtils.checkNull(map.get("DOWN_PART_CODE"));
					detail[4]=BaseUtils.checkNull(map.get("DOWN_PART_NAME"));
					detail[5]=BaseUtils.checkNull(map.get("BALANCE_QUANTITY"));
					detail[6]=BaseUtils.checkNull(map.get("BALANCE_AMOUNT"));
					detail[7]=BaseUtils.checkNull(map.get("DOWN_PRODUCT_CODE"));
					detail[8]=BaseUtils.checkNull(map.get("DOWN_PRODUCT_NAME"));
					
					params.add(detail);
				}
			}
			BaseUtils.toExcel(act.getResponse(), act.getRequest(), head, params, "导出月索赔配件数量TOP20明细数据.xls", "导出数据",null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	

	private String getSystemTime() {
		String systemDateStr = BaseUtils.getSystemDateStr();
		return systemDateStr;
	}

	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		return null;
	}


	/**
	 * 公用导出设置
	 * @param act
	 * @param head
	 * @param params
	 * @param name
	 */
	@SuppressWarnings("unchecked")
	public void toExcel(ActionContext act,String[] head,List params,List<Map<Integer, String>> listCount,String name){
		String systemDateStr = getSystemTime();
		try {
			BaseUtils.toExcel(act.getResponse(), act.getRequest(), head, params, name+systemDateStr+".xls", "导出数据",listCount);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("unchecked")
	private String getTypeDesc(String codeId){
		TcCodePO t=new TcCodePO();
		t.setCodeId(codeId);
		List<TcCodePO> list= dao.select(t);
		if(list!=null && list.size()>0){
			t=list.get(0);
			return t.getCodeDesc();
		}else{
			return "无";
		}
	}

	@SuppressWarnings("unchecked")
	public PageResult<Map<String, Object>> selectSupplierCode(RequestWrapper request, Integer pageSize, Integer currPage) {
		StringBuffer sb= new StringBuffer();
		String maker_code = DaoFactory.getParam(request, "maker_code");
		String maker_shotname = DaoFactory.getParam(request, "maker_shotname");
		sb.append("select c.maker_id   as supplier_id,  c.maker_code, c.maker_shotname\n" );
		sb.append("    from tt_part_maker_define  c where  1=1 and c.state=10011001 " );
		DaoFactory.getsql(sb, "c.maker_code", maker_code, 2);
		DaoFactory.getsql(sb, "c.maker_shotname", maker_shotname, 2);
		PageResult<Map<String, Object>> list=pageQuery(sb.toString(), null,getFunName(), pageSize, currPage);
		return list;
	}

}
