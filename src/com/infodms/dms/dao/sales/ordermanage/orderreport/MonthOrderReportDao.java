package com.infodms.dms.dao.sales.ordermanage.orderreport;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.dao.sales.storageManage.CheckVehicleDAO;
import com.infodms.dms.util.businessUtil.GetVinUtil;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

public class MonthOrderReportDao extends BaseDao{

	public static Logger logger = Logger.getLogger(CheckVehicleDAO.class);
	private static final MonthOrderReportDao dao = new MonthOrderReportDao ();
	public static final MonthOrderReportDao getInstance() {
		return dao;
	}
	
	/***
	 *查询可提报的月度订单列表
	 * @param : 
	 * @return
	 * @throws Exception
	 */
	public static PageResult <Map<String,Object>> getCanMonthReportList(String dealerId,String year, String month, String areaId, int pageSize,int curPage){
		StringBuffer sql = new StringBuffer("");
		
		sql.append("SELECT TTO.ORDER_ID,\n");
		sql.append("       TTO.ORDER_NO,\n");  
		sql.append("       TTO.ORDER_YEAR || '年' || TTO.ORDER_MONTH || '月' AS ORDER_MONTH,\n");  
		sql.append("       TBA.AREA_NAME,\n");  
		sql.append("       TTO.ORDER_STATUS,\n");  
		sql.append("       SUM(TTOD.ORDER_AMOUNT) ORDER_AMONUT,\n");  
		sql.append("       TBA.AREA_ID\n");
		sql.append("  FROM TT_VS_ORDER TTO, TT_VS_ORDER_DETAIL TTOD, TM_BUSINESS_AREA TBA\n");  
		sql.append(" WHERE TTO.AREA_ID = TBA.AREA_ID\n");  
		sql.append("   AND TTO.ORDER_ID = TTOD.ORDER_ID\n");  
		sql.append("   AND TTO.AREA_ID = "+areaId+"\n");  
		sql.append("   AND TTO.ORDER_ORG_ID IN ("+dealerId+")\n");  
		sql.append("   AND TTO.ORDER_STATUS IN ("+Constant.ORDER_STATUS_01+","+Constant.ORDER_STATUS_04+")\n");  
		sql.append("   AND TTO.ORDER_YEAR = "+year+"\n");  
		sql.append("   AND TTO.ORDER_MONTH = "+month+"\n");  
		sql.append("   AND TTO.ORDER_TYPE = "+Constant.ORDER_TYPE_01+"\n");
		sql.append(" GROUP BY TTO.ORDER_ID,\n");  
		sql.append("          TTO.ORDER_NO,\n");  
		sql.append("          TTO.ORDER_YEAR || '年' || TTO.ORDER_MONTH || '月',\n");  
		sql.append("          TBA.AREA_NAME,\n");  
		sql.append("          TTO.ORDER_STATUS,\n");  
		sql.append("          TBA.AREA_ID\n");
		sql.append(" ORDER BY TTO.ORDER_ID DESC\n");

		return dao.pageQuery(sql.toString(), null,dao.getFunName(),pageSize ,curPage );
	}
	/**
	 *判断是否可以提报月度订单 
	 * */
	public static String isCommit(String dealerId,String year, String month, String areaId){
		String flag = "0";
		StringBuffer sql = new StringBuffer("");
		sql.append("SELECT TTO.ORDER_ID\n");
		sql.append("  FROM TT_VS_ORDER TTO\n");  
		sql.append(" WHERE TTO.ORDER_YEAR = "+year+"\n");  
		sql.append("   AND TTO.ORDER_MONTH = "+month+"\n");  
		sql.append("   AND TTO.AREA_ID = "+areaId+"\n");  
		sql.append("   AND TTO.ORDER_STATUS IN ("+Constant.ORDER_STATUS_02+","+Constant.ORDER_STATUS_03+", "+Constant.ORDER_STATUS_05+","+Constant.ORDER_STATUS_07+","+Constant.ORDER_STATUS_08+","+Constant.ORDER_STATUS_09+")\n");  
		sql.append("   AND TTO.ORDER_ORG_ID = "+dealerId+"\n");  
		sql.append("   AND TTO.ORDER_WEEK IS NULL\n");
		List<Map<String,Object>> list = dao.pageQuery(sql.toString(), null,dao.getFunName());
		if (null == list || list.size()<=0) {
			flag = "1";
		}
		return flag;
	}
	
	
	/***
	 *查询已提报的月度订单列表
	 * @param : 
	 * @return
	 * @throws Exception
	 */
	public static PageResult <Map<String,Object>> getHasReportList(String dealerId,String year, String month, String areaId, int pageSize,int curPage){
		StringBuffer sql = new StringBuffer("");
		
		sql.append("SELECT TTO.ORDER_ID,\n");
		sql.append("       TTO.ORDER_NO,\n");  
		sql.append("       TTO.ORDER_YEAR || '年' || TTO.ORDER_MONTH || '月' AS ORDER_MONTH,\n");  
		sql.append("       TBA.AREA_NAME,\n");  
		sql.append("       TTO.ORDER_STATUS,\n");  
		sql.append("       SUM(TTOD.ORDER_AMOUNT) ORDER_AMONUT,\n");  
		sql.append("       TBA.AREA_ID\n");
		sql.append("  FROM TT_VS_ORDER TTO, TT_VS_ORDER_DETAIL TTOD, TM_BUSINESS_AREA TBA\n");  
		sql.append(" WHERE TTO.AREA_ID = TBA.AREA_ID\n");  
		sql.append("   AND TTO.ORDER_ID = TTOD.ORDER_ID\n");  
		sql.append("   AND TTO.AREA_ID = "+areaId+"\n");  
		sql.append("   AND TTO.ORDER_ORG_ID IN ("+dealerId+")\n");  
		sql.append("   AND TTO.ORDER_STATUS IN ("+Constant.ORDER_STATUS_08+")\n");  
		sql.append("   AND TTO.ORDER_YEAR = "+year+"\n");  
		sql.append("   AND TTO.ORDER_MONTH = "+month+"\n");  
		sql.append(" GROUP BY TTO.ORDER_ID,\n");  
		sql.append("          TTO.ORDER_NO,\n");  
		sql.append("          TTO.ORDER_YEAR || '年' || TTO.ORDER_MONTH || '月',\n");  
		sql.append("          TBA.AREA_NAME,\n");  
		sql.append("          TTO.ORDER_STATUS,\n");  
		sql.append("          TBA.AREA_ID\n");
		sql.append(" ORDER BY TTO.ORDER_ID DESC\n");

		return dao.pageQuery(sql.toString(), null,dao.getFunName(),pageSize ,curPage );
	}
	
