package com.infodms.yxdms.dao;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.bean.TtAsWrApplicationExtBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.common.materialManager.MaterialGroupManagerDao;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.dao.orgmng.DlrInfoMngDAO;
import com.infodms.dms.dao.sales.ordermanage.reqquery.ReqQueryDao;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.XHBUtil;
import com.infodms.dms.util.businessUtil.GetVinUtil;
import com.infodms.yxdms.constant.MyConstant;
import com.infodms.yxdms.utils.BaseUtils;
import com.infodms.yxdms.utils.DaoFactory;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;


public class ClaimBalanceDAO extends BaseDao {
	public static Logger logger = Logger.getLogger(ClaimBalanceDAO.class);
	private static final ClaimBalanceDAO dao = new ClaimBalanceDAO();

	public static final ClaimBalanceDAO getInstance() {
		return dao;
	}

	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public PageResult<Map<String,Object>> queryClaimAudit1(Map<String, String> map,Integer curPage,Integer pageSize){
		String sql = "";
		StringBuffer sqlBulider = new StringBuffer();
		sqlBulider.append(" SELECT /*+ INDEX (A IDX_TT_APPLICATION_STATUS) */ A.CLAIM_NO, tm.DEALER_CODE,tm.DEALER_SHORTNAME DEALER_NAME,\n" );
		sqlBulider.append("      A.RO_NO AS RO_NO,A.CLAIM_TYPE,A.VIN,a.submit_times,\n" );
		sqlBulider.append("      TO_CHAR(A.sub_date, 'yyyy-mm-dd hh24:mi') CREATE_DATE,A.STATUS,\n" );
		sqlBulider.append("      A.ID,A.REPAIR_TOTAL,B.Root_Org_Name,\n" );
		sqlBulider.append("       (select count(1) from Tt_As_Wr_App_Audit_Detail d where d.audit_result=10791006 and  d.claim_id=a.id) back_times\n" );
		sqlBulider.append(" FROM TT_AS_WR_APPLICATION A,\n" );
		sqlBulider.append("      VW_ORG_DEALER_SERVICE B,tm_dealer tm \n" );
		sqlBulider.append("WHERE 1 = 1\n" );
		sqlBulider.append("  AND A.DEALER_ID = B.DEALER_ID and tm.DEALER_ID=A.DEALER_ID\n" );
		sqlBulider.append("  AND a.STATUS in ( "+Constant.CLAIM_APPLY_ORD_TYPE_03+" ,"+Constant.CLAIM_APPLY_ORD_TYPE_02+"  )  \n" );
		
		if(Utility.testString(map.get("dealerId"))){
			sqlBulider.append("  AND B.dealer_id in ("+map.get("dealerId")+")\n" );
		}
		
		if(Utility.testString(map.get("STATUS"))){
			sqlBulider.append("  AND a.STATUS like '%"+map.get("STATUS")+"%'\n" );
		}
		if(Utility.testString(map.get("dealerName"))){
			sqlBulider.append("  AND tm.DEALER_SHORTNAME like '%"+map.get("dealerName")+"%'\n" );
		}
		if(Utility.testString(map.get("roNo"))){
			sqlBulider.append("  AND a.ro_no like '%"+map.get("roNo")+"%'\n" );
		}
		
		DaoFactory.getsql(sqlBulider, "a.claim_type", CommonUtils.checkNull(map.get("claimType")), 6);
		if(Utility.testString(map.get("vin"))){
			sqlBulider.append("  AND a.vin like '%"+map.get("vin")+"%'\n" );
		}
		if(Utility.testString(map.get("applyStartDate"))){
			sqlBulider.append("  AND a.sub_date>=to_date('"+map.get("applyStartDate")+"','yyyy-mm-dd')\n" );
		}
		if(Utility.testString(map.get("applyEndDate"))){
			sqlBulider.append("  AND a.sub_date<=to_date('"+map.get("applyEndDate")+"','yyyy-mm-dd')\n" );
		}
		if(Utility.testString(map.get("claimNo"))){
			sqlBulider.append("  AND a.claim_no like '%"+map.get("claimNo")+"%'\n" );
		}
		if(Utility.testString(map.get("orgId"))){
			sqlBulider.append("  AND b.root_org_id = "+map.get("orgId")+"\n" );
		}
		if(Utility.testString(map.get("pose_id"))){
			sqlBulider.append("  AND tm.dealer_org_id in ( SELECT r.region_id FROM Tr_Pose_Region r where r.pose_id = "+map.get("pose_id")+") \n" );
		}
		sqlBulider.append(" ORDER BY A.sub_date asc");
		sql = sqlBulider.toString();
		PageResult<Map<String,Object>> ps = pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
		return ps; 
	}
	
