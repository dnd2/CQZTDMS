package com.infodms.dms.dao.publicInfoMng;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.po.TtAsRoAddItemPO;
import com.infodms.dms.po.TtAsRoRepairPartPO;
import com.infodms.dms.po.TtAsRoRepairitemPO;
import com.infodms.dms.util.StringUtil;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

public class PublicInfoMngDao extends BaseDao{

	private PublicInfoMngDao(){}
	public static PublicInfoMngDao getInstance(){
		return new PublicInfoMngDao();
	}
	
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	//主页面第一次查询
	@SuppressWarnings("unchecked")
	public PageResult<Map<String,Object>> mainQuery(String con,int pageSize,int curPage){
		StringBuilder sbSql= new StringBuilder("\n");
		sbSql.append("SELECT A.CTM_ID,\r\n");
		sbSql.append("       A.CARD_NUM,\r\n");
		sbSql.append("       A.GUEST_STARS,\r\n");
		sbSql.append("       A.CTM_NAME,\r\n");
		sbSql.append("       A.MAIN_PHONE,\r\n");
		sbSql.append("       A.CTM_TYPE,\r\n");
		sbSql.append("       A.LEVEL_ID,\r\n");
		sbSql.append("       A.SEX,\r\n");
		sbSql.append("       TO_CHAR(C.PURCHASED_DATE, 'yyyy-MM-dd') PURCHASED_DATE,\r\n");
		sbSql.append("       C.VIN,\r\n");
		sbSql.append("       C.YIELDLY,\r\n");
		sbSql.append("       C.MATERIAL_ID,\r\n");
		sbSql.append("       G.GROUP_NAME AS MODEL_NAME,\r\n");
		sbSql.append("       S.GROUP_NAME AS SERIES_NAME\r\n");
		sbSql.append("  FROM TT_CUSTOMER              A,\r\n");
		sbSql.append("       TT_DEALER_ACTUAL_SALES   B,\r\n");
		sbSql.append("       TM_VEHICLE               C,\r\n");
		sbSql.append("       TM_VHCL_MATERIAL_GROUP   G,\r\n");
		sbSql.append("       TM_VHCL_MATERIAL_GROUP   S\r\n");
		sbSql.append(" WHERE A.CTM_ID = B.CTM_ID\r\n");
		sbSql.append("   AND B.VEHICLE_ID = C.VEHICLE_ID\r\n");
		sbSql.append("   AND c.series_id = s.group_id\r\n");
		sbSql.append("   AND c.model_id = g.group_id\r\n");
		sbSql.append("   AND G.GROUP_LEVEL = 3\r\n");
		sbSql.append("   AND S.GROUP_LEVEL = 2"); 

		if(StringUtil.notNull(con))
			sbSql.append(con);
		//sql.append(" order by a. card_num\n");
		return this.pageQuery(sbSql.toString(),null,this.getFunName(), pageSize, curPage) ;
	}

	//根据VIN查询维修配件信息
	@SuppressWarnings("unchecked")
	public List<TtAsRoRepairPartPO> queryParts(String vin){
		StringBuilder sql= new StringBuilder("\n");
		sql.append("select *\n" );
		sql.append("  from tt_as_ro_repair_part\n" );
		sql.append(" where ro_id in\n" );
		sql.append("       (select id from tt_as_wr_application where vin = '").append(vin).append("')");
		return this.select(TtAsRoRepairPartPO.class, sql.toString(), null);
	}
	
	//根据VIN查询维修项目
	@SuppressWarnings("unchecked")
	public List<TtAsRoRepairitemPO> queryItem(String vin){
		StringBuilder sql= new StringBuilder("\n");
		sql.append("select *\n" );
		sql.append("  from tt_as_ro_repairitem\n" );
		sql.append(" where ro_no in\n" );
		sql.append("       (select vin from tt_as_wr_application where vin = '").append(vin).append("')");
		return this.select(TtAsRoRepairitemPO.class, sql.toString(),null);
	}
	
	//根据VIN查询附加项目
	@SuppressWarnings("unchecked")
	public List<TtAsRoAddItemPO> queryAddItem(String vin){
		StringBuilder sql= new StringBuilder("\n");
		sql.append("select *\n" );
		sql.append("  from tt_as_ro_add_item\n" );
		sql.append(" where ro_id in\n" );
		sql.append("       (select id from tt_as_wr_application where vin = '").append(vin).append("')");
		return this.select(TtAsRoAddItemPO.class, sql.toString(), null);
	}
}