	/***
	 *查询可提报的月度详细订单列表
	 * @param : 
	 * @return
	 * @throws Exception
	 */
	public static List<Map<String, Object>> getCanMonthDetailReportList(String year, String month, String areaId, String order_id) {

		StringBuffer sql = new StringBuffer();

		sql.append("SELECT TTOD.DETAIL_ID,\n");
		sql.append("       TtO.order_org_id, \n");  
		sql.append("       TtO.PRODUCT_COMBO_ID, \n");  
		sql.append("       TVM.MATERIAL_CODE, --物料编号\n");  
		sql.append("       TVM.MATERIAL_NAME, --物料名称\n");  
		sql.append("       TVM.COLOR_NAME, --颜色\n");  
		sql.append("       TTOD.ORDER_AMOUNT, --数量\n");  
		sql.append("       G2.GROUP_NAME SERIES_NAME,\n");  
		sql.append("       TVM.MATERIAL_ID\n");
		sql.append("  FROM TT_VS_ORDER              TTO,\n");  
		sql.append("       TT_VS_ORDER_DETAIL       TTOD,\n");  
		sql.append("       TM_VHCL_MATERIAL         TVM,\n");  
		sql.append("       TM_VHCL_MATERIAL_GROUP_R R,\n");  
		sql.append("       TM_VHCL_MATERIAL_GROUP   G,\n");  
		sql.append("       TM_VHCL_MATERIAL_GROUP   G1,\n");  
		sql.append("       TM_VHCL_MATERIAL_GROUP   G2\n");  
		sql.append(" WHERE TTO.ORDER_ID = TTOD.ORDER_ID\n");  
		sql.append("   AND TTOD.MATERIAL_ID = TVM.MATERIAL_ID\n");  
		sql.append("   AND TVM.MATERIAL_ID = R.MATERIAL_ID\n");  
		sql.append("   AND R.GROUP_ID = G.GROUP_ID\n");  
		sql.append("   AND G.PARENT_GROUP_ID = G1.GROUP_ID\n");  
		sql.append("   AND G1.PARENT_GROUP_ID = G2.GROUP_ID\n");  
		sql.append("   AND TTO.ORDER_YEAR = "+year+"\n");  
		sql.append("   AND TTO.ORDER_MONTH = "+month+"\n");  
		sql.append("   AND TTO.AREA_ID = "+areaId+"\n"); 
		sql.append("   AND TTO.ORDER_ID = "+order_id+"\n");
		sql.append(" ORDER BY TTOD.CREATE_DATE DESC\n");

		List<Map<String, Object>> list = dao.pageQuery(sql.toString(), null,dao.getFunName());
		return list;
	}
	
