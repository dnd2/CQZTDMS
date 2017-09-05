package com.infodms.dms.dao.sales.storage.storagemanage;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.materialManager.MaterialGroupManagerDao;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TmBusinessAreaPO;
import com.infodms.dms.po.TmpTtCityDisPO;
import com.infodms.dms.po.TtSalesCityDisPO;
import com.infodms.dms.util.XHBUtil;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

import jxl.Cell;

/**
 * 
 * @ClassName     : VehicleImportDao 
 * @Description   : 车辆信息导入DAO 
 * @author        : syh
 * CreateDate     : 2017-7-10
 */
public class VehicleImportDao extends BaseDao<PO>{
	private static final VehicleImportDao dao = new VehicleImportDao ();
	public static final VehicleImportDao getInstance() {
		return dao;
	}
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}
	/**
	 * 导入车辆查询
	 * @param userId
	 * @param pageSize
	 * @param curPage
	 * @return
	 */
	public PageResult<Map<String, Object>> importQuery(String userId,
			int pageSize, Integer curPage) {
	    StringBuffer sql = new StringBuffer();

	    sql.append("select T.ROW_NUMBER,\n");
	    sql.append("       T.VIN,\n");
	    sql.append("       T.MATERIAL_CODE,\n");
	    sql.append("       T.COLOR,\n");
	    sql.append("       T.ENGINE_NO,\n"); 
	    sql.append("       T.GEARBOX_NO,\n");
	    sql.append("       T.HEGEZHENG_CODE,\n");
	    sql.append("       T.PRODUCT_DATE,\n");
	    sql.append("       T.OFFLINE_DATE\n");
	    sql.append("  from tmp_tm_vehicle t\n");
	    sql.append(" where t.create_by = ?\n"); 
	    sql.append(" ORDER BY to_number(row_number) ASC\n");
	    List<Object> params = new ArrayList<Object>();
	    params.add(userId);
		return dao.pageQuery(sql.toString(), params, getFunName(), pageSize, curPage);
	}
	/**
	 * 根据物料ID获取物料组信息
	 * @param materialId
	 * @return
	 */
	public Map<String, Object> getMaterialGroup(String materialId){
		StringBuffer sql = new StringBuffer();

		sql.append("select t1.material_id,\n");
		sql.append("       t2.group_id setId,\n");
		sql.append("       t3.group_id modelId,\n"); 
		sql.append("       t4.group_id seriesId\n");
		sql.append("  from TM_VHCL_MATERIAL         t1, --物料\n");
		sql.append("       TM_VHCL_MATERIAL_GROUP   t2, --配置\n");
		sql.append("       TM_VHCL_MATERIAL_GROUP   t3, --车型\n"); 
		sql.append("       TM_VHCL_MATERIAL_GROUP   t4, --车系\n");
		sql.append("       TM_VHCL_MATERIAL_GROUP_R rr --物料关系\n");
		sql.append(" where t1.material_id = rr.material_id\n");
		sql.append("   and t2.group_id = rr.group_id\n");
		sql.append("   and t2.parent_group_id = t3.group_id\n");
		sql.append("   and t3.parent_group_id = t4.group_id\n");
		sql.append("   and t1.material_id="+materialId+"\n");
		Map<String, Object> maps = this.pageQueryMap(sql.toString(), null, this.getFunName());
		return maps;
	}
}
	
