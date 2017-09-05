package com.infodms.dms.actions.parts.baseManager.partsBaseManager;

import com.infodms.dms.actions.sales.planmanage.PlanUtil.BaseImport;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.bean.OrgBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.dao.parts.baseManager.partsBaseManager.PartMoveSeatDao;
import com.infodms.dms.dao.parts.baseManager.partsBaseManager.PartWareHouseDao;
import com.infodms.dms.dao.parts.salesManager.PartOutstockDao;
import com.infodms.dms.dao.parts.storageManager.miscManager.MiscManagerDAO;
import com.infodms.dms.dao.parts.storageManager.miscManager.Misc_exManagerDAO;
import com.infodms.dms.dao.parts.storageManager.partDistributeMgr.PartDistributeMgrDao;
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
 * @author : luole CreateDate : 2013-4-3
 * @ClassName : PartLocation
 * @Description : 配件货位维护
 */
public class PartMoveSeat extends BaseImport {
    private final String PART_LOCATION_INIT_URL = "/jsp/parts/baseManager/partsBaseManager/partLocation/partMoveSeat.jsp"; // 配件货位维护初始化页面
    private final String PART_LOCATION_RECORD_URL = "/jsp/parts/baseManager/partsBaseManager/partLocation/partMoveSeatRecord.jsp"; // 配件货位维护初始化页面
    private final String PART_MOVE_SEAT_PRINT = "/jsp/parts/baseManager/partsBaseManager/partLocation/partMoveSeatPrint.jsp";//配件移库单打印页面

    public Logger logger = Logger.getLogger(PartMoveSeat.class);

    PartMoveSeatDao dao = PartMoveSeatDao.getInstance();
    private ActionContext act = ActionContext.getContext();
    RequestWrapper request = act.getRequest();

