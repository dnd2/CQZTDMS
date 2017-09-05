package com.infodms.dms.dao.parts.salesManager;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.bean.OrgBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.dao.common.BaseDao;
import com.infodms.dms.dao.parts.baseManager.partsBaseManager.PartWareHouseDao;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PartPkgDao extends BaseDao {
    private static final PartPkgDao dao = new PartPkgDao();

    private PartPkgDao() {
    }

    public static final PartPkgDao getInstance() {
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
    public PageResult<Map<String, Object>> queryPkgOrder(RequestWrapper request, int curPage, int pageSize) {
        StringBuffer sql = new StringBuffer();
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        String dealerId = "";
        //判断是否为车厂  PartWareHouseDao
        PartWareHouseDao dao =  PartWareHouseDao.getInstance();
        List<OrgBean> beanList = dao.getOrgInfo(loginUser);
        if (null != beanList || beanList.size() >= 0) {
            if ((beanList.get(0).getOrgId() + "").equals(Constant.OEM_ACTIVITIES)) {
                dealerId = Constant.OEM_ACTIVITIES;
            } else {
                dealerId = loginUser.getDealerId();
            }
        }
        String soCode = CommonUtils.checkNull(request.getParamValue("soCode"));//销售单号
        String dealerName = CommonUtils.checkNull(request.getParamValue("dealerName"));//订货单位
        String dealerCode = CommonUtils.checkNull(request.getParamValue("dealerCode"));//销售单位
        String whId = CommonUtils.checkNull(request.getParamValue("whId"));//出库仓库
        String SstartDate = CommonUtils.checkNull(request.getParamValue("SstartDate"));//销售日期S
        String SendDate = CommonUtils.checkNull(request.getParamValue("SendDate"));//销售日期E
        String orderType = CommonUtils.checkNull(request.getParamValue("orderType"));//订单类型
        String state = CommonUtils.checkNull(request.getParamValue("state"));//状态
        String column = CommonUtils.checkNull(request.getParamValue("column"));//字段
        String transType = CommonUtils.checkNull(request.getParamValue("TRANS_TYPE"));//发运方式
        String ifPick = CommonUtils.checkNull(request.getParamValue("IF_PICK"));//是否捡货
        String pickNum = CommonUtils.checkNull(request.getParamValue("pickNum"));//合并单号
        String issps = CommonUtils.checkNull(request.getParamValue("IS_SPS"));//
//        String isHava = CommonUtils.checkNull(request.getParamValue("IS_HAVA"));//是否发运单 是否有整车发运单
        String orderCode = CommonUtils.checkNull(request.getParamValue("orderCode"));//

        sql.append("SELECT A.*,\n");
        sql.append("       (SELECT NVL(T.WH_NAME, '') AS WH_NAME\n");
        sql.append("          FROM TT_PART_WAREHOUSE_DEFINE T\n");
        sql.append("         WHERE T.WH_ID = A.WH_ID) AS WH_NAME,\n");
        sql.append("       (SELECT NAME FROM TC_USER B WHERE A.CREATE_BY = B.USER_ID) AS CREATE_BY_NAME,\n");
        sql.append("       (SELECT NVL(MAX(TO_NUMBER(PKG_NO)), '0')\n");
        sql.append("          FROM TT_PART_PKG_DTL B\n");
        sql.append("         WHERE A.SO_ID = B.SO_ID) AS PKGEDNUM,\n");
        sql.append("       (SELECT FIX_NAME\n");
        sql.append("          FROM TT_PART_FIXCODE_DEFINE E\n");
        sql.append("         WHERE FIX_GOUPTYPE = '92251004'\n");
        sql.append("           AND E.FIX_VALUE = A.TRANS_TYPE) AS TRANS_TYPE_NAME,\n");
        sql.append("       RANK() OVER(PARTITION BY PICK_ORDER_ID ORDER BY FCAUDIT_DATE ASC) AS FD,\n");
        sql.append("       TO_CHAR(A.AMOUNT, 'FM999,999,990.00') AS CONVERSEAMOUNT,\n");
        sql.append("       TD.DEALER_CODE DEALER_CODE2,\n");
        sql.append("       TD.DEALER_NAME DEALER_NAME2\n");
        //sql.append("       (SELECT decode(COUNT(1),1,'有','无')\n");
        //sql.append("          FROM VW_PART_VS_DLR_ORDER VDO\n");
        //sql.append("         WHERE VDO.DEALER_ID = A.DEALER_ID\n");
        //sql.append("\t\t\t\t) VSO_FLAG --是否有提车单\n");
        sql.append("  FROM TT_PART_SO_MAIN A, TM_DEALER TD\n");
        sql.append(" WHERE 1 = 1");
        sql.append("AND A.DEALER_ID = TD.DEALER_ID");
        if (!"".equals(SstartDate)) {
            sql.append(" and A.CREATE_DATE>= to_date('").append(SstartDate).append(" 00:00:00','YYYY/MM/dd HH24:mi:ss')");
        }
        if (!"".equals(SendDate)) {
            sql.append(" and A.CREATE_DATE<= to_date('").append(SendDate).append(" 23:59:59','YYYY/MM/dd HH24:mi:ss')");
        }
        if (!"".equals(soCode)) {
            sql.append(" and A.SO_CODE like  '%").append(soCode).append("%'");
        }
        if (!"".equals(dealerName)) {
            sql.append(" and A.DEALER_NAME like '%").append(dealerName).append("%'");
        }
        if (!"".equals(dealerCode)) {
            sql.append(" and upper(TD.DEALER_CODE) like  upper('%").append(dealerCode).append("%')");
        }
        if (!"".equals(whId)) {
            sql.append(" and A.WH_ID ='").append(whId).append("'");
        }
        if (!"".equals(orderType)) {
            sql.append(" and A.ORDER_TYPE ='").append(orderType).append("'");
        }
        if (!"".equals(transType)) {
            sql.append(" and a.trans_type ='").append(transType).append("'");
        }
        if (!"".equals(state)) {
            sql.append(" and A.STATE ='").append(state).append("'");
        }
        if (!"".equals(pickNum)) {
            sql.append(" and pick_order_id  like '%").append(pickNum).append("%'");
        }
        if (!"".equals(ifPick)) {
            if (dealerId.equals(Constant.OEM_ACTIVITIES)) { //主机厂
                if (ifPick.equals(Constant.IF_TYPE_YES.toString())) { //enable
                    sql.append("  AND A.pick_order_Id IS NOT NULL");
                } else {
                    //sql.append("  AND A.pick_order_Id IS  NULL AND A.STATE =").append(Constant.CAR_FACTORY_ORDER_CHECK_STATE_05);
                    sql.append("  AND A.pick_order_Id IS  NULL ");
                }

            } else {
                if (ifPick.equals(Constant.IF_TYPE_YES.toString())) { //enable
                    //sql.append(" AND A.STATE <> ").append(Constant.CAR_FACTORY_ORDER_CHECK_STATE_02);
                    sql.append("  AND A.pick_order_Id IS NOT NULL");
                } else {
                    //sql.append(" AND A.STATE = ").append(Constant.CAR_FACTORY_ORDER_CHECK_STATE_02);
                    sql.append("  AND A.pick_order_Id IS  NULL ");
                }
            }
        }
        sql.append(" and STATE in('").append(Constant.CAR_FACTORY_ORDER_CHECK_STATE_05);
        sql.append("','");
        //sql.append(Constant.CAR_FACTORY_ORDER_CHECK_STATE_02);
        //sql.append("','");
        sql.append(Constant.CAR_FACTORY_PKG_STATE_01);
        sql.append("','");
        sql.append(Constant.CAR_FACTORY_PKG_STATE_02);
        sql.append("')");
        sql.append(" and A.seller_id='").append(CommonUtils.checkNull(dealerId)).append("'");
        if (!"".equals(issps)) {
            if (issps.equals(Constant.PART_BASE_FLAG_YES + "")) {
                sql.append("AND EXISTS (SELECT 1\n");
                sql.append("        FROM TM_DEALER D\n");
                sql.append("       WHERE D.DEALER_ID = A.DEALER_ID\n");
                sql.append("         AND D.DEALER_ID = 2013090976824237)\n");
            } else {
                sql.append("AND EXISTS (SELECT 1\n");
                sql.append("        FROM TM_DEALER D\n");
                sql.append("       WHERE D.DEALER_ID = A.DEALER_ID\n");
                sql.append("         AND D.DEALER_ID != 2013090976824237)\n");
            }
        }
//        if (!"".equals(isHava) && null != isHava) {
//            if (isHava.equals(Constant.IF_TYPE_YES + "")) {
//                sql.append("AND  EXISTS (SELECT 1\n");
//                sql.append("          FROM VW_PART_VS_DLR_ORDER VDO\n");
//                sql.append("         WHERE VDO.DEALER_ID = A.DEALER_ID)");
//            } else {
//                sql.append("AND NOT EXISTS (SELECT 1\n");
//                sql.append("         FROM VW_PART_VS_DLR_ORDER VDO\n");
//                sql.append("        WHERE VDO.DEALER_ID = A.DEALER_ID)");
//            }
//        }
        if (!"".equals(orderCode) && null != orderCode) {
            sql.append(" and A.ORDER_CODE like '%").append(orderCode).append("%'");
        }
        //直发、广宣品订单不在DMS出库，所以不需要拣货出库 add by yuan 140610   
        sql.append(" and A.ORDER_TYPE NOT IN (").append(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_04)
//        .append(",")
//        .append(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_07).append(",")
//        .append(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_12)
        .append(")");
        sql.append(" order by A.create_date DESC");

        PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
        return ps;
    }

    /**
     * 获取仓位
     *
     * @param soId
     * @return
     */
    public List<Map<String, Object>> queryPartLocation(String soId) {
        StringBuffer sql = new StringBuffer();
    /*	sql.append(" select LOC_NAME,LOC_ID from TT_PART_LOACTION_DEFINE where STATE='");
        sql.append(Constant.STATUS_ENABLE).append("'");
		sql.append(" and PART_ID in (");
		sql.append(" select part_id from TT_PART_SO_DTL sd ");
		sql.append(" where sd.wh  SO_ID='").append(soId).append("'");
		sql.append(")");*/

        sql.append("SELECT LOC_NAME, LOC_ID,MIN_PKG\n");
        sql.append("  FROM TT_PART_LOACTION_DEFINE TLD, TT_PART_SO_DTL SD, TT_PART_SO_MAIN M\n");
        sql.append(" WHERE TLD.STATE = ").append(Constant.STATUS_ENABLE);
        sql.append("   AND TLD.STATUS = 1\n");
        sql.append("   AND M.SO_ID = SD.SO_ID\n");
        sql.append("   AND M.WH_ID = TLD.WH_ID\n");
        sql.append("   AND SD.STATUS = 1\n");
        sql.append("   AND M.STATUS = 1\n");
        sql.append("   AND TLD.PART_ID = SD.PART_ID\n");
        sql.append("   AND M.SO_ID =").append(soId);
        List<Map<String, Object>> list = this.pageQuery(sql.toString(), null, this.getFunName());
        return list;
    }

    /**
     * 获取仓位
     *
     * @param soId
     * @return
     */
    public Map<String, Object> queryPartLocationByPartId(String partId, String whId) {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        String orgId = "";
        PartWareHouseDao partWareHouseDao =  PartWareHouseDao.getInstance();
        List<OrgBean> beanList = partWareHouseDao.getOrgInfo(loginUser);
        if (null != beanList || beanList.size() >= 0) {
            orgId = beanList.get(0).getOrgId() + "";

        }
        StringBuffer sql = new StringBuffer();
        sql.append(" select LOC_NAME,LOC_ID,MIN_PKG from TT_PART_LOACTION_DEFINE where STATE='");
        sql.append(Constant.STATUS_ENABLE).append("'");
        sql.append(" and PART_ID ='");
        sql.append(partId);
        sql.append("'");
        sql.append(" and wh_Id='").append(whId).append("'");
        sql.append(" and org_id='").append(orgId).append("'");
        List<Map<String, Object>> list = this.pageQuery(sql.toString(), null, this.getFunName());
        if (list == null || list.size() <= 0) {
            return new HashMap();
        }
        return list.get(0);
    }

    /**
     * @param : @param soId
     * @param : @param
     * @param : @param
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-4-16
     * @Title :
     * @Description: 查询详细
     */
    public List<Map<String, Object>> queryPartInfo(String soId) {
        StringBuffer sql = new StringBuffer();
        sql.append(" select * from Tt_Part_So_dtl ");
        sql.append(" where SO_ID='").append(soId).append("'");
        List<Map<String, Object>> list = this.pageQuery(sql.toString(), null, this.getFunName());
        return list;
    }

    /**
     * @param : @param soId
     * @param : @param
     * @param : @param
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-4-16
     * @Title :
     * @Description: 查询装箱
     */
    public List<Map<String, Object>> queryPkg(String soId, String partId) {
        StringBuffer sql = new StringBuffer();
        sql.append(" select nvl(COUNT(pkg_no),'0') as pkg_no,nvl(sum(pkg_qty),'0') as pkg_qty from TT_PART_PKG_DTL ");
        sql.append(" where 1=1 ");
        sql.append(" and SO_ID='").append(soId).append("'");
        sql.append(" and PART_ID='").append(partId).append("'");
        sql.append(" order by pkg_no desc ");
        List<Map<String, Object>> list = this.pageQuery(sql.toString(), null, this.getFunName());
        return list;
    }

    /**
     * @param : @param slineId
     * @param : @param
     * @param : @param
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-4-16
     * @Title :
     * @Description: 查询详细
     */
    public List<Map<String, Object>> queryDetail(String slineId) {
        StringBuffer sql = new StringBuffer();
        sql.append(" select * from TT_PART_SO_DTL ");
        sql.append(" where 1=1 ");
        sql.append(" and SLINE_ID='").append(slineId).append("'");
        List<Map<String, Object>> list = this.pageQuery(sql.toString(), null, this.getFunName());
        return list;
    }

    /**
     * @param : @param slineId
     * @param : @param
     * @param : @param
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-4-16
     * @Title :
     * @Description: 查询Bo
     */
    public String queryBo(String partId, String soId) {
        StringBuffer sql = new StringBuffer();
        sql.append(" select BO_QTY from TT_PART_BO_DTL ");
        sql.append(" where 1=1 ");
        sql.append(" and PART_ID='").append(partId).append("'");
        sql.append(" and BO_ID in (select BO_ID from TT_PART_BO_MAIN where BO_TYPE='2' and SO_ID = '").append(soId).append("')");
        List<Map<String, Object>> list = this.pageQuery(sql.toString(), null, this.getFunName());
        if (null == list || list.size() <= 0 || null == list.get(0)) {
            return "0";
        }
        return list.get(0).get("BO_QTY").toString();
    }

    /**
     * @param : @param slineId
     * @param : @param
     * @param : @param
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-4-16
     * @Title :
     * @Description: 查询详细
     */
    public Map<String, Object> querySoDtl(String slineId) {
        StringBuffer sql = new StringBuffer();
        sql.append(" select * from TT_PART_SO_DTL ");
        sql.append(" where 1=1 ");
        sql.append(" and SLINE_ID='").append(slineId).append("'");
        List<Map<String, Object>> list = this.pageQuery(sql.toString(), null, this.getFunName());
        if (null == list || list.size() <= 0 || null == list.get(0)) {
            return null;
        }
        return list.get(0);
    }

    /**
     * @param : @param slineId
     * @param : @param
     * @param : @param
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-4-16
     * @Title :
     * @Description: 获取装箱
     */
    public String getPkg(String soId) {
        String pkgedNo = "";
        StringBuffer sql = new StringBuffer();
        sql.append(" select * from tt_part_pkg_dtl where so_id='");
        sql.append(soId);
        sql.append("'");
        List<Map<String, Object>> list = this.pageQuery(sql.toString(), null, this.getFunName());
        for (Map<String, Object> map : list) {
            pkgedNo += CommonUtils.checkNull(map.get("PKG_NO")) + ",";
        }
        if (!"".equals(pkgedNo)) {
            pkgedNo = pkgedNo.substring(0, pkgedNo.length() - 1);
        } else {
            pkgedNo = "<font color='red'>未装箱</font>";
        }
        return pkgedNo;
    }

    /**
     * 拣货明细查询
     * @param soId 销售单ID
     * @param whId 仓库ID
     * @return
     */
    public List<Map<String, Object>> getDtlLoc(String soId, String whId) {
        StringBuffer sql = new StringBuffer();

        sql.append("SELECT PD.PART_ID,\n");
        sql.append("       PD.PART_CODE,\n");
        sql.append("       PD.PART_OLDCODE,\n");
        sql.append("       SUBSTR(PD.PART_CNAME, 0, 20) PART_CNAME,\n");
        sql.append("       PD.UNIT,\n");
        sql.append("       BD.BATCH_NO,\n");//批次号
        sql.append("       LD.LOC_CODE LOC,\n");//货位
        sql.append("       SUM(BD.BOOKED_QTY) BOOKED_QTY,\n");//某批次某货位的拣货数量
        sql.append("       SUM(BD.ITEM_QTY) ITEM_QTY,\n");//库存数量
        sql.append("       SUM(BD.SALES_QTY) AS SALES_QTY,\n");//订货数量
        sql.append("       (SELECT T.ITEM_QTY\n");
        sql.append("          FROM VW_PART_STOCK T\n");
        sql.append("         WHERE T.WH_ID = LD.WH_ID\n");
        sql.append("           AND T.PART_ID = LD.PART_ID\n");
        sql.append("           AND T.LOC_ID = BD.LOC_ID\n");
        sql.append("           AND T.BATCH_NO = BD.BATCH_NO\n");
        sql.append("           AND T.STATE = 10011001\n");
        sql.append("           AND T.STATUS = 1) AS NORMAL_QTY_NOW,\n");
        sql.append("       DR.REMARK\n");
        sql.append("  FROM TT_PART_BOOK_DTL        BD,\n");
        sql.append("       TT_PART_DEFINE          PD,\n");
        sql.append("       TT_PART_LOACTION_DEFINE LD,\n");
        sql.append("       VW_PART_SO_DTL_REMARK   DR\n");
        sql.append(" WHERE 1 = 1\n");
        sql.append("   AND BD.LOC_ID = LD.LOC_ID\n");
        sql.append("   AND BD.PART_ID = PD.PART_ID\n");
        sql.append("   AND BD.ORDER_ID = DR.SO_ID(+)\n");
        sql.append("   AND BD.PART_ID = DR.PART_ID(+)");
        sql.append("   AND BD.ORDER_ID IN (" + soId + ")\n");
        sql.append(" GROUP BY PD.PART_ID,\n");
        sql.append("          PD.PART_CODE,\n");
        sql.append("          PD.PART_OLDCODE,\n");
        sql.append("          PD.PART_CNAME,\n");
        sql.append("          PD.UNIT,\n");
        sql.append("          LD.WH_ID,\n");
        sql.append("          LD.PART_ID,\n");
        sql.append("          BD.LOC_ID,\n");
        sql.append("          LD.LOC_CODE,\n");
        sql.append("          BD.BATCH_NO,\n");
        sql.append("          DR.REMARK\n");
        sql.append(" ORDER BY LD.LOC_CODE\n");

       /* sql.append(" UNION ALL\n");
        sql.append("SELECT 0 PART_ID,\n");
        sql.append("              '' PART_CODE,\n");
        sql.append("              '' PART_CNAME,\n");
        sql.append("              '' PART_OLDCODE,\n");
        sql.append("              NULL SALES_QTY,\n");
        sql.append("              SUM(A.FREIGHT)  BUY_AMOUNT,\n");
        sql.append("              NULL BUY_PRICE,\n");
        sql.append("              NULL UNIT,\n");
        sql.append("              NULL NORMAL_QTY_NOW,\n");
        sql.append("              '运费' LOC\n");
        sql.append("          FROM TT_PART_SO_MAIN A\n");
        sql.append("         WHERE 1 = 1\n");
        sql.append("        and a.SO_ID in (").append(soId).append(") ");*/
        /*sql.append(" ) ");*/
        /*sql.append(" group by  part_id,part_code,part_cname, part_oldcode,buy_price,unit,NORMAL_QTY_NOW,loc ");
        sql.append(" ORDER BY loc ");*/
        List<Map<String, Object>> list = this.pageQuery(sql.toString(), null, this.getFunName());
        return list;
    }

    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> getDtlSuiche(String pick_order_id, String ass_no) {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT D.PKG_NO,\n");
        sql.append(" D.PART_OLDCODE,\n");
        sql.append(" D.PART_CNAME,\n");
        sql.append(" D.PART_CODE,\n");
        sql.append(" D.REMARK,\n");
        sql.append(" TO_CHAR(D.PKG_QTY, 'fm999990') AS PKG_QTY,\n");
        sql.append(" TO_CHAR(S.BUY_PRICE, 'fm999990.00') AS BUY_PRICE,\n");
        sql.append(" TO_CHAR((NVL(D.PKG_QTY, 0) * S.BUY_PRICE), 'fm999999990.00') AS PKG_AMOUNT\n");
        sql.append(" FROM TT_PART_PKG_DTL D,\n");
        sql.append(" (SELECT DISTINCT SM.PICK_ORDER_ID, SD.PART_ID, SD.BUY_PRICE\n");
        sql.append("	FROM TT_PART_SO_DTL SD, TT_PART_SO_MAIN SM\n");
        sql.append(" 	WHERE SD.SO_ID = SM.SO_ID) S\n");
        sql.append(" WHERE D.PART_ID = S.PART_ID\n");
        sql.append(" AND D.PICK_ORDER_ID = S.PICK_ORDER_ID\n");
        sql.append(" AND D.PICK_ORDER_ID = '" + pick_order_id + "' --拣货单号\n");
        sql.append(" AND EXISTS (SELECT 1\n");
        sql.append(" FROM TT_PART_PKG_BOX_DTL B\n");
        sql.append(" WHERE B.PICK_ORDER_ID = D.PICK_ORDER_ID\n");
        sql.append(" AND D.PKG_NO = B.PKG_NO\n");
        sql.append(" AND B.ASS_NO = '" + ass_no + "' --整车发运计划单号\n");
        sql.append(" AND B.STATE = 92451005)\n");
        sql.append(" ORDER BY D.PKG_NO\n");

        List<Map<String, Object>> list = this.pageQuery(sql.toString(), null, this.getFunName());
        return list;
    }


    public List<Map<String, Object>> getGiftLoc(String soIds) {
        StringBuffer sql = new StringBuffer();
        sql.append(" select ");
        sql.append(" loc_name as loc,part_oldcode,part_cname,tpsg.gift_qty as sales_qty,tpd.unit,part_code,'0' as buy_price,'0' as buy_amount ");
        sql.append(" from tt_part_so_gift tpsg ");
        sql.append(" join tt_part_so_main tpsm on tpsm.so_id=tpsg.so_id ");
        sql.append(" join tt_part_define tpd on tpsg.part_id=tpd.part_id ");
        sql.append(" left join tt_part_loaction_define tpld on tpld.wh_id=tpsm.wh_id and tpsg.part_id=tpld.part_id ");
        sql.append(" where 1=1 and tpsg.so_id in (").append(soIds).append(")");
        List<Map<String, Object>> list = this.pageQuery(sql.toString(), null, this.getFunName());
        return list;
    }

    public String getPkgNum(String pickOrderId, String partId) {
        StringBuffer sql = new StringBuffer();
        sql.append("  select nvl(SUM(PKG_QTY),'0') SUM_PKG_QTY from tt_part_pkg_dtl where pick_order_id ='").append(pickOrderId).append("'");
        if (null != partId) {
            sql.append(" and part_id='").append(partId).append("'");
        }
        List<Map<String, Object>> list = this.pageQuery(sql.toString(), null, this.getFunName());
        if (null == list || list.size() <= 0 || null == list.get(0) || null == list.get(0).get("SUM_PKG_QTY")) {
            return "0";
        }
        return list.get(0).get("SUM_PKG_QTY").toString();
    }

    public String getMaxPkgNo(String pickOrderId) {
        StringBuffer sql = new StringBuffer();
        sql.append(" select nvl(max(to_number(pkg_no)),'0') as maxNo from tt_part_pkg_dtl ");
        sql.append(" where pick_order_id='").append(pickOrderId).append("'");
        List<Map<String, Object>> list = this.pageQuery(sql.toString(), null, this.getFunName());
        if (null == list || list.size() <= 0 || null == list.get(0) || null == list.get(0).get("MAXNO")) {
            return "0";
        }
        return list.get(0).get("MAXNO").toString();
    }

    public String getMaxPkgMap(String soId) {
        StringBuffer sql = new StringBuffer();
        sql.append(" select nvl(max(to_number(pkg_no)),'0') as maxNo,part_id from tt_part_pkg_dtl ");
        sql.append(" where so_id='").append(soId).append("'");
        List<Map<String, Object>> list = this.pageQuery(sql.toString(), null, this.getFunName());
        if (null == list || list.size() <= 0 || null == list.get(0) || null == list.get(0).get("MAXNO")) {
            return "0";
        }
        return list.get(0).get("MAXNO").toString();
    }

    public List<Map<String, Object>> getSoMainGroup(String soIds) {
        StringBuffer sql = new StringBuffer();
        sql.append(" select dealer_id,wh_id,TRANS_TYPE,ORDER_TYPE,ADDR_ID from tt_part_so_main where so_id in ( ");
        sql.append(soIds);
        sql.append(") group by dealer_id,wh_id,TRANS_TYPE,CONSIGNEES_ID,ORDER_TYPE,ADDR_ID");
        List<Map<String, Object>> list = this.pageQuery(sql.toString(), null, this.getFunName());
        if (null == list || list.size() <= 0) {
            return new ArrayList<Map<String, Object>>();
        }
        return list;
    }

    public List<Map<String, Object>> getSoMains(String soIds) {
        StringBuffer sql = new StringBuffer();
        sql.append(" select dealer_id,wh_id,TRANS_TYPE from tt_part_so_main where so_id in ( ");
        sql.append(soIds);
        sql.append(") ");
        List<Map<String, Object>> list = this.pageQuery(sql.toString(), null, this.getFunName());
        if (null == list || list.size() <= 0) {
            return new ArrayList<Map<String, Object>>();
        }
        return list;
    }

    public String getTranTypeName(String id) {
        StringBuffer sql = new StringBuffer();
        //.FIXCODE_TYPE_04
        sql.append(" select FIX_NAME from tt_part_fixcode_define ");
        sql.append(" where 1=1 ");
        sql.append(" and FIX_GOUPTYPE='").append(Constant.FIXCODE_TYPE_04).append("'");
        sql.append(" and FIX_VALUE='").append(id).append("'");
        List<Map<String, Object>> list = this.pageQuery(sql.toString(), null, this.getFunName());
        if (null == list || list.size() <= 0 || null == list.get(0) || null == list.get(0).get("FIX_NAME")) {
            return "";
        }
        return list.get(0).get("FIX_NAME").toString();
    }

    public String getWhName(String id) {
        StringBuffer sql = new StringBuffer();
        sql.append(" select WH_NAME from tt_part_warehouse_define ");
        sql.append(" where wh_id='").append(id).append("'");
        List<Map<String, Object>> list = this.pageQuery(sql.toString(), null, this.getFunName());
        if (null == list || list.size() <= 0 || null == list.get(0) || null == list.get(0).get("WH_NAME")) {
            return "";
        }
        return list.get(0).get("WH_NAME").toString();
    }


    /**
     * 批量更新合并提货单
     *
     * @param soIds       销售单ID
     * @param dealerId    服务商ID
     * @param whId        仓库ID
     * @param pickOrderId 拣货单ID
     * @param transType   发运方式
     * @param createBy    合并人ID
     * @param orderType   订单类型
     */
    public void savePickOrderId(String soIds, String dealerId, String whId, String pickOrderId, String transType, String createBy, String orderType, String addrId) {
        StringBuffer sql = new StringBuffer();
        sql.append(" update tt_part_so_main set PICK_ORDER_ID='").append(pickOrderId).append("',PICK_ORDER_CREATE_DATE=sysdate ,PICK_ORDER_CREATE_BY='").append(createBy).append("'");
        sql.append(" where PICK_ORDER_ID is null and dealer_id='").append(dealerId).append("' and TRANS_TYPE='").append(transType).append("' and so_id in (").append(soIds).
                append(")").append(" and order_type=").append(orderType).append(" and wh_Id=").append(whId).append(" and addr_id=").append(addrId);

        this.update(sql.toString(), null);
    }

    /**
     * 更新预占明细中的拣货单ID
     *
     * @param soIds       销售单IDs
     * @param pickOrderId 拣货单ID
     */
    public void updateBookTtlPickOrderId(String soIds, String dealerId, String whId, String pickOrderId, String transType, String createBy, String orderType, String addrId) {
        StringBuffer sql = new StringBuffer();
       /* sql.append(" update tt_part_book_dtl bd set bd.pickorder_id=").append(pickOrderId);
        sql.append(" where  bd.order_id in (").append(soIds).append(")");*/

        sql.append("UPDATE TT_PART_BOOK_DTL BD\n");
        sql.append("   SET BD.PICKORDER_ID =" + pickOrderId + ", BD.UPDATE_DATE = SYSDATE, BD.UPDATE_BY =" + createBy + "\n");
        sql.append(" WHERE EXISTS (SELECT 1\n");
        sql.append("          FROM TT_PART_SO_MAIN SM\n");
        sql.append("         WHERE SM.SO_ID = BD.ORDER_ID\n");
        sql.append("           AND SM.WH_ID = BD.WH_ID\n");
        sql.append("           AND SM.WH_ID = " + whId + "\n");
        sql.append("           AND SM.SO_ID IN (" + soIds + ")\n");
        sql.append("           AND SM.ORDER_TYPE =" + orderType + "\n");
        sql.append("           AND SM.TRANS_TYPE ='"+ transType +"'\n");
        sql.append("           AND SM.addr_id =" + addrId + "\n");
        sql.append("           AND SM.DEALER_ID =" + dealerId + ")");

        this.update(sql.toString(), null);
    }

    public List<Map<String, Object>> getSoMain(String pickOrderId) {
        StringBuffer sql = new StringBuffer();
        sql.append(" select sm.*, to_char(SM.CREATE_DATE, 'yyyy.MM.dd') as CREATE_DATE_FM, u.name, c.code_desc from tt_part_so_main sm, tc_user u, tc_code c where pick_order_id='").append(pickOrderId).append("' and sm.create_by = u.user_id and sm.order_type = c.code_id ");
        return this.pageQuery(sql.toString(), null, this.getFunName());
    }

    public List<Map<String, Object>> getSoMain2(String pickOrderId) {
        StringBuffer sql = new StringBuffer();
        sql.append(" select * from tt_part_so_main where So_ID='").append(pickOrderId).append("'");
        return this.pageQuery(sql.toString(), null, this.getFunName());
    }

    /**
     * 获取配件发运单经销商信息
     *
     * @param pickOrderId
     * @return
     */
    public List<Map<String, Object>> getTransMain(String sellerId, String transId) {
        StringBuffer sql = new StringBuffer();

        sql.append("SELECT M.PICK_ORDER_ID,\n");
        sql.append("       S.TRANS_TYPE ORDER_TRANS_TYPE,\n");
        sql.append("       TD.DEALER_CODE,\n");
        sql.append("       TD.DEALER_NAME,\n");
        sql.append("       TO_CHAR(TRUNC(S.CREATE_DATE), 'yyyy.MM.dd') TRANS_DATE,\n");
        sql.append("       S.TRANS_CODE LOGISTICS_NO,\n");
        sql.append("       S.CREATE_DATE,\n");
        sql.append("       W.WH_NAME,\n");
        sql.append("       m.addr,\n");
        sql.append("\t\t\t m.tel,\n");
        sql.append("\t\t\t m.receiver\n");
        sql.append("  FROM TT_PART_OUTSTOCK_MAIN    M,\n");
        sql.append("       TT_PART_TRANS            S,\n");
        sql.append("       TM_DEALER                TD,\n");
        sql.append("       TT_PART_WAREHOUSE_DEFINE W\n");
        sql.append(" WHERE M.OUT_ID = S.OUT_ID\n");
        sql.append("   AND M.DEALER_ID = TD.DEALER_ID\n");
        sql.append("   AND W.WH_ID = M.WH_ID\n");

        if (!"".equals(sellerId)) {
            sql.append(" AND M.SELLER_ID = '").append(sellerId).append("' ");
        }
        if (!"".equals(transId) && null != transId) {
            sql.append("   AND S.TRANS_ID =").append(transId);
        }
        return this.pageQuery(sql.toString(), null, this.getFunName());
    }

    public List<Map<String, Object>> getTransPlanMain(String sellerId, String trplanId) {
        StringBuffer sql = new StringBuffer();

        sql.append("WITH TRPLAN_PICK_ID AS\n");
        sql.append(" (SELECT TP.TRPLAN_ID,\n");
        sql.append("         TP.TRPLAN_CODE,\n");
        sql.append("         SM.WH_ID,\n");
        sql.append("         SM.DEALER_ID,\n");
        sql.append("         ZA_CONCAT(DISTINCT PD.PICK_ORDER_ID) PICK_ORDER_ID,\n");
        sql.append("         ZA_CONCAT(DISTINCT SM.ORDER_CODE) ORDER_CODE,\n");
        sql.append("         ZA_CONCAT(DISTINCT TC.CODE_DESC) ORDER_TYPE\n");
        sql.append("    FROM TT_PART_TRANS_PLAN_DTL TPD,\n");
        sql.append("         TT_PART_TRANS_PLAN     TP,\n");
        sql.append("         TT_PART_PKG_BOX_DTL    PBD,\n");
        sql.append("         TT_PART_PKG_DTL        PD,\n");
        sql.append("         TT_PART_SO_MAIN        SM,\n");
        sql.append("         TC_CODE                TC\n");
        sql.append("   WHERE TPD.TRPLAN_ID = TP.TRPLAN_ID\n");
        sql.append("     AND TPD.PICK_ORDER_ID = PBD.PICK_ORDER_ID\n");
        sql.append("     AND TPD.PKG_NO = PBD.PKG_NO\n");
        sql.append("     AND PBD.PICK_ORDER_ID = PD.PICK_ORDER_ID\n");
        sql.append("     AND PBD.PICK_ORDER_ID = SM.PICK_ORDER_ID\n");
        sql.append("     AND SM.ORDER_TYPE = TC.CODE_ID\n");
        sql.append("     AND PBD.PKG_NO = PD.PKG_NO\n");
        sql.append("   GROUP BY TP.TRPLAN_ID, TP.TRPLAN_CODE, SM.WH_ID, SM.DEALER_ID)\n");
        sql.append("SELECT SM.PICK_ORDER_ID,\n");
//        sql.append("       S.TRANS_TYPE ORDER_TRANS_TYPE,\n");
//        sql.append("       S.TRANSPORT_ORG,\n");
        sql.append("   		(select TV_NAME from TT_TRANSPORT_VALUATION tv where tv.STATUS=10011001 and tv.tv_id=s.trans_type) ORDER_TRANS_TYPE,\n");
        sql.append("    	(select LOGI_FULL_NAME from TT_SALES_LOGI tv where tv.STATUS=10011001 and tv.LOGI_CODE=s.transport_org) TRANSPORT_ORG,\n");
        sql.append("       TD.DEALER_CODE,\n");
        sql.append("       TD.DEALER_NAME,\n");
        sql.append("       TO_CHAR(TRUNC(S.CREATE_DATE), 'yyyy.MM.dd') TRANS_DATE,\n");
        sql.append("       S.TRPLAN_CODE LOGISTICS_NO,\n");
        sql.append("       S.CREATE_DATE,\n");
        sql.append("       W.WH_NAME,\n");
        sql.append("       AD.ADDR,\n");
        sql.append("       AD.TEL,\n");
        sql.append("       SM.ORDER_CODE,\n");
        sql.append("       AD.LINKMAN RECEIVER,\n");
        sql.append("       SM.ORDER_TYPE\n");
        sql.append("  FROM TT_PART_TRANS_PLAN       S,\n");
        sql.append("       TM_DEALER                TD,\n");
        sql.append("       TT_PART_WAREHOUSE_DEFINE W,\n");
        sql.append("       TT_PART_ADDR_DEFINE      AD,\n");
        sql.append("       TRPLAN_PICK_ID           SM\n");
        sql.append(" WHERE S.TRPLAN_ID = SM.TRPLAN_ID\n");
        sql.append("   AND S.ADDR_ID = AD.ADDR_ID(+)\n");
        sql.append("   AND W.WH_ID = SM.WH_ID\n");
        sql.append("   AND TD.DEALER_ID = SM.DEALER_ID\n");
        sql.append("   AND S.TRPLAN_ID = "+trplanId+"");

        return this.pageQuery(sql.toString(), null, this.getFunName());
    }

    /**
     * 获取配件发运单基本信息
     *
     * @param pickOrderId
     * @return
     */
    public List<Map<String, Object>> getTransDtl(String pickOrderId, String transId) {
        StringBuffer sql = new StringBuffer();

        sql.append("SELECT PBD.*\n");
        sql.append("  FROM TT_PART_PKG_BOX_DTL PBD, TT_PART_TRANS PT\n");
        sql.append(" WHERE PBD.OUT_ID = PT.OUT_ID\n");
        sql.append("   AND PBD.PICK_ORDER_ID = '").append(pickOrderId).append("' ");
        sql.append("   AND PT.TRANS_ID = '").append(transId).append("' ");
        sql.append(" ORDER BY PBD.PKG_NO");

        return this.pageQuery(sql.toString(), null, this.getFunName());
    }

    public List<Map<String, Object>> getTransPlanDtl(String trplanId) {
        StringBuffer sql = new StringBuffer();

        sql.append("SELECT DISTINCT PBD.PKG_NO,\n");
        sql.append("                PBD.LENGTH,\n");
        sql.append("                PBD.WIDTH,\n");
        sql.append("                PBD.HEIGHT,\n");
        sql.append("                PBD.WEIGHT,\n");
        sql.append("                PBD.VOLUME,\n");
        sql.append("                PBD.EQ_WEIGHT,\n");
        sql.append("                PBD.CH_WEIGHT,\n");
        sql.append("                PBD.REMARK\n");
        sql.append("  FROM TT_PART_PKG_BOX_DTL PBD, TT_PART_TRANS_PLAN PT\n");
        sql.append(" WHERE PBD.TRPLAN_ID = PT.TRPLAN_ID\n");
        sql.append("   AND PBD.TRPLAN_ID = '" + trplanId + "'\n");
        sql.append(" ORDER BY PBD.PKG_NO");

        return this.pageQuery(sql.toString(), null, this.getFunName());
    }

    /**
     * 获取配件发运单基本信息统计
     *
     * @param pickOrderId
     * @return
     */
    public List<Map<String, Object>> getTransDtlAmount(String pickOrderId, String transId) {
        StringBuffer sql = new StringBuffer();

        sql.append("SELECT PBD.PICK_ORDER_ID,\n");
        sql.append("       COUNT(PBD.PICK_ORDER_ID) AS BOX_NUM,\n");
        sql.append("       SUM(PBD.VOLUME) AS VOLUME_SUM,\n");
        sql.append("       SUM(PBD.WEIGHT) AS WEIGHT_SUM,\n");
        sql.append("       SUM(PBD.EQ_WEIGHT) AS EQ_WEIGHT_SUM,\n");
        sql.append("       SUM(PBD.CH_WEIGHT) AS CH_WEIGHT_SUM\n");
        sql.append("  FROM TT_PART_PKG_BOX_DTL PBD, TT_PART_TRANS PT\n");
        sql.append(" WHERE PBD.OUT_ID = PT.OUT_ID\n");
        sql.append("   AND PBD.PICK_ORDER_ID = '" + pickOrderId + "'\n");
        sql.append("   AND PT.TRANS_ID = '").append(transId).append("'\n");
        sql.append(" GROUP BY PBD.PICK_ORDER_ID");

        return this.pageQuery(sql.toString(), null, this.getFunName());
    }

    public List<Map<String, Object>> getTransPlanDtlAmount(String trplanId) {
        StringBuffer sql = new StringBuffer();

        sql.append("SELECT COUNT(A.PKG_NO) AS BOX_NUM,\n");
        sql.append("       SUM(A.VOLUME) AS VOLUME_SUM,\n");
        sql.append("       SUM(A.WEIGHT) AS WEIGHT_SUM,\n");
        sql.append("       SUM(A.EQ_WEIGHT) AS EQ_WEIGHT_SUM,\n");
        sql.append("       SUM(A.CH_WEIGHT) CH_WEIGHT_SUM\n");
        sql.append("  FROM (SELECT DISTINCT PBD.PKG_NO,\n");
        sql.append("                        PBD.LENGTH,\n");
        sql.append("                        PBD.WIDTH,\n");
        sql.append("                        PBD.HEIGHT,\n");
        sql.append("                        PBD.WEIGHT,\n");
        sql.append("                        PBD.VOLUME,\n");
        sql.append("                        PBD.EQ_WEIGHT,\n");
        sql.append("                        PBD.CH_WEIGHT,\n");
        sql.append("                        PBD.REMARK\n");
        sql.append("          FROM TT_PART_PKG_BOX_DTL PBD, TT_PART_TRANS_PLAN PT\n");
        sql.append("         WHERE PBD.TRPLAN_ID = PT.TRPLAN_ID\n");
        sql.append("           AND PBD.TRPLAN_ID = '" + trplanId + "') A");

        return this.pageQuery(sql.toString(), null, this.getFunName());
    }

    /**
     * @param : @param pickOrderId
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-12-23
     * @Title : 装箱清单List
     */
    public List<Map<String, Object>> getPkgDetailList(String pickOrderId) {
        StringBuffer sql = new StringBuffer();

        sql.append("SELECT D.PKG_NO,\n");
        sql.append("       D.PART_OLDCODE,\n");
        sql.append("       D.PART_CNAME,\n");
        sql.append("       D.PART_CODE,\n");
        sql.append("       NULL REMARK,\n");
        sql.append("       SUM(D.PKG_QTY) AS PKG_QTY,\n");
        sql.append("       S.BUY_PRICE,\n");
        sql.append("       SUM(NVL(D.PKG_QTY, 0) * S.BUY_PRICE) AS PKG_AMOUNT\n");
        sql.append("  FROM TT_PART_PKG_DTL D,\n");
        sql.append("       (SELECT DISTINCT SM.PICK_ORDER_ID, SD.PART_ID, SD.BUY_PRICE\n");
        sql.append("          FROM TT_PART_SO_DTL SD, TT_PART_SO_MAIN SM\n");
        sql.append("         WHERE SD.SO_ID = SM.SO_ID) S\n");
        sql.append(" WHERE D.PART_ID = S.PART_ID\n");
        sql.append("   AND D.PICK_ORDER_ID = S.PICK_ORDER_ID\n");
        sql.append("   AND D.PICK_ORDER_ID = '" + pickOrderId + "'\n");
        sql.append("AND D.PKG_NO IS NOT NULL\n");
        sql.append(" GROUP BY D.PKG_NO, D.PART_OLDCODE, D.PART_CNAME, D.PART_CODE, S.BUY_PRICE\n");
        sql.append(" ORDER BY D.PKG_NO ASC\n");

        return this.pageQuery(sql.toString(), null, this.getFunName());
    }

    /**
     * @param : @param pickOrderId
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-12-23
     * @Title : 获取装箱总金额
     */
    public List<Map<String, Object>> getPkgDetailAmount(String pickOrderId) {
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT D.PICK_ORDER_ID, ");
        sql.append(" SUM(NVL(D.PKG_QTY,0) * S.BUY_PRICE) AS PKG_AMOUNTS ");
        sql.append(" FROM TT_PART_PKG_DTL D, ");
        sql.append(" (SELECT DISTINCT SM.PICK_ORDER_ID, SD.PART_ID, SD.BUY_PRICE ");
        sql.append(" FROM TT_PART_SO_DTL SD, TT_PART_SO_MAIN SM ");
        sql.append(" WHERE SD.SO_ID = SM.SO_ID) S ");
        sql.append(" WHERE D.PART_ID = S.PART_ID ");
        sql.append(" AND D.PICK_ORDER_ID = S.PICK_ORDER_ID ");
        sql.append(" AND D.PICK_ORDER_ID = '" + pickOrderId + "' ");
//		sql.append(" AND D.IS_GIFT = '"+ Constant.IF_TYPE_NO +"' ");//不考虑赠品
        sql.append(" GROUP BY D.PICK_ORDER_ID ");
        sql.append(" UNION ");
        sql.append(" SELECT '' PICK_ORDER_ID, ");
        sql.append(" SUM(NVL(SM.FREIGHT,0)) AS PKG_AMOUNTS ");
        sql.append(" FROM TT_PART_SO_MAIN SM ");
        sql.append(" WHERE SM.PICK_ORDER_ID = '" + pickOrderId + "' ");

        return this.pageQuery(sql.toString(), null, this.getFunName());
    }

    /**
     * @param pickOrderId
     */
    public void updatePickInfo(String pickOrderId) {
        StringBuffer sql = new StringBuffer();
        sql.append(" update tt_part_so_main set  PICK_ORDER_PRINT_NUM = nvl(PICK_ORDER_PRINT_NUM,1) + 1, PICK_ORDER_PRINT_DATE=sysdate where PICK_ORDER_ID='").append(pickOrderId).append("'");
        this.update(sql.toString(), null);
    }

    public void updateTt_part_pkg_box_dtl(String ass_no, String pickOrderId, String printBy) {
        StringBuffer sql = new StringBuffer();
        sql.append("UPDATE TT_PART_PKG_BOX_DTL B SET B.PRINT_DATE = SYSDATE,B.PRINT_BY = " + printBy + ", B.PRINT_NUM = NVL(B.PRINT_NUM,1)+1 ");
        sql.append(" WHERE B.PICK_ORDER_ID = " + pickOrderId + " AND B.ASS_NO = '" + ass_no + "'");
        this.update(sql.toString(), null);
    }

    /**
     * @param pickOrderId
     */
    public void updateTranInfo(String pickOrderId) {
        StringBuffer sql = new StringBuffer();
        sql.append(" update tt_part_so_main set TRANS_PRINT_NUM = nvl(TRANS_PRINT_NUM,1) + 1,TRANS_PRINT_DATE=sysdate where PICK_ORDER_ID='").append(pickOrderId).append("'");
        this.update(sql.toString(), null);
    }

    /**
     * 更新装箱单打印记录
     *
     * @param pickOrderId
     */
    public void updatePkgInfo(String pickOrderId) {
        StringBuffer sql = new StringBuffer();
        sql.append(" update tt_part_so_main set PKG_PRINT_NUM = nvl(PKG_PRINT_NUM,0) + 1,PKG_PRINT_DATE=sysdate where PICK_ORDER_ID='").append(pickOrderId).append("'");
        this.update(sql.toString(), null);
    }

    public Map<String, Object> getPkgDtl(String pkgId) {
        StringBuffer sql = new StringBuffer();
        sql.append("  select * from tt_part_pkg_dtl where pkg_id='").append(pkgId).append("'");
        List<Map<String, Object>> list = this.pageQuery(sql.toString(), null, this.getFunName());
        if (null == list || list.size() <= 0 || null == list.get(0)) {
            return new HashMap<String, Object>();
        }
        return list.get(0);
    }

    public List<Map<String, Object>> getPkgDtlGroup(String pkgIds) {
        StringBuffer sql = new StringBuffer();
        sql.append(" select sum(pkg_qty) as qty,part_id,part_code,part_cname,part_oldcode,SLINE_ID,count(pkg_id) as boxQty,pkg_no ");
        sql.append(" from tt_part_pkg_dtl where pkg_id in (").append(pkgIds).append(")");
        sql.append(" group by part_id,part_code,part_cname,part_oldcode,SLINE_ID,pkg_id,pkg_no order by pkg_no,part_id");
        return this.pageQuery(sql.toString(), null, this.getFunName());
    }

    /**
     * @param : @param slineId
     * @param : @param
     * @param : @param
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-4-16
     * @Title :
     * @Description: 查询详细
     */
    public List<Map<String, Object>> soDtl(String pickOrderId, String partId) {
        StringBuffer sql = new StringBuffer();
        sql.append(" select * from TT_PART_SO_DTL ");
        sql.append(" where 1=1 ");
        sql.append(" and so_id in （select so_id from tt_part_so_main where pick_order_id='").append(pickOrderId).append("'） and part_id='").append(partId).append("'");
        List<Map<String, Object>> list = this.pageQuery(sql.toString(), null, this.getFunName());
        return list;
    }


    public Long getSalesQty(String partId, String pickOrderId) {
        StringBuffer sql = new StringBuffer();
        sql.append(" select nvl(sum(sales_qty),'0') as salesQty ");
        sql.append(" from tt_part_so_dtl ");
        sql.append(" where 1=1 ");
        sql.append(" and so_id in （select so_id from tt_part_so_main where pick_order_id='").append(pickOrderId).append("'） and part_id='").append(partId).append("'");
        List<Map<String, Object>> list = this.pageQuery(sql.toString(), null, this.getFunName());
        if (list == null || list.size() <= 0 || list.get(0) == null) {
            return 0l;
        }
        return Long.valueOf(list.get(0).get("SALESQTY") == null ? "0" : CommonUtils.checkNull(list.get(0).get("SALESQTY")));
    }

    public String getPkgBoxNum(String id) {
        StringBuffer sql = new StringBuffer();
        sql.append(" select count(pkg_no) as pkg_no from tt_part_pkg_dtl where pick_order_id＝'").append(id).append("'");
        sql.append(" group by pkg_no ");
        List<Map<String, Object>> list = this.pageQuery(sql.toString(), null, this.getFunName());
        if (list == null || list.size() <= 0 || list.get(0) == null || list.get(0).get("PKG_NO") == null) {
            return "0";
        }
        return CommonUtils.checkNull(list.get(0).get("PKG_NO"));
    }

    public Map<String, Object> getPickInfo(String pickOrderId) {
        StringBuffer sql = new StringBuffer();
        sql.append(" select * from tt_part_pick_order_main where pick_order_id='").append(pickOrderId).append("'");
        List<Map<String, Object>> list = this.pageQuery(sql.toString(), null, this.getFunName());
        if (list == null || list.size() <= 0) {
            return new HashMap<String, Object>();
        }
        return list.get(0);
    }

    public List<Map<String, Object>> getPkgList(String pkgId) {
        StringBuffer sql = new StringBuffer();
        sql.append(" select distinct pkg_no from tt_part_pkg_dtl where pkg_id in (").append(pkgId).append(") order by to_number(pkg_no) ");
        List<Map<String, Object>> list = this.pageQuery(sql.toString(), null, this.getFunName());
        if (list == null) {
            return new ArrayList<Map<String, Object>>();
        }
        return list;
    }

    public String getPkgQty(String partId, String pickOrderId) {
        StringBuffer sql = new StringBuffer();
        sql.append(" select count(PKG_NO) as pkgQty from tt_part_pkg_dtl where part_id='").append(partId).append("'");
        sql.append(" and pick_Order_Id ='").append(pickOrderId).append("'");
        List<Map<String, Object>> list = this.pageQuery(sql.toString(), null, this.getFunName());
        if (null == list || list.size() <= 0 || null == list.get(0)) {
            return "0";
        }
        return list.get(0).get("PKGQTY").toString();
    }

    public List<Map<String, Object>> getGift(String pickOrderId, String dealerId, String whId) {
        StringBuffer sql = new StringBuffer();
        sql.append(" select ");
        sql.append(" tpsg.part_id ");
        sql.append(" ,nvl((SELECT SUM(pkg_qty) FROM tt_part_pkg_dtl tppd where tppd.pick_order_id='").append(pickOrderId).append("' and is_gift='").append(Constant.IF_TYPE_YES).append("' and tppd.part_id=tpsg.part_id),'0') AS pkgedQty");
        sql.append(" ,tpsg.gift_qty ");
        sql.append(" ,tpd.part_cname ");
        sql.append(" ,tpd.part_oldcode ");
        sql.append(" ,tpd.part_code ");
        sql.append(" ,tpd.unit ");
        sql.append(" ,nvl((select sum(ITEM_QTY) from vw_part_stock t where t.WH_ID='").append(whId).append("' and t.PART_ID=tpsg.PART_ID),'0') as NORMAL_QTY_NOW ");
        sql.append("       ,NVL(DECODE((SELECT D.PDEALER_TYPE\n");
        sql.append("                    FROM TM_DEALER D\n");
        sql.append("                   WHERE D.DEALER_ID = ").append(dealerId);
        sql.append("                     AND D.STATUS = 10011001),\n");
        sql.append("                  92101001,\n");
        sql.append("                  tpd.MIN_PACK1,\n");
        sql.append("                  tpd.MIN_PACK2),\n");
        sql.append("           0) AS MIN_PACKAGE\n");

        sql.append(" from tt_part_so_gift tpsg ");
        sql.append(" left join tt_part_define tpd on tpd.part_id=tpsg.part_id ");
        sql.append(" where 1=1 ");
        sql.append(" and tpsg.so_id in (select so_id from tt_part_so_main where pick_order_id ='").append(pickOrderId).append("')");

        return this.pageQuery(sql.toString(), null, this.getFunName());
    }

    public List<Map<String, Object>> getGiftPkgQty(String pickOrderId, String partId) {
        StringBuffer sql = new StringBuffer();
        sql.append(" select ");
        sql.append(" tpsg.gift_qty,tpsg.so_id ");
        sql.append(" from tt_part_so_gift tpsg ");
        sql.append(" where 1=1 ");
        sql.append(" and tpsg.so_id in (select so_id from tt_part_so_main where pick_order_id ='").append(pickOrderId).append("')");
        sql.append(" and part_id='").append(partId).append("'");
        return this.pageQuery(sql.toString(), null, this.getFunName());
    }

    public List<Map<String, Object>> getPartInfo(String partId) {
        StringBuffer sql = new StringBuffer();
        sql.append(" select ");
        sql.append(" * ");
        sql.append("  from tt_part_define where part_id='").append(partId).append("'");
        return this.pageQuery(sql.toString(), null, this.getFunName());
    }

    public List<Map<String, Object>> getPartInfo(String partId, String dealerId, String whId) {
        StringBuffer sql = new StringBuffer();
        sql.append(" select ");
        sql.append(" a.*,nvl((select sum(ITEM_QTY) from vw_part_stock t where t.WH_ID='").append(whId).append("' and t.PART_ID=a.PART_ID),'0') as NORMAL_QTY_NOW ");
        sql.append("       ,NVL(DECODE((SELECT D.PDEALER_TYPE\n");
        sql.append("                    FROM TM_DEALER D\n");
        sql.append("                   WHERE D.DEALER_ID = ").append(dealerId);
        sql.append("                     AND D.STATUS = 10011001),\n");
        sql.append("                  92101001,\n");
        sql.append("                  a.MIN_PACK1,\n");
        sql.append("                  a.MIN_PACK2),\n");
        sql.append("           0) AS MIN_PACKAGE\n");
        sql.append("  from tt_part_define a where part_id='").append(partId).append("'");
        return this.pageQuery(sql.toString(), null, this.getFunName());
    }

    public List<Map<String, Object>> getGiftInfo(String pickOrderId) {
        StringBuffer sql = new StringBuffer();
        sql.append(" select ");
        sql.append(" * ");
        sql.append(" from tt_part_so_gift tpsg ");
        sql.append(" left join tt_part_define tpd on tpd.part_id=tpsg.part_id ");
        sql.append(" where so_id in (");
        sql.append(" select so_id from tt_part_so_main where pick_order_id='").append(pickOrderId).append("')");
        return this.pageQuery(sql.toString(), null, this.getFunName());
    }

    public List<Map<String, Object>> getPkgedGift(String pickOrderId) {
        StringBuffer sql = new StringBuffer();
        sql.append(" select part_Id,sum(pkg_qty) as pkg_qty from tt_part_pkg_dtl where pick_order_id ='").append(pickOrderId).append("'");
        sql.append(" and is_gift='").append(Constant.IF_TYPE_YES).append("'");
        sql.append(" group by part_Id ");
        return this.pageQuery(sql.toString(), null, this.getFunName());
    }

    public List<Map<String, Object>> getSoGiftList(String partId, String pickOrderId) {
        StringBuffer sql = new StringBuffer();
        sql.append(" select ");
        sql.append(" so_id,gift_qty ");
        sql.append(" from tt_part_so_gift where part_id='").append(partId).append("' and so_id  in (select so_id from tt_part_so_main where pick_order_id ='").append(pickOrderId).append("')");
        return this.pageQuery(sql.toString(), null, this.getFunName());
    }

    public List<Map<String, Object>> getOutStockMain(String soId) {
        StringBuffer sql = new StringBuffer();
        sql.append(" select * from tt_part_outstock_main where so_id='").append(soId).append("'");
        return this.pageQuery(sql.toString(), null, this.getFunName());
    }

    public List<Map<String, Object>> soMainList(String pickOrderId) {
        StringBuffer sql = new StringBuffer();
        sql.append(" select * from tt_part_so_main where pick_order_id='").append(pickOrderId).append("'");
        return this.pageQuery(sql.toString(), null, this.getFunName());
    }

    public List<Map<String, Object>> getTransList(String soId) {
        StringBuffer sql = new StringBuffer();
        sql.append(" select * from tt_part_trans where so_id ='").append(soId).append("'");
        return this.pageQuery(sql.toString(), null, this.getFunName());
    }

    public List<Map<String, Object>> getStock(String partId, String dealerId) {
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT nvl(SUM(S.ITEM_QTY),'0') as stock_qty");
        sql.append("  FROM TT_PART_ITEM_STOCK S\n");
        sql.append("  WHERE S.PART_ID = '").append(partId).append("'");
        sql.append("  AND S.ORG_ID = '").append(dealerId).append("'");
        sql.append("  AND S.STATE = 1\n");
        sql.append("  AND S.STATUS = 1 \n");
        return this.pageQuery(sql.toString(), null, this.getFunName());
    }

    public Map<String, Object> queryBoxInfo(String pkgNo) throws Exception {
        try {
            StringBuffer sql = new StringBuffer("");

            sql.append(" SELECT T.LENGTH, \n");
            sql.append("        T.WIDTH, \n");
            sql.append("        T.PICK_ORDER_ID, \n");
            sql.append("        T.OUT_ID, \n");
            sql.append("        T.PKG_NO, \n");
            sql.append("        T.HEIGHT, \n");
            sql.append("        T.WEIGHT, \n");
            sql.append("        T.VOLUME, \n");
            sql.append("        T.EQ_WEIGHT, \n");
            sql.append("        T.CH_WEIGHT \n");
            sql.append("   FROM TT_PART_PKG_BOX_DTL T \n");
            sql.append("  WHERE T.PKG_NO = '").append(pkgNo).append("'");
            sql.append(" AND ROWNUM=1");

            return pageQueryMap(sql.toString(), null, getFunName());
        } catch (Exception e) {
            throw e;
        }
    }

    public Map<String, Object> isExist(String partId, String locId, String pkg_no, String pickOrderId) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            StringBuffer sql = new StringBuffer("");
            sql.append(" SELECT T.PKG_ID \n");
            sql.append("   FROM TT_PART_PKG_DTL T \n");
            sql.append("  WHERE T.PICK_ORDER_ID = '").append(pickOrderId).append("' \n");
            sql.append("    AND T.PKG_NO = '").append(pkg_no).append("' \n");
            sql.append("    AND T.PART_ID = ").append(partId);
            sql.append("    AND T.LOC_ID = ").append(locId);

            map = pageQueryMap(sql.toString(), null, getFunName());
        } catch (Exception e) {
            throw e;
        }
        return map;
    }

    public void updatePkgQty(Long pkgId, Long pkgQty) throws Exception {
        try {
            StringBuffer sql = new StringBuffer("");
            sql.append(" UPDATE TT_PART_PKG_DTL T \n");
            sql.append("    SET T.PKG_QTY = T.PKG_QTY + ").append(pkgQty);
            sql.append("  WHERE T.PKG_ID = ").append(pkgId);

            update(sql.toString(), null);
        } catch (Exception e) {
            throw e;
        }
    }

    public List<Map<String, Object>> queryUnPkgedPartInfo(String pickOrderId,
                                                          String whId) {

        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);

        StringBuffer sql = new StringBuffer();

        sql.append("SELECT DT.*\n");
        sql.append("  FROM (SELECT A.PART_ID,\n");
        sql.append("               D.PART_CODE,\n");
        sql.append("               D.PART_OLDCODE,\n");
        sql.append("               D.PART_CNAME,\n");
        sql.append("               D.UNIT,\n");
        sql.append("               L.LOC_ID,\n");
        sql.append("               L.LOC_CODE,\n");
        sql.append("               L.LOC_NAME,\n");
