package com.infodms.dms.dao.customerRelationships;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.infodms.dms.actions.customerRelationships.complaintConsult.CustomerComplain;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.materialManager.MaterialGroupManagerDao;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.yxdms.utils.DaoFactory;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

@SuppressWarnings("unchecked")
public class CustomerComplainDao extends BaseDao{

	private static final CustomerComplainDao dao = new CustomerComplainDao();
	
	public static final CustomerComplainDao getInstance() {
		return dao;
	}
	
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		return null;
	}
	
	public PageResult<Map<String, Object>> queryCustomerComplain(CustomerComplain customerComplain, AclUserBean logonUser, Integer pageSize,Integer curPage) {
		StringBuffer sql = new StringBuffer();
		List<Object> params = new ArrayList<Object>();
		if(customerComplain.getIsDealer().equals("false")){
			sql.append("select to_char(t.time,'yyyy-mm-dd') as baoyuan_shijian, t.*,(select code_desc from tc_code where code_id = t.COMPLAINT_TYPE) as COMPLAINT_TYPE_NAME,"
					+ "(select code_desc from tc_code where code_id = t.COMPLAINT_level) as COMPLAINT_level_NAME,"
					+ "t.create_by as dengji_ren,to_char(t.create_date,'yyyy-dd-mm') as dengji_date,t1.dealer_code,t1.dealer_name,"
					+ "t2.CHULI_NEIRONG,to_char(t2.time,'yyyy-dd-mm') as genjin_shijian,t2.create_by as chuli_ren,decode(t2.status,'3','已闭环','未闭环') as status_name, \n"); 
		}else{
			sql.append("select distinct t.*,t1.dealer_code,t1.dealer_name, \n"); 
		}
		if(logonUser.getDealerId()==null){
			sql.append("'false' as isDealer  \n"); 
			sql.append("from TT_CUSTOMER_COMPLAIN t,tm_dealer t1,TT_CUSTOMER_COMPLAIN_record t2  where t.id = t2.detail_id(+) and t.dealer_id = t1.dealer_id \n"); 
			if(!customerComplain.getDealerCode().equals("")){
				sql.append("and  t1.dealer_code like '%"+customerComplain.getDealerCode()+"%'\n"); 
			}
			sql.append(MaterialGroupManagerDao.getOrgDealerLimitSqlByService("t", logonUser));//省份限制
		}else{
			sql.append("'true' as isDealer  \n"); 
			sql.append("from TT_CUSTOMER_COMPLAIN t,tm_dealer t1 where t.dealer_id = t1.dealer_id and t.dealer_id =?  \n"); 
			params.add(logonUser.getDealerId());
		}
		if(!customerComplain.getStatus().equals("")){
			if(customerComplain.getStatus().equals("1")){
				sql.append("and  t.STATUS !=3\n"); 
			}else if(customerComplain.getStatus().equals("2")){
				sql.append("and  t.STATUS =3\n"); 
			}
		}
		if(!customerComplain.getCOMPLAINT_TYPE().equals("")){
			sql.append("and  COMPLAINT_TYPE ="+customerComplain.getCOMPLAINT_TYPE()+"\n"); 
		}
		if(!customerComplain.getCOMPLAINT_LEVEL().equals("")){
			sql.append("and  COMPLAINT_LEVEL ="+customerComplain.getCOMPLAINT_LEVEL()+"\n"); 
		}
		if(!customerComplain.getSDENGJI_DATE().equals("")){
			sql.append("and  trunc(t.CREATE_DATE)>=trunc(to_date('"+customerComplain.getSDENGJI_DATE()+"','yyyy-mm-dd'))\n"); 
		}
		if(!customerComplain.getEDENGJI_DATE().equals("")){
			sql.append("and  trunc(t.CREATE_DATE)<=trunc(to_date('"+customerComplain.getEDENGJI_DATE()+"','yyyy-mm-dd'))\n"); 
		}
		if(!customerComplain.getSGENJIN_DATE().equals("")){
			sql.append("and  trunc(t2.time)>=trunc(to_date('"+customerComplain.getSGENJIN_DATE()+"','yyyy-mm-dd'))\n"); 
		}
		if(!customerComplain.getEGENJIN_DATE().equals("")){
			sql.append("and  trunc(t2.time)<=trunc(to_date('"+customerComplain.getEGENJIN_DATE()+"','yyyy-mm-dd'))\n"); 
		}
		if(!customerComplain.getVIN().equals("")){
			sql.append("and  VIN like '%"+customerComplain.getVIN()+"%'\n"); 
		}
		if(!customerComplain.getGuzhang_miaosu().equals("")){
			sql.append("and  GUZHANG_MIAOSU like '%"+customerComplain.getGuzhang_miaosu()+"%'\n"); 
		}
		sql.append("order by t.CREATE_DATE desc\n"); 
		return (PageResult<Map<String, Object>>) this.pageQuery(sql.toString(), params, this.getFunName(), pageSize, curPage);
	}
	
	public Map<String, Object> showInfoByVin(RequestWrapper request) {
		StringBuffer sb= new StringBuffer();
		sb.append("select v.*,\n" );
		sb.append("       to_char(v.PRODUCT_DATE,'yyyy-mm-dd') as PRODUCT_DATE_act,\n" );
		sb.append("(select i.wrgroup_id\n" );
		sb.append("          from tt_as_wr_MODEL_ITEM i\n" );
		sb.append("         where i.model_id = v.PACKAGE_ID and i.wrgroup_id in\n" );
		sb.append("               (select WRGROUP_ID\n" );
		sb.append("                  from tt_as_wr_model_group\n" );
		sb.append("                 where wrgroup_type = 10451001)) as wrgroup_id,");
		sb.append("       to_char(vi.create_date, 'yyyy-mm-dd hh24:mi') in_store_date,\n" );
		sb.append("       c.ctm_name as customer_name,\n" );
		sb.append("       wu.rule_code,\n" );
		sb.append("       a.order_id,\n" );
		sb.append("       c.main_phone,\n" );
		sb.append("       a.car_charactor car_use_type,\n" );
		sb.append("       tc.code_desc car_use_desc,\n" );
		sb.append("       c. ctm_name,\n" );
		sb.append("       c.other_phone,\n" );
		sb.append("       c.address,\n" );
		sb.append("       vw.brand_name as brand_name,\n" );
		sb.append("       vw.series_name as series_name,\n" );
		sb.append("       vw.model_name as model_name,\n" );
		sb.append("       to_char(a.consignation_date,'yyyy-mm-dd') as purchased_date_act,\n" );
		sb.append("       vw.package_name,\n" );
		sb.append("       ba.area_name yieldly_name\n" );
		sb.append("  from tm_vehicle v\n" );
		sb.append("  left outer join vw_material_group_service vw\n" );
		sb.append("    on vw.package_id = v.package_id\n" );
		sb.append("  left outer join tt_dealer_actual_sales a\n" );
		sb.append("    on v.vehicle_id = a.vehicle_id\n" );
		sb.append("   and a.is_return = 10041002\n" );
		sb.append("  left outer join tt_customer c\n" );
		sb.append("    on a.ctm_id = c.ctm_id\n" );
		sb.append("  left join tm_business_area ba\n" );
		sb.append("    on ba.area_id = v.yieldly\n" );
		sb.append("  left join tc_code tc\n" );
		sb.append("    on tc.code_id = a.car_charactor\n" );
		sb.append("  left join tt_as_wr_game wg\n" );
		sb.append("    on wg.id = v.claim_tactics_id\n" );
		sb.append("  left join tt_as_wr_rule wu\n" );
		sb.append("    on wu.id = wg.rule_id\n" );
		sb.append("  left join (select max(vi.create_date) create_date, vi.vehicle_id\n" );
		sb.append("               from tt_vs_inspection vi\n" );
		sb.append("              group by vi.vehicle_id) vi\n" );
		sb.append("    on vi.vehicle_id = v.vehicle_id\n" );
		sb.append("   and v.life_cycle in (10321004)\n" );
		sb.append(" where 1 = 1\n" );
		DaoFactory.getsql(sb, "v.vin", DaoFactory.getParam(request, "vin"), 1);
		Map<String, Object> ps = pageQueryMap(sb.toString(), null, getFunName());
		return ps;
	}
	
	public boolean checkExist(RequestWrapper request) {
		StringBuffer sb= new StringBuffer();
		String vin = request.getParamValue("vin");
		sb.append("select * FROM TT_CUSTOMER_COMPLAIN T WHERE T.STATUS not in (3) AND T.VIN ='"+vin+"'\n" );
		List<Map<String, Object>> ps = pageQuery(sb.toString(), null, getFunName());
		if(ps!=null&&ps.size()>0){
			return true;
		}else{
			return false;
		}
		
	}
}
