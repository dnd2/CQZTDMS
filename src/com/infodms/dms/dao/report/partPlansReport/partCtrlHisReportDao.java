package com.infodms.dms.dao.report.partPlansReport;

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
 *         CreateDate     : 2013-11-20
 * @ClassName : partCtrlHisReportDao
 */
public class partCtrlHisReportDao extends BaseDao {
    public static Logger logger = Logger.getLogger(partCtrlHisReportDao.class);
    private static final partCtrlHisReportDao dao = new partCtrlHisReportDao();

    private partCtrlHisReportDao() {
    }

    public static final partCtrlHisReportDao getInstance() {
        return dao;
    }

    private static final int enableValue = Constant.STATUS_ENABLE;//有效

    protected TtIfStandardPO wrapperPO(ResultSet rs, int idx) {
        return null;
    }

    /**
     * @param : @param sbString
     * @param : @param pageSize
     * @param : @param curPage
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-7-31
     * @Title : 调拨历史查询
     */
    public PageResult<Map<String, Object>> queryCtrlHisInfos(RequestWrapper request, int pageSize, int curPage) {
        String planner = CommonUtils.checkNull(request
                .getParamValue("planner")); // 计划员
        String whId = CommonUtils.checkNull(request
                .getParamValue("WH_ID")); // 仓库
        String venderName = CommonUtils.checkNull(request
                .getParamValue("VENDER_NAME")); // 供应商
        String partType = CommonUtils.checkNull(request
                .getParamValue("PART_TYPE")); // 配件类型
        String checkSDate = CommonUtils.checkNull(request
                .getParamValue("checkSDate")); // 开始时间
        String checkEDate = CommonUtils.checkNull(request
                .getParamValue("checkEDate")); // 截止时间
        String state = CommonUtils.checkNull(request
                .getParamValue("state")); // 制单状态
        String partOldcode = CommonUtils.checkNull(request
                .getParamValue("partOldcode")); // 配件编码
        String partCode = CommonUtils.checkNull(request
                .getParamValue("partCode")); // 配件件号
        String partCname = CommonUtils.checkNull(request
                .getParamValue("partCname")); // 配件名称
        String whMan = CommonUtils.checkNull(request
                .getParamValue("whMan")); // 保管员

        StringBuffer sql = new StringBuffer("");

        sql.append(" SELECT PM.ORDER_CODE, ");
        sql.append(" PM.PUR_ORDER_CODE, ");
        sql.append(" PM.BUYER, ");
        sql.append(" PM.WH_NAME, ");
        sql.append(" PM.STATE, ");
        sql.append(" PD.PART_CODE, ");
        sql.append(" PD.PART_OLDCODE, ");
        sql.append(" PD.PART_CNAME, ");
        sql.append(" PD.BUY_QTY, ");
        sql.append(" PD.CHECK_QTY, ");
        sql.append(" PD.VENDER_NAME, ");
        sql.append(" SD.WHMAN, ");
        sql.append(" SD.ROOM, ");
        sql.append(" (SELECT OEP.CHK_CODE FROM ");
        sql.append(" TT_PART_OEM_PO OEP WHERE PM.ORDER_ID = OEP.ORDER_ID AND PD.PART_ID = OEP.PART_ID ) AS CHK_CODE, ");
        sql.append(" NVL((SELECT OEP.CHECK_QTY FROM ");
        sql.append(" TT_PART_OEM_PO OEP WHERE PM.ORDER_ID = OEP.ORDER_ID AND PD.PART_ID = OEP.PART_ID ), '0') AS CHECK_QTY_IN, ");
        sql.append(" (SELECT OEP.CREATE_DATE FROM ");
        sql.append(" TT_PART_OEM_PO OEP WHERE PM.ORDER_ID = OEP.ORDER_ID AND PD.PART_ID = OEP.PART_ID ) AS CREATE_DATE, ");
        sql.append(" (SELECT max(OEP.PRINT_DATE2) FROM ");
        sql.append(" TT_PART_OEM_PO OEP WHERE PM.ORDER_ID = OEP.ORDER_ID AND PD.PART_ID = OEP.PART_ID ) AS PRINT_DATE, ");
        sql.append(" VW.ITEM_QTY ");
        sql.append(" FROM TT_PART_PO_MAIN PM, TT_PART_PO_DTL PD, TT_PART_DEFINE TD, TC_CODE TC, VW_PART_STOCK VW, TT_PART_SPPLAN_DEFINE SD ");
        sql.append(" WHERE PM.PLAN_TYPE = '92111111' ");
        sql.append(" AND PM.ORDER_ID = PD.ORDER_ID ");
        sql.append(" AND PD.PART_ID = TD.PART_ID ");
        sql.append(" AND TD.PART_TYPE = TC.CODE_ID ");
        sql.append(" AND PD.PART_ID = VW.PART_ID ");
        sql.append(" AND PM.WH_ID = VW.WH_ID ");
        sql.append(" AND PD.PART_ID = SD.PART_ID(+)");

        if (null != venderName && !"".equals(venderName)) {
            sql.append(" AND PD.VENDER_NAME LIKE '%" + venderName.trim() + "%' ");
        }
        if (null != planner && !"".equals(planner)) {
            sql.append(" AND PM.BUYER LIKE '%" + planner.trim() + "%' ");
        }
        if (null != whId && !"".equals(whId)) {
            sql.append(" AND PM.WH_ID = '" + whId + "' ");
        }
        if (null != partType && !"".equals(partType)) {
            sql.append(" AND TD.PART_TYPE = '" + partType + "' ");
        }
        if (null != state && !"".equals(state)) {
            sql.append(" AND PM.STATE = '" + state + "' ");
        }
        if (null != partOldcode && !"".equals(partOldcode)) {
            sql.append(" AND UPPER(PD.PART_OLDCODE) LIKE '%" + partOldcode.trim().toUpperCase() + "%' ");
        }
        if (null != partCode && !"".equals(partCode)) {
            sql.append(" AND UPPER(PD.PART_CODE) LIKE '%" + partCode.trim().toUpperCase() + "%' ");
        }
        if (null != partCname && !"".equals(partCname)) {
            sql.append(" AND PD.PART_CNAME LIKE '%" + partCname.trim() + "%' ");
        }
        if (null != checkSDate && !"".equals(checkSDate)) {
            sql.append(" AND TO_CHAR(PM.CREATE_DATE,'yyyy-MM-dd') >= '" + checkSDate + "' ");
        }
        if (null != checkEDate && !"".equals(checkEDate)) {
            sql.append(" AND TO_CHAR(PM.CREATE_DATE,'yyyy-MM-dd') <= '" + checkEDate + "' ");
        }
        if (null != whMan && !"".equals(whMan)) {
            sql.append(" AND SD.WHMAN LIKE '%" + whMan.trim() + "%' ");
        }

        sql.append(" ORDER BY PM.CREATE_DATE DESC, PM.ORDER_CODE DESC ");


        PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null,
                getFunName(), pageSize, curPage);
        return ps;
    }

    /**
     * @param : @param sbString
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-7-31
     * @Title : 返回仓库信息List
     */
    public List<Map<String, Object>> getWareHouses(String sbString) {
        StringBuffer sql = new StringBuffer("");
        sql
                .append("SELECT TM.WH_ID, TM.WH_NAME AS WH_CNAME "
                        + " FROM TT_PART_WAREHOUSE_DEFINE TM "
                        + " WHERE 1 = 1  ");
        sql.append(" AND TM.STATE = '" + enableValue + "' ");
        sql.append(sbString);
        sql.append(" ORDER BY TM.WH_ID ");
        List<Map<String, Object>> list = pageQuery(sql.toString(), null, getFunName());
        return list;
    }

}
