package com.infodms.dms.dao.parts.purchaseOrderManager;

import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;
import org.apache.log4j.Logger;

import java.sql.ResultSet;
import java.util.Map;

public class PartChargeWeightDao extends BaseDao<PO> {
    public static Logger logger = Logger.getLogger(PartChargeWeightDao.class);
    private static final PartChargeWeightDao dao = new PartChargeWeightDao();

    private PartChargeWeightDao() {
    }

    public static final PartChargeWeightDao getInstance() {
        return dao;
    }

    protected PO wrapperPO(ResultSet rs, int idx) {
        return null;
    }

    /**
     * 配件计费重量明细表
     *
     * @param request
     * @param curPage
     * @param pageSize
     * @return
     */
    public PageResult<Map<String, Object>> queryDatas(RequestWrapper request, int curPage, int pageSize) {

        String dealerCode = CommonUtils.checkNull(request.getParamValue("DEALER_CODE"));//经销商编码
        String dealerName = CommonUtils.checkNull(request.getParamValue("DEALER_NAME"));//经销商名称
        String transType = CommonUtils.checkNull(request.getParamValue("transType"));//发运方式
        String transportOrg = CommonUtils.checkNull(request.getParamValue("transportOrg"));//承运物流
        String fstartDate = CommonUtils.checkNull(request.getParamValue("fstartDate"));//发运开始
        String fsendDate = CommonUtils.checkNull(request.getParamValue("fsendDate"));//发运结束
        String trplanCode = CommonUtils.checkNull(request.getParamValue("TRPLAN_CODE"));//发运单号

        StringBuffer sql = new StringBuffer();

        sql.append("WITH ADDR_PROV_CITY AS \n");
        sql.append("(SELECT AD.ADDR_ID, TR.REGION_NAME PROVINCE, TR2.REGION_NAME CITY \n");
        sql.append("FROM TT_PART_ADDR_DEFINE AD, TM_REGION TR, TM_REGION TR2 \n");
        sql.append("WHERE AD.PROVINCE_ID = TR.REGION_CODE(+) \n");
        sql.append("AND AD.CITY_ID = TR2.REGION_CODE(+)) \n");
        sql.append("SELECT DISTINCT AD.PROVINCE, \n");// --省份
        sql.append("AD.CITY, \n");// --城市
        sql.append("TP.DEALER_CODE, \n");// --经销商编码
        sql.append("TP.DEALER_NAME, \n");// --经销商名称
        sql.append("TP.TRPLAN_CODE, \n");// --发运单号
        sql.append("TP.TRANSPORT_ORG, \n");// --承运物流
        sql.append("TP.TRANS_TYPE, \n");// --发运方式
        sql.append("BD.PKG_NO, \n");// --箱号
        sql.append("BD.LENGTH, \n");// --长
        sql.append("BD.WIDTH, \n");// --宽
        sql.append("BD.HEIGHT, \n");// --高
        sql.append("BD.VOLUME, \n");// --体积
        sql.append("BD.WEIGHT, \n");// --实际重量
        sql.append("BD.EQ_WEIGHT, \n");// --折合重量
        sql.append("BD.CH_WEIGHT, \n");// --计费重量
        sql.append("tp.create_date, \n");//--发运日期
        sql.append("'' REMARK \n");// -- 备注
        sql.append("FROM TT_PART_TRANS_PLAN_DTL PD, \n");
        sql.append("TT_PART_TRANS_PLAN     TP, \n");
        sql.append("ADDR_PROV_CITY         AD, \n");
        sql.append("TT_PART_PKG_BOX_DTL    BD \n");
        sql.append("WHERE PD.TRPLAN_ID = TP.TRPLAN_ID \n");
        sql.append("AND TP.ADDR_ID = AD.ADDR_ID(+) \n");
        sql.append("AND PD.PICK_ORDER_ID = BD.PICK_ORDER_ID \n");
        sql.append("AND PD.PKG_NO = BD.PKG_NO \n");

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

        PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
        return ps;
    }

    /*public List<Map<String, Object>> getDatas(RequestWrapper request) {
         String dealerCode = CommonUtils.checkNull(request.getParamValue("DEALER_CODE"));//经销商编码
         String dealerName = CommonUtils.checkNull(request.getParamValue("DEALER_NAME"));//经销商名称
         String transType = CommonUtils.checkNull(request.getParamValue("transType"));//发运方式
         String transportOrg = CommonUtils.checkNull(request.getParamValue("transportOrg"));//承运物流
         String fstartDate = CommonUtils.checkNull(request.getParamValue("fstartDate"));//发运开始
         String fsendDate = CommonUtils.checkNull(request.getParamValue("fsendDate"));//发运结束
         String trplanCode = CommonUtils.checkNull(request.getParamValue("TRPLAN_CODE"));//发运单号

         StringBuffer sql = new StringBuffer();

         sql.append("WITH ADDR_PROV_CITY AS \n");
         sql.append("(SELECT AD.ADDR_ID, TR.REGION_NAME PROVINCE, TR2.REGION_NAME CITY \n");
         sql.append("FROM TT_PART_ADDR_DEFINE AD, TM_REGION TR, TM_REGION TR2 \n");
         sql.append("WHERE AD.PROVINCE_ID = TR.REGION_CODE(+) \n");
         sql.append("AND AD.CITY_ID = TR2.REGION_CODE(+)) \n");
         sql.append("SELECT DISTINCT AD.PROVINCE, \n");// --省份
         sql.append("AD.CITY, \n");// --城市
         sql.append("TP.DEALER_CODE, \n");// --经销商编码
         sql.append("TP.DEALER_NAME, \n");// --经销商名称
         sql.append("TP.TRPLAN_CODE, \n");// --发运单号
         sql.append("TP.TRANSPORT_ORG, \n");// --承运物流
         sql.append("TP.TRANS_TYPE, \n");// --发运方式
         sql.append("BD.PKG_NO, \n");// --箱号
         sql.append("BD.LENGTH, \n");// --长
         sql.append("BD.WIDTH, \n");// --宽
         sql.append("BD.HEIGHT, \n");// --高
         sql.append("BD.VOLUME, \n");// --体积
         sql.append("BD.WEIGHT, \n");// --实际重量
         sql.append("BD.EQ_WEIGHT, \n");// --折合重量
         sql.append("BD.CH_WEIGHT, \n");// --计费重量
         sql.append("tp.create_date, \n");//--发运日期
         sql.append("'' REMARK \n");// -- 备注
         sql.append("FROM TT_PART_TRANS_PLAN_DTL PD, \n");
         sql.append("TT_PART_TRANS_PLAN     TP, \n");
         sql.append("ADDR_PROV_CITY         AD, \n");
         sql.append("TT_PART_PKG_BOX_DTL    BD \n");
         sql.append("WHERE PD.TRPLAN_ID = TP.TRPLAN_ID \n");
         sql.append("AND TP.ADDR_ID = AD.ADDR_ID(+) \n");
         sql.append("AND PD.PICK_ORDER_ID = BD.PICK_ORDER_ID \n");
         sql.append("AND PD.PKG_NO = BD.PKG_NO \n");

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
         if(!"".equals(trplanCode) && trplanCode != null){
         	sql.append("   AND TP.TRPLAN_CODE LIKE '%" + trplanCode + "%'\n");
         }


        List<Map<String, Object>> ps = this.pageQuery(sql.toString(), null, this.getFunName());
        return ps;
    }*/

}
