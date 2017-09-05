package com.infodms.dms.actions.parts.salesManager.carFactorySalesManager;

import com.infodms.dms.actions.sales.planmanage.PlanUtil.BaseImport;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.bean.OrgBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.constants.PTConstants;
import com.infodms.dms.dao.parts.baseManager.partsBaseManager.PartWareHouseDao;
import com.infodms.dms.dao.parts.purchaseManager.purchasePlanSetting.PurchasePlanSettingDao;
import com.infodms.dms.dao.parts.salesManager.*;
import com.infodms.dms.exception.BizException;
import com.infodms.dms.po.*;
import com.infodms.dms.util.CommonUtils;
import com.infodms.dms.util.OrderCodeManager;
import com.infodms.dms.util.sequenceUitl.SequenceManager;
import com.infoservice.mvc.context.ActionContext;
import com.infoservice.mvc.context.RequestWrapper;
import com.infoservice.po3.bean.PO;
import com.infoservice.po3.bean.PageResult;
import org.apache.log4j.Logger;

import java.util.*;

/**
 * 配件发运计划
 * @see 
 * @since 
 * @deprecated
 */
public class PartTransPlan extends BaseImport implements PTConstants {
	
    public Logger logger = Logger.getLogger(PartTransPlan.class);
    
    public PartTransPlanDao dao = PartTransPlanDao.getInstance();

    /**
     * 发运计划-箱号选择页面
     */
    private final static String PKG_NO_SELECT_URL = "/jsp/parts/salesManager/carFactorySalesManager/transplan/pkgNoSelect.jsp";


