package com.infodms.dms.dao.sales.marketmanage.planmanage;

import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.common.BaseDao;
import com.infoservice.po3.POFactory;
import com.infoservice.po3.POFactoryBuilder;
import com.infoservice.po3.bean.PO;

public class ActivitiesOrgCostImportDao extends BaseDao {
	public static Logger logger = Logger.getLogger(ActivitiesCostImportDao.class);
	private static final ActivitiesOrgCostImportDao dao = new ActivitiesOrgCostImportDao ();
	private static POFactory factory = POFactoryBuilder.getInstance();
	public static final ActivitiesOrgCostImportDao getInstance() {
		return dao;
	}
	/**
	 * 市场活动费用导入模板下载SQL
	 * @param orgId
	 * @param companyId
	 * @return
	 * @author zouchao
	 * @since  2010-08-20
	 */
	public List<Map<String, Object>> getTempDownOrgCompanyload(String areaId){
		StringBuffer sql= new StringBuffer();
		sql.append(" SELECT ORG_CODE, ORG_NAME, '11291001'cost_type, '市场推广费用'cost_desc, 0 COST_AMOUNT\n");
		sql.append(" FROM TM_ORG,TM_ORG_BUSINESS_AREA\n");
		sql.append(" WHERE ORG_TYPE = 10191001\n");
		sql.append(" AND COMPANY_ID = 2010010100070674\n");
		sql.append(" AND STATUS = 10011001\n");
		sql.append(" AND DUTY_TYPE = 10431003\n");
		sql.append(" AND TM_ORG_BUSINESS_AREA.AREA_ID="+areaId+"\n");
		sql.append(" AND TM_ORG.ORG_ID=TM_ORG_BUSINESS_AREA.ORG_ID\n");
		sql.append(" UNION ALL\n");
		sql.append(" SELECT ORG_CODE, ORG_NAME,tc.code_id,tc.code_desc, 0\n");
		sql.append(" FROM TM_ORG o,tc_code tc,TM_ORG_BUSINESS_AREA TOBA\n");
		sql.append(" WHERE o.ORG_TYPE = 10191001\n");
		sql.append("  AND o.COMPANY_ID = 2010010100070674\n");
		sql.append(" AND o.STATUS = 10011001\n");
		sql.append(" AND o.DUTY_TYPE = 10431001\n");
		sql.append(" AND tc.type = 1129\n");
		sql.append(" AND O.ORG_ID=TOBA.ORG_ID\n");
		sql.append(" AND TOBA.AREA_ID="+areaId+"\n");
//		sql.append("select tmo.org_code,tmo.org_name,'11291001'cost_type,'市场推广费用'cost_desc,0 COST_AMOUNT\n");
//		sql.append("from tm_org tmo\n");
//		sql.append("where tmo.org_type = 10191001\n");
//		sql.append("and tmo.status = 10011001\n");
//		sql.append("order by tmo.duty_type\n");
		
//				sql.append("select tmo.org_name,tmo.org_code,0 COST_AMOUNT from tm_org tmo\n");
//				sql.append("where tmo.org_type = 10191001\n");
//				sql.append("and tmo.status = 10011001 and tmo.duty_type=10431001\n");
		//sql.append("select td.DEALER_CODE,\n");
		//sql.append("       td.DEALER_SHORTNAME,\n");  
		//sql.append("       '' ORG_CODE,\n");  
		//sql.append("       '' ORG_NAME,\n");  
		//sql.append("       '' COST_TYPE,\n");  
		//sql.append("       '' COST_SOURCE,\n");  
		//sql.append("       0 COST_AMOUNT\n");  
        //sql.append("  from TM_DEALER td\n");  
        //sql.append(" where td.OEM_COMPANY_ID = "+companyId+"\n");  //区分该区域下面的
        //sql.append("   and td.STATUS = "+Constant.STATUS_ENABLE+"\n");  
		//sql.append("union\n");  
		//sql.append("select '' DEALER_CODE,\n");  
		//sql.append("       '' DEALER_SHORTNAME,\n");  
		//sql.append("       org.ORG_CODE,\n");  
		//sql.append("       org.ORG_NAME,\n");  
		//sql.append("       '' COST_TYPE,\n");  
		//sql.append("       '' COST_SOURCE,\n");  
		//sql.append("       0 COST_AMOUNT\n");  
		//sql.append("  from tm_org org\n");  
		//sql.append(" where org.COMPANY_ID = "+companyId+"\n");  
		//sql.append("   and org.STATUS = "+Constant.STATUS_ENABLE+"\n");  
		//sql.append("   and org.ORG_TYPE = "+Constant.ORG_TYPE_OEM+"");
		return dao.pageQuery(sql.toString(), null, getFunName());

	}
	/**
	 * 市场活动费用导入模板下载SQL
	 * @param orgId
	 * @param companyId
	 * @return
	 * @author zouchao
	 * @since  2010-08-20
	 */
	public List<Map<String, Object>> getTempDownOrgload(){
		StringBuffer sql= new StringBuffer();
		sql.append("select tmo.org_name,tmo.org_code,0 COST_AMOUNT from tm_org tmo\n");
		sql.append("where tmo.org_type = 10191001\n");
		sql.append("and tmo.status = 10011001 and tmo.duty_type=10431003\n");
		//sql.append("select td.DEALER_CODE,\n");
		//sql.append("       td.DEALER_SHORTNAME,\n");  
		//sql.append("       '' ORG_CODE,\n");  
		//sql.append("       '' ORG_NAME,\n");  
		//sql.append("       '' COST_TYPE,\n");  
		//sql.append("       '' COST_SOURCE,\n");  
		//sql.append("       0 COST_AMOUNT\n");  
        //sql.append("  from TM_DEALER td\n");  
        //sql.append(" where td.OEM_COMPANY_ID = "+companyId+"\n");  //区分该区域下面的
        //sql.append("   and td.STATUS = "+Constant.STATUS_ENABLE+"\n");  
		//sql.append("union\n");  
		//sql.append("select '' DEALER_CODE,\n");  
		//sql.append("       '' DEALER_SHORTNAME,\n");  
		//sql.append("       org.ORG_CODE,\n");  
		//sql.append("       org.ORG_NAME,\n");  
		//sql.append("       '' COST_TYPE,\n");  
		//sql.append("       '' COST_SOURCE,\n");  
		//sql.append("       0 COST_AMOUNT\n");  
		//sql.append("  from tm_org org\n");  
		//sql.append(" where org.COMPANY_ID = "+companyId+"\n");  
		//sql.append("   and org.STATUS = "+Constant.STATUS_ENABLE+"\n");  
		//sql.append("   and org.ORG_TYPE = "+Constant.ORG_TYPE_OEM+"");
		return dao.pageQuery(sql.toString(), null, getFunName());

	}
	public List<Map<String, Object>> talbeCheckMyNuber(String userId){
		List<Object> params = new LinkedList<Object>();
		StringBuffer sql=new StringBuffer("");
		sql.append("select t1.row_number row_number1, t2.row_number row_number2\n");
		sql.append("from tmp_vs_hd_cost t1, tmp_vs_hd_cost t2\n");
		sql.append("where  t1.row_number<>t2.row_number\n");
		sql.append("and t1.user_id="+userId+"\n");
		sql.append("and t1.org_code=t2.org_code\n");
		sql.append("and t1.cost_type=t2.cost_type\n");
		List<Map<String,Object>>list=dao.pageQuery(sql.toString(), params, getFunName());
		return list;
	}
	/*
	 * OEM导入时，查询临时表数据
	 */
	public List<Map<String, Object>> selectDealerCostTmpTable(Long userId){
		List<Object> params = new LinkedList<Object>();
		StringBuffer sql=new StringBuffer("");
		sql.append(" select tvhc.hd_code,\n");
		sql.append(" tvhc.COST_TYPE,\n");
		sql.append(" tvhc.COST_DESC,\n");
		sql.append(" tvhc.org_code,\n");
		sql.append(" tvhc.org_name,\n");
		sql.append(" tvhc.org_amount\n");
		sql.append(" from tmp_vs_hd_cost tvhc\n");
		sql.append(" where tvhc.user_id="+userId+"\n");
		return dao.pageQuery(sql.toString(), params, getFunName());
		
	}
	/*
	 * 插入主表数据
	 */
	public int insertDealerCost(Map<String, Object> map,String areaId) {
		String userId=map.get("userId").toString();
		StringBuffer sql=new StringBuffer("");
		sql.append("merge into TT_VS_COST a\n");
		sql.append("USING (SELECT TVHC.ORG_AMOUNT,TMO.ORG_ID,tvhc.cost_type,TMO.DUTY_TYPE,tvhc.area_id\n");
		sql.append("FROM TM_ORG TMO,TMP_VS_HD_COST TVHC\n");
		sql.append(" WHERE TMO.ORG_CODE = TVHC.ORG_CODE\n");
		sql.append("  OR TMO.ORG_CODE = TVHC.HD_CODE AND TVHC.AREA_ID = "+areaId+") B\n");
		sql.append("ON (A.ORG_ID = B.ORG_ID AND A.DEALER_ID IS NULL AND A.COST_TYPE=B.COST_TYPE  AND A.AREA_ID=B.AREA_ID )\n");
		sql.append("WHEN MATCHED THEN\n");
		sql.append("update\n");
		sql.append("set a.cost_amount      = nvl(B.ORG_AMOUNT, 0) + a.cost_amount,\n");
		sql.append("  a.available_amount = a.available_amount + nvl(B.ORG_AMOUNT, 0) \n");
		sql.append("when not matched then\n");
		sql.append("insert\n");
		sql.append("(a.cost_id,\n");
		sql.append("a.cost_amount,\n");
		sql.append(" a.org_id,\n");
		sql.append("a.freeze_amount,\n");
		sql.append("a.available_amount,\n");
		sql.append("a.cost_type,\n");
		sql.append("a.create_by,\n");
		sql.append(" a.cost_source,\n");
		sql.append(" a.AREA_ID,\n");
		sql.append(" a.create_date)\n");
		sql.append("VALUES\n");
		sql.append("(seq_tt_vs_cost.nextval,\n");
		sql.append("nvl(B.ORG_AMOUNT, 0),\n");
		sql.append("b.org_id,\n");
		sql.append("0,\n");
		sql.append("nvl(B.ORG_AMOUNT, 0),\n");
		sql.append("B.COST_TYPE,"+userId+",DECODE(B.DUTY_TYPE, 10431001,11301003,11301002),"+areaId+",sysdate)\n");
//	sql.append(" merge into TT_VS_COST a\n");
//	sql.append(" USING (SELECT TVHC.ORG_AMOUNT, TVHC.HD_AMOUNT, TMO.ORG_ID\n");
//	sql.append("   FROM TM_ORG TMO, TMP_VS_HD_COST TVHC\n");
//	sql.append("   WHERE TMO.ORG_CODE = TVHC.ORG_CODE\n");
//	sql.append("     OR TMO.ORG_CODE = TVHC.HD_CODE) B\n");
//	sql.append(" ON (A.ORG_ID = B.ORG_ID AND A.DEALER_ID IS NULL)\n");
//	sql.append(" WHEN MATCHED THEN\n");
//	sql.append(" update\n");
//	sql.append("  set a.cost_amount      = nvl(b.hd_amount, 0) + nvl(B.ORG_AMOUNT, 0) +\n");
//	sql.append("                           a.cost_amount,\n");
//	sql.append("     a.available_amount = a.available_amount + nvl(b.hd_amount, 0) +\n");
//	sql.append("                           nvl(B.ORG_AMOUNT, 0) ,A.UPDATE_BY = -2\n");
//	sql.append(" when not matched then\n");
//	sql.append(" insert\n");
//	sql.append("  (a.cost_id,\n");
//	sql.append("   a.cost_amount,\n");
//	sql.append("    a.org_id,\n");
//	sql.append("  a.freeze_amount,\n");
//	sql.append(" a.available_amount,\n");
//	sql.append(" a.cost_type,\n");
//	sql.append("  a.create_by,\n");
//	sql.append("  a.create_date)\n");
//	sql.append("  VALUES\n");
//	sql.append(" (seq_tt_vs_cost.nextval,\n");
//	sql.append("  DECODE(b.Hd_Amount, NULL, B.ORG_AMOUNT, b.Hd_Amount),\n");
//	sql.append("  b.org_id,\n");
//	sql.append("  0,\n");
//	sql.append(" DECODE(b.Hd_Amount, NULL, B.ORG_AMOUNT, b.Hd_Amount),\n");
//	sql.append("  DECODE(b.Hd_Amount, NULL, 11291001, 11291002),"+userId+",sysdate)\n");
		return update(sql.toString(),null);
	}
	
	
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}

}
