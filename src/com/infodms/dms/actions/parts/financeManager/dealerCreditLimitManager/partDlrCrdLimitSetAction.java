package com.infodms.dms.actions.parts.financeManager.dealerCreditLimitManager;

import com.infodms.dms.actions.sales.planmanage.PlanUtil.BaseImport;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.getCompanyId.GetOemcompanyId;
import com.infodms.dms.dao.parts.financeManager.dealerAccQueryManager.partDealerAccQueryDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TmDealerPO;
import com.infodms.dms.po.TtPartAccountDefinePO;
import com.infodms.dms.util.CheckUtil;
import com.infodms.dms.util.CommonUtils;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;
import com.infoservice.po3.bean.PageResult;
import jxl.Cell;
import jxl.CellType;
import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.format.VerticalAlignment;
import jxl.write.*;
import jxl.write.biff.RowsExceededException;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.ParseException;
import java.util.*;

/**
 * @author huhcao
 * @version 1.0
 * @Title: 服务商信用额维护
 * @Description:YXDMS
 * @Date: 2014-3-5
 * @remark
 */
public class partDlrCrdLimitSetAction extends BaseImport {
    public Logger logger = Logger.getLogger(partDlrCrdLimitSetAction.class);
    private static final partDealerAccQueryDao dao = partDealerAccQueryDao.getInstance();
    private ActionContext act = ActionContext.getContext();
    RequestWrapper request = act.getRequest();

    //服务商账户查询
    private static final String PART_DEALER_ACC_QUERY = "/jsp/parts/financeManager/dealerCreditLimitManager/dlrCrdLimitSet.jsp";//服务商信用额维护
    //数据导入出错页面
    private static final String INPUT_ERROR_URL = "/jsp/parts/financeManager/dealerCreditLimitManager/inputError.jsp";
    private static final String USER_ROLE_OEM = "1"; //主机厂
    private static final String USER_ROLE_PDR = "2"; //供应中心
    private static final String USER_ROLE_DLR = "3"; //服务商

