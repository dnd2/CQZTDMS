package com.infodms.dms.dao.report.serviceReport;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.common.Constant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.dao.common.BaseDao;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

@SuppressWarnings("unchecked")
public class OldPartReportDao extends BaseDao{

	public static final Logger logger = Logger.getLogger(OldPartReportDao.class);
	public static OldPartReportDao dao = new OldPartReportDao();
	public static OldPartReportDao getInstance(){
		return dao;
	}
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		return null;
	}
	public PageResult<Map<String,Object>> getOldPartDownList(Map<String, String> map,int pageSize,int curPage ){
		
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT N.NOTICE_CODE, --供应商代码\n");
		sql.append("       MD.MAKER_NAME, --供应商名称\n");
		sql.append("       od.claim_no, --索赔单号\n");
		sql.append("       ND.MODEL_NAME, --车型\n");
		sql.append("       ND.PART_CODE, --配件代码\n");
		sql.append("       ND.PART_NAME, --配件名称\n");
		sql.append("       sum(ND.OUT_NUM) OUT_NUM, --出库数量\n");
		sql.append("       sum(decode(ND.CLAIM_PRICE,0,ND.OUT_NUM,0)) zk_num, --折扣数量\n");
		sql.append("       sum(decode(ND.CLAIM_PRICE,0,0,ND.OUT_NUM)) zh_num, --折后数量\n");
		sql.append("       round(sum(decode(ND.CLAIM_PRICE,0,ND.OUT_NUM,0)) * --NULL不计算在平均值中\n");
		sql.append("             AVG(decode(ND.CLAIM_PRICE,0,NULL,ND.CLAIM_PRICE)) * 1.17,2) zk_amount--含税折扣金额\n");
		sql.append("  FROM TT_AS_WR_OLD_OUT_NOTICE N\n");
		sql.append("  JOIN TT_AS_WR_OLD_OUT_NOTICE_DETAIL ND ON N.NOTICE_ID = ND.NOTICE_ID\n");
		sql.append("  JOIN TT_PART_MAKER_DEFINE MD ON N.NOTICE_CODE = MD.MAKER_CODE\n");
		sql.append("  JOIN (SELECT DISTINCT X.OUT_NO, X.SUPPLAY_CODE, X.OUT_PART_CODE, X.CLAIM_NO FROM TT_AS_WR_OLD_OUT_DETAIL X) OD\n");
		sql.append("   ON n.OUT_NO = OD.OUT_NO AND OD.SUPPLAY_CODE = N.NOTICE_CODE AND OD.OUT_PART_CODE = ND.PART_CODE\n");
		sql.append(" WHERE 1 = 1\n");
		if(Utility.testString(map.get("supplyCode"))){
			sql.append("AND N.NOTICE_CODE LIKE '%"+map.get("supplyCode").toUpperCase()+"%' --供应商代码查询条件\n");
		}
		if(Utility.testString(map.get("supplyName"))){
			sql.append("  AND MD.MAKER_NAME LIKE '%"+map.get("supplyName")+"%' --供应商名称查询条件\n");
		}
		if(Utility.testString(map.get("partCode"))){
			sql.append("  AND ND.PART_CODE LIKE '%"+map.get("partCode").toUpperCase()+"%' --配件代码查询条件\n");
		}
		if(Utility.testString(map.get("partName"))){
			sql.append("  AND ND.PART_NAME LIKE '%"+map.get("partName")+"%' --配件名称查询条件\n");
		}
		if(Utility.testString(map.get("modelCode"))){
			sql.append("  AND ND.MODEL_NAME LIKE '%"+map.get("modelCode")+"%' --车型查询条件\n");
		}
		if(Utility.testString(map.get("yieldly"))){
			sql.append("  AND N.YIELDLY ="+map.get("yieldly")+" --结算基地 查询条件\n");
		}
		sql.append(" GROUP BY N.NOTICE_CODE, MD.MAKER_NAME, od.claim_no, ND.MODEL_NAME, ND.PART_CODE, ND.PART_NAME"); 
		return this.pageQuery(sql.toString(),null,this.getFunName(), pageSize, curPage); 
		}
