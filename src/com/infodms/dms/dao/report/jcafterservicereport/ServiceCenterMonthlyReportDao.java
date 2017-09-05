package com.infodms.dms.dao.report.jcafterservicereport;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.infodms.dms.bean.ServiceCenterMonthlyReportBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.dao.common.BaseDao;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

@SuppressWarnings("unchecked")
public class ServiceCenterMonthlyReportDao extends BaseDao {

	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}


	/**
	 * 查询 轿车公司服务中心月申报费用明细表
	 * @param reportBean
	 * @param pageSize
	 * @param curPage
	 * @return
	 */
	public PageResult<Map<String,Object>> queryServiceCenterMonthlyReport(ServiceCenterMonthlyReportBean bean,
			Integer pageSize,Integer curPage){
		StringBuffer sql = new StringBuffer();

		sql.append("select S.DEALER_CODE,\n");
		sql.append("        d.dealer_shortname,\n" );
		sql.append("        s.root_dealer_name AS DEALERNAME,\n" );
		sql.append("              S.ROOT_ORG_NAME AS ORG_NAME,\n" );
		sql.append("              r.region_name,\n" );
		sql.append("              g.group_name,\n" );
		sql.append("              a.*");
		sql.append("          from (select dealer_id,\n" );
		sql.append("                       series_id,\n" );
		sql.append("                       SUM(CASE\n" );
		sql.append("                             WHEN n.claim_type in (").append(Constant.CLA_TYPE_01).append(",").append(Constant.CLA_TYPE_09).append(") THEN\n");
		sql.append("                                nvl(n.Labour_Amount,0)+nvl(n.apply_appendlabour_amount,0)\n" );
		sql.append("                             ELSE\n" );
		sql.append("                              0\n" );
		sql.append("                           END) as shgs, --售后工时费\n" );
		sql.append("                       SUM(CASE\n" );
		sql.append("                             WHEN n.claim_type in (").append(Constant.CLA_TYPE_01).append(",").append(Constant.CLA_TYPE_09).append(") THEN\n");
		sql.append("                                nvl(n.Part_Amount,0)\n" );
		sql.append("                             ELSE\n" );
		sql.append("                              0\n" );
		sql.append("                           END) as shcl, --售后材料费\n" );
		sql.append("\n" );
		sql.append("                       SUM(CASE\n" );
		sql.append("                             WHEN n.claim_type in (").append(Constant.CLA_TYPE_01).append(",").append(Constant.CLA_TYPE_09).append(") THEN\n");
		sql.append("                              1\n" );
		sql.append("                             ELSE\n" );
		sql.append("                              0\n" );
		sql.append("                           END) as shds, --售后单数\n" );
		sql.append("\n" );
		sql.append("                       SUM(CASE\n" );
		sql.append("                             WHEN n.claim_type = ").append(Constant.CLA_TYPE_07).append(" THEN\n" );
		sql.append("                                nvl(n.Labour_Amount,0)+nvl(n.appendlabour_amount,0)\n" );
		sql.append("                             ELSE\n" );
		sql.append("                              0\n" );
		sql.append("                           END) as sqgs, --售前工时费\n" );
		sql.append("                       SUM(CASE\n" );
		sql.append("                             WHEN n.claim_type = ").append(Constant.CLA_TYPE_07).append(" THEN\n" );
		sql.append("                               nvl(n.part_amount,0)\n" );
		sql.append("                             ELSE\n" );
		sql.append("                              0\n" );
		sql.append("                           END) as sqcl, --售前材料费\n" );
		sql.append("\n" );
		sql.append("                       SUM(CASE\n" );
		sql.append("                             WHEN n.claim_type = ").append(Constant.CLA_TYPE_07).append(" THEN\n" );
		sql.append("                              1\n" );
		sql.append("                             ELSE\n" );
		sql.append("                              0\n" );
		sql.append("                           END) as sqds, --售前单数\n" );
		sql.append("\n" );
		sql.append("                       SUM(CASE\n" );
		sql.append("                             WHEN n.claim_type in (").append(Constant.CLA_TYPE_01).append(",").append(Constant.CLA_TYPE_09).append(") THEN\n");
		sql.append("                              nvl(n.netitem_amount,0)\n" );
		sql.append("                             ELSE\n" );
		sql.append("                              0\n" );
		sql.append("                           END) as shwc, --售后外出费\n" );
		sql.append("\n" );
		sql.append("                       SUM(CASE\n" );
		sql.append("                             WHEN n.claim_type = ").append(Constant.CLA_TYPE_02).append(" THEN\n" );
		sql.append("                               nvl(n.free_m_price,0)+nvl(n.APPENDLABOUR_AMOUNT,0)\n" );
		sql.append("                             ELSE\n" );
		sql.append("                              0\n" );
		sql.append("                           END) as byf, --保养费\n" );
		sql.append("                       SUM(CASE\n" );
		sql.append("                             WHEN n.claim_type = ").append(Constant.CLA_TYPE_02).append(" THEN\n" );
		sql.append("                              1\n" );
		sql.append("                             ELSE\n" );
		sql.append("                              0\n" );
		sql.append("                           END) as byds, --保养单数\n" );
		sql.append("\n" );
		sql.append("                       SUM(CASE\n" );
		sql.append("                             WHEN claim_type = ").append(Constant.CLA_TYPE_06).append(" THEN\n" );
		sql.append("                              n.gross_credit\n" );
		sql.append("                             ELSE\n" );
		sql.append("                              0\n" );
		sql.append("                           END) as fwhd, --服务活动费\n" );
		sql.append("                       SUM(CASE\n" );
		sql.append("                             WHEN claim_type = ").append(Constant.CLA_TYPE_06).append(" THEN\n" );
		sql.append("                              1\n" );
		sql.append("                             ELSE\n" );
		sql.append("                              0\n" );
		sql.append("                           END) as fwhdds --服务活动单数\n" );
		sql.append("\n" );
		sql.append("                  from tt_as_wr_application n\n" );
		sql.append("                 where status in (" ).append(Constant.CLAIM_APPLY_ORD_TYPE_04).append(",");
		sql.append(Constant.CLAIM_APPLY_ORD_TYPE_07).append(",");
		sql.append(Constant.CLAIM_APPLY_ORD_TYPE_08).append(")\n");
		sql.append(" and n.claim_type IN (10661001, 10661007, 10661009,10661002,10661006 )");
		
		if(Utility.testString(bean.getSeriesName())){//车系
			sql.append("AND SERIES_id = '").append(bean.getSeriesName()).append("'\n");
		}
		if(Utility.testString(bean.getBeginTime())){//结算单开始时间
			sql.append("AND AUDITING_DATE >= TO_DATE('").append(bean.getBeginTime()).append(" 00:00:00', 'YYYY-MM-DD HH24:MI:SS')\n");
		}
		if(Utility.testString(bean.getEndTime())){//结算单结束时间
			sql.append("AND AUDITING_DATE <= TO_DATE('").append(bean.getEndTime()).append(" 23:59:59', 'YYYY-MM-DD HH24:MI:SS')\n");
		}
		sql.append("                 group by dealer_id, series_id) a,\n" );
		sql.append("VW_ORG_DEALER_SERVICE S,TM_DEALER D,tm_region r,tm_vhcl_material_group g\n");
		sql.append("                WHERE  a.DEALER_ID = S.DEALER_ID\n" );
		sql.append("                AND a.dealer_id = d.dealer_id\n" );
		sql.append("                and r.region_code = d.province_id\n" );
		sql.append("                and a.series_id = g.group_id \n");
		if(Utility.testString(bean.getDealerCode())){
		sql.append(" and d.dealer_code in ( ").append(bean.getDealerCode()).append(") \n");
		}
		if(Utility.testString(bean.getAreaName())){
		sql.append("               and s.root_org_id=").append(bean.getAreaName()).append("\n");
		}
		if(Utility.testString(bean.getSeriesName())){
		sql.append("               and g.group_id='").append(bean.getSeriesName()).append("' \n");
		}
		

		return this.pageQuery(sql.toString(), null, this.getFunName(), pageSize, curPage);
	}
	/**
	 * 查询 轿车公司服务中心月申报费用明细表
	 * @param reportBean
	 * @param pageSize
	 * @param curPage 
	 * @return
	 */
	
	public List<Map<String,Object>> queryServiceCenterMonthlyReport(ServiceCenterMonthlyReportBean bean){
		
		StringBuffer sql = new StringBuffer();
		sql.append("select S.DEALER_CODE,\n");
		sql.append("        d.dealer_shortname,\n" );
		sql.append("        s.root_dealer_name AS DEALERNAME,\n" );
		sql.append("              S.ROOT_ORG_NAME AS ORG_NAME,\n" );
		sql.append("              r.region_name,\n" );
		sql.append("              g.group_name,\n" );
		sql.append("              a.*");
		sql.append("          from (select dealer_id,\n" );
		sql.append("                       series_id,series_name,\n" );
		sql.append("                       SUM(CASE\n" );
		sql.append("                             WHEN n.claim_type in (").append(Constant.CLA_TYPE_01).append(",").append(Constant.CLA_TYPE_09).append(") THEN\n");
		sql.append("                                nvl(n.Labour_Amount,0)+nvl(n.appendlabour_amount,0)\n" );
		sql.append("                             ELSE\n" );
		sql.append("                              0\n" );
		sql.append("                           END) as shgs, --售后工时费\n" );
		sql.append("                       SUM(CASE\n" );
		sql.append("                             WHEN n.claim_type in (").append(Constant.CLA_TYPE_01).append(",").append(Constant.CLA_TYPE_09).append(") THEN\n");
		sql.append("                                nvl(n.Part_Amount,0)\n" );
		sql.append("                             ELSE\n" );
		sql.append("                              0\n" );
		sql.append("                           END) as shcl, --售后材料费\n" );
		sql.append("\n" );
		sql.append("                       SUM(CASE\n" );
		sql.append("                             WHEN n.claim_type in (").append(Constant.CLA_TYPE_01).append(",").append(Constant.CLA_TYPE_09).append(") THEN\n");
		sql.append("                              1\n" );
		sql.append("                             ELSE\n" );
		sql.append("                              0\n" );
		sql.append("                           END) as shds, --售后单数\n" );
		sql.append("\n" );
		sql.append("                       SUM(CASE\n" );
		sql.append("                             WHEN n.claim_type = ").append(Constant.CLA_TYPE_07).append(" THEN\n" );
		sql.append("                                nvl(n.Labour_Amount,0)+nvl(n.appendlabour_amount,0)\n" );
		sql.append("                             ELSE\n" );
		sql.append("                              0\n" );
		sql.append("                           END) as sqgs, --售前工时费\n" );
		sql.append("                       SUM(CASE\n" );
		sql.append("                             WHEN n.claim_type = ").append(Constant.CLA_TYPE_07).append(" THEN\n" );
		sql.append("                               nvl(n.part_amount,0)\n" );
		sql.append("                             ELSE\n" );
		sql.append("                              0\n" );
		sql.append("                           END) as sqcl, --售前材料费\n" );
		sql.append("\n" );
		sql.append("                       SUM(CASE\n" );
		sql.append("                             WHEN n.claim_type = ").append(Constant.CLA_TYPE_07).append(" THEN\n" );
		sql.append("                              1\n" );
		sql.append("                             ELSE\n" );
		sql.append("                              0\n" );
		sql.append("                           END) as sqds, --售前单数\n" );
		sql.append("\n" );
		sql.append("                       SUM(CASE\n" );
		sql.append("                             WHEN n.claim_type in (").append(Constant.CLA_TYPE_01).append(",").append(Constant.CLA_TYPE_09).append(") THEN\n");
		sql.append("                              nvl(n.netitem_amount,0)\n" );
		sql.append("                             ELSE\n" );
		sql.append("                              0\n" );
		sql.append("                           END) as shwc, --售后外出费\n" );
		sql.append("\n" );
		sql.append("                       SUM(CASE\n" );
		sql.append("                             WHEN n.claim_type = ").append(Constant.CLA_TYPE_02).append(" THEN\n" );
		sql.append("                               nvl(n.free_m_price,0)+nvl(n.APPENDLABOUR_AMOUNT,0)\n" );
		sql.append("                             ELSE\n" );
		sql.append("                              0\n" );
		sql.append("                           END) as byf, --保养费\n" );
		sql.append("                       SUM(CASE\n" );
		sql.append("                             WHEN n.claim_type = ").append(Constant.CLA_TYPE_02).append(" THEN\n" );
		sql.append("                              1\n" );
		sql.append("                             ELSE\n" );
		sql.append("                              0\n" );
		sql.append("                           END) as byds, --保养单数\n" );
		sql.append("\n" );
		sql.append("                       SUM(CASE\n" );
		sql.append("                             WHEN claim_type = ").append(Constant.CLA_TYPE_06).append(" THEN\n" );
		sql.append("                              n.gross_credit\n" );
		sql.append("                             ELSE\n" );
		sql.append("                              0\n" );
		sql.append("                           END) as fwhd, --服务活动费\n" );
		sql.append("                       SUM(CASE\n" );
		sql.append("                             WHEN claim_type = ").append(Constant.CLA_TYPE_06).append(" THEN\n" );
		sql.append("                              1\n" );
		sql.append("                             ELSE\n" );
		sql.append("                              0\n" );
		sql.append("                           END) as fwhdds --服务活动单数\n" );
		sql.append("\n" );
		sql.append("                  from tt_as_wr_application n\n" );
		sql.append("                 where status in (" ).append(Constant.CLAIM_APPLY_ORD_TYPE_04).append(",");
		sql.append(Constant.CLAIM_APPLY_ORD_TYPE_07).append(",");
		sql.append(Constant.CLAIM_APPLY_ORD_TYPE_08).append(")\n");
		sql.append(" and n.claim_type IN (10661001, 10661007, 10661009 ,10661002,10661006)");
		
		if(Utility.testString(bean.getSeriesName())){//车系
			sql.append("AND SERIES_id = '").append(bean.getSeriesName()).append("'\n");
		}
		if(Utility.testString(bean.getBeginTime())){//结算单开始时间
			sql.append("AND AUDITING_DATE >= TO_DATE('").append(bean.getBeginTime()).append(" 00:00:00', 'YYYY-MM-DD HH24:MI:SS')\n");
		}
		if(Utility.testString(bean.getEndTime())){//结算单结束时间
			sql.append("AND AUDITING_DATE <= TO_DATE('").append(bean.getEndTime()).append(" 23:59:59', 'YYYY-MM-DD HH24:MI:SS')\n");
		}
		sql.append("                 group by dealer_id, series_id,series_name) a,\n" );
		sql.append("VW_ORG_DEALER_SERVICE S,TM_DEALER D,tm_region r,tm_vhcl_material_group g\n");
		sql.append("                WHERE  a.DEALER_ID = S.DEALER_ID\n" );
		sql.append("                AND a.dealer_id = d.dealer_id\n" );
		sql.append("                and r.region_code = d.province_id\n" );
		sql.append("                and a.series_id = g.group_id \n");
		if(Utility.testString(bean.getDealerCode())){
		sql.append(" and d.dealer_code in ( '").append(bean.getDealerCode()).append("') \n");
		}
		if(Utility.testString(bean.getAreaName())){
		sql.append("               and s.root_org_id=").append(bean.getAreaName()).append("\n");
		}
		if(Utility.testString(bean.getSeriesName())){
		sql.append("               and g.group_id='").append(bean.getSeriesName()).append("' \n");
		}
		return this.pageQuery(sql.toString(), null, this.getFunName());
	}
	public List<Map<String,Object>> threeGuaranteesAuditReportView(String beginTime,String endTime,String audit_beginTime,String audit_endTime,String dealerCode,String areaName){
		StringBuffer sql = new StringBuffer();




		sql.append("select a.* ,CB.DEALER_CODE,CB.START_DATE,CB.END_DATE, CB.DEALER_NAME AS DEALER_name,\n");
		sql.append("        CB.BALANCE_NO as BALANCE_NO,\n" );
		sql.append("        nvl(cb.labour_amount,0) as labour_amount,\n" );
		sql.append("        nvl(cb.Part_Amount,0) as Part_Amount,\n" );
		sql.append("        nvl(cb.free_amount,0) as free_amount,\n" );
		sql.append("        nvl(cb.SERVICE_TOTAL_AMOUNT,0) as SERVICE_TOTAL_AMOUNT,\n" );
		sql.append("        nvl(CB.OTHER_AMOUNT,0)  AS OTHER_AMOUNT,\n" );
		sql.append("        nvl(cb.RETURN_AMOUNT,0) as RETURN_AMOUNT,\n" );
		sql.append("        nvl(cb.SPEOUTFEE_AMOUNT,0) as SPEOUTFEE_AMOUNT,\n" );
		sql.append("        nvl(cb.FREE_DEDUCT,0)  as  FREE_DEDUCT,\n" );
		sql.append("        nvl(cb.SERVICE_DEDUCT,0) as sum_SERVICE_DEDUCT,\n" );
		sql.append("        nvl(cb.OLD_DEDUCT,0) as OLD_DEDUCT,\n" );
		sql.append("        nvl(cb.CHECK_DEDUCT,0) as CHECK_DEDUCT,\n" );
		sql.append("        nvl(cb.Apply_Amount,0) as Apply_Amount,\n" );
		sql.append("         (nvl(cb.labour_amount_bak,0)+nvl(cb.part_amount_bak,0)+nvl(cb.service_total_amount_bak,0)+nvl(cb.append_labour_amount_bak,0)+nvl(cb.free_amount_bak,0)+nvl(cb.return_amount_bak,0))-(nvl(cb.labour_amount,0)+nvl(cb.part_amount,0)+nvl(cb.service_total_amount,0)+nvl(cb.append_labour_amount,0)+nvl(cb.free_amount,0)+nvl(return_amount,0)) as SUM_KKZJ, cb.invoice_maker ,\n" );
		sql.append("(nvl(cb.labour_amount_bak,0)+nvl(cb.part_amount_bak,0)+nvl(cb.service_total_amount_bak,0)+nvl(cb.speoutfee_amount_bak,0)+nvl(cb.market_amount_bak,0)+nvl(cb.return_amount_bak,0)+nvl(cb.free_amount_bak,0)+nvl(cb.other_amount_bak,0))\n");
		sql.append("      -(nvl(cb.labour_amount,0)+nvl(cb.part_amount,0)+nvl(cb.service_total_amount,0)+nvl(cb.speoutfee_amount,0)+nvl(cb.market_amount,0)+nvl(cb.return_amount,0)+nvl(cb.free_amount,0)+nvl(cb.other_amount,0))\n" );
		sql.append("      as SUM_KKZJ_SH,");
		sql.append("        nvl(CB.BALANCE_AMOUNT,0) BALANCE_AMOUNT,\n" );
		sql.append("       c.auth_person_name,\n" );
		sql.append("       C.CREATE_DATE,\n" );
		sql.append("       vw.root_org_name,\n" );
		sql.append("       cb.Service_Total_Amount_Bak as SERVICE_TOTAL_AMOUNT from (SELECT a.balance_id,\n" );
		sql.append("       nvl(sum(decode(a.series_name, '奔奔MINI', a.BEFORE_CLAIM_COUNT)), 0) bbmi_sqspds,\n" );
		sql.append("       nvl(sum(decode(a.series_name, '奔奔MINI', a.BEFORE_PART_AMOUNT)), 0) bbmi_sqclf,\n" );
		sql.append("       nvl(sum(decode(a.series_name, '奔奔MINI', a.BEFORE_LABOUR_AMOUNT)),\n" );
		sql.append("           0) bbmi_sqgsf,\n" );
		sql.append("       nvl(sum(decode(a.series_name, '奔奔MINI', a.AFTER_CLAIM_COUNT)), 0) bbmi_shspds,\n" );
		sql.append("       nvl(sum(decode(a.series_name, '奔奔MINI', a.AFTER_PART_AMOUNT)), 0) bbmi_shclf,\n" );
		sql.append("       nvl(sum(decode(a.series_name, '奔奔MINI', a.AFTER_LABOUR_AMOUNT)), 0) bbmi_SHGSF,\n" );
		sql.append("       nvl(sum(decode(a.series_name, '奔奔MINI', a.FREE_CLAIM_COUNT)), 0) bbmi_mfbyds,\n" );
		sql.append("       nvl(sum(decode(a.series_name, '奔奔MINI', a.FREE_CLAIM_AMOUNT)), 0) bbmi_mfbyzfy,\n" );
		sql.append("       nvl(sum(decode(a.series_name, '奔奔MINI', a.SERVICE_CLAIM_COUNT)), 0) bbmi_fwhdspds,\n" );
		sql.append("       nvl(sum(decode(a.series_name, '悦翔', a.BEFORE_CLAIM_COUNT)), 0) yx_sqspds,\n" );
		sql.append("       nvl(sum(decode(a.series_name, '悦翔', a.BEFORE_PART_AMOUNT)), 0) yx_sqclf,\n" );
		sql.append("       nvl(sum(decode(a.series_name, '悦翔', a.BEFORE_LABOUR_AMOUNT)), 0) yx_sqgsf,\n" );
		sql.append("       nvl(sum(decode(a.series_name, '悦翔', a.AFTER_CLAIM_COUNT)), 0) yx_shspds,\n" );
		sql.append("       nvl(sum(decode(a.series_name, '悦翔', a.AFTER_PART_AMOUNT)), 0) yx_shclf,\n" );
		sql.append("       nvl(sum(decode(a.series_name, '悦翔', a.AFTER_LABOUR_AMOUNT)), 0) yx_SHGSF,\n" );
		sql.append("       nvl(sum(decode(a.series_name, '悦翔', a.FREE_CLAIM_COUNT)), 0) YX_mfbyds,\n" );
		sql.append("       nvl(sum(decode(a.series_name, '悦翔', a.FREE_CLAIM_AMOUNT)), 0) yx_mfbyzfy,\n" );
		sql.append("       nvl(sum(decode(a.series_name, '悦翔', a.SERVICE_CLAIM_COUNT)), 0) yx_fwhdspds,\n" );
		sql.append("       ----\n" );
		sql.append("       nvl(sum(decode(a.series_name, '杰勋', a.BEFORE_CLAIM_COUNT)), 0) jx_sqspds,\n" );
		sql.append("       nvl(sum(decode(a.series_name, '杰勋', a.BEFORE_PART_AMOUNT)), 0) jx_sqclf,\n" );
		sql.append("       nvl(sum(decode(a.series_name, '杰勋', a.BEFORE_LABOUR_AMOUNT)), 0) jx_sqgsf,\n" );
		sql.append("       nvl(sum(decode(a.series_name, '杰勋', a.AFTER_CLAIM_COUNT)), 0) jx_shspds,\n" );
		sql.append("       nvl(sum(decode(a.series_name, '杰勋', a.AFTER_PART_AMOUNT)), 0) jx_shclf,\n" );
		sql.append("       nvl(sum(decode(a.series_name, '杰勋', a.AFTER_LABOUR_AMOUNT)), 0) jx_SHGSF,\n" );
		sql.append("       nvl(sum(decode(a.series_name, '杰勋', a.FREE_CLAIM_COUNT)), 0) jX_mfbyds,\n" );
		sql.append("       nvl(sum(decode(a.series_name, '杰勋', a.FREE_CLAIM_AMOUNT)), 0) jx_mfbyzfy,\n" );
		sql.append("       nvl(sum(decode(a.series_name, '杰勋', a.SERVICE_CLAIM_COUNT)), 0) jx_fwhdspds,\n" );
		sql.append("       ---奔奔\n" );
		sql.append("       nvl(sum(decode(a.series_name, '奔奔', a.BEFORE_CLAIM_COUNT)), 0) bb_sqspds,\n" );
		sql.append("       nvl(sum(decode(a.series_name, '奔奔', a.BEFORE_PART_AMOUNT)), 0) bb_sqclf,\n" );
		sql.append("       nvl(sum(decode(a.series_name, '奔奔', a.BEFORE_LABOUR_AMOUNT)), 0) bb_sqgsf,\n" );
		sql.append("       nvl(sum(decode(a.series_name, '奔奔', a.AFTER_CLAIM_COUNT)), 0) bb_shspds,\n" );
		sql.append("       nvl(sum(decode(a.series_name, '奔奔', a.AFTER_PART_AMOUNT)), 0) bb_shclf,\n" );
		sql.append("       nvl(sum(decode(a.series_name, '奔奔', a.AFTER_LABOUR_AMOUNT)), 0) bb_SHGSF,\n" );
		sql.append("       nvl(sum(decode(a.series_name, '奔奔', a.FREE_CLAIM_COUNT)), 0) bb_mfbyds,\n" );
		sql.append("       nvl(sum(decode(a.series_name, '奔奔', a.FREE_CLAIM_AMOUNT)), 0) bb_mfbyzfy,\n" );
		sql.append("       nvl(sum(decode(a.series_name, '奔奔', a.SERVICE_CLAIM_COUNT)), 0) bb_fwhdspds,\n" );
		sql.append("       ---志翔\n" );
		sql.append("       nvl(sum(decode(a.series_name, '志翔', a.BEFORE_CLAIM_COUNT)), 0) zx_sqspds,\n" );
		sql.append("       nvl(sum(decode(a.series_name, '志翔', a.BEFORE_PART_AMOUNT)), 0) zx_sqclf,\n" );
		sql.append("       nvl(sum(decode(a.series_name, '志翔', a.BEFORE_LABOUR_AMOUNT)), 0) zx_sqgsf,\n" );
		sql.append("       nvl(sum(decode(a.series_name, '志翔', a.AFTER_CLAIM_COUNT)), 0) zx_shspds,\n" );
		sql.append("       nvl(sum(decode(a.series_name, '志翔', a.AFTER_PART_AMOUNT)), 0) zx_shclf,\n" );
		sql.append("       nvl(sum(decode(a.series_name, '志翔', a.AFTER_LABOUR_AMOUNT)), 0) zx_SHGSF,\n" );
		sql.append("       nvl(sum(decode(a.series_name, '志翔', a.FREE_CLAIM_COUNT)), 0) zx_mfbyds,\n" );
		sql.append("       nvl(sum(decode(a.series_name, '志翔', a.FREE_CLAIM_AMOUNT)), 0) zx_mfbyzfy,\n" );
		sql.append("       nvl(sum(decode(a.series_name, '志翔', a.SERVICE_CLAIM_COUNT)), 0) zx_fwhdspds,\n" );
		sql.append("       ----CX30\n" );
		sql.append("       nvl(sum(decode(a.series_code, 'CX30', a.BEFORE_CLAIM_COUNT)), 0) cx30_sqspds,\n" );
		sql.append("       nvl(sum(decode(a.series_code, 'CX30', a.BEFORE_PART_AMOUNT)), 0) cx30_sqclf,\n" );
		sql.append("       nvl(sum(decode(a.series_code, 'CX30', a.BEFORE_LABOUR_AMOUNT)), 0) cx30_sqgsf,\n" );
		sql.append("       nvl(sum(decode(a.series_code, 'CX30', a.AFTER_CLAIM_COUNT)), 0) cx30_shspds,\n" );
		sql.append("       nvl(sum(decode(a.series_code, 'CX30', a.AFTER_PART_AMOUNT)), 0) cx30_shclf,\n" );
		sql.append("       nvl(sum(decode(a.series_code, 'CX30', a.AFTER_LABOUR_AMOUNT)), 0) cx30_SHGSF,\n" );
		sql.append("       nvl(sum(decode(a.series_code, 'CX30', a.FREE_CLAIM_COUNT)), 0) cx30_mfbyds,\n" );
		sql.append("       nvl(sum(decode(a.series_code, 'CX30', a.FREE_CLAIM_AMOUNT)), 0) cx30_mfbyzfy,\n" );
		sql.append("       nvl(sum(decode(a.series_code, 'CX30', a.SERVICE_CLAIM_COUNT)), 0) cx30_fwhdspds,\n" );
		sql.append("       ---CX20\n" );
		sql.append("       nvl(sum(decode(a.series_name, 'CX20', a.BEFORE_CLAIM_COUNT)), 0) cx20_sqspds,\n" );
		sql.append("       nvl(sum(decode(a.series_name, 'CX20', a.BEFORE_PART_AMOUNT)), 0) cx20_sqclf,\n" );
		sql.append("       nvl(sum(decode(a.series_name, 'CX20', a.BEFORE_LABOUR_AMOUNT)), 0) cx20_sqgsf,\n" );
		sql.append("       nvl(sum(decode(a.series_name, 'CX20', a.AFTER_CLAIM_COUNT)), 0) cx20_shspds,\n" );
		sql.append("       nvl(sum(decode(a.series_name, 'CX20', a.AFTER_PART_AMOUNT)), 0) cx20_shclf,\n" );
		sql.append("       nvl(sum(decode(a.series_name, 'CX20', a.AFTER_LABOUR_AMOUNT)), 0) cx20_SHGSF,\n" );
		sql.append("       nvl(sum(decode(a.series_name, 'CX20', a.FREE_CLAIM_COUNT)), 0) cx20_mfbyds,\n" );
		sql.append("       nvl(sum(decode(a.series_name, 'CX20', a.FREE_CLAIM_AMOUNT)), 0) cx20_mfbyzfy,\n" );
		sql.append("       nvl(sum(decode(a.series_name, 'CX20', a.SERVICE_CLAIM_COUNT)), 0) cx20_fwhdspds,\n" );
		sql.append("       ------\n" );
		sql.append("       nvl(sum(decode(a.series_name, 'CX30三厢', a.BEFORE_CLAIM_COUNT)), 0) cx30sx_sqspds,\n" );
		sql.append("       nvl(sum(decode(a.series_name, 'CX30三厢', a.BEFORE_PART_AMOUNT)), 0) cx30sx_sqclf,\n" );
		sql.append("       nvl(sum(decode(a.series_name, 'CX30三厢', a.BEFORE_LABOUR_AMOUNT)),\n" );
		sql.append("           0) cx30sx_sqgsf,\n" );
		sql.append("       nvl(sum(decode(a.series_name, 'CX30三厢', a.AFTER_CLAIM_COUNT)), 0) cx30sx_shspds,\n" );
		sql.append("       nvl(sum(decode(a.series_name, 'CX30三厢', a.AFTER_PART_AMOUNT)), 0) cx30sx_shclf,\n" );
		sql.append("       nvl(sum(decode(a.series_name, 'CX30三厢', a.AFTER_LABOUR_AMOUNT)), 0) cx30sx_SHGSF,\n" );
		sql.append("       nvl(sum(decode(a.series_name, 'CX30三厢', a.FREE_CLAIM_COUNT)), 0) cx30sx_mfbyds,\n" );
		sql.append("       nvl(sum(decode(a.series_name, 'CX30三厢', a.FREE_CLAIM_AMOUNT)), 0) cx30sx_mfbyzfy,\n" );
		sql.append("       nvl(sum(decode(a.series_name, 'CX30三厢', a.SERVICE_CLAIM_COUNT)), 0) cx30sx_fwhdspds,\n" );
		sql.append("       ---SC7133BR.CFA\n" );
		sql.append("       nvl(sum(decode(a.series_name, 'SC7133BR.CFA', a.BEFORE_CLAIM_COUNT)),\n" );
		sql.append("           0) SC7133BR_sqspds,\n" );
		sql.append("       nvl(sum(decode(a.series_name, 'SC7133BR.CFA', a.BEFORE_PART_AMOUNT)),\n" );
		sql.append("           0) SC7133BR_sqclf,\n" );
		sql.append("       nvl(sum(decode(a.series_name, 'SC7133BR.CFA', a.BEFORE_LABOUR_AMOUNT)),\n" );
		sql.append("           0) SC7133BR_sqgsf,\n" );
		sql.append("       nvl(sum(decode(a.series_name, 'SC7133BR.CFA', a.AFTER_CLAIM_COUNT)),\n" );
		sql.append("           0) SC7133BR_shspds,\n" );
		sql.append("       nvl(sum(decode(a.series_name, 'SC7133BR.CFA', a.AFTER_PART_AMOUNT)),\n" );
		sql.append("           0) SC7133BR_shclf,\n" );
		sql.append("       nvl(sum(decode(a.series_name, 'SC7133BR.CFA', a.AFTER_LABOUR_AMOUNT)),\n" );
		sql.append("           0) SC7133BR_SHGSF,\n" );
		sql.append("       nvl(sum(decode(a.series_name, 'SC7133BR.CFA', a.FREE_CLAIM_COUNT)),\n" );
		sql.append("           0) SC7133BR_mfbyds,\n" );
		sql.append("       nvl(sum(decode(a.series_name, 'SC7133BR.CFA', a.FREE_CLAIM_AMOUNT)),\n" );
		sql.append("           0) SC7133BR_mfbyzfy,\n" );
		sql.append("       nvl(sum(decode(a.series_name, 'SC7133BR.CFA', a.SERVICE_CLAIM_COUNT)),\n" );
		sql.append("           0) SC7133BR_fwhdspds\n" );
		sql.append("  FROM TT_AS_WR_CLAIM_BALANCE_DETAIL A\n" );
		sql.append(" group by a.balance_id) a,(\n" );
		sql.append(" select ba.balance_id,ba.auth_person_name ,max(ba.create_date) as CREATE_DATE from Tt_As_Wr_Balance_Authitem  ba where\n" );
		sql.append(" ba.auth_status=11861004\n" );
		sql.append(" group by ba.balance_id,ba.auth_person_name\n" );
		sql.append(" ) c,\n" );
		sql.append(" Tt_As_Wr_Claim_Balance cb ,VW_ORG_DEALER_SERVICE VW\n" );
		sql.append(" where a.balance_id = cb.id\n" );
		sql.append(" and cb.id = c.balance_id\n" );
		sql.append(" AND cb.dealer_id = vw.dealer_id \n");
		if(Utility.testString(beginTime)){
			sql.append("and cb.start_date>=to_date('"+beginTime+"','yyyy-MM-dd')  \n");
		}
		if(Utility.testString(endTime)){
		sql.append(" and cb.end_date<=to_date('"+endTime+"','yyyy-MM-dd')  \n" );
		}
		if(Utility.testString(audit_beginTime)){
		sql.append(" and c.create_date>=to_date('"+audit_beginTime+"','yyyy-MM-dd')  \n" );
		}
		if(Utility.testString(audit_endTime)){
			sql.append(" and c.create_date<=to_date('"+audit_endTime+"','yyyy-MM-dd'  )\n" );	
		}
		if(Utility.testString(dealerCode)){
		sql.append(" and cb.dealer_code='"+dealerCode+"'  \n" );
		}
		if(Utility.testString(areaName)){
		sql.append(" and vw.root_org_id ='"+areaName+"' ");
		}


		return this.pageQuery(sql.toString(), null, this.getFunName());
		
	}
	public List<Map<String,Object>> totalThreeGuaranteesAuditReportView(String beginTime,String endTime,String audit_beginTime,String audit_endTime,String dealerCode,String areaName){
		StringBuffer sql = new StringBuffer();

		sql.append("select sum(a.bbmi_sqspds) as bbmi_sqspds,\n");

		sql.append("sum(a.bbmi_sqclf) as bbmi_sqclf,\n");
		sql.append("      sum(a.bbmi_sqgsf) as bbmi_sqgsf,\n" );
		sql.append("      sum(a.bbmi_shspds) as bbmi_shspds,\n" );
		sql.append("      sum(a.bbmi_shclf) as bbmi_shclf,\n" );
		sql.append("      sum(a.bbmi_SHGSF) as bbmi_SHGSF,\n" );
		sql.append("      sum(a.bbmi_mfbyds) as bbmi_mfbyds,\n" );
		sql.append("      sum(a.bbmi_mfbyzfy) as bbmi_mfbyzfy,\n" );
		sql.append("      sum(a.bbmi_fwhdspds) as bbmi_fwhdspds,\n" );
		sql.append("      sum(a.bb_sqspds) as bb_sqspds,\n" );
		sql.append("      sum(a.bb_sqclf) as bb_sqclf,\n" );
		sql.append("      sum(a.bb_sqclf) as bb_sqgsf,\n" );
		sql.append("      sum(a.bb_shspds) as bb_shspds,\n" );
		sql.append("      sum(a.bb_shclf) as bb_shclf,\n" );
		sql.append("      sum(a.bb_SHGSF) as bb_SHGSF,\n" );
		sql.append("      sum(a.bb_mfbyds) as bb_mfbyds,\n" );
		sql.append("      sum(a.bb_mfbyzfy) as bb_mfbyzfy,\n" );
		sql.append("      sum(a.bb_fwhdspds) as bb_fwhdspds,\n" );
		sql.append("      sum(a.yx_sqspds) as yx_sqspds,\n" );
		sql.append("      sum(a.yx_sqclf) as yx_sqclf,\n" );
		sql.append("      sum(a.yx_sqgsf) as yx_sqgsf,\n" );
		sql.append("      sum(a.yx_shspds) as yx_shspds,\n" );
		sql.append("      sum(a.yx_shclf) as yx_shclf,\n" );
		sql.append("      sum(a.yx_SHGSF) as yx_SHGSF,\n" );
		sql.append("      sum(a.yx_mfbyds) as yx_mfbyds,\n" );
		sql.append("      sum(a.yx_mfbyzfy) as yx_mfbyzfy,\n" );
		sql.append("      sum(a.yx_fwhdspds) as yx_fwhdspds,\n" );
		sql.append("      sum(a.zx_sqspds) as zx_sqspds,\n" );
		sql.append("      sum(a.zx_sqclf) as zx_sqclf,\n" );
		sql.append("      sum(a.zx_sqgsf) as zx_sqgsf,\n" );
		sql.append("      sum(a.zx_shspds) as zx_shspds,\n" );
		sql.append("      sum(a.zx_shclf) as zx_shclf,\n" );
		sql.append("      sum(a.zx_SHGSF) as zx_SHGSF,\n" );
		sql.append("      sum(a.zx_mfbyds) as zx_mfbyds,\n" );
		sql.append("      sum(a.zx_mfbyzfy) as zx_mfbyzfy,\n" );
		sql.append("      sum(a.zx_fwhdspds) as zx_fwhdspds,\n" );
		sql.append("      sum(a.jx_sqspds) as jx_sqspds,\n" );
		sql.append("      sum(a.jx_sqclf) as jx_sqclf,\n" );
		sql.append("      sum(a.jx_sqgsf) as jx_sqgsf,\n" );
		sql.append("      sum(a.jx_shspds) as jx_shspds,\n" );
		sql.append("      sum(a.jx_shclf) as jx_shclf,\n" );
		sql.append("      sum(a.jx_SHGSF) as jx_SHGSF,\n" );
		sql.append("      sum(a.jx_mfbyds) as jx_mfbyds,\n" );
		sql.append("      sum(a.jx_mfbyzfy) as jx_mfbyzfy,\n" );
		sql.append("      sum(a.jx_fwhdspds) as jx_fwhdspds,\n" );
		sql.append("      sum(a.cx30_sqspds) as cx30_sqspds,\n" );
		sql.append("      sum(a.cx30_sqclf) as cx30_sqclf,\n" );
		sql.append("      sum(a.cx30_sqgsf) as cx30_sqgsf,\n" );
		sql.append("      sum(a.cx30_shspds) as cx30_shspds,\n" );
		sql.append("      sum(a.cx30_shclf) as cx30_shclf,\n" );
		sql.append("      sum(a.cx30_SHGSF) as cx30_SHGSF,\n" );
		sql.append("      sum(a.cx30_mfbyds) as cx30_mfbyds,\n" );
		sql.append("      sum(a.cx30_mfbyzfy) as cx30_mfbyzfy,\n" );
		sql.append("      sum(a.cx30_fwhdspds) as cx30_fwhdspds,\n" );
		sql.append("      sum(a.cx20_sqspds) as cx20_sqspds,\n" );
		sql.append("      sum(a.cx20_sqclf) as cx20_sqclf,\n" );
		sql.append("      sum(a.cx20_sqgsf) as cx20_sqgsf,\n" );
		sql.append("      sum(a.cx20_shspds) as cx20_shspds,\n" );
		sql.append("      sum(a.cx20_shclf) as cx20_shclf,\n" );
		sql.append("      sum(a.cx20_SHGSF) as cx20_SHGSF,\n" );
		sql.append("      sum(a.cx20_mfbyds) as cx20_mfbyds,\n" );
		sql.append("      sum(a.cx20_mfbyzfy) as cx20_mfbyzfy,\n" );
		sql.append("      sum(a.cx20_fwhdspds) as cx20_fwhdspds,\n" );
		sql.append("      sum(a.cx30sx_sqspds) as cx30sx_sqspds,\n" );
		sql.append("      sum(a.cx30sx_sqclf) as cx30sx_sqclf,\n" );
		sql.append("      sum(a.cx30sx_shspds) as cx30sx_shspds,\n" );
		sql.append("      sum(a.cx30sx_sqgsf) as cx30sx_sqgsf,\n" );
		sql.append("      sum(a.cx30sx_shclf) as cx30sx_shclf,\n" );
		sql.append("      sum(a.cx30sx_SHGSF) as cx30sx_SHGSF,\n" );
		sql.append("      sum(a.cx30sx_mfbyds) as cx30sx_mfbyds,\n" );
		sql.append("      sum(a.cx30sx_mfbyzfy) as cx30sx_mfbyzfy,\n" );
		sql.append("      sum(a.cx30sx_fwhdspds) as cx30sx_fwhdspds,\n" );
		sql.append("      sum(a.cx30sx_sqspds) as cx30sx_sqspds,\n" );
		sql.append("      sum(a.SC7133BR_sqclf) as SC7133BR_sqclf,\n" );
		sql.append("      sum(a.SC7133BR_shspds) as SC7133BR_shspds,\n" );
		sql.append("      sum(a.SC7133BR_sqgsf) as SC7133BR_sqgsf,\n" );
		sql.append("      sum(a.SC7133BR_shclf) as SC7133BR_shclf,\n" );
		sql.append("sum(a.SC7133BR_sqspds) as SC7133BR_sqspds,");
		sql.append("      sum(a.SC7133BR_SHGSF) as SC7133BR_SHGSF,\n" );
		sql.append("      sum(a.SC7133BR_mfbyds) as SC7133BR_mfbyds,\n" );
		sql.append("      sum(a.SC7133BR_mfbyzfy) as SC7133BR_mfbyzfy,\n" );
		sql.append("      sum(a.SC7133BR_fwhdspds) as SC7133BR_fwhdspds,\n" );
		sql.append("      sum(nvl(cb.labour_amount, 0)) as labour_amount,\n" );
		sql.append("      sum(nvl(cb.Part_Amount, 0)) as Part_Amount,\n" );
		sql.append("      sum(nvl(cb.free_amount, 0)) as free_amount,\n" );
		sql.append("      sum(nvl(cb.SERVICE_TOTAL_AMOUNT, 0)) as SERVICE_TOTAL_AMOUNT,\n" );
		sql.append("      sum(nvl(CB.OTHER_AMOUNT, 0)) AS OTHER_AMOUNT,\n" );
		sql.append("      sum(nvl(cb.RETURN_AMOUNT, 0)) as RETURN_AMOUNT,\n" );
		sql.append("      sum(nvl(cb.SPEOUTFEE_AMOUNT, 0)) as SPEOUTFEE_AMOUNT,\n" );
		sql.append("      sum(nvl(cb.FREE_DEDUCT, 0)) as FREE_DEDUCT,\n" );
		sql.append("      sum(nvl(cb.SERVICE_DEDUCT, 0)) as sum_SERVICE_DEDUCT,\n" );
		sql.append("      sum(nvl(cb.OLD_DEDUCT, 0)) as OLD_DEDUCT,\n" );
		sql.append("      sum(nvl(cb.CHECK_DEDUCT, 0)) as CHECK_DEDUCT,\n" );
		sql.append("      sum(nvl(cb.Apply_Amount, 0)) as Apply_Amount,");
		sql.append("       sum(nvl(cb.SERVICE_TOTAL_AMOUNT, 0)) as SERVICE_TOTAL_AMOUNT,\n" );
		sql.append("       sum(nvl(CB.OTHER_AMOUNT, 0)) AS OTHER_AMOUNT,\n" );
		sql.append("       sum(nvl(cb.RETURN_AMOUNT, 0)) as RETURN_AMOUNT,\n" );
		sql.append("       sum(nvl(cb.SPEOUTFEE_AMOUNT, 0)) as SPEOUTFEE_AMOUNT,\n" );
		sql.append("       sum(nvl(cb.FREE_DEDUCT, 0)) as FREE_DEDUCT,\n" );
		sql.append("       sum(nvl(cb.SERVICE_DEDUCT, 0)) as sum_SERVICE_DEDUCT,\n" );
		sql.append("       sum(nvl(cb.OLD_DEDUCT, 0)) as OLD_DEDUCT,\n" );
		sql.append("       sum(nvl(cb.CHECK_DEDUCT, 0)) as CHECK_DEDUCT,\n" );
		sql.append("       sum(nvl(cb.Apply_Amount, 0)) as Apply_Amount,\n" );
		sql.append("sum(nvl(cb.apply_amount - cb.balance_amount+nvl(cb.OLD_DEDUCT,0)+nvl(cb.CHECK_DEDUCT,0)+nvl(cb.SERVICE_DEDUCT,0)+nvl(cb.FREE_DEDUCT,0), 0)) as SUM_KKZJ_SH,\n");
		sql.append("sum(nvl(cb.apply_amount - cb.balance_amount,0)) as SUM_KKZJ,");
		sql.append("       sum(nvl(CB.BALANCE_AMOUNT, 0)) BALANCE_AMOUNT,\n" );
		sql.append("       sum(cb.Service_Total_Amount_Bak) as SERVICE_TOTAL_AMOUNT\n" );
		sql.append("  from (SELECT a.balance_id,\n" );
		sql.append("               nvl(sum(decode(a.series_name,\n" );
		sql.append("                              '奔奔MINI',\n" );
		sql.append("                              a.BEFORE_CLAIM_COUNT)),\n" );
		sql.append("                   0) bbmi_sqspds,\n" );
		sql.append("               nvl(sum(decode(a.series_name,\n" );
		sql.append("                              '奔奔MINI',\n" );
		sql.append("                              a.BEFORE_PART_AMOUNT)),\n" );
		sql.append("                   0) bbmi_sqclf,\n" );
		sql.append("               nvl(sum(decode(a.series_name,\n" );
		sql.append("                              '奔奔MINI',\n" );
		sql.append("                              a.BEFORE_LABOUR_AMOUNT)),\n" );
		sql.append("                   0) bbmi_sqgsf,\n" );
		sql.append("               nvl(sum(decode(a.series_name, '奔奔MINI', a.AFTER_CLAIM_COUNT)),\n" );
		sql.append("                   0) bbmi_shspds,\n" );
		sql.append("               nvl(sum(decode(a.series_name, '奔奔MINI', a.AFTER_PART_AMOUNT)),\n" );
		sql.append("                   0) bbmi_shclf,\n" );
		sql.append("               nvl(sum(decode(a.series_name,\n" );
		sql.append("                              '奔奔MINI',\n" );
		sql.append("                              a.AFTER_LABOUR_AMOUNT)),\n" );
		sql.append("                   0) bbmi_SHGSF,\n" );
		sql.append("               nvl(sum(decode(a.series_name, '奔奔MINI', a.FREE_CLAIM_COUNT)),\n" );
		sql.append("                   0) bbmi_mfbyds,\n" );
		sql.append("               nvl(sum(decode(a.series_name, '奔奔MINI', a.FREE_CLAIM_AMOUNT)),\n" );
		sql.append("                   0) bbmi_mfbyzfy,\n" );
		sql.append("               nvl(sum(decode(a.series_name,\n" );
		sql.append("                              '奔奔MINI',\n" );
		sql.append("                              a.SERVICE_CLAIM_COUNT)),\n" );
		sql.append("                   0) bbmi_fwhdspds,\n" );
		sql.append("               nvl(sum(decode(a.series_name, '悦翔', a.BEFORE_CLAIM_COUNT)),\n" );
		sql.append("                   0) yx_sqspds,\n" );
		sql.append("               nvl(sum(decode(a.series_name, '悦翔', a.BEFORE_PART_AMOUNT)),\n" );
		sql.append("                   0) yx_sqclf,\n" );
		sql.append("               nvl(sum(decode(a.series_name, '悦翔', a.BEFORE_LABOUR_AMOUNT)),\n" );
		sql.append("                   0) yx_sqgsf,\n" );
		sql.append("               nvl(sum(decode(a.series_name, '悦翔', a.AFTER_CLAIM_COUNT)),\n" );
		sql.append("                   0) yx_shspds,\n" );
		sql.append("               nvl(sum(decode(a.series_name, '悦翔', a.AFTER_PART_AMOUNT)),\n" );
		sql.append("                   0) yx_shclf,\n" );
		sql.append("               nvl(sum(decode(a.series_name, '悦翔', a.AFTER_LABOUR_AMOUNT)),\n" );
		sql.append("                   0) yx_SHGSF,\n" );
		sql.append("               nvl(sum(decode(a.series_name, '悦翔', a.FREE_CLAIM_COUNT)), 0) YX_mfbyds,\n" );
		sql.append("               nvl(sum(decode(a.series_name, '悦翔', a.FREE_CLAIM_AMOUNT)),\n" );
		sql.append("                   0) yx_mfbyzfy,\n" );
		sql.append("               nvl(sum(decode(a.series_name, '悦翔', a.SERVICE_CLAIM_COUNT)),\n" );
		sql.append("                   0) yx_fwhdspds,\n" );
		sql.append("               ----\n" );
		sql.append("               nvl(sum(decode(a.series_name, '杰勋', a.BEFORE_CLAIM_COUNT)),\n" );
		sql.append("                   0) jx_sqspds,\n" );
		sql.append("               nvl(sum(decode(a.series_name, '杰勋', a.BEFORE_PART_AMOUNT)),\n" );
		sql.append("                   0) jx_sqclf,\n" );
		sql.append("               nvl(sum(decode(a.series_name, '杰勋', a.BEFORE_LABOUR_AMOUNT)),\n" );
		sql.append("                   0) jx_sqgsf,\n" );
		sql.append("               nvl(sum(decode(a.series_name, '杰勋', a.AFTER_CLAIM_COUNT)),\n" );
		sql.append("                   0) jx_shspds,\n" );
		sql.append("               nvl(sum(decode(a.series_name, '杰勋', a.AFTER_PART_AMOUNT)),\n" );
		sql.append("                   0) jx_shclf,\n" );
		sql.append("               nvl(sum(decode(a.series_name, '杰勋', a.AFTER_LABOUR_AMOUNT)),\n" );
		sql.append("                   0) jx_SHGSF,\n" );
		sql.append("               nvl(sum(decode(a.series_name, '杰勋', a.FREE_CLAIM_COUNT)), 0) jX_mfbyds,\n" );
		sql.append("               nvl(sum(decode(a.series_name, '杰勋', a.FREE_CLAIM_AMOUNT)),\n" );
		sql.append("                   0) jx_mfbyzfy,\n" );
		sql.append("               nvl(sum(decode(a.series_name, '杰勋', a.SERVICE_CLAIM_COUNT)),\n" );
		sql.append("                   0) jx_fwhdspds,\n" );
		sql.append("               ---奔奔\n" );
		sql.append("               nvl(sum(decode(a.series_name, '奔奔', a.BEFORE_CLAIM_COUNT)),\n" );
		sql.append("                   0) bb_sqspds,\n" );
		sql.append("               nvl(sum(decode(a.series_name, '奔奔', a.BEFORE_PART_AMOUNT)),\n" );
		sql.append("                   0) bb_sqclf,\n" );
		sql.append("               nvl(sum(decode(a.series_name, '奔奔', a.BEFORE_LABOUR_AMOUNT)),\n" );
		sql.append("                   0) bb_sqgsf,\n" );
		sql.append("               nvl(sum(decode(a.series_name, '奔奔', a.AFTER_CLAIM_COUNT)),\n" );
		sql.append("                   0) bb_shspds,\n" );
		sql.append("               nvl(sum(decode(a.series_name, '奔奔', a.AFTER_PART_AMOUNT)),\n" );
		sql.append("                   0) bb_shclf,\n" );
		sql.append("               nvl(sum(decode(a.series_name, '奔奔', a.AFTER_LABOUR_AMOUNT)),\n" );
		sql.append("                   0) bb_SHGSF,\n" );
		sql.append("               nvl(sum(decode(a.series_name, '奔奔', a.FREE_CLAIM_COUNT)), 0) bb_mfbyds,\n" );
		sql.append("               nvl(sum(decode(a.series_name, '奔奔', a.FREE_CLAIM_AMOUNT)),\n" );
		sql.append("                   0) bb_mfbyzfy,\n" );
		sql.append("               nvl(sum(decode(a.series_name, '奔奔', a.SERVICE_CLAIM_COUNT)),\n" );
		sql.append("                   0) bb_fwhdspds,\n" );
		sql.append("               ---志翔\n" );
		sql.append("               nvl(sum(decode(a.series_name, '志翔', a.BEFORE_CLAIM_COUNT)),\n" );
		sql.append("                   0) zx_sqspds,\n" );
		sql.append("               nvl(sum(decode(a.series_name, '志翔', a.BEFORE_PART_AMOUNT)),\n" );
		sql.append("                   0) zx_sqclf,\n" );
		sql.append("               nvl(sum(decode(a.series_name, '志翔', a.BEFORE_LABOUR_AMOUNT)),\n" );
		sql.append("                   0) zx_sqgsf,\n" );
		sql.append("               nvl(sum(decode(a.series_name, '志翔', a.AFTER_CLAIM_COUNT)),\n" );
		sql.append("                   0) zx_shspds,\n" );
		sql.append("               nvl(sum(decode(a.series_name, '志翔', a.AFTER_PART_AMOUNT)),\n" );
		sql.append("                   0) zx_shclf,\n" );
		sql.append("               nvl(sum(decode(a.series_name, '志翔', a.AFTER_LABOUR_AMOUNT)),\n" );
		sql.append("                   0) zx_SHGSF,\n" );
		sql.append("               nvl(sum(decode(a.series_name, '志翔', a.FREE_CLAIM_COUNT)), 0) zx_mfbyds,\n" );
		sql.append("               nvl(sum(decode(a.series_name, '志翔', a.FREE_CLAIM_AMOUNT)),\n" );
		sql.append("                   0) zx_mfbyzfy,\n" );
		sql.append("               nvl(sum(decode(a.series_name, '志翔', a.SERVICE_CLAIM_COUNT)),\n" );
		sql.append("                   0) zx_fwhdspds,\n" );
		sql.append("               ----CX30\n" );
		sql.append("               nvl(sum(decode(a.series_code, 'CX30', a.BEFORE_CLAIM_COUNT)),\n" );
		sql.append("                   0) cx30_sqspds,\n" );
		sql.append("               nvl(sum(decode(a.series_code, 'CX30', a.BEFORE_PART_AMOUNT)),\n" );
		sql.append("                   0) cx30_sqclf,\n" );
		sql.append("               nvl(sum(decode(a.series_code, 'CX30', a.BEFORE_LABOUR_AMOUNT)),\n" );
		sql.append("                   0) cx30_sqgsf,\n" );
		sql.append("               nvl(sum(decode(a.series_code, 'CX30', a.AFTER_CLAIM_COUNT)),\n" );
		sql.append("                   0) cx30_shspds,\n" );
		sql.append("               nvl(sum(decode(a.series_code, 'CX30', a.AFTER_PART_AMOUNT)),\n" );
		sql.append("                   0) cx30_shclf,\n" );
		sql.append("               nvl(sum(decode(a.series_code, 'CX30', a.AFTER_LABOUR_AMOUNT)),\n" );
		sql.append("                   0) cx30_SHGSF,\n" );
		sql.append("               nvl(sum(decode(a.series_code, 'CX30', a.FREE_CLAIM_COUNT)), 0) cx30_mfbyds,\n" );
		sql.append("               nvl(sum(decode(a.series_code, 'CX30', a.FREE_CLAIM_AMOUNT)),\n" );
		sql.append("                   0) cx30_mfbyzfy,\n" );
		sql.append("               nvl(sum(decode(a.series_code, 'CX30', a.SERVICE_CLAIM_COUNT)),\n" );
		sql.append("                   0) cx30_fwhdspds,\n" );
		sql.append("               ---CX20\n" );
		sql.append("               nvl(sum(decode(a.series_name, 'CX20', a.BEFORE_CLAIM_COUNT)),\n" );
		sql.append("                   0) cx20_sqspds,\n" );
		sql.append("               nvl(sum(decode(a.series_name, 'CX20', a.BEFORE_PART_AMOUNT)),\n" );
		sql.append("                   0) cx20_sqclf,\n" );
		sql.append("               nvl(sum(decode(a.series_name, 'CX20', a.BEFORE_LABOUR_AMOUNT)),\n" );
		sql.append("                   0) cx20_sqgsf,\n" );
		sql.append("               nvl(sum(decode(a.series_name, 'CX20', a.AFTER_CLAIM_COUNT)),\n" );
		sql.append("                   0) cx20_shspds,\n" );
		sql.append("               nvl(sum(decode(a.series_name, 'CX20', a.AFTER_PART_AMOUNT)),\n" );
		sql.append("                   0) cx20_shclf,\n" );
		sql.append("               nvl(sum(decode(a.series_name, 'CX20', a.AFTER_LABOUR_AMOUNT)),\n" );
		sql.append("                   0) cx20_SHGSF,\n" );
		sql.append("               nvl(sum(decode(a.series_name, 'CX20', a.FREE_CLAIM_COUNT)), 0) cx20_mfbyds,\n" );
		sql.append("               nvl(sum(decode(a.series_name, 'CX20', a.FREE_CLAIM_AMOUNT)),\n" );
		sql.append("                   0) cx20_mfbyzfy,\n" );
		sql.append("               nvl(sum(decode(a.series_name, 'CX20', a.SERVICE_CLAIM_COUNT)),\n" );
		sql.append("                   0) cx20_fwhdspds,\n" );
		sql.append("               ------\n" );
		sql.append("               nvl(sum(decode(a.series_name,\n" );
		sql.append("                              'CX30三厢',\n" );
		sql.append("                              a.BEFORE_CLAIM_COUNT)),\n" );
		sql.append("                   0) cx30sx_sqspds,\n" );
		sql.append("               nvl(sum(decode(a.series_name,\n" );
		sql.append("                              'CX30三厢',\n" );
		sql.append("                              a.BEFORE_PART_AMOUNT)),\n" );
		sql.append("                   0) cx30sx_sqclf,\n" );
		sql.append("               nvl(sum(decode(a.series_name,\n" );
		sql.append("                              'CX30三厢',\n" );
		sql.append("                              a.BEFORE_LABOUR_AMOUNT)),\n" );
		sql.append("                   0) cx30sx_sqgsf,\n" );
		sql.append("               nvl(sum(decode(a.series_name, 'CX30三厢', a.AFTER_CLAIM_COUNT)),\n" );
		sql.append("                   0) cx30sx_shspds,\n" );
		sql.append("               nvl(sum(decode(a.series_name, 'CX30三厢', a.AFTER_PART_AMOUNT)),\n" );
		sql.append("                   0) cx30sx_shclf,\n" );
		sql.append("               nvl(sum(decode(a.series_name,\n" );
		sql.append("                              'CX30三厢',\n" );
		sql.append("                              a.AFTER_LABOUR_AMOUNT)),\n" );
		sql.append("                   0) cx30sx_SHGSF,\n" );
		sql.append("               nvl(sum(decode(a.series_name, 'CX30三厢', a.FREE_CLAIM_COUNT)),\n" );
		sql.append("                   0) cx30sx_mfbyds,\n" );
		sql.append("               nvl(sum(decode(a.series_name, 'CX30三厢', a.FREE_CLAIM_AMOUNT)),\n" );
		sql.append("                   0) cx30sx_mfbyzfy,\n" );
		sql.append("               nvl(sum(decode(a.series_name,\n" );
		sql.append("                              'CX30三厢',\n" );
		sql.append("                              a.SERVICE_CLAIM_COUNT)),\n" );
		sql.append("                   0) cx30sx_fwhdspds,\n" );
		sql.append("               ---SC7133BR.CFA\n" );
		sql.append("               nvl(sum(decode(a.series_name,\n" );
		sql.append("                              'SC7133BR.CFA',\n" );
		sql.append("                              a.BEFORE_CLAIM_COUNT)),\n" );
		sql.append("                   0) SC7133BR_sqspds,\n" );
		sql.append("               nvl(sum(decode(a.series_name,\n" );
		sql.append("                              'SC7133BR.CFA',\n" );
		sql.append("                              a.BEFORE_PART_AMOUNT)),\n" );
		sql.append("                   0) SC7133BR_sqclf,\n" );
		sql.append("               nvl(sum(decode(a.series_name,\n" );
		sql.append("                              'SC7133BR.CFA',\n" );
		sql.append("                              a.BEFORE_LABOUR_AMOUNT)),\n" );
		sql.append("                   0) SC7133BR_sqgsf,\n" );
		sql.append("               nvl(sum(decode(a.series_name,\n" );
		sql.append("                              'SC7133BR.CFA',\n" );
		sql.append("                              a.AFTER_CLAIM_COUNT)),\n" );
		sql.append("                   0) SC7133BR_shspds,\n" );
		sql.append("               nvl(sum(decode(a.series_name,\n" );
		sql.append("                              'SC7133BR.CFA',\n" );
		sql.append("                              a.AFTER_PART_AMOUNT)),\n" );
		sql.append("                   0) SC7133BR_shclf,\n" );
		sql.append("               nvl(sum(decode(a.series_name,\n" );
		sql.append("                              'SC7133BR.CFA',\n" );
		sql.append("                              a.AFTER_LABOUR_AMOUNT)),\n" );
		sql.append("                   0) SC7133BR_SHGSF,\n" );
		sql.append("               nvl(sum(decode(a.series_name,\n" );
		sql.append("                              'SC7133BR.CFA',\n" );
		sql.append("                              a.FREE_CLAIM_COUNT)),\n" );
		sql.append("                   0) SC7133BR_mfbyds,\n" );
		sql.append("               nvl(sum(decode(a.series_name,\n" );
		sql.append("                              'SC7133BR.CFA',\n" );
		sql.append("                              a.FREE_CLAIM_AMOUNT)),\n" );
		sql.append("                   0) SC7133BR_mfbyzfy,\n" );
		sql.append("               nvl(sum(decode(a.series_name,\n" );
		sql.append("                              'SC7133BR.CFA',\n" );
		sql.append("                              a.SERVICE_CLAIM_COUNT)),\n" );
		sql.append("                   0) SC7133BR_fwhdspds\n" );
		sql.append("          FROM TT_AS_WR_CLAIM_BALANCE_DETAIL A\n" );
		sql.append("         group by a.balance_id) a,\n" );
		sql.append("       (select ba.balance_id,\n" );
		sql.append("               ba.auth_person_name,\n" );
		sql.append("               max(ba.create_date) as CREATE_DATE\n" );
		sql.append("          from Tt_As_Wr_Balance_Authitem ba\n" );
		sql.append("         where ba.auth_status = 11861004\n" );
		sql.append("         group by ba.balance_id, ba.auth_person_name) c,\n" );
		sql.append("       Tt_As_Wr_Claim_Balance cb,\n" );
		sql.append("       VW_ORG_DEALER_SERVICE VW\n" );
		sql.append(" where a.balance_id = cb.id\n" );
		sql.append("   and cb.id = c.balance_id\n" );
		sql.append("   AND cb.dealer_id = vw.dealer_id \n");
		if(Utility.testString(beginTime)){
			sql.append("and cb.start_date>=to_date('"+beginTime+"','yyyy-MM-dd')\n");
		}
		if(Utility.testString(endTime)){
		sql.append(" and cb.end_date<=to_date('"+endTime+"','yyyy-MM-dd')\n" );
		}
		if(Utility.testString(audit_beginTime)){
		sql.append(" and c.create_date>=to_date('"+audit_beginTime+"','yyyy-MM-dd')\n" );
		}
		if(Utility.testString(audit_endTime)){
			sql.append(" and c.create_date<=to_date('"+audit_endTime+"','yyyy-MM-dd')\n" );	
		}
		if(Utility.testString(dealerCode)){
		sql.append(" and cb.dealer_code='"+dealerCode+"'\n" );
		}
		if(Utility.testString(areaName)){
		sql.append(" and vw.root_org_id ='"+areaName+"' ");
		}


		return this.pageQuery(sql.toString(), null, this.getFunName());
	}
	
}
