package com.infodms.dms.dao.sales.storage.storagebase;

import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.infodms.dms.common.Constant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.common.materialManager.MaterialGroupManagerDao;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.po.TtSalesFarePO;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

/**
 * 
 * @ClassName     : FareRatioDao 
 * @Description   : 运费系数设定DAO 
 * @author        : ranjian
 * CreateDate     : 2013-4-1
 */
public class FareRatioDao extends BaseDao<PO>{
	private static final FareRatioDao dao = new FareRatioDao ();
	public static final FareRatioDao getInstance() {
		return dao;
	}
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}
	/**
	 * 运费设定信息查询
	 * @param map
	 * @param curPage
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	public PageResult<Map<String, Object>> getFareRatioQuery(String groupId, int curPage, int pageSize)throws Exception{
		StringBuffer sql= new StringBuffer();
		sql.append("select t.set_id,t.series_id,t.ratio_num,\n");
		sql.append("       t2.group_code,\n");
		sql.append("       t2.group_name\n");
		sql.append("  from tm_fare_ratio t, TM_VHCL_MATERIAL_GROUP t2\n");
		sql.append(" where 1 = 1\n");
		sql.append("   and t.series_id = t2.group_id\n");
		if(groupId!=null && !"".equals(groupId)){
			sql.append("   and t.series_id ="+groupId+"\n");
		}
		sql.append("  order by t.create_date desc");

		PageResult<Map<String, Object>> ps= dao.pageQuery(sql.toString(), null, getFunName(),pageSize,curPage);
		return ps;
	}
	/**
	 * 
	 * @Title      : 
	 * @Description: 运费设定信息添加 
	 * @param      : @param ttSalesAreaPO      
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-5
	 */
	public void fareSetAdd(TtSalesFarePO ttSalesFarePO) {
		dao.insert(ttSalesFarePO);
    }
	/**
	 * 
	 * @Title      : 
	 * @Description: 运费设定信息修改 
	 * @param      : @param seachSalesAreaPO
	 * @param      : @param ttSalesAreaPO      
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-5
	 */
	public void fareSetUpdate(TtSalesFarePO seachPO,TtSalesFarePO ttSalesFarePO) {
		dao.update(seachPO, ttSalesFarePO);
    }
	/**
	 * 
	 * @Title      : 
	 * @Description: 根据车系ID得到车系信息
	 * @param      : @return     车系信息
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-5
	 */
	public List<Map<String, Object>> getVhclMsg (String yieldly)throws Exception{
		StringBuffer sql= new StringBuffer();
		sql.append("select group_id,group_code,group_name from tm_vhcl_material_group A where group_level=2 and status="+Constant.STATUS_ENABLE+"\n");
		/*if(!"".equals(yieldly)){//查询产地下的车系
			sql.append("AND EXISTS(SELECT 1 FROM TM_AREA_GROUP B WHERE B.MATERIAL_GROUP_ID=A.GROUP_ID AND B.AREA_ID="+yieldly+")");
		}*/
		List<Map<String, Object>> list= dao.pageQuery(sql.toString(), null, getFunName());
		return list;
	}
	/**
	 * 
	 * @Title      : 
	 * @Description: 查询里程信息
	 * @param      : @return     里程信息
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-5
	 */
	public List<Map<String, Object>> getFareMileageMsg (String yieldly,String poseId)throws Exception{
		StringBuffer sql= new StringBuffer();
		//ROWNUM用于添加事件用
		sql.append("select ROWNUM,TSM.MIL_ID,TSM.MIL_START,TSM.MIL_END,TSM.YIELDLY from TT_SALES_MILSET TSM WHERE 1=1 \n");
		sql.append(MaterialGroupManagerDao.getPoseIdBusinessSql("TSM.YIELDLY", poseId));
		if(yieldly!=null && !"".equals(yieldly)){
			sql.append(" AND TSM.YIELDLY=");
			sql.append(Long.parseLong(yieldly));
		}
		sql.append(" ORDER BY TSM.MIL_START");
/*		if(yieldly){
			
		}*/
		List<Map<String, Object>> list= dao.pageQuery(sql.toString(), null, getFunName());
		return list;
	}
	/**
	 * 
	 * @Title      : 
	 * @Description: 查询里程设定信息
	 * @param      : @return     里程设定信息
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-5
	 */
	public List<Map<String, Object>> getFareSetMsg (String yieldly,String milId,String groupId)throws Exception{
		StringBuffer sql= new StringBuffer();
		//ROWNUM用于添加事件用
		sql.append("select TSF.FARE_ID,TSF.MIL_ID,TSF.GROUP_ID,TSF.YIELDLY,TSF.AMOUNT from TT_SALES_FARE TSF WHERE 1=1 \n");
		if(yieldly!=null && !"".equals(yieldly)){
			sql.append(" AND TSF.YIELDLY=");
			sql.append(Long.parseLong(yieldly));
		}
		if(milId!=null && !"".equals(milId)){
			sql.append(" AND TSF.MIL_ID=");
			sql.append(Long.parseLong(milId));
		}
		if(groupId!=null && !"".equals(groupId)){
			sql.append(" AND TSF.GROUP_ID=");
			sql.append(Long.parseLong(groupId));
		}
		sql.append(" ORDER BY TSF.FARE_ID");
		List<Map<String, Object>> list= dao.pageQuery(sql.toString(), null, getFunName());
		return list;
	}
	/**
	 * 
	 * @Title      : 
	 * @Description: 根据产地和车系ID得到运费得到单条信息
	 * @param      : @param id
	 * @param      : @return     
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-5
	 */
	public Map<String, Object> getFareSetMsg(String groupId,String yieldly,Long poseId)throws Exception{
		//--获取某产地下的所有运费里程信息
		List<Map<String, Object>> list_FM =getFareMileageMsg(yieldly,poseId.toString());	
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT TVMG.GROUP_ID,TVMG.GROUP_CODE,\n");
		sql.append("TVMG.GROUP_NAME,\n");
		if(list_FM!=null && list_FM.size()>0){
			for(int i=0;i<list_FM.size();i++){//拼接里程信息字段
				Map<String, Object> FM=(Map<String, Object>)list_FM.get(i);
				sql.append("SUM(decode(tsf.mil_id,");
				sql.append(FM.get("MIL_ID"));
				sql.append(", tsf.amount, 0))\n");
				sql.append(" AMOUNT").append(i);//列字段
				sql.append(",\n");			
			}
		}
		sql.append("TSF.YIELDLY,TBA.AREA_NAME \n");
		sql.append(" FROM TT_SALES_FARE TSF, tm_vhcl_material_group TVMG, tt_sales_milset tsm,TM_BUSINESS_AREA TBA\n");
		sql.append(" WHERE TSF.GROUP_ID = TVMG.GROUP_ID AND TSF.YIELDLY=TBA.AREA_ID\n");
		sql.append(" and tsf.mil_id = tsm.mil_id\n");
			sql.append(" and tsf.yieldly=\n");
			sql.append(Long.parseLong(yieldly));
			sql.append(" and TSF.GROUP_ID=\n");
			sql.append(Long.parseLong(groupId));
		sql.append(" group by TVMG.GROUP_ID,TVMG.GROUP_CODE, TVMG.GROUP_NAME, TSF.YIELDLY,TBA.AREA_NAME\n");
		Map<String, Object> map = pageQueryMap(sql.toString(),null,getFunName());
		return map;
	}
	/**
	 * 
	 * @Title      : 
	 * @Description: 根据产地，车系删除运费设定 信息
	 * @param      : @param seachSalesAreaPO
	 * @param      : @param ttSalesAreaPO      
	 * @return     :    
	 * @throws     :
	 * LastDate    : 2013-4-6
	 */
	public void fareSetDelete(TtSalesFarePO tsp) {
		String sql="delete from tt_sales_fare tsf where tsf.yieldly="+tsp.getYieldly()+" and  tsf.group_id="+tsp.getGroupId();
		if(tsp.getYieldly()!=null && tsp.getGroupId()!=null){
			dao.update(sql, null);
		}
    }
	
	/**
	 * 在途位置维护(查询)(分页)
	 * @param shipNo
	 * @param shipStatus
	 * @param dealerCode
	 * @param startDate
	 * @param endDate
	 * @param endDate 
	 * @param county_id 
	 * @param city_id 
	 * @param province_id 
	 * @param curPage
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	public PageResult<Map<String, Object>> getLocationMaintainList(String shipNo,String shipStatus,String dealerCode, 
			String logi_name_seach, String startDate, String endDate, String province_id, String city_id, 
			String county_id, int curPage, int pageSize) throws Exception {
		
		StringBuffer sql= new StringBuffer();
		List<Object> params = new LinkedList<Object>();
		sql.append("SELECT B.LOGI_NAME,\n");
		sql.append("       A.BILL_NO,\n");
		sql.append("       TO_CHAR(A.BILL_CRT_DATE, 'yyyy-MM-dd') BILL_CRT_DATE,\n");
		sql.append("       A.BILL_ID,\n");
		sql.append("       C.DEALER_NAME,\n");
		sql.append("       D.TEL PHONE,\n");
		sql.append("       D.LINK_MAN,\n");
		sql.append("       D.ADDRESS\n");
		sql.append("  FROM TT_SALES_WAYBILL A, TT_SALES_LOGI B, TM_DEALER C, TM_VS_ADDRESS D\n");
		sql.append(" WHERE A.LOGI_ID = B.LOGI_ID\n");
		sql.append("   AND A.SEND_DEALER_ID = C.DEALER_ID(+)\n");
		sql.append("   AND A.ADDRESS_ID = D.ID(+)"); 
		if(shipNo!=null&&!"".equals(shipNo)){
			sql.append(" and a.bill_no like ?\n");
			params.add("%"+shipNo+"%");
		}
		if(shipStatus!=null&&!"".equals(shipStatus)){
			sql.append(" and b.logi_id=?\n");
			params.add(shipStatus);
		}
		if(logi_name_seach!=null&&!"".equals(logi_name_seach)) {
			sql.append(" and b.LOGI_ID=?\n" );
			params.add(logi_name_seach);
		}
		if(startDate!=null&&!"".equals(startDate)){
			sql.append(" and a.BILL_CRT_DATE>= TO_DATE(?, 'YYYY-MM-DD HH24:MI:SS')\n" );
			params.add(startDate);
		}
		if(endDate!=null&&!"".equals(endDate)){
			sql.append(" and a.BILL_CRT_DATE<= TO_DATE(?, 'YYYY-MM-DD HH24:MI:SS')\n" );
			params.add(endDate);
		}
		if (dealerCode != null && !"".equals(dealerCode))
		{//经销商code
			sql.append(Utility.getConSqlByParamForEqual(dealerCode, params,"C", "DEALER_CODE"));
		}
		sql.append(" order by a.bill_id desc\n");
		PageResult<Map<String, Object>> ps= dao.pageQuery(sql.toString(), params, getFunName(),pageSize,curPage);
		return ps;
	}
	
	/**
	 *  根据发运ID找到车辆信息
	 * @param billId
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> getMateriaDetail(String billId) throws Exception{
		StringBuffer sbSql = new StringBuffer();

		sbSql.append("SELECT VMT.SERIES_NAME, VMT.PACKAGE_NAME, C.VIN, e.order_no, F.ADDRESS, to_char(od.create_date, 'yyyy-mm-dd hh24:mi') SO_DATE\n");
		sbSql.append("  FROM TT_SALES_WAYBILL      A,\n");
		sbSql.append("       TT_SALES_ALLOCA_DE    B,\n");
		sbSql.append("       TM_VEHICLE            C,\n");
		sbSql.append("       tt_sales_board        d,\n");
		sbSql.append("       tt_sales_bo_detail    e,\n");
		sbSql.append("       tt_vs_order_detail    od,\n");
		sbSql.append("       TM_VS_ADDRESS         F,\n");
		sbSql.append("       VW_MATERIAL_GROUP_MAT VMT\n");
		sbSql.append(" WHERE A.BILL_ID = e.bill_id\n");
		sbSql.append("   AND B.VEHICLE_ID = C.VEHICLE_ID\n");
		sbSql.append("   AND B.Bo_De_Id =e.bo_de_id\n");
		sbSql.append("   AND e.bo_id = d.bo_id\n");
		sbSql.append("   AND e.or_de_id = od.detail_id\n");
		sbSql.append("   AND C.MATERIAL_ID = VMT.MATERIAL_ID\n");
		sbSql.append("   AND A.ADDRESS_ID = F.ID\n");
		sbSql.append("   and A.BILL_ID = "+billId+"\n");

		List<Map<String, Object>> list = dao.pageQuery(sbSql.toString(), null, getFunName());
		return list;
	}
	
	/**
	 *  根据发运ID找到位置明细
	 * @param billId
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> getAddressDetail(String billId) throws Exception{
		StringBuffer sql = new StringBuffer();

		sql.append("select to_char(b.zt_date, 'yyyy-MM-dd') zt_date, b.zt_address,b.is_sh,b.detail_id\n");
		sql.append("  from TT_SALES_WAYBILL a, tt_vehicle_address b\n"); 
		sql.append(" where a.bill_id = b.bill_id\n");
		sql.append("   AND a.bill_id = "+billId+"\n");

		List<Map<String, Object>> list = dao.pageQuery(sql.toString(), null, getFunName());
		return list;
	}
	
	/**
	 * 根据发运ID找到位置明细(并且要位置审核通过了的)
	 */
	public List<Map<String, Object>> getAddressShDetail(String billId) throws Exception{
		StringBuffer sql = new StringBuffer();

		sql.append("select to_char(b.zt_date, 'yyyy-MM-dd') zt_date, b.zt_address,b.is_sh,b.detail_id\n");
		sql.append("  from TT_SALES_WAYBILL a, tt_vehicle_address b\n"); 
		sql.append(" where a.bill_id = b.bill_id\n");
		sql.append(" and a.bill_id = "+billId+"\n");

		List<Map<String, Object>> list = dao.pageQuery(sql.toString(), null, getFunName());
		return list;
	}
}
