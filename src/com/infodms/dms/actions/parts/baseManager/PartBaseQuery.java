package com.infodms.dms.actions.parts.baseManager;

import com.infodms.dms.actions.sales.planmanage.PlanUtil.BaseImport;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.constants.PTConstants;
import com.infodms.dms.dao.parts.baseManager.PartBaseQueryDao;
import com.infodms.dms.dao.parts.baseManager.partPlannerQueryManager.partPlannerQueryDao;
import com.infodms.dms.dao.parts.baseManager.partSalePriceQuery.PartSalePriceDao;
import com.infodms.dms.dao.parts.baseManager.partsBaseManager.PartFixcodeDAO;
import com.infodms.dms.dao.parts.purchaseManager.purchasePlanSetting.PurchasePlanSettingDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.*;
import com.infodms.dms.util.CheckUtil;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.StringUtil;
import com.infodms.dms.util.csv.CsvWriterUtil;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.mvc.context.ResponseWrapper;
import com.infoservice.po3.bean.PageResult;
import jxl.Cell;
import jxl.Workbook;
import jxl.write.Label;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.crypto.spec.PSource;

/**
 * @author : bianlz
 *         CreateDate     : 2013-4-1
 * @ClassName : PartBaseQuery
 * @Description : TODO
 */
public class PartBaseQuery extends BaseImport implements PTConstants {
    private static final String INPUT_ERROR_URL = "/jsp/parts/baseManager/inputError.jsp";//数据导入出错页面
    public static Logger logger = Logger.getLogger(PartBaseQuery.class);
    PartBaseQueryDao dao = PartBaseQueryDao.getInstance();
    String PartHisQueryUrl = "/jsp/parts/baseManager/PartBaseHisQuery.jsp"; //配件主信息维护查询
    String PART_SALESBASE_QUERY_URL = "/jsp/parts/baseManager/PartSalesBaseQuery.jsp"; //销售主数据维护查询
    private final String uploadErrUrl = "/jsp/parts/servicerInforManager/partUploadInputError.jsp";

    public static Object exportEx(String fileName, ResponseWrapper response,
                                  RequestWrapper request, String[] head, List<String[]> list)
            throws Exception {

        jxl.write.WritableWorkbook wwb = null;
        OutputStream out = null;
        try {
            response.setContentType("application/octet-stream");
            response.addHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode(fileName, "utf-8"));
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
     * @throws : LastDate    : 2013-4-1
     * @Title : 配件主信息维护初始化
     * @Description: TODO
     */
    public void partBaseQueryInit() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            // 获取价格的变量
            List<Map<String, Object>> list = PartSalePriceDao.getInstance().queryPartPriceSettingList();
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
            
            PurchasePlanSettingDao dao2 = PurchasePlanSettingDao.getInstance();
            List<Map<String, Object>> planerList = dao2.getUserPoseLise(1, null);
            act.setForword(PartBaseQueryUrl);
            act.setOutData("map", map);
            act.setOutData("sortList", dao.getSortList());
            act.setOutData("planerList", planerList);
            // 艾春 10.08添加 东安数据过滤
            act.setOutData("poseBusType", logonUser.getPoseBusType());
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "配件主信息维护初始化");
            logger.error(logonUser, e1);
            act.setException(e1);
            act.setForword(PartBaseQueryUrl);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-4-1
     * @Title : 销售主数据维护初始化
     * @Description: TODO
     */
    public void partSalesBaseQueryInit() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            act.setForword(PART_SALESBASE_QUERY_URL);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "销售主数据");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-4-2
     * @Title : 配件主信息维护信息查询
     * @Description: TODO
     */
    public void queryPartBaseInfo() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        // 艾春 10.08 添加 获得职位
        Integer poseBusType = logonUser.getPoseBusType();
        try {
            //PartBaseQueryDao dao = PartBaseQueryDao.getInstance();
            //分页方法 begin
            Integer curPage = request.getParamValue("curPage") != null ? Integer
                    .parseInt(request.getParamValue("curPage"))
                    : 1; // 处理当前页
            PageResult<Map<String, Object>> ps = null;

            ps = dao.queryPartBase(request, curPage, Constant.PAGE_SIZE, poseBusType);
            act.setOutData("ps", ps);
            act.setOutData("sortList", dao.getSortList());
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "配件主信息维护");
            logger.error(logonUser, e1);
            act.setException(e1);

        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-4-2
     * @Title : 销售主数据信息查询
     * @Description: TODO
     */
    public void partSalesBaseQuery() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            //分页方法 begin
            Integer curPage = request.getParamValue("curPage") != null ? Integer
                    .parseInt(request.getParamValue("curPage"))
                    : 1; // 处理当前页
            PageResult<Map<String, Object>> ps = null;

            ps = dao.queryPartSalesBase(request, curPage, Constant.PAGE_SIZE);
            act.setOutData("ps", ps);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "销售主数据");
            logger.error(logonUser, e1);
            act.setException(e1);

        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-4-2
     * @Title : 保存
     * @Description: TODO
     */
    public void savePartSalesBase() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            String partId = CommonUtils.checkNull(request.getParamValue("partId")); //配件Id
            String isPlan = CommonUtils.checkNull(request.getParamValue("isPlan")); //是否计划
            String isDirect = CommonUtils.checkNull(request.getParamValue("isDirect")); //是否计划
            String packState = CommonUtils.checkNull(request.getParamValue("packState"));//包装发运方式
            String isLack = CommonUtils.checkNull(request.getParamValue("isLack"));//是否紧缺
            String ofFlag = CommonUtils.checkNull(request.getParamValue("ofFlag"));//是否流失关注件
            String isRecv1 = CommonUtils.checkNull(request.getParamValue("isRecv1"));//是否特殊配件
            String isGxp = CommonUtils.checkNull(request.getParamValue("isGxp"));//是否广宣品

            Long userId = logonUser.getUserId();
            TtPartDefinePO selPo = new TtPartDefinePO();
            TtPartDefinePO updatePo = new TtPartDefinePO();

            selPo.setPartId(Long.parseLong(partId));

            updatePo.setIsPlan(Integer.parseInt(isPlan));
            updatePo.setIsDirect(Integer.parseInt(isDirect));
            //updatePo.setPackState(Integer.parseInt(packState));
            updatePo.setIsLack(Integer.parseInt(isLack));
            updatePo.setOfFlag(Integer.parseInt(ofFlag));
            updatePo.setIsSpecial(Integer.parseInt(isRecv1));
            updatePo.setIsGxp(Integer.parseInt(isGxp));
            updatePo.setUpdateBy(userId);
            updatePo.setUpdateDate(new Date());

            String curPage = CommonUtils.checkNull(request.getParamValue("curPage"));//当前页
            if ("".equals(curPage)) {
                curPage = "1";
            }
            dao.update(selPo, updatePo);
            act.setOutData("success", "true");
            act.setOutData("curPage", curPage);
        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.UPDATE_FAILURE_CODE, "销售主数据");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-4-2
     * @Title : 配件主信息维护详细信息失效和修改初始化页面
     * @Description: 
     */
    public void queryPartBaseDetail() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper req = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        logonUser.getDealerId();
        String forword = "";
        try {
            String partId = CommonUtils.checkNull(req.getParamValue("partId"));//所选查看信息
            String flag = CommonUtils.checkNull(req.getParamValue("flag"));
            String curPage = CommonUtils.checkNull(req.getParamValue("curPage"));//当前页
            if ("".equals(curPage)) {
                curPage = "1";
            }
            //PartBaseQueryDao dao = PartBaseQueryDao.getInstance();
            Map<String, Object> ps = null;
            ps = dao.queryPartDetail(partId);
            if (flag.equals("mod") || flag.equals("view")) { //修改，详情
                Map<String, Object> carInfoMap = new HashMap<String, Object>(); // 车辆信息map
                // 适用品牌基础数据
                String brandName = null;
                List<Map<String, Object>> branList = PartFixcodeDAO.getInstance().getPartFixcodeListByTypeId(Constant.FIXCODE_TYPE_09);
                carInfoMap.put("branList", branList);
                
                // 适用车型基础数据
                List<Map<String, Object>> carTypeList = PartFixcodeDAO.getInstance().getPartFixcodeListByTypeId(Constant.FIXCODE_TYPE_10, brandName);
                carInfoMap.put("carTypeList", carTypeList);
//                carInfoMap.put("carTypeChkeds", dao.getPartCarTypes(partId)); 
                // 选中的车型
                Object modelId = ps.get("MODEL_ID");
                if(modelId != null && !"".equals(modelId)){
                    carInfoMap.put("carTypeChkeds", modelId.toString().split(","));
                }
                
                // 适用车系基础数据
                List<Map<String, Object>> seriesList = PartFixcodeDAO.getInstance().getPartFixcodeListByTypeId(Constant.FIXCODE_TYPE_11);
                carInfoMap.put("carSeriesList", seriesList);
                // 选中的车系
                Object seriesId = ps.get("SERIES_ID");
                if(seriesId != null && !"".equals(seriesId)){
                    carInfoMap.put("carSeriesChkeds", seriesId.toString().split(","));
                }
                
                act.setOutData("carInfo", carInfoMap);
                if(flag.equals("view")){
                    forword = PartBaseQueryDetailUrl;
                }else{
                    //获取配件计量单位信息
                    List<TtPartFixcodeDefinePO> list = dao.getPartUnitList(Constant.FIXCODE_TYPE_02);
                    act.setOutData("unit", list);
                    forword = PartBaseQueryEditUrl;
                }
            } else if (flag.equals("abate")) {   //失效
                TtPartDefinePO oldPo = new TtPartDefinePO();
                oldPo.setPartId(Long.valueOf(partId));
                TtPartDefinePO newPo = new TtPartDefinePO();
                //newPo.setPartId(Long.valueOf(partId));
                newPo.setState(Constant.STATUS_DISABLE);
                newPo.setUpdateBy(logonUser.getUserId());
                newPo.setUpdateDate(new Date());
                newPo.setStatus(0);
                this.saveHistory(partId);
                dao.update(oldPo, newPo);

                //add zhumingwei 2013-09-17
                TtPartBuyPricePO po = new TtPartBuyPricePO();
                po.setPartId(Long.valueOf(partId));
                TtPartBuyPricePO poValue = new TtPartBuyPricePO();
                poValue.setState(Constant.STATUS_DISABLE);
                dao.update(po, poValue);
                //add zhumingwei 2013-09-17
                //计划相关也置为无效 20131113
                TtPartPlanDefinePO srcPo = new TtPartPlanDefinePO();
                srcPo.setPartId(Long.valueOf(partId));
                TtPartPlanDefinePO updatePo = new TtPartPlanDefinePO();
                updatePo.setState(Constant.STATUS_DISABLE);
                updatePo.setUpdateBy(logonUser.getUserId());
                updatePo.setUpdateDate(new Date());
                dao.update(srcPo, updatePo);

                act.setForword(PartBaseQueryUrl);
                act.setOutData("success", "失效成功!");
                act.setOutData("curPage", curPage);
                return;
            } else if (flag.equals("enable")) {   //有效
                TtPartDefinePO oldPo = new TtPartDefinePO();
                oldPo.setPartId(Long.valueOf(partId));
                TtPartDefinePO newPo = new TtPartDefinePO();
                //newPo.setPartId(Long.valueOf(partId));
                newPo.setState(Constant.STATUS_ENABLE);
                newPo.setStatus(1);
                newPo.setUpdateBy(logonUser.getUserId());
                newPo.setUpdateDate(new Date());
                dao.update(oldPo, newPo);

                //add zhumingwei 2013-09-17
                TtPartBuyPricePO po = new TtPartBuyPricePO();
                po.setPartId(Long.valueOf(partId));
                TtPartBuyPricePO poValue = new TtPartBuyPricePO();
                poValue.setState(Constant.STATUS_ENABLE);
                poValue.setUpdateBy(logonUser.getUserId());
                poValue.setUpdateDate(new Date());
                dao.update(po, poValue);
                //add zhumingwei 2013-09-17

                //计划相关也置为有效 20131113
                TtPartPlanDefinePO srcPo = new TtPartPlanDefinePO();
                srcPo.setPartId(Long.valueOf(partId));
                TtPartPlanDefinePO updatePo = new TtPartPlanDefinePO();
                updatePo.setState(Constant.STATUS_ENABLE);
                updatePo.setUpdateBy(logonUser.getUserId());
                updatePo.setUpdateDate(new Date());
                dao.update(srcPo, updatePo);

                act.setForword(PartBaseQueryUrl);
                act.setOutData("success", "设置成功!");
                act.setOutData("curPage", curPage);
                return;
            }

            
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String now = sdf.format(date);
            Map<String, String> map = new HashMap<String, String>();
            ps.put("DEFEND_DATE", now);
            act.setOutData("ps", ps);
            act.setOutData("curPage", curPage);
            // 艾春 10.08添加 东安数据过滤
            act.setOutData("poseBusType", logonUser.getPoseBusType());
            act.setForword(forword);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "配件主信息维护详细信息");
            logger.error(logonUser, e1);
            act.setException(e1);
            act.setForword(PartBaseQueryUrl);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-4-3
     * @Title : 配件主信息维护修改
     * @Description: TODO
     */
    public void partBaseMod() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper req = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            String partId = CommonUtils.checkNull(req.getParamValue("PART_ID"));//配件ID
            saveHistory(partId);
            String partCode = CommonUtils.checkNull(req.getParamValue("PART_CODE"));//配件件号
            String partCname = CommonUtils.checkNull(req.getParamValue("PART_CNAME"));//配件名称
            String partEname = CommonUtils.checkNull(req.getParamValue("PART_ENAME"));//配件英文名称