public PageResult<Map<String,Object>> getTwiceClaimSuccList(Map<String, String> map,int pageSize,int curPage ){
		
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT O.PRODUCER_CODE, --供应商代码\n");
		sql.append("       MD.MAKER_NAME, --供应商名称\n");
		sql.append("       SUM(O.IN_NUM) IN_NUM, --入库数量\n");
		sql.append("       SUM(O.OUT_NUM) OUT_NUM, --出库数量\n");
		sql.append("       ROUND(SUM(O.OUT_AMOUNT), 2) OUT_AMOUNT, --含税索赔金额\n");
		sql.append("       ROUND(SUM(O.OUT_NUM) * 100 / SUM(O.IN_NUM), 2) || '%' SUC_PERCENT --二次索赔成功率\n");
		sql.append(" FROM (SELECT  A.PRODUCER_CODE, B.PART_CODE, A.IN_NUM, B.OUT_NUM OUT_NUM, B.OUT_AMOUNT\n");
		sql.append("        FROM (SELECT RD.PRODUCER_CODE, RD.PART_CODE, RD.CLAIM_NO,\n");
		sql.append("                     SUM(RD.SIGN_AMOUNT) IN_NUM, 0 OUT_NUM, 0 OUT_AMOUNT\n");
		sql.append("                FROM TT_AS_WR_OLD_RETURNED R\n");
		sql.append("                JOIN TT_AS_WR_OLD_RETURNED_DETAIL RD ON R.ID = RD.RETURN_ID\n");
		sql.append("               WHERE R.STATUS = "+Constant.BACK_LIST_STATUS_05+" --查询入库数据\n");
		if(Utility.testString(map.get("yieldly"))){
			sql.append("              AND R.YIELDLY ="+map.get("yieldly")+" --结算基地 查询条件\n");
		}
		sql.append("               GROUP BY RD.PRODUCER_CODE, RD.PART_CODE, RD.CLAIM_NO) A\n");
		sql.append("        JOIN (SELECT N.NOTICE_CODE, ND.PART_CODE, OD.CLAIM_NO, 0 IN_NUM,\n");
		sql.append("                     SUM(ND.OUT_NUM) OUT_NUM, SUM(ND.TOTAL) OUT_AMOUNT\n");
		sql.append("                FROM TT_AS_WR_OLD_OUT_NOTICE N\n");
		sql.append("                JOIN TT_AS_WR_OLD_OUT_NOTICE_DETAIL ND ON N.NOTICE_ID = ND.NOTICE_ID\n");
		sql.append("                JOIN (SELECT DISTINCT X.OUT_NO, X.SUPPLAY_CODE, X.OUT_PART_CODE, X.CLAIM_NO\n");
		sql.append("                       FROM TT_AS_WR_OLD_OUT_DETAIL X) OD ON N.OUT_NO = OD.OUT_NO\n");
		sql.append("                 AND OD.SUPPLAY_CODE = N.NOTICE_CODE AND OD.OUT_PART_CODE = ND.PART_CODE\n");
		sql.append("               WHERE 1 = 1\n");
		if(Utility.testString(map.get("yieldly"))){
			sql.append("              AND N.YIELDLY ="+map.get("yieldly")+" --结算基地 查询条件\n");
		}
		if(Utility.testString(map.get("bgDate"))){
			sql.append("               AND N.CREATE_DATE >= TO_DATE('"+map.get("bgDate")+"','YYYY-MM-DD')--开票时间开始\n");
		}
		if(Utility.testString(map.get("egDate"))){
			sql.append("                 AND N.CREATE_DATE <= TO_DATE('"+map.get("egDate")+"' ,'YYYY-MM-DD HH24:MI:SS')--开票时间结束\n");
		}
		if(Utility.testString(map.get("modelCode"))){
			sql.append("                 AND ND.MODEL_NAME LIKE '%"+map.get("modelCode").toUpperCase()+"%' --车型查询条件\n");
			}
		
		sql.append("               GROUP BY N.NOTICE_CODE, ND.PART_CODE, OD.CLAIM_NO) B ON A.PRODUCER_CODE = B.NOTICE_CODE\n");
		sql.append("                 AND A.PART_CODE = B.PART_CODE AND A.CLAIM_NO = B.CLAIM_NO\n");
		sql.append("      UNION ALL\n");
		sql.append("      --无实物索赔和特殊费用索赔部分\n");
		sql.append("      SELECT N.NOTICE_CODE, ND.PART_CODE, SUM(ND.OUT_NUM) IN_NUM,\n");
		sql.append("             SUM(ND.OUT_NUM) OUT_NUM, SUM(ND.TOTAL) OUT_AMOUNT\n");
		sql.append("        FROM TT_AS_WR_OLD_OUT_NOTICE N\n");
		sql.append("          JOIN TT_AS_WR_OLD_OUT_NOTICE_DETAIL ND ON N.NOTICE_ID = ND.NOTICE_ID\n");
		sql.append("        WHERE N.OUT_NO IS NULL\n");
		if(Utility.testString(map.get("bgDate"))){
			sql.append("               AND N.CREATE_DATE >= TO_DATE('"+map.get("bgDate")+"','YYYY-MM-DD')--开票时间开始\n");
		}
		if(Utility.testString(map.get("egDate"))){
			sql.append("                 AND N.CREATE_DATE <= TO_DATE('"+map.get("egDate")+"' ,'YYYY-MM-DD HH24:MI:SS')--开票时间结束\n");
		}
		if(Utility.testString(map.get("modelCode"))){
			sql.append("                 AND ND.MODEL_NAME LIKE '%"+map.get("modelCode").toUpperCase()+"%' --车型查询条件\n");
			}
		if(Utility.testString(map.get("yieldly"))){
			sql.append("                 AND N.YIELDLY ="+map.get("yieldly")+" --结算基地 查询条件\n");
		}
		sql.append("       GROUP BY N.NOTICE_CODE, ND.PART_CODE ) O\n");
		sql.append("  JOIN TT_PART_MAKER_DEFINE MD ON O.PRODUCER_CODE = MD.MAKER_CODE\n");
		sql.append(" WHERE 1 = 1\n");
		
		if(Utility.testString(map.get("supplyCode"))){
			sql.append("  AND MD.MAKER_CODE LIKE '%"+map.get("supplyCode").toUpperCase()+"%' --供应商代码查询条件\n");
		}
		if(Utility.testString(map.get("supplyName"))){
			sql.append("    AND MD.MAKER_NAME LIKE '%"+map.get("supplyName")+"%' --供应商名称查询条件\n");
		}
		sql.append("  GROUP BY O.PRODUCER_CODE, MD.MAKER_NAME"); 
		return this.pageQuery(sql.toString(),null,this.getFunName(), pageSize, curPage); 
		}
