package com.infodms.dms.actions.parts.financeManager.dealerAccImpManager;

import com.infodms.dms.actions.sales.planmanage.PlanUtil.BaseImport;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.getCompanyId.GetOemcompanyId;
import com.infodms.dms.dao.parts.financeManager.dealerAccImpManager.partDealerAccImpDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TtPartAccountDefinePO;
import com.infodms.dms.po.TtPartAccountHistoryPO;
import com.infodms.dms.po.TtPartAccountImportHistoryPO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;
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
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.Pattern;

/**
 * @author huhcao
 * @version 1.0
 * @Title: 处理服务商账户余额导入
 * @Description:CHANADMS
 * @Date: 2013-4-12
 * @remark
 */
public class partDealerAccImpAction extends BaseImport {
    public Logger logger = Logger.getLogger(partDealerAccImpAction.class);
    private static final partDealerAccImpDao dao = partDealerAccImpDao.getInstance();
    private ActionContext act = ActionContext.getContext();
    RequestWrapper request = act.getRequest();
    //服务商账户余额导入
    private static final String PART_DEALER_ACC_IMP = "/jsp/parts/financeManager/dealerAccImpManager/dealerAccountImport.jsp";//服务商账户余额导入首页
    private static final String INPUT_ERROR_URL = "/jsp/parts/financeManager/dealerAccImpManager/inputError.jsp";//数据导入出错页面
    private static final String PART_DEALER_ACC_IMP_DO = "/jsp/parts/financeManager/dealerAccImpManager/dealerAccountImportDo.jsp";//数据导入确认页面