//        sql.append("               L.MIN_PKG,\n");
        sql.append("               BD.BATCH_NO, \n");//2070828 add
        sql.append("               D.MIN_PACK2 MIN_PKG,\n");//最小包装量
        sql.append("               A.SO_ID,\n");
        sql.append("               A.SLINE_ID,\n");
        sql.append("               NVL(SUM(BD.BOOKED_QTY), '0') AS SALES_QTY,\n");
        sql.append("               NVL((SELECT SUM(PKG_QTY) FROM TT_PART_PKG_DTL TPPD\n");
        sql.append("                    WHERE TPPD.PICK_ORDER_ID = '" + pickOrderId + "'\n");
        sql.append("                      AND TPPD.PART_ID = A.PART_ID\n");
        sql.append("                      AND TPPD.batch_no = BD.batch_no \n");//20170829 add
        sql.append("                      AND TPPD.LOC_ID = L.LOC_ID AND TPPD.PKG_NO IS NOT NULL),\n");
        sql.append("                   '0') AS PKGEDQTY,\n");
        sql.append("               NVL((SELECT SUM(T.ITEM_QTY) FROM VW_PART_STOCK T\n");
        sql.append("                    WHERE T.WH_ID = '" + whId + "' AND T.PART_ID = A.PART_ID\n");
        sql.append("                      AND t.batch_no = BD.batch_no \n");//20170829 add
        sql.append("                      AND T.LOC_ID = L.LOC_ID),\n");
        sql.append("                   '0') AS NORMAL_QTY_NOW");
        sql.append("           FROM TT_PART_SO_DTL          A, \n");
        sql.append("                TT_PART_SO_MAIN         SM, \n");
        sql.append("                TT_PART_LOACTION_DEFINE L, \n");
        sql.append("                TT_PART_DEFINE          D, \n");
        sql.append("                TT_PART_BOOK_DTL        BD --配件多货位占用明细表 \n");
        sql.append("          WHERE A.SO_ID = SM.SO_ID \n");
        sql.append("            AND A.SO_ID = BD.ORDER_ID \n");
