package com.infodms.dms.dao.sales.storage.sendManage;

import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.infodms.dms.common.Constant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.common.materialManager.MaterialGroupManagerDao;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.po.TtSalesAssignPO;
import com.infodms.dms.po.TtVsOrderPO;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

/**
 * 
 * @ClassName     : SendAssignmentDao 
 * @Description   : 发运分派更改管理DAO 
 * @author        : ranjian
 * CreateDate     : 2013-4-18
 */
public class SendAssignmentChangeDao extends BaseDao<PO>{
	private static final SendAssignmentChangeDao dao = new SendAssignmentChangeDao ();
	public static final SendAssignmentChangeDao getInstance() {
		return dao;
	}
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}
	/**
	 * 修改订单表状态
	 * @param seach 订单过滤实体
	 * @param tspo 订单修改实体
	 * @return
	 * @throws Exception
	 */
	public void updateOrderStatus(TtVsOrderPO seach,TtVsOrderPO po)throws Exception{
		dao.update(seach, po);
	}
	/**
	 * 修改分派信息
	 * @param seach 分派过滤实体
	 * @param tspo 分派修改实体
	 * @return
	 * @throws Exception
	 */
	public void updateAssignment(TtSalesAssignPO seach,TtSalesAssignPO po)throws Exception{
		dao.update(seach, po);
	}	
	/**
	 * 运单查询
	 * @param map
	 * @param curPage
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	public PageResult<Map<String, Object>> getSendAssignmentChangeQuery(Map<String, Object> map, int curPage, int pageSize)throws Exception{
		Object[] objArr=getSql(map);
		PageResult<Map<String, Object>> ps= dao.pageQuery(String.valueOf(objArr[0]), (List<Object>)objArr[1], getFunName(),pageSize,curPage);
		return ps;
	}
	/**
	 * 分派更改查询(导出)
	 * @param map
	 * @param curPage
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> getSendAssignmentChangeExport(Map<String, Object> map)throws Exception{
		Object[] objArr=getSql(map);
		List<Map<String, Object>> list= dao.pageQuery(String.valueOf(objArr[0]),(List<Object>)objArr[1], getFunName());
		return list;
	}
	/**
	 * 分派更改查询查询统计
	 *param map 查询参数
	 *@return 
	 */
	public Map<String,Object> tgSum(Map<String, Object> map){
		Object[] obj=getSql(map);
		StringBuffer sbSql=new StringBuffer();
		sbSql.append("SELECT SUM(NVL(CHK_NUM, 0)) ORDER_NUM,\n");
		sbSql.append("       SUM(NVL(DLV_BD_TOTAL, 0)) BOARD_NUM,\n");
		sbSql.append("       SUM(NVL(CHK_NUM, 0) - NVL(DLV_BD_TOTAL, 0)) NOT_BOARD_NUM FROM (\n");
		sbSql.append(obj[0].toString());
		sbSql.append(")\n");
		Map<String, Object> mapR= dao.pageQueryMap(sbSql.toString(), (List<Object>)obj[1], getFunName());
		return mapR;
	}
	public Object[] getSql(Map<String, Object> map){
		/******************************页面查询字段start**************************/
		String yieldly = (String)map.get("yieldly"); // 产地		
		String dealerCode =(String)map.get("dealerCode"); // 经销商CODE
		String dlvOrdType = (String)map.get("dlvOrdType"); //单据来源
		String reqNo = (String)map.get("reqNO"); // 申请单号
		String dlvStartDate = (String)map.get("dlvStartDate"); //最晚发运日期开始
		String dlvEndDate = (String)map.get("dlvEndDate"); // 最晚发运日期结束
		String arrStartDate = (String)map.get("arrStartDate"); //最晚到货日期开始
		String arrEndDate = (String)map.get("arrEndDate"); // 最晚到货日期结束
		String assStartDate =(String)map.get("assStartDate"); // 分配日期开始
		String assEndDate = (String)map.get("assEndDate"); // 分派日期结束
		String logiId = (String)map.get("logiName"); // 物流商
		String isMiddleTurn = (String)map.get("isMiddleTurn"); //是否中转
		String isSdan = (String)map.get("isSdan"); //是否散单
		
		String poseId = (String)map.get("poseId");
		/******************************页面查询字段end***************************/
		List<Object> params = new LinkedList<Object>();
		
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT tvd.req_id,\n" );
		sql.append("       tvd.dlv_is_sd sd_id,\n" );
		sql.append("       st.code_desc sd_name,\n" );
		sql.append("       tvd.dlv_is_zz,\n" );
		sql.append("       (select tcd.code_desc\n" );
		sql.append("          from tc_code tcd\n" );
		sql.append("         where tcd.code_id = tvd.dlv_is_zz) zz_desc,\n" );
		sql.append("       nvl(TVD.DLV_BD_TOTAL, 0) DLV_BD_TOTAL,\n" );
		sql.append("       TVD.DLV_ZZ_PROV_ID,\n" );
		sql.append("       TVD.DLV_ZZ_CITY_ID,\n" );
		sql.append("       TVD.DLV_ZZ_COUNTY_ID,\n" );
		sql.append("       ZZ.PROV_NAME || ZZ.CITY_NAME || ZZ.COUNTY_NAME ZZ_ADDR_NAME,\n" );
		sql.append("       TVD.DLV_BAL_PROV_ID,\n" );
		sql.append("       TVD.DLV_BAL_CITY_ID,\n" );
		sql.append("       TVD.DLV_BAL_COUNTY_ID,\n" );
		sql.append("       JS.PROV_NAME || JS.CITY_NAME || JS.COUNTY_NAME JS_ADDR_NAME,\n" );
		sql.append("       tvd.dlv_logi_id logi_id,\n" );
		sql.append("       TVD.ORD_ID,TVD.ORD_NO,\n" );
		sql.append("       tvd.dlv_type,dt.code_desc dlv_type_name,\n" );
		sql.append("       td.dealer_shortname dealer_name,\n" );
		sql.append("       rw.warehouse_name rec_wh_name,\n" );
		sql.append("       tvd.req_wh_id req_wh_id,\n" );
		sql.append("       fw.warehouse_name req_wh_name,\n" );
		sql.append("       fs.code_desc req_ship,\n" );
		sql.append("       nvl(tvd.req_total, 0) CHK_NUM,\n" );
		sql.append("       rp.region_name || rc.region_name || rt.region_name req_addr,\n" );
		sql.append("       tvd.req_rec_addr,\n" );
		sql.append("       to_char(tvd.req_date, 'yyyy-mm-dd') req_date,\n" );
		sql.append("       to_char(tvd.dlv_date, 'yyyy-mm-dd') dlv_date,\n" );
//		sql.append("DECODE(TVD.REQ_REC_COUNTY_ID,\n" );
//		sql.append("              TVD.DLV_BAL_COUNTY_ID,\n" );
//		sql.append("              tvd.req_no,\n" );
//		sql.append("              DECODE(TVD.REQ_REC_CITY_ID,\n" );
//		sql.append("                     TVD.DLV_BAL_CITY_ID,\n" );
//		sql.append("                     tvd.req_no,\n" );
//		sql.append("                     DECODE(TVD.REQ_REC_PROV_ID,\n" );
//		sql.append("                            TVD.DLV_BAL_PROV_ID,\n" );
//		sql.append("                            tvd.req_no,\n" );
//		sql.append("                            '<font color=\\\"#FF0000\\\" style=\\\"font-weight:bold\\\">' ||\n" );
//		sql.append("                            tvd.req_no || '</font>'))) req_no,\n");
		sql.append("       DECODE(TVD.REQ_REC_COUNTY_ID,\n" );
		sql.append("              TVD.DLV_BAL_COUNTY_ID,\n" );
		sql.append("              tvd.req_no,\n" );
		sql.append("              '<font color=\\\"#FF0000\\\" style=\\\"font-weight:bold\\\">' ||\n" );
		sql.append("              tvd.req_no || '</font>') req_no,\n" );
		sql.append("       TVD.DLV_SHIP_TYPE,\n" );
		sql.append("       (SELECT TC.CODE_DESC\n" );
		sql.append("          FROM TC_CODE TC\n" );
		sql.append("         WHERE TC.CODE_ID = TVD.DLV_SHIP_TYPE) DLV_SHIP, --发运方式\n" );
		sql.append("       TVD.DLV_WH_ID,\n" );
		sql.append("       (SELECT TW.WAREHOUSE_NAME\n" );
		sql.append("          FROM TM_WAREHOUSE TW\n" );
		sql.append("         WHERE TW.WAREHOUSE_ID = TVD.DLV_WH_ID) DLV_WH_NAME --发运仓库\n" );
		sql.append("  FROM tt_vs_dlvry tvd,\n" );
		sql.append("       tm_warehouse fw,\n" );
		sql.append("       tm_warehouse rw,\n" );
		sql.append("       tm_dealer td,\n" );
		sql.append("       tm_region rp,\n" );
		sql.append("       tm_region rc,\n" );
		sql.append("       tm_region rt,\n" );
		sql.append("       tc_code fs,\n" );
		sql.append("       tc_code dt,\n" );
		sql.append("       tc_code st,\n" );
		sql.append("       tm_pose_business_area pb,\n" );
		sql.append("       tc_pose tp,\n" );
		sql.append("       (SELECT RP2.REGION_CODE PROV_CODE,\n" );
		sql.append("               RC2.REGION_CODE CITY_CODE,\n" );
		sql.append("               RT2.REGION_CODE COUNTY_CODE,\n" );
		sql.append("               RP2.REGION_NAME PROV_NAME,\n" );
		sql.append("               RC2.REGION_NAME CITY_NAME,\n" );
		sql.append("               RT2.REGION_NAME COUNTY_NAME\n" );
		sql.append("          FROM tm_region rp2, tm_region rc2, tm_region rt2\n" );
		sql.append("         where 1 = 1\n" );
		sql.append("           AND rp2.region_id = rc2.parent_id\n" );
		sql.append("           AND rc2.region_id = rt2.parent_id\n" );
		sql.append("           AND rp2.region_type = 10541002\n" );
		sql.append("           AND rc2.region_type = 10541003\n" );
		sql.append("           AND rt2.region_type = 10541004) ZZ,\n" );
		sql.append("       (SELECT RP3.REGION_CODE PROV_CODE,\n" );
		sql.append("               RC3.REGION_CODE CITY_CODE,\n" );
		sql.append("               RT3.REGION_CODE COUNTY_CODE,\n" );
		sql.append("               RP3.REGION_NAME PROV_NAME,\n" );
		sql.append("               RC3.REGION_NAME CITY_NAME,\n" );
		sql.append("               RT3.REGION_NAME COUNTY_NAME\n" );
		sql.append("          FROM tm_region rp3, tm_region rc3, tm_region rt3\n" );
		sql.append("         where 1 = 1\n" );
		sql.append("           AND rp3.region_id = rc3.parent_id\n" );
		sql.append("           AND rc3.region_id = rt3.parent_id\n" );
		sql.append("           AND rp3.region_type = 10541002\n" );
		sql.append("           AND rc3.region_type = 10541003\n" );
		sql.append("           AND rt3.region_type = 10541004) JS\n" );
		sql.append(" WHERE tvd.req_rec_dealer_id = td.dealer_id(+)\n" );
		sql.append("   AND tvd.req_rec_wh_id = rw.warehouse_id(+)\n" );
		sql.append("   AND tvd.req_wh_id = fw.warehouse_id\n" );
		sql.append("   AND fw.area_id = pb.area_id\n" );
		sql.append("   AND tp.pose_id = pb.pose_id\n" );
		sql.append("   AND tvd.dlv_type = dt.code_id\n" );
		sql.append("   AND tvd.dlv_is_sd = st.code_id\n" );
		sql.append("   AND tvd.req_rec_prov_id = rp.region_code\n" );
		sql.append("   AND tvd.req_rec_city_id = rc.region_code\n" );
		sql.append("   AND tvd.req_rec_county_id = rt.region_code\n" );
		sql.append("   AND tvd.req_ship_type = fs.code_id\n" );
		sql.append("   AND rp.region_id = rc.parent_id\n" );
		sql.append("   AND rc.region_id = rt.parent_id\n" );
		sql.append("   AND rp.region_type = 10541002\n" );
		sql.append("   AND rc.region_type = 10541003\n" );
		sql.append("   AND rt.region_type = 10541004\n" );
		sql.append("   AND TVD.DLV_ZZ_PROV_ID = ZZ.PROV_CODE(+)\n" );
		sql.append("   AND tvd.DLV_ZZ_CITY_ID = ZZ.CITY_CODE(+)\n" );
		sql.append("   AND tvd.DLV_ZZ_COUNTY_ID = ZZ.COUNTY_CODE(+)\n" );
		sql.append("   AND TVD.DLV_BAL_PROV_ID = JS.PROV_CODE(+)\n" );
		sql.append("   AND TVD.DLV_BAL_CITY_ID = JS.CITY_CODE(+)\n" );
		sql.append("   AND TVD.DLV_BAL_COUNTY_ID = JS.COUNTY_CODE(+)\n");
		sql.append("   AND tvd.dlv_is_sd = "+Constant.IF_TYPE_NO+"\n");//过滤掉是散单的记录
		sql.append("   AND tp.pose_id = ?\n");

		params.add(poseId);
		
		//if(dlvStatus!=null&&!"".equals(dlvStatus)){//发运状态
		//	sql.append("   AND tvd.dlv_status = ?\n");
		//	params.add(dlvStatus);
		//}else{
			sql.append("   AND tvd.dlv_status = "+Constant.ORDER_STATUS_02+"\n");//默认查询分派提交的数据
			//params.add(Constant.ORDER_STATUS_01);//默认查询未分派的数据
		//}
		if (yieldly != null && !"".equals(yieldly))
		{//仓库
			sql.append("   AND TVD.dlv_wh_id =?\n");
			params.add(yieldly);
		}
		if (reqNo != null && !"".equals(reqNo))
		{//申请单号
			sql.append("   AND TVD.req_no like ?\n");
			params.add("%"+reqNo+"%");
		}
		if (dealerCode != null && !"".equals(dealerCode))
		{//经销商code
			sql.append(Utility.getConSqlByParamForEqual(dealerCode, params,"TD", "DEALER_CODE"));
		}
		if(assStartDate!=null&&!"".equals(assStartDate)){//分派日期开始过滤
			sql.append("   AND TVD.dlv_date>=TO_DATE(?,'YYYY-MM-DD HH24:MI:SS')\n" );
			params.add(assStartDate+" 00:00:00");
		}
		if(assEndDate!=null&&!"".equals(assEndDate)){//分派日期结束过滤
			sql.append("  AND TVD.dlv_date<=TO_DATE(?,'YYYY-MM-DD HH24:MI:SS')\n" );
			params.add(assEndDate +" 23:59:59");
		}
		if (dlvOrdType != null && !"".equals(dlvOrdType))//单据来源
		{
			sql.append("   AND TVD.DLV_TYPE=?\n");
			params.add(dlvOrdType);
		}
		if(dlvStartDate!=null&&!"".equals(dlvStartDate)){//最晚发运日期开始过滤
			sql.append("   AND TVD.DLV_FY_DATE>=TO_DATE(?,'YYYY-MM-DD HH24:MI:SS')\n" );
			params.add(dlvStartDate+" 00:00:00");
		}
		if(dlvEndDate!=null&&!"".equals(dlvEndDate)){//最晚发运日期结束过滤
			sql.append("  AND TVD.DLV_FY_DATE<=TO_DATE(?,'YYYY-MM-DD HH24:MI:SS')\n" );
			params.add(dlvEndDate +" 23:59:59");
		}
		if(arrStartDate!=null&&!"".equals(arrStartDate)){//最晚到货日期开始过滤
			sql.append("   AND TVD.DLV_JJ_DATE>=TO_DATE(?,'YYYY-MM-DD HH24:MI:SS')\n" );
			params.add(arrStartDate+" 00:00:00");
		}
		if(arrEndDate!=null&&!"".equals(arrEndDate)){//最晚到货日期结束过滤
			sql.append("  AND TVD.DLV_JJ_DATE<=TO_DATE(?,'YYYY-MM-DD HH24:MI:SS')\n" );
			params.add(arrEndDate +" 23:59:59");
		}
		if (logiId != null && !"".equals(logiId))//发运承运商ID
		{
			sql.append("   AND TVD.Dlv_Logi_Id=?\n");
			params.add(logiId);
		}
		if (isMiddleTurn != null && !"".equals(isMiddleTurn))//是否中转
		{
			sql.append("   AND TVD.DLV_IS_ZZ=?\n");
			params.add(isMiddleTurn);
		}
		if (isSdan != null && !"".equals(isSdan))//是否散单
		{
			sql.append("   AND TVD.DLV_IS_SD=?\n");
			params.add(isSdan);
		}
		sql.append("ORDER BY tvd.req_date, tvd.dlv_date, tvd.req_id"); 
		Object[] obj=new Object[2];
		obj[0]=sql;
		obj[1]=params;
		return obj;
	}
	/**
	 * 发运分派取消查询(sbSql)
	 * @param map
	 */
	public PageResult<Map<String, Object>> getSendCancelQuery(Map<String, Object> map, int curPage, int pageSize){
		Object[] objArr=getSqlCancel(map);
		PageResult<Map<String, Object>> ps= dao.pageQuery(String.valueOf(objArr[0]),(List<Object>)objArr[1], getFunName(),pageSize,curPage);
		return ps;
	}
	/**
	 * 分派取消查询sql
	 * @param map
	 * @return
	 */
	public Object[] getSqlCancel(Map<String, Object> map){
		/****************************** 页面查询字段start **************************/
		String whId = (String) map.get("whId"); // 仓库ID
		String dealerCode = (String) map.get("dealerCode"); // 经销商CODE
		String logiId = (String) map.get("logiId"); // 承运商
		String reqNo = (String) map.get("reqNo"); // 申请单号
		String transType = (String) map.get("transType"); // 申请发运方式
		String startDate = (String) map.get("startDate"); // 申请日期开始
		String endDate = (String) map.get("endDate"); // 申请日期结束
		String fpStartDate = (String) map.get("fpStartDate"); // 分派日期开始
		String fpEndDate = (String) map.get("fpEndDate"); // 分派日期结束
		String dlvType = (String) map.get("dlvType"); // 来源类型
		String dlvIsSd = (String) map.get("dlvIsSd"); // 是否散单
		String poseId = (String) map.get("poseId");
		/****************************** 页面查询字段end ***************************/
		List<Object> params = new LinkedList<Object>();
		
		
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT tvd.req_id,\n" );
		sql.append("       st.code_desc sd_name,\n" );
		sql.append("       tsl.logi_name,\n" );
		sql.append("       tvd.req_no,\n" );
		sql.append("       tvd.ORD_ID,\n" );
		sql.append("       tvd.DLV_TYPE,\n" );
		sql.append("       tvd.ord_no,\n" );
		sql.append("       dt.code_desc dlv_type_name,\n" );
		sql.append("       td.dealer_shortname dealer_name,\n" );
		sql.append("       rw.warehouse_name rec_wh_name,\n" );
		sql.append("       tvd.req_wh_id req_wh_id,\n" );
		sql.append("       fw.warehouse_name req_wh_name,\n" );
		sql.append("       fs.code_desc req_ship,\n" );
		sql.append("       nvl(tvd.req_total, 0) req_total,\n" );
		sql.append("       rp.region_name || rc.region_name || rt.region_name req_addr,\n" );
		sql.append("       tvd.req_rec_addr,\n" );
		sql.append("       tvd.dlv_wh_id,\n" );
		sql.append("       dw.warehouse_name dlv_wh_name,\n" );
		sql.append("       ds.code_desc ds_ship,\n" );
		sql.append("       nvl(tvd.dlv_fp_total, 0) dlv_fp_total,\n" );
		sql.append("       nvl(tvd.req_total, 0) - nvl(tvd.dlv_fp_total, 0) no_fp_total,\n" );
		sql.append("       jp.region_name || jc.region_name || jt.region_name dlv_addr,\n" );
		sql.append("       to_char(tvd.req_date, 'yyyy-mm-dd') req_date,\n" );
		sql.append("       to_char(tvd.dlv_date, 'yyyy-mm-dd') dlv_date,\n" );
		sql.append("       (select TU.NAME FROM TC_USER TU WHERE TU.USER_ID = TVD.AUDIT_BY) AUDIT_BY,\n" );
		sql.append("       to_char(tvd.Audit_Date, 'yyyy-mm-dd') AUDIT_DATE,\n" );
		sql.append("       TVD.AUDIT_REMARK\n" );
		sql.append("  FROM tt_vs_dlvry   tvd,\n" );
		sql.append("       tm_warehouse  fw,\n" );
		sql.append("       tt_sales_logi tsl,\n" );
		sql.append("       tm_warehouse  rw,\n" );
		sql.append("       tm_warehouse  dw,\n" );
		sql.append("       tm_dealer     td,\n" );
		sql.append("       tm_region     rp,\n" );
		sql.append("       tm_region     rc,\n" );
		sql.append("       tm_region     rt,\n" );
		sql.append("       tm_region     jp,\n" );
		sql.append("       tm_region     jc,\n" );
		sql.append("       tm_region     jt,\n" );
		sql.append("       tc_code       fs,\n" );
		sql.append("       tc_code       ds,\n" );
		sql.append("       tc_code       dt,\n" );
		sql.append("       tc_code       st\n" );
		sql.append(" WHERE tvd.dlv_logi_id = tsl.logi_id(+)\n" );
		sql.append("   AND tvd.req_rec_dealer_id = td.dealer_id(+)\n" );
		sql.append("   AND tvd.req_rec_wh_id = rw.warehouse_id(+)\n" );
		sql.append("   AND tvd.dlv_wh_id = dw.warehouse_id\n" );
		sql.append("   AND tvd.req_wh_id = fw.warehouse_id\n" );
		sql.append("   AND tvd.dlv_type = dt.code_id\n" );
		sql.append("   AND tvd.dlv_is_sd = st.code_id\n" );
		sql.append("   AND tvd.req_rec_prov_id = rp.region_code\n" );
		sql.append("   AND tvd.req_rec_city_id = rc.region_code\n" );
		sql.append("   AND tvd.req_rec_county_id = rt.region_code\n" );
		sql.append("   AND tvd.dlv_bal_prov_id = jp.region_code\n" );
		sql.append("   AND tvd.dlv_bal_city_id = jc.region_code\n" );
		sql.append("   AND tvd.dlv_bal_county_id = jt.region_code\n" );
		sql.append("   AND tvd.req_ship_type = fs.code_id\n" );
		sql.append("   AND tvd.dlv_ship_type = ds.code_id\n" );
		sql.append("   AND rp.region_id = rc.parent_id\n" );
		sql.append("   AND rc.region_id = rt.parent_id\n" );
		sql.append("   AND rp.region_type = 10541002\n" );
		sql.append("   AND rc.region_type = 10541003\n" );
		sql.append("   AND rt.region_type = 10541004\n" );
		sql.append("   AND jp.region_id = jc.parent_id\n" );
		sql.append("   AND jc.region_id = jt.parent_id\n" );
		sql.append("   AND jp.region_type = 10541002\n" );
		sql.append("   AND jc.region_type = 10541003\n" );
		sql.append("   AND jt.region_type = 10541004\n" );
		sql.append("   AND tvd.dlv_status = 10211003 --已分派审核\n" );
		sql.append("   AND TVD.ORD_ID NOT IN\n" );
		sql.append("       (SELECT TSBD.ORDER_ID FROM TT_SALES_BO_DETAIL TSBD)");//未生成组板单
		if (whId != null && !"".equals(whId))
		{//发车仓库
			sql.append("   AND fw.warehouse_id =?\n");
			params.add(whId);
		}
		if (dealerCode != null && !"".equals(dealerCode))
		{//经销商code
			sql.append(Utility.getConSqlByParamForEqual(dealerCode, params,"TD", "DEALER_CODE"));
		}
		if (logiId != null && !"".equals(logiId))
		{//承运商
			sql.append("   AND tvd.dlv_logi_id = ?\n");
			params.add(logiId);
		}
		if (reqNo != null && !"".equals(reqNo))
		{//申请单号
			sql.append("   AND tvd.req_NO  LIKE ?\n");
			params.add("%"+reqNo+"%");
		}
		if (transType != null && !"".equals(transType))
		{//申请发运方式
			sql.append("   AND tvd.req_ship_type = ?\n");
			params.add(transType);
		}
		if (dlvType != null && !"".equals(dlvType))
		{//来源类型
			sql.append("   AND tvd.dlv_type = ?\n");
			params.add(dlvType);
		}
		if (dlvIsSd != null && !"".equals(dlvIsSd))
		{//是否散单
			sql.append("   AND tvd.dlv_is_sd = ?\n");
			params.add(dlvIsSd);
		}
		if(startDate!=null&&!"".equals(startDate)){//申请日期开始过滤
			sql.append("   AND tvd.req_DATE >=TO_DATE(?,'YYYY-MM-DD HH24:MI:SS')\n" );
			params.add(startDate+" 00:00:00");
		}
		if(endDate!=null&&!"".equals(endDate)){//申请日期结束过滤
			sql.append("  AND tvd.req_DATE <=TO_DATE(?,'YYYY-MM-DD HH24:MI:SS')\n" );
			params.add(endDate +" 23:59:59");
		}
		if(fpStartDate!=null&&!"".equals(fpStartDate)){//分派日期开始过滤
			sql.append("   AND tvd.DLV_DATE >=TO_DATE(?,'YYYY-MM-DD HH24:MI:SS')\n" );
			params.add(fpStartDate+" 00:00:00");
		}
		if(fpEndDate!=null&&!"".equals(fpEndDate)){//分派日期结束过滤
			sql.append("  AND tvd.DLV_DATE <=TO_DATE(?,'YYYY-MM-DD HH24:MI:SS')\n" );
			params.add(fpEndDate +" 23:59:59");
		}
		sql.append("ORDER BY tvd.req_date, tvd.dlv_date, tvd.req_id"); 

		Object[] obj=new Object[2];
		obj[0]=sql;
		obj[1]=params;
		return obj;
	}
}