//得到车系
public List<Map<String, Object>> getSeriesList(){
	
	List<Map<String, Object>> list = null;
	StringBuffer sql = new StringBuffer();
	sql.append("SELECT T.GROUP_ID ,T.GROUP_NAME FROM TM_VHCL_MATERIAL_GROUP T WHERE T.GROUP_LEVEL = 2\n");
	sql.append(" ORDER  BY  T.GROUP_ID"); 
	list=super.pageQuery(sql.toString(), null, getFunName());
	return list;
}
//传入list 拼接为下拉框
public  String getStr(List<Map<String, Object>> list){
	String str = "<select class=\"short_sel\" id=\"series_id\" name=\"series_id\" >";
	str+=" <option value=\"\">--请选择--</option>";
	if(list!=null&&list.size()>0){
		for(int i=0;i<list.size();i++){
			str+=" <option value=\""+list.get(i).get("GROUP_ID")+"\">"+list.get(i).get("GROUP_NAME")+"</option>";
		}
	}
	return str;
}
public PageResult<Map<String,Object>> getOldPartInstoreList(Map<String, String> map,int pageSize,int curPage ){
	
	StringBuffer sql = new StringBuffer();
	sql.append("SELECT G.GROUP_NAME SERIES_NAME, --车系\n");
	sql.append("       RD.PRODUCER_CODE, --制造商代码\n");
	sql.append("       RD.PRODUCER_NAME, --制造商名称\n");
	sql.append("       RD.PART_CODE, --配件代码\n");
	sql.append("       RD.PART_NAME, --配件名称\n");
	sql.append("       P.PART_CODE PART_NO, --配件件号\n");
	sql.append("       P.UNIT, --单位\n");
	sql.append("       SUM(CASE WHEN IN_WARHOUSE_DATE <= TRUNC(SYSDATE,'MM') - 1/24/60/60 THEN RD.SIGN_AMOUNT ELSE 0 END -\n");
	sql.append("           CASE WHEN OUT_DATE <= TRUNC(SYSDATE,'MM') - 1/24/60/60 THEN DECODE(OD.BARCODE_NO, NULL, 0, 1) ELSE 0 END) H, --截至前月月底的结存数量\n");
	sql.append("       SUM(CASE WHEN IN_WARHOUSE_DATE >= TRUNC(SYSDATE,'MM') THEN RD.SIGN_AMOUNT ELSE 0 END) I, --本月入库数量\n");
	sql.append("       SUM(CASE WHEN OUT_DATE >= TRUNC(SYSDATE,'MM') THEN DECODE(OD.BARCODE_NO, NULL, 0, 1) ELSE 0 END) O, --本月出库数量\n");
	sql.append("       SUM(RD.SIGN_AMOUNT - DECODE(OD.BARCODE_NO, NULL, 0, 1)) N --本月结存数量\n");
	sql.append("  FROM TT_AS_WR_OLD_RETURNED R\n");
	sql.append("  JOIN TT_AS_WR_OLD_RETURNED_DETAIL RD ON R.ID = RD.RETURN_ID\n");
	sql.append("  JOIN TM_VEHICLE V ON RD.VIN = V.VIN\n");
	sql.append("  JOIN TM_VHCL_MATERIAL_GROUP G ON G.GROUP_LEVEL = 2 AND V.SERIES_ID = G.GROUP_ID\n");
	sql.append("  LEFT JOIN TT_PART_DEFINE P ON RD.PART_CODE = P.PART_OLDCODE\n");
	sql.append("  LEFT JOIN TT_AS_WR_OLD_OUT_DETAIL OD ON RD.BARCODE_NO = OD.BARCODE_NO\n");
	sql.append(" WHERE R.STATUS = "+Constant.BACK_LIST_STATUS_05+" --查询入库数据\n");
	if(Utility.testString(map.get("yieldly"))){
		sql.append("  AND R.YIELDLY ="+map.get("yieldly")+" --结算基地查询条件\n");
	}
	if(Utility.testString(map.get("series_id"))){
		sql.append("  AND V.SERIES_ID ="+map.get("series_id")+" --车系查询条件\n");
	}
	if(Utility.testString(map.get("supplyCode"))){
		sql.append("  AND RD.PRODUCER_CODE LIKE '%"+map.get("supplyCode").toUpperCase()+"%'--制造商代码查询条件\n");
	}
	if(Utility.testString(map.get("supplyName"))){
		sql.append("  AND RD.PRODUCER_NAME LIKE '%"+map.get("supplyName")+"%'--制造商名称查询条件\n");
	}
	if(Utility.testString(map.get("partCode"))){
		sql.append("  AND RD.PART_CODE LIKE '%"+map.get("partCode")+"%' --配件代码查询条件\n");
	}
	if(Utility.testString(map.get("partName"))){
		sql.append("  AND RD.PART_NAME LIKE '%"+map.get("partName")+"%' --配件名称查询条件\n");
	}
	sql.append(" GROUP BY G.GROUP_NAME, RD.PRODUCER_CODE, RD.PRODUCER_NAME, RD.PART_CODE, RD.PART_NAME, P.PART_CODE, P.UNIT"); 
	return this.pageQuery(sql.toString(),null,this.getFunName(), pageSize, curPage); 
	}
}
