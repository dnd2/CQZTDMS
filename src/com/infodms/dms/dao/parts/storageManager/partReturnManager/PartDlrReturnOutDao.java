package com.infodms.dms.dao.parts.storageManager.partReturnManager;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.po.TtPartWarehouseDefinePO;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;
import org.apache.log4j.Logger;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unchecked")
public class PartDlrReturnOutDao extends BaseDao {

    public static Logger logger = Logger.getLogger(PartDlrReturnOutDao.class);

    private static final PartDlrReturnOutDao dao = new PartDlrReturnOutDao();

    private PartDlrReturnOutDao() {

    }

    public static final PartDlrReturnOutDao getInstance() {
        return dao;
    }

    @Override
    protected PO wrapperPO(ResultSet rs, int idx) {
        return null;
    }

    public PageResult<Map<String, Object>> queryPartDlrReturnApplyList(
            String returnCode, String startDate, String endDate, AclUserBean logonUser,
            Integer curPage, Integer pageSize) throws Exception {
        PageResult<Map<String, Object>> ps;
        try {
            StringBuffer sql = new StringBuffer("");

            sql.append("SELECT T1.RETURN_ID,T1.RETURN_CODE,T1.DEALER_NAME,T1.CREATE_ORGNAME CREATE_DEALER, T2.NAME CREATE_NAME,T1.CREATE_DATE,T1.REMARK,T1.APPLY_DATE,T1.VERIFY_DATE FROM TT_PART_DLR_RETURN_MAIN T1,TC_USER T2");
            sql.append(" WHERE T1.CREATE_BY=T2.USER_ID");
            sql.append(" and T1.DEALER_ID=").append(logonUser.getDealerId());
            sql.append(" and T1.state=").append(Constant.PART_DLR_RETURN_STATUS_04);
            if (!"".equals(returnCode)) {
                sql.append(" AND T1.RETURN_CODE LIKE '%")
                        .append(returnCode).append("%'\n");
            }
            if (!"".equals(startDate)) {
                sql.append(" AND to_date(T1.CREATE_DATE)>=").append("to_date('").append(startDate).append("','yyyy-MM-dd')");
            }

            if (!"".equals(endDate)) {
                sql.append(" AND to_date(T1.CREATE_DATE)<=").append("to_date('").append(endDate).append("','yyyy-MM-dd')");
            }
            sql.append(" AND T1.STATUS=1");
            sql.append(" ORDER BY T1.APPLY_DATE ASC");
            ps = pageQuery(sql.toString(), null, getFunName(), pageSize,
                    curPage);
        } catch (Exception e) {
            throw e;
        }
        return ps;
    }

    public PageResult<Map<String, Object>> queryPartDlrReturnChkList(
            String returnId, String soCode, Integer curPage, Integer pageSize) throws Exception {

        PageResult<Map<String, Object>> ps;
        try {
            StringBuffer sql = new StringBuffer("");
            if ("".equals(soCode)) {//如果销售单号为空,那么采购数量就默认为0
                sql.append("select t.part_id,t.part_code,t.part_oldcode,t.part_cname,0 BUY_QTY,t.apply_qty,t.check_qty,t.remark from tt_part_dlr_return_dtl t where t.return_id=")
                        .append(CommonUtils.parseLong(returnId));
            } else {
                sql.append("select t2.part_id,t2.part_code,t2.part_oldcode,t2.part_cname,t2.buy_qty,t3.apply_qty,t3.check_qty,t3.remark from tt_part_outstock_main t1,tt_part_outstock_dtl t2,tt_part_dlr_return_dtl t3 ");
                sql.append("  where t1.out_id=t2.out_id  and t2.part_id=t3.part_id and t1.so_code=").append("'").append(soCode).append("'");
                sql.append(" and t3.return_id=").append(CommonUtils.parseLong(returnId));
            }
            ps = pageQuery(sql.toString(), null, getFunName(), pageSize,
                    curPage);
        } catch (Exception e) {
            throw e;
        }
        return ps;

    }

    public Map getPartDlrReturnMainInfo(String returnId) throws Exception {

        try {
            StringBuffer sql = new StringBuffer("");
            sql.append("SELECT T1.RETURN_ID, T1.RETURN_CODE,T1.CREATE_ORGNAME ORG_NAME,T1.DEALER_NAME,T1.SELLER_NAME,T1.SO_CODE,T1.REMARK FROM TT_PART_DLR_RETURN_MAIN T1 WHERE T1.RETURN_ID=");
            sql.append(CommonUtils.parseLong(returnId));
            return pageQueryMap(sql.toString(), null, getFunName());

        } catch (Exception e) {
            throw e;
        }

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

}
