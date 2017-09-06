package com.infodms.dms.dao.sales.storage.sendManage;

import java.sql.ResultSet;
import java.util.ArrayList;
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
 * 发运交接单管理
 * @author shuyh
 * 2017/8/2
 */
public class DlvWayBillManageDao extends BaseDao<PO>{
	private static final DlvWayBillManageDao dao = new DlvWayBillManageDao ();
	public static final DlvWayBillManageDao getInstance() {
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
	public List<Map<String, Object>> getDlvBillQueryExport(Map<String, Object> map){
		Object[] obj=getSQL(map);
		List<Map<String, Object>> list= dao.pageQuery(obj[0].toString(), (List<Object>)obj[1], getFunName());
		return list;
	}
	/**
	 * 发运交接单管理查询
	 * @param map
	 * @param curPage
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	public PageResult<Map<String, Object>> getDlvBillManaQuery(Map<String, Object> map, int curPage, int pageSize)throws Exception{
		Object[] obj=getSQL(map);
		PageResult<Map<String, Object>> ps= dao.pageQuery(obj[0].toString(), (List<Object>)obj[1], getFunName(),pageSize,curPage);
		return ps;
	}
	//get sql
	public Object[] getSQL(Map<String, Object> map){
		/******************************页面查询字段start**************************/
		String billNo = CommonUtils.checkNull((String)map.get("billNo")); //交接单号
		String orderNo= CommonUtils.checkNull((String)map.get("orderNo"));//订单号
		String yieldly = CommonUtils.checkNull((String)map.get("yieldly")); //发运仓库
		String dealerCode= CommonUtils.checkNull((String)map.get("dealerCode"));//经销商code
		String transType = CommonUtils.checkNull((String)map.get("transType")); //发运方式
		String logiName = CommonUtils.checkNull((String)map.get("logiName")); //承运商ID
		String provinceId = CommonUtils.checkNull((String)map.get("provinceId")); //结算省份
		String cityId = CommonUtils.checkNull((String)map.get("cityId")); // 结算城市
		String countyId = CommonUtils.checkNull((String)map.get("countyId")); // 结算区县
		String startDate = CommonUtils.checkNull((String)map.get("startDate")); //交接日期开始
		String endDate = CommonUtils.checkNull((String)map.get("endDate")); // 交接日期结束
		
		String boNo = CommonUtils.checkNull((String)map.get("boNo")); //组板号
		String billStatus = CommonUtils.checkNull((String)map.get("billStatus")); //交接单状态
		String lastStartDate = CommonUtils.checkNull((String)map.get("lastStartDate")); //最后交车日期开始
		String lastEndDate = CommonUtils.checkNull((String)map.get("lastEndDate")); //最后交车日期结束
		
		String posBusType=(String)map.get("posBusType");
		String logiIdU=String.valueOf(map.get("logiId"));//当前职位所对应承运商ID
		/******************************页面查询字段end***************************/
		List<Object> params = new LinkedList<Object>();
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT DISTINCT TSW.BILL_ID,\n" );
		sql.append("       TSW.BILL_NO, --交接单号\n" );
		sql.append("       TVD.ORD_NO ORDER_NO, --订单号\n" );
		sql.append("       TW.WAREHOUSE_NAME WH_NAME, --发运仓库\n" );
		sql.append("       DECODE(TD.DEALER_SHORTNAME,\n" );
		sql.append("              NULL,\n" );
		sql.append("              SH.WAREHOUSE_NAME,\n" );
		sql.append("              TD.DEALER_SHORTNAME) DEALER_NAME, --经销商名称\n" );
		sql.append("       (select TC.CODE_DESC\n" );
		sql.append("          from tc_code tc\n" );
		sql.append("         where tc.code_id = tvd.dlv_ship_type) SHIP_NAME, --发运方式\n" );
		sql.append("       TSL.LOGI_NAME, --承运商名称\n" );
		sql.append("       TR1.REGION_NAME || TR2.REGION_NAME || TR3.REGION_NAME BAL_ADDR, --结算地\n" );
		sql.append("       TSB.BO_NO, --组板号\n" );
		sql.append("       TSW.VEH_NUM, --交接数量\n" );
		sql.append("       to_char(TVD.DLV_FY_DATE, 'yyyy-mm-dd') DLV_FY_DATE, --最晚发运日期\n" );
		sql.append("       to_char(TVD.DLV_JJ_DATE, 'yyyy-mm-dd') DLV_JJ_DATE, --最晚到货日期\n" );
		sql.append("       (SELECT TC.CODE_DESC\n" );
		sql.append("          FROM TC_CODE TC\n" );
		sql.append("         WHERE TC.CODE_ID = TSW.SEND_STATUS) STATUS_NAME, --交接单状态\n" );
		sql.append("       to_char(TSW.BILL_CRT_DATE, 'yyyy-mm-dd') BILL_CRT_DATE, --交接日期\n" );
		sql.append("       to_char(TSW.LAST_CAR_DATE, 'yyyy-mm-dd') LAST_CAR_DATE --最后交车日期\n" );
		sql.append("  FROM TT_SALES_WAYBILL   TSW, --交接单主表\n" );
		sql.append("       TT_SALES_BOARD     TSB, --组板主表\n" );
		sql.append("       TT_SALES_BO_DETAIL TSBD, --组板明细表\n" );
		sql.append("       TT_VS_DLVRY        TVD, --发运表\n" );
		sql.append("       TT_VS_DLVRY_DTL    TVDD, --发运明细表\n" );
		sql.append("       TM_WAREHOUSE       TW,\n" );
		sql.append("       TM_DEALER          TD,\n" );
		sql.append("       TT_SALES_LOGI      TSL,\n" );
		sql.append("       TM_REGION          TR1,\n" );
		sql.append("       TM_REGION          TR2,\n" );
		sql.append("       TM_REGION          TR3,\n" );
		sql.append("       TM_WAREHOUSE       SH\n" );
		sql.append(" WHERE 1 = 1\n" );
		sql.append("   AND TSB.BO_ID = TSBD.BO_ID\n" );
		sql.append("   AND TSBD.ORDER_ID = TVD.ORD_ID\n" );
		sql.append("   AND TSBD.OR_DE_ID = TVDD.ORD_DETAIL_ID\n" );
		sql.append("   AND TSBD.MAT_ID = TVDD.MATERIAL_ID\n" );
		sql.append("   AND TVD.REQ_ID = TVDD.REQ_ID\n" );
		sql.append("   AND TSW.BILL_ID = TSBD.BILL_ID\n" );
		sql.append("   AND TVD.DLV_WH_ID = TW.WAREHOUSE_ID(+)\n" );
		sql.append("   AND TVD.ORD_PUR_DEALER_ID = TD.DEALER_ID(+)\n" );
		sql.append("   AND TVD.DLV_REC_WH_ID = SH.WAREHOUSE_ID(+)\n" );
		sql.append("   AND TVD.DLV_LOGI_ID = TSL.LOGI_ID(+)\n" );
		sql.append("   AND TR1.REGION_ID = TR2.PARENT_ID\n" );
		sql.append("   AND TR2.REGION_ID = TR3.PARENT_ID\n" );
		sql.append("   AND TR1.REGION_CODE = TVD.DLV_BAL_PROV_ID\n" );
		sql.append("   AND TR2.REGION_CODE = TVD.DLV_BAL_CITY_ID\n" );
		sql.append("   AND TR3.REGION_CODE = TVD.DLV_BAL_COUNTY_ID\n");

		if(posBusType.equals(String.valueOf(Constant.POSE_BUS_TYPE_WL))){//物流商
			sql.append("   AND TSB.DLV_LOGI_ID= ?\n" );
			params.add(logiIdU);
		}
		if(boNo!=null&&!"".equals(boNo)){
			sql.append("AND TSB.BO_NO LIKE ?\n");		
			params.add("%"+boNo+"%");
		}
		if(billNo!=null&&!"".equals(billNo)){
			sql.append("AND TSW.BILL_NO LIKE ?\n");		
			params.add("%"+billNo+"%");
		}
		if(orderNo!=null&&!"".equals(orderNo)){
			sql.append("AND TSBD.ORDER_NO LIKE ?\n");		
			params.add("%"+orderNo+"%");
		}
		if(yieldly!=null&&!"".equals(yieldly)){
			sql.append("AND TVD.DLV_WH_ID= ?\n");		
			params.add(yieldly);
		}
		if (dealerCode != null && !"".equals(dealerCode))
		{//经销商code
			sql.append(Utility.getConSqlByParamForEqual(dealerCode, params,"TD", "DEALER_CODE"));
		}
		if(billStatus!=null&&!"".equals(billStatus)){
			sql.append("AND TSW.SEND_STATUS= ?\n");		
			params.add(billStatus);
		}
		
		if(transType!=null&&!"".equals(transType)){
			sql.append("AND TSB.DLV_SHIP_TYPE= ?\n");		
			params.add(transType);
		}
		if(logiName!=null&&!"".equals(logiName)){
			sql.append("AND TSB.DLV_LOGI_ID =?\n");		
			params.add(logiName);
		}
		if(startDate!=null&&!"".equals(startDate)){//交接日期开始过滤
			sql.append("   AND TSW.BILL_CRT_DATE>=TO_DATE(?,'YYYY-MM-DD HH24:MI:SS')\n" );
			params.add(startDate+" 00:00:00");
		}
		if(endDate!=null&&!"".equals(endDate)){//交接日期结束过滤
			sql.append("  AND TSW.BILL_CRT_DATE<=TO_DATE(?,'YYYY-MM-DD HH24:MI:SS')\n" );
			params.add(endDate +" 23:59:59");
		}
		if(lastStartDate!=null&&!"".equals(lastStartDate)){//最后交车日期开始过滤
			sql.append("   AND TSW.LAST_CAR_DATE>=TO_DATE(?,'YYYY-MM-DD HH24:MI:SS')\n" );
			params.add(lastStartDate+" 00:00:00");
		}
		if(lastEndDate!=null&&!"".equals(lastEndDate)){//最后交车日期结束过滤
			sql.append("  AND TSW.LAST_CAR_DATE<=TO_DATE(?,'YYYY-MM-DD HH24:MI:SS')\n" );
			params.add(lastEndDate +" 23:59:59");
		}
		if(provinceId!=null&&!"".equals(provinceId)){
			sql.append(" AND TSW.Dlv_Bal_Prov_Id =?\n");		
			params.add(provinceId);
		}
		if(cityId!=null&&!"".equals(cityId)){
			sql.append(" AND TSW.Dlv_Bal_City_Id=?\n");
			params.add(cityId);
		}
		if(countyId!=null&&!"".equals(countyId)){
			sql.append(" AND TSW.Dlv_Bal_County_Id=?\n");
			params.add(countyId);
		}
		//sql.append("   ORDER BY TSW.BILL_CRT_DATE DESC");
		Object[] arr=new Object[2];
		arr[0]=sql;
		arr[1]=params;
		return arr;
	}
	/**
	 * 根据交接单ID获取查看信息
	 * @param params
	 * @return
	 */
	public Map<String,Object> getViewMainByBillId(List<Object> params){
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT DISTINCT TSW.BILL_ID,\n" );
		sql.append("       TSW.BILL_NO, --交接单号\n" );
		sql.append("       TVD.ORD_NO ORDER_NO, --订单号\n" );
		sql.append("       TW.WAREHOUSE_NAME WH_NAME, --发运仓库\n" );
		sql.append("       DECODE(TD.DEALER_SHORTNAME,\n" );
		sql.append("              NULL,\n" );
		sql.append("              SH.WAREHOUSE_NAME,\n" );
		sql.append("              TD.DEALER_SHORTNAME) DEALER_NAME, --经销商名称\n" );
		sql.append("       (select TC.CODE_DESC\n" );
		sql.append("          from tc_code tc\n" );
		sql.append("         where tc.code_id = tvd.dlv_ship_type) SHIP_NAME, --发运方式\n" );
		sql.append("       TSL.LOGI_NAME, --承运商名称\n" );
		sql.append("       TR1.REGION_NAME || TR2.REGION_NAME || TR3.REGION_NAME BAL_ADDR, --结算地\n" );
		sql.append("       TSW.VEH_NUM,TVD.REQ_ID,TSB.BO_ID,TSB.BO_NO, --组板号\n" );
		sql.append("       to_char(TSB.BO_DATE, 'yyyy-mm-dd') BO_DATE, --组板日期\n" );
		sql.append("       TSB.DRIVER_NAME, --司机姓名\n" );
		sql.append("       TSB.DRIVER_TEL, --司机电话\n" );
		sql.append("       TSB.CAR_TEAM, --所属车队\n" );
		sql.append("       TSB.CAR_NO, --车牌号\n" );
		sql.append("       to_char(TSB.PLAN_LOAD_DATE, 'yyyy-mm-dd') PLAN_LOAD_DATE, --计划装车日期\n" );
		sql.append("       to_char(TVD.DLV_FY_DATE, 'yyyy-mm-dd') DLV_FY_DATE, --最晚发运日期\n" );
		sql.append("       to_char(TVD.DLV_JJ_DATE, 'yyyy-mm-dd') DLV_JJ_DATE --最晚到货日期\n" );
		sql.append("  FROM TT_SALES_WAYBILL   TSW, --交接单主表\n" );
		sql.append("       TT_SALES_BOARD     TSB, --组板主表\n" );
		sql.append("       TT_SALES_BO_DETAIL TSBD, --组板明细表\n" );
		sql.append("       TT_VS_DLVRY        TVD, --发运表\n" );
		sql.append("       TT_VS_DLVRY_DTL    TVDD, --发运明细表\n" );
		sql.append("       TM_WAREHOUSE       TW,\n" );
		sql.append("       TM_DEALER          TD,\n" );
		sql.append("       TT_SALES_LOGI      TSL,\n" );
		sql.append("       TM_REGION          TR1,\n" );
		sql.append("       TM_REGION          TR2,\n" );
		sql.append("       TM_REGION          TR3,\n" );
		sql.append("       TM_WAREHOUSE       SH\n" );
		sql.append(" WHERE 1 = 1\n" );
		sql.append("   AND TSB.BO_ID = TSBD.BO_ID\n" );
		sql.append("   AND TSBD.ORDER_ID = TVD.ORD_ID\n" );
		sql.append("   AND TSBD.OR_DE_ID = TVDD.ORD_DETAIL_ID\n" );
		sql.append("   AND TSBD.MAT_ID = TVDD.MATERIAL_ID\n" );
		sql.append("   AND TVD.REQ_ID = TVDD.REQ_ID\n" );
		sql.append("   AND TSW.BILL_ID = TSBD.BILL_ID\n" );
		sql.append("   AND TVD.DLV_WH_ID = TW.WAREHOUSE_ID(+)\n" );
		sql.append("   AND TVD.ORD_PUR_DEALER_ID = TD.DEALER_ID(+)\n" );
		sql.append("   AND TVD.DLV_REC_WH_ID = SH.WAREHOUSE_ID(+)\n" );
		sql.append("   AND TVD.DLV_LOGI_ID = TSL.LOGI_ID(+)\n" );
		sql.append("   AND TR1.REGION_ID = TR2.PARENT_ID\n" );
		sql.append("   AND TR2.REGION_ID = TR3.PARENT_ID\n" );
		sql.append("   AND TR1.REGION_CODE = TVD.DLV_BAL_PROV_ID\n" );
		sql.append("   AND TR2.REGION_CODE = TVD.DLV_BAL_CITY_ID\n" );
		sql.append("   AND TR3.REGION_CODE = TVD.DLV_BAL_COUNTY_ID\n" );
		sql.append("   AND TSW.BILL_ID=?");
		Map<String, Object> map= dao.pageQueryMap(sql.toString(), params, getFunName());
		return map;	
	}
	/**
	 * 交接单管理查看物料列表查询
	 * @param params 查询参数LIST
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> getBillMatListQuery(List<Object> params)throws Exception{
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT VMGM.SERIES_NAME,\n" );
		sql.append("       VMGM.MODEL_NAME,\n" );
		sql.append("       VMGM.PACKAGE_NAME,\n" );
		sql.append("       VMGM.MATERIAL_NAME,\n" );
		sql.append("       VMGM.MATERIAL_CODE,\n" );
		sql.append("       VMGM.COLOR_NAME,\n" );
		sql.append("       TSWB.VIN\n" );
		sql.append("  FROM VW_MATERIAL_GROUP_MAT VMGM, TT_SALES_WAY_BILL_DTL TSWB\n" );
		sql.append(" WHERE 1 = 1\n" );
		sql.append("   AND TSWB.MAT_ID = VMGM.MATERIAL_ID\n" );
		sql.append("   AND TSWB.BILL_ID = ?");
		List<Map<String, Object>> list= dao.pageQuery(sql.toString(), params, getFunName());
		return list;
	}
	/**
	 * 根据交接单ID获取打印主信息
	 * @param params
	 * @return
	 */
	public Map<String,Object> getPrintMainByBillId(String billId){
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT distinct TSW.BILL_ID,\n" );
		sql.append("                TSW.BILL_NO, --交接单号\n" );
		sql.append("                FY.WAREHOUSE_NAME, --发运仓库\n" );
		sql.append("                DECODE(TD.DEALER_SHORTNAME,\n" );
		sql.append("                       NULL,\n" );
		sql.append("                       SH.WAREHOUSE_NAME,\n" );
		sql.append("                       TD.DEALER_SHORTNAME) DEALER_NAME, --经销商名称\n" );
		sql.append("                (SELECT TC.CODE_DESC\n" );
		sql.append("                   FROM TC_CODE TC\n" );
		sql.append("                  WHERE TC.CODE_ID = TSB.DLV_SHIP_TYPE) SHIP_NAME, --发运方式\n" );
		sql.append("                TSL.LOGI_NAME, --承运商\n" );
		sql.append("                TSW.ADDRESS_INFO REQ_ADDR, --详细地址\n" );
		sql.append("                TR1.REGION_NAME || TR2.REGION_NAME || TR3.REGION_NAME BAL_ADDR, --结算地\n" );
		sql.append("                TSB.BO_NO, --组板号\n" );
		sql.append("                to_char(TSB.BO_DATE, 'yyyy-mm-dd') BO_DATE, --组板申请日期\n" );
		sql.append("                TSW.VEH_NUM, --交接数量\n" );
		sql.append("                to_char(TSB.DLV_FY_DATE, 'yyyy-mm-dd') DLV_FY_DATE, --最晚发运日期\n" );
		sql.append("                to_char(TSB.DLV_JJ_DATE, 'yyyy-mm-dd') DLV_JJ_DATE, --最晚交货日期\n" );
		sql.append("                NVL(TSW.PRINT_COUNT, 0) PRINT_COUNT --打印次数\n" );
		sql.append("  FROM TT_SALES_WAYBILL   TSW,\n" );
		sql.append("       TT_SALES_BO_DETAIL TSBD,\n" );
		sql.append("       TT_SALES_BOARD     TSB,\n" );
		sql.append("       TT_VS_DLVRY        TVD,\n" );
		sql.append("       TM_WAREHOUSE       FY,\n" );
		sql.append("       TM_WAREHOUSE       SH,\n" );
		sql.append("       TM_DEALER          TD,\n" );
		sql.append("       TT_SALES_LOGI      TSL,\n" );
		sql.append("       TM_REGION          TR1,\n" );
		sql.append("       TM_REGION          TR2,\n" );
		sql.append("       TM_REGION          TR3\n" );
		sql.append(" WHERE TSW.BILL_ID = TSBD.BILL_ID\n" );
		sql.append("   AND TSBD.BO_ID = TSB.BO_ID\n" );
		sql.append("   AND TSBD.ORDER_ID = TVD.ORD_ID\n" );
		sql.append("   AND TVD.DLV_WH_ID = FY.WAREHOUSE_ID\n" );
		sql.append("   AND TSW.OR_DEALER_ID = TD.DEALER_ID(+)\n" );
		sql.append("   AND TSW.OR_DEALER_ID = SH.WAREHOUSE_ID(+)\n" );
		sql.append("   AND TSW.LOGI_ID = TSL.LOGI_ID\n" );
		sql.append("   AND TR1.REGION_ID = TR2.PARENT_ID\n" );
		sql.append("   AND TR2.REGION_ID = TR3.PARENT_ID\n" );
		sql.append("   AND TR1.REGION_CODE = TVD.DLV_BAL_PROV_ID\n" );
		sql.append("   AND TR2.REGION_CODE = TVD.DLV_BAL_CITY_ID\n" );
		sql.append("   AND TR3.REGION_CODE = TVD.DLV_BAL_COUNTY_ID\n");
		sql.append("   AND TSW.BILL_ID = "+billId+"\n");
		Map<String, Object> map= dao.pageQueryMap(sql.toString(), null, getFunName());
		return map;	
	}
	/**
	 * 交接单打印物料明细信息
	 * @param params 查询参数LIST
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> getBillDelPrintQuery(String billId)throws Exception{
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT TSWD.ORDER_NO,\n" );
		sql.append("       MAT.SERIES_NAME,\n" );
		sql.append("       MAT.MODEL_NAME,\n" );
		sql.append("       MAT.PACKAGE_NAME,\n" );
		sql.append("       MAT.COLOR_NAME,\n" );
		sql.append("       MAT.MATERIAL_CODE,\n" );
		sql.append("       TSWD.VIN\n" );
		sql.append("  FROM TT_SALES_WAY_BILL_DTL TSWD, VW_MATERIAL_GROUP_MAT MAT\n" );
		sql.append(" WHERE 1 = 1\n" );
		sql.append("   AND TSWD.MAT_ID = MAT.MATERIAL_ID\n" );
		sql.append("   AND TSWD.BILL_ID = "+billId+"\n");
		List<Map<String, Object>> list= dao.pageQuery(sql.toString(), null, getFunName());
		return list;
	}
	/**
	 * 检验车架号
	 * @param vin
	 * @param poseId
	 * @return
	 */
	public List<Map<String, Object>>   getImportVin(String vin, Long poseId){
		List<Object> params = new ArrayList<Object>();
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT st.*, decode(x.material_group_id,null,'0','1') flag\n" );
		sql.append("  FROM tm_vehicle st\n" );
		sql.append("  LEFT JOIN (SELECT ag.material_group_id, ag.area_id\n" );
		sql.append("               FROM tm_area_group         ag,\n" );
		sql.append("                    tm_business_area      ba,\n" );
		sql.append("                    tm_pose_business_area pba\n" );
		sql.append("              WHERE ag.area_id = ba.area_id\n" );
		sql.append("                AND ba.area_id = pba.area_id\n" );
		sql.append("                AND pba.pose_id = ?) x\n");
		sql.append("       ON st.series_id = x.material_group_id and st.yieldly = x.area_id\n");
		params.add(poseId);
		sql.append("   where st.vin = ?");		
		params.add(vin);
		return pageQuery(sql.toString(), params, getFunName());
	}
	/**
	 * 检查物料编码
	 * @param materialCode
	 * @return
	 */
	public List<Map<String, Object>> getMatarielByCode() {
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT TTS.ROW_NUMBER\n" );
		sql.append("  FROM TMP_TT_SALES_WAY_BILL TTS\n" );
		sql.append("  LEFT JOIN VW_MATERIAL_GROUP_MAT MAT ON MAT.MATERIAL_CODE =\n" );
		sql.append("                                         TTS.MATERIAL_CODE\n" );
		sql.append(" WHERE MAT.MATERIAL_ID IS NULL");
		
		return  pageQuery(sql.toString(), null, getFunName());
		
	}
	/**
	 * 检查物料代码和车架号是否匹配
	 * @return
	 */
	public List<Map<String, Object>> checkIsMatchVin(String flag) {
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT TTS.ROW_NUMBER\n" );
		sql.append("  FROM TMP_TT_SALES_WAY_BILL TTS\n" );
		sql.append("  LEFT JOIN (SELECT TV.VEHICLE_ID, MAT.MATERIAL_CODE, TV.VIN\n" );
		sql.append("               FROM TM_VEHICLE TV, VW_MATERIAL_GROUP_MAT MAT\n" );
		sql.append("              WHERE TV.MATERIAL_ID = MAT.MATERIAL_ID\n" );
		//sql.append("                AND TV.LIFE_CYCLE = 10321002\n" );
		sql.append("                ) TTM ON TTS.MATERIAL_CODE =\n" );
		sql.append("                                                     TTM.MATERIAL_CODE\n" );
		if(flag.equals("1")){//替换车架号
			sql.append("                                                 AND TTS.NEW_VIN = TTM.VIN\n" );
		}else{//原车架号
			sql.append("                                                 AND TTS.VIN = TTM.VIN\n" );
		}
		
		sql.append(" WHERE 1=1 \n");
		if(flag.equals("1")){//替换车架号
			sql.append("AND TTS.NEW_VIN IS NOT NULL\n");
		}
		sql.append("AND TTM.VEHICLE_ID IS NULL\n");
		return  pageQuery(sql.toString(), null, getFunName());
		
	}
	
