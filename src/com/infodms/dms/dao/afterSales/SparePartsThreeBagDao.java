package com.infodms.dms.dao.afterSales;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.infodms.dms.common.Utility;
import com.infodms.dms.dao.common.BaseDao;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

public class SparePartsThreeBagDao extends BaseDao<PO>{
	private static final SparePartsThreeBagDao dao = new SparePartsThreeBagDao();
	public static final SparePartsThreeBagDao getInstance(){
		if (dao == null) {
			return new SparePartsThreeBagDao();
		}
		return dao;
	}
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public PageResult<Map<String, Object>> querySparePartsThreeBag(
			Map<String, Object> map, int pagesize, int curpage) {
		StringBuffer sql = new StringBuffer();
		String wr_months = (String) map.get("wr_months");
		String wr_mileage = (String) map.get("wr_mileage");
		String part_code = (String) map.get("part_code");
		String part_name = (String) map.get("part_name");

		sql.append(" SELECT T.PART_ID, T.PART_CODE, T.PART_NAME, T.WR_MONTHS, T.WR_MILEAGE\n");
		sql.append("   FROM TM_PT_PART_BASE T\n");
		sql.append("  WHERE 1 = 1\n");
		if (Utility.testString(part_code)) {
		sql.append("    AND T.PART_CODE like '%"+part_code+"%'\n");
		}
		if (Utility.testString(part_name)) {
		sql.append("    AND T.PART_NAME like '%"+part_name+"%'\n");
		}
		if (Utility.testString(wr_months)) {
		sql.append("    AND T.WR_MONTHS="+wr_months+"\n");
		}
		if (Utility.testString(wr_mileage)) {
		sql.append("    AND T.WR_MILEAGE="+wr_mileage+"\n");
		}
		sql.append(" order by t.PART_CODE ");
		PageResult<Map<String, Object>> ps = dao.pageQuery(sql.toString(),null, getFunName(), pagesize, curpage);
		return ps;
	}

	public void updateThreeBag(Map<String, Object> map) {
		String p_id = (String) map.get("p_id");
		String wr_months = (String) map.get("wr_months");
		String wr_mileage = (String) map.get("wr_mileage");
		String[] pId=p_id.split(",");
		String[] wrMonths=wr_months.split(",");
		String[] wrMileage=wr_mileage.split(",");
		for(int i=0;i<pId.length;i++){
		StringBuffer sql = new StringBuffer();
		sql.append(" UPDATE TM_PT_PART_BASE T\n");
		sql.append("    SET T.WR_MONTHS = "+wrMonths[i]+", T.WR_MILEAGE = "+wrMileage[i]+"\n");
		sql.append("  WHERE T.PART_ID = "+pId[i]+"");
		dao.update(sql.toString(), null);
		}
	}
}
