package com.infodms.dms.dao.report.storge;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.common.AjaxSelectDao;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.po3.bean.PO;

public class InAndOutDao extends BaseDao<PO>{
	public static final Logger logger = Logger.getLogger(InAndOutDao.class);
	public static InAndOutDao dao = new InAndOutDao();
	private static final AjaxSelectDao dao1 = new AjaxSelectDao();
	public static InAndOutDao getInstance(){
		return dao;
	}
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}
	//入库统计
	public List<Map<String, Object>> getInInfo(Map<String, Object> map)  throws Exception{
		String yieldly = (String)map.get("yieldly"); // 产地
		String startdate = (String)map.get("startdate"); // 开始时间
		String enddate = (String)map.get("enddate"); // 结束时间
		List<Object> params=new ArrayList<Object>();
		StringBuffer sbSql  =  new StringBuffer();
		sbSql.append("SELECT K.ERP_MODEL, K.ERP_PACKAGE, NVL(K.VEH_COUNT,0) VEH_COUNT\n");
		sbSql.append("  FROM (SELECT B.ERP_MODEL, B.ERP_PACKAGE, COUNT(1) VEH_COUNT\n");
		sbSql.append("          FROM TM_VEHICLE A, TM_VHCL_MATERIAL B\n");
		sbSql.append("         WHERE A.MATERIAL_ID = B.MATERIAL_ID\n");
		if(!"".equals(yieldly)){
			sbSql.append("           AND A.YIELDLY = ?\n");
			params.add(yieldly);
		}
		if(startdate!=null&&!"".equals(startdate)){//开始时间
			sbSql.append("   AND A.ORG_STORAGE_DATE>=TO_DATE(?,'YYYY-MM-DD HH24:MI:SS')\n" );
			params.add(startdate+" 00:00:00");
		}
		if(enddate!=null&&!"".equals(enddate)){//结束时间
			sbSql.append("  AND  A.ORG_STORAGE_DATE<=TO_DATE(?,'YYYY-MM-DD HH24:MI:SS')\n" );
			params.add(enddate +" 23:59:59");
		}
		sbSql.append("         GROUP BY B.ERP_MODEL, B.ERP_PACKAGE\n");
		sbSql.append("        UNION ALL\n");
		sbSql.append("        SELECT B.SERIES_NAME, '-', COUNT(1) VEH_COUNT\n");
		sbSql.append("          FROM TM_VEHICLE A, VW_MATERIAL_GROUP_MAT B\n");
		sbSql.append("         WHERE A.MATERIAL_ID = B.MATERIAL_ID\n");
		if(!"".equals(yieldly)){
			sbSql.append("           AND A.YIELDLY = ?\n");
			params.add(yieldly);
		}
		if(startdate!=null&&!"".equals(startdate)){//开始时间
			sbSql.append("   AND A.ORG_STORAGE_DATE>=TO_DATE(?,'YYYY-MM-DD HH24:MI:SS')\n" );
			params.add(startdate+" 00:00:00");
		}
		if(enddate!=null&&!"".equals(enddate)){//结束时间
			sbSql.append("  AND  A.ORG_STORAGE_DATE<=TO_DATE(?,'YYYY-MM-DD HH24:MI:SS')\n" );
			params.add(enddate +" 23:59:59");
		}
		sbSql.append("         GROUP BY B.SERIES_ID, B.SERIES_NAME\n");
		sbSql.append("        UNION ALL\n");
		sbSql.append("        SELECT '', '合计', SUM(1) VEH_COUNT\n");
		sbSql.append("          FROM TM_VEHICLE A, VW_MATERIAL_GROUP_MAT B\n");
		sbSql.append("         WHERE A.MATERIAL_ID = B.MATERIAL_ID\n");
		if(!"".equals(yieldly)){
			sbSql.append("           AND A.YIELDLY = ?\n");
			params.add(yieldly);
		}
		if(startdate!=null&&!"".equals(startdate)){//开始时间
			sbSql.append("   AND A.ORG_STORAGE_DATE>=TO_DATE(?,'YYYY-MM-DD HH24:MI:SS')\n" );
			params.add(startdate+" 00:00:00");
		}
		if(enddate!=null&&!"".equals(enddate)){//结束时间
			sbSql.append("  AND  A.ORG_STORAGE_DATE<=TO_DATE(?,'YYYY-MM-DD HH24:MI:SS')\n" );
			params.add(enddate +" 23:59:59");
		}
		sbSql.append(" ) K ORDER BY K.ERP_MODEL"); 
		return dao.pageQuery(sbSql.toString(), params, getFunName());
	}
	//出库统计
	public List<Map<String, Object>> getOutInfo(Map<String, Object> map)  throws Exception{
		String yieldly = (String)map.get("yieldly"); // 产地
		String startdate = (String)map.get("startdate"); // 开始时间
		String enddate = (String)map.get("enddate"); // 结束时间
		List<Object> params=new ArrayList<Object>();
		StringBuffer sbSql  =  new StringBuffer();
		sbSql.append("SELECT C.ERP_MODEL, NVL(COUNT(1),0) VEH_COUNT\n");
		sbSql.append("  FROM TM_VEHICLE A, TT_SALES_ALLOCA_DE B, TM_VHCL_MATERIAL C\n");
		sbSql.append(" WHERE A.VEHICLE_ID = B.VEHICLE_ID\n");
		sbSql.append("   AND A.MATERIAL_ID = C.MATERIAL_ID\n"); 
		if(!"".equals(yieldly)){
			sbSql.append("           AND A.YIELDLY = ?\n");
			params.add(yieldly);
		}
		if(startdate!=null&&!"".equals(startdate)){//开始时间
			sbSql.append("   AND B.OUT_DATE>=TO_DATE(?,'YYYY-MM-DD HH24:MI:SS')\n" );
			params.add(startdate+" 00:00:00");
		}
		if(enddate!=null&&!"".equals(enddate)){//结束时间
			sbSql.append("  AND  B.OUT_DATE<=TO_DATE(?,'YYYY-MM-DD HH24:MI:SS')\n" );
			params.add(enddate +" 23:59:59");
		}
		sbSql.append("GROUP BY C.ERP_MODEL\n");
		sbSql.append("UNION ALL\n");
		sbSql.append("SELECT C.GROUP_NAME, COUNT(1) VEH_COUNT\n");
		sbSql.append("  FROM TM_VEHICLE A, TT_SALES_ALLOCA_DE B, TM_VHCL_MATERIAL_GROUP C\n");
		sbSql.append(" WHERE A.VEHICLE_ID = B.VEHICLE_ID\n");
		sbSql.append("   AND A.SERIES_ID = C.GROUP_ID\n"); 
		if(!"".equals(yieldly)){
			sbSql.append("           AND A.YIELDLY = ?\n");
			params.add(yieldly);
		}
		if(startdate!=null&&!"".equals(startdate)){//开始时间
			sbSql.append("   AND B.OUT_DATE>=TO_DATE(?,'YYYY-MM-DD HH24:MI:SS')\n" );
			params.add(startdate+" 00:00:00");
		}
		if(enddate!=null&&!"".equals(enddate)){//结束时间
			sbSql.append("  AND  B.OUT_DATE<=TO_DATE(?,'YYYY-MM-DD HH24:MI:SS')\n" );
			params.add(enddate +" 23:59:59");
		}
		sbSql.append("GROUP BY C.GROUP_ID, C.GROUP_NAME\n");
		sbSql.append("UNION ALL\n");
		sbSql.append("SELECT '合计', NVL(SUM(1),0) VEH_COUNT\n");
		sbSql.append("  FROM TM_VEHICLE A, TT_SALES_ALLOCA_DE B, TM_VHCL_MATERIAL_GROUP C\n");
		sbSql.append(" WHERE A.VEHICLE_ID = B.VEHICLE_ID\n");
		sbSql.append("   AND A.SERIES_ID = C.GROUP_ID\n"); 
		if(!"".equals(yieldly)){
			sbSql.append("           AND A.YIELDLY = ?\n");
			params.add(yieldly);
		}
		if(startdate!=null&&!"".equals(startdate)){//开始时间
			sbSql.append("   AND B.OUT_DATE>=TO_DATE(?,'YYYY-MM-DD HH24:MI:SS')\n" );
			params.add(startdate+" 00:00:00");
		}
		if(enddate!=null&&!"".equals(enddate)){//结束时间
			sbSql.append("  AND  B.OUT_DATE<=TO_DATE(?,'YYYY-MM-DD HH24:MI:SS')\n" );
			params.add(enddate +" 23:59:59");
		}
		return dao.pageQuery(sbSql.toString(), params, getFunName());
	}
}