//      sql.append("            AND A.PART_ID = L.PART_ID \n");//一个配件对应一个货位
        sql.append("            AND A.PART_ID = D.PART_ID \n");
        sql.append("            AND A.PART_ID = BD.PART_ID \n");//一个配件对应多货位然后确定一个具体货位
        sql.append("            AND BD.LOC_ID = L.LOC_ID \n");
        sql.append("            AND L.WH_ID = '").append(whId).append("' \n");
        sql.append("            AND L.ORG_ID = ").append(loginUser.getOrgId());
        sql.append("            AND A.STATUS = 1 \n");
        sql.append("            AND SM.STATUS = 1 \n");
        sql.append("            AND SM.PICK_ORDER_ID = '").append(pickOrderId).append("' \n");

        sql.append("          GROUP BY A.PART_ID, \n");
        sql.append("                   D.PART_CODE, \n");
        sql.append("                   D.PART_OLDCODE, \n");
        sql.append("                   D.PART_CNAME, \n");
        sql.append("                   D.UNIT, \n");
        sql.append("                   L.LOC_ID, \n");
        sql.append("                   L.LOC_CODE, \n");
        sql.append("                   L.LOC_NAME, \n");
        sql.append("                   A.SO_ID, \n");
        sql.append("                   A.SLINE_ID, \n");
        sql.append("                   BD.BATCH_NO, \n");//2070828 add
        sql.append("                   D.MIN_PACK2 \n");