    /**
     * @param :
     * @return :
     * @throws : LastDate : 2013-4-12
     * @Title : 跳转至服务商账户余额导入页面
     */
    public void partDealerAccImpInit() {
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        try {
            act.setForword(PART_DEALER_ACC_IMP);
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.QUERY_FAILURE_CODE, "服务商账户余额导入初始化");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * : zhumingwei 2013-09-17
     *
     * @param :
     * @return :
     * LastDate    : 2013-9-17
     * @Title : download服务商余额导入模板
     */
    public void download() {
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        ResponseWrapper response = act.getResponse();
        OutputStream os = null;
        try {
            String sellerId = "";
            List<List<Object>> list = new ArrayList<List<Object>>();
            List<Object> listHead = new ArrayList<Object>();
            listHead.add("服务商编码");
            listHead.add("服务商名称");
            listHead.add("金额");
            listHead.add("备注");
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
            String fileName = "服务商资金导入模板.xls";
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

    //zhumingwei 2013-09-17
    public WritableCellFormat getDataCellFormat(CellType type) {
        WritableCellFormat wcf = null;

        try {
            // 字体样式

            if (type == CellType.NUMBER || type == CellType.NUMBER_FORMULA) {// 数字

                jxl.write.NumberFormat nf = new jxl.write.NumberFormat("#.00");

                wcf = new WritableCellFormat(nf);

            } else if (type == CellType.DATE || type == CellType.DATE_FORMULA) {// 日期

                jxl.write.DateFormat df = new jxl.write.DateFormat(

                        "yyyy-MM-dd hh:mm:ss");

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

    //zhumingwei 2013-09-17
    public void insertFormula(WritableSheet sheet, Integer col, Integer row,
                              String formula, WritableCellFormat format) {
        try {
            Formula f = new Formula(col, row, formula);
            sheet.addCell(f);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @param :
     * @return :
     * LastDate    : 2013-4-12
     * @Title : 导入服务商余额EXCEL
     * @Description: 上传
     */
    public void dealerAccUpload() {
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            String parentOrgId = "";//父机构（销售单位）ID

            //判断主机厂与服务商
            String comp = logonUser.getOemCompanyId();
            if (null == comp) {

                parentOrgId = Constant.OEM_ACTIVITIES;

            } else {
                parentOrgId = logonUser.getDealerId();
            }
            List<Map<String, String>> errorInfo = new ArrayList<Map<String, String>>();
            long maxSize = 1024 * 1024 * 5;
            int errNum = insertIntoTmpMore(request, "uploadFile", 4, 3, maxSize, Constant.EXPORT_ONLY_SHEET);

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
                act.setForword(INPUT_ERROR_URL);
            } else {
                List<Map> list = getMapList();
                List<Map<String, String>> voList = new ArrayList<Map<String, String>>();
                loadVoList(voList, list, errorInfo, parentOrgId);
                if (errorInfo.size() > 0) {
                    act.setOutData("errorInfo", errorInfo);
                    act.setForword(INPUT_ERROR_URL);
                } else {
                    act.setOutData("list", voList);
                    act.setForword(PART_DEALER_ACC_IMP_DO);
                }

            }
        } catch (Exception e) {// 异常方法
            BizException e1 = null;
            if (e instanceof BizException) {
                e1 = (BizException) e;
            } else {
                new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "文件读取错误");
            }
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param : @param voList
     * @param : @param list
     * @param : @param errorInfo
     * @return :
     * LastDate    : 2013-4-12
     * @Title : 读取CELL
     */
    private void loadVoList(List<Map<String, String>> voList, List<Map> list, List<Map<String, String>> errorInfo, String parentOrgId) {
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
                Map<String, String> tempmap = new HashMap<String, String>();
                String dlrId = "";
                    /*if ("".equals(cells[0].getContents().trim())) {
						Map<String, String> errormap = new HashMap<String, String>();
						errormap.put("1", "第" + (i + 1) + "页,第" + key + "行");
						errormap.put("2", "服务商编码");
						errormap.put("3", "空");
						errorInfo.add(errormap);
					}
					else*/
                if (cells.length < 2 || CommonUtils.isEmpty(cells[1].getContents())) {
                    Map<String, String> errormap = new HashMap<String, String>();
                    errormap.put("1", "第" + (i + 1) + "页,第" + key + "行");
                    errormap.put("2", "服务商名称");
                    errormap.put("3", "空");
                    errorInfo.add(errormap);
                } else {
                    String str = " AND UPPER(TD.DEALER_CODE) = '" + cells[0].getContents().trim().toUpperCase() + "' "
                            + " AND TD.DEALER_NAME = '" + cells[1].getContents().trim() + "' ";

                    //String str = " AND TD.DEALER_NAME = '"+cells[1].getContents().trim()+"' ";

                    List<Map<String, Object>> dealerList = dao.checkDealer(str);
                    if (dealerList.size() != 1) {
                        Map<String, String> errormap = new HashMap<String, String>();
                        errormap.put("1", "第" + (i + 1) + "页,第" + key + "行");
                        errormap.put("2", "服务商");
                        errormap.put("3", "不存在!");
                        errorInfo.add(errormap);
                    } else {
                        tempmap.put("4", dealerList.get(0).get("DEALER_ID").toString());
                        tempmap.put("1", dealerList.get(0).get("DEALER_CODE").toString());
                        dlrId = dealerList.get(0).get("DEALER_ID").toString();
                    }
                }
//					tempmap.put("1", cells[0].getContents().trim());
                tempmap.put("5", cells[1].getContents().trim());

                if (parentOrgId.equals(dlrId)) {
                    Map<String, String> errormap = new HashMap<String, String>();
                    errormap.put("1", "第" + (i + 1) + "页,第" + key + "行");
                    errormap.put("2", "服务商");
                    errormap.put("3", "不能为自身!");
                    errorInfo.add(errormap);
                }

                if (cells.length < 3 || CommonUtils.isEmpty(cells[2].getContents())) {
                    Map<String, String> errormap = new HashMap<String, String>();
                    errormap.put("1", "第" + (i + 1) + "页,第" + key + "行");
                    errormap.put("2", "金额");
                    errormap.put("3", "空!");
                    errorInfo.add(errormap);
                } else {
                    String accTemp = cells[2].getContents().trim();
                    if (null == accTemp || "".equals(accTemp)) {
                        accTemp = "0.00";
                    } else {
                        accTemp = accTemp.replace(",", "");
                    }

                    String regex = "((^[0]([.]{1}(\\d)*)?$)|(^[-][0]([.]{1}(\\d)*)?$)|(^[-]?[1-9]+(\\d)*([.]{1}(\\d)*)?$))";
                    Pattern pattern = Pattern.compile(regex);
                    Matcher matcher = pattern.matcher(accTemp);

                    if (matcher.find()) {
                        Double amount = Double.parseDouble(accTemp);

                        NumberFormat numberFormat = NumberFormat.getNumberInstance();

                        numberFormat.setMinimumFractionDigits(2);
                        numberFormat.setMaximumFractionDigits(2);
                        numberFormat.setMaximumIntegerDigits(10);

                        tempmap.put("2", numberFormat.format(amount));
                    } else {
                        Map<String, String> errormap = new HashMap<String, String>();
                        errormap.put("1", "第" + (i + 1) + "页,第" + key + "行");
                        errormap.put("2", "金额");
                        errormap.put("3", "为非法数值!");
                        errorInfo.add(errormap);
                    }


                }
                tempmap.put("3", cells.length < 4 || null == cells[3].getContents() ? ""
                        : cells[3].getContents().trim());
                voList.add(tempmap);
            }
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-4-15
     * @Title : 服务商金额导入到数据库中
     */
    public void dealerAccountAdd() {
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        act.getResponse().setContentType("application/json");
        try {
            Date date = new Date();
            String strCount = CommonUtils.checkNull(request.getParamValue("count")); //导入数据总条数

            String parentOrgId = "";//父机构（销售单位）ID
            String parentOrgCode = "";//父机构（销售单位）编码
            String parentOrgName = "";//父机构（销售单位）名称

            //判断主机厂与服务商
            String comp = logonUser.getOemCompanyId();
            if (null == comp) {

                parentOrgId = Constant.OEM_ACTIVITIES;
                parentOrgCode = Constant.ORG_ROOT_CODE;

            } else {

                parentOrgId = logonUser.getDealerId();
                parentOrgCode = logonUser.getDealerCode();

            }

            Integer currency = Constant.FIXCODE_CURRENCY_01;//资金类型 -> 现金
            int count = Integer.parseInt(strCount);
            String importType = dao.getAccountImportType();

            //供应中心 执行 发生额导入
            if (null != logonUser.getOemCompanyId()) {
                importType = Constant.FIXCODE_CHANGE_BALANCE_IMPORT;
            }

            for (int i = 1; i < count; i++) {
                String str = "";
                String dealerCode = CommonUtils.checkNull(request.getParamValue("code" + i));//服务商编码
                String dealerName = CommonUtils.checkNull(request.getParamValue("dealerName" + i));//服务商名称
                String dealerAmount = CommonUtils.checkNull(request.getParamValue("amount" + i)).replace(",", "");//服务商金额
                String dealerId = CommonUtils.checkNull(request.getParamValue("id" + i));//服务商ID
                String remark = CommonUtils.checkNull(request.getParamValue("remark" + i));//备注(财务凭证)

                str += " AND TA.PARENTORG_ID = '" + parentOrgId + "' ";
                str += " AND TA.CHILDORG_ID = '" + dealerId + "' ";

                String accountId = dao.checkDealerAccWhetherExisted(str);


                //服务商资金导入记录表
                TtPartAccountImportHistoryPO paihPo = new TtPartAccountImportHistoryPO();

                paihPo.setHistroryId(Long.parseLong(SequenceManager.getSequence("")));
                paihPo.setChildorgId(Long.parseLong(dealerId));
                paihPo.setChildorgCode(dealerCode);
                paihPo.setChildorgName(dealerName);
                paihPo.setParentorgId(Long.parseLong(parentOrgId));
                paihPo.setParentorgCode(parentOrgCode);
                paihPo.setAccountKind(currency);
                paihPo.setImportType(Integer.parseInt(importType));
                paihPo.setAmount(Double.parseDouble(dealerAmount));
                paihPo.setRemark(remark);
                paihPo.setState(Constant.STATUS_ENABLE);
                paihPo.setAccountPurpose(Constant.PART_ACCOUNT_PURPOSE_TYPE_01);
                paihPo.setCreateBy(logonUser.getUserId());
                paihPo.setDkDate(date);
                paihPo.setCreateDate(date);

                dao.insert(paihPo);


                //余额导入
                if (Constant.FIXCODE_BALANCE_IMPORT.equals(importType)) {

                    //新增账户
                    if ("".equalsIgnoreCase(accountId)) {
                        TtPartAccountDefinePO padPo = new TtPartAccountDefinePO();

                        padPo.setAccountId(Long.parseLong(SequenceManager.getSequence("")));
                        padPo.setChildorgId(Long.parseLong(dealerId));
                        padPo.setParentorgId(Long.parseLong(parentOrgId));
                        padPo.setAccountKind(currency);
                        padPo.setAccountSum(Double.parseDouble(dealerAmount));
                        padPo.setState(Constant.STATUS_ENABLE);
                        padPo.setCreateBy(logonUser.getUserId());
                        padPo.setCreateDate(date);
                        padPo.setUpdateBy(logonUser.getUserId());
                        padPo.setUpdateDate(date);

                        dao.insert(padPo);

                    }
                    //账户已存在
                    else {
                        TtPartAccountDefinePO selPo = new TtPartAccountDefinePO();
                        TtPartAccountDefinePO updatePo = new TtPartAccountDefinePO();

                        selPo.setAccountId(Long.parseLong(accountId));

                        updatePo.setAccountSum(Double.parseDouble(dealerAmount));
                        updatePo.setUpdateBy(logonUser.getUserId());
                        updatePo.setUpdateDate(date);

                        dao.update(selPo, updatePo);
                    }

                }
                //发生额导入
                else if (Constant.FIXCODE_CHANGE_BALANCE_IMPORT.equals(importType)) {
                    Long actId = 0l;
                    //新增账户
                    if ("".equalsIgnoreCase(accountId)) {
                        TtPartAccountDefinePO padPo = new TtPartAccountDefinePO();
                        actId = Long.parseLong(SequenceManager.getSequence(""));
                        padPo.setAccountId(actId);
                        padPo.setChildorgId(Long.parseLong(dealerId));
                        padPo.setParentorgId(Long.parseLong(parentOrgId));
                        padPo.setAccountKind(currency);
                        padPo.setAccountSum(Double.parseDouble(dealerAmount));
                        padPo.setState(Constant.STATUS_ENABLE);
                        padPo.setCreateBy(logonUser.getUserId());
                        padPo.setCreateDate(date);
                        padPo.setUpdateBy(logonUser.getUserId());
                        padPo.setUpdateDate(date);

                        dao.insert(padPo);

                    }
                    //账户已存在
                    else {
                        actId = Long.parseLong(accountId);
                        StringBuffer sb = new StringBuffer();
                        sb.append(" AND TA.ACCOUNT_ID = '" + accountId + "' ");

                        TtPartAccountDefinePO selPo = new TtPartAccountDefinePO();
                        TtPartAccountDefinePO updatePo = new TtPartAccountDefinePO();

                        Double originalAccountSum = Double.parseDouble(dao.getOriginalAmount(sb.toString()));
                        Double AccountSum = originalAccountSum + Double.parseDouble(dealerAmount);

                        selPo.setAccountId(Long.parseLong(accountId));

                        updatePo.setAccountSum(AccountSum);
                        updatePo.setUpdateBy(logonUser.getUserId());
                        updatePo.setUpdateDate(date);

                        dao.update(selPo, updatePo);
                    }

                    //TtPartAccountHistoryPO
                    TtPartAccountHistoryPO pdhPo = new TtPartAccountHistoryPO();

                    pdhPo.setHistroryId(Long.parseLong(SequenceManager.getSequence("")));
                    pdhPo.setAccountId(actId);
                    pdhPo.setChildorgId(Long.parseLong(dealerId));
                    pdhPo.setChildorgCode(dealerCode);
                    pdhPo.setChildorgName(dealerName);
                    pdhPo.setParentorgId(Long.parseLong(parentOrgId));
                    pdhPo.setParentorgCode(parentOrgCode);
                    pdhPo.setAcountKind(currency);
                    pdhPo.setAmount(Double.parseDouble(dealerAmount));
                    pdhPo.setAcountType(Constant.FIXCODE_ACCOUNT_TYPE_01);//服务商资金导入 均为 打款
                    pdhPo.setRemark(remark);
                    pdhPo.setState(Constant.STATUS_ENABLE);
                    pdhPo.setCreateBy(logonUser.getUserId());
                    pdhPo.setCreateDate(date);

                    dao.insert(pdhPo);
                }
            }

            act.setOutData("errorExist", null);
            act.setOutData("success", "true");
        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "服务商金额导入到数据库中");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

}
