package com.infodms.dms.dao.parts.salesManager;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.bean.OrgBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.dao.parts.baseManager.partsBaseManager.PartWareHouseDao;
import com.infodms.dms.po.TtPartPkgBoxDtlPO;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;
import org.apache.log4j.Logger;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PartPickOrderDao extends BaseDao {
    public static Logger logger = Logger.getLogger(PartPickOrderDao.class);
    private static final PartPickOrderDao dao = new PartPickOrderDao();

    private PartPickOrderDao() {
    }

    public static final PartPickOrderDao getInstance() {
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
     * @throws : LastDate    : 2013-4-16
     * @Title :
     * @Description: 查询
     */
    public PageResult<Map<String, Object>> queryPickOrder(RequestWrapper request, int curPage, int pageSize) {
        StringBuffer sql = new StringBuffer();
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        String dealerId = "";
        //判断是否为车厂  PartWareHouseDao
        PartWareHouseDao dao = PartWareHouseDao.getInstance();
        List<OrgBean> beanList = dao.getOrgInfo(loginUser);
        if (null != beanList || beanList.size() >= 0) {
            if ((beanList.get(0).getOrgId() + "").equals(Constant.OEM_ACTIVITIES)) {
                dealerId = Constant.OEM_ACTIVITIES;
            } else {
                dealerId = loginUser.getDealerId();
            }
        }
        String pickOrderId = CommonUtils.checkNull(request.getParamValue("pickOrderId"));//单号
        String dealerName = CommonUtils.checkNull(request.getParamValue("dealerName"));//单号
        String dealerCode = CommonUtils.checkNull(request.getParamValue("dealerCode"));//单号
        String whId = CommonUtils.checkNull(request.getParamValue("whId"));//单号
        String startDate = CommonUtils.checkNull(request.getParamValue("SstartDate"));//开始
        String endDate = CommonUtils.checkNull(request.getParamValue("SendDate"));//结束
        String printFlag = CommonUtils.checkNull(request.getParamValue("printFlag"));//打印标识
        String transFlag = CommonUtils.checkNull(request.getParamValue("TransFlag"));//装箱单打印标识
        String transType = CommonUtils.checkNull(request.getParamValue("TRANS_TYPE"));//发运方式
        String isPkg = CommonUtils.checkNull(request.getParamValue("IsPkg"));//是否已装箱
        String soCode = CommonUtils.checkNull(request.getParamValue("soCode"));//销售单编码
        String orderType = CommonUtils.checkNull(request.getParamValue("ORDER_TYPE"));//是否已装箱
        String startDate1 = CommonUtils.checkNull(request.getParamValue("SstartDate1"));//开始
        String endDate1 = CommonUtils.checkNull(request.getParamValue("SendDate1"));//结束
        String orderCode = CommonUtils.checkNull(request.getParamValue("orderCode"));//订单号

        sql.append("SELECT SM.PICK_ORDER_ID,\n");
        sql.append("       SM.DEALER_ID,\n");
        sql.append("       SM.DEALER_CODE,\n");
        sql.append("       SM.DEALER_NAME,\n");
        sql.append("       SM.WH_ID,\n");
        sql.append("       SM.ORDER_TYPE,\n");
        sql.append("       SM.TRANS_TYPE,\n");
        sql.append("       SM.PICK_ORDER_CREATE_DATE,\n");
        sql.append("       SM.PICK_ORDER_CREATE_BY,\n");
        sql.append("       SM.PICK_ORDER_PRINT_DATE,\n");
        sql.append("       SM.PICK_ORDER_PRINT_NUM,\n");//20170817 add 拣货单打印次数
        if(!isPkg.equals("") && isPkg!=null){
            sql.append("       SM.PKG_PRINT_DATE,\n");//20170817 add 装箱单打印日期
            sql.append("       SM.PKG_PRINT_NUM,\n");//20170817 add 装箱单打印次数
        }
        sql.append("       SM.STATE,\n");
        sql.append("       (SELECT WD.WH_NAME\n");
        sql.append("          FROM TT_PART_WAREHOUSE_DEFINE WD\n");
        sql.append("         WHERE WD.WH_ID = SM.WH_ID) WH_NAME,\n");
        sql.append("       (SELECT D.FIX_NAME\n");
        sql.append("          FROM TT_PART_FIXCODE_DEFINE D\n");
        sql.append("         WHERE D.FIX_VALUE = SM.TRANS_TYPE\n");
        sql.append("           AND D.FIX_GOUPTYPE = 92251004) TRANS_TYPE_NAME,\n");
        sql.append("       (SELECT U.NAME\n");
        sql.append("          FROM TC_USER U\n");
        sql.append("         WHERE U.USER_ID = SM.PICK_ORDER_CREATE_BY) CREATEBYNAME,\n");
        sql.append("       TO_CHAR(SUM(SM.AMOUNT), 'FM999,999,990.00') TOTALMONEY,\n");
        sql.append("       (SELECT COUNT(1)\n");
        sql.append("          FROM TT_PART_PKG_BOX_DTL T\n");
        sql.append("         WHERE T.PICK_ORDER_ID = SM.PICK_ORDER_ID\n");
        sql.append("              --AND T.PICK_ORDER_ID = '140400013'\n");
        sql.append("           AND T.PRINT_NUM > 0) PRINTPKG,\n");
        sql.append("       (SELECT COUNT(1)\n");
        sql.append("          FROM TT_PART_PKG_BOX_DTL T\n");
        sql.append("         WHERE T.PICK_ORDER_ID = SM.PICK_ORDER_ID\n");
        sql.append("              --AND T.PICK_ORDER_ID = '140400013'\n");
        sql.append("           AND T.PRINT_NUM = 0) NOPRINTPKG,\n");
        sql.append("       (SELECT COUNT(1)\n");
        sql.append("          FROM TT_PART_PKG_BOX_DTL T\n");
        sql.append("         WHERE T.PICK_ORDER_ID = SM.PICK_ORDER_ID) TOTOALPKG,\n");
        sql.append("       ZA_CONCAT(REMARK2) REMARK,\n");
        sql.append("       DECODE(SIGN((SELECT COUNT(1)\n");
        sql.append("                     FROM TT_PART_PKG_DTL PD\n");
        sql.append("                    WHERE PD.PICK_ORDER_ID = SM.PICK_ORDER_ID\n");
        sql.append("                      AND PD.PKG_NO IS NULL)),\n");
        sql.append("              1,\n");
        sql.append("              'Y',\n");
        sql.append("              'N') XC_FLAG,\n");
        sql.append("       SIGN((SELECT COUNT(1)\n");
        sql.append("              FROM TT_PART_PKG_DTL PD\n");
        sql.append("             WHERE PD.PICK_ORDER_ID = SM.PICK_ORDER_ID)) FLAG,\n");
        sql.append("       ZA_CONCAT(SM.ORDER_CODE) ORDER_CODE\n");
        //sql.append("\t\t\t  (SELECT DECODE(COUNT(1), 1, '有', '无')\n");
        //sql.append("          FROM VW_PART_VS_DLR_ORDER VDO\n");
        //sql.append("         WHERE VDO.DEALER_ID = sM.DEALER_ID) VSO_FLAG --是否有提车单\n");
        sql.append("  FROM TT_PART_SO_MAIN SM, TM_DEALER TD\n");
        sql.append(" WHERE SM.DEALER_ID = TD.DEALER_ID\n");
        sql.append(" and seller_id='").append(CommonUtils.checkNull(dealerId)).append("'\n");
        if (!"".equals(pickOrderId)) {
            sql.append(" and PICK_ORDER_ID like '%").append(pickOrderId).append("%'\n");
        }
        if (!"".equals(dealerName)) {
            sql.append(" and SM.DEALER_NAME LIKE  '%").append(dealerName).append("%'\n");
        }
        if (!"".equals(dealerCode)) {
            sql.append(" and upper(td.dealer_code) like  upper('%").append(dealerCode).append("%')\n");
        }
        if (!"".equals(whId)) {
            sql.append(" and wh_id='").append(whId).append("'\n");
        }
        if (!"".equals(transType)) {
            sql.append(" and TRANS_TYPE='").append(transType).append("'\n");
        }
        if (!"".equals(startDate) && !"".equals(startDate)) {
            sql.append(" and PICK_ORDER_CREATE_DATE>= to_date('").append(startDate).append(" 00:00:00','YYYY/MM/dd HH24:mi:ss')\n");
        }
        if (!"".equals(endDate) && !"".equals(endDate)) {
            sql.append(" and PICK_ORDER_CREATE_DATE<= to_date('").append(endDate).append(" 23:59:59','YYYY/MM/dd HH24:mi:ss')\n");
        }
        if (printFlag.equals(Constant.PART_BASE_FLAG_YES + "")) {
            sql.append(" and  PICK_ORDER_PRINT_NUM>0 \n");
        } else if (printFlag.equals(Constant.PART_BASE_FLAG_NO + "")) {
            sql.append(" and  PICK_ORDER_PRINT_NUM=0 \n");
        }
        if (transFlag.equals(Constant.PART_BASE_FLAG_YES + "")) {
            sql.append("--已打印完= 已打印箱子数=总箱子数且已装箱\n");
            sql.append("AND ((SELECT COUNT(1)\n");
            sql.append("        FROM TT_PART_PKG_BOX_DTL T\n");
            sql.append("       WHERE T.PICK_ORDER_ID = SM.PICK_ORDER_ID)) =\n");
            sql.append("    (SELECT COUNT(1)\n");
            sql.append("       FROM TT_PART_PKG_BOX_DTL T\n");
            sql.append("      WHERE T.PICK_ORDER_ID = SM.PICK_ORDER_ID\n");
            sql.append("           --AND T.PICK_ORDER_ID = '140400013'\n");
            sql.append("        AND T.PRINT_NUM > 0)\n");
            sql.append("AND (SELECT COUNT(1)\n");
            sql.append("       FROM TT_PART_PKG_BOX_DTL T\n");
            sql.append("      WHERE T.PICK_ORDER_ID = SM.PICK_ORDER_ID\n");
            sql.append("           --AND T.PICK_ORDER_ID = '140400013'\n");
            sql.append("        AND T.PRINT_NUM > 0) > 0\n");
            sql.append("AND ((SELECT COUNT(1)\n");
            sql.append("        FROM TT_PART_PKG_BOX_DTL T\n");
            sql.append("       WHERE T.PICK_ORDER_ID = SM.PICK_ORDER_ID)) > 0");
        } else if (transFlag.equals(Constant.PART_BASE_FLAG_NO + "")) {
            sql.append("--未打印完= 未打印箱子数>0且已装箱\n");
            sql.append("AND (SELECT COUNT(1)\n");
            sql.append("       FROM TT_PART_PKG_BOX_DTL T\n");
            sql.append("      WHERE T.PICK_ORDER_ID = SM.PICK_ORDER_ID\n");
            sql.append("           --AND T.PICK_ORDER_ID = '140400013'\n");
            sql.append("        AND T.PRINT_NUM = 0) > 0\n");
            sql.append("AND ((SELECT COUNT(1)\n");
            sql.append("        FROM TT_PART_PKG_BOX_DTL T\n");
            sql.append("       WHERE T.PICK_ORDER_ID = SM.PICK_ORDER_ID)) > 0");
        }
        if (isPkg.equals(Constant.PART_BASE_FLAG_YES + "")) {
            sql.append(" and  STATE in( ").append(Constant.CAR_FACTORY_PKG_STATE_02 + "," + Constant.CAR_FACTORY_PKG_STATE_03 + "," + Constant.CAR_FACTORY_TRANS_STATE_01 +
                    "，" + Constant.CAR_FACTORY_TRANS_STATE_02 + "," + Constant.CAR_FACTORY_PKG_STATE_03 + ")\n");//已装箱、已出库、已发运、已接收、已出库
        } else if (isPkg.equals(Constant.PART_BASE_FLAG_NO + "")) {
            sql.append(" and  STATE in ( ").append(Constant.CAR_FACTORY_ORDER_CHECK_STATE_05 + "," + Constant.CAR_FACTORY_PKG_STATE_01 + ")\n");//财务审核通过、装箱中
        }
        sql.append("and State <>" + Constant.CAR_FACTORY_SALE_ORDER_STATE_03 + "\n");
        if (soCode != null && !"".equals(soCode)) {
            sql.append("AND SM.SO_CODE LIKE upper('%").append(soCode).append("%')\n");
        }
        if (orderType != null && !"".equals(orderType)) {
            sql.append("AND SM.ORDER_TYPE =").append(orderType).append("\n");
        }
        if (!"".equals(startDate1) && !"".equals(startDate1)) {
            sql.append(" and PICK_ORDER_PRINT_DATE>= to_date('").append(startDate1).append(" 00:00:00','YYYY/MM/dd HH24:mi:ss')\n");
        }
        if (!"".equals(endDate1) && !"".equals(endDate1)) {
            sql.append(" and PICK_ORDER_PRINT_DATE<= to_date('").append(endDate1).append(" 23:59:59','YYYY/MM/dd HH24:mi:ss')\n");
        }
        if (!"".equals(orderCode)) {
            sql.append(" and SM.ORDER_CODE LIKE  '%").append(orderCode).append("%'\n");
        }
        //直发、广宣品订单不在DMS出库，所以不需要拣货出库 add by yuan 140610
        sql.append(" and SM.ORDER_TYPE NOT IN (").append(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_04)
//        	.append(",")
//    		.append(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_12).append(",")
//          .append(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_07)
            .append(")");

        sql.append("GROUP BY SM.PICK_ORDER_ID,\n");
        sql.append("         SM.DEALER_CODE,\n");
        sql.append("         SM.DEALER_ID,\n");
        sql.append("         SM.DEALER_NAME,\n");
        sql.append("         SM.WH_ID,\n");
        sql.append("         SM.TRANS_TYPE,\n");
        sql.append("         SM.PICK_ORDER_CREATE_DATE,\n");
        sql.append("         SM.PICK_ORDER_CREATE_BY,\n");
        sql.append("         SM.STATE,\n");
        sql.append("         SM.PICK_ORDER_PRINT_NUM,\n");
        sql.append("         SM.PICK_ORDER_PRINT_DATE,\n");
        if(!isPkg.equals("") && isPkg!=null){
            sql.append("       SM.PKG_PRINT_DATE,\n");//20170817 add 装箱单打印日期
            sql.append("       SM.PKG_PRINT_NUM,\n");//20170817 add 装箱单打印次数
        }
        sql.append(" 		 SM.ORDER_TYPE\n");
//        sql.append("ORDER BY PICK_ORDER_CREATE_DATE DESC\n");
        sql.append("ORDER BY PICK_ORDER_PRINT_DATE DESC\n");
        PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
        return ps;
    }

    public List<Map<String, Object>> getSoMainList(String pickOrderId) {
        StringBuffer sql = new StringBuffer();

        sql.append("SELECT A.*,\n");
        sql.append("       TO_CHAR(A.AMOUNT, 'FM999,999,990.00') AS CONVERS_AMOUNT,\n");
        sql.append("       (SELECT NAME FROM TC_USER B WHERE B.USER_ID = A.CREATE_BY) AS CREATE_BY_NAME\n");
        sql.append("  FROM TT_PART_SO_MAIN A\n");
        sql.append(" WHERE 1=1\n");
        if (!"".equals(pickOrderId)) {
            sql.append("  AND A.PICK_ORDER_ID IN (").append(pickOrderId).append(")");
        }
        sql.append(" order by A.create_date asc");
        List<Map<String, Object>> list = this.pageQuery(sql.toString(), null, this.getFunName());
        if (list == null) {
            return new ArrayList<Map<String, Object>>();
        }
        return list;
    }

    public List<Map<String, Object>> getSoDtlList(String soId) {
        StringBuffer sql = new StringBuffer();
        sql.append(" select * from tt_part_so_dtl where so_id='").append(soId).append("'");
        List<Map<String, Object>> list = this.pageQuery(sql.toString(), null, this.getFunName());
        if (list == null) {
            return new ArrayList<Map<String, Object>>();
        }
        return list;
    }

    public String getDealerName(String dealerId) {
        StringBuffer sql = new StringBuffer();
        sql.append(" select dealer_name from tm_dealer where dealer_id='").append(dealerId).append("'");
        List<Map<String, Object>> list = this.pageQuery(sql.toString(), null, this.getFunName());
        if (list == null || list.size() <= 0 || list.get(0) == null || list.get(0).get("DEALER_NAME") == null) {
            return "";
        }
        return CommonUtils.checkNull(list.get(0).get("DEALER_NAME"));
    }

    public String getDealerCode(String dealerId) {
        StringBuffer sql = new StringBuffer();
        sql.append(" select dealer_code from tm_dealer where dealer_id='").append(dealerId).append("'");
        List<Map<String, Object>> list = this.pageQuery(sql.toString(), null, this.getFunName());
        if (list == null || list.size() <= 0 || list.get(0) == null || list.get(0).get("DEALER_CODE") == null) {
            return "";
        }
        return CommonUtils.checkNull(list.get(0).get("DEALER_CODE"));
    }

    public String getUserName(String userId) {
        StringBuffer sql = new StringBuffer();
        sql.append(" select name from tc_user where user_id='").append(userId).append("'");
        List<Map<String, Object>> list = this.pageQuery(sql.toString(), null, this.getFunName());
        if (list == null || list.size() <= 0 || list.get(0) == null || list.get(0).get("NAME") == null) {
            return "";
        }
        return CommonUtils.checkNull(list.get(0).get("NAME"));

    }

    public String getSumMoney(String pickOrderId) {
        StringBuffer sql = new StringBuffer();
        sql.append(" select nvl(sum(AMOUNT),'0') as sumMoney from tt_part_so_main where PICK_ORDER_ID='").append(pickOrderId).append("'");
        List<Map<String, Object>> list = this.pageQuery(sql.toString(), null, this.getFunName());
        if (list == null || list.size() <= 0 || list.get(0) == null || list.get(0).get("SUMMONEY") == null) {
            return "";
        }
        return CommonUtils.checkNull(list.get(0).get("SUMMONEY"));
    }

    public List<Map<String, Object>> getPartGroup(String pickOrderId, String whId, String pkgNos) {
        ActionContext act = ActionContext.getContext();

        StringBuffer sql = new StringBuffer();

        sql.append("SELECT T.PART_ID,\n");
        sql.append("       T.LOC_ID,\n");
        sql.append("       D.LOC_CODE,\n");
        sql.append("       D.LOC_NAME,\n");
        sql.append("       T.PART_CODE,\n");
        sql.append("       T.PART_OLDCODE,\n");
        sql.append("       T.PART_CNAME,\n");
        sql.append("       T.UNIT,\n");
        sql.append("       T.SALES_QTY,\n");
        sql.append("       NVL((SELECT SUM(A.ITEM_QTY)\n");
        sql.append("             FROM VW_PART_STOCK A\n");
        sql.append("            WHERE A.WH_ID = " + whId + "\n");
        sql.append("              AND A.PART_ID = T.PART_ID\n");
        sql.append("              AND A.STATE = 10011001\n");
        sql.append("              AND A.STATUS = 1\n");
        sql.append("              AND A.LOC_ID = T.LOC_ID),\n");
        sql.append("           '0') AS NORMAL_QTY_NOW,\n");
        sql.append("       SUM(T.PKG_QTY) PKGEDQTY\n");
        sql.append("  FROM TT_PART_PKG_DTL T, TT_PART_LOACTION_DEFINE D\n");
        sql.append(" WHERE T.LOC_ID = D.LOC_ID\n");
        sql.append("   AND T.PICK_ORDER_ID IN (140900007, 140900006)\n");
        sql.append("   AND EXISTS\n");
        sql.append(" (SELECT 1\n");
        sql.append("          FROM TT_PART_PKG_BOX_DTL BD\n");
        sql.append("         WHERE BD.PICK_ORDER_ID = T.PICK_ORDER_ID\n");
        sql.append("           AND BD.PKG_NO = T.PKG_NO\n");
        sql.append("           AND BD.BOX_ID IN (" + pkgNos + "))\n");
        sql.append(" GROUP BY T.PART_ID,\n");
        sql.append("          T.LOC_ID,\n");
        sql.append("          D.LOC_CODE,\n");
        sql.append("          D.LOC_NAME,\n");
        sql.append("          T.PART_CODE,\n");
        sql.append("          T.PART_OLDCODE,\n");
        sql.append("          T.PART_CNAME,\n");
        sql.append("          T.UNIT,\n");
        sql.append("          T.SALES_QTY");

      /*  sql.append("    AND T.PKG_NO IN (");
        for (int i = 0; i < pkgNoArr.length; i++) {
            sql.append("'").append(pkgNoArr[i]).append("',");
        }
        sql.deleteCharAt(sql.length() - 1);
        sql.append(")");*/

        List<Map<String, Object>> list = this.pageQuery(sql.toString(), null, this.getFunName());
        return list;
    }

    public List<Map<String, Object>> getPartGroupInfo(String pickOrderId,String whId, String trplanId) {
        ActionContext act = ActionContext.getContext();

        StringBuffer sql = new StringBuffer();

        sql.append(" SELECT PD.PART_ID, \n");
        sql.append("        T.LOC_ID, \n");
        sql.append("        D.LOC_CODE, \n");
        sql.append("        D.LOC_CODE LOC_NAME, \n");
        sql.append("        PD.PART_CODE, \n");
        sql.append("        PD.PART_OLDCODE, \n");
        sql.append("        PD.PART_CNAME, \n");
        sql.append("        PD.UNIT, \n");
        sql.append("        BOOK_DTL.BATCH_NO, \n");
        sql.append("        T.SLINE_ID,T.SO_ID, \n");
//        sql.append("        SUM(T.SALES_QTY) SALES_QTY, \n");
        sql.append("        T.SALES_QTY, \n");//20170818 update
        sql.append("        nvl(PD.BUY_MIN_PKG,0) MIN_PACKAGE, \n");//20170818 add
        sql.append("        NVL((SELECT SUM(A.ITEM_QTY) FROM VW_PART_STOCK A \n");
        sql.append("             WHERE A.WH_ID = ").append(whId).append(" AND A.PART_ID = PD.PART_ID \n");
        sql.append("             and A.BATCH_NO=BOOK_DTL.Batch_No  \n");//20170825 add
        sql.append("             AND A.STATE = 10011001  AND A.STATUS = 1 AND A.LOC_ID = T.LOC_ID), \n");
        sql.append("            '0') AS NORMAL_QTY_NOW, \n");
        sql.append("        SUM(T.PKG_QTY) PKGEDQTY \n");
        sql.append("   FROM TT_PART_PKG_DTL T,TT_PART_LOACTION_DEFINE D, TT_PART_DEFINE PD \n");
        sql.append("   ,(select PICKORDER_ID,BATCH_NO,LOC_ID from TT_PART_BOOK_DTL where PICKORDER_ID="+pickOrderId+" group by PICKORDER_ID,BATCH_NO,LOC_ID) BOOK_DTL \n");
        sql.append("  WHERE T.LOC_ID=D.LOC_ID \n");
        sql.append("    AND D.LOC_ID=BOOK_DTL.LOC_ID AND T.PICK_ORDER_ID = BOOK_DTL.PICKORDER_ID \n");
        sql.append("    AND T.PICK_ORDER_ID = "+pickOrderId);
        sql.append("    AND T.PART_ID = PD.PART_ID \n");
        sql.append("    AND BOOK_DTL.Batch_No=t.batch_no \n");//20170830 add
        sql.append("	AND EXISTS (SELECT 1 FROM TT_PART_PKG_BOX_DTL PD\n");
        sql.append("         WHERE PD.PICK_ORDER_ID = T.PICK_ORDER_ID\n");
        sql.append("           AND PD.PKG_NO = T.PKG_NO AND PD.TRPLAN_ID = " + trplanId + ")");
        sql.append("  GROUP BY PD.PART_ID, \n");
        sql.append("           T.SLINE_ID,T.SO_ID, \n");
        sql.append("           T.LOC_ID, \n");
        sql.append("           D.LOC_CODE, \n");
        sql.append("           D.LOC_NAME, \n");
        sql.append("           PD.PART_CODE, \n");
        sql.append("           PD.PART_OLDCODE, \n");
        sql.append("           PD.PART_CNAME, \n");
        sql.append("           PD.UNIT, \n");
        sql.append("           T.SALES_QTY, \n");//20170818 add
        sql.append("           PD.BUY_MIN_PKG, \n");//20170818 add
        sql.append("           BOOK_DTL.BATCH_NO \n");

        List<Map<String, Object>> list = this.pageQuery(sql.toString(), null, this.getFunName());
        return list;
    }

    public Map<String, Object> getPartInfo(String partId, String soIds) {
        StringBuffer sql = new StringBuffer();
        sql.append(" select * from tt_part_so_dtl where part_id='").append(partId).append("'");
        sql.append(" and so_id in (").append(soIds).append(")");
        List<Map<String, Object>> list = this.pageQuery(sql.toString(), null, this.getFunName());
        if (list == null || list.size() <= 0 || list.get(0) == null) {
            return new HashMap<String, Object>();
        }
        return list.get(0);
    }

    public String getMaxPkgNoByPickOrderId(String pickOrderId) {
        StringBuffer sql = new StringBuffer();
        sql.append(" select nvl(max(to_number(pkg_no)),'0') as maxNo from tt_part_pkg_dtl ");
        sql.append(" where pick_order_id='").append(pickOrderId).append("'");
        List<Map<String, Object>> list = this.pageQuery(sql.toString(), null, this.getFunName());
        if (null == list || list.size() <= 0 || null == list.get(0) || null == list.get(0).get("MAXNO")) {
            return "0";
        }
        return list.get(0).get("MAXNO").toString();
    }

    /**
     * @param boxIds 箱号ID
     * @return
     */
    public String getPkgNOs(String boxIds) {
        StringBuffer sql = new StringBuffer();
        sql.append(" select za_concat(DISTINCT PKG_NO) pkg_no from TT_PART_PKG_BOX_DTL ");
        sql.append("  where box_id in (").append(boxIds).append(")");
        List<Map<String, Object>> list = this.pageQuery(sql.toString(), null, this.getFunName());
        if (null == list || list.size() <= 0) {
            return "";
        }
        return list.get(0).get("PKG_NO") + "";
    }

    public Long getPickOrderSalesQty(String partId, String soIds) {
        StringBuffer sql = new StringBuffer();
        sql.append(" select nvl(sum(sales_qty),'0') as salesQty from tt_part_so_dtl ");
        sql.append(" where 1=1 ");
        sql.append(" and so_Id in (").append(soIds).append(")");
        sql.append(" and part_id='").append(partId).append("'");
        sql.append(" group by part_id ");
        List<Map<String, Object>> list = this.pageQuery(sql.toString(), null, this.getFunName());
        if (list == null || list.size() <= 0 || list.get(0) == null || list.get(0).get("SALESQTY") == null) {
            return 0l;
        }
        return Long.valueOf(list.get(0).get("SALESQTY") + "");
    }

    public List<Map<String, Object>> getSoMain(String partId, String soIds) {
        StringBuffer sql = new StringBuffer();
        sql.append(" select * from tt_part_so_dtl ");
        sql.append(" where 1=1 ");
        sql.append(" and part_id='").append(partId).append("'");
        sql.append(" and so_id in (").append(soIds).append(")");
        return this.pageQuery(sql.toString(), null, this.getFunName());
    }

    public Map<String, Object> getSoOrderDtl(String partId, String soId) {
        StringBuffer sql = new StringBuffer();
        sql.append(" select * from tt_part_so_dtl ");
        sql.append(" where 1=1 ");
        sql.append(" and so_id='").append(soId).append("'");
        sql.append(" and part_id='").append(partId).append("'");
        List<Map<String, Object>> list = this.pageQuery(sql.toString(), null, this.getFunName());
        if (list == null || list.size() <= 0 || list.get(0) == null) {
            return new HashMap<String, Object>();
        }
        return list.get(0);
    }

    public Map<String, Object> getOutMain(String soId) {
        StringBuffer sql = new StringBuffer();
        sql.append(" select * from tt_part_outstock_main ");
        sql.append(" where so_id='").append(soId).append("'");
        List<Map<String, Object>> list = this.pageQuery(sql.toString(), null, this.getFunName());
        if (list == null || list.size() <= 0 || list.get(0) == null) {
            return new HashMap<String, Object>();
        }
        return list.get(0);
    }

    /**
     * @param pickOrderId
     * @return
     */
    public String getPickCount(String pickOrderId) {
        StringBuffer sql = new StringBuffer();
        sql.append(" select nvl(PICK_ORDER_PRINT_NUM,'0') as PICK_ORDER_PRINT_NUM from tt_part_so_main ");
        sql.append(" where 1=1 ");
        sql.append(" and pick_order_id = '").append(pickOrderId).append("'");
        List<Map<String, Object>> list = this.pageQuery(sql.toString(), null, this.getFunName());
        if (list == null || list.size() <= 0 || list.get(0) == null || list.get(0).get("PICK_ORDER_PRINT_NUM") == null) {
            return "0";
        }
        return CommonUtils.checkNull(list.get(0).get("PICK_ORDER_PRINT_NUM"));
    }

    /**
     * @param pickOrderId
     * @return
     */
    public String getTransCount(String pickOrderId) {
        StringBuffer sql = new StringBuffer();
        sql.append(" select nvl(TRANS_PRINT_NUM,'0') as TRANS_PRINT_NUM from tt_part_so_main ");
        sql.append(" where 1=1 ");
        sql.append(" and pick_order_id = '").append(pickOrderId).append("'");
        List<Map<String, Object>> list = this.pageQuery(sql.toString(), null, this.getFunName());
        if (list == null || list.size() <= 0 || list.get(0) == null || list.get(0).get("TRANS_PRINT_NUM") == null) {
            return "0";
        }
        return CommonUtils.checkNull(list.get(0).get("TRANS_PRINT_NUM"));
    }

    /**
     * @param pickOrderId
     * @return
     */
    public String getRemak(String pickOrderId) {
        StringBuffer sql = new StringBuffer();
        sql.append(" select za_concat(remark2) as REMARK from tt_part_so_main ");
        sql.append(" where 1=1 ");
        sql.append(" and pick_order_id = '").append(pickOrderId).append("'");
        List<Map<String, Object>> list = this.pageQuery(sql.toString(), null, this.getFunName());
        if (list == null || list.size() <= 0 || list.get(0) == null || list.get(0).get("REMARK") == null) {
            return "";
        }
        return CommonUtils.checkNull(list.get(0).get("REMARK"));
    }

    public PageResult<Map<String, Object>> queryPkgResult(RequestWrapper request, int curPage, int pageSize) {
        StringBuffer sql = new StringBuffer();
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        String sellerId = "";
        //判断是否为车厂  PartWareHouseDao
        PartWareHouseDao dao = PartWareHouseDao.getInstance();
        List<OrgBean> beanList = dao.getOrgInfo(loginUser);
        if (null != beanList || beanList.size() >= 0) {
            if ((beanList.get(0).getOrgId() + "").equals(Constant.OEM_ACTIVITIES)) {
                sellerId = Constant.OEM_ACTIVITIES;
            } else {
                sellerId = loginUser.getDealerId();
            }
        }
        String startDate = CommonUtils.checkNull(request.getParamValue("CstartDate"));//开始
        String endDate = CommonUtils.checkNull(request.getParamValue("CendDate"));//结束
        String dealerCode = CommonUtils.checkNull(request.getParamValue("dealerCode"));
        String dealerName = CommonUtils.checkNull(request.getParamValue("dealerName"));
        String pickOrderId = CommonUtils.checkNull(request.getParamValue("pickOrderId"));
        /*sql.append(" select ");
        sql.append(" * ");
		sql.append(" from tt_part_pick_order_main ");
		sql.append(" where 1=1 ");
		sql.append(" and seller_id='").append(sellerId).append("'");
		if(!"".equals(startDate)){
			sql.append(" and CREATE_DATE>= to_date('").append(startDate).append(" 00:00:00','YYYY/MM/dd HH24:mi:ss')")		
		}
		if(!"".equals(endDate)){
			sql.append(" and CREATE_DATE<= to_date('").append(endDate).append(" 23:59:59','YYYY/MM/dd HH24:mi:ss')");
		}
		if(!"".equals(dealerCode)){
			sql.append(" and dealer_code like '%").append(dealerCode).append("%'");
		}
		if(!"".equals(dealerName)){
			sql.append(" and dealer_name like '%").append(dealerName).append("%'");
		}
		if(!"".equals(pickOrderId)){
			sql.append(" and pick_Order_Id like '%").append(pickOrderId).append("%'");
		}
		sql.append("  and pick_order_id in ");
		sql.append(" (select pick_order_id from tt_part_so_main where state='").append(Constant.CAR_FACTORY_PKG_STATE_02).append("')");
		sql.append(" order by CREATE_DATE desc");*/


        sql.append("SELECT DISTINCT T.PICK_ORDER_ID,\n");
        sql.append("                T.DEALER_CODE,\n");
        sql.append("                T.DEALER_NAME,\n");
        sql.append("                U.NAME CREATE_BY_NAME,\n");
        sql.append("                T.PKG_BEIGIN CREATE_DATE,\n");
        sql.append("                SIGN((SELECT COUNT(1)\n");
        sql.append("                       FROM TT_PART_PKG_BOX_DTL BD\n");
        sql.append("                      WHERE BD.PICK_ORDER_ID = T.PICK_ORDER_ID\n");
        sql.append("                        AND BD.TRPLAN_ID IS NOT NULL)) FLAG\n");
        sql.append("  FROM TT_PART_SO_MAIN T, TC_USER U\n");
        sql.append(" WHERE T.PICK_ORDER_CREATE_BY = U.USER_ID\n");

//		sql.append(" AND T.STATE = ").append(Constant.CAR_FACTORY_PKG_STATE_01);
        /*sql.append(" AND (T.STATE = ").append(Constant.CAR_FACTORY_PKG_STATE_01);
        sql.append(" OR T.STATE = ").append(Constant.CAR_FACTORY_PKG_STATE_02).append(")");*/
        sql.append(" AND T.SELLER_ID='").append(sellerId).append("'\n");
        if (!"".equals(startDate)) {
            sql.append(" AND T.PACKG_DATE>= TO_DATE('").append(startDate).append(" 00:00:00','YYYY/MM/dd HH24:mi:ss')\n");

        }
        if (!"".equals(endDate)) {
            sql.append(" AND T.PACKG_DATE<= TO_DATE('").append(endDate).append(" 23:59:59','YYYY/MM/dd HH24:mi:ss')\n");
        }
        if (!"".equals(dealerCode)) {
            sql.append(" AND T.DEALER_CODE LIKE '%").append(dealerCode).append("%'\n");
        }
        if (!"".equals(dealerName)) {
            sql.append(" AND T.DEALER_NAME LIKE '%").append(dealerName).append("%'\n");
        }
        if (!"".equals(pickOrderId)) {
            sql.append(" AND T.PICK_ORDER_ID LIKE '%").append(pickOrderId).append("%'\n");
        }
        //有装箱明细且有未出库的箱子

        sql.append("AND EXISTS (SELECT 1\n");
        sql.append("          FROM TT_PART_PKG_DTL P\n");
        sql.append("         WHERE P.PICK_ORDER_ID = T.PICK_ORDER_ID)\n");

       /* sql.append("AND EXISTS (SELECT 1\n");
        sql.append("       FROM TT_PART_PKG_BOX_DTL P\n");
        sql.append("      WHERE P.PICK_ORDER_ID = T.PICK_ORDER_ID\n");
        sql.append("        AND P.OUT_ID IS NULL)\n");*/

        sql.append(" ORDER BY  T.PKG_BEIGIN DESC");
        PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
        return ps;
    }

    public List<Map<String, Object>> getOrderCount(String pickOrderId) {
        StringBuffer sql = new StringBuffer();
        sql.append(" select  * from tt_part_so_main where pick_order_id='").append(pickOrderId).append("' and state<>'").append(Constant.CAR_FACTORY_PKG_STATE_02).append("'");
        return this.pageQuery(sql.toString(), null, this.getFunName());
    }

    public void rollbackSoMainState(String pickOrderId) {
        StringBuffer sql = new StringBuffer();
        sql.append(" update tt_part_so_main set state='").append(Constant.CAR_FACTORY_PKG_STATE_01).append("'");
        sql.append(" where pick_order_id='").append(pickOrderId).append("'");
        this.update(sql.toString(), null);
    }

    public void updateSoMainState(String pickOrderId) {
        TtPartPkgBoxDtlPO boxDtlPO = new TtPartPkgBoxDtlPO();
        boxDtlPO.setPickOrderId(pickOrderId);
        List list = select(boxDtlPO);
        Integer state = Constant.CAR_FACTORY_ORDER_CHECK_STATE_05;
        if (list != null && list.size() > 0) {
            state = Constant.CAR_FACTORY_PKG_STATE_01;
        }
        StringBuffer sql = new StringBuffer();
        sql.append(" update tt_part_so_main set state='").append(state).append("'");
        sql.append(" where pick_order_id='").append(pickOrderId).append("'");
        this.update(sql.toString(), null);
    }

    public void cleanPkgDtl(String pickOrderId) {
        StringBuffer sql = new StringBuffer();
        sql.append(" delete from tt_part_pkg_dtl where pick_order_id='").append(pickOrderId).append("'");
        this.update(sql.toString(), null);
    }

    public void cleanBoDtl(String pickOrderId) {
        StringBuffer sql = new StringBuffer();
        sql.append(" delete from tt_part_pkg_dtl where pick_order_id='").append(pickOrderId).append("'");
        sql.append(" and pkg_no is null");
        this.update(sql.toString(), null);
    }

    public List<Map<String, Object>> getPkgDetail(String pickOrderId) {
        StringBuffer sql = new StringBuffer();
        sql.append(" select ");
        sql.append(" t1.part_id, ");
        sql.append(" t2.part_cname, ");
        sql.append(" t2.part_oldcode, ");
        sql.append(" t2.part_code, ");
        sql.append(" sum(t1.pkg_qty) as pkg_qty, ");
        sql.append(" t1.sales_qty, ");
        sql.append(" za_concat(distinct t1.pkg_no) pkg_no ");
        sql.append(" from tt_part_pkg_dtl t1,tt_part_define t2  ");
        sql.append(" where t1.part_id=t2.part_id ");
        sql.append(" and t1.pick_order_id='").append(pickOrderId).append("'");
        sql.append(" group by t1.part_id, t2.part_cname, t2.part_oldcode, t2.part_code, t1.sales_qty ");
        List<Map<String, Object>> list = this.pageQuery(sql.toString(), null, this.getFunName());
        return list;
    }

    public List<Map<String, Object>> getPkgDetail(String pkg_no, String pickOrderId) {
        StringBuffer sql = new StringBuffer();
        sql.append(" select ");
        sql.append(" t1.pkg_id, ");
        sql.append(" t1.loc_id, ");
        sql.append(" t2.LOC_CODE LOC_NAME, ");
        sql.append(" t1.BATCH_NO, ");//20170901 add
        sql.append(" t1.pkg_no, ");
        sql.append(" t1.part_id, ");
        sql.append(" t1.part_oldcode, ");
        sql.append(" t1.part_code, ");
        sql.append(" t1.part_cname, ");
        sql.append(" t1.sales_qty, ");
        sql.append(" t1.unit, ");
        sql.append(" t1.remark, ");
        sql.append(" t1.pkg_qty ");
        sql.append(" from tt_part_pkg_dtl t1,tt_part_loaction_define t2");
        sql.append(" where t1.loc_id = t2.loc_id ");
        sql.append(" and pkg_no='").append(pkg_no).append("'");
        sql.append(" and pick_order_id='").append(pickOrderId).append("'");
        sql.append(" order by part_oldcode ");
        return this.pageQuery(sql.toString(), null, this.getFunName());
    }

    public PageResult<Map<String, Object>> getPkgInfo(RequestWrapper request,
                                                      Integer curPage, Integer pageSize) throws Exception {
        try {

            String pickOrderId = CommonUtils.checkNull(request.getParamValue("pickOrderId1"));
            String partOldCode = CommonUtils.checkNull(request.getParamValue("PART_OLDCODE"));
            String partCname = CommonUtils.checkNull(request.getParamValue("PART_CNAME"));
            String pkgNo = CommonUtils.checkNull(request.getParamValue("PKG_NO"));

            StringBuffer sql = new StringBuffer();

            sql.append("SELECT DISTINCT T.PKG_NO,\n");
            sql.append("                T.BOX_ID,\n");
            sql.append("                (T.LENGTH || '*' || T.WIDTH || '*' || T.HEIGHT) BZCC,\n");
            sql.append("                T.VOLUME,\n");
            sql.append("                T.EQ_WEIGHT,\n");
            sql.append("                T.CH_WEIGHT,\n");
            sql.append("                T.WEIGHT,\n");
            sql.append("                CASE\n");
            sql.append("                  WHEN T.TRPLAN_ID IS NULL AND PP.PRINT_DATE IS NULL THEN\n");
            sql.append("                   0\n");
            sql.append("                  WHEN T.TRPLAN_ID IS NOT NULL AND PP.PRINT_DATE IS NULL THEN\n");
            sql.append("                   1\n");
            sql.append("                  WHEN T.TRPLAN_ID IS NOT NULL AND PP.PRINT_DATE IS NOT NULL THEN\n");
            sql.append("                   2\n");
            sql.append("                END FLAG,\n");
            sql.append("                SIGN(NVL(T.TRPLAN_ID, 0)) FLAG1\n");
            sql.append("  FROM TT_PART_PKG_BOX_DTL T,\n");
            sql.append("       TT_PART_PKG_DTL T1,\n");
            sql.append("       (SELECT D.PICK_ORDER_ID, D.PKG_NO, P.PRINT_DATE\n");
            sql.append("          FROM TT_PART_TRANS_PLAN_DTL D, TT_PART_TRANS_PLAN P\n");
            sql.append("         WHERE D.TRPLAN_ID = P.TRPLAN_ID) PP\n");
            sql.append(" WHERE T.PICK_ORDER_ID = T1.PICK_ORDER_ID\n");
            sql.append("   AND T.PKG_NO = T1.PKG_NO\n");
            sql.append("   AND T.PICK_ORDER_ID = PP.PICK_ORDER_ID(+)\n");
            sql.append("   AND T.PKG_NO = PP.PKG_NO(+)");
            sql.append("   AND T.PICK_ORDER_ID = '").append(pickOrderId).append("'");
            //未出库的箱子
//            sql.append("AND T.OUT_ID IS NULL\n");

            if (!"".equals(partOldCode)) {
                sql.append("    AND UPPER(T1.PART_OLDCODE) LIKE '%").append(partOldCode.toUpperCase()).append("%'");
            }
            if (!"".equals(partCname)) {
                sql.append("    AND T1.PART_CNAME LIKE '%").append(partCname).append("%'");
            }
            if (!"".equals(pkgNo)) {
                sql.append(" AND T.PKG_NO LIKE '%").append(pkgNo).append("%'");
            }
            sql.append(" ORDER BY T.PKG_NO");
            return pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
        } catch (Exception e) {
            throw e;
        }
    }

    public Map<String, Object> getPkg(String pickOrderId, String pkgNo) throws Exception {
        try {

            StringBuffer sql = new StringBuffer();
            sql.append(" SELECT T.PKG_NO, \n");
            sql.append("        T.LENGTH, \n");
            sql.append("        T.WIDTH, \n");
            sql.append("        T.WEIGHT, \n");
            sql.append("        T.HEIGHT, \n");
            sql.append("        T.VOLUME, \n");
            sql.append("        T.EQ_WEIGHT, \n");
            sql.append("        T.CH_WEIGHT \n");
            sql.append("   FROM TT_PART_PKG_BOX_DTL T \n");
            sql.append("  WHERE T.PKG_NO = '").append(pkgNo).append("'");
            sql.append("    AND T.PICK_ORDER_ID = '").append(pickOrderId).append("'");

            return pageQueryMap(sql.toString(), null, getFunName());
        } catch (Exception e) {
            throw e;
        }
    }


    public void updatePkgDtl(String pickOrderId, String pkgNo, String pkgNo1) throws Exception {
        StringBuffer sql = new StringBuffer();
        try {

            sql.append(" UPDATE TT_PART_PKG_DTL T \n");
            sql.append("    SET T.PKG_NO = '").append(pkgNo).append("'");
            sql.append("  WHERE T.PICK_ORDER_ID = '").append(pickOrderId).append("'");
            sql.append("    AND T.PKG_NO = '").append(pkgNo1).append("'");

            update(sql.toString(), null);
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * 查询已经处理的装箱数量（包括装箱数量+预产生现场BO数量）
     *
     * @param pickOrderId
     * @param partId
     * @param locId
     * @return
     * @throws Exception
     */
    public Map<String, Object> queryAllPkgedQty(String pickOrderId,
                                                String partId, String locId,String batchNo) throws Exception {
        StringBuffer sql = new StringBuffer();
        try {

            sql.append("SELECT NVL(SUM(T.PKG_QTY), 0) ALL_PKG_QTY\n");
            sql.append("  FROM TT_PART_PKG_DTL T\n");
            sql.append(" WHERE T.PICK_ORDER_ID = '" + pickOrderId + "'\n");
            sql.append("   AND T.PART_ID = " + partId + "\n");
            sql.append("   AND T.LOC_ID = " + locId + "\n");
            sql.append("   AND T.BATCH_NO = " + batchNo + "\n");
            sql.append("   AND T.PKG_NO IS NOT NULL");

            return pageQueryMap(sql.toString(), null, getFunName());
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * 箱子在未出库之前允许修改装箱明细和包装信息
     *
     * @param pickOrderId
     * @return
     * @throws Exception
     */
    public boolean isUpdateable(String pickOrderId, String pkgNo) throws Exception {
        List<Map<String, Object>> list;
        StringBuffer sql = new StringBuffer();
        try {

            sql.append("SELECT 1\n");
            sql.append("  FROM TT_PART_PKG_BOX_DTL BD\n");
            sql.append(" WHERE BD.PICK_ORDER_ID = '" + pickOrderId + "'\n");
            sql.append("   AND BD.PKG_NO = '" + pkgNo + "'\n");
            sql.append("   AND BD.OUT_ID IS  NULL\n");
            sql.append("   AND BD.STATUS = 1");

            list = pageQuery(sql.toString(), null, getFunName());
            if (list.size() > 0) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            throw e;
        }

    }
    public PageResult<Map<String, Object>> queryUnPkgedPartInfo(
            RequestWrapper request, AclUserBean loginUser, Integer curPage, Integer pageSize) throws Exception {
        try {
            String pickOrderId = CommonUtils.checkNull(request.getParamValue("pickOrderId"));
            String partOldCode = CommonUtils.checkNull(request.getParamValue("PART_OLDCODE"));
            String partCname = CommonUtils.checkNull(request.getParamValue("PART_CNAME"));
            String partCode = CommonUtils.checkNull(request.getParamValue("PART_CODE"));
            String whId = CommonUtils.checkNull(request.getParamValue("whId"));
            String orgId = "";
            if (null == loginUser.getDealerId()) {
                orgId = loginUser.getOrgId() + "";
            } else {
                orgId = loginUser.getDealerId();
            }

            StringBuffer sql = new StringBuffer();

            sql.append("SELECT DT.*\n");
            sql.append("  FROM (SELECT A.PART_ID,\n");
            sql.append("               D.PART_CODE,\n");
            sql.append("               D.PART_OLDCODE,\n");
            sql.append("               D.PART_CNAME,\n");
            sql.append("               D.UNIT,\n");
            sql.append("               L.LOC_ID,\n");
            sql.append("               L.LOC_CODE,\n");
            sql.append("               L.LOC_CODE LOC_NAME,\n");
            sql.append("               BD.BATCH_NO, \n");//20170828 add
//            sql.append("               L.MIN_PKG,\n");//最小包装量
            sql.append("               D.MIN_PACK2 MIN_PKG,\n");//最小包装量
            sql.append("               NVL(SUM(BD.BOOKED_QTY), '0') AS SALES_QTY,\n");
            sql.append("               NVL((SELECT SUM(PKG_QTY)\n");
            sql.append("                     FROM TT_PART_PKG_DTL TPPD\n");
            sql.append("                    WHERE TPPD.PICK_ORDER_ID = '" + pickOrderId + "'\n");
            sql.append("                      AND TPPD.PART_ID = A.PART_ID\n");
            sql.append("                      AND TPPD.LOC_ID = L.LOC_ID\n");
            sql.append("                      AND bd.batch_no=tppd.BATCH_NO\n");//20170825 add
            sql.append("                      AND TPPD.PKG_NO IS NOT NULL),\n");
            sql.append("                   '0') AS PKGEDQTY,\n");
            //20170904 update start
//            sql.append("                NVL((SELECT SUM(T.ITEM_QTY) \n");
//            sql.append("                      FROM VW_PART_STOCK T \n");
//            sql.append("                     WHERE T.WH_ID = '").append(whId).append("' \n");
//            sql.append("                       AND T.PART_ID = A.PART_ID \n");
//            sql.append("                       and t.BATCH_NO=bd.batch_no \n");//20170825 add
//            sql.append("                       AND T.LOC_ID = L.LOC_ID), \n");
//            sql.append("                    '0') AS NORMAL_QTY_NOW \n");
            //以上注释改为一下注释
           sql.append("                NVL((SELECT SUM(T.ITEM_QTY) \n");
	       sql.append("                      FROM TT_PART_ITEM_STOCK T \n");
	       sql.append("                     WHERE T.WH_ID = '").append(whId).append("' \n");
	       sql.append("                       AND T.PART_ID = A.PART_ID \n");
	       sql.append("                       and t.ORG_ID = ").append(orgId);
	       sql.append("                       and t.batch_code=bd.batch_no \n");
	       sql.append("                       AND T.LOC_ID = L.LOC_ID \n");
	       sql.append("                       AND T.STATUS = 1 \n");
	       sql.append("                       AND T.STATE IN (1, 2, 4) ),  \n");
           sql.append("                    '0') AS NORMAL_QTY_NOW \n");
            //20170904 update end
            
            sql.append("           FROM TT_PART_SO_DTL          A, \n");
            sql.append("                TT_PART_SO_MAIN         SM, \n");
            sql.append("                TT_PART_LOACTION_DEFINE L, \n");
            sql.append("                TT_PART_DEFINE          D, \n");
            sql.append("                TT_PART_BOOK_DTL        BD --配件多货位占用明细表 \n");
            sql.append("          WHERE A.SO_ID = SM.SO_ID \n");
            sql.append("            AND A.SO_ID = BD.ORDER_ID \n");
//            sql.append("            AND A.PART_ID = L.PART_ID \n");//一个配件对应一个货位
            sql.append("            AND A.PART_ID = D.PART_ID \n");
            sql.append("            AND A.PART_ID = BD.PART_ID \n");//一个配件对应多货位然后确定一个具体货位
            sql.append("            AND BD.LOC_ID = L.LOC_ID \n");
            sql.append("            AND L.WH_ID = ").append(whId);
            sql.append("            AND L.ORG_ID = ").append(orgId);
            sql.append("            AND A.STATUS = 1 \n");
            sql.append("            AND SM.STATUS = 1 \n");
            sql.append("            AND SM.PICK_ORDER_ID = '").append(pickOrderId).append("' \n");

            if (!"".equals(partOldCode)) {
                sql.append("    AND UPPER(D.PART_OLDCODE) LIKE '%").append(partOldCode.toUpperCase()).append("%'");
            }
            if (!"".equals(partCname)) {
                sql.append("    AND D.PART_CNAME LIKE '%").append(partCname).append("%'");
            }
            if (!"".equals(partCode)) {
                sql.append("    AND UPPER(D.PART_CODE) LIKE '%").append(partCode.toUpperCase()).append("%'");
            }

            sql.append("          GROUP BY A.PART_ID, \n");
            sql.append("                   D.PART_CODE, \n");
            sql.append("                   D.PART_OLDCODE, \n");
            sql.append("                   D.PART_CNAME, \n");
            sql.append("                   D.UNIT, \n");
            sql.append("                   BD.BATCH_NO, \n");//20170825 add
            sql.append("                   L.LOC_ID, \n");
            sql.append("                   L.LOC_CODE, \n");
            sql.append("                   L.LOC_NAME, \n");
            sql.append("                   D.MIN_PACK2 \n");
//            sql.append("                   L.MIN_PK \n");
            sql.append("                   ) DT \n");
            sql.append("  WHERE DT.PKGEDQTY < DT.SALES_QTY \n");
            sql.append(" 　ORDER BY DT.PART_OLDCODE");

            return pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
        } catch (Exception e) {
            throw e;
        }
    }

    public boolean queryPkgBoxInfo(String pickOrderId, String pkgNo) throws Exception {
        List<Map<String, Object>> list = null;
        try {

            StringBuffer sql = new StringBuffer();
            sql.append(" SELECT T.PKG_NO, \n");
            sql.append("        T.PICK_ORDER_ID \n");
            sql.append("   FROM TT_PART_PKG_BOX_DTL T \n");
            sql.append(" WHERE T.PKG_NO ='").append(pkgNo).append("'");
            sql.append(" AND T.PICK_ORDER_ID='").append(pickOrderId).append("'");

            list = pageQuery(sql.toString(), null, getFunName());
            if (list.size() > 0) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            throw e;
        }
    }

    public List<Map<String, Object>> getPartGroup(String pickOrderId,String whId, String partId, 
    		String locId,String batchNo) {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT A.PART_ID,\n");
        sql.append("       D.PART_CODE,\n");
        sql.append("       D.PART_OLDCODE,\n");
        sql.append("       D.PART_CNAME,\n");
        sql.append("       A.SO_ID,\n");
        sql.append("       A.SLINE_ID,\n");
        sql.append("       D.UNIT,\n");
        sql.append("       BD.LOC_ID,\n");
        sql.append("       BD.BATCH_NO,\n");//20170901 add
        sql.append("       NVL(SUM(BD.BOOKED_QTY), '0') AS SALES_QTY,\n");
        sql.append("       NVL((SELECT SUM(PKG_QTY) FROM TT_PART_PKG_DTL TPPD\n");
        sql.append("            WHERE TPPD.PICK_ORDER_ID = '" + pickOrderId + "'\n");
        sql.append("              AND TPPD.PART_ID = A.PART_ID\n");
        sql.append("              AND TPPD.LOC_ID = BD.LOC_ID\n");
        sql.append("              AND TPPD.BATCH_NO = BD.BATCH_NO\n");//20170901 add
        sql.append("              AND TPPD.PKG_NO IS NOT NULL),\n");
        sql.append("           '0') + NVL((SELECT SUM(TPPD.LOC_BO_QTY) FROM TT_PART_PKG_DTL TPPD\n");
        sql.append("                       WHERE TPPD.PICK_ORDER_ID = '" + pickOrderId + "'\n");
        sql.append("                         AND TPPD.PART_ID = A.PART_ID\n");
        sql.append("                         AND TPPD.LOC_ID = BD.LOC_ID\n");
        sql.append("                         AND TPPD.BATCH_NO = BD.BATCH_NO\n");//20170901 add
        sql.append("                         AND TPPD.PKG_NO IS NULL), --现场BO数量\n");
        sql.append("                      '0') AS PKGEDQTY");
        sql.append("  FROM TT_PART_SO_DTL A, TT_PART_SO_MAIN SM, TT_PART_DEFINE D, TT_PART_BOOK_DTL BD\n");
        sql.append("   WHERE A.SO_ID = SM.SO_ID\n");
        sql.append("   AND A.SO_ID = BD.ORDER_ID\n");
        sql.append("   AND A.PART_ID = BD.PART_ID\n");
        sql.append("   AND A.PART_ID = D.PART_ID\n");
        sql.append("   AND SM.PICK_ORDER_ID = '" + pickOrderId + "'\n");
        sql.append("   AND A.PART_ID = ").append(partId);
        sql.append("   AND BD.LOC_ID = ").append(locId);
        sql.append("   AND BD.BATCH_NO = ").append(batchNo);//20170901 add
        sql.append(" GROUP BY A.PART_ID,A.SO_ID,A.SLINE_ID, D.PART_CODE, D.PART_OLDCODE, D.PART_CNAME,D.UNIT,BD.LOC_ID");
        sql.append(" ,BD.BATCH_NO");//20170901 add

        List<Map<String, Object>> list = this.pageQuery(sql.toString(), null, this.getFunName());
        return list;
    }

    public PageResult<Map<String, Object>> queryPartConfirm(
            RequestWrapper request, Integer curPage, Integer pageSizeMiddle) throws Exception {
        try {
            String pickOrderId = CommonUtils.checkNull(request.getParamValue("pickOrderId"));
            String soCode = CommonUtils.checkNull(request.getParamValue("soCode"));
            String dealerName = CommonUtils.checkNull(request.getParamValue("dealerName"));
            String whId = CommonUtils.checkNull(request.getParamValue("whId"));

            StringBuffer sql = new StringBuffer();

            sql.append("SELECT ZA_CONCAT(M.SO_CODE) SO_CODE,\n");
            sql.append("       M.DEALER_NAME,\n");
            sql.append("       M.PICK_ORDER_ID,\n");
            sql.append("       M.ORDER_TYPE,\n");
            sql.append("       W.WH_NAME,\n");
            sql.append("       BT.ASS_NO,\n");
            sql.append("       BT.STATE,\n");
            sql.append("       (SELECT SUM(B.PRINT_NUM) FROM TT_PART_PKG_BOX_DTL B ");
            sql.append("         WHERE B.PICK_ORDER_ID = M.PICK_ORDER_ID\n");
            sql.append("           AND B.ASS_NO = BT.ASS_NO\n");
            sql.append("           AND B.ASS_NO IS NOT NULL ) PRINT_NUM, --打印次数\n");
            sql.append("       (SELECT ZA_CONCAT(B.PKG_NO)\n");
            sql.append("          FROM TT_PART_PKG_BOX_DTL B\n");
            sql.append("         WHERE B.PICK_ORDER_ID = M.PICK_ORDER_ID\n");
            sql.append("           AND B.ASS_NO = BT.ASS_NO\n");
            sql.append("           AND B.ASS_NO IS NOT NULL ) PKG_NO1, --随车箱号\n");
            sql.append("       (SELECT ZA_CONCAT(B.PKG_NO)\n");
            sql.append("          FROM TT_PART_PKG_BOX_DTL B\n");
            sql.append("         WHERE B.PICK_ORDER_ID = M.PICK_ORDER_ID\n");
//            sql.append("           AND B.ASS_NO = BT.ASS_NO\n");
            sql.append("           AND B.ASS_NO IS NULL ) PKG_NO2 --未随车箱号 \n");
            sql.append("  FROM TT_PART_SO_MAIN          M,\n");
            sql.append("       TT_PART_WAREHOUSE_DEFINE W,\n");
            sql.append("       TT_PART_PKG_BOX_DTL      BT\n");
            sql.append(" WHERE M.WH_ID = W.WH_ID\n");
            sql.append("   AND BT.PICK_ORDER_ID = M.PICK_ORDER_ID\n");
            sql.append("   AND (M.STATE = ").append(Constant.CAR_FACTORY_PKG_STATE_02);
            sql.append("  OR M.STATE = ").append(Constant.CAR_FACTORY_PKG_STATE_01).append(") \n");
            sql.append("   AND BT.ASS_NO IS NOT NULL\n");
            sql.append("   AND BT.VIN IS NOT NULL\n");
            sql.append("   AND BT.VIN IS NOT NULL\n");

            if (!"".equals(pickOrderId)) {
                sql.append("    AND M.PICK_ORDER_ID LIKE '%").append(pickOrderId).append("%'");
            }
            if (!"".equals(soCode)) {
                sql.append("    AND M.SO_CODE LIKE '%").append(soCode).append("%'");
            }
            if (!"".equals(dealerName)) {
                sql.append("    AND M.DEALER_NAME LIKE '%").append(dealerName).append("%'");
            }
            if (!"".equals(whId)) {
                sql.append("    AND M.WH_ID = ").append(whId);
            }

            sql.append("GROUP BY M.DEALER_NAME,\n");
            sql.append("         M.PICK_ORDER_ID,\n");
            sql.append("         M.ORDER_TYPE,\n");
            sql.append("         W.WH_NAME,\n");
            sql.append("         BT.ASS_NO,\n");
            sql.append("         BT.STATE\n");
            sql.append("ORDER BY M.PICK_ORDER_ID\n");

            return pageQuery(sql.toString(), null, getFunName(), pageSizeMiddle, curPage);
        } catch (Exception e) {
            throw e;
        }
    }

    public List<Map<String, Object>> queryPartCar(RequestWrapper request) throws Exception {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        try {

            StringBuffer sql = new StringBuffer();
            String pickOrderId = CommonUtils.checkNull(request.getParamValue("pickOrderId"));
            String soCode = CommonUtils.checkNull(request.getParamValue("soCode"));
            String dealerName = CommonUtils.checkNull(request.getParamValue("dealerName"));
            String whId = CommonUtils.checkNull(request.getParamValue("whId"));

            sql.append("SELECT ZA_CONCAT(M.SO_CODE) SO_CODE,\n");
            sql.append("       M.DEALER_NAME,\n");
            sql.append("       M.PICK_ORDER_ID,\n");
            sql.append("       M.ORDER_TYPE,\n");
            sql.append("       W.WH_NAME,\n");
            sql.append("       BT.ASS_NO,\n");
            sql.append("       BT.STATE,\n");
            sql.append("       (SELECT ZA_CONCAT(B.PKG_NO)\n");
            sql.append("          FROM TT_PART_PKG_BOX_DTL B\n");
            sql.append("         WHERE B.PICK_ORDER_ID = M.PICK_ORDER_ID\n");
            sql.append("           AND B.ASS_NO = BT.ASS_NO\n");
            sql.append("           AND B.ASS_NO IS NOT NULL ) PKG_NO1, --随车箱号\n");
            sql.append("       (SELECT ZA_CONCAT(B.PKG_NO)\n");
            sql.append("          FROM TT_PART_PKG_BOX_DTL B\n");
            sql.append("         WHERE B.PICK_ORDER_ID = M.PICK_ORDER_ID\n");
            sql.append("           AND B.ASS_NO = BT.ASS_NO\n");
            sql.append("           AND B.ASS_NO IS NULL ) PKG_NO2 --未随车箱号 \n");
            sql.append("  FROM TT_PART_SO_MAIN          M,\n");
            sql.append("       TT_PART_WAREHOUSE_DEFINE W,\n");
            sql.append("       TT_PART_PKG_BOX_DTL      BT\n");
            sql.append(" WHERE M.WH_ID = W.WH_ID\n");
            sql.append("   AND BT.PICK_ORDER_ID = M.PICK_ORDER_ID\n");
            sql.append("   AND (M.STATE = ").append(Constant.CAR_FACTORY_PKG_STATE_02);
            sql.append("  OR M.STATE = ").append(Constant.CAR_FACTORY_PKG_STATE_01).append(") \n");
            sql.append("   AND BT.ASS_NO IS NOT NULL\n");
            sql.append("   AND BT.VIN IS NOT NULL\n");

            if (!"".equals(pickOrderId)) {
                sql.append("    AND M.PICK_ORDER_ID LIKE '%").append(pickOrderId).append("%'");
            }
            if (!"".equals(soCode)) {
                sql.append("    AND M.SO_CODE LIKE '%").append(soCode).append("%'");
            }
            if (!"".equals(dealerName)) {
                sql.append("    AND M.DEALER_NAME LIKE '%").append(dealerName).append("%'");
            }
            if (!"".equals(whId)) {
                sql.append("    AND M.WH_ID = ").append(whId);
            }

            sql.append("GROUP BY M.DEALER_NAME,\n");
            sql.append("         M.PICK_ORDER_ID,\n");
            sql.append("         M.ORDER_TYPE,\n");
            sql.append("         W.WH_NAME,\n");
            sql.append("         BT.ASS_NO,\n");
            sql.append("         BT.STATE\n");
            sql.append("ORDER BY M.PICK_ORDER_ID\n");

            list = pageQuery(sql.toString(), null, getFunName());
        } catch (Exception e) {
            throw e;
        }
        return list;
    }

    /**
     * 清除包装信息
     *
     * @param pickOrderId
     */
    public void cleanPkgInfo(String pickOrderId) {
        StringBuffer sql = new StringBuffer();
        sql.append(" DELETE FROM tt_part_pkg_box_dtl d WHERE d.pick_order_id='").append(pickOrderId).append("'");
        this.update(sql.toString(), null);
    }

    /**
     * 查询已经处理的预产生现场BO的数量
     *
     * @param pickOrderId
     * @param partId
     * @param locId
     * @return
     * @throws Exception
     */
    public Map<String, Object> queryXCBOQty(String pickOrderId,
                                            String partId, String locId) throws Exception {
        try {

            StringBuffer sql = new StringBuffer();

            sql.append("        SELECT NVL(SUM(T.LOC_BO_QTY), 0) BO_QTY\n");
            sql.append("          FROM TT_PART_PKG_DTL T\n");
            sql.append("         WHERE T.PICK_ORDER_ID = '" + pickOrderId + "'\n");
            sql.append("           AND T.PART_ID = " + partId + "\n");
            sql.append("           AND T.LOC_ID = " + locId + "\n");
            sql.append("           AND T.PKG_NO IS NULL");

            return pageQueryMap(sql.toString(), null, getFunName());
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * 是否可以取消装箱校验逻辑
     *
     * @param pickOrderId
     * @param pkgNo
     * @return
     * @throws Exception
     */
    public boolean pkgCancelable(String pickOrderId, String pkgNo) throws Exception {
        List<Map<String, Object>> list = null;
        try {
            StringBuffer sql = new StringBuffer();

            sql.append("SELECT D.BOX_ID\n");
            sql.append("  FROM TT_PART_PKG_BOX_DTL D\n");
            sql.append(" WHERE D.TRPLAN_ID IS NOT NULL\n");
            if (pkgNo != null && !"".equals(pkgNo)) {
                sql.append("   AND D.PKG_NO ='" + pkgNo + "'");
            }
            sql.append("   AND D.PICK_ORDER_ID ='" + pickOrderId + "'");

            list = pageQuery(sql.toString(), null, getFunName());
            if (list.size() > 0) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            throw e;
        }
    }

    public List<Map<String, Object>> getDealerAddrList(String dealerId) {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT d.addr_id,d.addr FROM tt_part_addr_define d WHERE d.dealer_id=" + dealerId);

        List<Map<String, Object>> list = this.pageQuery(sql.toString(), null, this.getFunName());
        return list;

    }

}
