package com.infodms.dms.dao.parts.purchaseOrderManager;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.po.TmOrgPO;
import com.infodms.dms.po.TtPartMakerDefinePO;
import com.infodms.dms.po.TtPartPoBalancePO;
import com.infodms.dms.po.TtPartPoInPO;
import com.infodms.dms.po.TtPartStockChgMianPO;
import com.infodms.dms.po.TtPartWarehouseDefinePO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.OrderCodeManager;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;
import org.apache.log4j.Logger;

import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unchecked")
public class PurchaseOrderBalanceDao extends BaseDao {

    public static Logger logger = Logger.getLogger(PurchaseOrderBalanceDao.class);
    private static final PurchaseOrderBalanceDao dao = new PurchaseOrderBalanceDao();

    private PurchaseOrderBalanceDao() {

    }

    public static final PurchaseOrderBalanceDao getInstance() {
        return dao;
    }

    @Override
    protected PO wrapperPO(ResultSet rs, int idx) {
        return null;
    }

    public List getPartWareHouseList(AclUserBean logonUser) throws Exception {
        try {
            TtPartWarehouseDefinePO po = new TtPartWarehouseDefinePO();
            po.setState(Constant.STATUS_ENABLE);
            po.setStatus(1);
            if (logonUser.getDealerId() != null) {
                po.setOrgId(CommonUtils.parseLong(logonUser.getDealerId()));
            } else {
                po.setOrgId(logonUser.getOrgId());
            }
            List list = select(po);
            return list;
        } catch (Exception e) {
            throw e;
        }

    }

    /**
     * 备件入库结算管理
     *
     * @param po
     * @param beginTime
     * @param endTime
     * @param inBeginTime
     * @param inEndTime
     * @param inName
     * @param inCode
     * @param buyer
     * @param curPage
     * @param pageSize
     * @return
     * @throws Exception
     */
    public PageResult<Map<String, Object>> queryPartOrderBalanceMngList(Integer curPage, Integer pageSize) throws Exception {

        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);