//            String sortCode = CommonUtils.checkNull(req.getParamValue("SORT_CODE"));//分类代码
//            String groupCode = CommonUtils.checkNull(req.getParamValue("GROUP_CODE"));//分组代码
//            String isEngine = CommonUtils.checkNull(req.getParamValue("IS_ENGINE"));//发动机标记
//            String isExport = CommonUtils.checkNull(req.getParamValue("IS_EXPORT"));//是否出口
//            String isGift = CommonUtils.checkNull(req.getParamValue("IS_GIFT"));//是否为赠品
            String isPermanent = CommonUtils.checkNull(req.getParamValue("IS_PERMANENT"));//是否常备
            String isReplaced = CommonUtils.checkNull(req.getParamValue("IS_REPLACED"));//是否已替代
//            String produceState = CommonUtils.checkNull(req.getParamValue("PRODUCE_STATE"));//采购状态
            String produceFac = CommonUtils.checkNull(req.getParamValue("PRODUCE_FAC"));// 采购方式
//            String packState = CommonUtils.checkNull(req.getParamValue("PACK_STATE"));//包装发运方式
            String partMaterial = CommonUtils.checkNull(req.getParamValue("PART_MATERIAL"));//材料属性
//            String partType = CommonUtils.checkNull(req.getParamValue("PART_TYPE"));//配件种类
            String produceState = CommonUtils.checkNull(req.getParamValue("PRODUCE_STATE"));//配件种类
//            String mobility = CommonUtils.checkNull(req.getParamValue("MOBILITY"));//配件流动性
            String uom = CommonUtils.checkNull(req.getParamValue("UOM"));//配件单位
//            String isAssembly = CommonUtils.checkNull(req.getParamValue("IS_ASSEMBLY"));//是否总成
//            String buyState = CommonUtils.checkNull(req.getParamValue("BUY_STATE"));//采购状态
            String position = CommonUtils.checkNull(req.getParamValue("POSITION"));//车型大类
            String width = CommonUtils.checkNull(req.getParamValue("WIDTH"));//宽度
//            String depth = CommonUtils.checkNull(req.getParamValue("DEPTH"));//深度
            String height = CommonUtils.checkNull(req.getParamValue("HEIGHT"));//高度
            String weight = CommonUtils.checkNull(req.getParamValue("WEIGHT"));//重量
            String volume = CommonUtils.checkNull(req.getParamValue("VOLUME"));//体积
            String remark = CommonUtils.checkNull(req.getParamValue("REMARK"));//备注
//            String partTpcode = CommonUtils.checkNull(req.getParamValue("PART_TPCODE"));//配件类别代码
//            String repartCode = CommonUtils.checkNull(req.getParamValue("REPART_CODE"));//配件类别代码

            String seriesId = CommonUtils.checkNull(req.getParamValue("SERIES_ID"));//车系ID
            String seriesName = CommonUtils.checkNull(req.getParamValue("SERIES_NAME"));//车系名称
            String modelId = CommonUtils.checkNull(req.getParamValue("MODEL_ID"));//车型ID
            String modelName = CommonUtils.checkNull(req.getParamValue("MODEL_NAME"));//车型名称
            String modelCode = CommonUtils.checkNull(req.getParamValue("MODEL_CODE"));//车型名称
//            String minPack1 = CommonUtils.checkNull(req.getParamValue("MIN_PACK1"));//供应中心最小包装量
//            String minPack2 = CommonUtils.checkNull(req.getParamValue("MIN_PACK2"));//服务商最小包装量
//            String isChanghe = CommonUtils.checkNull(req.getParamValue("PART_IS_CHANGHE"));//结算基地 艾春9.11修改
//            String isSc = CommonUtils.checkNull(req.getParamValue("IS_SC"));//结算基地 艾春9.11修改
//            String isGxp = CommonUtils.checkNull(req.getParamValue("IS_GXP"));//结算基地 艾春9.11修改
//            String isSpjj = CommonUtils.checkNull(req.getParamValue("IS_SPJJ"));//结算基地 艾春9.11修改

            String curPage = CommonUtils.checkNull(req.getParamValue("curPage"));//当前页码
            
//            String minPack1 = CommonUtils.checkNull(req.getParamValue("MIN_PACK1"));//供应中心最小包装量
//            String minPack2 = CommonUtils.checkNull(req.getParamValue("MIN_PACK2"));//服务商最小包装两
            // ----- 17-07-05 张磊添加  -- begin
//            String owned = CommonUtils.checkNull(req.getParamValue("OWNED_BASE")); // 所属基地
            String partCategory = CommonUtils.checkNull(req.getParamValue("PART_CATEGORY"));//类型
            String length = CommonUtils.checkNull(req.getParamValue("LENGTH")); // 长
            String vehicleVolume = CommonUtils.checkNull(req.getParamValue("VEHICLE_VOLUME")); // 单车用量
            String partFit = CommonUtils.checkNull(req.getParamValue("PART_FIT")); // 装配
            String minPurchase = CommonUtils.checkNull(req.getParamValue("MIN_PURCHASE")); // 最小采购数量
            String minSale = CommonUtils.checkNull(req.getParamValue("MIN_SALE")); // 最小销售数量
            String maxSaleVolume = CommonUtils.checkNull(req.getParamValue("MAX_SALE_VOLUME")); // 最大销售数量
            String otherPartId = CommonUtils.checkNull(req.getParamValue("OTHER_PART_ID")); // 代用件号
            String isPartDisable = CommonUtils.checkNull(req.getParamValue("IS_PART_DISABLE")); // 是否停用
            String isSaleDisable = CommonUtils.checkNull(req.getParamValue("IS_SALE_DISABLE")); // 是否售完停用
            String isStopLoad = CommonUtils.checkNull(req.getParamValue("IS_STOP_LOAD")); // 是否停止装车
            String partDisableDate = CommonUtils.checkNull(req.getParamValue("PART_DISABLE_DATE")); // 停用日期
            String saleDisableDate = CommonUtils.checkNull(req.getParamValue("PART_DISABLE_DATE")); // 售完停用日期
            String stopLoadDate = CommonUtils.checkNull(req.getParamValue("PART_DISABLE_DATE")); // 停止装车日期
            String isSecurity = CommonUtils.checkNull(req.getParamValue("IS_SECURITY")); // 是否校验防伪
            String isProtocolPack = CommonUtils.checkNull(req.getParamValue("IS_PROTOCOL_PACK")); // 是否协议包装
            String isMagBatch = CommonUtils.checkNull(req.getParamValue("IS_MAG_BATCH")); // 是否批次包装
            String isEntrusrPack = CommonUtils.checkNull(req.getParamValue("IS_ENTRUSR_PACK")); // 是否委托发货
            String cccFlag = CommonUtils.checkNull(req.getParamValue("CCC_FLAG")); // 3C标识
            String packSpecification = CommonUtils.checkNull(req.getParamValue("PACK_SPECIFICATION")); // 包装规格
            String dlrPartId = CommonUtils.checkNull(req.getParamValue("DLR_PART_ID")); // 供应商件号
            // ----- 17-07-05 张磊添加 --end
            
            

            List partCarTypeList = new ArrayList<TtPartBrandCartypePO>();

            //先清空再重新插入
            TtPartBrandCartypePO delPo = new TtPartBrandCartypePO();
            delPo.setPartId(Long.valueOf(partId));
            dao.delete(delPo);

            for (String cartype : modelId.split(",")) {
                TtPartBrandCartypePO partBrandCartypePO = new TtPartBrandCartypePO();
                partBrandCartypePO.setBcId(Long.valueOf(SequenceManager.getSequence("")));
                partBrandCartypePO.setPartId(Long.valueOf(partId));
                partBrandCartypePO.setCarType(cartype);
                partBrandCartypePO.setCreateDate(new Date());
                partCarTypeList.add(partBrandCartypePO);
            }
            dao.insert(partCarTypeList);
            //转换为中文
//            modelId = dao.getCarTypeNames(modelId);
//            if (!"".equals(repartCode) && repartCode != null && 0 == dao.validatePartOldcode(repartCode).size()) {
//                act.setOutData("error", "替代件编码不存在,请重新确认!\n");
//            } else {

                TtPartDefinePO oldPo = new TtPartDefinePO();
                oldPo.setPartId(Long.valueOf(partId));

                TtPartDefinePO newPo = new TtPartDefinePO();
                newPo.setPartId(Long.valueOf(partId));
                if (!"".equals(partCode) && partCode != null) {
                    newPo.setPartCode(partCode);
                    newPo.setPartOldcode(partCode);
                }
                if (!"".equals(partCname) && partCname != null) newPo.setPartCname(partCname);
                if (!"".equals(partEname) && partEname != null) newPo.setPartEname(partEname);
//                if (!"".equals(sortCode) && sortCode != null) newPo.setSortCode(sortCode);
//                if (!"".equals(groupCode) && groupCode != null) newPo.setGroupCode(groupCode);
//                if (!"".equals(partTpcode) && partTpcode != null) newPo.setPartTpcode(partTpcode);
                if (!"".equals(seriesName) && seriesName != null) newPo.setSeriesName(seriesName);
                if (!"".equals(modelName) && modelName != null) newPo.setModelName(modelName);
                if (!"".equals(modelCode) && modelCode != null) newPo.setModelCode(modelCode);
//                if (!"".equals(minPack1) && minPack1 != null) newPo.setMinPack1(new Long(minPack1));
//                if (!"".equals(minPack2) && minPack2 != null) newPo.setMinPack2(new Long(minPack2));
//                if (!"".equals(repartCode) && repartCode != null) newPo.setRepartCode(repartCode);
//                if (!"".equals(isEngine) && isEngine != null) newPo.setIsEngine(Integer.valueOf(isEngine));
//                if (!"".equals(isExport) && isExport != null) newPo.setIsExport(Integer.valueOf(isExport));
//                if (!"".equals(isGift) && isGift != null) newPo.setIsGift(Integer.valueOf(isGift));
                if (!"".equals(isPermanent) && isPermanent != null) newPo.setIsPermanent(Integer.valueOf(isPermanent));
            	if(!"".equals(isReplaced)&&isReplaced!=null)newPo.setIsReplaced(Integer.valueOf(isReplaced));
//                if (!"".equals(produceState) && produceState != null)
//                    newPo.setProduceState(Integer.valueOf(produceState));
                if (!"".equals(produceFac) && produceFac != null) newPo.setProduceFac(Integer.valueOf(produceFac));
//                if (!"".equals(packState) && packState != null) newPo.setPackState(Integer.valueOf(packState));
                if (!"".equals(partMaterial) && partMaterial != null)
                    newPo.setPartMaterial(Integer.valueOf(partMaterial));
//                if (!"".equals(partType) && partType != null) newPo.setPartType(Integer.valueOf(partType));
                if (!"".equals(produceState) && produceState != null) newPo.setProduceState(Integer.valueOf(produceState));
//                if (!"".equals(mobility) && mobility != null) newPo.setMobility(Integer.valueOf(mobility));
                if (!"".equals(uom) && uom != null) newPo.setUnit(uom);
//                if (!"".equals(isAssembly) && isAssembly != null) newPo.setIsAssembly(Integer.valueOf(isAssembly));
//                if (!"".equals(buyState) && buyState != null) newPo.setBuyState(Integer.valueOf(buyState));
                if (!"".equals(position) && position != null) newPo.setPosition(Integer.valueOf(position));
                if (!"".equals(width) && width != null) newPo.setWidth(width);
//                if (!"".equals(depth) && depth != null) newPo.setDepth(depth);
                if (!"".equals(height) && height != null) newPo.setHeight(height);
                if (!"".equals(weight) && weight != null) newPo.setWeight(weight);
                if (!"".equals(volume) && volume != null) newPo.setVolume(volume);
                if (!"".equals(seriesName) && seriesName != null) newPo.setSeriesName(seriesName);
//                if (!"".equals(isSc) && isSc != null) newPo.setIsSc(Integer.valueOf(isSc));
//                if (!"".equals(isGxp) && isGxp != null) newPo.setIsGxp(Integer.valueOf(isGxp));
//                if (!"".equals(isSpjj) && isSpjj != null) newPo.setIsSpjj(Integer.valueOf(isSpjj));
                newPo.setRemark(remark);
//                if (!"".equals(isChanghe) && isChanghe != null)
//                    newPo.setPartIsChanghe(Integer.valueOf(isChanghe));//结算基地 艾春9.11修改
                if (logonUser.getPoseBusType().equals(Constant.POSE_BUS_TYPE_WRD))
                    newPo.setPartIsChanghe(Constant.PART_IS_CHANGHE_02); // 艾春10.9添加
                if (!"".equals(seriesId) && seriesId != null) newPo.setSeriesId(seriesId);
                if (!"".equals(modelId) && modelId != null) newPo.setModelId(modelId);