	public PageResult<Map<String,Object>> getInvoiceDate(Map<String, String> map,Integer curPage,Integer pageSize){
		StringBuffer sbSql = new StringBuffer();
		PageResult<Map<String,Object>> ps = pageQuery(sbSql.toString(), null, getFunName(), Constant.PAGE_SIZE, curPage);
		return ps;
	}
	public Map<String, Object> pdiView(AclUserBean loginUser,RequestWrapper request,boolean flag) {
		StringBuffer sb= new StringBuffer();
		sb.append("select a.claim_no,\n" );
		sb.append("       a.app_color,\n" );
		sb.append("       a.app_package_name,\n" );
		sb.append("       a.vin,\n" );
		sb.append("       a.status,\n" );
		if(!flag){
			sb.append(" 0 as timeout_amount,\n");
		}else{
			sb.append(" n.amount as timeout_amount,\n");
		}
		sb.append("       a.model_name,a.engine_no,\n" );
		sb.append("       n.*\n" );
		sb.append("  from Tt_As_Wr_Application a, tt_as_wr_netitem n\n" );
		sb.append(" where a.id=n.id  and  a.claim_type = 10661011");
		DaoFactory.getsql(sb, "a.id", DaoFactory.getParam(request, "id"), 1);
		return pageQueryMap(sb.toString(), null, getFunName());
	}
	
	@SuppressWarnings("unchecked")
	public boolean checkInvoiceDateIsInTime(Date subDate,int parameter,String vin){
		boolean intimeFlag = true;
		StringBuffer sbSql = new StringBuffer();
		sbSql.append("select tdas.invoice_date,tdas.is_return	 \n");
		sbSql.append("  from tt_dealer_actual_sales tdas, tm_vehicle tv\n");
		sbSql.append(" where tdas.vehicle_id = tv.vehicle_id\n");
		sbSql.append("   and tv.life_cycle = 10321004\n");
		DaoFactory.getsql(sbSql, "vin", vin, 1);
		List<Map<String, Object>> resList = pageQuery(sbSql.toString(), null,getFunName());
		if(null != resList && resList.size()>0){
			for(int i=0;i<resList.size();i++){
				Date invoiceDate = (Date)resList.get(i).get("INVOICE_DATE");
				String is_return = (String)resList.get(i).get("is_return");
				if(subDate.getTime()-invoiceDate.getTime()>parameter*24*60*60*1000 && Constant.IF_TYPE_NO.toString().equals(is_return)){
					//如果未退车并且提报时间超过开票时间?天则设置状态为false
					intimeFlag = false;
				}
			}
		}
		return intimeFlag;
	}
	
