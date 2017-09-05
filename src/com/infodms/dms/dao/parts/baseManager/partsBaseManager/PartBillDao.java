package com.infodms.dms.dao.parts.baseManager.partsBaseManager;

import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.po.TtIfStandardPO;
import com.infodms.dms.po.TtPartBillDefinePO;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;
import org.apache.log4j.Logger;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

/**
 * @author : chenjunjiang
 *         CreateDate     : 2013-4-9
 * @ClassName : PartBillDao
 * @Description : 配件服务商开票信息dao
 */
public class PartBillDao extends BaseDao {
    public static Logger logger = Logger.getLogger(PartBillDao.class);

    private static final PartBillDao dao = new PartBillDao();

    private PartBillDao() {

    }

    public static PartBillDao getInstance() {
        return dao;
    }

    @Override
    protected TtIfStandardPO wrapperPO(ResultSet rs, int idx) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * @param : @param bean
     * @param : @param curPage
     * @param : @param pageSize
     * @param : @return
     * @return :
     * @throws Exception
     * @throws Exception
     * @throws :         LastDate    : 2013-4-9
     * @Title :
     * @Description: 配件服务商开票信息
     */
    public PageResult<Map<String, Object>> queryPartBillList(String dealerName, String dealerCode, String invType,
                                                             Integer curPage, Integer pageSize) throws Exception {
        PageResult<Map<String, Object>> ps;
        try {
            StringBuffer sql = new StringBuffer("");
            sql.append("SELECT T.BILL_ID,T.DEALER_ID,T.DEALER_CODE,T.DEALER_NAME,T.TAX_NAME,T.INV_TYPE,T.TAX_NO,T.ADDR,T.MAIL_ADDR,T.TEL,T.BANK,T.ACCOUNT,T.REMARK,T.CREATE_DATE,T.UPDATE_DATE,T.STATE FROM TT_PART_BILL_DEFINE T WHERE T.STATUS=1\n");
            if (!dealerName.equals("") && dealerName != null) {
                sql.append("AND T.DEALER_NAME LIKE '%").append(dealerName).append("%'\n");
            }
            if (!dealerCode.equals("") && dealerCode != null) {
                sql.append("AND T.DEALER_CODE LIKE '%").append(dealerCode).append("%'\n");
            }
            if (null != invType && !"".equals(invType)) {
                sql.append("AND T.INV_TYPE = '" + invType + "'\n");
            }
            sql.append("ORDER BY T.CREATE_DATE DESC");
            ps = pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
        } catch (Exception e) {
            throw e;
        }
        return ps;
    }


    public Map<String, Object> getPartBillDetail(String billId) throws Exception {
        Map<String, Object> map;
        try {
            StringBuffer sql = new StringBuffer("");
            sql.append("SELECT T.BILL_ID,T.DEALER_ID,T.DEALER_CODE,T.DEALER_NAME,T.TAX_NAME,T.INV_TYPE,T.TAX_NO,T.ADDR,T.MAIL_ADDR,T.TEL,T.BANK,T.ACCOUNT,T.REMARK");
            sql.append(" FROM TT_PART_BILL_DEFINE T WHERE T.BILL_ID = ").append(billId);
            map = pageQueryMap(sql.toString(), null, getFunName());
        } catch (Exception e) {
            throw e;
        }
        return map;
    }

    /**
     * @param : @param request
     * @param : @return
     * @return :
     * @throws Exception
     * @throws :         LastDate    : 2013-4-9
     * @Title :
     * @Description: 查询满足条件的配件服务商开票信息, 主要用于数据下载
     */
    public List<Map<String, Object>> queryPartBill(RequestWrapper request) throws Exception {
        List<Map<String, Object>> list;
        try {

            String dealerName = CommonUtils.checkNull(request
                    .getParamValue("DEALER_NAME"));// 采购单位名称
            String dealerCode = CommonUtils.checkNull(request
                    .getParamValue("DEALER_CODE"));// 采购单位名称
            String invType = CommonUtils.checkNull(request
                    .getParamValue("dlrInvTpe"));//  开票类型
            StringBuilder sql = new StringBuilder("");
            sql.append("SELECT T.DEALER_CODE,T.DEALER_NAME,T.TAX_NAME,T.INV_TYPE,T.TAX_NO,T.ADDR,T.MAIL_ADDR,T.TEL,T.BANK,T.ACCOUNT");
            sql.append(" FROM TT_PART_BILL_DEFINE T WHERE 1=1 ");
            if (!"".equals(dealerName) && dealerName != null) {
                sql.append(" AND T.DEALER_NAME like '%").append(dealerName).append("%'");
            }
            if (!"".equals(dealerCode) && dealerCode != null) {
                sql.append(" AND T.DEALER_CODE like '%").append(dealerCode).append("%'");
            }
            if (null != invType && !"".equals(invType)) {
                sql.append("AND T.INV_TYPE = '" + invType + "'\n");
            }
            sql.append(" ORDER BY T.CREATE_DATE DESC");

            list = pageQuery(sql.toString(), null, getFunName());
        } catch (Exception e) {
            throw e;
        }

        return list;
    }

    public boolean checkDealerId(String strDealerId) throws Exception {
        try {
            TtPartBillDefinePO ttPartBillDefinePO = new TtPartBillDefinePO();
            ttPartBillDefinePO.setDealerId(CommonUtils.parseLong(strDealerId));
            List list = select(ttPartBillDefinePO);
            if (list.size() > 0) {
                return true;
            }
            return false;
        } catch (Exception e) {
            throw e;
        }
    }

}
