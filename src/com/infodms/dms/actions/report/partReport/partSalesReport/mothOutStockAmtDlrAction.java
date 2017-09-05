package com.infodms.dms.actions.report.partReport.partSalesReport;

import com.infodms.dms.actions.sales.planmanage.PlanUtil.BaseImport;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.dao.report.partSalesReport.mothOutStockAmtDlrDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TmDealerPO;
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
import java.util.List;
import java.util.Map;

/**
 * @author huhcao
 * @version 1.0
 * @Title: 处理配件进销存报表(服务商)业务
 * @Date: 2013-11-13
 * @remark
 */
public class mothOutStockAmtDlrAction extends BaseImport {
    public Logger logger = Logger.getLogger(mothOutStockAmtDlrAction.class);
    private static final mothOutStockAmtDlrDao dao = mothOutStockAmtDlrDao.getInstance();
    private ActionContext act = ActionContext.getContext();
    RequestWrapper request = act.getRequest();

    //配件进销存报表(服务商)
    private static final String PART_MOTH_OUT_STOCK_AMT_DLR = "/jsp/report/partSalesReport/mothOutStkAmtDlrReport.jsp";//配件进销存报表(服务商)首页
    //配件进销存报表(供应中心)
    private static final String PART_MOTH_OUT_STOCK_AMTGYZX_DLR = "/jsp/report/partSalesReport/mothOutStkAmtGyzxDlrReport.jsp";//配件进销存报表(供应中心)首页

