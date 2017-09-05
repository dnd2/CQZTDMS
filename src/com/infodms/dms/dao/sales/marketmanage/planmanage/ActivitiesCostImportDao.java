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

/**
 * @Title: 活动方案管理DAO
 * @Description:InfoFrame3.0.V01
 * @Copyright: Copyright (c) 2010
 * @Company: www.infoservice.com.cn
 * @Date: 2010-6-30
 * @author 
 * @mail 
 * @version 1.0
 * @remark 
 */
public class ActivitiesCostImportDao extends BaseDao{
	public static Logger logger = Logger.getLogger(ActivitiesCostImportDao.class);
	private static final ActivitiesCostImportDao dao = new ActivitiesCostImportDao ();
	private static POFactory factory = POFactoryBuilder.getInstance();
	public static final ActivitiesCostImportDao getInstance() {
		return dao;
	}
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}
	/*
	 * OEM导入时，查询临时表数据
	 */
	public List<Map<String, Object>> selectTmpTable(Long userId){
		List<Object> params = new LinkedList<Object>();
		StringBuffer sql=new StringBuffer("");


//		sql.append("select '' DEALER_CODE,\n");
//		sql.append("       '' DEALER_SHORTNAME,\n");  
//		sql.append("       org.ORG_CODE,\n");  
//		sql.append("       org.ORG_NAME,\n");  
//		sql.append("       c.ROW_NUMBER,\n");  
//		sql.append("       c.COST_TYPE,\n");  
//		sql.append("       c.COST_SOURCE,\n");  
//		sql.append("       c.COST_AMOUNT\n");  
//		sql.append("  from TMP_VS_COST c, TM_ORG org\n");  
//		sql.append(" where c.org_code = org.ORG_CODE\n");  
//		sql.append("   and c.USER_ID = "+userId+"\n");  
//		sql.append("UNION\n");  
//		sql.append("select td.DEALER_CODE,\n");  
//		sql.append("       td.DEALER_SHORTNAME,\n");  
//		sql.append("       '' ORG_CODE,\n");  
//		sql.append("       '' ORG_NAME,\n");  
//		sql.append("       c1.ROW_NUMBER,\n");  
//		sql.append("       c1.COST_TYPE,\n");  
//		sql.append("       c1.COST_SOURCE,\n");  
//		sql.append("       c1.COST_AMOUNT\n");  
//		sql.append("  from TMP_VS_COST c1, TM_DEALER td\n");  
//		sql.append(" where c1.DEALER_CODE = td.DEALER_CODE\n");  
//		sql.append("   and c1.USER_ID = "+userId+"\n");
		
		return dao.pageQuery(sql.toString(), params, getFunName());
	}
	/*
	 * OEM导入时，查询临时表数据
	 */
	public List<Map<String, Object>> selectDealerCostTmpTable(Long userId){
		List<Object> params = new LinkedList<Object>();
		StringBuffer sql=new StringBuffer("");
		sql.append("select tvdc.dealer_code, tvdc.dealer_name, tvdc.dealer_amount\n");
		sql.append("from tmp_vs_dealer_cost tvdc\n");
		sql.append("where tvdc.user_id="+userId+"\n");
		return dao.pageQuery(sql.toString(), params, getFunName());
		
	}
	/*
	 * 校验经销商是否存在
	 */
	public  List<Map<String, Object>> checkDlr(Map<String, Object> map){
		String userId=map.get("userId").toString();
		String companyId=map.get("companyId").toString();
		List<Object> params = new LinkedList<Object>();
		StringBuffer sql=new StringBuffer("");

		sql.append("select p.ROW_NUMBER\n");
		sql.append("  from TMP_VS_COST P\n");  
		sql.append(" where 1 = 1\n");  
		sql.append("   and p.USER_ID ="+userId+"\n");  
		sql.append("   and p.DEALER_CODE is not null\n");
		sql.append("   and p.ORG_CODE is null\n");
		sql.append("   and not exists (select 1\n");  
		sql.append("          from tm_dealer td\n");  
		sql.append("         where td.DEALER_CODE = p.DEALER_CODE\n");  
		sql.append("           and td.OEM_COMPANY_ID = "+companyId+"\n");//多公司过滤
		sql.append("           and td.STATUS = "+Constant.STATUS_ENABLE+"\n");//有效
		//sql.append("           and td.ORG_TYPE = "+Constant.ORG_TYPE_OEM+"\n");//车厂组织
		sql.append("          )\n");//大区
		sql.append("   order by TO_NUMBER(p.ROW_NUMBER) asc");

		return dao.pageQuery(sql.toString(), params, getFunName());
	}
	/*
	 * 校验组织是否存在
	 */
	public  List<Map<String, Object>> checkOrg(Map<String, Object> map){
		String userId=map.get("userId").toString();
		String companyId=map.get("companyId").toString();
		List<Object> params = new LinkedList<Object>();
		StringBuffer sql=new StringBuffer("");

		sql.append("select p.ROW_NUMBER\n");
		sql.append("  from TMP_VS_COST P\n");  
		sql.append(" where 1 = 1\n");  
		sql.append("   and p.USER_ID ="+userId+"\n");  
		sql.append("   and p.DEALER_CODE is null\n");
		sql.append("   and p.ORG_CODE is not null\n");
		sql.append("   and not exists (select 1\n");  
		sql.append("          from tm_org org\n");  
		sql.append("         where org.ORG_CODE = p.ORG_CODE\n");  
		sql.append("           and org.COMPANY_ID = "+companyId+"\n");//多公司过滤
		sql.append("           and org.STATUS = "+Constant.STATUS_ENABLE+"\n");//有效
		sql.append("           and org.ORG_TYPE = "+Constant.ORG_TYPE_OEM+"\n");//车厂组织
		sql.append("          )\n");//大区
		sql.append("   order by TO_NUMBER(p.ROW_NUMBER) asc");

		return dao.pageQuery(sql.toString(), params, getFunName());
	}
	
	/*
	 * 查询临时表中是否有相同的ORG/DEALER导入相同来源和类型的费用
	 * 返回所有重复数据集合
	 */
	public List<Map<String, Object>> talbeCheckDump(String userId){
		List<Object> params = new LinkedList<Object>();
		StringBuffer sql=new StringBuffer("");

		sql.append("SELECT C1.ROW_NUMBER ROW_NUMBER1, C2.ROW_NUMBER ROW_NUMBER2\n");
		sql.append("  FROM TMP_VS_COST C1, TMP_VS_COST C2\n");  
		sql.append(" WHERE C1.ROW_NUMBER <> C2.ROW_NUMBER\n");  
		sql.append("   AND C1.USER_ID = "+userId+"\n");  
		sql.append("   AND ((C1.DEALER_CODE = C2.DEALER_CODE AND C1.COST_TYPE = C2.COST_TYPE AND\n");  
		sql.append("       C1.COST_SOURCE = C2.COST_SOURCE) OR\n");  
		sql.append("       (C1.ORG_CODE = C2.ORG_CODE AND C1.COST_TYPE = C2.COST_TYPE AND\n");  
		sql.append("       C1.COST_SOURCE = C2.COST_SOURCE))");

		List<Map<String, Object>> list=dao.pageQuery(sql.toString(), params, getFunName());
		
		return list;
	}
	public List<Map<String, Object>> talbeCheckMyNuber(String userId){
		List<Object> params = new LinkedList<Object>();
		StringBuffer sql=new StringBuffer("");
		sql.append("select t1.row_number, t2.row_number\n");
		sql.append("from tmp_vs_dealer_cost t1, tmp_vs_dealer_cost t2\n");
		sql.append("WHERE t1.ROW_NUMBER <> t2.ROW_NUMBER\n");
		sql.append("and t1.user_id="+userId+"\n");
		sql.append("AND (t1.DEALER_CODE = t2.DEALER_CODE)\n");
		List<Map<String,Object>>list=dao.pageQuery(sql.toString(), params, getFunName());
		return list;
	}
	
	/*
	 * 验证该经销商是否是整车销售
	 * 验证是否是A网用户
	 */
	public List<Map<String, Object>> talbeCheckMyDealer(String userId,String dealerCode,String areaId){
		List<Object> params = new LinkedList<Object>();
		StringBuffer sql=new StringBuffer("");
		sql.append("select *\n");
		sql.append("from tm_dealer tmd, tm_dealer_business_area tdba\n");
		sql.append("where tmd.dealer_type = 10771001\n");
		sql.append("and tdba.area_id = "+areaId+"\n");
		sql.append("and tdba.dealer_id=tmd.dealer_id\n");
		sql.append("and tmd.dealer_code='"+dealerCode+"'\n");
		List<Map<String,Object>>list=dao.pageQuery(sql.toString(), params, getFunName());
		return list;
	}
	
	/*
	 * 导入费用规则：
	 * 1.费用来源 经销商-经销商费用；区域-区域费用；总部-总部费用
	 * 2.费用类型 经销商和区域只能有市场推广费用；总部即可以是市场推广费用也可以是广告费用
	 * 如下校验
	 * 返回所有重复数据集合
	 */
	public List<Map<String, Object>> checkTypeSource(Map<String, Object> map){
		String userId=map.get("userId").toString();
		List<Object> params = new LinkedList<Object>();
		StringBuffer sql=new StringBuffer("");

		sql.append("SELECT C.ROW_NUMBER\n");
		sql.append("  FROM TMP_VS_COST C, TM_ORG ORG\n");  
		sql.append(" WHERE C.ORG_CODE = ORG.ORG_CODE\n");  
		sql.append("   AND C.ORG_CODE IS NOT NULL\n");  
		sql.append("   AND C.DEALER_CODE IS NULL\n");  
		sql.append("   AND C.USER_ID = "+userId+"\n");  
		sql.append("   AND ((ORG.DUTY_TYPE IN ('"+Constant.DUTY_TYPE_LARGEREGION+"', '"+Constant.DUTY_TYPE_SMALLREGION+"') AND\n");  
		sql.append("       C.COST_TYPE <> '市场推广费用') OR\n");  
		sql.append("       (ORG.DUTY_TYPE IN ('"+Constant.DUTY_TYPE_COMPANY+"', '"+Constant.DUTY_TYPE_DEPT+"') AND\n");  
		sql.append("       C.COST_SOURCE <> '总部费用') OR\n");  
		sql.append("       (ORG.DUTY_TYPE IN ('"+Constant.DUTY_TYPE_LARGEREGION+"', '"+Constant.DUTY_TYPE_SMALLREGION+"') AND\n");  
		sql.append("       C.COST_SOURCE <> '区域费用'))\n");  
		sql.append("UNION\n");  
		sql.append("SELECT C1.ROW_NUMBER\n");  
		sql.append("  FROM TMP_VS_COST C1, TM_ORG ORG, TM_DEALER TD\n");  
		sql.append(" WHERE C1.DEALER_CODE = TD.DEALER_CODE\n");  
		sql.append("   AND C1.ORG_CODE IS NULL\n");  
		sql.append("   AND C1.DEALER_CODE IS NOT NULL\n");  
		sql.append("   AND C1.USER_ID = "+userId+"\n");  
		sql.append("   AND TD.COMPANY_ID = ORG.COMPANY_ID\n");  
		sql.append("   AND ((ORG.DUTY_TYPE IN ('"+Constant.DUTY_TYPE_DEALER+"') AND C1.COST_TYPE <> '市场推广费用') OR\n");  
		sql.append("       (ORG.DUTY_TYPE IN ('"+Constant.DUTY_TYPE_DEALER+"') AND C1.COST_SOURCE <> '经销商费用'))");

		List<Map<String, Object>> list=dao.pageQuery(sql.toString(), params, getFunName());
		
		return list;
	}
	/*
	 * 插入主表数据
	 */
	public int insertCost(Map<String, Object> map) {
		String userId=map.get("userId").toString();
		String companyId=map.get("companyId").toString();
		
		StringBuffer sql = new StringBuffer();

		sql.append("merge into TT_VS_COST c\n");
		sql.append("using (select td.DEALER_ID,\n");  
		sql.append("              1 org_id,\n"); 
		sql.append("              td.OEM_COMPANY_ID,\n"); 
		sql.append("              tc1.CODE_ID cost_type,\n");  
		sql.append("              tc2.CODE_ID cost_source,\n");  
		sql.append("              tmp.COST_AMOUNT\n");  
		sql.append("         FROM TMP_VS_COST tmp, TM_DEALER td, TC_CODE tc1, TC_CODE tc2\n");  
		sql.append("        where tmp.DEALER_CODE = td.DEALER_CODE\n");  
		sql.append("          and tmp.COST_TYPE = tc1.CODE_DESC\n");  
		sql.append("          and tmp.COST_SOURCE = tc2.CODE_DESC\n");  
		sql.append("          and td.OEM_COMPANY_ID = "+companyId+"\n");  
		sql.append("          and tmp.USER_ID = "+userId+"\n");  
		sql.append("       union\n");  
		sql.append("       select 1 DEALER_ID,\n");  
		sql.append("              org.ORG_ID org_id,\n");  
		sql.append("              org.COMPANY_ID,\n"); 
		sql.append("              tc1.CODE_ID cost_type,\n");  
		sql.append("              tc2.CODE_ID cost_source,\n");  
		sql.append("              tmp.COST_AMOUNT\n");  
		sql.append("         FROM TMP_VS_COST tmp, tm_org org, TC_CODE tc1, TC_CODE tc2\n");  
		sql.append("        where tmp.org_code = org.org_code\n");  
		sql.append("          and tmp.COST_TYPE = tc1.CODE_DESC\n");  
		sql.append("          and tmp.COST_SOURCE = tc2.CODE_DESC\n");  
		sql.append("          and org.COMPANY_ID = "+companyId+"\n");  
		sql.append("          and tmp.USER_ID = "+userId+") A\n");  
		sql.append("on ((c.DEALER_ID = a.dealer_id and c.COST_TYPE = a.cost_type and c.COST_SOURCE = a.cost_source) or (c.org_ID = a.org_id and c.COST_TYPE = a.cost_type and c.COST_SOURCE = a.cost_source))\n");  
		sql.append("when MATCHED then\n");  
		sql.append("  update\n");  
		sql.append("     set c.COST_AMOUNT      = to_number(a.COST_AMOUNT)+c.COST_AMOUNT,\n");  
		sql.append("         c.AVAILABLE_AMOUNT = to_number(a.COST_AMOUNT)+c.COST_AMOUNT - NVL(c.FREEZE_AMOUNT,0),\n");  
		sql.append("         c.UPDATE_BY        = "+userId+",\n");  
		sql.append("         c.UPDATE_DATE      = sysdate\n");  
		sql.append("when not matched then\n");  
		sql.append("  insert\n");  
		sql.append("    (c.COST_ID,\n");  
		sql.append("     c.DEALER_ID,\n");  
		sql.append("     c.ORG_ID,\n");  
		sql.append("     c.COST_TYPE,\n");  
		sql.append("     c.COST_SOURCE,\n");  
		sql.append("     c.COST_AMOUNT,\n");  
		sql.append("     c.FREEZE_AMOUNT,\n");  
		sql.append("     c.AVAILABLE_AMOUNT,\n");  
		sql.append("     c.CREATE_BY,\n");  
		sql.append("     c.CREATE_DATE)\n");  
		sql.append("  values\n");  
		sql.append("    (F_GETID(),\n");  
		sql.append("     decode(a.DEALER_ID,1,null,a.DEALER_ID),\n");  
		sql.append("     decode(a.ORG_ID,1,null,a.ORG_ID),\n");  
		sql.append("     a.COST_TYPE,\n");  
		sql.append("     a.COST_SOURCE,\n");  
		sql.append("     a.COST_AMOUNT,\n");  
		sql.append("     0,\n");  
		sql.append("     a.COST_AMOUNT,\n");  
		sql.append("     "+userId+",\n");  
		sql.append("     sysdate)");
		
		return update(sql.toString(), null);
	}
	/*
	 * 插入主表数据
	 */
	public int insertDealerCost(Map<String, Object> map) {
		String userId=map.get("userId").toString();
		String orgId = map.get("orgId").toString() ;
		String areaId = map.get("areaId").toString() ;
		
		StringBuffer sql=new StringBuffer("");
		sql.append("merge into TT_VS_COST a\n");
		sql.append("USING (select tvdc.*,\n");  
		sql.append("              tmd.dealer_id,\n");  
		sql.append("              "+orgId+"ORG_ID\n");  
		sql.append("         from tm_dealer              tmd,\n");  
		sql.append("              tmp_vs_dealer_cost     tvdc\n");  
		sql.append("        where tvdc.dealer_code = tmd.dealer_code\n");  
		sql.append("          AND TMD.STATUS = "+Constant.STATUS_ENABLE+" --有效\n");  
		sql.append("          AND TMD.DEALER_TYPE = "+Constant.DEALER_TYPE_DVS+" --经销商整车\n");  
		sql.append("          AND TMD.DEALER_CLASS = "+Constant.DEALER_CLASS_TYPE_01+" --服务中心\n");  
		sql.append("          and tvdc.area_id = "+areaId+") b\n");  
		sql.append("ON (a.dealer_id = b.dealer_id and a.area_id = b.area_id)\n");  
		sql.append("WHEN MATCHED THEN\n");  
		sql.append("  update\n");  
		sql.append("     set a.cost_amount      = b.dealer_amount + a.cost_amount,\n");  
		sql.append("         a.available_amount = a.available_amount + b.dealer_amount\n");  
		sql.append("when not matched then\n");  
		sql.append("  insert\n");  
		sql.append("    (a.area_id,\n");  
		sql.append("     a.cost_id,\n");  
		sql.append("     a.dealer_id,\n");  
		sql.append("     a.cost_amount,\n");  
		sql.append("     a.cost_source,\n");  
		sql.append("     a.org_id,\n");  
		sql.append("     a.freeze_amount,\n");  
		sql.append("     a.available_amount,\n");  
		sql.append("     a.cost_type,\n");  
		sql.append("     a.create_by,\n");  
		sql.append("     a.create_date)\n");  
		sql.append("  values\n");  
		sql.append("    (b.area_id,\n");  
		sql.append("     seq_tt_vs_cost.nextval,\n");  
		sql.append("     b.dealer_id,\n");  
		sql.append("     b.dealer_amount,\n");  
		sql.append("     "+Constant.COST_SOURCE_01+",\n");  
		sql.append("     b.org_id,\n");  
		sql.append("     0,\n");  
		sql.append("     b.dealer_amount,\n");  
		sql.append("     "+Constant.COST_TYPE_01+",\n");  
		sql.append("     "+userId+",\n");  
		sql.append("     sysdate)\n");
		return update(sql.toString(),null);
	}
	
	/**
	 * 市场活动费用导入模板下载SQL
	 * @param orgId
	 * @param companyId
	 * @return
	 * @author zouchao
	 * @since  2010-08-20
	 */
	public List<Map<String, Object>> getTempDownload(String orgId,String companyId,String areaId){
		StringBuffer sql= new StringBuffer();

		sql.append("SELECT TD.DEALER_CODE, TD.DEALER_SHORTNAME, 0 COST_AMOUNT\n");
				sql.append("FROM TM_DEALER TD, TM_DEALER_BUSINESS_AREA TDBA,TM_ORG TMO,TM_DEALER_ORG_RELATION TDOR\n");
				sql.append("WHERE TDBA.AREA_ID="+areaId+"\n");
				//sql.append("AND TD.DEALER_ID IN ("+dealerId+")\n");
				sql.append(" AND TD.STATUS="+Constant.STATUS_ENABLE+"\n");//判断该经销商是否存在
				sql.append("AND TD.DEALER_TYPE="+Constant.DEALER_TYPE_DVS+"\n");//判断该经销商是否是售后的
				sql.append("AND TD.DEALER_CLASS = " + Constant.DEALER_CLASS_TYPE_01 + "--经销商评级为服务中心\n ");
				sql.append("AND TD.DEALER_ID=TDBA.DEALER_ID\n");
				sql.append("AND TMO.ORG_ID=TDOR.ORG_ID\n");
				sql.append("AND TD.DEALER_ID=TDOR.DEALER_ID\n");
				sql.append("AND TMO.ORG_ID="+orgId+"\n");
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
}
