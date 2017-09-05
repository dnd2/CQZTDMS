package com.infodms.dms.dao.parts.purchaseOrderManager;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.util.StringUtil;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

/**
 * 采购到货确认dao
 * @author  
 * @version  
 * @see 
 * @since 
 * @deprecated
 */
public class PurchaseArrConfirDao extends BaseDao  {
	public static Logger logger = Logger.getLogger(PurchaseOrderDao.class);

    private static final PurchaseArrConfirDao dao = new PurchaseArrConfirDao();

    private PurchaseArrConfirDao() {

    }

    public static final PurchaseArrConfirDao getInstance() {
        return dao;
    }

    @Override
    protected PO wrapperPO(ResultSet rs, int idx) {
        return null;
    }
    
    
    
    /**
	 * 查询采购到货待确认信息
	 * @param request
	 * @param loginUser
	 * @param curPage
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public PageResult<Map<String, Object>> getPurRcvInfo(RequestWrapper request, AclUserBean loginUser, Integer curPage, Integer pageSize) throws Exception{
		StringBuffer sql = new StringBuffer();
		List<Object> params = new ArrayList<Object>();
		
		String ORDER_CODE = request.getParamValue("ORDER_CODE");//订单单号
//		String VENDER_NAME = request.getParamValue("VENDER_NAME");//供应商
		String sCreateDate = request.getParamValue("sCreateDate");//制单日期开始时间
		String eCreateDate = request.getParamValue("eCreateDate");//制单日期结束时间
		
		String PART_OLDCODE = request.getParamValue("PART_OLDCODE");
		String PART_CNAME = request.getParamValue("PART_CNAME");
		String PART_CODE = request.getParamValue("PART_CODE");
		String IS_DQR = request.getParamValue("IS_DQR");
		
		sql.append(" select td.poline_id po_id,td.POLINE_ID,tm.order_code,td.part_id,td.part_oldcode,td.part_cname,td.part_code,td.unit,");
		sql.append(" (select tv.vender_name from tt_part_vender_define tv where tv.vender_id=td.vender_id and tv.state=10011001 ) vender_name,");
		sql.append(" td.BATCH_NO,");//批次号
		sql.append(" td.plan_qty,");//计划数量
		sql.append(" nvl(td.buy_qty,0) buy_qty,");//采购数量
		sql.append(" nvl(td.check_qty,0) YCON_QTY,");//已确认数量，已验收数量
//		sql.append(" nvl(td.buy_qty,0)-nvl(td.spare_qty,0) DCON_QTY,");//待确认数量
		sql.append(" nvl(td.spare_qty,0) DCON_QTY,");//待确认数量,待验收
		sql.append(" td.CREATE_DATE,td.state,decode(td.state,15041001,'部分验收',15041002,'完全验收','未验收') state_cn ");
		sql.append(" from tt_part_po_dtl td,tt_part_po_main tm ");
		sql.append(" where td.order_id=tm.order_id and BUYER_TYPE in(15061001,15061003)"); //正常采购、销售转采购
		
		if(StringUtil.notNull(IS_DQR)){
			sql.append(" AND td.state = ").append(IS_DQR);
		}
		if(StringUtil.notNull(ORDER_CODE)){
			sql.append("  AND TM.ORDER_CODE LIKE ?\n");
			params.add("%"+ORDER_CODE.toUpperCase()+"%");
		}
//		if(StringUtil.notNull(VENDER_NAME)){
//			sql.append("  AND POP.VENDER_NAME LIKE ?\n");
//			params.add("%"+VENDER_NAME+"%");
//		}
//		
		if(StringUtil.notNull(sCreateDate)){
			sql.append("  AND TO_CHAR(TD.CREATE_DATE,'YYYY-MM-DD') >= ?\n");
			params.add(sCreateDate);
		}
		if(StringUtil.notNull(eCreateDate)){
			sql.append("  AND TO_CHAR(TD.CREATE_DATE,'YYYY-MM-DD') <= ?\n");
			params.add(eCreateDate);
		}
		if(StringUtil.notNull(PART_OLDCODE)){
			sql.append("  AND TD.PART_OLDCODE LIKE ?\n");
			params.add("%"+PART_OLDCODE.toUpperCase()+"%");
		}
		if(StringUtil.notNull(PART_CNAME)){
			sql.append("  AND TD.PART_CNAME LIKE ?\n");
			params.add("%"+PART_CNAME+"%");
		}
		if(StringUtil.notNull(PART_CODE)){
			sql.append("  AND TD.PART_CODE LIKE ?\n");
			params.add("%"+PART_CODE.toUpperCase()+"%");
		}
		sql.append(" ORDER BY TM.CREATE_DATE DESC\n");

		PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), params, getFunName(), pageSize, curPage);
		return ps;
	}
	
	
	/**
	 * 查询采购到货入库
	 * @param request
	 * @param loginUser
	 * @param curPage
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	public PageResult<Map<String, Object>> getPurRcvInwhInfo(RequestWrapper request, AclUserBean loginUser, Integer curPage, Integer pageSize) throws Exception{
		StringBuffer sql = new StringBuffer();
		List<Object> params = new ArrayList<Object>();
		
		String ORDER_CODE = request.getParamValue("ORDER_CODE");
		String VENDER_NAME = request.getParamValue("VENDER_NAME");
		String sCreateDate = request.getParamValue("sCreateDate");
		String eCreateDate = request.getParamValue("eCreateDate");
		
		String PART_OLDCODE = request.getParamValue("PART_OLDCODE");
		String PART_CNAME = request.getParamValue("PART_CNAME");
		String PART_CODE = request.getParamValue("PART_CODE");
		
		String CG_PRICE_FLAG = request.getParamValue("CG_PRICE_FLAG");//是否采购价
		
		sql.append("select tp.po_id,tp.order_id,tp.order_code,tp.part_id,tp.part_oldcode,tp.part_cname,tp.part_code,");
//		sql.append(" decode(PART_TYPE,93621002,000000000000,F_GET_BATCH_NO(1)) BATCH_NO, ");
//		sql.append(" decode(PRODUCE_STATE,93621002,000000000000,tp.batch_no)  BATCH_NO, ");//批次号
		sql.append(" tp.batch_no BATCH_NO, ");//批次号
		sql.append(" tp.buy_qty,");//采购数量
		sql.append(" tp.CHECK_QTY CON_QTY,");//确认到货数=验收数量
		sql.append(" tp.IN_QTY CON_IN_QTY,");//到货入库数
//		sql.append(" tp.CHECK_QTY CDIN_QTY,");//待入库数量
		sql.append(" tp.SPAREIN_QTY CDIN_QTY,");//待入库数量
		sql.append(" tp.IN_QTY,");//总入库数量
		sql.append(" tp.wh_name,F_GET_LOC_CODE(tp.part_id,tp.WH_ID) LOC_CODE,tp.REMARK,tp.unit,tp.vender_name,");
		sql.append(" tp.SUPERIOR_PURCHASING,tp.PART_CATEGORY PART_TYPE,decode(tp.PART_CATEGORY,95711002,'辅料',95711003,'精品','配件') PART_TYPE_CN, ");
		sql.append(" tp.CREATE_DATE,tp.STATE,decode(tp.STATE,15041001,'部分验收','完全验收') STATE_CN, decode(PRINT_TIMES,0,10041002,10041001) IS_PRINT,PRINT_DATE,");
		sql.append(" tb.IS_GUARD,DECODE(tb.IS_GUARD,92241002,'是','否') IS_GUARD_CN, ");
		sql.append(" tb.BUY_PRICE ");
		sql.append(" from tt_part_oem_po tp,tt_part_buy_price tb");
		sql.append(" where 1=1 and tp.part_id=tb.part_id and tb.state = 10011001  and tp.vender_id=tb.vender_id\n ");
		sql.append(" and tp.state in(15041001,15041002) ");//部分验收和完全验收可以进行入库
		
		if(StringUtil.notNull(ORDER_CODE)){
			sql.append("  AND tp.ORDER_CODE LIKE ?\n");
			params.add("%"+ORDER_CODE.toUpperCase()+"%");
		}
		if(StringUtil.notNull(VENDER_NAME)){
			sql.append("  AND tp.VENDER_NAME LIKE ?\n");
			params.add("%"+VENDER_NAME+"%");
		}
		
		if(StringUtil.notNull(sCreateDate)){
			sql.append("  AND TO_CHAR(tp.CREATE_DATE,'YYYY-MM-DD') >= ?\n");
			params.add(sCreateDate);
		}
		if(StringUtil.notNull(eCreateDate)){
			sql.append("  AND TO_CHAR(tp.CREATE_DATE,'YYYY-MM-DD') <= ?\n");
			params.add(eCreateDate);
		}
		if(StringUtil.notNull(PART_OLDCODE)){
			sql.append("  AND tp.PART_OLDCODE LIKE ?\n");
			params.add("%"+PART_OLDCODE.toUpperCase()+"%");
		}
		if(StringUtil.notNull(PART_CNAME)){
			sql.append("  AND tp.PART_CNAME LIKE ?\n");
			params.add("%"+PART_CNAME+"%");
		}
		if(StringUtil.notNull(PART_CODE)){
			sql.append("  AND tp.PART_CODE LIKE ?\n");
			params.add("%"+PART_CODE.toUpperCase()+"%");
		}
		
//		if(StringUtil.notNull(CG_PRICE_FLAG)){
//			if(Constant.IF_TYPE_YES.toString().equals(CG_PRICE_FLAG)){
//				//有
//				sql.append("  AND (SELECT COUNT(1) FROM TT_PART_BUY_PRICE BP WHERE BP.VENDER_ID = POP.VENDER_ID AND BP.PART_ID = POP.PART_ID AND BP.STATE = 10011001) > 0\n");
//			}else{
//				//没有
//				sql.append("  AND (SELECT COUNT(1) FROM TT_PART_BUY_PRICE BP WHERE BP.VENDER_ID = POP.VENDER_ID AND BP.PART_ID = POP.PART_ID AND BP.STATE = 10011001) <= 0\n");
//			}
//		}
		sql.append(" order by tp.create_date desc");
		PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), params, getFunName(), pageSize, curPage);
		return ps;
	}
	
	/**
	 * 查询当前配件信息及其货位信息
	 * @param partId
	 * @param orgId
	 * @param whId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map queryPartAndLocationInfo(Long partId, Long orgId, Long whId) throws Exception{
		try {
            StringBuilder sql = new StringBuilder("SELECT A.BUYER_ID,A.IS_DIRECT, A.BUYER_ID, B.LOC_ID, B.LOC_CODE,B.LOC_NAME FROM TT_PART_DEFINE A, TT_PART_LOACTION_DEFINE B");
            sql.append(" WHERE A.PART_ID(+) = B.PART_ID");
            sql.append("  AND B.WH_ID =").append(whId);
            sql.append(" AND B.PART_ID=").append(partId);
            sql.append(" AND B.STATE=").append(Constant.STATUS_ENABLE);
            sql.append(" AND B.STATUS=1");
            logger.info("--------查询当前配件信息及其货位、采购员信息sql=" + sql);
            return pageQueryMap(sql.toString(), null, getFunName());
        } catch (Exception e) {
            throw e;
        }
	}
	
	
	/**
     * 根据货位编码查找货位id
     * @param locCode
     * @return
     */
    public Map<String, Object> getLoc(String locCode) {
      StringBuffer sql = new StringBuffer();
      sql.append(" SELECT LOC_ID,LOC_CODE,LOC_NAME");
      sql.append("  FROM TT_PART_LOACTION_DEFINE ");
      sql.append(" WHERE LOC_CODE = '"+locCode+"' ");
      sql.append("   AND STATE = '10011001' ");
      sql.append("   AND STATUS = '10041001' ");

      List<Map<String, Object>> list = this.pageQuery(sql.toString(), null, this.getFunName());
      if (null == list || list.size() <= 0) {
          return new HashMap();
      }
      return list.get(0);
  }
    
