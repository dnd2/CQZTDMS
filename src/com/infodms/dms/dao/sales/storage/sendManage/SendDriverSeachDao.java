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
 * 发运司机调整DAO
 * @author shuyh
 * 2017/8/1
 */
public class SendDriverSeachDao extends BaseDao<PO>{
	private static final SendDriverSeachDao dao = new SendDriverSeachDao ();
	public static final SendDriverSeachDao getInstance() {
		return dao;
	}
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		return null;
	}
	/**
	 * 发运司机调整查询
	 * @param map
	 * @param curPage
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	public PageResult<Map<String, Object>> getSendDriverSeachQuery(Map<String, Object> map, int curPage, int pageSize)throws Exception{
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
		String driverName = CommonUtils.checkNull((String)map.get("driverName")); //司机姓名
		String driverTel = CommonUtils.checkNull((String)map.get("driverTel"));//司机电话
		
		String posBusType=(String)map.get("posBusType");
		String logiIdU=String.valueOf(map.get("logiId"));//当前职位所对应承运商ID
		/******************************页面查询字段end***************************/
		List<Object> params = new LinkedList<Object>();
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT TSB.BO_ID,\n" );
		sql.append("       TSB.BO_NO,\n" );
		sql.append("       NVL(TSB.BO_NUM, 0) BO_NUM,\n" );
		sql.append("       TSB.DRIVER_NAME,\n" );
		sql.append("       TSB.DRIVER_TEL,\n" );
		sql.append("       TSB.CAR_TEAM,\n" );
		sql.append("       TSB.CAR_NO,\n" );
		sql.append("       TSB.LOADS,\n" );
		sql.append("       TO_CHAR(TSB.BO_DATE, 'yyyy-mm-dd') BO_DATE\n" );
		sql.append("  FROM TT_SALES_BOARD TSB\n" );
		sql.append(" WHERE TSB.BO_STATUS = '1'\n");
		sql.append("	AND TSB.BO_ID NOT IN\n" );//过滤掉已生成交接单的数据
		sql.append("       (SELECT TSBD.BO_ID\n" );
		sql.append("          FROM Tt_Sales_Bo_Detail TSBD, TT_SALES_WAY_BILL_DTL TSW\n" );
		sql.append("         WHERE TSBD.OR_DE_ID = TSW.ORDER_DETAIL_ID\n" );
		sql.append("           AND TSBD.MAT_ID = TSW.MAT_ID)");

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
		if(driverName!=null&&!"".equals(driverName)){
			sql.append(" AND TSB.DRIVER_NAME LIKE ?\n");		
			params.add("%"+driverName+"%");
		}
		if(driverTel!=null&&!"".equals(driverTel)){
			sql.append(" AND TSB.DRIVER_TEL LIKE ?\n");
			params.add("%"+driverTel+"%");
		}
		sql.append(" order by tsb.bo_date desc");
		Object[] arr=new Object[2];
		arr[0]=sql;
		arr[1]=params;
		return arr;
	}
	/**
	 * 司机车辆绑定列表查询
	 * @param map
	 * @param curPage
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	public PageResult<Map<String, Object>> getDriverVehSeachQuery(Map<String, Object> map, int curPage, int pageSize)throws Exception{
		/******************************页面查询字段start**************************/
		String billNo = CommonUtils.checkNull(map.get("billNo")); //交接单号
		String orderNo = CommonUtils.checkNull(map.get("orderNo")); //订单号
		String boNo = CommonUtils.checkNull(map.get("boNo")); //组板号
		String driverName = CommonUtils.checkNull(map.get("driverName")); //驾驶员姓名
		String driverTel = CommonUtils.checkNull(map.get("driverTel")); // 驾驶员电话
		String startDate = CommonUtils.checkNull(map.get("startDate")); //绑定日期开始
		String endDate = CommonUtils.checkNull(map.get("endDate")); // 绑定日期结束
		
		String posBusType=(String)map.get("posBusType");
		String logiIdU=String.valueOf(map.get("logiId"));//当前职位所对应承运商ID
		/******************************页面查询字段end***************************/
		List<Object> params = new LinkedList<Object>();
		
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT TSWD.DTL_ID,\n" );
		sql.append("                TSW.BILL_ID,\n" );
		sql.append("                TSW.BILL_NO,\n" );
		sql.append("                TSWD.ORDER_NO,\n" );
		sql.append("                TSB.BO_NO,\n" );
		sql.append("                TSB.DRIVER_NAME,\n" );
		sql.append("                TSB.DRIVER_TEL,\n" );
		sql.append("                MAT.SERIES_NAME,\n" );
		sql.append("                MAT.MODEL_NAME,\n" );
		sql.append("                MAT.PACKAGE_NAME,\n" );
		sql.append("                MAT.COLOR_NAME,\n" );
		sql.append("                MAT.MATERIAL_CODE,\n" );
		sql.append("                TSWD.VIN\n" );
		sql.append("  FROM TT_SALES_WAY_BILL_DTL TSWD,\n" );
		sql.append("       TT_SALES_WAYBILL      TSW,\n" );
		sql.append("       TT_SALES_BO_DETAIL    TSBD,\n" );
		sql.append("       TT_SALES_BOARD        TSB,\n" );
		sql.append("       VW_MATERIAL_GROUP_MAT MAT\n" );
		sql.append(" WHERE 1 = 1\n" );
		sql.append("   AND TSW.BILL_ID = TSWD.BILL_ID\n" );
		sql.append("   AND TSWD.BILL_ID = TSBD.BILL_ID\n" );
		sql.append("   AND TSBD.BO_ID = TSB.BO_ID\n" );
		sql.append("   AND TSWD.MAT_ID = MAT.MATERIAL_ID\n" );
		sql.append("   AND TSWD.ORDER_DETAIL_ID=TSBD.OR_DE_ID\n" );
		sql.append("   AND TSWD.DRIVER_USER_ID IS NULL\n" );//未绑定
		sql.append("   AND (TSWD.STATUS = "+Constant.WAYBILL_DTL_STATUS_01+" OR TSWD.STATUS IS NULL)\n" );//未绑定
		if(posBusType.equals(String.valueOf(Constant.POSE_BUS_TYPE_WL))){//物流商
			sql.append("   AND TSW.LOGI_ID= ?\n" );
			params.add(logiIdU);
		}
		if(billNo!=null&&!"".equals(billNo)){
			sql.append("AND TSW.BILL_NO LIKE ?\n");		
			params.add("%"+billNo+"%");
		}
		if(orderNo!=null&&!"".equals(orderNo)){
			sql.append("AND TSWD.ORDER_NO LIKE ?\n");		
			params.add("%"+orderNo+"%");
		}
		if(boNo!=null&&!"".equals(boNo)){
			sql.append("AND TSB.BO_NO LIKE ?\n");		
			params.add("%"+boNo+"%");
		}
		if(driverName!=null&&!"".equals(driverName)){
			sql.append(" AND TSB.DRIVER_NAME LIKE ?\n");		
			params.add("%"+driverName+"%");
		}
		if(driverTel!=null&&!"".equals(driverTel)){
			sql.append(" AND TSB.DRIVER_TEL LIKE ?\n");
			params.add("%"+driverTel+"%");
		}
		if(startDate!=null&&!"".equals(startDate)){//绑定日期开始过滤
			sql.append("   AND TSWD.DRIVER_BIND_DATE>=TO_DATE(?,'YYYY-MM-DD HH24:MI:SS')\n" );
			params.add(startDate+" 00:00:00");
		}
		if(endDate!=null&&!"".equals(endDate)){//绑定日期结束过滤
			sql.append("  AND TSWD.DRIVER_BIND_DATE<=TO_DATE(?,'YYYY-MM-DD HH24:MI:SS')\n" );
			params.add(endDate +" 23:59:59");
		}
		sql.append("ORDER BY TSWD.BILL_ID, TSWD.ORDER_NO\n");
		PageResult<Map<String, Object>> ps= dao.pageQuery(sql.toString(), params, getFunName(),pageSize,curPage);
		return ps;
	}
	/**
	 * 根据职位获取司机列表
	 * @param poseId
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> getDriverListByPose(String poseId)throws Exception{
		List<Object> params = new LinkedList<Object>();
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT TU.USER_ID, TU.ACNT, TU.NAME,TU.HAND_PHONE\n" );
		sql.append("  FROM TR_USER_POSE TUP, TC_POSE TP, TC_USER TU\n" );
		sql.append(" WHERE TUP.POSE_ID = TP.POSE_ID\n" );
		sql.append("   AND TUP.USER_ID = TU.USER_ID\n" );
		sql.append("   AND TU.USER_STATUS = 10011001\n" );//有效
		sql.append("   AND TU.USER_TYPE = 10021001\n" );//主机厂
		sql.append("   AND TU.DRIVER_FLAG='1'\n" );//司机标识
		sql.append("   AND TP.POSE_ID = "+poseId+"\n");
		List<Map<String, Object>> list= dao.pageQuery(sql.toString(), null, getFunName());
		return list;
	}
	/**
	 * 判断司机所属承运商和组板承运商是否相同
	 * @param userId
	 * @param dtlId
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> getIsMatchLogi(String userId,String dtlId)throws Exception{
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT TSWB.DTL_ID\n" );
		sql.append("     FROM TT_SALES_WAY_BILL_DTL TSWB, TT_SALES_WAYBILL TSW\n" );
		sql.append("    WHERE TSWB.BILL_ID = TSW.BILL_ID\n" );
		sql.append("      AND TSWB.DTL_ID = "+dtlId+"\n" );
		sql.append("      AND TSW.LOGI_ID IN\n" );
		sql.append("          (SELECT TP.LOGI_ID\n" );
		sql.append("             FROM TR_USER_POSE TUP, TC_POSE TP\n" );
		sql.append("            WHERE TUP.POSE_ID = TP.POSE_ID\n" );
		sql.append("              AND TUP.USER_ID = "+userId+")");
		List<Map<String, Object>> list= dao.pageQuery(sql.toString(), null, getFunName());
		return list;
	}
}