	public List<Map<String, Object>> checkIsMatchWare(String flag) {
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT TT.ROW_NUMBER\n" );
		sql.append("  FROM TMP_TT_SALES_WAY_BILL TT,\n" );
		sql.append("       TM_VEHICLE TV,\n" );
		sql.append("       VW_MATERIAL_GROUP_MAT MAT,\n" );
		sql.append("       (SELECT TSBD.ORDER_NO, TSB.BO_NO, TSBD.MAT_ID, TVD.DLV_WH_ID\n" );
		sql.append("          FROM TT_SALES_BO_DETAIL TSBD, TT_SALES_BOARD TSB, TT_VS_DLVRY TVD\n" );
		sql.append("         WHERE TSBD.ORDER_ID = TVD.ORD_ID\n" );
		sql.append("           AND TSBD.BO_ID = TSB.BO_ID) TM\n" );
		sql.append(" WHERE TT.BO_NO = TM.BO_NO\n" );
		sql.append("   AND TT.ORDER_NO = TM.ORDER_NO\n" );
		sql.append("   AND TT.MATERIAL_CODE = MAT.MATERIAL_CODE\n" );
		sql.append("   AND MAT.MATERIAL_ID = TM.MAT_ID\n" );
		
		sql.append("   AND TM.DLV_WH_ID != tv.warehouse_id\n");
		if(flag.equals("1")){//替换车架号
			sql.append("   AND TT.New_Vin = TV.VIN\n" );
		}else{//原车架号
			sql.append("   AND TT.VIN = TV.VIN\n" );
		}
		return  pageQuery(sql.toString(), null, getFunName());
		
	}
	/**
	 * 校验组板号,返回错误行
	 */
	public List<Map<String, Object>> checkBoNo(String flag) {
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT TTS.ROW_NUMBER\n" );
		sql.append("  FROM TMP_TT_SALES_WAY_BILL TTS\n" );
		sql.append("  LEFT JOIN (SELECT TSB.BO_NO, TSB.BO_ID\n" );
		sql.append("               FROM TT_SALES_BOARD     TSB,\n" );
		sql.append("                    TT_SALES_BO_DETAIL TSBD,\n" );
		sql.append("                    TT_VS_DLVRY        TVD\n" );
		sql.append("              WHERE TSB.BO_ID = TSBD.BO_ID\n" );
		sql.append("                AND TSBD.ORDER_ID = TVD.ORD_ID\n" );
		if(flag.equals("1")){//是否已生成发运计划
			sql.append("				AND TSB.BO_STATUS='1'\n" );
			sql.append("AND (TSB.handle_status = 9710011 or\n" );
			sql.append("                   TSB.handle_status = 9710006) --针对初次导入的为计划已发送，在途导入的为已生成运单\n");
		}
		sql.append("                ) TTB ON TTS.BO_NO =TTB.BO_NO\n");
		sql.append(" WHERE TTB.BO_ID IS NULL");

		return dao.pageQuery(sql.toString(), null, getFunName());
	}
	/**
	 * 检查原车架号导入是否重复
	 * @return
	 */
	public List<Map<String, Object>> checkVinRepeat() {
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT TMP.ROW_NUMBER\n" );
		sql.append("  FROM tmp_tt_sales_way_bill TMP,\n" );
		sql.append("       (select count(tt.row_number) V_NUM, tt.VIN\n" );
		sql.append("          from tmp_tt_sales_way_bill tt\n" );
		sql.append("        having count(tt.row_number) > 1\n" );
		sql.append("         group by tt.vin) TM\n" );
		sql.append(" WHERE TMP.VIN = TM.VIN");

		return dao.pageQuery(sql.toString(), null, getFunName());
	}
	/**
	 * 检查替换车架号导入是否重复
	 * @return
	 */
	public List<Map<String, Object>> checkNewVinRepeat() {
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT TMP.ROW_NUMBER\n" );
		sql.append("  FROM tmp_tt_sales_way_bill TMP,\n" );
		sql.append("       (select count(tt.row_number) V_NUM, tt.new_vin\n" );
		sql.append("          from tmp_tt_sales_way_bill tt\n" );
		sql.append("         where tt.new_vin is not null having count(tt.row_number) > 1\n" );
		sql.append("         group by tt.new_vin) TM\n" );
		sql.append(" WHERE TMP.new_vin = TM.new_vin");

		return dao.pageQuery(sql.toString(), null, getFunName());
	}
	/**
	 * 检查订单号
	 * @param flag
	 * @return
	 */
	public List<Map<String, Object>> checkOrderNo(String flag) {
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT TTS.ROW_NUMBER\n" );
		sql.append("  FROM TMP_TT_SALES_WAY_BILL TTS\n" );
		sql.append("  LEFT JOIN TT_VS_DLVRY TVD ON TTS.ORDER_NO =TVD.ORD_NO\n" );
//		if(flag.equals("1")){//是否已发运
//			sql.append("                AND TVD.DLV_STATUS=10211007\n" );
//		}
		sql.append(" WHERE TVD.REQ_ID IS NULL");

		return dao.pageQuery(sql.toString(), null, getFunName());
	}
	/**
	 * 检查组板号和订单号是否对应
	 * @return
	 */
	public List<Map<String, Object>> checkIsMatchOrder() {
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT TTS.ROW_NUMBER\n" );
		sql.append("  FROM TMP_TT_SALES_WAY_BILL TTS\n" );
		sql.append("  LEFT JOIN (SELECT TSB.BO_NO, TSB.BO_ID, TVD.ORD_NO\n" );
		sql.append("               FROM TT_SALES_BOARD     TSB,\n" );
		sql.append("                    TT_SALES_BO_DETAIL TSBD,\n" );
		sql.append("                    TT_VS_DLVRY        TVD\n" );
		sql.append("              WHERE TSB.BO_ID = TSBD.BO_ID\n" );
		sql.append("                AND TSBD.ORDER_ID = TVD.ORD_ID) TTB ON TTS.BO_NO =\n" );
		sql.append("                                                       TTB.BO_NO\n" );
		sql.append("                                                   AND TTS.ORDER_NO =\n" );
		sql.append("                                                       TTB.ORD_NO\n" );
		sql.append(" WHERE TTB.BO_ID IS NULL");

		return dao.pageQuery(sql.toString(), null, getFunName());
	}
	/**
	 * 检查物料代码和订单号是否对应
	 * @return
	 */
	public List<Map<String, Object>> checkIsMatchOrderMat() {
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT TTS.ROW_NUMBER\n" );
		sql.append("  FROM TMP_TT_SALES_WAY_BILL TTS\n" );
		sql.append("  LEFT JOIN (SELECT TVD.REQ_ID, TVD.ORD_NO, MAT.MATERIAL_CODE\n" );
		sql.append("               FROM TT_VS_DLVRY           TVD,\n" );
		sql.append("                    TT_VS_DLVRY_DTL       TVDD,\n" );
		sql.append("                    VW_MATERIAL_GROUP_MAT MAT\n" );
		sql.append("              WHERE TVD.REQ_ID = TVDD.REQ_ID\n" );
		sql.append("                AND TVDD.MATERIAL_ID = MAT.MATERIAL_ID\n" );
		sql.append("                ) TTD ON TTS.ORDER_NO =\n" );
		sql.append("                                                      TTD.ORD_NO\n" );
		sql.append("                                                  AND TTS.MATERIAL_CODE =\n" );
		sql.append("                                                      TTD.MATERIAL_CODE\n" );
		sql.append(" WHERE TTD.REQ_ID IS NULL");
		return dao.pageQuery(sql.toString(), null, getFunName());
	}
	/**
	 * 获取车辆数量不等于组板数量的数据
	 * @param userId
	 * @return
	 */
	public List<Map<String, Object>> checkTmpSubNum(String userId) {
		StringBuffer sql= new StringBuffer();
		sql.append("--查询订单明细组板不等于交接数量\n" );
		sql.append("SELECT TTS.ROW_NUMBER\n" );
		sql.append("  FROM TMP_TT_SALES_WAY_BILL TTS\n" );
		sql.append("  left JOIN (SELECT COUNT(*) AS IM_NUM,\n" );
		sql.append("                    TMP.BO_NO,\n" );
		sql.append("                    TMP.ORDER_NO,\n" );
		sql.append("                    TMP.MATERIAL_CODE\n" );
		sql.append("               FROM TMP_TT_SALES_WAY_BILL TMP\n" );
		sql.append("              WHERE TMP.CREATE_BY = "+userId+"\n" );
		sql.append("                AND TMP.NEW_VIN IS NULL\n" );
		sql.append("              GROUP BY TMP.BO_NO, TMP.ORDER_NO, TMP.MATERIAL_CODE) T1 ON TTS.BO_NO =\n" );
		sql.append("                                                                         T1.BO_NO\n" );
		sql.append("                                                                     AND TTS.MATERIAL_CODE =\n" );
		sql.append("                                                                         T1.MATERIAL_CODE\n" );
		sql.append("                                                                     AND TTS.ORDER_NO =\n" );
		sql.append("                                                                         T1.ORDER_NO\n" );
		sql.append("  left JOIN (SELECT TSBD.BOARD_NUM,\n" );
		sql.append("                    TSBD.ORDER_NO,\n" );
		sql.append("                    TSB.BO_NO,\n" );
		sql.append("                    MAT.MATERIAL_CODE\n" );
		sql.append("               FROM TT_SALES_BO_DETAIL    TSBD,\n" );
		sql.append("                    TT_SALES_BOARD        TSB,\n" );
		sql.append("                    VW_MATERIAL_GROUP_MAT MAT\n" );
		sql.append("              WHERE TSB.BO_ID = TSBD.BO_ID\n" );
		sql.append("                AND TSBD.MAT_ID = MAT.MATERIAL_ID) T2 ON T1.BO_NO =\n" );
		sql.append("                                                         T2.BO_NO\n" );
		sql.append("                                                     AND T1.MATERIAL_CODE =\n" );
		sql.append("                                                         T2.MATERIAL_CODE\n" );
		sql.append("                                                     AND T1.ORDER_NO =\n" );
		sql.append("                                                         T2.ORDER_NO\n" );
		sql.append(" WHERE T1.IM_NUM <> T2.BOARD_NUM\n" );
		sql.append("UNION ALL\n" );
		sql.append("--查询组板总数不等于交接总数\n" );
		sql.append("SELECT TTS.ROW_NUMBER\n" );
		sql.append("  FROM TMP_TT_SALES_WAY_BILL TTS\n" );
		sql.append("  left JOIN (SELECT COUNT(*) AS IM_NUM, TMP.BO_NO\n" );
		sql.append("               FROM TMP_TT_SALES_WAY_BILL TMP\n" );
		sql.append("              WHERE TMP.CREATE_BY = '"+userId+"'\n" );
		sql.append("                AND TMP.NEW_VIN IS NULL\n" );
		sql.append("              GROUP BY TMP.BO_NO) T1 ON TTS.BO_NO = T1.BO_NO\n" );
		sql.append("  left JOIN (SELECT SUM(TSBD.BOARD_NUM) BOARD_NUM, TSB.BO_NO\n" );
		sql.append("               FROM TT_SALES_BO_DETAIL TSBD, TT_SALES_BOARD TSB\n" );
		sql.append("              WHERE TSB.BO_ID = TSBD.BO_ID\n" );
		sql.append("              GROUP BY TSB.BO_NO) T2 ON T1.BO_NO = T2.BO_NO\n" );
		sql.append(" WHERE T1.IM_NUM <> T2.BOARD_NUM");

		return dao.pageQuery(sql.toString(), null, getFunName());
	}
	/**
	 * 获取临时导入列表
	 * @param userId
	 * @param pageSize
	 * @param curPage
	 * @return
	 */
	public PageResult<Map<String, Object>> importQuery(String userId,
			int pageSize, Integer curPage) {
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT TTS.ROW_NUMBER,TTS.BO_NO,\n" );
		sql.append("       TTS.ORDER_NO,\n" );
		sql.append("       TTS.MODEL_NAME,\n" );
		sql.append("       TTS.PAKAGE_NAME,\n" );
		sql.append("       TTS.COLOR_NAME,\n" );
		sql.append("       TTS.MATERIAL_CODE,\n" );
		sql.append("       TTS.VIN,TTS.NEW_VIN\n" );
		sql.append("  FROM TMP_TT_SALES_WAY_BILL TTS\n" );
		sql.append("  WHERE TTS.CREATE_BY="+userId+"\n" );
		sql.append("  ORDER BY TTS.ROW_NUMBER");
		
		return dao.pageQuery(sql.toString(), null, getFunName(), pageSize,
				curPage);
	}
	
