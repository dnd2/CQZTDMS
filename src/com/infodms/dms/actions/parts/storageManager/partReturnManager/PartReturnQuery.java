package com.infodms.dms.actions.parts.storageManager.partReturnManager;

import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.bean.OrgBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.constants.PTConstants;
import com.infodms.dms.dao.parts.baseManager.partsBaseManager.PartWareHouseDao;
import com.infodms.dms.dao.parts.storageManager.partReturnManager.PartDlrReturnApplyDAO;
import com.infodms.dms.dao.parts.storageManager.partReturnManager.PartReturnPrintDao;
import com.infodms.dms.dao.parts.storageManager.partReturnManager.PartReturnQueryDao;
import com.infodms.dms.dao.parts.storageManager.partReturnManager.ProcurementReturnPrintDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TmDealerPO;
import com.infodms.dms.po.TtPartReturnRecordPO;
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
import java.util.*;

/**
 * @author : chenjunjiang
 *         CreateDate     : 2013-5-1
 * @ClassName : PartReturnQuery
 * @Description : 退貨查詢
 */
public class PartReturnQuery implements PTConstants {

    public Logger logger = Logger.getLogger(PartReturnQuery.class);
    private PartReturnQueryDao dao = PartReturnQueryDao.getInstance();
    private static final Integer PRINT_SIZE = 10;
    private static final String pkgPrintUrl = null;
    private static final String returnId = null;


    //导出EXCLE表格
    public static Object exportEx(ResponseWrapper response,
                                  RequestWrapper request, String[] head, List<String[]> list, String flag)
            throws Exception {

        String name = "";
        if ("1".equals(flag)) {
            name = "销售退货信息.xls";
        } else {
            name = "采购退货信息.xls";
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
     * @param :
     * @return :
     * @throws : LastDate    : 2013-5-1
     * @Title :
     * @Description: 查询初始化
     */
    public void queryPartReturnInit() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            act.setOutData("old", CommonUtils.getBefore(new Date()));
            act.setOutData("now", CommonUtils.getDate());
            act.setForword(PART_RETURN_QUERY_URL);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "退货信息");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-5-1
     * @Title :
     * @Description: 查询退货信息
     */
    public void queryPartReturnInfo() {

        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            String returnCode = CommonUtils.checkNull(request.getParamValue("RETURN_CODE"));//退货单号
            String startDate = CommonUtils.checkNull(request.getParamValue("startDate"));//退货开始时间
            String endDate = CommonUtils.checkNull(request.getParamValue("endDate"));//退货结束时间
            String partCode = CommonUtils.checkNull(request.getParamValue("PART_CODE"));//件号
            String partOldCode = CommonUtils.checkNull(request.getParamValue("PART_OLDCODE"));//配件编码
            String partCname = CommonUtils.checkNull(request.getParamValue("PART_CNAME"));//配件名称
            String returnType = CommonUtils.checkNull(request.getParamValue("RETURN_TYPE"));//退货类型

            TtPartReturnRecordPO po = new TtPartReturnRecordPO();
            po.setReturnCode(returnCode);
            po.setPartCode(partCode);
            po.setPartOldcode(partOldCode);
            po.setPartCname(partCname);
            if (!"".equals(returnType)) {
                po.setReturnType(CommonUtils.parseInteger(returnType));
            }

            //分页方法 begin
            Integer curPage = request.getParamValue("curPage") != null ? Integer
                    .parseInt(request.getParamValue("curPage"))
                    : 1; // 处理当前页
            PageResult<Map<String, Object>> ps = dao.queryPartReturnList(po, startDate, endDate, curPage, Constant.PAGE_SIZE);
            //分页方法 end
            act.setOutData("ps", ps);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "退货信息");
            logger.error(logonUser, e1);
            act.setException(e1);
        }

    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-7-18
     * @Title :
     * @Description: 销售退货查询初始化
     */
    public void queryPartDlrReturnApplyInit() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            act.setOutData("old", CommonUtils.getBefore(new Date()));
            act.setOutData("now", CommonUtils.getDate());