    /**
     * 发运计划 初始化
     * @param :
     * @return :
     * @throws : LastDate    : 2013-4-18
     * @Title : PartDlrOrderDao
     * @Description: 出库单初始化
     */
    public void init() {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        try {
            String flag = CommonUtils.checkNull(request.getParamValue("flag"));
//            List<TtPartFixcodeDefinePO> listf = CommonUtils.getPartUnitList(Constant.FIXCODE_TYPE_06);// 发运类型
//            List<TtPartFixcodeDefinePO> listc = CommonUtils.getPartUnitList(Constant.FIXCODE_TYPE_08);// 承运物流
        	List<Map<String, Object>> listf = dao.getTransportType();// 发运类型
        	List<Map<String, Object>> listc = dao.getLogiType();// 承运物流
            
            String orgId = "";
            PartWareHouseDao partWareHouseDao = PartWareHouseDao.getInstance();
            List<OrgBean> beanList = partWareHouseDao.getOrgInfo(loginUser);
            if (null != beanList && beanList.size() >= 0) {
                orgId = beanList.get(0).getOrgId() + "";
            }
            PurchasePlanSettingDao purchasePlanSettingDao = PurchasePlanSettingDao.getInstance();
            List<Map<String, Object>> wareHouseList = purchasePlanSettingDao.getWareHouse(orgId);
            List<TtPartFixcodeDefinePO> list = CommonUtils.getPartUnitList(Constant.FIXCODE_TYPE_04);// 获取配件发运方式
            request.setAttribute("wareHouseList", wareHouseList);
            act.setOutData("transList", list);
            act.setOutData("old", CommonUtils.getBefore(new Date()));
            act.setOutData("now", CommonUtils.getDate());
            act.setOutData("listf", listf);
            act.setOutData("listc", listc);
            if ("pickPrint".equals(flag)) {
                act.setForword(PART_TRANS_PLAN_PRINT);
            } else {
                act.setForword(PART_TRANS_PLAN_MAIN);
            }

        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "出库单初始化错误,请联系管理员!");
            logger.error(loginUser, e1);
            act.setException(e1);
        }
    }

    /**
     * 生成发运计划
     * 箱号筛选(目前只有未随车箱号，这边无随车)
     */
    public void pkgNoSelect() {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        try {
            String pickOrderIds = CommonUtils.checkNull(request.getParamValue("pickOrderId"));
            String pickOrderId = pickOrderIds.substring(0, pickOrderIds.length() - 1);
            
            String flag = CommonUtils.checkNull(request.getParamValue("flag"));
            //目前无随车说法
//            List<Map<String, Object>> list = dao.getYSuiChe(pickOrderId);//已随车箱号
//            act.setOutData("list", list);
//
            List<Map<String, Object>> list1 = dao.getWSuiChe(pickOrderId);//未随车箱号
            act.setOutData("list1", list1);

            act.setOutData("flag", flag);//如果为1就是在打印装箱单时弹出的页面
            act.setForword(PKG_NO_SELECT_URL);
            
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "出库单初始化错误,请联系管理员!");
            logger.error(loginUser, e1);
            act.setException(e1);
        }
    }

    /**
     *  配件发运单查询
     * @param :
     * @return :
     * @throws : LastDate    : 2013-4-18
     * @Title :
     * @Description:
     */
    public void partOutstockQuery() {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        try {
            String type = CommonUtils.checkNull(request.getParamValue("queryType"));
            //分页方法 beginfin
            Integer curPage = request.getParamValue("curPage") != null ? Integer
                    .parseInt(request.getParamValue("curPage")): 1; // 处理当前页
            PageResult<Map<String, Object>> ps = null;
            
            if (type.equals("normal")) {
            	//查询发运单
                ps = dao.queryPkgOrder(request, curPage, Constant.PAGE_SIZE_PART_MINI);
            } else if (type.equals("cancel")) {
            	//运单调整查询
//                List<TtPartFixcodeDefinePO> listf = CommonUtils.getPartUnitList(Constant.FIXCODE_TYPE_06);// 发运类型
//                List<TtPartFixcodeDefinePO> listc = CommonUtils.getPartUnitList(Constant.FIXCODE_TYPE_08);// 承运物流
            	List<Map<String, Object>> listf = dao.getTransportType();// 发运类型
            	List<Map<String, Object>> listc = dao.getLogiType();// 承运物流
                
                ps = dao.queryPkgOrder2(request, curPage, Constant.PAGE_SIZE_PART_MINI);
                
                act.setOutData("listf", listf);
                act.setOutData("listc", listc);
            }
            act.setOutData("ps", ps);
        } catch (Exception e) {
            if (e instanceof BizException) {
                BizException e1 = (BizException) e;
                logger.error(loginUser, e1);
                act.setException(e1);
                act.setForword(PART_OUTSTOCK_ORDER_MAIN);
                return;
            }
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "配件销售提报查询数据错误,请联系管理员!");
            logger.error(loginUser, e1);
            act.setException(e1);
            act.setForword(PART_OUTSTOCK_ORDER_MAIN);
        }
    }

    /**
     * 生成发运计划
     * @param :
     * @return :
     * @throws : LastDate    : 2013-4-18
     * @Title :
     * @Description: 
     */
    public void partOutstock() {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        PartPickOrderDao partPickdao = PartPickOrderDao.getInstance();
        PartPkgDao partPkgDao = PartPkgDao.getInstance();
        try {
//            List<TtPartFixcodeDefinePO> listf = CommonUtils.getPartUnitList(Constant.FIXCODE_TYPE_06);// 发运类型
//            List<TtPartFixcodeDefinePO> listc = CommonUtils.getPartUnitList(Constant.FIXCODE_TYPE_08);// 承运物流
        	List<Map<String, Object>> listf = dao.getTransportType();// 发运类型
        	List<Map<String, Object>> listc = dao.getLogiType();// 承运物流
        	
            String pickOrderIds = CommonUtils.checkNull(request.getParamValue("pickOrderId"));
            String pickOrderId = pickOrderIds.substring(0, pickOrderIds.length() - 1);
            String pkgNos = CommonUtils.checkNull(request.getParamValue("pkgNos"));

            List<Map<String, Object>> soOrderList = partPickdao.getSoMainList(pickOrderId);
            Map<String, Object> mainMap = soOrderList.get(0);

            List<Map<String, Object>> addrList = partPickdao.getDealerAddrList(mainMap.get("DEALER_ID") + "");

            String whId = CommonUtils.checkNull(mainMap.get("WH_ID"));
            String whName = partPkgDao.getWhName(whId);
            List<Map<String, Object>> list = partPickdao.getPartGroup(pickOrderId, whId, pkgNos);
            List<Map<String, Object>> detailList = new ArrayList<Map<String, Object>>();

            mainMap.put("whName", whName);
            for (int i = 0; i < list.size(); i++) {
                Map<String, Object> map = list.get(i);
                int pkgedQty = Integer.valueOf(list.get(i).get("PKGEDQTY") == null ? "0" : CommonUtils.checkNull(list.get(i).get("PKGEDQTY")));
                int salesQty = Integer.valueOf(list.get(i).get("SALES_QTY") == null ? "0" : CommonUtils.checkNull(list.get(i).get("SALES_QTY")));
                map.put("boQty", (salesQty - pkgedQty));
                detailList.add(map);
            }

            Map<String, Object> transMap = this.getTransMap();
            String transType = CommonUtils.checkNull(mainMap.get("TRANS_TYPE"));
            transType = CommonUtils.checkNull(transMap.get(transType));
            String boxNos = partPickdao.getPkgNOs(pkgNos);
            mainMap.put("TRANS_TYPE", transType);
            mainMap.put("boxAllNo", boxNos.split(",").length);
            act.setOutData("mainMap", mainMap);
            act.setOutData("addrList", addrList);

            act.setOutData("detailList", detailList);
            act.setOutData("listf", listf);
            act.setOutData("listc", listc);
            act.setOutData("pkgNos", boxNos);//箱号踢重后
            act.setOutData("boxIds", pkgNos);//箱号IDs
            act.setOutData("pickOrderIds", pickOrderId);//箱号IDs
            act.setOutData("CREATE_BY_NAME", loginUser.getName());
            act.setForword(PART_TRANSPLAN_ORDER);
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "配件出库初始化错误,请联系管理员!");
            logger.error(loginUser, e1);
            act.setException(e1);
        }
    }

    public Map<String, Object> getTransMap() throws Exception {
        List<TtPartFixcodeDefinePO> list = CommonUtils.getPartUnitList(Constant.FIXCODE_TYPE_04);
        Map<String, Object> transMap = new HashMap<String, Object>();
        for (TtPartFixcodeDefinePO po : list) {
            //list.fixValue }">${list.fixName
            transMap.put(po.getFixValue(), po.getFixName());
        }
        return transMap;
    }

    /**
     * 取消计划发运单
     *
     * @author wucl
     * @date 2014-3-27
     */
    public void cancelTransPlan() {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        PartOutstockDao dao = PartOutstockDao.getInstance();
        String errorMsg = "";
        String successMsg = "";
        try {
            String trplanId = CommonUtils.checkNull(request.getParamValue("trplanId"));

            //1.校验是否已经出库
            TtPartOutstockMainPO tposmp = new TtPartOutstockMainPO();
            tposmp.setTrplanId(Long.valueOf(trplanId));
            List<PO> list1 = dao.select(tposmp);
            if (list1 != null && list1.size() > 0 && list1.get(0) != null) {
                act.setOutData("errorMsg", "此发运计划已经出库，不能取消！");
                return;
            }
            //2.解除发运计划和箱子关系
            dao.updatePkgBoxTransInfo(trplanId);
            //3. 删除发运计划主表和明细
            TtPartTransPlanPO tptpp = new TtPartTransPlanPO();
            tptpp.setTrplanId(Long.valueOf(trplanId));
            dao.delete(tptpp);
            TtPartTransPlanDtlPO tptpdp = new TtPartTransPlanDtlPO();
            tptpdp.setTrplanId(Long.valueOf(trplanId));
            dao.delete(tptpdp);
            successMsg = "取消发运计划单成功";
            act.setOutData("successMsg", successMsg);
        } catch (Exception e) {

            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "发运单已出库，不允许取消！");
            logger.error(loginUser, e1);
            act.setException(e1);

        }

    }

    /**
     * 生成发运计划单
     * @param :
     * @return :
     * @throws : LastDate    : 2013-4-18
     * @Title :
     * @Description: 
     */
    public void createPartOutstockOrder() {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        String success = "";
        String error = "";
        try {
            String pkgNos = CommonUtils.checkNull(request.getParamValue("pkgNos"));
            String pickOrderId = CommonUtils.checkNull(request.getParamValue("pickOrderId"));
            String transType = CommonUtils.checkNull(request.getParamValue("transType"));//发运方式
            String transportOrg = CommonUtils.checkNull(request.getParamValue("transportOrg"));//承运物流
            String remark = CommonUtils.checkNull(request.getParamValue("remark"));
            String dealerId = CommonUtils.checkNull(request.getParamValue("DEALER_ID"));//订货单位
            String dealerCode = CommonUtils.checkNull(request.getParamValue("DEALER_CODE"));
            String dealerName = CommonUtils.checkNull(request.getParamValue("DEALER_NAME"));
            String sellerId = CommonUtils.checkNull(request.getParamValue("SELLER_ID"));//销售单位
//            String sellerCode = CommonUtils.checkNull(request.getParamValue("SELLER_CODE"));
//            String sellerName = CommonUtils.checkNull(request.getParamValue("SELLER_NAME"));
            String addrId = CommonUtils.checkNull(request.getParamValue("addrId"));
            String boxIds = CommonUtils.checkNull(request.getParamValue("boxIds"));//箱号IDs
            String printFlag = CommonUtils.checkNull(request.getParamValue("printFlag"));//弹出运单打印界面标志 1弹出打印界面
            double sumWeigth=0;//箱子总计费重量
            //生成发运计划单编码
            String trplanCode = OrderCodeManager.getOrderCode(Constant.PART_CODE_RELATION_48);
            
            //1.插入 tt_part_trans_plan 表
            TtPartTransPlanPO tptpp = new TtPartTransPlanPO();
            Long trplanId = Long.parseLong(SequenceManager.getSequence(""));
            tptpp.setTrplanId(trplanId);
            tptpp.setTrplanCode(trplanCode);
//            tptpp.setPickOrderId(Long.valueOf(pickOrderId));
            tptpp.setDealerId(Long.valueOf(dealerId));
            tptpp.setDealerCode(dealerCode);
            tptpp.setDealerName(dealerName);
            tptpp.setSellerId(Long.valueOf(sellerId));
            tptpp.setTransType(transType);
            tptpp.setTransportOrg(transportOrg);
            tptpp.setCreateBy(loginUser.getUserId());
            tptpp.setCreateDate(new Date());
            tptpp.setState(Constant.STATUS_ENABLE);
            tptpp.setStatus(1);
            tptpp.setAddrId(Long.valueOf(addrId));
            dao.insert(tptpp);
            
            //2.插入 tt_part_trans_plan_dtl 表
            String[] strs = boxIds.split(",");
            for (int i = 0; i < strs.length; i++) {
                //查询箱号信息
                String pkgNo = "";//箱号
                String boxId = "";//箱号ID
                String pickId = ""; //拣货单ID
                TtPartPkgBoxDtlPO pkgBoxDtlPO = new TtPartPkgBoxDtlPO();
                pkgBoxDtlPO.setBoxId(Long.valueOf(strs[i]));
                pkgBoxDtlPO = (TtPartPkgBoxDtlPO) dao.select(pkgBoxDtlPO).get(0);

                if (pkgBoxDtlPO.getBoxId() != null) {
                    pkgNo = pkgBoxDtlPO.getPkgNo();
                    boxId = pkgBoxDtlPO.getBoxId() + "";
                    pickId = pkgBoxDtlPO.getPickOrderId();
                    //箱号重复发运校验
                    if ("1".equals(dao.isDupPKG(pickId, pkgNo))) {
                        error = "箱号:【" + pkgNo + "】重复生成发运计划!请返回重新确认！";
                        BizException e1 = new BizException(act, ErrorCodeConstant.SPECIAL_MEG, "箱号:【" + pkgNo + "】重复生成发运计划!请返回重新确认！");
                        throw e1;
                    }
                } else {
                    error = "箱号:【" + pkgNo + "】不存在!请返回重新确认！";
                    BizException e1 = new BizException(act, ErrorCodeConstant.SPECIAL_MEG, "箱号:【" + pkgNo + "】不存在!请返回重新确认！");
                    throw e1;
                }

                TtPartTransPlanDtlPO tptpdp = new TtPartTransPlanDtlPO();
                Long trplineId = Long.parseLong(SequenceManager.getSequence(""));
                tptpdp.setTrplineId(trplineId);
                tptpdp.setTrplanId(trplanId);
                tptpdp.setPickOrderId(Long.valueOf(pickId));
                tptpdp.setPkgNo(pkgNo);
                tptpdp.setRemark(remark);
                tptpdp.setCreateBy(loginUser.getUserId());
                tptpdp.setCreateDate(new Date());
                tptpdp.setStatus(1);

                dao.insert(tptpdp);

                //3.更新 tt_part_pkg_box_dtl与发运计划对应关系
                TtPartPkgBoxDtlPO tppbdp1 = new TtPartPkgBoxDtlPO();
                tppbdp1.setBoxId(Long.valueOf(boxId));
                TtPartPkgBoxDtlPO tppbdp2 = new TtPartPkgBoxDtlPO();
                tppbdp2.setTrplanId(trplanId);
                dao.update(tppbdp1, tppbdp2);
                
                //4.生成发运计划运费重新计算,返回计费重量
                double ch_weight=this.updateFreight(boxId, transType, transportOrg);
                sumWeigth=CommonUtils.add(sumWeigth,ch_weight);
            }
            //计算发运单预估运费并更新 20170804 add
            estimatedFreight(act,transType,transportOrg,addrId,sumWeigth,trplanId,dealerId,pickOrderId);
            
            
            //5.插入配件操作记录
            List<Map<String, Object>> rs = PartTransPlanDao.getInstance().query4History(pickOrderId);
            for (Map<String, Object> m : rs) {
                String bussinessId = m.get("SO_CODE").toString();
                long orderId = Long.valueOf(m.get("ORDER_ID") == null ? "0" : m.get("ORDER_ID").toString());
                this.insertPartHistory(bussinessId, "已生成发运计划", Constant.PART_ORDER_STATE_02, Constant.PART_OPERATION_TYPE_02, "无", orderId);
            }
            
            success = "生成发运计划单成功!";
            act.setOutData("success", success);
            act.setOutData("error", error);
            act.setOutData("trplanId", trplanId);
            act.setOutData("pickOrderId", pickOrderId);
            act.setOutData("pkgNos", pkgNos);
            act.setOutData("printFlag", printFlag);

        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, e.getMessage());
            logger.error(loginUser, e1);
            act.setException(e1);
        }
    }

    /**
     *  计算发运单预估运费
     * @param act
     * @param transType 发运方式
     * @param transportOrg 承运商
     * @param addrId 订货单位地址id
     * @param sumWeigth 货物总重量
     * @param trplanId 发运计划id
     * @param dealerId 订货单位id
     * @throws BizException
     */
    public void estimatedFreight(ActionContext act,String transType,String transportOrg,
    		String addrId,double sumWeigth,Long trplanId,String dealerId,String pickOrderId) throws BizException{
    	 double sumPrice=0;
    	 String error="";
         //1、根据销售id,订货id,承运商、运输方式确认价格，如果批次计价就是一批的价格，如果是重量计价就是总重量*单价
         Map<String,Object> sellerMap=dao.getSellerAddr(pickOrderId);
         if(sellerMap==null && sellerMap.size()<=0){
        	 error = "对不起，未查找到销售单位地址信息!!!";
             BizException e1 = new BizException(act, ErrorCodeConstant.SPECIAL_MEG, error);
             throw e1;
        }
         //2、根据订单单位id找到省、市、县的id
         Map<String,Object> dealerMap=dao.getDealerAddr(dealerId,addrId);
         if(dealerMap==null && dealerMap.size()<=0){
         	 error = "对不起，未查找到订货单位地址信息!!!";
             BizException e1 = new BizException(act, ErrorCodeConstant.SPECIAL_MEG, error);
             throw e1;
        }
         //3、获取单价
         double price=dao.getPrice(sellerMap,dealerMap,transType,transportOrg);
         if(price==0){
         	  error = "未查找到该发运方式和承运商的价格";
              BizException e1 = new BizException(act, ErrorCodeConstant.SPECIAL_MEG, error);
              throw e1;
         }
         //4、获取计价方式  15011001重量  15011002批次 
         String valutionType =dao.getValutionType(transType);
         if(valutionType.equals("15011001")){
        	//重量计费：重量*单价
         	sumPrice=CommonUtils.multiply(sumWeigth,price);
         }else{
         	//批次
         	sumPrice=price;
         }
         //5、 更新tt_part_trans_plan表的预估运费
         TtPartTransPlanPO oldPlanPo = new TtPartTransPlanPO();
         oldPlanPo.setTrplanId(trplanId);
         TtPartTransPlanPO newPlanPo = new TtPartTransPlanPO();
         newPlanPo.setTransSumprice(sumPrice);
         dao.update(oldPlanPo, newPlanPo);
    }

    /**
     * 更新装箱信息上面的计费信息
     * @param boxId        箱子ID
     * @param transType    发运方式ID
     * @param transportOrg 承运物流ID
     * @return
     */
    public Double updateFreight(String boxId, String transType, String transportOrg) {
//    	boolean flag=false;
        double ch_weight = 0;
        
        //装箱包装TT_PART_PKG_BOX_DTL表条件
        TtPartPkgBoxDtlPO ppbdp1 = new TtPartPkgBoxDtlPO();
        ppbdp1.setBoxId(Long.valueOf(boxId));
        
        List<PO> list = dao.select(ppbdp1);
        
        if (list != null && list.size() > 0 && list.get(0) != null) {
            TtPartPkgBoxDtlPO ppbdp = (TtPartPkgBoxDtlPO) list.get(0);
            double length = ppbdp.getLength();//长
            double width = ppbdp.getWeight();//宽
            double heigth = ppbdp.getHeight();//高
            double weight = ppbdp.getWeight();//单箱重量
	        //承运商折合重量计算系数
            int coefficient = this.getCoefficient(transType, transportOrg);
            //折合重量（EQ_WEIGHT） = 长*宽*高/6000(跟承运商有关系，默认是6000)
            double eqWeight = length * width * heigth / coefficient;
            //计费重量（CH_WEIGHT） = 跟箱子长、宽、高有关，三边都大于60CM 取折合重量和单箱重量取最大值。
            // 三边有任一边小于等于60CM，取单箱重量。
            ch_weight = 0;
            if (length > 60 && width > 60 && heigth > 60) {
                ch_weight = eqWeight > weight ? eqWeight : weight;
            } else {
                ch_weight = weight;
            }
            
            //TT_PART_PKG_BOX_DTL 运费重新计算
            TtPartPkgBoxDtlPO ppbdp2 = new TtPartPkgBoxDtlPO();
            ppbdp2.setEqWeight(eqWeight);//折合重量（Equivalent_weight）
            ppbdp2.setChWeight(ch_weight);//计费重量（Charged_weight）
            int updateNum = dao.update(ppbdp1, ppbdp2);
//            flag = updateNum > 0 ? true : false;
        }
        return ch_weight;
    }

    /**
     * @param transType    发运方式
     * @param transportOrg 承运物流
     * @return 返回承运物流商的系数
     * @author wucl
     * @date 2014-4-1
     */
    private int getCoefficient(String transType, String transportOrg) {
        int coefficient = 6000; //默认承运物流商系数为6000

        return coefficient;
    }

    /**
     * 更新发运方式
     */
    public void updatePartTransInfo() {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        try {
            PartDlrOrderDao dao = PartDlrOrderDao.getInstance();

            String transType = CommonUtils.checkNull(request.getParamValue("transType"));
            String transOrg = CommonUtils.checkNull(request.getParamValue("transportOrg"));

            String[] params = request.getParamValues("ck");
            for (int i = 0, len = params.length; i < len; i++) {
                String planId = params[i].split(",")[0];
                dao.updatePartTransType(planId, transType, transOrg);
            }
            act.setOutData("successMsg", "发运方式调整成功!");
        } catch (Exception e) {//异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "发运方式调整失败,请联系管理员!");
            logger.error(loginUser, e1);
            act.setException(e1);
        }
    }
    

    /**
     * 插入配件操作记录
     *
     * @param bussinessId 业务ID
     * @param what        操作说明
     * @param status      操作状态
     * @param optType     操作类型
     * @param remark      备注说明
     * @param orderId     订单ID
     * @author wucl
     * @date 2014-4-9
     */
    public static void insertPartHistory(String bussinessId, String what, int status, int optType, String remark, Long orderId) {
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        long userId = loginUser.getUserId();
        String name = loginUser.getName();
        Long optId = Long.parseLong(SequenceManager.getSequence(""));

        TtPartOperationHistoryPO po = new TtPartOperationHistoryPO();
        po.setOptId(optId);
        po.setBussinessId(bussinessId);
        po.setOptBy(userId);
        po.setOptName(name);
        po.setOptDate(new Date());
        po.setWhat(what);
        po.setStatus(status);
        po.setOptType(optType);
        po.setRemark(remark);
        po.setOrderId(orderId);

        PartTransPlanDao.getInstance().insert(po);
    }
}