	public PageResult<Map<String, Object>> importQuery2(String userId,
			int pageSize, Integer curPage) {
		String sql= getImpSql(userId);		
		return dao.pageQuery(sql.toString(), null, getFunName(), pageSize,curPage);
	}
	
	/**
	 * 查询导入页面的所有数据
	 * @param userId
	 * @return
	 */
	public List<Map<String, Object>> getImportQuery(String userId) {
		String sql= getImpSql(userId);
		return dao.pageQuery(sql, null, getFunName());
	}
	
	/**
	 * 得到导入确认页面的数据
	 * @param userId
	 * @return
	 */
	private String getImpSql(String userId){
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT tts.row_number,\n" );
		sql.append("       tts.bo_no,\n" );
		sql.append("       tts.order_no,\n" );
		sql.append("       tts.model_name,\n" );
		sql.append("       tts.pakage_name,\n" );
		sql.append("       tts.color_name,\n" );
		sql.append("       tts.material_code,\n" );
		sql.append("       tts.vin,\n" );
		sql.append("       tts.new_vin\n" );
		sql.append("  FROM tmp_tt_sales_way_bill tts\n");
		sql.append("  WHERE TTS.CREATE_BY="+userId+"\n" );
		sql.append("  ORDER BY tts.bo_no, tts.order_no, tts.material_code, tts.vin");
		return sql.toString();
	}
	
