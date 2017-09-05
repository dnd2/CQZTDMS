package com.infodms.dms.dao.sales.storage.sendManage;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.infodms.dms.common.Constant;
import com.infodms.dms.common.materialManager.MaterialGroupManagerDao;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.po.TtSalesAllocaDePO;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

/**
 * 
 * @ClassName     : SendAssignmentDao 
 * @Description   : 配车管理DAO 
 * @author        : ranjian
 * CreateDate     : 2013-4-23
 */
public class AllocaDao  extends BaseDao<PO>{
	private static final AllocaDao dao = new AllocaDao ();
	public static final AllocaDao getInstance() {
		return dao;
	}
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		return null;
	}
	/**
	 * 组板信息查询(配置页面)
	 * 
	 * @param map
	 * @param curPage
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	public PageResult<Map<String, Object>> getAllocaSeachQuery(Map<String, Object> map, int curPage, int pageSize) throws Exception
	{
		Object[] objArr=getSql(map);
		PageResult<Map<String, Object>> ps= dao.pageQuery(String.valueOf(objArr[0]), (List<Object>)objArr[1], getFunName(),pageSize,curPage);
		return ps;
	}
	/**
	 * 组板信息查询(配置页面)统计
	 *param map 查询参数
	 *@return 
	 */
	public Map<String,Object> tgSum(Map<String, Object> map){
		Object[] obj=getSql(map);
		StringBuffer sbSql=new StringBuffer();
		sbSql.append("SELECT   SUM(NVL(BO_NUM, 0)) BO_NUM,SUM(NVL(ALLOCA_NUM, 0)) ALLOCA_NUM FROM (\n");
		sbSql.append(obj[0].toString());
		sbSql.append(")\n");
		Map<String, Object> mapR= dao.pageQueryMap(sbSql.toString(), (List<Object>)obj[1], getFunName());
		return mapR;
	}
	/**
	 * 发运分派查询(SQL)
	 * @param map
	 */
	public Object[] getSql(Map<String, Object> map){
		String raiseStartDate =CommonUtils.checkNull((String)map.get("raiseStartDate")); // 提报日期开始
		String raiseEndDate = CommonUtils.checkNull((String)map.get("raiseEndDate")); // 提报日期结束
		String boNo = CommonUtils.checkNull((String)map.get("boNo")); //组板编号
		String vin = CommonUtils.checkNull((String)map.get("VIN")); //VIN
		String poseId = CommonUtils.checkNull((String)map.get("poseId"));
		/******************************页面查询字段end***************************/
		List<Object> params = new LinkedList<Object>();
		params.add(Constant.IF_TYPE_YES);
		params.add(Constant.HANDLE_STATUS01);//未配车的
		params.add(Constant.HANDLE_STATUS02);//部分配车
		StringBuffer sbSql=new StringBuffer();
		sbSql.append("SELECT   A.BO_ID,A.BO_NO,\n");
		sbSql.append("         B.NAME,\n");
		sbSql.append("         TO_CHAR (A.BO_DATE, 'YYYY-MM-DD HH24:MI:SS') AS BO_DATE,\n");
		sbSql.append("         DECODE(A.HAVE_RETAIL,'1','是','0','否',null,'否') HAVE_RETAIL,\n");
		sbSql.append("         NVL (A.BO_NUM, 0) BO_NUM,\n");
		sbSql.append("         NVL (A.ALLOCA_NUM, 0) ALLOCA_NUM,\n");
		sbSql.append("         NVL (A.OUT_NUM, 0) OUT_NUM,\n");
		sbSql.append("         NVL (A.SEND_NUM, 0) SEND_NUM,\n");
		sbSql.append("         NVL (A.ACC_NUM, 0) ACC_NUM,\n");
		sbSql.append("(SELECT T.SEND_TYPE FROM TT_SALES_BO_DETAIL T WHERE  T.BO_ID=A.BO_ID AND ROWNUM=1) SEND_TYPE"); 
		sbSql.append("  FROM   TT_SALES_BOARD A, TC_USER B\n");
		sbSql.append(" WHERE   A.BO_PER = B.USER_ID\n");
		sbSql.append(" AND A.IS_ENABLE=?\n"); //有效的
		sbSql.append(" AND A.HANDLE_STATUS in(?,?)\n"); //未配车、部分配车
		sbSql.append(MaterialGroupManagerDao.getPoseIdBusinessSqlByPar("A.AREA_ID",poseId,params));//车厂端查询列表产地数据权限
		
		if(raiseStartDate!=null&&!"".equals(raiseStartDate)){//下线日期开始过滤
			sbSql.append("   AND A.BO_DATE>=TO_DATE(?,'YYYY-MM-DD HH24:MI:SS')\n" );
			params.add(raiseStartDate+" 00:00:00");
		}
		if(raiseEndDate!=null&&!"".equals(raiseEndDate)){//下线日期结束过滤
			sbSql.append("   AND A.BO_DATE<=TO_DATE(?,'YYYY-MM-DD HH24:MI:SS')\n" );
			params.add(raiseEndDate +" 23:59:59");
		}
		if(!"".equals(boNo)){
			sbSql.append("AND A.BO_NO LIKE ?\n");
			params.add("%"+boNo+"%");
		}
		if(!"".equals(vin)){
			sbSql.append(MaterialGroupManagerDao.geVinSqlPar("A.BO_ID",vin,params)); 
		}
		sbSql.append(" ORDER BY A.BO_DATE ASC\n");
		Object[] obj=new Object[2];
		obj[0]=sbSql;
		obj[1]=params;
		return obj;
	}
	/**
	 * 组板物料列表查询
	 * @param params 查询参数LIST
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> getSendBoardMatListQuery(List<Object> params)throws Exception{
		StringBuffer sbSql = new StringBuffer();
		sbSql.append("SELECT TSBD.BO_DE_ID,\n");
		sbSql.append("       TSB.BO_ID,\n");
		sbSql.append("       TSB.BO_NO, --组板号\n");
		sbSql.append("       TSB.AREA_ID YELID, --产地\n");
		sbSql.append("       VMGM.SERIES_NAME, --车系\n");
		sbSql.append("       VMGM.MODEL_NAME, --车型\n");
		sbSql.append("       VMGM.PACKAGE_NAME, --配置\n");
		sbSql.append("       NVL(TSB.CAR_NO,'') CAR_NO, \n");
		sbSql.append("       TSB.LOADS,\n");
		sbSql.append("       TSB.CAR_TEAM,\n");
		sbSql.append("       --VMGM.SERIES_NAME, \n");
		sbSql.append("       TSBD.ORDER_TYPE, \n");
		sbSql.append("       TSBD.DEALER_ID, \n");
		sbSql.append("       TSBD.REC_DEALER_ID, \n");
		sbSql.append("       TD.DEALER_CODE,TD.DEALER_NAME, \n");
		sbSql.append("       TSBD.LOGI_ID,\n");
		sbSql.append("       TSBD.ADDRESS_ID, \n");
		sbSql.append("       TSBD.INVOICE_NO, \n");
		sbSql.append("       TSBD.ORDER_NO, \n");
		sbSql.append("       TSBD.ORDER_ID, \n");
		sbSql.append("       TSBD.IS_RETAIL, \n");
		sbSql.append("       TSBD.AREA_ID, \n");
		sbSql.append("       VMGM.MATERIAL_ID, \n");
		sbSql.append("       VMGM.MATERIAL_CODE, \n");
		sbSql.append("       VMGM.MATERIAL_NAME, \n");
		sbSql.append("       VMGM.COLOR_NAME, \n");
		sbSql.append("       B.ORDER_TYPE, \n");
		sbSql.append("       NVL(TSBD.INVOICE_NUM, 0) CHECK_AMOUNT, \n");
		sbSql.append("       NVL(TSBD.BOARD_NUM, 0) THIS_BOARD_NUM,  \n");
		sbSql.append("       NVL(TSBD.ALLOCA_NUM, 0) ALLOCA_NUM  \n");
		sbSql.append("  from TT_SALES_BO_DETAIL    TSBD,\n");
		sbSql.append("       VW_MATERIAL_GROUP_MAT      VMGM,\n");
		sbSql.append("       TT_SALES_BOARD        TSB,\n");
		sbSql.append("       TM_DEALER TD,");
		sbSql.append("       TT_VS_ORDER_DETAIL A,");
		sbSql.append("       TT_VS_ORDER B");
		sbSql.append(" WHERE TSBD.MAT_ID = VMGM.MATERIAL_ID(+)\n");
		sbSql.append("   AND TSB.BO_ID = TSBD.BO_ID\n");
		sbSql.append("   AND TSBD.OR_DE_ID = A.DETAIL_ID\n");
		sbSql.append("   AND A.ORDER_ID = B.ORDER_ID\n");
		sbSql.append("   AND TD.DEALER_ID = TSBD.REC_DEALER_ID \n");
		sbSql.append("   AND NVL(TSBD.BOARD_NUM, 0)>0 \n");
		
		sbSql.append(" AND TSBD.BO_ID=? \n");
		sbSql.append(" ORDER BY TSBD.ORDER_ID \n");
		List<Map<String, Object>> list= dao.pageQuery(sbSql.toString(), params, getFunName());
		return list;
	}
	/**
	 * 根据组板详细信息ID获取该ID下的配车信息列表
	 * @param params 查询参数LIST
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> getVehicleQueryByBoDeId(List<Object> params)throws Exception{
		StringBuffer sbSql= new StringBuffer();
		sbSql.append("SELECT TSAD.DETAIL_ID, --配车明细ID\n");
		sbSql.append("        TV.VEHICLE_ID, --车辆ID\n");
		sbSql.append("        TV.VIN, --VIN号\n");
		sbSql.append("        TV.LIFE_CYCLE, --生命周期\n");
		sbSql.append("        TV.ENGINE_NO, --发动机号\n");
		sbSql.append("        TO_CHAR(TV.ORG_STORAGE_DATE, 'YYYY-MM-DD') ORG_STORAGE_DATE, --入库日期\n");
		sbSql.append("        TSA.AREA_NAME, --库区\n");
		sbSql.append("        TSR.ROAD_NAME, --库道\n");
		sbSql.append("        TSS.SIT_NAME, --库位\n");
		sbSql.append("        TV.SPECIAL_ORDER_NO\n");
		sbSql.append("   FROM TT_SALES_ALLOCA_DE TSAD, --配车明细表\n");
		sbSql.append("        TM_VEHICLE         TV, --车辆表\n");
		sbSql.append("        TT_SALES_SIT       TSS, --库位表\n");
		sbSql.append("        TT_SALES_ROAD      TSR, --库道表\n");
		sbSql.append("        TT_SALES_AREA      TSA --库区表\n");
		sbSql.append("  WHERE TSAD.VEHICLE_ID = TV.VEHICLE_ID\n");
		sbSql.append("    AND TV.SIT_ID = TSS.SIT_ID\n");
		sbSql.append("    AND TSS.ROAD_ID = TSR.ROAD_ID\n");
		sbSql.append("    AND TSR.AREA_ID = TSA.AREA_ID\n");
		sbSql.append("    AND TSAD.STATUS="+Constant.STATUS_ENABLE+"\n");
		sbSql.append("     AND TSAD.BO_DE_ID = ?"); 
		List<Map<String, Object>> list= dao.pageQuery(sbSql.toString(), params, getFunName());
		return list;
	}
	/**
	 * 根据组板详细信息ID获取该ID下的各省配车信息列表
	 * @param params 查询参数LIST
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> getOrgQueryByBoDeId(List<Object> params)throws Exception{
		StringBuffer sbSql= new StringBuffer();
		sbSql.append("SELECT A.ORG_ID, nvl(A.AMOUNT,0) AMOUNT, nvl(A.ALLOCA_NUM,0) ALLOCA_NUM,nvl(A.AMOUNT,0)-nvl(A.ALLOCA_NUM,0) CAN_COUNT,A.CREATE_ID\n");
		sbSql.append("  FROM TT_VS_ORDER_RESOURCE_RESERVE A, TT_SALES_BO_DETAIL B\n");
		sbSql.append(" WHERE A.ORDER_DETAIL_ID = B.OR_DE_ID\n");
		sbSql.append("   AND A.AMOUNT>0\n"); 
		sbSql.append("   AND B.BO_DE_ID = ?\n");
		sbSql.append("   AND A.RESERVE_STATUS = ?\n");

		List<Map<String, Object>> list= dao.pageQuery(sbSql.toString(), params, getFunName());
		return list;
	}
	/**
	 * 配车详细表添加数据
	 * @param po 配车详细表实体
	 * @return
	 * @throws Exception
	 */
	public void addAllocaDe(TtSalesAllocaDePO po) throws Exception{
		dao.insert(po);
	}
	/**
	 * 根据配车详细表ID删除配车详细表信息
	 * @param params 参数list
	 * @return
	 * @throws Exception
	 */
	public void delAllocaDeById(List<Object> params) throws Exception{
		StringBuffer sql= new StringBuffer();
		sql.append("DELETE FROM TT_SALES_ALLOCA_DE TSAD WHERE TSAD.DETAIL_ID=?\n"); 
		dao.delete(sql.toString(), params);
	}
	/**
	 * 修改车辆表out_status状态
	 * @param params 参数list
	 * @return
	 * @throws Exception
	 */
	public void updateVehicleOutStatus(List<Object> params) throws Exception{
		StringBuffer sbSql= new StringBuffer();
		sbSql.append("UPDATE TM_VEHICLE TV\r\n");
		sbSql.append("   SET TV.OUT_STATUS =\r\n");
		sbSql.append("       (SELECT DECODE(A.ORDER_TYPE, ?, ?, ?) O_STATUS\r\n");
		sbSql.append("          FROM TT_SALES_BO_DETAIL A\r\n");
		sbSql.append("         WHERE A.BO_DE_ID = ?),\r\n");
		sbSql.append("       TV.LOCK_STATUS = ?,\r\n");
		sbSql.append("       TV.DEALER_ID  =\r\n");
		sbSql.append("		(SELECT CASE\r\n");
		sbSql.append("		WHEN NVL(TSO.GROUP_CUS_ID, 0) > 0 THEN\r\n");
		sbSql.append("		TSBD.DEALER_ID\r\n");
		sbSql.append("		ELSE\r\n");
		sbSql.append("		TSBD.REC_DEALER_ID\r\n");
		sbSql.append("		END O_STATUS\r\n");
		sbSql.append("		FROM TT_SALES_BO_DETAIL TSBD\r\n");
		sbSql.append("		LEFT JOIN TT_VS_ORDER_DETAIL TVOD\r\n");
		sbSql.append("		ON TSBD.OR_DE_ID = TVOD.DETAIL_ID\r\n");
		sbSql.append("		AND TSBD.IS_ENABLE = 10041001\r\n");
		sbSql.append("		LEFT JOIN TT_VS_ORDER TVO\r\n");
		sbSql.append("		ON TVOD.ORDER_ID = TVO.ORDER_ID\r\n");
		sbSql.append("		LEFT JOIN TT_SA_ORDER TSO\r\n");
		sbSql.append("		ON TVO.WR_ORDER_ID = TSO.ORDER_ID\r\n");
		sbSql.append("		WHERE TSBD.BO_DE_ID = ? AND ROWNUM = 1),\r\n");
//		sbSql.append("       (SELECT CASE\r\n");
//		sbSql.append("                 WHEN a.order_type = 10201005 then\r\n");
//		sbSql.append("                  a.DEALER_ID\r\n");
//		sbSql.append("                 ELSE\r\n");
//		sbSql.append("                  a.rec_DEALER_ID\r\n");
//		sbSql.append("               END O_STATUS\r\n");
//		sbSql.append("          FROM TT_SALES_BO_DETAIL A\r\n");
//		sbSql.append("         WHERE A.BO_DE_ID = ?),\r\n");
		sbSql.append("       TV.UPDATE_BY   = ?,\r\n");
		sbSql.append("       TV.UPDATE_DATE = SYSDATE\r\n");
		sbSql.append(" WHERE TV.VEHICLE_ID = ?"); 
		dao.update(sbSql.toString(), params);
	}
	/**
	 * 根据配车明细ID获取非该配车明细ID 信息
	 * @param MatId 配车IDS
	 * @param boDeId 组板明细IDS
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> getNotMatByMatId(String[] MatId,String[] boDeId)throws Exception{
		List<Object> params=new ArrayList<Object>();
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT TSAD.DETAIL_ID, TSAD.VEHICLE_ID\r\n");
		sql.append("  FROM TT_SALES_ALLOCA_DE TSAD\r\n");
		sql.append(" WHERE TSAD.BO_DE_ID IN (''");
		if(boDeId!=null && boDeId.length>0){
			for(int i=0;i<boDeId.length;i++){
					sql.append(",").append(Long.parseLong(boDeId[i]));
			}
		}
		sql.append(",'')\n");
		sql.append("MINUS\r\n");
		sql.append("SELECT TSAD1.DETAIL_ID, TSAD1.VEHICLE_ID\r\n");
		sql.append("  FROM TT_SALES_ALLOCA_DE TSAD1\r\n");
		sql.append(" WHERE TSAD1.BO_DE_ID  IN (''");
		if(boDeId!=null && boDeId.length>0){
			for(int i=0;i<boDeId.length;i++){
					sql.append(",").append(Long.parseLong(boDeId[i]));
			}
		}
		sql.append(",'')\n");
		sql.append("   AND TSAD1.DETAIL_ID IN (''"); 
		if(MatId!=null && MatId.length>0){
			for(int i=0;i<MatId.length;i++){
				if(!MatId[i].equals("newVechile")){
					sql.append(",").append(Long.parseLong(MatId[i]));
				}
			}
		}
		sql.append(",'')\n");
		List<Map<String, Object>> list= dao.pageQuery(sql.toString(), null, getFunName());
		return list;
	}
	/**
	 * 根据车辆IDS获取非该配车明细ID 信息
	 * @param vehicleIds 车辆IDS
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> getVehicleIdsByMatId(String[] vehicleIds)throws Exception{
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT TSAD.VEHICLE_ID, TSA.AREA_NAME, TSR.ROAD_NAME, TSS.SIT_NAME\r\n");
		sql.append("  FROM TT_SALES_ALLOCA_DE TSAD,\r\n");
		sql.append("       TT_SALES_AREA      TSA,\r\n");
		sql.append("       TT_SALES_ROAD      TSR,\r\n");
		sql.append("       TT_SALES_SIT       TSS\r\n");
		sql.append(" WHERE TSAD.VEHICLE_ID = TSS.VEHICLE_ID\r\n");
		sql.append("   AND TSS.ROAD_ID = TSR.ROAD_ID\r\n");
		sql.append("   AND TSR.AREA_ID = TSA.AREA_ID");
		sql.append("   AND TSAD.STATUS = "+Constant.STATUS_ENABLE); 
		sql.append("   AND TSAD.VEHICLE_ID IN (''"); 
		if(vehicleIds!=null && vehicleIds.length>0){
			for(int i=0;i<vehicleIds.length;i++){
					sql.append(",").append(Long.parseLong(vehicleIds[i]==null?"0":vehicleIds[i]));
			}
		}
		sql.append(",'')\n");
		List<Map<String, Object>> list= dao.pageQuery(sql.toString(), null, getFunName());
		return list;
	}
	/**
	 * 一键选车
	 * 
	 * @param map
	 * @param curPage
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> getOneChooseVehicle(String materialId,String count,String vehicleIds) throws Exception
	{
		int count1 =Integer.parseInt(count); // 选中几条
		/******************************页面查询字段end***************************/
		List<Object> params = new LinkedList<Object>();
		params.add(materialId);//物料ID
		params.add(Constant.VEHICLE_LIFE_02);//生命周期（车厂库存）
		params.add(Constant.LOCK_STATUS_01);//锁定状态（正常状态）
		params.add(count1);//选取数量
		StringBuffer sbSql=new StringBuffer();
		sbSql.append("SELECT * FROM (SELECT '' AS DETAIL_ID,TV.VEHICLE_ID, --车辆ID\n");
		sbSql.append("       TV.VIN, --VIN号\n");
		sbSql.append("       TV.LIFE_CYCLE, --生命周期\n");
		sbSql.append("       TV.ENGINE_NO, --发动机号\n");
		sbSql.append("       TO_CHAR(TV.ORG_STORAGE_DATE, 'YYYY-MM-DD HH24:MI:SS') ORG_STORAGE_DATE, --入库日期\n");
		sbSql.append("       TSA.AREA_NAME, --库区\n");
		sbSql.append("       TSR.ROAD_NAME, --库道\n");
		sbSql.append("       TSS.SIT_NAME, --库位\n");
		sbSql.append("       TV.SD_NUMBER\n");
		sbSql.append("  FROM TM_VEHICLE    TV, --车辆表\n");
		sbSql.append("       TT_SALES_SIT  TSS, --库位表\n");
		sbSql.append("       TT_SALES_ROAD TSR, --库道表\n");
		sbSql.append("       TT_SALES_AREA TSA --库区表\n");
		sbSql.append(" WHERE TV.SIT_ID = TSS.SIT_ID\n");
		sbSql.append("   AND TV.VEHICLE_ID = TSS.VEHICLE_ID\n");
		sbSql.append("   AND TSS.ROAD_ID = TSR.ROAD_ID\n");
		sbSql.append("   AND TSR.AREA_ID = TSA.AREA_ID\n");
		sbSql.append("   AND TSA.STATUS = 10011001\n");
		sbSql.append("   AND TSA.OUT_STATUS = 9701001\n");
		sbSql.append("   AND TV.MATERIAL_ID = ?\n");
		sbSql.append("   AND TV.LIFE_CYCLE = ?\n");
		sbSql.append("   AND TV.LOCK_STATUS = ?\n");
	//	sbSql.append("   AND TV.CREATE_TYPE = ?\n");
		//sbSql.append("   AND TV.MATERIAL_ID = ?\n");
		if(vehicleIds!="" && !vehicleIds.equals("")){
			vehicleIds=vehicleIds.substring(0,vehicleIds.length()-1);
			sbSql.append("   AND TV.VEHICLE_ID NOT IN("+vehicleIds+")\n");
		}
//		if(createId!="" && !createId.equals("")){//特殊订单
//			params.add(createId);//特殊订单
//		}
		sbSql.append(" ORDER BY TV.ORG_STORAGE_DATE ASC) WHERE  ROWNUM <=?"); 
		List<Map<String, Object>> list= dao.pageQuery(sbSql.toString(), params, getFunName());
		return list;
	}
}