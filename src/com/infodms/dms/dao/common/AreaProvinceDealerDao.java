package com.infodms.dms.dao.common;

import java.sql.ResultSet;
import java.util.List;

import com.infodms.dms.bean.AreaProvinceBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.po.TmDealerPO;
import com.infodms.dms.po.TmOrgPO;
import com.infodms.dms.po.TmRegionPO;
import com.infoservice.po3.bean.PO;

public class AreaProvinceDealerDao extends BaseDao {

	private AreaProvinceDealerDao(){}
	
	public static AreaProvinceDealerDao getInstance(){
		return new AreaProvinceDealerDao();
	}
	
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}
	
	/*
	 * 大区查询
	 */
	@SuppressWarnings("unchecked")
	public List<TmOrgPO> getArea(){
		StringBuffer sql = new StringBuffer("\n");
		sql.append("select org_id,org_code,org_name\n");
		sql.append("from tm_org where org_type=");
		sql.append(Constant.ORG_TYPE_OEM);
		return this.select(TmOrgPO.class, sql.toString(), null);
	}
	
	/*
	 * 省份查询
	 */
	@SuppressWarnings("unchecked")
	public List<TmRegionPO> getProvince(){
		StringBuffer sql = new StringBuffer("\n");
		sql.append("select reg.region_id, reg.region_code, reg.region_name\n");
		sql.append("from tm_region reg\n");
		sql.append("where reg.region_code in\n");
		sql.append("(select d.province_id\n");
		sql.append("from tm_dealer d\n");
		sql.append("where dealer_id in\n");
		sql.append("(select dor.dealer_id\n");
		sql.append("from tm_dealer_org_relation dor}}\n");
		sql.append("order by reg.region_name\n");
		return this.select(TmRegionPO.class, sql.toString(), null);
	}

	/*
	 * 省份与大区查询
	 */
	@SuppressWarnings("unchecked")
	public List<AreaProvinceBean> getAreaProvince(){
		StringBuilder sql= new StringBuilder();
		sql.append("select distinct a.region_id,\n" );
		sql.append("                a.region_code,\n" );
		sql.append("                a.region_name,\n" );
		sql.append("                b.org_id,\n" );
		sql.append("                b.org_code,\n" );
		sql.append("                b.org_name\n" );
		sql.append("  from tm_region a, tm_org b, tm_dealer c, tm_dealer_org_relation d\n" );
		sql.append(" where a.region_code = c.province_id\n" );
		sql.append("   and b.org_id = d.org_id\n" );
		sql.append("   and c.dealer_id = d.dealer_id");
		return this.select(AreaProvinceBean.class,sql.toString(),null );
	}
	
	/*
	 * 经销商查询
	 */
	@SuppressWarnings("unchecked")
	public List<TmDealerPO> getDealer(String con){
		StringBuffer sql = new StringBuffer("\n");
		sql.append("select dealer_id,dealer_code,dealer_name\n");
		sql.append("from tm_dealer\n");
		sql.append("where 1=1\n");
		sql.append("and province_id=");
		sql.append(con);
		return this.select(TmDealerPO.class, sql.toString(), null);
	}
	
}
