package com.infodms.dms.dao.sales.ordermanage.resourceQuery;

import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import sun.java2d.pipe.SpanShapeRenderer.Simple;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.po.TmDateSetPO;
import com.infodms.dms.po.TtVsResourceReserveLogDtlPO;
import com.infodms.dms.po.TtVsResourceReserveLogPO;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

public class ResourceQueryDAO extends BaseDao<PO>{
	public static Logger logger = Logger.getLogger(ResourceQueryDAO.class);
	private static final ResourceQueryDAO dao = new ResourceQueryDAO ();
	public static final ResourceQueryDAO getInstance() {
		return dao;
	}
	
	public static PageResult <Map<String,Object>> getResourceQueryList(String warseId,String resStatus ,String areaId ,String groupCode, Long oemCompanyId , int pageSize,int curPage){
//		StringBuffer sql= new StringBuffer();
//		sql.append("SELECT M.MATERIAL_CODE,\n");
//		sql.append("       M.MATERIAL_NAME,\n");  
//		sql.append("       M.COLOR_NAME,\n");  
//		sql.append("       NVL(AVA_STOCK,0) AVA_STOCK,\n");  
//		sql.append("       NVL(req_amount,0) req_amount,\n");  
//		sql.append("       NVL(VWR.RESOURCE_AMOUNT,0)RESOURCE_AMOUNT\n");  
//		
//		sql.append("  FROM TM_VHCL_MATERIAL         M,\n");  
//		sql.append("       TM_VHCL_MATERIAL_GROUP_R R,\n");  
//		sql.append("       TM_VHCL_MATERIAL_GROUP   G,\n");  
//		sql.append("       VW_VS_ORDER_RESOURCE     VWR\n");  
//		sql.append(" WHERE M.MATERIAL_ID = R.MATERIAL_ID\n");  
//		sql.append("   AND R.GROUP_ID = G.GROUP_ID\n");  
//		sql.append("   AND M.STATUS = "+Constant.STATUS_ENABLE+"\n");  
//		sql.append("   AND VWR.MATERIAL_ID(+) = M.MATERIAL_ID\n");  
//		sql.append("   AND G.GROUP_ID IN\n");  
//		sql.append("       (SELECT T1.GROUP_ID\n");  
//		sql.append("          FROM TM_VHCL_MATERIAL_GROUP T1\n");  
//		sql.append("         WHERE T1.STATUS = "+Constant.STATUS_ENABLE+"\n");  
//		sql.append("         START WITH T1.GROUP_ID IN\n");  
//		sql.append("                    (SELECT TAP.MATERIAL_GROUP_ID\n");  
//		sql.append("                       FROM TM_AREA_GROUP TAP\n");  
//		sql.append("                      WHERE 1 = 1\n");  
//		sql.append("                        AND TAP.AREA_ID IN ("+areaId+"))\n");  
//		sql.append("        CONNECT BY PRIOR T1.GROUP_ID = T1.PARENT_GROUP_ID)\n");  
//		sql.append("   AND M.COMPANY_ID = "+oemCompanyId+"\n");  
//		sql.append("   AND M.STATUS = "+Constant.STATUS_ENABLE+"\n");  
//	//	sql.append("   AND M.ORDER_FLAG = "+Constant.NASTY_ORDER_REPORT_TYPE_01+"\n");  
//	//	sql.append("   AND M.RUSH_ORDER_FLAG = "+Constant.NASTY_ORDER_REPORT_TYPE_01+"\n");  
//		if (null != groupCode && !"".equals(groupCode)) {
//			String[] array = groupCode.split(",");
//			sql.append("   AND G.GROUP_ID IN --如果选择物料组\n");  
//			sql.append("       (SELECT G.GROUP_ID\n");  
//			sql.append("          FROM TM_VHCL_MATERIAL_GROUP G\n");  
//			sql.append("         START WITH G.GROUP_CODE IN (\n"); 
//			for (int i = 0; i < array.length; i++) {
//				sql.append("'" + array[i] + "'");
//				if (i != array.length - 1) {
//					sql.append(",");
//				}
//			}
//			sql.append(")\n");
//			sql.append("        CONNECT BY PRIOR G.GROUP_ID = G.PARENT_GROUP_ID)\n");  
//		}
//		if (null != resStatus && "0".equals(resStatus)) {//有
//			sql.append("   AND VWR.RESOURCE_AMOUNT > 0 --如果选择“有”\n");  
//		}
//		if (null != resStatus && "1".equals(resStatus)) {//无
//			sql.append("   AND (VWR.RESOURCE_AMOUNT <= 0  OR VWR.RESOURCE_AMOUNT IS NULL)--如果选择“有”\n");  
//		}
//		sql.append(" ORDER BY M.MATERIAL_CODE\n");
		
		StringBuilder sql= new StringBuilder();
		sql.append("SELECT DISTINCT TVM.MATERIAL_CODE,\n" );
		sql.append("       TVM.MATERIAL_NAME,\n" );
		sql.append("       TVM.MATERIAL_ID,\n" );
		sql.append("       TVM.COLOR_NAME,\n" );
		sql.append("       NVL(VVR.STOCK_AMOUNT, 0) RESOURCE_AMOUNT,\n" );
		sql.append("       NVL(VVR.LOCK_AMOUT, 0) REQ_AMOUNT,\n" );
		sql.append("       NVL(VVR.STOCK_AMOUNT,0)-NVL(VVR.LOCK_AMOUT, 0) AVA_STOCK,\n" );
		sql.append("       NVL(VVR.GENERAL_AMOUNT,0)-NVL(VVR.satisfy_general_order, 0) UNENTITY_STOCK\n" );
		sql.append("  FROM TM_VHCL_MATERIAL         TVM,\n" );
		sql.append("       TM_VHCL_MATERIAL_GROUP   TVMG,\n" );
		sql.append("       TM_VHCL_MATERIAL_GROUP_R TVMGR,\n" );
		sql.append("      TM_WAREHOUSE             TW,");
		sql.append("       VW_VS_RESOURCE_ENTITY_WEEK_NEW           VVR\n" );
		sql.append(" WHERE TVM.MATERIAL_ID = VVR.MATERIAL_ID\n" );
		sql.append("   AND TVMG.GROUP_ID = TVMGR.GROUP_ID\n" );
		sql.append("   AND TVM.MATERIAL_ID = TVMGR.MATERIAL_ID\n" );
		sql.append(" AND VVR.WAREHOUSE_ID = TW.WAREHOUSE_ID\n");

		if(groupCode!=null&&!"".equals(groupCode)){
			sql.append("   AND TVMG.GROUP_CODE = '"+groupCode+"'\n");
		}
		if (null != resStatus && "0".equals(resStatus)) {//有
			sql.append("   AND VVR.STOCK_AMOUNT > VVR.LOCK_AMOUT --如果选择“有”\n");  
		}
		if (null != resStatus && "1".equals(resStatus)) {//无
			sql.append("   AND (VVR.STOCK_AMOUNT <= VVR.LOCK_AMOUT)--如果选择“无”\n");  
		}
		if (null != warseId && !"".equals(warseId)) {
			sql.append("   AND TW.WAREHOUSE_ID="+warseId+"\n");  
		}
		sql.append("AND TVM.STATUS = 10011001\n" );
		sql.append("AND TVMG.STATUS = 10011001");
		
		return dao.pageQuery(sql.toString(), null, dao.getFunName(),pageSize ,curPage);
	}
	
