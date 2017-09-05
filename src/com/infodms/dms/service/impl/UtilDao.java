package com.infodms.dms.service.impl;

import java.sql.ResultSet;
import java.util.Map;

import com.infodms.dms.common.Constant;
import com.infodms.dms.common.tag.BaseUtils;
import com.infodms.dms.dao.common.BaseDao;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("unchecked")
public class UtilDao extends BaseDao {

	private static final UtilDao dao = new UtilDao();
	
	public static UtilDao getInstance(){
		if (dao == null) {
			return new UtilDao();
		}
		return dao;
	}
	
	public Map<String, Object> getInfoByVin(String vin){
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT V.*,to_char(vi.create_date,'yyyy-mm-dd hh24:mi') in_store_date,c.ctm_name as customer_name,wu.rule_code,a.order_id, c.main_phone,a.car_charactor car_use_type,TC.CODE_DESC car_use_desc,  c. ctm_name,c.OTHER_PHONE,c.Address,vw.brand_name as brand_name,vw.brand_code as brand_code,vw.series_name AS series_name,vw.series_code AS series_code,vw.model_name AS model_name,vw.model_code AS model_code,a.CONSIGNATION_DATE as purchased_date_act,vw.package_name,ba.area_name yieldly_Name FROM TM_VEHICLE V \n");
		sql.append(" LEFT OUTER JOIN VW_MATERIAL_GROUP_service vw ON vw.package_id=v.package_id \n");
		sql.append(" LEFT OUTER JOIN TT_DEALER_ACTUAL_SALES A ON V.VEHICLE_ID=A.VEHICLE_ID and a.is_return="
				+ Constant.IF_TYPE_NO + "\n");
		sql.append(" LEFT OUTER JOIN TT_CUSTOMER C ON A.CTM_ID=C.CTM_ID \n");
		sql.append(" LEFT JOIN tm_business_area ba on ba.area_id=V.YIELDLY");
		sql.append(" LEFT JOIN TC_CODE TC ON TC.CODE_ID=a.car_charactor");
		sql.append(" LEFT JOIN Tt_As_Wr_Game wg on wg.id = v.claim_tactics_id");
		sql.append(" LEFT JOIN tt_as_wr_rule wu on wu.id=wg.rule_id");
		sql.append("	LEFT JOIN (SELECT max(vi.create_date) create_date ,vi.vehicle_id\n");
		sql.append("	FROM TT_VS_INSPECTION vi GROUP BY vi.vehicle_id)vi\n");
		sql.append("	ON  vi.vehicle_id = v.vehicle_id AND v.life_cycle IN ("
				+ Constant.VEHICLE_LIFE_03 + "," + Constant.VEHICLE_LIFE_04
				+ "," + Constant.VEHICLE_LIFE_07 + ")");
		sql.append(" WHERE 1=1 ");
		if (BaseUtils.testString(vin)) {
			sql.append(" and v.VIN  = '"+vin +"'\n");
		}
		Map<String, Object> ps = pageQueryMap(sql.toString(), null,getFunName());
		return ps;

	}

	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		return null;
	}
}