            act.setForword(PART_DLRRETURN_QUERY_URL);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "销售退货");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-7-18
     * @Title :
     * @Description: 销售退货查询
     */
    public void queryPartDlrReturnInfo() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            PartDlrReturnApplyDAO dao2 = PartDlrReturnApplyDAO.getInstance();
            String returnCode = CommonUtils.checkNull(request.getParamValue("RETURN_CODE"));//退货单号
            String dealerName = CommonUtils.checkNull(request.getParamValue("DEALER_NAME"));//退货单位
            String startDate = CommonUtils.checkNull(request.getParamValue("startDate_y"));//验收开始时间
            String endDate = CommonUtils.checkNull(request.getParamValue("endDate_y"));//验收结束时间
            String partCode = CommonUtils.checkNull(request.getParamValue("PART_CODE"));//件号
            String partOldCode = CommonUtils.checkNull(request.getParamValue("PART_OLDCODE"));//配件编码
            String partCname = CommonUtils.checkNull(request.getParamValue("PART_CNAME"));//配件名称
            String radioSelect = CommonUtils.checkNull(request.getParamValue("RADIO_SELECT"));
            String state = CommonUtils.checkNull(request.getParamValue("state"));
            String isWo = CommonUtils.checkNull(request.getParamValue("is_wo"));//是否已冲销

            String startDate_t = CommonUtils.checkNull(request.getParamValue("startDate_t"));//提交开始时间
            String endDate_t = CommonUtils.checkNull(request.getParamValue("endDate_t"));//提交结束时间
            String startDate_s = CommonUtils.checkNull(request.getParamValue("startDate_s"));//审核开始时间
            String endDate_s = CommonUtils.checkNull(request.getParamValue("endDate_s"));//审核结束时间
            String startDate_h = CommonUtils.checkNull(request.getParamValue("startDate_h"));//回运开始时间
            String endDate_h = CommonUtils.checkNull(request.getParamValue("endDate_h"));//回运结束时间
            
            Map<String, String> paramMap = new HashMap<String, String>();
            paramMap.put("returnCode", returnCode);
            paramMap.put("dealerName", dealerName);
            paramMap.put("startDate", startDate);
            paramMap.put("endDate", endDate);
            paramMap.put("partCode", partCode);
            paramMap.put("partOldCode", partOldCode);
            paramMap.put("partCname", partCname);
            paramMap.put("radioSelect", radioSelect);
            paramMap.put("state", state);
            paramMap.put("isWo", isWo);
            paramMap.put("startDate_t", startDate_t);
            paramMap.put("endDate_t", endDate_t);
            paramMap.put("startDate_s", startDate_s);
            paramMap.put("endDate_s", endDate_s);
            paramMap.put("startDate_h", startDate_h);
            paramMap.put("endDate_h", endDate_h);


            String dealerId = "";
            //判断是否为车厂  PartWareHouseDao
            PartWareHouseDao partWareHouseDao = PartWareHouseDao.getInstance();
            List<OrgBean> beanList = partWareHouseDao.getOrgInfo(logonUser);
            if (null != beanList || beanList.size() >= 0) {
                dealerId = beanList.get(0).getOrgId() + "";

            }
            int flag = 0;//默认是车厂,0表示车厂,1表示供应中心,2表示一般服务商
            if (logonUser.getDealerId() != null) {//如果不是车厂
                flag = 1;
                TmDealerPO tmDealerPO = new TmDealerPO();
                tmDealerPO.setDealerId(CommonUtils.parseLong(logonUser.getDealerId()));
                tmDealerPO = (TmDealerPO) dao.select(tmDealerPO).get(0);
                if (tmDealerPO.getPdealerType().intValue() != Constant.PART_SALE_PRICE_DEALER_TYPE_01.intValue()) {//如果不是供应中心
                    flag = 2;
                }
            }
            paramMap.put("flag", String.valueOf(flag));
            TtPartReturnRecordPO po = new TtPartReturnRecordPO();
            po.setReturnCode(returnCode);
            po.setPartCode(partCode);
            po.setPartOldcode(partOldCode);
            po.setPartCname(partCname);

            //分页方法 begin
            Integer curPage = request.getParamValue("curPage") != null ? Integer
                    .parseInt(request.getParamValue("curPage"))
                    : 1; // 处理当前页
            PageResult<Map<String, Object>> ps = null;
            if ("2".equals(radioSelect)) {
                ps = dao.queryPartDlrReturnList(po, dealerId, dealerName, startDate, endDate, state, curPage, Constant.PAGE_SIZE);
            } else if ("1".equals(radioSelect)) {
                ps = dao2.queryPartDlrReturnList(paramMap, logonUser, curPage, Constant.PAGE_SIZE);
            } else {
                ps = dao2.PartDlrReturnInStockQuery(returnCode, dealerName, startDate, endDate, flag, logonUser, partOldCode, partCname, curPage, Constant.PAGE_SIZE);
            }

            //分页方法 end
            act.setOutData("ps", ps);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "退货信息查询");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-6-24
     * @Title :
     * @Description: 导出
     */
    public void exportPartReturnExcel() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        String flag = CommonUtils.checkNull(request.getParamValue("flag"));//退货类型标志
        String radioSelect = CommonUtils.checkNull(request.getParamValue("RADIO_SELECT"));//退货类型标志
        try {
            String[] head = new String[18];
            List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
            if ("1".equals(flag)) {//如果是销售退货
                PartDlrReturnApplyDAO dao2 = PartDlrReturnApplyDAO.getInstance();
                String returnCode = CommonUtils.checkNull(request.getParamValue("RETURN_CODE"));//退货单号
                String dealerName = CommonUtils.checkNull(request.getParamValue("DEALER_NAME"));//退货单位
                String startDate = CommonUtils.checkNull(request.getParamValue("startDate_y"));//验收开始时间
                String endDate = CommonUtils.checkNull(request.getParamValue("endDate_y"));//验收结束时间
                String partCode = CommonUtils.checkNull(request.getParamValue("PART_CODE"));//件号
                String partOldCode = CommonUtils.checkNull(request.getParamValue("PART_OLDCODE"));//配件编码
                String partCname = CommonUtils.checkNull(request.getParamValue("PART_CNAME"));//配件名称
                String state = CommonUtils.checkNull(request.getParamValue("state"));//配件名称
                String isWo = CommonUtils.checkNull(request.getParamValue("is_wo"));//是否已冲销

                String startDate_t = CommonUtils.checkNull(request.getParamValue("startDate_t"));//提交开始时间
                String endDate_t = CommonUtils.checkNull(request.getParamValue("endDate_t"));//提交结束时间
                String startDate_s = CommonUtils.checkNull(request.getParamValue("startDate_s"));//审核开始时间
                String endDate_s = CommonUtils.checkNull(request.getParamValue("endDate_s"));//审核结束时间
                String startDate_h = CommonUtils.checkNull(request.getParamValue("startDate_h"));//回运开始时间
                String endDate_h = CommonUtils.checkNull(request.getParamValue("endDate_h"));//回运结束时间

                Map<String, String> paramMap = new HashMap<String, String>();
                paramMap.put("returnCode", returnCode);
                paramMap.put("dealerName", dealerName);
                paramMap.put("startDate", startDate);
                paramMap.put("endDate", endDate);
                paramMap.put("partCode", partCode);
                paramMap.put("partOldCode", partOldCode);
                paramMap.put("partCname", partCname);
                paramMap.put("radioSelect", radioSelect);
                paramMap.put("state", state);
                paramMap.put("isWo", isWo);
                paramMap.put("startDate_t", startDate_t);
                paramMap.put("endDate_t", endDate_t);
                paramMap.put("startDate_s", startDate_s);
                paramMap.put("endDate_s", endDate_s);
                paramMap.put("startDate_h", startDate_h);
                paramMap.put("endDate_h", endDate_h);
                
                String dealerId = "";
                //判断是否为车厂  PartWareHouseDao
                PartWareHouseDao partWareHouseDao = PartWareHouseDao.getInstance();
                List<OrgBean> beanList = partWareHouseDao.getOrgInfo(logonUser);
                if (null != beanList || beanList.size() >= 0) {
                    dealerId = beanList.get(0).getOrgId() + "";

                }
                int flag2 = 0;//默认是车厂,0表示车厂,1表示供应中心,2表示一般服务商
                if (logonUser.getDealerId() != null) {//如果不是车厂
                    flag2 = 1;
                    TmDealerPO tmDealerPO = new TmDealerPO();
                    tmDealerPO.setDealerId(CommonUtils.parseLong(logonUser.getDealerId()));
                    tmDealerPO = (TmDealerPO) dao.select(tmDealerPO).get(0);
                    if (tmDealerPO.getPdealerType().intValue() != Constant.PART_SALE_PRICE_DEALER_TYPE_01.intValue()) {//如果不是供应中心
                        flag2 = 2;
                    }
                }
                paramMap.put("flag", String.valueOf(flag2));
                TtPartReturnRecordPO po = new TtPartReturnRecordPO();
                po.setReturnCode(returnCode);
                po.setPartCode(partCode);
                po.setPartOldcode(partOldCode);
                po.setPartCname(partCname);
                if ("2".equals(radioSelect)) {
                    head[0] = "退货单号";
                    head[1] = "单位编码";
                    head[2] = "退货单位";
                    head[3] = "配件编码";
                    head[4] = "配件名称";
                    head[5] = "单位";
                    head[6] = "出库库房";
                    head[7] = "入库库房";
                    head[8] = "申请数量";
                    head[9] = "审核数量";
                    head[10] = "回运数量";
                    head[11] = "验收数量";
                    head[12] = "退货价";
                    head[13] = "退货金额";
                    head[14] = "验收日期";
                    head[15] = "状态";
                } else if ("1".equals(radioSelect)) {
                    head[0] = "退货单号";
                    head[1] = "单位编码";
                    head[2] = "退货单位";
                    head[3] = "制单日期";
                    head[4] = "退货原因";
                    head[5] = "提交日期";
                    head[6] = "状态";
                } else {
                    head[0] = "退货单号";
                    head[1] = "退货单位编码";
                    head[2] = "退货单位";
                    head[3] = "配件编码";
                    head[4] = "配件名称";
                    head[5] = "单位";
                    head[6] = "验收数量";
                    head[7] = "验收日期";
                    head[8] = "验收人";
                }
                PageResult<Map<String, Object>> ps = null;
                if ("2".equals(radioSelect)) {
                    ps = dao.queryPartDlrReturnList(po, dealerId, dealerName, startDate, endDate, state, 1, Constant.PAGE_SIZE_MAX);
                } else if ("1".equals(radioSelect)) {
                    ps = dao2.queryPartDlrReturnList(paramMap, logonUser, 1, Constant.PAGE_SIZE_MAX);
                } else {
                    ps = dao2.PartDlrReturnInStockQuery(returnCode, dealerName, startDate, endDate, flag2, logonUser, partOldCode, partCname, 1, Constant.PAGE_SIZE_MAX);
                }
                list = ps.getRecords();
            } else {
                head[0] = "退货单号";
                head[1] = "退货类型";
                head[2] = "退货单位";
                head[3] = "配件编码";
                head[4] = "配件名称";
                head[5] = "配件类型";
                head[6] = "单位";
                head[7] = "制单人";
                head[8] = "出库库房";
                head[9] = "入库库房";
                head[10] = "退货数量";
                head[11] = "计划价";
                head[12] = "计划金额";
                head[13] = "退货日期";
                list = dao.queryPartReturn(request);
            }
            List list1 = new ArrayList();
            if (list != null && list.size() != 0) {
                for (int i = 0; i < list.size(); i++) {
                    Map map = (Map) list.get(i);
                    if (map != null && map.size() != 0) {
                        String[] detail = new String[18];
                        if ("1".equals(flag)) {//如果是销售退货
                            if ("2".equals(radioSelect)) {
                                detail[0] = CommonUtils.checkNull(map.get("RETURN_CODE"));
                                detail[1] = CommonUtils.checkNull(map.get("DEALER_CODE"));
                                detail[2] = CommonUtils.checkNull(map.get("DEALER_NAME"));
                                detail[3] = CommonUtils.checkNull(map.get("PART_OLDCODE"));
                                detail[4] = CommonUtils.checkNull(map.get("PART_CNAME"));
                                detail[5] = CommonUtils.checkNull(map.get("UNIT"));
                                detail[6] = CommonUtils.checkNull(map.get("STOCK_OUT_NAME"));
                                detail[7] = CommonUtils.checkNull(map.get("STOCK_IN_NAME"));
                                detail[8] = CommonUtils.checkNull(map.get("APPLY_QTY"));
                                detail[9] = CommonUtils.checkNull(map.get("CHECK_QTY"));
                                detail[10] = CommonUtils.checkNull(map.get("BACK_QTY"));
                                detail[11] = CommonUtils.checkNull(map.get("RETURN_QTY"));
                                detail[12] = CommonUtils.checkNull(map.get("BUY_PRICE"));
                                detail[13] = CommonUtils.checkNull(map.get("BUY_AMOUNT"));
                                detail[14] = CommonUtils.checkNull(map.get("CREATE_DATE"));
                                detail[15] = CommonUtils.checkNull(CommonUtils.getCodeDesc(map.get("STATE2") + ""));
                            } else if ("1".equals(radioSelect)) {
                                detail[0] = CommonUtils.checkNull(map.get("RETURN_CODE"));
                                detail[1] = CommonUtils.checkNull(map.get("DEALER_CODE"));
                                detail[2] = CommonUtils.checkNull(map.get("DEALER_NAME"));
                                detail[3] = CommonUtils.checkNull(map.get("CREATE_DATE"));
                                detail[4] = CommonUtils.checkNull(map.get("REMARK"));
                                detail[5] = CommonUtils.checkNull(map.get("APPLY_DATE"));
                                detail[6] = CommonUtils.checkNull(CommonUtils.getCodeDesc(map.get("STATE") + ""));
                            } else {
                                detail[0] = CommonUtils.checkNull(map.get("RETURN_CODE"));
                                detail[1] = CommonUtils.checkNull(map.get("DEALER_CODE"));
                                detail[2] = CommonUtils.checkNull(map.get("DEALER_NAME"));
                                detail[3] = CommonUtils.checkNull(map.get("PART_OLDCODE"));
                                detail[4] = CommonUtils.checkNull(map.get("PART_CNAME"));
                                detail[5] = CommonUtils.checkNull(map.get("UNIT"));
                                detail[6] = CommonUtils.checkNull(map.get("RETURN_QTY"));
                                detail[7] = CommonUtils.checkNull(map.get("CREATE_DATE"));
                                detail[7] = CommonUtils.checkNull(map.get("NAME"));
                            }
                        } else {
                            detail[0] = CommonUtils.checkNull(map.get("RETURN_CODE"));
                            detail[1] = CommonUtils.checkNull(map.get("RETURN_TYPE"));
                            detail[2] = CommonUtils.checkNull(map.get("DEALER_NAME"));
                            detail[3] = CommonUtils.checkNull(map.get("PART_OLDCODE"));
                            detail[4] = CommonUtils.checkNull(map.get("PART_CNAME"));
                            detail[5] = CommonUtils.checkNull(map.get("PART_TYPE"));
                            detail[6] = CommonUtils.checkNull(map.get("UNIT"));
                            detail[7] = CommonUtils.checkNull(map.get("NAME"));
                            detail[8] = CommonUtils.checkNull(map.get("STOCK_OUT_NAME"));
                            detail[9] = CommonUtils.checkNull(map.get("STOCK_IN_NAME"));
                            detail[10] = CommonUtils.checkNull(map.get("RETURN_QTY"));
                            detail[11] = CommonUtils.checkNull(map.get("SALE_PRICE3"));
                            detail[12] = CommonUtils.checkNull(map.get("RETURN_AMOUNT"));
                            detail[13] = CommonUtils.checkNull(map.get("CREATE_DATE"));


                        }
                        list1.add(detail);
                    }
                }
                this.exportEx(ActionContext.getContext().getResponse(),
                        request, head, list1, flag);
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
            if ("1".equals(flag)) {
                act.setForword(PART_DLRRETURN_QUERY_URL);
            } else {
                act.setForword(PART_RETURN_QUERY_URL);
            }
        }

    }


    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-12-23
     * @Title : 打印销售退货单
     */
    public void sellReturnPrint() {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        PartReturnPrintDao dao1 = PartReturnPrintDao.getInstance();
        try {
            int indexNO = 1;
            Map<String, Object> dataMap = new HashMap<String, Object>();
            //5.获取退货单表头信息
            List<Map<String, Object>> soReturnList = dao1.PrintHead(request, returnId);//上部出库单信息
            if (soReturnList != null) {

//            	String seller_id  = "";//供应商编号
//                String seller_name = "";//供应商名称
                String return_code = "";//退货单号
                String dealer_date = "";//退货日期
                String dealer_name = "";//退货单位
//                String 退货部门
                String remark = "";//备注


                Map<String, Object> mainMap = soReturnList.get(0);
//                seller_id = CommonUtils.checkNull(mainMap.get("SELLER_ID"));//
//                seller_name = CommonUtils.checkNull(mainMap.get("SELLER_NAME"));
                return_code = CommonUtils.checkNull(mainMap.get("RETURN_CODE"));
                dealer_date = CommonUtils.checkNull(mainMap.get("DEALER_DATE"));
                dealer_name = CommonUtils.checkNull(mainMap.get("DEALER_NAME"));
                remark = CommonUtils.checkNull(mainMap.get("REMARK"));

//                dataMap.put("seller_id", seller_id);
//                dataMap.put("seller_name", seller_name);
                dataMap.put("return_code", return_code);
                dataMap.put("dealer_date", dealer_date);
                dataMap.put("dealer_name", dealer_name);
                dataMap.put("remark", remark);

                //获取列表内信息
                List<Map<String, Object>> PrintList = dao1.returnPrintList(request, returnId);

                //获取本单合计数量
                List<Map<String, Object>> sumList = dao1.returnPrintSum(request, returnId);


                List<Map<String, Object>> subList = new ArrayList<Map<String, Object>>();
                for (int k = 0; k < PrintList.size(); k++) {//indexNO
                    Map<String, Object> row = PrintList.get(k);
                    row.put("indexNO", indexNO);
                    subList.add(row);
                    indexNO++;
                }

                act.setOutData("subList", subList);
                act.setOutData("listSize", (10 - subList.size()));
                act.setOutData("sumapplyQty", sumList.get(0));
            }
            indexNO = 1;
            act.setOutData("dataMap", dataMap);
            act.setForword(PART_OEMRETURN_PRINT_URL);
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "销售退货单打印错误,请联系管理员!");
            logger.error(loginUser, e1);
            act.setException(e1);
        }
    }

    //采购退货单打印
    public void partProcurementReturnPrint() {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        ProcurementReturnPrintDao dao = ProcurementReturnPrintDao.getInstance();
        try {
            int indexNO = 1;
            Map<String, Object> dataMap = new HashMap<String, Object>();
            //获取采购退货单表头信息
            List<Map<String, Object>> soReturnList = dao.ProcurementReturnPrintQuery(request, returnId);//出库单信息
            if (soReturnList != null) {

                String return_code = "";//退货单号
                String return_date = "";//退货日期
                String org_name = "";//退货单位
                String venderCode = "";//供应商编码
                String venderName = "";//供应商
                //退货部门
                String remark = "";//备注

                Map<String, Object> mainMap = soReturnList.get(0);
                return_code = CommonUtils.checkNull(mainMap.get("RETURN_CODE"));
                return_date = CommonUtils.checkNull(mainMap.get("RETURN_DATE"));
                org_name = CommonUtils.checkNull(mainMap.get("ORG_NAME"));
                remark = CommonUtils.checkNull(mainMap.get("REMARK"));
                venderCode = CommonUtils.checkNull(mainMap.get("VENDER_CODE"));
                venderName = CommonUtils.checkNull(mainMap.get("VENDER_NAME"));

                dataMap.put("venderCode", venderCode);
                dataMap.put("venderName", venderName);
                dataMap.put("return_code", return_code);
                dataMap.put("return_date", return_date);
                dataMap.put("org_name", org_name);
                dataMap.put("remark", remark);

                //获取表单内信息
                List<Map<String, Object>> PrintList = dao.ProcurementReturnPrintQuery(request, returnId);

                //获取本单合计数量
                List<Map<String, Object>> sumList = dao.returnPrintSum(request);

                List<Map<String, Object>> subList = new ArrayList<Map<String, Object>>();
                for (int k = 0; k < PrintList.size(); k++) {//indexNO
                    Map<String, Object> row = PrintList.get(k);
                    row.put("indexNO", indexNO);
                    subList.add(row);
                    indexNO++;
                }

                act.setOutData("subList", subList);
                act.setOutData("listSize", (10 - subList.size()));
                act.setOutData("sumReturnQty", sumList.get(0));
            }
            indexNO = 1;
            act.setOutData("dataMap", dataMap);
            act.setForword(PART_PROCUREMENT_PRINT_URL);
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "采购退货单打印错误,请联系管理员!");
            logger.error(loginUser, e1);
            act.setException(e1);
        }
    }
}