	public static PageResult <Map<String,Object>> getDealerResourceQueryList(String resStatus,String areaId ,String groupCode,Long oemCompanyId, int pageSize,int curPage){
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT M.MATERIAL_CODE,\n");
		sql.append("       M.MATERIAL_NAME,\n");  
		sql.append("       M.COLOR_NAME,\n");  
		sql.append("       VWR.RESOURCE_AMOUNT,\n");  
		sql.append("       (CASE\n");  
		sql.append("         WHEN RESOURCE_AMOUNT > 0 THEN\n");  
		sql.append("          '有'\n");  
		sql.append("         ELSE\n");  
		sql.append("          '无'\n");  
		sql.append("       END) RAMOUNT\n");  
		sql.append("  FROM TM_VHCL_MATERIAL         M,\n");  
		sql.append("       TM_VHCL_MATERIAL_GROUP_R R,\n");  
		sql.append("       TM_VHCL_MATERIAL_GROUP   G,\n");  
		sql.append("       VW_VS_ORDER_RESOURCE     VWR\n");  
		sql.append(" WHERE M.MATERIAL_ID = R.MATERIAL_ID\n");  
		sql.append("   AND R.GROUP_ID = G.GROUP_ID\n");  
		sql.append("   AND M.STATUS = "+Constant.STATUS_ENABLE+"\n");  
		sql.append("   AND VWR.MATERIAL_ID(+) = M.MATERIAL_ID\n");  
		sql.append("   AND G.GROUP_ID IN\n");  
		sql.append("       (SELECT T1.GROUP_ID\n");  
		sql.append("          FROM TM_VHCL_MATERIAL_GROUP T1\n");  
		sql.append("         WHERE T1.STATUS = "+Constant.STATUS_ENABLE+"\n");  
		sql.append("         START WITH T1.GROUP_ID IN\n");  
		sql.append("                    (SELECT TAP.MATERIAL_GROUP_ID\n");  
		sql.append("                       FROM TM_AREA_GROUP TAP\n");  
		sql.append("                      WHERE 1 = 1\n");  
		sql.append("                        AND TAP.AREA_ID IN ("+areaId+"))\n");  
		sql.append("        CONNECT BY PRIOR T1.GROUP_ID = T1.PARENT_GROUP_ID)\n");  
		sql.append("   AND M.COMPANY_ID = "+oemCompanyId+"\n");  
		sql.append("   AND M.STATUS = "+Constant.STATUS_ENABLE+"\n");  
		sql.append("   AND M.ORDER_FLAG = "+Constant.NASTY_ORDER_REPORT_TYPE_01+"\n");  
		sql.append("   AND M.RUSH_ORDER_FLAG = "+Constant.NASTY_ORDER_REPORT_TYPE_01+"\n");  
		if (null != groupCode && !"".equals(groupCode)) {
			sql.append("   AND G.GROUP_ID IN --如果选择物料组\n");  
			sql.append("       (SELECT G.GROUP_ID\n");  
			sql.append("          FROM TM_VHCL_MATERIAL_GROUP G\n");  
			sql.append("         START WITH G.GROUP_CODE = '"+groupCode+"'\n");  
			sql.append("        CONNECT BY PRIOR G.GROUP_ID = G.PARENT_GROUP_ID)\n");  
		}
		if (null != resStatus && "0".equals(resStatus)) {//有
			sql.append("   AND VWR.RESOURCE_AMOUNT > 0 --如果选择“有”\n");  
		}
		if (null != resStatus && "1".equals(resStatus)) {//无
			sql.append("   AND (VWR.RESOURCE_AMOUNT <= 0  OR VWR.RESOURCE_AMOUNT IS NULL)--如果选择“有”\n");  
		}
		
		sql.append(" ORDER BY M.MATERIAL_CODE\n");
		
		return dao.pageQuery(sql.toString(), null, dao.getFunName(),pageSize ,curPage);
	}
	public static PageResult <Map<String,Object>> getDealerResourceQueryList2(String resStatus,String areaId ,String groupCode,Long oemCompanyId, int pageSize,int curPage){ //YH 2011.2.22
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT M.MATERIAL_CODE,\n");
		sql.append("       M.MATERIAL_NAME,\n");  
		sql.append("       M.COLOR_NAME,\n");  
		sql.append("       VWR.RESOURCE_AMOUNT,\n");  
		sql.append("       (CASE\n");  
		sql.append("         WHEN RESOURCE_AMOUNT >= 50 THEN\n");  
		sql.append("          '资源充足'\n"); 
		sql.append("         WHEN RESOURCE_AMOUNT = 0 THEN\n");  
		sql.append("          '无'\n"); 
		sql.append("         WHEN RESOURCE_AMOUNT is null THEN\n");  
		sql.append("          '无'\n");
		sql.append("         WHEN RESOURCE_AMOUNT < 0 THEN\n");  
		sql.append("          '无'\n"); 
		sql.append("         ELSE\n");  
		sql.append("          to_char(RESOURCE_AMOUNT)\n");  
		sql.append("       END) RAMOUNT\n");  
		sql.append("  FROM TM_VHCL_MATERIAL         M,\n");  
		sql.append("       TM_VHCL_MATERIAL_GROUP_R R,\n");  
		sql.append("       TM_VHCL_MATERIAL_GROUP   G,\n");  
		sql.append("       VW_VS_ORDER_RESOURCE     VWR\n");  
		sql.append(" WHERE M.MATERIAL_ID = R.MATERIAL_ID\n");  
		sql.append("   AND R.GROUP_ID = G.GROUP_ID\n");  
		sql.append("   AND M.STATUS = "+Constant.STATUS_ENABLE+"\n");  
		sql.append("   AND VWR.MATERIAL_ID(+) = M.MATERIAL_ID\n");  
		sql.append("   AND G.GROUP_ID IN\n");  
		sql.append("       (SELECT T1.GROUP_ID\n");  
		sql.append("          FROM TM_VHCL_MATERIAL_GROUP T1\n");  
		sql.append("         WHERE T1.STATUS = "+Constant.STATUS_ENABLE+"\n");  
		sql.append("         START WITH T1.GROUP_ID IN\n");  
		sql.append("                    (SELECT TAP.MATERIAL_GROUP_ID\n");  
		sql.append("                       FROM TM_AREA_GROUP TAP\n");  
		sql.append("                      WHERE 1 = 1\n");  
		sql.append("                        AND TAP.AREA_ID IN ("+areaId+"))\n");  
		sql.append("        CONNECT BY PRIOR T1.GROUP_ID = T1.PARENT_GROUP_ID)\n");  
		sql.append("   AND M.COMPANY_ID = "+oemCompanyId+"\n");  
		sql.append("   AND M.STATUS = "+Constant.STATUS_ENABLE+"\n");  
		sql.append("   AND M.ORDER_FLAG = "+Constant.NASTY_ORDER_REPORT_TYPE_01+"\n");  
		sql.append("   AND M.RUSH_ORDER_FLAG = "+Constant.NASTY_ORDER_REPORT_TYPE_01+"\n");  
		if (null != groupCode && !"".equals(groupCode)) {
			sql.append("   AND G.GROUP_ID IN --如果选择物料组\n");  
			sql.append("       (SELECT G.GROUP_ID\n");  
			sql.append("          FROM TM_VHCL_MATERIAL_GROUP G\n");  
			sql.append("         START WITH G.GROUP_CODE = '"+groupCode+"'\n");  
			sql.append("        CONNECT BY PRIOR G.GROUP_ID = G.PARENT_GROUP_ID)\n");  
		}
		if (null != resStatus && "0".equals(resStatus)) {//有
			sql.append("   AND VWR.RESOURCE_AMOUNT > 0 --如果选择“有”\n");  
		}
		if (null != resStatus && "1".equals(resStatus)) {//无
			sql.append("   AND (VWR.RESOURCE_AMOUNT <= 0  OR VWR.RESOURCE_AMOUNT IS NULL)--如果选择“有”\n");  
		}
		
		sql.append(" ORDER BY M.MATERIAL_CODE\n");
		return dao.pageQuery(sql.toString(), null, dao.getFunName(),pageSize ,curPage);
	}
	@Override
	protected PO wrapperPO(ResultSet rs, int idx) {
		// TODO Auto-generated method stub
		return null;
	}

