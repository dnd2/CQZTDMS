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
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;


public class InterfaceDataQueryDao extends BaseDao<PO>{
	public static Logger logger = Logger.getLogger(InterfaceDataQueryDao.class);
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
			sql.append("       s.declare_sum1,\n" );
			sql.append("       s.vin,\n" );
			sql.append("       case\n" );
			sql.append("       when s.vin is not null then '1'\n" );
			sql.append("       else '0' end as cou,\n" );
			sql.append("       mg.group_code,\n" );
			sql.append("       mg1.group_name,s.declare_sum1 as average\n" );
			sql.append("  from Tt_As_Wr_Spefee        s,\n" );
			sql.append("       vw_org_dealer_service  d,\n" );
			sql.append("       tc_code                c,\n" );
			sql.append("       tm_vehicle             v,\n" );
			sql.append("       tm_vhcl_material_group mg,\n" );
			sql.append("       tm_vhcl_material_group mg1\n" );
			sql.append(" where s.fee_type = "+Constant.FEE_TYPE_01+"\n" );
			sql.append("   and s.status in ("+Constant.SPEFEE_STATUS_04+", "+Constant.SPEFEE_STATUS_06+","+Constant.SPEFEE_STATUS_07+")\n" );
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
			sql.append(" union all \n " );
			sql.append("select d.dealer_name,\n" );
			sql.append("       d.dealer_code,\n" );
			sql.append("       d.root_org_name,\n" );
			sql.append("       c.code_desc,\n" );
			sql.append("       s.fee_no,\n" );
			sql.append("       s.declare_sum1,\n" );
			sql.append("       ps.vin,\n" );
			sql.append("       to_char((select count(1) from Tt_As_Wr_Spefee_Claim sc where sc.fee_id = s.id)) as cou,\n" );
			sql.append("       mg.group_code,\n" );
			sql.append("       mg1.group_name,s.declare_sum1/(select count(1) from Tt_As_Wr_Spefee_Claim sc where sc.fee_id = s.id) as  average\n" );
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
		}else{
			if(bean.getFeeType().equals(Constant.FEE_TYPE_01)){
				sql.append("select d.dealer_name,\n" );
				sql.append("       d.dealer_code,\n" );
				sql.append("       d.root_org_name,\n" );
				sql.append("       c.code_desc,\n" );
				sql.append("       s.fee_no,\n" );
				sql.append("       s.declare_sum1,\n" );
				sql.append("       s.vin,\n" );
				sql.append("       case\n" );
				sql.append("       when s.vin is not null then '1'\n" );
				sql.append("       else '0' end as cou,\n" );
				sql.append("       mg.group_code,\n" );
				sql.append("       mg1.group_name,s.declare_sum1 as average\n" );
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
				sql.append("       mg1.group_name,s.declare_sum1/(select count(1) from Tt_As_Wr_Spefee_Claim sc where sc.fee_id = s.id) as  average\n" );
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
	public PageResult<Map<String, Object>> queryDraftFaxStatus(String beginTime,String endTime,String draftCode,String draftType,String draftStatus,String dealerERPcode ,int curPage, int pageSize){
		StringBuffer sql= new StringBuffer();
		sql.append("select dri.draft_code,\n" );
		sql.append("       dri.dealer_erp_code,\n" );
		sql.append("       dri.draft_money,\n" );
		sql.append(" decode(dri.ca_erp_org_code,726,'重庆轿车',82,'重庆微车',197,'南京微车',142,'河北微车',647,'新疆结算中心') ca_erp_org_code,\n" );
		sql.append(" decode(dri.draft_type,0,'承兑汇票',1,'三方信贷',2,'兵财融资') draft_type,\n" );
		sql.append("       dri.draft_bank,\n" );
		sql.append("       dri.draft_bank_name,\n" );
		sql.append(" to_char(dri.fax_receive_date,'YYYY-MM-DD') fax_receive_date,\n" );
		sql.append(" to_char(dri.original_receive_date,'YYYY-MM-DD') original_receive_date,\n" );
		sql.append(" decode(dri.receive_status,0,'传真件',1,'原件') receive_status,\n" );
		sql.append("       dri.dms_handle_flag,\n" );
		sql.append(" to_char(dri.create_date,'YYYY-MM-DD') create_date,\n" );
		sql.append("       dri.remark,\n" );
		sql.append("       dri.attribute1\n" );
		sql.append(" from t_draft_receive_info dri \n" );
		sql.append(" where 1=1");

		if(Utility.testString(beginTime)){
			sql.append(" AND dri.fax_receive_date >= TO_DATE('").append(beginTime).append("', 'YYYY-MM-DD HH24:MI:SS')\n");
		}
		if(Utility.testString(endTime)){
			sql.append(" AND dri.fax_receive_date <= TO_DATE('").append(endTime).append("', 'YYYY-MM-DD HH24:MI:SS')\n");
		}
		if(Utility.testString(draftCode)){
			sql.append(" AND dri.draft_code = '").append(draftCode).append("'\n");
		}
		if(Utility.testString(draftType)){
			sql.append(" AND dri.draft_type = '").append(draftType).append("'\n");
		}
		if(Utility.testString(draftStatus)){
			sql.append(" AND dri.receive_status = '").append(draftStatus).append("'\n");
		}
		if(Utility.testString(dealerERPcode)){
			sql.append(" AND dri.dealer_erp_code = '").append(dealerERPcode).append("'\n");
		}
		PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
		return ps;
	}
	public PageResult<Map<String, Object>> queryCertificationStatus(String beginTime,String endTime,String dmsOrderCode,String caErpOrgCode,String dealerERPcode,int curPage, int pageSize){
		StringBuffer sql= new StringBuffer();
		sql.append("\n" );
		sql.append("select \n" );
		sql.append("       h.dealer_erp_code,\n" );
		sql.append("       h.dms_order_code,\n" );
		sql.append("       h.erp_order_code,\n" );
		sql.append("       h.ca_erp_org_code,\n" );
		sql.append(" decode(h.ca_erp_org_code,726,'重庆轿车',82,'重庆微车',197,'南京微车',142,'河北微车',647,'新疆结算中心') ca_erp_org_name,\n" );
		sql.append("       h.car_quantity,\n" );
		sql.append("       h.account_type_code,\n" );
		sql.append("       h.account_type_name,\n" );
		sql.append("       h.dms_handle_flag,\n" );
		sql.append(" to_char(h.create_date,'YYYY-MM-DD') create_date,\n" );
		sql.append("       h.remark,\n" );
		sql.append("       h.attribute1\n" );
		sql.append("  from t_CERTIFICATION_POST_H h");
		sql.append(" where 1=1 \n");
		if(Utility.testString(beginTime)){
			sql.append(" AND h.create_date >= TO_DATE('").append(beginTime).append("', 'YYYY-MM-DD HH24:MI:SS')\n");
		}
		if(Utility.testString(endTime)){
			sql.append(" AND h.create_date <= TO_DATE('").append(endTime).append("', 'YYYY-MM-DD HH24:MI:SS')\n");
		}
		if(Utility.testString(dmsOrderCode)){
			sql.append(" AND h.dms_order_code = '").append(dmsOrderCode).append("'\n");
		}
		if(Utility.testString(caErpOrgCode)){
			sql.append(" AND h.ca_erp_org_code = '").append(caErpOrgCode).append("'\n");
		}
		if(Utility.testString(dealerERPcode)){
			sql.append(" AND h.dealer_erp_code = '").append(dealerERPcode).append("'\n");
		}
		PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
		return ps;
	}
	public PageResult<Map<String, Object>> queryCertificationDetail(String DMS_ORDER_CODE,String CA_ERP_ORG_CODE,int curPage, int pageSize){
		StringBuffer sql= new StringBuffer();
		sql.append("\n" );
		sql.append("select l.certification_post_l_id,\n" );
		sql.append("       l.dms_order_code,\n" );
		sql.append("       l.ca_erp_org_code,\n" );
		sql.append(" decode(l.ca_erp_org_code,726,'重庆轿车',82,'重庆微车',197,'南京微车',142,'河北微车',647,'新疆结算中心') ca_erp_org_name ,\n" );
		sql.append("       l.ems_number,\n" );
		sql.append("       l.certification_quantity,\n" );
		sql.append("       to_char(l.posted_date,'yyyy-MM-dd') posted_date,\n" );
		sql.append("       l.dms_handle_flag,\n" );
		sql.append("       to_char(l.create_date,'yyyy-MM-dd') create_date,\n" );
		sql.append("       l.remark,\n" );
		sql.append("       l.attribute1\n" );
		sql.append("from t_CERTIFICATION_POST_L l");
		sql.append(" where 1=1 \n");

		if(Utility.testString(DMS_ORDER_CODE)){
			sql.append(" AND l.dms_order_code = '").append(DMS_ORDER_CODE).append("'\n");
		}
		if(Utility.testString(CA_ERP_ORG_CODE)){
			sql.append(" AND l.ca_erp_org_code = '").append(CA_ERP_ORG_CODE).append("'\n");
		}		 
		PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
		return ps;			
	}	
}
