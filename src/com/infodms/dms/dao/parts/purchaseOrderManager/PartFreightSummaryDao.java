package com.infodms.dms.dao.parts.purchaseOrderManager;

import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;
import org.apache.log4j.Logger;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

public class PartFreightSummaryDao extends BaseDao<PO> {
    public static Logger logger = Logger.getLogger(PartFreightSummaryDao.class);
    private static final PartFreightSummaryDao dao = new PartFreightSummaryDao();

    private PartFreightSummaryDao() {
    }

    public static final PartFreightSummaryDao getInstance() {
        return dao;
    }

    protected PO wrapperPO(ResultSet rs, int idx) {
        return null;
    }


    /**
     * @param : @param request
     * @param : @param curPage
     * @param : @param pageSize
     * @param : @return
     * @return :
     * @throws :
     * @Title :
     * @Description: 配件待发运查询
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
        String dealerCode = CommonUtils.checkNull(request.getParamValue("DEALER_CODE"));//经销商编码
        String dealerName = CommonUtils.checkNull(request.getParamValue("DEALER_NAME"));//经销商名称
        String transType = CommonUtils.checkNull(request.getParamValue("transType"));//发运方式
        String transportOrg = CommonUtils.checkNull(request.getParamValue("transportOrg"));//承运物流
        String fstartDate = CommonUtils.checkNull(request.getParamValue("fstartDate"));//发运开始
        String fsendDate = CommonUtils.checkNull(request.getParamValue("fsendDate"));//发运结束
        String trplanCode = CommonUtils.checkNull(request.getParamValue("TRPLAN_CODE"));//发运单号

        StringBuffer sql = new StringBuffer();

        sql.append("WITH ADDR_PROV_CITY AS\n");
        sql.append(" (SELECT AD.ADDR_ID,\n");
        sql.append("         TR.REGION_NAME PROVINCE,\n");
        sql.append("         TR2.REGION_NAME CITY,\n");
        sql.append("         CD.LOGI_ID,\n");
        sql.append("         FD.FIX_NAME LOGI_NAME,\n");
        sql.append("         CD.TRANS_TYPE,\n");
        sql.append("         FD2.FIX_NAME TRANS_TYPE_NAME,\n");
        sql.append("         CD.ARRIVE_DAYS,\n");
        sql.append("         NVL(CD.FIRST_WEIGHT, 0) FIRST_WEIGHT,\n");
        sql.append("         NVL(CD.ADDITIONAL_WEIGHT, 0) ADDITIONAL_WEIGHT,\n");
        sql.append("         NVL(CD.MINI_WEIGHT, 1) MINI_WEIGHT\n");
        sql.append("    FROM TT_PART_ADDR_DEFINE    AD,\n");
        sql.append("         TM_REGION              TR,\n");
        sql.append("         TM_REGION              TR2,\n");
        sql.append("         TT_PART_CITY_DIS       CD,\n");
        sql.append("         TT_PART_FIXCODE_DEFINE FD,\n");
        sql.append("         TT_PART_FIXCODE_DEFINE FD2\n");
        sql.append("   WHERE AD.PROVINCE_ID = TR.REGION_CODE(+)\n");
        sql.append("     AND AD.CITY_ID = TR2.REGION_CODE(+)\n");
        sql.append("     AND TR2.REGION_ID = CD.END_PLACE_ID(+)\n");
        sql.append("     AND CD.LOGI_ID = FD.FIX_VALUE(+)\n");
        sql.append("     AND CD.TRANS_TYPE = FD2.FIX_VALUE(+)\n");
        sql.append("     AND FD.FIX_GOUPTYPE = '92251008'\n");
        sql.append("     AND FD2.FIX_GOUPTYPE = '92251006'),\n");
        sql.append("DEALER_PROVICE AS\n");
        sql.append(" (SELECT AD.ADDR_ID, TR.REGION_NAME PROVINCE, TR2.REGION_NAME CITY\n");
        sql.append("    FROM TT_PART_ADDR_DEFINE AD, TM_REGION TR, TM_REGION TR2\n");
        sql.append("   WHERE AD.PROVINCE_ID = TR.REGION_CODE(+)\n");
        sql.append("     AND AD.CITY_ID = TR2.REGION_CODE(+)),\n");
        sql.append("TRPLAN_BOX AS\n");
        sql.append(" (SELECT DISTINCT PBD.TRPLAN_ID,\n");
        sql.append("                  PBD.PKG_NO,\n");
        sql.append("                  PBD.LENGTH,\n");
        sql.append("                  PBD.WIDTH,\n");
        sql.append("                  PBD.HEIGHT,\n");
        sql.append("                  PBD.WEIGHT,\n");
        sql.append("                  PBD.VOLUME,\n");
        sql.append("                  PBD.EQ_WEIGHT,\n");
        sql.append("                  PBD.CH_WEIGHT,\n");
        sql.append("                  PBD.REMARK\n");
        sql.append("    FROM TT_PART_PKG_BOX_DTL PBD)\n");
        sql.append("\n");
        sql.append("SELECT DP.PROVINCE, --省份\n");
        sql.append("       DP.CITY, --城市\n");
        sql.append("       TP.DEALER_CODE, --经销商编码\n");
        sql.append("       TP.DEALER_NAME, --经销商名称\n");
        sql.append("       TP.TRPLAN_CODE, --发运单号\n");
        sql.append("       TP.TRANSPORT_ORG, --承运物流\n");
        sql.append("       TP.TRANS_TYPE, --发运方式\n");
        sql.append("       COUNT(BD.PKG_NO) PKG_NO, --箱数\n");
        sql.append("       SUM(BD.WEIGHT) WEIGHT, --实际重量\n");
        sql.append("       SUM(BD.EQ_WEIGHT) EQ_WEIGHT, --折合重量\n");
        sql.append("       SUM(BD.CH_WEIGHT) CH_WEIGHT, --计费重量\n");
        sql.append("       CASE\n");
        sql.append("         WHEN SUM(BD.CH_WEIGHT) > 0 AND SUM(BD.CH_WEIGHT) <= 1 THEN\n");
        sql.append("          1\n");
        sql.append("         ELSE\n");
        sql.append("          ROUND(SUM(BD.CH_WEIGHT))\n");
        sql.append("       END CH_WEIGHT2, --小数进位重量（KG）\n");
        sql.append("       AD.FIRST_WEIGHT,\n");
        sql.append("       AD.ADDITIONAL_WEIGHT,\n");
        sql.append("       AD.FIRST_WEIGHT + ((CASE\n");
        sql.append("         WHEN SUM(BD.CH_WEIGHT) > 0 AND SUM(BD.CH_WEIGHT) <= 1 THEN\n");
        sql.append("          1\n");
        sql.append("         ELSE\n");
        sql.append("          ROUND(SUM(BD.CH_WEIGHT))\n");
        sql.append("       END) - 1) * ADDITIONAL_WEIGHT WEIGHT_AMOUNT, --运费 = 首重金额+（实际重量-1）*续重\n");
        sql.append("       '' REMARK -- 备注\n");
        sql.append("  FROM /* TT_PART_TRANS_PLAN_DTL PD,*/ TT_PART_TRANS_PLAN TP,\n");
        sql.append("       ADDR_PROV_CITY     AD,\n");
        sql.append("       DEALER_PROVICE     DP,\n");
        sql.append("       TRPLAN_BOX         BD\n");
        sql.append(" WHERE BD.TRPLAN_ID = TP.TRPLAN_ID\n");
        sql.append("   AND TP.ADDR_ID = AD.ADDR_ID(+)\n");
        sql.append("   AND TP.ADDR_ID = DP.ADDR_ID(+)\n");
        sql.append("      /*AND PD.PICK_ORDER_ID = BD.PICK_ORDER_ID\n");
        sql.append("      AND PD.PKG_NO = BD.PKG_NO*/\n");
        sql.append("   AND TP.TRANS_TYPE = AD.TRANS_TYPE_NAME(+)\n");
        sql.append("   AND TP.TRANSPORT_ORG = AD.LOGI_NAME(+)");