    /**
     * @param :
     * @return :
     * @throws : LastDate : 2013-11-13
     * @Title : 跳转至配件进销存报表(服务商)页面
     */
    public void mothOutStockAmtInit() {
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        try {
            String parentOrgId = "";//父机构（销售单位）ID
            String parentOrgCode = "";//父机构（销售单位）编码
            String companyName = ""; //制单单位
            String oemFlag = Constant.IF_TYPE_NO + "";

            //判断主机厂与服务商
            String comp = logonUser.getOemCompanyId();
            if (null == comp) {
                parentOrgId = Constant.OEM_ACTIVITIES;
                parentOrgCode = "";
                companyName = "";
                oemFlag = Constant.IF_TYPE_YES + "";
            } else {
                parentOrgId = logonUser.getDealerId();
                parentOrgCode = logonUser.getDealerCode();
                companyName = dao.getDealerName(parentOrgId);

            }

            act.setOutData("parentOrgId", parentOrgId);
            act.setOutData("parentOrgCode", parentOrgCode);
            act.setOutData("oemOrgId", Constant.OEM_ACTIVITIES);
            act.setOutData("oemOrgId", Constant.OEM_ACTIVITIES);
            act.setOutData("oemFlag", oemFlag);
            act.setOutData("now", CommonUtils.getDate());
            act.setOutData("old", CommonUtils.getMonthFirstDay());
            act.setForword(PART_MOTH_OUT_STOCK_AMT_DLR);
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.QUERY_FAILURE_CODE, "配件进销存报表(服务商)页面初始化");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }


    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-9-23
     * @Title :
     * @Description: 配件进销存报表初始化(供应中心)
     */
    public void mothOutStockAmtGyzxInit() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        try {
            int flag = 0;//默认是车厂,0表示车厂,1表示供应中心
            if (logonUser.getDealerId() != null) {
                flag = 1;
                TmDealerPO po = new TmDealerPO();
                po.setDealerId(Long.parseLong(logonUser.getDealerId()));
                po = (TmDealerPO) dao.select(po).get(0);
                act.setOutData("venderId", po.getDealerId());
                act.setOutData("venderCode", po.getDealerCode());
                act.setOutData("venderName", po.getDealerName());
            }
            act.setOutData("now", CommonUtils.getDate());
            act.setOutData("old", CommonUtils.getMonthFirstDay());
            act.setOutData("flag", flag);
            act.setForword(PART_MOTH_OUT_STOCK_AMTGYZX_DLR);
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.QUERY_FAILURE_CODE, "配件进销存");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-11-13
     * @Title : 查询配件进销存报表(服务商)信息
     */
    public void mothOutStockAmtSearch() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        try {
            String dealerId1 = CommonUtils.checkNull(request.getParamValue("dealerId")); // 所选服务商ID

            Integer curPage = request.getParamValue("curPage") != null ? Integer
                    .parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
            PageResult<Map<String, Object>> ps = dao.queryMothOutStockAmtDlr(
                    request, Constant.PAGE_SIZE, curPage, logonUser);

            act.setOutData("ps", ps);
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.QUERY_FAILURE_CODE, "条件查询配件进销存报表(服务商)信息");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-11-13
     * @Title : 查询配件进销存报表(供应中心)信息
     */
    public void mothOutStockAmtGyzxSearch() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        try {
            Integer curPage = request.getParamValue("curPage") != null ? Integer
                    .parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
            PageResult<Map<String, Object>> ps = dao.queryMothOutStockAmtGyzxDlr(
                    request, logonUser, Constant.PAGE_SIZE, curPage);

            act.setOutData("ps", ps);
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.QUERY_FAILURE_CODE, "配件进销存");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param : @return
     * @return :
     * @throws : LastDate : 2013-11-13
     * @Title :导出配件进销存报表(服务商)信息
     */
    public void exportMothOutStockAmtExcel() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        try {
            String[] head = new String[15];
            head[0] = "序号";
            head[1] = "服务商代码";
            head[2] = "服务商名称";
            head[3] = "配件编码";
            head[4] = "配件名称";
//			head[5] = "配件件号";
            head[5] = "单位";
            head[6] = "期初数量";
            head[7] = "入库数量";
            head[8] = "入库金额";
            head[9] = "出库数量";
            head[10] = "出库金额";
            head[11] = "期末数量";
            head[12] = "期末金额";

            List<Map<String, Object>> list = dao.queryMothOutStockAmtDlr(
                    request, 99999, 1, logonUser).getRecords();
            List list1 = new ArrayList();
            if (list != null && list.size() != 0) {
                for (int i = 0; i < list.size(); i++) {
                    Map map = (Map) list.get(i);
                    if (map != null && map.size() != 0) {
                        String[] detail = new String[15];
                        detail[0] = CommonUtils.checkNull(i + 1);
                        detail[1] = CommonUtils.checkNull(map.get("DEALER_CODE"));
                        detail[2] = CommonUtils.checkNull(map.get("DEALER_NAME"));
                        detail[3] = CommonUtils.checkNull(map.get("PART_OLDCODE"));
                        detail[4] = CommonUtils.checkNull(map.get("PART_CNAME"));
//						detail[5] = CommonUtils.checkNull(map .get("PART_CODE"));
                        detail[5] = CommonUtils.checkNull(map.get("UNIT"));
                        detail[6] = CommonUtils.checkNull(map.get("QC_QTY"));
                        detail[7] = CommonUtils.checkNull(map.get("IN_QTY"));
                        detail[8] = CommonUtils.checkNull(map.get("IN_AMOUNT"));
                        detail[9] = CommonUtils.checkNull(map.get("OUT_QTY"));
                        detail[10] = CommonUtils.checkNull(map.get("OUT_AMOUNT"));
                        detail[11] = CommonUtils.checkNull(map.get("QM_QTY"));
                        detail[12] = CommonUtils.checkNull(map.get("QM_AMOUNT"));

                        list1.add(detail);
                    }
                }
            }

            String fileName = "配件进销存报表(服务商)信息";
            this.exportEx(fileName, ActionContext.getContext().getResponse(),
                    request, head, list1);

        } catch (Exception e) {
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.SPECIAL_MEG, "导出配件进销存报表(服务商)信息");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param : @return
     * @return :
     * @throws : LastDate : 2013-11-13
     * @Title :导出配件进销存报表(供应中心)信息
     */
    public void expMothOutStockAmtGyzxExcel() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        try {
            String[] head = new String[15];

            head[0] = "序号";
            head[1] = "供应中心代码";
            head[2] = "供应中心名称";
            head[3] = "配件编码";
            head[4] = "配件名称";
            head[5] = "配件件号";
            head[6] = "单位";
            head[7] = "期初数量";
            head[8] = "入库数量";
            head[9] = "入库金额";
            head[10] = "出库数量";
            head[11] = "出库金额";
            head[12] = "账面库存";

            List<Map<String, Object>> list = dao.queryMothOutStockAmtGyzxDlr(request, logonUser);
            List list1 = new ArrayList();
            if (list != null && list.size() != 0) {
                for (int i = 0; i < list.size(); i++) {
                    Map map = (Map) list.get(i);
                    if (map != null && map.size() != 0) {
                        String[] detail = new String[15];
                        detail[0] = CommonUtils.checkNull(i + 1);
                        detail[1] = CommonUtils.checkNull(map
                                .get("DEALER_CODE"));
                        detail[2] = CommonUtils.checkNull(map
                                .get("DEALER_NAME"));
                        detail[3] = CommonUtils
                                .checkNull(map.get("PART_OLDCODE"));
                        detail[4] = CommonUtils.checkNull(map
                                .get("PART_CNAME"));
                        detail[5] = CommonUtils.checkNull(map
                                .get("PART_CODE"));
                        detail[6] = CommonUtils.checkNull(map
                                .get("UNIT"));
                        detail[7] = CommonUtils.checkNull(map
                                .get("QC_STOCK"));
                        detail[8] = CommonUtils.checkNull(map
                                .get("IN_QTY"));
                        detail[9] = CommonUtils.checkNull(map
                                .get("IN_AMOUNT"));
                        detail[10] = CommonUtils.checkNull(map
                                .get("OUT_QTY"));
                        detail[11] = CommonUtils.checkNull(map
                                .get("SALE_AMOUNT"));
                        detail[12] = CommonUtils.checkNull(map
                                .get("ITEM_QTY"));

                        list1.add(detail);
                    }
                }
            }

            String fileName = "配件进销存报表";
            this.exportEx(fileName, ActionContext.getContext().getResponse(),
                    request, head, list1);

        } catch (Exception e) {
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.SPECIAL_MEG, "导出配件进销存报表失败,请联系管理员!");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
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
                        ws.addCell(new jxl.write.Number(i, z, Double.parseDouble(str[i].replace(",", ""))));
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

}
