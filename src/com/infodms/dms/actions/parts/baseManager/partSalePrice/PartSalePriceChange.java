package com.infodms.dms.actions.parts.baseManager.partSalePrice;

import com.infodms.dms.actions.sales.planmanage.PlanUtil.BaseImport;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.constants.PTConstants;
import com.infodms.dms.dao.parts.baseManager.partSalePriceQuery.PartSalePriceChangeDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TtPartSalesPriceChgPO;
import com.infodms.dms.po.TtPartSalesPricePO;
import com.infodms.dms.util.CheckUtil;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.OrderCodeManager;
import com.infodms.dms.util.StringUtil;
import com.infodms.dms.util.csv.CsvWriterUtil;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.FileObject;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.write.Label;
import org.apache.log4j.Logger;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author : wucl
 * @CreateDate : 2014-4-24
 * @ClassName : PartSalePrice
 * @Description : 配件销售价格变更申请
 */
public class PartSalePriceChange extends BaseImport implements PTConstants {
    public Logger logger = Logger.getLogger(PartSalePriceChange.class);
    PartSalePriceChangeDao dao = PartSalePriceChangeDao.getInstance();

    //配件销售价格变更申请
    String PART_SALE_PRICE = "/jsp/parts/baseManager/partSalePriceChange/partSalePrice.jsp"; //配件变更申请查询
    String PART_SALE_PRICE_ADD = "/jsp/parts/baseManager/partSalePriceChange/partSalePriceAdd.jsp"; //新增变更
    String PART_SALE_PRICE_LOOK = "/jsp/parts/baseManager/partSalePriceChange/partSalePriceDetail.jsp"; //新增变更

    /**
     * @author wucl
     * 配件销售价格变更申请--查看明细
     */
    public void lookDetailInit() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            String applyNo = request.getParamValue("APPLY_NO");
            act.setOutData("APPLY_NO", applyNo);

            List<Map<String, Object>> list = dao.queryPartPriceSettingList();
            Map<String, String> map = new HashMap<String, String>();
            map.put("prciceName1", "价格1");
            map.put("prciceName2", "价格2");
            map.put("prciceName3", "价格3");
            map.put("prciceName4", "价格4");
            map.put("prciceName5", "价格5");
            map.put("prciceName6", "价格6");
            map.put("prciceName7", "价格7");
            map.put("prciceName8", "价格8");
            map.put("prciceName9", "价格9");
            map.put("prciceName10", "价格10");
            map.put("prciceName11", "价格11");
            map.put("prciceName12", "价格12");
            map.put("prciceName13", "价格13");
            map.put("prciceName14", "价格14");
            map.put("prciceName15", "价格15");
            if (null != list && list.size() > 0) {
                for (int i = 0; i < list.size(); i++) {
                    String tempValue = list.get(i).get("TYPE_DESC").toString();
                    map.put("prciceName" + (i + 1), tempValue);
                }
            }
            act.setOutData("map", map);

