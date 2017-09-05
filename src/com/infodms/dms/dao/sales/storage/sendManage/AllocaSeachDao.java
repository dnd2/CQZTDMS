package com.infodms.dms.dao.sales.storage.sendManage;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.infodms.dms.common.Constant;
import com.infodms.dms.common.materialManager.MaterialGroupManagerDao;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

/**
 * 
 * @ClassName     : SendAssignmentDao 
 * @Description   : 配车查询DAO 
 * @author        : ranjian
 * CreateDate     : 2013-4-26
 */
public class AllocaSeachDao  extends BaseDao<PO>{
	private static final AllocaSeachDao dao = new AllocaSeachDao ();
	public static final AllocaSeachDao getInstance() {
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
	public List<Map<String, Object>> getAllocaSeachQueryExport(Map<String, Object> map){
		Object[] obj=getSQL(map);
		List<Map<String, Object>> list= dao.pageQuery(obj[0].toString(), (List<Object>)obj[1], getFunName());
		return list;
	}
	/**
	 * 查询统计
	 *param map 查询参数
	 *@return 
	 */
	public Map<String,Object> tgSum(Map<String, Object> map){
		Object[] obj=getSQL(map);
		StringBuffer sbSql=new StringBuffer();
		sbSql.append("SELECT SUM(NVL(BO_NUM, 0)) BO_NUM,\n");
		sbSql.append("       SUM(NVL(ALLOCA_NUM, 0)) ALLOCA_NUM,\n");
		sbSql.append("       SUM(NVL(SEND_NUM, 0)) SEND_NUM,\n");
		sbSql.append("       SUM(NVL(ACC_NUM, 0)) ACC_NUM,\n");
		sbSql.append("       SUM(NVL(OUT_NUM, 0)) OUT_NUM FROM (\n");
		sbSql.append(obj[0].toString());
		sbSql.append(")\n");
		Map<String, Object> mapR= dao.pageQueryMap(sbSql.toString(), (List<Object>)obj[1], getFunName());
		return mapR;
	}
	/**
	 * 配车信息查询
	 * @param map
	 * @param curPage
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	public PageResult<Map<String, Object>> getAllocaSeachQuery(Map<String, Object> map, int curPage, int pageSize)throws Exception{
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
		String handleStatus = CommonUtils.checkNull((String)map.get("handleStatus")); //状态
		String poseId = CommonUtils.checkNull((String)map.get("poseId"));
		String vin = CommonUtils.checkNull((String)map.get("VIN")); //VIN
		String transportType = CommonUtils.checkNull((String)map.get("transportType"));//发运方式
		String yieldly = CommonUtils.checkNull((String)map.get("yieldly")); //产地
		/******************************页面查询字段end***************************/
		List<Object> params = new LinkedList<Object>();
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
		sbSql.append("		   (SELECT T.SEND_TYPE FROM TT_SALES_BO_DETAIL T WHERE  T.BO_ID=A.BO_ID AND ROWNUM=1) SEND_TYPE,\n"); 
		sbSql.append("         NVL (A.HANDLE_STATUS, 0) HANDLE_STATUS,\n");//处理状态
		sbSql.append("		   (SELECT T2.DEALER_SHORTNAME FROM TT_SALES_BO_DETAIL T1,TM_DEALER T2 WHERE T1.REC_DEALER_ID=T2.DEALER_ID AND T1.IS_ENABLE=10041001 AND ROWNUM=1 AND T1.BO_ID=A.BO_ID GROUP BY BO_ID,REC_DEALER_ID,T2.DEALER_SHORTNAME) DEALER_SHORTNAME");
		sbSql.append("  FROM   TT_SALES_BOARD A, TC_USER B\n");
		sbSql.append(" WHERE   A.BO_PER = B.USER_ID\n");
		sbSql.append(" AND A.IS_ENABLE=").append(Constant.IF_TYPE_YES).append("\n"); //有效的
		sbSql.append(MaterialGroupManagerDao.getPoseIdBusinessSql("A.AREA_ID",poseId));//车厂端查询列表产地数据权限
		if(!raiseStartDate.equals("")){
			sbSql.append("AND A.BO_DATE>=TO_DATE('"+raiseStartDate+" 00:00:00','YYYY-MM-DD HH24:MI:SS')\n");
		}
		if(!raiseEndDate.equals("")){
			sbSql.append("AND A.BO_DATE<=TO_DATE('"+raiseEndDate+" 23:59:59','YYYY-MM-DD HH24:MI:SS')\n");
		}
		if(!"".equals(boNo)){
			sbSql.append("AND A.BO_NO LIKE '%"+boNo+"%'");
		}
		if(!"".equals(handleStatus)){
			sbSql.append("AND A.HANDLE_STATUS ="+handleStatus);
		}
		if(!"".equals(transportType)){
			sbSql.append("AND EXISTS(SELECT T.SEND_TYPE FROM TT_SALES_BO_DETAIL T WHERE  T.BO_ID=A.BO_ID AND ROWNUM=1 AND T.SEND_TYPE="+transportType).append(")\n");
		}
		if(!"".equals(vin)){
			sbSql.append(MaterialGroupManagerDao.vinSqlLike("A.BO_ID",vin)); 
		}
		if(!"".equals(yieldly)){
			sbSql.append("AND A.AREA_ID=?\n");
			params.add(Long.valueOf(yieldly));
		}
		Object[] arr=new Object[2];
		arr[0]=sbSql;
		arr[1]=params;
		return arr;
	}
	/**
	 * 打印基本信息
	 * @param boId 组板ID
	 * @return  组板信息
	 */
	public Map<String,Object> allocaPrint(Long boId){
		StringBuffer sbSql=new StringBuffer();
		sbSql.append("SELECT A.BO_NO\n");
		sbSql.append("  FROM TT_SALES_BOARD A WHERE\n");
		sbSql.append(" A.BO_ID = ?"); 
		List<Object> params=new ArrayList<Object>();
		params.add(boId);
		return dao.pageQueryMap(sbSql.toString(), params, getFunName());
	}
	/**
	 * 打印基本信息
	 * @param map
	 * @param curPage
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	public List<Map<String,Object>> allocaPrintMain(Long boId){
		StringBuffer sbSql=new StringBuffer();
		sbSql.append("SELECT D.SD_NUMBER,\n");
		sbSql.append("       B.INVOICE_NO,\n");
		sbSql.append("       B.ORDER_NO,\n");
		sbSql.append("       B.ORDER_TYPE,\n");
		sbSql.append("       D.VIN,\n");
		sbSql.append("       D.ENGINE_NO,\n");
		sbSql.append("       F.MODEL_CODE, F.MODEL_NAME, F.PACKAGE_NAME,F.COLOR_NAME,\n");
		sbSql.append("       D.COLOR,\n");
		sbSql.append("       D.SIT_CODE,\n"); 
		sbSql.append("       A.LOADS,\n");
		sbSql.append("       E.DEALER_ID,\n");
		sbSql.append("       E.DEALER_SHORTNAME,\n");
		sbSql.append("       E.DEALER_NAME\n");
		sbSql.append("  FROM TT_SALES_BOARD        A,\n");
		sbSql.append("       TT_SALES_BO_DETAIL    B,\n");
		sbSql.append("       TT_SALES_ALLOCA_DE    C,\n");
		sbSql.append("       TM_VEHICLE            D,\n");
		sbSql.append("       TM_DEALER             E,\n");
		sbSql.append("       VW_MATERIAL_GROUP_MAT F,\n");
		sbSql.append("       TT_SALES_SIT          H,\n");
		sbSql.append("       TT_SALES_ROAD         I,\n");
		sbSql.append("       TT_SALES_AREA         J\n");
		sbSql.append(" WHERE A.BO_ID = B.BO_ID\n");
		sbSql.append("   AND B.BO_DE_ID = C.BO_DE_ID\n");
		sbSql.append("   AND C.VEHICLE_ID = D.VEHICLE_ID\n");
		sbSql.append("   AND B.REC_DEALER_ID = E.DEALER_ID\n");
		sbSql.append("   AND D.MATERIAL_ID = F.MATERIAL_ID\n");
		sbSql.append("   AND D.SIT_ID = H.SIT_ID\n");
		sbSql.append("   AND H.ROAD_ID = I.ROAD_ID AND I.AREA_ID = J.AREA_ID\n");
		sbSql.append("   AND B.IS_ENABLE = ?\n");
		sbSql.append("   AND A.BO_ID = ?\n");
		sbSql.append(" ORDER BY TO_NUMBER(J.AREA_NAME),\n");
		sbSql.append("          TO_NUMBER(I.ROAD_NAME),\n");
		sbSql.append("          TO_NUMBER(H.SIT_NAME),E.DEALER_ID"); 
		List<Object> params=new ArrayList<Object>();
		params.add(Constant.IF_TYPE_YES);
		params.add(boId);
		return dao.pageQuery(sbSql.toString(), params, getFunName());
	}
	/**
	 * 打印(合并信息)
	 * @param map
	 * @param curPage
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	public List<Map<String,Object>> rowSpanList(Long boId){
		StringBuffer sbSql=new StringBuffer();
		sbSql.append("SELECT REC_DEALER_ID, COUNT(1) ROW_SPAN\r\n");
		sbSql.append("  FROM TT_SALES_BOARD SS, TT_SALES_BO_DETAIL XX, TT_SALES_ALLOCA_DE YY\r\n");
		sbSql.append(" WHERE SS.BO_ID = ?\r\n");
		sbSql.append("   AND XX.IS_ENABLE = ?\n");
		sbSql.append("   AND SS.BO_ID = XX.BO_ID\r\n");
		sbSql.append("   AND XX.BO_DE_ID = YY.BO_DE_ID\r\n");
		sbSql.append(" GROUP BY XX.REC_DEALER_ID");  
		List<Object> params=new ArrayList<Object>();
		params.add(boId);
		params.add(Constant.IF_TYPE_YES);
		return dao.pageQuery(sbSql.toString(), params, getFunName());
	}
}