        //---以下是查询条件

        if (dealerCode != "") {
            sql.append("   AND TP.DEALER_CODE LIKE '%" + dealerCode + "%'\n");
        }
        if (dealerName != "") {
            sql.append("   AND TP.DEALER_NAME LIKE '%" + dealerName + "%'\n");
        }

        if (!"".equals(transType) && null != transType) {
            sql.append("   AND TP.TRANS_TYPE LIKE '%" + transType + "%'\n");
        }
        if (!"".equals(transportOrg) && null != transportOrg) {
            sql.append("   AND TP.TRANSPORT_ORG LIKE '%" + transportOrg + "%'\n");
        }
        if (!"".equals(fstartDate) && fstartDate != null) {
            sql.append("   AND TRUNC(TP.CREATE_DATE) >= TO_DATE('" + fstartDate + "','YYYY-MM-DD')\n");
        }
        if (!"".equals(fsendDate) && fsendDate != null) {
            sql.append("   AND TRUNC(TP.CREATE_DATE) <= TO_DATE('" + fsendDate + "','YYYY-MM-DD')\n");
        }
        if (!"".equals(trplanCode) && trplanCode != null) {
            sql.append("   AND TP.TRPLAN_CODE LIKE '%" + trplanCode + "%'\n");
        }

        sql.append("  GROUP BY DP.PROVINCE,\n");
        sql.append("         DP.CITY,\n");
        sql.append("         TP.DEALER_CODE,\n");
        sql.append("         TP.DEALER_NAME,\n");
        sql.append("         TP.TRPLAN_CODE,\n");
        sql.append("         TP.TRANSPORT_ORG,\n");
        sql.append("         TP.TRANS_TYPE,\n");
        sql.append("         AD.FIRST_WEIGHT,\n");
        sql.append("         AD.ADDITIONAL_WEIGHT\n");
        sql.append("  ORDER BY TP.TRPLAN_CODE DESC");

        return sql;
    }

}
