package com.infodms.dms.dao.sales.storage.sendManage;

import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.infodms.dms.common.Constant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.common.materialManager.MaterialGroupManagerDao;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.po.TtVsOrderPO;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

/**
 * 发运计划发送
 * @author shuyh
 * 2017/8/1
 */
public class DlvPlanManageDao extends BaseDao<PO>{
	private static final DlvPlanManageDao dao = new DlvPlanManageDao ();
	public static final DlvPlanManageDao getInstance() {
		return dao;
	}
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		return null;
	}
	/**
	 * 导出EXCEL
	 * @param params 查询参数LIST
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> getDlvPlanQueryExport(Map<String, Object> map){
		Object[] obj=getSQL(map);
		List<Map<String, Object>> list= dao.pageQuery(obj[0].toString(), (List<Object>)obj[1], getFunName());
		return list;
	}
	/**
	 * 导出发运计划明细数据
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getDlvPlanQueryExpDel(Map<String, Object> map){
		String raiseStartDate =CommonUtils.checkNull((String)map.get("raiseStartDate")); // 提报日期开始
		String raiseEndDate = CommonUtils.checkNull((String)map.get("raiseEndDate")); // 提报日期结束
		String boNo = CommonUtils.checkNull((String)map.get("boNo")); //组板编号
		String poseId = CommonUtils.checkNull((String)map.get("poseId"));
		String logiName = CommonUtils.checkNull((String)map.get("logiName")); //物流商
		String transportType = CommonUtils.checkNull((String)map.get("transportType"));//发运方式
//		String provinceId = CommonUtils.checkNull((String)map.get("provinceId")); //结算省份
//		String cityId = CommonUtils.checkNull((String)map.get("cityId"));//结算城市
//		String countyId = CommonUtils.checkNull((String)map.get("countyId")); //结算区县
		String isBill = CommonUtils.checkNull((String)map.get("isBill")); //是否已生成交接单
		String pFlag = CommonUtils.checkNull((String)map.get("pFlag")); //不为空表示发运计划查询
		
		String posBusType=(String)map.get("posBusType");
		String logiIdU=String.valueOf(map.get("logiId"));//当前职位所对应承运商ID
		
		List<Object> params = new LinkedList<Object>();
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT TSB.BO_NO,\n" );
		sql.append("       TVD.ORD_NO,\n" );
		sql.append("       TW.WAREHOUSE_NAME WH_NAME,\n" );
		sql.append("       TD.DEALER_SHORTNAME DEALER_NAME,\n" );
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
		sql.append("       TT_SALES_BOARD        TSB,\n" );
		sql.append("       TT_VS_DLVRY           TVD,\n" );
		sql.append("       TT_VS_DLVRY_DTL       TVDD,\n" );
		sql.append("       VW_MATERIAL_GROUP_MAT VMGM,\n" );
		sql.append("       TM_WAREHOUSE          TW,\n" );
		sql.append("       TM_DEALER             TD\n" );
		sql.append(" WHERE TSBD.OR_DE_ID = TVDD.ORD_DETAIL_ID\n" );
		sql.append("   AND TSBD.MAT_ID = TVDD.MATERIAL_ID\n" );
		sql.append("   AND TVDD.REQ_ID = TVD.REQ_ID\n" );
		sql.append("   AND TVDD.MATERIAL_ID = VMGM.MATERIAL_ID\n" );
		sql.append("   AND TVD.DLV_WH_ID = TW.WAREHOUSE_ID\n" );
		sql.append("   AND TVD.ORD_PUR_DEALER_ID = TD.DEALER_ID(+)\n" );
		sql.append("   AND TSBD.BO_ID = TSB.BO_ID\n" );
		if(pFlag!=null&&!"".equals(pFlag)){//发运计划查询
			sql.append("                            AND TSB.HANDLE_STATUS >= "+Constant.HANDLE_STATUS06+"\n");//已发运
			
			if(posBusType.equals(String.valueOf(Constant.POSE_BUS_TYPE_WL))){//物流商
				sql.append("   AND TSB.DLV_LOGI_ID= ?\n" );
				params.add(logiIdU);
			}
		}
//		else{
//			sql.append("                            AND TVD.DLV_STATUS = "+Constant.ORDER_STATUS_06+"\n");//已组板确认
//		}
		if(isBill.equals(Constant.IF_TYPE_YES.toString())){
			sql.append("   AND TSBD.BILL_ID IS NOT NULL\n");//已生成交接单
		}else if(isBill.equals(Constant.IF_TYPE_NO.toString())){
			sql.append("   AND TSBD.BILL_ID IS NULL\n");//未生成交接单
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
//		if(provinceId!=null&&!"".equals(provinceId)){
//			sql.append(" AND TSB.Dlv_Bal_Prov_Id =?\n");		
//			params.add(provinceId);
//		}
//		if(cityId!=null&&!"".equals(cityId)){
//			sql.append(" AND TSB.Dlv_Bal_City_Id=?\n");
//			params.add(cityId);
//		}
//		if(countyId!=null&&!"".equals(countyId)){
//			sql.append(" AND TSB.Dlv_Bal_County_Id=?\n");
//			params.add(countyId);
//		}
		sql.append(" ORDER BY TSB.BO_NO, TVD.ORD_NO, VMGM.MATERIAL_CODE");
		List<Map<String, Object>> list= dao.pageQuery(sql.toString(),params, getFunName());
		return list;
	}
	/**
	 * 根据组板ID获取打印主信息
	 * @param boId
	 * @return
	 */
	public List<Map<String, Object>> getDlvPlanPrintMain(String boId){
		List<Object> params = new LinkedList<Object>();
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT TT.DEALER_NAME, TT.TRANS_NAME\n" );
		sql.append("  FROM (SELECT DECODE(TM.DEALER_SHORTNAME, NULL, SH.WAREHOUSE_NAME, TM.DEALER_SHORTNAME) DEALER_NAME,\n" );
		sql.append("               (SELECT TC.CODE_DESC\n" );
		sql.append("                  FROM TC_CODE TC\n" );
		sql.append("                 WHERE TC.CODE_ID = TSB.DLV_SHIP_TYPE) TRANS_NAME\n" );
//		sql.append("               TR1.REGION_NAME || TR2.REGION_NAME || TR3.REGION_NAME || TVD.REQ_REC_ADDR BAL_ADDR\n" );
		sql.append("          FROM TT_SALES_BOARD     TSB,\n" );
		sql.append("               TT_SALES_BO_DETAIL TSBD,\n" );
		sql.append("               TT_VS_DLVRY        TVD,\n" );
		sql.append("               TM_DEALER          TM,\n" );
		sql.append("               TM_WAREHOUSE       SH\n" );
//		sql.append("               TM_REGION          TR1,\n" );
//		sql.append("               TM_REGION          TR2,\n" );
//		sql.append("               TM_REGION          TR3\n" );
		sql.append("         WHERE TSB.BO_ID = TSBD.BO_ID\n" );
		sql.append("           AND TVD.ORD_ID = TSBD.ORDER_ID\n" );
		sql.append("           AND TVD.ORD_PUR_DEALER_ID = TM.DEALER_ID(+)\n" );
		sql.append("           AND TVD.DLV_REC_WH_ID = SH.WAREHOUSE_ID(+)\n" );
//		sql.append("           AND TR1.REGION_ID = TR2.PARENT_ID\n" );
//		sql.append("           AND TR2.REGION_ID = TR3.PARENT_ID\n" );
//		sql.append("           AND TR1.REGION_CODE = TSB.DLV_BAL_PROV_ID\n" );
//		sql.append("           AND TR2.REGION_CODE = TSB.DLV_BAL_CITY_ID\n" );
//		sql.append("           AND TR3.REGION_CODE = TSB.DLV_BAL_COUNTY_ID\n" );
		sql.append("           AND TSB.BO_ID = "+boId+") TT\n" );
		sql.append(" GROUP BY TT.DEALER_NAME, TT.TRANS_NAME");

		List<Map<String, Object>> list= dao.pageQuery(sql.toString(),null, getFunName());
		return list;
	}
	/**
	 * 获取运单 打印明细
	 * @param boId
	 * @param dealerId
	 * @return
	 */
	public List<Map<String, Object>> getDlvPlanPrintDetail(String boId,String dealerName,String transName){
		List<Object> params = new LinkedList<Object>();
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT *\n" );
		sql.append("  FROM (SELECT TSB.BO_NO,\n" );
		sql.append("               TVD.ORD_NO,\n" );
		sql.append("               TW.WAREHOUSE_NAME WH_NAME,\n" );
		sql.append("               DECODE(TD.DEALER_SHORTNAME,\n" );
		sql.append("                      NULL,\n" );
		sql.append("                      SH.WAREHOUSE_NAME,\n" );
		sql.append("                      TD.DEALER_SHORTNAME) DEALER_NAME,\n" );
		sql.append("               (SELECT TC.CODE_DESC\n" );
		sql.append("                  FROM TC_CODE TC\n" );
		sql.append("                 WHERE TC.CODE_ID = TSB.DLV_SHIP_TYPE) TRANS_NAME,\n" );
		sql.append("               TR1.REGION_NAME || TR2.REGION_NAME || TR3.REGION_NAME ||TVD.REQ_REC_ADDR BAL_ADDR,\n" );
		sql.append("               VMGM.SERIES_NAME,\n" );
		sql.append("               VMGM.MODEL_NAME,\n" );
		sql.append("               VMGM.PACKAGE_NAME,\n" );
		sql.append("               VMGM.MATERIAL_NAME,\n" );
		sql.append("               VMGM.MATERIAL_CODE,\n" );
		sql.append("               VMGM.COLOR_NAME,\n" );
		sql.append("               TVDD.BD_TOTAL DLV_BD_TOTAL, --已组板数量\n" );
		sql.append("               TSBD.BOARD_NUM BD_TOTAL, --本次组板数量\n" );
		sql.append("               nvl(TVDD.FY_TOTAL, 0) FY_TOTAL --发运 数量\n" );
		sql.append("          FROM TT_SALES_BO_DETAIL    TSBD,\n" );
		sql.append("               TT_SALES_BOARD        TSB,\n" );
		sql.append("               TT_VS_DLVRY           TVD,\n" );
		sql.append("               TT_VS_DLVRY_DTL       TVDD,\n" );
		sql.append("               VW_MATERIAL_GROUP_MAT VMGM,\n" );
		sql.append("               TM_WAREHOUSE          TW,\n" );
		sql.append("               TM_DEALER             TD,\n" );
		sql.append("               TM_WAREHOUSE          SH,\n" );
		sql.append("               TM_REGION             TR1,\n" );
		sql.append("               TM_REGION             TR2,\n" );
		sql.append("               TM_REGION             TR3\n" );
		sql.append("         WHERE TSBD.OR_DE_ID = TVDD.ORD_DETAIL_ID\n" );
		sql.append("           AND TSBD.MAT_ID = TVDD.MATERIAL_ID\n" );
		sql.append("           AND TVDD.REQ_ID = TVD.REQ_ID\n" );
		sql.append("           AND TVDD.MATERIAL_ID = VMGM.MATERIAL_ID\n" );
		sql.append("           AND TVD.DLV_WH_ID = TW.WAREHOUSE_ID\n" );
		sql.append("           AND TVD.ORD_PUR_DEALER_ID = TD.DEALER_ID(+)\n" );
		sql.append("           AND TVD.DLV_REC_WH_ID = SH.WAREHOUSE_ID(+)\n" );
		sql.append("           AND TR1.REGION_ID = TR2.PARENT_ID\n" );
		sql.append("           AND TR2.REGION_ID = TR3.PARENT_ID\n" );
		sql.append("           AND TR1.REGION_CODE = TSBD.DLV_BAL_PROV_ID\n" );
		sql.append("           AND TR2.REGION_CODE = TSBD.DLV_BAL_CITY_ID\n" );
		sql.append("           AND TR3.REGION_CODE = TSBD.DLV_BAL_COUNTY_ID\n" );
		sql.append("           AND TSBD.BO_ID = TSB.BO_ID\n");
		sql.append("           AND TSB.BO_ID = "+boId+") TT\n" );
		sql.append(" WHERE TT.DEALER_NAME = '"+dealerName+"'\n" );
		sql.append("   AND TT.TRANS_NAME = '"+transName+"'\n" );
		//sql.append("   AND TT.BAL_ADDR = '"+balAddr+"'");
		List<Map<String, Object>> list= dao.pageQuery(sql.toString(),null, getFunName());
		return list;
	}
	/**
	 * 发运计划发送查询
	 * @param map
	 * @param curPage
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	public PageResult<Map<String, Object>> getDlvPlanManaQuery(Map<String, Object> map, int curPage, int pageSize)throws Exception{
		Object[] obj=getSQL(map);
		PageResult<Map<String, Object>> ps= dao.pageQuery(obj[0].toString(), (List<Object>)obj[1], getFunName(),pageSize,curPage);
		return ps;
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
//		String provinceId = CommonUtils.checkNull((String)map.get("provinceId")); //结算省份
//		String cityId = CommonUtils.checkNull((String)map.get("cityId"));//结算城市
//		String countyId = CommonUtils.checkNull((String)map.get("countyId")); //结算区县
		String isBill = CommonUtils.checkNull((String)map.get("isBill")); //是否已生成交接单
		String pFlag = CommonUtils.checkNull((String)map.get("pFlag")); //不为空表示发运计划查询
		
		String posBusType=(String)map.get("posBusType");
		String logiIdU=String.valueOf(map.get("logiId"));//当前职位所对应承运商ID
		/******************************页面查询字段end***************************/
		List<Object> params = new LinkedList<Object>();
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT TSB.BO_ID,\n" );
		sql.append("       TSB.BO_NO,\n" );
		sql.append("       TSB.DLV_SHIP_TYPE,\n" );
		sql.append("       (SELECT TC.CODE_DESC\n" );
		sql.append("          FROM TC_CODE TC\n" );
		sql.append("         WHERE TC.CODE_ID = TSB.DLV_SHIP_TYPE) SHIP_NAME,\n" );
		sql.append("       TSL.LOGI_NAME,\n" );
		//sql.append("       TR1.REGION_NAME || TR2.REGION_NAME || TR3.REGION_NAME BAL_ADDR,\n" );
		sql.append("       to_char(TSB.BO_DATE, 'yyyy-mm-dd') BO_DATE,\n" );
		sql.append("       NVL(TSB.BO_NUM, 0) BO_NUM,\n" );
		sql.append("       DECODE(TSB.PLAN_LOAD_DATE,\n" );
		sql.append("              NULL,\n" );
		sql.append("              to_char(SYSDATE, 'yyyy-mm-dd'),\n" );
		sql.append("              to_char(TSB.PLAN_LOAD_DATE, 'yyyy-mm-dd')) PLAN_LOAD_DATE,\n" );
		sql.append("       to_char(TSB.DLV_FY_DATE, 'yyyy-mm-dd') DLV_FY_DATE,\n" );
		sql.append("       to_char(TSB.DLV_JJ_DATE, 'yyyy-mm-dd') DLV_JJ_DATE,\n" );
		sql.append("       DECODE(TTS.BILL_ID, NULL, '否', '是') AS HAS_BILL\n" );
		sql.append("  FROM TT_SALES_BOARD TSB,\n" );
		sql.append("       TT_SALES_LOGI TSL,\n" );
