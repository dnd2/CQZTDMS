package com.infodms.dms.dao.parts.purchaseOrderManager;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.po.TtPartWarehouseDefinePO;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;
import org.apache.log4j.Logger;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

public class InventoryOperationDao extends BaseDao<PO> {
    public static Logger logger = Logger.getLogger(InventoryOperationDao.class);
    private static final InventoryOperationDao dao = new InventoryOperationDao();

    private InventoryOperationDao() {
    }

    public static final InventoryOperationDao getInstance() {
        return dao;
    }

    protected PO wrapperPO(ResultSet rs, int idx) {
        return null;
    }


    /*
     * 库房查询
     */
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
     * @param : @param request
     * @param : @param curPage
     * @param : @param pageSize
     * @param : @return
     * @return :
     * @throws : LastDate    : 2014-3-31
     * @Title :
     * @Description: 库存运行情况查询
     */

    public PageResult<Map<String, Object>> queryDatas(RequestWrapper request, int curPage, int pageSize) {


        StringBuffer sql = this.getSql(request);

        PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
        return ps;
    }

    public List<Map<String, Object>> getDatas(RequestWrapper request) {

        StringBuffer sql = this.getSql(request);

        List<Map<String, Object>> ps = this.pageQuery(sql.toString(), null, this.getFunName());
        return ps;
    }

