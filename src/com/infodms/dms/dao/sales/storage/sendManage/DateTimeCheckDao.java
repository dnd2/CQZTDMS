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
 * 
 * @ClassName     : DateTimeCheckDao 
 * @Description   : 物流运输考核管理DAO 
 * @author        : ranjian
 * CreateDate     : 2013-9-4
 */
public class DateTimeCheckDao extends BaseDao<PO>{
	private static final DateTimeCheckDao dao = new DateTimeCheckDao ();
	public static final DateTimeCheckDao getInstance() {
		return dao;
	}
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}
	/**
	 * 物流运输考核管理查询列表
	 * @param map
	 * @param curPage
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	public PageResult<Map<String, Object>> getDateTimeCheckQuery(Map<String, Object> map, int curPage, int pageSize)throws Exception{
		Object[] obj=getSQL(map);
		PageResult<Map<String, Object>> ps= dao.pageQuery(obj[0].toString(), (List<Object>)obj[1], getFunName(),pageSize,curPage);
		return ps;
	}
	/**
	 * 物流运输考核管理导出
	 * @param map
	 * @return 组板信息列表
	 * @throws Exception
	 */
	public List<Map<String, Object>> getDateTimeCheckExport(Map<String, Object> map)throws Exception{
		Object[] obj=getSQL(map);
		List<Map<String, Object>> list= dao.pageQuery(obj[0].toString(), (List<Object>)obj[1], getFunName());
		return list;
	}
	/**
	 * 物流商时间考核数量和
	 *param map 查询参数
	 */
	public Map<String,Object> tgSum(Map<String, Object> map){
		Object[] obj=getSQL(map);
		StringBuffer sbSql=new StringBuffer();
		sbSql.append("SELECT SUM(NVL(VEH_NUM,0)) VEH_NUM FROM (\n");
		sbSql.append(obj[0].toString());
		sbSql.append(")\n");
		Map<String, Object> mapR= dao.pageQueryMap(sbSql.toString(), (List<Object>)obj[1], getFunName());
		return mapR;
	}
	//get sql
	public Object[] getSQL(Map<String, Object> map){
		/******************************页面查询字段start**************************/
		String yieldly = (String)map.get("yieldly"); // 产地		
		String dealerCode =(String)map.get("dealerCode"); // 经销商CODE
		String logiName = (String)map.get("logiName"); // 物流公司
		String assStartDate =(String)map.get("assStartDate"); // 提报日期开始
		String assEndDate = (String)map.get("assEndDate"); // 提报日期结束
		String poseId = (String)map.get("poseId");
		String countyId = (String)map.get("countyId");// 区县
		String cityId = (String)map.get("CITY_ID");// 地市
		String provinceId = (String)map.get("PROVINCE_ID");// 省份
		/******************************页面查询字段end***************************/
		List<Object> params = new LinkedList<Object>();
		params.add(Constant.IF_TYPE_YES);
		params.add(Constant.IF_TYPE_YES);
		params.add(Constant.IF_TYPE_YES);
		params.add(Constant.STATUS_ENABLE);
		StringBuffer sbSql= new StringBuffer();
		
		//sbSql.append("--及时到达率\n");
		sbSql.append("WITH ACC_TAB AS\n");
		sbSql.append("(SELECT T1.BO_ID, MIN(T2.ACC_DATE) ACC_DATE\n");
		sbSql.append("   FROM TT_SALES_BO_DETAIL T1, TT_SALES_ALLOCA_DE T2\n");
		sbSql.append("  WHERE T1.BO_DE_ID = T2.BO_DE_ID\n");
		sbSql.append("       --AND T1.BILL_ID = A.BILL_ID\n");
		sbSql.append("    AND T2.IS_ACC = ?\n");
		sbSql.append("  GROUP BY T1.BO_ID)"); 
		sbSql.append("SELECT T.*,\n");
		sbSql.append("T.CONFIRM_DATE + (SELECT MIN(TI.ASS_DAYS)\n" );
		sbSql.append("                           FROM TT_SALES_LOGI_INTREVAL TI\n" );
		sbSql.append("                          WHERE TI.STATUS = 10011001\n" );
		sbSql.append("                            AND T.VEH_NUM > TI.BEGIN_NUM\n" );
		sbSql.append("                            AND T.VEH_NUM <= TI.END_NUM) ZP_DATE\n");
		sbSql.append("FROM(SELECT I.BO_NO,\n");
		sbSql.append("       A.BILL_NO,\n");
		sbSql.append("       A.CONFIRM_DATE,\n");
		sbSql.append("       H.LOGI_NAME,\n");
		sbSql.append("       B.DEALER_NAME,\n");
		sbSql.append("       D.REGION_NAME PC_NAME,\n");
		sbSql.append("       E.REGION_NAME PC_NAME1,\n");
		sbSql.append("       F.REGION_NAME PC_NAME2,\n");
		sbSql.append("       I.CAR_NO,\n");
		sbSql.append("       I.CAR_TEAM,\n");
		sbSql.append("       I.POLICY_NO,\n");
		sbSql.append("       J.CODE_DESC POLICY_TYPE,\n");
		sbSql.append("       MAX(A.VEH_NUM) VEH_NUM,\n");
		sbSql.append("       MAX(TO_CHAR(A.BILL_CRT_DATE, 'YYYY-MM-DD HH24:MI:SS')) BILL_CRT_DATE,\n");
		sbSql.append("       MAX((SELECT T.ARRIVE_DAYS\n");
		sbSql.append("             FROM TT_SALES_CITY_DIS T\n");
		sbSql.append("            WHERE T.YIELDLY = A.AREA_ID\n");
		sbSql.append("              AND T.CITY_ID = F.REGION_ID)) ARRIVE_DAYS,\n");
		sbSql.append("       TO_CHAR(MAX(A.BILL_CRT_DATE +\n");
		sbSql.append("                   (SELECT T.ARRIVE_DAYS\n");
		sbSql.append("                      FROM TT_SALES_CITY_DIS T\n");
		sbSql.append("                     WHERE T.YIELDLY = A.AREA_ID\n");
		sbSql.append("                       AND T.CITY_ID = F.REGION_ID)),\n");
		sbSql.append("               'YYYY-MM-DD HH24:MI:SS') DD_DATE,\n");
		sbSql.append("       TO_CHAR(MAX(L.ACC_DATE), 'YYYY-MM-DD HH24:MI:SS') ACC_DATE,\n");
		sbSql.append("       MAX(ROUND(L.ACC_DATE -\n");
		sbSql.append("                 (A.BILL_CRT_DATE +\n");
		sbSql.append("                 (SELECT T.ARRIVE_DAYS\n");
		sbSql.append("                     FROM TT_SALES_CITY_DIS T\n");
		sbSql.append("                    WHERE T.YIELDLY = A.AREA_ID\n");
		sbSql.append("                      AND T.CITY_ID = F.REGION_ID)))) D_DAYS\n");
		sbSql.append("  from TT_SALES_WAYBILL   A,\n");
		sbSql.append("       TM_DEALER          B,\n");
		sbSql.append("       TM_VS_ADDRESS      C,\n");
		sbSql.append("       TM_REGION          D,\n");
		sbSql.append("       TM_REGION          E,\n");
		sbSql.append("       TM_REGION          F,\n");
		sbSql.append("       TT_SALES_LOGI      H,\n");
		sbSql.append("       TT_SALES_BOARD     I,\n");
		sbSql.append("       TC_CODE            J,\n");
		sbSql.append("       TT_SALES_BO_DETAIL K,\n");
		sbSql.append("       ACC_TAB            L\n");
		sbSql.append(" WHERE A.SEND_DEALER_ID = B.DEALER_ID\n");
		sbSql.append("   AND A.ADDRESS_ID = C.ID(+)\n");
		sbSql.append("   AND C.PROVINCE_ID = D.REGION_CODE\n");
		sbSql.append("   AND C.CITY_ID = E.REGION_CODE\n");
		sbSql.append("   AND C.AREA_ID = F.REGION_CODE\n");
		sbSql.append("   AND I.POLICY_TYPE = J.CODE_ID\n");
		sbSql.append("   AND A.LOGI_ID = H.LOGI_ID\n");
		sbSql.append("   AND A.BILL_ID = K.BILL_ID\n");
		sbSql.append("   AND K.BO_ID = I.BO_ID\n");
		sbSql.append("   AND I.BO_ID = L.BO_ID(+)\n");
		sbSql.append("   AND K.IS_ENABLE=?\n");
		sbSql.append("   AND I.IS_ENABLE=?\n"); 
		sbSql.append("   AND A.STATUS = ?\n");
		sbSql.append(MaterialGroupManagerDao.getPoseIdBusinessSqlByPar("A.AREA_ID",poseId,params));//车厂端查询列表产地数据权限
        if(yieldly!=null&&!"".equals(yieldly)){//产地
        	sbSql.append("   AND A.AREA_ID =?\n" );
        	params.add(yieldly);
		}
		if(dealerCode!=null&&!"".equals(dealerCode)){//经销商CODE
			sbSql.append(Utility.getConSqlByParamForEqual(dealerCode, params,"B", "DEALER_CODE"));
		}
		if(logiName!=null&&!"".equals(logiName)){//物流公司
			sbSql.append("   AND A.LOGI_ID=?\n" );
			params.add(logiName);
		}
		if(assStartDate != null && !"".equals(assStartDate)){
			sbSql.append("   AND A.BILL_CRT_DATE>=TO_DATE(?,'YYYY-MM-DD HH24:MI:SS')\n" );
			params.add(assStartDate+" 00:00:00");
		}
		if(assEndDate != null && !"".equals(assEndDate)){
			sbSql.append("   AND A.BILL_CRT_DATE<=TO_DATE(?,'YYYY-MM-DD HH24:MI:SS')\n" );
			params.add(assEndDate+" 23:59:59");
		}
		if(provinceId!=null&&!"".equals(provinceId)){
			sbSql.append(" AND D.REGION_CODE =?\n");	
			params.add(provinceId);
		}
		if(cityId!=null&&!"".equals(cityId)){
			sbSql.append(" AND E.REGION_CODE=?\n");
			params.add(cityId);
		}
		if(countyId!=null&&!"".equals(countyId)){
			sbSql.append(" AND F.REGION_NAME LIKE ?\n");
			params.add( "%"+countyId+"%");
		}
		sbSql.append(" GROUP BY I.BO_NO,\n");
		sbSql.append("          A.BILL_NO,\n");
		sbSql.append("          A.CONFIRM_DATE,\n");
		sbSql.append("          H.LOGI_NAME,\n");
		sbSql.append("          B.DEALER_NAME,\n");
		sbSql.append("          D.REGION_NAME,\n");
		sbSql.append("          E.REGION_NAME,\n");
		sbSql.append("          F.REGION_NAME,\n");
		sbSql.append("          I.CAR_NO,\n");
		sbSql.append("          I.CAR_TEAM,\n");
		sbSql.append("          I.POLICY_NO,\n");
		sbSql.append("          J.CODE_DESC\n"); 
		sbSql.append(" ORDER BY I.BO_NO ASC\n"); //按组板号排序
		sbSql.append(") T\n");
		Object[] arr=new Object[2];
		arr[0]=sbSql;
		arr[1]=params;
		return arr;
	}
	
	
	/**
	 * 物流商时间考核信息查询列表【配板】
	 * @param map
	 * @param curPage
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	public PageResult<Map<String, Object>> getDateTimeCheckQueryPC(Map<String, Object> map, int curPage, int pageSize)throws Exception{
		Object[] obj=getSQLPC(map);
		PageResult<Map<String, Object>> ps= dao.pageQuery(obj[0].toString(), (List<Object>)obj[1], getFunName(),pageSize,curPage);
		return ps;
	}
	/**
	 * 物流商时间考核信息查询导出【配板】
	 * @param map
	 * @return 组板信息列表
	 * @throws Exception
	 */
	public List<Map<String, Object>> getDateTimeCheckPCExport(Map<String, Object> map)throws Exception{
		Object[] obj=getSQLPC(map);
		List<Map<String, Object>> list= dao.pageQuery(obj[0].toString(), (List<Object>)obj[1], getFunName());
		return list;
	}
	/**
	 * 物流商时间考核数量和【配板】
	 *param map 查询参数
	 */
	public Map<String,Object> tgSumPC(Map<String, Object> map){
		Object[] obj=getSQLPC(map);
		StringBuffer sbSql=new StringBuffer();
		sbSql.append("SELECT SUM(NVL(VEH_NUM,0)) VEH_NUM FROM (\n");
		sbSql.append(obj[0].toString());
		sbSql.append(")\n");
		Map<String, Object> mapR= dao.pageQueryMap(sbSql.toString(), (List<Object>)obj[1], getFunName());
		return mapR;
	}
	//get sql
	public Object[] getSQLPC(Map<String, Object> map){
		/******************************页面查询字段start**************************/
		String yieldly = (String)map.get("yieldly"); // 产地		
		String dealerCode =(String)map.get("dealerCode"); // 经销商CODE
		String logiName = (String)map.get("logiName"); // 物流公司
		String assStartDate =(String)map.get("assStartDate"); // 提报日期开始
		String assEndDate = (String)map.get("assEndDate"); // 提报日期结束
		String poseId = (String)map.get("poseId");
		String countyId = (String)map.get("countyId");// 区县
		String cityId = (String)map.get("CITY_ID");// 地市
		String provinceId = (String)map.get("PROVINCE_ID");// 省份
		/******************************页面查询字段end***************************/
		List<Object> params = new LinkedList<Object>();
		params.add(Constant.STATUS_ENABLE);
		params.add(Constant.IF_TYPE_YES);
		params.add(Constant.IF_TYPE_YES);
		StringBuffer sbSql= new StringBuffer();
		sbSql.append("WITH TT_S AS\n");
		sbSql.append(" (SELECT B.BO_ID, MAX(B.ASS_DATE) ASS_DATE\n");
		sbSql.append("    FROM TT_SALES_WAYBILL A, TT_SALES_BO_DETAIL B\n");
		sbSql.append("   WHERE A.BILL_ID = B.BILL_ID\n");
		sbSql.append("   GROUP BY B.BO_ID)\n");
		sbSql.append("SELECT I.BO_NO,\n");
		sbSql.append("       A.BILL_NO,\n");
		sbSql.append("       H.LOGI_NAME,\n");
		sbSql.append("           B.DEALER_NAME,\n");
		sbSql.append("           D.REGION_NAME PC_NAME,\n");
		sbSql.append("           E.REGION_NAME PC_NAME1,\n");
		sbSql.append("           F.REGION_NAME PC_NAME2,\n");
		sbSql.append("           I.CAR_NO,\n");
		sbSql.append("           I.CAR_TEAM,\n");
		sbSql.append("           I.POLICY_NO,\n");
		sbSql.append("           J.CODE_DESC POLICY_TYPE,\n");
		sbSql.append("           MIN(A.VEH_NUM) VEH_NUM,\n");
		sbSql.append("           TO_CHAR(MIN(K.ASS_DATE), 'YYYY-MM-DD HH24:MI:SS') ASS_DATE,\n");
		sbSql.append("           TO_CHAR(MIN(I.BO_DATE), 'YYYY-MM-DD HH24:MI:SS') BO_DATE,\n");
		sbSql.append("           --默认第二天完为1天。。\n");
		sbSql.append("           DECODE(ROUND(MIN(I.BO_DATE) - MIN(K.ASS_DATE)) - 1,\n");
		sbSql.append("                  -1,\n");
		sbSql.append("                  0,\n");
		sbSql.append("                  ROUND(MIN(I.BO_DATE) - MIN(K.ASS_DATE)) - 1) CS_TIME\n");
		sbSql.append("      FROM TT_SALES_WAYBILL   A,\n");
		sbSql.append("           TM_DEALER          B,\n");
		sbSql.append("           TM_VS_ADDRESS      C,\n");
		sbSql.append("           TM_REGION          D,\n");
		sbSql.append("           TM_REGION          E,\n");
		sbSql.append("           TM_REGION          F,\n");
		sbSql.append("           TT_SALES_LOGI      H,\n");
		sbSql.append("           TT_SALES_BO_DETAIL G,\n");
		sbSql.append("           TT_SALES_BOARD     I,\n");
		sbSql.append("           TC_CODE            J,\n");
		sbSql.append("           TT_S               K\n");
		sbSql.append("     WHERE A.SEND_DEALER_ID = B.DEALER_ID\n");
		sbSql.append("       AND A.ADDRESS_ID = C.ID(+)\n");
		sbSql.append("       AND C.PROVINCE_ID = D.REGION_CODE\n");
		sbSql.append("       AND C.CITY_ID = E.REGION_CODE\n");
		sbSql.append("       AND C.AREA_ID = F.REGION_CODE\n");
		sbSql.append("       AND I.POLICY_TYPE = J.CODE_ID\n");
		sbSql.append("       AND A.LOGI_ID = H.LOGI_ID\n");
		sbSql.append("       AND A.STATUS = ?\n");
		sbSql.append("       AND G.IS_ENABLE = ?\n");
		sbSql.append("       AND I.IS_ENABLE = ?\n");
		sbSql.append("       AND A.BILL_ID = G.BILL_ID\n");
		sbSql.append("       AND G.BO_ID = I.BO_ID\n");
		sbSql.append("       AND G.BO_ID = K.BO_ID\n");
		sbSql.append(MaterialGroupManagerDao.getPoseIdBusinessSqlByPar("A.AREA_ID",poseId,params));//车厂端查询列表产地数据权限
		
		if(yieldly!=null&&!"".equals(yieldly)){//产地
        	sbSql.append("   AND A.AREA_ID =?\n" );
        	params.add(yieldly);
		}
		if(dealerCode!=null&&!"".equals(dealerCode)){//经销商CODE
			sbSql.append(Utility.getConSqlByParamForEqual(dealerCode, params,"B", "DEALER_CODE"));
		}
		if(logiName!=null&&!"".equals(logiName)){//物流公司
			sbSql.append("   AND A.LOGI_ID=?\n" );
			params.add(logiName);
		}
		if(assStartDate != null && !"".equals(assStartDate)){
			sbSql.append("   AND G.ASS_DATE>=TO_DATE(?,'YYYY-MM-DD HH24:MI:SS')\n" );
			params.add(assStartDate+" 00:00:00");
		}
		if(assEndDate != null && !"".equals(assEndDate)){
			sbSql.append("   AND G.ASS_DATE<=TO_DATE(?,'YYYY-MM-DD HH24:MI:SS')\n" );
			params.add(assEndDate+" 23:59:59");
		}
		if(provinceId!=null&&!"".equals(provinceId)){
			sbSql.append(" AND D.REGION_CODE =?\n");	
			params.add(provinceId);
		}
		if(cityId!=null&&!"".equals(cityId)){
			sbSql.append(" AND E.REGION_CODE=?\n");
			params.add(cityId);
		}
		if(countyId!=null&&!"".equals(countyId)){
			sbSql.append(" AND F.REGION_NAME LIKE ?\n");
			params.add( "%"+countyId+"%");
		}
		sbSql.append("     GROUP BY I.BO_NO,\n");
		sbSql.append("              A.BILL_NO,\n");
		sbSql.append("              H.LOGI_NAME,\n");
		sbSql.append("              B.DEALER_NAME,\n");
		sbSql.append("              D.REGION_NAME,\n");
		sbSql.append("              E.REGION_NAME,\n");
		sbSql.append("              F.REGION_NAME,\n");
		sbSql.append("              I.CAR_NO,\n");
		sbSql.append("              I.CAR_TEAM,\n");
		sbSql.append("              I.POLICY_NO,\n");
		sbSql.append("              J.CODE_DESC"); 
		sbSql.append(" ORDER BY I.BO_NO ASC"); //按组板号排序
		Object[] arr=new Object[2];
		arr[0]=sbSql;
		arr[1]=params;
		return arr;
	}
	/**
	 * 综合时间统计查询
	 * @param map
	 * @param curPage
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	public PageResult<Map<String, Object>> getDateTimeCheckQueryDate(Map<String, Object> map, int curPage, int pageSize)throws Exception{
		Object[] obj=getSQLZH(map);
		PageResult<Map<String, Object>> ps= dao.pageQuery(obj[0].toString(), (List<Object>)obj[1], getFunName(),pageSize,curPage);
		return ps;
	}
	/**
	 * 综合时间统计导出
	 * @param map
	 * @return 组板信息列表
	 * @throws Exception
	 */
	public List<Map<String, Object>> getDateTimeCheckDateExport(Map<String, Object> map)throws Exception{
		Object[] obj=getSQLZH(map);
		List<Map<String, Object>> list= dao.pageQuery(obj[0].toString(), (List<Object>)obj[1], getFunName());
		return list;
	}
	/**
	 * 综合时间统计求和
	 *param map 查询参数
	 */
	public Map<String,Object> tgSumDate(Map<String, Object> map){
		Object[] obj=getSQLZH(map);
		StringBuffer sbSql=new StringBuffer();
		sbSql.append("SELECT SUM(NVL(VEH_NUM,0)) VEH_NUM FROM (\n");
		sbSql.append(obj[0].toString());
		sbSql.append(")\n");
		Map<String, Object> mapR= dao.pageQueryMap(sbSql.toString(), (List<Object>)obj[1], getFunName());
		return mapR;
	}
	//get sql
	public Object[] getSQLZH(Map<String, Object> map){
		/******************************页面查询字段start**************************/
		String vin = (String)map.get("vin"); // VIN	
		String boardNo =(String)map.get("boardNo"); // 组板号
		String waybillNo = (String)map.get("waybillNo"); // 运单号
		String poseId = (String)map.get("poseId");
		/******************************页面查询字段end***************************/
		List<Object> params = new LinkedList<Object>();
		params.add(Constant.IF_TYPE_YES);
		params.add(Constant.IF_TYPE_YES);
		params.add(Constant.IF_TYPE_YES);
		params.add(Constant.STATUS_ENABLE);
		StringBuffer sbSql= new StringBuffer();
		
		sbSql.append("WITH ACC_TAB AS\n");
		sbSql.append(" (SELECT T1.BO_ID, MIN(T2.ACC_DATE) ACC_DATE\n");
		sbSql.append("    FROM TT_SALES_BO_DETAIL T1, TT_SALES_ALLOCA_DE T2\n");
		sbSql.append("   WHERE T1.BO_DE_ID = T2.BO_DE_ID\n");
		sbSql.append("        --AND T1.BILL_ID = A.BILL_ID\n");
		sbSql.append("     AND T2.IS_ACC = ?\n");
		sbSql.append("   GROUP BY T1.BO_ID),\n");
		sbSql.append("ASS_TAB AS\n");
		sbSql.append(" (SELECT B.BO_ID, MAX(B.ASS_DATE) ASS_DATE\n");
		sbSql.append("    FROM TT_SALES_WAYBILL A, TT_SALES_BO_DETAIL B\n");
		sbSql.append("   WHERE A.BILL_ID = B.BILL_ID\n");
		sbSql.append("   GROUP BY B.BO_ID)\n");
		sbSql.append("SELECT I.BO_NO,\n");
		sbSql.append("       A.BILL_NO,\n");
		sbSql.append("       H.LOGI_NAME,\n");
		sbSql.append("       B.DEALER_NAME,\n");
		sbSql.append("       D.REGION_NAME PC_NAME,\n");
		sbSql.append("       E.REGION_NAME PC_NAME1,\n");
		sbSql.append("       F.REGION_NAME PC_NAME2,\n");
		sbSql.append("       I.CAR_NO,\n");
		sbSql.append("       I.CAR_TEAM,\n");
		sbSql.append("       I.POLICY_NO,\n");
		sbSql.append("       J.CODE_DESC POLICY_TYPE,\n");
		sbSql.append("       MAX(A.VEH_NUM) VEH_NUM,\n");
		sbSql.append("       MAX(TO_CHAR(A.BILL_CRT_DATE, 'YYYY-MM-DD HH24:MI:SS')) BILL_CRT_DATE,\n");
		sbSql.append("       MAX((SELECT T.ARRIVE_DAYS\n");
		sbSql.append("             FROM TT_SALES_CITY_DIS T\n");
		sbSql.append("            WHERE T.YIELDLY = A.AREA_ID\n");
		sbSql.append("              AND T.CITY_ID = F.REGION_ID)) ARRIVE_DAYS,\n");
		sbSql.append("       TO_CHAR(MAX(A.BILL_CRT_DATE +\n");
		sbSql.append("                   (SELECT T.ARRIVE_DAYS\n");
		sbSql.append("                      FROM TT_SALES_CITY_DIS T\n");
		sbSql.append("                     WHERE T.YIELDLY = A.AREA_ID\n");
		sbSql.append("                       AND T.CITY_ID = F.REGION_ID)),\n");
		sbSql.append("               'YYYY-MM-DD HH24:MI:SS') DD_DATE,\n");
		sbSql.append("       TO_CHAR(MAX(L.ACC_DATE), 'YYYY-MM-DD HH24:MI:SS') ACC_DATE,\n");
		sbSql.append("       TO_CHAR(MAX(M.ASS_DATE), 'YYYY-MM-DD HH24:MI:SS') ASS_DATE,\n");
		sbSql.append("       TO_CHAR(MAX(I.BO_DATE), 'YYYY-MM-DD HH24:MI:SS') BO_DATE,\n");
		sbSql.append("       MAX(ROUND(L.ACC_DATE -\n");
		sbSql.append("                 (A.BILL_CRT_DATE +\n");
		sbSql.append("                 (SELECT T.ARRIVE_DAYS\n");
		sbSql.append("                     FROM TT_SALES_CITY_DIS T\n");
		sbSql.append("                    WHERE T.YIELDLY = A.AREA_ID\n");
		sbSql.append("                      AND T.CITY_ID = F.REGION_ID)))) D_DAYS\n");
		sbSql.append("  from TT_SALES_WAYBILL   A,\n");
		sbSql.append("       TM_DEALER          B,\n");
		sbSql.append("       TM_VS_ADDRESS      C,\n");
		sbSql.append("       TM_REGION          D,\n");
		sbSql.append("       TM_REGION          E,\n");
		sbSql.append("       TM_REGION          F,\n");
		sbSql.append("       TT_SALES_LOGI      H,\n");
		sbSql.append("       TT_SALES_BOARD     I,\n");
		sbSql.append("       TC_CODE            J,\n");
		sbSql.append("       TT_SALES_BO_DETAIL K,\n");
		sbSql.append("       ACC_TAB            L,\n");
		sbSql.append("       ASS_TAB            M\n");
		sbSql.append(" WHERE A.SEND_DEALER_ID = B.DEALER_ID\n");
		sbSql.append("   AND A.ADDRESS_ID = C.ID(+)\n");
		sbSql.append("   AND C.PROVINCE_ID = D.REGION_CODE\n");
		sbSql.append("   AND C.CITY_ID = E.REGION_CODE\n");
		sbSql.append("   AND C.AREA_ID = F.REGION_CODE\n");
		sbSql.append("   AND I.POLICY_TYPE = J.CODE_ID\n");
		sbSql.append("   AND A.LOGI_ID = H.LOGI_ID\n");
		sbSql.append("   AND A.BILL_ID = K.BILL_ID\n");
		sbSql.append("   AND K.BO_ID = I.BO_ID\n");
		sbSql.append("   AND I.BO_ID = L.BO_ID(+)\n");
		sbSql.append("   AND I.BO_ID = M.BO_ID(+)\n");
		sbSql.append("   AND K.IS_ENABLE = ?\n");
		sbSql.append("   AND I.IS_ENABLE = ?\n");
		sbSql.append("   AND A.STATUS = ?\n");
		sbSql.append(MaterialGroupManagerDao.getPoseIdBusinessSqlByPar("A.AREA_ID",poseId,params));//车厂端查询列表产地数据权限
        if(vin!=null&&!"".equals(vin)){//VIN
        	sbSql.append(" AND EXISTS(\n");
        	sbSql.append("SELECT 1\n");
        	sbSql.append("  FROM TT_SALES_ALLOCA_DE T1, TM_VEHICLE C\n");
        	sbSql.append(" WHERE K.BO_DE_ID = T1.BO_DE_ID\n");
        	sbSql.append("   AND T1.VEHICLE_ID = C.VEHICLE_ID\n");
        	sbSql.append("   AND C.VIN = ?)"); 
        	params.add(vin);
		}
		if(boardNo!=null&&!"".equals(boardNo)){//组板号
			//sbSql.append(Utility.getConSqlByParamForEqual(dealerCode, params,"B", "DEALER_CODE"));
			sbSql.append("   AND I.BO_NO=?\n" );
			params.add(boardNo);
		}
		if(waybillNo!=null&&!"".equals(waybillNo)){//运单号
			sbSql.append("   AND A.BILL_NO=?\n" );
			params.add(waybillNo);
		}
		sbSql.append(" GROUP BY I.BO_NO,\n");
		sbSql.append("          A.BILL_NO,\n");
		sbSql.append("          H.LOGI_NAME,\n");
		sbSql.append("          B.DEALER_NAME,\n");
		sbSql.append("          D.REGION_NAME,\n");
		sbSql.append("          E.REGION_NAME,\n");
		sbSql.append("          F.REGION_NAME,\n");
		sbSql.append("          I.CAR_NO,\n");
		sbSql.append("          I.CAR_TEAM,\n");
		sbSql.append("          I.POLICY_NO,\n");
		sbSql.append("          J.CODE_DESC\n");
		sbSql.append(" ORDER BY I.BO_NO ASC"); 
		Object[] arr=new Object[2];
		arr[0]=sbSql;
		arr[1]=params;
		return arr;
	}
}
