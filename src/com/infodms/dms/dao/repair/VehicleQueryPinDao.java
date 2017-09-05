package com.infodms.dms.dao.repair;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.common.Utility;
import com.infodms.dms.dao.common.BaseDao;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

/** 
* @ClassName: VehicleQueryPinDao 
* @Description: TODO(车辆PIN码导入查询Dao) 
* @author wenyudan 
* @date 2013-11-11 下午3:56:20 
*  
*/ 

public class VehicleQueryPinDao extends BaseDao {
	public static final Logger logger = Logger.getLogger(VehicleQueryPinDao.class);
	public static final VehicleQueryPinDao dao = new VehicleQueryPinDao();
	public static final VehicleQueryPinDao getInstance(){
		return dao;
	}

	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO 自动生成的方法存根
		return null;
	}

	public PageResult<Map<String, Object>> QueryActivity(
			Map<String, String> map, Integer pageSize, Integer curPage) {
		StringBuffer sql= new StringBuffer();
		sql.append("select TMGM.GROUP_NAME SERIES_NAME,\n" );
		sql.append("       TMGM1.GROUP_NAME MODEL_NAME,\n" );
		sql.append("       TV.VIN,\n" );
		sql.append("       TV.PIN,\n" );
		sql.append("       TU.NAME IMPORT_PEOPLE,\n" );
		sql.append("       TO_CHAR(TV.PRODUCT_DATE, 'yyyy-MM-dd') PRODUCT_DATE,  --生产时间\n" );
		sql.append("       TV.Engine_No,\n" );
		sql.append("      TO_CHAR(TV.PURCHASED_DATE, 'yyyy-MM-dd') PURCHASED_DATE, --销售时间\n" );
		sql.append("\n" );
		sql.append("       TO_CHAR(TV.IMPORT_DATE, 'yyyy-MM-dd hh24:mi:ss') IMPORT_DATE\n" );
		sql.append("  from tm_vehicle            TV,\n" );
		sql.append("       TM_VHCL_MATERIAL_GROUP TMGM,\n" );
		sql.append("       TC_USER TU,\n" );
		sql.append("       TM_VHCL_MATERIAL_GROUP TMGM1\n" );
		sql.append(" WHERE 1 = 1\n" );
		sql.append("   AND TV.SERIES_ID = TMGM.GROUP_ID\n" );
		sql.append("   AND TV.MODEL_ID = TMGM1.GROUP_ID");
		sql.append("   AND TV.IMPORT_PEOPLE = TU.USER_ID(+) ");

		List par=new ArrayList();
		if(Utility.testString(map.get("serisCode"))){
			sql.append(" and TMGM.GROUP_NAME like '%"+map.get("serisCode")+"%'\n"); 
		}
		if(Utility.testString(map.get("groupCode"))){
			sql.append(" and TMGM1.GROUP_NAME like '%"+map.get("groupCode")+"%'\n"); 
		}
		if(Utility.testString(map.get("PIN"))){
			sql.append(" and TV.PIN like '%"+map.get("PIN")+"%'\n"); 
		}
		if(Utility.testString(map.get("Importpeople"))){
			sql.append(" and TV.IMPORT_PEOPLE like '%"+map.get("Importpeople")+"%'\n"); 
		}
		if(Utility.testString(map.get("engineNo"))){
			sql.append(" and TV.Engine_No like '%"+map.get("engineNo")+"%'\n"); 
		}
		if(Utility.testString(map.get("SelectPin"))){
			if(map.get("SelectPin").equals("1")){
				sql.append(" AND TV.PIN	IS NULL ");
			}else{
				sql.append(" AND TV.PIN	IS NOT NULL ");
			}
		}
		
		if(Utility.testString(map.get("bDate"))){
			sql.append(" and tv.PURCHASED_DATE>=to_date(?,'yyyy-mm-dd')\n"); 
			par.add(map.get("bDate"));

		}

	
		if(Utility.testString(map.get("eDate"))){
			sql.append(" and tv.PURCHASED_DATE<=to_date(?,'yyyy-mm-dd hh24:mi:ss')\n"); 
			par.add(map.get("eDate")+" 23:59:59");
		}
		if(Utility.testString(map.get("bgDate"))){
			sql.append(" and tv.IMPORT_DATE>=to_date(?,'yyyy-mm-dd')\n"); 
			par.add(map.get("bgDate"));
			
		}
		
		if(Utility.testString(map.get("egDate"))){
			sql.append(" and tv.IMPORT_DATE<=to_date(?,'yyyy-mm-dd hh24:mi:ss')\n"); 
			par.add(map.get("egDate")+" 23:59:59");
		}
		sql.append("ORDER BY TV.SERIES_ID");	

		PageResult<Map<String, Object>> ps= pageQuery(sql.toString(), par, getFunName(),pageSize, curPage);
		return ps;
	}

}