    /**
     * @param :
     * @return :
     * @throws : luole LastDate : 2013-4-3
     * @Title : 初始化
     */
    public void init() {
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        long orgId = 0;
        String flag = CommonUtils.checkNull(request.getParamValue("FLAG")).equals("2") ? "2" : "1";
        try {
            if (logonUser.getDealerId() == null) {
                orgId = logonUser.getOrgId();
            } else {
                orgId = new Long(logonUser.getDealerId());
            }
            List<Map<String, Object>> list = dao.getPartWareHouse(orgId);
            act.setOutData("list", list);
            act.setOutData("orgId", orgId);
            act.setOutData("FLAG", flag);
            if ("1".equals(flag)) {
                act.setForword(PART_LOCATION_INIT_URL);
            } else {
                act.setForword(PART_LOCATION_RECORD_URL);
            }

        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "配件货位移位页面初始化");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    /**
     * @param :
     * @return :
     * @throws : luole LastDate : 2013-4-3
     * @Title : 查询
     */
    public void query() {
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            String loc_code = CommonUtils.checkNull(request.getParamValue("LOC_CODE"));
            String part_code = CommonUtils.checkNull(request.getParamValue("PART_CODE"));
            String part_name = CommonUtils.checkNull(request.getParamValue("PART_NAME"));
            String TstartDate = CommonUtils.checkNull(request.getParamValue("TstartDate"));
            String TendDate = CommonUtils.checkNull(request.getParamValue("TendDate"));
            String whId = CommonUtils.checkNull(request.getParamValue("WH_ID"));
            String flag = CommonUtils.checkNull(request.getParamValue("FLAG"));

            Map<String, Object> map = new HashMap<String, Object>();
            map.put("LOC_CODE", loc_code);
            map.put("PART_OLDCODE", part_code);
            map.put("PART_NAME", part_name);
            map.put("TstartDate", TstartDate);
            map.put("TendDate", TendDate);
            map.put("whId", whId);
            long orgId = PartWareHouseDao.getInstance().getOrgInfo(logonUser).get(0).getOrgId();
            map.put("ORG_ID", orgId);
            map.put("CONFIG_ID", Constant.PART_CODE_RELATION_54);

            Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1;
            PageResult<Map<String, Object>> ps = null;
            if ("1".equals(flag)) {
                ps = dao.getQueryData(map, curPage, Constant.PAGE_SIZE);
            } else {
                ps = dao.getQueryRecord(map, curPage, Constant.PAGE_SIZE);
            }
            act.setOutData("ps", ps);
        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "配件移位--数据信息查询失败");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    public void moveSeat() {
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper req = act.getRequest();
        String error = "";
        String success = "";
        try {
            /***获取参数****/
            String partId = CommonUtils.checkNull(req.getParamValue("PART_ID"));
            String partOldcode = CommonUtils.checkNull(req.getParamValue("PARTOLDCODE")).trim();
            String partCname = CommonUtils.checkNull(req.getParamValue("PART_CNAME"));
            String whId = CommonUtils.checkNull(req.getParamValue("WH_ID"));
            String locId = CommonUtils.checkNull(req.getParamValue("LOC_ID"));//移出货位ID
            String locCode = CommonUtils.checkNull(req.getParamValue("LOC_CODE"));//移入货位
            String moveNum = CommonUtils.checkNull(req.getParamValue("MOVE_NUM")); //移位数量
            String yRwhId = CommonUtils.checkNull(req.getParamValue("yRwhId")); //移入库房
            String orgId = PartWareHouseDao.getInstance().getOrgInfo(logonUser).get(0).getOrgId() + "";

            List<Map<String, Object>> vmPartStock = dao.getVm_part_stock(locId, partId, orgId);
            TtPartLocationPO tplp = new TtPartLocationPO();
            tplp.setLocCode(locCode);
            List<PO> list1 = dao.select(tplp);
            tplp = (TtPartLocationPO) list1.get(0);

            String relocId = tplp.getLocId() + "";
            String locName = vmPartStock.get(0).get("LOC_NAME").toString();//移出货位名称
            String loc_code2 = vmPartStock.get(0).get("LOC_CODE").toString();//移出货位编码
            String partCode = vmPartStock.get(0).get("PART_CODE").toString();//配件件号
            String orderCode = OrderCodeManager.getOrderCode(Constant.PART_CODE_RELATION_54);

            TtPartStockChgRecordPO chgRecordPO = new TtPartStockChgRecordPO();

            //一、校验--1.验证出库是否已经锁定
            String sqlStr = " AND VM.PART_ID = '" + partId + "' AND VM.WH_ID = '" + whId + "' ";
            List<Map<String, Object>> partList = MiscManagerDAO.getInstance().checkPartState(sqlStr);
            if (null != partList && partList.size() == 1) {
                int isLocked = Integer.parseInt(partList.get(0).get("IS_LOCKED").toString());//是否锁定
                if (Constant.PART_STATE_UN_LOCKED != isLocked) {
                    error += "配件：" + partCname + "已锁定,目前不能进行库存操作!<br/>";
                }
            }
            //一、校验--2.判断配件库存是否为0，为0则不需要要移位
            List<Map<String, Object>> list2 = dao.getItem_qty(partId, whId, locId);
            if (list2 != null && list2.size() > 0 && list2.get(0) != null) {
                String itemQty = list2.get(0).get("ITEM_QTY").toString();
                String normalQty = list2.get(0).get("NORMAL_QTY").toString();
                if (Long.parseLong(itemQty) < 0 || Long.parseLong(normalQty) < 0) {
                    error += "库存为零，不需要进行移位！";
                }
            }

            if ("".equals(error)) {
                //一、校验--3.判断出库数量是否正常
                List<Map<String, Object>> list = Misc_exManagerDAO.getInstance().checkItemQty(partId, whId, orgId, locId);
                if (list != null && list.size() > 0 && list.get(0) != null) {
                    String itemQty = list.get(0).get("ITEM_QTY").toString();//账面库存
                    String normalQty = list.get(0).get("NORMAL_QTY").toString();//可用库存
                    if (Integer.parseInt(itemQty) < Integer.parseInt(moveNum) || Integer.parseInt(normalQty) < Integer.parseInt(moveNum)) {
                        error = "在您操作保存前，配件【" + partOldcode + "】库存已经发生变化；本次移位数量【" + moveNum + "】已经大于当前库数量【" + itemQty + "】！请刷新页面！";
                        act.setOutData("error", error);
                        act.setOutData("success", success);
                        return;
                    }
                }

                //二、逻辑出库--1.插入Tt_Part_Record表
                StringBuffer sbStr = new StringBuffer();
                if (null != whId && !"".equals(whId)) {
                    sbStr.append(" AND TM.WH_ID = '" + whId + "' ");
                }
                Map<String, Object> mapWH = MiscManagerDAO.getInstance().getWareHouses(sbStr.toString()).get(0);
                String whName = mapWH.get("WH_NAME").toString();
                String whCode = mapWH.get("WH_CODE").toString();
                List<OrgBean> beanList = PartWareHouseDao.getInstance().getOrgInfo(logonUser);
                OrgBean org = beanList.get(0);
                String partBatch = "";
//                String partBatch = Constant.PART_RECORD_BATCH;
                String venderId = Constant.PART_RECORD_VENDER_ID;
                String configId = Constant.PART_CODE_RELATION_46 + "";

                TtPartRecordPO insertPRPo = new TtPartRecordPO();
                Date date = new Date();
                Long changeId = Long.parseLong(SequenceManager.getSequence(""));
                Long dtlId = Long.parseLong(SequenceManager.getSequence(""));
                Long recId = Long.parseLong(SequenceManager.getSequence(""));

                insertPRPo.setRecordId(recId);
                insertPRPo.setAddFlag(2);//出库
                insertPRPo.setPartId(Long.parseLong(partId));
                insertPRPo.setPartCode(partCode);
                insertPRPo.setPartOldcode(partOldcode);
                insertPRPo.setPartName(partCname);
                insertPRPo.setPartBatch(partBatch);
                insertPRPo.setVenderId(Long.parseLong(venderId));
                insertPRPo.setPartNum(Long.parseLong(moveNum));//出库数量
                insertPRPo.setConfigId(Long.valueOf(configId));
                insertPRPo.setOrderId(changeId);//业务ID
                insertPRPo.setLineId(dtlId);//行ID
                insertPRPo.setOrgId(Long.parseLong(orgId));
                insertPRPo.setOrgCode(org.getOrgCode());
                insertPRPo.setOrgName(org.getOrgName());
                insertPRPo.setWhId(Long.parseLong(whId));
                insertPRPo.setWhCode(whCode);
                insertPRPo.setWhName(whName);
                insertPRPo.setLocId(Long.parseLong(locId));
                insertPRPo.setLocCode(loc_code2);
                insertPRPo.setLocName(locName);
                insertPRPo.setOptDate(date);
                insertPRPo.setCreateDate(date);
                insertPRPo.setPersonId(logonUser.getUserId());
                insertPRPo.setPersonName(logonUser.getName());
                insertPRPo.setPartState(1);
                dao.insert(insertPRPo);

                chgRecordPO.setRecId(recId);
                chgRecordPO.setRecCode(orderCode);
                chgRecordPO.setConfigId(Constant.PART_CODE_RELATION_54);//移位
                chgRecordPO.setPartId(Long.parseLong(partId));
                chgRecordPO.setPartOldcode(partOldcode);
                chgRecordPO.setPartCname(partCname);
                chgRecordPO.setPartCode(partCode);
                chgRecordPO.setItemQty(Long.valueOf(list2.get(0).get("ITEM_QTY").toString()));
                chgRecordPO.setPartNum(Long.parseLong(moveNum));
                chgRecordPO.setLocId(Long.parseLong(locId));
                chgRecordPO.setLocCode(loc_code2);
                chgRecordPO.setWhId(Long.parseLong(whId));

                //二、逻辑出库--2.调用出库逻辑
                List<Object> ins = new LinkedList<Object>();
                ins.add(0, changeId);
                ins.add(1, configId);
                ins.add(2, 0);//先前未占用
                dao.callProcedure("PKG_PART.P_UPDATEPARTSTOCK", ins, null);

                /******************************************************************/
                String loc_id = "";
                //二、逻辑入库--1.检查主机厂tt_part_loaction_define 表 获取loc_id
                if (logonUser.getDealerId() == null) {
                    List<Map<String, Object>> listLocId = PartDistributeMgrDao.getInstance().getLocId(partId, relocId, yRwhId);
                    if (listLocId != null && listLocId.size() > 0 && listLocId.get(0).get("LOC_ID") != null) {
                        loc_id = listLocId.get(0).get("LOC_ID").toString();
                    } else {
                        TtPartLoactionDefinePO dp = new TtPartLoactionDefinePO();
                        loc_id = CommonUtils.parseLong(SequenceManager.getSequence("")) + "";
                        dp.setLocId(Long.parseLong(loc_id));
                        dp.setRelocId(Long.valueOf(relocId));
                        dp.setLocCode(tplp.getLocCode());
                        dp.setLocName(tplp.getLocName());
                        dp.setPartId(Long.parseLong(partId));
                        dp.setWhId(Long.parseLong(yRwhId));
                        dp.setOrgId(logonUser.getOrgId());
                        dp.setCreateBy(logonUser.getUserId());
                        dp.setCreateDate(new Date());
                        dp.setState(Constant.STATUS_ENABLE);
                        dao.insert(dp);
                    }
                } else {
                    loc_id = OrderCodeManager.getPartLocId(orgId, yRwhId, partId) + "";
                }
                //二、逻辑入库--2.插入 tt_part_record 表
                Long InId = CommonUtils.parseLong(SequenceManager.getSequence(""));
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("PART_NUM", moveNum);
                map.put("PART_ID", partId);
                map.put("PART_CODE", partCode);
                map.put("PART_NAME", partCname);
                map.put("WH_ID", yRwhId);//移入库房
                map.put("CONFIG_ID", Constant.PART_CODE_RELATION_47);
                map.put("LOC_ID", loc_id);//移入货位
                map.put("LOC_CODE", tplp.getLocCode());
                map.put("LOC_NAME", tplp.getLocName());
                this.insertRecord(InId, 1, map);
                //增加移入时库存的计算
                List<Map<String, Object>> list3 = dao.getItem_qty(partId, yRwhId, loc_id);
                if (list3.size() != 0) {
                    chgRecordPO.setToitemQty(Long.valueOf(list3.get(0).get("ITEM_QTY") + ""));
                } else {
                    chgRecordPO.setToitemQty(0l);
                }

                chgRecordPO.setTopartId(Long.parseLong(partId));
                chgRecordPO.setTopartCname(partCname);
                chgRecordPO.setTopartOldcode(partOldcode);
                chgRecordPO.setTopartCode(partCode);
                chgRecordPO.setTopartNum(Long.parseLong(moveNum));
                chgRecordPO.setTolocId(Long.parseLong(loc_id));
                chgRecordPO.setTolocCode(tplp.getLocCode());
                chgRecordPO.setRemark("");
                chgRecordPO.setCreateBy(logonUser.getUserId());
                chgRecordPO.setTowhId(Long.parseLong(yRwhId));
                chgRecordPO.setOrgId(Long.parseLong(orgId));

                //二、逻辑入库--3.调用PKG_PART.p_updatepartstock进行入库
                ArrayList<Object> inss = new ArrayList<Object>();
                inss.add(0, InId);
                inss.add(1, Constant.PART_CODE_RELATION_47);
                inss.add(2, 1);
                dao.callProcedure("PKG_PART.p_updatepartstock", inss, null);

                //三、插入修改历史
                TtPartLoactionDefinePO tldp = new TtPartLoactionDefinePO();
                tldp.setLocId(Long.parseLong(locId));
                List<PO> tldpList = dao.select(tldp);
                tldp = (TtPartLoactionDefinePO) tldpList.get(0);
                TtPartLoactionHistoryPO lhPo = new TtPartLoactionHistoryPO();
                lhPo.setHsId(Long.parseLong(SequenceManager.getSequence("")));
                lhPo.setLocId(Long.parseLong(locId));
                lhPo.setLocCode(locCode);
                lhPo.setOldLocCode(tldp.getLocCode());
                lhPo.setLocName(tldp.getLocName());
                lhPo.setOldLocName(tldp.getLocName());
                lhPo.setWhId(tldp.getWhId());
                lhPo.setOrgId(tldp.getOrgId());
                lhPo.setPartId(tldp.getPartId());
                lhPo.setWhmanId(tldp.getWhmanId());
                lhPo.setOldWhmanId(tldp.getWhmanId());
                lhPo.setCreateBy(logonUser.getUserId());
                lhPo.setCreateDate(new Date());
                dao.insert(lhPo);
                dao.insert(chgRecordPO);//插入移位历史信息
                success = "配件：" + partCname + " 移位成功!";
            }
            act.setOutData("error", error);// 返回错误信息
            act.setOutData("success", success);
        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "配件移位");
            logger.error(logonUser, e1);
            act.setException(e1);
        }

    }

