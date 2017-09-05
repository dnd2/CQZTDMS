package com.infoservice.dms.chana.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.po.TmPtSupplierPO;
import com.infoservice.dms.chana.common.RpcException;
import com.infoservice.po3.bean.PO;

@SuppressWarnings("unchecked")
public class DePtDcDao extends BaseDao {
	
	private static final DePtDcDao dao = new DePtDcDao ();
	
	public static final DePtDcDao getInstance() {
		return dao;
	}
	
	/**
	 * 
	* @Title: queryVender 
	* @Description: TODO(查询供应商接口表) 
	* @param @return    设定文件 
	* @return List<TmPtDcPO>    返回类型 
	* @throws
	 */
	@SuppressWarnings("unchecked")
	public List<TmPtSupplierPO> queryVender() {
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT VENDER_ID, VENDER_CODE, VENDER_NAME FROM T_VENDER_DEFINE\n");
		sql.append(" WHERE VENDER_CODE IS NOT NULL");
		sql.append("   AND IS_FORBIDDEN = 0");
		sql.append("   AND ROWNUM <= 500");
		List<TmPtSupplierPO> pos = select(sql.toString(), null, TmPtSupplierPO.class);
		return pos;
	}
	
	@Override
	protected PO wrapperObject(ResultSet rs, int idx) {
		TmPtSupplierPO po = new TmPtSupplierPO();
		try {
			po.setSupplierId(rs.getLong("VENDER_ID"));
			po.setSupplierCode(rs.getString("VENDER_CODE"));
			po.setSupplierName(rs.getString("VENDER_NAME"));
			po.setShortName(rs.getString("VENDER_NAME"));
		} catch (SQLException e) {
			throw new RpcException(e.getMessage());
		}
		return po;
	}

	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}
	/**
	 * 
	* @Title: delVenderByCodes 
	* @Description: TODO(根据供应商code删除供应商接口表) 
	* @param @param codes  ('a','b','c')
	* @return void    返回类型 
	* @throws
	 */
	@SuppressWarnings("unchecked")
	public void delVenderByCodes(String codes) {
		StringBuffer sql= new StringBuffer();
		sql.append("DELETE FROM T_VENDER_DEFINE\n");
		sql.append(" WHERE VENDER_CODE IN (").append(codes).append(")");
		delete(sql.toString(), null);
	}
	/**
	 * 
	* @Title: delPartVenderReByIds 
	* @Description: TODO(根据id列表删除接口表数据) 
	* @param @param ids   (1,2,3)
	* @return void    返回类型 
	* @throws
	 */
	@SuppressWarnings("unchecked")
	public void delPartVenderReByIds(String ids) {
		StringBuffer sql= new StringBuffer();
		sql.append("DELETE FROM T_SELLPART_VENDER\n");
		sql.append(" WHERE SV_ID IN (").append(ids).append(")");
		delete(sql.toString(), null);
	}
	/**
	 * 
	* @Title: queryPartVenderRelation 
	* @Description: TODO(查询配件和供应商关系接口表) 
	* @return List<Map<String,Object>>    返回类型 
	* @throws
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> queryPartVenderRelation() {
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT distinct A.SV_ID, A.SELLPART_ID, C.SUPPLIER_ID AS VENDER_ID\n" );
		sql.append("  FROM T_SELLPART_VENDER A, TM_PT_PART_BASE B, TM_PT_SUPPLIER C\n" );
		sql.append(" WHERE A.Sellpart_Oldcode = B.Part_Code\n" );
		sql.append("  AND A.Vender_Code = C.Supplier_Code\n" );
		sql.append("  AND ROWNUM <= 500");
		List<Map<String, Object>> maps = pageQuery(sql.toString(), null, getFunName());
		return maps;
	}
	
	
}
