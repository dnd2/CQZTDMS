package com.infodms.dms.actions.parts.salesManager.carFactorySalesManager;

import com.infodms.dms.actions.partsmanage.infoSearch.DealerDlrstockInfo;
import com.infodms.dms.actions.sales.planmanage.PlanUtil.BaseImport;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.bean.OrgBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.constants.PTConstants;
import com.infodms.dms.dao.parts.baseManager.partsBaseManager.PartWareHouseDao;
import com.infodms.dms.dao.parts.purchaseManager.purchasePlanSetting.PurchasePlanSettingDao;
import com.infodms.dms.dao.parts.salesManager.PartDlrOrderDao;
import com.infodms.dms.dao.parts.salesManager.PartOutstockDao;
import com.infodms.dms.dao.parts.salesManager.PartSoManageDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TtPartOutstockMainPO;
import com.infodms.dms.util.CheckUtil;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;
import com.infoservice.po3.bean.PageResult;
import jxl.Workbook;
import jxl.write.Label;
import org.apache.log4j.Logger;

import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
/**
 * 配件出库单查询
 * @author  
 * @version  
 * @see 
 * @since 
 * @deprecated
 */
public class PartOutstockQuery extends BaseImport implements PTConstants {
    public Logger logger = Logger.getLogger(PartOutstockQuery.class);

