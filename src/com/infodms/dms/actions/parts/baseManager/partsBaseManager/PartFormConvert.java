package com.infodms.dms.actions.parts.baseManager.partsBaseManager;

import com.infodms.dms.actions.sales.planmanage.PlanUtil.BaseImport;
import com.infodms.dms.bean.AclUserBean;
import com.infodms.dms.bean.OrgBean;
import com.infodms.dms.common.Constant;
import com.infodms.dms.common.ErrorCodeConstant;
import com.infodms.dms.common.Utility;
import com.infodms.dms.dao.parts.baseManager.partExceptionQueryManger.PartExceptionQueryDao;
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
public class PartFormConvert extends BaseImport {
    private final String PART_LOCATION_INIT_URL = "/jsp/parts/baseManager/partsBaseManager/partLocation/partFormConvert.jsp"; // 配件货位维护初始化页面
    private final String PART_LOCATION_CONVERT_URL = "/jsp/parts/baseManager/partsBaseManager/partLocation/partFormConvert2.jsp"; // 配件货位维护初始化页面
    private final String PART_LOCATION_RECORD_URL = "/jsp/parts/baseManager/partsBaseManager/partLocation/partFormConvertRecord.jsp"; // 配件货位维护初始化页面
    public Logger logger = Logger.getLogger(PartFormConvert.class);

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
            String part_oldcode = CommonUtils.checkNull(request.getParamValue("PART_CODE"));
            String part_name = CommonUtils.checkNull(request.getParamValue("PART_NAME"));
            String TstartDate = CommonUtils.checkNull(request.getParamValue("TstartDate"));
            String TendDate = CommonUtils.checkNull(request.getParamValue("TendDate"));
            String whId = CommonUtils.checkNull(request.getParamValue("WH_ID"));
            String flag = CommonUtils.checkNull(request.getParamValue("FLAG"));

            Map<String, Object> map = new HashMap<String, Object>();
            map.put("LOC_CODE", loc_code);
            map.put("PART_OLDCODE", part_oldcode);
            map.put("PART_NAME", part_name);
            map.put("TstartDate", TstartDate);
            map.put("TendDate", TendDate);
            map.put("whId", whId);
            long orgId = PartWareHouseDao.getInstance().getOrgInfo(logonUser).get(0).getOrgId();
            map.put("ORG_ID", orgId);
            map.put("CONFIG_ID", Constant.PART_CODE_RELATION_55);

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