//        sql.append("                   L.MIN_PKG  \n");
        sql.append("                   ) DT \n");
        sql.append("  WHERE DT.PKGEDQTY < DT.SALES_QTY \n");

        return pageQuery(sql.toString(), null, getFunName());
    }

    /**
     * 查询预占明细，数量从小到大排列
     *
     * @param pickOrderId 拣货单ID
     * @param whId        仓库ID
     * @param locId       货位ID
     * @param partId      配件ID
     * @return
     */
    public List<Map<String, Object>> queryBookDtl(String pickOrderId,
                                                  String whId, String locId, String partId) {
        StringBuffer sql = new StringBuffer();

        sql.append("SELECT BD.ORDER_ID, BD.SALES_QTY, BD.BOOKED_QTY\n");
        sql.append("  FROM TT_PART_BOOK_DTL BD\n");
        sql.append(" WHERE BD.PICKORDER_ID = " + pickOrderId + "\n");
        sql.append("   AND BD.PART_ID = " + partId + "\n");
        sql.append("   AND BD.LOC_ID = " + locId + "\n");
        sql.append("   AND BD.WH_ID = " + whId + "\n");
        sql.append("   AND BD.STATE = 10011001\n");
        sql.append("   AND BD.STATUS = 1");
        sql.append("   Order by BD.BOOKED_QTY ASC");

        return pageQuery(sql.toString(), null, getFunName());
    }

    /**
     * 查询已装箱明细
     *
     * @param pickOrderId 拣货单ID
     * @param whId        仓库ID
     * @param locId       货位ID
     * @param partId      配件ID
     * @param soId        销售单ID
     * @return
     */
    public List<Map<String, Object>> queryPkgDtl(String pickOrderId,
                                                 String whId, String locId, String partId, Long soId) {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT nvl(SUM(D.PKG_QTY),0) PKG_QTY\n");
        sql.append("  FROM TT_PART_PKG_DTL D\n");
        sql.append(" WHERE D.PICK_ORDER_ID = " + pickOrderId + "\n");
        sql.append("   AND D.PART_ID =" + partId + "\n");
        sql.append("   AND D.LOC_ID =" + locId + "\n");
        sql.append("   AND D.SO_ID =" + soId + "\n");
        sql.append("   AND D.PKG_NO IS NOT NULL");

        return pageQuery(sql.toString(), null, getFunName());
    }


    public List<Map<String, Object>> getSoMainByPkgNo(String pickOrderId, String pkgNos) {
        String[] pkgNoArr = pkgNos.split(",");
        StringBuffer sql = new StringBuffer();

        sql.append("    SELECT SM.SO_ID, \n");
        sql.append("           SM.REMARK2, \n");
        sql.append("           SM.WH_ID, \n");
        sql.append("           SM.TRANS_TYPE, \n");
        sql.append("           SM.DEALER_NAME, \n");
        sql.append("           SM.DEALER_CODE, \n");
        sql.append("           SM.TEL, \n");
        sql.append("           TO_CHAR(SM.CREATE_DATE, 'yyyy.MM.dd') AS CREATE_DATE_FM, \n");
        sql.append("           U.NAME, \n");
        sql.append("           SM.ADDR, \n");
        sql.append("           SM.ORDER_CODE, \n");
        sql.append("           C.CODE_DESC \n");
        sql.append("      FROM TT_PART_SO_MAIN SM, TT_PART_SO_DTL SD, TC_USER U, TC_CODE C \n");
        sql.append("     WHERE SM.SO_ID = SD.SO_ID \n");
        sql.append("       AND SM.CREATE_BY = U.USER_ID \n");
        sql.append("       AND SM.ORDER_TYPE = C.CODE_ID \n");
        sql.append("       AND SM.PICK_ORDER_ID = '").append(pickOrderId).append("'");
        sql.append("       AND EXISTS (SELECT 1 \n");
        sql.append("              FROM TT_PART_PKG_DTL GD \n");
        sql.append("             WHERE GD.PICK_ORDER_ID = '").append(pickOrderId).append("'");
        sql.append("               AND GD.PKG_NO IN (");
        for (int i = 0; i < pkgNoArr.length; i++) {
            sql.append("'").append(pkgNoArr[i]).append("',");
        }
        sql.deleteCharAt(sql.length() - 1);
        sql.append(")");
        sql.append("               AND SD.PART_ID = GD.PART_ID) \n");
        sql.append("     GROUP BY SM.SO_ID, \n");
        sql.append("              SM.REMARK2, \n");
        sql.append("              SM.WH_ID, \n");
        sql.append("              SM.TRANS_TYPE, \n");
        sql.append("              SM.DEALER_NAME, \n");
        sql.append("              SM.DEALER_CODE, \n");
        sql.append("              SM.ADDR, \n");
        sql.append("              SM.TEL, \n");
        sql.append("              SM.ORDER_CODE, \n");
        sql.append("              TO_CHAR(SM.CREATE_DATE, 'yyyy.MM.dd'), \n");
        sql.append("              U.NAME, \n");
        sql.append("              C.CODE_DESC \n");


        return this.pageQuery(sql.toString(), null, this.getFunName());
    }


    /**
     * @param : @param pickOrderId
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-12-23
     * @Title : 按箱号获取装箱总金额
     */
    public List<Map<String, Object>> getPkgDetailAmountByPkgNo(String pickOrderId, String pkgNos) {
        String[] pkgNoArr = pkgNos.split(",");
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT D.PICK_ORDER_ID, SUM(NVL(D.PKG_QTY, 0) * S.BUY_PRICE) AS PKG_AMOUNTS \n");
        sql.append("   FROM TT_PART_PKG_DTL D, \n");
        sql.append("        (SELECT DISTINCT SM.PICK_ORDER_ID, SD.PART_ID, SD.BUY_PRICE \n");
        sql.append("           FROM TT_PART_SO_DTL SD, TT_PART_SO_MAIN SM \n");
        sql.append("          WHERE SD.SO_ID = SM.SO_ID) S \n");
        sql.append("  WHERE D.PART_ID = S.PART_ID \n");
        sql.append("    AND D.PICK_ORDER_ID = S.PICK_ORDER_ID \n");
        sql.append("    AND D.PICK_ORDER_ID = '" + pickOrderId + "' ");
        sql.append("    AND D.PKG_NO IN (");
        for (int i = 0; i < pkgNoArr.length; i++) {
            sql.append("'").append(pkgNoArr[i]).append("',");
        }
        sql.deleteCharAt(sql.length() - 1);
        sql.append(")");
        sql.append("  GROUP BY D.PICK_ORDER_ID \n");
        sql.append(" UNION \n");
        sql.append(" SELECT '' PICK_ORDER_ID, SUM(NVL(SM.FREIGHT, 0)) AS PKG_AMOUNTS \n");
        sql.append("   FROM TT_PART_SO_MAIN SM \n");
        sql.append("  WHERE SM.PICK_ORDER_ID = '" + pickOrderId + "' ");


        return this.pageQuery(sql.toString(), null, this.getFunName());
    }


    /**
     * @param : @param pickOrderId
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-12-23
     * @Title : 装箱清单List
     */
    public List<Map<String, Object>> getPkgDetailListByPkgNo(String pickOrderId, String pkgNos) {

        List<Map<String, Object>> list = null;
        String[] pkgNoArr = pkgNos.split(",");
        StringBuffer sql = new StringBuffer();
        StringBuffer sql_sum = new StringBuffer();

        sql.append("SELECT D.PKG_NO,\n");
        sql.append("       D.PART_OLDCODE,\n");
        sql.append("       SUBSTR(D.PART_CNAME, 0, 20) PART_CNAME,");
        sql.append("       D.PART_CODE,\n");
        sql.append("       NULL REMARK,\n");
        sql.append("       SUM(D.PKG_QTY) AS PKG_QTY,\n");
        sql.append("       S.BUY_PRICE,\n");
        sql.append("       SUM(NVL(D.PKG_QTY, 0) * S.BUY_PRICE) AS PKG_AMOUNT\n");
        sql.append("  FROM TT_PART_PKG_DTL D,\n");
        sql.append("       (SELECT DISTINCT SM.PICK_ORDER_ID, SD.PART_ID, SD.BUY_PRICE\n");
        sql.append("          FROM TT_PART_SO_DTL SD, TT_PART_SO_MAIN SM\n");
        sql.append("         WHERE SD.SO_ID = SM.SO_ID) S\n");
        sql.append(" WHERE D.PART_ID = S.PART_ID\n");
        sql.append("   AND D.PICK_ORDER_ID = S.PICK_ORDER_ID\n");
        sql.append("   AND D.PICK_ORDER_ID = '" + pickOrderId + "'\n");
        sql.append("    AND D.PKG_NO IN (");
        for (int i = 0; i < pkgNoArr.length; i++) {
            sql.append("'").append(pkgNoArr[i]).append("',");
        }
        sql.deleteCharAt(sql.length() - 1);
        sql.append(")");
        sql.append(" GROUP BY D.PKG_NO, D.PART_OLDCODE, D.PART_CNAME, D.PART_CODE, S.BUY_PRICE\n");
        sql.append(" ORDER BY D.PKG_NO ASC,D.PART_OLDCODE\n");

        list = this.pageQuery(sql.toString(), null, this.getFunName());
        return list;
    }

    /**
     * @param : @param pickOrderId
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-12-23
     * @Title : 装箱清单合计
     */
    public List<Map<String, Object>> getPkgSumListByPkgNo(String pickOrderId, String pkgNos) {

        List<Map<String, Object>> list = null;
        String[] pkgNoArr = pkgNos.split(",");
        StringBuffer sql = new StringBuffer();

        sql.append(" select \n");
        sql.append("	SUM(a.PKG_QTY) AS SUM_NUM, \n");
        sql.append("	SUM(a.PKG_AMOUNT) AS SUM_MONEY \n");
        sql.append(" FROM ( \n");
        sql.append("SELECT D.PKG_NO,\n");
        sql.append("       D.PART_OLDCODE,\n");
        sql.append("       SUBSTR(D.PART_CNAME, 0, 20) PART_CNAME,");
        sql.append("       D.PART_CODE,\n");
        sql.append("       NULL REMARK,\n");
        sql.append("       SUM(D.PKG_QTY) AS PKG_QTY,\n");
        sql.append("       S.BUY_PRICE,\n");
        sql.append("       SUM(NVL(D.PKG_QTY, 0) * S.BUY_PRICE) AS PKG_AMOUNT\n");
        sql.append("  FROM TT_PART_PKG_DTL D,\n");
        sql.append("       (SELECT DISTINCT SM.PICK_ORDER_ID, SD.PART_ID, SD.BUY_PRICE\n");
        sql.append("          FROM TT_PART_SO_DTL SD, TT_PART_SO_MAIN SM\n");
        sql.append("         WHERE SD.SO_ID = SM.SO_ID) S\n");
        sql.append(" WHERE D.PART_ID = S.PART_ID\n");
        sql.append("   AND D.PICK_ORDER_ID = S.PICK_ORDER_ID\n");
        sql.append("   AND D.PICK_ORDER_ID = '" + pickOrderId + "'\n");
        sql.append("    AND D.PKG_NO IN (");
        for (int i = 0; i < pkgNoArr.length; i++) {
            sql.append("'").append(pkgNoArr[i]).append("',");
        }
        sql.deleteCharAt(sql.length() - 1);
        sql.append(")");
        sql.append(" GROUP BY D.PKG_NO, D.PART_OLDCODE, D.PART_CNAME, D.PART_CODE, S.BUY_PRICE\n");
        sql.append(" ORDER BY D.PKG_NO ASC,D.PART_OLDCODE\n");
        sql.append(" ) a \n");

        list = this.pageQuery(sql.toString(), null, this.getFunName());
        return list;
    }


    public PageResult<Map<String, Object>> queryPkgDtl(RequestWrapper request, int curPage, int pageSize) {
        StringBuffer sql = new StringBuffer();
        ActionContext act = ActionContext.getContext();

        String pickOrderId = CommonUtils.checkNull(request.getParamValue("pickOrderId"));//销售单号
        String pickOrderId2 = CommonUtils.checkNull(request.getParamValue("pickOrderId2"));//销售单号
        String partOldcode = CommonUtils.checkNull(request.getParamValue("PART_OLDCODE"));//订货单位
        String pkgNo = CommonUtils.checkNull(request.getParamValue("PKG_NO"));//订货单位
        String trplanCode = CommonUtils.checkNull(request.getParamValue("trplanCode"));//发运单号
        

        sql.append(" SELECT DISTINCT SM.DEALER_CODE,\n");
        sql.append("                SM.DEALER_NAME,\n");
        sql.append("                SM.PICK_ORDER_ID,\n");
        sql.append("                KD.PKG_NO,\n");
        sql.append("                PD.PART_OLDCODE,\n");
        sql.append("                PD.PART_CNAME,\n");
        sql.append("                KD.BATCH_NO,\n");//20170901 add
        sql.append("                KD.PKG_QTY\n");
        sql.append("  FROM TT_PART_PKG_DTL KD, TT_PART_DEFINE PD, TT_PART_SO_MAIN SM\n");
        sql.append(" WHERE KD.PART_ID = PD.PART_ID\n");
        sql.append("   AND KD.PICK_ORDER_ID = SM.PICK_ORDER_ID");
        if (!"".equals(pickOrderId) && null != pickOrderId) {
            sql.append("   AND KD.PICK_ORDER_ID = '" + pickOrderId + "'\n");
        }
        if (!"".equals(pickOrderId2) && null != pickOrderId2) {
            sql.append("   AND KD.PICK_ORDER_ID = '" + pickOrderId2 + "'\n");
        }
        sql.append("   AND KD.PKG_NO IS NOT NULL\n");
        if (!"".equals(partOldcode) && null != partOldcode) {
            sql.append("  AND upper(pd.PART_OLDCODE) like upper('%" + partOldcode + "%')\n");
        }
        if (!"".equals(pkgNo) && null != pkgNo) {
            sql.append("  AND upper(kd.PKG_NO) like upper('%" + pkgNo + "%')\n");
        }
        if (!"".equals(trplanCode) && null != trplanCode) {
            sql.append("AND EXISTS (SELECT 1\n");
            sql.append("       FROM TT_PART_TRANS_PLAN_DTL PD, TT_PART_TRANS_PLAN TP\n");
            sql.append("      WHERE PD.TRPLAN_ID = TP.TRPLAN_ID\n");
            sql.append("        AND PD.PICK_ORDER_ID = D.PICK_ORDER_ID\n");
            sql.append("        AND PD.PKG_NO = D.PKG_NO\n");
            sql.append("        AND TP.TRPLAN_CODE LIKE '%%')");
        }
        sql.append(" ORDER BY KD.PKG_NO ASC, PD.PART_OLDCODE");

        PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
        return ps;
    }

