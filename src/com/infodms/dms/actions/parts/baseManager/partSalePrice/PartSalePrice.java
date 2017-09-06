package com.infodms.dms.actions.parts.baseManager.partSalePrice;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.infodms.dms.actions.partsmanage.infoSearch.DealerDlrstockInfo;
import com.infodms.dms.actions.sales.planmanage.PlanUtil.BaseImport;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.constants.PTConstants;
import com.infodms.dms.dao.parts.baseManager.partSalePriceQuery.PartSalePriceDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TtPartPriceSettingPO;
import com.infodms.dms.po.TtPartSalePriceHistoryPO;
import com.infodms.dms.po.TtPartSalesPricePO;
import com.infodms.dms.util.CheckUtil;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.csv.CsvWriterUtil;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;

import jxl.Cell;
import jxl.CellType;
import jxl.DateCell;
import jxl.Workbook;
import jxl.write.Label;

/**
 * @author : bianlz
 *         CreateDate     : 2013-4-7
 * @ClassName : PartSalePrice
 * @Description : 配件销售价格维护
 */
public class PartSalePrice extends BaseImport implements PTConstants {
    public Logger logger = Logger.getLogger(DealerDlrstockInfo.class);
    PartSalePriceDao dao = PartSalePriceDao.getInstance();
    private static final String partSalePriceModUrl = "/jsp/parts/baseManager/partSalePrice/partSalePriceMod.jsp"; //配件主信息维护查询

    public void getDealers() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            //分页方法 begin
            Integer curPage = request.getParamValue("curPage") != null ? Integer
                    .parseInt(request.getParamValue("curPage"))
                    : 1; // 处理当前页
            PageResult<Map<String, Object>> ps = null;

