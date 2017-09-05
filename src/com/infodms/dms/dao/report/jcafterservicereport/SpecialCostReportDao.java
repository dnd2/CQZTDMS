package com.infodms.dms.dao.report.jcafterservicereport;

import java.sql.ResultSet;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.StandardVipApplyManagerBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;


public class SpecialCostReportDao extends BaseDao<PO>{
	
	public static Logger logger = Logger.getLogger(SpecialCostReportDao.class);
	
	
	private static SpecialCostReportDao dao = new SpecialCostReportDao() ;
	
	public SpecialCostReportDao() {
		
	};
	
	public static SpecialCostReportDao getInstance() {
		return dao;
	}
	
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public List<Map<String, Object>> getAreaList(){
		StringBuffer sql = new StringBuffer("");
		sql.append("SELECT ORG_ID, ORG_NAME FROM TM_ORG WHERE ORG_LEVEL = 2");
		List<Map<String, Object>> list = pageQuery(sql.toString(), null, getFunName());
		return list;
	}
	public List<Map<String, Object>> getAreaParentList(Long id){
		StringBuffer sql = new StringBuffer("");
		sql.append("SELECT ORG_LEVEL FROM TM_ORG WHERE ORG_ID = "+id+"");
		List<Map<String, Object>> list = pageQuery(sql.toString(), null, getFunName());
		return list;
	}
	
	public List<Map<String, Object>> getSeriesList(){
		StringBuffer sql = new StringBuffer("");
		sql.append("SELECT GROUP_ID, GROUP_CODE, GROUP_NAME FROM TM_VHCL_MATERIAL_GROUP WHERE GROUP_LEVEL = 2");
		List<Map<String, Object>> list = pageQuery(sql.toString(), null, getFunName());
		return list;
	}
	
	public List<Map<String, Object>> getModelGroupList(String groupType){
		StringBuffer sql = new StringBuffer("");
		sql.append("SELECT WRGROUP_ID, WRGROUP_NAME FROM TT_AS_WR_MODEL_GROUP WHERE WRGROUP_TYPE = ").append(groupType);
		List<Map<String, Object>> list = pageQuery(sql.toString(), null, getFunName());
		return list;
	} 
	
	public PageResult<Map<String, Object>> querySpecialCostReport(StandardVipApplyManagerBean bean,int curPage, int pageSize){
		StringBuffer sql= new StringBuffer();
		if(bean.getFeeType()==null||bean.getFeeType().equals("")){
			sql.append("select d.dealer_name,\n" );
			sql.append("       d.dealer_code,\n" );
			sql.append("       d.root_org_name,\n" );
			sql.append("       c.code_desc,\n" );
			sql.append("       s.fee_no,\n" );
			sql.append("       s.declare_sum1,s.declare_sum,\n" );
			sql.append("       s.vin,\n" );
			sql.append("       case\n" );
			sql.append("       when s.vin is not null then '1'\n" );
			sql.append("       else '0' end as cou,\n" );
			sql.append("       mg.group_code,\n" );
			sql.append("       mg1.group_name,s.declare_sum1 as average,s.declare_sum as average1\n" );
			sql.append("  from Tt_As_Wr_Spefee        s,\n" );
			sql.append("       vw_org_dealer_service  d,\n" );
			sql.append("       tc_code                c,\n" );
			sql.append("       tm_vehicle             v,\n" );
			sql.append("       tm_vhcl_material_group mg,\n" );
			sql.append("       tm_vhcl_material_group mg1\n" );
			sql.append(" where s.fee_type = "+Constant.FEE_TYPE_01+"\n" );
			sql.append("   and s.status in ("+Constant.SPEFEE_STATUS_02+","+Constant.SPEFEE_STATUS_04+", "+Constant.SPEFEE_STATUS_06+","+Constant.SPEFEE_STATUS_07+")\n" );
			sql.append("   and d.dealer_id = s.dealer_id\n" );
			sql.append("   and s.fee_type = c.code_id\n" );
			sql.append("   and v.model_id = mg.group_id(+)\n" );
			sql.append("   and v.series_id = mg1.group_id(+)\n" );
			//起始时间查询条件
			if(Utility.testString(bean.getBeginTime())){
				sql.append("     and s.make_date>=to_date('"+bean.getBeginTime()+"','YYYY-MM-DD HH24:MI:SS')  \n");	
			}  
			if(Utility.testString(bean.getBeginTime())){
				sql.append("     and s.make_date<=to_date('"+bean.getEndTime()+"','YYYY-MM-DD HH24:MI:SS')  \n");	
			}  
			sql.append("   and s.vin = v.vin(+)");
			if(Utility.testString(bean.getAreaName())){
				sql.append("  and d.root_org_id="+bean.getAreaName()+"  \n");	
			}
			//经销商查询条件
			if(Utility.testString(bean.getDealerCode())){
				sql.append("  and d.dealer_code="+bean.getDealerCode()+"  \n");	
			}
			//车系查询条件
			if(Utility.testString(bean.getSeriesName())){
				sql.append("   and v.series_id="+bean.getSeriesName()+"  \n");	
			}  
			sql.append(" union all \n " );
			sql.append("select d.dealer_name,\n" );
			sql.append("       d.dealer_code,\n" );
			sql.append("       d.root_org_name,\n" );
			sql.append("       c.code_desc,\n" );
			sql.append("       s.fee_no,\n" );
			sql.append("       s.declare_sum1,s.declare_sum,\n" );
			sql.append("       ps.vin,\n" );
			sql.append("       to_char((select count(1) from Tt_As_Wr_Spefee_Claim sc where sc.fee_id = s.id)) as cou,\n" );
			sql.append("       mg.group_code,\n" );
			sql.append("       mg1.group_name,s.declare_sum1/(select count(1) from Tt_As_Wr_Spefee_Claim sc where sc.fee_id = s.id) as  average,s.declare_sum/(select count(1) from Tt_As_Wr_Spefee_Claim sc where sc.fee_id = s.id) as  average1\n" );
			sql.append("  from Tt_As_Wr_Spefee        s,\n" );
			sql.append("       vw_org_dealer_service  d,\n" );
			sql.append("       tc_code                c,\n" );
			sql.append("       Tt_As_Wr_Spefee_Claim  ps,\n" );
			sql.append("       tm_vehicle             v,\n" );
			sql.append("       tm_vhcl_material_group mg,\n" );
			sql.append("       tm_vhcl_material_group mg1\n" );
			sql.append(" where s.fee_type = "+Constant.FEE_TYPE_02+"\n" );
			sql.append("   and s.status in ("+Constant.SPEFEE_STATUS_02+","+Constant.SPEFEE_STATUS_04+", "+Constant.SPEFEE_STATUS_06+","+Constant.SPEFEE_STATUS_07+")\n" );
			sql.append("   and d.dealer_id = s.dealer_id\n" );
			sql.append("   and s.fee_type = c.code_id\n" );
			sql.append("   and ps.fee_id = s.id\n" );
			sql.append("   and ps.vin = v.vin\n" );
			sql.append("   and v.model_id = mg.group_id\n" );
			sql.append("   and v.series_id = mg1.group_id\n" );
			//大区查询条件
			if(Utility.testString(bean.getAreaName())){
				sql.append("  and d.root_org_id="+bean.getAreaName()+"  \n");	
			}
			//经销商查询条件
			if(Utility.testString(bean.getDealerCode())){
				sql.append("  and d.dealer_code="+bean.getDealerCode()+"  \n");	
			}
			//车系查询条件
			if(Utility.testString(bean.getSeriesName())){
				sql.append("   and v.series_id="+bean.getSeriesName()+"  \n");	
			}  
			//起始时间查询条件
			if(Utility.testString(bean.getBeginTime())){
				
				sql.append("and exists (select sa.fee_id from Tt_As_Wr_Spefee_Auditing sa where sa.fee_id=s.id and sa.status=11841004\n" );
				sql.append("and  sa.create_date>=to_date('"+bean.getBeginTime()+"','YYYY-MM-DD HH24:MI:SS')\n" );
				sql.append("  and sa.create_date<=to_date('"+bean.getEndTime()+"','YYYY-MM-DD HH24:MI:SS')  )");

			}  
		
			//車型大類查詢條件
			if(Utility.testString(bean.getModelName())){
				sql.append("   and exists (select m.model_id from Tt_As_Wr_Model_Item m where m.wrgroup_id="+bean.getModelName()+" and m.model_id = v.package_id )  \n");	
			}  
		}else{
			if(bean.getFeeType().equals(Constant.FEE_TYPE_01)){
				sql.append("select d.dealer_name,\n" );
				sql.append("       d.dealer_code,\n" );
				sql.append("       d.root_org_name,\n" );
				sql.append("       c.code_desc,\n" );
				sql.append("       s.fee_no,\n" );
				sql.append("       s.declare_sum1,s.declare_sum,\n" );
				sql.append("       s.vin,\n" );
				sql.append("       case\n" );
				sql.append("       when s.vin is not null then '1'\n" );
				sql.append("       else '0' end as cou,\n" );
				sql.append("       mg.group_code,\n" );
				sql.append("       mg1.group_name,s.declare_sum1 as average,s.declare_sum as average1\n" );
				sql.append("  from Tt_As_Wr_Spefee        s,\n" );
				sql.append("       vw_org_dealer_service  d,\n" );
				sql.append("       tc_code                c,\n" );
				sql.append("       tm_vehicle             v,\n" );
				sql.append("       tm_vhcl_material_group mg,\n" );
				sql.append("       tm_vhcl_material_group mg1\n" );
				sql.append(" where s.fee_type = "+Constant.FEE_TYPE_01+"\n" );
				sql.append("   and s.status in ("+Constant.SPEFEE_STATUS_02+","+Constant.SPEFEE_STATUS_04+", "+Constant.SPEFEE_STATUS_06+","+Constant.SPEFEE_STATUS_07+")\n" );
				sql.append("   and d.dealer_id = s.dealer_id\n" );
				sql.append("   and s.fee_type = c.code_id\n" );
				sql.append("   and v.model_id = mg.group_id(+)\n" );
				sql.append("   and v.series_id = mg1.group_id(+)\n" );
				sql.append("   and s.vin = v.vin(+)");
				//大区查询条件
				if(Utility.testString(bean.getAreaName())){
					sql.append("  and d.root_org_id="+bean.getAreaName()+"  \n");	
				}
				//经销商查询条件
				if(Utility.testString(bean.getDealerCode())){
					sql.append("  and d.dealer_code="+bean.getDealerCode()+"  \n");	
				}
				//车系查询条件
				if(Utility.testString(bean.getSeriesName())){
					sql.append("   and v.series_id="+bean.getSeriesName()+"  \n");	
				} 
				//起始时间查询条件
				if(Utility.testString(bean.getBeginTime())){
					sql.append("     and s.make_date>=to_date('"+bean.getBeginTime()+"','YYYY-MM-DD HH24:MI:SS')  \n");	
				}  
				if(Utility.testString(bean.getBeginTime())){
					sql.append("     and s.make_date<=to_date('"+bean.getEndTime()+"','YYYY-MM-DD HH24:MI:SS')  \n");	
				}  
				//車型大類查詢條件
				if(Utility.testString(bean.getModelName())){
					sql.append("   and exists (select m.model_id from Tt_As_Wr_Model_Item m where m.wrgroup_id="+bean.getModelName()+" and m.model_id = v.package_id )  \n");	
				}   
			}else{
				sql.append("select d.dealer_name,\n" );
				sql.append("       d.dealer_code,\n" );
				sql.append("       d.root_org_name,\n" );
				sql.append("       c.code_desc,\n" );
				sql.append("       s.fee_no,\n" );
				sql.append("       s.declare_sum1,\n" );
				sql.append("       ps.vin,\n" );
				sql.append("       to_char((select count(1) from Tt_As_Wr_Spefee_Claim sc where sc.fee_id = s.id)) as cou,\n" );
				sql.append("       mg.group_code,\n" );
				sql.append("       mg1.group_name,s.declare_sum1/(select count(1) from Tt_As_Wr_Spefee_Claim sc where sc.fee_id = s.id) as  average,s.declare_sum/(select count(1) from Tt_As_Wr_Spefee_Claim sc where sc.fee_id = s.id) as  average1\n" );
				sql.append("  from Tt_As_Wr_Spefee        s,\n" );
				sql.append("       vw_org_dealer_service  d,\n" );
				sql.append("       tc_code                c,\n" );
				sql.append("       Tt_As_Wr_Spefee_Claim  ps,\n" );
				sql.append("       tm_vehicle             v,\n" );
				sql.append("       tm_vhcl_material_group mg,\n" );
				sql.append("       tm_vhcl_material_group mg1\n" );
				sql.append(" where s.fee_type = "+Constant.FEE_TYPE_02+"\n" );
				sql.append("   and s.status in ("+Constant.SPEFEE_STATUS_04+", "+Constant.SPEFEE_STATUS_06+","+Constant.SPEFEE_STATUS_07+")\n" );
				sql.append("   and d.dealer_id = s.dealer_id\n" );
				sql.append("   and s.fee_type = c.code_id\n" );
				sql.append("   and ps.fee_id = s.id\n" );
				sql.append("   and ps.vin = v.vin\n" );
				sql.append("   and v.model_id = mg.group_id\n" );
				sql.append("   and v.series_id = mg1.group_id\n" );
				//大区查询条件
				if(Utility.testString(bean.getAreaName())){
					sql.append("  and d.root_org_id="+bean.getAreaName()+"  \n");	
				}
				//经销商查询条件
				if(Utility.testString(bean.getDealerCode())){
					sql.append("  and d.dealer_code="+bean.getDealerCode()+"  \n");	
				}
				//车系查询条件
				if(Utility.testString(bean.getSeriesName())){
					sql.append("   and v.series_id="+bean.getSeriesName()+"  \n");	
				}  
				//起始时间查询条件
				if(Utility.testString(bean.getBeginTime())){
					
					sql.append("and exists (select sa.fee_id from Tt_As_Wr_Spefee_Auditing sa where sa.fee_id=s.id and sa.status=11841004\n" );
					sql.append("and  sa.create_date>=to_date('"+bean.getBeginTime()+"','YYYY-MM-DD HH24:MI:SS')\n" );
					sql.append("  and sa.create_date<=to_date('"+bean.getEndTime()+"','YYYY-MM-DD HH24:MI:SS')  )");

				}  
				//車型大類查詢條件
				if(Utility.testString(bean.getModelName())){
					sql.append("   and exists (select m.model_id from Tt_As_Wr_Model_Item m where m.wrgroup_id="+bean.getModelName()+" and m.model_id = v.package_id )  \n");	
				}  
			}
			}
		PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
		return ps;
	}
	
	public PageResult<Map<String, Object>> queryMainteranceVinReport(StandardVipApplyManagerBean bean,int curPage, int pageSize){
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT ROWNUM NUM, A.DEALER_CODE, A.DEALER_SHORTNAME, A.ORG_NAME,\n" );
		sql.append("       A.VIN, A.DEALER_NAME1\n" );
		sql.append("FROM(\n" );
		sql.append("    SELECT D.DEALER_CODE, D.DEALER_SHORTNAME, D.DEALER_SHORTNAME DEALER_NAME1,C.ORG_NAME, A.VIN\n" );
		sql.append("    FROM\n" );
		sql.append("        (\n" );
		sql.append("           SELECT DEALER_CODE, VIN\n" );
		sql.append("           FROM TT_AS_REPAIR_ORDER\n" );
		sql.append("   		   WHERE REPAIR_TYPE_CODE = '").append(Constant.REPAIR_TYPE_04).append("'\n");
		if(Utility.testString(bean.getModelName())){
			sql.append("       AND MODEL IN(SELECT B.GROUP_CODE FROM TT_AS_WR_MODEL_ITEM A, TM_VHCL_MATERIAL_GROUP B\n" );
			sql.append("               WHERE A.MODEL_ID = B.GROUP_ID\n" );
			sql.append("               AND A.WRGROUP_ID = ").append(bean.getModelName()).append(")\n"); 
		}
		if(Utility.testString(bean.getSeriesName())){
			sql.append("       AND SERIES = '").append(bean.getSeriesName()).append("'\n");
		}
		if(Utility.testString(bean.getBeginTime())){
			sql.append("       AND RO_CREATE_DATE >= TO_DATE('").append(bean.getBeginTime()).append("', 'YYYY-MM-DD HH24:MI:SS')\n");
		}
		if(Utility.testString(bean.getEndTime())){
			sql.append("       AND RO_CREATE_DATE <= TO_DATE('").append(bean.getEndTime()).append("', 'YYYY-MM-DD HH24:MI:SS')\n");
		}
		sql.append("        ) A,\n" );
		sql.append("       (\n" );
		sql.append("          SELECT DEALER_ID, DEALER_CODE, DEALER_SHORTNAME, PARENT_DEALER_D\n" );
		sql.append("          FROM TM_DEALER\n" );
		sql.append("   		  WHERE DEALER_TYPE = ").append(Constant.DEALER_TYPE_DWR).append("\n");
		sql.append("		  AND DEALER_LEVEL = ").append(Constant.DEALER_LEVEL_01).append("\n");
		if(Utility.testString(bean.getDealerCode())){
			sql.append("      AND DEALER_CODE IN (").append(bean.getDealerCode()).append(")\n"); 
		}
		sql.append("        ) D,\n" );
		sql.append("       (\n" );
		sql.append("          SELECT DEALER_ID, ORG_NAME\n" );
		sql.append("          FROM TM_ORG A, TM_DEALER_ORG_RELATION B\n" );
		sql.append("          WHERE A.ORG_ID = B.ORG_ID\n" );
		if(Utility.testString(bean.getAreaName())){
			sql.append("	  AND B.ORG_ID = ").append(bean.getAreaName()).append("\n");
		}
		sql.append("        ) C\n" );
		sql.append("    WHERE A.DEALER_CODE = D.DEALER_CODE\n" );
		sql.append("    AND D.DEALER_ID = C.DEALER_ID\n" );
		sql.append("UNION ALL\n");
		sql.append("    SELECT D.DEALER_CODE, D.DEALER_SHORTNAME, K.DEALER_SHORTNAME DEALER_NAME1,C.ORG_NAME, A.VIN\n" );
		sql.append("    FROM\n" );
		sql.append("        (\n" );
		sql.append("           SELECT DEALER_CODE, VIN\n" );
		sql.append("           FROM TT_AS_REPAIR_ORDER\n" );
		sql.append("   		   WHERE REPAIR_TYPE_CODE = '").append(Constant.REPAIR_TYPE_04).append("'\n");
		if(Utility.testString(bean.getModelName())){
			sql.append("       AND MODEL IN(SELECT B.GROUP_CODE FROM TT_AS_WR_MODEL_ITEM A, TM_VHCL_MATERIAL_GROUP B\n" );
			sql.append("               WHERE A.MODEL_ID = B.GROUP_ID\n" );
			sql.append("               AND A.WRGROUP_ID = ").append(bean.getModelName()).append(")\n"); 
		}
		if(Utility.testString(bean.getSeriesName())){
			sql.append("       AND SERIES = '").append(bean.getSeriesName()).append("'\n");
		}
		if(Utility.testString(bean.getBeginTime())){
			sql.append("       AND RO_CREATE_DATE >= TO_DATE('").append(bean.getBeginTime()).append("', 'YYYY-MM-DD HH24:MI:SS')\n");
		}
		if(Utility.testString(bean.getEndTime())){
			sql.append("       AND RO_CREATE_DATE <= TO_DATE('").append(bean.getEndTime()).append("', 'YYYY-MM-DD HH24:MI:SS')\n");
		}
		sql.append("        ) A,\n" );
		sql.append("       (\n" );
		sql.append("          SELECT DEALER_ID, DEALER_CODE, DEALER_SHORTNAME, PARENT_DEALER_D\n" );
		sql.append("          FROM TM_DEALER\n" );
		sql.append("   		  WHERE DEALER_TYPE = ").append(Constant.DEALER_TYPE_DWR).append("\n");
		sql.append("		  AND DEALER_LEVEL = ").append(Constant.DEALER_LEVEL_02).append("\n");
		if(Utility.testString(bean.getDealerCode())){
			sql.append("      AND DEALER_CODE IN (").append(bean.getDealerCode()).append(")\n"); 
		}
		sql.append("        ) D,\n" );
		sql.append("       (\n" );
		sql.append("          SELECT DEALER_ID, ORG_NAME\n" );
		sql.append("          FROM TM_ORG A, TM_DEALER_ORG_RELATION B\n" );
		sql.append("          WHERE A.ORG_ID = B.ORG_ID\n" );
		if(Utility.testString(bean.getAreaName())){
			sql.append("	  AND B.ORG_ID = ").append(bean.getAreaName()).append("\n");
		}
		sql.append("        ) C, TM_DEALER K\n" );
		sql.append("    WHERE A.DEALER_CODE = D.DEALER_CODE\n" );
		sql.append("    AND D.PARENT_DEALER_D = K.DEALER_ID\n");
		sql.append("    AND K.DEALER_ID = C.DEALER_ID\n" );
		sql.append(") A \n" );

		PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
		return ps;
	} 
	
	public PageResult<Map<String, Object>> queryDealerInfoReportReport(StandardVipApplyManagerBean bean, int curPage, int pageSize){
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT ROWNUM NUM, A.DEALER_NAME, A.DEALER_CODE, A.REGION_NAME, A.ORG_NAME, A.CREATE_DATE,A.HAND_PHONE, A.USER_STATUS, A.ACNT\n");
		sql.append("FROM ( ");
		sql.append("SELECT A.DEALER_NAME, A.DEALER_CODE, B.REGION_NAME, E.ORG_NAME,\n" );
		sql.append("       TO_CHAR(C.CREATE_DATE, 'YYYY-MM-DD') CREATE_DATE,\n" );
		sql.append("       C.HAND_PHONE, C.USER_STATUS, C.ACNT\n" );
		sql.append("FROM TM_DEALER A, TM_REGION B, TC_USER C,\n" );
		sql.append("     TM_ORG E, TM_DEALER_ORG_RELATION D\n" );
		sql.append("WHERE A.PROVINCE_ID = B.REGION_CODE\n" );
		sql.append("AND A.COMPANY_ID = C.COMPANY_ID\n" );
		sql.append("AND A.DEALER_ID = D.DEALER_ID\n" );
		sql.append("AND D.ORG_ID = E.ORG_ID\n");
		if(Utility.testString(bean.getAreaName())){
			sql.append("	  AND D.ORG_ID = ").append(bean.getAreaName()).append("\n");
		}
		if(Utility.testString(bean.getProvince())){
			sql.append("	  AND B.REGION_CODE = '").append(bean.getProvince()).append("'\n");
		}
		if(Utility.testString(bean.getDealerCode())){
			sql.append("      AND A.DEALER_CODE IN (").append(bean.getDealerCode()).append(")\n"); 
		}
		if(Utility.testString(bean.getDealerName())){
			sql.append("      AND A.DEALER_NAME LIKE '%").append(bean.getDealerName()).append("%'\n"); 
		}
		if(Utility.testString(bean.getBeginTime())){
			sql.append("       AND C.CREATE_DATE >= TO_DATE('").append(bean.getBeginTime()).append("', 'YYYY-MM-DD HH24:MI:SS')\n");
		}
		if(Utility.testString(bean.getEndTime())){
			sql.append("       AND C.CREATE_DATE <= TO_DATE('").append(bean.getEndTime()).append("', 'YYYY-MM-DD HH24:MI:SS')\n");
		}
		
		sql.append("UNION ALL\n");
		
		sql.append("SELECT A.DEALER_NAME, A.DEALER_CODE, B.REGION_NAME, E.ORG_NAME,\n" );
		sql.append("       TO_CHAR(C.CREATE_DATE, 'YYYY-MM-DD') CREATE_DATE,\n" );
		sql.append("       C.HAND_PHONE, C.USER_STATUS, C.ACNT\n" );
		sql.append("FROM TM_DEALER A, TM_DEALER F, TM_REGION B, TC_USER C,\n" );
		sql.append("     TM_ORG E, TM_DEALER_ORG_RELATION D\n" );
		sql.append("WHERE A.PROVINCE_ID = B.REGION_CODE\n" );
		sql.append("AND A.COMPANY_ID = C.COMPANY_ID\n" );
		sql.append("AND A.PARENT_DEALER_D = F.DEALER_ID\n" );
		sql.append("AND F.DEALER_ID = D.DEALER_ID\n");
		sql.append("AND D.ORG_ID = E.ORG_ID\n");
		if(Utility.testString(bean.getAreaName())){
			sql.append("	  AND D.ORG_ID = ").append(bean.getAreaName()).append("\n");
		}
		if(Utility.testString(bean.getProvince())){
			sql.append("	  AND B.REGION_CODE = '").append(bean.getProvince()).append("'\n");
		}
		if(Utility.testString(bean.getDealerCode())){
			sql.append("      AND A.DEALER_CODE IN (").append(bean.getDealerCode()).append(")\n"); 
		}
		if(Utility.testString(bean.getDealerName())){
			sql.append("      AND A.DEALER_NAME LIKE '%").append(bean.getDealerName()).append("%'\n"); 
		}
		if(Utility.testString(bean.getBeginTime())){
			sql.append("       AND C.CREATE_DATE >= TO_DATE('").append(bean.getBeginTime()).append("', 'YYYY-MM-DD HH24:MI:SS')\n");
		}
		if(Utility.testString(bean.getEndTime())){
			sql.append("       AND C.CREATE_DATE <= TO_DATE('").append(bean.getEndTime()).append("', 'YYYY-MM-DD HH24:MI:SS')\n");
		}
		
		sql.append(") A\n");
		
		PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
		return ps;
	}
	
