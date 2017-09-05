package com.infodms.dms.dao.sales.storage.sendManage;

import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.infodms.dms.common.Constant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.common.materialManager.MaterialGroupManagerDao;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.po.TtSalesBoDetailPO;
import com.infodms.dms.po.TtSalesBoardPO;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

/**
 * 
 * @ClassName     : SendAssignmentDao 
 * @Description   : 发运组板管理DAO 
 * @author        : ranjian
 * CreateDate     : 2013-4-19
 */
public class SendBoardDao extends BaseDao<PO>{
	private static final SendBoardDao dao = new SendBoardDao ();
	public static final SendBoardDao getInstance() {
		return dao;
	}
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}
	/**
	 * 组板信息查询列表
	 * @param map
	 * @param curPage
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	public PageResult<Map<String, Object>> getSendBoardQuery(Map<String, Object> map, int curPage, int pageSize)throws Exception{
		Object[] obj=getSQL(map);
		PageResult<Map<String, Object>> ps= dao.pageQuery(obj[0].toString(), (List<Object>)obj[1], getFunName(),pageSize,curPage);
		return ps;
	}
	
	/**
	 * 组板信息查询导出
	 * @param map
	 * @return 组板信息列表
	 * @throws Exception
	 */
	public List<Map<String, Object>> getSendBoardQueryExport(Map<String, Object> map)throws Exception{
		Object[] obj=getSQL(map);
		List<Map<String, Object>> list= dao.pageQuery(obj[0].toString(), (List<Object>)obj[1], getFunName());
		return list;
	}
	/**
	 * 求组板数量和
	 *param map 查询参数
	 *@return 组板订单，组板，未组板总数
	 */
	public Map<String,Object> tgSum(Map<String, Object> map){
		Object[] obj=getSQL(map);
		StringBuffer sbSql=new StringBuffer();
		sbSql.append("SELECT SUM(NVL(BOARD_NUM,0)) BOARD_NUM,SUM(NVL(ORDER_NUM,0)) ORDER_NUM,SUM(NVL(INNAGE_NUM,0)) INNAGE_NUM FROM (\n");
		sbSql.append(obj[0].toString());
		sbSql.append(")\n");
		Map<String, Object> mapR= dao.pageQueryMap(sbSql.toString(), (List<Object>)obj[1], getFunName());
		return mapR;
	}
	/**
	 * 根据订单明细ID统计组板数量
	 * @param orDeIds
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> getCountBoNumByBTid(String orDeIds)throws Exception{
		List<Object> params = new LinkedList<Object>();
		StringBuffer sql= new StringBuffer();
		sql.append("select NVL(sum(tt.board_num),0) AS BO_NUM, TT.OR_DE_ID\n" );
		sql.append("  from tt_sales_bo_detail tt\n" );
		sql.append(" where 1=1\n" );
		sql.append(Utility.getConSqlByParamForEqual(orDeIds, params,"tt", "or_de_id"));
		sql.append(" GROUP BY TT.OR_DE_ID");
		List<Map<String, Object>> list= dao.pageQuery(sql.toString(), params, getFunName());
		return list;
	}
	/**
	 * 根据订单ID统计发运分派明细表的分派数量
	 * @param ordId
	 * @return
	 */
	public Map<String, Object> getDlvDtlCount(String ordId)
	{
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT NVL(SUM(TT.BD_TOTAL), 0) AS BD_TOTAL\n" );
		sql.append("           FROM TT_VS_DLVRY_DTL TT\n" );
		sql.append("          WHERE TT.ORD_ID ="+ordId+"\n");
		Map<String, Object> map = dao.pageQueryMap(sql.toString(), null, getFunName());
		return map;
	}
	/**
	 * 根据职位ID获取是否属于物流商以及物流商ID
	 * @param poseId
	 * @return
	 */
	public Map<String, Object> getPoseLogiById(String poseId)
	{
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT TP.POSE_BUS_TYPE,TP.LOGI_ID\n" );
		sql.append("  FROM TC_POSE TP, TT_SALES_LOGI TS\n" );
		sql.append(" WHERE TP.LOGI_ID=TS.LOGI_ID(+) AND TP.POSE_ID="+poseId+"\n");
		Map<String, Object> map = dao.pageQueryMap(sql.toString(), null, getFunName());
		return map;
	}
	//get sql
	public Object[] getSQL(Map<String, Object> map){
		/******************************页面查询字段start**************************/
		String yieldly = (String)map.get("yieldly"); //发运仓库		
		String dlvStartDate =(String)map.get("dlvStartDate"); //最晚发运日期开始
		String dlvEndDate = (String)map.get("dlvEndDate"); //最晚发运日期结束
		String transType = (String)map.get("transType"); //发运方式
		String arrStartDate = (String)map.get("arrStartDate"); //最晚到货日期开始
		String arrEndDate = (String)map.get("arrEndDate"); //最晚到货日期结束
		String logiId =(String)map.get("logiName"); //承运商ID
		String startDate = (String)map.get("startDate"); //提交日期开始
		String endDate = (String)map.get("endDate"); // 提交日期结束
		String status = (String)map.get("status"); //状态
		String isMiddleTurn = (String)map.get("isMiddleTurn"); //是否中转
		String isSdan = (String)map.get("isSdan"); //是否散单
		String provinceId = (String)map.get("provinceId");//结算省份
		String cityId = (String)map.get("cityId");//结算城市
		String countyId = (String)map.get("countyId");//结算区县
		
		String poseId = (String)map.get("poseId");//职位ID
		String posBusType=(String)map.get("posBusType");
		String logiIdU=String.valueOf(map.get("logiId"));//当前职位所对应承运商ID
		//String orgCode = (String)map.get("orgCode");// 大区
		
		/******************************页面查询字段end***************************/
		List<Object> params = new LinkedList<Object>();
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT TSA.REQ_ID,\n" );
		sql.append("       TD.DEALER_CODE, --经销商CODE\n" );
		sql.append("       TSA.ORD_ID ORDER_ID, --订单ID\n" );
		sql.append("       DECODE(TD.dealer_shortname,NULL,SH.WAREHOUSE_NAME,TD.dealer_shortname) DEALER_NAME, --经销商NAME\n" );
		sql.append("       TSA.DLV_BAL_PROV_ID, --发运结算省份\n" );
		sql.append("       TSA.DLV_BAL_CITY_ID, --发运结算城市\n" );
		sql.append("       TSA.DLV_BAL_COUNTY_ID, --发运结算地区\n" );
		sql.append("       TR.REGION_NAME||TR2.REGION_NAME||TR3.REGION_NAME AS JS_ADDR,--结算省市县\n" );
		sql.append("       TSA.ORD_NO ORDER_NO, --订单号\n" );
		sql.append("       TSA.Dlv_Remark AS_REMARK, --说明\n" );
		sql.append("       TSL.LOGI_ID, --物流商ID\n" );
		sql.append("       TSL.LOGI_NAME, --物流商名称-\n" );
		sql.append("       TO_CHAR(TSA.DLV_JJ_DATE, 'YYYY-MM-DD') DLV_JJ_DATE, --最晚交货日期\n" );
		sql.append("       TO_CHAR(TSA.Dlv_Date, 'YYYY-MM-DD HH24:MI:SS') ASS_DATE, --分派时间\n" );
		sql.append("       TSA.DLV_SHIP_TYPE,\n" );
		sql.append("       (SELECT TC.CODE_DESC\n" );
		sql.append("          FROM TC_CODE TC\n" );
		sql.append("         WHERE TC.CODE_ID = TSA.DLV_SHIP_TYPE) DLV_SHIP_NAME, --发运方式\n" );
		sql.append("       TSA.DLV_TYPE, --发运类型\n" );
		sql.append("       NVL(TSA.Dlv_Bd_Total, 0) BOARD_NUM, --组板数量\n" );
		sql.append("       NVL(TSA.REQ_TOTAL, 0) ORDER_NUM, --订单数量\n" );
		sql.append("       NVL(TSA.REQ_TOTAL, 0) - NVL(TSA.Dlv_Bd_Total, 0) INNAGE_NUM, --剩余数量\n" );
		sql.append("       FW.WAREHOUSE_NAME, --发运仓库\n" );
		sql.append("       TSA.DLV_STATUS,\n" );
		sql.append("       (SELECT TC.CODE_DESC\n" );
		sql.append("          FROM TC_CODE TC\n" );
		sql.append("         WHERE TC.CODE_ID = TSA.Dlv_Status) DLV_STATUS_NAME, --发运状态\n" );
		sql.append("        TSA.DLV_IS_ZZ,--是否中转\n" );
		sql.append("        TSA.DLV_IS_SD--是否散单\n" );
		sql.append("  FROM TT_VS_DLVRY   TSA,\n" );
		sql.append("       TT_SALES_LOGI TSL,\n" );
		sql.append("       TM_DEALER TD,\n" );
		sql.append("       TM_REGION     TR,\n" );
		sql.append("       TM_REGION     TR2,\n" );
		sql.append("       TM_REGION     TR3,\n" );
		if(!posBusType.equals(String.valueOf(Constant.POSE_BUS_TYPE_WL))){//非物流商
			sql.append("   tm_pose_business_area pb,\n" );
			sql.append("       tc_pose               tp,\n" );
			
		}
		sql.append("       tm_warehouse          fw,TM_WAREHOUSE          SH\n" );
		sql.append(" WHERE 1 = 1\n" );
		sql.append("   AND TSA.req_rec_dealer_id = TD.DEALER_ID(+)\n" );
		sql.append("   AND TSA.DLV_LOGI_ID = TSL.LOGI_ID(+)\n" );
		sql.append("   AND TSA.DLV_REC_WH_ID = SH.WAREHOUSE_ID(+)\n");
		sql.append("   AND TSA.DLV_BAL_PROV_ID = TR.region_code\n" );
		sql.append("   AND TSA.DLV_BAL_CITY_ID = TR2.region_code\n" );
		sql.append("   AND TSA.DLV_BAL_county_ID = TR3.region_code\n" );
		sql.append("   AND TR.region_id = TR2.parent_id\n" );
		sql.append("   AND TR2.region_id = TR3.parent_id\n" );
		sql.append("   AND TR.region_type = 10541002\n" );
		sql.append("   AND TR2.region_type = 10541003\n" );
		sql.append("   AND TR3.region_type = 10541004\n" );
		sql.append("   AND (TSA.Dlv_Status = 10211003 OR TSA.Dlv_Status = 10211004) --已分派审核或者部分组板\n" );
		sql.append("   AND TSA.DLV_WH_ID = fw.warehouse_id\n" );
		if(!posBusType.equals(String.valueOf(Constant.POSE_BUS_TYPE_WL))){//非物流商
			sql.append("   AND fw.area_id = pb.area_id\n" );
			sql.append("   AND tp.pose_id = pb.pose_id\n" );
			sql.append("   AND tp.pose_id = ?\n" );
			params.add(poseId);
		}else{
			sql.append("   AND TSA.DLV_LOGI_ID= ?\n" );
			params.add(logiIdU);
		}
		
		if(yieldly!=null&&!"".equals(yieldly)){//发运仓库
			sql.append("AND TSA.DLV_WH_ID=?\n");
			params.add(yieldly);
		}
		if(dlvStartDate!=null&&!"".equals(dlvStartDate)){//最晚发运日期开始过滤
			sql.append("   AND TSA.DLV_FY_DATE>=TO_DATE(?,'YYYY-MM-DD HH24:MI:SS')\n" );
			params.add(dlvStartDate+" 00:00:00");
		}
		if(dlvEndDate!=null&&!"".equals(dlvEndDate)){//最晚发运日期结束过滤
			sql.append("  AND TSA.DLV_FY_DATE<=TO_DATE(?,'YYYY-MM-DD HH24:MI:SS')\n" );
			params.add(dlvEndDate +" 23:59:59");
		}
		if(transType!=null&&!"".equals(transType)){//发运方式
			sql.append("   AND TSA.DLV_SHIP_TYPE = ?\n" );
			params.add(transType);
		}
		if(arrStartDate!=null&&!"".equals(arrStartDate)){//最晚到货日期开始过滤
			sql.append("   AND TSA.DLV_JJ_DATE>=TO_DATE(?,'YYYY-MM-DD HH24:MI:SS')\n" );
			params.add(arrStartDate+" 00:00:00");
		}
		if(arrEndDate!=null&&!"".equals(arrEndDate)){//最晚到货日期结束过滤
			sql.append("  AND TSA.DLV_JJ_DATE<=TO_DATE(?,'YYYY-MM-DD HH24:MI:SS')\n" );
			params.add(arrEndDate +" 23:59:59");
		}
		if (logiId != null && !"".equals(logiId))//发运承运商ID
		{
			sql.append("   AND TSA.Dlv_Logi_Id=?\n");
			params.add(logiId);
		}
		if(startDate!=null&&!"".equals(startDate)){//提交日期开始过滤
			sql.append("   AND TSA.Dlv_Date>=TO_DATE(?,'YYYY-MM-DD HH24:MI:SS')\n" );
			params.add(startDate+" 00:00:00");
		}
		if(endDate!=null&&!"".equals(endDate)){//提交日期结束过滤
			sql.append("  AND TSA.Dlv_Date<=TO_DATE(?,'YYYY-MM-DD HH24:MI:SS')\n" );
			params.add(endDate +" 23:59:59");
		}
		if (isMiddleTurn != null && !"".equals(isMiddleTurn))//是否中转
		{
			sql.append("   AND TSA.DLV_IS_ZZ=?\n");
			params.add(isMiddleTurn);
		}
		if (isSdan != null && !"".equals(isSdan))//是否散单
		{
			sql.append("   AND TSA.DLV_IS_SD=?\n");
			params.add(isSdan);
		}
		if(provinceId!=null&&!"".equals(provinceId)){
			sql.append(" AND TSA.Dlv_Bal_Prov_Id =?\n");		
			params.add(provinceId);
		}
		if(cityId!=null&&!"".equals(cityId)){
			sql.append(" AND TSA.Dlv_Bal_City_Id=?\n");
			params.add(cityId);
		}
		if(countyId!=null&&!"".equals(countyId)){
			sql.append(" AND TSA.Dlv_Bal_County_Id=?\n");
			params.add(countyId);
		}
		if(status!=null&&!"".equals(status)){
			sql.append(" AND TSA.DLV_STATUS=?\n");
			params.add(status);
		}
		sql.append(" ORDER BY TSA.DLV_DATE DESC");
		Object[] arr=new Object[2];
		arr[0]=sql;
		arr[1]=params;
		return arr;
	}
	/**
	 * 组板物料列表查询
	 * @param params 查询参数LIST
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> getSendBoardMatListQuery(String orderIds)throws Exception{
		List<Object> params = new LinkedList<Object>();
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT VMGM.SERIES_NAME, --车系名称\n" );
		sql.append("       TSA.REQ_ID,\n" );
		sql.append("       TSA.ORD_ID ORDER_ID,\n" );
		sql.append("       TSA.DLV_LOGI_ID LOGI_ID,\n" );
		sql.append("       TW.AREA_ID, --发运产地ID\n" );
		sql.append("       TSA.DLV_WH_ID, --发运仓库ID\n" );
		sql.append("       TSA.ORD_NO ORDER_NO, --销售订单号\n" );
		sql.append("       LOGI.LOGI_NAME, --物流商名称\n" );
		sql.append("       TD.DEALER_CODE,\n" );
		sql.append("       DECODE(TD.DEALER_SHORTNAME,NULL,SH.WAREHOUSE_NAME,TD.DEALER_SHORTNAME) DEALER_NAME,\n" );
		sql.append("       VMGM.MATERIAL_ID, --物料ID\n" );
		sql.append("       VMGM.MATERIAL_CODE, --物料CODE\n" );
		sql.append("       VMGM.MATERIAL_NAME, --物料NAME\n" );
		sql.append("       VMGM.COLOR_NAME, --颜色\n" );
		sql.append("       VMGM.PACKAGE_NAME, --配置\n" );
		sql.append("       VMGM.MODEL_NAME, --车型\n" );
		sql.append("       TVOD.ORD_DETAIL_ID DETAIL_ID, --订单明细ID\n" );
		sql.append("       NVL(TVOD.ORD_TOTAL, 0) CHECK_AMOUNT, --开票数量\n" );
		sql.append("       NVL(TVOD.BD_TOTAL, 0) BOARD_NUM --已组板数量\n" );
		sql.append("  FROM TT_VS_DLVRY           TSA,\n" );
		sql.append("       TT_VS_DLVRY_DTL       TVOD,\n" );
		sql.append("       TM_DEALER             TD,\n" );
		sql.append("       VW_MATERIAL_GROUP_MAT VMGM,\n" );
		sql.append("       TT_SALES_LOGI         LOGI,\n" );
		sql.append("       TM_WAREHOUSE          TW,\n" );
		sql.append("       TM_WAREHOUSE          SH\n" );
		sql.append(" WHERE TVOD.MATERIAL_ID = VMGM.MATERIAL_ID\n" );
		sql.append("   AND TSA.DLV_WH_ID = TW.WAREHOUSE_ID\n" );
		sql.append("   AND TSA.REQ_REC_DEALER_ID = TD.DEALER_ID(+)\n" );
		sql.append("   AND TSA.REQ_REC_WH_ID=SH.WAREHOUSE_ID(+)\n" );
		sql.append("   AND TSA.DLV_LOGI_ID = LOGI.LOGI_ID(+)\n" );
		sql.append("   AND TSA.REQ_ID = TVOD.REQ_ID\n");
		sql.append(Utility.getConSqlByParamForEqual(orderIds, params,"TSA", "ORD_ID"));
		sql.append("   AND NVL(TVOD.BD_TOTAL, 0)< NVL(TVOD.ORD_TOTAL, 0)\n");//过滤掉组板数量小于订单数量的记录
		sql.append(" ORDER BY TSA.ORD_ID ASC");
		List<Map<String, Object>> list= dao.pageQuery(sql.toString(), params, getFunName());
		return list;
	}
	/**
	 * 根据结算地、发运方式、承运商获取未组板的散单
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> checkHasSD(Map<String,Object> map)throws Exception{
		List<Object> params = new LinkedList<Object>();
		String jsProvince = (String)map.get("jsProvince");
		String jsCity =(String)map.get("jsCity");
		String jsCounty = (String)map.get("jsCounty");
		String shipType = (String)map.get("shipType"); 
		String logiId = (String)map.get("logiId"); 
		String ordIds = (String)map.get("ordIds");
		StringBuffer sql= new StringBuffer();
		sql.append("select TVD.REQ_ID\n" );
		sql.append("  from TT_VS_DLVRY tvd\n" );
		sql.append(" WHERE TVD.DLV_BAL_PROV_ID = "+jsProvince+"\n" );
		sql.append("   AND TVD.DLV_BAL_CITY_ID = "+jsCity+"\n" );
		sql.append("   AND TVD.DLV_BAL_COUNTY_ID = "+jsCounty+"\n" );
		sql.append("   AND TVD.DLV_SHIP_TYPE = "+shipType+"\n" );
		sql.append("   AND TVD.DLV_LOGI_ID = "+logiId+"\n" );
		sql.append("   AND (TVD.Dlv_Status = 10211003 OR TVD.Dlv_Status = 10211004) --已分派审核或者部分组板\n" );
		sql.append("   AND TVD.DLV_IS_SD=10041001\n" );
		sql.append("   AND TVD.ORD_ID NOT IN ("+ordIds+")");
		List<Map<String, Object>> list= dao.pageQuery(sql.toString(), params, getFunName());
		return list;
	}
	/**
	 * 添加组板表信息
	 * @param po 组板表实体
	 * @return
	 * @throws Exception
	 */
	public void addSendBoard(TtSalesBoardPO po) throws Exception{
		dao.insert(po);
	}
	/**
	 * 添加组板明细表信息
	 * @param po 组板表实体
	 * @return
	 * @throws Exception
	 */
	public void addSendBoardDel(TtSalesBoDetailPO po) throws Exception{
		dao.insert(po);
	}
	
	/**
	 * 修改物流商
	 * @author liufazhong
	 */
	public int updateLogiIdByOrderId(String orderId,String logiId) {
		String sql = "UPDATE TT_SALES_ASSIGN SET LOGI_ID = "+logiId+" WHERE ORDER_ID = "+orderId;
		return dao.update(sql, null);
	}
	/**
	 * 发运组板审核列表查询
	 * @param map
	 * @param curPage
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	public PageResult<Map<String, Object>> sendBoardAuditQuery(Map<String, Object> map, int curPage, int pageSize)throws Exception{
		List<Object> params = new LinkedList<Object>();
		String boardNo=(String)map.get("boardNo");
		String transType=(String)map.get("transType");
		String logiName=(String)map.get("logiName");
		String startDate=(String)map.get("startDate");
		String endDate=(String)map.get("endDate");
		String provinceId = (String)map.get("provinceId");
		String cityId =(String)map.get("cityId");
		String countyId = (String)map.get("countyId");
		//String poseId = (String)map.get("poseId"); 
		StringBuffer sql= new StringBuffer();
		sql.append("select TSB.BO_ID,\n" );
		sql.append("       TSB.BO_NO,\n" );
		sql.append("       --TSB.DLV_TYPE,\n" );
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
		sql.append("       NVL(TSB.BO_NUM, 0) BO_NUM\n" );
		sql.append("  from tt_sales_board     tsb,\n" );
		sql.append("       TT_SALES_LOGI      TSL,\n" );
		sql.append("       TM_REGION          TR1,\n" );
		sql.append("       TM_REGION          TR2,\n" );
		sql.append("       TM_REGION          TR3\n" );
		sql.append(" WHERE 1=1\n" );
		sql.append("   AND TSB.DLV_LOGI_ID = TSL.LOGI_ID(+)\n" );
		sql.append("   AND TR1.REGION_ID = TR2.PARENT_ID\n" );
		sql.append("   AND TR2.REGION_ID = TR3.PARENT_ID\n" );
		sql.append("   AND TR1.REGION_CODE = TSB.DLV_BAL_PROV_ID\n" );
		sql.append("   AND TR2.REGION_CODE = TSB.DLV_BAL_CITY_ID\n" );
		sql.append("   AND TR3.REGION_CODE = TSB.DLV_BAL_COUNTY_ID\n" );
		sql.append("   AND TSB.bo_status = '0'\n");
		if(boardNo!=null&&!"".equals(boardNo)){
			sql.append("AND TSB.BO_NO LIKE ?\n");		
			params.add("%"+boardNo+"%");
		}
		if(transType!=null&&!"".equals(transType)){
			sql.append("AND TSB.DLV_SHIP_TYPE= ?\n");		
			params.add(transType);
		}
		if(logiName!=null&&!"".equals(logiName)){
			sql.append("AND TSB.DLV_LOGI_ID =?\n");		
			params.add(logiName);
		}
		if(startDate!=null&&!"".equals(startDate)){//提交日期开始过滤
			sql.append("   AND TSB.BO_DATE>=TO_DATE(?,'YYYY-MM-DD HH24:MI:SS')\n" );
			params.add(startDate+" 00:00:00");
		}
		if(endDate!=null&&!"".equals(endDate)){//提交日期结束过滤
			sql.append("  AND TSB.BO_DATE<=TO_DATE(?,'YYYY-MM-DD HH24:MI:SS')\n" );
			params.add(endDate +" 23:59:59");
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
		PageResult<Map<String, Object>> ps= dao.pageQuery(sql.toString(), params, getFunName(),pageSize,curPage);
		return ps;
	}
	/**
	 * 更新发运主表的发运状态为组板确认
	 * @param boId
	 * @param userId
	 * @return
	 */
	public int updateDvlStatus(String boId,String userId) {
		StringBuffer sql= new StringBuffer();
		sql.append("update TT_VS_DLVRY TVD\n" );
		sql.append("        SET TVD.DLV_STATUS  = "+Constant.ORDER_STATUS_06+",\n" );
		sql.append("            TVD.UPDATE_BY   ="+userId+",\n" );
		sql.append("            TVD.UPDATE_DATE = SYSDATE\n" );
		sql.append("WHERE TVD.REQ_ID IN\n" );
		sql.append("           (SELECT TVDD.REQ_ID\n" );
		sql.append("              FROM TT_SALES_BO_DETAIL TSD,TT_VS_DLVRY_DTL TVDD\n" );
		sql.append("               where TSD.OR_DE_ID=TVDD.ORD_DETAIL_ID\n" );
		sql.append("               AND TSD.MAT_ID=TVDD.MATERIAL_ID\n" );
		sql.append("               AND TSD.BO_ID = "+boId+")");

		return dao.update(sql.toString(), null);
	}
	/**
	 * 根据订单ID统计组板数量
	 * @param boId
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> getBDNumByOrdId(String boId)throws Exception{
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT NVL(SUM(TSD.BOARD_NUM),0) BO_NUM, TSD.ORDER_ID\n" );
		sql.append("  FROM TT_SALES_BO_DETAIL TSD\n" );
		sql.append(" WHERE TSD.BO_ID in ("+boId+")\n" );
		sql.append(" GROUP BY TSD.ORDER_ID");
		List<Map<String, Object>> list= dao.pageQuery(sql.toString(), null, getFunName());
		return list;
	}
	/**
	 * 根据组板ID获取明细信息
	 * @param boId
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> getBODetailByBid(String boId)throws Exception{
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT TSB.OR_DE_ID, TSB.MAT_ID, NVL(TSB.BOARD_NUM, 0) BOARD_NUM\n" );
		sql.append("  FROM TT_SALES_BO_DETAIL TSB\n" );
		sql.append(" WHERE TSB.BO_ID = "+boId+"\n");
		List<Map<String, Object>> list= dao.pageQuery(sql.toString(), null, getFunName());
		return list;
	}
	/**
	 * 根据组板ID获取发运类型
	 * @param boId
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> getDlvTypeByBid(String boId)throws Exception{
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT TVD.DLV_TYPE,TSBD.*\n" );
		sql.append("  FROM TT_SALES_BO_DETAIL TSBD, TT_VS_DLVRY TVD\n" );
		sql.append(" WHERE TSBD.ORDER_ID = TVD.ORD_ID\n" );
		sql.append("   AND TSBD.BO_ID = "+boId+"\n");
		List<Map<String, Object>> list= dao.pageQuery(sql.toString(), null, getFunName());
		return list;
	}
	/**
	 * 根据组板ID获取订单ID和发运状态
	 * @param boId
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> getDlvOrderByBoid(String boId)throws Exception{
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT TVD.ORD_ID, TVD.DLV_STATUS\n" );
		sql.append("  FROM TT_VS_DLVRY TVD\n" );
		sql.append(" WHERE TVD.ORD_ID IN (SELECT TSBD.ORDER_ID\n" );
		sql.append("                        FROM TT_SALES_BO_DETAIL TSBD\n" );
		sql.append("                       WHERE TSBD.BO_ID = '"+boId+"')");
		List<Map<String, Object>> list= dao.pageQuery(sql.toString(), null, getFunName());
		return list;
	}
	/**
	 * 根据订单ID获取组板号的数量
	 * @param orderId
	 * @return
	 * @throws Exception
	 */
	public Map<String,Object> getBoCountByoId(String orderId)throws Exception{
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT COUNT(DISTINCT TSBD.BO_ID) COUNT_BO\n" );
		sql.append("  FROM TT_SALES_BO_DETAIL TSBD\n" );
		sql.append(" WHERE TSBD.ORDER_ID = '"+orderId+"'");

		Map<String, Object> mapR= dao.pageQueryMap(sql.toString(), null, getFunName());
		return mapR;
	}
	/**
	 * 根据订单ID和组板ID汇总组板数量
	 * @param orderId
	 * @param boId
	 * @return
	 * @throws Exception
	 */
	public Map<String,Object> getBoSumCountByOrdId(String orderId,String boId)throws Exception{
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT NVL(SUM(TSBD.BOARD_NUM),0) BO_SUM\n" );
		sql.append("  FROM TT_SALES_BO_DETAIL TSBD\n" );
		sql.append(" WHERE TSBD.ORDER_ID = '"+orderId+"'\n" );
		sql.append("   AND TSBD.BO_ID = '"+boId+"'");
		
		Map<String, Object> mapR= dao.pageQueryMap(sql.toString(), null, getFunName());
		return mapR;
	}
	/**
	 * 根据组板ID和订单明细ID汇总本次组板数量
	 * @param orDetId
	 * @param boId
	 * @return
	 * @throws Exception
	 */
	public Map<String,Object> getBoSumCountDtlByOrdId(String orDetId,String boId)throws Exception{
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT NVL(SUM(TSBD.BOARD_NUM),0) BO_SUM_D\n" );
		sql.append("  FROM TT_SALES_BO_DETAIL TSBD\n" );
		sql.append(" WHERE TSBD.Or_De_Id = '"+orDetId+"'\n" );
		sql.append("   AND TSBD.BO_ID = '"+boId+"'");

		Map<String, Object> mapR= dao.pageQueryMap(sql.toString(), null, getFunName());
		return mapR;
	}
	/**
	 * 根据发运ID统计组板数量
	 * @param reqId
	 * @return
	 * @throws Exception
	 */
	public Map<String,Object> getDlvBoByRid(String reqId)throws Exception{
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT NVL(SUM(TVD.BD_TOTAL), 0) BD_TOTAL, TVD.REQ_ID\n" );
		sql.append("  FROM TT_VS_DLVRY_DTL TVD\n" );
		sql.append(" WHERE TVD.REQ_ID = "+reqId+"\n" );
		sql.append(" GROUP BY TVD.REQ_ID");

		Map<String, Object> mapR= dao.pageQueryMap(sql.toString(), null, getFunName());
		return mapR;
	}
	/**
	 * 获取组板申请单中的最大最晚发运日期和最晚到货日期
	 * @param reqIds
	 * @return
	 * @throws Exception
	 */
	public Map<String,Object> getMaxDlvDateByRid(String reqIds)throws Exception{
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT to_char(MAX(TVD.DLV_FY_DATE), 'yyyy-mm-dd') DLV_FY_DATE,\n" );
		sql.append("       to_char(MAX(TVD.DLV_JJ_DATE), 'yyyy-mm-dd') DLV_JJ_DATE\n" );
		sql.append("  FROM TT_VS_DLVRY TVD\n" );
		sql.append(" WHERE TVD.REQ_ID IN\n" );
		sql.append("       ("+reqIds+")");

		Map<String, Object> mapR= dao.pageQueryMap(sql.toString(), null, getFunName());
		return mapR;
	}
	/**
	 * 根据组板ID获取发运主表数量
	 * @param boId
	 * @return
	 * @throws Exception
	 */
	public Map<String,Object> getDlvInfoByBid(String boId)throws Exception{
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT NVL(TVD.ORD_TOTAL,0) ORD_TOTAL, NVL(TVD.DLV_BD_TOTAL,0) DLV_BD_TOTAL\n" );
		sql.append("  FROM TT_VS_DLVRY TVD\n" );
		sql.append(" WHERE TVD.REQ_ID IN (SELECT TVDD.REQ_ID\n" );
		sql.append("                        FROM TT_SALES_BO_DETAIL TSB, TT_VS_DLVRY_DTL TVDD\n" );
		sql.append("                       WHERE TSB.OR_DE_ID = TVDD.ORD_DETAIL_ID\n" );
		sql.append("                         AND TSB.MAT_ID = TVDD.MATERIAL_ID\n" );
		sql.append("                         AND TSB.BO_ID = "+boId+")\n");
		
		Map<String, Object> mapR= dao.pageQueryMap(sql.toString(), null, getFunName());
		return mapR;
	}
	/**
	 * 获取已经组板的数据
	 * @param reqId
	 * @return
	 * @throws Exception
	 */
	public Map<String,Object> getBoCountAlready(String reqId)throws Exception{
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT COUNT(TSB.BO_ID) AS BO_COUNT\n" );
		sql.append("  FROM TT_SALES_BOARD TSB, TT_SALES_BO_DETAIL TSBD, TT_VS_DLVRY_DTL TVD\n" );
		sql.append(" WHERE TSBD.OR_DE_ID = TVD.ORD_DETAIL_ID\n" );
		sql.append("   AND TSBD.MAT_ID = TVD.MATERIAL_ID\n" );
		sql.append("   AND TVD.REQ_ID = "+reqId+"\n" );
		sql.append("   AND TSBD.BO_ID = TSB.BO_ID\n" );
		sql.append("   AND TSB.BO_STATUS = '1'");

		Map<String, Object> mapR= dao.pageQueryMap(sql.toString(), null, getFunName());
		return mapR;
	}
	/**
	 * 组板取消查询
	 * @param map
	 * @param curPage
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	public PageResult<Map<String, Object>> sendBoardCancelQuery(Map<String, Object> map, int curPage, int pageSize)throws Exception{
		List<Object> params = new LinkedList<Object>();
		String boardNo=(String)map.get("boardNo");
		String transType=(String)map.get("transType");
		String logiName=(String)map.get("logiName");
		String startDate=(String)map.get("startDate");
		String endDate=(String)map.get("endDate");
		String provinceId = (String)map.get("provinceId");
		String cityId =(String)map.get("cityId");
		String countyId = (String)map.get("countyId");
		//String poseId = (String)map.get("poseId"); 
		StringBuffer sql= new StringBuffer();
		sql.append("select TSB.BO_ID,\n" );
		sql.append("       TSB.BO_NO,\n" );
		sql.append("       --TSB.DLV_TYPE,\n" );
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
		sql.append("       NVL(TSB.BO_NUM, 0) BO_NUM\n" );
		sql.append("  from tt_sales_board     tsb,\n" );
		sql.append("       TT_SALES_LOGI      TSL,\n" );
		sql.append("       TM_REGION          TR1,\n" );
		sql.append("       TM_REGION          TR2,\n" );
		sql.append("       TM_REGION          TR3\n" );
		sql.append(" WHERE 1=1\n" );
		sql.append("   AND TSB.DLV_LOGI_ID = TSL.LOGI_ID(+)\n" );
		sql.append("   AND TR1.REGION_ID = TR2.PARENT_ID\n" );
		sql.append("   AND TR2.REGION_ID = TR3.PARENT_ID\n" );
		sql.append("   AND TR1.REGION_CODE = TSB.DLV_BAL_PROV_ID\n" );
		sql.append("   AND TR2.REGION_CODE = TSB.DLV_BAL_CITY_ID\n" );
		sql.append("   AND TR3.REGION_CODE = TSB.DLV_BAL_COUNTY_ID\n" );
		sql.append("   AND TSB.bo_status = '1'\n");
		sql.append("   AND TSB.HANDLE_STATUS<"+Constant.HANDLE_STATUS06+"\n");//组板审核已通过并未发运计划

		if(boardNo!=null&&!"".equals(boardNo)){
			sql.append("AND TSB.BO_NO LIKE ?\n");		
			params.add("%"+boardNo+"%");
		}
		if(transType!=null&&!"".equals(transType)){
			sql.append("AND TSB.DLV_SHIP_TYPE= ?\n");		
			params.add(transType);
		}
		if(logiName!=null&&!"".equals(logiName)){
			sql.append("AND TSB.DLV_LOGI_ID =?\n");		
			params.add(logiName);
		}
		if(startDate!=null&&!"".equals(startDate)){//提交日期开始过滤
			sql.append("   AND TSB.BO_DATE>=TO_DATE(?,'YYYY-MM-DD HH24:MI:SS')\n" );
			params.add(startDate+" 00:00:00");
		}
		if(endDate!=null&&!"".equals(endDate)){//提交日期结束过滤
			sql.append("  AND TSB.BO_DATE<=TO_DATE(?,'YYYY-MM-DD HH24:MI:SS')\n" );
			params.add(endDate +" 23:59:59");
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
		PageResult<Map<String, Object>> ps= dao.pageQuery(sql.toString(), params, getFunName(),pageSize,curPage);
		return ps;
	}
	
}