	/**
	 * 判断组板号是否第一次生成发运交接单
	 * @param userId
	 * @return
	 */
	public List<Map<String, Object>> checkBoNoIsNew(String userId) {
		StringBuffer sql= new StringBuffer();
		sql.append("select b.bo_no,\n" );
		sql.append("       tt.newVin_qty is_new,\n" );
		sql.append("       CASE\n" );
		//sql.append("         WHEN tt.newVin_qty = b.bo_num THEN\n" );
		sql.append("         WHEN tt.newVin_qty > b.bo_num THEN\n" );
		sql.append("          1\n" );
		sql.append("         ELSE\n" );
		sql.append("          0\n" );
		sql.append("       END is_flag,\n" );
		sql.append("       to_char(b.handle_status) handle_status,\n" );
		sql.append("       to_char(b.bo_id) bo_id\n" );
		sql.append("  from tt_sales_board b,\n" );
		sql.append("       (select t.bo_no, SUM(decode(t.new_vin, NULL, 0, 1)) newVin_qty\n" );
		sql.append("          from tmp_tt_sales_way_bill t\n" );
		sql.append("         where t.CREATE_BY = "+userId+"\n" );
		sql.append("         group by t.bo_no) tt\n" );
		sql.append(" where b.bo_no = tt.bo_no\n" );
		sql.append(" ORDER BY 1");

		return dao.pageQuery(sql.toString(), null, getFunName());
	}
	