    public StringBuffer getSql(RequestWrapper request) {
        String whId = CommonUtils.checkNull(request.getParamValue("WH_ID"));//经销商编码
        String startDate = CommonUtils.checkNull(request.getParamValue("startDate"));//期初日期
        String endDate = CommonUtils.checkNull(request.getParamValue("endDate"));//发运方式

        StringBuffer sql = new StringBuffer();

        sql.append("WITH QC_QM_INFO AS\n");
        sql.append(" (SELECT SUM(NVL(QC.QC_QTY, 0)) QC_PART_QTYS,\n");
        sql.append("         SUM(DECODE(SIGN(NVL(QC.QC_QTY, 0)), 1, 1, 0)) QC_PARTTYPES,\n");
        sql.append("         SUM(NVL(QC.QC_QTY, 0) * SP.SALE_PRICE1) QC_PART_AMOUNTS,\n");
        sql.append("         SUM(NVL(QC.QC_QTY, 0) + NVL(RK.IN_QTY, 0) - NVL(CK.OUT_QTY, 0)) QM_PART_QTYS,\n");
        sql.append("         SUM(DECODE(SIGN((NVL(QC.QC_QTY, 0) + NVL(RK.IN_QTY, 0) -\n");
        sql.append("                         NVL(CK.OUT_QTY, 0))),\n");
        sql.append("                    1,\n");
        sql.append("                    1,\n");
        sql.append("                    0)) QM_PARTTYPES,\n");
        sql.append("         SUM((NVL(QC.QC_QTY, 0) + NVL(RK.IN_QTY, 0) - NVL(CK.OUT_QTY, 0)) *\n");
        sql.append("             SP.SALE_PRICE1) QM_PART_AMOUNTS\n");
        sql.append("    FROM (SELECT S.PART_ID, S.WH_ID, S.ITEM_QTY QC_QTY\n");
        sql.append("            FROM TT_PART_RP_INVREPORT S\n");
        sql.append("           WHERE 1 = 1\n");
        if (!"".equals(startDate) && null != startDate) {
            sql.append("             AND S.CREATE_DATE = TO_DATE('2014-11-01', 'yyyy-mm-dd') - 1 --起始日期-1天\n");
        }
        sql.append("             AND S.WH_ID = " + whId + ") QC,\n");
        sql.append("         (SELECT PID.PART_ID, PID.WH_ID, SUM(PID.PART_NUM) IN_QTY\n");
        sql.append("            FROM VW_PART_INSTOCK_DTL PID\n");
        sql.append("           WHERE 1 = 1\n");
        sql.append("             AND PID.WH_ID = " + whId + "\n");
        if (!"".equals(startDate) && null != startDate) {
            sql.append("             AND TRUNC(PID.CREATE_DATE) >=\n");
            sql.append("                 TO_DATE('" + startDate + "', 'yyyy-mm-dd') --起始日期\n");
        }
        if (!"".equals(endDate) && null != endDate) {
            sql.append("             AND TRUNC(PID.CREATE_DATE) <=\n");
            sql.append("                 TO_DATE('" + endDate + "', 'yyyy-mm-dd') --截至日期\n");
        }
        sql.append("           GROUP BY PID.PART_ID, PID.WH_ID) RK,\n");
        sql.append("         (SELECT D.PART_ID, D.WH_ID, SUM(D.PART_NUM) OUT_QTY\n");
        sql.append("            FROM VW_PART_OUTSTOCK_DTL D\n");
        sql.append("           WHERE 1 = 1\n");
        sql.append("             AND D.WH_ID = " + whId + "\n");
        if (!"".equals(startDate) && null != startDate) {
            sql.append("             AND TRUNC(D.CREATE_DATE) >= TO_DATE('" + startDate + "', 'yyyy-mm-dd') --起始日期\n");
        }
        if (!"".equals(endDate) && null != endDate) {
            sql.append("             AND TRUNC(D.CREATE_DATE) <= TO_DATE('" + endDate + "', 'yyyy-mm-dd') --截至日期\n");
        }
        sql.append("           GROUP BY D.PART_ID, D.WH_ID) CK,\n");
        sql.append("         TT_PART_DEFINE TPD,\n");
        sql.append("         TT_PART_SALES_PRICE SP\n");
        sql.append("   WHERE TPD.PART_ID = QC.PART_ID(+)\n");
        sql.append("     AND TPD.PART_ID = RK.PART_ID(+)\n");
        sql.append("     AND TPD.PART_ID = CK.PART_ID(+)\n");
        sql.append("     AND TPD.PART_ID = SP.PART_ID\n");
        sql.append("     AND EXISTS (SELECT 1\n");
        sql.append("            FROM TT_PART_ITEM_STOCK S\n");
        sql.append("           WHERE S.PART_ID = TPD.PART_ID\n");
        sql.append("             AND S.WH_ID = " + whId + ")),\n");
        sql.append("FY_INFO AS\n");
        sql.append(" (SELECT COUNT(DISTINCT TD.PART_ID) FY_PARTTYPES,\n");
        sql.append("         SUM(TD.OUTSTOCK_QTY) FY_PART_QTYS,\n");
        sql.append("         SUM(TD.SALE_AMOUNT) FY_PART_AMOUNTS\n");
        sql.append("    FROM VW_PART_TRANS_DTL TD\n");
        sql.append("   WHERE TD.WH_ID = '" + whId + "'\n");
        if (!"".equals(startDate) && null != startDate) {
            sql.append("     AND TRUNC(TD.SUBMIT_DATE) >= TO_DATE('" + startDate + "', 'yyyy-mm-dd')\n");
        }
        if (!"".equals(endDate) && null != endDate) {
            sql.append("     AND TRUNC(TD.SUBMIT_DATE) <= TO_DATE('" + endDate + "', 'yyyy-mm-dd')),\n");
        }
        sql.append("TH_INFO　AS\n");
        sql.append(" (SELECT COUNT(DISTINCT R.PART_ID) TH_PARTTYPES,\n");
        sql.append("         NVL(SUM(R.RETURN_QTY), 0) TH_PART_QTYS,\n");
        sql.append("         NVL(SUM(R.RETURN_QTY * RD.BUY_PRICE), 0) TH_PART_AMOUNTS\n");
        sql.append("    FROM TT_PART_RETURN_RECORD R, TT_PART_DLR_RETURN_DTL RD\n");
        sql.append("   WHERE R.RETURN_ID = RD.RETURN_ID\n");
        sql.append("     AND R.PART_ID = RD.PART_ID\n");
        sql.append("     AND R.RETURN_TYPE = 92391001\n");
        sql.append("        --AND R.STOCK_IN = '2013061319370891'\n");
        if (!"".equals(startDate) && null != startDate) {
            sql.append("     AND TRUNC(R.CREATE_DATE) >= TO_DATE('" + startDate + "', 'yyyy-mm-dd')\n");
        }
        if (!"".equals(endDate) && null != endDate) {
            sql.append("     AND TRUNC(R.CREATE_DATE) <= TO_DATE('" + endDate + "', 'yyyy-mm-dd'))\n");
        }
        sql.append("SELECT C.QC_PARTTYPES,\n");
        sql.append("       Y.FY_PARTTYPES,\n");
        sql.append("       H.TH_PARTTYPES,\n");
        sql.append("       C.QM_PARTTYPES,\n");
        sql.append("       C.QC_PART_QTYS,\n");
        sql.append("       Y.FY_PART_QTYS,\n");
        sql.append("       H.TH_PART_QTYS,\n");
        sql.append("       C.QM_PART_QTYS,\n");
        sql.append("       C.QC_PART_AMOUNTS,\n");
        sql.append("       Y.FY_PART_AMOUNTS,\n");
        sql.append("       H.TH_PART_AMOUNTS,\n");
        sql.append("       C.QM_PART_AMOUNTS\n");
        sql.append("  FROM QC_QM_INFO C, FY_INFO Y, TH_INFO H");

        return sql;
    }

}
