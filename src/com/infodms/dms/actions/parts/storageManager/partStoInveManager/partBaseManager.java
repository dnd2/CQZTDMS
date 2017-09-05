package com.infodms.dms.actions.parts.storageManager.partStoInveManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.infodms.dms.actions.sales.planmanage.PlanUtil.BaseImport;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.dao.parts.baseManager.PartBaseQueryDao;
import com.infodms.dms.dao.parts.baseManager.partsBaseManager.PartFixcodeDAO;
import com.infodms.dms.dao.parts.purchaseManager.purchasePlanSetting.PurchasePlanSettingDao;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.TtPartBrandCartypePO;
import com.infodms.dms.po.TtPartBuyPricePO;
import com.infodms.dms.po.TtPartDefineHistoryPO;
import com.infodms.dms.po.TtPartDefinePO;
import com.infodms.dms.po.TtPartFixcodeDefinePO;
import com.infodms.dms.po.TtPartPlanDefinePO;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PageResult;

/**
 * @author : bianlz
 *         CreateDate     : 2013-4-1
 * @ClassName : PartBaseQuery
 * @Description : 
 */
public class partBaseManager extends BaseImport {
    public static Logger logger = Logger.getLogger(partBaseManager.class);
    PartBaseQueryDao dao = PartBaseQueryDao.getInstance();
    String PartBaseQueryUrl = "/jsp/parts/storageManager/partStoInveManager/partBaseManager/PartBaseQuery.jsp"; //配件主信息维护查询
    String PartBaseQueryDetailUrl = "/jsp/parts/storageManager/partStoInveManager/partBaseManager/PartBaseView.jsp"; //配件主信息维护详细
    String PartBaseQueryEditUrl = "/jsp/parts/storageManager/partStoInveManager/partBaseManager/PartBaseMod.jsp"; //配件主信息维护修改
    String PartBaseQueryAddUrl = "/jsp/parts/storageManager/partStoInveManager/partBaseManager/partBaseAdd.jsp"; //配件主信息维护新增

    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-4-1
     * @Title : 配件主信息维护初始化
     * @Description: 
     */
    public void partBaseQueryInit() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            PurchasePlanSettingDao dao2 = PurchasePlanSettingDao.getInstance();
            List<Map<String, Object>> planerList = dao2.getUserPoseLise(1, null);
            act.setForword(PartBaseQueryUrl);
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
     * @throws : LastDate    : 2013-4-2
     * @Title : 配件主信息维护信息查询
     * @Description: 
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
     * @Description: 
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
//            Map<String, String> map = new HashMap<String, String>();
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
     * @Description: 
     */
    @SuppressWarnings("unchecked")
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
            String isPermanent = CommonUtils.checkNull(req.getParamValue("IS_PERMANENT"));//是否常备
            String isReplaced = CommonUtils.checkNull(req.getParamValue("IS_REPLACED"));//是否已替代
            String produceFac = CommonUtils.checkNull(req.getParamValue("PRODUCE_FAC"));// 采购方式
            String partMaterial = CommonUtils.checkNull(req.getParamValue("PART_MATERIAL"));//材料属性
            String produceState = CommonUtils.checkNull(req.getParamValue("PRODUCE_STATE"));//配件种类
            String uom = CommonUtils.checkNull(req.getParamValue("UOM"));//配件单位
            String position = CommonUtils.checkNull(req.getParamValue("POSITION"));//车型大类
            String width = CommonUtils.checkNull(req.getParamValue("WIDTH"));//宽度
            String height = CommonUtils.checkNull(req.getParamValue("HEIGHT"));//高度
            String weight = CommonUtils.checkNull(req.getParamValue("WEIGHT"));//重量
            String volume = CommonUtils.checkNull(req.getParamValue("VOLUME"));//体积
            String remark = CommonUtils.checkNull(req.getParamValue("REMARK"));//备注
            String seriesId = CommonUtils.checkNull(req.getParamValue("SERIES_ID"));//车系ID
            String seriesName = CommonUtils.checkNull(req.getParamValue("SERIES_NAME"));//车系名称
            String modelId = CommonUtils.checkNull(req.getParamValue("MODEL_ID"));//车型ID
            String modelName = CommonUtils.checkNull(req.getParamValue("MODEL_NAME"));//车型名称
            String modelCode = CommonUtils.checkNull(req.getParamValue("MODEL_CODE"));//车型名称
            String curPage = CommonUtils.checkNull(req.getParamValue("curPage"));//当前页码
            
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
            if (!"".equals(seriesName) && seriesName != null) newPo.setSeriesName(seriesName);
            if (!"".equals(modelName) && modelName != null) newPo.setModelName(modelName);
            if (!"".equals(modelCode) && modelCode != null) newPo.setModelCode(modelCode);
            if (!"".equals(isPermanent) && isPermanent != null) newPo.setIsPermanent(Integer.valueOf(isPermanent));
        	if(!"".equals(isReplaced)&&isReplaced!=null)newPo.setIsReplaced(Integer.valueOf(isReplaced));
            if (!"".equals(produceFac) && produceFac != null) newPo.setProduceFac(Integer.valueOf(produceFac));
            if (!"".equals(partMaterial) && partMaterial != null)
                newPo.setPartMaterial(Integer.valueOf(partMaterial));
            if (!"".equals(produceState) && produceState != null) newPo.setProduceState(Integer.valueOf(produceState));
            if (!"".equals(uom) && uom != null) newPo.setUnit(uom);
            if (!"".equals(position) && position != null) newPo.setPosition(Integer.valueOf(position));
            if (!"".equals(width) && width != null) newPo.setWidth(width);
            if (!"".equals(height) && height != null) newPo.setHeight(height);
            if (!"".equals(weight) && weight != null) newPo.setWeight(weight);
            if (!"".equals(volume) && volume != null) newPo.setVolume(volume);
            if (!"".equals(seriesName) && seriesName != null) newPo.setSeriesName(seriesName);
            newPo.setRemark(remark);
            if (logonUser.getPoseBusType().equals(Constant.POSE_BUS_TYPE_WRD))
                newPo.setPartIsChanghe(Constant.PART_IS_CHANGHE_02); // 艾春10.9添加
            if (!"".equals(seriesId) && seriesId != null) newPo.setSeriesId(seriesId);
            if (!"".equals(modelId) && modelId != null) newPo.setModelId(modelId);
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
            
            newPo.setUpdateBy(logonUser.getUserId());
            newPo.setUpdateDate(new Date());
            dao.update(oldPo, newPo);

            if ("".equals(curPage)) {
                curPage = "1";
            }
            act.setOutData("succeed", "succeed");
            act.setOutData("curPage", curPage);
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
     * @Description: 
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

    
}