//                if (!"".equals(owned) && owned != null) newPo.setOwnedBase(Integer.valueOf(owned));;
                if (!"".equals(partCategory) && partCategory != null) newPo.setPartCategory(Integer.valueOf(partCategory));;
                if (!"".equals(length) && length != null) newPo.setLength(Integer.valueOf(length));
                if (!"".equals(vehicleVolume) && vehicleVolume != null) newPo.setVehicleVolume(Integer.valueOf(vehicleVolume));
                if (!"".equals(isPartDisable) && isPartDisable != null)  newPo.setIsPartDisable(Integer.valueOf(isPartDisable));
                if (!"".equals(isSaleDisable) && isSaleDisable != null) newPo.setIsSaleDisable(Integer.valueOf(isSaleDisable));
                if (!"".equals(isStopLoad) && isStopLoad != null) newPo.setIsStopLoad(Integer.valueOf(isStopLoad));
                
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                if (!"".equals(partDisableDate) && partDisableDate != null) newPo.setPartDisableDate(sdf.parse(partDisableDate));
                if (!"".equals(saleDisableDate) && saleDisableDate != null) newPo.setSaleDisableDate(sdf.parse(saleDisableDate));
                if (!"".equals(stopLoadDate) && stopLoadDate != null) newPo.setStopLoadDate(sdf.parse(stopLoadDate));
                
                if (!"".equals(partFit) && partFit != null) newPo.setPartFit(Integer.valueOf(partFit));
                if (!"".equals(isEntrusrPack) && isEntrusrPack != null) newPo.setIsEntrusrPack(Integer.valueOf(isEntrusrPack));
                if (!"".equals(cccFlag) && cccFlag != null) newPo.setCccFlag(Integer.valueOf(cccFlag));
                if (!"".equals(minSale) && minSale != null) newPo.setMinSale(Integer.valueOf(minSale));
                if (!"".equals(minPurchase) && minPurchase != null) newPo.setMinPurchase(Integer.valueOf(minPurchase));
                if (!"".equals(maxSaleVolume) && maxSaleVolume != null) newPo.setMaxSaleVolume(Long.valueOf(maxSaleVolume));
                if (!"".equals(packSpecification) && packSpecification != null) newPo.setPackSpecification(packSpecification);
                if (!"".equals(otherPartId) && otherPartId != null) newPo.setOtherPartId(otherPartId);
                if (!"".equals(isSecurity) && isSecurity != null) newPo.setIsSecurity(Integer.valueOf(isSecurity));
                if (!"".equals(isProtocolPack) && isProtocolPack != null) newPo.setIsProtocolPack(Integer.valueOf(isProtocolPack));
                if (!"".equals(dlrPartId) && dlrPartId != null) newPo.setDlrPartId(dlrPartId);
                if (!"".equals(isMagBatch) && isMagBatch != null){
                    newPo.setIsMagBatch(Integer.valueOf(isMagBatch));
                }else{
                    newPo.setIsMagBatch(Constant.IF_TYPE_NO);
                }
                
                
                //PartBaseQueryDao dao = PartBaseQueryDao.getInstance();
                newPo.setUpdateBy(logonUser.getUserId());
                newPo.setUpdateDate(new Date());
                dao.update(oldPo, newPo);

                if ("".equals(curPage)) {
                    curPage = "1";
                }
                act.setOutData("succeed", "succeed");
                act.setOutData("curPage", curPage);
//            }
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "配件主信息维护修改");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :id 配件ID
     * @return :
     * @throws : 
     * @Title : 插入配件主信息维护修改记录
     * @Description: 
     */
    private void saveHistory(String id) {
        TtPartDefinePO vo = new TtPartDefinePO();
        vo.setPartId(Long.valueOf(id));
        List list = dao.select(vo);
        if (null == list || list.size() <= 0 || list.get(0) == null) {
            return;
        }
        vo = (TtPartDefinePO) list.get(0);
        ActionContext act = ActionContext.getContext();
        RequestWrapper req = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        Long historyId = Long.parseLong(SequenceManager.getSequence(""));//生成配件ID
        TtPartDefineHistoryPO po = new TtPartDefineHistoryPO();
        po.setHistory(historyId);
        po.setPartId(vo.getPartId() + "");
        po.setPartCode(vo.getPartCode());
        po.setPartCode(vo.getPartCode());
        po.setPartOldcode(vo.getPartOldcode());
        po.setPartCname(vo.getPartCname());
        po.setPartEname(vo.getPartEname());
        po.setParentId(vo.getParentId());
        po.setParentCode(vo.getParentCode());
        po.setIsPermanent(vo.getIsPermanent());
        po.setMobility(vo.getMobility());
        po.setPartType(vo.getPartType());
        po.setPartMaterial(vo.getPartMaterial());
        po.setSortCode(vo.getSortCode());
        po.setGroupCode(vo.getGroupCode());
        po.setPlanerId(vo.getPlanerId());
        po.setBuyerId(vo.getBuyerId());
        po.setIsReplaced(vo.getIsReplaced());
        po.setRepartId(vo.getRepartId());
        po.setRepartCode(vo.getRepartCode());
        po.setEnUnit(vo.getEnUnit());
        po.setUnit(vo.getUnit());
        po.setIsExport(vo.getIsExport());
        po.setIsEngine(vo.getIsEngine());
        po.setBuyState(vo.getBuyState());
        po.setMinPackage1(vo.getMinPack1());
        po.setMinPackage2(vo.getMinPack2());
        po.setIsAssembly(vo.getIsAssembly());
        po.setSeriesId(vo.getSeriesId());
        po.setSeriesName(vo.getSeriesName());
        po.setModelId(vo.getModelId());
        po.setModelName(vo.getModelName());
        po.setPosition(vo.getPosition());
        po.setWidth(vo.getWidth());
        po.setDepth(vo.getDepth());
        po.setHeight(vo.getHeight());
        po.setWeight(vo.getWeight());
        po.setVolume(vo.getVolume());
        po.setIsDirect(vo.getIsDirect());
        po.setIsPlan(vo.getIsPlan());
        po.setIsLack(vo.getIsLack());
        po.setIsGift(vo.getIsGift());
        po.setGuaranteesMile(vo.getGuaranteesMile());
        po.setGuaranteesTime(vo.getGuaranteesTime());
        po.setIsMaintain(vo.getIsMaintain());
        po.setIsJscontrol(vo.getIsJscontrol());
        po.setNote(vo.getNote());
        po.setRemark(vo.getRemark());
        po.setRepartDate(vo.getRepartDate());
        po.setRepartBy(vo.getRepartBy());
        po.setRepartMby(vo.getRepartMby());
        po.setRepartMdate(vo.getRepartMdate());
        po.setPartOrigin(vo.getPartOrigin());
        po.setCreateDate(vo.getCreateDate());
        po.setCreateBy(vo.getCreateBy());
        po.setUpdateDate(vo.getUpdateDate());
        po.setUpdateBy(vo.getUpdateBy());
        po.setDisableDate(vo.getDisableDate());
        po.setDisableBy(vo.getDisableBy());
        po.setDeleteDate(vo.getDeleteDate());
        po.setDeleteBy(vo.getDeleteBy());
        po.setState(vo.getState());
        po.setStatus(vo.getStatus());
        po.setPartTpcode(vo.getPartTpcode());
        po.setProduceFac(vo.getProduceFac());
        po.setPackState(vo.getPackState());
        po.setOemSales(vo.getOemSales());
        po.setDlrSales(vo.getDlrSales());
        po.setWhmanId(vo.getWhmanId());
        po.setOemPlan(vo.getOemPlan());
        po.setProduceState(vo.getProduceState());
        
        po.setModelCode(vo.getModelCode());
        po.setOwnedBase(vo.getOwnedBase());
        po.setPartCategory(vo.getPartCategory());
        po.setLength(vo.getLength());
        po.setVehicleVolume(vo.getVehicleVolume());
        po.setPartFit(vo.getPartFit());
        po.setMinPurchase(vo.getMinPurchase());
        po.setMinSale(vo.getMinSale());
        po.setMaxSaleVolume(vo.getMaxSaleVolume());
        po.setOtherPartId(vo.getOtherPartId());
        po.setIsPartDisable(vo.getIsPartDisable());
        po.setPartDisableDate(vo.getPartDisableDate());
        po.setIsSaleDisable(vo.getIsSaleDisable());
        po.setSaleDisableDate(vo.getSaleDisableDate());
        po.setIsStopLoad(vo.getIsStopLoad());
        po.setStopLoadDate(vo.getStopLoadDate());
        po.setIsSecurity(vo.getIsSecurity());
        po.setIsProtocolPack(vo.getIsProtocolPack());
        po.setIsMagBatch(vo.getIsMagBatch());
        po.setIsEntrusrPack(vo.getIsEntrusrPack());
        po.setCccFlag(vo.getCccFlag());
        po.setPackSpecification(vo.getPackSpecification());
        po.setDlrPartId(vo.getDlrPartId());
        
        po.setUpdateBy(logonUser.getUserId());
        po.setUpdateDate(new Date());
        dao.insert(po);
    }

    /**
     * @Title : 跳转新增配件主数据页面
     * @param :
     * @return :
     * @throws : LastDate    : 2013-4-3
     * @Description: TODO
     */
    public void partBaseAdd() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String now = sdf.format(date);
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("now", now);
            //获取配件计量单位信息
            List<Map<String, Object>> unitList = PartFixcodeDAO.getInstance().getPartFixcodeListByTypeId(Constant.FIXCODE_TYPE_02);
            // 品牌
            List<Map<String, Object>> brandList = PartFixcodeDAO.getInstance().getPartFixcodeListByTypeId(Constant.FIXCODE_TYPE_09);
            // 车型
            List<Map<String, Object>> carModelList = PartFixcodeDAO.getInstance().getPartFixcodeListByTypeId(Constant.FIXCODE_TYPE_10);
            // 车系
            List<Map<String, Object>> carSeriesList = PartFixcodeDAO.getInstance().getPartFixcodeListByTypeId(Constant.FIXCODE_TYPE_11);
            map.put("brandList", brandList); // 品牌
            map.put("carModelList", carModelList); // 车型
            map.put("carSeriesList", carSeriesList); // 车系
            map.put("unitList", unitList); // 单位
            act.setOutData("ps", map);
            // 艾春 10.08添加 东安数据过滤
            act.setOutData("poseBusType", logonUser.getPoseBusType());
            act.setForword(PartBaseQueryAddUrl);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "配件主信息维护新增");
            logger.error(logonUser, e1);
            act.setException(e1);
            act.setForword(PartBaseQueryUrl);
        }
    }
    
    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-4-3
     * @Title : 新增配件主数据
     * @Description: 
     */
    public void partBaseSave() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper req = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            String curPage = CommonUtils.checkNull(req.getParamValue("curPage"));//当前页码
            //PartBaseQueryDao dao = PartBaseQueryDao.getInstance();
            Long partId = Long.parseLong(SequenceManager.getSequence(""));//生成配件ID
            String partCode = CommonUtils.checkNull(req.getParamValue("PART_CODE"));//配件件号
//            String partOldcode = CommonUtils.checkNull(req.getParamValue("PART_OLDCODE"));//配件编码
            String partCname = CommonUtils.checkNull(req.getParamValue("PART_CNAME"));//配件名称
            String partEname = CommonUtils.checkNull(req.getParamValue("PART_ENAME"));//配件英文名称
//            String sortCode = CommonUtils.checkNull(req.getParamValue("SORT_CODE"));//分类代码
//            String groupCode = CommonUtils.checkNull(req.getParamValue("GROUP_CODE"));//分组代码
//            String isEngine = CommonUtils.checkNull(req.getParamValue("IS_ENGINE"));//发动机标记
//            String isExport = CommonUtils.checkNull(req.getParamValue("IS_EXPORT"));//是否出口
//            String isGift = CommonUtils.checkNull(req.getParamValue("IS_GIFT"));//是否为赠品
            String isPermanent = CommonUtils.checkNull(req.getParamValue("IS_PERMANENT"));//是否常备
            String isReplaced = CommonUtils.checkNull(req.getParamValue("IS_REPLACED"));//是否可替代
//            String produceState = CommonUtils.checkNull(req.getParamValue("PRODUCE_STATE"));//采购状态
//            String produceFac = CommonUtils.checkNull(req.getParamValue("PRODUCE_FAC"));//配件品牌
//            String packState = CommonUtils.checkNull(req.getParamValue("PACK_STATE"));//包装发运方式
            String partMaterial = CommonUtils.checkNull(req.getParamValue("PART_MATERIAL"));//材料属型
//            String mobility = CommonUtils.checkNull(req.getParamValue("MOBILITY"));//配件流动性
            String uom = CommonUtils.checkNull(req.getParamValue("UOM"));//配件单位
//            String isAssembly = CommonUtils.checkNull(req.getParamValue("IS_ASSEMBLY"));//是否总成
//            String buyState = CommonUtils.checkNull(req.getParamValue("BUY_STATE"));//采购状态
            String position = CommonUtils.checkNull(req.getParamValue("POSITION"));//车型大类
            String partCategory = CommonUtils.checkNull(req.getParamValue("PART_CATEGORY"));//类型
            String width = CommonUtils.checkNull(req.getParamValue("WIDTH"));//宽度
            String height = CommonUtils.checkNull(req.getParamValue("HEIGHT"));//高度
            String weight = CommonUtils.checkNull(req.getParamValue("WEIGHT"));//重量
            String volume = CommonUtils.checkNull(req.getParamValue("VOLUME"));//体积
            String remark = CommonUtils.checkNull(req.getParamValue("REMARK"));//备注