        PageResult<Map<String, Object>> ps;
        try {

            String balanceCode = CommonUtils.checkNull(request.getParamValue("BALANCE_CODE"));//结算单号
            String beginTime = CommonUtils.checkNull(request.getParamValue("beginTime"));//结算开始时间
            String endTime = CommonUtils.checkNull(request.getParamValue("endTime"));//结算结束时间
            String venderId = CommonUtils.checkNull(request.getParamValue("VENDER_ID"));//供应商id
            String state = CommonUtils.checkNull(request.getParamValue("STATE"));//状态
            String partOldCode = CommonUtils.checkNull(request.getParamValue("DPART_OLDCODE"));//备件编码
            String partCname = CommonUtils.checkNull(request.getParamValue("DPART_CNAME"));//备件名称
            String partCode = CommonUtils.checkNull(request.getParamValue("DPART_CODE"));//备件件号
            String balancerId = CommonUtils.checkNull(request.getParamValue("BALANCER_ID"));//结算员id
            String invoNo1 = CommonUtils.checkNull(request.getParamValue("INVO_NO1"));//发票号
            String ckPriceDateS = CommonUtils.checkNull(request.getParamValue("confirmBeginTime"));//
            String ckPriceDateE = CommonUtils.checkNull(request.getParamValue("confirmEndTime"));//
            String PURCHASE_WAY = CommonUtils.checkNull(request.getParamValue("PURCHASE_WAY"));//
            String partProduceState = CommonUtils.checkNull(request.getParamValue("PART_PRODUCE_STATE"));//采购方式

            StringBuffer sql = new StringBuffer("");
            sql.append("SELECT * FROM (WITH CHG AS\n");
            sql.append(" (SELECT DISTINCT PB.BALANCE_CODE, CD.CHG_ID\n");
            sql.append("    FROM TT_PART_STOCK_CHG_DTL  CD,\n");
            sql.append("         TT_PART_PO_BALANCE     PB,\n");
            sql.append("         TT_PART_STOCK_CHG_MIAN CM\n");
            sql.append("   WHERE CD.BALANCE_ID = PB.BALANCE_ID\n");
            sql.append("     AND CM.CHG_ID = CD.CHG_ID\n");
            sql.append("     AND CM.STATE <> 92501004),\n");
            sql.append("    DIA AS\n");
            sql.append(" (SELECT SUM(ROUND(L.DIFF_AMOUNT / 1.17, 2)) DIFF_AMOUNT, L.NEW_BALANCE_ID\n");
            sql.append("    FROM TT_PART_PO_BALANCE_FINAL L\n");
            sql.append("   WHERE L.NEW_BALANCE_ID IS NOT NULL\n");
            sql.append("   GROUP BY L.NEW_BALANCE_ID)");
            sql.append("SELECT T.BALANCE_CODE,\n");
            sql.append("       --U3.NAME BUYER_NAME,\n");
            sql.append("       MAX(G.CHG_ID) CHG_ID,\n");
            sql.append("       TRUNC(T.CREATE_DATE) CREATE_DATE,\n");
            sql.append("       VD.VENDER_NAME,\n");
            sql.append("       T.INVO_NO,\n");
            sql.append("       T.STATE,\n");
            sql.append("       T.CREATE_BY,\n");
			
			sql.append(" T.CK_PRICE_DATE,\n");
			sql.append("      F_GET_USERNAME(T.CK_PRICE_BY) NAME,\n");
			sql.append("T.PRODUCE_FAC,\n");
            sql.append("       MAX(S.NAME) SUB_BY,\n");
            sql.append("       MAX(T.SUB_DATE) SUB_DATE,\n");
            sql.append("       (SELECT COUNT(1)\n");
            sql.append("          FROM TT_PART_PO_BALANCE B\n");
            sql.append("         WHERE B.BALANCE_CODE = T.BALANCE_CODE) XS,\n");
            sql.append("       SUM(T.IN_QTY) IN_QTY,\n");
            sql.append("       TO_CHAR(T.INVO_AMOUNT_NOTAX, 'FM999,999,990.00') INVO_AMOUNT_NOTAX,\n");
            sql.append("       SUM(T.BAL_QTY) BAL_QTY,\n");
            sql.append("       TO_CHAR(SUM(ROUND(T.BUY_PRICE_NOTAX * T.BAL_QTY, 2)),\n");
            sql.append("               'FM999,999,990.00') IN_AMOUNT_NOTAX,\n");
            sql.append("       TO_CHAR(SUM(ROUND(T.BUY_PRICE * T.BAL_QTY, 2)), 'FM999,999,990.00') IN_AMOUNT,\n");
            sql.append("       TO_CHAR(SUM(T.BAL_AMOUNT_NOTAX), 'FM999,999,990.00') BAL_AMOUNT_NOTAX,\n");
            sql.append("       TO_CHAR(SUM(T.BAL_AMOUNT), 'FM999,999,990.00') BAL_AMOUNT,\n");
            sql.append("       TO_CHAR(SUM(T.BAL_AMOUNT_NOTAX) -\n");
            sql.append("               SUM(ROUND(T.BUY_PRICE_NOTAX * T.BAL_QTY, 2)),\n");
            sql.append("               'FM999,999,990.00') DIFF_AMOUNT,\n");
            sql.append("       TO_CHAR(T.INVO_AMOUNT_NOTAX -  (SUM(T.BAL_AMOUNT_NOTAX) + NVL(MAX(L.DIFF_AMOUNT), 0)),\n");
            sql.append("               'FM999,999,990.00') DIFF_INVO_NOTAX_AMOUNT,\n");
            sql.append("       NVL(MAX(L.DIFF_AMOUNT), 0) DIFF_AMOUNT_FINAL,\n");
            sql.append("       (SUM(T.BAL_AMOUNT_NOTAX) + NVL(MAX(L.DIFF_AMOUNT), 0)) BAL_AMOUNT_FINAL,\n");
            sql.append("       U.NAME AS BALANCER\n");
            sql.append("  FROM TT_PART_PO_BALANCE       T,\n");
            sql.append("       TT_PART_VENDER_DEFINE    VD,\n");
            sql.append("       TT_PART_DEFINE           PD,\n");
            sql.append("       TC_USER                  U,\n");
            sql.append("       TC_USER                  S,\n");
            sql.append("       CHG                      G,\n");
            sql.append("       DIA L\n");
            sql.append(" WHERE 1 = 1\n");
            sql.append("   AND T.PART_ID = PD.PART_ID\n");
            sql.append("   AND T.FCVDER_ID = VD.VENDER_ID\n");
            sql.append("   AND T.BALANCE_CODE=L.NEW_BALANCE_ID(+)\n");
            sql.append("   AND T.BALANCE_BY = U.USER_ID(+)\n");
            sql.append("   AND T.SUB_BY = S.USER_ID(+)\n");
            sql.append("   AND T.BALANCE_CODE = G.BALANCE_CODE(+)\n");

            if (logonUser.getVenderId() != null) {
                sql.append(" AND t.FCVDER_ID='" + logonUser.getVenderId() + "'\n");
            }
            if (!"".equals(PURCHASE_WAY)) {
                sql.append(" AND PD.PRODUCE_FAC=\n")
                        .append(PURCHASE_WAY).append("\n");
            }
            if (!"".equals(balanceCode)) {
                sql.append(" AND T.BALANCE_CODE LIKE '%")
                        .append(balanceCode).append("%'\n");
            }
            if (!"".equals(partOldCode)) {
                sql.append(" AND UPPER(T.PART_OLDCODE) LIKE '%")
                        .append(partOldCode.toUpperCase()).append("%'\n");
            }
            if (!"".equals(partCname)) {
                sql.append(" AND T.PART_CNAME LIKE '%")
                        .append(partCname).append("%'\n");
            }
            if (!"".equals(partCode)) {
                sql.append(" AND UPPER(T.PART_CODE) LIKE '%")
                        .append(partCode.toUpperCase()).append("%'\n");
            }
            if (!"".equals(partProduceState)) {
                sql.append("   AND PD.PRODUCE_STATE = '").append(partProduceState).append("' \n");
            }
            if (!"".equals(balancerId)) {
                sql.append(" AND T.CREATE_BY=").append(balancerId).append("\n");
            }
            if (!"".equals(invoNo1)) {

                sql.append(" AND T.INVO_NO LIKE '%").append(invoNo1).append("%'\n");
            }

            if (!"".equals(beginTime)) {
                //  sql.append(" AND TO_DATE(T.CREATE_DATE)>=").append("TO_DATE('").append(beginTime).append("','yyyy-MM-dd')");
                sql.append("  AND T.CREATE_DATE >= TO_DATE('").append(beginTime).append(" 00:00:00','yyyy-MM-dd HH24:mi:ss')  \n");
            }

            if (!"".equals(endTime)) {
                //  sql.append(" AND TO_DATE(T.CREATE_DATE)<=").append("TO_DATE('").append(endTime).append("','yyyy-MM-dd')");
                sql.append("  AND T.CREATE_DATE <= TO_DATE('").append(endTime).append(" 23:59:59','yyyy-MM-dd HH24:mi:ss')  \n");
            }

            if (!"".equals(ckPriceDateS)) {
                sql.append(" AND TRUNC(T.ck_price_date)>=").append("TO_DATE('").append(ckPriceDateS).append("','yyyy-MM-dd')\n");
            }
            if (!"".equals(ckPriceDateE)) {
                sql.append(" AND TRUNC(T.ck_price_date)<=").append("TO_DATE('").append(ckPriceDateE).append("','yyyy-MM-dd')\n");
            }
            if (!"".equals(venderId)) {
                sql.append(" AND T.FCVDER_ID=").append(venderId).append("\n");
            }

            if (!"".equals(state)) {
                sql.append(" AND T.STATE=").append(state).append("\n");
            }

            if (null != logonUser.getVenderId()) {
                sql.append("AND T.STATE NOT IN (92321005)\n");
            }
            sql.append("GROUP BY T.INVO_AMOUNT_NOTAX,\n");
            sql.append("          t.create_by,T.BALANCE_CODE,\n");
//            sql.append("         T.PRODUCE_FAC,\n");
            sql.append("         TRUNC(T.CREATE_DATE),\n");
            sql.append("         VD.VENDER_NAME,\n");
            sql.append("         T.INVO_NO,\n");
            sql.append("         T.STATE,\n");
            sql.append("         U.NAME,T.CK_PRICE_DATE,T.CK_PRICE_BY,T.PRODUCE_FAC)A");
            sql.append("  ORDER BY TRUNC(A.CREATE_DATE) DESC \n");

            ps = pageQuery(sql.toString(), null, getFunName(), pageSize,
                    curPage);
        } catch (Exception e) {
            throw e;
        }
        return ps;

    }

    public PageResult<Map<String, Object>> queryPartOrderBalanceList(RequestWrapper request,
            Integer curPage, Integer pageSize) throws Exception {
			
			PageResult<Map<String, Object>> ps;
			try {
			ActionContext act = ActionContext.getContext();
			AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
			String balanceCode = CommonUtils.checkNull(request.getParamValue("BALANCE_CODE")) == "" ? CommonUtils.checkNull(request.getParamValue("BALANCE_CODE1")) :
			CommonUtils.checkNull(request.getParamValue("BALANCE_CODE"));//结算单号
			String venderId = CommonUtils.checkNull(request.getParamValue("VENDER_ID"));//供应商id
			String state = CommonUtils.checkNull(request.getParamValue("STATE"));//状态
			String beginTime = CommonUtils.checkNull(request.getParamValue("beginTime"));//结算开始时间
			String endTime = CommonUtils.checkNull(request.getParamValue("endTime"));//结算结束时间
			String partOldCode = CommonUtils.checkNull(request.getParamValue("DPART_OLDCODE"));//备件编码
			String partCname = CommonUtils.checkNull(request.getParamValue("DPART_CNAME"));//备件名称
			String partCode = CommonUtils.checkNull(request.getParamValue("DPART_CODE"));//备件件号
			String balancerId = CommonUtils.checkNull(request.getParamValue("BALANCER_ID"));//结算员id
			String invoNo1 = CommonUtils.checkNull(request.getParamValue("INVO_NO1"));//发票号
			String ckPriceDateS = CommonUtils.checkNull(request.getParamValue("confirmBeginTime"));//
			String ckPriceDateE = CommonUtils.checkNull(request.getParamValue("confirmEndTime"));//
			String PURCHASE_WAY = CommonUtils.checkNull(request.getParamValue("PURCHASE_WAY"));//
			String partProduceState = CommonUtils.checkNull(request.getParamValue("PART_PRODUCE_STATE"));//采购方式
			String flag = CommonUtils.checkNull(request.getParamValue("FLAG"));//标志
			
			StringBuffer sql = new StringBuffer("");
			
			if ("3".equals(flag)) {
			sql.append("SELECT T.BALANCE_CODE,\n");
			sql.append("       T.PART_CODE,\n");
			sql.append("       T.PART_OLDCODE,\n");
			sql.append("       T.PART_CNAME,\n");
			sql.append("       T.FCVDERR_CODE,\n");
			sql.append("       SUM(T.BAL_AMOUNT) BAL_AMOUNT,\n");
			sql.append("       T.BUY_PRICE1_NOTAX,\n");
			sql.append("       (SELECT CODE_DESC FROM TC_CODE WHERE CODE_ID = T.PRODUCE_FAC) PRODUCE_FAC,\n");
			sql.append("       T.VENDER_NAME SJ_VENDER_NAME,\n");
			sql.append("       VD.VENDER_NAME,\n");
			sql.append("       SUM(T.BAL_QTY) BAL_QTY,\n");
			sql.append("       SUM(T.BAL_AMOUNT_NOTAX) BAL_AMOUNT_NOTAX,\n");
			sql.append("       PD.UNIT,\n");
			sql.append("       SUM(ROUND(T.BAL_AMOUNT_NOTAX * 0.17, 2)) TAX,\n");
			sql.append("       (SELECT CODE_DESC FROM TC_CODE WHERE CODE_ID = T.PRODUCE_STATE) PRODUCE_STATE,\n");
			sql.append("       (SELECT FD.FIX_NAME\n");
			sql.append("          FROM TT_PART_FIXCODE_DEFINE FD\n");
			sql.append("         WHERE FD.FIX_GOUPTYPE = '92251013'\n");
			sql.append("           AND FD.FIX_VALUE = PD.MODEL_ID) MODEL_NAME,\n");
			sql.append("       PD.BILL_PLACE\n");
			} else if ("4".equals(flag)) {
			sql.append("SELECT T.BALANCE_CODE,\n");
			sql.append("       (SELECT CODE_DESC FROM TC_CODE WHERE CODE_ID = T.PRODUCE_FAC) || '-' ||\n");
			sql.append("       (SELECT CODE_DESC FROM TC_CODE WHERE CODE_ID = T.PRODUCE_STATE) || '-' ||\n");
			sql.append("       PD.BILL_PLACE PRODUCE_FAC,\n");
			sql.append("       SUM(T.BAL_AMOUNT_NOTAX) BAL_AMOUNT_NOTAX,\n");
			sql.append("       SUM(ROUND(T.BAL_AMOUNT_NOTAX * 0.17, 2)) TAX,\n");
			sql.append("       SUM(T.BAL_AMOUNT_NOTAX + ROUND(T.BAL_AMOUNT_NOTAX * 0.17, 2)) BAL_AMOUNT\n");
			} else {
			sql.append("SELECT T.BALANCE_CODE,\n");
			sql.append("       T.BALANCE_ID || '_' BALANCE_ID,\n");
			sql.append("       T.PART_CODE,\n");
			sql.append("       T.PART_OLDCODE,\n");
			sql.append("       T.PART_CNAME,\n");
			sql.append("       T.FCVDERR_CODE,t.bal_amount,\n");
			sql.append("       T.BUY_PRICE1_NOTAX,\n");
			sql.append("       (SELECT CODE_DESC FROM TC_CODE WHERE CODE_ID = T.PRODUCE_FAC) PRODUCE_FAC,\n");
			sql.append("       T.VENDER_NAME SJ_VENDER_NAME,\n");
			sql.append("       VD.VENDER_NAME,\n");
			sql.append("       T.BAL_QTY,\n");
			sql.append("       T.BAL_AMOUNT_NOTAX,PD.UNIT,\n");
			sql.append("       ROUND(T.BAL_AMOUNT_NOTAX*" + Constant.PART_TAX_RATE + ",2) TAX,\n");
			sql.append("       T.CREATE_DATE,\n");
			sql.append("     case t.create_by  when -1 then  '自动结算'  else  '手动结算'  end state,\n");
			sql.append(" (select c.contract_number\n");
			sql.append("      from tt_part_contract_define c, tt_part_po_in c1\n");
			sql.append("      where c.state=10011001 and  c.vender_id = t.fcvder_id\n");
			sql.append("        and c.part_id = t.part_id\n");
			sql.append("       and t.in_id = c1.in_id\n");
			sql.append("      and t.in_date >= c.contract_sdate\n");
			sql.append("        and t.in_date <= c.contract_edate) contract_number,\n");
			sql.append("       (SELECT CODE_DESC FROM TC_CODE WHERE CODE_ID = T.PRODUCE_STATE) PRODUCE_STATE,\n");
			sql.append("       (SELECT FD.FIX_NAME\n");
			sql.append("          FROM TT_PART_FIXCODE_DEFINE FD\n");
			sql.append("         WHERE FD.FIX_GOUPTYPE = '92251013'\n");
			sql.append("           AND FD.FIX_VALUE = PD.MODEL_ID) MODEL_NAME,\n");
			sql.append("      to_char(t.in_date, 'yy-mm-dd') in_date,\n");
			sql.append("      t.order_code,\n");
			sql.append("       (SELECT NAME FROM TC_USER WHERE USER_ID = T.BUYER_ID) BUYER,\n");
			sql.append("       PD.BILL_PLACE\n");
			}
			sql.append("  FROM TT_PART_PO_BALANCE    T,\n");
			sql.append("       TT_PART_VENDER_DEFINE VD,\n");
			sql.append("       TT_PART_DEFINE        PD,\n");
			sql.append("       TC_USER               U\n");
			sql.append(" WHERE 1 = 1\n");
			sql.append("   AND T.PART_ID = PD.PART_ID\n");
			sql.append("   AND T.FCVDER_ID = VD.VENDER_ID");
			if (logonUser.getDealerId() != null) {
			sql.append(" and t.FCVDER_ID='" + logonUser.getVenderId() + "'\n");
			}
			sql.append("  AND T.BALANCE_BY = U.USER_ID(+)\n");
			if (!"".equals(PURCHASE_WAY)) {
			sql.append(" AND PD.PRODUCE_FAC=\n")
			.append(PURCHASE_WAY).append("\n");
			}
			if (!"".equals(balanceCode)) {
			sql.append(" AND T.BALANCE_CODE LIKE '%")
			.append(balanceCode).append("%'\n");
			}
			if (!"".equals(partOldCode)) {
			sql.append(" AND UPPER(T.PART_OLDCODE) LIKE '%")
			.append(partOldCode.toUpperCase()).append("%'\n");
			}
			if (!"".equals(partCname)) {
			sql.append(" AND T.PART_CNAME LIKE '%")
			.append(partCname).append("%'\n");
			}
			if (!"".equals(partCode)) {
			sql.append(" AND UPPER(T.PART_CODE) LIKE '%")
			.append(partCode.toUpperCase()).append("%'\n");
			}
			if (!"".equals(partProduceState)) {
			sql.append("   AND PD.PRODUCE_STATE = '").append(partProduceState).append("' \n");
			}
			if (!"".equals(balancerId)) {
			sql.append(" AND T.CREATE_BY=").append(balancerId).append("\n");
			}
			if (!"".equals(invoNo1)) {
			
			sql.append(" AND T.INVO_NO LIKE '%").append(invoNo1).append("%'\n");
			}
			
			if (!"".equals(beginTime)) {
			//  sql.append(" AND TO_DATE(T.CREATE_DATE)>=").append("TO_DATE('").append(beginTime).append("','yyyy-MM-dd')");
			sql.append("  AND T.CREATE_DATE >= TO_DATE('").append(beginTime).append(" 00:00:00','yyyy-MM-dd HH24:mi:ss')  \n");
			}
			
			if (!"".equals(endTime)) {
			//  sql.append(" AND TO_DATE(T.CREATE_DATE)<=").append("TO_DATE('").append(endTime).append("','yyyy-MM-dd')");
			sql.append("  AND T.CREATE_DATE <= TO_DATE('").append(endTime).append(" 23:59:59','yyyy-MM-dd HH24:mi:ss')  \n");
			}
			
			if (!"".equals(ckPriceDateS)) {
			sql.append(" AND TRUNC(T.ck_price_date)>=").append("TO_DATE('").append(ckPriceDateS).append("','yyyy-MM-dd')\n");
			}
			if (!"".equals(ckPriceDateE)) {
			sql.append(" AND TRUNC(T.ck_price_date)<=").append("TO_DATE('").append(ckPriceDateE).append("','yyyy-MM-dd')\n");
			}
			if (!"".equals(venderId)) {
			sql.append(" AND T.FCVDER_ID=").append(venderId).append("\n");
			}
			
			if (!"".equals(state)) {
			sql.append(" AND T.STATE=").append(state).append("\n");
			}
			
			if ("3".equals(flag)) {
			sql.append("GROUP BY T.BALANCE_CODE,\n");
			sql.append("          T.PART_CODE,\n");
			sql.append("          T.PART_OLDCODE,\n");
			sql.append("          T.PART_CNAME,\n");
			sql.append("          T.FCVDERR_CODE,\n");
			sql.append("          T.PRODUCE_FAC,\n");
			sql.append("          T.FCVDER_ID,\n");
			sql.append("          T.PART_ID,\n");
			sql.append("          T.PRODUCE_STATE,\n");
			sql.append("          PD.MODEL_ID,\n");
			sql.append("          T.BUY_PRICE1_NOTAX,\n");
			sql.append("          T.VENDER_NAME,\n");
			sql.append("          VD.VENDER_NAME,\n");
			sql.append("          PD.UNIT,\n");
			sql.append("          PD.BILL_PLACE\n");
			} else if ("4".equals(flag)) {
			sql.append("GROUP BY T.BALANCE_CODE, T.PRODUCE_FAC, T.PRODUCE_STATE, PD.BILL_PLACE\n");
			}
			
			ps = pageQuery(sql.toString(), null, getFunName(), pageSize,
			curPage);
			return ps;
			
			} catch (Exception e) {
			throw e;
			}
			
			
			}

    public List queryVenderInfoByPartId(Long partId) throws Exception {
        try {
            StringBuilder sql = new StringBuilder("SELECT D.VENDER_ID, D.VENDER_NAME, P.BUY_PRICE,P.PART_ID");
            sql.append(" FROM TT_PART_VENDER_DEFINE D, TT_PART_BUY_PRICE P");
            sql.append(" WHERE D.VENDER_ID = P.VENDER_ID AND P.PART_ID =").append(partId);
            sql.append(" AND P.STATUS = 1");
            sql.append(" AND D.STATUS = 1");
            return pageQuery(sql.toString(), null, getFunName());
        } catch (Exception e) {
            throw e;
        }
    }

    public Map queryNewPrice(Long venderId, Long partId) throws Exception {
        try {
            StringBuilder sql = new StringBuilder("SELECT B.VENDER_ID, B.VENDER_CODE, B.VENDER_NAME, C.BUY_PRICE");
            sql.append(" FROM TT_PART_VENDER_DEFINE B, TT_PART_BUY_PRICE C");
            sql.append(" WHERE B.VENDER_ID = C.VENDER_ID AND B.VENDER_ID=").append(venderId);
            sql.append(" AND C.PART_ID =").append(partId);
            //sql.append(" AND C.state ="+Constant.STATUS_ENABLE).append("\n");
            //sql.append(" AND b.state ="+Constant.STATUS_ENABLE).append("\n");
            sql.append(" AND B.status =1 and C.status=1 \n");
            return pageQueryMap(sql.toString(), null, getFunName());
        } catch (Exception e) {
            throw e;
        }
    }

    public PageResult<Map<String, Object>> queryPartOrderBalanceDetailList(
            TtPartPoBalancePO po, String planerId, String beginTime, String endTime,
            String balanceBeginTime, String balanceEndTime, String balanceName,
            String inCode, Integer curPage, Integer pageSize) throws Exception {
        PageResult<Map<String, Object>> ps;
        try {
            StringBuffer sql = new StringBuffer("");
            sql.append(" SELECT A.BALANCE_ID, \n");
            sql.append("        A.BALANCE_CODE, \n");
            sql.append("        A.ORDER_CODE, \n");
            sql.append("        A.IN_ID, \n");
            sql.append("        A.CHECK_ID, \n");
            sql.append("        A.CHECK_CODE, \n");
            sql.append("        A.IN_CODE, \n");
            sql.append("        A.CREATE_DATE, \n");
            sql.append("        A.PART_TYPE, \n");
            sql.append("        A.PART_OLDCODE, \n");
            sql.append("        A.PART_CNAME, \n");
            sql.append("        A.PART_CODE, \n");
            sql.append("        A.VENDER_ID, \n");
            sql.append("        A.VENDER_NAME, \n");
            sql.append("        D.MAKER_NAME, \n");
            sql.append("        A.PART_ID, \n");
            sql.append("        A.WH_ID, \n");
            sql.append("        A.WH_NAME, \n");
            sql.append("        A.IN_QTY, \n");
            sql.append("        A.RETURN_QTY, \n");
            sql.append("        (A.IN_QTY-A.RETURN_QTY) BALANCE_QTY, \n");
            sql.append("        TO_CHAR(A.BUY_PRICE, 'FM999,999,990.00') BUY_PRICE, \n");
            sql.append("        TO_CHAR(A.IN_AMOUNT, 'FM999,999,999,999,990.00') IN_AMOUNT, \n");
            sql.append("        TO_CHAR(A.BUY_PRICE*(A.IN_QTY-A.RETURN_QTY), 'FM999,999,999,999,990.00') BALANCE_AMOUNT, \n");
            sql.append("        A.BALANCE_DATE, \n");
            sql.append("        A.INVO_NO, \n");
            sql.append("        A.IS_BALANCES, \n");
            sql.append("        (SELECT U1.NAME FROM TC_USER U1 WHERE A.IN_BY=U1.USER_ID) IN_NAME, \n");
            sql.append("        (SELECT U2.NAME FROM TC_USER U2 WHERE A.CHECK_BY=U2.USER_ID) CHECK_NAME, \n");
            sql.append("        B.NAME BALANCE_NAME, \n");
            sql.append("        C.NAME BUYER \n");
            sql.append("   FROM TT_PART_PO_BALANCE A, TT_PART_MAKER_DEFINE D, TC_USER B, TC_USER C \n");
            sql.append("  WHERE A.MAKER_ID = D.MAKER_ID(+) \n");
            sql.append("    AND A.CREATE_BY = B.USER_ID \n");
            sql.append("    AND A.BUYER_ID = C.USER_ID \n");
            sql.append("    AND A.STATUS = 1 \n");

            if (!"".equals(po.getOrderCode())) {
                sql.append(" AND A.ORDER_CODE LIKE '%")
                        .append(po.getOrderCode()).append("%'\n");
            }

            if (!"".equals(po.getCheckCode())) {
                sql.append(" AND A.CHECK_CODE LIKE '%")
                        .append(po.getCheckCode()).append("%'\n");
            }

            if (!"".equals(inCode)) {
                sql.append(" AND A.BALANCE_CODE LIKE '%")
                        .append(inCode).append("%'\n");
            }

            if (!"".equals(balanceName)) {
                sql.append(" AND A.BALANCE_NAME LIKE '%")
                        .append(balanceName).append("%'\n");
            }

            if (!"".equals(planerId)) {
                sql.append(" AND A.BUYER_ID=")
                        .append(planerId);
            }

            if (!"".equals(beginTime)) {
                sql.append(" AND TO_DATE(A.CREATE_DATE)>=").append("TO_DATE('").append(beginTime).append("','yyyy-MM-dd')");
            }

            if (!"".equals(endTime)) {
                sql.append(" AND TO_DATE(A.CREATE_DATE)<=").append("TO_DATE('").append(endTime).append("','yyyy-MM-dd')");
            }
            if (!"".equals(balanceBeginTime)) {
                sql.append(" AND TO_DATE(A.BALANCE_DATE)>=").append("TO_DATE('").append(balanceBeginTime).append("','yyyy-MM-dd')");
            }

            if (!"".equals(balanceEndTime)) {
                sql.append(" AND TO_DATE(A.BALANCE_DATE)<=").append("TO_DATE('").append(balanceEndTime).append("','yyyy-MM-dd')");
            }

            if (po.getWhId() != null && po.getWhId() != 0) {
                sql.append(" AND A.WH_ID=").append(po.getWhId());
            }

            if (po.getPartType() != null && po.getPartType() != 0) {
                sql.append(" AND A.PART_TYPE=").append(po.getPartType());
            }

            if (po.getVenderId() != null && po.getVenderId() != 0) {
                sql.append(" AND A.VENDER_ID=").append(po.getVenderId());
            }
            if (po.getIsBalances() != null && po.getIsBalances() != 0) {
                sql.append(" AND A.IS_BALANCES=").append(po.getIsBalances());
            }

            if (!po.getPartOldcode().equals("")) {
                sql.append(" AND A.PART_OLDCODE LIKE '%")
                        .append(po.getPartOldcode()).append("%'\n");
            }
            if (!po.getPartCname().equals("") && po.getPartCname() != null) {
                sql.append(" AND A.PART_CNAME LIKE '%")
                        .append(po.getPartCname()).append("%'\n");
            }
            if (!po.getPartCode().equals("") && po.getPartCode() != null) {
                sql.append(" AND A.PART_CODE LIKE '%")
                        .append(po.getPartCode()).append("%'\n");
            }
            if (po.getInvoNo() != null) {
                sql.append(" AND A.INVO_NO LIKE '%")
                        .append(po.getInvoNo()).append("%'\n");
            }
            sql.append(" ORDER BY A.VENDER_ID,A.PART_TYPE, A.CREATE_DATE ASC");
            ps = pageQuery(sql.toString(), null, getFunName(), pageSize,
                    curPage);
        } catch (Exception e) {
            throw e;
        }
        return ps;


    }

    public Map getVerByInId(Long inId) throws Exception {
        try {
            StringBuffer sql = new StringBuffer("");
            sql.append("SELECT T.VER,T.IN_ID FROM TT_PART_PO_IN T WHERE T.IN_ID=").append(inId);
            return pageQueryMap(sql.toString(), null, getFunName());
        } catch (Exception e) {
            throw e;
        }
    }

    public void update(TtPartPoInPO po) throws Exception {
        try {
            StringBuffer sql = new StringBuffer("");
            List params = new ArrayList();
            if (po.getSpareQty() == 0) {//如果待结算数量为0，证明已经全部结算
                sql.append("UPDATE TT_PART_PO_IN T SET T.STATE=?,T.BAL_QTY=?,T.SPARE_QTY=? WHERE T.IN_ID=?");
                params.add(po.getState());
                params.add(po.getBalQty());
                params.add(po.getSpareQty());
            } else {
                sql.append("UPDATE TT_PART_PO_IN T SET T.BAL_QTY=?,T.SPARE_QTY=? WHERE T.IN_ID=?");
                params.add(po.getBalQty());
                params.add(po.getSpareQty());
            }
            params.add(po.getInId());
            update(sql.toString(), params);
        } catch (Exception e) {
            throw e;
        }
    }

    public void updateOrderIn(Integer ifTypeYes, Long inId) throws Exception {
        try {
            StringBuffer sql = new StringBuffer("");
            sql.append("UPDATE TT_PART_PO_IN T SET T.IS_BALANCES=? where t.IN_ID=?");
            List params = new ArrayList();
            params.add(ifTypeYes);
            params.add(inId);
            update(sql.toString(), params);
        } catch (Exception e) {
            throw e;
        }
    }

    public void updateVer(TtPartPoInPO updatepo) throws Exception {
        try {
            StringBuffer sql = new StringBuffer("");
            sql.append("UPDATE TT_PART_PO_IN T SET T.VER=? WHERE T.IN_ID=?");
            List params = new ArrayList();
            params.add(updatepo.getVer());
            params.add(updatepo.getInId());
            update(sql.toString(), params);
        } catch (Exception e) {
            throw e;
        }
    }

    public List<Map<String, Object>> queryPurchaseOrderBal(
            RequestWrapper request) throws Exception {
        List<Map<String, Object>> list;
        try {

            String chkCode = CommonUtils.checkNull(request.getParamValue("CHECK_CODE"));//验收单号
            String inCode = CommonUtils.checkNull(request.getParamValue("IN_CODE"));//入库单号
            String balCode = CommonUtils.checkNull(request.getParamValue("BALANCE_CODE1"));//结算单号
            String buyer = CommonUtils.checkNull(request.getParamValue("buyer"));//采购人员
            String chkBeginTime = CommonUtils.checkNull(request.getParamValue("chkBeginTime"));//验收开始时间
            String chkEndTime = CommonUtils.checkNull(request.getParamValue("chkEndTime"));//验收结束时间
            String inBeginTime = CommonUtils.checkNull(request.getParamValue("inBeginTime"));//入库开始时间
            String inEndTime = CommonUtils.checkNull(request.getParamValue("inEndTime"));//入库结束时间
            String balBeginTime = CommonUtils.checkNull(request.getParamValue("balBeginTime"));//结算开始时间
            String balEndTime = CommonUtils.checkNull(request.getParamValue("balEndTime"));//结算结束时间
            String confirmBeginTime = CommonUtils.checkNull(request.getParamValue("confirmBeginTime"));//财务确认开始时间
            String confirmEndTime = CommonUtils.checkNull(request.getParamValue("confirmEndTime"));//财务确认结束时间
            String whId = CommonUtils.checkNull(request.getParamValue("WH_ID2"));//库房id
            String partType = CommonUtils.checkNull(request.getParamValue("PART_TYPE"));//配件种类
            String venderId = CommonUtils.checkNull(request.getParamValue("VENDER_ID2"));//供应商id
            String partOldCode = CommonUtils.checkNull(request.getParamValue("PART_OLDCODE"));//配件编码
            String partName = CommonUtils.checkNull(request.getParamValue("PART_CNAME"));//配件名称
            String partCode = CommonUtils.checkNull(request.getParamValue("PART_CODE"));//配件件号
            String state = CommonUtils.checkNull(request.getParamValue("STATE2"));//状态
            String balanceName = CommonUtils.checkNull(request.getParamValue("BALANCE_NAME"));//结算人员
            String invoNo = CommonUtils.checkNull(request.getParamValue("INVO_NO"));//发票号

            StringBuffer sql = new StringBuffer("");
            sql.append(" SELECT A.CHECK_CODE, \n");
            sql.append("        A.IN_CODE, \n");
            sql.append("        T.BALANCE_CODE, \n");
            sql.append("        T.INVO_NO, \n");
            sql.append("        A.IN_DATE, \n");
            sql.append("        T.CREATE_DATE BALANCE_DATE, \n");
            sql.append("        DECODE(A.PART_TYPE, \n").append(Constant.PART_BASE_PART_TYPES_SELF_MADE);
            sql.append(", \n");
            sql.append("               '自制件', \n");
            sql.append(Constant.PART_BASE_PART_TYPES_PURCHASE);
            sql.append(", \n");
            sql.append("               '国产件', \n");
            sql.append(Constant.PART_BASE_PART_TYPES_ENTRANCE);
            sql.append(", \n");
            sql.append("               '进口件') PART_TYPE, \n");
            sql.append("        A.PART_OLDCODE, \n");
            sql.append("        A.PART_CNAME, \n");
            sql.append("        A.PART_CODE, \n");
            sql.append("        A.VENDER_NAME, \n");
            sql.append("        D.MAKER_NAME, \n");
            sql.append("        A.WH_NAME, \n");
            sql.append("        A.IN_QTY, \n");
            sql.append("        U.NAME, \n");
            sql.append("        A.RETURN_QTY, \n");
            sql.append("        NVL(T.BAL_QTY,0) BAL_QTY, \n");
            sql.append("        TO_CHAR(A.BUY_PRICE, 'FM999,999,990.00') PLAN_PRICE, \n");
            sql.append("        TO_CHAR(A.IN_AMOUNT, \n");
            sql.append("                'FM999,999,999,999,990.00') PLAN_AMOUNT, \n");
            sql.append("        TO_CHAR(NVL(T.BUY_PRICE1,NVL((SELECT R.BUY_PRICE \n");
            sql.append("                      FROM TT_PART_BUY_PRICE R \n");
            sql.append("                     WHERE A.PART_ID = R.PART_ID \n");
            sql.append("                       AND A.VENDER_ID = R.VENDER_ID), \n");
            sql.append("                    0)), \n");
            sql.append("                'FM999,999,990.00') BUY_PRICE, \n");
            sql.append("        TO_CHAR(NVL(T.BUY_PRICE1,NVL((SELECT R.BUY_PRICE \n");
            sql.append("                      FROM TT_PART_BUY_PRICE R \n");
            sql.append("                     WHERE A.PART_ID = R.PART_ID \n");
            sql.append("                       AND A.VENDER_ID = R.VENDER_ID), \n");
            sql.append("                    0))-A.BUY_PRICE, \n");
            sql.append("                'FM999,999,990.00') DIF_PRICE, \n");
            sql.append("        TO_CHAR(NVL(T.BAL_AMOUNT,0), \n");
            sql.append("                'FM999,999,999,999,990.00') BAL_AMOUNT, \n");
            sql.append("        (SELECT B.NAME FROM TC_USER B WHERE A.IN_BY = B.USER_ID) IN_NAME, \n");
            sql.append("        (SELECT C.NAME FROM TC_USER C WHERE C.USER_ID = A.BUYER_ID) BUYER, \n");
            sql.append("        DECODE(A.STATE, \n").append(Constant.PART_PURCHASE_ORDERBALANCE_STATUS_01);
            sql.append(", \n");
            sql.append("               '待结算', \n");
            sql.append(Constant.PART_PURCHASE_ORDERBALANCE_STATUS_02);
            sql.append(", \n");
            sql.append("               '结算中', \n");
            sql.append(Constant.PART_PURCHASE_ORDERBALANCE_STATUS_04);
            sql.append(", \n");
            sql.append("               '已提交', \n");
            sql.append(Constant.PART_PURCHASE_ORDERBALANCE_STATUS_05);
            sql.append(", \n");
            sql.append("               '已作废', \n");
            sql.append(Constant.PART_PURCHASE_ORDERBALANCE_STATUS_06);
            sql.append(", \n");
            sql.append("               '已驳回', \n");
            sql.append(Constant.PART_PURCHASE_ORDERBALANCE_STATUS_03);
            sql.append(", \n");
            sql.append("               '财务已确认') STATE\n");
            sql.append("   FROM TT_PART_PO_IN A, TT_PART_MAKER_DEFINE D,TT_PART_PO_BALANCE T,TC_USER U \n");
            sql.append("  WHERE A.MAKER_ID = D.MAKER_ID(+) \n");
            sql.append("    AND A.IN_ID=T.IN_ID \n");
            sql.append("    AND T.CREATE_BY=U.USER_ID \n");
            sql.append("    AND A.STATUS = 1 \n");

            if (!"".equals(chkCode)) {
                sql.append(" AND A.CHECK_CODE LIKE '%")
                        .append(chkCode).append("%'\n");
            }

            if (!"".equals(inCode)) {
                sql.append(" AND A.IN_CODE LIKE '%")
                        .append(inCode).append("%'\n");
            }

            if (!"".equals(invoNo)) {
                sql.append(" AND T.INVO_NO LIKE '%")
                        .append(invoNo).append("%'\n");
            }
            if (!"".equals(balCode)) {
                sql.append(" AND T.BALANCE_CODE LIKE '%")
                        .append(balCode).append("%'\n");
            }
            if (!"".equals(buyer)) {
                sql.append(" AND BUYER LIKE '%")
                        .append(buyer).append("%'\n");
            }

            if (!"".equals(chkBeginTime)) {
                sql.append(" AND TO_DATE(A.CHECK_DATE)>=").append("TO_DATE('").append(chkBeginTime).append("','yyyy-MM-dd')");
            }

            if (!"".equals(chkEndTime)) {
                sql.append(" AND TO_DATE(A.CHECK_DATE)<=").append("TO_DATE('").append(chkEndTime).append("','yyyy-MM-dd')");
            }
            if (!"".equals(inBeginTime)) {
                sql.append(" AND TO_DATE(A.IN_DATE)>=").append("TO_DATE('").append(inBeginTime).append("','yyyy-MM-dd')");
            }

            if (!"".equals(inEndTime)) {
                sql.append(" AND TO_DATE(A.IN_DATE)<=").append("TO_DATE('").append(inEndTime).append("','yyyy-MM-dd')");
            }
            if (!"".equals(balBeginTime)) {
                sql.append(" AND TO_DATE(T.CREATE_DATE)>=").append("TO_DATE('").append(balBeginTime).append("','yyyy-MM-dd')");
            }

            if (!"".equals(balEndTime)) {
                sql.append(" AND TO_DATE(T.CREATE_DATE)<=").append("TO_DATE('").append(balEndTime).append("','yyyy-MM-dd')");
            }

            if (!"".equals(confirmBeginTime)) {
                sql.append(" AND TO_DATE(T.CK_PRICE_DATE)>=").append("TO_DATE('").append(confirmBeginTime).append("','yyyy-MM-dd')");
            }

            if (!"".equals(confirmEndTime)) {
                sql.append(" AND TO_DATE(T.CK_PRICE_DATE)<=").append("TO_DATE('").append(confirmEndTime).append("','yyyy-MM-dd')");
            }

            if (!"".equals(whId)) {
                sql.append(" AND A.WH_ID=").append(whId);
            }

            if (!"".equals(partType)) {
                sql.append(" AND A.PART_TYPE=").append(partType);
            }

            if (!"".equals(venderId)) {
                sql.append(" AND A.VENDER_ID=").append(venderId);
            }

            if (!partOldCode.equals("")) {
                sql.append(" AND UPPER(A.PART_OLDCODE) LIKE '%")
                        .append(partOldCode.toUpperCase()).append("%'\n");
            }
            if (!partName.equals("")) {
                sql.append(" AND A.PART_CNAME LIKE '%")
                        .append(partName).append("%'\n");
            }
            if (!partCode.equals("")) {
                sql.append(" AND UPPER(A.PART_CODE) LIKE '%")
                        .append(partCode.toUpperCase()).append("%'\n");
            }
            if (!"".equals(state)) {
                sql.append(" AND A.STATE=")
                        .append(state);
            }
            if (!"".equals(balanceName)) {
                sql.append(" AND U.NAME LIKE '%")
                        .append(balanceName).append("%'\n");
            }
            sql.append("  ORDER BY A.VENDER_ID, A.PART_TYPE, A.CREATE_DATE ASC \n");

            list = pageQuery(sql.toString(), null, getFunName());
        } catch (Exception e) {
            throw e;
        }

        return list;
    }


    public List<Map<String, Object>> queryPurchaseOrderBalDtl(
            RequestWrapper request) throws Exception {
        List<Map<String, Object>> list;
        try {

            String orderCode = CommonUtils.checkNull(request.getParamValue("ORDER_CODE2"));//采购订单号
            String chkCode = CommonUtils.checkNull(request.getParamValue("CHECK_CODE"));//验收单号
            String inCode = CommonUtils.checkNull(request.getParamValue("IN_CODE"));//接收单号
            String beginTime = CommonUtils.checkNull(request.getParamValue("beginTime"));//制单开始时间
            String endTime = CommonUtils.checkNull(request.getParamValue("endTime"));//制单结束时间
            String whId = CommonUtils.checkNull(request.getParamValue("WH_ID"));//库房id
            String partType = CommonUtils.checkNull(request.getParamValue("PART_TYPE"));//配件种类
            String venderId = CommonUtils.checkNull(request.getParamValue("VENDER_ID2"));//供应商id
            String partOldCode = CommonUtils.checkNull(request.getParamValue("PART_OLDCODE"));//配件编码
            String partName = CommonUtils.checkNull(request.getParamValue("PART_CNAME"));//配件名称
            String partCode = CommonUtils.checkNull(request.getParamValue("PART_CODE"));//配件件号
            String balanceBeginTime = CommonUtils.checkNull(request.getParamValue("balanceBeginTime"));//结算开始时间
            String balanceEndTime = CommonUtils.checkNull(request.getParamValue("balanceEndTime"));//结算结束时间
            String balanceName = CommonUtils.checkNull(request.getParamValue("BALANCE_NAME"));//结算人员
            String is_balances = CommonUtils.checkNull(request.getParamValue("IS_BALANCES"));//是否已结算
            String invo = CommonUtils.checkNull(request.getParamValue("INVO_NO"));//是否已结算
            String planerId = CommonUtils.checkNull(request.getParamValue("PLANER_ID"));//计划员

            StringBuffer sql = new StringBuffer("");
            sql.append(" SELECT A.BALANCE_ID, \n");
            sql.append("        A.BALANCE_CODE, \n");
            sql.append("        A.ORDER_CODE, \n");
            sql.append("        A.IN_ID, \n");
            sql.append("        A.CHECK_ID, \n");
            sql.append("        A.CHECK_CODE, \n");
            sql.append("        A.IN_CODE, \n");
            sql.append("        A.CREATE_DATE, \n");
            sql.append("        A.PART_TYPE, \n");
            sql.append("        A.PART_OLDCODE, \n");
            sql.append("        A.PART_CNAME, \n");
            sql.append("        A.PART_CODE, \n");
            sql.append("        A.VENDER_ID, \n");
            sql.append("        A.VENDER_NAME, \n");
            sql.append("        D.MAKER_NAME, \n");
            sql.append("        A.PART_ID, \n");
            sql.append("        A.WH_ID, \n");
            sql.append("        A.WH_NAME, \n");
            sql.append("        A.IN_QTY, \n");
            sql.append("        A.RETURN_QTY, \n");
            sql.append("        (A.IN_QTY-A.RETURN_QTY) BALANCE_QTY, \n");
            sql.append("        TO_CHAR(A.BUY_PRICE, 'FM999,999,990.00') BUY_PRICE, \n");
            sql.append("        TO_CHAR(A.IN_AMOUNT, 'FM999,999,999,999,990.00') IN_AMOUNT, \n");
            sql.append("        TO_CHAR(A.BUY_PRICE*(A.IN_QTY-A.RETURN_QTY), 'FM999,999,999,999,990.00') BALANCE_AMOUNT, \n");
            sql.append("        A.BALANCE_DATE, \n");
            sql.append("        A.INVO_NO, \n");
            sql.append("        A.IS_BALANCES, \n");
            sql.append("        (SELECT U1.NAME FROM TC_USER U1 WHERE A.IN_BY=U1.USER_ID) IN_NAME, \n");
            sql.append("        (SELECT U2.NAME FROM TC_USER U2 WHERE A.CHECK_BY=U2.USER_ID) CHECK_NAME, \n");
            sql.append("        B.NAME BALANCE_NAME, \n");
            sql.append("        C.NAME BUYER \n");
            sql.append("   FROM TT_PART_PO_BALANCE A, TT_PART_MAKER_DEFINE D, TC_USER B, TC_USER C \n");
            sql.append("  WHERE A.MAKER_ID = D.MAKER_ID(+) \n");
            sql.append("    AND A.CREATE_BY = B.USER_ID \n");
            sql.append("    AND A.BUYER_ID = C.USER_ID \n");
            sql.append("    AND A.STATUS = 1 \n");

            if (!"".equals(orderCode)) {
                sql.append(" AND A.ORDER_CODE LIKE '%")
                        .append(orderCode).append("%'\n");
            }

            if (!"".equals(chkCode)) {
                sql.append(" AND A.CHECK_CODE LIKE '%")
                        .append(chkCode).append("%'\n");
            }

            if (!"".equals(inCode)) {
                sql.append(" AND A.BALANCE_CODE LIKE '%")
                        .append(inCode).append("%'\n");
            }

            if (!"".equals(balanceName)) {
                sql.append(" AND A.BALANCE_NAME LIKE '%")
                        .append(balanceName).append("%'\n");
            }

            if (!"".equals(planerId)) {
                sql.append(" AND A.BUYER_ID=")
                        .append(planerId);
            }

            if (!"".equals(beginTime)) {
                sql.append(" AND TO_DATE(A.CREATE_DATE)>=").append("TO_DATE('").append(beginTime).append("','yyyy-MM-dd')");
            }

            if (!"".equals(endTime)) {
                sql.append(" AND TO_DATE(A.CREATE_DATE)<=").append("TO_DATE('").append(endTime).append("','yyyy-MM-dd')");
            }
            if (!"".equals(balanceBeginTime)) {
                sql.append(" AND TO_DATE(A.BALANCE_DATE)>=").append("TO_DATE('").append(balanceBeginTime).append("','yyyy-MM-dd')");
            }

            if (!"".equals(balanceEndTime)) {
                sql.append(" AND TO_DATE(A.BALANCE_DATE)<=").append("TO_DATE('").append(balanceEndTime).append("','yyyy-MM-dd')");
            }

            if (!"".equals(whId)) {
                sql.append(" AND A.WH_ID=").append(whId);
            }

            if (!"".equals(partType)) {
                sql.append(" AND A.PART_TYPE=").append(partType);
            }

            if (!"".equals(venderId)) {
                sql.append(" AND A.VENDER_ID=").append(venderId);
            }
            if (!"".equals(is_balances)) {
                sql.append(" AND A.IS_BALANCES=").append(is_balances);
            }

            if (!partOldCode.equals("")) {
                sql.append(" AND A.PART_OLDCODE LIKE '%")
                        .append(partOldCode).append("%'\n");
            }
            if (!partName.equals("")) {
                sql.append(" AND A.PART_CNAME LIKE '%")
                        .append(partName).append("%'\n");
            }
            if (!partCode.equals("")) {
                sql.append(" AND A.PART_CODE LIKE '%")
                        .append(partCode).append("%'\n");
            }
            if (!"".equals(invo)) {
                sql.append(" AND A.INVO_NO LIKE '%")
                        .append(invo).append("%'\n");
            }
            sql.append(" ORDER BY A.VENDER_ID,A.PART_TYPE, A.CREATE_DATE ASC");

            list = pageQuery(sql.toString(), null, getFunName());
        } catch (Exception e) {
            throw e;
        }

        return list;
    }

    /**
     * @param venderId
     * @return
     * @throws Exception
     */
    public Map<String, Object> querychkInfo(Long chkId) throws Exception {
        List<Object> params = new ArrayList<Object>();
        try {
            StringBuffer sql = new StringBuffer("");
            sql.append("SELECT DISTINCT T.CHK_CODE  FROM TT_PART_OEM_PO T WHERE 1 = 1 AND T.STATE = ").append(Constant.PART_PURCHASE_ORDERCHK_STATUS_01).append(" AND T.CHK_ID =  \n")
                    .append(chkId);
            return super.pageQueryMap(sql.toString(), params, super.getFunName());
        } catch (Exception e) {
            throw e;
        }
    }

    public PageResult<Map<String, Object>> queryOrderBalancePrintList(
            String balanceCode, String venderName, String invoNo, String isConf,
            Integer curPage, Integer pageSize) throws Exception {
        PageResult<Map<String, Object>> ps;
        try {
            StringBuffer sql = new StringBuffer();
            sql.append("WITH DIA AS\n");
            sql.append(" (SELECT SUM(ROUND(L.DIFF_AMOUNT / 1.17, 2)) DIFF_AMOUNT, L.NEW_BALANCE_ID\n");
            sql.append("    FROM TT_PART_PO_BALANCE_FINAL L\n");
            sql.append("   WHERE L.NEW_BALANCE_ID IS NOT NULL\n");
            sql.append("   GROUP BY L.NEW_BALANCE_ID)");
            sql.append("SELECT A.BALANCE_CODE,\n");
            sql.append("       VD.VENDER_NAME,\n");
            sql.append("       TRUNC(A.CREATE_DATE) BALANCE_DATE,\n");
            sql.append("       A.INVO_NO,\n");
            sql.append("       B.NAME BALANCE_NAME,\n");
            sql.append("       (SELECT COUNT(1)\n" +
                    "          FROM TT_PART_PO_BALANCE B\n" +
                    "         WHERE B.BALANCE_CODE = A.BALANCE_CODE) DTL_NUM,\n");
            sql.append("       TO_CHAR(SUM(ROUND(A.BUY_PRICE * BAL_QTY, 2)),\n");
            sql.append("               'FM999,999,999,999,990.00') IN_AMOUNT,\n");
            sql.append("       TO_CHAR(SUM(ROUND(A.BUY_PRICE_NOTAX * BAL_QTY, 2)),\n");
            sql.append("               'FM999,999,999,999,990.00') IN_AMOUNT_NOTAX,\n");
            sql.append("       TO_CHAR(SUM(A.BAL_AMOUNT), 'FM999,999,990.00') BAL_AMOUNT,\n");
            sql.append("       TO_CHAR(SUM(A.BAL_AMOUNT_NOTAX), 'FM999,999,990.00') BAL_AMOUNT_NOTAX,\n");
            sql.append("       TO_CHAR(SUM(A.BAL_AMOUNT_NOTAX) -\n");
            sql.append("               SUM(ROUND(A.BUY_PRICE_NOTAX * BAL_QTY, 2)),\n");
            sql.append("               'FM999,999,990.00') DIFF_AMOUNT,\n");
            sql.append("       TO_CHAR(A.INVO_AMOUNT_NOTAX, 'FM999,999,990.00') INVO_AMOUNT_NOTAX,\n");
            sql.append("       TO_CHAR(A.INVO_AMOUNT_NOTAX - SUM(BAL_AMOUNT_NOTAX),'FM999,999,990.00') INVO_DIFF_AMOUNT,\n");
            sql.append("       NVL(MAX(F.DIFF_AMOUNT), 0) DIFF_AMOUNT_FINAL,\n");
            sql.append("       (SUM(A.BAL_AMOUNT_NOTAX) +\n");
            sql.append("      NVL(MAX(F.DIFF_AMOUNT), 0))  BAL_AMOUNT_FINAL");
            sql.append("  FROM TT_PART_PO_BALANCE       A,\n");
            sql.append("       TC_USER                  B,\n");
            sql.append("       TT_PART_VENDER_DEFINE    VD,\n");
            sql.append("       DIA F\n");
            sql.append(" WHERE A.CREATE_BY = B.USER_ID(+)\n");
            sql.append("   AND A.FCVDER_ID = VD.VENDER_ID\n");
            sql.append("   AND A.STATUS = 1\n");
            sql.append("   AND A.BALANCE_CODE = F.NEW_BALANCE_ID(+)\n");
            if ("1".equals(isConf)) {
                sql.append("    AND A.STATE = ").append(Constant.PART_PURCHASE_ORDERBALANCE_STATUS_04);
            } else {
                sql.append("    AND A.STATE = ").append(Constant.PART_PURCHASE_ORDERBALANCE_STATUS_03);
            }

            if (!"".equals(balanceCode)) {
                sql.append(" AND A.BALANCE_CODE LIKE '%")
                        .append(balanceCode).append("%'\n");
            }

            if (!"".equals(venderName)) {
                sql.append(" AND exists(select * from tt_part_vender_define where vender_name LIKE '%")
                        .append(venderName).append("%' and vender_id =a.fcvder_id) \n");
            }
            if (!"".equals(invoNo)) {
                sql.append(" AND A.INVO_NO LIKE '%")
                        .append(invoNo).append("%'\n");
            }

            sql.append("  GROUP BY A.BALANCE_CODE, \n");
            sql.append("           VD.VENDER_NAME, \n");
            sql.append("           TRUNC(A.CREATE_DATE), \n");
            sql.append("           A.INVO_NO, \n");
            sql.append("           A.INVO_AMOUNT_NOTAX, \n");
            sql.append("           B.NAME \n");
            sql.append(" ORDER BY TRUNC(A.CREATE_DATE) DESC\n");

            ps = pageQuery(sql.toString(), null, getFunName(), pageSize,
                    curPage);
        } catch (Exception e) {
            throw e;
        }
        return ps;
    }

    public PageResult<Map<String, Object>> queryPartOrderBalanceDtlList(
            TtPartPoBalancePO po, String beginTime, String endTime,
            String inBeginTime, String inEndTime, String inCode,
            Integer curPage, Integer pageSize, String balanceCode) throws Exception {
        PageResult<Map<String, Object>> ps;
        try {
            StringBuffer sql = new StringBuffer("");
            sql.append(" SELECT T.CHECK_CODE, \n");
            sql.append("        T.IN_CODE, \n");
            sql.append("        T.BALANCE_DATE, \n");
            sql.append("        T.PART_TYPE, \n");
            sql.append("        T.PART_OLDCODE, \n");
            sql.append("        T.PART_CNAME, \n");
            sql.append("        T.PART_CODE, \n");
            sql.append("        T.WH_NAME, \n");
            sql.append("        T.IN_QTY, \n");
            sql.append("        T.RETURN_QTY, \n");
            sql.append("        T.BAL_QTY, \n");
            sql.append("        T.VENDER_NAME, \n");
            sql.append("        T1.MAKER_NAME, \n");
            sql.append("        TO_CHAR(T.BUY_PRICE, 'FM999,999,990.00') PLAN_PRICE, \n");
            sql.append("        TO_CHAR(T.BUY_PRICE1, 'FM999,999,990.00') BUY_PRICE, \n");
            sql.append("TO_CHAR(T.BUY_PRICE*BAL_QTY, 'FM999,999,999,999,990.00') IN_AMOUNT,\n");
            sql.append("        TO_CHAR(T.BAL_AMOUNT, 'FM999,999,999,999,990.00') BAL_AMOUNT, \n");
            sql.append("        T.IN_DATE \n");
            sql.append("   FROM TT_PART_PO_BALANCE T, TT_PART_MAKER_DEFINE T1 \n");
            sql.append("  WHERE T.MAKER_ID = T1.MAKER_ID(+) \n");
            sql.append("    AND T.BALANCE_CODE = '").append(balanceCode).append("'");

            if (!"".equals(po.getCheckCode())) {
                sql.append(" AND T.CHECK_CODE LIKE '%")
                        .append(po.getOrderCode()).append("%'\n");
            }

            if (!"".equals(inCode)) {
                sql.append(" AND T.IN_CODE LIKE '%")
                        .append(inCode).append("%'\n");
            }

            if (!"".equals(beginTime)) {
                sql.append(" AND to_date(T.BALANCE_DATE)>=").append("to_date('").append(beginTime).append("','yyyy-MM-dd')");
            }

            if (!"".equals(endTime)) {
                sql.append(" AND to_date(T.BALANCE_DATE)<=").append("to_date('").append(endTime).append("','yyyy-MM-dd')");
            }
            if (!"".equals(inBeginTime)) {
                sql.append(" AND to_date(T.IN_DATE)>=").append("to_date('").append(inBeginTime).append("','yyyy-MM-dd')");
            }

            if (!"".equals(inEndTime)) {
                sql.append(" AND to_date(T.IN_DATE)<=").append("to_date('").append(inEndTime).append("','yyyy-MM-dd')");
            }

            if (po.getWhId() != null && po.getWhId() != 0) {
                sql.append(" AND T.Wh_Id=").append(po.getWhId());
            }

//            if (po.getPartType() != null && po.getPartType() != 0) {
//                sql.append(" AND T.PART_TYPE=").append(po.getPartType());
//            }
            
            if (po.getProduceState() != null && po.getProduceState() != 0) {
                sql.append(" AND T.PRODUCE_STATE =").append(po.getProduceState());
            }

            if (po.getVenderId() != null && po.getVenderId() != 0) {
                sql.append(" AND T.VENDER_ID=").append(po.getVenderId());
            }

            if (!po.getPartOldcode().equals("")) {
                sql.append(" AND UPPER(T.PART_OLDCODE) LIKE '%")
                        .append(po.getPartOldcode().toUpperCase()).append("%'\n");
            }
            if (!po.getPartCname().equals("")) {
                sql.append(" AND T.PART_CNAME LIKE '%")
                        .append(po.getPartCname()).append("%'\n");
            }
            if (!po.getPartCode().equals("")) {
                sql.append(" AND UPPER(T.PART_CODE) LIKE '%")
                        .append(po.getPartCode().toUpperCase()).append("%'\n");
            }

            sql.append("  ORDER BY T.CREATE_DATE ASC \n");
            ps = pageQuery(sql.toString(), null, getFunName(), pageSize,
                    curPage);
        } catch (Exception e) {
            throw e;
        }
        return ps;
    }

    public Map<String, Object> queryBalanceInfoByCode(String balanceCode) throws Exception {
        Map<String, Object> map;
        try {
            StringBuffer sql = new StringBuffer("");

            sql.append(" SELECT A.BALANCE_CODE, \n");
            sql.append("        A.VENDER_ID, \n");
            sql.append("        A.VENDER_NAME, \n");
            sql.append("        TO_CHAR(A.BALANCE_DATE, 'yyyy-MM-dd') BALANCE_DATE, \n");
            sql.append("        A.INVO_NO, \n");
            sql.append("        TO_CHAR(SUM(NVL(A.BAL_AMOUNT, A.IN_AMOUNT)), \n");
            sql.append("                'FM999,999,999,999,990.00') IN_AMOUNT, \n");
            sql.append("        TO_CHAR(SUM(NVL(A.BAL_AMOUNT_NOTAX, A.IN_AMOUNT_NOTAX)), \n");
            sql.append("                'FM999,999,999,999,990.00') IN_AMOUNT_NOTAX, \n");
            sql.append("        COUNT(1) DTL_NUM \n");
            sql.append("   FROM TT_PART_PO_BALANCE A, TC_USER B \n");
            sql.append("  WHERE A.CREATE_BY = B.USER_ID \n");
            sql.append("    AND A.STATE = ").append(Constant.PART_PURCHASE_ORDERBALANCE_STATUS_03);
            sql.append("    AND A.BALANCE_CODE = '").append(balanceCode).append("' \n");
            sql.append("  GROUP BY A.BALANCE_CODE, \n");
            sql.append("           A.VENDER_ID, \n");
            sql.append("           A.VENDER_NAME, \n");
            sql.append("           TO_CHAR(A.BALANCE_DATE, 'yyyy-MM-dd'), \n");
            sql.append("           A.INVO_NO \n");

            map = pageQueryMap(sql.toString(), null, getFunName());
        } catch (Exception e) {
            throw e;
        }
        return map;
    }

    public void updateState(String code, Long userId) throws Exception {
        try {
            StringBuffer sql = new StringBuffer("");
            sql.append(" UPDATE TT_PART_PO_IN T \n");
            sql.append("    SET T.STATE =").append(Constant.PART_PURCHASE_ORDERBALANCE_STATUS_03);
            sql.append("\n");
            sql.append("  WHERE 1=1 AND EXISTS (SELECT 1\n");
            sql.append("                      FROM TT_PART_PO_BALANCE T1 \n");
            sql.append("                     WHERE T.IN_ID=T1.IN_ID AND T1.BALANCE_CODE = '").append(code);
            sql.append("') \n");
            update(sql.toString(), null);

            StringBuffer sql1 = new StringBuffer("");

            sql1.append(" UPDATE TT_PART_PO_BALANCE T \n");
            sql1.append("    SET T.STATE =").append(Constant.PART_PURCHASE_ORDERBALANCE_STATUS_03);
            sql1.append(",T.REMARK1='',T.CK_PRICE_DATE=").append("sysdate");
            sql1.append(",T.CK_PRICE_BY=").append(userId);
            sql1.append("  WHERE T.BALANCE_CODE = '").append(code).append("'");
            update(sql1.toString(), null);
            
            StringBuffer sql2 = new StringBuffer();
            sql2.append("UPDATE TT_PART_PO_BALANCE_FINAL F\n");
            sql2.append("   SET F.STATE ="+Constant.PART_PURCHASE_ORDERBALANCE_STATUS_03+", F.CHECK_DATE =SYSDATE, F.CHECK_BY ="+userId+"\n");
            sql2.append("  WHERE F.NEW_BALANCE_ID = '").append(code).append("'");
            update(sql2.toString(), null);

        } catch (Exception e) {
            throw e;
        }
    }

    public PageResult<Map<String, Object>> queryBalanceConfDtlList(
            String balanceCode, String venderName, String invoNo,
            Integer curPage, Integer pageSize) throws Exception {
        PageResult<Map<String, Object>> ps;
        try {
            StringBuffer sql = new StringBuffer("");

            sql.append(" SELECT T1.CHECK_CODE, \n");
            sql.append("        T1.IN_CODE, \n");
            sql.append("        T1.BALANCE_CODE, \n");
            sql.append("        T1.INVO_NO, \n");
            sql.append("        T1.PART_TYPE, \n");
            sql.append("        T1.PART_OLDCODE, \n");
            sql.append("        T1.PART_CNAME, \n");
            sql.append("        T1.VENDER_NAME, \n");
            sql.append("        T3.MAKER_NAME, \n");
            sql.append("        TO_CHAR(T1.IN_AMOUNT, 'FM999,999,999,999,990.00') IN_AMOUNT, \n");
            sql.append("        TO_CHAR(T1.IN_AMOUNT_NOTAX, 'FM999,999,999,999,990.00') IN_AMOUNT_NOTAX, \n");
            sql.append("        T2.NAME BALANCE_NAME, \n");
            sql.append("        T1.BALANCE_DATE \n");
            sql.append("   FROM TT_PART_PO_BALANCE T1, TC_USER T2, TT_PART_MAKER_DEFINE T3 \n");
            sql.append("  WHERE T1.BALANCE_BY = T2.USER_ID \n");
            sql.append("    AND T1.MAKER_ID = T3.MAKER_ID(+) \n");
            sql.append("    AND T1.IS_BALANCES = ").append(Constant.IF_TYPE_YES);
            sql.append("\n");

            if (!"".equals(balanceCode)) {
                sql.append(" AND T1.BALANCE_CODE LIKE '%")
                        .append(balanceCode).append("%'\n");
            }

            if (!"".equals(venderName)) {
                sql.append(" AND T1.VENDER_NAME LIKE '%")
                        .append(venderName).append("%'\n");
            }
            if (!"".equals(invoNo)) {
                sql.append(" AND T1.INVO_NO LIKE '%")
                        .append(invoNo).append("%'\n");
            }

            ps = pageQuery(sql.toString(), null, getFunName(), pageSize,
                    curPage);
        } catch (Exception e) {
            throw e;
        }
        return ps;
    }

    public List<Map<String, Object>> queryBalanceConfirmDtl(
            RequestWrapper request) throws Exception {
        List<Map<String, Object>> list;
        try {
            String balanceCode = CommonUtils.checkNull(request.getParamValue("BALANCE_CODE"));//结算单号
            String venderName = CommonUtils.checkNull(request.getParamValue("VENDER_NAME"));//供应商名称
            String invoNo = CommonUtils.checkNull(request.getParamValue("INVO_NO"));//发票号

            StringBuffer sql = new StringBuffer("");
            sql.append(" SELECT T1.CHECK_CODE, \n");
            sql.append("        T1.IN_CODE, \n");
            sql.append("        T1.BALANCE_CODE, \n");
            sql.append("        T1.INVO_NO, \n");
            sql.append("        T1.PART_TYPE, \n");
            sql.append("        T1.PART_OLDCODE, \n");
            sql.append("        T1.PART_CNAME, \n");
            sql.append("        T1.VENDER_NAME, \n");
            sql.append("        T3.MAKER_NAME, \n");
            sql.append("        TO_CHAR(T1.IN_AMOUNT, 'FM999,999,999,999,990.00') IN_AMOUNT, \n");
            sql.append("        TO_CHAR(T1.IN_AMOUNT_NOTAX, 'FM999,999,999,999,990.00') IN_AMOUNT_NOTAX, \n");
            sql.append("        T2.NAME BALANCE_NAME, \n");
            sql.append("        TO_CHAR(T1.BALANCE_DATE,'yyyy-MM-dd') BALANCE_DATE \n");
            sql.append("   FROM TT_PART_PO_BALANCE T1, TC_USER T2, TT_PART_MAKER_DEFINE T3 \n");
            sql.append("  WHERE T1.BALANCE_BY = T2.USER_ID \n");
            sql.append("    AND T1.MAKER_ID = T3.MAKER_ID(+) \n");
            sql.append("    AND T1.IS_BALANCES = ").append(Constant.IF_TYPE_YES);
            sql.append("\n");

            if (!"".equals(balanceCode)) {
                sql.append(" AND T1.BALANCE_CODE LIKE '%")
                        .append(balanceCode).append("%'\n");
            }

            if (!"".equals(venderName)) {
                sql.append(" AND T1.VENDER_NAME LIKE '%")
                        .append(venderName).append("%'\n");
            }
            if (!"".equals(invoNo)) {
                sql.append(" AND T1.INVO_NO LIKE '%")
                        .append(invoNo).append("%'\n");
            }

            list = pageQuery(sql.toString(), null, getFunName());
        } catch (Exception e) {
            throw e;
        }

        return list;
    }


    public List<Map<String, Object>> queryForeCastInfo(RequestWrapper request) throws Exception {
        List<Map<String, Object>> list;
        try {

            String partOldCode = CommonUtils.checkNull(request.getParamValue("PART_OLDCODE"));//配件编码
            String partName = CommonUtils.checkNull(request.getParamValue("PART_CNAME"));//配件名称
            String foreCastBeginTime = CommonUtils.checkNull(request.getParamValue("foreCastBeginTime"));//开始时间
            String foreCastEndTime = CommonUtils.checkNull(request.getParamValue("foreCastEndTime"));//结束时间

            StringBuffer sql = new StringBuffer("");
            sql.append(" SELECT T.PART_CODE, \n");
            sql.append("        T.PART_OLDCODE, \n");
            sql.append("        T.PART_CNAME, \n");
            sql.append("        T.PLAN_QTY, \n");
            sql.append("        D.PKG_SIZE, \n");
            sql.append("        T.FORECAST_DATE \n");
            sql.append("   FROM TT_PART_PO_MAIN T1, TT_PART_PO_DTL T, TT_PART_DEFINE D \n");
            sql.append("  WHERE T1.ORDER_ID = T.ORDER_ID \n");
            sql.append("    AND T.PART_ID = D.PART_ID \n");

            if (!partOldCode.equals("")) {
                sql.append(" AND T.PART_OLDCODE LIKE '%")
                        .append(partOldCode).append("%'\n");
            }
            if (!partName.equals("") && partName != null) {
                sql.append(" AND T.PART_CNAME LIKE '%")
                        .append(partName).append("%'\n");
            }

            if (!"".equals(foreCastBeginTime)) {
                sql.append(" AND to_date(T.FORECAST_DATE)>=").append("to_date('").append(foreCastBeginTime).append("','yyyy-MM-dd') \n");
            }

            if (!"".equals(foreCastEndTime)) {
                sql.append(" AND to_date(T.FORECAST_DATE)<=").append("to_date('").append(foreCastEndTime).append("','yyyy-MM-dd') \n");
            }

            sql.append("  ORDER BY T.PART_OLDCODE ASC, T.FORECAST_DATE ASC \n");
            list = pageQuery(sql.toString(), null, getFunName());
        } catch (Exception e) {
            throw e;
        }

        return list;
    }

    public PageResult<Map<String, Object>> queryPurOrderBalanceList(
            RequestWrapper request, Integer curPage, Integer pageSize) throws Exception {
        PageResult<Map<String, Object>> ps;
        try {
            String chkCode = CommonUtils.checkNull(request.getParamValue("CHECK_CODE"));
            String orderCode = CommonUtils.checkNull(request.getParamValue("ORDER_CODE"));
            String balanceCode = CommonUtils.checkNull(request.getParamValue("BALANCE_CODE"));
            String venderId = CommonUtils.checkNull(request.getParamValue("VENDER_ID"));
            String beginTime = CommonUtils.checkNull(request.getParamValue("beginTime"));
            String endTime = CommonUtils.checkNull(request.getParamValue("endTime"));
            String balBeginTime = CommonUtils.checkNull(request.getParamValue("balBeginTime"));
            String balEndTime = CommonUtils.checkNull(request.getParamValue("balEndTime"));
            String whId = CommonUtils.checkNull(request.getParamValue("WH_ID"));
            String partOldCode = CommonUtils.checkNull(request.getParamValue("PART_OLDCODE"));
            String partCode = CommonUtils.checkNull(request.getParamValue("PART_CODE"));
            String partTYpe = CommonUtils.checkNull(request.getParamValue("PART_TYPE"));
            String radioSelect = CommonUtils.checkNull(request.getParamValue("RADIO_SELECT"));
            String invo_no = CommonUtils.checkNull(request.getParamValue("INVO_NO"));
            String makerId = CommonUtils.checkNull(request.getParamValue("MAKER_ID"));

            StringBuffer sql = new StringBuffer("");

            if ("1".equals(radioSelect)) {

                sql.append("SELECT T1.BALANCE_CODE,\n");
                sql.append("       T1.CHECK_CODE,\n");
                sql.append("       T1.ORDER_CODE,\n");
                sql.append("       T1.PART_OLDCODE,\n");
                sql.append("       T1.PART_CNAME,\n");
                sql.append("       T1.PART_CODE,\n");
                sql.append("       T1.UNIT,\n");
                sql.append("       T1.CHECK_QTY,\n");
                sql.append("       T1.IN_QTY,\n");
                sql.append("       NVL(T1.BAL_QTY, (T1.IN_QTY - T1.RETURN_QTY)) BALANCE_QTY,\n");
                sql.append("       T1.RETURN_QTY,\n");
                sql.append("       TO_CHAR(NVL(T1.BUY_PRICE1, T1.BUY_PRICE), 'FM999,999,999,999,990.00') BUY_PRICE,\n");
                sql.append("       TO_CHAR(NVL(T1.BAL_AMOUNT,\n");
                sql.append("                   T1.BUY_PRICE * (T1.IN_QTY - T1.RETURN_QTY)),\n");
                sql.append("               'FM999,999,999,999,990.00') BALANCE_AMOUNT,\n");
                sql.append("       T3.NAME,\n");
                sql.append("       T1.CREATE_DATE,\n");
                sql.append("       T1.VENDER_NAME,\n");
                sql.append("       T2.MAKER_NAME,\n");
                sql.append("       T4.NAME PLANER,\n");
                sql.append("       T1.STATE,\n");
                sql.append("       T1.INVO_NO\n");
                sql.append("  FROM TT_PART_PO_BALANCE   T1,\n");
                sql.append("       TT_PART_MAKER_DEFINE T2,\n");
                sql.append("       TC_USER              T3,\n");
                sql.append("       TC_USER              T4\n");
                sql.append("  WHERE T1.BALANCE_TYPE=").append(Constant.PART_BALANCE_TYPE_01);
                sql.append("  AND T1.MAKER_ID = T2.MAKER_ID \n");
                sql.append("    AND T1.CREATE_BY = T3.USER_ID \n");
                sql.append("   AND T1.BUYER_ID = T4.USER_ID(+)\n");


            }

            if ("2".equals(radioSelect)) {
                sql.append(" SELECT T1.BALANCE_CODE, \n");
                sql.append("        SUM(T1.CHECK_QTY) CHECK_QTY, \n");
                sql.append("        SUM(T1.IN_QTY) IN_QTY, \n");
                sql.append("        SUM(NVL(T1.BAL_QTY,(T1.IN_QTY - T1.RETURN_QTY))) BALANCE_QTY, \n");
                sql.append("        SUM(T1.RETURN_QTY) RETURN_QTY, \n");
                sql.append("        TO_CHAR(SUM(NVL(T1.BAL_AMOUNT,T1.BUY_PRICE * (T1.IN_QTY - T1.RETURN_QTY))), \n");
                sql.append("                'FM999,999,999,999,990.00') BALANCE_AMOUNT, \n");
                sql.append("        T3.NAME, \n");
                sql.append("        T1.ORIGIN_TYPE, \n");
                sql.append("        TRUNC(T1.CREATE_DATE) CREATE_DATE,\n");
                sql.append("        T1.STATE,\n");
                sql.append("        T1.INVO_NO\n");
                sql.append("   FROM TT_PART_PO_BALANCE T1, TC_USER T3 \n");
                sql.append("  WHERE T1.BALANCE_TYPE=").append(Constant.PART_BALANCE_TYPE_01);
                sql.append(" AND T1.CREATE_BY = T3.USER_ID \n");

            }
            if (!"".equals(invo_no)) {
                sql.append("  AND T1.INVO_NO LIKE '%").append(invo_no).append("%'");
            }
            if (!"".equals(chkCode)) {
                sql.append(" AND T1.CHECK_CODE LIKE '%").append(chkCode).append("%'");
            }
            if (!"".equals(orderCode)) {
                sql.append(" AND T1.ORDER_CODE LIKE '%").append(orderCode).append("%'");
            }
            if (!"".equals(balanceCode)) {
                sql.append(" AND T1.BALANCE_CODE LIKE '%").append(balanceCode).append("%'");
            }
            if (!"".equals(partOldCode)) {
                sql.append(" AND UPPER(T1.PART_OLDCODE) LIKE '%").append(partOldCode.toUpperCase()).append("%'");
            }
            if (!"".equals(partCode)) {
                sql.append(" AND UPPER(T1.PART_CODE) LIKE '%").append(partCode.toUpperCase()).append("%'");
            }
            if (!"".equals(partTYpe)) {
                sql.append(" AND T1.PART_TYPE=").append(partTYpe);
            }
            if (!"".equals(venderId)) {
                sql.append(" AND T1.VENDER_ID=").append(venderId);
            }
            if (!"".equals(makerId)) {
                sql.append(" AND T1.MAKER_ID=").append(makerId);
            }
            if (!"".equals(beginTime)) {
                sql.append(" AND TO_DATE(T1.CHECK_DATE)>=").append("TO_DATE('").append(beginTime).append("','yyyy-MM-dd')");
            }
            if (!"".equals(endTime)) {
                sql.append(" AND TO_DATE(T1.CHECK_DATE)<=").append("TO_DATE('").append(endTime).append("','yyyy-MM-dd')");
            }
            if (!"".equals(balBeginTime)) {
                sql.append(" AND TO_DATE(T1.CREATE_DATE)>=").append("TO_DATE('").append(balBeginTime).append("','yyyy-MM-dd')");
            }
            if (!"".equals(balEndTime)) {
                sql.append(" AND TO_DATE(T1.CREATE_DATE)<=").append("TO_DATE('").append(balEndTime).append("','yyyy-MM-dd')");
            }
            if (!"".equals(whId)) {
                sql.append(" AND T1.WH_ID=").append(whId);
            }
            sql.append(" AND T1.STATE<>").append(Constant.PART_PURCHASE_ORDERBALANCE_STATUS_05);
            if ("2".equals(radioSelect)) {
                sql.append("GROUP BY T1.BALANCE_CODE,\n");
                sql.append("          T3.NAME,\n");
                sql.append("          T1.ORIGIN_TYPE,\n");
                sql.append("          TRUNC(T1.CREATE_DATE),\n");
                sql.append("          T1.STATE,\n");
                sql.append("          T1.INVO_NO\n");
            }
            sql.append("  ORDER BY T1.BALANCE_CODE DESC \n");

            ps = pageQuery(sql.toString(), null, getFunName(), pageSize,
                    curPage);
        } catch (Exception e) {
            throw e;
        }
        return ps;
    }


    /**
     * @param : @param request
     * @param : @param curPage
     * @param : @param pageSize
     * @param : @return
     * @param : @throws Exception
     * @return :
     * @throws : LastDate    : 2013-10-8
     * @Title : 领用报表查询
     */
    public PageResult<Map<String, Object>> queryRecvOrderReport(
            RequestWrapper request, Integer curPage, Integer pageSize) throws Exception {
        PageResult<Map<String, Object>> ps;
        try {
            String chkCode = CommonUtils.checkNull(request.getParamValue("CHECK_CODE"));
            String orderCode = CommonUtils.checkNull(request.getParamValue("ORDER_CODE"));
            String venderId = CommonUtils.checkNull(request.getParamValue("VENDER_ID"));
            String beginTime = CommonUtils.checkNull(request.getParamValue("beginTime"));
            String endTime = CommonUtils.checkNull(request.getParamValue("endTime"));
            String whId = CommonUtils.checkNull(request.getParamValue("WH_ID"));
            String partOldCode = CommonUtils.checkNull(request.getParamValue("PART_OLDCODE"));
            String partCode = CommonUtils.checkNull(request.getParamValue("PART_CODE"));
            String partTYpe = CommonUtils.checkNull(request.getParamValue("PART_TYPE"));
            String radioSelect = CommonUtils.checkNull(request.getParamValue("RADIO_SELECT"));
            String invo_no = CommonUtils.checkNull(request.getParamValue("INVO_NO"));
            String makerId = CommonUtils.checkNull(request.getParamValue("MAKER_ID"));
            String CheckEDate = CommonUtils.checkNull(request.getParamValue("CheckEDate"));//结算(编制)截止日期
            String CheckSDate = CommonUtils.checkNull(request.getParamValue("CheckSDate"));//结算(编制)开始日期
            String balanceCode = CommonUtils.checkNull(request.getParamValue("balanceCode"));//结算单号

            StringBuffer sql = new StringBuffer("");

            if ("1".equals(radioSelect)) {
                sql.append("SELECT T1.BALANCE_CODE,\n");
                sql.append("       T1.CHECK_CODE,\n");
                sql.append("       T1.ORDER_CODE,\n");
                sql.append("       T1.PART_OLDCODE,\n");
                sql.append("       T1.PART_CNAME,\n");
                sql.append("       T1.PART_CODE,\n");
                sql.append("       T1.UNIT,\n");
                sql.append("       T1.CHECK_QTY,\n");
                sql.append("       T1.IN_QTY,\n");
                sql.append("       NVL(T1.BAL_QTY, (T1.IN_QTY - T1.RETURN_QTY)) BALANCE_QTY,\n");
                sql.append("       T1.RETURN_QTY,\n");
                sql.append("       TO_CHAR(NVL(T1.BUY_PRICE1, T1.BUY_PRICE), 'FM999,999,999,999,990.00') BUY_PRICE,\n");
                sql.append("       TO_CHAR(NVL(T1.BAL_AMOUNT,\n");
                sql.append("                   T1.BUY_PRICE * (T1.IN_QTY - T1.RETURN_QTY)),\n");
                sql.append("               'FM999,999,999,999,990.00') BALANCE_AMOUNT,\n");
                sql.append("       T3.NAME,\n");
                sql.append("       T1.CREATE_DATE,\n");
                sql.append("       T1.VENDER_NAME,\n");
                sql.append("       T2.MAKER_NAME,\n");
                sql.append("       T4.NAME PLANER,\n");
                sql.append("       T1.INVO_NO,\n");
                sql.append("       T5.CODE_DESC\n");
                sql.append("  FROM TT_PART_PO_BALANCE   T1,\n");
                sql.append("       TT_PART_MAKER_DEFINE T2,\n");
                sql.append("       TC_USER              T3,\n");
                sql.append("       TC_USER              T4, TC_CODE T5\n");
                sql.append("  WHERE T1.BALANCE_TYPE=").append(Constant.PART_BALANCE_TYPE_02);
                sql.append("  AND T1.MAKER_ID = T2.MAKER_ID \n");
                sql.append("  AND T1.CREATE_BY = T3.USER_ID \n");
                sql.append("  AND T1.BUYER_ID = T4.USER_ID(+)\n");
                sql.append("   AND T1.STATE = T5.CODE_ID\n");
                sql.append("   AND T1.STATE <> " + Constant.PART_PURCHASE_ORDERBALANCE_STATUS_05 + "\n");
            }

            if ("2".equals(radioSelect)) {
                sql.append(" SELECT T1.BALANCE_CODE, \n");
                sql.append("        SUM(T1.CHECK_QTY) CHECK_QTY, \n");
                sql.append("        SUM(T1.IN_QTY) IN_QTY, \n");
                sql.append("        SUM(NVL(T1.BAL_QTY,(T1.IN_QTY - T1.RETURN_QTY))) BALANCE_QTY, \n");
                sql.append("        SUM(T1.RETURN_QTY) RETURN_QTY, \n");
                sql.append("        TO_CHAR(SUM(NVL(T1.BAL_AMOUNT,T1.BUY_PRICE * (T1.IN_QTY - T1.RETURN_QTY))), \n");
                sql.append("                'FM999,999,999,999,990.00') BALANCE_AMOUNT, \n");
                sql.append("        T3.NAME, \n");
                sql.append("        T1.ORIGIN_TYPE, \n");
                sql.append("        TRUNC(T1.CREATE_DATE) CREATE_DATE,\n");
                sql.append("        T1.INVO_NO,\n");
                sql.append("       T5.CODE_DESC\n");
                sql.append("   FROM TT_PART_PO_BALANCE T1, TC_USER T3, TC_CODE T5 \n");
                sql.append("  WHERE T1.BALANCE_TYPE=").append(Constant.PART_BALANCE_TYPE_02);
                sql.append(" AND T1.CREATE_BY = T3.USER_ID \n");
                sql.append("   AND T1.STATE = T5.CODE_ID\n");
                sql.append("   AND T1.STATE <> " + Constant.PART_PURCHASE_ORDERBALANCE_STATUS_05 + "\n");

            }

            if (!"".equals(invo_no)) {
                sql.append("  AND T1.INVO_NO LIKE '%").append(invo_no).append("%'");
            }
            if (!"".equals(chkCode)) {
                sql.append(" AND T1.CHECK_CODE LIKE '%").append(chkCode.trim().toUpperCase()).append("%'");
            }
            if (!"".equals(orderCode)) {
                sql.append(" AND T1.ORDER_CODE LIKE '%").append(orderCode.trim().toUpperCase()).append("%'");
            }
            if (!"".equals(partOldCode)) {
                sql.append(" AND UPPER(T1.PART_OLDCODE) LIKE '%").append(partOldCode.toUpperCase()).append("%'");
            }
            if (!"".equals(partCode)) {
                sql.append(" AND UPPER(T1.PART_CODE) LIKE '%").append(partCode.toUpperCase()).append("%'");
            }
            if (!"".equals(partTYpe)) {
                sql.append(" AND T1.PART_TYPE=").append(partTYpe);
            }
            if (!"".equals(venderId)) {
                sql.append(" AND T1.VENDER_ID=").append(venderId);
            }
            if (!"".equals(makerId)) {
                sql.append(" AND T1.MAKER_ID=").append(makerId);
            }
            if (!"".equals(beginTime)) {
                sql.append(" AND TO_DATE(T1.CHECK_DATE)>=").append("TO_DATE('").append(beginTime).append("','yyyy-MM-dd')");
            }
            if (!"".equals(endTime)) {
                sql.append(" AND TO_DATE(T1.CHECK_DATE)<=").append("TO_DATE('").append(endTime).append("','yyyy-MM-dd')");
            }
            if (!"".equals(whId)) {
                sql.append(" AND T1.WH_ID=").append(whId);
            }
            if (!"".equals(CheckSDate)) {
                sql.append(" AND TO_DATE(T1.CREATE_DATE)>=").append("TO_DATE('").append(CheckSDate).append("','yyyy-MM-dd')");
            }
            if (!"".equals(CheckEDate)) {
                sql.append(" AND TO_DATE(T1.CREATE_DATE)<=").append("TO_DATE('").append(CheckEDate).append("','yyyy-MM-dd')");
            }
            if (!"".equals(balanceCode)) {
                sql.append(" AND T1.BALANCE_CODE LIKE '%").append(balanceCode.trim().toUpperCase()).append("%'");
            }
            if ("2".equals(radioSelect)) {
                sql.append("  GROUP BY T1.BALANCE_CODE, \n");
                sql.append("           T3.NAME, \n");
                sql.append("           T1.ORIGIN_TYPE, \n");
                sql.append("           TRUNC(T1.CREATE_DATE), \n");
                sql.append("           T1.INVO_NO, T5.CODE_DESC\n");
            }
            sql.append("  ORDER BY T1.BALANCE_CODE \n");

            ps = pageQuery(sql.toString(), null, getFunName(), pageSize,
                    curPage);
        } catch (Exception e) {
            throw e;
        }
        return ps;
    }

    public PageResult<Map<String, Object>> queryPurOrderBalByCodeList(
            RequestWrapper request, Integer curPage, Integer pageSize) throws Exception {
        PageResult<Map<String, Object>> ps;
        try {
            String balanceCode = CommonUtils.checkNull(request.getParamValue("balanceCode"));
            String partOldCode = CommonUtils.checkNull(request.getParamValue("PART_OLDCODE"));
            String partCode = CommonUtils.checkNull(request.getParamValue("PART_CODE"));
            String partTYpe = CommonUtils.checkNull(request.getParamValue("PART_TYPE"));

            StringBuffer sql = new StringBuffer("");

            sql.append(" SELECT T1.BALANCE_CODE, \n");
            sql.append("        T1.CHECK_CODE, \n");
            sql.append("        T1.ORDER_CODE, \n");
            sql.append("        T1.PART_OLDCODE, \n");
            sql.append("        T1.PART_CNAME, \n");
            sql.append("        T1.PART_CODE, \n");
            sql.append("        T1.UNIT, \n");
            sql.append("        T1.CHECK_QTY, \n");
            sql.append("        T1.IN_QTY, \n");
            sql.append("        NVL(T1.BAL_QTY,(T1.IN_QTY - T1.RETURN_QTY)) BALANCE_QTY, \n");
            sql.append("        T1.RETURN_QTY, \n");
            sql.append("        TO_CHAR(NVL(T1.BUY_PRICE1,T1.BUY_PRICE), 'FM999,999,999,999,990.00') BUY_PRICE, \n");
            sql.append("        TO_CHAR(NVL(T1.BAL_AMOUNT,T1.BUY_PRICE * (T1.IN_QTY - T1.RETURN_QTY)), \n");
            sql.append("                'FM999,999,999,999,990.00') BALANCE_AMOUNT, \n");
            sql.append("        T3.NAME, \n");
            sql.append("        T1.CREATE_DATE, \n");
            sql.append("        T1.VENDER_NAME, \n");
            sql.append("        T2.MAKER_NAME \n");
            sql.append("   FROM TT_PART_PO_BALANCE T1, TT_PART_MAKER_DEFINE T2, TC_USER T3 \n");
            sql.append("  WHERE T1.MAKER_ID = T2.MAKER_ID \n");
            sql.append("    AND T1.CREATE_BY = T3.USER_ID \n");
            sql.append("    AND T1.BALANCE_CODE = '").append(balanceCode).append("'");

            if (!"".equals(partOldCode)) {
                sql.append(" AND UPPER(T1.PART_OLDCODE) LIKE '%").append(partOldCode.toUpperCase()).append("%'");
            }
            if (!"".equals(partCode)) {
                sql.append(" AND UPPER(T1.PART_CODE) LIKE '%").append(partCode.toUpperCase()).append("%'");
            }
            if (!"".equals(partTYpe)) {
                sql.append(" AND T1.PART_TYPE=").append(partTYpe);
            }

            ps = pageQuery(sql.toString(), null, getFunName(), pageSize,
                    curPage);
        } catch (Exception e) {
            throw e;
        }
        return ps;
    }

    public List<Map<String, Object>> queryPurOrderBalDtl(RequestWrapper request) throws Exception {
        List<Map<String, Object>> list;
        try {
            String chkCode = CommonUtils.checkNull(request.getParamValue("CHECK_CODE"));
            String orderCode = CommonUtils.checkNull(request.getParamValue("ORDER_CODE"));
            String balanceCode = CommonUtils.checkNull(request.getParamValue("BALANCE_CODE"));
            String venderId = CommonUtils.checkNull(request.getParamValue("VENDER_ID"));
            String beginTime = CommonUtils.checkNull(request.getParamValue("beginTime"));
            String endTime = CommonUtils.checkNull(request.getParamValue("endTime"));
            String balBeginTime = CommonUtils.checkNull(request.getParamValue("balBeginTime"));
            String balEndTime = CommonUtils.checkNull(request.getParamValue("balEndTime"));
            String whId = CommonUtils.checkNull(request.getParamValue("WH_ID"));
            String partOldCode = CommonUtils.checkNull(request.getParamValue("PART_OLDCODE"));
            String partCode = CommonUtils.checkNull(request.getParamValue("PART_CODE"));
            String partTYpe = CommonUtils.checkNull(request.getParamValue("PART_TYPE"));
            String radioSelect = CommonUtils.checkNull(request.getParamValue("RADIO_SELECT"));
            String invo_no = CommonUtils.checkNull(request.getParamValue("INVO_NO"));
            String makerId = CommonUtils.checkNull(request.getParamValue("MAKER_ID"));

            StringBuffer sql = new StringBuffer("");

            if ("1".equals(radioSelect)) {

                sql.append("SELECT T1.BALANCE_CODE,\n");
                sql.append("       T1.CHECK_CODE,\n");
                sql.append("       T1.ORDER_CODE,\n");
                sql.append("       T1.PART_OLDCODE,\n");
                sql.append("       T1.PART_CNAME,\n");
                sql.append("       T1.PART_CODE,\n");
                sql.append("       T1.UNIT,\n");
                sql.append("       T1.CHECK_QTY,\n");
                sql.append("       T1.IN_QTY,\n");
                sql.append("       NVL(T1.BAL_QTY, (T1.IN_QTY - T1.RETURN_QTY)) BALANCE_QTY,\n");
                sql.append("       T1.RETURN_QTY,\n");
                sql.append("       TO_CHAR(NVL(T1.BUY_PRICE1, T1.BUY_PRICE), 'FM999,999,999,999,990.00') BUY_PRICE,\n");
                sql.append("       TO_CHAR(NVL(T1.BAL_AMOUNT,\n");
                sql.append("                   T1.BUY_PRICE * (T1.IN_QTY - T1.RETURN_QTY)),\n");
                sql.append("               'FM999,999,999,999,990.00') BALANCE_AMOUNT,\n");
                sql.append("       T3.NAME,\n");
                sql.append("       T1.CREATE_DATE,\n");
                sql.append("       T1.VENDER_NAME,\n");
                sql.append("       T2.MAKER_NAME,\n");
                sql.append("       T4.NAME PLANER,\n");
                sql.append("       T5.CODE_DESC,\n");
                sql.append("       T1.INVO_NO\n");
                sql.append("  FROM TT_PART_PO_BALANCE   T1,\n");
                sql.append("       TT_PART_MAKER_DEFINE T2,\n");
                sql.append("       TC_USER              T3,\n");
                sql.append("       TC_USER              T4,\n");
                sql.append("       TC_CODE              T5\n");
                sql.append("  WHERE T1.BALANCE_TYPE=").append(Constant.PART_BALANCE_TYPE_01);
                sql.append("  AND T1.MAKER_ID = T2.MAKER_ID \n");
                sql.append("    AND T1.CREATE_BY = T3.USER_ID \n");
                sql.append("   AND T1.BUYER_ID = T4.USER_ID(+)\n");
                sql.append("   AND T1.STATE = T5.CODE_ID\n");


            }

            if ("2".equals(radioSelect)) {
                sql.append(" SELECT T1.BALANCE_CODE, \n");
                sql.append("        SUM(T1.CHECK_QTY) CHECK_QTY, \n");
                sql.append("        SUM(T1.IN_QTY) IN_QTY, \n");
                sql.append("        SUM(NVL(T1.BAL_QTY,(T1.IN_QTY - T1.RETURN_QTY))) BALANCE_QTY, \n");
                sql.append("        SUM(T1.RETURN_QTY) RETURN_QTY, \n");
                sql.append("        TO_CHAR(SUM(NVL(T1.BAL_AMOUNT,T1.BUY_PRICE * (T1.IN_QTY - T1.RETURN_QTY))), \n");
                sql.append("                'FM999,999,999,999,990.00') BALANCE_AMOUNT, \n");
                sql.append("        T3.NAME, \n");
                sql.append("        T1.ORIGIN_TYPE, \n");
                sql.append("        TRUNC(T1.CREATE_DATE) CREATE_DATE,\n");
                sql.append("        T5.CODE_DESC,\n");
                sql.append("        T1.INVO_NO\n");
                sql.append("   FROM TT_PART_PO_BALANCE T1, TC_USER T3, TC_CODE T5\n");
                sql.append("  WHERE T1.BALANCE_TYPE=").append(Constant.PART_BALANCE_TYPE_01);
                sql.append(" AND T1.CREATE_BY = T3.USER_ID \n");
                sql.append("   AND T1.STATE = T5.CODE_ID\n");

            }
            if (!"".equals(invo_no)) {
                sql.append("  AND T1.INVO_NO LIKE '%").append(invo_no).append("%'");
            }
            if (!"".equals(chkCode)) {
                sql.append(" AND T1.CHECK_CODE LIKE '%").append(chkCode).append("%'");
            }
            if (!"".equals(orderCode)) {
                sql.append(" AND T1.ORDER_CODE LIKE '%").append(orderCode).append("%'");
            }
            if (!"".equals(balanceCode)) {
                sql.append(" AND T1.BALANCE_CODE LIKE '%").append(balanceCode).append("%'");
            }
            if (!"".equals(partOldCode)) {
                sql.append(" AND UPPER(T1.PART_OLDCODE) LIKE '%").append(partOldCode.toUpperCase()).append("%'");
            }
            if (!"".equals(partCode)) {
                sql.append(" AND UPPER(T1.PART_CODE) LIKE '%").append(partCode.toUpperCase()).append("%'");
            }
            if (!"".equals(partTYpe)) {
                sql.append(" AND T1.PART_TYPE=").append(partTYpe);
            }
            if (!"".equals(venderId)) {
                sql.append(" AND T1.VENDER_ID=").append(venderId);
            }
            if (!"".equals(makerId)) {
                sql.append(" AND T1.MAKER_ID=").append(makerId);
            }
            if (!"".equals(beginTime)) {
                sql.append(" AND TO_DATE(T1.CHECK_DATE)>=").append("TO_DATE('").append(beginTime).append("','yyyy-MM-dd')");
            }
            if (!"".equals(endTime)) {
                sql.append(" AND TO_DATE(T1.CHECK_DATE)<=").append("TO_DATE('").append(endTime).append("','yyyy-MM-dd')");
            }
            if (!"".equals(balBeginTime)) {
                sql.append(" AND TO_DATE(T1.CREATE_DATE)>=").append("TO_DATE('").append(balBeginTime).append("','yyyy-MM-dd')");
            }
            if (!"".equals(balEndTime)) {
                sql.append(" AND TO_DATE(T1.CREATE_DATE)<=").append("TO_DATE('").append(balEndTime).append("','yyyy-MM-dd')");
            }
            if (!"".equals(whId)) {
                sql.append(" AND T1.WH_ID=").append(whId);
            }
            sql.append(" AND T1.STATE<>").append(Constant.PART_PURCHASE_ORDERBALANCE_STATUS_05);
            if ("2".equals(radioSelect)) {
                sql.append("GROUP BY T1.BALANCE_CODE,\n");
                sql.append("          T3.NAME,\n");
                sql.append("          T1.ORIGIN_TYPE,\n");
                sql.append("          TRUNC(T1.CREATE_DATE),\n");
                sql.append("          T5.CODE_DESC,\n");
                sql.append("          T1.INVO_NO\n");
            }
            sql.append("  ORDER BY T1.BALANCE_CODE \n");

            list = pageQuery(sql.toString(), null, getFunName());
        } catch (Exception e) {
            throw e;
        }
        return list;
    }

    public PageResult<Map<String, Object>> queryPurOrderInList(
            RequestWrapper request, Integer curPage, Integer pageSize, AclUserBean logonUser) throws Exception {
        PageResult<Map<String, Object>> ps;
        try {

            String venderId = CommonUtils.checkNull(request.getParamValue("VENDER_ID"));//供应商id
            String purchaseWay = CommonUtils.checkNull(request.getParamValue("PURCHASE_WAY"));//采购组织
            String chkCode = CommonUtils.checkNull(request.getParamValue("CHK_CODE"));//验收单号
            String partOldCode = CommonUtils.checkNull(request.getParamValue("PART_OLDCODE"));//备件编码
            String partName = CommonUtils.checkNull(request.getParamValue("PART_CNAME"));//备件名称
            String partCode = CommonUtils.checkNull(request.getParamValue("PART_CODE"));//备件件号
            String beginTime = CommonUtils.checkNull(request.getParamValue("beginTime"));//验收开始时间
            String endTime = CommonUtils.checkNull(request.getParamValue("endTime"));//验收结束时间
            String planerId = CommonUtils.checkNull(request.getParamValue("PLANER_ID"));//计划员
            String purId = CommonUtils.checkNull(request.getParamValue("BALANCER_ID"));//CommonUtils.checkNull(request.getParamValue("PUR_ID"));//采购员
            String origin_type = CommonUtils.checkNull(request.getParamValue("ORIGIN_TYPE"));//来源
            String inCode = CommonUtils.checkNull(request.getParamValue("inCodeS"));//入库单号
            String orderCode = CommonUtils.checkNull(request.getParamValue("orderCodeS"));//订单单号
            String inSDate = CommonUtils.checkNull(request.getParamValue("inSDate"));//入库开始时间
            String inEDate = CommonUtils.checkNull(request.getParamValue("inEDate"));//入库结束时间
            String isSeam = CommonUtils.checkNull(request.getParamValue("isSeam"));//制造商与供应商相同
            String isGuard = CommonUtils.checkNull(request.getParamValue("IS_GUARD"));//是否暂估
            String produceState = CommonUtils.checkNull(request.getParamValue("PART_PRODUCE_STATE"));//
            String originType = Constant.ORDER_ORIGIN_TYPE_03.toString();//直发订单-来源
            if (originType.equals(origin_type)) {
                //直发订单-来源
                StringBuffer sql = new StringBuffer();
                sql.append("\n SELECT TEMP.* FROM ( \n");
                sql.append("    SELECT TPPI.IN_ID,\n");
                sql.append("       TPPI.CHECK_ID,\n");
                sql.append("       TPPI.CHECK_CODE,\n");
                sql.append("       TPPI.ORDER_CODE,\n");
                sql.append("       TPPI.IN_CODE,\n");
                sql.append("       TPPI.PART_ID,\n");
                sql.append("       TPPI.PART_TYPE,\n");
                sql.append("       TPPI.PART_OLDCODE,\n");
                sql.append("       TPPI.PART_CNAME,\n");
                sql.append("       TPPI.PART_CODE,\n");
                sql.append("       TPPI.UNIT,\n");
                sql.append("       TO_CHAR(TPPI.IN_DATE,'YYYY-MM-DD')IN_DATE,\n");
                sql.append("        (SELECT TC.NAME FROM TC_USER TC WHERE TC.USER_ID=tpd.buyer_ID)BUY_NAME,\n");
                sql.append("       TPPI.VENDER_ID,\n");
                sql.append("       TPPI.VENDER_NAME,\n");
                sql.append("       TPVD.VENDER_ID VENDER_ID1,\n");
                sql.append("       TPVD.VENDER_NAME VENDER_NAME1,\n");
                sql.append("       TPVD.VENDER_CODE VENDER_CODE1,\n");
                sql.append("       TPPI.WH_ID,\n");
                sql.append("       TPPI.WH_NAME,\n");
                sql.append("       SUM(TPDID.IN_QTY) AS IN_QTY,\n");
                sql.append("       TPPI.BAL_QTY,\n");
                sql.append("       (SUM(TPDID.IN_QTY) - TPPI.RETURN_QTY - TPPI.BAL_QTY) AS SPARE_QTY,\n");
                sql.append("       TPPI.RETURN_QTY,\n");
                sql.append("       TPPI.REMARK,\n");
                sql.append("       TPPI.PRODUCE_FAC,\n");
                sql.append("       TPBP.IS_GUARD,\n");
                sql.append("       TO_CHAR(TPPI.CREATE_DATE, 'YYYY-MM-DD HH24:MM:SS') CREATE_DATE\n");
                sql.append("  FROM TT_PART_DLR_INSTOCK_DTL TPDID, --入库单\n");
                sql.append("       TT_PART_TRANS           TPT, --发运单\n");
                sql.append("       TT_PART_DLR_ORDER_MAIN  TPDOM, --订单表\n");
                sql.append("       TT_PART_PO_IN           TPPI, --入库明细\n");
                sql.append("       TT_PART_BUY_PRICE       TPBP, --销售关系表\n");
                sql.append("       TT_PART_DEFINE          TPD, --备件基础信息表\n");
                sql.append("       TT_PART_VENDER_DEFINE   TPVD --供应商基础信息表\n");
                sql.append(" WHERE TPT.TRANS_ID = TPDID.TRANS_ID\n");
                sql.append("   AND TPDOM.ORDER_ID = TPT.ORDER_ID\n");
                sql.append("   AND TPPI.PO_ID = TPDOM.ORDER_ID\n");
                sql.append("   AND TPBP.PART_ID = TPPI.PART_ID\n");
                sql.append("   AND TPDID.PART_ID(+) = TPPI.PART_ID\n");
                sql.append("   AND TPD.PART_ID = TPPI.PART_ID\n");
                sql.append("   AND TPPI.VENDER_ID = TPBP.VENDER_ID\n");
                sql.append("   AND TPPI.VENDER_ID = TPVD.VENDER_ID\n");
                sql.append("   AND TPT.ORDER_TYPE IN ('92151004', '92151013')\n");
               // sql.append("   AND TPT.CONFIRM_RECEIPT = " + Constant.IF_TYPE_YES + " \n");
                if (!"".equals(produceState)) {
                    sql.append("  AND TPD.PRODUCE_STATE = '").append(produceState).append("'  \n");
                }
                //验收单code
                if (!"".equals(chkCode)) {
                    sql.append(" AND TPPI.CHECK_CODE LIKE '%")
                            .append(chkCode.trim().toUpperCase()).append("%'\n");
                }

                if (!"".equals(venderId)) {
                    sql.append(" AND TPPI.VENDER_ID=").append(venderId);
                }

                if (!"".equals(purchaseWay)) {
                    sql.append(" AND TPD.PRODUCE_FAC=").append(purchaseWay);
                }

                if (!partOldCode.equals("")) {
                    sql.append(" AND TPPI.PART_OLDCODE LIKE '%")
                            .append(partOldCode.trim().toUpperCase()).append("%'\n");
                }
                if (!partName.equals("")) {
                    sql.append(" AND TPPI.PART_CNAME LIKE '%")
                            .append(partName).append("%'\n");
                }
                if (!partCode.equals("")) {
                    sql.append(" AND TPPI.PART_CODE LIKE '%")
                            .append(partCode.trim().toUpperCase()).append("%'\n");
                }


                //采购员  
                if (!"".equals(planerId)) {
                    sql.append(" AND  tpd.PLANER_ID =").append(planerId);

                }
                if (!"".equals(purId)) {
                    sql.append(" AND  tpd.buyer_ID =").append(purId);

                }
                if (!"".equals(origin_type)) {
                    sql.append(" AND TPPI.ORIGIN_TYPE=").append(origin_type);
                }
                if (!inCode.equals("")) {
                    sql.append(" AND TPPI.IN_CODE LIKE '%")
                            .append(inCode.trim().toUpperCase()).append("%'\n");
                }
                if (!orderCode.equals("")) {
                    sql.append(" AND TPPI.ORDER_CODE LIKE '%")
                            .append(orderCode.trim().toUpperCase()).append("%'\n");
                }

                if (!"".equals(inSDate)) {
                    sql.append(" AND TO_DATE(TPPI.IN_DATE)>=").append("TO_DATE('").append(inSDate).append("','yyyy-MM-dd')");
                }

                if (!"".equals(inEDate)) {
                    sql.append(" AND TO_DATE(TPPI.IN_DATE)<=").append("TO_DATE('").append(inEDate).append("','yyyy-MM-dd')");
                }
                if (!"".equals(beginTime)) {
                    sql.append(" AND TO_DATE(TPPI.CREATE_DATE)>=").append("TO_DATE('").append(beginTime).append("','yyyy-MM-dd')");
                }

                if (!"".equals(endTime)) {
                    sql.append(" AND TO_DATE(TPPI.CREATE_DATE)<=").append("TO_DATE('").append(endTime).append("','yyyy-MM-dd')");
                }
                if (!"".equals(isGuard)) {
                    if ("1".equals(isGuard)) {
                        sql.append("  AND TPBP.IS_GUARD = '10041001'  \n");
                    } else if ("2".equals(isGuard)) {
                        sql.append("   AND TPBP.IS_GUARD = '10041002'  \n");
                    }
                }
                sql.append(" group by TPPI.IN_ID,\n");
                sql.append("          TPPI.CHECK_ID,\n");
                sql.append("          TPPI.CHECK_CODE,\n");
                sql.append("          TPPI.ORDER_CODE,\n");
                sql.append("          TPPI.IN_CODE,\n");
                sql.append("          TPPI.PART_ID,\n");
                sql.append("          TPPI.PART_TYPE,\n");
                sql.append("          TPPI.PART_OLDCODE,\n");
                sql.append("          TPPI.PART_CNAME,\n");
                sql.append("          TPPI.PART_CODE,tpd.buyer_ID,\n");
                sql.append("          TPPI.UNIT,\n");
                sql.append("          TPPI.IN_DATE,\n");
                sql.append("          TPPI.VENDER_ID,\n");
                sql.append("          TPPI.VENDER_NAME,\n");
                sql.append("          TPVD.VENDER_ID,\n");
                sql.append("          TPVD.VENDER_NAME,\n");
                sql.append("          TPVD.VENDER_CODE,\n");
                sql.append("          TPPI.WH_ID,\n");
                sql.append("          TPPI.WH_NAME,\n");
                sql.append("          TPPI.RETURN_QTY,\n");
                sql.append("          TPPI.REMARK,\n");
                sql.append("          TPD.PRODUCE_FAC,\n");
                sql.append("          TPBP.IS_GUARD,\n");
                sql.append("          TPPI.CREATE_DATE,\n");
                sql.append("          TPPI.BAL_QTY,TPPI.PRODUCE_FAC\n");

                sql.append("  )TEMP WHERE \n");
                sql.append("  (TEMP.IN_QTY - TEMP.RETURN_QTY - TEMP.BAL_QTY)<>0\n");


                ps = pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
            } else {
                //非直发订单，订单类型为空
                StringBuffer sql = new StringBuffer("");
                //采购订单结算带出财务供应商

				sql.append(" SELECT *\n");
				sql.append("   FROM (SELECT A.IN_ID,\n");
				sql.append("                A.CHECK_ID,\n");
				sql.append("                A.CHECK_CODE,\n");
				sql.append("                A.ORDER_CODE,\n");
				sql.append("                A.BUY_PRICE_NOTAX,\n");
				sql.append("                A.IN_CODE,\n");
				sql.append("                A.PART_ID,\n");
				sql.append("                A.PART_TYPE,\n");
				sql.append("                A.PART_OLDCODE,\n");
				sql.append("                A.PART_CNAME,\n");
				sql.append("                A.PART_CODE,\n");
				sql.append("                A.UNIT,\n");
				sql.append("                TO_CHAR(A.IN_DATE,'YYYY-MM-DD')IN_DATE,\n");
				sql.append("                (SELECT TC.NAME FROM TC_USER TC WHERE TC.USER_ID = pd.buyer_id) BUY_NAME,\n");
				sql.append("                A.BUYER_ID,\n");
				sql.append("                A.VENDER_ID,\n");
				sql.append("                A.VENDER_NAME,\n");
				sql.append("                   A.VENDER_NAME VENDER_NAME1,\n");
				sql.append("                   A.VENDER_ID VENDER_ID1,\n");
				sql.append("                   A.VENDER_CODE VENDER_CODE1,\n");
				sql.append("                NVL((SELECT CASE CONTRACT_NUMBER\n");
				sql.append("                                 WHEN '' THEN\n");
				sql.append("                                  10041001\n");
				sql.append("                                 ELSE\n");
				sql.append("                                  10041002\n");
				sql.append("                               END\n");
				sql.append("                          FROM TT_PART_CONTRACT_DEFINE CD\n");
				sql.append("                         WHERE CD.VENDER_ID = A.VENDER_ID\n");
				sql.append("                           AND CD.STATE = 10011001\n");
				sql.append("                           AND CD.PART_ID = A.PART_ID\n");
				sql.append("                           AND CD.CONTRACT_SDATE <= A.IN_DATE\n");
				sql.append("                           AND CD.CONTRACT_EDATE >= A.IN_DATE),\n");
				sql.append("                    10041001) IS_GUARD,\n");
				sql.append("                TPWD.WH_ID,\n");
				sql.append("                TPWD.WH_NAME,\n");
				sql.append("                A.IN_QTY,\n");
				sql.append("                A.BAL_QTY,\n");
				sql.append("                (A.IN_QTY - A.BAL_QTY - A.RETURN_QTY) SPARE_QTY,\n");
				sql.append("                A.RETURN_QTY,\n");
				sql.append("                A.REMARK,\n");
				sql.append("                A.PRODUCE_FAC\n");
				sql.append("         --    TO_CHAR(M.CREATE_DATE,'yyyy-MM-dd') CREATE_DATE\n");
				sql.append("           FROM TT_PART_PO_IN            A,\n");
				sql.append("                TT_PART_DEFINE           PD,\n");
				sql.append("                TT_PART_WAREHOUSE_DEFINE TPWD\n");
				sql.append("          WHERE A.PART_ID = PD.PART_ID\n");
				sql.append("            AND A.IS_CHECK = 1\n");
				sql.append("            AND A.WH_ID = TPWD.WH_ID\n");

                sql.append("    AND A.STATE = ").append(Constant.PART_PURCHASE_ORDERBALANCE_STATUS_01);
                sql.append("    AND A.STATUS = 1 \n");
                StringBuffer sql1 = new StringBuffer();
                sql1.append("SELECT 1 FROM TC_USER U, TT_PART_USERPOSE_DEFINE PU, TT_PART_FIXCODE_DEFINE PF\n");
                sql1.append("WHERE PU.USER_ID = U.USER_ID(+)\n");
                sql1.append(" AND PF.FIX_VALUE = PU.USER_TYPE\n");
                sql1.append(" AND U.USER_STATUS = '10011001'\n");
                sql1.append(" AND PF.FIX_VALUE = '9'\n");
                sql1.append("AND PU.STATE = 10011001\n");
                sql1.append("AND PF.FIX_GOUPTYPE = 92251001 and u.user_id = " + logonUser.getUserId());
              /*  if (pageQuery(sql1.toString(), null, getFunName()).size() == 0) {
                    sql.append("AND ((TRUNC(A.CK_PRICE_DATE) <= TO_DATE('2016-05-01', 'yyyy-mm-dd')))\n");

                }
*/
                if (!"".equals(produceState)) {
                    sql.append(" AND a.PRODUCE_STATE = '").append(produceState).append("' \n");
                }
                if (!"".equals(chkCode)) {
                    sql.append(" AND A.CHECK_CODE LIKE '%")
                            .append(chkCode.trim().toUpperCase()).append("%'\n");
                }
                if (!"".equals(purId)) {
                    sql.append(" AND A.BUYER_ID = ")
                            .append(purId).append("\n");
                }

                if (!"".equals(beginTime)) {
                    sql.append(" AND TO_DATE(M.CREATE_DATE)>=").append("TO_DATE('").append(beginTime).append("','yyyy-MM-dd')");
                }

                if (!"".equals(endTime)) {
                    sql.append(" AND TO_DATE(M.CREATE_DATE)<=").append("TO_DATE('").append(endTime).append("','yyyy-MM-dd')");
                }

                if (!"".equals(venderId)) {
                    sql.append(" AND a.fcvder_id=").append(venderId);
                }

                if (!"".equals(purchaseWay)) {
                    sql.append(" AND a.PRODUCE_FAC=").append(purchaseWay);
                }

                if (!partOldCode.equals("")) {
                    sql.append(" AND A.PART_OLDCODE LIKE '%")
                            .append(partOldCode.trim().toUpperCase()).append("%'\n");
                }
                if (!partName.equals("")) {
                    sql.append(" AND A.PART_CNAME LIKE '%")
                            .append(partName).append("%'\n");
                }
                if (!partCode.equals("")) {
                    sql.append(" AND A.PART_CODE LIKE '%")
                            .append(partCode.trim().toUpperCase()).append("%'\n");
                }

                if (!"".equals(planerId)) {
                    //sql.append(" AND ( A.BUYER_ID =").append(planerId);
                    sql.append(" AND( M.CREATE_BY=").append(planerId).append(" OR ");
                    sql.append("M.ORIGIN_TYPE = DECODE(NVL((SELECT PU.IS_DIRECT\n");
                    sql.append("  FROM TT_PART_USERPOSE_DEFINE PU\n");
                    sql.append(" WHERE PU.USER_ID =").append(planerId);
                    sql.append(" AND PU.USER_TYPE = 1),");
                    sql.append(Constant.PART_BASE_FLAG_NO).append("),");
                    sql.append(Constant.PART_BASE_FLAG_YES).append(",");
                    sql.append(Constant.ORDER_ORIGIN_TYPE_03).append(",1))");
                }
                if (!"".equals(origin_type)) {
                    sql.append(" AND A.ORIGIN_TYPE=").append(origin_type);
                }
                if (!inCode.equals("")) {
                    sql.append(" AND A.IN_CODE LIKE '%")
                            .append(inCode.trim().toUpperCase()).append("%'\n");
                }
                if (!orderCode.equals("")) {
                    sql.append(" AND A.ORDER_CODE LIKE '%")
                            .append(orderCode.trim().toUpperCase()).append("%'\n");
                }

                if (!"".equals(inSDate)) {
                    sql.append(" AND TO_DATE(A.CK_PRICE_DATE)>=").append("TO_DATE('").append(inSDate).append("','yyyy-MM-dd')");
                }

                if (!"".equals(inEDate)) {
                    sql.append(" AND TO_DATE(A.CK_PRICE_DATE)<=").append("TO_DATE('").append(inEDate).append("','yyyy-MM-dd')");
                }

                sql.append("  ORDER BY pd.buyer_id,VENDER_ID1,VENDER_ID,a.PRODUCE_FAC,A.PART_OLDCODE  \n");

                sql.append(")  \n");
                sql.append(" where 1=1  \n");
                if (!"".equals(isGuard)) {
                    if ("1".equals(isGuard)) {
                        sql.append("  AND IS_GUARD = '10041001'  \n");
                    } else if ("2".equals(isGuard)) {
                        sql.append("   AND IS_GUARD = '10041002'  \n");
                    }
                }
                ps = pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
            }

        } catch (Exception e) {
            throw e;
        }
        return ps;
    }

    public void updateBalanceOrder(String balanceCode) throws Exception {
        try {
            StringBuffer sql1 = new StringBuffer("");
            sql1.append("DELETE FROM TT_PART_PO_BALANCE T WHERE T.BALANCE_CODE=?");
            List params1 = new ArrayList();
            params1.add(balanceCode);

            delete(sql1.toString(), params1);
        } catch (Exception e) {
            throw e;
        }
    }

    public void updateState1(String code, String rejectReason,String userId) throws Exception {
        try {
           /* StringBuffer sql = new StringBuffer("");
            sql.append(" UPDATE TT_PART_PO_IN T \n");
            sql.append("    SET T.STATE =").append(Constant.PART_PURCHASE_ORDERBALANCE_STATUS_06);
            sql.append("\n");
            sql.append("  WHERE 1=1 AND EXISTS (SELECT 1\n");
            sql.append("                      FROM TT_PART_PO_BALANCE T1 \n");
            sql.append("                     WHERE T.IN_ID=T1.IN_ID AND T1.BALANCE_CODE = '").append(code);
            sql.append("') \n");
            update(sql.toString(), null);*/

            StringBuffer sql1 = new StringBuffer("");
            sql1.append(" UPDATE TT_PART_PO_BALANCE T \n");
            sql1.append("    SET T.STATE =").append(Constant.PART_PURCHASE_ORDERBALANCE_STATUS_06);
            sql1.append(",T.REMARK1='").append(rejectReason).append("'\n");
            sql1.append("  WHERE T.BALANCE_CODE = '").append(code).append("'");
            update(sql1.toString(), null);
            StringBuffer sql2 = new StringBuffer();
            sql2.append("UPDATE TT_PART_PO_BALANCE_FINAL F\n");
            sql2.append("   SET F.STATE ="+Constant.PART_PURCHASE_ORDERBALANCE_STATUS_06+", F.DELETE_DATE =SYSDATE, F.DELETE_BY ="+userId+"\n");
            sql2.append("  WHERE F.NEW_BALANCE_ID = '").append(code).append("'");
            update(sql2.toString(), null);
        } catch (Exception e) {
            throw e;
        }
    }

    public void updateState2(String balanceCode,Long userId) throws Exception {
        try {
            StringBuffer sql = new StringBuffer("");
            sql.append(" UPDATE TT_PART_PO_IN T \n");
            sql.append("    SET T.STATE =").append(Constant.PART_PURCHASE_ORDERBALANCE_STATUS_04);
            sql.append("\n");
            sql.append("  WHERE 1=1 AND EXISTS (SELECT 1\n");
            sql.append("                      FROM TT_PART_PO_BALANCE T1 \n");
            sql.append("                     WHERE T.IN_ID=T1.IN_ID AND T1.BALANCE_CODE = '").append(balanceCode);
            sql.append("') \n");
            update(sql.toString(), null);

            StringBuffer sql1 = new StringBuffer("");
            sql1.append(" UPDATE TT_PART_PO_BALANCE T \n");
            sql1.append("    SET T.STATE =").append(Constant.PART_PURCHASE_ORDERBALANCE_STATUS_04);
            sql1.append(",\n").append("T.SUB_BY=").append(userId);
            sql1.append(",\n").append("t.SUB_date=sysdate \n");
            sql1.append("  WHERE T.BALANCE_CODE = '").append(balanceCode).append("'");
            update(sql1.toString(), null);

        } catch (Exception e) {
            throw e;
        }
    }

    public void updateState3(String balanceCode) throws Exception {
        try {
            StringBuffer sql1 = new StringBuffer("");
            sql1.append(" UPDATE TT_PART_PO_BALANCE T \n");
            sql1.append("    SET T.STATE =").append(Constant.PART_PURCHASE_ORDERBALANCE_STATUS_05);
            sql1.append("\n");
            sql1.append("  WHERE T.BALANCE_CODE = '").append(balanceCode).append("'");
            update(sql1.toString(), null);

        } catch (Exception e) {
            throw e;
        }
    }

    public List<Map<String, Object>> queryPurOrderBalListByCode(String balCode) throws Exception {
        List<Map<String, Object>> list;
        try {
            StringBuffer sql = new StringBuffer("");


            sql.append("SELECT T.BALANCE_CODE,\n");
            sql.append("       T.CHECK_CODE,\n");
            sql.append("       T.VENDER_NAME,\n");
            sql.append("       T.PART_OLDCODE,\n");
            sql.append("       T.PART_CNAME,\n");
            sql.append("       T.PART_CODE,\n");
            sql.append("       NVL(T.BAL_QTY, T.IN_QTY - T.RETURN_QTY) BAL_QTY,\n");
            sql.append("       T.UNIT,\n");
            sql.append("       TO_CHAR(NVL(T.BUY_PRICE1, T.BUY_PRICE), 'FM999,999,999,999,990.00') BUY_PRICE,\n");
            sql.append("       TO_CHAR(NVL(T.BUY_PRICE1_NOTAX, T.BUY_PRICE_NOTAX),\n");
            sql.append("               'FM999,999,999,999,990.00') BUY_PRICE_NOTAX,\n");
            sql.append("       TO_CHAR(IN_AMOUNT, 'FM999,999,999,999,990.00') IN_AMOUNT,\n");
            sql.append("       TO_CHAR(NVL(T.BAL_AMOUNT, T.BUY_PRICE * (T.IN_QTY - T.RETURN_QTY)),\n");
            sql.append("               'FM999,999,999,999,990.00') BAL_AMOUNT,\n");
            sql.append("       T.CREATE_DATE,\n");
            sql.append("       U.NAME,\n");
            sql.append("       T.REMARK,\n");
            sql.append("\t\t\t ld.loc_name\n");
            sql.append("  FROM TT_PART_PO_BALANCE T, TC_USER U,tt_part_loaction_define ld\n");
            sql.append(" WHERE T.CREATE_BY = U.USER_ID\n");
            sql.append("   AND t.part_id=ld.part_id\n");
            sql.append("\t AND t.wh_id=ld.wh_id\n");

            sql.append("    AND T.BALANCE_CODE = '").append(balCode).append("'");

            list = pageQuery(sql.toString(), null, getFunName());
        } catch (Exception e) {
            throw e;
        }
        return list;
    }

    public Map<String, Object> queryPurOrderBalMainByCode(String balCode) throws Exception {
        Map<String, Object> map;
        try {
            StringBuffer sql = new StringBuffer("");

            sql.append(" SELECT T.BALANCE_CODE, \n");
            sql.append("        T.VENDER_NAME, \n");
            sql.append("        SUM(NVL(T.BAL_QTY, T.IN_QTY - T.RETURN_QTY)) BAL_QTY, \n");
            sql.append("        TRUNC(T.CREATE_DATE) CREATE_DATE, \n");
            sql.append("        U.NAME, \n");
            sql.append("        T.WH_NAME, \n");
            sql.append("        T.INVO_NO, \n");
            sql.append("        TO_CHAR(SUM(NVL(T.BAL_AMOUNT, \n");
            sql.append("                        T.BUY_PRICE * (T.IN_QTY - T.RETURN_QTY))), \n");
            sql.append("                'FM999,999,999,999,990.00') BAL_AMOUNT \n");
            sql.append("   FROM TT_PART_PO_BALANCE T, TC_USER U \n");
            sql.append("  WHERE T.CREATE_BY = U.USER_ID \n");
            sql.append("    AND T.BALANCE_CODE = '").append(balCode).append("'");
            sql.append("  GROUP BY T.BALANCE_CODE, T.VENDER_NAME, TRUNC(T.CREATE_DATE), U.NAME,T.WH_NAME,T.INVO_NO \n");

            map = pageQueryMap(sql.toString(), null, getFunName());
        } catch (Exception e) {
            throw e;
        }
        return map;
    }

    public PageResult<Map<String, Object>> queryOrderBalAllList(
            RequestWrapper request, Integer curPage, Integer pageSize) throws Exception {
        PageResult<Map<String, Object>> ps;
        try {
            StringBuffer sql = new StringBuffer("");

            String beginTime = CommonUtils.checkNull(request.getParamValue("beginTime"));//开始时间
            String endTime = CommonUtils.checkNull(request.getParamValue("endTime"));//结束时间
            String invo_num = CommonUtils.checkNull(request.getParamValue("invo_num"));//结束时间

            sql.append("SELECT T.INVO_NO,\n");
            sql.append("       TPD.PRODUCE_STATE,\n");
            sql.append("       /* T.BUY_PRICE, --计划价\n");
            sql.append("       NVL(T.BUY_PRICE1, T.BUY_PRICE) BUY_PRICE1, --采购价 */\n");
            /*sql.append("       TO_CHAR(NVL(SUM(T.IN_AMOUNT), 0), 'FM999,999,999,999,990.00') IN_AMOUNT, --计划金额汇总\n");
            sql.append("       TO_CHAR(NVL(SUM(NVL(T.BAL_AMOUNT, T.IN_AMOUNT)), 0),\n");
            sql.append("               'FM999,999,999,999,990.00') BAL_AMOUNT --采购金额汇总\n");*/
            sql.append(" NVL(SUM(T.IN_AMOUNT), 0) IN_AMOUNT,");
            sql.append(" NVL(SUM(NVL(T.BAL_AMOUNT, T.IN_AMOUNT)), 0) BAL_AMOUNT");
           /* sql.append("       TRUNC(T.CK_PRICE_DATE) CK_PRICE_DATE --财务确认日期\n");*/
            sql.append("  FROM TT_PART_PO_BALANCE T, TT_PART_DEFINE TPD\n");
            sql.append(" WHERE 1 = 1\n");
            sql.append("   AND T.PART_ID = TPD.PART_ID\n");
            if (!"".equals(beginTime)) {
                sql.append(" AND TO_DATE(T.CK_PRICE_DATE)>=").append("TO_DATE('").append(beginTime).append("','yyyy-MM-dd')");
            }

            if (!"".equals(endTime)) {
                sql.append(" AND TO_DATE(T.CK_PRICE_DATE)<=").append("TO_DATE('").append(endTime).append("','yyyy-MM-dd')");
            }
            if (!"".equals(invo_num)) {
                sql.append("AND  T.INVO_NO like '%").append(invo_num).append("%'");
            }
            sql.append("GROUP BY T.INVO_NO, TPD.PRODUCE_STATE \n");


            ps = pageQuery(sql.toString(), null, getFunName(), pageSize,
                    curPage);
        } catch (Exception e) {
            throw e;
        }
        return ps;
    }

    public List<Map<String, Object>> queryPurOrderBalAll(RequestWrapper request) throws Exception {
        List<Map<String, Object>> list;
        try {
            StringBuffer sql = new StringBuffer("");

            String beginTime = CommonUtils.checkNull(request.getParamValue("beginTime"));//开始时间
            String endTime = CommonUtils.checkNull(request.getParamValue("endTime"));//结束时间
            String invo_num = CommonUtils.checkNull(request.getParamValue("invo_num"));//结束时间

            sql.append("SELECT T.INVO_NO,\n");
            sql.append("       TPD.PRODUCE_STATE,\n");
            sql.append("       /* T.BUY_PRICE, --计划价\n");
            sql.append("       NVL(T.BUY_PRICE1, T.BUY_PRICE) BUY_PRICE1, --采购价 */\n");
            /*sql.append("       TO_CHAR(NVL(SUM(T.IN_AMOUNT), 0), 'FM999,999,999,999,990.00') IN_AMOUNT, --计划金额汇总\n");
            sql.append("       TO_CHAR(NVL(SUM(NVL(T.BAL_AMOUNT, T.IN_AMOUNT)), 0),\n");
            sql.append("               'FM999,999,999,999,990.00') BAL_AMOUNT --采购金额汇总\n");*/
            sql.append(" NVL(SUM(T.IN_AMOUNT), 0) IN_AMOUNT,");
            sql.append(" NVL(SUM(NVL(T.BAL_AMOUNT, T.IN_AMOUNT)), 0) BAL_AMOUNT");
           /* sql.append("       TRUNC(T.CK_PRICE_DATE) CK_PRICE_DATE --财务确认日期\n");*/
            sql.append("  FROM TT_PART_PO_BALANCE T, TT_PART_DEFINE TPD\n");
            sql.append(" WHERE 1 = 1\n");
            sql.append("   AND T.PART_ID = TPD.PART_ID\n");
            if (!"".equals(beginTime)) {
                sql.append(" AND TO_DATE(T.CK_PRICE_DATE)>=").append("TO_DATE('").append(beginTime).append("','yyyy-MM-dd')");
            }

            if (!"".equals(endTime)) {
                sql.append(" AND TO_DATE(T.CK_PRICE_DATE)<=").append("TO_DATE('").append(endTime).append("','yyyy-MM-dd')");
            }
            if (!"".equals(invo_num)) {
                sql.append("AND  T.INVO_NO like '%").append(invo_num).append("%'");
            }
            sql.append("GROUP BY T.INVO_NO, TPD.PRODUCE_STATE \n");


            list = pageQuery(sql.toString(), null, getFunName());
        } catch (Exception e) {
            throw e;
        }
        return list;
    }

    //zhumingwei add 2013-10-25 导出明细
    public List<Map<String, Object>> queryPartPlan(RequestWrapper request) {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        StringBuffer sql = new StringBuffer();

        String chkCode = CommonUtils.checkNull(request.getParamValue("CHECK_CODE"));//验收单号
        String inCode = CommonUtils.checkNull(request.getParamValue("IN_CODE"));//入库单号
        String balCode = CommonUtils.checkNull(request.getParamValue("BALANCE_CODE1"));//结算单号
        String buyer = CommonUtils.checkNull(request.getParamValue("buyer"));//采购人员
        String chkBeginTime = CommonUtils.checkNull(request.getParamValue("chkBeginTime"));//验收开始时间
        String chkEndTime = CommonUtils.checkNull(request.getParamValue("chkEndTime"));//验收结束时间
        String inBeginTime = CommonUtils.checkNull(request.getParamValue("inBeginTime"));//入库开始时间
        String inEndTime = CommonUtils.checkNull(request.getParamValue("inEndTime"));//入库结束时间
        String balBeginTime = CommonUtils.checkNull(request.getParamValue("balBeginTime"));//结算开始时间
        String balEndTime = CommonUtils.checkNull(request.getParamValue("balEndTime"));//结算结束时间
        String whId = CommonUtils.checkNull(request.getParamValue("WH_ID2"));//库房id
        String partType = CommonUtils.checkNull(request.getParamValue("PART_TYPE"));//配件种类
        String venderId = CommonUtils.checkNull(request.getParamValue("VENDER_ID2"));//供应商id
        String partOldCode = CommonUtils.checkNull(request.getParamValue("PART_OLDCODE"));//配件编码
        String partName = CommonUtils.checkNull(request.getParamValue("PART_CNAME"));//配件名称
        String partCode = CommonUtils.checkNull(request.getParamValue("PART_CODE"));//配件件号
        String state = CommonUtils.checkNull(request.getParamValue("STATE2"));//状态
        String balanceName = CommonUtils.checkNull(request.getParamValue("BALANCE_NAME"));//结算人员

        sql.append(" SELECT A.CHECK_CODE, \n");
        sql.append("        A.IN_CODE, \n");
        sql.append("        T.BALANCE_CODE, \n");
        sql.append("        A.IN_DATE, \n");
        sql.append("        T.CREATE_DATE BALANCE_DATE, \n");
        sql.append("        A.PART_TYPE, \n");
        sql.append("        A.PART_OLDCODE, \n");
        sql.append("        A.PART_CNAME, \n");
        sql.append("        A.PART_CODE, \n");
        sql.append("        A.VENDER_NAME, \n");
        sql.append("        D.MAKER_NAME, \n");
        sql.append("        A.WH_NAME, \n");
        sql.append("        A.IN_QTY, \n");
        sql.append("        U.NAME, \n");
        sql.append("        A.RETURN_QTY, \n");
        sql.append("        NVL(T.BAL_QTY,0) BAL_QTY, \n");
        sql.append("        TO_CHAR(A.BUY_PRICE, 'FM999,999,990.00') PLAN_PRICE, \n");
        sql.append("        TO_CHAR(A.IN_AMOUNT, \n");
        sql.append("                'FM999,999,999,999,990.00') PLAN_AMOUNT, \n");
        sql.append("        TO_CHAR(NVL(T.BUY_PRICE1,NVL((SELECT R.BUY_PRICE \n");
        sql.append("                      FROM TT_PART_BUY_PRICE R \n");
        sql.append("                     WHERE A.PART_ID = R.PART_ID \n");
        sql.append("                       AND A.VENDER_ID = R.VENDER_ID), \n");
        sql.append("                    0)), \n");
        sql.append("                'FM999,999,990.00') BUY_PRICE, \n");
        sql.append("        TO_CHAR(NVL(T.BUY_PRICE1,NVL((SELECT R.BUY_PRICE \n");
        sql.append("                      FROM TT_PART_BUY_PRICE R \n");
        sql.append("                     WHERE A.PART_ID = R.PART_ID \n");
        sql.append("                       AND A.VENDER_ID = R.VENDER_ID), \n");
        sql.append("                    0))-A.BUY_PRICE, \n");
        sql.append("                'FM999,999,990.00') DIF_PRICE, \n");
        sql.append("        TO_CHAR(NVL(T.BAL_AMOUNT,0), \n");
        sql.append("                'FM999,999,999,999,990.00') BAL_AMOUNT, \n");
        sql.append("        (SELECT B.NAME FROM TC_USER B WHERE A.IN_BY = B.USER_ID) IN_NAME, \n");
        sql.append("        (SELECT C.NAME FROM TC_USER C WHERE C.USER_ID = A.BUYER_ID) BUYER, \n");
        sql.append("        (select code_desc from tc_code where code_id=a.state) STATE \n");
        sql.append("   FROM TT_PART_PO_IN A, TT_PART_MAKER_DEFINE D,TT_PART_PO_BALANCE T,TC_USER U \n");
        sql.append("  WHERE A.MAKER_ID = D.MAKER_ID(+) \n");
        sql.append("    AND A.IN_ID=T.IN_ID \n");
        sql.append("    AND T.CREATE_BY=U.USER_ID \n");
        sql.append("    AND A.STATUS = 1 \n");

        if (!"".equals(chkCode)) {
            sql.append(" AND A.CHECK_CODE LIKE '%")
                    .append(chkCode).append("%'\n");
        }

        if (!"".equals(inCode)) {
            sql.append(" AND A.IN_CODE LIKE '%")
                    .append(inCode).append("%'\n");
        }
        if (!"".equals(balCode)) {
            sql.append(" AND T.BALANCE_CODE LIKE '%")
                    .append(balCode).append("%'\n");
        }
        if (!"".equals(buyer)) {
            sql.append(" AND BUYER LIKE '%")
                    .append(buyer).append("%'\n");
        }

        if (!"".equals(chkBeginTime)) {
            sql.append(" AND TO_DATE(A.CHECK_DATE)>=").append("TO_DATE('").append(chkBeginTime).append("','yyyy-MM-dd')");
        }

        if (!"".equals(chkEndTime)) {
            sql.append(" AND TO_DATE(A.CHECK_DATE)<=").append("TO_DATE('").append(chkEndTime).append("','yyyy-MM-dd')");
        }
        if (!"".equals(inBeginTime)) {
            sql.append(" AND TO_DATE(A.IN_DATE)>=").append("TO_DATE('").append(inBeginTime).append("','yyyy-MM-dd')");
        }

        if (!"".equals(inEndTime)) {
            sql.append(" AND TO_DATE(A.IN_DATE)<=").append("TO_DATE('").append(inEndTime).append("','yyyy-MM-dd')");
        }
        if (!"".equals(balBeginTime)) {
            sql.append(" AND TO_DATE(T.CREATE_DATE)>=").append("TO_DATE('").append(balBeginTime).append("','yyyy-MM-dd')");
        }

        if (!"".equals(balEndTime)) {
            sql.append(" AND TO_DATE(T.CREATE_DATE)<=").append("TO_DATE('").append(balEndTime).append("','yyyy-MM-dd')");
        }

        if (!"".equals(whId)) {
            sql.append(" AND A.WH_ID=").append(whId);
        }

        if (!"".equals(partType)) {
            sql.append(" AND A.PRODUCE_STATE=").append(partType);
        }

        if (!"".equals(venderId)) {
            sql.append(" AND A.VENDER_ID=").append(venderId);
        }

        if (!partOldCode.equals("")) {
            sql.append(" AND UPPER(A.PART_OLDCODE) LIKE '%")
                    .append(partOldCode.toUpperCase()).append("%'\n");
        }
        if (!partName.equals("")) {
            sql.append(" AND A.PART_CNAME LIKE '%")
                    .append(partName).append("%'\n");
        }
        if (!partCode.equals("")) {
            sql.append(" AND UPPER(A.PART_CODE) LIKE '%")
                    .append(partCode.toUpperCase()).append("%'\n");
        }
        if (!"".equals(state)) {
            sql.append(" AND A.STATE=")
                    .append(state);
        }
        if (!"".equals(balanceName)) {
            sql.append(" AND U.NAME LIKE '%")
                    .append(balanceName).append("%'\n");
        }
        sql.append("  ORDER BY A.VENDER_ID, A.PART_TYPE, A.CREATE_DATE ASC \n");

        List<Map<String, Object>> list = this.pageQuery(sql.toString(), null, this.getFunName());
        return list;
    }

    public List<Map<String, Object>> getBalInfo4Update(String balCode) throws Exception {
        List<Map<String, Object>> list;
        try {
            StringBuffer sql = new StringBuffer("");

            sql.append(" SELECT T1.IN_ID, \n");
            sql.append("        T1.CHECK_CODE, \n");
            sql.append("        T1.IN_CODE, \n");
            sql.append("        T1.ORDER_CODE, \n");
            sql.append("        T1.PART_OLDCODE, \n");
            sql.append("        T1.PART_CODE, \n");
            sql.append("        T1.PART_CNAME, \n");
            sql.append("        T1.UNIT, \n");
            sql.append("        T1.VENDER_NAME, \n");
            sql.append("        T3.MAKER_NAME, \n");
            sql.append("        T1.BAL_QTY, \n");
            sql.append("        T1.IN_QTY, \n");
            sql.append("        T1.RETURN_QTY, \n");
            sql.append("        (T2.BAL_QTY-T1.BAL_QTY) BALED_QTY, \n");
            sql.append("        TO_CHAR(T1.BALANCE_DATE, 'yyyy-MM-dd') BALANCE_DATE, \n");
            sql.append("        T1.CREATE_DATE, \n");
            sql.append("        TO_CHAR(T1.IN_DATE, 'yyyy-MM-dd HH:mm:ss') IN_DATE, \n");
            sql.append("        TO_CHAR(T1.CHECK_DATE, 'yyyy-MM-dd') CHECK_DATE, \n");
            sql.append("        T4.NAME, \n");
            sql.append("        T1.BALANCE_TYPE, \n");
            sql.append("        T1.REMARK \n");
            sql.append("   FROM TT_PART_PO_BALANCE   T1, \n");
            sql.append("        TT_PART_PO_IN        T2, \n");
            sql.append("        TT_PART_MAKER_DEFINE T3, \n");
            sql.append("        TC_USER              T4 \n");
            sql.append("  WHERE T1.IN_ID = T2.IN_ID \n");
            sql.append("    AND T1.MAKER_ID = T3.MAKER_ID \n");
            sql.append("    AND T1.CREATE_BY = T4.USER_ID \n");
            sql.append("    AND T1.BALANCE_CODE='").append(balCode).append("'");
            sql.append("    ORDER BY T1.CHECK_CODE ASC\n");

            list = pageQuery(sql.toString(), null, getFunName());
        } catch (Exception e) {
            throw e;
        }
        return list;
    }

    public void updatePOIn(long inId, Long balQty) throws Exception {
        try {
            StringBuffer sql = new StringBuffer("");
            sql.append(" UPDATE TT_PART_PO_IN T \n");
            sql.append("    SET T.STATE = ?,T.BAL_QTY = T.BAL_QTY -").append(balQty);
            sql.append(",T.SPARE_QTY=T.SPARE_QTY+").append(balQty);
            sql.append("  WHERE T.IN_ID=").append(inId);

            List params = new ArrayList();
            params.add(Constant.PART_PURCHASE_ORDERBALANCE_STATUS_01);

            update(sql.toString(), params);
        } catch (Exception e) {
            throw e;
        }
    }

    public List<Map<String, Object>> queryPurOrderDtl(String balCode, String invo_no, String vender_name) throws Exception {
        List<Map<String, Object>> list;
        try {
            StringBuffer sql = new StringBuffer("");

            sql.append(" SELECT T.CHECK_CODE, \n");
            sql.append("        T.IN_CODE, \n");
            sql.append("        T.BALANCE_DATE, \n");
            sql.append("        DECODE(T.PRODUCE_STATE, \n").append(Constant.PART_PRODUCE_STATE_01);
            sql.append(", \n");
            sql.append("               '自制', \n");
            sql.append(Constant.PART_PRODUCE_STATE_02);
            sql.append(", \n");
            sql.append("               '国产件'') PART_TYPE, \n");
            sql.append("        T.PART_OLDCODE, \n");
            sql.append("        T.PART_CNAME, \n");
            sql.append("        T.PART_CODE, \n");
            sql.append("        T.WH_NAME, \n");
            sql.append("        T.IN_QTY, \n");
            sql.append("        T.RETURN_QTY, \n");
            sql.append("        T.BAL_QTY, \n");
            sql.append("        T.VENDER_NAME, \n");
            sql.append("        T1.MAKER_NAME, \n");
            sql.append("        TO_CHAR(T.BUY_PRICE, 'FM999,999,990.00') PLAN_PRICE, \n");
            sql.append("        TO_CHAR(T.BUY_PRICE1, 'FM999,999,990.00') BUY_PRICE, \n");
            sql.append("        TO_CHAR(T.BUY_PRICE * T.BAL_QTY, 'FM999,999,999,999,990.00') IN_AMOUNT,\n");
            sql.append("        TO_CHAR(T.BAL_AMOUNT, 'FM999,999,999,999,990.00') BAL_AMOUNT, \n");
            sql.append("        T.IN_DATE \n");
            sql.append("   FROM TT_PART_PO_BALANCE T, TT_PART_MAKER_DEFINE T1 \n");
            sql.append("  WHERE T.MAKER_ID = T1.MAKER_ID(+) \n");
            if (!"".equals(balCode)) {
                sql.append("    AND T.BALANCE_CODE like '%").append(balCode).append("%'");
            }
            if (!"".equals(vender_name)) {
                sql.append("    AND T.VENDER_NAME like '%").append(vender_name).append("%'");
            }
            if (!"".equals(invo_no)) {
                sql.append("    AND T.invo_no like '%").append(invo_no).append("%'");
            }
            sql.append(" AND T.STATE =  ").append(Constant.PART_PURCHASE_ORDERBALANCE_STATUS_04);
            list = pageQuery(sql.toString(), null, getFunName());

        } catch (Exception e) {
            throw e;
        }
        return list;
    }

    public Map<String, Object> queryBalDtlInfo(RequestWrapper request) throws Exception {
        try {
            String chkCode = CommonUtils.checkNull(request.getParamValue("CHECK_CODE"));
            String orderCode = CommonUtils.checkNull(request.getParamValue("ORDER_CODE"));
            String balanceCode = CommonUtils.checkNull(request.getParamValue("BALANCE_CODE"));
            String venderId = CommonUtils.checkNull(request.getParamValue("VENDER_ID"));
            String beginTime = CommonUtils.checkNull(request.getParamValue("beginTime"));
            String endTime = CommonUtils.checkNull(request.getParamValue("endTime"));
            String balBeginTime = CommonUtils.checkNull(request.getParamValue("balBeginTime"));
            String balEndTime = CommonUtils.checkNull(request.getParamValue("balEndTime"));
            String whId = CommonUtils.checkNull(request.getParamValue("WH_ID"));
            String partOldCode = CommonUtils.checkNull(request.getParamValue("PART_OLDCODE"));
            String partCode = CommonUtils.checkNull(request.getParamValue("PART_CODE"));
            String partTYpe = CommonUtils.checkNull(request.getParamValue("PART_TYPE"));
            String radioSelect = CommonUtils.checkNull(request.getParamValue("RADIO_SELECT"));
            String invo_no = CommonUtils.checkNull(request.getParamValue("INVO_NO"));
            String makerId = CommonUtils.checkNull(request.getParamValue("MAKER_ID"));

            StringBuffer sql = new StringBuffer("");

            if ("1".equals(radioSelect)) {
                sql.append(" SELECT COUNT(1) BAL_COUNT, \n");
                sql.append("        NVL(SUM(NVL(T1.BAL_QTY, (T1.IN_QTY - T1.RETURN_QTY))), 0) ALL_BALQTY \n");
                sql.append("   FROM TT_PART_PO_BALANCE T1 \n");
                sql.append("  WHERE T1.BALANCE_TYPE=").append(Constant.PART_BALANCE_TYPE_01);
            }

            if ("2".equals(radioSelect)) {
                sql.append(" SELECT COUNT(1) BAL_COUNT, NVL(SUM(A.BALANCE_QTY), 0) ALL_BALQTY \n");
                sql.append("   FROM (SELECT SUM(NVL(T1.BAL_QTY, (T1.IN_QTY - T1.RETURN_QTY))) BALANCE_QTY \n");
                sql.append("           FROM TT_PART_PO_BALANCE T1, TC_USER T3 \n");
                sql.append("          WHERE T1.BALANCE_TYPE = 92771001 \n");
                sql.append("            AND T1.CREATE_BY = T3.USER_ID \n");
            }
            if (!"".equals(invo_no)) {
                sql.append("  AND T1.INVO_NO LIKE '%").append(invo_no).append("%'");
            }
            if (!"".equals(chkCode)) {
                sql.append(" AND T1.CHECK_CODE LIKE '%").append(chkCode).append("%'");
            }
            if (!"".equals(orderCode)) {
                sql.append(" AND T1.ORDER_CODE LIKE '%").append(orderCode).append("%'");
            }
            if (!"".equals(balanceCode)) {
                sql.append(" AND T1.BALANCE_CODE LIKE '%").append(balanceCode).append("%'");
            }
            if (!"".equals(partOldCode)) {
                sql.append(" AND UPPER(T1.PART_OLDCODE) LIKE '%").append(partOldCode.toUpperCase()).append("%'");
            }
            if (!"".equals(partCode)) {
                sql.append(" AND UPPER(T1.PART_CODE) LIKE '%").append(partCode.toUpperCase()).append("%'");
            }
            if (!"".equals(partTYpe)) {
                sql.append(" AND T1.PART_TYPE=").append(partTYpe);
            }
            if (!"".equals(venderId)) {
                sql.append(" AND T1.VENDER_ID=").append(venderId);
            }
            if (!"".equals(makerId)) {
                sql.append(" AND T1.MAKER_ID=").append(makerId);
            }
            if (!"".equals(beginTime)) {
                sql.append(" AND TO_DATE(T1.CHECK_DATE)>=").append("TO_DATE('").append(beginTime).append("','yyyy-MM-dd')");
            }
            if (!"".equals(endTime)) {
                sql.append(" AND TO_DATE(T1.CHECK_DATE)<=").append("TO_DATE('").append(endTime).append("','yyyy-MM-dd')");
            }
            if (!"".equals(balBeginTime)) {
                sql.append(" AND TO_DATE(T1.CREATE_DATE)>=").append("TO_DATE('").append(balBeginTime).append("','yyyy-MM-dd')");
            }
            if (!"".equals(balEndTime)) {
                sql.append(" AND TO_DATE(T1.CREATE_DATE)<=").append("TO_DATE('").append(balEndTime).append("','yyyy-MM-dd')");
            }
            if (!"".equals(whId)) {
                sql.append(" AND T1.WH_ID=").append(whId);
            }
            sql.append(" AND T1.STATE<>").append(Constant.PART_PURCHASE_ORDERBALANCE_STATUS_05);
            if ("2".equals(radioSelect)) {
                sql.append("          GROUP BY T1.BALANCE_CODE, \n");
                sql.append("                   T3.NAME, \n");
                sql.append("                   T1.ORIGIN_TYPE, \n");
                sql.append("                   TRUNC(T1.CREATE_DATE), \n");
                sql.append("                   T1.INVO_NO) A \n");
            }

            return pageQueryMap(sql.toString(), null, getFunName());
        } catch (Exception e) {
            throw e;
        }
    }

    public PageResult<Map<String, Object>> queryPartMakerList(
            TtPartMakerDefinePO bean, Integer curPage, Integer pageSize) throws Exception {
        PageResult<Map<String, Object>> ps;
        try {
            StringBuffer sql = new StringBuffer("");
            sql.append(" SELECT T2.MAKER_ID, T2.MAKER_CODE, T2.MAKER_NAME \n");
            sql.append("   FROM TT_PART_MAKER_DEFINE T2 \n");
            sql.append("  WHERE EXISTS (SELECT 1 \n");
            sql.append("           FROM TT_PART_PO_IN T1, \n");
            sql.append("                (SELECT DISTINCT PO.CHK_ID, \n");
            sql.append("                                 TRUNC(PO.CREATE_DATE) CREATE_DATE, \n");
            sql.append("                                 PO.ORIGIN_TYPE, \n");
            sql.append("                                 PO.CREATE_BY \n");
            sql.append("                   FROM TT_PART_OEM_PO PO \n");
            sql.append("                  WHERE PO.STATE = ").append(Constant.PART_PURCHASE_ORDERCHK_STATUS_03).append(") M \n");
            sql.append("          WHERE T1.CHECK_ID = M.CHK_ID \n");
            sql.append("            AND T1.MAKER_ID = T2.MAKER_ID \n");
            sql.append("            AND T1.STATE = ").append(Constant.PART_PURCHASE_ORDERBALANCE_STATUS_01);
            sql.append("            AND T1.STATUS = 1) \n");


            if (!bean.getMakerCode().equals("")) {
                sql.append(" AND UPPER(T2.MAKER_CODE) LIKE '%").append(bean.getMakerCode()).append("%'\n");
            }
            if (!bean.getMakerName().equals("")) {
                sql.append(" AND T2.MAKER_NAME LIKE '%").append(bean.getMakerName()).append("%'\n");
            }

            sql.append(" ORDER BY T2.MAKER_CODE ");
            ps = pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
        } catch (Exception e) {
            throw e;
        }
        return ps;
    }

    public List<Map<String, Object>> querySpareBalDtl(RequestWrapper request) throws Exception {
        List<Map<String, Object>> list;
        try {
            StringBuffer sql = new StringBuffer("");

            String venderId = CommonUtils.checkNull(request.getParamValue("VENDER_ID"));//供应商id
            String makerId = CommonUtils.checkNull(request.getParamValue("MAKER_ID"));//制造商名称id
            String chkCode = CommonUtils.checkNull(request.getParamValue("CHK_CODE"));//验收单号
            String partOldCode = CommonUtils.checkNull(request.getParamValue("PART_OLDCODE"));//配件编码
            String partName = CommonUtils.checkNull(request.getParamValue("PART_CNAME"));//配件名称
            String partCode = CommonUtils.checkNull(request.getParamValue("PART_CODE"));//配件件号
            String beginTime = CommonUtils.checkNull(request.getParamValue("beginTime"));//验收开始时间
            String endTime = CommonUtils.checkNull(request.getParamValue("endTime"));//验收结束时间
            String planerId = CommonUtils.checkNull(request.getParamValue("PLANER_ID"));//计划员
            String origin_type = CommonUtils.checkNull(request.getParamValue("ORIGIN_TYPE"));//来源
            String inCode = CommonUtils.checkNull(request.getParamValue("inCodeS"));//入库单号
            String orderCode = CommonUtils.checkNull(request.getParamValue("orderCodeS"));//订单单号
            String inSDate = CommonUtils.checkNull(request.getParamValue("inSDate"));//入库开始时间
            String inEDate = CommonUtils.checkNull(request.getParamValue("inEDate"));//入库结束时间
            String isSeam = CommonUtils.checkNull(request.getParamValue("isSeam"));//制造商与供应商相同

            sql.append(" SELECT A.IN_ID, \n");
            sql.append("        A.CHECK_ID, \n");
            sql.append("        A.CHECK_CODE, \n");
            sql.append("        A.ORDER_CODE, \n");
            sql.append("        A.IN_CODE, \n");
            sql.append("        A.PART_ID, \n");
            sql.append("        A.PART_TYPE, \n");
            sql.append("        A.PART_OLDCODE, \n");
            sql.append("        A.PART_CNAME, \n");
            sql.append("        A.PART_CODE, \n");
            sql.append("        A.UNIT, \n");
            sql.append("        A.IN_DATE, \n");
            sql.append("        A.VENDER_ID, \n");
            sql.append("        A.VENDER_NAME, \n");
            sql.append("        D.MAKER_NAME, \n");
            sql.append("        A.IN_QTY, \n");
            sql.append("        A.BAL_QTY, \n");//已结算数量
            sql.append("        (A.IN_QTY-A.BAL_QTY-A.RETURN_QTY) SPARE_QTY, \n");//待结算数量
            sql.append("        A.RETURN_QTY, \n");
            sql.append("        A.REMARK, \n");
            sql.append("        TO_CHAR(M.CREATE_DATE,'yyyy-MM-dd') CREATE_DATE \n");
            sql.append("   FROM TT_PART_PO_IN A, \n");
            sql.append("        TT_PART_MAKER_DEFINE D, \n");
            sql.append("        (SELECT DISTINCT PO.CHK_ID, \n");
            sql.append("                         TRUNC(PO.CREATE_DATE) CREATE_DATE, \n");
            sql.append("                         PO.ORIGIN_TYPE, \n");
            sql.append("                         PO.CREATE_BY \n");
            sql.append("           FROM TT_PART_OEM_PO PO \n");
            sql.append("          WHERE PO.STATE = ").append(Constant.PART_PURCHASE_ORDERCHK_STATUS_03).append(") M \n");
            sql.append("  WHERE A.MAKER_ID = D.MAKER_ID(+) \n");
            sql.append("    AND A.CHECK_ID = M.CHK_ID \n");
            sql.append("    AND A.STATE = ").append(Constant.PART_PURCHASE_ORDERBALANCE_STATUS_01);
            sql.append("    AND A.STATUS = 1 \n");

            if (!"".equals(chkCode)) {
                sql.append(" AND A.CHECK_CODE LIKE '%")
                        .append(chkCode.trim().toUpperCase()).append("%'\n");
            }

            if (!"".equals(beginTime)) {
                sql.append(" AND TO_DATE(M.CREATE_DATE)>=").append("TO_DATE('").append(beginTime).append("','yyyy-MM-dd')");
            }

            if (!"".equals(endTime)) {
                sql.append(" AND TO_DATE(M.CREATE_DATE)<=").append("TO_DATE('").append(endTime).append("','yyyy-MM-dd')");
            }

            if (!"".equals(venderId)) {
                sql.append(" AND A.VENDER_ID=").append(venderId);
            }

            if (!"".equals(makerId)) {
                sql.append(" AND A.MAKER_ID=").append(makerId);
            }

            if (!partOldCode.equals("")) {
                sql.append(" AND A.PART_OLDCODE LIKE '%")
                        .append(partOldCode.trim().toUpperCase()).append("%'\n");
            }
            if (!partName.equals("")) {
                sql.append(" AND A.PART_CNAME LIKE '%")
                        .append(partName).append("%'\n");
            }
            if (!partCode.equals("")) {
                sql.append(" AND A.PART_CODE LIKE '%")
                        .append(partCode.trim().toUpperCase()).append("%'\n");
            }

            if (!"".equals(planerId)) {
                //sql.append(" AND ( A.BUYER_ID =").append(planerId);
                sql.append(" AND( M.CREATE_BY=").append(planerId).append(" OR ");
                sql.append("M.ORIGIN_TYPE = DECODE(NVL((SELECT PU.IS_DIRECT\n");
                sql.append("  FROM TT_PART_USERPOSE_DEFINE PU\n");
                sql.append(" WHERE PU.USER_ID =").append(planerId);
                sql.append(" AND PU.USER_TYPE = 1),");
                sql.append(Constant.PART_BASE_FLAG_NO).append("),");
                sql.append(Constant.PART_BASE_FLAG_YES).append(",");
                sql.append(Constant.ORDER_ORIGIN_TYPE_03).append(",1))");
            }
            if (!"".equals(origin_type)) {
                sql.append(" AND A.ORIGIN_TYPE=").append(origin_type);
            }
            if (!inCode.equals("")) {
                sql.append(" AND A.IN_CODE LIKE '%")
                        .append(inCode.trim().toUpperCase()).append("%'\n");
            }
            if (!orderCode.equals("")) {
                sql.append(" AND A.ORDER_CODE LIKE '%")
                        .append(orderCode.trim().toUpperCase()).append("%'\n");
            }

            if (!"".equals(inSDate)) {
                sql.append(" AND TO_DATE(A.IN_DATE)>=").append("TO_DATE('").append(inSDate).append("','yyyy-MM-dd')");
            }

            if (!"".equals(inEDate)) {
                sql.append(" AND TO_DATE(A.IN_DATE)<=").append("TO_DATE('").append(inEDate).append("','yyyy-MM-dd')");
            }

            if ("1".equals(isSeam)) {
                sql.append(" AND A.MAKER_ID=A.VENDER_ID");
            }

            if ("0".equals(isSeam)) {
                sql.append(" AND A.MAKER_ID<>A.VENDER_ID");
            }

            sql.append("  ORDER BY A.CHECK_CODE ,a.part_oldcode \n");

            list = pageQuery(sql.toString(), null, getFunName());

        } catch (Exception e) {
            throw e;
        }
        return list;
    }

    
    /**
     * 根据经销商ID和备件ID获取备件价格
     *
     * @param orderType
     * @param dealerId
     * @param partId
     * @return
     */
    public double getBuyPrice(Long partId, Long venderId, String inDate, Long ARGE_SUPERIOR_PURCHASING) {
        StringBuffer sql = new StringBuffer();
        sql.append(" select ");
        sql.append("  F_GET_PART_BUY_PRICE('").append(partId).append("','").append(venderId).append("','").append(inDate).append("','").append(ARGE_SUPERIOR_PURCHASING).append("') AS price ");
        sql.append(" from dual ");
        List<Map<String, Object>> list = this.pageQuery(sql.toString(), null, this.getFunName());
        if (null == list && list.size() <= 0 && null == list.get(0) || list.get(0).get("PRICE") == null) {
            return 0D;
        }
        return Double.valueOf(CommonUtils.checkNull(list.get(0).get("PRICE")));
    }
    
    public void insertMain(Long batNo) throws Exception {

        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        TmOrgPO tmOrgPO = new TmOrgPO();
        Long orgId = logonUser.getOrgId();
        tmOrgPO.setOrgId(orgId);
        tmOrgPO = (TmOrgPO) dao.select(tmOrgPO).get(0);
        String createOrgName = tmOrgPO.getOrgName();
        String createOrgCode = tmOrgPO.getOrgCode();
        Long batId = CommonUtils.parseLong(SequenceManager.getSequence(""));
        try {
            StringBuffer sql = new StringBuffer("");
            sql.append("  select    d.vender_id,                      \n");
            sql.append("         vd.vender_code,                   \n");
            sql.append("         vd.vender_name,                   \n");
            sql.append("         d.create_by,                        \n");
            sql.append("         wh_Id,     \n");
            sql.append("         sum(d.adjust_amount) amount　from tt_part_stock_chg_dtl d,     \n");
            sql.append("         tt_part_vender_define vd where d.vender_id = vd.vender_id  AND D.BAT_ID = " + batNo + "\n");
            sql.append("  group by d.wh_id,      \n");
            sql.append("          d.vender_id,   \n");
            sql.append("          vd.vender_code,\n");
            sql.append("          d.create_by,   \n");
            sql.append("          vd.vender_name \n");
            List<Map<String, Object>> mainList = dao.pageQuery(sql.toString(), null, this.getFunName());
            for (int i = 0; i < mainList.size(); i++) {
                TtPartStockChgMianPO CMPO = new TtPartStockChgMianPO();//成本调整主表
                CMPO.setChgId(CommonUtils.parseLong(SequenceManager.getSequence("")));
                CMPO.setChgCode(OrderCodeManager.getOrderCode(Constant.PART_CODE_RELATION_66));
                CMPO.setOrgId(Long.valueOf(mainList.get(i).get("VENDER_ID").toString()));//调整单位ID
                CMPO.setOrgCode(mainList.get(i).get("VENDER_CODE").toString());//调整单位编码
                CMPO.setOrgName((String) mainList.get(i).get("VENDER_NAME").toString());//调整单位名称
                CMPO.setCreateDate(new Date());
                CMPO.setCreateBy(logonUser.getUserId());
                CMPO.setChgType(4);
                CMPO.setWhId(Long.valueOf((String) mainList.get(i).get("WH_ID").toString()));
                CMPO.setTowhId(Long.valueOf((String) mainList.get(i).get("WH_ID").toString()));
                CMPO.setToorgCode(createOrgCode);//制单单位
                CMPO.setToorgId(orgId);//制单单位ID
                CMPO.setToorgName(createOrgName);//制单单位名称
                CMPO.setRemark("结算时系统自动生成差异调整");
                CMPO.setDodept(Constant.DODEPT_02);
                CMPO.setState(Constant.PART_SPCPD_STATUS_01);//已保存
                CMPO.setAdjustmentSumamount(Double.valueOf((String) mainList.get(i).get("AMOUNT").toString()));
                CMPO.setBatId(batId);
                dao.insert(CMPO);
            }
            StringBuffer upSql = new StringBuffer("");
            upSql.append("    update tt_part_stock_chg_dtl d               \n");
            upSql.append("             set d.chg_id =                      \n");
            upSql.append("                 (select chg_id                  \n");
            upSql.append("                    from Tt_Part_Stock_Chg_Mian m\n");
            upSql.append("                   where m.org_id = d.vender_id  \n");
            upSql.append("                     and m.wh_id = d.wh_id       \n");
            upSql.append("                     and m.bat_id =" + batId + ")             \n");
            upSql.append("           where d.bat_id =" + batNo + "                      \n");
            dao.update(upSql.toString(), null);
        } catch (Exception e) {
            throw e;
        }

    }
    
    public void changeBalanceCode(Long batNo) throws Exception {

        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);

        try {
            StringBuffer sql = new StringBuffer("");
            sql.append("select  fcvder_id,produce_fac  ,buyer_id,t.fcvderr_code,WH_iD  from tt_part_po_balance t where t.bat_id = '" + batNo + "' group by fcvder_id,produce_fac,WH_ID  ,buyer_id,t.fcvderr_code ");
            List<Map<String, Object>> balanceList = dao.pageQuery(sql.toString(), null, this.getFunName());
            for (int i = 0; i < balanceList.size(); i++) {
                SimpleDateFormat f = new SimpleDateFormat("yyyyMM");
                String TEMP = f.format(new Date()) + "-" + Math.round(Math.random() * 10000);
                String code = OrderCodeManager.getBalCode(balanceList.get(i).get("FCVDERR_CODE").toString());
                TtPartPoBalancePO po = new TtPartPoBalancePO();
                TtPartPoBalancePO uppo = new TtPartPoBalancePO();
                po.setBatId(batNo);
                po.setFcvderId(Long.valueOf(balanceList.get(i).get("FCVDER_ID").toString()));
                po.setProduceFac(Long.valueOf(balanceList.get(i).get("PRODUCE_FAC").toString()));
                po.setBuyerId(Long.valueOf(balanceList.get(i).get("BUYER_ID").toString()));
                po.setWhId(Long.valueOf(balanceList.get(i).get("WH_ID").toString()));
                uppo.setBalanceCode(code);
                dao.update(po, uppo);

            }
        } catch (Exception e) {
            throw e;
        }

    }
}