    private void insertRecord(Long InId, int addFlag, Map<String, Object> map) throws Exception {
        String part_num = map.get("PART_NUM").toString();
        String part_id = map.get("PART_ID").toString();
        String part_code = map.get("PART_CODE").toString();
        String part_name = map.get("PART_NAME").toString();
        String whId = map.get("WH_ID").toString();
        String configId = map.get("CONFIG_ID").toString();
        String loc_id = map.get("LOC_ID").toString();
        String loc_code = map.get("LOC_CODE").toString();
        String loc_name = map.get("LOC_NAME").toString();

        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        TtPartRecordPO insertPRPo = new TtPartRecordPO();
        Long recId = Long.parseLong(SequenceManager.getSequence(""));
        insertPRPo.setRecordId(recId);
        insertPRPo.setAddFlag(addFlag);
        insertPRPo.setPartId(Long.parseLong(part_id));
        insertPRPo.setPartCode(CommonUtils.checkNull(part_code));
        insertPRPo.setPartOldcode(CommonUtils.checkNull(part_code));
        insertPRPo.setPartName(CommonUtils.checkNull(part_name));
//        insertPRPo.setPartBatch(Constant.PART_RECORD_BATCH);
        insertPRPo.setVenderId(Long.parseLong(Constant.PART_RECORD_VENDER_ID));
        insertPRPo.setPartNum(Long.parseLong(part_num));
        insertPRPo.setConfigId(Long.parseLong(configId));
        insertPRPo.setOrderId(InId);//业务ID
        insertPRPo.setOrgId(Long.parseLong(Constant.OEM_ACTIVITIES));
        insertPRPo.setOrgCode(Constant.ORG_ROOT_CODE);
        insertPRPo.setWhId(Long.valueOf(whId));
        Map<String, Object> warehouseMap = PartOutstockDao.getInstance().getWarehouse(whId);
        insertPRPo.setWhCode(CommonUtils.checkNull(warehouseMap.get("WH_CODE")));
        insertPRPo.setWhName(CommonUtils.checkNull(warehouseMap.get("WH_NAME")));
        insertPRPo.setLocId(Long.parseLong(loc_id));
        insertPRPo.setLocCode(CommonUtils.checkNull(loc_code));
        insertPRPo.setLocName(CommonUtils.checkNull(loc_name));
        insertPRPo.setOptDate(new Date());
        insertPRPo.setCreateDate(new Date());
        insertPRPo.setPersonId(Long.valueOf(loginUser.getUserId()));
        insertPRPo.setPersonName(loginUser.getName());
        insertPRPo.setPartState(1);//配件状态
        dao.insert(insertPRPo);
    }