	public List<Map<String, Object>> queryDealerInfoReportExcel(StandardVipApplyManagerBean bean){
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT ROWNUM NUM, A.DEALER_NAME, A.DEALER_CODE, A.REGION_NAME, A.ORG_NAME, A.CREATE_DATE,A.HAND_PHONE, A.CODE_DESC, A.ACNT\n");
		sql.append("FROM ( ");
		sql.append("SELECT A.DEALER_NAME, A.DEALER_CODE, B.REGION_NAME, E.ORG_NAME,\n" );
		sql.append("       TO_CHAR(C.CREATE_DATE, 'YYYY-MM-DD') CREATE_DATE,\n" );
		sql.append("       C.HAND_PHONE, G.CODE_DESC, C.ACNT\n" );
		sql.append("FROM TM_DEALER A, TM_REGION B, TC_USER C,\n" );
		sql.append("     TM_ORG E, TC_CODE G, TM_DEALER_ORG_RELATION D\n" );
		sql.append("WHERE A.PROVINCE_ID = B.REGION_CODE\n" );
		sql.append("AND A.COMPANY_ID = C.COMPANY_ID\n" );
		sql.append("AND A.DEALER_ID = D.DEALER_ID\n" );
		sql.append("AND C.USER_STATUS = G.CODE_ID\n");
		sql.append("AND D.ORG_ID = E.ORG_ID\n");
		if(Utility.testString(bean.getAreaName())){
			sql.append("	  AND D.ORG_ID = ").append(bean.getAreaName()).append("\n");
		}
		if(Utility.testString(bean.getProvince())){
			sql.append("	  AND B.REGION_CODE = '").append(bean.getProvince()).append("'\n");
		}
		if(Utility.testString(bean.getDealerCode())){
			sql.append("      AND A.DEALER_CODE IN (").append(bean.getDealerCode()).append(")\n"); 
		}
		if(Utility.testString(bean.getDealerName())){
			sql.append("      AND A.DEALER_NAME LIKE '%").append(bean.getDealerName()).append("%'\n"); 
		}
		if(Utility.testString(bean.getBeginTime())){
			sql.append("       AND C.CREATE_DATE >= TO_DATE('").append(bean.getBeginTime()).append("', 'YYYY-MM-DD HH24:MI:SS')\n");
		}
		if(Utility.testString(bean.getEndTime())){
			sql.append("       AND C.CREATE_DATE <= TO_DATE('").append(bean.getEndTime()).append("', 'YYYY-MM-DD HH24:MI:SS')\n");
		}
		
		sql.append("UNION ALL\n");
		
		sql.append("SELECT A.DEALER_NAME, A.DEALER_CODE, B.REGION_NAME, E.ORG_NAME,\n" );
		sql.append("       TO_CHAR(C.CREATE_DATE, 'YYYY-MM-DD') CREATE_DATE,\n" );
		sql.append("       C.HAND_PHONE, G.CODE_DESC, C.ACNT\n" );
		sql.append("FROM TM_DEALER A, TM_DEALER F, TM_REGION B, TC_USER C,\n" );
		sql.append("     TM_ORG E, TC_CODE G, TM_DEALER_ORG_RELATION D\n" );
		sql.append("WHERE A.PROVINCE_ID = B.REGION_CODE\n" );
		sql.append("AND A.COMPANY_ID = C.COMPANY_ID\n" );
		sql.append("AND A.PARENT_DEALER_D = F.DEALER_ID\n" );
		sql.append("AND F.DEALER_ID = D.DEALER_ID\n");
		sql.append("AND D.ORG_ID = E.ORG_ID\n");
		sql.append("AND C.USER_STATUS = G.CODE_ID\n");
		if(Utility.testString(bean.getAreaName())){
			sql.append("	  AND D.ORG_ID = ").append(bean.getAreaName()).append("\n");
		}
		if(Utility.testString(bean.getProvince())){
			sql.append("	  AND B.REGION_CODE = '").append(bean.getProvince()).append("'\n");
		}
		if(Utility.testString(bean.getDealerCode())){
			sql.append("      AND A.DEALER_CODE IN (").append(bean.getDealerCode()).append(")\n"); 
		}
		if(Utility.testString(bean.getDealerName())){
			sql.append("      AND A.DEALER_NAME LIKE '%").append(bean.getDealerName()).append("%'\n"); 
		}
		if(Utility.testString(bean.getBeginTime())){
			sql.append("       AND C.CREATE_DATE >= TO_DATE('").append(bean.getBeginTime()).append("', 'YYYY-MM-DD HH24:MI:SS')\n");
		}
		if(Utility.testString(bean.getEndTime())){
			sql.append("       AND C.CREATE_DATE <= TO_DATE('").append(bean.getEndTime()).append("', 'YYYY-MM-DD HH24:MI:SS')\n");
		}
		
		sql.append(") A\n");
		
		List<Map<String, Object>> list = pageQuery(sql.toString(), null, getFunName());
		return list;
	}
	
