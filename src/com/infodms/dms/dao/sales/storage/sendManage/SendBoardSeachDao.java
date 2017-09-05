package com.infodms.dms.dao.sales.storage.sendManage;

import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.infodms.dms.common.Constant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.common.materialManager.MaterialGroupManagerDao;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

/**
 * 
 * @ClassName     : SendAssignmentDao 
 * @Description   : 发运组板查询DAO 
 * @author        : ranjian
 * CreateDate     : 2013-4-22
 */
public class SendBoardSeachDao extends BaseDao<PO>{
	private static final SendBoardSeachDao dao = new SendBoardSeachDao ();
	public static final SendBoardSeachDao getInstance() {
		return dao;
	}
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		return null;
	}
	/**
	 * 组板信息查询
	 * @param map
	 * @param curPage
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	public PageResult<Map<String, Object>> getSendBoardSeachQuery(Map<String, Object> map, int curPage, int pageSize)throws Exception{
		Object[] obj=getSQL(map);
		PageResult<Map<String, Object>> ps= dao.pageQuery(obj[0].toString(), (List<Object>)obj[1], getFunName(),pageSize,curPage);
		return ps;
	}
	/**
	 * 组板物料列表查询
	 * @param params 查询参数LIST
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> getSendBoardMatListQuery(List<Object> params)throws Exception{
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT TVD.ORD_NO,\n" );
		sql.append("       TW.WAREHOUSE_NAME WH_NAME,\n" );
		sql.append("       DECODE(TD.DEALER_SHORTNAME,NULL,SH.WAREHOUSE_NAME,TD.DEALER_SHORTNAME) DEALER_NAME,\n" );
		sql.append("       VMGM.SERIES_NAME,\n" );
		sql.append("       VMGM.MODEL_NAME,\n" );
		sql.append("       VMGM.PACKAGE_NAME,\n" );
		sql.append("       VMGM.MATERIAL_NAME,\n" );
		sql.append("       VMGM.MATERIAL_CODE,\n" );
		sql.append("       VMGM.COLOR_NAME,\n" );
		sql.append("       TVDD.BD_TOTAL DLV_BD_TOTAL, --已组板数量\n" );
		sql.append("       TSBD.BOARD_NUM BD_TOTAL, --本次组板数量\n" );
		sql.append("       nvl(TVDD.FY_TOTAL, 0) FY_TOTAL --发运 数量\n" );
		sql.append("  FROM TT_SALES_BO_DETAIL    TSBD,\n" );
		sql.append("       TT_VS_DLVRY           TVD,\n" );
		sql.append("       TT_VS_DLVRY_DTL       TVDD,\n" );
		sql.append("       VW_MATERIAL_GROUP_MAT VMGM,\n" );
		sql.append("       TM_WAREHOUSE          TW,\n" );
		sql.append("       TM_DEALER             TD,\n" );
		sql.append("       TM_WAREHOUSE          SH\n" );
		sql.append(" WHERE TSBD.OR_DE_ID = TVDD.ORD_DETAIL_ID\n" );
		sql.append("   AND TSBD.MAT_ID = TVDD.MATERIAL_ID\n" );
		sql.append("   AND TVDD.REQ_ID = TVD.REQ_ID\n" );
		sql.append("   AND TVDD.MATERIAL_ID = VMGM.MATERIAL_ID\n" );
		sql.append("   AND TVD.DLV_WH_ID = TW.WAREHOUSE_ID\n" );
		sql.append("   AND TVD.ORD_PUR_DEALER_ID = TD.DEALER_ID(+)\n" );
		sql.append("   AND TVD.DLV_REC_WH_ID=SH.WAREHOUSE_ID(+)\n");
		sql.append("   AND TSBD.BO_ID = ?\n");
		sql.append(" ORDER BY TVD.ORD_NO, TW.WAREHOUSE_NAME, VMGM.MATERIAL_CODE");
		List<Map<String, Object>> list= dao.pageQuery(sql.toString(), params, getFunName());
		return list;
	}
	/**
	 * 根据提车单ID 查询订单信息
	 * @param params 查询参数LIST
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> getSendBoardMatQuery(List<Object> params)throws Exception{
		StringBuffer sbSql= new StringBuffer();
		sbSql.append("SELECT TSA.ASS_ID,\n");
		sbSql.append("       TSA.ORDER_ID,\n");
		sbSql.append("       TSL.LOGI_NAME, --物流商\n");
		sbSql.append("       TD.DEALER_CODE, --订货经销商代码\n");
		sbSql.append("       TD.DEALER_NAME, --订货经销商名称\n");
		sbSql.append("       TSA.SEND_TYPE DELIVERY_TYPE, --发运方式\n");
		sbSql.append("       TD1.DEALER_NAME RE_DEALER_NAME, --收货方\n");
		sbSql.append("       TVA.ADDRESS, --收货地址\n");
		sbSql.append("       TVA.RECEIVE_ORG, --收货单位\n");
		sbSql.append("       NVL(TSA.BOARD_NUM, 0) ASSIGN_BOARD_NUM, --已组板数量(分派表)\n");
		sbSql.append("       NVL(TSA.ORDER_NUM, 0) ORDER_NUM, --订单数\n");
		sbSql.append("       TSA.LINK_MAN, --联系人\n");
		sbSql.append("       TSA.TEL --联系电话\n");
		sbSql.append("  FROM TM_DEALER       TD,\n");
		sbSql.append("       TT_SALES_ASSIGN TSA,\n");
		sbSql.append("       TT_SALES_LOGI   TSL,\n");
		sbSql.append("       TM_VS_ADDRESS   TVA, --地址表\n");
		sbSql.append("       TM_DEALER       TD1 --收货方\n");
		sbSql.append(" WHERE TSA.DEALER_ID = TD.DEALER_ID\n");
		sbSql.append("   AND TSA.LOGI_ID = TSL.LOGI_ID\n");
		sbSql.append("   AND TSA.REC_DEALER_ID = TD1.DEALER_ID\n");
		sbSql.append("   AND TSA.ADDRESS_ID = TVA.ID\n");
		sbSql.append("   AND TSA.ORDER_ID = ?"); 
		Map<String, Object> map= dao.pageQueryMap(sbSql.toString(), params, getFunName());
		return map;
	}
	/**
	 * 修改组板明细表信息
	 * @param params 参数list
	 * @return
	 * @throws Exception
	 */
	public void updateSendBoardDel(List<Object> params) throws Exception{
		StringBuffer sbSql= new StringBuffer();
		sbSql.append("update TT_SALES_BO_DETAIL TSBD\n");
		sbSql.append("   set TSBD.BOARD_NUM   = NVL(TSBD.BOARD_NUM + ?, 0),\n");
		sbSql.append("       TSBD.UPDATE_BY   = ?,\n");
		sbSql.append("       TSBD.UPDATE_DATE = sysdate\n");
		sbSql.append(" WHERE TSBD.BO_DE_ID = ?"); 
		dao.update(sbSql.toString(), params);
	}
	/**
	 * 根据组板ID获取组板信息
	 * @param boId 组板ID
	 */
	public Map<String,Object> getBoardByBoId(List<Object> params){
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT TSB.BO_ID,\n" );
		sql.append("       TSB.BO_NO,\n" );
		sql.append("       TSB.CAR_NO,\n" );
		sql.append("       TSB.DRIVER_NAME,\n" );
		sql.append("       TSB.DRIVER_TEL,\n" );
		sql.append("       TSB.LOADS,\n" );
		sql.append("       to_char(TSB.BO_DATE, 'yyyy-mm-dd') BO_DATE,\n" );
		sql.append("       TSB.CAR_TEAM,\n" );
		sql.append("       TSB.DRIVER_NAME,\n" );
		sql.append("       TSB.DRIVER_TEL,\n" );
		sql.append("       TSL.LOGI_NAME,\n" );
		sql.append("       (SELECT TC.CODE_DESC\n" );
		sql.append("          FROM TC_CODE TC\n" );
		sql.append("         WHERE TC.CODE_ID = TSB.DLV_SHIP_TYPE) SHIP_NAME,\n" );
		sql.append("       TR1.REGION_NAME || TR2.REGION_NAME || TR3.REGION_NAME BAL_ADDR,\n" );
		sql.append("       DECODE(TSB.PLAN_LOAD_DATE,NULL,to_char(SYSDATE, 'yyyy-mm-dd'),to_char(TSB.PLAN_LOAD_DATE, 'yyyy-mm-dd')) PLAN_LOAD_DATE,\n" );
		sql.append("       to_char(TSB.DLV_FY_DATE, 'yyyy-mm-dd') DLV_FY_DATE,\n" );
		sql.append("       to_char(TSB.DLV_JJ_DATE, 'yyyy-mm-dd') DLV_JJ_DATE\n" );
		sql.append("  FROM TT_SALES_BOARD TSB,\n" );
		sql.append("       TT_SALES_LOGI  TSL,\n" );
		sql.append("       TM_REGION      TR1,\n" );
		sql.append("       TM_REGION      TR2,\n" );
		sql.append("       TM_REGION      TR3\n" );
		sql.append(" WHERE 1 = 1\n" );
		sql.append("   AND TSB.DLV_LOGI_ID = TSL.LOGI_ID(+)\n" );
		sql.append("   AND TR1.REGION_ID = TR2.PARENT_ID\n" );
		sql.append("   AND TR2.REGION_ID = TR3.PARENT_ID\n" );
		sql.append("   AND TR1.REGION_CODE = TSB.DLV_BAL_PROV_ID\n" );
		sql.append("   AND TR2.REGION_CODE = TSB.DLV_BAL_CITY_ID\n" );
		sql.append("   AND TR3.REGION_CODE = TSB.DLV_BAL_COUNTY_ID\n" );
		sql.append("   AND TSB.BO_ID = ?");
		Map<String, Object> map= dao.pageQueryMap(sql.toString(), params, getFunName());
		return map;	
	}
	/**
	 * 导出EXCEL
	 * @param params 查询参数LIST
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> getSendBoardSeachQueryExport(Map<String, Object> map){
		Object[] obj=getSQL(map);
		List<Map<String, Object>> list= dao.pageQuery(obj[0].toString(), (List<Object>)obj[1], getFunName());
		return list;
	}
	/**
	 * 查询统计
	 *param map 查询参数
	 *@return 
	 */
	public Map<String,Object> tgSum(Map<String, Object> map){
		Object[] obj=getSQL(map);
		StringBuffer sbSql=new StringBuffer();
		sbSql.append("SELECT SUM(NVL(BO_NUM, 0)) BO_NUM,\n");
		sbSql.append("       SUM(NVL(ALLOCA_NUM, 0)) ALLOCA_NUM,\n");
		sbSql.append("       SUM(NVL(SEND_NUM, 0)) SEND_NUM,\n");
		sbSql.append("       SUM(NVL(ACC_NUM, 0)) ACC_NUM,\n");
		sbSql.append("       SUM(NVL(OUT_NUM, 0)) OUT_NUM FROM (\n");
		sbSql.append(obj[0].toString());
		sbSql.append(")\n");
		Map<String, Object> mapR= dao.pageQueryMap(sbSql.toString(), (List<Object>)obj[1], getFunName());
		return mapR;
	}
	//get sql
	public Object[] getSQL(Map<String, Object> map){
		/******************************页面查询字段start**************************/
		String raiseStartDate =CommonUtils.checkNull((String)map.get("raiseStartDate")); // 提报日期开始
		String raiseEndDate = CommonUtils.checkNull((String)map.get("raiseEndDate")); // 提报日期结束
		String boNo = CommonUtils.checkNull((String)map.get("boNo")); //组板编号
		String poseId = CommonUtils.checkNull((String)map.get("poseId"));
		String logiName = CommonUtils.checkNull((String)map.get("logiName")); //物流商
		String transportType = CommonUtils.checkNull((String)map.get("transportType"));//发运方式
		String provinceId = CommonUtils.checkNull((String)map.get("provinceId")); //结算省份
		String cityId = CommonUtils.checkNull((String)map.get("cityId"));//结算城市
		String countyId = CommonUtils.checkNull((String)map.get("countyId")); //结算区县
		
		String posBusType=(String)map.get("posBusType");
		String logiIdU=String.valueOf(map.get("logiId"));//当前职位所对应承运商ID
		/******************************页面查询字段end***************************/
		List<Object> params = new LinkedList<Object>();
		StringBuffer sql= new StringBuffer();
		sql.append("select TSB.BO_ID,\n" );
		sql.append("       TSB.BO_NO,\n" );
		sql.append("       TSB.DLV_SHIP_TYPE,\n" );
		sql.append("       (SELECT TC.CODE_DESC\n" );
		sql.append("          FROM TC_CODE TC\n" );
		sql.append("         WHERE TC.CODE_ID = TSB.DLV_SHIP_TYPE) SHIP_NAME,\n" );
		sql.append("       TSL.LOGI_NAME,\n" );
		sql.append("       TSB.DLV_BAL_PROV_ID,\n" );
		sql.append("       TSB.DLV_BAL_CITY_ID,\n" );
		sql.append("       TSB.DLV_BAL_COUNTY_ID,\n" );
		sql.append("       TR1.REGION_NAME || TR2.REGION_NAME || TR3.REGION_NAME BAL_ADDR,\n" );
		sql.append("       to_char(TSB.BO_DATE, 'yyyy-mm-dd') BO_DATE,\n" );
		sql.append("       NVL(TSB.BO_NUM, 0) BO_NUM, --组板数量\n" );
		sql.append("       NVL(TSB.ALLOCA_NUM, 0) ALLOCA_NUM, --配车数量\n" );
		sql.append("       NVL(TSB.OUT_NUM, 0) OUT_NUM, --出库数量\n" );
		sql.append("       NVL(TSB.Send_Num, 0) SEND_NUM, --发运数量\n" );
		sql.append("		NVL(TSB.ACC_NUM, 0) ACC_NUM, --验收数量\n" );
		sql.append("      (select TU.NAME from tc_user tu where tu.user_id = TSB.AUDIT_BY) AUDIT_BY,\n" );
		sql.append("      TSB.AUDIT_REMARK,\n" );
		sql.append("      to_char(TSB.AUDIT_TIME,'yyyy-mm-dd') AUDIT_TIME");
		sql.append("  from tt_sales_board tsb,\n" );
		sql.append("       TT_SALES_LOGI  TSL,\n" );
		sql.append("       TM_REGION      TR1,\n" );
		sql.append("       TM_REGION      TR2,\n" );
		sql.append("       TM_REGION      TR3\n" );
		sql.append(" WHERE 1 = 1\n" );
		sql.append("   AND TSB.DLV_LOGI_ID = TSL.LOGI_ID(+)\n" );
		sql.append("   AND TR1.REGION_ID = TR2.PARENT_ID\n" );
		sql.append("   AND TR2.REGION_ID = TR3.PARENT_ID\n" );
		sql.append("   AND TR1.REGION_CODE = TSB.DLV_BAL_PROV_ID\n" );
		sql.append("   AND TR2.REGION_CODE = TSB.DLV_BAL_CITY_ID\n" );
		sql.append("   AND TR3.REGION_CODE = TSB.DLV_BAL_COUNTY_ID\n" );
		sql.append("   AND TSB.BO_STATUS = '1'\n" );
		
		if(posBusType.equals(String.valueOf(Constant.POSE_BUS_TYPE_WL))){//物流商
			sql.append("   AND TSB.DLV_LOGI_ID= ?\n" );
			params.add(logiIdU);
		}
		
		if(boNo!=null&&!"".equals(boNo)){
			sql.append("AND TSB.BO_NO LIKE ?\n");		
			params.add("%"+boNo+"%");
		}
		if(transportType!=null&&!"".equals(transportType)){
			sql.append("AND TSB.DLV_SHIP_TYPE= ?\n");		
			params.add(transportType);
		}
		if(logiName!=null&&!"".equals(logiName)){
			sql.append("AND TSB.DLV_LOGI_ID =?\n");		
			params.add(logiName);
		}
		if(raiseStartDate!=null&&!"".equals(raiseStartDate)){//提交日期开始过滤
			sql.append("   AND TSB.BO_DATE>=TO_DATE(?,'YYYY-MM-DD HH24:MI:SS')\n" );
			params.add(raiseStartDate+" 00:00:00");
		}
		if(raiseEndDate!=null&&!"".equals(raiseEndDate)){//提交日期结束过滤
			sql.append("  AND TSB.BO_DATE<=TO_DATE(?,'YYYY-MM-DD HH24:MI:SS')\n" );
			params.add(raiseEndDate +" 23:59:59");
		}
		if(provinceId!=null&&!"".equals(provinceId)){
			sql.append(" AND TSB.Dlv_Bal_Prov_Id =?\n");		
			params.add(provinceId);
		}
		if(cityId!=null&&!"".equals(cityId)){
			sql.append(" AND TSB.Dlv_Bal_City_Id=?\n");
			params.add(cityId);
		}
		if(countyId!=null&&!"".equals(countyId)){
			sql.append(" AND TSB.Dlv_Bal_County_Id=?\n");
			params.add(countyId);
		}
		sql.append(" order by tsb.bo_date desc");
		Object[] arr=new Object[2];
		arr[0]=sql;
		arr[1]=params;
		return arr;
	}
}