	public PageResult<Map<String,Object>> queryClaimAudit2(Map<String, String> map,Integer curPage,Integer pageSize){
		String sql = "";
		StringBuffer sqlBulider = new StringBuffer();
		sqlBulider.append(" SELECT /*+ INDEX (A IDX_TT_APPLICATION_STATUS) */DISTINCT A.CLAIM_NO, b.DEALER_CODE,b.DEALER_SHORTNAME DEALER_NAME,vm.ROOT_ORG_NAME, \n" );
		sqlBulider.append("      A.RO_NO AS RO_NO,A.CLAIM_TYPE,A.VIN,a.submit_times,\n" );
		sqlBulider.append("      TO_CHAR(A.sub_date, 'yyyy-mm-dd hh24:mi') CREATE_DATE,A.STATUS,\n" );
		sqlBulider.append("      A.ID,A.REPAIR_TOTAL\n" );
		sqlBulider.append(" FROM TT_AS_WR_APPLICATION A,\n" );
		sqlBulider.append("      TM_DEALER B,\n" );
		sqlBulider.append("      Tt_As_Wr_Partsitem TAWP,\n" );
		sqlBulider.append("      Tt_As_Wr_Labouritem TAWL,\n" );
		sqlBulider.append("      VW_ORG_DEALER_SERVICE vm\n" );
		sqlBulider.append("WHERE 1 = 1\n" );
		DaoFactory.getsql(sqlBulider, "a.create_date", MyConstant.onlineDate, 3);//加入时间新分单节点
		sqlBulider.append("  AND A.DEALER_ID = vm.DEALER_ID\n" );
		sqlBulider.append("  AND A.DEALER_ID = B.DEALER_ID\n" );
		sqlBulider.append("  AND A.ID = TAWP.ID(+)\n" );
		sqlBulider.append("  AND A.ID = TAWL.ID(+)\n" );
		sqlBulider.append("  AND a.STATUS in ( "+Constant.CLAIM_APPLY_ORD_TYPE_08+"  )  \n" );
		sqlBulider.append("  AND A.ID not in ( SELECT d.claim_id FROM Tt_As_Wr_Old_Returned r,Tt_As_Wr_Old_Returned_Detail d  WHERE r.id = d.return_id  GROUP BY d.claim_id )  ");
		sqlBulider.append("  AND A.DEALER_ID = B.DEALER_ID and A.IS_INVOICE = 0  \n" );
		sqlBulider.append("  AND A.claim_type not in (10661002,10661011)  \n" );//限制pdi和保养不能撤销
		if(Utility.testString(map.get("dealerId"))){
			sqlBulider.append("  AND B.dealer_id in ("+map.get("dealerId")+")\n" );
		}
		if(Utility.testString(map.get("dealerName"))){
			sqlBulider.append("  AND b.DEALER_SHORTNAME like '%"+map.get("dealerName")+"%'\n" );
		}
		if(Utility.testString(map.get("roNo"))){
			sqlBulider.append("  AND a.ro_no like '%"+map.get("roNo")+"%'\n" );
		}
		if(Utility.testString(map.get("claimType"))){
			sqlBulider.append("  AND a.claim_type="+map.get("claimType")+"\n" );
		}
		if(Utility.testString(map.get("vin"))){
			sqlBulider.append("  AND a.vin like '%"+map.get("vin")+"%'\n" );
		}
		if(Utility.testString(map.get("applyStartDate"))){
			sqlBulider.append("  AND a.sub_date>=to_date('"+map.get("applyStartDate")+"','yyyy-mm-dd')\n" );
		}
		if(Utility.testString(map.get("applyEndDate"))){
			sqlBulider.append("  AND a.sub_date<=to_date('"+map.get("applyEndDate")+"','yyyy-mm-dd')\n" );
		}
		if(Utility.testString(map.get("claimNo"))){
			sqlBulider.append("  AND a.claim_no like '%"+map.get("claimNo")+"%'\n" );
		}
		//零件代码
		if(Utility.testString(map.get("partCode"))){
			sqlBulider.append("  AND TAWP.PART_CODE like '%"+map.get("partCode")+"%'\n" );
		}
		//作业代码
		if(Utility.testString(map.get("wrLabourCode"))){
			sqlBulider.append("  AND TAWL.WR_LABOURCODE like '%"+map.get("wrLabourCode")+"%'\n" );
		}
		sqlBulider.append(" ORDER BY A.ID DESC");
		sql = sqlBulider.toString();
		PageResult<Map<String,Object>> ps = pageQuery(sql.toString(), null, getFunName(), Constant.PAGE_SIZE, curPage);
		return ps;
	}
	