//    public List<Map<String, Object>> exportPkgDtl(String pickOrderId) {
//        StringBuffer sql = new StringBuffer();
//        
//        sql.append(" SELECT DISTINCT SM.DEALER_CODE,\n");
//        sql.append("                SM.DEALER_NAME,\n");
//        sql.append("                SM.PICK_ORDER_ID,\n");
//        sql.append("                KD.PKG_NO,\n");
//        sql.append("                PD.PART_OLDCODE,\n");
//        sql.append("                PD.PART_CNAME,\n");
//        sql.append("                KD.PKG_QTY\n");
//        sql.append("  FROM TT_PART_PKG_DTL KD, TT_PART_DEFINE PD, TT_PART_SO_MAIN SM\n");
//        sql.append(" WHERE KD.PART_ID = PD.PART_ID\n");
//        sql.append("   AND KD.PICK_ORDER_ID = SM.PICK_ORDER_ID");
//        sql.append("   AND KD.PICK_ORDER_ID = '" + pickOrderId + "'\n");
//        
//        return this.pageQuery(sql.toString(), null, this.getFunName());
//    }
    
    /**
     * 更新装装箱明细信息
     *
     * @param pickOrderId 捡货单ID
     * @param pkgNos      箱号s
     */
    public void updatePkgPrintInfo(String pickOrderId, String pkgNos) {
        String[] pkgNoArr = pkgNos.split(",");
        StringBuffer sql = new StringBuffer();
        sql.append("UPDATE TT_PART_PKG_BOX_DTL D\n");
        sql.append("   SET D.PRINT_DATE = SYSDATE, D.PRINT_NUM = NVL(D.PRINT_NUM,0) + 1\n");
        sql.append(" WHERE D.PICK_ORDER_ID ='").append(pickOrderId).append("'\n");
        sql.append("   AND D.PKG_NO IN (");
        for (int i = 0; i < pkgNoArr.length; i++) {
            sql.append("'").append(pkgNoArr[i]).append("',");
        }
        sql.deleteCharAt(sql.length() - 1);
        sql.append(")");
        this.update(sql.toString(), null);
    }

    /**
     * 更新发运计划打印信息
     *
     * @param trplanId 发运计划ID
     * @param userId   用户ID
     */
    public void updateTransPlanPrintInfo(String trplanId, String userId) {
        StringBuffer sql = new StringBuffer();
        sql.append("UPDATE TT_PART_TRANS_PLAN P\n");
        sql.append("   SET P.PRINT_NUM  = NVL(P.PRINT_NUM, 0) + 1,\n");
        sql.append("       P.PRINT_BY   =" + userId + ",\n");
        sql.append("       P.PRINT_DATE = SYSDATE\n");
        sql.append(" WHERE P.TRPLAN_ID =" + trplanId);
        this.update(sql.toString(), null);
    }

    /**
     * 未装箱明细查询
     * @param request
     * @param curPage
     * @param pageSize
     * @return
     */
    public PageResult<Map<String, Object>> queryNonePkgDtl(RequestWrapper request, int curPage, int pageSize) {
        StringBuffer sql = new StringBuffer();
        ActionContext act = ActionContext.getContext();

        String pickOrderId = CommonUtils.checkNull(request.getParamValue("pickOrderId"));//销售单号

        sql.append("SELECT BB.*,\n");
        sql.append("       (BB.BOOKED_QTY - BB.PKG_QTY) DIFF_QTY,\n");
        sql.append("       DECODE(BB.NONEPKG_QTY, 0, '未装箱', '现场BO') RESSON　\n");
        sql.append("  FROM (SELECT B.*,\n");
        sql.append("               NVL((SELECT VD.PKG_QTY\n");
        sql.append("                     FROM (SELECT TPPD.PICK_ORDER_ID,\n");
        sql.append("                                  TPPD.PART_ID,\n");
        sql.append("                                  SUM(PKG_QTY) PKG_QTY\n");
        sql.append("                             FROM TT_PART_PKG_DTL TPPD\n");
        sql.append("                            WHERE TPPD.PKG_NO IS NOT NULL\n");
        sql.append("                            GROUP BY TPPD.PICK_ORDER_ID, TPPD.PART_ID) VD\n");
        sql.append("                    WHERE VD.PICK_ORDER_ID = B.PICKORDER_ID\n");
        sql.append("                      AND VD.PART_ID = B.PART_ID),\n");
        sql.append("                   0) PKG_QTY,\n");
        sql.append("               NVL((SELECT VD.BO_QTY\n");
        sql.append("                     FROM (SELECT TPPD.PICK_ORDER_ID,TPPD.PART_ID,SUM(TPPD.LOC_BO_QTY) BO_QTY\n");
        sql.append("                             FROM TT_PART_PKG_DTL TPPD\n");
        sql.append("                            WHERE TPPD.PKG_NO IS NULL\n");
        sql.append("                            GROUP BY TPPD.PICK_ORDER_ID, TPPD.PART_ID) VD\n");
        sql.append("                    WHERE VD.PICK_ORDER_ID = B.PICKORDER_ID\n");
        sql.append("                      AND VD.PART_ID = B.PART_ID),\n");
        sql.append("                   0) NONEPKG_QTY\n");
        sql.append("          FROM (SELECT BD.PICKORDER_ID,PD.PART_ID,PD.PART_OLDCODE,\n");
        sql.append("                       PD.PART_CNAME,SUM(BD.BOOKED_QTY) BOOKED_QTY\n");
        sql.append("                 FROM TT_PART_BOOK_DTL BD, TT_PART_DEFINE PD\n");
        sql.append("                 WHERE BD.PART_ID = PD.PART_ID\n");
        sql.append("                 GROUP BY BD.PICKORDER_ID,\n");
        sql.append("                          PD.PART_ID,\n");
        sql.append("                          PD.PART_OLDCODE,\n");
        sql.append("                          PD.PART_CNAME) B) BB\n");
        sql.append(" WHERE BB.PICKORDER_ID = '" + pickOrderId + "'\n");
        sql.append("   AND BB.BOOKED_QTY <> BB.PKG_QTY\n");
        sql.append(" ORDER BY BB.PART_OLDCODE");

        PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
        return ps;
    }

    /**
     * 更新装箱起始日期
     *
     * @param pickOrderId 捡货单ID
     */
    public void updatePkgBeginDate(String pickOrderId) {
        StringBuffer sql = new StringBuffer();

        sql.append("UPDATE TT_PART_SO_MAIN M\n");
        sql.append("   SET M.PKG_BEIGIN = SYSDATE\n");
        sql.append(" WHERE M.PKG_BEIGIN IS NULL\n");
        sql.append("   AND M.PICK_ORDER_ID =" + pickOrderId);

        this.update(sql.toString(), null);

    }

    /**
     * 查询预产生的现场BO明细
     *
     * @param pickOrderId 拣货单ID
     * @param partId      配件ID
     * @param locId       货位ID
     * @return
     * @throws Exception
     */
    public Map<String, Object> queryPkgId(String pickOrderId, String partId,
                                          String locId,String batchNo) throws Exception {
        try {
            StringBuffer sql = new StringBuffer();

            sql.append(" SELECT T.PKG_ID \n");
            sql.append("   FROM TT_PART_PKG_DTL T \n");
            sql.append("  WHERE T.PICK_ORDER_ID = '" + pickOrderId + "'\n");
            sql.append("    AND T.PART_ID = ").append(partId);
            sql.append("    AND T.LOC_ID = ").append(locId);
            sql.append("    AND T.BATCH_NO = ").append(batchNo);//20170904 add 
            sql.append("    AND T.PKG_NO IS NULL \n");//无箱号
            sql.append("    AND T.STATUS = 1 \n");

            return pageQueryMap(sql.toString(), null, getFunName());
        } catch (Exception e) {
            throw e;
        }
    }

    public void updateBodtl(Long pkgIdBo, Long difQty,String batchNo) throws Exception {
        try {
            StringBuffer sql = new StringBuffer();

            sql.append(" UPDATE TT_PART_PKG_DTL T \n");
            sql.append("       SET T.LOC_BO_QTY = T.LOC_BO_QTY + (").append(difQty).append(")");
            sql.append(", T.PKG_QTY = T.PKG_QTY - (").append(difQty).append(")");
            sql.append("     WHERE T.PKG_ID = ").append(pkgIdBo);
            sql.append("     AND T.BATCH_NO = ").append(batchNo);//20170904 add

            update(sql.toString(), null);
        } catch (Exception e) {
            throw e;
        }
    }

    public void updatePkgBodtl(String pickId, String partId, String pkgQty,String batchNo) throws Exception {
        try {
            StringBuffer sql = new StringBuffer();
            sql.append("UPDATE TT_PART_PKG_DTL PD\n");
            sql.append("   SET PD.LOC_BO_QTY = PD.LOC_BO_QTY -" + pkgQty + ", PD.PKG_QTY = PD.PKG_QTY + " + pkgQty + "\n");
            sql.append(" WHERE PD.PICK_ORDER_ID =" + pickId);
            sql.append("   AND PD.PART_ID =" + partId);
            sql.append("   AND PD.BATCH_NO =" + batchNo);//20170904 add
            sql.append("   AND PD.PKG_NO IS NULL");
            update(sql.toString(), null);
        } catch (Exception e) {
            throw e;
        }
    }

    public void deleXCBodtl(String pickId) throws Exception {
        try {
            StringBuffer sql = new StringBuffer();

            sql.append("DELETE FROM TT_PART_PKG_DTL D\n");
            sql.append(" WHERE D.LOC_BO_QTY = 0\n");
            sql.append("   AND D.PKG_NO IS NULL\n");
            sql.append("   AND D.PICK_ORDER_ID =" + pickId);

            delete(sql.toString(), null);
        } catch (Exception e) {
            throw e;
        }
    }

    public boolean isExistBookDtl(String pickOrderId, String partId,
                                  String locId,String batchNo) throws Exception {
        try {
            StringBuffer sql = new StringBuffer();
            sql.append(" SELECT T.PART_ID \n");
            sql.append("   FROM TT_PART_BOOK_DTL T \n");
            sql.append("  WHERE T.PICKORDER_ID = '").append(pickOrderId).append("'");
            sql.append("    AND T.PART_ID = ").append(partId);
            sql.append("    AND T.BATCH_NO = ").append(batchNo);
            sql.append("    AND T.LOC_ID = ").append(locId);

            return pageQueryMap(sql.toString(), null, getFunName()) == null ? false : true;
        } catch (Exception e) {
            throw e;
        }
    }

    public PageResult<Map<String, Object>> queryPikDtl(RequestWrapper request, int curPage, int pageSize) {
        StringBuffer sql = new StringBuffer();
        ActionContext act = ActionContext.getContext();

        String pickOrderId = CommonUtils.checkNull(request.getParamValue("pickOrderId"));//销售单号
        String partOldcode = CommonUtils.checkNull(request.getParamValue("PART_OLDCODE"));//订货单位
        String partCname = CommonUtils.checkNull(request.getParamValue("PART_CNAME"));//订货单位

        sql.append("SELECT SD.PART_OLDCODE, SD.PART_CNAME, SUM(SD.SALES_QTY) SALES_QTY\n");
        sql.append("  FROM TT_PART_SO_MAIN SM, TT_PART_SO_DTL SD\n");
        sql.append(" WHERE SM.SO_ID = SD.SO_ID\n");
        sql.append("   AND SM.PICK_ORDER_ID=").append(pickOrderId);

        if (!"".equals(partOldcode) && null != partOldcode) {
            sql.append("  AND upper(SD.PART_OLDCODE) like upper('%" + partOldcode + "%')\n");
        }
        if (!"".equals(partCname) && null != partCname) {
            sql.append("  AND upper(SD.PART_CNAME) like upper('%" + partCname + "%')\n");
        }
        sql.append(" GROUP BY SD.PART_OLDCODE, SD.PART_CNAME");
        sql.append(" ORDER BY SD.PART_OLDCODE asc");

        PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
        return ps;
    }

    public PageResult<Map<String, Object>> queryTrnasDtl(RequestWrapper request, int curPage, int pageSize) {
        StringBuffer sql = new StringBuffer();
        ActionContext act = ActionContext.getContext();

        String pickOrderId = CommonUtils.checkNull(request.getParamValue("pickOrderId"));//拣货单号
        String partOldcode = CommonUtils.checkNull(request.getParamValue("PART_OLDCODE"));//订货单位
        String partCname = CommonUtils.checkNull(request.getParamValue("PART_CNAME"));//订货单位

        sql.append("SELECT TD.SUBMIT_DATE,\n");
        sql.append("       TD.PICK_ORDER_ID,\n");
        sql.append("       TD.TRPLAN_ID,\n");
        sql.append("       OT.TRPLAN_CODE,\n");
        sql.append("       COUNT(DISTINCT TD.PKG_NO) PKG_NUMS,\n");
        sql.append("       ZA_CONCAT(DISTINCT TD.PKG_NO) PKG_NO,\n");
        sql.append("       SUM(TD.SALE_AMOUNT) SALE_AMOUNT,\n");
        sql.append("       OT.TRANSPORT_ORG,\n");
        sql.append("       OT.TRANS_TYPE,\n");
        sql.append("       OT.IN_DATE,\n");
        sql.append("       OT.IN_ID,\n");
        sql.append("       OT.NAME,\n");
        sql.append("       OT.IF_HS\n");
        sql.append("  FROM VW_PART_TRANS_DTL TD, VW_PART_ORDER_TRANS OT\n");
        sql.append(" WHERE TD.TRPLAN_ID = OT.TRPLAN_ID\n");
        sql.append("   AND TD.PICK_ORDER_ID = OT.PICK_ORDER_ID\n");
        sql.append("   AND TD.PICK_ORDER_ID = '"+pickOrderId+"'\n");
        sql.append(" GROUP BY TD.SUBMIT_DATE,\n");
        sql.append("          TD.PICK_ORDER_ID,\n");
        sql.append("          TD.TRPLAN_ID,\n");
        sql.append("          OT.TRPLAN_CODE,\n");
        sql.append("          OT.TRANSPORT_ORG,\n");
        sql.append("          OT.TRANS_TYPE,\n");
        sql.append("          OT.IN_DATE,\n");
        sql.append("          OT.IN_ID,\n");
        sql.append("          OT.NAME,\n");
        sql.append("          OT.IF_HS");

       /* if (!"".equals(partOldcode) && null != partOldcode) {
            sql.append("  AND upper(PD.PART_OLDCODE) like upper('%" + partOldcode + "%')\n");
        }
        if (!"".equals(partCname) && null != partCname) {
            sql.append("  AND upper(PD.PART_CNAME) like upper('%" + partCname + "%')\n");
        }
        sql.append(" ORDER BY PD.PART_OLDCODE asc");
*/
        PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
        return ps;
    }

    //物流信息明细查询
    public PageResult<Map<String, Object>> queryLogisticsInfo(RequestWrapper request, int curPage, int pageSize) {
        StringBuffer sql = new StringBuffer();
        ActionContext act = ActionContext.getContext();

        String trplanId = CommonUtils.checkNull(request.getParamValue("trplanId"));//销售单号
//        String partOldcode = CommonUtils.checkNull(request.getParamValue("PART_OLDCODE"));//订货单位
//        String partCname = CommonUtils.checkNull(request.getParamValue("PART_CNAME"));//订货单位
        String transSDate = CommonUtils.checkNull(request
                .getParamValue("transSDate")); // 提交开始时间transSDate
        String transEDate = CommonUtils.checkNull(request
                .getParamValue("transEDate")); // 提交截止时间,transEDate

        String createSDate = CommonUtils.checkNull(request
                .getParamValue("createSDate")); // 提交开始时间createSDate
        String createEDate = CommonUtils.checkNull(request
                .getParamValue("createEDate")); // 提交截止时间,createEDate


        sql.append("select l.*,  \n");
        sql.append("(select u.name from tc_user u  where u.user_id= l.CREATE_BY) CREAT_NAME \n");
        sql.append("from TT_PART_Logistics_info l \n");
        sql.append("where \n");
        sql.append(" LOGISTICS_NO = " + trplanId + " \n");
        if (null != transSDate && !"".equals(transSDate)) {
            sql.append(" AND to_date(l.LOGISTICS_DATE) >= TO_DATE('" + transSDate + "', 'yyyy-mm-dd')");
        }
        if (null != transEDate && !"".equals(transEDate)) {
            sql.append(" AND to_date(l.LOGISTICS_DATE) <= TO_DATE('" + transEDate + "', 'yyyy-mm-dd')");
        }

        if (null != createSDate && !"".equals(createSDate)) {
            sql.append(" AND to_date(l.CREATE_DATE) >= TO_DATE('" + createSDate + "', 'yyyy-mm-dd')");
        }
        if (null != createEDate && !"".equals(createEDate)) {
            sql.append(" AND to_date(l.CREATE_DATE) <= TO_DATE('" + createEDate + "', 'yyyy-mm-dd')");
        }

        PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
        return ps;
    }

    /**
     * 取消合并提货单
     *
     * @param pickOrderId 拣货单ID
     */
    public void cancelPickOrderId(String pickOrderId) {
        StringBuffer sql = new StringBuffer();

        sql.append("UPDATE TT_PART_SO_MAIN SM\n");
        sql.append("   SET SM.PICK_ORDER_ID       = NULL,\n");
        sql.append("       PICK_ORDER_CREATE_DATE = NULL,\n");
        sql.append("       PICK_ORDER_CREATE_BY   = NULL\n");
        sql.append(" WHERE SM.PICK_ORDER_ID = '" + pickOrderId + "'");

        this.update(sql.toString(), null);
    }

    /**
     * 取消更新占明细中的拣货单ID
     *
     * @param pickOrderId 拣货单ID
     */
    public void cancelUpdateBookedPickOrderId(String pickOrderId) {
        StringBuffer sql = new StringBuffer();

        sql.append("UPDATE TT_PART_BOOK_DTL BD\n");
        sql.append("   SET BD.PICKORDER_ID = NULL,\n");
        sql.append("       BD.UPDATE_DATE  = SYSDATE,\n");
        sql.append("       BD.UPDATE_BY    = 4000012501\n");
        sql.append(" WHERE BD.PICKORDER_ID = " + pickOrderId);

        this.update(sql.toString(), null);
    }

    /**
     * 更新包装计费信息
     *
     * @param pickOrderId
     */
    public void updatePkgWeightInfo(String pickOrderId) {
        StringBuffer sql = new StringBuffer();

        sql.append("UPDATE TT_PART_PKG_BOX_DTL D\n");
        sql.append("   SET (D.EQ_WEIGHT, D.CH_WEIGHT) =\n");
        sql.append("       (SELECT T.MEQ_WEIGHT, T.END_WEIGHT\n");
        sql.append("          FROM VW_PART_PKG_BOX T\n");
        sql.append("         WHERE T.BOX_ID = D.BOX_ID)\n");
        sql.append(" WHERE EXISTS\n");
        sql.append(" (SELECT 1\n");
        sql.append("          FROM VW_PART_PKG_BOX T\n");
        sql.append("         WHERE T.BOX_ID = D.BOX_ID\n");
        if (!"".equals(pickOrderId) && null != pickOrderId) {
            sql.append("       AND T.pick_order_id = " + pickOrderId + "\n");
        }
        sql.append("           AND (T.END_WEIGHT <> T.CH_WEIGHT OR T.EQ_WEIGHT <> T.MEQ_WEIGHT))");

        this.update(sql.toString(), null);

    }

    public void deleteOptHis(String orderId) {
        StringBuffer sql = new StringBuffer();
        sql.append("DELETE FROM TT_PART_OPERATION_HISTORY H\n");
        sql.append(" WHERE H.ORDER_ID = " + orderId + "\n");
        sql.append("   AND H.WHAT LIKE '%拣货中%'");

        this.delete(sql.toString(), null);
    }

    public Map<String, Object> queryDealerPKGNos(String dealerId, String pkgNo) throws Exception {
        try {
            StringBuffer sql = new StringBuffer("");

            sql.append("SELECT *\n");
            sql.append("  FROM TT_PART_DLR_PKGNO PD\n");
            sql.append(" WHERE 1 = 1\n");
            if (!"".equals(dealerId) && null != dealerId) {
                sql.append("   AND PD.DEALER_ID = '" + dealerId + "'\n");
            }
            if (!"".equals(pkgNo) && null != pkgNo) {
                sql.append("   AND PD.PKG_NO = '" + pkgNo + "'");
            }

            return pageQueryMap(sql.toString(), null, getFunName());
        } catch (Exception e) {
            throw e;
        }
    }
    /**
     * 查询物流信息
     * @param request
     * @param curPage
     * @param pageSize
     * @return
     */
    public PageResult<Map<String, Object>> queryogistics(RequestWrapper request, int curPage, int pageSize) {
        StringBuffer sql = new StringBuffer();
        String pickOrderId = CommonUtils.checkNull(request.getParamValue("pickOrderId"));//拣货单号

        if(!"".equals(pickOrderId) && null != pickOrderId) {

            sql.append("SELECT *\n");
            sql.append("  FROM TT_PART_LOGISTICS_INFO I\n");
            sql.append(" WHERE EXISTS (SELECT 1\n");
            sql.append("          FROM TT_PART_TRANS_PLAN_DTL PD, TT_PART_TRANS_PLAN TP\n");
            sql.append("         WHERE PD.TRPLAN_ID = TP.TRPLAN_ID\n");
            sql.append("           AND TP.TRPLAN_CODE = I.LOGISTICS_NO\n");
            sql.append("           AND PD.PICK_ORDER_ID ="+pickOrderId+"\n");
            sql.append("\t\t\t\t\t\t)\n");
        }
        PageResult<Map<String, Object>> ps = pageQuery(sql.toString(), null, getFunName(), pageSize, curPage);
        return ps;
    }

}
