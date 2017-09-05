package com.infodms.dms.dao.sales.storage.sendManage;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import bsh.util.Util;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.common.materialManager.MaterialGroupManagerDao;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.po.TtSalesBoDetailPO;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

/**
 * 
 * @ClassName     : WaybillManageDao
 * @Description   : 运单生成管理DAO 
 * @author        : wenyd
 * CreateDate     : 2013-4-28
 */
public class WaybillManageDao extends BaseDao<PO> {
	private static final WaybillManageDao dao = new WaybillManageDao ();
	public static final WaybillManageDao getInstance() {
		return dao;
	}
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}
	public List<TtSalesBoDetailPO> select(TtSalesBoDetailPO t) {
		return factory.select(t);
	}
	
//	public PageResult<Map<String, Object>> getWaybillManageQuery(Map<String, Object> map, int curPage, int pageSize)throws Exception{
//		String yieldly = (String)map.get("yieldly"); // 产地		
//		String dealerCode =(String)map.get("dealerCode"); // 经销商CODE
//		String orderType = (String)map.get("orderType"); // 订单类型
//		String orderNo = (String)map.get("orderNo"); // 销售订单号
//		String invoiceNo = (String)map.get("invoiceNo"); // 发票号
//		String logiName = (String)map.get("logiName"); // 物流公司
//		String raiseStartDate =(String)map.get("boStartDate"); // 组板日期开始
//		String raiseEndDate = (String)map.get("boEndDate"); // 组板日期结束
//		String boNo = (String)map.get("boNo"); //组板编号
//		String allocaStartDate =(String)map.get("allocaStartDate"); // 配车日期开始
//		String allocaEndDate = (String)map.get("allocaEndDate"); // 配车日期结束
//		String poseId = (String)map.get("poseId");
//		/******************************页面查询字段end***************************/
//		List<Object> params = new LinkedList<Object>();		
//		StringBuffer sbSql= new StringBuffer();
//		sbSql.append("SELECT TSB.BO_ID,\n");
//		sbSql.append("       TSB.BO_NO,\n");
//		sbSql.append("       TD.DEALER_CODE,\n");
//		sbSql.append("       TD.DEALER_ID,\n");
//		sbSql.append("       TD.DEALER_NAME,\n");
//		sbSql.append("       TSA.ORDER_NO,\n");
//		sbSql.append("       TSA.ORDER_ID,\n");
//		sbSql.append("       TSA.DEALER_ID TSA_DEALER_ID, --订货经销商\n");
//		sbSql.append("       TSA.REC_DEALER_ID, --发运经销商\n");
//		sbSql.append("       TSA.ADDRESS_ID, --发运地址ID\n");
//		sbSql.append("       TVA.ADDRESS REC_DEALER_ADD, --发运详细地址\n");
//		sbSql.append("       TSA.INVOICE_NO,\n");
//		sbSql.append("       TSG.LOGI_ID,\n");
//		sbSql.append("       TSG.LOGI_NAME,\n");
//		sbSql.append("       TSA.AREA_ID,\n");
//		sbSql.append("       TBA.AREA_NAME,\n");
//		sbSql.append("       TO_CHAR(TSA.PLAN_CHK_DATE, 'YYYY-MM-DD') PLAN_CHK_DATE,\n");
//		sbSql.append("       TO_CHAR(TSA.FIN_CHK_DATE, 'YYYY-MM-DD') FIN_CHK_DATE,\n");
//		sbSql.append("       TO_CHAR(TSB.ALLOCA_DATE, 'YYYY-MM-DD') ALLOCA_DATE,\n");
//		sbSql.append("       TSA.ORDER_TYPE,\n");
//		sbSql.append("       NVL(TSA.ORDER_NUM, 0) ORDER_NUM,\n");
//		sbSql.append("       NVL(TSB.BO_NUM, 0) BO_NUM\n");
//		sbSql.append("  FROM TT_SALES_BOARD   TSB,\n");
//		sbSql.append("       TT_SALES_ASSIGN  TSA,\n");
//		sbSql.append("       TM_DEALER        TD,\n");
//		sbSql.append("       TM_VS_ADDRESS    TVA,\n");
//		sbSql.append("       TT_SALES_LOGI    TSG,\n");
//		sbSql.append("       TM_BUSINESS_AREA TBA\n");
//		sbSql.append(" WHERE TVA.ID = TSA.ADDRESS_ID\n");
//		sbSql.append("   AND TSB.HANDLE_STATUS = 9710005\n");
//		sbSql.append("   AND TSA.ASS_ID = TSB.ASS_ID\n");
//		sbSql.append("   AND TD.DEALER_ID = TSA.REC_DEALER_ID\n");
//		sbSql.append("   AND TSG.LOGI_ID = TSA.LOGI_ID\n");
//		sbSql.append("   AND TSA.AREA_ID = TBA.AREA_ID\n"); 
//		sbSql.append(mgDao.getPoseIdBusinessSql("TSA.AREA_ID",poseId));//车厂端查询列表产地数据权限
//	   if(yieldly!=null&&!"".equals(yieldly)){//产地
//		   sbSql.append("   AND TSA.AREA_ID ="+yieldly+"\n" );
//		}
//	
//		if (null != dealerCode && !"".equals(dealerCode))
//		{
//			String[] array = dealerCode.split(",");
//			sbSql.append("   AND TD.DEALER_CODE IN ( \n");
//			if (array.length > 0)
//			{
//				for (int i = 0; i < array.length; i++)
//				{
//					sbSql.append("'" + array[i] + "'");
//					if (i != array.length - 1)
//					{
//						sbSql.append(",");
//					}
//				}
//			}
//			else
//			{
//				sbSql.append("''");// 放空置，防止in里面报错
//			}
//			sbSql.append(")\n");
//			
//		}
//		if(orderType!=null&&!"".equals(orderType)){//订单类型
//			sbSql.append("   AND TSA.ORDER_TYPE = "+orderType+"\n" );
//		}
//		if(orderNo!=null&&!"".equals(orderNo)){//销售订单号
//			sbSql.append("   AND TSA.ORDER_NO  LIKE '%"+orderNo+"%'\n" );
//		}
//		if(invoiceNo!=null&&!"".equals(invoiceNo)){//发票号
//			sbSql.append("   AND TSA.INVOICE_NO LIKE '%"+invoiceNo+"%'\n" );
//		}
//		if(logiName!=null&&!"".equals(logiName)){//物流公司
//			sbSql.append("   AND TSG.LOGI_ID="+logiName+"\n" );
//		}
//		if(raiseStartDate!=null&&!"".equals(raiseStartDate)){//组板日期开始
//			sbSql.append("   AND TO_CHAR(TSB.BO_DATE,'YYYY-MM-DD')>='"+raiseStartDate+"'\n" );
//		}
//		if(raiseEndDate!=null&&!"".equals(raiseEndDate)){//组板日期结束
//			sbSql.append("   AND TO_CHAR(TSB.BO_DATE,'YYYY-MM-DD')<='"+raiseEndDate+"'\n" );
//		}
//		if(boNo!=null&&!"".equals(boNo)){//组板编号
//			sbSql.append("   AND TEMP2.BO_NO like'%"+boNo+"%'\n" );
//		}
//		if(allocaStartDate!=null&&!"".equals(allocaStartDate)){//配车日期开始
//			sbSql.append("   AND TO_CHAR(TSB.ALLOCA_DATE,'YYYY-MM-DD')>='"+allocaStartDate+"'\n" );
//		}
//		if(allocaEndDate!=null&&!"".equals(allocaEndDate)){//配车日期结束
//			sbSql.append("   AND TO_CHAR(TSB.ALLOCA_DATE,'YYYY-MM-DD')<='"+allocaEndDate+"'\n" );
//		}
//		PageResult<Map<String, Object>> ps= dao.pageQuery(sbSql.toString(), params, getFunName(),pageSize,curPage);
//		return ps;
//	}
	/********************************************************************************************************/
	/**
	 * 运单生成管理查询
	 * @param map
	 * @param curPage
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	public PageResult<Map<String, Object>> getWaybillManageQuery(Map<String, Object> map, int curPage, int pageSize)throws Exception{
		String raiseStartDate =(String)map.get("boStartDate"); // 组板日期开始
		String raiseEndDate = (String)map.get("boEndDate"); // 组板日期结束
		String boNo = (String)map.get("boNo"); //组板编号
		String allocaStartDate =(String)map.get("allocaStartDate"); // 配车日期开始
		String allocaEndDate = (String)map.get("allocaEndDate"); // 配车日期结束
		String yieldly = (String)map.get("yieldly"); // 产地	
		String logiName = (String)map.get("logiName"); // 物流公司
		String dealerCode = (String)map.get("dealerCode"); //经销商
		String orgCode =(String)map.get("orgCode"); // 区域
		String groupCode = (String)map.get("groupCode"); // 物料组
		String orderType = (String)map.get("orderType"); // 订单类型
		String orderNo = (String)map.get("orderNo"); // 订单编号
		String invoiceNo = (String)map.get("invoiceNo"); //发票号
		String poseId = (String)map.get("poseId");
		List<Object> params = new LinkedList<Object>();	
		String Str = "";
		if(orgCode!=null&&!"".equals(orgCode)){
			Str = "where T.REC_DEALER_ID in (select t.dealer_id from tm_dealer_org_relation t where t.org_id="+orgCode+")";
		}
		StringBuffer sbSql= new StringBuffer();
		sbSql.append("SELECT T.BO_ID,T.BO_NO,C.LOGI_NAME,C.ORDER_NO,C.INVOICE_NO,C.LOGI_ID,\n");
		sbSql.append("       A.NAME,\n");
		sbSql.append("       NVL(T.BO_NUM,0) BO_NUM,\n");
		sbSql.append("       NVL (T.ALLOCA_NUM, 0) ALLOCA_NUM,\n");
		sbSql.append("       TO_CHAR(T.BO_DATE,'YYYY-MM-DD HH24:MI:SS') BO_DATE,\n");
		sbSql.append("       TO_CHAR(ALLOCA_DATE,'YYYY-MM-DD HH24:MI:SS') ALLOCA_DATE, \n");
		sbSql.append("       C.FUEL_COEFFICIENT,");
		sbSql.append("       T.OUT_NUM \n");
		sbSql.append(" 		FROM TT_SALES_BOARD T,TC_USER A,\n");
		sbSql.append("		(select T.BO_ID,T.LOGI_ID,T.ORDER_NO,T.ORDER_TYPE,T.INVOICE_NO,T.REC_DEALER_ID,ts.fuel_coefficient,ti.LOGI_NAME \n");
		sbSql.append("		from TT_SALES_BO_DETAIL T left join TT_SALES_LOGI ti on t.LOGI_ID = ti.LOGI_ID left join  tm_vs_address m on  t.address_id = m.id \n");
		sbSql.append("		left join tm_region n on m.area_id = n.region_code left join TT_SALES_CITY_DIS ts on n.region_id = ts.city_id "+Str+" \n");
		sbSql.append("		GROUP BY T.BO_ID,T.LOGI_ID,T.ORDER_NO,T.REC_DEALER_ID,T.ORDER_TYPE,T.INVOICE_NO,ts.fuel_coefficient,ti.LOGI_NAME ) C\n");
		sbSql.append(" 		WHERE   T.BO_PER = A.USER_ID \n");
		sbSql.append(" 		AND   T.BO_ID = C.BO_ID\n");
		sbSql.append(" 		AND   T.HANDLE_STATUS = "+Constant.HANDLE_STATUS05+" \n");
		sbSql.append(" 		AND   NVL(T.OUT_NUM,0) > NVL(T.SEND_NUM,0) \n");
		sbSql.append(MaterialGroupManagerDao.getPoseIdBusinessSql("T.AREA_ID",poseId));
		sbSql.append(MaterialGroupManagerDao.getPoseIdLogiSql("C.LOGI_ID",poseId));//物流判断（是否是物流公司）
		if(yieldly!=null&&!"".equals(yieldly)){//产地
			   sbSql.append("   AND T.AREA_ID ="+yieldly+"\n" );
			}
		if(raiseStartDate!=null&&!"".equals(raiseStartDate)){//组板日期开始
			sbSql.append("   AND TO_CHAR(T.BO_DATE,'YYYY-MM-DD')>='"+raiseStartDate+"'\n" );
		}
		if(raiseEndDate!=null&&!"".equals(raiseEndDate)){//组板日期结束
			sbSql.append("   AND TO_CHAR(T.BO_DATE,'YYYY-MM-DD')<='"+raiseEndDate+"'\n" );
		}
		if(boNo!=null&&!"".equals(boNo)){//组板编号
			sbSql.append("   AND T.BO_NO like'%"+boNo+"%'\n" );
		}
		if(allocaStartDate!=null&&!"".equals(allocaStartDate)){//配车日期开始
			sbSql.append("   AND TO_CHAR(T.ALLOCA_DATE,'YYYY-MM-DD')>='"+allocaStartDate+"'\n" );
		}
		if(allocaEndDate!=null&&!"".equals(allocaEndDate)){//配车日期结束
			sbSql.append("   AND TO_CHAR(T.ALLOCA_DATE,'YYYY-MM-DD')<='"+allocaEndDate+"'\n" );
		}
		if(logiName!=null&&!"".equals(logiName)){//物流公司
			   sbSql.append("   AND C.LOGI_ID ="+logiName+"\n" );
		}
		if(dealerCode!=null&&!"".equals(dealerCode)){//经销商
			sbSql.append("   AND C.REC_DEALER_ID ='"+dealerCode+"'\n" );
		}
		if(groupCode!=null&&!"".equals(groupCode)){//物料组
			sbSql.append("   AND C.MAT_ID ='"+groupCode+"'\n" );
		}
		if(orderType!=null&&!"".equals(orderType)){// 订单类型
			sbSql.append("   AND C.ORDER_TYPE  ='"+orderType+"'\n" );
		}
		if(orderNo!=null&&!"".equals(orderNo)){//订单编号
			sbSql.append("   AND C.ORDER_NO like '%"+orderNo+"%'\n" );
		}
		if(invoiceNo!=null&&!"".equals(invoiceNo)){//发票号
			sbSql.append("   AND C.INVOICE_NO like '%"+invoiceNo+"%'\n" );
		}
	
		sbSql.append("		 ORDER BY BO_ID DESC\n"); 

		PageResult<Map<String, Object>> ps= dao.pageQuery(sbSql.toString(), params, getFunName(),pageSize,curPage);
		return ps;
	}
	
	/**
	 * 组板物料列表查询
	 * @param params 查询参数LIST
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> getSendBoardMatListQuery(String boid)throws Exception{
		StringBuffer sbSql = new StringBuffer();
		sbSql.append("SELECT TSBD.BO_DE_ID, --组板详细表ID\n");
		sbSql.append("       TSB.BO_ID,\n");
		sbSql.append("       TSB.BO_NO, --组板号\n");
		sbSql.append("       TSB.CAR_NO, --承运车牌号\n");
		sbSql.append("       TSB.LOADS, --装车道次\n");
		sbSql.append("       TSB.CAR_TEAM, --领票车队\n");
		sbSql.append("       TSB.DRIVER_NAME, --驾驶员姓名\n");
		sbSql.append("       TSB.DRIVER_TEL, --驾驶员电话\n");
		sbSql.append("       VMGM.SERIES_NAME, --车系名称\n");
		sbSql.append("       VMGM.MODEL_NAME, --车型名称\n");
		sbSql.append("       VMGM.PACKAGE_NAME, --配置名称\n");
		sbSql.append("       TSBD.ORDER_TYPE, --订单类型\n");
		sbSql.append("       TSBD.DEALER_ID, --收货方ID\n");
		sbSql.append("       TSBD.REC_DEALER_ID, --订货方ID\n");
		sbSql.append("       TD.DEALER_CODE,\n");
		sbSql.append("       TD.DEALER_NAME,\n");
		sbSql.append("       TSBD.LOGI_ID, --物流商ID\n");
		sbSql.append("       TSBD.ADDRESS_ID, --发运地址ID\n");
		sbSql.append("       TSBD.INVOICE_NO, --发票号\n");
		sbSql.append("       TSBD.ORDER_NO, --订单号\n");
		sbSql.append("       TSBD.AREA_ID, --产地\n");
		sbSql.append("       VMGM.MATERIAL_ID, --物料ID\n");
		sbSql.append("       VMGM.MATERIAL_CODE, --物料CODE\n");
		sbSql.append("       VMGM.MATERIAL_NAME, --物料NAME\n");
		sbSql.append("       VMGM.COLOR_NAME, --颜色\n");
		sbSql.append("       NVL(TSBD.INVOICE_NUM, 0) CHECK_AMOUNT, --开票数量\n");
		sbSql.append("       NVL(TSBD.BOARD_NUM, 0) THIS_BOARD_NUM, --当前组板数量\n");
		sbSql.append("       NVL(TSBD.ALLOCA_NUM, 0) ALLOCA_NUM, --配车数量\n");
		//sbSql.append("      tt.fuel_coefficient, --燃油费调节系数\n");
		sbSql.append("       B.REGION_NAME||'-'||C.REGION_NAME||'-'||D.REGION_NAME DEALER_ADD\n");
		sbSql.append("  from TT_SALES_BO_DETAIL    TSBD,\n");
		sbSql.append("       VW_MATERIAL_GROUP_MAT VMGM,\n");
		sbSql.append("       TT_SALES_BOARD        TSB,\n");
		sbSql.append("       TM_DEALER             TD,\n");
		sbSql.append("       TM_VS_ADDRESS         A ,\n");
		//inner join  TT_SALES_CITY_DIS tt on A.city_id = tt.city_id 
		sbSql.append("       TM_REGION             B,\n");
		sbSql.append("       TM_REGION             C,\n");
		sbSql.append("       TM_REGION             D\n");
		sbSql.append("\n");
		sbSql.append(" WHERE TSBD.MAT_ID = VMGM.MATERIAL_ID(+)\n");
		sbSql.append("   AND TSB.BO_ID = TSBD.BO_ID\n");
		sbSql.append("   AND TSBD.REC_DEALER_ID = TD.DEALER_ID\n");
		sbSql.append("   AND TSBD.ADDRESS_ID=A.ID(+)\n");
		sbSql.append("   AND A.PROVINCE_ID=B.REGION_CODE(+)\n");
		sbSql.append("   AND A.CITY_ID=C.REGION_CODE(+)\n");
		sbSql.append("   AND A.AREA_ID=D.REGION_CODE(+)\n"); 
		sbSql.append("   AND TSBD.BO_ID IN ("+boid+")\n");
		sbSql.append(" order by TSBD.ORDER_NO, VMGM.SERIES_NAME, VMGM.MODEL_NAME, VMGM.PACKAGE_NAME, VMGM.COLOR_NAME\n");
		List<Map<String, Object>> list= dao.pageQuery(sbSql.toString(), null, getFunName());
		return list;
	}
//	public List<Map<String, Object>> getSendBoardMatListQuery(String boid)throws Exception{
//		StringBuffer sbSql= new StringBuffer();
//		sbSql.append("SELECT TSBD.BO_DE_ID, --组板详细表ID\n");
//		sbSql.append("       TSBD.OR_DE_ID, --订单明细ID\n");
//		sbSql.append("       TSB.BO_ID,\n");
//		sbSql.append("       TSB.BO_NO, --组板号\n");
//		sbSql.append("       TSBD.CAR_NO, --承运车牌号\n");
//		sbSql.append("       TSBD.LOADS, --装车道次\n");
//		sbSql.append("       TSBD.CAR_TEAM, --领票车队\n");
//		sbSql.append("       VMGM.SERIES_NAME, --车系名称\n");
//		sbSql.append("       TSA.ORDER_TYPE, --订单类型\n");
//		sbSql.append("       TSA.REC_DEALER_ID, --收货方ID\n");
//		sbSql.append("       TSA.DEALER_ID, --订货方ID\n");
//		sbSql.append("       TSA.LOGI_ID, --物流商ID\n");
//		sbSql.append("       TSA.ADDRESS_ID, --发运地址ID\n");
//		sbSql.append("       TSA.ORDER_NO, --销售订单号\n");
//		sbSql.append("       TSA.INVOICE_NO, --发票号\n");
//		sbSql.append("       TSA.ORDER_ID, --订单号\n");
//		sbSql.append("       TSA.AREA_ID, --产地\n");
//		sbSql.append("       VMGM.MATERIAL_ID, --物料ID\n");
//		sbSql.append("       VMGM.MATERIAL_CODE, --物料CODE\n");
//		sbSql.append("       VMGM.MATERIAL_NAME, --物料NAME\n");
//		sbSql.append("       VMGM.COLOR_NAME, --颜色\n");
//		sbSql.append("       NVL(TVOD.CHECK_AMOUNT, 0) CHECK_AMOUNT, --开票数量\n");
//		sbSql.append("       NVL(TVOD.BOARD_NUMBER, 0) BOARD_NUM, --已组板数量\n");
//		sbSql.append("       NVL(TSBD.BOARD_NUM, 0) THIS_BOARD_NUM, --当前组板数量\n");
//		sbSql.append("       NVL(TSBD.ALLOCA_NUM, 0) ALLOCA_NUM --配车数量\n");
//		sbSql.append("  from TT_SALES_ASSIGN       TSA,\n");
//		sbSql.append("       TT_VS_ORDER_DETAIL    TVOD,\n");
//		sbSql.append("       VW_MATERIAL_GROUP_MAT VMGM,\n");
//		sbSql.append("       TT_SALES_BOARD        TSB,\n");
//		sbSql.append("       TT_SALES_BO_DETAIL    TSBD\n");
//		sbSql.append(" WHERE TVOD.MATERIAL_ID = VMGM.MATERIAL_ID\n");
//		sbSql.append("   AND TSA.ORDER_ID = TVOD.ORDER_ID\n");
//		sbSql.append("   AND TSB.BO_ID = TSBD.BO_ID\n");
//		sbSql.append("   AND TSBD.OR_DE_ID = TVOD.DETAIL_ID\n");
//		if (null != boid && !"".equals(boid))
//		{
//			String[] array =boid.split(",");
//			sbSql.append("  AND TSB.BO_ID IN ( \n");
//			if (array.length > 0)
//			{
//				for (int i = 0; i < array.length; i++)
//				{
//					sbSql.append("'" + array[i] + "'");
//					if (i != array.length - 1)
//					{
//						sbSql.append(",");
//					}
//				}
//			}
//			else
//			{
//				sbSql.append("''");// 放空置，防止in里面报错
//			}
//			sbSql.append(")\n");
//			
//		}
//		List<Map<String, Object>> list= dao.pageQuery(sbSql.toString(), null, getFunName());
//		return list;
//	}
	
	/**
	 * 根据物流商和订货是哪个分组查询
	 */
	public List<Map<String, Object>> getInfoForWayBill(String boid){
		StringBuffer sbSql = new StringBuffer();
		sbSql.append("SELECT     TSBD.DEALER_ID,TSBD.REC_DEALER_ID,\n");
		sbSql.append("           TSBD.ADDRESS_ID,\n");
		sbSql.append("           TO_CHAR(COUNT(TSAD.VEHICLE_ID)) VEHICLE_NUM,\n");
		sbSql.append("            TO_CHAR(COUNT(TSBD.Board_Num)) BO_NUM,\n");
		sbSql.append("           TSBD.AREA_ID,\n");
		sbSql.append("           TSBD.LOGI_ID\n");
		sbSql.append("  FROM     TT_SALES_BO_DETAIL TSBD,\n");
		sbSql.append("           TT_SALES_ALLOCA_DE TSAD\n");
		sbSql.append(" WHERE     TSBD.BO_DE_ID = TSAD.BO_DE_ID\n");
		sbSql.append("   AND     TSBD.BO_ID IN ("+boid+")\n");
		sbSql.append("   AND     TSAD.STATUS="+Constant.STATUS_ENABLE+"");
		sbSql.append(" GROUP BY  TSBD.DEALER_ID,TSBD.REC_DEALER_ID,\n");
		sbSql.append("           TSBD.ADDRESS_ID,\n");
		sbSql.append("           TSBD.AREA_ID,\n");
		sbSql.append("           TSBD.LOGI_ID");  
		return dao.pageQuery(sbSql.toString(), null, getFunName());
	}
	
	/**
	 * 查询配车明细
	 * @author liufazhong
	 */
	public List<Map<String, Object>> queryAllocaDetail(String billId){
		StringBuffer sql = new StringBuffer("SELECT B.* FROM TT_SALES_BO_DETAIL A,TT_SALES_ALLOCA_DE B" +
				" WHERE A.BO_DE_ID=B.BO_DE_ID AND B.STATUS="+Constant.STATUS_ENABLE+" AND A.BILL_ID="+billId+"");
		return dao.pageQuery(sql.toString(), null, getFunName());
	}
	 
	/**
	 * 修改车辆表生命周期和锁定状态
	 * @param boId
	 * @param userId
	 */
	public void updateVehicleStatus(Long boId,Long userId){//
		StringBuffer sbSql = new StringBuffer();
		sbSql.append("UPDATE TM_VEHICLE TV\n");
		sbSql.append("   SET TV.LIFE_CYCLE  = ?,\n");
		sbSql.append("       TV.LOCK_STATUS = ?,\n");
		sbSql.append("       TV.UPDATE_BY   = ?,\n");
		sbSql.append("       TV.UPDATE_DATE = SYSDATE\n");
		sbSql.append(" WHERE EXISTS (SELECT TSAD.VEHICLE_ID\n");
		sbSql.append("          FROM TT_SALES_BOARD     TSS,\n");
		sbSql.append("               TT_SALES_BO_DETAIL TSBD,\n");
		sbSql.append("               TT_SALES_ALLOCA_DE TSAD\n");
		sbSql.append("         WHERE TSS.BO_ID = TSBD.BO_ID\n");
		sbSql.append("           AND TSBD.BO_DE_ID = TSAD.BO_DE_ID\n");
		sbSql.append("           AND TV.VEHICLE_ID = TSAD.VEHICLE_ID\n");
		sbSql.append("           AND TSAD.STATUS=?\n");
		sbSql.append("           AND TSS.BO_ID = ?)"); 
		List<Object> list6=new ArrayList<Object>();
		list6.add(Constant.VEHICLE_LIFE_09);//已生成运单状态 （以前是经销商在途）
		list6.add(Constant.LOCK_STATUS_01);
		list6.add(userId);
		list6.add(Constant.STATUS_ENABLE);
		list6.add(boId);
		factory.update(sbSql.toString(), list6);//修改车辆表生命周期和锁定状态
	}
	/**
	 * 修改车辆表生命周期和锁定状态(Zt单)
	 * @param boId
	 * @param userId
	 */
	public void updateVehicleStatusZT(Long boId,Long userId){//
		StringBuffer sbSql = new StringBuffer();
		sbSql.append("UPDATE TM_VEHICLE TV\n");
		sbSql.append("   SET TV.LIFE_CYCLE  = ?,\n");
		sbSql.append("       TV.LOCK_STATUS = ?,\n");
		sbSql.append("       TV.UPDATE_BY   = ?,\n");
		sbSql.append("       TV.UPDATE_DATE = SYSDATE\n");
		sbSql.append(" WHERE EXISTS (SELECT TSAD.VEHICLE_ID\n");
		sbSql.append("          FROM TT_SALES_BOARD     TSS,\n");
		sbSql.append("               TT_SALES_BO_DETAIL TSBD,\n");
		sbSql.append("               TT_SALES_ALLOCA_DE TSAD\n");
		sbSql.append("         WHERE TSS.BO_ID = TSBD.BO_ID\n");
		sbSql.append("           AND TSBD.BO_DE_ID = TSAD.BO_DE_ID\n");
		sbSql.append("           AND TV.VEHICLE_ID = TSAD.VEHICLE_ID\n");
		sbSql.append("           AND TSS.BO_ID = ?)"); 
		List<Object> list6=new ArrayList<Object>();
		list6.add(Constant.VEHICLE_LIFE_05);//以前是经销商在途
		list6.add(Constant.LOCK_STATUS_01);
		list6.add(userId);
		list6.add(boId);
		factory.update(sbSql.toString(), list6);//修改车辆表生命周期和锁定状态
	}
	
	
	/**查询所需要录入订单的运单信息
	 * @param invoiceFlag */
	public PageResult<Map<String, Object>> getWayBillInList(String billNo,
			String vin,String vehcleId, String dearlerId,String invoiceFlag, Integer curPage, Integer pageSize) {
		StringBuffer sql = new StringBuffer();
		List<Object> parasList = new ArrayList<Object>();
		sql.append("SELECT  veh.vehicle_id,   area.area_name, veh.vin,veh.invoice_no, \n");
		sql.append("       vwmet.SERIES_NAME, vwmet.MATERIAL_NAME,   vwmet.MODEL_NAME,vwmet.PACKAGE_NAME,vwmet.MATERIAL_CODE,\n");
		sql.append("	   vwmet.COLOR_NAME, det.color_code,bill.bill_no,\n");
		sql.append("	   bill.bill_crt_per,bill.bo_num, bill.BILL_CRT_DATE \n");
		sql.append(" FROM  tt_sales_bo_detail det,tt_sales_waybill   bill,tt_sales_alloca_de alloca, \n");
		sql.append("       tm_vehicle         veh,  vw_material_group_mat vwmet ,tm_business_area  area \n");
		sql.append(" WHERE det.bill_id = bill.bill_id \n");
		sql.append("   AND  det.bo_de_id = alloca.bo_de_id \n");
		sql.append("   AND alloca.vehicle_id = veh.vehicle_id \n");
		sql.append("   AND  area.area_id = bill.area_id \n");
		sql.append("   AND veh.Material_Id = vwmet.MATERIAL_ID \n");
		sql.append("   AND veh.LIFE_CYCLE in (?,?) \n");
		
		parasList.add(Constant.VEHICLE_LIFE_08);
		parasList.add(Constant.VEHICLE_LIFE_09);
		if(Utility.testString(dearlerId)){
			sql.append("   AND bill.send_dealer_id = ? ");
			parasList.add(dearlerId);
			
		}
		
		if(Utility.testString(billNo)){
			sql.append("  AND bill.bill_no like '%"+billNo+"%");
		}
		
		if(Utility.testString(vin)){
			sql.append("  AND veh.vin  like '%"+vin+"%");
		}
		
		if(Utility.testString(vehcleId)){
			sql.append("  AND veh.vehicle_id = ?");
			parasList.add(vehcleId);
		}
		
		if(Utility.testString(invoiceFlag)){
			if("yes".equals(invoiceFlag)){
				sql.append("  AND veh.invoice_no is not null");
			}else if("no".equals(invoiceFlag)){
				sql.append("  AND veh.invoice_no is null");
			}
		}
		return dao.pageQuery(sql.toString(), parasList, getFunName(),pageSize,curPage);
	}
	
	//保存发票号到车辆信息表
	public void updateInvoiceNo(String vehcleId,String invoiceNo, AclUserBean logonUser) {
		
		StringBuffer sql = new StringBuffer();
		sql.append(" Update tm_vehicle vehicle \n");
		sql.append("  Set vehicle.invoice_no = ?  , vehicle.UPDATE_BY =? ,vehicle.UPDATE_DATE = ?  \n");
		sql.append(" Where vehicle.VEHICLE_ID = ?");
		
		List<Object> params = new ArrayList<Object>();
		params.add(invoiceNo);
		params.add(logonUser.getUserId());
		params.add(new Date());
		params.add(vehcleId);
		dao.update(sql.toString(), params);
		
	}
	public List<Map<String, Object>> getOrderIdsByBoId(String boId) {
		StringBuffer sql= new StringBuffer();
		sql.append("select t.order_id\n" );
		sql.append("  from tt_sales_bo_detail t\n" );
		sql.append(" where 1= 1\n" );
		sql.append("   and t.bo_id = ?\n" );
		sql.append("   group by t.order_id");
		List<Object> params = new ArrayList<Object>();
		params.add(boId);
		return pageQuery(sql.toString(), params, getFunName());
	}
	
	
}