	/***
	 *查询可提报:新增产品，根据物料id查询物料详细信息
	 * @param : 
	 * @return
	 * @throws Exception
	 */
	public static Map<String, Object> getMaterialInfo(String material_id) {

		StringBuffer sql = new StringBuffer();

		sql.append("SELECT TVM.MATERIAL_ID,\n");
		sql.append("       G2.GROUP_NAME,\n");  
		sql.append("       TVM.MATERIAL_CODE,\n");  
		sql.append("       TVM.MATERIAL_NAME,\n");  
		sql.append("       TVM.COLOR_NAME\n");  
		sql.append("  FROM TM_VHCL_MATERIAL         TVM,\n");  
		sql.append("       TM_VHCL_MATERIAL_GROUP_R R,\n");  
		sql.append("       TM_VHCL_MATERIAL_GROUP   G,\n");  
		sql.append("       TM_VHCL_MATERIAL_GROUP   G1,\n");  
		sql.append("       TM_VHCL_MATERIAL_GROUP   G2\n");  
		sql.append(" WHERE TVM.MATERIAL_ID = R.MATERIAL_ID\n");  
		sql.append("   AND R.GROUP_ID = G.GROUP_ID\n");  
		sql.append("   AND G.PARENT_GROUP_ID = G1.GROUP_ID\n");  
		sql.append("   AND G1.PARENT_GROUP_ID = G2.GROUP_ID\n");  
		sql.append("   AND TVM.MATERIAL_ID = "+material_id+"\n");

		Map<String, Object> materialInfo = dao.pageQueryMap(sql.toString(), null,dao.getFunName());
		return materialInfo;
	}
	/**
	 * 查看是否存在月度已保存但为提交的订单
	 * */
	public static Map<String,String> getOldOrder(String year, String month, String areaId, String dealerId){
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT TTO.ORDER_ID\n");
		sql.append("  FROM TT_VS_ORDER TTO\n");  
		sql.append(" WHERE TTO.ORDER_YEAR = "+year+"\n");  
		sql.append("   AND TTO.ORDER_MONTH = "+month+"\n");  
		sql.append("   AND TTO.AREA_ID = "+areaId+"\n");  
		sql.append("   AND TTO.ORDER_STATUS IN ("+Constant.ORDER_STATUS_01+", "+Constant.ORDER_STATUS_04+")\n");  
		sql.append("   AND TTO.ORDER_ORG_ID = "+dealerId+"\n");  
		sql.append("   AND TTO.ORDER_WEEK IS NULL\n");
		Map<String, String> oldOrder = dao.pageQueryMap(sql.toString(), null,dao.getFunName());
		return oldOrder;
	}
	
	/***
	 *查询可提报的月度订单审核记录
	 * @param : 
	 * @return
	 * @throws Exception
	 */
	public static List<Map<String, Object>> getCheckList(String order_id) {

		StringBuffer sql = new StringBuffer();
		sql.append("SELECT TVS.CHECK_TYPE,\n");
		sql.append("       TVS.CHECK_DESC,\n");  
		sql.append("       TO_CHAR(TVS.CHECK_DATE, 'yyyy-MM-dd') CHECK_DATE,\n");  
		sql.append("       TVS.CHECK_STATUS\n");
		sql.append("  FROM TT_VS_ORDER_CHECK TVS\n");  
		sql.append(" WHERE TVS.ORDER_ID = "+order_id+"\n");  
		sql.append(" ORDER BY TVS.CREATE_DATE\n");
		List<Map<String, Object>> list = dao.pageQuery(sql.toString(), null,dao.getFunName());
		return list;
	}
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}

}
