package com.infodms.dms.actions.parts.salesManager.carFactorySalesManager;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.bean.OrgBean;
import com.infodms.dms.common.Arith;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.constants.PTConstants;
import com.infodms.dms.dao.parts.baseManager.partsBaseManager.PartWareHouseDao;
import com.infodms.dms.dao.parts.purchaseManager.partPlanConfirm.PartPlanConfirmDao;
import com.infodms.dms.dao.parts.salesManager.PartBoDao;
import com.infodms.dms.dao.parts.salesManager.PartDlrOrderDao;
import com.infodms.dms.dao.parts.salesManager.PartTransPlanDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.*;
import com.infodms.dms.util.CheckUtil;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.OrderCodeManager;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;
import com.infoservice.po3.bean.DynaBean;
import com.infoservice.po3.bean.PageResult;
import jxl.Workbook;
import jxl.write.Label;
import org.apache.log4j.Logger;

import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.*;

/**
 * @author : chenjunjiang
 *         CreateDate     : 2013-5-13
 * @ClassName : BOManager
 * @Description : BO单管理
 */
@SuppressWarnings("unchecked")
public class BOManager implements PTConstants {
    public Logger logger = Logger.getLogger(BOManager.class);
    private PartBoDao dao = PartBoDao.getInstance();

    public static Object exportEx(ResponseWrapper response,
                                  RequestWrapper request, String[] head, List<String[]> list, int flag)
            throws Exception {

        String name = "";
        if (flag == 0) {
            name = "配件BO单信息.xls";
        } else {
            name = "配件BO单明细信息.xls";
        }
        jxl.write.WritableWorkbook wwb = null;
        OutputStream out = null;
        try {
            response.setContentType("application/octet-stream");
            response.addHeader("Content-disposition", "attachment;filename="
                    + URLEncoder.encode(name, "utf-8"));
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
     * @param : @param response
     * @param : @param request
     * @param : @param head
     * @param : @param list
     * @param : @return
     * @param : @throws Exception
     * @return :
     * @throws : LastDate    : 2013-5-3
     * @Title : 文件导出为xls文件
     */
    public static Object exportEx(String fileName, ResponseWrapper response,
                                  RequestWrapper request, String[] head, List<String[]> list)
            throws Exception {

        String name = fileName + ".xls";
        jxl.write.WritableWorkbook wwb = null;
        OutputStream out = null;
        try {
            response.setContentType("application/octet-stream");
            response.addHeader("Content-disposition", "attachment;filename="
                    + URLEncoder.encode(name, "utf-8"));
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
     * @throws : LastDate    : 2013-5-13
     * @Title :
     * @Description: BO单查询初始化
     */
    public void boQueryInit() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            boolean salerFlag = false;
            PartDlrOrderDao dao = PartDlrOrderDao.getInstance();
            List<Map<String, Object>> salerList = dao.getSaler();
            if (logonUser.getDealerId() == null) {
                salerFlag = true;
            }
            act.setOutData("curUserId", logonUser.getUserId());
            act.setOutData("salerList", salerList);
            act.setOutData("salerFlag", salerFlag);
            act.setOutData("now", CommonUtils.getDate());
            act.setOutData("old", CommonUtils.getBefore(new Date()));
            act.setForword(PART_BO_QUERY_URL);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "配件BO单");
            logger.error(logonUser, e1);
            act.setException(e1);
        }

    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-5-13
     * @Title :
     * @Description: 查询配件bo
     */
    public void queryPartBoinfo() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            String orderCode = CommonUtils.checkNull(request.getParamValue("ORDER_CODE"));//订单号
            String partOldCode = CommonUtils.checkNull(request.getParamValue("PART_OLDCODE"));//配件编码
            String boCode = CommonUtils.checkNull(request.getParamValue("BO_CODE"));//BO单号
            String dealerName = CommonUtils.checkNull(request.getParamValue("DEALER_NAME"));//订货单位
            String sellerName = CommonUtils.checkNull(request.getParamValue("SELLER_NAME"));//销售单位
            String state = CommonUtils.checkNull(request.getParamValue("STATE"));//BO单状态
            String orderType = CommonUtils.checkNull(request.getParamValue("ORDER_TYPE"));//订单类型
            String dealerCode = CommonUtils.checkNull(request.getParamValue("DEALER_CODE"));//订单单位编码
            String startDate = CommonUtils.checkNull(request.getParamValue("startDate"));//制单开始时间
            String endDate = CommonUtils.checkNull(request.getParamValue("endDate"));//制单结束时间
            String startDate1 = CommonUtils.checkNull(request.getParamValue("startDate1"));//制单开始时间
            String endDate1 = CommonUtils.checkNull(request.getParamValue("endDate1"));//制单结束时间
            String salerId = CommonUtils.checkNull(request.getParamValue("salerId"));//销售人员
            String boType = CommonUtils.checkNull(request.getParamValue("boType"));//bo单；类型

            TtPartBoMainPO po = new TtPartBoMainPO();
            po.setOrderCode(orderCode);
            po.setBoCode(boCode);
            if (!"".equals(state)) {
                po.setState(CommonUtils.parseInteger(state));
            }

            //分页方法 begin
            Integer curPage = request.getParamValue("curPage") != null ? Integer
                    .parseInt(request.getParamValue("curPage"))
                    : 1; // 处理当前页
            PageResult<Map<String, Object>> ps = dao.queryPartBoList(po, partOldCode, startDate, endDate, dealerName, sellerName, orderType,
                    dealerCode, salerId, startDate1, endDate1, logonUser, boType, curPage, Constant.PAGE_SIZE);
            //分页方法 end
            act.setOutData("ps", ps);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "配件BO单");
            logger.error(logonUser, e1);
            act.setException(e1);
        }

    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-5-13
     * @Title :
     * @Description: 查询配件bo
     */
    public void queryPartBoinfo4Handle() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            String orderCode = CommonUtils.checkNull(request.getParamValue("ORDER_CODE"));//订单号
            String partOldCode = CommonUtils.checkNull(request.getParamValue("PART_OLDCODE"));//配件编码
            String boCode = CommonUtils.checkNull(request.getParamValue("BO_CODE"));//BO单号
            String dealerName = CommonUtils.checkNull(request.getParamValue("DEALER_NAME"));//订货单位
            String sellerName = CommonUtils.checkNull(request.getParamValue("SELLER_NAME"));//销售单位
            String startDate = CommonUtils.checkNull(request.getParamValue("startDate"));//制单开始时间
            String endDate = CommonUtils.checkNull(request.getParamValue("endDate"));//制单结束时间
            String state = CommonUtils.checkNull(request.getParamValue("STATE"));//BO单状态
            String orderType = CommonUtils.checkNull(request.getParamValue("ORDER_TYPE"));//订单类型
            String dealerCode = CommonUtils.checkNull(request.getParamValue("DEALER_CODE"));//订单单位编码
            String IF_TYPE = CommonUtils.checkNull(request.getParamValue("IF_TYPE"));//是否有库存
            String salerId = CommonUtils.checkNull(request.getParamValue("salerId"));//销售人员

            //判断是否为车厂  PartWareHouseDao
            String dealerId = "";
            PartWareHouseDao partWareHouseDao = PartWareHouseDao.getInstance();
            List<OrgBean> beanList = partWareHouseDao.getOrgInfo(logonUser);
            if (null != beanList || beanList.size() >= 0) {
                dealerId = beanList.get(0).getOrgId() + "";
            }

            TtPartBoMainPO po = new TtPartBoMainPO();
            po.setOrderCode(orderCode);
            po.setBoCode(boCode);
            if (!"".equals(state)) {
                po.setState(CommonUtils.parseInteger(state));
            }