    /**
     * @param :
     * @return :
     * @throws : LastDate : 2013-4-15
     * @Title : 跳转至服务商账户查询页面
     */
    public void partDealerAccQueryInit() {
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        try {
            String parentOrgId = "";//父机构（销售单位）ID
            String parentOrgCode = "";//父机构（销售单位）编码
            String childOrgId = "";
            String childOrgCode = "";
            String userRole = "";//用户角色
            //判断主机厂与服务商
            String comp = logonUser.getOemCompanyId();
            if (null == comp) {

                parentOrgId = Constant.OEM_ACTIVITIES;
                parentOrgCode = Constant.ORG_ROOT_CODE;
                userRole = USER_ROLE_OEM;

            } else {
                childOrgId = logonUser.getDealerId();
                childOrgCode = logonUser.getDealerCode();

                TmDealerPO selPo = new TmDealerPO();

                selPo.setDealerId(Long.parseLong(childOrgId));

                List<TmDealerPO> dlrList = dao.select(selPo);
                TmDealerPO resPo = null;
                if (null != dlrList && dlrList.size() == 1) {
                    resPo = dlrList.get(0);
                    int pDlrType;
                    if (null != resPo.getPdealerType() && !"".equals(resPo.getPdealerType())) {
                        pDlrType = resPo.getPdealerType();
                        if (Constant.PART_SALE_PRICE_DEALER_TYPE_01 == pDlrType) {
                            userRole = USER_ROLE_PDR;
                        } else {
                            userRole = USER_ROLE_DLR;
                        }
                    } else {
                        userRole = USER_ROLE_DLR;
                    }
                }

            }
            act.setOutData("userRole", userRole);
            act.setOutData("normalDlr", USER_ROLE_DLR);
            act.setOutData("parentOrgId", parentOrgId);
            act.setOutData("parentOrgCode", parentOrgCode);
            act.setOutData("childOrgId", childOrgId);
            act.setOutData("childOrgCode", childOrgCode);
            act.setForword(PART_DEALER_ACC_QUERY);
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.QUERY_FAILURE_CODE, "服务商账户查询页面初始化");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-4-15
     * @Title : 查询服务商账户信息
     */
    public void partDealerAccQuerySearch() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        try {
            String parentOrgId = CommonUtils.checkNull(request.getParamValue("parentOrgId"));// 父机构（销售单位）ID
            Integer curPage = request.getParamValue("curPage") != null ? Integer
                    .parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
            //add by yuan 20130528
            if (logonUser.getDealerId() != null) {
                parentOrgId = logonUser.getDealerId();
            }
            PageResult<Map<String, Object>> ps = dao.queryPartDealerAccount(request, parentOrgId, logonUser, Constant.PAGE_SIZE, curPage);

            act.setOutData("ps", ps);
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.QUERY_FAILURE_CODE, "条件查询服务商账户信息");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2014-3-5
     * @Title : 服务商信用额修改
     */
    public void updateDlrCrdLmt() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        act.getResponse().setContentType("application/json");
        RequestWrapper req = act.getRequest();
        String errorExist = "";
        try {
            String curPage = CommonUtils.checkNull(request.getParamValue("curPage"));//当前页
            if ("".equals(curPage)) {
                curPage = "1";
            }
            Long userId = logonUser.getUserId(); //修改人ID
            String acountId = CommonUtils.checkNull(req.getParamValue("acountId")); //账户ID
            String crLmtStr = CommonUtils.checkNull(req.getParamValue("CrdLimit_" + acountId));
            double crdLmt = 0.0d;

            if (null != crLmtStr && !"".equals(crLmtStr) && Double.parseDouble(crLmtStr.replace(",", "")) > 0) {
                crdLmt = Double.parseDouble(crLmtStr.replace(",", ""));
                TtPartAccountDefinePO selPo = new TtPartAccountDefinePO();
                TtPartAccountDefinePO updPo = new TtPartAccountDefinePO();

                selPo.setAccountId(Long.parseLong(acountId));

                updPo.setCreditLimit(crdLmt);

                dao.update(selPo, updPo);
            } else {
                errorExist = "设置的信用额度【" + crLmtStr + "】不合法!";
            }

            act.setOutData("errorExist", errorExist);// 返回错误信息
            act.setOutData("success", "true");
            act.setOutData("curPage", curPage);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "服务商信用额维护 异常");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param : @return
     * @return :
     * @throws : LastDate : 2013-4-15
     * @Title :导出服务商账户信息
     */
    public void exportPartDealerAccountExcel() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        try {
            RequestWrapper request = act.getRequest();
            String parentOrgId = CommonUtils.checkNull(request.getParamValue("parentOrgId"));// 父机构（销售单位）ID
            if (logonUser.getDealerId() != null) {
                parentOrgId = logonUser.getDealerId();
            }
            String[] head = new String[15];
            head[0] = "序号";
            head[1] = "服务商编码";
            head[2] = "服务商名称";
            head[3] = "销售单位编码";
            head[4] = "销售单位名称";
            head[5] = "资金类型";
            head[6] = "总可用金额";
            head[8] = "现金可用金额";
            head[7] = "已扣款金额";
            head[9] = "信用额度";
            head[10] = "现金账户余额";
            head[11] = "已开票金额";
            List<Map<String, Object>> list = dao.queryAllPartDealerAccount(request, parentOrgId);
            List list1 = new ArrayList();
            if (list != null && list.size() != 0) {
                for (int i = 0; i < list.size(); i++) {
                    Map map = (Map) list.get(i);
                    if (map != null && map.size() != 0) {
                        String[] detail = new String[15];
                        detail[0] = CommonUtils.checkNull(i + 1);
                        detail[1] = CommonUtils.checkNull(map
                                .get("DEALER_CODE"));
                        detail[2] = CommonUtils
                                .checkNull(map.get("DEALER_NAME"));
                        detail[3] = CommonUtils.checkNull(map
                                .get("PARENTORG_CODE"));
                        detail[4] = CommonUtils
                                .checkNull(map.get("PARENTORG_NAME"));
                        Integer kindTemp = Integer.parseInt(CommonUtils
                                .checkNull(map.get("ACCOUNT_KIND")));
                        if (Constant.FIXCODE_CURRENCY_01.equals(kindTemp)) {
                            detail[5] = "现金";
                        } else if (Constant.FIXCODE_CURRENCY_02.equals(kindTemp)) {
                            detail[5] = "其他";
                        }
                        detail[6] = CommonUtils.checkNull(map
                                .get("USEABLEACCOUNT"));
                        detail[8] = CommonUtils.checkNull(map
                                .get("CASH_KY"));
                        detail[7] = CommonUtils.checkNull(map
                                .get("PREEMPTIONVALUE"));
                        detail[9] = CommonUtils.checkNull(map
                                .get("CREDIT_LIMIT"));
                        detail[10] = CommonUtils.checkNull(map
                                .get("ACCOUNT_SUM"));
                        detail[11] = CommonUtils.checkNull(map
                                .get("ACCOUNT_INVO"));
                        list1.add(detail);
                    }
                }
            }

            String fileName = "服务商账户信息";
            this.exportEx(fileName, ActionContext.getContext().getResponse(),
                    request, head, list1);

        } catch (Exception e) {
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.SPECIAL_MEG, "导出服务商账户信息");
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
     * @throws : LastDate    : 2013-4-15
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
     * @throws : LastDate    : 2014-3-7
     * @Title : 导出EXECEL模板
     * @Description: TODO
     */
    public void exportExcelTemplate() {

        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        ResponseWrapper response = act.getResponse();
        OutputStream os = null;
        try {
            String sellerId = "";
            List<List<Object>> list = new ArrayList<List<Object>>();
            List<Object> listHead = new ArrayList<Object>();

            listHead.add("服务商编码");
            listHead.add("服务商名称");
            listHead.add("信用额度");
            list.add(listHead);

            if (logonUser.getDealerId() == null) {
                sellerId = Constant.OEM_ACTIVITIES.toString();
            } else {
                sellerId = logonUser.getDealerId();
            }
            // 用于下载传参的集合
            List<Map<String, Object>> listDear = dao.getDealerList(GetOemcompanyId.getOemCompanyId(logonUser), sellerId);
            // 导出的文件名
            String[] dealerHead = {"经销商全称", "经销商简称", "经销商代码"};
            // 导出的文件名
            String fileName = "服务商信用额维护模板.xls";
            // 导出的文字编码
            fileName = new String(fileName.getBytes("GB2312"), "ISO8859-1");
            response.setContentType("Application/text/xls");
            response.addHeader("Content-Disposition", "attachment;filename=" + fileName);
            os = response.getOutputStream();
            this.createXlsFile(list, listDear, dealerHead, os);
            os.flush();

        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "服务商余额导入模板下载");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param : @param content
     * @param : @param os
     * @param : @throws ParseException
     * @return :
     * @throws :
     * @FUNCTION : 创建下载文件
     * @author : zhumingwei
     * @LastDate : 2013-9-17
     */
    public void createXlsFile(List<List<Object>> content, List<Map<String, Object>> listDear, String[] dealerHead, OutputStream os) throws ParseException {
        try {
            WritableWorkbook workbook = Workbook.createWorkbook(os);
            WritableSheet sheet = workbook.createSheet("下载模板", 0);
            if (dealerHead != null) {
                for (int i = 0; i < content.size(); i++) {
                    for (int j = 0; j < content.get(i).size(); j++) {
                        // 添加单元格
                        sheet.addCell(new Label(j, i, (content.get(i).get(j) != null ? content.get(i).get(j).toString() : "")));
                    }
                }
                WritableSheet sheet1 = workbook.createSheet("经销商信息", 1);
                for (int i = 0; i < dealerHead.length; i++) {
                    sheet1.addCell(new Label(i, 0, dealerHead[i]));
                }
                if (listDear != null && listDear.size() > 0) {
                    for (int i = 0; i < listDear.size(); i++) {
                        Map<String, Object> map = listDear.get(i);
                        //sheet1.addCell(new Label(0,i+1,String.valueOf(i+1),wcf1));

                        sheet1.addCell(new Label(1, i + 1, CommonUtils.checkNull(map.get("DEALER_SHORTNAME"))));
                        sheet1.addCell(new Label(0, i + 1, CommonUtils.checkNull(map.get("DEALER_NAME"))));
                        sheet1.addCell(new Label(2, i + 1, CommonUtils.checkNull(map.get("DEALER_CODE"))));
                    }
                }
                //套用多少行公式
                for (int i = 0; i < 5; i++) {
                    insertFormula(sheet, 0, i + 1, "VLOOKUP(B" + (i + 2) + ",'经销商信息'!A:C,3,0)", getDataCellFormat(CellType.STRING_FORMULA));//套用公式
                }
            } else {
                for (int i = 0; i < content.size(); i++) {
                    for (int j = 0; j < content.get(i).size(); j++) {
                        // 添加单元格
                        sheet.addCell(new Label(j, i, (content.get(i).get(j) != null ? content.get(i).get(j).toString() : "")));
                    }
                }
            }
            workbook.write();
            workbook.close();
        } catch (RowsExceededException e) {
            e.printStackTrace();
        } catch (WriteException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param : @throws Exception
     * @return :
     * @throws : LastDate    : 2014-03-07
     * @Title :Excel批量导入
     * @Description: TODO
     */
    public void uploadPartBaseExcel() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        long userId = logonUser.getUserId();//User ID
        Date date = new Date();//Date
        StringBuffer errorInfo = new StringBuffer("");
        RequestWrapper request = act.getRequest();
        try {

            long maxSize = 1024 * 1024 * 5;
            int errNum = insertIntoTmpMore(request, "uploadFile", 3, 3, maxSize, Constant.EXPORT_ONLY_SHEET);
            String err = "";
            if (errNum != 0) {
                switch (errNum) {
                    case 1:
                        err += "文件列数过多!";
                        break;
                    case 2:
                        err += "空行不能大于三行!";
                        break;
                    case 3:
                        err += "文件不能为空!";
                        break;
                    case 4:
                        err += "文件不能为空!";
                        break;
                    case 5:
                        err += "文件不能大于!";
                        break;
                    default:
                        break;
                }
            }

            if (!"".equals(err)) {
                act.setOutData("error", err);
                BizException e1 = new BizException(act, ErrorCodeConstant.SPECIAL_MEG, err);
                throw e1;
            } else {
                List<Map> list = getMapList();
                List<String> vdIdList = new ArrayList<String>();
                List<TtPartAccountDefinePO> voList = new ArrayList<TtPartAccountDefinePO>();
                loadVoList(voList, list, errorInfo, vdIdList);

                if (errorInfo.length() > 0) {
                    BizException e1 = new BizException(act, ErrorCodeConstant.SPECIAL_MEG, errorInfo);
                    throw e1;
                }
                //批量更新信用额度
                for (int i = 0; i < voList.size(); i++) {
                    TtPartAccountDefinePO ttPartAccountDefinePO1 = new TtPartAccountDefinePO();
                    TtPartAccountDefinePO ttPartAccountDefinePO2 = new TtPartAccountDefinePO();
                    ttPartAccountDefinePO1.setChildorgId(voList.get(i).getChildorgId());
                    ttPartAccountDefinePO1.setParentorgId(Long.parseLong(request.getParamValue("parentOrgId")));
                    ttPartAccountDefinePO2.setCreditLimit(voList.get(i).getCreditLimit());
                    dao.update(ttPartAccountDefinePO1, ttPartAccountDefinePO2);
                }
            }
            act.setForword(PART_DEALER_ACC_QUERY);
        } catch (Exception e) {
            BizException e1 = null;
            e.printStackTrace();
            if (e instanceof BizException) {
                e1 = (BizException) e;
            } else {
                new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "文件读取错误");
            }
            logger.error(logonUser, e1);
            act.setException(e1);
            act.setOutData("errorInfo", e1.getMessage());
            act.setForword(INPUT_ERROR_URL);
        }
    }

    /**
     * @param : @param voList
     * @param : @param list
     * @param : @param errorInfo
     * @param : @throws Exception
     * @return :
     * @throws : LastDate    : 2013-4-3
     * @Title :循环获取CELL
     * @Description: TODO
     */
    private void loadVoList(List<TtPartAccountDefinePO> voList, List<Map> list, StringBuffer errorInfo,
                            List<String> vdIdList) {
        if (null == list) {
            list = new ArrayList();
        }
        for (int i = 0; i < list.size(); i++) {
            Map map = list.get(i);
            if (null == map) {
                map = new HashMap<String, Cell[]>();
            }
            Set<String> keys = map.keySet();
            Iterator it = keys.iterator();
            String key = "";
            while (it.hasNext()) {
                key = (String) it.next();
                Cell[] cells = (Cell[]) map.get(key);
                parseCells(voList, key, cells, errorInfo, vdIdList);
                if (errorInfo.length() > 0) {
                    break;
                }
            }
        }
    }

    /**
     * @param : @param list
     * @param : @param rowNum
     * @param : @param cells
     * @param : @param errorInfo
     * @param : @throws Exception
     * @return :
     * @throws : LastDate    : 2013-4-3
     * @Title : 装载VO
     * @Description: TODO
     */
    private void parseCells(List<TtPartAccountDefinePO> list, String rowNum, Cell[] cells, StringBuffer errorInfo,
                            List<String> vdIdList) {
        TtPartAccountDefinePO po = new TtPartAccountDefinePO();

        String dealerCode = "";
        if (cells.length > 0 && null != cells[0].getContents() && !"".equals(cells[0].getContents().trim())) {
            dealerCode = subCell(cells[0].getContents().trim());
        } else {
            errorInfo.append("第" + (Integer.parseInt(rowNum) - 1) + "行：服务商编码不能为空!");
            return;
        }

        TmDealerPO tdpo = new TmDealerPO();
        tdpo.setDealerCode(dealerCode);
        List<TmDealerPO> ListTdpo = dao.select(tdpo);
        if (ListTdpo.size() == 0) {
            errorInfo.append("第" + (Integer.parseInt(rowNum) - 1) + "行：服务商编码不存在!");
            return;
        }

        po.setChildorgId(ListTdpo.get(0).getDealerId());

        String creditLimit = "";
        if (cells.length > 2 && null != cells[2].getContents() && !"".equals(cells[2].getContents().trim())) {
            creditLimit = subCell(cells[2].getContents().trim());
        } else {
            errorInfo.append("第" + (Integer.parseInt(rowNum) - 1) + "行：信用额度不能为空");
            return;
        }

        if (CheckUtil.checkFormatNumber1(creditLimit)) {
            po.setCreditLimit(Double.parseDouble(cells[2].getContents().trim()));
        } else {
            errorInfo.append("第" + (Integer.parseInt(rowNum) - 1) + "行：信用额度必须是数字");
            return;
        }


        list.add(po);
    }

    /**
     * @param : @param orgAmt
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-4-3
     * @Title :截取字符
     * @Description: TODO
     */
    private String subCell(String orgAmt) {
        String newAmt = "";
        if (null == orgAmt || "".equals(orgAmt)) {
            return newAmt;
        }
        if (orgAmt.length() > 30) {
            newAmt = orgAmt.substring(0, 30);
        } else {
            newAmt = orgAmt;
        }
        return newAmt;
    }

    //zhengZhiQiang 2014-03-07
    public WritableCellFormat getDataCellFormat(CellType type) {
        WritableCellFormat wcf = null;

        try {
            // 字体样式
            if (type == CellType.NUMBER || type == CellType.NUMBER_FORMULA) {// 数字
                jxl.write.NumberFormat nf = new jxl.write.NumberFormat("#.00");
                wcf = new WritableCellFormat(nf);
            } else if (type == CellType.DATE || type == CellType.DATE_FORMULA) {// 日期
                jxl.write.DateFormat df = new jxl.write.DateFormat("yyyy-MM-dd hh:mm:ss");
                wcf = new jxl.write.WritableCellFormat(df);
            } else {
                WritableFont wf = new WritableFont(WritableFont.TIMES, 10,
                        WritableFont.NO_BOLD, false);// 最后一个为是否italic
                wcf = new WritableCellFormat(wf);
            }
            // 对齐方式
            wcf.setAlignment(Alignment.CENTRE);
            wcf.setVerticalAlignment(VerticalAlignment.CENTRE);

            // 边框
            wcf.setBorder(Border.LEFT, BorderLineStyle.THIN);
            wcf.setBorder(Border.BOTTOM, BorderLineStyle.THIN);
            wcf.setBorder(Border.RIGHT, BorderLineStyle.THIN);
            // 背景色
            wcf.setBackground(Colour.WHITE);
            wcf.setWrap(true);// 自动换行
        } catch (WriteException e) {
            e.printStackTrace();
        }
        return wcf;
    }

    public void insertFormula(WritableSheet sheet, Integer col, Integer row,
                              String formula, WritableCellFormat format) {
        try {
            Formula f = new Formula(col, row, formula);
            sheet.addCell(f);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
