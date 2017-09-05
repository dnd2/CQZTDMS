package com.infodms.dms.dao.parts.financeManager.partInvoiceQuery;

import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.po.TtIfStandardPO;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;
import org.apache.log4j.Logger;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

/**
 * @author : huchao
 *         CreateDate     : 2013-5-3
 * @ClassName : invoiceQueryDao
 */
public class invoiceQueryDao extends BaseDao {
    public static Logger logger = Logger.getLogger(invoiceQueryDao.class);

    private static final invoiceQueryDao dao = new invoiceQueryDao();

    private invoiceQueryDao() {
    }

    public static final invoiceQueryDao getInstance() {
        return dao;
    }

    protected TtIfStandardPO wrapperPO(ResultSet rs, int idx) {
        return null;
    }

    /**
     * @param : @param sbString
     * @param : @param pageSize
     * @param : @param curPage
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-7-10
     * @Title : 财务开票信息查询
     */
    public PageResult<Map<String, Object>> queryInvoices(String sbString, int pageSize, int curPage) {
        StringBuffer sql = new StringBuffer("");

        sql.append("SELECT OM.SO_CODE,\n");
        sql.append("       OM.BILL_NO,\n");
        sql.append("       /*  BM.TAX,*/\n");
        sql.append("       OM.BILL_DATE,\n");
        sql.append("       OM.BILL_BY,\n");
        sql.append("       TO_CHAR(OM.AMOUNT, 'FM999,999,999,990.00') AS BILL_AMOUNT,\n");
        sql.append("       OM.DEALER_NAME,\n");
        sql.append("       OM.DEALER_CODE,\n");
        sql.append("       BD.INV_TYPE\n");
        sql.append("  FROM TT_PART_OUTSTOCK_MAIN OM, TT_PART_BILL_DEFINE BD\n");
        sql.append(" WHERE 1 = 1\n");
        sql.append("   AND OM.DEALER_ID = BD.DEALER_ID\n");
        sql.append(sbString);
        sql.append(" ORDER BY OM.BILL_DATE DESC ");

        PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null,
                getFunName(), pageSize, curPage);
        return ps;
    }

    /**
     * @param : @param sbString
     * @param : @param pageSize
     * @param : @param curPage
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-8-20
     * @Title : 汇总查询
     */
    public PageResult<Map<String, Object>> countInvoices(String sbString, int pageSize, int curPage) {
        StringBuffer sql = new StringBuffer("");

        sql.append("SELECT TO_CHAR(OM.BILL_DATE, 'yyyy-MM') AS BILL_MONTH,\n");
        sql.append("       TO_CHAR(SUM(OM.AMOUNT), 'FM999,999,999,990.00') AS BILL_AMOUNT,\n");
        sql.append("       OM.DEALER_NAME,\n");
        sql.append("       OM.DEALER_CODE\n");
        sql.append("  FROM TT_PART_OUTSTOCK_MAIN OM\n");
        sql.append(" WHERE 1 = 1\n");
        sql.append(sbString);
        sql.append("  GROUP BY OM.DEALER_CODE, OM.DEALER_NAME, TO_CHAR(OM.BILL_DATE, 'yyyy-MM') ");

        PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null,
                getFunName(), pageSize, curPage);
        return ps;
    }

    /**
     * @param : @param sbString
     * @param : @param pageSize
     * @param : @param curPage
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-11-5
     * @Title : 汇总查询List
     */
    public List<Map<String, Object>> countInvoList(String sbString) {
        StringBuffer sql = new StringBuffer("");

        sql.append("SELECT TO_CHAR(OM.BILL_DATE, 'yyyy-MM') AS BILL_MONTH,\n");
        sql.append("       TO_CHAR(SUM(OM.AMOUNT), 'FM999,999,999,990.00') AS BILL_AMOUNT,\n");
        sql.append("       OM.DEALER_NAME,\n");
        sql.append("       OM.DEALER_CODE\n");
        sql.append("  FROM TT_PART_OUTSTOCK_MAIN OM\n");
        sql.append(" WHERE 1 = 1\n");
        sql.append(sbString);
        sql.append("  GROUP BY OM.DEALER_CODE, OM.DEALER_NAME, TO_CHAR(OM.BILL_DATE, 'yyyy-MM') ");

        List<Map<String, Object>> list = pageQuery(sql.toString(), null, getFunName());

        return list;
    }

    /**
     * @param : @param sbString
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-7-10
     * @Title : 返回财务开票信息List
     */
    public List<Map<String, Object>> queryInvoicesList(String sbString) {
        StringBuffer sql = new StringBuffer("");

        sql.append("SELECT OM.SO_CODE,\n");
        sql.append("       OM.BILL_NO,\n");
        sql.append("       /*  BM.TAX,*/\n");
        sql.append("       OM.BILL_DATE,\n");
        sql.append("       OM.BILL_BY,\n");
        sql.append("       TO_CHAR(OM.AMOUNT, 'FM999,999,999,990.00') AS BILL_AMOUNT,\n");
        sql.append("       OM.DEALER_NAME,\n");
        sql.append("       OM.DEALER_CODE,\n");
        sql.append("       BD.INV_TYPE\n");
        sql.append("  FROM TT_PART_OUTSTOCK_MAIN OM, TT_PART_BILL_DEFINE BD\n");
        sql.append(" WHERE 1 = 1\n");
        sql.append("   AND OM.DEALER_ID = BD.DEALER_ID\n");
        sql.append(sbString);
        sql.append(" ORDER BY OM.BILL_DATE DESC ");

        List<Map<String, Object>> list = pageQuery(sql.toString(), null, getFunName());
        return list;
    }

    public PageResult<Map<String, Object>> queryInvoicePatrt(String sbString, int pageSize, int curPage) {
        StringBuffer sql = new StringBuffer("");
        //modify by yuan 20130922 start
        /*sql.append("SELECT BD.PART_ID, BD.PART_OLDCODE, BD.PART_CNAME, BD.PART_CODE, BD.UNIT, BD.BILL_QTY, ");
        sql.append(" TO_CHAR(BD.TAX,'FM0.00') AS TAX, TO_CHAR(BD.DISCOUNT,'FM0.00') AS DISCOUNT, ");
		sql.append(" TO_CHAR(BD.BILL_AMUNT, 'FM999,999,990.00') AS BILL_AMUNT, ");
		sql.append(" TO_CHAR(BD.BILL_AMOUNTNOTAX, 'FM999,999,990.00') AS BILL_AMOUNTNOTAX, ");
		sql.append(" TO_CHAR(BD.TAX_AMOUNT, 'FM999,999,990.00') AS TAX_AMOUNT ");
		sql.append(" FROM TT_PART_BILL_DTL BD");
		sql.append(" WHERE 1 = 1 ");*/

        sql.append("SELECT M.SO_CODE, to_char(M.AMOUNT,'fm999,999,999.00') AMOUNT, M.CREATE_DATE\n");
        sql.append("  FROM TT_PART_OUTSTOCK_MAIN M\n");
        sql.append(" WHERE 1 = 1\n");

        sql.append(sbString);
        //sql.append(" ORDER BY BD.PART_CODE ");
        //end

        PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null,
                getFunName(), pageSize, curPage);
        return ps;
    }

    //明细下载 add zhumingwei 2013-09-28
    public List<Map<String, Object>> queryInvoicesDetailList(String sbString) {
        StringBuffer sql = new StringBuffer("");


		/*sql.append("SELECT OM.DEALER_CODE,\n");
        sql.append("       OM.DEALER_NAME,\n");
		sql.append("       (select t.code_desc from tc_code t where t.code_id=TPD.PART_TYPE) PART_TYPE,\n"); 
		sql.append("       SUM(OD.SALE_AMOUNT) SALE_AMOUNT\n"); 
		sql.append("  FROM Tt_Part_Outstock_Main OM,\n"); 
		sql.append("       TT_PART_OUTSTOCK_DTL  OD,\n"); 
		sql.append("       TT_PART_DEFINE        TPD,tt_part_bill_define d\n"); 
		sql.append(" WHERE OM.OUT_ID = OD.OUT_ID and om.dealer_id=d.dealer_id\n"); 
		sql.append("   AND OD.PART_ID = TPD.PART_ID\n"); 
		sql.append("   AND OM.BILL_NO IS NOT NULL\n"); 
		sql.append(sbString);
		sql.append(" GROUP BY OM.DEALER_CODE,OM.DEALER_NAME, TPD.PART_TYPE\n"); 
		sql.append(" ORDER BY OM.DEALER_NAME\n");*/


        sql.append("SELECT A.*\n");
        sql.append("  FROM (SELECT OM.DEALER_CODE,\n");
        sql.append("               OM.DEALER_NAME,\n");
        sql.append("               (SELECT T.CODE_DESC\n");
        sql.append("                  FROM TC_CODE T\n");
        sql.append("                 WHERE T.CODE_ID = TPD.PART_TYPE) PART_TYPE,\n");
        sql.append("               TRUNC(OM.BILL_DATE) BILL_DATE,\n");
        sql.append("               OM.BILL_NO,\n");
        sql.append("               SUM(OD.SALE_AMOUNT) SALE_AMOUNT\n");
        sql.append("          FROM TT_PART_OUTSTOCK_MAIN OM,\n");
        sql.append("               TT_PART_OUTSTOCK_DTL  OD,\n");
        sql.append("               TT_PART_DEFINE        TPD,\n");
        sql.append("               TT_PART_BILL_DEFINE   D\n");
        sql.append("         WHERE OM.OUT_ID = OD.OUT_ID\n");
        sql.append("           AND OM.DEALER_ID = D.DEALER_ID\n");
        sql.append("           AND OD.PART_ID = TPD.PART_ID\n");
        sql.append("           AND OM.SELLER_ID = 2010010100070674\n");
        sql.append("           AND OM.BILL_NO IS NOT NULL\n");
        sql.append(sbString);
        sql.append("         GROUP BY OM.DEALER_CODE,\n");
        sql.append("                  OM.DEALER_NAME,\n");
        sql.append("                  TPD.PART_TYPE,\n");
        sql.append("                  TRUNC(OM.BILL_DATE),\n");
        sql.append("                  OM.BILL_NO\n");
        sql.append("        UNION\n");
        sql.append("        SELECT OM.DEALER_CODE,\n");
        sql.append("               OM.DEALER_NAME,\n");
        sql.append("               '运费' PART_TYPE,\n");
        sql.append("               TRUNC(OM.BILL_DATE) BILL_DATE,\n");
        sql.append("               OM.BILL_NO,\n");
        sql.append("               SUM(OM.FREIGHT) SALE_AMOUNT\n");
        sql.append("          FROM TT_PART_OUTSTOCK_MAIN OM,\n");
        sql.append("               TT_PART_BILL_DEFINE   D\n");
        sql.append("         WHERE OM.BILL_NO IS NOT NULL\n");
        sql.append("           AND OM.DEALER_ID = D.DEALER_ID\n");
        sql.append(sbString);
        sql.append("           AND OM.SELLER_ID = 2010010100070674 HAVING\n");
        sql.append("         SUM(OM.FREIGHT) > 0\n");
        sql.append("         GROUP BY OM.DEALER_CODE,\n");
        sql.append("                  OM.DEALER_NAME,\n");
        sql.append("                  TRUNC(OM.BILL_DATE),\n");
        sql.append("                  OM.BILL_NO) A\n");
        sql.append(" ORDER BY A.DEALER_CODE, A.DEALER_NAME\n");

        List<Map<String, Object>> list = pageQuery(sql.toString(), null, getFunName());
        return list;
    }

    public PageResult<Map<String, Object>> getInvoiceAmount(RequestWrapper request, int pageSize, int curPage) {
        StringBuffer sql = new StringBuffer();
        String dealerName = CommonUtils.checkNull(request.getParamValue("dealerName"));
        String dealerCode = CommonUtils.checkNull(request.getParamValue("dealerCode"));
        String startDate = CommonUtils.checkNull(request.getParamValue("checkSDate"));
        String endDate = CommonUtils.checkNull(request.getParamValue("checkEDate"));
        String salerId = CommonUtils.checkNull(request.getParamValue("salerId"));
        String dlrInvTpe = CommonUtils.checkNull(request.getParamValue("dlrInvTpe")); // 开票类型
        String is_bns = CommonUtils.checkNull(request.getParamValue("is_bns")); // 开票类型


        sql.append("SELECT TD.DEALER_ID, TD.DEALER_NAME, TD.DEALER_CODE\n");
        sql.append("  FROM TM_DEALER TD, TT_PART_BILL_DEFINE BD\n");
        sql.append(" WHERE TD.DEALER_ID = BD.DEALER_ID\n");
        sql.append("   AND TD.DEALER_TYPE = 10771002\n");
        sql.append("   AND TD.DEALER_LEVEL = 10851001\n");
        if (!"".equals(salerId) && salerId != null) {
            sql.append("   AND EXISTS (SELECT 1\n");
            sql.append("          FROM TT_PART_SALESSCOPE_DEFINE SD\n");
            sql.append("         WHERE SD.DEALER_ID = TD.DEALER_ID\n");
            sql.append("           AND SD.USER_ID = ").append(salerId).append(")");
        }

        if (!"".equals(dealerName)) {
            sql.append(" and TD.dealer_name like '%").append(dealerName).append("%'");
        }
        if (!"".equals(dealerCode)) {
            sql.append(" and TD.dealer_code like '%").append(dealerCode).append("%'");
        }
        if (null != dlrInvTpe && !"".equals(dlrInvTpe)) {
            sql.append(" AND BD.INV_TYPE = '" + dlrInvTpe + "' ");
        }
        if (null != is_bns && !"".equals(is_bns)) {
            if (is_bns.equals(Constant.IF_TYPE_YES + "")) {
                sql.append("AND EXISTS (SELECT 1\n");
                sql.append("         FROM TT_PART_SO_MAIN M\n");
                sql.append("        WHERE M.DEALER_ID = TD.DEALER_ID\n");
                sql.append("          AND M.SELLER_ID = 2010010100070674)\n");
            } else if (is_bns.equals(Constant.IF_TYPE_NO + "")) {
                sql.append("\tAND NOT EXISTS (SELECT 1\n");
                sql.append("        FROM TT_PART_SO_MAIN M\n");
                sql.append("       WHERE M.DEALER_ID = TD.DEALER_ID\n");
                sql.append("         AND M.SELLER_ID = 2010010100070674)\n");
            }
        }
        PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
        return ps;
    }

    public List<Map<String, Object>> getInvoiceAmount(RequestWrapper request, String dealerIds) {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT TD.DEALER_ID,\n");
        sql.append("       TD.DEALER_NAME,\n");
        sql.append("       TD.DEALER_CODE,\n");
        sql.append("       BD.MAIL_ADDR ADDR,\n");
        sql.append("       AD.POST_CODE,\n");
        sql.append("       AD.TEL\n");
        sql.append("  FROM TT_PART_BILL_DEFINE BD, TT_PART_ADDR_DEFINE AD, TM_DEALER TD\n");
        sql.append(" WHERE 1 = 1\n");
        sql.append("   AND TD.DEALER_ID = BD.DEALER_ID\n");
        sql.append("   AND TD.DEALER_ID = AD.DEALER_ID\n");
        sql.append("   AND TD.DEALER_ID in (").append(dealerIds).append(")");

        List<Map<String, Object>> list = pageQuery(sql.toString(), null, getFunName());
        return list;
    }
}