	/**
	 * 采购订单汇总
	 * @param request
	 * @param loginUser
	 * @param curPage
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public PageResult<Map<String, Object>> getPurcharOrderHz(RequestWrapper request, AclUserBean loginUser, Integer curPage, Integer pageSize) throws Exception{
		StringBuffer sql = new StringBuffer();
		List<Object> params = new ArrayList<Object>();
		
		String ORDER_CODE = request.getParamValue("ORDER_CODE");//采购单号
		String PLAN_CODE = request.getParamValue("PLAN_CODE");//计划单号
		String sCreateDate = request.getParamValue("sCreateDate");//订单日期
		String eCreateDate = request.getParamValue("eCreateDate");
		
		String VENDER_CODE = request.getParamValue("VENDER_CODE");//供应商编码
		String VENDER_NAME = request.getParamValue("VENDER_NAME");
		String purUserId = request.getParamValue("purUserId");//采购员

		sql.append("SELECT TEMP.*\n" );
		sql.append("  FROM (SELECT a.ORDER_CODE,\n" );
		sql.append("               a.CREATE_DATE,\n" );
		sql.append("               a.BUYER_ID,\n" );
		sql.append("               a.BUYER,\n" );
		sql.append("               c.plan_no PLAN_CODE,\n" );
		sql.append("               d.VENDER_CODE,\n" );
		sql.append("               d.VENDER_NAME,\n" );
		sql.append("               COUNT(b.PART_ID) AS PART_COUNT,\n" );
		sql.append("               SUM(b.buy_qty) AS CHECK_QTY_SUM\n" );
		sql.append("          FROM tt_part_po_main       a,\n" );
		sql.append("               tt_part_po_dtl        b,\n" );
		sql.append("               TT_PART_PLAN_SCROLL   c,\n" );
		sql.append("               tt_part_vender_define d\n" );
		sql.append("         WHERE a.order_id = b.order_id\n" );
		sql.append("           and a.plan_id = c.id(+)\n" );
		sql.append("           and b.vender_id = d.vender_id\n" );

		
		if(StringUtil.notNull(ORDER_CODE)){
			sql.append("    AND a.ORDER_CODE LIKE ?\n");
			params.add("%"+ORDER_CODE.trim().toUpperCase()+"%");
		}
		if(StringUtil.notNull(purUserId)){
			sql.append("    AND a.BUYER_ID = ?\n");
			params.add(purUserId.trim().toUpperCase());
		}
		if(StringUtil.notNull(PLAN_CODE)){
			sql.append("    AND c.plan_no LIKE ?\n");
			params.add("%"+PLAN_CODE.trim().toUpperCase()+"%");
		}
		if(StringUtil.notNull(sCreateDate)){
			sql.append("    AND TO_CHAR(a.CREATE_DATE,'YYYY-MM-DD') >= ?\n");
			params.add(sCreateDate);
		}
		if(StringUtil.notNull(eCreateDate)){
			sql.append("    AND TO_CHAR(a.CREATE_DATE,'YYYY-MM-DD') <= ?\n");
			params.add(eCreateDate);
		}
		if(StringUtil.notNull(VENDER_CODE)){
			sql.append("    AND d.vender_code LIKE ?\n");
			params.add("%"+VENDER_CODE.trim().toUpperCase()+"%");
		}
		if(StringUtil.notNull(VENDER_NAME)){
			sql.append("    AND d.vender_name LIKE ?\n");
			params.add("%"+VENDER_NAME.trim().toUpperCase()+"%");
		}
		sql.append("  GROUP BY a.ORDER_CODE,\n" );
		sql.append("           a.CREATE_DATE,\n" );
		sql.append("           a.BUYER_ID,\n" );
		sql.append("           a.BUYER,\n" );
		sql.append("           d.VENDER_CODE,\n" );
		sql.append("           d.VENDER_NAME,\n" );
		sql.append("           c.plan_no) TEMP");
		sql.append(" order by create_date desc");
		PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), params, getFunName(), pageSize, curPage);
		return ps;
	}
	/**
	 * 月度计划汇总
	 * @param request
	 * @param loginUser
	 * @param curPage
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public PageResult<Map<String, Object>> getMonthlyPlanHz(RequestWrapper request, AclUserBean loginUser, Integer curPage, Integer pageSize) throws Exception{
		StringBuffer sql = new StringBuffer();
		List<Object> params = new ArrayList<Object>();
		
		String plan_no = request.getParamValue("plan_no");
		String MYYEAR = request.getParamValue("MYYEAR");
		String MYMONTH = request.getParamValue("MYMONTH");

		sql.append("select t.plan_no, --计划单号\n" );
		sql.append("       t.id as tid, --主表id\n" );
		sql.append("       t.PLAN_TYPES, --计划类型\n" );
		sql.append("       TO_CHAR(T.CREATE_DATE,'YYYY-MM-DD')as CREATE_DATE, --创建时间\n" );
		sql.append("       u.NAME, --创建人\n" );
		sql.append("       decode(t.status, 0, '未审核', '已审核') status, --状态\n" );
		sql.append("      t.MONTH_DATE --计划日期\n" );		
		sql.append("  from TT_PART_PLAN_SCROLL t\n" );
		sql.append(" left join tc_user u on t.CREATE_BY = u.USER_ID where\n" );
		sql.append(" T.PLAN_TYPES = "+Constant.PART_PURCHASE_PLAN_TYPE_01+"\n" );

		
		if(StringUtil.notNull(MYYEAR) && StringUtil.notNull(MYMONTH)){
			sql.append("   AND t.MONTH_DATE = ?\n" );
			params.add((MYYEAR+MYMONTH).trim().toUpperCase());
		}
		if(StringUtil.notNull(plan_no)){
			sql.append("   and t.plan_no like ?\n" );
			params.add("%"+plan_no+"%");
		}
		
		PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), params, getFunName(), pageSize, curPage);
		return ps;
	}
	/**
	 * 月度计划汇总
	 * @param request
	 * @param loginUser
	 * @param curPage
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public PageResult<Map<String, Object>> getMonthlyPlanHzs(RequestWrapper request, AclUserBean loginUser, Integer curPage, Integer pageSize) throws Exception{
		StringBuffer sql = new StringBuffer();
		List<Object> params = new ArrayList<Object>();
		
		String produce_state = request.getParamValue("produce_state");
		String PART_OLDCODE = request.getParamValue("PART_OLDCODE");
		String PART_CNAME = request.getParamValue("PART_CNAME");
		String PART_CODE = request.getParamValue("PART_CODE");
		String vender_code = request.getParamValue("vender_code");
		String vender_name = request.getParamValue("vender_name");
		String plan_no = request.getParamValue("plan_no");
		String planId = request.getParamValue("planId");
		String MYYEAR = request.getParamValue("MYYEAR");
		String MYMONTH = request.getParamValue("MYMONTH");

		sql.append("select t.plan_no, --计划单号\n" );
		sql.append("       t.id as tid, --主表id\n" );
		sql.append("       t1.PLAN_NUM, --计划数量\n" );
		sql.append("       t2.part_oldcode, --配件编码\n" );
		sql.append("       t2.part_cname, --配件名称\n" );
		sql.append("       t2.PART_CODE, --配件件号\n" );
		sql.append("       t2.produce_state, --配件类型\n" );
		sql.append("       (SELECT c.code_desc FROM tc_code c where c.code_id=t2.produce_state) as TARGET,\n" );
		sql.append("       decode(t.status, 0, '未审核', '已审核') status, --状态\n" );
		sql.append("      t.MONTH_DATE, --计划日期\n" );
		sql.append("       t3.vender_code, --供应商代码\n" );
		sql.append("       t3.vender_name --供应商名称\n" );
		sql.append("  from TT_PART_PLAN_SCROLL     t,\n" );
		sql.append("       TT_PART_PLAN_SCROLL_DEL t1,\n" );
		sql.append("       tt_part_define          t2,\n" );
		sql.append("       tt_part_vender_define   t3\n" );
		sql.append(" where T.ID = T1.PLAN_ID\n" );
		sql.append("   AND T.PLAN_TYPES = "+Constant.PART_PURCHASE_PLAN_TYPE_01+"\n" );
		sql.append("   and t1.part_id = t2.part_id\n" );
		sql.append("   and t3.vender_id = t1.vender_id\n" );

		
		if(StringUtil.notNull(MYYEAR) && StringUtil.notNull(MYMONTH)){
			sql.append("   AND t.MONTH_DATE = ?\n" );
			params.add((MYYEAR+MYMONTH).trim().toUpperCase());
		}
		if(StringUtil.notNull(planId) && StringUtil.notNull(planId)){
			sql.append("   AND t.ID = ?\n" );
			params.add(planId);
		}
		if(StringUtil.notNull(plan_no)){
			sql.append("   and t.plan_no like ?\n" );
			params.add("%"+plan_no+"%");
		}
		if(StringUtil.notNull(PART_OLDCODE)){
			sql.append("   and t2.PART_OLDCODE like ?\n" );
			params.add("%"+PART_OLDCODE+"%");
		}
		if(StringUtil.notNull(PART_CNAME)){
			sql.append("   and t2.PART_CNAME like ?\n" );
			params.add("%"+PART_CNAME+"%");
		}
		if(StringUtil.notNull(PART_CODE)){
			sql.append("   and t2.PART_CODE like ?\n" );
			params.add("%"+PART_CODE+"%");
		}
		if(StringUtil.notNull(vender_code)){
			sql.append("   and t3.vender_code like ?\n" );
			params.add("%"+vender_code+"%");
		}
		if(StringUtil.notNull(vender_name)){
			sql.append("   and t3.vender_name like ?\n" );
			params.add("%"+vender_name+"%");
		}
		if(StringUtil.notNull(produce_state)){
			sql.append("   and t2.produce_state = ?");
			params.add(produce_state);
		}

		
		PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), params, getFunName(), pageSize, curPage);
		return ps;
	}
	/**
	 * 查询采购员
	 * @param request
	 * @param loginUser
	 * @param curPage
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getPurUserInfos() throws Exception{
		StringBuffer sql= new StringBuffer();
		sql.append("select t.user_id,u.name\n" );
		sql.append("  from TT_PART_USERPOSE_DEFINE t\n" );
		sql.append("  left join tc_user u\n" );
		sql.append("    on t.user_id = u.user_id\n" );
		sql.append(" where t.user_type = '2'");
		List<Map<String, Object>> list = pageQuery(sql.toString(), null, getFunName());
		return list;
	}
	/**
	 * 月度计划汇总
	 * @param request
	 * @param loginUser
	 * @param curPage
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getMonthlyPlanHzsList(String id) throws Exception{
		StringBuffer sql = new StringBuffer();

		sql.append("select t.plan_no, --计划单号\n" );
		sql.append("       t.id as tid, --主表id\n" );
		sql.append("       t1.PLAN_NUM, --计划数量\n" );
		sql.append("       t2.part_oldcode, --配件编码\n" );
		sql.append("       t2.part_cname, --配件名称\n" );
		sql.append("       t2.PART_CODE, --配件件号\n" );
		sql.append("       t2.produce_state, --配件类型\n" );
		sql.append("       (SELECT c.code_desc FROM tc_code c where c.code_id=t2.produce_state) as TARGET,\n" );
		sql.append("       decode(t.status, 0, '未审核', '已审核') status, --状态\n" );
		sql.append("      t.MONTH_DATE, --计划日期\n" );
		sql.append("       t3.vender_code, --供应商代码\n" );
		sql.append("       t3.vender_name --供应商名称\n" );
		sql.append("  from TT_PART_PLAN_SCROLL     t,\n" );
		sql.append("       TT_PART_PLAN_SCROLL_DEL t1,\n" );
		sql.append("       tt_part_define          t2,\n" );
		sql.append("       tt_part_vender_define   t3\n" );
		sql.append(" where T.ID = T1.PLAN_ID\n" );
		sql.append("   AND T.PLAN_TYPES = "+Constant.PART_PURCHASE_PLAN_TYPE_01+"\n" );
		sql.append("   and t1.part_id = t2.part_id\n" );
		sql.append("   and t3.vender_id = t1.vender_id\n" );
		sql.append("   and t1.plan_id = "+id+"\n" );
		List<Map<String, Object>> list = pageQuery(sql.toString(), null, getFunName());
		return list;
	}
	/**
	 * 根据订单号获取采购订单明细
	 * @param request
	 * @param loginUser
	 * @param curPage
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public PageResult<Map<String, Object>> getPurcharOrderMx(RequestWrapper request, AclUserBean loginUser, Integer curPage, Integer pageSize) throws Exception{
		StringBuffer sql = new StringBuffer();
		List<Object> params = new ArrayList<Object>();
		
		String ORDER_CODE = request.getParamValue("ORDER_CODE");
		
		String PART_OLDCODE = request.getParamValue("PART_OLDCODE");
		String PART_CNAME = request.getParamValue("PART_CNAME");
		String PART_CODE = request.getParamValue("PART_CODE");
		

		sql.append(" SELECT a.order_code, --采购单号\n");
		sql.append("       c.plan_no PLAN_CODE, --计划号\n");
		sql.append("       e.part_oldcode, --配件编码\n");
		sql.append("       e.part_cname, --配件名称\n");
		sql.append("       e.part_code, --配件件号\n");
		sql.append("       e.unit, --单位\n");
		sql.append("       a.state, --状态\n");
		sql.append("       a.REMARK, --备注\n");
		sql.append("       b.buy_qty, --采购数量\n");
//		sql.append("       nvl(f.check_qty, 0) check_qty,--验收数量\n");//20170828屏蔽
		sql.append("       d.vender_code, --供应商编码\n");
		sql.append("       d.vender_name, --供应商名称\n");
		sql.append("       a.create_date,\n");
		sql.append("       F_GET_TCUSER_NAME(e.planer_id) planer, --计划员\n");
		sql.append("       F_GET_TCUSER_NAME(e.buyer_id) buyer--采购员\n");
		sql.append("  FROM tt_part_po_main       a,\n");
		sql.append("       tt_part_po_dtl        b,\n");
		sql.append("       TT_PART_PLAN_SCROLL   c,\n");
		sql.append("       tt_part_vender_define d,\n");
		sql.append("       tt_part_define        e\n");
//		sql.append("       ,tt_part_oem_po        f\n");//20170828屏蔽
		sql.append(" WHERE a.order_id = b.order_id\n");
		sql.append("   and a.plan_id = c.id(+)\n");
		sql.append("   and b.vender_id = d.vender_id\n");
		sql.append("   and b.part_id = e.part_id\n");
//		sql.append("   and f.order_id(+) = a.order_id");//20170828屏蔽

		sql.append("   AND a.ORDER_CODE = '"+ORDER_CODE+"'");
		
		if(StringUtil.notNull(PART_OLDCODE)){
			sql.append("    AND e.PART_OLDCODE LIKE ?\n");
			params.add("%"+PART_OLDCODE.trim().toUpperCase()+"%");
		}
		if(StringUtil.notNull(PART_CODE)){
			sql.append("    AND e.PART_CODE LIKE ?\n");
			params.add("%"+PART_CODE.trim().toUpperCase()+"%");
		}
		if(StringUtil.notNull(PART_CNAME)){
			sql.append("    AND e.PART_CNAME LIKE ?\n");
			params.add("%"+PART_CNAME.trim()+"%");
		}
		sql.append("ORDER BY\n");
		sql.append("  e.PART_OLDCODE ASC\n");

		PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), params, getFunName(), pageSize, curPage);
		return ps;
	}
	
	/**
	 * 查询采购订单入库-查询
	 * @param request
	 * @param loginUser
	 * @param curPage
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	public PageResult<Map<String, Object>> getPurOrderStorageInfo(RequestWrapper request, AclUserBean loginUser, Integer curPage, Integer pageSize) throws Exception{
		StringBuffer sql = new StringBuffer();
		List<Object> params = new ArrayList<Object>();
		
		String ORDER_CODE = request.getParamValue("ORDER_CODE");
		String VENDER_NAME = request.getParamValue("VENDER_NAME");
		String sCreateDate = request.getParamValue("sCreateDate");//入库开始日期
		String eCreateDate = request.getParamValue("eCreateDate");//入库结束日期
		
		String PART_OLDCODE = request.getParamValue("PART_OLDCODE");
		String PART_CNAME = request.getParamValue("PART_CNAME");
		String PART_CODE = request.getParamValue("PART_CODE");
		
		String CG_PRICE_FLAG = request.getParamValue("CG_PRICE_FLAG");//是否采购价
		
		sql.append(" SELECT TI.IN_ID,\n");
		sql.append("        TI.IN_CODE,\n");
		sql.append("        TI.CHECK_CODE,\n");
		sql.append("        TI.ORDER_CODE,\n");
		sql.append("        TI.PLAN_CODE,\n");
		sql.append("        TI.BUYER_ID,\n");
		sql.append("        TI.PRODUCE_STATE,\n");
		sql.append("        TI.PART_ID,\n");
		sql.append("        TI.PART_OLDCODE,\n");
		sql.append("        TI.PART_CNAME,\n");
		sql.append("        TI.PART_CODE,\n");
		sql.append("        TI.UNIT,\n");
		sql.append("        TI.VENDER_CODE,\n");
		sql.append("        TI.VENDER_NAME,\n");
		sql.append("        TI.BUY_PRICE,\n");
		sql.append("        TI.BUY_QTY,\n");
		sql.append("        TI.CHECK_QTY,\n");
		sql.append("        TI.IN_QTY,\n");
		sql.append("        TI.IN_AMOUNT,\n");
		sql.append("        TI.WH_NAME,\n");
		sql.append("        TI.BATCH_NO,\n");
		sql.append("        TI.LOC_CODE,\n");
		sql.append("        TI.ITEM_QTY,\n");
		sql.append("        TI.REMARK,\n");
		sql.append("        TI.IN_DATE,\n");
		sql.append("        TI.STATE,\n");
		sql.append("        TI.IS_GAUGE,\n");
		sql.append("        TI.TAX_RATE,\n");
		sql.append("        TRIM(TO_CHAR(TI.BUY_PRICE_NOTAX, '999990.00')) BUY_PRICE_NOTAX,\n");
		sql.append("        TRIM(TO_CHAR(TI.IN_AMOUNT_NOTAX, '999990.00')) IN_AMOUNT_NOTAX\n");
		sql.append("   FROM TT_PART_PO_IN TI\n");
		sql.append("  WHERE 1 = 1\n");
		
		if(StringUtil.notNull(ORDER_CODE)){
			sql.append("  AND ti.ORDER_CODE LIKE ?\n");
			params.add("%"+ORDER_CODE.toUpperCase()+"%");
		}
		if(StringUtil.notNull(VENDER_NAME)){
			sql.append("  AND ti.VENDER_NAME LIKE ?\n");
			params.add("%"+VENDER_NAME+"%");
		}
		
		if(StringUtil.notNull(sCreateDate)){
			sql.append("  AND TO_CHAR(ti.IN_DATE,'YYYY-MM-DD') >= ?\n");
			params.add(sCreateDate);
		}
		if(StringUtil.notNull(eCreateDate)){
			sql.append("  AND TO_CHAR(ti.IN_DATE,'YYYY-MM-DD') <= ?\n");
			params.add(eCreateDate);
		}
		if(StringUtil.notNull(PART_OLDCODE)){
			sql.append("  AND ti.PART_OLDCODE LIKE ?\n");
			params.add("%"+PART_OLDCODE.toUpperCase()+"%");
		}
		if(StringUtil.notNull(PART_CNAME)){
			sql.append("  AND ti.PART_CNAME LIKE ?\n");
			params.add("%"+PART_CNAME+"%");
		}
		if(StringUtil.notNull(PART_CODE)){
			sql.append("  AND ti.PART_CODE LIKE ?\n");
			params.add("%"+PART_CODE.toUpperCase()+"%");
		}
		sql.append(" ORDER BY ti.in_date desc ");
		PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), params, getFunName(), pageSize, curPage);
		return ps;
	}

	
	/**
     * 根据机构id和配件id查找库存量
     * @param locCode
     * @return
     */
    public Map<String, Object> getStockQty(Long partId,String orgId) {
      StringBuffer sql = new StringBuffer();
      sql.append(" SELECT SUM(S.ITEM_QTY) STOCK_QTY FROM TT_PART_ITEM_STOCK S WHERE S.PART_ID = "+partId);
      sql.append("  AND S.ORG_ID = '" + orgId + "'");
      sql.append("  AND S.STATE = 1");

      List<Map<String, Object>> list = this.pageQuery(sql.toString(), null, this.getFunName());
      if (null == list || list.size() <= 0) {
          return new HashMap();
      }
      return list.get(0);
  }
  /**
   * 获取明细数据中总金额
   * @param SO_ID
   * @return
   */
  public Double getSoMainAmount(Long SO_ID) {
        StringBuffer sql = new StringBuffer();
        sql.append(" select nvl(sum(buy_amount),0) AMOUNT from tt_part_so_dtl where so_id = "+SO_ID);
        List<Map<String, Object>> list = this.pageQuery(sql.toString(), null, this.getFunName());
        if (null == list || list.size() <= 0) {
            return 0d;
        }else{
        	Map<String, Object> map=list.get(0);
        	return Double.parseDouble(map.get("AMOUNT")+"");
        }
    }

  
  /**
	 * 查询入库信息
	 * @param conId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> getPurConfirmPrintInfoByConId(String po_Id)throws Exception {
		StringBuffer sql = new StringBuffer();
		
		sql.append(" SELECT TI.IN_ID,\n");
		sql.append("        TI.IN_CODE,\n");
		sql.append("        TI.CHECK_CODE,\n");
		sql.append("        TI.ORDER_CODE,\n");
		sql.append("        TI.PLAN_CODE,\n");
		sql.append("        TI.BUYER_ID,\n");
		sql.append("        TI.PRODUCE_STATE,\n");
		sql.append("        TI.PART_ID,\n");
		sql.append("        TI.PART_OLDCODE,\n");
		sql.append("        TI.PART_CNAME,\n");
		sql.append("        TI.PART_CODE,\n");
		sql.append("        TI.UNIT,\n");
		sql.append("        TI.VENDER_CODE,\n");
		sql.append("        TI.VENDER_NAME,\n");
		sql.append("        TI.BUY_PRICE,\n");
		sql.append("        TI.BUY_QTY,\n");
		sql.append("        TI.CHECK_QTY,\n");
		sql.append("        TI.IN_QTY,\n");
		sql.append("        TI.IN_AMOUNT,\n");
		sql.append("        TI.WH_NAME,\n");
		sql.append("        TI.BATCH_NO,\n");
		sql.append("        TI.LOC_CODE,\n");
		sql.append("        TI.ITEM_QTY,\n");
		sql.append("        TI.REMARK,\n");
		sql.append("        TI.IN_DATE,\n");
		sql.append("        TI.STATE,\n");
		sql.append("        TI.IS_GAUGE,\n");
		sql.append("        TI.TAX_RATE,\n");
		sql.append("        TRIM(TO_CHAR(TI.BUY_PRICE_NOTAX, '999990.00')) BUY_PRICE_NOTAX,\n");
		sql.append("        TRIM(TO_CHAR(TI.IN_AMOUNT_NOTAX, '999990.00')) IN_AMOUNT_NOTAX\n");
		sql.append("   FROM TT_PART_PO_IN TI\n");
		sql.append("  WHERE 1 = 1\n");
		sql.append("    AND TI.IN_ID = "+po_Id+"");

		Map<String, Object> map = this.pageQueryMap(sql.toString(), null, getFunName());
		return map;
	}
}