    /**
     * @param : @param orgAmt
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-4-3
     * @Title :
     * @Description:导出
     */
    public static Object exportEx(ResponseWrapper response,
                                  RequestWrapper request, String[] head, List<String[]> list)
            throws Exception {
        String flag = CommonUtils.checkNull(request.getParamValue("flag"));
        String name = null;
        if ("1".equals(flag)) {
            name = "配件出库列表.xls";
        } else {
            name = "配件出库明细.xls";
        }

        jxl.write.WritableWorkbook wwb = null;
        OutputStream out = null;
        try {
            response.setContentType("application/octet-stream");
            response.addHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode(name, "utf-8"));
            out = response.getOutputStream();
            wwb = Workbook.createWorkbook(out);
            jxl.write.WritableSheet ws = wwb.createSheet("sheettest", 0);

            if (head != null && head.length > 0) {
                for (int i = 0; i < head.length; i++) {
                    ws.addCell(new Label(i, 0, head[i]));
                }
            }
            int pageSize = list.size() / 30000;
            for (int z = 1; z < list.size() + 1; z++) {
                String[] str = list.get(z - 1);
                for (int i = 0; i < str.length; i++) {
                       /*ws.addCell(new Label(i, z, str[i]));*/ //modify by yuan
                    if (CheckUtil.checkFormatNumber1(str[i] == null ? "" : str[i])) {
                        ws.addCell(new jxl.write.Number(i, z, Double.parseDouble(str[i])));
                    } else {
                        ws.addCell(new Label(i, z, str[i]));
                    }
                }
            }
            wwb.write();
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (null != wwb) {
                wwb.close();
            }
            if (null != out) {
                out.close();
            }
        }
        return null;
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-4-18
     * @Title :
     * @Description: 出库单初始化
     */
    public void partOutstockQueryInit() {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        try {
            String orgId = "";
            PartWareHouseDao partWareHouseDao = PartWareHouseDao.getInstance();
            List<OrgBean> beanList = partWareHouseDao.getOrgInfo(loginUser);
            if (null != beanList || beanList.size() >= 0) {
                orgId = beanList.get(0).getOrgId() + "";
            }
            PurchasePlanSettingDao purchasePlanSettingDao = PurchasePlanSettingDao.getInstance();
            List<Map<String, Object>> wareHouseList = purchasePlanSettingDao.getWareHouse(orgId);
            request.setAttribute("wareHouseList", wareHouseList);
            act.setOutData("old", CommonUtils.getBefore(new Date()));
            act.setOutData("now", CommonUtils.getDate());
            act.setForword(PART_OUTSTOCK_QUERY_MAIN);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "出库单初始化错误,请联系管理员!");
            logger.error(loginUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-4-18
     * @Title :
     * @Description: 配件销售查询
     */
    public void partOutstockQuery() {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        try {
            PartOutstockDao dao = PartOutstockDao.getInstance();
            //分页方法 begin
            Integer curPage = request.getParamValue("curPage") != null ? Integer
                    .parseInt(request.getParamValue("curPage"))
                    : 1; // 处理当前页
            PageResult<Map<String, Object>> ps = null;

            //判断是否为车厂  PartWareHouseDao
            String dealerId = "";
            PartWareHouseDao dao2 = PartWareHouseDao.getInstance();
            List<OrgBean> beanList = dao2.getOrgInfo(loginUser);
            if (null != beanList || beanList.size() >= 0) {
                if ((beanList.get(0).getOrgId() + "").equals(Constant.OEM_ACTIVITIES)) {
                    dealerId = Constant.OEM_ACTIVITIES;
                } else {
                    dealerId = loginUser.getDealerId();
                }
            }
            ps = dao.queryOustockOrder(request, dealerId, curPage, Constant.PAGE_SIZE);
            act.setOutData("ps", ps);
        } catch (Exception e) {
            if (e instanceof BizException) {
                BizException e1 = (BizException) e;
                logger.error(loginUser, e1);
                act.setException(e1);
                act.setForword(PART_OUTSTOCK_QUERY_MAIN);
                return;
            }
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "配件销售提报查询数据错误,请联系管理员!");
            logger.error(loginUser, e1);
            act.setException(e1);
            act.setForword(PART_OUTSTOCK_QUERY_MAIN);
        }
    }

    /**
     * 配件出库单-查看
     * @param :
     * @return :
     * @throws : LastDate    : 2013-4-18
     * @Title :
     * @Description: 明细查看
     */
    public void partOutstockDetail() {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        PartOutstockDao partOutstockDao = PartOutstockDao.getInstance();
        PartSoManageDao partSoManageDao = PartSoManageDao.getInstance();
        PartDlrOrderDao partDlrOrderDao = PartDlrOrderDao.getInstance();
        try {
            String outId = CommonUtils.checkNull(request.getParamValue("outId"));
            String flag = CommonUtils.checkNull(request.getParamValue("flag"));
            
            Map<String, Object> mainMap = partOutstockDao.queryOutstockMain(outId);
            //销售单信息
            Map<String, Object> soMap = partSoManageDao.getSoOrderMain(CommonUtils.checkNull(mainMap.get("SO_ID")));
            String orderCreateBy = "";
            String orderCreateDate = "";
            if (Constant.CAR_FACTORY_SO_FORM_02.toString().equals(CommonUtils.checkNull(mainMap.get("SO_FROM")))) {
                String orderId = CommonUtils.checkNull(soMap.get("ORDER_ID"));
                Map<String, Object> orderMap = partDlrOrderDao.queryPartDlrOrderMain(orderId);
                orderCreateBy = CommonUtils.checkNull(orderMap.get("NAME"));
                orderCreateDate = CommonUtils.checkNull(orderMap.get("CREATE_DATE"));
            }
            //单据操作记录
            List<Map<String, Object>> historyList = partSoManageDao.queryOrderHistory(CommonUtils.checkNull(mainMap.get("SO_CODE")));
            mainMap.put("orderCreateBy", orderCreateBy);
            mainMap.put("orderCreateDate", orderCreateDate);
            
            //配件信息查询
            List<Map<String, Object>> detailList = partOutstockDao.queryOutstockDetail(outId);
            
            act.setOutData("mainMap", mainMap);
            act.setOutData("detailList", detailList);
            act.setOutData("historyList", historyList);
            act.setOutData("flag", flag);
            act.setForword(PART_OUTSTOCK_QUERY_DETAIL);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "出库单初始化错误,请联系管理员!");
            logger.error(loginUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-4-18
     * @Title :
     * @Description: 作废
     */
    public void cancelOrder() {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        try {
            PartOutstockDao partOutstockDao =  PartOutstockDao.getInstance();
            String outId = CommonUtils.checkNull(request.getParamValue("outId"));
            TtPartOutstockMainPO oldPo = new TtPartOutstockMainPO();
            oldPo.setOutId(Long.valueOf(outId));
            TtPartOutstockMainPO newPo = new TtPartOutstockMainPO();
            newPo.setOutId(Long.valueOf(outId));
            newPo.setState(Constant.CAR_FACTORY_OUTSTOCK_STATE_05);
            partOutstockDao.update(oldPo, newPo);
            act.setForword(PART_OUTSTOCK_QUERY_MAIN);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "出库单初始化错误,请联系管理员!");
            logger.error(loginUser, e1);
            act.setException(e1);
        }

    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-4-18
     * @Title :
     * @Description: 导出
     */
    public void exportExcel() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            RequestWrapper request = act.getRequest();
            PartOutstockDao dao =  PartOutstockDao.getInstance();
            String flag = CommonUtils.checkNull(request.getParamValue("flag"));
            String dealerId = "";
            //判断是否为车厂  PartWareHouseDao
            PartWareHouseDao dao2 = PartWareHouseDao.getInstance();
            List<OrgBean> beanList = dao2.getOrgInfo(logonUser);
            if (null != beanList || beanList.size() >= 0) {
                if ((beanList.get(0).getOrgId() + "").equals(Constant.OEM_ACTIVITIES)) {
                    dealerId = Constant.OEM_ACTIVITIES;
                } else {
                    dealerId = logonUser.getDealerId();
                }
            }
            String[] head = null;
            //主表导出
            if ("1".equals(flag)) {
                head = new String[13];
                head[0] = "拣货单号 ";
                head[1] = "销售单号 ";
                head[2] = "出库单号";
                head[3] = "订货单位编码";
                head[4] = "订货单位";
                head[5] = "制单人";
                head[6] = "拣货单号";
                head[7] = "出库日期";
                head[8] = "拣货日期";
                head[9] = "销售单位";
                head[10] = "订单类型";
                head[11] = "销售金额";
                head[12] = "出库仓库";
                PageResult<Map<String, Object>> ps = dao.queryOustockOrder(request, dealerId, 1, Constant.PAGE_SIZE_MAX);
                List<Map<String, Object>> list = ps.getRecords();
                list = list == null ? new ArrayList() : list;
                List list1 = new ArrayList();
                for (int i = 0; i < list.size(); i++) {
                    Map map = (Map) list.get(i);
                    if (map != null && map.size() != 0) {
                        String[] detail = new String[13];
                        detail[0] = CommonUtils.checkNull(map.get("PICK_ORDER_ID"));
                        detail[1] = CommonUtils.checkNull(map.get("SOCODE"));
                        detail[2] = CommonUtils.checkNull(map.get("OUT_CODE"));
                        detail[3] = CommonUtils.checkNull(map.get("DEALER_CODE"));
                        detail[4] = CommonUtils.checkNull(map.get("DEALER_NAME"));
                        detail[5] = CommonUtils.checkNull(map.get("CREATE_BY_NAME"));
                        detail[6] = CommonUtils.checkNull(map.get("PICK_ORDER_ID"));
                        detail[7] = CommonUtils.checkNull(map.get("CREATE_DATE"));
                        detail[8] = CommonUtils.checkNull(map.get("PICK_ORDER_CREATE_DATE"));
                        detail[9] = CommonUtils.checkNull(map.get("SELLER_NAME"));
                        detail[10] = CommonUtils.checkNull(CommonUtils.getCodeDesc(map.get("ORDER_TYPE") + ""));
                        detail[11] = CommonUtils.checkNull(map.get("AMOUNT"));
                        detail[12] = CommonUtils.checkNull(map.get("WH_NAME"));
                        list1.add(detail);
                    }
                }
                this.exportEx(ActionContext.getContext().getResponse(), request, head, list1);
            } else {
                head = new String[19];
                head[0] = "出库单号 ";
                head[1] = "订货单位编码";
                head[2] = "订货单位";
                head[3] = "配件编码";
                head[4] = "配件名称";
                head[5] = "配件件号";
                head[6] = "单位";
                head[7] = "销售单价";
                head[8] = "出库数量";
                head[9] = "出库金额";
                head[10] = "销售日期";
                head[11] = "发票号";
                PageResult<Map<String, Object>> ps = dao.queryOutstockDetail(request, dealerId, 1, Constant.PAGE_SIZE_MAX);
                List<Map<String, Object>> list = ps.getRecords();
                list = list == null ? new ArrayList() : list;
                List list1 = new ArrayList();
                for (int i = 0; i < list.size(); i++) {
                    Map map = (Map) list.get(i);
                    if (map != null && map.size() != 0) {
                        String[] detail = new String[19];
                        detail[0] = CommonUtils.checkNull(map.get("OUT_CODE"));
                        detail[1] = CommonUtils.checkNull(map.get("DEALER_CODE"));
                        detail[2] = CommonUtils.checkNull(map.get("DEALER_NAME"));
                        detail[3] = CommonUtils.checkNull(map.get("PART_OLDCODE"));
                        detail[4] = CommonUtils.checkNull(map.get("PART_CNAME"));
                        detail[5] = CommonUtils.checkNull(map.get("PART_CODE"));
                        detail[6] = CommonUtils.checkNull(map.get("UNIT"));
                        detail[7] = CommonUtils.checkNull(map.get("SALE_PRICE"));
                        detail[8] = CommonUtils.checkNull(map.get("OUTSTOCK_QTY"));
                        detail[9] = CommonUtils.checkNull(map.get("SALE_AMOUNT"));
                        detail[10] = CommonUtils.checkNull(map.get("CREATE_DATE"));
                        detail[11] = CommonUtils.checkNull(map.get("BILL_NO"));
                        list1.add(detail);
                    }
                }
                this.exportEx(ActionContext.getContext().getResponse(), request, head, list1);
            }
        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }
}