            ps = dao.queryDealers(request, logonUser.getPoseBusType(), curPage, Constant.PAGE_SIZE);
            act.setOutData("ps", ps);
            act.setOutData("curPage", curPage);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "配件销售价格维护");
            logger.error(logonUser, e1);
            act.setException(e1);
            act.setForword(PartSalePriceUrl);
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
    public static Object exportEx(ResponseWrapper response,
                                  RequestWrapper request, String[] head, List<String[]> list)
            throws Exception {

        String name = "配件销售价格列表.xls";
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
     * @Description: 配件销售价格维护初始化
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

            act.setForword(PartSalePriceUrl);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "配件销售价格维护");
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
     * @Description: 查询配件销售价格
     */
    public void queryPartSalePrice() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {

            //分页方法 begin
            Integer curPage = request.getParamValue("curPage") != null ? Integer
                    .parseInt(request.getParamValue("curPage"))
                    : 1; // 处理当前页
            PageResult<Map<String, Object>> ps = null;

            ps = dao.queryPartSalePrice(request, logonUser.getPoseBusType(), curPage, Constant.PAGE_SIZE);
            act.setOutData("ps", ps);
            act.setOutData("curPage", curPage);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "配件销售价格维护");
            logger.error(logonUser, e1);
            act.setException(e1);
            act.setForword(PartSalePriceUrl);
        }

    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-4-7
     * @Title :
     * @Description: 配件销售价格维护保存
     */
    public void savePartSalePrice() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            String curPage = CommonUtils.checkNull(request.getParamValue("curPage"));//当前页
            if ("".equals(curPage)) {
                curPage = "1";
            }
            String priceId = CommonUtils.checkNull(request.getParamValue("PRICE_ID"));//主键ID
            
            //dao.validPartVender(venderId);
            String priceValidStartDate = CommonUtils.checkNull(request.getParamValue("PRICE_VALID_START_DATE"));//调拨价有效开始日期
            String priceValidEndDate = CommonUtils.checkNull(request.getParamValue("PRICE_VALID_END_DATE"));//调拨价有效结束日期
            String salePriceStartDate = CommonUtils.checkNull(request.getParamValue("SALE_PRICE_START_DATE"));//促销调拨价有效开始日期
            String salePriceEndDate = CommonUtils.checkNull(request.getParamValue("SALE_PRICE_END_DATE"));//促销调拨价结束日期
            
            Double SALE_PRICE1 = 0d;
            Double SALE_PRICE2 = 0d;
            Double SALE_PRICE3 = 0d;
            Double SALE_PRICE4 = 0d;
            Double SALE_PRICE5 = 0d;
            Double SALE_PRICE6 = 0d;
            Double SALE_PRICE7 = 0d;
            Double SALE_PRICE8 = 0d;
            Double SALE_PRICE9 = 0d;
            Double SALE_PRICE10 = 0d;
            Double SALE_PRICE11 = 0d;
            Double SALE_PRICE12 = 0d;
            Double SALE_PRICE13 = 0d;
            Double SALE_PRICE14 = 0d;
            Double SALE_PRICE15 = 0d;
            
            if (!"".equals(CommonUtils.checkNull(request.getParamValue("SALE_PRICE1")))) {
                SALE_PRICE1 = Double.valueOf(CommonUtils.checkNull(request.getParamValue("SALE_PRICE1")));//调拨价
            }
            if (!"".equals(CommonUtils.checkNull(request.getParamValue("SALE_PRICE2")))) {
                SALE_PRICE2 = Double.valueOf(CommonUtils.checkNull(request.getParamValue("SALE_PRICE2")));//建议零售价
            }
            if (!"".equals(CommonUtils.checkNull(request.getParamValue("SALE_PRICE3")))) {
                SALE_PRICE3 = Double.valueOf(CommonUtils.checkNull(request.getParamValue("SALE_PRICE3")));//计划价
            }
            if (!"".equals(CommonUtils.checkNull(request.getParamValue("SALE_PRICE4")))) {
                SALE_PRICE4 = Double.valueOf(CommonUtils.checkNull(request.getParamValue("SALE_PRICE4")));//团购价
            }
            if (!"".equals(CommonUtils.checkNull(request.getParamValue("SALE_PRICE5")))) {
                SALE_PRICE5 = Double.valueOf(CommonUtils.checkNull(request.getParamValue("SALE_PRICE5")));//价格5
            }
            if (!"".equals(CommonUtils.checkNull(request.getParamValue("SALE_PRICE6")))) {
                SALE_PRICE6 = Double.valueOf(CommonUtils.checkNull(request.getParamValue("SALE_PRICE6")));//价格6
            }
            if (!"".equals(CommonUtils.checkNull(request.getParamValue("SALE_PRICE7")))) {
                SALE_PRICE7 = Double.valueOf(CommonUtils.checkNull(request.getParamValue("SALE_PRICE7")));//价格7
            }
            if (!"".equals(CommonUtils.checkNull(request.getParamValue("SALE_PRICE8")))) {
                SALE_PRICE8 = Double.valueOf(CommonUtils.checkNull(request.getParamValue("SALE_PRICE8")));//价格8
            }
            if (!"".equals(CommonUtils.checkNull(request.getParamValue("SALE_PRICE9")))) {
                SALE_PRICE9 = Double.valueOf(CommonUtils.checkNull(request.getParamValue("SALE_PRICE9")));//价格9
            }
            if (!"".equals(CommonUtils.checkNull(request.getParamValue("SALE_PRICE10")))) {
                SALE_PRICE10 = Double.valueOf(CommonUtils.checkNull(request.getParamValue("SALE_PRICE10")));//价格10
            }
            if (!"".equals(CommonUtils.checkNull(request.getParamValue("SALE_PRICE11")))) {
                SALE_PRICE11 = Double.valueOf(CommonUtils.checkNull(request.getParamValue("SALE_PRICE11")));//价格11
            }
            if (!"".equals(CommonUtils.checkNull(request.getParamValue("SALE_PRICE12")))) {
                SALE_PRICE12 = Double.valueOf(CommonUtils.checkNull(request.getParamValue("SALE_PRICE12")));//价格12
            }
            if (!"".equals(CommonUtils.checkNull(request.getParamValue("SALE_PRICE13")))) {
                SALE_PRICE13 = Double.valueOf(CommonUtils.checkNull(request.getParamValue("SALE_PRICE13")));//价格13
            }
            if (!"".equals(CommonUtils.checkNull(request.getParamValue("SALE_PRICE14")))) {
                SALE_PRICE14 = Double.valueOf(CommonUtils.checkNull(request.getParamValue("SALE_PRICE14")));//价格14
            }
            if (!"".equals(CommonUtils.checkNull(request.getParamValue("SALE_PRICE15")))) {
                SALE_PRICE15 = Double.valueOf(CommonUtils.checkNull(request.getParamValue("SALE_PRICE15")));//价格15
            }

            //获取原值为插入HIS
            TtPartSalesPricePO po = new TtPartSalesPricePO();
            po.setPriceId(Long.valueOf(priceId));
            List<PO> list = dao.select(po);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            if (null != list && list.size() > 0 && null != list.get(0)) {
                po = (TtPartSalesPricePO) list.get(0);
                TtPartSalePriceHistoryPO hisPo = new TtPartSalePriceHistoryPO();
                Long historyId = Long.parseLong(SequenceManager.getSequence(""));
                hisPo.setHistoryId(historyId);
                hisPo.setPriceId(po.getPriceId());
                hisPo.setCreateBy(logonUser.getUserId());
                hisPo.setCreateDate(CommonUtils.getTZDate());//10118
                hisPo.setPartId(po.getPartId());
                hisPo.setRemark(po.getRemark());
                //add by yuan 20131022 老价格为0取新价格for AS
                if (po.getSalePrice1() == 0) {
                    hisPo.setOldSalePrice1(SALE_PRICE1);
                } else {
                    hisPo.setOldSalePrice1(po.getSalePrice1());
                }
                //end
                hisPo.setOldSalePrice2(po.getSalePrice2());
                hisPo.setOldSalePrice3(po.getSalePrice3());
                hisPo.setOldSalePrice4(po.getSalePrice4());
                hisPo.setOldSalePrice5(po.getSalePrice5());
                hisPo.setOldSalePrice6(po.getSalePrice6());
                hisPo.setOldSalePrice7(po.getSalePrice7());
                hisPo.setOldSalePrice8(po.getSalePrice8());
                hisPo.setOldSalePrice9(po.getSalePrice9());
                hisPo.setOldSalePrice10(po.getSalePrice10());
                hisPo.setOldSalePrice11(po.getSalePrice11());
                hisPo.setOldSalePrice12(po.getSalePrice12());
                hisPo.setOldSalePrice13(po.getSalePrice13());
                hisPo.setOldSalePrice14(po.getSalePrice14());
                hisPo.setOldSalePrice15(po.getSalePrice15());
                hisPo.setSalePrice1(SALE_PRICE1);
                hisPo.setSalePrice2(SALE_PRICE2);
                hisPo.setSalePrice3(SALE_PRICE3);
                hisPo.setSalePrice4(SALE_PRICE4);
                hisPo.setSalePrice5(SALE_PRICE5);
                hisPo.setSalePrice6(SALE_PRICE6);
                hisPo.setSalePrice7(SALE_PRICE7);
                hisPo.setSalePrice8(SALE_PRICE8);
                hisPo.setSalePrice9(SALE_PRICE9);
                hisPo.setSalePrice10(SALE_PRICE10);
                hisPo.setSalePrice11(SALE_PRICE11);
                hisPo.setSalePrice12(SALE_PRICE12);
                hisPo.setSalePrice13(SALE_PRICE13);
                hisPo.setSalePrice14(SALE_PRICE14);
                hisPo.setSalePrice15(SALE_PRICE15);
                hisPo.setState(po.getState());
                hisPo.setStatus(po.getStatus());
                
                hisPo.setOldPriceValidStartDate(po.getPriceValidStartDate());
                hisPo.setOldPriceValidEndDate(po.getPriceValidEndDate());
                if(priceValidStartDate!="" && priceValidEndDate!=""){
                    hisPo.setPriceValidStartDate(sdf.parse(priceValidStartDate));
                    hisPo.setPriceValidEndDate(sdf.parse(priceValidEndDate));
                }
                hisPo.setOldSalePriceStartDate(po.getSalePriceEndDate());
                hisPo.setOldSalePriceEndDate(po.getSalePriceEndDate());
                if(salePriceStartDate!="" && salePriceEndDate!=""){
                    hisPo.setSalePriceStartDate(sdf.parse(salePriceStartDate));
                    hisPo.setSalePriceEndDate(sdf.parse(salePriceEndDate));
                }
                
                dao.insert(hisPo);
            }

            //PartSalePriceDao dao = new PartSalePriceDao();
            TtPartSalesPricePO oldPo = new TtPartSalesPricePO();
            oldPo.setPriceId(Long.valueOf(priceId));
            TtPartSalesPricePO newPo = new TtPartSalesPricePO();
            newPo.setPriceId(Long.valueOf(priceId));
            if (!"".equals(priceValidStartDate) && priceValidStartDate != null){
                newPo.setPriceValidStartDate(sdf.parse(priceValidStartDate));
            }
            if (!"".equals(priceValidEndDate) && priceValidEndDate != null){
                newPo.setPriceValidEndDate(sdf.parse(priceValidEndDate));
            }
            if (!"".equals(salePriceStartDate) && salePriceStartDate != null){
                newPo.setSalePriceStartDate(sdf.parse(salePriceStartDate));
            }
            if (!"".equals(salePriceEndDate) && salePriceEndDate != null){
                newPo.setSalePriceEndDate(sdf.parse(salePriceEndDate));
            }
            if (SALE_PRICE1 != null) {
                newPo.setSalePrice1(SALE_PRICE1);
            }
            if (SALE_PRICE2 != null) {
                newPo.setSalePrice2(SALE_PRICE2);
            }
            if (SALE_PRICE3 != null) {
                newPo.setSalePrice3(SALE_PRICE3);
            }
            if (SALE_PRICE4 != null) {
                newPo.setSalePrice4(SALE_PRICE4);
            }
            if (SALE_PRICE5 != null) {
                newPo.setSalePrice5(SALE_PRICE5);
            }
            if (SALE_PRICE6 != null) {
                newPo.setSalePrice6(SALE_PRICE6);
            }
            if (SALE_PRICE7 != null) {
                newPo.setSalePrice7(SALE_PRICE7);
            }
            if (SALE_PRICE8 != null) {
                newPo.setSalePrice8(SALE_PRICE8);
            }
            if (SALE_PRICE9 != null) {
                newPo.setSalePrice9(SALE_PRICE9);
            }
            if (SALE_PRICE10 != null) {
                newPo.setSalePrice10(SALE_PRICE10);
            }
            if (SALE_PRICE11 != null) {
                newPo.setSalePrice11(SALE_PRICE11);
            }
            if (SALE_PRICE12 != null) {
                newPo.setSalePrice12(SALE_PRICE12);
            }
            if (SALE_PRICE13 != null) {
                newPo.setSalePrice13(SALE_PRICE13);
            }
            if (SALE_PRICE14 != null) {
                newPo.setSalePrice14(SALE_PRICE14);
            }
            if (SALE_PRICE15 != null) {
                newPo.setSalePrice15(SALE_PRICE15);
            }
            newPo.setUpdateBy(logonUser.getUserId());
            newPo.setUpdateDate(CommonUtils.getTZDate());//
            newPo.setValidDate(CommonUtils.getTZDate(new Date(), Constant.Part_SALES_PRICE_FOR_SA));//45天后
            dao.update(oldPo, newPo);

            act.setOutData("success", "ture");
            act.setOutData("curPage", curPage);
            act.setForword(PartSalePriceUrl);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "配件销售价格维护失败,请联系管理员!");
            logger.error(logonUser, e1);
            act.setException(e1);
            act.setForword(PartSalePriceUrl);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-4-7
     * @Title :
     * @Description: 跳转设置价格列表
     */
    public void addPartSalePrice() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            act.setForword(AddPartSalePriceUrl);
        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "配件销售价格维护");
            logger.error(logonUser, e1);
            act.setException(e1);
            act.setForword(AddPartSalePriceUrl);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-4-8
     * @Title :
     * @Description: 设置价格列表
     */
    public void queryPartSalePriceSetting() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            //PartSalePriceDao dao = new PartSalePriceDao();
            //分页方法 begin
            Integer curPage = request.getParamValue("curPage") != null ? Integer
                    .parseInt(request.getParamValue("curPage"))
                    : 1; // 处理当前页
            PageResult<Map<String, Object>> ps = null;

            ps = dao.queryPartPriceSetting(request, curPage, 1000);
            act.setOutData("ps", ps);
            act.setForword(AddPartSalePriceUrl);
        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "配件销售价格维护");
            logger.error(logonUser, e1);
            act.setException(e1);
            act.setForword(AddPartSalePriceUrl);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-4-8
     * @Title :
     * @Description: 更改启动停用状态
     */
    public void changePartSalePriceState() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            Long typeId = Long.valueOf(CommonUtils.checkNull(request.getParamValue("typeId")));
            int status = Integer.valueOf(CommonUtils.checkNull(request.getParamValue("status")));
            String typeDesc = CommonUtils.checkNull(request.getParamValue("typeDesc"));
            int isFirst = Integer.valueOf(CommonUtils.checkNull(request.getParamValue("isFirst")));
            int scopeType = Integer.valueOf(CommonUtils.checkNull(request.getParamValue("scopeType")));
            String dealer = CommonUtils.checkNull(request.getParamValue("dealer"));
            TtPartPriceSettingPO newPo = new TtPartPriceSettingPO();
            TtPartPriceSettingPO oldPo = new TtPartPriceSettingPO();
            newPo.setTypeId(typeId);
            newPo.setUpdateBy(logonUser.getUserId());
            newPo.setUpdateDate(new Date());
            oldPo.setTypeId(typeId);
            if (status == Constant.PART_SALE_PRICE_START) {
                status = Constant.PART_SALE_PRICE_STOP;
            } else {
                typeDesc = new String(typeDesc.getBytes("ISO-8859-1"), "gb2312");
                newPo.setTypeDesc(typeDesc);
                newPo.setIsFirst(isFirst);
                newPo.setScopeType(scopeType);
                if (!"".equals(CommonUtils.checkNull(request.getParamValue("dealerType")))) {
                    int dealerType = Integer.valueOf(CommonUtils.checkNull(request.getParamValue("dealerType")));
                    newPo.setDealerType(dealerType);
                }
                newPo.setDealerId(dealer);
                status = Constant.PART_SALE_PRICE_START;
            }

            newPo.setStatus(status);
            dao.update(oldPo, newPo);
            act.setOutData("result", "sucess");
            act.setOutData("status", status);
            act.setOutData("typeId", typeId);
        } catch (Exception e) {
            act.setOutData("result", "error");
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "配件销售价格维护启用失败");
            logger.error(logonUser, e1);
            act.setException(e1);
            act.setForword(AddPartSalePriceUrl);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-4-8
     * @Title :
     * @Description: 保存设置列表
     */
    public void savePartSalePriceSetting() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            String typeIdStr = request.getParamValue("typeIdStr");
            String isFirst = request.getParamValue("isFirst");
            String typeIds[] = typeIdStr.split("\\|");
            String isFirsts[] = isFirst.split("\\|");
            Map<String, String> isFirstMap = new HashMap();
            for (int i = 1; i < isFirsts.length; i++) {
                String isFirstV[] = isFirsts[i].split("\\:");
                isFirstMap.put(isFirstV[0], isFirstV[1]);
            }
            for (int i = 1; i < typeIds.length; i++) {
                TtPartPriceSettingPO oldPo = new TtPartPriceSettingPO();
                TtPartPriceSettingPO newPo = new TtPartPriceSettingPO();
                Long typeId = Long.valueOf(typeIds[i]);
                String typeDesc = CommonUtils.checkNull(request.getParamValue("TypeDesc_" + typeIds[i]));
                int idFirst = Integer.valueOf(CommonUtils.checkNull(isFirstMap.get("rd_" + typeIds[i])));
                if (null != request.getParamValue("DealerType_" + typeIds[i])) {
                    int dealerType;
                    if ("".equals(request.getParamValue("DealerType_" + typeIds[i]))) {
                        dealerType = 0;
                    }
                    try {
                        dealerType = Integer.valueOf(request.getParamValue("DealerType_" + typeIds[i]));
                    } catch (Exception ex) {
                        BizException e1 = new BizException(act, ex, ErrorCodeConstant.SPECIAL_MEG, "插入价格描述为：" + typeDesc + "的价格的服务商类型出错!");
                        throw e1;
                    }
                    newPo.setDealerType(dealerType);
                }
                if (null != request.getParamValue("scopeType_" + typeIds[i])) {
                    int scopeType;
                    if ("".equals(request.getParamValue("scopeType_" + typeIds[i]))) {
                        scopeType = 0;
                    }
                    try {
                        scopeType = Integer.valueOf(request.getParamValue("scopeType_" + typeIds[i]));
                    } catch (Exception ex) {
                        BizException e1 = new BizException(act, ex, ErrorCodeConstant.SPECIAL_MEG, "插入价格描述为：" + typeDesc + "的价格的价格范围值出错!");
                        throw e1;
                    }
                    newPo.setScopeType(scopeType);
                }


                String dealer = CommonUtils.checkNull(request.getParamValue("Dealer_" + typeIds[i]));
                oldPo.setTypeId(typeId);
                newPo.setTypeId(typeId);
                newPo.setTypeDesc(typeDesc);
                newPo.setIsFirst(idFirst);
                newPo.setDealerId(dealer);
                newPo.setUpdateBy(logonUser.getUserId());
                newPo.setUpdateDate(new Date());
                dao.update(oldPo, newPo);

            }
            act.setOutData("success", "保存成功!");
        } catch (Exception e) {
            act.setOutData("error", "保存失败!");
            if (e instanceof BizException) {
                BizException e1 = (BizException) e;
                logger.error(logonUser, e1);
                act.setException(e1);

                return;
            }
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "配件销售价格维护");
            logger.error(logonUser, e1);
            act.setException(e1);

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
            listHead.add("配件编码");
            listHead.add("实际调拨价有效开始日期");
            listHead.add("实际调拨价有效结束日期");
            listHead.add("促销调拨价有效开始日期");
            listHead.add("促销调拨价有效结束日期");
            List<Map<String, Object>> priceHeadList = dao.queryPartPriceSettingList();
            for (int i = 0; i < priceHeadList.size(); i++) {
                String tempValue = priceHeadList.get(i).get("TYPE_DESC").toString();
                listHead.add(tempValue);
            }
            
