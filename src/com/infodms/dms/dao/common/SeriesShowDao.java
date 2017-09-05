package com.infodms.dms.dao.common;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

import com.infodms.dms.bean.SeriesBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.po.TmVhclMaterialGroupPO;
import com.infodms.dms.po.TmVhclMaterialPO;
import com.infodms.dms.po.VwMaterialGroupPO;
import com.infodms.dms.util.StringUtil;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

public class SeriesShowDao extends BaseDao {

	private SeriesShowDao(){
	}
	
	public static SeriesShowDao getInstance(){
		return new SeriesShowDao();
	}
	
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}
	
	/*
	 * 查询视图里面的车系数据
	 */
	@SuppressWarnings("unchecked")
	public List<SeriesBean> querySeries(){
		StringBuffer sql = new StringBuffer("\n");
		sql.append("select distinct c.SERIES_ID,c.SERIES_CODE,c.SERIES_NAME\n");
		sql.append("from tt_as_wr_model_group a,tt_as_wr_model_item b,vw_material_group c\n");
		sql.append("where a.wrgroup_type =").append(Constant.WR_MODEL_GROUP_TYPE_02).append("\n");
		sql.append("and a.wrgroup_id = b.wrgroup_id\n");
		sql.append("and c.MODEL_ID =b.model_id\n");
		return select(SeriesBean.class, sql.toString(), null);
	}
	
	/*
	 * 查询物料组里的车系
	 */
	@SuppressWarnings("unchecked")
	public List<SeriesBean> querySeriesFromMG(){
		StringBuffer sql = new StringBuffer("\n");
		sql.append("select group_id as series_id,group_code as series_code,group_name as series_name\n");
		sql.append("from tm_vhcl_material_group\n");
		sql.append("where group_level=2\n");
		sql.append("order by group_code asc\n");
		return select(SeriesBean.class, sql.toString(), null);
	}
	
	/*
	 * 查询所有车型数据
	 */
	@SuppressWarnings("unchecked")
	public List<TmVhclMaterialGroupPO> getModel(String id){
		StringBuilder sql= new StringBuilder("\n");
		sql.append("select group_id, group_code, group_name\n" );
		sql.append(" from tm_vhcl_material_group\n" );
		sql.append("where group_level = 3\n" );
		sql.append("  and parent_group_id = " ).append(id).append("\n");
		sql.append("order by group_code asc\n");
		return this.select(TmVhclMaterialGroupPO.class, sql.toString(), null);
	}
	
	/*
	 * 
	 */
	@SuppressWarnings("unchecked")
	public List<SeriesBean> getSeries(Long id){
		StringBuilder sql= new StringBuilder("\n");
		sql.append("select distinct g.SERIES_ID,g.SERIES_CODE,g.SERIES_NAME\n" );
		sql.append("  from vw_material_group g, tm_vhcl_material_group_r r,tm_vhcl_material m\n" );
		sql.append(" where g.PACKAGE_ID = r.group_id\n" );
		sql.append("   and m.material_id = r.material_id\n" );
		sql.append("   and m.material_id =").append(id).append("\n");
		return select(SeriesBean.class,sql.toString(),null);
	}
	
	/*
	 * 根据视图里面的车系选择物料代码与物料名称
	 */
	@SuppressWarnings("unchecked")
	public PageResult<TmVhclMaterialPO> queryMaterial(String id,String code,String name,int pageSize,int curPage){
		StringBuffer sql = new StringBuffer("\n");
		sql.append("select * from tm_vhcl_material m\n");
		sql.append("where m.material_id in\n");
		sql.append("(select r.material_id\n");
		sql.append("from tm_vhcl_material_group_r r, vw_material_group mg\n");
		sql.append("where 1=1 \n");
		if(StringUtil.notNull(id) && !"null".equals(id))
			sql.append("and mg.SERIES_ID =").append(id).append("\n");
		sql.append("and mg.PACKAGE_ID = r.group_id)\n");
		if(StringUtil.notNull(code))
			sql.append("and m.material_code like '%"+code+"%'\n");
		if(StringUtil.notNull(name))
			sql.append("and m.material_name like '%"+name+"%'\n");
		sql.append("order by material_code\n");
		return this.pageQuery(TmVhclMaterialPO.class, sql.toString(), null, pageSize, curPage);
	}
	
	/*
	 * 物料组里面的查询出的车系
	 */
	@SuppressWarnings("unchecked")
	public List<TmVhclMaterialGroupPO> getMaterialGroup(){
		StringBuffer sql = new StringBuffer("\n");
		sql.append("select * from tm_vhcl_material_group where group_level = 2\n");
		sql.append("order by group_name asc\n");
		return this.select(TmVhclMaterialGroupPO.class, sql.toString(), null);
	}
	
	/*
	 * 根据车系查询车型.
	 */
	@SuppressWarnings("unchecked")
	public PageResult<Map<String,Object>> queryModelByDept(String sql,int pageSize,int curPage){
		return (PageResult<Map<String, Object>>)this.pageQuery(sql, null, this.getFunName(), pageSize, curPage);
	}
	
}
