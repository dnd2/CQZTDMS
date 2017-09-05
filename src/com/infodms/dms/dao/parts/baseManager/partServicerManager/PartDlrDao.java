package com.infodms.dms.dao.parts.baseManager.partServicerManager;

import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.po.TtPartDealerCartypePO;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;
import org.apache.log4j.Logger;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

public class PartDlrDao extends BaseDao {
    public static Logger logger = Logger.getLogger(PartDlrDao.class);
    private static final PartDlrDao dao = new PartDlrDao();

    private PartDlrDao() {
    }

    public static final PartDlrDao getInstance() {
        return dao;
    }

    @Override
    protected PO wrapperPO(ResultSet rs, int idx) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     */
    public PageResult<Map<String, Object>> queryDealerInfo(RequestWrapper request, int curPage, int pageSize) throws Exception {
        String dealerCode = request.getParamValue("DEALER_CODE");
        String dealerName = request.getParamValue("DEALER_NAME");
        String status = request.getParamValue("DEALERSTATUS");

        StringBuffer sql = new StringBuffer();

        sql.append("WITH DEALER_CARTYPE AS\n");
        sql.append(" (SELECT DC.DEALER_ID, ZA_CONCAT(DISTINCT FD.FIX_NAME) CARTYPE\n");
        sql.append("    FROM TT_PART_DEALER_CARTYPE DC, TT_PART_FIXCODE_DEFINE FD\n");
        sql.append("   WHERE DC.CAR_TYPE = FD.FIX_VALUE(+)\n");
        sql.append("     AND FD.FIX_GOUPTYPE = 92251010\n");
        sql.append("   GROUP BY DC.DEALER_ID)\n");
        sql.append("SELECT TD.DEALER_ID, TD.DEALER_CODE, TD.DEALER_NAME, DC.CARTYPE\n");
        sql.append("  FROM TM_DEALER TD, DEALER_CARTYPE DC\n");
        sql.append(" WHERE 1 = 1\n");
        sql.append("   AND TD.DEALER_ID = DC.DEALER_ID(+)\n");
        sql.append("   AND TD.DEALER_TYPE = 10771002\n");

        if (dealerCode != null && !"".equals(dealerCode)) {
            sql.append("  AND TD.DEALER_CODE LIKE '%" + dealerCode.trim().toUpperCase() + "%'\n");
        }
        if (dealerName != null && !"".equals(dealerName)) {
            sql.append("  AND TD.DEALER_NAME LIKE '%" + dealerName + "%'\n");
        }
        if (!"".equals(status) && null != status) {
            sql.append("  AND TD.service_status = '" + status + "'\n");
        }
        sql.append("  ORDER BY TD.DEALER_CODE\n");
        PageResult<Map<String, Object>> rs = dao.pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
        return rs;
    }

    /**
     * 查询经销商地址
     *
     * @param dealerId 经销商Id
     * @return
     * @throws Exception
     */
    public List<Map<String, Object>> getAddress(String dealerId) throws Exception {

        StringBuffer sql = new StringBuffer();
        sql.append("SELECT T.ADDR_ID,\n");
        sql.append("       T.ADDR,\n");
        sql.append("       T.LINKMAN,\n");
        sql.append("       T.TEL,\n");
        sql.append("       T.STATUS,T.STATE\n");
        sql.append("  FROM tt_part_addr_define t\n");
        sql.append(" WHERE T.DEALER_ID = " + dealerId);
        logger.debug("SQL+++++++++++++++++++++++查询经销商地址: " + sql.toString());
        return pageQuery(sql.toString(), null, getFunName());
    }

    // 查询开票信息
    public Map<String, Object> getInvoiceInfo(String dealerId) throws Exception {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT T.TAX_NAME,\n");
        sql.append("       T.TAX_NO,\n");
        sql.append("       T.BANK,\n");
        sql.append("       T.ACCOUNT,\n");
        sql.append("       T.ADDR,\n");
        sql.append("       T.MAIL_ADDR,\n");
        sql.append("       T.TEL,\n");
        sql.append("       T.INV_TYPE\n");
        sql.append("  FROM tt_part_bill_define T\n");
        sql.append(" WHERE T.DEALER_ID = " + dealerId);

        return pageQueryMap(sql.toString(), null, getFunName());
    }

    /**
     * 获取服务商已设置的对应车型
     *
     * @param dealerId
     * @return
     */
    public String getDlrCarTypes(String dealerId) {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT za_concat(DC.CAR_TYPE) CAR_TYPE\n");
        sql.append("  FROM TT_PART_DEALER_CARTYPE DC\n");
        sql.append(" WHERE DC.DEALER_ID = '" + dealerId + "'\n");
        List<Map<String, Object>> dlrCarTypeList = dao.pageQuery(sql.toString(), null, getFunName());

        if (dlrCarTypeList != null && dlrCarTypeList.size() > 0) {
            return dlrCarTypeList.get(0).get("CAR_TYPE") + "";
        }

        return "";
    }

    /**
     * 删除已经存在的
     *
     * @param al dealerId 列表
     */
    public void delete(List<Object> al) {
        for (Object dealerId : al.toArray()) {
            TtPartDealerCartypePO cartypePO = new TtPartDealerCartypePO();
            cartypePO.setDealerId(Long.valueOf(dealerId.toString()));

            dao.delete(cartypePO);

        }
    }


}