	public PageResult<Map<String, Object>> queryPreserveVehicleChangeCountReport(StandardVipApplyManagerBean bean, int curPage, int pageSize){
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT ROWNUM NUM, A.GROUP_CODE, A.PART_CODE, A.PART_NAME, PCOUNT FROM( \n");
		sql.append("SELECT D.GROUP_CODE, B.PART_CODE, B.PART_NAME, COUNT(B.ID) PCOUNT\n" );
		sql.append("FROM\n" );
		sql.append("     (\n" );
		sql.append("       SELECT ID, VIN\n" );
		sql.append("       FROM TT_AS_WR_APPLICATION\n" );
		sql.append("       WHERE CLAIM_TYPE IN (").append(Constant.CLA_TYPE_01).append(",");
		sql.append(Constant.CLA_TYPE_07).append(",");
		sql.append(Constant.CLA_TYPE_09);
		sql.append("      )\n");
		if(Utility.testString(bean.getModelName())){
			sql.append("   AND MODEL_CODE IN(SELECT B.GROUP_CODE FROM TT_AS_WR_MODEL_ITEM A, TM_VHCL_MATERIAL_GROUP B\n" );
			sql.append("                WHERE A.MODEL_ID = B.GROUP_ID\n" );
			sql.append("                AND A.WRGROUP_ID = ").append(bean.getModelName()).append(")\n"); 
		}
		if(Utility.testString(bean.getBendTime())){
			sql.append("   AND RO_STARTDATE >= TO_DATE('").append(bean.getBeginTime()).append("', 'YYYY-MM-DD HH24:MI:SS')\n");
		}
		if(Utility.testString(bean.getEndTime())){
			sql.append("   AND RO_ENDDATE <= TO_DATE('").append(bean.getEndTime()).append("', 'YYYY-MM-DD HH24:MI:SS')\n");
		}
		if(Utility.testString(bean.getClaType())){
			sql.append("   AND CLAIM_TYPE = ").append(bean.getClaType()).append("\n");
		}
		sql.append("      ) A,\n" );
		sql.append("     TT_AS_WR_PARTSITEM B\n" );
		sql.append("     ,\n" );
		sql.append("     ( SELECT VIN,MODEL_ID\n" );
		sql.append("       FROM TM_VEHICLE\n" );
		sql.append("       WHERE 1=1\n" );
		if(Utility.testString(bean.getPbeginTime())){
			sql.append("   AND PRODUCT_DATE >= TO_DATE('").append(bean.getPbeginTime()).append("', 'YYYY-MM-DD HH24:MI:SS')\n");
		}
		if(Utility.testString(bean.getPendTime())){
			sql.append("   AND PRODUCT_DATE <= TO_DATE('").append(bean.getPendTime()).append("', 'YYYY-MM-DD HH24:MI:SS')\n");
		}
		if(Utility.testString(bean.getBbeginTime())){
			sql.append("   AND PURCHASED_DATE >= TO_DATE('").append(bean.getBbeginTime()).append("', 'YYYY-MM-DD HH24:MI:SS')\n");
		}
		if(Utility.testString(bean.getBendTime())){
			sql.append("   AND PURCHASED_DATE <= TO_DATE('").append(bean.getBendTime()).append("', 'YYYY-MM-DD HH24:MI:SS')\n");
		}
		sql.append("     ) C,\n" );
		sql.append("     (\n" );
		sql.append("       SELECT GROUP_ID, GROUP_CODE,GROUP_NAME\n" );
		sql.append("       FROM TM_VHCL_MATERIAL_GROUP\n" );
		sql.append("       WHERE GROUP_LEVEL = 3\n" );
		if(Utility.testString(bean.getGroupCode())){
			sql.append("   AND GROUP_CODE IN (").append(bean.getGroupCode()).append(")");
		}
		sql.append("      ) D\n" );
		sql.append("WHERE A.VIN = C.VIN\n" );
		sql.append("AND A.ID = B.ID\n" );
		sql.append("AND C.MODEL_ID = D.GROUP_ID\n" );
		sql.append("GROUP BY D.GROUP_CODE, B.PART_CODE, B.PART_NAME ) A\n");

		PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
		return ps;
	}
	
	public PageResult<Map<String, Object>> queryVehicleByVinReport(String vin, int curPage, int pageSize){
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT  ROWNUM NUM, A.VIN, A.ENGINE_NO,B.MODEL_CODE,\n" );
		sql.append("        A.MILEAGE, A.CLAIM_TACTICS_ID, A.LIFE_CYCLE,NVL(A.FREE_TIMES,0) AS FREE_TIMES,\n" );
		sql.append("        TO_CHAR(A.PRODUCT_DATE, 'YYYY-MM-DD') PRODUCT_DATE,g.game_name,\n" );
		sql.append("        TO_CHAR(A.PURCHASED_DATE, 'YYYY-MM-DD') PURCHASED_DATE,\n" );
		sql.append("        B.COLOR_NAME\n" );
		sql.append("FROM TM_VEHICLE A, TM_VHCL_MATERIAL B, TT_AS_WR_GAME g\n" );
		sql.append("WHERE A.MATERIAL_ID = B.MATERIAL_ID and a.claim_tactics_id = g.id(+) \n" );
		sql.append("AND A.VIN = '").append(vin).append("'\n"); 

		PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
		return ps;
	}
	
	public PageResult<Map<String, Object>> vehicleRepairReport(String vin, int curPage, int pageSize){
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT ROWNUM NUM, A.CLAIM_TYPE, A.VIN, D.DEALER_SHORTNAME, TO_CHAR(A.RO_STARTDATE, 'YYYY-MM-DD') RO_STARTDATE, A.IN_MILEAGE,\n" );
		sql.append("       TO_CHAR(B.PRODUCT_DATE, 'YYYY-MM-DD') PRODUCT_DATE, C.TROUBLE_CODE_NAME, C.DAMAGE_AREA_NAME,  C.COUNTID\n" );
		sql.append("FROM TT_AS_WR_APPLICATION A, TM_VEHICLE B, TM_DEALER D,\n" );
		sql.append("     (\n" );
		sql.append("        SELECT  C.ID, C.TROUBLE_CODE_NAME, C.DAMAGE_AREA_NAME, COUNT(ID) COUNTID\n" );
		sql.append("        FROM TT_AS_WR_LABOURITEM C\n" );
		sql.append("        GROUP BY C.ID, C.TROUBLE_CODE_NAME, C.DAMAGE_AREA_NAME\n" );
		sql.append("     ) C\n" );
		sql.append("WHERE A.ID = C.ID\n" );
		sql.append("AND A.DEALER_ID = D.DEALER_ID\n");
		sql.append("AND A.VIN = B.VIN\n" );
		sql.append("AND B.VIN = '").append(vin).append("'\n"); 

		PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
		return ps;
	}
	
	public PageResult<Map<String,Object>> maintainReport(String vin,int curPage,int pageSize){
		StringBuffer sql= new StringBuffer("\n");
		sql.append("select c.ctm_name,\n");
		sql.append("       v.vin,\n");  
		sql.append("       to_char(v.purchased_date,'yyyy-MM-dd') purchased_date,\n");  
		sql.append("       g.group_name,\n");  
		sql.append("       c.address,\n");  
		sql.append("       c.main_phone || ' ' || c.other_phone phone,\n");  
		sql.append("       (select nvl(sum(1),0)\n");  
		sql.append("          from tt_as_wr_application a\n");  
		sql.append("         where a.vin = v.vin\n");  
		sql.append("           and a.claim_type = 10661001) weixiu,\n");  
		sql.append("       (select nvl(sum(1),0)\n");  
		sql.append("          from tt_as_repair_order o\n");  
		sql.append("         where o.vin = v.vin\n");  
		sql.append("           and o.repair_type_code = 11441004\n");  
		sql.append("           and nvl(o.order_valuable_type,0) != 13591002) baoyang,\n");  
		sql.append("       (select nvl(sum(1),0)\n");  
		sql.append("          from tt_as_wr_application a\n");  
		sql.append("         where a.vin = v.vin\n");  
		sql.append("           and a.claim_type = 10661006) fuwuhuodong\n");  
		sql.append("  from tm_vehicle             v,\n");  
		sql.append("       tt_dealer_actual_sales s,\n");  
		sql.append("       tt_customer            c,\n");  
		sql.append("       tm_vhcl_material_group g\n");  
		sql.append(" where s.vehicle_id(+) = v.vehicle_id\n");  
		sql.append("   and s.ctm_id = c.ctm_id(+)\n");  
		sql.append("   and v.model_id = g.group_id\n");  
		sql.append("   and v.vin = '").append(vin).append("'\n");
		return  pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
	}
	
	public List<Map<String, Object>> maintainReportExcel(String vin){
		StringBuffer sql = new StringBuffer();
		sql.append("select c.ctm_name,\n");
		sql.append("       v.vin,\n");  
		sql.append("       to_char(v.purchased_date,'yyyy-MM-dd') purchased_date,\n");  
		sql.append("       g.group_name,\n");  
		sql.append("       c.address,\n");  
		sql.append("       c.main_phone || ' ' || c.other_phone phone,\n");  
		sql.append("       (select nvl(sum(1),0)\n");  
		sql.append("          from tt_as_wr_application a\n");  
		sql.append("         where a.vin = v.vin\n");  
		sql.append("           and a.claim_type = 10661001) weixiu,\n");  
		sql.append("       (select nvl(sum(1),0)\n");  
		sql.append("          from tt_as_repair_order o\n");  
		sql.append("         where o.vin = v.vin\n");  
		sql.append("           and o.repair_type_code = 11441004\n");  
		sql.append("           and nvl(o.order_valuable_type,0) != 13591002) baoyang,\n");  
		sql.append("       (select nvl(sum(1),0)\n");  
		sql.append("          from tt_as_wr_application a\n");  
		sql.append("         where a.vin = v.vin\n");  
		sql.append("           and a.claim_type = 10661006) fuwuhuodong\n");  
		sql.append("  from tm_vehicle             v,\n");  
		sql.append("       tt_dealer_actual_sales s,\n");  
		sql.append("       tt_customer            c,\n");  
		sql.append("       tm_vhcl_material_group g\n");  
		sql.append(" where s.vehicle_id(+) = v.vehicle_id\n");  
		sql.append("   and s.ctm_id = c.ctm_id(+)\n");  
		sql.append("   and v.model_id = g.group_id\n");  
		sql.append("   and v.vin = '").append(vin).append("'\n");
		List<Map<String, Object>> list = pageQuery(sql.toString(), null, getFunName());
		return list;
	}
	
	public PageResult<Map<String, Object>> vehicleGoodRepairReportReport(String vin, int curPage, int pageSize){
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT ROWNUM NUM, A.VIN, B.ENGINE_NO, A.OWNER_NAME, B.MILEAGE,\n" );
		sql.append("       TO_CHAR(B.PURCHASED_DATE, 'YYYY-MM-DD') PURCHASED_DATE,\n" );
		sql.append("       TO_CHAR(A.RO_CREATE_DATE, 'YYYY-MM-DD') RO_CREATE_DATE,\n" );
		sql.append("       TO_CHAR(B.PRODUCT_DATE, 'YYYY-MM-DD') PRODUCT_DATE,\n" );
		sql.append("       A.FREE_TIMES, C.DEALER_SHORTNAME\n" );
		sql.append("FROM TT_AS_REPAIR_ORDER A, TM_VEHICLE B, TM_DEALER C\n" );
		sql.append("WHERE A.VIN = B.VIN\n" );
		sql.append("AND A.DEALER_CODE = C.DEALER_CODE\n" );
		sql.append("AND A.VIN = '").append(vin).append("'\n"); 

		PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
		return ps;
	}
	
	public List<Map<String, Object>> queryVehicleGoodRepairReportExcel(String vin){
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT ROWNUM NUM, A.VIN, B.ENGINE_NO, A.OWNER_NAME, B.MILEAGE,\n" );
		sql.append("       TO_CHAR(B.PURCHASED_DATE, 'YYYY-MM-DD') PURCHASED_DATE,\n" );
		sql.append("       TO_CHAR(A.RO_CREATE_DATE, 'YYYY-MM-DD') RO_CREATE_DATE,\n" );
		sql.append("       TO_CHAR(B.PRODUCT_DATE, 'YYYY-MM-DD') PRODUCT_DATE,\n" );
		sql.append("       A.FREE_TIMES, C.DEALER_SHORTNAME\n" );
		sql.append("FROM TT_AS_REPAIR_ORDER A, TM_VEHICLE B, TM_DEALER C\n" );
		sql.append("WHERE A.VIN = B.VIN\n" );
		sql.append("AND A.DEALER_CODE = C.DEALER_CODE\n" );
		sql.append("AND A.VIN = '").append(vin).append("'\n"); 
		List<Map<String, Object>> list = pageQuery(sql.toString(), null, getFunName());
		return list;
	}
	
	
	public List<Map<String, Object>> queryVehicleRepairReportExcel(String vin){
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT ROWNUM NUM, E.CODE_DESC, A.VIN, D.DEALER_SHORTNAME, TO_CHAR(A.RO_STARTDATE, 'YYYY-MM-DD') RO_STARTDATE, A.IN_MILEAGE,\n" );
		sql.append("       TO_CHAR(B.PRODUCT_DATE, 'YYYY-MM-DD') PRODUCT_DATE, C.TROUBLE_CODE_NAME, C.DAMAGE_AREA_NAME,  C.COUNTID\n" );
		sql.append("FROM TT_AS_WR_APPLICATION A, TM_VEHICLE B, TM_DEALER D, TC_CODE E,\n" );
		sql.append("     (\n" );
		sql.append("        SELECT  C.ID, C.TROUBLE_CODE_NAME, C.DAMAGE_AREA_NAME, COUNT(ID) COUNTID\n" );
		sql.append("        FROM TT_AS_WR_LABOURITEM C\n" );
		sql.append("        GROUP BY C.ID, C.TROUBLE_CODE_NAME, C.DAMAGE_AREA_NAME\n" );
		sql.append("     ) C\n" );
		sql.append("WHERE A.ID = C.ID\n" );
		sql.append("AND A.DEALER_ID = D.DEALER_ID\n");
		sql.append("AND A.CLAIM_TYPE = E.CODE_ID\n");
		sql.append("AND A.VIN = B.VIN\n" );
		sql.append("AND B.VIN = '").append(vin).append("'\n"); 
		List<Map<String, Object>> list = pageQuery(sql.toString(), null, getFunName());
		return list;
	}
	
	public List<Map<String, Object>> queryVehicleByVinReportExcelList(String vin){
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT  ROWNUM NUM, A.VIN, A.ENGINE_NO,B.MODEL_CODE,\n" );
		sql.append("        A.MILEAGE, A.CLAIM_TACTICS_ID, C.CODE_DESC,\n" );
		sql.append("        TO_CHAR(A.PRODUCT_DATE, 'YYYY-MM-DD') PRODUCT_DATE,\n" );
		sql.append("        TO_CHAR(A.PURCHASED_DATE, 'YYYY-MM-DD') PURCHASED_DATE,\n" );
		sql.append("        B.COLOR_NAME\n" );
		sql.append("FROM TM_VEHICLE A, TM_VHCL_MATERIAL B, TC_CODE C\n" );
		sql.append("WHERE A.MATERIAL_ID = B.MATERIAL_ID\n" );
		sql.append("AND A.LIFE_CYCLE = C.CODE_ID\n");
		sql.append("AND A.VIN = '").append(vin).append("'\n"); 
		List<Map<String, Object>> list = pageQuery(sql.toString(), null, getFunName());
		return list;
	}
	
	public PageResult<Map<String, Object>> queryPreserveVehicleWorkCountReport(StandardVipApplyManagerBean bean, int curPage, int pageSize){
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT ROWNUM NUM, A.GROUP_CODE, A.PART_CODE, A.PART_NAME, PCOUNT FROM( \n");
		sql.append("SELECT D.GROUP_CODE, B.WR_LABOURCODE PART_CODE, B.WR_LABOURNAME PART_NAME, COUNT(B.ID) PCOUNT\n" );
		sql.append("FROM\n" );
		sql.append("     (\n" );
		sql.append("       SELECT ID, VIN\n" );
		sql.append("       FROM TT_AS_WR_APPLICATION\n" );
		sql.append("       WHERE CLAIM_TYPE IN (").append(Constant.CLA_TYPE_01).append(",");
		sql.append(Constant.CLA_TYPE_07).append(",");
		sql.append(Constant.CLA_TYPE_09);
		sql.append("      )\n");
		if(Utility.testString(bean.getModelName())){
			sql.append("   AND MODEL_CODE IN(SELECT B.GROUP_CODE FROM TT_AS_WR_MODEL_ITEM A, TM_VHCL_MATERIAL_GROUP B\n" );
			sql.append("                WHERE A.MODEL_ID = B.GROUP_ID\n" );
			sql.append("                AND A.WRGROUP_ID = ").append(bean.getModelName()).append(")\n"); 
		}
		if(Utility.testString(bean.getBendTime())){
			sql.append("   AND RO_STARTDATE >= TO_DATE('").append(bean.getBeginTime()).append("', 'YYYY-MM-DD HH24:MI:SS')\n");
		}
		if(Utility.testString(bean.getEndTime())){
			sql.append("   AND RO_ENDDATE <= TO_DATE('").append(bean.getEndTime()).append("', 'YYYY-MM-DD HH24:MI:SS')\n");
		}
		if(Utility.testString(bean.getClaType())){
			sql.append("   AND CLAIM_TYPE = ").append(bean.getClaType()).append("\n");
		}
		sql.append("      ) A,\n" );
		sql.append("     TT_AS_WR_LABOURITEM B\n" );
		sql.append("     ,\n" );
		sql.append("     ( SELECT VIN,MODEL_ID\n" );
		sql.append("       FROM TM_VEHICLE\n" );
		sql.append("       WHERE 1=1\n" );
		if(Utility.testString(bean.getPbeginTime())){
			sql.append("   AND PRODUCT_DATE >= TO_DATE('").append(bean.getPbeginTime()).append("', 'YYYY-MM-DD HH24:MI:SS')\n");
		}
		if(Utility.testString(bean.getPendTime())){
			sql.append("   AND PRODUCT_DATE <= TO_DATE('").append(bean.getPendTime()).append("', 'YYYY-MM-DD HH24:MI:SS')\n");
		}
		if(Utility.testString(bean.getBbeginTime())){
			sql.append("   AND PURCHASED_DATE >= TO_DATE('").append(bean.getBbeginTime()).append("', 'YYYY-MM-DD HH24:MI:SS')\n");
		}
		if(Utility.testString(bean.getBendTime())){
			sql.append("   AND PURCHASED_DATE <= TO_DATE('").append(bean.getBendTime()).append("', 'YYYY-MM-DD HH24:MI:SS')\n");
		}
		sql.append("     ) C,\n" );
		sql.append("     (\n" );
		sql.append("       SELECT GROUP_ID, GROUP_CODE,GROUP_NAME\n" );
		sql.append("       FROM TM_VHCL_MATERIAL_GROUP\n" );
		sql.append("       WHERE GROUP_LEVEL = 3\n" );
		if(Utility.testString(bean.getGroupCode())){
			sql.append("   AND GROUP_CODE IN (").append(bean.getGroupCode()).append(")");
		}
		sql.append("      ) D\n" );
		sql.append("WHERE A.VIN = C.VIN\n" );
		sql.append("AND A.ID = B.ID\n" );
		sql.append("AND C.MODEL_ID = D.GROUP_ID\n" );
		sql.append("GROUP BY D.GROUP_CODE, B.WR_LABOURCODE, B.WR_LABOURNAME ) A\n");

		PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
		return ps;
	}
	
	public List<Map<String, Object>> preserveVehicleWorkCountReportExcel(StandardVipApplyManagerBean bean){
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT ROWNUM NUM, A.GROUP_CODE, A.PART_CODE, A.PART_NAME, PCOUNT FROM( \n");
		sql.append("SELECT D.GROUP_CODE, B.WR_LABOURCODE PART_CODE, B.WR_LABOURNAME PART_NAME, COUNT(B.ID) PCOUNT\n" );
		sql.append("FROM\n" );
		sql.append("     (\n" );
		sql.append("       SELECT ID, VIN\n" );
		sql.append("       FROM TT_AS_WR_APPLICATION\n" );
		sql.append("       WHERE CLAIM_TYPE IN (").append(Constant.CLA_TYPE_01).append(",");
		sql.append(Constant.CLA_TYPE_07).append(",");
		sql.append(Constant.CLA_TYPE_09);
		sql.append("      )\n");
		if(Utility.testString(bean.getModelName())){
			sql.append("   AND MODEL_CODE IN(SELECT B.GROUP_CODE FROM TT_AS_WR_MODEL_ITEM A, TM_VHCL_MATERIAL_GROUP B\n" );
			sql.append("                WHERE A.MODEL_ID = B.GROUP_ID\n" );
			sql.append("                AND A.WRGROUP_ID = ").append(bean.getModelName()).append(")\n"); 
		}
		if(Utility.testString(bean.getBendTime())){
			sql.append("   AND RO_STARTDATE >= TO_DATE('").append(bean.getBeginTime()).append("', 'YYYY-MM-DD HH24:MI:SS')\n");
		}
		if(Utility.testString(bean.getEndTime())){
			sql.append("   AND RO_ENDDATE <= TO_DATE('").append(bean.getEndTime()).append("', 'YYYY-MM-DD HH24:MI:SS')\n");
		}
		if(Utility.testString(bean.getClaType())){
			sql.append("   AND CLAIM_TYPE = ").append(bean.getClaType()).append("\n");
		}
		sql.append("      ) A,\n" );
		sql.append("     TT_AS_WR_LABOURITEM B\n" );
		sql.append("     ,\n" );
		sql.append("     ( SELECT VIN,MODEL_ID\n" );
		sql.append("       FROM TM_VEHICLE\n" );
		sql.append("       WHERE 1=1\n" );
		if(Utility.testString(bean.getPbeginTime())){
			sql.append("   AND PRODUCT_DATE >= TO_DATE('").append(bean.getPbeginTime()).append("', 'YYYY-MM-DD HH24:MI:SS')\n");
		}
		if(Utility.testString(bean.getPendTime())){
			sql.append("   AND PRODUCT_DATE <= TO_DATE('").append(bean.getPendTime()).append("', 'YYYY-MM-DD HH24:MI:SS')\n");
		}
		if(Utility.testString(bean.getBbeginTime())){
			sql.append("   AND PURCHASED_DATE >= TO_DATE('").append(bean.getBbeginTime()).append("', 'YYYY-MM-DD HH24:MI:SS')\n");
		}
		if(Utility.testString(bean.getBendTime())){
			sql.append("   AND PURCHASED_DATE <= TO_DATE('").append(bean.getBendTime()).append("', 'YYYY-MM-DD HH24:MI:SS')\n");
		}
		sql.append("     ) C,\n" );
		sql.append("     (\n" );
		sql.append("       SELECT GROUP_ID, GROUP_CODE,GROUP_NAME\n" );
		sql.append("       FROM TM_VHCL_MATERIAL_GROUP\n" );
		sql.append("       WHERE GROUP_LEVEL = 3\n" );
		if(Utility.testString(bean.getGroupCode())){
			sql.append("   AND GROUP_CODE IN (").append(bean.getGroupCode()).append(")");
		}
		sql.append("      ) D\n" );
		sql.append("WHERE A.VIN = C.VIN\n" );
		sql.append("AND A.ID = B.ID\n" );
		sql.append("AND C.MODEL_ID = D.GROUP_ID\n" );
		sql.append("GROUP BY D.GROUP_CODE, B.WR_LABOURCODE, B.WR_LABOURNAME ) A\n");
		List<Map<String, Object>> list = pageQuery(sql.toString(), null, getFunName());
		return list;
	}
	
	public List<Map<String, Object>> getPreserveVehicleChangeCountExcelList(StandardVipApplyManagerBean bean){
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT ROWNUM NUM, A.GROUP_CODE, A.PART_CODE, A.PART_NAME, PCOUNT FROM( \n");
		sql.append("SELECT D.GROUP_CODE, B.PART_CODE, B.PART_NAME, COUNT(B.ID) PCOUNT\n" );
		sql.append("FROM\n" );
		sql.append("     (\n" );
		sql.append("       SELECT ID, VIN\n" );
		sql.append("       FROM TT_AS_WR_APPLICATION\n" );
		sql.append("       WHERE CLAIM_TYPE IN (").append(Constant.CLA_TYPE_01).append(",");
		sql.append(Constant.CLA_TYPE_07).append(",");
		sql.append(Constant.CLA_TYPE_09);
		sql.append("      )\n");
		if(Utility.testString(bean.getModelName())){
			sql.append("   AND MODEL_CODE IN(SELECT B.GROUP_CODE FROM TT_AS_WR_MODEL_ITEM A, TM_VHCL_MATERIAL_GROUP B\n" );
			sql.append("                WHERE A.MODEL_ID = B.GROUP_ID\n" );
			sql.append("                AND A.WRGROUP_ID = ").append(bean.getModelName()).append(")\n"); 
		}
		if(Utility.testString(bean.getBendTime())){
			sql.append("   AND RO_STARTDATE >= TO_DATE('").append(bean.getBeginTime()).append("', 'YYYY-MM-DD HH24:MI:SS')\n");
		}
		if(Utility.testString(bean.getEndTime())){
			sql.append("   AND RO_ENDDATE <= TO_DATE('").append(bean.getEndTime()).append("', 'YYYY-MM-DD HH24:MI:SS')\n");
		}
		if(Utility.testString(bean.getClaType())){
			sql.append("   AND CLAIM_TYPE = ").append(bean.getClaType()).append("\n");
		}
		sql.append("      ) A,\n" );
		sql.append("     TT_AS_WR_PARTSITEM B\n" );
		sql.append("     ,\n" );
		sql.append("     ( SELECT VIN,MODEL_ID\n" );
		sql.append("       FROM TM_VEHICLE\n" );
		sql.append("       WHERE 1=1\n" );
		if(Utility.testString(bean.getPbeginTime())){
			sql.append("   AND PRODUCT_DATE >= TO_DATE('").append(bean.getPbeginTime()).append("', 'YYYY-MM-DD HH24:MI:SS')\n");
		}
		if(Utility.testString(bean.getPendTime())){
			sql.append("   AND PRODUCT_DATE <= TO_DATE('").append(bean.getPendTime()).append("', 'YYYY-MM-DD HH24:MI:SS')\n");
		}
		if(Utility.testString(bean.getBbeginTime())){
			sql.append("   AND PURCHASED_DATE >= TO_DATE('").append(bean.getBbeginTime()).append("', 'YYYY-MM-DD HH24:MI:SS')\n");
		}
		if(Utility.testString(bean.getBendTime())){
			sql.append("   AND PURCHASED_DATE <= TO_DATE('").append(bean.getBendTime()).append("', 'YYYY-MM-DD HH24:MI:SS')\n");
		}
		sql.append("     ) C,\n" );
		sql.append("     (\n" );
		sql.append("       SELECT GROUP_ID, GROUP_CODE,GROUP_NAME\n" );
		sql.append("       FROM TM_VHCL_MATERIAL_GROUP\n" );
		sql.append("       WHERE GROUP_LEVEL = 3\n" );
		if(Utility.testString(bean.getGroupCode())){
			sql.append("   AND GROUP_CODE IN (").append(bean.getGroupCode()).append(")");
		}
		sql.append("      ) D\n" );
		sql.append("WHERE A.VIN = C.VIN\n" );
		sql.append("AND A.ID = B.ID\n" );
		sql.append("AND C.MODEL_ID = D.GROUP_ID\n" );
		sql.append("GROUP BY D.GROUP_CODE, B.PART_CODE, B.PART_NAME ) A\n");
		List<Map<String, Object>> list = pageQuery(sql.toString(), null, getFunName());
		return list;
	}
	
	public PageResult<Map<String, Object>> queryPreserveVehicleReport04(StandardVipApplyManagerBean bean,int curPage, int pageSize){
		StringBuffer sql= new StringBuffer();
		sql.append("select distinct vin from (SELECT  VIN,dealer_id FROM (select yw.vin,yw.dealer_id from tt_as_wr_application_TOPUP yw  \n");
		if(bean.getBeginTime()!=null){
			sql.append("  where trunc(yw.create_date) >=to_date('"+bean.getBeginTime()+"', 'YYYY-MM-DD HH24:MI:SS')\n" );
		}
		if(bean.getEndTime()!=null){
			sql.append("  and trunc(yw.create_date) <=to_date('"+bean.getEndTime()+"', 'YYYY-MM-DD HH24:MI:SS')\n" );
		}
        
		if(bean.getDealerCode()!=null&&!bean.getDealerCode().equals("")){

			sql.append("and yw.dealer_id ="+bean.getDealerId()+"");

		}
		if(bean.getModelName()!=null){
			sql.append(" and yw.model_name='"+bean.getModelName()+"' ");
		}
		
		if(bean.getRepairType()!=null){
			sql.append("  and yw.claim_type in('"+bean.getRepairType()+"')\n" );
		}
        
		if(bean.getSeriesName()!=null){
			sql.append("  and yw.series_CODE='"+bean.getSeriesName()+"'\n" );
		}
		if(Integer.valueOf(bean.getFreeTims().toString())==2){
			sql.append(" and yw.claim_type!='10661002' ");
		}
			sql.append(" and yw.claim_type!='10661006' ");
		sql.append("        union all\n" );
		sql.append("        SELECT a.vin, a.dealer_id\n" );
		sql.append("          FROM (select g.group_name, wa.vin, wa.dealer_id\n" );
		sql.append("                  from tt_as_wr_application wa, tm_vhcl_material_group g,tm_dealer d\n" );
		sql.append("                 where trunc(wa.auditing_date) >=\n" );
		
		try {
			Date begin = Utility.getDate(bean.getBeginTime(), 1) ;
			Date begin1 = Utility.getDate("2010-10-05", 1) ;
			if(begin1.before(begin)){
				sql.append("                       to_date('"+bean.getBeginTime()+"', 'YYYY-MM-DD HH24:MI:SS')\n" );
			}
			else{
				sql.append("                       to_date('2010-10-05', 'YYYY-MM-DD')\n" );
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		sql.append("                   and trunc(wa.auditing_date) <=\n" );
		if(bean.getEndTime()!=null){
			sql.append("                       to_date('"+bean.getEndTime()+"', 'YYYY-MM-DD HH24:MI:SS')\n" );
		}
		if(bean.getRepairType()!=null){
			sql.append("  and WA.claim_type in('"+bean.getRepairType()+"')\n" );
		}
		sql.append("                   and wa.status in (10791004, 10791007, 10791008)\n" );
		sql.append("                   and g.group_code = wa.series_code\n" );
		sql.append("				  and d.dealer_id = wa.dealer_id ");
	    sql.append(" and wa.claim_type!=10661006 ");
		if(bean.getDealerCode()!=null&&!bean.getDealerCode().equals("")){
		sql.append("                  and d.dealer_id="+bean.getDealerId()+"  \n" );
		}
		if(bean.getSeriesName()!=null){
			sql.append("  and wa.series_code='"+bean.getSeriesName()+"'\n" );
		}
		if(bean.getModelName()!=null){
			sql.append(" and wa.model_name='"+bean.getModelName()+"' ");
		}
		if(Integer.valueOf(bean.getFreeTims().toString())==2){
			sql.append(" and wa.claim_type!='10661002' ");
		}
		sql.append(" and wa.claim_type!='10661006' ");
		sql.append("                    ) A,\n" );
		sql.append("               TM_DEALER D\n" );
		sql.append(" WHERE A.dealer_id = d.dealer_ID))\n" );
		if(bean.getAreaName()!=null){
			sql.append(" where dealer_id in ( ");
			sql.append("select r.dealer_id from TM_DEALER_ORG_RELATION r where r.org_id="+bean.getAreaName()+")\n" );
			

		}
		PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
		return ps;
	}
	
	public PageResult<Map<String, Object>> queryPreserveVehicleReport02(StandardVipApplyManagerBean bean,int curPage, int pageSize){
		StringBuffer sql= new StringBuffer();
		 sql.append("        SELECT   distinct wa.VIN\n");  
		 sql.append("          FROM   TT_AS_WR_APPLICATION WA\n");  
		 sql.append("         WHERE   WA.AUDITING_DATE >=\n");  
		 sql.append("                    TO_DATE ('"+bean.getBeginTime()+"',\n");  
		 sql.append("                             'YYYY-MM-DD HH24:MI:SS')\n");  
		 sql.append("                 AND WA.AUDITING_DATE <=\n");  
		 sql.append("                       TO_DATE ('"+bean.getEndTime()+"',\n");  
		 sql.append("                                'YYYY-MM-DD HH24:MI:SS')\n");  
		 sql.append("                 AND WA.STATUS IN (10791004, 10791007, 10791008)\n"); 
		
		 if(bean.getAreaName()!=null){
			 sql.append("         and     exists ( select r.dealer_id from vw_org_dealer_service r where r.root_org_id="+bean.getAreaName()+" and r.dealer_id=wa.dealer_id)\n"); 
		 }
		   
		 if(bean.getDealerId()!=null&&!bean.getDealerId().equals("")){
			 sql.append("         and wa.dealer_id="+bean.getDealerId()+"\n");
		 }
		   
		 sql.append("         and  wa.claim_type in ('10661001',\n");  
		 sql.append("                        '10661003',\n");  
		 sql.append("                        '10661002',\n");  
		 sql.append("                        '10661004',\n");  
		 sql.append("                        '10661005',\n");  
		 sql.append("                        '10661007',\n");  
		 sql.append("                        '10661008',\n");  
		 sql.append("                        '10661009')\n");  
		 if(bean.getSeriesName()!=null){
			 sql.append("             and  series_code='"+bean.getSeriesName()+"'\n");  
		 }
		 if(bean.getRepairType()!=null){
			 sql.append("             and claim_type="+bean.getRepairType()+"\n");
		 }
		 if(Integer.valueOf(bean.getFreeTims().toString())==2){
			 sql.append("         and  claim_type in ('10661001',\n");  
			 sql.append("                        '10661003',\n");  
			 sql.append("                        '10661004',\n");  
			 sql.append("                        '10661005',\n");  
			 sql.append("                        '10661007',\n");  
			 sql.append("                        '10661008',\n");  
			 sql.append("                        '10661009')\n");  
			}
			if(bean.getModelName()!=null){
				sql.append(" and model_code in  ( ");
				sql.append(" select vm.group_code from tm_vhcl_material_group vm where vm.group_id in (");
				sql.append("  select model_id from Tt_As_Wr_Model_Item  m where m.wrgroup_id in (select l.wrgroup_id from Tt_As_Wr_Model_Group l where l.wrgroup_name='"+bean.getModelName()+"') ) ");
				sql.append(" )");	
			}
		PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
		return ps;
	}
	
	
	public List<Map<String, Object>>  queryPreserveVehicleReport04(StandardVipApplyManagerBean bean){
		StringBuffer sql= new StringBuffer();
		 sql.append("        SELECT   distinct wa.VIN\n");  
		 sql.append("          FROM   TT_AS_WR_APPLICATION WA\n");  
		 sql.append("         WHERE   WA.AUDITING_DATE >=\n");  
		 sql.append("                    TO_DATE ('"+bean.getBeginTime()+"',\n");  
		 sql.append("                             'YYYY-MM-DD HH24:MI:SS')\n");  
		 sql.append("                 AND WA.AUDITING_DATE <=\n");  
		 sql.append("                       TO_DATE ('"+bean.getEndTime()+"',\n");  
		 sql.append("                                'YYYY-MM-DD HH24:MI:SS')\n");  
		 sql.append("                 AND WA.STATUS IN (10791004, 10791007, 10791008)\n"); 
		 if(bean.getAreaName()!=null){
			 sql.append("         and     exists ( select r.dealer_id from vw_org_dealer_service r where r.root_org_id="+bean.getAreaName()+" and r.dealer_id=wa.dealer_id)\n"); 
		 }
		  
		 if(bean.getDealerId()!=null&&!bean.getDealerId().equals("")){
			 sql.append("         and wa.dealer_id="+bean.getDealerId()+"\n");
		 }
		   
		 sql.append("         and  wa.claim_type in ('10661001',\n");  
		 sql.append("                        '10661003',\n");  
		 sql.append("                        '10661002',\n");  
		 sql.append("                        '10661004',\n");  
		 sql.append("                        '10661005',\n");  
		 sql.append("                        '10661007',\n");  
		 sql.append("                        '10661008',\n");  
		 sql.append("                        '10661009')\n");  
		 if(bean.getSeriesName()!=null){
			 sql.append("             and  series_code='"+bean.getSeriesName()+"'\n");  
		 }
		 if(bean.getRepairType()!=null){
			 sql.append("             and claim_type="+bean.getRepairType()+"\n");
		 }
		 if(Integer.valueOf(bean.getFreeTims().toString())==2){
			 sql.append("         and  claim_type in ('10661001',\n");  
			 sql.append("                        '10661003',\n");  
			 sql.append("                        '10661004',\n");  
			 sql.append("                        '10661005',\n");  
			 sql.append("                        '10661007',\n");  
			 sql.append("                        '10661008',\n");  
			 sql.append("                        '10661009')\n");  
			}
			if(bean.getModelName()!=null){
				sql.append(" and model_code in  ( ");
				sql.append(" select vm.group_code from tm_vhcl_material_group vm where vm.group_id in (");
				sql.append("  select model_id from Tt_As_Wr_Model_Item  m where m.wrgroup_id in (select l.wrgroup_id from Tt_As_Wr_Model_Group l where l.wrgroup_name='"+bean.getModelName()+"') ) ");
				sql.append(" )");	
			}
		List<Map<String, Object>> list = pageQuery(sql.toString(), null, getFunName());
		return list;
	}
	
	
	
	public PageResult<Map<String, Object>> queryPreserveVehicleReport03(String province,StandardVipApplyManagerBean bean,int curPage, int pageSize){
	
		
		StringBuffer sql= new StringBuffer();
		sql.append("select ds.dealer_code,ds.dealer_name,ds.root_org_name,ds.region_name,count(1) CON\n" );
		sql.append("  from (select distinct vin,aa.dealer_id\n" );
		sql.append("          from tt_as_wr_application aa, tm_dealer t\n" );
		sql.append("         where t.dealer_id = aa.dealer_id\n" );
		if(bean.getBeginTime()!=null){
		sql.append("           and aa.Auditing_Date >=\n" );
		sql.append("               to_date('"+bean.getBeginTime()+"', 'YYYY-MM-DD HH24:MI:SS')\n" );
		}
		if(bean.getEndTime()!=null){
		sql.append("           and aa.Auditing_Date <=\n" );
		sql.append("               to_date('"+bean.getEndTime()+"', 'YYYY-MM-DD HH24:MI:SS')\n" );
		}
		if(bean.getDealerCode()!=null){
			sql.append("and t.dealer_code like '%"+bean.getDealerCode()+"%'\n" );
		}
		
		if(Integer.valueOf(bean.getFreeTims().toString())==2){
			sql.append(" and aa.claim_type!='"+Constant.CLA_TYPE_02+"'");
		}
		if(Integer.valueOf(bean.getFreeTims().toString())==1){
			sql.append(" and aa.claim_type='"+Constant.CLA_TYPE_02+"'");
		}
		if(bean.getRepairType()!=null){
			sql.append("  and aa.claim_type = '"+bean.getRepairType()+"'\n" );
		}
		sql.append("AND AA.Status IN (10791004, 10791007, 10791008) \n");
		if(Utility.testString(province)){
			sql.append("  and t.province_id='"+province+"'\n" );
		}
		if(bean.getSeriesName()!=null){
			sql.append("  and aa.series_code='"+bean.getSeriesName()+"'\n" );
		}
		if(bean.getModelName()!=null){
			sql.append("  and aa.Model_Code in (select vm.group_code from tm_vhcl_material_group vm  where vm.group_id in (select model_id from Tt_As_Wr_Model_Item m where m.wrgroup_id in (select l.wrgroup_id from Tt_As_Wr_Model_Group l where l.wrgroup_name = '"+bean.getModelName()+"'))) ");
		}
		sql.append(") a,\n" );
		sql.append("vw_org_dealer_service ds where 1=1\n" );
		if(bean.getDealerCode()!=null){
		sql.append("and ds.dealer_code='"+bean.getDealerCode()+"'\n" );
		}
		sql.append(" and a.dealer_id = ds.dealer_id\n" );
		if(bean.getAreaName()!=null){
			sql.append("and ds.root_org_id="+bean.getAreaName()+"\n" );
		}
		sql.append(" group by ds.dealer_code,ds.dealer_name,ds.root_org_name,ds.region_name");

		
		System.out.println("......"+sql.toString());
		
		PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
		return ps;
	}
	
	
	
	public PageResult<Map<String, Object>> queryPreserveVehicleReport(StandardVipApplyManagerBean bean,int curPage, int pageSize){
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT ROWNUM NUM, A.VIN, A.DEALER_SHORTNAME\n" );
		sql.append("FROM(\n" );
		sql.append("    SELECT  distinct A.VIN, D.DEALER_SHORTNAME\n" );
		sql.append("    FROM\n" );
		sql.append("        (\n" );
		sql.append("           SELECT DEALER_CODE, VIN\n" );
		sql.append("           FROM TT_AS_REPAIR_ORDER\n" );
		sql.append("   		   WHERE 1=1\n");
		if(Utility.testString(bean.getModelName())){
			sql.append("       AND MODEL IN(SELECT B.GROUP_CODE FROM TT_AS_WR_MODEL_ITEM A, TM_VHCL_MATERIAL_GROUP B\n" );
			sql.append("               WHERE A.MODEL_ID = B.GROUP_ID\n" );
			sql.append("               AND A.WRGROUP_ID = ").append(bean.getModelName()).append(")\n"); 
		}
		if(Utility.testString(bean.getSeriesName())){
			sql.append("       AND SERIES = '").append(bean.getSeriesName()).append("'\n");
		}
		if(Utility.testString(bean.getRepairType())){
			sql.append("	   AND REPAIR_TYPE_CODE = '").append(bean.getRepairType()).append("'\n");
		}else{
			sql.append("       AND REPAIR_TYPE_CODE IN ('").append(Constant.REPAIR_TYPE_01).append("','").append(Constant.REPAIR_TYPE_04).append("')\n");
		}
		if(!bean.getFreeTims().equals("0")){
			if(bean.getFreeTims().equals("1")){
				sql.append("   AND FREE_TIMES >= 1\n");
			}else{
				sql.append("   AND FREE_TIMES > 1\n");
			}
		}
		if(Utility.testString(bean.getBeginTime())){
			sql.append("       AND RO_CREATE_DATE >= TO_DATE('").append(bean.getBeginTime()).append("', 'YYYY-MM-DD HH24:MI:SS')\n");
		}
		if(Utility.testString(bean.getEndTime())){
			sql.append("       AND RO_CREATE_DATE <= TO_DATE('").append(bean.getEndTime()).append("', 'YYYY-MM-DD HH24:MI:SS')\n");
		}
		sql.append("        ) A,\n" );
		sql.append("       (\n" );
		sql.append("          SELECT DEALER_ID, DEALER_CODE, DEALER_SHORTNAME, PARENT_DEALER_D\n" );
		sql.append("          FROM TM_DEALER\n" );
		sql.append("   		  WHERE DEALER_TYPE = ").append(Constant.DEALER_TYPE_DWR).append("\n");
		sql.append("		  AND DEALER_LEVEL = ").append(Constant.DEALER_LEVEL_01).append("\n");
		if(Utility.testString(bean.getDealerCode())){
			sql.append("      AND DEALER_CODE IN (").append(bean.getDealerCode()).append(")\n"); 
		}
		sql.append("        ) D,\n" );
		sql.append("       (\n" );
		sql.append("          SELECT DEALER_ID\n" );
		sql.append("          FROM TM_ORG A, TM_DEALER_ORG_RELATION B\n" );
		sql.append("          WHERE A.ORG_ID = B.ORG_ID\n" );
		if(Utility.testString(bean.getAreaName())){
			sql.append("	  AND B.ORG_ID = ").append(bean.getAreaName()).append("\n");
		}
		sql.append("        ) C\n" );
		sql.append("    WHERE A.DEALER_CODE = D.DEALER_CODE\n" );
		sql.append("    AND D.DEALER_ID = C.DEALER_ID\n" );
		
		sql.append("UNION ALL\n");
		
		sql.append("    SELECT   A.VIN, K.DEALER_SHORTNAME\n" );
		sql.append("    FROM\n" );
		sql.append("        (\n" );
		sql.append("           SELECT DEALER_CODE, VIN\n" );
		sql.append("           FROM TT_AS_REPAIR_ORDER\n" );
		sql.append("   		   WHERE 1=1\n");
		if(Utility.testString(bean.getModelName())){
			sql.append("       AND MODEL IN(SELECT B.GROUP_CODE FROM TT_AS_WR_MODEL_ITEM A, TM_VHCL_MATERIAL_GROUP B\n" );
			sql.append("               WHERE A.MODEL_ID = B.GROUP_ID\n" );
			sql.append("               AND A.WRGROUP_ID = ").append(bean.getModelName()).append(")\n"); 
		}
		if(Utility.testString(bean.getSeriesName())){
			sql.append("       AND SERIES = '").append(bean.getSeriesName()).append("'\n");
		}
		if(Utility.testString(bean.getRepairType())){
			sql.append("	   AND REPAIR_TYPE_CODE = '").append(bean.getRepairType()).append("'\n");
		}else{
			sql.append("       AND REPAIR_TYPE_CODE IN ('").append(Constant.REPAIR_TYPE_01).append("','").append(Constant.REPAIR_TYPE_04).append("')\n");
		}
		if(!bean.getFreeTims().equals("0")){
			if(bean.getFreeTims().equals("1")){
				sql.append("   AND FREE_TIMES >= 1\n");
			}else{
				sql.append("   AND FREE_TIMES > 1\n");
			}
		}
		if(Utility.testString(bean.getBeginTime())){
			sql.append("       AND RO_CREATE_DATE >= TO_DATE('").append(bean.getBeginTime()).append("', 'YYYY-MM-DD HH24:MI:SS')\n");
		}
		if(Utility.testString(bean.getEndTime())){
			sql.append("       AND RO_CREATE_DATE <= TO_DATE('").append(bean.getEndTime()).append("', 'YYYY-MM-DD HH24:MI:SS')\n");
		}
		sql.append("        ) A,\n" );
		sql.append("       (\n" );
		sql.append("          SELECT DEALER_ID, DEALER_CODE, DEALER_SHORTNAME, PARENT_DEALER_D\n" );
		sql.append("          FROM TM_DEALER\n" );
		sql.append("   		  WHERE DEALER_TYPE = ").append(Constant.DEALER_TYPE_DWR).append("\n");
		sql.append("		  AND DEALER_LEVEL = ").append(Constant.DEALER_LEVEL_02).append("\n");
		if(Utility.testString(bean.getDealerCode())){
			sql.append("      AND DEALER_CODE IN (").append(bean.getDealerCode()).append(")\n"); 
		}
		sql.append("        ) D,\n" );
		sql.append("       (\n" );
		sql.append("          SELECT DEALER_ID, ORG_NAME\n" );
		sql.append("          FROM TM_ORG A, TM_DEALER_ORG_RELATION B\n" );
		sql.append("          WHERE A.ORG_ID = B.ORG_ID\n" );
		if(Utility.testString(bean.getAreaName())){
			sql.append("	  AND B.ORG_ID = ").append(bean.getAreaName()).append("\n");
		}
		sql.append("        ) C, TM_DEALER K\n" );
		sql.append("    WHERE A.DEALER_CODE = D.DEALER_CODE\n" );
		sql.append("    AND D.PARENT_DEALER_D = K.DEALER_ID\n");
		sql.append("    AND K.DEALER_ID = C.DEALER_ID\n" );
		sql.append(") A  \n" );

		PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
		return ps;
	}
	
	public PageResult<Map<String, Object>> queryActivityCostReport(StandardVipApplyManagerBean bean,int curPage, int pageSize){
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT ROWNUM NUM, A.DEALER_CODE,A.DEALER_SHORTNAME, A.ORG_NAME,A.CAMPAIGN_CODE,A.ACTIVITY_NAME, A.CAMPAIGN_FEE\n");
		sql.append("FROM(");
		sql.append("SELECT B.DEALER_CODE,B.DEALER_SHORTNAME, E.ORG_NAME,A.CAMPAIGN_CODE,C.ACTIVITY_NAME, A.CAMPAIGN_FEE\n" );
		sql.append("FROM TT_AS_WR_APPLICATION A, TM_DEALER B, TM_ORG E, TM_DEALER_ORG_RELATION D, TT_AS_ACTIVITY C\n" );
		sql.append("WHERE A.DEALER_ID = B.DEALER_ID\n" );
		sql.append("AND A.CAMPAIGN_CODE = C.ACTIVITY_CODE\n" );
		sql.append("AND B.DEALER_ID = D.DEALER_ID\n" );
		sql.append("AND D.ORG_ID = E.ORG_ID\n" );
		sql.append("AND A.CLAIM_TYPE = ").append(Constant.CLA_TYPE_06).append("\n");
		sql.append("AND A.STATUS IN (").append(Constant.CLAIM_APPLY_ORD_TYPE_02).append(",");
		sql.append(Constant.CLAIM_APPLY_ORD_TYPE_03).append(",");
		sql.append(Constant.CLAIM_APPLY_ORD_TYPE_04).append(",");
		sql.append(Constant.CLAIM_APPLY_ORD_TYPE_07).append(",");
		sql.append(Constant.CLAIM_APPLY_ORD_TYPE_08).append(")\n");
		if(Utility.testString(bean.getDealerCode())){
			sql.append("AND B.DEALER_CODE IN (").append(bean.getDealerCode()).append(")\n");
		}
		if(Utility.testString(bean.getAreaName())){
			sql.append("AND D.ORG_ID = ").append(bean.getAreaName()).append("\n");
		}
		if(Utility.testString(bean.getCampaignCode())){
			sql.append("AND C.ACTIVITY_CODE = '").append(bean.getCampaignCode()).append("'\n");
		}
		if(Utility.testString(bean.getCampaignName())){
			sql.append("AND C.ACTIVITY_NAME = '").append(bean.getCampaignName()).append("'\n");
		}
		if(Utility.testString(bean.getBeginTime())){
			sql.append("AND REPORT_DATE >= TO_DATE('").append(bean.getBeginTime()).append("', 'YYYY-MM-DD HH24:MI:SS')\n");
		}
		if(Utility.testString(bean.getEndTime())){
			sql.append("AND REPORT_DATE <= TO_DATE('").append(bean.getEndTime()).append("', 'YYYY-MM-DD HH24:MI:SS')\n");
		}
		
		sql.append("UNION ALL\n");
		
		sql.append("SELECT B.DEALER_CODE,B.DEALER_SHORTNAME, E.ORG_NAME,A.CAMPAIGN_CODE,C.ACTIVITY_NAME, A.CAMPAIGN_FEE\n" );
		sql.append("FROM TT_AS_WR_APPLICATION A, TM_DEALER B, TM_DEALER F, TM_ORG E, TM_DEALER_ORG_RELATION D, TT_AS_ACTIVITY C\n" );
		sql.append("WHERE A.DEALER_ID = B.DEALER_ID\n" );
		sql.append("AND B.PARENT_DEALER_D = F.DEALER_ID\n");
		sql.append("AND A.CAMPAIGN_CODE = C.ACTIVITY_CODE\n" );
		sql.append("AND F.DEALER_ID = D.DEALER_ID\n" );
		sql.append("AND D.ORG_ID = E.ORG_ID\n" );
		sql.append("AND A.CLAIM_TYPE = ").append(Constant.CLA_TYPE_06).append("\n");
		sql.append("AND A.STATUS IN (").append(Constant.CLAIM_APPLY_ORD_TYPE_02).append(",");
		sql.append(Constant.CLAIM_APPLY_ORD_TYPE_03).append(",");
		sql.append(Constant.CLAIM_APPLY_ORD_TYPE_04).append(",");
		sql.append(Constant.CLAIM_APPLY_ORD_TYPE_07).append(",");
		sql.append(Constant.CLAIM_APPLY_ORD_TYPE_08).append(")\n");
		if(Utility.testString(bean.getDealerCode())){
			sql.append("AND B.DEALER_CODE IN (").append(bean.getDealerCode()).append(")\n");
		}
		if(Utility.testString(bean.getAreaName())){
			sql.append("AND D.ORG_ID = ").append(bean.getAreaName()).append("\n");
		}
		if(Utility.testString(bean.getCampaignCode())){
			sql.append("AND C.ACTIVITY_CODE = '").append(bean.getCampaignCode()).append("'\n");
		}
		if(Utility.testString(bean.getCampaignName())){
			sql.append("AND C.ACTIVITY_NAME = '").append(bean.getCampaignName()).append("'\n");
		}
		if(Utility.testString(bean.getBeginTime())){
			sql.append("AND REPORT_DATE >= TO_DATE('").append(bean.getBeginTime()).append("', 'YYYY-MM-DD HH24:MI:SS')\n");
		}
		if(Utility.testString(bean.getEndTime())){
			sql.append("AND REPORT_DATE <= TO_DATE('").append(bean.getEndTime()).append("', 'YYYY-MM-DD HH24:MI:SS')\n");
		}
		sql.append(") A\n");
		PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
		return ps;
	}
	
	public List<Map<String, Object>> getActivityCostReportExcelList(StandardVipApplyManagerBean bean){
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT ROWNUM NUM, A.DEALER_CODE,A.DEALER_SHORTNAME, A.ORG_NAME,A.CAMPAIGN_CODE,A.ACTIVITY_NAME, A.CAMPAIGN_FEE\n");
		sql.append("FROM(");
		sql.append("SELECT B.DEALER_CODE,B.DEALER_SHORTNAME, E.ORG_NAME,A.CAMPAIGN_CODE,C.ACTIVITY_NAME, A.CAMPAIGN_FEE\n" );
		sql.append("FROM TT_AS_WR_APPLICATION A, TM_DEALER B, TM_ORG E, TM_DEALER_ORG_RELATION D, TT_AS_ACTIVITY C\n" );
		sql.append("WHERE A.DEALER_ID = B.DEALER_ID\n" );
		sql.append("AND A.CAMPAIGN_CODE = C.ACTIVITY_CODE\n" );
		sql.append("AND B.DEALER_ID = D.DEALER_ID\n" );
		sql.append("AND D.ORG_ID = E.ORG_ID\n" );
		sql.append("AND A.CLAIM_TYPE = ").append(Constant.CLA_TYPE_06).append("\n");
		sql.append("AND A.STATUS IN (").append(Constant.CLAIM_APPLY_ORD_TYPE_02).append(",");
		sql.append(Constant.CLAIM_APPLY_ORD_TYPE_03).append(",");
		sql.append(Constant.CLAIM_APPLY_ORD_TYPE_04).append(",");
		sql.append(Constant.CLAIM_APPLY_ORD_TYPE_07).append(",");
		sql.append(Constant.CLAIM_APPLY_ORD_TYPE_08).append(")\n");
		if(Utility.testString(bean.getDealerCode())){
			sql.append("AND B.DEALER_CODE IN (").append(bean.getDealerCode()).append(")\n");
		}
		if(Utility.testString(bean.getAreaName())){
			sql.append("AND D.ORG_ID = ").append(bean.getAreaName()).append("\n");
		}
		if(Utility.testString(bean.getCampaignCode())){
			sql.append("AND C.ACTIVITY_CODE = '").append(bean.getCampaignCode()).append("'\n");
		}
		if(Utility.testString(bean.getCampaignName())){
			sql.append("AND C.ACTIVITY_NAME = '").append(bean.getCampaignName()).append("'\n");
		}
		if(Utility.testString(bean.getBeginTime())){
			sql.append("AND REPORT_DATE >= TO_DATE('").append(bean.getBeginTime()).append("', 'YYYY-MM-DD HH24:MI:SS')\n");
		}
		if(Utility.testString(bean.getEndTime())){
			sql.append("AND REPORT_DATE <= TO_DATE('").append(bean.getEndTime()).append("', 'YYYY-MM-DD HH24:MI:SS')\n");
		}
		
		sql.append("UNION ALL\n");
		
		sql.append("SELECT B.DEALER_CODE,B.DEALER_SHORTNAME, E.ORG_NAME,A.CAMPAIGN_CODE,C.ACTIVITY_NAME, A.CAMPAIGN_FEE\n" );
		sql.append("FROM TT_AS_WR_APPLICATION A, TM_DEALER B, TM_DEALER F, TM_ORG E, TM_DEALER_ORG_RELATION D, TT_AS_ACTIVITY C\n" );
		sql.append("WHERE A.DEALER_ID = B.DEALER_ID\n" );
		sql.append("AND B.PARENT_DEALER_D = F.DEALER_ID\n");
		sql.append("AND A.CAMPAIGN_CODE = C.ACTIVITY_CODE\n" );
		sql.append("AND F.DEALER_ID = D.DEALER_ID\n" );
		sql.append("AND D.ORG_ID = E.ORG_ID\n" );
		sql.append("AND A.CLAIM_TYPE = ").append(Constant.CLA_TYPE_06).append("\n");
		sql.append("AND A.STATUS IN (").append(Constant.CLAIM_APPLY_ORD_TYPE_02).append(",");
		sql.append(Constant.CLAIM_APPLY_ORD_TYPE_03).append(",");
		sql.append(Constant.CLAIM_APPLY_ORD_TYPE_04).append(",");
		sql.append(Constant.CLAIM_APPLY_ORD_TYPE_07).append(",");
		sql.append(Constant.CLAIM_APPLY_ORD_TYPE_08).append(")\n");
		if(Utility.testString(bean.getDealerCode())){
			sql.append("AND B.DEALER_CODE IN (").append(bean.getDealerCode()).append(")\n");
		}
		if(Utility.testString(bean.getAreaName())){
			sql.append("AND D.ORG_ID = ").append(bean.getAreaName()).append("\n");
		}
		if(Utility.testString(bean.getCampaignCode())){
			sql.append("AND C.ACTIVITY_CODE = '").append(bean.getCampaignCode()).append("'\n");
		}
		if(Utility.testString(bean.getCampaignName())){
			sql.append("AND C.ACTIVITY_NAME = '").append(bean.getCampaignName()).append("'\n");
		}
		if(Utility.testString(bean.getBeginTime())){
			sql.append("AND REPORT_DATE >= TO_DATE('").append(bean.getBeginTime()).append("', 'YYYY-MM-DD HH24:MI:SS')\n");
		}
		if(Utility.testString(bean.getEndTime())){
			sql.append("AND REPORT_DATE <= TO_DATE('").append(bean.getEndTime()).append("', 'YYYY-MM-DD HH24:MI:SS')\n");
		}
		sql.append(") A\n");
		List<Map<String, Object>> list = pageQuery(sql.toString(), null, getFunName());
		return list;
	}
	
	public List<Map<String, Object>> getPreserveVehicleExcelList02(String province,StandardVipApplyManagerBean bean){
		StringBuffer sql= new StringBuffer();
		sql.append("select ds.dealer_code,ds.dealer_name,ds.root_org_name,ds.region_name,count(1) CON\n" );
		sql.append("  from (select distinct vin,aa.dealer_id\n" );
		sql.append("          from tt_as_wr_application aa, tm_dealer t\n" );
		sql.append("         where t.dealer_id = aa.dealer_id\n" );
		if(bean.getBeginTime()!=null){
		sql.append("           and aa.Auditing_Date >=\n" );
		sql.append("               to_date('"+bean.getBeginTime()+"', 'YYYY-MM-DD HH24:MI:SS')\n" );
		}
		if(bean.getEndTime()!=null){
		sql.append("           and aa.Auditing_Date <=\n" );
		sql.append("               to_date('"+bean.getEndTime()+"', 'YYYY-MM-DD HH24:MI:SS')\n" );
		}
		if(bean.getDealerCode()!=null&&!bean.getDealerCode().equals("")){
			sql.append("and t.dealer_code like '%"+bean.getDealerCode()+"%'\n" );
		}
		
		if(Integer.valueOf(bean.getFreeTims().toString())==2){
			sql.append(" and aa.claim_type!='"+Constant.CLA_TYPE_02+"'");
		}
		if(Integer.valueOf(bean.getFreeTims().toString())==1){
			sql.append(" and aa.claim_type='"+Constant.CLA_TYPE_02+"'");
		}
		if(bean.getRepairType()!=null){
			sql.append("  and aa.claim_type = '"+bean.getRepairType()+"'\n" );
		}
		sql.append("AND AA.Status IN (10791004, 10791007, 10791008) \n");
		if(Utility.testString(province)){
			sql.append("  and t.province_id='"+province+"'\n" );
		}
		if(bean.getSeriesName()!=null){
			sql.append("  and aa.series_code='"+bean.getSeriesName()+"'\n" );
		}
		if(bean.getModelName()!=null){
			sql.append("  and aa.Model_Code in (select vm.group_code from tm_vhcl_material_group vm  where vm.group_id in (select model_id from Tt_As_Wr_Model_Item m where m.wrgroup_id in (select l.wrgroup_id from Tt_As_Wr_Model_Group l where l.wrgroup_name = '"+bean.getModelName()+"'))) ");
		}
		sql.append(") a,\n" );
		sql.append("vw_org_dealer_service ds where 1=1 \n" );
		if(bean.getDealerCode()!=null&&!bean.getDealerCode().equals("")){
		sql.append(" and ds.dealer_code='"+bean.getDealerCode()+"'\n" );
		}
		sql.append(" and a.dealer_id = ds.dealer_id\n" );
		if(bean.getAreaName()!=null){
			sql.append("and ds.root_org_id="+bean.getAreaName()+"\n" );
		}
		sql.append(" group by ds.dealer_code,ds.dealer_name,ds.root_org_name,ds.region_name");

		
		System.out.println("......"+sql.toString());
		
		List<Map<String, Object>> list = pageQuery(sql.toString(), null, getFunName());
		return list;
	}
	
	public List<Map<String, Object>> getExcelList(StandardVipApplyManagerBean bean){
		StringBuffer sql= new StringBuffer();
		if(bean.getFeeType()==null||bean.getFeeType().equals("")){
			sql.append("select d.dealer_name,\n" );
			sql.append("       d.dealer_code,\n" );
			sql.append("       d.root_org_name,\n" );
			sql.append("       c.code_desc,\n" );
			sql.append("       s.fee_no,\n" );
			sql.append("       s.declare_sum1,s.declare_sum,\n" );
			sql.append("       s.vin,\n" );
			sql.append("       case\n" );
			sql.append("       when s.vin is not null then '1'\n" );
			sql.append("       else '0' end as cou,\n" );
			sql.append("       mg.group_code,\n" );
			sql.append("       mg1.group_name,s.declare_sum1 as average,s.declare_sum as average1\n" );
			sql.append("  from Tt_As_Wr_Spefee        s,\n" );
			sql.append("       vw_org_dealer_service  d,\n" );
			sql.append("       tc_code                c,\n" );
			sql.append("       tm_vehicle             v,\n" );
			sql.append("       tm_vhcl_material_group mg,\n" );
			sql.append("       tm_vhcl_material_group mg1\n" );
			sql.append(" where s.fee_type = "+Constant.FEE_TYPE_01+"\n" );
			sql.append("   and s.status in ("+Constant.SPEFEE_STATUS_02+","+Constant.SPEFEE_STATUS_04+", "+Constant.SPEFEE_STATUS_06+","+Constant.SPEFEE_STATUS_07+")\n" );
			sql.append("   and d.dealer_id = s.dealer_id\n" );
			sql.append("   and s.fee_type = c.code_id\n" );
			sql.append("   and v.model_id = mg.group_id(+)\n" );
			sql.append("   and v.series_id = mg1.group_id(+)\n" );
			if(Utility.testString(bean.getBeginTime())){
				sql.append("     and s.make_date>=to_date('"+bean.getBeginTime()+"','YYYY-MM-DD HH24:MI:SS')  \n");	
			}  
			if(Utility.testString(bean.getBeginTime())){
				sql.append("     and s.make_date<=to_date('"+bean.getEndTime()+"','YYYY-MM-DD HH24:MI:SS')  \n");	
			}  
			sql.append("   and s.vin = v.vin(+)");
			if(Utility.testString(bean.getAreaName())){
				sql.append("  and d.root_org_id="+bean.getAreaName()+"  \n");	
			}
			//经销商查询条件
			if(Utility.testString(bean.getDealerCode())){
				sql.append("  and d.dealer_code="+bean.getDealerCode()+"  \n");	
			}
			//车系查询条件
			if(Utility.testString(bean.getSeriesName())){
				sql.append("   and v.series_id="+bean.getSeriesName()+"  \n");	
			}  
			
			sql.append("   and s.vin = v.vin(+)");
			sql.append(" union all \n " );
			sql.append("select d.dealer_name,\n" );
			sql.append("       d.dealer_code,\n" );
			sql.append("       d.root_org_name,\n" );
			sql.append("       c.code_desc,\n" );
			sql.append("       s.fee_no,\n" );
			sql.append("       s.declare_sum1,s.declare_sum,\n" );
			sql.append("       ps.vin,\n" );
			sql.append("       to_char((select count(1) from Tt_As_Wr_Spefee_Claim sc where sc.fee_id = s.id)) as cou,\n" );
			sql.append("       mg.group_code,\n" );
			sql.append("       mg1.group_name,s.declare_sum1/(select count(1) from Tt_As_Wr_Spefee_Claim sc where sc.fee_id = s.id) as  average,s.declare_sum/(select count(1) from Tt_As_Wr_Spefee_Claim sc where sc.fee_id = s.id) as  average1\n" );
			sql.append("  from Tt_As_Wr_Spefee        s,\n" );
			sql.append("       vw_org_dealer_service  d,\n" );
			sql.append("       tc_code                c,\n" );
			sql.append("       Tt_As_Wr_Spefee_Claim  ps,\n" );
			sql.append("       tm_vehicle             v,\n" );
			sql.append("       tm_vhcl_material_group mg,\n" );
			sql.append("       tm_vhcl_material_group mg1\n" );
			sql.append(" where s.fee_type = "+Constant.FEE_TYPE_02+"\n" );
			sql.append("   and s.status in ("+Constant.SPEFEE_STATUS_02+","+Constant.SPEFEE_STATUS_04+", "+Constant.SPEFEE_STATUS_06+","+Constant.SPEFEE_STATUS_07+")\n" );
			sql.append("   and d.dealer_id = s.dealer_id\n" );
			sql.append("   and s.fee_type = c.code_id\n" );
			sql.append("   and ps.fee_id = s.id\n" );
			sql.append("   and ps.vin = v.vin\n" );
			sql.append("   and v.model_id = mg.group_id\n" );
			sql.append("   and v.series_id = mg1.group_id\n" );
			//大区查询条件
			if(Utility.testString(bean.getAreaName())){
				sql.append("  and d.root_org_id="+bean.getAreaName()+"  \n");	
			}
			//经销商查询条件
			if(Utility.testString(bean.getDealerCode())){
				sql.append("  and d.dealer_code="+bean.getDealerCode()+"  \n");	
			}
			//车系查询条件
			if(Utility.testString(bean.getSeriesName())){
				sql.append("   and v.series_id="+bean.getSeriesName()+"  \n");	
			}  
			//起始时间查询条件
			if(Utility.testString(bean.getBeginTime())){
				
				sql.append("and exists (select sa.fee_id from Tt_As_Wr_Spefee_Auditing sa where sa.fee_id=s.id and sa.status=11841004\n" );
				sql.append("and  sa.create_date>=to_date('"+bean.getBeginTime()+"','YYYY-MM-DD HH24:MI:SS')\n" );
				sql.append("  and sa.create_date<=to_date('"+bean.getEndTime()+"','YYYY-MM-DD HH24:MI:SS')  )");

			}  
		
			//車型大類查詢條件
			if(Utility.testString(bean.getModelName())){
				sql.append("   and exists (select m.model_id from Tt_As_Wr_Model_Item m where m.wrgroup_id="+bean.getModelName()+" and m.model_id = v.model_id )  \n");	
			}  
		}else{
			if(bean.getFeeType().equals(Constant.FEE_TYPE_01)){
				sql.append("select d.dealer_name,\n" );
				sql.append("       d.dealer_code,\n" );
				sql.append("       d.root_org_name,\n" );
				sql.append("       c.code_desc,\n" );
				sql.append("       s.fee_no,\n" );
				sql.append("       s.declare_sum1,s.declare_sum,\n" );
				sql.append("       s.vin,\n" );
				sql.append("       case\n" );
				sql.append("       when s.vin is not null then '1'\n" );
				sql.append("       else '0' end as cou,\n" );
				sql.append("       mg.group_code,\n" );
				sql.append("       mg1.group_name,s.declare_sum1 as average,s.declare_sum as average1\n" );
				sql.append("  from Tt_As_Wr_Spefee        s,\n" );
				sql.append("       vw_org_dealer_service  d,\n" );
				sql.append("       tc_code                c,\n" );
				sql.append("       tm_vehicle             v,\n" );
				sql.append("       tm_vhcl_material_group mg,\n" );
				sql.append("       tm_vhcl_material_group mg1\n" );
				sql.append(" where s.fee_type = "+Constant.FEE_TYPE_01+"\n" );
				sql.append("   and s.status in ("+Constant.SPEFEE_STATUS_02+","+Constant.SPEFEE_STATUS_04+", "+Constant.SPEFEE_STATUS_06+","+Constant.SPEFEE_STATUS_07+")\n" );
				sql.append("   and d.dealer_id = s.dealer_id\n" );
				sql.append("   and s.fee_type = c.code_id\n" );
				sql.append("   and v.model_id = mg.group_id(+)\n" );
				sql.append("   and v.series_id = mg1.group_id(+)\n" );
				sql.append("   and s.vin = v.vin(+)");
				//大区查询条件
				if(Utility.testString(bean.getAreaName())){
					sql.append("  and d.root_org_id="+bean.getAreaName()+"  \n");	
				}
				//经销商查询条件
				if(Utility.testString(bean.getDealerCode())){
					sql.append("  and d.dealer_code="+bean.getDealerCode()+"  \n");	
				}
				//车系查询条件
				if(Utility.testString(bean.getSeriesName())){
					sql.append("   and v.series_id="+bean.getSeriesName()+"  \n");	
				} 
				//起始时间查询条件
				if(Utility.testString(bean.getBeginTime())){
					sql.append("     and s.make_date>=to_date('"+bean.getBeginTime()+"','YYYY-MM-DD HH24:MI:SS')  \n");	
				}  
				if(Utility.testString(bean.getBeginTime())){
					sql.append("     and s.make_date<=to_date('"+bean.getEndTime()+"','YYYY-MM-DD HH24:MI:SS')  \n");	
				}  
				//車型大類查詢條件
				if(Utility.testString(bean.getModelName())){
					sql.append("   and exists (select m.model_id from Tt_As_Wr_Model_Item m where m.wrgroup_id="+bean.getModelName()+" and m.model_id = v.model_id )  \n");	
				}   
			}else{
				sql.append("select d.dealer_name,\n" );
				sql.append("       d.dealer_code,\n" );
				sql.append("       d.root_org_name,\n" );
				sql.append("       c.code_desc,\n" );
				sql.append("       s.fee_no,\n" );
				sql.append("       s.declare_sum1,\n" );
				sql.append("       ps.vin,\n" );
				sql.append("       to_char((select count(1) from Tt_As_Wr_Spefee_Claim sc where sc.fee_id = s.id)) as cou,\n" );
				sql.append("       mg.group_code,\n" );
				sql.append("       mg1.group_name,s.declare_sum1/(select count(1) from Tt_As_Wr_Spefee_Claim sc where sc.fee_id = s.id) as  average,s.declare_sum/(select count(1) from Tt_As_Wr_Spefee_Claim sc where sc.fee_id = s.id) as  average1\n" );
				sql.append("  from Tt_As_Wr_Spefee        s,\n" );
				sql.append("       vw_org_dealer_service  d,\n" );
				sql.append("       tc_code                c,\n" );
				sql.append("       Tt_As_Wr_Spefee_Claim  ps,\n" );
				sql.append("       tm_vehicle             v,\n" );
				sql.append("       tm_vhcl_material_group mg,\n" );
				sql.append("       tm_vhcl_material_group mg1\n" );
				sql.append(" where s.fee_type = "+Constant.FEE_TYPE_02+"\n" );
				sql.append("   and s.status in ("+Constant.SPEFEE_STATUS_04+", "+Constant.SPEFEE_STATUS_06+","+Constant.SPEFEE_STATUS_07+")\n" );
				sql.append("   and d.dealer_id = s.dealer_id\n" );
				sql.append("   and s.fee_type = c.code_id\n" );
				sql.append("   and ps.fee_id = s.id\n" );
				sql.append("   and ps.vin = v.vin\n" );
				sql.append("   and v.model_id = mg.group_id\n" );
				sql.append("   and v.series_id = mg1.group_id\n" );
				//大区查询条件
				if(Utility.testString(bean.getAreaName())){
					sql.append("  and d.root_org_id="+bean.getAreaName()+"  \n");	
				}
				//经销商查询条件
				if(Utility.testString(bean.getDealerCode())){
					sql.append("  and d.dealer_code="+bean.getDealerCode()+"  \n");	
				}
				//车系查询条件
				if(Utility.testString(bean.getSeriesName())){
					sql.append("   and v.series_id="+bean.getSeriesName()+"  \n");	
				}  
				//起始时间查询条件
				if(Utility.testString(bean.getBeginTime())){
					
					sql.append("and exists (select sa.fee_id from Tt_As_Wr_Spefee_Auditing sa where sa.fee_id=s.id and sa.status=11841004\n" );
					sql.append("and  sa.create_date>=to_date('"+bean.getBeginTime()+"','YYYY-MM-DD HH24:MI:SS')\n" );
					sql.append("  and sa.create_date<=to_date('"+bean.getEndTime()+"','YYYY-MM-DD HH24:MI:SS')  )");

				}  
				//車型大類查詢條件
				if(Utility.testString(bean.getModelName())){
					sql.append("   and exists (select m.model_id from Tt_As_Wr_Model_Item m where m.wrgroup_id="+bean.getModelName()+" and m.model_id = v.model_id )  \n");	
				}  
			}
			}
		List<Map<String, Object>> list = pageQuery(sql.toString(), null, getFunName());
		return list;
	}
	
	public List<Map<String, Object>> getMainteranceVinExcelList(StandardVipApplyManagerBean bean){
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT ROWNUM NUM, A.DEALER_CODE, A.DEALER_SHORTNAME, A.ORG_NAME,\n" );
		sql.append("       A.VIN, A.DEALER_NAME1\n" );
		sql.append("FROM(\n" );
		sql.append("    SELECT D.DEALER_CODE, D.DEALER_SHORTNAME, D.DEALER_SHORTNAME DEALER_NAME1,C.ORG_NAME, A.VIN\n" );
		sql.append("    FROM\n" );
		sql.append("        (\n" );
		sql.append("           SELECT DEALER_CODE, VIN\n" );
		sql.append("           FROM TT_AS_REPAIR_ORDER\n" );
		sql.append("   		   WHERE REPAIR_TYPE_CODE = '").append(Constant.REPAIR_TYPE_04).append("'\n");
		if(Utility.testString(bean.getModelName())){
			sql.append("       AND MODEL IN(SELECT B.GROUP_CODE FROM TT_AS_WR_MODEL_ITEM A, TM_VHCL_MATERIAL_GROUP B\n" );
			sql.append("               WHERE A.MODEL_ID = B.GROUP_ID\n" );
			sql.append("               AND A.WRGROUP_ID = ").append(bean.getModelName()).append(")\n"); 
		}
		if(Utility.testString(bean.getSeriesName())){
			sql.append("       AND SERIES = '").append(bean.getSeriesName()).append("'\n");
		}
		if(Utility.testString(bean.getBeginTime())){
			sql.append("       AND RO_CREATE_DATE >= TO_DATE('").append(bean.getBeginTime()).append("', 'YYYY-MM-DD HH24:MI:SS')\n");
		}
		if(Utility.testString(bean.getEndTime())){
			sql.append("       AND RO_CREATE_DATE <= TO_DATE('").append(bean.getEndTime()).append("', 'YYYY-MM-DD HH24:MI:SS')\n");
		}
		sql.append("        ) A,\n" );
		sql.append("       (\n" );
		sql.append("          SELECT DEALER_ID, DEALER_CODE, DEALER_SHORTNAME, PARENT_DEALER_D\n" );
		sql.append("          FROM TM_DEALER\n" );
		sql.append("   		  WHERE DEALER_TYPE = ").append(Constant.DEALER_TYPE_DWR).append("\n");
		sql.append("		  AND DEALER_LEVEL = ").append(Constant.DEALER_LEVEL_01).append("\n");
		if(Utility.testString(bean.getDealerCode())){
			sql.append("      AND DEALER_CODE IN (").append(bean.getDealerCode()).append(")\n"); 
		}
		sql.append("        ) D,\n" );
		sql.append("       (\n" );
		sql.append("          SELECT DEALER_ID, ORG_NAME\n" );
		sql.append("          FROM TM_ORG A, TM_DEALER_ORG_RELATION B\n" );
		sql.append("          WHERE A.ORG_ID = B.ORG_ID\n" );
		if(Utility.testString(bean.getAreaName())){
			sql.append("	  AND B.ORG_ID = ").append(bean.getAreaName()).append("\n");
		}
		sql.append("        ) C\n" );
		sql.append("    WHERE A.DEALER_CODE = D.DEALER_CODE\n" );
		sql.append("    AND D.DEALER_ID = C.DEALER_ID\n" );
		sql.append("UNION ALL\n");
		sql.append("    SELECT D.DEALER_CODE, D.DEALER_SHORTNAME, K.DEALER_SHORTNAME DEALER_NAME1,C.ORG_NAME, A.VIN\n" );
		sql.append("    FROM\n" );
		sql.append("        (\n" );
		sql.append("           SELECT DEALER_CODE, VIN\n" );
		sql.append("           FROM TT_AS_REPAIR_ORDER\n" );
		sql.append("   		   WHERE REPAIR_TYPE_CODE = '").append(Constant.REPAIR_TYPE_04).append("'\n");
		if(Utility.testString(bean.getModelName())){
			sql.append("       AND MODEL IN(SELECT B.GROUP_CODE FROM TT_AS_WR_MODEL_ITEM A, TM_VHCL_MATERIAL_GROUP B\n" );
			sql.append("               WHERE A.MODEL_ID = B.GROUP_ID\n" );
			sql.append("               AND A.WRGROUP_ID = ").append(bean.getModelName()).append(")\n"); 
		}
		if(Utility.testString(bean.getSeriesName())){
			sql.append("       AND SERIES = '").append(bean.getSeriesName()).append("'\n");
		}
		if(Utility.testString(bean.getBeginTime())){
			sql.append("       AND RO_CREATE_DATE >= TO_DATE('").append(bean.getBeginTime()).append("', 'YYYY-MM-DD HH24:MI:SS')\n");
		}
		if(Utility.testString(bean.getEndTime())){
			sql.append("       AND RO_CREATE_DATE <= TO_DATE('").append(bean.getEndTime()).append("', 'YYYY-MM-DD HH24:MI:SS')\n");
		}
		sql.append("        ) A,\n" );
		sql.append("       (\n" );
		sql.append("          SELECT DEALER_ID, DEALER_CODE, DEALER_SHORTNAME, PARENT_DEALER_D\n" );
		sql.append("          FROM TM_DEALER\n" );
		sql.append("   		  WHERE DEALER_TYPE = ").append(Constant.DEALER_TYPE_DWR).append("\n");
		sql.append("		  AND DEALER_LEVEL = ").append(Constant.DEALER_LEVEL_02).append("\n");
		if(Utility.testString(bean.getDealerCode())){
			sql.append("      AND DEALER_CODE IN (").append(bean.getDealerCode()).append(")\n"); 
		}
		sql.append("        ) D,\n" );
		sql.append("       (\n" );
		sql.append("          SELECT DEALER_ID, ORG_NAME\n" );
		sql.append("          FROM TM_ORG A, TM_DEALER_ORG_RELATION B\n" );
		sql.append("          WHERE A.ORG_ID = B.ORG_ID\n" );
		if(Utility.testString(bean.getAreaName())){
			sql.append("	  AND B.ORG_ID = ").append(bean.getAreaName()).append("\n");
		}
		sql.append("        ) C, TM_DEALER K\n" );
		sql.append("    WHERE A.DEALER_CODE = D.DEALER_CODE\n" );
		sql.append("    AND D.PARENT_DEALER_D = K.DEALER_ID\n");
		sql.append("    AND K.DEALER_ID = C.DEALER_ID\n" );
		sql.append(") A \n" );

		List<Map<String, Object>> list = pageQuery(sql.toString(), null, getFunName());
		return list;
	}
	
	/**
	 * Iverson add By 2010-12-10
	 * 经销商索赔申报审核明细(分页)
	 */
	public PageResult<Map<String, Object>> queryDealerAppDetailDao(Map<String, String> map,int curPage, int pageSize){
		StringBuffer sql= new StringBuffer();
		sql.append("select ROWNUM NUM,a.ID,a.status,to_char(a.AUDITING_DATE,'yyyy-MM-dd') as AUDITING_DATE,a.yieldly as yieldly,-- 结算厂家,\n");
		sql.append("a.claim_no as claim_no,-- 索赔单号,\n");
		sql.append("a.CLAIM_TYPE as CLAIM_TYPE,-- 索赔类型,\n" );
		sql.append("nvl(a.part_amount,0) as part_amount,-- 配件金额,\n" );
		sql.append("nvl(a.labour_amount,0) as labour_amount,-- 申请工时金额,\n" );
		sql.append("nvl(a.apply_appendlabour_amount,0) AS APPLY_APPENDLABOUR_AMOUNT,-- 申请追加工时金额,\n" );
		sql.append("nvl(a.NETITEM_AMOUNT,0) as NETITEM_AMOUNT,-- 申请其他费用金额,\n" );
		sql.append("nvl(a.campaign_fee,0) as campaign_fee,-- 申请服务活动金额,\n" );
		sql.append("nvl(a.FREE_M_PRICE,0) as FREE_M_PRICE,-- 申请保养金额,\n" );
		sql.append("nvl(a.apply_append_amount,0) as APPLY_APPEND_AMPUNT,-- 申报特殊费用金额,\n" );
		sql.append("(nvl(a.part_amount,0)+nvl(a.labour_amount,0)+nvl(a.apply_appendlabour_amount,0)+nvl(a.NETITEM_AMOUNT,0)+nvl(a.campaign_fee,0)+nvl(a.FREE_M_PRICE,0)+nvl(a.apply_append_amount,0)) as claimtotal,-- 申请索赔单总金额,\n" );
		
		sql.append("(case when a.status='"+Constant.CLAIM_APPLY_ORD_TYPE_07+"' then nvl(a.balance_part_amount,0) else 0 end) as balance_part_amount,-- 结算配件金额,\n" );
		sql.append("(case when a.status='"+Constant.CLAIM_APPLY_ORD_TYPE_07+"' then nvl(a.balance_labour_amount,0) else 0 end) as balance_labour_amount,--  结算工时金额,\n");
		sql.append("(case when a.status='"+Constant.CLAIM_APPLY_ORD_TYPE_07+"' then nvl(a.appendlabour_amount,0) else 0 end) as APPENDLABOUR_AMOUNT,--  结算追加工时费,\n");
		sql.append("(case when a.status='"+Constant.CLAIM_APPLY_ORD_TYPE_07+"' then nvl(a.balance_netitem_amount,0) else 0 end) as balance_netitem_amount,--  结算其他费用金额,\n");
		sql.append("(case when a.status='"+Constant.CLAIM_APPLY_ORD_TYPE_07+"' then nvl(a.append_amount,0) else 0 end) as APPEND_AMOUNT,--  结算特殊费用金额,\n");
		sql.append("((case when a.status='"+Constant.CLAIM_APPLY_ORD_TYPE_07+"' then nvl(a.balance_part_amount,0) else 0 end)+(case when a.status='"+Constant.CLAIM_APPLY_ORD_TYPE_07+"' then nvl(a.balance_labour_amount,0) else 0 end)+(case when a.status='"+Constant.CLAIM_APPLY_ORD_TYPE_07+"' then nvl(a.appendlabour_amount,0) else 0 end)+(case when a.status='"+Constant.CLAIM_APPLY_ORD_TYPE_07+"' then nvl(a.balance_netitem_amount,0) else 0 end)+(case when a.status='"+Constant.CLAIM_APPLY_ORD_TYPE_07+"' then nvl(a.APPEND_amount,0) else 0 end)+(case when a.status='"+Constant.CLAIM_APPLY_ORD_TYPE_07+"' then nvl(a.campaign_fee,0) else 0 end)+(case when a.status='"+Constant.CLAIM_APPLY_ORD_TYPE_07+"' then nvl(a.FREE_M_PRICE,0)else 0 end)) as balancetotal,-- 结算索赔单总金额,\n");
		
		sql.append("((case when a.status='"+Constant.CLAIM_APPLY_ORD_TYPE_07+"' then NVL(a.part_amount,0) else 0 end)-(case when a.status='"+Constant.CLAIM_APPLY_ORD_TYPE_07+"' then NVL(a.balance_part_amount,0) else 0 end)) as partbalance,--  配件扣款,\n");
		sql.append("((case when a.status='"+Constant.CLAIM_APPLY_ORD_TYPE_07+"' then NVL(a.labour_amount,0) else 0 end)-(case when a.status='"+Constant.CLAIM_APPLY_ORD_TYPE_07+"' then NVL(a.balance_labour_amount,0) else 0 end)) as appendamount,--工时扣款,\n");
		sql.append("((case when a.status='"+Constant.CLAIM_APPLY_ORD_TYPE_07+"' then NVL(a.apply_appendlabour_amount,0) else 0 end)-(case when a.status='"+Constant.CLAIM_APPLY_ORD_TYPE_07+"' then NVL(a.appendlabour_amount,0) else 0 end)) as applyamount,--追加工时扣款,\n");
		sql.append("((case when a.status='"+Constant.CLAIM_APPLY_ORD_TYPE_07+"' then NVL(a.NETITEM_AMOUNT,0) else 0 end)-(case when a.status='"+Constant.CLAIM_APPLY_ORD_TYPE_07+"' then NVL(a.balance_netitem_amount,0) else 0 end)) as orteramount,--其他费用扣款,\n");
		sql.append("((case when a.status='"+Constant.CLAIM_APPLY_ORD_TYPE_07+"' then NVL(a.apply_append_amount,0) else 0 end)-(case when a.status='"+Constant.CLAIM_APPLY_ORD_TYPE_07+"' then NVL(a.append_amount,0) else 0 end)) as teshuamount,--特殊费用扣款,\n");
		
		sql.append("((((case when a.status='"+Constant.CLAIM_APPLY_ORD_TYPE_07+"' then NVL(a.part_amount,0) else 0 end)-(case when a.status='"+Constant.CLAIM_APPLY_ORD_TYPE_07+"' then NVL(a.balance_part_amount,0) else 0 end)))+" +
				"(((case when a.status='"+Constant.CLAIM_APPLY_ORD_TYPE_07+"' then NVL(a.labour_amount,0) else 0 end)-(case when a.status='"+Constant.CLAIM_APPLY_ORD_TYPE_07+"' then NVL(a.balance_labour_amount,0) else 0 end)))+" +
				"(((case when a.status='"+Constant.CLAIM_APPLY_ORD_TYPE_07+"' then NVL(a.apply_appendlabour_amount,0) else 0 end)-(case when a.status='"+Constant.CLAIM_APPLY_ORD_TYPE_07+"' then NVL(a.appendlabour_amount,0) else 0 end)))+" +
				"(((case when a.status='"+Constant.CLAIM_APPLY_ORD_TYPE_07+"' then NVL(a.NETITEM_AMOUNT,0) else 0 end)-(case when a.status='"+Constant.CLAIM_APPLY_ORD_TYPE_07+"' then NVL(a.balance_netitem_amount,0) else 0 end)))+" +
				"(((case when a.status='"+Constant.CLAIM_APPLY_ORD_TYPE_07+"' then NVL(a.apply_append_amount,0) else 0 end)-(case when a.status='"+Constant.CLAIM_APPLY_ORD_TYPE_07+"' then NVL(a.append_amount,0) else 0 end))))" +
				" as subclaimtotal  -- 索赔单总扣款\n");
		
		sql.append("from tt_as_wr_application a WHERE A.STATUS IN('"+Constant.CLAIM_APPLY_ORD_TYPE_04+"','"+Constant.CLAIM_APPLY_ORD_TYPE_07+"','"+Constant.CLAIM_APPLY_ORD_TYPE_08+"') \n" );
		if(Utility.testString(map.get("dealerId"))){
			sql.append("AND a.dealer_id = '"+map.get("dealerId")+"'\n");
		}
		if(Utility.testString(map.get("balanceNo"))){
			sql.append("AND A.CLAIM_NO LIKE '%"+map.get("balanceNo")+"%'\n");
		}
		if(Utility.testString(map.get("claimType"))){
			sql.append("AND A.CLAIM_TYPE='"+map.get("claimType")+"'\n"); 
		}
		if(Utility.testString(map.get("STATUS"))){
			sql.append("AND A.STATUS = '"+map.get("STATUS")+"'\n");
		}
		if(Utility.testString(map.get("YIELDLY"))){
			sql.append("AND A.YIELDLY = '"+map.get("YIELDLY")+"'\n");
		}
		if(Utility.testString(map.get("START_DATE"))){
			sql.append("AND A.AUDITING_DATE >= TO_DATE('"+map.get("START_DATE")+"', 'YYYY-MM-DD HH24:MI:SS')\n");
		}
		if(Utility.testString(map.get("END_DATE"))){
			sql.append("AND A.AUDITING_DATE <= TO_DATE('"+map.get("END_DATE")+"', 'YYYY-MM-DD HH24:MI:SS')\n");
		}
		sql.append("order BY A.CLAIM_NO ASC\n");
		System.out.println("sqlsql=="+sql.toString());
		PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
		return ps;
	}
	/**
	 * yx writed 20101217
	 * 结算单扣款报表
	 * @param bean
	 * @return
	 */
	public PageResult<Map<String, Object>> getBalanceDeduct(String balanceNo,String dealerCode,String yieldly,
			String beginTime,String endTime,int curPage, int pageSize){

		StringBuffer sql = new StringBuffer();
		sql.append("select t.balance_no,\n");
		sql.append("       t.dealer_code,\n");
		sql.append("       TO_CHAR(t.start_date, 'YYYY-MM-dd') AS start_date,\n");
		sql.append("       TO_CHAR(t.end_date, 'YYYY-MM-dd') AS end_date,\n");
		sql.append("       t.yieldly,\n");
		sql.append("       NVL(t.old_deduct, 0) as old_deduct,\n");
		sql.append("       NVL(t.check_deduct, 0) as check_deduct,\n");
		sql.append("       NVL(t.admin_deduct, 0) as admin_deduct,\n");
		sql.append("       NVL(t.free_deduct, 0) as free_deduct,\n");
		sql.append("       NVL(t.service_deduct, 0) as service_deduct,\n");
		sql.append("       NVL(t.financial_deduct, 0) as financial_deduct,\n");
		sql.append("       (NVL(t.old_deduct, 0) + NVL(t.check_deduct, 0) +\n");
		sql.append("       NVL(t.admin_deduct, 0) + NVL(t.free_deduct, 0) +\n");
		sql.append("       NVL(t.free_deduct, 0) + NVL(t.financial_deduct, 0)) as total_deduct\n");
		sql.append("  from tt_as_wr_claim_balance t\n");
		sql.append(" where 1=1\n");
		if(Utility.testString(balanceNo)){
			sql.append("AND t.balance_no LIKE '%"+balanceNo+"%'\n");
		}
		if(Utility.testString(dealerCode)){
			sql.append("AND t.dealer_code LIKE '%"+dealerCode+"%'\n");
		}
		if(Utility.testString(yieldly)){
			sql.append("AND t.yieldly LIKE '%"+yieldly+"%'\n");
		}
		if(Utility.testString(beginTime)){
			sql.append("AND t.start_date >= TO_DATE('"+beginTime+"', 'YYYY-MM-DD HH24:MI:SS')\n");
		}
		if(Utility.testString(endTime)){
			sql.append("AND t.start_date <= TO_DATE('"+endTime+"', 'YYYY-MM-DD HH24:MI:SS')\n");
		}
		sql.append(" order by t.create_date\n");
		PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
		return ps;
	}
	
	public PageResult<Map<String, Object>> queryKeepAmountReport(String beginTime,String endTime, String dealerCode,int curPage, int pageSize){
		StringBuffer sql= new StringBuffer(); //YH 2011.03.01
		sql.append("select dealer_code,\n" );
		sql.append("       dealer_name,\n" );
		sql.append("       sum(soubao) soubao,\n" );
		sql.append("       sum(libao) libao,sum(soubaojiesuan) soubaojiesuan,sum(libaojiesuan) libaojiesuan,\n" );
		sql.append("       sum(quanbu) quanbu\n" );
		sql.append("  from (select re.dealer_code,\n" );
		sql.append("               a.dealer_name,\n" );
		sql.append("               (select nvl(sum(1),0)\n" );
		sql.append("                  from tt_as_repair_order c\n" );
		sql.append("                 where c.repair_type_code = 11441004\n" );
		sql.append("                   and c.free_times = 1\n" );
		sql.append("                   and re.id = c.id) soubao,\n" );
		sql.append("                   (select nvl(sum(1),0) from tt_as_repair_order c where c.repair_type_code = 11441004 and c.free_times = 1 and re.id = c.id and c.ro_status=11591002) soubaojiesuan,\n" );
		sql.append("                   (select nvl(sum(1),0) from tt_as_repair_order c where c.repair_type_code = 11441004 and c.free_times != 1 and re.id = c.id and c.ro_status=11591002) libaojiesuan,\n" );
		sql.append("               (select nvl(sum(1),0)\n" );
		sql.append("                  from tt_as_repair_order c\n" );
		sql.append("                 where c.repair_type_code = 11441004\n" );
		sql.append("                   and c.free_times != 1\n" );
		sql.append("                   and re.id = c.id) libao,\n" );
		sql.append("               (select nvl(sum(1),0)\n" );
		sql.append("                  from tt_as_repair_order c\n" );
		sql.append("                 where c.repair_type_code = 11441004\n" );
		sql.append("                   and re.id = c.id)  quanbu\n" );
		sql.append("          from tt_as_repair_order re, tm_dealer a\n" );
		sql.append("         where re.dealer_code = a.dealer_code");
		if(Utility.testString(beginTime)){
			sql.append(" AND re.create_date >= TO_DATE('").append(beginTime).append("', 'YYYY-MM-DD HH24:MI:SS') \n");
		}
		if(Utility.testString(endTime)){
			sql.append(" AND re.create_date <= TO_DATE('").append(endTime).append("', 'YYYY-MM-DD HH24:MI:SS') \n");
		}
		if(Utility.testString(dealerCode)){
			sql.append(" AND a.dealer_code in ( ").append(dealerCode).append(" ) \n");
		}
		
		sql.append(" ) group by dealer_code, dealer_name ");
		
		PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
		return ps;
	}
	
	public List<Map<String, Object>> getKeepAmountReportExcelList(String beginTime,String endTime, String dealerCode){
		StringBuffer sql= new StringBuffer();   //YH 2011.03.01
		sql.append("select dealer_code,\n" );
		sql.append("       dealer_name,\n" );
		sql.append("       sum(soubao) soubao,\n" );
		sql.append("       sum(libao) libao,sum(soubaojiesuan) soubaojiesuan,sum(libaojiesuan) libaojiesuan,\n" );
		sql.append("       sum(quanbu) quanbu\n" );
		sql.append("  from (select re.dealer_code,\n" );
		sql.append("               a.dealer_name,\n" );
		sql.append("               (select nvl(sum(1),0)\n" );
		sql.append("                  from tt_as_repair_order c\n" );
		sql.append("                 where c.repair_type_code = 11441004\n" );
		sql.append("                   and c.free_times = 1\n" );
		sql.append("                   and re.id = c.id) soubao,\n" );
		sql.append("                   (select nvl(sum(1),0) from tt_as_repair_order c where c.repair_type_code = 11441004 and c.free_times = 1 and re.id = c.id and c.ro_status=11591002) soubaojiesuan,\n" );
		sql.append("                   (select nvl(sum(1),0) from tt_as_repair_order c where c.repair_type_code = 11441004 and c.free_times != 1 and re.id = c.id and c.ro_status=11591002) libaojiesuan,\n" );
		sql.append("               (select nvl(sum(1),0)\n" );
		sql.append("                  from tt_as_repair_order c\n" );
		sql.append("                 where c.repair_type_code = 11441004\n" );
		sql.append("                   and c.free_times != 1\n" );
		sql.append("                   and re.id = c.id) libao,\n" );
		sql.append("               (select nvl(sum(1),0)\n" );
		sql.append("                  from tt_as_repair_order c\n" );
		sql.append("                 where c.repair_type_code = 11441004\n" );
		sql.append("                   and re.id = c.id)  quanbu\n" );
		sql.append("          from tt_as_repair_order re, tm_dealer a\n" );
		sql.append("         where re.dealer_code = a.dealer_code");
		if(Utility.testString(beginTime)){
			sql.append(" AND re.create_date >= TO_DATE('").append(beginTime).append("', 'YYYY-MM-DD HH24:MI:SS') \n");
		}
		if(Utility.testString(endTime)){
			sql.append(" AND re.create_date <= TO_DATE('").append(endTime).append("', 'YYYY-MM-DD HH24:MI:SS') \n");
		}
		if(Utility.testString(dealerCode)){
			sql.append(" AND a.dealer_code in ( ").append(dealerCode).append(" ) \n");
		}
		
		sql.append(" ) group by dealer_code, dealer_name ");
		
		List<Map<String, Object>> list = pageQuery(sql.toString(), null, getFunName());
		return list;
	}
	
	public PageResult<Map<String, Object>> queryChangeDetailReport(String beginTime,String endTime, String dealerCode,String part_code ,int curPage, int pageSize){
		StringBuffer sql= new StringBuffer();    //YH 2011.03.01
		sql.append("  select /*+ORDERED INDEX(m IDX_WR_APPLICATION_CT) INDEX(o TT_PARTSITEM_ID)  INDEX(a PK_TM_DEALER) */ a.dealer_code,\n" );
		sql.append("      a.dealer_name,\n" );
		sql.append("      o.down_part_code PART_NO,\n" );
		sql.append("      o.down_part_name PART_NAME,\n" );
		sql.append("      count(o.down_part_code) CHANGE_AMOUNT\n" );
		sql.append(" from tt_as_wr_application m, Tt_As_Wr_Partsitem o, tm_dealer a\n" );
		sql.append("where \n" );
		sql.append("   o.id = m.id\n" );
		
		if(Utility.testString(beginTime)){
			sql.append(" AND m.account_date >= TO_DATE('").append(beginTime).append("', 'YYYY-MM-DD HH24:MI:SS') \n");
		}
		if(Utility.testString(endTime)){
			sql.append(" AND m.account_date <= TO_DATE('").append(endTime).append("', 'YYYY-MM-DD HH24:MI:SS') \n");
		}
		sql.append(" and  m.dealer_id = a.dealer_id\n");
		sql.append(" and m.status=10791007 \n");
		if(Utility.testString(dealerCode)){
			sql.append(" AND a.dealer_code in ( ").append(dealerCode).append(" ) \n");
		}
		if(Utility.testString(part_code)){
			sql.append(" AND o.down_part_code = '").append(part_code).append("' \n");
		}		
		sql.append("\n" );
		sql.append("group by a.dealer_code,  a.dealer_name, o.down_part_code, o.down_part_name");
		
		PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
		return ps;
	}
	
	
	public List<Map<String, Object>> queryChangeDetailReport(String beginTime,String endTime, String dealerCode,String part_code){
		StringBuffer sql= new StringBuffer();   //YH 2011.03.01
		sql.append("  select /*+ORDERED INDEX(m IDX_WR_APPLICATION_CT) INDEX(o TT_PARTSITEM_ID)  INDEX(a PK_TM_DEALER) */ a.dealer_code,\n" );
		sql.append("      a.dealer_name,\n" );
		sql.append("      o.down_part_code PART_NO,\n" );
		sql.append("      o.down_part_name PART_NAME,\n" );
		sql.append("      count(o.down_part_code) CHANGE_AMOUNT\n" );
		sql.append(" from tt_as_wr_application m, Tt_As_Wr_Partsitem o, tm_dealer a\n" );
		sql.append("where \n" );
		sql.append("   o.id = m.id\n" );
		
		if(Utility.testString(beginTime)){
			sql.append(" AND m.account_date >= TO_DATE('").append(beginTime).append("', 'YYYY-MM-DD HH24:MI:SS') \n");
		}
		if(Utility.testString(endTime)){
			sql.append(" AND m.account_date <= TO_DATE('").append(endTime).append("', 'YYYY-MM-DD HH24:MI:SS') \n");
		}
		sql.append(" and  m.dealer_id = a.dealer_id\n");
		sql.append(" and m.status=10791007 \n");
		if(Utility.testString(dealerCode)){
			sql.append(" AND a.dealer_code in ( ").append(dealerCode).append(" ) \n");
		}
		if(Utility.testString(part_code)){
			sql.append(" AND o.down_part_code = '").append(part_code).append("' \n");
		}		
		sql.append("\n" );
		sql.append("group by a.dealer_code,  a.dealer_name, o.down_part_code, o.down_part_name");
		
		List<Map<String, Object>> list = pageQuery(sql.toString(), null, getFunName());
		return list;
	}
	
	public PageResult<Map<String, Object>> queryWalkKeepAmountReport(String beginTime,String endTime, String dealerCode,String yieldly,String province ,String areaId,int curPage, int pageSize){ 
		
		StringBuffer sql= new StringBuffer();
		sql.append("select syb ,\n" );
		sql.append("       sf ,\n" );
		sql.append("       dcode ,\n" );
		sql.append("       dname ,\n" );
		sql.append("       sum(zbs) zbs,\n" );
		sql.append("       sum(wxs) wxs,\n" );
		sql.append("       sum(wxcs) wxcs \n" );
		sql.append("  from (select syb,\n" );
		sql.append("               sf,\n" );
		sql.append("               dcode,\n" );
		sql.append("               dname,\n" );
		sql.append("               0 zbs,\n" );
		sql.append("               count(distinct wvin) wxs,\n" );
		sql.append("               count(distinct idd) wxcs \n" );
		sql.append("          from (select s.root_org_name syb,\n" );
		sql.append("                       s.region_name sf,\n" );
		sql.append("                       d.dealer_code dcode,\n" );
		sql.append("                       d.dealer_name dname, t.id idd, \n" );
		sql.append("                       t.vin wvin\n" );
		sql.append("                  from TT_AS_WR_APPLICATION   t,\n" );
		sql.append("                       tm_dealer              d,\n" );
		sql.append("                       vw_org_dealer_service  s\n" );
		sql.append("                   where s.dealer_id = d.dealer_id\n" );
		sql.append("                   and t.dealer_id = d.dealer_id\n" );
		if(Utility.testString(beginTime)){
			sql.append(" AND T.AUDITING_DATE >= TO_DATE('").append(beginTime).append("', 'YYYY-MM-DD HH24:MI:SS')\n");
		}
		if(Utility.testString(endTime)){
			sql.append(" AND T.AUDITING_DATE <= TO_DATE('").append(endTime).append("', 'YYYY-MM-DD HH24:MI:SS')\n");
		}
		if(Utility.testString(dealerCode)){
			sql.append(" AND d.dealer_code in (").append(dealerCode).append(" ) \n");
		}
		if(Utility.testString(province)){
			sql.append(" AND s.region_name like '%").append(province).append("%' \n");
		}
		if(Utility.testString(areaId)){
			sql.append(" AND s.root_org_id ='").append(areaId).append("' \n");
		}
		if(Utility.testString(yieldly)){
			sql.append(" AND t.yieldly in (").append(yieldly).append(" ) \n");
		}
		sql.append("                   and t.status in (10791004, 10791007, 10791008)\n" );
		sql.append("                   and t.claim_type in (10661001, 10661007, 10661009)\n" );
		sql.append("                   )\n" );
		sql.append("         group by syb, sf, dcode, dname\n" );
		sql.append("        union all\n" );
		sql.append("        select syb,\n" );
		sql.append("               sf,\n" );
		sql.append("               dcode,\n" );
		sql.append("               dname,\n" );
		sql.append("               count(distinct bvin) zbs,\n" );
		sql.append("               0 wxs,\n" );
		sql.append("               0 wxcs \n" );
		sql.append("          from (select s.root_org_name syb,\n" );
		sql.append("                       s.region_name sf,\n" );
		sql.append("                       d.dealer_code dcode,\n" );
		sql.append("                       d.dealer_name dname,t.id idd, \n" );
		sql.append("                       t.vin bvin\n" );
		sql.append("                  from TT_AS_WR_APPLICATION   t,\n" );
		sql.append("                       tm_dealer              d,\n" );
		sql.append("                       vw_org_dealer_service  s\n" );
		sql.append("                 where s.dealer_id = d.dealer_id\n" );
		sql.append("                   and t.dealer_id = d.dealer_id\n" );
		if(Utility.testString(beginTime)){
			sql.append(" AND T.AUDITING_DATE >= TO_DATE('").append(beginTime).append("', 'YYYY-MM-DD HH24:MI:SS')\n");
		}
		if(Utility.testString(endTime)){
			sql.append(" AND T.AUDITING_DATE <= TO_DATE('").append(endTime).append("', 'YYYY-MM-DD HH24:MI:SS')\n");
		}
		if(Utility.testString(dealerCode)){
			sql.append(" AND d.dealer_code in (").append(dealerCode).append(" ) \n");
		}
		if(Utility.testString(province)){
			sql.append(" AND s.region_name like '%").append(province).append("%'\n");
		}
		if(Utility.testString(areaId)){
			sql.append(" AND s.root_org_id ='").append(areaId).append("' \n");
		}
		if(Utility.testString(yieldly)){
			sql.append(" AND t.yieldly in (").append(yieldly).append(" ) \n");
		}
		sql.append("                   and t.status in (10791004, 10791007, 10791008)\n" );
		sql.append("                   and t.claim_type = 10661002\n" );
		sql.append("                   )\n" );
		sql.append("         group by syb, sf, dcode, dname\n" );
		sql.append("        )\n" );
		sql.append(" group by syb, sf, dcode, dname ");
		
		PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
		return ps;	
	}
	
 public List<Map<String, Object>> queryWalkKeepAmountReportExcel(String beginTime,String endTime, String dealerCode,String yieldly,String province,String areaId ){ 
		
	 StringBuffer sql= new StringBuffer();
		sql.append("select syb ,\n" );
		sql.append("       sf ,\n" );
		sql.append("       dcode ,\n" );
		sql.append("       dname ,\n" );
		sql.append("       sum(zbs) zbs,\n" );
		sql.append("       sum(wxs) wxs,\n" );
		sql.append("       sum(wxcs) wxcs \n" );
		sql.append("  from (select syb,\n" );
		sql.append("               sf,\n" );
		sql.append("               dcode,\n" );
		sql.append("               dname,\n" );
		sql.append("               0 zbs,\n" );
		sql.append("               count(distinct wvin) wxs,\n" );
		sql.append("               count(distinct idd) wxcs \n" );
		sql.append("          from (select s.root_org_name syb,\n" );
		sql.append("                       s.region_name sf,\n" );
		sql.append("                       d.dealer_code dcode,\n" );
		sql.append("                       d.dealer_name dname, t.id idd, \n" );
		sql.append("                       t.vin wvin\n" );
		sql.append("                  from TT_AS_WR_APPLICATION   t,\n" );
		sql.append("                       tm_dealer              d,\n" );
		sql.append("                       vw_org_dealer_service  s\n" );
		sql.append("                   where s.dealer_id = d.dealer_id\n" );
		sql.append("                   and t.dealer_id = d.dealer_id\n" );
		if(Utility.testString(beginTime)){
			sql.append(" AND T.AUDITING_DATE >= TO_DATE('").append(beginTime).append("', 'YYYY-MM-DD HH24:MI:SS')\n");
		}
		if(Utility.testString(endTime)){
			sql.append(" AND T.AUDITING_DATE <= TO_DATE('").append(endTime).append("', 'YYYY-MM-DD HH24:MI:SS')\n");
		}
		if(Utility.testString(dealerCode)){
			sql.append(" AND d.dealer_code in (").append(dealerCode).append(" ) \n");
		}
		if(Utility.testString(province)){
			sql.append(" AND s.region_name like '%").append(province).append("%' \n");
		}
		if(Utility.testString(areaId)){
			sql.append(" AND s.root_org_id ='").append(areaId).append("' \n");
		}
		if(Utility.testString(yieldly)){
			sql.append(" AND t.yieldly in (").append(yieldly).append(" ) \n");
		}
		sql.append("                   and t.status in (10791004, 10791007, 10791008)\n" );
		sql.append("                   and t.claim_type in (10661001, 10661007, 10661009)\n" );
		sql.append("                   )\n" );
		sql.append("         group by syb, sf, dcode, dname\n" );
		sql.append("        union all\n" );
		sql.append("        select syb,\n" );
		sql.append("               sf,\n" );
		sql.append("               dcode,\n" );
		sql.append("               dname,\n" );
		sql.append("               count(distinct bvin) zbs,\n" );
		sql.append("               0 wxs,\n" );
		sql.append("               0 wxcs \n" );
		sql.append("          from (select s.root_org_name syb,\n" );
		sql.append("                       s.region_name sf,\n" );
		sql.append("                       d.dealer_code dcode,\n" );
		sql.append("                       d.dealer_name dname,t.id idd, \n" );
		sql.append("                       t.vin bvin\n" );
		sql.append("                  from TT_AS_WR_APPLICATION   t,\n" );
		sql.append("                       tm_dealer              d,\n" );
		sql.append("                       vw_org_dealer_service  s\n" );
		sql.append("                 where s.dealer_id = d.dealer_id\n" );
		sql.append("                   and t.dealer_id = d.dealer_id\n" );
		if(Utility.testString(beginTime)){
			sql.append(" AND T.AUDITING_DATE >= TO_DATE('").append(beginTime).append("', 'YYYY-MM-DD HH24:MI:SS')\n");
		}
		if(Utility.testString(endTime)){
			sql.append(" AND T.AUDITING_DATE <= TO_DATE('").append(endTime).append("', 'YYYY-MM-DD HH24:MI:SS')\n");
		}
		if(Utility.testString(dealerCode)){
			sql.append(" AND d.dealer_code in (").append(dealerCode).append(" ) \n");
		}
		if(Utility.testString(province)){
			sql.append(" AND s.region_name like '%").append(province).append("%'\n");
		}
		if(Utility.testString(areaId)){
			sql.append(" AND s.root_org_id ='").append(areaId).append("' \n");
		}
		if(Utility.testString(yieldly)){
			sql.append(" AND t.yieldly in (").append(yieldly).append(" ) \n");
		}
		sql.append("                   and t.status in (10791004, 10791007, 10791008)\n" );
		sql.append("                   and t.claim_type = 10661002\n" );
		sql.append("                   )\n" );
		sql.append("         group by syb, sf, dcode, dname\n" );
		sql.append("        )\n" );
		sql.append(" group by syb, sf, dcode, dname ");
		
		List<Map<String, Object>> list = pageQuery(sql.toString(), null, getFunName());
		return list;	
	}
	public PageResult<Map<String, Object>> queryRepairAmountReport
	(String beginTime,String endTime,String pbeginTime,String pendTime,String bbeginTime,String bendTime,
	 String dealerCode,String yieldly,String seriesCode,String modelName,String partYes,String partNo,
	 String NoNeed,String bugDesc,String oldPartName,String bugName,String partName,String jobName,String vin,Long orgId,int curPage, int pageSize){ 
		
		StringBuffer sql= new StringBuffer();
		sql.append("select t.model_name ,\n" );
		sql.append("       t.vin,\n" );
		sql.append("       t.engine_no ,\n" );
		sql.append("       to_char(v.product_date,'yyyy-MM-dd') product_date ,\n" );
		sql.append("       to_char(v.purchased_date,'yyyy-MM-dd') purchased_date,\n" );
		sql.append("       to_char(t.auditing_date,'yyyy-MM-dd') auditing_date,\n" );
		sql.append("       t.in_mileage ,\n" );
		sql.append("       t.trouble_desc　trouble_descs,\n" );
		sql.append("       t.trouble_reason　,\n" );
		sql.append("       t.repair_method ,\n" );
		sql.append("       l.trouble_code_name　,\n" );
		sql.append("       l.damage_type_name ,\n" );
		sql.append("       l.damage_area_name ,\n" );
		sql.append("       l.damage_degree_name ,\n" );
		sql.append("       p.remark　,\n" );
		sql.append("       l.wr_labourname ,\n" );
		sql.append("       l.labour_amount ,\n" );
		sql.append("       (case\n" );
		sql.append("         when l.first_part || '' <> p.part_id || '' then\n" );
		sql.append("          '是'\n" );
		sql.append("         else\n" );
		sql.append("          '否'\n" );
		sql.append("       end) first_part,\n" );
		sql.append("       t.appendlabour_num ,\n" );
		sql.append("       p.down_part_code ,\n" );
		sql.append("       p.down_part_name ,\n" );
		sql.append("       p.quantity　,\n" );
		sql.append("       p.amount ,to_char(r.create_date,'yyyy-MM-dd') create_date,\n" );
		sql.append("       p.producer_name　,\n" );
		sql.append("       d.dealer_name　,\n" );
		sql.append("       t.serve_advisor ,\n" );
		sql.append("       d.link_man,\n" );
		sql.append("       d.phone ,\n" );
		sql.append("       t.claim_no ,\n" );
		sql.append("       c.code_desc claim_type,\n" );
		sql.append("       t.remark remark2,\n" );
		sql.append("       decode(p.is_agree, 10041001, '是', 10041002, '否', '不需要授权') is_agree,ds.root_org_name,ds.dealer_code,r.deliverer,r.deliverer_phone,l.trouble_type,t.DEGRADATION_TYPE \n" );
		sql.append("  from TT_AS_WR_APPLICATION t,\n" );
		sql.append("       tt_as_wr_partsitem   p,\n" );
		sql.append("       tt_as_wr_labouritem  l,\n" );
		sql.append("       tt_as_repair_order  r,\n" );
		sql.append("       tm_vehicle           v,\n" );
		sql.append("       tm_dealer            d,\n" );
		sql.append("       tc_code              c,\n" );
		sql.append("       vw_org_dealer_service              ds\n" );
		sql.append(" where p.id(+) = t.id\n" );
		sql.append("   and t.vin = v.vin\n" );
		sql.append("   and c.code_id = t.claim_type\n");
		sql.append("   and p.wr_labourcode = l.wr_labourcode\n" );
		sql.append("   and  t.ro_no=r.ro_no \n" );
		sql.append("   and l.id(+) = t.id\n" );
		sql.append("   and t.dealer_id = d.dealer_id\n" );
		sql.append("   and t.dealer_id = ds.dealer_id\n" );
		SpecialCostReportDao dao = new SpecialCostReportDao();
		List<Map<String, Object>>  orgLever = dao.getAreaParentList(orgId);
		if(orgLever.get(0).get("ORG_LEVEL").toString().equals("2")){
			sql.append("and ds.root_org_id = "+orgId+" \n");
		}
		sql.append("   and t.status in (10791008, 10791004, 10791007)\n" );
		sql.append("   and t.claim_type in (10661001, 10661007, 10661009)\n" );
		if(Utility.testString(pbeginTime)) {
			sql.append(" AND T.AUDITING_DATE >= TO_DATE('").append(pbeginTime).append("', 'YYYY-MM-DD HH24:MI:SS')\n");
		}
		if(Utility.testString(pendTime)){
			sql.append(" AND T.AUDITING_DATE <= TO_DATE('").append(pendTime).append("', 'YYYY-MM-DD HH24:MI:SS')\n");
		}
		if(Utility.testString(beginTime)) {
			sql.append(" AND v.product_date >= TO_DATE('").append(beginTime).append("', 'YYYY-MM-DD HH24:MI:SS')\n");
		}
		if(Utility.testString(endTime)){
			sql.append(" AND v.product_date <= TO_DATE('").append(endTime).append("', 'YYYY-MM-DD HH24:MI:SS')\n");
		}
		if(Utility.testString(bbeginTime)) {
			sql.append(" AND v.purchased_date >= TO_DATE('").append(bbeginTime).append("', 'YYYY-MM-DD HH24:MI:SS')\n");
		}
		if(Utility.testString(bendTime)){
			sql.append(" AND v.purchased_date <= TO_DATE('").append(bendTime).append("', 'YYYY-MM-DD HH24:MI:SS')\n");
		}
		if(Utility.testString(dealerCode)){
			sql.append(" AND d.dealer_code in (").append(dealerCode).append(" ) \n");
		}
		if(Utility.testString(yieldly)) {
	       sql.append(" AND t.yieldly in (").append(yieldly).append(") \n");
		}
		if(Utility.testString(seriesCode)) {
		       sql.append(" AND t.series_code ='").append(seriesCode).append("' \n");
			}
		if(Utility.testString(modelName)) {
		       sql.append(" AND t.model_name like '%").append(modelName).append("%' \n");
			}
		String sql2 = "";
		if(Utility.testString(partYes)){
			
			 sql2 =" and p.is_agree = 10041001 \n";
			if(Utility.testString(partNo)){
			 sql2 =" and ( p.is_agree = 10041001 or p.is_agree = 10041002 ) \n";	
			}else if(Utility.testString(NoNeed)){			
			 sql2=" and ( p.is_agree = 10041001 or p.is_agree is null ) \n"; 
			}
		}else if (Utility.testString(partNo)){
			sql2="   and p.is_agree = 10041002 \n";		
				if(Utility.testString(NoNeed)){			
			sql2=" and ( p.is_agree = 10041002 or p.is_agree is null ) \n"; 
			  }
		}else if (Utility.testString(NoNeed)){			
			 sql.append(" and p.is_agree is null \n"); 
		}	
		     sql.append(sql2);
		 if(Utility.testString(bugDesc)) {
		       sql.append(" AND t.trouble_desc like '%").append(bugDesc).append("%' \n");
		}
		 if(Utility.testString(bugName)) {
		       sql.append(" AND t.trouble_reason like '%").append(bugName).append("%' \n");
		} 
		 if(Utility.testString(oldPartName)) {
		       sql.append(" AND p.down_part_name like '%").append(oldPartName).append("%' \n");
		} 
		 if(Utility.testString(partName)) {
		       sql.append(" AND p.part_name like '%").append(partName).append("%' \n");
		}
		 if(Utility.testString(jobName)){
		       sql.append(" AND l.wr_labourname like '%").append(jobName).append("%' \n");	 
		  }
		 if(Utility.testString(vin)){
		       sql.append(" AND t.vin  ='").append(vin).append("' \n");	 
		  }
		PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
		return ps;	
	}
	
	public List<Map<String, Object>> queryRepairAmountReportExcel
	(String beginTime,String endTime,String pbeginTime,String pendTime,String bbeginTime,String bendTime,
	 String dealerCode,String yieldly,String seriesCode,String modelName,String partYes,String partNo,
	 String NoNeed,String bugDesc,String oldPartName,String bugName,String partName,String jobName,String vin,Long orgId){ 
		
		
		StringBuffer sql= new StringBuffer();
		sql.append("select t.model_name ,\n" );
		sql.append("       t.vin,\n" );
		sql.append("       t.engine_no ,\n" );
		sql.append("       to_char(v.product_date,'yyyy-MM-dd') product_date ,\n" );
		sql.append("       to_char(v.purchased_date,'yyyy-MM-dd') purchased_date,\n" );
		sql.append("       to_char(t.auditing_date,'yyyy-MM-dd') auditing_date,\n" );
		sql.append("       t.in_mileage ,\n" );
		sql.append("       t.trouble_desc　trouble_descs,\n" );
		sql.append("       t.trouble_reason　,\n" );
		sql.append("       t.repair_method ,\n" );
		sql.append("       l.trouble_code_name　,\n" );
		sql.append("       l.damage_type_name ,\n" );
		sql.append("       l.damage_area_name ,\n" );
		sql.append("       l.damage_degree_name ,\n" );
		sql.append("       p.remark　,\n" );
		sql.append("       l.wr_labourname ,\n" );
		sql.append("       l.labour_amount ,\n" );
		sql.append("       (case\n" );
		sql.append("         when l.first_part || '' <> p.part_id || '' then\n" );
		sql.append("          '是'\n" );
		sql.append("         else\n" );
		sql.append("          '否'\n" );
		sql.append("       end) first_part,\n" );
		sql.append("       t.appendlabour_num ,\n" );
		sql.append("       p.down_part_code ,\n" );
		sql.append("       p.down_part_name ,\n" );
		sql.append("       p.quantity　,\n" );
		sql.append("       p.amount ,\n" );
		sql.append("       p.amount ,to_char(r.create_date,'yyyy-MM-dd') create_date,\n" );
		sql.append("       p.producer_name　,\n" );
		sql.append("       d.dealer_name　,\n" );
		sql.append("       t.serve_advisor ,\n" );
		sql.append("       d.link_man,\n" );
		sql.append("       d.phone ,\n" );
		sql.append("       t.claim_no ,\n" );
		sql.append("       c.code_desc claim_type,\n" );
		sql.append("       t.remark remark2,\n" );
		sql.append("       decode(p.is_agree, 10041001, '是', 10041002, '否', '不需要授权') is_agree,ds.root_org_name,ds.dealer_code,r.deliverer,r.deliverer_phone,l.trouble_type \n" );
		sql.append("  from TT_AS_WR_APPLICATION t,\n" );
		sql.append("       tt_as_wr_partsitem   p,\n" );
		sql.append("       tt_as_wr_labouritem  l,\n" );
		sql.append("       tt_as_repair_order  r,\n" );
		sql.append("       tm_vehicle           v,\n" );
		sql.append("       tm_dealer            d,\n" );
		sql.append("       tc_code              c,\n" );
		sql.append("       vw_org_dealer_service              ds\n" );
		sql.append(" where p.id(+) = t.id\n" );
		sql.append("   and t.vin = v.vin\n" );
		sql.append("   and c.code_id = t.claim_type\n");
		sql.append("   and p.wr_labourcode = l.wr_labourcode\n" );
		sql.append("   and  t.ro_no=r.ro_no \n" );
		sql.append("   and l.id(+) = t.id\n" );
		sql.append("   and t.dealer_id = d.dealer_id\n" );
		sql.append("   and t.dealer_id = ds.dealer_id\n" );
		SpecialCostReportDao dao = new SpecialCostReportDao();
		List<Map<String, Object>>  orgLever = dao.getAreaParentList(orgId);
		if(orgLever.get(0).get("ORG_LEVEL").toString().equals("2")){
			sql.append("and ds.root_org_id = "+orgId+" \n");
		}
		sql.append("   and t.status in (10791008, 10791004, 10791007)\n" );
		sql.append("   and t.claim_type in (10661001, 10661007, 10661009)\n" );
		if(Utility.testString(pbeginTime)) {
			sql.append(" AND T.AUDITING_DATE >= TO_DATE('").append(pbeginTime).append("', 'YYYY-MM-DD HH24:MI:SS')\n");
		}
		if(Utility.testString(pendTime)){
			sql.append(" AND T.AUDITING_DATE <= TO_DATE('").append(pendTime).append("', 'YYYY-MM-DD HH24:MI:SS')\n");
		}
		if(Utility.testString(beginTime)) {
			sql.append(" AND v.product_date >= TO_DATE('").append(beginTime).append("', 'YYYY-MM-DD HH24:MI:SS')\n");
		}
		if(Utility.testString(endTime)){
			sql.append(" AND v.product_date <= TO_DATE('").append(endTime).append("', 'YYYY-MM-DD HH24:MI:SS')\n");
		}
		if(Utility.testString(bbeginTime)) {
			sql.append(" AND v.purchased_date >= TO_DATE('").append(bbeginTime).append("', 'YYYY-MM-DD HH24:MI:SS')\n");
		}
		if(Utility.testString(bendTime)){
			sql.append(" AND v.purchased_date <= TO_DATE('").append(bendTime).append("', 'YYYY-MM-DD HH24:MI:SS')\n");
		}
		if(Utility.testString(dealerCode)){
			sql.append(" AND d.dealer_code in (").append(dealerCode).append(" ) \n");
		}
		if(Utility.testString(yieldly)) {
	       sql.append(" AND t.yieldly in (").append(yieldly).append(") \n");
		}
		if(Utility.testString(seriesCode)) {
		       sql.append(" AND t.series_code ='").append(seriesCode).append("' \n");
			}
		if(Utility.testString(modelName)) {
		       sql.append(" AND t.model_name like '%").append(modelName).append("%' \n");
			}
		String sql2 = "";
		if(Utility.testString(partYes)){
			
			 sql2 =" and p.is_agree = 10041001 \n";
			if(Utility.testString(partNo)){
			 sql2 =" and ( p.is_agree = 10041001 or p.is_agree = 10041002 ) \n";	
			}else if(Utility.testString(NoNeed)){			
			 sql2=" and ( p.is_agree = 10041001 or p.is_agree is null ) \n"; 
			}
		}else if (Utility.testString(partNo)){
			sql2="   and p.is_agree = 10041002 \n";		
				if(Utility.testString(NoNeed)){			
			sql2=" and ( p.is_agree = 10041002 or p.is_agree is null ) \n"; 
			  }
		}else if (Utility.testString(NoNeed)){			
			 sql.append(" and p.is_agree is null \n"); 
		}	
		     sql.append(sql2);
		 if(Utility.testString(bugDesc)) {
		       sql.append(" AND t.trouble_desc like '%").append(bugDesc).append("%' \n");
		}
		 if(Utility.testString(bugName)) {
		       sql.append(" AND t.trouble_reason like '%").append(bugName).append("%' \n");
		} 
		 if(Utility.testString(oldPartName)) {
		       sql.append(" AND p.down_part_name like '%").append(oldPartName).append("%' \n");
		} 
		 if(Utility.testString(partName)) {
		       sql.append(" AND p.part_name like '%").append(partName).append("%' \n");
		}
		 if(Utility.testString(jobName)){
		       sql.append(" AND l.wr_labourname like '%").append(jobName).append("%' \n");	 
		  }
		 if(Utility.testString(vin)){
		       sql.append(" AND t.vin  ='").append(vin).append("' \n");	 
		  }
		 List<Map<String, Object>> list = pageQuery(sql.toString(), null, getFunName());
		return list;	
	}
	
	public PageResult<Map<String, Object>> queryTaurusAmountReport(String beginTime,String endTime,String partYes,String partNo,String NoNeed,String yieldly,int curPage, int pageSize){
		
		StringBuffer sql= new StringBuffer();
		sql.append("select decode(t.claim_type,\n" );
		sql.append("              10661009,\n" );
		sql.append("              '外出维修',\n" );
		sql.append("              10661001,\n" );
		sql.append("              '一般维修',\n" );
		sql.append("              10661002,\n" );
		sql.append("              '免费保养',\n" );
		sql.append("              10661003,\n" );
		sql.append("              '追加索赔',\n" );
		sql.append("              10661004,\n" );
		sql.append("              '重复修理索赔',\n" );
		sql.append("              10661005,\n" );
		sql.append("              '零件索赔更换',\n" );
		sql.append("              10661006,\n" );
		sql.append("              '服务活动',\n" );
		sql.append("              10661007,\n" );
		sql.append("              '售前维修',\n" );
		sql.append("              10661008,\n" );
		sql.append("              '保外索赔') claim_type,\n" );
		sql.append("       count(distinct t.vin) amount\n" );
		sql.append("  from TT_AS_WR_APPLICATION t, tt_as_wr_partsitem p, tm_dealer d\n" );
		sql.append(" where p.id = t.id\n" );
		sql.append("   and t.series_name like '%金牛星%'\n" );
		sql.append("   and t.dealer_id = d.dealer_id\n" );
		sql.append("   and t.status in (10791008, 10791004, 10791007)\n" );
		sql.append("   and t.claim_type in (10661001, 10661007, 10661009)\n" );
		sql.append("   and p.down_part_name != '无零件'\n" );
		sql.append("   and t.dealer_id = d.dealer_id\n" );
		sql.append("\n" );
		if(Utility.testString(beginTime)){
			sql.append(" AND T.AUDITING_DATE >= TO_DATE('").append(beginTime).append("', 'YYYY-MM-DD HH24:MI:SS')\n");
		}
		if(Utility.testString(endTime)){
			sql.append(" AND T.AUDITING_DATE <= TO_DATE('").append(endTime).append("', 'YYYY-MM-DD HH24:MI:SS')\n");
		}
		String sql2 = "";
		if(Utility.testString(partYes)){
			
			 sql2 =" and p.is_agree = 10041001 \n";
			if(Utility.testString(partNo)){
			 sql2 =" and ( p.is_agree = 10041001 or p.is_agree = 10041002 or p.is_agree is null ) \n";	
			}else if(Utility.testString(NoNeed)){			
			 sql2=" and ( p.is_agree = 10041001 or p.is_agree is null ) \n"; 
			}
		}else if (Utility.testString(partNo)){
			sql2="   and p.is_agree = 10041002 \n";		
				if(Utility.testString(NoNeed)){			
			sql2=" and ( p.is_agree = 10041002 or p.is_agree is null ) \n"; 
			  }
		}else if (Utility.testString(NoNeed)){			
			 sql.append(" and p.is_agree is null \n"); 
		}	
		     sql.append(sql2);
		     
		 if(Utility.testString(yieldly)) {
			       //sql.append(" AND t.yieldly in (").append(yieldly).append(") \n");
				}
		sql.append("\n" );
		sql.append(" group by t.claim_type");

		PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
		return ps;
	}
   public List<Map<String, Object>> queryTaurusAmountReportExcel(String beginTime,String endTime,String partYes,String partNo,String NoNeed,String yieldly){
		
	   StringBuffer sql= new StringBuffer();
		sql.append("select decode(t.claim_type,\n" );
		sql.append("              10661009,\n" );
		sql.append("              '外出维修',\n" );
		sql.append("              10661001,\n" );
		sql.append("              '一般维修',\n" );
		sql.append("              10661002,\n" );
		sql.append("              '免费保养',\n" );
		sql.append("              10661003,\n" );
		sql.append("              '追加索赔',\n" );
		sql.append("              10661004,\n" );
		sql.append("              '重复修理索赔',\n" );
		sql.append("              10661005,\n" );
		sql.append("              '零件索赔更换',\n" );
		sql.append("              10661006,\n" );
		sql.append("              '服务活动',\n" );
		sql.append("              10661007,\n" );
		sql.append("              '售前维修',\n" );
		sql.append("              10661008,\n" );
		sql.append("              '保外索赔') claim_type,\n" );
		sql.append("       count(distinct t.vin) amount\n" );
		sql.append("  from TT_AS_WR_APPLICATION t, tt_as_wr_partsitem p, tm_dealer d\n" );
		sql.append(" where p.id = t.id\n" );
		sql.append("   and t.series_name like '%金牛星%'\n" );
		sql.append("   and t.dealer_id = d.dealer_id\n" );
		sql.append("   and t.status in (10791008, 10791004, 10791007)\n" );
		sql.append("   and t.claim_type in (10661001, 10661007, 10661009)\n" );
		sql.append("   and p.down_part_name != '无零件'\n" );
		sql.append("   and t.dealer_id = d.dealer_id\n" );
		sql.append("\n" );
		if(Utility.testString(beginTime)){
			sql.append(" AND T.AUDITING_DATE >= TO_DATE('").append(beginTime).append("', 'YYYY-MM-DD HH24:MI:SS')\n");
		}
		if(Utility.testString(endTime)){
			sql.append(" AND T.AUDITING_DATE <= TO_DATE('").append(endTime).append("', 'YYYY-MM-DD HH24:MI:SS')\n");
		}
		String sql2 = "";
		if(Utility.testString(partYes)){
			
			 sql2 =" and p.is_agree = 10041001 \n";
			if(Utility.testString(partNo)){
			 sql2 =" and ( p.is_agree = 10041001 or p.is_agree = 10041002 or p.is_agree is null ) \n";	
			}else if(Utility.testString(NoNeed)){			
			 sql2=" and ( p.is_agree = 10041001 or p.is_agree is null ) \n"; 
			}
		}else if (Utility.testString(partNo)){
			sql2="   and p.is_agree = 10041002 \n";		
				if(Utility.testString(NoNeed)){			
			sql2=" and ( p.is_agree = 10041002 or p.is_agree is null ) \n"; 
			  }
		}else if (Utility.testString(NoNeed)){			
			 sql.append(" and p.is_agree is null \n"); 
		}	
		     sql.append(sql2);
		     
		 if(Utility.testString(yieldly)) {
			      // sql.append(" AND t.yieldly in (").append(yieldly).append(") \n");
				}
		sql.append("\n" );
		sql.append(" group by t.claim_type");
		
		 List<Map<String, Object>> list = pageQuery(sql.toString(), null, getFunName());

		return list;
	}
   
   public List<Map<String,Object>> weixiu(String vin){
	   StringBuffer sql = new StringBuffer("\n") ;
	   sql.append("select rownum,\n");
	   sql.append("       g.group_code,\n");  
	   sql.append("       v.vin,\n");  
	   sql.append("       a.in_mileage,\n");  
	   sql.append("       v.engine_no,\n");  
	   sql.append("       tc1.code_desc claim_type,\n");  
	   sql.append("       to_char(v.purchased_date, 'yyyy-MM-dd') purchased_date,\n");  
	   sql.append("       c.ctm_name,\n");  
	   sql.append("       d.dealer_name,\n");  
	   sql.append("       c.main_phone || ' ' || c.other_phone phone,\n");  
	   sql.append("       to_char(a.ro_startdate, 'yyyy-MM-dd') ro_date,\n");  
	   sql.append("       l.trouble_type,\n");  
	   sql.append("       p.part_code,\n");  
	   sql.append("       p.part_name,\n");  
	   sql.append("       l.wr_labourcode,\n");  
	   sql.append("       l.wr_labourname,\n");  
	   sql.append("       decode(p.is_gua,0,'否',1,'是') is_gua,\n");  
	   sql.append("       p.producer_name,\n");  
	   sql.append("       decode(p.is_agree,10041001,'同意',10041002,'不同意','不用授权') is_agree,\n");  
	   sql.append("       tc2.code_desc status,\n");  
	   sql.append("       a.trouble_desc\n");  
	   sql.append("  from tt_as_wr_application   a,\n");  
	   sql.append("       tt_as_wr_partsitem     p,\n");  
	   sql.append("       tt_as_wr_labouritem    l,\n");  
	   sql.append("       tm_vehicle             v,\n");  
	   sql.append("       tm_vhcl_material_group g,\n");  
	   sql.append("       tt_dealer_actual_sales s,\n");  
	   sql.append("       tt_customer            c,\n");  
	   sql.append("       tm_dealer              d,\n");
	   sql.append("       tc_code                tc1,\n");
	   sql.append("       tc_code                tc2\n");
	   sql.append(" where v.model_id = g.group_id\n");  
	   sql.append("   and a.vin = v.vin\n");  
	   sql.append("   and a.id = p.id\n");  
	   sql.append("   and a.id = l.id\n");  
	   sql.append("   and tc1.code_id = a.claim_type\n");
	   sql.append("   and tc2.code_id = a.status\n"); 
	   sql.append("   and l.wr_labourcode = p.wr_labourcode\n");  
	   sql.append("   and s.vehicle_id(+) = v.vehicle_id\n");  
	   sql.append("   and s.ctm_id = c.ctm_id(+)\n");  
	   sql.append("   and a.dealer_id = d.dealer_id\n");  
	   sql.append("   and v.vin = '").append(vin).append("'\n");
	   return this.pageQuery(sql.toString(), null,this.getFunName()) ;
   }
   
	public List<Map<String, Object>> baoyang(String vin) {
		StringBuffer sql = new StringBuffer("\n");
		sql.append("select rownum,\n");
		sql.append("       v.vin,\n");
		sql.append("       o.in_mileage,\n");
		sql.append("       o.free_times,\n");
		sql.append("       c.ctm_name,\n");
		sql.append("       v.engine_no,\n");
		sql.append("       d.dealer_name,\n");
		sql.append("       to_char(o.ro_create_date,'yyyy-MM-dd') ro_date,\n");
		sql.append("       to_char(v.product_date,'yyyy-MM-dd') product_date,\n");
		sql.append("       to_char(v.purchased_date,'yyyy-MM-dd') purchased_date,\n");
		sql.append("       tc.code_desc status\n");
		sql.append("  from tt_as_repair_order     o,\n");
		sql.append("       tm_vehicle             v,\n");
		sql.append("       tt_dealer_actual_sales s,\n");
		sql.append("       tt_customer            c,\n");
		sql.append("       tm_dealer              d,\n");
		sql.append("       tc_code                tc\n");
		sql.append(" where o.vin = v.vin\n");
		sql.append("   and v.vehicle_id = s.vehicle_id(+)\n");
		sql.append("   and s.ctm_id = c.ctm_id(+)\n");
		sql.append("   and o.dealer_id = d.dealer_id\n");
		sql.append("   and tc.code_id = o.ro_status\n");
		sql.append("   and o.repair_type_code = 11441004\n");
		sql.append("   and nvl(o.order_valuable_type,0) != 13591002\n");
		sql.append("   and v.vin = '").append(vin).append("'\n");
		return this.pageQuery(sql.toString(), null, this.getFunName());
	}
	
	public List<Map<String,Object>> fuwuhuodong(String vin){
		StringBuffer sql = new StringBuffer("\n") ;
		sql.append("select rownum,\n");
		sql.append("       g.group_code,\n");  
		sql.append("       a.vin,\n");  
		sql.append("       v.engine_no,\n");  
		sql.append("       a.in_mileage,\n");  
		sql.append("       o.cam_code,\n");  
		sql.append("       ta.activity_name,\n");  
		sql.append("       tc1.code_desc activity_type,\n");  
		sql.append("       o.free_times,\n");  
		sql.append("       to_char(o.ro_create_date, 'yyyy-MM-dd') ro_date,\n");  
		sql.append("       to_char(v.purchased_date, 'yyyy-MM-dd') purchased_date,\n");  
		sql.append("       c.ctm_name,\n");  
		sql.append("       d.dealer_name,\n");  
		sql.append("       c.main_phone || ' ' || c.other_phone phone,\n");  
		sql.append("       tc2.code_desc status\n");  
		sql.append("  from tt_as_wr_application   a,\n");  
		sql.append("       tt_as_repair_order     o,\n");  
		sql.append("       tt_dealer_actual_sales s,\n");  
		sql.append("       tt_customer            c,\n");  
		sql.append("       tm_vehicle             v,\n");  
		sql.append("       tm_dealer              d,\n");  
		sql.append("       tm_vhcl_material_group g,\n");  
		sql.append("       tt_as_activity         ta,\n");  
		sql.append("       tc_code                tc1,\n");  
		sql.append("       tc_code                tc2\n");  
		sql.append(" where a.ro_no = o.ro_no\n");  
		sql.append("   and o.vin = v.vin\n");  
		sql.append("   and v.vehicle_id = s.vehicle_id(+)\n");  
		sql.append("   and s.ctm_id = c.ctm_id(+)\n");  
		sql.append("   and o.dealer_id = d.dealer_id\n");  
		sql.append("   and v.model_id = g.group_id\n");  
		sql.append("   and o.cam_code = ta.activity_code\n");  
		sql.append("   and ta.activity_type = tc1.code_id\n");  
		sql.append("   and o.ro_status = tc2.code_id\n");
		sql.append("   and a.claim_type = 10661006\n");
		sql.append("   and v.vin = '").append(vin).append("'\n");
		return this.pageQuery(sql.toString(), null, this.getFunName());
	}
	
   public  String  conversionCode(String code){
	   String t_code="";
    	StringBuffer sql= new StringBuffer();
    	sql.append("select code_desc from tc_code  where code_id="+code+"");
    	if(!code.equals("--")){
    	List<Map<String,Object>> list = this.pageQuery(sql.toString(), null, this.getFunName());
    	if(list.size()>0){
    		t_code=list.get(0).get("CODE_DESC").toString();	
    	}
    	
    	}
    	else{
    		t_code = code;
    	}
    	return t_code;
    }
   
   
   /**
    * 集团客户实销审核DAO 
    * @param map 参数列表
    * */
   public List<Map<String,Object>> getActualSalesReport(Map<String, String> map) {
	   
	    String produce = map.get("produce");
	   	String startTime = map.get("startTime");
		String endTime = map.get("endTime");
		String dealerCode = map.get("dealerCode");
		String orgCode = map.get("orgCode");
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT VOD.ROOT_ORG_NAME,\n");
		sql.append("       VOD.REGION_NAME,\n");  
		sql.append("       TO_CHAR(TRUNC(TDASL.LOG_DATE),'yyyy-MM-dd') AUDITTIME,\n");  
		sql.append("       VOD.DEALER_NAME,\n");  
		sql.append("       TFC.BUY_FROM,\n");  
		sql.append("       TVM.MATERIAL_NAME,\n");  
		sql.append("       DECODE(TBA.ERP_CODE, 726, '重庆', 82, '重庆', 142, '河北', 197, '南京') CODE_DESC,\n");  
		sql.append("              TFC.CONTRACT_NO,\n");  
		sql.append("              TDAS.FLEET_ID,\n");  
		sql.append("              TDASL.LOG_STATUS,\n");  
		sql.append("              TFC.STATUS,\n");  
		sql.append("              count(1) ACTAMOUNT\n");  
		sql.append("  FROM TT_DEALER_ACTUAL_SALES     TDAS,\n");  
		sql.append("       TT_DEALER_ACTUAL_SALES_LOG TDASL,\n");  
		sql.append("       VW_ORG_DEALER              VOD,\n");  
		sql.append("       TT_FLEET_CONTRACT          TFC,\n");  
		sql.append("       TM_VEHICLE                 TMV,\n");  
		sql.append("       TM_VHCL_MATERIAL           TVM,\n");  
		sql.append("       TM_BUSINESS_AREA TBA\n");  
		sql.append(" WHERE TDAS.ORDER_ID = TDASL.ORDER_ID\n");  
		sql.append("   AND VOD.DEALER_ID = TDAS.DEALER_ID\n");  
		sql.append("   AND TDAS.IS_FLEET=10041001\n");  
		sql.append("   AND TBA.AREA_ID=TMV.AREA_ID\n");  
		sql.append("   AND TFC.CONTRACT_NO(+) = TDAS.CONTRACT_NO\n");  
		sql.append("   AND TMV.LIFE_CYCLE = 10321004\n");  
		sql.append("   AND TMV.VEHICLE_ID = TDAS.VEHICLE_ID\n");  
		sql.append("   AND TMV.MATERIAL_ID = TVM.MATERIAL_ID\n");
		
		if(!CommonUtils.isNullString(endTime)) {
			if(CommonUtils.isNullString(startTime)) {
				sql.append("	and TDASL.LOG_DATE <= to_date('");
				sql.append(endTime);
				sql.append("', 'yyyy-MM-dd HH24:MI:SS')\n");
			} else {
				sql.append("	and TDASL.LOG_DATE >= to_date('");
				sql.append(startTime);
				sql.append("', 'yyyy-MM-dd HH24:MI:SS')\n");
				sql.append("	and TDASL.LOG_DATE <= to_date('");
				sql.append(endTime);
				sql.append("', 'yyyy-MM-dd HH24:MI:SS')\n");
			}
		}
		if(!CommonUtils.isNullString(dealerCode)) {
			sql.append("and VOD.DEALER_CODE in (");
			String[] arr = dealerCode.split(",");
			StringBuffer dc = new StringBuffer();
			for(int i=0; i<arr.length; i++) {
				dc.append("'").append(arr[i]).append("'");
				if(i != arr.length-1) {
					dc.append(",");
				}
			}
			sql.append(dc.toString());
			sql.append(")\n");
		}
		if(!CommonUtils.isNullString(orgCode)) {
			sql.append("and VOD.ROOT_ORG_CODE = '");
			/*String[] arr = orgCode.split(",");
			StringBuffer roc = new StringBuffer();
			for(int i=0; i<arr.length; i++) {
				roc.append("'").append(arr[i]).append("'");
			}
			sql.append(roc.toString());*/
			sql.append(orgCode);
			sql.append("'\n");
		}
		if(!CommonUtils.isNullString(produce)) {
			sql.append("AND DECODE(TBA.ERP_CODE,726,'重庆',82,'重庆',142,'河北',197,'南京') = '");
			sql.append(produce);
			sql.append("'\n");
		}
		
		sql.append("GROUP BY VOD.ROOT_ORG_NAME,\n");
		sql.append("       VOD.REGION_NAME,\n");  
		sql.append("       TRUNC(TDASL.LOG_DATE),\n");  
		sql.append("       VOD.DEALER_NAME,TDAS.FLEET_ID,TFC.STATUS,\n");  
		sql.append("       TFC.BUY_FROM,TBA.ERP_CODE,TDASL.LOG_STATUS,\n");  
		sql.append("       TVM.MATERIAL_NAME,TFC.CONTRACT_NO\n");
		sql.append("	order by VOD.ROOT_ORG_NAME,\n");
		sql.append("     VOD.REGION_NAME\n");

		return  dao.pageQuery(sql.toString(), null, dao.getFunName());
   }
   
   
   /**
    * 集团客户实销DAO
    * @param map 参数列表
    * */
   public List<Map<String,Object>> getActualSalesLogReport(Map<String, String> map) {
	   
	    String fleetName = map.get("fleetName");
	   	String startTime = map.get("startTime");
		String endTime = map.get("endTime");
		String dealerCode = map.get("dealerCode");
		String orgCode = map.get("orgCode");
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT XX.ROOT_ORG_NAME,\n");
		sql.append("XX.REGION_NAME,\n");  
		sql.append("XX.DEALER_NAME,\n");  
		sql.append("XX.MATERIAL_NAME,\n");  
		sql.append("XX.FLEET_NAME,\n");  
		sql.append("XX.CODE_DESC,\n");  
		sql.append("XX.AA ACTAMOUNT FROM (\n");  
		sql.append("SELECT VOD.ROOT_ORG_NAME,\n");  
		sql.append("       VOD.REGION_NAME,\n");  
		sql.append("       TTS.IS_FLEET,\n");  
		sql.append("       VOD.DEALER_NAME,\n");  
		sql.append("       M.MATERIAL_NAME,\n");  
		sql.append("       　　TF.FLEET_NAME,\n");  
		sql.append("       DECODE(TBA.ERP_CODE,\n");  
		sql.append("              726,\n");  
		sql.append("              '重庆',\n");  
		sql.append("              82,\n");  
		sql.append("              '重庆',\n");  
		sql.append("              142,\n");  
		sql.append("              '河北',\n");  
		sql.append("              197,\n");  
		sql.append("              '南京') CODE_DESC,\n");  
		sql.append("       COUNT(1) AA\n");  
		sql.append("  FROM TT_DEALER_ACTUAL_SALES TTS,\n");  
		sql.append("       TM_DEALER              TMD,\n");  
		sql.append("       TT_CUSTOMER            TTC,\n");  
		sql.append("       TM_VEHICLE             TMV,\n");  
		sql.append("       TM_BUSINESS_AREA       TBA,\n");  
		sql.append("       TM_VHCL_MATERIAL       M,\n");  
		sql.append("       TM_FLEET               TF,\n");  
		sql.append("       TT_FLEET_CONTRACT      TFC,\n");  
		sql.append("       VW_ORG_DEALER          VOD\n");  
		sql.append(" WHERE TTS.DEALER_ID = TMD.DEALER_ID\n");  
		sql.append("   AND VOD.DEALER_ID = TMD.DEALER_ID\n");  
		sql.append("   AND TBA.AREA_ID = TMV.AREA_ID\n");  
		sql.append("   AND TTS.CTM_ID = TTC.CTM_ID\n");  
		sql.append("   AND TTS.VEHICLE_ID = TMV.VEHICLE_ID\n");  
		sql.append("   AND TMV.MATERIAL_ID = M.MATERIAL_ID\n");  
		sql.append("   AND TTS.FLEET_ID = TF.FLEET_ID(+)\n");  
		sql.append("   AND TTS.IS_RETURN = '10041002'\n");  
		sql.append("   AND TTS.CONTRACT_ID = TFC.CONTRACT_ID(+)\n");  
		sql.append("   AND TTS.IS_FLEET = 10041001\n");  
		sql.append("   AND TMV.AREA_ID IN\n");  
		sql.append("       (4011012651232877, 2010010100000001, 2010010100000002,\n");  
		sql.append("        2010010100000003, 2010010100000004, 2010010100000005,\n");  
		sql.append("        2010010100000006, 2010010100000007, 2010010100000008)\n");  
		sql.append("   AND TMV.LIFE_CYCLE = 10321004\n");

		
		//TODO 添加条件
		if(!CommonUtils.isNullString(orgCode)) {
			sql.append("and VOD.ROOT_ORG_CODE = '");
			sql.append(orgCode);
			sql.append("'\n");
		}
		
		if(!CommonUtils.isNullString(fleetName)) {
			sql.append("AND TF.FLEET_NAME LIKE  '%");
			sql.append(fleetName);
			sql.append("%'\n");
		}
		
		if(!CommonUtils.isNullString(dealerCode)) {
			sql.append("and VOD.DEALER_CODE in (");
			String[] arr = dealerCode.split(",");
			StringBuffer dc = new StringBuffer();
			for(int i=0; i<arr.length; i++) {
				dc.append("'").append(arr[i]).append("'");
				if(i != arr.length-1) {
					dc.append(",");
				}
			}
			sql.append(dc.toString());
			sql.append(")\n");
		}
		
		if(!CommonUtils.isNullString(endTime)) {
			if(CommonUtils.isNullString(startTime)) {
				sql.append("	and TTS.SALES_DATE <= to_date('");
				sql.append(endTime);
				sql.append("', 'yyyy-MM-dd HH24:MI:SS')\n");
			} else {
				sql.append("	and TTS.SALES_DATE >= to_date('");
				sql.append(startTime);
				sql.append("', 'yyyy-MM-dd HH24:MI:SS')\n");
				sql.append("	and TTS.SALES_DATE <= to_date('");
				sql.append(endTime);
				sql.append("', 'yyyy-MM-dd HH24:MI:SS')\n");
			}
		}
		
		sql.append("GROUP BY VOD.ROOT_ORG_NAME,\n");
		sql.append("          VOD.REGION_NAME,TF.FLEET_NAME,\n");  
		sql.append("          VOD.DEALER_NAME,TTS.IS_FLEET,\n");  
		sql.append("          TBA.ERP_CODE,\n");  
		sql.append("          M.MATERIAL_NAME\n");  
		sql.append("UNION\n");  
		sql.append("SELECT VOD.ROOT_ORG_NAME,\n");  
		sql.append("       VOD.REGION_NAME,TTS.IS_FLEET,\n");  
		sql.append("       VOD.DEALER_NAME,\n");  
		sql.append("       M.MATERIAL_NAME,\n");  
		sql.append("       TCP.PACT_NAME FLEET_NAME,\n");  
		sql.append("       DECODE(TBA.ERP_CODE,\n");  
		sql.append("              726,\n");  
		sql.append("              '重庆',\n");  
		sql.append("              82,\n");  
		sql.append("              '重庆',\n");  
		sql.append("              142,\n");  
		sql.append("              '河北',\n");  
		sql.append("              197,\n");  
		sql.append("              '南京') CODE_DESC,\n");  
		sql.append("      COUNT(1) AA\n");  
		sql.append("  FROM TT_DEALER_ACTUAL_SALES TTS,\n");  
		sql.append("       TM_DEALER              TMD,\n");  
		sql.append("       TT_CUSTOMER            TTC,\n");  
		sql.append("       TM_VEHICLE             TMV,\n");  
		sql.append("       TM_BUSINESS_AREA       TBA,\n");  
		sql.append("       TM_VHCL_MATERIAL       M,\n");  
		sql.append("       TM_FLEET               TF,\n");  
		sql.append("       TT_FLEET_CONTRACT      TFC,\n");  
		sql.append("       VW_ORG_DEALER          VOD,TM_COMPANY_PACT TCP\n");  
		sql.append(" WHERE TTS.DEALER_ID = TMD.DEALER_ID\n");  
		sql.append("   AND VOD.DEALER_ID = TMD.DEALER_ID\n");  
		sql.append("   AND TBA.AREA_ID = TMV.AREA_ID\n");  
		sql.append("   AND TTS.CTM_ID = TTC.CTM_ID\n");  
		sql.append("   AND TTS.VEHICLE_ID = TMV.VEHICLE_ID\n");  
		sql.append("   AND TMV.MATERIAL_ID = M.MATERIAL_ID\n");  
		sql.append("   AND TTS.FLEET_ID = TF.FLEET_ID(+)\n");  
		sql.append("   AND TTS.IS_RETURN = '10041002'\n");  
		sql.append("   AND TTS.FLEET_ID = TCP.PACT_ID\n");  
		sql.append("   AND TTS.CONTRACT_ID = TFC.CONTRACT_ID(+)\n");  
		sql.append("   AND TTS.IS_FLEET = 10041001\n");  
		sql.append("   AND TMV.AREA_ID IN\n");  
		sql.append("       (4011012651232877, 2010010100000001, 2010010100000002,\n");  
		sql.append("        2010010100000003, 2010010100000004, 2010010100000005,\n");  
		sql.append("        2010010100000006, 2010010100000007, 2010010100000008)\n");  
		sql.append("   AND TMV.LIFE_CYCLE = 10321004\n");

		//TODO 添加条件
		if(!CommonUtils.isNullString(orgCode)) {
			sql.append("and VOD.ROOT_ORG_CODE = '");
			sql.append(orgCode);
			sql.append("'\n");
		}
		
		if(!CommonUtils.isNullString(fleetName)) {
			sql.append("AND VOD.DEALER_NAME LIKE  '%");
			sql.append(fleetName);
			sql.append("%'\n");
		}
		
		if(!CommonUtils.isNullString(dealerCode)) {
			sql.append("and VOD.DEALER_CODE in (");
			String[] arr = dealerCode.split(",");
			StringBuffer dc = new StringBuffer();
			for(int i=0; i<arr.length; i++) {
				dc.append("'").append(arr[i]).append("'");
				if(i != arr.length-1) {
					dc.append(",");
				}
			}
			sql.append(dc.toString());
			sql.append(")\n");
		}
		
		if(!CommonUtils.isNullString(endTime)) {
			if(CommonUtils.isNullString(startTime)) {
				sql.append("	and TTS.SALES_DATE <= to_date('");
				sql.append(endTime);
				sql.append("', 'yyyy-MM-dd HH24:MI:SS')\n");
			} else {
				sql.append("	and TTS.SALES_DATE >= to_date('");
				sql.append(startTime);
				sql.append("', 'yyyy-MM-dd HH24:MI:SS')\n");
				sql.append("	and TTS.SALES_DATE <= to_date('");
				sql.append(endTime);
				sql.append("', 'yyyy-MM-dd HH24:MI:SS')\n");
			}
		}
		
		sql.append("GROUP BY VOD.ROOT_ORG_NAME,\n");
		sql.append("          VOD.REGION_NAME,\n");  
		sql.append("          TCP.PACT_NAME,\n");  
		sql.append("          VOD.DEALER_NAME,TTS.IS_FLEET,\n");  
		sql.append("          TBA.ERP_CODE,\n");  
		sql.append("          M.MATERIAL_NAME\n");  
		sql.append("  ) xx\n");  
		sql.append("   WHERE (XX.IS_FLEET=10041001 AND XX.FLEET_NAME IS NOT NULL) OR XX.IS_FLEET=10041002\n");  
		sql.append("group by XX.ROOT_ORG_NAME,\n");  
		sql.append("XX.REGION_NAME,\n");  
		sql.append("XX.DEALER_NAME,\n");  
		sql.append("XX.MATERIAL_NAME,\n");  
		sql.append("XX.FLEET_NAME,\n");  
		sql.append("XX.CODE_DESC,\n");  
		sql.append("XX.AA\n");
		sql.append("order by  XX.ROOT_ORG_NAME,XX.REGION_NAME\n");

		return  dao.pageQuery(sql.toString(), null, dao.getFunName());
  }
   
   
   /**
    * 常规订单超时统计汇总表查询DAO
    * @param map 参数列表
    * */
   public List<Map<String,Object>> getTimeoutStatisticsSummaryReport(Map<String, String> map) {
	   
	   /*	String startTime = map.get("startTime");
		String endTime = map.get("endTime");*/
		String dealerId = map.get("dealerId");
		String orgId = map.get("orgId");
		String price = map.get("price");
		StringBuffer sql = new StringBuffer();
		
		
		sql.append("SELECT D.ORG_NAME, --大区\n");
		sql.append("       D.REGION_NAME, --省份\n");  
		sql.append("       D.DEALER_NAME, --单位名称\n");  
		sql.append("       D.ORDER_WEEKLY, --订单周度\n");  
		sql.append("       SUM(D.AMOUNT) AMOUNT, --数量\n");  
		sql.append("       D.OVERTIME_DAYS, --当前超期天数\n");  
		sql.append("       D.TOTAL_OVERTIME_DAYS, --总超期天数\n");  
		sql.append("       (D.TOTAL_OVERTIME_DAYS * SUM(D.AMOUNT) * NVL(P.PAYMENT_PRICE, "+price+")) TOTAL_PRICE, --总超期金额\n");  
		sql.append("       (D.OVERTIME_DAYS * SUM(D.AMOUNT)* NVL(P.PAYMENT_PRICE, "+price+")) PAYMENT_PRICE --当期扣款金额\n");  
		sql.append("  FROM TT_VS_ORDER_TOS_DETAIL    D,\n");  
		sql.append("       TT_VS_ORDER_PAYMENT_PRICE P,\n");  
		sql.append("(SELECT TDS.SET_YEAR,\n");
		sql.append("               TDS.SET_MONTH,\n");  
		sql.append("               TDS.SET_WEEK,\n");  
		sql.append("               MAX(TDS.SET_DATE) SET_DATE\n");  
		sql.append("          FROM TM_DATE_SET TDS\n");  
		sql.append("         GROUP BY TDS.SET_YEAR, TDS.SET_MONTH, TDS.SET_WEEK) TDS\n");  
		sql.append("         WHERE 1 = 1\n");  
		sql.append("      AND TDS.SET_YEAR = P.PAYMENT_YEAR(+)\n");  
		sql.append("      AND TDS.SET_MONTH = P.PAYMENT_MONTH(+)\n");  
		sql.append("      AND D.ORDER_WEEKLY = TDS.SET_YEAR || TDS.SET_WEEK\n");
		if(!CommonUtils.isNullString(orgId)) {
			sql.append(" and d.ORG_ID = ");
			sql.append(orgId);
			sql.append("\n");
		}
		
		if(!CommonUtils.isNullString(dealerId)) {
			sql.append(" and d.DEALER_ID in (");
			sql.append(dealerId);
			sql.append(")\n");
		}
		sql.append(" GROUP BY D.ORG_NAME, --大区\n");  
		sql.append("          D.REGION_NAME, --省份\n");  
		sql.append("          D.DEALER_NAME, --单位名称\n");  
		sql.append("          D.ORDER_WEEKLY, --订单周度\n");
		sql.append("       	  D.OVERTIME_DAYS, --当前超期天数\n");  
		sql.append("       	  D.TOTAL_OVERTIME_DAYS, --总超期天数\n");  
		sql.append("          P.PAYMENT_PRICE --当期扣款金额\n");  
		sql.append(" ORDER BY D.ORG_NAME, D.REGION_NAME, D.DEALER_NAME\n");
		return  dao.pageQuery(sql.toString(), null, dao.getFunName());
  }
   

   /**
    * 常规订单超时统计明细表查询DAO
    * @param map 参数列表
    * */
   public List<Map<String,Object>> getTimeoutStatisticsDetailReport(Map<String, String> map) {
	   
	   /*	String startTime = map.get("startTime");
		String endTime = map.get("endTime");*/
		String dealerId = map.get("dealerId");
		String orgId = map.get("orgId");
		String price = map.get("price");
		StringBuffer sql = new StringBuffer();
		
		
		sql.append("SELECT D.ORG_NAME, --大区\n");
		sql.append("       D.REGION_NAME, --省份\n");  
		sql.append("       D.DEALER_NAME, --单位名称\n");  
		sql.append("       D.ORDER_WEEKLY, --订单周度\n");  
		sql.append("       D.ORDER_NO, --常规订单号\n");  
		sql.append("       D.MATERIAL_CODE, --车型编码\n");  
		sql.append("       D.AMOUNT, --数量\n");  
		sql.append("       D.OVERTIME_DAYS, --当前超期天数\n");  
		sql.append("       D.OVERTIME_DAYS * NVL(P.PAYMENT_PRICE, "+price+"), --当前超期金额\n");  
		sql.append("       D.TOTAL_OVERTIME_DAYS, --总超期天数\n");  
		sql.append("       (D.TOTAL_OVERTIME_DAYS * D.AMOUNT * NVL(P.PAYMENT_PRICE, "+price+")) TOTAL_PRICE, --总超期金额\n");  
		sql.append("	   (D.OVERTIME_DAYS * D.AMOUNT * NVL(P.PAYMENT_PRICE, "+price+")) PAYMENT_PRICE \n");
		sql.append("  FROM TT_VS_ORDER_TOS_DETAIL D,\n");  
		sql.append("       TT_VS_ORDER_PAYMENT_PRICE P,\n");  
		sql.append("       (SELECT TDS.SET_YEAR,\n");  
		sql.append("               TDS.SET_MONTH,\n");  
		sql.append("               TDS.SET_WEEK,\n");  
		sql.append("               MAX(TDS.SET_DATE) SET_DATE\n");  
		sql.append("          FROM TM_DATE_SET TDS\n");  
		sql.append("         GROUP BY TDS.SET_YEAR, TDS.SET_MONTH, TDS.SET_WEEK) TDS\n");  
		sql.append(" WHERE 1 = 1\n");  
		sql.append("   AND TDS.SET_YEAR = P.PAYMENT_YEAR(+)\n");  
		sql.append("   AND TDS.SET_MONTH = P.PAYMENT_MONTH(+)\n");  
		sql.append("   AND D.ORDER_WEEKLY = TDS.SET_YEAR || TDS.SET_WEEK\n");  
		if(!CommonUtils.isNullString(orgId)) {
			sql.append(" and d.ORG_ID = ");
			sql.append(orgId);
			sql.append("\n");
		}
		
		if(!CommonUtils.isNullString(dealerId)) {
			sql.append(" and d.DEALER_ID in (");
			sql.append(dealerId);
			sql.append(")\n");
		}
		sql.append(" ORDER BY D.ORG_NAME, D.REGION_NAME, D.DEALER_NAME\n");
		return  dao.pageQuery(sql.toString(), null, dao.getFunName());
  }
   
   /**
    * 常规订单资源延时满足明细表查询DAO
    * @param map 参数列表
    * */
   public List<Map<String,Object>> getDealyDetailReport(Map<String, String> map) {
	   
        String startYear = map.get("startYear");
        String startMonth = map.get("startMonth");
        String endYear = map.get("endYear");
        String endMonth = map.get("endMonth");
		String dealerId = map.get("dealerId");
		String orgId = map.get("orgId");
		String orderNo = map.get("orderNo");
		String deliveryNo = map.get("deliveryNo");
		StringBuffer sql = new StringBuffer();
		
		
		sql.append("SELECT D.ORG_NAME, --大区\n");
		sql.append("       D.REGION_NAME, --省份\n");  
		sql.append("       D.DEALER_NAME, --单位名称\n");  
		sql.append("       D.ORDER_WEEKLY, --订单周度\n");  
		sql.append("       D.ORDER_NO, --常规订单号\n");  
		sql.append("       D.DELIVERY_NO, --发运申请号\n");  
		sql.append("       D.MATERIAL_CODE, --车型编码\n");  
		sql.append("       D.AMOUNT, --数量\n");  
		sql.append("       D.TOTAL_OVERTIME_DAYS --总超期天数\n");  
		sql.append("  FROM TT_VS_ORDER_DELAY_DETAIL D\n");  
		sql.append(" WHERE 1 = 1\n");  
		if(!CommonUtils.isNullString(endYear) && !CommonUtils.isNullString(endMonth)
				&& !CommonUtils.isNullString(startYear) && !CommonUtils.isNullString(startMonth)) {
				sql.append("	and D.DELAY_YEAR >= ");
				sql.append(startYear);
				sql.append("	and D.DELAY_MONTH >= ");
				sql.append(startMonth);
				sql.append("	and D.DELAY_YEAR <= ");
				sql.append(endYear);
				sql.append(" 	and D.DELAY_MONTH <= ");
				sql.append(endMonth);
				sql.append("\n");
		}
		
		if(!CommonUtils.isNullString(orgId)) {
			sql.append(" and d.ORG_ID = ");
			sql.append(orgId);
			sql.append("\n");
		}
		
		if(!CommonUtils.isNullString(dealerId)) {
			sql.append(" and d.dealer_id in (");
			sql.append(dealerId);
			sql.append(")\n");
		}
		
		if(!CommonUtils.isNullString(deliveryNo)) {
			sql.append(" and d.DELIVERY_NO like '%");
			sql.append(deliveryNo);
			sql.append("%'\n");
		}
		
		if(!CommonUtils.isNullString(orderNo)) {
			sql.append(" and d.ORDER_NO like '%");
			sql.append(orderNo);
			sql.append("%'\n");
		}
		sql.append(" ORDER BY D.ORG_NAME, D.REGION_NAME, D.DEALER_NAME\n");
		return  dao.pageQuery(sql.toString(), null, dao.getFunName());
  }
   
   /**
    * 常规订单资源延时满足汇总表查询DAO
    * @param map 参数列表
    * */
   public List<Map<String,Object>> getDealySummaryReport(Map<String, String> map) {
	   
        String startYear = map.get("startYear");
        String startMonth = map.get("startMonth");
        String endYear = map.get("endYear");
        String endMonth = map.get("endMonth");
		String dealerId = map.get("dealerId");
		String orgId = map.get("orgId");
		StringBuffer sql = new StringBuffer();
		
		
		sql.append("SELECT D.ORG_NAME, --大区\n");
		sql.append("       D.REGION_NAME, --省份\n");  
		sql.append("       D.DEALER_NAME, --单位名称\n");  
		sql.append("       D.ORDER_WEEKLY, --订单周度\n");  
		sql.append("       D.MATERIAL_CODE, --物料编码\n");  
		sql.append("       SUM(D.AMOUNT) AMOUNT, --数量\n");  
		sql.append("       D.TOTAL_OVERTIME_DAYS --总超期天数\n");  
		sql.append("  FROM TT_VS_ORDER_DELAY_DETAIL D\n");  
		sql.append(" WHERE 1 = 1\n");  
		if(!CommonUtils.isNullString(endYear) && !CommonUtils.isNullString(endMonth)
				&& !CommonUtils.isNullString(startYear) && !CommonUtils.isNullString(startMonth)) {
				sql.append("	and D.DELAY_YEAR >= ");
				sql.append(startYear);
				sql.append("	and D.DELAY_MONTH >= ");
				sql.append(startMonth);
				sql.append("	and D.DELAY_YEAR <= ");
				sql.append(endYear);
				sql.append(" 	and D.DELAY_MONTH <= ");
				sql.append(endMonth);
				sql.append("\n");
		}
		
		if(!CommonUtils.isNullString(orgId)) {
			sql.append(" and d.ORG_ID = ");
			sql.append(orgId);
			sql.append("\n");
		}
		if(!CommonUtils.isNullString(dealerId)) {
			sql.append(" and d.dealer_id in (");
			sql.append(dealerId);
			sql.append(")\n");
		}
		sql.append(" GROUP BY D.ORG_NAME, --大区\n");  
		sql.append("          D.REGION_NAME, --省份\n");  
		sql.append("          D.DEALER_NAME, --单位名称\n");  
		sql.append("          D.ORDER_WEEKLY, --订单周度\n");
		sql.append("          D.MATERIAL_CODE, --物料编码\n");  
		sql.append("       	  D.TOTAL_OVERTIME_DAYS --总超期天数\n");  
		sql.append(" ORDER BY D.ORG_NAME, D.REGION_NAME, D.DEALER_NAME\n");
		return  dao.pageQuery(sql.toString(), null, dao.getFunName());
  }

}