	public String getWeekDate(String week){
		String date = "";
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT TO_CHAR(MAX(CREATE_DATE), 'YYYY-MM-DD') DD, ORDER_WEEK\n" );
		sql.append("FROM TT_VS_ORDER\n" );
		sql.append("WHERE ORDER_WEEK = ").append(week).append("\n");
		sql.append("GROUP BY ORDER_WEEK");

		Map<String, Object> map = pageQueryMap(sql.toString(), null, getFunName());
		if(map!=null&&map.size()>0){
			date = String.valueOf(map.get("DD"));
		}
		return date;
	}
	// 根据物料组编码获得物料编码
	public String getMateCode(String groCode){
		String mateCode = "" ;
		
		StringBuffer sql= new StringBuffer();
		sql.append("select tvm.MATERIAL_CODE\n");
		sql.append("  from TM_VHCL_MATERIAL         tvm,\n");  
		sql.append("       TM_VHCL_MATERIAL_GROUP   tvmg1,\n");  
		sql.append("       TM_VHCL_MATERIAL_GROUP   tvmg2,\n");  
		sql.append("       TM_VHCL_MATERIAL_GROUP   tvmg3,\n"); 
		sql.append("       TM_VHCL_MATERIAL_GROUP   tvmg4,\n"); 
		sql.append("       TM_VHCL_MATERIAL_GROUP_R tvmgr\n");  
		sql.append(" where 1 = 1\n");  
		sql.append("   and tvm.MATERIAL_ID = tvmgr.MATERIAL_ID\n");  
		sql.append("   and tvmgr.GROUP_ID = tvmg1.GROUP_ID\n");  
		sql.append("   and tvmg1.PARENT_GROUP_ID = tvmg2.GROUP_ID\n");  
		sql.append("   and tvmg2.PARENT_GROUP_ID = tvmg3.GROUP_ID\n");
		sql.append("   and tvmg3.PARENT_GROUP_ID = tvmg4.GROUP_ID\n");  
		if(!"''".equals(groCode)) {
		sql.append("   and (tvmg1.GROUP_CODE in ("+groCode+")\n");  
		sql.append("    or tvmg2.GROUP_CODE in ("+groCode+")\n");  
		sql.append("    or tvmg3.GROUP_CODE in ("+groCode+")\n");
		sql.append("    or tvmg4.GROUP_CODE in ("+groCode+"))\n");
		}

		List<Map<String,Object>> map = pageQuery(sql.toString(), null, getFunName());
		
		if(map!=null&&map.size()>0){
			for(int i=0; i<map.size(); i++) {
				mateCode += "'" +map.get(i).get("MATERIAL_CODE").toString()+"'," ;
			}
		}
		return mateCode;
	}
	
