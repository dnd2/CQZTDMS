package com.infodms.dms.actions.report.partReport.partSalesReport;

import com.infodms.dms.actions.partsmanage.infoSearch.DealerDlrstockInfo;
import com.infodms.dms.actions.sales.planmanage.PlanUtil.BaseImport;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.dao.parts.storageManager.partStaSetManager.partStockSettingDao;
import com.infodms.dms.dao.report.partSalesReport.PartTransInfoRepDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TtPartLogisticsInfoPO;
import com.infodms.dms.po.TtPartTransPlanPO;
import com.infodms.dms.util.CheckUtil;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.csv.CsvWriterUtil;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;
import com.infoservice.po3.bean.PageResult;
import jxl.Cell;
import jxl.CellType;
import jxl.DateCell;
import jxl.Workbook;
import jxl.write.Label;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;

public class PartTransInfoRep extends BaseImport {
    public Logger logger = Logger.getLogger(DealerDlrstockInfo.class);
    private static final String init = "/jsp/report/partSalesReport/partTransInfoRepMain.jsp";
    private static final String queryinit = "/jsp/report/partSalesReport/partTransInfo.jsp";

    public void init() {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        try {
            List list = CommonUtils.getPartUnitList(Constant.FIXCODE_TYPE_06);// 获取配件发运方式
            List list2 = CommonUtils.getPartUnitList(Constant.FIXCODE_TYPE_08);// 获取配件发运方式
            //获取仓库信息修改 modify by hu 2013-11-26
            String orgId = "";
            //判断主机厂与服务商
            String comp = loginUser.getOemCompanyId();
            if (null == comp) {
                orgId = Constant.OEM_ACTIVITIES;
            } else {
                orgId = loginUser.getDealerId();
            }
            StringBuffer sbString = new StringBuffer();
            sbString.append(" AND TM.ORG_ID = '" + orgId + "' ");
            List<Map<String, Object>> wareHouseList = partStockSettingDao.getInstance().getWareHouses(sbString.toString());

            String dealerId = loginUser.getDealerId();
            if (dealerId == null || "".equals(dealerId)) {
                act.setOutData("OK", 1);
            }

            act.setOutData("transList", list);
            act.setOutData("transOrg", list2);
            act.setOutData("wareHouseList", wareHouseList);
            act.setOutData("old", CommonUtils.getMonthFirstDay());
            act.setOutData("now", CommonUtils.getDate());
            act.setForword(init);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "页面始化错误,请联系管理员!");
            logger.error(loginUser, e1);
            act.setException(e1);
        }
    }

    public void query() {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        PartTransInfoRepDao dao = PartTransInfoRepDao.getInstance();
        try {
            Integer curPage = request.getParamValue("curPage") != null ? Integer
                    .parseInt(request.getParamValue("curPage"))
                    : 1; // 处理当前页
            PageResult<Map<String, Object>> ps = null;
            String radioSelect = CommonUtils.checkNull(request.getParamValue("RADIO_SELECT"));
            if ("1".equals(radioSelect)) {
                ps = dao.queryRep(request, curPage, Constant.PAGE_SIZE);
            } else {
                ps = dao.queryRep(request, curPage, Constant.PAGE_SIZE);
            }

            act.setOutData("ps", ps);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "查询错误,请联系管理员!");
            logger.error(loginUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param : @param orgAmt
     * @param : @return
     * @return :
     * @throws : LastDate    : 2013-4-3
     * @Title :
     * @Description:导出
     */
    public void exportExcel() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            RequestWrapper request = act.getRequest();
            String radioSelect = CommonUtils.checkId(request.getParamValue("RADIO_SELECT"));
            String[] head = null;
            PartTransInfoRepDao dao = PartTransInfoRepDao.getInstance();
            if ("1".equals(radioSelect)) {
                head = new String[25];
                head[0] = "服务商编码";
                head[1] = "服务商名称";
                head[2] = "发运日期";
                head[3] = "发运单号";
                head[4] = "运输方式";
                head[5] = "装箱数量";
                head[6] = "发货数量";
                head[7] = "体积";
                head[8] = "实际重量";
                head[9] = "折合重KG";
                head[10] = "计费重量KG";
                head[11] = "订单类型";
                head[12] = "要求到货天数";
                head[13] = "要求到货时间";
                head[14] = "系统到货时间";
                head[15] = "实际到货时间";
                head[16] = "超期后预计到货时间";
                head[17] = "运输单位";
                head[18] = "在途情况";
                head[19] = "备注";
                head[20] = "超期原因";
            } else {
                head = new String[15];
                head[0] = "服务商编码";
                head[1] = "服务商名称";
                head[2] = "订单号";
                head[3] = "销售单";
                head[4] = "拣货单";
                head[5] = "出库日期";
                head[6] = "发运单号";
                head[7] = "配件编码";
                head[8] = "配件名称";
                head[9] = "出库数量";
                head[10] = "单价";
                head[11] = "出库金额";
                head[12] = "箱号";
            }
            PageResult<Map<String, Object>> list = dao.queryRep(request, 1, 99999);
            List list1 = new ArrayList();
            for (int i = 0; i < list.getRecords().size(); i++) {
                Map map = (Map) list.getRecords().get(i);
                if (map != null && map.size() != 0) {
                    String[] detail = null;
                    if ("1".equals(radioSelect)) {
                        detail = new String[25];
                        detail[0] = CommonUtils.checkNull(map.get("DEALER_CODE"));
                        detail[1] = CommonUtils.checkNull(map.get("DEALER_NAME"));
                        detail[2] = CommonUtils.checkNull(map.get("CREATE_DATE"));
                        detail[3] = CommonUtils.checkNull(map.get("TRPLAN_CODE"));
                        detail[4] = CommonUtils.checkNull(map.get("TRANS_TYPE"));
                        detail[5] = CommonUtils.checkNull(map.get("PKG_NUMS"));
                        detail[6] = CommonUtils.checkNull(map.get("PKG_QTY"));
                        detail[7] = CommonUtils.checkNull(map.get("VOLUME"));
                        detail[8] = CommonUtils.checkNull(map.get("WEIGHT"));
                        detail[9] = CommonUtils.checkNull(map.get("EQ_WEIGHT"));
                        detail[10] = CommonUtils.checkNull(map.get("CH_WEIGHT"));
                        detail[11] = CommonUtils.checkNull(map.get("ORDER_TYPE"));
                        detail[12] = CommonUtils.checkNull(map.get("ARRIVE_DAYS"));
                        detail[13] = CommonUtils.checkNull(map.get("REQUIRE_DATE"));
                        detail[14] = CommonUtils.checkNull(map.get("ARRIVAL_DATE"));
                        detail[15] = CommonUtils.checkNull(map.get("ARR_DATE2"));
                        detail[16] = CommonUtils.checkNull(map.get("ARR_DATE"));
                        detail[17] = CommonUtils.checkNull(map.get("TRANSPORT_ORG"));
                        detail[18] = CommonUtils.checkNull(map.get("OTW_DES"));
                        detail[19] = CommonUtils.checkNull(map.get("REMARK1"));
                        detail[20] = CommonUtils.checkNull(map.get("REMARK2"));
                        list1.add(detail);
                    } else {
                        detail = new String[15];
                        detail[0] = CommonUtils.checkNull(map.get("DEALER_CODE"));
                        detail[1] = CommonUtils.checkNull(map.get("DEALER_NAME"));
                        detail[2] = CommonUtils.checkNull(map.get("ORDER_CODE"));
                        detail[3] = CommonUtils.checkNull(map.get("SO_CODE"));
                        detail[4] = CommonUtils.checkNull(map.get("PICK_ORDER_ID"));
                        detail[5] = CommonUtils.checkNull(map.get("CREATE_DATE"));
                        detail[6] = CommonUtils.checkNull(map.get("TRPLAN_CODE"));
                        detail[7] = CommonUtils.checkNull(map.get("PART_OLDCODE"));
                        detail[8] = CommonUtils.checkNull(map.get("PART_CNAME"));
                        detail[9] = CommonUtils.checkNull(map.get("OUTSTOCK_QTY"));
                        detail[10] = CommonUtils.checkNull(map.get("SALE_PRICE"));
                        detail[11] = CommonUtils.checkNull(map.get("SALE_AMOUNT"));
                        detail[12] = CommonUtils.checkNull(map.get("PKG_NO"));
                        list1.add(detail);
                    }
                }
            }
            this.exportEx(ActionContext.getContext().getResponse(), request, head, list1);

        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

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

        String name = "发运信息统计.xls";
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

    public void updateTransRemark() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper req = act.getRequest();
        PartTransInfoRepDao dao = PartTransInfoRepDao.getInstance();
        try {
            String tranPlanId = CommonUtils.checkNull(req.getParamValue("tranPlanId"));
            String arrDate = CommonUtils.checkNull(req.getParamValue("ARR_DATE"));
            String otwDes = CommonUtils.checkNull(req.getParamValue("OTW_DES"));
            String remark1 = CommonUtils.checkNull(req.getParamValue("REMARK1"));
            String remark2 = CommonUtils.checkNull(req.getParamValue("REMARK2"));
            String remark3 = CommonUtils.checkNull(req.getParamValue("REMARK3"));//实际到货日期

            TtPartTransPlanPO transPlanPO = new TtPartTransPlanPO();
            transPlanPO.setTrplanId(Long.valueOf(tranPlanId));

            TtPartTransPlanPO updatePO = new TtPartTransPlanPO();
            if (!"".equals(arrDate) && null != arrDate) {
                updatePO.setArrDate(arrDate);
            }
            if (!"".equals(otwDes) && null != otwDes) {
                updatePO.setOtwDes(otwDes);
            }
            if (!"".equals(remark1) && null != remark1) {
                updatePO.setRemark1(remark1);
            }
            if (!"".equals(remark2) && null != remark2) {
                updatePO.setRemark2(remark2);
            }
            if (!"".equals(remark3) && null != remark3) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                updatePO.setArrDate2(sdf.parse(remark3));
            }
            dao.update(transPlanPO, updatePO);

        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "");
            logger.error(logonUser, e1);
            act.setException(e1);
        }

    }


    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2014-8-21
     * @Title : 导出EXECEL模板
     * @Description: TODO
     */
    public void exportExcelTemplate() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();

        ResponseWrapper response = act.getResponse();
        OutputStream os = null;
        try {
            List<List<Object>> list = new LinkedList<List<Object>>();
            List<Object> listHead = new LinkedList<Object>();//导出模板第一列
            String type = CommonUtils.checkNull(request.getParamValue("excelType"));//读取模板类型
            System.out.println(request.getParamValue("excelType"));
            String fileName = "";
            if (type.equals("1") || type == "1") {
                //标题
                listHead.add("发运单号");
                listHead.add("发生时间");
                listHead.add("物流信息");
                listHead.add("实际物流单号");
                list.add(listHead);
                // 导出的文件名
                fileName = "物流信息导入模板.xls";
            } else {
                //标题
                listHead.add("发运单号");
                listHead.add("到货日期");
                list.add(listHead);
                // 导出的文件名
                fileName = "收货时间导入模板.xls";
            }
            // 导出的文字编码
            fileName = new String(fileName.getBytes("GB2312"), "ISO8859-1");
            response.setContentType("Application/text/xls");
            response.addHeader("Content-Disposition", "attachment;filename=" + fileName);


            os = response.getOutputStream();
//				CsvWriterUtil.writeCsv(list, os);
            CsvWriterUtil.createXlsFile(list, os);
            os.flush();
        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "文件读取错误");
            logger.error(logonUser, e1);
            act.setException(e1);
        } finally {
            if (null != os) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * @param : @throws Exception
     * @return :
     * @throws : LastDate    : 2014-8-21
     * @Title :Excel导入
     * @Description: TODO
     */
    public void uploadPartLogExcel() {
        PartTransInfoRepDao dao = PartTransInfoRepDao.getInstance();
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        StringBuffer errorInfo = new StringBuffer("");
        RequestWrapper request = act.getRequest();
        String type = CommonUtils.checkNull(request.getParamValue("excelType"));//读取模板类型

        try {
            long maxSize = 1024 * 1024 * 5;
            int errNum = 3;

            if (type.equals("1") || type == "1") {
                errNum = insertIntoTmp(request, "uploadFile", 4, 10, maxSize);
            } else {
                errNum = insertIntoTmp(request, "uploadFile2", 2, 3, maxSize);
            }
            String err = "";

            if (errNum != 0) {
                switch (errNum) {
                    case 1:
                        err += "文件列数过多,请修改后再上传!";
                        break;
                    case 2:
                        err += "空行不能大于三行,请修改后再上传!";
                        break;
                    case 3:
                        err += "文件内容不能为空,请修改后再上传!";
                        break;
                    case 4:
                        err += "文件类型错误,请重新上传!";
                        break;
                    case 5:
                        err += "文件不能大于" + maxSize + ",请修改后再上传";
                        break;
                    default:
                        break;
                }
            }

            if (!"".equals(err)) {
                BizException e1 = new BizException(act,
                        ErrorCodeConstant.SPECIAL_MEG, err);
                throw e1;
            } else {
                if (type.equals("1") || type == "1") {
                    List<Map> list = getMapList();
                    List voList = new ArrayList();
                    loadVoList(voList, list, errorInfo);
                    for (int i = 0; i < voList.size(); i++) {
                        if (errorInfo.length() > 0) {
                            BizException e1 = new BizException(act,
                                    ErrorCodeConstant.SPECIAL_MEG, errorInfo);
                            throw e1;
                        }
                        TtPartLogisticsInfoPO logisticsInfoPo = (TtPartLogisticsInfoPO) voList.get(i);

                        if (errorInfo.length() > 0) {
                            BizException e1 = new BizException(act,
                                    ErrorCodeConstant.SPECIAL_MEG, errorInfo);
                            throw e1;
                        }

                        logisticsInfoPo.setLogisticsId(Long.parseLong(SequenceManager.getSequence("")));
                        logisticsInfoPo.setCreateBy(logonUser.getUserId());
                        logisticsInfoPo.setCreateDate(new Date());
                        dao.insert(logisticsInfoPo);
                    }
                    act.setOutData("OK", "2");
                } else {
                    List<Map> list = getMapList();
                    List voList = new ArrayList();
                    loadVoListUpload(voList, list, errorInfo);
                }

            }
            act.setForword(init);

        } catch (Exception e) {
            BizException e1 = null;
            if (e instanceof BizException) {
                e1 = (BizException) e;
            } else {
                e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG,
                        "文件读取错误");
            }
            logger.error(logonUser, e1);
            String dealerId = logonUser.getDealerId();
            if (dealerId == null || "".equals(dealerId)) {
                int ok = 1;
                act.setOutData("OK", ok);
            }
            act.setException(e1);

            act.setForword(init);
        }
    }


    /**
     * @param : @param voList
     * @param : @param list
     * @param : @param errorInfo
     * @param : @throws Exception
     * @return :
     * @throws :
     * @Title :循环获取CELL
     * @Description: TODO
     */
    private void loadVoList(List voList, List<Map> list, StringBuffer errorInfo)
            throws Exception {

        System.out.println("配件物流修改导入");
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
                parseCells(voList, key, cells, errorInfo);
                if (errorInfo.length() > 0) {
                    break;
                }
            }
        }
    }

    private void parseCells(List list, String rowNum, Cell[] cells,
                            StringBuffer errorInfo) throws Exception {

        String[] logicticsNos = cells[0].getContents().trim().split("、");

        for (String logicticsNo : logicticsNos) {
            TtPartLogisticsInfoPO logisticsInfoPo = new TtPartLogisticsInfoPO();
            Date date = null;
            if (cells[1].getType() == CellType.DATE) {
                DateCell dc = (DateCell) cells[1];
                date = dc.getDate();
            }
            logisticsInfoPo.setLogisticsNo(logicticsNo);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

            logisticsInfoPo.setLogisticsDate(date);
            logisticsInfoPo.setLogisticsDate(sdf.parse(sdf.format(date)));

            logisticsInfoPo.setLogisticsInfo(subCell(cells[2].getContents().trim()));

            if (cells.length > 3) {
                logisticsInfoPo.setLogisticsNo2(subCell(cells[3].getContents().trim()));
            }
            list.add(logisticsInfoPo);
        }

       /* PartTransInfoRepDao dao = new PartTransInfoRepDao();

        logisticsInfoPo.setLogisticsNo(subCell(cells[0].getContents().trim()));

        List<TtPartLogisticsInfoPO> t = dao.select(logisticsInfoPo);
        //取消覆盖导入
        if (!(t == null)) {
            dao.delete(logisticsInfoPo);
        }
*/

    }


    //循环取出CELL(发运时间修改导入)
    private void loadVoListUpload(List voList, List<Map> list, StringBuffer errorInfo)
            throws Exception {

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
                parseCellsUpload(voList, key, cells, errorInfo);
                if (errorInfo.length() > 0) {
                    break;
                }
            }
        }
    }

    private void parseCellsUpload(List list, String rowNum, Cell[] cells,
                                  StringBuffer errorInfo) throws Exception {

        TtPartTransPlanPO partDefinePo = new TtPartTransPlanPO();

        TtPartTransPlanPO uploadPo = new TtPartTransPlanPO();

        PartTransInfoRepDao dao = PartTransInfoRepDao.getInstance();

        partDefinePo.setTrplanCode(subCell(cells[0].getContents().trim()));
        Date date = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        if (cells[1].getType() == CellType.DATE) {
            DateCell dc = (DateCell) cells[1];
            date = dc.getDate();
        }
        uploadPo.setArrDate2(sdf.parse(sdf.format(date)));

        dao.update(partDefinePo, uploadPo);

    }


    /**
     * 截取字符串
     *
     * @param : @param orgAmt
     * @param : @return
     * @return :
     * @throws : LastDate : 2013-4-7
     * @Title :
     * @Description: TODO
     */
    private String subCell(String orgAmt) {
        String newAmt = "";
        if (null == orgAmt || "".equals(orgAmt)) {
            return newAmt;
        }
        if (orgAmt.length() > 300) {
            newAmt = orgAmt.substring(0, 300);
        } else {
            newAmt = orgAmt;
        }
        return newAmt;
    }

    /**
     * 查询物流信息
     */
    public void queryLogisticsInfoInit() {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        try {
            String trplanCode = CommonUtils.checkNull(request.getParamValue("trplanCode"));
            act.setOutData("trplanCode", trplanCode);
            act.setForword(queryinit);

        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "查询错误,请联系管理员!");
            logger.error(loginUser, e1);
            act.setException(e1);
        }
    }

    /**
     * 查询物流信息
     */
    public void queryLogisticsInfo() {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        PartTransInfoRepDao dao = PartTransInfoRepDao.getInstance();
        try {
            Integer curPage = request.getParamValue("curPage") != null ? Integer
                    .parseInt(request.getParamValue("curPage"))
                    : 1; // 处理当前页
            PageResult<Map<String, Object>> ps = null;
            ps = dao.queryogisticsrep(request, curPage, Constant.PAGE_SIZE);

            act.setOutData("ps", ps);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "查询错误,请联系管理员!");
            logger.error(loginUser, e1);
            act.setException(e1);
        }
    }

}
