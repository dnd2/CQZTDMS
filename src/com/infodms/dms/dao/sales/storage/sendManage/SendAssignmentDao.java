
package com.infodms.dms.dao.sales.storage.sendManage;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.infodms.dms.common.Constant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.common.materialManager.MaterialGroupManagerDao;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.dao.sales.storage.StorageUtil;
import com.infodms.dms.po.TtSalesAssignPO;
import com.infodms.dms.po.TtSalesBoardPO;
import com.infodms.dms.po.TtVsOrderPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

/**
 * @ClassName : SendAssignmentDao
 * @Description : 发运分派管理DAO
 * @author : ranjian
 *         CreateDate : 2014-4-17
 */
public class SendAssignmentDao extends BaseDao<PO> {
	private static final SendAssignmentDao dao = new SendAssignmentDao();
	public static final SendAssignmentDao getInstance()
	{
		return dao;
	}
	
	@Override
	protected PO wrapperPO(ResultSet rs, int idx)
	{
		return null;
	}
	
	
	/**
	 * 分派管理查询统计
	 *param map 查询参数
	 *@return 
	 */
	public Map<String,Object> tgSum(Map<String, Object> map){
		Object[] obj=getsbSql(map);
		StringBuffer sbSql=new StringBuffer();
		sbSql.append("SELECT   SUM(NVL(CHK_NUM, 0)) CHK_NUM FROM (\n");
		sbSql.append(obj[0].toString());
		sbSql.append(")\n");
		Map<String, Object> mapR= dao.pageQueryMap(sbSql.toString(), (List<Object>)obj[1], getFunName());
		return mapR;
	}
	/**
	 * 分派管理查询(导出)
	 * @param map
	 * @param curPage
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> getSendAssignmentExport(Map<String, Object> map)throws Exception{
		Object[] objArr=getsbSql(map);
		List<Map<String, Object>> list= dao.pageQuery(String.valueOf(objArr[0]),(List<Object>)objArr[1], getFunName());
		return list;
	}
	
	/**
	 * 分派管理查询
	 * 
	 * @param map
	 * @param curPage
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	public PageResult<Map<String, Object>> getSendAssignmentQuery(Map<String, Object> map, int curPage, int pageSize) throws Exception
	{
		Object[] objArr=getsbSql(map);
		PageResult<Map<String, Object>> ps= dao.pageQuery(String.valueOf(objArr[0]), (List<Object>)objArr[1], getFunName(),pageSize,curPage);
		return ps;
	}
	/**
	 * 根据发运仓库和调拨单ID获取可用库存数不足的订单明细
	 * @param warehouseId
	 * @param ordId
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> getStockMinusNum(String warehouseId,String ordId) throws Exception {
		
		StringBuffer sql= new StringBuffer();
//		sql.append("SELECT nvl(SUM(tt.sj_total), 0)-nvl(SUM(tvod.order_amount), 0) minusNum, --可用库存数-本次提报数\n" );
//		sql.append("       tvod.material_id\n" );
//		sql.append("  FROM tt_vs_dispatch_order_dtl tvod\n" );
//		sql.append("  left join (SELECT tv.material_id,\n" );
//		sql.append("                    tv.warehouse_id,\n" );
//		sql.append("                    nvl(COUNT(tv.vehicle_id), 0) sj_total\n" );
//		sql.append("               FROM tm_vehicle tv\n" );
//		sql.append("              WHERE tv.life_cycle = 10321002\n" );
//		sql.append("                AND tv.lock_status = 10241001\n" );
//		sql.append("              GROUP BY tv.material_id, tv.warehouse_id\n" );
//		sql.append("             UNION ALL\n" );
//		sql.append("             SELECT tvdd.material_id,\n" );
//		sql.append("                    tvd.send_ware_id,\n" );
//		sql.append("                    nvl(SUM(tvdd.order_amount), 0) * (-1)\n" );
//		sql.append("               FROM tt_vs_dispatch_order tvd, tt_vs_dispatch_order_dtl tvdd\n" );
//		sql.append("              WHERE tvd.disp_id = tvdd.disp_id\n" );
//		sql.append("                AND tvd.order_status = 12141002\n" );
//		sql.append("              GROUP BY tvdd.material_id, tvd.send_ware_id) tt on tt.material_id =\n" );
//		sql.append("                                                                 tvod.material_id\n" );
//		sql.append("                                                             AND tt.WAREHOUSE_ID = "+warehouseId+"\n" );
//		sql.append(" where tvod.disp_id = "+ordId+"\n" );
//		sql.append("having nvl(SUM(tt.sj_total), 0)-nvl(SUM(tvod.order_amount), 0)<0\n" );
//		sql.append(" group by tvod.material_id");
		sql.append("SELECT nvl(SUM(tt.STOCK_AMOUNT), 0)-nvl(sum(tt.LOCK_AMOUT), 0) - nvl(SUM(tvod.order_amount), 0) minusNum, --可用库存数-本次提报数\n" );
		sql.append("       tvod.material_id\n" );
		sql.append("  FROM tt_vs_dispatch_order_dtl tvod\n" );
		sql.append("  left join VW_VS_RESOURCE_ENTITY_WEEK_NEW tt on tt.material_id =\n" );
		sql.append("                                                 tvod.material_id\n" );
		sql.append("                                             AND tt.WAREHOUSE_ID = "+warehouseId+"\n" );
		sql.append(" where tvod.disp_id = "+ordId+" having\n" );
		sql.append(" nvl(SUM(tt.STOCK_AMOUNT), 0) - nvl(SUM(tvod.order_amount), 0) < 0\n" );
		sql.append(" group by tvod.material_id");
		return dao.pageQuery(sql.toString(), null, getFunName());
	}
	/**
	 * 发运分派查询(SQL)
	 * @param map
	 */
	public Object[] getsbSql(Map<String, Object> map){
		/****************************** 页面查询字段start **************************/
		String yieldly = (String) map.get("yieldly"); // 产地		
		String dealerCode = (String) map.get("dealerCode"); // 经销商CODE
		String reqNo = (String) map.get("reqNO"); // 申请单号
		String startDate = (String) map.get("startDate"); // 申请日期开始
		String endDate = (String) map.get("endDate"); // 申请日期结束
		String poseId = (String) map.get("poseId");
		String dlvOrdType = (String) map.get("dlvOrdType");//单据来源
		String dlvStartDate = (String) map.get("dlvStartDate");//最晚发运开始日期
		String dlvEndDate = (String) map.get("dlvEndDate");//最晚发运结束日期
		String dlvStatus = (String) map.get("dlvStatus");//发运状态
		String arrStartDate = (String) map.get("arrStartDate");//最晚到货开始日期
		String arrEndDate = (String) map.get("arrEndDate");//最晚到货结束日期
		String logiId = (String) map.get("logiId");//承运商ID
		String isMiddleTurn = (String) map.get("isMiddleTurn");//是否中专
		String isSdan = (String) map.get("isSdan");//是否散单
		String jsProvince = (String) map.get("jsProvince");//结算省份
		String jsCity = (String) map.get("jsCity");//结算城市
		String jsCounty = (String) map.get("jsCounty");//结算区县
		
		/****************************** 页面查询字段end ***************************/
		List<Object> params = new LinkedList<Object>();
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT tvd.req_id,tvd.ORD_ID,\n" );
		sql.append("       tvd.dlv_is_sd sd_id,\n" );
		sql.append("       st.code_desc sd_name,\n" );
		sql.append("	    tvd.dlv_is_zz,\n" );
		sql.append("       (select tcd.code_desc from tc_code tcd where tcd.code_id=tvd.dlv_is_zz) zz_desc,\n" );
		//sql.append("        TVD.REQ_TOTAL,\n" );
		sql.append("        TVD.DLV_ZZ_PROV_ID,\n" );
		sql.append("        TVD.DLV_ZZ_CITY_ID,\n" );
		sql.append("        TVD.DLV_ZZ_COUNTY_ID,\n");
		sql.append("		TVD.DLV_BAL_PROV_ID,\n" );
		sql.append("		TVD.DLV_BAL_CITY_ID,\n" );
		sql.append("		TVD.DLV_BAL_COUNTY_ID,\n");
		sql.append("       tvd.dlv_logi_id logi_id,\n" );
		sql.append("       tvd.req_no,\n" );
		sql.append("       tvd.DLV_TYPE,dt.code_desc dlv_type_name,\n" );
		sql.append("       td.dealer_shortname dealer_name,\n" );
		sql.append("       rw.warehouse_name rec_wh_name,\n" );
		sql.append("       tvd.req_wh_id req_wh_id,\n" );
		sql.append("       fw.warehouse_name req_wh_name,\n" );
		sql.append("       fs.code_desc req_ship,\n" );
		sql.append("       nvl(tvd.req_total, 0) CHK_NUM,\n" );
		sql.append("       rp.region_name || rc.region_name || rt.region_name req_addr,\n" );
		sql.append("       tvd.req_rec_addr,\n" );
		sql.append("       to_char(tvd.req_date, 'yyyy-mm-dd') req_date,\n" );
		sql.append("	(select TU.NAME FROM TC_USER TU WHERE TU.USER_ID=TVD.AUDIT_BY) AUDIT_BY,\n" );
		sql.append("       to_char(tvd.Audit_Date, 'yyyy-mm-dd') AUDIT_DATE,\n" );
		sql.append("       TVD.AUDIT_REMARK");

		sql.append("  FROM tt_vs_dlvry           tvd,\n" );
		sql.append("       tm_warehouse          fw,\n" );
		sql.append("       tm_warehouse          rw,\n" );
		sql.append("       tm_dealer             td,\n" );
		sql.append("       tm_region             rp,\n" );
		sql.append("       tm_region             rc,\n" );
		sql.append("       tm_region             rt,\n" );
		sql.append("       tc_code               fs,\n" );
		sql.append("       tc_code               dt,\n" );
		sql.append("       tc_code               st,\n" );
		sql.append("       tm_pose_business_area pb,\n" );
		sql.append("       tc_pose               tp\n" );
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
		sql.append("   AND tp.pose_id = ?\n");

		params.add(poseId);
		
		if(dlvStatus!=null&&!"".equals(dlvStatus)){//发运状态
			sql.append("   AND tvd.dlv_status = ?\n");
			params.add(dlvStatus);
		}else{
			sql.append("   AND (tvd.dlv_status = "+Constant.ORDER_STATUS_01+" or tvd.dlv_status = "+Constant.ORDER_STATUS_14+")\n");//默认查询未分派和分派驳回的数据
			//params.add(Constant.ORDER_STATUS_01);//默认查询未分派的数据
		}
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
		if(startDate!=null&&!"".equals(startDate)){//创建日期开始过滤
			sql.append("   AND TVD.req_date>=TO_DATE(?,'YYYY-MM-DD HH24:MI:SS')\n" );
			params.add(startDate+" 00:00:00");
		}
		if(endDate!=null&&!"".equals(endDate)){//创建日期结束过滤
			sql.append("  AND TVD.req_date<=TO_DATE(?,'YYYY-MM-DD HH24:MI:SS')\n" );
			params.add(endDate +" 23:59:59");
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
		if (jsProvince!= null && !"".equals(jsProvince))//结算省份
		{
			sql.append("   AND TVD.Dlv_Bal_Prov_Id=?\n");
			params.add(jsProvince);
		}
		if (jsCity != null && !"".equals(jsCity))//结算城市
		{
			sql.append("   AND TVD.Dlv_Bal_City_Id=?\n");
			params.add(jsCity);
		}
		if (jsCounty != null && !"".equals(jsCounty))//结算区县
		{
			sql.append("   AND TVD.Dlv_Bal_County_Id=?\n");
			params.add(jsCounty);
		}
		
		sql.append("ORDER BY tvd.req_date, tvd.dlv_date, tvd.req_id"); 
		Object[] obj=new Object[2];
		obj[0]=sql;
		obj[1]=params;
		return obj;
	}
	
	
	/**
	 * 分派查询(导出)
	 * @param map
	 * @param curPage
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> getSendAssExport(Map<String, Object> map)throws Exception{
		Object[] objArr=getSql(map);
		List<Map<String, Object>> list= dao.pageQuery(String.valueOf(objArr[0]),(List<Object>)objArr[1], getFunName());
		return list;
	}
	/**
	 * 发运分派查询(sbSql)
	 * @param map
	 */
	public PageResult<Map<String, Object>> getSendAssQuery(Map<String, Object> map, int curPage, int pageSize){
		Object[] objArr=getSql(map);
		PageResult<Map<String, Object>> ps= dao.pageQuery(String.valueOf(objArr[0]),(List<Object>)objArr[1], getFunName(),pageSize,curPage);
		return ps;
	}
	
	/**
	 * 分派导出
	 * @param map
	 * @return
	 */
	public Object[] getSql(Map<String, Object> map){
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
		sql.append("       tvd.ORD_ID,tvd.DLV_TYPE,tvd.ord_no,\n" );
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
		sql.append("       to_char(tvd.req_date,'yyyy-mm-dd') req_date,\n" );
		sql.append("       to_char(tvd.dlv_date,'yyyy-mm-dd') dlv_date,\n" );
		sql.append("	(select TU.NAME FROM TC_USER TU WHERE TU.USER_ID=TVD.AUDIT_BY) AUDIT_BY,\n" );
		sql.append("       to_char(tvd.Audit_Date, 'yyyy-mm-dd') AUDIT_DATE,\n" );
		sql.append("       TVD.AUDIT_REMARK");

		sql.append("  FROM tt_vs_dlvry   tvd,\n" );
		sql.append("       tm_warehouse          fw,\n" );
		sql.append("       tt_sales_logi         tsl,\n" );
		sql.append("       tm_warehouse          rw,\n" );
		sql.append("       tm_warehouse          dw,\n");
		sql.append("       tm_dealer             td,\n" );
		sql.append("       tm_region             rp,\n" );
		sql.append("       tm_region             rc,\n" );
		sql.append("       tm_region             rt,\n" );
		sql.append("       tm_region             jp,\n" );
		sql.append("       tm_region             jc,\n" );
		sql.append("       tm_region             jt,\n" );
		sql.append("       tc_code               fs,\n" );
		sql.append("       tc_code               ds,\n" );
		sql.append("       tc_code               dt,\n" );
		sql.append("       tc_code               st,\n" );
		sql.append("       tm_pose_business_area pb,\n" );
		sql.append("       tc_pose               tp\n" );
		sql.append(" WHERE tvd.dlv_logi_id = tsl.logi_id(+)\n" );
		sql.append("   AND (tvd.dlv_logi_id = tp.logi_id OR tp.logi_id IS NULL)\n" );
		sql.append("   AND tvd.req_rec_dealer_id = td.dealer_id(+)\n" );
		sql.append("   AND tvd.req_rec_wh_id = rw.warehouse_id(+)\n" );
		sql.append("   AND tvd.dlv_wh_id = dw.warehouse_id\n");
		sql.append("   AND tvd.req_wh_id = fw.warehouse_id\n" );
		sql.append("   AND dw.area_id = pb.area_id\n" );
		sql.append("   AND tp.pose_id = pb.pose_id\n" );
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
		sql.append("   AND rp.region_type = "+Constant.REGION_TYPE_02+"\n" );
		sql.append("   AND rc.region_type = "+Constant.REGION_TYPE_03+"\n" );
		sql.append("   AND rt.region_type = "+Constant.REGION_TYPE_04+"\n" );
		sql.append("   AND jp.region_id = jc.parent_id\n" );
		sql.append("   AND jc.region_id = jt.parent_id\n" );
		sql.append("   AND jp.region_type = "+Constant.REGION_TYPE_02+"\n" );
		sql.append("   AND jc.region_type = "+Constant.REGION_TYPE_03+"\n" );
		sql.append("   AND jt.region_type = "+Constant.REGION_TYPE_04+"\n");
		sql.append("   AND tvd.dlv_status >= "+Constant.ORDER_STATUS_02+"\n");
		sql.append("   AND tp.pose_id = ?\n");
		params.add(poseId);

		//sbSql.append(MaterialGroupManagerDao.getPoseIdBusinessSqlByPar("T.AREA_ID", poseId,params));//车厂端查询列表产地数据权限
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
	
	/**
	 * 发运分派添加
	 * 
	 * @param tspo
	 *            发运分派实体
	 * @return
	 * @throws Exception
	 */
	public void addSendAssignment(TtSalesAssignPO tspo) throws Exception
	{
		
		dao.insert(tspo);
		
	}
	
	/**
	 * 修改订单表状态
	 * 
	 * @param seach
	 *            订单过滤实体
	 * @param tspo
	 *            订单修改实体
	 * @return
	 * @throws Exception
	 */
	public void updateOrderStatus(TtVsOrderPO seach, TtVsOrderPO po) throws Exception
	{
		
		dao.update(seach, po);
		
	}
	
	/**
	 * 根据订单ID获取发运分派信息
	 * 
	 * @param map
	 * @param curPage
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> getSendAssignmentByOrderID(List<Object> params)
	{
		StringBuffer sbSql = new StringBuffer();
		sbSql.append("select TVO.ORDER_ID,TD.DEALER_CODE,\n");//--经销商CODE
		sbSql.append("TD.DEALER_NAME,\n");//--经销商名称
		sbSql.append("TR.REGION_NAME PROVINCE_NAME,\n");//--省NAME
		sbSql.append("TR2.REGION_NAME CITY_NAME,\n");//--市NAME
		sbSql.append("TR.REGION_NAME||TR2.REGION_NAME PC_NAME,\n");//--省市
		sbSql.append("TVO.ORDER_NO,\n");//--销售单号
		sbSql.append("TVO.INVOICE_NO,\n");//--发票号
		sbSql.append("TO_CHAR(TVO.RAISE_DATE,'YYYY-MM-DD') RAISE_DATE,\n");//--提报日期
		sbSql.append("TO_CHAR(TVO.PLAN_CHK_DATE,'YYYY-MM-DD') PLAN_CHK_DATE,\n");//--计划处审核时间
		sbSql.append("TO_CHAR(TVO.FIN_CHK_DATE,'YYYY-MM-DD') FIN_CHK_DATE,\n");//--财务审核时间
		sbSql.append("TVO.ORDER_TYPE,\n");//--订单类型
		sbSql.append("TVO.CHK_NUM,\n");//--订单数量
		sbSql.append("TVO.DELIV_ADD_ID,\n");//-- 发运地址ID（物流商）
		sbSql.append("TSL.LOGI_ID\n");//物流商ID
		sbSql.append("from TT_VS_ORDER TVO,TM_REGION TR,TM_REGION TR2,\n");//--订单表
		sbSql.append("TM_DEALER TD,\n");//--代理商表
		sbSql.append("TT_SALES_LOGI TSL,\n");//
		sbSql.append("TT_SALES_LOGI_AREA TSLA,\n");//
		sbSql.append("TT_SALES_CITY_DIS TSCD\n");//
		sbSql.append("WHERE TVO.REC_DEALER_ID=TD.DEALER_ID\n");//
		sbSql.append("AND TD.PROVINCE_ID=TR.REGION_CODE\n");//
		sbSql.append("AND TD.CITY_ID=TR2.REGION_CODE\n");//
		sbSql.append(" AND TR2.REGION_ID=TSCD.CITY_ID\n");//
		sbSql.append(" AND TSL.LOGI_ID = TSLA.LOGI_ID\n");//
		sbSql.append(" AND TSLA.DIS_ID = TSCD.DIS_ID");//
		sbSql.append(" AND TVO.ORDER_ID=?");
		Map<String, Object> map = dao.pageQueryMap(sbSql.toString(), params, getFunName());
		return map;
	}
	/**
	 * 根据申请ID获取发运信息
	 * @param reqId
	 * @return
	 */
	public Map<String, Object> getDlvInfoById(String reqId)
	{
		StringBuffer sql= new StringBuffer();
		sql.append("select TVD.DLV_IS_ZZ,\n" );
		sql.append("       (SELECT TC.CODE_DESC FROM Tc_Code TC WHERE TC.CODE_ID = TVD.DLV_IS_ZZ) IS_ZZ_DESC,\n" );
		sql.append("       TVD.ZZ_WH_ID,TVD.DLV_ZZ_PROV_ID,\n" );
		sql.append("       TVD.DLV_ZZ_CITY_ID,\n" );
		sql.append("       TVD.DLV_ZZ_COUNTY_ID,\n" );
		sql.append("       TVD.REQ_WH_ID,TVD.REQ_REC_WH_ID,\n" );
		sql.append("       TVD.DLV_BAL_PROV_ID,\n" );
		sql.append("       TVD.DLV_BAL_CITY_ID,\n" );
		sql.append("       TVD.DLV_BAL_COUNTY_ID,TVD.req_ship_type SHIP_DESC\n" );
		//sql.append("	   (SELECT TC.CODE_DESC FROM Tc_Code TC WHERE TC.CODE_ID = TVD.req_ship_type) SHIP_DESC\n");
		sql.append("  from tt_vs_dlvry tvd\n" );
		sql.append(" where TVD.REQ_ID = "+reqId+"\n");

		Map<String, Object> map = dao.pageQueryMap(sql.toString(), null, getFunName());
		return map;
	}
	/**
	 * 发运分派添加(自动添加自提单)
	 * 
	 * @param orderId
	 *            订单ID
	 * @param userId
	 *            用户ID
	 * @return
	 * @throws Exception
	 */
	public void autoAddSend(Long orderId, Long userId) throws Exception
	{
		
		List<Object> params = new ArrayList<Object>();
		params.add(orderId);//订单ID
		StringBuffer sbSql = new StringBuffer();
		sbSql.append("select TVO.ORDER_ID, --订单ID\n");
		sbSql.append("       TVO.CHK_NUM --订单数量\n");
		sbSql.append("  from TT_VS_ORDER TVO --订单表\n");
		sbSql.append(" where TVO.ORDER_ID = ?");
		Map<String, Object> map = dao.pageQueryMap(sbSql.toString(), params, getFunName());
		if (!map.isEmpty())
		{
			TtVsOrderPO order = new TtVsOrderPO();
			order.setOrderId(orderId);
			order = (TtVsOrderPO) dao.select(order).get(0);
			//TODO 根据订单ID发运经销商查出物流商ID
			
			String assId = SequenceManager.getSequence("");
			//添加数据到分派表
			TtSalesAssignPO tspo = new TtSalesAssignPO();
			tspo.setAssId(Long.parseLong(assId));//分派ID
			tspo.setLogiId(2013060519373217L);
			tspo.setOrderId(orderId);//提车单ID
			tspo.setOrderNum(Integer.parseInt(map.get("CHK_NUM").toString()));//订单数量
			tspo.setBoardNum(Integer.parseInt(map.get("CHK_NUM").toString()));//组板数量
			tspo.setAssPer(userId);//分派人
			tspo.setAssDate(new Date());//分派日期
			tspo.setCreateBy(userId);
			tspo.setCreateDate(new Date());
			tspo.setAssRemark(Constant.SEND_REMARK);//备注(自动分派的单备注信息)
			tspo.setBoStatus(Constant.BO_STATUS03);//完全组板状态 默认
			tspo.setSendType(Long.parseLong(Constant.TRANSPORT_TYPE_01.toString()));//发运方式：自取
			tspo.setAreaId(order.getAreaId());//产地
			
			
			/****** 将订单表信息冗余到发运分配表 beg *****/
//			tspo.setDealerId(order.getDealerId());
//			tspo.setAddressId(order.getDelivAddId());
//			tspo.setRecDealerId(order.getRecDealerId());
			tspo.setOrderType(Long.valueOf(order.getOrderType()));
			tspo.setOrderNo(order.getOrderNo());
			tspo.setAccType(order.getFundTypeId());
//			tspo.setInvoiceNo(order.getInvoiceNo());
//			tspo.setFinChkDate(order.getFinChkDate());
//			tspo.setPlanChkDate(order.getPlanChkDate());
			tspo.setRaiseDate(order.getRaiseDate());
			tspo.setLinkMan(order.getLinkMan());
			tspo.setTel(order.getTel());
//			tspo.setIsRetail(CommonUtils.checkNull(order.getIsRetail()));//是否零售
			/****** 将订单表信息冗余到发运分配表 end *****/
			dao.insert(tspo);//添加分派信息
			
			String boId = SequenceManager.getSequence("");
			String boNo = CommonUtils.getBusNo(Constant.NOCRT_BOARD_MY_NO,order.getAreaId());
			TtSalesBoardPO tsbPO = new TtSalesBoardPO();//组板表实体
			tsbPO.setBoId(Long.parseLong(boId));//组板ID
//			tsbPO.setAssId(tspo.getAssId());//分派ID
//			tsbPO.setBillId(orderId);//运单ID
			tsbPO.setBoNo(boNo);//组板编号(生成规则：ZB+日期+序列号)
			tsbPO.setBoPer(userId);//组板人
			tsbPO.setBoDate(new Date());//组板时间
			tsbPO.setCreateBy(userId);//创建人
			tsbPO.setBoNum(tspo.getBoardNum());
			tsbPO.setCreateDate(new Date());//创建时间
			tsbPO.setAreaId(order.getAreaId());//产地
			tsbPO.setIsEnable(Constant.IF_TYPE_YES);//是否有效（有效）
			tsbPO.setHandleStatus(Constant.HANDLE_STATUS01);//默认未配车
//			tsbPO.setHaveRetail(CommonUtils.checkNull(order.getIsRetail()));//是否零售
			dao.insert(tsbPO);//添加组板信息
			
			// TODO 保存组板明细
			
			//修改订单表状态位已分派
			TtVsOrderPO tvopo = new TtVsOrderPO();
			tvopo.setOrderStatus(Constant.ORDER_STATUS_11);//修改订单状态(已分派)
			TtVsOrderPO seach = new TtVsOrderPO();
			seach.setOrderId(orderId);//提车单ID
			dao.update(seach, tvopo);//修改订单状态		
			
			sbSql.delete(0, sbSql.length());
			sbSql.append("INSERT INTO TT_SALES_BO_DETAIL ( \n"); 
			sbSql.append("   BO_DE_ID, BO_ID, OR_DE_ID, \n"); 
			sbSql.append("   MAT_ID, COLOR_CODE, INVOICE_NUM, \n"); 
			sbSql.append("   BOARD_NUM, ALLOCA_NUM, OUT_NUM, \n"); 
			sbSql.append("   SEND_NUM, ACC_NUM, CAR_NO, \n"); 
			sbSql.append("   LOADS, IS_ENABLE, CAR_TEAM, \n"); 
			sbSql.append("   CREATE_BY, CREATE_DATE, UPDATE_BY, \n"); 
			sbSql.append("   UPDATE_DATE, LOGI_ID, DEALER_ID, \n"); 
			sbSql.append("   REC_DEALER_ID, ADDRESS_ID, ORDER_TYPE, \n"); 
			sbSql.append("   ORDER_NO, ACC_TYPE, AREA_ID, \n"); 
			sbSql.append("   \n"); 
			sbSql.append("   INVOICE_NO, FIN_CHK_DATE, PLAN_CHK_DATE, \n"); 
			sbSql.append("   RAISE_DATE, SEND_TYPE, LINK_MAN, \n"); 
			sbSql.append("   TEL, ORDER_ID, ASS_DATE, \n"); 
			sbSql.append("   ASS_PER, ASS_REMARK, BILL_ID, \n"); 
			sbSql.append("   ORDER_NUM, INVOICE_NO_VER) \n"); 
			sbSql.append(" SELECT F_GETID(),"+boId+",B.DETAIL_ID,\n"); 
			sbSql.append("   B.MATERIAL_ID,C.COLOR_NAME,B.CHECK_AMOUNT,\n"); 
			sbSql.append("   B.CHECK_AMOUNT,B.MATCH_AMOUNT,B.OUT_AMOUNT,\n"); 
			sbSql.append("   B.DELIVERY_AMOUNT,B.ACC_AMOUNT,'',\n"); 
			sbSql.append("   '','"+Constant.IF_TYPE_YES+"','',\n"); 
			sbSql.append("    "+userId+",SYSDATE,"+userId+",\n"); 
			sbSql.append("   SYSDATE,-1,A.DEALER_ID,\n"); 
			sbSql.append("   A.REC_DEALER_ID,A.DELIV_ADD_ID,A.ORDER_TYPE,\n"); 
			sbSql.append("   A.ORDER_NO,A.FUND_TYPE_ID,A.AREA_ID,\n"); 
			sbSql.append("   \n"); 
			sbSql.append("   A.INVOICE_NO,A. FIN_CHK_DATE, A.PLAN_CHK_DATE, \n"); 
			sbSql.append("   A.RAISE_DATE, A.DELIVERY_TYPE, A.LINK_MAN,\n"); 
			sbSql.append("   A.TEL, A.ORDER_ID, SYSDATE,\n"); 
			sbSql.append("   "+userId+", '', NULL,\n"); 
			sbSql.append("   A.CHK_NUM, A.INVOICE_NO_VER \n"); 
			sbSql.append(" FROM TT_VS_ORDER A,TT_VS_ORDER_DETAIL B,TM_VHCL_MATERIAL C\n"); 
			sbSql.append(" WHERE A.ORDER_ID=B.ORDER_ID\n"); 
			sbSql.append(" AND B.MATERIAL_ID=C.MATERIAL_ID\n"); 
			sbSql.append(" AND A.ORDER_ID="+orderId+"\n");
			dao.insert(sbSql.toString());
			StorageUtil.getInstance().updateBoardOrAllocaNum(tsbPO.getBoId(),userId);
		}
	}
	/**
	 * 发运计划自动审核为【自动】的时侯调用,自动派单
	 * 
	 * @param orderId
	 *            订单ID
	 * @param userId
	 *            用户ID
	 * @return
	 * @throws Exception
	 */
	public void autoAssignment(Long orderId, Long userId) throws Exception
	{
		List<Object> params = new ArrayList<Object>();
		params.add(orderId);//订单ID
		StringBuffer sbSql = new StringBuffer();
		sbSql.append("select A.ORDER_ID, A.AREA_ID, A.CHK_NUM, B.PROVINCE_ID\n");
		sbSql.append("  from tt_vs_order A, TM_VS_ADDRESS B\n");
		sbSql.append(" WHERE A.DELIV_ADD_ID = B.ID\n");
		sbSql.append("   AND A.ORDER_ID = ?"); 
		Map<String, Object> map = dao.pageQueryMap(sbSql.toString(), params, getFunName());
		sbSql.delete(0, sbSql.length());
		sbSql.append("SELECT A.LOGI_ID, C.PROVINCE_ID\n");
		sbSql.append("  FROM TT_SALES_LOGI      A,\n");
		sbSql.append("       TT_SALES_LOGI_AREA B,\n");
		sbSql.append("       TT_SALES_CITY_DIS  C,\n");
		sbSql.append("       TM_REGION          D\n");
		sbSql.append(" WHERE A.LOGI_ID = B.LOGI_ID\n");
		sbSql.append("   AND B.DIS_ID = C.DIS_ID\n");
		sbSql.append("   AND C.PROVINCE_ID = D.REGION_ID\n");
		sbSql.append("   AND A.YIELDLY = ?\n");
		sbSql.append("   AND D.REGION_CODE = ?\n");
		sbSql.append(" GROUP BY C.PROVINCE_ID, A.LOGI_ID"); 
		List<Object> params1 = new ArrayList<Object>();
		params1.add(map.get("AREA_ID").toString());//产地
		params1.add(map.get("PROVINCE_ID").toString());//省份
		List<Map<String, Object>> list=dao.pageQuery(sbSql.toString(), params1, getFunName());
		if (!map.isEmpty())
		{
			if(list!=null && list.size()>0){
				TtVsOrderPO order = new TtVsOrderPO();
				order.setOrderId(orderId);
				order = (TtVsOrderPO) dao.select(order).get(0);
				//TODO 根据订单ID发运经销商查出物流商ID
				String assId = SequenceManager.getSequence("");
				//添加数据到分派表
				TtSalesAssignPO tspo = new TtSalesAssignPO();
				tspo.setAssId(Long.parseLong(assId));//分派ID
				tspo.setLogiId(Long.parseLong(((Map<String, Object>)list.get(0)).get("LOGI_ID").toString()));
				tspo.setOrderId(orderId);//提车单ID
//				tspo.setOrderNum(order.getChkNum());//订单数量
				tspo.setAssPer(userId);//分派人
				tspo.setAssDate(new Date());//分派日期
				tspo.setCreateBy(userId);
				tspo.setCreateDate(new Date());
				tspo.setAssRemark("发运计划自动审核生成派单");//备注
				tspo.setBoStatus(Constant.BO_STATUS01);//未组板状态
				tspo.setSendType(Long.valueOf(order.getDeliveryType()));//发运方式
				tspo.setAreaId(order.getAreaId());//产地
				/****** 将订单表信息冗余到发运分配表 beg *****/
//				tspo.setDealerId(order.getDealerId());
//				tspo.setAddressId(order.getDelivAddId());
//				tspo.setRecDealerId(order.getRecDealerId());
				tspo.setOrderType(Long.valueOf(order.getOrderType()));
				tspo.setOrderNo(order.getOrderNo());
				tspo.setAccType(order.getFundTypeId());
//				tspo.setInvoiceNo(order.getInvoiceNo());
//				tspo.setFinChkDate(order.getFinChkDate());
//				tspo.setPlanChkDate(order.getPlanChkDate());
				tspo.setRaiseDate(order.getRaiseDate());
				tspo.setLinkMan(order.getLinkMan());
				tspo.setTel(order.getTel());
//				tspo.setIsRetail(CommonUtils.checkNull(order.getIsRetail()));//是否零售
				/****** 将订单表信息冗余到发运分配表 end *****/
				dao.insert(tspo);//添加分派信息
			}else{//无发找到该省份下的物流商
				throw new RuntimeException("该发运省市还没设定物流商，解决方案有2种:1,储运基础信息>>物流商管理，添加该省市到指定物流商;2,储运基础信息>>发运计划自动审核参数,设置该产地下为手动！");
			}
		}
	}
	
	/**
	 * 发运分派详细信息
	 * @param orderId
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> getSendQueryById(String orderId)throws Exception{
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT T.ORDER_NO,\n");
		sql.append("       T.ASS_REMARK,\n");
		sql.append("       T.ORDER_NUM,\n");
		sql.append("       T.BOARD_NUM,\n");
		sql.append("       TO_CHAR(T.ASS_DATE, 'YYYY-MM-DD hh:mm:ss') ASS_DATE,TO_CHAR(T.RAISE_DATE, 'YYYY-MM-DD hh:mm:ss') RAISE_DATE,\n");
		sql.append("       T.BOARD_NUM,T.INVOICE_NO,T.INVOICE_NO_VER,\n");
		sql.append("       T.BOARD_NUM,T.LINK_MAN,T.TEL,\n");
		sql.append("       U.NAME,\n");
		sql.append("C3.CODE_DESC ORDER_STATUS,\n");
		sql.append("      C2.CODE_DESC SEND_STATUS,\n");
		sql.append("      C1.CODE_DESC BO_SA,"); 
		sql.append("       T.OUT_NUM,\n");
		sql.append("       T.ALLOCA_NUM,TD.DEALER_NAME,\n");
		sql.append("       T.CREATE_BY,TSL.LOGI_NAME,T.ORDER_TYPE,T.BO_STATUS,T.SEND_TYPE,\n");
		sql.append("       T.CREATE_DATE,\n");
		sql.append("       T.UPDATE_BY,\n");
		sql.append("       T.UPDATE_DATE,\n");
		sql.append("       TD.ADDRESS DEALER_ADDRESS,\n");
		sql.append("       TVA.ADDRESS ADDRESS,\n");
		sql.append("       T.AS_REMARK,\n");
		sql.append("       TO_CHAR(T.PLAN_DATE, 'YYYY-MM-DD') PLAN_DATE,\n");
		sql.append("       TO_CHAR(T.PLAN_CHK_DATE, 'YYYY-MM-DD') PLAN_CHK_DATE,\n");
		sql.append("       T.SEND_NUM,\n");
		sql.append("       T.ACC_NUM,\n");
		sql.append("       T.ORDER_NO,\n");
		sql.append("       T.INVOICE_NO,\n");
		sql.append("       TR.REGION_NAME||TR2.REGION_NAME||TR3.REGION_NAME PCA_NAME,\n");
		sql.append("       TO_CHAR(T.FIN_CHK_DATE, 'YYYY-MM-DD') FIN_CHK_DATE\n");
		sql.append("  FROM TT_SALES_ASSIGN  T,\n");
		sql.append("       TT_SALES_LOGI    TSL,\n");
		sql.append("       TM_VS_ADDRESS    TVA,\n");
		sql.append("       TM_DEALER        TD,\n");
		sql.append("       TM_REGION        TR,\n");
		sql.append("       TM_BUSINESS_AREA TBA,\n");
		sql.append("       TM_REGION        TR2,\n");
		sql.append("       TM_REGION        TR3,\n");
		sql.append("	   TC_CODE          C1,\n");
		sql.append("       TC_CODE          C2,\n");
		sql.append("       TC_CODE          C3,"); 
		sql.append("       TC_USER          U\n");
		sql.append(" WHERE T.LOGI_ID = TSL.LOGI_ID\n");
		sql.append("   AND T.ADDRESS_ID = TVA.ID(+)\n");
		sql.append("   AND T.DEALER_ID = TD.DEALER_ID\n");
		sql.append("   AND T.AREA_ID = TBA.AREA_ID\n");
		sql.append("   AND TVA.PROVINCE_ID = TR.REGION_CODE\n");
		sql.append("   AND TVA.CITY_ID = TR2.REGION_CODE\n");
		sql.append("   AND TVA.AREA_ID = TR3.REGION_CODE\n");
		sql.append("AND T.BO_STATUS = C1.CODE_ID\n");
		sql.append("   AND T.SEND_TYPE = C2.CODE_ID\n");
		sql.append("   AND T.ORDER_TYPE = C3.CODE_ID\n"); 
		sql.append("   AND T.ASS_PER = U.USER_ID\n");  

		sql.append("   AND T.ORDER_ID = "+orderId+"\n");
		sql.append("ORDER BY T.IS_RETAIL DESC"); 
		Map<String, Object> map= dao.pageQueryMap(sql.toString(), null, getFunName());
		return map;	
	}
	
}