//            listHead.add("服务站价格");
//            listHead.add("零售价");
//            listHead.add("计划价");
//            listHead.add("海运价格");
//            listHead.add("空运价格");
//            listHead.add("价格6");
//            listHead.add("价格7");
//            listHead.add("价格8");
//            listHead.add("价格9");
//            listHead.add("价格10");
//            listHead.add("价格11");
//            listHead.add("价格12");
//            listHead.add("价格13");
//            listHead.add("领用价格");
//            listHead.add("价格15");
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
        OutputStream os = null;
        try {
            ResponseWrapper response = act.getResponse();
            RequestWrapper request = act.getRequest();
            //PartSalePriceDao dao = new PartSalePriceDao();
            List<Map<String, Object>> priceHeadList = dao.queryPartPriceSettingList();
            List<Object> head = new ArrayList<Object>();
            head.add("件号");
            head.add("配件编码");
            head.add("配件名称");
            head.add("实际调拨价有效开始日期");
            head.add("实际调拨价有效结束日期");
            head.add("促销调拨价有效开始日期");
            head.add("促销调拨价有效结束日期");
            for (int i = 0; i < priceHeadList.size(); i++) {
                String tempValue = priceHeadList.get(i).get("TYPE_DESC").toString();
                head.add(tempValue);
            }

            List<Map<String, Object>> list = dao.queryPartSalePriceForExport(request);
            List<List<Object>> expList = new ArrayList<List<Object>>();
            expList.add(head);
            if (list != null && list.size() != 0) {
                for (int i = 0; i < list.size(); i++) {
                    Map<String, Object> map = (Map<String, Object>) list.get(i);
                    if (map != null && map.size() != 0) {
                        List<Object> deatil = new ArrayList<Object>();
                        deatil.add(CommonUtils.checkNull(map.get("PART_CODE")));
                        deatil.add(CommonUtils.checkNull(map.get("PART_OLDCODE")));
                        deatil.add(CommonUtils.checkNull(map.get("PART_CNAME")));
                        deatil.add(CommonUtils.checkNull(map.get("PRICE_VALID_START_DATE")));
                        deatil.add(CommonUtils.checkNull(map.get("PRICE_VALID_END_DATE")));
                        deatil.add(CommonUtils.checkNull(map.get("SALE_PRICE_START_DATE")));
                        deatil.add(CommonUtils.checkNull(map.get("SALE_PRICE_END_DATE")));
                        for (int n = 0; n < priceHeadList.size(); n++) {
                            deatil.add(CommonUtils.checkNull(map.get("SALE_PRICE"+(n+1))));
                        }
                        expList.add(deatil);
                    }
                }
            }
            // 导出的文件名
            String fileName = "配件销售价格.xls";
            // 导出的文字编码
            fileName = new String(fileName.getBytes("GB2312"), "ISO8859-1");
            response.setContentType("Application/text/xls");
            response.addHeader("Content-Disposition", "attachment;filename=" + fileName);
            os = response.getOutputStream();
            CsvWriterUtil.createXlsFile(expList, os);
        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "");
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
     * @Description: 导入数据
     */
    public void loadPartPriceDataIntoDB() throws Exception {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        StringBuffer errorInfo = new StringBuffer("");
        RequestWrapper request = act.getRequest();
        try {
            long maxSize = 1024 * 1024 * 5;
            int errNum = insertIntoTmp(request, "uploadFile", 20, 3, maxSize);

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
                List<TtPartSalesPricePO> voList = new ArrayList<TtPartSalesPricePO>();
                loadVoList(voList, list, errorInfo);
                if (errorInfo.length() > 0) {
                    BizException e1 = new BizException(act, ErrorCodeConstant.SPECIAL_MEG, errorInfo);
                    throw e1;
                }
                for (int i = 0; i < voList.size(); i++) {
                    TtPartSalesPricePO po = (TtPartSalesPricePO) voList.get(i);

                    String partId = po.getPartId() + "";

                    String sqlStr = " AND SP.PART_ID = '" + partId + "'";
                    List<Map<String, Object>> prList = dao.CheckPriceExist(sqlStr);

                    if (null != prList && prList.size() > 0) {
                        TtPartSalesPricePO selPo = new TtPartSalesPricePO();
                        TtPartSalesPricePO updPo = new TtPartSalesPricePO();

                        selPo.setPartId(Long.parseLong(partId));
                        
                        updPo.setPriceValidStartDate(po.getPriceValidStartDate());
                        updPo.setPriceValidEndDate(po.getPriceValidEndDate());
                        updPo.setSalePriceStartDate(po.getSalePriceStartDate());
                        updPo.setSalePriceEndDate(po.getSalePriceEndDate());
                        if (null != po.getSalePrice1() && !"".equals(po.getSalePrice1())) {
                            updPo.setSalePrice1(po.getSalePrice1());
                        }
                        if (null != po.getSalePrice2() && !"".equals(po.getSalePrice2())) {
                            updPo.setSalePrice2(po.getSalePrice2());
                        }
                        if (null != po.getSalePrice3() && !"".equals(po.getSalePrice3())) {
                            updPo.setSalePrice3(po.getSalePrice3());
                        }
                        if (null != po.getSalePrice4() && !"".equals(po.getSalePrice4())) {
                            updPo.setSalePrice4(po.getSalePrice4());
                        }
                        if (null != po.getSalePrice5() && !"".equals(po.getSalePrice5())) {
                            updPo.setSalePrice5(po.getSalePrice5());
                        }
                        if (null != po.getSalePrice6() && !"".equals(po.getSalePrice6())) {
                            updPo.setSalePrice6(po.getSalePrice6());
                        }
                        if (null != po.getSalePrice7() && !"".equals(po.getSalePrice7())) {
                            updPo.setSalePrice7(po.getSalePrice7());
                        }
                        if (null != po.getSalePrice8() && !"".equals(po.getSalePrice8())) {
                            updPo.setSalePrice8(po.getSalePrice8());
                        }
                        if (null != po.getSalePrice9() && !"".equals(po.getSalePrice9())) {
                            updPo.setSalePrice9(po.getSalePrice9());
                        }
                        if (null != po.getSalePrice10() && !"".equals(po.getSalePrice10())) {
                            updPo.setSalePrice10(po.getSalePrice10());
                        }
                        if (null != po.getSalePrice11() && !"".equals(po.getSalePrice11())) {
                            updPo.setSalePrice11(po.getSalePrice11());
                        }
                        if (null != po.getSalePrice12() && !"".equals(po.getSalePrice12())) {
                            updPo.setSalePrice12(po.getSalePrice12());
                        }
                        if (null != po.getSalePrice13() && !"".equals(po.getSalePrice13())) {
                            updPo.setSalePrice13(po.getSalePrice13());
                        }
                        if (null != po.getSalePrice14() && !"".equals(po.getSalePrice14())) {
                            updPo.setSalePrice14(po.getSalePrice14());
                        }
                        if (null != po.getSalePrice15() && !"".equals(po.getSalePrice15())) {
                            updPo.setSalePrice15(po.getSalePrice15());
                        }
                        updPo.setUpdateDate(CommonUtils.getTZDate());
                        updPo.setUpdateBy(logonUser.getUserId());
                        updPo.setValidDate(CommonUtils.getTZDate(new Date(), Constant.Part_SALES_PRICE_FOR_SA));
                        insertHistory(po.getPartId().toString(), po, logonUser, 1);
                        dao.update(selPo, updPo);

                    } else {
                        Long priceId = Long.parseLong(SequenceManager.getSequence(""));
                        po.setPriceId(priceId);
                        po.setCreateDate(CommonUtils.getTZDate());
                        po.setCreateBy(logonUser.getUserId());
                        insertHistory(po.getPartId().toString(), po, logonUser, 0);
                        dao.insert(po);
                    }


                }
                partSalePriceInit();
            }

        } catch (Exception e) {
            if (e instanceof BizException) {
                BizException e1 = (BizException) e;
                logger.error(logonUser, e1);
                act.setException(e1);
                act.setForword(PartSalePriceUrl);
                return;
            }
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "配件销售价格维护");
            logger.error(logonUser, e1);
            act.setException(e1);
            act.setForword(PartSalePriceUrl);
        }
    }

    /**
     * @param : @param voList
     * @param : @param list
     * @param : @param errorInfo
     * @param : @throws Exception
     * @return :
     * @throws : LastDate    : 2013-4-8
     * @Title :
     * @Description: 导入数据
     */
    private void loadVoList(List<TtPartSalesPricePO> voList, List<Map> list, StringBuffer errorInfo) throws Exception {
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

    /**
     * @param : @param list
     * @param : @param rowNum
     * @param : @param cells
     * @param : @param errorInfo
     * @param : @throws Exception
     * @return :
     * @throws : LastDate    : 2013-4-8
     * @Title :
     * @Description: 导入数据
     */
    private void parseCells(List<TtPartSalesPricePO> list, String rowNum, Cell[] cells, StringBuffer errorInfo) throws Exception {
        TtPartSalesPricePO po = new TtPartSalesPricePO();
        //PartSalePriceDao dao = new PartSalePriceDao();
        int size = 0;
        if (null != cells) {
            size = cells.length;
            int index = 0;
            int length = 1;
            if (size >= length && "" == CommonUtils.checkNull(cells[index].getContents().trim())) {
                errorInfo.append("配件编码不能为空!");
                return;
            }
            String partId = dao.getPartId(CommonUtils.checkNull(cells[index].getContents().trim()));
            if ("".equals(partId)) {
                errorInfo.append("配件编码【" + cells[0].getContents().trim() + "】不存在!");
                return;
            }
            po.setPartId(Long.valueOf(partId));
            
            index++; length++;
            if (size >= length && !"".equals(subCell(cells[index].getContents().trim()))) {
                Date startDate = null;
                if(cells[index].getType() == CellType.DATE){
                    DateCell dc = (DateCell) cells[index];
                    startDate = dc.getDate();
                }else{
                    errorInfo.append("第" + rowNum + "行实际调拨价有效开始日期格式错误！");
                    return;
                }
                po.setPriceValidStartDate(startDate);
            }

            index++; length++;
            if (size >= length && !"".equals(subCell(cells[index].getContents().trim()))) {
                Date endDate = null;
                if(cells[index].getType() == CellType.DATE){
                    DateCell dc = (DateCell) cells[index];
                    endDate = dc.getDate();
                }else{
                    errorInfo.append("第" + rowNum + "行实际调拨价有效结束日期格式错误！");
                    return;
                }
                po.setPriceValidEndDate(endDate);
            }
            
            index++; length++;
            if (size >= length && !"".equals(subCell(cells[index].getContents().trim()))) {
                Date startDate = null;
                if(cells[index].getType() == CellType.DATE){
                    DateCell dc = (DateCell) cells[index];
                    startDate = dc.getDate();
                }else{
                    errorInfo.append("第" + rowNum + "行促销调拨价有效开始日期格式错误！");
                    return;
                }
                po.setSalePriceStartDate(startDate);
            }
            
            index++; length++;
            if (size >= length && !"".equals(subCell(cells[index].getContents().trim()))) {
                Date endDate = null;
                if(cells[index].getType() == CellType.DATE){
                    DateCell dc = (DateCell) cells[index];
                    endDate = dc.getDate();
                }else{
                    errorInfo.append("第" + rowNum + "行促销调拨价有效结束日期格式错误！");
                    return;
                }
                po.setSalePriceEndDate(endDate);
            }
            
            int methodInde = 1;
            Class<?> poClass = po.getClass();
            for(int i = 5; i < size; i++){
                if(methodInde == 16){
                    break;
                }
                index++; length++;
                if (size >= length && !"".equals(subCell(cells[index].getContents().trim()))) {
                    Double value = Double.valueOf(CommonUtils.checkNull(cells[index].getContents().trim().replace(",", "")));
                    Method method = poClass.getMethod("setSalePrice"+methodInde, Double.class);
                    method.invoke(po, value);
                }
                methodInde++;
            }

        }
        list.add(po);
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
        if (orgAmt.length() > 30) {
            newAmt = orgAmt.substring(0, 30);
        } else {
            newAmt = orgAmt;
        }
        return newAmt;
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-4-7
     * @Title :
     * @Description: 查询修改历史
     */
    public void queryModifyHis() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            String query = CommonUtils.checkNull(request.getParamValue("query"));// 件号
            if (null != query && "json".equalsIgnoreCase(query)) {
                //分页方法 begin
                Integer curPage = request.getParamValue("curPage") != null ? Integer
                        .parseInt(request.getParamValue("curPage"))
                        : 1; // 处理当前页
                PageResult<Map<String, Object>> ps = null;

                ps = dao.queryPartSalePriceHis(request, curPage, Constant.PAGE_SIZE);
                act.setOutData("ps", ps);
                act.setOutData("curPage", curPage);
            } else {
                String priceId = CommonUtils.checkNull(request.getParamValue("priceId"));// 件号

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
                act.setOutData("priceId", priceId);
                act.setForword(partSalePriceHisUrl);
            }

        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "配件销售价格维护");
            logger.error(loginUser, e1);
            act.setException(e1);
            act.setForword(PartSalePriceUrl);
        }
    }

    /**
     * @param partId    配件ID
     * @param newPo     新价格
     * @param logonUser
     * @param flag
     */
    public void insertHistory(String partId, TtPartSalesPricePO newPo, AclUserBean logonUser, int flag) {

        TtPartSalesPricePO oldpo = new TtPartSalesPricePO();
        oldpo.setPartId(Long.valueOf(partId));
        List<PO> list = dao.select(oldpo);
        //如果存在销售价格
        if (flag == 1) {
            oldpo = (TtPartSalesPricePO) list.get(0);
            TtPartSalePriceHistoryPO hisPo = new TtPartSalePriceHistoryPO();
            Long historyId = Long.parseLong(SequenceManager.getSequence(""));
            hisPo.setHistoryId(historyId);
            hisPo.setPriceId(oldpo.getPriceId());
            hisPo.setCreateBy(logonUser.getUserId());
            hisPo.setCreateDate(CommonUtils.getTZDate());//10118
            hisPo.setPartId(oldpo.getPartId());
            hisPo.setRemark(oldpo.getRemark());
            hisPo.setOldSalePrice1(oldpo.getSalePrice1());
            hisPo.setOldSalePrice2(oldpo.getSalePrice2());
            hisPo.setOldSalePrice3(oldpo.getSalePrice3());
            hisPo.setOldSalePrice4(oldpo.getSalePrice4());
            hisPo.setOldSalePrice5(oldpo.getSalePrice5());
            hisPo.setOldSalePrice6(oldpo.getSalePrice6());
            hisPo.setOldSalePrice7(oldpo.getSalePrice7());
            hisPo.setOldSalePrice8(oldpo.getSalePrice8());
            hisPo.setOldSalePrice9(oldpo.getSalePrice9());
            hisPo.setOldSalePrice10(oldpo.getSalePrice10());
            hisPo.setOldSalePrice11(oldpo.getSalePrice11());
            hisPo.setOldSalePrice12(oldpo.getSalePrice12());
            hisPo.setOldSalePrice13(oldpo.getSalePrice13());
            hisPo.setOldSalePrice14(oldpo.getSalePrice14());
            hisPo.setOldSalePrice15(oldpo.getSalePrice15());
            hisPo.setSalePrice1(newPo.getSalePrice1() == null ? 0 : newPo.getSalePrice1());
            hisPo.setSalePrice2(newPo.getSalePrice2() == null ? 0 : newPo.getSalePrice2());
            hisPo.setSalePrice3(newPo.getSalePrice3() == null ? 0 : newPo.getSalePrice3());
            hisPo.setSalePrice4(newPo.getSalePrice4() == null ? 0 : newPo.getSalePrice4());
            hisPo.setSalePrice5(newPo.getSalePrice5() == null ? 0 : newPo.getSalePrice5());
            hisPo.setSalePrice6(newPo.getSalePrice6() == null ? 0 : newPo.getSalePrice6());
            hisPo.setSalePrice7(newPo.getSalePrice7() == null ? 0 : newPo.getSalePrice7());
            hisPo.setSalePrice8(newPo.getSalePrice8() == null ? 0 : newPo.getSalePrice8());
            hisPo.setSalePrice9(newPo.getSalePrice9() == null ? 0 : newPo.getSalePrice9());
            hisPo.setSalePrice10(newPo.getSalePrice10() == null ? 0 : newPo.getSalePrice10());
            hisPo.setSalePrice11(newPo.getSalePrice11() == null ? 0 : newPo.getSalePrice11());
            hisPo.setSalePrice12(newPo.getSalePrice12() == null ? 0 : newPo.getSalePrice12());
            hisPo.setSalePrice13(newPo.getSalePrice13() == null ? 0 : newPo.getSalePrice13());
            hisPo.setSalePrice14(newPo.getSalePrice14() == null ? 0 : newPo.getSalePrice14());
            hisPo.setSalePrice15(newPo.getSalePrice15() == null ? 0 : newPo.getSalePrice15());
            hisPo.setState(Constant.STATUS_ENABLE);
            hisPo.setStatus(1);
            dao.insert(hisPo);
        } else {
            TtPartSalePriceHistoryPO hisPo = new TtPartSalePriceHistoryPO();
            Long historyId = Long.parseLong(SequenceManager.getSequence(""));
            hisPo.setHistoryId(historyId);
            hisPo.setPriceId(newPo.getPriceId());
            hisPo.setCreateBy(logonUser.getUserId());
            hisPo.setCreateDate(CommonUtils.getTZDate());//10118
            hisPo.setPartId(newPo.getPartId());
            hisPo.setRemark(newPo.getRemark());
            hisPo.setOldSalePrice1(newPo.getSalePrice1() == null ? 0 : newPo.getSalePrice1());
            hisPo.setOldSalePrice2(newPo.getSalePrice2() == null ? 0 : newPo.getSalePrice2());
            hisPo.setOldSalePrice3(newPo.getSalePrice3() == null ? 0 : newPo.getSalePrice3());
            hisPo.setOldSalePrice4(newPo.getSalePrice4() == null ? 0 : newPo.getSalePrice4());
            hisPo.setOldSalePrice5(newPo.getSalePrice5() == null ? 0 : newPo.getSalePrice5());
            hisPo.setOldSalePrice6(newPo.getSalePrice6() == null ? 0 : newPo.getSalePrice6());
            hisPo.setOldSalePrice7(newPo.getSalePrice7() == null ? 0 : newPo.getSalePrice7());
            hisPo.setOldSalePrice8(newPo.getSalePrice8() == null ? 0 : newPo.getSalePrice8());
            hisPo.setOldSalePrice9(newPo.getSalePrice9() == null ? 0 : newPo.getSalePrice9());
            hisPo.setOldSalePrice10(newPo.getSalePrice10() == null ? 0 : newPo.getSalePrice10());
            hisPo.setOldSalePrice11(newPo.getSalePrice11() == null ? 0 : newPo.getSalePrice11());
            hisPo.setOldSalePrice12(newPo.getSalePrice12() == null ? 0 : newPo.getSalePrice12());
            hisPo.setOldSalePrice13(newPo.getSalePrice13() == null ? 0 : newPo.getSalePrice13());
            hisPo.setOldSalePrice14(newPo.getSalePrice14() == null ? 0 : newPo.getSalePrice14());
            hisPo.setOldSalePrice15(newPo.getSalePrice15() == null ? 0 : newPo.getSalePrice15());
            hisPo.setSalePrice1(newPo.getSalePrice1() == null ? 0 : newPo.getSalePrice1());
            hisPo.setSalePrice2(newPo.getSalePrice2() == null ? 0 : newPo.getSalePrice2());
            hisPo.setSalePrice3(newPo.getSalePrice3() == null ? 0 : newPo.getSalePrice3());
            hisPo.setSalePrice4(newPo.getSalePrice4() == null ? 0 : newPo.getSalePrice4());
            hisPo.setSalePrice5(newPo.getSalePrice5() == null ? 0 : newPo.getSalePrice5());
            hisPo.setSalePrice6(newPo.getSalePrice6() == null ? 0 : newPo.getSalePrice6());
            hisPo.setSalePrice7(newPo.getSalePrice7() == null ? 0 : newPo.getSalePrice7());
            hisPo.setSalePrice8(newPo.getSalePrice8() == null ? 0 : newPo.getSalePrice8());
            hisPo.setSalePrice9(newPo.getSalePrice9() == null ? 0 : newPo.getSalePrice9());
            hisPo.setSalePrice10(newPo.getSalePrice10() == null ? 0 : newPo.getSalePrice10());
            hisPo.setSalePrice11(newPo.getSalePrice11() == null ? 0 : newPo.getSalePrice11());
            hisPo.setSalePrice12(newPo.getSalePrice12() == null ? 0 : newPo.getSalePrice12());
            hisPo.setSalePrice13(newPo.getSalePrice13() == null ? 0 : newPo.getSalePrice13());
            hisPo.setSalePrice14(newPo.getSalePrice14() == null ? 0 : newPo.getSalePrice14());
            hisPo.setSalePrice15(newPo.getSalePrice15() == null ? 0 : newPo.getSalePrice15());
            hisPo.setState(Constant.STATUS_ENABLE);
            hisPo.setStatus(1);
            dao.insert(hisPo);
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
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "配件销售价格维护");
            logger.error(loginUser, e1);
            act.setException(e1);
            act.setForword(PartSalePriceUrl);
        }
    }
}