    /**
     * @param :
     * @return :
     * @throws : LastDate    : 2013-12-23
     * @Title : 打印配件移库单
     */
    public void partMoveSeatPrint() {
        System.out.println("进入打印");
        ActionContext act = ActionContext.getContext();
        AclUserBean loginUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper request = act.getRequest();
        PartMoveSeatDao partMoveSeatDao = PartMoveSeatDao.getInstance();
        try {

            //制单单位ID
            int config_id = Constant.PART_CODE_RELATION_54;

            int indexNO = 1;
            Map<String, Object> dataMap = new HashMap<String, Object>();
            //5.获取装箱单表头信息
            List<Map<String, Object>> soMainList = partMoveSeatDao.getPartMoveSeatPrint(request, config_id);//上部出库单信息
            if (soMainList != null) {
                String org_name = "";
                String create_date = "";
                String wh_name = "";
                String wh_name_2 = "";
                String remark = "";

                for (int i = 0; i < soMainList.size(); i++) {
                    Map<String, Object> mainMap = soMainList.get(i);
                    org_name = CommonUtils.checkNull(mainMap.get("ORG_NAME"));
                    create_date = CommonUtils.checkNull(mainMap.get("CREATE_DATE"));
                    wh_name = CommonUtils.checkNull(mainMap.get("WH_NAME"));
                    wh_name_2 = CommonUtils.checkNull(mainMap.get("WH_NAME2"));
                    remark = CommonUtils.checkNull(mainMap.get("REMARK"));
                }
                dataMap.put("org_name", org_name);
                dataMap.put("create_date", create_date);
                dataMap.put("wh_name", wh_name);
                dataMap.put("wh_name_2", wh_name_2);
                dataMap.put("remark", remark);
                //获取出库单明细条目
                //List<Map<String, Object>> miscDetailList = miscPrintDao.getMainPrintList(request, orgId);

                //获取本单合计数量
                List<Map<String, Object>> sumList = partMoveSeatDao.getPartMoveSeatPrintSum(request, config_id);


                List<Map<String, Object>> partList = new ArrayList<Map<String, Object>>();
                for (int k = 0; k < soMainList.size(); k++) {//indexNO
                    Map<String, Object> row = soMainList.get(k);
                    row.put("indexNO", indexNO);
                    partList.add(row);
                    indexNO++;
                }

                act.setOutData("partList", partList);
                act.setOutData("listSize", (10 - partList.size()));
                act.setOutData("sumInQty", sumList.get(0));
            }
            indexNO = 1;
            act.setOutData("dataMap", dataMap);
            act.setForword(PART_MOVE_SEAT_PRINT);
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.SPECIAL_MEG, "配件移位单打印错误,请联系管理员!");
            logger.error(loginUser, e1);
            act.setException(e1);
        }
    }
}
