package com.infodms.dms.dao.parts.partAllotManager;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.po.TmDealerPO;
import com.infodms.dms.po.TtPartDlrOrderMainPO;
import com.infodms.dms.po.TtPartWarehouseDefinePO;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;
import org.apache.log4j.Logger;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unchecked")
public class PartTransferDao extends BaseDao {
    public static Logger logger = Logger.getLogger(PartTransferDao.class);

    private static final PartTransferDao dao = new PartTransferDao();

    private PartTransferDao() {

    }

    public static final PartTransferDao getInstance() {
        return dao;
    }

    @Override
    protected PO wrapperPO(ResultSet rs, int idx) {
        return null;
    }

    public PageResult<Map<String, Object>> queryPartTransferList(
            TtPartDlrOrderMainPO po, String startDate, String endDate,
            String startDate1, String endDate1, AclUserBean logonUser,
            Integer curPage, Integer pageSize) throws Exception {
        PageResult<Map<String, Object>> ps;
        try {
            StringBuffer sql = new StringBuffer("");
            sql.append("SELECT T.ORDER_ID,T.ORDER_CODE,T.DEALER_CODE,T.DEALER_NAME,T.SELLER_CODE,T.SELLER_NAME,U.NAME,T.CREATE_DATE,T.ORDER_AMOUNT,T.SUBMIT_DATE,T.STATE FROM TT_PART_DLR_ORDER_MAIN T,TC_USER U WHERE T.CREATE_BY=U.USER_ID");
            if (logonUser.getDealerId() != null) {//如果是车厂就可以查看所有的调拨单,如果是供应中心或服务商就只能查看调出单位是自己的调拨单
                sql.append(" AND T.SELLER_ID=").append(logonUser.getDealerId());
            }

            if (po.getOrderCode() != null && !"".equals(po.getOrderCode().trim())) {
                sql.append(" AND T.ORDER_CODE LIKE '%")
                        .append(po.getOrderCode().trim()).append("%'\n");
            }
            if (po.getDealerName() != null && !"".equals(po.getDealerName().trim())) {
                sql.append(" AND T.DEALER_NAME LIKE '%")
                        .append(po.getDealerName().trim()).append("%'\n");
            }
            if (po.getSellerName() != null && !"".equals(po.getSellerName().trim())) {
                sql.append(" AND T.SELLER_NAME LIKE '%")
                        .append(po.getSellerName().trim()).append("%'\n");
            }
            if (!"".equals(startDate)) {
                sql.append(" AND to_date(T.CREATE_DATE)>=").append("to_date('").append(startDate).append("','yyyy-MM-dd')");
            }

            if (!"".equals(endDate)) {
                sql.append(" AND to_date(T.CREATE_DATE)<=").append("to_date('").append(endDate).append("','yyyy-MM-dd')");
            }
            if (!"".equals(startDate1)) {
                sql.append(" AND to_date(T.SUBMIT_DATE)>=").append("to_date('").append(startDate1).append("','yyyy-MM-dd')");
            }

            if (!"".equals(endDate1)) {
                sql.append(" AND to_date(T.SUBMIT_DATE)<=").append("to_date('").append(endDate1).append("','yyyy-MM-dd')");
            }
            if (po.getState() != null && po.getState() != 1) {
                if (po.getState().intValue() == Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE_02.intValue()) {//如果是已提交
                    sql.append(" AND T.STATE=").append(Constant.PART_TRANSFER_ORDER_STATE_01);
                } else {
                    sql.append(" AND T.STATE=").append(po.getState());
                }
            }
            sql.append(" AND T.ORDER_TYPE=").append(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_05);
            sql.append(" AND T.STATUS=1");
            sql.append(" ORDER BY NVL(T.SUBMIT_DATE,T.CREATE_DATE) ASC");
            ps = pageQuery(sql.toString(), null, getFunName(), pageSize,
                    curPage);
        } catch (Exception e) {
            throw e;
        }
        return ps;
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

    public PageResult<Map<String, Object>> queryPartByWhIdList(String whId,
                                                               AclUserBean logonUser, String partCode, String partOldCode,
                                                               String partCname, Integer curPage, Integer pageSize) throws Exception {
        PageResult<Map<String, Object>> ps;
        try {
            StringBuffer sql = new StringBuffer("");
            sql.append("SELECT T.PART_ID,T.PART_CODE,T.PART_OLDCODE,T.PART_CNAME,T.IS_DIRECT,T.IS_PLAN,T.IS_LACK,T.IS_REPLACED,T.MIN_PACK1,T.UNIT,S.SALE_PRICE1,V.NORMAL_QTY FROM VW_PART_STOCK V,TT_PART_DEFINE T,TT_PART_SALES_PRICE S");
            sql.append(" WHERE T.PART_ID=V.PART_ID AND T.PART_ID=S.PART_ID");
            sql.append("   AND V.ORG_ID=").append(logonUser.getDealerId());
            sql.append("    AND V.WH_ID=");
            if ("".equals(whId)) {
                sql.append(0);
            } else {
                sql.append(whId);
            }
            if (!"".equals(partCode)) {
                sql.append(" AND T.PART_CODE LIKE '%")
                        .append(partCode).append("%'\n");
            }
            if (!"".equals(partOldCode)) {
                sql.append(" AND T.PART_OLDCODE LIKE '%")
                        .append(partOldCode).append("%'\n");
            }
            if (!"".equals(partCname)) {
                sql.append(" AND T.PART_CNAME LIKE '%")
                        .append(partCname).append("%'\n");
            }
            ps = pageQuery(sql.toString(), null, getFunName(), pageSize,
                    curPage);
        } catch (Exception e) {
            throw e;
        }
        return ps;
    }

    public List<Map<String, Object>> queryPartInfoByWhId(String whId,
                                                         AclUserBean logonUser) throws Exception {
        List<Map<String, Object>> list;
        try {
            StringBuffer sql = new StringBuffer("");
            sql.append("SELECT T.PART_ID,T.PART_CODE,T.PART_OLDCODE,T.PART_CNAME,T.IS_DIRECT,T.IS_PLAN,T.IS_LACK,T.IS_REPLACED,T.MIN_PACK1,T.UNIT,S.SALE_PRICE1,V.NORMAL_QTY FROM VW_PART_STOCK V,TT_PART_DEFINE T,TT_PART_SALES_PRICE S");
            sql.append(" WHERE T.PART_ID=V.PART_ID AND T.PART_ID=S.PART_ID");
            sql.append("   AND V.ORG_ID=").append(logonUser.getDealerId());
            sql.append("    AND V.WH_ID=").append(whId);
            sql.append(" ORDER BY T.PART_ID ASC");
            list = pageQuery(sql.toString(), null, getFunName());
        } catch (Exception e) {
            throw e;
        }
        return list;
    }

    public List<Map<String, Object>> queryCurNormalQty(String[] partIds,
                                                       String whId, String dealerId) throws Exception {
        List<Map<String, Object>> list;
        List<Object> params = new ArrayList<Object>();
        try {
            StringBuffer sql = new StringBuffer("");
            sql.append("SELECT T.NORMAL_QTY FROM VW_PART_STOCK T WHERE T.PART_ID IN(");
            for (String partId : partIds) {
                sql.append(partId).append(",");
            }
            sql.deleteCharAt(sql.length() - 1);
            sql.append(") AND T.WH_ID=? AND T.ORG_ID=?");
            params.add(whId);
            params.add(dealerId);
            list = pageQuery(sql.toString(), params, getFunName());
        } catch (Exception e) {
            throw e;
        }
        return list;
    }

    public List<Map<String, Object>> queryPartTransfer(RequestWrapper request, AclUserBean logonUser) throws Exception {
        List<Map<String, Object>> list;
        try {
            String orderCode = CommonUtils.checkNull(request.getParamValue("ORDER_CODE"));//订单号
            String dealerName = CommonUtils.checkNull(request.getParamValue("DEALER_NAME"));//调入单位
            String sellerName = CommonUtils.checkNull(request.getParamValue("SELLER_NAME"));//调出单位
            String startDate = CommonUtils.checkNull(request.getParamValue("startDate"));//制单开始时间
            String endDate = CommonUtils.checkNull(request.getParamValue("endDate"));//制单结束时间
            String state = CommonUtils.checkNull(request.getParamValue("STATE"));//订单状态
            String startDate1 = CommonUtils.checkNull(request.getParamValue("startDate1"));//提交开始时间
            String endDate1 = CommonUtils.checkNull(request.getParamValue("endDate1"));//提交结束时间

            StringBuffer sql = new StringBuffer("");
            sql.append("SELECT T.ORDER_ID,T.ORDER_CODE,T.DEALER_CODE,T.DEALER_NAME,T.SELLER_CODE,T.SELLER_NAME,U.NAME,T.CREATE_DATE,T.ORDER_AMOUNT,T.SUBMIT_DATE,decode(T.STATE,")
                    .append(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE_01).append(",'已保存',")
                    .append(Constant.PART_TRANSFER_ORDER_STATE_01).append(",'已提交',")
                    .append(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE_06).append(",'车厂已审核',")
                    .append(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE_05).append(",'已驳回',")
                    .append(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE_04).append(",'已作废')");
            sql.append(" STATE FROM TT_PART_DLR_ORDER_MAIN T,TC_USER U WHERE T.CREATE_BY=U.USER_ID");

            if (logonUser.getDealerId() != null) {//如果是车厂就可以查看所有的调拨单,如果是供应中心或服务商就只能查看调出单位是自己的调拨单
                sql.append(" AND T.SELLER_ID=").append(logonUser.getDealerId());
            }

            if (orderCode != null && !"".equals(orderCode.trim())) {
                sql.append(" AND T.ORDER_CODE LIKE '%")
                        .append(orderCode.trim()).append("%'\n");
            }
            if (dealerName != null && !"".equals(dealerName.trim())) {
                sql.append(" AND T.DEALER_NAME LIKE '%")
                        .append(dealerName.trim()).append("%'\n");
            }
            if (sellerName != null && !"".equals(sellerName.trim())) {
                sql.append(" AND T.SELLER_NAME LIKE '%")
                        .append(sellerName.trim()).append("%'\n");
            }
            if (!"".equals(startDate)) {
                sql.append(" AND to_date(T.CREATE_DATE)>=").append("to_date('").append(startDate).append("','yyyy-MM-dd')");
            }

            if (!"".equals(endDate)) {
                sql.append(" AND to_date(T.CREATE_DATE)<=").append("to_date('").append(endDate).append("','yyyy-MM-dd')");
            }
            if (!"".equals(startDate1)) {
                sql.append(" AND to_date(T.SUBMIT_DATE)>=").append("to_date('").append(startDate1).append("','yyyy-MM-dd')");
            }

            if (!"".equals(endDate1)) {
                sql.append(" AND to_date(T.SUBMIT_DATE)<=").append("to_date('").append(endDate1).append("','yyyy-MM-dd')");
            }

            if (state != null && !"1".equals(state) && !"".equals(state)) {
                if (state.equals(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE_02.toString())) {//如果是已提交
                    sql.append(" AND T.STATE=").append(Constant.PART_TRANSFER_ORDER_STATE_01);
                } else {
                    sql.append(" AND T.STATE=").append(state);
                }
            }
            sql.append(" AND T.ORDER_TYPE=").append(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_05);
            sql.append(" AND T.STATUS=1");
            sql.append(" ORDER BY T.CREATE_DATE DESC");

            list = pageQuery(sql.toString(), null, getFunName());
        } catch (Exception e) {
            throw e;
        }

        return list;

    }

    public Map getPartDlrOrderMainInfo(String orderId) throws Exception {
        try {
            StringBuffer sql = new StringBuffer("");
            sql.append("SELECT T.ORDER_ID,T.ORDER_CODE,T.SELLER_ID,T.SELLER_CODE,T.SELLER_NAME,U.USER_ID,U.NAME,TO_CHAR(T.CREATE_DATE,'yyyy-MM-dd') CREATE_DATE,T.DEALER_ID,T.DEALER_CODE,T.RCV_ORG,T.RECEIVER,T.ADDR_ID,T.ADDR,T.TEL,T.POST_CODE,T.TRANS_TYPE,T.PAY_TYPE,TO_CHAR(T.ORDER_AMOUNT,'fm999,999,990.00') ORDER_AMOUNT,T.WH_ID,T.WH_CNAME,T.STATION,T.REMARK,T.STATE,T.AUDIT_OPINION");
            sql.append(" FROM TT_PART_DLR_ORDER_MAIN T,TC_USER U WHERE T.CREATE_BY=U.USER_ID AND T.ORDER_ID=");
            sql.append(CommonUtils.parseLong(orderId));
            return pageQueryMap(sql.toString(), null, getFunName());
        } catch (Exception e) {
            throw e;
        }
    }

    public PageResult<Map<String, Object>> queryPartTransferDetailList(
            String orderId, Integer curPage, Integer pageSize) throws Exception {
        PageResult<Map<String, Object>> ps;
        try {
            StringBuffer sql = new StringBuffer("");
            sql.append("SELECT T.LINE_ID,T.PART_CODE,T.PART_OLDCODE,T.PART_CNAME,T.MIN_PACKAGE,T.BUY_QTY,T.UNIT,TO_CHAR(T.BUY_PRICE,'fm999,999,990.00') BUY_PRICE,TO_CHAR(T.BUY_AMOUNT,'fm999,999,990.00') BUY_AMOUNT,T.REMARK");
            sql.append("  FROM TT_PART_DLR_ORDER_DTL T WHERE T.ORDER_ID=")
                    .append(orderId);
            sql.append(" AND T.STATUS=1");
            ps = pageQuery(sql.toString(), null, getFunName(), pageSize,
                    curPage);
        } catch (Exception e) {
            throw e;
        }
        return ps;
    }

    public List<Map<String, Object>> queryPartDlrOrderDtl(String orderId) throws Exception {
        List<Map<String, Object>> list;
        try {
            StringBuffer sql = new StringBuffer("");
            sql.append("SELECT T.LINE_ID,T.ORDER_ID,T.PART_ID,T.PART_CODE,T.PART_OLDCODE,T.PART_CNAME,T.MIN_PACKAGE,T.STOCK_QTY,T.BUY_QTY,T.UNIT,T.BUY_PRICE,T.BUY_AMOUNT,T.REMARK");
            sql.append("  FROM TT_PART_DLR_ORDER_DTL T WHERE T.ORDER_ID=")
                    .append(orderId);
            sql.append(" AND T.STATUS=1");
            list = pageQuery(sql.toString(), null, getFunName());
        } catch (Exception e) {
            throw e;
        }
        return list;
    }

    public PageResult<Map<String, Object>> queryPartTransferChkList(
            TtPartDlrOrderMainPO po, String startDate, String endDate,
            String startDate1, String endDate1, AclUserBean logonUser,
            Integer curPage, Integer pageSize) throws Exception {
        PageResult<Map<String, Object>> ps;
        try {
            StringBuffer sql = new StringBuffer("");
            sql.append("SELECT T.ORDER_ID,T.ORDER_CODE,T.DEALER_CODE,T.DEALER_NAME,T.SELLER_CODE,T.SELLER_NAME,U.NAME,T.CREATE_DATE,TO_CHAR(T.ORDER_AMOUNT, 'fm999,999,990.00') ORDER_AMOUNT,T.SUBMIT_DATE,T.STATE FROM TT_PART_DLR_ORDER_MAIN T,TC_USER U WHERE T.CREATE_BY=U.USER_ID");
            if (po.getOrderCode() != null && !"".equals(po.getOrderCode().trim())) {
                sql.append(" AND T.ORDER_CODE LIKE '%")
                        .append(po.getOrderCode().trim()).append("%'\n");
            }
            if (po.getDealerName() != null && !"".equals(po.getDealerName().trim())) {
                sql.append(" AND T.DEALER_NAME LIKE '%")
                        .append(po.getDealerName().trim()).append("%'\n");
            }
            if (po.getSellerName() != null && !"".equals(po.getSellerName().trim())) {
                sql.append(" AND T.SELLER_NAME LIKE '%")
                        .append(po.getSellerName().trim()).append("%'\n");
            }
            if (!"".equals(startDate)) {
                sql.append(" AND to_date(T.CREATE_DATE)>=").append("to_date('").append(startDate).append("','yyyy-MM-dd')");
            }

            if (!"".equals(endDate)) {
                sql.append(" AND to_date(T.CREATE_DATE)<=").append("to_date('").append(endDate).append("','yyyy-MM-dd')");
            }
            if (!"".equals(startDate1)) {
                sql.append(" AND to_date(T.SUBMIT_DATE)>=").append("to_date('").append(startDate1).append("','yyyy-MM-dd')");
            }

            if (!"".equals(endDate1)) {
                sql.append(" AND to_date(T.SUBMIT_DATE)<=").append("to_date('").append(endDate1).append("','yyyy-MM-dd')");
            }

            sql.append(" AND T.STATE=").append(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_STATE_02);

            sql.append(" AND T.ORDER_TYPE=").append(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_05);
            sql.append(" AND T.STATUS=1");
            sql.append(" ORDER BY T.CREATE_DATE DESC");
            ps = pageQuery(sql.toString(), null, getFunName(), pageSize,
                    curPage);
        } catch (Exception e) {
            throw e;
        }
        return ps;
    }

    public void updateOrderAmount(String orderId) throws Exception {
        try {
            StringBuffer sql = new StringBuffer("");
            sql.append("UPDATE TT_PART_DLR_ORDER_MAIN T1 SET(T1.ORDER_AMOUNT)=(SELECT SUM(T2.BUY_AMOUNT) FROM TT_PART_DLR_ORDER_DTL T2 WHERE T1.ORDER_ID=T2.ORDER_ID AND T2.STATUS=1)");
            sql.append("  WHERE T1.ORDER_ID=")
                    .append(orderId);
            update(sql.toString(), null);
        } catch (Exception e) {
            throw e;
        }
    }

    public PageResult<Map<String, Object>> getSalesRelation(String dealerId, TmDealerPO po,
                                                            String type, String rcvOrgId, String name, String code,
                                                            int pageSize, Integer curPage) {
        StringBuffer sql = new StringBuffer();
        //2为接收单位
        //3为地址
        if ("2".equals(type)) {
            sql.append("SELECT DISTINCT T.CHILDORG_ID,T.CHILDORG_NAME,T.CHILDORG_CODE FROM TT_PART_SALES_RELATION T,TM_DEALER T2 WHERE T.CHILDORG_ID=T2.DEALER_ID");
            sql.append(" AND T.PARENTORG_ID IN (select t1.parentorg_id from TT_PART_SALES_RELATION t1 where t1.childorg_id=")
                    .append(dealerId);
            sql.append(" AND T1.STATE=").append(Constant.STATUS_ENABLE);
            sql.append(" AND T1.STATUS=1");
            sql.append(" ) AND T.CHILDORG_ID!=").append(dealerId);
            sql.append(" AND T.STATE=").append(Constant.STATUS_ENABLE);
            sql.append(" AND T.STATUS=1");
            sql.append(" AND T2.PDEALER_TYPE=").append(po.getPdealerType());
            if (!"".equals(name)) {
                sql.append(" and T.CHILDORG_NAME like '%").append(name).append("%'");
            }
            if (!"".equals(code)) {
                sql.append(" and T.CHILDORG_CODE like '%").append(code).append("%'");
            }
        } else if ("3".equals(type)) {
            sql.append("SELECT T.ADDR_ID,T.ADDR,T.LINKMAN,T.TEL,T.POST_CODE,T.STATION FROM TT_PART_ADDR_DEFINE T WHERE T.STATE=");
            sql.append(Constant.STATUS_ENABLE);
            sql.append(" AND T.STATUS=1 AND T.DEALER_ID=").append(rcvOrgId);
            if (!"".equals(name)) {
                sql.append(" and T.ADDR like '%").append(name).append("%'");
            }
        } else {
            return null;
        }
        return this.pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
    }

}