//            String minPack1 = CommonUtils.checkNull(req.getParamValue("MIN_PACK1"));//供应中心最小包装量
//            String minPack2 = CommonUtils.checkNull(req.getParamValue("MIN_PACK2"));//服务商最小包装两
            String partTpcode = CommonUtils.checkNull(req.getParamValue("PART_TPCODE"));//配件类别代码//配件类别代码
//            String partType = CommonUtils.checkNull(req.getParamValue("PART_TYPE"));//配件种类
            String produceState = CommonUtils.checkNull(req.getParamValue("PRODUCE_STATE"));//配件种类
//            String isChanghe = CommonUtils.checkNull(req.getParamValue("PART_IS_CHANGHE"));//结算基地 艾春9.11添加
//            String repartCode = CommonUtils.checkNull(req.getParamValue("REPART_CODE"));//结算基地 艾春9.11添加
//            String is_sc = CommonUtils.checkNull(req.getParamValue("IS_SC"));//是否可随车件 wucl 20140227
//            String is_gxp = CommonUtils.checkNull(req.getParamValue("IS_GXP"));//是否广宣品配件 wucl 20140227
//            String is_spjj = CommonUtils.checkNull(req.getParamValue("IS_SPJJ"));//是否广宣品配件 wucl 20140227
            // ----- 17-07-05 张磊添加  -- begin
            String seriesId = CommonUtils.checkNull(req.getParamValue("SERIES_ID"));//车系ID
            String seriesName = CommonUtils.checkNull(req.getParamValue("SERIES_NAME"));//车系名称
            String modelId = CommonUtils.checkNull(req.getParamValue("MODEL_ID"));//车型ID
            String modelName = CommonUtils.checkNull(req.getParamValue("MODEL_NAME"));//车型名称
            String modelCode = CommonUtils.checkNull(req.getParamValue("MODEL_CODE"));//车型名称
//            String ownedBase = CommonUtils.checkNull(req.getParamValue("OWNED_BASE")); // 所属基地
            String produceFac = CommonUtils.checkNull(req.getParamValue("PRODUCE_FAC")); // 采购方式
            String length = CommonUtils.checkNull(req.getParamValue("LENGTH")); // 长
            String vehicleVolume = CommonUtils.checkNull(req.getParamValue("VEHICLE_VOLUME")); // 单车用量
            String partFit = CommonUtils.checkNull(req.getParamValue("PART_FIT")); // 装配
            String minPurchase = CommonUtils.checkNull(req.getParamValue("MIN_PURCHASE")); // 最小采购数量
            String minSale = CommonUtils.checkNull(req.getParamValue("MIN_SALE")); // 最小销售数量
            String maxSaleVolume = CommonUtils.checkNull(req.getParamValue("MAX_SALE_VOLUME")); // 最大销售数量
            String otherPartId = CommonUtils.checkNull(req.getParamValue("OTHER_PART_ID")); // 代用件号
            String isPartDisable = CommonUtils.checkNull(req.getParamValue("IS_PART_DISABLE")); // 是否停用
            String isSaleDisable = CommonUtils.checkNull(req.getParamValue("IS_SALE_DISABLE")); // 是否售完停用
            String isStopLoad = CommonUtils.checkNull(req.getParamValue("IS_STOP_LOAD")); // 是否停止装车
            String partDisableDate = CommonUtils.checkNull(req.getParamValue("PART_DISABLE_DATE")); // 停用日期
            String saleDisableDate = CommonUtils.checkNull(req.getParamValue("PART_DISABLE_DATE")); // 售完停用日期
            String stopLoadDate = CommonUtils.checkNull(req.getParamValue("PART_DISABLE_DATE")); // 停止装车日期
            String isSecurity = CommonUtils.checkNull(req.getParamValue("IS_SECURITY")); // 是否校验防伪
            String isProtocolPack = CommonUtils.checkNull(req.getParamValue("IS_PROTOCOL_PACK")); // 是否协议包装
            String isMagBatch = CommonUtils.checkNull(req.getParamValue("IS_MAG_BATCH")); // 是否批次包装
            String isEntrusrPack = CommonUtils.checkNull(req.getParamValue("IS_ENTRUSR_PACK")); // 是否委托发货
            String cccFlag = CommonUtils.checkNull(req.getParamValue("CCC_FLAG")); // 3C标识
            String packSpecification = CommonUtils.checkNull(req.getParamValue("PACK_SPECIFICATION")); // 包装规格
            String dlrPartId = CommonUtils.checkNull(req.getParamValue("DLR_PART_ID")); // 供应商件号
            // ----- 17-07-05 张磊添加 --end
            
            long userId = logonUser.getUserId();//User ID
            Date date = new Date();//Date

            TtPartDefinePO newPo = new TtPartDefinePO();
            newPo.setPartId(partId);
            if (!"".equals(partEname) && partEname != null) newPo.setPartEname(partEname);
//            if (!"".equals(sortCode) && partEname != null) newPo.setSortCode(sortCode);
//            if (!"".equals(groupCode) && groupCode != null) newPo.setGroupCode(groupCode);
//            if (!"".equals(seriesName) && seriesName != null) newPo.setSeriesName(seriesName);
//            if (!"".equals(modelName) && modelName != null) newPo.setModelName(modelName);
            if (!"".equals(partTpcode) && partTpcode != null) newPo.setPartTpcode(partTpcode);
            if (!"".equals(partCode) && partCode != null) {
                newPo.setPartCode(partCode);
                newPo.setPartOldcode(partCode);
            }
//            if (!"".equals(partOldcode) && partOldcode != null) newPo.setPartOldcode(partOldcode);
            if (!"".equals(partCname) && partCname != null) newPo.setPartCname(partCname);
            if (!"".equals(remark) && remark != null) newPo.setRemark(remark);//备注
//            if (!"".equals(repartCode) && repartCode != null) newPo.setRepartCode(repartCode);
//            if (!"".equals(partType) && partType != null) newPo.setPartType(Integer.valueOf(partType));
            if (!"".equals(produceState) && produceState != null) newPo.setProduceState(Integer.valueOf(produceState));
//            if (!"".equals(isEngine) && isEngine != null) newPo.setIsEngine(Integer.valueOf(isEngine));
//            if (!"".equals(isExport) && isExport != null) newPo.setIsExport(Integer.valueOf(isExport));
//            if (!"".equals(isGift) && isGift != null) newPo.setIsGift(Integer.valueOf(isGift));
            if (!"".equals(isPermanent) && isPermanent != null) newPo.setIsPermanent(Integer.valueOf(isPermanent));
            if (!"".equals(isReplaced) && isReplaced != null) newPo.setIsReplaced(Integer.valueOf(isReplaced));
//            if (!"".equals(produceState) && produceState != null) newPo.setProduceState(Integer.valueOf(produceState));
//            if (!"".equals(produceFac) && produceFac != null) newPo.setProduceFac(Integer.valueOf(produceFac));
//            if (!"".equals(packState) && packState != null) newPo.setPackState(Integer.valueOf(packState));
            if (!"".equals(partMaterial) && partMaterial != null) newPo.setPartMaterial(Integer.valueOf(partMaterial));
//            if (!"".equals(mobility) && mobility != null) newPo.setMobility(Integer.valueOf(mobility));
            if (!"".equals(uom) && uom != null) newPo.setUnit(uom);
//            if (!"".equals(isAssembly) && isAssembly != null) newPo.setIsAssembly(Integer.valueOf(isAssembly));
//            if (!"".equals(buyState) && buyState != null) newPo.setBuyState(Integer.valueOf(buyState));
            if (!"".equals(position) && position != null) newPo.setPosition(Integer.valueOf(position));
            if (!"".equals(partCategory) && partCategory != null) newPo.setPartCategory(Integer.valueOf(partCategory));
            if (!"".equals(width) && width != null) newPo.setWidth(width);
//            if (!"".equals(depth) && depth != null) newPo.setDepth(depth);
            if (!"".equals(height) && height != null) newPo.setHeight(height);
            if (!"".equals(weight) && weight != null) newPo.setWeight(weight);
            if (!"".equals(volume) && volume != null) newPo.setVolume(volume);
//            if (!"".equals(minPack1) && minPack1 != null) newPo.setMinPack1(Long.valueOf(minPack1));
//            if (!"".equals(minPack2) && minPack2 != null) newPo.setMinPack2(Long.valueOf(minPack2));
//            if (!"".equals(is_sc) && is_sc != null) newPo.setIsSc(Integer.valueOf(is_sc));
//            if (!"".equals(is_gxp) && is_gxp != null) newPo.setIsGxp(Integer.valueOf(is_gxp));
//            if (!"".equals(is_spjj) && is_spjj != null) newPo.setIsSpjj(Integer.valueOf(is_spjj));
//            if (!"".equals(isChanghe) && isChanghe != null)
//                newPo.setPartIsChanghe(Integer.valueOf(isChanghe));//艾春9.11添加
            if (logonUser.getPoseBusType().equals(Constant.POSE_BUS_TYPE_WRD))
                newPo.setPartIsChanghe(Constant.PART_IS_CHANGHE_02); // 艾春10.9添加

            if (!"".equals(seriesId) && seriesId != null) newPo.setSeriesId(seriesId);
            if (!"".equals(seriesName) && seriesName != null) newPo.setSeriesName(seriesName);
            if (!"".equals(modelId) && modelId != null) newPo.setModelId(modelId);
            if (!"".equals(modelName) && modelName != null) newPo.setModelName(modelName);
            if (!"".equals(modelCode) && modelCode != null) newPo.setModelCode(modelCode);
//            if (!"".equals(ownedBase) && ownedBase != null) newPo.setOwnedBase(Integer.valueOf(ownedBase));
            if (!"".equals(produceFac) && produceFac != null) newPo.setProduceFac(Integer.valueOf(produceFac));
            if (!"".equals(length) && length != null) newPo.setLength(Integer.valueOf(length));
            if (!"".equals(vehicleVolume) && vehicleVolume != null) newPo.setVehicleVolume(Integer.valueOf(vehicleVolume));
            if (!"".equals(isPartDisable) && isPartDisable != null)  newPo.setIsPartDisable(Integer.valueOf(isPartDisable));
            if (!"".equals(isSaleDisable) && isSaleDisable != null) newPo.setIsSaleDisable(Integer.valueOf(isSaleDisable));
            if (!"".equals(isStopLoad) && isStopLoad != null) newPo.setIsStopLoad(Integer.valueOf(isStopLoad));
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            if (!"".equals(partDisableDate) && partDisableDate != null) newPo.setPartDisableDate(sdf.parse(partDisableDate));
            if (!"".equals(saleDisableDate) && saleDisableDate != null) newPo.setSaleDisableDate(sdf.parse(saleDisableDate));
            if (!"".equals(stopLoadDate) && stopLoadDate != null) newPo.setStopLoadDate(sdf.parse(stopLoadDate));
            if (!"".equals(partFit) && partFit != null) newPo.setPartFit(Integer.valueOf(partFit));
            if (!"".equals(isEntrusrPack) && isEntrusrPack != null) newPo.setIsEntrusrPack(Integer.valueOf(isEntrusrPack));
            if (!"".equals(cccFlag) && cccFlag != null) newPo.setCccFlag(Integer.valueOf(cccFlag));
            if (!"".equals(minSale) && minSale != null) newPo.setMinSale(Integer.valueOf(minSale));
            if (!"".equals(minPurchase) && minPurchase != null) newPo.setMinPurchase(Integer.valueOf(minPurchase));
            if (!"".equals(maxSaleVolume) && maxSaleVolume != null) newPo.setMaxSaleVolume(Long.valueOf(maxSaleVolume));
            if (!"".equals(packSpecification) && packSpecification != null) newPo.setPackSpecification(packSpecification);
            if (!"".equals(otherPartId) && otherPartId != null) newPo.setOtherPartId(otherPartId);
            if (!"".equals(isSecurity) && isSecurity != null) newPo.setIsSecurity(Integer.valueOf(isSecurity));
            if (!"".equals(isProtocolPack) && isProtocolPack != null) newPo.setIsProtocolPack(Integer.valueOf(isProtocolPack));
            if (!"".equals(dlrPartId) && dlrPartId != null) newPo.setDlrPartId(dlrPartId);
            if (!"".equals(isMagBatch) && isMagBatch != null){
                newPo.setIsMagBatch(Integer.valueOf(isMagBatch));
            }else{
                newPo.setIsMagBatch(Constant.IF_TYPE_NO);
            }
            
            newPo.setCreateBy(userId);
            newPo.setCreateDate(date);

            newPo.setState(Constant.STATUS_ENABLE);
            dao.insert(newPo); // 插入配件主数据记录
            
            //先清空再重新插入
            TtPartBrandCartypePO delPo = new TtPartBrandCartypePO();
            delPo.setPartId(Long.valueOf(partId));
            dao.delete(delPo);
            
            // 插入车型记录
            List partCarTypeList = new ArrayList<TtPartBrandCartypePO>();
            for (String cartype : modelName.split(",")) {
                TtPartBrandCartypePO partBrandCartypePO = new TtPartBrandCartypePO();
                partBrandCartypePO.setBcId(Long.valueOf(SequenceManager.getSequence("")));
                partBrandCartypePO.setPartId(Long.valueOf(partId));
                partBrandCartypePO.setCarType(cartype);
                partBrandCartypePO.setCreateDate(new Date());
                partCarTypeList.add(partBrandCartypePO);
            }
            dao.insert(partCarTypeList);

            if ("".equals(curPage)) {
                curPage = "1";
            }
            act.setOutData("succeed", "succeed");
            //act.setOutData("curPage", curPage);

        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "配件主信息维护修改");
            logger.error(logonUser, e1);
            act.setException(e1);
            act.setForword(PartBaseQueryAddUrl);
        }
    }
    
    /**
     * @throws : 
     * @Title : 验证件号唯一值
     * @Description: 
     */
    public void validate() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper req = act.getRequest();