    public void jumpConvert() {
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            String partId = CommonUtils.checkNull(request.getParamValue("partId"));
            String whId = CommonUtils.checkNull(request.getParamValue("whId"));
            long orgId = PartWareHouseDao.getInstance().getOrgInfo(logonUser).get(0).getOrgId();
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("whId", whId);
            map.put("partId", partId);
            map.put("ORG_ID", orgId);
            PageResult<Map<String, Object>> ps = dao.getQueryData(map, 1, Constant.PAGE_SIZE);
            Map<String, Object> mapData = ps.getRecords().get(0);
            act.setOutData("map", mapData);
            act.setForword(PART_LOCATION_CONVERT_URL);
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "配件形态转换页面初始化");
            logger.error(logonUser, e1);
            act.setException(e1);
        }

    }

    public void convert() {
        String error = "";
        String success = "";
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        String orgId = PartWareHouseDao.getInstance().getOrgInfo(logonUser).get(0).getOrgId() + "";
        try {
            int myIndex = Integer.valueOf(request.getParamValue("myIndex"));
            String type = request.getParamValue("TYPE");
            //总成件信息
            String partId = request.getParamValue("PART_ID");
            String partCode = request.getParamValue("PART_CODE");
            String partOldcode = request.getParamValue("PART_OLDCODE");
            String partCname = request.getParamValue("PART_CNAME");
            String whId = request.getParamValue("WH_ID");
            String whName = request.getParamValue("WH_NAME");
            String item_qty = request.getParamValue("ITEM_QTY");
            String normal_qty = request.getParamValue("NORMAL_QTY");
            String loc_code = request.getParamValue("LOC_CODE");
            Map<String, Object> mapZong = new HashMap<String, Object>();
            mapZong.put("PART_ID", partId);
            mapZong.put("PART_CODE", partCode);
            mapZong.put("PART_OLDCODE", partOldcode);
            mapZong.put("PART_CNAME", partCname);
            mapZong.put("WH_ID", whId);
            // 1入库  2出库
            if (type.equals("2")) {//拆分总成件
                partOutWarehouse(mapZong, error);
            } else if (type.equals("1")) {//合成总成件
                partIntoWarehouse(mapZong);
            }
            //分总成件信息
            for (int i = 1; i <= myIndex; i++) {
                String subpart_id = request.getParamValue("SUBPART_ID" + i);
                String subpart_code = request.getParamValue("SUBPART_CODE" + i);
                String subpart_oldcode = request.getParamValue("SUBPART_OLDCODE" + i);
                String subpart_cname = request.getParamValue("SUBPART_CNAME" + i);
                String split_num = request.getParamValue("SPLIT_NUM" + i);
                String loc_id = request.getParamValue("LOC_ID_" + i);
                String remark = request.getParamValue("REMARK" + i);
                Map<String, Object> mapFeng = new HashMap<String, Object>();
                mapFeng.put("PART_ID", subpart_id);
                mapFeng.put("PART_CODE", subpart_code);
                mapFeng.put("PART_OLDCODE", subpart_oldcode);
                mapFeng.put("PART_CNAME", subpart_cname);
                mapFeng.put("WH_ID", whId);
                if (type.equals("2")) {
                    partIntoWarehouse(mapFeng);
                } else if (type.equals("1")) {
                    partOutWarehouse(mapFeng, error);
                }
            }
        } catch (Exception e) {// 异常方法
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "配件形态转换");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    private void partOutWarehouse(Map<String, Object> map, String error) throws Exception {
        String partId = map.get("PART_ID").toString();
        String partCode = map.get("PART_CODE").toString();
        String partOldcode = map.get("PART_OLDCODE").toString();
        String partCname = map.get("PART_CNAME").toString();
        String whId = map.get("WH_ID").toString();
        String locId = map.get("LOC_ID").toString();
        String loc_code = map.get("LOC_CODE").toString();
        String locName = map.get("LOC_NAME").toString();
        String moveNum = map.get("MOVE_NUM").toString();

        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        String orgId = PartWareHouseDao.getInstance().getOrgInfo(logonUser).get(0).getOrgId() + "";
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
            List<Map<String, Object>> list = Misc_exManagerDAO.getInstance().checkItemQty(partId, whId, orgId, locId, "");
            if (list != null && list.size() > 0 && list.get(0) != null) {
                String itemQty = list.get(0).get("ITEM_QTY").toString();//账面库存
                String normalQty = list.get(0).get("NORMAL_QTY").toString();//可用库存
                if (Integer.parseInt(itemQty) < Integer.parseInt(moveNum) || Integer.parseInt(normalQty) < Integer.parseInt(moveNum)) {
                    error = "配件[" + partCode + "]移位数量大于现存库数量！";
                    BizException e1 = new BizException(act, new RuntimeException(), ErrorCodeConstant.SPECIAL_MEG, error);
                    throw e1;
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
//            String partBatch = Constant.PART_RECORD_BATCH;
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
            insertPRPo.setLocCode(loc_code);
            insertPRPo.setLocName(locName);
            insertPRPo.setOptDate(date);
            insertPRPo.setCreateDate(date);
            insertPRPo.setPersonId(logonUser.getUserId());
            insertPRPo.setPersonName(logonUser.getName());
            insertPRPo.setPartState(1);
            dao.insert(insertPRPo);

            TtPartStockChgRecordPO chgRecordPO = new TtPartStockChgRecordPO();
            chgRecordPO.setRecId(recId);
            chgRecordPO.setConfigId(Constant.PART_CODE_RELATION_55);//移位
            chgRecordPO.setPartId(Long.parseLong(partId));
            chgRecordPO.setPartOldcode(partOldcode);
            chgRecordPO.setPartCname(partCname);
            chgRecordPO.setPartCode(partCode);
            chgRecordPO.setItemQty(Long.valueOf(list2.get(0).get("ITEM_QTY").toString()));
            chgRecordPO.setPartNum(Long.parseLong(moveNum));
            chgRecordPO.setLocId(Long.parseLong(locId));
            chgRecordPO.setLocCode(loc_code);
            chgRecordPO.setWhId(Long.parseLong(whId));

            //二、逻辑出库--2.调用出库逻辑
            List<Object> ins = new LinkedList<Object>();
            ins.add(0, changeId);
            ins.add(1, configId);
            ins.add(2, 0);//先前未占用
            dao.callProcedure("PKG_PART.P_UPDATEPARTSTOCK", ins, null);
        }
    }

    private void partIntoWarehouse(Map<String, Object> map) throws Exception {
        String partId = map.get("PART_ID").toString();
        String partCode = map.get("PART_CODE").toString();
        String partOldcode = map.get("PART_OLDCODE").toString();
        String partCname = map.get("PART_CNAME").toString();
        String whId = map.get("WH_ID").toString();
        String loc_id = map.get("LOC_ID").toString();
        String relocId = map.get("RELOC_ID").toString();
        String loc_code = map.get("LOC_CODE").toString();
        String locName = map.get("LOC_NAME").toString();
        String moveNum = map.get("MOVE_NUM").toString();
        String remark = map.get("REMARK").toString();

        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        String orgId = PartWareHouseDao.getInstance().getOrgInfo(logonUser).get(0).getOrgId() + "";
        //二、逻辑入库--1.检查主机厂tt_part_loaction_define 表 获取loc_id
        if (logonUser.getDealerId() == null) {
            List<Map<String, Object>> listLocId = PartDistributeMgrDao.getInstance().getLocId(partId, relocId, whId);
            if (listLocId != null && listLocId.size() > 0 && listLocId.get(0).get("LOC_ID") != null) {
                loc_id = listLocId.get(0).get("LOC_ID").toString();
            } else {
                TtPartLoactionDefinePO dp = new TtPartLoactionDefinePO();
                loc_id = CommonUtils.parseLong(SequenceManager.getSequence("")) + "";
                dp.setLocId(Long.parseLong(loc_id));
                dp.setRelocId(Long.valueOf(relocId));
                dp.setLocCode(loc_code);
                dp.setLocName(locName);
                dp.setPartId(Long.parseLong(partId));
                dp.setWhId(Long.parseLong(whId));
                dp.setOrgId(logonUser.getOrgId());
                dp.setCreateBy(logonUser.getUserId());
                dp.setCreateDate(new Date());
                dp.setState(Constant.STATUS_ENABLE);
                dao.insert(dp);
            }
        } else {
            loc_id = OrderCodeManager.getPartLocId(orgId, whId, partId) + "";
        }
        //二、逻辑入库--2.插入 tt_part_record 表
        Long InId = CommonUtils.parseLong(SequenceManager.getSequence(""));
        Map<String, Object> map2 = new HashMap<String, Object>();
        map2.put("PART_NUM", moveNum);
        map2.put("PART_ID", partId);
        map2.put("PART_CODE", partCode);
        map2.put("PART_OLDCODE", partOldcode);
        map2.put("PART_NAME", partCname);
        map2.put("WH_ID", whId);
        map2.put("CONFIG_ID", Constant.PART_CODE_RELATION_47);
        map2.put("LOC_ID", loc_id);
        map2.put("LOC_CODE", loc_code);
        map2.put("LOC_NAME", locName);
        this.insertRecord(InId, 1, map);

        TtPartStockChgRecordPO chgRecordPO = new TtPartStockChgRecordPO();
        chgRecordPO.setTopartId(Long.valueOf(partId));
        chgRecordPO.setTopartCname(partCname);
        chgRecordPO.setTopartOldcode(partOldcode);
        chgRecordPO.setTopartCode(partCode);
        chgRecordPO.setToitemQty(0l);
        chgRecordPO.setTopartNum(Long.parseLong(moveNum));
        chgRecordPO.setTolocId(Long.parseLong(loc_id));
        chgRecordPO.setTolocCode(loc_code);
        chgRecordPO.setRemark(remark);
        chgRecordPO.setCreateBy(logonUser.getUserId());
        chgRecordPO.setTowhId(Long.parseLong(whId));
        chgRecordPO.setOrgId(Long.parseLong(orgId));

        //二、逻辑入库--3.调用PKG_PART.p_updatepartstock进行入库
        ArrayList<Object> inss = new ArrayList<Object>();
        inss.add(0, InId);
        inss.add(1, Constant.PART_CODE_RELATION_47);
        inss.add(2, 1);
        dao.callProcedure("PKG_PART.p_updatepartstock", inss, null);
    }


    public void moveSeat() {
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        RequestWrapper req = act.getRequest();
        String error = "";
        String success = "";
        try {
            /***获取参数****/
            String partId = CommonUtils.checkNull(req.getParamValue("PART_ID"));
            String partOldcode = CommonUtils.checkNull(req.getParamValue("PART_OLDCODE"));
            String partCname = CommonUtils.checkNull(req.getParamValue("PART_CNAME"));
            String whId = CommonUtils.checkNull(req.getParamValue("WH_ID"));
            String locId = CommonUtils.checkNull(req.getParamValue("LOC_ID")); //移出货位ID
            String locCode = CommonUtils.checkNull(req.getParamValue("LOC_CODE")); //移入货位
            String moveNum = CommonUtils.checkNull(req.getParamValue("MOVE_NUM")); //移位数量
            String convertCode = CommonUtils.checkNull(req.getParamValue("convertCode")); //转换编码
            String orgId = PartWareHouseDao.getInstance().getOrgInfo(logonUser).get(0).getOrgId() + "";

            // -- 验证形态转换的配件编码是否存在
            TtPartDefinePO pdp = new TtPartDefinePO();
            pdp.setPartOldcode(convertCode);
            List<PO> listPdp = dao.select(pdp);
            if (listPdp != null && listPdp.size() > 0) {
                pdp = (TtPartDefinePO) listPdp.get(0);
            } else {
                error = "形态转换配件[" + convertCode + "] 不存在！";
                BizException e1 = new BizException(act, new RuntimeException(), ErrorCodeConstant.SPECIAL_MEG, error);
                throw e1;
            }
            List<Map<String, Object>> vmPartStock = dao.getVm_part_stock(locId, partId, orgId);
            TtPartLocationPO tplp = new TtPartLocationPO();
            tplp.setLocCode(locCode);
            List<PO> list1 = dao.select(tplp);
            tplp = (TtPartLocationPO) list1.get(0);

            String relocId = tplp.getLocId() + "";
            String locName = vmPartStock.get(0).get("LOC_NAME").toString();
            String loc_code = vmPartStock.get(0).get("LOC_CODE").toString();
            String partCode = vmPartStock.get(0).get("PART_CODE").toString();
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
                List<Map<String, Object>> list = Misc_exManagerDAO.getInstance().checkItemQty(partId, whId, orgId, locId, "");
                if (list != null && list.size() > 0 && list.get(0) != null) {
                    String itemQty = list.get(0).get("ITEM_QTY").toString();//账面库存
                    String normalQty = list.get(0).get("NORMAL_QTY").toString();//可用库存
                    if (Integer.parseInt(itemQty) < Integer.parseInt(moveNum) || Integer.parseInt(normalQty) < Integer.parseInt(moveNum)) {
                        error = "配件[" + partCode + "]移位数量大于现存库数量！";
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
//              String partBatch = Constant.PART_RECORD_BATCH;
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
                insertPRPo.setLocCode(loc_code);
                insertPRPo.setLocName(locName);
                insertPRPo.setOptDate(date);
                insertPRPo.setCreateDate(date);
                insertPRPo.setPersonId(logonUser.getUserId());
                insertPRPo.setPersonName(logonUser.getName());
                insertPRPo.setPartState(1);
                dao.insert(insertPRPo);

                chgRecordPO.setRecId(recId);
                chgRecordPO.setRecCode(orderCode);
                chgRecordPO.setConfigId(Constant.PART_CODE_RELATION_55);//移位
                chgRecordPO.setPartId(Long.parseLong(partId));
                chgRecordPO.setPartOldcode(partOldcode);
                chgRecordPO.setPartCname(partCname);
                chgRecordPO.setPartCode(partCode);
                chgRecordPO.setItemQty(Long.valueOf(list2.get(0).get("ITEM_QTY").toString()));
                chgRecordPO.setPartNum(Long.parseLong(moveNum));
                chgRecordPO.setLocId(Long.parseLong(locId));
                chgRecordPO.setLocCode(loc_code);
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
                partId = pdp.getPartId() + "";
                if (logonUser.getDealerId() == null) {
                    List<Map<String, Object>> listLocId = PartDistributeMgrDao.getInstance().getLocId(partId, relocId, whId);
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
                        dp.setWhId(Long.parseLong(whId));
                        dp.setOrgId(logonUser.getOrgId());
                        dp.setCreateBy(logonUser.getUserId());
                        dp.setCreateDate(new Date());
                        dp.setState(Constant.STATUS_ENABLE);
                        dao.insert(dp);
                    }
                } else {
                    loc_id = OrderCodeManager.getPartLocId(orgId, whId, partId) + "";
                }
                //二、逻辑入库--2.插入 tt_part_record 表
                Long InId = CommonUtils.parseLong(SequenceManager.getSequence(""));
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("PART_NUM", moveNum);
                map.put("PART_ID", pdp.getPartId());
                map.put("PART_CODE", pdp.getPartCode());
                map.put("PART_OLDCODE", pdp.getPartOldcode());
                map.put("PART_NAME", pdp.getPartCname());
                map.put("WH_ID", whId);
                map.put("CONFIG_ID", Constant.PART_CODE_RELATION_47);
                map.put("LOC_ID", loc_id);
                map.put("LOC_CODE", tplp.getLocCode());
                map.put("LOC_NAME", tplp.getLocName());
                this.insertRecord(InId, 1, map);
                //增加移入时库存的计算
                List<Map<String, Object>> list3 = dao.getItem_qty(pdp.getPartId() + "", whId, loc_id);
                if (list3.size() != 0) {
                    chgRecordPO.setToitemQty(Long.valueOf(list3.get(0).get("ITEM_QTY") + ""));
                } else {
                    chgRecordPO.setToitemQty(0l);
                }
                chgRecordPO.setTopartId(pdp.getPartId());
                chgRecordPO.setTopartCname(pdp.getPartCname());
                chgRecordPO.setTopartOldcode(pdp.getPartOldcode());
                chgRecordPO.setTopartCode(pdp.getPartCode());
                chgRecordPO.setTopartNum(Long.parseLong(moveNum));
                chgRecordPO.setTolocId(Long.parseLong(loc_id));
                chgRecordPO.setTolocCode(tplp.getLocCode());
                chgRecordPO.setRemark("");
                chgRecordPO.setCreateBy(logonUser.getUserId());
                chgRecordPO.setTowhId(Long.parseLong(whId));
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
                dao.insert(chgRecordPO);//插入形态转化历史

                success = "配件：" + partCode + " --> " + convertCode + " 形态转换成功!";
            }
            act.setOutData("error", error);// 返回错误信息
            act.setOutData("success", success);
        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "配件形态转换");
            logger.error(logonUser, e1);
            act.setException(e1);
        }

    }

    private void insertRecord(Long InId, int addFlag, Map<String, Object> map) throws Exception {
        String part_num = map.get("PART_NUM").toString();
        String part_id = map.get("PART_ID").toString();
        String part_code = map.get("PART_CODE").toString();
        String part_oldcode = map.get("PART_OLDCODE").toString();
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
        insertPRPo.setPartOldcode(CommonUtils.checkNull(part_oldcode));
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

    public void partExceptionQuerySelect() {
        ActionContext act = ActionContext.getContext();
        AclUserBean logonUser = (AclUserBean) act.getSession().get(Constant.LOGON_USER);
        try {
            RequestWrapper request = act.getRequest();
            String id = CommonUtils.checkNull(request.getParamValue("ID"));
            act.setOutData("ID", id);
            act.getResponse().setContentType("application/json");
            if ("1".equals(request.getParamValue("query"))) { // 开始查询
                Map<String, String> paramMap = new HashMap<String, String>();
                paramMap.put("partOlcode", request.getParamValue("partolcode")); // 配件编码
                paramMap.put("partCName", request.getParamValue("partcname")); // 配件名称
                paramMap.put("partOldId", id);
                
                Integer curPage = request.getParamValue("curPage") != null ? Integer.parseInt(request.getParamValue("curPage")) : 1; // 处理当前页
                PartExceptionQueryDao dao = PartExceptionQueryDao.getInsttance();
                PageResult<Map<String, Object>> ps = dao.getPartOLCode(12, curPage, paramMap);
                act.setOutData("ps", ps);
            } else {
                act.setForword("/jsp/parts/baseManager/partExceptionQueryManger/partSelectSingle3.jsp");
            }
        } catch (Exception e) {
            BizException e1 = new BizException(act, e, ErrorCodeConstant.QUERY_FAILURE_CODE, "配件编码选项查询(1)");
            logger.error(logonUser, e1);
            act.setException(e1);
        }
    }

    public void checkPartExists() {
        String part_oldCode = CommonUtils.checkId(request.getParamValue("PART_OLDCODE")).trim();
        String locId = CommonUtils.checkId(request.getParamValue("locId")).trim();
        TtPartDefinePO definePO = new TtPartDefinePO();
        definePO.setPartOldcode(part_oldCode);

        if (dao.select(definePO).size() > 0) {
            definePO = (TtPartDefinePO) dao.select(definePO).get(0);
            act.setOutData("FLAG", 1);
            act.setOutData("PART_OLDCODE", definePO.getPartOldcode());
            act.setOutData("PART_CNAME", definePO.getPartCname());
            act.setOutData("PART_CODE", definePO.getPartCode());

        } else {
            act.setOutData("FLAG", 0);
            act.setOutData("PART_OLDCODE", part_oldCode);

        }
        act.setOutData("locId", locId);
    }
}