            //分页方法 begin
            Integer curPage = request.getParamValue("curPage") != null ? Integer
                    .parseInt(request.getParamValue("curPage"))
                    : 1; // 处理当前页
            PageResult<Map<String, Object>> ps = dao.queryPartBoList4Handle(po, partOldCode, startDate, endDate, dealerName, sellerName, orderType,
                    dealerCode, IF_TYPE, salerId, logonUser, curPage, Constant.PAGE_SIZE, dealerId);
            //分页方法 end
            if (!"".equals(dealerCode) && ps.getRecords() != null) {
                Map<String, Object> map = ps.getRecords().get(0);
                act.setOutData("DEALER_NAME", map.get("DEALER_NAME"));
            }
            act.setOutData("ps", ps);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "配件BO单");
            logger.error(logonUser, e1);
            act.setException(e1);
        }

    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-6-29
     * @Title :
     * @Description: 转销售单时查询配件明细
     */
    public void queryPartBoDetail4Sal() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            String dealerId = CommonUtils.checkNull(request.getParamValue("dealerId"));//订货单位id
            String whId = CommonUtils.checkNull(request.getParamValue("whId"));//库房id
            String ids = CommonUtils.checkNull(request.getParamValue("ids"));//BO单id
            String[] boIds = ids.split(",");
            //分页方法 begin
            Integer curPage = request.getParamValue("curPage") != null ? Integer
                    .parseInt(request.getParamValue("curPage"))
                    : 1; // 处理当前页
            PageResult<Map<String, Object>> ps = dao.queryPartBoDetail4SalList(dealerId, whId, boIds, logonUser, curPage, Constant.PAGE_SIZE);
            //分页方法 end
            act.setOutData("ps", ps);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "BO单明细");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-5-15
     * @Title :
     * @Description: bo单处理查询初始化
     */
    public void boHandleQueryInit() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            boolean salerFlag = false;
            PartDlrOrderDao dao = PartDlrOrderDao.getInstance();
            List<Map<String, Object>> salerList = dao.getSaler();
            if (logonUser.getDealerId() == null) {
                salerFlag = true;
            }
            act.setOutData("curUserId", logonUser.getUserId());
            act.setOutData("salerList", salerList);
            act.setOutData("salerFlag", salerFlag);
            act.setForword(PART_BOHANDLE_QUERY_URL);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "配件BO单");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-5-15
     * @Title :
     * @Description: 关闭整个bo单
     */
    @SuppressWarnings("unchecked")
    public void closeBo() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            String[] boIds = request.getParamValue("ck").split(",");//bo单id
            if(boIds!=null){

                System.out.println(boIds.length+"=========================");
            }else{

                System.out.println("==========空对象===============");
            }
            if (boIds != null && boIds.length > 0) {
                for (int i = 0; i < boIds.length; i++) {
                    TtPartBoMainPO smainPO = new TtPartBoMainPO();
                    smainPO.setBoId(CommonUtils.parseLong(boIds[i]));
                    TtPartBoMainPO mainPO = new TtPartBoMainPO();
                    mainPO.setState(Constant.CAR_FACTORY_SALES_MANAGER_BO_STATE_03);//关闭之后状态变为已处理
                    mainPO.setDisableDate(new Date());
                    mainPO.setDisableBy(logonUser.getUserId());
                    dao.update(smainPO, mainPO);
                    //同时需要把bo明细中的状态更新为已处理,并更新明细中的关闭数量和剩余数量
                    dao.updateBoDtlStatus(CommonUtils.parseLong(boIds[i]));
                }
            }
            act.setOutData("success", "关闭成功!");
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "BO单关闭错误,请联系管理员!");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-5-15
     * @Title :
     * @Description: 关闭bo单明细
     */
    @SuppressWarnings("unchecked")
    public void closeBoDtl() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            String boLineId = CommonUtils.checkNull(request.getParamValue("boLineId"));//bo单明细id
            String boId = CommonUtils.checkNull(request.getParamValue("boId"));//bo单id
            String remark = CommonUtils.checkNull(request.getParamValue("REMARK" + boLineId));//备注
            TtPartBoDtlPO boDtlPO = new TtPartBoDtlPO();
            boDtlPO.setBolineId(CommonUtils.parseLong(boLineId));
            boDtlPO = (TtPartBoDtlPO) dao.select(boDtlPO).get(0);

            TtPartBoDtlPO sDtlPO = new TtPartBoDtlPO();
            sDtlPO.setBolineId(CommonUtils.parseLong(boLineId));
            TtPartBoDtlPO dtlPO = new TtPartBoDtlPO();
            dtlPO.setCloseQty(boDtlPO.getBoOddqty());
            dtlPO.setBoOddqty(0l);
            dtlPO.setStatus(0);
            dtlPO.setRemark(remark);
            dtlPO.setDisableDate(new Date());
            dtlPO.setDisableBy(logonUser.getUserId());
            dao.update(sDtlPO, dtlPO);
            //如果当前关闭的已经是bo单的最后一个明细,那么就要更新bo单的状态为已完成
            TtPartBoMainPO smainPO = new TtPartBoMainPO();
            smainPO.setBoId(CommonUtils.parseLong(boId));
            boolean flag = dao.isCloseAll(boId);
            TtPartBoMainPO mainPO = new TtPartBoMainPO();
            if (flag) {
                mainPO.setState(Constant.CAR_FACTORY_SALES_MANAGER_BO_STATE_03);
                mainPO.setDisableDate(new Date());
                mainPO.setDisableBy(logonUser.getUserId());
            } else {
                mainPO.setState(Constant.CAR_FACTORY_SALES_MANAGER_BO_STATE_02);
            }
            dao.update(smainPO, mainPO);
            act.setOutData("isAllClose", flag);
            act.setOutData("success", "关闭成功!");
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "BO单明细关闭错误,请联系管理员!");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-5-15
     * @Title :
     * @Description: bo单明细关闭初始化
     */
    public void closePartBoDetailInit() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            String boId = CommonUtils.checkNull(request.getParamValue("boId"));//bo单id
            String orderId = CommonUtils.checkNull(request.getParamValue("orderId"));//订单id
            String flag = CommonUtils.checkNull(request.getParamValue("flag"));//是否为bo处理
            Map map = dao.getPartBoMainInfo(boId);
            request.setAttribute("po", map);
            request.setAttribute("orderId", orderId);
            act.setOutData("flag", flag);
            act.setForword(PART_BO_DETAIL_CLOSE_URL);
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "BO单明细关闭初始化错误 ,请联系管理员!");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-6-29
     * @Title :
     * @Description: BO单转销售单初始化
     */
    public void toSalOrderInit() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            String dealerId = CommonUtils.checkNull(request.getParamValue("dealerId"));//订货单位id
            String whId = CommonUtils.checkNull(request.getParamValue("whId"));//库房id
            String ids = CommonUtils.checkNull(request.getParamValue("ids"));//bo单id
            String orderId = CommonUtils.checkNull(request.getParamValue("orderId"));//订单id

            String[] boIds = ids.split(",");

            Map<String, Object> map = dao.queryNameByBoId(boIds[0]);

            request.setAttribute("dealerId", dealerId);
            request.setAttribute("dealerName", (String) map.get("DEALER_NAME"));
            request.setAttribute("sellerName", (String) map.get("SELLER_NAME"));
            request.setAttribute("ids", ids);
            request.setAttribute("whId", whId);
            request.setAttribute("orderId", orderId);
            act.setForword(PART_BO_TOSALORDER_URL);
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "BO单转销售单失败 ,请联系管理员!");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-5-13
     * @Title :
     * @Description: 导出bo单信息
     */
    public void expPartBoExcel() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        boolean salerFlag = false;
        PartDlrOrderDao dao1 = PartDlrOrderDao.getInstance();
        List<Map<String, Object>> salerList = dao1.getSaler();
        if (logonUser.getDealerId() == null) {
            salerFlag = true;
        }
        act.setOutData("curUserId", logonUser.getUserId());
        act.setOutData("salerList", salerList);
        act.setOutData("salerFlag", salerFlag);

        try {
            RequestWrapper request = act.getRequest();
            String[] head = new String[14];
            head[0] = "BO单号";
            head[1] = "配件订单号";
            head[2] = "订货单位编码";
            head[3] = "订货单位";
            head[4] = "销售单位";
            head[5] = "订单类型";
            head[6] = "BO产生日期";
            head[7] = "订货数量";
            head[8] = "满足数量";
            head[9] = "BO数量";
            head[10] = "转销售数量";
            head[11] = "关闭数量";
            head[12] = "Bo剩余数量";
            head[13] = "BO单状态";
            List<Map<String, Object>> list = dao.queryPartBo(request, logonUser);
            List list1 = new ArrayList();
            if (list != null && list.size() != 0) {
                for (int i = 0; i < list.size(); i++) {
                    Map map = (Map) list.get(i);
                    if (map != null && map.size() != 0) {
                        String[] detail = new String[14];
                        detail[0] = CommonUtils.checkNull(map.get("BO_CODE"));
                        detail[1] = CommonUtils.checkNull(map
                                .get("ORDER_CODE"));
                        detail[2] = CommonUtils
                                .checkNull(map.get("DEALER_CODE"));
                        detail[3] = CommonUtils.checkNull(map
                                .get("DEALER_NAME"));
                        detail[4] = CommonUtils.checkNull(map
                                .get("SELLER_NAME"));
                        detail[5] = CommonUtils.checkNull(map.get("ORDER_TYPE"));
                        detail[6] = CommonUtils.checkNull(map.get("CREATE_DATE"));
                        detail[7] = CommonUtils
                                .checkNull(map.get("BUY_QTY"));
                        detail[8] = CommonUtils
                                .checkNull(map.get("SALES_QTY"));
                        detail[9] = CommonUtils
                                .checkNull(map.get("BO_QTY"));
                        detail[10] = CommonUtils
                                .checkNull(map.get("TOSAL_QTY"));
                        detail[11] = CommonUtils
                                .checkNull(map.get("CLOSE_QTY"));
                        detail[12] = CommonUtils
                                .checkNull(map.get("BO_ODDQTY"));
                        detail[13] = CommonUtils
                                .checkNull(map.get("STATE"));
                        list1.add(detail);
                    }
                }
                this.exportEx(ActionContext.getContext().getResponse(),
                        request, head, list1, 0);
            } else {
                BizException e1 = new BizException(act,
                        ErrorCodeConstant.SPECIAL_MEG, "没有满足条件的数据!");
                throw e1;
            }

        } catch (Exception e) {
            BizException e1 = null;
            if (e instanceof BizException) {
                e1 = (BizException) e;
            } else {
                e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG,
                        "文件下载错误");
            }
            logger.error(logonUser, e1);
            act.setException(e1);
            act.setForword(PART_BO_QUERY_URL);
        }

    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-5-13
     * @Title :
     * @Description: bo单明细查询初始化
     */
    public void queryPartBoDetailInit() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            String boId = CommonUtils.checkNull(request.getParamValue("boId"));//bo单id
            String orderId = CommonUtils.checkNull(request.getParamValue("orderId"));//订单id
            String flag = CommonUtils.checkNull(request.getParamValue("flag"));//是否为bo处理(1为bo处理)
            String buttonFalg = "";//
            if (null != request.getParamValue("buttonFalg")) {
                buttonFalg = request.getParamValue("buttonFalg");
            }
            Map map = dao.getPartBoMainInfo(boId);
            request.setAttribute("po", map);
            request.setAttribute("orderId", orderId);
            act.setOutData("buttonFalg", buttonFalg);
            act.setOutData("flag", flag);
            act.setForword(PART_BO_DETAIL_URL);
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.QUERY_FAILURE_CODE, "配件BO明细");
            logger.error(logonUser, e1);
            act.setException(e1);
        }

    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-5-13
     * @Title :
     * @Description: bo单明细查询
     */
    public void queryPartBoDetail() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            String boId = CommonUtils.checkNull(request.getParamValue("boId"));//bo单id
            String orderId = CommonUtils.checkNull(request.getParamValue("orderId"));//订单id
            String partOldCode = CommonUtils.checkNull(request.getParamValue("PART_OLDCODE"));//配件编码
            String partCname = CommonUtils.checkNull(request.getParamValue("PART_CNAME"));//配件名称
            String partCode = CommonUtils.checkNull(request.getParamValue("PART_CODE"));//配件件号
            String flag = CommonUtils.checkNull(request.getParamValue("flag"));//是否为bo处理
            String orgId = logonUser.getDealerId() == null ? Constant.OEM_ACTIVITIES : logonUser.getDealerId();//增加机构判断
            String viewFlag = "1"; //是否查看已处理完成的BO单，默认查看
            /*if (orgId.equals(Constant.OEM_ACTIVITIES + "")) {
                TtPartUserposeDefinePO userposeDefinePO = new TtPartUserposeDefinePO();
                userposeDefinePO.setUserId(logonUser.getUserId());
                if (dao.select(userposeDefinePO).size() != 0) {
                    viewFlag = "0";  //不查看
                }
            }*/
            //分页方法 begin
            Integer curPage = request.getParamValue("curPage") != null ? Integer
                    .parseInt(request.getParamValue("curPage"))
                    : 1; // 处理当前页
            PageResult<Map<String, Object>> ps = dao.queryPartBoDetailList(boId, orderId, partOldCode, partCname, partCode, flag, curPage, Constant.PAGE_SIZE_PART_MINI, orgId, viewFlag);
            //分页方法 end
            act.setOutData("ps", ps);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "BO单明细");
            logger.error(logonUser, e1);
            act.setException(e1);
        }

    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-5-13
     * @Title :
     * @Description: bo单所有明细查询
     */
    public void queryPartBoDtl() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {

            Map<String, Object> map = dao.queryMainInfo(request, logonUser);
            act.setOutData("allQty", map == null ? 0 : ((BigDecimal) map.get("ALLQTY")).longValue());
            act.setOutData("bonum", map == null ? 0 : ((BigDecimal) map.get("BO_NUM")).longValue());
            act.setOutData("boqty", map == null ? 0 : ((BigDecimal) map.get("BO_QTY")).longValue());
            act.setOutData("amount", map == null ? 0 : (String) map.get("AMOUNT"));

            //分页方法 begin
            Integer curPage = request.getParamValue("curPage") != null ? Integer
                    .parseInt(request.getParamValue("curPage"))
                    : 1; // 处理当前页
            PageResult<Map<String, Object>> ps = dao.queryPartBoDtlList(request, logonUser, curPage, Constant.PAGE_SIZE);
            //分页方法 end
            act.setOutData("ps", ps);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "BO单明细");
            logger.error(logonUser, e1);
            act.setException(e1);
        }

    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-6-28
     * @Title :
     * @Description: Bo汇总查询初始化
     */
    public void queryAllPartBoInit() {
        ActionContext act = ActionContext.getContext();
        //RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            act.setOutData("old", CommonUtils.getBefore(new Date()));
            act.setOutData("now", CommonUtils.getDate());
            act.setForword(PART_BO_QUERYALL_URL);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "配件BO汇总");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-6-28
     * @Title :
     * @Description: Bo明细查询初始化
     */
    public void queryPartBoDtlInit() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            boolean salerFlag = false;
            PartDlrOrderDao dao1 = PartDlrOrderDao.getInstance();
            List<Map<String, Object>> salerList = dao1.getSaler();
            if (logonUser.getDealerId() == null) {
                salerFlag = true;
            }

            act.setOutData("curUserId", logonUser.getUserId());
            act.setOutData("salerList", salerList);
            act.setOutData("salerFlag", salerFlag);
            act.setOutData("old", CommonUtils.getBefore(new Date()));
            act.setOutData("now", CommonUtils.getDate());
            act.setForword(PART_BO_QUERYDTL_URL);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "配件BO明细");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * ：add zhumingwei 2013-09-23
     *
     * @param :
     * @return :
     * @throws : LastDate    : 2013-6-28
     * @Title :
     * @Description: Bo汇总查询
     */
    public void queryAllPartBo() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            String PARTOLD_NAME = CommonUtils.checkNull(request.getParamValue("PARTOLD_NAME"));
            String PART_CODE = CommonUtils.checkNull(request.getParamValue("PART_CODE"));
            String PARTOLD_CODE = CommonUtils.checkNull(request.getParamValue("PARTOLD_CODE"));
            String desc = CommonUtils.checkNull(request.getParamValue("desc"));
            String startDate = CommonUtils.checkNull(request.getParamValue("startDate"));
            String endDate = CommonUtils.checkNull(request.getParamValue("endDate"));
            String startDate1 = CommonUtils.checkNull(request.getParamValue("startDate1"));
            String endDate1 = CommonUtils.checkNull(request.getParamValue("endDate1"));
            String fliter = CommonUtils.checkNull(request.getParamValue("fliter"));//筛选条件

            //分页方法 begin
            Integer curPage = request.getParamValue("curPage") != null ? Integer
                    .parseInt(request.getParamValue("curPage"))
                    : 1; // 处理当前页
            PageResult<Map<String, Object>> ps = dao.queryAllPartBoList(PARTOLD_NAME, PART_CODE, PARTOLD_CODE,
                    desc, startDate, endDate, startDate1, endDate1, logonUser, fliter, curPage, Constant.PAGE_SIZE);
            //分页方法 end
            act.setOutData("ps", ps);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "BO单明细");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-6-29
     * @Title :
     * @Description: 转销售单审核初始化
     */
    @SuppressWarnings("null")
    public void toSalOrderChkInit() {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        try {
            PartDlrOrderDao partDlrOrderDao = PartDlrOrderDao.getInstance();
            String oIds = CommonUtils.checkNull(request.getParamValue("oIds"));//订单id
            String dIds = CommonUtils.checkNull(request.getParamValue("dIds"));//订货单位id
            String wIds = CommonUtils.checkNull(request.getParamValue("wIds"));//仓库id
            String ids = request.getParamValue("ids");//bo单id
            String oCodes = request.getParamValue("oCodes");//订单号
            String accountPurpose = CommonUtils.checkNull(request.getParamValue("ACCOUNT_PURPOSE"));
            String[] boIds = ids.split(",");
            String[] orderIds = oIds.split(",");
            String[] whIds = wIds.split(",");
            String[] dealerIds = dIds.split(",");
            String orderId = orderIds[0];
            String whId = whIds[0];
            String rDealerId = dealerIds[0];
            String boId = boIds[0];
            String orderCode = oCodes;

            //查询订单
            Map<String, Object> mainMap = partDlrOrderDao.queryPartDlrOrderMain(orderId);
            String dealerId = "";
            String flag = "";

            TtPartWarehouseDefinePO warehouseDefinePO = new TtPartWarehouseDefinePO();
            warehouseDefinePO.setWhId(CommonUtils.parseLong(whId));
            warehouseDefinePO = (TtPartWarehouseDefinePO) dao.select(warehouseDefinePO).get(0);

            PartWareHouseDao partWareHouseDao = PartWareHouseDao.getInstance();
            List<OrgBean> beanList = partWareHouseDao.getOrgInfo(loginUser);
            if (null != beanList || beanList.size() >= 0) {
                dealerId = beanList.get(0).getOrgId() + "";
            }
            //折扣率
            String discount = partDlrOrderDao.getDiscount(mainMap.get("DEALER_ID").toString());
            mainMap.put("discount", discount);
            mainMap.put("saleName", loginUser.getName());
            //获取账户
            Map<String, Object> accountMap = partDlrOrderDao.getAccountMoney(mainMap.get("DEALER_ID").toString(), mainMap.get("SELLER_ID").toString(), accountPurpose);
            String accountSumNow = "";
            String accountKyNow = "";
            String accountDjNow = "";
            String accountId = "";
            boolean accountFlag = false;
            if (null != accountMap) {
                accountFlag = true;
                accountSumNow = accountMap.get("ACCOUNT_SUM").toString();
                accountKyNow = accountMap.get("ACCOUNT_KY").toString();
                accountDjNow = accountMap.get("ACCOUNT_DJ").toString();
                accountId = accountMap.get("ACCOUNT_ID").toString();
            }
            mainMap.put("accountFlag", accountFlag);
            mainMap.put("accountSumNow", accountSumNow);
            mainMap.put("accountKyNow", accountKyNow);
            mainMap.put("accountDjNow", accountDjNow);
            mainMap.put("accountId", accountId);

            //获取当前bo单中的配件信息
            List<Map<String, Object>> detailList = dao.queryBoDtlDetail(boId, rDealerId, loginUser, whId);

            for (int i = 0; i < detailList.size(); i++) {
                Map<String, Object> deMap = detailList.get(i);
                int boTosales = ((BigDecimal) deMap.get("BO_TOSALES")).intValue();
                float allotRatio = ((BigDecimal) deMap.get("ALLOT_RATIO")).floatValue();
                int allotNum = ((BigDecimal) deMap.get("ALLOT_NUM")).intValue();
                int normalQty = ((BigDecimal) deMap.get("NORMAL_QTY")).intValue();

                if (boTosales == Constant.PART_BASE_FLAG_NO.intValue()) {//如果该配件不能转销售
                    detailList.remove(i);
                } else {
                    if (allotRatio != 0) {//如果有资源分配比列
                        normalQty = (int) Math.floor(Arith.mul(normalQty, allotRatio));
                        deMap.put("NORMAL_QTY", normalQty);
                        detailList.set(i, deMap);
                    }
                    if (allotNum != 0) {//如果有分配数量
                        normalQty = allotNum;
                        deMap.put("NORMAL_QTY", normalQty);
                        detailList.set(i, deMap);
                    }
                }
            }

            if (CommonUtils.checkNull(mainMap.get("ORDER_TYPE")).equals(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_04 + "") ||
            	CommonUtils.checkNull(mainMap.get("ORDER_TYPE")).equals(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_12 + "")) {
                if (dealerId.equals(Constant.OEM_ACTIVITIES)) {
                    flag = "1";
                    PartPlanConfirmDao partPlanConfirmDao = PartPlanConfirmDao.getInstance();
                    for (Map<String, Object> map : detailList) {
                        String partId = map.get("PART_ID").toString();
                        List<Map<String, Object>> venderList = partPlanConfirmDao.getVender("", partId);
                        map.put("venderList", venderList);
                    }
                } else {
                    flag = "2";
                }
            }

            String oIds1 = arrToString(orderIds);//订单id
            String dIds1 = arrToString(dealerIds);//订货单位id
            String wIds1 = arrToString(whIds);//仓库id
            String ids1 = arrToString(boIds);//bo单id
//            String oCodes1 = arrToString(orderCodes);//订单号
            String oCodes1 = "";//订单号

            int len = boIds.length;

//            List list = CommonUtils.getPartUnitList(Constant.FIXCODE_TYPE_04);// 获取配件发运方式
            PartTransPlanDao daoPlan=PartTransPlanDao.getInstance();
            List<Map<String, Object>> list = daoPlan.getTransportType();// 发运类型
            request.setAttribute("transList", list);
            // request.setAttribute("wareHouseList", wareHouseList);
            //request.setAttribute("historyList", historyList);
            request.setAttribute("mainMap", mainMap);
            request.setAttribute("detailList", detailList);
            request.setAttribute("flag", flag);
            request.setAttribute("warehouseDefinePO", warehouseDefinePO);
            request.setAttribute("ids", ids1);
            request.setAttribute("oIds", oIds1);
            request.setAttribute("dIds", dIds1);
            request.setAttribute("wIds", wIds1);
            request.setAttribute("oCodes", oCodes1);
            request.setAttribute("whId", whId);
            request.setAttribute("boId", boId);
            request.setAttribute("orderCode", orderCode);
            request.setAttribute("orderId", orderId);
            request.setAttribute("len", len);

            act.setForword(PART_BO_TOSALORDER_CHK_URL);
        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "转销售单失败,请联系管理员!");
            logger.error(loginUser, e1);
            act.setException(e1);
            act.setForword(PART_BOHANDLE_QUERY_URL);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-6-29
     * @Title :
     * @Description:汇总 转销售单审核初始化
     */
    public void toSalOrderAllInit() {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        PartDlrOrderDao partDlrOrderDao = PartDlrOrderDao.getInstance();
        try {

            String firstFlag = CommonUtils.checkNull(request.getParamValue("FIRST_FLAG"));//是否为初次汇总转销售单
            String accountPurpose = CommonUtils.checkNull(request.getParamValue("ACCOUNT_PURPOSE"));
            String boParamsStr = request.getParamValue("BO_PARAMS");//BO单参数
            String[] boParams = boParamsStr.split("x");
            String boParam = boParams[0];
            String boIdsStr = "";
            String boId = "";

            if ("1".equals(firstFlag)) {
                String[] allBoIds1 = request.getParamValues("boIds" + boParam);
                StringBuffer buffer1 = new StringBuffer();
                for (int i = 0; i < allBoIds1.length; i++) {
                    if (allBoIds1[i] != null && !"".equals(allBoIds1[i])) {
                        buffer1.append(allBoIds1[i]).append(",");
                        boId = allBoIds1[i];
                    }
                }
                if (buffer1.length() > 0) {
                    buffer1.deleteCharAt(buffer1.length() - 1);
                    boIdsStr = buffer1.toString();
                }

                StringBuffer buffer = new StringBuffer();
                for (int i = 1; i < boParams.length; i++) {
                    String[] allBoIds = request.getParamValues("boIds" + boParams[i]);
                    StringBuffer boIdsBuffer = new StringBuffer();
                    for (int j = 0; j < allBoIds.length; j++) {
                        if (allBoIds[j] != null && !"".equals(allBoIds[j])) {
                            boIdsBuffer.append(allBoIds[j]).append(",");
                        }
                    }
                    boIdsBuffer.deleteCharAt(boIdsBuffer.length() - 1);
                    buffer.append(boIdsBuffer.toString()).append("y").append(boParams[i]).append(";");
                }
                if (buffer.toString().length() > 0) {
                    buffer.deleteCharAt(buffer.length() - 1);
                    request.setAttribute("ALLBO_PARAMS", buffer.toString());
                }
            } else {
                String allBoParamsStr = request.getParamValue("ALLBO_PARAMS");
                String[] allBoParams = allBoParamsStr.split(";");
                String allBoParam = allBoParams[0];
                boIdsStr = allBoParam.substring(0, allBoParam.indexOf("y" + boParams[0]));
                boId = boIdsStr.split(",")[0];
                request.setAttribute("ALLBO_PARAMS", this.arrToString3(allBoParams));
            }

            String dealerId = boParam.split(",")[0];//订货单位id
            String orderType = boParam.split(",")[1];//订单类型

            TtPartBoMainPO boMainPO = new TtPartBoMainPO();
            boMainPO.setBoId(CommonUtils.parseLong(boId));
            boMainPO = (TtPartBoMainPO) dao.select(boMainPO).get(0);

            Map<String, Object> mainMap = partDlrOrderDao.queryPartDlrOrderMain(boMainPO.getOrderId().toString());
            String flag = "";
            Long whId = ((BigDecimal) mainMap.get("WH_ID")).longValue();
            TtPartWarehouseDefinePO warehouseDefinePO = new TtPartWarehouseDefinePO();
            warehouseDefinePO.setWhId(whId);
            warehouseDefinePO = (TtPartWarehouseDefinePO) dao.select(warehouseDefinePO).get(0);

            //折扣率
            String discount = partDlrOrderDao.getDiscount(mainMap.get("DEALER_ID").toString());
            mainMap.put("discount", discount);
            mainMap.put("saleName", loginUser.getName());
            //获取账户
            Map<String, Object> accountMap = partDlrOrderDao.getAccountMoney(mainMap.get("DEALER_ID").toString(), mainMap.get("SELLER_ID").toString(), accountPurpose);
            String accountSumNow = "";
            String accountKyNow = "";
            String accountDjNow = "";
            String accountId = "";
            boolean accountFlag = false;
            if (null != accountMap) {
                accountFlag = true;
                accountSumNow = accountMap.get("ACCOUNT_SUM").toString();
                accountKyNow = accountMap.get("ACCOUNT_KY").toString();
                accountDjNow = accountMap.get("ACCOUNT_DJ").toString();
                accountId = accountMap.get("ACCOUNT_ID").toString();
            }
            mainMap.put("accountFlag", accountFlag);
            mainMap.put("accountSumNow", accountSumNow);
            mainMap.put("accountKyNow", accountKyNow);
            mainMap.put("accountDjNow", accountDjNow);
            mainMap.put("accountId", accountId);

            //获取bo单中的配件信息
            List<Map<String, Object>> detailList = dao.queryBoDtlDetailAll(boIdsStr, dealerId, loginUser, whId.toString());

            for (int i = 0; i < detailList.size(); i++) {
                Map<String, Object> deMap = detailList.get(i);
                int boTosales = ((BigDecimal) deMap.get("BO_TOSALES")).intValue();
                float allotRatio = ((BigDecimal) deMap.get("ALLOT_RATIO")).floatValue();
                int allotNum = ((BigDecimal) deMap.get("ALLOT_NUM")).intValue();
                int normalQty = ((BigDecimal) deMap.get("NORMAL_QTY")).intValue();

                if (boTosales == Constant.PART_BASE_FLAG_NO.intValue()) {//如果该配件不能转销售
                    detailList.remove(i);
                } else {
                    if (allotRatio != 0) {//如果有资源分配比列
                        normalQty = (int) Math.floor(Arith.mul(normalQty, allotRatio));
                        deMap.put("NORMAL_QTY", normalQty);
                        detailList.set(i, deMap);
                    }
                    if (allotNum != 0) {//如果有分配数量
                        normalQty = allotNum;
                        deMap.put("NORMAL_QTY", normalQty);
                        detailList.set(i, deMap);
                    }
                }
            }

            for (int i = 0; i < detailList.size(); i++) {
                Map<String, Object> deMap = detailList.get(i);

                Long partId = ((BigDecimal) deMap.get("PART_ID")).longValue();

                StringBuffer buffer = new StringBuffer("");
                //获取当前配件对应的所有订单号
                List<DynaBean> orderCodeList = dao.queryOrderCode(partId, boIdsStr);
                for (DynaBean bean : orderCodeList) {
                    String orderCode = (String) bean.get("ORDER_CODE");
                    //buffer.append(orderCode.substring(11) + ",");
                    buffer.append(orderCode + ",");
                }
                String orderCode = buffer.deleteCharAt(buffer.length() - 1).toString();
                deMap.put("code", orderCode);
            }

            if (CommonUtils.checkNull(mainMap.get("ORDER_TYPE")).equals(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_04 + "")) {
                if (dealerId.equals(Constant.OEM_ACTIVITIES)) {
                    flag = "1";
                    PartPlanConfirmDao partPlanConfirmDao = PartPlanConfirmDao.getInstance();
                    for (Map<String, Object> map : detailList) {
                        String partId = map.get("PART_ID").toString();
                        List<Map<String, Object>> venderList = partPlanConfirmDao.getVender("", partId);
                        map.put("venderList", venderList);
                    }
                } else {
                    flag = "2";
                }
            }

            String uBoParams = arrToString1(boParams);//BO单参数

            List list = CommonUtils.getPartUnitList(Constant.FIXCODE_TYPE_04);// 获取配件发运方式
            request.setAttribute("transList", list);

            request.setAttribute("mainMap", mainMap);
            request.setAttribute("detailList", detailList);
            request.setAttribute("warehouseDefinePO", warehouseDefinePO);
            request.setAttribute("BO_PARAMS", uBoParams);
            request.setAttribute("whId", whId);
            request.setAttribute("orderType", orderType);
            request.setAttribute("boIdsStr", boIdsStr);
            request.setAttribute("len", boParams.length);

            act.setForword(PART_BO_TOSALORDER_CHKALL_URL);
        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "转销售单失败,请联系管理员!");
            logger.error(loginUser, e1);
            act.setException(e1);
            act.setForword(PART_BOHANDLE_QUERY_URL);
        }
    }

    //数组转字符串
    private String arrToString(String[] ids) {
        StringBuffer sb = new StringBuffer();
        for (int i = 1; i < ids.length; i++) {
            sb.append(ids[i] + ",");
        }
        if (sb.length() > 0) {
            sb.deleteCharAt(sb.length() - 1);
        }
        return sb.toString();
    }

    //数组转字符串
    private String arrToString1(String[] ids) {
        StringBuffer sb = new StringBuffer();
        for (int i = 1; i < ids.length; i++) {
            sb.append(ids[i] + "x");
        }
        if (sb.length() > 0) {
            sb.deleteCharAt(sb.length() - 1);
        }
        return sb.toString();
    }

    //数组转字符串
    private String arrToString2(String[] ids) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < ids.length; i++) {
            sb.append(ids[i] + ",");
        }
        if (sb.length() > 0) {
            sb.deleteCharAt(sb.length() - 1);
        }
        return sb.toString();
    }

    //数组转字符串
    private String arrToString3(String[] ids) {
        StringBuffer sb = new StringBuffer();
        for (int i = 1; i < ids.length; i++) {
            sb.append(ids[i] + ";");
        }
        if (sb.length() > 0) {
            sb.deleteCharAt(sb.length() - 1);
        }
        return sb.toString();
    }

    /**
     * @param : @param orgAmt
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-4-3
     * @Title :
     * @Description:生成销售单
     */
    public void createSaleOrder() {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        PartDlrOrderDao dao = PartDlrOrderDao.getInstance();
        try {
            request.setAttribute("boFlag", false);

            String ids = CommonUtils.checkNull(request.getParamValue("ids"));//bo单id
            String oIds = CommonUtils.checkNull(request.getParamValue("oIds"));//订单id
            String dIds = CommonUtils.checkNull(request.getParamValue("dIds"));//订货单位id
            String wIds = CommonUtils.checkNull(request.getParamValue("wIds"));//仓库id
            String oCodes = CommonUtils.checkNull(request.getParamValue("oCodes"));//订单单号
            String bo_params = CommonUtils.checkNull(request.getParamValue("BO_PARAMS"));//汇总转销售bo单参数

            int len = 0;
            if (!"".equals(dIds)) {
                len = dIds.split(",").length;
            }
            if (!"".equals(bo_params)) {
                len = bo_params.split("x").length;
            }
            //插入数据
            Long soId = this.insertSoMain();

            String orderType = CommonUtils.checkNull(request.getParamValue("ORDER_TYPE"));
            //如果直发   直接推送销售单到已发运状态
            /*if (orderType.equals(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_04 + "")) {
                if (len == 0) {
                    act.setOutData("success", "操作成功,销售单号为：" + CommonUtils.checkNull(request.getAttribute("soCode")) + "!");
                } else {
                    act.setOutData("success", "操作成功,销售单号为：" + CommonUtils.checkNull(request.getAttribute("soCode")) + ",还可以生成" + len + "个销售单!");
                }
                return;
            } 20170901屏蔽*/

            TtPartSoMainPO mainPO = new TtPartSoMainPO();
            mainPO.setSoId(soId);
            List<TtPartSoMainPO> mainList = dao.select(mainPO);

            TtPartSoDtlPO dtlPO = new TtPartSoDtlPO();
            dtlPO.setSoId(soId);
            List<TtPartSoDtlPO> dtlList = dao.select(dtlPO);
            if (dtlList.size() <= 0 || mainList.size() <= 0) {
                BizException e1 = new BizException(act, new Exception(), ErrorCodeConstant.SPECIAL_MEG, "生成销售单错误,请联系管理员!");
                throw e1;
            }

            //调用库存占用逻辑
//            ArrayList ins = new ArrayList();
//            ins.add(0, soId);
//            ins.add(1, Constant.PART_CODE_RELATION_07);
//            ins.add(2, 1);
//            dao.callProcedure("PKG_PART.P_UPDATEPARTSTATE", ins, null);

	          List ins = new ArrayList();
              ins.add(0, soId);
              ins.add(1, Constant.PART_CODE_RELATION_07);
              ins.add(2, 1);
              dao.callProcedure("PROC_TT_PART_UPDATE_PART_STATE", ins, null);
              
            if (len == 0) {
                act.setOutData("success", "操作成功,销售单号为：" + CommonUtils.checkNull(request.getAttribute("soCode")) + ",已转完毕!");
            } else {
                act.setOutData("success", "操作成功,销售单号为：" + CommonUtils.checkNull(request.getAttribute("soCode")) + ",还可以生成" + len + "个销售单!");
            }

            act.setOutData("ids", ids);
            act.setOutData("oIds", oIds);
            act.setOutData("dIds", dIds);
            act.setOutData("wIds", wIds);
            act.setOutData("oCodes", oCodes);
            act.setOutData("len", len);
        } catch (Exception e) {
            if (e instanceof BizException) {
                BizException e1 = (BizException) e;
                logger.error(loginUser, e1);
                act.setException(e1);
                return;
            }
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "生成销售单错误,请联系管理员!");
            logger.error(loginUser, e1);
            act.setException(e1);
        }
    }

    public Double reCountMoney(Double buyQty, Double price) {
        Double money = Arith.mul(Double.valueOf(buyQty), Double.valueOf(price));
        return money;
    }

    /**
     * @param : @param orgAmt
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-4-3
     * @Title :
     * @Description:插入销售订单主表
     */
    private Long insertSoMain() throws Exception {
        try {
            ActionContext act = ActionContext.getContext();
            RequestWrapper request = act.getRequest();
            AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
            Long soId = Long.valueOf(SequenceManager.getSequence(""));

            String isAll = CommonUtils.checkNull(request.getParamValue("isAll"));

            String soCode = "";
            if (loginUser.getDealerId() == null) {
                soCode = OrderCodeManager.getOrderCode(Constant.PART_CODE_RELATION_07, request.getParamValue("dealerId"));
            } else {
                soCode = OrderCodeManager.getOrderCode(Constant.PART_CODE_RELATION_11, request.getParamValue("dealerId"));
            }
            request.setAttribute("soCode", soCode);
            String orderType = CommonUtils.checkNull(request.getParamValue("ORDER_TYPE")); //订单类型
            String payType = CommonUtils.checkNull(request.getParamValue("PAY_TYPE")); //付费方式
            String dealerId = CommonUtils.checkNull(request.getParamValue("dealerId")); //订货单位ID
            String dealerCode = CommonUtils.checkNull(request.getParamValue("dealerCode")); //订货单位CODE
            String dealerName = CommonUtils.checkNull(request.getParamValue("dealerName")); //订货单位CODE
            String sellerId = CommonUtils.checkNull(request.getParamValue("sellerId")); //销售单位ID
            String sellerCode = CommonUtils.checkNull(request.getParamValue("sellerCode")); //销售单位CODE
            String sellerName = CommonUtils.checkNull(request.getParamValue("sellerName")); //销售单位NAME
            String buyerId = CommonUtils.checkNull(request.getParamValue("buyerId")); //订货人ID
            String buyerName = CommonUtils.checkNull(request.getParamValue("buyerName")); //订货人
            String consignees = CommonUtils.checkNull(request.getParamValue("RCV_ORG")); //接收单位
            String consigneesId = CommonUtils.checkNull(request.getParamValue("RCV_ORGID")); //接收单位ID
            String addr = CommonUtils.checkNull(request.getParamValue("ADDR")); //接收地址ID
            String addrId = CommonUtils.checkNull(request.getParamValue("ADDR_ID")); //接收地址ID
            String receiver = CommonUtils.checkNull(request.getParamValue("RECEIVER")); //接收人
            String tel = CommonUtils.checkNull(request.getParamValue("TEL")); //接收人电话
            String postCode = CommonUtils.checkNull(request.getParamValue("POST_CODE")); //接收邮编
            String station = CommonUtils.checkNull(request.getParamValue("STATION")); //接收站
            String transType = CommonUtils.checkNull(request.getParamValue("TRANS_TYPE")); //发运方式
            String transpayType = CommonUtils.checkNull(request.getParamValue("transpayType")); //发运付费方式
            String sAmount = request.getParamValue("Amount");
            Double amount = Double.valueOf(sAmount.replaceAll(",", "")); //获取JAVA重新计算的金额
            String discount = CommonUtils.checkNull(request.getParamValue("DISCOUNT")); //折扣
            String remark = CommonUtils.checkNull(request.getParamValue("REMARK")); //订单备注
            String remark2 = CommonUtils.checkNull(request.getParamValue("REMARK2")); //备注
            String whId = CommonUtils.checkNull(request.getParamValue("whId")); //仓库id
            String accountId = CommonUtils.checkNull(request.getParamValue("accountId")); //accountId
            String orderId = CommonUtils.checkNull(request.getParamValue("orderId")); //经销商订单id
            String orderCode = CommonUtils.checkNull(request.getParamValue("orderCode")); //经销商订单code

            int state = Constant.CAR_FACTORY_ORDER_CHECK_STATE_05;//转销售后直接财务审核通过

            TtPartSoMainPO po = new TtPartSoMainPO();
            po.setSoId(soId);
            po.setSoFrom(Constant.CAR_FACTORY_SO_FORM_04);//BO单
            po.setSoCode(soCode);
            po.setIsBatchso(Constant.PART_BASE_FLAG_NO);
            po.setOrderId(Long.parseLong(orderId));//20170824 add start
            po.setOrderCode(orderCode);//20170824 add start
            po.setOrderType(Integer.valueOf(orderType));
            po.setPayType(Integer.valueOf(payType));
            po.setDealerId(Long.valueOf(dealerId));
            po.setDealerCode(dealerCode);
            po.setDealerName(dealerName);
            po.setSellerId(Long.valueOf(sellerId));
            po.setSellerCode(sellerCode);
            po.setSellerName(sellerName);
            po.setSaleDate(new Date());
            if (buyerId != null && !"".equals(buyerId)) {
                po.setBuyerId(Long.valueOf(buyerId));
            }
            po.setBuyerName(buyerName);
            po.setConsigneesId(Long.valueOf(consigneesId));
            po.setConsignees(consignees);
            po.setAddr(addr);
            po.setAddrId(Long.valueOf(addrId));
            po.setReceiver(receiver);
            po.setTel(tel);
            po.setPostCode(postCode);
            po.setStation(station);
            po.setTransType(transType);
            po.setTranspayType(Integer.valueOf(transpayType));
            po.setAmount(amount);
            po.setDiscount(Double.valueOf(discount));
            po.setRemark(remark);
            po.setRemark2(remark2);
            po.setWhId(Long.valueOf(whId));
            po.setIsTransfree(Constant.IF_TYPE_YES);//免费
            po.setFreight(0d);//运费为0
            po.setVer(1);
            po.setState(state);
            po.setSubmitBy(loginUser.getUserId());//提交人
            po.setSubmitDate(new Date());//提交日期
            po.setCreateBy(loginUser.getUserId());
            po.setCreateDate(new Date());
            po.setAccountId(Long.valueOf(accountId));
            po.setCheckId(soId);//唯一校验码
            dao.insert(po);

            if (isAll.equals("1")) {//如果是汇总转销售
                this.insertOrderDetailAll(soId, soCode,Long.parseLong(orderId));
            } else {
                this.insertOrderDetail(soId, soCode,Long.parseLong(orderId));
            }
            return soId;
        } catch (Exception ex) {
            throw ex;
        }
    }

    /**
     * @param : @param orgAmt
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-4-3
     * @Title :
     * @Description:插入销售订单详细表
     */
    private void insertOrderDetail(Long soId, String soCode,Long orderId) throws Exception {
        try {
            ActionContext act = ActionContext.getContext();
            RequestWrapper request = act.getRequest();
            AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
            String whId = request.getParamValue("wh_id");

            //当前bo单id
            String boId = CommonUtils.checkNull(request.getParamValue("boId"));

            String orderCode = CommonUtils.checkNull(request.getParamValue("orderCode"));

            PartDlrOrderDao dao1 = PartDlrOrderDao.getInstance();

            Double reCountMoney = 0d;
            Double discount = Double.valueOf(request.getParamValue("DISCOUNT")); //折扣

            String[] partIdArr = request.getParamValues("cb");
            PartPlanConfirmDao partPlanConfirmDao = PartPlanConfirmDao.getInstance();

            String orgId = "";
            if (loginUser.getDealerId() == null) {
                orgId = loginUser.getOrgId() + "";
            } else {
                orgId = loginUser.getDealerId();
            }
            //Map<String,Long> normalQtyMap = new HashMap<String, Long>();
            Map<String, Long> salQtyMap = new HashMap<String, Long>();
            for (int i = 0; i < partIdArr.length; i++) {
                String partId = partIdArr[i];
                //获取库存信息
                Map<String, Object> map = dao.queryPartStockInfo(whId, partIdArr[i], orgId);

                float allotRatio = ((BigDecimal) map.get("ALLOT_RATIO")).floatValue();
                int allotNum = ((BigDecimal) map.get("ALLOT_NUM")).intValue();
                int normalQty = ((BigDecimal) map.get("NORMAL_QTY")).intValue();

                if (allotRatio != 0) {//如果有资源分配比列
                    normalQty = (int) Math.floor(Arith.mul(normalQty, allotRatio));
                }
                if (allotNum != 0) {//如果有分配数量
                    normalQty = allotNum;
                }
                int salQty1 = Integer.valueOf(request.getParamValue("saleQty_" + partId));//销售数量

                String partCname = CommonUtils.checkNull(map.get("PART_CNAME"));
                String partOldCode = CommonUtils.checkNull(map.get("PART_OLDCODE"));
                //重新比较库存数量和销售数量
                //如果库存数量为0
                if (normalQty == 0) {
                    BizException e1 = new BizException(act, new Exception(), ErrorCodeConstant.SPECIAL_MEG, "配件【" + partOldCode + "】的可用库存为0,请返回重新选择配件!");
                    throw e1;
                }
                //如果销售数量大于可用数量
                if (salQty1 > normalQty) {
                    BizException e1 = new BizException(act, new Exception(), ErrorCodeConstant.SPECIAL_MEG, "配件【" + partOldCode + "】的可用库存数量小于销售数量,请重新输入销售数量!");
                    throw e1;
                }
                //获取数据生成订单明细
                Long lineId = Long.parseLong(SequenceManager.getSequence(""));

                Double price = parseDouble(request.getParamValue("BUY_PRICE_" + partId));
                Double realPrice = Arith.mul(price, discount);
                //单价和金额重新校验
                if (price == 0d || realPrice == 0d) {
                    BizException e1 = new BizException(act, new RuntimeException(), ErrorCodeConstant.SPECIAL_MEG, "配件【" + partOldCode + "】销售单价不能为零!");
                    throw e1;
                }
                String partCode = CommonUtils.checkNull(map.get("PART_CODE"));
                String unit = CommonUtils.checkNull(map.get("UNIT"));
                String isDirect = CommonUtils.checkNull(map.get("IS_DIRECT"));
                String isPlan = CommonUtils.checkNull(map.get("IS_PLAN"));
                String isLack = CommonUtils.checkNull(map.get("IS_LACK"));
                String isReplaced = CommonUtils.checkNull(map.get("IS_REPLACED"));
                String stockQty = CommonUtils.checkNull(normalQty);
                String isGift = CommonUtils.checkNull(map.get("IS_GIFT"));
                String venderId = CommonUtils.checkNull(request.getParamValue("vender_" + partId));

                TtPartSoDtlPO po = new TtPartSoDtlPO();
                po.setSoId(soId);
                po.setSlineId(lineId);
                po.setOrderId(orderId);//20170901 add
                po.setPartId(Long.valueOf(partId));
                po.setPartOldcode(partOldCode);
                po.setPartCode(partCode);
                po.setPartCname(partCname);
                po.setUnit(unit);
                if (!"".equals(venderId)) {
                    po.setVenderId(Long.valueOf(venderId));
                    List<Map<String, Object>> venderList = partPlanConfirmDao.getVender(venderId, partId);
                    if (null != venderList && venderList.size() > 0 && null != venderList.get(0)) {
                        String venderName = CommonUtils.checkNull(venderList.get(0).get("VENDER_NAME"));
                        po.setVenderName(venderName);
                    }
                }
                if (!"".equals(isDirect)) {
                    po.setIsDirect(Integer.valueOf(isDirect));//直发件，如机油
                }
                if (!"".equals(isPlan)) {
                    po.setIsPlan(Integer.valueOf(isPlan));//大件、占空间（如保险杠）
                }
                if (!"".equals(isLack)) {
                    po.setIsLack(Integer.valueOf(isLack));//紧缺件
                }
                if (!"".equals(isReplaced)) {
                    po.setIsReplaced(Integer.valueOf(isReplaced));//是否替换件
                }
                if (!"".equals(isGift)) {
                    po.setIsGift(Integer.valueOf(isGift));//是否赠品
                }
                po.setStockQty(Long.valueOf(stockQty));
                po.setMinPackage(1l);
                po.setBuyQty(Long.valueOf(request.getParamValue("buyQty_" + partId)));

                po.setBuyPrice(price);

                po.setCreateBy(loginUser.getUserId());
                po.setCreateDate(new Date());
                po.setRemark(orderCode);

                Long salQty = 0l;
                if (salQtyMap.containsKey(partId)) {
                    salQty = salQtyMap.get(partId);
                } else {
                    salQty = Long.valueOf(request.getParamValue("saleQty_" + partId));//销售数量
                }

                Map<String, Object> dtlMap = dao.queryDtlById(boId, partId);
                if (dtlMap != null) {

                    Long boLineId = ((BigDecimal) dtlMap.get("BOLINE_ID")).longValue();
                    Long boOddQty = ((BigDecimal) dtlMap.get("BO_ODDQTY")).longValue();
                    Long toSalQty = ((BigDecimal) dtlMap.get("TOSAL_QTY")).longValue();

                    //更新bo明细表中的剩余bo数量、转销售数量和状态
                    TtPartBoDtlPO dtlPO1 = new TtPartBoDtlPO();
                    TtPartBoDtlPO dtlPO2 = new TtPartBoDtlPO();
                    dtlPO1.setBolineId(boLineId);

                    TtPartBotosoRelationPO relationPO = new TtPartBotosoRelationPO();

                    if (boOddQty > 0) {

                        if (salQty >= boOddQty) {//如果销售数量大于剩余bo数量,那剩余的bo数量就全部转销售

                            po.setSalesQty(boOddQty);

                            Double amount = Arith.mul(realPrice, boOddQty);
                            reCountMoney = Arith.add(reCountMoney, amount);
                            po.setBuyAmount(amount);

                            dtlPO2.setBoOddqty(0l);
                            dtlPO2.setTosalQty(toSalQty + boOddQty);
                            salQty = salQty - boOddQty;
                            salQtyMap.put(partId, salQty);
                            dtlPO2.setStatus(0);
                            dao.update(dtlPO1, dtlPO2);

                            //插入BO单和销售单关系
                            relationPO.setRelationId(CommonUtils.parseLong(SequenceManager.getSequence("")));
                            relationPO.setBoId(CommonUtils.parseLong(boId));
                            relationPO.setSoId(soId);
                            relationPO.setPartId(Long.valueOf(partId));
                            relationPO.setTosalesQty(boOddQty);
                            relationPO.setCreateDate(new Date());
                            relationPO.setCreateBy(loginUser.getUserId());

                            dao.insert(relationPO);
                        } else if (salQty > 0) {//如果还有销售数量可用,但是此时的销售数量已经小于剩余bo数量
                            po.setSalesQty(salQty);
                            Double amount = Arith.mul(realPrice, salQty);
                            reCountMoney = Arith.add(reCountMoney, amount);
                            po.setBuyAmount(amount);

                            dtlPO2.setBoOddqty(boOddQty - salQty);
                            dtlPO2.setTosalQty(toSalQty + salQty);
                            dao.update(dtlPO1, dtlPO2);
                            //插入BO单和销售单关系
                            relationPO.setRelationId(CommonUtils.parseLong(SequenceManager.getSequence("")));
                            relationPO.setBoId(CommonUtils.parseLong(boId));
                            relationPO.setSoId(soId);
                            relationPO.setPartId(Long.valueOf(partId));
                            relationPO.setTosalesQty(salQty);
                            relationPO.setCreateDate(new Date());
                            relationPO.setCreateBy(loginUser.getUserId());

                            dao.insert(relationPO);

                            salQty = 0l;
                            salQtyMap.put(partId, salQty);
                        }
                        dao.insert(po);
                    } else {
                        BizException e1 = new BizException(act, new Exception(), ErrorCodeConstant.SPECIAL_MEG, "配件【" + partCname + "】已转完毕,请返回重新操作!");
                        throw e1;
                    }
                }
            }
            //判断当前bo单是否已经处理完毕,如果处理完毕就更新状态为已处理,否则更新为部分处理
            TtPartBoMainPO smainPO = new TtPartBoMainPO();
            smainPO.setBoId(CommonUtils.parseLong(boId));
            boolean flag = dao.isCloseAll(boId);
            TtPartBoMainPO mainPO = new TtPartBoMainPO();
            if (flag) {
                mainPO.setState(Constant.CAR_FACTORY_SALES_MANAGER_BO_STATE_03);
            } else {
                mainPO.setState(Constant.CAR_FACTORY_SALES_MANAGER_BO_STATE_02);
            }
            dao.update(smainPO, mainPO);

            //将bo单转成销售单之前要先占用金额
            String dealerId = CommonUtils.checkNull(request.getParamValue("dealerId"));
            String parentId = CommonUtils.checkNull(request.getParamValue("sellerId"));
            String orderType = CommonUtils.checkNull(request.getParamValue("ORDER_TYPE"));
            Map<String, Object> acountMap = dao1.getAccount(dealerId, parentId, "");
            //向车厂提报非内部领用订单必须有账户//mod by yuan 20131104
            if (parentId.equals(Constant.OEM_ACTIVITIES + "") && !orderType.equals(Constant.CAR_FACTORY_SALES_MANAGER_ORDER_TYPE_09.toString())) {
                if (null == acountMap) {
                    BizException e1 = new BizException(act, new Exception(), ErrorCodeConstant.SPECIAL_MEG, "余额不足!");
                    throw e1;
                } else {
                    if (!this.validateSum(dealerId, parentId, CommonUtils.checkNull(request.getParamValue("Amount")))) {
                        BizException e1 = new BizException(act, new Exception(), ErrorCodeConstant.SPECIAL_MEG, "余额不足!");
                        throw e1;
                    }
                }
            }/* else {
                //获取账户余额等
                if (null != acountMap) {
                    if (!this.validateSum(dealerId, parentId, CommonUtils.checkNull(request.getParamValue("Amount")))) {
                        BizException e1 = new BizException(act, new Exception(), ErrorCodeConstant.SPECIAL_MEG, "余额不足!");
                        throw e1;
                    }
                }
            }*/
            //资金占用
            this.insertAccount(dealerId, parentId, reCountMoney, Long.valueOf(soId), soCode, loginUser);

            request.setAttribute("reCountMoney", reCountMoney);
        } catch (Exception ex) {
            throw ex;
        }
    }

    /**
     * @param : @param soId
     * @param : @param soCode
     * @param : @throws Exception
     * @return :
     * @throws : LastDate    : 2013-7-25
     * @Title :
     * @Description: 汇总转销售时插入销售明细
     */
    private void insertOrderDetailAll(Long soId, String soCode,Long orderId) throws Exception {
        try {
            ActionContext act = ActionContext.getContext();
            RequestWrapper request = act.getRequest();
            AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
            String whId = request.getParamValue("wh_id");

            //当前bo单id
            String ids = CommonUtils.checkNull(request.getParamValue("ids"));
            String[] boIds = ids.split(",");

            PartDlrOrderDao dao1 = PartDlrOrderDao.getInstance();

            Double reCountMoney = 0d;
            Double discount = Double.valueOf(request.getParamValue("DISCOUNT")); //折扣

            String[] partIdArr = request.getParamValues("cb");
            PartPlanConfirmDao partPlanConfirmDao = PartPlanConfirmDao.getInstance();

            String orgId = "";
            if (loginUser.getDealerId() == null) {
                orgId = loginUser.getOrgId() + "";
            } else {
                orgId = loginUser.getDealerId();
            }

            Map<String, Long> salQtyMap = new HashMap<String, Long>();

            for (int j = 0; j < partIdArr.length; j++) {

                String partId = partIdArr[j];

                //获取库存信息
                Map<String, Object> map = dao.queryPartStockInfo(whId, partId, orgId);

                float allotRatio = ((BigDecimal) map.get("ALLOT_RATIO")).floatValue();
                int allotNum = ((BigDecimal) map.get("ALLOT_NUM")).intValue();
                int normalQty = ((BigDecimal) map.get("NORMAL_QTY")).intValue();

                if (allotRatio != 0) {//如果有资源分配比列
                    normalQty = (int) Math.floor(Arith.mul(normalQty, allotRatio));
                }
                if (allotNum != 0) {//如果有分配数量
                    normalQty = allotNum;
                }

                int salQty1 = Integer.valueOf(request.getParamValue("saleQty_" + partId));//销售数量

                String partCname = CommonUtils.checkNull(map.get("PART_CNAME"));

                //重新比较库存数量和销售数量
                if (normalQty == 0) {//如果库存数量为0
                    BizException e1 = new BizException(act, new Exception(), ErrorCodeConstant.SPECIAL_MEG, "配件【" + partCname + "】的可用库存为0,请返回重新选择配件!");
                    throw e1;
                }

                if (salQty1 > normalQty) {//如果销售数量大于可用数量
                    BizException e1 = new BizException(act, new Exception(), ErrorCodeConstant.SPECIAL_MEG, "配件【" + partCname + "】的可用库存数量小于销售数量,请重新输入销售数量!");
                    throw e1;
                }

                //获取当前配件对应的所有订单号
                StringBuffer buffer = new StringBuffer("");
                List<DynaBean> orderCodeList = dao.queryOrderCode(CommonUtils.parseLong(partId), ids);
                for (DynaBean bean : orderCodeList) {
                    String orderCode = (String) bean.get("ORDER_CODE");
                    buffer.append(orderCode + ",");
                }
                String orderCode = buffer.deleteCharAt(buffer.length() - 1).toString();


                //获取数据生成订单明细
                Long lineId = Long.parseLong(SequenceManager.getSequence(""));

                Double price = parseDouble(request.getParamValue("BUY_PRICE_" + partId));
                Double realPrice = Arith.mul(price, discount);
                String partCode = CommonUtils.checkNull(map.get("PART_CODE"));
                String partOldCode = CommonUtils.checkNull(map.get("PART_OLDCODE"));
                String unit = CommonUtils.checkNull(map.get("UNIT"));
                String isDirect = CommonUtils.checkNull(map.get("IS_DIRECT"));
                String isPlan = CommonUtils.checkNull(map.get("IS_PLAN"));
                String isLack = CommonUtils.checkNull(map.get("IS_LACK"));
                String isReplaced = CommonUtils.checkNull(map.get("IS_REPLACED"));
                String stockQty = CommonUtils.checkNull(normalQty);
                String isGift = CommonUtils.checkNull(map.get("IS_GIFT"));
                String venderId = CommonUtils.checkNull(request.getParamValue("vender_" + partId));

                TtPartSoDtlPO po = new TtPartSoDtlPO();
                po.setSoId(soId);
                po.setSlineId(lineId);
                po.setOrderId(orderId);
                po.setPartId(Long.valueOf(partId));
                po.setPartOldcode(partOldCode);
                po.setPartCode(partCode);
                po.setPartCname(partCname);
                po.setUnit(unit);
                if (!"".equals(venderId)) {
                    po.setVenderId(Long.valueOf(venderId));
                    List<Map<String, Object>> venderList = partPlanConfirmDao.getVender(venderId, partId);
                    if (null != venderList && venderList.size() > 0 && null != venderList.get(0)) {
                        String venderName = CommonUtils.checkNull(venderList.get(0).get("VENDER_NAME"));
                        po.setVenderName(venderName);
                    }
                }
                if (!"".equals(isDirect)) {
                    po.setIsDirect(Integer.valueOf(isDirect));
                }
                if (!"".equals(isPlan)) {
                    po.setIsPlan(Integer.valueOf(isPlan));
                }
                if (!"".equals(isLack)) {
                    po.setIsLack(Integer.valueOf(isLack));
                }
                if (!"".equals(isReplaced)) {
                    po.setIsReplaced(Integer.valueOf(isReplaced));
                }
                if (!"".equals(isGift)) {
                    po.setIsGift(Integer.valueOf(isGift));
                }
                po.setStockQty(Long.valueOf(stockQty));
                po.setMinPackage(1l);
                po.setBuyQty(Long.valueOf(request.getParamValue("buyQty_" + partId)));
                po.setBuyPrice(price);

                po.setCreateBy(loginUser.getUserId());
                po.setCreateDate(new Date());
                po.setRemark(orderCode);

                Double amount = 0d;
                Long allSalesQty = 0l;//当前配件对应的所有销售数量

                for (int i = 0; i < boIds.length; i++) {

                    String boId = boIds[i];

                    Long salQty = 0l;

                    if (salQtyMap.containsKey(partIdArr[j])) {
                        salQty = salQtyMap.get(partIdArr[j]);
                    } else {
                        salQty = Long.valueOf(request.getParamValue("saleQty_" + partIdArr[j]));//销售数量
                    }

                    Map<String, Object> dtlMap = dao.queryDtlById(boId, partIdArr[j]);
                    if (dtlMap != null) {

                        Long boLineId = ((BigDecimal) dtlMap.get("BOLINE_ID")).longValue();
                        Long boOddQty = ((BigDecimal) dtlMap.get("BO_ODDQTY")).longValue();
                        Long toSalQty = ((BigDecimal) dtlMap.get("TOSAL_QTY")).longValue();

                        //更新bo明细表中的剩余bo数量、转销售数量和状态
                        TtPartBoDtlPO dtlPO1 = new TtPartBoDtlPO();
                        TtPartBoDtlPO dtlPO2 = new TtPartBoDtlPO();
                        dtlPO1.setBolineId(boLineId);

                        TtPartBotosoRelationPO relationPO = new TtPartBotosoRelationPO();

                        if (boOddQty > 0) {

                            if (salQty >= boOddQty) {//如果销售数量大于剩余bo数量,那剩余的bo数量就全部转销售

                                allSalesQty += boOddQty;

                                dtlPO2.setBoOddqty(0l);
                                dtlPO2.setTosalQty(toSalQty + boOddQty);
                                salQty = salQty - boOddQty;
                                salQtyMap.put(partIdArr[j], salQty);
                                dtlPO2.setStatus(0);
                                dao.update(dtlPO1, dtlPO2);

                                //插入BO单和销售单关系
                                relationPO.setRelationId(CommonUtils.parseLong(SequenceManager.getSequence("")));
                                relationPO.setBoId(CommonUtils.parseLong(boId));
                                relationPO.setSoId(soId);
                                relationPO.setPartId(Long.valueOf(partIdArr[j]));
                                relationPO.setTosalesQty(boOddQty);
                                relationPO.setCreateDate(new Date());
                                relationPO.setCreateBy(loginUser.getUserId());

                                dao.insert(relationPO);
                            } else if (salQty > 0) {//如果还有销售数量可用,但是此时的销售数量已经小于剩余bo数量

                                allSalesQty += salQty;

                                dtlPO2.setBoOddqty(boOddQty - salQty);
                                dtlPO2.setTosalQty(toSalQty + salQty);

                                dao.update(dtlPO1, dtlPO2);

                                //插入BO单和销售单关系
                                relationPO.setRelationId(CommonUtils.parseLong(SequenceManager.getSequence("")));
                                relationPO.setBoId(CommonUtils.parseLong(boId));
                                relationPO.setSoId(soId);
                                relationPO.setPartId(Long.valueOf(partIdArr[j]));
                                relationPO.setTosalesQty(salQty);
                                relationPO.setCreateDate(new Date());
                                relationPO.setCreateBy(loginUser.getUserId());

                                dao.insert(relationPO);

                                salQty = 0l;
                                salQtyMap.put(partIdArr[j], salQty);
                                break;
                            }

                        }

                    }

                }

                if (allSalesQty == 0) {
                    BizException e1 = new BizException(act, new Exception(), ErrorCodeConstant.SPECIAL_MEG, "配件【" + partCname + "】已转完毕,请返回重新操作!");
                    throw e1;
                }

                amount = Arith.mul(realPrice, allSalesQty);
                reCountMoney = Arith.add(reCountMoney, amount);
                po.setSalesQty(allSalesQty);
                po.setBuyAmount(amount);

                dao.insert(po);

            }

            for (int i = 0; i < boIds.length; i++) {

                String boId = boIds[i];

                //判断当前bo单是否已经处理完毕,如果处理完毕就更新状态为已处理,否则更新为部分处理
                TtPartBoMainPO smainPO = new TtPartBoMainPO();
                smainPO.setBoId(CommonUtils.parseLong(boId));
                boolean flag = dao.isCloseAll(boId);
                TtPartBoMainPO mainPO = new TtPartBoMainPO();
                if (flag) {
                    mainPO.setState(Constant.CAR_FACTORY_SALES_MANAGER_BO_STATE_03);
                } else {
                    mainPO.setState(Constant.CAR_FACTORY_SALES_MANAGER_BO_STATE_02);
                }
                dao.update(smainPO, mainPO);
            }

            //将bo单转成销售单之前要先占用金额

            String dealerId = CommonUtils.checkNull(request.getParamValue("dealerId"));
            String parentId = CommonUtils.checkNull(request.getParamValue("sellerId"));
            Map<String, Object> acountMap = dao1.getAccount(dealerId, parentId, "");
            //向车厂提必须有账户
            if (parentId.equals(Constant.OEM_ACTIVITIES + "")) {
                if (null == acountMap) {
                    BizException e1 = new BizException(act, new Exception(), ErrorCodeConstant.SPECIAL_MEG, "余额不足!");
                    throw e1;
                } else {
                    if (!this.validateSum(dealerId, parentId, CommonUtils.checkNull(request.getParamValue("Amount")))) {
                        BizException e1 = new BizException(act, new Exception(), ErrorCodeConstant.SPECIAL_MEG, "余额不足!");
                        throw e1;
                    }
                }
            } else {
                //获取账户余额等
                if (null != acountMap) {
                    if (!this.validateSum(dealerId, parentId, CommonUtils.checkNull(request.getParamValue("Amount")))) {
                        BizException e1 = new BizException(act, new Exception(), ErrorCodeConstant.SPECIAL_MEG, "余额不足!");
                        throw e1;
                    }
                }
            }


            //资金占用
            this.insertAccount(dealerId, parentId, reCountMoney, Long.valueOf(soId), soCode, loginUser);

            request.setAttribute("reCountMoney", reCountMoney);
        } catch (Exception ex) {
            throw ex;
        }
    }

    private Double parseDouble(Object obj) throws Exception {
        ActionContext act = ActionContext.getContext();
        String str = CommonUtils.checkNull(obj);
        try {
            if (str.indexOf(",") > -1) {
                String[] strArr = str.split("\\,");
                str = "";
                for (int i = 0; i < strArr.length; i++) {
                    str += strArr[i];
                }
            }
            return Double.valueOf(str);
        } catch (Exception ex) {
            BizException e = new BizException(act, ex, ErrorCodeConstant.SPECIAL_MEG, "数字转换错误!");
            throw e;
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-4-19
     * @Title :
     * @Description: 校验余额
     */
    public boolean validateSum(String dealerId, String parentId, String sum) throws Exception {
        PartDlrOrderDao dao = PartDlrOrderDao.getInstance();
        Map<String, Object> acountMap = dao.getAccount(dealerId, parentId, "");
        String sumNow = acountMap.get("ACCOUNT_KY").toString();
        sumNow = sumNow.replaceAll(",", "");
        try {
            if (Double.valueOf(sumNow.replace(",", "")) < parseDouble(sum)) {
                return false;
            }
            return true;
        } catch (Exception ex) {
            throw ex;
        }
    }

    /**
     * @param : @param orgAmt
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-4-3
     * @Title :
     * @Description:占用资金
     */
    public void insertAccount(String dealerId, String parentId, Double amount, Long orderId, String orderCode, AclUserBean loginUser) throws Exception {
        try {
            PartDlrOrderDao dlrOrderDao = PartDlrOrderDao.getInstance();
            TtPartAccountRecordPO po = new TtPartAccountRecordPO();
            Long recordId = Long.parseLong(SequenceManager.getSequence(""));
            po.setRecordId(recordId);
            po.setChangeType(Constant.CAR_FACTORY_SALE_ACCOUNT_RECOUNT_TYPE_01);
            po.setDealerId(Long.valueOf(dealerId));
            //获取账户余额等
            Map<String, Object> acountMap = dlrOrderDao.getAccount(dealerId, parentId, "");
            if (null != acountMap) {
                po.setAccountId(Long.valueOf(acountMap.get("ACCOUNT_ID").toString()));
            }
            po.setAmount(amount);
            po.setFunctionName("BO转销售预占");
            po.setSourceId(Long.valueOf(orderId));
            po.setSourceCode(orderCode);
            po.setOrderId(orderId);
            po.setOrderCode(orderCode);
            po.setCreateBy(loginUser.getUserId());
            po.setCreateDate(new Date());
            dlrOrderDao.insert(po);
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * ：add zhumingwei 2013-09-23
     *
     * @param :
     * @return :
     * @throws : LastDate    : 2013-6-28
     * @Title :
     * @Description: 导出汇总信息
     */
    public void expPartBoAllExcel() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            RequestWrapper request = act.getRequest();
            String PARTOLD_NAME = CommonUtils.checkNull(request.getParamValue("PARTOLD_NAME"));
            String PART_CODE = CommonUtils.checkNull(request.getParamValue("PART_CODE"));
            String PARTOLD_CODE = CommonUtils.checkNull(request.getParamValue("PARTOLD_CODE"));
            String desc = CommonUtils.checkNull(request.getParamValue("desc"));
            String startDate = CommonUtils.checkNull(request.getParamValue("startDate"));
            String endDate = CommonUtils.checkNull(request.getParamValue("endDate"));
            String startDate1 = CommonUtils.checkNull(request.getParamValue("startDate1"));
            String endDate1 = CommonUtils.checkNull(request.getParamValue("endDate1"));
            String fliter = CommonUtils.checkNull(request.getParamValue("fliter"));//筛选条件

            String[] head = new String[8];
            head[0] = "配件编码";
            head[1] = "配件名称";
//            head[2] = "件号";
            head[2] = "当前可用库存";
            head[3] = "BO汇总数量";
            head[4] = "有效BO汇总";
            head[5] = "BO汇总次数";
            List<Map<String, Object>> list = dao.queryAllPartBoList(PARTOLD_NAME, PART_CODE, PARTOLD_CODE,
                    desc, startDate, endDate, startDate1, endDate1, logonUser, fliter, 1, Constant.PAGE_SIZE_MAX).getRecords();
            List list1 = new ArrayList();
            if (list != null && list.size() != 0) {
                for (int i = 0; i < list.size(); i++) {
                    Map map = (Map) list.get(i);
                    if (map != null && map.size() != 0) {
                        String[] detail = new String[8];
                        detail[0] = CommonUtils.checkNull(map.get("PART_OLDCODE"));
                        detail[1] = CommonUtils.checkNull(map.get("PART_CNAME"));
//                        detail[2] = CommonUtils.checkNull(map.get("PART_CODE"));
                        detail[2] = CommonUtils.checkNull(map.get("NORMAL_QTY"));
                        detail[3] = CommonUtils.checkNull(map.get("BO_QTY"));
                        detail[4] = CommonUtils.checkNull(map.get("BO_ODDQTY"));
                        detail[5] = CommonUtils.checkNull(map.get("BO_CNT"));
                        list1.add(detail);
                    }
                }
                this.exportEx(ActionContext.getContext().getResponse(), request, head, list1, 1);
            } else {
                BizException e1 = new BizException(act,
                        ErrorCodeConstant.SPECIAL_MEG, "没有满足条件的数据!");
                throw e1;
            }

        } catch (Exception e) {
            BizException e1 = null;
            if (e instanceof BizException) {
                e1 = (BizException) e;
            } else {
                e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "文件下载错误");
            }
            logger.error(logonUser, e1);
            act.setException(e1);
            act.setForword(PART_BO_QUERYALL_URL);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-6-28
     * @Title :
     * @Description: 导出明细信息
     */
    public void expPartBoDtlExcel() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        boolean salerFlag = false;
        PartDlrOrderDao dao1 = PartDlrOrderDao.getInstance();
        List<Map<String, Object>> salerList = dao1.getSaler();
        if (logonUser.getDealerId() == null) {
            salerFlag = true;
        }
        act.setOutData("curUserId", logonUser.getUserId());
        act.setOutData("salerList", salerList);
        act.setOutData("salerFlag", salerFlag);
        try {
            RequestWrapper request = act.getRequest();
            String[] head = new String[15];
            head[0] = "订货单位编码";
            head[1] = "订货单位";
            head[2] = "配件编码";
            head[3] = "配件名称";
            head[4] = "当前可用数量";
            head[5] = "在途数量";
            head[6] = "BO剩余数量";
            head[7] = "订货数量";
            head[8] = "满足数量";
            head[9] = "BO数量";
            head[10] = "转销售数量";
            head[11] = "关闭数量";
            head[12] = "BO金额";
            List<Map<String, Object>> list = dao.queryPartBoDtlList(request, logonUser, 1, 99999).getRecords();
            List list1 = new ArrayList();
            if (list != null && list.size() != 0) {
                for (int i = 0; i < list.size(); i++) {
                    Map map = (Map) list.get(i);
                    if (map != null && map.size() != 0) {
                        String[] detail = new String[15];
                        detail[0] = CommonUtils
                                .checkNull(map.get("DEALER_CODE"));
                        detail[1] = CommonUtils
                                .checkNull(map.get("DEALER_NAME"));
                        detail[2] = CommonUtils
                                .checkNull(map.get("PART_OLDCODE"));
                        detail[3] = CommonUtils
                                .checkNull(map.get("PART_CNAME"));
//                        detail[4] = CommonUtils
//                                .checkNull(map.get("PART_CODE"));
                        detail[4] = CommonUtils.checkNull(map
                                .get("NORMAL_QTY"));
                        detail[5] = CommonUtils.checkNull(map
                                .get("ZT_QTY"));
                        detail[6] = CommonUtils.checkNull(map
                                .get("BO_ODDQTY"));
                        detail[7] = CommonUtils.checkNull(map
                                .get("BUY_QTY"));
                        detail[8] = CommonUtils.checkNull(map
                                .get("SALES_QTY"));
                        detail[9] = CommonUtils.checkNull(map
                                .get("BO_QTY"));
                        detail[10] = CommonUtils.checkNull(map
                                .get("TOSAL_QTY"));
                        detail[11] = CommonUtils.checkNull(map
                                .get("CLOSE_QTY"));
                        detail[12] = CommonUtils.checkNull(map
                                .get("AMOUNT")).replace(",", "");
                        list1.add(detail);
                    }
                }
                this.exportEx(ActionContext.getContext().getResponse(),
                        request, head, list1, 1);
            } else {
                BizException e1 = new BizException(act,
                        ErrorCodeConstant.SPECIAL_MEG, "没有满足条件的数据!");
                throw e1;
            }

        } catch (Exception e) {
            BizException e1 = null;
            if (e instanceof BizException) {
                e1 = (BizException) e;
            } else {
                e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG,
                        "文件下载错误");
            }
            logger.error(logonUser, e1);
            act.setException(e1);
            act.setForword(PART_BO_QUERYDTL_URL);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-12-10
     * @Title : 订单明细导出
     */
    public void exportOrderExcel() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        try {
            String boCode = CommonUtils.checkNull(request.getParamValue("boCode")); // BO单单号
            String boId = CommonUtils.checkNull(request.getParamValue("boId")); // BO单ID
            String orderId = CommonUtils.checkNull(request.getParamValue("orderIdE"));//订单id
            String flag = CommonUtils.checkNull(request.getParamValue("flag"));//是否为bo处理
            String orgId = logonUser.getDealerId() == null ? Constant.OEM_ACTIVITIES : logonUser.getDealerId();
            PageResult<Map<String, Object>> ps = dao.queryPartBoDetailList(boId, orderId, "", "", "", flag, 1, Constant.PAGE_SIZE_MAX, orgId, "");

            String[] head = new String[20];
            head[0] = "序号";
            head[1] = "配件编码";
            head[2] = "配件名称";
            //head[3] = "配件件号";
            head[3] = "单位";
            head[4] = "订货单价";
            head[5] = "订货数量";
            head[6] = "满足数量";
            head[7] = "BO数量";
            head[8] = "转销售数量";
            head[9] = "关闭数量";
            head[10] = "BO剩余数量";
            head[11] = "库存数量";
            head[12] = "生成日期";
            head[13] = "处理状态";
            head[14] = "备注";

            List<Map<String, Object>> list = ps.getRecords();
            List list1 = new ArrayList();
            if (list != null && list.size() != 0) {
                for (int i = 0; i < list.size(); i++) {
                    Map map = (Map) list.get(i);
                    if (map != null && map.size() != 0) {
                        String[] detail = new String[20];
                        detail[0] = CommonUtils.checkNull(i + 1);
                        detail[1] = CommonUtils.checkNull(map
                                .get("PART_OLDCODE"));
                        detail[2] = CommonUtils.checkNull(map
                                .get("PART_CNAME"));
//                        detail[3] = CommonUtils.checkNull(map
//                                .get("PART_CODE"));
                        detail[3] = CommonUtils.checkNull(map
                                .get("UNIT"));
                        detail[4] = CommonUtils.checkNull(map
                                .get("BUY_PRICE"));
                        detail[5] = CommonUtils.checkNull(map
                                .get("BUY_QTY"));
                        detail[6] = CommonUtils.checkNull(map
                                .get("SALES_QTY"));
                        detail[7] = CommonUtils.checkNull(map
                                .get("BO_QTY"));
                        detail[8] = CommonUtils
                                .checkNull(map.get("TOSAL_QTY"));
                        detail[9] = CommonUtils
                                .checkNull(map.get("CLOSE_QTY"));
                        detail[10] = CommonUtils
                                .checkNull(map.get("BO_ODDQTY"));
                        detail[11] = CommonUtils
                                .checkNull(map.get("NORMAL_QTY"));
                        detail[12] = CommonUtils
                                .checkNull(map.get("CREATE_DATE"));
                        detail[13] = CommonUtils
                                .checkNull(map.get("STATUS"));
                        detail[14] = CommonUtils
                                .checkNull(map.get("REMARK"));

                        list1.add(detail);
                    }
                }
            }

            String fileName = "BO单[" + boCode + "]明细";
            this.exportEx(fileName, ActionContext.getContext().getResponse(),
                    request, head, list1);

        } catch (Exception e) {
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.SPECIAL_MEG, "导出采购订单明细失败");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }
}