//		sql.append("       TM_REGION TR1,\n" );
//		sql.append("       TM_REGION TR2,\n" );
//		sql.append("       TM_REGION TR3,\n" );
		sql.append("       (SELECT TSBD.BO_ID, TSBD.BILL_ID\n" );
		sql.append("          FROM TT_SALES_BO_DETAIL TSBD\n" );
		sql.append("         GROUP BY TSBD.BO_ID, TSBD.BILL_ID) TTS\n" );
		sql.append(" WHERE 1 = 1\n" );
		sql.append("   AND TSB.BO_STATUS = '1'\n" );
		sql.append("   AND TSB.DLV_LOGI_ID = TSL.LOGI_ID(+)\n" );
//		sql.append("   AND TR1.REGION_ID = TR2.PARENT_ID\n" );
//		sql.append("   AND TR2.REGION_ID = TR3.PARENT_ID\n" );
//		sql.append("   AND TR1.REGION_CODE = TSB.DLV_BAL_PROV_ID\n" );
//		sql.append("   AND TR2.REGION_CODE = TSB.DLV_BAL_CITY_ID\n" );
//		sql.append("   AND TR3.REGION_CODE = TSB.DLV_BAL_COUNTY_ID\n" );
		sql.append("   AND TSB.BO_ID = TTS.BO_ID\n");
		if(isBill.equals(Constant.IF_TYPE_YES.toString())){
			sql.append("   AND TTS.BILL_ID IS NOT NULL\n");//已生成交接单
		}else if(isBill.equals(Constant.IF_TYPE_NO.toString())){
			sql.append("   AND TTS.BILL_ID IS NULL\n");//未生成交接单
		}
		if(pFlag!=null&&!"".equals(pFlag)){//发运计划查询
//			sql.append("   AND TSB.BO_ID IN (SELECT TSBD.BO_ID\n" );
//			sql.append("                           FROM TT_SALES_BO_DETAIL TSBD, TT_VS_DLVRY TVD\n" );
//			sql.append("                          WHERE TVD.ORD_ID = TSBD.ORDER_ID\n" );
//			sql.append("                            AND TVD.DLV_STATUS = "+Constant.ORDER_STATUS_07+")\n");//已发运
			//sql.append("   AND TSB.HANDLE_STATUS="+Constant.HANDLE_STATUS11+"\n");//计划已发送
			if(posBusType.equals(String.valueOf(Constant.POSE_BUS_TYPE_WL))){//物流商
				sql.append("   AND TSB.DLV_LOGI_ID= ?\n" );
				params.add(logiIdU);
			}
		}else{
			sql.append("   AND TSB.HANDLE_STATUS<"+Constant.HANDLE_STATUS06+"\n");//未配车
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
//		if(provinceId!=null&&!"".equals(provinceId)){
//			sql.append(" AND TSB.Dlv_Bal_Prov_Id =?\n");		
//			params.add(provinceId);
//		}
//		if(cityId!=null&&!"".equals(cityId)){
//			sql.append(" AND TSB.Dlv_Bal_City_Id=?\n");
//			params.add(cityId);
//		}
//		if(countyId!=null&&!"".equals(countyId)){
//			sql.append(" AND TSB.Dlv_Bal_County_Id=?\n");
//			params.add(countyId);
//		}
		sql.append("   ORDER BY TSB.BO_DATE DESC");
		Object[] arr=new Object[2];
		arr[0]=sql;
		arr[1]=params;
		return arr;
	}
	/**
	 * 根据组板ID获取发运IDs
	 * @param boId
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> getDlvIdByBoId(String boId)throws Exception{
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT DISTINCT TVD.REQ_ID,NVL(TVD.DLV_BD_TOTAL,0) DLV_BD_TOTAL,TVD.DLV_TYPE\n" );
		sql.append("   FROM TT_SALES_BO_DETAIL TSBD, TT_VS_DLVRY_DTL TVDD, TT_VS_DLVRY TVD\n" );
		sql.append("  WHERE 1=1\n" );
		sql.append("    AND TSBD.OR_DE_ID = TVDD.ORD_DETAIL_ID\n" );
		sql.append("    AND TSBD.MAT_ID=TVDD.MATERIAL_ID\n" );
		sql.append("    AND TVDD.REQ_ID=TVD.REQ_ID\n" );
		sql.append("    AND TSBD.BO_ID = "+boId+"\n");
		List<Map<String, Object>> list= dao.pageQuery(sql.toString(),null, getFunName());
		return list;
	}
	/**
	 * 更新组板明细表的发运数量
	 * @param userId
	 * @param boId
	 * @throws Exception
	 */
	public void updateBoDetailFy(long userId,String boId) throws Exception
	{
		StringBuffer sql= new StringBuffer();
		sql.append("update tt_sales_bo_detail tt\n" );
		sql.append("   set tt.send_num    = nvl(tt.board_num, 0),\n" );
		sql.append("       tt.update_by   = "+userId+",\n" );
		sql.append("       tt.update_date = sysdate\n" );
		sql.append(" where tt.bo_id = "+boId+"\n");
		dao.update(sql.toString(), null);
	}
	/**
	 * 根据调拨单ID更新发运明细表的发运数量
	 * @param userId
	 * @param boId
	 * @throws Exception
	 */
	public void updateDlvDetailFy(long userId,String boId) throws Exception
	{
		StringBuffer sql= new StringBuffer();
		sql.append("update TT_VS_DLVRY_DTL TVDD\n" );
		sql.append("       SET TVDD.FY_TOTAL    = NVL(TVDD.BD_TOTAL, 0),\n" );
		sql.append("           TVDD.UPDATE_BY   = "+userId+",\n" );
		sql.append("           TVDD.UPDATE_DATE = SYSDATE\n" );
		sql.append("     WHERE TVDD.ORD_ID IN\n" );
		sql.append("           (SELECT TSBD.ORDER_ID\n" );
		sql.append("              FROM TT_SALES_BO_DETAIL TSBD\n" );
		sql.append("             WHERE TSBD.BO_ID = "+boId+")\n");
		dao.update(sql.toString(), null);
	}
	/**
	 * 更新调拨单明细表的发运数量
	 * @param userId
	 * @param boId
	 * @throws Exception
	 */
	public void updateDispDetFy(long userId,String boId) throws Exception
	{
		StringBuffer sql= new StringBuffer();
		sql.append("update TT_VS_DISPATCH_ORDER_DTL TVDD\n" );
		sql.append("      SET TVDD.DELIVERY_AMOUNT = NVL(TVDD.BOARD_NUMBER, 0),\n" );
		sql.append("          TVDD.UPDATE_BY   ="+userId+",\n" );
		sql.append("          TVDD.UPDATE_DATE = SYSDATE\n" );
		sql.append("    WHERE TVDD.DETAIL_ID IN\n" );
		sql.append("          (SELECT TSBD.OR_DE_ID\n" );
		sql.append("             FROM TT_SALES_BO_DETAIL TSBD\n" );
		sql.append("            WHERE TSBD.BO_ID = "+boId+")");
		dao.update(sql.toString(), null);
	}
	/**
	 * 更新调拨单主表的发运数量
	 * @param userId
	 * @param boId
	 * @throws Exception
	 */
	public void updateDispMainFy(long userId,String boId) throws Exception
	{
		StringBuffer sql= new StringBuffer();
		sql.append("update TT_VS_DISPATCH_ORDER TVDD\n" );
		sql.append("       SET TVDD.Send_Num = NVL(TVDD.BO_NUM, 0),\n" );
		sql.append("           TVDD.UPDATE_BY   = "+userId+",\n" );
		sql.append("           TVDD.UPDATE_DATE = SYSDATE\n" );
		sql.append("     WHERE TVDD.DISP_ID IN\n" );
		sql.append("           (SELECT TSBD.ORDER_ID\n" );
		sql.append("              FROM TT_SALES_BO_DETAIL TSBD\n" );
		sql.append("             WHERE TSBD.BO_ID = "+boId+")");
		dao.update(sql.toString(), null);
	}
}