	/**
     * 车厂索赔单查询 zyw 重构修改2014-8-8  删减代码80%
     */
   @SuppressWarnings("unchecked")
   public PageResult<Map<String, Object>> queryApplication2(RequestWrapper request, AclUserBean loginUser,Integer pageSize, Integer currPage) {
	   	String dealerCode = DaoFactory.getParam(request,"DEALER_CODE");
		String dealerName = DaoFactory.getParam(request,"DEALER_NAME");
		String roNo = DaoFactory.getParam(request,"RO_NO");
		String lineNo = DaoFactory.getParam(request,"LINE_NO");
		String claimNo = DaoFactory.getParam(request,"CLAIM_NO");
		String claimType = DaoFactory.getParam(request,"CLAIM_TYPE");
		String vin = DaoFactory.getParam(request,"VIN");
		String roStartdate = DaoFactory.getParam(request,"RO_STARTDATE");
		String roEnddate = DaoFactory.getParam(request,"RO_ENDDATE");
		String approveDate = DaoFactory.getParam(request,"approve_date");// 审核时间
		String approveDate2 = DaoFactory.getParam(request,"approve_date2");
		String status = DaoFactory.getParam(request,"STATUS");
		String person = DaoFactory.getParam(request,"PERSON");
		String yieldly = DaoFactory.getParam(request,"YIELDLY");// 查询条件--产地
		String yieldlys = CommonUtils.findYieldlyByPoseId(loginUser.getPoseId());// 该用户拥有的产地权限
		String isImport = DaoFactory.getParam(request,"is_import");
		String foreAuthPerson = DaoFactory.getParam(request,"foreAuthPerson");
		String model = DaoFactory.getParam(request,"model");
		String partCode = DaoFactory.getParam(request,"partCode");
		String wrLabourCode = DaoFactory.getParam(request,"wrLabourCode");
		
		StringBuffer sb= new StringBuffer();
		sb.append("select t.*\n" );
		sb.append("  from (select b.dealer_code,\n" );
		sb.append("               b.dealer_shortname,\n" );
		sb.append("               a.ro_no,a.id,\n" );
		sb.append("               a.claim_no,a.report_date,\n" );
		sb.append("               a.claim_type,\n" );
		sb.append("               (select tt.area_name\n" );
		sb.append("               from tm_business_area tt\n" );
		sb.append("               where tt.area_id = a.yieldly) as yieldly,\n");
		sb.append("               a.submit_times,\n" );
		sb.append("               a.vin,\n" );
		sb.append("               a.sub_date,\n" );
		sb.append("               a.status,\n" );
		sb.append("               vm.model_code,\n" );
		sb.append("               vm.model_name,\n" );
		sb.append("               a.model_id,\n" );
		sb.append("               a.balance_amount,\n" );
		sb.append("               a.report_date as auditing_date,\n" );
		sb.append("       (select count(1) from Tt_As_Wr_App_Audit_Detail d where d.audit_result=10791006 and  d.claim_id=a.id) back_times,\n" );//退回次数
		
		sb.append("(select c.name from tc_user c where c.user_id =\n" );
		sb.append("              (select max(d.audit_by) from Tt_As_Wr_App_Audit_Detail d\n" );
		sb.append("              where d.claim_id=a.id and d.audit_date=(select max(d.audit_date)\n" );
		sb.append("              from Tt_As_Wr_App_Audit_Detail d where d.claim_id =a.id))) as auditing_man,");
		sb.append("               (select case\n" );
		sb.append("                         when max(h.id) is null then\n" );
		sb.append("                          '未扣件'\n" );
		sb.append("                         else\n" );
		sb.append("                          '扣件'\n" );
		sb.append("                       end\n" );
		sb.append("                  from tt_as_wr_partsitem h\n" );
		sb.append("                 where h.id = a.id\n" );
		sb.append("                   and h.balance_quantity < h.quantity\n" );
		sb.append("                   and h.is_return = 95361001) kou_jian\n" );
		sb.append("          from tt_as_wr_application a, tm_dealer b,vw_material_group_mat vm, tm_vehicle tv\n" );
		sb.append("         where 1 = 1 and vm.MATERIAL_ID = tv.material_id and tv.vin=a.vin \n" );
		sb.append("           and a.status not in (10791001)\n" );
		sb.append("           and b.dealer_id = nvl(a.second_dealer_id, a.dealer_id)\n" );
		DaoFactory.getsql(sb, "A.YIELDLY", yieldly, 1);
		// 2015.09.14 增加历史的经销商名称查询
//		DaoFactory.getsql(sb, "b.dealer_shortname", dealerName, 2);
		DlrInfoMngDAO dimDAO = new DlrInfoMngDAO();
		if(BaseUtils.notNull(dealerName)){
			sb.append(" and (b.dealer_shortname like '%"+dealerName+"%'\n");
			String idSql = dimDAO.queryHisDealerIdsByNameSql(dealerName);
			if(idSql!=null&&idSql.length()>0){
				sb.append(" or b.dealer_id in ( ");
				sb.append(idSql);
				sb.append(" ) ");
			}
			sb.append(" ) ");
		}
		DaoFactory.getsql(sb, "a.create_date", MyConstant.onlineDate, 3);//加入时间新分单节点
		// 2015.09.14 增加历史的经销商code查询
//		DaoFactory.getsql(sb, "b.dealer_Code", dealerCode, 6);
		if(BaseUtils.notNull(dealerCode)){
			String[] split = dealerCode.split(",");
			StringBuffer sbTemp=new StringBuffer(); 
			int length = split.length;
			if(length>0){
				for (int i = 0; i < length; i++) {
					String str = split[i];
					if(i==length-1){
						sbTemp.append("'"+str+"'");
					}else{
						sbTemp.append("'"+str+"',");
					}
				}
				sb.append(" and (b.dealer_Code in ("+sbTemp.toString()+")\n");
				String idSql = dimDAO.queryHisDealerIdsByCodesSql(dealerCode);
				if(idSql!=null&&idSql.length()>0){
					sb.append(" or b.dealer_id in ( ");
					sb.append(idSql);
					sb.append(" ) ");
				}
				sb.append(" ) ");
			}
		}
		DaoFactory.getsql(sb, "A.LINE_NO", lineNo, 1);
		DaoFactory.getsql(sb, "A.CLAIM_TYPE", claimType, 6);
		DaoFactory.getsql(sb, "A.CREATE_DATE", roStartdate, 3);
		DaoFactory.getsql(sb, "A.CREATE_DATE", roEnddate, 4);
		DaoFactory.getsql(sb, "A.STATUS", status, 1);
		DaoFactory.getsql(sb, "A.claim_No", claimNo.toUpperCase(), 2);
		DaoFactory.getsql(sb, "A.RO_NO", roNo.toUpperCase(), 2);
		DaoFactory.getsql(sb, "vm.model_code", model, 1);
		if(Utility.testString(partCode)){
			sb.append("   AND A.ID in ( SELECT TAWP.ID FROM TT_AS_WR_PARTSITEM TAWP WHERE TAWP.PART_CODE LIKE '%"+partCode+"%' GROUP BY TAWP.ID )  \n");
		}
		if(Utility.testString(wrLabourCode)){
			sb.append("   AND A.ID in ( SELECT TAWL.ID FROM TT_AS_WR_LABOURITEM TAWL WHERE TAWL.WR_LABOURCODE LIKE '%"+wrLabourCode+"%' GROUP BY TAWL.ID )  \n");
		}
		// 车辆VIN码
		if (Utility.testString(vin)) {sb.append(GetVinUtil.getVins(vin.replaceAll("'","\''"), "A"));
		}
		DaoFactory.getsql(sb, "A.is_import", isImport, 1);
		sb.append(" order by a.id desc ) t where 1=1 \n");
		DaoFactory.getsql(sb, "t.auditing_man", foreAuthPerson, 2);
		DaoFactory.getsql(sb, "t.report_date", approveDate, 3);
		DaoFactory.getsql(sb, "t.report_date", approveDate2, 4);
		PageResult<Map<String, Object>> list=pageQuery(sb.toString(), null,getFunName(), pageSize, currPage);
		return list;
	}