	public static Long insertLog(String reqId, Long userId) {
		TtVsResourceReserveLogPO tvrrl = new TtVsResourceReserveLogPO() ;

		Long logId = Long.parseLong(SequenceManager.getSequence("")) ;
		
		tvrrl.setLogId(logId) ;
		tvrrl.setReqId(Long.parseLong(reqId)) ;
		tvrrl.setCreateBy(userId) ;
		tvrrl.setCreateDate(new Date(System.currentTimeMillis())) ;

		dao.insert(tvrrl) ;

		return logId ;
	}

	public static Long insertDtlLog(Long logId, String reqDtlId, String batchNo, String newAmount, String oldAmount,String materialId, Long userId) {
		TtVsResourceReserveLogDtlPO tvrrld = new TtVsResourceReserveLogDtlPO() ;

		Long dtlId = Long.parseLong(SequenceManager.getSequence("")) ;

		tvrrld.setDtlId(dtlId) ;
		tvrrld.setLogId(logId) ;
		tvrrld.setReqDetailId(Long.parseLong(reqDtlId)) ;
		tvrrld.setBatchNo(batchNo) ;
		tvrrld.setMaterialId(Long.parseLong(materialId)) ;
		tvrrld.setNewAmount(Integer.parseInt(newAmount)) ;
		tvrrld.setOldAmount(Integer.parseInt(oldAmount)) ;
		tvrrld.setCreateBy(userId) ;
		tvrrld.setCreateDate(new Date(System.currentTimeMillis())) ;

		dao.insert(tvrrld) ;

		return dtlId ;
	}
	/**
	 * 获取锁定资源的明细信息
	 * @return
	 */
	public static List <Map<String,Object>> getLockResourceDetail(String material_id){
		List<Map<String,Object>> list=null;
//		StringBuilder sql= new StringBuilder();
//		sql.append("SELECT TVO.ORDER_ORG_ID,\n" );
//		sql.append("       TVDR.DLVRY_REQ_NO,\n" );
//		sql.append("       TD.DEALER_CODE,\n" );
//		sql.append("       TD.DEALER_SHORTNAME,\n" );
//		sql.append("       TVM.MATERIAL_CODE,\n" );
//		sql.append("       TVM.MATERIAL_NAME,\n" );
//		sql.append("       NVL(TVORR.AMOUNT,0)-NVL(TVORR.DELIVERY_AMOUNT,0) AMOUNT\n" );
//		sql.append("  FROM TT_VS_ORDER_RESOURCE_RESERVE TVORR,\n" );
//		sql.append("       TT_VS_DLVRY_REQ_DTL          TVDRD,\n" );
//		sql.append("       TT_VS_DLVRY_REQ              TVDR,\n" );
//		sql.append("       TT_VS_ORDER                  TVO,\n" );
//		sql.append("       TM_VHCL_MATERIAL             TVM,\n" );
//		sql.append("       TM_DEALER                    TD\n" );
//		sql.append(" WHERE TVORR.MATERIAL_ID(+) = TVM.MATERIAL_ID\n" );
//		sql.append("   AND TVORR.RESERVE_STATUS = 11581001\n" );
//		sql.append("   AND TVORR.REQ_DETAIL_ID = TVDRD.DETAIL_ID\n" );
//		sql.append("   AND TVDRD.REQ_ID = TVDR.REQ_ID\n" );
//		sql.append("   AND TVO.ORDER_ID(+) = TVDR.ORDER_ID\n" );
//		sql.append("   AND TD.DEALER_ID(+) = TVO.ORDER_ORG_ID\n" );
//		sql.append("   AND TVM.MATERIAL_ID = "+material_id+"\n" );
//		sql.append("   AND NVL(TVORR.AMOUNT,0)-NVL(TVORR.DELIVERY_AMOUNT,0)! = 0 \n" );
//		sql.append(" GROUP BY TVO.ORDER_ORG_ID,\n" );
//		sql.append("          TVDR.DLVRY_REQ_NO,\n" );
//		sql.append("          TD.DEALER_CODE,\n" );
//		sql.append("          TD.DEALER_SHORTNAME,\n" );
//		sql.append("          TVORR.AMOUNT,\n" );
//		sql.append("          TVM.MATERIAL_CODE,\n" );
//		sql.append("       TVM.MATERIAL_NAME\n" );
		//sql.append(" ORDER BY TVO.ORDER_ORG_ID DESC");
		SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMdd");
		TmDateSetPO tds=new TmDateSetPO();
		tds.setSetDate(sdf.format(new Date()));
		tds=(TmDateSetPO) dao.select(tds).get(0);
		StringBuilder sql= new StringBuilder();
		sql.append("select t.dlvry_req_no,t.dealer_code,t.dealer_shortname,t.material_code,t.material_name,t.types ,t.amount from (");
		sql.append("      SELECT TVO.ORDER_ORG_ID,\n" );
		sql.append("       TVDR.DLVRY_REQ_NO,\n" );
		sql.append("       TD.DEALER_CODE,\n" );
		sql.append("       TD.DEALER_SHORTNAME,\n" );
		sql.append("       TVM.MATERIAL_CODE,\n" );
		sql.append("       TVM.MATERIAL_NAME,\n" );
		sql.append("        '发运申请' types,");
		sql.append("       NVL(TVORR.AMOUNT,0)-NVL(TVORR.DELIVERY_AMOUNT,0) AMOUNT\n" );
		sql.append("  FROM TT_VS_ORDER_RESOURCE_RESERVE TVORR,\n" );
		sql.append("       TT_VS_DLVRY_REQ_DTL          TVDRD,\n" );
		sql.append("       TT_VS_DLVRY_REQ              TVDR,\n" );
		sql.append("       TT_VS_ORDER                  TVO,\n" );
		sql.append("       TM_VHCL_MATERIAL             TVM,\n" );
		sql.append("       TM_DEALER                    TD\n" );
		sql.append(" WHERE TVORR.MATERIAL_ID(+) = TVM.MATERIAL_ID\n" );
		sql.append("   AND TVORR.RESERVE_STATUS = 11581001\n" );
		sql.append("   AND TVORR.REQ_DETAIL_ID = TVDRD.DETAIL_ID\n" );
		sql.append("   AND TVDRD.REQ_ID = TVDR.REQ_ID\n" );
		sql.append("   AND TVO.ORDER_ID(+) = TVDR.ORDER_ID\n" );
		sql.append("   AND TD.DEALER_ID(+) = TVO.ORDER_ORG_ID\n" );
		sql.append("   AND TVM.MATERIAL_ID ="+material_id+" \n" );
		sql.append("   AND NVL(TVORR.AMOUNT,0)-NVL(TVORR.DELIVERY_AMOUNT,0)! = 0\n" );
//		sql.append(" union\n" );
//		sql.append("SELECT TVO.ORDER_ORG_ID,\n" );
//		sql.append("       TVo.order_no,\n" );
//		sql.append("       TD.DEALER_CODE,\n" );
//		sql.append("       TD.DEALER_SHORTNAME,\n" );
//		sql.append("       TVM.MATERIAL_CODE,\n" );
//		sql.append("       TVM.MATERIAL_NAME,\n" );
//		sql.append("       '常规订单',\n" );
//		sql.append("       NVL(TVOd.check_amount,0)-NVL(TVOd.CALL_AMOUNT,0) AMOUNT\n" );
//		sql.append("  FROM\n" );
//		sql.append("       TT_VS_order_DeTaiL          TVoD,\n" );
//		sql.append("       TT_VS_ORDER                  TVO,\n" );
//		sql.append("       TM_VHCL_MATERIAL             TVM,\n" );
//		sql.append("       TM_DEALER                    TD\n" );
//		sql.append(" WHERE TVOd.MATERIAL_ID(+) = TVM.MATERIAL_ID\n" );
//		sql.append("   AND TD.DEALER_ID(+) = TVO.ORDER_ORG_ID\n" );
//		sql.append("   and tvo.order_id=tvod.order_id\n" );
//		sql.append("and tvo.order_year="+tds.getSetYear()+"\n" );
//		sql.append("   and tvo.order_month="+tds.getSetMonth()+"\n" );
//		sql.append("   and tvo.order_week="+tds.getSetWeek()+"");
//		sql.append( "  and tvo.order_type="+Constant.ORDER_TYPE_01);
//		sql.append("   AND TVM.MATERIAL_ID = "+material_id+"\n" );
//		sql.append("   AND (Tvod.is_lock IS NULL OR TVOd.IS_LOCK=10041001)\n" );
//		sql.append("   AND NVL(Tvod.check_amount,0)-NVL(TVOd.CALL_AMOUNT,0)! = 0");
		sql.append(")  t order by  t.types");


		list=dao.pageQuery(sql.toString(), null, dao.getFunName());
		return list;
	}
	
