package com.infoservice.dms.chana.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.po.TmPtSupplierPO;
import com.infodms.dms.po.TmVehiclePO;
import com.infoservice.dms.chana.common.RpcException;
import com.infoservice.dms.chana.po.XxdmsVinCodePO;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("unchecked")
public class DeFromERPDao extends BaseDao {
	
	private static final DeFromERPDao dao = new DeFromERPDao ();
	
	public static final DeFromERPDao getInstance() {
		return dao;
	}
	
	/**
	 * 
	* @Title: queryVender 
	* @Description: TODO(查询ERP接口表) 
	* @param @return    设定文件 
	* @return List<TmPtDcPO>    返回类型 
	* @throws
	 */
	@SuppressWarnings("unchecked")
	public List<XxdmsVinCodePO> queryfromerpData() {
		StringBuffer sql= new StringBuffer();
		/*sql.append("SELECT * FROM XXDMS_VIN_CODE ");
		sql.append(" WHERE VIN_CODE IS NOT NULL ");
		sql.append(" AND ORGANIZATION_ID - 10000 < 0 ");
		sql.append(" AND ROWNUM <= 500 order by organization_id");*/
		
		sql.append("select *\n");
		sql.append("  from xxdms_vin_code xvc\n");  
		sql.append(" where xvc.organization_id < 10000\n");  
		sql.append("   and exists\n");  
		sql.append(" (select 1 from tm_vehicle tmv where tmv.vin = xvc.vin_code)\n");  
		sql.append(" and rownum <= 5000\n");

		List<XxdmsVinCodePO> pos = select(sql.toString(), null, XxdmsVinCodePO.class);
		return pos;
	}
    
	public Map<String, Object> selectTmVehicle(String vin) {
		String sql="select * from tm_vehicle tvl where tvl.vin ='"+vin+"'";		
		Map<String, Object> map = pageQueryMap(sql, null, getFunName());
		if(map !=null && map.size()>0){
			return map;
		}else {
			return null;
		}	
	}
	
	@Override
	protected XxdmsVinCodePO wrapperObject(ResultSet rs, int idx) {
		
		XxdmsVinCodePO tfe = new XxdmsVinCodePO();
		try {
			tfe.setVinCode(rs.getString("VIN_CODE"));
			tfe.setEngineCode(rs.getString("ENGINE_CODE"));
			tfe.setOrganizationId(rs.getInt("ORGANIZATION_ID"));
			tfe.setItemCode(rs.getString("ITEM_CODE"));
			tfe.setHgDate(rs.getDate("HG_DATE"));
			tfe.setHegezhengCode(rs.getString("HEGEZHENG_CODE")); //YH 2011.10.25
		} catch (SQLException e) {
			
			throw new RpcException(e);
		}
		
		return tfe;
	}
	
	@SuppressWarnings("unchecked")
	public void delFromERPByvin(String ids) {
		StringBuffer sql= new StringBuffer();
		sql.append("DELETE FROM XXDMS_VIN_CODE WHERE VIN_CODE ='").append(ids).append("'");
		int i = delete(sql.toString(), null);
		if(i==1){
			System.out.println("删除数据：VIN_CODE = "+ids);
		}else{
			System.out.println("删除失败：VIN_CODE = "+ids);
		}
	}

	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		
		return null;
	}
	
}