	/**
	 * 通过组板号校验导入列表物料是否齐全,并且车辆数是否匹配
	 * @param boNo
	 * @return
	 */
	public List<Map<String, Object>> checkMaterialIsExist(String boNo) {
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT tsb.bo_no,\n" );
		sql.append("       tsbd.order_no,\n" );
		sql.append("       tvm.material_code,\n" );
		sql.append("       tvm.material_name,\n" );
		sql.append("       MAX(board_num) board_qty,\n" );
		sql.append("       COUNT(tsw.vin) imp_qty,\n" );
		sql.append("       decode(MAX(board_num),COUNT(tsw.vin),0,1) is_flag,\n" );
		sql.append("       SUM(decode(MAX(board_num),COUNT(tsw.vin),0,1)) over(PARTITION BY tsb.bo_no) res_flag\n" );
		sql.append("  FROM tt_sales_board tsb\n" );
		sql.append("  JOIN tt_sales_bo_detail tsbd ON tsb.bo_id = tsbd.bo_id\n" );
		sql.append("  JOIN tm_vhcl_material tvm ON tsbd.mat_id = tvm.material_id\n" );
		sql.append("  LEFT JOIN tmp_tt_sales_way_bill tsw ON tsb.bo_no = tsw.bo_no\n" );
		sql.append("       AND tsbd.order_no = tsw.order_no AND tvm.material_code = tsw.material_code\n" );
		sql.append(" WHERE tsb.handle_status <> "+Constant.HANDLE_STATUS06+"\n" );
		sql.append("   AND tsb.bo_no = '"+boNo+"'\n" );
		sql.append(" GROUP BY tsb.bo_no, tsbd.order_no, tvm.material_code, tvm.material_name");
		return dao.pageQuery(sql.toString(), null, getFunName());
	}
	