	public List<Map<String, Object>> getWareHouseList(String companyId){
		StringBuilder sql= new StringBuilder();
		sql.append("SELECT TW.WAREHOUSE_ID, TW.WAREHOUSE_NAME\n" );
		sql.append("  FROM TM_WAREHOUSE TW\n" );
		sql.append(" WHERE TW.STATUS = 10011001\n" );
		sql.append("   AND TW.COMPANY_ID = "+companyId+"");
		List<Map<String, Object>> list=dao.pageQuery(sql.toString(), null, dao.getFunName());
		return list;
	}
	public static List <Map<String,Object>> getResourceDownList(String warseId,String resStatus ,String areaId ,String groupCode, Long oemCompanyId ){
		StringBuilder sql= new StringBuilder();
		sql.append("SELECT DISTINCT TVM.MATERIAL_CODE,\n" );
		sql.append("       TVM.MATERIAL_NAME,\n" );
		sql.append("       TVM.MATERIAL_ID,\n" );
		sql.append("       TVM.COLOR_NAME,\n" );
		sql.append("       NVL(VVR.STOCK_AMOUNT, 0) RESOURCE_AMOUNT,\n" );
		sql.append("       NVL(VVR.UN_DLVRY_AMOUNT, 0) REQ_AMOUNT,\n" );
		sql.append("       NVL(VVR.AVA_STOCK,0) AVA_STOCK\n" );
		sql.append("  FROM TM_VHCL_MATERIAL         TVM,\n" );
		sql.append("       TM_VHCL_MATERIAL_GROUP   TVMG,\n" );
		sql.append("       TM_VHCL_MATERIAL_GROUP_R TVMGR,\n" );
		sql.append("      TM_WAREHOUSE             TW,");
		sql.append("       Vw_Vs_Resource_Entity           VVR\n" );
		sql.append(" WHERE TVM.MATERIAL_ID = VVR.MATERIAL_ID(+)\n" );
		sql.append("   AND TVMG.GROUP_ID = TVMGR.GROUP_ID\n" );
		sql.append("   AND TVM.MATERIAL_ID = TVMGR.MATERIAL_ID\n" );
		sql.append(" AND VVR.WAREHOUSE_ID = TW.WAREHOUSE_ID(+)");

		if(groupCode!=null&&!"".equals(groupCode)){
			sql.append("   AND TVMG.GROUP_CODE = '"+groupCode+"'\n");
		}
		if (null != resStatus && "0".equals(resStatus)) {//有
			sql.append("   AND VVR.STOCK_AMOUNT > 0 --如果选择“有”\n");  
		}
		if (null != resStatus && "1".equals(resStatus)) {//无
			sql.append("   AND (VVR.STOCK_AMOUNT <= 0  OR VVR.STOCK_AMOUNT IS NULL)--如果选择“无”\n");  
		}
		if (null != warseId && !"".equals(warseId)) {
			sql.append("   AND TW.WAREHOUSE_ID="+warseId+"\n");  
		}
		sql.append("AND TVM.STATUS = 10011001\n" );
		sql.append("AND TVMG.STATUS = 10011001");
		
		return dao.pageQuery(sql.toString(), null, dao.getFunName() );
	}
	
	
	/**
	 * 未满足常规订单的明细信息
	 * @return
	 */
	public static List <Map<String,Object>> getUnFitNormalOrderDetail(String material_id){
		List<Map<String,Object>> list=null;
		StringBuilder sql= new StringBuilder();
		sql.append("SELECT TD.DEALER_CODE,\n" );
		sql.append("       TD.DEALER_SHORTNAME,\n" );
		sql.append("       TVM.MATERIAL_CODE,\n" );
		sql.append("       TVM.MATERIAL_NAME,\n" );
		sql.append("       T.ORDER_NO,\n" );
		sql.append("       T.AMOUNT UN_AMOUNT\n" );
		sql.append("  FROM VW_VS_UNENTITY_ORDER_DETAIL T,\n" );
		sql.append("       TM_DEALER                 TD,\n" );
		//sql.append("       TT_VS_ORDER               TVO,\n" );
		sql.append("       TM_VHCL_MATERIAL          TVM\n" );
		sql.append(" WHERE T.ORDER_ORG_ID = TD.DEALER_ID\n" );
		//sql.append("   AND TVO.ORDER_ID(+) = T.ORDER_ID\n" );
		sql.append("   AND TVM.MATERIAL_ID = T.MATERIAL_ID\n" );
		sql.append("   AND T.MATERIAL_ID='"+material_id+"'");
		//sql.append("   AND T.N_UN_REMAIN_AMOUNT!=0");

		
		list=dao.pageQuery(sql.toString(), null, dao.getFunName());
		return list;
	}
	
}