            act.setForword(this.PART_SALE_PRICE_LOOK);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "配件销售价格变更申请--查看明细，初始化失败");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @author wucl
     * 配件销售价格变更申请--新增初始化
     */
    public void addApply() {

        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            act.setForword(this.PART_SALE_PRICE_ADD);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "配件销售价格变更申请--新增初始化失败");
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
     * @throws : LastDate    : 2013-4-8
     * @Title :
     * @Description: 导出数据方法
     */
    public Object exportEx(ResponseWrapper response, RequestWrapper request, String[] head, List<String[]> list)
            throws Exception {

        String name = "配件销售价格变更申请列表.xls";
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
     * @throws : LastDate    : 2013-4-7
     * @Title :
     * @Description: 配件销售价格变更申请初始化
     */
    public void partSalePriceInit() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            List<Map<String, Object>> list = dao.queryPartPriceSettingList();
            Map<String, String> map = new HashMap<String, String>();
            map.put("prciceName1", "价格1");
            map.put("prciceName2", "价格2");
            map.put("prciceName3", "价格3");
            map.put("prciceName4", "价格4");
            map.put("prciceName5", "价格5");
            map.put("prciceName6", "价格6");
            map.put("prciceName7", "价格7");
            map.put("prciceName8", "价格8");
            map.put("prciceName9", "价格9");
            map.put("prciceName10", "价格10");
            map.put("prciceName11", "价格11");
            map.put("prciceName12", "价格12");
            map.put("prciceName13", "价格13");
            map.put("prciceName14", "价格14");
            map.put("prciceName15", "价格15");

            if (null != list && list.size() > 0) {
                for (int i = 0; i < list.size(); i++) {
                    String tempValue = list.get(i).get("TYPE_DESC").toString();
                    map.put("prciceName" + (i + 1), tempValue);
                }
            }

            act.setOutData("map", map);
            // 艾春 10.08添加 东安数据过滤
            act.setOutData("poseBusType", logonUser.getPoseBusType());
            
            act.setOutData("old", CommonUtils.getBefore(new Date()));
            act.setOutData("now", CommonUtils.getDate());
            act.setForword(PART_SALE_PRICE);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "配件销售价格变更申请");
            logger.error(logonUser, e1);
            act.setException(e1);
            act.setForword(PART_SALE_PRICE);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-4-8
     * @Title :
     * @Description: 查询配件销售价格
     */
    public void queryPartSalePrice() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            //分页方法 begin
            Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
            PageResult<Map<String, Object>> ps = null;

            ps = dao.queryPartSalePrice(request, logonUser.getPoseBusType(), curPage, Constant.PAGE_SIZE);
            act.setOutData("ps", ps);
            act.setOutData("curPage", curPage);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "配件销售价格变更申请");
            logger.error(logonUser, e1);
            act.setException(e1);
            act.setForword(PartSalePriceUrl);
        }

    }

    /**
     * @author wucl
     * @Date : LastDate    : 2013-4-7
     * @Title :
     * @Description: 配件销售价格变更申请保存
     */
    public void savePartSalePrice() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            String applyNo = CommonUtils.checkNull(request.getParamValue("APPLY_NO"));
            TtPartSalesPriceChgPO pspcp1 = new TtPartSalesPriceChgPO();
            pspcp1.setApplyNo(applyNo);

            TtPartSalesPriceChgPO pspcp2 = new TtPartSalesPriceChgPO();
            pspcp2.setSubmitBy(logonUser.getUserId());
            pspcp2.setSubmitDate(new Date());
            pspcp2.setState(Constant.PART_PRICE_CHG_STATE_02);

            int rs = dao.update(pspcp1, pspcp2);
            act.setOutData("success", "配件价格变更申请提交成功 ");
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "配件销售价格变更提交失败,请联系管理员!");
            logger.error(logonUser, e1);
            act.setException(e1);
            act.setForword(PartSalePriceUrl);
        }
    }

    /**
     * @author wucl
     * @Date : LastDate    : 2013-4-7
     * @Title :
     * @Description: 配件销售价格变更申请作废
     */
    public void cancelPartSalePrice() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            String applyNo = CommonUtils.checkNull(request.getParamValue("APPLY_NO"));
            TtPartSalesPriceChgPO pspcp1 = new TtPartSalesPriceChgPO();
            pspcp1.setApplyNo(applyNo);

            TtPartSalesPriceChgPO pspcp2 = new TtPartSalesPriceChgPO();
            pspcp2.setSubmitBy(logonUser.getUserId());
            pspcp2.setSubmitDate(new Date());
            pspcp2.setState(Constant.PART_PRICE_CHG_STATE_05);

            int rs = dao.update(pspcp1, pspcp2);
            act.setOutData("success", "作废配件价格变更申请成功 ");
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "确认作废配件价格变更申请失败,请联系管理员!");
            logger.error(logonUser, e1);
            act.setException(e1);
            act.setForword(PartSalePriceUrl);
        }
    }

    /**
     * @author wucl
     * @Date : LastDate    : 2013-4-7
     * @Title :
     * @Description: 配件销售价格变更申请查看
     */
    public void lookDetail() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            String applyNo = CommonUtils.checkNull(request.getParamValue("APPLY_NO"));
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("APPLY_NO", applyNo);
            Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页

            PageResult<Map<String, Object>> ps = null;
            ps = dao.applyDetail(map, curPage, Constant.PAGE_SIZE_MIDDLE);
            act.setOutData("ps", ps);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "确认作废配件价格变更申请失败,请联系管理员!");
            logger.error(logonUser, e1);
            act.setException(e1);
            act.setForword(PartSalePriceUrl);
        }
    }

    public void queryPartList() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            String partCode = CommonUtils.checkNull(request.getParamValue("partCode"));
            String partOldcode = CommonUtils.checkNull(request.getParamValue("partOldcode"));
            String partCname = CommonUtils.checkNull(request.getParamValue("partCname"));
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("partCname", partCname);
            map.put("partOldcode", partOldcode);
            map.put("partCode", partCode);
            Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页

            PageResult<Map<String, Object>> ps = null;
            ps = dao.partList(map, curPage, Constant.PAGE_SIZE);
            act.setOutData("ps", ps);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "确认作废配件价格变更申请失败,请联系管理员!");
            logger.error(logonUser, e1);
            act.setException(e1);
            act.setForword(PartSalePriceUrl);
        }
    }

    public void savePartApply() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            long createBy = logonUser.getUserId();
            String remark = request.getParamValue("textarea1");
            String dateFrom = request.getParamValue("dateFrom");
            String dateTo = request.getParamValue("dateTo");
            
            if(StringUtil.isNull(dateFrom) || StringUtil.isNull(dateFrom)){
                act.setOutData("error", "保存失败，有效日期不能为空！");
                return;
            }
            
            Calendar calendar = Calendar.getInstance();
            Date date = calendar.getTime();
            int h = calendar.get(Calendar.HOUR);
            int m = calendar.get(Calendar.MINUTE);
            int s = calendar.get(Calendar.SECOND);
            dateFrom = dateFrom + " " + h + ":" + m + ":" + s;
            dateTo = dateTo + " " + h + ":" + m + ":" + s;
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            int state = Constant.PART_PRICE_CHG_STATE_01;//已保存
            String applyNo = OrderCodeManager.getOrderCode(Constant.PART_CODE_RELATION_53);
            List<PO> addList = new ArrayList<PO>();
            String[] partIds = request.getParamValues("cb");
            for (String partId : partIds) {
                long chgId = Long.parseLong(SequenceManager.getSequence(""));
                String partOldcode = request.getParamValue("partOldcode_" + partId);
                //String partCode = request.getParamValue("partCode_"+partId);
                String partCname = request.getParamValue("partCname_" + partId);
                String price1 = request.getParamValue("price1_" + partId);
                String price1After = request.getParamValue("price1After_" + partId);
                String price2 = request.getParamValue("price2_" + partId);
                String price2After = request.getParamValue("price2After_" + partId);
                TtPartSalesPriceChgPO chpo = new TtPartSalesPriceChgPO();
                chpo.setChgId(chgId);
                chpo.setApplyNo(applyNo);
                chpo.setPartId(Long.valueOf(partId));
                chpo.setPartCname(partCname);
                chpo.setPartOldcode(partOldcode);
                chpo.setSvcPrice(Double.valueOf(price1));
                chpo.setSvcPriceC(Double.valueOf(price1After));
                chpo.setRetailPrice(Double.valueOf(price2));
                chpo.setRetailPriceC(Double.valueOf(price2After));
                chpo.setRemark(remark);
                chpo.setCreateBy(createBy);
                chpo.setCreateDate(date);
                chpo.setState(state);
                chpo.setStatus(1);
                chpo.setValidFrom(df.parse(dateFrom));
                chpo.setValidTo(df.parse(dateTo));
                addList.add(chpo);
            }
            dao.insert(addList);
            act.setOutData("success", "保存成功!");
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "保存配件价格变更申请失败,请联系管理员!");
            logger.error(logonUser, e1);
            act.setException(e1);
            act.setForword(PartSalePriceUrl);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-4-8
     * @Title :
     * @Description: 导出模板
     */
    public void exportExcelTemplate() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        OutputStream os = null;
        try {
            ResponseWrapper response = act.getResponse();
            // 用于下载传参的集合
            List<List<Object>> list = new LinkedList<List<Object>>();

            //标题
            List<Object> listHead = new LinkedList<Object>();//导出模板第一列
//            listHead.add("配件件号");
            listHead.add("配件编码");
            listHead.add("配件名称");
//            listHead.add("变更前服务站价格(元)");
            listHead.add("变更后服务站价格(元)");
//            listHead.add("变更前零售价(元)");
            listHead.add("变更后零售价(元)");
            list.add(listHead);
            // 导出的文件名
            String fileName = "配件销售价格导入模板.xls";
            // 导出的文字编码
            fileName = new String(fileName.getBytes("GB2312"), "ISO8859-1");
            response.setContentType("Application/text/xls");
            response.addHeader("Content-Disposition", "attachment;filename=" + fileName);
            os = response.getOutputStream();
            CsvWriterUtil.createXlsFile(list, os);
            os.flush();
        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "文件读取错误");
            logger.error(logonUser, e1);
            act.setException(e1);
            act.setForword(PartSalePriceUrl);
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
     * @param :
     * @return :
     * @throws : LastDate    : 2013-4-8
     * @Title :
     * @Description: 导出数据
     */
    public void exportPartPriceExcel() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            RequestWrapper request = act.getRequest();

            String[] head = new String[19];

            head[0] = "配件编码";
            head[1] = "配件名称";
            head[2] = "变更前服务站价格(元)";
            head[3] = "变更后服务站价格(元)";
            head[4] = "变更前零售价(元)";
            head[5] = "变更后零售价(元)";


            List<Map<String, Object>> list = dao.queryPartSalePriceForExport(request);
            List<String[]> list1 = new ArrayList<String[]>();
            if (list != null && list.size() != 0) {
                for (int i = 0; i < list.size(); i++) {
                    Map<String, Object> map = list.get(i);
                    if (map != null && map.size() != 0) {
                        String[] detail = new String[7];
//                        detail[0] = CommonUtils.checkNull(map.get("PART_CODE"));
                        detail[0] = CommonUtils.checkNull(map.get("PART_OLDCODE"));
                        detail[1] = CommonUtils.checkNull(map.get("PART_CNAME"));
                        detail[2] = CommonUtils.checkNull(map.get("SVC_PRICE"));
                        detail[3] = CommonUtils.checkNull(map.get("SVC_PRICE_C"));
                        detail[4] = CommonUtils.checkNull(map.get("RETAIL_PRICE"));
                        detail[5] = CommonUtils.checkNull(map.get("RETAIL_PRICE_C"));
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
     * @param :
     * @return :
     * @throws : LastDate    : 2013-4-8
     * @Title :
     * @Description: 配件销售价格变更申请--导入保存
     */
    public void loadPartPriceDataIntoDB() throws Exception {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();

        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            Date date = new Date();
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR) + 10;
            calendar.set(Calendar.YEAR, year);
            Date dateTo = calendar.getTime();
            long createBy = logonUser.getUserId();
            String remark = request.getParamValue("textarea1");
            int state = Constant.PART_PRICE_CHG_STATE_01;//已保存
            String applyNo = OrderCodeManager.getOrderCode(Constant.PART_CODE_RELATION_53);

            List<PO> addList = new ArrayList<PO>();

            FileObject uploadFile = request.getParamObject("uploadFile");//获取导入文件
            if (uploadFile == null) {//文件为空报空指针异常
                return;
            }
            String fileName = uploadFile.getFileName();//获取文件名
            fileName = fileName.substring(fileName.lastIndexOf("\\") + 1, fileName.length());//截取文件名
            ByteArrayInputStream inputStream = new ByteArrayInputStream(uploadFile.getContent());//获取文件数据
            Workbook wb = Workbook.getWorkbook(inputStream);
            inputStream.reset();
            Sheet[] sheets = wb.getSheets();
            Sheet sheet = sheets[0];
            int rowNum = sheet.getRows();
            for (int j = 1; j < rowNum; j++) {
                Cell[] cells = sheet.getRow(j);
                if (cells.length != 4) {
                    BizException e1 = new BizException(act, ErrorCodeConstant.QUERY_FAILURE_CODE, "第" + j + "行导入数据必须是4列，切每个单元格不允许为空！");
                    throw e1;
                }
                String partId = "";
//				String partCode = cells[0].getContents().trim();
                String partOldcode = cells[0].getContents().trim();
                String partCname = cells[1].getContents().trim();
//				String price1 = cells[2].getContents().trim();
                String price1After = cells[2].getContents().trim();
//				String price2 = cells[4].getContents().trim();
                String price2After = cells[3].getContents().trim();
                if (!"".equals(price1After) && !"".equals(price2After)) {
                    TtPartSalesPriceChgPO chpo = new TtPartSalesPriceChgPO();
                    long chgId = Long.parseLong(SequenceManager.getSequence(""));
                    partId = dao.getPartId(partOldcode);
                    if (partId == null) {
                        BizException e1 = new BizException(act, ErrorCodeConstant.QUERY_FAILURE_CODE, "第" + j + "行配件编码不存在！");
                        throw e1;
                    }
                    TtPartSalesPricePO pricePO = new TtPartSalesPricePO();
                    pricePO.setPartId(Long.valueOf(partId));
                    pricePO = (TtPartSalesPricePO) dao.select(pricePO).get(0);

                    chpo.setChgId(chgId);
                    chpo.setApplyNo(applyNo);
                    chpo.setPartId(Long.valueOf(partId));
                    chpo.setPartCname(partCname);
                    chpo.setPartOldcode(partOldcode);
//	            	chpo.setSvcPrice(Double.valueOf(price1));
                    chpo.setSvcPrice(pricePO.getSalePrice1());
                    chpo.setSvcPriceC(Double.valueOf(price1After));
//	            	chpo.setRetailPrice(Double.valueOf(price2));
                    chpo.setRetailPrice(pricePO.getSalePrice2());
                    chpo.setRetailPriceC(Double.valueOf(price2After));
                    chpo.setRemark(remark + " [批量导入] ");
                    chpo.setCreateBy(createBy);
                    chpo.setCreateDate(date);
                    chpo.setState(state);
                    chpo.setStatus(1);
                    chpo.setValidFrom(date);
                    chpo.setValidTo(dateTo);
                    addList.add(chpo);
                }
            }
            dao.insert(addList);
            act.setOutData("success", "success");
            this.partSalePriceInit();
        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "配件销售价格变更申请--导入保存数据失败");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }


    public void deleteDealer() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            String typeId = CommonUtils.checkNull(request.getParamValue("typeId"));
            String id = CommonUtils.checkNull(request.getParamValue("id"));
            List<Map<String, Object>> list = dao.getPrice(typeId);
            if (list.size() <= 0) {
                act.setOutData("error", "不存在供应商!");
                return;
            }
            String dealerIds = CommonUtils.checkNull(list.get(0).get("DEALER_ID"));
            dealerIds = dealerIds.replace("," + id, "");
            dao.updateDealer(typeId, dealerIds);
            act.setOutData("success", "删除成功!");

        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "配件销售价格变更申请");
            logger.error(loginUser, e1);
            act.setException(e1);
            act.setForword(PartSalePriceUrl);
        }
    }
}
