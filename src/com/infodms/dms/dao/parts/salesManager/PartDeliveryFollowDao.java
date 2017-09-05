package com.infodms.dms.dao.parts.salesManager;

import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;
import org.apache.log4j.Logger;

import java.sql.ResultSet;
import java.util.Map;

public class PartDeliveryFollowDao extends BaseDao<PO> {
    public static Logger logger = Logger.getLogger(PartDeliveryFollowDao.class);

    private static final PartDeliveryFollowDao dao = new PartDeliveryFollowDao();

    private PartDeliveryFollowDao() {
    }

    public static final PartDeliveryFollowDao getInstance() {
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
     * @throws : LastDate : 2014-3-31
     * @Title :
     * @Description: 配件待发运查询
     */

    public PageResult<Map<String, Object>> queryDatas(RequestWrapper request,
                                                      int curPage, int pageSize) {

        String orderType = CommonUtils.checkNull(request.getParamValue("ORDER_TYPE")); // 订单类型
        String pickOrderId = CommonUtils.checkNull(request.getParamValue("PICK_ORDER_ID"));// 拣配单
        String orderCode = CommonUtils.checkNull(request.getParamValue("ORDER_CODE"));// 拣配单
        String dealerCode = CommonUtils.checkNull(request.getParamValue("DEALER_CODE"));// 服务商代码
        String dealerName = CommonUtils.checkNull(request.getParamValue("DEALER_NAME"));// 服务商名称
        String sSubmitDate = CommonUtils.checkNull(request.getParamValue("SSUBMIT_DATE"));//提交时间 开始
        String eSubmitDate = CommonUtils.checkNull(request.getParamValue("ESUBMIT_DATE"));//提交时间 结束
        String sort = CommonUtils.checkNull(request.getParamValue("sort"));// 排序方式
        String isOver = CommonUtils.checkNull(request.getParamValue("is_over"));// 是否完成
        String sort2 = CommonUtils.checkNull(request.getParamValue("sort2"));// 阶段
        String state = CommonUtils.checkNull(request.getParamValue("state"));// 阶段状态

        StringBuffer sql = new StringBuffer();

        sql.append("SELECT * FROM VW_PART_ORDER_TRANS_KF\n");
        sql.append(" WHERE 1 = 1\n");
        if (orderType != "" && !"".equals(orderType)) {
            sql.append("   AND ORDER_TYPE2 = '" + orderType + "'\n");
        }

        if (pickOrderId != "" && !"".equals(pickOrderId)) {
            sql.append("   AND PICK_ORDER_ID LIKE '%" + pickOrderId + "%'\n");
        }
        if (dealerCode != "" && !"".equals(dealerCode)) {
            sql.append("   AND DEALER_CODE LIKE '%" + dealerCode + "%'\n");
        }
        if (dealerName != "" && !"".equals(dealerName)) {
            sql.append("   AND DEALER_NAME LIKE '%" + dealerName + "%'\n");
        }
        if (isOver != "" && !"".equals(isOver)) {
            sql.append("  AND IS_OVER = '" + isOver + "'");
        }
        if (!"".equals(sSubmitDate) && !"".equals(sSubmitDate)) {
            sql.append(" and CREATE_DATE>= to_date('").append(sSubmitDate).append(" 00:00:00','YYYY/MM/dd HH24:mi:ss')");
        }
        if (!"".equals(eSubmitDate) && !"".equals(eSubmitDate)) {
            sql.append(" and CREATE_DATE<= to_date('").append(eSubmitDate).append(" 23:59:59','YYYY/MM/dd HH24:mi:ss')");
        }
        if (!"".equals(sort2) && null != sort2) {
            if (!"".equals(state) && null != state) {
                sql.append(" AND ").append(sort2).append(" = '" + state + "'");
            }
        }
        if (!"".equals(orderCode) && null != orderCode) {
            sql.append("   AND ORDER_CODE LIKE '%" + orderCode.trim().toUpperCase() + "%'\n");
        }
        if (sort != "") {
            sql.append(" order BY ").append(sort).append(" desc");
        }

        PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null,
                getFunName(), pageSize, curPage);
        return ps;
    }
}