	@SuppressWarnings("unchecked")
	public PageResult<Map<String, Object>> queryapplicationlist(RequestWrapper request, AclUserBean loginUser, Integer pageSize,Integer currPage) {
		StringBuffer sql= new StringBuffer();
		sql.append("select distinct a.*,d.part_code,vd.maker_name,vd.maker_code,de.part_code as code,de.part_cname as name\n" );
		sql.append("        from tt_as_wr_application a,\n" );
		sql.append("             tt_as_wr_partsitem   d,\n" );
		sql.append("             tt_part_define       de,\n" );
		sql.append("             tt_part_maker_define vd,\n" );
		sql.append("             TT_PART_MAKER_RELATION p\n" );
		sql.append("       where a.id = d.id(+) and d.part_code=de.part_code(+) and de.part_id =p.part_id(+)  \n" );
		sql.append(" and P.MAKER_ID = VD.Maker_Id(+)  and a.claim_no not in (select pp.claim_no from tt_as_wr_old_out_part pp where pp.claim_no=a.claim_no) ");
		DaoFactory.getsql(sql, "vd.maker_code",DaoFactory.getParam(request,"supply_code") , 1);
        DaoFactory.getsql(sql, "a.claim_no",DaoFactory.getParam(request,"claim_no") , 2);
        DaoFactory.getsql(sql, "a.vin",DaoFactory.getParam(request,"vin") , 2);
		return this.pageQuery(sql.toString(), null, getFunName(), pageSize, currPage);
	}
}