//        String partOldcode = CommonUtils.checkNull(req.getParamValue("PART_OLDCODE")); // 配件编码
        String partCode = CommonUtils.checkNull(req.getParamValue("PART_CODE")); // 件号
        String errorInfo = "";
        List<Map<String, Object>> ps = null;
//        ps = dao.validateUniqueVal(partOldcode, "PART_OLDCODE");
//        if (0 != ps.size()) {
//            errorInfo = "配件编码已存在!\n";
//        }
        if(StringUtils.isNotEmpty("partCode") && 0 != dao.validateUniqueVal(partCode, "PART_CODE").size()){
            errorInfo = "件号已存在!\n";
        }
        act.setOutData("error", errorInfo);
    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-4-3
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
            String flag = CommonUtils.checkNull(request.getParamValue("flag"));
            List<List<Object>> list = new LinkedList<List<Object>>();
            List<Object> listHead = new LinkedList<Object>();//导出模板第一列
            if ("1".equals(flag)) {
                listHead.add("件号");
                listHead.add("配件编码");
                listHead.add("配件名称");
                listHead.add("单位");
//                listHead.add("最小包装量(供应中心)");
//                listHead.add("最小包装量(服务商)");
//                listHead.add("发动机标记");
//                listHead.add("是否出口");
//                listHead.add("是否为赠品");
                listHead.add("配件种类");
                listHead.add("采购方式");
//                listHead.add("是否常备");
                listHead.add("是否可替代");
//                listHead.add("所属大类");
//                listHead.add("是否总成");
//                listHead.add("采购状态");
//                listHead.add("车型大类");
                listHead.add("是否停用");
                listHead.add("是否协议包装");
                listHead.add("是否批次管理");
                listHead.add("长");
                listHead.add("宽");
                listHead.add("高");
                listHead.add("净重");
                listHead.add("体积");
                listHead.add("供应商编码");
                list.add(listHead);
                // 导出的文件名
                String fileName = "配件主数据维护模板.xls";
                // 导出的文字编码
                fileName = new String(fileName.getBytes("GB2312"), "ISO8859-1");
                response.setContentType("Application/text/xls");
                response.addHeader("Content-Disposition", "attachment;filename=" + fileName);
            } else {
                listHead.add("配件编码");
                listHead.add("配件件号");
                listHead.add("配件品牌(多品牌请用逗号分隔)");
                listHead.add("配件状态");
                listHead.add("适用车系(多车系请用逗号分隔)");
                listHead.add("适用车型(多车型请用逗号分隔)");
                listHead.add("备注");
                listHead.add("最小包装量");
                listHead.add("单位");
                list.add(listHead);
                // 导出的文件名
                String fileName = "配件主数据批量更新模板.xls";
                // 导出的文字编码
                fileName = new String(fileName.getBytes("GB2312"), "ISO8859-1");
                response.setContentType("Application/text/xls");
                response.addHeader("Content-Disposition", "attachment;filename=" + fileName);
            }

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
     * @param :
     * @return :
     * @throws : LastDate    : 2013-4-3
     * @Title : 导出EXECEL模板
     * @Description: TODO
     */
    public void exportPartSalesTemplate() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        OutputStream os = null;
        try {
            ResponseWrapper response = act.getResponse();

            List<List<Object>> list = new LinkedList<List<Object>>();

            //标题
            List<Object> listHead = new LinkedList<Object>();//导出模板第一列
            listHead.add("配件编码(必填)");
            //listHead.add("是否计划(1:是；0：否)");
            //listHead.add("是否紧缺件(1:是；0：否) ");
            listHead.add("是否打码(1:是；0：否)");
            listHead.add("是否特殊(1:是；0：否) ");
            listHead.add("是否精品(1:是；0：否) ");
            listHead.add("是否广宣(1:是；0：否) ");

            list.add(listHead);
            // 导出的文件名
            String fileName = "销售主数据批量更新模板.xls";
            // 导出的文字编码
            fileName = new String(fileName.getBytes("GB2312"), "ISO8859-1");
            response.setContentType("Application/text/xls");
            response.addHeader("Content-Disposition", "attachment;filename=" + fileName);

            os = response.getOutputStream();
            CsvWriterUtil.createXlsFile(list, os);
            os.flush();
        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "导出销售主数据批量修改模板错误");
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
     * @throws : LastDate    : 2013-4-3
     * @Title :Excel导入
     * @Description: TODO
     */
    public void uploadPartBaseExcel() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        long userId = logonUser.getUserId();//User ID
        Date date = new Date();//Date
        StringBuffer errorInfo = new StringBuffer("");
        //PartBaseQueryDao dao = PartBaseQueryDao.getInstance();
        RequestWrapper request = act.getRequest();
        try {
            long maxSize = 1024 * 1024 * 5;
            int errNum = 0;
            String flag = CommonUtils.checkNull(request.getParamValue("flag"));//1为新增，其它修改
            if ("1".equals(flag)) {
                errNum = insertIntoTmp(request, "uploadFile", 16, 3, maxSize);
            } else {
                errNum = insertIntoTmp(request, "uploadFile2", 9, 3, maxSize);
            }


            if (errNum != 0) {
                switch (errNum) {
                    case 1:
                        errorInfo.append("文件列数过多!");
                        break;
                    case 2:
                        errorInfo.append("空行不能大于三行!");
                        break;
                    case 3:
                        errorInfo.append("文件不能为空!");
                        break;
                    case 4:
                        errorInfo.append("文件不能为空!");
                        break;
                    case 5:
                        errorInfo.append("文件不能大于!");
                        break;
                    default:
                        break;
                }
            }

            if (!"".equals(errorInfo.toString())) {
                act.setOutData("error", errorInfo.toString());
                BizException e1 = new BizException(act, ErrorCodeConstant.SPECIAL_MEG, errorInfo.toString());
                throw e1;
            } else {
                List<Map> list = getMapList();
                List<String> vdIdList = new ArrayList();
                List voList = new ArrayList();
                List partCarTypeList = new ArrayList<TtPartBrandCartypePO>();
                List partCarTypeErrList = new ArrayList();

                loadVoList(voList, list, errorInfo, vdIdList, flag, partCarTypeList, partCarTypeErrList);
                if (errorInfo.length() > 0) {
                    BizException e1 = new BizException(act, ErrorCodeConstant.SPECIAL_MEG, errorInfo);
                    throw e1;
                }
                if (partCarTypeErrList.size() > 0) {
                    act.setOutData("errList", partCarTypeErrList);
                    act.setOutData("flag", 2);
                    act.setForword(uploadErrUrl);
                    return;
                }
                if ("1".equals(flag)) {
                    for (int i = 0; i < voList.size(); i++) {
                        TtPartDefinePO newPo = (TtPartDefinePO) voList.get(i);
                        Long partId = Long.parseLong(SequenceManager.getSequence(""));
                        newPo.setPartId(partId);
                        newPo.setCreateBy(userId);
                        newPo.setCreateDate(date);
                        dao.insert(newPo);
                        //新增配件供应商 采购关系
                        if (null != vdIdList.get(i) && !"".equals(vdIdList.get(i))) {
                            TtPartBuyPricePO insBPPo = new TtPartBuyPricePO();
                            insBPPo.setPriceId(Long.parseLong(SequenceManager.getSequence("")));
                            insBPPo.setBuyPrice(0d);
                            insBPPo.setClaimPrice(0d);
                            insBPPo.setMinPackage(1l);
                            insBPPo.setPartId(partId);
                            insBPPo.setVenderId(Long.parseLong(vdIdList.get(i)));
                            insBPPo.setCreateBy(userId);
                            insBPPo.setCreateDate(date);
                            dao.insert(insBPPo);
                            TtPartVenderPO venderPO = new TtPartVenderPO();
                            venderPO.setSvId(Long.parseLong(SequenceManager.getSequence("")));
                            venderPO.setPartId(partId);
                            venderPO.setVenderId(Long.parseLong(vdIdList.get(i)));
                            venderPO.setCreateUser(userId);
                            dao.insert(venderPO);
                            
                        }
                    }
                } else {
                    for (int i = 0; i < voList.size(); i++) {
                        TtPartDefinePO srcPo = new TtPartDefinePO();
                        TtPartDefinePO updatePo = new TtPartDefinePO();

                        TtPartDefinePO newPo = (TtPartDefinePO) voList.get(i);
                        srcPo.setPartOldcode(newPo.getPartOldcode());
                        if (null != newPo.getProduceFac() && !"".equals(newPo.getProduceFac())) {
                            updatePo.setProduceFac(newPo.getProduceFac());
                        }
                        if (null != newPo.getPartCode() && !"".equals(newPo.getPartCode())) {
                            updatePo.setPartCode(newPo.getPartCode());
                        }
                        if (null != newPo.getState() && !"".equals(newPo.getState())) {
                            updatePo.setState(newPo.getState());
                            if (newPo.getState().equals(Constant.STATUS_ENABLE)) {
                                updatePo.setStatus(1);
                            } else {
                                updatePo.setStatus(0);
                            }
                        }
                        if (null != newPo.getSeriesName() && !"".equals(newPo.getSeriesName())) {
                            updatePo.setSeriesName(newPo.getSeriesName());
                        }
                        if (null != newPo.getModelName() && !"".equals(newPo.getModelName())) {
                            updatePo.setModelName(newPo.getModelName());
                        }
                        if (null != newPo.getRemark() && !"".equals(newPo.getRemark())) {
                            updatePo.setRemark(newPo.getRemark());
                        } else {
                            updatePo.setRemark(".");
                        }

						if (null != newPo.getMinPack1()) {
						    updatePo.setMinPack1(newPo.getMinPack1());
						    updatePo.setMinPack2(newPo.getMinPack1());
						}
                        if (null != newPo.getUnit() && !"".equals(newPo.getUnit())) {
                            updatePo.setUnit(newPo.getUnit());
                        }
                        dao.update(srcPo, updatePo);
                        saveHistory(((TtPartDefinePO) dao.select(srcPo).get(0)).getPartId() + "");
                    }
                    for (Object cartypePO : partCarTypeList) {
                        TtPartBrandCartypePO delPo = new TtPartBrandCartypePO();
                        delPo.setPartId(((TtPartBrandCartypePO) cartypePO).getPartId());
                        dao.delete(delPo);
                    }
                    dao.insert(partCarTypeList);

                    dao.updatePartCarType();
                }
                act.setOutData("poseBusType", logonUser.getPoseBusType());
                act.setOutData("flag", "导入成功");
                act.setForword(PartBaseQueryUrl);
            }

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
            act.setOutData("flag", "导入失败，"+errorInfo.toString());
            act.setForword(PartBaseQueryUrl);
        }

    }

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013年12月4日
     * @Title :
     * @Description: 导入
     */
    public void partSalesBaseUpload() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        try {

            List<Map<String, String>> errorInfo = null;
            String err = "";

            errorInfo = new ArrayList<Map<String, String>>();
            long maxSize = 1024 * 1024 * 3;
            int errNum = insertIntoTmp(request, "uploadFile", 5, 5, maxSize);

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
                loadVoList(voList, list, errorInfo);
                if (errorInfo.size() > 0) {
                    act.setOutData("errorInfo", errorInfo);
                    act.setForword(INPUT_ERROR_URL);
                } else {
                    //保存
                    savePartSalesBase(voList);
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
     * @param :
     * @return :
     * @throws : LastDate    : 2013-4-7
     * @Title : 保存销售主数据
     */
    public void savePartSalesBase(List<Map<String, String>> relList) {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            if (null != relList && relList.size() > 0) {
                Long userId = logonUser.getUserId();
                Date date = new Date();
                TtPartDefinePO selPo = null;
                TtPartDefinePO updatePo = null;
                int listSize = relList.size();
                for (int i = 0; i < listSize; i++) {
                    String partId = relList.get(i).get("partId"); //配件Id
                    //String isPlan = relList.get(i).get("isPlan");//是否计划
                    //String isLack = relList.get(i).get("isLack");//是否紧缺
                    //String oemPlan = relList.get(i).get("oemPlan");//车厂是否计划
                    String ofFlag = relList.get(i).get("ofFlag");//是否打码
                    String isSpecial = relList.get(i).get("isSpecial");//是否特殊
                    String isDirect = relList.get(i).get("isDirect");
                    String isGx = relList.get(i).get("isGx");

                    selPo = new TtPartDefinePO();
                    updatePo = new TtPartDefinePO();

                    selPo.setPartId(Long.parseLong(partId));

                  /*  if (!"".equals(isPlan)) {
                        updatePo.setIsPlan(Integer.parseInt(isPlan));
                    }
                    if (!"".equals(isLack)) {
                        updatePo.setIsLack(Integer.parseInt(isLack));
                    }*/
                    if (!"".equals(ofFlag)) {
                        updatePo.setOfFlag(Integer.parseInt(ofFlag));
                    }
                    if (!"".equals(isSpecial)) {
                        updatePo.setIsSpecial(Integer.parseInt(isSpecial));
                    }
                    /*if (!"".equals(oemPlan)) {
                        updatePo.setOemPlan(Integer.parseInt(oemPlan));
                    }*/
                    if (!"".equals(isDirect)) {
                        updatePo.setIsDirect(Integer.parseInt(isDirect));
                    }
                    if (!"".equals(isGx)) {
                        updatePo.setIsGxp(Integer.parseInt(isGx));
                    }

                    updatePo.setUpdateBy(userId);
                    updatePo.setUpdateDate(date);

                    dao.update(selPo, updatePo);


                }
            }

            partSalesBaseQueryInit();

        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.UPDATE_FAILURE_CODE, "批量更新销售主数据!");
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
    private void loadVoList(List<Map<String, String>> voList, List<Map> list, List<Map<String, String>> errorInfo) {
        partPlannerQueryDao dao1 = partPlannerQueryDao.getInstance();
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
                String partIdTmp = "";
                Map<String, String> tempmap = new HashMap<String, String>();
                if ("".equals(cells[0].getContents().trim())) {
                    Map<String, String> errormap = new HashMap<String, String>();
                    errormap.put("1", "第" + (i + 1) + "页,第" + key + "行");
                    errormap.put("2", "配件编码");
                    errormap.put("3", "为空!");
                    errorInfo.add(errormap);
                } else {
                    List<Map<String, Object>> partCheck = dao1.checkOldCode(cells[0].getContents().trim().toUpperCase());
                    if (partCheck.size() == 1) {
                        partIdTmp = partCheck.get(0).get("PART_ID").toString();
                        tempmap.put("partId", partCheck.get(0).get("PART_ID").toString());
                        tempmap.put("partoldCode", partCheck.get(0).get("PART_OLDCODE").toString());
                    } else {
                        Map<String, String> errormap = new HashMap<String, String>();
                        errormap.put("1", "第" + (i + 1) + "页,第" + key + "行");
                        errormap.put("2", "配件编码【" + cells[0].getContents().trim() + "】");
                        errormap.put("3", "不存在!");
                        errorInfo.add(errormap);
                    }
                }

                String yesStr = "1";

               /* tempmap.put("oemPlan", "");


                String isPlan = "";
                if (cells.length < 2 || CommonUtils.isEmpty(cells[1].getContents())) {
                    tempmap.put("isPlan", "");
                } else {
                    String isPlanStr = cells[1].getContents().trim();
                    isPlan = isPlanStr;
                    if (yesStr.equals(isPlanStr)) {
                        tempmap.put("isPlan", Constant.IF_TYPE_YES + "");
                    } else {
                        tempmap.put("isPlan", Constant.IF_TYPE_NO + "");
                    }

                }


                if (cells.length < 3 || CommonUtils.isEmpty(cells[2].getContents())) {
                    tempmap.put("isLack", "");
                } else {
                    String isLack = cells[2].getContents().trim();
                    if (yesStr.equals(isLack)) {
                        tempmap.put("isLack", Constant.IF_TYPE_YES + "");
                    } else {
                        tempmap.put("isLack", Constant.IF_TYPE_NO + "");
                    }
                }*/

                if (cells.length < 2 || CommonUtils.isEmpty(cells[1].getContents())) {
                    tempmap.put("ofFlag", "");
                } else {
                    String ofFlag = cells[1].getContents().trim();
                    if (yesStr.equals(ofFlag)) {
                        tempmap.put("ofFlag", Constant.IF_TYPE_YES + "");
                    } else {
                        tempmap.put("ofFlag", Constant.IF_TYPE_NO + "");
                    }
                }

                if (cells.length < 3 || CommonUtils.isEmpty(cells[2].getContents())) {
                    tempmap.put("isSpecial", "");
                } else {
                    String isSpecial = cells[2].getContents().trim();
                    if (yesStr.equals(isSpecial)) {
                        tempmap.put("isSpecial", Constant.IF_TYPE_YES + "");
                    } else {
                        tempmap.put("isSpecial", Constant.IF_TYPE_NO + "");
                    }
                }
                if (cells.length < 4 || CommonUtils.isEmpty(cells[3].getContents())) {
                    tempmap.put("isDirect", "");
                } else {
                    String isDirect = cells[3].getContents().trim();
                    if (yesStr.equals(isDirect)) {
                        tempmap.put("isDirect", Constant.IF_TYPE_YES + "");
                    } else {
                        tempmap.put("isDirect", Constant.IF_TYPE_NO + "");
                    }
                }

                if (cells.length < 5 || CommonUtils.isEmpty(cells[4].getContents())) {
                    tempmap.put("isGx", "");
                } else {
                    String isGx = cells[4].getContents().trim();
                    if (yesStr.equals(isGx)) {
                        tempmap.put("isGx", Constant.IF_TYPE_YES + "");
                    } else {
                        tempmap.put("isGx", Constant.IF_TYPE_NO + "");
                    }
                }

                voList.add(tempmap);
            }
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
    private void loadVoList(List voList, List<Map> list, StringBuffer errorInfo, List<String> vdIdList, String flag, List<TtPartBrandCartypePO> carTypeList, List carTypeErrList) {
        if (null == list) {
            list = new ArrayList();
        }
        if (null == carTypeList) {
            carTypeList = new ArrayList();
        }
        if (null == carTypeErrList) {
            carTypeErrList = new ArrayList();
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
                parseCells(voList, key, cells, errorInfo, vdIdList, flag, carTypeList, carTypeErrList);
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
    private void parseCells(List<TtPartDefinePO> list, String rowNum, Cell[] cells, StringBuffer errorInfo, List<String> vdIdList, String flag, List<TtPartBrandCartypePO> carTypeList, List<HashMap<String, Object>> errList) {
        TtPartDefinePO po = new TtPartDefinePO();
        po.setCreateBy(-1l);
        po.setCreateDate(new Date());
        int cellNum = 0;
        if ("1".equals(flag)) {
            Map<String, Object> codeMap = new HashMap<String, Object>();
            codeMap.put("是", Constant.PART_BASE_FLAG_YES);
            codeMap.put("否", Constant.PART_BASE_FLAG_NO);
//            loadCode(codeMap, Constant.PART_BASE_PART_TYPES);
            loadCode(codeMap, Constant.PART_BASE_MATERIAL);
            loadCode(codeMap, Constant.PART_BASE_BUY_STATE);
            loadCode(codeMap, Constant.PART_BASE_POSITION);
            loadCode(codeMap, Constant.PURCHASE_WAY);
            loadCode(codeMap, Constant.PART_PRODUCE_STATE);
            
            // 件号
            if ("" == subCell(cells[cellNum].getContents().trim())) {
                errorInfo.append("第" + (Integer.parseInt(rowNum) - 1) + "行件号不能为空!");
                return;
            }
            po.setPartCode(subCell(cells[cellNum].getContents().trim()));
            List<Map<String, Object>> list2 = null;
            list2 = dao.validateUniqueVal(subCell(cells[cellNum].getContents().trim()), "PART_CODE");
            //logger.info("list2.size()============"+list2.size());
            if (list2.size() != 0) {
                errorInfo.append("第" + (Integer.parseInt(rowNum) - 1) + "行" + subCell(cells[2].getContents().trim()) + "配件编码已经存在!");
                return;
            }
            
            // 配件编码
            cellNum++;
            if ("" == subCell(cells[cellNum].getContents().trim())) {
                errorInfo.append("第" + (Integer.parseInt(rowNum) - 1) + "行配件编码不能为空!");
                return;
            }
            list2 = dao.validatePartOldcode(subCell(cells[cellNum].getContents().trim()));
            //logger.info("list2.size()============"+list2.size());
            if (list2.size() != 0) {
                errorInfo.append("第" + (Integer.parseInt(rowNum) - 1) + "行" + subCell(cells[2].getContents().trim()) + "配件编码已经存在!");
                return;
            }
            po.setPartOldcode(subCell(cells[cellNum].getContents().trim()));
            if(!po.getPartCode().equals(po.getPartOldcode())){
                errorInfo.append("第" + (Integer.parseInt(rowNum) - 1) + "行" + subCell(cells[2].getContents().trim()) + "件号和配件编码必须一致!");
                return;
            }

            // 配件名称
            cellNum++;
            if ("" == subCell(cells[cellNum].getContents().trim())) {
                errorInfo.append("配件名称不能为空!");
                return;
            }
            po.setPartCname(subCell(cells[cellNum].getContents().trim()));
            
            // 单位
            cellNum++;
            po.setUnit(subCell(cells[cellNum].getContents().trim()));
            
//            // 最小包装量(供应中心)
//            cellNum++;
//            try {
//                po.setMinPack1(Long.valueOf(subCell(cells[cellNum].getContents().trim())));
//            } catch (Exception ex) {
//                errorInfo.append("第" + (Integer.parseInt(rowNum) - 1) + "行" + subCell(cells[4].getContents().trim()) + "的最小包装量(供应中心)数据类型错误!");
//                return;
//            }
//
//            // 最小包装量(服务商)
//            cellNum++;
//            try {
//                po.setMinPack2(Long.valueOf(subCell(cells[cellNum].getContents().trim())));
//            } catch (Exception ex) {
//                errorInfo.append("第" + (Integer.parseInt(rowNum) - 1) + "行" + subCell(cells[5].getContents().trim()) + "的最小包装量(服务商)数据类型错误!");
//                return;
//            }

            // 发动机标记
//            cellNum++;
//            if (cells.length > cellNum && null != cells[cellNum].getContents() && !"".equals(cells[cellNum].getContents().trim()) && !"".equals(CommonUtils.checkNull(codeMap.get(cells[cellNum].getContents().trim())))) {
//                try {
//                    po.setIsEngine(Integer.valueOf(CommonUtils.checkNull(codeMap.get(cells[cellNum].getContents().trim()))));
//                } catch (Exception ex) {
//                    errorInfo.append("第" + (Integer.parseInt(rowNum) - 1) + "行" + subCell(cells[cellNum].getContents().trim()) + "发动机标记错误!");
//                    return;
//                }
//            }

            // 是否出口
//            cellNum++;
//            if (cells.length > cellNum && null != cells[cellNum].getContents() && !"".equals(cells[cellNum].getContents().trim()) && !"".equals(CommonUtils.checkNull(codeMap.get(cells[cellNum].getContents().trim())))) {
//                try {
//                    po.setIsExport(Integer.valueOf(CommonUtils.checkNull(codeMap.get(cells[cellNum].getContents().trim()))));
//                } catch (Exception ex) {
//                    errorInfo.append("第" + (Integer.parseInt(rowNum) - 1) + "行" + subCell(cells[cellNum].getContents().trim()) + "是否出口错误!");
//                    return;
//                }
//            }

            // 是否为赠品
//            cellNum++;
//            if (cells.length > cellNum && null != cells[cellNum].getContents() && !"".equals(cells[cellNum].getContents().trim()) && !"".equals(CommonUtils.checkNull(codeMap.get(cells[cellNum].getContents().trim())))) {
//                try {
//                    po.setIsGift(Integer.valueOf(CommonUtils.checkNull(codeMap.get(cells[cellNum].getContents().trim()))));
//                } catch (Exception ex) {
//                    errorInfo.append("第" + (Integer.parseInt(rowNum) - 1) + "行" + subCell(cells[cellNum].getContents().trim()) + "是否为赠品错误!");
//                    return;
//                }
//            }

            // 配件种类
            cellNum++;
            String produceState = CommonUtils.checkNull(codeMap.get(cells[cellNum].getContents().trim()));
            if (cells.length > cellNum + 1 && null != cells[cellNum].getContents() && !"".equals(cells[cellNum].getContents().trim()) && !"".equals(CommonUtils.checkNull(codeMap.get(cells[cellNum].getContents().trim())))) {
                try {
//                    po.setPartType(Integer.valueOf(CommonUtils.checkNull(codeMap.get(cells[cellNum].getContents().trim()))));
                    po.setProduceState(Integer.valueOf(CommonUtils.checkNull(codeMap.get(cells[cellNum].getContents().trim()))));
                } catch (Exception ex) {
                    errorInfo.append("第" + (Integer.parseInt(rowNum) - 1) + "行" + subCell(cells[cellNum].getContents().trim()) + "配件种类错误!");
                    return;
                }
            }
            
            // 采购方式
            cellNum++;
            if (cells.length > cellNum && null != cells[cellNum].getContents() && !"".equals(cells[cellNum].getContents().trim()) && !"".equals(CommonUtils.checkNull(codeMap.get(cells[cellNum].getContents().trim())))) {
                try {
//                    po.setIsPermanent(Integer.valueOf(CommonUtils.checkNull(codeMap.get(cells[cellNum].getContents().trim()))));
                    po.setProduceFac(Integer.valueOf(CommonUtils.checkNull(codeMap.get(cells[cellNum].getContents().trim()))));
                } catch (Exception ex) {
                    errorInfo.append("第" + (Integer.parseInt(rowNum) - 1) + "行" + subCell(cells[cellNum].getContents().trim()) + "是否常备错误!");
                    return;
                }
            }

            // 是否可替代
            cellNum++;
            if (cells.length > cellNum && null != cells[cellNum].getContents() && !"".equals(cells[cellNum].getContents().trim()) && !"".equals(CommonUtils.checkNull(codeMap.get(cells[cellNum].getContents().trim())))) {
                try {
                    po.setIsReplaced(Integer.valueOf(CommonUtils.checkNull(codeMap.get(cells[cellNum].getContents().trim()))));
                } catch (Exception ex) {
                    errorInfo.append("第" + (Integer.parseInt(rowNum) - 1) + "行" + subCell(cells[cellNum].getContents().trim()) + "是否可替代错误!");
                    return;
                }
            }

            // 所属大类
//            cellNum++;
//            if (cells.length > cellNum && null != cells[cellNum].getContents() && !"".equals(cells[cellNum].getContents().trim()) && !"".equals(CommonUtils.checkNull(codeMap.get(cells[cellNum].getContents().trim())))) {
//                try {
////                    po.setPartMaterial(Integer.valueOf(CommonUtils.checkNull(codeMap.get(cells[cellNum].getContents().trim()))));
////                    po.setPartMaterial(Integer.valueOf(CommonUtils.checkNull(codeMap.get(cells[cellNum].getContents().trim()))));
//                    po.setPosition(Integer.valueOf(CommonUtils.checkNull(codeMap.get(cells[cellNum].getContents().trim()))));
//                } catch (Exception ex) {
//                    errorInfo.append("第" + (Integer.parseInt(rowNum) - 1) + "行" + subCell(cells[cellNum].getContents().trim()) + "材料属性错误!");
//                    return;
//                }
//            }

            // 是否总成
//            cellNum++;
//            if (cells.length > cellNum && null != cells[cellNum].getContents() && !"".equals(cells[cellNum].getContents().trim()) && !"".equals(CommonUtils.checkNull(codeMap.get(cells[cellNum].getContents().trim())))) {
//                try {
//                    po.setIsAssembly(Integer.valueOf(CommonUtils.checkNull(codeMap.get(cells[cellNum].getContents().trim()))));
//                } catch (Exception ex) {
//                    errorInfo.append("第" + (Integer.parseInt(rowNum) - 1) + "行" + subCell(cells[cellNum].getContents().trim()) + "是否总成错误!");
//                    return;
//                }
//            }

            // 采购状态
//            cellNum++;
//            if (cells.length > cellNum && null != cells[cellNum].getContents() && !"".equals(cells[cellNum].getContents().trim()) && !"".equals(CommonUtils.checkNull(codeMap.get(cells[cellNum].getContents().trim())))) {
//                try {
//                    po.setBuyState(Integer.valueOf(CommonUtils.checkNull(codeMap.get(cells[cellNum].getContents().trim()))));
//                } catch (Exception ex) {
//                    errorInfo.append("第" + (Integer.parseInt(rowNum) - 1) + "行" + subCell(cells[cellNum].getContents().trim()) + "采购状态错误!");
//                    return;
//                }
//            }

            // 车型大类
//            cellNum++;
//            if (cells.length > cellNum && null != cells[cellNum].getContents() && !"".equals(cells[cellNum].getContents().trim()) && !"".equals(CommonUtils.checkNull(codeMap.get(cells[cellNum].getContents().trim())))) {
//                try {
//                    po.setPosition(Integer.valueOf(CommonUtils.checkNull(codeMap.get(cells[cellNum].getContents()))));
//                } catch (Exception ex) {
//                    errorInfo.append("第" + (Integer.parseInt(rowNum) - 1) + "行" + subCell(cells[cellNum].getContents().trim()) + "车型大类错误!");
//                    return;
//                }
//            }
            
            // 是否停用
            cellNum++;
            if (cells.length > cellNum && null != cells[cellNum].getContents() && !"".equals(cells[cellNum].getContents().trim()) && !"".equals(CommonUtils.checkNull(codeMap.get(cells[cellNum].getContents().trim())))) {
                try {
                    po.setIsPartDisable(Integer.valueOf(CommonUtils.checkNull(codeMap.get(cells[cellNum].getContents()))));
                } catch (Exception ex) {
                    errorInfo.append("第" + (Integer.parseInt(rowNum) - 1) + "行" + subCell(cells[cellNum].getContents().trim()) + "是否停用错误!");
                    return;
                }
            }
            
            // 是否协议包装
            cellNum++;
            if (cells.length > cellNum && null != cells[cellNum].getContents() && !"".equals(cells[cellNum].getContents().trim()) && !"".equals(CommonUtils.checkNull(codeMap.get(cells[cellNum].getContents().trim())))) {
                try {
                    po.setIsProtocolPack(Integer.valueOf(CommonUtils.checkNull(codeMap.get(cells[cellNum].getContents()))));
                } catch (Exception ex) {
                    errorInfo.append("第" + (Integer.parseInt(rowNum) - 1) + "行" + subCell(cells[cellNum].getContents().trim()) + "是否协议包装错误!");
                    return;
                }
            }
            
            // 是否批次管理
            cellNum++;
            if (cells.length > cellNum && null != cells[cellNum].getContents() && !"".equals(cells[cellNum].getContents().trim()) && !"".equals(CommonUtils.checkNull(codeMap.get(cells[cellNum].getContents().trim())))) {
                try {
                    po.setIsMagBatch(Integer.valueOf(CommonUtils.checkNull(codeMap.get(cells[cellNum].getContents()))));
                    if(Constant.IF_TYPE_YES.equals(po.getIsProtocolPack()) && Constant.IF_TYPE_YES.equals(po.getIsMagBatch())){
                        errorInfo.append("第" + (Integer.parseInt(rowNum) - 1) + "行" + subCell(cells[cellNum].getContents().trim()) + "是否批次管理错误，是协议包装时，不能是批次管理!");
                        return;
                    }
                } catch (Exception ex) {
                    errorInfo.append("第" + (Integer.parseInt(rowNum) - 1) + "行" + subCell(cells[cellNum].getContents().trim()) + "是否批次管理错误!");
                    return;
                }
            }
            
            // 长
            cellNum++;
            if (cells.length > cellNum && StringUtil.notNull(CommonUtils.checkNull(cells[cellNum].getContents()))) {
                try {
                    po.setLength(Integer.parseInt(CommonUtils.checkNull(cells[cellNum].getContents())));
                } catch (Exception ex) {
                    errorInfo.append("第" + (Integer.parseInt(rowNum) - 1) + "行" + subCell(cells[cellNum].getContents().trim()) + "长错误!");
                    return;
                }
            }

            // 宽
            cellNum++;
            if (cells.length > cellNum && null != cells[cellNum].getContents() && !"".equals(cells[cellNum].getContents().trim())) {
                try {
                    po.setWidth(CommonUtils.checkNull(cells[cellNum].getContents()));
                } catch (Exception ex) {
                    errorInfo.append("第" + (Integer.parseInt(rowNum) - 1) + "行" + subCell(cells[cellNum].getContents().trim()) + "宽错误!");
                    return;
                }
            }

            // 深度
//            cellNum++;
//            if (cells.length > cellNum && null != cells[cellNum].getContents() && !"".equals(cells[cellNum].getContents().trim())) {
//                try {
//                    po.setDepth(CommonUtils.checkNull(cells[cellNum].getContents()));
//                } catch (Exception ex) {
//                    errorInfo.append("第" + (Integer.parseInt(rowNum) - 1) + "行" + subCell(cells[cellNum].getContents().trim()) + "深度错误!");
//                    return;
//                }
//            }

            // 高
            cellNum++;
            if (cells.length > cellNum && null != cells[cellNum].getContents() && !"".equals(cells[cellNum].getContents())) {
                try {
                    po.setHeight(CommonUtils.checkNull(cells[cellNum].getContents().trim()));
                } catch (Exception ex) {
                    errorInfo.append("第" + (Integer.parseInt(rowNum) - 1) + "行" + subCell(cells[cellNum].getContents()) + "高错误!");
                    return;
                }
            }

            // 净重
            cellNum++;
            if (cells.length > cellNum && null != cells[cellNum].getContents() && !"".equals(cells[cellNum].getContents().trim())) {
                try {
                    po.setWeight(CommonUtils.checkNull(cells[cellNum].getContents().trim()));
                } catch (Exception ex) {
                    errorInfo.append("第" + (Integer.parseInt(rowNum) - 1) + "行" + subCell(cells[cellNum].getContents().trim()) + "净重错误!");
                    return;
                }
            }

            // 体积
            cellNum++;
            if (cells.length > cellNum && null != cells[cellNum].getContents() && !"".equals(cells[cellNum].getContents().trim())) {
                try {
                    po.setVolume(CommonUtils.checkNull(cells[cellNum].getContents()));
                } catch (Exception ex) {
                    errorInfo.append("第" + (Integer.parseInt(rowNum) - 1) + "行" + subCell(cells[cellNum].getContents().trim()) + "体积错误!");
                    return;
                }
            }
            
            // 供应商编码
            cellNum++;
            if (cells.length > cellNum && null != cells[cellNum].getContents() && !"".equals(cells[cellNum].getContents().trim())) {
                try {
                    String venderCode = CommonUtils.checkNull(cells[cellNum].getContents().trim().toUpperCase());
                    String sqlStr = " AND VD.VENDER_CODE = '" + venderCode + "' ";
                    List<Map<String, Object>> vdList = dao.getVenderList(sqlStr);
                    if (null != vdList && vdList.size() == 1) {
                        vdIdList.add(vdList.get(0).get("VENDER_ID").toString());
                    } else {
                        errorInfo.append("第" + (Integer.parseInt(rowNum) - 1) + "行" + subCell(cells[cellNum].getContents().trim()) + "供应商编码无效或不存在!");
                        return;
                    }
                } catch (Exception ex) {
                    errorInfo.append("第" + (Integer.parseInt(rowNum) - 1) + "行" + subCell(cells[cellNum].getContents().trim()) + "供应商编码无效或不存在!");
                    return;
                }
            } else {
                vdIdList.add("");
            }
            list.add(po);
        } else {
            HashMap<String, Object> errMap = null;
            TtPartBrandCartypePO brandCartypePO = null;
            Integer rnum = Integer.valueOf(rowNum) - 1;
            Long partId = null;

            String partOldCode = subCell(cells[0].getContents().trim());
            String partCode = subCell(cells[1].getContents().trim());
            String brands = subCell(cells[2].getContents().trim());
            String state = subCell(cells[3].getContents().trim());
            String series = subCell(cells[4].getContents().trim());
            String carTypes = subCell(cells[5].getContents().trim());
            String minPkg = subCell(cells[7].getContents().trim());
            String unit = subCell(cells[8].getContents().trim());
            String note = "";
            if (cells.length > 6) {
                note = subCell(cells[6].getContents().trim());
            }

            if ("" == partOldCode) {
                errMap = new HashMap<String, Object>();
                errMap.put("err", "第" + rnum + "行配件编码不能为空！");
                errList.add(errMap);
            }
            List<Map<String, Object>> list2 = dao.validatePartOldcode(partOldCode);
            if (list2.size() == 0) {
                errMap = new HashMap<String, Object>();
                errMap.put("err", "第" + rnum + "行配件编码【" + partOldCode + "】不存在！");
                errList.add(errMap);
            } else {
                po.setPartOldcode(partOldCode);
                partId = Long.valueOf(list2.get(0).get("PART_ID") + "");
            }
            //件号
            if ("" != partCode) {
                po.setPartCode(partCode);
            }
            //配件品牌
            if (!"".equals(brands) && "" != brands) {
                for (String brand : brands.split(",")) {
                    if (!brand.equals("幻速") && !brand.equals("威旺")) {
                        errMap = new HashMap<String, Object>();
                        errMap.put("err", "第" + rnum + "行配件品牌【" + brands + "】应为幻速或威旺！");
                        errList.add(errMap);
                    } /*else {
                        if (brand.equals("幻速")) {
                            po.setProduceFac(Constant.YIELDLY_01);
                        } else {
                            po.setProduceFac(Constant.YIELDLY_02);
                        }
                    }*/
                }
            }
            //配件状态
            if (!"".equals(state) && "" != state) {
                if (!state.equals("有效") && !state.equals("无效")) {
                    errMap = new HashMap<String, Object>();
                    errMap.put("err", "第" + rnum + "行配件【" + partOldCode + "】状态应为有效或无效！");
                    errList.add(errMap);
                } else {
                    if (state.equals("有效")) {
                        po.setState(Constant.STATUS_ENABLE);
                    } else {
                        po.setState(Constant.STATUS_DISABLE);
                    }
                }
            }
            //配件车系
            if ("" != series) {
                po.setSeriesName(series);
            }
            //配件车型
            if (!"".equals(carTypes) && "" != carTypes) {
                if (carTypes.split(",").length == 0) {
                    errMap = new HashMap<String, Object>();
                    errMap.put("err", "第" + rnum + "行配件【" + partOldCode + "】多车型必须通过逗分隔！");
                    errList.add(errMap);
                }
                TtPartFixcodeDefinePO fixcodeDefinePO = new TtPartFixcodeDefinePO();
                fixcodeDefinePO.setFixGouptype(Constant.FIXCODE_TYPE_10);
                for (String carType : carTypes.split(",")) {
                    String carTypeValue = null;
                    fixcodeDefinePO.setFixName(carType);
                    if (dao.select(fixcodeDefinePO).size() != 0) {
                        carTypeValue = ((TtPartFixcodeDefinePO) dao.select(fixcodeDefinePO).get(0)).getFixValue();
                    }
                    if (carTypeValue == null) {
                        errMap = new HashMap<String, Object>();
                        errMap.put("err", "第" + rnum + "行车型【" + carType + "】不存在！");
                        errList.add(errMap);
                    } else {
                        po.setModelName(carTypes);
                        brandCartypePO = new TtPartBrandCartypePO();
                        brandCartypePO.setPartId(partId);
                        brandCartypePO.setCarType(carTypeValue);
                        brandCartypePO.setBcId(Long.valueOf(SequenceManager.getSequence("")));
                        brandCartypePO.setCreateDate(new Date());
                        carTypeList.add(brandCartypePO);
                    }
                }
            }
            //配件备注
            if ("" != note) {
                po.setRemark(note);
            } else {
                po.setRemark("");
            }

            if ("" != minPkg) {
                po.setMinPack1(Long.valueOf(minPkg));
            }

            if ("" != unit) {
                po.setUnit(unit);
            }
            list.add(po);
        }
    }

    private void loadCode(Map<String, Object> codeMap, int code) {
        List<Map<String, Object>> codeList = dao.getTcCode(code);
        //CODE_ID,CODE_DESC
        for (Map<String, Object> map : codeList) {
            codeMap.put(CommonUtils.checkNull(map.get("CODE_DESC")), CommonUtils.checkNull(map.get("CODE_ID")));
        }
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
//            newAmt = orgAmt.substring(0, 30);
            newAmt = orgAmt.trim();
        } else {
            newAmt = orgAmt;
        }
        return newAmt;
    }

    /**
     * <p>
     * Description: 导出配件excel
     * </p>
     */
    public void exportPartBaseExcel() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        ResponseWrapper response = act.getResponse();
        OutputStream os = null;
        try {
            RequestWrapper request = act.getRequest();
            List<Object> headList = new ArrayList<Object>();
            headList.add("件号");
            headList.add("配件编码");
            headList.add("配件名称");
            headList.add("英文名称");
            headList.add("采购方式");
            headList.add("单车用量");
            headList.add("是否停用");
            headList.add("停用日期");
            headList.add("是否售完停用");
            headList.add("售完停用日期");
            headList.add("是否停止装车");
            headList.add("停止装车日期");
            headList.add("配件种类");
            headList.add("装配");
            headList.add("备件类别");
            headList.add("车系");
            headList.add("适用车型");
            headList.add("第一次入库时间");
            headList.add("是否协议包装");
            headList.add("是否委托发货");
            headList.add("最小销售数量");
            headList.add("最大销售数量");
            headList.add("最小采购数量");
            headList.add("长（mm)");
            headList.add("宽（mm)");
            headList.add("高（mm)");
            headList.add("体积(mm3)");
            headList.add("重量(KG)");
            headList.add("包装规格");
            headList.add("3C标识");
            headList.add("是否批次管理");
            headList.add("是否有效");
            headList.add("新增日期");
            List<Map<String, Object>> list = dao.queryPartBase(request, logonUser.getPoseBusType());
            List<List<Object>> expExcelList = new ArrayList<List<Object>>();
            
            expExcelList.add(headList);
            if (list != null && list.size() != 0) {
                for (int i = 0; i < list.size(); i++) {
                    Map<String, Object> map = (Map<String, Object>) list.get(i);
                    if (map != null && map.size() != 0) {
                        List<Object> bodyList = new ArrayList<Object>();
                        bodyList.add(CommonUtils.checkNull(map.get("PART_CODE")));
                        bodyList.add(CommonUtils.checkNull(map.get("PART_OLDCODE")));
                        bodyList.add(CommonUtils.checkNull(map.get("PART_CNAME")));
                        bodyList.add(CommonUtils.checkNull(map.get("PART_ENAME")));
                        bodyList.add(CommonUtils.checkNull(map.get("PRODUCE_FAC")));
                        bodyList.add(CommonUtils.checkNull(map.get("VEHICLE_VOLUME")));
                        bodyList.add(CommonUtils.checkNull(map.get("IS_PART_DISABLE")));
                        bodyList.add(CommonUtils.checkNull(map.get("PART_DISABLE_DATE")));
                        bodyList.add(CommonUtils.checkNull(map.get("IS_SALE_DISABLE")));
                        bodyList.add(CommonUtils.checkNull(map.get("SALE_DISABLE_DATE")));
                        bodyList.add(CommonUtils.checkNull(map.get("IS_STOP_LOAD")));
                        bodyList.add(CommonUtils.checkNull(map.get("STOP_LOAD_DATE")));
                        bodyList.add(CommonUtils.checkNull(map.get("PRODUCE_STATE")));
                        bodyList.add(CommonUtils.checkNull(map.get("PART_FIT")));
                        bodyList.add(CommonUtils.checkNull(map.get("PART_CATEGORY")));
                        bodyList.add(CommonUtils.checkNull(map.get("MODEL_CODE")));
                        bodyList.add(CommonUtils.checkNull(map.get("MODEL_NAME")));
                        bodyList.add(CommonUtils.checkNull(map.get("FIRST_WARNHOUSE_DATE")));
                        bodyList.add(CommonUtils.checkNull(map.get("IS_PROTOCOL_PACK")));
                        bodyList.add(CommonUtils.checkNull(map.get("IS_ENTRUSR_PACK")));
                        bodyList.add(CommonUtils.checkNull(map.get("MIN_SALE")));
                        bodyList.add(CommonUtils.checkNull(map.get("MAX_SALE_VOLUME")));
                        bodyList.add(CommonUtils.checkNull(map.get("MIN_PURCHASE")));
                        bodyList.add(CommonUtils.checkNull(map.get("LENGTH")));
                        bodyList.add(CommonUtils.checkNull(map.get("WIDTH")));
                        bodyList.add(CommonUtils.checkNull(map.get("HEIGHT")));
                        bodyList.add(CommonUtils.checkNull(map.get("VOLUME")));
                        bodyList.add(CommonUtils.checkNull(map.get("WEIGHT")));
                        bodyList.add(CommonUtils.checkNull(map.get("PACK_SPECIFICATION")));
                        bodyList.add(CommonUtils.checkNull(map.get("CCC_FLAG")));
                        bodyList.add(CommonUtils.checkNull(map.get("IS_MAG_BATCH")));
                        bodyList.add(CommonUtils.checkNull(map.get("STATE")));
                        bodyList.add(CommonUtils.checkNull(map.get("CREATE_DATE")));
                        expExcelList.add(bodyList);
                    }
                }
            }
            
            String fileName = "配件主数据信息.xls";
            // 导出的文字编码
            fileName = new String(fileName.getBytes("GB2312"), "ISO8859-1");
            response.setContentType("Application/text/xls");
            response.addHeader("Content-Disposition", "attachment;filename=" + fileName);
            os = response.getOutputStream();
            CsvWriterUtil.createXlsFile(expExcelList, os);
            os.flush();
            
        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    public void expPartSalesBaseExcel() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(
                Constant.LOGON_USER);
        try {

            String[] head = new String[10];
            head[0] = "配件编码";
            head[1] = "配件名称";
            head[2] = "件号";
            //head[3] = "包装发运方式";
            //head[4] = "是否计划";
            //head[5] = "是否紧缺件";
            head[3] = "是否打码";
            head[4] = "是否特殊";
            head[5] = "是否精品";
            head[6] = "是否广宣";
            head[7] = "是否有效";

            List<Map<String, Object>> list = dao.partSalesBaseList(request);
            List list1 = new ArrayList();
            if (list != null && list.size() != 0) {
                for (int i = 0; i < list.size(); i++) {
                    Map map = (Map) list.get(i);
                    if (map != null && map.size() != 0) {
                        String[] detail = new String[10];
                        detail[0] = CommonUtils.checkNull(map
                                .get("PART_OLDCODE"));
                        detail[1] = CommonUtils.checkNull(map
                                .get("PART_CNAME"));
                        detail[2] = CommonUtils.checkNull(map
                                .get("PART_CODE"));

                        //int isPlanValue = 0;
                        //int isLackValue = 0;
                        //int packState = 0;
                        int ofFlagValue = 0;
                        int isSpcialValue = 0;
                        int stateValue = 0;
                        int isDirectValue = 0;
                        int isGxValue = 0;
                        /*if (null != map.get("IS_PLAN") && !"".equals(map.get("IS_PLAN"))) {
                            isPlanValue = Integer.parseInt(map.get("IS_PLAN").toString());
                        }
                        if (null != map.get("IS_LACK") && !"".equals(map.get("IS_LACK"))) {
                            isLackValue = Integer.parseInt(map.get("IS_LACK").toString());
                        }*/
                        if (null != map.get("OF_FLAG") && !"".equals(map.get("OF_FLAG"))) {
                            ofFlagValue = Integer.parseInt(map.get("OF_FLAG").toString());
                        }
                       /* if (null != map.get("PACK_STATE") && !"".equals(map.get("PACK_STATE"))) {
                            packState = Integer.parseInt(map.get("PACK_STATE").toString());
                        }*/
                        if (null != map.get("IS_SPECIAL") && !"".equals(map.get("IS_SPECIAL"))) {
                            isSpcialValue = Integer.parseInt(map.get("IS_SPECIAL").toString());
                        }
                        if (null != map.get("STATE") && !"".equals(map.get("STATE"))) {
                            stateValue = Integer.parseInt(map.get("STATE").toString());
                        }

                        if (null != map.get("IS_DIRECT") && !"".equals(map.get("IS_DIRECT"))) {
                            isDirectValue = Integer.parseInt(map.get("IS_DIRECT").toString());
                        }
                        if (null != map.get("IS_GXP") && !"".equals(map.get("IS_GXP"))) {
                            isGxValue = Integer.parseInt(map.get("IS_GXP").toString());
                        }

                        /*if (Constant.PART_PACK_STATE_01.intValue() == packState) {
                            detail[3] = "不限";
                        } else {
                            detail[3] = "不可空运";
                        }

                        if (Constant.IF_TYPE_YES.intValue() == isPlanValue) {
                            detail[4] = "是";
                        } else {
                            detail[4] = "否";
                        }
                        if (Constant.IF_TYPE_YES.intValue() == isLackValue) {
                            detail[5] = "是";
                        } else {
                            detail[5] = "否";
                        }*/
                        if (Constant.IF_TYPE_YES.intValue() == ofFlagValue) {
                            detail[3] = "是";
                        } else {
                            detail[3] = "否";
                        }
                        if (Constant.IF_TYPE_YES.intValue() == isSpcialValue) {
                            detail[4] = "是";
                        } else {
                            detail[4] = "否";
                        }

                        if (Constant.IF_TYPE_YES.intValue() == isDirectValue) {
                            detail[5] = "是";
                        } else {
                            detail[5] = "否";
                        }
                        if (Constant.IF_TYPE_YES.intValue() == isGxValue) {
                            detail[6] = "是";
                        } else {
                            detail[6] = "否";
                        }
                        if (Constant.STATUS_ENABLE.intValue() == stateValue) {
                            detail[7] = "有效";
                        } else {
                            detail[7] = "无效";
                        }
                        list1.add(detail);
                    }
                }
            }

            String fileName = "配件采购属性维护信息.xls";
            this.exportEx(fileName, ActionContext.getContext().getResponse(),
                    request, head, list1);

        } catch (Exception e) {
            BizException e1 = new BizException(act, e,
                    ErrorCodeConstant.SPECIAL_MEG, "导出配件采购属性Excel");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    public void detailHis() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            String partId = CommonUtils.checkNull(request.getParamValue("partId"));
//            List<Map<String, Object>> list = dao.getBaseHis(partId);
//            act.setOutData("list", list);
            act.setOutData("partId", partId);
            act.setForword(this.PartHisQueryUrl);
        } catch (Exception e) {
            if (e instanceof BizException) {
                BizException e1 = (BizException) e;
                logger.error(loginUser, e1);
                act.setException(e1);
                act.setForword(PART_DLR_ORDER);
                return;
            }
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "配件销售提报查询数据错误,请联系管理员!");
            logger.error(loginUser, e1);
            act.setException(e1);
            act.setForword(PART_DLR_ORDER);
        }
    }
    
    /**
     * <p>Description: 获取配件主数据修改记录</p>
     */
    public void detailHisJson() {
        ActionContext act = ActionContext.getContext();
        RequestWrapper request = act.getRequest();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            String partId = CommonUtils.checkNull(request.getParamValue("partId"));
            Integer curPage = request.getParamValue("curPage") != null ? Integer
                    .parseInt(request.getParamValue("curPage"))
                    : 1; // 处理当前页
            PageResult<Map<String, Object>> ps = null;
            ps = dao.getBaseHisListPage(request, curPage, Constant.PAGE_SIZE, partId);
            act.setOutData("ps", ps);
//            act.setOutData("sortList", dao.getSortList());
        } catch (Exception e) {
            if (e instanceof BizException) {
                BizException e1 = (BizException) e;
                logger.error(loginUser, e1);
                act.setException(e1);
                act.setForword(PART_DLR_ORDER);
                return;
            }
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "配件销售提报查询数据错误,请联系管理员!");
            logger.error(loginUser, e1);
            act.setException(e1);
            act.setForword(PART_DLR_ORDER);
        }
    }
    
    
}

