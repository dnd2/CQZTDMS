package com.infodms.dms.dao.sales.storage.sendManage;

import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.infodms.dms.common.materialManager.MaterialGroupManagerDao;
import com.infodms.dms.dao.common.BaseDao;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

/**
 * 
 * @ClassName     : CommprDao 
 * @Description   : 综合信息查询DAO 
 * @author        : ranjian
 * CreateDate     : 2013-9-11
 */
public class CommprDao extends BaseDao<PO>{
	private static final CommprDao dao = new CommprDao ();
	public static final CommprDao getInstance() {
		return dao;
	}
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}
	/**
	 * 商品车出库查询
	 * @param map
	 * @return 组板信息列表
	 * @throws Exception
	 */
	public List<Map<String, Object>> getCommprQuery(Map<String, Object> map)throws Exception{
		Object[] obj=getSQL(map);
		List<Map<String, Object>> valueMapList= dao.pageQuery(obj[0].toString(), (List<Object>)obj[1], getFunName());
		return valueMapList;
	}
	/**
	 * 商品车出库查询分页
	 * @param map
	 * @param curPage
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	public PageResult<Map<String, Object>> getCommprQueryFY(Map<String, Object> map, int curPage, int pageSize)throws Exception{
		Object[] objArr=getSQL(map);
		PageResult<Map<String, Object>> ps= dao.pageQuery(String.valueOf(objArr[0]), (List<Object>)objArr[1], getFunName(),pageSize,curPage);
		return ps;
	}
	//get sql
	public Object[] getSQL(Map<String, Object> map){
		/******************************页面查询字段start**************************/
		String vin = (String)map.get("vin"); // VIN	
		String billStartdate = (String)map.get("billStartdate"); // 
		String billEnddate = (String)map.get("billEnddate"); // 	
		String groupId = (String)map.get("groupId"); // 	
		String yieldly = (String)map.get("yieldly"); // 	
		String poseId = (String)map.get("poseId");
		String logiName = (String)map.get("logiName");
		String sdNumber = (String)map.get("sdNumber");
		String orderType = (String)map.get("orderType");
		/******************************页面查询字段end***************************/
		List<Object> params = new LinkedList<Object>();
		StringBuffer sbSql= new StringBuffer();
		sbSql.append("SELECT ROWNUM,TO_CHAR(D.ORG_STORAGE_DATE, 'YYYY-MM-DD HH24:MI:SS') ORG_STORAGE_DATE,\n");
		sbSql.append("       TO_CHAR(C.OUT_DATE, 'YYYY-MM-DD HH24:MI:SS') OUT_DATE,\n");
		sbSql.append("       E.LOGI_NAME,\n");
		sbSql.append("       A.CAR_TEAM,\n");
		sbSql.append("       B.ORDER_TYPE,\n");
		sbSql.append("       DECODE(B.ORDER_TYPE,10201004,'是','否') ISZZ,\n");
		sbSql.append("       F.DEALER_NAME,\n");
		sbSql.append("       G.SERIES_NAME,\n");
		sbSql.append("       G.MATERIAL_CODE,\n");
		sbSql.append("       D.VIN,\n");
		sbSql.append("       D.SD_NUMBER,\n");
		sbSql.append("       I.REGION_NAME,\n");
		sbSql.append("       J.REGION_NAME REGION_NAME1,\n");
		sbSql.append("       K.REGION_NAME REGION_NAME2\n");
		sbSql.append("  FROM TT_SALES_BOARD        A,\n");
		sbSql.append("       TT_SALES_BO_DETAIL    B,\n");
		sbSql.append("       TT_SALES_ALLOCA_DE    C,\n");
		sbSql.append("       TM_VEHICLE            D,\n");
		sbSql.append("       TT_SALES_LOGI         E,\n");
		sbSql.append("       TM_DEALER             F,\n");
		sbSql.append("       VW_MATERIAL_GROUP_MAT G,\n");
		sbSql.append("       TM_VS_ADDRESS         H,\n");
		sbSql.append("       TM_REGION             I,\n");
		sbSql.append("       TM_REGION             J,\n");
		sbSql.append("       TM_REGION             K\n");
		sbSql.append(" WHERE A.BO_ID = B.BO_ID\n");
		sbSql.append("   AND B.BO_DE_ID = C.BO_DE_ID\n");
		sbSql.append("   AND C.VEHICLE_ID = D.VEHICLE_ID\n");
		sbSql.append("   AND B.LOGI_ID = E.LOGI_ID(+)\n");
		sbSql.append("   AND B.REC_DEALER_ID = F.DEALER_ID\n");
		sbSql.append("   AND B.MAT_ID = G.MATERIAL_ID\n");
		sbSql.append("   AND B.ADDRESS_ID = H.ID\n");
		sbSql.append("   AND H.PROVINCE_ID = I.REGION_CODE\n");
		sbSql.append("   AND H.CITY_ID = J.REGION_CODE\n");
		sbSql.append("   AND H.AREA_ID = K.REGION_CODE\n");
		sbSql.append(MaterialGroupManagerDao.getPoseIdBusinessSqlByPar("D.YIELDLY",poseId,params));//车厂端查询列表产地数据权限
		if(!"".equals(vin)){
			sbSql.append("   AND D.VIN = ?"); 
			params.add(vin);
		}
		if(billStartdate!=null&&!"".equals(billStartdate)){//
			sbSql.append("   AND C.OUT_DATE>=TO_DATE(?,'YYYY-MM-DD HH24:MI:SS')\n" );
			params.add(billStartdate+" 00:00:00");
		}
		if(billEnddate!=null&&!"".equals(billEnddate)){//
			sbSql.append("  AND C.OUT_DATE<=TO_DATE(?,'YYYY-MM-DD HH24:MI:SS')\n" );
			params.add(billEnddate +" 23:59:59");
		}
		if(groupId!=null&&!"".equals(groupId)){//
			sbSql.append("  AND  G.SERIES_ID=?\n" );
			params.add(groupId);
		}
		if(yieldly!=null&&!"".equals(yieldly)){//
			sbSql.append("  AND D.yieldly=?\n" );
			params.add(yieldly);
		}
		if(logiName!=null&&!"".equals(logiName)){
			sbSql.append(" AND B.LOGI_ID = ?\n");
			params.add(logiName);
		}
		if(sdNumber!=null&&!"".equals(sdNumber)){
			sbSql.append(" AND D.SD_NUMBER = ?\n");
			params.add(sdNumber);
		}
		if(orderType!=null&&!"".equals(orderType)){
			sbSql.append(" AND B.ORDER_TYPE =?\n");
			params.add(orderType);
		}
		sbSql.append("  ORDER BY D.ORG_STORAGE_DATE ASC\n");
		Object[] arr=new Object[2];
		arr[0]=sbSql;
		arr[1]=params;
		return arr;
	}
}