	/**
	 * 通过组板号校验车辆状态是否符合要求
	 * @param boNo
	 * @return
	 */
	public List<Map<String, Object>> checkVehicleIsReq(String boNo) {
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT tsw.vin,\n" );
		sql.append("       lc.code_desc life_cycle,\n" );
		sql.append("       ls.code_desc lock_status,\n" );
		sql.append("       sum(CASE WHEN tv.life_cycle = 10321002 AND tv.lock_status = 10241001 THEN 0 ELSE 1 END) over(PARTITION BY tsw.vin) res_vin_flag\n" );
		sql.append("  FROM tmp_tt_sales_way_bill tsw, tm_vehicle tv, tc_code lc, tc_code ls\n" );
		sql.append(" WHERE tsw.vin = tv.vin(+)\n" );
		sql.append("   AND tv.life_cycle = lc.code_id(+)\n" );
		sql.append("   AND tv.lock_status = ls.code_id(+)");
		sql.append("   AND tsw.bo_no = '"+boNo+"'" );
		return dao.pageQuery(sql.toString(), null, getFunName());
	}
	
	/**
	 * 判断导入数据中重复数据
	 * @return
	 */
	public List<Map<String, Object>> checkRepeatRow() {
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT count(ttS.row_number) as reNum,\n" );
		sql.append("       TTS.BO_NO,\n" );
		sql.append("       TTS.ORDER_NO,\n" );
		sql.append("       TTS.MATERIAL_CODE,\n" );
		sql.append("       TTS.VIN\n" );
		sql.append("  FROM TMP_TT_SALES_WAY_BILL TTS\n" );
		sql.append("having count(TTS.row_number) > 1\n" );
		sql.append(" GROUP BY TTS.BO_NO, TTS.ORDER_NO, TTS.MATERIAL_CODE, TTS.VIN");
		return dao.pageQuery(sql.toString(), null, getFunName());
	}
	/**
	 * 判断交接单是否为二次导入
	 * @param boNo
	 * @param orderNo
	 * @param materialCode
	 * @return
	 */
	public List<Map<String, Object>> getBillInfoByImport(String boNo,String orderNo,String materialCode) {
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT TSW.SEND_STATUS\n" );
		sql.append("  FROM TT_SALES_BOARD        TSB,\n" );
		sql.append("       TT_SALES_BO_DETAIL    TSBD,\n" );
		sql.append("       TT_SALES_WAYBILL      TSW,\n" );
		sql.append("       TT_SALES_WAY_BILL_DTL TSWB,\n" );
		sql.append("       VW_MATERIAL_GROUP_MAT MAT\n" );
		sql.append(" WHERE TSB.BO_ID = TSBD.BO_ID\n" );
		sql.append("   AND TSBD.BILL_ID = TSW.BILL_ID\n" );
		sql.append("   AND TSBD.MAT_ID = MAT.MATERIAL_ID\n" );
		sql.append("   AND TSW.BILL_ID = TSWB.BILL_ID\n" );
		sql.append("   AND TSB.BO_NO = '"+boNo+"'\n" );
		sql.append("   AND TSBD.ORDER_NO = '"+orderNo+"'\n" );
		sql.append("   AND MAT.MATERIAL_CODE = '"+materialCode+"'\n" );
		return dao.pageQuery(sql.toString(), null, getFunName());
	}
	/**
	 * 判断是否已存在相同的交接单
	 * @param boNo
	 * @param orderNo
	 * @param materialCode
	 * @param vin
	 * @return
	 */
	public List<Map<String, Object>> getBillRepeatByImport(String boNo,String orderNo,String materialCode,String vin) {
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT TSW.BILL_ID\n" );
		sql.append("  FROM TT_SALES_BOARD        TSB,\n" );
		sql.append("       TT_SALES_BO_DETAIL    TSBD,\n" );
		sql.append("       TT_SALES_WAYBILL      TSW,\n" );
		sql.append("       TT_SALES_WAY_BILL_DTL TSWB,\n" );
		sql.append("       VW_MATERIAL_GROUP_MAT MAT,\n" );
		sql.append("       TM_VEHICLE            TV\n" );
		sql.append(" WHERE TSB.BO_ID = TSBD.BO_ID\n" );
		sql.append("   AND TSBD.BILL_ID = TSW.BILL_ID\n" );
		sql.append("   AND TSBD.MAT_ID = MAT.MATERIAL_ID\n" );
		sql.append("   AND TSW.BILL_ID = TSWB.BILL_ID\n" );
		sql.append("   AND TV.VEHICLE_ID = TSWB.VEHICLE_ID\n" );
		sql.append("   AND TSB.BO_NO = '"+boNo+"'\n" );
		sql.append("   AND TSBD.ORDER_NO = '"+orderNo+"'\n" );
		sql.append("   AND MAT.MATERIAL_CODE = '"+materialCode+"'\n" );
		sql.append("   AND TSWB.VIN = '"+vin+"'\n");
		return dao.pageQuery(sql.toString(), null, getFunName());
	}
	/**
	 * 根据组板号统计可生成交接单的数量
	 * @param boNo
	 * @return
	 */
	public List<Map<String, Object>> getBillMainNumByBno(String boNo) {
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT TVD.DLV_LOGI_ID,\n" );
		sql.append("       TVD.ORD_PUR_DEALER_ID,\n" );
		sql.append("       TVD.DLV_SHIP_TYPE,\n" );
		sql.append("       TVD.DLV_BAL_PROV_ID,\n" );
		sql.append("       TVD.DLV_BAL_CITY_ID,\n" );
		sql.append("       TVD.DLV_BAL_COUNTY_ID,\n" );
		sql.append("       TVD.REQ_REC_ADDR\n" );
		sql.append("  FROM TT_SALES_BOARD TSB, TT_SALES_BO_DETAIL TSBD, TT_VS_DLVRY TVD\n" );
		sql.append(" WHERE TSB.BO_ID = TSBD.BO_ID\n" );
		sql.append("   AND TSBD.ORDER_ID = TVD.ORD_ID\n" );
		sql.append("   AND TVD.DLV_TYPE = '12131001' --批售订单\n" );
		sql.append("   AND TSB.BO_NO = '"+boNo+"'\n" );
		sql.append(" GROUP BY TVD.DLV_LOGI_ID,\n" );
		sql.append("          TVD.ORD_PUR_DEALER_ID,\n" );
		sql.append("          TVD.DLV_SHIP_TYPE,\n" );
		sql.append("          TVD.DLV_BAL_PROV_ID,\n" );
		sql.append("          TVD.DLV_BAL_CITY_ID,\n" );
		sql.append("          TVD.DLV_BAL_COUNTY_ID,\n" );
		sql.append("          TVD.REQ_REC_ADDR\n" );
		sql.append("UNION ALL\n" );
		sql.append("SELECT TVD.DLV_LOGI_ID,\n" );
		sql.append("       TVD.DLV_REC_WH_ID ORD_PUR_DEALER_ID,\n" );
		sql.append("       TVD.DLV_SHIP_TYPE,\n" );
		sql.append("       TVD.DLV_BAL_PROV_ID,\n" );
		sql.append("       TVD.DLV_BAL_CITY_ID,\n" );
		sql.append("       TVD.DLV_BAL_COUNTY_ID,\n" );
		sql.append("       TVD.REQ_REC_ADDR\n" );
		sql.append("  FROM TT_SALES_BOARD TSB, TT_SALES_BO_DETAIL TSBD, TT_VS_DLVRY TVD\n" );
		sql.append(" WHERE TSB.BO_ID = TSBD.BO_ID\n" );
		sql.append("   AND TSBD.ORDER_ID = TVD.ORD_ID\n" );
		sql.append("   AND TVD.DLV_TYPE = '12131002' --调拨单\n" );
		sql.append("   AND TSB.BO_NO = '"+boNo+"'\n" );
		sql.append(" GROUP BY TVD.DLV_LOGI_ID,\n" );
		sql.append("          TVD.DLV_REC_WH_ID,\n" );
		sql.append("          TVD.DLV_SHIP_TYPE,\n" );
		sql.append("          TVD.DLV_BAL_PROV_ID,\n" );
		sql.append("          TVD.DLV_BAL_CITY_ID,\n" );
		sql.append("          TVD.DLV_BAL_COUNTY_ID,\n" );
		sql.append("          TVD.REQ_REC_ADDR");
		return dao.pageQuery(sql.toString(), null, getFunName());
	}
	/**
	 * 根据组板号、收货经销商、承运商、结算地获取待保存的交接单明细数据
	 * @param boNo
	 * @param logiId
	 * @param dealerId
	 * @param shipType
	 * @param provId
	 * @param cityId
	 * @param countyId
	 * @return
	 */
	public List<Map<String, Object>> getBillDetailImport(String boNo,String logiId,
			String dealerId,String shipType,String provId,String cityId,String countyId) {
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT TV.VEHICLE_ID,\n" );
		sql.append("       TVD.ORD_NO,\n" );
		sql.append("       TTS.VIN,\n" );
		sql.append("       TVDD.MATERIAL_ID,\n" );
		sql.append("       TVDD.ORD_ID,\n" );
		sql.append("       TVDD.ORD_DETAIL_ID,\n" );
		sql.append("       TSBD.BOARD_NUM,TVDD.REQ_DETAIL_ID,TVD.DLV_WH_ID,TSBD.ZZ_WH_ID,\n" );
		sql.append("       TVD.DLV_IS_SD,TSBD.DLV_IS_ZZ,TSBD.DLV_ZZ_PROV_ID,TSBD.DLV_ZZ_CITY_ID,TSBD.DLV_ZZ_COUNTY_ID\n" );
		sql.append("  FROM TT_SALES_BOARD        TSB,\n" );
		sql.append("       TT_SALES_BO_DETAIL    TSBD,\n" );
		sql.append("       TT_VS_DLVRY           TVD,\n" );
		sql.append("       TT_VS_DLVRY_DTL       TVDD,\n" );
		sql.append("       TMP_TT_SALES_WAY_BILL TTS,\n" );
		sql.append("       TM_VEHICLE            TV,\n" );
		sql.append("       VW_MATERIAL_GROUP_MAT MAT\n" );
		sql.append(" WHERE TSB.BO_ID = TSBD.BO_ID\n" );
		sql.append("   AND TVD.REQ_ID = TVDD.REQ_ID\n" );
		sql.append("   AND TSBD.OR_DE_ID = TVDD.ORD_DETAIL_ID\n" );
		sql.append("   AND TSBD.MAT_ID = TVDD.MATERIAL_ID\n" );
		sql.append("   AND TTS.BO_NO = TSB.BO_NO\n" );
		sql.append("   AND TTS.VIN = TV.VIN\n" );
		sql.append("	AND TTS.NEW_VIN IS NULL\n");
		sql.append("   AND TTS.ORDER_NO = TVD.ORD_NO\n" );
		sql.append("   AND TTS.MATERIAL_CODE = MAT.MATERIAL_CODE\n" );
		sql.append("   AND MAT.MATERIAL_ID = TVDD.MATERIAL_ID\n" );
		sql.append("   AND TSB.BO_NO = '"+boNo+"'\n" );
		sql.append("   AND TVD.DLV_LOGI_ID = '"+logiId+"'\n" );
		sql.append("   AND (TVD.ORD_PUR_DEALER_ID = '"+dealerId+"' OR TVD.DLV_REC_WH_ID = '"+dealerId+"')\n" );
		sql.append("   AND TVD.DLV_SHIP_TYPE = '"+shipType+"'\n" );
		sql.append("   AND TVD.DLV_BAL_PROV_ID = '"+provId+"'\n" );
		sql.append("   AND TVD.DLV_BAL_CITY_ID = '"+cityId+"'\n" );
		sql.append("   AND TVD.DLV_BAL_COUNTY_ID = '"+countyId+"'");
		
		return dao.pageQuery(sql.toString(), null, getFunName());
	}
	/**
	 * 更新发运主表的交接数量
	 * @param boNo
	 * @param userId
	 * @return
	 */
	public int updateJjDlvMain(String boNo,String userId){
		StringBuffer sql= new StringBuffer();
		sql.append("UPDATE TT_VS_DLVRY TVD\n" );
		sql.append("   SET TVD.DLV_JJ_TOTAL = TVD.DLV_BD_TOTAL,\n" );
		sql.append("       TVD.UPDATE_BY    = "+userId+",\n" );
		sql.append("       TVD.UPDATE_DATE  = SYSDATE\n" );
		sql.append(" WHERE TVD.ORD_ID IN (SELECT TSBD.ORDER_ID\n" );
		sql.append("                        FROM TT_SALES_BOARD TSB, TT_SALES_BO_DETAIL TSBD\n" );
		sql.append("                       WHERE TSB.BO_ID = TSBD.BO_ID\n" );
		sql.append("                         AND TSB.BO_NO = '"+boNo+"')");
		
		return dao.update(sql.toString(), null);
	}
	/**
	 * 更新发运明细表的交接数量
	 * @param boNo
	 * @param userId
	 * @return
	 */
	public int updateJjDlvDetail(String boNo,String userId){
		StringBuffer sql= new StringBuffer();
		sql.append("UPDATE TT_VS_DLVRY_DTL TVDD\n" );
		sql.append("   SET TVDD.JJ_TOTAL    = TVDD.BD_TOTAL,\n" );
		sql.append("       TVDD.UPDATE_BY   = "+userId+",\n" );
		sql.append("       TVDD.UPDATE_DATE = SYSDATE\n" );
		sql.append(" WHERE TVDD.ORD_DETAIL_ID IN\n" );
		sql.append("       (SELECT TSBD.OR_DE_ID\n" );
		sql.append("          FROM TT_SALES_BOARD TSB, TT_SALES_BO_DETAIL TSBD\n" );
		sql.append("         WHERE TSB.BO_ID = TSBD.BO_ID\n" );
		sql.append("           AND TSB.BO_NO = '"+boNo+"')");

		return dao.update(sql.toString(), null);
	}
	/**
	 * 更新调拨单主表的交接数量
	 * @param boNo
	 * @param userId
	 * @return
	 */
	public int updateJjDispMain(String boNo,String userId){
		StringBuffer sql= new StringBuffer();
		sql.append("UPDATE TT_VS_DISPATCH_ORDER TT\n" );
		sql.append("   SET TT.REP_NUM  = TT.BO_NUM,\n" );
		sql.append("       TT.UPDATE_BY   = "+userId+",\n" );
		sql.append("       TT.UPDATE_DATE = SYSDATE\n" );
		sql.append(" WHERE TT.DISP_ID IN\n" );
		sql.append("       (SELECT TSBD.ORDER_ID\n" );
		sql.append("          FROM TT_SALES_BOARD TSB, TT_SALES_BO_DETAIL TSBD\n" );
		sql.append("         WHERE TSB.BO_ID = TSBD.BO_ID\n" );
		sql.append("           AND TSB.BO_NO = '"+boNo+"')");
		
		return dao.update(sql.toString(), null);
	}
}
